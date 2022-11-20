package com.ohj.glkt.user.api;

import com.alibaba.fastjson.JSON;
import com.ohj.ggkt.model.user.UserInfo;
import com.ohj.glkt.user.service.UserInfoService;
import com.ohj.glkt.utils.jwt.JwtHelper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;

@Api(tags = "微信授权登陆")
@Controller
@RequestMapping("/api/user/wechat")
@Slf4j
public class WeChatController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private WxMpService wxMpService;

    //授权后回调，获取用户信息的接口地址
    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;


    /**
     * 进行授权
     *
     * @param returnUrl 授权后要访问的第三方页面
     * @param request
     * @return
     */
    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        //这行代码有误，路径中并没有guiguketan
//        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
//                WxConsts.OAUTH2_SCOPE_USER_INFO,
//                URLEncoder.encode(returnUrl.replace("guiguketan", "#")));

        /**
         * 第一个参数： 授权成功后回调的网址url 可以用于微信用户的openid的获取并保存到数据库
         * 第二个参数： 设置授权的作用域
         * 第三个参数： 授权之后要跳转的路径
         */

        String redirectURL = wxMpService.oauth2buildAuthorizationUrl(userInfoUrl,
                WxConsts.OAUTH2_SCOPE_USER_INFO,
                URLEncoder.encode(returnUrl));
        return "redirect:" + redirectURL;
    }


    //回调网址，获取
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {

        //根据code获取accessToken
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        //获取openId
        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info("openId：{}", openId);

        //获取微信信息
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }

        log.info("微信信息：   {}", JSON.toJSONString(wxMpUser));

        //以下是业务需求===========================

        //获取微信信息，并添加到数据库中
        //先根据openid判断用户是否是第一次授权
        UserInfo userInfo=userInfoService.getUserInfoOpenid(openId);

        //若不是，就进行保存到数据库中
        if(userInfo==null){
            userInfo = new UserInfo();
            userInfo.setOpenId(openId);
            userInfo.setUnionId(wxMpUser.getUnionId());
            userInfo.setNickName(wxMpUser.getNickname());
            userInfo.setAvatar(wxMpUser.getHeadImgUrl());
            userInfo.setSex(wxMpUser.getSexId());
            userInfo.setProvince(wxMpUser.getProvince());

            userInfoService.save(userInfo);
            userInfo=userInfoService.getUserInfoOpenid(openId);
        }

        //=====================

        //授权完成之后，跳转到具体功能页面
        //生成token，按照一定规则生成字符串，可以包含用户信息
        String token= JwtHelper.createToken(userInfo.getId(),userInfo.getNickName());
        log.info("后端生成了token:  {}",token);
        log.info("要跳转的url: {}",returnUrl);

        //判断跳转到功能页面中是否带参数
        if(returnUrl.indexOf("?")==-1){
            return "redirect:"+returnUrl+"?token="+token;
        }else {
            return "redirect:"+returnUrl+"&token="+token;
        }

    }
}

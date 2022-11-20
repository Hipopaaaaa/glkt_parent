package com.ohj.glkt.wechat.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohj.ggkt.model.wechat.Menu;
import com.ohj.ggkt.vo.wechat.MenuVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.wechat.mapper.MenuMapper;
import com.ohj.glkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */

@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private WxMpService wxMpService;


    //返回一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {

        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId, 0);

        List<Menu> menuList = menuMapper.selectList(menuLambdaQueryWrapper);
        return menuList;
    }


    //返回所有的菜单
    @Override
    public List<MenuVo> findMenuInfo() {


        //查询所有菜单数据（包括一级和二级）
        List<Menu> menuList = menuMapper.selectList(null);


        //从所有菜单数据中获取所有一级菜单数据,并进行数据封装
        List<MenuVo> finallLIst = menuList.stream()
                .filter(oneMenu -> oneMenu.getParentId().longValue() == 0)
                .map(oneMenu -> {
                    //封装最终数据
                    MenuVo menuVo = CopyUtil.copyBean(oneMenu, MenuVo.class);
                    //封装二级菜单的数据
                    List<MenuVo> twoMenuList = menuList.stream()
                            .filter(twoMenu -> twoMenu.getParentId().longValue() == oneMenu.getId())
                            .map(twoMenu -> CopyUtil.copyBean(twoMenu, MenuVo.class))
                            .collect(Collectors.toList());

                    menuVo.setChildren(twoMenuList);
                    return menuVo;
                })
                .collect(Collectors.toList());

        return finallLIst;
    }

    //同步菜单(同步微信公众号的菜单，有一个固定格式)
    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        //菜单
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            JSONArray subButton = new JSONArray();

            //封装subButton 也就是二级菜单
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                view.put("type", twoMenuVo.getType());
                if (twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());

                    //TODO 内网穿透 需要手动设置
                    view.put("url", "http://hipopaaaaqian.gz2vip.91tunnel.com"
                            + twoMenuVo.getUrl());
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                subButton.add(view);
            }
            one.put("sub_button", subButton);
            buttonList.add(one);
        }
        //最外层菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            String menuId = this.wxMpService.getMenuService().menuCreate(button.toJSONString());
            log.info("同步菜单后返回的id：{}",menuId);
        } catch (WxErrorException e) {
            throw new GlktException(20001,"公众号菜单同步失败");
        }
    }

    //公众号菜单的删除
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();

        } catch (WxErrorException e) {
            throw new GlktException(20001,"公众号菜单删除失败");
        }

    }
}
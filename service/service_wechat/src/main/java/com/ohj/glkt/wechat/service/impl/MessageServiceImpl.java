package com.ohj.glkt.wechat.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.glkt.client.course.CourseFeignClient;
import com.ohj.glkt.wechat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private WxMpService wxMpService;

    //接收消息
    @Override
    public String receiveMessage(Map<String, String> param) {
        String content = "";
        try {
            //获取消息的类型
            String msgType = param.get("MsgType");
            switch(msgType){
                case "text" :  //普通文本类型，如输入关键字
                    content = this.search(param);
                    break;
                case "event" : //事件类型 如关注，取消关注，点击
                    String event = param.get("Event");
                    String eventKey = param.get("EventKey");
                    if("subscribe".equals(event)) {//关注公众号
                        content = this.subscribe(param);
                    } else if("unsubscribe".equals(event)) {//取消关注公众号
                        content = this.unsubscribe(param);
                    } else if("CLICK".equals(event) && "aboutUs".equals(eventKey)){
                        content = this.aboutUs(param);
                    } else {
                        content = "success";
                    }
                    break;
                default:
                    content = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            content = this.text(param, "请重新输入关键字，没有匹配到相关内容").toString();
        }
        return content;


    }

    @Override
    public void pushPayMessage(long orderId) {
        //TODO 微信openid  写到微信登陆授权时，这里需要修改
        String openid = "oBwiL6F0zSnefKCgyMKFTL-YG17s";
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId("J5Jp519EhgKNoOwSVSF9fL6x3S7JXX10OuJI0sYmld4")//模板id
                .url("http://hipopaaaaqian.gz2vip.91tunnel.com"+orderId)//点击模板消息要访问的网址
                .build();
        //3,如果是正式版发送消息，，这里需要配置你的信息
        templateMessage.addData(new WxMpTemplateData("first", "亲爱的用户：您有一笔订单支付成功。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", "1314520", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword2", "java基础课程", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword3", "2022-01-11", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword4", "100", "#272727"));
        templateMessage.addData(new WxMpTemplateData("remark", "感谢你购买课程，如有疑问，随时咨询！", "#272727"));
        String msg = null;
        try {
            msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
        log.info("msg:{}",msg);
    }

    /**
     * 关于我们
     * @param param
     * @return
     */
    private String aboutUs(Map<String, String> param) {
        return this.text(param, "Hipopaaaa第一次制作")
                .toString();
    }

    /**
     * 处理关注事件
     * @param param
     * @return
     */
    private String subscribe(Map<String, String> param) {
        //处理业务
        return this.text(param, "来了啊～").toString();
    }

    /**
     * 处理取消关注事件
     * @param param
     * @return
     */
    private String unsubscribe(Map<String, String> param) {
        //处理业务
        return "success";
    }

    /**
     * 处理关键字搜索事件
     * 图文消息个数；当用户发送文本、图片、语音、视频、图文、地理位置这六种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息
     * @param param
     * @return
     */
    private String search(Map<String, String> param) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        String content = param.get("Content");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();

        if(content.equals("我是你爹")){
            text=this.text(param,"sb，去死");
            return text.toString();
        }
        if(content.equals("ohj")){
            text=this.text(param,"区浩锦是真的牛");
            return text.toString();
        }


        List<Course> courseList = courseFeignClient.findByKeyword(content);
        if(CollectionUtils.isEmpty(courseList)) {
            text = this.text(param, "请重新输入关键字，没有匹配到相关内容");
        }else {
            //一次只能返回一个
            Random random = new Random();
            int num = random.nextInt(courseList.size());
            Course course = courseList.get(num);
            StringBuffer articles = new StringBuffer();
            articles.append("<item>");
            articles.append("<Title><![CDATA["+course.getTitle()+"]]></Title>");
            articles.append("<Description><![CDATA["+course.getTitle()+"]]></Description>");
            articles.append("<PicUrl><![CDATA["+course.getCover()+"]]></PicUrl>");
            articles.append("<Url><![CDATA[http://glkt.atguigu.cn/#/liveInfo/"+course.getId()+"]]></Url>");
            articles.append("</item>");

            text.append("<xml>");
            text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
            text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
            text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
            text.append("<MsgType><![CDATA[news]]></MsgType>");
            text.append("<ArticleCount><![CDATA[1]]></ArticleCount>");
            text.append("<Articles>");
            text.append(articles);
            text.append("</Articles>");
            text.append("</xml>");
        }
        return text.toString();
    }

    /**
     * 回复文本
     * @param param
     * @param content
     * @return
     */
    private StringBuffer text(Map<String, String> param, String content) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();
        text.append("<xml>");
        text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
        text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
        text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
        text.append("<MsgType><![CDATA[text]]></MsgType>");
        text.append("<Content><![CDATA["+content+"]]></Content>");
        text.append("</xml>");
        return text;
    }
}

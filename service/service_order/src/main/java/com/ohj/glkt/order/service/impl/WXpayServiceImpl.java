package com.ohj.glkt.order.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.ohj.glkt.order.service.OrderInfoService;
import com.ohj.glkt.order.service.WXpayService;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.utils.order.HttpClientUtils;
import com.ohj.glkt.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXpayServiceImpl implements WXpayService {

    @Autowired
    private OrderInfoService orderInfoService;

    //微信支付
    @Override
    public Map<String, String> createJsapi(String orderNo) {
        //1 封装微信支付需要的参数
        Map<String,String> paramMap=new HashMap();
        // 正式服务器号的id
        paramMap.put("appid", "wxf913bfa3a2c7eeeb");
        // 服务号的商户号
        paramMap.put("mch_id", "1481962542");
        // 每次支付时都需要一个随机数
        paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
        //支付时，弹框的内容
        paramMap.put("body", "test");
        //订单号
        paramMap.put("out_trade_no", orderNo);
        //支付金额
        paramMap.put("total_fee", "1");
        //当前支付，客户端的ip
        paramMap.put("spbill_create_ip", "127.0.0.1");
        //支付后的跳转
        paramMap.put("notify_url", "http://hipopaaaaa.gz2vip.91tunnel.com/api/order/wxPay/notify");
        //支付类型，按照生成的固定金额支付
        paramMap.put("trade_type", "JSAPI");

        /**
         * 下面的内容： 设置当前用户的openid
         * 实现逻辑： 第一步 根据订单号获取userid， 第二步 根据userid获取openid
         *
         * taps： 因为当前使用测试号，测试号不支持支付功能，为了使用正式服务号进行测试，使用下面写法
         *        获取正式服务号的微信openid
         *
         */
        //TODO 关注正式号后能获取到用户的openid，然后进行设置
        paramMap.put("openid", "oQTXC56lAy3xMOCkKCImHtHoLL");




        try {
            //2 通过httpclient去调用微信支付接口 固定路径
            HttpClientUtils client=new HttpClientUtils("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //把要上传的参数设置到请求中（XML格式），设置服务号的商户key
            client.setXmlParam( WXPayUtil.generateSignedXml(paramMap,"MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            //发送请求
            client.post();

            //3 微信支付接口会放回数据(XML格式)
            String xml = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xml);

            // 判断是否成功调用支付接口
            if(null != resultMap.get("result_code")  && !"SUCCESS".equals(resultMap.get("result_code"))) {
                throw new GlktException(20001,"支付失败");
            }

            //4、再次封装参数
            Map<String, String> parameterMap = new HashMap<>();
            String prepayId = String.valueOf(resultMap.get("prepay_id"));
            String packages = "prepay_id=" + prepayId;
            parameterMap.put("appId", "wxf913bfa3a2c7eeeb");
            parameterMap.put("nonceStr", resultMap.get("nonce_str"));
            parameterMap.put("package", packages);
            parameterMap.put("signType", "MD5");
            parameterMap.put("timeStamp", String.valueOf(new Date().getTime()));
            String sign = WXPayUtil.generateSignature(parameterMap, "MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9");

            //返回结果
            Map<String, String> result = new HashMap();
            result.put("appId", "wxf913bfa3a2c7eeeb");
            result.put("timeStamp", parameterMap.get("timeStamp"));
            result.put("nonceStr", parameterMap.get("nonceStr"));
            result.put("signType", "MD5");
            result.put("paySign", sign);
            result.put("package", packages);
            System.out.println(result);
            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //调用微信支付接口，查询订单状态
    @Override
    public Boolean queryPayStatus(String orderNo) {
        Map<String, String> resultMap = null;
        try {
            //1、封装参数
            Map paramMap = new HashMap<>();
            paramMap.put("appid", "wxf913bfa3a2c7eeeb");
            paramMap.put("mch_id", "1481962542");
            //订单流水号
            paramMap.put("out_trade_no", orderNo);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            //2、设置请求
            HttpClientUtils client = new HttpClientUtils("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(paramMap,"MXb72b9RfshXZD4FRGV5KLqmv5bx9LT9"));
            client.setHttps(true);
            client.post();
            //3、返回第三方的数据
            String xml = client.getContent();
            resultMap = WXPayUtil.xmlToMap(xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //判断支付是否成功
        if(resultMap==null){
            throw new GlktException(20001,"支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            System.out.println("out_trade_no:"+out_trade_no);
            orderInfoService.updateOrderStatus(out_trade_no);
            return true;
        }
        return false; //表示支付中
    }
}

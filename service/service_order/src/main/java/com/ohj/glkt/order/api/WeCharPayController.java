package com.ohj.glkt.order.api;

import com.ohj.glkt.order.service.WXpayService;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Api(tags = "微信支付")
@RestController
@RequestMapping("/api/order/wxPay")
public class WeCharPayController {

    @Autowired
    private WXpayService wXpayService;



    @ApiOperation("小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@PathVariable("orderNo")String orderNo){
        //个人无法测试该功能
        //Map<String,String> map=wXpayService.createJsapi(orderNo);
        //return Result.ok(map);

        return Result.ok().message("支付功能由于技术原因，已关闭");
    }

    @ApiOperation("查询支付状态")
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable("orderNo")String orderNo){
        //根据订单流水号查询支付状态
       Boolean isSuccess=wXpayService.queryPayStatus(orderNo);

       if(isSuccess){
           return Result.ok().message("支付成功");
       }else {
           return Result.ok().message("支付中");
       }
    }
}

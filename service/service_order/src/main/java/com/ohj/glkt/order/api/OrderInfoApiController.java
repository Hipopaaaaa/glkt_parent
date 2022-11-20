package com.ohj.glkt.order.api;

import cn.hutool.log.Log;
import com.ohj.ggkt.vo.order.OrderFormVo;
import com.ohj.ggkt.vo.order.OrderInfoVo;
import com.ohj.glkt.order.service.OrderInfoService;
import com.ohj.glkt.utils.order.AuthContextHolder;
import com.ohj.glkt.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "公众号生成订单接口")
@RestController
@RequestMapping("/api/order/orderInfo")
public class OrderInfoApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("生成订单")
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo,HttpServletRequest request){

        Long orderId= orderInfoService.submitOrder(orderFormVo);
        return Result.ok(orderId);
    }

    @ApiOperation("根据订单id查询订单")
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable("id")Long id){
        OrderInfoVo orderInfoVo=orderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfoVo);
    }
}

package com.ohj.glkt.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.order.OrderInfo;
import com.ohj.ggkt.vo.order.OrderInfoQueryVo;
import com.ohj.glkt.order.service.OrderInfoService;
import com.ohj.glkt.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-15
 */
@Api("订单管理")
@RestController
@RequestMapping("/admin/order/orderInfo")
public class OrderInfoController {

    @Autowired
    private OrderInfoService orderInfoService;

    //订单列表
    @ApiOperation("订单列表")
    @GetMapping("{page}/{limit}")
    public Result listOrder(@PathVariable("page")Long page,
                            @PathVariable("limit")Long limit,
                            OrderInfoQueryVo orderInfoQueryVo){
        Page<OrderInfo> orderInfoPage = orderInfoService.getPage(page, limit, orderInfoQueryVo);

        if(orderInfoPage!=null){
            return Result.ok(orderInfoPage);
        }else {
            return Result.fail();
        }
    }
}


package com.ohj.glkt.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.order.OrderDetail;
import com.ohj.ggkt.model.order.OrderInfo;
import com.ohj.ggkt.vo.order.OrderInfoQueryVo;
import com.ohj.glkt.order.mapper.OrderInfoMapper;
import com.ohj.glkt.order.service.OrderDetailService;
import com.ohj.glkt.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-15
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderDetailService orderDetailService;

    //条件分页查询
    @Override
    public Page<OrderInfo> getPage(Long page, Long limit, OrderInfoQueryVo orderInfoQueryVo) {
        Page<OrderInfo> finallyPage = new Page(page, limit);
        LambdaQueryWrapper<OrderInfo> orderInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderInfoLambdaQueryWrapper.eq(orderInfoQueryVo.getUserId()!=null,OrderInfo::getUserId,orderInfoQueryVo.getUserId())
                .like(StringUtils.isNotBlank(orderInfoQueryVo.getOutTradeNo()),OrderInfo::getOutTradeNo,orderInfoQueryVo.getOutTradeNo())
                .like(StringUtils.isNotBlank(orderInfoQueryVo.getPhone()),OrderInfo::getPhone,orderInfoQueryVo.getPhone())
                .eq(orderInfoQueryVo.getOrderStatus()!=null,OrderInfo::getOrderStatus,orderInfoQueryVo.getOrderStatus())
                .ge(StringUtils.isNotBlank(orderInfoQueryVo.getCreateTimeBegin()),OrderInfo::getCreateTime,orderInfoQueryVo.getCreateTimeBegin())
                .le(StringUtils.isNotBlank(orderInfoQueryVo.getCreateTimeEnd()),OrderInfo::getCreateTime,orderInfoQueryVo.getCreateTimeEnd());

        orderInfoMapper.selectPage(finallyPage,orderInfoLambdaQueryWrapper);

        //订单里包含详情内容，因此需要封装详情数据，根据订单id查询详情
        finallyPage.getRecords().stream().forEach(orderInfo -> {
            getOrderDetail(orderInfo);
        });

        return finallyPage;
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo){
        OrderDetail orderDetail = orderDetailService.getById(orderInfo.getId());
        if(orderDetail!=null){
            String courseName = orderDetail.getCourseName();
            orderInfo.getParam().put("courseName",courseName);
        }
        return orderInfo;
    }
}

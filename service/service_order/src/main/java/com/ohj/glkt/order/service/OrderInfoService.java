package com.ohj.glkt.order.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.order.OrderInfo;
import com.ohj.ggkt.vo.order.OrderInfoQueryVo;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-15
 */
public interface OrderInfoService extends IService<OrderInfo> {

   Page<OrderInfo> getPage(Long page, Long limit, OrderInfoQueryVo orderInfoQueryVo);
}

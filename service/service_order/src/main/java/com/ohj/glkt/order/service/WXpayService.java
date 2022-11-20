package com.ohj.glkt.order.service;

import java.util.Map;

public interface WXpayService {
    Map<String, String> createJsapi(String orderNo);

    //根据订单流水号查询订单状态
    Boolean queryPayStatus(String orderNo);
}

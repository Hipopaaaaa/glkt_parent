package com.ohj.glkt.order.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestRedisSessionController {
    @GetMapping("/api/order/test2")
    public String test(HttpServletRequest request){
        return "获得数据："+request.getSession().getAttribute("userId").toString();
    }
}

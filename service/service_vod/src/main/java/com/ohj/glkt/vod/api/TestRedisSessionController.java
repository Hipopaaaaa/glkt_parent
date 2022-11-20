package com.ohj.glkt.vod.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestRedisSessionController {

    @GetMapping("/api/vod/test/{id}")
    public String test(@PathVariable Long id, HttpServletRequest request){
        return "放入session: "+id;
    }
}

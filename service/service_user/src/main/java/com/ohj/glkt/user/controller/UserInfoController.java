package com.ohj.glkt.user.controller;


import com.ohj.ggkt.model.user.UserInfo;
import com.ohj.glkt.user.service.UserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user/userInfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("获取用户信息")
    @GetMapping("inner/getById/{id}")
    public UserInfo getById(@PathVariable("id")Long id){
        UserInfo userInfo = userInfoService.getById(id);

        return userInfo;
    }
}


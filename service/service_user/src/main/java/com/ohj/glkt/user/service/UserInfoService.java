package com.ohj.glkt.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.user.UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getUserInfoOpenid(String openId);
}

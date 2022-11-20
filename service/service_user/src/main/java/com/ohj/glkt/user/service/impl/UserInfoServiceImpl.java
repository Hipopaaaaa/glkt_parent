package com.ohj.glkt.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ohj.ggkt.model.user.UserInfo;
import com.ohj.glkt.user.mapper.UserInfoMapper;
import com.ohj.glkt.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    //根据openid获取用户信息
    @Override
    public UserInfo getUserInfoOpenid(String openId) {
        LambdaQueryWrapper<UserInfo> userInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userInfoLambdaQueryWrapper.eq(StringUtils.isNotBlank(openId),UserInfo::getOpenId,openId);

        UserInfo userInfo = userInfoMapper.selectOne(userInfoLambdaQueryWrapper);

        return userInfo;
    }
}

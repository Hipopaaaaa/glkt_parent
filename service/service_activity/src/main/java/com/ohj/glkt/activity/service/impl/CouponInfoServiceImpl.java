package com.ohj.glkt.activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.activity.CouponInfo;
import com.ohj.ggkt.model.activity.CouponUse;
import com.ohj.ggkt.model.user.UserInfo;
import com.ohj.ggkt.vo.activity.CouponUseQueryVo;
import com.ohj.glkt.activity.mapper.CouponInfoMapper;
import com.ohj.glkt.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohj.glkt.activity.service.CouponUseService;
import com.ohj.glkt.client.user.UserInfoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {

    @Autowired
    private CouponUseService couponUseService;


    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    @Override
    public Page<CouponUse> getCouponUsePage(Long page, Long limit, CouponUseQueryVo couponUseQueryVo) {

        Page<CouponUse> couponUsePage = new Page<>(page, limit);

        LambdaQueryWrapper<CouponUse> couponUseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        couponUseLambdaQueryWrapper.eq(couponUseQueryVo.getCouponId()!=null,CouponUse::getCouponId,couponUseQueryVo.getCouponId())
                .eq(StringUtils.isNotBlank(couponUseQueryVo.getCouponStatus()),CouponUse::getCouponStatus,couponUseQueryVo.getCouponStatus())
                .ge(StringUtils.isNotBlank(couponUseQueryVo.getGetTimeBegin()),CouponUse::getGetTime,couponUseQueryVo.getGetTimeBegin())
                .le(StringUtils.isNotBlank(couponUseQueryVo.getGetTimeEnd()), CouponUse::getGetTime, couponUseQueryVo.getGetTimeEnd());

        couponUseService.page(couponUsePage,couponUseLambdaQueryWrapper);

        couponUsePage.getRecords().stream().forEach(couponUse -> {
            //数据封装
            this.getUserInfoById(couponUse);
        });

        return couponUsePage;
    }



    //根据用户id，通过远程调用等到用户信息
    private CouponUse getUserInfoById(CouponUse couponUse) {
        Long userId = couponUse.getUserId();
        if(userId==null){
            return couponUse;
        }
        UserInfo userInfo = userInfoFeignClient.getById(userId);

        if(userInfo==null){
            return couponUse;
        }
        couponUse.getParam().put("nickName",userInfo.getNickName());
        couponUse.getParam().put("phone",userInfo.getPhone());
        return couponUse;
    }
}

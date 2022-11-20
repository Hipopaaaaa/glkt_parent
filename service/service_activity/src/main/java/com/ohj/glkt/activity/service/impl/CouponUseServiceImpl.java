package com.ohj.glkt.activity.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ohj.ggkt.model.activity.CouponUse;
import com.ohj.glkt.activity.mapper.CouponUseMapper;
import com.ohj.glkt.activity.service.CouponUseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 优惠券领用表 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Service
public class CouponUseServiceImpl extends ServiceImpl<CouponUseMapper, CouponUse> implements CouponUseService {

    @Autowired
    private CouponUseMapper couponUseMapper;

    //更新优惠卷的方法
    @Override
    public boolean updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        LambdaUpdateWrapper<CouponUse> couponUseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        couponUseLambdaUpdateWrapper.eq(couponUseId!=null,CouponUse::getId,couponUseId)
                .eq(orderId!=null,CouponUse::getOrderId,orderId)
                .set(CouponUse::getCouponStatus,1)
                .set(CouponUse::getUsingTime,new Date());

        int result = couponUseMapper.update(null, couponUseLambdaUpdateWrapper);
        return  result>0?true:false;
    }
}

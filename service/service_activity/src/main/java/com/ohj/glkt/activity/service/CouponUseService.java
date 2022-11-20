package com.ohj.glkt.activity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.activity.CouponUse;

/**
 * <p>
 * 优惠券领用表 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
public interface CouponUseService extends IService<CouponUse> {

    //更新优惠卷的状态
    boolean updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}

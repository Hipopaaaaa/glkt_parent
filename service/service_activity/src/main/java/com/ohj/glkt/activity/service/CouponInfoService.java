package com.ohj.glkt.activity.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.activity.CouponInfo;
import com.ohj.ggkt.model.activity.CouponUse;
import com.ohj.ggkt.vo.activity.CouponUseQueryVo;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
public interface CouponInfoService extends IService<CouponInfo> {

    Page<CouponUse> getCouponUsePage(Long page, Long limit, CouponUseQueryVo couponUseQueryVo);



}

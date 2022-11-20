package com.ohj.glkt.client.activity;

import com.ohj.ggkt.model.activity.CouponInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-activity")
public interface CouponInfoFeignClient {

    @ApiOperation("根据优惠卷id查询")
    @GetMapping("/inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable("couponId")Long couponId);

    @ApiOperation("更新优惠卷使用状态")
    @GetMapping("/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId")Long couponUseId,
                                             @PathVariable("orderId")Long orderId);
}

package com.ohj.glkt.activity.api;

import com.ohj.ggkt.model.activity.CouponInfo;
import com.ohj.glkt.activity.service.CouponInfoService;
import com.ohj.glkt.activity.service.CouponUseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优惠卷接口")
@RestController
@RequestMapping("/api/activity/couponInfo")
public class CouponInfoApiController {

    @Autowired
    private CouponInfoService couponInfoService;

    @Autowired
    private CouponUseService couponUseService;

    @ApiOperation("根据优惠卷id查询")
    @GetMapping("/inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable("couponId")Long couponId){
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return couponInfo;
    }

    @ApiOperation("更新优惠卷使用状态")
    @GetMapping("/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId")Long couponUseId,
                                             @PathVariable("orderId")Long orderId){
      boolean isSuccess =  couponUseService.updateCouponInfoUseStatus(couponUseId,orderId);
      return isSuccess;
    }

}

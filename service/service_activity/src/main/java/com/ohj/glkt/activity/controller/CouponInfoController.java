package com.ohj.glkt.activity.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.activity.CouponInfo;
import com.ohj.ggkt.model.activity.CouponUse;
import com.ohj.ggkt.vo.activity.CouponInfoVo;
import com.ohj.ggkt.vo.activity.CouponUseQueryVo;
import com.ohj.glkt.activity.service.CouponInfoService;
import com.ohj.glkt.utils.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-16
 */
@Api(tags = "优惠卷管理")
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {

    @Autowired
    private CouponInfoService couponInfoService;

    @ApiOperation("获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page")Long page,
                        @PathVariable("limit")Long limit){
        Page<CouponInfo> couponInfoPage = new Page<>(page, limit);
        couponInfoService.page(couponInfoPage);
        return Result.ok(couponInfoPage);
    }

    @ApiOperation("获取已经使用的分页列表（条件查询）")
    @GetMapping("couponUse/{page}/{limit}")
    public Result index(@PathVariable("page")Long page,
                        @PathVariable("limit")Long limit,
                        CouponUseQueryVo couponUseQueryVo){
        Page<CouponUse> couponUsePage=couponInfoService.getCouponUsePage(page,limit,couponUseQueryVo);

        return Result.ok(couponUsePage);

    }

    @ApiOperation("获取优惠卷")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id")Long id){
        CouponInfo couponInfo = couponInfoService.getById(id);
        if(couponInfo!=null){
            return Result.ok(couponInfo);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("新增优惠卷")
    @PostMapping("save")
    public Result save(@RequestBody CouponInfo couponInfo){
        boolean isSuccess = couponInfoService.save(couponInfo);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("修改优惠卷")
    @PutMapping("update")
    public Result updateById(@RequestBody CouponInfo couponInfo){
        boolean isSuccess = couponInfoService.updateById(couponInfo);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除优惠卷")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id")Long id){
        boolean isSuccess = couponInfoService.removeById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除优惠卷")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        boolean isSuccess = couponInfoService.removeByIds(idList);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

}


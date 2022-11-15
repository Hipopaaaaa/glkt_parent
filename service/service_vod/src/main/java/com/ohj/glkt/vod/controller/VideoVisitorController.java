package com.ohj.glkt.vod.controller;


import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.VideoVisitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-13
 */
@Api(tags = "视频访客管理")
@RestController
@RequestMapping("/admin/vod/videoVisitor")

public class VideoVisitorController {

    @Autowired
    private VideoVisitorService videoVisitorService;

    @ApiOperation("课程统计")
    @GetMapping("findCount/{courseId}/{startDate}/{endDate}")
    public Result findCount(@PathVariable("courseId")Long courseId,
                            @PathVariable("startDate")String startDate,
                            @PathVariable("endDate")String endDate){
        Map<String,Object> map=videoVisitorService.findCount(courseId,startDate,endDate);

        if(map.isEmpty()){
            return Result.fail();
        }else {
            return Result.ok(map);
        }
    }
}


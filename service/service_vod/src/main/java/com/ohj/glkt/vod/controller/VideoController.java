package com.ohj.glkt.vod.controller;


import com.ohj.ggkt.model.vod.Video;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Api(tags = "小节管理")
@RestController
@RequestMapping("/admin/vod/video")

public class VideoController {
    @Autowired
    private VideoService videoService;


    @ApiOperation("查询小节")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id")Long id){
        Video video = videoService.getById(id);
        if(video!=null){
            return Result.ok(video);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("增加小节")
    @PostMapping("save")
    public Result save(@RequestBody Video video){
        boolean isSuccess = videoService.save(video);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("修改小节")
    @PutMapping("update")
    public Result update(@RequestBody Video video){
        boolean isSuccess = videoService.updateById(video);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除小节")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id")Long id){
        boolean isSuccess = videoService.removeVideoById(id);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}


package com.ohj.glkt.vod.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.ohj.ggkt.model.vod.Chapter;
import com.ohj.ggkt.vo.vod.ChapterVo;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.ChapterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Api(tags = "课程大纲管理")
@RestController
@RequestMapping("/admin/vod/chapter")

public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    //大纲列表（章节和小节列表）
    @ApiOperation("大纲列表")
    @GetMapping("getNestedTreeList/{courseId}")
    public Result getTreeList(@PathVariable("courseId")Long courseId){
       List<ChapterVo> chapterList= chapterService.getTreeList(courseId);
           return Result.ok(chapterList);
    }
    //添加章节
    @ApiOperation("添加章节")
    @PostMapping("save")
    public Result save(@RequestBody Chapter chapter){
        boolean isSuccess = chapterService.save(chapter);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //根据id查询章节
    @ApiOperation("查询章节")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id") Long id){
        Chapter chapter = chapterService.getById(id);
        if(chapter!=null){
            return Result.ok(chapter);
        }else {
            return Result.fail();
        }
    }

    //修改
    @ApiOperation("修改章节")
    @PutMapping("update")
    public Result update(@RequestBody Chapter chapter){
        boolean isSuccess = chapterService.updateById(chapter);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    //删除章节
    @ApiOperation("删除章节")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") Long id){
        boolean isSuccess = chapterService.removeChapterById(id);
        if(isSuccess){

            return Result.ok();
        }else {
            return Result.fail();
        }
    }
}


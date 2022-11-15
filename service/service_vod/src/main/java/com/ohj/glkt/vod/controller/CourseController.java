package com.ohj.glkt.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.vo.vod.*;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Api(tags = "课程管理")
@RestController
@RequestMapping("/admin/vod/course")

public class CourseController {
    @Autowired
    private CourseService courseService;

    @ApiOperation("点播课程的列表")
    @GetMapping("{page}/{limit}")
    public Result courseList(@PathVariable("page")Integer page,
                             @PathVariable("limit")Integer limit,
                               CourseQueryVo courseQueryVo){
        Page<Course> coursePage=courseService.findPageCourse(page,limit,courseQueryVo);
        if(coursePage!=null){
            return Result.ok(coursePage);
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("添加课程基本信息")
    @PostMapping("save")
    public Result save(@RequestBody CourseFormVo courseFormVo){

       Long courseId = courseService.saveCourseInfo(courseFormVo);

       if(courseId!=null){
           return Result.ok(courseId);
       }else {
           return Result.fail();
       }
    }

    @ApiOperation("获取课程信息")
    @GetMapping("get/{id}")
    public Result get(@PathVariable("id")Long id){
       CourseFormVo courseFormVo= courseService.getCourseInfoById(id);

       if(courseFormVo!=null){
           return Result.ok(courseFormVo);
       }else {
           return Result.fail();
       }
    }

    @ApiOperation("修改课程信息")
    @PutMapping("update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        boolean isSuccess=courseService.updateCourseId(courseFormVo);
        if(isSuccess){
            return Result.ok(courseFormVo.getId());
        }else {
            return Result.fail();
        }
    }

    //根据课程id查询发布的课程信息
    @ApiOperation("查询发布的课程信息")
    @GetMapping("getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable("id")Long id){
        CoursePublishVo coursePublishVo=courseService.getCoursePublishVo(id);

        if(coursePublishVo!=null){
            return Result.ok(coursePublishVo);
        }else {
            return Result.fail();
        }
    }

    //发布课程
    @ApiOperation("课程最终发布")
    @PutMapping("publishCourse/{id}")
    public Result publishCourse(@PathVariable("id")Long id){
       boolean isSuccess= courseService.publishCourse(id);
       if(isSuccess){
           return Result.ok();
       }else {
           return Result.fail();
       }
    }

    @ApiOperation("删除课程")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id")Long id){
       boolean isSuccess = courseService.removeByCourseId(id);
       if(isSuccess){
           return Result.ok();
       }else {
           return Result.fail();
       }
    }

}


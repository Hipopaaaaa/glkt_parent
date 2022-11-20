package com.ohj.glkt.vod.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.vo.vod.CourseFormVo;
import com.ohj.ggkt.vo.vod.CourseQueryVo;
import com.ohj.ggkt.vo.vod.CourseVo;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "公众号的课程管理")
@RestController
@RequestMapping("/api/vod/course")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/inner/getById/{courseId}")
    public Course getById(@PathVariable("courseId")Long courseId){
        Course course = courseService.getById(courseId);
        return course;
    }


    @ApiOperation("根据一级id查询课程列表")
    @GetMapping("/{subjectParentId}/{page}/{limit}")
    public Result findPageByParentId(@PathVariable("subjectParentId")Long subjectParentId,
                                     @PathVariable("page")Integer page,
                                     @PathVariable("limit")Integer limit){

        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);
        Page<Course> pageCourse = courseService.findPageCourse(page, limit, courseQueryVo);

        return Result.ok(pageCourse);
    }

    @ApiOperation("根据课程id查询课程详情信息")
    @GetMapping("/getInfo/{courseId}")
    public Result getInfo(@PathVariable("courseId")Long courseId){

        Map<String,Object> map = courseService.getCourseInfoByCourseId(courseId);
        return Result.ok(map);
    }


    //根据关键字查询课程
    @ApiOperation("根据关键字查询课程")
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@PathVariable("keyword")String keyword){
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseLambdaQueryWrapper.like(StringUtils.isNotBlank(keyword),Course::getTitle,keyword);

        List<Course> courseList = courseService.list(courseLambdaQueryWrapper);
        return courseList;
    }

}

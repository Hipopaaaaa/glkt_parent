package com.ohj.glkt.client.course;

import com.ohj.ggkt.model.vod.Course;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-vod")
public interface CourseFeignClient {

    @ApiOperation("根据关键字查询课程")
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@PathVariable("keyword")String keyword);



    @ApiOperation("根据课程id查询课程信息")
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    public Course getById(@PathVariable("courseId")Long courseId);

}

package com.ohj.glkt.vod.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.vo.vod.*;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface CourseService extends IService<Course> {

    Page<Course> findPageCourse(Integer page, Integer limit, CourseQueryVo courseQueryVo);

    Long saveCourseInfo(CourseFormVo courseFormVo);

    CourseFormVo getCourseInfoById(Long id);

    boolean updateCourseId(CourseFormVo courseFormVo);

    CoursePublishVo getCoursePublishVo(Long id);

    boolean publishCourse(Long id);

    boolean removeByCourseId(Long id);
}

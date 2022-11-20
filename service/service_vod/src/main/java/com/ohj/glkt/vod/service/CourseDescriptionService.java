package com.ohj.glkt.vod.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.CourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface CourseDescriptionService extends IService<CourseDescription> {

    void removeByCourseId(Long id);

    //根据课程id，获取详细信息
    CourseDescription getByCourseId(Long courseId);
}

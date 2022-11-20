package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohj.ggkt.model.vod.CourseDescription;
import com.ohj.glkt.vod.mapper.CourseDescriptionMapper;
import com.ohj.glkt.vod.service.CourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    //根据课程id 删除课程描述
    @Override
    public void removeByCourseId(Long id) {

        LambdaQueryWrapper<CourseDescription> courseDescriptionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseDescriptionLambdaQueryWrapper.eq(id!=null,CourseDescription::getCourseId,id);

        courseDescriptionMapper.delete(courseDescriptionLambdaQueryWrapper);
    }

    @Override
    public CourseDescription getByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseDescription> courseDescriptionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        courseDescriptionLambdaQueryWrapper.eq(courseId!=null,CourseDescription::getCourseId,courseId);

        return courseDescriptionMapper.selectOne(courseDescriptionLambdaQueryWrapper);
    }
}

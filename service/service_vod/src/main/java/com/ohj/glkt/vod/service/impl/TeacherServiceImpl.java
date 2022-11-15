package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.vod.Teacher;
import com.ohj.ggkt.vo.vod.TeacherQueryVo;
import com.ohj.glkt.vod.mapper.TeacherMapper;
import com.ohj.glkt.vod.service.TeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-09
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Page findQueryPage(Long current,Long limit,TeacherQueryVo teacherQueryVo) {

        Page<Teacher> teacherPage = new Page<>(current,limit);

        if(teacherQueryVo==null){
            teacherMapper.selectPage(teacherPage,null);
            return teacherPage;
        }

        LambdaQueryWrapper<Teacher> teacherLambdaQueryWrapper = new LambdaQueryWrapper<>();

        teacherLambdaQueryWrapper.like(StringUtils.isNotBlank(teacherQueryVo.getName()),Teacher::getName,teacherQueryVo.getName())
                .eq(teacherQueryVo.getLevel()!=null,Teacher::getLevel,teacherQueryVo.getLevel())
                .ge(StringUtils.isNotBlank(teacherQueryVo.getJoinDateBegin()),Teacher::getJoinDate,teacherQueryVo.getJoinDateBegin())
                .le(StringUtils.isNotBlank(teacherQueryVo.getJoinDateEnd()),Teacher::getJoinDate,teacherQueryVo.getJoinDateEnd())
                        .orderByDesc(Teacher::getJoinDate);

        teacherMapper.selectPage(teacherPage,teacherLambdaQueryWrapper);
        return teacherPage;
    }


}

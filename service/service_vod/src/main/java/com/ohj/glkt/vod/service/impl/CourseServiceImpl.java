package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.model.vod.CourseDescription;
import com.ohj.ggkt.model.vod.Subject;
import com.ohj.ggkt.model.vod.Teacher;
import com.ohj.ggkt.vo.vod.CourseFormVo;
import com.ohj.ggkt.vo.vod.CoursePublishVo;
import com.ohj.ggkt.vo.vod.CourseQueryVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.exception.GlktException;

import com.ohj.glkt.vod.mapper.CourseMapper;
import com.ohj.glkt.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseDescriptionService courseDescriptionService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private FileService fileService;




    @Override
    public Page<Course> findPageCourse(Integer page, Integer limit, CourseQueryVo courseQueryVo) {
        Page<Course> coursePage = new Page<>(page, limit);

        //无条件查询
        if (courseQueryVo == null) {
            courseMapper.selectPage(coursePage, null);
            //当前查询到的coursePage里 关于课程专业和课程专业父级 都只是id 因此需要转换
            coursePage.getRecords().stream().forEach(course -> {
                this.getNameById(course);
            });
            return coursePage;
        }
        //select * from course where title like "ja%"
        LambdaQueryWrapper<Course> courseLambdaQueryWrapper = new LambdaQueryWrapper<>();


        courseLambdaQueryWrapper.like(StringUtils.isNotBlank(courseQueryVo.getTitle()), Course::getTitle, courseQueryVo.getTitle())
                .eq(courseQueryVo.getSubjectId() != null, Course::getSubjectId, courseQueryVo.getSubjectId())
                .eq(courseQueryVo.getSubjectParentId() != null, Course::getSubjectParentId, courseQueryVo.getSubjectParentId())
                .eq(courseQueryVo.getTeacherId() != null, Course::getTeacherId, courseQueryVo.getTeacherId());

        courseMapper.selectPage(coursePage, courseLambdaQueryWrapper);

        //当前查询到的coursePage里 关于课程专业和课程专业父级 都只是id 因此需要转换
        coursePage.getRecords().stream().forEach(course -> {
            this.getNameById(course);
        });

        return coursePage;
    }

    @Override
    @Transactional
    public Long saveCourseInfo(CourseFormVo courseFormVo) {

        //添加课程基本信息，操作course表
        Course course = CopyUtil.copyBean(courseFormVo, Course.class);
        courseMapper.insert(course);
        //添加课程描述信息，操作course_description表
        CourseDescription courseDescription = CopyUtil.copyBean(courseFormVo, CourseDescription.class);
        courseDescription.setId(course.getId());
        courseDescription.setCourseId(course.getId());
        courseDescriptionService.save(courseDescription);
        return course.getId();
    }

    @Override
    public CourseFormVo getCourseInfoById(Long id) {
        //课程基本信息
        Course course = courseMapper.selectById(id);
        if(course==null){
            return null;
        }
        //课程描述信息
        CourseDescription courseDescription = courseDescriptionService.getById(id);
        if(courseDescription==null){
            return null;
        }
        //数据封装
        CourseFormVo courseFormVo = CopyUtil.copyBean(course, CourseFormVo.class);
        courseFormVo.setDescription(courseDescription.getDescription());

        return courseFormVo;
    }

    @Override
    @Transactional
    public boolean updateCourseId(CourseFormVo courseFormVo) {
        Course course = CopyUtil.copyBean(courseFormVo, Course.class);
        //修改课程基本信息
        int result = courseMapper.updateById(course);
        if(result==0){
            throw new GlktException(20001,"修改失败");
        }

        //修改课程描述信息
        CourseDescription courseDescription = CopyUtil.copyBean(courseFormVo, CourseDescription.class);
        courseDescription.setId(courseFormVo.getId());
        boolean isSuccess = courseDescriptionService.updateById(courseDescription);
        if(isSuccess){
            return true;
        }else {
            throw new GlktException(20001,"修改失败");
        }
    }

    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {

        return courseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourse(Long id) {
        LambdaUpdateWrapper<Course> courseLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        courseLambdaUpdateWrapper.eq(id!=null,Course::getId,id)
                .set(Course::getStatus,1)
                .set(Course::getPublishTime,new Date());

        int update = courseMapper.update(null, courseLambdaUpdateWrapper);
        if(update>0){
            return true;
        }else {
            return false;
        }
    }
    @Transactional
    @Override
    public boolean removeByCourseId(Long id) {
        //根据课程id删除小节
        videoService.removeByCourseId(id);
        //根据课程id删除章节
        chapterService.removeByCourseId(id);
        //根据课程id删除课程描述
        courseDescriptionService.removeByCourseId(id);


        //删除cos服务中的课程图片
        Course course = courseMapper.selectById(id);
        //   https://glkt-1304791648.cos.ap-guangzhou.myqcloud.com/2022/11/12/a287003405104c9faa5e9b2cbd11530dWechatIMG38.png
        //处理查询到的连接 获得key

        if(course==null){
            throw  new GlktException(20001,"课程不存在");
        }

        String cover = course.getCover();

        if(StringUtils.isBlank(cover)){
            return true;
        }

        int index = cover.indexOf("com/");
        String key = cover.substring(index+4);

        boolean isSuccess = fileService.delete(key);
        if(!isSuccess){
            throw new GlktException(20001,"删除图片失败");
        }

        //根据课程id删除课程
        courseMapper.deleteById(id);

        return true;
    }

    private void getNameById(Course course) {
        //根据讲师id获取讲师名称
        Teacher teacher = teacherService.getById(course.getTeacherId());
        if (teacher != null) {
            String name = teacher.getName();
            course.getParam().put("teacherName", name);
        }
        //根据课程分类id湖获取课程名称
        //第一层分类
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if (subjectOne != null) {
            course.getParam().put("subjectParentTitle", subjectOne.getTitle());
        }
        //第二层分类
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if (subjectTwo != null) {
            course.getParam().put("subjectTitle", subjectTwo.getTitle());
        }
    }
}

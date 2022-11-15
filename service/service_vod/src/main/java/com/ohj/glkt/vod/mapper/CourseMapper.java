package com.ohj.glkt.vod.mapper;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohj.ggkt.model.vod.Course;
import com.ohj.ggkt.vo.vod.CourseProgressVo;
import com.ohj.ggkt.vo.vod.CoursePublishVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface CourseMapper extends BaseMapper<Course> {

    CoursePublishVo selectCoursePublishVoById(Long id);
}

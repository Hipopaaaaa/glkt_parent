package com.ohj.glkt.vod.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.Teacher;
import com.ohj.ggkt.vo.vod.TeacherQueryVo;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-09
 */
public interface TeacherService extends IService<Teacher> {

    Page findQueryPage(Long current, Long limit, TeacherQueryVo teacherQueryVo);



}

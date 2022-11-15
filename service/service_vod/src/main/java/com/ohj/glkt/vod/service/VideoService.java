package com.ohj.glkt.vod.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.Video;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface VideoService extends IService<Video> {

    List<Video> getByCourseId(Long courseId);

    //根据课程删除小节
    boolean removeByCourseId(Long id);

    //删除小节时 删除视频
    boolean removeVideoById(Long id);

    //根据章节id 删除小节
    boolean removeByChapterId(Long id);

}

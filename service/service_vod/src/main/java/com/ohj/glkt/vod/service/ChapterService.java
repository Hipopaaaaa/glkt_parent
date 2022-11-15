package com.ohj.glkt.vod.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.ohj.ggkt.model.vod.Chapter;
import com.ohj.ggkt.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
public interface ChapterService extends IService<Chapter> {

    List<ChapterVo> getTreeList(Long courseId);

    boolean removeByCourseId(Long id);


    boolean removeChapterById(Long id);
}

package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ohj.ggkt.model.vod.Chapter;
import com.ohj.ggkt.model.vod.Video;
import com.ohj.ggkt.vo.vod.ChapterVo;
import com.ohj.ggkt.vo.vod.VideoVo;
import com.ohj.glkt.utils.copy.CopyUtil;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.vod.mapper.ChapterMapper;
import com.ohj.glkt.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohj.glkt.vod.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private VideoService videoService;

    @Override
    public List<ChapterVo> getTreeList(Long courseId) {

        //根据课程id 获取到课程的所有章节
        LambdaQueryWrapper<Chapter> chapterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chapterLambdaQueryWrapper.eq(courseId!=null,Chapter::getCourseId,courseId)
                .orderByAsc(Chapter::getSort);
        List<Chapter> chapterList = chapterMapper.selectList(chapterLambdaQueryWrapper);

        //根据课程id 获取到课程的所有小节
        List<Video> videoList=videoService.getByCourseId(courseId);

        //封装章节
        List<ChapterVo> chapterVoList = chapterList.stream().map(chapter -> {
            ChapterVo chapterVo = CopyUtil.copyBean(chapter, ChapterVo.class);
            //封装章节里面到小节，先找出章节对应的小节
            List<VideoVo> videoVoList=new ArrayList<>();
            videoList.forEach(video -> {
                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo = CopyUtil.copyBean(video, VideoVo.class);
                    videoVoList.add(videoVo);
                }
            });

            chapterVo.setChildren(videoVoList);
            return chapterVo;
        }).collect(Collectors.toList());

        return chapterVoList;
    }

    //根据课程id删除章节
    @Transactional
    @Override
    public boolean removeByCourseId(Long id) {
        LambdaQueryWrapper<Chapter> chapterLambdaQueryWrapper = new LambdaQueryWrapper<>();
        chapterLambdaQueryWrapper.eq(id!=null,Chapter::getCourseId,id);

        boolean isSuccess = videoService.removeByChapterId(id);
        if(!isSuccess){
            throw new GlktException(20001,"删除章节失败");
        }

        int result = chapterMapper.delete(chapterLambdaQueryWrapper);
        return result>0?true:false;
    }

    //根据章节id 删除章节 同时删除小节
    @Transactional
    @Override
    public boolean removeChapterById(Long id) {


        //根据章节id 删除小节
        boolean isSuccess = videoService.removeByChapterId(id);
        if(!isSuccess){
            throw new GlktException(20001,"删除小节失败");
        }
        //删除章节
        int result = chapterMapper.deleteById(id);
        return result>0?true:false;
    }


}

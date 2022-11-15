package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ohj.ggkt.model.vod.Video;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.vod.mapper.VideoMapper;
import com.ohj.glkt.vod.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohj.glkt.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-12
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private VodService vodService;

    //根据课程id获取小节
    @Override
    public List<Video> getByCourseId(Long courseId) {
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(courseId != null, Video::getCourseId, courseId);

        return videoMapper.selectList(videoLambdaQueryWrapper);
    }

    //根据课程id删除小节
    @Transactional
    @Override
    public boolean removeByCourseId(Long id) {
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(id != null, Video::getCourseId, id);

        //根据课程id获取所有小节
        List<Video> videoList = videoMapper.selectList(videoLambdaQueryWrapper);
        for (Video video : videoList) {
            Long videoId = video.getId();
            boolean isSuccess = removeVideoById(videoId);
            if(!isSuccess){
                throw new GlktException(20001,"删除小节失败");
            }
        }
        //根据课程id删除小节
        int result = videoMapper.delete(videoLambdaQueryWrapper);
        return result>0?true:false;
    }


    //根据章节id删除小节 同时还要删除vod中的视频
    @Override
    public boolean removeByChapterId(Long id) {
        LambdaQueryWrapper<Video> videoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        videoLambdaQueryWrapper.eq(id!=null,Video::getChapterId,id);

        List<Video> videoList = videoMapper.selectList(videoLambdaQueryWrapper);

        //若该章节无小节
        if(videoList.isEmpty()){
            return true;
        }
        //删除小节的vod视频
        videoList.forEach(video -> {
            String videoSourceId = video.getVideoSourceId();
            boolean isSuccess = vodService.removeVideo(videoSourceId);
            if(!isSuccess){
                throw new GlktException(20001,"删除小节失败");
            }
        });

        //根据章节id删除小节
        int result = videoMapper.delete(videoLambdaQueryWrapper);
        return result>0?true:false;
    }

    //根据小节id删除小节 同时删除vod中的视频
    @Override
    public boolean removeVideoById(Long id) {
        Video video = videoMapper.selectById(id);
        //小节的视频id
        String videoSourceId = video.getVideoSourceId();
        if(StringUtils.isBlank(videoSourceId)){
            //视频id为空，表示该小节还为上传视频
            //删除小节
            return videoMapper.deleteById(id)>0?true:false;
        }

        boolean isSuccess = vodService.removeVideo(videoSourceId);
        if (!isSuccess) {
            return false;
        }
        //删除小节
        int result = videoMapper.deleteById(id);
        return result>0?true:false;
    }
}

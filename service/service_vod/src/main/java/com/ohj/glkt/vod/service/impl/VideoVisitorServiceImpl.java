package com.ohj.glkt.vod.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ohj.ggkt.model.vod.VideoVisitor;
import com.ohj.ggkt.vo.vod.VideoVisitorCountVo;
import com.ohj.ggkt.vo.vod.VideoVisitorVo;
import com.ohj.glkt.vod.mapper.VideoVisitorMapper;
import com.ohj.glkt.vod.service.VideoVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 视频来访者记录表 服务实现类
 * </p>
 *
 * @author ohj
 * @since 2022-11-13
 */
@Service
public class VideoVisitorServiceImpl extends ServiceImpl<VideoVisitorMapper, VideoVisitor> implements VideoVisitorService {

    @Autowired
    private VideoVisitorMapper videoVisitorMapper;

    @Override
    public Map<String, Object> findCount(Long courseId, String startDate, String endDate) {

        List<VideoVisitorCountVo> videoVisitorCountVoList= videoVisitorMapper.findCount(courseId,startDate,endDate);

        Map<String,Object> map=new HashMap<>();
        //创建两个list集合 ，一个代表所有日期，一个代表日常对应的访客数量
        List<String> dateList= videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getJoinTime).collect(Collectors.toList());
        List<Integer> countList= videoVisitorCountVoList.stream().map(VideoVisitorCountVo::getUserCount).collect(Collectors.toList());

        map.put("xData",dateList);
        map.put("yData",countList);

        return map;
    }
}

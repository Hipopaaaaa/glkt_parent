package com.ohj.glkt.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ohj.ggkt.model.vod.VideoVisitor;
import com.ohj.ggkt.vo.vod.VideoVisitorCountVo;
import com.ohj.ggkt.vo.vod.VideoVisitorVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
}

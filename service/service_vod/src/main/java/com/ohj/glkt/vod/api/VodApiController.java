package com.ohj.glkt.vod.api;

import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Api(tags = "腾讯视频点播")
@RestController
@RequestMapping("/api/vod")
public class VodApiController {
    @Autowired
    private VodService vodService;

    @ApiOperation("根据课程id和小节id，获取视频")
    @GetMapping("/getPlayAuth/{courseId}/{videoId}")
    public Result getPlayAuth(@PathVariable("courseId")Long courseId,@PathVariable("videoId")Long videoId){
      Map<String,Object> map= vodService.getPlayAuth(courseId,videoId);
      return Result.ok(map);
    }
}

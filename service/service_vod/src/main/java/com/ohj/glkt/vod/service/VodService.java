package com.ohj.glkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface VodService {
    String uploadVideo(MultipartFile file);

    boolean removeVideo(String fileId);

    //点播视频播放接口
    Map<String, Object> getPlayAuth(Long courseId, Long videoId);
}

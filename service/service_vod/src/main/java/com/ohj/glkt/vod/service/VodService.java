package com.ohj.glkt.vod.service;

import org.springframework.web.multipart.MultipartFile;

public interface VodService {
    String uploadVideo(MultipartFile file);

    boolean removeVideo(String fileId);
}

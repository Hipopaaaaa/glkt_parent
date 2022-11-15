package com.ohj.glkt.vod.controller;

import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/vod/file")

public class FileUploadController {

    @Autowired
    private FileService fileService;

    @ApiOperation("文件上传")
    @PostMapping("upload")
    public Result uploadFile(MultipartFile file){
        String url=fileService.upload(file);
        return Result.ok(url).message("上传文件成功");
    }

//    @ApiOperation("文件删除")
//    @GetMapping("delete")
//    public Result deleteFile(@RequestParam String key){
//        boolean isSuccess=fileService.delete(key);
//        if(isSuccess){
//            return Result.ok();
//        }else {
//            return Result.fail();
//        }
//    }

}

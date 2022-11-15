package com.ohj.glkt.vod.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.utils.result.Result;
import com.ohj.glkt.vod.service.VodService;
import com.ohj.glkt.vod.utils.ConstantPropertiesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ohj.glkt.vod.utils.Signature;
import org.springframework.web.multipart.MultipartFile;

import java.util.Random;

@Api(tags = "视频点播管理")
@RestController
@RequestMapping("/admin/vod")

@Slf4j
public class VodController {



    @Autowired
    private VodService vodService;

    //暂时没用
    @GetMapping("sign")
    public Result sign(){
        Signature sign = new Signature();
        // 设置 App 的云 API 密钥
        sign.setSecretId(ConstantPropertiesUtils.ACCESS_KEY_ID);
        sign.setSecretKey(ConstantPropertiesUtils.ACCESS_KEY_SECRET);
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(java.lang.Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 2); // 签名有效期：2天
        try {
            String signature = sign.getUploadSignature();
            System.out.println("signature : " + signature);

           if(StringUtils.isNotBlank(signature)){
               return Result.ok(signature);
           }else {
               return Result.fail();
           }
        } catch (Exception e) {
            System.out.print("获取签名失败");
            throw new GlktException(20001,"获取签名失败");
        }

    }

    @ApiOperation("上传视频")
    @PostMapping("upload")
    public Result uploadVideo(@RequestParam("file")MultipartFile file){


        //真实功能：已停用，因为要计费
        //String fileId=vodService.uploadVideo(file);

        //假数据
        String fileId="243791575728326096";

        log.info("视频id：{}",fileId);

       if(StringUtils.isNotBlank(fileId)){
           return Result.ok(fileId);
       }else {
           return Result.fail();
       }
    }

    @ApiOperation("删除视频")
    @DeleteMapping("remove/{fileId}")
    public Result remove(@PathVariable("fileId")String fileId){
        boolean isSuccess=vodService.removeVideo(fileId);
        if(isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }


    }
}

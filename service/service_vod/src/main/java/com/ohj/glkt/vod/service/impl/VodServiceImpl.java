package com.ohj.glkt.vod.service.impl;

import com.ohj.glkt.utils.exception.GlktException;
import com.ohj.glkt.vod.service.VodService;
import com.ohj.glkt.vod.utils.ConstantPropertiesUtils;
import com.qcloud.vod.VodUploadClient;
import com.qcloud.vod.model.VodUploadRequest;
import com.qcloud.vod.model.VodUploadResponse;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaRequest;
import com.tencentcloudapi.vod.v20180717.models.DeleteMediaResponse;
import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


@Service
@Slf4j
public class VodServiceImpl implements VodService {
    @Override
    public String uploadVideo(MultipartFile file) {
        //设置腾讯云的id和key
        VodUploadClient client = new VodUploadClient(ConstantPropertiesUtils.ACCESS_KEY_ID, ConstantPropertiesUtils.ACCESS_KEY_SECRET);

        //设置要上传的视频
        VodUploadRequest request = new VodUploadRequest();



        String path="/Users/hipopaaaa/Library/Mobile Documents/com~apple~CloudDocs/glkt_parent/service/service_vod/src/main/java/com/ohj/glkt/vod/file"+file.getOriginalFilename();
        InputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            //文件先保存到本地，然后上传到vod后，进行文件的删除
            fileInputStream=file.getInputStream();
            fileOutputStream=new FileOutputStream(path);

            byte[] bytes = new byte[1024];
            int readlen=0;
            while((readlen=fileInputStream.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,readlen);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(fileInputStream!=null){
                    fileInputStream.close();
                }

                if(fileOutputStream!=null){
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        //设置要上传的文件
        request.setMediaFilePath(path);


        try {
            VodUploadResponse response = client.upload("ap-guangzhou", request);
            //获取视频的id
            log.info("Upload FileId = {}", response.getFileId());

            //删除本地文件
            File local = new File(path);
            local.delete();

            return response.getFileId();

        } catch (Exception e) {
            // 业务方进行异常处理
            throw new GlktException(20001,"视频上传失败");
        }




    }

    @Override
    public boolean removeVideo(String fileId) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            Credential cred = new Credential(ConstantPropertiesUtils.ACCESS_KEY_ID,
                            ConstantPropertiesUtils.ACCESS_KEY_SECRET);
            // 实例化要请求产品的client对象,clientProfile是可选的
            VodClient client = new VodClient(cred, "");
            // 实例化一个请求对象,每个接口都会对应一个request对象
            DeleteMediaRequest req = new DeleteMediaRequest();
            //设置要删除视频的id
            req.setFileId(fileId);
            // 返回的resp是一个DeleteMediaResponse的实例，与请求对象对应
            DeleteMediaResponse resp = client.DeleteMedia(req);

            // 输出json格式的字符串回包
            System.out.println(DeleteMediaResponse.toJsonString(resp));

            if(resp==null){
                throw new GlktException(20001,"删除视频失败");
            }
            return true;
        } catch (TencentCloudSDKException e) {
            throw new GlktException(20001,"删除视频失败");
        }
    }
}

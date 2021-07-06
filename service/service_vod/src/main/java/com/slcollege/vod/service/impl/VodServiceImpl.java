package com.slcollege.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.slcollege.commonutils.Result;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import com.slcollege.vod.Utils.InitVideoClient;
import com.slcollege.vod.service.VodService;
import com.slcollege.vod.Utils.ConstantVideoUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {
    // 上传视频到阿里云的方法
    @Override
    public String uploadVideoAliyun(MultipartFile file) {
        try {
            // 节点id-accessKeyId
            // 节点密钥-accessKeySecret
            // fileName-上传文件的文件名
            String fileName = file.getOriginalFilename();

            // title-上传之后文件的标题
            String title = fileName.substring(0, fileName.lastIndexOf("."));

            // inputStream-文件的输入流
            InputStream inputStream = file.getInputStream();

            UploadStreamRequest request = new UploadStreamRequest(ConstantVideoUtils.ACCESS_KEY_ID, ConstantVideoUtils.ACCESS_KEY_SECRET, title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);

            // 获得视频id
            String videoId = null;
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                videoId = response.getVideoId();
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
                videoId = response.getVideoId();
            }
            return videoId;
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 删除多个阿里云视频的方法
    @Override
    public void removeSeveralAliyunVideo(List videoIdList) {
        try {
            // 1.初始化对象
            DefaultAcsClient defaultClient = InitVideoClient.initVideoClient(ConstantVideoUtils.ACCESS_KEY_ID, ConstantVideoUtils.ACCESS_KEY_SECRET);
            // 2.创建视频删除对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            // 3.取出videoIdList集合中的值
            String id = StringUtils.join(videoIdList.toArray(), ",");
            // 4.向request设置视频id
            request.setVideoIds(id);
            // 5.调用初始化对象的方法实现删除
            defaultClient.getAcsResponse(request);

        } catch(Exception e) {
            e.printStackTrace();
            throw new SlCollegeException(20001,"删除视频失败");
        }
    }



}

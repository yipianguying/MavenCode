package com.slcollege.vodtest;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.AcsResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.*;
import org.junit.Test;

import java.util.List;

public class TestVod {
/*    public static void main(String[] args) {
    }*/
    // 上传视频的方法

    public static void uploadVideo() {
        String accessKeyId = "LTAI4FzaV5A1D1kmK65xVB27";
        String accessKeySecret = "RwcEKtJz7x9RUPF3yK7L09p5HzytVP";
        // 上传之后的文件名称
        String title = "What If I Want to Move Faster";
        // 本地文件的路径和名称
        String fileName = "E:/What If I Want to Move Faster.mp4";
        //上传视频的方法
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        /* 可指定分片上传时每个分片的大小，默认为2M字节 */
        request.setPartSize(2 * 1024 * 1024L);
        /* 可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）*/
        request.setTaskNum(1);

        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);

        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
            /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }
    // 根据视频id获取视频播放地址
    public static  void getPlayUrl() throws Exception{
        // 1.根据视频id获取视频播放地址
        // 创建初始化对象
        DefaultAcsClient defaultAcsClient = InitObject.initVodClient("LTAI4FzaV5A1D1kmK65xVB27", "RwcEKtJz7x9RUPF3yK7L09p5HzytVP");

        // 创建获取视频地址request和response
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();

        // 向request对象里面设置视频id
        request.setVideoId("4c453cf7362f487f94bc64e621a8bb7c");

        // 调用初始化对象里面的方法,传递request,获取数据
        response = defaultAcsClient.getAcsResponse(request);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
        //播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        //Base信息
        System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
    }

    // 根据视频id获取视频凭证(视频凭证,相当于视频的许可证)的方法
    public static void getPlayAuth() throws Exception {
        // 根据视频id获取视频凭证(视频凭证,相当于视频的许可证)
        // 创建初始化对象
        DefaultAcsClient defaultAcsClient = InitObject.initVodClient("LTAI4FzaV5A1D1kmK65xVB27", "RwcEKtJz7x9RUPF3yK7L09p5HzytVP");
        // 创建获取视频凭证的request和response对象
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        // 向视频对象里面设置id值
        request.setVideoId("4c453cf7362f487f94bc64e621a8bb7c");
        // 调用初始化对象里面的方法获取视频里面的凭证
        response = defaultAcsClient.getAcsResponse(request);
        System.out.println("playAuth:"+response.getPlayAuth());
    }


}

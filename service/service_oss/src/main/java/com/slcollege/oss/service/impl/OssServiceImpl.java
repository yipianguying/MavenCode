package com.slcollege.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.slcollege.oss.service.OssService;
import com.slcollege.oss.utils.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {


    // 上传头像到oss的方法
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 地域节点
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // ID
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        // 密钥
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        // 创建的存储对象名
        String bucketName = ConstantPropertiesUtil.BUCKET_NAME;

        try {
            // 创建OSS对象。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();
            // 获取文件名称的方法
            String originalFilename = file.getOriginalFilename();

            // 解决上传文件覆盖的问题:在文件名称中添加随机唯一的值,并将-替换
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            //yay89696rew03.jpg
            originalFilename = uuid + originalFilename;
            // 解决文件分类的问题:把文件按照日期进行分类
            // 2020/12/23/test.jpg
            // 获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            // 拼接: 2020/12/23/test.jpg
            originalFilename = datePath + "/" +originalFilename;
            // 调用Oss中的方法实现上传
            // 第一个参数:创建的存储对象的名称
            // 第二个参数:上传到oss的文件的路径和名称
            // 第三个参数: 上传文件输入流
            ossClient.putObject(bucketName, originalFilename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            // 把上传之后文件的路径返回,手动拼接路径
            // https://slcollege-file.oss-cn-qingdao.aliyuncs.com/avatar/test.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;

            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}

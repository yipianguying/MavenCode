package com.slcollege.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    // 上传头像到oss的方法
    String uploadFileAvatar(MultipartFile file);
}

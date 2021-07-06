package com.slcollege.eduservice.entity.chapter;

import lombok.Data;

/*
*  小节的实体类
* */
@Data
public class VideoVo {
    // video表的id
    private String id;

    // 标题
    private String title;

    // 视频id
    private String videoSourceId;



}

package com.slcollege.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/*
*  章节的实体类
* */
@Data
public class ChapterVo {
    // chapter表的id
    private String id;

    // 标题
    private String title;

    // 在章节里面表示小节
    private List<VideoVo> children = new ArrayList<>();
}

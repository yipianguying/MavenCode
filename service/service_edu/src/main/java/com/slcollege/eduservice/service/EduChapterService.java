package com.slcollege.eduservice.service;

import com.slcollege.eduservice.entity.EduChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.slcollege.eduservice.entity.chapter.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
public interface EduChapterService extends IService<EduChapter> {
    // 返回课程大纲列表的方法,根据课程id进行查询,找到对应的课程。
    List<ChapterVo> getChapterVideoByCourseId(String courseId);

    // 删除章节的方法
    boolean deleteChapter(String chapterId);

    // 2.根据课程id删除章节
    void removeChapterByCourseId(String courseId);
}

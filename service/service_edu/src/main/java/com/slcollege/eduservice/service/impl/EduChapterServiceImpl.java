package com.slcollege.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.eduservice.entity.EduChapter;
import com.slcollege.eduservice.entity.EduVideo;
import com.slcollege.eduservice.entity.chapter.ChapterVo;
import com.slcollege.eduservice.entity.chapter.VideoVo;
import com.slcollege.eduservice.mapper.EduChapterMapper;
import com.slcollege.eduservice.service.EduChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slcollege.eduservice.service.EduVideoService;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-25
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    // 注入课程小节的service
    @Autowired
    private EduVideoService videoService;

    // 返回课程大纲列表的方法,根据课程id进行查询,找到对应的课程。
    @Override
    public List<ChapterVo> getChapterVideoByCourseId(String courseId) {
        // 1.根据课程id查询课程里面所有的章节
        QueryWrapper<EduChapter> chapter = new QueryWrapper<>();
        chapter.eq("course_id",courseId);
        // 所有的章节的集合
        List<EduChapter> eduChaptersList = baseMapper.selectList(chapter);

        // 2. 根据课程id查询课程里面所有的小节
        QueryWrapper<EduVideo> video = new QueryWrapper<>();
        video.eq("course_id",courseId);
        List<EduVideo> eduVideoList = videoService.list(video);

        // 创建list集合用于封装最终的数据
        List<ChapterVo> finalList = new ArrayList<>();

        // 3. 遍历查询章节list集合进行封装
        // 遍历查询章节list集合,得到每个章节
        for (EduChapter eduChapter : eduChaptersList) {
            // eduChapter赋值到ChapterVo中
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);
            // 把chapterVo放到最终的list集合中
            finalList.add(chapterVo);

            // 4. 遍历查询小节list集合进行封装
            // 创建集合,用于封装章节中的小节
            List<VideoVo> videoVoList = new ArrayList<>();
            // 得到每个小节
            for (EduVideo eduVideo : eduVideoList) {
                // 判断：小节里面的chapterId和章节里面的id是否相同
                if(eduVideo.getChapterId().equals(eduChapter.getId())) {
                    // 进行封装
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);
                    // 放到小节封装集合
                    videoVoList.add(videoVo);
                }
            }
            // 把封装之后的小节list集合,放到章节对象里面
            chapterVo.setChildren(videoVoList);
        }

        return finalList;
    }

    // 删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        // 根据chapterId 章节id 查询小节表,如果查询到了数据,则不进行删除
        QueryWrapper<EduVideo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id",chapterId);
        // List<EduVideo> list = videoService.list(queryWrapper);
        //
        int count = videoService.count(queryWrapper);
        // 判断-能否查询小节
        if(count > 0){
            // 查询出有小节则不能进行删除
            throw new SlCollegeException(20001,"不能删除");
        } else {
            // 查询出没有小节则可以进行删除
            // 删除章节
            int result = baseMapper.deleteById(chapterId);

            return result > 0;
        }
    }

    // 2.根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        baseMapper.delete(chapterQueryWrapper);
    }


}

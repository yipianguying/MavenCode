package com.slcollege.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.eduservice.entity.EduSubject;
import com.slcollege.eduservice.entity.excel.SubjectData;
import com.slcollege.eduservice.service.EduSubjectService;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;

/*
*  监听器类
*   因为SubjectExcelListener不能交给spring进行管理,需要自己new,不能注入其他对象
*    在实现数据库的操作时,比较麻烦,需要自己写jdbc代码
* */
public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    // 自定义
    public EduSubjectService subjectService;

    public SubjectExcelListener() {
    }
    public SubjectExcelListener(EduSubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // 读取excel中的内容,一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        if (subjectData == null) {
            throw new SlCollegeException(20001,"文件数据为空");
        }
        // 一行一行进行读取, 每次读取有两个值, 第一个值一级分类 第二个值二级分类
        // 判断一级分类是否成功
        EduSubject oneSubject = this.existOneSubject(subjectData.getOneSubjectName(),subjectService);
        // 没有相同的一级分类,进行添加,有相同的一级分类,则不添加
        if(oneSubject == null) {
            oneSubject = new EduSubject();
            oneSubject.setParentId("0");
            // 设置一级分类名称
            oneSubject.setTitle(subjectData.getOneSubjectName());

            subjectService.save(oneSubject);
        }

        // 获取一级分类的id值
        String pid = oneSubject.getId();
        // 添加二级分类
        // 判断二级分类是否重复
        EduSubject twoSubject = this.existTwoSubject(subjectData.getTwoSubjectName(), subjectService, pid);
        if(twoSubject == null) {
            twoSubject = new EduSubject();
            twoSubject.setParentId(pid);
            // 设置二级分类名称
            twoSubject.setTitle(subjectData.getTwoSubjectName());

            subjectService.save(twoSubject);
        }

    }
    // 判断一级分类不能重复添加
    private EduSubject existOneSubject(String name,EduSubjectService subjectService) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id","0");

        EduSubject one = subjectService.getOne(queryWrapper);

        return  one;
    }

    // 判断二级分类不能重复添加
    private EduSubject existTwoSubject(String name,EduSubjectService subjectService,String pid) {
        QueryWrapper<EduSubject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",name);
        queryWrapper.eq("parent_id",pid);

        EduSubject two = subjectService.getOne(queryWrapper);

        return  two;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

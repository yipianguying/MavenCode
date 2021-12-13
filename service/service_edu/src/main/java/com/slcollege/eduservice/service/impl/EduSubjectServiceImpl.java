package com.slcollege.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slcollege.eduservice.entity.EduSubject;
import com.slcollege.eduservice.entity.excel.SubjectData;
import com.slcollege.eduservice.entity.subject.OneSubject;
import com.slcollege.eduservice.entity.subject.TwoSubject;
import com.slcollege.eduservice.listener.SubjectExcelListener;
import com.slcollege.eduservice.mapper.EduSubjectMapper;
import com.slcollege.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-23
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    // 添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            // 得到文件输入流
            InputStream input = file.getInputStream();
            // 调用EasyExcel的方法进行读取
            EasyExcel.read(input, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 课程分类列表(树型)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        // 查询所有的一级分类: parentId=0
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        // eq: 等于
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        // 查询所有的二级分类: parentId!=0
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        // ne: 不等于
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        // 创建list集合,用于存储最终的数据
        List<OneSubject> oneFinalSubjectList = new ArrayList<>();

        // 封装一级分类
        // 查询出来所有的一级分类list集合遍历,得到每一个一级分类对象,获取每个一级分类对象值
        // 封装到要求的list集合里面 finalSubjectList
        // oneSubjectList遍历
        for (EduSubject subject : oneSubjectList) {
            // 得到每个oneSubjectList中的每个对象并放在oneFinalSubjectList里面
            OneSubject oneSubject = new OneSubject();
            // oneSubject.setId(subject.getId());
            // oneSubject.setTitle(subject.getTitle());
            BeanUtils.copyProperties(subject,oneSubject);

            oneFinalSubjectList.add(oneSubject);

            // 在一级分类里面循环遍历查询到所有的二级分类
            // 创建list集合封装每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();
            // 遍历二级分类list集合
            for (EduSubject eduSubject : twoSubjectList) {
                // 获取每一个二级分类并判断二级分类的parentId和一级分类的id值是否一样
                if(eduSubject.getParentId().equals(subject.getId())) {
                    // 如果一样,则将获取到的二级分类的对象放在twoFinalSubjectList里面
                    TwoSubject twoSubject = new TwoSubject();
                    // 封装二级分类
                    BeanUtils.copyProperties(eduSubject, twoSubject);
                    twoFinalSubjectList.add(twoSubject);
                }
            }
            // 把一级分类下面的所有二级分类放在一级分类里面
            oneSubject.setChildren(twoFinalSubjectList);
        }

        return oneFinalSubjectList;
    }


}

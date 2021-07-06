package com.slcollege.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slcollege.commonutils.Result;
import com.slcollege.eduservice.entity.EduTeacher;
import com.slcollege.eduservice.entity.vo.TeacherQuery;
import com.slcollege.eduservice.service.EduTeacherService;
import com.slcollege.servicebase.exceptionhandler.SlCollegeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author 一片孤影
 * @since 2020-12-15
 */
@Api(description= "教师管理")
@RestController
@RequestMapping("/eduservice/teacher")
@CrossOrigin  //解决跨域问题
public class EduTeacherController {

    // 访问地址: http://localhost:8001/eduservice/teacher/findAll
    // 把service注入
    @Autowired
    private EduTeacherService teacherService;

    // 1.查询教师表中的所有数据
    // rest风格:它是一种针对网络应用的设计和开发方式，可以降低开发的复杂性，提高系统的可伸缩性。
    @GetMapping("findAll")
    public Result findAllTeacher() {
        // 调用service方法来实现查询所有操作,查询列表
        List<EduTeacher> list = teacherService.list(null);
        return Result.ok().data("items",list);
    }

    // 2.逻辑删除教师的方法
    //@PathVariable注解:接收请求路径中占位符的值
    @ApiOperation(value = "根据ID删除教师")
    @DeleteMapping("{id}") //"{id}":id需要通过路径进行传递
    public Result removeTeacher(@ApiParam(name = "id", value = "讲师ID", required = true)
                                     @PathVariable String id){//@PathVariable String id:获取路径中的id值
        // 根据 ID 删除
        boolean flag = teacherService.removeById(id);
        if(flag){
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    // 3.分页查询教师方法
    // current:当前页,limit:每页记录数
    @ApiOperation(value = "分页教师列表")
    @GetMapping("pageTeacher/{current}/{limit}")
    public Result pageListTeacher(@PathVariable long current,
                                    @PathVariable long limit) {
        // 创建Page对象
        Page<EduTeacher> pageTeacher = new Page<>(1,3);
        //自定义异常
        try {
            int i = 1/0;
        }catch(Exception e) {
            // 执行自定义异常
            throw new SlCollegeException(20001,"执行了自定义异常处理");
        }

        //调用方法实现分页
        //调用方法时,底层封装,把分页所有数据封装到pageTeacher对象里面
        teacherService.page(pageTeacher,null);
        // 总记录数
        long total = pageTeacher.getTotal();
        // 数据list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        // 返回数据的两种方式
        // 第一种
        // Map map = new HashMap();
        // map.put("total",total);
        // map.put("row",records);
        // return Result.ok().data(map);
        //第二种
        return Result.ok().data("total",total).data("rows",records);
    }

    // 4.条件查询带分页
    @ApiOperation(value = "条件分页教师带查询")
    @PostMapping("pageTeacherCondition/{current}/{limit}")
    public Result pageTeacherCondition(@PathVariable long current, @PathVariable long limit,
                                       @RequestBody(required = false) TeacherQuery teacherQuery) {//@RequestBody:使用json形式传递数据,
                                                                                    // 把json数据封装到对应对象里面,需要使用post提交方式
        // 创建一个page对象
        Page<EduTeacher> pageTeacher = new Page<>(current,limit);
        //构建条件
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        // 多条件组合查询
        // mybatis-动态sql
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        // 判断条件值是否为空，如果不为空拼接条件'
        if(!StringUtils.isEmpty(name)) {
            // 构建条件
            // like模糊查询
            wrapper.like("name",name);
        }
        if(!StringUtils.isEmpty(level)) {
            // 等于 =
            wrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(begin)) {
            // 大于等于 >=
            wrapper.ge("gmt_create",begin);
        }
        if(!StringUtils.isEmpty(end)) {
            // 小于等于 <=
            wrapper.le("gmt_create",end);
        }
        // 根据创建时间排序-降序排序
        wrapper.orderByDesc("gmt_create");

        // 调用方法实现条件分页查询
        teacherService.page(pageTeacher,wrapper);

        // 总记录数
        long total = pageTeacher.getTotal();
        // 数据list集合
        List<EduTeacher> records = pageTeacher.getRecords();
        return Result.ok().data("total",total).data("rows",records);
    }

    // 5.添加教师的方法
    @PostMapping("addTeacher")
    public Result addTeacher(@RequestBody EduTeacher eduTeacher) {
        // 插入一条记录（选择字段，策略插入）
        boolean save = teacherService.save(eduTeacher);

        if(save) {
            return Result.ok();
        }else{
            return Result.error();
        }
    }

    // 根据教师id进行查询
    @GetMapping("getTeacher/{id}")
    public Result getTeacher(@PathVariable String id) {
        // 根据 ID 查询
        EduTeacher eduTeacher = teacherService.getById(id);
        return Result.ok().data("teacher",eduTeacher);
    }

    // 6.修改教师的方法
    @PostMapping("updateTeacher")
    public Result updateTeacher(@RequestBody EduTeacher eduTeacher) {
        // 根据 ID 选择修改
        boolean flag = teacherService.updateById(eduTeacher);
        if(flag) {
            return Result.ok();
        }else{
            return Result.error();
        }
    }


}


package com.slcollege.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {

    public static void main(String[] args) {
        // 实现excel写的操作
        // 1.设置文件夹地址和excel文件名称
        // String fileName = "D:\\SpringBoot\\Excel\\writer.xlsx";
        //
        // // 2.调用easyexcel里面的方法实现写操作
        //     // 第一个参数:文件的路径名称,第二个参数:实体类class
        // EasyExcel.write(fileName,DemoData.class).sheet("学生列表").doWrite(getExcelData());

        // 实现excel的读操作
        String fileName = "D:\\SpringBoot\\Excel\\writer.xlsx";

        EasyExcel.read(fileName,DemoData.class,new ExcelListener()).sheet().doRead();
    }

    // 创建一个方法,令方法返回list集合
    private static List<DemoData> getExcelData() {
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("lucy"+i);

            list.add(data);
        }
        return list;
    }
}

package com.slcollege.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

/*
*   读取excel文件的监听器类
* */
public class ExcelListener  extends AnalysisEventListener<DemoData> {

    // 一行一行的读取excel的内容
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("****"+demoData);
    }

    // 读取表头中的内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头:" + headMap);
    }

    // 读取完成之后的方法
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

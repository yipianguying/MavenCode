package com.crowd.funding.utils;

import java.util.List;

/*
*  页面分页查询的工具类
* */
public class PageUtils<T> {

    // 定义和分页有关的变量
    private Integer pageNumber;     //  编号
    private Integer pageSize;   //  每一页多少条记录
    private List data;          //当前页的数据
    private Integer totalSize;  //  总记录数
    private Integer totalNumber; // 每一页的记录的编号

    public PageUtils(Integer pageNumber, Integer pageSize) {
        if(pageNumber <= 0) {
            this.pageNumber = 1;
        } else {
            this.pageNumber = pageNumber;
        }
        if(pageSize <= 0) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize;
        }
    }


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
        this.totalNumber = (totalSize%pageSize) == 0?(totalSize/pageSize):(totalSize/pageSize + 1);
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    private void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    // 取开始索引的方法
    public Integer getStartIndex() {
        return (this.pageNumber-1)*pageSize;
    }
}
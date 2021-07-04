package com.crowd.funding.utils;
/*
*  工具类-Ajax异步请求的工具类
* */
public class AjaxResultUtils {
    // 判断异步请求操作是否成功的变量
    private boolean success;
    // 判断返回的消息
    private String message;
    // 设置分页数
    private PageUtils pages;
    // 数据
    private Object data ;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PageUtils getPages() {
        return pages;
    }

    public void setPages(PageUtils pages) {
        this.pages = pages;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

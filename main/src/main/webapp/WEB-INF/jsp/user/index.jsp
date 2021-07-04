<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/main.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 用户维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <%@ include file="/WEB-INF/jsp/topMessage/top.jsp" %>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <div class="tree">
                <%@include file="/WEB-INF/jsp/common/menu.jsp" %>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="queryText" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="queryBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" id="deleteBatchBtn" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${APP_PATH}/user/toUserAdd.htm'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input id="allCheckbox" type="checkbox"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <ul class="pagination">
                                    </ul>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script  type="text/javascript" src="${APP_PATH}/jquery/layer/layer.js" ></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
        queryUserPage(1);
        showMenu();
    });

    // 被点击的元素标红,并默认展开
    function showMenu(){
        var href = window.location.href ;
        var host = window.location.host ;
        var index = href.indexOf(host);
        var path = href.substring(index + host.length);

        var contextPath = "${APP_PATH}";
        var pathAddress = path.substring(contextPath.length);

        var alink = $(".list-group a[href*='"+pathAddress+"']");

        alink.css("color","red");

        alink.parent().parent().parent().removeClass("tree-closed");
        alink.parent().parent().show();
    }


    $("tbody .btn-success").click(function(){
        window.location.href = "assignRole.html";
    });
    $("tbody .btn-primary").click(function(){
        window.location.href = "edit.html";
    });
    // 点击跳转到上一页的方法
    function pageChange(pageNumber) {
        <%--window.location.href = "${APP_PATH}/user/userList.do?pageNumber=" +pageNumber;--%>
        // 调用queryUserPage方法
        queryUserPage(pageNumber);
    }
    /* 查询分页的方法*/
    // 定义layer弹窗的变量,可以显示或者关闭弹窗
    var loadingIndex = -1;
    // 定义数据对象jsonObj
    var jsonObj = {
        "pageNumber": 1,
        "pageSize": 10,
    };
    function queryUserPage(pageNumber) {
        jsonObj.pageNumber = pageNumber;
        $.ajax({
            type: "POST",
            data: jsonObj,
            // 跳转到后台的方法
            url: "${APP_PATH}/user/userList.do",
            beforeSend: function () {

                // 调用layer组件
                loadingIndex = layer.load(2,{time: 10*1000});
                // 继续发起请求
                return true;
            },
            success: function (result) {
                // 关闭Layer弹层
                layer.close(loadingIndex);
                if(result.success) {
                    // 获取分页数
                    var pages = result.pages;
                    // 获取分页的相关数据
                    var datas = pages.data;

                    var content = '';
                    // 使用循环得到用户数据
                    $.each(datas,function (i, n) {
                        content +='<tr>';
                        content +=' <td>'+(i+1)+'</td>';
                        content +=' <td><input type="checkbox" id="'+n.id+'"></td>';
                        content +=' <td>'+n.loginacct+'</td>';
                        content +=' <td>'+n.username+'</td>';
                        content +=' <td>'+n.email+'</td>';
                        content +=' <td>';
                        content +='     <button type="button" onclick="window.location.href=\'${APP_PATH}/user/assignRole.htm?id='+n.id+'\'" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>';
                        content +='     <button type="button" onclick="window.location.href=\'${APP_PATH}/user/toUserUpdate.htm?id='+n.id+'\'"  class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i><button>';
                        content +='     <button type="button" onclick="deleteUser('+n.id+',\''+n.loginacct+'\')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
                        content +=' </td>';
                        content +='</tr>';
                    });
                    // alert("局部刷新"+data);
                    // 得到的用户数据显示在页面上
                    $("tbody").html(content);
                    // 得到分页条
                    var contentBar = '';
                    // 上一页
                    if(pages.pageNumber == 1) {
                        contentBar+= '<li class="disabled"><a href="#">上一页</a></li>';
                    } else {
                        contentBar+= '<li><a href="#" onclick="pageChange('+(pages.pageNumber-1)+')">上一页</a></li>';
                    }
                    for (var i=1; i<=pages.totalNumber;i++) {
                        contentBar+='<li ';
                            if(pages.pageNumber==i) {
                                contentBar+='class="active"';
                            }
                            contentBar+='><a href="#" onclick="pageChange('+i+')">'+i+'</a></li>';

                    }
                    // 下一页
                    if(pages.pageNumber == pages.totalNumber) {
                        contentBar+= '<li class="disabled"><a href="#">下一页</a></li>';
                    } else {
                        contentBar+= '<li><a href="#" onclick="pageChange('+(pages.pageNumber+1)+')">下一页</a></li>';
                    }
                    // 得到分页条的样式
                    $(".pagination").html(contentBar);
                } else {
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            },
            error: function() {
                layer.msg("加载数据失败!", {time:1000, icon:5, shift:6});
            }

        });
    }
    /*
    *  查询按钮的方法
    *  */
    $('#queryBtn').click(function () {
       var queryText = $("#queryText").val();
       // 将查询框中的数据赋值到jsonObj中
       jsonObj.queryText   = queryText;
       // 查询第一页
       queryUserPage(1);
    });
    /*
    *  删除按钮的方法
    * */
    function deleteUser(id,loginacct) {
        // 定义一个变量,能关闭layer层
        var loadingIndex = -1;
        // 询问是否删除
        layer.confirm("确定要删除["+loginacct+"]用户吗",  {icon: 3, title:'提示'}, function(cindex){
            layer.close(cindex);
            // 发送ajax请求
            $.ajax({
                type: "POST",
                data: {
                    "id": id
                },
                url: "${APP_PATH}/user/userDelete.do",
                beforeSend: function () {
                    // 返回true
                    return true
                },
                success: function (result) {
                    if(result.success) {
                        window.location.href="${APP_PATH}/user/toUserList.htm"
                    } else {
                        layer.msg("删除用户失败", {time:1000, icon:5, shift:6});
                    }
                },
                error: function () {
                    layer.msg("删除失败!", {time:1000, icon:5, shift:6});
                }
            });
        }, function(cindex){
            layer.close(cindex);
        });

    }

    /* 批量删除时选中表头*/
    $("#allCheckbox").click(function() {
        // 获取checkbox的状态-是否被选中
        var checkedStatus = this.checked;
        // alert(checkedStatus);

        // 获取复选框的选中数量
        var tbodyCheckBox = $("tbody tr td input[type='checkbox']");
        $.each(tbodyCheckBox,function (i,n) {
            n.checked = checkedStatus;
        });
        // $("tbody tr td input[type='checkbox']").prop("checked", checkedStatus);
        // $("tbody tr td input[type='checkbox']").attr("checked", checkedStatus);

    });
    /* 批量删除的方法*/
    $("#deleteBatchBtn").click(function() {
        // 获取选中的复选框
        var selectCheckBox = $("tbody tr td input:checked");

        if(selectCheckBox.length==0) {
            layer.msg("至少选择一个用户进行删除,请先选择用户!", {time:1000, icon:5, shift:6});
            return false;
        }
        // 定义能够将多个id拼接起来的字符串
/*        var idString = "";

        $.each(selectCheckBox,function(i,n) {

            if(i!=0) {
                idString += "&";
            }
            idString += "id=" +n.id;
        });*/

        var jsonObj = {};

        $.each(selectCheckBox,function(i,n){
            jsonObj["datas["+i+"].id"] = n.id;
            jsonObj["datas["+i+"].loginacct"] = n.name;
        });
        // alert(idString);
        // 定义一个变量,能关闭layer层
        var loadingIndex = -1;
        // 询问是否删除
        layer.confirm("确定要删除这些用户吗",  {icon: 3, title:'提示'}, function(cindex){
            layer.close(cindex);
            // 发送ajax请求
            $.ajax({
                type: "POST",
                // data: idString,
                data : jsonObj,
                url: "${APP_PATH}/user/userDeleteBatch.do",
                beforeSend: function () {

                    // 返回true
                    return true;
                },
                success: function (result) {
                    if(result.success) {
                        window.location.href="${APP_PATH}/user/toUserList.htm"
                    } else {
                        layer.msg("删除用户失败", {time:1000, icon:5, shift:6});
                    }
                },
                error: function () {
                    layer.msg("删除失败!", {time:1000, icon:5, shift:6});
                }
            });
        }, function(cindex){
            layer.close(cindex);
        });
    })

</script>
<%--<script type="text/javascript" src="${APP_PATH}/script/menu.js"></script>--%>
</body>
</html>

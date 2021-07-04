<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="${APP_PATH}/css/doc.min.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 用户维护</a></div>
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
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" class="form-inline">
                        <div class="form-group">
                            <label for="leftRoleList">未分配角色列表</label><br>
                            <select id="leftRoleList" class="form-control" multiple size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${leftRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li  id="leftToRightBtn"   class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="rightToLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="rightRoleList">已分配角色列表</label><br>
                            <select id="rightRoleList" class="form-control" multiple size="10" style="width:200px;overflow-y:auto;">
                                <c:forEach items="${rightRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题1</h4>
                    <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                </div>
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题2</h4>
                    <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                </div>
            </div>
            <!--
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
            -->
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
    // 从左向右-分配角色
    $("#leftToRightBtn").click(function () {
        // 获取页面上左边选中的值
        var selectedOption = $("#leftRoleList option:selected");
        // alert(selectedOption.length);

        // 定义一个json变量
        var jsonObj = {
            // param相当于request.getParameter("id"),获取参数的id
            userId: "${param.id}",
        };
        $.each(selectedOption,function (i,n) {
            jsonObj["ids["+i+"]"] = this.value;
        });

        // 定义layer弹窗的变量,可以显示或者关闭弹窗
        var loadingIndex = -1;
        // 发起ajax请求
        $.ajax({
            type: "POST",
            data: jsonObj,
            url: "${APP_PATH}/user/userAssignRole.do",
            beforeSend: function () {
                loadingIndex =  layer.load(2,{time: 10*1000});
                return true;
            },
            success: function (result) {
                layer.close(loadingIndex);
                if(result.success) {
                    // 将选中的值复制并追加到右边
                    $("#rightRoleList").append(selectedOption.clone());
                    // 删除选中的值
                    selectedOption.remove();
                } else {
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            },
            error: function () {
                layer.msg("分配失败!", {time:1000, icon:5, shift:6});
            }
        })
    });

    // 从右向左-取消分配角色
    $("#rightToLeftBtn").click(function () {
        // 获取页面上右边选中的值
        var selectedOption = $("#rightRoleList option:selected");
        // alert(selectedOption.length);

        // 定义一个json变量
        var jsonObj = {
            // param相当于request.getParameter("id"),获取参数的id
            userId: "${param.id}",
        };
        $.each(selectedOption,function (i,n) {
            jsonObj["ids["+i+"]"] = this.value;
        });

        // 定义layer弹窗的变量,可以显示或者关闭弹窗
        var loadingIndex = -1;
        // 发起ajax请求
        $.ajax({
            type: "POST",
            data: jsonObj,
            url: "${APP_PATH}/user/unUserAssignRole.do",
            beforeSend: function () {
                loadingIndex =  layer.load(2,{time: 10*1000});
                return true;
            },
            success: function (result) {
                layer.close(loadingIndex);
                if(result.success) {
                    // 将选中的值复制并追加到左边
                    $("#leftRoleList").append(selectedOption.clone());
                    // 删除选中的值
                    selectedOption.remove();
                } else {
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            },
            error: function () {
                layer.msg("分配失败!", {time:1000, icon:5, shift:6});
            }
        })
    });
</script>
</body>
</html>


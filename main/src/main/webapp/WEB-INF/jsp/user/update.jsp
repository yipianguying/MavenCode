<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
                <li class="active">修改</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据<div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i class="glyphicon glyphicon-question-sign"></i></div></div>
                <div class="panel-body">
                    <form role="form" id="updateForm">
                        <div class="form-group">
                            <label for="floginacct">登陆账号</label>
                            <input type="text" class="form-control" id="floginacct" value="${user.loginacct}">
                        </div>
                        <div class="form-group">
                            <label for="fusername">用户名称</label>
                            <input type="text" class="form-control" id="fusername" value="${user.username}">
                        </div>
                        <div class="form-group">
                            <label for="femail">邮箱地址</label>
                            <input type="email" class="form-control" id="femail" value="${user.email}">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button id="updateBtn" type="button" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 修改</button>
                            <button id="resetBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置</button>
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
    // 点击修改的按钮
    $("#updateBtn").click(function () {
        var floginacct = $("#floginacct");
        var fusername = $("#fusername");
        var femail = $("#femail");
        // 判断账号是否为空
        if($.trim(floginacct.val()) == "") {
            // 使用layer组件提示信息
            layer.msg("用户账号不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 如果输入的账号是空格,则将文本框清空
                floginacct.val("");
                // 获取没有输入数据的文本框
                floginacct.focus();
            });
            // return false，下面的代码不再执行
            return false;
        }
        // 判断密码是否为空
        if($.trim(fusername.val()) == "") {
            layer.msg("用户名称不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 如果输入密码是空格,则将文本框清空
                fusername.val("");
                // 获取没有输入数据的文本框
                fusername.focus();
            });
            // return false，下面的代码不再执行
            return false;
        }
        // 判断类型是否为空
        if($.trim(femail.val()) == "") {
            layer.msg("邮箱地址不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 如果输入邮箱地址为空,则将文本框清空
                femail.val("");
                // 获取没有输入数据的文本框
                femail.focus();
            });
            // alert("用户类型不能为空,请重新输入");
            // return false，下面的代码不再执行
            return false;
        }
        // 定义一个变量,能关闭layer层
        var loadingIndex = -1;

        $.ajax({
            type: "POST",
            data: {
                "loginacct": floginacct.val(),
                "username": fusername.val(),
                "email": femail.val(),
                // 取出当前id
                "id": "${user.id}"
            },
            url: "${APP_PATH}/user/userUpdate.do",
            beforeSend: function () {
                // 添加数据提示处理中
                loadingIndex = layer.msg('处理中',{icon: 16});
                //
                return true;
            },
            success: function (result) {
                if(result.success) {
                    window.location.href="${APP_PATH}/user/toUserList.htm"
                } else {
                    layer.msg("修改用户失败", {time:1000, icon:5, shift:6});
                }
            },
            error: function () {
                layer.msg("修改失败!", {time:1000, icon:5, shift:6});
            }
        });
    });
    // 重置按钮
    $("#resetBtn").click(function () {
        $("#updateForm")[0].reset();
    });
</script>
</body>
</html>

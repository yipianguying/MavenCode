<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/login.css">
    <style>

    </style>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <div><a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a></div>
        </div>
    </div>
</nav>

<div class="container">
    <form id="loginForm" action="${APP_PATH}/register.do" method="post" class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-log-in"></i> 用户注册</h2>
        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="floginacct" name="loginacct" placeholder="请输入登录账号" autofocus>
            <span class="glyphicon glyphicon-user form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="password" class="form-control"  id="fuserpswd" name="userpswd" placeholder="请输入登录密码" style="margin-top:10px;">
            <span class="glyphicon glyphicon-lock form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <input type="text" class="form-control" id="femail" name="email" placeholder="请输入邮箱地址"  style="margin-top:10px;">
            <span class="glyphicon glyphicon glyphicon-envelope form-control-feedback"></span>
        </div>
        <div class="form-group has-success has-feedback">
            <select id="ftype" class="form-control" name="type">
                <option value="member" selected>会员(个人)</option>
                <option value="enterprise">会员(企业)</option>
                <option value="user">管理</option>
            </select>
        </div>
        <div class="checkbox">
            <br>
            <label>
            </label>
            <label style="float:right">
                <a href="${APP_PATH}/loginUser.htm">我有账号</a>
            </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="doRegister()" >注册</a>
        <%--exception为异常对象--%>
        ${exception.message}
    </form>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script  type="text/javascript" src="${APP_PATH}/jquery/layer/layer.js" ></script>
<script>
    <%--登录的方法-ajax异步请求的方式--%>
    function doRegister() {
        var floginacct = $("#floginacct");
        var fuserpswd = $("#fuserpswd");
        var femail = $("#femail");
        var ftype = $("#ftype");
        // 判断账号是否为空
        if($.trim(floginacct.val()) == "") {
            // alert("用户账号不能为空,请重新输入");
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
        if($.trim(fuserpswd.val()) == "") {
            // alert("用户密码不能为空,请重新输入");
            layer.msg("用户密码不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 如果输入密码是空格,则将文本框清空
                fuserpswd.val("");
                // 获取没有输入数据的文本框
                fuserpswd.focus();
            });
            // return false，下面的代码不再执行
            return false;
        }
        // 判断邮箱是否为空
        if($.trim(femail.val()) == "") {
            // alert("用户密码不能为空,请重新输入");
            layer.msg("用户邮箱不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 如果输入密码是空格,则将文本框清空
                femail.val("");
                // 获取没有输入数据的文本框
                femail.focus();
            });
            // return false，下面的代码不再执行
            return false;
        }
        // 判断类型是否为空
        if(ftype.val() == "") {
            layer.msg("用户类型不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
                // 获取没有输入数据的文本框
                ftype.focus();
            });
            // alert("用户类型不能为空,请重新输入");
            // return false，下面的代码不再执行
            return false;
        }
        // 定义一个变量,能关闭layer层
        var loadingIndex = -1;
        // 定义一个变量,是否[记住我]
        // var flag =  $("#rememberMe")[0].checked;
        $.ajax({
            type : "POST",
            data : {
                loginacct :  floginacct.val(),
                userpswd : fuserpswd.val(),
                email: femail.val(),
                type : ftype.val()
            },
            // 跳转到后台代码
            url : "${APP_PATH}/register.do",
            // 一般做表单数据校验
            beforeSend : function() {
                // 登录时提示处理中
                loadingIndex = layer.msg('处理中',{icon: 16});
                // 一般做表单数据校验,
                return true;
            },
            // 注册成功的提示
            success : function(result) {
                layer.close(loadingIndex);
                // 取出json数据
                if(result.success) {
                    window.location.href = "${APP_PATH}/loginUser.htm";
                } else {
                    layer.msg(result.message,{time:1000, icon:5, shift:6});
                    // alert("登录失败");
                }
            },
            // 出现异常
            error:function () {
                layer.msg("注册错误!",{time:1000, icon:5, shift:6});
            }
        });
        // $("#loginForm").submit();

        /*        var type = $(":selected").val();
                if ( type == "user" ) {
                    window.location.href = "main.html";
                } else {
                    window.location.href = "index.html";
                }*/
    }
</script>
</body>
</html>

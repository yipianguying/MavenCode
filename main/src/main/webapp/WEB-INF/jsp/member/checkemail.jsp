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
	<link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
	<link rel="stylesheet" href="${APP_PATH }/css/theme.css">
	<style>
#footer {
    padding: 15px 0;
    background: #fff;
    border-top: 1px solid #ddd;
    text-align: center;
}
	</style>
  </head>
  <body>
 <div class="navbar-wrapper">
      <div class="container">
			<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			  <div class="container">
				<div class="navbar-header">
				  <a class="navbar-brand" href="index.html" style="font-size:32px;">众筹网-创意产品众筹平台</a>
				</div>
            <div id="navbar" class="navbar-collapse collapse" style="float:right;">
                <%@include file="/WEB-INF/jsp/common/memberInfo.jsp"%>
            </div>
			  </div>
			</nav>

      </div>
    </div>

    <div class="container theme-showcase" role="main">
      <div class="page-header">
        <h1>实名认证 - 申请</h1>
      </div>

		<ul class="nav nav-tabs" role="tablist">
		  <li role="presentation" ><a href="#"><span class="badge">1</span> 基本信息</a></li>
		  <li role="presentation" ><a href="#"><span class="badge">2</span> 资质文件上传</a></li>
		  <li role="presentation" class="active"><a href="#"><span class="badge">3</span> 邮箱确认</a></li>
		  <li role="presentation"><a href="#"><span class="badge">4</span> 申请确认</a></li>
		</ul>
        
		<form role="form" style="margin-top:20px;">
		  <div class="form-group">
			<label for="memberEmail">邮箱地址</label>
			<input type="text" class="form-control" id="memberEmail" value="${memberLogin.email }" placeholder="请输入用于接收验证码的邮箱地址">
		  </div>
		  <button type="button" id="nextBtn" class="btn btn-success">下一步</button>
		</form>
		<hr>
    </div> <!-- /container -->
    <%@include file="/WEB-INF/jsp/common/memberBottom.jsp"%>
 	<script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
    <script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
	<script src="${APP_PATH }/script/docs.min.js"></script>
    <script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>

 <script>
        $('#myTab a').click(function (e) {
          e.preventDefault();
          $(this).tab('show');
        });    
        
        
        $("#nextBtn").click(function(){
        	var email = $("#memberEmail");
			if($.trim(email.val()) == "") {
				// alert("用户账号不能为空,请重新输入");
				// 使用layer组件提示信息
				layer.msg("邮箱地址不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
					// 如果输入的邮箱地址是空格,则将文本框清空
					email.val("");
					// 获取没有输入数据的文本框
					email.focus();
				});
				// return false，下面的代码不再执行
				return false;
			}
        	$.ajax({
        		type : "POST",
        		url  : "${APP_PATH}/member/startProcess.do",
        		data : {
        			"email" : email.val()
        		},
        		success : function(result) {
        			if ( result.success ) {
                        layer.msg("正在发送验证码,请注意邮箱提示!", {time:1000, icon:6, shift:6});
        				window.location.href = "${APP_PATH}/member/apply.htm";
        			} else {
        				layer.msg("发送验证码失败", {time:1000, icon:5, shift:6});
        			}
        		}
        	});

        });
        
        
        
        
	</script>
  </body>
</html>
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
		  <li role="presentation" class="active"><a href="#"><span class="badge">1</span> 基本信息</a></li>
		  <li role="presentation"><a href="#"><span class="badge">2</span> 资质文件上传</a></li>
		  <li role="presentation"><a href="#"><span class="badge">3</span> 邮箱确认</a></li>
		  <li role="presentation"><a href="#"><span class="badge">4</span> 申请确认</a></li>
		</ul>
        
		<form role="form" style="margin-top:20px;">
		  <div class="form-group">
			<label for="realname">真实名称</label>
			<input type="text" class="form-control" id="realname" placeholder="请输入真实名称">
		  </div>
		  <div class="form-group">
			<label for="cardnum">身份证号码</label>
			<input type="text" class="form-control" id="cardnum" placeholder="请输入身份证号码">
		  </div>
		  <div class="form-group">
			<label for="tel">电话号码</label>
			<input type="text" class="form-control" id="tel" placeholder="请输入电话号码">
		  </div>
		  <button id="nextBtn" type="button"  class="btn btn-success">下一步</button>
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
			var realname = $("#realname");
			var	cardnum  = $("#cardnum");
			var tel      = $("#tel");

			if($.trim(realname.val()) == "") {
				// alert("用户账号不能为空,请重新输入");
				// 使用layer组件提示信息
				layer.msg("用户真实名称不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
					// 如果输入的真实名称是空格,则将文本框清空
					realname.val("");
					// 获取没有输入数据的文本框
					realname.focus();
				});
				// return false，下面的代码不再执行
				return false;
			}
			if($.trim(cardnum.val()) == "") {
				// alert("用户账号不能为空,请重新输入");
				// 使用layer组件提示信息
				layer.msg("用户身份证号码不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
					// 如果输入的身份证号码是空格,则将文本框清空
					cardnum.val("");
					// 获取没有输入数据的文本框
					cardnum.focus();
				});
				// return false，下面的代码不再执行
				return false;
			}
			if($.trim(tel.val()) == "") {
				// alert("用户账号不能为空,请重新输入");
				// 使用layer组件提示信息
				layer.msg("用户电话号码不能为空,请重新输入",{time:1000, icon:5, shift:6},function () {
					// 如果输入的电话号码是空格,则将文本框清空
					tel.val("");
					// 获取没有输入数据的文本框
					tel.focus();
				});
				// return false，下面的代码不再执行
				return false;
			}

	       	$.ajax({
	       		type : "POST",
	       		url  : "${APP_PATH}/member/updateBasicinfo.do",
	       		data : {
	       			realname : realname.val(),
	       			cardnum  : cardnum.val(),
	       			tel      : tel.val()
	       		},
	       		success : function(result) {
	       			if ( result.success ) {
	       				window.location.href = "${APP_PATH}/member/uploadCert.htm"
	       			} else {
	       				layer.msg("基本信息更新失败", {time:1000, icon:5, shift:6});
	       			}
	       		}
	       	});
       });

	</script>
  </body>
</html>
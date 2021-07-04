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

    <link rel="stylesheet" href="${APP_PATH }/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH }/css/main.css">
    <link rel="stylesheet" href="${APP_PATH }/ztree/zTreeStyle.css">
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
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 许可维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <%@include file="/WEB-INF/jsp/topMessage/top.jsp"%>
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
                <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
            </div>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 许可树</h3>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH }/script/docs.min.js"></script>
<script src="${APP_PATH }/ztree/jquery.ztree.all-3.5.min.js"></script>


<script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>

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
        // 页面加载数据的方法
        loadTreeData();

        // 点击选项变红的方法
        showMenu();
    });

    // 显示图标
    var setting = {
        view : {
            // treeId:树的id, treeNode:树的节点
            addDiyDom: function(treeId, treeNode){
                // tId:代表树的节点id,这是一个固定值,不能更改
                var icoObj = $("#" + treeNode.tId + "_ico"); // tId = permissionTree_1, $("#permissionTree_1_ico")
                // 如果节点图标不为空,则将原来图标样式删除,修改为数据库中的样式。js中可以直接判断字符串是不是空值。
                if ( treeNode.icon ) {
                    icoObj.removeClass("button ico_docu ico_open").addClass(treeNode.icon).css("background","");
                }
            },
            // 设置自定义的按钮组,在节点后面悬停显示按钮增删改查按钮。
            addHoverDom: function(treeId, treeNode){
                var aObj = $("#" + treeNode.tId + "_a"); // tId = permissionTree_1, ==> $("#permissionTree_1_a")
                // 取消当前链接的事件
                aObj.attr("href", "javascript:;");
                // aObj.attr("href", "#");
                // 获取链接
                if (treeNode.editNameFlag || $("#btnGroup"+treeNode.tId).length>0) return;
                var s = '<span id="btnGroup'+treeNode.tId+'">';
                if ( treeNode.level == 0 ) {
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="window.location.href=\'${APP_PATH}/permission/permissionAdd.htm?id='+treeNode.id+'\'">&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                } else if ( treeNode.level == 1 ) {
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#" onclick="window.location.href=\'${APP_PATH}/permission/permissionUpdate.htm?id='+treeNode.id+'\'" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                    if (treeNode.children.length == 0) {
                        s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="deletePermission('+treeNode.id+',\''+treeNode.name+'\')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                    }
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="window.location.href=\'${APP_PATH}/permission/permissionAdd.htm?id='+treeNode.id+'\'">&nbsp;&nbsp;<i class="fa fa-fw fa-plus rbg "></i></a>';
                } else if ( treeNode.level == 2 ) {
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;"  href="#"  onclick="window.location.href=\'${APP_PATH}/permission/permissionUpdate.htm?id='+treeNode.id+'\'" title="修改权限信息">&nbsp;&nbsp;<i class="fa fa-fw fa-edit rbg "></i></a>';
                    s += '<a class="btn btn-info dropdown-toggle btn-xs" style="margin-left:10px;padding-top:0px;" href="#" onclick="deletePermission('+treeNode.id+',\''+treeNode.name+'\')">&nbsp;&nbsp;<i class="fa fa-fw fa-times rbg "></i></a>';
                }

                s += '</span>';
                aObj.after(s);
            },
            //
            removeHoverDom: function(treeId, treeNode){
                $("#btnGroup"+treeNode.tId).remove();
            }
        }
    };

    function loadTreeData() {
        $.ajax({

            url:"${APP_PATH}/permission/loadTreeData.do",
            type:"post",
            success:function(result){
                if(result.success){
                    var zNodes = result.data ;
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                }else{
                    layer.msg("加载数据失败", {time:1000, icon:5, shift:6});
                }
            }

        });
    }

    // 删除节点的方法
    function deletePermission(id,name) {
        layer.confirm("确定要删除["+name+"]许可吗?",  {icon: 3, title:'提示'}, function(cindex){
            layer.close(cindex);
            $.ajax({
                type:"POST",
                url:"${APP_PATH}/permission/doPermissionDelete.do",
                data: {
                    "id" : id
                },
                success:function(result){
                    if(result.success){
                        loadTreeData();
                    }else{
                        layer.msg("删除数据失败", {time:1000, icon:5, shift:6});
                    }
                }

            });
        }, function(cindex){
            layer.close(cindex);
        });


    }

    /* $(document).ready(function(){
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    }); */

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

</script>
<%--<script type="text/javascript" src="${APP_PATH }/script/menu.js"></script>--%>
</body>
</html>

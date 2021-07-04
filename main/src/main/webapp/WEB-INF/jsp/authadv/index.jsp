<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="${APP_PATH }/jquery/pagination/pagination.css">
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
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 广告审核</a></div>
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
                              <button type="button" class="btn btn-warning" onclick="queryAuthAdv()">
                                  <i class="glyphicon glyphicon-search"></i> 查询
                              </button>
                            </form>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th>广告名称</th>
                                <th>广告链接</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
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

<script src="${APP_PATH }/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH }/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH }/script/docs.min.js"></script>
<script type="text/javascript" src="${APP_PATH }/jquery/layer/layer.js"></script>
<script src="${APP_PATH}/jquery/pagination/jquery.pagination.js"></script>
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
        <c:if test="${empty param.pageno}">
            pageQuery(0);
        </c:if>
        <c:if test="${not empty param.pageno}">
            pageQuery(${param.pageno}-1);
        </c:if>
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
    // 分页查询的方法
    function pageQuery(pageIndex) {
        var loadingIndex = -1;

        var obj = {
            "pageno" : pageIndex+1,
            "pagesize" : 5
        };

        if(condition){
            obj.pagetext = $("#queryText").val(); //增加模糊查询条件
        }

        $.ajax({
            url : "${APP_PATH}/authadv/pageQuery.do",
            type : "POST",
            data : obj,
            beforeSend : function() {
                loadingIndex = layer.msg('数据查询中', {icon: 16});
                return true;
            },
            success : function( result ) {
                layer.close(loadingIndex);
                if ( result.success ) {
                    // 将查询结果循环遍历，将数据展现出来
                    var page = result.pages;
                    var authAdvList = page.data;

                    var content = '';
                    $.each(authAdvList, function(i, authAdv){
                        content += '<tr>';
                        content += '  <td>' + (i + 1) + '</td>';
                        content += '  <td>' + authAdv.name + '</td>';
                        content += '  <td>' + authAdv.url + '</td>';
                        content += '  <td>';
                        content += "		<button type='button' onclick='window.location.href=\"${APP_PATH}/authadv/show.htm?pageNumber="+page.pageNumber+"&id="+authAdv.id+"\"' class='glyphicon glyphicon-eye-open'>";
                        content += '  </td>';
                        content += '</tr>';
                    });
                    $("tbody").html(content);

                    // 创建分页
                    var num_entries = page.totalSize ;
                    $("#Pagination").pagination(num_entries, {
                        num_edge_entries: 2, //边缘页数
                        num_display_entries: 4, //主体页数
                        callback: pageQuery, //查询当前页的数据.
                        items_per_page:page.pageSize, //每页显示1项
                        current_page:(page.pageNumber-1), //当前页,索引从0开始
                        prev_text:"上一页",
                        next_text:"下一页"
                    });


                } else {
                    layer.msg(result.message, {time:1000, icon:5, shift:6});
                }
            }
        });
    }

    var condition = false ;
    //条件查询
    function queryAuthAdv(){
        var queryText = $("#queryText");

        if($.trim(queryText.val())==""){
            layer.msg("查询条件不能为空", {time:1000, icon:5, shift:6}, function(){
                queryText.focus();
            });
            return ;
        }
        condition = true ;
        pageQuery(0);
    }

</script>
<%--<script type="text/javascript" src="${APP_PATH }/script/menu.js"></script>--%>
</body>
</html>

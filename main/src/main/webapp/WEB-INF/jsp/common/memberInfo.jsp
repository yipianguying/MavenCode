<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<ul class="nav navbar-nav">
    <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i
                class="glyphicon glyphicon-user"></i> ${sessionScope.memberLogin.username }<span
                class="caret"></span></a>
        <ul class="dropdown-menu" role="menu">
            <li class="divider"></li>
            <li><a href="${APP_PATH }/loginOut.do"><i class="glyphicon glyphicon-off"></i> 退出系统</a>
            </li>
        </ul>
    </li>
</ul>>

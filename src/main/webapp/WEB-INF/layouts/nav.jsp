<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container" style="margin-left: 40px;width: auto;">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span>
			</a>

			<div class="nav-collapse">
				<ul class="nav navbar-nav">
					<shiro:hasAnyRoles name="admin,summary">
						<li class="dropdown"><a href="${ctx}/manage/index" class="dropdown-toggle" >首页 <b class="caret"></b></a>
						</li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="admin,systemUser,systemStore">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">后台设置 <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<shiro:hasAnyRoles name="admin,systemUser">
									<li><a href="${ctx}/manage/user/index">账号管理</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin,systemLogger">
									<li><a href="${ctx}/manage/logger/index">操作记录</a></li>
								</shiro:hasAnyRoles>
								<shiro:hasAnyRoles name="admin">
									<li><a href="${ctx}/manage/functions/index">权限管理</a></li>
								</shiro:hasAnyRoles>
							</ul>
						</li>
					</shiro:hasAnyRoles>
					<shiro:hasAnyRoles name="admin,functionGiftCode,functionPlacard,functionEmail,functionBroadCast,functionSeal">
						<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown">功能管理 <b class="caret"></b></a>
							<ul class="dropdown-menu">
								<shiro:hasAnyRoles name="admin">
									<li><a href="${ctx}/manage/index">景点管理</a></li>
								</shiro:hasAnyRoles>
							</ul>
						</li>
					</shiro:hasAnyRoles>
				</ul>
				<shiro:user>
					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a href="#" class="dropdown-toggle"
							data-toggle="dropdown"><i class="icon-user icon-white"></i>&nbsp;<shiro:principal
									property="name" /><b class="caret"></b></a>
							<ul class="dropdown-menu nav-list">
								<li><a href="${ctx}/profile">编辑个人资料</a></li>
								<li><a href="${ctx}/logout">安全退出</a></li>
							</ul></li>
					</ul>
				</shiro:user>
			</div>
			<!--/.nav-collapse -->

		</div>
	</div>
</div>

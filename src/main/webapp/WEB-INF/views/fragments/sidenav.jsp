<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="currentPage" value="${fn:substring(pageContext.request.servletPath, 22, -1)}" />

<div class="sidebar-collapse">
            

	<ul class="nav metismenu" id="side-menu">
		<li class="nav-header">
            <div class="dropdown profile-element"> 
            	<span>
                    <img alt="image" class="img-circle" src="${ctx}/user/profilepic" onerror="this.src='${ctx}/assets/img/profile-photos/1.png'" style="max-width: 70px;" />
                </span>
                <sec:authorize access="isAuthenticated()">
	                <a data-toggle="dropdown" class="dropdown-toggle" href="#">
	                    <span class="clear"> <span class="block m-t-xs"> <strong class="font-bold">${full_name}</strong>
	                     </span> <span class="text-muted text-xs block"> <sec:authentication property="principal.username" /> <b class="caret"></b></span> </span> 
                    </a>
	                <ul class="dropdown-menu animated fadeInRight m-t-xs">
	                    <li><a href="${ctx}/user/profile">Profile</a></li>
	                    <li><a href="${ctx}/user/changepassword">Change Password</a></li>
	                    <li class="divider"></li>
	                    <li><a href="#" onclick="logout()">Logout</a></li>
	                </ul>
                </sec:authorize>
            </div>
            <div class="logo-element">
                <%-- <img src="${ctx}/assets/img/favicon.ico" alt="GW" > --%> GW
            </div>
        </li>
	
		<c:choose>
			<c:when test="${is_admin}">
				<!----------------------------------------- ADMIN ---------------------------------------->
				<!-- <li class="list-header">Admin</li> -->
				
				<li class="${fn:contains(currentPage, '/admin/users/') ? 'active' : ''}">
					<a href="${ctx}/admin/users/index"> <i class="fa fa-user"></i> <span
						class="nav-label"> Manage Users</span> 
					</a> 
				</li>
				
				<li class="${fn:contains(currentPage, '/admin/groups/') ? 'active' : ''}">
					<a href="${ctx}/admin/groups/index"> <i class="fa fa-group"></i> <span
						class="nav-label"> Manage Groups</span> 
					</a> 
				</li>
				
				<li class="${fn:contains(currentPage, '/admin/projects/') ? 'active' : ''}">
					<a href="${ctx}/admin/projects/index"> <i class="fa fa-bank"></i> <span
						class="nav-label"> Manage Projects</span> 
					</a> 
				</li>
				
				<li class="${fn:contains(currentPage, '/admin/settings/') ? 'active' : ''}">
					<a href="${ctx}/admin/settings/index"> <i class="fa fa-gear"></i> <span
						class="nav-label"> Settings </span> 
					</a> 
				</li>
			</c:when>
			
			<c:otherwise>
				<!----------------------------------------- USER ---------------------------------------->
				<li class="${fn:contains(currentPage, '/dashboard/') ? 'active' : ''}">
					<a href="${ctx}/user/dashboard"> <i class="fa fa-th-large"></i> 
					<span class="nav-label">Dashboard</span> 
					</a> 
				</li>
				
				<!-- <li class="list-header">General</li> -->
				
				<li class="${fn:contains(currentPage, '/donate/') ? 'active' : ''}">
					<a href="${ctx}/donate/projects"> <i class="fa fa-money"></i> 
						<span class="nav-label">Donate</span> 
					</a> 
				</li>
				
				<!-- <li class="list-header">My Campaign</li> -->
				
				<li class="${fn:contains(currentPage, '/group/') ? 'active' : ''}">
					<a href="${ctx}/group/index"> <i class="fa fa-group"></i> <span
						class="nav-label">Groups</span> 
					</a> 
				</li>
				<li class="${fn:contains(currentPage, '/project/') ? 'active' : ''}">
					<a href="${ctx}/project/index"> <i class="fa fa-bank"></i> <span
						class="nav-label">Projects</span> 
					</a> 
				</li>
			</c:otherwise>
		</c:choose>
		
		<li class="list-divider"></li>
	</ul>

</div>

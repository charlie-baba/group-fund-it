<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<nav class="navbar navbar-static-top  " role="navigation" style="margin-bottom: 0">
   <div class="navbar-header">
       <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
       <form role="search" class="navbar-form-custom" action="${ctx}/search" method="get">
           <div class="form-group">
               <input type="text" placeholder="Search for something..." class="form-control" name="q" id="top-search">
           </div>
       </form>
   </div>
    <ul class="nav navbar-top-links navbar-right">
        <li>
            <span class="m-r-sm text-muted welcome-message">Group Wallet</span>
        </li>
        
        
        <li class="dropdown">
            <a href="#" data-toggle="dropdown" class="dropdown-toggle">
                <i class="fa fa-user-circle"></i>  <span>${full_name}</span>
            </a>

            <ul class="dropdown-menu dropdown-alerts" style="width: 250px">

				<sec:authorize access="isAuthenticated()">
				    <li>
				    	<div>
					        Signed in as:
					        <span class="pull-right text-muted"><sec:authentication property="principal.username" /></span>
				        </div>
				    </li>
				</sec:authorize>
                <li class="divider"></li>
                <li>
                	<a href="${ctx}/user/profile">
	                	<div>
	                    	<i class="fa fa-user icon-lg icon-fw"></i> Profile
	                    </div>
                    </a>
                </li>
                <li>
                	<a href="${ctx}/user/changepassword">
	                	<div>
	                    	<i class="fa fa-gear icon-lg icon-fw"></i> Change Password
	                    </div>
                    </a>
                </li>
				<li class="divider"></li>
				<li>
               		<a href="#" onclick="logout()">
	               		<div>
                			<i class="fa fa-sign-out"></i> Log out
	               		</div>
           			</a>
               	</li>
            </ul>
        </li>

    </ul>

</nav>
        

<form class="hide" action="${ctx}/logout" method="post" id="logout-form">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<script type="text/javascript">
    function logout() {
        $("#logout-form").submit();
    }
</script>

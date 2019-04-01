<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="Access Denied">
	
	<body class="gray-bg">

	    <div class="middle-box text-center animated fadeInDown">
		    <div>
	            <a href="${ctx}/user/dashboard"> <img src="${ctx}/assets/img/logo.png" alt="GW" style="max-width: 200px"/> </a>
	        </div>
	        <h1> <i class="fa fa-ban cstm-icon-lg"></i> </h1>
	        <h3 class="font-bold"> ACCESS DENIED! </h3>
	
	        <div class="error-desc">
	            You do not have the privilege to access the requested page. <br>
	            You can go back to main page: <br/><a href="${ctx}/user/dashboard" class="btn btn-primary m-t">Dashboard</a>
	        </div>
	    </div>
	
	</body>
	
</t:baseLayout>
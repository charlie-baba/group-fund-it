<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="404">
	<div id="container" class="cls-container">
			
		<div class="cls-header">
		    <div class="cls-brand">
		        <a class="box-inline" href="${ctx}/user/dashboard">
		            <img alt="Group Wallet" src="${ctx}/assets/img/logo.png" class="brand-icon">
		            <!-- <span class="brand-title">Group<span class="text-thin">Wallet</span></span> -->
		        </a>
		    </div>
		</div>
		
		<div class="cls-content">
		    <h1 class="error-code text-info">404</h1>
		    <p class="h4 text-uppercase text-bold">Page Not Found!</p>
		    <div class="pad-btm">
		        Sorry, but the page you are looking for has not been found on our server.
		    </div>
		    <div class="row mar-ver"> </div>
		    
		    <hr class="new-section-sm bord-no">
		    <div class="pad-top"><a class="btn btn-primary" href="${ctx}/login">Return Home</a></div>
		</div>
		
		
	</div>
</t:baseLayout>
<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="pageTitle" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<!doctype html>
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 	<meta name="description" content="Group Wallet"/>
	<meta name="author" content="Group Wallet"/>
	<meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'/>
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<meta name="google-signin-scope" content="profile email">
	<meta name="google-signin-client_id" content="397022561210-cisi4m869coecp66s4vvkq0e258l58de.apps.googleusercontent.com"></meta>
	<title> ${ empty pageTitle ? "Group Wallet" : pageTitle } | Group Wallet </title>

    <link href="${ctx}/assets/img/favicon.ico" rel="shortcut icon" type="image/x-icon" />
    <link href="${ctx}/assets/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/assets/fonts/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx}/assets/css/style.css" rel="stylesheet">
    <link href="${ctx}/assets/css/extra.css" rel="stylesheet">
    
    <!-- Mainly scripts -->
    <script src="${ctx}/assets/js/jquery-3.1.1.min.js"></script>
    <script src="${ctx}/assets/js/bootstrap.min.js"></script>
    
</head>
    
    <div>
      <jsp:doBody/>
    </div>
    
    
</html>
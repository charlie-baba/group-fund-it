<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="Forgot Password">

<body class="gray-bg">
	
	<div class="passwordBox animated fadeInDown">
        <div class="row">

            <div class="col-md-12">
                <div class="ibox-content">

                    <h2 class="font-bold">Forgot password</h2>

                    <p> Enter your email address and your password will be reset and emailed to you. </p>

                    <div class="row">

                        <div class="col-lg-12">
                        	<jsp:include page="/WEB-INF/views/alert.jsp" />
                            <form:form action="${ctx}/forgotpassword" method="post" modelAttribute="pswdResetForm" >
		            			<small> <form:errors path="*" class="cstm-form-error" /> </small>
                                <div class="form-group">
                                    <form:input type="email" class="form-control" placeholder="Email" path="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required="required" />
                                </div>

                                <button type="submit" class="btn btn-primary block full-width m-b">Send new password</button>
                            </form:form>
                            
                            <div class="p-t-20 text-center">
				                <a href="${ctx}/login" class="btn-link text-bold text-main">Back to Login</a>
				            </div>
		            
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <hr/>
        
        <div class="row">
            <div class="col-md-6">
                Group Wallet
            </div>
            <div class="col-md-6 text-right">
               <small> &copy; 2018 </small>
            </div>
        </div>
    </div>
    
</body>
    
</t:baseLayout>
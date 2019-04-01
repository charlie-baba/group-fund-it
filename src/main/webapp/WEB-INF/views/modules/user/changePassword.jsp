<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:pageLayout pageTitle="Change Password">
    
		
	<div class="" style="padding: 15px 15px !important;">
	    <div class="panel" style="height: 80vh;">
	        <div class="panel-body">
	        
	        <div class="row">
	        	<div class="col-sm-6 col-sm-offset-2">
		            <h1 class="h3">Change password</h1>
		            
		            <jsp:include page="/WEB-INF/views/alert.jsp" />		            
		            <form:form action="${ctx}/user/changepassword" method="post" modelAttribute="changePswdForm" autocomplete="off" >
		            	<small> <form:errors path="*" class="cstm-form-error" /> </small>
		            	
		                <div class="form-group">
		                    <label class="control-label">Current Password</label>
		                    <form:input type="password" class="form-control" path="currentPassword" required="required" />
		                </div>
		                <div class="form-group">
		                    <label class="control-label">New Password</label>
		                    <input type="password" class="form-control" name="newPassword" required="required" />
		                </div>
		                <div class="form-group">
		                    <label class="control-label">Confirm Password</label>
		                    <input type="password" class="form-control" name="confirmPassword" required="required" />
		                </div>
		                <div class="form-group text-right">
		                    <button class="btn btn-primary btn-lg btn-block" type="submit">Change Password</button>
		                </div>
		            </form:form>
	            </div>
	        </div>
	        
		    </div>
		</div>
	</div>
</t:pageLayout>
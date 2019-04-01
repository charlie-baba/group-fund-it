<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:pageLayout pageTitle="Profile">

	<link href="${ctx}/assets/css/datepicker3.css" rel="stylesheet">
	
	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2>Profile</h2>            
        </div>
        <div class="col-lg-2">

        </div>
    </div>

	<div class="wrapper wrapper-content">
        <div class="row animated fadeInRight">
        
            <div class="col-md-4">
            	<div class="ibox float-e-margins">
	                <div class="ibox-title">
	                    <h5>Profile Detail</h5>
	                </div>
	                <div>
	                    <div class="ibox-content border-left-right">
	                        <img src="${ctx}/user/profilepic" onerror="this.src='${ctx}/assets/img/profile-photos/1.png'" class="img-xlg" alt="Profile Picture">
	                        
	                        <div class="row">
		                        <div class="col-sm-8 p-t-10">
			                        <form:form id="profilePic-Form" action="${ctx}/user/changePicture" method="post" enctype="multipart/form-data" modelAttribute="pictureForm">
			                        	<form:input type="file" path="profilePicture" class="btn btn-block btn-primary btn-sm" accept=".jpg, .jpeg, .png, .gif" />
			                        </form:form>
	                        	</div>
                        	</div>
	                    </div>
	                    
	                    <div class="ibox-content profile-content">
	                        <h4><strong>${profile.firstName} ${profile.middleName} ${profile.lastName}</strong></h4>
	                        <p class="text-muted"><small> ${profile.email} </small></p>
	                        <br>
	                        <p><i class="fa fa-phone icon-lg icon-fw"></i> ${profile.phoneNumber}</p>
	                    	<p><i class="fa fa-neuter icon-lg icon-fw"></i> ${profile.gender}</p>
	                    	<p><i class="fa fa-birthday-cake icon-lg icon-fw"></i> ${profile.dateOfBirth} </p>
	                        <h5>
	                            About me
	                        </h5>
	                        <p>
	                            ${profile.aboutMe}
	                        </p>
	                        
	                    </div>
                    </div>
                </div>
        	</div>
        	
        	<div class="col-md-8">
	            <div class="ibox float-e-margins">
	            	<div class="ibox-title">
	                    <h5>Update Profile</h5>
	                </div>
	                <div class="ibox-content">
	                    <div>
	                    	<div class="feed-activity-list">
	                    		<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
                
								<!--Form-->
			                    <form:form action="${ctx}/user/editProfile" method="post" modelAttribute="profile">
			                    	<div class="p-l-20"> <form:errors path="*" class="cstm-form-error" /> </div>
			                    	
			                    	<div class="row">
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">First name</label>
				                                <form:input class="form-control" type="text" path="firstName" required="required" />
				                            </div>
				                        </div>
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Last name</label>
				                                <form:input class="form-control" type="text" path="lastName" required="required" />
				                            </div>
				                        </div>
				                    </div>
				                    <div class="row">
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Middle Name</label>
				                                <form:input class="form-control" type="text" path="middleName" />
				                            </div>
				                        </div>
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Phone Number</label>
				                                <form:input class="form-control" type="tel" path="phoneNumber" max="11" />
				                            </div>
				                        </div>
				                    </div>
				                    <div class="row">
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Email</label>
				                                <form:input class="form-control" type="email" path="email" disabled="true" />
				                            </div>
				                        </div>
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Gender</label>
				                                <form:select class="selectpicker form-control" path="gender" required="required" >
					                                <form:option value="" label="-- Gender -- " />
				                                    <c:forEach items="${gender}" var="gender" >
				                                        <form:option value="${gender}" label="${gender}" />
				                                    </c:forEach>
					                            </form:select>
				                            </div>
				                        </div>
				                    </div>
				                    <div class="row">
				                        <div class="col-sm-6">
				                            <div class="form-group">
				                                <label class="control-label">Date of Birth</label>
				                                <form:input class="form-control" type="text" path="dateOfBirth" />
				                            </div>
				                        </div>
				                        <div class="col-sm-6">
				                        	<div class="form-group">
				                                <label class="control-label">About me</label>
				                                <form:textarea class="form-control" type="text" rows="3" path="aboutMe" />
				                            </div>
				                        </div>
				                    </div>
				                    <button class="btn btn-primary mar-ver"> Update Profile </button>
			                    </form:form>	
	                    	</div>
	                    </div>
                    </div>
	            </div>
            </div>
            
    	</div>
    </div>    

   	   	
   	<script type="text/javascript">
    	$(document).ready(function() {
    		$('#dateOfBirth').datepicker({
    	        format: "MM dd, yyyy",
    	        todayBtn: "linked",
    	        autoclose: true,
    	        todayHighlight: true,
    	        maxDate: new Date()
    	    });
    	});
    	
    	$('#profilePicture').change(function() {
    		$('#profilePic-Form').submit();
    	}); 
    </script>
    <script src="${ctx}/assets/js/plugins/datepicker/bootstrap-datepicker.js"></script>
                
</t:pageLayout>
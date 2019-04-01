<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="isCreate" value="${action eq 'create' ? true : false}"></c:set>

	
<t:pageLayout pageTitle="${isCreate ? 'Create' : 'Edit'} Group">
	
	<link href="${ctx}/assets/css/switchery/switchery.css" rel="stylesheet">
	<link href="${ctx}/assets/css/steps/jquery.steps.css" rel="stylesheet">
    
    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> ${isCreate ? 'Create' : 'Edit'} Group </h2>
        </div>
        <div class="col-lg-2 p-t-20">
        	<a href="${ctx}/group/index" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a>
        </div>
    </div>
    
    <div class="wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-lg-12">
    		
    		<!-- Form wizard with Validation -->
            <!--===================================================-->
            <div class="ibox">
                <div class="ibox-title">
					<h5> ${isCreate ? 'Add New Group' : 'Update Group Information'} </h5>
                </div>

				<div class="ibox-content">
					<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>

	                <!--Form-->
	                <form:form id="form" class="wizard-big" action="${ctx}/group/${action}" method="post" enctype="multipart/form-data" modelAttribute="groupForm">
	                	<div class="p-l-20"> <form:errors path="*" class="cstm-form-error" /> </div>
	                    
	                    <form:input type="hidden" path="groupId" />
                        <!--Group Details-->
                        <h1> Group Details </h1>
           				<fieldset>
                        	<h2> Group Information</h2>
                        	<div class="row">	                            	
	                         	<div class="col-lg-7">
	                            	<div class="form-group">
	                                  <label class="control-label">Group Name: *</label>
	                                  <form:input type="text" class="form-control" path="name" required="required" />
	                              	</div>
	                              	<div class="form-group">
	                                  	<label class="control-label">Group Description:</label>
	                                   	<form:textarea class="form-control" path="description" rows="3" maxlength="250"/>
	                              	</div>
	                              <%-- <div class="form-group">
	                              	<label class="control-label"> Group Category: </label>
	                              	<form:select class="selectpicker form-control" path="category">
	                                <form:option value="" label="-- Category -- " />
	                                   <c:forEach items="${category}" var="category" >
	                                       <form:option value="${category}" label="${category}" />
	                                   </c:forEach>
	                            </form:select>
	                              </div> 
	                              <div class="form-group">
	                                  <label class="control-label">How long (in months) should Group Fund be available for? *</label>
	                                     <form:input type="number" class="form-control" path="fundDuration" min="0" max="24" title="Not more than 24 months" />
	                              </div>--%>
	                              	<div class="form-group">
			                    		<label class="control-label">Set Group Privacy</label>
		                        		<div class="radio">
		                        			<label for="public" class="p-r-40">
				                            	<form:radiobutton id="public" value="public" class="magic-radio" path="groupPrivacy" /> Public Group
				                            </label>
				
											<label for="private">
				                            	<form:radiobutton id="private" value="private" class="magic-radio" path="groupPrivacy" checked="checked" /> Private Group
				                            </label>
				                    	</div>
				                	</div>
	                             </div>
	                             <div class="col-lg-5">
	                       			<div class="text-center">
	                             		<p class="text-lg text-semibold mar-btm mar-top">Upload Group Logo</p>
		                        		<img alt="Logo" class="img-lg img-circle m-b-10" src="data:image/jpeg;base64,${groupForm.base64Image}" 
		                        			onerror="this.src='${ctx}/assets/img/favicon.ico'" />
		                        		<div> <form:input type="file" class="btn btn-mint cstm-inline" path="logo" accept=".jpg, .jpeg, .png, .gif" /></div>
				                        <p class="text-muted mar-top"><small><i>*maximum file size: 1Mb</i></small></p>
				                    </div>
	                             </div>
	                         </div>
	                     </fieldset>
	                        
                   		 <!--Payment Info-->
                         <h1>Payment Information </h1>
                         <fieldset>
	                         <h2> Setup the group payment information </h2>
	                         <div class="row">
                            	
                            	<div class="col-sm-7">
	                                <div class="form-group">
	                                    <label class="control-label">Target Amount for Group Fund:</label>
	                                    <form:input type="text" class="form-control" path="targetAmount" pattern="^\d" />
	                                </div>
	                                <div class="form-group">
	                                    <label class="control-label"> Select Bank: </label>
	                                	<form:select class="selectpicker form-control" path="bankCode">
			                                <form:option value="" label="-- Bank -- " />
		                                    <c:forEach items="${banks}" var="bank" >
		                                        <form:option value="${bank.code}" label="${bank.name}" />
		                                    </c:forEach>
			                            </form:select>
	                                </div>
	                                <div class="form-group">
	                                    <label class="control-label">Account Number:</label>
	                                    <form:input type="text" class="form-control" path="acctNo" pattern="^\d{10}" title="must be a 10 digit number"/>
	                                </div>
	                                <div class="list-group-item p-l-0">
	                                	<label class="control-label mar-rgt">Should group allow anonymous donations?</label>
	                                    <div class="box-inline">
	                                        <input class="js-switch" id="allowAnonymous" type="checkbox" <c:if test="${groupForm.allowAnonymous}">checked</c:if> name="allowAnonymous" />
	                                    </div>
	                                </div>
	                                <div class="form-group hide">
					                    <label class="control-label">Who should pay the donation charge?</label>
				                        <div class="radio">
				                            <form:radiobutton id="DONOR" value="DONOR" class="magic-radio" path="chargeBearer" checked="checked"/>
				                            <label for="DONOR" class="p-r-40">DONOR</label>
				
				                            <form:radiobutton id="GROUP" value="GROUP" class="magic-radio" path="chargeBearer" />
				                            <label for="GROUP">GROUP</label>
					                    </div>
					                </div>
                                </div>
                            </div>
                        </fieldset>
	
	                </form:form>
	                
                </div>
            </div>
            <!--===================================================-->
            <!-- End Form wizard with Validation -->
					         
        	</div>   
    	</div>
    </div>
    
    
    <script src="${ctx}/assets/js/plugins/steps/jquery.steps.min.js"></script>
    <script src="${ctx}/assets/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx}/assets/js/plugins/switchery/switchery.js"></script>
    <script type="text/javascript">
	    $(document).ready(function(){
	        $("#wizard").steps();
	        $("#form").steps({
	            bodyTag: "fieldset",
	            onStepChanging: function (event, currentIndex, newIndex)
	            {
	                if (currentIndex > newIndex)
	                {
	                    return true;
	                }
	
	                var form = $(this);
	
	                if (currentIndex < newIndex)
	                {
	                    // To remove error styles
	                    $(".body:eq(" + newIndex + ") label.error", form).remove();
	                    $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
	                }
	
	                form.validate().settings.ignore = ":disabled,:hidden";
	                return form.valid();
	            },
	            onFinishing: function (event, currentIndex)
	            {
	                var form = $(this);
	                form.validate().settings.ignore = ":disabled";
	                return form.valid();
	            },
	            onFinished: function (event, currentIndex)
	            {
	                var form = $(this);
	
	                // Submit form input
	                form.submit();
	            }
	        });
	        var elem = document.querySelector('.js-switch');
            var switchery = new Switchery(elem, { color: '#1AB394' });
	   	});
    </script>
</t:pageLayout>
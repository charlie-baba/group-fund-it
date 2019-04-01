<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="isCreate" value="${action eq 'create' ? true : false}"></c:set>

	
<t:pageLayout pageTitle="${isCreate ? 'Create' : 'Edit'} Project">


<link href="${ctx}/assets/css/steps/jquery.steps.css" rel="stylesheet">
<link href="${ctx}/assets/css/dropzone/dropzone.min.css" rel="stylesheet">
<link href="${ctx}/assets/css/datepicker3.css" rel="stylesheet">
	
    
    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> ${isCreate ? 'Create' : 'Edit'} Project </h2>
            <ol class="breadcrumb">
            	<li><a href="${ctx}/user/dashboard">Home</a></li>
				<li><a href="${ctx}/group/index">Groups</a></li>
                <li><a href="${ctx}/project/index/${groupId}"> <strong> Projects </strong> </a> </li>
            </ol>
        </div>
        <div class="col-lg-2 p-t-20">
        	<a href="${ctx}/project/index/${groupId}" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a>
        </div>
    </div>
    
    <div class="wrapper wrapper-content animated fadeInRight">
    	<div class="row">
    		<div class="col-lg-12">
    		
    		<!-- Form wizard with Validation -->
            <!--===================================================-->
            <div class="ibox">
                <div class="ibox-title">
					<h5> ${isCreate ? 'Add New Project' : 'Update Project Information'} </h5>
                </div>
                                
				<div class="ibox-content">
					<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
                
	                <!--Form-->
	                <form:form id="form" class="wizard-bigger" action="${ctx}/project/${action}" method="post" enctype="multipart/form-data" modelAttribute="projectForm">
	                	<div class="p-l-20"> <form:errors path="*" class="cstm-form-error" /> </div>
	                    
	                    <form:input type="hidden" path="projectId" />
	                    <form:input type="hidden" path="groupId" />
                        <!--Project Details-->
                        <h1> Project Details </h1>
   						<fieldset>
                     		<h2> Project Information</h2>
                     		<div class="row">	                            	
                         		<div class="col-lg-6"> 
                              		<div class="form-group">
                                  		<label class="control-label">Project Name/Title: *</label>
                                  		<form:input type="text" class="form-control" path="name" />
                              		</div>
                              		<div class="form-group">
                                  		<label class="control-label">Project Description:</label>
                                     	<form:textarea rows="3" class="form-control" path="description" maxlength="250" />
                              		</div>
                              		<div class="form-group">
                              			<label class="control-label"> Project Category: </label>
                              			<form:select class="selectpicker form-control" path="category">
                                			<form:option value="" label="-- Category --" />
                                   			<c:forEach items="${categories}" var="category">
		                                       <form:option value="${category.id}" label="${category.name}" />
		                                   	</c:forEach>
                            			</form:select>
                              		</div>
                              		<div class="form-group" id="campaign-range">
                              			<label class="control-label"> Duration of Campaign: </label>
                               			<div class="input-daterange input-group" id="datepicker">
                                   			<form:input type="text" class="form-control" path="startDate" />
                                   			<span class="input-group-addon">to</span>
		                                   	<form:input type="text" class="form-control" path="endDate" />
		                               	</div>
                              		</div>
                              		<div class="form-group">
		                    			<label class="control-label">Set Project Privacy</label>
	                        			<div class="radio">
	                        				<label for="public" class="p-r-40">
				                            	<input type="radio" id="public" value="public" name="projectPrivacy" ${projectForm.privateGroup ? 'disabled' : ''} required /> Public Project
				                            </label>
				
											<label for="private">
				                            	<input type="radio" id="private" value="private" name="projectPrivacy" required /> Private Project
				                            </label>
				                            
		                    			</div>
		                			</div>
                             	</div>
                             	<div class="col-lg-6">
                             		<div class="text-center">
                             			<p class="text-lg text-semibold">Drag'n'drop up to 5 project pictures</p>
	                        			<p class="text-muted"><small><i>*maximum file size: 1Mb</i></small></p>
				                        <!--Dropzonejs-->
								        <!--===================================================-->
								        <div id="pic-dropzone" class="dropzone dz-clickable">
								            <div class="dz-default dz-message">
								                <div class="dz-icon">
								                    <i class="fa fa-cloud-upload cstm-icon-xlg"></i>
								                </div>
								                <div>
								                    <span class="dz-text">Drop files to upload</span>
								                    <p class="text-sm text-muted">or click to pick manually</p>
								                </div>
								            </div>
								            <div class="fallback">
								                <form:input type="file" path="pics" accept=".jpg, .jpeg, .png, .gif" class="btn btn-mint cstm-inline" multiple="multiple" />
								            </div>
							            </div>
								        <!--===================================================-->
								        <!-- End Dropzonejs -->
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
		                                	<label class="control-label">Target Amount for Project Fund:</label>
		                                  	<form:input type="text" class="form-control" path="targetAmount" pattern="^\d" value='<fmt type="number" value="${targetAmount}"/>' />
		                              	</div>
                              			<div class="form-group">
                                  			<label class="control-label"> Select Bank: </label>
                              				<form:select class="selectpicker form-control" path="bankCode" required="required">
                                				<form:option value="" label="-- Bank -- " />
			                                   	<c:forEach items="${banks}" var="bank" >
			                                       	<form:option value="${bank.code}" label="${bank.name}" />
			                                   	</c:forEach>
                            				</form:select>
                              			</div>
                              			<div class="form-group">
                                  			<label class="control-label">Account Number:</label>
                                  			<form:input type="text" class="form-control" path="acctNo" pattern="^\d{10,14}" required="required" />
                              			</div>
                              			<div class="form-group">
                                  			<label class="control-label">Account Name:</label>
                                  			<form:input type="text" class="form-control" path="acctName" required="required" />
                           				</div>
                              			<%-- <div class="list-project-item p-l-0">
                              				<label class="control-label mar-rgt">Should project allow anonymous donations?</label>
                               				<div class="box-inline">
                                      			<input class="toggle-switch" id="allowAnonymous" type="checkbox" <c:if test="${projectForm.allowAnonymous}">checked</c:if> name="allowAnonymous" />
                                         	<label for="allowAnonymous"></label>
		                                  	</div>
		                              	</div>  --%>
                              			<div class="form-group hide">
				                    		<label class="control-label">Who should pay the donation charge?</label>
			                        		<div class="radio">
				                            <form:radiobutton id="DONOR" value="DONOR" class="magic-radio" path="chargeBearer" checked="checked" />
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
    
    
    <script src="${ctx}/assets/js/plugins/dropzone/dropzone.min.js"></script>
    <script src="${ctx}/assets/js/plugins/steps/jquery.steps.min.js"></script>
    <script src="${ctx}/assets/js/plugins/validate/jquery.validate.min.js"></script>
    
    <script type="text/javascript">
    	Dropzone.autoDiscover = false;
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
	                form.submit();
	            }
	        });

	        $('#campaign-range .input-daterange').datepicker({
    	        format: "MM dd, yyyy",
    	        todayBtn: "linked",
    	        autoclose: true,
    	        todayHighlight: true,
    	        minDate: new Date(),
	        	keyboardNavigation: false,
                forceParse: false
    	    });
    		
    	    $("#pic-dropzone").dropzone({
    	        url: "#",
		        autoProcessQueue: false,
		        uploadMultiple: true,
    	        addRemoveLinks: true,
		        acceptedFiles: 'image/*',
		        parallelUploads: 5,
		        maxFiles: 5,
		        maxFilesize: 1,
		        init: function() {
		            this.on("addedfile", function(file) { 
		            	$('.dz-progress').hide();
		            	var usedInput = this.hiddenFileInput;
		            	setTimeout(function() {
		            		$('#form').append(usedInput);
			            	usedInput.name = "pics";
		            	}, 0);
	            	});
		        }
    	    });
	   	});
	        	    	
    </script>
    
    <script src="${ctx}/assets/js/plugins/datepicker/bootstrap-datepicker.js"></script>
</t:pageLayout>
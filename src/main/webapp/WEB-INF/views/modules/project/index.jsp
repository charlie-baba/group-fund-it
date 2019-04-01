<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
	

<t:pageLayout pageTitle="Projects">

	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> Projects <c:if test="${not empty groupName}">in ${groupName}</c:if> </h2>
            <ol class="breadcrumb">
            	<li><a href="${ctx}/user/dashboard">Home</a></li>
				<li><a href="${ctx}/group/index">Groups</a></li>
                <li class="active"> <strong>Projects</strong> </li>
            </ol>
        </div>
        <c:if test="${not empty groupId}">
	        <div class="col-lg-2 p-t-20">
	        	<c:choose> 
					<c:when test="${empty projects}"> <a href="${ctx}/project/create/${groupId}" class="btn btn-primary"><i class="fa fa-plus"></i> Add Project </a> </c:when> 
					<c:otherwise> <a href="${ctx}/group/index" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a> </c:otherwise>
				</c:choose>
	        </div>
        </c:if>
    </div>
    
	
    
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
    	<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
    	
    	<c:choose>
			<c:when test="${not empty projectDetails}">
				<c:forEach items="${projectDetails}" var="projectDetail"> 
					<div class="col-lg-4">
						<div class="ibox">
						
						 <c:set var="project" value="${projectDetail.project}" />
						 <!--Project Widget-->
				         <!--===================================================-->
				         <div class="ibox-title">
                            <span class="label label-${project.privateProject ? 'warning' : 'success'} pull-right" id="prjPrivacy-${project.id}"> 
                            	${project.privateProject ? "Private" : "Public"} </span>
                            <h5 id="prjName-${project.id}">${project.name}</h5>
                         </div>
	                         
	                     <div class="ibox-content">
                            <div class="team-members">
                            	<c:choose>
                            		<c:when test="${empty projectDetail.images}">
                            			<a href="#"><img alt="logo" class="img-circle" src="${ctx}/assets/img/favicon.ico"> </a>
                            		</c:when>
                            		<c:otherwise>
                            			<c:forEach items="${projectDetail.images}" var="img">
			                                <a href="#"><img alt="logo" class="img-circle" src="data:image/jpeg;base64,${img}" onerror="src='${ctx}/assets/img/favicon.ico'"></a>
		                                </c:forEach>
                            		</c:otherwise>
                            	</c:choose>
                            </div>
                            <h4>Project description</h4>
                            <p class="group-desc"> ${project.description} </p>
                            <div>
                                <span>Status of project donation:</span>
                                <div class="stat-percent">${projectDetail.projectStatus}%</div>
                                <div class="progress progress-mini">
                                    <div style="width: ${projectDetail.projectStatus}%;" class="progress-bar"></div>
                                </div>
                            </div>
                            <div class="row  m-t-sm">
                                <div class="col-sm-4">
                                	<a href="${ctx}/project/dashboard/${project.id}" style="color: inherit;">
	                                    <div class="font-bold">Project Dashboard</div>
                                    </a>
                                </div>
                                <div class="col-sm-4">
                                	<a href="${ctx}/project/dashboard/${project.id}" style="color: inherit;"> 
	                                    <div class="font-bold">Donation History</div>
                                    </a>
                                    <%-- data-target="#trans-hist-modal" data-toggle="modal" onclick="loadTranModal(${project.id})" --%>
                                </div>
                                <div class="col-sm-4 text-right">
                                    <div class="btn-group">
			                            <button data-toggle="dropdown" class="btn btn-${project.completed ? 'default' : 'primary'} btn-sm dropdown-toggle">Action <span class="caret"></span></button>
			                            <ul class="dropdown-menu">
			                            	<li><a href="${ctx}/project/edit/${project.id}">Edit</a></li>
					                        <li><a href="#" data-target="#link-modal" data-toggle="modal" onclick="loadLinkModal(${project.id})">Share Project Link</a></li>
					                        <li class="divider"></li>
					                        <c:choose>
						         				<c:when test="${project.completed}">
						         					<li> <a> <strong> Paid Out </strong> </a> </li>
						         				</c:when>
						         				<c:otherwise>
						         					<c:choose>
										         		<c:when test="${project.active}">
									         				<li><a href="#" data-target="#deactivate-modal" data-toggle="modal" onclick="loadDeactModal(${project.id})"> Deactivate Project </a> </li>
										         		</c:when>
										         		<c:otherwise>
									            			<li><a href="#" data-target="#publish-modal" data-toggle="modal" onclick="loadPubModal(${project.id})"> Publish Project </a> </li>
										         		</c:otherwise>
										         	</c:choose>
						         					<li> <a href="#" onclick="loadPoutModal(${project.id})"><i class="fa fa-money"></i> Payout Project </a> </li>
						         				</c:otherwise>
				         					</c:choose>
			                            </ul>
			                        </div>
                                </div>
                             </div>

				             <input type="hidden" id="link-${project.id}" value="${rootURL}/na/lnk/${project.link}" />
				             <input type="hidden" id="acct-name-${project.id}" value="${project.projectBanks.iterator().next().accountName}" />
				             <input type="hidden" id="acct-bank-${project.id}" value="${project.projectBanks.iterator().next().bank.name}" />
				             <input type="hidden" id="acct-no-${project.id}" value="${project.projectBanks.iterator().next().accountNumber}" />
                         </div>    
				         <!--===================================================-->
				         
				         </div>
					</div>
				</c:forEach>
			</c:when>
			
			<c:otherwise>
				<div class="wrapper wrapper-content animated fadeInRight">
					<div class="row">
					    <div class="col-lg-12">
					        <div class="panel panel-trans text-center p-t-20 p-b-20">
						        <div class="panel-heading">
						            <h3>You have not created any project <c:if test="${not empty groupName}">in ${groupName}</c:if> yet.</h3>
						        </div>
						        <div class="panel-body">
						            <p>Projects are campaigns created within groups to aid donations to a beneficiary. </p>
						            <c:choose>
							            <c:when test="${not empty groupId}">
							            	<a href="${ctx}/project/create/${groupId}" class="btn btn-success"><i class="fa fa-plus"></i> Create Project </a>
							            </c:when>
							            <c:otherwise>
							            	Select a group to create project &nbsp; <a href="${ctx}/group/index" class="btn btn-outline btn-success"> here </a>
							            </c:otherwise>
						            </c:choose>
						        </div>
					        </div>
					    </div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
    	</div>
    </div>
    
	<!-- Notiication -->    
	<c:if test="${firstProject}">   
		<div id="floating-top-right" class="floating-container">
			<div class="alert-wrap in animated rubberBand">
				<div class="alert alert-info" role="alert">
					<button class="close" type="button" data-dismiss="alert"> <i class="pci-cross pci-circle"></i> </button>
					<div class="media">
						<div class="media-left">
							<span class="icon-wrap icon-wrap-xs icon-circle alert-icon"><i class="fa fa-info-circle cstm-icon-lg"></i></span>
						</div>
						<div class="media-body">
							<h4 class="alert-title">Well Done!</h4>
							<p class="alert-message">You now have to Publish your project before users can be able to donate.</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:if>
    
    
    <!-- Publish Modal-->
    <!--===================================================-->
    <div class="modal fade" id="publish-modal" role="dialog" tabindex="-1" aria-labelledby="publish-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Publish Project</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body text-center">
                    <p class="text-info" style="font-size: xx-large;"><i class="fa fa-wifi"></i></p>
                    <p class="text-bold text-main">Are you sure you want to publish this project?</p>
                    <p> Publishing the project will make it visible to group members and other GroupWallet users. </p>
                </div>
                
                <form:form action="${ctx}/project/publish" method="post" modelAttribute="publishForm">
                	<form:input type="hidden" id="pub-prjId" path="projectId" />
                
                <!--Modal footer-->
                <div class="modal-footer">
                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
                    <button class="btn btn-primary" type="submit">Publish</button>
                </div>
                </form:form>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Publish Modal-->
    
    <!-- Deactivate Modal-->
    <!--===================================================-->
    <div class="modal fade" id="deactivate-modal" role="dialog" tabindex="-1" aria-labelledby="deactivate-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Deactivate Project</h4>
                </div>

				<form:form action="${ctx}/project/deactivate" method="post" modelAttribute="publishForm">
                	<form:input type="hidden" id="deact-prjId" path="projectId" />
                	
	                <!--Modal body-->
	                <div class="modal-body text-center">
	                    <p class="text-danger" style="font-size: xx-large;"><i class="fa fa-eye-slash"></i></p>
	                    <p class="text-bold text-main">Are you sure you want to deactivate this project?</p>
	                    <p> <span id="deact-projName"> </span> <i>(<span id="deact-projPrivacy"></span>)</i></p>
	                </div>
	
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button class="btn btn-danger" type="submit" >Deactivate</button>
	                </div>
                </form:form>
                
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Deactivate Modal-->
    
    <!-- Payout Modal-->
    <!--===================================================-->
    <div class="modal fade" id="payout-modal" role="dialog" tabindex="-1" aria-labelledby="payout-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Project Payout</h4>
                </div>

				<form>
	                <!--Modal body-->
	                <div class="modal-body">
		                <div class="col-sm-10 col-sm-offset-1">
		                    <p class="text-bold text-main"> Confirm that you want to pay out the sum of  
		                    	&#8358; <span id="payout-amt">0</span> to the beneficiary </p>
		                    <br>
		                    <h6> Beneficiary Details: </h6>
		                    <p> Account Name: <b id="proj-acct-name"></b></p>
		                    <p> Account Bank: <b id="proj-acct-bank"></b></p>
		                    <p> Account Number: <b id="proj-acct-no"></b></p>
		                </div>
		                <div class="clearfix"></div>
	                </div>
	                <input type="hidden" id="pout-prjId" />
	
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button class="btn btn-primary" id="payout-btn">Payout</button>
	                </div>
                </form>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Payout Modal-->
    
    <%-- <!-- Transaction History Modal-->
    <!--===================================================-->
    <div class="modal fade" id="trans-hist-modal" role="dialog" tabindex="-1" aria-labelledby="trans-hist-modal" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title"> Transaction History </h4>
                </div>

                <!--Modal body-->
                <div class="modal-body">
                    <!-- Transaction Table -->
					<!--===================================================-->
					<div class="table-responsive">
		    			<table id="trans-table" class="table table-striped table-bordered table-hover" cellspacing="0" width="100%">
				            <thead>
				                <tr>
				                    <th>Performed By</th>
				                    <th>Amount (&#8358;)</th>
				                    <th>Date</th>
				                </tr>
				            </thead>
				            <tbody id="trans-body">
			            		<tr>
				                    <td>${trans.performedBy}</td>
				                    <td>${trans.amount}</td>
				                    <td>${trans.date}</td>
				                </tr>
				            </tbody>
				        </table>
			        </div>
	   				<!--===================================================-->
					<!-- End Transaction Table -->
					
	            	<div class="clearfix"></div>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Transaction History Modal--> --%>
    
    <!-- Share Link Modal-->
    <!--===================================================-->
    <div class="modal fade" id="link-modal" role="dialog" tabindex="-1" aria-labelledby="link-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Share Link</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body text-center">
                    <p class="text-info" style="font-size: xx-large;"><i class="fa fa-share-alt"></i></p>
                    <p class="text-bold text-main"> Copy the link below and share with family and friends</p>
                    <input type="text" class="form-control" id="project-link" />
                </div>

                <!--Modal footer-->
                <div class="modal-footer">
                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
                    <button class="btn btn-primary" id="copy-btn"> <i class=" fa fa-copy"></i> Copy Link </button>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Share Link Modal-->
    
    
    
    <script type="text/javascript">    
    	function loadPubModal(id) {
    		$('#pub-prjId').val(id);
    	}
    	
    	function loadDeactModal(id) {
    		$('#deact-projName').html($('#prjName-'+id).html().trim());
			$('#deact-projPrivacy').html($('#prjPrivacy-'+id).html().trim());
    		$('#deact-prjId').val(id);
    	}
    	
    	function loadLinkModal(id) {
    		$('#project-link').val($('#link-'+id).val());
    	}
    	
    	$('#payout-btn').click(function() {
    		payout();
    	});
    	
    	$('#copy-btn').click(function() {
    		var copyText = $("#project-link");
   		    copyText.select();
   		    document.execCommand("Copy");
    	});
    	
    	function loadPoutModal(id) {
    		getPayoutAmount(id);
    		$('#pout-prjId').val(id);
    		$('#proj-acct-name').html($('#acct-name-'+id).val());
    		$('#proj-acct-bank').html($('#acct-bank-'+id).val());
    		$('#proj-acct-no').html($('#acct-no-'+id).val());
    	}
    	
    	function getPayoutAmount(id) {
    		$.ajax({
				url: "${ctx}/project/getPayoutAmount",
				data: JSON.stringify({"projectId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code == '00') {
						$('#payout-amt').html(data.amount);
						$('#payout-modal').modal('show');
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
			});
    	}
    	
    	function loadTranModal(id) {
    		$.ajax({
				url: "${ctx}/project/transactions",
				data: JSON.stringify({"projectId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code == '00') {
						populateTable(data);
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
			});
    	}
    	
    	function populateTable(data) {
			var table = $('#trans-table').DataTable();
			table.clear().draw();
			
    		if (data.donations) {
    			var row = "";
    			for (i = 0; i < data.donations.length; i++) {
    				var donation = data.donations[i];
    				table.row.add([donation[0], "&#8358; "+ donation[1], new Date(donation[2]).toDateString() ]).draw().node();
    			}
    		} 
    	}
    	
    	function payout() {
    		disablePayoutBtn();
    		var id = $('#pout-prjId').val();
    		$.ajax({
				url: "${ctx}/project/payout",
				data: JSON.stringify({"projectId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		enablePayoutBtn();
	          		if(data.code == '00') {
	          			alert("You have successfully paid out the donation to the recipient");
	          			location.reload();
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
              		enablePayoutBtn();
              		alert("Something went wrong please try again.");
              	}
			});
    	}
    	
    	function disablePayoutBtn() {
    		$('#payout-btn').prop("disabled", true);
	    	$("#payout-btn").html('<i class="fa fa-spinner fa-spin pull-left"> </i> Payout');
    	}
    	
    	function enablePayoutBtn() {
    		$('#payout-btn').prop("disabled", false);
	    	$("#payout-btn").html('Payout');
    	}
    </script>
    
</t:pageLayout>
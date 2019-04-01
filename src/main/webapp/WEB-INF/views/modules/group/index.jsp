<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
	

<t:pageLayout pageTitle="Groups">

	<div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2>Groups</h2>            
        </div>
        <div class="col-lg-2 p-t-20">
			<a href="${ctx}/group/create" class="btn btn-primary"><i class="fa fa-plus"></i> Create Group</a>
        </div>
    </div>
	

	<div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
			<c:choose>
				<c:when test="${not empty groupDetails}">
					<c:forEach items="${groupDetails}" var="groupDetail">
						<div class="col-lg-4">
							<div class="ibox">
										
							 <c:set var="group" value="${groupDetail.group}" />			
					         <!--Group Widget-->
					         <!--===================================================-->
					         <div class="ibox-title">
	                            <span class="label label-${group.privateGroup ? 'warning' : 'success'} pull-right" id="grpPrivacy-${group.id}"> 
	                            	${group.privateGroup ? "Private" : "Public"} </span>
	                            <h5 id="grpName-${group.id}">${group.name}</h5>
	                         </div>
                        
                        	 <div class="ibox-content">
	                            <div class="team-members">
	                                <a href="#"><img alt="logo" class="img-circle" src="data:image/jpeg;base64,${group.base64Image}" 
						             	onerror="src='${ctx}/assets/img/favicon.ico'"></a>
	                                <!-- <a href="#"><img alt="member" class="img-circle" src="img/a2.jpg"></a> -->
	                            </div>
	                            <h4>Group description</h4>
	                            <p class="group-desc"> ${group.description} </p>
	                            <!-- <div>
	                                <span>Status of current project:</span>
	                                <div class="stat-percent">48%</div>
	                                <div class="progress progress-mini">
	                                    <div style="width: 48%;" class="progress-bar"></div>
	                                </div>
	                            </div> -->
	                            <div class="row  m-t-sm">
	                                <div class="col-sm-4">
	                                	<a href="${ctx}/group/members/${group.id}" style="color: inherit;">
		                                    <div class="font-bold">MEMBERS</div>
		                                    <fmt:formatNumber type="number" value="${groupDetail.noOfMembers}" /> 
	                                    </a>
	                                </div>
	                                <div class="col-sm-4">
	                                	<a href="${ctx}/project/index/${group.id}" style="color: inherit;">
		                                    <div class="font-bold">PROJECTS</div>
		                                    <fmt:formatNumber type="number" value="${groupDetail.noOfProjects}" /> 
	                                    </a>
	                                </div>
	                                <div class="col-sm-4 text-right">
	                                    <div class="btn-group">
				                            <button data-toggle="dropdown" class="btn btn-${group.active ? 'primary' : 'default'} btn-sm dropdown-toggle">Action <span class="caret"></span></button>
				                            <ul class="dropdown-menu">
				                            	<c:choose>
							         				<c:when test="${group.active}">
						                                 <li><a href="${ctx}/group/edit/${group.id}">Edit</a></li>
								                         <li><a href="${ctx}/group/members/${group.id}">Share Group</a></li>
								                         <li class="divider"></li>
								                         <li><a data-target="#deactivate-modal" data-toggle="modal" href="#" onclick="loadDeactModal(${group.id})">Deactivate</a></li>
						                         	</c:when>
						         					<c:otherwise>
						         						<li><a data-target="#activate-modal" data-toggle="modal" href="#" onclick="loadActModal(${group.id})"> 
							         						<i class="fa fa-check icon-lg icon-fw"></i> Activate</a></li>
						         					</c:otherwise>
							         			</c:choose>
				                            </ul>
				                        </div>
	                                </div>
	                            </div>
	
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
							            <h3>You do not have any groups yet.</h3>
							        </div>
							        <div class="panel-body">
							            <p>You can add groups by clicking on the link below. </p>
							            <a href="${ctx}/group/create" class="btn btn-success"><i class="fa fa-plus"></i> Add Group </a>
							        </div>
						        </div>
						    </div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	<!-- Deactivate Modal-->
    <!--===================================================-->
    <div class="modal fade" id="deactivate-modal" role="dialog" tabindex="-1" aria-labelledby="deactivate-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Deactivate Group</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body text-center">
                    <p class="text-danger" style="font-size: xx-large;"><i class="fa fa-eye-slash"></i></p>
                    <p class="text-bold text-main">Are you sure you want to deactivate this group?</p>
                    <p> <span id="deact-grpName"> </span> <i>(<span id="deact-grpPrivacy"></span>)</i></p>
                </div>
                <input type="hidden" id="deact-grpId" />

                <!--Modal footer-->
                <div class="modal-footer">
                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
                    <button class="btn btn-danger" onclick="deactivate()">Deactivate</button>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Deactivate Modal-->
    
    <!-- Activate Modal-->
    <!--===================================================-->
    <div class="modal fade" id="activate-modal" role="dialog" tabindex="-1" aria-labelledby="activate-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Activate Group</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body text-center">
                    <p class="text-info" style="font-size: xx-large;"><i class="fa fa-check-square-o"></i></p>
                    <p class="text-bold text-main">Are you sure you want to activate this group?</p>
                    <p> <span id="act-grpName"> </span> <i>(<span id="act-grpPrivacy"></span>)</i></p>
                </div>
                <input type="hidden" id="act-grpId" />

                <!--Modal footer-->
                <div class="modal-footer">
                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
                    <button class="btn btn-primary" onclick="activate()">Activate</button>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Activate Modal-->


	<script type="text/javascript">
		function loadDeactModal(id) {
			$('#deact-grpName').html($('#grpName-'+id).html().trim());
			$('#deact-grpPrivacy').html($('#grpPrivacy-'+id).html().trim());
			$('#deact-grpId').val(id);
		}
		
		function loadActModal(id) {
			$('#act-grpName').html($('#grpName-'+id).html().trim());
			$('#act-grpPrivacy').html($('#grpPrivacy-'+id).html().trim());
			$('#act-grpId').val(id);
		}
		
		function deactivate() {
			$.ajax({
				url: "${ctx}/group/deactivate",
				data: JSON.stringify({"groupId": $('#deact-grpId').val()}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		$('#deactivate-modal').modal('hide');
	          		if(data.code == '00') {
	          			alert("Group was deactivated Successfully!");
	          			location.reload();
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
			});
		}
		
		function activate() {
			$.ajax({
				url: "${ctx}/group/activate",
				data: JSON.stringify({"groupId": $('#act-grpId').val()}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		$('#activate-modal').modal('hide');
	          		if(data.code == '00') {
	          			alert("Group was activated Successfully!");
	          			location.reload();
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
			});
		}
	</script>

</t:pageLayout>
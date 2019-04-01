<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>


<t:pageLayout pageTitle="Manage Groups">

	<link href="${ctx}/assets/plugins/datatables/media/css/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${ctx}/assets/plugins/datatables/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet">

	<div id="page-head">
        <div id="page-title">
            <h1 class="page-header text-overflow"> Manage Groups</h1>
        </div>
    </div>

	<div id="page-content">
		<c:choose>
			<c:when test="${not empty groups}">
				<div class="panel">
					<div class="panel-body">
						<!-- Groups Table -->
						<!--===================================================-->
						<table id="groups-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
				            <thead>
				                <tr>
				                    <th>Group Name</th>
				                    <th>Creator</th>
				                    <th>Date Created</th>
				                    <th>Status</th>
				                    <th>Action </th>
				                </tr>
				            </thead>
				            <tbody>
				            	<c:forEach items="${groups}" var="group">
				            		<tr>
					                    <td>${group[1]}</td>
					                    <td>${group[2]}</td>
					                    <td> <fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${group[3]}" /> </td>
					                    <td> <span class="label label-${group[4] ? 'success' : 'warning'}">${group[4] ? "Active" : "Inactive"}</span> </td>
					                    <td>
					                    	<div class="btn-group">
					                            <div class="dropdown">
					                                <button class="btn btn-xs btn-primary dropdown-toggle" data-toggle="dropdown" type="button">
					                                    Action <i class="dropdown-caret"></i>
					                                </button>
					                                <ul class="dropdown-menu dropdown-menu-left">
					                                    <li><a href="#" onclick="getGroupInfo(${group[0]})" data-target="#groupInfo-modal" data-toggle="modal" >Group info</a></li>
					                                    <li><a href="${ctx}/admin/groups/activate/${group[0]}">${group[4] ? "Deactivate" : "Activate"}</a></li>
					                                </ul>
					                            </div>
					                        </div>
					                    </td>
					                </tr>
				            	</c:forEach>
				            </tbody>
				        </table>
						<!--===================================================-->
						<!-- End Groups Table -->
					</div>
				</div>
			</c:when>

			<c:otherwise>
				<hr class="new-section-sm bord-no">
				<div class="row">
				    <div class="col-lg-8 col-lg-offset-2">
				        <div class="panel panel-trans text-center">
					        <div class="panel-heading">
					            <h3>No one has created any group yet on GroupWallet.</h3>
					        </div>
				        </div>
				    </div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	 <!-- Group Info Modal-->
    <!--===================================================-->
    <div class="modal fade" id="groupInfo-modal" role="dialog" tabindex="-1" aria-labelledby="groupInfo-modal" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Group Info</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body">

                	<div class="col-sm-10 col-sm-offset-1 pad-top pad-btm">
			            <div class="fixed-fluid">
			                <div class="fixed-md-300 pull-sm-left fixed-right-border">
			                	<div class="pad-ver">
		                            <img id="grp-img" src="" onerror="this.src='${ctx}/assets/img/favicon.ico'" class="img-xlg img-circle" alt="Group Logo">
		                        </div>

			                </div>
			                <div class="fluid">
			                	<dl>
			                        <dt>Name</dt>
			                        <dd class="text-dark text-semibold"><span id="grp-name"></span></dd>
			                        <dt>Description</dt>
			                        <dd class="text-dark text-semibold"><span id="grp-desc"></span></dd>
			                        <dt>Created By</dt>
			                        <dd class="text-dark text-semibold"><span id="grp-crtBy"></span></dd>
			                        <dt>Create Date</dt>
			                        <dd class="text-dark text-semibold"><span id="grp-crtDt"></span></dd>
			                        <dt>Status</dt>
			                        <dd><span class="label label-info" id="grp-status"></span></dd>
			                        <dt>Privacy</dt>
			                        <dd class="text-danger text-semibold"><span id="grp-privacy"></span></dd>
			                        <dt>Allow Anonymous</dt>
			                        <dd class="text-dark text-semibold"><span id="grp-anonymous"></span></dd>
		                        </dl>
			                </div>
		                </div>
	                </div>
	                <div class="clearfix"></div>
                </div>

                <!--Modal footer-->
                <div class="modal-footer">
                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Group Info Modal-->



	<script type="text/javascript">
		$(document).ready(function() {
			$('#groups-table').DataTable({
		        "responsive": true,
		        "language": {
		            "paginate": {
		              "previous": '<i class="fa fa-angle-left"></i>',
		              "next": '<i class="fa fa-angle-right"></i>'
		            }
		        }
		    });
		});

		function getGroupInfo(id) {
	    	$.ajax({
				url: "${ctx}/admin/groups/getInfo",
				data: JSON.stringify({"groupId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code === "00") {
						populateModal(data.groupDto);
	          		} else {
	          			alert(data.description);
	          		}
	          	},
	          	error: function(xhr, ajaxOptions, thrownError) {
	          		enableBtn();
              		alert("Something went wrong please try again.");
              	}
	    	});
	    }

		function populateModal(data) {
			$('#grp-name').html(data.name);
			$('#grp-desc').html(data.description);
			$('#grp-crtBy').html(data.createdBy);
			$('#grp-crtDt').html( new Date(data.createDate).toDateString());
			$('#grp-status').html(data.active ? "Active" : "Inactive");
			$('#grp-privacy').html(data.privateGroup ? "Private Group" : "Public Group");
			$('#grp-anonymous').html(data.allowAnonymous ? "Yes" : "No");
			$('#grp-img').attr("src", ("data:image/jpeg;base64," + data.base64Image));
		}
	</script>

   	<script src="${ctx}/assets/plugins/datatables/media/js/jquery.dataTables.js"></script>
   	<script src="${ctx}/assets/plugins/datatables/media/js/dataTables.bootstrap.js"></script>
	<script src="${ctx}/assets/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
</t:pageLayout>
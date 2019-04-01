<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>


<t:pageLayout pageTitle="Manage Projects">

	<link href="${ctx}/assets/plugins/datatables/media/css/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${ctx}/assets/plugins/datatables/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet">

	<div id="page-head">
        <div id="page-title">
            <h1 class="page-header text-overflow"> Manage Projects</h1>
        </div>
    </div>

	<div id="page-content">
		<c:choose>
			<c:when test="${not empty projects}">
				<div class="panel">
					<div class="panel-body">
						<!-- Projects Table -->
						<!--===================================================-->
						<table id="projects-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
				            <thead>
				                <tr>
				                    <th>Project Name</th>
				                    <th>Creator</th>
				                    <th>Date Created</th>
				                    <th>Status</th>
				                    <th>Action </th>
				                </tr>
				            </thead>
				            <tbody>
				            	<c:forEach items="${projects}" var="project">
				            		<tr>
					                    <td>${project[1]}</td>
					                    <td>${project[2]}</td>
					                    <td> <fmt:formatDate type="both" dateStyle="medium" timeStyle="short" value="${project[3]}" /> </td>
					                    <td> <span class="label label-${project[4] ? 'success' : 'warning'}">${project[4] ? "Published" : "Unpublished"}</span> </td>
					                    <td>
					                    	<div class="btn-group">
					                            <div class="dropdown">
					                                <button class="btn btn-xs btn-primary dropdown-toggle" data-toggle="dropdown" type="button">
					                                    Action <i class="dropdown-caret"></i>
					                                </button>
					                                <ul class="dropdown-menu dropdown-menu-left">
					                                    <li><a href="#" onclick="getProjectInfo(${project[0]})" data-target="#projectInfo-modal" data-toggle="modal">Project info</a></li>
					                                    <li><a href="#" onclick="loadTranModal(${project[0]})" data-target="#trans-hist-modal" data-toggle="modal"> View Transactions </a></li>
					                                </ul>
					                            </div>
					                        </div>
					                    </td>
					                </tr>
				            	</c:forEach>
				            </tbody>
				        </table>
						<!--===================================================-->
						<!-- End Projects Table -->
					</div>
				</div>
			</c:when>

			<c:otherwise>
				<hr class="new-section-sm bord-no">
				<div class="row">
				    <div class="col-lg-8 col-lg-offset-2">
				        <div class="panel panel-trans text-center">
					        <div class="panel-heading">
					            <h3>No one has created any project yet on GroupWallet.</h3>
					        </div>
				        </div>
				    </div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	 <!-- Project Info Modal-->
    <!--===================================================-->
    <div class="modal fade" id="projectInfo-modal" role="dialog" tabindex="-1" aria-labelledby="projectInfo-modal" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Project Info</h4>
                </div>

                <!--Modal body-->
                <div class="modal-body">

                	<div class="col-sm-10 col-sm-offset-1 pad-top pad-btm">
			            <div class="fixed-fluid">
			                <div class="fixed-md-300 pull-sm-left fixed-right-border">
			                	<div class="pad-ver">
		                            <img id="proj-img" src="" onerror="this.src='${ctx}/assets/img/favicon.ico'" class="img-xlg img-circle" alt="Project Logo">
		                        </div>

			                </div>
			                <div class="fluid">
			                	<dl class="dl-horizontal">
			                        <dt>Name</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-name"></span></dd>
			                        <dt>Description</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-desc"></span></dd>
			                        <dt>Category</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-cat"></span></dd>
			                        <dt>Created By</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-crtBy"></span></dd>
			                        <dt>Create Date</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-crtDt"></span></dd>
			                        <dt>Project Duration</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-duratn"></span></dd>
			                        <dt>Target Amount</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-amt"></span></dd>
			                        <dt>Status</dt>
			                        <dd><span class="label label-info" id="proj-status"></span></dd>
			                        <dt>Privacy</dt>
			                        <dd class="text-danger text-semibold"><span id="proj-privacy"></span></dd>
			                        <dt>Paid Out</dt>
			                        <dd class="text-dark text-semibold"><span id="proj-paidOut"></span></dd>
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
    <!--End Project Info Modal-->

	<!-- Transaction History Modal-->
    <!--===================================================-->
    <div class="modal fade" id="trans-hist-modal" role="dialog" tabindex="-1" aria-labelledby="trans-hist-modal" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title"> Transaction History </h4>
                </div>

                <!--Modal body-->
                <div class="modal-body">
                    <!-- Transaction Table -->
					<!--===================================================-->
	    			<table id="trans-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
			            <thead>
			                <tr>
			                    <th>Performed By</th>
			                    <th>Amount</th>
			                    <th>Date</th>
			                </tr>
			            </thead>
			            <tbody id="trans-body">
		            		<tr>
			                    <td>${trans.performedBy}</td>
			                    <td>&#8358; ${trans.amount}</td>
			                    <td>${trans.date}</td>
			                </tr>
			            </tbody>
			        </table>
	   				<!--===================================================-->
					<!-- End Transaction Table -->

	            <div class="clearfix"></div>
                </div>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Transaction History Modal-->



	<script type="text/javascript">
		$(document).ready(function() {
			$('#projects-table').DataTable({
		        "responsive": true,
		        "language": {
		            "paginate": {
		              "previous": '<i class="fa fa-angle-left"></i>',
		              "next": '<i class="fa fa-angle-right"></i>'
		            }
		        }
		    });
		});

		function getProjectInfo(id) {
	    	$.ajax({
				url: "${ctx}/admin/projects/getInfo",
				data: JSON.stringify({"projectId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code === "00") {
						populateModal(data.projectDto);
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
			$('#proj-name').html(data.name);
			$('#proj-desc').html(data.description);
			$('#proj-cat').html(data.category);
			$('#proj-crtBy').html(data.createdBy);
			$('#proj-crtDt').html(new Date(data.createDate).toDateString());
			$('#proj-duratn').html(new Date(data.startDate).toDateString() + " - " + new Date(data.endDate).toDateString());
			$('#proj-amt').html("&#8358 " + data.targetAmount);
			$('#proj-status').html(data.active ? "Published" : "Unpublished");
			$('#proj-privacy').html(data.privateProject ? "Private Project" : "Public Project");
			$('#proj-paidOut').html(data.paidOut ? "Yes" : "No");
			$('#proj-img').attr("src", ("data:image/jpeg;base64," + data.base64Image));
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
	</script>

   	<script src="${ctx}/assets/plugins/datatables/media/js/jquery.dataTables.js"></script>
   	<script src="${ctx}/assets/plugins/datatables/media/js/dataTables.bootstrap.js"></script>
	<script src="${ctx}/assets/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
</t:pageLayout>
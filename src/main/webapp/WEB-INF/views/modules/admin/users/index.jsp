<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

	
<t:pageLayout pageTitle="Users">

	<link href="${ctx}/assets/plugins/datatables/media/css/dataTables.bootstrap.css" rel="stylesheet">
	<link href="${ctx}/assets/plugins/datatables/extensions/Responsive/css/responsive.dataTables.min.css" rel="stylesheet">
	<link href="${ctx}/assets/plugins/magic-check/css/magic-check.min.css" rel="stylesheet">

	<div id="page-head">                    
        <div id="page-title">
            <h1 class="page-header text-overflow"> Manage Users</h1>
        </div>
    </div>

	<div id="page-content">
    	<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
    	
		<c:choose>
			<c:when test="${not empty users}">
				<div class="panel">
					<div class="panel-body">
						
						<!-- Projects Table -->
						<!--===================================================-->
						<table id="users-table" class="table table-striped table-bordered" cellspacing="0" width="100%">
				            <thead>
				                <tr>
				                    <th>Full Name</th>
				                    <th>Email</th>
				                    <th>Gender</th>
				                    <th>Status</th>
				                    <th>Action </th>
				                </tr>
				            </thead>
				            <tbody>
				            	<c:forEach items="${users}" var="user"> 
				            		<tr>
					                    <td>${user[2]} ${user[3]}</td>
					                    <td>${user[1]}</td>
					                    <td>${user[4]}</td>
					                    <td> <span class="label label-${user[5] ? 'success' : 'warning'}">${user[5] ? "Active" : "Inactive"}</span> </td>
					                    <td>
					                    	<div class="btn-group">
					                            <div class="dropdown">
					                                <button class="btn btn-xs btn-primary dropdown-toggle" data-toggle="dropdown" type="button">
					                                    Action <i class="dropdown-caret"></i>
					                                </button>
					                                <ul class="dropdown-menu dropdown-menu-left">
					                                    <li><a href="#" onclick="getUserInfo(${user[0]})" data-target="#userInfo-modal" data-toggle="modal">View Details</a></li>
					                                    <li><a href="#" onclick="getGroups('${user[1]}')" data-target="#removeGroup-modal" data-toggle="modal"> Remove From Group </a></li>
					                                    <li><a href="${ctx}/admin/users/activate/${user[0]}"> ${user[5] ? "Deactivate" : "Activate"} </a></li>
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
					            <h3>There is no user yet on GroupWallet.</h3>
					        </div>
				        </div>
				    </div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<!-- User Details Modal-->
    <!--===================================================-->
    <div class="modal fade" id="userInfo-modal" role="dialog" tabindex="-1" aria-labelledby="userInfo-modal" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title"> User Details </h4>
                </div>

                <!--Modal body-->
                <div class="modal-body">
                
                	<div class="col-sm-10 col-sm-offset-1 pad-top pad-btm">
			            <div class="fixed-fluid">
			                <div class="fixed-md-300 pull-sm-left fixed-right-border">
			                	<div class="pad-ver">
		                            <img id="user-img" src="" onerror="this.src='${ctx}/assets/img/profile-photos/1.png'" class="img-xlg img-circle" alt="Project Logo">
		                        </div>
			                
			                </div>
			                <div class="fluid">
			                	<dl>
			                        <dt>Full Name</dt>
			                        <dd class="text-dark text-semibold"><span id="user-name"></span></dd>
			                        <dt>Email</dt>
			                        <dd class="text-dark text-semibold"><span id="user-email"></span></dd>
			                        <dt>Phone number</dt>
			                        <dd class="text-dark text-semibold"><span id="user-phone"></span></dd>
			                        <dt>Date of Birth</dt>
			                        <dd class="text-dark text-semibold"><span id="user-dob"></span></dd>
			                        <dt>Age</dt>
			                        <dd class="text-dark text-semibold"><span id="user-age"></span></dd>
			                        <dt>Gender</dt>
			                        <dd class="text-dark text-semibold"><span id="user-gender"></span></dd>
			                        <dt>Groups</dt>
			                        <dd class="text-dark text-semibold"><span id="user-groups"></span></dd>
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
    <!--End User Details  Modal-->
    
    <!-- Remove From Group Modal-->
    <!--===================================================-->
    <div class="modal fade" id="removeGroup-modal" role="dialog" tabindex="-1" aria-labelledby="removeGroup-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Remove From Group</h4>
                </div>

				<form:form action="${ctx}/admin/users/removeFromGroup" method="post" modelAttribute="removeFromGroupForm">
					<div class="p-l-20"> <form:errors path="*" class="cstm-form-error" /> </div>
				
	                <!--Modal body-->
	                <div class="modal-body">
	                	<div class="col-sm-10 col-sm-offset-1">
		                    <p class="text-bold text-main">Select the groups you want to remove the user from </p>
		                    <form:input type="hidden" id="rem-email" path="email" />
		                	
		                	<div id="groups-list"></div>
	                	</div>
	                	<div class="clearfix"></div>
	                </div>
	                
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button class="btn btn-danger btn-labeled fa fa-remove" type="submit">Remove from Group</button>
	                </div>
	                
                </form:form>
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Remove From Group Modal-->
    
    
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('#users-table').DataTable({
		        "responsive": true,
		        "language": {
		            "paginate": {
		              "previous": '<i class="fa fa-angle-left"></i>',
		              "next": '<i class="fa fa-angle-right"></i>'
		            }
		        }
		    });
		});
		
		function getUserInfo(id) {
	    	$.ajax({
				url: "${ctx}/admin/users/getUserDetails",
				data: JSON.stringify({"userId": id}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code === "00") {
						populateModal(data.userDto, data.groups);
	          		} else {
	          			alert(data.description);
	          		}
	          	},
	          	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
	    	});
	    }

		function getGroups(email) {
			$('#groups-list').html("");
			$.ajax({
				url: "${ctx}/admin/users/getGroups",
				data: JSON.stringify({"email": email}),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if (data.code === "00") {
	        			populateGroups(data.groups, email);
	          		} else {
	          			alert(data.description);
	          		}
	          	},
	          	error: function(xhr, ajaxOptions, thrownError) {
              		alert("Something went wrong please try again.");
              	}
	    	});
		}
		
		function populateModal(data, groups) {
			$('#user-name').html(data.firstName + " " + data.lastName);
			$('#user-email').html(data.email);
			$('#user-phone').html(data.phoneNumber);
			$('#user-dob').html(data.dateOfBirth);
			$('#user-age').html(data.age);
			$('#user-gender').html(data.gender);
			$('#user-img').attr("src", ("data:image/jpeg;base64," + data.base64Image));
			var groupNames = "";
			var size = groups.length;
			for (var i = 0; i < size; i++) {
				groupNames += groups[i].name;
				if (i != (size -1)) {
					groupNames += ", ";
				}
			}
			$('#user-groups').html(groupNames);
		}
		
		function populateGroups(groups, email) {
			$('#rem-email').val(email);
			var size = groups.length;
			var row = size == 0 ? '<i>User does not belong to any group.</i>' : '';
			for (var i = 0; i < size; i++) {
				var id = groups[i].id;
				var admin = groups[i].admin;
				row += '<div class="checkbox">' +
                    		'<input id="group-'+ id +'" class="magic-checkbox" name="groupId" value="'+ id +'" type="checkbox"'+ (admin ? 'disabled' : '') +'>' +
                    		'<label for="group-'+ id +'">'+ groups[i].name +' '+ (admin ? '(Group Admin)' : '') +'</label>'
                		'</div>';
			}
			$('#groups-list').html(row);
		}
	</script>
	
   	<script src="${ctx}/assets/plugins/datatables/media/js/jquery.dataTables.js"></script>
   	<script src="${ctx}/assets/plugins/datatables/media/js/dataTables.bootstrap.js"></script>
	<script src="${ctx}/assets/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
</t:pageLayout>
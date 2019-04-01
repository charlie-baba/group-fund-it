<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:pageLayout pageTitle="${groupName} Members">

    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2> ${groupName} Members </h2>
           	<button data-target="#add-member-modal" data-toggle="modal" class="btn btn-sm btn-primary"><i class="fa fa-plus"></i> Add New Member</button>
        </div>
        <c:if test="${not empty groupId}">
	        <div class="col-lg-2 p-t-20">
				<a href="${ctx}/group/index" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a>
	        </div>
        </c:if>
    </div>
    
    

	<div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
		<div> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
		
	    	<c:choose>
	    		<c:when test="${empty members}">
	    			<div class="text-center mar-all">
			            <h4>Your group does not have any members yet.</h4>
			        </div>
	    		</c:when>
	    		
	    		<c:otherwise>
	    		
	    			<c:forEach items="${members}" var="member">
		    			<div class="col-lg-4">
			                <div class="contact-box">			                	
			                    
			                    <div class="col-sm-4">
			                        <div class="text-center">
			                            <img alt="image" class="img-circle m-t-xs img-responsive" src="data:image/jpeg;base64,${member.picture}" 
			                            onerror="this.src='${ctx}/assets/img/profile-photos/1.png'">
			                            <div class="m-t-xs font-bold"><span class="label ${member.status == 'SIGNED_UP' ? 'label-info' : 'label-warning'}">${member.status}</span> </div>
			                        </div>
			                    </div>
			                    <div class="col-sm-7">
			                        <h3><strong> ${member.firstName} ${member.lastName}</strong></h3>
			                        <p> ${member.email} </p>
			                        
			                        <address>
			                            <strong>About</strong><br>
			                            ${empty member.aboutMe ? 'N/A' : member.aboutMe}
			                        </address>
			                    </div>
			                    <div class="text-right"> 
			                		<button data-target="#remove-member-modal" data-toggle="modal" class="btn btn-xs btn-danger" onclick="loadRemModal('${member.email}')"> 
			                		<i class="fa fa-trash"></i></button> 
		                		</div>
			                    <div class="clearfix"></div>
		                        
			                </div>
			            </div>
		            </c:forEach>
		            
	    		</c:otherwise>
	    	</c:choose>
		
		</div>
	</div>
	
	
	<!-- Add Member Modal-->
    <!--===================================================-->
    <div class="modal fade" id="add-member-modal" role="dialog" tabindex="-1" aria-labelledby="add-member-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Add New Member</h4>
                </div>

                <form:form action="${ctx}/group/addmember" method="post" modelAttribute="memberForm"> 
	                <!--Modal body-->
	                <div class="modal-body">
	                	<div class="row">
	                    	
	                		<input type="hidden" name="groupId" value="${groupId}" />
	                    	<div class="col-md-10 col-sm-offset-1">
	                    		<div class="form-group">
	                                <label class="control-label">Enter the email address of the person you want to add to this group.</label>
	                                <input type="email" class="form-control" name="email" placeholder="E-mail" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required />
	                            </div>
	                    	</div>
                    	</div>
	                </div>
	
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button type="submit" class="btn btn-primary"> <i class="fa fa-paper-plane"></i> Send Invite Email </button>
	                </div>
                </form:form>
                
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Add Member Modal-->
    
    <!-- Remove Member Modal-->
    <!--===================================================-->
    <div class="modal fade" id="remove-member-modal" role="dialog" tabindex="-1" aria-labelledby="remove-member-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="fa fa-times"></i></button>
                    <h4 class="modal-title">Remove Member</h4>
                </div>

                <form:form action="${ctx}/group/removemember" method="post" modelAttribute="memberForm"> 
	                <!--Modal body-->
	                <div class="modal-body text-center">
	                    	
                    	<div class="row">
		                    <div class="col-sm-10 col-sm-offset-1 mar-top">
			                    <input type="hidden" name="groupId" value="${groupId}" />
		                		<input type="hidden" id="mem-email" name="email" value="" />
		                		<p class="text-danger">Are you sure you want to remove <strong class="text-bold" id="mem-email-i"></strong> from ${groupName}?</p>
	                		</div>
                		</div>
	                </div>
	
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button type="submit" class="btn btn-danger"> <i class="fa fa-trash"></i> Remove </button>
	                </div>
                </form:form>
                
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Remove Member Modal-->
	
	
	<script type="text/javascript">
		$(document).ready(function() {
			$('#members-table').DataTable({
		        "responsive": true,
		        "language": {
		            "paginate": {
		              "previous": '<i class="fa fa-angle-left"></i>',
		              "next": '<i class="fa fa-angle-right"></i>'
		            }
		        }
		    });
		});
		
		function loadRemModal(email) {
			$('#mem-email').val(email);
			$('#mem-email-i').html(email);
		}
	</script>
	
    <script src="${ctx}/assets/plugins/datatables/media/js/jquery.dataTables.js"></script>
   	<script src="${ctx}/assets/plugins/datatables/media/js/dataTables.bootstrap.js"></script>
	<script src="${ctx}/assets/plugins/datatables/extensions/Responsive/js/dataTables.responsive.min.js"></script>
</t:pageLayout>
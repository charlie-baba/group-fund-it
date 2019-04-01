<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

	<link href="${ctx}/assets/plugins/magic-check/css/magic-check.min.css" rel="stylesheet">

    <div class="row wrapper border-bottom white-bg page-heading">
        <div class="col-lg-10">
            <h2>Donate to <i>${project.name}</i> </h2>
        </div>
        <div class="col-lg-2 p-t-20">
			<a href="${ctx}${returnUrl}" class="btn btn-default btn-rounded"><i class="fa fa-arrow-left"></i> Return </a>
        </div>
    </div>
    
    <div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
    
    <div class="row">
        <div class="col-lg-9">
            <div class="wrapper wrapper-content animated fadeInUp">
                <div class="ibox">
                    <div class="ibox-content">
                    
                    	<div class="row">
                            <div class="col-lg-12">
                                <div class="m-b-md">
                                    <a href="#" data-target="#amount-modal" data-toggle="modal" class="btn btn-primary btn-sm pull-right"> <i class="fa fa-credit-card-alt"></i> Donate </a>
                                    <h2>${project.name}</h2>
                                </div>
                                <dl class="dl-horizontal">
                                    <dt>Status:</dt> <dd><span class="label label-success">Active</span></dd>
                                </dl>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-lg-5">
                                <dl class="dl-horizontal">

                                    <dt>Created by:</dt> <dd>${creator[1]} ${creator[2]} </dd>
                                    <dt>Target Amount:</dt> <dd> &#8358; <fmt:formatNumber type="number" maxFractionDigits="2" value="${project.targetAmount}" /> </dd>
                                    <!-- <dt>Client:</dt> <dd><a href="#" class="text-navy"> Zender Company</a> </dd>
                                    <dt>Version:</dt> <dd> 	v1.4.2 </dd> -->
                                </dl>
                            </div>
                            <div class="col-lg-7" id="cluster_info">
                                <dl class="dl-horizontal" >

                                    <dt>Start Date:</dt> <dd> <fmt:formatDate type="date" dateStyle = "long" value = "${project.startDate}" /> </dd>
                                    <dt>End Date:</dt> <dd> <fmt:formatDate type="date" dateStyle = "long" value = "${project.endDate}" /> </dd>
                                    
                                    <c:if test="${not empty projImages}">
	                                    <dt>Images:</dt>
	                                    <dd class="project-people">
		                                    <c:forEach items="${projImages}" var="img">
				                                <a><img alt="image" class="img-circle" src="data:image/jpeg;base64,${img}" onerror="src='${ctx}/assets/img/favicon.ico'"></a>
			                                </c:forEach>
	                                    </dd>
                                    </c:if>
                                </dl>
                            </div>
                        </div>
                            
                        <div class="row">
                            <div class="col-lg-12">
                                <dl class="dl-horizontal">
                                    <dt>Completed:</dt>
                                    <dd>
                                        <div class="progress progress-striped active m-b-sm cstm-progress">
                                            <div style="width: ${percentage}%;" class="progress-bar"></div>
                                        </div>
                                        <small><strong>${percentage}%</strong> of the target amount has been donated. </small>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                            
                            
                    </div>
                </div>
            </div>
        </div>
        
        <div class="col-lg-3">
            <div class="wrapper wrapper-content project-manager">
                <h4>Project description</h4>
                <img src="${ctx}/assets/img/favicon.ico" class="img-responsive">
                <p class="small">
                    <c:choose>
	            		<c:when test="${not empty project.description}">
	            			 ${project.description}
	            		</c:when>
	            		<c:otherwise>
	                		No project description
	            		</c:otherwise>
	            	</c:choose>
                </p>
                <!-- <p class="small font-bold">
                    <span><i class="fa fa-circle text-warning"></i> High priority</span>
                </p> -->
                <div class="text-center m-t-md">
                    <a href="#" data-target="#amount-modal" data-toggle="modal" class="btn btn-primary"> <i class="fa fa-credit-card-alt"></i> Donate </a>
                </div>
            </div>
        </div>
            
    </div>
    
    
    <!-- Amount Modal-->
    <!--===================================================-->
    <div class="modal fade" id="amount-modal" role="dialog" tabindex="-1" aria-labelledby="amount-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Enter Amount</h4>
                </div>

				<form action="#" id="donation-form">
	                <!--Modal body-->
	                <div class="modal-body text-center">
	                    <p class="text-bold text-main"> Enter the amount (in Naira) you will like to donate to this project</p>
		                <input type="hidden" value="${project.id}" name="projectId" />
	                    
	                    <div class="col-sm-8 col-sm-offset-2">
	                    
	                    	<!-- #1 -->
	                    	<div id="enter-amt-div">
		                    	<div class="form-group">
			                    	<input type="number" class="form-control" name="amount" id="amount" min="50" required placeholder="Amount" />
			                    </div>
			                    
			                    <sec:authorize access="!isAuthenticated()">
			                    	<div class="checkbox pad-btm text-left ${allowAnonymous ? '' : 'hide'}">
			                    		<label for="anonymous">
					                    	<input id="anonymous" name="anonymous" class="magic-checkbox" type="checkbox" value="true"> Donate Anonymously
					                    </label>
					                </div>
				                    <div class="form-group">
			                    		<input type="email" class="form-control" name="email" id="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required placeholder="Email" />
			                    	</div>
				                </sec:authorize>
			                </div>
			                
			                <!-- #2 -->
		                    <div id="display-amt-div" style="display: none;">
		                    	<dl class="dl-horizontal text-primary">
			                    	<dt>Donation Amount: </dt>  <dd>&#8358 <b id="donation-amt"></b></dd>
			                    	<dt>Transaction Charge: </dt>  <dd>&#8358 <b id="charge-amt"></b></dd>
		                    	</dl>
		                    </div>
			                
	                    </div>
	                    <div class="clearfix"></div>
	                    <br>
	                </div>
	
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
	                    <button type="submit" class="btn btn-info" id="nxt-btn"> Next</button>
	                    <button type="submit" class="btn btn-primary" id="proceed-btn" style="display: none;"> Proceed </button>
	                </div>
                </form>
                
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End Amount Modal-->
    
    <form >
	  <script src="https://js.paystack.co/v1/inline.js"></script>
	</form>
    
    <script type="text/javascript">
	    $('#amount-modal').on('shown.bs.modal', function () {
	    	resetModal();
	        if ($("#email").length) {
	        	$("#email").prop('required', true);
	        }
	    });
	    
	    $('#anonymous').click(function() {
	    	$(this).is(':checked') ? $("#email").prop('required', false) : $("#email").prop('required', true);
	    });
	    
	    $('#donation-form').submit(function(e) {
	    	e.preventDefault();
	    	getTransactionFee();
	    });
	    
	    $('#proceed-btn').click(function() {
	    	donate();
	    });
	    
	    function getTransactionFee() {
	    	disableNxtBtn();
	    	$.ajax({
				url: "${ctx}/na/getTransactionCharge",
				data: JSON.stringify($('#donation-form').serializeObject()),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		enableNxtBtn();
	          		if(data.code === "00") {
	          			showSummary(data);
	          		} else {
	          			alert(data.description);
	          		}
	          	},
	          	error: function(xhr, ajaxOptions, thrownError) {
	          		enableNxtBtn();
              		alert("Something went wrong please try again.");
              	}
	    	});
	    }
	    
	    function donate() {
	    	disablePrcdBtn();
	    	$.ajax({
				url: "${ctx}/na/donation/initiate",
				data: JSON.stringify($('#donation-form').serializeObject()),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code === "00") {
	          			payWithPaystack(data);
	          		} else {
		          		enablePrcdBtn();
	          			alert(data.description);
	          		}
	          	},
	          	error: function(xhr, ajaxOptions, thrownError) {
	          		enablePrcdBtn();
              		alert("Something went wrong please try again.");
              	}
	    	});
	    }
	    
	    function payWithPaystack(trans){
	        var handler = PaystackPop.setup({
	          key: 'pk_test_c5972e8f8ffb87a6775a6bb2550297ae74ee5e65',
	          email: trans.email,
	          amount: trans.amount,
	          ref: trans.transRef,
	          metadata: {
	             custom_fields: [
	                {
	                    display_name: "Mobile Number",
	                    variable_name: "mobile_number",
	                    value: "+2348012345678"
	                }
	             ]
	          },
	          callback: function(response) {
	        	  window.location = "${ctx}/na/donation/verify/" + trans.transRef;
	          },
	          onClose: function(){
	              $('#amount-modal').modal("hide");
	          }
	        });
	        handler.openIframe();
	      }
	    
	    function resetModal() {
	    	$('#enter-amt-div').show();
			$('#display-amt-div').hide();
  			$("#proceed-btn").hide();
  			$("#nxt-btn").show();
	        $('#amount').val("");
	        $('#amount').focus();
	    }
	    
	    function showSummary(data) {
	    	$('#charge-amt').html(formatNum(data.amount));
  			$('#donation-amt').html(formatNum($('#amount').val()));
  			$('#enter-amt-div').hide();
  			$('#display-amt-div').show();
  			$('#nxt-btn').hide();
  			$('#proceed-btn').show();
	    }
	    
	    function disableNxtBtn() {
	    	$("#nxt-btn").prop("disabled", true);
	    	$("#nxt-btn").html('<i class="fa fa-spinner fa-spin pull-left"> </i> Next');
	    }
	    
	    function enableNxtBtn() {
	    	$("#nxt-btn").prop("disabled", false);
	    	$("#nxt-btn").html('Next');	    	
	    }
	    
	    function disablePrcdBtn() {
	    	$("#proceed-btn").prop("disabled", true);
	    	$("#proceed-btn").html('<i class="fa fa-spinner fa-spin pull-left"> </i> Proceed');
	    }
	    
	    function enablePrcdBtn() {
	    	$("#proceed-btn").prop("disabled", false);
	    	$("#proceed-btn").html('Proceed');	    	
	    }
	    
	    function formatNum(val) {
	    	return (val + "").replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
	    }
    </script>
    <script src="${ctx}/assets/js/extra.js"></script>
    <script src="${ctx}/assets/plugins/bootstrap-select/bootstrap-select.min.js"></script>

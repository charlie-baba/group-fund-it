<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

	
<t:pageLayout pageTitle="General Settings">

	<link href="${ctx}/assets/plugins/magic-check/css/magic-check.min.css" rel="stylesheet">
	
	<div id="page-head">                    
        <div id="page-title">
            <h1 class="page-header text-overflow"> General Settings </h1>
        </div>
    </div>

	<div id="page-content">
		<div class="mar-all"> <jsp:include page="/WEB-INF/views/alert.jsp" /> </div>
		
		<div class="panel">			
			<div class="panel-body">
			
				<form:form action="${ctx}/admin/settings/save" method="post" modelAttribute="settingsForm">
					<div class="p-l-20"> <form:errors path="*" class="cstm-form-error" /> </div>
				
					<p class="text-main text-bold">Define the general cost model for donations</p>
					<div class="radio pad-lft">
	                    <!-- Inline radio buttons -->
	                    <form:radiobutton id="flat-fee" class="magic-radio" path="donationChargeType" value="Flat_Fee" checked="checked" />
	                    <label for="flat-fee" class="p-r-40">Flat Fee </label>
						
	                    <form:radiobutton id="percentage" class="magic-radio" path="donationChargeType" value="Percentage" />
	                    <label for="percentage">Percentage of Donation </label>
	                </div>
	                <div class="col-sm-4" id="ff-field">
	                    <div class="form-group">
	                        <label class="control-label">Enter flat fee (&#8358) that will be charged for donations</label>
	                        <form:input class="form-control" type="number" placeholder="Enter Fee..." min="0" path="flatAmount" />
	                    </div>
	                </div>
	                <div class="col-sm-4 hide" id="pp-field">
	                    <div class="form-group">
	                        <label class="control-label">Enter percentage (%) that will be charged for donations</label>
	                        <form:input class="form-control" type="number" placeholder="Enter Percentage..." min="0" max="100" path="percentageAmount" />
	                    </div>
	                </div>
	                <div class="clearfix"></div> 
	                <hr>
	                
	                <p class="text-main text-bold">Configure OTP for Payouts</p>
	                <div class="list-group-item" style="border: 0px;">
                    	<label class="control-label mar-rgt">Every payout will send OTP to the paystack phone number?</label>
                        <div class="box-inline">
                            <input class="toggle-switch" id="otpEnabled" type="checkbox" <c:if test="${settingsForm.otpEnabled}">checked</c:if> name="otpEnabled" />
                            <label for="otpEnabled"></label>
                        </div>
                    </div>
	                <div class="clearfix"></div> 
                
                	<button type="submit" class="btn btn-success mar-lft mar-top"> <i class="fa fa-save"></i> Save </button> 
                </form:form>
			</div>
		</div>
	</div>
	
	<!-- OTP Modal-->
    <!--===================================================-->
    <div class="modal fade" id="otp-modal" role="dialog" tabindex="-1" aria-labelledby="otp-modal" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">

                <!--Modal header-->
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><i class="pci-cross pci-circle"></i></button>
                    <h4 class="modal-title">Diasble OTP</h4>
                </div>

                <form action="#" method="post" id="otpForm"> 
	                <!--Modal body-->
	                <div class="modal-body">
	                    	
                    	<div class="col-md-10">
                    		<div class="form-group">
                                <label class="control-label">Enter the OTP that was sent to your phone number.</label>
                                <input type="text" class="form-control" name="otp" required />
                            </div>
                    	</div>
	                </div>
	
	                <!--Modal footer-->
	                <div class="modal-footer">
	                    <button data-dismiss="modal" class="btn btn-default" type="button">Close</button>
	                    <button type="submit" class="btn btn-primary">Proceed</button>
	                </div>
                </form>
                
            </div>
        </div>
    </div>
    <!--===================================================-->
    <!--End OTP Modal-->
	
	<script type="text/javascript">
		$(document).ready(function() {
			if ($('#flat-fee').is(':checked')) {
				selectFlatFee();
			}
			if ($('#percentage').is(':checked')) {
				selectPercentage();
			}
		});
		
		$('#flat-fee').click(function() {
			selectFlatFee();
		});
		
		$('#percentage').click(function() {
			selectPercentage();
		});
		
		function selectFlatFee() {
			$('#ff-field').removeClass('hide');
			$('#flatAmount').prop("required", true);
			$('#pp-field').addClass('hide');
			$('#percentageAmount').prop("required", false);
		}
		
		function selectPercentage() {
			$('#ff-field').addClass('hide');
			$('#flatAmount').prop("required", false);
			$('#pp-field').removeClass('hide');
			$('#percentageAmount').prop("required", true);
		}
		
		$('#otpEnabled').click(function() {
			toggleOTP($('#otpEnabled').is(':checked'));
		});
		
		$('#otpForm').submit(function(e) {
			e.preventDefault();
			finalizeDisableOTP();
		});
		
		function toggleOTP(enabled) {
			$('#otpEnabled').prop("disabled", true);
			$.ajax({
				url: "${ctx}/admin/settings/toggleOTP",
				data: JSON.stringify({"enable": enabled }),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	    			$('#otpEnabled').prop("disabled", false);
	          		if(data.code == '00') {
	          			$('#otp-modal').modal('show');
	          		} else {
	          			alert(data.description);
	          		}
	          	},
              	error: function(xhr, ajaxOptions, thrownError) {
        			$('#otpEnabled').prop("disabled", false);
              		alert("Something went wrong please try again.");
              	}
			});
		}
		
		function finalizeDisableOTP() {
			$.ajax({
				url: "${ctx}/admin/settings/finalizeDisableOTP",
				data: JSON.stringify($('#otpForm').serializeObject()),
				headers: { "Content-Type" : "application/json", "Accept" : "application/json" },
				type: "POST",
	          	dataType: "json",
	          	success: function (data) {
	          		if(data.code == '00') {
	          			$('#otp-modal').modal('hide');
	          			alert("OTP has been disabled successfully");
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
    <script src="${ctx}/assets/js/extra.js"></script>
</t:pageLayout>

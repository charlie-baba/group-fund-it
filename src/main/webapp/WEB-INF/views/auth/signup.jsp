<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>


<t:baseLayout pageTitle="Sign Up">

<body class="gray-bg">
	<div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <img src="${ctx}/assets/img/logo.png" alt="GW" style="max-width: 200px"/>
        </div>
        <h3>Create a New Account</h3>
		            
        <jsp:include page="../alert.jsp" />
        <form:form action="${ctx}/signup" method="post" modelAttribute="signupForm">
        	<small> <form:errors path="*" class="cstm-form-error" /> </small>
        	
        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        	<div class="form-group">
                <form:input path="firstname" type="text" class="form-control" placeholder="First name *" required="required" />
                <small> <form:errors path="firstname" cssClass="cstm-form-error" /></small>
            </div>
            <div class="form-group">
                <form:input type="text" class="form-control" placeholder="Last name *" path="lastname" required="required" />
                <small> <form:errors path="lastname" cssClass="cstm-form-error" /></small>
            </div>
            <div class="form-group">
                <form:input type="text" class="form-control" placeholder="Phone: +234 XXX XXX XXXX" path="phone" 
                pattern="((\+234)|0)[- ]?[0-9]{3}[- ]?[0-9]{3}[- ]?[0-9]{4}" title="Phone number must be of the pattern +234-XXX-XXX-XXXX"/>
                <small> <form:errors path="phone" cssClass="cstm-form-error" /></small>
            </div>
            <div class="form-group">
                <form:input type="email" class="form-control" placeholder="Email *" path="email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required="required" />
                <small> <form:errors path="email" cssClass="cstm-form-error" /></small>
            </div>
            <div class="form-group">
                <form:input type="password" class="form-control" placeholder="Password *" path="password" min="6" title="At least 6 characters" required="required" />
                <small> <form:errors path="password" cssClass="cstm-form-error" /></small>
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="Confirm Password *" name="confirmPassword" min="6" title="At least 6 characters" required="required" />
            </div>
            
            <div class="form-group">
                <div class="checkbox i-checks">
                	<label> <input type="checkbox" required><i></i>I agree with the <a href="#" class="btn-link text-bold" target="_blank">Terms of Use</a> </label>
              	</div>
            </div>
            <button type="submit" class="btn btn-primary block full-width m-b">Create Account</button>
            
            <div class="media pad-top bord-top">
                <div class="pull-right">
                    <a href="#" id="fb-btn" class="btn btn-icon"><i class="fa fa-facebook-square cstm-icon-lg text-primary"></i></a>
                	<a href="#" id="gg-btn" class="btn btn-icon"><i class="fa fa-google-plus-square cstm-icon-lg text-danger"></i></a>
                </div>
                <div class="media-body text-left text-main">
                    <strong>Or Login with</strong>
                </div>
            </div>

            <p class="text-muted text-center"><small>Already have an account?</small></p>
            <a class="btn btn-sm btn-white btn-block" href="${ctx}/login">Login</a>
        </form:form>
        <p class="m-t"> <small>Group Wallet &copy; 2018</small> </p>
    </div>
	
	<form:form action="${ctx}/signup/oauth" method="post" modelAttribute="oauthForm">
    	<form:input type="hidden" path="uid" id="oauth-uid" />
    	<form:input type="hidden" path="email" id="oauth-email" />
    	<form:input type="hidden" path="firstName" id="oauth-fName" />
    	<form:input type="hidden" path="middleName" id="oauth-mName" />
    	<form:input type="hidden" path="lastName" id="oauth-lName" />
    	<form:input type="hidden" path="gender" id="oauth-gender" />
    	<form:input type="hidden" path="socialNetworkType" id="ntwkType" />
    </form:form>
            
	
	<script type="text/javascript">

		/* -------------- Facebook ------------- */
		$('#fb-btn').click(function() {
			FB.getLoginStatus(function(res) {
				if(res.status != "unknown" && res.authResponse) {
					FB.logout();
				}
				FB.login(function(res) {
					signupWithFb();
		    	}, { scope: 'email', 
		    	     return_scopes: true
		    	});
                
			}, true);
		});
		
		function signupWithFb() {
			FB.api('/me', { locale: 'en_US', fields: 'id, email, first_name, middle_name, last_name, gender' }, function(fbUser) {
				$('#oauth-uid').val(fbUser.id);
				$('#oauth-email').val(fbUser.email);
				$('#oauth-fName').val(fbUser.first_name);
				$('#oauth-mName').val(fbUser.middle_name);
				$('#oauth-lName').val(fbUser.last_name);
				$('#oauth-gender').val(fbUser.gender);
				$('#ntwkType').val("FACEBOOK");
				$('#oauthForm').submit();
			});
		}
		
		/* -------------- Google ------------- */
		var auth2 = {};
    	$('#gg-btn').click(function() {
       		auth2 = gapi.auth2.getAuthInstance();
   			auth2.signIn({
   			  	scope: 'profile email',
   			  	prompt: 'consent',
   			}, function(response) {
   			  	if (response.error) {
    			    alert('An error occured! Google could not sign you in. Please refresh and try again.');
    			    return;
    			}
   			}).then(function() {
   				signupWithGoogle();
   			});
    	});
    	
    	function signupWithGoogle() {
    		gapi.client.load('oauth2', 'v2', function() {
      			gapi.client.oauth2.userinfo.get().execute(function(resp) {
	        		$('#oauth-uid').val(resp.id);
					$('#oauth-email').val(resp.email);
					$('#oauth-fName').val(resp.given_name);
					$('#oauth-lName').val(resp.family_name);
					$('#oauth-gender').val(resp.gender);
					$('#ntwkType').val("GOOGLE");
					$('#oauthForm').submit();
      		  	});
      		});
    	}
	</script>
    <script src="${ctx}/assets/js/extra.js"></script>
	<script src="https://apis.google.com/js/client:platform.js?onload=initGoogle" async defer> </script>
    <script src="${ctx}/assets/plugins/bootstrap-select/bootstrap-select.min.js"></script>
    
</body>
</t:baseLayout>
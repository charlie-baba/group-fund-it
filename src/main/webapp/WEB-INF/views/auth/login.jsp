<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>

<t:baseLayout pageTitle="Login">

<body class="gray-bg">
	<div class="middle-box text-center loginscreen animated fadeInDown" style="padding-top: 100px;">
        <div>
            <img src="${ctx}/assets/img/logo.png" alt="GW" style="max-width: 200px"/>
        </div>
        <h3>Account Login</h3>
                
        <jsp:include page="../alert.jsp" />
        <form class="m-t" role="form" action="${ctx}/login" method="post">
        	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            <div class="form-group">
                <input type="email" class="form-control" name="username" placeholder="Email" required />
            </div>
            <div class="form-group">
                <input type="password" class="form-control" placeholder="Password" name="password" required />
            </div>
            <button type="submit" class="btn btn-primary block full-width m-b">Login</button>

			<div class="media pad-top bord-top">
                <div class="pull-right">
                    <a href="#" id="fb-btn" class="btn btn-icon"><i class="fa fa-facebook-square cstm-icon-lg text-primary"></i></a>
                    <a href="#" id="gg-btn" class="btn btn-icon"><i class="fa fa-google-plus-square cstm-icon-lg text-danger"></i></a>
                </div>
                <div class="media-body text-left text-main text-bold">
                    Or Login with
                </div>
            </div>
            
            <a href="${ctx}/forgotpassword"><small>Forgot password?</small></a>
            <p class="text-muted text-center"><small>Do not have an account?</small></p>
            <a class="btn btn-sm btn-white btn-block" href="${ctx}/signup">Create an account</a>
        </form>
        <p class="m-t"> <small>Group Wallet &copy; 2018</small> </p>
    </div>
</body>

	
	<form:form action="${ctx}/login/oauth" method="post" modelAttribute="oauthForm">
    	<form:input type="hidden" path="uid" id="oauth-uid" />
    	<form:input type="hidden" path="email" id="oauth-email" />
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
					loginWithFb();
		    	}, { scope: 'email', 
		    	     return_scopes: true
		    	});
			}, true);
		});
		
		function loginWithFb() {
			FB.api('/me', { locale: 'en_US', fields: 'id, email' }, function(fbUser) {
				$('#oauth-uid').val(fbUser.id);
				$('#oauth-email').val(fbUser.email);
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
   			}).then(function(resp) {
   				loginWithGoogle();
   			});
    	});
		
		function loginWithGoogle() {
			gapi.client.load('oauth2', 'v2', function() {
      			gapi.client.oauth2.userinfo.get().execute(function(resp) {
      				$('#oauth-uid').val(resp.id);
					$('#oauth-email').val(resp.email);
					$('#ntwkType').val("GOOGLE");
					$('#oauthForm').submit();
      			});
      		});
		}		
	</script>

    <script src="${ctx}/assets/js/extra.js"></script>
	<script src="https://apis.google.com/js/client:platform.js?onload=initGoogle" async defer> </script>
</t:baseLayout>
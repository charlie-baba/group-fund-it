
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

/*------------------- Facebook OAuth -------------------------*/
window.fbAsyncInit = function() {
  FB.init({
    appId      : '1804183579874306',
    cookie     : true,
    xfbml      : true,
    version    : 'v2.11'
  });
    
  FB.AppEvents.logPageView();   	      
};

(function(d, s, id){
   var js, fjs = d.getElementsByTagName(s)[0];
   if (d.getElementById(id)) {return;}
   js = d.createElement(s); js.id = id;
   js.src = "https://connect.facebook.net/en_US/sdk.js";
   fjs.parentNode.insertBefore(js, fjs);
 }(document, 'script', 'facebook-jssdk'));


/*------------------- Google OAuth -------------------------*/
function initGoogle() {
	gapi.load('auth2', function() {
        gapi.auth2.init({
        	client_id: '397022561210-cisi4m869coecp66s4vvkq0e258l58de.apps.googleusercontent.com',
        }).then(function(auth2) {
            console.log('init');
        });
    });
}

window.___gcfg = {
	lang: 'en-US',
  	parsetags: 'onload'
};
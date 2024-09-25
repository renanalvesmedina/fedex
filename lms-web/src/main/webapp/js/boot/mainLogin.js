

var app;

(function(angular) {
	if ($("html").is(".lt-ie10")) {
		window.location.href = contextPath+"view/browser-incompativel";
		return "";
	} else if ($("html").is(".ff") || $("html").is(".chrome")) {
		var bVersion = head.browser.version;
		if (bVersion <= 29) {
			window.location.href = contextPath+"view/browser-incompativel";
			return "";
		}
	} 

	var appName = 'app';
	
	app = angular.module(
			appName,
			[ "app.login",
              "ui.bootstrap",
              "ui.router",
              "app.common",
              "app.lmsCommon"
              ]
	);

	app.factory("ErrorInterceptorFactory", ErrorInterceptorFactory);
	app.factory("noCacheInterceptor", NoCacheInterceptor);
	
	app.config(["$sceProvider", "$httpProvider", function($sceProvider, $httpProvider) {
		// Completely disable SCE to support IE7.
		$sceProvider.enabled(false);
		$httpProvider.interceptors.push("ErrorInterceptorFactory");
		
		$httpProvider.interceptors.push('noCacheInterceptor');
		
	}]);
	if (localStorage.getItem('cookieconsentlms') === null) {
		document.body.innerHTML += '\
			<div class="cookieconsentlms"\
				 style="position:fixed;font-size:17px;\
				 font-family:Roboto Condensed,sans-serif;\
				 padding:20px;left:0;bottom:0;\
				 background-color:#CE5100;color:#FFF;\
				 text-align:center;width:100%;z-index:99999;">\
				 Esta aplica&ccedil;&atilde;o armazena cookies. Ao continuar a usar, voc&ecirc; concorda com seu uso. \
				 <u><a href="#" style="color:#FFFFFF;">Eu concordo</a></u>\
			</div>\
		';
		document.querySelector('.cookieconsentlms a').onclick = function(e) {
			e.preventDefault();
			document.querySelector('.cookieconsentlms').style.display = 'none';
			localStorage.setItem('cookieconsentlms', true);
		};
	};
angular.bootstrap( document.getElementsByTagName("html")[0], [ appName ]);
})(angular);

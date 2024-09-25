
(function(angular, RouteUtils) {

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

	var appName = "app";

	var dependencies = ["app.common",
	                    "app.lmsCommon",
	                    "app.menu",
	                    "app.directives.lms"];

	dependencies = dependencies.concat(appModuleDependencies);


	var app = angular.module(appName, dependencies);

	app.factory("ErrorInterceptorFactory", ErrorInterceptorFactory);
	app.factory("noCacheInterceptor", NoCacheInterceptor);

	app.config(["$sceProvider", "$controllerProvider", "$httpProvider", "$stateProvider", "$urlRouterProvider", "securityAuthorizationProvider",
	    function($sceProvider ,  $controllerProvider ,  $httpProvider ,  $stateProvider ,  $urlRouterProvider ,  securityAuthorizationProvider) {
		// Completely disable SCE to support IE7.
		$sceProvider.enabled(false);

		$httpProvider.interceptors.push("SecurityInterceptorFactory");
		$httpProvider.interceptors.push("ErrorInterceptorFactory");

		$httpProvider.interceptors.push('noCacheInterceptor');

		var basePath = contextPath+"js/app/";
	    var mainModule = "app";
	    var mainTemplate = contextPath+"template/app/tela.html";

	    var appRota = {
				name: mainModule,
				url: "/"+mainModule,
				'abstract': true,
				controller: AppController,
				templateUrl: basePath+"common/view/templates/app-template.html",
				resolve: {
					authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
				}
		};

		var home = {
				name: mainModule+".home",
				url: "/home",
				templateUrl: basePath+"common/view/home.html",
				resolve: {
					authenticatedUser: securityAuthorizationProvider.requireAuthenticatedUser
				}
		};

		$stateProvider
			.state(appRota)
			.state(home)
			;

	    $urlRouterProvider.when("", "/app/home");
	    $urlRouterProvider.when("/", "/app/home");
	    $urlRouterProvider.otherwise(function($injector, $location){
	    	if(typeof _ !== 'undefined'){
		    	var path = $location.path();
		    	var array = path.split('/');
		    	if(array.length > 3){
		    		var module = _.find(routeModule, function(item){
		    			return item.moduleName === array[2];
		    		});
		    		if(module){
		    			var rotas = _.reduce(module.views, function(memo, item){
		    				_.each(item.tabs, function(tab){
		    					memo.push('/app/'+module.moduleName+(item.url || '/'+item.name)+tab.url);
		    				});
		    				return memo;
		    			}, []);
		    			console.error('A Rota para a url', path, 'não foi encontrada no múdulo', module.moduleName, ', as possiveis rotas para esse múdulo são', rotas);
		    		}else{
		    			console.error('Rota e módulo não encontrados para a url', path);
		    		}
		    	}else{
		    		console.error('Rota não encontrada para a url', path);
		    	}
	    	}

	    	$location.path("/app/home");
	    });

	    for (var i = 0; i < routeModule.length; i++) {
	    	RouteUtils.addRoute($stateProvider, basePath, mainModule, mainTemplate, routeModule[i]);
	    }
	    
	}]);

	app.run(["$rootScope", "$state", "$stateParams", "SecurityFactory", "SystemInfoFactory", function ($rootScope, $state, $stateParams, SecurityFactory, SystemInfoFactory) {
		$rootScope.$state = $state;
		$rootScope.$stateParams = $stateParams;
		SecurityFactory.requestCurrentUser();
		SystemInfoFactory.atualizarInfo();
		
	}]);

})(angular, RouteUtils);



(function(loginModule, appControllerUtils, configureLoginController) {

	loginModule.controller("LoginController", [
       "$rootScope",
       "$scope",
       "$state",
       "$timeout",
       "$q",
       "SecurityFactory",
       "$location",
       "SystemInfoFactory",
       function($rootScope, $scope, $state, $timeout, $q, SecurityFactory, $location, SystemInfoFactory) {
    	   $rootScope.labelSystemName = "SISTEMA LMS";
    	   SystemInfoFactory.atualizarInfo();

    	   $scope.usuario = {};
		   $scope.loginOkta = false;

    	   appControllerUtils($rootScope, $scope, $state, $timeout, $q, null, null);

    	   configureLoginController($rootScope, $scope, SecurityFactory, function() {			   
		   
        		var initPath = contextPath + 'view/index';
			if (localStorage.getItem('cookieconsentlms') === null) {
				alert("Necess\u00e1rio clicar em Eu concordo para prosseguir.");
				} else {
        		var urlParameters = $location.search();
        		if ("redirectURL" in urlParameters) {
        			initPath += '#' + urlParameters.redirectURL;
        		}
			   console.log(urlParameters, "callback");

			   window.location.href = initPath;	
			   }		   
        	});      	
       }
   ]);
})(loginModule, appControllerUtils, configureLoginController);

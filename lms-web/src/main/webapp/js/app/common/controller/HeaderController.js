
var HeaderController = [ 
     "$scope", 
     "SecurityFactory",
     "translateService",
     function($scope, SecurityFactory, translateService) {
		  $scope.logout = SecurityFactory.logout;
		 
		  $scope.labelData =  translateService.getMensagem("data");
	      $scope.labelEmpresa = translateService.getMensagem("empresa");
	      $scope.labelFilial = translateService.getMensagem("filial");
	      $scope.labelServidor = translateService.getMensagem("servidor");
		  
     }
];

lmsCommonModule.controller("HeaderController", HeaderController);
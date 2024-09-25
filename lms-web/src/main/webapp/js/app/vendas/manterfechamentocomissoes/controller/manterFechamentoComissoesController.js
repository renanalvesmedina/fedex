var manterFechamentoComissoesController = [
	"$state",
   	"$rootScope",
	"$scope",
	"$stateParams",
 	function($state, $rootScope, $scope, $stateParams) {
		$scope.setConstructor({
			rest: "/vendas/fechamentoComissoes"
		});
		
		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
			$scope.rest.doPost("findHasFechamento", {}).then(function(data) {
				$scope.data.desabilitaBotaoAprovacao = data.desabilitaBotaoAprovacao ;
				$scope.data.desabilitaBotaoFechamento = data.desabilitaBotaoFechamento; 
				$scope.data.desabilitaEnvioRh = data.desabilitaEnvioRh;
				$scope.data.idPendeciaAprovacao = data.idPendeciaAprovacao;
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		$scope.initializeAbaPesq();

		$scope.fechamento = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("storeFechamento", {}).then(function(data) {
				$scope.data.desabilitaBotaoAprovacao = true ;
				$scope.data.desabilitaBotaoFechamento = true; 
				$scope.data.desabilitaEnvioRh = true;  
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.aprovacao = function() {
			$rootScope.showLoading = true;

			$scope.rest.doPost("storeAprovacao", {}).then(function(data) {
				
				$scope.data.desabilitaBotaoAprovacao = true ;
				$scope.data.desabilitaBotaoFechamento = true; 
				$scope.data.desabilitaEnvioRh = true;  

				$scope.aprovou=true;
							
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.envioRh = function() {
			$rootScope.showLoading = true;

			$scope.rest.doPost("storeEnvioRh", {}).then(function(data) {
				
				$scope.data.desabilitaBotaoAprovacao = true ;
				$scope.data.desabilitaBotaoFechamento = true; 
				$scope.data.desabilitaEnvioRh = true;  

				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

	}
];

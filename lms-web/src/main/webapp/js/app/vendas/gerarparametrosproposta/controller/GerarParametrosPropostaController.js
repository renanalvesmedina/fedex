var GerarParametrosPropostaController = [
	"$scope",
	"$rootScope",
	"DataTransfer",
	function($scope, $rootScope, DataTransfer) {

		$scope.setConstructor({
			rest: "/vendas/gerarParametrosProposta"
		});
		
		$scope.geracaoParametroPropostaDTO = null;
		$scope.setGeracaoParametroPropostaDTO = function(param){
			$scope.geracaoParametroPropostaDTO = param;
		};
		
		//Vai cair fora------------------------------------------
		$scope.disableAbaCad = true;
		$scope.setDisableAbaCad = function(param){
			$scope.disableAbaCad = param;
		};
		$scope.disableAbaDest = true;
		$scope.setDisableAbaDest = function(param){
			$scope.disableAbaDest = param;
		};
		$scope.disableAbaDestAereo = true;
		$scope.setDisableAbaDestAereo = function(param){
			$scope.disableAbaDestAereo = param;
		};
		//-----------------------------------------------------------
		
		
		$scope.disableAbaDestConv = false;
		$scope.setDisableAbaDestConv = function(param){
			$scope.disableAbaDestConv = param;
		};
		$scope.simulacao = DataTransfer.getAndClear();

	}
];


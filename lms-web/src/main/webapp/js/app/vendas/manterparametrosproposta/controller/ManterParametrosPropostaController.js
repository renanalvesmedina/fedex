var ManterParametrosPropostaController = [
	"$scope",
	"$rootScope",
	"DataTransfer",
	function($scope, $rootScope, DataTransfer) {

		$scope.setConstructor({
			rest: "/vendas/parametroProposta"
		});

		//TODO: regra será implementada na próxima sprint.
		$scope.disableBotoesCrud = true;
		$scope.setDisableBotoesCrud = function(param){
			$scope.disableBotoesCrud = param;
		};

		$scope.disableAbaRota = true;
		$scope.disableAbaParam = true;
		$scope.disableAbaTaxas = true;
		$scope.disableAbaGeneralidades = true;
		$scope.setDisableAbaRota = function(param){
			$scope.disableAbaRota = param;
		};
		$scope.setDisableAbaParam = function(param){
			$scope.disableAbaParam = param;
		};
		$scope.setDisableAbaTaxas = function(param){
			$scope.disableAbaTaxas = param;
		};
		$scope.setDisableAbaGeneralidades = function(param){
			$scope.disableAbaGeneralidades = param;
		};

		$scope.parametroPropostaDTO = null;
		$scope.setParametroPropostaDTO = function(param){
			$scope.parametroPropostaDTO = param;
		};

		$scope.simulacao = DataTransfer.getAndClear();
		$scope.carregaCabecalho = function(param){
			param.idSimulacao = $scope.simulacao.id;
			param.cliente = $scope.simulacao.cliente;
			param.divisaoCliente = $scope.simulacao.divisaoCliente;
			param.tabelaPreco = $scope.simulacao.tabelaPreco;
			param.servico = $scope.simulacao.servico;
			param.tpGeracaoProposta = $scope.simulacao.tpGeracaoProposta;
		};
		$scope.carregaCabecalho($scope.data);

	}
];


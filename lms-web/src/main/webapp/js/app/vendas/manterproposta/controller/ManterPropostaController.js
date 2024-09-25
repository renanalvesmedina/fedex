var ManterPropostaController = [
	"$scope",
	"ClienteService",
	function($scope, ClienteService) {

		$scope.setConstructor({
			rest: "/vendas/proposta"
		});
		
		$scope.disableAbaServ = true;
		$scope.disableAbaHist = true;
		$scope.disableAbaFlux = true;
		
		$scope.disabledCampoFatorCubagem;
		
		$scope.setDisableAbaServ = function(param){
			$scope.disableAbaServ = param;
		};
		$scope.setDisableAbaHist = function(param){
			$scope.disableAbaHist = param;
		};
		$scope.setDisableAbaFlux = function(param){
			$scope.disableAbaFlux = param;
		};

		$scope.filtrosServicoAdicional = {};
		$scope.divisoesCliente = [];
		$scope.servicosTabela = [];
		carregaServicosTabela();

		//------------------- Divisão cliente
		var changeCliente = function(cliente) {
			if (cliente && cliente.idCliente) {
				carregaDivisoesCliente(cliente.idCliente);
			} else {
				delete $scope.filter.cliente;
				limpaDivisoesCliente();
			}
		};
		$scope.$watch("filter.cliente", changeCliente);

		function carregaDivisoesCliente(idCliente) {
			ClienteService.buscaDivisoesCliente(idCliente).then(function(divisoes) {
				$scope.divisoesCliente = divisoes;
				if (divisoes.length === 1) {
					$scope.filter.divisaoCliente = divisoes[0].idDivisaoCliente;
				}
			});
		}

		function limpaDivisoesCliente() {
			$scope.divisoesCliente = [];
			delete $scope.filter.divisaoCliente;
		}
		//------------------- FIM Divisão cliente

		//------------------- Serviço tabela
		var changeTabelaPreco = function(tabelaPreco) {
			if (tabelaPreco && tabelaPreco.idTabelaPreco) {
				if (tabelaPreco.tpTipoTabelaPreco == "C") {
					$scope.addAlerts([{msg: "LMS-30054", type: MESSAGE_SEVERITY.WARNING}]);
					delete $scope.filter.tabelaPreco;
				} else {
					carregaServicosTabela();
				}
			} else {
				delete $scope.filter.tabelaPreco;
			}
		};
		$scope.$watch("filter.tabelaPreco", changeTabelaPreco);

		function carregaServicosTabela() {
			$scope.rest.doPost("findServicosCombo").then(function(servicos) {
				$scope.servicosTabela = servicos;
			});
		}
		
		//------------------- FIM Serviço tabela
		
	}
];


var ManterPropostaCadController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"$state",
	"translateService",
	"ClienteService",
	"$location",
	"DataTransfer",
	function($scope, $rootScope, $stateParams, $state, translateService, ClienteService,$location, DataTransfer) {

		function initializeAbaCad(params){
			if (params.id) {
				$rootScope.showLoading = true;
				$scope.findById(params.id).then(function(data) {
					$scope.setData(data);
					$scope.data.saveMode = "parcial";

					controleAbas(params.id);
					$scope.changeTpGeracaoProposta();
					$scope.changeBtnGerarParametros();
					
					if ($scope.data.tpGeracaoProposta.value == 'O'){
						$scope.disabledBtnGerarParametros = true;
					}
					
					$rootScope.showLoading = false;
				},function() {
					$scope.limpar();
					$rootScope.showLoading = false;
				});
			} else {
				$scope.setData({});
				$scope.data.saveMode = "parcial";
				$scope.disabledBtnGerarParametros = true;
			}
			$scope.data.campos = {};
		}

		initializeAbaCad($stateParams);

		$scope.store = function() {
			$rootScope.showLoading = true;

			$scope.rest.doPost("", $scope.data).then(function(response) {
				$rootScope.showLoading = false;
				$state.transitionTo($state.current, {id: response[$scope.getIdProperty()]}, {
					reload: false,
					inherit: false,
					notify: true
				});
			initializeAbaCad($stateParams);
				$scope.showSuccessMessage();
			},function() {
				$rootScope.showLoading = false;
			});
		};

		/* ------------------------------------------------ */

		$scope.divisoesCliente = [];
		$scope.servicosTabela = [];
		$scope.servicoPadrao = [];
		$scope.produtosEspecificos = [];
		$scope.tiposGeracao = [];
		carregaServicoPadrao();
		carregaComboProdutosEspecificos();



		//------------------- limpar
		$scope.limpar = function() {
			$scope.clearData();
			$scope.data.tpGeracaoProposta = {value:'O'};
			controleAbas();
			$scope.disabledBtnGerarParametros = true;
		};
		//-------------------FIM limpar

		//------------------- Servico Padrao
		function carregaServicoPadrao() {
			$scope.rest.doPost("findServicoPadrao").then(function(servicoPadrao) {
				$scope.servicoPadrao = servicoPadrao;
			});
		}
		//------------------- FIM Servico Padrao

		function carregaComboProdutosEspecificos(){
			$scope.rest.doPost("findComboProdutosEspecificos").then(function(produtosEspecificos) {
				$scope.produtosEspecificos = produtosEspecificos;
			});
		}


		//------------------- Divisão cliente
		var changeCliente = function(cliente) {
			if (cliente && cliente.idCliente) {
				carregaDivisoesCliente(cliente.idCliente);
			} else {
				delete $scope.data.cliente;
				limpaDivisoesCliente();
			}
		};
		$scope.$watch("data.cliente", changeCliente);

		function carregaDivisoesCliente(idCliente) {
			ClienteService.buscaDivisoesCliente(idCliente).then(function(divisoes) {
				$scope.divisoesCliente = divisoes;
				if (divisoes.length === 1) {
					$scope.data.divisaoCliente = divisoes[0];
				}
			});
		}

		function limpaDivisoesCliente() {
			$scope.divisoesCliente = [];
			delete $scope.data.divisaoCliente;
		}
		//------------------- FIM Divisão cliente

		//------------------- Serviço tabela
		var changeTabelaPreco = function(tabelaPreco) {
			if($scope.data.campos){
				$scope.data.campos.tpGeracaoProposta = false;
			}
			if (tabelaPreco && tabelaPreco.idTabelaPreco) {
				carregaServicosTabela(tabelaPreco);

				if (tabelaPreco.tpSubtipoTabelaPreco == "P") {
					$scope.data.tpGeracaoProposta = {value:'T'};
					$scope.data.campos.tpGeracaoProposta = true;
				} else {
					$scope.data.campos.tpGeracaoProposta = false;
				}
			} else {
				$scope.servicosTabela = [];
				$scope.setDisableAbaServ(true);
			}
		};
		$scope.$watch("data.tabelaPreco", changeTabelaPreco);

		function carregaServicosTabela(tabelaPreco) {
			if(tabelaPreco){
				var situacao = "A";
				var tpModal = tabelaPreco.tpModal ? tabelaPreco.tpModal.value : null;
				var tpAbrangencia = tabelaPreco.tpAbrangencia ? tabelaPreco.tpAbrangencia.value : null;

				$scope.rest.doPost("findServicosCombo", {tpModal: tpModal, tpAbrangencia: tpAbrangencia, tpSituacao: situacao}).then(function(servicos) {
					$scope.servicosTabela = servicos;
					$scope.data.campos.servico = false;
					if (servicos.length === 1) {
						$scope.data.servico = servicos[0];
					}

					if ($scope.data.tabelaPreco && $scope.data.tabelaPreco.idTabelaPreco
							&& $scope.data.tabelaPrecoFob && $scope.data.tabelaPrecoFob.idTabelaPreco) {
						var blPossuiServicoPadrao = false;
						angular.forEach(servicos, function(value) {
							if (value.idServico == $scope.servicoPadrao.idServico) {
		   						blPossuiServicoPadrao = true;
		   						return;
		   					}
						});
						if (!blPossuiServicoPadrao) {
		   					$scope.addAlerts([{msg: translateService.getMensagem("LMS-30057", [$scope.data.tabelaPreco.nomeTabela, $scope.data.tabelaPrecoFob.nomeTabela]), type: MESSAGE_SEVERITY.WARNING}]);
							delete $scope.data.tabelaPreco;
		   				} else {
		   					$scope.data.servico.idServico = $scope.servicoPadrao.idServico;
		   					if(!$scope.data.campos || !$scope.data.disableAll || $scope.data.disableAll === false){
		   						$scope.data.campos.servico = false;
		   					}
		   				}
					}

					$scope.changeTpGeracaoProposta();
				});
			}
		}

		function changeServicoTabela(idServico) {
			if ($scope.servicoPadrao && $scope.servicoPadrao.idServico != idServico) {
				delete $scope.data.tabelaPrecoFob;
				$scope.data.campos.tabelaPrecoFob = true;
			} else {
				if($scope.data.campos){
					$scope.data.campos.tabelaPrecoFob = false;
				}
			}
		}
		$scope.$watch("data.servico.idServico", changeServicoTabela);

		//------------------- FIM Serviço tabela

		//------------------- tabela preco fob
		var changeTabelaPrecoFob = function(tabelaPrecoFob) {
            var situacao = "A";
			if (tabelaPrecoFob && $scope.data.tabelaPreco && $scope.data.tabelaPreco.idTabelaPreco) {
				var tpModal = $scope.data.tabelaPreco.tpModal ? $scope.data.tabelaPreco.tpModal.value : null;
				var tpAbrangencia = $scope.data.tabelaPreco.tpAbrangencia ? $scope.data.tabelaPreco.tpAbrangencia.value : null;
				$scope.rest.doPost("findServicosCombo", {tpModal: tpModal, tpAbrangencia: tpAbrangencia, tpSituacao:situacao}).then(function(servicos) {
					$scope.servicosTabela = servicos;
					if (servicos.length === 1) {
						$scope.data.servico = servicos[0];
					}

					var blPossuiServicoPadrao = false;
					angular.forEach(servicos, function(value) {
						if (value.idServico == $scope.servicoPadrao.idServico) {
	   						blPossuiServicoPadrao = true;
	   						return;
	   					}
					});
					if (blPossuiServicoPadrao) {
						$scope.data.servico = $scope.servicoPadrao;
	   					if($scope.data.campos){
	   						$scope.data.campos.servico = true;
	   					}
	   				} else {
	   					$scope.addAlerts([{msg: translateService.getMensagem("LMS-30057", [tabelaPrecoFob.nomeTabela, $scope.data.tabelaPreco.nomeTabela]), type: MESSAGE_SEVERITY.WARNING}]);
						delete $scope.data.tabelaPrecoFob;
	   				}

					$scope.changeTpGeracaoProposta();
				});
			} else if (tabelaPrecoFob){
				$scope.rest.doPost("findServicosCombo", {tpSituacao:situacao}).then(function(servicos) {
					$scope.servicosTabela = servicos;
				});
				$scope.data.servico = $scope.servicoPadrao;
				if($scope.data.campos){
					$scope.data.campos.servico = true;
				} else {
					$scope.data.campos = {servico:true};
				}
				$scope.changeTpGeracaoProposta();
			}
		};
		$scope.$watch("data.tabelaPrecoFob", changeTabelaPrecoFob);
		//------------------- FIM tabela preco fob


		//---------
		$scope.changeTpGeracaoProposta = function(){
			if(angular.isDefined($scope.data.nrSimulacao)){
				$scope.data.campos.servico = true;
				$scope.data.campos.tpGeracaoProposta = true;
				$scope.data.campos.nrFatorDensidade = true;
				if(angular.isDefined($scope.data.tpSituacaoAprovacao) && $scope.data.tpSituacaoAprovacao.value !== 'I'){
					$scope.data.campos.nrFatorCubagem = true;
				}
			}
		};
		//---------

		//---------
		$scope.changeBtnGerarParametros = function(){
			if(angular.isDefined($scope.data.nrSimulacao) &&
					angular.isDefined($scope.data.tpGeracaoProposta) &&
					($scope.data.tpGeracaoProposta.value == "V" ||
					 $scope.data.tpGeracaoProposta.value == "P" ||
					 $scope.data.tpGeracaoProposta.value == "M" ||
					 $scope.data.tpGeracaoProposta.value == "I" ||
					 $scope.data.tpGeracaoProposta.value == "O")){
				$scope.disabledBtnGerarParametros = false;
			}else{
				$scope.disabledBtnGerarParametros = true;
			}
		};
		//---------

		function controleAbas(idProposta) {
			if (idProposta) {
				//------- ABA Servicos Adicionais
				if ($scope.data.tabelaPreco && $scope.data.tabelaPreco.tpSubtipoTabelaPreco != "P") {
					$scope.setDisableAbaServ(false);
				} else {
					$scope.setDisableAbaServ(true);
				}

				//------- ABA Historico
				var tpSituacaoAprovacao = $scope.data.tpSituacaoAprovacao ? $scope.data.tpSituacaoAprovacao.value : null;
				if (tpSituacaoAprovacao && (tpSituacaoAprovacao == "M" || tpSituacaoAprovacao == "F" || tpSituacaoAprovacao == "H")) {
					$scope.setDisableAbaHist(false);
				} else {
					$scope.setDisableAbaHist(true);
				}

				//------- ABA Fluxo Aprovacao
				if (tpSituacaoAprovacao && (tpSituacaoAprovacao == "A" || tpSituacaoAprovacao == "C" || tpSituacaoAprovacao == "E" || tpSituacaoAprovacao == "R" || tpSituacaoAprovacao == "M" || tpSituacaoAprovacao == "H" || tpSituacaoAprovacao == "F")) {
					$scope.setDisableAbaFlux(false);
				} else {
					$scope.setDisableAbaFlux(true);
				}

			} else {
				$scope.setDisableAbaServ(true);
				$scope.setDisableAbaHist(true);
				$scope.setDisableAbaFlux(true);
			}
		}


		$scope.openManterParametros = function(){
			DataTransfer.set($scope.data);
			$location.path("/app/vendas/manterParametrosProposta/");
		};

		$scope.setDsProdutoEspecifico = function(){
			if($scope.data.produtoEspecifico != null && $scope.data.produtoEspecifico != undefined && $scope.produtosEspecificos != null & $scope.produtosEspecificos != undefined){
				for( var i = 0; i < $scope.produtosEspecificos.length; i++){
					if($scope.produtosEspecificos[i].idProdutoEspecifico == $scope.data.produtoEspecifico.idProdutoEspecifico){
						$scope.data.produtoEspecifico.dsProdutoEspecifico = "TE" + $scope.produtosEspecificos[i].nrTarifaEspecifica;
						break;
					}
				}
			}
		};

		$scope.openGerarParametros = function(){
			$scope.setDsProdutoEspecifico();
			DataTransfer.set($scope.data);
			$location.path("/app/vendas/gerarParametrosProposta/destConv/"+$scope.data.id);
		};
	}
];


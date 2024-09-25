var ManterPropostaFrmController = [
	"$scope",
	"$stateParams",
	"modalService",
	"translateService",
	"$rootScope",
	function($scope, $stateParams, modalService, translateService, $rootScope) {

		function initializeAbaFrm(params){
			$scope.data.saveMode = "completo";
			if (params.id) {
				var tpSituacaoAprovacao = $scope.data.tpSituacaoAprovacao ? $scope.data.tpSituacaoAprovacao.value : "";
				var disableAll = !!$scope.data.disableAll;
				var isFilialMatriz = !!$scope.data.isFilialMatriz;

				if (!!$scope.data.blEfetivada || (($scope.data.idFilialSessao != $scope.data.idFilial) && $scope.data.isFilialMatriz == "N")) {
					disableAll = true;
				} else {
					disableAll = false;
				}

				controleCampos(disableAll);
				controleAbasBotoes(tpSituacaoAprovacao, disableAll);
			}
		}

		initializeAbaFrm($stateParams);

		/* ----------- overriding store method -------------*/
		var inheritedStore = $scope.store;
		$scope.store = function() {
			inheritedStore();
			initializeAbaFrm($stateParams);
		};
		/* ------------------------------------------------ */

		function controleCampos(disableAll) {
			$scope.data.campos.dataValidadeProposta = disableAll;
			$scope.data.campos.dataInicioVigencia = disableAll;
			$scope.data.campos.dataAceitacaoCliente = disableAll;
			$scope.data.campos.observacao = disableAll;
		}

		function controleAbasBotoes(tpSituacaoAprovacao, disableAll) {

			if (tpSituacaoAprovacao == "A") {
				$scope.setDisableAbaFlux(false);

				$scope.data.botoes.btImprimirProposta = false;
				$scope.data.botoes.btSolicitarEfetivacao = false;
				$scope.data.botoes.btAprovacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btEfetivarProposta = true;

				if (disableAll) {
					$scope.data.botoes.btSalvar = true;
				}
			} else if (tpSituacaoAprovacao == "C") {
				$scope.setDisableAbaFlux(false);

				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btImprimirProposta = true;
				$scope.data.botoes.btEfetivarProposta = true;

				if (disableAll) {
					$scope.data.botoes.btAprovacao = true;
					$scope.data.botoes.btSalvar = true;
				} else {
					$scope.data.botoes.btAprovacao = false;
				}
			} else if (tpSituacaoAprovacao == "E" || tpSituacaoAprovacao == "R") {
				$scope.setDisableAbaFlux(false);

				$scope.data.botoes.btImprimirProposta = tpSituacaoAprovacao == "R";
				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btAprovacao = true;
				$scope.data.botoes.btEfetivarProposta = true;
			} else if (tpSituacaoAprovacao == "" || tpSituacaoAprovacao == "I") {
				$scope.setDisableAbaFlux(true);

				$scope.data.botoes.btAprovacao = false;
				$scope.data.botoes.btImprimirProposta = true;
				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btEfetivarProposta = true;

				if (disableAll) {
					$scope.data.botoes.btSalvar = true;
				}
			} else if (tpSituacaoAprovacao == "M") {
				$scope.setDisableAbaHist(false);
				$scope.setDisableAbaFlux(false);

				$scope.data.botoes.btReprovarEfetivacao = false;
				$scope.data.botoes.btImprimirProposta = false;
				$scope.data.botoes.btEfetivarProposta = false;
				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btAprovacao = true;
			} else if(tpSituacaoAprovacao == "H"){
				$scope.setDisableAbaFlux(false);
				$scope.setDisableAbaHist(false);

				$scope.data.botoes.btImprimirProposta = false;
				$scope.data.botoes.btSolicitarEfetivacao = false;
				$scope.data.botoes.btAprovacao = false;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btEfetivarProposta = true;
			} else if(tpSituacaoAprovacao == "F"){
				$scope.setDisableAbaFlux(false);
				$scope.setDisableAbaHist(false);

				$scope.data.botoes.btImprimirProposta = false;
				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btAprovacao = true;
				$scope.data.botoes.btEfetivarProposta = true;
			}

			if(disableAll == true){
				$scope.setDisableAbaFlux(false);

				$scope.data.botoes.btImprimirProposta = false;
				$scope.data.botoes.btSolicitarEfetivacao = true;
				$scope.data.botoes.btReprovarEfetivacao = true;
				$scope.data.botoes.btAprovacao = true;
				$scope.data.botoes.btEfetivarProposta = true;
			} else {
				$scope.data.botoes.btSalvar = false;
			}

		}

		$scope.solicitarEfetivacaoProposta = function(){
			$rootScope.showLoading = true;
			$scope.rest.doGet("solicitarEfetivacaoProposta?idSimulacao=" + $scope.data.id).then(function(data) {
				$scope.data.dhSolicitacao = data.dhSolicitacao;
				$scope.data.tpSituacaoAprovacao = data.tpSituacaoAprovacaoDomain;
				initializeAbaFrm($stateParams);
				$rootScope.showLoading = false;
				$scope.showSuccessMessage();
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.efetivarProposta = function(blConfirmaEfetivarProposta){
			$rootScope.showLoading = true;
			$scope.data.blConfirmaEfetivarProposta = blConfirmaEfetivarProposta;

			$scope.rest.doPost("efetivarProposta", $scope.data).then(function(data) {
				$scope.setData(data);
				initializeAbaFrm($stateParams);
				$rootScope.showLoading = false;
				$scope.showSuccessMessage();

			}, function(error) {
				//Esse atributo não é necessário no DTO, então o mesmo é removido para evitar erro ao salvar.
				delete $scope.data.blConfirmaEfetivarProposta;

				$rootScope.showLoading = false;
				if(error.data.key == "LMS-01233"){
					//Para não exibir o alerta padrão de erro.
					$rootScope.clearAlerts();
					$scope.confirmarEfetivarProposta(error.data);
				}
			});
		};

		$scope.confirmarEfetivarProposta = function(data){
			modalService.open({windowClass: "modal-formalidades", confirm: true, title: translateService.getMensagem("confirmacao"), message: data.error})
			.then(function() {
				$scope.efetivarProposta(true);
			},function() {
				//Se o usuário responder "Não" abortar o processo.
			});
		};

		$scope.aprovarProposta = function(){
			$rootScope.showLoading = true;
			$scope.rest.doPost("aprovarProposta", $scope.data).then(function() {
				$rootScope.showLoading = false;
				$scope.findById($scope.data.id).then(function(data) {
					$scope.setData(data);
					$scope.showSuccessMessage();
					initializeAbaFrm($stateParams);
				},function() {
				});

			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.atualizar = function(id){
			$scope.findById(id).then(function(data) {
				$scope.setData(data);
				$scope.showSuccessMessage();
				initializeAbaFrm($stateParams);
			},function() {
			});
		};

		$scope.openModalReprov = function() {
			$rootScope.isPopup = true;
			var rest = $scope.rest;
			var atualizar = $scope.atualizar;
			var simulacao = $scope.data;

			var myController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
				$scope.title = "reprovarEfetivacao";
				$scope.innerTemplate = contextPath + "js/app/vendas/manterproposta/view/manterPropostaReprovPopup.html";

				$scope.initializeReprovPopup = function(){
					$scope.limparReprovarEfetivacao();
					$scope.reprovarEfetivacao.idSimulacao = simulacao.id;
				};

				$scope.limparReprovarEfetivacao = function() {
					$scope.reprovarEfetivacao = {};
					$scope.reprovarEfetivacao.idSimulacao = simulacao.id;
					$scope.reprovarEfetivacao.idMotivoReprovacao = null;
					$scope.reprovarEfetivacao.observacaoReprovacao = null;
					$scope.motivosReprovacao = [];
					carregaMotivoReprovacao();
					$("#reprovarForm").removeClass("submitted");
				};

				function carregaMotivoReprovacao() {
					rest.doPost("findMotivoReprovacaoCombo").then(function(motivos) {
						$scope.motivosReprovacao = motivos;
					});
				}

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};

				$scope.reprovarEfetivacaoProposta = function(){
					$rootScope.showLoading = true;
					rest.doPost("reprovarEfetivacaoProposta", $scope.reprovarEfetivacao).then(function(data) {
						$rootScope.showLoading = false;
						$scope.close();
						atualizar(simulacao.id);
					}, function() {
						$rootScope.showLoading = false;
					});
				};

				$scope.initializeReprovPopup();
			}];

			modalService.open({windowClass: "modal-detail", controller: myController})["finally"](function(){$rootScope.isPopup = false;});
		};

		$scope.openModalImprimir = function() {
			$rootScope.isPopup = true;
			var rest = $scope.rest;
			var data = $scope.data;

			var myController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
				$scope.title = "ordenacao";
				$scope.innerTemplate = contextPath + "js/app/vendas/manterproposta/view/manterPropostaImprimirPopup.html";

				$scope.initializeImprimirPopup = function(){
					$scope.ordenacao = {};
					$scope.ordenacao.name = "R";
				};

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};

				$scope.imprimirProposta = function(){
					$rootScope.showLoading = true;
					data.ordenacao = $scope.ordenacao.name;
					rest.doPost("imprimirProposta", data).then(function(data) {
						$rootScope.showLoading = false;
						$scope.close();
						if (data.reportLocator) {
							location.href = contextPath + "/viewBatchReport?" + data.reportLocator;
						}
					}, function() {
						$rootScope.showLoading = false;
					});
				};

				$scope.initializeImprimirPopup();
			}];

			modalService.open({windowClass: "modal-imprimir", controller: myController})["finally"](function(){$rootScope.isPopup = false;});
		};

	}
];


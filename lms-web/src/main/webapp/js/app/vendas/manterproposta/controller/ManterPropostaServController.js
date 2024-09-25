var ManterPropostaServController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"$state",
	"$modal",
	"modalService",
	"translateService",
	"TableFactory",
	function($scope, $rootScope, $stateParams, $state, $modal, modalService, translateService, TableFactory) {

		$scope.initializeAbaServ = function (findServicosAdicionais) {
			$rootScope.showLoading = true;
			$scope.filtrosServicoAdicional = {};

			$scope.servicosAdicionaisTableParams = new TableFactory({
				service: $scope.rest.doPost,
				method: "findServicosAdicionais",
				remotePagination: true
			});

			$scope.rest.doPost("findTabelaPrecoParcelaCombo", {tabelaPreco: {idTabelaPreco:$scope.data.tabelaPreco.idTabelaPreco}, parcelaPreco: {tpParcelaPreco: 'S'} }).then(function(data) {
				$scope.filtrosServicoAdicional.servicos = data.servicos;

				if(findServicosAdicionais === true){
					$scope.findServicosAdicionais();
				}

				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.findServicosAdicionais = function(){
			$scope.filtrosServicoAdicional.id = $scope.data.id;
			$scope.servicosAdicionaisTableParams.load($scope.filtrosServicoAdicional);
		};

		$scope.removeServicosAdicionaisByIds = function() {
			var ids = [];
			$.each($scope.servicosAdicionaisTableParams.selected, function() {
				ids.push(this.idServicoAdicionalCliente);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeServicoAdicionalClienteByIds", ids).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findServicosAdicionais();
						}, function() {
							$rootScope.showLoading = false;
						});
					});
			}
		};

		$scope.detailServicoAdicionalCliente = function(row){
			$scope.openModalServicos(row.idServicoAdicionalCliente);
		};

		$scope.openModalServicos = function(idServicoAdicionalCliente) {
			$rootScope.isPopup = true;
			var rest = $scope.rest;
			var simulacao = $scope.data;
			var findServicosAdicionaisFunction = $scope.findServicosAdicionais;

			var myController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
				$scope.title = "servicosAdicionaisProposta";
				$scope.innerTemplate = contextPath + "js/app/vendas/manterproposta/view/manterPropostaServPopup.html";

				$scope.initializeServPopup = function(){
					$scope.simulacao = simulacao;
					$scope.limparServicoAdicionalCliente();
					$scope.idParcelaPrecoChange();

					rest.doPost("findTabelaPrecoParcelaCombo", {tabelaPreco: {idTabelaPreco: simulacao.tabelaPreco.idTabelaPreco}, parcelaPreco: {tpParcelaPreco: 'S'} }).then(function(data) {
						$scope.servicos = data.servicos;
						$rootScope.showLoading = false;
						$scope.findByIdServicoAdicionalCliente(idServicoAdicionalCliente);
					}, function() {
						$rootScope.showLoading = false;
					});
					
				};

				$scope.findByIdServicoAdicionalCliente = function(idServicoAdicionalCliente){
					if(angular.isDefined(idServicoAdicionalCliente)){
						$rootScope.showLoading = true;
						rest.doGet("findByIdServicoAdicionalCliente?idServicoAdicionalCliente=" + idServicoAdicionalCliente).then(function(data) {
							$scope.servicoAdicionalCliente = data;
							$scope.idParcelaPrecoChangeEdit();
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
					}
				};

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};

				$scope.limparServicoAdicionalCliente = function() {
					$scope.servicoAdicionalCliente = {};
					$scope.servicoAdicionalCliente.tpFormaCobranca = {};
					$scope.servicoAdicionalCliente.tpFormaCobranca.value = "P";
					$scope.servicoAdicionalCliente.blCobrancaRetroativa = true;
					$("#servicosForm").removeClass("submitted");
				};

				$scope.storeServicosAdicionais = function(){
					$rootScope.showLoading = true;
					$scope.servicoAdicionalCliente.simulacao = {};
					$scope.servicoAdicionalCliente.simulacao.idSimulacao = simulacao.id;

					rest.doPost("storeServicoAdicionalCliente", $scope.servicoAdicionalCliente).then(function(data) {
						$scope.servicoAdicionalCliente.idServicoAdicionalCliente = data.idServicoAdicionalCliente;
						$rootScope.showLoading = false;
						$scope.showSuccessMessage();
		            	findServicosAdicionaisFunction();
					}, function(data) {
						$rootScope.showLoading = false;
					});
				};

				$scope.$watch('servicoAdicionalCliente.tpIndicador', function(data){
					if(angular.isDefined($scope.servicoAdicionalCliente.tpIndicador) && $scope.servicoAdicionalCliente.tpIndicador.value == "T"){
						$scope.servicoAdicionalCliente.vlValor = 0;
						$scope.servicoAdicionalCliente.vlMinimo = 0;

						$scope.vlValorDisabled = true;
						$scope.vlMinimoDisabled = true;
					} else {
						$scope.vlValorDisabled = false;
						$scope.vlMinimoDisabled = false;
					}
				});
				
				
				//LMSA-365
				$scope.idParcelaPrecoChange = function(){
					$rootScope.showLoading = true;
					if(angular.isDefined($scope.servicoAdicionalCliente.parcelaPreco)){
						rest.doGet("findTaxaPermanenciaCargaOrTaxaFielDepositario?idParcelaPreco=" + $scope.servicoAdicionalCliente.parcelaPreco.idParcelaPreco).then(function(data) {
							if(data.isTaxaPermanenciaCargaOrTaxaFielDepositario){
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr = {};
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr.value = "T";
								$scope.tpUnidMedidaCalcCobrRequired = true;
								$scope.tpUnidMedidaCalcCobrDisabled = false;
							}else{
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr = {};
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr.value = "";
								$scope.tpUnidMedidaCalcCobrRequired = false;
								$scope.tpUnidMedidaCalcCobrDisabled = true;
							}
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
					}
				};
				
				$scope.idParcelaPrecoChangeEdit = function(){
					$rootScope.showLoading = true;
					if(angular.isDefined($scope.servicoAdicionalCliente.parcelaPreco)){
						rest.doGet("findTaxaPermanenciaCargaOrTaxaFielDepositario?idParcelaPreco=" + $scope.servicoAdicionalCliente.parcelaPreco.idParcelaPreco).then(function(data) {
							if(data.isTaxaPermanenciaCargaOrTaxaFielDepositario){
								$scope.tpUnidMedidaCalcCobrRequired = true;
								$scope.tpUnidMedidaCalcCobrDisabled = false;
							}else{
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr = {};
								$scope.servicoAdicionalCliente.tpUnidMedidaCalcCobr.value = "";
								$scope.tpUnidMedidaCalcCobrRequired = false;
								$scope.tpUnidMedidaCalcCobrDisabled = true;
							}
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
					}
				};

				$scope.vlValorBlur = function(){
					var value = $scope.servicoAdicionalCliente.vlValor;

					if (angular.isDefined(value)){
						if(angular.isDefined($scope.servicoAdicionalCliente.tpIndicador) && $scope.servicoAdicionalCliente.tpIndicador.value == "D"){
							if(value < 0 || value > 100){
								delete $scope.servicoAdicionalCliente.vlValor;
								$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("valor")]), type: MESSAGE_SEVERITY.WARNING}]);
							}
						} else {
							if(value < 0){
								delete $scope.servicoAdicionalCliente.vlValor;
								$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("valor")]), type: MESSAGE_SEVERITY.WARNING}]);
							}
						}
					}
				};

				$scope.nrQuantidadeDiasBlur = function(){
					var value = $scope.servicoAdicionalCliente.nrQuantidadeDias;
					if(angular.isDefined(value) && parseInt(value, 10) < 0){
						delete $scope.servicoAdicionalCliente.nrQuantidadeDias;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("quantidadeDias")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				};

				$scope.vlMinimoBlur = function(){
					var value = $scope.servicoAdicionalCliente.vlMinimo;
					if(angular.isDefined(value) && parseInt(value, 10) < 0){
						delete $scope.servicoAdicionalCliente.vlMinimo;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("valorMinimo")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				};

				$scope.nrDecursoPrazoBlur = function(){
					var value = $scope.servicoAdicionalCliente.nrDecursoPrazo;
					if(angular.isDefined(value) && (parseInt(value, 10) < 1 || parseInt(value, 10) > 999)){
						delete $scope.servicoAdicionalCliente.nrDecursoPrazo;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("decursoPrazo")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				};

				$scope.removeServicoAdicionalClienteById = function(){
					if ($scope.servicoAdicionalCliente.idServicoAdicionalCliente) {
						$scope.confirm($scope.getMensagem("erExcluirRegistroAtual")).then(function(response) {
							$rootScope.showLoading = true;

							rest.doPost("removeServicoAdicionalClienteById", $scope.servicoAdicionalCliente.idServicoAdicionalCliente).then(function(data) {
									$rootScope.showLoading = false;
									$scope.showSuccessMessage();
									$scope.limparServicoAdicionalCliente();
									findServicosAdicionaisFunction();
							}, function() {
								$rootScope.showLoading = false;
							});
						});
					} else {
						$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: MESSAGE_SEVERITY.WARNING}]);
					}
				};

				$scope.initializeServPopup();
			}];

			modalService.open({windowClass: "modal-detail", controller: myController})["finally"](function(){$rootScope.isPopup = false;});
		};

		if($scope.data && $scope.data.id){
			$scope.initializeAbaServ(true);
		}
	}
];


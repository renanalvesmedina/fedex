var ProgramacaoColetasVeiculosControllerCadController = [
	"$rootScope",                                         
	"$scope",
	"$stateParams",
	"typeTab",
	"editTabState",
	"modalService",
	"TableFactory", 
	function($rootScope, $scope, $stateParams, typeTab, editTabState, modalService, TableFactory) {
		$scope.initAbas($stateParams, typeTab, editTabState);		
				
		$scope.listTableColetasRealizadas = new TableFactory({
			service: $scope.rest.doPost,
			method: "findColetasRealizadas",
			remotePagination: false
		});
		
		$scope.listTableEntregasRealizar = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEntregasRealizar",
			remotePagination: false
		});
		
		$scope.listTableColetasPendentes = new TableFactory({
			service: $scope.rest.doPost,
			method: "findColetasPendentes",
			remotePagination: false
		});

		$scope.listTableEntregasRealizadas = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEntregasRealizadas",
			remotePagination: false
		});
		
		$scope.desabilitarConfirmTab = function(){
			$rootScope.currentTab.isChangesAbandoned = true;
		}
		
		$scope.desabilitarConfirmTab();
		
		if ($stateParams.id) {
			$scope.listTableColetasRealizadas.load({id: $stateParams.id});
			$scope.listTableEntregasRealizar.load({id: $stateParams.id});
			$scope.listTableColetasPendentes.load({id: $stateParams.id});
			$scope.listTableEntregasRealizadas.load({id: $stateParams.id});
		} else {
			$scope.listTableColetasRealizadas.clear();
			$scope.listTableEntregasRealizar.clear();
			$scope.listTableColetasPendentes.clear();
			$scope.listTableEntregasRealizadas.clear();
		}
		
		
		$scope.atualizarListaColetasPendentes = function() {
			if ($stateParams.id) {
				$scope.listTableColetasPendentes.load({id: $stateParams.id});	
			}else{
				$scope.listTableColetasPendentes.clear();	
			}
		};
		
		$scope.atualizarListaColetasRealizadas = function() {
			if ($stateParams.id) {
				$scope.listTableColetasRealizadas.load({id: $stateParams.id});	
			}else{
				$scope.listTableColetasRealizadas.clear();	
			}
		};
		
		function getScope() {
			return $scope;
		}
		
		
		$scope.desabilitarConfirmTab();
		
		$scope.openModalExecutar = function() {
			var rest = $scope.rest;
			var itensSelecionados = $scope.listTableColetasPendentes.selected;
			
			if (itensSelecionados.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
			} else {

				var myController = [
						"$scope",
						"$modalInstance",
						"$rootScope",
						function($scope, $modalInstance, $rootScope) {

							//$rootScope.isPopup = true;
							$scope.title = "Executar coleta";
							$scope.innerTemplate = contextPath
									+ "js/app/coleta/programacaocoletasveiculos/view/modalInformarDataEvento.html";
							
							$scope.generateExecutar = function(retornarColetaDTO){
								
								modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-02012"), windowClass: 'modal-confirm'})
								.then(function() {
									rest.doPost("generateExecutarColetasPendentes", retornarColetaDTO).then(
											function(response) {
												if (response.businessMsg != null) {
													$scope.addAlerts([ {
																msg : response.businessMsg,
																type : 'danger'
															} ]);
													
												}else{
													$scope.showSuccessMessage();
													getScope().atualizarListaColetasPendentes();
													getScope().atualizarListaColetasRealizadas();
													$scope.close();
												}
											});	
								});
							}

								$scope.confirmar = function() {
								var idsPedidoColeta = [];
								$.each(itensSelecionados, function() {
									idsPedidoColeta.push(this.idPedidoColeta);
									});
								var retornarColetaDTO = {};
								retornarColetaDTO.idsPedidoColeta = idsPedidoColeta;
								retornarColetaDTO.dtHoraOcorrencia = $scope.data.dtHoraOcorrencia;
								$rootScope.showLoading = true;

								rest.doPost("executarColeta", retornarColetaDTO).then(
												function(response) {
													if (response.businessMsg != null) {
														$scope.addAlerts([ {
																	msg : response.businessMsg,
																	type : 'danger'
																} ]);
														
													} else {
														$scope.generateExecutar(retornarColetaDTO);
														getScope().desabilitarConfirmTab();
													}
												}, function(erro) {
													
												})['finally'](function() {
													getScope().desabilitarConfirmTab();				
													$rootScope.showLoading = false;
								
								});
							}
	
							$scope.data = {};
							rest.doPost("getDadosModalExecutarColeta").then(
									function(data) {
											$scope.data.nmUsuario = data.nmUsuario;
											$scope.data.dtHoraOcorrencia = data.dtHoraOcorrencia;
									});

							$scope.close = function() {

								$modalInstance.dismiss("cancel");
							};

						} ];
				modalService.open({
					controller : myController,
					windowClass : "my-modal"
				});

			}
		};
		
				
		$scope.openModalRetornar = function() {

			var idControleCarga = $stateParams.id;
			var rest = $scope.rest;
			var itensSelecionados = $scope.listTableColetasPendentes.selected;

			if (itensSelecionados.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
			} else {

				var myController = [
						"$scope",
						"$modalInstance",
						"$rootScope",
						function($scope, $modalInstance, $rootScope) {

							$rootScope.isPopup = true;
							$scope.title = "Retorno da coleta";
							$scope.innerTemplate = contextPath
									+ "js/app/coleta/programacaocoletasveiculos/view/programacaoColetasVeiculosRetorno.html";

							$scope.confirmar = function() {
								var idsPedidoColeta = [];
								$.each(itensSelecionados, function() {
									idsPedidoColeta.push(this.idPedidoColeta);
									});

								var retornarColetaDTO = {};
								
								retornarColetaDTO.idsPedidoColeta = idsPedidoColeta;
								retornarColetaDTO.idControleCarga = idControleCarga;
								retornarColetaDTO.idOcorrenciaColeta = $scope.data.idOcorrenciaColeta;
								retornarColetaDTO.blIneficienciaFrota = $scope.data.blIneficienciaFrota;
								retornarColetaDTO.dtHoraOcorrencia = $scope.data.dtHoraOcorrencia;
								retornarColetaDTO.dsDescricao = $scope.data.dsDescricao;
								if($scope.data.meioTransporte!=undefined){
									retornarColetaDTO.idMeioTransporte =$scope.data.meioTransporte.idMeioTransporte;
								}

								$rootScope.showLoading = true;

								rest.doPost("retornarColeta",retornarColetaDTO).then(
												function(response) {
													if (response.businessMsg != null) {
														$scope.addAlerts([ {
																	msg : response.businessMsg,
																	type : 'danger'
																} ]);
													} else {
														$scope.showSuccessMessage();
														getScope().atualizarListaColetasPendentes();
														getScope().listTableProgramacaoColetas.load({});
														
														$scope.close();
														
														getScope().desabilitarConfirmTab();
													}
												}, function(erro) {

												})['finally'](function() {
									$rootScope.showLoading = false;
									getScope().desabilitarConfirmTab();
								});
							}
							
							$scope.ocorrenciasColeta = {};
							$scope.data = {};
							var retornarColetaDTO = {};
							rest.doPost("getDadosModalRetornarColeta").then(
									function(data) {
											$scope.ocorrenciasColeta = data.ocorrencias;
											$scope.data.nmUsuario = data.nmUsuario;
											$scope.data.dtHoraOcorrencia = data.dtHoraOcorrencia;
									});
							
							$scope.close = function() {
								$rootScope.isPopup = false;
								$modalInstance.dismiss("cancel");
							};

						} ];
				modalService.open({
					controller : myController,
					windowClass : "my-modal"
				});

			}
		};
	} ];
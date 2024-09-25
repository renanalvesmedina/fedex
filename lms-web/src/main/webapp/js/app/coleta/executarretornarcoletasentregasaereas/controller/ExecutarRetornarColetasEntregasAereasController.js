var ExecutarRetornarColetasEntregasAereasController = [
	"$scope",
	"$rootScope",
	"TableFactory",
	"modalService",
 	function($scope, $rootScope,TableFactory, modalService) {
		$scope.setConstructor({
			rest: "/coleta/coletasEntregasAereas"
		});
		
		$scope.filter = {};
		$scope.tmp = {tpStatusAwb : null};
		
		$scope.$watch('tmp.awb', function (awb) {
			$scope.filter.awb = awb;
		}, true);
		
		$scope.coletasPendentesTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findColetasPendentes",
			remotePagination: false
		});
		
		$scope.entregasRealizarTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEntregasRealizar",
			remotePagination: false
		});
		
		$scope.entregasRealizadasTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findEntregasRealizadas",
			remotePagination: false
		});
		
		
		$scope.consultar = function(){
			if ($scope.filter.meioTransporte 	!== undefined ||
				$scope.filter.controleCarga 	!== undefined ||
				$scope.filter.manifestoColeta 	!== undefined ||
				$scope.filter.manifestoEntrega 	!== undefined ||
				$scope.filter.doctoServico 		!== undefined ||
				$scope.filter.awb				!== undefined) {
				$scope.coletasPendentesTableParams.load($scope.filter);
				$scope.entregasRealizarTableParams.load($scope.filter);
				$scope.entregasRealizadasTableParams.load($scope.filter);
			} else {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00055"), type: "danger"}]);
			}
		};
		
		$scope.clearFilter = function() {
			$scope.filter = {};
			$scope.tmp = {tpStatusAwb : null};
			$scope.coletasPendentesTableParams.clear();
			$scope.entregasRealizarTableParams.clear();
			$scope.entregasRealizadasTableParams.clear();
		};
		
		$scope.executarColetasPendentes = function(){
			var ids = [];
			$.each($scope.coletasPendentesTableParams.selected, function() {
					ids.push({"idPedidoColeta":this.id, "idAwb": this.idAwb});
			});
			
			$rootScope.showLoading = true;
			if (ids.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
				$rootScope.showLoading = false;
			} else {
				$scope.rest.doPost("executarColetasPendentes", ids).then(function(response) {
					$scope.showSuccessMessage();
					$scope.coletasPendentesTableParams.load($scope.filter);
					$scope.entregasRealizarTableParams.load($scope.filter);
					$scope.entregasRealizadasTableParams.load($scope.filter);
					$rootScope.showLoading = false;
				},function() {
					$rootScope.showLoading = false;
				});
			}
		};
		
		$scope.openModalRetornarColeta = function() {
			var rest = $scope.rest;
			var itensSelecionados = $scope.coletasPendentesTableParams.selected;
			var filter = $scope.filter;
			var coletasPendentesTableParams = $scope.coletasPendentesTableParams;
			
			if (itensSelecionados.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
				$rootScope.showLoading = false;
			} else {
				var myController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
					$rootScope.isPopup = true;
					$scope.title = "retornarEntregasNoAeroporto";
					$scope.innerTemplate = contextPath+"js/app/coleta/executarretornarcoletasentregasaereas/view/modalRetornarColetas.html";
					
					$scope.ocorrenciasColeta = {};
					$scope.data = {};
					$scope.data.dtHrOcorrencia = moment();
					
					rest.doPost("findOcorrenciaColetaRetorno").then(function(ocorrencias) {
						$scope.ocorrenciasColeta = ocorrencias;
					});
					
					$scope.retornarColetasAeroporto = function() {
						$rootScope.showLoading = true;
						var retornarColetaDTO = {};
						var idsPedidoColeta = [];
						var idsPedidoColetaAndAwb = [];
						
						$.each(itensSelecionados, function() {
							idsPedidoColeta.push(this.id);
							idsPedidoColetaAndAwb.push({"idPedidoColeta":this.id, "idAwb": this.idAwb});
						});
						
						retornarColetaDTO.idsPedido = idsPedidoColeta;
						retornarColetaDTO.idsPedidoAndAwb = idsPedidoColetaAndAwb;
						retornarColetaDTO.idOcorrenciaColeta = $scope.data.idOcorrenciaColeta;
						retornarColetaDTO.blIneficienciaFrota = $scope.data.blIneficienciaFrota;
						retornarColetaDTO.dtHrOcorrencia = $scope.data.dtHrOcorrencia;
	
						rest.doPost("retornarColetasAeroporto", retornarColetaDTO).then(function(response) {
							coletasPendentesTableParams.load(filter);
							$scope.showSuccessMessage();
							$modalInstance.dismiss("cancel");
							$rootScope.showLoading = false;
							$rootScope.isPopup = false;
						},function() {
							$rootScope.showLoading = false;
						});
					};				
					
					$scope.close = function() {
						$modalInstance.dismiss("cancel");
					};
				}];
				modalService.open({controller: myController, windowClass: "my-modal"});
			}
		};
		
		$scope.openModalExecutarEntrega = function() {
			var rest = $scope.rest;
			var itensSelecionados = $scope.entregasRealizarTableParams.selected;
			var filter = $scope.filter;
			var entregasRealizarTableParams = $scope.entregasRealizarTableParams;
			var entregasRealizadasTableParams = $scope.entregasRealizadasTableParams;
			
			if (itensSelecionados.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
				$rootScope.showLoading = false;
			} else {
				var myController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
					$rootScope.isPopup = true;
					$scope.title = "executarEntregas";
					$scope.innerTemplate = contextPath+"js/app/coleta/executarretornarcoletasentregasaereas/view/modalExecutarEntregas.html";
					
					$scope.data = {};
					
					$scope.executarEntregasRealizar = function(){
						var entregaRealizarDTOs = [];
						$.each(itensSelecionados, function() {
							var entregaRealizarDTO = {};
							entregaRealizarDTO.id = this.id;
							entregaRealizarDTO.nmRecebedor = $scope.data.nmRecebedor;
							entregaRealizarDTO.isEntregaAeroporto = this.isEntregaAeroporto;
							entregaRealizarDTOs.push(entregaRealizarDTO);
						});
						
						$rootScope.showLoading = true;
						rest.doPost("executarEntregasRealizar", entregaRealizarDTOs).then(function(response) {
							$scope.showSuccessMessage();
							entregasRealizarTableParams.load(filter);
							entregasRealizadasTableParams.load(filter);
							$rootScope.showLoading = false;
							$rootScope.isPopup = false;
							$modalInstance.dismiss("cancel");
						}, function() {
							$rootScope.showLoading = false;
						});
					};				
					
					$scope.close = function() {
						$modalInstance.dismiss("cancel");
					};
				}];				
				modalService.open({controller: myController, windowClass: "my-modal"});
			}
		};
		
		$scope.openModalRetornarEntrega = function() {
			var rest = $scope.rest;
			var itensSelecionados = $scope.entregasRealizarTableParams.selected;
			var filter = $scope.filter;
			var entregasRealizarTableParams = $scope.entregasRealizarTableParams;
			
			if (itensSelecionados.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-00053"), type: "warning"}]);
				$rootScope.showLoading = false;
			} else {
				var myController = ["$scope", "$modalInstance", function($scope, $modalInstance) {
					$rootScope.isPopup = true;
					$scope.title = "retornarEntregasNoAeroporto";
					$scope.innerTemplate = contextPath+"js/app/coleta/executarretornarcoletasentregasaereas/view/modalRetornarEntregas.html";
					
					$scope.ocorrenciasEntrega = {};
					$scope.data = {};
					$scope.data.dhOcorrencia = moment();
					
					rest.doPost("findOcorrenciaEntregaRetorno").then(function(ocorrencias) {
						$scope.ocorrenciasEntrega = ocorrencias;
					});
					
					$scope.retornarEntregaAeroporto = function() {
						$rootScope.showLoading = true;
						var entregaRealizarDTOs = [];
						
						$.each(itensSelecionados, function() {
							var entregaRealizarDTO = {};
							entregaRealizarDTO.id = this.id;
							entregaRealizarDTO.tpStatusManifesto = this.tpStatusManifesto;
							entregaRealizarDTO.cdOcorrenciaEntrega = $scope.data.cdOcorrenciaEntrega;
							entregaRealizarDTO.dhOcorrencia = $scope.data.dhOcorrencia;							
							entregaRealizarDTOs.push(entregaRealizarDTO);							
						});

						rest.doPost("retornarEntregasAeroporto", entregaRealizarDTOs).then(function(response) {
							entregasRealizarTableParams.load(filter);
							$scope.showSuccessMessage();
							$modalInstance.dismiss("cancel");
							$rootScope.showLoading = false;
							$rootScope.isPopup = false;
						}, function() {
							$rootScope.showLoading = false;
						});
					};				
					
					$scope.close = function() {
						$modalInstance.dismiss("cancel");
					};
				}];				
				modalService.open({controller: myController, windowClass: "my-modal"});
			}
		};		
		
		
		
		
	}
];

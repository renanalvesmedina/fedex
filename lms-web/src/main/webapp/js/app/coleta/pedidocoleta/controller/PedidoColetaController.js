var PedidoColetaController = [
		"$scope",
		"$rootScope",
		"$state",
		"modalService",
		"TableFactory",
		function($scope, $rootScope, $state, modalService,TableFactory) {
			
			$scope.setConstructor({
				rest : "/coleta/pedidocoleta/pedidoColeta"
			});
			
			$scope.modalDadosCliente = function(idCliente) {
				console.log(idCliente);
				modalService.open({
					controller : ColetasDadoClienteController,
					windowClass : 'my-modal',
					data:{idCliente : idCliente}
				});
			};
			
			$scope.modalEventosColeta = function(idPedidoColeta) {

				modalService.open({
					controller : EventosColetaController,
					windowClass : 'my-modal',
					data:{idPedidoColeta : idPedidoColeta}
				});
			};
			
			function getScope() {
				return $scope;
			}

			$scope.openModalAWBs = function(idPedidoColeta) {
				var rest = $scope.rest;
				var myController = [
						"$scope",
						"$modalInstance",
						"$rootScope",
						
						function($scope, $modalInstance, $rootScope) {

							var pedidoColetaFilterDTO = {};
							pedidoColetaFilterDTO.id = idPedidoColeta;
							$scope.title = "AWB";
							$scope.innerTemplate = contextPath + "js/app/coleta/pedidocoleta/view/consultaAWBColeta.html";
							
							
							$scope.listTableAWBs = new TableFactory({
								service : rest.doPost,
								method : "findAwbs",
								remotePagination : false
							});
							$scope.listTableAWBs.load(pedidoColetaFilterDTO);
							
							$scope.close = function() {
								$modalInstance.dismiss("cancel");
							};

						} ];
				modalService.open({
					controller : myController,
					windowClass : "my-modal"
				});
			};
			
			$scope.cadastrar = function() {
				if ($scope.editTabState) {
					$state.transitionTo($scope.editTabState, {id: null}, {
						reload: false,
						inherit: false,
						notify: true
					});
				}
			};
			
			$scope.inicializarCamposPedidoColetaList = function () {
				
				$scope.rest.doPost("inicializarCamposPedidoColetaList").then(function(filter) {
					$scope.filter.filial = filter.filial;
					$scope.filter.dtPedidoColetaInicial = filter.dtPedidoColetaInicial;
					$scope.filter.dtPedidoColetaFinal = filter.dtPedidoColetaFinal;
				});
				
			};
			
			$scope.inicializarCamposPedidoColetaList();
			
			$scope.desabilitaRota = false;
			$scope.desabilitaRegiao = false;
			$scope.desabilitaCliente = false;
			
			$scope.$watch('filter.rotaColetaEntrega', function(data) {
				if (data != null) {
					$scope.desabilitaRegiao = true;
					$scope.desabilitaCliente = true;
				} else {
					$scope.desabilitaRegiao = false;
					$scope.desabilitaCliente = false;
				}
			});
			
			$scope.$watch('filter.regiaoColetaEntrega', function(data) {
				if (data != null) {
					$scope.desabilitaRota = true;
					$scope.desabilitaCliente = true;
				} else {
					$scope.desabilitaRota = false;
					$scope.desabilitaCliente = false;
				}
			});
			
			$scope.$watch('filter.cliente', function(data) {
				if (data != null) {
					$scope.desabilitaRota = true;
					$scope.desabilitaRegiao = true;
				} else {
					$scope.desabilitaRota = false;
					$scope.desabilitaRegiao = false;
				}
			});
			
			$scope.clearFilter = function() {
				$scope.filter = {};
				$scope.listTableParams.clear();
				$scope.inicializarCamposPedidoColetaList();
				$scope.setFilter($scope.filter);
			};

			
		} ];

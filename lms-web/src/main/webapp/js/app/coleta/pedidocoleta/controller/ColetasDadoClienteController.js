var ColetasDadoClienteController = [
		"$scope",
		"$rootScope",
		"$modalInstance",
		"RestAccessFactory",
		"TableFactory",
		"data",
		function($scope, $rootScope, $modalInstance, RestAccessFactory,TableFactory,data) {
			$scope.rest = RestAccessFactory.create("/coleta/pedidocoleta/pedidoColeta");
		
			$scope.title = "Clientes";
			$scope.innerTemplate = contextPath	+ "js/app/coleta/pedidocoleta/view/consultarColetasDadosCliente.html";
			$rootScope.isPopup = true;
			$scope.close = function() {
				$rootScope.isPopup = false;
				$modalInstance.dismiss("cancel");
				
			};
			
			var pedidoColetaFilterDTO = {};
			pedidoColetaFilterDTO.idCliente = data.idCliente;

			$scope.setDados = function() {
				
				$scope.rest.doPost("findDadosClientesColeta", pedidoColetaFilterDTO)
						.then(function(response) {
							
							$scope.data = response;
						});
			};
			$scope.setDados();
			
			$scope.listTableEnderecosColeta = new TableFactory({
				service: $scope.rest.doPost,
				method: "findEnderecosColeta",
				remotePagination: false
			});
			$scope.listTableEnderecosColeta.load(pedidoColetaFilterDTO);
			
			$scope.listColetasAutomaticas = function(row){
				pedidoColetaFilterDTO.idEnderecoPessoa = row.id;
				$scope.listTableColetasAutomaticas.load(pedidoColetaFilterDTO)
			};
			
			$scope.listTableColetasAutomaticas = new TableFactory({
				service: $scope.rest.doPost,
				method: "findColetasAutomatica",
				remotePagination: false
			});
			

		} ];

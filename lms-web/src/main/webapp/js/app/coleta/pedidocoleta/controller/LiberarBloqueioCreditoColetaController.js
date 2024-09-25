var LiberarBloqueioCreditoColetaController = [
		"$scope",
		"$rootScope",
		"$modalInstance",
		"RestAccessFactory",
		"TableFactory",
		"data",
		function($scope, $rootScope, $modalInstance, RestAccessFactory,TableFactory,data) {
			$scope.rest = RestAccessFactory.create("/coleta/pedidocoleta/pedidoColeta");
		
			$scope.data = {};
			$scope.title = "Liberar bloqueio de credito";
			$scope.innerTemplate = contextPath	+ "js/app/coleta/pedidocoleta/view/liberarBloqueioCreditoColeta.html";
			$rootScope.isPopup = true;
			$scope.close = function() {
				$rootScope.isPopup = false;
				$modalInstance.dismiss("cancel");
				
			};
			
			$scope.liberar = function() {
				
				retorno = {idUsuario: $scope.data.usuarioDTO.idUsuario,
						   idOcorrenciaColeta: $scope.data.idOcorrenciaColeta,
						   dsDescricao: $scope.data.dsDescricao,
						   buttonBloqueioCredito: true};
				
				data.callback(retorno);
				
				$rootScope.isPopup = false;
				$modalInstance.dismiss("cancel");
				
				return retorno;
				
			};
			
			var pedidoColetaFilterDTO = {};
			pedidoColetaFilterDTO.idCliente = data.idCliente;

			$scope.ocorrenciasColeta = {};
			$scope.rest.doPost("findOcorrenciaColetaLiberacaoCredito").then(function(ocorrencias) {
				$scope.ocorrenciasColeta = ocorrencias;
			});
			
			$scope.listTableProibidoEmbarque = new TableFactory({
				service: $scope.rest.doPost,
				method: "findProibidoEmbarque",
				remotePagination: false
			});
			$scope.listTableProibidoEmbarque.load(pedidoColetaFilterDTO);

		} ];

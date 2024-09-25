var EventosColetaController = [
		"$scope",
		"$rootScope",
		"$modalInstance",
		"RestAccessFactory",
		"TableFactory",
		"data",
		function($scope, $rootScope, $modalInstance,
				RestAccessFactory,TableFactory,data) {

			$scope.rest = RestAccessFactory.create("/coleta/programacaoColetasVeiculos");

			$scope.innerTemplate = contextPath + "js/app/coleta/programacaocoletasveiculos/view/consultarEventosColeta.html";
			$scope.title= "Eventos da coleta";
			$rootScope.isPopup = true;
			var idPedidoColeta = data.idPedidoColeta;
			

			$scope.listTableEventosColeta = new TableFactory({
				service : $scope.rest.doPost,
				method : "consultaEventosColeta",
				remotePagination : false
			});
			
			$scope.listTableEventosColeta.load({idPedidoColeta : idPedidoColeta});

			
			$scope.rest.doPost("findPedidoColetaById",
					data.idPedidoColeta).then(
					function(data) {

						$scope.data = data;
						$rootScope.showLoading = false;
					});
			
			
			

			$scope.visualizaRelatorioEventosColeta = function() {
				$rootScope.showLoading = true;
				
				$scope.rest.doPost("visualizaRelatorioEventosColeta",
						idPedidoColeta).then(
						function(data) {
							
							$rootScope.showLoading = false;
							if (data.reportLocator) {
								location.href = contextPath
										+ "/viewBatchReport?"
										+ data.reportLocator;
							}
						}, function() {
							$rootScope.showLoading = false;
						});
			};
			
			$scope.exportExcel = function() {
				$rootScope.showLoading = true;
				$scope.rest.doPost("exportExcelEventosColeta",
						idPedidoColeta).then(
						function(data) {
							
							$rootScope.showLoading = false;
							if (data.reportLocator) {
								location.href = contextPath
										+ "/viewBatchReport?"
										+ data.reportLocator;
							}
						}, function() {
							$rootScope.showLoading = false;
						});
			};

			$scope.close = function() {
				$rootScope.isPopup = false;
				$modalInstance.dismiss("cancel");
			};
			

		} ];

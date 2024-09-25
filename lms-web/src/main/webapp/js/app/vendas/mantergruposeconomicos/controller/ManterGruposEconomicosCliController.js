var ManterGruposEconomicosCliController = [
	"$scope",
	"$rootScope",
	"TableFactory",
	function($scope, $rootScope, TableFactory) {
		
		$scope.dados = {};

		$scope.clientesTableParams = new TableFactory({
			service: $scope.rest.doPost,
			method: "findGrupoEconomicoClientes",
			remotePagination: true
		});
		
		$scope.disabledRow =  function (row) {
			return !row.id;
		};

		$scope.findGrupoEconomicoClientes = function(){
			$scope.clientesTableParams.load({id: $scope.data.id});
		};
		
		$scope.salvarCliente = function(){

			if (angular.isUndefined($scope.dados.cliente)){
				$scope.addAlerts([{msg: window.AlertsConstants.CAMPO_OBRIGATORIO, type: MESSAGE_SEVERITY.WARNING}]);
				$event.preventDefault();
				return false;
			}

			$rootScope.showLoading = true;
			$scope.dados.idGrupoEconomico = $scope.data.id;
			$scope.dados.idCliente = $scope.dados.cliente.idCliente;
			$scope.rest.doPost("storeGrupoEconomicoCliente", $scope.dados).then(function() {
				$scope.limparClientes();
				$scope.findGrupoEconomicoClientes();
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
			
		};

		$scope.removeGrupoEconomicoClientesByIdsClientes = function() {
			var ids = [];
			$.each($scope.clientesTableParams.selected, function() {
				ids.push(this.id);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.rest.doPost("removeGrupoEconomicoClientesByIdsClientes", ids).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findGrupoEconomicoClientes();
						}, function() {
							$rootScope.showLoading = false;
						});

					});
			}
		};
			
		$scope.limparClientes = function(){
			$scope.dados = {};
			$("#cliente").removeClass("lms-invalid");
			$("#clientesForm").removeClass("submitted");
		};

		$scope.findGrupoEconomicoClientes();
	}
];


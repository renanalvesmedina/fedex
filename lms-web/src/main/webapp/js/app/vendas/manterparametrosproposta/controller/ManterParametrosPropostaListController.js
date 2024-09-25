var ManterParametrosPropostaListController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"TableFactory",
	"editTabState",
	function($scope, $rootScope, $stateParams, TableFactory, editTabState) {
		$scope.parametroPropostaFiltro = {};

		$scope.parametrosPropostaTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "findParametrosProposta",
			remotePagination: true
		});

		$scope.findParametrosProposta = function(){
			$scope.parametrosPropostaTable.load($scope.data);
		};

		$scope.initializeAbaList = function(params){
			$scope.setDisableAbaRota(!(!!$scope.data.tpGeracaoProposta && "T" == $scope.data.tpGeracaoProposta.value));
			$scope.setDisableAbaParam(true);
			$scope.setDisableAbaTaxas(true);
			$scope.setDisableAbaGeneralidades(true);
			
			$scope.setEditTabState(editTabState);

			$scope.findParametrosProposta();
		};

		$scope.removeParametrosPropostaTableByIds = function(){
			var ids = [];
			$.each($scope.parametrosPropostaTable.selected, function() {
				ids.push(this.idParametroCliente);
			});

			if (ids.length === 0) {
				$scope.addAlerts([ {msg : $scope.getMensagem("erSemRegistro"), type : MESSAGE_SEVERITY.WARNING } ]);
			} else {
				$scope.confirm($scope.getMensagem("erExcluir")).then(
					function() {
						$rootScope.showLoading = true;
						$scope.parametroPropostaFiltro.idSimulacao = $scope.data.idSimulacao;
						$scope.parametroPropostaFiltro.ids = ids;

						$scope.rest.doPost("removeParametrosPropostaByIds", $scope.parametroPropostaFiltro).then(function(data) {
								$rootScope.showLoading = false;
								$scope.showSuccessMessage();
								$scope.findParametrosProposta();
						}, function() {
							$rootScope.showLoading = false;
						});
					});
			}
		};

		$scope.initializeAbaList($stateParams);
	}
];


var ManterPropostaFluxoController = [
	"$scope",
	"TableFactory",
	"WorkflowFactory",
	function($scope, TableFactory, WorkflowFactory) {

		$scope.initializeAbaFluxo = function () {

			$scope.fluxoAprovacaoTableParams = new TableFactory({
				service : WorkflowFactory.findWorkflow,
				remotePagination : false
			});

			if ($scope.data && $scope.data.idPendenciaAprovacao) {
				$scope.fluxoAprovacaoTableParams.load($scope.data.idPendenciaAprovacao);
			} else {
				$scope.fluxoAprovacaoTableParams.clear();
			}
		};

		$scope.initializeAbaFluxo();
	}
];


var ManterClienteTerritorioFluxoAprovacaoController = [
	"$scope",
	"TableFactory",
	"WorkflowFactory",
	"$stateParams" ,
	function($scope, TableFactory, WorkflowFactory , $stateParams) {
		$scope.initializeAbaFluxo = function () {

			$scope.fluxoAprovacaoTableParams = new TableFactory({
				service : WorkflowFactory.findWorkflow,
				remotePagination : false
			});		
			
			if ($scope.data && $scope.data.idPendeciaAprovacao) {
				$scope.fluxoAprovacaoTableParams.load($scope.data.idPendeciaAprovacao);				
				
			} else {
				$scope.fluxoAprovacaoTableParams.clear();
			}
		};

		$scope.initializeAbaFluxo();
	}
];

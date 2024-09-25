
var ManterReciboComplementarWorkflowController = [
  	"$scope",
	"TableFactory",
	"WorkflowFactory",
	function($scope, TableFactory, WorkflowFactory) {
		function initializeAbaWorkflow() {
			$scope.workflowTableParams = new TableFactory({
				service : WorkflowFactory.findWorkflow,
				remotePagination : false
			});
			
			if ($scope.data && $scope.data.idReciboFreteCarreteiro && $scope.data.pendencia && $scope.data.pendencia.idPendencia) {
				$scope.workflowTableParams.load($scope.data.pendencia.idPendencia);
			} else {
				$scope.workflowTableParams.clear();	
			}
		}
		
		initializeAbaWorkflow();
	}
];

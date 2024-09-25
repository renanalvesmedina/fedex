
var ManterProprietarioWorkflowController = [	
	"$scope", 
	"WorkflowFactory",
	"TableFactory",	 
	function($scope, WorkflowFactory, TableFactory) {
		function initializeAbaWorkflow() {
			$scope.workflowTableParams = new TableFactory({
				service : WorkflowFactory.findWorkflow,
				remotePagination : false
			});
			
			if ($scope.data && $scope.data.idPendencia) {
				$scope.workflowTableParams.load($scope.data.idPendencia);
			} else {
				$scope.workflowTableParams.clear();	
			}
		}
		
		initializeAbaWorkflow();
	}
];

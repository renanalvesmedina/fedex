
var ManterNotaCreditoPadraoWorkflowController = [ 
	"$scope",
	"TableFactory",
	"WorkflowFactory",
	function($scope, TableFactory, WorkflowFactory) {
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

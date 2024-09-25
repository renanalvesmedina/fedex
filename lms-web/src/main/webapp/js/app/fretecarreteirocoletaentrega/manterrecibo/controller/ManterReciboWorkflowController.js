
var ManterReciboWorkflowController = [
	"$q", 
	"$rootScope", 
	"$scope", 
	"$filter", 
	"$location", 
	"$stateParams", 
	"$state", 
	"$modal", 
	"$http",
	"ManterReciboFactory",
	"FilialFactory", 
	"WorkflowFactory",
	"UsuarioLmsFactory",
	"TableFactory", 
	"domainService", 
	"modalService", 
	function($q, $rootScope, $scope, $filter, $location, $stateParams, $state, $modal, $http, ManterReciboFactory, FilialFactory, WorkflowFactory, UsuarioLmsFactory, TableFactory, domainService, modalService) {
		function initializeAbaWorkflow() {
			$scope.workflowTableParams = new TableFactory({
				service : WorkflowFactory.findWorkflow,
				remotePagination : false
			});
			
			if ($scope.dados && $scope.dados.idPendencia) {
				$scope.workflowTableParams.load($scope.dados.idPendencia);
			} else {
				$scope.workflowTableParams.clear();	
			}
		}
		
		initializeAbaWorkflow();
	}
];


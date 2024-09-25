var ManterPropostaListController = [
	"$scope",
	"$stateParams",
	"editTabState",
	function($scope, $stateParams, editTabState) {
		
		function initializeAbaList(params){
			$scope.setEditTabState(editTabState);
			if ($scope.listTableParams.list.length > 0) {
				$scope.find();
			}
			$scope.setDisableAbaServ(true);
			$scope.setDisableAbaHist(true);
			$scope.setDisableAbaFlux(true);
		}
		
		initializeAbaList($stateParams);
		
	}
];


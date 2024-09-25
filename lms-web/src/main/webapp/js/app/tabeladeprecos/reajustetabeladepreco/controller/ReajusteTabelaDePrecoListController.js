
var ReajusteTabelaDePrecoListController = [
	"$scope" , 
	"editTabState",
	function($scope, editTabState) {

		$scope.setEditTabState(editTabState);
				
		if ($scope.listTableParams.list.length > 0) {
			$scope.listTableParams.load($scope.filterParams);
		}
    	
    	$scope.consultar = function(filterParams) {
    		$scope.listTableParams.load(filterParams);
		};
		
    }
];
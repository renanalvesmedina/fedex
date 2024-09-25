
var ManterNotaCreditoPadraoListController = [
	"$scope", 
	"$location",
	"editTabState",
 	function($scope, $location, editTabState) {
		$scope.setEditTabState(editTabState);
		
		$scope.clearFilter = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			
			$scope.loadDefaultValues();
			
			$scope.setFilter($scope.filter);
		};
		
		$scope.loadDefaultValues = function(){
			$scope.loadCurrentFilial($scope.filter);	
			$scope.filter.tpMostrarNotaZerada = {"value":"N"}; 
		};
		
		$scope.loadDefaultValues();
	}
];
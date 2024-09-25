
var ManterReciboComplementarListController = [
	"$rootScope",
	"$scope", 
	"$location",
	"editTabState",
 	function($rootScope, $scope, $location, editTabState) {
		$scope.setEditTabState(editTabState);

		$scope.loadDefaultValues = function(){			
			$scope.loadCurrentFilial($scope.filter);
		};
		
		$scope.loadDefaultValues();
		
		$scope.clearFilter = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			$scope.loadCurrentFilial($scope.filter);
			
			$scope.setFilter($scope.filter);
		};
				
		$scope.exportFullCsv = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("exportFullCsv", $scope.filter).then(function(data) {
				$rootScope.showLoading = false;
				
				if (data.fileName) {
					location.href = contextPath+"rest/report/"+data.fileName;
				}
				
			}, function() {
				$rootScope.showLoading = false;
			});
		};		
	}
];

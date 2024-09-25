var ManterTabelaMunicipioEMEXControllerCad = [
	"$scope",
	"$rootScope",
	"$stateParams",
 	function($scope, $rootScope, $stateParams) {
	
		$scope.initializeAbaCad = function(params){
			
			if (params.id){
				$rootScope.showLoading = true;
				$scope.findById(params.id).then(function(data) {
					$scope.setData(data);
					$rootScope.showLoading = false;
				},function() {
					$rootScope.showLoading = false;
				});
			}else{
				$scope.clearData();
			}
			
			
		}
		
		
		$scope.initializeAbaCad($stateParams);
	}
];

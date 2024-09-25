
var ManterTabelaMunicipioEMEXController = [
	"$scope",
	"$rootScope",
 	function($scope, $rootScope) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/tabelaMunicipioEMEX"
		});

		$scope.clearFilter = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			$scope.setFilter($scope.filter);
		};

	}
	
	
	
];

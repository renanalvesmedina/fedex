
var RelatorioGreenPODController = [
	"$scope",
	"RestAccessFactory",
	"TableFactory",
    "$rootScope",
 	function($scope, RestAccessFactory, TableFactory, $rootScope) {

        $scope.intervaloMaximoPeriodo = 30;

        $scope.setConstructor({
            rest: "/veiculoonline/relatorioGreenPOD"
        });
		
		$scope.clearFilter = function() {
			$scope.filter = {};
            $scope.listTableParams.clear();

            $scope.loadDefaultValues();

            $scope.setFilter($scope.filter);
		};
	}
];

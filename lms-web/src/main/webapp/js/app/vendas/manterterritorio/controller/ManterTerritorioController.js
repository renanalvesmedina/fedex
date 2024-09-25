var ManterTerritorioController = [
	"$scope",
 	function($scope) {
		$scope.setConstructor({
			rest: "/vendas/territorio"
		});

		$scope.selectRegionalFilter = function() {
			$scope.filter.filial = null;
		};

		$scope.$watch('filter.filial.idFilial', function() {

			if ($scope.filter.filial && $scope.filter.filial.idFilial) {

				$scope.rest.doPost("findRegionalByFilial", $scope.filter.filial.idFilial)
					.then(function(response) {
						$scope.filter.regional = response;
					})
					['finally'](function() {
						$rootScope.showLoading = false;
					});
			}
		});
	}
];

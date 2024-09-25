var ManterClienteTerritorioController = [
	"$rootScope",
	"$scope",
	"$stateParams",
 	function($rootScope, $scope, $stateParams) {
		$scope.setConstructor({
			rest: "/vendas/clienteTerritorio"
		});
		
		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		$scope.initializeAbaPesq();

		$scope.selectRegionalFilter = function() {
		}

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

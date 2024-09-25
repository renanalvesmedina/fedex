
var SimularPlanoGerenciamentoRiscoController = [
	"$rootScope",
	"$scope",
	function($rootScope, $scope) {
		$scope.setConstructor({
			rest : "/sgr/planoGerenciamentoRisco"
		});
		$scope.$watch("filter.controleCarga", function() {
			if ($scope.filter.controleCarga) {
				delete $scope.filter.filial
				delete $scope.filter.dhGeracaoInicial;
				delete $scope.filter.dhGeracaoFinal;
			}
		});
		$scope.$watch("data.id", function() {
			if (angular.isDefined($scope.data.id)) {
				$rootScope.showLoading = true;
				$scope.rest.doGet("generateEnquadramentoRegra?id=" + $scope.data.id).then(function(plano) {
					$scope.data.plano = plano;
					$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			}
		});
	}
];

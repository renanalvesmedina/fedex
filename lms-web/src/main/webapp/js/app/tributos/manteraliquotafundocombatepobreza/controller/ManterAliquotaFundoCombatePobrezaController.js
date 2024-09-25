
var ManterAliquotaFundoCombatePobrezaController = [
	"$scope",
	"$rootScope",
 	function($scope, $rootScope) {
		$scope.setConstructor({
			rest: "/tributos/manterAliquotaFundoCombatePobreza"
		});
		
		$scope.popularComboUfDestino = function(){
			$scope.rest.doPost("findUnidadeFederativaCombo").then(function(data) {
				$scope.unidadesFederativas = data.unidadesFederativas;
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		}

		$scope.popularComboUfDestino();
	}
];

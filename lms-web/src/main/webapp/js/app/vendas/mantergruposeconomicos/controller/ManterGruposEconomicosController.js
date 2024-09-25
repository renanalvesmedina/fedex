var ManterGruposEconomicosController = [
	"$scope", 
	"$stateParams",
 	function($scope, $stateParams) {
		$scope.setConstructor({
			rest: "/vendas/gruposEconomicos"
		});
	}
];

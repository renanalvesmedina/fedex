var ManterCargoComissaoController = [
	"$scope", 
 	function($scope) {
		$scope.setConstructor({
			rest: "/vendas/cargoComissao"
		});
		
		$scope.dataAtual = Utils.Date.formatMomentAsISO8601(moment());

	}
];

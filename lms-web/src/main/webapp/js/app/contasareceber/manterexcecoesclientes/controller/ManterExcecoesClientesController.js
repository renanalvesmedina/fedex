
var ManterExcecoesClientesController = [
	"$scope", 
 	function($scope) {
		$scope.setConstructor({
			rest: "/contasareceber/excecoesClientes"
		});
		
		$scope.tmp = {};
		
		$scope.setDataPadrao = function() {
			$scope.tmp.dataAlteracao = Utils.Date.formatMomentAsISO8601(moment());
		};
		
		$scope.setDataPadrao();
		
	}
];

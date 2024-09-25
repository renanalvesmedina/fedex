
var ManterDiasCorteFaturamentoController = [
	"$scope",
	"$rootScope", 
 	function($scope, $rootScope) {
		
		$scope.setConstructor({
			rest: "/contasareceber/diasCorteFaturamento"
		});
		
		
		$scope.tmp = {};
		
		$scope.setDataPadrao = function() {
			$scope.tmp.dataAlteracao = Utils.Date.formatMomentAsISO8601(moment());
		};
		
		$scope.setDataPadrao();
		
		$scope.mostrarSql = function() {
			$rootScope.showLoading = false;
			window.open(contextPath+"/rest/contasareceber/diasCorteFaturamento/mostraSQL");
		};
		
	}
];

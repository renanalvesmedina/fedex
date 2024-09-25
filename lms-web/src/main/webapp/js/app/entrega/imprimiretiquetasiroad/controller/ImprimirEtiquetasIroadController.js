var ImprimirEtiquetasIroadController = [
	"$rootScope", 
	"$scope",
	"ImpressoraFactory",
	function($rootScope, $scope, ImpressoraFactory) {
		
		$scope.setConstructor({
			rest: "/entrega/imprimirEtiquetasIroad"
		});

		$scope.find = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("find", $scope.filter).then(
				function(data) {
					$rootScope.showLoading = false;
					
					$scope.filter.codigoBarras = "";
				}, function() {
					$rootScope.showLoading = false;
					$scope.filter.codigoBarras = "";
				}
			);
		};
		
		$scope.limpar = function() {
			$scope.filter = {};
		};
		
		$scope.impressora = [];
		ImpressoraFactory.findImpressora().then(function(values) {
			$scope.impressora = values;
		});
	}
];

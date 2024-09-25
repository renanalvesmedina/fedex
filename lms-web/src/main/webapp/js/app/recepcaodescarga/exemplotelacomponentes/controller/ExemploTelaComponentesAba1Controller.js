
var ExemploTelaComponentesAba1Controller = [
	"$scope",
	"$rootScope",
	"$http",
 	function($scope, $rootScope, $http) {
		
		
		/* -------------------- Sub Abas --------------------- */

		$scope.desabilitarSubAba = function() {
			$scope.subAbaIsDesabilitada = true;
			$scope.labelSubAbaDisabilidar = "Desabilitada";
		};
		
		$scope.habilitadarSubAba = function() {
			$scope.subAbaIsDesabilitada = false;
			$scope.labelSubAbaDisabilidar = "Habilitada";
		};
		
		$scope.desabilitarSubAba();
		

		/* ------------------ Fim Sub Abas ------------------- */
		
	}
];

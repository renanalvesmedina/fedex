
var ReajusteTabelaDePrecoController = [
	"$http", 
	"$scope", 
	function($http, $scope) {
		
		$scope.setConstructor({
			rest: "/tabeladeprecos/reajustetabeladepreco"
		});
		
		$scope.filterParams = {};
		$scope.filterParams.filtros = {};
		$scope.filterParams.filtros.tabelaBase = '';
		$scope.filterParams.filtros.tabelaNova = '';
		
		$scope.limpar = function() {    			
			$scope.filterParams = {};
			$scope.filterParams.filtros = {};
			$scope.filterParams.filtros.tabelaBase = '';
			$scope.filterParams.filtros.tabelaNova = '';
		};
		
    }
];


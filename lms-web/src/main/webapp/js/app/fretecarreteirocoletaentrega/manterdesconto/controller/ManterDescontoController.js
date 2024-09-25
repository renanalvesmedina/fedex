
var ManterDescontoController = [
	"$rootScope",
	"$scope", 
	"$location", 
	"FilialFactory",
 	function($rootScope, $scope, $location, FilialFactory) {
    	
		$scope.desconto = {};
		
		$scope.setConstructor({
			rest: "/fretecarreteirocoletaentrega/descontoRfc"
		});
		
		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function(object){
			$rootScope.showLoading = true;
			
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = data;
    			}
    			
    			$scope.desconto.isMatriz = data.isMatriz;
    			
    			$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		/** Carrega dados do select one de tipos de descontos */
		$scope.populateTipoDescontoRfc = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("populateTipoDescontoRfc").then(function(data) {
				$scope.desconto.listTipoDescontoRfc = data;
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.populateTipoDescontoRfc();
	}
];

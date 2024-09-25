
var ManterNotaCreditoPadraoController = [
	"$rootScope",
	"$scope", 
	"$location", 
	"FilialFactory",
 	function($rootScope, $scope, $location, FilialFactory) {
    	
		$scope.notaCreditoPadrao = {};
		
		$scope.setConstructor({
			rest: "/fretecarreteirocoletaentrega/notaCreditoPadrao"
		});
		
		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function(object){
			$rootScope.showLoading = true;
			
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = data;
    			}
    			
    			$scope.notaCreditoPadrao.isMatriz = data.isMatriz;
    			
    			$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
	}
];

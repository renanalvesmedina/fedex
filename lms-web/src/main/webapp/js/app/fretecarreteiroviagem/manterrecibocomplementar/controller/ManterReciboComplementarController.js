
var ManterReciboComplementarController = [
	"$rootScope",
	"$scope",
	"FilialFactory", 
	"UsuarioLmsFactory",
	function($rootScope, $scope, FilialFactory, UsuarioLmsFactory) {
		$scope.setConstructor({
			rest: "/fretecarreteiroviagem/manterReciboComplementar"
		});
		
		$scope.recibo = {};
				
		$scope.findUsuarioLogado = function(){
			$rootScope.showLoading = true;
			
			if(!$scope.recibo.usuario){
				UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
					$scope.recibo.usuario = {idUsuarioLogado: data.idUsuario, nmUsuarioLogado: data.nmUsuario};
					
					$rootScope.showLoading = false;
    			},function() {
    				$rootScope.showLoading = false;
    			});		
			}
		};
						
		$scope.loadCurrentFilial = function(object){
			$rootScope.showLoading = true;
			
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = data;
    			}
    			
    			$scope.recibo.isMatriz = data.isMatriz;
    			
    			$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		/** Carrega dados do select de moedas */
		$scope.populateMoedas = function(){  
			if($scope.recibo.moedas){
				return;
			}
			
			$rootScope.showLoading = true;
			$scope.rest.doGet("populateMoedas").then(function(data) {
				$rootScope.showLoading = false;
				
				$scope.recibo.moedas = data;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.populateMoedas();
	}
];

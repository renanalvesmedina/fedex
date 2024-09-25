
var CloneTabelaDePrecoController = [
	"$scope", "$http", "$rootScope", "translateService",
 	function($scope, $http, $rootScope, translateService) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/clonetabeladepreco"
		});

		$rootScope.showLoading = false;

		$scope.loadMensagens = function(chaves) {
    		translateService.getMensagens(chaves);
    	};
    	$scope.loadMensagens("LMS-00054");
    	$scope.getMensagem = function(chave, params) {
    		return translateService.getMensagem(chave, params);
    	};
	    	
		$scope.clonarTabelaDePreco = function() {
			$rootScope.showLoading = true; 
			
			var dto = {idTabelaBase:$scope.data.tabelaBase.idTabelaPreco, idTabelaNova:$scope.data.tabelaNova.idTabelaPreco};
			$http.post(contextPath+'rest/tabeladeprecos/clonetabeladepreco/clonarTabelaPrecoETarifaPrecoRotas',dto).then(function(response){
				toastr.success($scope.getMensagem("LMS-00054"));
				$rootScope.showLoading = false; 
			}, function() {
				$rootScope.showLoading = false; 
			}); 
			
		};
	}
];

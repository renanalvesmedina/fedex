
var CloneTrtMunicipioController = [
	"$scope", "$http", "$rootScope", "translateService",
 	function($scope, $http, $rootScope, translateService) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/cloneTrtMunicipio"
		});
		

		$scope.cloneTrtMunicipio = function(){
			$rootScope.showLoading = true;
			
			if(!$scope.data.tabelaBase || !$scope.data.tabelaNova){
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-00010", ''), type: MESSAGE_SEVERITY.DANGER}]);
               	$rootScope.showLoading = false;
               	
			} else {
			
				$scope.rest.doPost("cloneTrtTabela", {tabelaBase: $scope.data.tabelaBase.idTabelaPreco, tabelaNova: $scope.data.tabelaNova.idTabelaPreco}).then(function(response) {
					$scope.showSuccessMessage();
				
				})['finally'](function(){
	               	$rootScope.showLoading = false;
	            });
				
			}
		};
	}
];

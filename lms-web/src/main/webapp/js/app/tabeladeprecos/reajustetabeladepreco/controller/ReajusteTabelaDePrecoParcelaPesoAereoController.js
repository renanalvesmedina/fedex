
(function(tabelaDePrecosModule, modalChangeValue){
	
	tabelaDePrecosModule.controller("ReajusteTabelaDePrecoParcelaPesoAereoController", [
		"$http" , 
		"$scope", 
		"$rootScope" , 
		"modalService", 
		function($http, $scope, $rootScope, modalService) {
    		
    		$scope.list = [];
    		
    		$scope.loadAbaPesoAereo = function (){
    			var idOrigem = null;
    			var idDestino = null;
    			
    			if($scope.data.ufOrigem){
    				idOrigem = $scope.data.ufOrigem.idUnidadeFederativa;
    			}
    			
    			if($scope.data.ufDestino){
    				idDestino = $scope.data.ufDestino.idUnidadeFederativa;
    			}
    			
    			$rootScope.showLoading = true; 
    			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/listParcelaPesoAereo', {idReajuste: $scope.$parent.data.id, tipoTabela: $scope.$parent.data.tipo, idOrigem: idOrigem, idDestino: idDestino }).then(function(response){
            		$scope.list = response.data;
            		$rootScope.showLoading = false;
      			
    			}, function() {
      				$rootScope.showLoading = false; 
   				});
    		};
    		
    		$scope.salvar = function (){ 
    			$rootScope.showLoading = true; 
    			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/salvarParcelaPesoAereo',  $scope.list).then(function(response){
            		toastr.success(response.data);
            		$rootScope.showLoading = false;
      			}, function() {
      				$rootScope.showLoading = false; 
   				}); 
    		};
    		
    		$scope.openModalValue = function(index, columnBaseValue, columnShowValue, columnShowPercent) {
	        	modalChangeValue(modalService, $scope.list, index, columnBaseValue, columnShowValue, columnShowPercent);
    		};
    		
    		
        }
	]);
	
})(tabelaDePrecosModule, openModalValue);
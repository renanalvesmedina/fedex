
(function(tabelaDePrecosModule, modalChangeValue){

	tabelaDePrecosModule.controller("ReajusteTabelaDePrecoParcelaPesoRodoviarioController", [
		"$http" , 
		"$scope", 
		"$rootScope" , 
		"modalService", 
		function($http, $scope, $rootScope, modalService) {
    		
    		$scope.keys = function(obj){
    			  return obj ? Object.keys(obj) : [];
    		};
    		
    		$scope.removeKey = function(item) {
    		    return item.charAt(0) !== '$';
    		};
    		
    		$scope.loadAbaPesoRodoviario = function (){
    			$rootScope.showLoading = true; 
    			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/listParcelaPesoRodoviario', $scope.$parent.data.id).then(function(response){
            		$scope.list = response.data;
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
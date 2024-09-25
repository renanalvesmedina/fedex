
(function(tabelaDePrecosModule, modalChangeValue){
	
	tabelaDePrecosModule.controller("ReajusteTabelaDePrecoParcelaAdvaloremController", [
		"$http" , 
		"$scope" , 
		"$rootScope",		
		"modalService" ,
		function($http, $scope, $rootScope, modalService) {
    		
    		$scope.loadAbaAdvalorem = function (){
    			$rootScope.showLoading = true; 
    			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/listParcelaAdvalorem',  $scope.$parent.data.id).then(function(response){
            		$scope.list = response.data;
            		$rootScope.showLoading = false;
          			
    			}, function() {
      				$rootScope.showLoading = false; 
   				});
    		};
    		
    		$scope.salvar = function (){ 
    			$rootScope.showLoading = true; 
    			$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/salvarParcelaAdvalorem',  $scope.list).then(function(response){
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

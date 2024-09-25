
(function(tabelaDePrecosModule, modalChangeValue){
	
	tabelaDePrecosModule.controller("ReajusteTabelaDePrecoParcelaFreteQuiloController", [
		"$http" ,
		"$scope" , 
		"$rootScope",
		"modalService" , 
		function($http, $scope, $rootScope, modalService) {
		
			function listagem($http, $scope, $rootScope) {
				$rootScope.showLoading = true; 
				$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/listParcelaFreteQuilo',  $scope.$parent.data.id).then(function(response){
					$scope.list = response.data;
					$rootScope.showLoading = false;
					
				}, function() {
					$rootScope.showLoading = false; 
				});
			}
			
			$scope.list = listagem($http, $scope, $rootScope);
    		
    		$scope.loadAbaFreteQuilo = function (){
    			$scope.list = listagem($http, $scope, $rootScope);
    		};
    		
    		$scope.salvarFreteQuilo = function (){ 
    			$rootScope.showLoading = true; 
            	$http.post(contextPath+'rest/tabeladeprecos/reajustetabeladepreco/salvarParcelaFreteQuilo',  $scope.list).then(function(response){
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

 


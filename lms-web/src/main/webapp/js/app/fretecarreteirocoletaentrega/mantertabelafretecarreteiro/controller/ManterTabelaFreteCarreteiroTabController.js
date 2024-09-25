
var ManterTabelaFreteCarreteiroTabController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"TableFactory",
	"$modal",
	function($rootScope, $scope, $stateParams, $state, TableFactory, $modal) {					
		$scope.tabelaValoresTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findListTabelaFcValores", remotePagination: true });
		
		$scope.tabelafretecarreteiro.currentDataType = $scope.dataType[$rootScope.currentTab.name];
		
		$scope.createPrecos = function(){
			$scope.openPrecos($scope.createItemTabelaValor($stateParams.id));
		};	
			
		$scope.openPrecos = function(tabelaFcValoresDTO){
			$rootScope.isPopup = true;
			
			var modalInstance = $scope.openGeral(tabelaFcValoresDTO);
			
			modalInstance.result.then(function(data) {}, function(data) {				
				$scope.loadCommonTableParams();
			})['finally'](function() {
				$rootScope.isPopup = false;
			});  
		};
		
		$scope.editPrecos = function(tabelaFcValoresDTO){
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("findTabelaFcValores?id="+tabelaFcValoresDTO.idTabelaFcValores).then(function(data) {							
				$rootScope.showLoading = false;
								
				$scope.openPrecos(data);
			}, function() {
				$rootScope.showLoading = false;
			});		
		};
				
		$scope.removeTabelaValoresByIds = function() {
			var ids = [];
			    			
			$.each($scope.tabelaValoresTableParams.selected, function() {				
				ids.push(this.idTabelaFcValores);
			});
			    			
			if (ids.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: "warning"}]);
			} else {    				
    			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
    				$rootScope.showLoading = true;
    				    				
    				$scope.rest.doPost("removeTabelaValoresByIds",ids).then(function(data) {    					
	    				$rootScope.showLoading = false;
	    					
    					$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
    					
    					$scope.loadCommonTableParams();
    				}, function() {
    					$rootScope.showLoading = false;
    				});
				});
			}
		};
		
		$scope.reloadTabelaFreteCarreteiroCe = function(){
			if(!$scope.data || !$scope.data.idTabelaFreteCarreteiroCe){
				$scope.findTabelaFreteCarreteiroCe($stateParams.id);
			}
			
			$scope.loadCommonTableParams();
		};
		
		$scope.loadCommonTableParams = function(type){
			$rootScope.showLoading = true;
			
			$scope.tabelaValoresTableParams.load({ idTabelaFreteCarreteiroCe: $stateParams.id, blTipo: { value: $scope.tabelafretecarreteiro.currentDataType.type } }).then(function(data) {
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.reloadTabelaFreteCarreteiroCe();		
	}
];

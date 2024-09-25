var InfoReajusteClienteController = [
	"$scope", "$http", "$rootScope", 
 	function($scope, $http, $rootScope) {
		$scope.setConstructor({
			rest: "/vendas/infoReajusteCliente"
		});
		
		$scope.modal = [ {id: "1", desc: $scope.getMensagem("rodoviario")}, {id: "2", desc: $scope.getMensagem("aereo")} ];

		$scope.reajuste = [ {id: "S", desc: $scope.getMensagem("automatico")}, {id: "N", desc: "Manual"} ];
		
		$scope.estrategico = [ {id: "S", desc: $scope.getMensagem("sim")}, {id: "N", desc: $scope.getMensagem("nao")} ];
		
		$scope.divisao = [ {id: "A", desc: "Ativo"}, {id: "I", desc: "Inativo"} ];
		
		$scope.tiposCliente = [ {id: "E", desc: "Eventual"}, 
		                        {id: "P", desc: "Potencial"}, 
		                        {id: "S", desc: "Especial"}, 
		                        {id: "F", desc: "Filial de Cliente Especial"} ];
		
		$scope.exportarReajusteCliente = function(){
			
			$rootScope.showLoading = true;
			$http.post(contextPath+'rest/vendas/infoReajusteCliente/imprimirRelatorioClientes', $scope.data).then(function(response){
				location.href = contextPath+"viewBatchReport?"+response.data.fileName;
			
			})['finally'](function(){
               	$rootScope.showLoading = false;
            });					
				
		};		
		
	}
];

var ReajusteTabelaClienteController = [
	"$scope",
	function($scope) {
		
		$scope.setConstructor({
			rest: "/vendas/reajustetabelacliente"
		});
		
		$scope.aux = {};
		$scope.tiposPessoa = [ {idPessoa: "cnpj", dsPessoa: "CNPJ"}, {idPessoa: "cpf", dsPessoa: "CPF"} ];

		$scope.$watch('aux.filial', function(data){
			$scope.filter.idFilial = null;
			if($scope.aux.filial){
				$scope.filter.idFilial = $scope.aux.filial.id;
			}
		});
		
		$scope.changePessoa = function(value) {
			$scope.filter.nrIdentificacaoEmpregador = null;
		};
		
		$scope.clear = function(){
			$scope.aux    = {};
			$scope.aux.filial = '';
			$scope.setFilter({});
		};
		
	}
];

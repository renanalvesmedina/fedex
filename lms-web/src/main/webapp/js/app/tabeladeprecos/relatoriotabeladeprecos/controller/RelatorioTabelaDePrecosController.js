
var RelatorioTabelaDePrecosController = [
	"$scope", "$http", "$rootScope", 
 	function($scope, $http, $rootScope) {
		$scope.setConstructor({
			rest: "/tabeladeprecos/relatorioTabelaDePrecos"
		});

		$scope.tipoTabela = [ {id: "T", desc: "T"}, {id: "M", desc: "M"}, {id: "A", desc: "A"}, {id: "B", desc: "B"}, {id: "C", desc: "C"}, {id: "D", desc: "D"}, {id: "E", desc: "E"}, {id: "R", desc: "R"}];
		$scope.servico = [ {id: "1", desc: $scope.getMensagem("servicoRodoviario")}, {id: "2", desc: $scope.getMensagem("servicoAereo")}, {id: "3", desc: $scope.getMensagem("servicoRodoviarioExpresso")} ];
		$scope.tipoTabelaBase = [ {id: "T", desc: "T"}, {id: "M", desc: "M"}, {id: "A", desc: "A"}, {id: "B", desc: "B"}, {id: "C", desc: "C"}, {id: "D", desc: "D"}, {id: "E", desc: "E"}, {id: "R", desc: "R"}];
		$scope.tipoServico = [ {id: "V", desc: $scope.getMensagem("tipoServicoV")},{id: "P", desc: $scope.getMensagem("tipoServicoP")},{id: "C", desc: $scope.getMensagem("tipoServicoC")} ];
		$scope.categoria =  [ {id: "T", desc: "T"},{id: "A", desc: "A"},{id: "C", desc: "C"} ];
		$scope.tipoCalculo = [ {id: "P", desc: "Valor Quilo Excedente Progressivo"},{id: "N", desc: "Volume Natura"},{id: "E", desc: "Valor Quilo Excedente"} ,{id: "Q", desc: "Valor Quilo"} ];
		$scope.tipoCalculoPedagio = [ {id: "P", desc: $scope.getMensagem("tipoCalculoPedagioP")},{id: "X", desc: $scope.getMensagem("tipoCalculoPedagioX")},{id: "F", desc: $scope.getMensagem("tipoCalculoPedagioF")},{id: "D", desc: $scope.getMensagem("tipoCalculoPedagioD")} ]; 
		$scope.efetivado =   [ {id: "S", desc: $scope.getMensagem("sim")},{id: "N", desc: $scope.getMensagem("nao")} ];
		
		$scope.gerarRelatorio = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("gerarRelatorio",  $scope.data).then(function(response) {
				location.href = contextPath+"viewBatchReport?"+response.fileName;
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
	}
];

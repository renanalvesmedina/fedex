
var InformarSaidaController = [
	"$scope", 
	"$state", 
	"$stateParams", 
	"TableFactory", 
	"InformarSaidaService", 
	"PortariaFactory", 
	"FilialFactory", 
	function ($scope, $state, $stateParams, TableFactory, InformarSaidaService, PortariaFactory, FilialFactory) {
		
		$scope.initializeAbaList = function(params) {};
		
		$scope.listViagemTableParams = new TableFactory({ resourceService: InformarSaidaService.getResource().saida, remotePagination: false });
		$scope.listViagemTableParams.load();
		
    	$scope.listColetaEntregaTableParams = new TableFactory({ resourceService: InformarSaidaService.getResource().saidaColetaEntrega, remotePagination: false });
		
		FilialFactory.findFilialUsuarioLogado().then(function(data) {
				$scope.filialLogado = data;
		}, function() {});
		
		PortariaFactory.portariasFilial().then(function (data) { 
			$scope.portarias = data.list;
			for (var i = 0; i<data.list.length; i++) {
				if (data.list[i].padrao) {
					$scope.selectedPortaria = data.list[i];
					break;
				}
			}
			$scope.listColetaEntregaTableParams.load($scope.selectedPortaria.id);
		}, function (data) {
			$scope.portarias = [];
		});
		
		$scope.changePortaria = function (data) {
			if (data == null) return;
			$scope.selectedPortaria = data;
			$scope.listColetaEntregaTableParams.load($scope.selectedPortaria.id);
		};
    }
];

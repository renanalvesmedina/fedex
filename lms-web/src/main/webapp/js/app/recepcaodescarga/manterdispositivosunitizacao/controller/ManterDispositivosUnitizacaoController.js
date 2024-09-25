
var ManterDispositivosUnitizacaoController = [
	"$scope", 
	"$location", 
	"modalService", 
	"TipoDispositivoUnitizacaoFactory", 
 	function($scope, $location, modalService, TipoDispositivoUnitizacaoFactory) {
    		
		$scope.setConstructor({
			rest: "/carregamento/dispositivoUnitizacao"
		});
		
		$scope.limpar = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
		};
		
		$scope.consultarExportar = function(){
			if("C" == $scope.filter.acao){
				$scope.find();
			} else if("E" == $scope.filter.acao){
				$scope.exportCsv();
			}
		};

		//Combos
		$scope.tiposDispositivoUnitizacao = [];
		TipoDispositivoUnitizacaoFactory.findTipoDispositivoUnitizacao().then(function(values) {
			$scope.tiposDispositivoUnitizacao = values;
		});
		
		$scope.verDispositivoPai = function() {
			$location.path("/app/manterDispositivosUnitizacao/detalhe/" + $scope.data.dispositivoUnitizacaoPai.id);
		};
		
		$scope.verMacroZona = function($event) {
			openMacroZona(modalService, $event, $scope.data.macroZona.id);
		};
	}
];

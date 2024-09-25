
var ConsultarDoctoServicoNaoFaturadoController = [
	"$rootScope",
	"$scope", 
	"TableFactory",
 	function($rootScope,$scope,TableFactory) {
		
		$scope.isFiltrosClienteOpen = true;
 		$scope.isFiltrsoDocumentoOpen = true;
 		
		$scope.setConstructor({
			rest: "/contasareceber/doctoServicoNaoFaturado"
		});
		
		$scope.limpar = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			$scope.initializeAbaPesq();
		};
		
		$scope.getFilter = function() {
			if(!$scope.filter){
				return;
			}
			
			return $scope.filter;
		};
		
		$scope.consultarExportar = function () {
			if ("C" === $scope.filter.acao) {
				delete ($scope.filter.acao);
				$scope.find();
			} else if ("E" === $scope.filter.acao) {
				delete ($scope.filter.acao);

				$rootScope.showLoading = false;
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-36419"), type: "success"}]);
				$scope.exportCsv();
			}
		};

		$scope.exportCsv = function () {
			$scope.rest.doPost("reportCsv", $scope.getFilter());
		};

		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
			$scope.filter.estadoCobranca = {'value': 'AF'};
			
			$scope.rest.doPost("carregarValoresPadrao", {}).then(function(data) {
				$scope.filter.regionais = data.regionais;
				$scope.filter.servicos = data.servicos;
				$scope.nmFilial = data.nmFilial;
				$scope.idFilial = data.idFilial;
				$scope.filter.idFilialCobranca = {sgFilial:data.sgFilial,nmFilial:data.nmFilial,idFilial:data.idFilial};
				$scope.filter.idRegional= data.idRegional;
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		$scope.initializeAbaPesq();
	}
];

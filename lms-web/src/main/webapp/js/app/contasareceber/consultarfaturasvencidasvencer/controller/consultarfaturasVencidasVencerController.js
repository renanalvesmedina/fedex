
var consultarfaturasVencidasVencerController = [
	"$rootScope",
	"$scope", 
	"TableFactory",
	function($rootScope,$scope,TableFactory) {
		
		$scope.isFiltrosClienteOpen = true;
 		$scope.isFiltrsoDocumentoOpen = true;
 		$scope.isFiltrosFaturaOpen = true;
 		
		$scope.setConstructor({
			rest: "/contasareceber/faturasVencidasVencer"
		});
		
		$scope.mostrarSql = function() {
			var formData = new FormData();
			formData.append("data", angular.toJson($scope.getFilter()));
			$scope.rest.doPostMultipart("mostraSQL", formData).then(
					function(response) {
						$("#devedoresListar").val(response.sql);
					}, function(erro) {
						// tratamento de erro
					})['finally'](function() {
			});
		};
		
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
		
		$scope.initializeAbaPesq = function () {
			$rootScope.showLoading = true;
	
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

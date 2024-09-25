
var GerarNotaCreditoPadraoController = [
	"$rootScope",
	"$scope", 
	"$location", 
	"modalService",
	"FilialFactory",
 	function($rootScope, $scope, $location, modalService, FilialFactory) {
    	
		$scope.gerarNotaCreditoPadrao = {};
		
		$scope.setConstructor({
			rest: "/fretecarreteirocoletaentrega/gerarNotaCreditoPadrao"
		});
		
		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function(object){
			$rootScope.showLoading = true;
			
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = data;
    			}
    			
    			$scope.gerarNotaCreditoPadrao.isMatriz = data.isMatriz;
    			
    			$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.gerarNotaCredito = function(id) {
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("gerar?id=" + id + "&tpGerarNotaCredito=" + $scope.filter.tpGerarNotaCredito.value).then(function(data) {
				$rootScope.showLoading = false;
								
				if(data && data.idNotaCredito){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25122"), windowClass: 'modal-confirm'})
					.then(function() {
						$scope.emitirNotaCredito(data.idNotaCredito);
					}, function() {
						$scope.find();
					});
				}else{
					if(data && data.proprio){
						$scope.find();
					}
				}
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.emitirNotaCredito = function(idNotaCredito) {			
			$rootScope.showLoading = true;
						
			$scope.rest.doGet("emitir?id=" + idNotaCredito).then(function(data) {
				$rootScope.showLoading = false;
				
				if(data && data.fileName){
					location.href = contextPath+"/viewBatchReport?"+data.fileName;	
				}
				
				$scope.find();
			},function() {
				$rootScope.showLoading = false;
			});			
		};
		
		$scope.clearFilter = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			
			$scope.setFilter($scope.filter);
			
			$scope.initializeAbaGerar();
		};
		
		$scope.initializeAbaGerar = function(){
			$scope.loadCurrentFilial($scope.filter);
			
			$scope.gerarNotaCreditoPadrao.dtAtual = Utils.Date.formatMomentAsISO8601(moment().subtract('days', 15));
		};
		
		$scope.initializeAbaGerar();
	}
];

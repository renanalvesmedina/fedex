
var ManterProprietarioListController = [
	"$rootScope",
	"$scope",
	"$location",
	function($rootScope,$scope,$location) {
		
		$scope.consultar = function() {
			$scope.consultando = true;
			$scope.listTableParams.load($scope.getFilter(false));
		};

		$scope.limpar = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			$scope.anexosTableParams.clear();
			$scope.loadCurrentFilial($scope.filter);
		};

		$scope.abreDetalhe = function(o) {
			$location.path("/app/manterProprietario/detalhe/" + o.idProprietario);
		};
		
		/**
		 * Remove item(s) da listagem de proprietarios.
		 */
		$scope.removeProprietariosByIds = function() {
			var ids = [];
			$.each($scope.listTableParams.selected, function() {
					ids.push(this.idProprietario);
			});
			
			if (ids.length==0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: MESSAGE_SEVERITY.WARNING}]);
			} else {
	    			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
	    				$scope.excluindo = true;
	    				$scope.rest.doPost("removeProprietariosByIds", ids).then(function(data) {
    					$scope.excluindo = false;
    					$scope.showSuccessMessage();
    					$scope.listTableParams.load($scope.getFilter(false));
    				}, function() {
    					$scope.excluindo = false;
    				});        				
				});
			}    			
		};
		
		$scope.exportCsv = function() {
			$rootScope.showLoading = true;
			$scope.rest.doPost("reportCsv", $scope.getFilter()).then(function(data) {
				$rootScope.showLoading = false;
				if (data.fileName) {
					location.href = contextPath+"rest/report/"+data.fileName;
				}
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.getFilter = function() {
			if(!$scope.filter){
				return;
			}
			
			var o = {};			    			
			o.filtros = $scope.filter;
			    			
			return o;
		};
		
		/** Processa inicializacao da aba listagem */
		$scope.initializeAbaList = function() {
			$scope.loadCurrentFilial($scope.filter);    			
		};
		
		$scope.initializeAbaList();
	}
];


var manterConhecimentosNaoComissionaveisController = [
	"$rootScope",
	"$scope",
	"$stateParams",	
 	function($rootScope, $scope, $stateParams) {
		
		$scope.setConstructor({
			rest: "/vendas/conhecimentosNaoComissionaveis", remotePagination: true
		});

		$scope.conhecimentosNaoComissionaveis = { };
		$scope.naoComissionaveisList = [];
		$scope.comissionaveisList = [];
		
		$scope.verifyChanges = function() {
			$scope.salvarAlteracoes();
	    	return true;
	    };

		function remove(list, id) {
			if (list.length > 0) {
				var idx = list.indexOf(id);
				if (idx > -1) {
					list.splice(idx, 1);
				}
			}
		}

		$scope.salvarAlteracoes = function() {
			console.info("salvarNaoComissionaveis");
			
			if (($scope.naoComissionaveisList.length > 0) || ($scope.comissionaveisList.length > 0)) {
			
				$scope.conhecimentosNaoComissionaveis = { };
				$scope.conhecimentosNaoComissionaveis.naoComissionaveisList = $scope.naoComissionaveisList;
				$scope.conhecimentosNaoComissionaveis.comissionaveisList = $scope.comissionaveisList;
				$scope.conhecimentosNaoComissionaveis.dataCompetencia = $scope.filter.dtInicio;
				$scope.conhecimentosNaoComissionaveis.idExecutivo = $scope.filter.executivo.idUsuario;

				$rootScope.showLoading = true;				
				$scope.rest.doPost("salvarNaoComissionaveis", $scope.conhecimentosNaoComissionaveis).then(function(response) {
					
					$scope.naoComissionaveisList = [];
					$scope.comissionaveisList = [];
					toastr.success("Sucesso");
					
				}, function() {
					$scope.data = {};
	  				$rootScope.showLoading = false; 
				})['finally'](function(){
					$scope.data = {};
	               	$rootScope.showLoading = false;
	            });
			}
		};
		
		$scope.checkUncheckLocalRow = function(row) {
			remove($scope.naoComissionaveisList, row.id);
			remove($scope.comissionaveisList, row.id);
			if (row.checked) {
				if (!row.naoComissionavel) {
					$scope.naoComissionaveisList.push(row.id);
					$scope.conhecimentosNaoComissionaveis.idExecutivo = row.idExecutivo;
				}
			} else if (row.naoComissionavel) {
				$scope.comissionaveisList.push(row.id);
			}
		};
		
		$scope.dataAtual = Utils.Date.formatMomentAsISO8601(moment());
	}
];

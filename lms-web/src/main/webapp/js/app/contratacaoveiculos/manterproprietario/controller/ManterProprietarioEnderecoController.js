
var ManterProprietarioEnderecoController = [
	"$scope",
	"$rootScope",
	"$modal",
	function($scope, $rootScope, $modal) {
		
		/** Abre a tela para visualizar/editar o endereco da grid. */
		$scope.openModalEnderecoPessoa = function(id, novo){	
			var idPessoa = $scope.data.idPessoa;
			
			$rootScope.isPopup = true;
			var modalInstance = $modal.open(
					{
					controller: ManterEnderecoPessoaPopupController,
					templateUrl: contextPath+"js/common/modal/view/modal-template.html",
					windowClass: "modal-detail",
					resolve: {
		    			modalParams: function() {		    				
	        		    	return {novo: novo, id: id, idPessoa: idPessoa };		        		
		    			}
		    		}}
				);    
			
			modalInstance.result.then(
				function() {					
				}, function() {
					var params = {};
					params.filtros = {};
					params.filtros.idPessoa = $scope.data.idPessoa;
					
					$scope.enderecoPessoaTableParams.load(params);
				}
			)['finally'](function() {
				$rootScope.isPopup = false;
			});
		};
		
		/** Processa inicializacao da aba de endereco */
		$scope.initializeAbaEndereco = function() {

			if ($scope.data && $scope.data.idPessoa) {
				var o = {};
    			o.filtros = {};
    			o.filtros.idPessoa = $scope.data.idPessoa;
				
				$scope.enderecoPessoaTableParams.load(o).then(function() {}, function() {});
			} else {
				$scope.enderecoPessoaTableParams.clear();	
			}
		};
		
		$scope.initializeAbaEndereco();
	}
];


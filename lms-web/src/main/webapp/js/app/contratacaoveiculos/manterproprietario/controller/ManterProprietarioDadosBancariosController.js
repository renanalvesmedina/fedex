
var ManterProprietarioDadosBancariosController = [
	"$scope",
	"$rootScope",
	"$modal",
	function($scope,$rootScope, $modal) {
		
		/** Abre a tela para visualizar/editar a conta bancaria da grid. */
		$scope.openModalContaBancaria = function(id, novo){    	
			var nmPessoa = $scope.data.nmPessoa;
			var nrIdentificacao = $scope.data.nrIdentificacao; 
			var tpIdentificacao = $scope.data.tpIdentificacao.value;		
			var idPessoa = $scope.data.idPessoa;    	
			$rootScope.isPopup = true;
			
			var modalInstance = $modal.open(
					{
					controller: ManterDadosBancariosPopupController,
					templateUrl: contextPath+"js/common/modal/view/modal-template.html",
					windowClass: "modal-detail",
					resolve: {
		    			modalParams: function() {		    				
	        		    	return {novo: novo, id: id, idPessoa: idPessoa, nmPessoa: nmPessoa ,nrIdentificacao:nrIdentificacao ,tpIdentificacao:tpIdentificacao };		        		
		    			}
		    		}}
				);    
			
			modalInstance.result.then(
				function() {
				}, function() {
					var params = {};
					params.filtros = {};
					params.filtros.idPessoa = $scope.data.idPessoa;
					
					$scope.contaBancariaTableParams.load(params);
				}
				)['finally'](function() {
					$rootScope.isPopup = false;
				});
		};
		
		/** Processa inicializacao da aba de dados bancarios */
		$scope.initializeAbaDadosBancarios = function() {
							
			if ($scope.data && $scope.data.idPessoa) {
				var o = {};
    			o.filtros = {};
    			o.filtros.idPessoa = $scope.data.idPessoa;
				
				$scope.contaBancariaTableParams.load(o).then(function() {}, function() {});
			} else {
				$scope.contaBancariaTableParams.clear();	
			}
		};
		
		$scope.initializeAbaDadosBancarios();
	}
];


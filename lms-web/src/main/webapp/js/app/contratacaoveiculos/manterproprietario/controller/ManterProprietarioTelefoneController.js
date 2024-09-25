
var ManterProprietarioTelefoneController = [
	"$scope",
	"$rootScope",
	"$modal",
	function($scope,$rootScope,$modal) {

		/** Abre a tela para visualizar/editar o telefone da grid. */
		$scope.openModalTelefonePessoa = function(id, novo){    	
			var nmPessoa = $scope.data.nmPessoa;
			var nrIdentificacao = $scope.data.nrIdentificacao; 
			var tpIdentificacao = $scope.data.tpIdentificacao.value;		
			var idPessoa = $scope.data.idPessoa;  

			$rootScope.isPopup = true;
			
			
			var modalInstance = $modal.open(
					{
					controller: ManterTelefonePessoaPopupController,
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
						
						$scope.telefonePessoaTableParams.load(params);
					}
					)['finally'](function() {
				$rootScope.isPopup = false;
			});
		};
		
		/** Processa inicializacao da aba de contato */
		$scope.initializeAbaTelefone = function() {
			if ($scope.data && $scope.data.idPessoa) {
				var o = {};
    			o.filtros = {};
    			o.filtros.idPessoa = $scope.data.idPessoa;
				
				$scope.telefonePessoaTableParams.load(o).then(function() {}, function() {});
			} else {
				$scope.telefonePessoaTableParams.clear();	
			}
		};
		
		$scope.initializeAbaTelefone();
	}
];


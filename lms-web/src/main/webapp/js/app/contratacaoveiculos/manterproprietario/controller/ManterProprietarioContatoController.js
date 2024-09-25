
var ManterProprietarioContatoController = [
	"$scope",
	"$rootScope",
	"$modal",
	function($scope,$rootScope, $modal) {
		
		/** Abre a tela para visualizar/editar o endereco da grid. */
		$scope.openModalContatoPessoa = function(id, novo){    	
			var nmPessoa = $scope.data.nmPessoa;
			var nrIdentificacao = $scope.data.nrIdentificacao; 
			var tpIdentificacao = $scope.data.tpIdentificacao.value;		
			var idPessoa = $scope.data.idPessoa;    
			$rootScope.isPopup = true;
			
			var modalInstance = $modal.open(
					{
					controller: ManterContatoPessoaPopupController,
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
						
						$scope.contatoPessoaTableParams.load(params);
					}
			)['finally'](function() {
				$rootScope.isPopup = false;
			});
		};
		
		/** Processa inicializacao da aba de contato */
		$scope.initializeAbaContato = function() {
			if ($scope.data && $scope.data.idPessoa) {
				var o = {};
    			o.filtros = {};
    			o.filtros.idPessoa = $scope.data.idPessoa;
				
				$scope.contatoPessoaTableParams.load(o).then(function() {}, function() {});
			} else {
				$scope.contatoPessoaTableParams.clear();	
			}
		};
		
		$scope.initializeAbaContato();
	}
];


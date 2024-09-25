
var ManterTelefonePessoaPopupController = [
	"$q",
	"$rootScope",
	"$scope",
	"$controller", 
	"ManterTelefonePessoaFactory",
	"ManterEnderecoPessoaFactory",
	"$modalInstance",
	"modalParams", 
	function($q, $rootScope, $scope, $controller, ManterTelefonePessoaFactory, ManterEnderecoPessoaFactory, $modalInstance, modalParams) {    		
		
		// Inicializa a super classe ManterTelefonePessoaController.
		angular.extend(this, $controller(ManterTelefonePessoaController, {$scope: $scope}));
		    	
		$scope.popup = true;
		    		
		$scope.title = $scope.getMensagem("telefone");
		$scope.innerTemplate = contextPath+"js/app/configuracoes/mantertelefonepessoa/view/manterTelefonePessoaCad.html";
		$scope.modalParams = modalParams;
		
 		$scope.enderecos = {};
 		
 		$scope.findEnderecos =function(idPessoa){
 			var o = {};
			o.filtros = {};
			o.filtros.idPessoa = idPessoa;
			
			ManterEnderecoPessoaFactory.find(o).then(function(data) {	    		    		
 				 $scope.enderecos = data.list;
  			}, function() {
  				
  			});
 		};
		    		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");				
		};
		    		
		$scope.limparDados = function() {    			
			var idPessoa 		= $scope.dados.idPessoa;
			var nmPessoa 		= $scope.dados.nmPessoa; 
			var nrIdentificacao = $scope.dados.nrIdentificacao; 
			var tpIdentificacao = $scope.dados.tpIdentificacao;		
				
			$scope.dados = {};
			$scope.dados.idPessoa = idPessoa;
			$scope.dados.nmPessoa = nmPessoa;
			$scope.dados.nrIdentificacao = nrIdentificacao; 
			$scope.dados.tpIdentificacao = tpIdentificacao;
		};
		
		/** Inicializa a modal para criar um novo registro */
		$scope.create = function(modalParams){
			$scope.dados = {};
			$scope.dados.idPessoa = modalParams.idPessoa;
			$scope.dados.nmPessoa = modalParams.nmPessoa;
			$scope.dados.nrIdentificacao = modalParams.nrIdentificacao; 
			$scope.dados.tpIdentificacao = modalParams.tpIdentificacao;
			
			$scope.findEnderecos(modalParams.idPessoa);
		};			
		
		/** Inicializa a modal para visualizar/editar um registro */
		$scope.find = function(id){		
			var deferred = $q.defer();
			
			if(!id){
				deferred.reject();
				
				return deferred.promise;
			}
			$rootScope.showLoading = true;
			ManterTelefonePessoaFactory.findById(id).then(function(data) {	    		    		
				$scope.dados = data;
				$scope.enderecos = data.enderecos;
				
				deferred.resolve();
				$rootScope.showLoading = false;					
			}, function() {
				$rootScope.showLoading = false;
				deferred.reject();
			});
			
			return deferred.promise;			
		};	
		
		/** Define comportamento da modal */
		if(modalParams.novo){	
			$scope.create($scope.modalParams);
		} else {
			$scope.find($scope.modalParams.id);
		}
	}
];

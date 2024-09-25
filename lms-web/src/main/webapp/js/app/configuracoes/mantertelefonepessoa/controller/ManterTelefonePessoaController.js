
var ManterTelefonePessoaController = [
	"$scope", 
	"ManterTelefonePessoaFactory",
	function($scope, ManterTelefonePessoaFactory) {
    	
		$scope.popup = false;
		
		$scope.contatoPessoa = {};
		
		/** Inicializa expansao das sub-abas */
 		$scope.isTelefoneOpen = true;
 		$scope.isContatoOpen = true;
 		$scope.isEnderecoOpen = true;
 					
		/** Processa inicializacao da aba cadastro */
		$scope.initializeAbaCad = function(params) {
			
		};
		    
		/** Executa store do contato */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$scope.salvando = true;
			
			ManterTelefonePessoaFactory.store($scope.getBean()).then(function(data) {
				$scope.processStore(data);
				
				$scope.salvando = false;
			},function() {
				$scope.salvando = false;
			});			
		};
		    				
		/** Processa resultados do detalhamento */
		$scope.processFind = function(data){				  
    		$scope.dados = data;	    		
		};
		
		/** Processa resultados de inserir/atualizar */
		$scope.processStore = function(data){				    			
			$scope.dados = data;
			$scope.showSuccessMessage();
		};
		
		$scope.limparDados = function() {    			
			$scope.dados = {};    			
		};
		    			
		$scope.getBean = function() {
			return $scope.dados;
		};
		
		$scope.removeTelefonePessoaById = function() {
			var id = $scope.dados.idTelefone;
			    			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
    			$scope.excluindo = true;
    			ManterTelefonePessoaFactory.removeTelefonePessoaById(id).then(function(data) {
					$scope.excluindo = false;
					$scope.showSuccessMessage();
					$scope.limparDados();
				}, function() {
					$scope.excluindo = false;
				});        				
			});  			
		};
	}
];


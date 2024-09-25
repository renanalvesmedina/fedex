
var ManterContatoPessoaController = [
	"$scope", 
	"ManterContatoPessoaFactory", 
	function($scope, ManterContatoPessoaFactory) {
		
		$scope.popup = false;
		
		$scope.contatoPessoa = {};
		
		/** Inicializa expansao das sub-abas */
 		$scope.isContatoOpen = true;
 		$scope.isTelefoneOpen = true;
		
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
			
			ManterContatoPessoaFactory.store($scope.dados).then(function(data) {
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
		        		
		$scope.loadDefaultValues = function(){
			$scope.contatoPessoa.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
		};
		
		/**
		 * Remove contato pessoa.
		 */
		$scope.removeContatoPessoaById = function() {
			var id = $scope.dados.idContato;
			    			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
    			$scope.excluindo = true;
    			ManterContatoPessoaFactory.removeContatoPessoaById(id).then(function(data) {
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



var ManterDadosBancariosController = [
	"$scope",
	"$locale", 
	"ManterDadosBancariosFactory", 
	function($scope, $locale, ManterDadosBancariosFactory) {
		
		$scope.popup = false;
		
		$scope.dados = {};
		$scope.dadosBancarios = {};
		
		/** Inicializa expansao das sub-abas */
 		$scope.isDadosBancariosOpen = true;
 		$scope.isVigenciaOpen = true;
		
		/** Processa inicializacao da aba cadastro */
		$scope.initializeAbaCad = function(params) {
			
		};
		
		$scope.changeBanco = function() {    			
			$scope.dados.agenciaBancaria = null;   			
		};
		
		/** Executa store do endereco */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$scope.salvando = true;
			
			ManterDadosBancariosFactory.store($scope.getBean()).then(function(data) {
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
			var o = $scope.dados;
			
			o.dtVigenciaInicial =o.dtVigenciaInicial;
			o.dtVigenciaFinal = o.dtVigenciaFinal;
			
			return o;
		}; 
		
		/**
		 * Remove conta bancaria.
		 */
		$scope.removeDadosBancariosById = function() {
			var id = $scope.dados.idContaBancaria;
			    			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
    			$scope.excluindo = true;
    			ManterDadosBancariosFactory.removeDadosBancariosById(id).then(function(data) {
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


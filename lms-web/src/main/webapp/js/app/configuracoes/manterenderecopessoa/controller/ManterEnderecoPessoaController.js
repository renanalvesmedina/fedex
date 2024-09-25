
var ManterEnderecoPessoaController = [
	"$rootScope",
	"$q", 
	"$scope", 
	"$locale", 
	"ManterEnderecoPessoaFactory", 
	"ManterPaisFactory", 
	"domainService", 
	"modalService", 
	function($rootScope, $q, $scope, $locale, ManterEnderecoPessoaFactory, ManterPaisFactory, domainService, modalService) {	
		$scope.enderecoPessoa = {};
		$scope.enderecoPessoa.isTpEnderecoDuplicado = false;
		
		/** Inicializa expansao das sub-abas */
 		$scope.isEnderecoOpen = true;
 		$scope.isVigenciaOpen = true;
 		$scope.isAlteracaoOpen = true;
 				  
		/** Inicializa a modal para visualizar/editar um registro */
		$scope.find = function(id){
			$rootScope.showLoading = true;
			
			ManterEnderecoPessoaFactory.findById(id).then(function(data) {	    		    		
				$scope.dados = data;
				
				$scope.loadTpEndereco($scope.dados.tpPessoa);
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.populateTipoLogradouro = function(){				  
			ManterEnderecoPessoaFactory.populateTipoLogradouro().then(function(data) {
				$scope.enderecoPessoa.tpLogradouro = data;
			},function() {
				
			});	
		};
		
		$scope.loadTpEndereco = function(tpPessoa){
			ManterEnderecoPessoaFactory.loadTpEndereco(tpPessoa).then(function(data) {
				$scope.enderecoPessoa.tpEndereco = data;
			},function() {
				
			});
		};
		
		$scope.populateTipoLogradouro();
		
		/** Executa store do endereco */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$rootScope.showLoading = true;
			
			ManterEnderecoPessoaFactory.store($scope.getBean()).then(function(data) {
				$scope.processStore(data);
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});			
		};
		    				
		/** Processa resultados do detalhamento */
		$scope.processFind = function(data){				  
    		$scope.dados = data;
    		
    		$scope.enderecoPessoa.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
    		
    		if($scope.dados.mensagemSubstituicao){
    			modalService.open({title: $scope.getMensagem("aviso"), message: $scope.dados.mensagemSubstituicao});	   
    		}
		};
		
		/** Processa resultados de inserir/atualizar */
		$scope.processStore = function(data){				    			
			$scope.dados = data;
			
			$scope.enderecoPessoa.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
			
			$scope.showSuccessMessage();	
		};
		
		$scope.findCep = function(){
			var cep = $scope.dados.nrCep;

			if(!cep){
				return;
			}
			
			var pais = $scope.dados.pais;
			
			if(pais){
    			if(pais.sgPais == 'BRA' && cep.length < 5){
    				$scope.dados.nrCep = null;
    				
    				modalService.open({title: $scope.getMensagem("aviso"), message: $scope.getMensagem("LMS-29173")});	   
    				
					return;
    			}
			}
				
			$rootScope.showLoading = true;
			
			ManterEnderecoPessoaFactory.findCep(cep).then(function(data) {
				$rootScope.showLoading = false;
									
				$scope.dados.nrEndereco = null;
				$scope.dados.dsComplemento = null;
				$scope.dados.nrLatitude = null;
				$scope.dados.nrLongitude = null;
				
				$scope.dados.uf = data.uf;
				$scope.dados.municipio = data.municipio;    					
				$scope.dados.dsEndereco = data.dsEndereco;
				$scope.dados.dsBairro = data.dsBairro;
				$scope.dados.tpLogradouro = data.tpLogradouro;
				
				if(data.pais){
					$scope.dados.pais = data.pais;
				}					
			},function() {
				$rootScope.showLoading = false;
			});      			    				
		   			
		};

		/**
		 * Por padrao sempre havera um idPessoa vinculado ao endereco.
		 */
		$scope.limparDados = function() {
			$scope.createEnderecoPessoa($scope.dados.idPessoa);
		};
		    			
		$scope.getBean = function() {
			var o = $scope.dados;
			
			if($scope.dados.substituirEndereco && $scope.dados.enderecoPessoaVigente){
				o.idEnderecoPessoaSubstituido = $scope.dados.enderecoPessoaVigente.idEnderecoPessoa;
			}
			
			return o;
		}; 
		
		/**
		 * Remove endereco pessoa.
		 */
		$scope.removeEnderecoPessoaById = function() {
			var id = $scope.dados.idEnderecoPessoa;
			    			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
    			$scope.excluindo = true;
    			ManterEnderecoPessoaFactory.removeEnderecoPessoaById(id).then(function(data) {
					$scope.excluindo = false;
					$scope.showSuccessMessage();
					$scope.limparDados();
				}, function() {
					$scope.excluindo = false;
				});        				
			});  			
		};
		
		$scope.changePais = function(){
			$scope.dados.uf = null;
			$scope.dados.nrCep = null;
			
			$scope.changeUnidadeFederativa();
		};
		
		$scope.changeUnidadeFederativa = function(){
			$scope.dados.municipio = null;
		}; 
		
		$scope.changeLatitude = function(){
			var nrLatitude = $scope.dados.nrLatitude;
			
			if(!angular.isUndefined(nrLatitude)){
				var er = new RegExp("^(\-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
		        if (!er.test(nrLatitude))  {
		        	$scope.dados.nrLatitude = null;
		        }
		        
		        if (nrLatitude > 90 || nrLatitude < -90){
		        	$scope.dados.nrLatitude = null;
				} 
			}
		}; 
		
		$scope.changeLatitudeTmp = function(){
			var nrLatitudeTmp = $scope.dados.nrLatitudeTmp;
			
			if(!angular.isUndefined(nrLatitudeTmp)){
				var er = new RegExp("^(\-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
				if (!er.test(nrLatitudeTmp))  {
					$scope.dados.nrLatitudeTmp = null;
				}
				
				if (nrLatitudeTmp > 90 || nrLatitudeTmp < -90){
					$scope.dados.nrLatitudeTmp = null;
				} 
			}
		}; 
		
		$scope.changeLongitude = function(){
			var nrLongitude = $scope.dados.nrLongitude;
			
			if(!angular.isUndefined(nrLongitude)){
				var er = new RegExp("^(\-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
		        if (!er.test(nrLongitude))  {
		        	$scope.dados.nrLongitude = null;
		        }
		        
		        if (nrLongitude > 90 || nrLongitude < -90){
		        	$scope.dados.nrLongitude = null;
				} 
			}
		}; 
		
		$scope.changeLongitudeTmp = function(){
			var nrLongitudeTmp = $scope.dados.nrLongitudeTmp;
			
			if(!angular.isUndefined(nrLongitudeTmp)){
				var er = new RegExp("^(\-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:(\,|.)[0-9]{1,18})?))$");  
				if (!er.test(nrLongitudeTmp))  {
					$scope.dados.nrLongitudeTmp = null;
				}
				
				if (nrLongitudeTmp > 90 || nrLongitudeTmp < -90){
					$scope.dados.nrLongitudeTmp = null;
				} 
			}
		}; 
		
		$scope.changePreencherQualidadePadrao = function() {
			var nrLatitudeTmp = $scope.dados.nrLatitudeTmp;
			var nrLongitudeTmp = $scope.dados.nrLongitudeTmp;
			
			if ((nrLatitudeTmp != null && nrLatitudeTmp != "") && (nrLongitudeTmp != null && nrLongitudeTmp != "")) {
				$scope.dados.qualidade = 1;
			}
		};
		
		/**
		 * Pesquisa se existe algum endereco que pode ser substituido.
		 * 
		 */
		$scope.findEnderecoPessoaVigente = function(){
			var dtVigenciaInicial = $scope.dados.dtVigenciaInicial;
			
			if(angular.isUndefined(dtVigenciaInicial)){
				return;
			}			
			
			var bean = {};
			bean.idPessoa = $scope.dados.idPessoa;
			bean.idEnderecoPessoa = $scope.dados.idEnderecoPessoa;
			bean.dtVigenciaInicial = dtVigenciaInicial;
			
			$rootScope.showLoading = true;
			
			ManterEnderecoPessoaFactory.findEnderecoPessoaVigente(bean).then(function(data) {
				$scope.dados.enderecoPessoaVigente = data;
				$scope.dados.substituirEndereco = null;
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});	
		};
		
		/**
		 * Pesquisa se existe algum endereco que pode ser substituido.
		 * 
		 */
		$scope.createEnderecoPessoa = function(idPessoa){			
			$rootScope.showLoading = true;
			
			ManterEnderecoPessoaFactory.createEnderecoPessoa(idPessoa).then(function(data) {
				$scope.dados = data;
				
				$scope.loadTpEndereco($scope.dados.tpPessoa);
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});	
		};
		
	}
];

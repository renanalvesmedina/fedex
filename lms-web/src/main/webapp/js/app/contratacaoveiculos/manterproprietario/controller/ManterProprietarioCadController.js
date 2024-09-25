
var ManterProprietarioCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"$http",
	"UsuarioLmsFactory",
	"ManterPaisFactory",
	"modalService",
	function($rootScope, $scope, $stateParams, $state, $http, UsuarioLmsFactory, ManterPaisFactory, modalService) {
		
		$scope.fileUploadParams = {};
				
		/** Inicializa expansao das sub-abas */
		$scope.isOutrasInformacoesOpen = true;
		$scope.isTelefonePrincipalOpen = true;
		$scope.isInformacoesDocumentosOpen = true;
		$scope.isInformacoesPagamento = true;
		$scope.isBloqueioLiberacaoOpen = true;
		$scope.isESocial = true;
		$scope.isVigenciaOpen = true;
		$scope.isInformacoesOpen = true;
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			var o = {};
			o.filtros = {};    			
			o.filtros.idProprietario = $scope.data.idProprietario;
			o.report = false;
			
			$scope.anexosTableParams.load(o).then(function(data) {
				if(!$scope.anexosTableParams.list){
        			$scope.anexosTableParams.list = [];   
        			$scope.anexosTableParams.qtdRegistros = 0;
    			}
				
				$scope.loadNewFiles();
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});    			    			
		};
		
		$scope.resetUpload = function(){
			$scope.proprietario.files = [];
			$scope.proprietario.bytea = [];
			$scope.proprietario.descricao = null;
		};    		
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.proprietario.descricao) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-27117"), type: MESSAGE_SEVERITY.WARNING}]);    				
				return false;
			}
			
			if (angular.isUndefined($scope.fileUploadParams) 
					|| angular.isUndefined($scope.fileUploadParams.selectedFiles)
						|| $scope.fileUploadParams.selectedFiles.length == 0){    				
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-27118"), type: MESSAGE_SEVERITY.WARNING}]);
				return false;
			}
		
			return true;
		};
		
		/**
		 * Adiciona um novo arquivo a grid.
		 */
		$scope.addFile = function(){
			if(!$scope.isFileUpload()){
				return false;
			}
			    			
			var selectedFile = $scope.fileUploadParams.selectedFiles[0];
			
			/*
			 * Cria um item para adicionar a grid. 
			 */
			var file = {};
			file.dsAnexo = $scope.proprietario.descricao;
			file.nmUsuario = $scope.data.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());
			
			/*
			 * Mantem no scope o array de dados para ser enviado quando o
			 * usuario salvar o registro.
			 */
			$scope.proprietario.files.push(file);
			$scope.proprietario.bytea.push(selectedFile);
			   
			/*
			 * Zera configuracoes do componente. 
			 */
			$scope.fileUploadParams.clear();
			$scope.proprietario.descricao = null;
			
			/*
			 * Consulta itens no banco, para atualizar a tabela 
			 * e conseguir adicionar novos itens.
			 * 
			 * Solucao encontrada para forcar atualizacao do componente table.
			 */
			$scope.loadAnexosTableParams();
		};
		
		
		/**
		 * Adiciona novos arquivos a grid.
		 */
		$scope.loadNewFiles = function(){
			if($scope.proprietario.files){    				
				$.each($scope.proprietario.files, function(){
					$scope.anexosTableParams.list.unshift(this);
				});
				
				$scope.anexosTableParams.qtdRegistros += $scope.proprietario.files.length;
			}
		};
				
		$scope.downloadFile = function(id) {
			$scope.downloading = true;
			
			$scope.rest.doGet("findAnexoById?id="+id).then(function(data) {
				$scope.downloading = false;
				
				if(data != null){
					var params = "";
					params += "table="+ data.table;
					params += "&blobColumn="+ data.blobColumn;
					params += "&idColumn="+ data.idColumn;
					params += "&id="+ data.id;

					window.open(contextPath+'attachment?' + params);
				}
			});
		};
		
		$scope.removeAnexosByIds = function() {
			var ids = [];
			var tmp = [];
			    			
			$.each($scope.anexosTableParams.selected, function() {
				if(!this.idAnexoProprietario){    					
					tmp.push($scope.proprietario.files.indexOf(this));    					
				} else {
					ids.push(this.idAnexoProprietario);	
				}
			});
			    			
			if (ids.length===0 && tmp.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: MESSAGE_SEVERITY.WARNING}]);
			} else {    				
	    			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
	    				$scope.excluindo = true;
	    				
	    				/*
	    				 * Remove itens temporarios.
	    				 */
	    				$.each(tmp, function(){
	    					$scope.proprietario.files.splice(this, 1);
	    					$scope.proprietario.bytea.splice(this, 1);
	    				});
	    				
	    				$scope.rest.doPost("removeAnexoProprietarioByIds",ids).then(function(data) {
    					$scope.excluindo = false;
    					$scope.showSuccessMessage();
    					
    					$scope.loadAnexosTableParams();
    				}, function() {
    					$scope.excluindo = false;
    				});
				});
			}
		};
		
		/** Processa inicializacao da aba anexos */
		$scope.loadAbaAnexos = function() {
			$scope.loadAnexosTableParams();
			$scope.findUsuarioLogado();
		};
		
		/** Executa store do proprietario */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$scope.doStore();
		};
		
		$scope.getBean = function() {
			var o = $scope.data;
			
			o.files = $scope.proprietario.files;
			
			return o;
		};  
		
		$scope.doStore = function(){
			$rootScope.showLoading = true;
			
			$http({
                method: 'POST',
                url: contextPath+'rest/contratacaoveiculos/manterProprietario/store',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("dados", angular.toJson(data));
                    
                    if(!angular.isUndefined(data.files)){	                      
	                      formData.append("qtdArquivos", data.files.length);
	                      
	                      for (var i = 0; i < data.files.length; i++) {
	                          formData.append("arquivo_" + i, $scope.proprietario.bytea[i]);
	                      }
                    }
                    
                    return formData;
                },
                data: $scope.getBean()
            }).
            success(function (data, status, headers, config) {
  				$scope.processStore(data); 
  				  
  				$scope.loadAnexosTableParams();
  				
  				$rootScope.showLoading = false;
            }).
            error(function (data, status, headers, config) {                	
            	$rootScope.showLoading = false;
            });
		};
		
		/** Processa resultados de inserir/atualizar */
		$scope.processStore = function(data){				    			
			$scope.data = data;
			$state.transitionTo($state.current, {id: data["idProprietario"]}, {
				reload: false,
				inherit: false,
				notify: true
			});
			$scope.data.tpSituacao.tpPessoa = $scope.data.tpPessoa.value;			
							
			$scope.isESocial();
			
			$scope.resetUpload();
			$scope.setData($scope.data);
			$scope.showSuccessMessage();
			
			// LMS-5482
			if ($scope.data.tpSituacao.value == "N") {
				$scope.addAlerts([{msg: "LMS-26096", type: MESSAGE_SEVERITY.WARNING}]);
			}
		};
		
		
		$scope.openBloqueio = function() {    			
			var idProprietario = $scope.data.idProprietario;
			var btSituacaoBloqueioLabel = $scope.data.btSituacaoBloqueioLabel;
			
			var myController = ["$rootScope","$scope", "$modalInstance", "ManterBloqueiosMotoristaProprietarioFactory", function($rootScope, $scope, $modalInstance, ManterBloqueiosMotoristaProprietarioFactory) {
				
				$scope.bloqueio = {};
				$scope.bloqueio.proprietario = {};
				$scope.bloqueio.proprietario.idProprietario = idProprietario;
				
				$scope.title = btSituacaoBloqueioLabel;
				$scope.innerTemplate = contextPath+"js/app/contratacaoveiculos/manterbloqueiosmotoristaproprietario/view/manterBloqueiosMotoristaProprietarioPopup.html";    				
				$scope.isBloqueioOpen = true;    				
				
				$scope.consultando = true;
				
				ManterBloqueiosMotoristaProprietarioFactory.findBloqueioByProprietario(idProprietario).then(function(data) {
    				$scope.consultando = false;
    				
    				$scope.bloqueio = data;
    				$scope.bloqueio.idProprietario = idProprietario;
    				$scope.bloqueio.bloqueado = data.blBloqueio == 'S' ? true : false;
    				
    				if(angular.isUndefined($scope.bloqueio.dhVigenciaInicial)){
    					$scope.bloqueio.dhVigenciaInicial = Utils.Date.formatMomentAsISO8601(moment());
    				}
				}, function() {
    				$scope.consultando = false;
				});
				
				$scope.close = function() {
					$modalInstance.dismiss("cancel");
				};
				
				$scope.store = function(event){
					$scope.salvando = true;
					
					ManterBloqueiosMotoristaProprietarioFactory.store($scope.bloqueio).then(function(data) {   
						$scope.salvando = false;
						
	    				$modalInstance.close();
					}, function() {
        				$scope.salvando = false;
        			});   
				};            		
			}];
			
			modalService.open({windowClass: "modal-bloqueio",controller: myController}).then(function() { 
				$scope.salvando = true;
				
				$scope.rest.doGet("findById?id=" + idProprietario).then(function(data) {	    		    		
					$scope.processFind(data);
					
					$scope.proprietario.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
					
					$scope.salvando = false;
					
					$scope.showSuccessMessage();
    			}, function() {
    				$scope.salvando = false;
    			});
			},function() {
				$scope.salvando = false;
			}); 	    			  						   			    			    			
		};
		
		 
		/**
		 * Remove proprietario.
		 */
		$scope.removeProprietarioById = function() {
			var id = $scope.data.idProprietario;
			    			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {    					
    			$scope.excluindo = true;
    			$scope.rest.doGet('removeProprietarioById?id='+id).then(function(data) {
					$scope.excluindo = false;
					$scope.showSuccessMessage();
					$scope.limparDados();
				}, function() {
					$scope.excluindo = false;
				});        				
			});  			
		};
		

		$scope.openInscricaoEstadual = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";
    		params += "&pessoa.idPessoa="+ $scope.data.idProprietario;
    		params += "&pessoa.tpIdentificacao="+ $scope.data.tpIdentificacao.value;
    		params += "&pessoa.nrIdentificacao="+ $scope.data.nrIdentificacao;
    		params += "&pessoa.nmPessoa="+ $scope.data.nmPessoa;
    		params += "&labelPessoaTemp="+ $scope.getMensagem("proprietario");
			
			$scope.openLms('configuracoes/manterInscricoesEstaduais.do?cmd=main' + params);
		}; 
	
		$scope.openConsultarBloqueioLiberacao = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";    						
    		params += "&proprietario.idProprietario="+ $scope.data.idProprietario;
    		params += "&proprietario.pessoa.tpIdentificacao="+ $scope.data.tpIdentificacao.value;
    		params += "&proprietario.pessoa.nrIdentificacao="+ $scope.data.nrIdentificacao;
    		params += "&proprietario.pessoa.nmPessoa="+ $scope.data.nmPessoa;
			
			$scope.openLms('contratacaoVeiculos/manterBloqueiosMotoristaProprietario.do?cmd=main' + params);    			
		}; 
		
		$scope.openMeioTransporte = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";    						
    		params += "&proprietario.idProprietario="+ $scope.data.idProprietario;
    		params += "&proprietario.pessoa.nrIdentificacao="+ $scope.data.nrIdentificacao;
    		params += "&proprietario.pessoa.nmPessoa="+ $scope.data.nmPessoa;    			
			    			
			$scope.openLms('contratacaoVeiculos/manterMeiosTransporteProprietarios.do?cmd=main' + params);    			
		};
		    		 
		$scope.changeDescricao = function() {
			$("#descAnexo").removeClass("lms-invalid");
		};
		
		/**
		 * Valida a identificacao inserida para o proprietario.
		 */
		$scope.validateIdentificacao = function(){			
			if(!$scope.data.nrIdentificacao){
				return;
			}
						
			$rootScope.showLoading = true;
			
			$scope.params = {};
			$scope.params.tpIdentificacao = $scope.data.tpIdentificacao.value;
			$scope.params.nrIdentificacao = $scope.data.nrIdentificacao;
			
			$scope.rest.doPost("validateIdentificacao",$scope.params).then(function(data) {
				$rootScope.showLoading = false;
				
				var pessoa = data[0];
				
				if(pessoa){    				    				
					$scope.loadDadosIdentificacao(pessoa);
				}
			},function() {
				$scope.data.nrIdentificacao = null;
				
				$rootScope.showLoading = false;
			});
		};
		
		$scope.validateCpfMei = function(){
			if(!$scope.data.esocial.nrIdentificacaoMei){
				return;
			}
			
			$rootScope.showLoading = true;
			
			$scope.params = {};
			$scope.params.nrIdentificacao = $scope.data.esocial.nrIdentificacaoMei;
			$scope.params.tpIdentificacao = 'CPF';
			
			$scope.rest.doPost("validateIdentificacaoCpfMei", $scope.params).then(function() {
				$rootScope.showLoading = false;				
			},function() {
				$scope.data.esocial.nrIdentificacaoMei = null;
				
				$rootScope.showLoading = false;
			});	
		};
		
		$scope.nrPisChange = function(){
			if(!$scope.data.esocial.nrPis){
				return;
			}
			
			$scope.params = {};
			$scope.params.nrPis = $scope.data.esocial.nrPis;
			
			$scope.rest.doPost("validatePis", $scope.params).then(function(data) {
				$rootScope.showLoading = false;				
			},function() {
				$scope.data.esocial.nrPis = null;
				
				$rootScope.showLoading = false;
			});	
		};
		
		$scope.isPessoaJuridica = function(){
			if(angular.isUndefined($scope.data.tpPessoa)){
				return false;
			}
			
			return $scope.data.tpPessoa.value === 'J';
		};
		
		$scope.isPessoaFisica = function(){
			if(angular.isUndefined($scope.data.tpPessoa)){
				return false;
			}
			
			return $scope.data.tpPessoa.value === 'F';
		};
		
		/**
		 * Funcao correspondente a alteracao do select tpProprietario.
		 */
		$scope.tpProprietarioChange = function(){
			$scope.data.tpSituacao = {};
			$scope.data.tpSituacao.value = 'I';
			
			if(!$scope.data.tpProprietario || $scope.isPessoaJuridica()){    				
				return;
			}
			    			
			$scope.isESocial();
			$scope.validaGerarWorkflow();			

			if($scope.isTpProprietarioDocumentos()){
				return;
			}
		};
		
		$scope.validaGerarWorkflow = function(){
			if(!$scope.data.isMatriz && "P" != $scope.data.tpProprietario.value){
				$scope.data.geraWorkflow = true;
			} else {
				$scope.data.geraWorkflow = false;
			}
		};
		
		$scope.resetESocial = function(){
			if(!$scope.data.esocial){
				return;
			}
			
			$scope.data.esocial.nmMei = null;
			$scope.data.esocial.nrIdentificacaoMei = null;
		};
		
		$scope.tpIdentificacaoChange = function(){
			$scope.data.nrIdentificacao = null;
		};
		
		/**
		 * Quando alterado o tipo de pessoa e necessario limpar os
		 * dados previamente preenchidos.
		 */
		$scope.tpPessoaChange = function(){
			$scope.data.tpIdentificacao = null;
			$scope.data.nrIdentificacao = null;
						
			$scope.data.blMei = {};
			$scope.data.blMei.value = 'N';
			
			if($scope.isPessoaJuridica()){
				$scope.data.tpIdentificacao = {};
				$scope.data.tpIdentificacao.value = 'CNPJ';
			} else if($scope.isPessoaFisica()){				
				$scope.data.tpIdentificacao = {};
				$scope.data.tpIdentificacao.value = 'CPF';
			}

			$scope.resetESocial();
			$scope.isESocial();
		};
		
		/**
		 * Funcao correspondente a alteracao do select blMei.
		 */
		$scope.blMeiChange = function(){
			$scope.resetESocial(false);
			$scope.isESocial();
		};
		
		/**
		 * Verifica se e necessario informar campos para o esocial de acordo com
		 * o preenchimento de dados pelo usuario.
		 */
		$scope.isESocial = function(){
			$scope.data.isESocial = $scope.data.tpPessoa && $scope.data.tpPessoa.value == 'J' ? true : false;
			$scope.data.isMei = $scope.data.isESocial && $scope.data.blMei && $scope.data.blMei.value == 'S' ? true : false;			
			$scope.data.isDocumentos = $scope.data.isMei || (!$scope.data.isESocial && $scope.isTpProprietarioDocumentos()) ? true : false;
		};
		
		/**
		 * Verifica se o proprietario e Agregado ou Eventual.
		 */
		$scope.isTpProprietarioDocumentos = function(){
			return $scope.data.tpProprietario && ($scope.data.tpProprietario.value == 'A' || $scope.data.tpProprietario.value == 'E');
		};		
		
		/**
		 * Carrega dados do proprietario apartir da identificacao informada.
		 */
		$scope.loadDadosIdentificacao = function(data){    			
			$scope.data.idPessoa = data.idPessoa;
			$scope.data.nmPessoa = data.nmPessoa;
			$scope.data.dsEmail = data.dsEmail;
			$scope.data.idTelefoneEndereco = data.idTelefoneEndereco;
			$scope.data.tpTelefone = data.tpTelefone;
			$scope.data.tpUso = data.tpUso;
			$scope.data.nrDdi = data.nrDdi;
			$scope.data.nrDdd = data.nrDdd;
			$scope.data.nrTelefone = data.nrTelefone;

			if(!$scope.data.esocial){
				$scope.data.esocial = {};
			}
			
			$scope.data.esocial.nrRg = data.nrRg;
			$scope.data.esocial.dtEmissaoRg = data.dtEmissaoRg;
			$scope.data.esocial.dsOrgaoEmissorRg = data.dsOrgaoEmissorRg;			
		};
		
		$scope.limparDados = function() {
			$scope.clearData();
			$scope.anexosTableParams.clear();
						
			$scope.resetUpload();
			$scope.setData($scope.data);
			
			$scope.loadDefaultValues();
		};
		
		/** Processa resultados do detalhamento */
		$scope.processFind = function(data){				  
    		$scope.data = data;
    		    		
    		$scope.isESocial();
    		$scope.setData($scope.data);
		};
		
		$scope.loadCurrentPais = function(){
			ManterPaisFactory.findPaisUsuarioLogado().then(function(data) {
				$scope.data.pais = data;
			},function() {
				
			});
		};
		
		$scope.findUsuarioLogado = function(){
			if(!$scope.data.idUsuarioLogado){
				UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
    				$scope.data.idUsuarioLogado = data.idUsuario;
    				$scope.data.nmUsuarioLogado = data.nmUsuario;
    			},function() {
    				
    			});		
			};
		};
		
		$scope.tpIdentificacaoFilter = function () {
			if ($scope.data.tpPessoa && $scope.data.tpPessoa.value == 'F'){
				return  ['CPF', 'DNI'];
			} 
				
			return  ['CNPJ','CUIT','RUT','RUC'];
		};
		
		$scope.loadDefaultValues = function(){
			$scope.proprietario.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
			
			$scope.loadCurrentFilial($scope.data);
			$scope.loadCurrentPais();
			
			$scope.data.dtVigenciaInicial = Utils.Date.formatMomentAsISO8601(moment());
			$scope.data.tpSituacao = {};
			$scope.data.tpSituacao.value = 'N';			
			
			$scope.data.blNaoAtualizaDbi = {};
			$scope.data.blNaoAtualizaDbi.value = 'N';	
			
			$scope.data.blRotaFixa = {};
			$scope.data.blRotaFixa.value = 'N';	
		};
		
		/** Processa inicializacao da aba cadastro */
		$scope.initializeAbaCad = function(params) {
			$scope.anexosTableParams.clear();
			$scope.resetUpload();
			
			if (params.id) {					
				if(params.id.indexOf('&idProcessoWorkflow=') > -1){
					  var idProcesso = params.id;						
					  var id = idProcesso.substring(idProcesso.indexOf('=') + 1, idProcesso.length);
					  $rootScope.showLoading = true;
					  $scope.rest.doGet("findByIdProcesso?id="+id).then(function(data) {	    		    		
						$scope.processFind(data);
						$rootScope.showLoading = false;
        			}, function() {
        				$rootScope.showLoading = false;
        			});
				} else {
					$rootScope.showLoading = true;
					$scope.rest.doGet('findById?id=' + params.id).then(function(data) {	    		    		
						$scope.processFind(data);
						$rootScope.showLoading = false;
        			}, function() {
        				$rootScope.showLoading = false;
        			});
				}
    		} else {
    			$scope.setData({});
    			
    			$scope.loadDefaultValues();
    		}
		};
		
		$scope.initializeAbaCad($stateParams);	
	}
];

var ManterReciboComplementarCadController = [
 	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"$http",
	"modalService",
	"TableFactory",
	function($rootScope, $scope, $stateParams, $state, $http, modalService, TableFactory) {

		$scope.fileUploadParams = {};
		
		/** Inicializa expansao das sub-abas */
 		$scope.isInssOpen = false;
		$scope.isOutrosImpostosOpen = false;
		$scope.isValoresOpen = true;
		$scope.isDatasOpen = true;
		$scope.isDadosReciboOpen = true;
		$scope.isDadosReciboComplementarOpen = true;
		
		$scope.recibo.blockSuggest = true;
		
		/** Define factory das tabelas da tela */
		$scope.anexosTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findAnexos" });
					
		/** Monita alteracoes no componente suggest da filial. */
		$scope.$watch('data.filial', function(data){
			if(!$scope.recibo.blockSuggest){
    			$scope.setReciboComplementarFilial(data);    			
			}
		});
		
		/** Monita alteracoes no componente suggest de recibo. */
		$scope.$watch('data.reciboComplementado', function(data){
			if(!$scope.recibo.blockSuggest){
				$scope.loadReciboComplementado(data);
			}
		});
		
		$scope.resetUpload = function(){
			$scope.recibo.files = [];
			$scope.recibo.bytea = [];
			$scope.recibo.descricao = null;
		}; 
		
		$scope.loadDefaultValues = function(){
			$scope.data = {};
			
			$scope.recibo.blockSuggest = false;
			
			$scope.loadCurrentFilial($scope.data);
		};
		
		$scope.limparDados = function() {
			$scope.clearData();
			$scope.anexosTableParams.clear();
						
			$scope.resetUpload();
			
			$scope.loadDefaultValues();
			
			$scope.setData($scope.data);
		};
		
		$scope.createRecibo = function(){
			$scope.loadDefaultValues();
			
			$scope.setData($scope.data);
		};
		
		/** Processa inicializacao da aba cadastro */
		$scope.initializeAbaCad = function(params) {
			$scope.anexosTableParams.clear();
			$scope.resetUpload();
			
			if (params.id) {					
				if(params.id.indexOf('&idProcessoWorkflow=') > -1){
					var idProcesso = params.id;						
					var id = idProcesso.substring(idProcesso.indexOf('=') + 1, idProcesso.length);
					
					
					
					$scope.rest.doGet("findByIdProcesso?id="+id).then(function(data) {						
						$scope.processFind(data);
						
						
        			}, function() {
        				
        			});
				} else {
					
					
					$scope.findById(params.id).then(function(data) {						
						$scope.processFind(data);
												
						
					},function() {
						
					});
				}
    		} else {
    			$scope.createRecibo();
    		}
		};
		
		$scope.initializeAbaCad($stateParams);	
				
		/** Carrega dados dos campos da tela de acordo com a alteracao da suggest de recibo */
		$scope.loadReciboComplementado = function(data){
			if(!data || !data.idReciboFreteCarreteiro){
				$scope.setReciboComplementado({});
				
				return;
			}
			
			
			
			$scope.findById(data.idReciboFreteCarreteiro).then(function(data) {				
    			if(data){        				
    				$scope.setReciboComplementado(data);      				
    			} else {
    				$scope.setReciboComplementado({});
    			}
    			
    			
			}, function() {
				
			});
		};
		
		/** Define dados de acordo com os dados do recibo carregado na suggest */
		$scope.setReciboComplementado = function(data){
			if(angular.isUndefined($scope.data)){
				return;
			}
			
    		$scope.data.tpReciboFreteCarreteiro = data.tpReciboFreteCarreteiro;
    		$scope.data.proprietario = data.proprietario;
    		$scope.data.meioTransporteRodoviario = data.meioTransporteRodoviario;
    		$scope.data.controleCarga = data.controleCarga;
    		$scope.data.moedaPais = data.moedaPais;
    		$scope.data.motorista = data.motorista;
		};
				
		/** Executa cancelamento do recibo complementar */
		$scope.cancel = function() {    			
			
			
			$scope.rest.doGet('cancelar?id='+$scope.data.idReciboFreteCarreteiro).then(function(data) {				
				$scope.processFind(data);
				
				
				
				$scope.showSuccessMessage();				
			},function() {
				
			});			
		};
		
		/** Processa inicializacao da aba anexos */
		$scope.loadAbaAnexos = function() {
			$scope.loadAnexosTableParams();
			$scope.findUsuarioLogado();
		};
		
		/** Define os dados da suggest de recibo */
		$scope.setReciboComplementarFilial = function(data){
			$scope.data.reciboComplementado = null;
			
			if(!data){
				$scope.recibo.reciboComplementado = null;
				return;
			}
			
			$scope.data.reciboComplementado = null;			
			$scope.recibo.reciboComplementado = { sgFilial: data.sgFilial };						
		};
		
		$scope.changeDescricao = function() {
			$("#descAnexo").removeClass("lms-invalid");
		};
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			
			
			$scope.anexosTableParams.load({filter:{idReciboFreteCarreteiro: $scope.data.idReciboFreteCarreteiro}}).then(function(data) {
				if(!$scope.anexosTableParams.list){
        			$scope.anexosTableParams.list = [];   
        			$scope.anexosTableParams.qtdRegistros = 0;
    			}
							
				$scope.loadNewFiles();
				
				
			},function() {
				
			});    			    			
		};   		
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.recibo.descricao) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-27117"), type: MESSAGE_SEVERITY.WARNING}]);    				
				return false;
			}
			
			if (angular.isUndefined($scope.fileUploadParams) || 
                    angular.isUndefined($scope.fileUploadParams.selectedFiles) || 
                    $scope.fileUploadParams.selectedFiles.length == 0){    				
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
				$scope.fileUploadParams.clear();
				return false;
			}
			    			
			var selectedFile = $scope.fileUploadParams.selectedFiles[0];
						
			/*
			 * Cria um item para adicionar a grid. 
			 */
			var file = {};
			file.descAnexo = $scope.recibo.descricao;
			file.nmUsuario = $scope.recibo.usuario.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());		
						
			/* 
			* Mantem no scope o array de dados para ser enviado quando o usuario salvar o registro.
			*/	 
			$scope.recibo.files.push(file);
			$scope.recibo.bytea.push(selectedFile);
			
			/*
			 * Zera configuracoes do componente.
			 */			 
			$scope.fileUploadParams.clear();
			$scope.recibo.descricao = null;
						
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
			if($scope.recibo.files){    				
				$.each($scope.recibo.files, function(){
					$scope.anexosTableParams.list.unshift(this);
				});
				
				$scope.anexosTableParams.qtdRegistros += $scope.recibo.files.length;
			}
		};
		
		$scope.downloadFile = function(id) {
		
			
			$scope.rest.doGet("findAnexoReciboFcById?id="+id).then(function(data) {
				

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
				if(!this.idAnexoReciboFc){    					
					tmp.push($scope.recibo.files.indexOf(this));    					
				} else {
					ids.push(this.idAnexoReciboFc);	
				}
			});
			    			
			if (ids.length===0 && tmp.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: MESSAGE_SEVERITY.WARNING}]);
			} else {    				
    			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
    				
    				
    				/*
    				 * Remove itens temporarios.
    				 */	    				 
    				$.each(tmp, function(){
    					$scope.recibo.files.splice(this, 1);
    					$scope.recibo.bytea.splice(this, 1);
    				});
    				
    				$scope.rest.doPost("removeAnexoReciboFcByIds",ids).then(function(data) {
    					$scope.loadAnexosTableParams();
    					
    				
    					
    					$scope.showSuccessMessage();
    				}, function() {
    					
    				});
				});
			}
		};
		
		$scope.emitirRecibo = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			if($scope.data.tpReciboFreteCarreteiro.value == 'C'){
				
				
				$scope.rest.doGet("emitirRecibo?id=" + $scope.data.idReciboFreteCarreteiro).then(function(data) {
					var filename = data.fileName;
					
					$scope.findById($scope.data.idReciboFreteCarreteiro).then(function(data){
						$scope.processFind(data);
						
						
						
						location.href = contextPath+"/viewBatchReport?"+filename;
					});    				
    			},function() {
    				
    			});
			} else if($scope.data.tpReciboFreteCarreteiro.value == 'V'){
				
				
				$scope.rest.doGet('emitirReciboViagem?id=' + $scope.data.idReciboFreteCarreteiro).then(function(data) {
					var filename = data.fileName;
					
					$scope.findById($scope.data.idReciboFreteCarreteiro).then(function(data){
						$scope.processFind(data);
						
						
						
						location.href = contextPath+"/viewBatchReport?"+filename;
					});
    			},function() {
    				
    			});
			}    			
		};
		
		$scope.isAnexos = function(){		
			var isAnexos = false;
			
			$scope.rest.doGet('isAnexos?id=' + $scope.data.idReciboFreteCarreteiro).then(function(data) {
				isAnexos = data;
			},function() {
				
			});
			
			return isAnexos || $scope.recibo.files.length > 0;
		};	    	
		
		$scope.confirmStore = function(isStoredAnexos){
			/*
			 * Exibe mensagem de confirmacao caso o usuario nao tenha
			 * inserido nenhum anexo.
			 */
			if(!isStoredAnexos && $scope.recibo.files.length == 0){
				modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25079"), windowClass: 'modal-confirm'}).then(function() {    						
					$scope.doStore();					
				},function() {
					
				}); 
			} else {
				$scope.doStore();
			}
		};
		
		/** Executa store do recibo */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			/*
			 * Apenas valida a existencia de anexos se o tipo de recibo for
			 * coleta entrega.
			 */
			if($scope.isColetaEntrega()){
				if($scope.data.idReciboFreteCarreteiro){
					$scope.rest.doGet('countAnexos?id=' + $scope.data.idReciboFreteCarreteiro).then(function(data) {
						$scope.confirmStore(data > 0);
					},function() {
						
					});	
				} else {
					$scope.confirmStore(false);
				}						
			} else {
				$scope.doStore();
			}
		};
		
		$scope.getReturnParams = function(data){
			var params = null;
			
			if(data.workflow){
				params = {id: "&idProcessoWorkflow=" + data["idReciboFreteCarreteiro"]};
			} else {
				params = {id: data["idReciboFreteCarreteiro"]};
			}
			
			return params;
		};
		
		$scope.processStore = function(data){
			$scope.resetUpload();
			
			$scope.data = data;
			$state.transitionTo($state.current, $scope.getReturnParams(data), {
				reload: false,
				inherit: false,
				notify: true
			});	
			
			$scope.setData(data);
			
			$scope.recibo.blockSuggest = true;
			$scope.recibo.reciboComplementado = { sgFilial:  data.reciboComplementado.filial.sgFilial };
			
			$scope.showSuccessMessage();					
		};

		/** Processa resultados do detalhamento */
		$scope.processFind = function(data){
			$scope.resetUpload();
			
			$scope.data = data;						
			$state.transitionTo($state.current, $scope.getReturnParams(data), {
				reload: false,
				inherit: false,
				notify: true
			});			
			
			$scope.setData(data);
			
			$scope.recibo.blockSuggest = true;
			$scope.recibo.reciboComplementado = { sgFilial:  data.reciboComplementado.filial.sgFilial };
		};
		
		$scope.doStore = function(){
			
			
			$http({
                method: 'POST',
                url: contextPath+'rest/fretecarreteiroviagem/manterReciboComplementar/store',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("data", angular.toJson(data));
                    
                    if(!angular.isUndefined(data.files)){	                      
	                      formData.append("qtdArquivos", data.files.length);
	                      
	                      for (var i = 0; i < data.files.length; i++) {
	                          formData.append("arquivo_" + i, $scope.recibo.bytea[i]);
	                      }
                    }
                    
                    return formData;
                },
                data: $scope.getBean(false)
            }).
            success(function (data, status, headers, config) {
  				$scope.processStore(data); 
  				  
  				$scope.loadAnexosTableParams();
  				
  				
            }).
            error(function (data, status, headers, config) {            	
            	
            });
		};
		
		$scope.isColetaEntrega = function(){
			if(!$scope.data.tpReciboFreteCarreteiro){
				return false;
			}
			
			return 'C' == $scope.data.tpReciboFreteCarreteiro.value;
		};
		
		$scope.getBean = function(confirmed) {
			var o = {};
			
			o = $scope.data;
			o.confirmed = confirmed;
			o.files	= $scope.recibo.files;
			
			return o;
		};
		
		$scope.openOcorrencias = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";    			
			params += "&reciboFreteCarreteiro.idReciboFreteCarreteiro="+ $scope.data.idReciboFreteCarreteiro;    			
			params += "&reciboFreteCarreteiro.nrReciboFreteCarreteiro="+ $scope.data.nrReciboFreteCarreteiro;   			
			params += "&reciboFreteCarreteiro.filial.idFilial="+ $scope.data.filial.idFilial;
			params += "&reciboFreteCarreteiro.filial.sgFilial="+ $scope.data.filial.sgFilial;    			
			params += "&reciboFreteCarreteiro.filial.pessoa.nmFantasia="+ $scope.data.filial.nmFilial;    			
			params += "&reciboFreteCarreteiro2.idReciboFreteCarreteiro="+ $scope.data.idReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro2.filial.sgFilial="+ $scope.data.filial.sgFilial;
			params += "&reciboFreteCarreteiro.controleCarga.idControleCarga="+ $scope.getModalValue($scope.data.idControleCarga);
			params += "&reciboFreteCarreteiro.controleCarga.nrControleCarga="+ $scope.getModalValue($scope.data.nrControleCarga);
			params += "&reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem.sgFilial="+ $scope.getModalValue($scope.data.sgFilialControleCarga);						
			params += "&tpReciboFreteCarreteiro="+ $scope.data.tpReciboFreteCarreteiro.description.value;
			params += "&blComplementar=true";    				
			
			$scope.openLms('freteCarreteiroViagem/manterOcorrenciasRecibo.do?cmd=main' + params);    			
		};
		
		$scope.openControleCarga = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";
						
    		params += "&idControleCarga="+ $scope.getModalValue($scope.data.idControleCarga);
    		params += "&nrControleCarga="+ $scope.getModalValue($scope.data.nrControleCarga);
    		params += "&filialByIdFilialOrigem.idFilial="+ $scope.getModalValue($scope.data.idFilialControleCarga);
    		params += "&filialByIdFilialOrigem.sgFilial="+ $scope.getModalValue($scope.data.sgFilialControleCarga);
    		params += "&filialByIdFilialOrigem.pessoa.nmFantasia="+ $scope.getModalValue($scope.data.nmFilialControleCarga);
			
			$scope.openLms('carregamento/consultarControleCargas.do?cmd=main' + params);    			
		};
		
		$scope.getModalValue = function(object){
			if(object === null || angular.isUndefined(object)){
				return "";
			}
			
			return object;
		};
	}
];

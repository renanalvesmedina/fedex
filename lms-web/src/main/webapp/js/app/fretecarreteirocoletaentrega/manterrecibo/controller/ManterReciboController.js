
var ManterReciboController = [
	"$rootScope", 
	"$scope", 
	"$location", 
	"$http",
	"ManterReciboFactory", 
	"ManterReciboComplementarFactory",
	"FilialFactory", 
	"TableFactory", 
	"domainService", 
	"modalService", 
	"$modal", 
	function($rootScope, $scope, $location, $http, ManterReciboFactory, ManterReciboComplementarFactory, FilialFactory, TableFactory, domainService, modalService, $modal) {
		$scope.setConstructor({
			rest: "/fretecarreteirocoletaentrega/manterRecibo"
		});
		
		$scope.dados = {};
		
		$scope.fileUploadParams = {};
		$scope.filter = {};
		$scope.recibo = {};

		/** Inicializa expansao das sub-abas */
 		$scope.isInssOpen = false;
		$scope.isOutrosImpostosOpen = false;
		$scope.isValoresOpen = true;
		$scope.isDatasOpen = true;
		$scope.isProprietarioOpen = true;			
		
		/** Define factory das tabelas da tela */
		$scope.listTableParams = new TableFactory({ service: ManterReciboFactory.find });
		$scope.anexosTableParams = new TableFactory({ service: ManterReciboFactory.findAnexos, remotePagination: false });
		
		$scope.loadCurrentFilial = function(){
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				$scope.filter.filialEmissao = {};
	    			$scope.filter.filialEmissao = data;    	    			
    			}
    			
    			$scope.filter.isMatriz = data.isMatriz;
			}, function() {
								
			});
		};
		
		/** Processa inicializacao da aba anexos */
		$scope.loadAbaAnexos = function() {
			$scope.loadAnexosTableParams();
		};
		
		$scope.consultar = function() {
			$scope.consultando = true;
			$scope.listTableParams.load($scope.getFilter(false));
		};
		
		$scope.exportarExcel = function() {
			$scope.exportando = true;
			ManterReciboFactory.find($scope.getFilter(true), true).then(function(data) {
				$scope.exportando = false;
				
				if (data.fileName) {
					location.href = contextPath+"rest/report/"+data.fileName;
				}
				
			}, function() {
				$scope.exportando = false;
			});
		};
		
		$scope.relatorioReciboComplementar = function() {
			$scope.exportando = true;
			ManterReciboFactory.report($scope.getFilter(true), true).then(function(data) {
				$scope.exportando = false;
				
				if (data.fileName) {
					window.open(contextPath+"rest/report/"+data.fileName);
				}
				
			}, function() {
				$scope.exportando = false;
			});
		};
		
		$scope.limpar = function() {
			$scope.filter = {};
			$scope.listTableParams.clear();
			$scope.loadCurrentFilial();
		};
		
		$scope.abreDetalhe = function(o) {
			$location.path("/app/manterRecibo/detalhe/" + o.idReciboFreteCarreteiro);
		};
			
		$scope.getFilter = function(report) {
			var o = {};
			o.filtros = $scope.filter;			       		
			o.report = report ? true : false;
						    			
			return o;
		};
		
		$scope.limparDados = function() {
			$scope.dados = {};
			
			$scope.resetUpload();
			
			$location.path("/app/manterRecibo/detalhe/");
		};
		
		$scope.doStore = function(){
			$rootScope.showLoading = true;
			
			$http({
                method: 'POST',
                url: contextPath+'rest/fretecarreteirocoletaentrega/manterRecibo/store',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("dados", angular.toJson(data));
                    
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
            	$scope.dados = data;
  				  	  				
  				$rootScope.showLoading = false;
  				
  				$scope.resetUpload();  				
  				$scope.loadAnexosTableParams();
  				$scope.showSuccessMessage();
            }).
            error(function (data, status, headers, config) {                	
            	$rootScope.showLoading = false;
            });
		};
		
		/** Executa store do recibo */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);    				    				
				$event.preventDefault();
				return false;
			}    			
			$scope.doStore();
			    						
		};
		
		$scope.onChangeDescricao = function() {
			$("#descricao").removeClass("lms-invalid");
		};
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			$scope.anexosTableParams.load({filtros:{idReciboFreteCarreteiro: $scope.dados.idReciboFreteCarreteiro}}).then(function(data) {
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
			$scope.recibo.files = [];
			$scope.recibo.bytea = [];
		}; 
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.recibo.descricao) {
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
				$scope.fileUploadParams.clear();
				return false;
			}
			    			
			var selectedFile = $scope.fileUploadParams.selectedFiles[0];
			
			/*
			 * Cria um item para adicionar a grid. 
			 */
			var file = {};
			file.descAnexo = $scope.recibo.descricao;
			file.nmUsuario = $scope.dados.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());		
			
			/*
			 * Mantem no scope o array de dados para ser enviado quando o
			 * usuario salvar o registro.
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
		
		$scope.removeAnexo = function(id) {
			var idFreteCarreteiro = $scope.dados.idReciboFreteCarreteiro;
			
			ManterReciboFactory.deleteAnexo(id).then(function(data) {
				$scope.anexosTableParams.load({filtros:{idReciboFreteCarreteiro: idFreteCarreteiro}});
			});    			
		};
		
		$scope.downloadFile = function(id) {
			$scope.downloading = true;
			
			ManterReciboFactory.findAnexoById(id).then(function(data) {
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
	    				$scope.excluindo = true;
	    				
	    				/*
	    				 * Remove itens temporarios.
	    				 */
	    				$.each(tmp, function(){
	    					$scope.recibo.files.splice(this, 1);
	    					$scope.recibo.bytea.splice(this, 1);
	    				});
	    				
	    				ManterReciboFactory.removeAnexoReciboByIds(ids).then(function(data) {
    					$scope.excluindo = false;
    					$scope.showSuccessMessage();
    					
    					$scope.loadAnexosTableParams();
    				}, function() {
    					$scope.excluindo = false;
    				});
				});
			}
		};
		
		$scope.getBean = function(confirmed) {
			var o = {};
			
			o.idReciboFreteCarreteiro = $scope.dados.idReciboFreteCarreteiro;
			o.nrNfCarreteiro = $scope.dados.nrNfCarreteiro;
			o.obReciboFreteCarreteiro = $scope.dados.obReciboFreteCarreteiro;    			
			o.dtProgramadaPagto = $scope.dados.dtProgramadaPagto;
			o.confirmed = confirmed;
			o.workflow = $scope.dados.workflow;
			o.tpSituacao = $scope.dados.tpSituacaoRecibo;
			o.files = $scope.recibo.files;
			
			return o;
		};    		
	    		
		$scope.emitirRecibo = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			$scope.emitindoRecibo = true;

			ManterReciboFactory.emitirRecibo($scope.dados.idReciboFreteCarreteiro).then(function(data) {
				$scope.emitindoRecibo = false;
				location.href = contextPath+"/viewBatchReport?"+data.fileName;
			},function() {
				$scope.emitindoRecibo = false;
			});
		};
		    		
		$scope.cancelarRecibo = function() {	
			$scope.cancelando = true;
			    			
			var idReciboFreteCarreteiro = $scope.dados.idReciboFreteCarreteiro;
			    			
			ManterReciboFactory.cancelarRecibo(idReciboFreteCarreteiro).then(function(data) {
				$scope.cancelando = false;
				    				
				if(!angular.isUndefined(data.cancel)){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25079"), windowClass: 'modal-confirm'}).then(function() {    						
						ManterReciboFactory.cancelarReciboComplementar(idReciboFreteCarreteiro).then(function(data) {
		    				$scope.cancelando = false;
		    			
		    				$scope.dados = data;
		    				
		    				$scope.showSuccessMessage();
		    			},function() {
		    				$scope.cancelando = false;
		    			});    						
    				},function() {
    					$scope.cancelando = false;
    				});
				} else {
					$scope.cancelando = false;
					
					$scope.dados = data;
					
					$scope.showSuccessMessage();
				}
			},function() {
				$scope.cancelando = false;
			});
		};
		    		    		
		$scope.openRim = function() {    			
			var idReciboFreteCarreteiro = $scope.dados.idReciboFreteCarreteiro;
			
			var myController = ["$rootScope","$scope", "$modalInstance", "TableFactory", "ManterReciboFactory", function($rootScope,$scope, $modalInstance, TableFactory, ManterReciboFactory) {
				
				$scope.title = "documentos";
				$scope.innerTemplate = contextPath+"js/app/fretecarreteirocoletaentrega/manterrecibo/view/popupRim.html";
				$scope.listTableParams = new TableFactory({service: ManterReciboFactory.findRim});
				$scope.isRimOpen = true;
				$scope.consultando = true;
				
    			ManterReciboFactory.findRimDados({filtros:{idReciboFreteCarreteiro: idReciboFreteCarreteiro}}, true).then(function(data) {
    				$scope.consultando = false;
    				
    				$scope.info = data;
    				
    				$scope.listTableParams.load({filtros:{idReciboFreteCarreteiro: idReciboFreteCarreteiro}});
    				
				}, function() {
    				$scope.consultando = false;
				});
	    		
				$scope.close = function() {
					$modalInstance.dismiss("cancel");
				};
				
				$scope.visualizarRim = function(url){    
					$rootScope.openLms('indenizacoes/consultarReciboIndenizacao.do?cmd=main' + url);
				};
        		
			}];
			modalService.open({windowClass: "modal-rim",controller: myController});    			    			    			
		};
		
		$scope.openNotasCreditos = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			       	
			var params = "&isPopup=true";    			
			params += "&reciboFreteCarreteiro.idReciboFreteCarreteiro="+ $scope.dados.idReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro2.idReciboFreteCarreteiro="+ $scope.dados.idReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro.nrReciboFreteCarreteiro="+ $scope.dados.nrReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro2.filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;
			params += "&filial.idFilial="+ $scope.dados.filialEmissao.idFilial;
			params += "&filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;
			params += "&filial.pessoa.nmFantasia="+ $scope.dados.filialEmissao.nmFilial;
			params += "&controleCargas.meioTransporte.idMeioTransporte="+ $scope.dados.idMeioTransporte;
			params += "&controleCargas.meioTransporte2.nrFrota="+ $scope.dados.nrFrota;
			params += "&controleCargas.meioTransporte.nrIdentificador="+ $scope.dados.nrIdentificador;
			params += "&controleCargas.proprietario.idProprietario="+ $scope.dados.idProprietario;
			params += "&controleCargas.proprietario.pessoa.nrIdentificacao="+ $scope.dados.nrIdentificacaoProprietarioFormatado;    			
			params += "&controleCargas.proprietario.pessoa.nmPessoa="+ $scope.dados.nmPessoaProprietario;
			    			
			$scope.openLms('freteCarreteiroColetaEntrega/manterNotasCredito.do?cmd=main' + params);    			
		};
		
		$scope.openRecibosComplementares = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			    	    			
			var params = "&isPopup=true";    			
			params += "&reciboComplementado.idReciboFreteCarreteiro="+ $scope.dados.idReciboFreteCarreteiro;
			params += "&reciboComplementado.nrReciboFreteCarreteiro="+ $scope.dados.nrReciboFreteCarreteiro;    			
			params += "&reciboComplementado.filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;
			params += "&filial.idFilial="+ $scope.dados.filialEmissao.idFilial;
			params += "&filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;    			
			params += "&filial.pessoa.nmFantasia="+ $scope.dados.filialEmissao.nmFilial;
			params += "&tpReciboFreteCarreteiro=C";
			    			
			$scope.openLms('freteCarreteiroViagem/manterRecibosComplementar.do?cmd=main' + params);    			
		};
		
		$scope.openOcorrencias = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";    			
			params += "&reciboFreteCarreteiro.idReciboFreteCarreteiro="+ $scope.dados.idReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro.nrReciboFreteCarreteiro="+ $scope.dados.nrReciboFreteCarreteiro;
			params += "&reciboFreteCarreteiro2.filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;
			params += "&reciboFreteCarreteiro.filial.idFilial="+ $scope.dados.filialEmissao.idFilial;
			params += "&reciboFreteCarreteiro.filial.sgFilial="+ $scope.dados.filialEmissao.sgFilial;    			
			params += "&reciboFreteCarreteiro.filial.pessoa.nmFantasia="+ $scope.dados.filialEmissao.nmFilial;
			params += "&tpReciboFreteCarreteiro=C";
			params += "&blComplementar="+ $scope.dados.blComplementar;    				
			
			$scope.openLms('freteCarreteiroViagem/manterOcorrenciasRecibo.do?cmd=main' + params);    			
		};
		
		/** Processa resultados do detalhamento */
		$scope.processFind = function(data){ 
			$scope.dados = data;					    		
    		$scope.resetUpload();
		};
	}
];


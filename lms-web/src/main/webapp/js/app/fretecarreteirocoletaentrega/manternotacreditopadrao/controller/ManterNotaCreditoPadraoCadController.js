
var ManterNotaCreditoPadraoCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"$http",
	"$modal",
	"$filter",
	"modalService",
	"UsuarioLmsFactory",
	"TableFactory",
	"RestAccessFactory",
	function($rootScope, $scope, $stateParams, $state, $http, $modal, $filter, modalService, UsuarioLmsFactory, TableFactory, RestAccessFactory) {
		
		$scope.anexosTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findAnexos" });
		
		$scope.fileUploadParams = {};
		$scope.isInformacoesProprietarioOpen = true;
		$scope.isInformacoesOpen = true;
		$scope.isAcrescimoDescontoOpen = true;		
		$scope.isTabelaOpen = true;
		
		$scope.notaCreditoPadrao.blNaoExecutadoAtivo = true;
		$scope.notaCreditoPadrao.blItemNaoExecutadoAtivo = true;
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			var o = {};
			o.filtros = {};    			
			o.filtros.idNotaCredito = $scope.data.idNotaCredito;
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
			$scope.notaCreditoPadrao.files = [];
			$scope.notaCreditoPadrao.bytea = [];
			$scope.notaCreditoPadrao.descricao = null;
		};    		
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.notaCreditoPadrao.descricao) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-27117"), type: "warning"}]);    				
				return false;
			}
			
			if (angular.isUndefined($scope.fileUploadParams) 
					|| angular.isUndefined($scope.fileUploadParams.selectedFiles)
						|| $scope.fileUploadParams.selectedFiles.length == 0){    				
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-27118"), type: "warning"}]);
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
			file.dsAnexo = $scope.notaCreditoPadrao.descricao;
			file.nmUsuario = $scope.notaCreditoPadrao.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());
			
			/*
			 * Mantem no scope o array de dados para ser enviado quando o
			 * usuario salvar o registro.
			 */
			$scope.notaCreditoPadrao.files.push(file);
			$scope.notaCreditoPadrao.bytea.push(selectedFile);
			   
			/*
			 * Zera configuracoes do componente. 
			 */
			$scope.fileUploadParams.clear();
			$scope.notaCreditoPadrao.descricao = null;
			
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
			if($scope.notaCreditoPadrao.files){    				
				$.each($scope.notaCreditoPadrao.files, function(){
					$scope.anexosTableParams.list.unshift(this);
				});
				
				$scope.anexosTableParams.qtdRegistros += $scope.notaCreditoPadrao.files.length;
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
				if(!this.idAnexoNotaCredito){    					
					tmp.push($scope.notaCreditoPadrao.files.indexOf(this));    					
				} else {
					ids.push(this.idAnexoNotaCredito);	
				}
			});
			    			
			if (ids.length===0 && tmp.length===0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: "warning"}]);
			} else {    				
	    			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
	    				$scope.excluindo = true;
	    				
	    				/*
	    				 * Remove itens temporarios.
	    				 */
	    				$.each(tmp, function(){
	    					$scope.notaCreditoPadrao.files.splice(this, 1);
	    					$scope.notaCreditoPadrao.bytea.splice(this, 1);
	    				});
	    				
	    				$scope.rest.doPost("removeAnexoNotaCreditoByIds",ids).then(function(data) {
    					$scope.excluindo = false;
    					$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
    					
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
		
		/** Executa store do desconto */
		$scope.store = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: "warning"}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$scope.doStore();
		};
		
		$scope.getBean = function() {
			var o = {};
			
			o = $scope.data;
			o.files	= $scope.notaCreditoPadrao.files;
			
			return o;
		};  
		
		$scope.doStore = function(){
			$rootScope.showLoading = true;
			
			$http({
                method: 'POST',
                url: contextPath+'rest/fretecarreteirocoletaentrega/notaCreditoPadrao/store',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("data", angular.toJson(data));
                    
                    if(!angular.isUndefined(data.files)){	                      
	                      formData.append("qtdArquivos", data.files.length);
	                      
	                      for (var i = 0; i < data.files.length; i++) {
	                          formData.append("arquivo_" + i, $scope.notaCreditoPadrao.bytea[i]);
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
			$scope.resetUpload();
			
			$scope.data = data;
			$state.transitionTo($state.current, {id: data["idNotaCredito"]}, {
				reload: false,
				inherit: false,
				notify: true
			});
			
			$scope.setData(data);
			
			$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
		};
				
		$scope.findUsuarioLogado = function(){
			if(!$scope.notaCreditoPadrao.idUsuarioLogado){
				UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
					$scope.notaCreditoPadrao.idUsuarioLogado = data.idUsuario;
					$scope.notaCreditoPadrao.nmUsuarioLogado = data.nmUsuario;
    			},function() {
    				
    			});		
			};
		};
		
		$scope.visualizarNotaCredito = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			$rootScope.showLoading = true;
						
			$scope.rest.doGet("visualizarNotaCredito?id=" + $scope.data.idNotaCredito).then(function(data) {				
				$rootScope.showLoading = false;
				
				if(data && data.fileName){
					location.href = contextPath+"/viewBatchReport?"+data.fileName;	
				}	
			},function() {
				$rootScope.showLoading = false;
			});			
		};
		
		$scope.emitirNotaCredito = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			$rootScope.showLoading = true;
			
			var emitido = $scope.data.dhEmissao != null;
			
			$scope.rest.doGet("emitirNotaCredito?id=" + $scope.data.idNotaCredito).then(function(data) {				
				if(!emitido){					
					$scope.findById($scope.data.idNotaCredito).then(function(data) {						
						$scope.processStore(data);
        			}, function() { });
				}
				
				$rootScope.showLoading = false;
				
				if(data && data.fileName){
					location.href = contextPath+"/viewBatchReport?"+data.fileName;	
				}		
			},function() {
				$rootScope.showLoading = false;
			});			
		};
		
		$scope.regerarNotaCredito = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			var bean = { idControleCarga: $scope.data.idControleCarga, tpNotaCredito: $scope.data.tpNotaCredito };
			
			$rootScope.showLoading = true;
						
			$scope.rest.doPost("regerarNotaCredito", bean).then(function(data) {				
				$scope.findById($scope.data.idNotaCredito).then(function(data) {						
					$scope.processStore(data);
					$scope.notaCreditoPadrao.summary = {};
					$scope.subTabControl.selectByIndex(0);
    			}, function() { });
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		var EventosController = ["RestAccessFactory", "$rootScope","$scope", "$modalInstance", "modalParams", function(RestAccessFactory, $rootScope, $scope, $modalInstance, modalParams) {				
			$scope.rest = RestAccessFactory.create("/fretecarreteirocoletaentrega/notaCreditoPadrao");
			
			$scope.eventosTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findEventos" });
			
			$scope.title = $scope.getMensagem("eventos") + ": " + modalParams.sgFilial + " " + $filter('lpad')(modalParams.nrNotaCredito, 10);
			$scope.innerTemplate = contextPath+"js/app/fretecarreteirocoletaentrega/manternotacreditopadrao/view/popup/manterNotaCreditoPadraoEventos.html";								
						
			$scope.close = function() {
				$modalInstance.dismiss("close");
			};
			
			$scope.initModal = function(){
				$scope.eventosTableParams.load( { idNotaCredito: modalParams.idNotaCredito } );
			};
			
			$scope.initModal();
		}];
		
		$scope.openEventos = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			
			$modal.open({
				controller: EventosController,
				templateUrl: contextPath+"js/common/modal/view/modal-template.html",
				windowClass: "modal-eventos",
				resolve: {
	    			modalParams: function() {
        		    	return { idNotaCredito: $scope.data.idNotaCredito, 
        		    			 sgFilial : $scope.data.filial.sgFilial, 
        		    			 nrNotaCredito: $scope.data.nrNotaCredito };
	    			}
	    		}}			
			); 			  						   			    			    			
		};
		
		$scope.openControleCarga = function($event) {
			$event.preventDefault();
			$event.stopPropagation();
			        			
			var params = "&isPopup=true";
						
    		params += "&notaCredito.filial.idFilial="+ $scope.getModalValue($scope.data.filial.idFilial);
    		params += "&notaCredito.filial.sgFilial="+ $scope.getModalValue($scope.data.filial.sgFilial);
    		params += "&filialByIdFilialOrigem.idFilial="+ $scope.getModalValue($scope.data.filial.idFilial);
    		params += "&filialByIdFilialOrigem.sgFilial="+ $scope.getModalValue($scope.data.filial.sgFilial);
    		params += "&filialByIdFilialOrigem.pessoa.nmFantasia="+ $scope.getModalValue($scope.data.filial.nmFilial);
    		params += "&notaCredito.idNotaCredito="+ $scope.getModalValue($scope.data.idNotaCredito);
    		params += "&notaCredito.nrNotaCredito="+ $scope.getModalValue($scope.data.nrNotaCredito);
    		
			$scope.openLms('carregamento/consultarControleCargas.do?cmd=main' + params);    			
		};
		
		$scope.getModalValue = function(object){
			if(object === null || angular.isUndefined(object)){
				return "";
			}
			
			return object;
		};
				
		/** Processa inicializacao da aba itens */
		$scope.loadAbaItens = function() {
			if(!angular.equals({}, $scope.notaCreditoPadrao.summary)){
				return;
			}
			
			$scope.loadItens();
		};
		
        function nthIndex(rawString, separator, n) {
            var index = -1;
            for(i = 0; i < n; i++) {
                index = rawString.indexOf(separator, index);
                if (index == -1) break;
                index++
            }
            return index;
        }

        function populaNotasFiscaisEllipsis(objetosContemNotasFiscais) {
            if (objetosContemNotasFiscais && objetosContemNotasFiscais.length > 0) {
                for (var i = 0; i < objetosContemNotasFiscais.length; i++) {
                    var contemNotaFiscal = objetosContemNotasFiscais[i];
                    var index20 = nthIndex(contemNotaFiscal.notasFiscais, ',', 20)

                    if (index20 == -1) {
                        contemNotaFiscal.notasFiscaisEllipsis = contemNotaFiscal.notasFiscais
                    } else {
                        contemNotaFiscal.notasFiscaisEllipsis = contemNotaFiscal.notasFiscais.substring(0, index20) + '...'
                    }
                }
            }
        }
        
		$scope.loadItens = function(){
			var bean = { idNotaCredito: $scope.data.idNotaCredito, tpNotaCredito: $scope.data.tpNotaCredito };
			
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("findItens", bean).then(function(data) {		
				$scope.notaCreditoPadrao.summary = data;
				
				var itensEntrega = data ? data.itensEntrega : undefined;
                if(itensEntrega && itensEntrega.length > 0){
                    for(var i = 0; i < itensEntrega.length; i++){
                        var doctos = itensEntrega[i] ? itensEntrega[i].listDoctos : undefined;
                        populaNotasFiscaisEllipsis(doctos);
                    }
                }

				var itensNaoExecutados = data ? data.itensNaoExecutados : undefined;
                populaNotasFiscaisEllipsis(itensNaoExecutados);

				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.changeTpAcrescimoDesconto = function(){
			$scope.data.vlAcrescimoDesconto = null;
		};
		
		$scope.toggleAccordions = function(){
			$scope.isTabelaOpen = !$scope.isTabelaOpen;
			
			var itensEntrega = $scope.notaCreditoPadrao.summary.itensEntrega;
			
			if(itensEntrega && itensEntrega.length > 0){
				for(var i = 0; i < itensEntrega.length; i++){
					itensEntrega[i].blAtivo = $scope.isTabelaOpen;
					itensEntrega[i].blDoctoAtivo = false;
					itensEntrega[i].blCalculoAtivo = false;
				}
			}
			
			var itensColeta = $scope.notaCreditoPadrao.summary.itensColeta;
			
			if(itensColeta && itensColeta.length > 0){		
				for(var i = 0; i < itensColeta.length; i++){
					itensColeta[i].blAtivo = $scope.isTabelaOpen;
					itensColeta[i].blPedidoAtivo = false;
					itensColeta[i].blCalculoAtivo = false;
				}
			}
			
			var itensNaoExecutados = $scope.notaCreditoPadrao.summary.itensNaoExecutados;
			
			if(itensNaoExecutados && itensNaoExecutados.length > 0){
				$scope.notaCreditoPadrao.blNaoExecutadoAtivo = $scope.isTabelaOpen;
				$scope.notaCreditoPadrao.blItemNaoExecutadoAtivo = false;
			}
		};
		
		$scope.openTabelaFreteCarreteiro = function(idTabelaFcValores, nmTabela){
			$rootScope.showLoading = true;
			
			RestAccessFactory.create("/fretecarreteirocoletaentrega/tabelaFreteCarreteiroCe").doGet("findTabelaFcValores?id="+idTabelaFcValores).then(function(data) {							
				$rootScope.showLoading = false;
								
				$scope.openPrecos(data, nmTabela);
			}, function() {
				$rootScope.showLoading = false;
			});		
		};
		
		$scope.openPrecos = function(tabelaFcValoresDTO, nmTabela){
			$rootScope.isPopup = true;
			
			$modal.open({
				controller: ManterTabelaFreteCarreteiroModalController,
				templateUrl: contextPath+"js/common/modal/view/modal-template.html",
				windowClass: "modal-detail",
				resolve: {
	    			modalParams: function() {
        		    	return { tabelaFcValoresDTO: tabelaFcValoresDTO, 
        		    		listTipoMeioTransporte: {}, 
        		    		type: tabelaFcValoresDTO.blTipo,
        		    		nmTabela: nmTabela,
        		    		disabled: true,
        		    		isMatriz: false };
	    			}
	    		}}
			);			
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
						$scope.setData(data);
						
						$scope.notaCreditoPadrao.summary = {};
						
						$rootScope.showLoading = false;
        			}, function() {
        				$rootScope.showLoading = false;
        			});
				} else {
					$rootScope.showLoading = true;
					
					$scope.findById(params.id).then(function(data) {						
						$scope.setData(data);
						
						$scope.notaCreditoPadrao.summary = {};
						
						$rootScope.showLoading = false;
					},function() {
						$rootScope.showLoading = false;
					});
				}
    		}
		};
		
		$scope.initializeAbaCad($stateParams);	
	}
];
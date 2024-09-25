
var ManterDescontoCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"$http",
	"modalService",
	"UsuarioLmsFactory",
	"TableFactory",
	function($rootScope, $scope, $stateParams, $state, $http, modalService, UsuarioLmsFactory, TableFactory) {
		
		$scope.anexosTableParams = new TableFactory({ service: $scope.rest.doPost, method: "findAnexos" });
		
		$scope.fileUploadParams = {};
				
		$scope.isInformacoesProprietario = true;
		$scope.isInformacoesRecibo = true;
		$scope.isInformacoesDesconto = true;		
		$scope.isParcelamento = true;
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			var o = {};
			o.filtros = {};    			
			o.filtros.idDesconto = $scope.data.idDescontoRfc;
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
			$scope.desconto.files = [];
			$scope.desconto.bytea = [];
			$scope.desconto.descricao = null;
		};    		
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.desconto.descricao) {
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
			file.dsAnexo = $scope.desconto.descricao;
			file.nmUsuario = $scope.desconto.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());
			
			/*
			 * Mantem no scope o array de dados para ser enviado quando o
			 * usuario salvar o registro.
			 */
			$scope.desconto.files.push(file);
			$scope.desconto.bytea.push(selectedFile);
			   
			/*
			 * Zera configuracoes do componente. 
			 */
			$scope.fileUploadParams.clear();
			$scope.desconto.descricao = null;
			
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
			if($scope.desconto.files){    				
				$.each($scope.desconto.files, function(){
					$scope.anexosTableParams.list.unshift(this);
				});
				
				$scope.anexosTableParams.qtdRegistros += $scope.desconto.files.length;
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
				if(!this.idAnexoDesconto){    					
					tmp.push($scope.desconto.files.indexOf(this));    					
				} else {
					ids.push(this.idAnexoDesconto);	
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
	    					$scope.desconto.files.splice(this, 1);
	    					$scope.desconto.bytea.splice(this, 1);
	    				});
	    				
	    				$scope.rest.doPost("removeAnexoDescontoByIds",ids).then(function(data) {
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
			o.files	= $scope.desconto.files;
			o.prioritario = $scope.data.prioritario;
			
			
			return o;
		};  
		
		$scope.doStore = function(){
			$rootScope.showLoading = true;
			
			$http({
                method: 'POST',
                url: contextPath+'rest/fretecarreteirocoletaentrega/descontoRfc/store',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("data", angular.toJson(data));
                    
                    if(!angular.isUndefined(data.files)){	                      
	                      formData.append("qtdArquivos", data.files.length);
	                      
	                      for (var i = 0; i < data.files.length; i++) {
	                          formData.append("arquivo_" + i, $scope.desconto.bytea[i]);
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
			$state.transitionTo($state.current, {id: data["idDescontoRfc"]}, {
				reload: false,
				inherit: false,
				notify: true
			});
			
			$scope.setData(data);
			
			$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
		};
						
		$scope.limparDados = function() {
			$scope.clearData();
			$scope.anexosTableParams.clear();
						
			$scope.resetUpload();
			$scope.setData($scope.data);
			
			$scope.loadDefaultValues();
			
			$scope.initParcela();			
		};
						
		$scope.loadDefaultValues = function(){			
			$scope.data = {};
			$scope.data.tpSituacao = {};
			$scope.data.tpSituacao.value = 'I';
			
			$scope.data.workflow = false;
			$scope.data.disabled = false;
			$scope.data.cancelar = true;			
			
			$scope.desconto.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
			
			$scope.loadCurrentFilial($scope.data);		
		};
		
		$scope.findUsuarioLogado = function(){
			if(!$scope.desconto.idUsuarioLogado){
				UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
					$scope.desconto.idUsuarioLogado = data.idUsuario;
					$scope.desconto.nmUsuarioLogado = data.nmUsuario;
    			},function() {
    				
    			});		
			};
		};
			
		$scope.$watch('data.proprietario', function(data){
			if(data){
				$scope.rest.doGet("findProprietario?id="+data.idProprietario).then(function(data) {
					$scope.data.proprietario.dtVigenciaInicial = data.dtVigenciaInicial;
					$scope.data.proprietario.dtVigenciaFinal = data.dtVigenciaFinal;
					$scope.data.proprietario.tpSituacao = data.tpSituacao;
					
					$rootScope.showLoading = false;
				}, function() {
					$rootScope.showLoading = false;
				});
			} else {
				$scope.data.meioTransporte = null;
			}
		});	
		
		$scope.$watch('data.tpOperacao.value', function(data){
			if(data == 'CE'){
				$scope.data.controleCarga = null;
			} else{
				$scope.data.nrReciboIndenizacao = null;	
			}
		});
		
		$scope.zerarParametros = function(){
			$scope.data.qtParcelas = "";							
			$scope.data.pcDesconto = "";
			$scope.data.vlFixoParcela = "";
			$scope.data.prioritario = false;
		};
		
		$scope.initParcela = function(){
			
			$scope.data.qtDias = 0;
			$scope.data.parcelas = [];
			$scope.data.vlTotalDesconto = 0,00;
			$scope.data.dtInicioDesconto = Utils.Date.formatMomentAsISO8601(moment());
			
			$scope.zerarParametros();
			
			var parcela = {};
			parcela.numeroParcela = 1;
			parcela.data = Utils.Date.formatMomentAsISO8601(moment());
			
						
			$scope.data.parcelas.push(parcela);
			
			$scope.data.bloquearParcelas = false;
		};
		
		$scope.clearParcelas = function(){
			$scope.initParcela();		
			
			$scope.desconto.tipoParcelamentoSelecionado = {};
		};
		
		$scope.generateParcelas = function(){
			$rootScope.showLoading = true;					
			
			if($scope.desconto.tipoParcelamentoSelecionado == $scope.tipoParcelamentoPrioritario){
				$scope.data.prioritario = true;
			}
			
			
			$scope.rest.doPost("generateParcelas", $scope.data).then(function(data) {
				$scope.data.parcelas = data.parcelas;
				
				$scope.data.bloquearParcelas = true; 
				
				$rootScope.showLoading = false;
			}, function() {
				$scope.data.bloquearParcelas = false;
				
				$rootScope.showLoading = false;
			});
		};
		
		$scope.createDesconto = function(){
			$scope.loadDefaultValues();
			
			$scope.initParcela();
			
			$scope.setData($scope.data);			
		};
		
		/**
		 * Executa o cancelamento do desconto.
		 * 
		 */
		$scope.doCancelarDesconto = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("cancelarDesconto?idDescontoRfc="+$scope.data.idDescontoRfc).then(function(data) {
				$scope.setData(data);
				
				$rootScope.showLoading = false;
				
				$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);						
			}, function() {
				$rootScope.showLoading = false;
			});		
		};
		
		/**
		 * Verifica se e possivel cancelar o desconto.
		 * 
		 */
		$scope.cancelarDesconto = function(){
			$rootScope.showLoading = true;
						
			$scope.rest.doGet("isCancelarDesconto?idDescontoRfc="+$scope.data.idDescontoRfc).then(function(data) {
				$rootScope.showLoading = false;
				
				if(data === 'false'){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25102"), windowClass: 'modal-confirm'})
					.then(function() {
						$scope.doCancelarDesconto();
    				}); 
				} else {
					$scope.doCancelarDesconto();
				}
			}, function() {
				$rootScope.showLoading = false;
			});		
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
						
						$rootScope.showLoading = false;
        			}, function() {
        				$rootScope.showLoading = false;
        			});
				} else {
					$rootScope.showLoading = true;
					
					$scope.findById(params.id).then(function(data) {						
						$scope.setData(data);
												
						$rootScope.showLoading = false;
					},function() {
						$rootScope.showLoading = false;
					});
				}
    		} else {
    			$scope.createDesconto();
    		}
		};
		
		$scope.tipoParcelamentoNumeroParcela = {
				id: 1,
				label: $scope.getMensagem("parcelas")
		};
		$scope.tipoParcelamentoPercentual = {
				id: 2,
				label: $scope.getMensagem("%")
		};
		
		$scope.tipoParcelamentoValorFixo = {
				id: 3,
				label: $scope.getMensagem("valorFixo")
		};
		
		$scope.tipoParcelamentoPrioritario = {
				id: 4,
				label: $scope.getMensagem("prioritario")
		};
		
		$scope.desconto.tiposParcelamento = [$scope.tipoParcelamentoNumeroParcela,$scope.tipoParcelamentoPercentual ,$scope.tipoParcelamentoValorFixo,$scope.tipoParcelamentoPrioritario];	
		
		$scope.desconto.tipoParcelamentoSelecionado = {};
		
		$scope.initializeAbaCad($stateParams);	
		
		
		
		
	}
];
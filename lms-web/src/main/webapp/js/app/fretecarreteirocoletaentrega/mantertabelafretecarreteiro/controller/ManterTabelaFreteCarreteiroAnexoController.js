
var ManterTabelaFreteCarreteiroAnexoController  = [
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
		
		$scope.tabelaFreteCarreteiroCe = {};
		
		/**
		 * Carrega os arquivos, adicionando os novos se houverem.
		 */
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			var o = {};
			o.filtros = {};    			
			o.filtros.idTabelaFreteCarreteiroCe = $stateParams.id;
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
			$scope.tabelaFreteCarreteiroCe.files = [];
			$scope.tabelaFreteCarreteiroCe.bytea = [];
			$scope.tabelaFreteCarreteiroCe.descricao = null;
		};    		
		
		/**
		 * Verifica se os campos descricao e arquivo foram informados.
		 */
		$scope.isFileUpload = function(){
			if (!$scope.tabelaFreteCarreteiroCe.descricao) {
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
			file.dsAnexo = $scope.tabelaFreteCarreteiroCe.descricao;
			file.nmUsuario = $scope.tabelaFreteCarreteiroCe.nmUsuarioLogado;
			file.nmArquivo = selectedFile.name;
			file.dhCriacao = Utils.Date.formatMomentAsISO8601(moment());
			
			/*
			 * Mantem no scope o array de dados para ser enviado quando o
			 * usuario salvar o registro.
			 */
			$scope.tabelaFreteCarreteiroCe.files.push(file);
			$scope.tabelaFreteCarreteiroCe.bytea.push(selectedFile);
			   
			/*
			 * Zera configuracoes do componente. 
			 */
			$scope.fileUploadParams.clear();
			$scope.tabelaFreteCarreteiroCe.descricao = null;
			
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
			if($scope.tabelaFreteCarreteiroCe.files){    				
				$.each($scope.tabelaFreteCarreteiroCe.files, function(){
					$scope.anexosTableParams.list.unshift(this);
				});
				
				$scope.anexosTableParams.qtdRegistros += $scope.tabelaFreteCarreteiroCe.files.length;
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
				if(!this.idAnexoTabelaFreteCe){    					
					tmp.push($scope.tabelaFreteCarreteiroCe.files.indexOf(this));    					
				} else {
					ids.push(this.idAnexoTabelaFreteCe);	
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
	    					$scope.tabelaFreteCarreteiroCe.files.splice(this, 1);
	    					$scope.tabelaFreteCarreteiroCe.bytea.splice(this, 1);
	    				});
	    				
	    				$scope.rest.doPost("removeAnexoTabelaFreteRfcByIds",ids).then(function(data) {
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
			$scope.resetUpload();
			
			$scope.loadAnexosTableParams();
			$scope.findUsuarioLogado();
		};
		
		$scope.storeAnexos = function($event) {    			
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: "warning"}]);    				    				
				$event.preventDefault();
				return false;
			}
			
			$scope.doStoreAnexos();
		};
		
		$scope.getBean = function() {
			var o = { };
			
			o.idTabelaFreteCarreteiroCe = $stateParams.id;
			o.files	= $scope.tabelaFreteCarreteiroCe.files;
			
			return o;
		};  
		
		$scope.doStoreAnexos = function(){
			$rootScope.showLoading = true;
			
			$http({
                method: 'POST',
                url: contextPath+'rest/fretecarreteirocoletaentrega/tabelaFreteCarreteiroCe/storeAnexos',
                headers: { 'Content-Type': undefined},
                transformRequest: function (data) {
                    var formData = new FormData();
                    formData.append("data", angular.toJson(data));
                    
                    if(!angular.isUndefined(data.files)){	                      
	                      formData.append("qtdArquivos", data.files.length);
	                      
	                      for (var i = 0; i < data.files.length; i++) {
	                          formData.append("arquivo_" + i, $scope.tabelaFreteCarreteiroCe.bytea[i]);
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
			
			$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
		};
				
		$scope.findUsuarioLogado = function(){
			if(!$scope.tabelaFreteCarreteiroCe.idUsuarioLogado){
				UsuarioLmsFactory.findUsuarioLmsLogado().then(function(data) {
					$scope.tabelaFreteCarreteiroCe.idUsuarioLogado = data.idUsuario;
					$scope.tabelaFreteCarreteiroCe.nmUsuarioLogado = data.nmUsuario;
    			},function() {
    				
    			});		
			};
		};
		
		$scope.loadAbaAnexos();	
	}
];
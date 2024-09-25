
var ManterReciboComplementarPopupController = [
	"$q", 
	"$scope", 	
	"$controller",
	"ManterReciboComplementarFactory", 
	"$modalInstance", 
	"modalParams",
	"$rootScope",
	"TableFactory",
	"ManterReciboFactory",
	function($q, $scope, $controller, ManterReciboComplementarFactory, $modalInstance, modalParams,$rootScope,TableFactory,ManterReciboFactory) {    		
		
		// Inicializa a super classe ManterReciboComplementarController.
		angular.extend(this, $controller(ManterReciboComplementarCadController, {$scope: $scope}));
		    		
		$scope.loadMensagens("reciboComplementar");
		
		$scope.title = $scope.getMensagem("reciboComplementar");
		$scope.innerTemplate = contextPath+"js/app/fretecarreteiroviagem/manterrecibocomplementar/view/manterReciboComplementarCad.html";
		$scope.modalParams = modalParams;
		$scope.anexosTableParams = new TableFactory({ service: ManterReciboFactory.findAnexos, remotePagination: false });
		
		$scope.blockSuggest = true;
		$rootScope.popup = true;
		
		$scope.fileUploadParams = {};
		
		$scope.close = function() {
			$modalInstance.dismiss("cancel");				
		};
		
		/** Carrega dados do combobox de moedas */
		$scope.populateMoedas = function(){    			
			ManterReciboFactory.populateMoedas().then(function(data) {
				$scope.recibo.moedas = data;
			}); 
		};
		
		$scope.limparDados = function() {    			
			$scope.dados = {};
			$scope.dados.coletaEntrega = true;
			
			if($scope.recibo == undefined){
				$scope.recibo = {};
			}
			
			$scope.recibo.novo = true;	

			if($scope.anexosTableParams){
				$scope.anexosTableParams.clear();				
			}
			
			$scope.resetUpload();
			
			$scope.loadReciboComplementado($scope.modalParams.idReciboComplementado);
			
			$scope.populateMoedas();
		};
		
		/** Inicializa a modal para criar um novo registro de recibo complementar */
		$scope.create = function(id){
			$scope.limparDados();
			
			$scope.populateMoedas();
			
			$scope.loadReciboComplementado(id);
		};
		
		/** Inicializa a modal para visualizar/editar um registro de recibo complementar */
		$scope.find = function(id){		
			if($scope.recibo == undefined){
				$scope.recibo = {};
				$scope.recibo.novo = false;				
			}
			
			ManterReciboComplementarFactory.findById(id).then(function(data) {	    		    		
				$scope.dados = data;
				$scope.setReciboComplementado(data);

			}, function() {
				
			});
		};
		
		/** Define dados adicionais para a modal. */
		$scope.setDadosReciboModal = function(data){
			$scope.dados.reciboComplementado = data.recibo;
			
			$scope.dados.sgFilialReciboComplementado = data.recibo.sgFilial;
			
			$scope.dados.filialEmissao = data.filialEmissao;    			
		};
		
		/** Define comportamento da modal */
		if(modalParams.novo){	
			$scope.create($scope.modalParams.id);
		} else {
			$scope.find($scope.modalParams.id);
		}
		
		/** Define dados de acordo com os dados do recibo carregado na suggest */
		$scope.setReciboComplementado = function(data){
			if(angular.isUndefined($scope.dados)){
				return;
			}
			
    		$scope.dados.tpReciboFreteCarreteiro = data.tpReciboFreteCarreteiro;
			$scope.dados.tpReciboFreteCarreteiroValue = data.tpReciboFreteCarreteiroValue;
			$scope.dados.coletaEntrega = data.coletaEntrega;
    		
    		$scope.dados.nrIdentificacaoProprietarioFormatado = data.nrIdentificacaoProprietarioFormatado;
    		$scope.dados.nmPessoaProprietario = data.nmPessoaProprietario;        				
    		$scope.dados.nrFrota = data.nrFrota;
    		$scope.dados.nrIdentificador = data.nrIdentificador;
    		$scope.dados.dsMarcaMeioTransporte = data.dsMarcaMeioTransporte;
    		$scope.dados.dsModeloMeioTransporte = data.dsModeloMeioTransporte;
			$scope.dados.idControleCarga = data.idControleCarga;
			$scope.dados.nrControleCarga = data.nrControleCarga;
			$scope.dados.controleCargaSgFilial = data.controleCargaSgFilial;
			$scope.dados.idProprietarioComp = data.idProprietario;
			$scope.dados.idMotoristaComp = data.idMotorista;
			$scope.dados.idMeioTransporteComp = data.idMeioTransporte;
						
			$scope.loadAbaAnexos();
			
			$scope.setDadosReciboModal(data);
		};
		
		$scope.loadAbaAnexos = function() {
			$scope.loadAnexosTableParams();
			//$scope.findUsuarioLogado();
		};
		
		$scope.loadAnexosTableParams = function(){    			
			$rootScope.showLoading = true;
			
			if($scope.anexosTableParams == undefined){
				$scope.anexosTableParams = new TableFactory({ service: ManterReciboFactory.findAnexos, remotePagination: false });
			}
			
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
					window.open(contextPath+'picture?blob=' + data.blob);
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
	}
];


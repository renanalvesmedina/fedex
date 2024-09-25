
var ManterTabelaFreteCarreteiroController = [
	"$rootScope",
	"$scope", 
	"$location", 
	"FilialFactory",
	"modalService", 
	"$modal", 
 	function($rootScope, $scope, $location, FilialFactory,modalService,$modal) {
    	
		$scope.tabelafretecarreteiro = {};
		
		$scope.dataType = {
			rotas : {value: 'rota', type: 'RC', visible: 5},			
			proprietarios : {value: 'proprietario', type: 'PR', visible: 4},
			clientes : {value: 'cliente', type: 'CL', visible: 3},
			municipios : {value: 'municipio', type: 'MU', visible: 2},
			tiposMeioTransporte : {value: 'tipoMeioTransporte', type: 'TM', visible: 1},
			geral : {value: 'geral', type: 'GE', visible: 0},
			meiosTransporte : {value: 'meioTransporte', type: 'MT', visible: -1}
		};
		
		$scope.setConstructor({
			rest: "/fretecarreteirocoletaentrega/tabelaFreteCarreteiroCe"
		});
		
		$scope.createItemTabelaValor = function(idTabelaFreteCarreteiroCe){
			var defaultValue = 0.00;
			
			var tabelaFcValoresDTO = {};
			tabelaFcValoresDTO.idTabelaFreteCarreteiroCe = idTabelaFreteCarreteiroCe;
			tabelaFcValoresDTO.blTipo = { value: $scope.tabelafretecarreteiro.currentDataType.type };
			tabelaFcValoresDTO.listTabelaFcFaixaPeso = [];
			
			if(tabelaFcValoresDTO.blTipo.value === 'RC'){
				tabelaFcValoresDTO.filialRotaColetaEntrega = $scope.data.filial;
			}
			
			tabelaFcValoresDTO.idTabelaFreteCarreteiroCe = $scope.data.idTabelaFreteCarreteiroCe;
			tabelaFcValoresDTO.pcMercadoria = defaultValue;
			tabelaFcValoresDTO.pcFrete = defaultValue;
			tabelaFcValoresDTO.vlAjudante = defaultValue;
			tabelaFcValoresDTO.vlCapataziaCliente = defaultValue;
			tabelaFcValoresDTO.vlConhecimento = defaultValue;
			tabelaFcValoresDTO.vlDedicado = defaultValue;
			tabelaFcValoresDTO.vlDiaria = defaultValue;
			tabelaFcValoresDTO.vlEvento = defaultValue;
			tabelaFcValoresDTO.vlFreteMinimo = defaultValue;
			tabelaFcValoresDTO.vlHora = defaultValue;
			tabelaFcValoresDTO.vlKmExcedente = defaultValue;
			tabelaFcValoresDTO.vlLocacaoCarreta = defaultValue;
			tabelaFcValoresDTO.vlMercadoriaMinimo = defaultValue;
			tabelaFcValoresDTO.vlPalete = defaultValue;
			tabelaFcValoresDTO.vlPernoite = defaultValue;
			tabelaFcValoresDTO.vlPreDiaria = defaultValue;
			tabelaFcValoresDTO.vlPremio = defaultValue;
			tabelaFcValoresDTO.vlTransferencia = defaultValue;
			tabelaFcValoresDTO.vlVolume = defaultValue;
			tabelaFcValoresDTO.qtAjudante = defaultValue; 
			tabelaFcValoresDTO.pcFreteLiq = defaultValue;
			tabelaFcValoresDTO.vlFreteMinimoLiq = defaultValue;
			
			return tabelaFcValoresDTO;
		};
		
		/** Carrega dados da filial do usuario logado */
		$scope.loadCurrentFilial = function(object){
			$rootScope.showLoading = true;
			
			FilialFactory.findFilialUsuarioLogado().then(function(data) {
    			if(!data.isMatriz){
    				object.filial = data;
    			}
    			
    			$scope.tabelafretecarreteiro.isMatriz = data.isMatriz;
    			
    			$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.openGeral = function(tabelaFcValoresDTO){			
			var modalInstance = $modal.open({
				controller: ManterTabelaFreteCarreteiroModalController,
				templateUrl: contextPath+"js/common/modal/view/modal-template.html",
				windowClass: "modal-detail",
				resolve: {
	    			modalParams: function() {
        		    	return { tabelaFcValoresDTO: tabelaFcValoresDTO, 
        		    		listTipoMeioTransporte: $scope.tabelafretecarreteiro.listTipoMeioTransporte, 
        		    		type: $scope.tabelafretecarreteiro.currentDataType,
        		    		disabled: $scope.data.disabled,
        		    		isMatriz: $scope.tabelafretecarreteiro.isMatriz };
	    			}
	    		}}
			);
			
			return modalInstance;
		};
		
		$scope.findTabelaFreteCarreteiroCe = function(id){
			$scope.loadCurrentFilial($scope.data);		
			
			$rootScope.showLoading = true;
			
			$scope.findById(id).then(function(data) {						
				$scope.setData(data);
				
				$scope.tabelafretecarreteiro.dtAtual = Utils.Date.formatMomentAsISO8601(moment());
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});				
		};
		
		/**
		 * Verifica se algum dos valores da tabela de valores foi definido acima
		 * de zero.
		 */
		$scope.isZeroTabelaFcValores = function(tabelaFcValores){	
			$scope.rest.doPost("checkZeroTabelaFcValores", tabelaFcValores).then(function(data) {	
				return data === 'true';			
			},function() {
				return false;
			});
		};
		
		/** Carrega dados do select one de tipos de meio de transporte */
		$scope.populateTipoMeioTransporte = function(){
			if($scope.tabelafretecarreteiro.listTipoMeioTransporte){
				return;
			}
			
			$rootScope.showLoading = true;
			
			$scope.rest.doGet("populateTipoMeioTransporte").then(function(data) {
				$scope.tabelafretecarreteiro.listTipoMeioTransporte = data;
				
				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.populateTipoMeioTransporte();
	}
];

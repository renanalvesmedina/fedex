
var ManterTabelaFreteCarreteiroModalController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$state",
	"TableFactory",
	"RestAccessFactory",
	"$modal",
	"$modalInstance",
	"translateService",
	"modalService",
	"modalParams",
	function($rootScope, $scope, $stateParams, $state, TableFactory, RestAccessFactory, $modal, $modalInstance, translateService, modalService, modalParams) {
		$scope.currentDataType = modalParams.type;
		$scope.listTipoMeioTransporte = modalParams.listTipoMeioTransporte;
		$scope.data = modalParams.tabelaFcValoresDTO;
		
		$scope.innerTemplate = contextPath+"js/app/fretecarreteirocoletaentrega/mantertabelafretecarreteiro/view/popup/manterTabelaFreteCarreteiroPrecos.html";
		$scope.rest = RestAccessFactory.create("/fretecarreteirocoletaentrega/tabelaFreteCarreteiroCe");
		
		$scope.faixa = {};
		$scope.psInicial = 0.001;
		
		$scope.isIdentificacaoOpen = true;
		$scope.isValoresPrecosOpen = true;
		$scope.isFaixasPesoOpen = true;
		$scope.isValorMercadoriaOpen = true;
		$scope.isValorFreteOpen = true;
		$scope.faixa.blCalculoFaixaUnica = false;
		$scope.isReadOnly = false;
		$scope.getTitle = function(){
			if(modalParams.nmTabela){
				return $scope.getMensagem("tabelaPrecos") + ": " + modalParams.nmTabela;
			} else {
				return $scope.getMensagem("tabelaPrecos") + ": " + $scope.getMensagem($scope.currentDataType.value);
			}
		};
		
		$scope.title = $scope.getTitle();
		
		$scope.close = function() {
			$modalInstance.dismiss("close");
		};
		
		$scope.salvarPrecos = function(){
			$scope.rest.doPost("checkZeroTabelaFcValores", $scope.data).then(function(data) {	
				$rootScope.showLoading = false;
				
				if(data === 'true'){
					modalService.open({confirm: true, title: $scope.getMensagem("confirmacao"), message: $scope.getMensagem("LMS-25111"), windowClass: 'modal-confirm'})
					.then(function() {
						$scope.doModalStore();
					});
				} else {
					$scope.doModalStore();
				}
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.doModalStore = function(){
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("storeTabelaFcValores", $scope.data).then(function(data) {
				$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
				
				$modalInstance.dismiss("salvar");						
				
				$rootScope.showLoading = false;
			},function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.addFaixa = function(){						
			 if(!$scope.faixa.psFinal || $scope.faixa.psFinal <= $scope.faixa.psInicial){
				 $scope.addAlerts([{msg: "LMS-25059", type: MESSAGE_SEVERITY.WARNING}]);
			 } else {					 
				 if($scope.faixa.vlValor == undefined || $scope.faixa.vlValor == null || $scope.faixa.vlValor == ""){					 
					 $scope.addAlerts([{msg: translateService.getMensagem("LMS-00001", $scope.getMensagem("valor")), type: MESSAGE_SEVERITY.WARNING}]);
				 } else {								 
					 var row = {};

					 if(!$scope.faixa.blCalculoFaixaUnica) {
						 if ($scope.data.listTabelaFcFaixaPeso.length === 0) {
							 row.tpFator = {value: 'D'};
						 } else {
							 row.tpFator = {value: 'K'};
						 }
					 }else{
						 if(!$scope.faixa.tpFator){
							 $scope.addAlerts([{msg: translateService.getMensagem("LMS-00001", $scope.getMensagem("Primeiro Tipo Fator")), type: MESSAGE_SEVERITY.WARNING}]);
							 return;
						 }

						 if($scope.faixa.tpFatorSegundo && !$scope.faixa.vlValorSegundo){
							 $scope.addAlerts([{msg: translateService.getMensagem("LMS-00001", $scope.getMensagem("Segundo Valor")), type: MESSAGE_SEVERITY.WARNING}]);
							 return;
						 }

						 if($scope.faixa.vlValorSegundo && !$scope.faixa.tpFatorSegundo) {
							 $scope.addAlerts([{msg: translateService.getMensagem("LMS-00001", $scope.getMensagem("Segundo Tipo Fator")), type: MESSAGE_SEVERITY.WARNING}]);
							 return;
						 }

						 if ($scope.faixa.tpFatorSegundo && $scope.faixa.tpFator.id === $scope.faixa.tpFatorSegundo.id) {
							 $scope.addAlerts([{
								 msg: translateService.getMensagem("LMS-25135", $scope.getMensagem("Tipo Fator")),
								 type: MESSAGE_SEVERITY.WARNING
							 }]);
							 return;
						 }
					 	row.tpFator = $scope.faixa.tpFator;
					 }
					 
					 row.tabelaFcValores = { idTabelaFcValores: $scope.data.idTabelaFcValores };
					 row.psInicial =  $scope.faixa.psInicial;
					 row.psFinal   =  $scope.faixa.psFinal;
					 row.vlValor   =  $scope.faixa.vlValor;

					 row.blCalculoFaixaUnica =  $scope.faixa.blCalculoFaixaUnica;
					 row.tpFatorSegundo   =  $scope.faixa.tpFatorSegundo;
					 row.vlValorSegundo   =  $scope.faixa.vlValorSegundo;
					 
					 $scope.data.listTabelaFcFaixaPeso.push(row);
					 $scope.isReadOnly = true;
					 $scope.limparFaixa($scope.faixa.psFinal);
				 }
			 }
		};
		
		$scope.removeFaixa = function(row){
			var index = $scope.data.listTabelaFcFaixaPeso.indexOf(row);
			$scope.data.listTabelaFcFaixaPeso.splice(index, 1); 
			
			$scope.psInicial = row.psInicial;
			$scope.faixa.psInicial = $scope.psInicial;
			$scope.getReadOnly();
		};
				
		$scope.limparFaixa = function(atual){
			const blCalculoFaixaUnica = $scope.faixa.blCalculoFaixaUnica;
			$scope.faixa = {};
			$scope.faixa.blCalculoFaixaUnica = blCalculoFaixaUnica;
			$scope.faixa.psInicial = atual + 0.001;
			$scope.psInicial = $scope.faixa.psInicial;
		};					
						
		$scope.changePais = function(){
			$scope.data.unidadeFederativa = null;
			
			$scope.changeUnidadeFederativa();
		};
		
		$scope.changeUnidadeFederativa = function(){
			$scope.data.municipio = null;
		};
		
		$scope.removeTabelaValoresById = function() {
			$scope.confirm($scope.getMensagem("erExcluirRegistroAtual")).then(function() {
				$rootScope.showLoading = true;
				    				
				$scope.rest.doGet("removeTabelaValoresById?id=" + $scope.data.idTabelaFcValores).then(function(data) {    					
    				$rootScope.showLoading = false;
    					
					$scope.addAlerts([{msg: "LMS-00054", type: "success"}]);
					
					$scope.close();
				}, function() {
					$rootScope.showLoading = false;
				});
			});
		};
		
		$scope.initModal = function(){
			$scope.isGeral =  $scope.currentDataType.type == 'GE';			
			$scope.disabled = modalParams.disabled || !modalParams.isMatriz;	
			
			$scope.data.listTabelaFcFaixaPeso = !$scope.data.listTabelaFcFaixaPeso ? [] : $scope.data.listTabelaFcFaixaPeso;
			
			var length = $scope.data.listTabelaFcFaixaPeso.length;
			
			if(length > 0){
				$scope.psInicial = $scope.data.listTabelaFcFaixaPeso[length - 1].psFinal;
				$scope.faixa.blCalculoFaixaUnica = $scope.data.listTabelaFcFaixaPeso[0].blCalculoFaixaUnica;
				$scope.isReadOnly = true;
			} else {
				$scope.psInicial = 0.000;
			}
			
			$scope.limparFaixa($scope.psInicial);
		};

		$scope.clearSecondComponent = function(){
			if(!$scope.faixa.blCalculoFaixaUnica){
				$scope.faixa.tpFatorSegundo = null;
				$scope.faixa.vlValorSegundo = null;
				$scope.faixa.tpFator = null;

			}
		};

		$scope.getReadOnly = function() {
			if (!$scope.data.listTabelaFcFaixaPeso.length) {
				$scope.faixa.blCalculoFaixaUnica = false;
				$scope.isReadOnly = false;
			}
		}
		$scope.initModal();
	}

];

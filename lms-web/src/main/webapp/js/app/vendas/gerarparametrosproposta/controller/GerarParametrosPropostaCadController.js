var GerarParametrosPropostaCadController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"$location",
	"editTabState",
	"ClienteService",
	"translateService",
	function($scope, $rootScope, $stateParams, $location, editTabState, ClienteService, translateService) {
		
		$scope.unidadesFederativas = [];
		$scope.tiposLocalizacao = [];
		$scope.vlAdValoremDisabled = false;
		$scope.vlFreteMinimoDisabled = false;
		$scope.vlFretePesoDisabled = false;
		$scope.pcDiferencaAdvaloremmDisabled = false;
		
		function initializeAbaCad(params){
			$scope.setDisableAbaDest(true);
			$scope.setDisableAbaDestAereo(true);
			$scope.setDisableAbaDestConv(true);
			initComboUnidadesFederativas();
			initComboTiposLocalizacao();
			
			//usado para desabilitar todos os campos, baseado no tipo de proposta
			$scope.disabledByTpGeracaoProposta = false;
			
			if ($scope.simulacao){
				$scope.data.id = $scope.simulacao.id;
				findPersistedData();
				
				if ($scope.simulacao.tpGeracaoProposta.value == 'M' || $scope.simulacao.tpGeracaoProposta.value == 'I'){
					$scope.disabledByTpGeracaoProposta = true;
					$scope.setDisableAbaDestAereo(false);
				}else if($scope.simulacao.tpGeracaoProposta.value == 'V'){
					$scope.setDisableAbaDestConv(false);
				}
				
			}else{
				setDefaultValues();
				validateFields();
			}
		}
		
		function initComboUnidadesFederativas(){
			$scope.rest.doPost("findComboUnidadesFederativas").then(function (unidadesFederativas){
				$scope.unidadesFederativas = unidadesFederativas;
			});
		}

		function initComboTiposLocalizacao(){
			$scope.rest.doPost("findComboTipoLocalizacaoMunicipio").then(function (tiposLocalizacao){
				$scope.tiposLocalizacao = tiposLocalizacao;
			});
		}
		
		function findPersistedData(){
			$rootScope.showLoading = true;
			$scope.rest.doPost("findPropostaByIdSimulacao",$scope.data.id).then(function (data){
				$scope.data = data;
				$scope.ufOrigemDisabled = true;
				findPercentualDefaultValues()
				if (!$scope.data.idProposta){
					setDefaultValues();
					$scope.ufOrigemDisabled = false;
				}
				validateFields();
				$rootScope.showLoading = false;
			},function(){
				$rootScope.showLoading = false;
			});
		}
		
		
		function setDefaultValues(){
			
			//Sessão Frete peso
			$scope.data.tpIndicadorMinFretePeso = getDomainValue("T");
			$scope.data.vlMinFretePeso = 0;

			$scope.data.tpIndicadorFreteMinimo = getDomainValue("D");
			$scope.data.vlFreteMinimo = 0;

			$scope.data.tpIndicadorFretePeso = getDomainValue("D");
			$scope.data.vlFretePeso = 0;
			
			$scope.data.pcDiferencaFretePeso = 0;
			
			//Sessao frete valor
			$scope.data.tpIndicadorAdvalorem = getDomainValue("T");
			$scope.data.vlAdvalorem = 0;
			
			$scope.data.tpDiferencaAdvalorem = getDomainValue("P");
			$scope.data.pcDiferencaAdvalorem= 0;
			
			findPercentualDefaultValues();
		}
		
		function findPercentualDefaultValues(){
			$scope.data.pcFretePercentual = 0;
			$scope.data.vlMinimoFretePercentual = 0;
			$scope.data.vlToneladaFretePercentual = 0;
			$scope.data.psFretePercentual = 0;

		}
		
		function validateFields(){
			var isValid = true;
			
			isValid = isValid && $scope.validateFretePeso();
			isValid = isValid && $scope.validateMinimoFretePeso();
			isValid = isValid && $scope.validateVlFreteMinimo();
			isValid = isValid && $scope.validateAdvalorem();
			
			return isValid;
		}
		
		function getDomainValue(value){
			var domain = new Object();
			domain.value = value;
			return domain;
		}
		

		function btnContinuar(){
			$scope.setGeracaoParametroPropostaDTO($scope.data);
			$scope.setDisableAbaDest(false);
			$scope.setDisableAbaDestAereo(true);
			var path = "/app/vendas/gerarParametrosProposta/dest/"+$scope.data.idSimulacao;
			$location.path(path);
		}
		
		$scope.continuar = function(){
			btnContinuar();
		};
		
		function validateFlagsExpedicao(){
			if (!$scope.data.blFreteExpedido && !$scope.data.blFreteRecebido){
				$scope.addAlerts([{msg: "LMS-01051: "+$scope.getMensagem("LMS-01051"), type: 'danger'}]);
				return false;
			}
			return true;
		}
		
		$scope.validateMinimoFretePeso = function(){
			if (!$scope.data.tpIndicadorMinFretePeso){
				return;
			}
			
			if ($scope.data.tpIndicadorMinFretePeso.value == "T"){
				$scope.vlMinFretePesoDisabled = true;
				$scope.data.vlMinFretePeso = 0;
			}else{
				$scope.vlMinFretePesoDisabled = false;
				if ($scope.data.vlMinFretePeso < 0){
					addAlert("LMS-01050","minimoFretePeso");
					return false;
				}
			}
			
			return true;
		};
	
		$scope.validateVlFreteMinimo = function(){
			if (!$scope.data.tpIndicadorFreteMinimo){
				return;
			}
			
			if ($scope.data.vlFreteMinimo < 0){
				addAlert("LMS-01050","percentualMinimo");
				return false;
			}
			
			if ($scope.data.tpIndicadorFreteMinimo.value == "D" && !isPercent($scope.data.vlFreteMinimo)){
				addAlert("LMS-01050","percentualMinimo");
				return false;
			}
			return true;
		};
		
		$scope.validateFretePeso = function(){
			if (!$scope.data.tpIndicadorFretePeso){
				return;
			}
			
			if ($scope.data.tpIndicadorFretePeso.value == "D" && !isPercent($scope.data.vlFretePeso)){
				addAlert("LMS-01050","fretePeso");
				return false;
			}
			
			if ($scope.data.tpIndicadorFretePeso.value == "T"){
				$scope.vlFretePesoDisabled = true;
				$scope.data.vlFretePeso =0;
			}else{
				$scope.vlFretePesoDisabled = false;
				if ($scope.data.vlFretePeso < 0){
					addAlert("LMS-01050","fretePeso");
					return false;
				}
			}
			return true;
		};
		
		$scope.validateAdvalorem = function(){
			if (!$scope.data.tpIndicadorAdvalorem){
				return;
			}
			$scope.vlAdValoremDisabled = false;
			if ($scope.data.tpIndicadorAdvalorem.value == "T"){
				$scope.vlAdValoremDisabled = true;
				$scope.data.vlAdvalorem =0;
			}else if(($scope.data.tpIndicadorAdvalorem.value == "D" && !isPercent($scope.data.vlAdvalorem)) || $scope.data.vlAdvalorem < 0){
				addAlert("LMS-01050","advalorem");
				return false;
			}
			return true;
		};

		$scope.validateDiferencaAdvalorem = function(){
			if (!$scope.data.tpDiferencaAdvalorem){
				return;
			}
			
			if ($scope.data.tpIndicadorAdvalorem.value == "T"){
				$scope.pcDiferencaAdvaloremDisabled = true;
				$scope.data.pcDiferencaAdvalorem =0;
			}else{
				$scope.pcDiferencaAdvaloremmDisabled = false;
				if ($scope.data.pcDiferencaAdvalorem < 0){
					addAlert("LMS-01050","tipoDiferencaCapitalInterior");
					return false;
				}
			}
			return true;
		};
		
		
		
		function addAlert(key,fieldLabel){
			$scope.addAlerts([{msg: $scope.getMensagem(key,[translateService.getMensagem(fieldLabel)]), type: 'danger'}]);
		}
		
		function isPercent(val) {
			return !( (val < 0) || (val > 100) );
		}
		
		function isIn(val, arr){
			for(var k in arr) if(arr[k] == val) return true;
			return false;
		}
		
		initializeAbaCad($stateParams);
	}
	
];


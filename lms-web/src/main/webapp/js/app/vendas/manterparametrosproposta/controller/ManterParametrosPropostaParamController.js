var ManterParametrosPropostaParamController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"$location",
	"editTabState",
	"ClienteService",
	"translateService",
	function($scope, $rootScope, $stateParams, $location, editTabState, ClienteService, translateService) {
		
		function initializeAbaParam(params){
			if(!!params.id){
				$scope.setDisableAbaTaxas(false);
				$scope.setDisableAbaGeneralidades(false);
			}else{
				$scope.setDisableAbaTaxas(true);
				$scope.setDisableAbaGeneralidades(true);
			}
			$scope.setDisableAbaParam(false);
			if ($scope.$parent.parametroPropostaDTO) {
				$scope.data = $scope.$parent.parametroPropostaDTO;
				$scope.rest.doPost("ajustaDsRota", $scope.data).then(function(parametroPropostaDTO) {
					$scope.data = parametroPropostaDTO;
				}, function() {
				});
				
				$scope.validateParametroCliente();
			}
			$scope.setDisableBotoesCrud(!!$scope.data.tpGeracaoProposta && "O" == $scope.data.tpGeracaoProposta.value);
		}

		$scope.storeParam = function($event) {
			
			$scope.validateParametroCliente();

			$rootScope.showLoading = true;
			$scope.rest.doPost("storeParam", $scope.data).then(function(parametroPropostaDTO) {
				$scope.showSuccessMessage();
				$scope.carregaCabecalho(parametroPropostaDTO);
				$scope.setParametroPropostaDTO(parametroPropostaDTO);
				$rootScope.showLoading = false;
				$location.path("/app/vendas/manterParametrosProposta/param/"+parametroPropostaDTO.id);
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.validateParametroCliente = function() {
			$scope.validateMinimoFretePeso();
			$scope.validateFretePeso();
			$scope.validateMinimoFreteQuilo();
			$scope.validatePagaPesoExcedente();
			$scope.validateTarifaMinima();
			$scope.validateFreteVolume();
			$scope.validateTarifaEspecifica();
			$scope.validateAdvalorem1();
			$scope.validateAdvalorem2();
			$scope.validateValorReferencia();
			$scope.validatePercentualFretePercentual();
			$scope.validateFretePercentual();
			$scope.validatePercentualGris();
			$scope.validateMinimoGris();
			$scope.validatePedagio();
			$scope.validatePercentualTrt();
			$scope.validateMinimoTrt();
			$scope.validatePercentualTde();
			$scope.validateMinimoTde();
			$scope.validatePercentualDescontoFreteTotal();
		};
		
		/************************************************
		* Metodos do grupo Frete Peso                   *
		/************************************************/
		$scope.validateMinimoFretePeso = function(){
			if (!$scope.data.tpIndicadorMinFretePeso){
				return;
			}
			
			$scope.vlMinFretePesoDisabled = false;
			if (isTabela($scope.data.tpIndicadorMinFretePeso)){
				$scope.vlMinFretePesoDisabled = true;
				$scope.data.vlMinFretePeso = 0;
			}
			return true;
		};
		
		$scope.vlPercMinimoProgrValidate = function(modelValue, viewValue){
			var retorno = {isValid: true, messageKey: 'LMS-01050', messageParams: ['% mínimo progressivo']};

			if(angular.isDefined($scope.data.tpIndicadorPercMinimoProgr) && angular.isDefined(viewValue)){
				var tpIndicador = $scope.data.tpIndicadorPercMinimoProgr.value;

				if((tpIndicador == "D" && (viewValue < 0 || viewValue > 100))){
					retorno.isValid = false;
				}
			}

			return retorno;
		};
		
		$scope.validateFretePeso = function(){
			if (!$scope.data.tpIndicadorFretePeso){
				return;
			}
			
			$scope.vlFretePesoDisabled = false;
			if (isTabela($scope.data.tpIndicadorFretePeso)){
				$scope.vlFretePesoDisabled = true;
				$scope.data.vlFretePeso = 0;
				return true;
			}
			
			if(isValor($scope.data.tpIndicadorFretePeso)) {
				if( !(isDesconto($scope.data.tpIndicadorPercMinimoProgr) && eqZero($scope.data.vlPercMinimoProgr)) ) {
					addAlert("LMS-01040","fretePeso");
					return false;
				}
			}
			
			if (isDesconto($scope.data.tpIndicadorFretePeso)){
				if(!isPercent($scope.data.vlFretePeso)){
					addAlert("LMS-01050","fretePeso");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorFretePeso) || isValor($scope.data.tpIndicadorFretePeso)) &&$scope.data.vlFretePeso < 0){
				addAlert("LMS-01050","fretePeso");
				return false;
			}
			
			return true;
		};
		
		$scope.validateMinimoFreteQuilo = function() {
			if (!$scope.data.vlMinimoFreteQuilo){
				return;
			}
			
			if ($scope.data.vlMinimoFreteQuilo < 0){
				addAlert("LMS-01050","valorMinimoFreteQuilo");
				return false;
			}
			
			if (!eqZero($scope.data.vlMinimoFreteQuilo)){
				if (!$scope.data.tpIndicadorFretePeso && !isValor($scope.data.tpIndicadorFretePeso)) {
					addAlert("LMS-01040","valorMinimoFreteQuilo");
					return false;
				}
			}
			return true;
		};
		
		$scope.validatePagaPesoExcedente = function () {
			if (!$scope.data.blPagaPesoExcedente){
				return;
			}
			
			if($scope.data.blPagaPesoExcedente === true) {
				if(eqZero($scope.data.vlMinimoFreteQuilo) || !isPeso($scope.data.tpIndicadorMinFretePeso)) {
					addAlert("LMS-01040","pagaPesoExcedente");
					return false;
				}
			}
			return true;
		};
		
		$scope.validateTarifaMinima = function () {
			if (!$scope.data.tpTarifaMinima){
				return;
			}
			
			$scope.vlTarifaMinimaDisabled = false;
			if (isTabela($scope.data.tpTarifaMinima)){
				$scope.vlTarifaMinimaDisabled = true;
				$scope.data.vlTarifaMinima = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpTarifaMinima)){
				if(!isPercent($scope.data.vlTarifaMinima)) {
					addAlert("LMS-01050","tarifaMinima");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpTarifaMinima) || isValor($scope.data.tpTarifaMinima)) && $scope.data.vlTarifaMinima < 0){
				addAlert("LMS-01050","tarifaMinima");
				return false;
			}
			return true;
		};
		
		$scope.validateFreteVolume = function () {
			if(!$scope.data.vlFreteVolume){
				return;
			}
			
			if($scope.data.vlFreteVolume < 0) {
				addAlert("LMS-01050","valorFreteVolume");
				return false;
			}
			
			if(!eqZero($scope.data.vlFreteVolume)) {
				var tpFretePeso = $scope.data.tpIndicadorFretePeso;
				var tpPercentualMinimoProgressivo = $scope.data.tpIndicadorPercMinimoProgr;
				var vlPercentualMinimoProgressivo = $scope.data.vlPercMinimoProgr;
				if(!(isTabela(tpFretePeso) && isDesconto(tpPercentualMinimoProgressivo) && eqZero(vlPercentualMinimoProgressivo)) ) {
					addAlert("LMS-01040","valorFreteVolume");
					return false;
				}
			}
			return true;
		};
		
		$scope.validateTarifaEspecifica = function () {
			if (!$scope.data.tpIndicVlrTblEspecifica){
				return;
			}
			
			$scope.vlTblEspecificaDisabled = false;
			if (isTabela($scope.data.tpIndicVlrTblEspecifica)){
				$scope.vlTblEspecificaDisabled = true;
				$scope.data.vlTblEspecifica = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicVlrTblEspecifica)){
				if(!isPercent($scope.data.vlTblEspecifica)) {
					addAlert("LMS-01050","valorTarifaEspecifica");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicVlrTblEspecifica) || isValor($scope.data.tpIndicVlrTblEspecifica)) && $scope.data.vlTarifaMinima < 0){
				addAlert("LMS-01050","valorTarifaEspecifica");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo Frete Peso                   *
		/************************************************/
		
		/************************************************
		* Metodos do grupo Frete Valor                  *
		/************************************************/
		$scope.validateAdvalorem1 = function () {
			if (!$scope.data.tpIndicadorAdvalorem){
				return;
			}
			
			$scope.vlAdvaloremDisabled = false;
			if (isTabela($scope.data.tpIndicadorAdvalorem)){
				$scope.vlAdvaloremDisabled = true;
				$scope.data.vlAdvalorem = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorAdvalorem)){
				if(!isPercent($scope.data.vlAdvalorem)) {
					addAlert("LMS-01050","advalorem1");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorAdvalorem) || isValor($scope.data.tpIndicadorAdvalorem)) && $scope.data.vlAdvalorem < 0){
				addAlert("LMS-01050","advalorem1");
				return false;
			}
			return true;
		};
		
		$scope.validateAdvalorem2 = function () {
			if (!$scope.data.tpIndicadorAdvalorem2){
				return;
			}
			
			$scope.vlAdvalorem2Disabled = false;
			if (isTabela($scope.data.tpIndicadorAdvalorem2)){
				$scope.vlAdvalorem2Disabled = true;
				$scope.data.vlAdvalorem2 = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorAdvalorem2)){
				if(!isPercent($scope.data.vlAdvalorem2)) {
					addAlert("LMS-01050","advalorem2");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorAdvalorem2) || isValor($scope.data.tpIndicadorAdvalorem2)) && $scope.data.vlAdvalorem2 < 0){
				addAlert("LMS-01050","advalorem2");
				return false;
			}
			return true;
		};
		
		$scope.validateValorReferencia = function () {
			if (!$scope.data.tpIndicadorValorReferencia){
				return;
			}
			
			$scope.vlValorReferenciaDisabled = false;
			if (isTabela($scope.data.tpIndicadorValorReferencia)){
				$scope.vlValorReferenciaDisabled = true;
				$scope.data.vlValorReferencia = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorValorReferencia)){
				if(!isPercent($scope.data.vlValorReferencia)) {
					addAlert("LMS-01050","valorReferencia");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorValorReferencia) || isValor($scope.data.tpIndicadorValorReferencia)) && $scope.data.vlValorReferencia < 0){
				addAlert("LMS-01050","valorReferencia");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo Frete Valor                  *
		/************************************************/
		
		/************************************************
		* Metodos do grupo Frete Percentual             *
		/************************************************/
		$scope.validatePercentualFretePercentual = function () {
			
			if(eqZero($scope.data.pcFretePercentual) || !$scope.data.pcFretePercentual) {
				$scope.data.vlMinimoFretePercentual = 0;
				$scope.data.vlToneladaFretePercentual = 0;
				$scope.data.psFretePercentual = 0;
			}else{
				var tpAdvalorem1 = $scope.data.tpIndicadorAdvalorem;
				var tpAdvalorem2 = $scope.data.tpIndicadorAdvalorem2;
				var tpIndicadorMinFretePeso = $scope.data.tpIndicadorMinFretePeso;
				var vlPercentualMinimoProgressivo = $scope.data.vlPercMinimoProgr;
				var tpIndicadorFretePeso = $scope.data.tpIndicadorFretePeso;
				var vlMinimoFreteQuilo = $scope.data.vlMinimoFreteQuilo;
				var tpTarifaMinima = $scope.data.tpTarifaMinima;
				var vlFreteVolume = $scope.data.vlFreteVolume;
				var tpTarifaEspecifica = $scope.data.tpIndicVlrTblEspecifica;
				if( (!isTabela(tpAdvalorem1) || !isTabela(tpAdvalorem2)) ||
					(!isTabela(tpIndicadorMinFretePeso) || !isTabela(tpIndicadorFretePeso) || !isTabela(tpTarifaMinima) || !isTabela(tpTarifaEspecifica)) ||
					(!eqZero(vlPercentualMinimoProgressivo) || !eqZero(vlMinimoFreteQuilo) || !eqZero(vlFreteVolume) )
				) {
					addAlert("LMS-01040","pcFretePercentual");
					return false;
				}
			}
			return true;
		};
		
		$scope.validateFretePercentual = function() {
			var vlMinimoFretePercentual = $scope.data.vlMinimoFretePercentual;
			var vlToneladaFretePercentual = $scope.data.vlToneladaFretePercentual;
			var psFretePercentual = $scope.data.psFretePercentual;
			var vlPercentualFretePercentual = $scope.data.pcFretePercentual;
			if( (!eqZero(vlMinimoFretePercentual) || !eqZero(vlToneladaFretePercentual) || !eqZero(psFretePercentual)) && eqZero(vlPercentualFretePercentual) ) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-01045"), type: 'danger'}]);
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo Frete Percentual             *
		/************************************************/
		
		/************************************************
		* Metodos do grupo GRIS                         *
		/***********************************************/
		$scope.validatePercentualGris = function () {
			if (!$scope.data.tpIndicadorPercentualGris){
				return;
			}
			
			$scope.vlPercentualGrisDisabled = false;
			if (isTabela($scope.data.tpIndicadorPercentualGris)){
				$scope.vlPercentualGrisDisabled = true;
				$scope.data.vlPercentualGris = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorPercentualGris)){
				if(!isPercent($scope.data.vlPercentualGris)) {
					addAlert("LMS-01050","percentualGris");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorPercentualGris) || isValor($scope.data.tpIndicadorPercentualGris)) && $scope.data.vlPercentualGris < 0){
				addAlert("LMS-01050","percentualGris");
				return false;
			}
			
			if(!isPercent($scope.data.pcCobrancaDevolucoes)) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-01070"), type: 'danger'}]);
				return false;
			}
			return true;
		};
		
		$scope.validateMinimoGris = function () {
			if (!$scope.data.tpIndicadorMinimoGris){
				return;
			}
			
			$scope.vlMinimoGrisDisabled = false;
			if (isTabela($scope.data.tpIndicadorMinimoGris)){
				$scope.vlMinimoGrisDisabled = true;
				$scope.data.vlMinimoGris = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorMinimoGris)){
				if(!isPercent($scope.data.vlMinimoGris)) {
					addAlert("LMS-01050","minimoGris");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorMinimoGris) || isValor($scope.data.tpIndicadorMinimoGris)) && $scope.data.vlMinimoGris < 0){
				addAlert("LMS-01050","minimoGris");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo GRIS                         *
		/***********************************************/
		
		/************************************************
		* Metodos do grupo Pedagio                      *
		/************************************************/
		$scope.validatePedagio = function () {
			if (!$scope.data.tpIndicadorPedagio){
				return;
			}
			
			$scope.vlPedagioDisabled = false;
			if (isTabela($scope.data.tpIndicadorPedagio)){
				$scope.vlPedagioDisabled = true;
				$scope.data.vlPedagio = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorPedagio)){
				if(!isPercent($scope.data.vlPedagio)) {
					addAlert("LMS-01050","valorPedagio");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorPedagio) || isValor($scope.data.tpIndicadorPedagio)) && $scope.data.vlPedagio < 0){
				addAlert("LMS-01050","valorPedagio");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo Pedagio                      *
		/************************************************/
		
		/************************************************
		* Metodos do grupo TRT                         *
		/************************************************/
		$scope.validatePercentualTrt = function () {
			if (!$scope.data.tpIndicadorPercentualTrt){
				return;
			}
			
			$scope.vlPercentualTrtDisabled = false;
			if (isTabela($scope.data.tpIndicadorPercentualTrt)){
				$scope.vlPercentualTrtDisabled = true;
				$scope.data.vlPercentualTrt = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorPercentualTrt)){
				if(!isPercent($scope.data.vlPercentualTrt)) {
					addAlert("LMS-01050","percentualTrt");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorPercentualTrt) || isValor($scope.data.tpIndicadorPercentualTrt)) && $scope.data.vlPercentualTrt < 0){
				addAlert("LMS-01050","percentualTrt");
				return false;
			}
			
			if(!isPercent($scope.data.pcCobrancaDevolucoes)) {
				$scope.addAlerts([{msg: $scope.getMensagem("LMS-01070"), type: 'danger'}]);
				return false;
			}
			return true;
		};
		
		$scope.validateMinimoTrt = function () {
			if (!$scope.data.tpIndicadorMinimoTrt){
				return;
			}
			
			$scope.vlMinimoTrtDisabled = false;
			if (isTabela($scope.data.tpIndicadorMinimoTrt)){
				$scope.vlMinimoTrtDisabled = true;
				$scope.data.vlMinimoTrt = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorMinimoTrt)){
				if(!isPercent($scope.data.vlMinimoTrt)) {
					addAlert("LMS-01050","minimoTrt");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorMinimoTrt) || isValor($scope.data.tpIndicadorMinimoTrt)) && $scope.data.vlMinimoTrt < 0){
				addAlert("LMS-01050","minimoTrt");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo TRT                         *
		/************************************************/
		
		/************************************************
		* Metodos do grupo TDE                          *
		/************************************************/
		$scope.validatePercentualTde = function () {
			if (!$scope.data.tpIndicadorPercentualTde){
				return;
			}
			
			$scope.vlPercentualTdeDisabled = false;
			if (isTabela($scope.data.tpIndicadorPercentualTde)){
				$scope.vlPercentualTdeDisabled = true;
				$scope.data.vlPercentualTde = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorPercentualTde)){
				if(!isPercent($scope.data.vlPercentualTde)) {
					addAlert("LMS-01050","percentualTde");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorPercentualTde) || isValor($scope.data.tpIndicadorPercentualTde)) && $scope.data.vlPercentualTde < 0){
				addAlert("LMS-01050","percentualTde");
				return false;
			}
			return true;
		};
		
		$scope.validateMinimoTde = function () {
			if (!$scope.data.tpIndicadorMinimoTde){
				return;
			}
			
			$scope.vlMinimoTdeDisabled = false;
			if (isTabela($scope.data.tpIndicadorMinimoTde)){
				$scope.vlMinimoTdeDisabled = true;
				$scope.data.vlMinimoTde = 0;
				return true;
			}
			
			if (isDesconto($scope.data.tpIndicadorMinimoTde)){
				if(!isPercent($scope.data.vlMinimoTde)) {
					addAlert("LMS-01050","minimoTde");
					return false;
				}
			}else if((isAcrecimo($scope.data.tpIndicadorMinimoTde) || isValor($scope.data.tpIndicadorMinimoTde)) && $scope.data.vlMinimoTde < 0){
				addAlert("LMS-01050","minimoTde");
				return false;
			}
			return true;
		};
		/************************************************
		* FIM Metodos do grupo TDE                          *
		/************************************************/
		
		/************************************************
		* Metodos do grupo Total Frete                  *
		/************************************************/
		$scope.validatePercentualDescontoFreteTotal = function () {
			var pcDescontoFreteTotal = $scope.data.pcDescontoFreteTotal;
			if(!isPercent(pcDescontoFreteTotal)) {
				addAlert("LMS-01050","percentualDescontoFreteTotal");
				return false;
			}

			if(!eqZero(pcDescontoFreteTotal)) {
				var tpMinimoFretePeso = $scope.data.tpIndicadorMinFretePeso;
				var tpFretePeso = $scope.data.tpIndicadorFretePeso;
				var tpTarifaMinima = $scope.data.tpTarifaMinima;
				var tpTarifaEspecifica = $scope.data.tpIndicVlrTblEspecifica;
				var tpAdvalorem1 = $scope.data.tpIndicadorAdvalorem;
				var tpAdvalorem2 = $scope.data.tpIndicadorAdvalorem2;
				var tpValorReferencia = $scope.data.tpIndicadorValorReferencia;
				var tpPercentualGris = $scope.data.tpIndicadorPercentualGris;
				var tpMinimoGris = $scope.data.tpIndicadorMinimoGris;
				var tpPercentualTrt = $scope.data.tpIndicadorPercentualTrt;
				var tpMinimoTrt = $scope.data.tpIndicadorMinimoTrt;
				var tpMinimoTde = $scope.data.tpIndicadorMinimoTde;
				var tpPercentualTde = $scope.data.tpIndicadorPercentualTde;
				var tpPedagio = $scope.data.tpIndicadorPedagio;

				if(!isTabela(tpMinimoFretePeso) || !isTabela(tpFretePeso) || 
					!isTabela(tpTarifaMinima) || !isTabela(tpTarifaEspecifica) || 
					!isTabela(tpAdvalorem1) || !isTabela(tpAdvalorem2) || 
					!isTabela(tpValorReferencia) || !isTabela(tpPercentualGris) || 
					!isTabela(tpMinimoGris) || !isTabela(tpPercentualTrt) || 
					!isTabela(tpMinimoTrt) || !isTabela(tpMinimoTde) ||
					!isTabela(tpPercentualTde) || !isTabela(tpPedagio)) {
					$scope.addAlerts([{msg: $scope.getMensagem("LMS-01052"), type: 'danger'}]);
					return false;
				}

				var vlMinimoFretePeso = $scope.data.vlMinFretePeso;
				var vlTarifaMinima = $scope.data.vlTarifaMinima;
				var vlTarifaEspecifica = $scope.data.vlTblEspecifica;
				var vlPercentualFretePercentual = $scope.data.pcFretePercentual;
				if(!eqZero(vlMinimoFretePeso) || !eqZero(vlTarifaMinima) || !eqZero(vlTarifaEspecifica) || !eqZero(vlPercentualFretePercentual)) {
					$scope.addAlerts([{msg: $scope.getMensagem("LMS-01052"), type: 'danger'}]);
					return false;
				}
			}
			return true;
		};
		
		/************************************************
		* FIM Metodos do grupo Total Frete                  *
		/************************************************/
		
		function addAlert(key,fieldLabel){
			$scope.addAlerts([{msg: $scope.getMensagem(key,[translateService.getMensagem(fieldLabel)]), type: 'danger'}]);
		}
		
		function isTabela(indic) {
			return (indic.value && indic.value == "T");
		}
		
		function isDesconto(indic) {
			return (indic.value && indic.value == "D");
		}
		
		function isAcrecimo(indic) {
			return (indic.value && indic.value == "A");
		}
		
		function isValor(indic) {
			return (indic.value && indic.value == "V");
		}
		
		function isPeso(indic) {
			return (indic.value && indic.value == "P");
		}
		
		function isPercent(val) {
			return !( (val < 0) || (val > 100) );
		}
		
		function eqZero(val) {
			return (val === 0);
		}
		
		
		initializeAbaParam($stateParams);
	}
];


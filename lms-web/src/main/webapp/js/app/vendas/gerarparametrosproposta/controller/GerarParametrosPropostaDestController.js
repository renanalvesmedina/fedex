var GerarParametrosPropostaDestController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"editTabState",
	"translateService",
	"TableFactory",
	function($scope, $rootScope, $stateParams, editTabState, translateService, TableFactory) {

		$scope.destinosTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "generateDestinosProposta",
			remotePagination: false
		});

		$scope.tpIndicadorFreteMinimoChange = function(row){
			$scope.vlFreteMinimoBlur(row);
		};

		$scope.vlFreteMinimoBlur = function(row){
			var tpIndicadorFreteMinimo = row.tpIndicadorFreteMinimo;
			if(tpIndicadorFreteMinimo != null && angular.isDefined(tpIndicadorFreteMinimo)){
				if(tpIndicadorFreteMinimo.value == "D"){
					if(angular.isDefined(row.vlFreteMinimo) && (row.vlFreteMinimo < 0 || row.vlFreteMinimo > 100)){
						delete row.vlFreteMinimo;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("percentualMinimo")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				} else if (tpIndicadorFreteMinimo.value == "A"){
					if(angular.isDefined(row.vlFreteMinimo) && row.vlFreteMinimo < 0){
						delete row.vlFreteMinimo;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("percentualMinimo")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				}
			}
		};

		$scope.tpIndicadorFretePesoChange = function(row){
			$scope.vlFretePesoBlur(row);
		};

		$scope.vlFretePesoBlur = function(row){
			var tpIndicadorFretePeso = row.tpIndicadorFretePeso;
			if(tpIndicadorFretePeso != null && angular.isDefined(tpIndicadorFretePeso)){
				if(tpIndicadorFretePeso.value == "D"){
					if(angular.isDefined(row.vlFretePeso) && (row.vlFretePeso < 0 || row.vlFretePeso > 100)){
						delete row.vlFretePeso;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("fretePeso")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				} else if (tpIndicadorFretePeso.value == "A"){
					if(angular.isDefined(row.vlFretePeso) && row.vlFretePeso < 0){
						delete row.vlFretePeso;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("fretePeso")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				}
			}
		};

		$scope.pcDiferencaFretePesoBlur = function(row){
			if(angular.isDefined(row.pcDiferencaFretePeso) && (row.pcDiferencaFretePeso < 0 || row.pcDiferencaFretePeso > 100)){
				delete row.pcDiferencaFretePeso;
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("diferencaCapitalInterior")]), type: MESSAGE_SEVERITY.WARNING}]);
			}
		};

		$scope.tpIndicadorAdvaloremChange = function(row){
			$scope.vlAdvaloremBlur(row);
		};

		$scope.vlAdvaloremBlur = function(row){
			var tpIndicadorAdvalorem = row.tpIndicadorAdvalorem;
			if(tpIndicadorAdvalorem != null && angular.isDefined(tpIndicadorAdvalorem)){
				if(tpIndicadorAdvalorem.value == "D"){
					row.vlAdvaloremDisabled = false;
					if(angular.isDefined(row.vlAdvalorem) && (row.vlAdvalorem < 0 || row.vlAdvalorem > 100)){
						delete row.vlAdvalorem;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("adValorem")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				} else if (tpIndicadorAdvalorem.value == "A" || tpIndicadorAdvalorem.value == "V"){
					row.vlAdvaloremDisabled = false;
					if(angular.isDefined(row.vlAdvalorem) && row.vlAdvalorem < 0){
						delete row.vlAdvalorem;
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("adValorem")]), type: MESSAGE_SEVERITY.WARNING}]);
					}
				} else if (tpIndicadorAdvalorem.value == "T"){
					row.vlAdvalorem = 0.00;
					row.vlAdvaloremDisabled = true;
				} else {
					row.vlAdvaloremDisabled = false;
				}
			}
		};

		$scope.pcDiferencaAdvaloremBlur = function(row){
			if(angular.isDefined(row.pcDiferencaAdvalorem) && (row.pcDiferencaAdvalorem < 0 || row.pcDiferencaAdvalorem > 100)){
				delete row.pcDiferencaAdvalorem;
				$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("diferencaCapitalInterior")]), type: MESSAGE_SEVERITY.WARNING}]);
			}
		};

		$scope.initializeAbaDest = function(params){
			if ($scope.$parent.geracaoParametroPropostaDTO) {
				$scope.data = $scope.$parent.geracaoParametroPropostaDTO;

				$scope.data.generate=false;
				$scope.destinosTable.load($scope.data);
			}

			$scope.btnLimparDisabled = true;
			$scope.btnSalvarDisabled = true;
			$scope.btnGerarDisabled = true;

			if($scope.data.idSimulacao != null){
				$scope.btnSalvarDisabled = false;
				$scope.btnGerarDisabled = false;
			} else {
				$scope.btnLimparDisabled = false;
			}


			$scope.desabilitarTudo();
		};

		$scope.desabilitarTudo = function(){
			if($scope.data.disableAll == true){
				$scope.btnLimparDisabled = true;
				$scope.btnSalvarDisabled = true;
				$scope.btnGerarDisabled = true;
				$scope.setDisableAbaDest(true);

			} else {
				$scope.setDisableAbaDest(false);
			}
		};

		$scope.limpar = function(){
			$scope.data.generate=false;
			$scope.destinosTable.clear();
			$scope.destinosTable.load($scope.data);
		};

		$scope.salvar = function(){
			$rootScope.showLoading = true;
			$scope.data.listDestinosProposta = $scope.destinosTable.list;
			$scope.rest.doPost("storeDestinosProposta", $scope.data).then(function(idProposta) {
				$scope.showSuccessMessage();

				$scope.data.idProposta = idProposta;
				$scope.setGeracaoParametroPropostaDTO($scope.data);
				$scope.initializeAbaDest($stateParams);

				$rootScope.showLoading = false;
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.gerarDestinos = function(){
			$scope.data.generate=true;
			$scope.destinosTable.clear();
			$scope.destinosTable.load($scope.data);
		};



		$scope.initializeAbaDest($stateParams);
	}
];


var GerarParametrosPropostaDestAereoController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"editTabState",
	"modalService",
	"translateService",
	"TableFactory",
	"DataTransfer",
	function($scope, $rootScope, $stateParams, editTabState, modalService, translateService, TableFactory,DataTransfer) {
		$scope.nomeCabecalhoProdutoEspecifico = "";
		$scope.showCabecalhoProdutoEspecifico = false;

		if($scope.$parent.simulacao && $scope.$parent.simulacao.produtoEspecifico && $scope.$parent.simulacao.produtoEspecifico.dsProdutoEspecifico){
			$scope.nomeCabecalhoProdutoEspecifico = $scope.$parent.simulacao.produtoEspecifico.dsProdutoEspecifico;
			$scope.showCabecalhoProdutoEspecifico = true;
		}

		$scope.destinosAereoTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "generateDestinosAereo",
			remotePagination: false
		});

		$scope.gerarDestinosAereo = function(isGenerate){
			$scope.destinosAereoTable.clear();
			$scope.destinosAereoTable.load({idSimulacao: $scope.data.idSimulacao, isGenerate: isGenerate});
		};

		$scope.initializeAbaDestAereo = function(params){
			if (params.id){
				$scope.data = params;
				$scope.data.idSimulacao = params.id;
				$scope.setDisableAbaCad(false);
				$scope.gerarDestinosAereo(false);
				$scope.setDisableAbaDestConv(true);
			}
		};

		$scope.gerarDestinos = function (){
			$scope.initializeAbaDestAereo($stateParams);
		};


		$scope.salvar = function (){
			$rootScope.showLoading = true;
			$scope.data.listaParametroPropostaDestinoAereoTableDTO = $scope.destinosAereoTable.selected;
			$scope.rest.doPost("storeDestinosAereo", $scope.data).then(function() {
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
				$scope.gerarDestinosAereo(false);
			}, function() {
				$rootScope.showLoading = false;
			});
		};

		$scope.openModalGenerico = function(dsDestino, coluna, indicadorColuna, cabecalhoColuna, rowChecked){
			if(!rowChecked){
				return;
			}

			$rootScope.isPopup = true;
			var rest = $scope.rest;
			var nomeCabecalhoProdutoEspecifico = $scope.nomeCabecalhoProdutoEspecifico;

			var genericoController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
				$scope.VALOR_MINIMO = "M";
				$scope.VALOR_KG = "K";
				$scope.PRODUTO_ESPECIFICO = "P";
				$scope.GENERALIDADE= "G";
				$scope.TAXA = "T";
				$scope.TAXA_PESO_MININO_VALOR_EXCEDENTE = "TPV";
				$scope.PESO_MINIMO = "PM";


				$scope.dsCabecalho = "";
				$scope.nmDomain = "";
				$scope.genericoModel = {};

				$scope.inicializarPopup = function(){
					$scope.setarDadosEspecificos();
				};

				$scope.setarDadosEspecificos = function(){
					if($scope.PRODUTO_ESPECIFICO == indicadorColuna){
						$scope.dsCabecalho = nomeCabecalhoProdutoEspecifico;
						$scope.nmDomain = "DM_INDICADOR_PARAMETRO_CLIENTE";
					} else if($scope.TAXA == indicadorColuna || $scope.GENERALIDADE == indicadorColuna || $scope.TAXA_PESO_MININO_VALOR_EXCEDENTE == indicadorColuna
							|| $scope.VALOR_MINIMO == indicadorColuna || $scope.VALOR_KG == indicadorColuna){
						$scope.dsCabecalho = $scope.getMensagem(cabecalhoColuna);
						$scope.nmDomain = "DM_INDICADOR_PARAMETRO_CLIENTE";
					} else if($scope.PESO_MINIMO == indicadorColuna){
						$scope.dsCabecalho = $scope.getMensagem(cabecalhoColuna);
					}
					$scope.popularDominioGenerico();
				};

				$scope.popularDominioGenerico = function(){
					if($scope.nmDomain != "") {
					rest.doPost("findDominioGenericoCombo", {nmDomain: $scope.nmDomain}).then(function(data) {
						$scope.dominioGenerico = data.dominioGenerico;
						$scope.popularModel(coluna);
						$scope.tpIndicadorGenericoChange(true);
						$rootScope.showLoading = false;
					}, function() {
						$rootScope.showLoading = false;
					});

					} else {
						$scope.popularModel(coluna);
					}
				};

				$scope.popularModel = function(coluna){
					$scope.genericoModel = {};
					$scope.genericoModel.vlGenericoDisabled = false;
					$scope.genericoModel.pcGenericoDisabled = false;
					$scope.genericoModel.vlOriginalGenerico = coluna.vlOriginal;
					$scope.genericoModel.vlGenerico = coluna.valor;
					$scope.genericoModel.tpIndicadorGenerico = {};
					$scope.genericoModel.tpIndicadorGenerico.value = coluna.tpIndicador.value;
					$scope.genericoModel.tpIndicadorGenerico.description = coluna.tpIndicador.descriptionAsString;
					$scope.genericoModel.pcGenerico = coluna.percentual;
					$scope.genericoModel.psMinimoGenericoOriginal = coluna.psMinimoOriginal;
					$scope.genericoModel.psMinimoGenerico = coluna.psMinimo;
					$scope.genericoModel.vlExcedenteGenericoOriginal = coluna.vlExcedenteOriginal;
					$scope.genericoModel.vlExcedenteGenerico = coluna.vlExcedente;
				};

				$scope.storeGenerico = function(){
					if ($scope.genericoModel.tpIndicadorGenerico.value == "D" || $scope.genericoModel.tpIndicadorGenerico.value == "A") {
						$scope.calcularValorPeloPercentual();
					} else {
						$scope.tpIndicadorGenericoChange();
					}

					coluna.vlOriginal = $scope.genericoModel.vlOriginalGenerico;
					coluna.valor = $scope.genericoModel.vlGenerico;
					coluna.tpIndicador = {};
					coluna.tpIndicador.value = $scope.genericoModel.tpIndicadorGenerico.value;
					coluna.tpIndicador.descriptionAsString = $scope.genericoModel.tpIndicadorGenerico.description;
					coluna.percentual = $scope.genericoModel.pcGenerico;
					coluna.psMinimo = $scope.genericoModel.psMinimoGenerico;
					coluna.vlExcedente = $scope.genericoModel.vlExcedenteGenerico;

					$scope.close();
				};

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};

				$scope.validateValor = function(chave, valor, tpIndicador){
					if (angular.isDefined(valor) && angular.isDefined(tpIndicador) && angular.isDefined(tpIndicador.value)){
						if(tpIndicador.value == "D"){
							if(valor < 0 || valor > 100){
								$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem(chave)]), type: MESSAGE_SEVERITY.WARNING}]);
								return false;
							}
						} else if(tpIndicador.value == "V" || tpIndicador.value == "A"){
							if(valor < 0){
								$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem(chave)]), type: MESSAGE_SEVERITY.WARNING}]);
								return false;
							}
						}
					}
					return true;
				};

				$scope.tpIndicadorGenericoChange = function(isInicializar){
					if(angular.isDefined($scope.genericoModel.tpIndicadorGenerico) ) {
						if($scope.genericoModel.tpIndicadorGenerico.value == "T"){
							if(isInicializar != true){
								$scope.genericoModel.vlGenerico = $scope.genericoModel.vlOriginalGenerico;
								$scope.genericoModel.pcGenerico = 0;
							}

							$scope.genericoModel.vlGenericoDisabled = true;
							$scope.genericoModel.pcGenericoDisabled = true;

						} else if ($scope.genericoModel.tpIndicadorGenerico.value == "A" || $scope.genericoModel.tpIndicadorGenerico.value == "D") {
							if(isInicializar != true){
								$scope.genericoModel.vlGenerico = 0;
								$scope.genericoModel.pcGenerico = 0;
								$scope.calcularValorPeloPercentual();
							}

							$scope.genericoModel.vlGenericoDisabled = true;
							$scope.genericoModel.pcGenericoDisabled = false;

						} else if ($scope.genericoModel.tpIndicadorGenerico.value == "V") {
							if(isInicializar != true){
								$scope.genericoModel.pcGenerico = 0;
							}

							$scope.genericoModel.vlGenericoDisabled = false;
							$scope.genericoModel.pcGenericoDisabled = true;
						}

						$scope.vlGenericoBlur();
					}
				};

				$scope.calcularPercentualPeloValor = function(){
					if(angular.isDefined($scope.genericoModel.vlGenerico) && $scope.genericoModel.vlGenerico){

						if ($scope.genericoModel.vlGenerico ==  $scope.genericoModel.vlOriginalGenerico){
							$scope.genericoModel.pcGenerico = 0;
						}else{
							var valor = $scope.genericoModel.vlGenerico - $scope.genericoModel.vlOriginalGenerico;
							$scope.genericoModel.pcGenerico = parseFloat((valor * 100) / $scope.genericoModel.vlOriginalGenerico);
						}

					} else {
						$scope.genericoModel.pcGenerico = null;
					}
				};

				$scope.calcularValorPeloPercentual = function(){
					if(angular.isDefined($scope.genericoModel.pcGenerico)){
						var percentualDoValor = parseFloat($scope.genericoModel.vlOriginalGenerico * ($scope.genericoModel.pcGenerico / 100));

						if($scope.genericoModel.tpIndicadorGenerico.value == "D"){
							$scope.genericoModel.vlGenerico = $scope.genericoModel.vlOriginalGenerico - percentualDoValor;
						} else if($scope.genericoModel.tpIndicadorGenerico.value == "A"){
							$scope.genericoModel.vlGenerico = $scope.genericoModel.vlOriginalGenerico + percentualDoValor;
						}
					}
				};

				$scope.vlGenericoBlur = function(){
					var valido = $scope.validateValor('valor', $scope.genericoModel.vlGenerico, $scope.genericoModel.tpIndicadorGenerico);
					if(!valido){
						delete $scope.genericoModel.vlGenerico;
						return;
					}

					if(angular.isDefined($scope.genericoModel.tpIndicadorGenerico) && angular.isDefined($scope.genericoModel.tpIndicadorGenerico.value) && $scope.genericoModel.tpIndicadorGenerico.value == "V"){
						$scope.calcularPercentualPeloValor();
					}
				};

				$scope.pcGenericoBlur = function(){
					var valido = $scope.validateValor('percentual', $scope.genericoModel.pcGenerico, $scope.genericoModel.tpIndicadorGenerico);
					if(valido){
						$scope.calcularValorPeloPercentual();
					} else {
						delete $scope.genericoModel.vlGenerico;
						delete $scope.genericoModel.pcGenerico;
					}
				};

				$scope.inicializarPopup();
				$scope.title = $scope.getMensagem("destino") +  " " + dsDestino + " - " + $scope.dsCabecalho;

				if($scope.PESO_MINIMO == indicadorColuna){
					$scope.innerTemplate = contextPath + "js/app/vendas/gerarparametrosproposta/view/genericoPesoPopup.html";
				} else if($scope.TAXA_PESO_MININO_VALOR_EXCEDENTE == indicadorColuna){
					$scope.innerTemplate = contextPath + "js/app/vendas/gerarparametrosproposta/view/genericoTaxaPopup.html";
				} else {
				$scope.innerTemplate = contextPath + "js/app/vendas/gerarparametrosproposta/view/genericoPopup.html";
				}
			}];
			modalService.open({windowClass: "modal-destinos-aereo", controller: genericoController})["finally"](function(){$rootScope.isPopup = false;});
		};

		$scope.initializeAbaDestAereo($stateParams);
	}
];


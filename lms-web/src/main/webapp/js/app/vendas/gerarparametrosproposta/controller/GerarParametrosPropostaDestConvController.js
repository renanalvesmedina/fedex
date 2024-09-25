var GerarParametrosPropostaDestConvController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"editTabState",
	"modalService",
	"translateService",
	"TableFactory",
	"DataTransfer",
	function($scope, $rootScope, $stateParams, editTabState, modalService, translateService, TableFactory,DataTransfer) {
		$scope.simulacao = new Object();
		$scope.regioes = [];
		$scope.hasChanges = false;
		$scope.blDestinosGerados = false;
		$scope.destinosSalvos = [];
		$scope.regiaoDestinoFake = null;
		$scope.gerarDestinosDisabled = true;
		
		$scope.geracaoParcelasCount = 0;

		$scope.destinosAereoTable = new TableFactory({
			service: $scope.rest.doPost,
			method: "findParcelasAereoConvencional",
			remotePagination: false
		});

		$scope.initializeAbaDestConv = function(params){
			$scope.destinosSalvos = [];
			findComboRegioesPropostaConvencional();
			findPersistedData(params.id);
		};
		
		function findPersistedData(idSimulacao){
			$scope.rest.doPost("findPropostaByIdSimulacao",idSimulacao).then(function(data){
				$scope.data = data;
				$scope.gerarTodosDestinos(false);
			});
		}

		function findComboRegioesPropostaConvencional(){
			$scope.rest.doPost("findComboRegioesPropostaConvencional").then(function(data){
				$scope.regioes = data;
			});
		}
		
		function findAeroportoReferencia(idCliente){
			$scope.rest.doPost("findAeroportoAtendeCliente",idCliente).then(function(data){
				$scope.data.aeroportoReferencia = data;
			});
		}

		function hasDestinosGerados(regiao){
			return regiao.destinos && regiao.destinos.length > 0;
		}

		$scope.generateParcelasDestino = function(destino){

			if (hasParcelasGeradas(destino)){
				return;
			}

			$scope.hasChanges = true;

			var criteria = {
					idTabelaPreco:$scope.data.tabelaPreco.idTabelaPreco,
					idRotaPreco:destino.idRotaPreco,
					idDestinoProposta: destino.idDestinoProposta,
					tpGeracaoProposta: $scope.data.tpGeracaoProposta.value
					};
			
			if ($scope.data.produtoEspecifico != undefined){
				criteria.idProdutoEspecifico =  $scope.data.produtoEspecifico.idProdutoEspecifico;
			}
			
			if ($scope.geracaoParcelasCount == 0){
				$rootScope.showLoading = true;
			}
			$scope.geracaoParcelasCount++;
			$scope.rest.doPost("findParcelasDestinoConvencionalAereo",criteria).then(function (data){
				destino.parcelas = data;
				$scope.geracaoParcelasCount--;
				if ($scope.geracaoParcelasCount == 0){
					$rootScope.showLoading = false;
				}
			},function(){
				$rootScope.showLoading = false;
			});
		};

		function hasParcelasGeradas(destino){
			return destino.parcelas && destino.parcelas.length > 0;
		}
		
		$scope.gerarTodosDestinos = function(isGenerate){
			if ($scope.data.blFreteExpedido){
				$scope.geraDestinosProposta("D", isGenerate);
			}
			
			if ($scope.data.blFreteRecebido){
				$scope.geraDestinosProposta("O", isGenerate);
			}
			
		};
		
		$scope.geraDestinos = function(event){
			if (event != null){
				event.stopPropagation();
			}
			
			if($scope.data.destinosProposta && $scope.data.destinosProposta.length > 0){
				return;
			}
			
			$scope.geraDestinosProposta("D",false);
			
		};
		
		$scope.geraOrigens = function(event){
			if (event != null){
				event.stopPropagation();
			}
			
			if ($scope.data.origensProposta && $scope.data.origensProposta.length > 0){
				return;
			}
			
			$scope.geraDestinosProposta("O",false);
		};
		
		
		$scope.geraDestinosProposta = function(tpRota, isGenerate){
			if (isGenerate == undefined){
				isGenerate = false;
			}

			$rootScope.showLoading = true;
			var criteria = {idSimulacao:$scope.data.idSimulacao, isGenerate: isGenerate, tpRota:tpRota};
			criteria.idAeroportoOrigem = $scope.data.aeroportoReferencia.idAeroporto;
			$scope.rest.doPost("findDestinosRegiaoConvencional", criteria).then(function(data){
				if (tpRota =="D"){
					$scope.data.destinosProposta = data;
					$scope.data.marcarTodosDestinos = validateChecked($scope.data.destinosProposta);
					geraTodasParcelas($scope.data.destinosProposta);
				}else{
					$scope.data.origensProposta = data;
					$scope.data.marcarTodasOrigens = validateChecked($scope.data.origensProposta);
					geraTodasParcelas($scope.data.origensProposta);
				}

				if($scope.geracaoParcelasCount==0){
					$rootScope.showLoading = false;
				}
			}, function(data){
				$rootScope.showLoading = false;
			});
		};
		
		function validateChecked(destinos){
			for(var i = 0; i < destinos.length; i++){
				if (!destinos[i].blGeraDestinoProposta){
					return false;
				}
			}
			return true;
			
		}
		
		function geraTodasParcelas(destinos){
			for(var i = 0; i < destinos.length; i++){
				if (destinos[i].blGeraDestinoProposta){
					$scope.generateParcelasDestino(destinos[i]);
				}
			}
		}
		
		$scope.clickMarcarTodosDestinos = function(event){
			event.stopPropagation();
			
			if ($scope.data.destinosProposta && $scope.data.destinosProposta.length == 0){
				return;
			}
			
			
			for(var i = 0; i < $scope.data.destinosProposta.length; i++){
				var destino = $scope.data.destinosProposta[i]; 
				destino.blGeraDestinoProposta = !$scope.data.marcarTodosDestinos; 
				if (destino.blGeraDestinoProposta){
					$scope.generateParcelasDestino(destino);
				}
			}
			
		};
		
		$scope.clickMarcarTodasOrigens = function(event){
			event.stopPropagation();
		
			if ($scope.data.origensProposta && $scope.data.origensProposta.length == 0){
				return;
			}
			
			
			for(var i = 0; i < $scope.data.origensProposta.length; i++){
				var destino = $scope.data.origensProposta[i]; 
				destino.blGeraDestinoProposta = !$scope.data.marcarTodasOrigens; 
				if (destino.blGeraDestinoProposta){
					$scope.generateParcelasDestino(destino);
				}
			}
			

		};
		
		$scope.changeAeroportoReferencia = function(){
			$scope.data.origensProposta = [];
			$scope.data.destinosProposta = [];
			$scope.data.blFreteExpedido = false;
			$scope.data.blFreteRecebido = false;
			$scope.data.marcarTodosDestinos = false;
			$scope.data.marcarTodasOrigens = false;
			$scope.accordionDestinosIsOpen = false;
			$scope.accordionOrigensIsOpen = false;
		};
		

		$scope.clickBlGeraDestinoProposta = function(destino, event){
			event.stopPropagation();
			$scope.generateParcelasDestino(destino);
		};

		$scope.salvar = function (){
			var todosDestinos = [];
			if ($scope.data.origensProposta){
				todosDestinos = todosDestinos.concat($scope.data.origensProposta); 
			}
			if ($scope.data.destinosProposta){
				todosDestinos = todosDestinos.concat($scope.data.destinosProposta);
			}
			
			if (todosDestinos.length == 0){
				return;
			}
			
			$rootScope.showLoading = true;
			$scope.hasChanges = false;
			
			var geracaoParametroPropostaDTO = {idSimulacao: $scope.data.idSimulacao, listaDestinoPropostaDTO: todosDestinos};
			geracaoParametroPropostaDTO.aeroportoReferencia = $scope.data.aeroportoReferencia;
			geracaoParametroPropostaDTO.blFreteExpedido = $scope.data.blFreteExpedido;
			geracaoParametroPropostaDTO.blFreteRecebido = $scope.data.blFreteRecebido;
			geracaoParametroPropostaDTO.tpGeracaoProposta = $scope.data.tpGeracaoProposta; 
			
			$scope.rest.doPost("storeDestinosConvencional", geracaoParametroPropostaDTO).then(function() {
				$scope.showSuccessMessage();
				$rootScope.showLoading = false;
				$scope.gerarTodosDestinos(false);
				findPersistedData($stateParams.id);
			}, function() {
				$rootScope.showLoading = false;
			});
			
		};
		
		$scope.destinoFilter = function (actual, expected){
			return  expected == null || actual.id == expected.id;
		};

		$scope.initializeAbaDestConv($stateParams);

		$scope.openModalParcela = function(parcelaRow, destino){
			if (!destino.blGeraDestinoProposta){
				return;
			}
			$scope.blExibeCamposExtras = false;
			$scope.blExibeCamposPsMinimo = false;
			$rootScope.isPopup = true;
			var rest = $scope.rest;

			var parcelaController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {

				$scope.inicializarPopupParcelas = function(){
					rest.doPost("findDominioGenericoCombo", {nmDomain: "DM_INDICADOR_PARAMETRO_CLIENTE"}).then(function(data) {
						$scope.dominioGenerico = data.dominioGenerico;
					});
					carregaParcelaPopup();
					$scope.calculaValores();

					$scope.title = destino.dsDestino + ": " + parcelaRow.dsParcela;


					$scope.blExibeCamposExtras = ("IDEntregaInteriorConvencional" == $scope.parcela.cdParcelaPreco || 
                                                  "IDColetaInteriorConvencional" == $scope.parcela.cdParcelaPreco || 
                                                  "IDEntregaUrbanaEmergencia" == $scope.parcela.cdParcelaPreco || 
                                                  "IDEntregaUrbanaConvencional" == $scope.parcela.cdParcelaPreco || 
                                                  "IDColetaUrbanaEmergencia" == $scope.parcela.cdParcelaPreco || 
                                                  "IDColetaUrbanaConvencional" == $scope.parcela.cdParcelaPreco
							);
					
					$scope.blExibeCamposPsMinimo = "IDFreteQuilo" == $scope.parcela.cdParcelaPreco; 

				};

				function carregaParcelaPopup(){
					$scope.parcela = {};
					$scope.parcela.cdParcelaPreco = parcelaRow.cdParcelaPreco;
					$scope.parcela.vlOriginal = parcelaRow.vlOriginal;
					$scope.parcela.vlCalculado = parcelaRow.vlCalculado;
					$scope.parcela.vlPercentual = parcelaRow.vlPercentual;
					$scope.parcela.tpIndicador = parcelaRow.tpIndicador;
					$scope.parcela.vlExcedente = parcelaRow.vlExcedente;
					$scope.parcela.vlExcedenteOriginal = parcelaRow.vlExcedenteOriginal;
					$scope.parcela.psMinimo = parcelaRow.psMinimo;
					$scope.parcela.psMinimoOriginal = parcelaRow.psMinimoOriginal;
				}

				$scope.storeParcela = function(){
					if (!validateValores()){
						return;
					}
					
					$scope.calculaValores();

					parcelaRow.vlCalculado = $scope.parcela.vlCalculado;
					parcelaRow.vlPercentual = $scope.parcela.vlPercentual;
					parcelaRow.tpIndicador = $scope.parcela.tpIndicador;
					parcelaRow.vlExcedente = $scope.parcela.vlExcedente;
					parcelaRow.vlExcedenteOriginal = $scope.parcela.vlExcedenteOriginal;
					parcelaRow.psMinimo = $scope.parcela.psMinimo;
					parcelaRow.psMinimoOriginal = $scope.parcela.psMinimoOriginal;

					$scope.close();
				};

				$scope.close = function() {
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};

				$scope.calculaValores = function (){
					if(angular.isDefined($scope.parcela.tpIndicador)){
						var indicador = $scope.parcela.tpIndicador.value;
					}
					
					if (indicador == "T") {
						$scope.parcela.vlCalculado = $scope.parcela.vlOriginal;
						$scope.disableVlCalculado = true;
						$scope.parcela.vlPercentual = 0;
						$scope.disableVlPercentual = true;
					} else if (indicador == "V") {
						$scope.disableVlCalculado = false;
						calcularPercentualPeloValor();
						$scope.disableVlPercentual = true;
					} else if (indicador == "A" || indicador == "D") {
						calcularValorPeloPercentual(indicador);
						$scope.disableVlCalculado = true;
						$scope.disableVlPercentual = false;
					}
				};


				function validateValores(){
					var indicador = $scope.parcela.tpIndicador.value;

					if((indicador == "D" && ($scope.parcela.vlPercentual < 0 || $scope.parcela.vlPercentual > 100) )||
							(indicador == "A" && $scope.parcela.vlPercentual < 0)){
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("percentual")]), type: MESSAGE_SEVERITY.WARNING}]);
						return false;
					}else if (indicador == "V" && $scope.parcela.vlCalculado < 0){
						$scope.addAlerts([{msg: translateService.getMensagem("LMS-01050", [translateService.getMensagem("valor")]), type: MESSAGE_SEVERITY.WARNING}]);
						return false;
					}

					return true;
				}


				function calcularValorPeloPercentual(indicador){
					if(angular.isDefined($scope.parcela.vlPercentual)){
						var percentualDoValor = parseFloat($scope.parcela.vlOriginal * ($scope.parcela.vlPercentual / 100));

						if(indicador == "D"){
							$scope.parcela.vlCalculado = $scope.parcela.vlOriginal - percentualDoValor;
						} else if(indicador == "A"){
							$scope.parcela.vlCalculado = $scope.parcela.vlOriginal + percentualDoValor;
						}
					}
				}

				function calcularPercentualPeloValor(){
					if ($scope.parcela.vlCalculado ==  $scope.parcela.vlOriginal){
						$scope.parcela.vlPercentual = 0;
					}else{
						var valor = $scope.parcela.vlCalculado - $scope.parcela.vlOriginal;
						$scope.parcela.vlPercentual = parseFloat((valor * 100) / $scope.parcela.vlOriginal);
					}
				}

				$scope.changeTpIndicador = function(){
					$scope.parcela.vlPercentual = 0;
					$scope.parcela.vlCalculado = $scope.parcela.vlOriginal;
					$scope.calculaValores();
				};

				$scope.inicializarPopupParcelas();
				$scope.innerTemplate = contextPath + "js/app/vendas/gerarparametrosproposta/view/parcelasPopup.html";
			}];

			modalService.open({windowClass: "modal-parcelas-aereo", controller: parcelaController})["finally"](function(){$rootScope.isPopup = false;});
		};
	}
];


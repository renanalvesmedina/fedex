
var ManterMarkupController = [
	"$scope",
	"$rootScope",
	"TableFactory",
	"modalService",
	function($scope, $rootScope, TableFactory, modalService) {

		$scope.setConstructor({
			rest: '/tabeladeprecos/markup'
		});
		
		$scope.dtAtual = moment();
		
		$scope.tabelasAbertas = {tabelaMinimoProgressivoAberta: false, generalidadeAberta:false};

		$scope.itemsMinimoProgressivo = [];
		$scope.headerConfigMinimoProgressivo = [];
		$scope.minimoProgressivoSelected = [];

		$scope.filterParams = {};
		$scope.data = {};
		$scope.dados = {};
		$scope.listGeneralidade = [];
		var registroVazio = {
			listMarkupDto: [],
			listMarkupPrecoFreteDTO: [],
			listMarkupMinimoProgressivoDTO: []
		};
		$scope.alterados = angular.copy(registroVazio);
		var tiposParcelaTabela = ['M' /*Mínimo progressivo*/, 'P' /*Peso frete*/];
		var tiposParcelaGeneralidade = ['G' /*Generalidade*/, 'T' /*Taxa*/, 'S' /*Serviço adicional*/];
		var statusSalvo = 0;
		var statusNovo = 1;

		$scope.listParcelasTipoGenerica = [];

		$scope.setFilterParams = function(filterParams) {
			$scope.filterParams = filterParams;
		};

		$scope.setData = function(data) {
			$scope.data = data;
		};

		$scope.initializeAbaList = function() {};
		$scope.initializeAbaCad = function() {};

		$scope.markupsParcelasGeneralidade = new TableFactory({
			remotePagination: false
		});

		$scope.limpaListas = function() {
			$scope.markupGeral = {};
			$scope.listGeneralidade = [];
			$scope.itemsMinimoProgressivo = [];
			$scope.itemsPrecoFrete = [];
			$scope.headerConfigMinimoProgressivo = [];
			$scope.headerConfigPrecoFrete = [];
			$scope.alterados = angular.copy(registroVazio);
			$scope.minimoProgressivoSelected.length = 0; //não pode perder referência
			$scope.precoFreteSelected.length = 0;
		};

		var markupCompleto = function(markup) {
			return !!markup.idParcela && !!markup.valorMarkup && !!markup.dataVigenciaInicial;
		};
		
		function existeGeneralidadeNaoPreenchido(){
			var existeGeneralidadeNaoPreenchido = false;
			angular.forEach($scope.listGeneralidade, function(item) {
				if (!markupCompleto(item.markup)) {
					existeGeneralidadeNaoPreenchido = true;
				}
			});
			return existeGeneralidadeNaoPreenchido;
		};

		$scope.incluiNovoGeneralidade = function() {
			if(!existeGeneralidadeNaoPreenchido()){
				var novo = {markup: {'idParcela': null, 'nomeParcela': null, 'valorMarkup': null, 'dataVigenciaInicial': null, 'dataVigenciaFinal': null}, status: statusNovo};
				$scope.listGeneralidade.push(novo);
			}
		};

		$scope.changeGeneralidade = function(markupGeneralidade){
			if ($scope.alterados.listMarkupDto.indexOf(markupGeneralidade) === -1) {
				$scope.alterados.listMarkupDto.push(markupGeneralidade);
			}
		};

		/** Monita alteracoes no componente suggest da tabela. */
		$scope.$watch('dados.tabela', function(tabelaSuggest){
			if(tabelaSuggest && tabelaSuggest.idTabelaPreco){
				$scope.loadMarkups(tabelaSuggest.idTabelaPreco);
			} else{
				$scope.clearData();
			}
		});

		/** Carrega markup de acordo com a alteracao da suggest de TabelaPreco */
		$scope.loadMarkups = function(idTabela){
			$scope.limpaListas();
			if (!idTabela) {
				return;
			}
			$scope.idTabela = idTabela;

			$scope.loadInicial(idTabela);
		};

		/** Carrega todas as listas de forma encadeada para poder funcionar o loader de tela */
		$scope.loadInicial = function(idTabela){
			$rootScope.showLoading = true;

			//loadMarkupGeral
			$scope.rest.doGet("tabelapreco?idTabela=" + idTabela).then(function(markupGeralDTO) {
				$scope.markupGeral = markupGeralDTO;

				//loadMarkupMinimoProgressivo
				$scope.itemsMinimoProgressivo = [];
				$scope.headerConfigMinimoProgressivo = [];
				$scope.minimoProgressivoSelected.length = 0;
				$scope.rest.doGet("minimoprogressivo?idTabela=" + idTabela).then(function(listMarkupMinimoProgressivoDTO) {
					$scope.itemsMinimoProgressivo = listMarkupMinimoProgressivoDTO;
					$scope.configHeaderMinimoProgressivo();

					// loadMarkupPrecoFrete
					$scope.itemsPrecoFrete = [];
					$scope.headerConfigPrecoFrete = [];
					$scope.precoFreteSelected.length = 0;
					$scope.rest.doGet("precofrete?idTabela=" + idTabela).then(function(listMarkupPrecoFreteDTO) {
						$scope.itemsPrecoFrete = listMarkupPrecoFreteDTO;
						$scope.configHeaderPrecoFrete();

						//loadMarkupGeneralidade - parte 1
						$scope.listGeneralidade = [];
						$scope.rest.doGet("generalidade?idTabela=" + idTabela).then(function(listMarkupDto) {
							angular.forEach(listMarkupDto, function(markup) {
								$scope.listGeneralidade.push({ markup: markup, status: statusSalvo });
							});

							//loadMarkupGeneralidade - parte 2
							$scope.rest.doGet('tabelapreco/'+$scope.idTabela+'/parcelas?tipoPrecificacao=Generica').then(function(parcelas) {
								$scope.listParcelasTipoGenerica = parcelas;

								$rootScope.showLoading = false;
							});
						});
					});
				});
			});
		};

		$scope.loadMarkupGeral = function(idTabela){
			$rootScope.showLoading = true;
			$scope.rest.doGet("tabelapreco?idTabela=" + idTabela).then(function(markupGeralDTO) {
				$scope.markupGeral = markupGeralDTO;
				$rootScope.showLoading = false;
			});
		};

		$scope.loadMarkupMinimoProgressivo = function(idTabela){
			$rootScope.showLoading = true;
			$scope.itemsMinimoProgressivo = [];
			$scope.headerConfigMinimoProgressivo = [];
			$scope.minimoProgressivoSelected.length = 0;
			$scope.rest.doGet("minimoprogressivo?idTabela=" + idTabela).then(function(listMarkupMinimoProgressivoDTO) {
				$scope.itemsMinimoProgressivo = listMarkupMinimoProgressivoDTO;
				$scope.configHeaderMinimoProgressivo();
				$rootScope.showLoading = false;
			});
		};

		$scope.loadMarkupPrecoFrete = function(idTabela){
			$rootScope.showLoading = true;
			$scope.itemsPrecoFrete = [];
			$scope.headerConfigPrecoFrete = [];
			$scope.precoFreteSelected.length = 0;
			$scope.rest.doGet("precofrete?idTabela=" + idTabela).then(function(listMarkupPrecoFreteDTO) {
				$scope.itemsPrecoFrete = listMarkupPrecoFreteDTO;
				$scope.configHeaderPrecoFrete();
				$rootScope.showLoading = false;
			});
		};

		$scope.loadMarkupGeneralidade = function(idTabela){
			$rootScope.showLoading = true;
			$scope.listGeneralidade = [];
			$scope.rest.doGet("generalidade?idTabela=" + idTabela).then(function(listMarkupDto) {
				angular.forEach(listMarkupDto, function(markup) {
					$scope.listGeneralidade.push({ markup: markup, status: statusSalvo });
				});
				$rootScope.showLoading = false;
			});
			$scope.rest.doGet('tabelapreco/'+$scope.idTabela+'/parcelas?tipoPrecificacao=Generica').then(function(parcelas) {
				$scope.listParcelasTipoGenerica = parcelas;
				$rootScope.showLoading = false;
			});
		};

		$scope.habilita = function(dado) {
			return dado && dado.status && dado.status === statusNovo;
		};

		var limpaVazios = function(registros) {
			var limpos = {};
			limpos.listMarkupDto = limpaVaziosGeneralidades(registros.listMarkupDto);
			limpos.listMarkupPrecoFreteDTO = limpaVaziosPrecosFretes(registros.listMarkupPrecoFreteDTO);
			limpos.listMarkupMinimoProgressivoDTO = limpaVaziosMinimosProgressivos(registros.listMarkupMinimoProgressivoDTO);
			return limpos;
		};

		var limpaVaziosGeneralidades = function(generalidades) {
			return generalidades;
		};

		var limpaVaziosMinimosProgressivos = function(minimosProgressivos) {
			if (!minimosProgressivos || !minimosProgressivos.length) {
				return;
			}
			var minimosProgressivosLimpos = [];
			angular.forEach(minimosProgressivos, function(minimoProgressivo) {
				var parcelas = limpaVaziosParcelaMinimoProgressivo(minimoProgressivo.listParcelas);
				if (parcelas) {
					minimosProgressivosLimpos.push({
						idRota: minimoProgressivo.idRota,
						idTarifa: minimoProgressivo.idTarifa,
						dataVigenciaInicial: minimoProgressivo.dataVigenciaInicial,
						dataVigenciaFinal: minimoProgressivo.dataVigenciaFinal,
						listParcelas: parcelas
					});
				}
			});
			return minimosProgressivosLimpos;
		};

		var limpaVaziosValorFaixa = function(valoresFaixa) {
			var valoresFaixaLimpos = [];
			angular.forEach(valoresFaixa, function(valorFaixa) {
				if (!(valorFaixa.idValorMarkupFaixaProgressiva || valorFaixa.valor)) return;
				valoresFaixaLimpos.push(valorFaixa);
			});
			if (!valoresFaixaLimpos.length) {
				return;
			}
			return valoresFaixaLimpos;
		};

		var limpaVaziosParcelaMinimoProgressivo = function(parcelas) {
			var parcelasLimpas = [];
			angular.forEach(parcelas, function(parcela) {
				var valoresFaixa = limpaVaziosValorFaixa(parcela.listValoresFaixaProgressiva);
				if (valoresFaixa) {
					parcelasLimpas.push({
						idMarkupFaixaProgressiva: parcela.idMarkupFaixaProgressiva,
						dataVigenciaInicial: parcela.dataVigenciaInicial,
						dataVigenciaFinal: parcela.dataVigenciaFinal,
						idParcela: parcela.idParcela,
						listValoresFaixaProgressiva: valoresFaixa
					});
				}
			});
			if (!parcelasLimpas.length) {
				return;
			}
			return parcelasLimpas;

		};

		var limpaVaziosPrecosFretes = function(precosFretes) {
			if (!precosFretes || !precosFretes.length) {
				return;
			}
			var precosFretesLimpos = [];
			angular.forEach(precosFretes, function(precoFrete){
				var parcelas = [];
				angular.forEach(precoFrete.listParcelas, function(parcela) {
					if (!(parcela.idValorMarkupPrecoFrete || parcela.valor)) return;
					parcelas.push(parcela);
				});
				if (parcelas.length) {
					precosFretesLimpos.push({
						id: precoFrete.id,
						idTarifa: precoFrete.idTarifa,
						idRota: precoFrete.idRota,
						dataVigenciaInicial: precoFrete.dataVigenciaInicial,
						dataVigenciaFinal: precoFrete.dataVigenciaFinal,
						listParcelas: parcelas
					});
				}
			});
			if (!precosFretesLimpos.length) {
				return;
			}
			return precosFretesLimpos;
		};

		$scope.storeMarkup = function($event) {
			if (jQuery($event.target).hasClass("ng-invalid")) {
				$scope.addAlerts([{msg: "LMS-00070", type: MESSAGE_SEVERITY.WARNING}]);
				$event.preventDefault();
				return false;
			}
			
			$scope.rest.doPost("validavigenciaMinimoProgressivotela", $scope.itemsMinimoProgressivo).then(function() {
				
				$scope.rest.doPost("validavigenciaPrecoFretetela", $scope.itemsPrecoFrete).then(function() {
					
					var listaMarkupGeneralidade = [];
					angular.forEach($scope.listGeneralidade, function(item) {
						listaMarkupGeneralidade.push(item.markup);
					});
					$scope.rest.doPost("validavigenciaGeneralidadetela", listaMarkupGeneralidade).then(function() {
					
						var lista = limpaVazios($scope.alterados);
						if ($scope.markupGeral && !$scope.markupGeral.id && $scope.markupGeral.valorMarkup) {
							lista.markupGeral = $scope.markupGeral;
						}
						if ($scope.markupGeral && $scope.markupGeral.id) {
							lista.markupGeral = $scope.markupGeral;
						}
	
						$rootScope.showLoading = true;
						lista.idTabelaPreco = $scope.idTabela;
						$scope.rest.doPost("storeall", lista).then(function() {
							$scope.loadMarkups($scope.idTabela);
							$scope.showSuccessMessage();
							$rootScope.showLoading = false;
						}, function() {
							$rootScope.showLoading = false;
						});
						
					});
					
				});
				
			});
			
		};

		$scope.removeGeneralidade = function() {
			if (angular.isUndefined($scope.markupsParcelasGeneralidade.selected) || angular.isUndefined($scope.markupsParcelasGeneralidade.selected.length) || $scope.markupsParcelasGeneralidade.selected.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}
			
			var idsMarkupsGeneralidadesExcluir = [];
			$scope.markupsParcelasGeneralidade.selected.forEach(function(markupGeneralidade) {
				if(markupGeneralidade.markup.id){
					idsMarkupsGeneralidadesExcluir.push(markupGeneralidade.markup.id);
				}
			});
			if (!idsMarkupsGeneralidadesExcluir.length) {
				$scope.markupsParcelasGeneralidade.selected.forEach(function(markupGeneralidade) {
					removeItem($scope.alterados.listMarkupDto, markupGeneralidade.markup);
				});
				return;
			}
			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
				$scope.markupsParcelasGeneralidade.selected.forEach(function(markupGeneralidade) {
					removeItem($scope.alterados.listMarkupDto, markupGeneralidade.markup);
				});
				
				$scope.rest.doPost('removeall', idsMarkupsGeneralidadesExcluir).then(function() {
		   				$scope.showSuccessMessage();
		   				$scope.loadMarkupGeneralidade($scope.idTabela);
					},
					function(){
						$rootScope.showLoading = false;
					});
			});

		};
		
		$scope.clearData = function(){
			$scope.markupGeral = {};
			$scope.dados = {};
			$("#formMarkup").removeClass("submitted");
			$scope.limpaListas();
			delete $scope.idTabela;
			$scope.tabelasAbertas.tabelaMinimoProgressivoAberta = false;
			$scope.tabelasAbertas.tabelaPrecoFreteAberta = false;
			$scope.tabelasAbertas.generalidadeAberta = false;
		};

		//--------------------------------------------------------------\\
		//-------------------- clickFields -----------------------------\\
		$scope.atualizar = function(id, nome, linha, coluna){
			if(coluna.title === "rota"){
				linha.idRota = id;
				linha.nomeRota = nome;
				return ;
			}
			linha.idTarifa = id;
			linha.nomeTarifa = nome;
		};

		var clickFieldRota = function(tipo){
			var _tipo = tipo;
			return function(row, column, callback) {
				$rootScope.isPopup = true;
				var rest = $scope.rest;
				var atualizar = $scope.atualizar;
				var linha = row;
				var coluna = column;
				var idTabelaPreco = $scope.idTabela;

				var myController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
					$scope.title = "rota";
					$scope.innerTemplate = contextPath + "js/app/tabeladeprecos/mantermarkup/view/manterMarkupRotaPopup.html";

					$scope.initializeRotaPopup = function(){
						$scope.rotaTableParams = new TableFactory({
							remotePagination: false
						});

						$scope.listRotas = {};
						$scope.rotaMarkupFiltro = {};
						$scope.rotaMarkupFiltro.textoBusca = null;
						$scope.rotaMarkupFiltro.idTabelaPreco = idTabelaPreco;
						$scope.rotaMarkupFiltro.tipo = _tipo;

						$("#rotaForm").removeClass("submitted");
					};

					$scope.selecionarRota = function(rota) {
						atualizar(rota.idRotaPreco, rota.descricao, linha, coluna);
						callback(rota.descricao);
						$scope.close();
					};

					$scope.findRotas = function() {
						$rootScope.showLoading = true;
						rest.doPost("findRotas", $scope.rotaMarkupFiltro).then(function(rotas) {
							$scope.listRotas = rotas;
							$rootScope.showLoading = false;
						},function() {
							$rootScope.showLoading = false;
						});
					};

					$scope.limparBuscaRota = function() {
						$scope.initializeRotaPopup();
					};

					$scope.close = function() {
						$modalInstance.dismiss("cancel");
						$rootScope.isPopup = false;
					};

					$scope.initializeRotaPopup();
				}];

				modalService.open({windowClass: "modal-rota", controller: myController})["finally"](function(){$rootScope.isPopup = false;});
			};
		};

		var clickFieldTarifa = function(tipo){
			var _tipo = tipo;
			return function(row, column, callback) {
				$rootScope.isPopup = true;
				var rest = $scope.rest;
				var atualizar = $scope.atualizar;
				var linha = row;
				var coluna = column;
				var idTabelaPreco = $scope.idTabela;

				var myController = ["$rootScope", "$scope", "$modalInstance", function($rootScope, $scope, $modalInstance) {
					$scope.title = "tarifa";
					$scope.innerTemplate = contextPath + "js/app/tabeladeprecos/mantermarkup/view/manterMarkupTarifaPopup.html";

					$scope.initializeTarifaPopup = function(){
						$scope.tarifaTableParams = new TableFactory({
							remotePagination: false
						});

						$scope.listTarifas = {};
						$scope.tarifaMarkupFiltro = {};
						$scope.tarifaMarkupFiltro.cdTarifaPreco = null;
						$scope.tarifaMarkupFiltro.idTabelaPreco = idTabelaPreco;
						$scope.tarifaMarkupFiltro.tipo = _tipo;

						$("#tarifaForm").removeClass("submitted");
					};

					$scope.selecionarTarifa = function(tarifa) {
						atualizar(tarifa.idTarifaPreco, tarifa.cdTarifaPreco, linha, coluna);
						callback(tarifa.cdTarifaPreco);
						$scope.close();
					};

					$scope.findTarifas = function() {
						$rootScope.showLoading = true;
						rest.doPost("findTarifas", $scope.tarifaMarkupFiltro).then(function(tarifas) {
							$scope.listTarifas = tarifas;
							$rootScope.showLoading = false;
						},function() {
							$rootScope.showLoading = false;
						});
					};

					$scope.limparBuscaTarifa = function() {
						$scope.initializeTarifaPopup();
					};

					$scope.close = function() {
						$modalInstance.dismiss("cancel");
						$rootScope.isPopup = false;
					};

					$scope.initializeTarifaPopup();
				}];

				modalService.open({windowClass: "modal-tarifa", controller: myController})["finally"](function(){$rootScope.isPopup = false;});
			};
		};

		//-------------------------------------------------------------------------//
		//-------------------- Callbacks de mínimo progressivo --------------------//
		$scope.configHeaderMinimoProgressivo = function() {

			$scope.itemsMinimoProgressivo.forEach(function(linha) {
				var templateDataInicial = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-inicial-markup.html';
				var templateDataFinal = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-final-markup.html';


				if (!chaveJaExiste($scope.headerConfigMinimoProgressivo, linha)) {
					var obj = {};
					if (linha.nomeTarifa != undefined) {
						obj.title = 'tarifa';
						obj.onClickField = clickFieldTarifa('minimoProgressivo');
						obj.minWidth = '100px';
					} else {
						obj.title = 'rota';
						obj.onClickField = clickFieldRota('minimoProgressivo');
						obj.minWidth = '500px';
					}
					obj.readOnly = true;
					obj.editable = true;
					obj.required = true;
					obj.type = 'text';

					$scope.headerConfigMinimoProgressivo.push(obj);


					var vigenciaInicialObj = {};
					vigenciaInicialObj.title = 'vigenciaInicial';
					vigenciaInicialObj.id = 'dt_inicial';
					vigenciaInicialObj.minWidth = '150px';
					vigenciaInicialObj.editable = true;
					vigenciaInicialObj.type = 'date';
					vigenciaInicialObj.templateUrl = templateDataInicial;
					$scope.headerConfigMinimoProgressivo.push(vigenciaInicialObj);


					var vigenciaFinalObj = {};
					vigenciaFinalObj.title = 'vigenciaFinal';
					vigenciaFinalObj.id = 'dt_final';
					vigenciaFinalObj.minWidth = '150px';
					vigenciaFinalObj.editable = true;
					vigenciaFinalObj.type = 'date';
					vigenciaFinalObj.templateUrl = templateDataFinal;
					$scope.headerConfigMinimoProgressivo.push(vigenciaFinalObj);

				}

				if (!linha.listParcelas) {
					return;
				}

				linha.listParcelas.forEach(function(parcela, indexParcela) {
					var grupo = buscaGrupo($scope.headerConfigMinimoProgressivo, parcela);
					if (!grupo) {
						var grupo = { title: parcela.nomeParcela, children: []};
						$scope.headerConfigMinimoProgressivo.push(grupo);
					}

					parcela.listValoresFaixaProgressiva.forEach(function(faixa, indexFaixa) {
						var titulo = faixa.faixa.replace(/([V])([\d,.]+)/, '$2 Kg').replace(/([P])([\d]+)/, '\$2');
						var campo = "";
						incluiColuna(grupo, titulo, 'parc_'+parcela.idParcela+'_fx_'+faixa.faixa, 'perc', 2);
					});
				});
			});

			function buscaGrupo(headerConfigMinimoProgressivo, parcela) {
				if (!(headerConfigMinimoProgressivo && parcela)) {
					return;
				}
				for (var i = 0; i < headerConfigMinimoProgressivo.length; i++) {
					if (!!headerConfigMinimoProgressivo[i].title && headerConfigMinimoProgressivo[i].title === parcela.nomeParcela) {
						return headerConfigMinimoProgressivo[i];
					}
				}
			}

			function chaveJaExiste(headerConfigMinimoProgressivo, linha) {
				return headerConfigMinimoProgressivo.some(function(grupo) {
					var titulo = (linha.nomeTarifa ? 'tarifa' : 'rota');
					return grupo.title === titulo;
				});
			}

			function incluiColuna(grupo, titulo, id, tipo, decimais, template) {
				if (colunaJaCadastrada(grupo, titulo)) {
					return;
				}
				var faixaPesoTemplate = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/faixa-peso-markup.html';
				var minWidth = tipo === 'date' ? '130px': '60px';
				var column = { title: titulo, id: id, type: tipo, editable: true, decimais: decimais, minWidth: minWidth, templateUrl: faixaPesoTemplate };

				grupo.children.push(column);
			}

			function colunaJaCadastrada(grupo, titulo) {
				return grupo.children && grupo.children.some(function(current) {
					return current.title === titulo;
				});
			}
			return $scope.headerConfigMinimoProgressivo;
		};

		var buscaParcela = function(linha, titulo) {
			var parcelaVazia = {
					dataVigenciaInicial: '',
					dataVigenciaFinal: '',
					nomeParcela: '',
					listValoresFaixaProgressiva: []};
			if (!linha) {
				return parcelaVazia;
			}
			if (!linha.listParcelas) {
				return parcelaVazia;
			}
			for (var i = 0; i < linha.listParcelas.length; i++) {
				if (linha.listParcelas[i].nomeParcela === titulo) {
					return parcela = linha.listParcelas[i];
				}
			}
			linha.listParcelas.push(parcelaVazia);
			return parcelaVazia;
		};

		var regExpValorFaixa = /^([\d,.]+)( )(Kg|KG|kg|kG)$/;
		var regExpProdutoFaixa = /^(\d+)$/;

		var buscaFaixa = function(parcela, linha, faixa) {
			var faixaVazia = {
				faixa: faixa.replace(regExpValorFaixa, 'V$1').replace(regExpProdutoFaixa, 'P$1'),
				valor: ''
			};
			if (!linha) return faixaVazia;
			if (!parcela.listValoresFaixaProgressiva) {
				return faixaVazia;
			}
			for (var i = 0; i < parcela.listValoresFaixaProgressiva.length; i++) {
				if (parcela.listValoresFaixaProgressiva[i].faixa === faixa.replace(regExpValorFaixa, 'V$1').replace(regExpProdutoFaixa, 'P$1')) {
					return parcela.listValoresFaixaProgressiva[i];
				}
			}
			parcela.listValoresFaixaProgressiva.push(faixaVazia);
			return faixaVazia;
		};

		var removeItem = function(array, item) {
			var indice = array.indexOf(item);
			if (indice < 0) return;
			array.splice(indice, 1);
		};

		$scope.removeMinimoProgressivo = function() {
			if (angular.isUndefined($scope.minimoProgressivoSelected) || angular.isUndefined($scope.minimoProgressivoSelected.length) || $scope.minimoProgressivoSelected.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}

			var idsValorMarkupFaixaProgressiva = [];
			$scope.minimoProgressivoSelected.forEach(function(markupMinimoProgressivo) {
				if (!markupMinimoProgressivo.listParcelas) {
					removeItem($scope.itemsMinimoProgressivo, markupMinimoProgressivo);
					return;
				}
				markupMinimoProgressivo.listParcelas.forEach(function(parcelaMinimoProgressivo) {
					parcelaMinimoProgressivo.listValoresFaixaProgressiva.forEach(function(valoresFaixaProgressiva) {
						if (angular.isDefined(valoresFaixaProgressiva.idValorMarkupFaixaProgressiva) && valoresFaixaProgressiva.idValorMarkupFaixaProgressiva != null) {
							idsValorMarkupFaixaProgressiva.push(valoresFaixaProgressiva.idValorMarkupFaixaProgressiva);
						}
					});
				});

			});
			if (!idsValorMarkupFaixaProgressiva.length) {
				$scope.minimoProgressivoSelected.forEach(function(markupMinimoProgressivo) {
					removeItem($scope.itemsMinimoProgressivo, markupMinimoProgressivo);
					removeItem($scope.alterados.listMarkupMinimoProgressivoDTO, markupMinimoProgressivo);
				});
				return;
			}

			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
				$scope.minimoProgressivoSelected.forEach(function(markupMinimoProgressivo) {
					removeItem($scope.alterados.listMarkupMinimoProgressivoDTO, markupMinimoProgressivo);
				});

				$scope.rest.doPost('deleteMinimoProgressivo', idsValorMarkupFaixaProgressiva).then(
					function() {
		   				$scope.showSuccessMessage();
						$scope.loadMarkupMinimoProgressivo($scope.idTabela);
					},
					function() {
						$rootScope.showLoading = false;
				});
			});
		};

		$scope.rowMapperMinimoProgressivo = {
			map: function(linha, coluna) {
				if (!coluna.parent) {
					if(coluna.title == 'vigenciaInicial'){
						return linha.dataVigenciaInicial;
					}

					if(coluna.title == 'vigenciaFinal'){
						return linha.dataVigenciaFinal;
					}

					return linha.nomeTarifa ? linha.nomeTarifa: linha.nomeRota;
				}

				var p = buscaParcela(linha, coluna.parent.title);
				var faixa = buscaFaixa(p, linha, coluna.title);
				return faixa.valor;
			}
		};

		$scope.disableFieldMinimoProgressivo = function(row, column) {
			if (!column.parent) {
				if (column.title === 'vigenciaFinal') {
					return false;
				}
				for (var i = 0; i < row.listParcelas.length; i++) {
					if (angular.isDefined(row.listParcelas[i].idMarkupFaixaProgressiva) && row.listParcelas[i].idMarkupFaixaProgressiva != null) {
						return true;
					}
				}
				return false;
			}

			return false;
		};

		$scope.changeCellValueMinimoProgressivo = function(row, column, value) {
			if (!row) {
				return;
			}
			if (!column) {
				return;
			}
			if(!column.parent){

				if(column.title === 'rota'){
					row.nomeRota = value;
					row.idRota = value;
					return;
				}

				if(column.title === 'tarifa'){
					row.nomeTarifa = value;
					row.idTarifa = value;
					return;
				}

				if (column.title === 'vigenciaInicial') {
					row.dataVigenciaInicial = value;
					return;
				}

				if (column.title === 'vigenciaFinal') {
					row.dataVigenciaFinal = value;
					if ($scope.alterados.listMarkupMinimoProgressivoDTO.indexOf(row) === -1) {
						$scope.alterados.listMarkupMinimoProgressivoDTO.push(row);
					}
					return;
				}
			}

			var p = buscaParcela(row, column.parent.title);
			if ($scope.alterados.listMarkupMinimoProgressivoDTO.indexOf(row) === -1) {
				$scope.alterados.listMarkupMinimoProgressivoDTO.push(row);
			}

			var f = buscaFaixa(p, row, column.title);
			f.valor = value;
		};

		function existeMinimoProgressivoNaoPreenchido(){
			var existeMinimoNaoPreenchido = false;
			angular.forEach($scope.itemsMinimoProgressivo, function(item) {
				if(!item.id){
					if(!item.idTarifa && !item.idRota){
						existeMinimoNaoPreenchido = true;
					}
				}
			});
			return existeMinimoNaoPreenchido;
		}

		$scope.incluiNovoMinimoProgressivo = function() {
			if(!existeMinimoProgressivoNaoPreenchido()){

				$scope.rest.doGet("modelominimoprogressivo?idTabela=" + $scope.idTabela).then(function(modeloMarkupMinimoProgressivoDTO) {
					var minimoProgressivoModelo = modeloMarkupMinimoProgressivoDTO;

					var minimoProgressivoTemp = {};
					minimoProgressivoTemp.id = null;
					minimoProgressivoTemp.idTarifa = null;
					minimoProgressivoTemp.nomeTarifa = null;
					minimoProgressivoTemp.idRota = null;
					minimoProgressivoTemp.nomeRota = null;
					minimoProgressivoTemp.dataVigenciaInicial = null;
					minimoProgressivoTemp.dataVigenciaFinal = null;
					minimoProgressivoTemp.tipoParcela = "M";
					minimoProgressivoTemp.listParcelas = [];

					var parcelasTemp = [];

					angular.forEach(minimoProgressivoModelo.listParcelas, function(parcela) {
						var parcelaTemp = {};
						parcelaTemp.idParcela = parcela.idParcela;
						parcelaTemp.nomeParcela = parcela.nomeParcela;
						parcelaTemp.idValorMarkupFaixaProgressiva = null;
						parcelaTemp.listValoresFaixaProgressiva = [];

						var valoresFaixaProgressivaTemp = [];
						angular.forEach(parcela.listValoresFaixaProgressiva, function(valorFaixaProgressiva) {
							var valorFaixaProgressivaTemp = {};
							valorFaixaProgressivaTemp.faixa = valorFaixaProgressiva.faixa;
							valorFaixaProgressivaTemp.valor = null;
							valorFaixaProgressivaTemp.idValorMarkupFaixaProgressiva = null;
							valorFaixaProgressivaTemp.idFaixaProgressiva = valorFaixaProgressiva.idFaixaProgressiva;
							parcelaTemp.listValoresFaixaProgressiva.push(valorFaixaProgressivaTemp);
						});

						minimoProgressivoTemp.listParcelas.push(parcelaTemp);
					});

					$scope.itemsMinimoProgressivo.push(minimoProgressivoTemp);
					$scope.configHeaderMinimoProgressivo();
				});
			}
		};
		//------------------------ Fim Mínimo Progressivo -------------------------//
		//-------------------------------------------------------------------------//

		/* Preço Frete */
		function existeprecoFreteNaoPreenchido(){
			var existePrecoFreteNaoPreenchido = false;
			angular.forEach($scope.itemsPrecoFrete, function(item) {
				if(!item.id){
					if(!item.idTarifa && !item.idRota){
						existePrecoFreteNaoPreenchido = true;
					}
				}
			});
			return existePrecoFreteNaoPreenchido;
		}

		$scope.incluiNovoPrecoFrete = function() {
			if(!existeprecoFreteNaoPreenchido()){

				$scope.rest.doGet("modeloprecofrete?idTabela=" + $scope.idTabela).then(function(modeloMarkupPrecoFreteDTO) {
					var precoFreteModelo = modeloMarkupPrecoFreteDTO;

					var precoFreteTemp = {};
					precoFreteTemp.id = null;
					precoFreteTemp.idTarifa =  null;
					precoFreteTemp.nomeTarifa = null;
					precoFreteTemp.idRota = null;
					precoFreteTemp.nomeRota = null;
					precoFreteTemp.tipoParcela = "P";
					precoFreteTemp.dataVigenciaInicial = null;
					precoFreteTemp.dataVigenciaFinal = null;
					precoFreteTemp.listParcelas = [];

					angular.forEach(precoFreteModelo.listParcelas, function(parcela) {
						var parcelaTemp = {};
						parcelaTemp.idParcela = parcela.idParcela;
						parcelaTemp.nomeParcela = parcela.nomeParcela;
						parcelaTemp.valor =  null;
						parcelaTemp.idValorMarkupPrecoFrete = null;
						precoFreteTemp.listParcelas.push(parcelaTemp);
					});

					$scope.itemsPrecoFrete.push(precoFreteTemp);
					$scope.configHeaderPrecoFrete();
				});
			}
		};

		$scope.removePrecoFrete = function() {
			if (angular.isUndefined($scope.precoFreteSelected) || angular.isUndefined($scope.precoFreteSelected.length) || $scope.precoFreteSelected.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}
			var idsPrecoFrete = [];
			$scope.precoFreteSelected.forEach(function(markupPrecoFrete) {
				markupPrecoFrete.listParcelas.forEach(function(parcelaPrecoFrete) {
					if(parcelaPrecoFrete.idValorMarkupPrecoFrete){
						idsPrecoFrete.push(parcelaPrecoFrete.idValorMarkupPrecoFrete);
					}
				});
			});
			if (!idsPrecoFrete.length) {
				$scope.precoFreteSelected.forEach(function(markupPrecoFrete) {
					removeItem($scope.itemsPrecoFrete, markupPrecoFrete);
					removeItem($scope.alterados.listMarkupPrecoFreteDTO, markupPrecoFrete);
				});
				return;
			}
			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
				$scope.precoFreteSelected.forEach(function(markupPrecoFrete) {
					removeItem($scope.alterados.listMarkupPrecoFreteDTO, markupPrecoFrete);
				});
				
				$scope.rest.doPost('deletePrecoFrete', idsPrecoFrete).then(
					function(){
		   				$scope.showSuccessMessage();
						$scope.loadMarkupPrecoFrete($scope.idTabela);
					},
					function(){
						$rootScope.showLoading = false;
					});
			});
		};

		$scope.precoFreteSelected = [];
		$scope.itemsPrecoFrete = [];
		$scope.headerConfigPrecoFrete = [];
		$scope.configHeaderPrecoFrete = function() {

			$scope.itemsPrecoFrete.forEach(function(linha) {
				var templateDataInicial = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-inicial-markup.html';
				var templateDataFinal = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-final-markup.html';

				if (!chaveJaExiste($scope.headerConfigPrecoFrete, linha)) {
					var obj = {};
					if (linha.nomeTarifa != undefined) {
						obj.title = 'tarifa';
						obj.onClickField = clickFieldTarifa('precoFrete');
						obj.minWidth = '200px';
					} else {
						obj.title = 'rota';
						obj.onClickField = clickFieldRota('precoFrete');
						obj.minWidth = '500px';
					}
					obj.readOnly = true;
					obj.editable = true;
					obj.required = true;
					obj.type = 'text';
					$scope.headerConfigPrecoFrete.push(obj);

					var vigenciaInicialObj = {};
					vigenciaInicialObj.title = 'vigenciaInicial';
					vigenciaInicialObj.id = 'dt_inicial_pf';
					vigenciaInicialObj.minWidth = '150px';
					vigenciaInicialObj.editable = true;
					vigenciaInicialObj.type = 'date';
					vigenciaInicialObj.templateUrl = templateDataInicial;
					$scope.headerConfigPrecoFrete.push(vigenciaInicialObj);


					var vigenciaFinalObj = {};
					vigenciaFinalObj.title = 'vigenciaFinal';
					vigenciaFinalObj.id = 'dt_final_pf';
					vigenciaFinalObj.minWidth = '150px';
					vigenciaFinalObj.editable = true;
					vigenciaFinalObj.type = 'date';
					vigenciaFinalObj.templateUrl = templateDataFinal;
					$scope.headerConfigPrecoFrete.push(vigenciaFinalObj);
				}

				if (!linha.listParcelas) {
					return;
				}

				linha.listParcelas.forEach(function(parcela, indexParcela) {
					var grupo = buscaGrupo($scope.headerConfigPrecoFrete, parcela);
					if (!grupo) {
						var grupo = { title: parcela.nomeParcela, children: []};
						$scope.headerConfigPrecoFrete.push(grupo);
					}
					incluiColuna(grupo, 'markup', 'parc_'+parcela.idParcela+'_valor', 'perc', 2);
				});
			});

			function buscaGrupo(headerConfigPrecoFrete, parcela) {
				if (!(headerConfigPrecoFrete && parcela)) {
					return;
				}
				for (var i = 0; i < headerConfigPrecoFrete.length; i++) {
					if (headerConfigPrecoFrete[i].title === parcela.nomeParcela) {
						return headerConfigPrecoFrete[i];
					}
				}
			}

			function chaveJaExiste(headerConfigPrecoFrete, linha) {
				return headerConfigPrecoFrete.some(function(grupo) {
					var titulo = (linha.nomeTarifa ? 'tarifa' : 'rota');
					return grupo.title === titulo;
				});
			}

			function incluiColuna(grupo, titulo, id, tipo, decimais, template) {
				if (colunaJaCadastrada(grupo, titulo)) {
					return;
				}
				var minWidth = tipo === 'date' ? '200px': '50px';
				var faixaPesoTemplate = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/faixa-peso-markup.html';
				grupo.children.push({ title: titulo, id: id, type: tipo, editable: true, decimais: decimais, minWidth: minWidth, templateUrl: faixaPesoTemplate });
			}

			function colunaJaCadastrada(grupo, titulo) {
				return grupo.children && grupo.children.some(function(current) {
					return current.title === titulo;
				});
			}
			return $scope.headerConfigPrecoFrete;
		};

		$scope.rowMapperPrecoFrete = {
				map: function(linha, coluna) {
					if (!coluna.parent) {
						if (coluna.title === 'vigenciaInicial') {
							return linha.dataVigenciaInicial;
						}

						if (coluna.title === 'vigenciaFinal') {
							return linha.dataVigenciaFinal;
						}

						return linha.nomeTarifa ? linha.nomeTarifa: linha.nomeRota;
					}

					var p = $scope.buscaParcelaPrecoFrete(linha, coluna.parent.title);

					if (coluna.title === 'markup') {
						return p.valor;
					}

					return '';
				}
			};

		$scope.disableFieldPrecoFrete = function(row, column) {
			if (!column.parent) {
				if (column.title === 'vigenciaFinal') {
					return false;
				}
				for (var i = 0; i < row.listParcelas.length; i++) {
					if (angular.isDefined(row.listParcelas[i].idValorMarkupPrecoFrete) && row.listParcelas[i].idValorMarkupPrecoFrete != null) {
						return true;
					}
				}
				return false;
			}

			return false;
		};

		$scope.changeCellValuePrecoFrete = function(row, column, value) {
			if (!row) {
				return;
			}

			if (!column) {
				return;
			}

			if(!column.parent){

				if(column.title === 'rota'){
					row.nomeRota = value;
					row.idRota = value;
					return;
				}

				if(column.title === 'tarifa'){
					row.nomeTarifa = value;
					row.idTarifa = value;
					return;
				}

				if (column.title === 'vigenciaInicial') {
					row.dataVigenciaInicial = value;
					return;
				}

				if (column.title === 'vigenciaFinal') {
					row.dataVigenciaFinal = value;
					if ($scope.alterados.listMarkupPrecoFreteDTO.indexOf(row) === -1) {
						$scope.alterados.listMarkupPrecoFreteDTO.push(row);
					}
					return;
				}
			}

			var p = $scope.buscaParcelaPrecoFrete(row, column.parent.title);
			if ($scope.alterados.listMarkupPrecoFreteDTO.indexOf(row) === -1) {
				$scope.alterados.listMarkupPrecoFreteDTO.push(row);
			}

			if (column.title === 'markup') {
				p.valor = value;
			}

			return '';
		};

		$scope.buscaParcelaPrecoFrete = function(linha, titulo) {
			var parcelaVazia = {
					idParcela: '',
					nomeParcela: '',
					idValorMarkupPrecoFrete: '',
					valor: ''};
			if (!linha) {
				return parcelaVazia;
			}
			if (!linha.listParcelas) {
				return parcelaVazia;
			}
			for (var i = 0; i < linha.listParcelas.length; i++) {
				if (linha.listParcelas[i].nomeParcela === titulo) {
					return parcela = linha.listParcelas[i];
				}
			}
			linha.listParcelas.push(parcelaVazia);
			return parcelaVazia;
		};

	}
];

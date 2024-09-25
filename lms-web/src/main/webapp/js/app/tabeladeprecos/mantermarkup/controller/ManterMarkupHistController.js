
var ManterMarkupHistController = [
	"$scope",
	"$rootScope",
	"$stateParams",
	"TableFactory",
	"modalService",
	function($scope, $rootScope, $stateParams, TableFactory, modalService) {
		
		function initializeAbaHist(){
			$scope.loadMarkups($scope.idTabela);
		}
		

		$scope.itemsMinimoProgressivoHist = [];
		$scope.headerConfigMinimoProgressivoHist = [];
		$scope.minimoProgressivoSelectedHist = [];
		
		$scope.itemsPrecoFreteHist = [];
		$scope.headerConfigPrecoFreteHist = [];
		$scope.precoFreteSelectedHist = [];

		$scope.filterParams = {};
		$scope.data = {};
		$scope.dados = {};
		
		$scope.listGeneralidadeHist = [];
		var statusSalvo = 0;
		
		$scope.markupsParcelasGeneralidadeHist = new TableFactory({
			remotePagination: false
		});

		$scope.limpaListas = function() {
			$scope.markupGeral = {};
			$scope.listGeneralidadeHist = [];
			$scope.itemsMinimoProgressivoHist = [];
			$scope.itemsPrecoFreteHist = [];
			$scope.headerConfigMinimoProgressivoHist = [];
			$scope.headerConfigPrecoFreteHist = [];
			$scope.minimoProgressivoSelectedHist.length = 0; //não pode perder referência
			$scope.precoFreteSelectedHist.length = 0;
		};
		
		/** Carrega markup de acordo com a alteracao da suggest de TabelaPreco */
		$scope.loadMarkups = function(idTabela){
			$scope.limpaListas();
			if (!idTabela) {
				return;
			}
			
			$scope.rest.doGet("findtabelapreco?idTabela=" + idTabela).then(function(tabelaPrecoSuggestDTO) {
				$scope.dados.tabela = tabelaPrecoSuggestDTO;
			});

			$scope.loadInicial(idTabela);
		};

		/** Carrega todas as listas de forma encadeada para poder funcionar o loader de tela */
		$scope.loadInicial = function(idTabela){
			$rootScope.showLoading = true;

			//loadMarkupGeral
			$scope.rest.doGet("tabelapreco?idTabela=" + idTabela).then(function(markupGeralDTO) {
				$scope.markupGeral = markupGeralDTO;

				//loadMarkupMinimoProgressivo
				$scope.itemsMinimoProgressivoHist = [];
				$scope.headerConfigMinimoProgressivoHist = [];
				$scope.minimoProgressivoSelectedHist.length = 0;
				$scope.rest.doGet("minimoprogressivohist?idTabela=" + idTabela).then(function(listMarkupMinimoProgressivoDTO) {
					$scope.itemsMinimoProgressivoHist = listMarkupMinimoProgressivoDTO;
					$scope.configHeaderMinimoProgressivo();

					// loadMarkupPrecoFrete
					$scope.itemsPrecoFreteHist = [];
					$scope.headerConfigPrecoFreteHist = [];
					$scope.precoFreteSelectedHist.length = 0;
					$scope.rest.doGet("precofretehist?idTabela=" + idTabela).then(function(listMarkupPrecoFreteDTO) {
						$scope.itemsPrecoFreteHist = listMarkupPrecoFreteDTO;
						$scope.configHeaderPrecoFrete();

						//loadMarkupGeneralidade - parte 1
						$scope.listGeneralidadeHist = [];
						$scope.rest.doGet("generalidadehist?idTabela=" + idTabela).then(function(listMarkupDto) {
							angular.forEach(listMarkupDto, function(markup) {
								$scope.listGeneralidadeHist.push({ markup: markup, status: statusSalvo });
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
			$scope.itemsMinimoProgressivoHist = [];
			$scope.headerConfigMinimoProgressivoHist = [];
			$scope.minimoProgressivoSelectedHist.length = 0;
			$scope.rest.doGet("minimoprogressivohist?idTabela=" + idTabela).then(function(listMarkupMinimoProgressivoDTO) {
				$scope.itemsMinimoProgressivoHist = listMarkupMinimoProgressivoDTO;
				$scope.configHeaderMinimoProgressivo();
				$rootScope.showLoading = false;
			});
		};

		$scope.loadMarkupPrecoFrete = function(idTabela){
			$rootScope.showLoading = true;
			$scope.itemsPrecoFreteHist = [];
			$scope.headerConfigPrecoFreteHist = [];
			$scope.precoFreteSelectedHist.length = 0;
			$scope.rest.doGet("precofretehist?idTabela=" + idTabela).then(function(listMarkupPrecoFreteDTO) {
				$scope.itemsPrecoFreteHist = listMarkupPrecoFreteDTO;
				$scope.configHeaderPrecoFrete();
				$rootScope.showLoading = false;
			});
		};

		$scope.loadMarkupGeneralidade = function(idTabela){
			$rootScope.showLoading = true;
			$scope.listGeneralidadeHist = [];
			$scope.rest.doGet("generalidadehist?idTabela=" + idTabela).then(function(listMarkupDto) {
				angular.forEach(listMarkupDto, function(markup) {
					$scope.listGeneralidadeHist.push({ markup: markup, status: statusSalvo });
				});
				$rootScope.showLoading = false;
			});
			$scope.rest.doGet('tabelapreco/'+$scope.idTabela+'/parcelas?tipoPrecificacao=Generica').then(function(parcelas) {
				$scope.listParcelasTipoGenerica = parcelas;
				$rootScope.showLoading = false;
			});
		};

		$scope.removeGeneralidadeHist = function() {
			if (angular.isUndefined($scope.markupsParcelasGeneralidadeHist.selected) || angular.isUndefined($scope.markupsParcelasGeneralidadeHist.selected.length) || $scope.markupsParcelasGeneralidadeHist.selected.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}
			
			var idsMarkupsGeneralidadesExcluir = [];
			$scope.markupsParcelasGeneralidadeHist.selected.forEach(function(markupGeneralidade) {
				if(markupGeneralidade.markup.id){
					idsMarkupsGeneralidadesExcluir.push(markupGeneralidade.markup.id);
				}
			});
			
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
				$scope.markupsParcelasGeneralidadeHist.selected.forEach(function(markupGeneralidade) {
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
		
		//-------------------------------------------------------------------------//
		//-------------------- Callbacks de minimo progressivo --------------------//
		$scope.configHeaderMinimoProgressivo = function() {

			$scope.itemsMinimoProgressivoHist.forEach(function(linha) {
				var templateDataInicial = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-inicial-markup.html';
				var templateDataFinal = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-final-markup.html';


				if (!chaveJaExiste($scope.headerConfigMinimoProgressivoHist, linha)) {
					var obj = {};
					if (linha.nomeTarifa != undefined) {
						obj.title = 'tarifa';
						obj.minWidth = '100px';
					} else {
						obj.title = 'rota';
						obj.minWidth = '500px';
					}
					obj.readOnly = true;
					obj.editable = true;
					obj.required = true;
					obj.type = 'text';

					$scope.headerConfigMinimoProgressivoHist.push(obj);


					var vigenciaInicialObj = {};
					vigenciaInicialObj.title = 'vigenciaInicial';
					vigenciaInicialObj.id = 'dt_inicial';
					vigenciaInicialObj.minWidth = '150px';
					vigenciaInicialObj.editable = true;
					vigenciaInicialObj.type = 'date';
					vigenciaInicialObj.templateUrl = templateDataInicial;
					$scope.headerConfigMinimoProgressivoHist.push(vigenciaInicialObj);


					var vigenciaFinalObj = {};
					vigenciaFinalObj.title = 'vigenciaFinal';
					vigenciaFinalObj.id = 'dt_final';
					vigenciaFinalObj.minWidth = '150px';
					vigenciaFinalObj.editable = true;
					vigenciaFinalObj.type = 'date';
					vigenciaFinalObj.templateUrl = templateDataFinal;
					$scope.headerConfigMinimoProgressivoHist.push(vigenciaFinalObj);

				}

				if (!linha.listParcelas) {
					return;
				}

				linha.listParcelas.forEach(function(parcela, indexParcela) {
					var grupo = buscaGrupo($scope.headerConfigMinimoProgressivoHist, parcela);
					if (!grupo) {
						var grupo = { title: parcela.nomeParcela, children: []};
						$scope.headerConfigMinimoProgressivoHist.push(grupo);
					}

					parcela.listValoresFaixaProgressiva.forEach(function(faixa, indexFaixa) {
						var titulo = faixa.faixa.replace(/([V])([\d,.]+)/, '$2 Kg').replace(/([P])([\d]+)/, '\$2');
						var campo = "";
						incluiColuna(grupo, titulo, 'parc_'+parcela.idParcela+'_fx_'+faixa.faixa, 'perc', 2);
					});
				});
			});

			function buscaGrupo(headerConfigMinimoProgressivoHist, parcela) {
				if (!(headerConfigMinimoProgressivoHist && parcela)) {
					return;
				}
				for (var i = 0; i < headerConfigMinimoProgressivoHist.length; i++) {
					if (!!headerConfigMinimoProgressivoHist[i].title && headerConfigMinimoProgressivoHist[i].title === parcela.nomeParcela) {
						return headerConfigMinimoProgressivoHist[i];
					}
				}
			}

			function chaveJaExiste(headerConfigMinimoProgressivoHist, linha) {
				return headerConfigMinimoProgressivoHist.some(function(grupo) {
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
			return $scope.headerConfigMinimoProgressivoHist;
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

		$scope.removeMinimoProgressivoHist = function() {
			if (angular.isUndefined($scope.minimoProgressivoSelectedHist) || angular.isUndefined($scope.minimoProgressivoSelectedHist.length) || $scope.minimoProgressivoSelectedHist.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}

			var idsValorMarkupFaixaProgressiva = [];
			$scope.minimoProgressivoSelectedHist.forEach(function(markupMinimoProgressivo) {
				if (!markupMinimoProgressivo.listParcelas) {
					removeItem($scope.itemsMinimoProgressivoHist, markupMinimoProgressivo);
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

			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
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

		$scope.rowMapperMinimoProgressivoHist = {
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
		
		//------------------------ Fim Minimo Progressivo -------------------------//
		//-------------------------------------------------------------------------//

		//-------------------------------------------------------------------------//
		//------------------------ Preco Frete -------------------------//

		$scope.removePrecoFreteHist = function() {
			if (angular.isUndefined($scope.precoFreteSelectedHist) || angular.isUndefined($scope.precoFreteSelectedHist.length) || $scope.precoFreteSelectedHist.length == 0) {
				$scope.addAlerts([{msg: $scope.getMensagem("erSemRegistro"), type: 'warning'}]);
				return;
			}
			var idsPrecoFrete = [];
			$scope.precoFreteSelectedHist.forEach(function(markupPrecoFrete) {
				markupPrecoFrete.listParcelas.forEach(function(parcelaPrecoFrete) {
					if(parcelaPrecoFrete.idValorMarkupPrecoFrete){
						idsPrecoFrete.push(parcelaPrecoFrete.idValorMarkupPrecoFrete);
					}
				});
			});
			$scope.confirm($scope.getMensagem("erExcluir")).then(function() {
				$rootScope.showLoading = true;
				
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

		$scope.configHeaderPrecoFrete = function() {

			$scope.itemsPrecoFreteHist.forEach(function(linha) {
				var templateDataInicial = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-inicial-markup.html';
				var templateDataFinal = contextPath + 'js/app/tabeladeprecos/mantermarkup/view/data-vigencia-final-markup.html';

				if (!chaveJaExiste($scope.headerConfigPrecoFreteHist, linha)) {
					var obj = {};
					if (linha.nomeTarifa != undefined) {
						obj.title = 'tarifa';
						obj.minWidth = '200px';
					} else {
						obj.title = 'rota';
						obj.minWidth = '500px';
					}
					obj.readOnly = true;
					obj.editable = true;
					obj.required = true;
					obj.type = 'text';
					$scope.headerConfigPrecoFreteHist.push(obj);

					var vigenciaInicialObj = {};
					vigenciaInicialObj.title = 'vigenciaInicial';
					vigenciaInicialObj.id = 'dt_inicial_pf';
					vigenciaInicialObj.minWidth = '150px';
					vigenciaInicialObj.editable = true;
					vigenciaInicialObj.type = 'date';
					vigenciaInicialObj.templateUrl = templateDataInicial;
					$scope.headerConfigPrecoFreteHist.push(vigenciaInicialObj);


					var vigenciaFinalObj = {};
					vigenciaFinalObj.title = 'vigenciaFinal';
					vigenciaFinalObj.id = 'dt_final_pf';
					vigenciaFinalObj.minWidth = '150px';
					vigenciaFinalObj.editable = true;
					vigenciaFinalObj.type = 'date';
					vigenciaFinalObj.templateUrl = templateDataFinal;
					$scope.headerConfigPrecoFreteHist.push(vigenciaFinalObj);
				}

				if (!linha.listParcelas) {
					return;
				}

				linha.listParcelas.forEach(function(parcela, indexParcela) {
					var grupo = buscaGrupo($scope.headerConfigPrecoFreteHist, parcela);
					if (!grupo) {
						var grupo = { title: parcela.nomeParcela, children: []};
						$scope.headerConfigPrecoFreteHist.push(grupo);
					}
					incluiColuna(grupo, 'markup', 'parc_'+parcela.idParcela+'_valor', 'perc', 2);
				});
			});

			function buscaGrupo(headerConfigPrecoFreteHist, parcela) {
				if (!(headerConfigPrecoFreteHist && parcela)) {
					return;
				}
				for (var i = 0; i < headerConfigPrecoFreteHist.length; i++) {
					if (headerConfigPrecoFreteHist[i].title === parcela.nomeParcela) {
						return headerConfigPrecoFreteHist[i];
					}
				}
			}

			function chaveJaExiste(headerConfigPrecoFreteHist, linha) {
				return headerConfigPrecoFreteHist.some(function(grupo) {
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
			return $scope.headerConfigPrecoFreteHist;
		};

		$scope.rowMapperPrecoFreteHist = {
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

		initializeAbaHist();
	}
];


var ConsultarLocalizacoesMercadoriasCadController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	"$filter",
	"$locale",
	"$location",
	"modalService",
	"ConsultarLocalizacaoMercadoriaFactory",
	"TableFactory",
	function($rootScope, $scope, $stateParams, $filter, $locale, $location, modalService, ConsultarLocalizacaoMercadoriaFactory, TableFactory) {

		/* Aba Principal - Notas Fiscais */
		$scope.notasFiscaisTable = new TableFactory({
			service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoNotasFiscais
		});

		$scope.loadAbaPrincipalNotasFiscais = function() {
			$scope.isAbaPrincipalNotasFiscaisOpen = true;

			$scope.loadNotasFiscaisTable();
		};

		$scope.loadNotasFiscaisTable = function() {
			$scope.notasFiscaisTable.load($scope.data.idDoctoServico);
		};

		/* Aba Principal - Volumes */
		$scope.volumesTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoVolumes,
				remotePagination: true
			}
		);

		$scope.loadAbaPrincipalVolumes = function() {
			$scope.isAbaPrincipalVolumesOpen = true;
			$scope.loadVolumesTable();
		};

		$scope.loadVolumesTable = function() {
			var filtro = {};
			filtro.filtros = {};
			filtro.filtros.idDoctoServico = $scope.data.idDoctoServico;

			$scope.volumesTable.load(filtro);

		};

		/* Aba Integrantes */
		$scope.integrantesTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoIntegrantes
			}
		);

		$scope.loadAbaIntegrantes = function() {
			$scope.isAbaIntegrantesOpen = true;

			$scope.loadIntegrantesTable();
		};

		$scope.loadIntegrantesTable = function() {
			$scope.integrantesTable.load($scope.data.idDoctoServico);
		};



		/* Aba Complementos */
		$scope.complementosObservacoesTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosObservacoes
			}
		);

		/* Aba Complementos - Observacoes  */
		$scope.loadAbaComplementos = function() {
			$scope.isAbaComplementosOpen = true;
			$scope.loadComplementosObservacoesTable();
		};

		$scope.loadComplementosObservacoesTable = function() {
			$scope.complementosObservacoesTable.load($scope.data.idDoctoServico);
		};

		/* Aba Complementos - Embalagens */
		$scope.complementosEmbalagensTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosEmbalagens
			}
		);

		$scope.loadAbaComplementosEmbalagens = function() {
			$scope.isAbaComplementosEmbalagensOpen = true;
			$scope.loadComplementosEmbalagensTable();
		};

		$scope.loadComplementosEmbalagensTable = function() {
			$scope.complementosEmbalagensTable.load($scope.data.idDoctoServico);
		};

		/* Aba Complementos - Dados Complementares */
		$scope.dadosComplementaresDoctoServicoTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosDoctoServico
			}
		);
		$scope.dadosComplementaresNotasFiscaisTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosNotasFiscais
			}
		);

		$scope.loadAbaComplementosDadosComplementares = function() {
			$scope.isAbaComplementosDadosComplementaresOpen = true;
			$scope.loadDadosComplementaresDoctoServicoTable();
			$scope.loadDadosComplementaresNotasFiscaisTable();
		};

		$scope.loadDadosComplementaresDoctoServicoTable = function() {
			$scope.dadosComplementaresDoctoServicoTable.load($scope.data.idDoctoServico);
		};

		$scope.loadDadosComplementaresNotasFiscaisTable = function() {
			$scope.dadosComplementaresNotasFiscaisTable.load($scope.data.idDoctoServico);
		};

		/* Aba Complementos - Agendamentos */
		$scope.complementosAgendamentosTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosAgendamentos
			}
		);

		$scope.loadComplementosAgendamentos = function() {
			$scope.isComplementosAgendamentosOpen = true;
			$scope.loadComplementosAgendamentosTable();
		};

		$scope.loadComplementosAgendamentosTable = function() {
			$scope.complementosAgendamentosTable.load($scope.data.idDoctoServico).then(function(data) {
				if (data.length == 1) {
					$scope.openAgendamento(data[0]);
				}
			});
		};

		$scope.openAgendamento = function(agendamento) {
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.complementos) {
				$scope.abas.complementos = {};
			}
			$scope.abas.complementos.agendamentos = {};
			ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosAgendamentosFindById(agendamento.idAgendamentoEntrega).then(function(data) {
				$scope.abas.complementos.agendamentos = data;
			});

		};

		/* Aba Complementos - Outros */
		$scope.loadComplementosOutros = function() {
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.complementos) {
				$scope.abas.complementos = {};
			}
			$scope.abas.complementos.outros = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoComplementosOutros($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.complementos.outros = data;
				$rootScope.showLoading = false;

			});
		};

		/* Aba Frete - Dados Frete */
		$scope.freteCalculoParcelaTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedParcelaPreco
			}
		);
		$scope.freteCalculoServicosTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedCalculoServico
			}
		);

		$scope.loadAbaFrete = function() {
			$scope.isFreteOpen = true;
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.frete) {
				$scope.abas.frete = {};
			}

			$scope.loadFreteCalculoParcelaTable();
			$scope.loadFreteCalculoServicosTable();

			$scope.abas.frete.dadosFrete = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findTotaisCalculoServico($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.frete.dadosFrete = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.loadFreteCalculoParcelaTable = function() {
			$scope.freteCalculoParcelaTable.load($scope.data.idDoctoServico);
		};

		$scope.loadFreteCalculoServicosTable = function() {
			$scope.freteCalculoServicosTable.load($scope.data.idDoctoServico);
		};

		/* Aba Frete - Impostos */
		$scope.freteImpostosTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedImpostos
			}
		);

		$scope.loadAbaFreteImpostos = function() {
			$scope.isFreteImpostosOpen = true;

			$scope.loadFreteImpostosTable();

			$scope.abas.frete.impostos = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findTipoTributacaoIcms($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.frete.impostos = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.loadFreteImpostosTable = function() {
			$scope.freteImpostosTable.load($scope.data.idDoctoServico);
		};

		/* Aba Frete - Calculo */
		$scope.loadAbaFreteCalculo = function() {
			$scope.isFreteCalculoOpen = true;

			$scope.abas.frete.calculo = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findDadosCalculoFrete($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.frete.calculo = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Aba Frete - Dados Estatisticos */
		$scope.loadAbaFreteDadosEstatisticos = function() {
			$scope.isFreteDadosEstatisticosOpen = true;

			$scope.abas.frete.dadosEstatisticos = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findDadosCalculoFrete($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.frete.dadosEstatisticos = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Aba Frete - Servicos Adicionais */
		$scope.loadAbaFreteServicosAdicionais = function() {
			$scope.isFreteDadosEstatisticosOpen = true;

			$scope.abas.frete.dadosEstatisticos = {};
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findDadosCalculoFrete($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.frete.dadosEstatisticos = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Aba Cobranca */
		$scope.cobrancaTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedDevedorDocServFatByDoctoServico
			}
		);

		$scope.loadAbaCobranca = function() {
			$scope.isCobrancaOpen = true;
			$scope.loadCobrancaTable();
		};

		$scope.loadCobrancaTable = function() {
			$rootScope.showLoading = true;

			$scope.cobrancaTable.load($scope.data.idDoctoServico).then(function(data) {
				if (data.length == 1) {
					$scope.openCobranca(data[0]);
					$rootScope.showLoading = false;

				}
			}, function() {$rootScope.showLoading = false;})
				['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.openCobranca = function(cobranca) {
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.cobranca) {
				$scope.abas.cobranca = {};
			}
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findDevedorDocServFatDetail(cobranca.idDevedorDocServFat).then(function(data) {
				$scope.abas.cobranca = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Aba CC */
		$scope.ccTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedControleCarga
			}
		);

		$scope.loadAbaCc = function() {
			$scope.isCcOpen = true;
			$scope.loadCcTable();
		};

		$scope.loadCcTable = function() {
			$scope.ccTable.load($scope.data.idDoctoServico);
		};

		/* Aba Manifesto Coleta */
		$scope.loadAbaManifesto = function() {
			$scope.isManifestoColetaOpen = true;
			$scope.loadManifestoColetaTable();
		};

		$scope.manifestoColetaTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findManifestoColetaByIdDoctoServico
			}
		);

		$scope.loadManifestoColetaTable = function() {
			$scope.manifestoColetaTable.load($scope.data.idDoctoServico);
		};

		/* Aba Manifesto - Viagem */
		$scope.manifestoViagemTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedManifestoViagem
			}
		);

		$scope.loadAbaManifestoViagem = function() {
			$scope.isManifestoViagemOpen = true;
			$scope.loadManifestoViagemTable();
		};

		$scope.loadManifestoViagemTable = function() {
			$scope.manifestoViagemTable.load($scope.data.idDoctoServico);
		};

		/* Aba Manifesto - Entrega */
		$scope.manifestoEntregaTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedManifestoEntrega
			}
		);

		$scope.loadAbaManifestoEntrega = function() {
			$scope.isManifestoEntregaOpen = true;
			$scope.loadManifestoEntregaTable();
		};

		$scope.loadManifestoEntregaTable = function() {
			$scope.manifestoEntregaTable.load($scope.data.idDoctoServico);
		};

		/* Aba NF Manifesto de Entrega */
		$scope.manifestoEntregaNotasFiscaisTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedManifestoEntregaNotasFiscais
			}
		);

		$scope.loadAbaManifestoEntregaNotasFiscais = function() {
			$scope.isManifestoEntregaNotasFiscaisOpen = true;
			$scope.loadManifestoEntregaNotasFiscaisTable();
		};

		$scope.loadManifestoEntregaNotasFiscaisTable = function() {
			$scope.manifestoEntregaNotasFiscaisTable.load($scope.data.idDoctoServico);
		};

		/* Aba Eventos */
		$scope.eventosTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedEventos
			}
		);

		$scope.loadAbaEventos = function() {
			$scope.isEventosOpen = true;
			$scope.loadEventosTable();
		};

		$scope.loadEventosTable = function() {
			$scope.eventosTable.load($scope.data.idDoctoServico);
		};

		/* Aba AWB */
		$scope.loadAbaAwb = function() {
			$scope.isAwbOpen = true;
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.awb) {
				$scope.abas.awb = {};
			}
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findAwbByIdDoctoServico($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.awb.awbInfo = data;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Aba Tracking Awb */
		$scope.trackingAwbTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedTrackingAwb
			}
		);

		$scope.loadAbaTrackingAwb = function() {
			$scope.isTrackingAwbOpen = true;
			$rootScope.showLoading = true;

			$scope.rest.doGet("findById/trackingAwb/dtUltimaAtualizacao?idDoctoServico=" + $scope.data.idDoctoServico).then(function (data){
				$scope.dtUltimaAtualizacao = data.dtUltimaAtualizacao;
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
			$scope.loadTrackingAwbTable();
		};

		$scope.loadTrackingAwbTable = function() {
			$scope.trackingAwbTable.load($scope.data.idDoctoServico);
		};

		/* Aba Historico Awb */
		$scope.historicoAwbTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedHistoricoAwb
			}
		);

		$scope.loadAbaHistoricoAwb = function() {
			$scope.isHistoricoAwbOpen = true;
			$scope.loadHistoricoAwbTable();
		};

		$scope.loadHistoricoAwbTable = function() {
			$scope.historicoAwbTable.load($scope.data.idDoctoServico);
		};

		/* Aba RNC */
		$scope.rncTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedOcorrenciaNaoConformidade
			}
		);

		$scope.loadAbaRnc = function() {
			$scope.isRncOpen = true;
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.rnc) {
				$scope.abas.rnc = {};
			}
			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findByIdDetailAbaRNC($scope.data.idDoctoServico).then(function(data) {
				$scope.abas.rnc = data;

				if(data.idNaoConformidade){
					$scope.loadRncTable();
				}
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.loadRncTable = function() {
			$scope.rncTable.load($scope.abas.rnc.idNaoConformidade);
		};

		/* Aba Bloqueios/Liberacoes */
		$scope.bloqueiosLiberacoesTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedBloqueiosLiberacoes
			}
		);

		$scope.loadAbaBloqueios = function() {
			$scope.isBloqueiosOpen = true;
			$scope.loadBloqueiosLiberacoesTable();
		};

		$scope.loadBloqueiosLiberacoesTable = function() {
			$scope.bloqueiosLiberacoesTable.load($scope.data.idDoctoServico);
		};

		/* Aba Indenizacoes */
		$scope.isIndenizacoesDebitoOpen = true;
		$scope.isIndenizacoesFavorecidoOpen = true;

		$scope.indenizacoesDebitoTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedFilialDebitada
			}
		);

		$scope.loadAbaIndenizacoes = function() {
			$scope.isIndenizacoesOpen = true;
			if (!$scope.abas) {
				$scope.abas = {};
			}
			if (!$scope.abas.indenizacoes) {
				$scope.abas.indenizacoes = {};
			}

			$rootScope.showLoading = true;

			ConsultarLocalizacaoMercadoriaFactory.findIndenizacaoById($scope.data.idDoctoServicoIndenizacao).then(function(data) {
				$scope.abas.indenizacoes.infBas = data;

				$scope.loadIndenizacoesDebitoTable();
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.loadIndenizacoesDebitoTable = function() {
			$scope.indenizacoesDebitoTable.load($scope.data.idDoctoServicoIndenizacao);
		};

		/* Aba Indenizacoes - Eventos */
		$scope.indenizacoesEventosTable = new TableFactory(
			{
				service: ConsultarLocalizacaoMercadoriaFactory.findPaginatedEventosRim,
				remotePagination: true
			}
		);

		$scope.loadAbaIndenizacoesEventos = function() {
			$scope.isIndenizacoesEventosOpen = true;

			$scope.loadIndenizacoesEventosTable();
		};

		$scope.loadIndenizacoesEventosTable = function() {
			var filtro = {};
			filtro.filtros = {};
			filtro.filtros.idReciboIndenizacao = $scope.data.idReciboIndenizacao;

			$scope.indenizacoesEventosTable.load(filtro);

		};

		$scope.remark = function() {
			var idDoctoServico = $scope.data.idDoctoServico;

			var EventosRemarkCtrl = ["$scope", "$modalInstance", "TableFactory", "ConsultarLocalizacaoMercadoriaFactory", function ($scope, $modalInstance, TableFactory, ConsultarLocalizacaoMercadoriaFactory) {
				$scope.dsObservacao = {};
				$scope.title = $scope.$eval("'remark' | translate");
				$scope.innerTemplate = contextPath+"js/app/sim/consultarlocalizacoesmercadorias/view/consultarLocalizacoesMercadoriasObservacoesPopup.html";

				$scope.popupTable = new TableFactory (
					{
						service: ConsultarLocalizacaoMercadoriaFactory.findRemarks,
						remotePagination: false
					});

				$scope.filtro = {field: "idDoctoServico", value: idDoctoServico};

				var filtro = {};
				filtro.filtros = {};
				filtro.filtros[$scope.filtro.field] = $scope.filtro.value;
				$scope.popupTable.load(filtro);

				$scope.close = function () {
					$modalInstance.close();
				};

				$scope.salvarObservacao = function() {
					var remark = {};
					remark.idDoctoServico = idDoctoServico;
					remark.dsObservacao = $scope.dsObservacao.value;

					ConsultarLocalizacaoMercadoriaFactory.storeRemark(remark).then(function(response) {
						$scope.dsObservacao.value = "";
						$scope.popupTable.load(filtro);
					});
				};

				$scope.preencheDsConsulta = function(row) {
					$scope.dsConsulta = row.dsObservacao;
				};

			}];

			modalService.open({windowClass: "clmListaPopup", controller: EventosRemarkCtrl});

		};

		$scope.imprimirInformacoesVolumes = function(formato) {
			$rootScope.showLoading = true;

			var o = {};
			o.idDoctoServico = $scope.data.idDoctoServico;
			o.formatoRelatorio = formato;
			ConsultarLocalizacaoMercadoriaFactory.imprimirInformacoesVolumes(o).then(function(data) {
				$rootScope.showLoading = false;

				location.href = contextPath+"/viewBatchReport?"+data.fileName;
			},function() {
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		/* Botoes da aba listagem */
		$scope.openMCD = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			$scope.openLms('municipios/consultarMCD.do?cmd=main');
		};

		$scope.openSPE = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			var parans = "&isPopup=true";
			parans += "&idDoctoServico="+$scope.data.idDoctoServico;
			parans += "&doctoServico.idDoctoServico="+$scope.data.idDoctoServico;
			parans += "&doctoServico.nrDoctoServico="+ $scope.data.nrDoctoServico;
			parans += "&doctoServico.tpDocumentoServico="+ $scope.data.tpDocumentoServicoLinkProperty;
			parans += "&doctoServico.filialByIdFilialOrigem.idFilial="+ $scope.data.idFilial;
			parans += "&doctoServico.filialByIdFilialOrigem.sgFilial="+ $scope.data.dsSgFilial;

			$scope.openLms('sim/registrarSolicitacoesEmbarque.do?cmd=main'+parans);
		};

		$scope.openSR = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			var parans = "&isPopup=true";
			parans += "&idDoctoServico="+$scope.data.idDoctoServico;
			parans += "&doctoServico.idDoctoServico="+$scope.data.idDoctoServico;
			parans += "&doctoServico.nrDoctoServico="+ $scope.data.nrDoctoServico;
			parans += "&doctoServico.tpDocumentoServico="+ $scope.data.tpDocumentoServicoLinkProperty;
			parans += "&doctoServico.filialByIdFilialOrigem.idFilial="+ $scope.data.idFilial;
			parans += "&doctoServico.filialByIdFilialOrigem.sgFilial="+ $scope.data.dsSgFilial;

			$scope.openLms('sim/registrarSolicitacoesRetirada.do?cmd=main'+parans);
		};

		$scope.openRNC = function($event, id) {
			$event.preventDefault();
			$event.stopPropagation();

			var parans = "&idOcorrenciaNaoConformidadeLocMerc="+id;

			$scope.openLms('rnc/manterOcorrenciasNaoConformidade.do?cmd=main'+parans);
		};

		$scope.openModalNotaFiscalOperada = function(notaFiscal) {

			/* Configuracao da modal padrao para as telas */
			var NotaFiscalOperadaCtrl = ["$scope", "$modalInstance", "TableFactory", "ConsultarLocalizacaoMercadoriaFactory", function ($scope, $modalInstance, TableFactory, ConsultarLocalizacaoMercadoriaFactory) {
				$scope.innerTemplate = contextPath+"js/app/sim/consultarlocalizacoesmercadorias/view/consultarLocalizacoesMercadoriasPopupNotaFiscalOperada.html";
				$scope.filtro = {field: "idNotaFiscalConhecimento", value: notaFiscal.idNotaFiscalConhecimento};
				$scope.notaFiscal = notaFiscal;

				$scope.popupTableNotaFiscalOperada = new TableFactory({
					service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoAbaPrincipalNotasFiscaisOperadas,
					remotePagination: true
				});

				var filtro = {};
				filtro.filtros = {};
				filtro.filtros[$scope.filtro.field] = $scope.filtro.value;

				$scope.popupTableNotaFiscalOperada.load(filtro);

				$scope.close = function () {
					$modalInstance.close();
				};
			}];

			modalService.open({windowClass: "clmListaPopup", controller: NotaFiscalOperadaCtrl});


		};

		$scope.openEventosVolume = function(volume) {

			/* Configuracao da modal padrao para as telas */
			var EventosVolumeCtrl = ["$scope", "$modalInstance", "TableFactory", "ConsultarLocalizacaoMercadoriaFactory", function ($scope, $modalInstance, TableFactory, ConsultarLocalizacaoMercadoriaFactory) {
				$scope.title = $scope.$eval("'eventos' | translate");
				$scope.innerTemplate = contextPath+"js/app/sim/consultarlocalizacoesmercadorias/view/consultarLocalizacoesMercadoriasPopupLista.html";
				$scope.columns = [
					{ title: "'dataInclusao' | translate", field: "row.dhInclusao | dateTimeZone", visible: true },
					{ title: "'filial' | translate", field: "row.sgFilial", visible: true },
					{ title: "'evento' | translate", field: "row.cdEvento + ' - ' + row.dsEvento", visible: true },
					{ title: "'complemento' | translate", field: "row.obComplemento", visible: true },
					{ title: "'ocorrenciaEntrega' | translate", field: "row.cdOcorrenciaEntrega + (row.dsOcorrenciaEntrega ? ' - ' + row.dsOcorrenciaEntrega : '')", visible: true },
					{ title: "'tipoScan' | translate", field: "row.tpScan", visible: true },
					{ title: "'usuario' | translate", field: "row.nmUsuario", visible: true }
				];
				$scope.subcolumns = null;
				$scope.filtro = {field: "idVolumeNotaFiscal", value: volume.idVolumeNotaFiscal};
				$scope.telefonesGerais = [];
				$scope.popupTable = new TableFactory (
					{
						service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoAbaPrincipalEventosVolume,
						remotePagination: true
					});

				var filtro = {};
				filtro.filtros = {};
				filtro.filtros[$scope.filtro.field] = $scope.filtro.value;

				$scope.popupTable.load(filtro);

				$scope.close = function () {
					$modalInstance.close();
				};
			}];

			modalService.open({windowClass: "clmListaPopup", controller: EventosVolumeCtrl});

		};

		$scope.openMacroZona = function($event, macroZona) {
			openMacroZona(modalService, $event, macroZona.idMacroZona);
		};

		$scope.openSituacaoWorkflow = function () {

			$scope.lista = [];
			var idDoctoServico = $scope.data.idDoctoServico;
			var EventosSituacaoWorkflow = ["$scope", "$modalInstance", "TableFactory", "ConsultarLocalizacaoMercadoriaFactory", function ($scope, $modalInstance, TableFactory, ConsultarLocalizacaoMercadoriaFactory) {
				$scope.innerTemplate = contextPath+"js/app/sim/consultarlocalizacoesmercadorias/view/consultarLocalizacoesMercadoriasSituacaoWorkflow.html";

				$scope.listaSituacaoWorkflow = new TableFactory({
					service: ConsultarLocalizacaoMercadoriaFactory.findPendenciaSituacaoWorkflow,
					remotePagination: false
				});

				$scope.listaSituacaoWorkflow.load(idDoctoServico);

				$scope.close = function () {
					$modalInstance.close();
				};
			}];

			modalService.open({windowClass: "clmListaPopup", controller: EventosSituacaoWorkflow});
		};

		$scope.imprimirRNC = function() {
			$rootScope.showLoading = true;

			var o = {};
			o.idNaoConformidade = $scope.abas.rnc.idNaoConformidade;
			o.nrNaoConformidade = $scope.abas.rnc.nrNaoConformidade;
			o.sgFilial = $scope.abas.rnc.sgFilial;

			ConsultarLocalizacaoMercadoriaFactory.imprimirRNC(o).then(function(data) {
				$rootScope.showLoading = false;

				location.href = contextPath+"/viewBatchReport?"+data.fileName;
			},function() {
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};


		$scope.openEnderecosIntegrante = function(integrante) {

			/* Configuracao da modal padrao para as telas */
			var EventosVolumeCtrl = ["$scope", "$modalInstance", "TableFactory", "ConsultarLocalizacaoMercadoriaFactory", function ($scope, $modalInstance, TableFactory, ConsultarLocalizacaoMercadoriaFactory) {
				$scope.title = $scope.$eval("'enderecos' | translate");
				$scope.innerTemplate = contextPath+"js/app/sim/consultarlocalizacoesmercadorias/view/consultarLocalizacoesMercadoriasPopupLista.html";
				$scope.columns = [
					{ title: "'endereco' | translate", field: "row.enderecoCompleto", sortable: "'enderecoCompleto'", visible: true },
					{ title: "'tipoEndereco' | translate", field: "row.tpEndereco", sortable: "'tpEndereco'", visible: true },
					{ title: "'vigenciaInicial' | translate", field: "row.dtVigenciaInicial", sortable: "'dtVigenciaInicial'", visible: true },
					{ title: "'vigenciaFinal' | translate", field: "row.dtVigenciaFinal", sortable: "'dtVigenciaFinal'", visible: true }
				];
				$scope.subcolumns = [
					{ title: "'tipoTelefone' | translate", field: "subrow.tpTelefone", visible: true },
					{ title: "'usoTelefone' | translate", field: "subrow.tpUso", visible: true },
					{ title: "'ddi' | translate", field: "subrow.nrDdi", visible: true },
					{ title: "'numero' | translate", field: "'('+subrow.nrDdd+') ' + subrow.nrTelefone", visible: true }
				];
				$scope.filtro = {field: 'idPessoa', value: integrante.idCliente};
				ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoIntegrantesEnderecos({filtros:{idPessoa: integrante.idCliente, geral: true}}).then(function(data) {
					$scope.telefonesGerais = data;
				});
				$scope.popupTable = new TableFactory (
					{
						service: ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoIntegrantesEnderecos,
						remotePagination: false
					});

				var filtro = {};
				filtro.filtros = {};
				filtro.filtros[$scope.filtro.field] = $scope.filtro.value;

				$scope.popupTable.load(filtro);

				$scope.close = function () {
					$modalInstance.close();
				};
			}];

			modalService.open({windowClass: "clmListaPopup", controller: EventosVolumeCtrl});

		};

		$scope.visualizar = function() {
			$rootScope.showLoading = true;

			var o = {};
			o.idDoctoServico = $scope.data.idDoctoServico;
			ConsultarLocalizacaoMercadoriaFactory.visualizar(o).then(function(data) {
				$rootScope.showLoading = false;
				location.href = contextPath+"/viewBatchReport?"+data.fileName;
			},function() {
				$rootScope.showLoading = false;

			})['finally'](function() {
				$rootScope.showLoading = false;

			});
		};

		$scope.imagem = function($event) {

			$event.preventDefault();
			$event.stopPropagation();

			var data = Utils.Date.formatDateYYYYMMDD($scope.data.dsDhEmissao, $locale);
			var url = $scope.data.urlWorkImage + "/ConhecimentoServlet?empresa=1&filial="+$filter("lpad")($scope.data.cdFilial,3)+"&numero="+$scope.data.nrDoctoServico+"&data="+data;
			window.open(url);
		};

		$scope.openBloqueio = function($event) {
			$event.preventDefault();
			$event.stopPropagation();

			$scope.close = function () {
				$modalInstance.close();
			};

			$scope.popup = {"idDoctoServico": $scope.data.idDoctoServico};

			var bloquearBloqueio = function(deveBloquear) {
				$scope.popup.blAgendamentoBloqueio = deveBloquear;

				$scope.rest.doPost("/storeBlAgendamento", $scope.popup).then(function (response) {

					$scope.findByIdDetalhamento();

				}, function () {
					$scope.tmp.informacaoDoctoCliente = null;
				});
			};

			var ctrl = ["$scope", "$modalInstance", function($scope, $modalInstance) {
				$scope.title = $scope.getMensagem("obrigarAgendamentoModel");
				$scope.message = $scope.getMensagem("obrigarAgendamentoQuestion");
				$scope.innerTemplate = contextPath+"js/common/modal/view/modal-default-content.html";
				$scope.ok = function () {
					bloquearBloqueio(true);
					$modalInstance.close();
					$rootScope.isPopup = false;
				};

				$scope.cancel = function () {
					bloquearBloqueio(false);
					$modalInstance.dismiss("cancel");
					$rootScope.isPopup = false;
				};
				$scope.close = function () {
					$modalInstance.close();
					$rootScope.isPopup = false;
				};


			}];

			modalService.open({controller: ctrl});
		};

		$scope.findByIdDetalhamento = function(){
			if ($scope.data.idDoctoServico) {
				$rootScope.showLoading = true;

				ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamento($stateParams.id).then(function(data) {
					if (data == null || data.length == 0) {
						$location.path("/app/consultarLocalizacaoMercadoria/");
						return;
					}
					$scope.data = data;

					$scope.abas = {};
					$rootScope.showLoading = true;

					ConsultarLocalizacaoMercadoriaFactory.findByIdDetalhamentoAbaPrincipal($stateParams.id).then(function(data) {

						var abas = {};

						$scope.abas.principal = data;

						/* Aba Principal */
						$scope.abas.abaRNC = true;
						$scope.abas.abaBloqueio = true;
						$scope.abas.abaCC = true;
						$scope.abas.abaIndenizacoes = false;
						$scope.abas.abaAgend = true;
						$scope.abas.abaEmb = true;
						$scope.abas.abaDados = true;
						$scope.abas.abaAWB = true;


						ConsultarLocalizacaoMercadoriaFactory.findRespostaAbasDetalhamento($stateParams.id).then(function(data) {

							$scope.abas.abaRNC = data.abaRNC;
							$scope.abas.abaBloqueio = data.abaBloqueio;
							$scope.abas.abaCC = data.abaCC;

							$scope.abas.abaIndenizacoes = false;
							if (data.blIndenizacoes == "S" && $scope.data.idDoctoServicoIndenizacao) {
								$scope.abas.abaIndenizacoes = true;
							}

							$scope.abas.abaAgend = data.abaAgend;
							$scope.abas.abaEmb = data.abaEmb;
							$scope.abas.abaDados = data.abaDados;
							$scope.abas.abaAWB = data.abaAWB;
						});
					})['finally'](function() {
						$rootScope.showLoading = false;

					});
				})['finally'](function() {
					$rootScope.showLoading = false;

				});
			} else {
				$scope.dados = null;
				$scope.abas = null;
			}
		}

		$scope.data.idDoctoServico = $stateParams.id;
		$scope.findByIdDetalhamento();

		/* Accordions que sempre estarao com estado inicial expandido */
		$scope.isDadosComplementaresDoctoServicoOpen = true;
		$scope.isDadosComplementaresNotasFiscaisOpen = true;
		$scope.isCalculoFreteOpen = true;
		$scope.isCalculoServicosOpen = true;
		$scope.isEDIOpen = true;
		$scope.isRemetenteOpen = true;
		$scope.isDadosAwbOpen = true;
		$scope.isDestinatarioOpen = true;
	}
];

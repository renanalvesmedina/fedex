package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy.NotaCreditoCalculoStrategy;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SpringBeanFactory;
import com.mercurio.lms.util.session.SessionUtils;

public class GerarNotaCreditoService {

	private static final String NR_NOTA_CREDITO = "NR_NOTA_CREDITO";
	private static final String BEAN_PREFIX = "lms.fretecarreteirocoletaentrega.";
	private static final String CALCULO_UM_BEAN_NAME = "notaCreditoCalculoUmStrategy";
	private static final String CALCULO_DOIS_BEAN_NAME = "notaCreditoCalculoDoisStrategy";

	private SpringBeanFactory springBeanFactory;
	private ConfiguracoesFacade configuracoesFacade;
	private ControleCargaService controleCargaService;
	private ManifestoEntregaService manifestoEntregaService;
	private DoctoServicoService doctoServicoService;
	private PedidoColetaService pedidoColetaService;
	private NotaCreditoService notaCreditoService;
	private NotaCreditoPadraoService notaCreditoPadraoService;
	private EventoNotaCreditoService eventoNotaCreditoService;
	private TabelaColetaEntregaService tabelaColetaEntregaService;
	private CalculoTabelaFreteCarreteiroCeService calculoTabelaFreteCarreteiroCeService;
	private EnderecoPessoaService enderecoPessoaService;
	private MoedaPaisService moedaPaisService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";

	/**
	 * Executa método para gerar nota de crédito usando controle de carga.
	 * O método vai considerar que o controle de carga está vinculado à sessão do hibernate
	 * @param controleCarga
	 */
	public void execute(ControleCarga controleCarga) {
		execute(controleCarga,true);
	}

	/**
	 * Executa método para gerar nota de crédito usando controle de carga.
	 * O método lida com requisições de um objeto que está ou não vinculado à sessão do hibernate.
	 * @param controleCarga
	 * @param blEstaVinculadoASessaoDoHibernate
	 */
	public void execute(ControleCarga controleCarga, Boolean blEstaVinculadoASessaoDoHibernate) {
		if (blEstaVinculadoASessaoDoHibernate) {
			NotaCredito notaCredito = executeGetNotaCredito(controleCarga);
			if (notaCredito != null) {
				controleCarga.setNotaCredito(notaCredito);
				controleCargaService.store(controleCarga);
			}
		} else{
			ControleCarga controleCargaAux = getControleCarga(controleCarga.getIdControleCarga());
			NotaCredito notaCredito = executeGetNotaCredito(controleCargaAux);
			if (notaCredito != null) {
				controleCarga.setNotaCredito(notaCredito);
				controleCargaAux.setNotaCredito(notaCredito);
				controleCargaService.store(controleCargaAux);
			}

		}
	}

	/**
	 * Executa método para gerar nota de crédito usando controle de carga.
	 * O método vai buscar um controle de carga baseado no id e não atualizará o controle de carga com a nova nota de crédito.
	 * @param idControleCarga
	 * @return
	 */
	public NotaCredito execute(Long idControleCarga) {
		ControleCarga controleCarga = getControleCarga(idControleCarga);
		return executeGetNotaCredito(controleCarga);

	}

	private NotaCredito executeGetNotaCredito(ControleCarga controleCarga) {
		boolean calculoPadrao = false;

		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}

		NotaCredito notaCredito = null;

		if (calculoPadrao) {/*/*/
			notaCredito = generateNotaCreditoPadrao(controleCarga);
			calculoTabelaFreteCarreteiroCeService.executar(controleCarga, notaCredito);
		} else {

			validateControleCarga(controleCarga);

			notaCredito = generateNotaCredito(controleCarga, false);

			// LMS-4153 - item 18
			if (notaCredito.getObNotaCredito() == null || !notaCredito.getObNotaCredito().contains("LMS-25073")) {
				notaCredito.setObNotaCredito(this.setObsToNotaCredito(controleCarga, notaCredito.getObNotaCredito()));
			}

			// LMS-4153 - item 19
			// A observação da nota não possui a mensagem LMS-25065
			if (notaCredito.getObNotaCredito() == null || !notaCredito.getObNotaCredito().contains("LMS-25065")) {
				this.concatenaObsToNotaCredito(controleCarga, notaCredito);
			}

			return notaCreditoService.storeNotaCredito(notaCredito);
		}

		return notaCredito;
	}

	private String setObsToNotaCredito(ControleCarga controleCarga, String obNotaCredito) {
		List<TabelaColetaEntrega> listTabelaCE = getTabelaColetaEntregaService().findTabelaColetaEntregaComParcelaPFOrPVSemEntregaByIdControleCarga(controleCarga);
		String obNotaCreditoRetorno = obNotaCredito;

		if (!listTabelaCE.isEmpty()) {
			String chave = "LMS-25073";
			String tpParcela = getTpParcelaPfOrPv(listTabelaCE.get(0).getParcelaTabelaCes());
			obNotaCreditoRetorno = chave.concat(": ").concat(configuracoesFacade.getMensagem(chave, new Object[]{tpParcela}));
		}
		return obNotaCreditoRetorno;
	}

	// LMS-4153 - item 19
	private void concatenaObsToNotaCredito(ControleCarga controleCarga, NotaCredito notaCredito) {
		// Verifica se a tabela de coleta entrega possui uma parcela com valor definido > 0 do tipo DH
		List<TabelaColetaEntrega> listTabelaCE = getTabelaColetaEntregaService().findTabelaColetaEntregaComParcelaDHByControleCarga(controleCarga);
		List<NotaCredito> listaNotasCredito = new ArrayList<NotaCredito>();

		if (!listTabelaCE.isEmpty()) {
			String chave = "LMS-25065";
			StringBuilder notasCreditoPagas = new StringBuilder();
			List<ControleCarga> controlesCargaNoPeriodo = new ArrayList<ControleCarga>();

			if (notaCredito.getControleCarga() != null) {
				// Verifica se existem controles de carga relacionados ao proprietário no periodo
				controlesCargaNoPeriodo = controleCargaService.findControlesCargaProprietarioNoPeriodo(notaCredito.getControleCarga());
			}

			// Para cada controle de carga retornado no passo anterior, verificar se existem outras notas de crédito que tenham parcelas diárias com valor
			// definido > 0 e quantidade maior que 0
			for (ControleCarga cc : controlesCargaNoPeriodo) {
				listaNotasCredito.addAll(notaCreditoService.findNotasCreditoComParcelaDiariaEvalorDefinido(cc, notaCredito));
			}


			if (!listaNotasCredito.isEmpty()) {
				efetuaConcatenamentoObNotaCredito(notaCredito, listaNotasCredito, chave, notasCreditoPagas);
			}
		}

	}

	private void efetuaConcatenamentoObNotaCredito(NotaCredito notaCredito, List<NotaCredito> listaNotasCredito, String chave, StringBuilder notasCreditoPagas) {
		for (NotaCredito notaCreditoAtual : listaNotasCredito) {
			if (notasCreditoPagas.length() > 0) {
				notasCreditoPagas.append(", ");
			}

			notasCreditoPagas.append(notaCreditoAtual.getFilial().getSgFilial());
			notasCreditoPagas.append("-");
			notasCreditoPagas.append(notaCreditoAtual.getNrNotaCredito().toString());
		}


		if (notaCredito.getObNotaCredito() == null) {
			notaCredito.setObNotaCredito(chave.concat(": ").concat(configuracoesFacade.getMensagem(chave, new Object[]{notasCreditoPagas})));
		} else {
			notaCredito.setObNotaCredito(notaCredito.getObNotaCredito().concat("\n").concat(chave).concat(": ").concat(configuracoesFacade.getMensagem(chave, new Object[]{notasCreditoPagas})));
		}
	}

	public BigDecimal executeTeste(Long idControleCarga) {
		NotaCredito nota = execute(idControleCarga);

		boolean calculoPadrao = false;

		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}

		if (!calculoPadrao) {
			return notaCreditoService.findValorTotalNotaCredito(nota.getIdNotaCredito());
		} else {
			return nota.getVlTotal();
		}
	}

	private void calculaNotaCredito(NotaCredito notaCredito, ControleCarga controleCarga) {
		NotaCreditoCalculoStrategy calculo = getNotaCreditoCalculoStrategy(notaCredito, controleCarga);
		calculo.generateNotaCredito();
	}

	private void validateControleCarga(ControleCarga controleCarga) {
		if (controleCarga == null) {
			throw new BusinessException("LMS-02044");
		}

		if (hasNotaCreditoEmitidaSemParceira(controleCarga) || !hasChegadaPortariaSemParceira(controleCarga) || !hasSaidaPortaria(controleCarga)) {
			throw new BusinessException("LMS-25044");
		}

		if (!hasTabelaColetaEntrega(controleCarga)) {
			throw new BusinessException("LMS-25045");
		}
	}

	private NotaCredito generateNotaCredito(ControleCarga controleCarga, boolean padrao) {
		NotaCredito notaCredito = new NotaCredito();

		if (!hasManifestoEntregaParceira(controleCarga)) {
			notaCredito = getNotaCredito(controleCarga, padrao, notaCredito);
		}

		notaCredito.setControleCarga(controleCarga);

		if (notaCredito.getIdNotaCredito() == null) {
			notaCredito.setNrNotaCredito(generateNumeroNotaCredito(controleCarga));
			notaCredito.setFilial(controleCarga.getFilialByIdFilialOrigem());
			notaCredito.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
			if (padrao) {
				EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
				MoedaPais moedaPais = moedaPaisService.findByPaisAndMoeda(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getIdPais(), controleCarga.getFilialByIdFilialOrigem().getMoeda().getIdMoeda());
				notaCredito.setMoedaPais(moedaPais);
			} else {
				notaCredito.setMoedaPais(getMoedaPais(controleCarga));
			}

			notaCreditoService.storeNotaCredito(notaCredito);

			eventoNotaCreditoService
					.storeEventoNotaCredito(
							notaCredito,
							ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO,
							ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_GERADO);
		} else {
			if (padrao) {
				notaCreditoPadraoService.removeListaNotaCredito(notaCredito);
			}
		}
		if (!padrao) {
			calculaNotaCredito(notaCredito, controleCarga);
		}

		return notaCredito;
	}

	private NotaCredito getNotaCredito(ControleCarga controleCarga, boolean padrao, NotaCredito notaCredito) {

		NotaCredito notaCreditoRetorno = notaCredito;

		if (padrao) {
			List<NotaCredito> notasCredito = notaCreditoService.findByIdControleCarga(controleCarga.getIdControleCarga());

			for (NotaCredito nc : notasCredito) {
				if (nc.getTpNotaCredito() != null && "E".equals(nc.getTpNotaCredito().getValue())) {
					notaCreditoRetorno = nc;
					break;
				}
			}
		} else {
			NotaCredito notaCreditoExistente = getNotaCredito(controleCarga);

			if ((notaCreditoExistente != null && !padrao) || notaCreditoExistente != null && notaCreditoExistente.getTpNotaCredito() != null && !"C".equals(notaCreditoExistente.getTpNotaCredito().getValue())) {
				notaCreditoRetorno = notaCreditoExistente;
			}
		}
		return notaCreditoRetorno;
	}

	private NotaCredito generateNotaCreditoPadrao(ControleCarga controleCarga) {
		return generateNotaCredito(controleCarga, true);
	}

	private String getTpParcelaPfOrPv(List<ParcelaTabelaCe> list) {
		String tpParcela = null;
		for (ParcelaTabelaCe parcelaTabelaCe : list) {
			if ("PF".equals(parcelaTabelaCe.getTpParcela().getValue()) ||
					"PV".equals(parcelaTabelaCe.getTpParcela().getValue())) {
				tpParcela = parcelaTabelaCe.getTpParcela().getDescriptionAsString();
				break;
			}

		}
		return tpParcela;
	}

	private Long generateNumeroNotaCredito(ControleCarga controleCarga) {
		return configuracoesFacade.incrementaParametroSequencial(controleCarga.getFilialByIdFilialOrigem()
				.getIdFilial(), NR_NOTA_CREDITO, true);
	}

	private ControleCarga getControleCarga(Long idControleCarga) {
		return controleCargaService.findById(idControleCarga);
	}

	private NotaCreditoCalculoStrategy getNotaCreditoCalculoStrategy(NotaCredito notaCredito, ControleCarga controleCarga) {
		NotaCreditoCalculoStrategy strategy;

		if (controleCargaService.hasTabelaColetaEntregaCC(controleCarga)) {
			strategy = getStrategyBean(CALCULO_DOIS_BEAN_NAME);
		} else {
			strategy = getStrategyBean(CALCULO_UM_BEAN_NAME);
		}

		strategy.setup(notaCredito);

		return strategy;
	}

	private NotaCreditoCalculoStrategy getStrategyBean(String strategyName) {
		return (NotaCreditoCalculoStrategy) springBeanFactory.getBean(BEAN_PREFIX + strategyName);
	}

	private MoedaPais getMoedaPais(ControleCarga controleCarga) {
		return (controleCarga.getTabelaColetaEntrega() != null) ? controleCarga.getTabelaColetaEntrega().getMoedaPais()
				: controleCarga.getTabelasColetaEntregaCC().get(0).getTabelaColetaEntrega().getMoedaPais();
	}

	private NotaCredito getNotaCredito(ControleCarga controleCarga) {
		List<NotaCredito> notasCredito = notaCreditoService.findByIdControleCarga(controleCarga.getIdControleCarga());

		if (notasCredito != null && !notasCredito.isEmpty()) {
			return notasCredito.get(0);
		}

		return null;
	}

	private boolean hasTabelaColetaEntrega(ControleCarga controleCarga) {
		return controleCarga.getTabelaColetaEntrega() != null
				|| controleCargaService.hasTabelaColetaEntregaCC(controleCarga);
	}

	private boolean hasChegadaPortariaSemParceira(ControleCarga controleCarga) {
		if (!hasManifestoEntregaParceira(controleCarga)) {
			return controleCarga.getDhChegadaColetaEntrega() != null;
		}

		return true;
	}

	public Map<String, Object> generateNotaCreditoParceira(Map<String, Object> criteria) {

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("hasNotaGerada", false);
		retorno.put("skipValidations", false);

		NotaCredito notaGerada = null;

		BigDecimal id = (BigDecimal) criteria.get("idControleCarga");
		Long idControleCarga = id.setScale(0, BigDecimal.ROUND_UP).longValueExact();

		ControleCarga cc = controleCargaService.findById(idControleCarga);

//		LMS-5023
		boolean hasGerarNotaCredito = controleCargaService.validateGeracaoNotaCreditoParceiraByIdControleCargar(cc.getIdControleCarga());

		if (hasGerarNotaCredito) {
			notaGerada = execute(cc.getIdControleCarga());
			if (notaGerada != null) {
				retorno.put("hasNotaGerada", true);
				retorno.put("skipValidations", true);
				retorno.put("controleCargas.meioTransporte.idMeioTransporte",
						cc.getMeioTransporteByIdTransportado()
								.getIdMeioTransporte());
				retorno.put("controleCargas.proprietario.idProprietario", cc
						.getProprietario().getIdProprietario());
				retorno.put("filial.idFilial", SessionUtils.getFilialSessao()
						.getIdFilial());
			}
		} else {
			throw new BusinessException("LMS-25069");
		}


		boolean hasDoctoServicoSemOcorrenciaEntrega = doctoServicoService.validateDoctoServicoSemOcorrenciaEntrega(cc.getIdControleCarga());
		boolean hasColetasExecutadasOuNao = pedidoColetaService.validateColetaExecutadaOuNao(cc.getIdControleCarga());

		if (hasDoctoServicoSemOcorrenciaEntrega && hasColetasExecutadasOuNao) {
			cc.setNotaCredito(getMaiorNotaCredito(cc.getNotasCredito()));
			controleCargaService.store(cc);
		}
		return retorno;
	}

	private NotaCredito getMaiorNotaCredito(List<NotaCredito> notas) {
		Collections.sort(notas, new Comparator<NotaCredito>() {

			public int compare(NotaCredito o1, NotaCredito o2) {
				return o1.getIdNotaCredito().compareTo(o2.getIdNotaCredito());
			}
		});
		return notas.get(notas.size() - 1);
	}

	private boolean hasSaidaPortaria(ControleCarga controleCarga) {
		return controleCarga.getDhSaidaColetaEntrega() != null;
	}

	private boolean hasNotaCreditoEmitidaSemParceira(ControleCarga controleCarga) {
		return !hasManifestoEntregaParceira(controleCarga)
				&& notaCreditoService.hasNotaCreditoEmitidaControleCarga(controleCarga.getIdControleCarga());
	}

	private boolean hasManifestoEntregaParceira(ControleCarga controleCarga) {
		return manifestoEntregaService.isManifestoEntregaParceira(controleCarga);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public void setSpringBeanFactory(SpringBeanFactory springBeanFactory) {
		this.springBeanFactory = springBeanFactory;
	}

	public EventoNotaCreditoService getEventoNotaCreditoService() {
		return eventoNotaCreditoService;
	}

	public void setEventoNotaCreditoService(
			EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}

	/**
	 * @return the tabelaColetaEntregaService
	 */
	public TabelaColetaEntregaService getTabelaColetaEntregaService() {
		return tabelaColetaEntregaService;
	}

	/**
	 * @param tabelaColetaEntregaService the tabelaColetaEntregaService to set
	 */
	public void setTabelaColetaEntregaService(
			TabelaColetaEntregaService tabelaColetaEntregaService) {
		this.tabelaColetaEntregaService = tabelaColetaEntregaService;
	}

	public CalculoTabelaFreteCarreteiroCeService getCalculoTabelaFreteCarreteiroCeService() {
		return calculoTabelaFreteCarreteiroCeService;
	}

	public void setCalculoTabelaFreteCarreteiroCeService(
			CalculoTabelaFreteCarreteiroCeService calculoTabelaFreteCarreteiroCeService) {
		this.calculoTabelaFreteCarreteiroCeService = calculoTabelaFreteCarreteiroCeService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public MoedaPaisService getMoedaPaisService() {
		return moedaPaisService;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setNotaCreditoPadraoService(
			NotaCreditoPadraoService notaCreditoPadraoService) {
		this.notaCreditoPadraoService = notaCreditoPadraoService;
	}

}

package com.mercurio.lms.contasreceber.model.service;

import br.com.tntbrasil.integracao.domains.dell.FaturaDellDMN;
import br.com.tntbrasil.integracao.domains.dell.layout.d2l.*;
import br.com.tntbrasil.integracao.domains.financeiro.DadosEnvioFaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.DoctoFaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.MonitoramentoMensagemDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.*;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemFaturaDAO;
import com.mercurio.lms.contasreceber.model.dao.RecebimentoPosLiqFaturaDAO;
import com.mercurio.lms.contasreceber.model.param.FaturaLookupParam;
import com.mercurio.lms.contasreceber.model.param.GenerateFaturaParam;
import com.mercurio.lms.contasreceber.model.param.RelacaoFaturaDepositoParam;
import com.mercurio.lms.contasreceber.util.SituacaoFaturaLookup;
import com.mercurio.lms.contasreceber.util.SqlInUtils;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.contasreceber.faturaService"
 */
public class FaturaService extends CrudService<Fatura, Long> {

	private static final Logger LOGGER = LogManager.getLogger(FaturaService.class);
	
	//Constantes necess�rias � gera��o da fatura para impress�o. 
	/** Vinte e quatro itens no primeiro quadrante */
	private static final  int QTD_REGISTROS_PRIMEIRO_QUADRANTE = 24;
	
	/** Quarenta e cinco itens para o primeiro e segundo quadrantes */
	private static final  int QTD_REGISTROS_TERCEIRO_QUADRANTE = 45;
	
	/** Noventa itens para a p�gina (todos os quadrantes) */
	private static final  int QTD_REGISTROS_QUARTO_QUADRANTE = 90;
	
	private static String FATURAMENTO = "FA";
	
	private FilialService filialService;
	private WorkflowPendenciaService workflowPendenciaService;
	private DescontoService descontoService;
	private DevedorDocServFatService devedorDocServFatService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaService moedaService;
	private GerarFaturaFaturaService gerarFaturaFaturaService;
	private BoletoService boletoService;
	private CalcularValorAtualFaturaService calcularValorAtualFaturaService;
	private GerarEncerramentoCobrancaService gerarEncerramentoCobrancaService;
	private ValidateItemFaturaService validateItemFaturaService;
	private ParametroGeralService parametroGeralService;
	private QuestionamentoFaturasService questionamentoFaturasService;
	private FaturaAnexoService faturaAnexoService;
	private BloqueioFaturamentoService bloqueioFaturamentoService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	private MonitoramentoMensagemService monitoramentoMensagemService;
	private MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService;
	private ContatoService contatoService;
	private RegionalFilialService regionalFilialService;
	private RegionalService regionalService;
	private MonitoramentoMensagemFaturaService monitoramentoMensagemFaturaService;
	private MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDao;
	private RecebimentoPosLiqFaturaDAO recebimentoPosLiqFaturaDAO;
	private CalcularJurosDiarioService calcularJurosDiarioService;
	private IntegracaoJmsService integracaoJmsService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private HistoricoFilialService historicoFilialService; 
	private FaturaCloudService faturaCloudService;

	@Override
	public void flush() {
		getFaturaDAO().flush();
	}

	@Override
	public Fatura findById(java.lang.Long id) {
		return (Fatura) super.findById(id);
	}

	/**
	 * Retorna a fatura com o boleto ativo 'fetchado' se existe.
	 * 
	 * @author Micka�l Jalbert
	 * @since 26/06/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @return Fatura
	 * */
	public Fatura findByIdWithBoleto(Long idFatura) {
		Fatura fatura = getFaturaDAO().findByIdWithBoleto(idFatura);

		List<Boleto> lstBoletos = null;
		Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
		if (boleto != null) {
			lstBoletos = new ArrayList<Boleto>(1);
			lstBoletos.add(boleto);
		}
		fatura.setBoletos(lstBoletos);
		return fatura;
	}

	public boolean validaDtEmissao(YearMonthDay dtEmissao) {
		String paramCompetencia = parametroGeralService
				.findSimpleConteudoByNomeParametro("COMPETENCIA_FINANCEIRO");
		if (paramCompetencia != null && paramCompetencia.length() > 0) {
			YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
			YearMonthDay competencia = JTDateTimeUtils
					.convertDataStringToYearMonthDay(paramCompetencia,
							"yyyy-MM-dd");
			String fechamento = String
					.valueOf(parametroGeralService
							.findSimpleConteudoByNomeParametro("IND_FECHTO_MENSAL_FINANCEIRO"));
			if (((dtAtual.getMonthOfYear() != competencia.getMonthOfYear()) || dtAtual
					.getYear() != competencia.getYear())
					&& "N".equals(fechamento)) {
				if (JTDateTimeUtils
						.comparaData(dtEmissao, dtAtual.minusDays(3)) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao, dtAtual) > 0)
					return false;
			} else {
				YearMonthDay primeiroDiaAtual = JTDateTimeUtils
						.getFirstDayOfYearMonthDay(dtAtual);
				if (JTDateTimeUtils.comparaData(dtEmissao, primeiroDiaAtual) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao,
								dtAtual.minusDays(3)) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao, dtAtual) > 0)
					return false;
			}
		}
		return true;
	}

	public boolean validaDtEmissaoComDataFatura(YearMonthDay dtEmissao,
			YearMonthDay dtEmissaoFatura) {
		String paramCompetencia = parametroGeralService
				.findSimpleConteudoByNomeParametro("COMPETENCIA_FINANCEIRO");
		if (paramCompetencia != null && paramCompetencia.length() > 0) {
			YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
			YearMonthDay competencia = JTDateTimeUtils
					.convertDataStringToYearMonthDay(paramCompetencia,
							"yyyy-MM-dd");
			String fechamento = String
					.valueOf(parametroGeralService
							.findSimpleConteudoByNomeParametro("IND_FECHTO_MENSAL_FINANCEIRO"));
			if (((dtAtual.getMonthOfYear() != competencia.getMonthOfYear()) || dtAtual
					.getYear() != competencia.getYear())
					&& "N".equals(fechamento)) {
				if (JTDateTimeUtils
						.comparaData(dtEmissao, dtAtual.minusDays(3)) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao, dtAtual) > 0
						|| JTDateTimeUtils.comparaData(dtEmissao,
								dtEmissaoFatura) < 0)
					return false;
			} else {
				YearMonthDay primeiroDiaAtual = JTDateTimeUtils
						.getFirstDayOfYearMonthDay(dtAtual);
				if (JTDateTimeUtils.comparaData(dtEmissao, primeiroDiaAtual) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao,
								dtAtual.minusDays(3)) < 0
						|| JTDateTimeUtils.comparaData(dtEmissao, dtAtual) > 0
						|| JTDateTimeUtils.comparaData(dtEmissao,
								dtEmissaoFatura) < 0)
					return false;
			}
		}
		return true;
	}


	/**
	 * Retorna a fatura do id informado, disconectado.
	 * 
	 * @param Long
	 *            idFatura
	 * @return Fatura
	 */
	public Fatura findByIdDisconnected(Long idFatura) {
		return getFaturaDAO().findByIdDisconnected(idFatura);
	}

	public List findFaturas(TypedFlatMap tfm) {
		return getFaturaDAO().findFaturas(tfm);
	}
	
	public List findFaturasVencidasEAVencer(TypedFlatMap tfm) {
		return getFaturaDAO().findFaturasVencidasEAVencer(tfm);
	}
	public List findFaturasVencidasEAVencerFull(TypedFlatMap tfm) {
		return getFaturaDAO().findFaturasVencidasEAVencerFull(tfm);
	}

	public List<Long> findIdsFaturasByCriteria(TypedFlatMap tfm) {
		return getFaturaDAO().findIdsFaturasByCriteria(tfm);
	}

	/**
	 * Valida se a situ��o do boleto � DI, EM, BN ou BP, caso n�o seja, lan�a a
	 * exception.
	 * 
	 * Hector Julian Esnaola Junior 07/01/2008
	 * 
	 * @param idFatura
	 * 
	 *            void
	 * @param tpFinalidade 
	 * @param string 
	 */
	public void validateTpSituacaoBoletoFromFatura(Long idFatura, String tpFinalidade) {
		Fatura fatura = this.findById(idFatura);
		Boolean situacaoOk = Boolean.TRUE;

		if (fatura.getBoleto() != null) {
			situacaoOk = boletoService.validateTpSituacaoBoleto(fatura
					.getBoleto().getTpSituacaoBoleto().getValue(), tpFinalidade);
		}

		if (!situacaoOk) {
			throw new BusinessException("LMS-36156");
		}
	}

	public void validateTpSituacaoFatura(Long idFatura, String tpFinalidade) {
		Fatura fatura = this.findById(idFatura);
		Boolean situacaoOk = validateTpSituacaoFatura(fatura
				.getTpSituacaoFatura().getValue(), tpFinalidade);
		if (!situacaoOk) {
			throw new BusinessException("LMS-36156");
		}
	}

	public Boolean validateTpSituacaoFatura(String tpSituacaoFatura, String tpFinalidade) {
		Boolean situacaoOk = Boolean.FALSE;
		if ("EM".equals(tpSituacaoFatura) || "BL".equals(tpSituacaoFatura)
				|| "DI".equals(tpSituacaoFatura)
				|| "RC".equals(tpSituacaoFatura)) {
			situacaoOk = Boolean.TRUE;
		}
		if (("LI".equals(tpSituacaoFatura)) && 
			("PR".equals(tpFinalidade)
				|| "DR".equals(tpFinalidade)
				|| "JU".equals(tpFinalidade)
				|| "OR".equals(tpFinalidade))) {
			situacaoOk = Boolean.TRUE;
		}
		return situacaoOk;
	}

	/**
	 * Retorna a fatura com o n�mero de fatura e a filial informado.
	 * 
	 * @author Micka�l Jalbert
	 * @since 31/01/2006
	 * 
	 * @param nrFatura
	 * @param idFilial
	 * 
	 * @return List
	 */
	public List<Fatura> findByNrFaturaByFilial(Long nrFatura, Long idFilial) {
		return getFaturaDAO().findByNrFaturaByFilial(nrFatura, idFilial);
	}

	public List findByNrFaturaIdFilialOrigem(Long nrFatura, Long idFilial) {
		return getFaturaDAO().findByNrFaturaIdFilialOrigem(nrFatura, idFilial);
	}

	public List findByNrFaturaIdFilialOrigemTpSituacaoFatura(Long nrFatura,
			Long idFilial, String tpSituacaoFatura) {
		return getFaturaDAO().findByNrFaturaIdFilialOrigemTpSituacaoFatura(
				nrFatura, idFilial, tpSituacaoFatura);
	}

	/**
	 * Retorna uma lista de fatura onde tem o nrPreFatura informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 12/05/2006
	 * 
	 * @param String
	 *            nrPreFatura
	 * @param List
	 * */
	public List<Fatura> findByNrPreFatura(Long nrPreFatura) {
		return getFaturaDAO().findByNrPreFatura(nrPreFatura);
	}

	public List<Fatura> findDocumentosServico(Long idFatura) {
		return getFaturaDAO().findDocumentosServico(idFatura);
	}

	public void validateFaturaCancelada(List<Map<String, Object>> l) {
		if (l != null && !l.isEmpty()) {
			Map<String, Object> fatura = l.get(0);
			if (fatura.get("tpSituacaoFatura") != null) {
				if ("CA".equals(((Map<String, Object>) fatura.get("tpSituacaoFatura")).get("value"))) {
					throw new BusinessException("LMS-04117");
				}
			}
		}
	}

	@Override
	protected Fatura beforeInsert(Fatura fatura) {
		// Buscar a filial da sessao
		Filial filialSessao = filialService.findFilialUsuarioLogado();

		// Buscar a filial centralizadora da filial da sessao
		Filial filialFaturamento = filialService
				.findFilialCentralizadoraByFilial(filialSessao.getIdFilial(),
						fatura.getTpModal().getValue(), fatura
								.getTpAbrangencia().getValue());

		// Se a filial da sessao tem uma filial centralizadora atribuir ela na
		// filial de faturamento da fatura
		if (filialFaturamento != null) {
			fatura.setFilialByIdFilial(filialFaturamento);
		} else { // Sen�o atribuir a tela da sess�o na filial de faturamento
			// da fatura
			fatura.setFilialByIdFilial(filialSessao);
		}

		fatura.setBlFaturaReemitida(Boolean.FALSE);
		fatura.setBlIndicadorImpressao(Boolean.FALSE);

		fatura.setVlIva(BigDecimal.ZERO);
		fatura.setVlTotal(BigDecimal.ZERO);
		fatura.setVlDesconto(BigDecimal.ZERO);
		fatura.setVlTotalRecebido(BigDecimal.ZERO);
		fatura.setVlJuroCalculado(BigDecimal.ZERO);
		fatura.setVlJuroRecebido(BigDecimal.ZERO);

		generateProximoNumero(fatura);

		return fatura;
	}

	@Override
	protected Fatura beforeStore(Fatura fatura) {

		// O intervalo de dias entre a da data de emiss�o e vencimento n�o pode
		// ultrapassar
		// o parametro geral NR_DIAS_LIMITE_VENCIMENTO (a menos que ele venha da
		// integracao)
		if (!"I".equalsIgnoreCase(fatura.getTpOrigem().getValue())) {
			validateLimitDate(fatura.getDtEmissao(), fatura.getDtVencimento());
		}

		// Valor iva n�o pode ser null
		if (fatura.getVlIva() == null) {
			fatura.setVlIva(BigDecimal.ZERO);
		}

		fatura.setVlBaseCalcPisCofinsCsll(BigDecimal.ZERO);
		fatura.setVlBaseCalcIr(BigDecimal.ZERO);
		fatura.setVlPis(BigDecimal.ZERO);
		fatura.setVlCofins(BigDecimal.ZERO);
		fatura.setVlCsll(BigDecimal.ZERO);
		fatura.setVlIr(BigDecimal.ZERO);

		if (fatura.getIdFatura() == null) {
			fatura = beforeInsert(fatura);
		}

		return fatura;
	}

	/**
	 * Verifica se a diferen�a de dias, entre a data de emiss�o e a data de
	 * vencimento � superior ao numero de dias cadastrado no parametro geral
	 * NR_DIAS_LIMITE_VENCIMENTO
	 * 
	 * @param emissao
	 * @param vencimento
	 */
	public void validateLimitDate(YearMonthDay emissao, YearMonthDay vencimento) {

		Integer interval = JTDateTimeUtils.getIntervalInDays(emissao,
				vencimento);

		Integer limite = IntegerUtils
				.getInteger(parametroGeralService.findConteudoByNomeParametro(
						"NR_DIAS_LIMITE_VENCIMENTO", false));

		if (CompareUtils.gt(interval, limite)) {
			throw new BusinessException("LMS-36250", new Object[] { limite });
		}
	}

	protected Fatura beforeStore(Fatura fatura, ItemList itensFatura) {
		fatura = beforeStore(fatura);

		// Se n�o tem item e a situa��o da fatura for diferente de inutilizado,
		// mandar exception
		if (!itensFatura.hasItems()
				&& !"IN".equals(fatura.getTpSituacaoFatura().getValue())) {
			throw new BusinessException("LMS-36038");
		}

		List<ItemFatura> list = itensFatura.getNewOrModifiedItems();
		for (ItemFatura itemFatura : list) {
			if (itemFatura.getDevedorDocServFat().getDescontos() != null
					&& itemFatura.getDevedorDocServFat().getDescontos().size() > 0) {
				// Se existe um valor desconto sem id desconto, quer dizer que
				// tem que criar um novo desconto
				if (((Desconto) itemFatura.getDevedorDocServFat()
						.getDescontos().get(0)).getIdDesconto() == null
						&& ((Desconto) itemFatura.getDevedorDocServFat()
								.getDescontos().get(0)).getVlDesconto() != null) {
					this.generateDesconto(itemFatura);
				}
			}

		}

		if (fatura.getMoeda() == null) {
			// Atribuir a moeda a fatura a partir da moeda do primeiro documento
			fatura.setMoeda(list.get(0).getDevedorDocServFat()
					.getDoctoServico().getMoeda());
		}

		// Atribuir o n�mero de documento para a fatura a partir do n�mero de
		// itens
		fatura.setQtDocumentos(Integer.valueOf(itensFatura.size()));

		// Se o tipo de origem n�o for 'Pre-Fatura' (P), usar regras de neg�cio
		// de 'Pre-fatura'
		if (!"P".equals(fatura.getTpOrigem().getValue())) {
			fatura = beforeStoreFatura(fatura, itensFatura);
		}

		return fatura;
	}

	private Fatura beforeStoreFatura(Fatura fatura, ItemList itensFatura) {

		// Validar o direito de accesso do usu�rio
		validateFatura(fatura.getIdFatura());

		/*
		 * for (Iterator iter = itensFatura.getNewOrModifiedItems().iterator();
		 * iter.hasNext();) { ItemFatura itemFatura = (ItemFatura)iter.next();
		 * // Validar se o usu�rio da sess�o pode usar aquele documento
		 * this.doctoServicoService
		 * .validatePermissaoDocumentoUsuario(itemFatura.
		 * getDevedorDocServFat().getDoctoServico().getIdDoctoServico(),
		 * fatura.getFilialByIdFilialCobradora().getIdFilial()); }
		 */

		return fatura;
	}

	public Fatura beforeInsertPreFatura(Fatura fatura, ItemList itensFatura,
			Cliente cliente) {

		// Buscar a filial de cobran�a do cliente e colocar na fatura
		fatura.setFilialByIdFilialCobradora(cliente
				.getFilialByIdFilialCobranca());
		fatura.setDtEmissao(JTDateTimeUtils.getDataAtual());
		fatura.setBlGerarBoleto(Boolean.FALSE);
		fatura.setBlGerarEdi(Boolean.FALSE);

		/*
		 * for (Iterator iter = itensFatura.getNewOrModifiedItems().iterator();
		 * iter.hasNext();){
		 * doctoServicoService.validatePermissaoDocumentoUsuario
		 * (((ItemFatura)iter
		 * .next()).getDevedorDocServFat().getDoctoServico().getIdDoctoServico
		 * (), fatura.getFilialByIdFilialCobradora().getIdFilial()); }
		 */

		return fatura;
	}

	public Fatura beforeInsertFatura(Fatura fatura, ItemList itensFatura) {
		fatura.setTpOrigem(new DomainValue("M"));

		return fatura;
	}

	// FIXME corrigir para retornar o ID
	public Fatura store(Fatura fatura, ItemList itensFatura, ItemList anexos,
			ItemListConfig configFatura) {
		boolean rollbackMasterId = fatura.getIdFatura() == null;

		if (!SessionUtils.isIntegrationRunning()) {
			if (fatura.getBlConhecimentoResumo() != null
					&& fatura.getBlConhecimentoResumo()) {
				throw new BusinessException("LMS-36256");
			}

			if (Boolean.TRUE.equals(fatura.getBlOcorrenciaCorp())
					&& !SessionUtils.isBatchJobRunning()) {
				throw new BusinessException("LMS-36266", new Object[] {
						fatura.getFilialByIdFilial().getSgFilial(),
						fatura.getNrFatura().toString() });
			}
		}

		try {
			List<ItemFatura> lstAll = new ArrayList<ItemFatura>();
			List<ItemFatura> lstNew = new ArrayList<ItemFatura>();
			List<ItemFatura> lstDeleted = new ArrayList<ItemFatura>();

			if (itensFatura.isInitialized()) {
				for (Iterator<ItemFatura> iter = itensFatura.iterator(
						fatura.getIdFatura(), configFatura); iter.hasNext();) {
					ItemFatura itemFatura = iter.next();
					lstAll.add(itemFatura);
				}
				lstDeleted = itensFatura.getRemovedItems();
				lstNew = itensFatura.getNewOrModifiedItems();
			} else if (fatura.getIdFatura() != null) {
				lstAll = findItemFatura(fatura.getIdFatura());
			}

			for (ItemFatura itemFatura : lstAll) {
				validateItemFaturaService.validateItemFatura(fatura,
						itemFatura, null);
			}

			Long idPendenciaAntigo = fatura.getIdPendenciaDesconto();
			if ("BL".equals(fatura.getTpSituacaoFatura().getValue())) {

				// campos relacionados ao desconto que devem ser salvos
				MotivoDesconto motivoDesconto = fatura.getMotivoDesconto();
				DomainValue setorCausadorDesconto = fatura
						.getTpSetorCausadorAbatimento();
				String obAcaoCorretiva = fatura.getObAcaoCorretiva();
				String obFatura = fatura.getObFatura();
				Filial filialDebitada = fatura.getFilialByIdFilialDebitada();

				fatura = findByIdTela(fatura.getIdFatura());

				fatura.setMotivoDesconto(motivoDesconto);
				fatura.setTpSetorCausadorAbatimento(setorCausadorDesconto);
				fatura.setObAcaoCorretiva(obAcaoCorretiva);
				fatura.setObFatura(obFatura);
				fatura.setFilialByIdFilialDebitada(filialDebitada);

				store(fatura);

				// LMS-2770
				if (validaNovoQuestionamento(fatura, lstAll)) {
					if (!existeAnexoFatura(fatura.getIdFatura(), anexos)) {
						throw new BusinessException("LMS-42045");
					}
				}

				fatura = gerarFaturaFaturaService
						.storeFaturaWithItemFaturaDesconto(fatura, lstAll);
			} else {

				// LMS-2770
				if (validaNovoQuestionamento(fatura, lstAll)) {
					if (!existeAnexoFatura(fatura.getIdFatura(), anexos)) {
						throw new BusinessException("LMS-42045");
					}
				}

				gerarFaturaFaturaService.storeFaturaWithItemFatura(fatura,
						lstAll, lstNew, lstDeleted);
			}

			// LMS-2770
			List<Serializable> idFaturaAnexosGerados = new ArrayList<Serializable>();
			if (anexos.isInitialized()) {
				// salva anexo da fatura
				for (Object faturaAnexoO : anexos.getNewOrModifiedItems()) {
					FaturaAnexo faturaAnexo = (FaturaAnexo) faturaAnexoO;
					idFaturaAnexosGerados.add(getFaturaAnexoService().store(
							faturaAnexo));
				}

				for (Object faturaAnexoO : anexos.getRemovedItems()) {
					FaturaAnexo faturaAnexo = (FaturaAnexo) faturaAnexoO;
					getFaturaAnexoService().removeById(
							faturaAnexo.getIdFaturaAnexo());
				}
			}

			// LMS-2770
			if ((idPendenciaAntigo == null && fatura.getIdPendenciaDesconto() != null)
					|| (idPendenciaAntigo != null && !idPendenciaAntigo
							.equals(fatura.getIdPendenciaDesconto()))) {
				List<FaturaAnexo> listFaturaAnexo = faturaAnexoService
						.findAllFaturaAnexoByIdFatura(fatura.getIdFatura());

				for (FaturaAnexo faturaAnexo : listFaturaAnexo) {
					if (!idFaturaAnexosGerados.contains(faturaAnexo
							.getIdFaturaAnexo())) {
						faturaAnexo.setDhEnvioQuestFat(null);
						getFaturaAnexoService().store(faturaAnexo);
					}
				}
			}

		} catch (RuntimeException e) {
			this.rollbackMasterState(fatura, rollbackMasterId, e);
			itensFatura.rollbackItemsState();
			anexos.rollbackItemsState();
			throw e;
		}
		return fatura;
	}

	private boolean existeAnexoFatura(Long idFatura, ItemList anexos) {
		if (anexos.isInitialized() && (anexos.getNewOrModifiedItems() != null && !anexos.getNewOrModifiedItems().isEmpty())) {
				for (Object faturaAnexoO : anexos.getNewOrModifiedItems()) {
					FaturaAnexo faturaAnexo = (FaturaAnexo) faturaAnexoO;
					if (faturaAnexo.getBlEnvAnexoQuestFat()) {
						return true;

					}
				}

			}

		List<FaturaAnexo> listaFaturaAnexo = new ArrayList<FaturaAnexo>();
		if (idFatura != null) {
			listaFaturaAnexo = faturaAnexoService
					.findAllFaturaAnexoByIdFatura(idFatura);
		}

		if (anexos.isInitialized()) {
			for (Object faturaAnexoO : anexos.getRemovedItems()) {
				FaturaAnexo faturaAnexo = (FaturaAnexo) faturaAnexoO;

				if (faturaAnexo.getIdFaturaAnexo() != null) {
					compareById: for (int i = 0; i < listaFaturaAnexo.size(); i++) {
						if (listaFaturaAnexo.get(i).getIdFaturaAnexo() != null
								&& listaFaturaAnexo.get(i).getIdFaturaAnexo()
										.equals(faturaAnexo.getIdFaturaAnexo())) {
							listaFaturaAnexo.remove(i);
							break compareById;
						}
					}
				}
			}
		}

		if (!listaFaturaAnexo.isEmpty()) {
			for (FaturaAnexo faturaAnexo : listaFaturaAnexo) {
				if (faturaAnexo.getBlEnvAnexoQuestFat()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean validaNovoQuestionamento(Fatura fatura,
			List<ItemFatura> lstItemFatura) {

		final BigDecimal valorMinimoDesconto = (BigDecimal) configuracoesFacade
				.getValorParametro("VL_MINIMO_DOCUMENTO_DESCONTO");

		BigDecimal valorDescontos = BigDecimal.ZERO;
		BigDecimal valorDescontosAprovados = BigDecimal.ZERO;

		for (ItemFatura itemFatura : lstItemFatura) {
			for (Object oDesconto : itemFatura.getDevedorDocServFat()
					.getDescontos()) {
				Desconto desconto = (Desconto) oDesconto;
				valorDescontos = valorDescontos.add(desconto.getVlDesconto());
				if (desconto.getIdDesconto() != null) {
					Desconto descontoAntigo = descontoService.findById(desconto
							.getIdDesconto());
					if (descontoAntigo != null
							&& descontoAntigo.getTpSituacaoAprovacao() != null
							&& "A".equals(descontoAntigo
									.getTpSituacaoAprovacao().getValue())) {
						valorDescontosAprovados = valorDescontosAprovados
								.add(descontoAntigo.getVlDesconto());
					}
				}
			}
		}

		BigDecimal vlDescontoAnterior = BigDecimal.ZERO;
		if (fatura.getVlDesconto() != null) {
			vlDescontoAnterior = fatura.getVlDesconto();
		}

		if (BigDecimal.ZERO.compareTo(valorDescontos) != 0
				|| (BigDecimal.ZERO.compareTo(vlDescontoAnterior) > -1)
				&& (valorDescontos.compareTo(valorMinimoDesconto) > 0)) {
			if (valorDescontos.compareTo(valorDescontosAprovados) > 0
					|| ((fatura.getTpSituacaoAprovacao() != null && "R"
							.equals(fatura.getTpSituacaoAprovacao().getValue())) && valorDescontos
							.compareTo(vlDescontoAnterior) != 0)) {
				return true;
			}
		}

		return false;
	}

	public boolean findIsQuestionamentoFatura(Fatura fatura) {
		fatura = findById(fatura.getIdFatura());
		return isQuestionamentoFatura(fatura, fatura.getItemFaturas());
	}

	public boolean isQuestionamentoFatura(Fatura fatura, List lstItemFatura) {
		return isQuestionamentoFatura(gerarFaturaFaturaService
				.getFirstDoctoServicoFromFatura(fatura, lstItemFatura)
				.getTpDocumentoServico());
	}

	public boolean isQuestionamentoFatura(DomainValue TpDocumentoServico) {
		// LMS-5958
		// Todos os documentos permitem questionamento
		return true;
	}

	public java.io.Serializable storeFaturaGerado(Fatura fatura) {
		this.beforeStore(fatura);
		this.getFaturaDAO().store(fatura);
		this.afterStore(fatura);
		return fatura;
	}

	protected void afterStore(Fatura fatura) {
		// Buscar a soma dos valores dos documento e setar na fatura
		TypedFlatMap somaValores = descontoService.findSomaByFatura(fatura
				.getIdFatura());
		if (somaValores != null) {
			if (somaValores.getInteger("qtDocumento") != null) {
				fatura.setQtDocumentos(somaValores.getInteger("qtDocumento"));
			}
			if (somaValores.getBigDecimal("vlDevido") != null) {
				fatura.setVlTotal(somaValores.getBigDecimal("vlDevido"));
			}
			if (somaValores.getBigDecimal("vlDesconto") != null) {
				fatura.setVlDesconto(somaValores.getBigDecimal("vlDesconto"));
			}
		}

		// Gerar uma pendencia de workflow caso que a cota��o n�o vem do banco
		// (input manual do usu�rio)
		fatura.setPendencia(generatePendenciaDeCotacao(fatura));

		// Gerar pendencia de pre-Fatura
		fatura.setPendencia(generatePendenciaPreFatura(fatura));

		this.store(fatura);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		// Buscar a fatura
		Fatura fatura = this.findById(id);

		// Se a situacao � diferente de 'Digitado' (DI) lan�ar uma exception
		if (!"DI".equals(fatura.getTpSituacaoFatura().getValue())) {
			throw new BusinessException("LMS-36009");
		}

		// Se a situa��o do workflow est� 'Em aprova��o' (E) lan�ar uma
		// exception
		if (fatura.getTpSituacaoAprovacao() != null
				&& "E".equals(fatura.getTpSituacaoAprovacao().getValue())) {
			throw new BusinessException("LMS-36203");
		}

		// Validar o direito de accesso do usu�rio
		validateFatura(id);

		cancelItemsFatura(fatura.getIdFatura());

		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		// Por cada id
		for (Long id : ids) {
			beforeRemoveById(id);
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Regras de neg�cio de inser��o de filho, compara
	 * 
	 * @param Fatura
	 *            fatura
	 * @param ItemList
	 *            items
	 * @param ItemListConfig
	 *            config
	 * @param ItemFatura
	 *            itemFaturaNew
	 */
	public void storeBeforeItemFatura(Fatura fatura, ItemList items,
			ItemListConfig config, ItemFatura itemFaturaNew) {

		// Item fatura anterior
		ItemFatura itemFaturaOldTmp = null;

		GenerateFaturaParam generateFaturaParam = null;

		validateItemFatura(fatura.getIdFatura(),
				itemFaturaNew.getIdItemFatura());

		for (Iterator<ItemFatura> iter = items.iterator(fatura.getIdFatura(),
				config); iter.hasNext();) {
			ItemFatura itemFaturaTmp = iter.next();

			// Verifica a unique key
			if (itemFaturaNew.getDevedorDocServFat().equals(
					itemFaturaTmp.getDevedorDocServFat())
					&& !itemFaturaTmp.getIdItemFatura().equals(
							itemFaturaNew.getIdItemFatura())) {
				throw new BusinessException("uniqueConstraintViolated");
			}

			if (itemFaturaOldTmp == null) {
				itemFaturaOldTmp = itemFaturaTmp;
			}

		}

		// Validar com o primeiro item fatura se existe
		if (itemFaturaOldTmp != null) {
			DevedorDocServFat devedorDocServFatOld = devedorDocServFatService
					.findById(itemFaturaOldTmp.getDevedorDocServFat()
							.getIdDevedorDocServFat());
			generateFaturaParam = gerarFaturaFaturaService
					.mountGenerateFaturaParam(devedorDocServFatOld);
		}

		validateItemFaturaService.validateItemFatura(fatura, itemFaturaNew,
				generateFaturaParam);
	}

	/**
	 * Cancela a fatura, os descontos pendentes e faz update em cima do devedor.
	 * 
	 * @author Micka�l Jalbert 15/03/2006
	 * 
	 * @param Fatura
	 *            fatura
	 */
	public void cancelFatura(Long idFatura) {
		Fatura fatura = findByIdWithBoleto(idFatura);

		if (Boolean.TRUE.equals(fatura.getBlOcorrenciaCorp())
				&& !SessionUtils.isIntegrationRunning()
				&& !SessionUtils.isBatchJobRunning()) {
			throw new BusinessException("LMS-36266", new Object[] {
					fatura.getFilialByIdFilial().getSgFilial(),
					fatura.getNrFatura().toString() });
		}

		DoctoServico ds = gerarFaturaFaturaService
				.getFirstDoctoServicoFromFatura(fatura, fatura.getItemFaturas());

		// Validar o direito de accesso do usu�rio
		validateFatura(idFatura);

		// S� pode cancelar uma fatura com boleto cancelado ou sem boleto
		// vinculado
		if ("BL".equals(fatura.getTpSituacaoFatura().getValue())) {
			throw new BusinessException("LMS-36151");
		}

		// Trocar a situa��o para 'Cancelada'
		fatura.setTpSituacaoFatura(new DomainValue("CA"));
		if (fatura.getTpSituacaoAprovacao() != null
				&& "E".equals(fatura.getTpSituacaoAprovacao().getValue())) {
			fatura.setTpSituacaoAprovacao(new DomainValue("C"));
		}

		// Salvar a fatura
		store(fatura);

		cancelItemsFatura(idFatura);

		// Se tem pendencia 'Em aprova��o', cancelar-lo
		if (fatura.getPendencia() != null
				&& "E".equals(fatura.getPendencia().getTpSituacaoPendencia().getValue())) {
			workflowPendenciaService.cancelPendencia(fatura.getPendencia()
					.getIdPendencia());
		}

		// Se tem pendencia 'Em aprova��o', cancelar-lo
		if (fatura.getIdPendenciaDesconto() != null) {
			if (ds != null
					&& ("NFS".equals(ds.getTpDocumentoServico().getValue())
							|| "NFT".equals(ds.getTpDocumentoServico()
									.getValue())
							|| "NTE".equals(ds.getTpDocumentoServico()
									.getValue()) || "NSE".equals(ds
							.getTpDocumentoServico().getValue()))) {
				Pendencia pendencia = workflowPendenciaService
						.getPendenciaService().findById(
								fatura.getIdPendenciaDesconto());
				if ("E".equals(pendencia.getTpSituacaoPendencia().getValue())) {
					workflowPendenciaService.cancelPendencia(pendencia
							.getIdPendencia());
				}
			} else {
				QuestionamentoFatura questionamentoFatura = questionamentoFaturasService
						.findById(fatura.getIdPendenciaDesconto());
				questionamentoFaturasService.storeCancelarQuestionamento(
						questionamentoFatura, "");
			}

		}

		gerarEncerramentoCobrancaService.executeEncerrarCobranca();

		calcularValorAtualFaturaService
				.executeAtualizarValorAtualFatura(fatura);
	}

	/**
	 * Troca a situa��o dos documentos de servico e descontos
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/04/2006
	 * 
	 * @param Long
	 *            idFatura
	 */
	public void cancelItemsFatura(Long idFatura) {
		// Busca os devedores a partir da fatura
		List<DevedorDocServFat> lstDevedor = devedorDocServFatService
				.findByFatura(idFatura);

		// Para cada devedor da fatura
		for (DevedorDocServFat devedorDocServFat : lstDevedor) {
			// Trocar a situa��o para 'Em carteira'
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("C"));
			devedorDocServFat.setFatura(null);

			// Salvar o devedor
			this.devedorDocServFatService.store(devedorDocServFat);
		}

		// Reprovar os descontos em aprova��o
		descontoService.removeDescontoByIdFatura(idFatura);
	}

	/**
	 * Gera uma pendencia (workflow) para liberar um valor especial de cota��o
	 * (quando a cota��o n�o � igual � cota��o que vem do banco)
	 * 
	 * @author Micka�l Jalbert 13/03/2006
	 * 
	 * @param Fatura
	 *            fatura
	 */
	private Pendencia generatePendenciaDeCotacao(Fatura fatura) {
		// Se o valor da cota��o na fatura � diferente do valor da cota��o do
		// banco
		if (fatura.getVlCotacaoMoeda() != null) {
			// Se � um update
			if (fatura.getIdFatura() != null) {
				Fatura faturaBD = this.findByIdDisconnected(fatura
						.getIdFatura());
				if (fatura.getVlCotacaoMoeda().compareTo(
						faturaBD.getVlCotacaoMoeda()) != 0) {
					// Cancela a pendencia ligada
					if (fatura.getPendencia() != null) {
						workflowPendenciaService.cancelPendencia(fatura
								.getPendencia().getIdPendencia());
					}
				}
			}
			// Gera uma nova pendencia
			return workflowPendenciaService.generatePendencia(fatura
					.getFilialByIdFilial().getIdFilial(),
					ConstantesWorkflow.NR3606_ALT_COT_DOL_FAT, fatura
							.getIdFatura(), null, JTDateTimeUtils
							.getDataHoraAtual());
		}

		return null;
	}

	public void storeItemFatura(List<ItemFatura> lstItemFatura) {
		bloqueioFaturamentoService.validateByItemFatura(lstItemFatura);
		getFaturaDAO().storeItemFatura(lstItemFatura);
	}

	public void removeItemFatura(List<ItemFatura> lstItemFatura) {
		beforeRemoveItemFatura(lstItemFatura);
		getFaturaDAO().removeItemFatura(lstItemFatura);
	}

	private void beforeRemoveItemFatura(List<ItemFatura> lstItemFatura) {
		for (ItemFatura itemFatura : lstItemFatura) {
			DevedorDocServFat devedorDocServFat = devedorDocServFatService
					.findByIdSimplificado(itemFatura.getDevedorDocServFat()
							.getIdDevedorDocServFat());
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("C"));
			devedorDocServFat.setFatura(null);
			devedorDocServFatService.store(devedorDocServFat);

			Desconto desconto = devedorDocServFat.getDesconto();

			// Caso o desconto n�o esteja associado a uma pendencia,
			// ou seja, o desconto foi gerado na fatura, remove o mesmo.
			if (desconto != null && desconto.getIdPendencia() == null) {
				descontoService.removeById(desconto.getIdDesconto());
			}
		}
	}

	/**
	 * Gera um desconto para o item fatura.
	 * 
	 * @author Micka�l Jalbert 15/03/2006
	 * 
	 * @param ItemFatura
	 *            itemFatura
	 */
	private void generateDesconto(ItemFatura itemFatura) {
		Desconto desconto = (Desconto) itemFatura.getDevedorDocServFat()
				.getDescontos().get(0);

		Long idmotivodesconto = desconto.getMotivoDesconto()
				.getIdMotivoDesconto();
		if (idmotivodesconto == null) {
			idmotivodesconto = itemFatura.getFatura().getMotivoDesconto()
					.getIdMotivoDesconto();
		}

		this.descontoService.generateDesconto(itemFatura.getDevedorDocServFat()
				.getIdDevedorDocServFat(), idmotivodesconto, desconto
				.getVlDesconto(), desconto.getObDesconto());
	}

	/**
	 * Gera uma pendencia (workflow) para aprovar a cria��o de uma fatura pelo
	 * cliente
	 * 
	 * @author Micka�l Jalbert 05/04/2006
	 * 
	 * @param Fatura
	 *            fatura
	 */
	private Pendencia generatePendenciaPreFatura(Fatura fatura) {
		if ("P".equals(fatura.getTpOrigem().getValue())
				&& fatura.getPendencia() == null) {
			// Gera uma nova pendencia
			return workflowPendenciaService.generatePendencia(
					fatura.getFilialByIdFilial().getIdFilial(),
					ConstantesWorkflow.NR3611_INCL_PRE_FAT,
					fatura.getIdFatura(),
					configuracoesFacade.getMensagem("inclusaoPreFatura")
							.replaceAll(
									"&preFatura",
									fatura.getFilialByIdFilial().getSgFilial()
											+ " " + fatura.getNrFatura()),
					JTDateTimeUtils.getDataHoraAtual());
		} else {
			return fatura.getPendencia();
		}
	}

	/**
	 * Valida se o usu�rio tem direito de modifica��o em cima da fatura.
	 * 
	 * @author Micka�l Jalbert
	 * @since 17/03/2006
	 * 
	 * @param Fatura
	 *            fatura
	 */
	public void validateFatura(Long idFatura) {

		if (idFatura != null) {
			Object[] dadosFatura = findSituacaoFaturaBoletoByFatura(idFatura);

			if (!SessionUtils.isIntegrationRunning()) {
				try {
					if (dadosFatura[3] != null
							&& ("S".equals(dadosFatura[3]))) {
						throw new BusinessException("LMS-36256");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// nao faz nada
				}
			}

			if (relacaoPagtoParcialService
					.validateRelacaoPagamentoParcial(idFatura)) {
				throw new BusinessException("LMS-36268");
			}

			// Se a fatura est� 'Em boleto' n�o pode modificar
			if (("BL".equals(dadosFatura[0]))) {
				throw new BusinessException("LMS-36149");
			}

			validateSituacaoFatura((String) dadosFatura[0],
					(String) dadosFatura[1], (Long) dadosFatura[2]);
		}
	}

	/**
	 * Valida se pode modificar o item da fatura baseado na situa��o da fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 20/11/2006
	 * 
	 * @param Fatura
	 *            fatura
	 * @param ItemFatura
	 *            itemFatura
	 */
	private void validateItemFatura(Long idFatura, Long idItemFatura) {
		if (idFatura != null) {
			Object[] dadosFatura = findSituacaoFaturaBoletoByFatura(idFatura);

			validateSituacaoFatura((String) dadosFatura[0],
					(String) dadosFatura[1], (Long) dadosFatura[2]);

			// Se for inclus�o de um novo item, valida os Pagamentos Parciais
			if (idItemFatura == null) {
				if (relacaoPagtoParcialService
						.validateRelacaoPagamentoParcial(idFatura)) {
					throw new BusinessException("LMS-36268");
				}
			}

			// Se a fatura est� 'Em boleto'
			if ("BL".equals((dadosFatura[0]))) {

				// Se � um novo documento
				if (idItemFatura == null) {
					throw new BusinessException("LMS-36207");
				}
			}
		}
	}

	/**
	 * Valida a situacao da fatura
	 */
	private void validateSituacaoFatura(String tpSituacaoFatura,
			String tpSituacaoBoleto, Long idFilial) {
		// Se a situa��o da fatura for diferente de 'Digitado', 'Emitido' e
		// 'Em boleto', o usu�rio n�o pode mudar a fatura
		if (!"DI".equals(tpSituacaoFatura) && !"EM".equals(tpSituacaoFatura)
				&& !"BL".equals(tpSituacaoFatura)) {
			throw new BusinessException("LMS-36062");
		}

		// Se a filial do usu�rio da sess�o for diferente da filial da fatura,
		// n�o pode modificar a fatura
		// e n�o for matriz
		if (!SessionUtils.isFilialSessaoMatriz()
				&& !idFilial.equals(SessionUtils.getFilialSessao()
						.getIdFilial())) {
			throw new BusinessException("LMS-36063");
		}

		// Se a fatura est� 'Em boleto' e que o boleto est� 'Em banco
		// protestado', n�o pode modificar
		if ("BL".equals(tpSituacaoFatura) && tpSituacaoBoleto != null
				&& "BP".equals(tpSituacaoBoleto)) {
			throw new BusinessException("LMS-36205");
		}
	}

	public List findItemFatura(Long masterId) {
		return this.getFaturaDAO().findItemFatura(masterId);
	}

	public Integer getRowCountItemFatura(Long masterId) {
		return this.getFaturaDAO().getRowCountItemFatura(masterId);
	}

	public Fatura findByIdTela(Long idFatura) {
		Fatura fatura = this.getFaturaDAO().findByIdTela(idFatura);
		// Busca o boleto ativo da fatura se existe.
		Boleto boleto = boletoService.findByFatura(idFatura);
		if (boleto != null) {
			List<Boleto> lstBoletos = new ArrayList<Boleto>(1);
			lstBoletos.add(boleto);
			fatura.setBoletos(lstBoletos);
			evict(boleto.getFatura());
		} else {
			fatura.setBoletos(null);
		}

		return fatura;
	}

	public List<Object[]> findFaturaByCriteria(TypedFlatMap criteria) {
		return this.getFaturaDAO().findFaturaByCriteria(criteria);
	}

	public List<Object[]> getRowCountFaturaByCriteria(TypedFlatMap criteria) {
		return this.getFaturaDAO().getRowCountFaturaByCriteria(criteria);
	}

	/**
	 * Retorna a fatura ativa por Devedor_Doc_Serv_Fat Busca a fatura ativa por
	 * Devedor_Doc_Serv_Fat, onde a situa��o da fatura seja diferente de 'CA' e
	 * 'IN'
	 * 
	 * @author Micka�l Jalbert
	 * @since 01/03/2006
	 * 
	 * @param Long
	 *            idDevedorDocServFat
	 * @return Fatura
	 */
	public Fatura findByDevedorDocServFat(Long idDevedorDocServFat) {
		List<Fatura> lstFatura = this.getFaturaDAO().findByDevedorDocServFat(
				idDevedorDocServFat);
		if (lstFatura.size() > 0) {
			return lstFatura.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna a lista de faturas ligado ao Recibo informado passando por a
	 * tabela faturaRecibo
	 * 
	 * @author Micka�l Jalbert
	 * @since 05/05/2006
	 * 
	 * @param Long
	 *            idRecibo
	 * @return List
	 */
	public List findByRecibo(Long idRecibo) {
		return getFaturaDAO().findByRecibo(idRecibo);
	}

	
	public List<Fatura> findByRecibos(List<Long> idRecibo) {
		return getFaturaDAO().findByRecibos(idRecibo);
	}

	/**
	 * Retorna a lista de id de faturas ligado ao Recibo informado passando por
	 * a tabela faturaRecibo
	 * 
	 * @author Micka�l Jalbert
	 * @since 23/08/2006
	 * 
	 * @param Long
	 *            idRecibo
	 * @return List
	 */
	public List findIdFaturaByRecibo(Long idRecibo) {
		return getFaturaDAO().findIdFaturaByRecibo(idRecibo);
	}

	/**
	 * Busca faturas para c�lculo de juros di�rio.<BR>
	 * 
	 * @see com.mercurio.lms.contasreceber.model.service.CalcularJurosDiarioService
	 * @param criterions
	 * @return
	 */
	public List<Fatura> findFaturaCalculoJuroDiario(Map criterions) {
		return getFaturaDAO().findFaturaCalculoJuroDiario(criterions);
	}

	public void generateProximoNumero(Fatura fatura) {
		Long idFilial = fatura.getFilialByIdFilial().getIdFilial();

		Long nrFatura = configuracoesFacade.incrementaParametroSequencial(
				idFilial, "NR_FATURA", true);
		fatura.setNrFatura(nrFatura);
	}

	/**
	 * Valida se a fatura ou os descontos s�o pendente, se tem, lan�a uma
	 * exception
	 * 
	 * @author Micka�l Jalbert
	 * @since 19/04/2006
	 * 
	 * @param Fatura
	 *            fatura
	 * @return Boolean
	 * 
	 * @exception BusinessException
	 *                ("LMS-36097")
	 * */
	public Boolean validateFaturaPendenteWorkflow(Fatura fatura) {
		// Se a situa��o de aprova��o da fatura for igual a 'Em aprova��o',
		// lan�ar uma exception
		if (fatura.getTpSituacaoAprovacao() != null
				&& "E".equals(fatura.getTpSituacaoAprovacao().getValue())) {
			throw new BusinessException("LMS-36097");
		}

		List<Desconto> lstDescontos = descontoService.findByFatura(fatura
				.getIdFatura());
		// Se tem descontos 'Em aprova��o', lan�ar uma exception
		for (Desconto desconto : lstDescontos) {
			descontoService.validateDescontoPendenteWorkflow(desconto);
		}

		return Boolean.TRUE;
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		Fatura fatura = findById(id);
		if (!SessionUtils.isIntegrationRunning()) {
			if (fatura.getBlConhecimentoResumo() != null
					&& fatura.getBlConhecimentoResumo()) {
				throw new BusinessException("LMS-36256");
			}
		}
		getDao().remove(fatura);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for (Long idFatura : ids) {
			removeById(idFatura);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(Fatura bean) {
		if (Boolean.TRUE.equals(bean.getBlOcorrenciaCorp())
				&& !SessionUtils.isIntegrationRunning()
				&& !SessionUtils.isBatchJobRunning()) {
			throw new BusinessException("LMS-36266", new Object[] {
					bean.getFilialByIdFilial().getSgFilial(),
					bean.getNrFatura().toString() });
		}
		return super.store(bean);
	}

	/**
	 * Salva a fatura usando o hibernate puro para optimizar o tempo de inser��o
	 * 
	 * @author Micka�l Jalbert
	 * @since 26/07/2006
	 * 
	 * @param Fatura
	 *            fatura
	 * 
	 * @return Fatura
	 */
	public Fatura storeBasic(Fatura fatura) {
		if (Boolean.TRUE.equals(fatura.getBlOcorrenciaCorp())
				&& !SessionUtils.isIntegrationRunning()
				&& !SessionUtils.isBatchJobRunning()) {
			throw new BusinessException("LMS-36266", new Object[] {
					fatura.getFilialByIdFilial().getSgFilial(),
					fatura.getNrFatura().toString() });
		}
		return getFaturaDAO().storeBasic(fatura);
	}

	/**
	 * Salva os itens fatura usando o hibernate puro para optimizar o tempo de
	 * inser��o
	 * 
	 * @author Micka�l Jalbert
	 * @since 26/07/2006
	 * 
	 * @param List
	 *            lstItemFatura
	 */
	public void storeItemFaturaBasic(List lstItemFatura) {
		getFaturaDAO().storeItemFaturaBasic(lstItemFatura);
	}

	/**
	 * M�todo respons�vel por carregar dados p�ginados de acordo com os filtros
	 * passados
	 * 
	 * @param criteria
	 * @return ResultSetPage contendo o resultado do hql.
	 */
	public List<Map<String, Object>> findPaginatedByFatura(TypedFlatMap criteria) {
		List<Map<String, Object>> faturas = getFaturaDAO()
				.findPaginatedByFatura(criteria);

		for (Map<String, Object> element : faturas) {
			// Juros at� a data atual
			BigDecimal jurosAteData = (BigDecimal) element
					.get("vlJuroCalculado");

			// Valor total da fatura
			BigDecimal valorTotal = (BigDecimal) element.get("vlTotal");

			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
			YearMonthDay dataVencimento = (YearMonthDay) element
					.get("dtVencimento");

			// Dias de atraso (dataAtual - dataVencimento)
			BigDecimal diasAtraso = new BigDecimal(
					JTDateTimeUtils
							.getIntervalInDays(dataVencimento, dataAtual));

			// Juros ao dia (jurosAteData / diasAtraso)
			BigDecimal jurosDia = jurosAteData.divide(diasAtraso, 2,
					BigDecimal.ROUND_DOWN);

			// % de juros ao m�s (jurosDia / valorTotal * 100 * 30)
			BigDecimal percJurosMes = jurosDia
					.divide(valorTotal, 2, BigDecimal.ROUND_DOWN)
					.multiply(new BigDecimal(100)).multiply(new BigDecimal(30));

			// Insere no Map os Atributo calculados acima
			element.put("jurosDia", jurosDia);
			element.put("diasAtraso", diasAtraso);
			element.put("percJurosMes", percJurosMes);
		}
		return faturas;
	}

	/**
	 * M�todo respons�vel por fazer a contagem dos registros que retornam do
	 * hql.
	 * 
	 * @param criteria
	 * @return Integer contendo o n�mero de registros retornados.
	 */
	public Integer getRowCountByFatura(TypedFlatMap criteria) throws Exception {
		return getFaturaDAO().getRowCountByFatura(criteria);
	}

	/**
	 * M�todo respons�vel por buscar dados referentes as faturas de uma
	 * cobranc�a inadimpl�ncia
	 * 
	 * @param criteria
	 * @return List
	 */
	public Map<String, Object> findDadosFaturasByCobrancaInadimplencia(
			TypedFlatMap criteria) {
		List<Map<String, Object>> dados = getFaturaDAO()
				.findDadosFaturasByCobrancaInadimplencia(criteria);
		Map<String, Object> element = new HashMap<String, Object>();
		if (!dados.isEmpty()) {
			element = dados.get(0);
			BigDecimal valorTotalFatura = (BigDecimal) element
					.get("somaFaturas");
			BigDecimal valorTotalJuros = (BigDecimal) element
					.get("somaJurosFaturas");
			BigDecimal valorTotalCobrado = valorTotalFatura
					.add(valorTotalJuros);
			element.put("totalCobrado", valorTotalCobrado);
		}
		return element;
	}

	/**
	 * Carrega uma fatura de acordo com idItemCobranca
	 * 
	 * @param idItemCobranca
	 * @return Map
	 */
	public Map<String, Object> findFaturaByIdItemCobranca(Long idItemCobranca) {
		return getFaturaDAO().findFaturaByIdItemCobranca(idItemCobranca);
	}

	/**
	 * M�todo respons�vel por buscar dados referentes as faturas de uma
	 * cobranc�a inadimpl�ncia
	 * 
	 * @param criteria
	 * @return Map
	 */
	public Map<String, Object> findDadosFaturasByCobrancaInadimplencia(
			TypedFlatMap criteria, ItemList items, ItemListConfig config) {
		Map<String, Object> element = new HashMap<String, Object>();

		BigDecimal valorTotalFatura = BigDecimal.ZERO;
		BigDecimal valorTotalFaturaPendente = BigDecimal.ZERO;
		BigDecimal valorTotalJuros = BigDecimal.ZERO;
		BigDecimal valorTotalCobrado = BigDecimal.ZERO;
		Long qtFatura = Long.valueOf(0);
		String moedaFatura = "";

		for (Iterator<ItemCobranca> iter = items.iterator(
				criteria.getLong("idCobrancaInadimplencia"), config); iter
				.hasNext();) {
			Fatura f = (iter.next()).getFatura();

			valorTotalFatura = valorTotalFatura.add(f.getVlTotal());

			// Calcular o valor dos somat�rios s� quando a fatura est� pendente
			// (diferente de 'Liquidado' e 'Cancelado')
			if (!"LI".equals(f.getTpSituacaoFatura().getValue())
					&& !"CA".equals(f.getTpSituacaoFatura().getValue())) {

				valorTotalJuros = valorTotalJuros.add(f.getVlJuroCalculado());
				valorTotalFaturaPendente = valorTotalFaturaPendente.add(f
						.getVlTotal());

				qtFatura++;
			}

			if (!iter.hasNext()) {
				Moeda moeda = moedaService.findById(f.getMoeda().getIdMoeda());
				moedaFatura = moeda.getSgMoeda() + " " + moeda.getDsSimbolo();
			}
		}

		valorTotalCobrado = valorTotalFaturaPendente.add(valorTotalJuros);

		element.put("moedaFatura", moedaFatura);
		element.put("somaFaturas", valorTotalFatura);
		element.put("somaFaturasPendentes", valorTotalFaturaPendente);
		element.put("somaJurosFaturas", valorTotalJuros);
		element.put("totalCobrado", valorTotalCobrado);
		element.put("nrFaturas", qtFatura);

		return element;
	}

	public void validateOcorrenciaFaturaCorporativo(Long idFatura) {
		Fatura fatura = findById(idFatura);
		if (fatura != null) {
			validateOcorrenciaFaturaCorporativo(fatura);
		}
	}

	public void validateOcorrenciaFaturaCorporativo(Fatura fatura) {
		if (Boolean.TRUE.equals(fatura.getBlOcorrenciaCorp())
				&& !SessionUtils.isBatchJobRunning()) {
			throw new BusinessException("LMS-36266", new Object[] {
					fatura.getFilialByIdFilial().getSgFilial(),
					fatura.getNrFatura().toString() });
		}
	}

	/**
	 * � chamado na aprova��o/reprova��o de um evento gerado na troca da cota��o
	 * da fatura
	 * 
	 * @author Micka�l Jalbert
	 * @since 15/08/2006
	 * 
	 * @param List
	 *            lstIds
	 * @param List
	 *            lstSituacao
	 */
	public String executeWorkflow(List<Long> lstIds, List<String> lstSituacao) {
		Long idFatura = lstIds.get(0);
		String tpSituacaoAprovacao = lstSituacao.get(0);

		Fatura fatura = findByIdFatura(idFatura);

		fatura.setTpSituacaoAprovacao(new DomainValue(tpSituacaoAprovacao));
		if (!"A".equals(tpSituacaoAprovacao)) {
			fatura.setVlCotacaoMoeda(null);
			calcularValorAtualFaturaService.executeAtualizarValorAtualFatura(fatura);

		}
		evict(fatura);
		return null;
		
	}

	/**
	 * Atualiza s
	 * 
	 * @param questionamentoFatura
	 */
	public void executeSynchronizeQuestionamentoFatura(
			QuestionamentoFatura questionamentoFatura) {
		Long idFatura = getFaturaDAO().findByIdPendenciaDesconto(
				questionamentoFatura.getIdQuestionamentoFatura());
		if (idFatura != null) {
			Fatura fatura = findById(idFatura);

			if (isQuestionamentoFatura(((ItemFatura) fatura.getItemFaturas()
					.get(0)).getDevedorDocServFat().getDoctoServico()
					.getTpDocumentoServico())) {
				if (!fatura
						.getTpSetorCausadorAbatimento()
						.getValue()
						.equals(questionamentoFatura
								.getTpSetorCausadorAbatimento().getValue())
						|| !fatura.getMotivoDesconto().equals(
								questionamentoFatura.getMotivoDesconto())) {
					fatura.setTpSetorCausadorAbatimento(questionamentoFatura
							.getTpSetorCausadorAbatimento());
					fatura.setMotivoDesconto(questionamentoFatura
							.getMotivoDesconto());
					super.store(fatura);
				}
			}
		}
	}

	/**
	 * Retorna o valor iva da fatura. Calculado a partir dos valores de frete
	 * externo dos ctos internacionais e dos porcentagens de aliquota vigentes
	 * dos paises de destino dos documentos da fatura informada.
	 * 
	 * @author Micka�l Jalbert
	 * @since 04/07/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @param YearMonthDay
	 *            dtVigencia
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal findVlIvaByFatura(Long idFatura,
			Long idFilialFaturamento, YearMonthDay dtVigencia) {
		return getFaturaDAO().findVlIvaByFatura(idFatura, idFilialFaturamento,
				dtVigencia);
	}

	/**
	 * FindPaginated especial para a tela de Rela��o de documentos com deposito
	 * 
	 * @author Micka�l Jalbert
	 * @since 29/03/2006
	 * 
	 * @param param
	 * @param findDef
	 * 
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedRelacaoDeposito(
			RelacaoFaturaDepositoParam param, FindDefinition findDef) {
		return getFaturaDAO().findPaginatedRelacaoDeposito(param, findDef);
	}

	/**
	 * GetRowCount especial para a tela de Rela��o de documentos com deposito
	 * 
	 * @author Micka�l Jalbert
	 * @since 29/03/2006
	 * 
	 * @param param
	 * 
	 * @return n�mero de linha
	 */
	public Integer getRowCountRelacaoDeposito(RelacaoFaturaDepositoParam param) {
		return getFaturaDAO().getRowCountRelacaoDeposito(param);
	}

	public void evict(Object object) {
		getFaturaDAO().getAdsmHibernateTemplate().evict(object);
	}

	/**
	 * Atualiza a situa��o das faturas para 'emitido', 'em recibo' ou 'em
	 * boleto' do redeco informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 06/07/2006
	 * 
	 * @param idRedeco
	 */
	public void cancelFaturasByIdRedeco(final Long idRedeco) {
		List faturas = getFaturaDAO().findFaturasByIdRedeco(idRedeco);
		if (faturas != null) {
			for (Fatura fatura : (ArrayList<Fatura>) faturas) {
				validateOcorrenciaFaturaCorporativo(fatura);
			}
		}

		getFaturaDAO().cancelFaturasByIdRedeco(idRedeco);
	}

    public void updateIndicadorImpressao(Long idFatura, Boolean indicadorImpressao){
    	getFaturaDAO().updateIndicadorImpressao(idFatura, indicadorImpressao);
    }
	
	/**
	 * Retorna a lista de ids de faturado redeco informado que tem boleto ativo
	 * 
	 * @author Micka�l Jalbert
	 * @since 07/07/2006
	 * 
	 * @param Long
	 *            idRedeco
	 * @return List
	 */
	public List<Long> findIdFaturaEmBoletoByIdRedeco(Long idRedeco) {
		return getFaturaDAO().findIdFaturaByIdRedeco(idRedeco);
	}

	/**
	 * M�todo respons�vel por trazer uma fatura com a filial carregada de acordo
	 * com o id da fatura
	 * 
	 * @param idFatura
	 * @return Fatura
	 */
	public Fatura findByIdFatura(Long idFatura) {
		return getFaturaDAO().findByIdFatura(idFatura);
	}
	
	public boolean findExistFaturasParaPgto(Long idFatura) {
		return getFaturaDAO().findExistFaturasParaPgto(idFatura);
	}
	public RecebimentoPosLiqFatura findRecebimentoPosLiqFatura(Long idFatura){
		return recebimentoPosLiqFaturaDAO.findByIdFatura(idFatura);
	}

	/**
	 * Retornar true se a fatura tem boleto ativo
	 * 
	 * @author Micka�l Jalbert
	 * @since 12/07/2006
	 * 
	 * @param Long
	 *            idFatura
	 * 
	 * @return Boolean
	 */
	public Boolean validateFaturaEmBoleto(Long idFatura) {
		return getFaturaDAO().validateFaturaEmBoleto(idFatura);
	}

	/**
	 * Retornar true se a fatura tem recibo ativo
	 * 
	 * @author Micka�l Jalbert
	 * @since 12/07/2006
	 * 
	 * @param Long
	 *            idFatura
	 * 
	 * @return Boolean
	 */
	public Boolean validateFaturaEmRecibo(Long idFatura) {
		return getFaturaDAO().validateFaturaEmRecibo(idFatura);
	}

	/**
	 * Retorna um map de dados por devedorDocServFat que serve no relat�rio
	 * 'EmitirDocumentosServicoPendenteCliente'.
	 * 
	 * @author Micka�l Jalbert
	 * @since 28/08/2006
	 * 
	 * @param idDevedorDocServFat
	 * @return Map
	 * */
	public Map<String, Object> findDadosEmitirDocumentosServicoPendenteCliente(
			Long idDevedorDocServFat) {
		return getFaturaDAO().findDadosEmitirDocumentosServicoPendenteCliente(
				idDevedorDocServFat);
	}

	/**
	 * M�todo respons�vel por buscar faturas de acordo com idDoctoServico
	 * 
	 * @author HectorJ
	 * @since 29/06/2006
	 * 
	 * @param Long
	 *            idDoctoServico
	 * @param List
	 *            tpSituacaoFatura
	 * 
	 * @return List <Fatura>
	 */
	public List<Fatura> findFaturasByDoctoServico(Long idDoctoServico,
			List<String> tpSituacaoFatura) {
		return getFaturaDAO().findFaturasByDoctoServico(idDoctoServico,
				tpSituacaoFatura);
	}

	/**
	 * Retorna o somat�rio dos valores totais dos documentos de servi�o
	 * pertencentes aos itens da fatura
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 22/09/2006
	 * 
	 * @param idFatura
	 *            Identificador da fatura
	 * @return Valor total dos documentos de servi�o dos itens da fatura
	 */
	public BigDecimal findValorTotalDocumentosServico(Long idFatura) {
		return getFaturaDAO().findValorTotalDocumentosServico(idFatura);
	}

	/**
	 * Retorna um array com a situa��o da fatura e a situa��o do boleto se tem
	 * 
	 * @author Micka�l Jalbert
	 * @since 20/11/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @return Object[]
	 */
	public Object[] findSituacaoFaturaBoletoByFatura(Long idFatura) {
		return getFaturaDAO().findSituacaoFaturaBoletoByFatura(idFatura);
	}

	/**
	 * Retorna a situa��o da aprova��o da fatura baseado nas duas pendencias da
	 * fatura.
	 * 
	 * @author Micka�l Jalbert
	 * @since 21/11/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @return String
	 */
	public String getSituacaoAprovacaoFatura(Long idFatura) {
		return getFaturaDAO().getSituacaoAprovacaoFatura(idFatura);
	}

	/**
	 * M�todo respons�vel por buscar faturas de acordo com os filtros
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 * 
	 * @param criteria
	 * @return
	 * 
	 */
	public ResultSetPage findPaginated(FaturaLookupParam flp,
			TypedFlatMap criteria) {

		/** Defini��es de pagina��o */
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

		return getFaturaDAO().findPaginated(flp, findDef);
	}

	/**
	 * M�todo respons�vel por busca a quantidade de faturas de acordo com os
	 * filtros
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 * 
	 * @param criteria
	 * @return
	 * 
	 */
	public Integer getRowCount(FaturaLookupParam flp) {
		return getFaturaDAO().getRowCount(flp);
	}

	/**
	 * Busca a(s) fatura(s) de acordo com os filtros
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 * 
	 * @return
	 * 
	 */
	public List findLookupFatura(FaturaLookupParam flp, String msgException) {
		/**
		 * Configura as situa��es da fatura que ser�o usadas no filtro de acordo
		 * com o par�metro da tela
		 */
		SituacaoFaturaLookup sfl = new SituacaoFaturaLookup(
				flp.getTpSituacaoFaturaValido());

		/**
		 * Zerar esta propriedade para ela n�o filtrar na hora de executar o hql
		 */
		flp.setTpSituacaoFaturaValido(null);

		List<Map<String, Object>> lstFatura = getFaturaDAO().findLookupFatura(
				flp);
		/** Se s� tem uma fatura */
		if (!lstFatura.isEmpty() && lstFatura.size() == 1) {
			Map<String, Object> mapFatura = lstFatura.get(0);

			/**
			 * Validar se a situa��o da fatura retornada faz parte da lista de
			 * situa��o validas, sen�o, lan�ar uma exception
			 */
			if (!sfl.validateTpSituacaoFatura(((DomainValue) mapFatura
					.get("tpSituacaoFatura")).getValue())) {
				throw new BusinessException(msgException);
			}
		}

		return lstFatura;
	}

	/**
	 * Retorna a lista de DevedorDocServFat da fatura informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 30/11/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @return List
	 */
	public List<DevedorDocServFat> findNrDoctoServicoByIdFatura(Long idFatura) {
		return this.getFaturaDAO().findNrDoctoServicoByIdFatura(idFatura);
	}

	/**
	 * Retorna a lista de n�mero de documento de servi�o da fatura informada e
	 * do tipo de documento diferente daquele informado
	 * 
	 * @author Micka�l Jalbert
	 * @since 30/11/2006
	 * 
	 * @param Long
	 *            idFatura
	 * @return Long
	 */
	public Long findQtDocumentoServicoByFatura(Long idFatura,
			String strTpDocumentoServico) {
		return getFaturaDAO().findQtDocumentoServicoByFatura(idFatura,
				strTpDocumentoServico);
	}

	/**
	 * Retorna a fatura com filial, filial cobradora, cliente, moeda.
	 * 
	 * @author Micka�l Jalbert
	 * @since 17/01/2007
	 * 
	 * @param idFatura
	 * @return fatura
	 */
	public Fatura findByIdTelaNotaDebitoNacional(Long idFatura) {
		return getFaturaDAO().findByIdTelaNotaDebitoNacional(idFatura);
	}

	/**
	 * Atualiza para nulo o manifesto de entrega e manifesto de entrega origem
	 * das faturas do manifesto informado.
	 * 
	 * @author Micka�l Jalbert
	 * @since 14/03/2007
	 * 
	 * @param idManifesto
	 */
	public void updateManifestoFatura(Long idManifesto) {
		getFaturaDAO().updateManifestoFatura(idManifesto);
	}

	/**
	 * Busca a fatura de acordo com o n�mero da fatura e o idFilialOrigem
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 23/04/2007
	 * 
	 * @param nrFatura
	 * @param idFilial
	 * @return
	 * 
	 */
	public Fatura findFaturaByNrFaturaAndIdFilial(Long nrFatura, Long idFilial) {
		return getFaturaDAO().findFaturaByNrFaturaAndIdFilial(nrFatura,
				idFilial);
	}

	/**
	 * 
	 * Carrega a fatura de acordo com o idFatura.
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 * 
	 * @param idFatura
	 * @return
	 * 
	 */
	public Fatura findFaturaByIdFatura(Long idFatura) {
		return getFaturaDAO().findFaturaByIdFatura(idFatura);
	}

	/**
	 * LMS-5727
	 * 
	 * Verifica se a filial debitada est� setada em caso de vlDesconto > 0 e se
	 * a filial debitada � a mesma da filial origem da fatura ou de algum de
	 * seus doctos servi�o
	 * 
	 * @author Caetano Matos
	 * 
	 * @param map
	 * @param fatura
	 * @param items
	 * @param config
	 */

	public void validateFilialDebitada(Fatura fatura, ItemList items,
			ItemListConfig config) {
		if (fatura.getFilialByIdFilialDebitada() == null) {
			throw new BusinessException("LMS-36294");
		} else {
			if (!fatura.getFilialByIdFilial().getIdFilial()
					.equals(fatura.getFilialByIdFilialDebitada().getIdFilial())
					&& !filialDebitadaExisteEmItemsFatura(fatura, items, config)) {
				throw new BusinessException("LMS-36295");
			}
		}
	}

	private boolean filialDebitadaExisteEmItemsFatura(Fatura fatura,
			ItemList itemsFatura, ItemListConfig configFatura) {
		List<ItemFatura> lstAll = new ArrayList<ItemFatura>();
		for (Iterator<ItemFatura> iter = itemsFatura.iterator(
				fatura.getIdFatura(), configFatura); iter.hasNext();) {
			ItemFatura itemFatura = iter.next();
			lstAll.add(itemFatura);
		}
		for (ItemFatura itemFatura : lstAll) {
			Filial filialOrigemDoctoServico = itemFatura.getDevedorDocServFat()
					.getDoctoServico().getFilialByIdFilialOrigem();
			if (filialOrigemDoctoServico.getIdFilial().equals(
					fatura.getFilialByIdFilialDebitada().getIdFilial())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Busca a fatura de acordo com o n�mero do documento de servi�o e a
	 * idFilialOrigem.
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 10/10/2007
	 * 
	 * @param nrDoctoServico
	 * @param idFilial
	 * @return
	 * 
	 */
	public Fatura findFaturaByNrDoctoServicoAndIdFilial(Long nrDoctoServico,
			Long idFilial) {
		if (nrDoctoServico == null || idFilial == null)
			return null;
		return getFaturaDAO().findFaturaByNrDoctoServicoAndIdFilial(
				nrDoctoServico, idFilial);
	}

	public List<Fatura> findByIdRedeco(Long idRedeco) {
		return getFaturaDAO().findByIdRedeco(idRedeco);
	}

	public List<Map<String, Object>> findInfoDoctosFatura(
			Map<String, Object> parameters) {
		return getFaturaDAO().findInfoDoctosFatura(parameters);
	}

	/**
	 * Verifica o cedente da fatura e atualiza conforme regra 1.3 da ET
	 * "30.03.01.05"
	 * 
	 * @param fatura
	 */
	public boolean updateCedente(Fatura fatura) {
		Cedente cedente = fatura.getCedente();
		Cliente cliente = fatura.getCliente();
		Filial filial = fatura.getFilialByIdFilial();

		boolean changed = false;

		// Caso a fatura n�o possua um cedente
		if (cedente == null
				&& (cliente != null && cliente.getCedente() != null)) {
			fatura.setCedente(cliente.getCedente());

			changed = true;
		} else if (cedente == null
				&& (filial != null && filial.getCedenteByIdCedente() != null)) {
			fatura.setCedente(filial.getCedenteByIdCedente());

			changed = true;
		}

		if (!changed) {
			// Caso a fatura possua um cedente anterior a execu��o atual e o
			// cliente possua um cedente setado
			if (cedente != null
					&& (cliente != null && cliente.getCedente() != null)) {
				if (!cedente.getIdCedente().equals(
						cliente.getCedente().getIdCedente())) {
					fatura.setCedente(cliente.getCedente());

					changed = true;
				}
				// Sen�o verifica se a fatura possui um cedente anterior a
				// execu��o atual e a filial possua um cedente setado
			} else if (cedente != null
					&& (filial != null && filial.getCedenteByIdCedente() != null)) {
				if (!cedente.getIdCedente().equals(
						filial.getCedenteByIdCedente().getIdCedente())) {
					fatura.setCedente(filial.getCedenteByIdCedente());

					changed = true;
				}
			}
		}

		if (fatura.getCedente() == null) {
			String nrFatura = fatura.getFilialByIdFilial() != null ? fatura
					.getFilialByIdFilial().getSgFilial() : "";
			nrFatura += " " + fatura.getNrFatura();

			throw new BusinessException("LMS-36298", new Object[] { nrFatura });
		}

		return changed;
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setFaturaDAO(FaturaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private FaturaDAO getFaturaDAO() {
		return (FaturaDAO) getDao();
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setGerarFaturaFaturaService(
			GerarFaturaFaturaService gerarFaturaFaturaService) {
		this.gerarFaturaFaturaService = gerarFaturaFaturaService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setCalcularValorAtualFaturaService(
			CalcularValorAtualFaturaService calcularValorAtualFaturaService) {
		this.calcularValorAtualFaturaService = calcularValorAtualFaturaService;
	}

	public void setGerarEncerramentoCobrancaService(
			GerarEncerramentoCobrancaService gerarEncerramentoCobrancaService) {
		this.gerarEncerramentoCobrancaService = gerarEncerramentoCobrancaService;
	}

	public void setValidateItemFaturaService(
			ValidateItemFaturaService validateItemFaturaService) {
		this.validateItemFaturaService = validateItemFaturaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(
			ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setQuestionamentoFaturasService(
			QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public QuestionamentoFaturasService getQuestionamentoFaturasService() {
		return questionamentoFaturasService;
	}

	public Long findByIdPendenciaDesconto(Long idPendenciaDesconto) {
		return getFaturaDAO().findByIdPendenciaDesconto(idPendenciaDesconto);
	}

	@SuppressWarnings("rawtypes")
	public List findFaturasWithoutBoleto() {
		Object param = configuracoesFacade
				.getValorParametroWithoutException("QT_DIAS_EMISSAO_BOLETO_AUT");

		int qtDiasEmissaoBoletoAutomatico = 0;
		if (param != null && param instanceof BigDecimal) {
			qtDiasEmissaoBoletoAutomatico = ((BigDecimal) param).intValue();
		}
		return getFaturaDAO().findFaturasWithoutBoleto(
				qtDiasEmissaoBoletoAutomatico);
	}

	public void setFaturaAnexoService(FaturaAnexoService faturaAnexoService) {
		this.faturaAnexoService = faturaAnexoService;
	}

	public FaturaAnexoService getFaturaAnexoService() {
		return faturaAnexoService;
	}

	public void setBloqueioFaturamentoService(
			BloqueioFaturamentoService bloqueioFaturamentoService) {
		this.bloqueioFaturamentoService = bloqueioFaturamentoService;
	}

	public void setRelacaoPagtoParcialService(
			RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}

	public RelacaoPagtoParcialService getRelacaoPagtoParcialService() {
		return relacaoPagtoParcialService;
	}

	/**
	 * LMS-6106 - Busca <tt>Fatura</tt> correspondente a um boleto. Utilizado
	 * para recuperar o tipo de manuten��o (<tt>tpManutencaoFatura</tt>) e no
	 * processo de cancelamento da fatura.
	 * 
	 * @param idBoleto
	 *            id do <tt>Boleto</tt>
	 * @return <tt>Fatura</tt> relacionada ao boleto
	 */
	public Fatura findFaturaByBoleto(Long idBoleto) {
		return getFaturaDAO().findFaturaByBoleto(idBoleto);
	}


	//----------------------------------------- Gera��o das Faturas (Report)
	
	private void sendMonitoramentoMensagemToQueue(MonitoramentoMensagemDMN mensagem) {
		if (mensagem == null) {
			return;
		}
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.FINANCEIRO_AUTOMACAO_MONITMENSAGEM_BUSCA, mensagem);
		integracaoJmsService.storeMessage(msg);
	}
        
	private void sendFaturasToQueue(FaturaDMN fatura) {
		if (fatura == null) {
			return;

		}
		JmsMessageSender msg = integracaoJmsService.createMessage(VirtualTopics.EVENTO_FATURA, fatura);
		integracaoJmsService.storeMessage(msg);

	}

	/**
	 * Retorna e encaminha para a fila as informa��es necess�rias 
	 * para a gera��o e envio das faturas (pdf/dropBox/email) por 
	 * um servi�o externo
	 * 
	 * @return List<FaturaDMN>
	 *
	 */
	@SuppressWarnings("unchecked")
	public void gerarFaturas() {
		setMyLocale();
		List<MonitoramentoMensagem> monitoramentos = monitoramentoMensagemService.findMonitoramentosFaturar();
		TypedFlatMap parametros = new TypedFlatMap();
		FaturaDMN faturaDMN = new FaturaDMN();

		for (MonitoramentoMensagem monitoramentoMensagem : monitoramentos) {
			List<Long> idsFatura = monitoramentoMensagemFaturaService.findFaturas(monitoramentoMensagem);
			parametros.put("importacaoPreFaturas", idsFatura.toString().replace("[", "").replace("]", ""));
			List<List<Map>> imgFatura = new ArrayList();
			String idFaturas = parametros.getString("importacaoPreFaturas");
			if (null != idFaturas && idFaturas.length() > 0) {
				for (List<String> ids : SqlInUtils.splitEachThrousand(idFaturas)) {
					if(!ids.isEmpty()) {
						parametros.put("importacaoPreFaturas", ids.toString().replace("[", "").replace("]", ""));
						imgFatura.add(iteratorSelectFatura(parametros));

					}
				}
			}
			Object dadosFatura = new Object();
			boolean isDropbox = false;
			if(!imgFatura.isEmpty()) {
				dadosFatura = imgFatura.get(0).get(0);
				String blEnviaNas = MapUtils.getString((Map) dadosFatura, "BL_ENVIA_DOCS_FATURAMENTO_NAS");
				isDropbox = blEnviaNas == null || blEnviaNas.equals("N");
				for (List<Map> idsSplitted : imgFatura) {
					for (Object fatura : idsSplitted) {
						faturaDMN.setIdMonitoramentoMensagem(monitoramentoMensagem.getIdMonitoramentoMensagem());
						setFaturaDMN(faturaDMN, (Map) fatura);
						// envia a fatura para a fila, para processamento externo
						sendFaturasToQueue(faturaDMN);
						faturaDMN = new FaturaDMN();

					}
				}
			}
			parametros = new TypedFlatMap();

			//envia o monitoramentoMensagem para a fila para geracao dos emails
			MonitoramentoMensagemDMN mensagemDMN = new MonitoramentoMensagemDMN();
			setMonitoramentoMensagemDMN(monitoramentoMensagem, mensagemDMN);
			mensagemDMN.setIdsFaturas(idsFatura);
			//M�do que dispara o envio de e-mails depois de mei
			if(isDropbox) {
				sendMonitoramentoMensagemToQueue(mensagemDMN);
			}

			//setar monitoramentoMensagem.dhProcessamento com sysdate
			monitoramentoMensagemService.updateDhProcessamentoMonitoramentoMensagem(monitoramentoMensagem);
			monitoramentoMensagemDetalheService.updateDhEnvioMensagemByIdMonitoramentoMensagem(monitoramentoMensagem);

			LOGGER.info("Faturas do MonitoramentoMensagem enviadas para processamento. idMonitoramentoMensagem: " + monitoramentoMensagem.getIdMonitoramentoMensagem());

		}
	}

	/**
	 * Retorna e encaminha para a fila as informacoes necessarias
	 * para a gerar e envio dos documentos das faturas (email) por
	 * um servico externo (NAS)
	 */
	@SuppressWarnings("unchecked")
	public MonitoramentoMensagemDMN buscaMonitoramentoMensagem(FaturaDMN faturaDMN) {
		setMyLocale();
		List<MonitoramentoMensagem> monitoramentos = monitoramentoMensagemService.findMonitoramentoMensagemByIdFatura(faturaDMN.getIdFatura());
		MonitoramentoMensagem monitoramentoMensagem = monitoramentos.get(0);
		MonitoramentoMensagemDMN mensagemDMN = new MonitoramentoMensagemDMN();
		setMonitoramentoMensagemDMN(monitoramentoMensagem, mensagemDMN);
		List<Long> idsFatura = monitoramentoMensagemFaturaService.findFaturas(monitoramentoMensagem);
		mensagemDMN.setIdsFaturas(idsFatura);
		return mensagemDMN;
	}
	
	private void setMonitoramentoMensagemDMN(MonitoramentoMensagem monitoramentoMensagem, MonitoramentoMensagemDMN mensagemDMN) {
		mensagemDMN.setIdMonitoramentoMensagem(monitoramentoMensagem.getIdMonitoramentoMensagem());
		mensagemDMN.setDsDestinatario(monitoramentoMensagem.getDsDestinatario());
		mensagemDMN.setDsParametro(monitoramentoMensagem.getDsParametro());
		mensagemDMN.setTpEnvioMensagem(monitoramentoMensagem.getTpEnvioMensagem().getValue());
		mensagemDMN.setTpModeloMensagem(monitoramentoMensagem.getTpModeloMensagem().getValue());

	}

	public List<FaturaDMN> findDadosEnvioFatura(MonitoramentoMensagemDMN mensagemDMN) {
		FaturaDMN faturaDMN = new FaturaDMN();
		List<FaturaDMN> faturasDMN = new ArrayList<FaturaDMN>();
		List<Long> idsFaturas = mensagemDMN.getIdsFaturas();

		Collections.sort(idsFaturas);
		Boolean first = true;
		for (Long idFatura : idsFaturas) {
			Fatura fatura = this.findById(idFatura);
			FaturaCloud faturaCloud = faturaCloudService.findByFatura(idFatura);

			String dsLink = faturaCloud == null ? null : faturaCloud.getDsLink();

			Filial filial = filialService.findById(fatura.getFilialByIdFilial().getIdFilial());
			String sgFilial = filial.getSgFilial();
			String razaoSocialFilial = filial.getPessoa().getNmPessoa();
			String nrFatura = org.apache.commons.lang3.StringUtils.leftPad(fatura.getNrFatura().toString(), 10, "0");
			faturaDMN.setIdMonitoramentoMensagem(mensagemDMN.getIdMonitoramentoMensagem());
			faturaDMN.setIdFatura(idFatura);
			faturaDMN.setSgFilialNrFatura(sgFilial + " " + nrFatura);
			faturaDMN.setDtEmissao(fatura.getDtEmissao());
			faturaDMN.setDtVencimento(fatura.getDtVencimento());
			faturaDMN.setVlTotal(fatura.getVlTotal());

			String nmEmpresa = fatura.getCliente().getPessoa().getNmPessoa();
			String nrIdentificacao = fatura.getCliente().getPessoa().getNrIdentificacao();
			faturaDMN.setDsEmpresaCliente(nrIdentificacao + " - " + nmEmpresa);
			faturaDMN.setPath(getPath(faturaDMN.getSgFilialNrFatura(), faturaDMN.getDsEmpresaCliente()));
			faturaDMN.setUrlCloud(dsLink);

			faturaDMN.setNmPessoaFilial(razaoSocialFilial);

			//Adiciona os dados de envio apenas na primeira fatura do monitoramento
			if(first) {
				DadosEnvioFaturaDMN dadosEnvioFatura = new DadosEnvioFaturaDMN();
				Long idFilial = filial.getIdFilial();
				Long idMonitoramentoMens = mensagemDMN.getIdMonitoramentoMensagem();
				String tpModeloEmail = mensagemDMN.getTpModeloMensagem();
				dadosEnvioFatura.setTpModeloMensagem(tpModeloEmail);
				dadosEnvioFatura.setIdMonitoramentoMensagem(idMonitoramentoMens);

				HistoricoFilial historicoFilial = historicoFilialService.findHistoricoFilialVigente(idFilial);
				String tpFilial = historicoFilial != null ? historicoFilial.getTpFilial().getValue() : null;

				if (null != tpFilial && "FR".equals(tpFilial)) {
					Object[] contatoArray = contatoService.findContatoTelefoneFilialByIdFilialTpContato(idFilial, FATURAMENTO);
					if (null == contatoArray) {
						return null;
					}
					dadosEnvioFatura.setSgFilial((String) contatoArray[1]);
					dadosEnvioFatura.setDsEmailContato((String) contatoArray[2]);
					String telefone = "(" + (String) contatoArray[3] + ")" + (String) contatoArray[4];
					dadosEnvioFatura.setNrTelefoneContato(telefone);
					if ("SE".equals(tpModeloEmail)) {
						String dsParametro = mensagemDMN.getDsParametro();
						Map<String, String> contatoMap = getRegional(dsParametro, idMonitoramentoMens);
						dadosEnvioFatura.setDsEmailsDestinatarios(MapUtils.getString(contatoMap, "DS_EMAIL_FATURAMENTO"));
						dadosEnvioFatura.setNmResponsavel(MapUtils.getString(contatoMap, "SG_REGIONAL"));
					} else {
						dadosEnvioFatura.setDsEmailsDestinatarios(mensagemDMN.getDsDestinatario());
						dadosEnvioFatura.setNmResponsavel((String) contatoArray[0]);
						dadosEnvioFatura.setDsRemetente((String) contatoArray[2]);
					}
				} else if (null != tpFilial && "FI".equals(tpFilial)) {
					String dsParametro = mensagemDMN.getDsParametro();
					Map<String, String> contatoMap = getRegional(dsParametro, idMonitoramentoMens);
					dadosEnvioFatura.setSgFilial(MapUtils.getString(contatoMap, "SG_REGIONAL"));
					dadosEnvioFatura.setDsEmailContato(MapUtils.getString(contatoMap, "DS_EMAIL_FATURAMENTO"));
					String telefone = "(" + MapUtils.getString(contatoMap, "NR_DDD") + ")" + MapUtils.getString(contatoMap, "NR_TELEFONE");
					dadosEnvioFatura.setNrTelefoneContato(telefone);
					dadosEnvioFatura.setDsEmailsDestinatarios("SE".equals(tpModeloEmail) ? dadosEnvioFatura.getDsEmailContato() : mensagemDMN.getDsDestinatario());
					dadosEnvioFatura.setNmResponsavel(MapUtils.getString(contatoMap, "NM_USUARIO"));
					dadosEnvioFatura.setDsRemetente(MapUtils.getString(contatoMap, "DS_EMAIL_FATURAMENTO"));
				}
				faturaDMN.setDadosEnvioFaturaDMN(dadosEnvioFatura);
				first = false;
			}
			faturasDMN.add(faturaDMN);

			Cliente cliente = fatura.getCliente();

			if (faturaCloud == null  && (cliente.getBlEnviaDocsFaturamentoNas() == null || !cliente.getBlEnviaDocsFaturamentoNas())) {
				sendFaturasToQueue(faturaDMN);
			}
			faturaDMN = new FaturaDMN();
		}
		return faturasDMN;
	}

	private Map<String, String>  getRegional(String dsParametro, Long idMonitoramentoMens) {
		String idRegionalStr = dsParametro.substring(dsParametro.lastIndexOf(":")).replaceAll("\"", "").replace(":", "").replace("}", "");
		Long idRegional = Long.parseLong(idRegionalStr);
		Map<String, String> contatoMap = regionalService.findContatoRegionalByIdRegionalIdMonitoramentoMensagem(idRegional, idMonitoramentoMens);

		return contatoMap;
		
	}
	
	private void setMyLocale() {
		Locale myLocale =  LocaleContextHolder.getLocale();
		if (myLocale.getCountry().isEmpty()) {
			Locale ptBR = new Locale("pt", "BR");
			LocaleContextHolder.setLocale(ptBR);
			
		}
	}
	
	public String getPath(String sgFilialNrFatura, String empresa) {
		String nrFatura = sgFilialNrFatura.replace(" ", "");
		String datum = empresa.replace(".", "").replace("/", "").replaceAll("-", "");
		String identEmpresa = datum.substring(0, datum.indexOf(" "));
		String path = identEmpresa + "_" + nrFatura;

		return path; 
		
	}

	
	private void setFaturaDMN(FaturaDMN faturaDMN, Map fatura) {
		faturaDMN.setNmEmpresa(MapUtils.getString(fatura, "EMPRESA"));
		faturaDMN.setObFatura(MapUtils.getString(fatura, "OB_FATURA"));
		faturaDMN.setVlCotacaoDolar((BigDecimal) MapUtils.getObject(fatura, "COTACAO_DOLAR"));
		faturaDMN.setSgFilialNrFatura(MapUtils.getString(fatura, "SG_FILIAL_NR_FATURA"));
		faturaDMN.setDsEnderecoFilial(MapUtils.getString(fatura, "ENDERECOFILIAL"));
		faturaDMN.setDtEmissao(YearMonthDay.fromDateFields((Date) MapUtils.getObject(fatura, "EMISSAO")));
		faturaDMN.setNmFilial(MapUtils.getString(fatura, "NM_FILIAL"));
		faturaDMN.setDsFormaAgrupamento(MapUtils.getString(fatura, "FORMA_AGRUPAMENTO"));
		faturaDMN.setDtVencimento(YearMonthDay.fromDateFields((Date) MapUtils.getObject(fatura, "VENCIMENTO")));
		faturaDMN.setNrCep(MapUtils.getString(fatura, "CEP"));
		faturaDMN.setTpAgrupamento(MapUtils.getString(fatura, "TIPO_AGRUPAMENTO"));
		faturaDMN.setDsMoeda(MapUtils.getString(fatura, "MOEDA"));
		faturaDMN.setDsModal(MapUtils.getString(fatura, "MODAL"));
		faturaDMN.setTpDocumentoServico(MapUtils.getString(fatura, "TP_DOCUMENTO"));
		faturaDMN.setDsDivisaoCliente(MapUtils.getString(fatura, "DS_DIVISAO_CLIENTE"));
		faturaDMN.setDsAbrangencia(MapUtils.getString(fatura, "ABRANGENCIA"));
		faturaDMN.setNrTelefoneCliente(MapUtils.getString(fatura, "TELEFONECLIENTE"));
		faturaDMN.setNrInscricaoEstadual(MapUtils.getString(fatura, "INSCRICAO"));
		faturaDMN.setDsEnderecoCliente(MapUtils.getString(fatura, "ENDERECOCLIENTE"));
		faturaDMN.setDsBairroCliente(MapUtils.getString(fatura, "BAIRROCLIENTE"));
		faturaDMN.setDsCidadeCliente(MapUtils.getString(fatura, "CIDADECLIENTE"));
		faturaDMN.setDsUfCliente(MapUtils.getString(fatura, "UFCLIENTE"));
		faturaDMN.setNrCepCliente(MapUtils.getString(fatura, "CEPCLIENTE"));
		faturaDMN.setBlValorLiquido(MapUtils.getString(fatura, "BL_VALOR_LIQUIDO"));
		faturaDMN.setTpIdentificacaoFilial(MapUtils.getString(fatura, "TPF_IDENTIFICACAO"));
		faturaDMN.setNrIdentificacaoFilial(MapUtils.getString(fatura, "CNPJ_FILIAL"));
		faturaDMN.setVlJuroDiario((BigDecimal) MapUtils.getObject(fatura, "JURODIARIO"));
		faturaDMN.setIdFatura(MapUtils.getLong(fatura, "ID_FATURA"));
		faturaDMN.setIdFilial(MapUtils.getLong(fatura, "ID_FILIAL"));
		faturaDMN.setTpSituacaoFatura(MapUtils.getString(fatura, "TP_SITUACAO_FATURA"));
		faturaDMN.setIdPaisOrigem(MapUtils.getLong(fatura, "ID_PAIS_ORIGEM"));
		faturaDMN.setIdMoedaOrigem(MapUtils.getLong(fatura, "ID_MOEDA_ORIGEM"));
		faturaDMN.setDsEmpresaCliente(MapUtils.getString(fatura, "EMPRESACLIENTE"));
		faturaDMN.setPath(getPath(faturaDMN.getSgFilialNrFatura(), faturaDMN.getDsEmpresaCliente()));
		faturaDMN.setNmPessoaFilial(MapUtils.getString(fatura, "NM_PESSOA_FILIAL"));
		
	}

	/**
	 * Busca os documentos da fatura
	 * 
	 * @param idFatura
	 * @param idFilial
	 * @param idPaisOrigem
	 * @param idMoedaOrigem
	 * @return
	 */
	public List<DoctoFaturaDMN> findDoctosFatura(FaturaDMN faturaDMN) {
		Long idPais = faturaDMN.getIdPaisOrigem() != null ? faturaDMN.getIdPaisOrigem() : SessionUtils.getPaisSessao().getIdPais();
		Long idMoeda = faturaDMN.getIdMoedaOrigem() != null ? faturaDMN.getIdMoedaOrigem() : SessionUtils.getMoedaSessao().getIdMoeda();
		Long idFilial = faturaDMN.getIdFilial();
		Long idFatura = faturaDMN.getIdFatura();
		
		return cleanDoctosFatura(findDoctosFatura(idFatura, idFilial, idPais, idMoeda), idFatura);
		
	}
	
	private List<Map<String, Object>> findDoctosFatura(Long idFatura, Long idFilial, Long idPaisOrigem, Long idMoedaOrigem) {
		SqlTemplate sql = mountSqlSubFatura(idFatura, idFilial, idPaisOrigem, idMoedaOrigem);

		return ordenaLista(iteratorSubSelectFatura(sql));
		
	}	

	/**
	 * Converte documento por documento para DoctoFaturaDMN e o envia para a fila
	 * @param doctosFatura
	 */
	private List<DoctoFaturaDMN> cleanDoctosFatura(List<Map<String, Object>> doctosFatura, Long idFatura) {
		List<DoctoFaturaDMN> neuDoctosFatura = new ArrayList<DoctoFaturaDMN>();
		if (null != doctosFatura && !doctosFatura.isEmpty()) {
			for (Map<String, Object> doctoFatura : doctosFatura) {
				DoctoFaturaDMN doctoFaturaDMN = new DoctoFaturaDMN();

				doctoFaturaDMN.setRownum(MapUtils.getLong(doctoFatura, "ROWNUM"));
				doctoFaturaDMN.setNotaFiscal(MapUtils.getLong(doctoFatura, "NOTA_FISCAL"));
				doctoFaturaDMN.setDocumentoServico(MapUtils.getString(doctoFatura, "DOCUMENTOSERVICO"));
				doctoFaturaDMN.setValor((BigDecimal) MapUtils.getObject(doctoFatura, "VALOR"));
				doctoFaturaDMN.setIcms((BigDecimal) MapUtils.getObject(doctoFatura, "ICMS"));
				doctoFaturaDMN.setDesconto((BigDecimal) MapUtils.getObject(doctoFatura, "DESCONTO"));
				doctoFaturaDMN.setMercadoria((BigDecimal) MapUtils.getObject(doctoFatura, "MERCADORIA"));
				doctoFaturaDMN.setPeso((BigDecimal) MapUtils.getObject(doctoFatura, "PESO"));
				doctoFaturaDMN.setIdDoctoServico(MapUtils.getLong(doctoFatura, "ID_DOCTO_SERVICO"));
				doctoFaturaDMN.setIdDevedorDocServFat(MapUtils.getLong(doctoFatura, "ID_DEVEDOR_DOC_SERV_FAT"));
				doctoFaturaDMN.setNrDoctoEletronico(MapUtils.getString(doctoFatura, "NR_DOCUMENTO_ELETRONICO"));
				doctoFaturaDMN.setIdFatura(MapUtils.getLong(doctoFatura, "ID_FATURA"));
				doctoFaturaDMN.setTotalPaginas(String.valueOf(MapUtils.getLong(doctoFatura, "TOTAL_PAGINAS")));
				doctoFaturaDMN.setPaginaAtual(String.valueOf(MapUtils.getLong(doctoFatura, "PAGINA_ATUAL")));
				doctoFaturaDMN.setHouveResize(MapUtils.getBoolean(doctoFatura, "HOUVE_RESIZE"));
				doctoFaturaDMN.setTpDocumentoServico(String.valueOf(MapUtils.getString(doctoFatura, "TP_DOCUMENTO_SERVICO")));
				String blEnviaNas = MapUtils.getString(doctoFatura, "BL_ENVIA_DOCS_FATURAMENTO_NAS");
				doctoFaturaDMN.setBlEnviaParaNas(blEnviaNas == null?"N":blEnviaNas);


				neuDoctosFatura.add(doctoFaturaDMN);

			}
		} else {
			LOGGER.info("(cleanDoctosFatura) Fatura sem documentos. idFatura: " + idFatura);

		}
		return neuDoctosFatura;

	}


	/**
	 * Este m�todo ordena a lista de documentos de forma que a impress�o horizontal do ireport pare�a
	 * com a impress�o vertical. (Necess�rio para o "balanceamento" da quantidade de itens em cada coluna.
	 * 
	 * Este m�todo recebe uma lista dos documentos de cada fatura.
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 11/10/2006
	 *
	 * @param listaDocumentos Lista de resultados da query de pesquisa limitado a 1 fatura
	 * @return Lista reordenada
	 */
	public List ordenaLista(List listaDocumentos) {
		
		List retorno = new ArrayList();
		
		/* Metade dos dados por p�gina. 90 itens por p�gina, 45 metade */
		int metade        = 0;
		/* Valor sequencial inicial de cada p�gina, primeira p�gina � 1, na segunda ser� 91, etc... */
		int base          = 1;
		/* N�mero de p�ginas que o relat�rio ir� conter */
		int paginas       = 0;		
		/* N�mero de documentos de cada fatura */
		int qtdDocumentos = 0;		
		
		/* Quantidade de documentos desta fatura */
		int itens = listaDocumentos.size();
		
		BigDecimal baseItens   = new BigDecimal(itens);
		BigDecimal basePaginas = null;
		
		//Se tivermos menos de 3 documentos n�o � necess�rio a reordena��o
		if( itens < 3 ){	
			for (Iterator iter = listaDocumentos.iterator(); iter.hasNext();) {
				setaPaginas((Map) iter.next(),1,1);
			}
			retorno = completaPagina(listaDocumentos,false,1,1);
		} else {
			
			float maximoPagina = QTD_REGISTROS_QUARTO_QUADRANTE;
		
			metade  = Math.round(itens / 2f);
			basePaginas = new BigDecimal(maximoPagina).setScale(2,BigDecimal.ROUND_HALF_UP);
			basePaginas = baseItens.divide(basePaginas,2,BigDecimal.ROUND_HALF_UP);
			
			if( basePaginas.compareTo(BigDecimalUtils.round(new BigDecimal(basePaginas.intValue()))) > 0 ){
				paginas = basePaginas.intValue()+1;
			} else {
				paginas = basePaginas.intValue();
			}
			
			if( paginas < 1 ){
				paginas = 1;
			}
			
			int qtdDocumentosUltima = qtdDocumentos = itens - ((paginas-1)*90);
			int qtdPaginasAMais = 0;
			
			if( (qtdDocumentosUltima / 2f) > QTD_REGISTROS_PRIMEIRO_QUADRANTE ){
				qtdPaginasAMais = paginas + 1;
			} else {
				qtdPaginasAMais = paginas;
			}
			
			boolean houveResize = qtdPaginasAMais == (paginas+1);
			
			//itera todas as p�ginas
			for (int i = 1; i <= paginas; i++) {
				
				if( paginas > 1 ){
					
					//se for a �ltima p�gina
					if( i == paginas ){
						//Calcula a quantidade de documentos na �ltima p�gina
						qtdDocumentos = itens - ((i-1)*90);
						//calcula a metade
						metade  = Math.round(qtdDocumentos / 2f);
						//realiza a montagem da p�gina
						retorno.addAll(montaPagina(base, metade, listaDocumentos, i, qtdPaginasAMais, houveResize));
						
					} else {//se n�o for a �ltima p�gina
						//monta esta p�gina
						retorno.addAll(montaPagina(base,
								                   QTD_REGISTROS_TERCEIRO_QUADRANTE,listaDocumentos, 
								                   i, qtdPaginasAMais, houveResize ));
						
					}
					//controle do valor base
					base = (i*90)+1;
					
				} else {
					//chamada para primeira p�gina : Base = 1
					retorno.addAll(montaPagina(base,metade,listaDocumentos, i, qtdPaginasAMais, houveResize));
				}	
				
			}
			
		}
		
		return retorno;				
		
	}
	
	/**
	 * M�todo respons�vel por verificar a quantidade de resgistros a serem impressos na p�gina
	 * e dependendo da quantidade completa um total de 90 registros por p�gina com linhas em branco.
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 11/10/2006
	 *
	 * @param retorno Lista com os dados da p�gina
	 * @param paginaAMais Identifica se ser� necess�rio uma p�gina a mais devido a 
	 *        quantidade de registros por p�gina ter sido excedido
	 * @param pagina N�mero da p�gina atual
	 * @param paginas Quantidade de p�ginas do documento
	 * @return Lista da p�gina completada
	 */
	private List completaPagina(List retorno, boolean paginaAMais, int pagina, int paginas) {
		if (!retorno.isEmpty()) {
			int nrBrancos = 0;
			
			float metadeParcial = retorno.size() / 2f;
			int metade = Math.round(retorno.size() / 2f);
			
			if( metade <= QTD_REGISTROS_PRIMEIRO_QUADRANTE ){					
				nrBrancos = (QTD_REGISTROS_PRIMEIRO_QUADRANTE - metade)*2;					
			} else if( metade <= QTD_REGISTROS_TERCEIRO_QUADRANTE ){					
				nrBrancos = (QTD_REGISTROS_TERCEIRO_QUADRANTE - metade)*2;
			}
			
			if( metadeParcial < metade ){
				nrBrancos += Math.round(metade - metadeParcial);
			}
		
			Map mapFatura = (Map) retorno.get(0);
			if (mapFatura != null) {
				Long idFatura = (Long) mapFatura.get("ID_FATURA");
				for (int i = 0; i < nrBrancos; i++) {
					retorno.add(mapBlank(mapFatura,idFatura));
				}
				
				if( paginaAMais && pagina == (paginas-1) ){
					mapFatura.put("HOUVE_RESIZE",Boolean.TRUE);
					retorno.add(mapBlank(mapFatura,idFatura));
				} else {
					mapFatura.put("HOUVE_RESIZE",Boolean.FALSE);
				}
			}
		}
		
		return retorno;
	}

	/**
	 * Este m�todo � respons�vel por montar a p�gina, ou seja, recebe a lista em uma determinada ordem
	 * e realiza a reordem dos itens da lista, setando as p�ginas em cada linha da listagem.
	 * � chamada pelo "ordenaLista" e realiza a chamada do "completaP�gina".
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 13/10/2006
	 *
	 * @param itens Quantidade de itens na p�gina
	 * @param base N�mero do registro inicial da p�gina
	 * @param metade Valor da metade dos itens da p�gina (itens/2)
	 * @param listaDocumentos Lista dos itens da p�gina
	 * @param pagina N�mero da p�gina atual
	 * @param paginas Quantidade de p�ginas da fatura
	 * @param paginaAMais Identifica se ser� necess�rio uma p�gina a mais devido a 
	 *        quantidade de registros por p�gina ter sido excedido 
	 * @return Lista com os dados reordenados e completos
	 */
	private List montaPagina(int base, int metade, List listaDocumentos, int pagina, int paginas, boolean paginaAMais){
		int proximo = 0;
		int altBase = base;
		List retorno = new ArrayList();
			
		for (int i = 0; i < metade; i++) {
			
			if( metade <= QTD_REGISTROS_TERCEIRO_QUADRANTE ){					
				proximo = altBase + metade;
			} 
			
			Map mapFatura = setaPaginas((Map) listaDocumentos.get(altBase-1), pagina, paginas);
			retorno.add(mapFatura);
			
			//tratamento para verificar a quantidade de documentos 
			if( (proximo-1) < listaDocumentos.size() ){				
				Map mapFaturaProximo = setaPaginas((Map) listaDocumentos.get(proximo-1), pagina, paginas);
				retorno.add(mapFaturaProximo);				
			}
			
			altBase++;				
			
		}
		
		return completaPagina(retorno,paginaAMais, pagina, paginas);
	}

	/**
	 * Seta a p�gina e a quantidade total de p�ginas de determinada fatura
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 13/10/2006
	 *
	 * @param map Representa uma linha da listagem de uma fatura
	 * @param pagina N�mero da p�gina atual
	 * @param paginas Quantidade de p�ginas da fatura
	 * @return Map com os dados de p�gina e p�ginas setados
	 */
	private Map setaPaginas(Map map, int pagina, int paginas) {
		map.put("PAGINA_ATUAL",Long.valueOf(pagina));
		map.put("TOTAL_PAGINAS",Long.valueOf(paginas));	
		return map;
		
	}

	private Map mapBlank(Map map, Long idFatura) {
		Map newMap = new HashMap();
		newMap.putAll(map);

		newMap.put("FILIAL_ORIGEM", StringUtils.EMPTY);
		newMap.put("DOCUMENTOSERVICO", StringUtils.EMPTY);
		newMap.put("NOTA_FISCAL", null);
		newMap.put("MOEDA", map.get("MOEDA"));
		newMap.put("VALOR", null);
		newMap.put("ICMS", null);
		newMap.put("DESCONTO", null);
		newMap.put("ROWNUM", null);
		newMap.put("ID_DOCTO_SERVICO",null);
		newMap.put("ID_DEVEDOR_DOC_SERV_FAT",null);
		newMap.put("MERCADORIA",new BigDecimal(0));
		newMap.put("PESO",new BigDecimal(0));
		newMap.put("FATURA", idFatura);
		newMap.put("NR_DOCUMENTO_ELETRONICO",StringUtils.EMPTY);
		return newMap;
	}	
	
	
	/**
	 * Retorna dados para o Report interno (LMS) 	
	 * @param criteria
	 * 
	 * @return {@link SqlTemplate}
	 */
	public SqlTemplate mountSqlFatura(TypedFlatMap criteria) {
		return getFaturaDAO().mountSqlFatura(criteria);

	}
	
	public SqlTemplate getDefaultQuery(SqlTemplate sql, TypedFlatMap criteria) {
		return getFaturaDAO().getDefaultQuery(sql, criteria);

	}
	
	public SqlTemplate mountSqlSubFatura(Long idFatura, Long idFilial, Long idPaisOrigem, Long idMoedaOrigem) {
		return getFaturaDAO().mountSqlSubFatura(idFatura, idFilial, idPaisOrigem, idMoedaOrigem);

	}

	public boolean isCnpjIsIn(String cnpj) {
		String cnpjs = (String) parametroGeralService.findConteudoByNomeParametro("CNPJ_CLIENTE_INTEGRACAO", false);
		if (isNull(cnpjs)) {
			return false;
		}
		String[] split = cnpjs.split(";");
		for (String s : split) {
			if(cnpj.equals(s)) {
				return true;
			}
		}
		return false;
	}
	public FaturaDellDMN getFaturasDell(Long idFatura) {
		return getFaturaDAO().findFaturasDellByIdFatura(idFatura);
	}

	public void updateFatura(TypedFlatMap criteria) {
		getFaturaDAO().updateFatura(criteria);
	}
	
	public List iteratorSelectFatura(TypedFlatMap criteria) {
		List<Map> faturas = getFaturaDAO().iteratorSelectFatura(criteria);
		for (Map map : faturas) {
			BigDecimal juroDiario = calcularPercentualJuroDiario(MapUtils.getLong(map, "ID_FILIAL"), (BigDecimal) MapUtils.getObject(map, "CJURODIARIO"), (BigDecimal) MapUtils.getObject(map, "FILJURODIARIO"), JTDateTimeUtils.getDataAtual());
			map.put("JURODIARIO", juroDiario.divide(new BigDecimal(100).setScale(5), 5, BigDecimal.ROUND_HALF_UP));

		}

		return faturas;

	}

	public List iteratorSubSelectFatura(SqlTemplate sql) {
		return getFaturaDAO().iteratorSubSelectFatura(sql);

	}
	
	public BigDecimal calcularPercentualJuroDiario(Long idFilial, BigDecimal clientePcJuroDiario, BigDecimal filialPcJuroDiario, YearMonthDay dtVigencia) {
		return calcularJurosDiarioService.calcularPercentualJuroDiario(idFilial, clientePcJuroDiario, filialPcJuroDiario, dtVigencia);

	}
	

	//----------------------------------------- FIM Gera��o das Faturas (Report)
	
	/**
	 * LMS-6106 - Executa o cancelamento de uma fatura. Ajusta o atributo
	 * <tt>blCancelaFaturaInteira</tt> da <tt>Fatura</tt> com <tt>true</tt> ou
	 * <tt>false</tt> para cancelamento total (I: Cancelar a fatura inteira) ou
	 * parcial (E: Excluir alguns documentos de servi�o), respectivamente.
	 * 
	 * @param fatura
	 *            <tt>Fatura</tt> para cancelamento
	 * @param tpManutencaoFatura
	 *            tipo de manuten��o da fatura (I ou E)
	 * @param idDoctoServicoList
	 *            lista de id's de <tt>DoctoServico</tt>
	 */
	public void executeCancelarFatura(Fatura fatura, String tpManutencaoFatura,
			List<Long> idDoctoServicoList) {
		getFaturaDAO().executeCancelarFatura(fatura, tpManutencaoFatura,
				idDoctoServicoList);
	}

	public Map storeRegistrarEventoMsg(TypedFlatMap parameters) throws Exception{
		Fatura fatura = findById( parameters.getLong("idFatura") );
		Regional regional = regionalFilialService.findRegional(fatura.getFilialByIdFilial().getIdFilial());
		
		List<Contato> contatos = contatoService.findContatosByIdPessoa(fatura.getCliente().getIdCliente());
		if ( contatos.isEmpty() ){
			throw new BusinessException("LMS-36311");
		}
		boolean existeContatoFaturamento = false;
		String clienteEmail = "";
		for(Contato contato: contatos){
			if ( "FA".equalsIgnoreCase(contato.getTpContato().getValue()) && contato.getDsEmail() != null && !contato.getDsEmail().isEmpty() ){
				existeContatoFaturamento = true;
				clienteEmail += contato.getDsEmail()+",";
			}
		}
		if (!existeContatoFaturamento){
			throw new BusinessException("LMS-36311");
		}
		if ( clienteEmail.endsWith(",") ){
			clienteEmail = clienteEmail.substring(0,clienteEmail.lastIndexOf(","));
		}
		storeMonitoramentoMensagem(fatura, regional, clienteEmail);
		return new HashMap<String,Object>();
	}
	
	private void storeMonitoramentoMensagem(Fatura fatura, Regional regional, String clienteEmail) {
		MonitoramentoMensagem monitoramentoMensagem = new MonitoramentoMensagem();
		monitoramentoMensagem.setTpModeloMensagem(new DomainValue("FA"));
		monitoramentoMensagem.setTpEnvioMensagem(new DomainValue("E"));
		monitoramentoMensagem.setDhInclusao(new DateTime());
		monitoramentoMensagem.setDsParametro("{\":1\":\""+regional.getIdRegional()+"\"}");
		monitoramentoMensagem.setDsDestinatario("{\"de\":\""+regional.getDsEmailFaturamento()+"\",\"para\": \""+clienteEmail+"\"}");
		monitoramentoMensagemService.store(monitoramentoMensagem);
		
		MonitoramentoMensagemFatura monitoramentoMensagemFatura = new MonitoramentoMensagemFatura();
		monitoramentoMensagemFatura.setMonitoramentoMensagem(monitoramentoMensagem);
		monitoramentoMensagemFatura.setFatura(fatura);
		monitoramentoMensagemFaturaDao.store(monitoramentoMensagemFatura);
		monitoramentoMensagemService.saveEventoMensagem(monitoramentoMensagem.getIdMonitoramentoMensagem(), "S","LMS-36310");
	}
	
	public void storeMonitoramentoMensagem(TypedFlatMap parameters) {
		Fatura fatura = findById( parameters.getLong("idFatura"));
		String clienteEmail = parameters.getString("clienteEmail");
		
		Regional regional = regionalFilialService.findRegional(fatura.getFilialByIdFilial().getIdFilial());
		storeMonitoramentoMensagem(fatura, regional, clienteEmail);
	}
	
	public Map<String, Object> findSessionData() {
		Map<String, Object> sessionData = new HashMap<String, Object>();
		sessionData.put("usuario", usuarioService.findUsuarioByLogin("automacaoFinanceiro"));
		sessionData.put("pais", paisService.findById(30L));
		sessionData.put("moeda", moedaService.findById(1L));
		sessionData.put("filial", filialService.findById(361L));
		
		return sessionData;
	}
	
	public boolean existeMonitoramentoMensagem(Long idFatura) {
		return monitoramentoMensagemService.findMonitoramentoMensagem(idFatura);
	}
	
	public void setMonitoramenoMensagemService(MonitoramentoMensagemService monitoramenoMensagemService) {
		this.monitoramentoMensagemService = monitoramenoMensagemService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}
	public void setMonitoramentoMensagemFaturaDao(MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDao) {
		this.monitoramentoMensagemFaturaDao = monitoramentoMensagemFaturaDao;
	}

	public void setRecebimentoPosLiqFaturaDAO(RecebimentoPosLiqFaturaDAO recebimentoPosLiqFaturaDAO) {
		this.recebimentoPosLiqFaturaDAO = recebimentoPosLiqFaturaDAO;
	}

	public String findFaturasVencidasEAVencerFullSQL(TypedFlatMap tfm) {
		return getFaturaDAO().findFaturasVencidasEAVencerFullSQL(tfm);
	}

	public Serializable storeRecebimentoPosLiqFatura(TypedFlatMap parameters) {
		RecebimentoPosLiqFatura bean = new RecebimentoPosLiqFatura();
		bean.setIdRecebimentoPosLiqFatura(parameters.getLong("idRecebimento"));
		bean.setUsuario(SessionUtils.getUsuarioLogado());
		bean.setDtRecebimento( parameters.getYearMonthDay("dtRecebimento") );
		bean.setVlRecebimento(parameters.getBigDecimal("valorRecebimento"));
		bean.setObRecebimento(parameters.getString("observacoes"));
		bean.setDhAlteracao(new DateTime());
		bean.setFatura( getFaturaDAO().findByIdFatura( parameters.getLong("idFatura") ) );
		recebimentoPosLiqFaturaDAO.store(bean);;
		return bean;
	}
	
	public boolean findExistComposicaoPagamento(Long idFatura) {
		return getFaturaDAO().findExistsComposicaoPagamento(idFatura);
	}

	public void setMonitoramentoMensagemFaturaService(
			MonitoramentoMensagemFaturaService monitoramentoMensagemFaturaService) {
		this.monitoramentoMensagemFaturaService = monitoramentoMensagemFaturaService;
	}

	public void setCalcularJurosDiarioService(CalcularJurosDiarioService calcularJurosDiarioService) {
		this.calcularJurosDiarioService = calcularJurosDiarioService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public void setFaturaCloudService(FaturaCloudService faturaCloudService) {
		this.faturaCloudService = faturaCloudService;
	}

	public void setMonitoramentoMensagemDetalheService(MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService) {
		this.monitoramentoMensagemDetalheService = monitoramentoMensagemDetalheService;
	}

	public void generateTopicEntry(Fatura fatura) {
		FaturaDMN faturaDMN = new FaturaDMN(); 
		faturaDMN.setIdFatura(fatura.getIdFatura());
		JmsMessageSender msg = integracaoJmsService.createMessage(VirtualTopics.EVENTO_ENVIA_FATURA, fatura);
		integracaoJmsService.storeMessage(msg);
	}

	public D2LIntegracaoDMN findFaturaLayoutD2l(FaturaDellDMN faturaDellDMN) {
		return getlIntegracaoDMN(faturaDellDMN);
	}

	private D2LIntegracaoDMN getlIntegracaoDMN(FaturaDellDMN faturaDellDMN) {
		String cnpjs = (String) parametroGeralService.findConteudoByNomeParametro("CNPJ_INTEGRACAO_DELL_D2L", false);
		for(String cnpj : cnpjs.split(";")){
			if(faturaDellDMN.getNrIdentificacaoCliente().equals(cnpj)) {
				return getD2LIntegracaoDMN(faturaDellDMN);
			}
		}
		return new D2LIntegracaoDMN();
	}

	private D2LIntegracaoDMN getD2LIntegracaoDMN(FaturaDellDMN faturaDellDMN) {
		Cabecalho cabecalho = new Cabecalho();
		cabecalho.setDataCriacao();
		Rodape rodape = new Rodape();
		FaturaD2l dadosFatura = getFaturaDAO().findDadosFatura(faturaDellDMN.getIdFatura());
		FaturaDetalheD2l dadosFaturaDetalhe = getFaturaDAO().findDadosDetalheFatura(faturaDellDMN.getIdFatura());

		D2LIntegracaoDMN d2LIntegracaoDMN = new D2LIntegracaoDMN();
		d2LIntegracaoDMN.setCabecalho(cabecalho);
		d2LIntegracaoDMN.setRodape(rodape);
		d2LIntegracaoDMN.setFatura(dadosFatura);
		d2LIntegracaoDMN.setFaturaDetalhe(dadosFaturaDetalhe);

		d2LIntegracaoDMN.setListaD2l(montarD2ls(faturaDellDMN.getIdFatura()));

		d2LIntegracaoDMN.setIdCliente(Long.parseLong(dadosFatura.getIdCliente()));
		d2LIntegracaoDMN.setIdFatura(faturaDellDMN.getIdFatura());
		validaDadosRetornoQueryD2L(d2LIntegracaoDMN);
		return d2LIntegracaoDMN;
	}

	private List<D2l> montarD2ls(Long idFatura){
		List<D2l> d2ls = new ArrayList<>();
		List<DadosConhecimentoD2l> dadosConhecimentoCTRC = getFaturaDAO().findDadosConhecimentoCtr(idFatura);
		List<FaturaConhecimentoD2l> relacionaFaturaConhecimento = getFaturaDAO().findRelacionaFaturaConhecimento(idFatura);
		List<ConhecimentoNotaFiscalD2l> relacionaConhecimentoNotaFiscal = getFaturaDAO().findRelacionaConhecimentoNotaFiscal(idFatura);
		List<Tipo07> tp07 = getFaturaDAO().findTipo7(idFatura);

		Map<String, List<DadosConhecimentoD2l>> dadosConhecimentoCTRCGroup = dadosConhecimentoCTRC.stream().collect(Collectors.groupingBy(d -> d.getNumeroDocumentoFiscal()));
		Set<String> ctes = dadosConhecimentoCTRCGroup.keySet();
		for(String cte : ctes){
			D2l d2l = new D2l();
			List<DadosConhecimentoD2l> dadosConhecimentosList = dadosConhecimentoCTRCGroup.get(cte);
			List<FaturaConhecimentoD2l> faturaConhecimentoList = relacionaFaturaConhecimento.stream().filter(r -> r.getNumeroDocumentoFilho().equals(cte)).collect(Collectors.toList());
			List<ConhecimentoNotaFiscalD2l> conhecimentoNotaFiscalList = relacionaConhecimentoNotaFiscal.stream().filter(r -> r.getNumeroDocumentoPai().equals(cte)).collect(Collectors.toList());
			List<Tipo07> tipo07List = tp07.stream().filter(t -> t.getNumeroOrdemDell().equals(cte)).collect(Collectors.toList());
			DadosConhecimentoD2l dadosConhecimento = dadosConhecimentosList.get(0);
			FaturaConhecimentoD2l faturaConhecimento = faturaConhecimentoList.get(0);
			Tipo07 tipo07 = tipo07List.get(0);

			d2l.setConhecimento(dadosConhecimento);
			d2l.setFaturaConhecimento(faturaConhecimento);
			d2l.setTipo07(tipo07);
			d2l.setConhecimentoNotasFiscais(conhecimentoNotaFiscalList);

			d2ls.add(d2l);
		}
		return d2ls;
	}

	private void validaDadosRetornoQueryD2L(D2LIntegracaoDMN d2LIntegracaoDMN) {
		Cabecalho cabecalho = d2LIntegracaoDMN.getCabecalho();
		FaturaD2l faturaD2l = d2LIntegracaoDMN.getFatura();
		FaturaDetalheD2l faturaDetalheD2l = d2LIntegracaoDMN.getFaturaDetalhe();
		List<D2l> listaD2l = d2LIntegracaoDMN.getListaD2l();
		Rodape rodape = d2LIntegracaoDMN.getRodape();

		validaValor(cabecalho, "Cabecalho");
		validaValor(faturaD2l, "FaturaD2l");
		validaValor(faturaDetalheD2l, "FaturaDetalheD2l");
		validaValor(listaD2l, "ListaD2l");
		validaValor(rodape, "Rodape");
	}

	private void validaValor(Object valor, String campo) {
		if(valor == null) {
			throw new BusinessException(" " + campo + " não pode ser nulo ou vazio");
		}
	}


}

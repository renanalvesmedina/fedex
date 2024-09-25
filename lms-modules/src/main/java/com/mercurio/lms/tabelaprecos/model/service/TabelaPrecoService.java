package com.mercurio.lms.tabelaprecos.model.service;

import static java.lang.Boolean.TRUE;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.tabelaprecos.model.DadosTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoAnexo;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.model.dao.AtualizaTabelaCustoTNTDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.HistoricoReajusteClienteService;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.tabelaPrecoService"
 */
public class TabelaPrecoService extends CrudService<TabelaPreco, Long> {
	private static final String WORKFLOW_APROVADO = "A";
	private static final Short DESEFETIVACAO_MODAL_RODOVIARIO = Short.valueOf("151");
	private static final Short SOLICITAR_APROVACAO_MODAL_RODOVIARIO = Short.valueOf("150");
	private static final Short SOLICITAR_EFETIVACAO_MODAL_RODOVIARIO = Short.valueOf("149");
	private CalculoSimulacaoNovosPrecosService calculoSimulacaoNovosPrecosService;
	private CalculoNovosPrecosCiasAereasService calculoNovosPrecosCiasAereasService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private TipoTabelaPrecoService tipoTabelaPrecoService;
	private SubtipoTabelaPrecoService subtipoTabelaPrecoService;
	private VigenciaService vigenciaService;
	private TarifaPrecoRotaService tarifaPrecoRotaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ReajusteTabelaClienteService reajusteTabelaClienteService;
	private HistoricoReajusteClienteService historicoReajusteClienteService;
	private ParametroGeralService parametroGeralService; 
	private AtualizaTabelaCustoTNTDAO atualizaTabelaCustoDao;
	private WorkflowPendenciaService workflowPendenciaService; 
	private HistoricoWorkflowService historicoWorkflowService;
	private ObservacaoService observacaoService;
	private ReportExecutionManager reportExecutionManager;
	private ReajusteTabelaPrecoService reajusteTabelaPrecoService;

	public HistoricoReajusteClienteService getHistoricoReajusteClienteService() {
		return historicoReajusteClienteService;
	}

	public void setHistoricoReajusteClienteService(
			HistoricoReajusteClienteService historicoReajusteClienteService) {
		this.historicoReajusteClienteService = historicoReajusteClienteService;
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 *
	 * @param idTipoTabelaPreco
	 * @param idSubtipoTabelaPreco
	 * @return TabelaPreco
	 */
	public TabelaPreco findTabelaPreco(Long idTipoTabelaPreco, Long idSubtipoTabelaPreco) {
		return getTabelaPrecoDAO().findTabelaPreco(idTipoTabelaPreco, idSubtipoTabelaPreco);
	}

	public List<TabelaPreco> findByIdDivisaoCliente(Long idDivisaoCliente){
		return getTabelaPrecoDAO().findByIdDivisaoCliente(idDivisaoCliente);
	}

	public Boolean validateTabelasAereo(Long idDivisaoCliente){
		List<TabelaPreco> tabelas = this.findByIdDivisaoCliente(idDivisaoCliente);
		Boolean hasTabelaAereo = Boolean.FALSE;
		for (TabelaPreco tp:tabelas){
			if ("A".equals(tp.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())){
				hasTabelaAereo = Boolean.TRUE;
			}
		}
		return hasTabelaAereo;
	}

	/**
	 * Recupera uma instância de <code>TabelaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TypedFlatMap findByIdMap(Long id) {
		TabelaPreco tabelaPreco = (TabelaPreco)super.findById(id);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
		Moeda moeda = tabelaPreco.getMoeda();
		retorno.put("moeda.idMoeda", moeda.getIdMoeda());
		retorno.put("moeda.dsSimbolo", moeda.getDsSimbolo());
		retorno.put("moeda.sgMoeda", moeda.getSgMoeda());
		retorno.put("dsDescricao", tabelaPreco.getDsDescricao());
		retorno.put("blEfetivada", tabelaPreco.getBlEfetivada());
		retorno.put("dtGeracao", tabelaPreco.getDtGeracao());
		retorno.put("dtVigenciaInicial", tabelaPreco.getDtVigenciaInicial());
		retorno.put("dtVigenciaFinal", tabelaPreco.getDtVigenciaFinal());
		retorno.put("tabelaPrecoString", tabelaPreco.getTabelaPrecoString());
		retorno.put("obTabelaPreco", tabelaPreco.getObTabelaPreco());

		retorno.put("informacoesTabelaPreco", observacaoService.findObservacao(tabelaPreco.getIdTabelaPreco()));

		retorno.put("blImprimeTabela", tabelaPreco.getBlImprimeTabela());
		
		DomainValue tpCategoria = tabelaPreco.getTpCategoria();
		if(tpCategoria != null) {
			retorno.put("tpCategoria.value", tpCategoria.getValue());
			retorno.put("tpCategoria.description", tpCategoria.getDescription());
		}

		DomainValue tpServico = tabelaPreco.getTpServico();
		if(tpServico != null) {
			retorno.put("tpServico.value", tpServico.getValue());
			retorno.put("tpServico.description", tpServico.getDescription());
		}

		DomainValue tpCalculoFretePeso = tabelaPreco.getTpCalculoFretePeso();
		if(tpCalculoFretePeso != null) {
			retorno.put("tpCalculoFretePeso.value", tpCalculoFretePeso.getValue());
			retorno.put("tpCalculoFretePeso.description", tpCalculoFretePeso.getDescription());
		}

		DomainValue tpCalculoPedagio = tabelaPreco.getTpCalculoPedagio();
		if(tpCalculoPedagio != null) {
			retorno.put("tpCalculoPedagio.value", tpCalculoPedagio.getValue());
			retorno.put("tpCalculoPedagio.description", tpCalculoPedagio.getDescription());
		}

		retorno.put("psMinimo", tabelaPreco.getPsMinimo());
		retorno.put("pcReajuste", tabelaPreco.getPcReajuste());
		retorno.put("pcDescontoFreteMinimo", tabelaPreco.getPcDescontoFreteMinimo());
		if (tabelaPreco.getTpTarifaReajuste() != null) {
			retorno.put("tpTarifaReajuste.value", tabelaPreco.getTpTarifaReajuste().getValue());
			retorno.put("tpTarifaReajuste.description", tabelaPreco.getTpTarifaReajuste().getDescription());
		}
		TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
		retorno.put("tipoTabelaPreco.idTipoTabelaPreco", tipoTabelaPreco.getIdTipoTabelaPreco());
		DomainValue tpTipoTabelaPreco = tipoTabelaPreco.getTpTipoTabelaPreco();
		retorno.put("tipoTabelaPreco.tpTipoTabelaPreco.value", tpTipoTabelaPreco.getValue());
		retorno.put("tipoTabelaPreco.tpTipoTabelaPreco.description", tpTipoTabelaPreco.getDescription());
		retorno.put("tipoTabelaPreco.nrVersao", tipoTabelaPreco.getNrVersao());
		retorno.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.idEmpresa",
				tipoTabelaPreco.getEmpresaByIdEmpresaCadastrada().getIdEmpresa());
		retorno.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa",
				tipoTabelaPreco.getEmpresaByIdEmpresaCadastrada().getPessoa().getNmPessoa());
		/* Usado pela tela de manterTabelasDivisao */
		Servico servico = tipoTabelaPreco.getServico();
		if(servico != null) {
			retorno.put("tipoTabelaPreco.servico.idServico", servico.getIdServico());
			retorno.put("tipoTabelaPreco.servico.dsServico", servico.getDsServico());
			retorno.put("tipoTabelaPreco.servico.tpModal", servico.getTpModal().getValue());
			retorno.put("tipoTabelaPreco.servico.tpAbrangencia", servico.getTpAbrangencia().getValue());
		}
		retorno.put("subtipoTabelaPreco.idSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
		retorno.put("subtipoTabelaPreco.tpSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
		if(tabelaPreco.getTabelaPreco() != null) {
			retorno.put("tabelaPreco.tabelaPrecoString", tabelaPreco.getTabelaPreco().getTabelaPrecoString());
			retorno.put("tabelaPreco.dsDescricao", tabelaPreco.getTabelaPreco().getDsDescricao());
			retorno.put("tabelaPreco.idTabelaPreco", tabelaPreco.getTabelaPreco().getIdTabelaPreco());
		}
		if (tabelaPreco.getUsuario() != null) {
			retorno.put("usuario.idUsuario", tabelaPreco.getUsuario().getIdUsuario());
			retorno.put("usuario.nmUsuario", tabelaPreco.getUsuario().getNmUsuario());
			retorno.put("usuario.nrMatricula", tabelaPreco.getUsuario().getNrMatricula());
		}
		if(tipoTabelaPreco.getCliente() != null) {
			retorno.put("tipoTabelaPreco.cliente.pessoa.nmPessoa",tipoTabelaPreco.getCliente().getPessoa().getNmPessoa());
			retorno.put("tipoTabelaPreco.cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(tipoTabelaPreco.getCliente().getPessoa()));
		}
		retorno.put("blIcmsDestacado", tabelaPreco.getBlIcmsDestacado());
		
		/**
		 * PENDÊNCIAS WORKFLOW
		 */
		
		if(tabelaPreco.getPendencia() != null) {
			retorno.put("idPendencia", tabelaPreco.getPendencia().getIdPendencia());
			DomainValue tpSituacaoPendencia = tabelaPreco.getPendencia().getTpSituacaoPendencia();
			if (WORKFLOW_APROVADO.equals(tpSituacaoPendencia.getValue())) {
				retorno.put("temPendenciaAprovada", true);
			}
		} else {
			retorno.put("idPendencia", null);
			retorno.put("temPendenciaAprovada", null);
		}
		
		if(tabelaPreco.getPendenciaDesefetivacao() != null) {
			retorno.put("idPendenciaDesefetivacao", tabelaPreco.getPendenciaDesefetivacao().getIdPendencia());
		} else {
			retorno.put("idPendenciaDesefetivacao", null);
		}
		
		if(tabelaPreco.getPendenciaEfetivacao() != null) {
			retorno.put("idPendenciaEfetivacao", tabelaPreco.getPendenciaEfetivacao().getIdPendencia());
		} else {
			retorno.put("idPendenciaEfetivacao", null);
		}
		
		TabelaPreco tabelaPrecoCustoTnt = tabelaPreco.getTabelaPrecoCustoTnt();
		if(tabelaPrecoCustoTnt != null) {
			String tipoTabelaPrecoCustoTnt = tabelaPrecoCustoTnt.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
			Integer nrVersao = tabelaPrecoCustoTnt.getTipoTabelaPreco().getNrVersao();
			String tpSubtipoTabelaPreco = tabelaPrecoCustoTnt.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco(); 
			
			String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tipoTabelaPrecoCustoTnt, nrVersao, tpSubtipoTabelaPreco);
			retorno.put("tabelaPrecoCustoTnt.tabelaPrecoString", tabelaPrecoString);
			retorno.put("tabelaPrecoCustoTnt.dsDescricao", tabelaPrecoCustoTnt.getDsDescricao());
			retorno.put("tabelaPrecoCustoTnt.idTabelaPreco", tabelaPrecoCustoTnt.getIdTabelaPreco());
		}
		return retorno;
	}

	public List<TabelaPreco> findTabelaPrecoByIdCliente(Long idCliente, String tipoTabelaPreco, String subtipoTabelaPreco, DateTime dthAtual ) {
		return  getTabelaPrecoDAO().findTabelaPrecoByIdCliente(idCliente, tipoTabelaPreco, subtipoTabelaPreco, dthAtual);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		validateEfetivadas(ids);

		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		validateEfetivadas(ids);
		super.removeByIds(ids);
	}

	/**
	 * Verifica se dentro dos ids passados existe alguma TabelaPreco efetivada.
	 *
	 * @param ids
	 */
	public void validateEfetivadas(List<Long> ids) {
		Integer count = getTabelaPrecoDAO().getCountEfetivados(ids);
		if(CompareUtils.gt(count, IntegerUtils.ZERO)) throw new BusinessException("LMS-30042");
	}
	/**
	 * Verifica se dentro do id passado existe alguma TabelaPreco efetivada.
	 *
	 * @param ids
	 */
	public void validateEfetivadas(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		validateEfetivadas(ids);
	}

	/**
	 * Remove a tabela de preco e seus relacionamentos verificando antes se
	 * a tabela ja esta efetivada. Neste caso a tabela nao podera ser removida.
	 *
	 * @param ids lista com as entidades que deverao ser removidas.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsProposta(List<Long> ids, boolean deleteTipoTabelaPreco) {
		for (int i = 0; i < ids.size(); i++) {
			Long idTabelaPreco = ids.get(i);
			TabelaPreco tabelaPreco = (TabelaPreco) super.findById(idTabelaPreco);
			if (Boolean.TRUE.equals(tabelaPreco.getBlEfetivada())) {
				throw new BusinessException("LMS-30042");
			}
			tabelaPrecoParcelaService.removeByIdTabelaPreco(idTabelaPreco);
			getTabelaPrecoDAO().getSessionFactory().getCurrentSession().flush();

			removeById(idTabelaPreco);

			TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
			SubtipoTabelaPreco subtipoTabelaPreco = tabelaPreco.getSubtipoTabelaPreco();

			if (deleteTipoTabelaPreco) {
				List<TabelaPreco> tabelas = findByTipoTabelaPreco(
						tipoTabelaPreco.getTpTipoTabelaPreco().getValue(),
						subtipoTabelaPreco.getTpSubtipoTabelaPreco(),
						tipoTabelaPreco.getNrVersao()
				);
				if (tabelas == null || tabelas.size() <= 0) {
					tipoTabelaPrecoService.removeById(tipoTabelaPreco.getIdTipoTabelaPreco());
				}
			}
		}
	}
	
	public java.io.Serializable storeTabelaPreco(TabelaPreco tabelaPreco, Boolean blDesativaTabelaAntiga, Long idTabelaPrecoVigente, String informacoesTabelaPreco) {
		
		Serializable resultStore = store(tabelaPreco, blDesativaTabelaAntiga, idTabelaPrecoVigente);
		
		observacaoService.store(informacoesTabelaPreco, tabelaPreco.getIdTabelaPreco());
		
		return resultStore;
	}

	/**
	 * Insere uma nova tabela ou altera uma tabela existente
	 * @param tabelaPreco
	 * @param blDesativaTabelaAntiga Se existe uma tabela semelhante e vigente, indica se esta deve ser desativada.
	 * @param idTabelaPrecoVigente
	 * @return
	 */
	public java.io.Serializable store(TabelaPreco tabelaPreco, Boolean blDesativaTabelaAntiga, Long idTabelaPrecoVigente) {
		TabelaPreco tabelaPrecoParam = tabelaPreco;
		Boolean tabelaOriginalEfetivada = Boolean.FALSE;
		TabelaPreco tabelaPrecoOriginal = null;

		if(Boolean.TRUE.equals(tabelaPreco.getBlEfetivada())) {
			if(tabelaPreco.getIdTabelaPreco() != null) {
				tabelaPrecoOriginal = (TabelaPreco)super.findById(tabelaPreco.getIdTabelaPreco());
				tabelaOriginalEfetivada = tabelaPrecoOriginal.getBlEfetivada();
			}
			//Se tabela ja efetivada apenas altera a vigencia final.
			if(Boolean.TRUE.equals(tabelaOriginalEfetivada)) {
				tabelaPrecoOriginal.setDtVigenciaFinal(tabelaPreco.getDtVigenciaFinal());
				tabelaPrecoOriginal.setObTabelaPreco(tabelaPreco.getObTabelaPreco());
				tabelaPreco = tabelaPrecoOriginal;
			}
		} else {
			if(Boolean.FALSE.equals(blDesativaTabelaAntiga)) {
				// valida data vigencia final para evitar colisões de tabelas vigentes
				this.validateVigencia(tabelaPreco);
			} else {
				//Desativa tabela antiga
				TabelaPreco tabelaPrecoVigente = (TabelaPreco)super.findById(idTabelaPrecoVigente);
				tabelaPrecoVigente.setDtVigenciaFinal(tabelaPreco.getDtVigenciaInicial().minusDays(1));
				getTabelaPrecoDAO().store(tabelaPrecoVigente);
			}
		}
		tabelaPreco.setBlImprimeTabela(tabelaPrecoParam.getBlImprimeTabela());
		getTabelaPrecoDAO().getAdsmHibernateTemplate().evict(tabelaPrecoOriginal);
		return super.store(tabelaPreco);
	}
	
	private void validateVigencia(TabelaPreco tabelaPreco) {
		if (tabelaPreco.getDtVigenciaFinal() != null && this.getTabelaPrecoDAO().validateVigenciaInferiorTabelaPrecoVigente(tabelaPreco)) {
			return;
		}
		
		if (Arrays.asList("C","D","E","F").contains(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())) {
			return;
		}
		
		if (tabelaPreco.getDtVigenciaFinal() == null) {
			tabelaPreco.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		}
		
		Integer nrVersaoConflitoDtVigente = this.getTabelaPrecoDAO().findNrVersaoTabelaPrecoDtVigencia(tabelaPreco);
		if (nrVersaoConflitoDtVigente != null) {
			throw new BusinessException("LMS-30028", new Object[] {
				tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue() + nrVersaoConflitoDtVigente,
				tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco(),
				tabelaPreco.getTipoTabelaPreco().getServico().getDsServico(),
			});
		}
	}

	/**
	 * 
	 * @param tabelaPreco
	 */
	public void validatePendenciaWorkflow(TabelaPreco tabelaPreco){
		if (getTabelaPrecoDAO().validatePendenciaWorkflow(tabelaPreco.getIdTabelaPreco())) {
			Hibernate.initialize(tabelaPreco.getTipoTabelaPreco());
			Hibernate.initialize(tabelaPreco.getSubtipoTabelaPreco());
			throw new BusinessException("LMS-30066", new String[] {tabelaPreco.getTabelaPrecoString()});
		}
	}
	
	@Override
	protected void beforeRemoveById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		beforeRemoveByIds(ids);
	}
	
	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		validateHistoricoWorkflow(ids);
		super.beforeRemoveByIds(ids);
	}
	
	private void validateHistoricoWorkflow(List<Long> ids) {
		for (Long id : ids) {
			if (historicoWorkflowService.validateHistoricoWorkflow(id, TabelaHistoricoWorkflow.TABELA_PRECO)) {
				TabelaPreco tabelaPreco = (TabelaPreco) this.findById(id);
				throw new BusinessException("LMS-30066", new String[] { tabelaPreco.getTabelaPrecoString() });
			}
		}
	}
	
	@Override
	protected TabelaPreco beforeInsert(TabelaPreco bean) {
		TabelaPreco tabelaPreco = bean;
		tabelaPreco.setDtGeracao(JTDateTimeUtils.getDataAtual());
		return super.beforeInsert(bean);
	}

	@Override
	protected TabelaPreco beforeStore(TabelaPreco bean) {
		TabelaPreco tabelaPreco = bean;
		if(tabelaPreco.getDtVigenciaInicial() == null && tabelaPreco.getDtVigenciaFinal() != null){
			throw new BusinessException("LMS-30019");
		}

		tabelaPreco.setUsuario(SessionUtils.getUsuarioLogado());
		return super.beforeStore(bean);
	}
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setTabelaPrecoDAO(TabelaPrecoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TabelaPrecoDAO getTabelaPrecoDAO() {
		return (TabelaPrecoDAO) getDao();
	}

	/* Cria lista para trazer no bean apenas os campos apresentados no Grid.
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		FilterResultSetPage filterRs = new FilterResultSetPage(super.findPaginated(criteria)) {
			@Override
			public Map<String, Object> filterItem(Object item) {
				TabelaPreco tabelaPreco = (TabelaPreco)item;
				TypedFlatMap retorno = new TypedFlatMap();
				retorno.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
				retorno.put("tabelaPrecoString", tabelaPreco.getTabelaPrecoString());
				retorno.put("moeda.dsSimbolo", tabelaPreco.getMoeda().getDsSimbolo());
				retorno.put("moeda.sgMoeda", tabelaPreco.getMoeda().getSgMoeda());
				retorno.put("moeda.siglaSimbolo", tabelaPreco.getMoeda().getSiglaSimbolo());
				retorno.put("dsDescricao", tabelaPreco.getDsDescricao());
				retorno.put("blEfetivada", tabelaPreco.getBlEfetivada());
				retorno.put("dtVigenciaInicial", tabelaPreco.getDtVigenciaInicial());
				retorno.put("dtVigenciaFinal", tabelaPreco.getDtVigenciaFinal());
				retorno.put("pcReajuste", tabelaPreco.getPcReajuste());
				retorno.put("pcDescontoFreteMinimo", tabelaPreco.getPcDescontoFreteMinimo());
				TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
				retorno.put("tipoTabelaPreco.tpTipoTabelaPrecoNrVersao", tipoTabelaPreco.getTpTipoTabelaPrecoNrVersao());
				retorno.put("tipoTabelaPreco.tpTipoTabelaPreco", tipoTabelaPreco.getTpTipoTabelaPreco());
				retorno.put("tipoTabelaPreco.nrVersao", tipoTabelaPreco.getNrVersao());
				retorno.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa",
						tipoTabelaPreco.getEmpresaByIdEmpresaCadastrada().getPessoa().getNmPessoa());
				/* Usado pela tela de manterTabelasDivisao */
				Servico servico = tipoTabelaPreco.getServico();
				if(servico != null) {
					retorno.put("tipoTabelaPreco.servico.idServico", servico.getIdServico());
					retorno.put("tipoTabelaPreco.servico.dsServico", servico.getDsServico());
					retorno.put("tipoTabelaPreco.servico.tpModal", servico.getTpModal());
					retorno.put("tipoTabelaPreco.servico.tpAbrangencia", servico.getTpAbrangencia());
				}
				retorno.put("subtipoTabelaPreco.tpSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
				retorno.put("subtipoTabelaPreco.idSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());

				return retorno;
			}
		};
		return (ResultSetPage)filterRs.doFilter();
	}

	@Override
	public List findLookup(Map criteria) {
		String tabelaPrecoString = (String)criteria.remove("tabelaPrecoString");
		if(StringUtils.isNotBlank(tabelaPrecoString)) {
			Map<String, Object> mycriteria = TabelaPrecoUtils.unmontCdTabelaPreco(tabelaPrecoString);
			if(mycriteria == null) {
				throw new BusinessException("invalidField", new Object[]{configuracoesFacade.getMensagem("tabela")});
			}
			criteria.putAll(mycriteria);
		}
		return getTabelaPrecoDAO().findLookup(criteria);
	}

	public ResultSetPage findPaginatedSimulacao(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getTabelaPrecoDAO().findPaginatedSimulacao(criteria, def);
	}

	public Integer getRowCountSimulacao(TypedFlatMap criteria) {
		return getTabelaPrecoDAO().getRowCountSimulacao(criteria);
	}

	public Integer findUltimaVersaoTabela(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco) {
		return getTabelaPrecoDAO().findUltimaVersaoTabela(tpTipoTabelaPreco, tpSubtipoTabelaPreco);
	}

	public TabelaPreco findByIdTabelaPreco(Long idTabelaPreco) {
		return (TabelaPreco)super.findById(idTabelaPreco);
	}

	public TabelaPreco findByIdTabelaPrecoReajuste(Long idTabelaPreco) {
		return getTabelaPrecoDAO().findByIdTabelaPrecoReajuste(idTabelaPreco);
	}

	public TabelaPreco findByIdTabelaPrecoNotLazy(Long idTabelaPreco) {
		return getTabelaPrecoDAO().findByIdTabelaPrecoNotLazy(idTabelaPreco);
	}

	public TabelaPreco findByIdTabelaDivisaoCliente(Long IdTabelaDivisaoCliente) {
		return getTabelaPrecoDAO().findByIdTabelaDivisaoCliente(IdTabelaDivisaoCliente);
	}

	public void validateTrocaTabelaPreco(TabelaDivisaoCliente bean) {
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(bean.getTabelaPreco().getIdTabelaPreco());
		bean.setTabelaPreco(tabelaPreco);

		if (bean.getTabelaPrecoFob() != null && TRUE.equals(bean.getBlAtualizacaoAutomatica())) {
			TabelaPreco tabelaPrecoFob = findByIdTabelaPrecoReajuste(bean.getTabelaPrecoFob().getIdTabelaPreco());
			if (!tabelaPrecoFob.getTipoTabelaPreco().equals(tabelaPreco.getTipoTabelaPreco())) {
				throw new BusinessException("LMS-01161");
			}
		}

		if(LongUtils.hasValue(bean.getIdTabelaDivisaoCliente())) {
			TabelaPreco tabelaPrecoOld = findByIdTabelaDivisaoCliente(bean.getIdTabelaDivisaoCliente());

			/** Verifica se houve troca da Tabela de Preco */
			if(!tabelaPreco.getIdTabelaPreco().equals(tabelaPrecoOld.getIdTabelaPreco())) {
				/**
				 * Gera Historido referente a Troca de Tabelas
				 * #Quest 11147
				 */
				historicoReajusteClienteService.generateHistoricoReajusteCliente(
					bean.getIdTabelaDivisaoCliente()
					,tabelaPrecoOld.getIdTabelaPreco()
					,tabelaPreco.getIdTabelaPreco()
					,BigDecimal.ZERO
					,"L");
			}
		}
	}

	public TabelaPreco generateSimulacao(TypedFlatMap data) {
		Long idTabelaPreco = data.getLong("tabelaPreco.idTabelaPreco");
		boolean insert = idTabelaPreco == null;
		Long idTabelaBase = data.getLong("tabelaBase.idTabelaPreco");
		Integer nrVersao = data.getInteger("tipoTabelaPreco.nrVersao");
		BigDecimal pcReajuste = data.getBigDecimal("pcReajuste");

		TabelaPreco tabelaPreco = null;
		List<TabelaPrecoParcela> generalidades = null;
		List<TabelaPrecoParcela> taxas = null;

		if(insert) {
			generalidades = data.getList("generalidades");
			taxas = data.getList("taxas");

			TabelaPreco tabelaBase = (TabelaPreco) super.findById(idTabelaBase);
			TipoTabelaPreco tipoTabelaBase = tabelaBase.getTipoTabelaPreco();

			TipoTabelaPreco tipoTabelaPreco = storeTipoTabelaPreco(tipoTabelaBase, nrVersao);

			YearMonthDay dtVigenciaInicial = data.getYearMonthDay("dtVigenciaInicial");

			validateSimulacao(dtVigenciaInicial, pcReajuste, tipoTabelaPreco.getTpTipoTabelaPreco().getValue());

			tabelaPreco = generateSimulacao(idTabelaBase, pcReajuste, taxas, generalidades);
			tabelaPreco.setDtGeracao(JTDateTimeUtils.getDataAtual());
			tabelaPreco.setBlEfetivada(Boolean.FALSE);
			tabelaPreco.setTipoTabelaPreco(tipoTabelaPreco);
			tabelaPreco.setTpCalculoPedagio(tabelaBase.getTpCalculoPedagio());

			Long idSubtipoTabelaBase = data.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco");
			if (idSubtipoTabelaBase != null) {
				SubtipoTabelaPreco subtipoTabelaPreco = subtipoTabelaPrecoService.findById(idSubtipoTabelaBase);
				tabelaPreco.setSubtipoTabelaPreco(subtipoTabelaPreco);
			}

			tabelaPreco.setMoeda(tabelaBase.getMoeda());

			Long idUsuario = data.getLong("funcionario.idUsuario");
			if (idUsuario != null) {
				Usuario usuario = new Usuario();
				usuario.setIdUsuario(idUsuario);
				tabelaPreco.setUsuario(usuario);
			}

			tabelaPreco.setDtVigenciaInicial(data.getYearMonthDay("dtVigenciaInicial"));
			tabelaPreco.setDtVigenciaFinal(data.getYearMonthDay("dtVigenciaFinal"));
			tabelaPreco.setPcReajuste(pcReajuste);
			tabelaPreco.setPsMinimo(tabelaBase.getPsMinimo());
			tabelaPreco.setTabelaPreco(tabelaBase);
			tabelaPreco.setTpCalculoFretePeso(tabelaBase.getTpCalculoFretePeso());
			tabelaPreco.setDsDescricao(tabelaBase.getDsDescricao());
			prepareTabelaPrecoParcelas(tabelaPreco);

		} else {
			tabelaPreco = (TabelaPreco) super.findById(idTabelaPreco);
			tabelaPreco.getTabelaPrecoParcelas().clear();

			// nos casos de edição todas as taxas e generalidades serão reajustadas
			TabelaPreco simulada = generateSimulacao(idTabelaBase, pcReajuste, null, null);
			prepareTabelaPrecoParcelasEdit(tabelaPreco, simulada.getTabelaPrecoParcelas());

			tabelaPreco.setDtVigenciaInicial(data.getYearMonthDay("dtVigenciaInicial"));
			tabelaPreco.setPcReajuste(pcReajuste);
		}
		store(tabelaPreco);

		if(insert) {
			List<TarifaPrecoRota> tarifaPrecoRotas = tarifaPrecoRotaService.findByIdTabelaPreco(idTabelaBase);
			for (TarifaPrecoRota tarifaPrecoRota : tarifaPrecoRotas) {
				TarifaPrecoRota copia = copyTarifaPrecoRota(tarifaPrecoRota, tabelaPreco);

				tarifaPrecoRotaService.validate(copia, idTabelaBase);
				tarifaPrecoRotaService.basicStore(copia);
			}
		}
		getTabelaPrecoDAO().getAdsmHibernateTemplate().flush();
		return tabelaPreco;
	}

	public TabelaPreco generateSimulacao(Long idTabelaBase, BigDecimal pcReajuste, List<TabelaPrecoParcela> taxas, List<TabelaPrecoParcela> generalidades) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();
		List<TabelaPrecoParcela> tabelaPrecoParcelas = null;
		List<TabelaPrecoParcela> reajustes = null;

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "G");
		reajustes = calculoSimulacaoNovosPrecosService.executeReajusteGeneralidade(tabelaPrecoParcelas, pcReajuste, generalidades);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "T");
		reajustes = calculoSimulacaoNovosPrecosService.executeReajusteValorTaxa(tabelaPrecoParcelas, pcReajuste, taxas);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "S");
		reajustes = calculoSimulacaoNovosPrecosService.executeReajusteServicoAdicional(tabelaPrecoParcelas, pcReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "P");
		reajustes = calculoSimulacaoNovosPrecosService.executeReajustePrecoFrete(tabelaPrecoParcelas, pcReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPrecoParcelas = tabelaPrecoParcelaService.findParcelasPrecoByIdTabelaPreco(idTabelaBase, "M");
		reajustes = calculoSimulacaoNovosPrecosService.executeReajusteMinimoProgressivo(tabelaPrecoParcelas, pcReajuste);
		if (reajustes != null) {
			result.addAll(reajustes);
		}

		tabelaPreco.setTabelaPrecoParcelas(result);
		return tabelaPreco;
	}


	/**
	 *
	 * @param idTabelaNova - a tabela nova do reajuste para a qual as parcelas serao copiadas
	 * @param simulada - a tabela com as parcelas copiadas da tabela base
	 * @return
	 */
	public TabelaPreco getTabelaPrecoReajuste(Long idTabelaNova, TabelaPreco simulada){

		//pega a tabela que ja foi gerada no salvar q vai ser a nova
		// para popular as parcelas dela
    	TabelaPreco tabelaPreco = (TabelaPreco) super.findById(idTabelaNova);
		tabelaPreco.getTabelaPrecoParcelas().clear();

		prepareTabelaPrecoParcelasEdit(tabelaPreco, simulada.getTabelaPrecoParcelas());

		return tabelaPreco;
	}

	public TabelaPreco generateSimulacaoCiaAerea(
			Long idTabelaBase,
			BigDecimal pcReajuste,
			String tpReajuste,
			List<TypedFlatMap> reajustesEspecificos,
			List<TypedFlatMap> aeroportosExcecoes,
			List<TypedFlatMap> produtosEspecificosExcecoes
	) {
		TabelaPreco tabelaPreco = new TabelaPreco();
		List<TabelaPrecoParcela> result = new ArrayList<TabelaPrecoParcela>();

		if (idTabelaBase != null){
			TabelaPreco tabelaBase = (TabelaPreco)super.findById(idTabelaBase);
			tabelaPreco.setTpCalculoPedagio(tabelaBase.getTpCalculoPedagio());
		}


		if (ConstantesTabelaPrecos.TP_REAJUSTE_TARIFA_MINIMA.equals(tpReajuste) ||
				ConstantesTabelaPrecos.TP_REAJUSTE_GERAL_MINIMO.equals(tpReajuste)) {
			List<TabelaPrecoParcela> reajustes = calculoNovosPrecosCiasAereasService.executeReajusteTarifaMinima(idTabelaBase, pcReajuste, aeroportosExcecoes, reajustesEspecificos);
			if(reajustes != null) {
				result.addAll(reajustes);
			}
		}

		if (ConstantesTabelaPrecos.TP_REAJUSTE_ESPECIFICO.equals(tpReajuste)) {
			List<TabelaPrecoParcela> reajustes = calculoNovosPrecosCiasAereasService.executeReajusteEspecifico(idTabelaBase, pcReajuste, aeroportosExcecoes, produtosEspecificosExcecoes, reajustesEspecificos);
			if(reajustes != null) {
				result.addAll(reajustes);
			}
		}

		if (ConstantesTabelaPrecos.TP_REAJUSTE_GERAL.equals(tpReajuste) ||
				ConstantesTabelaPrecos.TP_REAJUSTE_GERAL_MINIMO.equals(tpReajuste)) {
			List<TabelaPrecoParcela> reajustes = calculoNovosPrecosCiasAereasService.executeReajusteGeral(idTabelaBase, pcReajuste, aeroportosExcecoes, reajustesEspecificos);
			if(reajustes != null) {
				result.addAll(reajustes);
			}
		}

		if (ConstantesTabelaPrecos.TP_REAJUSTE_GERAL_ESPECIFICO.equals(tpReajuste)) {
			List<TabelaPrecoParcela> reajustes = calculoNovosPrecosCiasAereasService.executeReajusteGeralEspecifico(idTabelaBase, pcReajuste, aeroportosExcecoes, produtosEspecificosExcecoes, reajustesEspecificos);
			if(reajustes != null) {
				result.addAll(reajustes);
			}
		}

		if (ConstantesTabelaPrecos.TP_REAJUSTE_TODOS.equals(tpReajuste)) {
			List<TabelaPrecoParcela> reajustes = calculoNovosPrecosCiasAereasService.executeReajusteGeralEspecifico(idTabelaBase, pcReajuste, aeroportosExcecoes, produtosEspecificosExcecoes, reajustesEspecificos);
			if(reajustes != null) {
				result.addAll(reajustes);
			}

			reajustes = calculoNovosPrecosCiasAereasService.executeReajusteTarifaMinima(idTabelaBase, pcReajuste, aeroportosExcecoes, reajustesEspecificos);
			if (reajustes != null) {
				result.addAll(reajustes);
			}
		}

		List<TabelaPrecoParcela> semReajuste = tabelaPrecoParcelaService.findByIdTabelaPrecoNotParcelaPreco(idTabelaBase, getParcelasPreco(result));
		if(semReajuste != null && semReajuste.size() > 0) {
			for(TabelaPrecoParcela original : semReajuste) {
				result.add(executeCopiaTabelaPrecoParcela(original, tabelaPreco));
			}
		}

		tabelaPreco.setTabelaPrecoParcelas(result);
		return tabelaPreco;
	}

	public java.io.Serializable storeTabelaPrecoCiaAerea(TabelaPreco tabelaPreco, boolean flushBeforStore) {
		if (tabelaPreco.getIdTabelaPreco() != null) {
			tabelaPrecoParcelaService.removeByIdTabelaPreco(tabelaPreco.getIdTabelaPreco());
			if (flushBeforStore) {
				getTabelaPrecoDAO().getSessionFactory().getCurrentSession().flush();
			}
		}
		return super.store(tabelaPreco);
	}

	public void validateSimulacao(YearMonthDay dtVigenciaInicial, BigDecimal pcReajuste, String tpTipoTabela) {
		if (dtVigenciaInicial != null) {
			vigenciaService.validateInicioVigencia(dtVigenciaInicial, "LMS-30040");
		}
		if (pcReajuste == null || CompareUtils.le(pcReajuste, BigDecimalUtils.ZERO)) {
			throw new BusinessException("LMS-04153", new Object[] {"percentualReajuste"});
		}
		if ("C".equals(tpTipoTabela)) {
			throw new BusinessException("LMS-30043");
		}
	}

	public List findByTipoTabelaPreco(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Integer nrVersao) {
		return getTabelaPrecoDAO().findByTipoTabelaPreco(tpTipoTabelaPreco, tpSubtipoTabelaPreco, nrVersao);
	}

	/**
	 * Efetivação da Tabela de Preço.
	 * @param idTabelaPreco
	 * @param idSubtipoTabelaPreco
	 * @param tpTipoTabelaPreco
	 * @param blEfetivada
	 * @param dtVigenciaInicial
	 */
	public void executeEfetivarTabela(Long idTabelaPreco, Long idSubtipoTabelaPreco, DomainValue tpTipoTabelaPreco, Boolean blEfetivada, YearMonthDay dtVigenciaInicial) {
		//Regra 1 - Verifica se a data de vigência inicial está preenchida e se é maior que a data atual.
		vigenciaService.validateInicioVigencia2(dtVigenciaInicial, "LMS-30040");

		//Regra 2 - Atualiza data final da tabela vigente, se houver.
		TabelaPreco tabelaPrecoAtual = getTabelaPrecoDAO().findTabelaVigente(tpTipoTabelaPreco, idSubtipoTabelaPreco, blEfetivada, JTDateTimeUtils.getDataAtual());
		this.updateVigenciaTabelaAtual(tabelaPrecoAtual, dtVigenciaInicial);

		//Regra 2 - Atualiza data final da tabela origem.
		TabelaPreco tabelaPreco = this.get(idTabelaPreco);
		this.updateVigenciaTabelaOrigem(tabelaPreco, dtVigenciaInicial);

		//Regra 3 - Seta a nova tabela como efetivada.
		tabelaPreco.setBlEfetivada(Boolean.TRUE);
		tabelaPreco.setDtVigenciaInicial(dtVigenciaInicial);
		this.store(tabelaPreco);

		/** Agenda Reajuste das Tabelas dos Clientes */
		if (!"C".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())) {
			reajusteTabelaClienteService.executeAgendamentoReajusteCliente(tabelaPreco.getIdTabelaPreco(), dtVigenciaInicial);
		}
	}

	/**
	 * Finaliza Tabela Vigente.
	 * @param tabelaPrecoAtual
	 * @param dtVigenciaInicial
	 */
	private void updateVigenciaTabelaAtual(TabelaPreco tabelaPrecoAtual, YearMonthDay dtVigenciaInicial){
		if(tabelaPrecoAtual != null) {
			if(!"D".equals(tabelaPrecoAtual.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
				&& !"E".equals(tabelaPrecoAtual.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
				&& !"C".equals(tabelaPrecoAtual.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
			) {
				tabelaPrecoAtual.setDtVigenciaFinal(dtVigenciaInicial.minusDays(1));
				this.store(tabelaPrecoAtual);
			}
		}
	}

	/**
	 * Finaliza a vigencia da Tabela de Origem para os tipos 'D' e 'E'.
	 * @param tabelaPreco
	 * @param dtVigenciaInicial
	 */
	private void updateVigenciaTabelaOrigem(TabelaPreco tabelaPreco, YearMonthDay dtVigenciaInicial){
		if("D".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
			|| "E".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
		) {
			TabelaPreco tabelaPrecoOrigem = tabelaPreco.getTabelaPreco();
			tabelaPrecoOrigem.setDtVigenciaFinal(dtVigenciaInicial.minusDays(1));
			this.store(tabelaPrecoOrigem);
		}
	}

	/**
	 * Busca o cliente relacionado a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return cliente relacionado
	 */
	public TypedFlatMap findClienteByIdTabelaPreco(Long idTabelaPreco) {
		TypedFlatMap result = getTabelaPrecoDAO().findClienteByIdTabelaPreco(idTabelaPreco);
		if(result != null) {
			result.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(result.getString("cliente.pessoa.tpIdentificacao.value"), result.getString("cliente.pessoa.nrIdentificacao")));
		}
		return result;
	}

	/**
	 * Busca a moeda relacionada a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return moeda relacionada
	 */
	public TypedFlatMap findMoedaByIdTabelaPreco(Long idTabelaPreco) {
		return getTabelaPrecoDAO().findMoedaByIdTabelaPreco(idTabelaPreco);
	}

	/**
	 * Busca o tipo e subtipo relacionada a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return tipo e subtipo relacionada
	 */
	public TypedFlatMap findTiposByIdTabelaPreco(Long idTabelaPreco) {
		return getTabelaPrecoDAO().findTiposByIdTabelaPreco(idTabelaPreco);
	}

	/**
	 * Busca a tabela vigente na data de referência informada.
	 *
	 * @param tpTipoTabelaPreco
	 * @param tpSubtipoTabelaPreco
	 * @param blEfetivada
	 * @param dtReferencia
	 * @return
	 */
	public TabelaPreco findTabelaVigente(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Boolean blEfetivada, YearMonthDay dtReferencia) {
		return getTabelaPrecoDAO().findTabelaVigente(tpTipoTabelaPreco, tpSubtipoTabelaPreco, blEfetivada, dtReferencia);
	}

	public TabelaPreco findTabelaVigente(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco) {
		return findTabelaVigente(tpTipoTabelaPreco, tpSubtipoTabelaPreco, Boolean.TRUE, JTDateTimeUtils.getDataAtual());
	}

	public TabelaPreco findTabelaXVigenteByServico(Long idServico) {
		return getTabelaPrecoDAO().findTabelaXVigenteByServico(idServico);
	}

	/**
	 * Busca Tabelas entre asversões informadas
	 * @param tpTipoTabelaPreco
	 * @param tpSubtipoTabelaPreco
	 * @param nrVersaoInicial
	 * @param nrVersaoFinal
	 * @return
	 */
	public List<TabelaPreco> findTabelaPrecoByNrVersao(String tpTipoTabelaPreco, Integer nrVersao, Long idTabelaPrecoOrigem) {
		return getTabelaPrecoDAO().findTabelaPrecoByNrVersao(tpTipoTabelaPreco, nrVersao, idTabelaPrecoOrigem);
	}

	/**
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	private void prepareTabelaPrecoParcelas(TabelaPreco tabelaPreco) {
		List<TabelaPrecoParcela> tabelaPrecoParcelas = tabelaPreco.getTabelaPrecoParcelas();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tpp : tabelaPrecoParcelas) {
				tpp.setIdTabelaPrecoParcela(null);
				tpp.setTabelaPreco(tabelaPreco);
			}
		}
	}

	private void prepareTabelaPrecoParcelasEdit(TabelaPreco tabelaPreco, List<TabelaPrecoParcela> tabelaPrecoParcelas) {
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tpp : tabelaPrecoParcelas) {
				tpp.setIdTabelaPrecoParcela(null);
				tpp.setTabelaPreco(tabelaPreco);
				tabelaPreco.getTabelaPrecoParcelas().add(tpp);
			}
		}
	}

	private TipoTabelaPreco storeTipoTabelaPreco(TipoTabelaPreco tipoTabelaBase, Integer nrVersao) {
		Long idCliente = null;
		if (tipoTabelaBase.getCliente() != null) {
			idCliente = tipoTabelaBase.getCliente().getIdCliente();
		}
		TipoTabelaPreco found = tipoTabelaPrecoService.findByTipoTabelaBase(
				tipoTabelaBase.getTpTipoTabelaPreco().getValue(),
				nrVersao,
				tipoTabelaBase.getEmpresaByIdEmpresaCadastrada().getIdEmpresa(),
				tipoTabelaBase.getServico().getIdServico(),
				idCliente);

		if(found != null) {
			return found;
		}

		TipoTabelaPreco tipoTabelaPreco = new TipoTabelaPreco();

		tipoTabelaPreco.setDsIdentificacao(tipoTabelaBase.getDsIdentificacao());
		tipoTabelaPreco.setTpTipoTabelaPreco(tipoTabelaBase.getTpTipoTabelaPreco());
		tipoTabelaPreco.setTpSituacao(new DomainValue("A"));
		tipoTabelaPreco.setEmpresaByIdEmpresaCadastrada(tipoTabelaBase.getEmpresaByIdEmpresaCadastrada());
		tipoTabelaPreco.setEmpresaByIdEmpresaLogada(tipoTabelaBase.getEmpresaByIdEmpresaLogada());
		tipoTabelaPreco.setCliente(tipoTabelaBase.getCliente());
		tipoTabelaPreco.setServico(tipoTabelaBase.getServico());
		tipoTabelaPreco.setNrVersao(nrVersao);

		tipoTabelaPrecoService.store(tipoTabelaPreco);
		return tipoTabelaPreco;
	}

	private TarifaPrecoRota copyTarifaPrecoRota(TarifaPrecoRota original, TabelaPreco tabelaPreco) {
		TarifaPrecoRota result = new TarifaPrecoRota();
		result.setRotaPreco(original.getRotaPreco());
		result.setTabelaPreco(tabelaPreco);
		result.setTarifaPreco(original.getTarifaPreco());
		return result;
	}

	private List<Long> getParcelasPreco(List<TabelaPrecoParcela> tabelaPrecoParcelas) {
		List<Long> result = new ArrayList<Long>();
		if (tabelaPrecoParcelas != null) {
			for(TabelaPrecoParcela tpp : tabelaPrecoParcelas) {
				result.add(tpp.getParcelaPreco().getIdParcelaPreco());
			}
		}
		return result;
	}

	private TabelaPrecoParcela executeCopiaTabelaPrecoParcela(TabelaPrecoParcela original, TabelaPreco tabelaPreco) {
		TabelaPrecoParcela tpp = new TabelaPrecoParcela();
		tpp.setFaixaProgressivas(executeCopiaFaixasProgressivas(original.getFaixaProgressivas(), tpp));
		tpp.setGeneralidade(executeCopiaGeneralidade(original.getGeneralidade(), tpp));
		tpp.setParcelaPreco(original.getParcelaPreco());
		tpp.setPrecoFretes(executeCopiaPrecoFretes(original.getPrecoFretes(), tpp));
		tpp.setTabelaPreco(tabelaPreco);
		tpp.setValorServicoAdicional(executeCopiaValorServicoAdicional(original.getValorServicoAdicional(), tpp));
		tpp.setValorTaxa(executeCopiaValorTaxa(original.getValorTaxa(), tpp));
		return tpp;
	}

	private Generalidade executeCopiaGeneralidade(Generalidade original, TabelaPrecoParcela tabelaPrecoParcela) {
		if (original != null) {
			Generalidade result = new Generalidade();
			result.setTabelaPrecoParcela(tabelaPrecoParcela);
			result.setVlGeneralidade(original.getVlGeneralidade());
			result.setVlMinimo(original.getVlMinimo());
			return result;
		}
		return null;
	}

	private ValorServicoAdicional executeCopiaValorServicoAdicional(ValorServicoAdicional original, TabelaPrecoParcela tabelaPrecoParcela) {
		if (original != null) {
			ValorServicoAdicional result = new ValorServicoAdicional();
			result.setTabelaPrecoParcela(tabelaPrecoParcela);
			result.setVlMinimo(original.getVlMinimo());
			result.setVlServico(original.getVlServico());
			return result;
		}
		return null;
	}


	private ValorTaxa executeCopiaValorTaxa(ValorTaxa original, TabelaPrecoParcela tabelaPrecoParcela) {
		if (original != null) {
			ValorTaxa result = new ValorTaxa();
			result.setPsTaxado(original.getPsTaxado());
			result.setTabelaPrecoParcela(tabelaPrecoParcela);
			result.setVlExcedente(original.getVlExcedente());
			result.setVlTaxa(original.getVlTaxa());
			return result;
		}
		return null;
	}

	private List<PrecoFrete> executeCopiaPrecoFretes(List<PrecoFrete> originais, TabelaPrecoParcela tabelaPrecoParcela) {
		if (originais != null && originais.size() > 0) {
			List<PrecoFrete> result = new ArrayList<PrecoFrete>();
			for(PrecoFrete original : originais) {
				result.add(executeCopiaPrecoFrete(original, tabelaPrecoParcela));
			}
			return result;
		}
		return null;
	}

	private PrecoFrete executeCopiaPrecoFrete(PrecoFrete original, TabelaPrecoParcela tabelaPrecoParcela) {
		if (original != null) {
			PrecoFrete result = new PrecoFrete();
			result.setRotaPreco(original.getRotaPreco());
			result.setTabelaPrecoParcela(tabelaPrecoParcela);
			result.setTarifaPreco(original.getTarifaPreco());
			result.setVlPrecoFrete(original.getVlPrecoFrete());
			return result;
		}
		return null;
	}

	private List<FaixaProgressiva> executeCopiaFaixasProgressivas(List<FaixaProgressiva> originais, TabelaPrecoParcela tabelaPrecoParcela) {
		if (originais != null && originais.size() > 0) {
			List<FaixaProgressiva> result = new ArrayList<FaixaProgressiva>();
			for(FaixaProgressiva original : originais) {
				result.add(executeCopiaFaixaProgressiva(tabelaPrecoParcela, original));
			}
			return result;
		}
		return null;
	}

	private FaixaProgressiva executeCopiaFaixaProgressiva(TabelaPrecoParcela tabelaPrecoParcela, FaixaProgressiva original) {
		FaixaProgressiva result = new FaixaProgressiva();
		result.setCdMinimoProgressivo(original.getCdMinimoProgressivo());
		result.setProdutoEspecifico(original.getProdutoEspecifico());
		result.setTabelaPrecoParcela(tabelaPrecoParcela);
		result.setTpIndicadorCalculo(original.getTpIndicadorCalculo());
		result.setUnidadeMedida(original.getUnidadeMedida());
		result.setVersao(original.getVersao());
		result.setVlFaixaProgressiva(original.getVlFaixaProgressiva());
		result.setTpSituacao(original.getTpSituacao());

		List<ValorFaixaProgressiva> vfps = original.getValoresFaixasProgressivas();
		if (vfps != null && vfps.size() > 0) {
			List<ValorFaixaProgressiva> valores = new ArrayList<ValorFaixaProgressiva>();
			for(ValorFaixaProgressiva vfp : vfps) {
				ValorFaixaProgressiva newVfp = copyValorFaixaProgressiva(result, vfp);
				valores.add(newVfp);
			}
			result.setValoresFaixasProgressivas(valores);
		}

		return result;
	}

	private ValorFaixaProgressiva copyValorFaixaProgressiva(FaixaProgressiva faixaProgressiva, ValorFaixaProgressiva original) {
		ValorFaixaProgressiva result = new ValorFaixaProgressiva();
		result.setBlPromocional(original.getBlPromocional());
		result.setDtVigenciaPromocaoFinal(original.getDtVigenciaPromocaoFinal());
		result.setDtVigenciaPromocaoInicial(original.getDtVigenciaPromocaoInicial());
		result.setFaixaProgressiva(faixaProgressiva);
		result.setNrFatorMultiplicacao(original.getNrFatorMultiplicacao());
		result.setPcDesconto(original.getPcDesconto());
		result.setPcTaxa(original.getPcTaxa());
		result.setRotaPreco(original.getRotaPreco());
		result.setTarifaPreco(original.getTarifaPreco());
		result.setVersao(original.getVersao());
		result.setVlAcrescimo(original.getVlAcrescimo());
		result.setVlFixo(original.getVlFixo());
		return result;
	}

	public Long generatePendenciaWorkflow(TypedFlatMap parameters) {
		String dsMotivoSolicitacao = parameters.getString("dsMotivoSolicitacao");
		Long idFilialSolicitacao = parameters.getLong("idFilialSolicitacao");
		Long idTabelaPreco = parameters.getLong("idTabelaPreco");
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);

		Short nrTipoEvento = ConstantesWorkflow.NR137_APROVACAO_TABELA_PRECO;
		Long idProcesso = tabelaPreco.getIdTabelaPreco();
		DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
		String dsProcesso = this.generateDsProcessoWorkflow(tabelaPreco, dsMotivoSolicitacao);

		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilialSolicitacao);
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(idProcesso);
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(dhLiberacao);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.TABELA_PRECO);
		pendenciaHistoricoDTO.setDsObservacao(dsMotivoSolicitacao);

		// LMS - 7550
		TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
		Servico servico = tipoTabelaPreco.getServico();
		String tpModal = servico.getTpModal().getValue();

		Pendencia pendencia = null;

		if("A".equals(tpModal)){
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TBPA);
			pendencia = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		}else{
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TBPR);
			pendenciaHistoricoDTO.setNrTipoEvento(SOLICITAR_APROVACAO_MODAL_RODOVIARIO);
			pendencia = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		}

		tabelaPreco.setPendencia(pendencia);
		store(tabelaPreco);

		return pendencia.getIdPendencia();
	}

	private String generateDsProcessoWorkflow(TabelaPreco tabelaPreco, String dsMotivoSolicitacao) {
		String dsTabelaPreco = tabelaPreco.getTabelaPrecoString();
		return configuracoesFacade.getMensagem("LMS-30065", new Object[] {dsTabelaPreco, dsMotivoSolicitacao});
	}

	public TabelaPreco generateWorkflowEfetivacao(TabelaPreco tabelaPreco, TypedFlatMap parameters) {
		String dsMotivoSolicitacao = parameters.getString("dsMotivoSolicitacao");
		Long idFilialSolicitacao = parameters.getLong("idFilialSolicitacao");

		Short nrTipoEvento = ConstantesWorkflow.NR143_APROVACAO_EFETIVACAO_TABELA_PRECO;
		Long idProcesso = tabelaPreco.getIdTabelaPreco();
		DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
		String dsProcesso = this.generateDsProcessoEfetivacao(tabelaPreco, dsMotivoSolicitacao);

		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilialSolicitacao);
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(idProcesso);
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(dhLiberacao);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.TABELA_PRECO);
		pendenciaHistoricoDTO.setDsObservacao(dsMotivoSolicitacao);

		//LMS - 7550
		TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
		Servico servico = tipoTabelaPreco.getServico();
		String tpModal = servico.getTpModal().getValue();

		Pendencia pendenciaEfetivacao = null;

		if("A".equals(tpModal)){
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TEFA);
			pendenciaEfetivacao = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		}else{
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TEFE);
			pendenciaHistoricoDTO.setNrTipoEvento(SOLICITAR_EFETIVACAO_MODAL_RODOVIARIO);
			pendenciaEfetivacao = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		}

		tabelaPreco.setPendenciaEfetivacao(pendenciaEfetivacao);
		store(tabelaPreco);

		return tabelaPreco;
	}

	public TabelaPreco executeAtualizarVigenciasEGerarWK(TypedFlatMap parameters) {
		Long idTabelaPreco = parameters.getLong("idTabelaPreco");
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);

		tabelaPreco.setDtVigenciaInicial(parameters.getYearMonthDay("dtVigenciaInicial"));

		if (parameters.containsKey("dtVigenciaFinal")) {
			tabelaPreco.setDtVigenciaFinal(parameters.getYearMonthDay("dtVigenciaFinal"));
		}

		return generateWorkflowEfetivacao(tabelaPreco, parameters);
	}
	private String generateDsProcessoEfetivacao(TabelaPreco tabelaPreco, String dsMotivoSolicitacao) {
		String dsTabelaPreco = tabelaPreco.getTabelaPrecoString();
		return configuracoesFacade.getMensagem("LMS-30069", new Object[] {
				dsTabelaPreco,
				dsMotivoSolicitacao});
	}

	public TabelaPreco generateWorkflowDesefetivacao(TypedFlatMap parameters) {
		String dsMotivoSolicitacao = parameters.getString("dsMotivoSolicitacao");
		Long idFilialSolicitacao = parameters.getLong("idFilialSolicitacao");
		Long idTabelaPreco = parameters.getLong("idTabelaPreco");
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);

		Short nrTipoEvento = ConstantesWorkflow.NR142_APROVACAO_DESEFETIVACAO_TABELA_PRECO;
		Long idProcesso = tabelaPreco.getIdTabelaPreco();
		DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
		String dsProcesso = this.generateDsProcessoDesefetivacao(tabelaPreco, dsMotivoSolicitacao);

		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilialSolicitacao);
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(idProcesso);
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(dhLiberacao);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.TABELA_PRECO);
		pendenciaHistoricoDTO.setDsObservacao(dsMotivoSolicitacao);

		// LMS - 7550
		TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
		Servico servico = tipoTabelaPreco.getServico();
		String tpModal = servico.getTpModal().getValue();

		Pendencia pendenciaDesefetivacao = null;

		if("A".equals(tpModal)){
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TDEA);
			pendenciaDesefetivacao = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);

		}else{
			pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.TDES);
			pendenciaHistoricoDTO.setNrTipoEvento(DESEFETIVACAO_MODAL_RODOVIARIO);
			pendenciaDesefetivacao = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		}

		tabelaPreco.setPendenciaDesefetivacao(pendenciaDesefetivacao);
		getTabelaPrecoDAO().getAdsmHibernateTemplate().saveOrUpdate(tabelaPreco);

		return tabelaPreco;
	}

	private String generateDsProcessoDesefetivacao(TabelaPreco tabelaPreco, String dsMotivoSolicitacao) {
		String dsTabelaPreco = tabelaPreco.getTabelaPrecoString();
		return configuracoesFacade.getMensagem("LMS-30068", new Object[] {
				dsTabelaPreco,
				dsMotivoSolicitacao});
	}

	public void executeDesefetivacao(Long idTabelaPreco) {
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);
		tabelaPreco.setBlEfetivada(false);
		tabelaPreco.setPendenciaDesefetivacao(null);
		getTabelaPrecoDAO().getAdsmHibernateTemplate().saveOrUpdate(tabelaPreco);
	}

	public void executeEfetivacao(Long idTabelaPreco) {
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);
		this.atualizarDataVigenciaTabela(tabelaPreco);

		tabelaPreco.setBlEfetivada(true);
		tabelaPreco.setPendencia(null);
		tabelaPreco.setPendenciaEfetivacao(null);
		tabelaPreco.setPendenciaDesefetivacao(null);
		getTabelaPrecoDAO().getAdsmHibernateTemplate().saveOrUpdate(tabelaPreco);
	}

	private void atualizarDataVigenciaTabela(TabelaPreco tabelaPreco) {
		Map<String,Object> reajuste = this.reajusteTabelaPrecoService.findReajusteByIdTabelaNova(tabelaPreco.getIdTabelaPreco());

		if (reajuste == null || !reajuste.containsKey("idTabelaBase") || !((boolean) reajuste.get("fechaVigenciaTabelaBase"))) {
			return;
		}

		getTabelaPrecoDAO().updateVigenciaFinalTabelaPreco(
		  new Long(reajuste.get("idTabelaBase").toString()),
		  tabelaPreco.getDtVigenciaInicial().minusDays(1)
		);
	}

	public void executeReiniciarAlteracao(Long idTabelaPreco) {
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);
		tabelaPreco.setPendencia(null);
		getTabelaPrecoDAO().store(tabelaPreco);
	}

	public DadosTabelaPrecoDTO findFirstTabelaEfetivadaByIdPipelineCliente(PipelineCliente pipelineCliente) {
		return getTabelaPrecoDAO().findFirstTabelaEfetivadaByIdPipelineCliente(pipelineCliente);
	}

	public Integer getRowCountForManterGruposRegioes(Map criteria) {
		return getTabelaPrecoDAO().getRowCountForManterGruposRegioes(criteria);
	}

	public Boolean isEfetivado(Long idTabelaPreco) {
		return getTabelaPrecoDAO().isEfetivado(idTabelaPreco);
	}

	public ResultSetPage<TabelaPrecoAnexo> findPaginatedTabelaPrecoAnexo(PaginatedQuery paginatedQuery) {
		return getTabelaPrecoDAO().findPaginatedTabelaPrecoAnexo(paginatedQuery);
	}

	public Integer getRowCountTabelaPrecoAnexo(TypedFlatMap criteria) {
		return getTabelaPrecoDAO().getRowCountTabelaPrecoAnexo(criteria);
	}

	public TabelaPrecoAnexo storeTabelaPrecoAnexo(TypedFlatMap map){
		TabelaPrecoAnexo tabelaPrecoAnexo = new TabelaPrecoAnexo();
		tabelaPrecoAnexo.setIdTabelaPrecoAnexo(map.getLong("idTabelaPrecoAnexo"));

		TabelaPreco tabelaPreco = new TabelaPreco();
    	tabelaPreco.setIdTabelaPreco(map.getLong("idTabelaPreco"));
    	tabelaPrecoAnexo.setTabelaPreco(tabelaPreco);

    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	tabelaPrecoAnexo.setUsuario(usuarioLMS);

    	try{
    		tabelaPrecoAnexo.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}

    	tabelaPrecoAnexo.setDsAnexo(map.getString("dsAnexo"));
    	tabelaPrecoAnexo.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());

    	getTabelaPrecoDAO().store(tabelaPrecoAnexo);

    	return tabelaPrecoAnexo;
	}

	public TabelaPrecoAnexo findTabelaPrecoAnexoById(Long idTabelaPrecoAnexo) {
		TabelaPrecoAnexo tabelaPrecoAnexo = getTabelaPrecoDAO().findTabelaPrecoAnexoById(idTabelaPrecoAnexo);
		if(tabelaPrecoAnexo != null){
			Hibernate.initialize(tabelaPrecoAnexo);
		}
		return tabelaPrecoAnexo;
	}

	public void findPossuiTarifaRota(Long idTabelaPreco) {
		TabelaPreco tabelaPreco = findByIdTabelaPrecoReajuste(idTabelaPreco);

		if(!"A".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
				&& !"C".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())
					&& !"T".equals(tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue())){
			Boolean possuiTabelaPreco = CollectionUtils.isNotEmpty(getTarifaPrecoRotaService().findByIdTabelaPreco(idTabelaPreco));
			if (BooleanUtils.isFalse(possuiTabelaPreco)) {
				throw new BusinessException("LMS-30070", new Object[] { tabelaPreco.getTabelaPrecoString() });
			}
		}

		this.validateVigencia(tabelaPreco);
	}

	public void removeByIdsTabelaPrecoAnexo(List ids) {
		/*
		 * Valida somente o primeiro item, pois todos possuem a mesma Tabela de Preço.
		 */
		TabelaPrecoAnexo tabelaPrecoAnexo = this.findTabelaPrecoAnexoById((Long) ids.get(0));
		validatePendenciaWorkflow(tabelaPrecoAnexo.getTabelaPreco());
		getTabelaPrecoDAO().removeByIdsTabelaPrecoAnexo(ids);
	}

	public void cancelWorkflow(Long idTabelaPreco) {
		getHistoricoWorkflowService().cancelWorkflow(idTabelaPreco, TabelaHistoricoWorkflow.TABELA_PRECO);
	}

	public void cancelPendencia(Long idTabelaPreco){
		getTabelaPrecoDAO().cancelPendencia(idTabelaPreco);
	}


	public void cancelPendenciaDesefetivacao(Long idTabelaPreco) {
		getTabelaPrecoDAO().cancelPendenciaDesefetivacao(idTabelaPreco);
	}

	public void cancelPendenciaEfetivacao(Long idTabelaPreco) {
		getTabelaPrecoDAO().cancelPendenciaEfetivacao(idTabelaPreco);
	}

	public ResultSetPage<TabelaPreco> findPaginated(PaginatedQuery paginatedQuery) {
		return getTabelaPrecoDAO().findPaginated(paginatedQuery);
	}

	public List findTabelasInPipelineClienteSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		return getTabelaPrecoDAO().findTabelasInPipelineClienteSimulacaoByPipelineCliente(pipelineCliente);
	}

	public TabelaPreco findTabelaCliente(Long idDivisaoCliente, Long idServico) {
		return getTabelaPrecoDAO().findTabelaCliente(idDivisaoCliente, idServico);
	}

	public TabelaPreco findTabelaPrecoByIdDivisaoCliente(Long idDivisaoCliente, Long idServico) {
		return getTabelaPrecoDAO().findTabelaPrecoByIdDivisaoCliente(idDivisaoCliente, idServico);

	}

	public TabelaPreco findTabelaSimulacao(Long idSimulacao) {
		return getTabelaPrecoDAO().findTabelaSimulacao(idSimulacao);
	}

	public void setTarifaPrecoRotaService(TarifaPrecoRotaService tarifaPrecoRotaService) {
		this.tarifaPrecoRotaService = tarifaPrecoRotaService;
	}
	public void setCalculoSimulacaoNovosPrecosService(CalculoSimulacaoNovosPrecosService calculoSimulacaoNovosPrecosService) {
		this.calculoSimulacaoNovosPrecosService = calculoSimulacaoNovosPrecosService;
	}
	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	public void setTipoTabelaPrecoService(TipoTabelaPrecoService tipoTabelaPrecoService) {
		this.tipoTabelaPrecoService = tipoTabelaPrecoService;
	}
	public void setSubtipoTabelaPrecoService(SubtipoTabelaPrecoService subtipoTabelaPrecoService) {
		this.subtipoTabelaPrecoService = subtipoTabelaPrecoService;
	}
	public void setCalculoNovosPrecosCiasAereasService(CalculoNovosPrecosCiasAereasService calculoNovosPrecosCiasAereasService) {
		this.calculoNovosPrecosCiasAereasService = calculoNovosPrecosCiasAereasService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setReajusteTabelaClienteService(ReajusteTabelaClienteService reajusteTabelaClienteService) {
		this.reajusteTabelaClienteService = reajusteTabelaClienteService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public boolean validaAtualizacaoAutomatica(TabelaPreco tabelaPreco){
		if (tabelaPreco == null) {
			return false;
		}
		if (tabelaPreco.getTpCategoria() == null || tabelaPreco.getTpServico() == null) {
			return false;
		}
		String atualizacaoAutomatica = (String)parametroGeralService.findConteudoByNomeParametro("ATUALIZACAO_AUTOMATICA_TABELA_PRECO", false);
		boolean blAtualizacaoAutomatica = "S".equals(atualizacaoAutomatica);
		String tpCategoria = tabelaPreco.getTpCategoria().getValue();
		String tpServico = tabelaPreco.getTpServico().getValue();

		if(blAtualizacaoAutomatica && "A".equals(tpCategoria) && StringUtils.isNotBlank(tpServico)) {
			return true;
		}
		return false;
	}

	public String atualizaTabelaCustos(String tpServico) {
		return this.atualizaTabelaCustoDao.atualizaTabelaCustosTNT(tpServico);
	}

	public AtualizaTabelaCustoTNTDAO getAtualizaTabelaCustoDao() {
		return atualizaTabelaCustoDao;
	}

	public void setAtualizaTabelaCustoDao(AtualizaTabelaCustoTNTDAO atualizaTabelaCustoDao) {
		this.atualizaTabelaCustoDao = atualizaTabelaCustoDao;
	}

	@Override
	public Serializable store(TabelaPreco bean) {
        return super.store(bean);
    }

	public List<ParcelaPreco> findParcelasByTabelaPreco(Long idTabelaPreco){
		return getTabelaPrecoDAO().findParcelasByTabelaPreco(idTabelaPreco);
	}

	public String findAtualizaTabelaCustos(Long idTabelaPrecoParcela) {
		String msgAtualizacaoAutomatica = null;
		if(idTabelaPrecoParcela != null){
			TabelaPrecoParcela tabelaPrecoParcela = tabelaPrecoParcelaService.findById(idTabelaPrecoParcela);
			TabelaPreco tabelaPreco = this.findByIdTabelaPrecoReajuste(tabelaPrecoParcela.getTabelaPreco().getIdTabelaPreco());
			if(CollectionUtils.isNotEmpty(this.findParcelasByTabelaPreco(tabelaPreco.getIdTabelaPreco()))
					&& this.validaAtualizacaoAutomatica(tabelaPreco)){
				msgAtualizacaoAutomatica = this.atualizaTabelaCustos(tabelaPreco.getTpServico().getValue());
			}
		}
		return msgAtualizacaoAutomatica;
	}

	public String findRelatorioAlteracoesTarifas(TypedFlatMap criteria) {
		List<Map<String, Object>> listForCSV = this.getTabelaPrecoDAO().findRelatorioAlteracoesTarifas(criteria);
		return reportExecutionManager.generateReportLocator(listForCSV, Boolean.TRUE);
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}

	public List<TabelaPreco> findTabelaPrecoSuggest(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Integer nrVersao, DomainValue tpModal,boolean apenasVigentes) {
		return getTabelaPrecoDAO().findTabelaPrecoSuggest(tpTipoTabelaPreco, tpSubtipoTabelaPreco, nrVersao, tpModal, apenasVigentes);
	}

	public List<ParcelaPreco> findByIdTabelaPrecoTipoPrecificacao(Long idTabelaPreco, List<String> tiposPrecificacao) {
		return getTabelaPrecoDAO().findByIdTabelaPrecoTipoPrecificacao(idTabelaPreco, tiposPrecificacao);
	}
	
	public TarifaPrecoRotaService getTarifaPrecoRotaService() {
		return tarifaPrecoRotaService;
	}

	public void setObservacaoService(ObservacaoService observacaoService) {
		this.observacaoService = observacaoService;
	}

	public void setReajusteTabelaPrecoService(ReajusteTabelaPrecoService reajusteTabelaPrecoService) {
		this.reajusteTabelaPrecoService = reajusteTabelaPrecoService;
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
}

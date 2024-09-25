package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.tabelaprecos.model.AtualizarReajusteDTO;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.ParametroReajusteTabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteParametroClienteService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ReajusteCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.dao.DivisaoClienteDAO;
import com.mercurio.lms.vendas.model.dao.GeneralidadeClienteDAO;
import com.mercurio.lms.vendas.model.dao.ParametroClienteDAO;
import com.mercurio.lms.vendas.model.dao.ReajusteClienteDAO;
import com.mercurio.lms.vendas.model.dao.ServicoAdicionalClienteDAO;
import com.mercurio.lms.vendas.model.dao.TaxaClienteDAO;
import com.mercurio.lms.vendas.util.SimulacaoUtils;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.reajusteClienteService"
 */
public class ReajusteClienteService extends CrudService<ReajusteCliente, Long> {

	private static final int FIRST = 0;
	private static final String ATIVO = "A";
	private static final String AUTOMATICO = "A";
	private static final String REAJUSTE_CLIENTE_MANUAL = "R";
	private WorkflowPendenciaService workflowPendenciaService;
	private TabelaPrecoService tabelaPrecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private AcaoService acaoService;
	private PerfilUsuarioService perfilUsuarioService;
	private DivisaoClienteDAO divisaoClienteDAO;
	private PessoaDAO pessoaDAO;
	private TabelaPrecoDAO tabelaPrecoDAO;
	private ReajusteParametroClienteService reajusteParametroClienteService;
	private ParametroClienteDAO parametroClienteDAO;
	private ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO;
	private ServicoAdicionalClienteDAO servicoAdicionalClienteDAO;
	private GeneralidadeClienteDAO generalidadeClienteDAO;
	private TaxaClienteDAO taxaClienteDAO;
	private BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
	private UsuarioService usuarioService;
	private VolDadosSessaoService volDadosSessaoService;
	
	/**
	 * Recupera uma instância de <code>Simulacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public ReajusteCliente findById(Long id) {
		return (ReajusteCliente)super.findById(id);
	}

	public TypedFlatMap findDadosById(Long idReajusteCliente) {
		return getReajusteClienteDAO().findDadosById(idReajusteCliente);
	}

	@Override
	public void removeById(Long id) {
		ReajusteCliente reajusteCliente = findById(id);
		/** Valida Exclusão */
		if (Boolean.TRUE.equals(reajusteCliente.getBlEfetivado())) {
			throw new BusinessException("LMS-01036");
		}

		if(reajusteCliente.getPendenciaAprovacao() != null) {
			reajusteCliente.getPendenciaAprovacao().setTpSituacaoPendencia(new DomainValue("E"));
			reajusteCliente.getPendenciaAprovacao().getOcorrencia().setTpSituacaoOcorrencia(new DomainValue("P"));
			workflowPendenciaService.cancelPendencia(reajusteCliente.getPendenciaAprovacao());
		}
		super.removeById(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(ReajusteCliente bean) {
		return super.store(bean);
	}

	public void storeReajusteCliente(ReajusteCliente reajusteCliente) {
		/** Valida para que VigenciaInicial da Tabela Nova não seja menor que InicioVigencia do ReajusteCliente */
		TabelaPreco tabelaPreco = tabelaPrecoService.findByIdTabelaPreco(reajusteCliente.getTabelaPreco().getIdTabelaPreco());
		if(reajusteCliente.getDtInicioVigencia().isBefore(tabelaPreco.getDtVigenciaInicial())) {
			throw new BusinessException("LMS-01037");
		}

		if(reajusteCliente.getIdReajusteCliente() == null) {
			Long nrReajuste = configuracoesFacade.incrementaParametroSequencial(reajusteCliente.getFilial().getIdFilial(), "NR_REAJUSTE", true);
			reajusteCliente.setNrReajuste(nrReajuste);
		}
		store(reajusteCliente);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getReajusteClienteDAO().findPaginated(criteria, def);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getReajusteClienteDAO().getRowCount(criteria);
	}
	
	public String executeWorkflow(List<Long> idsReajusteCliente, List<String> tpSituacoesWorkflow) {
		if (idsReajusteCliente != null && tpSituacoesWorkflow != null) {
			if (idsReajusteCliente.size() != tpSituacoesWorkflow.size()) {
				return configuracoesFacade.getMensagem("LMS-01110");
			}
			for(int i = 0; i < idsReajusteCliente.size(); i++) {
				Long idReajusteCliente = idsReajusteCliente.get(i);
				String tpSituacaoAprovacao = tpSituacoesWorkflow.get(i);
				ReajusteCliente reajusteCliente = findById(idReajusteCliente);
				reajusteCliente.setTpSituacaoAprovacao(new DomainValue(tpSituacaoAprovacao));
				if (ATIVO.equals(tpSituacaoAprovacao)) {
					List<Acao> acoes = acaoService.findByIdPendenciaTpSituacaoAcao(reajusteCliente.getPendenciaAprovacao().getIdPendencia(), ATIVO);
					if (acoes != null && !acoes.isEmpty()) {
						Acao acao = acoes.get(0);
						reajusteCliente.setUsuarioByIdUsuarioAprovou(acao.getUsuario());
						reajusteCliente.setDtAprovacao(acao.getDhAcao().toYearMonthDay());
					}
				} else {
					reajusteCliente.setUsuarioByIdUsuarioAprovou(null);
					reajusteCliente.setDtAprovacao(null);
				}
				store(reajusteCliente);
			}
		}
		return null;
	}
	
	private void storeWorkflow(ReajusteCliente reajusteCliente){
		if(parametroClienteDAO.existPercentualMenorDoAcordado(reajusteCliente.getIdReajusteCliente()) > 0) {
			this.generatePendencia(reajusteCliente);
		} else {
			reajusteCliente.setPendenciaAprovacao(null);
			reajusteCliente.setTpSituacaoAprovacao(new DomainValue(ATIVO));
			reajusteCliente.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
			reajusteCliente.setDtAprovacao(JTDateTimeUtils.getDataAtual());
			store(reajusteCliente);
		} 
	}

	/**
	 * Aprova/Gera Pendencia sobre o Reajuste
	 * @param reajusteCliente
	 */
	public void storePendenciaAprovacao(ReajusteCliente reajusteCliente) {
		/** Se o campo % de reajuste tratado estiver maior ou igual
		 *  ao % de reajuste sugerido atualizar os campos da tabela */
		if(CompareUtils.ge(reajusteCliente.getPcReajusteAcordado(), reajusteCliente.getPcReajusteSugerido())) {
			if(Boolean.TRUE.equals(generatePendenciaCamposDefault(reajusteCliente))) {
				reajusteCliente.setPendenciaAprovacao(null);
				reajusteCliente.setTpSituacaoAprovacao(new DomainValue(ATIVO));
				reajusteCliente.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
				reajusteCliente.setDtAprovacao(JTDateTimeUtils.getDataAtual());
				store(reajusteCliente);
			}
		} else {
			/** Atualizar a pendência de aprovação */
			this.generatePendencia(reajusteCliente);
		}
	}

	public Boolean generatePendenciaCamposDefault(ReajusteCliente reajusteCliente) {
		/*
		 * Caso um ou mais dos campos default estejam desmarcados e ainda não
		 * tenha sido atualizada pendência de aprovação referente a esta
		 * proposta, atualizar a pendência de aprovação da proposta conforme
		 * abaixo:
		 * 
		 * Se o % de reajuste tratado for negativo então variável TP_REAJUSTE =
		 * “retorno de reajuste”, senão TP_REAJUSTE = “reajuste”. Esta variável
		 * será utilizada abaixo no campo dsProcesso.
		 * WorkFlowPendenciaService.generatePendencia ( Long idFilial = <ID da
		 * filial do usuário logado>, Short nrTipoEvento = <102> Long idProcesso
		 * = <REAJUSTE.ID_REAJUSTE>, String dsProcesso = <“Aprovação de ” +
		 * TP_REAJUSTE + “ de cliente número “ + FILIAL.SG_FILIAL (referente à
		 * REAJUSTE_CLIENTE.ID_FILIAL) + “ “ + REAJUSTE_CLIENTE.NR_REAJUSTE
		 * (formatado 6 algarismos) + “.”>; DateTime dhLiberacao = <Data/hora
		 * atual para a filial do usuário>)
		 * 
		 * Atualizar os seguintes campos na tabela REAJUSTE_CLIENTE:
		 * 
		 * ID_PENDENCIA_APROVACAO = <ID da pendência retornado da rotina>;
		 * TP_SITUACAO_APROVACAO = <Situação da pendência retornada da rotina>;
		 * ID_USUARIO_APROVOU = NULL; DT_APROVACAO = NULL.
		 * 
		 * Os campos default a ser tratados nesse item são: "% GRIS",
		 * "Ad valorem1", "Ad valorem2" e "Frete percentual".
		 */
		
		Boolean isMarcadoTodosCamposDefault = reajusteCliente.getBlReajustaPercGris()
				&& reajusteCliente.getBlReajustaAdValorEm()
				&& reajusteCliente.getBlReajustaAdValorEm2()
				&& reajusteCliente.getBlReajustaFretePercentual();
		
		if(!Boolean.TRUE.equals(isMarcadoTodosCamposDefault)) {
			generatePendencia(reajusteCliente);
		}		
		
		return isMarcadoTodosCamposDefault;
	}

	/**
	 * Gera Pendencia no WorkFlow
	 * @param reajusteCliente
	 */
	private void generatePendencia(ReajusteCliente reajusteCliente) {
		int nrTipoEvento = 102;
		String tpReajuste = reajusteCliente.getPcReajusteAcordado().compareTo(BigDecimal.ZERO) < 0 ? "retorno de reajuste" : "reajuste";
		String nrReajuste = SimulacaoUtils.formatNrSimulacao(reajusteCliente.getFilial().getSgFilial(), ""+reajusteCliente.getNrReajuste());
		Pendencia pendencia = workflowPendenciaService.generatePendencia(
				SessionUtils.getFilialSessao().getIdFilial(),
				(short) nrTipoEvento,
				reajusteCliente.getIdReajusteCliente(),
				configuracoesFacade.getMensagem("aprovacaoReajuste", new Object[]{tpReajuste,nrReajuste}),
				JTDateTimeUtils.getDataHoraAtual());

		reajusteCliente.setPendenciaAprovacao(pendencia);
		reajusteCliente.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
		reajusteCliente.setUsuarioByIdUsuarioAprovou(null);
		reajusteCliente.setDtAprovacao(null);
		store(reajusteCliente);
	}

	/**
	 * Método que será executado para verificar se o usuário tem permissão para
	 * liberar o embarque.
	 * @param usuario
	 *
	 * @return
	 */
	public Boolean validateUsuarioPerfilComercialParametrizacao(Usuario usuario) {
		List<PerfilUsuario> perfis = perfilUsuarioService.findByIdUsuarioPerfilUsuario(usuario.getIdUsuario());
		for (PerfilUsuario perfilUsuario : perfis) {
			if("Comercial LMS - Vendas Parametrização - MTZ".equals(perfilUsuario.getPerfil().getDsPerfil())){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	public String findNomeFantasiaCliente(String nrIdentificacao){
		if(nrIdentificacao == null){
			return "";
		}
		Pessoa p =  pessoaDAO.findByNrIdentificacao(nrIdentificacao);
		return p == null ? "" : p.getNmFantasia();
	}
	
	public List<Map<String,Object>> listDivisoesCliente(String nrIdentificacao){
		if(nrIdentificacao == null){
			return new ArrayList<>();
		}
		return divisaoClienteDAO.findDivisoesClienteByNrIdentificacaoAndTpSituacao(nrIdentificacao, ATIVO);
	}
	
	public String getTabelaBase(Long idDivisaoCliente){
		if(idDivisaoCliente == null){
			return "";
		}
		List<String> list = divisaoClienteDAO.findTabelaBaseByNrIdentificacaoOrDivisaoCliente(idDivisaoCliente, null);
		return CollectionUtils.isEmpty(list) ? "" : list.get(FIRST);
	}
	
	public BigDecimal getPercentualSugerido(Long idTabelaDivisaoCliente, Long idTabelaNova){
		return tabelaPrecoDAO.getPercentualSugerido(idTabelaDivisaoCliente, idTabelaNova);
	}
	
	public Long saveReajusteCliente(ReajusteCliente reajuste){
		Integer count = getReajusteClienteDAO().existsReajusteClienteByDataVigenciaInicial(reajuste.getIdReajusteCliente(), reajuste.getTabelaDivisaoCliente().getIdTabelaDivisaoCliente(), reajuste.getDtInicioVigencia());
		
		validateSave(reajuste, count);
		
		if(reajuste.getIdReajusteCliente() != null){ 
			getReajusteClienteDAO().updateReajustecliente(reajuste.getIdReajusteCliente(), reajuste.getPcReajusteAcordado(), reajuste.getDtInicioVigencia(), reajuste.getDsJustificativa());
		} else {
			executeReajuste(reajuste);
		}
		
		return reajuste.getIdReajusteCliente();
	}
	
	private boolean validateSave(ReajusteCliente reajuste, Integer count){
		if(count != null && count > 0){
			throw new BusinessException("reajusteExistente"); 
		}
		
		if(JTDateTimeUtils.getDataAtual().isEqual(reajuste.getDtInicioVigencia()) || JTDateTimeUtils.getDataAtual().isAfter(reajuste.getDtInicioVigencia())){
			throw new BusinessException("LMS-30040");
		}
		
		return Boolean.TRUE;
	}

	private void executeReajuste(ReajusteCliente reajuste) {
		try{
			getReajusteClienteDAO().store(reajuste);
			boolean statusClone    = reajusteParametroClienteService.executeClone(createCloneClienteAutomaticoDTO(reajuste.getTabelaDivisaoCliente().getIdTabelaDivisaoCliente(), reajuste.getDtInicioVigencia()));
			boolean statusReajuste = reajusteParametroClienteService.executeReajustarClienteAutomatico(createReajusteClienteAutomaticoDTO(reajuste.getTabelaDivisaoCliente().getIdTabelaDivisaoCliente()), false);
			executeAddServicoAdicionalCliente(reajuste.getIdReajusteCliente(), reajuste.getTabelaDivisaoCliente().getIdTabelaDivisaoCliente(), reajuste.getPcReajusteAcordado());
			if(!statusClone || !statusReajuste){
				throw new BusinessException("excecoesReajuste");
			}
		}catch(Exception e){
			throw new BusinessException("excecoesReajuste");
		}
	}
	
	public void executeAddServicoAdicionalCliente(Long idReajusteCliente, Long idTabelaDivisaoCliente, BigDecimal percentual){
		List<ServicoAdicionalCliente> list = servicoAdicionalClienteDAO.findByIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		for (ServicoAdicionalCliente servAdicional : list) {
			servicoAdicionalClienteDAO.insertServicoAdicionalReajusteCliente(idReajusteCliente, servAdicional.getIdServicoAdicionalCliente(), percentual, percentual, servAdicional.getVlMinimo(), servAdicional.getVlValor());
		}
	}
	
	public void removeReajusteCliente(Long idReajusteCliente, Long idTabDivisaoCliente, YearMonthDay dataVigenciaInicial){
		List<ParametroCliente> paramsCliente = parametroClienteDAO.findParamClienteByIdTabDivisaoClienteAndSituacaoAndVigInicial(idTabDivisaoCliente, new DomainValue(REAJUSTE_CLIENTE_MANUAL), dataVigenciaInicial);
		for (ParametroCliente parametroCliente : paramsCliente) {
			parametroClienteDAO.removeParamClienteAndAllDependecies(parametroCliente.getIdParametroCliente());
		}
		servicoAdicionalClienteDAO.removeServicoAdicionalReajusteCliente(idReajusteCliente);
		getReajusteClienteDAO().removeById(idReajusteCliente);
	}
	
	public boolean executeEfetivarReajuste(Long idReajusteCliente, Long idTabDivisaoCliente, YearMonthDay dataVigenciaInicial, BigDecimal percAcordado, String justificativa){
		ReajusteCliente reajusteCliente = findById(idReajusteCliente);
		validateEfetivar(reajusteCliente, dataVigenciaInicial, justificativa, percAcordado);
	 
		getReajusteClienteDAO().updateEfetivaReajusteCliente(idReajusteCliente, SessionUtils.getUsuarioLogado().getIdUsuario());
		executeEncerramentoParametros(idTabDivisaoCliente, dataVigenciaInicial); 
		executeAtivaParametros(idTabDivisaoCliente, dataVigenciaInicial);
		return Boolean.TRUE;
	}
	
	public boolean executeWorkflow(Long idReajusteCliente){
		ReajusteCliente reajusteCliente = findById(idReajusteCliente);
		storeWorkflow(reajusteCliente); 
		return Boolean.TRUE;
	}
	
	public boolean validateEfetivar(ReajusteCliente reajusteCliente, YearMonthDay dataVigenciaInicial, String justificativa, BigDecimal percAcordado){
		if(!reajusteCliente.getDtInicioVigencia().isEqual(dataVigenciaInicial) || !justificativa.equals(reajusteCliente.getDsJustificativa()) || percAcordado.compareTo(reajusteCliente.getPcReajusteAcordado()) != 0 ){
			throw new BusinessException("LMS-36244");
		}
		
		return Boolean.TRUE;
	}
	
	public boolean executeAsyncEfetivacao(){
		
		Usuario usuario = usuarioService.findUsuarioByLogin("Reajuste.Cliente");	
		volDadosSessaoService.setDadosSessaoBanco(usuario.getEmpresaPadrao().getIdEmpresa(), usuario.getIdUsuario(), usuario.getLogin(), "|to>-03|tl>0|i>0|");
	
		List<Map<String, Object>> list = getReajusteClienteDAO().findDadosReajustesEfetivados();
		if(CollectionUtils.isEmpty(list)){
			return Boolean.FALSE;
		}
			
		for (Map<String, Object> map : list) {
			AtualizarReajusteDTO dto = createAtualizarReajusteDTO(map);
			parametroReajusteTabelaPrecoDAO.updateTabelaDivisao(dto); 
			parametroReajusteTabelaPrecoDAO.insertHistoricoReajuste(dto, AUTOMATICO);
			updateServicoAdicionalCliente((Long) map.get("id"));
		}
		
		return Boolean.TRUE;
	}
	
	private void updateServicoAdicionalCliente(Long idReajusteCliente){
		List<Map<String, Object>> list = servicoAdicionalClienteDAO.findServicoAdicReajusteCliente(idReajusteCliente);
		for (Map<String, Object> map : list) {
			BigDecimal valor = map.get("valor") == null ? null : (BigDecimal)map.get("valor");
			BigDecimal valorMinimo = map.get("valorMinimo") == null ? null : (BigDecimal)map.get("valorMinimo");
			servicoAdicionalClienteDAO.updateValorAndValorMinById((Long)map.get("id"), valor, valorMinimo);
		}
	}
	
	private AtualizarReajusteDTO createAtualizarReajusteDTO(Map<String, Object> map){
		AtualizarReajusteDTO dto = new AtualizarReajusteDTO();
		dto.setIdTabelaBase((Long) map.get("tabBase"));
		dto.setIdTabelaDivisao((Long) map.get("idTabDivisaoCliente"));
		dto.setIdTabelaNova((Long) map.get("tabNova"));
		dto.setPcReajusteGeral((BigDecimal) map.get("percentual"));
		return dto;
	}
	
	private void executeAtivaParametros(Long idTabDivisaoCliente, YearMonthDay dataVigenciaInicial){
		List<ParametroCliente> paramsClienteNovos = parametroClienteDAO.findParamClienteByIdTabDivisaoClienteAndSituacaoAndVigInicial(idTabDivisaoCliente, new DomainValue(REAJUSTE_CLIENTE_MANUAL), dataVigenciaInicial);

		for (ParametroCliente parametroCliente : paramsClienteNovos) {
			parametroCliente.setTpSituacaoParametro(new DomainValue(ATIVO)); 
			parametroClienteDAO.store(parametroCliente);
		}
	}
	
	private void executeEncerramentoParametros(Long idReajusteCliente, YearMonthDay dataVigenciaInicial){
		List<ParametroCliente> paramsClienteAtivos = parametroClienteDAO.findByIdTabelaDivisaoCliente(idReajusteCliente, new DomainValue(ATIVO), JTDateTimeUtils.getDataAtual());
		for (ParametroCliente parametroCliente : paramsClienteAtivos) {
			parametroCliente.setDtVigenciaFinal(dataVigenciaInicial);
			parametroClienteDAO.storeEncerrandoParametro(parametroCliente, dataVigenciaInicial); 
		}
	}
	
	private CloneClienteAutomaticoDTO createCloneClienteAutomaticoDTO(Long idTabelaDivisaoCliente, YearMonthDay dataVigenciaInicial){
		CloneClienteAutomaticoDTO clone = new CloneClienteAutomaticoDTO();
		clone.setDataVigenciaInicial(dataVigenciaInicial);
		clone.setIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		clone.setTipo(REAJUSTE_CLIENTE_MANUAL);
		return clone;
	}
	
	private ReajusteClienteAutomaticoDTO createReajusteClienteAutomaticoDTO(Long idTabelaDivisaoCliente){
		ReajusteClienteAutomaticoDTO dto = new ReajusteClienteAutomaticoDTO();
		dto.setIdTabelaDivisaoCliente(idTabelaDivisaoCliente);
		dto.setTipo(REAJUSTE_CLIENTE_MANUAL);
		return dto;
	}
	
	public List<Map<String,Object>> findReajusteService(Long id, Long nrIdentificacao, Long idFilial, YearMonthDay dataReajuste){
		return getReajusteClienteDAO().findReajusteCliente(id, nrIdentificacao, idFilial, dataReajuste);
	}

	public List<Map<String,Object>> findByIdReajusteService(Long id){
		return findReajusteService(id, null, null, null);
	}
	
	public List<Map<String,Object>> listGeneralidadesObrigatorias(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		return parametroClienteDAO.listGeneralidadesObrigatorias(idTabDivisaoCliente, vigenciaInicial);
	}

	public List<Map<String,Object>> listGeneralidades(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		return parametroClienteDAO.listGeneralidades(idTabDivisaoCliente, vigenciaInicial);
	}
	
	public void saveGeneralidades(List<Map<String,Object>> listParams){
		for (Map<String, Object> map : listParams) {
			 generalidadeClienteDAO.updatePercValorAndPercValorMin(Long.valueOf(map.get("ID_GENERALIDADE_CLIENTE").toString()), 
					 											   getValue(map, "PERCENTUAL") , 
					 											   getValue(map, "MINIMO_PERCENTUAL") ,
					 											   getValue(map, "CALCULADO") , 
					 											   getValue(map, "MINIMO_CALCULADO") ); 
		}
	}
	
	public List<Map<String,Object>> listServAdicionalCliente(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial, Long idReajuste){
		return servicoAdicionalClienteDAO.listServicoAdicionalCliente(idTabDivisaoCliente, vigenciaInicial, idReajuste);
	}
	
	public List<Map<String,Object>> listTaxaCliente(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		return parametroClienteDAO.listTaxaCliente(idTabDivisaoCliente, vigenciaInicial);
	}
	
	public void saveTaxaCliente(List<Map<String,Object>> listParams){
		for (Map<String, Object> map : listParams) {
			taxaClienteDAO.updatePercValorTaxaAndPercValorExcedente(Long.valueOf(map.get("ID_TAXA_CLIENTE").toString()), 
					 											   getValue(map, "TAXA_CALCULADO") , 
					 											   getValue(map, "TAXA_PERCENTUAL") ,
					 											   getValue(map, "EXCEDENTE_CALCULADO") , 
					 											   getValue(map, "EXCEDENTE_PERCENTUAL") ); 
		}
	}
	
	public void saveServicoAdicionalCliente(List<Map<String,Object>> listParams){
		for (Map<String, Object> map : listParams) {
			servicoAdicionalClienteDAO.updatePercValorAndPercValorMinimo(Long.valueOf(map.get("ID_SERV_ADIC_CLIENTE").toString()), 
					 											   getValue(map, "CALCULADO") , 
					 											   getValue(map, "CALCULADO_MINIMO") ,
					 											   getValue(map, "PERCENTUAL") , 
					 											   getValue(map, "PERCENTUAL_MINIMO") ); 
		}
	}
	
	public List<Map<String,Object>> listFretePeso(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		return parametroClienteDAO.listFretePeso(idTabDivisaoCliente, vigenciaInicial);
	}
	
	public void saveGeneralidadesObrigatorias(List<Map<String,Object>> listParams){
		for (Map<String, Object> map : listParams) {
			ParametroCliente paramCliente = parametroClienteDAO.findParametroById(Long.valueOf(map.get("ID_PARAM_CLIENTE").toString()));
			paramCliente.setVlPedagio( getValue(map, "PEDAGIO_CALCULADO") );
			paramCliente.setVlPercentualGris( getValue(map, "GRIS_CALCULADO") );
			paramCliente.setVlMinimoGris( getValue(map, "GRIS_MIN_CALCULADO") );
			paramCliente.setVlMinimoTde( getValue(map, "TDE_MIN_CALCULADO")   );
			paramCliente.setVlPercentualTde( getValue(map, "TDE_CALCULADO")   );
			paramCliente.setVlMinimoTrt( getValue(map, "TRT_MIN_CALCULADO")   ); 
			paramCliente.setVlPercentualTrt( getValue(map, "TRT_CALCULADO")	  ); 
			paramCliente.setPcReajPedagio( getValue(map, "PEDAGIO_PERCENTUAL")	   ); 
			paramCliente.setPcReajPercentualGris( getValue(map, "GRIS_PERCENTUAL") );
			paramCliente.setPcReajMinimoGris( getValue(map, "GRIS_MIN_PERCENTUAL") ); 
			paramCliente.setPcReajMinimoTde( getValue(map, "TDE_MIN_PERCENTUAL") );
			paramCliente.setPcReajPercentualTDE( getValue(map, "TDE_PERCENTUAL") );
			paramCliente.setPcReajPercentualTRT( getValue(map, "TRT_PERCENTUAL") ); 
			paramCliente.setPcReajMinimoTrt( getValue(map, "TRT_MIN_PERCENTUAL") ); 
			parametroClienteDAO.store(paramCliente, true); 
		}
	}
	
	public void saveFretePeso(List<Map<String,Object>> listParams){
		for (Map<String, Object> map : listParams) {
			ParametroCliente paramCliente = parametroClienteDAO.findParametroById(Long.valueOf(map.get("ID_PARAM_CLIENTE").toString()));
			paramCliente.setVlAdvalorem( getValue(map, "ADVALOREN_CALCULADO") );
			paramCliente.setVlAdvalorem2( getValue(map, "ADVALOREN2_CALCULADO") );
			paramCliente.setVlToneladaFretePercentual( getValue(map, "TONELADA_FRETE_CALCULADO") );
			paramCliente.setVlFretePeso( getValue(map, "FRETE_PESO_CALCULADO")   );
			paramCliente.setVlFreteVolume( getValue(map, "FRETE_VOLUME_CALCULADO")   );
			paramCliente.setVlTarifaMinima( getValue(map, "TARIFA_MINIMA_CALCULADO")   ); 
			paramCliente.setVlTblEspecifica( getValue(map, "TARIFA_ESPECIFICA_CALCULADO")	  );
			paramCliente.setVlValorReferencia( getValue(map, "REFERENCIA_CALCULADO")	  );
			paramCliente.setVlMinimoFretePercentual( getValue(map, "MINIMO_FRETE_CALCULADO")	  );
			paramCliente.setVlMinimoFreteQuilo( getValue(map, "MINIMO_FRETE_QUILO_CALCULADO")	  );
			paramCliente.setVlMinFretePeso( getValue(map, "MINIMO_FRETE_PESO_CALCULADO")	  );
			paramCliente.setVlPercMinimoProgr(	getValue(map, "MINIMO_PROGRAMADO_CALCULADO")	  );	
					
			paramCliente.setPcReajAdvalorem( getValue(map, "ADVALOREN_PERCENTUAL")	   ); 
			paramCliente.setPcReajAdvalorem2( getValue(map, "ADVALOREN2_PERCENTUAL") );
			paramCliente.setPcReajVlToneladaFretePerc( getValue(map, "TONELADA_FRETE_PERCENTUAL") ); 
			paramCliente.setPcReajFretePeso( getValue(map, "FRETE_PESO_PERCENTUAL") );
			paramCliente.setPcReajVlFreteVolume( getValue(map, "FRETE_VOLUME_PERCENTUAL") );
			paramCliente.setPcReajTarifaMinima( getValue(map, "TARIFA_MINIMA_PERCENTUAL") ); 
			paramCliente.setPcReajVlTarifaEspecifica( getValue(map, "TARIFA_ESPECIFICA_PERCENTUAL") ); 
			paramCliente.setPcReajReferencia(  getValue(map, "REFERENCIA_PERCENTUAL") ); 
			paramCliente.setPcReajVlMinimoFretePercen( getValue(map, "MINIMO_FRETE_PERCENTUAL") ); 
			paramCliente.setPcReajVlMinimoFreteQuilo( getValue(map, "MINIMO_FRETE_QUILO_PERCENTUAL") ); 
			paramCliente.setPcReajVlMinimoFretePeso( getValue(map, "MINIMO_FRETE_PESO_PERCENTUAL")  );     
			paramCliente.setPcReajVlMinimoProg(  getValue(map, "MINIMO_PROGRAMADO_PERCENTUAL")  );      
			
			parametroClienteDAO.store(paramCliente, true); 
		}

	}
	
	
	private BigDecimal getValue(Map<String, Object> map, String key){
		if(map.get(key) == null){
			return BigDecimal.ZERO;
		}
		return (BigDecimal) bigDecimalConverter.convert(String.class, map.get(key).toString());
	}
	
	public Long findIdClienteByIdentificacao(String nrIdentificacao){
		Pessoa p = pessoaDAO.findByNrIdentificacao(nrIdentificacao);
		return p == null ? null : p.getIdPessoa();
	}
	
	public Long executeGetNrReajuste(Long idFilial){
		return getReajusteClienteDAO().getMaxNrReajuste(idFilial);
	}
	
	public void setReajusteClienteDAO(ReajusteClienteDAO dao) {
		setDao( dao );
	}
	private ReajusteClienteDAO getReajusteClienteDAO() {
		return (ReajusteClienteDAO) getDao();
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	public void setDivisaoClienteDAO(DivisaoClienteDAO divisaoClienteDAO) {
		this.divisaoClienteDAO = divisaoClienteDAO;
	}
	public void setPessoaDAO(PessoaDAO pessoaDAO) {
		this.pessoaDAO = pessoaDAO;
	}
	public void setTabelaPrecoDAO(TabelaPrecoDAO tabelaPrecoDAO) {
		this.tabelaPrecoDAO = tabelaPrecoDAO;
	}

	public void setReajusteParametroClienteService(ReajusteParametroClienteService reajusteParametroClienteService) {
		this.reajusteParametroClienteService = reajusteParametroClienteService;
	}
	public void setParametroClienteDAO(ParametroClienteDAO parametroClienteDAO) {
		this.parametroClienteDAO = parametroClienteDAO;
	}
	public void setServicoAdicionalClienteDAO(ServicoAdicionalClienteDAO servicoAdicionalClienteDAO) {
		this.servicoAdicionalClienteDAO = servicoAdicionalClienteDAO;
	}
	public void setParametroReajusteTabelaPrecoDAO(ParametroReajusteTabelaPrecoDAO parametroReajusteTabelaPrecoDAO) {
		this.parametroReajusteTabelaPrecoDAO = parametroReajusteTabelaPrecoDAO;
	}
	public void setGeneralidadeClienteDAO(GeneralidadeClienteDAO generalidadeClienteDAO) {
		this.generalidadeClienteDAO = generalidadeClienteDAO;
	}
	public void setTaxaClienteDAO(TaxaClienteDAO taxaClienteDAO) {
		this.taxaClienteDAO = taxaClienteDAO;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}



	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}
}

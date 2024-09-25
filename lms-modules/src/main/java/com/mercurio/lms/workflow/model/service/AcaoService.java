package com.mercurio.lms.workflow.model.service;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.mercurio.adsm.framework.model.*;
import com.mercurio.lms.tributos.dto.FluxoWorkFlowDto;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Integrante;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dao.AcaoDAO;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.acaoService"
 */
public class AcaoService extends CrudService<Acao, Long> {

	private ConfiguracoesFacade configuracoesFacade;

	private DomainValueService domainValueService;

	private PendenciaService pendenciaService;

	private WorkflowPendenciaService workflowPendenciaService;

	private TipoEventoService tipoEventoService;

	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;

	/**
	 * Recupera uma instância de <code>Acao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Acao findById(java.lang.Long id) {
        return (Acao)super.findById(id);
    }

	public Map<String, Object> findMapById(Long id) {
		Acao acao=(Acao)super.findById(id);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("idAcao", acao.getIdAcao());
		map.put("idPendencia", acao.getPendencia().getIdPendencia());
		map.put("nmClasseVisualizacao", acao.getPendencia().getOcorrencia().getEventoWorkflow().getNmClasseVisualizacao());
		map.put("nmChaveTitulo", acao.getPendencia().getOcorrencia().getEventoWorkflow().getNmChaveTitulo());
		map.put("blRequerAprovacao", acao.getPendencia().getOcorrencia().getEventoWorkflow().getBlRequerAprovacao());
		map.put("dsTipoEvento", acao.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getDsTipoEvento());
		map.put("dhInclusao", acao.getDhInclusao());
		map.put("dhLiberacao", acao.getDhLiberacao());
		map.put("idProcesso", acao.getPendencia().getIdProcesso());
		map.put("nmUsuario", acao.getPendencia().getOcorrencia().getUsuario().getNmUsuario());
		String nr="";
		Long lNr=getAcaoDAO().findLastNr(acao.getPendencia().getIdProcesso());
		Short tipoEvento = acao.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();

		// Verifica se o evento é do tipo 2501, 2502, 2503, 2504 ou 2602, caso seja de um dos tipos citados então não entra na condição para não concatenar 2 vezes a mensagem
		if(!(tipoEvento.equals(ConstantesWorkflow.NR2501_VLR_CARTCE) || tipoEvento.equals(ConstantesWorkflow.NR2502_DESC_NOTCRE) ||
				tipoEvento.equals(ConstantesWorkflow.NR2503_ACRE_NOTCRE) || tipoEvento.equals(ConstantesWorkflow.NR2504_CTO_MAIOR_VLR) || tipoEvento.equals(ConstantesWorkflow.NR2602_VLR_CONTRCE))){
		if(lNr!=null){
			nr=" "+acao.getPendencia().getOcorrencia().getFilial().getSgFilial()+" "+new DecimalFormat("0000000000").format(lNr);
		}
		}

		map.put("dsPendencia", (acao.getPendencia().getDsPendencia()!=null?acao.getPendencia().getDsPendencia():"")+nr);
		return map;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Acao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param Instância do DAO.
     */
    public void setAcaoDAO(AcaoDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AcaoDAO getAcaoDAO() {
        return (AcaoDAO) getDao();
    }

    /**
     * Paginação para visualização do fluxo da pendência
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedPendencia(TypedFlatMap criteria) {
    	return getAcaoDAO().findPaginatedPendencia(criteria, FindDefinition.createFindDefinition(criteria));
    }

    /**
     * Contage da paginação para visualização do fluxo da pendência
     * @param criteria
     * @return
     */
    public Integer getRowCountPendencia(TypedFlatMap criteria) {
    	return getAcaoDAO().getRowCountPendencia(criteria);
    }


    /**
	 * @param idEvento: Evento do workflow
	 * @param dhLiberacaoInicial e dhLiberacaoFinal
	 * 		  Data de inclusão da ação
	 * @return List com as seguintes ações:
	 * 		- pendentes para o usuário logado
	 * 		- pendentes para usuários onde o usuário logado seja subsitituto por falta
	 * 		- pendentes para usuários onde o usuário logado seja substituto
	 *
	 * pendentes = blLiberada = 'S' e dhLiberacao menor ou igual data atual
	 *
	 * order: dhLiberacao e dsTipoEvento
	 *
	 */
	public ResultSetPage findAcoesUsuarioLogado(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia,
			int currentPage,
			int pageSize) {

		ResultSetPage rsp = getAcaoDAO().findAcoesUsuarioLogado(dsTipoEvento,
				dhLiberacaoInicial,
				dhLiberacaoFinal,
				idSolicitante,
				idProcesso,
				dsPendencia,
				currentPage,
				pageSize);
		Iterator iter = rsp.getList().iterator();
		List newList = new ArrayList(rsp.getList().size());
		while (iter.hasNext()){

			Object[] obj = (Object[]) iter.next();

			Map map = new HashMap();

			map.put("idAcao",obj[0]);
			map.put("dhLiberacao", new DateTime(((Timestamp)obj[1]).getTime()));
			map.put("idTipoEvento",obj[2]);
			map.put("pendencia",obj[3]);
			map.put("idProcesso",obj[4]);
			map.put("dsTipoEvento", obj[5]);
			map.put("nmClasseVisualizacao", obj[6]);

			String novaDescricao=obj[3]!=null?obj[3].toString():"";
			map.put("novaDescricao", novaDescricao);
			newList.add(map);

		}

		rsp.setList(newList);

		return rsp;

	}

	public Integer getRowCountAcoesUsuarioLogado(String dsTipoEvento,
			YearMonthDay dhLiberacaoInicial,
			YearMonthDay dhLiberacaoFinal,
			Long idSolicitante,
			Long idProcesso,
			String dsPendencia){
		return getAcaoDAO().getRowCountAcoesUsuarioLogado(dsTipoEvento,
				dhLiberacaoInicial,
				dhLiberacaoFinal,
				idSolicitante,
				idProcesso,
				dsPendencia);
	}

	/**
	 * @param idAcao: Ação a ser aprovada
	 *
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveAprovarAcao(Long idAcao, String obAcao) {
		return this.saveAcao(idAcao, true, obAcao, "A");
	}

	/**
	 * @param idAcao: Ação a ser reprovada
	 *
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveReprovarAcao(Long idAcao, String obAcao) {
		return this.saveAcao(idAcao, false, obAcao, "R");
	}

	/**
	 * @param idAcao: Ação a ser marcada como visualizada
	 *
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String saveVisualizarAcao(Long idAcao, String obAcao) {
		return this.saveAcao(idAcao, true, obAcao, "V");
	}

	/**
	 * Salva a acao recebida e habilita a proxima acao.
	 *
	 * */
	private String saveAcao(Long idAcao, boolean blAprovado, String obAcao, String statusAcaoWorkflow) {

		Acao acao = (Acao)this.findById(idAcao);

		if (acao == null) {
			throw new IllegalStateException("Não foi localizada uma Acao com o idAcao = "+idAcao+" com statusAcaoWorkflow="+statusAcaoWorkflow+" do com flag aprovado="+blAprovado);
		}
		DomainValue tpSituacaoAcao = this.getDomainValueService().findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW", statusAcaoWorkflow);
		if (tpSituacaoAcao == null) {
			throw new IllegalArgumentException("ValorDominio não é valido: "+statusAcaoWorkflow+" para o dominio 'DM_STATUS_ACAO_WORKFLOW'.");
		}

		/*
		 * Verificar se a ação foi cancelada (pode acontecer que o solicitante cancela
		 * o evento e o usuario ainda enxergar a ação antes de ser cancelada, por isso
		 * tem que verificar de novo na hora de salvar).
		 * */
		if (acao.getTpSituacaoAcao().getValue().equals("C")){
			throw new BusinessException("LMS-39019");
		}

		acao.setTpSituacaoAcao(tpSituacaoAcao);
		acao.setObAcao(obAcao);
		acao.setDhAcao(JTDateTimeUtils.getDataHoraAtual());
		acao.setUsuario(SessionUtils.getUsuarioLogado());

		this.store(acao);

		Pendencia pendencia = acao.getPendencia();

		String textoRetorno = null; // texto de retorno, deve ser 'null' que é o retorno que a tela espera, caso contrário é exibida na tela o alert.
		boolean fecharPendencia = true; // por padrão sempre fecha a pendencia a não ser que exista uma proxima ação

		if (blAprovado == false) {
			pendencia.setTpSituacaoPendencia(this.getDomainValueService().findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW","R"));
		} else {

			Acao proximaAcao = findProximaAcao(acao.getPendencia().getIdPendencia(), acao.getNrOrdemAprovacao());

			//Se tem uma proxima ação então encaminha para o proximo usuario a pendencia
			if (proximaAcao != null) {
				proximaAcao.setBlLiberada(Boolean.TRUE);
				proximaAcao.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());

				//Liberar a proxima ação
				this.store(proximaAcao);

				Long idFilial = pendencia.getOcorrencia().getFilial().getIdFilial();
				Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();

				//Mandar um email ou salvar uma mensagem para o usuario informando que ele tem uma nova pendencia
				final Integrante integrante = proximaAcao.getIntegrante();
				this.getGerarEmailMensagemAvisoService().gerarEmailMensagem(idFilial,
																			nrTipoEvento,
																			integrante.getUsuario(),
																			integrante.getPerfil(),
																			proximaAcao.getPendencia());

				fecharPendencia = false;

			} else {
				//Fechar a pendencia caso não tenha proxima ação
				pendencia.setTpSituacaoPendencia(getDomainValueService().findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW","A"));
			}
		}

		if (fecharPendencia) {
			textoRetorno = getWorkflowPendenciaService().executeClosePendencia(pendencia);
		}

		return textoRetorno;

	}

	/**
	 * Cancela as ações da pendencia.
	 *
	 * @param Long idPendencia
	 * */
	public void cancelAcoesPendentes(Long idPendencia){
		List<Acao> lstAcoes = this.findByPendencia(idPendencia);

		for (Acao acao : lstAcoes) {
			acao.setTpSituacaoAcao(this.getDomainValueService().findDomainValueByValue("DM_STATUS_ACAO_WORKFLOW","C"));
			acao.setDhAcao(JTDateTimeUtils.getDataHoraAtual());
			acao.setObAcao(configuracoesFacade.getMensagem("LMS-39020"));
			super.store(acao);
	}
	}

	/**
	 * Pesquisa as acoes relacionadas a pendencia informada ordenando de forma
	 * descendente pelos campos dhAcao e idAcao.
	 *
	 * @param criteria
	 *            criterios de pesquisa, deve possuir um campo com o nome
	 *            idPendencia do tipo Long
	 * @return a pagina com os resultados
	 */
	public ResultSetPage findPaginatedByIdPendencia(TypedFlatMap criteria) {
		Long idPendencia = criteria.getLong("idPendencia");
		FindDefinition def = FindDefinition.createFindDefinition(criteria);

		return getAcaoDAO().findPaginatedByIdPendencia(idPendencia, def);
	}

	/**
	 * Calcula o numero de acoes relacionadas a pendencia informada.
	 *
	 * @param criteria
	 *            criterios de pesquisa, deve possuir um campo com o nome
	 *            idPendencia do tipo Long
	 * @return numero de acoes da pendencia
	 */
	public Integer getRowCountByIdPendencia(TypedFlatMap criteria) {
		Long idPendencia = criteria.getLong("idPendencia");
		return getAcaoDAO().getRowCountByIdPendencia(idPendencia);
	}


	/**
	 * @param acao: Retorna a próxima ação de acordo
	 * 				com a ordem (acao.nr_ordem)
	 */
	public List findAcoesPendente() {
		return this.getAcaoDAO().findAcoesPendente();
	}

	/**
	 * @param acao: Retorna a próxima ação de acordo
	 * 				com a ordem (acao.nr_ordem)
	 */
	private Acao findProximaAcao(Long idPendencia, Byte nrOrdemAtual) {
		return this.getAcaoDAO().findProximaAcao(idPendencia, nrOrdemAtual);
	}

	/**
	 * Retorna as ações pendentes de uma pendencia
	 *
	 * @param Long idPendencia
	 * @return List
	 */
	public List findByPendencia(Long idPendencia) {
		return this.getAcaoDAO().findByPendencia(idPendencia);
	}
	/**
	 * Retorna a ultima ação da pendencia passado no parametro
	 * @author Samuel Herrmann
	 * @param idPendencia
	 * @return
	 */
	public Acao findLastAcaoByPendencia(Long idPendencia) {
		return getAcaoDAO().findLastAcaoByPendencia(idPendencia);
	}

	/**
	 * Retorna as ações pendentes de uma pendencia
	 *
	 * @param Long idPendencia
	 * @return String
	 */
	public String findObservasaoUltimaAcao(Long idPendencia) {
		List lstAcoes = this.getAcaoDAO().findUltimaAcaoByPendencia(idPendencia);

		if (lstAcoes.size() > 0) {
			return ((Acao)lstAcoes.get(0)).getObAcao();
		} else {
			return null;
		}
	}

	public List findAcoesNaoLiberadas(){
		return this.getAcaoDAO().findAcoesNaoLiberadas();
	}

	public List findByIdPendenciaTpSituacaoAcao(Long idPendencia, String tpSituacaoAcao) {
		return getAcaoDAO().findByIdPendenciaTpSituacaoAcao(idPendencia, tpSituacaoAcao);
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public TipoEventoService getTipoEventoService() {
		return tipoEventoService;
	}

	public void setTipoEventoService(TipoEventoService tipoEventoService) {
		this.tipoEventoService = tipoEventoService;
	}

	public GerarEmailMensagemAvisoService getGerarEmailMensagemAvisoService() {
		return gerarEmailMensagemAvisoService;
	}

	public void setGerarEmailMensagemAvisoService(
			GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public List<Map<String,Object>> findPendencias(Long idPendencia){
		return getAcaoDAO().findPendencias(idPendencia);
	}

	public List<FluxoWorkFlowDto> findPendenciaSituacaoWorkflow(Long idDoctoServico){
		List<Object[]> retornoPendencia = getAcaoDAO().findPendenciaSituacaoWorkflow(idDoctoServico);
		List<FluxoWorkFlowDto> listFluxoWorkFlow = retornoPendencia.stream().map(rp -> new FluxoWorkFlowDto((Long) rp[0],
				(String) rp[1], (String) rp[2], (String) rp[3], (String) rp[4], (String) rp[5], (String) rp[6], (Long) rp[7])).collect(Collectors.toList());
		return  listFluxoWorkFlow;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}

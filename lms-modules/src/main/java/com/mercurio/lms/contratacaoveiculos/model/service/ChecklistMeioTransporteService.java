package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.ChecklistMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.ChecklistMeioTransporteDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.checklistMeioTransporteService"
 */
public class ChecklistMeioTransporteService extends CrudService<ChecklistMeioTransporte, Long> {
	
	private PessoaService pessoaService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private RespostaChecklistService respostaChecklistService;
	
	private MeioTransporteService meioTransporteService;
	
	private SolicitacaoContratacaoService solicitacaoContratacaoService;

	
	public void setSolicitacaoContratacaoService(
			SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}


	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}


	public RespostaChecklistService getRespostaChecklistService() {
		return respostaChecklistService;
	}


	public void setRespostaChecklistService(
			RespostaChecklistService respostaChecklistService) {
		this.respostaChecklistService = respostaChecklistService;
	}


	protected ChecklistMeioTransporte beforeStore(ChecklistMeioTransporte bean) {
		ChecklistMeioTransporte checklistMT = (ChecklistMeioTransporte)bean;
		if(checklistMT.getPessoaByIdPrimeiroMotorista()!= null && checklistMT.getPessoaByIdSegundoMotorista()!= null)
			if(checklistMT.getPessoaByIdPrimeiroMotorista().getNrIdentificacao()!= null && checklistMT.getPessoaByIdSegundoMotorista().getNrIdentificacao()!= null)
				if(checklistMT.getPessoaByIdPrimeiroMotorista().getNrIdentificacao().equalsIgnoreCase(checklistMT.getPessoaByIdSegundoMotorista().getNrIdentificacao()))
					throw new BusinessException("LMS-26074");
		
		if(checklistMT.getPessoaByIdPrimeiroMotorista()!= null)
			if((checklistMT.getPessoaByIdPrimeiroMotorista().getNrIdentificacao()!= null && checklistMT.getPessoaByIdPrimeiroMotorista().getNmPessoa() == null)
				|| (checklistMT.getPessoaByIdPrimeiroMotorista().getNmPessoa() != null && checklistMT.getPessoaByIdPrimeiroMotorista().getNrIdentificacao() == null))
				throw new BusinessException("LMS-26054");
		
		if(checklistMT.getPessoaByIdSegundoMotorista()!= null)
			if((checklistMT.getPessoaByIdSegundoMotorista().getNrIdentificacao()!= null && checklistMT.getPessoaByIdSegundoMotorista().getNmPessoa() == null)
			|| (checklistMT.getPessoaByIdSegundoMotorista().getNmPessoa() != null && checklistMT.getPessoaByIdSegundoMotorista().getNrIdentificacao() == null) 
			|| (checklistMT.getPessoaByIdSegundoMotorista().getTpIdentificacao()!= null && checklistMT.getPessoaByIdSegundoMotorista().getNmPessoa() == null && checklistMT.getPessoaByIdSegundoMotorista().getNrIdentificacao() == null))
				throw new BusinessException("LMS-26054");
		
		if(checklistMT.getPessoaByIdPrimeiroMotorista() != null)
			if(checklistMT.getPessoaByIdPrimeiroMotorista().getNmPessoa() != null && checklistMT.getPessoaByIdPrimeiroMotorista().getIdPessoa() == null){
				Long idPessoaPri = (Long) getPessoaService().store(checklistMT.getPessoaByIdPrimeiroMotorista());
				checklistMT.getPessoaByIdPrimeiroMotorista().setIdPessoa(idPessoaPri);
			}
		
		if(checklistMT.getPessoaByIdSegundoMotorista()!= null)
			if(checklistMT.getPessoaByIdSegundoMotorista().getNmPessoa() != null && checklistMT.getPessoaByIdSegundoMotorista().getIdPessoa() == null){
				Long idPessoaSeg = (Long)getPessoaService().store(checklistMT.getPessoaByIdSegundoMotorista());
				checklistMT.getPessoaByIdSegundoMotorista().setIdPessoa(idPessoaSeg);
			}		
				
		return super.beforeStore(bean);
	}
	
	// FIXME corrigir para retornar o ID
	public ChecklistMeioTransporte store(Object bean) {
		ChecklistMeioTransporte checklistMT = (ChecklistMeioTransporte)bean;

		DomainValue domainValue = new DomainValue();
		if(checklistMT.getIdChecklistMeioTransporte() != null){
			domainValue.setValue(getSituacaoChecklistMeioTransporte(checklistMT.getIdChecklistMeioTransporte()));
			checklistMT.setTpSituacao(domainValue);
		} else {
			domainValue.setValue("I");
			domainValue.setDescription(new VarcharI18n("Incompleto"));
			checklistMT.setTpSituacao(domainValue);
		}

		Long nrCheckList = configuracoesFacade.incrementaParametroSequencial(checklistMT.getFilial().getIdFilial(), "NR_CHECKLIST", true);
		checklistMT.setNrChecklist(nrCheckList);

		super.store(checklistMT);
		return checklistMT;
	}

	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {
		ResultSetPage<Map<String, Object>> rsp = getChecklistMeioTransporteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> rs = rsp.getList();
		for(Map<String, Object> map : rs) {
			String nrIdentificador = (String)map.get("nrIdentificacaoMeioTransp");
			if (StringUtils.isNotBlank(nrIdentificador)) {
				MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(nrIdentificador);
				if (meioTransporte != null)
					map.put("nrFrota", meioTransporte.getNrFrota());
			}
		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getChecklistMeioTransporteDAO().getRowCount(criteria);
	}

	public String findDsRotaSolicitacaoContratacao(Long idSolicitacaoContratacao){
		Map<String, Object> mapRota = solicitacaoContratacaoService.findRotasByIdSolicitacaoContratacao(idSolicitacaoContratacao);

		String dsRotaConcatenada = "";

		if(!mapRota.isEmpty()){
			String nrRotaFormatada = "";
			if(mapRota.get("nrRota")!= null){
				nrRotaFormatada = FormatUtils.formatIntegerWithZeros((Integer)mapRota.get("nrRota"),"0000");
				if(mapRota.get("dsRota")!= null) {
					dsRotaConcatenada = dsRotaConcatenada.concat(nrRotaFormatada.concat(" - ").concat(mapRota.get("dsRota").toString()));
				} else {
					dsRotaConcatenada = nrRotaFormatada;
				}
			} else {
				if(mapRota.get("dsRota") != null)
					dsRotaConcatenada = mapRota.get("dsRota").toString();
			}
		}

		return dsRotaConcatenada;
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
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
    	super.removeByIds(ids);
    }
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setChecklistMeioTransporteDAO(ChecklistMeioTransporteDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ChecklistMeioTransporteDAO getChecklistMeioTransporteDAO() {
        return (ChecklistMeioTransporteDAO) getDao();
    }

    //verifica se existe algum checklist associado a solicitacao de contratacao
    public void findCheckListByIdSolicitacao(Long idSolicitacaoContratacao){
    	boolean existeCheckList = getChecklistMeioTransporteDAO().findCheckListByIdSolicitacao(idSolicitacaoContratacao);
    	if(existeCheckList)
    		throw new BusinessException("LMS-26047");
    }

    public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	//verifica se existem respostas para os dois checklists(motorista e meio de transporte) 
	//se existe verifica se existem respostas reprovadas onde existe obrigatoriedade de serem aprovadas.
	public String getSituacaoChecklistMeioTransporte(Long idChecklistMeioTransporte){
		String situacao = "I";
		if(getRespostaChecklistService().findRespostaCheckListByIdCheckListMeioTransporte(idChecklistMeioTransporte) && getRespostaChecklistService().findRespostaCheckListMotByIdCheckListMeioTransporte(idChecklistMeioTransporte)){
			boolean respostaMT = getRespostaChecklistService().findRespostaChecklistMTReprovada(idChecklistMeioTransporte);
			boolean respostaMO = getRespostaChecklistService().findRespostaChecklistMOReprovada(idChecklistMeioTransporte);
			if(respostaMT || respostaMO)
				situacao="R";
			else if (!respostaMT && !respostaMO)
				situacao="A";
		}			
		return situacao;
	}

	protected void beforeRemoveById(Long id) {
		ChecklistMeioTransporte checklistMeioTransporte = (ChecklistMeioTransporte) super.findById(id);

		getDao().getAdsmHibernateTemplate().deleteAll(checklistMeioTransporte.getRespostaChecklists());
		getDao().getAdsmHibernateTemplate().flush();

		if((checklistMeioTransporte.getTpSituacao().getValue().equalsIgnoreCase("R")) || checklistMeioTransporte.getTpSituacao().getValue().equalsIgnoreCase("A"))
			throw new BusinessException("LMS-26048");
		super.beforeRemoveById(id);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for(int i = 0; i < ids.size(); i++){
			ChecklistMeioTransporte checklistMeioTransporte = (ChecklistMeioTransporte)this.findById((Long)ids.get(i));
			if((checklistMeioTransporte.getTpSituacao().getValue().equalsIgnoreCase("R")) || checklistMeioTransporte.getTpSituacao().getValue().equalsIgnoreCase("A"))
				throw new BusinessException("LMS-26048");
			else{
				getDao().getAdsmHibernateTemplate().deleteAll(checklistMeioTransporte.getRespostaChecklists());
				getDao().getAdsmHibernateTemplate().flush();
			}
		}
		super.beforeRemoveByIds(ids);
	}

	@Override
	public ChecklistMeioTransporte findById(Long id) {
		return (ChecklistMeioTransporte) super.findById(id);
	}
	
	public void updateSituacaoChecklist(ChecklistMeioTransporte check){
		ChecklistMeioTransporte cmt = (ChecklistMeioTransporte) getChecklistMeioTransporteDAO().getAdsmHibernateTemplate().load(ChecklistMeioTransporte.class,check.getIdChecklistMeioTransporte());
		cmt.setTpSituacao(check.getTpSituacao());
		getChecklistMeioTransporteDAO().getAdsmHibernateTemplate().update(cmt);
	}

	
	public TypedFlatMap findByIdCustom(Long id) {
		Map<String, Object> map = (Map<String, Object>)getChecklistMeioTransporteDAO().findByIdCustom(id);
        AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
        TypedFlatMap retorno = a.transformeTupleMap(map);

        String nrIdentificacaoMT = retorno.getString("solicitacaoContratacao.nrIdentificacaoMeioTransp");
    	if (StringUtils.isNotEmpty(nrIdentificacaoMT)) {
    		String nrFrota = meioTransporteService.findNrFrotaByNrIdentificacao(nrIdentificacaoMT);
    		retorno.put("nrFrotaMT",nrFrota);
    	}

    	String nrIdentificacaoSR = retorno.getString("solicitacaoContratacao.nrIdentificacaoSemiReboque");
    	if (StringUtils.isNotEmpty(nrIdentificacaoSR)) {
    		String nrFrotaSR = meioTransporteService.findNrFrotaByNrIdentificacao(nrIdentificacaoSR);
    		retorno.put("nrFrotaSR",nrFrotaSR);
    	}

    	Long idSolicitacao = retorno.getLong("solicitacaoContratacao.idSolicitacaoContratacao");
    	String dsRota = retorno.getString("rota.dsRota");
		if(StringUtils.isBlank(dsRota) && idSolicitacao != null){
			dsRota = findDsRotaSolicitacaoContratacao(idSolicitacao);
			retorno.put("rota.dsRota",dsRota);
		}

        Long idPessoaByIdPrimeiroMotorista = retorno.getLong("pessoaByIdPrimeiroMotorista.idPessoa");
        if(idPessoaByIdPrimeiroMotorista != null){
            //1º motorista
    		String tpIdentificacao = retorno.getString("pessoaByIdPrimeiroMotorista.tpIdentificacao.value");
    		retorno.put("pessoaByIdPrimeiroMotorista.nrIdentificacao",
    				FormatUtils.formatIdentificacao(tpIdentificacao,retorno.getString("pessoaByIdPrimeiroMotorista.nrIdentificacao")));
    	}

        Long idPessoaByIdSegundoMotorista = retorno.getLong("pessoaByIdSegundoMotorista.idPessoa");
        if(idPessoaByIdSegundoMotorista != null){
            //2º motorista
        	String tpIdentificacao = retorno.getString("pessoaByIdSegundoMotorista.tpIdentificacao.value");
        	retorno.put("pessoaByIdSegundoMotorista.nrIdentificacao",
    				FormatUtils.formatIdentificacao(tpIdentificacao,retorno.getString("pessoaByIdSegundoMotorista.nrIdentificacao")));
    	}

        YearMonthDay dtRealizacao = retorno.getYearMonthDay("dtRealizacao");
        if(dtRealizacao.compareTo(JTDateTimeUtils.getDataAtual())==0)
			retorno.put("isDataAtual","true");

        return retorno;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}
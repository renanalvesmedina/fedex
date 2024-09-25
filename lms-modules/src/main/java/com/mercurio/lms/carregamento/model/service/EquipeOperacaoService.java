package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.carregamento.model.dao.EquipeOperacaoDAO;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.SetorService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.equipeOperacaoService"
 */
public class EquipeOperacaoService extends CrudService<EquipeOperacao, Long> {

	private CarregamentoDescargaService carregamentoDescargaService;
	private ControleCargaService controleCargaService;
	private EquipeService equipeService;
	private IntegranteEqOperacService integranteEqOperacService;
	private IntegranteEquipeService integranteEquipeService;
	private SetorService setorService;
	private ParametroGeralService parametroGeralService;


	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public void setIntegranteEquipeService(IntegranteEquipeService integranteEquipeService) {
		this.integranteEquipeService = integranteEquipeService;
	}
	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public EquipeService getEquipeService() {
		return equipeService;
	}
	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}

	/**
	 * Recupera uma instância de <code>EquipeOperacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EquipeOperacao findById(Long id) {
        return (EquipeOperacao)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(Long id) {
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
    public java.io.Serializable store(EquipeOperacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEquipeOperacaoDAO(EquipeOperacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EquipeOperacaoDAO getEquipeOperacaoDAO() {
        return (EquipeOperacaoDAO) getDao();
    }
    
    /**
     * Chama a DAO de EquipeOperacao que por sua vez persiste o mesmo
     * 
     * @param equipeOperacao
     * @param items
     */
    public java.io.Serializable storeEquipeOperacao(EquipeOperacao equipeOperacao, ItemList items) {
    	this.getEquipeOperacaoDAO().storeEquipeOperacao(equipeOperacao, items);
    	return getEquipeOperacaoDAO().getIdentifier(equipeOperacao);
    }
    
    /**
	 * Faz o findPaginated
	 * 
	 * @param idCarregamentoDescarga
	 * @param idControleCarga
	 * @param blOrderDescIdEquipeOperacao 
	 * @param blOrderDescDhFimOperacao
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedByIdControleCarga(Long idCarregamentoDescarga, Long idControleCarga, 
														Boolean blOrderDescIdEquipeOperacao, Boolean blOrderDescDhFimOperacao, 
														FindDefinition findDefinition) 
	{		
		return this.getEquipeOperacaoDAO().findPaginatedByIdControleCarga(
				idCarregamentoDescarga, idControleCarga, blOrderDescIdEquipeOperacao, blOrderDescDhFimOperacao, findDefinition);
	}

	/**
	 * Faz o getRowCount
	 * 
	 * @param idCarregamentoDescarga
	 * @param idControleCarga
	 * @return
	 */
	public Integer getRowCountByIdControleCarga(Long idCarregamentoDescarga, Long idControleCarga) {
		return this.getEquipeOperacaoDAO().getRowCountByIdControleCarga(idCarregamentoDescarga, idControleCarga);
	}
	
	/**
	 * Retorna a ultima equipe operacao de um determinado carregamentoDescarga
	 * 
	 * @param criteria
	 * @return
	 */
	public List findEquipeByIdControleCarga(Long idCarregamentoDescarga) {
		return this.getEquipeOperacaoDAO().findEquipeByIdControleCarga(idCarregamentoDescarga);
	}

	
    /**
     * Retorna a última equipe operacao cadastrada de um controle de carga coleta/entrega
     * 
     * @param idControleCarga
     * @return
     */
    public EquipeOperacao findByIdControleCargaColetaEntrega(Long idControleCarga){
    	return getEquipeOperacaoDAO().findByIdControleCargaColetaEntrega(idControleCarga);
    }

	
	/**
	 * Busca a ultima equipe vinculada a um controle de carga e a finaliza.
	 * Tem como obrigatorio um idCarregamentoDescarga. Caso o parametro dataFinal nao seja informado,
	 * ele considera a data atual para o registro da equipe operacao
	 * 
	 * @param idCarregamentoDescarga
	 * @param dataFinal
	 */
	public void storeEquipeAtual(Long idCarregamentoDescarga, DateTime dataFinal) {
		CarregamentoDescarga carregamentoDescarga = this.getCarregamentoDescargaService().findById(idCarregamentoDescarga);
		List equipeOperacoes = carregamentoDescarga.getEquipeOperacoes();
		
		for (Iterator iter = equipeOperacoes.iterator(); iter.hasNext();) {
			EquipeOperacao equipeOperacao = (EquipeOperacao) iter.next();
			if (equipeOperacao.getDhFimOperacao() == null) {
				if (dataFinal != null) {
					equipeOperacao.setDhFimOperacao(dataFinal);
				} else {
					equipeOperacao.setDhFimOperacao(JTDateTimeUtils.getDataHoraAtual());
				}
				
				this.getEquipeOperacaoDAO().store(equipeOperacao);								
			}			
		}
	}
	
	/**
	 * 
	 * 
	 * @param criteria
	 * @param equipeOperacao
	 * @param items
	 * @return
	 */
	public java.io.Serializable storeTrocarEquipe(TypedFlatMap criteria, EquipeOperacao equipeOperacao, ItemList items, ItemListConfig config) {
		
    	if (!items.hasItems()) {
    		throw new BusinessException("LMS-05101");
    	} else {
    		for (Iterator iter = items.iterator(equipeOperacao.getIdEquipeOperacao(), config); iter.hasNext();) {
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
				integranteEqOperac.setEquipeOperacao(equipeOperacao);
				
				//Remove a instancia setada na action para visualizacao apenas...
				if (integranteEqOperac.getTpIntegrante().getValue().equals("F")){
					integranteEqOperac.setCargoOperacional(null);
				}
			}
    	}
    	
    	try {
    		
	    	Long idEquipe = criteria.getLong("equipe.idEquipe");
	    	Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
	    	Long idControleCarga = criteria.getLong("controleCarga.idControleCarga");
	    	
	    	//Finaliza a equipe anterior.
	    	storeEquipeAtual(idCarregamentoDescarga, criteria.getDateTime("dataTerminoEquipe"));
	    	
	    	//Busca objetos para a sessao do hibernate...
	    	final CarregamentoDescarga carregamentoDescarga = this.getCarregamentoDescargaService().findById(idCarregamentoDescarga);
	    	final Equipe equipe = this.getEquipeService().findById(idEquipe);
	    	final ControleCarga controleCarga = this.getControleCargaService().findByIdInitLazyProperties(idControleCarga, false);
	    	
	        equipeOperacao.setDhInicioOperacao(criteria.getDateTime("dhInicioOperacao"));
		    //Gerando os relacionamentos...
		    equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
		    equipeOperacao.setControleCarga(controleCarga);
		    equipeOperacao.setEquipe(equipe);
	        
	    	//Persistencia da DF2
	        this.storeEquipeOperacao(equipeOperacao, items);
	        
    	} catch (RuntimeException runex) {
        	//this.rollbackMasterState(equipeOperacao, rollbackMasterId, runex); 
            //items.rollbackItemsState();   
            throw runex;
        }
    	
    	return new CarregamentoDescarga();
	}
	
	


	/**
	 * 
	 * @param idEquipe
	 * @param idControleCarga
	 * @param idEquipeOperacaoOld
	 * @return
	 */
	public Map generateTrocaEquipeByControleCarga(Long idEquipe, Long idControleCarga) {
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
		
		List listaEquipeOperacaoOld = findPaginatedByIdControleCarga(
				null, idControleCarga, null, Boolean.TRUE, FindDefinition.createFindDefinition(null)).getList();
		if (!listaEquipeOperacaoOld.isEmpty()) {
			EquipeOperacao equipeOperacaoOld = (EquipeOperacao)listaEquipeOperacaoOld.get(0);
			equipeOperacaoOld.setDhFimOperacao(dhAtual);
			store(equipeOperacaoOld);
		}

		Equipe equipe = equipeService.findById(idEquipe);

		EquipeOperacao equipeOperacao = new EquipeOperacao();
		equipeOperacao.setEquipe(equipe);
		equipeOperacao.setDhInicioOperacao(dhAtual);
		equipeOperacao.setControleCarga(controleCargaService.findByIdInitLazyProperties(idControleCarga, false));
		equipeOperacao.setCarregamentoDescarga(null);
		equipeOperacao.setDhFimOperacao(null);
		equipeOperacao.setIdEquipeOperacao((Long)store(equipeOperacao));

		Map mapEquipe = new HashMap();
		mapEquipe.put("idEquipe", idEquipe);
		
		Map mapIntegranteEquipe = new HashMap();
		mapIntegranteEquipe.put("equipe", mapEquipe);
		
		List listaIntegranteEquipe = integranteEquipeService.find(mapIntegranteEquipe);

		List listaIntegranteEqOperac = new ArrayList();
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = listaIntegranteEquipe.iterator(); iter.hasNext();) {
			IntegranteEquipe integranteEquipe = (IntegranteEquipe)iter.next();

			Long idUsuario = integranteEquipe.getUsuario() == null ? null : integranteEquipe.getUsuario().getIdUsuario();
			Long idPessoa = integranteEquipe.getPessoa() == null ? null : integranteEquipe.getPessoa().getIdPessoa();
			List retorno = controleCargaService.findIntegranteEmEquipesComControleCarga(idControleCarga, idPessoa, idUsuario);
	    	if (!retorno.isEmpty()) {
	    		Map map = (Map)retorno.get(0);
	    		String siglaNumero = FormatUtils.formatSgFilialWithLong((String)map.get("sgFilial"), (Long)map.get("nrControleCarga"), "00000000");
	    		sb.append("\n- ").append((String)map.get("nmIntegrante")).append(" (").append(siglaNumero).append(")");
	    		continue;
	    	}
			IntegranteEqOperac integranteEqOperac = new IntegranteEqOperac();
			integranteEqOperac.setCargoOperacional(integranteEquipe.getCargoOperacional());
			integranteEqOperac.setEmpresa(integranteEquipe.getEmpresa());
			integranteEqOperac.setEquipeOperacao(equipeOperacao);
			integranteEqOperac.setPessoa(integranteEquipe.getPessoa());
			integranteEqOperac.setTpIntegrante(integranteEquipe.getTpIntegrante());
			integranteEqOperac.setUsuario(integranteEquipe.getUsuario());
			listaIntegranteEqOperac.add(integranteEqOperac);
		}

		for (Iterator iter = listaIntegranteEqOperac.iterator(); iter.hasNext();) {
			IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)iter.next();
			integranteEqOperacService.store(integranteEqOperac);
		}
		
		Map map = new HashMap();
		map.put("equipeOperacao", equipeOperacao);
		map.put("msgErro", sb.toString());
		return map;
	}


    /**
     * Obtém a última EquipeOperacao sem data de fechamento, para o ControleCarga em questão.
     * @param idControleCarga
     * @return
     */
    public EquipeOperacao findLastEquipeOperacaoWithoutFechamentoByIdControleCarga(Long idControleCarga) {
    	return getEquipeOperacaoDAO().findLastEquipeOperacaoWithoutFechamentoByIdControleCarga(idControleCarga);
    }
    
    
    public List findEquipeOperacaoByIdControleCarga(Long idControleCarga, Boolean blDhFimOperacaoNulo) {
    	return getEquipeOperacaoDAO().findEquipeOperacaoByIdControleCarga(idControleCarga, blDhFimOperacaoNulo);
    }
    
    /**
     * Busca List de EquipeOperacao que seja de um determinado CarregamentoDescarga levando em consideração o tipo
     * de operação ("C" ou "D") e se a data de fim da operação é nula ou não.
     * @param idCarregamentoDescarga
     * @param blDhFimOperacaoNulo
     * @param tpOperacao
     * @return
     */
    public List findEquipeOperacaoByIdCarregamentoDescarga(Long idCarregamentoDescarga, Boolean blDhFimOperacaoNulo, String tpOperacao) {
    	return getEquipeOperacaoDAO().findEquipeOperacaoByIdCarregamentoDescarga(idCarregamentoDescarga, blDhFimOperacaoNulo, tpOperacao);
    }
    
    public EquipeOperacao findEquipeOperacao(Long idCarregamentoDescarga, Boolean blDhFimOperacaoNulo, String tpOperacao) {
    	List lista = findEquipeOperacaoByIdCarregamentoDescarga(idCarregamentoDescarga, blDhFimOperacaoNulo, tpOperacao);
    	if(lista != null && !lista.isEmpty()){
    		return (EquipeOperacao) lista.get(0);
    	}
    	return null;
    }
    
    /**
     * Altera os dados de equipe operação para o cancelamento do controle de carga 
     * em questão.
     * 
     * @param idControleCarga
     * @param dhAtual
     */
    public void updateEquipeOperacaoByCancelamentoControleCarga(Long idControleCarga, DateTime dhAtual) {
    	List lista = getEquipeOperacaoDAO().findEquipeOperacaoByCancelamentoControleCarga(idControleCarga);
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		EquipeOperacao eo = (EquipeOperacao)iter.next();
    		eo.setDhFimOperacao(dhAtual);
    		store(eo);
    	}
    }
    
    
    
    
    /**
     * Busca a Equipe Operacao atraves do controle de carga e o carregamento descarga
     * @param idControleCarga
     * @param idCarregamentoDescarga
     * @return
     */
    public EquipeOperacao findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(Long idControleCarga, 
    																				  Long idCarregamentoDescarga){
    	
    	return getEquipeOperacaoDAO().findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(idControleCarga, idCarregamentoDescarga);
    }
    
    
    
    public EquipeOperacao findEquipeOperacaoByIdControleCargaByIdCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga){
    	
    	return getEquipeOperacaoDAO().findEquipeOperacaoByIdControleCargaByIdCarregamentoDescarga(idControleCarga, idCarregamentoDescarga);
    }
    
    
      
    public EquipeOperacao findEquipeOperacaoByDescricao (String descricao){
    	return getEquipeOperacaoDAO().findEquipeOperacaoByDescricao(descricao);
    }
    
    
    /**
	 * Inativa a equipe apos a conclusão da descarga;
	 * @param controleCarga
	 * @param carregamentoDescarga 
	 * @return
	 */
	public void storeEquipeOperacaoFimDescarga(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga) {
		
		EquipeOperacao equipe = this.findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(controleCarga.getIdControleCarga(), carregamentoDescarga.getIdCarregamentoDescarga());
		equipe.setDhFimOperacao(JTDateTimeUtils.getDataHoraAtual());
		this.store(equipe);
			
	}
    
    
    
    /**
	 * Salva a equipe e retorna equipe de operação para persistencia.
	 * @param controleCarga
	 * @param carregamentoDescarga 
	 * @return
	 */
	public EquipeOperacao storeEquipeDescarga(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga) {
		
		EquipeOperacao equipeOperacao = this.findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(controleCarga.getIdControleCarga(), carregamentoDescarga.getIdCarregamentoDescarga());
		
		if(equipeOperacao!=null && equipeOperacao.getEquipe()!=null){ 

			List<IntegranteEqOperac> lstEqOperacs = integranteEqOperacService.findIntegranteEqOperacByIdEquipeOp(equipeOperacao.getIdEquipeOperacao());
			for (IntegranteEqOperac integranteEqOperac : lstEqOperacs) {
				//Se o usuario já está inscrito nessa equipe operação retorna.
				if(integranteEqOperac.getUsuario() != null && integranteEqOperac.getUsuario().equals(SessionUtils.getUsuarioLogado())){
					return equipeOperacao;
				}
			}
	
			if(equipeOperacao.getEquipe().getTpSituacao().equals(new DomainValue("I"))){
				//LMS-45070 - Esta equipe está inativa
				throw new BusinessException("LMS-45070");
			}else{
				Equipe equipe = equipeOperacao.getEquipe();
				this.storeIntegranteEquipe(equipe, equipeOperacao);
				return equipeOperacao;
			}
		}else{
			//58 - Descarga, indicar via parametro_geral.
			//Setor responsavel pela Descarga
			BigDecimal idSetor = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("SETOR_DESCARGA_MWW", false);
			return this.storeNovaEquipe(controleCarga, carregamentoDescarga, getSetorService().findById(idSetor.longValue()));
		}
		
	}
    
    
	/**
	 * Salva a equipe e retorna equipe de operação para persistencia.
	 * @param controleCarga
	 * @param carregamentoDescarga 
	 * @param tpSituacaoEquipe 
	 * @param setor 
	 * @return
	 */
	private EquipeOperacao storeNovaEquipe(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga, Setor setor) {
		
		Equipe equipe = new Equipe();
		equipe.setDsEquipe(setor.getDsSetor()+" "+
						   controleCarga.getFilialByIdFilialOrigem().getSgFilial()+" "+
						   controleCarga.getNrControleCarga()+" "+
						   SessionUtils.getFilialSessao().getSgFilial());
		
		equipe.setFilial(SessionUtils.getFilialSessao());
		equipe.setSetor(setor);
		equipe.setTpSituacao(new DomainValue("A"));
		equipeService.store(equipe);
		
		EquipeOperacao equipeOperacao = new EquipeOperacao();
		equipeOperacao.setDhInicioOperacao(JTDateTimeUtils.getDataHoraAtual());
		equipeOperacao.setControleCarga(controleCarga);
		equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
		equipeOperacao.setEquipe(equipe);
		
		this.store(equipeOperacao);
				
		this.storeIntegranteEquipe(equipe, equipeOperacao);
		
		return equipeOperacao;
	}
	
	
	/**
	 * Salva integrante da equipe e equipe de operação.
	 * @param equipe
	 * @param equipeOperacao 
	 */
	private void storeIntegranteEquipe(Equipe equipe, EquipeOperacao equipeOperacao) {
		
//		IntegranteEquipe integranteEquipe = integranteEquipeService.findByUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
//
//		integranteEquipe.setEquipe(equipe);
//		integranteEquipeService.store(integranteEquipe);
		
		IntegranteEqOperac integranteEqOperac = new IntegranteEqOperac();
		integranteEqOperac.setEquipeOperacao(equipeOperacao);
		integranteEqOperac.setTpIntegrante(new DomainValue("F"));
		integranteEqOperac.setUsuario(SessionUtils.getUsuarioLogado());
		
		Empresa empresa = new Empresa();
		empresa.setIdEmpresa(SessionUtils.getUsuarioLogado().getEmpresaPadrao().getIdEmpresa());
		
		integranteEqOperac.setEmpresa(empresa);
		integranteEqOperacService.store(integranteEqOperac);
		
	}
	
	
    /**
	 * Cria equipe.
	 * @return
	 */
	public Equipe storeEquipeCarregamento(ControleCarga controleCarga) {
		
		Equipe equipe = new Equipe();
		//53 - Carregamento, indicar via parametro_geral.
		//Setor responsavel pelo carregamento
		BigDecimal idSetor = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("SETOR_CARREGAMENTO_MWW", false);
		equipe.setDsEquipe(getSetorService().findById(idSetor.longValue()).getDsSetor()+" "+
						   controleCarga.getFilialByIdFilialOrigem().getSgFilial()+" "+
						   controleCarga.getNrControleCarga()+" "+
						   SessionUtils.getFilialSessao().getSgFilial());
		
		equipe.setFilial(SessionUtils.getFilialSessao());
		equipe.setSetor(getSetorService().findById(idSetor.longValue()));
		equipe.setTpSituacao(new DomainValue("A"));
		equipeService.store(equipe);
	
		return equipe;
		
	}
	
	public void storeIntegranteEquipeOperacao(EquipeOperacao equipeOperacao){
		
		if(equipeOperacao!=null){
			List<IntegranteEqOperac> lstEqOperacs = integranteEqOperacService.findIntegranteEqOperacByIdEquipeOp(equipeOperacao.getIdEquipeOperacao());
			for (IntegranteEqOperac integranteEqOperac : lstEqOperacs) {
				//Se o usuario já está inscrito nessa equipe operação retorna.
				if(integranteEqOperac.getUsuario() != null && integranteEqOperac.getUsuario().equals(SessionUtils.getUsuarioLogado())){
					return;
				}
			}
			
			if(equipeOperacao.getEquipe()!=null && equipeOperacao.getEquipe().getTpSituacao().equals(new DomainValue("I"))){
				//LMS-45070 - Esta equipe está inativa
				throw new BusinessException("LMS-45070");
			}else{
				Equipe equipe = equipeOperacao.getEquipe();
				this.storeIntegranteEquipe(equipe, equipeOperacao);
			}
		}
	}
	
	
	public SetorService getSetorService() {
		return setorService;
	}
	public void setSetorService(SetorService setorService) {
		this.setorService = setorService;
	}
	public IntegranteEqOperacService getIntegranteEqOperacService() {
		return integranteEqOperacService;
	}
	public IntegranteEquipeService getIntegranteEquipeService() {
		return integranteEquipeService;
	}
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
    
    
}
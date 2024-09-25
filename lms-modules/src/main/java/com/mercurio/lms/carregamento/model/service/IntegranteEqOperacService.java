package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.carregamento.model.dao.IntegranteEqOperacDAO;
import com.mercurio.lms.carregamento.model.dao.IntegranteEquipeDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.integranteEqOperacService"
 */
public class IntegranteEqOperacService extends CrudService<IntegranteEqOperac, Long> {

	private ControleCargaService controleCargaService;
	private IntegranteEquipeDAO integranteEquipeDAO;


	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public IntegranteEquipeDAO getIntegranteEquipeDAO() {
		return integranteEquipeDAO;
	}

	public void setIntegranteEquipeDAO(IntegranteEquipeDAO integranteEquipeDAO) {
		this.integranteEquipeDAO = integranteEquipeDAO;
	}

	/**
	 * Recupera uma inst�ncia de <code>IntegranteEqOperac</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public IntegranteEqOperac findById(java.lang.Long id) {
        return (IntegranteEqOperac)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IntegranteEqOperac bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 * Este store nao possui regras de negocio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeBasic(IntegranteEqOperac bean) {
        return super.store(bean);
    }
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIntegranteEqOperacDAO(IntegranteEqOperacDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IntegranteEqOperacDAO getIntegranteEqOperacDAO() {
        return (IntegranteEqOperacDAO) getDao();
    }
    
    
    /**
     * Busca os dados da equipeOperacao informada
     * @param idEquipeOperacao
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedLiberacaoRiscoDadosEquipe(Long idEquipeOperacao, FindDefinition findDefinition) {
        return getIntegranteEqOperacDAO().findPaginatedLiberacaoRiscoDadosEquipe(idEquipeOperacao,findDefinition);
    }
    
    
    /**
     * Row count para o m�todo findPaginatedLiberacaoRiscoDadosEquipe()
     * @param idEquipeOperacao
     * @param fd
     * @return
     */
    public Integer getRowCountLiberacaoRiscoDadosEquipe(Long idEquipeOperacao) {
        return getIntegranteEqOperacDAO().getRowCountLiberacaoRiscoDadosEquipe(idEquipeOperacao);
    }

    /** 
     * Busca os integrantes de uma determinada equipe e transforma os mesmos em
     * integrantesEquipeOperacao.
     * 
     * Este metodo e especifico para a tela de iniciar	
     * 
     * @param idEquipe
     * @return
     */
    public List findIntegranteEqOperacao(Long idEquipe ){
    	
    	List integrantes = this.getIntegranteEquipeDAO().findIntegrantesEquipeByEquipe(idEquipe);
    	List result = new ArrayList();
    	
    	String nmIntegranteEquipe;
    	int idIntegranteEqOperac = 0;
    	
    	
    	IntegranteEqOperac integranteEqOperac = null;
    	for (Iterator iter = integrantes.iterator(); iter.hasNext();) {
			IntegranteEquipe integranteEquipe = (IntegranteEquipe) iter.next();
			integranteEqOperac = new IntegranteEqOperac();
			integranteEqOperac.setIdIntegranteEqOperac(Long.valueOf(--idIntegranteEqOperac));
			integranteEqOperac.setCargoOperacional(integranteEquipe.getCargoOperacional());
			integranteEqOperac.setEmpresa(integranteEquipe.getEmpresa());
			integranteEqOperac.setEquipeOperacao(null);
			integranteEqOperac.setPessoa(integranteEquipe.getPessoa());
			integranteEqOperac.setTpIntegrante(integranteEquipe.getTpIntegrante());
			integranteEqOperac.setUsuario(integranteEquipe.getUsuario());
			
			nmIntegranteEquipe = (integranteEquipe.getTpIntegrante().getValue().equals("T"))?
					integranteEquipe.getPessoa().getNmPessoa():integranteEquipe.getUsuario().getNmUsuario();
					
			integranteEqOperac.setNmIntegranteEquipe(nmIntegranteEquipe);
			result.add(integranteEqOperac);
		}
    	
    	return result;
    }
    
    /** 
     * Busca o numero de integrantes de uma determinada equipe
     * 
     * @param idEquipe
     * @return Integer
     */
    public Integer getRowCountIntegranteEqOperac(Long idEquipe){
    	return this.getIntegranteEquipeDAO().getRowCountIntegrantesEquipeByEquipe(idEquipe);
    }

    
    
    
	/**
	 * Busca os dados dos integrantes da equipe opera��o informada.
	 * 
	 * @param idEquipeOperacao
	 * @return
	 */
    public List findByEmitirControleCargaDadosEquipe(Long idEquipeOperacao) {
    	return getIntegranteEqOperacDAO().findByEmitirControleCargaDadosEquipe(idEquipeOperacao);
    }
    
    /**
     * Busca todos o integrantes de uma equipe operacao pelo id de sua equipe
     * 
     * @param idEquipeOperacao
     * @return
     */
    public List findIntegranteEqOperacByIdEquipeOp(Long idEquipeOperacao) {
    	return this.getIntegranteEqOperacDAO().findIntegranteEqOperacByIdEquipeOp(idEquipeOperacao);
    }


    /**
     * 
     * @param idEquipeOperacao
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByIdEquipeOperacao(Long idEquipeOperacao, FindDefinition findDefinition) {
    	ResultSetPage rsp = getIntegranteEqOperacDAO().findPaginatedByIdEquipeOperacao(idEquipeOperacao, findDefinition);
    	rsp.setList(new AliasToNestedBeanResultTransformer(IntegranteEqOperac.class).transformListResult(rsp.getList()));
    	return rsp;
    }

    /**
     * 
     * @param idEquipeOperacao
     * @return
     */
    public Integer getRowCountByIdEquipeOperacao(Long idEquipeOperacao) {
    	return getIntegranteEqOperacDAO().getRowCountByIdEquipeOperacao(idEquipeOperacao);
    }
    
    
    public java.io.Serializable storeByControleCarga(IntegranteEqOperac bean) {
    	Long idPessoa = bean.getPessoa() != null ? bean.getPessoa().getIdPessoa() : null;
    	Long idUsuario = bean.getUsuario() != null ? bean.getUsuario().getIdUsuario() : null;
    	controleCargaService.validateIntegranteEmEquipesComControleCarga(
    			bean.getEquipeOperacao().getControleCarga().getIdControleCarga(), idPessoa, idUsuario);
        return super.store(bean);
    }
    
    
    /**
     * 
     * @param ids
     */
    public void removeIntegranteEqOperacaoByControleCarga(List ids) {
    	if (!ids.isEmpty()) {
    		IntegranteEqOperac ieo = findById( (Long)ids.get(0) );
    		Long idEquipeOperacao = ieo.getEquipeOperacao().getIdEquipeOperacao();
        	removeByIds(ids);
        	getIntegranteEqOperacDAO().getAdsmHibernateTemplate().flush();

        	if (getRowCountByIdEquipeOperacao(idEquipeOperacao).intValue() == 0) {
        		throw new BusinessException("LMS-05101");
        	}
    	}
    }
}
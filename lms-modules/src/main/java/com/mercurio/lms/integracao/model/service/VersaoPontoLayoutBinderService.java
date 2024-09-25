package com.mercurio.lms.integracao.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.integracao.model.VersaoPontoLayoutBinder;
import com.mercurio.lms.integracao.model.dao.VersaoPontoLayoutBinderDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.integracao.versaoPontoLayoutBinderService" 					
 */
public class VersaoPontoLayoutBinderService extends CrudService {
	
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ParametroFilialService parametroFilialService;
	
	@SuppressWarnings("unchecked")
	public HashMap findById(Serializable id) {
		return getVersaoPontoLayoutBinderDAO().findById(id);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupPontoLayoutBinder(Map criteria) {
		return  getVersaoPontoLayoutBinderDAO().findLookupPontoLayoutBinder(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupConteudoParametroFilial(Map criteria) {
		return conteudoParametroFilialService.findLookup(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupParametroFilial(Map criteria) {
		return parametroFilialService.findLookup(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rp =  getVersaoPontoLayoutBinderDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		return rp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer getRowCount(Map criteria) {	
		return getVersaoPontoLayoutBinderDAO().getRowCount(criteria);
	}
	
	
	/**
	 * Recupera uma inst�ncia de <code>DominioVinculoIntegracao</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws //
	 *             TODO: documentar a exce��o
	 */
	public VersaoPontoLayoutBinder findById(java.lang.Long id) {
		return (VersaoPontoLayoutBinder)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 * 
	 *
	 */
	@SuppressWarnings("unchecked")
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	private void validateBusinessRules(VersaoPontoLayoutBinder bean) {
		if(bean.getPontoLayoutBinder()==null && bean.getGrupoLayoutBinder() == null){
			throw new BusinessException("INT_VPLB_001");
		}
		if(bean.getParametroFilialInicio()==null){
			throw new BusinessException("INT_VPLB_002");
		}
		if(bean.getParametroFilialFim()==null){
			throw new BusinessException("INT_VPLB_003");
		}
		
	}	
	
	public Serializable store(VersaoPontoLayoutBinder bean) {
		validateBusinessRules(bean);
		return super.store(bean);
	}
	
	
	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVersaoPontoLayoutBinderDAO(VersaoPontoLayoutBinderDAO dao) {
        setDao(dao);
    }
	/**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos 
     * dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    public VersaoPontoLayoutBinderDAO getVersaoPontoLayoutBinderDAO() {
        return (VersaoPontoLayoutBinderDAO)getDao();
    }    

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public ParametroFilialService getParametroFilialService() {
		return parametroFilialService;
	}

	public void setParametroFilialService(
			ParametroFilialService parametroFilialService) {
		this.parametroFilialService = parametroFilialService;
	}
	
}

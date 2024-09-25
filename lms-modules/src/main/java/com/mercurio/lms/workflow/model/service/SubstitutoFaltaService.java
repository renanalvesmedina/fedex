package com.mercurio.lms.workflow.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.SubstitutoFalta;
import com.mercurio.lms.workflow.model.dao.SubstitutoFaltaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.substitutoFaltaService"
 */
public class SubstitutoFaltaService extends CrudService<SubstitutoFalta, Long> {

	/**
	 * M�todo override invocado antes do store
	 * @param Object bean
	 * @return Object 
	 */
	public SubstitutoFalta beforeStore(SubstitutoFalta bean) {
		final SubstitutoFalta substitutoFalta = ((SubstitutoFalta)bean);
		//Se a substitucaoFalta � ativa
		if (substitutoFalta.getTpSituacao().getValue().equals("A")) {
			/** Invoca o m�todo respons�vel pela valida��o do Substitutopor falta */
			this.validateSubstitutoFalta(substitutoFalta.getIntegrante().getIdIntegrante(),
					substitutoFalta.getUsuario(), substitutoFalta.getPerfil(),
						substitutoFalta.getIdSubstitutoFalta());
		}
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma inst�ncia de <code>SubstitutoFalta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public SubstitutoFalta findById(java.lang.Long id) {
        return (SubstitutoFalta)super.findById(id);
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
    public java.io.Serializable store(SubstitutoFalta bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna os substitutos falta de uma a��o que n�o tem substituto falta a��o cadastrado.]
     * 
     * @param Long idIntegrante
     * @param Long idAcao
     * @return List
     * */
    public List findSubstitutoFaltaByIntegrante(Long idIntegrante, Long idAcao){
    	return this.getSubstitutoFaltaDAO().findSubstitutoFaltaByIntegrante(idIntegrante, idAcao);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setSubstitutoFaltaDAO(SubstitutoFaltaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private SubstitutoFaltaDAO getSubstitutoFaltaDAO() {
        return (SubstitutoFaltaDAO) getDao();
    }
    
    /**
     * M�todo respons�vel pela valida��o no momento da inclus�o ou da edi��o de um SubstitutoFalta
     * @param idIntegrante
     * @param usuario
     * @param perfil
     * @return void
     */
    public void validateSubstitutoFalta(Long idIntegrante, Usuario usuario, Perfil perfil, Long idSubstitutoFalta){
    	
    	/** Contadores de Substitutos por falta do tipo Perfil e do tipo Usuario para o integrante em quest�o */
    	int countUsuario = 0;
    	int countPerfil = 0;
    	
    	/** Invoca o m�todo que realiza a pesquisa no banco */
    	List list = this.getSubstitutoFaltaDAO().findSubstitutoFaltaByIntegrante(idIntegrante);
    	
    	/** Itera o resultado da pesquisa para classificar o substituto por Usuario ou por Perfil */
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		SubstitutoFalta sf = (SubstitutoFalta)iter.next();
    		
    		//Se a situa��o do substitutoFalta � ativa
    		if (sf.getTpSituacao().getValue().equals("A")){
	    		if(sf.getPerfil() != null){
	    			countPerfil++;
	    		}else{
	    			countUsuario++;
	    		}
    		}
    		getSubstitutoFaltaDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(sf);
    	}
    	
    	
    	
    	/*####### VALIDA A REGRA DE NEG�CIO INSERT/UPDATE SubstitutoFalta #######*/
    	
    	/** Se idSubstitutoFalta for diferente de null, ser� realixado UPDATE na SubstitutoFalta */
    	if(idSubstitutoFalta != null){
    		
    		if(perfil != null){
    			
    			/** O integrante em quest�o poder� ter um substituto do tipo Perfil, ou um substituto do tipo Usuario na base,
    			 *  logo se countUsuario for >= 1,ser� emitido um alerta informando que esta n�o
    			 *  � uma opera��o v�lida 
    			 */
    			if(countUsuario > 1){
    				//alerta adequado a esta situa��o
    				throw new BusinessException("LMS-39015");
    			}
    			
    		}
    		
    	/** Sen�o ser� realizado INSERT na SubstitutoFalta */	
    	}else{
    		
    		/** Se o Substituto � do tipo Uu�rio */
    		if(usuario != null){
    			
    			/** O integrante em quest�o n�o poder� ter nenhum substituto do tipo Perfil na base, logo se countPerfil for != 0,
    			 *  ser� emitido um alerta informando que esta n�o � uma opera��o v�lida 
    			 */
    			if(countPerfil != 0){
    				//alerta adequado a esta situa��o
    				throw new BusinessException("LMS-39015");
    			}
    			
    		/** Se o Substituto � do tipo Perfil */	
    		}else{
    			
    			/** O integrante em quest�o n�o poder� ter nenhum substituto do tipo Perfil, e nenhum substituo do tipo Usuario na base,
    			 *  logo countPerfil for != 0 ou countUsuario for != 0, ser� emitido um alerta informando que esta n�o � 
    			 *  uma opera��o v�lida 
    			 */
    			if(countPerfil != 0){
    				//alerta adequado a esta situa��o
    				throw new BusinessException("LMS-39014");
    			}else if( countUsuario != 0){
    				//alerta adequado a esta situa��o
    				throw new BusinessException("LMS-39015");
    			}
    			
    		}
    		
    	}
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria){
    	return getSubstitutoFaltaDAO().findPaginated(criteria);
    }
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getSubstitutoFaltaDAO().getRowCount(criteria);
    }    
}
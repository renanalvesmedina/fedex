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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.substitutoFaltaService"
 */
public class SubstitutoFaltaService extends CrudService<SubstitutoFalta, Long> {

	/**
	 * Método override invocado antes do store
	 * @param Object bean
	 * @return Object 
	 */
	public SubstitutoFalta beforeStore(SubstitutoFalta bean) {
		final SubstitutoFalta substitutoFalta = ((SubstitutoFalta)bean);
		//Se a substitucaoFalta é ativa
		if (substitutoFalta.getTpSituacao().getValue().equals("A")) {
			/** Invoca o método responsável pela validação do Substitutopor falta */
			this.validateSubstitutoFalta(substitutoFalta.getIntegrante().getIdIntegrante(),
					substitutoFalta.getUsuario(), substitutoFalta.getPerfil(),
						substitutoFalta.getIdSubstitutoFalta());
		}
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>SubstitutoFalta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public SubstitutoFalta findById(java.lang.Long id) {
        return (SubstitutoFalta)super.findById(id);
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
    public java.io.Serializable store(SubstitutoFalta bean) {
        return super.store(bean);
    }
    
    /**
     * Retorna os substitutos falta de uma ação que não tem substituto falta ação cadastrado.]
     * 
     * @param Long idIntegrante
     * @param Long idAcao
     * @return List
     * */
    public List findSubstitutoFaltaByIntegrante(Long idIntegrante, Long idAcao){
    	return this.getSubstitutoFaltaDAO().findSubstitutoFaltaByIntegrante(idIntegrante, idAcao);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSubstitutoFaltaDAO(SubstitutoFaltaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SubstitutoFaltaDAO getSubstitutoFaltaDAO() {
        return (SubstitutoFaltaDAO) getDao();
    }
    
    /**
     * Método responsável pela validação no momento da inclusão ou da edição de um SubstitutoFalta
     * @param idIntegrante
     * @param usuario
     * @param perfil
     * @return void
     */
    public void validateSubstitutoFalta(Long idIntegrante, Usuario usuario, Perfil perfil, Long idSubstitutoFalta){
    	
    	/** Contadores de Substitutos por falta do tipo Perfil e do tipo Usuario para o integrante em questão */
    	int countUsuario = 0;
    	int countPerfil = 0;
    	
    	/** Invoca o método que realiza a pesquisa no banco */
    	List list = this.getSubstitutoFaltaDAO().findSubstitutoFaltaByIntegrante(idIntegrante);
    	
    	/** Itera o resultado da pesquisa para classificar o substituto por Usuario ou por Perfil */
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		SubstitutoFalta sf = (SubstitutoFalta)iter.next();
    		
    		//Se a situação do substitutoFalta é ativa
    		if (sf.getTpSituacao().getValue().equals("A")){
	    		if(sf.getPerfil() != null){
	    			countPerfil++;
	    		}else{
	    			countUsuario++;
	    		}
    		}
    		getSubstitutoFaltaDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(sf);
    	}
    	
    	
    	
    	/*####### VALIDA A REGRA DE NEGÓCIO INSERT/UPDATE SubstitutoFalta #######*/
    	
    	/** Se idSubstitutoFalta for diferente de null, será realixado UPDATE na SubstitutoFalta */
    	if(idSubstitutoFalta != null){
    		
    		if(perfil != null){
    			
    			/** O integrante em questão poderá ter um substituto do tipo Perfil, ou um substituto do tipo Usuario na base,
    			 *  logo se countUsuario for >= 1,será emitido um alerta informando que esta não
    			 *  é uma operação válida 
    			 */
    			if(countUsuario > 1){
    				//alerta adequado a esta situação
    				throw new BusinessException("LMS-39015");
    			}
    			
    		}
    		
    	/** Senão será realizado INSERT na SubstitutoFalta */	
    	}else{
    		
    		/** Se o Substituto é do tipo Uuário */
    		if(usuario != null){
    			
    			/** O integrante em questão não poderá ter nenhum substituto do tipo Perfil na base, logo se countPerfil for != 0,
    			 *  será emitido um alerta informando que esta não é uma operação válida 
    			 */
    			if(countPerfil != 0){
    				//alerta adequado a esta situação
    				throw new BusinessException("LMS-39015");
    			}
    			
    		/** Se o Substituto é do tipo Perfil */	
    		}else{
    			
    			/** O integrante em questão não poderá ter nenhum substituto do tipo Perfil, e nenhum substituo do tipo Usuario na base,
    			 *  logo countPerfil for != 0 ou countUsuario for != 0, será emitido um alerta informando que esta não é 
    			 *  uma operação válida 
    			 */
    			if(countPerfil != 0){
    				//alerta adequado a esta situação
    				throw new BusinessException("LMS-39014");
    			}else if( countUsuario != 0){
    				//alerta adequado a esta situação
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
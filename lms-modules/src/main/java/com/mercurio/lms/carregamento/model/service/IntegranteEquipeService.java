package com.mercurio.lms.carregamento.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;
import com.mercurio.lms.carregamento.model.dao.IntegranteEquipeDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.integranteEquipeService"
 */
public class IntegranteEquipeService extends CrudService<IntegranteEquipe, Long> {


	/**
	 * Recupera uma instância de <code>IntegranteEquipe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public IntegranteEquipe findById(java.lang.Long id) {
        return (IntegranteEquipe)super.findById(id);
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
	 * Procura todos os municipios de destino atraves de uma equipe
	 *
	 * @param id indica a entidade equipe
	 */
	public void removeByIdEquipe(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("equipe.idEquipe", id);		
    	List integranteEquipes = find(criteria);
    	for (Iterator iter = integranteEquipes.iterator(); iter.hasNext();) {
     		removeById( ((IntegranteEquipe)iter.next()).getIdIntegranteEquipe() );    		
    	}
	}
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IntegranteEquipe bean) {
        return super.store(bean);
    }


    
    
    /**
     * Busca Integrante da equipe atraves do id do usuário.
     * @return
     */
    public IntegranteEquipe findByUsuario(Long idUsuario){
    	
    	return getIntegranteEquipeDAO().findByUsuario(idUsuario);
    	
    }
    
    
    
        
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setIntegranteEquipeDAO(IntegranteEquipeDAO dao) {
        setDao( dao );
    }
    
	public Integer getRowCountIntegranteEquipe(Long masterId) {
		return getIntegranteEquipeDAO().getRowCountIntegranteEquipe(masterId);
	}
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private IntegranteEquipeDAO getIntegranteEquipeDAO() {
        return (IntegranteEquipeDAO) getDao();
    }
	
   }
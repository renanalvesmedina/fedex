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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.integranteEquipeService"
 */
public class IntegranteEquipeService extends CrudService<IntegranteEquipe, Long> {


	/**
	 * Recupera uma inst�ncia de <code>IntegranteEquipe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public IntegranteEquipe findById(java.lang.Long id) {
        return (IntegranteEquipe)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IntegranteEquipe bean) {
        return super.store(bean);
    }


    
    
    /**
     * Busca Integrante da equipe atraves do id do usu�rio.
     * @return
     */
    public IntegranteEquipe findByUsuario(Long idUsuario){
    	
    	return getIntegranteEquipeDAO().findByUsuario(idUsuario);
    	
    }
    
    
    
        
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIntegranteEquipeDAO(IntegranteEquipeDAO dao) {
        setDao( dao );
    }
    
	public Integer getRowCountIntegranteEquipe(Long masterId) {
		return getIntegranteEquipeDAO().getRowCountIntegranteEquipe(masterId);
	}
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IntegranteEquipeDAO getIntegranteEquipeDAO() {
        return (IntegranteEquipeDAO) getDao();
    }
	
   }
package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.coleta.model.MilkRemetente;
import com.mercurio.lms.coleta.model.dao.MilkRemetenteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.milkRemetenteService"
 */
public class MilkRemetenteService extends CrudService<MilkRemetente, Long> {

	
	private MilkRunService milkRunService;

	/**
	 * Recupera uma instância de <code>MilkRemetente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MilkRemetente findById(java.lang.Long id) {
        return (MilkRemetente)super.findById(id);
    }

	public List findByIdMilkRun(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("milkRun.idMilkRun", id);
    	List milkRemetentes = find(criteria);
    	return milkRemetentes;
	}
	
	public List findMilkRemetente(Long idMilkRun) {
		List listMilkRemetente = new ArrayList();
		List listMilks = this.getMilkRemetenteDAO().findMilkRemetenteByIdMilkRun(idMilkRun);
		
		for(int i=0; i < listMilks.size(); i++) {
			MilkRemetente milkRemetente = (MilkRemetente) listMilks.get(i);
			Hibernate.initialize(milkRemetente.getSemanaRemetMruns());

			listMilkRemetente.add(i, milkRemetente);
		}
		
		return listMilkRemetente;
	}
	
	public Integer getRowCountMilkRemetente(Long idMilkRun) {
		return this.getMilkRemetenteDAO().getRowCountMilkRemetenteByIdMilkRun(idMilkRun);
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
     * Deleta MilkRemetente com o ID do MilkRun
     * 
     * @param idMilkRun
     */
    public void removeByIdMilkRun(Long idMilkRun) {
    	this.getMilkRemetenteDAO().removeByIdMilkRun(idMilkRun);
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
    public java.io.Serializable store(MilkRemetente bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMilkRemetenteDAO(MilkRemetenteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MilkRemetenteDAO getMilkRemetenteDAO() {
        return (MilkRemetenteDAO) getDao();
    }

	public MilkRunService getMilkRunService() {
		return milkRunService;
	}

	public void setMilkRunService(MilkRunService milkRunService) {
		this.milkRunService = milkRunService;
	}
 
}
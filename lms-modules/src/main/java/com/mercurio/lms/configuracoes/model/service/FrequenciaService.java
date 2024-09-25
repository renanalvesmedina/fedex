package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Frequencia;
import com.mercurio.lms.configuracoes.model.dao.FrequenciaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.frequenciaService"
 */
public class FrequenciaService extends CrudService<Frequencia, Long> {


	/**
	 * Recupera uma instância de <code>Frequencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public Frequencia findById(java.lang.Long id) {
        return (Frequencia)super.findById(id);
    }
    
    public List find(Map criteria) {
    	ArrayList orderBy = new ArrayList(1);
    	orderBy.add("dsFrequencia");
        return this.getFrequenciaDAO().findListByCriteria(criteria,orderBy);   
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
    public java.io.Serializable store(Frequencia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFrequenciaDAO(FrequenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FrequenciaDAO getFrequenciaDAO() {
        return (FrequenciaDAO) getDao();
    }
    
    public List findAtivas(Map map){
    	ArrayList orderBy = new ArrayList(1);
    	orderBy.add("dsFrequencia");
    	if( map == null ){
    		map = new HashMap();
    	}
    	map.put("tpSituacao","A");
        return this.getFrequenciaDAO().findListByCriteria(map,orderBy); 
    }
   }
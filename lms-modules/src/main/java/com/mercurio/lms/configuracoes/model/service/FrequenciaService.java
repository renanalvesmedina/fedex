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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.frequenciaService"
 */
public class FrequenciaService extends CrudService<Frequencia, Long> {


	/**
	 * Recupera uma inst�ncia de <code>Frequencia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
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
    public java.io.Serializable store(Frequencia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setFrequenciaDAO(FrequenciaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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
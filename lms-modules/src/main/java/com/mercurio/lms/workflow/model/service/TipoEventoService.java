package com.mercurio.lms.workflow.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.workflow.model.TipoEvento;
import com.mercurio.lms.workflow.model.dao.TipoEventoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.tipoEventoService"
 */
public class TipoEventoService extends CrudService<TipoEvento, Long> {


	/**
	 * Recupera uma inst�ncia de <code>TipoEvento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public TipoEvento findById(java.lang.Long id) {
        return (TipoEvento)super.findById(id);
    }
    

    /**
     * Retorna uma lista de Tipo de Evento por id informado
     * 
     * @param List ids
     * @return List
     * 
	 *
     * */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public List findByIds(List ids) {
        return (List)this.getTipoEventoDAO().findByIds(ids);
    }    

    
    /**
     * Retorna os tipos de eventos e os eventos a partir 
     * dos filtros informado.
     * 
     * @param Map map
     * @return List
     * */
    public List findLookupWithEvento(Map map) {
    	return this.getTipoEventoDAO().findLookupWithEvento(map);
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
    public java.io.Serializable store(TipoEvento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoEventoDAO(TipoEventoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoEventoDAO getTipoEventoDAO() {
        return (TipoEventoDAO) getDao();
    }


	public List<TipoEvento> findTipoEventoCombo(List<Short> listNrEventos) {
		// TODO Auto-generated method stub
		return getTipoEventoDAO().findTipoEventoCombo(listNrEventos);
	}
   }
package com.mercurio.lms.workflow.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.workflow.model.TipoEvento;
import com.mercurio.lms.workflow.model.dao.TipoEventoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.workflow.tipoEventoService"
 */
public class TipoEventoService extends CrudService<TipoEvento, Long> {


	/**
	 * Recupera uma instância de <code>TipoEvento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
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
    public java.io.Serializable store(TipoEvento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoEventoDAO(TipoEventoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoEventoDAO getTipoEventoDAO() {
        return (TipoEventoDAO) getDao();
    }


	public List<TipoEvento> findTipoEventoCombo(List<Short> listNrEventos) {
		// TODO Auto-generated method stub
		return getTipoEventoDAO().findTipoEventoCombo(listNrEventos);
	}
   }
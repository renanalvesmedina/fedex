package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.ObservacaoCiaAerea;
import com.mercurio.lms.municipios.model.dao.ObservacaoCiaAereaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.observacaoCiaAereaService"
 */
public class ObservacaoCiaAereaService extends CrudService<ObservacaoCiaAerea, Long> {


	/**
	 * Recupera uma instância de <code>ObservacaoCiaAerea</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ObservacaoCiaAerea findById(java.lang.Long id) {
        return (ObservacaoCiaAerea)super.findById(id);
    }

    /**
     * Retorna apenas as observacoes ordenadas pela descrição.
     * @param criteria
     * @return
     */
    public List findByOrder(Map criteria) {
    	List listaOrder = new ArrayList();
		listaOrder.add("dsObservacaoCiaAerea:asc");
		return getObservacaoCiaAereaDAO().findListByCriteria(criteria,listaOrder);
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
    public java.io.Serializable store(ObservacaoCiaAerea bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setObservacaoCiaAereaDAO(ObservacaoCiaAereaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ObservacaoCiaAereaDAO getObservacaoCiaAereaDAO() {
        return (ObservacaoCiaAereaDAO) getDao();
    }
   }
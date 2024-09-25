package com.mercurio.lms.vol.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vol.model.VolModeloseqps;
import com.mercurio.lms.vol.model.dao.VolModeloseqpsDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volModeloseqpsService"
 */
public class VolModeloseqpsService extends CrudService<VolModeloseqps, Long> {

	private VolTiposEqptoService volTiposEqptoService;

	
	/**
	 * Recupera uma instância de <code>VolModeloseqps</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolModeloseqps findById(java.lang.Long id) {
        return (VolModeloseqps)super.findById(id);
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
    public java.io.Serializable store(VolModeloseqps bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolModeloseqpsDAO(VolModeloseqpsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolModeloseqpsDAO getVolModeloseqpsDAO() {
        return (VolModeloseqpsDAO) getDao();
    }
	
	
	/**
	 * FindPaginated para a grid de modelos
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedModelos(TypedFlatMap criteria) {
		return getVolModeloseqpsDAO().findPaginatedModelos(criteria,FindDefinition.createFindDefinition(criteria));
	}
	               
	/**
	 * RowCount para a grid de modelos
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountModelos(TypedFlatMap criteria) {
		return getVolModeloseqpsDAO().getRowCountModelos(criteria);
	}
	
	public VolTiposEqptoService getVolTiposEqptoService() {
		return volTiposEqptoService;
	}

	public void setVolTiposEqptoService(VolTiposEqptoService volTiposEqptoService) {
		this.volTiposEqptoService = volTiposEqptoService;
	}

   }
	
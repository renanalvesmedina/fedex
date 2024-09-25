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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.volModeloseqpsService"
 */
public class VolModeloseqpsService extends CrudService<VolModeloseqps, Long> {

	private VolTiposEqptoService volTiposEqptoService;

	
	/**
	 * Recupera uma inst�ncia de <code>VolModeloseqps</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolModeloseqps findById(java.lang.Long id) {
        return (VolModeloseqps)super.findById(id);
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
    public java.io.Serializable store(VolModeloseqps bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolModeloseqpsDAO(VolModeloseqpsDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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
	
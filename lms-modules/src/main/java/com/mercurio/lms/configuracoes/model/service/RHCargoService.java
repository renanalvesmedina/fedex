package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.RHCargo;
import com.mercurio.lms.configuracoes.model.dao.RHCargoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.RHCargoService"
 */
public class RHCargoService extends CrudService<RHCargo, Long> {


	public ResultSetPage findPaginated(Map criteria) {
		criteria.put("codigo",criteria.get("idCodigo"));
		criteria.remove("idCodigo");
		return super.findPaginated(criteria);
	}
	
	public Integer getRowCount(Map criteria) {
		criteria.remove("idCodigo");
		return super.getRowCount(criteria);
	}

	/**
	 * Recupera uma inst�ncia de <code>Funcionario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado. 
	 */
    public RHCargo findById(java.lang.Long id) {
        return (RHCargo)super.findById(id);
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
    public java.io.Serializable store(RHCargo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRHCargoDAO(RHCargoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RHCargoDAO getRHCargoDAO() {
        return (RHCargoDAO) getDao();
    }

	public List findLookup(Map criteria) {
		criteria.put("codigo",criteria.get("idCodigo"));
		criteria.remove("idCodigo");
		return super.findLookup(criteria);
	}
	
	/**
     * Retorna os cargos ordenados pelo nome.
     * @author Andr�sa Vargas
     * @param criteria
     * @return
     */
    public List findByOrder(Map criteria) {
    	List listaOrder = new ArrayList();
		listaOrder.add("nome:asc");
		return getRHCargoDAO().findListByCriteria(criteria,listaOrder);
    }

	public List findCargoMotoristaInstrutor(TypedFlatMap criteria) {
		return getRHCargoDAO().findCargoMotoristaInstrutor(criteria);
	}


    
}
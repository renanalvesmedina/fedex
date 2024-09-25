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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
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
	 * Recupera uma instância de <code>Funcionario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado. 
	 */
    public RHCargo findById(java.lang.Long id) {
        return (RHCargo)super.findById(id);
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
    public java.io.Serializable store(RHCargo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRHCargoDAO(RHCargoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
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
     * @author Andrêsa Vargas
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
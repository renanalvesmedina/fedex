package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.dao.TipoDocumentoEntregaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.tipoDocumentoEntregaService"
 */
public class TipoDocumentoEntregaService extends CrudService<TipoDocumentoEntrega, Long> {


	/**
	 * Recupera uma instância de <code>TipoDocumentoEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TipoDocumentoEntrega findById(java.lang.Long id) {
        return (TipoDocumentoEntrega)super.findById(id);
    }
    
    public List find(Map criteria) {
    	List orderBy = new ArrayList();
    	orderBy.add("dsTipoDocumentoEntrega");
    	return this.getTipoDocumentoEntregaDAO().findListByCriteria(criteria, orderBy);   
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
    public java.io.Serializable store(TipoDocumentoEntrega bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoDocumentoEntregaDAO(TipoDocumentoEntregaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoDocumentoEntregaDAO getTipoDocumentoEntregaDAO() {
        return (TipoDocumentoEntregaDAO) getDao();
    }
   }
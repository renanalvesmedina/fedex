package com.mercurio.lms.sgr.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.EventoSMP;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.dao.EventoSmpDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.EventoSMPService"
 */
public class EventoSMPService extends CrudService<EventoSMP, Long> {

	
	/**
	 * Recupera uma instância de <code>EventoSMP</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EventoSMP findById(java.lang.Long id) {
        return (EventoSMP)super.findById(id);
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
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(EventoSMP bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoSmpDAO(EventoSmpDAO dao) {
        setDao(dao);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoSmpDAO getEventoSmpDAO() {
        return (EventoSmpDAO) getDao();
    }
    
    
    /**
	 * LMS-7906 - Busca quantidade de {@link EventoSMP}s relacionados a
	 * determinado {@link SolicMonitPreventivo}.
	 * 
	 * @param criteria
	 *            Filtro para busca incluindo {@code idSolicMonitPreventivo}.
	 * @return Quantidade de {@link EventoSMP}s.
	 */
	public Integer getRowCountEventoSMP(TypedFlatMap criteria) {
		Long idSolicMonitPreventivo = criteria.getLong("idSolicMonitPreventivo");
		return this.getEventoSmpDAO().getRowCountEventoSMP(idSolicMonitPreventivo);
	}

	/**
	 * LMS-7906 - Busca página de {@link EventoSMP}s relacionados a determinado
	 * {@link SolicMonitPreventivo}.
	 * 
	 * @param criteria
	 *            Filtro para busca incluindo {@code idSolicMonitPreventivo}.
	 * @param findDefinition
	 *            Parâmetros para consulta e paginação.
	 * @return Página de {@link EventoSMP}s.
	 */
	public ResultSetPage<EventoSMP> findPaginatedEventoSMP(TypedFlatMap criteria, FindDefinition findDefinition) {
		Long idSolicMonitPreventivo = criteria.getLong("idSolicMonitPreventivo");
    	return this.getEventoSmpDAO().findPaginatedEventoSMP(idSolicMonitPreventivo, findDefinition);
	}
    
    
	
}
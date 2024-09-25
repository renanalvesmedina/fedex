package com.mercurio.lms.sgr.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sgr.model.EventoSMP;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoSmpDAO extends BaseCrudDao<EventoSMP, Long>{
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("solicMonitPreventivo", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class<EventoSMP> getPersistentClass() {
        return EventoSMP.class;
    }

    /**
     * Persiste uma coleção de EventoSMP
     * @param EventoSMP
     * @author RomuloPanassolo
     */
    public void storeAll(List<EventoSMP> EventoSMP) {
    	getAdsmHibernateTemplate().saveOrUpdateAll(EventoSMP);
    }
    
    /**
	 * LMS-7906 - Busca quantidade de {@link EventoSMP}s relacionados a
	 * determinado {@link SolicMonitPreventivo}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id do {@link SolicMonitPreventivo}.
	 * @return Quantidade de {@link EventoSMP}s.
	 * 
	 * @see #makeQueryEventoSMP()
	 * @see #makeParametersEventoSMP(Long)
	 */
	public Integer getRowCountEventoSMP(Long idSolicMonitPreventivo) {
		return getAdsmHibernateTemplate().getRowCountForQuery(
				makeQueryEventoSMP(),
				makeParametersEventoSMP(idSolicMonitPreventivo));
	}

	/**
	 * LMS-7906 - Busca página de {@link EventoSMP}s relacionados a determinado
	 * {@link SolicMonitPreventivo}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id do {@link SolicMonitPreventivo}.
	 * @param findDefinition
	 *            Parâmetros para consulta e paginação.
	 * @return Página de {@link EventoSMP}s.
	 * 
	 * @see #makeQueryEventoSMP()
	 * @see #makeParametersEventoSMP(Long)
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<EventoSMP> findPaginatedEventoSMP(Long idSolicMonitPreventivo, FindDefinition findDefinition) {
		return getAdsmHibernateTemplate().findPaginated(
				makeQueryEventoSMP(),
				findDefinition.getCurrentPage(),
				findDefinition.getPageSize(),
				makeParametersEventoSMP(idSolicMonitPreventivo));
	}

	/**
	 * LMS-7906 - Prepara query HQL para busca de {@link EventoSMP}s
	 * relacionados a determinado {@link SolicMonitPreventivo} pelo parâmetro
	 * {@code idSolicMonitPreventivo}.
	 * 
	 * @return Query HQL para busca de {@link EventoSMP}s incluindo parâmetro
	 *         {@code idSolicMonitPreventivo}.
	 */
	private String makeQueryEventoSMP() {
		StringBuilder hql = new StringBuilder()
				.append("SELECT e ")
				.append("FROM SolicMonitPreventivo smp ")
				.append("JOIN smp.eventos e ")
				.append("WHERE smp.idSolicMonitPreventivo = :idSolicMonitPreventivo ")
				.append("ORDER BY e.dhEvento.value DESC");
		return hql.toString();
	}

	/**
	 * LMS-7906 - Prepara {@link Map} para busca parametrizada por
	 * {@code idSolicMonitPreventivo}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id de {@link SolicMonitPreventivo}.
	 * @return {@link Map} contendo {@code idSolicMonitPreventivo}.
	 */
	private Map<String, Object> makeParametersEventoSMP(Long idSolicMonitPreventivo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idSolicMonitPreventivo", idSolicMonitPreventivo);
		return parameters;
	}

    

}
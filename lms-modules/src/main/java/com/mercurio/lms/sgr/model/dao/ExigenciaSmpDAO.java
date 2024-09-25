package com.mercurio.lms.sgr.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sgr.model.ExigenciaSmp;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ExigenciaSmpDAO extends BaseCrudDao<ExigenciaSmp, Long>{
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("exigenciaGerRisco", FetchMode.JOIN);
		lazyFindById.put("exigenciaGerRisco.tipoExigenciaGerRisco", FetchMode.JOIN);
		lazyFindById.put("filialInicio", FetchMode.JOIN);
		lazyFindById.put("filialInicio.pessoa", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ExigenciaSmp.class;
    }

    /**
     * Persiste uma coleção de ExigenciaSmp
     * @param exigenciasSMP
     * @author luisfco
     */
    public void storeAll(List exigenciasSMP) {
    	getAdsmHibernateTemplate().saveOrUpdateAll(exigenciasSMP);
    }

	/**
	 * LMS-6853 - Busca quantidade de {@link ExigenciaSmp} relacionadas a
	 * determinada {@link SolicMonitPreventivo}. Se o parâmetro
	 * {@code blRestrito} for {@code null} ou {@code true} considera todos os
	 * {@link TipoExigenciaGerRisco}, caso contrário (se igual a {@code false})
	 * restringe acesso aos {@code TipoExigenciaGerRisco} com atributo
	 * {@code blRestrito} de valor {@code true}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id da {@link SolicMonitPreventivo}.
	 * @param blRestrito
	 *            Se {@code false} restringe acesso aos
	 *            {@link TipoExigenciaGerRisco} com atributo {@code blRestrito}
	 *            de valor {@code true}.
	 * @return Quantidade de {@link ExigenciaSmp}.
	 * 
	 * @see #makeQueryByIdSmp(Boolean)
	 * @see #makeParametersByIdSmp(Long)
	 */
    public Integer getRowCountExigenciaSmpByIdSmp(Long idSolicMonitPreventivo, Boolean blRestrito) {
    	return getAdsmHibernateTemplate().getRowCountForQuery(
    			makeQueryByIdSmp(blRestrito),
    			makeParametersByIdSmp(idSolicMonitPreventivo));
    }
    
	/**
	 * LMS-6853 - Busca {@link ExigenciaSmp}s relacionadas a determinada
	 * {@link SolicMonitPreventivo}. Se o parâmetro {@code blRestrito} for
	 * {@code null} ou {@code true} considera todos os
	 * {@link TipoExigenciaGerRisco}, caso contrário (se igual a {@code false})
	 * restringe acesso aos {@code TipoExigenciaGerRisco} com atributo
	 * {@code blRestrito} de valor {@code true}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id da {@link SolicMonitPreventivo}.
	 * @param blRestrito
	 *            Se {@code false} restringe acesso aos
	 *            {@link TipoExigenciaGerRisco} com atributo {@code blRestrito}
	 *            de valor {@code true}.
	 * @return Quantidade de {@link ExigenciaSmp}.
	 * 
	 * @see #makeQueryByIdSmp(Boolean)
	 * @see #makeParametersByIdSmp(Long)
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<ExigenciaSmp> findPaginatedExigenciaSmpByIdSmp(
			Long idSolicMonitPreventivo, FindDefinition findDefinition, Boolean blRestrito) {
		return getAdsmHibernateTemplate().findPaginated(
				makeQueryByIdSmp(blRestrito),
				findDefinition.getCurrentPage(),
				findDefinition.getPageSize(),
				makeParametersByIdSmp(idSolicMonitPreventivo));
	}

	/**
	 * LMS-6853 - Prepara query HQL para busca de {@link ExigenciaSmp}
	 * relacionadas a determinada {@link SolicMonitPreventivo} pelo parâmetro
	 * {@code idSolicMonitPreventivo}, restringindo ou não o acesso aos
	 * {@link TipoExigenciaGerRisco} de atributo {@code blRestrito} de valor
	 * {@code true}.
	 * 
	 * @param blRestrito
	 *            Se {@code false} restringe acesso aos
	 *            {@link TipoExigenciaGerRisco} com atributo {@code blRestrito}
	 *            de valor {@code true}.
	 * @return Query HQL para busca de {@link ExigenciaSmp}s incluindo parâmetro
	 *         {@code idSolicMonitPreventivo}.
	 */
	private String makeQueryByIdSmp(Boolean blRestrito) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT exigenciaSmp ")
				.append("FROM ExigenciaSmp exigenciaSmp ")
				.append("JOIN FETCH exigenciaSmp.exigenciaGerRisco exigenciaGerRisco ")
				.append("JOIN FETCH exigenciaGerRisco.tipoExigenciaGerRisco tipoExigenciaGerRisco ")
				.append("LEFT JOIN FETCH exigenciaSmp.filialInicio filialInicio ")
				.append("LEFT JOIN FETCH filialInicio.pessoa pessoa ")
				.append("WHERE exigenciaSmp.solicMonitPreventivo.idSolicMonitPreventivo = :idSolicMonitPreventivo ");
		if (BooleanUtils.isFalse(blRestrito)) {
			hql.append("AND tipoExigenciaGerRisco.blRestrito = 'N' ");
		}
		hql.append("ORDER BY VI18N(tipoExigenciaGerRisco.dsTipoExigenciaGerRisco), VI18N(exigenciaGerRisco.dsResumida) ");
		return hql.toString();
	}

	/**
	 * LMS-6853 - Prepara {@link Map} para busca parametrizada por
	 * {@code idSolicMonitPreventivo}.
	 * 
	 * @param idSolicMonitPreventivo
	 *            Id de {@link SolicMonitPreventivo}.
	 * @return {@link Map} contendo {@code idSolicMonitPreventivo}.
	 */
	private Map<String, Object> makeParametersByIdSmp(Long idSolicMonitPreventivo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idSolicMonitPreventivo", idSolicMonitPreventivo);
		return parameters;
	}

	/**
	 * Remove a Exigência de SMP pelo id da SMP
	 * @param idSolicMonitPreventivo
	 */
	public void removeByIdSolicMonitPreventivo(Long idSolicMonitPreventivo) {
		String s = "delete from " + ExigenciaSmp.class.getName() + " as es " +
			" where es.id in (select esmp.id from " + 
			ExigenciaSmp.class.getName()+ 
			" esmp where esmp.solicMonitPreventivo.id = ?) ";
		
		getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s).setParameter(0, idSolicMonitPreventivo).executeUpdate();
	}

}
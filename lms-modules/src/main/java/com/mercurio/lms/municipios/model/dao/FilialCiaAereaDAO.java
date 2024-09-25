package com.mercurio.lms.municipios.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialCiaAereaDAO extends BaseCrudDao<FilialCiaAerea, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FilialCiaAerea.class;
	}

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("empresa.pessoa",FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchMode) {
		fetchMode.put("pessoa",FetchMode.JOIN);
		fetchMode.put("aeroporto",FetchMode.JOIN);
		fetchMode.put("aeroporto.pessoa",FetchMode.JOIN);
		fetchMode.put("empresa",FetchMode.JOIN);
		fetchMode.put("empresa.pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchMode) {
		fetchMode.put("pessoa",FetchMode.JOIN);
		fetchMode.put("aeroporto",FetchMode.JOIN);
		fetchMode.put("aeroporto.pessoa",FetchMode.JOIN);
		fetchMode.put("empresa",FetchMode.JOIN);
		fetchMode.put("empresa.pessoa",FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("aeroporto",FetchMode.JOIN);
		lazyFindLookup.put("aeroporto.pessoa",FetchMode.JOIN);
		lazyFindLookup.put("empresa",FetchMode.JOIN);
		lazyFindLookup.put("empresa.pessoa",FetchMode.JOIN);
	}

	/**
	 * Verifica se a filial de cia aerea esta vigente dentro do perido informado 
	 * @param idFilialCiaAerea
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public boolean verificaFilialCiaAereaVigente(Long idFilialCiaAerea, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());

		dc.add(Restrictions.idEq(idFilialCiaAerea));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}
	
	@SuppressWarnings("unchecked")
	public List<FilialCiaAerea> findFilialCiaAerea(YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idAeroporto, Long idEmpresa, Long idFilialCiaAereaDiferente) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder()
			.append(" SELECT fca ")
			.append(" FROM " + FilialCiaAerea.class.getName() + " fca ")
			.append(" WHERE 1 = 1 ");
		
		if(idAeroporto != null){
			query.append(" AND fca.aeroporto.idAeroporto = :idAeroporto ");
			params.put("idAeroporto", idAeroporto);
		}
		
		if(idEmpresa != null){
			query.append(" AND fca.empresa.idEmpresa = :idEmpresa ");
			params.put("idEmpresa", idEmpresa);
		}
		
		if(idFilialCiaAereaDiferente != null){
			query.append(" AND fca.idFilialCiaAerea <> :idFilialCiaAerea ");
			params.put("idFilialCiaAerea", idFilialCiaAereaDiferente);
		}
		
		if(dtVigenciaFinal != null){
			query.append(" AND ( ");
			query.append(" (fca.dtVigenciaInicial <= :dtVigenciaInicial AND fca.dtVigenciaFinal >= :dtVigenciaInicial) ");
			query.append(" 	OR ");
			query.append(" (fca.dtVigenciaFinal >= :dtVigenciaFinal AND fca.dtVigenciaInicial <= :dtVigenciaFinal) ");
			query.append(" 	OR ");
			query.append(" (fca.dtVigenciaFinal is null AND (fca.dtVigenciaInicial <= :dtVigenciaInicial AND fca.dtVigenciaInicial <= :dtVigenciaFinal)) ");
			query.append(" 	   ) ");
			
			params.put("dtVigenciaInicial", dtVigenciaInicial);
			params.put("dtVigenciaFinal", dtVigenciaFinal);
		} else {
			query.append(" AND ( ");
			query.append(" (fca.dtVigenciaInicial <= :dtVigenciaInicial AND fca.dtVigenciaFinal >= :dtVigenciaInicial) ");
			query.append(" 	OR ");
			query.append(" (fca.dtVigenciaFinal is null ) ");
			query.append(" 	   ) ");
			
			params.put("dtVigenciaInicial", dtVigenciaInicial);
		}
			
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
}
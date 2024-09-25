package com.mercurio.lms.portaria.model.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.lms.portaria.model.Portaria;
import com.mercurio.lms.portaria.model.dao.suggestion.ChegadaColetaEntregaSQLBuilder;
import com.mercurio.lms.portaria.model.dao.suggestion.ChegadaViagemSQLBuilder;
import com.mercurio.lms.portaria.model.dao.suggestion.SaidaColetaEntregaSQLBuilder;
import com.mercurio.lms.portaria.model.dao.suggestion.SaidaViagemSQLBuilder;
import com.mercurio.lms.portaria.model.service.utils.FrotaPlacaChegadaSaidaSuggestUtils;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.dao.ConsultaSQL;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PortariaDAO extends BaseCrudDao<Portaria, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Portaria.class;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("terminal", FetchMode.JOIN);
		lazyFindById.put("terminal.filial", FetchMode.JOIN);
		lazyFindById.put("terminal.pessoa", FetchMode.JOIN);
		lazyFindById.put("terminal.filial.pessoa", FetchMode.JOIN);
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("terminal", FetchMode.JOIN);
		lazyFindPaginated.put("terminal.filial", FetchMode.JOIN);
		lazyFindPaginated.put("terminal.pessoa", FetchMode.JOIN);
	}

	public boolean validateDuplicated(
			Long id,
			YearMonthDay dtVigenciaInicial,
			YearMonthDay dtVigenciaFinal,
			Byte nrPortaria, Long idFilial
	) {
		DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(), id, dtVigenciaInicial, dtVigenciaFinal);
		dc.setProjection(Projections.rowCount());

		dc.createAlias("terminal", "t");
		dc.add(Restrictions.eq("nrPortaria", nrPortaria));
		dc.add(Restrictions.eq("t.filial.id", idFilial));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return result.intValue() > 0;
	}

	public boolean validateDuplicatedPadrao(Long id, Long idFilial) {  	
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.rowCount());
		dc.createAlias("terminal", "t");

		if (id != null) {
			dc.add(Restrictions.ne("id", id));
		}
			
		dc.add(Restrictions.eq("blPadraoFilial", Boolean.TRUE));
		dc.add(Restrictions.eq("t.filial.id", idFilial));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return result.intValue() > 0;
	}

	public List findByFilial(Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();

		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("idPortaria"), "idPortaria")
			.add(Projections.property("dsPortaria"), "dsPortaria")
			.add(Projections.property("blPadraoFilial"), "blPadraoFilial");

		dc.setProjection(pl);

		dc.createAlias("terminal", "t");
		dc.add(Restrictions.eq("t.filial.idFilial", idFilial));
		dc.addOrder(Order.asc("dsPortaria"));
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		Long idFilial = (Long) filter.get("idFilial");
		String placaOuFrota = (String) filter.get("value");
		
		if(StringUtils.isNumeric(placaOuFrota) && FrotaPlacaChegadaSaidaSuggestUtils.isNumeroFrotaValido(placaOuFrota)) {
			placaOuFrota = FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros(placaOuFrota);
			filter.put("value", placaOuFrota);
		}
		
		ConsultaSQL saidaViagemSQL = new SaidaViagemSQLBuilder(idFilial, placaOuFrota).build();
		ConsultaSQL saidaColetaEntregaSQL = new SaidaColetaEntregaSQLBuilder(idFilial, placaOuFrota).build();
		ConsultaSQL chegadaViagemSQL = new ChegadaViagemSQLBuilder(idFilial, placaOuFrota).build();
		ConsultaSQL chegadaColetaEntregaSQL = new ChegadaColetaEntregaSQLBuilder(idFilial, placaOuFrota).build();

		StringBuilder consultaFinal = new StringBuilder();
		consultaFinal.append(saidaViagemSQL.getSql())
		             .append(" UNION ")
		             .append(saidaColetaEntregaSQL.getSql())
		             .append(" UNION ")
		             .append(chegadaColetaEntregaSQL.getSql())
		             .append(" UNION ")
		             .append(chegadaViagemSQL.getSql());
		
		filter.putAll(saidaViagemSQL.getParametros());
		filter.putAll(saidaColetaEntregaSQL.getParametros());
		filter.putAll(chegadaColetaEntregaSQL.getParametros());
		filter.putAll(chegadaViagemSQL.getParametros());
		
		final ConfigureSqlQuery configureSqlQuery = getConfigureSqlQuery();

		return new ResponseSuggest(consultaFinal.toString(), filter, configureSqlQuery);
	}
	
	private ConfigureSqlQuery getConfigureSqlQuery() {
		final ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idControleCarga", Hibernate.LONG);
				sqlQuery.addScalar("dsControleCarga", Hibernate.STRING);
				sqlQuery.addScalar("idControle", Hibernate.STRING);
				sqlQuery.addScalar("idOrdemSaida", Hibernate.LONG);
				sqlQuery.addScalar("nrFrota", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificador", Hibernate.STRING);
				sqlQuery.addScalar("idMeioTransporte", Hibernate.LONG);
				sqlQuery.addScalar("tipo", Hibernate.STRING);
				sqlQuery.addScalar("tipoLabel", Hibernate.STRING);
				sqlQuery.addScalar("subTipo", Hibernate.STRING);
				sqlQuery.addScalar("subTipoLabel", Hibernate.STRING);
			}
		};
		return configureSqlQuery;
	}	
	
	public  List<Object[]> findChegadaViagemIntegracaoFedex(String placaOuFrota, Long idFilial) {
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		if(StringUtils.isNumeric(placaOuFrota) && FrotaPlacaChegadaSaidaSuggestUtils.isNumeroFrotaValido(placaOuFrota)) {
			placaOuFrota = FrotaPlacaChegadaSaidaSuggestUtils.completarNumeroFrotaComZeros(placaOuFrota);
			filter.put("value", placaOuFrota);
		}
		
		ConsultaSQL chegadaViagemSQL = new ChegadaViagemSQLBuilder(idFilial, placaOuFrota).build();

		filter.putAll(chegadaViagemSQL.getParametros());
		
		final ConfigureSqlQuery configureSqlQuery = getConfigureSqlQuery();
		
		return getAdsmHibernateTemplate().findBySql(chegadaViagemSQL.getSql(), filter, configureSqlQuery);
	}


	/**
	 * Verifica se existe uma descrição de portaria para a mesmo filial
	 * @param dsPortaria
	 * @param idFilial
	 * @return
	 */
	public boolean validateIsDsPortariaFilial(String dsPortaria, Long idPortaria, Long idFilial) {  	
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.rowCount());

		dc.createAlias("terminal", "t");

		dc.add(Restrictions.ilike("dsPortaria", dsPortaria, MatchMode.EXACT));
		dc.add(Restrictions.eq("t.filial.id", idFilial));
		
		if (idPortaria != null) {
			dc.add(Restrictions.ne("idPortaria", idPortaria));
		}

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return result.intValue() > 0;
	}

}
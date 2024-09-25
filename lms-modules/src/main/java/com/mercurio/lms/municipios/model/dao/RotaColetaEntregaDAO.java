package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureRestrictionsBuilder;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.RestrictionBuilder;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HorarioPrevistoSaidaRota;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaColetaEntregaDAO extends BaseCrudDao<RotaColetaEntrega, Long> {

	protected String vigentes = "";
	
	public ResultSetPage findPaginated(Map criteria) {
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);		
		RestrictionsBuilder rb = createRestrictionsBuilder();
		ArrayList arrayList = new ArrayList();
		arrayList.add("filial.idFilial");
		
		this.vigentes = (String)criteria.get("vigentes");
		
		rb.addCustomRestrictionBuilder(arrayList, new RestrictionBuilder() {
		
			public void buildRestriction(DetachedCriteria dc, String ownerProperty, Map fields) {
				
				Long idFilial = (Long)fields.get("idFilial");
				
				Object obj[] = new Object [] {idFilial};
				Type type[] = new Type[] {Hibernate.LONG};
				
				if (!StringUtils.isBlank(ownerProperty)) {					
					dc.add(Restrictions.sqlRestriction("{alias}.ID_FILIAL = ? ", obj, type ));

					if (!StringUtils.isBlank(vigentes)) {
						Object objVigente[] = new Object [] {JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual()};
						Type typeVigente[] = new Type[] {Hibernate.custom(JodaTimeYearMonthDayUserType.class), Hibernate.custom(JodaTimeYearMonthDayUserType.class)};
						
						if (vigentes.equals("S")) {
							dc.add(Restrictions.sqlRestriction("(DT_VIGENCIA_INICIAL <= ? and DT_VIGENCIA_FINAL >= ? )", objVigente, typeVigente ));
						} else if (vigentes.equals("N")) {
							dc.add(Restrictions.sqlRestriction("(DT_VIGENCIA_INICIAL > ? or DT_VIGENCIA_FINAL < ? )", objVigente, typeVigente ));
						}
					} 
					
				}
			}
		
		});
		
		ConfigureRestrictionsBuilder crb = new ConfigureRestrictionsBuilder() {
			public void configure(RestrictionsBuilder rb) {
			}
		};		
		
		return super.findPaginated(criteria, findDef, rb, crb);
	}
	
	public Integer getRowCount(TypedFlatMap tfm) {
		this.vigentes = (String)tfm.getString("vigentes");
		
		Map parameters = new HashMap();
		
		StringBuffer sql = new StringBuffer();		

		sql.append(" select * ")
		.append(" from rota_coleta_entrega roce, " )
		.append(" filial filial " )
		.append(" where roce.id_Filial = filial.id_Filial ");
		
		if (!StringUtils.isEmpty(vigentes)) {
			
			if (vigentes.equals("S")) {
				sql.append(" and (trunc(roce.dt_Vigencia_Inicial) <= trunc(:dataAtual) and trunc(roce.dt_Vigencia_Final) >= trunc(:dataAtual)) ");
				parameters.put("dataAtual", JTDateTimeUtils.getDataHoraAtual());
			} else if (vigentes.equals("N")) {
				sql.append(" and (trunc(roce.dt_Vigencia_Inicial) > trunc(:dataAtual) or trunc(roce.dt_Vigencia_Final) < trunc(:dataAtual)) ");
				parameters.put("dataAtual", JTDateTimeUtils.getDataHoraAtual());
			}
			
		} 
		
		if (tfm.getYearMonthDay("dtVigenciaInicial") != null) {
			sql.append(" and roce.dt_Vigencia_Inicial >= :dtVigenciaInicial" );
			parameters.put("dtVigenciaInicial", JTDateTimeUtils.yearMonthDayToDateTime(tfm.getYearMonthDay("dtVigenciaInicial")));
		}

		if (tfm.getYearMonthDay("dtVigenciaFinal") != null) {
			sql.append(" and roce.dt_Vigencia_Final <= :dtVigenciaFinal");
			parameters.put("dtVigenciaFinal", JTDateTimeUtils.yearMonthDayToDateTime(tfm.getYearMonthDay("dtVigenciaFinal")));
		}

		if (tfm.get("filial.idFilial") != null) {
			sql.append(" and roce.id_filial = :filial.idFilial");
			parameters.put("filial.idFilial", tfm.getLong("filial.idFilial"));
		}

		if (tfm.getShort("nrRota") != null) {
			sql.append(" and roce.nr_Rota = :nrRota");
			parameters.put("nrRota", tfm.getShort("nrRota"));
		}

		if (tfm.getInteger("nrKm") != null) {
			sql.append(" and roce.nr_Km = :nrKm");
			parameters.put("nrKm", tfm.getInteger("nrKm"));
		}		

		if (!StringUtils.isEmpty(tfm.getString("dsRota"))){
			sql.append(" and lower(roce.ds_rota) like :dsRota");
			parameters.put("dsRota", tfm.getString("dsRota").toLowerCase());
		}

		return getAdsmHibernateTemplate().getRowCountBySql(new StringBuffer(sql.toString()).insert(0,"FROM (").append(")").toString(), parameters);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);		
	}
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
		lazyFindLookup.put("horarioPrevistoSaidaRotas", FetchMode.SELECT);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("horarioPrevistoSaidaRotas", FetchMode.SELECT);
		lazyFindById.put("rotaIntervaloCeps", FetchMode.SELECT);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return RotaColetaEntrega.class;
	}

	/**
	* Verifica a colisao de vigencias de rotas de coleta e entrega de uma filial
	* @param idRotaColetaEntrega
	* @param idFilial
	* @param dtVigenciaInicial
	* @param dtVigenciaFinal
	* @return TRUE se existe colisao de vigencia, FALSE caso contrario
	*/
	public boolean verificaVigenciaRotaColetaEntrega(Long idRotaColetaEntrega, Long idFilial, String dsRota, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		if (idRotaColetaEntrega != null)
			dc.add(Restrictions.ne("idRotaColetaEntrega", idRotaColetaEntrega));

		dc.add(Restrictions.eq("dsRota", dsRota));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));
		JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

		dc.setProjection(Projections.rowCount());

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		return ((Integer) result.get(0)).intValue() > 0;
	}

	public Short consultaUltimoNrRota(Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.max("nrRota"));

		dc.add(Restrictions.eq("filial.idFilial", idFilial));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		return (result.get(0) != null) ? (Short) result.get(0) : Short.valueOf((short)0); 
	}

	/**
	 * List com as rota de coleta entrega da filial do usuario logado.
	 * 
	 * @param filial do usuario logado
	 * @return list com as rota de coleta entrega da filial do usuario logado
	 */
	public List findLookupByFilialUsuario(Filial filial, Short nrRota){
		DetachedCriteria dc = DetachedCriteria.forClass(RotaColetaEntrega.class)
		.setFetchMode("filial", FetchMode.JOIN)
		.add(Restrictions.eq("nrRota", nrRota))
		.add(Restrictions.eq("filial.id", filial.getIdFilial()));

		return super.findByDetachedCriteria(dc); 
	}

	public List findRotaColetaEntregaById(Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRotaColetaEntrega",idRotaColetaEntrega));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		if (dtVigenciaFinal != null)
			dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public boolean findRotaColetaEntregaValidaVigencias(Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("idRotaColetaEntrega",idRotaColetaEntrega));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * Verifica se existe alguma rota de coleta/entrega com intervalo de cep dentro dos parametros informados e na mesma vigencia informada
	 * @param idFilial
	 * @param idMunicipio
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return TRUE se existe alguma rota, FALSE caso contrario
	 */
	public boolean verificaExisteRotaColetaEntrega(Long idFilial, Long idMunicipio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.createAlias("rotaIntervaloCeps", "cep");

		dc.add(Restrictions.eq("filial.idFilial", idFilial));
		dc.add(Restrictions.eq("cep.municipio.idMunicipio", idMunicipio));

		dc.add(Restrictions.ge("cep.dtVigenciaFinal", dtVigenciaInicial));
		dc.add(Restrictions.le("cep.dtVigenciaInicial", JTDateTimeUtils.maxYmd(dtVigenciaFinal)));

		dc.setProjection(Projections.rowCount());

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		return ((Integer) result.get(0)).intValue() > 0;
	}

	/**
	 * Finder de rotas de coleta/entrega filtrando pelo id da filial.
	 * Pode ser utilizado em combos de rota de coleta/entrega.
	 * @param idFilial 
	 * @param apenasRegistrosVigentes define se a consulta trará apenas os registros vigentes ou todos eles.
	 * @return list
	 * @author luisfco
	 */
	public List findRotaColetaEntrega(Long idFilial, boolean apenasRegistrosVigentes) {

		SqlTemplate s = new SqlTemplate();
		if (apenasRegistrosVigentes) {
			s.addProjection("new Map(rce.idRotaColetaEntrega as idRotaColetaEntrega, rce.nrRota || ' - ' || rce.dsRota as nrDsRota)");
		}else{
			s.addProjection("new Map(rce.idRotaColetaEntrega as idRotaColetaEntrega, rce.nrRota || ' - ' || rce.dsRota as nrDsRota, " +
					"rce.dtVigenciaInicial as dtVigenciaInicial, rce.dtVigenciaFinal as dtVigenciaFinal)");
		}

		s.addFrom(getPersistentClass().getName(), "rce");
		s.addCriteria("rce.filial.id", "=", idFilial);
		
		if (apenasRegistrosVigentes) {
			s.addCriteria("rce.dtVigenciaInicial", "<=", JTDateTimeUtils.getDataAtual());
			s.addCriteria("rce.dtVigenciaFinal",">=", JTDateTimeUtils.getDataAtual());
		}		
		s.addOrderBy("rce.nrRota");
		return getAdsmHibernateTemplate().find(s.getSql(), s.getCriteria()); 
	}

	
	public List<RotaColetaEntrega> findRotaColetaEntrega(Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(RotaColetaEntrega.class, "rce");		
		dc.add(Restrictions.eq("rce.filial.id", idFilial));
		dc.add(Restrictions.le("rce.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.ge("rce.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));		
		return super.findByDetachedCriteria(dc); 
	}
	
	/**
	 * Finder de rotas de coleta/entrega.
	 * Pode ser utilizado em combos de rota de coleta/entrega.
	 * @param apenasRegistrosVigentes define se a consulta trará apenas os registros vigentes ou todos eles.
	 * @return list
	 * @author luisfco
	 */
	public List findRotaColetaEntrega(boolean apenasRegistrosVigentes) {
		return findRotaColetaEntrega(null, apenasRegistrosVigentes);
	}

	/**
	 * 
	 * @param rota
	 */
	public void storeRotaColetaEntrega(RotaColetaEntrega rota) {
		if (rota.getIdRotaColetaEntrega() != null) {
			getAdsmHibernateTemplate().removeById("delete "+HorarioPrevistoSaidaRota.class.getName()+" where rotaColetaEntrega.id = :id", rota.getIdRotaColetaEntrega());
		}
		store(rota);
	}

	/**
	 * Busca as rotas de coleta entrega que possuem pedidos coleta abertos e com data de previsão de coleta
	 * menor igual à data passada por parâmetro e com filial responsável igual à filial informada.
	 * @param tpStatusColeta
	 * @param idFilialResponsavel
	 * @param datePrevisaoColeta
	 * @return
	 */
	public List findIdsRotasColetaEntregaByTpStatusColetaUntilPrevisaoColeta(String tpStatusColeta, Long idFilialResponsavel, YearMonthDay datePrevisaoColeta) {
		DetachedCriteria dc = DetachedCriteria.forClass(RotaColetaEntrega.class, "rce");
		dc.setProjection(Projections.distinct(Projections.property("rce.id")));
		dc.createAlias("rce.pedidoColetas", "pedidosColeta");
		dc.add(Restrictions.eq("pedidosColeta.tpStatusColeta", tpStatusColeta));
		dc.add(Restrictions.eq("pedidosColeta.filialByIdFilialResponsavel.id", idFilialResponsavel));
		dc.add(Restrictions.le("pedidosColeta.dtPrevisaoColeta", datePrevisaoColeta));
		return super.findByDetachedCriteria(dc);
	}

	/**
	 * Retorna Rota de Coleta/Entrega para os parâmetros informados
	 * @param idFilial
	 * @param nrRota
	 * @return Rota de Coleta/Entrega
	 */
	public RotaColetaEntrega findRotaColetaEntrega(Long idFilial, Short nrRota) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("nrRota",nrRota));
		dc.add(Restrictions.eq("filial.id",idFilial));
		return (RotaColetaEntrega) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public RotaColetaEntrega findRotaColetaEntregaByControleCarga(Long idControleCarga) {
		StringBuffer hql = new StringBuffer()
		.append(" select cc.rotaColetaEntrega from ")
		.append(ControleCarga.class.getName() + " cc ")		
		.append(" join cc.rotaColetaEntrega rota ")
		.append(" where cc.idControleCarga = ? ");
		
		List params = new ArrayList();
		params.add(idControleCarga);
		
		List listaCC = getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
		
		 if(!listaCC.isEmpty()){
			 return (RotaColetaEntrega) listaCC.get(0);			 			 
		 }else{
			 return null;
		 }		 
	}
	
	/**
	 * Query responsável por retornar os resultados para a suggest de rota de coleta/entrega.
	 * 
	 * @param idFilial
	 * @param nrRota
	 * @param dsRota
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findRotaColetaEntregaSuggest(
			Long idFilial, String nrRota, String dsRota, Integer limiteRegistros) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		String sql = getSqlRotaColetaEntregaSuggest(idFilial, nrRota, dsRota, limiteRegistros, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, parametersValues, getConfigureSqlQueryRotaColetaEntregaSuggest());
	}
	
	/**
	 * Definição do SQL da query.
	 * 
	 * @param idFilial
	 * @param nrRota
	 * @param dsRota
	 * @param limiteRegistros
	 * @param parametersValues
	 * 
	 * @return String
	 */
	private String getSqlRotaColetaEntregaSuggest(Long idFilial, String nrRota,
			String dsRota, Integer limiteRegistros,
			Map<String, Object> parametersValues) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT rota.id_rota_coleta_entrega AS idRotaColetaEntrega,");
		sql.append("  rota.nr_rota AS nrRota,");
		sql.append("  rota.ds_rota AS dsRota,");
		sql.append("  rota.nr_km AS nrKm");
		sql.append(" FROM rota_coleta_entrega rota");
		sql.append(" WHERE rownum <= :limite");
		parametersValues.put("limite", limiteRegistros);
		
		if(idFilial != null){
			sql.append(" AND rota.id_filial = :idFilial");
			parametersValues.put("idFilial", idFilial);	
		}
		
		if(nrRota != null){
			sql.append(" AND rota.nr_rota = :nrRota");
			parametersValues.put("nrRota", nrRota);	
		}
				
		if(StringUtils.isNotBlank(dsRota)){
			sql.append(" AND LOWER(rota.ds_rota) LIKE LOWER(:dsRota)");
			parametersValues.put("dsRota", "%" + dsRota + "%");	
		}

		sql.append(" ORDER BY rota.ds_rota");
		
		return sql.toString();
	}
	
	/**
	 * Projeção da query de rota de coleta/entrega.
	 * 
	 * @return ConfigureSqlQuery
	 */
	private ConfigureSqlQuery getConfigureSqlQueryRotaColetaEntregaSuggest(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idRotaColetaEntrega", Hibernate.LONG);	
				sqlQuery.addScalar("nrRota", Hibernate.SHORT);
				sqlQuery.addScalar("dsRota", Hibernate.STRING);
				sqlQuery.addScalar("nrKm", Hibernate.INTEGER);
			}
		};
		return csq;
	}

	public String verificaVigencia(RotaColetaEntrega rotaColetaEntrega) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idRota", rotaColetaEntrega.getIdRotaColetaEntrega());
		parametersValues.put("hoje",  JTDateTimeUtils.getDataAtual());
		parametersValues.put("dhInicio",  rotaColetaEntrega.getDtVigenciaInicial());
		parametersValues.put("dhFinal",  JTDateTimeUtils.maxYmd(rotaColetaEntrega.getDtVigenciaFinal()));
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 'intervalosCEPRotaMensagem' AS HAS ");
		sql.append("	FROM ROTA_INTERVALO_CEP ");
		sql.append(getWhereVigencia());
		sql.append("	UNION ");
		sql.append("SELECT 'meiosTransporteRotaMensagem' AS HAS ");
		sql.append("	FROM ROTA_TIPO_MEIO_TRANSPORTE ");
		sql.append(getWhereVigencia());
		sql.append("	UNION ");
		sql.append("SELECT 'postosPassagemRotaMensagem' AS HAS ");
		sql.append("	FROM POSTO_PASSAGEM_ROTA_COL_ENT ");
		sql.append(getWhereVigencia());
		sql.append("	UNION ");
		sql.append("SELECT 'regioesRotaMensagem' AS HAS ");
		sql.append("	FROM REGIAO_FILIAL_ROTA_COL_ENT ");
		sql.append(getWhereVigencia());
		
		final ConfigureSqlQuery csq = getScalar();
		
		List lista = getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues,csq);
		
		 if(!lista.isEmpty()){
			 return (String) lista.get(0);			 			 
		 }else{
			 return null;
		 }		 
	}

	private ConfigureSqlQuery getScalar() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("HAS", Hibernate.STRING);	
			}
		};
		return csq;
	}

	private String getWhereVigencia() {
		StringBuilder sql = new StringBuilder();
		sql.append("	WHERE ID_ROTA_COLETA_ENTREGA = :idRota ");				
		sql.append("		AND DT_VIGENCIA_FINAL         > :dhFinal");
		return sql.toString();
	}

	public Short findNrRota(Long idRota) {
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idRota", idRota);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NR_ROTA from rota_coleta_entrega WHERE ID_ROTA_COLETA_ENTREGA = :idRota ");	
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("NR_ROTA", Hibernate.SHORT);	
			}
		};

		List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parametersValues, csq);
		
		if(!object.isEmpty()){
			return (Short) object.get(0);
		}
		
		return  null;
	} 

	
	public boolean findExisteRota(Long idFilial, Short nrRota, Long idRotaColetaEntrega) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("nrRota", nrRota);
		parametersValues.put("idFilial", idFilial);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT 1 from ");
		sql.append(RotaColetaEntrega.class.getName());
		sql.append(" WHERE filial.id = ? and nrRota = ? and idRotaColetaEntrega != ?");	
   		
		Integer qtdRows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idFilial, nrRota,idRotaColetaEntrega});
		return (qtdRows.intValue() > 0) ? Boolean.TRUE : Boolean.FALSE;
	}
	
	
	
}
package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

import org.joda.time.format.DateTimeFormat;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SeguroClienteDAO extends BaseCrudDao<SeguroCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SeguroCliente.class;
	}

	/**
	 * Busca os dados para a listagem
	 * @param criteria Critérios de pesquisa
	 * @param findDef Dados de paginação
	 * @return ResultSetPage Objeto contendo o resultado da pesquisa e os dados de paginação
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map criteria, FindDefinition findDef) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map( " + " sc.id as idSeguroCliente," +
						"   cl.tpIdentificacao as tpIdentificacao," +
						"   cl.nrIdentificacao as identCliente," +
						"   cl.nmPessoa as infCliente," +
						"   sc.tpModal as tpModal, " +
						"   sc.tpAbrangencia as tpAbrangencia, " +
						"   ts.sgTipo as sgTipo, " +
						"   pes.nmPessoa as nmPessoa, " +
						"   sg.nmPessoa as infSeguradora, " +
						"   sc.dsApolice as dsApolice, " +
						"   sc.dtVigenciaInicial as dtVigenciaInicial, " +
						"   sc.dtVigenciaFinal as dtVigenciaFinal," +
						// LMS-7285
						"   sc.vlLimite AS vlLimite, " +
						"   sc.vlLimiteControleCarga AS vlLimiteControleCarga " +
						" ) ");
		
		sql.addFrom( DomainValue.class.getName() + " vd1 ");
		sql.addFrom( DomainValue.class.getName() + " vd2 ");
		sql.addFrom( getPersistentClass().getName()+ " sc " +
					" left join sc.reguladoraSeguro rs " +
					" join sc.cliente.pessoa cl" +
					" left join sc.seguradora.pessoa sg" +
					" left join rs.pessoa pes " +
					" join sc.tipoSeguro ts " );

		sql.addJoin("vd1.value","sc.tpModal");
		sql.addJoin("vd2.value","sc.tpAbrangencia");

		sql.addCriteria("vd1.domain.name", "=", "DM_MODAL");
		sql.addCriteria("vd2.domain.name", "=", "DM_ABRANGENCIA");
		sql.addCriteria("sc.tpModal", "=", MapUtils.getString(criteria, "tpModal"));
		sql.addCriteria("sc.tpAbrangencia", "=", MapUtils.getString(criteria, "tpAbrangencia"));

		sql.addCriteria("ts.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "tipoSeguro"), "idTipoSeguro"));
		sql.addCriteria("sc.cliente.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "cliente"), "idCliente"));

		// LMS 6148
		if(MapUtils.getLong(MapUtils.getMap(criteria, "seguradora"), "idSeguradora") != null){
			sql.addCriteria("sc.seguradora.idSeguradora", "=", MapUtils.getLong(MapUtils.getMap(criteria, "seguradora"), "idSeguradora"));
		}
		
		if (MapUtils.getLong(MapUtils.getMap(criteria, "reguladoraSeguro"), "idReguladora") != null) {
			sql.addCriteria("rs.idReguladora", "=", MapUtils.getLong(MapUtils.getMap(criteria, "reguladoraSeguro"), "idReguladora"));
		}

		// LMS-7285 - considera critério para data de vigência
		@SuppressWarnings("unchecked")
		YearMonthDay dtVigencia = getCriteriaDtVigencia(criteria);
		if (dtVigencia != null) {
			sql.addCriteria("sc.dtVigenciaInicial", "<=", dtVigencia);
			sql.addCriteria("sc.dtVigenciaFinal", ">=", dtVigencia);
		}

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", Locale.getDefault()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd2.description", Locale.getDefault()));
		sql.addOrderBy("ts.sgTipo");
		sql.addOrderBy("pes.nmPessoa");
		sql.addOrderBy("sc.dtVigenciaInicial");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

	/**
	 * LMS-7285 - Converte parâmetros opcional <tt>dtVigencia</tt> em
	 * {@link YearMonthDay}.
	 * 
	 * @param criteria
	 *            Mapa de parâmetros incluindo <tt>dtVigencia</tt>.
	 * @return Data de vigência como {@link YearMonthDay} ou {@code null} caso
	 *         inexistente.
	 */
	private YearMonthDay getCriteriaDtVigencia(Map<String, Object> criteria) {
		String string = (String) criteria.get("dtVigencia");
		if (string == null || string.isEmpty()) {
			return null;
		}
		return (YearMonthDay) ReflectionUtils.toObject(string, YearMonthDay.class);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoSeguro", FetchMode.JOIN);
		lazyFindById.put("reguladoraSeguro", FetchMode.JOIN);
		lazyFindById.put("reguladoraSeguro.pessoa", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("municipioOrigem", FetchMode.JOIN);
		lazyFindById.put("municipioOrigem.unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("municipioDestino", FetchMode.JOIN);
		lazyFindById.put("municipioDestino.unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("seguradora", FetchMode.JOIN);
		lazyFindById.put("seguradora.pessoa", FetchMode.JOIN);
		lazyFindById.put("usuarioAviso", FetchMode.JOIN);
		lazyFindById.put("usuarioAviso.usuario", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	/**
	 * LMS-7285 - Incluir relacionamento com {@link Seguradora} e {@link Pessoa}
	 * nas buscas para lookup.
	 * 
	 * @param lazyFindLookup
	 *            mapa de {@link FetchMode}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("seguradora", FetchMode.JOIN);
		lazyFindLookup.put("seguradora.pessoa", FetchMode.JOIN);
	}

	public List findByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		ProjectionList pl = Projections
				.projectionList()
				.add(Projections.property("sc.idSeguroCliente"),
						"idSeguroCliente")
				.add(Projections.property("sc.tipoSeguro.idTipoSeguro"),
						"idTipoSeguro");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "sc")
			.setProjection(pl)
			.add(Restrictions.eq("sc.cliente.id", idCliente))
			.add(Restrictions.eq("sc.tpModal", tpModal))
			.add(Restrictions.eq("sc.tpAbrangencia", tpAbrangencia))
			.add(Restrictions.le("sc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("sc.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);
	}

	public List findVigentesByClienteTpModalTpAbrangencia(Long idCliente, String tpModal, String tpAbrangencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "sc")
			.add(Restrictions.eq("sc.cliente.id", idCliente))
			.add(Restrictions.eq("sc.tpModal", tpModal))
			.add(Restrictions.eq("sc.tpAbrangencia", tpAbrangencia))
			.add(Restrictions.le("sc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("sc.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		return findByDetachedCriteria(dc);
	}

    /**
	 * Retorna valores para geração obrigatória das informações de Seguros no
	 * XML do CT-e.
	 * 
	 * Jira LMS-3996
	 * 
	 * @return List
	 */
	public List findSegValues(Long idServico, Long idCliente) {
    	final StringBuilder sql = new StringBuilder();
    	sql.append("SELECT ps.nm_pessoa,")
			.append(" 	sc.ds_apolice 	,")
			.append(" 	sc.vl_limite, ")
			.append(" 	ps.nr_identificacao, ")
			.append(" 	ps.tp_pessoa ")
			.append("FROM Seguro_Cliente sc ")
			.append("	JOIN pessoa ps ON ps.id_pessoa = sc.id_seguradora ")
			.append("	JOIN cliente cl ON cl.id_cliente = sc.id_cliente ")
			.append("	JOIN tipo_seguro ts ON ts.id_tipo_seguro = sc.id_tipo_seguro ")
			.append("	JOIN servico svc ON svc.tp_modal = ts.tp_modal ")
			.append("WHERE ")
			.append("	(sc.dt_vigencia_inicial <= sysdate AND ")
			.append("	sc.dt_vigencia_final >= sysdate) ")
			.append("	AND ts.tp_abrangencia 	= 'N' ")
			.append("	AND ts.tp_situacao 		= 'A' ")
			.append("	AND sc.tp_abrangencia 	= 'N' ")
			.append("	AND svc.id_servico 		= " + idServico)
			.append("	AND sc.id_cliente 		= " + idCliente)
			.append("	AND ROWNUM 			   <= 1 ")
			.append("ORDER BY sc.vl_limite desc");
	
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nm_pessoa",Hibernate.STRING);
				sqlQuery.addScalar("ds_apolice",Hibernate.STRING);
				sqlQuery.addScalar("vl_limite",Hibernate.STRING);
				sqlQuery.addScalar("nr_identificacao",Hibernate.STRING);
				sqlQuery.addScalar("tp_pessoa",Hibernate.STRING);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}
	
	/**
		 * Retorna valores para geração obrigatória das informações de Seguros no
		 * XML do CT-e.
		 * 
		 * Jira LMS-3996
		 * 
		 * @return List
		 */
		public List findSegValues(Long idServico, Long idCliente, DateTime dhEmissao) {
			final StringBuilder sql = new StringBuilder();
                        String dataEmissao = DateTimeFormat.forPattern("yyyy-MM-dd").print(dhEmissao);
	    	sql.append("SELECT ps.nm_pessoa,")
				.append(" 	sc.ds_apolice 	,")
				.append(" 	sc.vl_limite, ")
				.append(" 	ps.nr_identificacao, ")
				.append(" 	ps.tp_pessoa ")
				.append("FROM Seguro_Cliente sc ")
				.append("	JOIN pessoa ps ON ps.id_pessoa = sc.id_seguradora ")
				.append("	JOIN cliente cl ON cl.id_cliente = sc.id_cliente ")
				.append("	JOIN tipo_seguro ts ON ts.id_tipo_seguro = sc.id_tipo_seguro ")
				.append("	JOIN servico svc ON svc.tp_modal = ts.tp_modal ")
				.append("WHERE ")
				.append("	(sc.dt_vigencia_inicial <= TO_DATE('" + dataEmissao + "', 'yyyy-mm-dd') AND ")
				.append("	sc.dt_vigencia_final >= TO_DATE('" + dataEmissao + "', 'yyyy-mm-dd')) ")
				.append("	AND ts.tp_abrangencia 	= 'N' ")
				.append("	AND ts.tp_situacao 		= 'A' ")
				.append("	AND sc.tp_abrangencia 	= 'N' ")
				.append("	AND svc.id_servico 		= " + idServico)
				.append("	AND sc.id_cliente 		= " + idCliente)
				.append("	AND ROWNUM 			   <= 1 ")
				.append("ORDER BY sc.vl_limite desc");
		
			final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
				public void configQuery(org.hibernate.SQLQuery sqlQuery) {
					sqlQuery.addScalar("nm_pessoa",Hibernate.STRING);
					sqlQuery.addScalar("ds_apolice",Hibernate.STRING);
					sqlQuery.addScalar("vl_limite",Hibernate.STRING);
					sqlQuery.addScalar("nr_identificacao",Hibernate.STRING);
					sqlQuery.addScalar("tp_pessoa",Hibernate.STRING);
				}
			};

	    	final HibernateCallback hcb = new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					SQLQuery query = session.createSQLQuery(sql.toString());
	            	csq.configQuery(query);
					return query.list();
				}
			};

			return getHibernateTemplate().executeFind(hcb);
		}
	
	/**
	 * Retorna os ids das apólices de seguros de clientes que estão vencidas ou
	 * estão para vencer e seus respectivos responsáveis.
	 * 
	 * Jira LMS-6150
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findApolicesAVencer() {
		
    	final StringBuilder sql = new StringBuilder();
    	sql.append("SELECT t.id_seguro_cliente, ")
			.append("		t.id_usuario_aviso, ")	
			.append("		nvl((SELECT us.ds_email ")
			.append("			   FROM usuario us ")
			.append("			  WHERE us.id_usuario = t.id_usuario_aviso), ")
			.append("			(SELECT pg.ds_conteudo ")
			.append("			   FROM parametro_geral pg ")
			.append("			  WHERE pg.nm_parametro_geral = 'DS_EMAIL_SEGUROS')) ds_email ")
			.append("  FROM (SELECT sc.id_seguro_cliente, ")
			.append("				(SELECT us.id_usuario ")
			.append("				   FROM promotor_cliente pc, ")
            .append("						pessoa           pes, ")
            .append("						usuario          us ")
            .append("				  WHERE pc.id_cliente = pes.id_pessoa ")
            .append("					AND pc.dt_fim_promotor IS NULL ")
            .append("					AND pc.id_usuario = us.id_usuario ")
            .append("					AND pc.id_usuario <> 5000 ")
            .append("					AND (pc.tp_modal = sc.tp_modal OR pc.tp_modal IS NULL) ")
            .append("					AND (pc.tp_abrangencia = sc.tp_abrangencia OR pc.tp_abrangencia IS NULL) ")
            .append("					AND pc.id_cliente = sc.id_cliente ")
            .append("					AND us.ds_email IS NOT NULL ")
            .append("					AND rownum = 1) id_usuario_aviso ")
            .append("		   FROM seguro_cliente sc ")
            .append("		  WHERE ((sysdate between sc.dt_vigencia_inicial and sc.dt_vigencia_final) and sc.dt_vigencia_final <= trunc(SYSDATE + nvl((SELECT CAST(pg.ds_conteudo AS NUMBER) ")
            .append("															   FROM parametro_geral pg ")
            .append("															  WHERE pg.nm_parametro_geral = 'NR_DIAS_AVISO_DDR_SEGUROS'), 0))) ")
            .append("			AND ((sc.dh_envio_aviso IS NULL) OR ")
            .append("				(SYSDATE > trunc(sc.dh_envio_aviso + nvl((SELECT CAST(pg.ds_conteudo AS NUMBER)	")
            .append(" 					FROM parametro_geral pg ")
            .append("				 	WHERE pg.nm_parametro_geral = 'NR_DIAS_AVISO_DDR_SEGUROS'), 0))))) t ")
            .append(" ORDER BY id_usuario_aviso");
           
    	final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_seguro_cliente",Hibernate.LONG);
				sqlQuery.addScalar("id_usuario_aviso",Hibernate.LONG);
				sqlQuery.addScalar("ds_email",Hibernate.STRING);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}

	public List findPromotorClientes(Long idSeguroCliente) {
	
		List<Object> params = new ArrayList<Object>();
		params.add(idSeguroCliente);
		
		final StringBuilder hql = new StringBuilder()
			.append("select pc.usuario ")
			.append("from ").append(SeguroCliente.class.getName()).append(" sc, ")
			.append(PromotorCliente.class.getName()).append(" pc ")
			.append("where sc.cliente.idCliente = pc.cliente.idCliente ")
			.append("and pc.dtFimPromotor is null ")
			.append("and pc.usuario.idUsuario != 5000 ")
			.append("and (pc.tpModal = sc.tpModal ")
			.append("or pc.tpModal is null) ")
			.append("and (pc.tpAbrangencia = sc.tpAbrangencia ")
			.append("or pc.tpAbrangencia is null) ")
			.append("and sc.idSeguroCliente = ?");
			
		return getHibernateTemplate().find(hql.toString(), params.toArray());
	}

	/**
	 * LMS-7285 - Atualiza valor limite para controle de carga (
	 * <tt>SEGURO_CLIENTE.VL_LIMITE_CONTROLE_CARGA</tt>) de determinado
	 * {@link SeguroCliente}.
	 * 
	 * @param idSeguroCliente
	 *            id da {@link SeguroCliente}
	 * @param vlLimiteControleCarga
	 *            valor limite para controle
	 */
	public void storeVlLimiteControleCarga(Long idSeguroCliente, BigDecimal vlLimiteControleCarga) {
		StringBuilder sql = new StringBuilder()
				.append("UPDATE seguro_cliente ")
				.append("SET vl_limite_controle_carga = " + (vlLimiteControleCarga == null ? "NULL" : ":vl_limite_controle_carga") + " ")
				.append("WHERE id_seguro_cliente = :id_seguro_cliente");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id_seguro_cliente", idSeguroCliente);
		parametersValues.put("vl_limite_controle_carga", vlLimiteControleCarga);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

}
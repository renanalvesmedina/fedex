package com.mercurio.lms.tabelaprecos.model.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.tabelaprecos.model.DadosTabelaPrecoDTO;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoAnexo;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class TabelaPrecoDAO extends BaseCrudDao<TabelaPreco, Long> {

	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return TabelaPreco.class;
	}

	public TabelaPreco findByIdTabelaPrecoNotLazy(Long id) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(getPersistentClass().getName()).append(" as tp ")
    	.append(" inner join fetch tp.subtipoTabelaPreco stp ")
		.append(" inner join fetch tp.moeda m ")
		.append(" inner join fetch tp.tipoTabelaPreco ttp ")
    	.append(" where ")
    	.append(" tp.id = ? ");
		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sql.toString(),new Object[]{id});
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoTabelaPreco", FetchMode.JOIN);
		/* Usado pela tela de manterTabelasDivisao */
		lazyFindById.put("tipoTabelaPreco.servico", FetchMode.JOIN);
		lazyFindById.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		lazyFindById.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
		lazyFindById.put("subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		/* Usado pela tela de simularNovosPrecos */
		lazyFindById.put("tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("usuario", FetchMode.JOIN);

		super.initFindByIdLazyProperties(lazyFindById);
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoTabelaPreco", FetchMode.JOIN);
		/* Usado pela tela de manterTabelasDivisao */
		lazyFindById.put("tipoTabelaPreco.servico", FetchMode.JOIN);
		lazyFindById.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		lazyFindById.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
		lazyFindById.put("subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindById);
	}

	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("tipoTabelaPreco", FetchMode.JOIN);
		/* Usado pela tela de manterTabelasDivisao */
		lazyFindLookup.put("tipoTabelaPreco.servico", FetchMode.JOIN);
		lazyFindLookup.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		lazyFindLookup.put("tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
		lazyFindLookup.put("subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindLookup.put("moeda", FetchMode.JOIN);

		super.initFindPaginatedLazyProperties(lazyFindLookup);
	}

	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("tipoTabelaPreco", FetchMode.JOIN);
		lazyFindList.put("subtipoTabelaPreco", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 *
	 * @param idTipoTabelaPreco
	 * @param idSubtipoTabelaPreco
	 * @return TabelaPreco
	 */
	public TabelaPreco findTabelaPreco(Long idTipoTabelaPreco, Long idSubtipoTabelaPreco) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("tipoTabelaPreco", "ttp");
		dc.createAlias("subtipoTabelaPreco", "stp");
		dc.add(Restrictions.eq("ttp.idTipoTabelaPreco", idTipoTabelaPreco));
		dc.add(Restrictions.eq("stp.idSubtipoTabelaPreco", idSubtipoTabelaPreco));
		dc.add(Restrictions.eq("blEfetivada",Boolean.TRUE));
		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List findLookup(Map criteria) {
		return super.findLookupByCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findByIdDivisaoCliente(Long idDivisaoCliente){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("tdc.tabelaPreco");
		sql.addFrom(TabelaDivisaoCliente.class.getName(), "tdc");
		sql.addCriteria("tdc.divisaoCliente.id", "=", idDivisaoCliente);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public TabelaPreco findByIdTabelaDivisaoCliente(Long dTabelaDivisaoCliente){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("tdc.tabelaPreco");
		sql.addFrom(TabelaDivisaoCliente.class.getName(), "tdc");
		sql.addCriteria("tdc.id", "=", dTabelaDivisaoCliente);

		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	public Long findIdTabelaPrecoVigente(String tpTipoTabelaPreco, Long idSubtipoTabelaPreco,
			Long idServico, Boolean blEfetivada, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		Criterion servico = null;
		if(idServico == null) {
			servico = Restrictions.isNull("ttp.servico.id");
		} else {
			servico = Restrictions.eq("ttp.servico.id", idServico);
		}

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
			.setProjection(Projections.property("tp.idTabelaPreco"))
			.createAlias("tp.tipoTabelaPreco", "ttp")
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
			.add(Restrictions.eq("tp.subtipoTabelaPreco.id", idSubtipoTabelaPreco))
			.add(servico)
			.add(Restrictions.eq("tp.blEfetivada", blEfetivada))
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		if (dtVigenciaFinal == null) {
			dc = dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigenciaInicial))
				.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaInicial));
		} else {
			Criterion andInicial = Restrictions.and(
					Restrictions.le("tp.dtVigenciaInicial", dtVigenciaInicial),
					Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaInicial));
			Criterion andFinal = Restrictions.and(
					Restrictions.le("tp.dtVigenciaInicial", dtVigenciaFinal),
					Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaFinal));
			dc = dc.add(Restrictions.or(andInicial, andFinal));
		}
		List result = findByDetachedCriteria(dc);
		if(!result.isEmpty()) {
			return (Long)result.get(0);
		}
		return null;
	}

	public Long findIdTabelaPrecoVigenteByEmpresa(String tpTipoTabelaPreco, Long idSubtipoTabelaPreco,
			Long idEmpresaCadastrada, Boolean blEfetivada, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		Criterion servico = null;
		if(idEmpresaCadastrada == null) {
			servico = Restrictions.isNull("ttp.empresaByIdEmpresaCadastrada.id");
		} else {
			servico = Restrictions.eq("ttp.empresaByIdEmpresaCadastrada.id", idEmpresaCadastrada);
		}

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
			.setProjection(Projections.property("tp.idTabelaPreco"))
			.createAlias("tp.tipoTabelaPreco", "ttp")
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
			.add(Restrictions.eq("tp.subtipoTabelaPreco.id", idSubtipoTabelaPreco))
			.add(servico)
			.add(Restrictions.eq("tp.blEfetivada", blEfetivada))
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		if (dtVigenciaFinal == null) {
			dc = dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigenciaInicial))
				.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaInicial));
		} else {
			Criterion andInicial = Restrictions.and(
					Restrictions.le("tp.dtVigenciaInicial", dtVigenciaInicial),
					Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaInicial));
			Criterion andFinal = Restrictions.and(
					Restrictions.le("tp.dtVigenciaInicial", dtVigenciaFinal),
					Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaFinal));
			dc = dc.add(Restrictions.or(andInicial, andFinal));
		}
		List result = findByDetachedCriteria(dc);
		if(!result.isEmpty()) {
			return (Long)result.get(0);
		}
		return null;
	}

	public Integer getRowTabelaPrecoVigenteDtFinal(String tpTipoTabelaPreco, Long idSubtipoTabelaPreco,
			Long idServico, Boolean blEfetivada, YearMonthDay dtVigenciaFinal, Long idTabelaPreco) {
		Criterion servico = null;
		if(idServico == null)
			servico = Restrictions.isNull("ttp.servico.id");
		else
			servico = Restrictions.eq("ttp.servico.id", idServico);
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
			.setProjection(Projections.rowCount())
			.createAlias("tp.tipoTabelaPreco", "ttp")
			.add(Restrictions.ne("tp.id", idTabelaPreco))
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
			.add(Restrictions.eq("tp.subtipoTabelaPreco.id", idSubtipoTabelaPreco))
			.add(servico)
			.add(Restrictions.eq("tp.blEfetivada", blEfetivada))
			.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
			.add(Restrictions.le("tp.dtVigenciaInicial", dtVigenciaFinal))
			.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigenciaFinal));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public boolean validateVigenciaInferiorTabelaPrecoVigente(TabelaPreco tabelaPreco) {
		StringBuilder sql = new StringBuilder()
			.append(" SELECT tp.id_tabela_preco ")
			.append("   FROM tabela_preco tp, tipo_tabela_preco ttp ")
			.append("  WHERE tp.id_tipo_tabela_preco    = ttp.id_tipo_tabela_preco ")
			.append("    AND tp.id_subtipo_tabela_preco = :idSubtipoTabelaPreco ")
			.append("    AND ttp.id_servico             = :idServico ")
			.append("    AND tp.bl_efetivada            = 'S' ")
			.append("    AND ttp.tp_tipo_tabela_preco   = :tpTipoTabelaPreco ")
			.append("    AND TRUNC(tp.dt_vigencia_inicial) <= :dataAtual ")
			.append("    AND TRUNC(tp.dt_vigencia_final)   >= :dataAtual ")
			.append("    AND TRUNC(tp.dt_vigencia_inicial) <= :dtVigenciaFinal ");
		
		if (tabelaPreco.getIdTabelaPreco() != null) {
			sql.append("    AND tp.id_tabela_preco <> :idTabelaPreco ");
		}
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
				sqlQuery.addScalar("id_tabela_preco", Hibernate.LONG);
			}    		
		};
	
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
		parameters.put("idSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
		parameters.put("idServico", tabelaPreco.getTipoTabelaPreco().getServico().getIdServico());
		parameters.put("tpTipoTabelaPreco", tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
		parameters.put("dataAtual", JTDateTimeUtils.getDataAtual());
		parameters.put("dtVigenciaFinal", tabelaPreco.getDtVigenciaFinal());
		
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq).isEmpty();
	}

	public Integer findNrVersaoTabelaPrecoDtVigencia(TabelaPreco tabelaPreco) {
		StringBuilder sql = new StringBuilder()
			.append(" SELECT ttp.nr_versao ")
			.append("   FROM tabela_preco tp, tipo_tabela_preco ttp ")
			.append("  WHERE tp.id_tipo_tabela_preco    = ttp.id_tipo_tabela_preco ")
			.append("    AND tp.id_subtipo_tabela_preco = :idSubtipoTabelaPreco ")
			.append("    AND ttp.id_servico             = :idServico ")
			.append("    AND tp.bl_efetivada            = 'S' ")
			.append("    AND ttp.tp_tipo_tabela_preco   = :tpTipoTabelaPreco ")
			.append("    AND ( ")
			.append("           TRUNC(tp.dt_vigencia_inicial) <= :dtVigenciaInicial ")
			.append("       AND TRUNC(tp.dt_vigencia_final)   >= :dtVigenciaInicial ")
			
			.append("        OR TRUNC(tp.dt_vigencia_inicial) <= :dtVigenciaFinal ")
			.append("       AND TRUNC(tp.dt_vigencia_final)   >= :dtVigenciaFinal ")

			.append("        OR TRUNC(tp.dt_vigencia_inicial) >= :dtVigenciaInicial ")
			.append("       AND TRUNC(tp.dt_vigencia_inicial) <= :dtVigenciaFinal ")
			.append("    ) ");
			
		if (tabelaPreco.getIdTabelaPreco() != null) {
			sql.append("    AND tp.id_tabela_preco <> :idTabelaPreco ");
		}
			
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {    			
				sqlQuery.addScalar("nr_versao", Hibernate.INTEGER);
			}    		
		};
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("idTabelaPreco", tabelaPreco.getIdTabelaPreco());
		parameters.put("idSubtipoTabelaPreco", tabelaPreco.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
		parameters.put("idServico", tabelaPreco.getTipoTabelaPreco().getServico().getIdServico());
		parameters.put("tpTipoTabelaPreco", tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
		parameters.put("dtVigenciaInicial", tabelaPreco.getDtVigenciaInicial());
		parameters.put("dtVigenciaFinal", tabelaPreco.getDtVigenciaFinal());
		
		List<?> retorno = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
		if(!retorno.isEmpty()) {
			return (Integer) retorno.get(0);
		}
		return null;
	}
	
	public ResultSetPage findPaginatedSimulacao(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco_tpTipoTabelaPreco")
		.add(Projections.property("ttp.nrVersao"), "tipoTabelaPreco_nrVersao")
		.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco_tpSubtipoTabelaPreco")
		.add(Projections.property("tp.idTabelaPreco"), "idTabelaPreco")
		.add(Projections.property("tp.pcReajuste"), "pcReajuste")
		.add(Projections.property("tp.dtVigenciaInicial"), "dtVigenciaInicial")
		.add(Projections.property("tp.dtVigenciaFinal"), "dtVigenciaFinal")
		.add(Projections.property("tp.blEfetivada"), "blEfetivada")
		.add(Projections.property("p.nmPessoa"), "empresaByIdEmpresaCadastrada_pessoa_nmPessoa");

		DetachedCriteria dc = getCriteriaSimulacao(criteria);
		dc.setProjection(pl);
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCountSimulacao(TypedFlatMap criteria) {
		DetachedCriteria dc = getCriteriaSimulacao(criteria);
		dc.setProjection(Projections.rowCount());

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	private DetachedCriteria getCriteriaSimulacao(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("ttp.empresaByIdEmpresaCadastrada", "e");
		dc.createAlias("e.pessoa", "p");

		dc.add(Restrictions.isNotNull("tp.tabelaPreco"));

		Long idTipoTabelaPreco = criteria.getLong("tipoTabelaPreco.idTipoTabelaPreco");
		if (idTipoTabelaPreco != null) {
			dc.add(Restrictions.eq("ttp.idTipoTabelaPreco", idTipoTabelaPreco));
		}

		String tpTipoTabelaPreco = criteria.getString("tipoTabelaPreco.tpTipoTabelaPreco");
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		}

		Integer nrVersao = criteria.getInteger("tipoTabelaPreco.nrVersao");
		if (nrVersao != null) {
			dc.add(Restrictions.eq("ttp.nrVersao", nrVersao));
		}

		Long idSubtipoTabelaPreco = criteria.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco");
		if (idSubtipoTabelaPreco != null) {
			dc.add(Restrictions.eq("stp.idSubtipoTabelaPreco", idSubtipoTabelaPreco));
		}

		YearMonthDay dtReferencia = criteria.getYearMonthDay("dtReferencia");
		if (dtReferencia != null) {
			dc.add(Restrictions.le("tp.dtVigenciaInicial", dtReferencia));
			dc.add(Restrictions.ge("tp.dtVigenciaFinal", dtReferencia));
		}

		Long idEmpresaCadastrada = criteria.getLong("empresaByIdEmpresaCadastrada.idEmpresa");
		if (idEmpresaCadastrada != null) {
			dc.add(Restrictions.eq("ttp.empresaByIdEmpresaCadastrada.id", idEmpresaCadastrada));
		}

		dc.addOrder(Order.asc("ttp.tpTipoTabelaPreco"));
		dc.addOrder(Order.asc("ttp.nrVersao"));
		dc.addOrder(Order.asc("stp.tpSubtipoTabelaPreco"));
		return dc;
	}

	public Integer findUltimaVersaoTabela(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco) {

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
		.setProjection(Projections.max("ttp.nrVersao"))
		.createAlias("tp.tipoTabelaPreco", "ttp")
		.createAlias("tp.subtipoTabelaPreco", "stp")
		.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && result.size() > 0) {
			return (Integer) result.get(0);
		}

		return null;
	}

	public List findByTipoTabelaPreco(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Integer nrVersao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
		.createAlias("tp.tipoTabelaPreco", "ttp")
		.createAlias("tp.subtipoTabelaPreco", "stp")
		.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.eq("ttp.nrVersao", nrVersao))
		.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Busca Tabelas entre as versões informadas
	 * @param tpTipoTabelaPreco
	 * @param nrVersao
	 * @param nrVersaoFinal
	 * @return
	 */
	public List<TabelaPreco> findTabelaPrecoByNrVersao(String tpTipoTabelaPreco, Integer nrVersao, Long idTabelaPrecoOrigem) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
		.createAlias("tp.tipoTabelaPreco", "ttp")
		.createAlias("tp.subtipoTabelaPreco", "stp")
		.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.eq("ttp.nrVersao", nrVersao))
		.add(Restrictions.eq("tp.tabelaPreco.idTabelaPreco", idTabelaPrecoOrigem));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<TabelaPreco> findTabelaPrecoByIdCliente(Long idCliente, String tipoTabelaPreco, String subtipoTabelaPreco, DateTime dthAtual) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, Object> parameter = new HashMap<String, Object>();
		
		hql
		.append("SELECT tp ")
		.append("FROM ")
		.append(TabelaPreco.class.getSimpleName()).append(" tp ")
		.append("JOIN tp.tipoTabelaPreco ttp ")
		.append("JOIN tp.subtipoTabelaPreco stp, ")
		.append(TabelaDivisaoCliente.class.getSimpleName()).append(" tdc ")
		.append("JOIN tdc.divisaoCliente dc ")
		.append("JOIN dc.cliente c ")
		.append("WHERE ")
		.append("	 tp.idTabelaPreco = tdc.tabelaPreco.idTabelaPreco ")
		.append("AND c.idCliente =:idCliente ")
		.append("AND tp.dtVigenciaInicial <= :dthAtual ")
		.append("AND (tp.dtVigenciaFinal >= :dthAtual or tp.dtVigenciaFinal IS NULL)")
		.append("AND ttp.tpSituacao =:tpSituacao ")
		.append("AND ttp.tpTipoTabelaPreco =:tipoTabelaPreco ");
		
		parameter.put("idCliente", idCliente);
		parameter.put("tpSituacao", "A");
		parameter.put("dthAtual", dthAtual);
		parameter.put("tipoTabelaPreco", tipoTabelaPreco);
		
		if (StringUtils.isNotBlank(subtipoTabelaPreco)) {
			hql.append("AND stp.tpSubtipoTabelaPreco =:subtipoTabelaPreco ");
			parameter.put("subtipoTabelaPreco", subtipoTabelaPreco);
		}
		
		List<TabelaPreco> l = (List<TabelaPreco>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameter);
		
		return l;
	}
	
	public TabelaPreco findTabelaVigente(DomainValue tpTipoTabelaPreco, Long idSubtipoTabelaPreco, Boolean blEfetivada, YearMonthDay dtAtual){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "sttp");

		dc.add(Restrictions.eq("tp.blEfetivada", blEfetivada));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco.getValue()));
		dc.add(Restrictions.eq("sttp.idSubtipoTabelaPreco", idSubtipoTabelaPreco));

		dc.add(Restrictions.or(
								Restrictions.isNull("tp.dtVigenciaFinal"),
								Restrictions.gt("tp.dtVigenciaFinal", dtAtual)
							  ));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && result.size() > 0) {
			return (TabelaPreco) result.get(0);
		}

		return null;
	}


	public TabelaPreco findTabelaVigente(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Boolean blEfetivada, YearMonthDay dtReferencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "sttp");

		dc.add(Restrictions.eq("tp.blEfetivada", blEfetivada));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		dc.add(Restrictions.eq("sttp.tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));
		dc.add(Restrictions.le("tp.dtVigenciaInicial", dtReferencia));
		dc.add(Restrictions.or(
								Restrictions.isNull("tp.dtVigenciaFinal"),
								Restrictions.gt("tp.dtVigenciaFinal", dtReferencia)
							  ));

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && result.size() > 0) {
			return (TabelaPreco) result.get(0);
		}

		return null;
	}

	/**
	 * Busca o cliente relacionado a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return cliente relacionado
	 */
	public TypedFlatMap findClienteByIdTabelaPreco(Long idTabelaPreco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select new map( \n");
		hql.append("        c.idCliente as cliente_idCliente, \n");
		hql.append("        p.nrIdentificacao as cliente_pessoa_nrIdentificacao, \n");
		hql.append("        p.tpIdentificacao as cliente_pessoa_tpIdentificacao, \n");
		hql.append("        p.nmPessoa as cliente_pessoa_nmPessoa \n");
		hql.append("        ) \n");
		hql.append("   from ").append(getPersistentClass().getName()).append(" tp \n");
		hql.append("   join tp.tipoTabelaPreco ttp \n");
		hql.append("   join ttp.cliente c \n");
		hql.append("   join c.pessoa p \n");
		hql.append("  where tp.idTabelaPreco = ? ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), idTabelaPreco);
		if (result != null && result.size() > 0) {
			result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
			return (TypedFlatMap) result.get(0);
		}

		return null;
	}

	/**
	 * Busca a moeda relacionada a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return moeda relacionada
	 */
	public TypedFlatMap findMoedaByIdTabelaPreco(Long idTabelaPreco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select new map( \n");
		hql.append("        m.idMoeda as idMoeda, \n");
		hql.append("        m.sgMoeda as sgMoeda, \n");
		hql.append("        m.dsSimbolo as dsSimbolo \n");
		hql.append("        ) \n");
		hql.append("   from ").append(getPersistentClass().getName()).append(" tp \n");
		hql.append("   join tp.moeda m \n");
		hql.append("  where tp.idTabelaPreco = ? ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), idTabelaPreco);
		if (result != null && result.size() > 0) {
			result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
			return (TypedFlatMap) result.get(0);
		}

		return null;
	}

	/**
	 * Busca o tipo e subtipo relacionada a tabela de preco informada.
	 *
	 * @param idTabelaPreco identificador da tabela preco
	 * @return tipo e subtipo relacionada
	 */
	public TypedFlatMap findTiposByIdTabelaPreco(Long idTabelaPreco) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select new map( \n");
		hql.append("        ttp.idTipoTabelaPreco as tipoTabelaPreco_idTipoTabelaPreco, \n");
		hql.append("        ttp.tpTipoTabelaPreco as tipoTabelaPreco_tpTipoTabelaPreco, \n");
		hql.append("        stp.idSubtipoTabelaPreco as subtipoTabelaPreco_idSubtipoTabelaPreco \n");
		hql.append("        ) \n");
		hql.append("   from ").append(getPersistentClass().getName()).append(" tp \n");
		hql.append("   join tp.tipoTabelaPreco ttp \n");
		hql.append("   join tp.subtipoTabelaPreco stp \n");
		hql.append("  where tp.idTabelaPreco = ? ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), idTabelaPreco);
		if (result != null && result.size() > 0) {
			result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
			return (TypedFlatMap) result.get(0);
		}

		return null;
	}

	public Integer getCountEfetivados(List ids){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tp")
			.setProjection(Projections.rowCount())
			.add(Restrictions.in("tp.id", ids))
			.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Boolean isEfetivado(Long idTabelaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),
				"tp").setProjection(Projections.rowCount()).add(
				Restrictions.eq("tp.id", idTabelaPreco)).add(
				Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		 Integer efetivados = getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);

		return efetivados >0? true : false;

	}
	
	public void removeByIdsTabelaPrecoAnexo(List ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + TabelaPrecoAnexo.class.getName() + " WHERE idTabelaPrecoAnexo IN (:id)", ids);
	}

	public TabelaPrecoAnexo findTabelaPrecoAnexoById(Long idTabelaPrecoAnexo) {
		return (TabelaPrecoAnexo) getAdsmHibernateTemplate().load(TabelaPrecoAnexo.class, idTabelaPrecoAnexo);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<TabelaPrecoAnexo> findPaginatedTabelaPrecoAnexo(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append("tpa.idTabelaPrecoAnexo as idTabelaPrecoAnexo");
		hql.append(",tpa.nmArquivo as nmArquivo");
		hql.append(",tpa.dsAnexo as dsAnexo");
		hql.append(",tpa.dhCriacao as dhInclusao");
		hql.append(",u.usuarioADSM.nmUsuario as nmUsuario");
		hql.append(") FROM ");
		hql.append(TabelaPrecoAnexo.class.getName());
		hql.append(" as tpa ");
		hql.append("inner join tpa.tabelaPreco tp ");
		hql.append("inner join tpa.usuario u ");
		hql.append("WHERE ");
		hql.append("tp.idTabelaPreco = :idTabelaPreco ");
		hql.append("ORDER BY tpa.dhCriacao.value DESC ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountTabelaPrecoAnexo(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM tabela_preco_anexo WHERE id_tabela_preco = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{criteria.get("idTabelaPreco")});
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cancelPendencia(Long idTabelaPreco){
		Map parametersValues = new HashMap();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql("update tabela_preco set id_pendencia = null where id_tabela_preco = :idTabelaPreco", parametersValues);
	}

	public void updateEfetivarTabelaPreco(Long idTabelaPreco){
		Map parametersValues = new HashMap();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql("update tabela_preco set bl_efetivada = 'S' where id_tabela_preco = :idTabelaPreco", parametersValues);
	}

	public void updateDescricaoTabelaPreco(Long idTabelaPreco, String novaDescricao){
		Map parametersValues = new HashMap();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		parametersValues.put("descricao", novaDescricao);
		getAdsmHibernateTemplate().executeUpdateBySql("update tabela_preco set ds_descricao = :descricao where id_tabela_preco = :idTabelaPreco and ds_descricao <> :descricao", parametersValues);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cancelPendenciaDesefetivacao(Long idTabelaPreco) {
		Map parametersValues = new HashMap();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql("update tabela_preco set id_pendencia_desefetivacao = null where id_tabela_preco = :idTabelaPreco", parametersValues);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void cancelPendenciaEfetivacao(Long idTabelaPreco) {
		Map parametersValues = new HashMap();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		getAdsmHibernateTemplate().executeUpdateBySql("update tabela_preco set id_pendencia_efetivacao = null where id_tabela_preco = :idTabelaPreco", parametersValues);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage<TabelaPreco> findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> query = montaQuery(paginatedQuery.getCriteria());
		paginatedQuery.addCriteria((Map)query.get("criteria"));
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, (String)query.get("hql"));
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> montaQuery(Map criteria){
		StringBuilder sql = new StringBuilder();
		sql.append(" from ").append(getPersistentClass().getName()).append(" as tabelaPreco ");
		sql.append(" inner join fetch tabelaPreco.tipoTabelaPreco tipoTabelaPreco ");
		sql.append(" inner join fetch tipoTabelaPreco.empresaByIdEmpresaCadastrada empresa ");
		sql.append(" inner join fetch empresa.pessoa pessoa ");
		sql.append(" inner join fetch empresa.pessoa pessoa ");
		sql.append(" inner join fetch tabelaPreco.subtipoTabelaPreco subtipoTabelaPreco ");

		sql.append(" where 1=1 ");

		Map<String, Object> newCriteria = new HashMap<String, Object>();
		newCriteria.putAll(criteria);

		if(MapUtils.getObject(criteria, "idSubtipoTabelaPreco") != null) {
			sql.append(" and subtipoTabelaPreco.id = :idSubtipoTabelaPreco ");
		}
		if(MapUtils.getObject(criteria, "idTipoTabelaPreco") != null) {
			sql.append(" and tipoTabelaPreco.id = :idTipoTabelaPreco ");
		}
		if(MapUtils.getObject(criteria, "dtVigencia") != null) {
			sql.append(" and :dtVigencia between tabelaPreco.dtVigenciaInicial and tabelaPreco.dtVigenciaFinal ");
		}
		if(MapUtils.getObject(criteria, "blEfetivada") != null) {
			if(MapUtils.getString(criteria, "blEfetivada").equals("N")) {
				newCriteria.put("blEfetivada", Boolean.FALSE);
			} else {
				newCriteria.put("blEfetivada", Boolean.TRUE);
			}
			sql.append(" and tabelaPreco.blEfetivada = :blEfetivada ");
		}

		sql.append(" order by tipoTabelaPreco.tpTipoTabelaPreco, tipoTabelaPreco.nrVersao ");

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("hql", sql.toString());
		retorno.put("criteria", newCriteria);
		return retorno;
	}

	public List findTabelasInPipelineClienteSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select distinct tapr ");
		hql.append("from PipelineClienteSimulacao pics ");
		hql.append("join pics.pipelineCliente as picl ");
		hql.append("join pics.tabelaPreco tapr ");
		hql.append("where pics.tabelaPreco is not null ");
		hql.append("and picl = ? ");
		hql.append("and tapr.blEfetivada = 'S' ");
		params.add(pipelineCliente);

		return getHibernateTemplate().find(hql.toString(), params.toArray());
	}

	public DadosTabelaPrecoDTO findFirstTabelaEfetivadaByIdPipelineCliente(PipelineCliente pipelineCliente) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		hql.append(" select new Map(tapr.dtVigenciaInicial as dtVigenciaInicial ");
		hql.append(" 	  ,titp.tpTipoTabelaPreco as tpTipoTabelaPreco ");
		hql.append(" 	  ,titp.nrVersao as nrVersao ");
		hql.append(" 	  ,sutp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco) ");
		hql.append(" from PipelineClienteSimulacao pcsi ");
		hql.append(" join pcsi.tabelaPreco as tapr ");
		hql.append(" join tapr.tipoTabelaPreco as titp ");
		hql.append(" join tapr.subtipoTabelaPreco as sutp ");
		hql.append(" join pcsi.pipelineCliente picl ");
		hql.append(" where picl.idPipelineCliente = :pipelineCliente ");
		hql.append(" and tapr.blEfetivada = 'S' ");
		hql.append(" group by tapr.dtVigenciaInicial, titp.tpTipoTabelaPreco, titp.nrVersao ,sutp.tpSubtipoTabelaPreco, tapr.blEfetivada ");
		hql.append(" having tapr.dtVigenciaInicial = ( ");
		hql.append(" 									select min(tapr.dtVigenciaInicial) ");
		hql.append(" 									from PipelineClienteSimulacao pcsi1 ");
		hql.append(" 									join pcsi1.tabelaPreco as tapr1 ");
		hql.append(" 									where pcsi1.pipelineCliente = :pipelineCliente ");
		hql.append(" 									and tapr1.blEfetivada = 'S' ");
		hql.append(" 								 ) ");

		params.put("pipelineCliente", pipelineCliente.getIdPipelineCliente());
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(DadosTabelaPrecoDTO.class);
		List transformed = transformer.transformListResult(result);
		if (CollectionUtils.isNotEmpty(transformed)) {
			return (DadosTabelaPrecoDTO) transformed.get(0);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public Integer getRowCountForManterGruposRegioes(Map criteria) {
		Map<String, Object> query = montaQuery(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery((String)query.get("hql"), (Map)query.get("criteria"));
	}

	public TabelaPreco findTabelaCliente(Long idDivisaoCliente, Long idServico) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select tp ");
		sb.append(" from " + TabelaDivisaoCliente.class.getName() + " tdc ");
		sb.append(" inner join tdc.tabelaPreco tp ");
		sb.append(" inner join fetch tp.tipoTabelaPreco ttp ");
		sb.append(" inner join fetch tp.subtipoTabelaPreco stp ");
		sb.append(" where ");
		sb.append(" 	tdc.divisaoCliente.idDivisaoCliente = :idDivisaoCliente ");
		sb.append(" and	tdc.servico.idServico = :idServico ");
		sb.append(" and tp.blEfetivada = :blEfetivada ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDivisaoCliente", idDivisaoCliente);
		params.put("idServico", idServico);
		params.put("blEfetivada", Boolean.TRUE);

		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), params);
	}
	
	public TabelaPreco findTabelaPrecoByIdDivisaoCliente(Long idDivisaoCliente, Long idServico) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select tp ");
		sb.append(" from " + TabelaDivisaoCliente.class.getName() + " tdc ");
		sb.append(" inner join tdc.tabelaPreco tp ");
		sb.append(" inner join fetch tp.tipoTabelaPreco ttp ");
		sb.append(" inner join fetch tp.subtipoTabelaPreco stp ");
		sb.append(" where ");
		sb.append(" 	tdc.divisaoCliente.idDivisaoCliente = :idDivisaoCliente ");
		sb.append(" and	tdc.servico.idServico = :idServico ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDivisaoCliente", idDivisaoCliente);
		params.put("idServico", idServico);
		
		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), params);
	}

	public TabelaPreco findTabelaSimulacao(Long idSimulacao) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select tp ");
		sb.append(" from " + Simulacao.class.getName() + " s ");
		sb.append(" inner join s.tabelaPreco tp ");
		sb.append(" inner join fetch tp.tipoTabelaPreco ttp ");
		sb.append(" inner join fetch tp.subtipoTabelaPreco stp ");
		sb.append(" where ");
		sb.append(" 	s.idSimulacao = :idSimulacao ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idSimulacao", idSimulacao);

		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), params);
	}

	public TabelaPreco findTabelaXVigenteByServico(Long idServico) {
		StringBuilder sb = new StringBuilder();
		sb.append(" from " + TabelaPreco.class.getName() + " tp ");
		sb.append(" inner join fetch tp.tipoTabelaPreco ttp ");
		sb.append(" inner join fetch tp.subtipoTabelaPreco stp ");
		sb.append(" where ");
		sb.append(" 	stp.tpSubtipoTabelaPreco = 'X' ");
		sb.append(" and	ttp.tpTipoTabelaPreco in ('T','M','A') ");
		sb.append(" and	ttp.servico.idServico = :idServico ");
		sb.append(" and tp.blEfetivada = :blEfetivada ");
		sb.append(" and tp.dtVigenciaInicial <= :dtVigencia ");
		sb.append(" and ( ");
		sb.append("			tp.dtVigenciaFinal is null ");
		sb.append("		or 	tp.dtVigenciaFinal >= :dtVigencia) ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idServico", idServico);
		params.put("blEfetivada", Boolean.TRUE);
		params.put("dtVigencia", JTDateTimeUtils.getDataAtual());

		return (TabelaPreco)getAdsmHibernateTemplate().findUniqueResult(sb.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<ParcelaPreco> findParcelasByTabelaPreco(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder("SELECT p FROM TabelaPreco t JOIN t.tabelaPrecoParcelas tt JOIN tt.parcelaPreco p ");
		query.append("WHERE t.idTabelaPreco = :id");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("id", idTabelaPreco);
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<TabelaPreco> findTabelaPrecoSuggest(String tpTipoTabelaPreco, String tpSubtipoTabelaPreco, Integer nrVersao, DomainValue tpModal, boolean apenasVigentes) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder();
		query.append(" SELECT tp FROM TabelaPreco tp ");
		query.append(" INNER JOIN FETCH tp.tipoTabelaPreco ttp ");
		query.append("       JOIN FETCH ttp.servico s ");
		query.append(" INNER JOIN FETCH tp.subtipoTabelaPreco stp ");
		query.append(" INNER JOIN FETCH tp.moeda m ");
		query.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(tpTipoTabelaPreco)){
			query.append(" AND ttp.tpTipoTabelaPreco = :tpTipoTabelaPreco ");
			parametros.put("tpTipoTabelaPreco", tpTipoTabelaPreco);
		}
		if(nrVersao != null){
			query.append(" AND ttp.nrVersao = :nrVersao ");
			parametros.put("nrVersao", nrVersao);
		}
		if(StringUtils.isNotBlank(tpSubtipoTabelaPreco)){
			query.append(" AND stp.tpSubtipoTabelaPreco = :tpSubtipoTabelaPreco ");
			parametros.put("tpSubtipoTabelaPreco", tpSubtipoTabelaPreco);
		}
		
		if(tpModal != null){
			query.append(" AND s.tpModal = :tpModal ");
			parametros.put("tpModal", tpModal.getValue());
		}
		
		if (apenasVigentes){
		    query.append(" AND tp.dtVigenciaInicial <= :dtVigencia ");
	        query.append(" AND ( ");
	        query.append("          tp.dtVigenciaFinal is null ");
	        query.append("      OR  tp.dtVigenciaFinal >= :dtVigencia) ");
	        
	        parametros.put("dtVigencia", JTDateTimeUtils.getDataAtual());
		}else{
		    query.append("AND tp.blEfetivada = :blEfetivada");
		    parametros.put("blEfetivada", Boolean.TRUE);
		}
		
				
		query.append(" ORDER BY ttp.tpTipoTabelaPreco, ttp.nrVersao, stp.tpSubtipoTabelaPreco ");
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}
	
	
	
	public Boolean validatePendenciaWorkflow(Long idTabelaPreco) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT 1 FROM ");
		hql.append(getPersistentClass().getName()).append(" tp ");
		hql.append("WHERE ");
		hql.append("idTabelaPreco = ? ");
		hql.append("AND pendencia IS NOT NULL ");

		List<?> registros = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idTabelaPreco});
		return !registros.isEmpty();
	}
	
	@SuppressWarnings("unchecked")
	public List<ParcelaPreco> findByIdTabelaPrecoTipoPrecificacao(Long idTabelaPreco, List<String> tiposPrecificacao) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT pp ");
		query.append("FROM TabelaPrecoParcela tpp  ");
		query.append("JOIN tpp.tabelaPreco tp ");
		query.append("JOIN tpp.parcelaPreco pp ");
		query.append("WHERE tp.idTabelaPreco = :idTabelaPreco ");
		query.append("AND pp.tpSituacao = :tpSituacao ");
		if(CollectionUtils.isNotEmpty(tiposPrecificacao)) {
			query.append("AND pp.tpPrecificacao in (:tpPrecificacao) ");
		}
		query.append("ORDER BY pp.nmParcelaPreco ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabelaPreco", idTabelaPreco);
		parametros.put("tpSituacao", "A");
		if(CollectionUtils.isNotEmpty(tiposPrecificacao)) {
			parametros.put("tpPrecificacao", tiposPrecificacao);
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}

	public void updateVigenciaInicialTabelaPreco(Long idTabelaPreco, YearMonthDay vigencia){
		updateVigenciaTabelaPreco(idTabelaPreco, "dt_vigencia_inicial", vigencia);
	}
	
	public void updateVigenciaFinalTabelaPreco(Long idTabelaPreco, YearMonthDay vigencia){
		updateVigenciaTabelaPreco(idTabelaPreco, "dt_vigencia_final", vigencia);
	}

	private void updateVigenciaTabelaPreco(Long idTabelaPreco, String vigenciaField, YearMonthDay vigencia) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		parametersValues.put("vigencia", vigencia);
		
		StringBuilder sql = new StringBuilder();
		sql.append("update tabela_preco set ");
		sql.append(vigenciaField);
		sql.append(" =:vigencia where id_tabela_preco = :idTabelaPreco");
		
		getAdsmHibernateTemplate().executeUpdateBySql(
				sql.toString()
				, parametersValues);
	}
	
	public BigDecimal getPercentualSugerido(Long  idTabelaDivisaoCliente, Long idTabelaNova){
		String query = new StringBuilder()
							.append(" select tp.pc_reajuste ")
							.append("   from tabela_preco tp, ")
							.append("        tabela_divisao_cliente tdc ")
							.append(" where  tp.ID_TABELA_PRECO_ORIGEM = tdc.id_tabela_preco ")
							.append("   and  tdc.id_tabela_divisao_cliente = ? ")
							.append("   and  tp.ID_TABELA_PRECO = ? ")
							.toString();
		
		ResultSetExtractor extractor = new ResultSetExtractor() {			
			@Override
			public BigDecimal extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
			}
		};	
		
		return (BigDecimal) jdbcTemplate.query(query, new Object[]{idTabelaDivisaoCliente, idTabelaNova}, extractor);
	}
	
	public List<Map<String,Object>> findRelatorioTabelaPreco(Map<String, Object> criteria){
		StringBuilder sql = new StringBuilder()
		
		.append(" select ")
		.append(" 				ttp.tp_tipo_tabela_preco ")
		.append(" 				  || ttp.nr_versao ")
		.append(" 				  || '-' ")
		.append(" 				  || stp.tp_subtipo_tabela_preco AS TABELA_PRECO, ")
		.append("				  SUBSTR(REGEXP_SUBSTR(s.ds_servico_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(s.ds_servico_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) AS SERVICO, ")
		.append(" 				  tp.tp_servico AS TIPO_SERVICO, ")
		.append(" 				  tp.tp_categoria AS CATEGORIA, ")
		.append(" 				  tp.ds_descricao AS DESCRICAO, ")
		.append(" 				  (select SUBSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) ")
		.append(" 				  from dominio d, valor_dominio vd ")
		.append(" 				  where d.id_dominio = vd.id_dominio ")
		.append(" 				  and d.nm_dominio='DM_CALCULO_FRETE_PESO' ")
		.append(" 				  and vd.vl_valor_dominio = tp.tp_calculo_frete_peso) AS TIPO_CALCULO, ")
		.append(" 				  tp.ps_minimo AS PESO_MINIMO, ")
		.append(" 				  (select SUBSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR(vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) ")
		.append(" 				  from dominio d, valor_dominio vd ")
		.append(" 				  where d.id_dominio = vd.id_dominio ")
		.append(" 				  and d.nm_dominio='DM_TIPO_CALCULO_PEDAGIO' ")
		.append(" 				  and vd.vl_valor_dominio = tp.tp_calculo_pedagio) AS TIPO_DE_CALCULO_PEDAGIO, ")
		.append(" 				  tp.pc_desconto_frete_minimo AS DESCONTO_FRETE_MINIMO, ")
		.append(" 				  (select ")
		.append(" 				   ttp.tp_tipo_tabela_preco ")
		.append(" 				   || ttp.nr_versao ")
		.append(" 				   || '-' ")
		.append(" 				   || stp.tp_subtipo_tabela_preco AS TABELA_BASE ")
		.append(" 				   from  tabela_preco tpo ")
		.append(" 				   join tipo_tabela_preco ttp on (tpo.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco) ")
		.append(" 				   join subtipo_tabela_preco stp on (tpo.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco) ")
		.append(" 				   where tpo.id_tabela_preco = tp.id_tabela_preco_origem) AS TABELA_BASE, ")
		.append(" 				  tp.pc_reajuste AS PERCENTUAL_DE_REAJUSTE, ")
		.append(" 				  CASE tp.bl_efetivada WHEN 'S' THEN 'SIM' ELSE 'NÃO' END AS EFETIVADA, ")
		.append(" 				  tp.dt_vigencia_inicial AS DATA_DE_VIGENCIA_INICIAL, ")
		.append(" 				  tp.dt_vigencia_final AS DATA_DE_VIGENCIA_FINAL, ")
		.append(" 				  tp.ob_tabela_preco AS OBSERVACOES, ")
		.append(" 				  o.ds_descricao AS INFORMACOES, ")
		.append(queryListParcelas())
		.append("   AS PARCELAS_CADASTRADAS ")
		.append(" 				from  tabela_preco tp ")
		.append(" 				  join tipo_tabela_preco ttp on (tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco) ")
		.append(" 				  join subtipo_tabela_preco stp on (tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco) ")
		.append(" 				  join servico s on (ttp.ID_SERVICO = s.ID_SERVICO) ")
		.append(" 				  left join observacao o on (tp.id_tabela_preco = o.id_tabela and o.nm_tabela = 'TABELA_PRECO') ")
		.append(" 				  where 1=1 ");

		Map<String, Object> parametersValues = new HashMap<String, Object>();
		if(contemString(criteria, "tipoTabela")){
			sql.append(" and ttp.tp_tipo_tabela_preco = :tipoTabela ");
			parametersValues.put("tipoTabela", criteria.get("tipoTabela"));
		}
		if(contemString(criteria, "servico")){
			sql.append(" and ttp.ID_SERVICO = :servico ");
			parametersValues.put("servico", criteria.get("servico"));
		}
		if(contemString(criteria, "tipoTabelaBase")){
			sql.append(" and ttp.tp_tipo_tabela_preco = :tipoTabelaBase and  tp.id_tabela_preco_origem is not null");
			parametersValues.put("tipoTabelaBase", criteria.get("tipoTabelaBase"));
		}
		if(contemString(criteria, "tipoServico")){
			sql.append(" and tp.tp_servico = :tipoServico ");
			parametersValues.put("tipoServico", criteria.get("tipoServico"));
		}
		if(contemString(criteria, "categoria")){
			sql.append(" and tp.tp_categoria = :categoria ");
			parametersValues.put("categoria", criteria.get("categoria"));
		}
		if(contemString(criteria, "tipoCalculo")){
			sql.append(" and tp.tp_calculo_frete_peso = :tipoCalculo ");
			parametersValues.put("tipoCalculo", criteria.get("tipoCalculo"));
		}
		if(contemString(criteria, "tipoCalculoPedagio")){
			sql.append(" and tp.tp_calculo_pedagio = :tipoCalculoPedagio ");
			parametersValues.put("tipoCalculoPedagio", criteria.get("tipoCalculoPedagio"));
		}
		if(contemString(criteria, "efetivado")){
			sql.append(" and tp.bl_efetivada = :efetivado ");
			parametersValues.put("efetivado", criteria.get("efetivado"));
		}
	
		String sqlStr =  sql.toString();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sqlStr, parametersValues, getConfigureSqlQueryRelatorioTabelaPreco());
	}

	private ConfigureSqlQuery getConfigureSqlQueryRelatorioTabelaPreco() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
      			sqlQuery.addScalar("TABELA_PRECO", Hibernate.STRING);
      			sqlQuery.addScalar("SERVICO", Hibernate.STRING);
      			sqlQuery.addScalar("TIPO_SERVICO", Hibernate.STRING);
      			sqlQuery.addScalar("CATEGORIA", Hibernate.STRING);
      			sqlQuery.addScalar("DESCRICAO", Hibernate.STRING);
      			sqlQuery.addScalar("TIPO_CALCULO", Hibernate.STRING);
      			sqlQuery.addScalar("TIPO_DE_CALCULO_PEDAGIO", Hibernate.STRING);
      			sqlQuery.addScalar("DESCONTO_FRETE_MINIMO", Hibernate.STRING);
      			sqlQuery.addScalar("TABELA_BASE", Hibernate.STRING);
      			sqlQuery.addScalar("PERCENTUAL_DE_REAJUSTE", Hibernate.STRING);
      			sqlQuery.addScalar("EFETIVADA", Hibernate.STRING);
      			sqlQuery.addScalar("DATA_DE_VIGENCIA_INICIAL", Hibernate.STRING);
      			sqlQuery.addScalar("DATA_DE_VIGENCIA_FINAL", Hibernate.STRING);
      			sqlQuery.addScalar("OBSERVACOES", Hibernate.STRING);
      			sqlQuery.addScalar("INFORMACOES", Hibernate.STRING);
      			sqlQuery.addScalar("PARCELAS_CADASTRADAS", Hibernate.STRING);
			}
		};
		
		return csq;
	}
	

	private Boolean contemString(Map<String, Object> criteria, String strFind) {
		return strFind!=null && criteria.containsKey(strFind) && criteria.get(strFind) != null && !"".equals(criteria.get(strFind));
	}

	private String queryListParcelas(){
		
		String sql = new StringBuilder()
		.append(" (SELECT rtrim (xmlagg (xmlelement (e, SUBSTR(REGEXP_SUBSTR( parcela_preco.NM_PARCELA_PRECO_I, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( parcela_preco.NM_PARCELA_PRECO_I, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) ")
		.append(" 		    || ' (' ")
		.append(" 		    || SUBSTR(REGEXP_SUBSTR( vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( vd.ds_valor_dominio_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) ")
		.append(" 		    || ')' ")
		.append(" 		    || ';')).extract ('//text()'), ',') enames ")
		.append(" 		  FROM tabela_preco_parcela, ")
		.append(" 		    parcela_preco, ")
		.append(" 		    dominio d, ")
		.append(" 		    valor_dominio vd ")
		.append(" 		  WHERE tabela_preco_parcela.ID_TABELA_PRECO = tp.id_tabela_preco ")
		.append(" 		  AND d.nm_dominio                           = 'DM_TIPO_PARCELA' ")
		.append(" 		  AND d.id_dominio                           = vd.id_dominio ")
		.append(" 		  AND vd.vl_valor_dominio                    = tp_parcela_preco ")
		.append(" 		  AND parcela_preco.ID_PARCELA_PRECO         = tabela_preco_parcela.ID_PARCELA_PRECO ) ")
		.toString();
		
		return sql;
	}

	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> findRelatorioAlteracoesTarifas(TypedFlatMap criteria) {

		Map<String, Object> paramValues = new HashMap<String, Object>();
		paramValues.put("idEmpresa", MapUtilsPlus.getLongOnMap(criteria, "ciaAerea", "idEmpresa"));
		paramValues.put("idAeroportoOrigem", MapUtilsPlus.getLongOnMap(criteria, "aeroportoOrigem", "idAeroporto"));
		paramValues.put("tipoServico", MapUtilsPlus.getStringOnMap(criteria, "tipoServico", "value", null));
		paramValues.put("dtVigenciaInicial", new YearMonthDay(criteria.get("periodoInicial")));
		paramValues.put("dtVigenciaFinal", new YearMonthDay(criteria.get("periodoFinal")));

		Long idAeroportoDestino = MapUtilsPlus.getLongOnMap(criteria, "aeroportoDestino", "idAeroporto");
		if(idAeroportoDestino != null){
			paramValues.put("idAeroportoDestino", idAeroportoDestino);
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a_origem.sg_aeroporto AS origem, \n");
		sql.append("  a_destino.sg_aeroporto     AS destino, \n");
		sql.append("  tp_a.ds_PARCELA as parcela, \n");
		sql.append("  to_char(tp_a.vl_parcela, 'FM999G999G990D90') AS anterior, \n");
		sql.append("  to_char(tp_b.vl_parcela, 'FM999G999G990D90') AS alterado, \n");
		sql.append("  CASE \n");
		sql.append("    WHEN tp_b.vl_parcela < tp_a.vl_parcela \n");
		sql.append("    THEN 'Desconto' \n");
		sql.append("    ELSE 'Acréscimo' \n");
		sql.append("  END                                                                AS indicador, \n");
		sql.append("  to_char(ROUND((tp_b.vl_parcela * 100 /tp_a.vl_parcela)-100 ,2), 'FM999G999G990D90') || '%'   AS reajuste, \n");
		sql.append("  to_char(tp_b.dt_vigencia_inicial,'dd/mm/yyyy')                                           AS dataAlteracao, \n");
		sql.append("  e.sg_empresa                                           AS ciaAerea \n");
		sql.append("FROM \n");
		sql.append("  ( ").append(getSqlTabelaPrecoRelatorioAlteracoesTarifas(paramValues)).append(" ) tp_a, \n");
		sql.append("  ( ").append(getSqlTabelaPrecoRelatorioAlteracoesTarifas(paramValues)).append(" ) tp_b, \n");
		sql.append("  rota_preco rp, \n");
		sql.append("  aeroporto a_origem, \n");
		sql.append("  aeroporto a_destino, \n");
		sql.append("  empresa e \n");
		sql.append("WHERE 1                            =1 \n");
		sql.append("AND tp_a.DT_VIGENCIA_FINAL +1      = tp_b.DT_VIGENCIA_INICIAL \n");
		sql.append("AND tp_b.DT_VIGENCIA_inicial      >= :dtVigenciaInicial \n");
		sql.append("AND tp_b.DT_VIGENCIA_inicial      <= :dtVigenciaFinal \n");
		sql.append("AND tp_a.id_rota_preco             = tp_b.id_rota_preco \n");
		sql.append("AND tp_a.id_rota_preco             = rp.id_rota_preco \n");
		sql.append("AND rp.id_aeroporto_origem         = a_origem.id_aeroporto \n");
		sql.append("AND rp.id_aeroporto_origem         = :idAeroportoOrigem \n");
		sql.append("AND rp.id_aeroporto_destino        = a_destino.id_aeroporto \n");
		if (idAeroportoDestino != null){
		    sql.append("AND rp.id_aeroporto_destino        = :idAeroportoDestino \n");
		}
		sql.append("AND tp_a.id_parcela_preco          = tp_b.id_parcela_preco \n");
		sql.append("AND tp_a.vl_parcela               <> tp_b.vl_parcela \n");
		sql.append("AND tp_a.ID_EMPRESA_CADASTRADA    = e.id_empresa \n");
		
		sql.append("AND ( (tp_a.id_produto_especifico IS NULL \n");
		sql.append("AND tp_a.vl_faixa_progressiva      = tp_b.vl_faixa_progressiva) \n");
		sql.append("OR (tp_a.vl_faixa_progressiva     IS NULL \n");
		sql.append("AND tp_a.id_produto_especifico     = tp_b.id_produto_especifico) \n");
		sql.append("OR (tp_a.vl_faixa_progressiva     IS NULL \n");
		sql.append("AND tp_b.vl_faixa_progressiva     IS NULL \n");
		sql.append("AND tp_a.id_produto_especifico    IS NULL \n");
		sql.append("AND tp_b.id_produto_especifico    IS NULL) )");
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), paramValues, getConfigureSqlQueryRelatorioAlteracoesTarifas());
	}

	private String getSqlTabelaPrecoRelatorioAlteracoesTarifas(Map<String, Object> paramValues){
	    
	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT pf.id_rota_preco, \n");
        sql.append("    vi18n(pp.ds_parcela_preco_i) AS ds_parcela, \n");
        sql.append("    tpp.id_parcela_preco, \n");
        sql.append("    tp.dt_vigencia_inicial, \n");
        sql.append("    tp.dt_vigencia_final, \n");
        sql.append("    pf.VL_PRECO_FRETE AS vl_parcela, \n");
        sql.append("    NULL              AS id_produto_especifico, \n");
        sql.append("    NULL              AS vl_faixa_progressiva, \n");
        sql.append("    ttp.ID_EMPRESA_CADASTRADA \n");
        sql.append("  FROM tabela_preco tp, \n");
        sql.append("    tabela_preco_parcela tpp, \n");
        sql.append("    preco_frete pf, \n");
        sql.append("    tipo_tabela_preco ttp, \n");
        sql.append("    subtipo_tabela_preco stp, \n");
        sql.append("    parcela_preco pp \n");
        sql.append("  WHERE 1                         =1 \n");
        sql.append("  AND tp.id_tabela_preco          = tpp.id_tabela_preco \n");
        sql.append("  AND pp.id_parcela_preco         = tpp.id_parcela_preco \n");
        sql.append("  AND tp.tp_servico = :tipoServico \n");
        sql.append("  AND TP.BL_EFETIVADA            = 'S' \n");
        sql.append("  AND pf.ID_TABELA_PRECO_PARCELA  = tpp.ID_TABELA_PRECO_PARCELA \n");
        sql.append("  and ttp.ID_EMPRESA_CADASTRADA = :idEmpresa \n");
        sql.append("  AND ttp.ID_TIPO_TABELA_PRECO    = tp.ID_TIPO_TABELA_PRECO \n");
        sql.append("  AND stp.ID_SUBTIPO_TABELA_PRECO = tp.ID_SUBTIPO_TABELA_PRECO \n");
        sql.append("  AND ttp.TP_TIPO_TABELA_PRECO    = 'C' \n");
        sql.append("  AND stp.TP_SUBTIPO_TABELA_PRECO = 'L' \n");
        sql.append("  UNION \n");
        sql.append("  SELECT vfp.id_rota_preco, \n");
        sql.append("    vi18n(pp.ds_parcela_preco_i) \n");
        sql.append("    ||': ' \n");
        sql.append("    ||fp.VL_FAIXA_PROGRESSIVA \n");
        sql.append("    || ' Kg'AS ds_parcela, \n");
        sql.append("    tpp.id_parcela_preco, \n");
        sql.append("    vfp.DT_VIGENCIA_PROMOCAO_INICIAL, \n");
        sql.append("    vfp.DT_VIGENCIA_PROMOCAO_FINAL, \n");
        sql.append("    vfp.VL_FIXO             AS vl_parcela, \n");
        sql.append("    NULL                    AS id_produto_especifico, \n");
        sql.append("    fp.VL_FAIXA_PROGRESSIVA AS vl_faixa_progressiva, \n");
        sql.append("    ttp.ID_EMPRESA_CADASTRADA \n");
        sql.append("  FROM tabela_preco tp, \n");
        sql.append("    tabela_preco_parcela tpp, \n");
        sql.append("    tipo_tabela_preco ttp, \n");
        sql.append("    subtipo_tabela_preco stp, \n");
        sql.append("    faixa_progressiva fp, \n");
        sql.append("    valor_faixa_progressiva vfp, \n");
        sql.append("    parcela_preco pp \n");
        sql.append("  WHERE 1                               =1 \n");
        sql.append("  AND tp.id_tabela_preco                = tpp.id_tabela_preco \n");
        sql.append("  AND pp.id_parcela_preco               = tpp.id_parcela_preco \n");
        sql.append("  AND fp.ID_TABELA_PRECO_PARCELA        = tpp.ID_TABELA_PRECO_PARCELA \n");
        sql.append("  AND tp.tp_servico = :tipoServico \n");
        sql.append("  AND TP.BL_EFETIVADA            = 'S' \n");
        sql.append("  AND vfp.ID_FAIXA_PROGRESSIVA          = fp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("  AND ttp.ID_TIPO_TABELA_PRECO          = tp.ID_TIPO_TABELA_PRECO \n");
        sql.append("  AND stp.ID_SUBTIPO_TABELA_PRECO       = tp.ID_SUBTIPO_TABELA_PRECO \n");
        sql.append("  and ttp.ID_EMPRESA_CADASTRADA = :idEmpresa \n");
        sql.append("  AND fp.ID_PRODUTO_ESPECIFICO         IS NULL \n");
        sql.append("  AND ttp.TP_TIPO_TABELA_PRECO          = 'C' \n");
        sql.append("  AND stp.TP_SUBTIPO_TABELA_PRECO       = 'L' \n");
        sql.append("  UNION \n");
        sql.append("  SELECT vfp.id_rota_preco, \n");
        sql.append("    'TE' \n");
        sql.append("    ||pe.NR_TARIFA_ESPECIFICA AS ds_parcela, \n");
        sql.append("    tpp.id_parcela_preco, \n");
        sql.append("    vfp.DT_VIGENCIA_PROMOCAO_INICIAL, \n");
        sql.append("    vfp.DT_VIGENCIA_PROMOCAO_FINAL, \n");
        sql.append("    vfp.VL_FIXO              AS vl_parcela, \n");
        sql.append("    fp.ID_PRODUTO_ESPECIFICO AS id_produto_especifico, \n");
        sql.append("    NULL                     AS vl_faixa_progressiva, \n");
        sql.append("    ttp.ID_EMPRESA_CADASTRADA \n");
        sql.append("  FROM tabela_preco tp, \n");
        sql.append("    tabela_preco_parcela tpp, \n");
        sql.append("    tipo_tabela_preco ttp, \n");
        sql.append("    subtipo_tabela_preco stp, \n");
        sql.append("    faixa_progressiva fp, \n");
        sql.append("    valor_faixa_progressiva vfp, \n");
        sql.append("    parcela_preco pp, \n");
        sql.append("    produto_especifico pe \n");
        sql.append("  WHERE 1                               =1 \n");
        sql.append("  AND tp.id_tabela_preco                = tpp.id_tabela_preco \n");
        sql.append("  AND pp.id_parcela_preco               = tpp.id_parcela_preco \n");
        sql.append("  AND tp.tp_servico = :tipoServico \n");
        sql.append("  AND TP.BL_EFETIVADA            = 'S' \n");
        sql.append("  AND fp.ID_TABELA_PRECO_PARCELA        = tpp.ID_TABELA_PRECO_PARCELA \n");
        sql.append("  AND pe.id_produto_especifico          = fp.id_produto_especifico \n");
        sql.append("  AND vfp.ID_FAIXA_PROGRESSIVA          = fp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("  AND ttp.ID_TIPO_TABELA_PRECO          = tp.ID_TIPO_TABELA_PRECO \n");
        sql.append("  and ttp.ID_EMPRESA_CADASTRADA = :idEmpresa \n");
        sql.append("  AND stp.ID_SUBTIPO_TABELA_PRECO       = tp.ID_SUBTIPO_TABELA_PRECO \n");
        sql.append("  AND fp.ID_PRODUTO_ESPECIFICO         IS NOT NULL \n");
        sql.append("  AND ttp.TP_TIPO_TABELA_PRECO          = 'C' \n");
        sql.append("  AND stp.TP_SUBTIPO_TABELA_PRECO       = 'L' \n");
        
        return sql.toString();
	}
	
	
    
	
	private ConfigureSqlQuery getConfigureSqlQueryRelatorioAlteracoesTarifas() {
		
		return new ConfigureSqlQuery() {
		    
			@Override
			public void configQuery(SQLQuery sqlQuery) {
			    sqlQuery.addScalar("ciaAerea");
				sqlQuery.addScalar("origem", Hibernate.STRING);
				sqlQuery.addScalar("destino", Hibernate.STRING);
				sqlQuery.addScalar("parcela", Hibernate.STRING);
				sqlQuery.addScalar("anterior" );
				sqlQuery.addScalar("alterado");
				sqlQuery.addScalar("indicador", Hibernate.STRING);
				sqlQuery.addScalar("reajuste", Hibernate.STRING);
				sqlQuery.addScalar("dataAlteracao");
			}
		};
	}

	public TabelaPreco findByIdTabelaPrecoReajuste(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tp ");
		query.append("FROM TabelaPreco tp  ");
		query.append("WHERE tp.idTabelaPreco = :idTabelaPreco ");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabelaPreco", idTabelaPreco);

		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(query.toString(), parametros);
	}


}

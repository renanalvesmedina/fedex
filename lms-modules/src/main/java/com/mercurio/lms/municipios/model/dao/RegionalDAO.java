package com.mercurio.lms.municipios.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegionalDAO extends BaseCrudDao<Regional, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Regional.class;
	}

	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("usuario",FetchMode.JOIN);
	}
	
	@Override
	protected void initFindListLazyProperties(Map fetchModes) {
		fetchModes.put("usuario",FetchMode.JOIN);
	}

	public List findRegionaisVigentesByEmpresa(Long idEmpresa) {
		if (idEmpresa == null) {
			throw new IllegalArgumentException("idEmpresa cannot be null");
		}
		final String hql = 
			"select new Map(r.idRegional as idRegional, " +
							"r.dsRegional as dsRegional, " +
							"r.sgRegional as sgRegional, " +
							"r.dtVigenciaInicial as dtVigenciaInicial, " +
							"r.dtVigenciaFinal as dtVigenciaFinal, " +
							"us.idUsuario as usuario_idUsuario, " +
							"us.nmUsuario as usuario_nmUsuario) " +
			"from " +Regional.class.getName()+" as r " +
				"join r.usuario as us " +
			"where " +
				"r.dtVigenciaFinal >= ? " +
				" AND r.idRegional in ( select rf.regional.idRegional " +
										"from "+RegionalFilial.class.getName()+" as rf " +
										"where rf.filial.empresa.idEmpresa = ?) " +
			"order by r.sgRegional, r.dsRegional";

		Object[] retricao = {JTDateTimeUtils.getDataAtual(), idEmpresa};

		return getAdsmHibernateTemplate().find(hql.toString(), retricao);
	}
	
	public List findRegionaisVigentes() {
		StringBuilder hql = new StringBuilder()
		.append("select new Map(r.idRegional as idRegional, ")
		.append("r.dsRegional as dsRegional, ")
		.append("r.sgRegional as sgRegional, ")
		.append("r.dtVigenciaInicial as dtVigenciaInicial, ")
		.append("r.dtVigenciaFinal as dtVigenciaFinal, ")
		.append("us.idUsuario as usuario_idUsuario, ")
		.append("us.nmUsuario as usuario_nmUsuario) ")
		.append("from ").append(Regional.class.getName()).append(" as r ")
		.append("join r.usuario as us ")
		.append("where ")
		.append("r.dtVigenciaFinal >= ? ")
		.append("order by r.sgRegional,r.dsRegional");

		return getAdsmHibernateTemplate().find(hql.toString(), JTDateTimeUtils.getDataAtual() );
	} 
	
	public List findRegionais() {
		StringBuilder hql = new StringBuilder()
		.append("select new Map(r.idRegional as idRegional, ")
		.append("r.dsRegional as dsRegional, ")
		.append("r.sgRegional as sgRegional, ")
		.append("r.dtVigenciaInicial as dtVigenciaInicial, ")
		.append("r.dtVigenciaFinal as dtVigenciaFinal, ")
		.append("us.idUsuario as usuario_idUsuario, ")
		.append("us.nmUsuario as usuario_nmUsuario) ")
		.append("from ").append(Regional.class.getName()).append(" as r ")
		.append("join r.usuario as us ")
		.append("order by r.sgRegional,r.dsRegional");

		return getAdsmHibernateTemplate().find(hql.toString());
	}

	/**
	 * Verifica se já existe filial vigente com a mesma sigla.
	 * Utilliza comparação de sigla com ignoreCase (ilike).
	 * @param regional
	 * @return True em caso afirmativo
	 * @author luisfco
	 */
	public boolean existeFilialVigenteDeMesmaSigla(Regional regional) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());

		if (regional.getIdRegional() != null)
			dc.add(Restrictions.ne("idRegional", regional.getIdRegional()));
		dc = JTVigenciaUtils.getDetachedVigencia(dc, regional.getDtVigenciaInicial(), regional.getDtVigenciaFinal());
		dc.add(Restrictions.ilike("sgRegional", regional.getSgRegional()));
		return findByDetachedCriteria(dc).size() > 0;		
	}

	public List findRegionalByIdRegional(Long idRegional, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRegional",idRegional));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List findRegionaisByUsuarioEmpresa(Usuario usuario, Empresa empresa) {
		final String hql =  " select new Map(r.idRegional as idRegional, "+
							"	r.dsRegional as dsRegional, " +
							"	r.sgRegional as sgRegional, " +
							"	r.dtVigenciaInicial as dtVigenciaInicial, " +
							"	r.dtVigenciaFinal as dtVigenciaFinal, " +
							"	us.idUsuario as usuario_idUsuario, " +
							"	us.nmUsuario as usuario_nmUsuario) " +
							" from " +Regional.class.getName() + " as r " +
							"	join r.usuario as us " +
							"   join r.usuariosRegional ureg "+
							"	join ureg.empresaUsuario empu " +
							"	join empu.usuario u " +
							" where u.id = ? " +
							" AND r.idRegional in ( select r2.idRegional from" +
							" 						"+RegionalFilial.class.getName()+" as rf " +
													" join rf.regional r2 " +
													" join rf.filial f2 "+
													" join f2.empresa e2 " +
													" where e2.id = ?) " +		
							" order by r.sgRegional,r.dsRegional";

		Object[] parametros = { usuario.getIdUsuario(), empresa.getIdEmpresa() };
		return getAdsmHibernateTemplate().find(hql.toString(), parametros);
	}

	public List findRegionaisByUsuario(Usuario usuario) {
		StringBuilder hql = new StringBuilder()
		.append("select new Map(r.idRegional as idRegional, ")
		.append("r.dsRegional as dsRegional, ")
		.append("r.sgRegional as sgRegional, ")
		.append("r.dtVigenciaInicial as dtVigenciaInicial, ")
		.append("r.dtVigenciaFinal as dtVigenciaFinal, ")
		.append("us.idUsuario as usuario_idUsuario, ")
		.append("us.nmUsuario as usuario_nmUsuario) ")
		.append("from "+Regional.class.getName()+" as r ")
		.append(" join r.usuariosRegional ur")
		.append(" join ur.empresaUsuario eu")
		.append(" join r.usuario as us ")		
		.append(" where eu.usuario.id = ? ")
		.append(" order by r.sgRegional, r.dsRegional");

		return getAdsmHibernateTemplate().find(hql.toString(), usuario.getIdUsuario());
	}

	@SuppressWarnings("unchecked")
	public List<Regional> findRegionaisVigentesByIdUsuario(Long idUsuario) {
		StringBuilder hql = new StringBuilder()
		.append("select r ")
		.append("from "+Regional.class.getName()+" as r ")
		.append(" join r.usuariosRegional ur")
		.append(" join ur.empresaUsuario eu")
		.append(" join r.usuario as us ")		
		.append(" where eu.usuario.id = :idUsuario ")
		.append(" and r.dtVigenciaInicial <= :dtVigenciaInicial ")
		.append(" and r.dtVigenciaFinal >= :dtVigenciaFinal ")
		.append(" order by r.sgRegional, r.dsRegional");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idUsuario", idUsuario);
		criteria.put("dtVigenciaInicial", JTDateTimeUtils.getDataAtual());
		criteria.put("dtVigenciaFinal", JTDateTimeUtils.getDataAtual());
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
	}
	
	public Regional findRegionalPadraoByUsuario(Usuario usuario) {
		StringBuilder sb = new StringBuilder()
		.append(" select r")
		.append(" from " + Regional.class.getName() + " as r")
		.append(" where r.regionalUsuariosPadrao.idUsuario = ?");

		return (Regional) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[] { usuario.getIdUsuario() });
	}

	public List findByUsuarioLogado(TypedFlatMap m) {
		Usuario u = SessionUtils.getUsuarioLogado();

		StringBuilder sb = new StringBuilder()
		.append("select")
		.append(" r.idRegional")
		.append(", r.dsRegional")
		.append(" from ")
		.append(Regional.class.getName() + " as r")
		.append(" where")
		.append(" r.usuariosRegional.usuario.idUsuario = ?");
		
		List l = getAdsmHibernateTemplate().find(sb.toString(), u.getIdUsuario());
		
		List r = new ArrayList();
		for (Iterator it = l.iterator(); it.hasNext();) {
			Object[] o = (Object[]) it.next();

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idRegional", o[0]);
			tfm.put("dsRegional", o[1]);
			
			r.add(tfm);
		}
		return r;
	}

	/**
	 * Andresa Vargas
	 * 
	 * Consulta padrao da tela
	 * 
	 * @param sgRegional
	 * @param dsRegional
	 * @param idUsuario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(String sgRegional, String dsRegional, Long idUsuario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, FindDefinition findDef){
		SqlTemplate sql = getSqlTemplate(sgRegional, dsRegional, idUsuario, dtVigenciaInicial, dtVigenciaFinal);
		ResultSetPage findPaginated = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		return findPaginated;
	}

	/**
	 * Andresa Vargas
	 * 
	 * Retorno do numero de registros da tela
	 * 
	 * @param sgRegional
	 * @param dsRegional
	 * @param idUsuario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public Integer getRowCountCustom(String sgRegional, String dsRegional, Long idUsuario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		SqlTemplate sql = getSqlTemplate(sgRegional, dsRegional, idUsuario, dtVigenciaInicial, dtVigenciaFinal);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	/**
	 * SQLTemplate da consulta de regional
	 * @param sgRegional
	 * @param dsRegional
	 * @param idUsuario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	private SqlTemplate getSqlTemplate(String sgRegional, String dsRegional, Long idUsuario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		StringBuilder hqlProjection = new StringBuilder();
		hqlProjection.append("new Map(");
		hqlProjection.append("  reg.sgRegional as sgRegional,");
		hqlProjection.append("  reg.dsRegional as dsRegional,");
		hqlProjection.append("  reg.idRegional as idRegional,");
		hqlProjection.append("  usu.nmUsuario as nmUsuario,");
		hqlProjection.append("  reg.dtVigenciaInicial as dtVigenciaInicial,");
		hqlProjection.append("  reg.dtVigenciaFinal as dtVigenciaFinal");
		hqlProjection.append(") ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(hqlProjection.toString());

		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(Regional.class.getName()).append(" as reg ")	.append("inner join reg.usuario as usu ");

		sql.addFrom(hqlFrom.toString());

		sql.addCriteria("upper(reg.sgRegional)", "like", sgRegional);
		sql.addCriteria("lower(reg.dsRegional)", "like", dsRegional.toLowerCase());
		sql.addCriteria("usu.idUsuario", "=", idUsuario);
		sql.addCriteria("reg.dtVigenciaInicial", ">=", dtVigenciaInicial, YearMonthDay.class);
		sql.addCriteria("reg.dtVigenciaFinal", "<=", dtVigenciaFinal, YearMonthDay.class);

		sql.addOrderBy("reg.sgRegional");
		sql.addOrderBy("reg.dtVigenciaInicial");

		return sql; 
	}

	public List findRegionaisByFiliais(List filiais) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct rf.regional from ").append(RegionalFilial.class.getName()).append(" rf")
		.append(" where rf.filial in (:filiais) "+
				" and rf.dtVigenciaInicial <= :dtVigenciaInicial" +
				" and rf.dtVigenciaFinal >= :dtVigenciaFinal");
		
		Map params = new HashMap();
		params.put("filiais", filiais);
		params.put("dtVigenciaInicial", JTDateTimeUtils.getDataAtual());
		params.put("dtVigenciaFinal", JTDateTimeUtils.getDataAtual());
		
		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
	}
	
	/**
	 * Método que busca as regionais que tenham iniciado sua vigência inicial 
	 * na dataInicial data e que sua vigencia final vá até a dataFinal informada
	 * ou que esteje vigente. 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return list
	 */
	public List findRegionaisVigentesPorPeriodo(YearMonthDay dataInicial, YearMonthDay dataFinal) {
		DetachedCriteria dc = createDetachedCriteria();

		Criterion restricaoInicial = Restrictions.and( 
				Restrictions.ge("dtVigenciaFinal", dataInicial), 
				Restrictions.le("dtVigenciaInicial", dataInicial)
		);

		Criterion restricaoFinal = Restrictions.and( 
				Restrictions.ge("dtVigenciaFinal", dataFinal), 
				Restrictions.le("dtVigenciaInicial", dataFinal)
		);

		dc.add(Restrictions.or(restricaoInicial, restricaoFinal));

		dc.addOrder( Order.asc("sgRegional") );
		dc.addOrder( Order.asc("dsRegional") );

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<Regional> findRegionaisValidas() {
		final StringBuilder sql = new StringBuilder();
		sql.append(" SELECT r.id_regional idRegional");
		sql.append(" FROM   regional r");
		sql.append(" WHERE  EXISTS ( SELECT 1"); 
		sql.append("                FROM   regional_filial  rf");
		sql.append("                JOIN   historico_filial hf ON rf.id_filial = hf.id_filial");
		sql.append("                WHERE  rf.id_regional = r.id_regional");
		sql.append("                AND    SYSDATE BETWEEN rf.dt_vigencia_inicial AND rf.dt_vigencia_final");
		sql.append("                AND    SYSDATE BETWEEN hf.dt_real_operacao_inicial AND hf.dt_real_operacao_final");
		sql.append("                AND    ROWNUM = 1 )");
		sql.append(" AND    SYSDATE BETWEEN r.dt_vigencia_inicial AND r.dt_vigencia_final");
		sql.append(" ORDER BY r.sg_regional");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idRegional",Hibernate.LONG); 
			}
		};
		HibernateCallback hcb = findBySql( sql.toString(), null,csq);

		List list =(List)getAdsmHibernateTemplate().execute(hcb); 
		
		if (!list.isEmpty()){
			List<Regional> regionais=new ArrayList<Regional>();
			
			for(Object idRegional:list){
				regionais.add(findById((Long)idRegional));
			}
			
			return regionais;
		} else {
			return null;
		}
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("select r.id_regional as idRegional, r.sg_regional as sgRegional, r.ds_regional as dsRegional ");
		sql.append("  from regional r");
		sql.append(" where sysdate between r.dt_vigencia_inicial and r.dt_vigencia_final");
		
		if (filter.get("sgRegional") != null) {
			sql.append(" and LOWER(r.sg_regional) = LOWER(:sgRegional)");
		}

		return new ResponseSuggest(sql.toString(), filter);
	}

	public static HibernateCallback findBySql(final String sql,final Object[] parametersValues,final ConfigureSqlQuery configQuery) {
	
		final HibernateCallback hcb = new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				
				configQuery.configQuery(query);
			
				if (parametersValues != null) {
					for (int i = 0; i < parametersValues.length; i++) {
						if( parametersValues[i] instanceof YearMonthDay){
							query.setParameter(i, parametersValues[i], Hibernate.custom(JodaTimeYearMonthDayUserType.class));
						}else{
							query.setParameter(i, parametersValues[i]);
						}
					}
				}
				return query.list();
			}
		};
		return hcb;
	}

	public List<Regional> findChosen() {
		Criteria criteria = createCriteria();
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		criteria.add(Restrictions.le("dtVigenciaInicial", dataAtual));
		criteria.add(Restrictions.ge("dtVigenciaFinal", dataAtual));
		criteria.addOrder(Order.asc("dsRegional"));
		
		return criteria.list();
	}

	public Regional findRegionalAtivaByIdFilial(Long idFilial) {
		Criteria criteria = createCriteria(RegionalFilial.class);
		criteria.createAlias("regional", "regional");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		criteria.add(Restrictions.le("dtVigenciaInicial", dataAtual));
		criteria.add(Restrictions.ge("dtVigenciaFinal", dataAtual));
		criteria.add(Restrictions.eq("filial.idFilial", idFilial));
		
		Object result = criteria.uniqueResult();
		if (result == null) {
			return null;
		}
		return ((RegionalFilial) result).getRegional();
	}

	public Map<String, String> findContatoRegionalByIdRegionalIdMonitoramentoMensagem(Long idRegional, Long idMonitMens) {
		final StringBuilder sql = new StringBuilder();
		sql.append(" select decode(hf.tp_filial, 'FR', fi.nm_contato, us.nm_usuario) nm_usuario ");
		sql.append(" 	,decode(hf.tp_filial, 'FR', fi.sg_filial, r.sg_regional) sg_regional ");
		sql.append(" 	,decode(hf.tp_filial, 'FR', fi.ds_email, r.ds_email_faturamento) ds_email_faturamento ");
		sql.append(" 	,decode(hf.tp_filial, 'FR', fi.nr_ddd, r.nr_ddd) nr_ddd ");
		sql.append(" 	,decode(hf.tp_filial, 'FR', fi.nr_telefone, r.nr_telefone) nr_telefone ");
		sql.append(" from regional r ");
		sql.append(" 	,usuario_adsm us ");
		sql.append(" 	,monit_mens_fatura mmf ");
		sql.append(" 	,fatura fa ");
		sql.append(" 	,( ");
		sql.append(" 		select fi.id_filial ");
		sql.append(" 			,fi.sg_filial ");
		sql.append(" 			,pe.nm_fantasia ");
		sql.append(" 			,nvl(cofa.nm_contato, cocb.nm_contato) nm_contato ");
		sql.append(" 			,nvl(cofa.ds_email, cocb.ds_email) ds_email ");
		sql.append(" 			,te.nr_ddd ");
		sql.append(" 			,te.nr_telefone ");
		sql.append(" 		from filial fi ");
		sql.append(" 			,pessoa pe ");
		sql.append(" 			,( ");
		sql.append(" 				select * ");
		sql.append(" 				from telefone_endereco te ");
		sql.append(" 				where tp_uso = 'FO' ");
		sql.append(" 				) te ");
		sql.append(" 			,( ");
		sql.append(" 				select * ");
		sql.append(" 				from contato co ");
		sql.append(" 				where co.tp_contato = 'FA' ");
		sql.append(" 				) cofa ");
		sql.append(" 			,( ");
		sql.append(" 				select * ");
		sql.append(" 				from contato co ");
		sql.append(" 				where co.tp_contato = 'CB' ");
		sql.append(" 				) cocb ");
		sql.append(" 		where fi.id_filial = pe.id_pessoa ");
		sql.append(" 			and pe.id_endereco_pessoa = te.id_endereco_pessoa(+) ");
		sql.append(" 			and fi.id_filial = cofa.id_pessoa(+) ");
		sql.append(" 			and fi.id_filial = cocb.id_pessoa(+) ");
		sql.append(" 		) fi ");
		sql.append(" 	,( ");
		sql.append(" 		select * ");
		sql.append(" 		from historico_filial hf ");
		sql.append(" 		where trunc(SYSDATE) between hf.dt_real_operacao_inicial ");
		sql.append(" 				and hf.dt_real_operacao_final ");
		sql.append(" 		) HF ");
		sql.append(" where r.id_regional = ? ");
		sql.append(" 	and r.id_usuario_faturamento = us.id_usuario ");
		sql.append(" 	and mmf.id_monitoramento_mensagem = ? ");
		sql.append(" 	and mmf.id_fatura = fa.id_fatura ");
		sql.append(" 	and fa.id_filial = fi.id_filial(+) ");
		sql.append(" 	and fi.id_filial = hf.id_filial(+) ");
		sql.append(" 	and rownum = 1 ");

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("NM_USUARIO", Hibernate.STRING);
				sqlQuery.addScalar("SG_REGIONAL", Hibernate.STRING);
				sqlQuery.addScalar("DS_EMAIL_FATURAMENTO", Hibernate.STRING);
				sqlQuery.addScalar("NR_DDD", Hibernate.STRING);
				sqlQuery.addScalar("NR_TELEFONE", Hibernate.STRING);

}
		};
		Long[] param = { idRegional, idMonitMens };
		HibernateCallback hcb = findBySql(sql.toString(), param, csq);
		List<Object[]> result = (List<Object[]>) getAdsmHibernateTemplate().execute(hcb);
		if (!result.isEmpty()) {
			Object[] mapResult = (Object[]) result.get(0);
			Map<String, String> contato = new HashMap<String, String>();
			contato.put("NM_USUARIO", (String) mapResult[0]);
			contato.put("SG_REGIONAL", (String) mapResult[1]);
			contato.put("DS_EMAIL_FATURAMENTO", (String) mapResult[2]);
			contato.put("NR_DDD", (String) mapResult[3]);
			contato.put("NR_TELEFONE", (String) mapResult[4]);

			return contato;

		}
		return null;
		
	}
}

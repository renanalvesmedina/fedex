package com.mercurio.lms.municipios.model.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean 
 */
public class MunicipioFilialDAO extends BaseCrudDao<MunicipioFilial, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return MunicipioFilial.class;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("filial.empresa", FetchMode.JOIN);
		fetchModes.put("filial.empresa.pessoa", FetchMode.JOIN);		
		fetchModes.put("municipio", FetchMode.JOIN);
		fetchModes.put("municipio.municipioDistrito", FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais", FetchMode.JOIN);		
	}

	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
		fetchModes.put("municipio.municipioDistrito", FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais.zona", FetchMode.JOIN);
	}

	public List findByIdCustomizado(Long idMunicipioFilial){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" + "pesEmp.nmPessoa as nomeEmpresa, " + "fil.sgFilial as sgFilial, " + "pesFil.nmFantasia as nmFantasia, " + "mun.nmMunicipio as nmMunicipio, "
			+ "uf.sgUnidadeFederativa as sgUnidadeFederativa, " + "uf.nmUnidadeFederativa as nmUnidadeFederativa, " + "pais.nmPais as nmPais, " + "mun.blDistrito as blDistrito, "
			+ "munDist.nmMunicipio as nmMunicipioDist, " + "mf.nrDistanciaChao as nrDistanciaChao, " + "mf.nrDistanciaAsfalto as nrDistanciaAsfalto, " + "mf.nrGrauDificuldade as nrGrauDificuldade, "
			+ "mf.blRecebeColetaEventual as blRecebeColetaEventual, " + "mf.blDificuldadeEntrega as blDificuldadeEntrega, " + "mf.blPadraoMcd as blPadraoMcd, "
			+ "mf.blRestricaoAtendimento as blRestricaoAtendimento, " + "mf.blRestricaoTransporte as blRestricaoTransporte, " + "mf.blRecebeColetaEventual as blRecebeColetaEventual, "
			+ "mf.blRecebeColetaEventual as blRecebeColetaEventual, " + "mf.idMunicipioFilial as idMunicipioFilial, " + "mf.nmMunicipioAlternativo as nmMunicipioAlternativo, "
			+ "mf.blRecebeColetaEventual as blRecebeColetaEventual)");

		hql.addFrom("MunicipioFilial mf " + "join mf.municipio mun " + "join mun.unidadeFederativa uf " + "join uf.pais pais " + "join mf.filial fil " + "join fil.pessoa pesFil "
			+ "left outer join mun.municipioDistrito munDist " + "join fil.empresa emp " + "join emp.pessoa pesEmp ");
		
		hql.addCriteria("mf.idMunicipioFilial", "=", idMunicipioFilial);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = createHQLPaginated(criteria); 
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = createHQLPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	public List findLookupAsPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = createHQLPaginated(criteria); 
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.getSql(), Integer.valueOf(1), Integer.valueOf(2), hql.getCriteria());
		return rsp.getList();
	}

	private SqlTemplate createHQLPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(mf.id","idMunicipioFilial");
		sql.addProjection("f.sgFilial || ' - ' || pf.nmFantasia","filial_siglaNomeFilial");
		sql.addProjection("f.sgFilial","filial_sgFilial");
		sql.addProjection("f.idFilial","filial_idFilial");
		sql.addProjection("pf.nmFantasia","filial_pessoa_nmFantasia");
		sql.addProjection("pf.nmFantasia","filial_pessoa_nmFantasia");
		sql.addProjection("e.id","filial_empresa_idEmpresa");
		sql.addProjection("pe.nmPessoa","filial_empresa_pessoa_nmPessoa");
		sql.addProjection("pe.nrIdentificacao","filial_empresa_pessoa_nrIdentificacao");
		sql.addProjection("pe.tpIdentificacao","filial_empresa_pessoa_tpIdentificacao");

		sql.addProjection("uff.idUnidadeFederativa","filial_pessoa_enderecoPessoa_municipio_unidadeFederativa_idUnidadeFederativa");

		sql.addProjection("m.nmMunicipio","municipio_nmMunicipio");
		sql.addProjection("m.idMunicipio","municipio_idMunicipio");
		sql.addProjection("uf.idUnidadeFederativa","municipio_unidadeFederativa_idUnidadeFederativa");
		sql.addProjection("uf.sgUnidadeFederativa","municipio_unidadeFederativa_sgUnidadeFederativa");
		sql.addProjection("uf.nmUnidadeFederativa","municipio_unidadeFederativa_nmUnidadeFederativa");
		sql.addProjection("p.idPais","municipio_unidadeFederativa_pais_idPais");
		sql.addProjection("p.nmPais","municipio_unidadeFederativa_pais_nmPais");
		sql.addProjection("z.idZona","municipio_unidadeFederativa_pais_zona_idZona");
		sql.addProjection("z.dsZona","municipio_unidadeFederativa_pais_zona_dsZona");
		sql.addProjection("m.blDistrito","municipio_blDistrito");
		sql.addProjection("md.nmMunicipio","municipio_municipioDistrito_nmMunicipio");
		sql.addProjection("mf.nrDistanciaChao","nrDistanciaChao");
		sql.addProjection("mf.nrDistanciaAsfalto","nrDistanciaAsfalto");
		sql.addProjection("mf.blRecebeColetaEventual","blRecebeColetaEventual");
		sql.addProjection("mf.blDificuldadeEntrega","blDificuldadeEntrega");
		sql.addProjection("mf.blPadraoMcd","blPadraoMcd");
		sql.addProjection("mf.blRestricaoAtendimento","blRestricaoAtendimento");
		sql.addProjection("mf.blRestricaoTransporte","blRestricaoTransporte");
		sql.addProjection("mf.dtVigenciaInicial","dtVigenciaInicial");
		sql.addProjection("mf.dtVigenciaFinal","dtVigenciaFinal)");

		StringBuilder from = new StringBuilder().append(MunicipioFilial.class.getName()).append(" mf").append("	join mf.filial as f ").append("	join f.empresa as e ").append("	join e.pessoa as pe")
			.append("	join f.pessoa pf ").append("	left join pf.enderecoPessoa epf ").append("	left join epf.municipio mf2 ").append("	left join mf2.unidadeFederativa uff ")
			.append("	join mf.municipio as m ").append("	left join m.municipioDistrito as md ").append("	join m.unidadeFederativa as uf ").append("	join uf.pais as p ")
			.append("	left join p.zona as z ");
		sql.addFrom(from.toString());

		/** Ids */
		sql.addCriteria("e.id ", "=", criteria.getLong("filial.empresa.idEmpresa"));
		sql.addCriteria("f.id ", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("m.id ", "=", criteria.getLong("municipio.idMunicipio"));
		sql.addCriteria("p.id", "=", criteria.getLong("municipio.unidadeFederativa.pais.idPais"));
		sql.addCriteria("uf.id", "=", criteria.getLong("municipio.unidadeFederativa.idUnidadeFederativa"));
		sql.addCriteria("md.id", "=", criteria.getLong("municipio.municipioDistrito.idMunicipio"));
		String nmMunicipio = criteria.getString("municipio.nmMunicipio");
		if (StringUtils.isNotBlank(nmMunicipio)) {
			sql.addCriteria("lower(m.nmMunicipio)", "like", nmMunicipio.toLowerCase().concat("%"));
		}

		/** Booleans */
		Boolean blDistrito = criteria.getBoolean("municipio.blDistrito");
		if (blDistrito != null){
			sql.addCriteria("m.blDistrito", "=", blDistrito);
		}
		Boolean blRecebeColetaEventual = criteria.getBoolean("blRecebeColetaEventual");
		if (blRecebeColetaEventual != null){ 
			sql.addCriteria("mf.blRecebeColetaEventual", "=", blRecebeColetaEventual);
		}
		Boolean blPadraoMcd = criteria.getBoolean("blPadraoMcd");
		if (blPadraoMcd != null){ 
			sql.addCriteria("mf.blPadraoMcd", "=", blPadraoMcd);
		}
		Boolean blRestricaoAtendimento = criteria.getBoolean("blRestricaoAtendimento");
		if (blRestricaoAtendimento != null){
			sql.addCriteria("mf.blRestricaoAtendimento", "=", blRestricaoAtendimento);
		}
		Boolean blRestricaoTransporte = criteria.getBoolean("blRestricaoTransporte");
		if (blRestricaoTransporte != null){
			sql.addCriteria("mf.blRestricaoTransporte", "=", blRestricaoTransporte);
		}
		Boolean blDificuldadeEntrega = criteria.getBoolean("blDificuldadeEntrega");
		if (blDificuldadeEntrega != null){ 
			sql.addCriteria("mf.blDificuldadeEntrega", "=", blDificuldadeEntrega);
		}

		/** Vigencias */
		sql.addCriteria(" mf.dtVigenciaInicial ", ">=", criteria.getYearMonthDay("dtVigenciaInicial"));
		sql.addCriteria(" mf.dtVigenciaFinal ", "<=", criteria.getYearMonthDay("dtVigenciaFinal"));
		String vigentes = criteria.getString("vigentes");
		if (StringUtils.isNotBlank(vigentes)) {
			if (vigentes.equals("S")) {
				sql.addCriteria(" mf.dtVigenciaInicial ", "<=", JTDateTimeUtils.getDataAtual());
				sql.addCustomCriteria(" (mf.dtVigenciaFinal is null or mf.dtVigenciaFinal >= ? ) ", JTDateTimeUtils.getDataAtual());
			} else if (vigentes.equals("N")) {
				sql.addCustomCriteria(" ((mf.dtVigenciaInicial < ? and mf.dtVigenciaFinal < ? ) or (mf.dtVigenciaInicial > ? )) ", JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
				sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			}
		}

		sql.addOrderBy("f.sgFilial");
		sql.addOrderBy("m.nmMunicipio");
		sql.addOrderBy("mf.dtVigenciaInicial");
		return sql;
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();

		sql.append("select m.nm_municipio nmMunicipio, uf.sg_unidade_federativa sgUnidadeFederativa, f.sg_filial sgFilial, f.id_empresa idEmpresa, mf.id_municipio_filial idMunicipioFilial, m.id_municipio idMunicipio, f.id_filial idFilial, p.nm_fantasia nmFantasia ");
		sql.append("  from municipio_filial mf, municipio m, filial f, unidade_federativa uf, pessoa p ");
		sql.append("  where mf.id_municipio = m.id_municipio ");
		sql.append("  and mf.id_filial = f.id_filial ");
		sql.append("  and mf.dt_vigencia_final >= SYSDATE ");
		sql.append("  and mf.dt_vigencia_inicial <= SYSDATE ");
		sql.append("  and m.id_unidade_federativa = uf.id_unidade_federativa ");
		sql.append("  and m.tp_situacao = 'A' ");
		sql.append("  and p.id_pessoa = f.id_filial ");
		if (filter.get("nmMunicipio") != null) {
			sql.append(" and LOWER(m.nm_municipio) like LOWER(:nmMunicipio)");
			filter.put("nmMunicipio", filter.get("nmMunicipio") + "%");
		}
		if (filter.get("idEmpresa") != null) {
			sql.append(" and f.id_empresa = :idEmpresa");
			filter.put("idEmpresa", filter.get("idEmpresa"));
		}

		return new ResponseSuggest(sql.toString(), filter);
	}

	public List findMunicipioFilialByFilial(Long idFilial,YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("filial.idFilial",idFilial));
		dc.add(Restrictions.le("dtVigenciaInicial",new YearMonthDay()));
		dc.add(Restrictions.or(Restrictions.isNull("dtVigenciaFinal"),Restrictions.ge("dtVigenciaFinal",dtVigenciaFinal)));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}	

	/**
	 * verifica MCD vigente para um mesmo municipio
	 */
	public boolean verificaMCDVigente(MunicipioFilial mf){
		//cria a query com a classe de persistencia
		DetachedCriteria dc = createDetachedCriteria();
		if(mf.getIdMunicipioFilial() != null){
			dc.add(Restrictions.ne("idMunicipioFilial",mf.getIdMunicipioFilial()));
		}

		dc = JTVigenciaUtils.getDetachedVigencia(dc, mf.getDtVigenciaInicial(), mf.getDtVigenciaFinal());	

		dc.add(Restrictions.eq("municipio.idMunicipio", mf.getMunicipio().getIdMunicipio()));
		dc.add(Restrictions.eq("blPadraoMcd", Boolean.TRUE));
		return findByDetachedCriteria(dc).size()>0;
	}

	public boolean isMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("idMunicipioFilial",idMunicipioFilial));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	/**
	 * Retorna uma lista de filiais vigentes na data atual ou com vigencia
	 * futura para o municipio informado Utilizado na tela Atualizar troca de
	 * filiais
	 * 
	 * @param idMunicipio
	 * @return 
	 */
	public List findMunicipioFilialVigenteByMunicipio(Long idMunicipio){
		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class, "mf_");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		ProjectionList projections = Projections.projectionList().add(Projections.property("mf_.idMunicipioFilial"), "idMunicipioFilial")
			.add(Projections.property("mf_.blRestricaoAtendimento"), "blRestricaoAtendimento").add(Projections.property("mf_.blRestricaoTransporte"), "blRestricaoTransporte")
			.add(Projections.property("mf_.dtVigenciaInicial"), "dtVigenciaInicial").add(Projections.property("mf_.dtVigenciaFinal"), "dtVigenciaFinal")
			.add(Projections.property("fil.sgFilial"), "sgFilial").add(Projections.property("fil_pes.nmFantasia"), "nmFantasia");

		dc.setProjection(projections);
		dc.createAlias("filial", "fil");
		dc.createAlias("fil.pessoa", "fil_pes");

		dc.add(Restrictions.or(Restrictions.and(Restrictions.le("mf_.dtVigenciaInicial", today), Restrictions.ge("mf_.dtVigenciaFinal", today)), Restrictions.gt("mf_.dtVigenciaInicial", today)));

		dc.add(Restrictions.eq("mf_.municipio.idMunicipio", idMunicipio));

		dc.addOrder(Order.asc("fil.sgFilial"));		
		dc.addOrder(Order.asc("mf_.dtVigenciaInicial"));

		dc.setResultTransformer(new AliasToNestedMapResultTransformer());

		return findByDetachedCriteria(dc); 
	}
	
	public List findMunicipioFilialVigente(Long idMunicipio, Long idFilial){
		
		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class, "mf_");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		dc.createAlias("filial", "fil");

		dc.add(Restrictions.or(Restrictions.and(Restrictions.le("mf_.dtVigenciaInicial", today), Restrictions.ge("mf_.dtVigenciaFinal", today)), Restrictions.gt("mf_.dtVigenciaInicial", today)));

		dc.add(Restrictions.eq("mf_.municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.eq("fil.id", idFilial));

		dc.addOrder(Order.asc("fil.sgFilial"));		
		dc.addOrder(Order.asc("mf_.dtVigenciaInicial"));

		return getAdsmHibernateTemplate().findByCriteria(dc); 
	}	
	
	/**
	 * Retorna uma lista de municipioFiliais vigentes na data atual para o
	 * municipio informado Utilizado na tela Cadastrar Pedido Coleta (aba
	 * Detalhamento)
	 * 
	 * @param nmMunicipio
	 * @return 
	 */
	public List findMunicipioFilialVigenteByNmMunicipio(String nmMunicipio, boolean pesquisaExata){
		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class, "mf");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		dc.createAlias("mf.filial", "fil");
		dc.createAlias("fil.pessoa", "pes");
		dc.createAlias("mf.municipio", "mun");
		dc.createAlias("mun.unidadeFederativa", "uf");

		dc.add(Restrictions.and(Restrictions.le("mf.dtVigenciaInicial", today), Restrictions.ge("mf.dtVigenciaFinal", today)));
		if (pesquisaExata){
			dc.add(Restrictions.ilike("mun.nmMunicipio", nmMunicipio, MatchMode.EXACT));
		} else {
			dc.add(Restrictions.ilike("mun.nmMunicipio", nmMunicipio, MatchMode.START));
		}
		
		dc.addOrder(Order.asc("fil.sgFilial"));		
		dc.addOrder(Order.asc("mf.dtVigenciaInicial"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Retorna uma lista de municipioFiliais vigentes na data atual para o
	 * municipio informado Utilizado na tela Cadastrar Pedido Coleta (aba
	 * Detalhamento)
	 * 
	 * @param idMunicipio
	 * @return 
	 */
	public List findMunicipioFilialVigenteByIdMunicipio(Long idMunicipio){
		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class, "mf");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		dc.createAlias("mf.filial", "fil");
		dc.createAlias("fil.pessoa", "pes");
		dc.createAlias("mf.municipio", "mun");
		dc.createAlias("mun.unidadeFederativa", "uf");

		dc.add(Restrictions.and(Restrictions.le("mf.dtVigenciaInicial", today), Restrictions.ge("mf.dtVigenciaFinal", today)));
		dc.add(Restrictions.eq("mun.idMunicipio", idMunicipio));

		dc.addOrder(Order.asc("fil.sgFilial"));		
		dc.addOrder(Order.asc("mf.dtVigenciaInicial"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Retorna uma lista de Municípios vigentes na data atual ou com vigencia
	 * futura para a filial informada Utilizado na tela Atualizar troca de
	 * filiais
	 * 
	 * @param idFilial
	 * @return 
	 */
	public List findMunicipioFilialVigenteByFilial(Long idFilial){
		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		StringBuffer hql = new StringBuffer().append(" select new Map(").append("	MF.idMunicipioFilial as idMunicipioFilial, ").append("	MF.blRestricaoAtendimento as blRestricaoAtendimento,")
			.append("	MF.blRestricaoTransporte as blRestricaoTransporte,").append("	M.idMunicipio as idMunicipio, ").append("	M.nmMunicipio as nmMunicipio ) ")
			.append(" from " + getPersistentClass().getName() + " MF ").append(" inner join MF.filial as F ").append(" inner join MF.municipio as M ").append(" where MF.dtVigenciaInicial <= :today ")
			.append("	and MF.dtVigenciaFinal >= :today").append("	and F.idFilial = :idF").append(" order by M.nmMunicipio");

		List l = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), new String[] { "today", "idF" }, new Object[] { today, idFilial });

		for (Iterator i = l.iterator() ; i.hasNext() ;) {
			Map map = (Map)i.next();

			StringBuffer subHql = new StringBuffer().append(" select min(OP.blAtendimentoGeral) as blAtendimentoGeral ").append(" from " + OperacaoServicoLocaliza.class.getName() + " OP ")
				.append(" left join OP.municipioFilial as MF ").append(" where OP.blAtendimentoGeral != null ").append(" and MF.idMunicipioFilial = ?");
			
			Object objAtend = getAdsmHibernateTemplate().find(subHql.toString(),map.get("idMunicipioFilial")).get(0);
			if (objAtend != null)
				map.put("blAtendimentoGeral",(Boolean)objAtend);
			else
				map.put("blAtendimentoGeral",Boolean.TRUE);
		}

		return l;
	}

	/**
	 * Retorna o id do atendimento padrao MCD vigente para o municipio informado
	 * 
	 * @param idMunicipio
	 * @return o id do MunicipioXFilial encontrado ou null caso nao seja
	 *         encontrado
	 */
	public Object[] findMunicipioFilialVigenteByMunicipioPadraoMCD(Long idMunicipio){
		DetachedCriteria dc = createDetachedCriteria();

		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		ProjectionList pl = Projections.projectionList().add(Projections.property("idMunicipioFilial")).add(Projections.property("filial.idFilial"));

		dc.setProjection(pl);
		dc.add(Restrictions.eq("blPadraoMcd", Boolean.TRUE));
		dc.add(Restrictions.eq("municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.ge("dtVigenciaFinal",today));
		dc.add(Restrictions.le("dtVigenciaInicial",today));

		List result = findByDetachedCriteria(dc);

		if (!result.isEmpty())
			return ((Object[])result.get(0));
		else
			return null;
	}

	/**
	 * Verifica se existe algum atendimento numa vigencia futura para os
	 * paramentros inforamdos
	 * 
	 * @param idMunicipio
	 * @param idFilial
	 * @return TRUE se existe algum atendimento, FALSE caso contrario
	 */
	public boolean verificaExisteMunicipioFilialVigenciaFutura(Long idMunicipio, Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		dc.add(Restrictions.gt("dtVigenciaInicial", today));
		Restrictions.or(Restrictions.isNull("dtVigenciaFinal"), Restrictions.gt("dtVigenciaFinal", today));

		dc.add(Restrictions.eq("municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));

		dc.setProjection(Projections.rowCount());

		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return ((Integer)result.get(0)).intValue() > 0;
	}

	/**
	 * Verifica se nao existe outro atendimento para o mesmo municipioXfilial na
	 * mesma vigencia
	 * 
	 * @param municipioFilial
	 * @return
	 */
	public boolean verificaVigencia(MunicipioFilial municipioFilial){
		DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(MunicipioFilial.class, municipioFilial.getIdMunicipioFilial(), municipioFilial.getDtVigenciaInicial(),
			municipioFilial.getDtVigenciaFinal());

		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("municipio", municipioFilial.getMunicipio()));
		dc.add(Restrictions.eq("filial", municipioFilial.getFilial()));

		return ((Integer)(getAdsmHibernateTemplate().findByDetachedCriteria(dc).get(0))).intValue() > 0;
	}

	// verifica a BL_RESTRICAO_ATENDIMENTO para o município (tabela
	// MUNICIPIO_FILIAL) e se está vigente. Regra 3.2.
	public boolean findMunicipioFilialByBlRestricaoAtendimento(MunicipioFilial municipioFilial){
		DetachedCriteria dc = createDetachedCriteria();
		if(municipioFilial.getIdMunicipioFilial() != null){
			dc.add(Restrictions.ne("idMunicipioFilial",municipioFilial.getIdMunicipioFilial()));
		}
		dc.add(Restrictions.eq("municipio.idMunicipio",municipioFilial.getMunicipio().getIdMunicipio()));
		dc.add(Restrictions.eq("blRestricaoAtendimento", municipioFilial.getBlRestricaoAtendimento()));
		dc = JTVigenciaUtils.getDetachedVigencia(dc,municipioFilial.getDtVigenciaInicial(),municipioFilial.getDtVigenciaFinal());		
		return findByDetachedCriteria(dc).size()>0;
	}

	public boolean findMunicipioFilialByFilialMunicipioVigencias(MunicipioFilial municipioFilial){
		DetachedCriteria dc = createDetachedCriteria();
		if(municipioFilial.getIdMunicipioFilial()!= null){
			dc.add(Restrictions.ne("idMunicipioFilial",municipioFilial.getIdMunicipioFilial()));
		}
		dc.add(Restrictions.eq("filial.idFilial",municipioFilial.getFilial().getIdFilial()));
		dc.add(Restrictions.eq("municipio.idMunicipio",municipioFilial.getMunicipio().getIdMunicipio()));
		dc = JTVigenciaUtils.getDetachedVigencia(dc,municipioFilial.getDtVigenciaInicial(),municipioFilial.getDtVigenciaFinal());

		return findByDetachedCriteria(dc).size()>0;
	}

	// verifica se existe BL_RESTRICAO_TRANSPORTE = "S" para o municípioFilial
	// passado
	public boolean findMunicipioFilialByBlRestricaoTransporte(Long idMunicipioFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("idMunicipioFilial",idMunicipioFilial));
		dc.add(Restrictions.eq("blRestricaoTransporte", Boolean.TRUE));
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}
	
	// verifica se existe BL_RESTRICAO_ATENDIMENTO = "S" para o municípioFilial
	// passado
	public boolean findMunicipioFilialByBlRestricaoAtendimento(Long idMunicipioFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("idMunicipioFilial",idMunicipioFilial));
		dc.add(Restrictions.eq("blRestricaoAtendimento", Boolean.TRUE));
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	public List findAtendimentosVigentesByMunicipioServico(Long idMunicipioOrigem, Long idServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("mf.idMunicipioFilial");
		sql.addProjection("mf.filial.idFilial");
		sql.addProjection("mf.blRestricaoAtendimento");
		sql.addProjection("mf.blRestricaoTransporte");
		sql.addProjection("osl.nrTempoColeta");
		sql.addProjection("osl.idOperacaoServicoLocaliza");
		sql.addProjection("osl.nrTempoEntrega");

		sql.addFrom("MunicipioFilial mf left outer join mf.operacaoServicoLocalizas as osl");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		sql.addCustomCriteria("? between mf.dtVigenciaInicial and mf.dtVigenciaFinal", today);
		sql.addCustomCriteria("? between osl.dtVigenciaInicial and osl.dtVigenciaFinal", today);
		sql.addCriteria("mf.municipio.idMunicipio", "=", idMunicipioOrigem);
		sql.addCustomCriteria("(osl.servico.idServico is null or osl.servico.idServico = ?)", idServico);
		sql.addCustomCriteria("((osl.tpOperacao = 'C') or (osl.tpOperacao = 'A'))");
		sql.addCustomCriteria("mf.blPadraoMcd = 'S'");

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	/**
	 * Consulta atendimentos vigentes na data corrente a partir dos parametros
	 * informados
	 * 
	 * @param idMunicipioOrigem
	 * @param idServico
	 * @param tpOperacao
	 * @return
	 */
	public List findAtendimentosVigentesByMunicipioServicoOperacao(Long idMunicipioOrigem, Long idServico, String tpOperacao){

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("mf.idMunicipioFilial");
		sql.addProjection("mf.filial.idFilial");
		sql.addProjection("mf.blRestricaoAtendimento");
		sql.addProjection("mf.blRestricaoTransporte");
		sql.addProjection("nvl(osl.nrTempoColeta, 0)");
		sql.addProjection("osl.idOperacaoServicoLocaliza");
		sql.addProjection("nvl(osl.nrTempoEntrega, 0)");

		sql.addFrom("MunicipioFilial mf left outer join mf.operacaoServicoLocalizas as osl");

		YearMonthDay today = JTDateTimeUtils.getDataAtual();

		sql.addCustomCriteria("? between mf.dtVigenciaInicial and mf.dtVigenciaFinal", today);
		sql.addCustomCriteria("? between osl.dtVigenciaInicial and osl.dtVigenciaFinal", today);
		sql.addCriteria("mf.municipio.idMunicipio", "=", idMunicipioOrigem);
		sql.addCustomCriteria("(osl.servico.idServico is null or osl.servico.idServico = ?)", idServico);

		if (tpOperacao.equals("C"))
			sql.addCustomCriteria("((osl.tpOperacao = 'C') or (osl.tpOperacao = 'A'))");
		else if (tpOperacao.equals("E"))
			sql.addCustomCriteria("((osl.tpOperacao = 'E') or (osl.tpOperacao = 'A'))");

		sql.addCustomCriteria("mf.blPadraoMcd = 'S'");
		sql.addOrderBy("osl.servico.idServico");

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	/**
	 * Consulta os atendimentos a partir dos parametros informados 
	 * 
	 * @param idMunicipio
	 * @param indicadorColeta
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 * @deprecated
	 * @see 
	 */
	public List findAtendimentosVigentesByMunicipioServicoOperacao(Long idMunicipio, Boolean indicadorColeta, Long idServico, YearMonthDay dtVigencia){

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("mf.idMunicipioFilial");
		sql.addProjection("mf.blRestricaoAtendimento");
		sql.addProjection("mf.blRestricaoTransporte");
		sql.addProjection("opl.blAtendimentoGeral");
		sql.addProjection("fil.idFilial");
		sql.addProjection("opl.idOperacaoServicoLocaliza");
		sql.addProjection("opl.tpOperacao");
		sql.addProjection("opl.tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio");
		sql.addProjection("fil.sgFilial");
		sql.addProjection("fil_pes.nmFantasia");
		sql.addProjection("opl.nrTempoColeta");
		sql.addProjection("opl.nrTempoEntrega");
		sql.addProjection("opl.blDomingo");
		sql.addProjection("opl.blSegunda");
		sql.addProjection("opl.blTerca");
		sql.addProjection("opl.blQuarta");
		sql.addProjection("opl.blQuinta");
		sql.addProjection("opl.blSexta");
		sql.addProjection("opl.blSabado");

		sql.addFrom("MunicipioFilial mf inner join mf.operacaoServicoLocalizas opl inner join mf.filial fil inner join fil.pessoa fil_pes");

		sql.addCriteria("mf.municipio.idMunicipio", "=", idMunicipio);
		if (indicadorColeta.booleanValue())
			sql.addCustomCriteria("(opl.tpOperacao = 'A' or opl.tpOperacao = 'C')");
		else
			sql.addCustomCriteria("(opl.tpOperacao = 'A' or opl.tpOperacao = 'E')");		
		sql.addCustomCriteria("(opl.servico.idServico is null or opl.servico.idServico = ?)", idServico);
		sql.addCriteria("mf.dtVigenciaInicial", "<=", dtVigencia);
		sql.addCriteria("mf.dtVigenciaFinal", ">=", dtVigencia);
        sql.addCustomCriteria("? between opl.dtVigenciaInicial and opl.dtVigenciaFinal", dtVigencia);

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	public Map findFilialByMunipioNrCep(Long idMunicipio, String nrCep) {
		List l = getAdsmHibernateTemplate().findByNamedQueryAndNamedParam(MunicipioFilial.FIND_FILIAL_BY_MUNICIPIO_NR_CEP, new String[] { "idMunicipio", "nrCep", "dataAtual" },
				new Object[]{idMunicipio, nrCep, JTDateTimeUtils.getDataAtual()});
		if(!l.isEmpty())
			return (Map)l.get(0);
		return new HashMap();
	}

	/**
	 * Método q busca a filial de acordo com o municipio e o cep fornecido
	 * retorna os dados da filial (id, sigla, nome e telefone da mesma)
	 * 
	 * autor Pedro Henrique Jatobá 26/12/2005
	 * 
	 * @param idMunicipio
	 * @param nrCep
	 * @return
	 */
	public Map findFilialDadosByMunipioNrCep(Long idMunicipio, String nrCep) {
		List l = getAdsmHibernateTemplate().findByNamedQueryAndNamedParam(MunicipioFilial.FIND_FILIAL_DADOS_BY_MUNICIPIO_NR_CEP, new String[] { "idMunicipio", "nrCep", "dataAtual" },
				new Object[]{idMunicipio, nrCep, JTDateTimeUtils.getDataAtual()});
		if(!l.isEmpty())
			return (Map)l.get(0);
		return new HashMap();
	}

	/**
	 * Método q busca a filial de acordo com o municipio e o cep fornecido
	 * retorna os dados da filial (id, sigla, nome e telefone da mesma)
	 * 
	 * autor Pedro Henrique Jatobá 26/12/2005
	 * 
	 * @param idMunicipio
	 * @param nrCep
	 * @return
	 */
	public List<MunicipioFilial> findMunicipioFilialByIdMunipioIdEmpresa(Long idMunicipio, Long idEmpresa) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("munf");
		hql.addProjection("muni");
		hql.addProjection("fili");
		hql.addProjection("empr");
		
		hql.addLeftOuterJoin(MunicipioFilial.class.getName(),"munf");
		hql.addLeftOuterJoin("fetch munf.municipio","muni");
		hql.addLeftOuterJoin("fetch munf.filial","fili");
		hql.addLeftOuterJoin("fetch fili.empresa","empr");
		hql.addCriteria("muni.idMunicipio","=",idMunicipio);
		hql.addCriteria("empr.idEmpresa","=",idEmpresa);
				
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());		
	}
	
	/**
	 * Retorna a distancia total entre o municipio e a filial que o atende
	 * 
	 * @param idMunicipioFilial
	 * @return
	 */
	public Integer findDistanciaTotalMunicipioFilial(Long idMunicipioFilial){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("(nvl(mf.nrDistanciaAsfalto,0) + nvl(mf.nrDistanciaChao,0) + nvl(mf.nrGrauDificuldade,0))");
		sql.addFrom("MunicipioFilial mf");
		sql.addCriteria("mf.idMunicipioFilial", "=", idMunicipioFilial);

		return (Integer) getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria()).get(0);
	}
	
	/**
	 * Consulta todos os atendimentos padrao MCD vigentes na data informada,
	 * fazendo um produto cartesiano com todos os servicos que geram MCD
	 * 
	 * @param dtVigencia
	 * @return
	 */
	public List findMunicipiosFilialDestinoMCD(YearMonthDay dtVigencia, Long idServico){				
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("mfDest.idMunicipioFilial");
		sql.addProjection("mfDest.municipio.idMunicipio");
		sql.addProjection("mfDest.filial.idFilial");
		sql.addProjection("oslDest.tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio");
		sql.addProjection("oslDest.blDomingo");
		sql.addProjection("oslDest.blSegunda");
		sql.addProjection("oslDest.blTerca");
		sql.addProjection("oslDest.blQuarta");
		sql.addProjection("oslDest.blQuinta");
		sql.addProjection("oslDest.blSexta");
		sql.addProjection("oslDest.blSabado");
		sql.addProjection("oslDest.nrTempoEntrega");
		sql.addProjection("(case when (oslDest.blDomingo = 'S') then 1 else 0 end + " + "case when (oslDest.blSegunda = 'S') then 1 else 0 end + "
			+ "case when (oslDest.blTerca = 'S')   then 1 else 0 end + " + "case when (oslDest.blQuarta = 'S')  then 1 else 0 end + " + "case when (oslDest.blQuinta = 'S')  then 1 else 0 end + "
			+ "case when (oslDest.blSexta = 'S')   then 1 else 0 end + " + "case when (oslDest.blSabado = 'S')  then 1 else 0 end) as nrDiasAtendidos");
		sql.addProjection("nvl(mfDest.nrDistanciaAsfalto,0) + nvl(mfDest.nrDistanciaChao,0) + nvl(mfDest.nrGrauDificuldade,0)) as distanciaDestino");
		
		sql.addFrom("MunicipioFilial mfDest inner join mfDest.operacaoServicoLocalizas oslDest ");
		
		sql.addCustomCriteria("mfDest.blPadraoMcd = 'S'");
		sql.addCriteria("mfDest.dtVigenciaInicial", "<=", dtVigencia);
		sql.addCriteria("mfDest.dtVigenciaFinal", ">=", dtVigencia);	
		
		sql.addCriteria("mfDest.filial.idFilial","=",Long.valueOf(378));
		
		sql.addCustomCriteria("(oslDest.tpOperacao = 'A' or oslDest.tpOperacao = 'E')");
		sql.addCustomCriteria("(oslDest.servico.idServico = ? or oslDest.servico.idServico is null)");
		sql.addCriteriaValue(idServico);
		sql.addCustomCriteria("oslDest.dtVigenciaInicial <= ? and oslDest.dtVigenciaFinal >= ?");
		sql.addCriteriaValue(dtVigencia);
		sql.addCriteriaValue(dtVigencia); 
		
		sql.addOrderBy("mfDest.filial.idFilial");

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}

	public Integer getRowCountByIdMunicipioByIdFilial(Long idMunicipio, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf").setProjection(Projections.count("mf.id")).add(Restrictions.eq("mf.municipio.id", idMunicipio))
			.add(Restrictions.eq("mf.filial.id", idFilial));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountByIdMunicipioBlRecebeColeta(Long idMunicipio, Boolean blRecebeColetaEventual) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf").setProjection(Projections.count("mf.id")).add(Restrictions.eq("mf.municipio.id", idMunicipio))
			.add(Restrictions.eq("mf.blRecebeColetaEventual", blRecebeColetaEventual));
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		JTVigenciaUtils.getDetachedVigencia(dc, dataAtual, dataAtual);
		
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Integer getRowCountMunicipioFilial(Long idMunicipio, Boolean blRecebeColetaEventual, Boolean blPadraoMcd, YearMonthDay dtVigencia) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf").setProjection(Projections.count("mf.id")).add(Restrictions.eq("mf.municipio.id", idMunicipio))
			.add(Restrictions.eq("mf.blRecebeColetaEventual", blRecebeColetaEventual)).add(Restrictions.eq("mf.blPadraoMcd", blPadraoMcd));
			JTVigenciaUtils.getDetachedVigencia(dc, dtVigencia, dtVigencia);

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	//busca somente os registros vigentes
	public List findMunicipioFilialVigenteAtualByMunicipio(Long idMunicipio){
		StringBuffer hql = new StringBuffer().append("select new Map(pes.nmPessoa as nmEmpresa, ").append("fil.sgFilial as sgFilial, ").append("fil.idFilial as idFilial, ")
			.append("munFil.idMunicipioFilial as idMunicipioFilial, ").append("filPes.nmFantasia as nmFilial) ").append("from " + MunicipioFilial.class.getName() + " as munFil ")
			.append("join munFil.filial as fil ").append("left outer join fil.pessoa as filPes ").append("left outer join fil.empresa as emp ").append("left outer join emp.pessoa as pes ")
			.append("where munFil.dtVigenciaInicial <= ? ").append("and munFil.dtVigenciaFinal >= ? ").append("and munFil.municipio.idMunicipio = ? ")
		.append("order by pes.nmPessoa, filPes.nmFantasia");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{dataAtual,dataAtual,idMunicipio});
		return lista;
	}

	// busca somente os registros vigentes para paginação.Tela consultar
	// Municipios -> municipios atendidos
	public ResultSetPage findPaginatedMunicipioFilialVigenteByMunicipio(Map criteria, FindDefinition findDef){
		StringBuffer hql = new StringBuffer().append("select new Map(pes.nmPessoa as nmEmpresa, ").append("fil.sgFilial as sgFilial, ").append("munFil.idMunicipioFilial as idMunicipioFilial, ")
			.append("filPes.nmFantasia as nmFilial) ").append("from " + MunicipioFilial.class.getName() + " as munFil ").append("join munFil.filial as fil ")
			.append("left outer join fil.pessoa as filPes ").append("left outer join fil.empresa as emp ").append("left outer join emp.pessoa as pes ")
			.append("where munFil.dtVigenciaInicial <= :dataAtual ").append("and munFil.dtVigenciaFinal >= :dataAtual ").append("and munFil.municipio.idMunicipio = :idMunicipio ")
		.append("order by pes.nmPessoa, filPes.nmFantasia");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		Map parameters = new HashMap();
		parameters.put("dataAtual",dataAtual);
		parameters.put("idMunicipio",criteria.get("idMunicipio"));

		return getAdsmHibernateTemplate().findPaginated(hql.toString(),findDef.getCurrentPage(),findDef.getPageSize(),parameters);
	}

	public Integer getRowCountMunicipioFilialVigenteByMunicipio(Long idMunicipio) {
		StringBuffer hql = new StringBuffer().append("from " + MunicipioFilial.class.getName() + " as munFil ").append("where munFil.dtVigenciaInicial <= :dataAtual ")
			.append("and munFil.dtVigenciaFinal >= :dataAtual ").append("and munFil.municipio.idMunicipio = :idMunicipio ");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		Map parameters = new HashMap();
		parameters.put("dataAtual",dataAtual);
		parameters.put("idMunicipio",idMunicipio);

		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(),parameters);
	}

	/**
	 * Consulta a menor data de vigencia inicial e a maior data de vigencia dos
	 * atendimentos existentes para o municipio
	 *
	 * @param idMunicipio
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public Map findIntervaloVigenciaByMunicipio(Long idMunicipio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(min(mf.dtVigenciaInicial)", "dtVigenciaInicial");
		sql.addProjection("max(mf.dtVigenciaFinal)", "dtVigenciaFinal)");

		sql.addFrom(getPersistentClass().getName(), "mf");

		sql.addCriteria("mf.municipio.id", "=", idMunicipio);

		List result = (List) getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		return (Map) (result.isEmpty() ? null : result.get(0));
	}

	/**
	 * verifica se o municipio esta vigente na data atual de acordo com o
	 * idFilial e idMunicipio
	 *
	 * @author Diego Umpierre 09/08/2006
	 * @param idFilial
	 *            ,idMunicipio
	 * @return boolean - Retorna true se a filial estiver vigente para o
	 *         municipio em questao na data atual, caso contrario retorna false
	 */
	public boolean verificaVigenciaMunicipioByFilialMunicipio(Long idFilial,Long idMunicipio){
		StringBuffer hql = new StringBuffer().append("select 1 ").append(" from ").append(MunicipioFilial.class.getName()).append(" as munFil ").append(" where munFil.dtVigenciaInicial <= ? ")
			.append(" and munFil.dtVigenciaFinal >= ? ").append(" and munFil.filial.idFilial = ? ").append(" and munFil.municipio.idMunicipio = ? ");

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

		List lista = getAdsmHibernateTemplate().find(hql.toString(),new Object[]{dataAtual,dataAtual,idFilial,idMunicipio});
		return (lista.isEmpty() ? false : true);
	}

	public MunicipioFilial findMunicipioFilial(Long idMunicipio, Long idFilial) {
		DetachedCriteria dc = createDetachedCriteria();

		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

		dc.add(Restrictions.le("dtVigenciaInicial", dtToday));
		dc.add(Restrictions.or(Restrictions.eq("dtVigenciaFinal", JTDateTimeUtils.MAX_YEARMONTHDAY), Restrictions.ge("dtVigenciaFinal", dtToday)));

		dc.add(Restrictions.eq("municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));

		List<MunicipioFilial> result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if(result.size() == 1) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * verifica se Municipio passado pertence a alguma Filial da Matriz.
	 * 
	 * @param idMunicipio
	 * @return
	 */
	public Boolean isMunicipioFilialMatriz(Long idMunicipio) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf").createAlias("mf.filial", "f").createAlias("f.empresa", "e").setProjection(Projections.count("mf.id"))
			.add(Restrictions.eq("mf.municipio.id", idMunicipio)).add(Restrictions.eq("e.tpEmpresa", ConstantesExpedicao.TP_EMPRESA_MERCURIO));
			JTVigenciaUtils.getDetachedVigencia(dc, JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual());

		return CompareUtils.gt(getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc), IntegerUtils.ZERO);
	}
	
	public List findListMunicipiosFilialComUF(){
		ProjectionList pl = Projections.projectionList().add(Projections.property("m.idMunicipio"), "idMunicipio").add(Projections.property("m.nmMunicipio"), "nmMunicipio")
			.add(Projections.property("uf.sgUnidadeFederativa"), "sgUnidadeFederativa");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf");
		dc.createAlias("mf.municipio", "m");
		dc.createAlias("m.unidadeFederativa","uf");
		dc.add(Restrictions.eq("mf.blPadraoMcd", true)).add(Restrictions.le("mf.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("mf.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		dc.setProjection(pl);
		dc.addOrder(Order.asc("m.nmMunicipio"));
		dc.addOrder(Order.asc("uf.sgUnidadeFederativa"));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List findMunicipiosComRestricaoTransporte(TypedFlatMap criteria){
		DetachedCriteria dc = montaDcMunicipioComRestricaoTransporte();		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * 
	 * @param idFranquia
	 * @param idMunicipioColetaEntrega
	 * @param dtInicioCompetencia
	 * @return MunicipioFilial
	 */
	@SuppressWarnings("unchecked")
	public MunicipioFilial findByIdFranquiaMunicipioVigencia(long idFranquia, long idMunicipioColetaEntrega, YearMonthDay dtInicioCompetencia) {
		
		StringBuffer hql = new StringBuffer().append("select munFil from " + MunicipioFilial.class.getName() + " as munFil	").append("where munFil.filial.idFilial = ?				")
			.append("and munFil.municipio.idMunicipio = ? 		").append("and munFil.dtVigenciaInicial <= ? 				").append("and munFil.dtVigenciaFinal >= ? 				");
	
		List<MunicipioFilial> municipioFiliais = (List<MunicipioFilial>) getAdsmHibernateTemplate().find(hql.toString(),
			new Object[] { Long.valueOf(idFranquia), Long.valueOf(idMunicipioColetaEntrega), dtInicioCompetencia, dtInicioCompetencia });
		if(municipioFiliais.isEmpty()){
			return null;
		}
		return municipioFiliais.get(0) ;
	}

	private DetachedCriteria montaDcMunicipioComRestricaoTransporte(){
		ProjectionList pl = Projections.projectionList().add(Projections.property("m.idMunicipio"), "idMunicipio").add(Projections.property("m.nmMunicipio"), "nmMunicipio")
			.add(Projections.property("mf.blRestricaoTransporte"), "blRestricaoTransporte").add(Projections.property("uf.sgUnidadeFederativa"), "sgUnidadeFederativa");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mf");
		dc.createAlias("mf.municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");
		dc.add(Restrictions.eq("mf.blRestricaoTransporte", true)).add(Restrictions.eq("mf.blPadraoMcd", true)).add(Restrictions.le("mf.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()))
			.add(Restrictions.ge("mf.dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		dc.setProjection(pl);
		dc.addOrder(Order.asc("m.nmMunicipio"));
		return dc;
	}
	
	public List<Municipio> findMunicipioByIdFilialParaImportador(Long idFilial) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT distinct m ");
		query.append("FROM MunicipioFilial mf ");
		query.append("JOIN mf.filial f ");
		query.append("JOIN mf.municipio m ");
		query.append("JOIN m.unidadeFederativa uf ");
		query.append("WHERE f.idFilial = :idFilial ");
		query.append("AND (mf.dtVigenciaInicial <= :dtAtual ");
		query.append("		AND (mf.dtVigenciaFinal is null OR mf.dtVigenciaFinal >= :dtAtual )) ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idFilial", idFilial);
		parametros.put("dtAtual", JTDateTimeUtils.getDataAtual());
		
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}
	
}
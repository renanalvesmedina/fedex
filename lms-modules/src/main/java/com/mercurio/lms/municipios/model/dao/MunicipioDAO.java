package com.mercurio.lms.municipios.model.dao;
 
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.IntervaloCepUF;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicaÁ„o
 * atravÈs do suporte ao Hibernate em conjunto com o Spring.
 * N„o inserir documentaÁ„o apÛs ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
@SuppressWarnings("deprecation")
public class MunicipioDAO extends BaseCrudDao<Municipio, Long> {
	/**
	 * Nome da classe que o DAO È respons·vel por persistir.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected final Class getPersistentClass() {
		return Municipio.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipioDistrito", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa.pais", FetchMode.JOIN);			
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("municipioDistrito", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa.pais.zona", FetchMode.JOIN);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuery(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}

	public Integer getRowCountConsultaMunicipios(TypedFlatMap criteria) {
		SqlTemplate hql = montaQueryConsultaMunicipios(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
		return rowCountQuery;
	}

	@SuppressWarnings({ "rawtypes" })
	public ResultSetPage findPaginatedConsultarMunicipios(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQueryConsultaMunicipios(criteria);
		ResultSetPage rs = getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),hql.getCriteria());
		return rs;
	}

	@SuppressWarnings({ "rawtypes" })
	public Map findDadosMunicipioById(Long id) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("municipio.idMunicipio", id);
		SqlTemplate hql = montaQueryConsultaMunicipios(criteria);
		Map rs = (Map) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return rs;
	}

	private SqlTemplate montaQueryConsultaMunicipios(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map("
			+ "mun.nmMunicipio as nmMunicipio, "
			+ "mun.idMunicipio as idMunicipio, "
			+ "uf.sgUnidadeFederativa as sgUnidadeFederativa, "
			+ "uf.sgUnidadeFederativa||' - '||uf.nmUnidadeFederativa as unidadeFederativa_siglaDescricao, "
			+ "paisM.nmPais as unidadeFederativa_pais_nmPais, "
			+ "mun.blDistrito as blDistrito, "
			+ "munD.nmMunicipio as municipioDistrito_nmMunicipio) ");

		sql.addFrom("Municipio mun "
			+ "left join mun.unidadeFederativa uf "
			+ "left join mun.municipioDistrito munD "
			+ "left join uf.pais paisM ");

		sql.addCriteria("mun.idMunicipio", "=" , criteria.getLong("municipio.idMunicipio"), Long.class);
		sql.addCriteria("uf.idUnidadeFederativa", "=" , criteria.getLong("unidadeFederativa.idUnidadeFederativa"), Long.class);
		sql.addCriteria("paisM.idPais", "=" , criteria.getLong("pais.idPais"), Long.class);
		sql.addCriteria("mun.nrCep", "like" , criteria.getString("nrCep"));
		sql.addCriteria("munD.idMunicipio", "=" , criteria.getLong("municipioDistrito.idMunicipio"));
		sql.addCriteria("mun.blDistrito", "=" , criteria.getBoolean("blDistrito"));

		if (!StringUtils.isBlank(criteria.getString("filial.idFilial"))){
			sql.addCustomCriteria("exists(select mf.idMunicipioFilial from MunicipioFilial as mf where mf.filial.id = ? and mf.municipio.idMunicipio = mun.idMunicipio)");
			sql.addCriteriaValue(criteria.getLong("filial.idFilial"));
		}

		sql.addOrderBy("mun.nmMunicipio");

		return sql ;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List findLookupMunicipio(TypedFlatMap criteria){
		SqlTemplate sql = montaQuery(criteria);
		return getAdsmHibernateTemplate().find(sql.getSql(true) ,sql.getCriteria());
	}

	@SuppressWarnings({ "rawtypes" })
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = montaQuery(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	private SqlTemplate montaQuery(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(m.idMunicipio as idMunicipio," 
			+ "m.nmMunicipio as nmMunicipio, "
			+ "m.nrCep as nrCep, "
			+ "m.nmMunicipio || ' - ' || uf.sgUnidadeFederativa as nmMunicipioAndSgUnidadeFederativa, "			
			+ "uf.sgUnidadeFederativa as unidadeFederativa_sgUnidadeFederativa, "
			+ "uf.idUnidadeFederativa as unidadeFederativa_idUnidadeFederativa, "
			+ "uf.nmUnidadeFederativa as unidadeFederativa_nmUnidadeFederativa, "
			+ "uf.sgUnidadeFederativa||' - '||uf.nmUnidadeFederativa as unidadeFederativa_siglaDescricao, "
			+ "md.idMunicipio as municipioDistrito_idMunicipio, "
			+ "md.nmMunicipio as municipioDistrito_nmMunicipio, "
			+ "p.idPais as unidadeFederativa_pais_idPais, "
			+ "p.sgPais as unidadeFederativa_pais_sgPais, "
			+ "p.nmPais as unidadeFederativa_pais_nmPais, "
			+ "m.blDistrito as blDistrito, "
			+ "m.tpSituacao as tpSituacao, "
			+ "m.nrDistanciaCapital as nrDistanciaCapital)");

		sql.addFrom("Municipio m join m.unidadeFederativa uf "
			+ "left join m.municipioDistrito md "
			+ "join uf.pais p");

		sql.addCriteria("lower(m.nmMunicipio)", "like", criteria.getString("nmMunicipio").toLowerCase());
		sql.addCriteria("m.nrCep", "like", criteria.getString("nrCep"));
		sql.addCriteria("uf.idUnidadeFederativa", "=", criteria.getLong("unidadeFederativa.idUnidadeFederativa"), Long.class );
		sql.addCriteria("md.idMunicipio", "=", criteria.getLong("municipioDistrito.idMunicipio"), Long.class);
		sql.addCriteria("p.idPais", "=", criteria.getLong("unidadeFederativa.pais.idPais"), Long.class);
		sql.addCriteria("m.blDistrito", "=", criteria.getBoolean("blDistrito"));
		sql.addCriteria("m.tpSituacao", "=", criteria.get("tpSituacao") != null ? criteria.getDomainValue("tpSituacao").getValue() : null);

		// Criterios para trazer somente municipios atendidos pela Filial informada (utilizado na lookup Municipio da tela Manter Intervalos de CEP da Rota de Coleta/Entrega)
		boolean porFilial = true;
		if( criteria.containsKey("blAtendimentoTemporario") && "S".equals(criteria.getString("blAtendimentoTemporario"))){
			porFilial = false;
		}
		
		if (criteria.getLong("municipioFiliais.filial.idFilial") != null && porFilial){
			sql.addCustomCriteria("exists (select 1 from MunicipioFilial mf join mf.municipio mu join mf.filial f where mu.id = m.id ");
			sql.addCustomCriteria("f.id = ?)", criteria.getLong("municipioFiliais.filial.idFilial"));
		}
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("p.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("m.nmMunicipio");

		return sql;
	}

	/**
	 * Valida se o CEP esta dentro da faixa de CEP da UF
	 * @param idUnidadeFederativa
	 * @param nrCep 
	 * @return TRUE ou FALSE
	 */
	public boolean validaIntervaloCep (Long idUnidadeFederativa, String nrCep) {
		DetachedCriteria dc = DetachedCriteria.forClass(IntervaloCepUF.class);
 	
		dc.add(	Restrictions.eq("unidadeFederativa.idUnidadeFederativa", idUnidadeFederativa) );
		dc.add( Restrictions.le("nrCepInicial", nrCep));
		dc.add( Restrictions.ge("nrCepFinal", nrCep));

		dc.setProjection(Projections.count("unidadeFederativa.idUnidadeFederativa"));
		Integer num = (Integer)findByDetachedCriteria(dc).get(0);
 
		return ( (num.intValue() > 0) );
	}

	@SuppressWarnings("rawtypes")
	public boolean isMunicipioAtivo(Long idMunicipio) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("tpSituacao","A"))
		.add(Restrictions.eq("idMunicipio",idMunicipio));
		List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (rs == null || rs.size() == 0)
			return false;
		return true;
	}

	@SuppressWarnings("rawtypes")
	public List findByNmMunicipioTpSituacao(String nmMunicipio, String tpSituacao){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("m.idMunicipio"), "idMunicipio")
			.add(Projections.property("m.nmMunicipio"), "nmMunicipio")
			.add(Projections.property("uf.idUnidadeFederativa"), "unidadeFederativa_idUnidadeFederativa")
			.add(Projections.property("uf.sgUnidadeFederativa"), "unidadeFederativa_sgUnidadeFederativa")
			.add(Projections.property("uf.nmUnidadeFederativa"), "unidadeFederativa_nmUnidadeFederativa")
			.add(Projections.property("p.idPais"), "unidadeFederativa_pais_idPais")
			.add(Projections.property("p.nmPais"), "unidadeFederativa_pais_nmPais");
		DetachedCriteria dc = DetachedCriteria.forClass(Municipio.class, "m")
			.setProjection(pl) 
			.createAlias("unidadeFederativa", "uf")
			.createAlias("uf.pais", "p")
			.add(Restrictions.ilike("m.nmMunicipio", nmMunicipio))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (tpSituacao != null)
			dc.add(Restrictions.eq("m.tpSituacao", tpSituacao));
		return findByDetachedCriteria(dc);
	}

	@SuppressWarnings("rawtypes")
	public List findByNmMunicipioBySgUfByTpSituacao(String nmMunicipio, String sgUnidadeFederativa, String tpSituacao){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("m.idMunicipio"), "idMunicipio")
			.add(Projections.property("m.nmMunicipio"), "nmMunicipio")
			.add(Projections.property("uf.idUnidadeFederativa"), "unidadeFederativa_idUnidadeFederativa")
			.add(Projections.property("uf.sgUnidadeFederativa"), "unidadeFederativa_sgUnidadeFederativa")
			.add(Projections.property("uf.nmUnidadeFederativa"), "unidadeFederativa_nmUnidadeFederativa")
			.add(Projections.property("p.idPais"), "unidadeFederativa_pais_idPais")
			.add(Projections.property("p.nmPais"), "unidadeFederativa_pais_nmPais");
		DetachedCriteria dc = DetachedCriteria.forClass(Municipio.class, "m")
			.setProjection(pl)  
			.createAlias("unidadeFederativa", "uf")
			.createAlias("uf.pais", "p")
			.add(Restrictions.ilike("m.nmMunicipio", nmMunicipio, MatchMode.EXACT))
			.add(Restrictions.ilike("uf.sgUnidadeFederativa", sgUnidadeFederativa, MatchMode.EXACT))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		if (tpSituacao != null)
			dc.add(Restrictions.eq("m.tpSituacao", tpSituacao));
		return findByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> findByIdUnidadeFederativa(Long idUnidadeFederativa){
		StringBuilder hql = new StringBuilder();
		hql.append(" select m ");
		hql.append(" from Municipio m ");
		hql.append(" inner join m.unidadeFederativa uf ");
		hql.append(" where uf.idUnidadeFederativa = ? ");
		hql.append(" and m.blDistrito = ? ");
		hql.append(" order by m.nmMunicipio ");
		
		return getHibernateTemplate().find(hql.toString(), new Object[]{idUnidadeFederativa, Boolean.FALSE});
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> findByIdUnidadeFederativaComDistrito(Long idUnidadeFederativa){
		StringBuilder hql = new StringBuilder();
		hql.append(" select m ");
		hql.append(" from Municipio m ");
		hql.append(" inner join m.unidadeFederativa uf ");
		hql.append(" where uf.idUnidadeFederativa = ? ");
		hql.append(" order by m.nmMunicipio ");
		
		return getHibernateTemplate().find(hql.toString(), new Object[]{idUnidadeFederativa});
	}
	
	@SuppressWarnings("unchecked")
	public Municipio findByNmMunicipioAndUf(String nmMunicipio, String sgUnidadeFederativa){
		StringBuilder hql = new StringBuilder();
		hql.append(" select municipio ");
		hql.append(" from Municipio municipio ");
		hql.append(" inner join municipio.unidadeFederativa uf ");
		hql.append(" where upper(uf.sgUnidadeFederativa) = ? ");
		hql.append(" and translate(upper(municipio.nmMunicipio), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù','AEIOUaeiouCcAaAaOoAaEeOo') = translate(upper(?), '¡…Õ”⁄·ÈÌÛ˙«Á¿‡√„’ı¬‚ Í‘Ù','AEIOUaeiouCcAaAaOoAaEeOo') ");
		
		List<Municipio> municipios = getHibernateTemplate().find(hql.toString(), new Object[]{sgUnidadeFederativa.toUpperCase(), nmMunicipio.toUpperCase()});
		
		if (isNotEmpty(municipios)){
			return municipios.get(0);
		}
		
		return null;
	}

	
	@SuppressWarnings("rawtypes")
	public List findByNmMunicipioTpSituacaoPaisUf(String nmMunicipio,Long idPais,Long idUf, String tpSituacao){
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("m.idMunicipio"), "idMunicipio")
			.add(Projections.property("m.nmMunicipio"), "nmMunicipio")
			.add(Projections.property("uf.idUnidadeFederativa"), "unidadeFederativa_idUnidadeFederativa")
			.add(Projections.property("uf.sgUnidadeFederativa"), "unidadeFederativa_sgUnidadeFederativa")
			.add(Projections.property("uf.nmUnidadeFederativa"), "unidadeFederativa_nmUnidadeFederativa")
			.add(Projections.property("p.idPais"), "unidadeFederativa_pais_idPais")
			.add(Projections.property("p.nmPais"), "unidadeFederativa_pais_nmPais");
		DetachedCriteria dc = DetachedCriteria.forClass(Municipio.class, "m")
			.setProjection(pl) 
			.createAlias("unidadeFederativa", "uf")
			.createAlias("uf.pais", "p")
			.add(Restrictions.ilike("m.nmMunicipio", nmMunicipio))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (tpSituacao != null)
			dc.add(Restrictions.eq("m.tpSituacao", tpSituacao));
		if (idUf != null)
			dc.add(Restrictions.eq("uf.idUnidadeFederativa", idUf));
		if (idPais != null)
			dc.add(Restrictions.eq("p.idPais", idPais));
		return findByDetachedCriteria(dc);
	}

	@SuppressWarnings("rawtypes")
	public List findByMunicipioFilial(Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("municipioFiliais", "mf");
		dc.createAlias("mf.filial", "fil");

		dc.add(Restrictions.eq("fil.idFilial", idFilial));
		dc.add(
				Restrictions.or(
					Restrictions.isNull("mf.dtVigenciaFinal"),
					Restrictions.ge("mf.dtVigenciaFinal", new Date())
				)
		);	
		dc.add(Restrictions.le("mf.dtVigenciaInicial", new Date()));

		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("idMunicipio"), "idMunicipio")
			.add(Projections.property("nmMunicipio"), "nmMunicipio");

		dc.setProjection(pl);
		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	
	public Municipio findMunicipioByFilial(Long idFilial){
		StringBuilder str = new StringBuilder();
		str.append("select ep.municipio from EnderecoPessoa ep join ep.pessoa p where p.idPessoa = ?");
		return (Municipio)getAdsmHibernateTemplate().findUniqueResult(str.toString(), new Object[]{idFilial});
		
	}


	/**
	 * MÈtodo que busca os municpios baseado na rotaColetaEntrega.
	 * Apenas s„o setados as seguintes propriedades: idMunicipio, nrCep 
	 * @param idRotaColetaEntrega
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findMunicipiosByRotaColetaEntrega(Long idRotaColetaEntrega) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("m.idMunicipio"), "idMunicipio");
		projectionList.add(Projections.property("m.nrCep"), "nrCep");

		DetachedCriteria dc = DetachedCriteria.forClass(RotaIntervaloCep.class,"ric");
		dc.setProjection(projectionList);

		dc.createAlias("ric.rotaColetaEntrega", "rce"); 
		dc.createAlias("ric.municipio", "m"); 

		dc.add(Restrictions.eq("rce.idRotaColetaEntrega",idRotaColetaEntrega));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Municipio.class)); 

		List retorno = findByDetachedCriteria(dc);
		return retorno;
	}

	@SuppressWarnings("rawtypes")
	public Map findZonaPaisUFByIdMunicipio(Long idMunicipio) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("m.idMunicipio"), "idMunicipio")
			.add(Projections.property("uf.idUnidadeFederativa"), "idUnidadeFederativa")
			.add(Projections.property("p.idPais"), "idPais")
			.add(Projections.property("z.idZona"), "idZona");

		DetachedCriteria dc = DetachedCriteria.forClass(Municipio.class,"m")
			.setProjection(pl)
			.createAlias("m.unidadeFederativa", "uf") 
			.createAlias("uf.pais", "p")
			.createAlias("p.zona", "z")
			.add(Restrictions.eq("m.id", idMunicipio))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP); 

		List l = findByDetachedCriteria(dc);
		if (!l.isEmpty())
			return ((Map)l.get(0));
		return null;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public List findByIdCustom(Long idMunicipio){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"mun.idMunicipio as idMunicipio, " +
				"mun.tpSituacao as tpSituacao, " +
				"mun.nmMunicipio as nmMunicipio, " +
				"mun.nrCep as nrCep, " +
				"mun.cdIbge as cdIbge, " +
				"mun.nrPopulacao as nrPopulacao, " +
				"mun.cdEstadual as cdEstadual, " +
				"mun.blDistrito as blDistrito, " +
				"md.nmMunicipio as municipioDistrito, " + 
				"uf.sgUnidadeFederativa as sgUnidadeFederativa, " +
				"uf.nmUnidadeFederativa as nmUnidadeFederativa, " +
				"pais.nmPais as pais" +
				")");
		hql.addFrom("Municipio mun " +
				"join mun.unidadeFederativa uf " +
				"join uf.pais pais " +
				"left outer join mun.municipioDistrito as md ");
		
		hql.addCriteria("mun.idMunicipio","=",idMunicipio);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		
	}
	
		
	
	/**
	 * Retorna o id do municipio do endereÁo padr„o (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author MickaÎl Jalbert
	 * @since 14/08/2006
	 * 
	 * @param Long idPessoa
	 * @return Long
	 */
	@SuppressWarnings({ "rawtypes" })
	public Long findIdMunicipioByPessoa(Long idPessoa){	
		SqlTemplate hql = mountHqlPessoa(idPessoa);
		
		hql.addProjection("mu.id");

		List lstMunicipios = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (!lstMunicipios.isEmpty()){
			return (Long) lstMunicipios.get(0);
		} else {
			return null;
		}
	}	
	
	/**
	 * Monta o hql que liga a pessoa com o municipio passando com o endereÁo padr„o da pessoa.
	 * 
	 * @author MickaÎl Jalbert
	 * @since 14/08/2006
	 * 
	 * @param Long idPessoa
	 * @return SqlTemplate
	 */
	private SqlTemplate mountHqlPessoa(Long idPessoa) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addInnerJoin(Pessoa.class.getName(), "pes");
		hql.addInnerJoin("pes.enderecoPessoa", "ep");
		hql.addInnerJoin("ep.municipio", "mu");
		
		hql.addCriteria("pes.id", "=", idPessoa);
		
		return hql;
	}
	
	public boolean findCepByPais(Long idPais, String cep, Long idMunicipio){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("mun.idMunicipio");
		
		hql.addFrom(Municipio.class.getName()+ " mun " +
				"join mun.unidadeFederativa uf " +
				"join uf.pais pais ");
		
		hql.addCriteria("mun.nrCep","=", cep);
		hql.addCriteria("pais.idPais","=",idPais);
		if(idMunicipio!= null)
			hql.addCriteria("mun.idMunicipio","!=", idMunicipio);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
	}
	
	//	###################### *** INTEGRA«√O *** ################################//
	/**
	 * find para buscar o Municipio conforme o cep
	 * MÈtodo solicitado pela equipe de integracao
	 * @param nrCep
	 * @return Municipio
	 */
	@SuppressWarnings({ "unchecked" })
	public Municipio findMunicipioByCep(String nrCep){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(Municipio.class.getName() +" as mu ");

		sql.addCriteria("mu.nrCep","like", nrCep);

		List<Municipio> lstMunicipio = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (lstMunicipio.size() == 1) {
			return lstMunicipio.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * MÈtodo utilizado pela integraÁ„o
	 * CQPRO00006784
	 * 
	 * @param idFiliais
	 * @param tpOperacoes
	 * @param idTipoLocalizacaoMunicipio
	 * @return Lista de municÌpios
	 */
	@SuppressWarnings("unchecked")
	public List<Municipio> findMunicipiosByFiliais(
			List<Long> idFiliais,
			List<String> tpOperacoes,
			Long idTipoLocalizacaoMunicipio 
	) {
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(OperacaoServicoLocaliza.class, "osl");
		dc.setProjection(Projections.distinct(Projections.property("mf.municipio")));
		dc.createAlias("osl.municipioFilial", "mf");
		dc.add(Restrictions.in("mf.filial.id", idFiliais));
		dc.add(Restrictions.le("mf.dtVigenciaInicial", dtToday));
		dc.add(Restrictions.ge("mf.dtVigenciaFinal", dtToday));
		dc.add(Restrictions.in("osl.tpOperacao", tpOperacoes));
		dc.add(Restrictions.ne("osl.tipoLocalizacaoMunicipio.id", idTipoLocalizacaoMunicipio));
		dc.add(Restrictions.le("osl.dtVigenciaInicial", dtToday));
		dc.add(Restrictions.ge("osl.dtVigenciaFinal", dtToday));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * MÈtodo utilizado pela IntegraÁ„o (DBI)
	 * Consulta o municÌpio atravÈs de um intervalo de cep e Unidade Federativa.
	 * Retorna o municÌpio que abranger o cep passado como par‚metro. 
	 * @param nrCep
	 * @param idUnidadeFederativa
	 * @return Municipio
	 */
	@SuppressWarnings("unchecked")
	public Municipio findByIntervaloCepByUf(String nrCep, Long idUnidadeFederativa){
	
		List<Municipio> lstMunicipio = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery("SELECT * FROM MUNICIPIO MUN where exists (select 1 from intervalo_cep ic where ic.id_municipio = mun.id_municipio and  to_number(REGEXP_SUBSTR(nr_cep_inicial, '^\\d*[0-9](|.\\d*[0-9]|,\\d*[0-9])?$')) <= " + nrCep + " and to_number(REGEXP_SUBSTR(ic.nr_cep_final, '^\\d*[0-9](|.\\d*[0-9]|,\\d*[0-9])?$')) >= " + nrCep + ")").addEntity(Municipio.class).list();
	
		if (lstMunicipio.size() == 1) {
			return lstMunicipio.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Query respons·vel por retornar os resultados para a suggest de municÌpio.
	 * 
	 * @param idPais
	 * @param idUnidadeFederativa
	 * @param nmMunicipio
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findMunicipioSuggest(Long idPais, Long idUnidadeFederativa, String nmMunicipio, Integer limiteRegistros) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		String sql = getSqlMunicipioSuggest(idPais, idUnidadeFederativa, nmMunicipio, limiteRegistros, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, parametersValues, getConfigureSqlQueryMunicipioSuggest());
	}
	
	/**
	 * DefiniÁ„o do SQL da query.
	 * 
	 * @param idPais
	 * @param idUnidadeFederativa
	 * @param nmMunicipio
	 * @param limiteRegistros
	 * @param parametersValues
	 * 
	 * @return String
	 */
	private String getSqlMunicipioSuggest(Long idPais, Long idUnidadeFederativa, String nmMunicipio, Integer limiteRegistros, Map<String, Object> parametersValues){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT m.id_municipio AS idMunicipio, m.nm_municipio AS nmMunicipio");
		sql.append(" FROM municipio m");		
		sql.append(" INNER JOIN unidade_federativa u");
		sql.append("  ON u.id_unidade_federativa = m.id_unidade_federativa");
		sql.append(" INNER JOIN pais p");
		sql.append("  ON p.id_pais = u.id_pais");
		sql.append(" WHERE m.tp_situacao = 'A'");
		
		if(limiteRegistros != null){
			sql.append(" AND rownum <= :limite");
			parametersValues.put("limite", limiteRegistros);
		}
		
		if(idPais != null){
			sql.append(" AND p.id_pais = :idPais");
			parametersValues.put("idPais", idPais);	
		}
		
		if(idUnidadeFederativa != null){
			sql.append(" AND u.id_unidade_federativa = :idUnidadeFederativa");
			parametersValues.put("idUnidadeFederativa", idUnidadeFederativa);	
		}
				
		if(StringUtils.isNotBlank(nmMunicipio)){
			sql.append(" AND LOWER(m.nm_municipio) LIKE LOWER(:nmMunicipio)");
			parametersValues.put("nmMunicipio", "%" + nmMunicipio + "%");	
		}

		sql.append(" ORDER BY m.nm_municipio");
		
		return sql.toString();
	}
	
	/**
	 * ProjeÁ„o da query de unidade federativa.
	 * 
	 * @return ConfigureSqlQuery
	 */
	private ConfigureSqlQuery getConfigureSqlQueryMunicipioSuggest(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idMunicipio", Hibernate.LONG);	
				sqlQuery.addScalar("nmMunicipio", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	public Long findIdMunicipioByIdFilial(Long idFilial) {		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("EnderecoPessoa", "ep");
		sql.addFrom("Pessoa", "pes");
		
		sql.addProjection("ep.municipio.id");
		
		sql.addCustomCriteria("pes.id = ep.pessoa.id");
		sql.addCustomCriteria("ep.id = pes.enderecoPessoa.id");
		
		sql.addCriteria("pes.id", "=", idFilial);
		
		return (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }	
	
    @SuppressWarnings("rawtypes")
	public List findCdIbgeByPessoa(Long idPessoa) {
        String sql = "select new map(ep.municipio.cdIbge as cdIbgeMunicipio, ep.municipio.unidadeFederativa.nrIbge as cdIbgeUF) "
                + "from Pessoa p, EnderecoPessoa ep "
                + "where "
                + " p.id = ep.pessoa.id"
                + " and ep.id = p.enderecoPessoa.id"
                + " and p.id = ?";
        
        return getAdsmHibernateTemplate().find(sql, idPessoa);
    }	
	
}

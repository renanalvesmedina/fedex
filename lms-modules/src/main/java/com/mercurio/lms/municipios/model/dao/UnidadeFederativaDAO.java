package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/** 
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class UnidadeFederativaDAO extends BaseCrudDao<UnidadeFederativa, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return UnidadeFederativa.class;
	}
	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("pais",FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("pais",FetchMode.JOIN);
		fetchModes.put("pais.zona",FetchMode.JOIN);
		fetchModes.put("regiaoGeografica", FetchMode.JOIN);
		fetchModes.put("regiaoGeografica", FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
		fetchModes.put("unidadeFederativaSefazVirtual", FetchMode.JOIN);
	}

	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("pais",FetchMode.JOIN); 
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("pais", FetchMode.JOIN);
		fetchModes.put("regiaoGeografica", FetchMode.JOIN);
		fetchModes.put("municipio", FetchMode.JOIN);
	}

	public List findListByCriteria(Map criterions) {
		if (criterions == null) criterions = new HashMap();
		List order = new ArrayList(1);
		order.add("sgUnidadeFederativa");
		return super.findListByCriteria(criterions, order);
	}

	/**
	 * Identifica quais as UFs possuem atendimento pela empresa passada como parâmetro.
	 * @author Andre Valadas
	 * 
	 * @param sgPais
	 * @param tpEmpresa
	 * @return
	 */
	public List findUnidadeFederativa(String sgPais, String tpEmpresa) {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT DISTINCT id_unidade_federativa");
		query.append("        ,sg_unidade_federativa");
		query.append("        ,nm_unidade_federativa");
		query.append(" FROM unidade_federativa,");
		query.append(" 		municipio_filial,");
		query.append(" 		filial,");
		query.append(" 		empresa,");
		query.append(" 		pais");
		query.append(" WHERE unidade_federativa.id_pais = pais.id_pais");
		query.append(" 	 AND unidade_federativa.id_capital = municipio_filial.id_municipio");
		query.append(" 	 AND filial.id_filial = municipio_filial.id_filial");
		query.append(" 	 AND empresa.id_empresa = filial.id_empresa");
		query.append(" 	 AND unidade_federativa.tp_situacao = :tpSituacao");
		
		if(tpEmpresa != null){
		query.append(" 	 AND empresa.tp_empresa = :tpEmpresa");
		}
		
		query.append(" 	 AND pais.sg_pais = :sgPais");
		query.append(" ORDER BY sg_unidade_federativa");

		Map parametersValues =  new HashMap(2);
		parametersValues.put("sgPais", sgPais);
		
		if(tpEmpresa != null){
		parametersValues.put("tpEmpresa", tpEmpresa);
		}
		
		parametersValues.put("tpSituacao", ConstantesVendas.SITUACAO_ATIVO);

		ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_unidade_federativa", Hibernate.LONG);
				sqlQuery.addScalar("sg_unidade_federativa", Hibernate.STRING);
				sqlQuery.addScalar("nm_unidade_federativa", Hibernate.STRING);
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySql(query.toString(), IntegerUtils.ONE, Integer.valueOf(1000), parametersValues, configQuery).getList();
	}

	/**
	 * Busca as UnidadeFederativa com os atributos específicos para uso de Combo.
	 *@author Robson Edemar Gehl
	 * @param map
	 * @return
	 */
	public List findCombo(Map map){
		SqlTemplate sql = montarSqlCombo(map);
		return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	private SqlTemplate montarSqlCombo(Map map) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(uf.idUnidadeFederativa as idUnidadeFederativa," +
			"uf.sgUnidadeFederativa as sgUnidadeFederativa," +
			"uf.nmUnidadeFederativa as nmUnidadeFederativa, uf.sgUnidadeFederativa || ' - ' || uf.nmUnidadeFederativa as siglaDescricao," +
			"uf.tpSituacao as tpSituacao)");

		sql.addFrom(UnidadeFederativa.class.getName(),"uf");

		sql.addCriteria("uf.pais.id","=", MapUtils.getLong(MapUtils.getMap(map, "pais"), "idPais"));
		String tpSituacao = MapUtils.getString(map,"tpSituacao");
		if(StringUtils.isNotBlank(tpSituacao)) {
			sql.addCriteria("uf.tpSituacao","=",tpSituacao);
		}
		if(map.get("idPais") != null) {
			sql.addCriteria("uf.pais.id","=",map.get("idPais"));
		}

		sql.addOrderBy("uf.sgUnidadeFederativa");
		return sql;
	}	

	public List findUfBySgAndMunicipio(String sgUF,Long idMunicipio) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.alias(Projections.property("P.nmPais"),"pais_nmPais"))
		.add(Projections.alias(Projections.property("P.idPais"),"pais_idPais"))
		.add(Projections.alias(Projections.property("UF.nmUnidadeFederativa"),"nmUnidadeFederativa"))
		.add(Projections.alias(Projections.property("UF.sgUnidadeFederativa"),"sgUnidadeFederativa"))
		.add(Projections.alias(Projections.property("UF.idUnidadeFederativa"),"idUnidadeFederativa"));

		DetachedCriteria dc = DetachedCriteria.forClass(UnidadeFederativa.class,"UF")
		.setProjection(pl)
		.createAlias("UF.pais","P")
		.add(Restrictions.ilike("UF.sgUnidadeFederativa",sgUF))
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		if (idMunicipio != null) {
			dc.createAlias("UF.municipios","M")
				.add(Restrictions.eq("M.idMunicipio",idMunicipio));
		}
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<Map<String, Object>> findUfBySgAndPais(String sgUF,Long idPais) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.alias(Projections.property("P.nmPais"),"pais_nmPais"))
		.add(Projections.alias(Projections.property("P.idPais"),"pais_idPais"))
		.add(Projections.alias(Projections.property("UF.nmUnidadeFederativa"),"nmUnidadeFederativa"))
		.add(Projections.alias(Projections.property("UF.sgUnidadeFederativa"),"sgUnidadeFederativa"))
		.add(Projections.alias(Projections.property("UF.idUnidadeFederativa"),"idUnidadeFederativa"));

		DetachedCriteria dc = DetachedCriteria.forClass(UnidadeFederativa.class,"UF")
		.setProjection(pl)
		.createAlias("UF.pais","P")
		.add(Restrictions.ilike("UF.sgUnidadeFederativa",sgUF))
		.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		if (idPais != null)
			dc.add(Restrictions.eq("P.idPais", idPais));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Verifica se o intervalo de ceps recebido já não foi cadastrado.
	 * @param pais Pais
	 * @param idUnidadeFederativa idUnidadeFederativa 
	 * @param nrCepInicial Cep inicial
	 * @param nrCepFinal Cep final
	 * @return true se invtervalo válido ou false caso intervalo inválido
	 */
	public boolean validaIntervaloCep (Pais pais, Long idUnidadeFederativa, String nrCepInicial, String nrCepFinal) {
		DetachedCriteria dc = createDetachedCriteria();

		dc.setFetchMode("pais", FetchMode.JOIN);
		dc.add(	Restrictions.eq("pais.idPais", pais.getIdPais()) );
		if (idUnidadeFederativa != null) {
			dc.add(Restrictions.ne("idUnidadeFederativa", idUnidadeFederativa));
		}
		dc.add(Restrictions.or(  
				Restrictions.between("nrCepInicial", nrCepInicial, nrCepFinal), 
				Restrictions.between("nrCepFinal", nrCepInicial, nrCepFinal)
				)
		);
		dc.setProjection(Projections.count("id"));
		Integer num = (Integer)findByDetachedCriteria(dc).get(0);

		return (! (num.intValue() > 0) );
	}

	public boolean verificaIntervaloCepAtendido(String nrCepInicial, String nrCepFinal, Long idMunicipio, Long idMunicipioFilialIntervCep){
		DetachedCriteria dc = createDetachedCriteria();
		if (idMunicipioFilialIntervCep != null){
			dc.add(Restrictions.ne("idMunicipioFilialIntervCep",idMunicipioFilialIntervCep));
		}
		dc.add(Restrictions.or(
				Restrictions.isNull("dtVigenciaFinal"),Restrictions.ge("dtVigenciaFinal",new Date())));
		dc.add(Restrictions.or(
					Restrictions.and(
							Restrictions.le("nrCepInicial",nrCepInicial),
							Restrictions.ge("nrCepFinal",nrCepFinal)),
							//Restrictions.sqlRestriction("to_number(NR_CEP_INICIAL) <= ?",nrCepInicial, TypeFactory.basic("long") ),
							//Restrictions.sqlRestriction("to_number(NR_CEP_FINAL) >= ?",nrCepFinal, TypeFactory.basic("long") )),
					Restrictions.and(
							Restrictions.between("nrCepInicial",nrCepInicial,nrCepFinal),
							Restrictions.between("nrCepFinal",nrCepInicial,nrCepFinal))
							//Restrictions.sqlRestriction("to_number(NR_CEP_INICIAL) between ? and ?",ceps, types),
							//Restrictions.sqlRestriction("to_number(NR_CEP_FINAL) between ? and ?",ceps, types))
				));

		DetachedCriteria dcMunicipioFilial = dc.createCriteria("municipioFilial");
		DetachedCriteria dcMunicipio = dcMunicipioFilial.createCriteria("municipio");
		dcMunicipio.add(Restrictions.eq("idMunicipio",idMunicipio));

		return findByDetachedCriteria(dcMunicipio).size()>0;
	}

	public List<Map> findUfsAtivasByPais(Long idPais) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("u.idUnidadeFederativa"), "idUnidadeFederativa")
			.add(Projections.property("u.sgUnidadeFederativa"), "sgUnidadeFederativa")
			.add(Projections.property("u.tpSituacao"), "tpSituacao")
			.add(Projections.property("u.nmUnidadeFederativa"), "nmUnidadeFederativa");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "u")
			.setProjection(pl)
			.createAlias("u.pais", "p")
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		
		dc.add(Restrictions.eq("p.id", idPais));
		dc.add(Restrictions.eq("u.tpSituacao", "A"));
		dc.addOrder(Order.asc("u.nmUnidadeFederativa"));
		return super.findByDetachedCriteria(dc);
	}

	public List findByPais(Long idPais, String sgPais, String tpSituacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("u.idUnidadeFederativa"), "idUnidadeFederativa")
			.add(Projections.property("u.sgUnidadeFederativa"), "sgUnidadeFederativa")
			.add(Projections.property("u.tpSituacao"), "tpSituacao")
			.add(Projections.property("u.nmUnidadeFederativa"), "nmUnidadeFederativa");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "u")
			.setProjection(pl)
			.createAlias("u.pais", "p")
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		if (idPais != null) {
			dc.add(Restrictions.eq("p.id", idPais));
		}
		if (StringUtils.isNotBlank(sgPais)) {
			dc.add(Restrictions.eq("p.sgPais", sgPais));
		}
		if (StringUtils.isNotBlank(tpSituacao)) {
			dc.add(Restrictions.eq("u.tpSituacao", tpSituacao));
		}
		dc.addOrder(Order.asc("u.sgUnidadeFederativa"));
		return super.findByDetachedCriteria(dc);
	}

	public List findUfsByPais(Long idPais, String tpStituacao){
		DetachedCriteria dc = DetachedCriteria.forClass(UnidadeFederativa.class, "uf");
		dc.add(Restrictions.eq("uf.pais.id", idPais));
		if (StringUtils.isNotBlank(tpStituacao)){
			dc.add(Restrictions.eq("uf.tpSituacao", tpStituacao));	
		}
		dc.addOrder(Order.asc("uf.sgUnidadeFederativa"));
		dc.addOrder(Order.asc("uf.nmUnidadeFederativa"));
		return super.findByDetachedCriteria(dc);
	}
	
	public UnidadeFederativa findUnidadeFederativa(Long idUnidadeFederativa, Long idPais){
		DetachedCriteria dc = DetachedCriteria.forClass(UnidadeFederativa.class, "uf");
		dc.add(Restrictions.eq("uf.id", idUnidadeFederativa));
		dc.add(Restrictions.eq("uf.pais.id", idPais));
		return (UnidadeFederativa) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public List findUfBySgPaisAndSgUf(String sgPais, String uf){
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("u.idUnidadeFederativa"), "idUnidadeFederativa")
		.add(Projections.property("u.sgUnidadeFederativa"), "sgUnidadeFederativa")
		.add(Projections.property("u.tpSituacao"), "tpSituacao")
		.add(Projections.property("u.nmUnidadeFederativa"), "nmUnidadeFederativa");
	
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "u")
			.setProjection(pl)
			.createAlias("u.pais", "p")
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
	
		dc.add(Restrictions.eq("p.sgPais", sgPais));
		dc.add(Restrictions.eq("u.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));
		dc.add(Restrictions.eq("u.sgUnidadeFederativa", uf));
		
		List unidadesFederativas = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if(unidadesFederativas == null){
			return null;
		}else{
			return unidadesFederativas;
		}
	}

	/**
	 * Verifica se o Pais das Unidades Federativas são iguais.<BR>
	 *@author Robson Edemar Gehl
	 * @param idUf1
	 * @param idUf2
	 * @return true se os pais são iguais; false, diferentes.
	 */
	public boolean validateEqualsPais(Long idUf1, Long idUf2){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" count (uf1.id) ");
		sql.addFrom( getPersistentClass().getName(), "uf1" );
		sql.addFrom( getPersistentClass().getName(), "uf2 join uf1.pais p1 join uf2.pais p2" );
		sql.addCriteria("uf1.id", "=", idUf1);
		sql.addCriteria("uf2.id", "=", idUf2);
		sql.addJoin("p1.id","p2.id");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() > 0);
	}

	public Integer getRowCountIncideIss(Long idUf, Boolean incideIss) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "uf");
		dc.setProjection(Projections.count("uf.id"));
		dc.add(Restrictions.idEq(idUf));
		dc.add(Restrictions.eq("uf.blIncideIss", incideIss));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Busca a Unidade federativa pelo id do municipio sede
	 * @param idMunicipioSede Identificador do municipio
	 * @return Unidade Federativa
	 */
	public UnidadeFederativa findUFByIdMunicipio(Long idMunicipioSede) {
		SqlTemplate sql = montaQueryHqlPadrao(idMunicipioSede);
		sql.addProjection("uf");
		List ufs = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if( ufs != null && !ufs.isEmpty() ){
			return (UnidadeFederativa) ufs.get(0);
		} else {
			return null;
		} 
	}
	
	/**
	 * Monta a query de Unidades federativas sem projection e sem order by.
	 * Cada um deve implementar o seu projection e seu order by
	 * @param idMunicipio
	 * @return
	 */
	private SqlTemplate montaQueryHqlPadrao(Long idMunicipio){
		SqlTemplate sql = new SqlTemplate();
		sql.addInnerJoin(Municipio.class.getName(),"mun");
		sql.addInnerJoin("mun.unidadeFederativa","uf");
		sql.addCriteria("mun.id","=",idMunicipio);
		return sql;
	}
	
	/**
	 * Retorna a unidade federativa do endereço padrão da pessoa informada.
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/08/2006
	 * 
	 * @param Long idPessoa
	 * @return UnidadeFederativa
	 */
	public UnidadeFederativa findByIdPessoa(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("uf");
		
		hql.addInnerJoin(Pessoa.class.getName(), "pes");
		hql.addInnerJoin("pes.enderecoPessoa", "en");
		hql.addInnerJoin("en.municipio", "mun");
		hql.addInnerJoin("mun.unidadeFederativa", "uf");
		
		hql.addCriteria("pes.id", "=", idPessoa);
		
		List lstUf = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (lstUf.size() == 1) {
			return (UnidadeFederativa)lstUf.get(0);
		} else {
			return null;
		}
	}
	 
	
	//###################### *** INTEGRAÇÃO *** ################################//
	/**
	 * find para buscar a unidade federativa conforme a sigla
	 * Método solicitado pela equipe de integracao
	 * @param sgUnidadeFederativa
	 * @return UnidadeFederativa
	 */
	public UnidadeFederativa findBySgUnidadeFederativa(String sgUnidadeFederativa){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(UnidadeFederativa.class.getName() +" as UF ");

		sql.addCriteria("lower(UF.sgUnidadeFederativa)","like", sgUnidadeFederativa.toLowerCase());

		List<UnidadeFederativa> result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (result.size() == 1) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	public UnidadeFederativa findBySgUnidadeFederativaSgPais(String sgUnidadeFederativa, String sgPais){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(UnidadeFederativa.class.getName() +" as UF ");
		sql.addJoin("UF.pais", "pais");
		sql.addCriteria("lower(UF.sgUnidadeFederativa)","like", sgUnidadeFederativa.toLowerCase());
		sql.addCriteria("lower(pais.sgPais)", "=", sgPais.toLowerCase());
		return (UnidadeFederativa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Busca o identificador da unidade federativa a partir do endereco da
	 * pessoa para ser utilizado no processo de confirmação de saída de veiculo
	 * da portaria.
	 * 
	 * @author Luis Carlos Poletto
	 * @param idPessoa
	 *            identificador da pessoa usada como filtro
	 * @return identificador da unidade federativa do endereço padrão
	 */
	public Long findConfirmacaoSaida(Long idPessoa) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select uf.id from ");
		hql.append(Pessoa.class.getName());
		hql.append(" p join p.enderecoPessoa ep ");
		hql.append(" join ep.municipio m ");
		hql.append(" join m.unidadeFederativa uf ");
		hql.append(" where p.id = ? ");

		List<Long> result = getAdsmHibernateTemplate().find(hql.toString(), idPessoa);
		if (result != null && result.size() == 1) {
			return result.get(0);
		}
		return null;
	}
	
	public UnidadeFederativa findBySgUnidadeFederativaIdPais(String sgUnidadeFederativa, Long idPais){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(UnidadeFederativa.class.getName() +" as UF ");
		sql.addJoin("UF.pais", "pais");
		sql.addCriteria("lower(UF.sgUnidadeFederativa)","like", sgUnidadeFederativa.toLowerCase());
		sql.addCriteria("pais.id", "=", idPais);
		return (UnidadeFederativa) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}
	
	public List<UnidadeFederativa> findUnidadesFederativasByAereportosAtivos(){
		List<UnidadeFederativa> retorno = new ArrayList<UnidadeFederativa>();
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("situacaoAtivo", "A");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct(uf.id_unidade_federativa) as idUnidadeFederativa ");
		sql.append(", uf.sg_unidade_federativa as sgUnidadeFederativa ");
		sql.append("FROM aeroporto ae ");
		sql.append(", endereco_pessoa endp ");
		sql.append(", municipio mun ");
		sql.append(", unidade_federativa uf ");
		sql.append("WHERE ae.id_aeroporto = endp.id_pessoa ");
		sql.append("AND endp.id_municipio = mun.id_municipio ");
		sql.append("AND mun.id_unidade_federativa = uf.id_unidade_federativa ");
		sql.append("AND ae.tp_situacao = :situacaoAtivo ");
		sql.append("AND (endp.dt_vigencia_final IS NULL OR endp.dt_vigencia_final > TRUNC(SYSDATE)) ");
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idUnidadeFederativa", Hibernate.LONG);
				sqlQuery.addScalar("sgUnidadeFederativa", Hibernate.STRING);
			}
		};
		
		List<Map<String, Object>> ufs = getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, csq);
		
		if(ufs != null && !ufs.isEmpty()){
			for (Map<String, Object> map : ufs) {
				UnidadeFederativa uf = new UnidadeFederativa();
				uf.setIdUnidadeFederativa((Long) map.get("idUnidadeFederativa"));
				uf.setSgUnidadeFederativa((String) map.get("sgUnidadeFederativa"));
				retorno.add(uf);
			}
		}
		
		return retorno;
	}
	
	/**
	 * Query responsável por retornar os resultados para a suggest de unidade federativa.
	 * 
	 * @param idPais
	 * @param sgUnidadeFederativa
	 * @param nmUnidadeFederativa
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findUnidadeFederativaSuggest(Long idPais, String sgUnidadeFederativa, String nmUnidadeFederativa, Integer limiteRegistros) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		String sql = getSqlUnidadeFederativaSuggest(idPais, sgUnidadeFederativa, nmUnidadeFederativa, limiteRegistros, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql, parametersValues, getConfigureSqlQueryUnidadeFederativaSuggest());
	}
	
	/**
	 * Definição do SQL da query.
	 * 
	 * @param idPais
	 * @param sgUnidadeFederativa
	 * @param nmUnidadeFederativa
	 * @param limiteRegistros
	 * @param parametersValues
	 * 
	 * @return String
	 */
	private String getSqlUnidadeFederativaSuggest(Long idPais, String sgUnidadeFederativa, String nmUnidadeFederativa, Integer limiteRegistros, Map<String, Object> parametersValues){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT u.id_unidade_federativa AS idUnidadeFederativa, u.sg_unidade_federativa AS sgUnidadeFederativa, u.nm_unidade_federativa AS nmUnidadeFederativa");
		sql.append(" FROM unidade_federativa u");
		sql.append(" INNER JOIN pais p");
		sql.append("  ON p.id_pais = u.id_pais");
		sql.append(" WHERE u.tp_situacao = 'A'");
		
		if(limiteRegistros != null){
			sql.append(" AND rownum <= :limite");
			parametersValues.put("limite", limiteRegistros);
		}
		
		if(idPais != null){
			sql.append(" AND p.id_pais = :idPais");
			parametersValues.put("idPais", idPais);	
		}
		
		if(StringUtils.isNotBlank(sgUnidadeFederativa)){
			sql.append(" AND LOWER(u.sg_unidade_federativa) LIKE LOWER(:sgUnidadeFederativa)");
			parametersValues.put("sgUnidadeFederativa", sgUnidadeFederativa + "%");	
		}
		
		if(StringUtils.isNotBlank(nmUnidadeFederativa)){
			sql.append(" AND LOWER(u.nm_unidade_federativa) LIKE LOWER(:nmUnidadeFederativa)");
			parametersValues.put("nmUnidadeFederativa", "%" + nmUnidadeFederativa + "%");	
		}

		sql.append(" ORDER BY u.nm_unidade_federativa");
		
		return sql.toString();
	}
	
	/**
	 * Projeção da query de unidade federativa.
	 * 
	 * @return ConfigureSqlQuery
	 */
	private ConfigureSqlQuery getConfigureSqlQueryUnidadeFederativaSuggest(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("idUnidadeFederativa", Hibernate.LONG);				
				sqlQuery.addScalar("sgUnidadeFederativa", Hibernate.STRING);
				sqlQuery.addScalar("nmUnidadeFederativa", Hibernate.STRING);
			}
		};
		return csq;
	}

	public String findSgUnidadeFederativaByIdDoctoServico(Long idDoctoServico) {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT uf.sgUnidadeFederativa ");
		sql.append(" FROM " + UnidadeFederativa.class.getSimpleName() + " uf, " + DoctoServico.class.getSimpleName() + " ds, ");
		sql.append( Municipio.class.getSimpleName() + " m, " + Conhecimento.class.getSimpleName() + " c ");
		sql.append(" where uf.id = m.unidadeFederativa.id ");
		sql.append(" and m.id = c.municipioByIdMunicipioEntrega.id ");
		sql.append(" and c.id = ds.id ");
		sql.append(" and ds.idDoctoServico = ? ");

		return (String) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idDoctoServico});

	}
	
}

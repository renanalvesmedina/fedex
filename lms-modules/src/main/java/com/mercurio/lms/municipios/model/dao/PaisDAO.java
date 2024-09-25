package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PaisDAO extends BaseCrudDao<Pais, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Pais.class;
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 06/07/2006
	 * 
	 * Método descomentado: Troca de Map para TypedFlatMap evitando assim o uso da ReflectionUtils
	 * 
	 * @param criterias Critérios de pesquisa
	 * @return Lista de moedasPais
	 */	
	public List findPaisesByMoedaLookup(TypedFlatMap map){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(BaseCompareVarcharI18n.ilike("nmPais", map.getString("nmPais"), LocaleContextHolder.getLocale()));
		Long idMoeda = null;
		try {
			idMoeda = map.getLong("moedaPais.moeda.idMoeda");
			if(idMoeda != null) {
				DetachedCriteria dcMoedaPais = dc.createCriteria("moedaPais", "mp");
				DetachedCriteria dcMoeda = dcMoedaPais.createCriteria("moeda", "m");
				dcMoeda.add(Restrictions.eq("idMoeda", idMoeda ));
				dc = dcMoeda;
			}
		}catch (Exception e) {
			// Se não tiver idMoeda, faz a busca normal, sem tratamento de erro.
		}
		return findByDetachedCriteria(dc);
	}	

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("zona", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map fetchModes) {
		fetchModes.put("zona", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("zona", FetchMode.JOIN);
	}
		
	public Map findByIdPais(Long idPais) {
		Map p = null;
		DetachedCriteria dc = DetachedCriteria.forClass(Pais.class, "p")
			.setProjection(Projections
					.projectionList()
					.add(Projections.property("p.idPais"), "idPais")
					.add(Projections.property("p.nmPais"), "nmPais")
					.add(Projections.property("p.sgPais"), "sgPais"))
			.add(Restrictions.idEq(idPais))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		List l = findByDetachedCriteria(dc); 
		if(!l.isEmpty())
			p = (Map)l.get(0);
		return p;
	}

	public List findByNmPais(String nmPais, String tpSituacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(Pais.class, "p")
			.setProjection(Projections
				.projectionList()
				.add(Projections.property("p.idPais"), "idPais")
				.add(Projections.property("p.nmPais"), "nmPais"))
			.add(BaseCompareVarcharI18n.ilike("p.nmPais", nmPais.toLowerCase(), LocaleContextHolder.getLocale()))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (StringUtils.isNotBlank(tpSituacao)) {
			dc.add(Restrictions.eq("p.tpSituacao", tpSituacao));
		}
		return findByDetachedCriteria(dc);
	}

	public List findByNomeUfMunicipio(String nmPais,Long idUf,Long idMunicipio) {
		DetachedCriteria dc = DetachedCriteria.forClass(Pais.class, "P")
			.setProjection(Projections
				.projectionList()
				.add(Projections.property("P.idPais"), "idPais")
				.add(Projections.property("P.nmPais"), "nmPais"))
			.add(BaseCompareVarcharI18n.ilike("P.nmPais", nmPais.toLowerCase(), LocaleContextHolder.getLocale()))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (idUf != null) {
			dc.createAlias("P.unidadeFederativas","UF")
				.add(Restrictions.eq("UF.idUnidadeFederativa",idUf));
		}
		if (idMunicipio != null) {
			dc.createAlias("UF.municipios","M")
				.add(Restrictions.eq("M.idMunicipio",idMunicipio));
		}
		return findByDetachedCriteria(dc);
	}

	/**
	 * Retorna o pais do endereço padrão (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 22/06/2006
	 * 
	 * @param Long idPessoa
	 * @return Pais
	 */
	public Pais findByIdPessoa(Long idPessoa){
		StringBuilder hql = new StringBuilder();
		hql.append("select pa from com.mercurio.lms.configuracoes.model.Pessoa pes ");
		hql.append("inner join pes.enderecoPessoa ep ");
		hql.append("inner join ep.municipio mu ");
		hql.append("inner join mu.unidadeFederativa uf ");
		hql.append("inner join uf.pais pa ");
		hql.append("where pes.id = ? ");
		List lstPais = getAdsmHibernateTemplate().find(hql.toString(), idPessoa);
		
		if (!lstPais.isEmpty()){
			return (Pais) lstPais.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * Retorna o id do pais do endereço padrão (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/07/2006
	 * 
	 * @param Long idPessoa
	 * @return Long
	 */
	public Long findIdPaisByIdPessoa(Long idPessoa){	
		StringBuilder hql = new StringBuilder();
		hql.append("select pa.id from com.mercurio.lms.configuracoes.model.Pessoa pes ");
		hql.append("inner join pes.enderecoPessoa ep ");
		hql.append("inner join ep.municipio mu ");
		hql.append("inner join mu.unidadeFederativa uf ");
		hql.append("inner join uf.pais pa ");
		hql.append("where pes.id = ? ");
		List lstPais = getAdsmHibernateTemplate().find(hql.toString(), idPessoa);
		
		if (!lstPais.isEmpty()){
			return (Long) lstPais.get(0);
		} else {
			return null;
		}
	}	

	/**
	 * Retorna a sigla do pais do endereço padrão (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 01/09/2006
	 * 
	 * @param Long idPessoa
	 * @return String
	 */
	public String findSgPaisByIdPessoa(Long idPessoa){	
		StringBuilder hql = new StringBuilder();
		hql.append("select pa.sgPais from com.mercurio.lms.configuracoes.model.Pessoa pes ");
		hql.append("inner join pes.enderecoPessoa ep ");
		hql.append("inner join ep.municipio mu ");
		hql.append("inner join mu.unidadeFederativa uf ");
		hql.append("inner join uf.pais pa ");
		hql.append("where pes.id = ? ");
		
		List lstPais = getAdsmHibernateTemplate().find(hql.toString(), idPessoa);

		if (!lstPais.isEmpty()){
			return (String)lstPais.get(0);
		}
			return null;
		}
	
	/**
	 * Retorna o nome do pais do pais informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 27/09/2006
	 * 
	 * @param Long idPais
	 * @return String
	 */
	public String findNmPaisById(Long idPais){	
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("pa.nmPais");

		hql.addInnerJoin(Pais.class.getName(), "pa");

		hql.addCriteria("pa.id", "=", idPais);
		
		List lstPais = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (!lstPais.isEmpty()){
			return ((VarcharI18n)lstPais.get(0)).getValue();
		}
			return null;
		}
	
	public Pais findPaisByIdMunicipio(Long idMunicipio) {
		StringBuffer sb = new StringBuffer()
		.append(" select p")
		.append(" from ").append(Pais.class.getName()).append(" as p")
		.append(" join p.unidadeFederativas ufs ")
		.append(" join ufs.municipios mus ")
		.append(" where mus.id = ?");
		return (Pais) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[]{idMunicipio});
	}

	public Pais findPaisBySgPais(String sgPais) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select p");
		sb.append(" from ").append(Pais.class.getName()).append(" as p");
		sb.append(" where p.sgPais = ?");
		List result = getAdsmHibernateTemplate().find(sb.toString(), sgPais);
		if (!result.isEmpty()){
			return (Pais) result.get(0);
		}
		return null;
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.id_pais AS idPais, p.cd_iso AS cdIso, p.sg_pais AS sgPais, ");
		sql.append(PropertyVarcharI18nProjection.createProjection("p.nm_pais_i", "nmPais"));
		sql.append("  FROM pais p");
		sql.append(" WHERE p.tp_situacao = 'A' ");
		
		if(filter.get("cdIso") != null) {
			sql.append(" AND p.cd_iso = :cdIso");
		}
		
		if(filter.get("dsPais")  != null) {
			sql.append(" AND (LOWER(p.sg_pais) LIKE LOWER(:dsPais) OR LOWER(p.nm_pais_i) LIKE LOWER(:dsPais))");
			filter.put("dsPais", "%" + filter.get("dsPais") + "%");
		}
		
		if (filter.get("idZona") != null) {
			sql.append(" AND p.id_zona = :idZona ");
		}
		
		return new ResponseSuggest(sql.toString(), filter);
    }
}
package com.mercurio.lms.municipios.model.dao;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.lowerCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AeroportoDAO extends BaseCrudDao<Aeroporto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Aeroporto.class;
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {	
		lazyFindLookup.put("pessoa",FetchMode.JOIN);
		lazyFindLookup.put("pessoa.enderecoPessoas.municipio.unidadeFederativa.pais.zona",FetchMode.SELECT);
		lazyFindLookup.put("pessoa.enderecoPessoas.tipoLogradouro",FetchMode.SELECT);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map FetchModes) {
		FetchModes.put("pessoa",FetchMode.JOIN);
		FetchModes.put("pessoa.enderecoPessoas",FetchMode.SELECT);
		FetchModes.put("filial",FetchMode.JOIN);
		FetchModes.put("filial.pessoa",FetchMode.JOIN);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map FetchModes) {
		FetchModes.put("pessoa",FetchMode.JOIN);
		FetchModes.put("filial",FetchMode.JOIN);
		FetchModes.put("filial.pessoa",FetchMode.JOIN);
	}

	/**
	 * Verifica a existencia da especialização com mesmo Numero e Tipo de Identificacao, exceto a mesma.
	 * @param map
	 * @return a existência de uma especialização
	 */
	public boolean verificaExistenciaEspecializacao(Aeroporto aeroporto){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());

		dc.createAlias("pessoa", "pessoa_");
		dc.add(Restrictions.eq("pessoa_.nrIdentificacao", aeroporto.getPessoa().getNrIdentificacao()));
		dc.add(Restrictions.eq("pessoa_.tpIdentificacao", aeroporto.getPessoa().getTpIdentificacao().getValue()));
		//FIXME Aqui não poderia ser Projection.rowCount() ???
		dc.setProjection( Projections.count("pessoa_.idPessoa") );

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);	
	}

	/**
	 * findPaginated
	 * @param criteria
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	/**
	 * getRowCountCustom
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate createHqlPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		StringBuilder from = new StringBuilder()
		.append(Aeroporto.class.getName()).append(" as A ")
		.append("join fetch A.pessoa as P ")
		.append("left join fetch A.filial as F ")
		.append("left join fetch F.pessoa as F_P ");
		sql.addFrom(from.toString());
		String tpPessoa = criteria.getString("pessoa.tpPessoa");
		if (isNotBlank(tpPessoa)) {
			sql.addCriteria("P.tpPessoa", "=", tpPessoa);
		}
		
		DomainValue tpIdentificacao = criteria.getDomainValue("pessoa.tpIdentificacao");
		if (tpIdentificacao != null) {
			sql.addCriteria("P.tpIdentificacao", "=", tpIdentificacao.getValue());
		}
		
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if (isNotBlank(nrIdentificacao)) {
			sql.addCriteria("lower(P.nrIdentificacao)", "like", lowerCase(nrIdentificacao));
		}
		
		String nmPessoa = criteria.getString("pessoa.nmPessoa");
		if (isNotBlank(nmPessoa)) {
			sql.addCriteria("lower(P.nmPessoa)", "like", lowerCase(nmPessoa));
		}

		String sgAeroporto = criteria.getString("sgAeroporto");
		if (isNotBlank(sgAeroporto)) {
			sql.addCriteria("lower(A.sgAeroporto)", "like", lowerCase(sgAeroporto));
		}
		
		String cdCidade = criteria.getString("cdCidade");
		if (isNotBlank(cdCidade)) {
			sql.addCriteria("lower(A.cdCidade)", "like", lowerCase(cdCidade));
		}

		Long idFilial = criteria.getLong("filial.idFilial");
		if (idFilial != null) {
			sql.addCriteria("F.idFilial","=", idFilial);
		}

		DomainValue tpSituacao = criteria.getDomainValue("tpSituacao");
		if (tpSituacao != null) {
			sql.addCriteria("A.tpSituacao","=", tpSituacao.getValue());
		}

		sql.addOrderBy("P.nmPessoa, A.sgAeroporto, A.cdCidade, F.sgFilial");
		return sql;
	}

	/**
	 *
	 * Busca aeroportos pela situação e sigla do aeroporto
	 * O resultado da busca será composto pelos campos:
	 * - idAeroporto
	 * - sgAeroporto
	 * - idFilial
	 * - pessoa.nmPessoa
	 * @param sgAeroporto
	 * @param tpSituacao
	 * @return
	 */
	public List findLookupAeroporto(String sgAeroporto, String tpSituacao) {
		StringBuilder projection = new StringBuilder()
			.append("new Map(")
			.append("  a.idAeroporto as idAeroporto,")
			.append("  a.sgAeroporto as sgAeroporto,")
			.append("  a.filial.idFilial as idFilial,")
			.append("  a.pessoa.nmPessoa as pessoa_nmPessoa")
			.append(")");
		StringBuilder from = new StringBuilder()
			.append(getPersistentClass().getName()).append(" as a");

		SqlTemplate hql = new SqlTemplate();
		hql.addProjection(projection.toString());
		hql.addFrom(from.toString());
		/** where */
		if (isNotBlank(sgAeroporto))
			hql.addCriteria("lower(a.sgAeroporto)", "like", lowerCase(sgAeroporto));
		if (isNotBlank(tpSituacao))
			hql.addCriteria("a.tpSituacao", "=", tpSituacao);

		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()); 
		return new AliasToNestedMapResultTransformer().transformListResult(result);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um aeroporto ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isAeroporto(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(ae.id)");
		
		hql.addInnerJoin(Aeroporto.class.getName(), "ae");
		
		hql.addCriteria("ae.id", "=", idPessoa);
		hql.addCriteria("ae.tpSituacao", "=", "A");
		
		List lstAeroporto = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Long)lstAeroporto.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}
	
	//###################### *** INTEGRAÇÃO *** ################################//
	/**
	 * find para buscar o aeroporto conforme a sigla
	 * Método solicitado pela equipe de integracao
	 * @param sgAeroporto
	 * @return Aeroporto
	 */
	public Aeroporto findBySgAeroporto(String sgAeroporto){
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(Aeroporto.class.getName() +" as Aeroporto " +
						"inner join fetch Aeroporto.pessoa	as P ");

		sql.addCriteria("lower(Aeroporto.sgAeroporto)","like", sgAeroporto.toLowerCase());

		List lstAeroporto = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (lstAeroporto.size() == 1) {
			return (Aeroporto)lstAeroporto.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Filial> findByIdFilial(Long idFilial) {
		List<Object> param = new ArrayList<Object>();
		param.add(idFilial);
		
		StringBuilder sql = new StringBuilder();
		sql.append("from " + Filial.class.getSimpleName() + " as f ");
		sql.append("inner join fetch f.pessoa as p ");
		sql.append("inner join fetch f.aeroporto as a ");
		sql.append("where f.idFilial=?");
		
		return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

	public Aeroporto findBySgAeroportoAndTpSituacao(String sgAeroporto, String tpSituacao) {
		StringBuilder sql = new StringBuilder();
		sql.append("from " + Aeroporto.class.getSimpleName() + " as aereo ");
		sql.append("inner join fetch aereo.pessoa as p ");
		sql.append("where aereo.tpSituacao = :tpSituacao");
		sql.append(" and aereo.sgAeroporto = :sgAeroporto");
		
		TypedFlatMap paramValues = new TypedFlatMap();
		paramValues.put("tpSituacao", tpSituacao);
		paramValues.put("sgAeroporto", sgAeroporto);
		
		return (Aeroporto) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), paramValues);
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("select  aer.id_aeroporto as idAeroporto, aer.sg_aeroporto as sgAeroporto,  pes.nm_pessoa as nmAeroporto");
		sql.append(" from aeroporto  aer");
		sql.append(" inner join pessoa  pes on aer.id_aeroporto = pes.id_pessoa");
		sql.append(" WHERE 1 = 1 ");
		
		if(filter.get("sgAeroporto") != null) {
			sql.append(" AND LOWER(aer.sg_aeroporto) = LOWER(:sgAeroporto)");
		}
		
		if(filter.get("nmAeroporto") != null) {
			sql.append(" AND LOWER(pes.nm_pessoa) LIKE LOWER(:nmAeroporto)");
			filter.put("nmAeroporto", "%" + filter.get("nmAeroporto") + "%");
		}
		return new ResponseSuggest(sql.toString(), filter);
	}

    public Aeroporto findAeroportoAtendeCliente(Long idCliente) {
        String query = "select a from "+Cliente.class.getSimpleName()+" c "
                + " join c.filialByIdFilialAtendeOperacional.aeroporto a "
                + " join fetch a.pessoa p "
                + " where c.id = :idCliente";
        Map criteria = new TypedFlatMap();
        criteria.put("idCliente", idCliente);
        return (Aeroporto)getAdsmHibernateTemplate().findUniqueResult(query,criteria) ;
    }
}
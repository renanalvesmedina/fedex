package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.Despachante;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DespachanteDAO extends BaseCrudDao<Despachante, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Despachante.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa",FetchMode.JOIN);
	}

	public List findLookupByCriteriaWithPhone(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();  	 	   
		sql.addProjection("new Map(de.idDespachante as idDespachante, " +
							"  pe.nrIdentificacao as nrIdentificacao, " +
							"  pe.nmPessoa as nmPessoa, " +
							"  pe.idPessoa as idPessoa, " +
							"  pe.tpPessoa as tpPessoa, " +
							"  pe.tpIdentificacao as tpIdentificacao)");
		sql.addFrom(Despachante.class.getName()+" de join de.pessoa as pe");
		sql.addCriteria("pe.nrIdentificacao","=",criteria.getString("pessoa.nrIdentificacao").replace(".","").replace("/","").replace("-",""));
		sql.addOrderBy("pe.nmPessoa");			
		return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
	}

	/**
	 * Retorna 'true' se a pessoa informada é um despachante ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isDespachante(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(de.id)");

		hql.addInnerJoin(Despachante.class.getName(), "de");

		hql.addCriteria("de.id", "=", idPessoa);
		hql.addCriteria("de.tpSituacao", "=", "A");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return (result.intValue()  > 0);
	}

}
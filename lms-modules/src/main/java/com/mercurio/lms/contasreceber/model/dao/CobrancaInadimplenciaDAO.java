package com.mercurio.lms.contasreceber.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemCobranca;
import com.mercurio.lms.contasreceber.model.TratativaCobInadimplencia;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CobrancaInadimplenciaDAO extends BaseCrudDao<CobrancaInadimplencia, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CobrancaInadimplencia.class;
	}

	/**
	 * Método responsável por carregar dados páginados de acordo com os filtros passados
	 * @param criteria
	 * @return ResultSetPage contendo o resultado do hql.
	 */
	public ResultSetPage findPaginatedByCobrancaInadimplencia(TypedFlatMap criteria) throws Exception{
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

		String idUsuario = criteria.getString("usuario.idUsuario");
		String idCliente = criteria.getString("cliente.idCliente");
		Boolean blCobrancaEncerrada = criteria.getBoolean("blCobrancaEncerrada");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(
				new StringBuffer()
					.append("new Map( ci.idCobrancaInadimplencia as idCobrancaInadimplencia, ")
					.append(" u.nmFuncionario as nmUsuario, ")
					.append(" ci.blCobrancaEncerrada as blCobrancaEncerrada, ")
					.append(" p.nmPessoa as nmPessoa ) ")
					.toString()
			);

		sql.addFrom(CobrancaInadimplencia.class.getName(), "ci JOIN ci.usuario us join us.vfuncionario as u " +
				"JOIN ci.cliente as c " +
				"JOIN c.pessoa as p");

		if(StringUtils.isNotBlank(idUsuario)) {
			sql.addCriteria("ci.usuario.idUsuario", "=", Long.valueOf(idUsuario));
		}

		if(StringUtils.isNotBlank(idCliente)) {
			sql.addCriteria("c.idCliente", "=", Long.valueOf(idCliente));
		}

		if(blCobrancaEncerrada != null) {
			sql.addCriteria("ci.blCobrancaEncerrada", "=", blCobrancaEncerrada);
		}

		sql.addOrderBy("p.nmPessoa, u.nmFuncionario");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}
	
	/**
	 * Método responsável por fazer a contagem dos registros que retornam do hql.
	 * @param criteria
	 * @return Integer contendo o número de registros retornados.
	 */
	public Integer getRowCountByCobrancaInadimplencia(TypedFlatMap criteria) throws Exception{
		String idUsuario = criteria.getString("usuario.idUsuario");
		String idCliente = criteria.getString("cliente.idCliente");
		Boolean blCobrancaEncerrada = criteria.getBoolean("blCobrancaEncerrada");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(ci.idCobrancaInadimplencia)");

		sql.addFrom(CobrancaInadimplencia.class.getName(), "ci " +
				"JOIN ci.usuario.vfuncionario as f "+
				"JOIN ci.usuario as u " +
				"JOIN ci.cliente.pessoa as p " +
				"JOIN ci.cliente as c ");

		if(StringUtils.isNotBlank(idUsuario)) {
			sql.addCriteria("u.idUsuario", "=", Long.valueOf(idUsuario));
		}

		if(StringUtils.isNotBlank(idCliente)) {
			sql.addCriteria("c.idCliente", "=", Long.valueOf(idCliente));
		}

		if(blCobrancaEncerrada != null) {
			sql.addCriteria("ci.blCobrancaEncerrada", "=", blCobrancaEncerrada);
		}

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}

	public CobrancaInadimplencia findById(java.lang.Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ci");

		sql.addFrom(CobrancaInadimplencia.class.getName(), "ci JOIN FETCH ci.usuario as u " +
				"JOIN FETCH ci.cliente as c " +
				"JOIN FETCH c.pessoa as p ");

		sql.addCriteria("ci.idCobrancaInadimplencia", "=", id);

		List<CobrancaInadimplencia> list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Método responsável por buscar a cobrancaInadimplencia do itemCobranca passado por parametro
	 * 
	 * @param idItemCobranca
	 * @return CobrancaInadimplencia do itemCobranca
	 */
	public CobrancaInadimplencia findCobrancaInadimplenciaByIdItemCobranca(java.lang.Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ic");

		sql.addFrom(ItemCobranca.class.getName(), "ic JOIN FETCH ic.cobrancaInadimplencia as ci ");

		sql.addCriteria("ic.idItemCobranca", "=", id);

		List<ItemCobranca> list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(list.size() == 1) {
			return list.get(0).getCobrancaInadimplencia();
		} else {
			return null;
		}
	}

	public CobrancaInadimplencia findByIdBasic(java.lang.Long id) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ci");

		sql.addFrom(CobrancaInadimplencia.class.getName(), "ci ");

		sql.addCriteria("ci.idCobrancaInadimplencia", "=", id);

		List<CobrancaInadimplencia> list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public CobrancaInadimplencia store(CobrancaInadimplencia m, ItemList items) {
		super.store(m);
		removeFaturas(items.getRemovedItems());
		storeFaturas(items.getNewOrModifiedItems());
		return m;
	}

	public void storeFaturas(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
		getAdsmHibernateTemplate().flush();
	}

	public void removeFaturas(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}
	
	/**
	 * Método responsável por carregar dados páginados de acordo com os filtros passados
	 * @param criteria
	 * @return ResultSetPage contendo o resultado do hql.
	 */
	public List<ItemCobranca> findPaginatedByFatura(Long idPai){
		if(idPai != null && StringUtils.isNotBlank(idPai.toString())) {
			SqlTemplate sql = new SqlTemplate();

			sql.addProjection(" ic ");

			sql.addFrom(ItemCobranca.class.getName(), "ic JOIN FETCH ic.fatura as f " +
					"JOIN FETCH ic.cobrancaInadimplencia as ci " +
					"JOIN FETCH ci.cliente as cli " + 
					"JOIN FETCH cli.pessoa as pes " + 
					"JOIN FETCH f.filialByIdFilial as fil " +
					"JOIN FETCH fil.pessoa as p "
			);

			sql.addCriteria("ci.idCobrancaInadimplencia", "=", idPai);

			sql.addOrderBy("f.nrFatura");

			return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());

		} else {
			return new ArrayList<ItemCobranca>();
		}
	}
	
	public List<TratativaCobInadimplencia> findPaginatedByTratativa(Long idPai){
		if(idPai != null && StringUtils.isNotBlank(idPai.toString())) {
			SqlTemplate hql = new SqlTemplate();
			hql.addProjection(" t ");
			hql.addFrom(TratativaCobInadimplencia.class.getName(), "t" +
				" JOIN fetch t.cobrancaInadimplencia c " );
			hql.addCriteria("c.idCobrancaInadimplencia", "=", idPai);

			return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		} else {
			return new ArrayList<TratativaCobInadimplencia>();
		}
	}
	
	

	/**
	 * Método responsável por fazer a contagem dos registros que retornam do hql.
	 * @param criteria
	 * @return Integer contendo o número de registros retornados.
	 */
	public Integer getRowCountByFatura(Long idPai){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(f.idFatura)");

		sql.addFrom(ItemCobranca.class.getName(), "ic JOIN ic.fatura as f " +
				"JOIN ic.cobrancaInadimplencia as ci " +
				"JOIN f.filialByIdFilial as fil "
		);

		if(idPai != null && StringUtils.isNotBlank(idPai.toString())) {
			sql.addCriteria("ci.idCobrancaInadimplencia", "=", idPai);
		}

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}
	
	public Integer getRowCountByTratativa(Long idPai){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("count(t.idTratativaCobInadimplencia)");

		sql.addFrom(TratativaCobInadimplencia.class.getName(), "t" +
			" JOIN t.cobrancaInadimplencia c " );

		if(idPai != null && StringUtils.isNotBlank(idPai.toString())) {
			sql.addCriteria("c.idCobrancaInadimplencia", "=", idPai);
		}

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}

	public int removeByIds(List ids) {
		return super.removeByIds(ids, true);
	}	

	/**
	 * Retorna a lista de cobranca de inadimplencia que estam com blCobrancaEncerrada = 'N' e
	 * que os itens de cobrança estam TODOS liquidados
	 * 
	 * @author Mickaël Jalbert
	 * @since 14/07/2006
	 * 
	 * @return List
	 */
	public List<Long> findCobrancaNaoEncerrado(){
		SqlTemplate hqlSub = new SqlTemplate();

		hqlSub.addProjection("icob.id");

		hqlSub.addInnerJoin(ItemCobranca.class.getName(), "icob");

		hqlSub.addCustomCriteria("icob.fatura.tpSituacaoFatura NOT IN ('LI', 'CA')");
		hqlSub.addCustomCriteria("icob.cobrancaInadimplencia = cob");

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("cob");

		hql.addInnerJoin(CobrancaInadimplencia.class.getName(), "cob");
		hql.addCriteria("cob.blCobrancaEncerrada", "=", Boolean.FALSE);

		hql.addCustomCriteria("not exists ("+hqlSub.getSql()+")");

		hql.addCriteriaValue(hqlSub.getCriteria());

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());	
	}

	/**
	 * Retorna a lista de cobrança de inadimplenca aberta da fatura informada onde o
	 * id da cobrança é diferente do id de cobrança informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/12/2006
	 * 
	 * @param Long idFatura
	 * @param Long idCobrancaInadimplenca
	 * @return List
	 */
	public List<CobrancaInadimplencia> findByIdFatura(Long idFatura, Long idCobrancaInadimplenca){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("ci");

		sql.addInnerJoin(Fatura.class.getName(), "fat");
		sql.addInnerJoin("fat.itemCobrancas", "ic");
		sql.addInnerJoin("ic.cobrancaInadimplencia", "ci");
		sql.addInnerJoin("fetch ci.usuario", "us");

		sql.addCriteria("fat.id", "=", idFatura);

		sql.addCriteria("ci.id", "!=", idCobrancaInadimplenca);

		return getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
	}	

	/**
	 * Valida se o usuário pode efetuar alguma ação(excluir, editar etc...) nas cobrancas de inadimplência 
	 * 
	 * RETURN TRUE -> O usuário pode realizar ações sobre as cobrancas de inadimplencia
	 * RETURN FALSE -> O usuário não pode realizar ações sobre alguma das cobrancas de inadimplencia
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 20/12/2006
	 *
	 * @param idsCobrancaInadimplencia
	 * @return
	 *
	 */
	public Boolean validateCobrancaInadimplencia(List<Long> idsCobrancaInadimplencia){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" COUNT(ci) ");

		hql.addFrom(CobrancaInadimplencia.class.getName(), " ci JOIN ci.itemCobrancas as ic " +
				"JOIN ic.fatura as f " +
				"JOIN f.filialByIdFilialCobradora as fil "
		);

		/** Filtra pela filial do usuário logado */
		hql.addCriteria("fil.idFilial", "<>", SessionUtils.getFilialSessao().getIdFilial());

		/** Filtra pelos ids de cobranca de inadimplência */
		SQLUtils.joinExpressionsWithComma(idsCobrancaInadimplencia, hql, "ci.idCobrancaInadimplencia");

		Long nrCobrancas = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());

		return (nrCobrancas.intValue() == 0);
	}
	
	
	/**
	 * Calcula Percentual Juro Diário
	 * 
	 */
	public BigDecimal buscaCotacaoIndFinanceiro(YearMonthDay dtEmicaoFatura, Long idPais){
		
		Projection pj = Projections.property("cot.vlCotacaoIndFinanceiro");
		
		DetachedCriteria dc = DetachedCriteria.forClass(CotacaoIndicadorFinanceiro.class,"cot")
		.setProjection(pj)
		.createAlias("cot.indicadorFinanceiro", "ind")
		.createAlias("ind.pais", "pa")
		.add(Restrictions.eq("ind.sgIndicadorFinanceiro", "JURODIA"))
		.add(Restrictions.eq("pa.idPais", idPais))
		.add(Restrictions.le("cot.dtCotacaoIndFinanceiro", dtEmicaoFatura) )
		.addOrder(Order.desc("cot.dtCotacaoIndFinanceiro"));
		
		return (BigDecimal) getAdsmHibernateTemplate().findByCriteria(dc).get(0);
	}
	
	
	public CobrancaInadimplencia findCobrancaInadimplenciaByIdBlEncerrada(Long idCobrancaInadimplencia, Boolean blCobrancaEncerrada){
		Criteria c = getSession().createCriteria(getPersistentClass());
		c.add(Restrictions.eq("idCobrancaInadimplencia", idCobrancaInadimplencia));
		c.add(Restrictions.eq("blCobrancaEncerrada", blCobrancaEncerrada));
		return (CobrancaInadimplencia) c.uniqueResult();	
}
}

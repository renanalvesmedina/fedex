package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.ProdutoCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProdutoClienteDAO extends BaseCrudDao<ProdutoCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ProdutoCliente.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("produto", FetchMode.JOIN);
		lazyFindPaginated.put("embalagem", FetchMode.JOIN);
		lazyFindPaginated.put("moeda",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("produto",FetchMode.JOIN);
		lazyFindById.put("embalagem",FetchMode.JOIN);
		lazyFindById.put("cliente",FetchMode.JOIN);
		lazyFindById.put("moeda",FetchMode.JOIN);
	}

	/**
	 * Pesquisa de Produtos Clientes com left join para embalagem que pode ser nula.
	 * @param criteria Critérios da pesquisa
	 * @param findDef Classe que contém informações de paginação
	 * @return ResultSetPage Objeto contendo a lista retorno da pesquisa e as informações de paginação 
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(" pc ");

		StringBuffer joins = new StringBuffer()
			.append(" inner join fetch pc.cliente c ")
			.append(" inner join fetch pc.produto p ")
			.append(" left outer join fetch pc.moeda m")
			.append(" left outer join fetch pc.embalagem e");

		sql.addFrom( ProdutoCliente.class.getName() + " pc " + joins.toString() );

		sql.addCriteria("c.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "cliente"), "idCliente"));
		sql.addCriteria("p.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "produto"), "idProduto"));
		sql.addCriteria("e.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "embalagem"), "idEmbalagem"));

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("p.dsProduto", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("e.dsEmbalagem", LocaleContextHolder.getLocale()));
		sql.addOrderBy("pc.dsTipoClassificacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		return rsp;
	}

	/**
	 * Retorna a lista de ProdutoCliente através do idCliente
	 * @param idCliente
	 * @return
	 */
	public List<ProdutoCliente> findByIdCliente(Long idCliente) {
		DetachedCriteria dc = createDetachedCriteria()
		.setFetchMode("moeda", FetchMode.JOIN)
		.add(Restrictions.eq("cliente.id", idCliente));

		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
}
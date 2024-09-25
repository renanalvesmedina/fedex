package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.vendas.model.DivisaoProduto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DivisaoProdutoDAO extends BaseCrudDao<DivisaoProduto, Long>
{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DivisaoProduto.class;
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("produto", FetchMode.JOIN);
		map.put("naturezaProduto", FetchMode.JOIN);
		map.put("moeda", FetchMode.JOIN);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new map(dp.idDivisaoProduto as idDivisaoProduto, p.dsProduto as produto_dsProduto, " +
				"np.dsNaturezaProduto as naturezaProduto_dsNaturezaProduto, " +
				"m.dsSimbolo as moeda_dsSimbolo, m.sgMoeda as moeda_sgMoeda, dp.vlMedioKg as vlMedioKg)");
		sql.addFrom(DivisaoProduto.class.getName() + " as dp join dp.naturezaProduto as np " +
				"left join dp.produto as p left join dp.moeda as m");
		sql.addCriteria("np.id", "=", criteria.getLong("naturezaProduto.idNaturezaProduto"), Long.class);
		sql.addCriteria("dp.divisaoCliente.id", "=", criteria.getLong("divisaoCliente.idDivisaoCliente"), Long.class);
		sql.addCriteria("p.id", "=", criteria.getLong("produto.idProduto"), Long.class);
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("p.dsProduto", LocaleContextHolder.getLocale()));
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(),
				findDef.getPageSize(), sql.getCriteria());
		return AliasToTypedFlatMapResultTransformer.getInstance().transformResultSetPage(rsp);
	}
	
	
}
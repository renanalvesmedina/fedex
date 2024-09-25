package com.mercurio.lms.tabelaprecos.model.dao;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SubtipoTabelaPrecoDAO extends BaseCrudDao<SubtipoTabelaPreco, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SubtipoTabelaPreco.class;
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param tpSubtipoTabelaPreco
	 * @return SubtipoTabelaPreco
	 */
	public SubtipoTabelaPreco findByTpSubtipoTabelaPreco(String tpSubtipoTabelaPreco) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));
		return (SubtipoTabelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca os subtipos de tabela de preco (idSubtipoTabelaPreco e
	 * tpSubtipoTabelaPreco), restringindo pelo tipo da tabela de preco. Caso
	 * seja uma tabela FOB (<code>blTabelaFob == Boolean.TRUE</code>) busca
	 * apenas os subtipos 'F', caso não seja uma tabela FOB (<code>blTabelaFob 
	 * == Boolean.FALSE</code>)
	 * busca todos os subtipos que não são 'F', caso o parametro não seja
	 * recebido (<code>blTabelaFob == null</code>) não restringe quanto ao
	 * subtipo da tabela de preço.
	 * 
	 * @param tpTipoTabelaPreco
	 * @param blTabelaFob
	 * @return
	 */
	public List findByTpTipoTabelaPreco(String tpTipoTabelaPreco, Boolean blTabelaFob) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("stp.idSubtipoTabelaPreco"), "idSubtipoTabelaPreco")
			.add(Projections.property("stp.tpSubtipoTabelaPreco"), "tpSubtipoTabelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "stp");
		dc.setProjection(pl);
		if (FALSE.equals(blTabelaFob)) {
			dc.add(Restrictions.ne("stp.tpSubtipoTabelaPreco", "F"));
		} else if (TRUE.equals(blTabelaFob)) {
			dc.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", "F"));
		}
		dc.addOrder(Order.asc("stp.tpSubtipoTabelaPreco"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List findByTipoSelecionadoOuTipoNulo(String tpTipoTabelaPreco,boolean onlyActiveValues) {
		StringBuilder sql = new StringBuilder();

		sql.append(" select new map( ");
		sql.append("          sttp.idSubtipoTabelaPreco as idSubtipoTabelaPreco, ");
		sql.append("          sttp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco, ");
		sql.append("          sttp.tpTipoTabelaPreco as tpTipoTabelaPreco, ");
		sql.append("          sttp.tpSituacao as tpSituacao ");
		sql.append("        ) ");
		sql.append("   from ").append(SubtipoTabelaPreco.class.getName()).append(" as sttp ");
		sql.append("  where (sttp.tpTipoTabelaPreco=:tpTipoTabelaPreco OR sttp.tpTipoTabelaPreco is NULL) ");
		if(onlyActiveValues) {
			sql.append("    and sttp.tpSituacao = 'A'");
		}

		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),"tpTipoTabelaPreco", tpTipoTabelaPreco);
	}

}
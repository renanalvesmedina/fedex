package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TabelaPrecoParcelaDAO extends BaseCrudDao<TabelaPrecoParcela, Long>{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TabelaPrecoParcela.class;
    }

   protected void initFindByIdLazyProperties(Map lazyFindById) {
	   lazyFindById.put("parcelaPreco", FetchMode.JOIN);
   }
   
   protected void initFindPaginatedLazyProperties(Map lazyFindById) {
	   lazyFindById.put("parcelaPreco", FetchMode.JOIN);
   }

	public List findListByCriteria(Map criterions) {
		if (criterions == null) criterions = new HashMap(1);
		List order = new ArrayList(1);
		order.add("parcelaPreco_.nmParcelaPreco");
		return super.findListByCriteria(criterions, order);
	}

	public ResultSetPage findPaginatedByIdTabelaPreco(TypedFlatMap criteria) {
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tpp.idTabelaPrecoParcela"), "idTabelaPrecoParcela")
			.add(Projections.property("pp.nmParcelaPreco"), "parcelaPreco.nmParcelaPreco")
			.add(Projections.property("pp.tpParcelaPreco"), "parcelaPreco.tpParcelaPreco");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tpp")
			.createAlias("tpp.parcelaPreco", "pp")
			.setProjection(pl)
			.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);	
		return findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

   
	public ParcelaPreco findParcelaByIdServicoAdicional(Long idTabelaPreco, Long idServicoAdicional) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("pp.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "nmParcelaPreco")
			.add(Projections.property("pp.cdParcelaPreco"), "cdParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tpp");
		dc.setProjection(pl);

		dc.createAlias("tpp.parcelaPreco", "pp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("pp.servicoAdicional.id", idServicoAdicional));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));

		List result = findByDetachedCriteria(dc);
		if(!result.isEmpty())
			return (ParcelaPreco) result.get(0);
		return null;
	}
	
	/**
	 * Remove as parcelas da tabela de preço e todas as suas dependências para execução dos reajustes 
	 * 
	 * @param idTabelaPreco
	 */
	public void removeByTabelaPreco(Long idTabelaPreco){
		String hql;
		
		//PrecoFrete
		hql = "delete from PrecoFrete as pf " +
				"where pf.idPrecoFrete in(" +
					"select pf.idPrecoFrete from PrecoFrete pf where pf.tabelaPrecoParcela.tabelaPreco.idTabelaPreco = :id"+ 
				")";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);
		
		//ValorTaxa
		hql = "delete from ValorTaxa as vt " +
				"where vt.idValorTaxa in(" +
				"	select vt.idValorTaxa from ValorTaxa vt where vt.tabelaPrecoParcela.tabelaPreco.idTabelaPreco = :id" +
				")";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);
		
		//ValorServicoAdicional
		hql = "delete from ValorServicoAdicional as vsa " +
				"where vsa.idValorServicoAdicional in(" +
				"	select vsa.idValorServicoAdicional from ValorServicoAdicional vsa where vsa.tabelaPrecoParcela.tabelaPreco.idTabelaPreco = :id" +
				")";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);
		
		//ValorFaixaProgressiva - dependência de Faixa Progressiva
		hql = "delete from ValorFaixaProgressiva as vfp " +
				"where vfp.idValorFaixaProgressiva in(" +
				"	select vfp.idValorFaixaProgressiva from ValorFaixaProgressiva vfp where vfp.faixaProgressiva.tabelaPrecoParcela.tabelaPreco.idTabelaPreco = :id" +
				")";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);
		
		//FaixaProgressiva
		hql = "delete from FaixaProgressiva as fp " +
				"where fp.idFaixaProgressiva in(" +
				"	select fp.idFaixaProgressiva from FaixaProgressiva fp where fp.tabelaPrecoParcela.tabelaPreco.idTabelaPreco = :id" +
				")";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);
		
		//TabelaPrecoParcela - ATENÇÃO!!! deve ficar por último por causa das dependências
		hql = "delete TabelaPrecoParcela t where t.tabelaPreco.idTabelaPreco = :id";
		getAdsmHibernateTemplate().removeById(hql, idTabelaPreco);		
	}
	
	public java.io.Serializable store(TabelaPrecoParcela tabelaPrecoParcela){
		super.store(tabelaPrecoParcela);
		if (tabelaPrecoParcela.getFaixaProgressivas() != null){
			getHibernateTemplate().saveOrUpdateAll(tabelaPrecoParcela.getFaixaProgressivas());
		}		
		if (tabelaPrecoParcela.getPrecoFretes() != null){
			getHibernateTemplate().saveOrUpdateAll(tabelaPrecoParcela.getPrecoFretes());	
		}
		return tabelaPrecoParcela.getIdTabelaPrecoParcela();
	}
	
	public List findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco(String tpParcelaPreco, String tpPrecificacao, Long idTabelaPreco) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("tpp.idTabelaPrecoParcela"), "idTabelaPrecoParcela")
			.add(Projections.property("pp.idParcelaPreco"), "idParcelaPreco")
			.add(Projections.property("pp.nmParcelaPreco"), "nmParcelaPreco")
			.add(Projections.property("pp.cdParcelaPreco"), "cdParcelaPreco");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tpp")
			.setProjection(pl)
			.createAlias("tpp.tabelaPreco", "tp")
			.createAlias("tpp.parcelaPreco", "pp")
			.add(Restrictions.eq("pp.tpParcelaPreco", tpParcelaPreco))
			.add(Restrictions.eq("pp.tpPrecificacao", tpPrecificacao))
			.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco))
			.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List findParcelasPrecoByIdTabelaPreco(Long idTabelaPreco, String tpPrecificacao){
		StringBuilder hql = new StringBuilder()
		.append(" select tpp \n")
		.append("  from ").append(getPersistentClass().getName()).append(" as tpp \n")
		.append("  join tpp.tabelaPreco as tp \n")
		.append("  join tpp.parcelaPreco as pp \n")
		.append(" where	tp.idTabelaPreco = ? \n");
		if (StringUtils.isNotBlank(tpPrecificacao)) {
			hql.append("   and pp.tpPrecificacao = '"+tpPrecificacao+"' \n");
		}
		
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idTabelaPreco});
	}
	
	public Long findByIdTabelaPrecoIdParcelaPreco(Long idTabelaPreco, Long idParcelaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tpp")
			.setProjection(Projections.property("tpp.idTabelaPrecoParcela"))
			.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco))
			.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		
		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && result.size() > 0) {
			return (Long) result.get(0);
		}
		return null;
	}
	
	/**
	 * Busca todas as TabelaPrecoParcela relacionada a tabela de preco recebida
	 * e que suas parcelas de preco possuam codigo igual ao codigo recebido.
	 * 
	 * @param idTabelaPreco id da tabela a ser pesquisada
	 * @param cdParcelaPreco codigo da parcela de filtro
	 * @return lista das parcelas de preco da tabela
	 */
	public List findByIdTabelaPrecoCdParcelaPreco(Long idTabelaPreco, String cdParcelaPreco) {
		StringBuilder hql = new StringBuilder()
			.append(" select tpp \n")
			.append("   from ").append(getPersistentClass().getName()).append(" tpp \n")
			.append("   join tpp.parcelaPreco pp \n")
			.append("   join tpp.tabelaPreco tp \n")
			.append("  where pp.cdParcelaPreco = ? \n")
			.append("    and tp.idTabelaPreco = ? ");
		
		Object[] values = new Object[] {cdParcelaPreco, idTabelaPreco};
		return getAdsmHibernateTemplate().find(hql.toString(), values);
	}
	
	/**
	 * Busca todas as TabelaPrecoParcela relacionadas a tabela de preco e que
	 * nao estejam relacionadas com nenhuma das parcelas de preco fornecidas.
	 * 
	 * @param idTabelaPreco id da tabela a ser pesquisada
	 * @param idsParcelaPrecos ids de exceções de parcelas de preco
	 * @return lista das parcelas encontradas
	 */
	public List findByIdTabelaPrecoNotParcelaPreco(Long idTabelaPreco, List idsParcelaPrecos) {
		StringBuilder hql = new StringBuilder()
		.append("   from ").append(getPersistentClass().getName()).append(" tpp \n")
		.append("  where tpp.tabelaPreco.id = :id \n");
		
		String[] paramNames = new String[] {"id"};
		Object[] paramValues = new Object[] {idTabelaPreco};
		
		if (idsParcelaPrecos != null && idsParcelaPrecos.size() > 0) {
			hql.append("    and tpp.parcelaPreco.id not in (:parcelas) ");
			paramNames = new String[] {"id", "parcelas"};
			paramValues = new Object[] {idTabelaPreco, idsParcelaPrecos};
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramNames, paramValues);
	}

	/**
	 * Contabiliza as tabelaPreco efetuadas pelos ids de Parcelas  
	 * 
	 * @param idsParcela
	 * @return
	 */
	public Integer getCountTabelaPrecoEfetivadas(List idsParcela){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "tpp")
		.createAlias("tpp.tabelaPreco", "tpptp")
		.setProjection(Projections.rowCount())
		.add(Restrictions.in("tpp.id", idsParcela))
		.add(Restrictions.eq("tpptp.blEfetivada", Boolean.TRUE));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	/**
	 * Busca todas as parcelas de preco que estao relacionadas com a tabela
	 * antiga e não estao relacionadas com a tabela nova.
	 * 
	 * @param idTabelaPrecoNova tabela nova
	 * @param idTabelaPrecoAntiga tabela antiga
	 * @return lista de parcelas de preco
	 */
	public List findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(Long idTabelaPrecoNova, Long idTabelaPrecoAntiga) {
		StringBuilder hql = new StringBuilder();
		hql.append("select tpp.parcelaPreco.id \n");
		hql.append("  from ").append(getPersistentClass().getName()).append(" tpp \n");
		hql.append(" where tpp.parcelaPreco.id not in ( \n");
		hql.append("       select tppi.parcelaPreco.id \n");
		hql.append("         from ").append(getPersistentClass().getName()).append(" tppi \n");
		hql.append("        where tppi.tabelaPreco.id = ?) \n"); // nova
		hql.append("   and tpp.tabelaPreco.id = ? \n"); // antiga
		
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {idTabelaPrecoNova, idTabelaPrecoAntiga});
	}

}
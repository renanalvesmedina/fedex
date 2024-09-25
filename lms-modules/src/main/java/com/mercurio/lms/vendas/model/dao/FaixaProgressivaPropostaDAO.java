package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.FaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;

public class FaixaProgressivaPropostaDAO extends BaseCrudDao<FaixaProgressivaProposta, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FaixaProgressivaProposta.class;
	}
	
	public BigDecimal findVlFaixaProgressivaByIdProdutoEspecifico(Long idParametroCliente, Long idProdutoEspecifico, Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressivaProposta.class, "vfpp");
		dc.createAlias("vfpp.parametroCliente", "pc");
		dc.createAlias("vfpp.faixaProgressivaProposta", "fpp");
		dc.createAlias("fpp.produtoEspecifico", "pe");
		dc.createAlias("fpp.simulacao", "s");
		dc.add(Restrictions.eq("pc.id", idParametroCliente));
		dc.add(Restrictions.eq("pe.id", idProdutoEspecifico));
		dc.add(Restrictions.eq("s.id", idSimulacao));

		List valores = findByDetachedCriteria(dc);
		if(valores.size() == 1) {
			return ((ValorFaixaProgressivaProposta)valores.get(0)).getVlFixo();
		}
		return null;
	}

	public BigDecimal findVlFaixaProgressivaProposta(Long idParametroCliente, Long idSimulacao, BigDecimal psReferencia) {

		DetachedCriteria fppMinAux = DetachedCriteria.forClass(FaixaProgressivaProposta.class, "fpp");
		fppMinAux.setProjection(Projections.min("fpp.vlFaixa"));
		fppMinAux.createAlias("fpp.simulacao", "s");
		fppMinAux.add(Restrictions.ge("fpp.vlFaixa", psReferencia));
		fppMinAux.add(Restrictions.eq("s.id", idSimulacao));
		
		DetachedCriteria fppMin = DetachedCriteria.forClass(FaixaProgressivaProposta.class, "fpp2");
		fppMin.setProjection(Projections.min("fpp2.id"));
		fppMin.createAlias("fpp2.simulacao", "s");
		fppMin.add(Restrictions.eq("s.id", idSimulacao));
		fppMin.add(Property.forName("fpp2.vlFaixa").eq(fppMinAux));

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressivaProposta.class, "vfpp");
		dc.createAlias("vfpp.parametroCliente", "pc");
		dc.createAlias("vfpp.faixaProgressivaProposta", "fpp");
		dc.add(Restrictions.eq("pc.id", idParametroCliente));
		dc.add(Property.forName("fpp.id").eq(fppMin));

		List valores = findByDetachedCriteria(dc);
		if(valores.size() == 1) {
			return ((ValorFaixaProgressivaProposta)valores.get(0)).getVlFixo();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map> findByIdSimulacao(Long idSimulacao) {
		StringBuilder hql = new StringBuilder();
		hql.append("select new Map(fpp.vlFaixa as vlFaixa, fpp.produtoEspecifico.id as idProdutoEspecifico, fpp.idFaixaProgressivaProposta as idFaixaProgressivaProposta) ");
		hql.append("from ");
		hql.append(getPersistentClass().getName()).append(" fpp ");
		hql.append("where ");
		hql.append("fpp.simulacao.id = ?");

		List param = new ArrayList();
		param.add(idSimulacao);

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

	@SuppressWarnings("rawtypes")
	public void removeByIdsNaoUtilizados(List<Long> idsFaixaProgressivaProposta) {
	    if (idsFaixaProgressivaProposta != null && idsFaixaProgressivaProposta.size() > 0){
	        StringBuilder hql = new StringBuilder();
	        hql.append(" DELETE FROM ").append(getPersistentClass().getName()).append(" fpp ");
	        hql.append(" WHERE fpp.id IN (");
	        
	        for (Iterator<Long> iterator = idsFaixaProgressivaProposta.iterator(); iterator.hasNext();) {
	            hql.append(iterator.next());
	            
	            if(iterator.hasNext()){
	                hql.append(",");
	            }
	        }
	        hql.append(") ");

	        hql.append("AND NOT EXISTS( SELECT 1 FROM ");
	        hql.append(ValorFaixaProgressivaProposta.class.getName()).append(" vfpp ");
	        hql.append("WHERE ");
	        hql.append("vfpp.faixaProgressivaProposta.id = fpp.id ");
	        hql.append(") ");
	        
	        super.executeHql(hql.toString(), new ArrayList());
	    }
	}
}
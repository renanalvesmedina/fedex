package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemLoteSerasa;
import com.mercurio.lms.contasreceber.model.LoteSerasa;
import com.mercurio.lms.util.FormatUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LoteSerasaDAO extends BaseCrudDao<LoteSerasa, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LoteSerasa.class;
    }

 
    /**
     * Faz consulta paginada 
     * 
     * @param criteria
     * @param findDef
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	DetachedCriteria dc = montaDetachedCriteria(criteria);
        ResultSetPage rsp = super.findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
        
        List listaRetorno = new ArrayList();
        for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
            LoteSerasa loteSerasa = (LoteSerasa) iter.next();
			
			Map map = new HashMap();
			
			map.put("idLoteSerasa", loteSerasa.getIdLoteSerasa());
			map.put("nrLote", loteSerasa.getNrLote());
			map.put("dsLote", loteSerasa.getDsLote());
			map.put("tpLote", loteSerasa.getTpLote());
			map.put("dhGeracao", loteSerasa.getDhGeracao());
			if (loteSerasa.getArquivoGerado() != null) {
				map.put("arquivoGerado",Base64Util.encode(loteSerasa.getArquivoGerado()));	
			} else {
				map.put("arquivoGerado",loteSerasa.getArquivoGerado());
			}
			
			
			listaRetorno.add(map);
        }
        
        rsp.setList(listaRetorno);
        return rsp;
    }

    /**
     * Prepara o detachedCriteria
     * 
     * @param criteria
     * @return
     */
	private DetachedCriteria montaDetachedCriteria(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	
    	Long nrLote = criteria.getLong("nrLote");
    	DomainValue tpLote = criteria.getDomainValue("tpLote");
    	String dsLote = criteria.getString("dsLote") ;
    	Boolean blLotesNaoGerados = criteria.getBoolean("blLotesNaoGerados");
    	Long idFatura = criteria.getLong("idFatura");
    	
    	DateTime dhGeracaoInicial = criteria.getDateTime("dhGeracaoInicial");
    	DateTime dhGeracaoFinal = criteria.getDateTime("dhGeracaoFinal");
    	
    	if (nrLote != null) {
    		dc.add(Restrictions.eq("nrLote", nrLote));	
    	}
    	if (dhGeracaoInicial != null) {
    		YearMonthDay dtIni = dhGeracaoInicial.toYearMonthDay();
    		dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_GERACAO AS DATE)) >= ?", dtIni,  Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
    	}
    	if (dhGeracaoFinal != null) {
    		YearMonthDay dtFim = dhGeracaoFinal.toYearMonthDay();
    		dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_GERACAO AS DATE)) <= ?", dtFim,  Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
    	}
    	if (tpLote != null) {
    		dc.add(Restrictions.eq("tpLote", tpLote));	
    	}
    	if (dsLote != null) {
    		dc.add(Restrictions.like("dsLote", dsLote));	
    	}
    	if (blLotesNaoGerados) {
    		dc.add(Restrictions.sqlRestriction("{alias}.DH_GERACAO is null "));
    	} else {
    		dc.add(Restrictions.sqlRestriction("{alias}.DH_GERACAO is not null "));
    	}
    		
    	if (idFatura != null) {
    		dc.add( Subqueries.exists(
    			      DetachedCriteria.forClass(ItemLoteSerasa.class, "i")
    			        .setProjection(Projections.id())
    			        .add(Restrictions.eq("i.fatura.idFatura",idFatura))));
    			      
    				
    	}
    	
    	dc.addOrder(Order.desc(("nrLote")));
    	
		return dc;
	}
    
    /**
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCount(TypedFlatMap criteria) {
    	DetachedCriteria dc = montaDetachedCriteria(criteria);
    	dc.setProjection(Projections.rowCount());
    
		return (Integer) dc.getExecutableCriteria(getSession()).uniqueResult();
    }
   
    /**
	 * Retorna uma lista de itens com as faturas de determinado lote enviado ao Serasa
	 * 
	 * @param idLoteSerasa
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ItemLoteSerasa> findItemLoteSerasaByIdLoteSerasa(Long idLoteSerasa) {
		Criteria crit = getSession().createCriteria(ItemLoteSerasa.class);
		crit.createAlias("fatura", "f");
		crit.createAlias("f.cliente", "c");
		crit.createAlias("f.moeda", "m");
		crit.createAlias("f.filialByIdFilial", "fl");
		crit.createAlias("c.pessoa", "p");
		
		crit.add(Restrictions.eq("loteSerasa.idLoteSerasa", idLoteSerasa));
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("idItemLoteSerasa").as("idItemLoteSerasa"));
		pl.add(Projections.property("loteSerasa").as("loteSerasa"));
		pl.add(Projections.property("fatura").as("fatura"));
		pl.add(Projections.property("f.nrFatura").as("nrFatura"));
		pl.add(Projections.property("p.nmPessoa").as("nmPessoa"));
		pl.add(Projections.property("f.dtEmissao").as("dtEmissao"));
		pl.add(Projections.property("f.dtVencimento").as("dtVencimento"));
		pl.add(Projections.property("f.tpSituacaoFatura").as("tpSituacaoFatura"));
		pl.add(Projections.property("m.sgMoeda").as("sgMoeda"));
		pl.add(Projections.property("m.dsSimbolo").as("dsSimbolo"));
		pl.add(Projections.property("p.tpIdentificacao").as("tpIdentificacao"));
		pl.add(Projections.property("p.nrIdentificacao").as("nrIdentificacao"));
		pl.add(Projections.property("fl.sgFilial").as("sgFilial"));

		crit.setProjection(pl);
		
		ResultTransformer resultTransformer = Transformers.aliasToBean(ItemLoteSerasa.class);
		crit.setResultTransformer(resultTransformer);
		
		List<ItemLoteSerasa> lista = crit.list();
		
		return lista;
	}

	/**
	 * Quantidade de itens de um lote enviado ao Serasa
	 * 
	 * @param masterId
	 * @return
	 */
	public Integer getRowCountItemLoteSerasa(Long idLoteSerasa) {
		Criteria crit = getSession().createCriteria(ItemLoteSerasa.class);
		crit.add(Restrictions.eq("loteSerasa.idLoteSerasa", idLoteSerasa));
		crit.setProjection(Projections.rowCount());
		return (Integer) crit.uniqueResult();
	}


	/**
	 * Salva o mestre (LoteSerasa) e os detalhes (ItemLoteSerasa -> Fatura )
	 * 
	 * @param loteSerasa
	 * @param itemList
	 * @return
	 */
	public LoteSerasa store(final LoteSerasa loteSerasa, final ItemList itemList) {
		
		super.store(loteSerasa, true);
		
		if (itemList.getRemovedItems().size() > 0) {
			getAdsmHibernateTemplate().deleteAll(itemList.getRemovedItems());
		}
		
		if (itemList.getNewOrModifiedItems().size() > 0) {
			super.store(itemList.getNewOrModifiedItems(), true);
		}
		
		return loteSerasa;
	}
	
	public LoteSerasa store(final LoteSerasa loteSerasa) {
		super.store(loteSerasa, true);
		return loteSerasa;
	}

	/**
	 * Cria RestrictionsBuilder para a pesquisa 
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private RestrictionsBuilder createRestrictionsBuilderLoteSerasa(Map parameters) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setFetchMode("itemLoteSerasa", FetchMode.JOIN);
		dc.createAlias("itemLoteSerasa.fatura", "f");

		RestrictionsBuilder rb = new RestrictionsBuilder(LoteSerasa.class, true);

		Long idFatura = (Long) parameters.get("idFatura");
		Boolean blLotesNaoGerados = (Boolean) parameters.get("blLotesNaoGerados");

		parameters.remove("idFatura");
		parameters.remove("blLotesNaoGerados");

		rb.createDefaultBuilders(parameters);

		if (blLotesNaoGerados != null && blLotesNaoGerados) {
			dc.add(Restrictions.isNotNull("dhGeracao"));
		}
		if (idFatura != null) {
			dc.add(Restrictions.eq("f.idFatura", idFatura));
		}
		
		return rb;
	}

	@Override
	public void initFindListLazyProperties(Map lazyFindList) {
		super.initFindListLazyProperties(lazyFindList);
	}
	
	/**
	 * Localiza LoteSerasa pelo id
	 * 
	 */
	public LoteSerasa findById(Long idLoteSerasa) {
		Criteria crit = getSession().createCriteria(LoteSerasa.class);
		
		crit.add(Restrictions.eq("idLoteSerasa", idLoteSerasa));
		
		LoteSerasa loteSerasa;
		try {
			loteSerasa = (LoteSerasa)crit.uniqueResult();
		} catch (Exception e) {
			loteSerasa = null;
		}
		return loteSerasa; 
	}
	
	/**
	 * Remove LoteSerasa e ItemLoteSerasa pelo id
	 */
	@Override
	public void removeById(Long id) {
		String hql = "delete from " + ItemLoteSerasa.class.getName() + " where loteSerasa.idLoteSerasa = :id";
		getAdsmHibernateTemplate().removeById(hql, id);
		
		hql = "delete from " + LoteSerasa.class.getName() + " where idLoteSerasa = :id";
		getAdsmHibernateTemplate().removeById(hql, id);
	}
	
	/**
	 * Remove lista de LoteSerasa e ItemLoteSerasa pelo id
	 */
	@Override
	public int removeByIds(List<Long> ids) {
		String hql = "delete from " + ItemLoteSerasa.class.getName() + " where loteSerasa.idLoteSerasa in (:id) ";
		getAdsmHibernateTemplate().removeByIds(hql, ids);
		
		hql = "delete from " + LoteSerasa.class.getName() + " where idLoteSerasa in (:id) ";
		getAdsmHibernateTemplate().removeByIds(hql, ids);
		
		
		return ids.size();
	}
}
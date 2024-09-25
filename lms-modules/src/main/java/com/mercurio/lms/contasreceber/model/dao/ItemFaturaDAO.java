package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ItemFatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemFaturaDAO extends BaseCrudDao<ItemFatura, Long>
{

	public void flush() {
		getAdsmHibernateTemplate().flush();
    }
	
	public void clear() {
		getAdsmHibernateTemplate().clear();
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemFatura.class;
    }

   
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("devedorDocServFat", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.cliente", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.cliente.pessoa", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.filial", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.doctoServico", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.doctoServico.divisaoCliente", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.doctoServico.servico", FetchMode.JOIN);
    	lazyFindById.put("devedorDocServFat.doctoServico.moeda", FetchMode.JOIN);
    	/*lazyFindById.put("", FetchMode.JOIN);*/
    	
    	super.initFindByIdLazyProperties(lazyFindById);
    }
    
    /**
     * Retorna o itemFatura ativo do devedorDocServFat informado.
     * 
     * @author Mickaël Jalbert
     * @since 15/05/2006
     * 
     * @param Long idDevedorDocServFat
     * @return ItemFatura
     * */
    public ItemFatura findByDevedorDocServFat(Long idDevedorDocServFat){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("ifat");
    	hql.addInnerJoin(ItemFatura.class.getName(), "ifat");
    	hql.addCriteria("ifat.devedorDocServFat.id", "=", idDevedorDocServFat);
    	hql.addCriteria("ifat.fatura.tpSituacaoFatura", "!=", "CA");
    	List lstDevedorDocServFat = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstDevedorDocServFat.isEmpty()){
    		return (ItemFatura) lstDevedorDocServFat.get(0);
    	} else {
    		return null;
    	}
    }
    
    /**
     * Remove uma coleção de itens fatura
     *
     * @author Hector Julian Esnaola Junior
     * @since 27/06/2007
     *
     * @param itens
     *
     */
    public void removeItemFaturas(List itens) {
    	getHibernateTemplate().deleteAll(itens);
    }


    /**
     * Obtem a lista de tpDocumentoServico através do ItemFatura
     * @param idFatura
     * @return
     */
	public List<TypedFlatMap> findTpDocumentoItemFatura(Long idFatura) {
		
    	ProjectionList pl = Projections.projectionList()
		.add(Projections.property("dcs.tpDocumentoServico"), "tpDocumentoServico");
		
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(pl)
		.createAlias("fatura", "fat")
		.createAlias("devedorDocServFat", "dds")
		.createAlias("devedorDocServFat.doctoServico", "dcs")
		.add(Restrictions.eq("fat.id", idFatura));
		
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}    
    
	@SuppressWarnings("rawtypes")
	public List findItemsManutenidos(Long nrFatura, Long idFilial) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT item.idItemFatura, doc.nrDoctoServico,doc.filialByIdFilialOrigem.sgFilial, doc.tpDocumentoServico ")
		.append("FROM ").append(getPersistentClass().getName()).append(" as item ")
		.append("JOIN item.devedorDocServFat dev ")
		.append("JOIN dev.doctoServico doc ")
		.append("JOIN item.fatura fat ")
		.append("WHERE ")
		.append("fat.filialByIdFilial.idFilial = ? AND ")
		.append("fat.nrFatura = ? AND ")
		.append("item.blExcluir = 'S' ")
		.append("ORDER BY doc.filialByIdFilialOrigem.sgFilial,doc.nrDoctoServico ");
		
		return getHibernateTemplate().find(hql.toString(), new Object[]{idFilial,nrFatura});
	}    
    
}
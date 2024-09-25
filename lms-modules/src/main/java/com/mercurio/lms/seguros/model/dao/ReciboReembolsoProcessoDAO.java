package com.mercurio.lms.seguros.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.ReciboReembolsoDoctoServico;
import com.mercurio.lms.seguros.model.ReciboReembolsoProcesso;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboReembolsoProcessoDAO extends BaseCrudDao<ReciboReembolsoProcesso, Long>
{
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("processoSinistro", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReciboReembolsoProcesso.class;
    }
    
    /**
     * Método store da DF2.
     * @param reciboReembolsoProcesso
     * @param itemList
     * @author luisfco
     * @return
     */
    public java.io.Serializable storeReciboReembolsoProcesso(ReciboReembolsoProcesso reciboReembolsoProcesso, ItemList itemList) {
    	getAdsmHibernateTemplate().saveOrUpdate(reciboReembolsoProcesso);
    	getAdsmHibernateTemplate().saveOrUpdateAll(itemList.getNewOrModifiedItems());
    	getAdsmHibernateTemplate().deleteAll(itemList.getRemovedItems());
    	getAdsmHibernateTemplate().flush();
    	return reciboReembolsoProcesso.getIdReciboReembolsoProcesso();
    }
    
    /**
     * Remove filhos e pais em cascata de acordo com os ids dos pais.
     * @param ids
     */
    public void removeByIdsCustom(List ids) {
    	// remove filhos
    	getAdsmHibernateTemplate().removeByIds("delete from " + ReciboReembolsoDoctoServico.class.getName() + " as rrds where rrds.reciboReembolsoProcesso.id in(:id)", ids);
    	
    	// remove pai (ou pais)
    	getAdsmHibernateTemplate().removeByIds("delete from " + ReciboReembolsoProcesso.class.getName() + " as rrp where rrp.id in(:id)", ids);
    }
    
    private SqlTemplate getFindPaginatedQuery(TypedFlatMap tfm) {
    	Long idProcessoSinistro = tfm.getLong("processoSinistro.idProcessoSinistro"); 
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("new map(rrp.id", "idReciboReembolsoProcesso");
    	sql.addProjection("rrp.nrRecibo", "nrReciboReembolso");
    	sql.addProjection("rrp.dtReebolso", "dtReembolso");
    	sql.addProjection("rrp.vlReembolso", "vlReembolso");
    	sql.addProjection("mo.sgMoeda || ' ' || mo.dsSimbolo", "sgSimboloReembolso)");
    	sql.addFrom(ReciboReembolsoProcesso.class.getCanonicalName(), "rrp join rrp.moeda mo ");
    	sql.addCriteria("rrp.processoSinistro.id", "=",  idProcessoSinistro);
    	return sql;
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm, FindDefinition fd) {
    	SqlTemplate sql = getFindPaginatedQuery(tfm);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());    	
    }
    
    public List findRecibosByIdProcessoSinistro(Long idProcessoSinistro) {    	
    	return getAdsmHibernateTemplate().find("select rrp from "+ReciboReembolsoProcesso.class.getName()+" rrp where rrp.processoSinistro.id = ?", idProcessoSinistro); 
    }

	public List findSomaValoresReembolsoReciboReembolso(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		 
		sql.addProjection("SUM(RRP.vlReembolso + RRP.vlReembolsoAvulso)");
		sql.addFrom(ReciboReembolsoProcesso.class.getName(), "RRP");
		sql.addCriteria("RRP.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

}
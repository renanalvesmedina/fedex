package com.mercurio.lms.configuracoes.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.DistrFreteInternacional;
import com.mercurio.lms.configuracoes.model.TramoFreteInternacional;
import com.mercurio.lms.expedicao.model.CtoInternacional;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DistrFreteInternacionalDAO extends BaseCrudDao<DistrFreteInternacional, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DistrFreteInternacional.class;
    }
    
    public void storeTramosFreteInternacionais(List newOrModifiedItems){
    	getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
    }

    public void removeTramosFreteInternacionais(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
        getAdsmHibernateTemplate().flush();
    }

    /**
     * Bisca Tramos Internacionais da Distribuição informada.
     *@author Robson Edemar Gehl
     * @param id
     * @return
     */
    public List findTramosFreteInternacionais(Long id){
        
        if( id != null ){
        
            SqlTemplate sql = new SqlTemplate();
            
            sql.addProjection("t");
            
            sql.addFrom(TramoFreteInternacional.class.getName(),"t " +
                        "inner join t.distrFreteInternacional dis " +
                        "left outer join fetch t.cliente c " +
                        "left outer join fetch c.pessoa cp ");
            
            sql.addCriteria("dis.id","=",id);        
            
            
            sql.addOrderBy("cp.nmPessoa");
            sql.addOrderBy("t.nrTramoFreteInternacional");
            
            
            return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
            
        } else {
            return new ArrayList();
        }
        
    }
    
    public Integer getRowCountTramos(Long id){
    	DetachedCriteria dc = DetachedCriteria.forClass(TramoFreteInternacional.class);
    	dc.add(Restrictions.eq("distrFreteInternacional.id",id));
    	dc.setProjection(Projections.rowCount());
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }

    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
    	return super.findPaginated(criteria, findDef);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("filialOrigem", FetchMode.JOIN);
    	lazyFindPaginated.put("filialOrigem.pessoa", FetchMode.JOIN);
    	lazyFindPaginated.put("filialDestino", FetchMode.JOIN);
    	lazyFindPaginated.put("filialDestino.pessoa", FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filialOrigem", FetchMode.JOIN);
    	lazyFindById.put("filialOrigem.pessoa", FetchMode.JOIN);
    	lazyFindById.put("filialDestino", FetchMode.JOIN);
    	lazyFindById.put("filialDestino.pessoa", FetchMode.JOIN);
    }

    /**
     * Salva os items e o master
     * @param bean Master
     * @param items Itens
     * @return DistrFreteInternacional salvo
     */
    public DistrFreteInternacional store(DistrFreteInternacional bean, ItemList items) {
        super.store(bean);
        removeTramosFreteInternacionais(items.getRemovedItems());
        storeTramosFreteInternacionais(items.getNewOrModifiedItems());        
        getAdsmHibernateTemplate().flush();
        return bean;
    }
    
    public DistrFreteInternacional findByCtoInternacional(CtoInternacional ctoInternacional){
    	DistrFreteInternacional distrFreteInternacional = null;
    	StringBuilder hql = new StringBuilder()
    	.append("select	dfi.pcFreteExterno as pcFreteExterno").append("\n")
    	.append("from	").append(getPersistentClass().getName()).append(" as dfi").append("\n")
    	.append("inner	join dfi.tramoFreteInternacionais as dfitfi").append("\n")
    	.append("left 	outer join dfitfi.cliente as dfitfic").append("\n")
    	.append("where	dfi.filialOrigem.id = ").append(ctoInternacional.getFilialByIdFilialOrigem().getIdFilial()).append("\n")
    	.append("and  	dfi.filialDestino.id = ").append(ctoInternacional.getFilialByIdFilialDestino().getIdFilial()).append("\n")
    	;

    	if(ctoInternacional.getClienteByIdClienteRemetente() != null){
    		hql.append("and	 (dfitfic.id = ").append(ctoInternacional.getClienteByIdClienteRemetente().getIdCliente())
    		.append(" or dfitfic.id is null)")
    		;
    	}

    	List distrsFreteInternacional = getAdsmHibernateTemplate().find(hql.toString());
    	if(distrsFreteInternacional != null && !distrsFreteInternacional.isEmpty()){
    		BigDecimal pcFreteExterno = (BigDecimal) distrsFreteInternacional.get(0);
    		distrFreteInternacional = new DistrFreteInternacional();
    		distrFreteInternacional.setPcFreteExterno(pcFreteExterno);
    	}

    	return distrFreteInternacional;
    }
    
    public void removeById(Long id) {    
    	super.removeById(id, true);
    }
    
    public int removeByIds(List ids) {    
    	return super.removeByIds(ids, true);
    }
    
}
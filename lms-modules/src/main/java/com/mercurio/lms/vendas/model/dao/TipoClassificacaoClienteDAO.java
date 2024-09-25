package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.vendas.model.DescClassificacaoCliente;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoClassificacaoClienteDAO extends BaseCrudDao<TipoClassificacaoCliente, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoClassificacaoCliente.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("empresa", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }
    
    public TipoClassificacaoCliente findByDescClassificacaoCliente(Long idDescClassificacaoCliente){
    	
    	TipoClassificacaoCliente tcc = null;
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.createAlias("descClassificacaoClientes", "dcc");
    	dc.add(Restrictions.eq("dcc.idDescClassificacaoCliente", idDescClassificacaoCliente));
    	
    	List list = findByDetachedCriteria(dc);

    	if (!list.isEmpty()){
    		tcc = (TipoClassificacaoCliente) list.get(0);
    	}
    	
    	return tcc;
    }
    
    public void removeById(Long id) {
    	// TODO Auto-generated method stub
    	super.removeById(id, true);
    }
    
    public int removeByIds(List ids) {
    	// TODO Auto-generated method stub
    	return super.removeByIds(ids, true);
    }
    
    public TipoClassificacaoCliente store(TipoClassificacaoCliente m, ItemList items) {
        super.store(m);
		removeDescClassificacaoCliente(items.getRemovedItems());
		storeDescClassificacaoCliente(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();    
        return m;
    }    
    
	public void storeDescClassificacaoCliente(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	public void removeDescClassificacaoCliente(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}
	
	public List findDescClassificacaoClienteByTipoClassificacaoClienteId(Long tipoRegistroId) {
		return super.findByDetachedCriteria(getDetachedCriteria(tipoRegistroId));
	}
	
	public Integer getRowCountDescClassificacaoClienteByTipoClassificacaoClienteId(Long tipoRegistroId) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(tipoRegistroId)
				.setProjection(Projections.rowCount()));
	}
	
	private DetachedCriteria getDetachedCriteria(Long tipoRegistroId) {
		return DetachedCriteria.forClass(DescClassificacaoCliente.class)
		.add(Restrictions.eq("tipoClassificacaoCliente.id", tipoRegistroId));
	}

}
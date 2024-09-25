package com.mercurio.lms.coleta.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.lms.coleta.model.MilkRemetente;
import com.mercurio.lms.coleta.model.MilkRun;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MilkRunDAO extends BaseCrudDao<MilkRun, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MilkRun.class;
    }	
	
    protected void initFindByIdLazyProperties(Map map) 
    {
    	map.put("cliente", FetchMode.JOIN);
    	map.put("cliente.pessoa", FetchMode.SELECT);
    	map.put("milkRemetentes", FetchMode.SELECT);
    }

    protected void initFindPaginatedLazyProperties(Map map) 
    {
    	map.put("cliente", FetchMode.JOIN);
    	map.put("cliente.pessoa", FetchMode.SELECT);
    	map.put("milkRemetentes", FetchMode.SELECT);
    }
	
    public MilkRun store(MilkRun master, ItemList items, ItemListConfig config) {
        super.store(master);
    	
        removeMilkRemtente(items.getRemovedItems());
    	storeMilkRemetente(items.getNewOrModifiedItems());
    	
		for(Iterator iter = items.iterator(master.getIdMilkRun(), config); iter.hasNext();) {
			MilkRemetente milkRemetente = (MilkRemetente) iter.next();
			storeListSemanaRemetMRun(milkRemetente.getSemanaRemetMruns());
		}		
 	   	getAdsmHibernateTemplate().flush();
 	   	
 	   	return master;
    }
    
    private void storeMilkRemetente(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeMilkRemtente(List removeItems) {
		for (int i = 0; i < removeItems.size(); i++) {
			MilkRemetente milkRemetente = (MilkRemetente) removeItems.get(i);
			getAdsmHibernateTemplate().deleteAll(milkRemetente.getSemanaRemetMruns());
		}
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}    
    
    /**
     * Salva lista de Semana Remetente Milk Run
     * 
     * @param newOrModifiedItems
     */
	private void storeListSemanaRemetMRun(List listSemanaRemetMRun) {
		getAdsmHibernateTemplate().saveOrUpdateAll(listSemanaRemetMRun);
	}
	    
	/**
	 * Deleta MilkRun com o ID do MilkRun
	 * 
	 * @param idMilkRun
	 */
    public void removeById(Long idMilkRun) {
        String sql = "delete from " + MilkRun.class.getName() + " as mr " +
        			 " where mr.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idMilkRun);
    }  	
}
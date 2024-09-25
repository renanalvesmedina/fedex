package com.mercurio.lms.sgr.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.sgr.model.RotaPostoControle;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaPostoControleDAO extends BaseCrudDao<RotaPostoControle, Long>
{

	protected void initFindByIdLazyProperties(Map map) {
		map.put("postoControle", FetchMode.JOIN);
		map.put("postoControle.rodovia", FetchMode.JOIN);
		map.put("postoControle.municipio", FetchMode.JOIN);
		map.put("postoControle.municipio.unidadeFederativa", FetchMode.JOIN);
		map.put("rota", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map map) {
		map.put("rota", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("postoControle", FetchMode.JOIN);
		map.put("postoControle.rodovia", FetchMode.JOIN);
		map.put("postoControle.municipio", FetchMode.JOIN);
		map.put("postoControle.municipio.unidadeFederativa", FetchMode.JOIN);
		map.put("rota", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RotaPostoControle.class;
    }
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid agrupados pela rota.
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public Integer getRowCountByRota(Map criteria){
    	String idRota = null;
    	Map rota =  (Map)criteria.get("rota");
    	
    	if (rota!=null) {
    		idRota = (String)rota.get("idRota");
    	}
    	
        DetachedCriteria dc = DetachedCriteria.forClass(Rota.class)
        .setProjection(Projections.count("idRota")) 
       	.add(Restrictions.isNotNull("rotaPostoControles"))
       	.add(Restrictions.isNotEmpty("rotaPostoControles")); 
        
        if (!idRota.equals("")) dc.add(Restrictions.eq("idRota", Long.valueOf(idRota)));
        
        List result = super.findByDetachedCriteria(dc);
        
        return (Integer) result.get(0);
    }
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid agrupados pela rota.
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedGroupByRota(Map criteria, FindDefinition findDefinition){
        
    	String idRota = null;
    	Map rota =  (Map)criteria.get("rota");
    	
    	if (rota!=null) {
    		idRota = (String)rota.get("idRota");
    	}

        DetachedCriteria dc = DetachedCriteria.forClass(Rota.class)
       	.add(Restrictions.isNotNull("rotaPostoControles"))
       	.add(Restrictions.isNotEmpty("rotaPostoControles")); 
        
        if (!idRota.equals("")) dc.add(Restrictions.eq("idRota", Long.valueOf(idRota.toString())));
        
        return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
   
    public List findPostosControleByIdRota(Map map){
        Long idRota = Long.valueOf(map.get("idRota").toString()); 
        
        DetachedCriteria dc = DetachedCriteria.forClass(RotaPostoControle.class)
        	.setFetchMode("rota", FetchMode.JOIN)
        	.setFetchMode("postoControle", FetchMode.JOIN)
        	.setFetchMode("postoControle.municipio", FetchMode.JOIN)
        	.setFetchMode("postoControle.municipio.unidadeFederativa", FetchMode.JOIN)
        	.setFetchMode("postoControle.rodovia", FetchMode.JOIN)
        	.add(Restrictions.eq("rota.idRota", idRota))
        	.addOrder(Order.asc("nrOrdem"));

        return super.findByDetachedCriteria(dc);
    }
    
    public Short findLastNrOrdem(Long idRota) {

        DetachedCriteria dc = DetachedCriteria.forClass(RotaPostoControle.class)
        	.setFetchMode("rota", FetchMode.JOIN)
        	.add(Restrictions.eq("rota.idRota", idRota))
        	.setProjection(Projections.max("nrOrdem"));

        List result =  super.findByDetachedCriteria(dc);
        
        Short maxNrOrdem = (Short) result.get(0);
        
        
        return maxNrOrdem;
    }
    
    /**
     * Retorna o numero de rows encontrados para a grid de rota.
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public Integer getRowCountRota(Long idRota){

        DetachedCriteria dc = DetachedCriteria.forClass(Rota.class)
        .setProjection(Projections.rowCount());
       	        
        if (idRota!=null) dc.add(Restrictions.eq("idRota", idRota));
        
        List result = super.findByDetachedCriteria(dc);
        
        return (Integer) result.get(0);	
    }
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid agrupados pela rota.
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedRota(Long idRota, FindDefinition findDefinition){
    	
        DetachedCriteria dc = DetachedCriteria.forClass(Rota.class);
        	
        if (idRota!=null) dc.add(Restrictions.eq("idRota", Long.valueOf(idRota.toString())));
        
    	return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
}
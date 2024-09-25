package com.mercurio.lms.contasreceber.model.dao;


import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ItemPreFatura;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemPreFaturaDAO extends BaseCrudDao<ItemPreFatura, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemPreFatura.class;
    }

    
    
	/**Busca os dados do item_pre_fatura, item_fatura, fatura e filial de acordo com o idFatura passado.
	 * 
	 * @author Diego Umpierre
	 * @param idFatura
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedComplemento(Long idFatura, FindDefinition findDef){
	
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("itPreFat");
		
		//from
		sql.addInnerJoin(ItemPreFatura.class.getName(), "itPreFat");
    	sql.addInnerJoin("fetch itPreFat.itemFatura", "itFatura");
    	sql.addInnerJoin("fetch itFatura.fatura", "fat");
    	sql.addInnerJoin("fetch fat.filialByIdFilial", "fil");
    	
    	//where
    	sql.addCriteria("itFatura.fatura.id","=",idFatura);
    	
    	//order by
    	sql.addOrderBy("itPreFat.nrNotaFiscal");
		
	    return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());	
		
	}



	/**Usado para realizar a contagem de linhas retornada na consulta, findPaginatedComplemento
	 * 
	 * @author Diego Umpierre
	 * @param idFatura
	 * @return
	 */
	public Integer getRowCountComplemento(Long idFatura) {
		
		
		SqlTemplate sql = new SqlTemplate();
		
//		from
		sql.addInnerJoin(ItemPreFatura.class.getName(), "itPreFat");
    	sql.addInnerJoin("fetch itPreFat.itemFatura", "itFatura");
 
    	//where
    	sql.addCriteria("itFatura.fatura.id","=",idFatura);
    	
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());	}
	
}
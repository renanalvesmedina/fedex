package com.mercurio.lms.pendencia.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.ItemMda;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemMdaDAO extends BaseCrudDao<ItemMda, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemMda.class;
    }

	/**
	 * Método que retorna um Item de MDA usando como filtro o ID do ItemMda
	 * 
	 * @param Long ItemMda
	 * @return ItemMda
	 */
    public ItemMda findItemMdaById(Long idItemMda) {
		SqlTemplate sqlTemp = new SqlTemplate();	
		sqlTemp.addFrom(ItemMda.class.getName() + " imda join fetch imda.mda mda " +
												  " left join fetch imda.doctoServico ds " +
												  " left join fetch ds.filialByIdFilialOrigem fo " +
												  " join fetch imda.naturezaProduto np " +
												  " join fetch imda.moeda mo ");		
		
		sqlTemp.addCriteria("imda.idItemMda", "=", idItemMda);
		
		return (ItemMda) getAdsmHibernateTemplate().findUniqueResult(sqlTemp.getSql(), sqlTemp.getCriteria());        
    } 
    
    
	/**
	 * Método que monta o SQL para pesquisa de ItemMDA
	 * 
	 * @param TypedFlatMap criteria
	 * @return
	 */
	private SqlTemplate mountSqlTemp(TypedFlatMap criteria){
		SqlTemplate sqlTemp = new SqlTemplate();
		
		sqlTemp.addFrom(ItemMda.class.getName() + " imda join fetch imda.mda mda " +
												  " left join fetch imda.doctoServico ds " +
												  " left join fetch ds.filialByIdFilialOrigem fo " +
												  " left join fetch imda.naoConformidade nc " +
												  " left join fetch nc.filial ncf " +
												  " join fetch imda.naturezaProduto np " +
												  " join fetch imda.moeda mo ");		
		
		sqlTemp.addCriteria("mda.id", "=", criteria.getLong("idDoctoServico"));
		
		return sqlTemp;
	}
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param TypedFlatMap criteria
     * @param FindDefinition findDefinition  
     * @return
     */ 
    public ResultSetPage findPaginatedItemMda(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sqlTemp = mountSqlTemp(criteria);
		
		sqlTemp.addProjection("imda");
		sqlTemp.addOrderBy("fo.sgFilial");
		sqlTemp.addOrderBy("ds.nrDoctoServico");
		
		return getAdsmHibernateTemplate().findPaginated(sqlTemp.getSql(true), findDefinition.getCurrentPage(), 
														findDefinition.getPageSize(), sqlTemp.getCriteria());
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados 
     * para determinados parametros.
     * 
     * @param TypedFlatMap criteria
     * @param FindDefinition findDefinition
     * @return
     */    
    public Integer getRowCountItemMda(TypedFlatMap criteria, FindDefinition findDefinition) {
    	SqlTemplate sqlTemp = mountSqlTemp(criteria);    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sqlTemp.getSql(false), sqlTemp.getCriteria());    	
    }	

    
	/**
	 * Método que retorna um ItemMda a partir do ID do DoctoServico referente ao MDA. 
	 * 
	 * @param idDoctoServico
	 * @return List
	 */
    public List findItemMdaByMda(Long idDoctoServico) {
		SqlTemplate sqlTemp = new SqlTemplate();
		
		sqlTemp.addFrom(ItemMda.class.getName() + " imda join fetch imda.mda mda " +
												  " left join fetch imda.doctoServico ds " +
												  " join fetch ds.filialByIdFilialOrigem fo " +
												  " left join fetch imda.naoConformidade nc " +
												  " left join fetch nc.filial ncf " +
												  " join fetch imda.naturezaProduto np " +
												  " join fetch imda.moeda mo ");		
		
		sqlTemp.addCriteria("mda.id", "=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(sqlTemp.getSql(), sqlTemp.getCriteria());
    }    
}
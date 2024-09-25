package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.MilkRemetente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MilkRemetenteDAO extends BaseCrudDao<MilkRemetente, Long>
{
    protected void initFindByIdLazyProperties(Map map) 
    {
    	map.put("cliente", FetchMode.JOIN);
    	map.put("cliente.pessoa", FetchMode.JOIN);
    	map.put("milkRun", FetchMode.JOIN);
    	map.put("enderecoPessoa", FetchMode.JOIN);
    	map.put("enderecoPessoa.municipio", FetchMode.JOIN);
    	map.put("enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);    	
    	map.put("naturezaProduto", FetchMode.JOIN);
    	map.put("servico", FetchMode.JOIN);            	
    	map.put("semanaRemetMruns", FetchMode.SELECT);
    }

    protected void initFindPaginatedLazyProperties(Map map) 
    {
    	map.put("cliente", FetchMode.JOIN);
    	map.put("cliente.pessoa", FetchMode.JOIN);
    	map.put("milkRun", FetchMode.JOIN);
    	map.put("enderecoPessoa", FetchMode.JOIN);
    	map.put("enderecoPessoa.municipio", FetchMode.JOIN);
    	map.put("enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);    	
    	map.put("naturezaProduto", FetchMode.JOIN);
    	map.put("servico", FetchMode.JOIN);            	
    	map.put("semanaRemetMruns", FetchMode.SELECT);
    }

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MilkRemetente.class;
    }

	private DetachedCriteria getDetachedCriteria(Long idMilkRun) {
		DetachedCriteria dc = DetachedCriteria.forClass(MilkRemetente.class);
		dc.add(Restrictions.eq("milkRun.id", idMilkRun));
		dc.setFetchMode("cliente", FetchMode.JOIN);
		dc.setFetchMode("cliente.pessoa", FetchMode.JOIN);
		dc.setFetchMode("enderecoPessoa", FetchMode.JOIN);
		dc.setFetchMode("enderecoPessoa.municipio", FetchMode.JOIN);
		dc.setFetchMode("enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);
		dc.setFetchMode("servico", FetchMode.JOIN);
		dc.setFetchMode("naturezaProduto", FetchMode.JOIN);		            	
		
		return dc;
	}
	
	public List findMilkRemetenteByIdMilkRun(Long idMilkRun) {
		return super.findByDetachedCriteria(getDetachedCriteria(idMilkRun));		
	}	
	
	public Integer getRowCountMilkRemetenteByIdMilkRun(Long idMilkRun) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idMilkRun)
				.setProjection(Projections.rowCount()));
	}
	
	/**
	 * Deleta MilkRemetente com o ID do MilkRun
	 * 
	 * @param idMilkRun
	 */
    public void removeByIdMilkRun(Serializable idMilkRun) {
        String sql = "delete from " + MilkRemetente.class.getName() + " as mr " + 
        			 " where " +
        			 "mr.milkRun.id = :id";
        
        getAdsmHibernateTemplate().removeById(sql, idMilkRun);
    }  	
	
}
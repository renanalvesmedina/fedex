package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.PipelineReceita;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PipelineReceitaDAO extends BaseCrudDao<PipelineReceita, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PipelineReceita.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("pipelineCliente", FetchMode.JOIN);
		
	
	}
    
    /**
     * Consulta as etapas de uma determinada visita
     * @param idPipelineCliente
     * @return
     */
    public List findPipelineReceitaByPipelineCliente(Long idPipelineCliente){
    	 SqlTemplate sql = new SqlTemplate();
    	 
    	 sql.addProjection(" pr ");
    	 sql.addFrom(PipelineReceita.class.getName() + " pr inner join fetch pr.pipelineCliente pc " );						    
    	 sql.addCriteria("pc.id", "=", idPipelineCliente);

    	 return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    			 
    }
    
    
    /**
     * Remove os registros pertencentes à pipeline Cliente onde o id  do pipelineCliente é
     * recebido como parâmetro
     * @param idPipelineCliente
     */
    public void removeByIdPipelineCliente(Long idPipelineCliente){
        String hql = " delete from " + PipelineReceita.class.getName() + " as pr where pr.pipelineCliente.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idPipelineCliente);
        getAdsmHibernateTemplate().flush();
    }
}
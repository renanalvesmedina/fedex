package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.PipelineEtapa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PipelineEtapaDAO extends BaseCrudDao<PipelineEtapa, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PipelineEtapa.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("pipelineCliente", FetchMode.JOIN);
    		
	}
    
    /**
     * Consulta as etapas de uma determinada visita
     * @param idPipelineCliente
     * @return
     */
    public List findPipelineEtapaByPipelineCliente(Long idPipelineCliente){
    	 SqlTemplate sql = new SqlTemplate();
    	 
    	 sql.addProjection("new Map(pe.idPipelineEtapa", "idPipelineEtapa");
    	 
    	 sql.addProjection("pc.idPipelineCliente", "idPipelineCliente");
    	 
    	 sql.addProjection("pe.tpPipelineEtapa", "tpPipelineEtapa");
    	 
    	 sql.addProjection("pe.dtEvento", "dtEvento");
    	 
    	 sql.addProjection("pe.dsObservacao", "dsObservacao");
    	 
    	 sql.addProjection("v.idVisita", "idVisita)");
    	 
    	 
    	 sql.addFrom(PipelineEtapa.class.getName() + " pe inner join pe.pipelineCliente pc left outer join pe.visita v" );
    	 
    	 sql.addCriteria("pc.id", "=", idPipelineCliente);

    	 return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    			 
    }
    
    /**
     * Consulta as etapas de uma determinada visita
     * @param idPipelineCliente
     * @return
     */
    public List findPipelineEtapaByIdPipelineCliente(Long idPipelineCliente){
    	 SqlTemplate sql = new SqlTemplate();
    	 
    	 sql.addProjection("pe");
    	 
    	 
    	 sql.addFrom(PipelineEtapa.class.getName() + " pe inner join fetch pe.pipelineCliente pc " );
    	 
    	 sql.addCriteria("pc.id", "=", idPipelineCliente);

    	 return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    			 
    }
    
   
    /**
     * Remove os registros pertencentes à pipeline Cliente onde o id  do pipelineCliente é
     * recebido como parâmetro
     * @param idPipelineCliente
     */
    public void removeByIdPipelineCliente(Long idPipelineCliente){
        String hql = " delete from " + PipelineEtapa.class.getName() + " as pe where pe.pipelineCliente.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idPipelineCliente);
        getAdsmHibernateTemplate().flush();
    }
    
    public PipelineEtapa findEtapaFilledByIdPipelineClienteAndTpPipelineEtapa(Long idPipelineCliente, String tpPipelineEtapa) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select piet ");
		hql.append("from PipelineEtapa piet ");
		hql.append("join piet.pipelineCliente picl ");
		hql.append("where picl.idPipelineCliente = ? ");
		hql.append("and piet.tpPipelineEtapa = ? ");
		hql.append("and (piet.dtEvento is not null or piet.dsObservacao is not null) ");
		params.add(idPipelineCliente);
		params.add(tpPipelineEtapa);
		
		return (PipelineEtapa) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}

	public List<Long> findEtapasByTpPipelineEtapaAndIdPipelineClienteSimulacao(List<String> lstTpPipelineEtapa, Long idPipelineClienteSimulacao) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select piet.idPipelineEtapa ");
		hql.append("from PipelineEtapa piet ");
		hql.append("join piet.pipelineCliente picl ");
		hql.append("where piet.tpPipelineEtapa in (:lstTpPipelineEtapa) ");
		hql.append("and exists ( ");
		hql.append(" 			from PipelineClienteSimulacao pcsi ");
		hql.append("            join pcsi.pipelineCliente picl1 ");
		hql.append("            where picl1 = picl");
		hql.append("            and pcsi.idPipelineClienteSimulacao = :idPipelineClienteSimulacao ");
		hql.append("            )");
		params.put("lstTpPipelineEtapa", lstTpPipelineEtapa);
		params.put("idPipelineClienteSimulacao", idPipelineClienteSimulacao);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);		
	}

}
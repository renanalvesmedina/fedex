package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.workflow.model.Pendencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PendenciaDAO extends BaseCrudDao<Pendencia, Long>
{
	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Pendencia.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("ocorrencia",FetchMode.JOIN);
    	lazyFindById.put("ocorrencia.usuario",FetchMode.JOIN);
    	lazyFindById.put("ocorrencia.eventoWorkflow",FetchMode.JOIN);
    	lazyFindById.put("ocorrencia.eventoWorkflow.tipoEvento",FetchMode.JOIN);
    }

    public List findPendenciasAbertasByOcorrencia(Long idOcorrencia) {
		SqlTemplate sql = this.mountSqlFindPendenciaByOcorrencia(idOcorrencia);
		sql.addCriteria("pe.tpSituacaoPendencia","=","E");
		
        return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public List findPendenciasByOcorrencia(Long idOcorrencia) {
		SqlTemplate sql = this.mountSqlFindPendenciaByOcorrencia(idOcorrencia);
		
        return this.getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    private SqlTemplate mountSqlFindPendenciaByOcorrencia(Long idOcorrencia){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("pe");
						
		sql.addFrom(Pendencia.class.getName(), "pe ");
		sql.addCriteria("pe.ocorrencia.id","=",idOcorrencia);
		return sql;
    }

	/**
	 * Retorna o usuario (dentro de uma list) da pendencia informada.
	 * 
	 * @param Long idPendencia
	 * @return List
	 * */
	public List<Usuario> findSolicitanteByPendencia(Long idPendencia){
		if (idPendencia == null) {
			throw new IllegalArgumentException("idPendencia não pode ser null");
		}
		String sql = "select distinct u from "+
						Pendencia.class.getName()+" pen "+
						"join pen.ocorrencia as oco " +
						"join oco.usuario as u where pen.id = ?";
		return this.getAdsmHibernateTemplate().find(sql,new Object[]{idPendencia});		
	}		

	public List<Pendencia> findPendenciaByEvento(Long idProcesso, Long idEvento, String ... tpSituacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pendencia");
		dc.createAlias("pendencia.ocorrencia", "ocorrencia");
		dc.setFetchMode("ocorrencia", FetchMode.DEFAULT);
		dc.createAlias("ocorrencia.eventoWorkflow", "eventoWorkflow");
		dc.setFetchMode("eventoWorkflow", FetchMode.DEFAULT);
		dc.add(Restrictions.eq("pendencia.idProcesso", idProcesso));
		dc.add(Restrictions.eq("eventoWorkflow.id", idEvento));
		dc.add(Restrictions.in("pendencia.tpSituacaoPendencia", tpSituacao));
		dc.addOrder(Order.desc("pendencia.id"));
		
		return this.getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}	
}
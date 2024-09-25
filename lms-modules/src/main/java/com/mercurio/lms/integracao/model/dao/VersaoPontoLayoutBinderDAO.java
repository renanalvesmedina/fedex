package com.mercurio.lms.integracao.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.RestrictionsBuilder;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsmmanager.integracao.model.PontoLayoutBinder;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.integracao.model.VersaoPontoLayoutBinder;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class VersaoPontoLayoutBinderDAO extends BaseCrudDao {

	@SuppressWarnings("unchecked")
	protected Class getPersistentClass() {
		return VersaoPontoLayoutBinder.class;
	}	
	
	@Override
	@SuppressWarnings("unchecked")
	public HashMap findById(Serializable id) {
		
		SqlTemplate sql = new SqlTemplate();
		montaHqlJoins(sql);
		sql.addProjection(" new Map( "+
							"vplb.idVersaoPontoLayoutBinder    			       as idVersaoPontoLayoutBinder, 			   					    " +
							"plb.id                            			       as pontoLayoutBinder_id, " +
							"glb.id     			   				 	       as grupoLayoutBinder_id,	" +
							"pb.nome                           			       as pontoLayoutBinder_pontoBinder_nome,      				    	" +
							"pfIni.nmParametroFilial           			       as parametroFilialInicio_nmParametroFilial,         	  								    " +
							"pfFim.nmParametroFilial           			       as parametroFilialFim_nmParametroFilial," +
							"pfIni.idParametroFilial           			       as parametroFilialInicio_idParametroFilial,         	  								    " +
							"pfFim.idParametroFilial           			       as parametroFilialFim_idParametroFilial,         	  								    " +         	  								    
							"case when lb.nome is null or lb.nome = '' then glb.nome      												        " +
							"else lb.nome end  as layoutOrGroup " +
						 " )");
		sql.addCriteria("vplb.idVersaoPontoLayoutBinder", "=",id);
		
		ArrayList resultado = (ArrayList)getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(resultado==null){
			return null;
		}else{			
			HashMap mpResultado = (HashMap)resultado.get(0);			
			return mpResultado;
		}
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {		
		SqlTemplate sql = createSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());		
	}	
	
	@Override
	@SuppressWarnings("unchecked")
	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = createSqlTemplate(criteria);		
		List list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		return Integer.valueOf(list.size());
	}
	
	@SuppressWarnings("unchecked")
	private SqlTemplate createSqlTemplate(Map parameters){
		SqlTemplate sql = new SqlTemplate(); 
		montaHqlJoins(sql,parameters);
		montaHqlProjection(sql);
		montaHqlCriteria(sql,parameters);
		return sql;
	}
	

	private void montaHqlJoins(SqlTemplate sql,Map parameters) {
		sql.addFrom(getPersistentClass().getName() + " as vplb "
				+ "left join vplb.pontoLayoutBinder  						as plb       "
				+ "left join vplb.parametroFilialInicio           as pfIni    "
				+ "left join vplb.parametroFilialFim              as pfFim    "
				+ "left join vplb.grupoLayoutBinder            		    as glb 	     "
				+"left join plb.pontoBinder  			  as pb       "
				+"left join plb.layoutBinder  			  as lb ");
		
		
		
	}
	
	private void montaHqlJoins(SqlTemplate sql) {
		sql.addFrom(getPersistentClass().getName() + " as vplb "
				+ "left join vplb.pontoLayoutBinder  						as plb       "
				+ "left join vplb.parametroFilialInicio           as pfIni    "
				+ "left join vplb.parametroFilialFim              as pfFim    "
				+ "left join vplb.grupoLayoutBinder            		    as glb 	     "
				+"left join plb.pontoBinder  			  as pb       "
				+"left join plb.layoutBinder  			  as lb ");
		
			
	}
	
	private void montaHqlProjection(SqlTemplate sql){
		sql.addProjection(" new Map( " +
						  " vplb.idVersaoPontoLayoutBinder          as idVersaoPontoLayoutBinder, " + 	
						  " pb.nome                        			as nomePontoBinder,       	  " +
						  " lb.nome                                 as nomeLayoutBinder,      	  " +
						  " glb.nome                                as nomeGrupoLayoutBinder, 	  " +
						  " pfIni.nmParametroFilial                 as nomeVersaoIni,         	  " +
						  " pfFim.nmParametroFilial                 as nomeVersaoFim         	  " +
				" )");		
	}
	
	@SuppressWarnings("unchecked")
	private void montaHqlCriteria(SqlTemplate sql,Map parameters) {		
		sql.addCriteria("vplb.idVersaoPontoLayoutBinder", "=", 
				MapUtilsPlus.getLong(parameters,"idVersaoPontoLayoutBinder",null));		
		
		
		sql.addCriteria("pfFim.idParametroFilial", "=", MapUtilsPlus.getLongOnMap(parameters, "parametroFilialFim", "idParametroFilial"));	
		sql.addCriteria("pfIni.idParametroFilial", "=", MapUtilsPlus.getLongOnMap(parameters, "parametroFilialInicio", "idParametroFilial"));	
		
		sql.addCriteria("plb.id", "=", MapUtilsPlus.getLongOnMap(parameters, "pontoLayoutBinder", "id"));	
		sql.addCriteria("glb.id", "=", MapUtilsPlus.getLongOnMap(parameters, "grupoLayoutBinder", "id"));
	}

	/**
	 * Método criado para remover dependencia com PontoLayoutBinderService.
	 * @param criterions
	 * @return
	 */
    public List findLookupPontoLayoutBinder(Map<String, Object> criterions) {
    	DetachedCriteria dc = DetachedCriteria.forClass(PontoLayoutBinder.class);
        RestrictionsBuilder rb = new RestrictionsBuilder(PontoLayoutBinder.class, false);
        rb.createDefaultBuilders(criterions); 
        rb.createCriterions(criterions, 
							null, 
							dc);
		List ret = findPaginatedByDetachedCriteria(dc, Integer.valueOf(1), Integer.valueOf(2)).getList();
		return ret;
    }
	
}
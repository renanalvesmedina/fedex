package com.mercurio.lms.contasreceber.model.dao;




import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoMotivoOcorrenciaDAO extends BaseCrudDao<HistoricoMotivoOcorrencia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HistoricoMotivoOcorrencia.class;
    }

   
    
    /**Consulta  que retorna dados do Motivos de Rejeição de um boleto. 
     * Usado na tela:
     * Consultar Histórico das Ocorrências do Boleto
     * 
     *@author Diego Umpierre - LMS
     *@see com.mercurio.lms.contasreceber.action.ConsultarHistoricoOcorrenciasBoletoAction
     *
     *@param idHistoricoBoleto identificador do Historico do Boleto, findDef FindDefinition    
     *
     *@return ResultSetPage com o resultado da consulta de acordo com os parametros.
     */
    public ResultSetPage findPaginatedMotMov(Long idHistoricoBoleto,FindDefinition findDef) {
   
   
    	SqlTemplate histMotOco = new SqlTemplate();
    	
    	//consulta que retorna os dados da historico motivo ocorrencia, motivo ocorrencia banco
    	histMotOco.addProjection("distinct( motOcoBan.nr_motivo_ocorrencia_banco) as nrMotivoOcorrenciaBanco," +
    							"motOcoBan.ds_motivo_ocorrencia_banco as dsMotivoOcorrencia");
    	
    	
    	
    	histMotOco.addFrom("motivo_ocorrencia_banco motOcoBan " +
                 "   INNER JOIN historico_motivo_ocorrencia hisMotOco    " +
                 		" ON motOcoBan.id_motivo_ocorrencia_banco = hisMotOco.id_motivo_ocorrencia_banco  ");
    	histMotOco.addCriteria("hisMotOco.id_historico_boleto","=",idHistoricoBoleto);  
    		
		  
    	SqlTemplate hisBoletoMot = new SqlTemplate();
    	 
     	//consulta que retorna os dados da historico motivo ocorrencia, motivo ocorrencia 
    	hisBoletoMot.addProjection(" null as nrMotivoOcorrenciaBanco," +
    			PropertyVarcharI18nProjection.createProjection("motOco.ds_motivo_ocorrencia_i") + " as dsMotivoOcorrencia");
    	
    	hisBoletoMot.addFrom("historico_boleto hisBol " +
                 "   INNER JOIN motivo_ocorrencia motOco   " +
                 		" ON motOco.id_motivo_ocorrencia = hisBol.id_motivo_ocorrencia  ");
    	hisBoletoMot.addCriteria("hisBol.id_historico_boleto","=",idHistoricoBoleto);  
    		
    	 
    	 ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
             public void configQuery(SQLQuery sqlQuery) {                
                 sqlQuery.addScalar("nrMotivoOcorrenciaBanco", Hibernate.LONG);
                 sqlQuery.addScalar("dsMotivoOcorrencia", Hibernate.STRING);
                 
                 
             }
         };
         
         
         String select = histMotOco.getSql() + 	
         		"\nUNION\n" +
         		hisBoletoMot.getSql();
         
         histMotOco.addCriteriaValue(hisBoletoMot.getCriteria());
     
         
return getAdsmHibernateTemplate().findPaginatedBySql(select,
                                          findDef.getCurrentPage(),
                                          findDef.getPageSize(),
                                          histMotOco.getCriteria(),
                                          configSql);
	}

    
    
    
    /**
     * Faz a contagem de tuplas da consulta findPaginatedMotMov
     * 
     * @param idHistoricoBoleto
     * @return
     * @author Diego Umpierre - LMS
     * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
     */
	public Integer getRowCountMotMov(Long idHistoricoBoleto) {
		 
      
		
		
  	SqlTemplate histMotOco = new SqlTemplate();
    	
    	//consulta que retorna os dados da historico motivo ocorrencia, motivo ocorrencia banco
  		histMotOco.addProjection("motOcoBan.id_motivo_ocorrencia_banco");
  	
  	
  		histMotOco.addFrom("motivo_ocorrencia_banco motOcoBan " +
                 "   INNER JOIN historico_motivo_ocorrencia hisMotOco    " +
                 		" ON motOcoBan.id_motivo_ocorrencia_banco = hisMotOco.id_motivo_ocorrencia_banco  ");
    	histMotOco.addCriteria("hisMotOco.id_historico_boleto","=",idHistoricoBoleto);  
    		
		  
    	SqlTemplate hisBoletoMot = new SqlTemplate();
    	 
     	//consulta que retorna os dados da historico motivo ocorrencia, motivo ocorrencia 
    	hisBoletoMot.addProjection("motOco.id_motivo_ocorrencia");
    	
    	hisBoletoMot.addFrom("historico_boleto hisBol " +
                 "   INNER JOIN motivo_ocorrencia motOco   " +
                 		" ON motOco.id_motivo_ocorrencia = hisBol.id_motivo_ocorrencia  ");
    	hisBoletoMot.addCriteria("hisBol.id_historico_boleto","=",idHistoricoBoleto);  
    	
    	
    	String select = histMotOco.getSql() + 	
 						"\nUNION\n" +
 						hisBoletoMot.getSql();
    	
    	histMotOco.addCriteriaValue(hisBoletoMot.getCriteria());
        
        return getAdsmHibernateTemplate().getRowCountBySql(" from ("+select+")",
        													histMotOco.getCriteria());
		
				 
	}
    
	    
  
}
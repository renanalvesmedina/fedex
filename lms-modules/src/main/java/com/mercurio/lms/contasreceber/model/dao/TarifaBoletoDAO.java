package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.TarifaBoleto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaBoletoDAO extends BaseCrudDao<TarifaBoleto, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TarifaBoleto.class;
    }

      /**Consulta  que retorna dados das Tarifas de um boleto. 
     * Usado na tela:
     * Consultar Histórico das Ocorrências do Boleto
     * 
     *@author Diego Umpierre - LMS
     *@see com.mercurio.lms.contasreceber.action.ConsultarHistoricoOcorrenciasBoletoAction
     *
     *@param idBoleto identificador do boleto, findDef FindDefinition
     *
     *@return ResultSetPage com o resultado da consulta de acordo com os parametros.
     */
    public ResultSetPage findPaginatedTarBol(Long idBoleto,FindDefinition findDef) {
   
    	/* chama o metodo que monta os joins */
    	SqlTemplate sql = montaHqlJoinHistMov();
    	

    	/* incluido para que se consiga ordenar por um dominio */
//		sql.addFrom(","+DomainValue.class.getName()," as domainValue " +
//				    "	inner join fetch domainValue.domain as domain ");
		
		
    	/* Projection */
    	sql.addProjection("tarifaBoleto");
		
		
    	/* Critérios */
    	sql.addCriteria(" bol.idBoleto ","=",idBoleto); 
		
    	/* Ordenação */
    	sql.addOrderBy("ocorrenciaBanco.tpOcorrenciaBanco");
    	sql.addOrderBy("ocorrenciaBanco.nrOcorrenciaBanco");

        return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

    
    
    /**
     * Faz a contagem de tuplas da consulta findPaginatedTarBol
     * 
     * @param idBoleto
     * @return
     * @author Diego Umpierre - LMS
     * @see com.mercurio.lms.contasreceber.model.service.ConsultarHistoricoOcorrenciasBoletoService
     */
	public Integer getRowCountTarBol(Long idBoleto) {
		
		/* chama o metodo que monta os joins */
    	SqlTemplate sql = montaHqlJoinHistMov();
    	

    	   
    	/* Critérios */
    	sql.addCriteria(" bol.idBoleto ","=",idBoleto);
		List lstRegistros = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		return Integer.valueOf(lstRegistros.size());
	}
    

	
	/**
	 * Monta os joins necessarios para findPaginatedTarBol
	 * @return
	 */
	private SqlTemplate montaHqlJoinHistMov(){
		
	   	/*  Joins  */
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addInnerJoin(getPersistentClass().getName(), "tarifaBoleto");
    	sql.addInnerJoin(" tarifaBoleto.boleto", "bol");
    	sql.addInnerJoin(" fetch tarifaBoleto.ocorrenciaBanco", "ocorrenciaBanco");
    	
    	return sql;
	}
  

}
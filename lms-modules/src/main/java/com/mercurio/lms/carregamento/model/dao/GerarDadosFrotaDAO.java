package com.mercurio.lms.carregamento.model.dao;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Classe DAO responsável pelo processo de Geração de Dados de Estoque dos 
 * Dispositivos de Unitização de acordo com o item de número 05.03.01.12
 * @spring.bean
 */
public class GerarDadosFrotaDAO extends JdbcDaoSupport {


	/**
	 * Atualiza o id cliente formatado na coluna FAUNIT
	 *  
	 * @param nrFrota
	 * @param idcliente
	 */
	public void updateFAUNIT(String nrFrota, String idcliente) {
		getJdbcTemplate().update("UPDATE F1201 SET FAUNIT='"+idcliente+"' WHERE F1201.FAAPID = '"+nrFrota+"' ");
    }

	/**
	 * Atualiza o numero da matricula do funcionario e o id cliente na
	 * tabela PFCOMPL
	 * 
	 * @param nrMatricula
	 * @param idCliente
	 */
	public void updateCODCLIDED(String nrMatricula, String idCliente) {		
    	getJdbcTemplate().update("UPDATE PFCOMPL SET CODCLIDED='"+idCliente+"',CODCOLIGADA=1 WHERE PFCOMPL.CHAPA ='"+nrMatricula+"' ");    	
	}
    
}
package com.mercurio.lms.tabelaprecos.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class ReajusteTabelaClienteDAO extends JdbcDaoSupport {

	private static final String P_REAJUSTE_TABELA_CLIENTE = "{call P_REAJUSTE_TABELA_CLIENTE(?)}";

	/**
	 * Rotina chamada pelo Quartz para execução de tarefas agendadas
	 * @param idTabelaPreco
	 */
	public void executeJobReajusteCliente(Long idTabelaPreco) {

		CallableStatementCreatorFactory csf = new CallableStatementCreatorFactory(P_REAJUSTE_TABELA_CLIENTE);
        // adiciona os tipos de dados relacionados com os parametros a serem enviados para a funcao
        csf.addParameter(new SqlParameter("idTabelaPreco", Types.NUMERIC));

        // Monta o map com os parametros a serem pesquisados.
        Map in = new HashMap();
        in.put("idTabelaPreco", idTabelaPreco);

        getJdbcTemplate().call(csf.newCallableStatementCreator(in), new ArrayList());
	}
	
	public void setDadosSessaoBanco( String dadosSessao ) {

		CallableStatementCreatorFactory csf = new CallableStatementCreatorFactory("call DBMS_APPLICATION_INFO.SET_CLIENT_INFO (?)");
        // adiciona os tipos de dados relacionados com os parametros a serem enviados para a funcao
        csf.addParameter(new SqlParameter("dadosSessao", Types.VARCHAR));

        // Monta o map com os parametros a serem pesquisados.
        Map in = new HashMap();
        in.put("dadosSessao", dadosSessao);

        getJdbcTemplate().call(csf.newCallableStatementCreator(in), new ArrayList());
        
	}

}
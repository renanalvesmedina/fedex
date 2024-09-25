package com.mercurio.lms.vol.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mercurio.adsm.core.InfrastructureException;


/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplica��o atrav�s do suporte
 * ao Hibernate em conjunto com o Spring. N�o inserir documenta��o ap�s ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class VolDadosSessaoDao extends HibernateDaoSupport{
	
	
	/**
	 * Corre��o de bug na integra��o quando as baixas s�o enviadas para o corporativo. O id da filial na sess�o do banco
	 * n�o estava sendo atualizada.
	 * Define informa��es adicionais na sess�o atrav�s de uma chamada a um
	 * procedimento de banco de dados
	 * <code>DBMS_APPLICATION_INFO.SET_CLIENT_INFO</code>.
	 * 
	 * @param dadosFilial
	 */
	public void setDadosSessaoBanco( String dadosSessao ) {
		CallableStatement stmt = null;
		Connection conn = null;
			
		try { 
			conn = this.getHibernateTemplate().getSessionFactory().getCurrentSession().connection();
				
			stmt = conn.prepareCall("call DBMS_APPLICATION_INFO.SET_CLIENT_INFO (?)");
			stmt.setString(1, dadosSessao);
			stmt.execute();
		} catch (Exception e) {
			throw new InfrastructureException("Erro ao definir informa��es do usu�rio no contexto da conex�o.",	e);
		} finally {
			getHibernateTemplate().flush();
			JdbcUtils.closeStatement(stmt);
		}
	}

}

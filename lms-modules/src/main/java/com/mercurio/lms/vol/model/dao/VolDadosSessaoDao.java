package com.mercurio.lms.vol.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.mercurio.adsm.core.InfrastructureException;


/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class VolDadosSessaoDao extends HibernateDaoSupport{
	
	
	/**
	 * Correção de bug na integração quando as baixas são enviadas para o corporativo. O id da filial na sessão do banco
	 * não estava sendo atualizada.
	 * Define informações adicionais na sessão através de uma chamada a um
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
			throw new InfrastructureException("Erro ao definir informações do usuário no contexto da conexão.",	e);
		} finally {
			getHibernateTemplate().flush();
			JdbcUtils.closeStatement(stmt);
		}
	}

}

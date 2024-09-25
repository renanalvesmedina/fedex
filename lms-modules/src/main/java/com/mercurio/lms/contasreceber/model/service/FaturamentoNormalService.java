package com.mercurio.lms.contasreceber.model.service;

import java.sql.SQLException;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.LinhaSqlFaturamentoParam;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.faturamentoNormalService"
 */
public class FaturamentoNormalService extends FaturamentoService {
	
	protected Integer executeFaturamento(SqlFaturamentoParam param) throws SQLException {
		return executeSql(param, getSql(param));
	}

	protected void mountLinhaSqlFaturamento(LinhaSqlFaturamentoParam linha, Object[] element) throws SQLException {
	}
	
	public SqlTemplate getSql(SqlFaturamentoParam param) {
		return new GerarSqlFaturamentoNormalService().generateSql(param);
	}
	
	protected boolean groupByFatura(LinhaSqlFaturamentoParam linha, LinhaSqlFaturamentoParam linhaAnterior) {
		return false;
	}
}
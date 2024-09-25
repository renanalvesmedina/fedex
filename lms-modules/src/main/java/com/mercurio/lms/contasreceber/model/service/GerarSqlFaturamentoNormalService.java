package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;


public class GerarSqlFaturamentoNormalService extends GerarSqlFaturamentoService {
	
	protected void mountSqlSpecific(SqlTemplate sql, SqlFaturamentoParam sqlParam) {
		/*SqlTemplate sqlAgrupamento = new SqlTemplate();
		
		sqlAgrupamento.addProjection("1");
		sqlAgrupamento.addFrom("agrupamento_cliente", "agr");
		sqlAgrupamento.addJoin("agr.id_divisao_cliente", "dev.id_divisao_cliente");

		sql.addCustomCriteria("NOT EXISTS ("+sqlAgrupamento.getSql()+")");*/
		
		sql.addCustomCriteria(" NOT EXISTS( SELECT 1 FROM LIBERACAO_DOC_SERV WHERE LIBERACAO_DOC_SERV.TP_BLOQUEIO_LIBERADO = '"+ ConstantesExpedicao.LIBERACAO_CALCULO_CORTESIA +"' AND LIBERACAO_DOC_SERV.ID_DOCTO_SERVICO = doc.id_docto_servico ) ");
		
		sql.addCustomCriteria("(doc.TP_CALCULO_PRECO != '"+  ConstantesExpedicao.CALCULO_CORTESIA + "' or doc.TP_CALCULO_PRECO is null)");
		
	}
}
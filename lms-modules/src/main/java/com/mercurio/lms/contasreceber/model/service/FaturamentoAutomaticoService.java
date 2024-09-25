package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.lms.contasreceber.model.param.SqlFaturamentoParam;
import com.mercurio.lms.util.JTDateTimeUtils;



/**
 * @author Mickaël Jalbert
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.faturamentoAutomaticoService"
 */
public class FaturamentoAutomaticoService {
	
	private FaturamentoNormalService faturamentoNormalService;
	
	private FaturamentoEspecialService faturamentoEspecialService;

	/**
	 * Método chamado pelo 'Job Scheduler'. Gera as faturas e boletos dos documentos de serviço pendente no sistema.
	 * 
	 * author Mickaël Jalbert
	 * @since 25/10/2006
	 */
	public void executeFaturamentoAutomatico() {
		
		SqlFaturamentoParam param1 = new SqlFaturamentoParam();
		param1.setDtEmissao(JTDateTimeUtils.getDataAtual());
		param1.setDtFinalEmissao(JTDateTimeUtils.getDataAtual());
		
		SqlFaturamentoParam param2 = new SqlFaturamentoParam();
		param2.setDtEmissao(JTDateTimeUtils.getDataAtual());
		param2.setDtFinalEmissao(JTDateTimeUtils.getDataAtual());

		faturamentoEspecialService.generateFaturamento(param1);
		faturamentoNormalService.generateFaturamento(param2);
	}
	
	
	public String executeFaturamentoAutomaticoSql() {
		
		SqlFaturamentoParam param1 = new SqlFaturamentoParam();
		param1.setDtEmissao(JTDateTimeUtils.getDataAtual());
		param1.setDtFinalEmissao(JTDateTimeUtils.getDataAtual());
		
		SqlFaturamentoParam param2 = new SqlFaturamentoParam();
		param2.setDtEmissao(JTDateTimeUtils.getDataAtual());
		param2.setDtFinalEmissao(JTDateTimeUtils.getDataAtual());

//		faturamentoEspecialService.generateFaturamento(param1);
		String sql = faturamentoNormalService.getSql(param2).getSql();
		
		Object[] criteria = faturamentoNormalService.getSql(param2).getCriteria();
		
		sql = sql.replace(String.valueOf(param2.getDtEmissao()), "to_date('"+param2.getDtEmissao()+"', 'YYYY-MM-DD')");
		
		for(int i=0; i<criteria.length; i++) {
			if(criteria[i] instanceof org.joda.time.YearMonthDay) {
				sql = sql.replaceFirst("\\?", "to_date('"+criteria[i]+"', 'YYYY-MM-DD')");
			}else if(criteria[i] instanceof Long) {
				sql = sql.replaceFirst("\\?", String.valueOf(criteria[i]));
			}else {
				sql = sql.replaceFirst("\\?", "'"+criteria[i]+"'");
			}
		}
		
		return sql;
	}
	
	public void setFaturamentoEspecialService(
			FaturamentoEspecialService faturamentoEspecialService) {
		this.faturamentoEspecialService = faturamentoEspecialService;
	}

	public void setFaturamentoNormalService(
			FaturamentoNormalService faturamentoNormalService) {
		this.faturamentoNormalService = faturamentoNormalService;
	}
	
	
}
package com.mercurio.lms.contasreceber.action;


import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.report.EmitirNotaDebitoCobrancaJuroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Generated by: ADSM ActionGenerator
 * 
 * @author Diego Umpierre
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirNotaDebitoCobrancaJurosAction"
 */

public class EmitirNotaDebitoCobrancaJurosAction {

	private ReportExecutionManager reportExecutionManager;
	private EmitirNotaDebitoCobrancaJuroService emitirNotaDebitoCobrancaJuroService;

	// executa o execute da super, seta a service default IoC do Spring
	public void setEmitirNotaDebitoCobrancaJuroService(EmitirNotaDebitoCobrancaJuroService emitirNotaDebitoCobrancaJuroService) {
		this.emitirNotaDebitoCobrancaJuroService = emitirNotaDebitoCobrancaJuroService;
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public String execute(TypedFlatMap params) throws Exception {
		return this.reportExecutionManager.generateReportLocator(this.emitirNotaDebitoCobrancaJuroService, params);
	}
	
	/**
	 * Busca a filial do usu�rio 
	 * @param tfm Crit�rios de pesquisa
	 * @return TypedFlatMap com dados de filial 
	 */
	public TypedFlatMap findFilialUsuario(TypedFlatMap tfm){
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
			
		tfm = new TypedFlatMap();
		
		tfm.put("filial.idFilial",filialUsuario.getIdFilial());
		tfm.put("filial.sgFilial",filialUsuario.getSgFilial());
		tfm.put("filial.pessoa.nmFantasia",filialUsuario.getPessoa().getNmFantasia());
		
		
		return tfm;
	}
	
	
	
}

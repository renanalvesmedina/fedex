package com.mercurio.lms.workflow.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.workflow.report.EmitirAprovacaoAprovadorService;
/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.emitirAprovacaoAprovadorAction"
 */

public class EmitirAprovacaoAprovadorAction extends ReportActionSupport {
	
	public void setEmitirAprovacaoAprovadorService(EmitirAprovacaoAprovadorService aprovacaoAprovadorService) {
		this.reportServiceSupport = aprovacaoAprovadorService;
	}

}

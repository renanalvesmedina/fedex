package com.mercurio.lms.configuracoes.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.configuracoes.report.EmitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoAction"
 */

public class EmitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoAction extends ReportActionSupport {
	
	public void setEmitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService(EmitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService) {
		this.reportServiceSupport = emitirOcorrenciaAtualizacaoAutomaticaEnderecoPadraoService;
	}
	
}

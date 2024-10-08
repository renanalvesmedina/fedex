package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.contasreceber.report.EmitirChequesTransferidosFilialCobrancaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirChequesTransferidosFilialCobrancaAction"
 */

public class EmitirChequesTransferidosFilialCobrancaAction extends ReportActionSupport {

	public void setEmitirChequesTransferidosFilialCobrancaService(EmitirChequesTransferidosFilialCobrancaService emitirChequesTransferidosFilialCobrancaService) {
		this.reportServiceSupport = emitirChequesTransferidosFilialCobrancaService;
	}
	
    /**
     * Busca a data atual
     * @return String data atual
     */
    public String findDataAtual(){
    	return JTFormatUtils.format(JTDateTimeUtils.getDataAtual(), JTFormatUtils.YEARMONTHDAY);
    } 

}
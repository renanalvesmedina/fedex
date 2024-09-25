package com.mercurio.lms.seguros.report;

import java.io.File;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.report.emitirProtocoloDocEntreguesAction"
 */
public class EmitirProtocoloDocEntreguesAction extends ReportActionSupport {

	EmitirProtocoloDocEntreguesEntregaService emitirProtocoloDocEntreguesEntregaService;
	EmitirProtocoloDocEntreguesRecebimentoService emitirProtocoloDocEntreguesRecebimentoService;
	
	
	public File executeReport(TypedFlatMap tfm) throws Exception {
		
		if(tfm.getList("selectedIds") == null || tfm.getList("selectedIds").size() == 0) {
			throw new BusinessException("LMS-22003");
		}
		
		if ("R".equals(tfm.getString("tpEntregaRecebimento"))) {
			this.reportServiceSupport = this.emitirProtocoloDocEntreguesRecebimentoService;
			return this.emitirProtocoloDocEntreguesRecebimentoService.executeReport(tfm);
		}
		this.reportServiceSupport = this.emitirProtocoloDocEntreguesEntregaService;
		return this.emitirProtocoloDocEntreguesEntregaService.executeReport(tfm); 
	}


	public void setEmitirProtocoloDocEntreguesEntregaService(
			EmitirProtocoloDocEntreguesEntregaService emitirProtocoloDocEntreguesEntregaService) {
		this.emitirProtocoloDocEntreguesEntregaService = emitirProtocoloDocEntreguesEntregaService;
	}


	public void setEmitirProtocoloDocEntreguesRecebimentoService(
			EmitirProtocoloDocEntreguesRecebimentoService emitirProtocoloDocEntreguesRecebimentoService) {
		this.emitirProtocoloDocEntreguesRecebimentoService = emitirProtocoloDocEntreguesRecebimentoService;
	}
	
	public void setDefaultService(EmitirProtocoloDocEntreguesEntregaService emitirProtocoloDocEntreguesEntregaService) {
		this.reportServiceSupport = emitirProtocoloDocEntreguesEntregaService;
	}
	

}

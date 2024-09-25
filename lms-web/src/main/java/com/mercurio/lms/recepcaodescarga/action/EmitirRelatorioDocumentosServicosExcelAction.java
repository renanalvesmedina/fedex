package com.mercurio.lms.recepcaodescarga.action;

import java.io.File;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.recepcaodescarga.report.EmitirRelatorioDocumentosServicosExcelService;

public class EmitirRelatorioDocumentosServicosExcelAction extends ReportActionSupport {
	@Override
	public File execute(TypedFlatMap parameters) throws Exception {
		return super.execute(parameters);
	}
	
	public void setDefaultService(EmitirRelatorioDocumentosServicosExcelService emitirRelatorioDocumentosServicosExcelService) {
		this.reportServiceSupport = emitirRelatorioDocumentosServicosExcelService;
	}
}

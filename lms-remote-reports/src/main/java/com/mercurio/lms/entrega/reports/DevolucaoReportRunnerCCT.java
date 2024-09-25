package com.mercurio.lms.entrega.reports;

import java.io.File;

import org.apache.poi.hssf.usermodel.HSSFSheet;

public class DevolucaoReportRunnerCCT extends ExcelRemoteReportRunnerCCT {

	public DevolucaoReportRunnerCCT(byte[] byteArrayTemplate, String fileName, File reportOutputDir) {
		super(byteArrayTemplate, fileName, reportOutputDir);
	}

	@Override
	protected void fillReportDadosGerais(HSSFSheet sheet) {
		// Este relat�rio n�o possui dados gerais, somente itens.
	}

	@Override
	protected void fillReportDadosTotalizadores(HSSFSheet sheet) {
		// Este relat�rio n�o necessita de totalizadores.
	}
}
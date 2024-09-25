package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClienteMergerPDF extends TemplatePdf {

	private Logger log = LogManager.getLogger(this.getClass());

	@Override
	public void addContent() throws Exception {
	}

	public void merge(List<TemplatePdf> templatesPdf) {
		try {
			this.file = File.createTempFile("report-listadepreco-merger-"+ System.currentTimeMillis(), ".pdf",new ReportExecutionManager().generateOutputDir());
			this.document = new Document(PageSize.A4, 36, 36, 54, 36);
			writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();

			for (TemplatePdf template : templatesPdf) {
				PdfReader reader = new PdfReader(template.getFile().getPath());
				int n = reader.getNumberOfPages();
				int i = 0;
				document.setPageSize(template.landsscape ? PageSize.A4.rotate() :PageSize.A4);
				while (i < n) {
					document.newPage();
					i++;
					PdfContentByte cb = writer.getDirectContent();
					PdfImportedPage page = writer.getImportedPage(reader, i);
					int rotation = reader.getPageRotation(i);
					if (rotation == 90 || rotation == 270) {
						cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
					} else {
						cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
					}
				}
			}

			document.close();
		} catch (Exception e) {
			log.error(e);
		}
	}
}

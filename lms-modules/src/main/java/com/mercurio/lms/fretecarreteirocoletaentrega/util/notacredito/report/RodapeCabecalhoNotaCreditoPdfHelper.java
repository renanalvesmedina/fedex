package com.mercurio.lms.fretecarreteirocoletaentrega.util.notacredito.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.mercurio.lms.vendas.util.ImageUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RodapeCabecalhoNotaCreditoPdfHelper extends PdfPageEventHelper {

	private static final String REPORT_LOGO = "mercurio_logo.jpg";

	private Logger log = LogManager.getLogger(this.getClass());
	private Boolean preview;	
	private PdfTemplate template;	
	private Map<String, String> headerData;
		
	public RodapeCabecalhoNotaCreditoPdfHelper(Map<String, String> headerData, Boolean preview) {		
		this.headerData = headerData;		
		this.preview = preview;
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		template = writer.getDirectContent().createTemplate(30, 16);
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		try {
			addCabecalho(document);				
		} catch (Exception e) {
			log.error(e);
		}
	}

	private String getReportTitle(Map<String, String> notaCredito) {
		StringBuilder title = new StringBuilder();
		
		if(preview){
			title.append(String.format("%s %s ", 
					TemplateNotaCreditoPadraoPDF.getLabel("previa"), 
					TemplateNotaCreditoPadraoPDF.getLabel("da")));
		}
		
		title.append(String.format("%s %s %s", 
				TemplateNotaCreditoPadraoPDF.getLabel("reportTitle"), 
				TemplateNotaCreditoPadraoPDF.getLabel("de"), 
				notaCredito.get("tipoDescription")));
				
		return title.toString();
	}
	
	private void addCabecalho(Document document) throws BadElementException,
			MalformedURLException, IOException, DocumentException {
		addLogo(document);
		
		PdfPTable pdfPTable = null;
		
		if(preview){
			 pdfPTable = new PdfPTable(new float[]{1.25f,0.5f,0.5f});
		} else {
			 pdfPTable = new PdfPTable(new float[]{1.25f,0.5f,0.25f,0.25f});
		}
		
		pdfPTable.setWidthPercentage(100f);
		pdfPTable.setSpacingAfter(10f);
		
		PdfPCell cTitulo = new PdfPCell(new Phrase(getReportTitle(headerData),TemplateNotaCreditoPadraoPDF.subFont12b));
		cTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		cTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cTitulo.setBorderWidthBottom(0.5f);
		cTitulo.setBorderWidthLeft(0.5f);
		cTitulo.setBorderWidthRight(0.5f);
		cTitulo.setBorderWidthTop(0.5f);
		pdfPTable.addCell(cTitulo);
		
		PdfPTable pTableNota = addLinha1e2(TemplateNotaCreditoPadraoPDF.getLabel("nota"), MapUtils.getString(headerData, "nota"));
				
		PdfPCell cNota = new PdfPCell(pTableNota);			
		cNota.setHorizontalAlignment(Element.ALIGN_CENTER);
		cNota.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cNota.setBorderWidthBottom(0.5f);
		cNota.setBorderWidthLeft(0.5f);
		cNota.setBorderWidthRight(0.5f);
		cNota.setBorderWidthTop(0.5f);
		pdfPTable.addCell(cNota);	
		
		PdfPTable pTableDataGeracao = addLinha1e2(TemplateNotaCreditoPadraoPDF.getLabel("dataDeGeracao"), MapUtils.getString(headerData, "dhGeracao"));
		
		PdfPCell cdataGeracao = new PdfPCell(pTableDataGeracao);
		cdataGeracao.setBorder(PdfPCell.NO_BORDER);
		cdataGeracao.setHorizontalAlignment(Element.ALIGN_CENTER);
		cdataGeracao.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cdataGeracao.setBorderWidthBottom(0.5f);
		cdataGeracao.setBorderWidthLeft(0.5f);
		cdataGeracao.setBorderWidthRight(0.5f);
		cdataGeracao.setBorderWidthTop(0.5f);
		
		pdfPTable.addCell(cdataGeracao);
		
		if(!preview){		
			PdfPTable pTableDataEmissao = addLinha1e2(TemplateNotaCreditoPadraoPDF.getLabel("dataDeEmissao"), MapUtils.getString(headerData, "dhEmissao"));
			
			PdfPCell cdataEmissao = new PdfPCell(pTableDataEmissao);
			cdataEmissao.setBorder(PdfPCell.NO_BORDER);
			cdataEmissao.setHorizontalAlignment(Element.ALIGN_CENTER);
			cdataEmissao.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cdataEmissao.setBorderWidthBottom(0.5f);
			cdataEmissao.setBorderWidthLeft(0.5f);
			cdataEmissao.setBorderWidthRight(0.5f);
			cdataEmissao.setBorderWidthTop(0.5f);
			
			pdfPTable.addCell(cdataEmissao);
		}
		
		document.add(pdfPTable);
	}

	private void addLogo(Document document) throws BadElementException,
			MalformedURLException, IOException, DocumentException {		
		Image image = ImageUtil.getImage("/images/" + REPORT_LOGO);
		
		if(image != null){
			image.scalePercent(22f);
			image.setAbsolutePosition(30f, document.getPageSize().getHeight() - 72.5f);
			
			document.add(image);			
		}
	}

	private PdfPTable addLinha1e2(String linha1,String linha2) {
		PdfPTable pTableNota = new PdfPTable(1);
		pTableNota.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		pTableNota.setSpacingAfter(5f);
		pTableNota.setSpacingBefore(5f);
		
		PdfPCell cell = new PdfPCell(new Phrase(linha1,TemplateNotaCreditoPadraoPDF.subFont7b));
		setDefaultConfig(cell);
		pTableNota.addCell(cell);
		
		PdfPCell cell2 = new PdfPCell(new Phrase(linha2,TemplateNotaCreditoPadraoPDF.subFont7));
		setDefaultConfig(cell2);
		pTableNota.addCell(cell2);
		return pTableNota;
	}

	private void setDefaultConfig(PdfPCell cell) {
		cell.setBorderWidthBottom(0);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthTop(0);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}
		
	public void onEndPage(PdfWriter writer, Document document) {
		if(preview) {
			PdfContentByte canvas = writer.getDirectContentUnder();
        	ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, 
        			new Phrase(TemplateNotaCreditoPadraoPDF.getLabel("previa"), new Font(FontFamily.HELVETICA, 200, Font.NORMAL, BaseColor.LIGHT_GRAY)), 450, 270, 45);
		}
		
		float[] colunas = { 1f };
		PdfPTable rodape = new PdfPTable(1);
		rodape.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		try {
			rodape.setWidths(colunas);
		} catch (DocumentException e) {
			log.error(e);
		}
		
		rodape.setTotalWidth(document.getPageSize().getWidth() - 64);

		PdfPCell cell = new PdfPCell(new Phrase(TemplateNotaCreditoPadraoPDF.getLabel("tnt"), TemplateNotaCreditoPadraoPDF.subFont6b));
		cell.setBorder(PdfPCell.TOP);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell cell2 = new PdfPCell(new Phrase(TemplateNotaCreditoPadraoPDF.getLabel("site"), TemplateNotaCreditoPadraoPDF.subFont6b));
		cell2.setBorder(PdfPCell.TOP);
		cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);

		PdfPTable info = new PdfPTable(2);
		info.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		info.addCell(cell);
		info.addCell(cell2);
		
		rodape.addCell(info);

		float[] cl = { 1f, 1f };
		PdfPTable pdfPTabletotal = new PdfPTable(cl);
		pdfPTabletotal.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		pdfPTabletotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
		PdfPCell cellPagina = new PdfPCell(new Phrase(String.format("%s %d %s ", 
				TemplateNotaCreditoPadraoPDF.getLabel("pagina"), 
				writer.getPageNumber(), 
				TemplateNotaCreditoPadraoPDF.getLabel("de")), TemplateNotaCreditoPadraoPDF.subFont6));
		cellPagina.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cellPagina.setBorder(0);

		pdfPTabletotal.addCell(cellPagina);
		
		PdfPCell cellTotal = null;

		try {
			Image image = Image.getInstance(template);
			image.scalePercent(50);
			cellTotal = new PdfPCell(image);
			cellTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellTotal.setPadding(1f);
			cellTotal.setBorder(0);
			pdfPTabletotal.addCell(cellTotal);				
		} catch (BadElementException e) {
			log.error(e);
		}
						
		PdfPCell celtotal = new PdfPCell(pdfPTabletotal);
		celtotal.setBorder(PdfPCell.NO_BORDER);
		celtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
		rodape.addCell(celtotal);
		
		rodape.writeSelectedRows(0, -1, 34, 34, writer.getDirectContent());
	}
	
	public void onCloseDocument(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(template, Element.ALIGN_LEFT,new Phrase(String.valueOf(writer.getPageNumber() - 1)),2, 2, 0);
	}
}

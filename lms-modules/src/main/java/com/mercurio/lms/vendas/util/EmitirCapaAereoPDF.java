package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirCapaAereoPDF extends TemplatePdf{

	private Logger log = LogManager.getLogger(this.getClass());
	
	public EmitirCapaAereoPDF(Map<String, String> dados) {
		
		this.dados = dados;
		
		try {
			File file = File.createTempFile("report-capaAereo-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			
			this.file = file;
			this.landsscape = true;
			this.document = new Document(landsscape ? PageSize.A4.rotate() : PageSize.A4, 30, 30, 30, 36);
			try {
				writer = PdfWriter.getInstance(document,new FileOutputStream(file) );
			} catch (FileNotFoundException e) {
				log.error(e);
			} catch (DocumentException e) {
				log.error(e);
			}
			
			document.open();
			
			try{
				addContent();
				document.close();
			} catch (Exception e) {
				addErro(this.getClass(),e);
			}
			
		} catch (IOException e) {
			log.error(e);
		}
	}
		
	public void addContent() throws DocumentException  {
		
		try {
			Image image = ImageUtil.getImage("/images/aviao_capa.png");
			
			if(image != null){
				image.scalePercent(86f, 67f);
				image.setAbsolutePosition(30f,70f);
				document.add(image);
			}
			
			Image image2 = ImageUtil.getImage("/images/the_people_network.png");
			if(image2 != null){
				image2.scalePercent(110f, 105f);
				image2.setAbsolutePosition(635f,70f);
				document.add(image2);
			}
			
			getDadosCapa();
			
		}catch (Exception e) {
				log.error(e);
		}
	}
	
	public void getDadosCapa() throws DocumentException {
		String cliente = dados.get("NM_CLIENTE"); 
		String executivoVendas = dados.get("NM_PROMOTOR");
		String dataVigenciaInicial = dados.get("DT_TABELA_VIGENCIA_INICIAL");
		String dataVigenciaFinal = dados.get("DT_VALIDADE_PROPOSTA");
		
		float[] f1 = {10f};
		PdfPTable  tablePrincipal = new PdfPTable(f1);
		tablePrincipal.setWidthPercentage(100f);
		
		PdfPCell cEspaco= new PdfPCell(new Phrase(""));
		cEspaco.setBorder(0);
		
		PdfPCell branco = new PdfPCell(new Phrase(""));
		branco.setBorder(0);
		branco.setFixedHeight(350f);
		tablePrincipal.addCell(branco);
		
		PdfPCell cCliente = getCellSemBorda(TemplatePdf.getDefaultResourceBundle().getString("cliente") + cliente,false);
		tablePrincipal.addCell(cCliente);
		tablePrincipal.addCell(cEspaco);
		
		PdfPCell cModal = getCellSemBorda(TemplatePdf.getDefaultResourceBundle().getString("modalAereoCapa"),false);
		tablePrincipal.addCell(cModal);
		tablePrincipal.addCell(cEspaco);
		
		PdfPCell  cExecutivoVendas = getCellSemBorda(TemplatePdf.getDefaultResourceBundle().getString("executivoVendas") + executivoVendas,false);
		tablePrincipal.addCell(cExecutivoVendas);
		tablePrincipal.addCell(cEspaco);
		
		PdfPCell  cDataVigenciaInicial = getCellSemBorda(TemplatePdf.getDefaultResourceBundle().getString("vigenciaProposta") + dataVigenciaInicial + "  a  " + dataVigenciaFinal,false);
		tablePrincipal.addCell(cDataVigenciaInicial);
		
		PdfPCell rodape = new PdfPCell(new Phrase(TemplatePdf.getDefaultResourceBundle().getString("rodapeLms"), FONT_6B));
		rodape.setHorizontalAlignment(Element.ALIGN_LEFT);
		rodape.setBorder(Rectangle.TOP);
		PdfPCell branco2 = new PdfPCell(new Phrase(""));
		branco2.setBorder(0);
		branco2.setFixedHeight(100f);
		tablePrincipal.addCell(branco2);
		tablePrincipal.addCell(rodape);
		
		document.add(tablePrincipal);
	}
	
	private PdfPCell getCellSemBorda(String var, boolean bold) {
		PdfPCell  cel = new PdfPCell(new Phrase(var, FONT_10B));
		cel.setBorder(0);
		return cel;
	}
	
}

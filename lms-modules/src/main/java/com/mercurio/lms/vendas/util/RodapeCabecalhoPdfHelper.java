package com.mercurio.lms.vendas.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class RodapeCabecalhoPdfHelper extends PdfPageEventHelper {

	private static Font font6 = new Font(Font.FontFamily.HELVETICA, 6,Font.NORMAL);
	private static Font font6b = new Font(Font.FontFamily.HELVETICA, 6,Font.BOLD);
	private Logger log = LogManager.getLogger(this.getClass());
	private String usuario;
	private String data;
	private boolean A4_LANDSCAPE = true;
	private boolean rodape = true;


	PdfTemplate total;
	public RodapeCabecalhoPdfHelper(boolean landsscape) {
		this.A4_LANDSCAPE = landsscape;
	}

	public void onOpenDocument(PdfWriter writer, Document document) {
		total = writer.getDirectContent().createTemplate(30, 16);
	}
	
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		Date dataHora = new Date();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dfHora = new SimpleDateFormat("HH:mm");
		Image image = null;
		String logo = "logo_report_cabecalho.png";
		try {
			if(usuario == null || "".equals(usuario)){
				logo = "logo_fedex_report2.jpg";
				image = ImageUtil.getImage("/images/" + logo);
				if(image != null){
					image.scalePercent(A4_LANDSCAPE ? 52.2f : 35.6f);
					image.setAbsolutePosition(30f, document.getPageSize().getHeight() - 80f);
					document.add(image);
				}
				Paragraph paragraph = new Paragraph(new Phrase(" ",font6));
				paragraph.setSpacingAfter(15f);
				document.add(paragraph);
			}else{
				image = ImageUtil.getImage("/images/" + logo);
				
				if(image != null){
					image.scalePercent(10f);
				}
			}
	
			if(usuario == null || "".equals(usuario)){
				Paragraph p2 = new Paragraph(new Phrase(data,font6)); 
				p2.setAlignment(Element.ALIGN_RIGHT);
				p2.setSpacingBefore(20f);
				document.add(p2);
			}else{				
				float[] w = {2f,1f};
				PdfPTable dados = new PdfPTable(w);
				dados.setWidthPercentage(100f);
				PdfPTable dadosU = new PdfPTable(1);
				
				PdfPCell imgCell = new PdfPCell(image);				
				imgCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				imgCell.setBorder(0);
				dados.addCell(imgCell);
				
				PdfPCell dataCell = new PdfPCell(new Phrase(TemplatePdf.getDefaultResourceBundle().getString("dataImpressao")+df.format(dataHora),font6));
				dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				dataCell.setBorder(0);
				dataCell.setPaddingLeft(50f);
				
				dadosU.addCell(dataCell);
				
				PdfPCell horaCell = new PdfPCell(new Phrase(TemplatePdf.getDefaultResourceBundle().getString("horaImpressao")+dfHora.format(dataHora),font6));
				horaCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				horaCell.setBorder(0);
				horaCell.setPaddingLeft(50f);
				
				dadosU.addCell(horaCell);
				
				PdfPCell usuarioCell = new PdfPCell(new Phrase(TemplatePdf.getDefaultResourceBundle().getString("usuario")+getUsuario(),font6));
				usuarioCell.setHorizontalAlignment(Element.ALIGN_LEFT);
				usuarioCell.setBorder(0);
				usuarioCell.setPaddingLeft(50f);
				
				dadosU.addCell(usuarioCell);
				
				PdfPCell cellDados = new PdfPCell(dadosU);
				cellDados.setBorder(0);
				
				dados.addCell(cellDados);
				
				document.add(dados);
			}
				PdfPTable data = new PdfPTable(1);
				data.setWidthPercentage(100f);
				PdfPCell c3 = new PdfPCell();
				c3.setHorizontalAlignment(Element.ALIGN_LEFT);
				c3.setBorderWidthBottom(0.5f);
				c3.setBorderWidthLeft(0);
				c3.setBorderWidthRight(0);
				c3.setBorderWidthTop(0);
				data.addCell(c3);
				data.setSpacingAfter(5f);			
				
				document.add(data);
			
		} catch (Exception e) {
			log.error(e);
		}
	}
	public void onEndPage(PdfWriter writer, Document document) {
		if(!isRodape()){
			return;
		}
		
		float[] colunas = { 1f, 1f,1f};
		PdfPTable rodape = new PdfPTable(3);
		try {
			rodape.setWidths(colunas);
		} catch (DocumentException e) {
			log.error(e);
		}
		rodape.setTotalWidth(document.getPageSize().getWidth() - 64);

		PdfPCell cell = new PdfPCell(new Phrase(TemplatePdf.getDefaultResourceBundle().getString("rodapeLms"), font6b));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPCell cell1 = new PdfPCell(new Phrase("", font6b));

		PdfPCell cell2 = new PdfPCell(new Phrase("", font6b));
		cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);

		cell.setBorder(Rectangle.TOP);
		cell2.setBorder(Rectangle.TOP);
		cell1.setBorder(Rectangle.TOP);

		rodape.addCell(cell);
		rodape.addCell(cell1);
		rodape.addCell(cell2);

		PdfPCell cellPagina = new PdfPCell(new Phrase(String.format(TemplatePdf.getDefaultResourceBundle().getString("pagina"), writer.getPageNumber()), font6));
		cellPagina.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cellPagina.setColspan(2);
		cellPagina.setBorder(0);

		PdfPCell cellTotal = null;
		rodape.addCell(cellPagina);

		try {
			Image image = Image.getInstance(total);
			image.scalePercent(50);
			cellTotal = new PdfPCell(image);
			cellTotal.setHorizontalAlignment(Element.ALIGN_LEFT);
			cellTotal.setPadding(1f);
			cellTotal.setBorder(0);
			rodape.addCell(cellTotal);
		} catch (BadElementException e) {
			log.error(e);
		}
		rodape.writeSelectedRows(0, -1, 34, 34, writer.getDirectContent());
	}
	
	public void onCloseDocument(PdfWriter writer, Document document) {
		if(!isRodape()){
			return;
		}
		ColumnText.showTextAligned(total, Element.ALIGN_LEFT,new Phrase(String.valueOf(writer.getPageNumber() - 1)),2, 2, 0);
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getData() {
		return data;
	}
	public void setRodape(boolean rodape) {
		this.rodape = rodape;
}
	public boolean isRodape() {
		return rodape;
	}
}

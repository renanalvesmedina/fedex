package com.mercurio.lms.fretecarreteirocoletaentrega.util.notacredito.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.MapUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.Cell;
import com.mercurio.adsm.framework.report.ReportExecutionManager;

public class TemplateNotaCreditoPadraoPDF {
	
	/*
	 * Fontes utilizadas no report. 
	 */
	protected static Font subFont6 = new Font(Font.FontFamily.HELVETICA, 6,Font.NORMAL);
	protected static Font subFont7 = new Font(Font.FontFamily.HELVETICA, 7,Font.NORMAL);
	protected static Font subFont7b = new Font(Font.FontFamily.HELVETICA, 7,Font.BOLD);
	protected static Font subFont6b = new Font(Font.FontFamily.HELVETICA, 6,Font.BOLD);
	protected static Font subFont8 = new Font(Font.FontFamily.HELVETICA, 8,Font.NORMAL);
	protected static Font subFont8b = new Font(Font.FontFamily.HELVETICA, 8,Font.BOLD);
	protected static Font subFont2 = new Font(Font.FontFamily.HELVETICA, 2,Font.ITALIC);
	protected static Font subFont12b = new Font(Font.FontFamily.HELVETICA, 12,Font.BOLD);

	/*
	 * Documento 
	 */
	private Document document;
	private File file;
	
	protected Boolean preview;
	
	private static final String REPORT_NAME = "report-nota-credito-padrao";	
	
	public TemplateNotaCreditoPadraoPDF(Boolean preview){
		this.preview = preview;
	}
	
	/**
	 * Cria um documento no tamanho definido.
	 * 
	 * @param tamanho
	 */
	protected void createDocument(Rectangle tamanho) {
		document = new Document(tamanho, 36, 36, 25, 36);	
	}
	
	/**
	 * Adiciona event.
	 * 
	 * @param headerData
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	protected void addPageEvent(Map<String, String> headerData)
			throws IOException, DocumentException {
		file = File.createTempFile(REPORT_NAME + "_" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
		
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
						
		writer.setPageEvent(new RodapeCabecalhoNotaCreditoPdfHelper(headerData, preview));		
	}
	
	/**
	 * Abre documento para escrita.
	 */
	protected void openDocument(){
		document.open();
	}
	
	/**
	 * Fecha documento para escrita.
	 */
	protected void closeDocument(){
		document.close();
	}
	
	/**
	 * Retorna arquivo do report.
	 * 
	 * @return File
	 */
	protected File getDocumentFile(){
		return file;
	}
	
	/**
	 * Retorna documento do report.
	 * 
	 * @return Document
	 */
	protected Document getDocument(){
		return this.document;
	}
	
	protected PdfPTable getAssinaturaCell(String key) {
		PdfPTable tabelaInterna = new PdfPTable(1);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		tabelaInterna.setWidthPercentage(100f);	
		
		PdfPCell c1 = new PdfPCell(new Phrase(getLabel(key),subFont8b));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_TOP);
		c1.setBorderWidthBottom(0f);
		c1.setBorderWidthLeft(0f);
		c1.setBorderWidthRight(0f);
		c1.setBorderWidthTop(1f);		
		c1.setBorderColorTop(BaseColor.BLACK);
		
		tabelaInterna.addCell(c1);
		
		return tabelaInterna;
	}

	protected PdfPTable getPremioTable(Map<String, String> premio) throws DocumentException {
		PdfPTable tabelaPrincipal = new PdfPTable(1);
		tabelaPrincipal.setWidthPercentage(100f);				
		tabelaPrincipal.getDefaultCell().setPadding(2);
		tabelaPrincipal.setSpacingBefore(5f);
				
		PdfPTable tabelaInterna = new PdfPTable(8);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		tabelaInterna.setWidthPercentage(100f);
		tabelaInterna.getDefaultCell().setPadding(0);
		tabelaInterna.getDefaultCell().setPaddingLeft(2f);
		
		PdfPCell c2 = new PdfPCell(new Phrase(getLabel("valorPremio"),subFont8b));
		setDefaultConfig(c2);
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);		
		c2.setPaddingLeft(2f);
		c2.setColspan(8);
		
		tabelaInterna.addCell(c2);
		
		tabelaInterna.addCell(addChaveValorPremio(getLabel("cte"), premio.get("pcPremioCte")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("evento"), premio.get("pcPremioEvento")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("saida"), premio.get("pcPremioSaida")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("diaria"), premio.get("pcPremioDiaria")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("volume"), premio.get("pcPremioVolume")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("freteBruto"), premio.get("pcPremioFreteBruto")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("freteLiquido"), premio.get("pcPremioFreteLiq")));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("mercadoria"), premio.get("pcPremioMercadoria")));
		
		tabelaPrincipal.addCell(tabelaInterna);
		
		return tabelaPrincipal;
	}
	
	private PdfPTable addChaveValorPremio(String chave, String valor) {
		PdfPTable pdfPTable = new PdfPTable(1);
		pdfPTable.setWidthPercentage(100f);		
		
		PdfPCell c1 = new PdfPCell(new Phrase(chave,subFont7b));
		setDefaultConfig(c1);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setPadding(0);
		pdfPTable.addCell(c1);
		
		PdfPCell c2 = new PdfPCell(new Phrase(valor,subFont7));
		setDefaultConfig(c2);
		c2.setHorizontalAlignment(Element.ALIGN_CENTER);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);		
		c2.setPadding(0);
		pdfPTable.addCell(c2);
		return pdfPTable;
	}	
	
	protected PdfPTable getTotalTable(Map<String, String> notaCredito) {
		PdfPTable tabelaPrincipal = new PdfPTable(1);
		tabelaPrincipal.setWidthPercentage(100f);				
		tabelaPrincipal.getDefaultCell().setPadding(2);
		
		tabelaPrincipal.setSpacingBefore(5f);
		
		PdfPTable tabelaInterna = new PdfPTable(2);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		tabelaInterna.setWidthPercentage(100f);	
		tabelaInterna.getDefaultCell().setPaddingLeft(2f);
		
		tabelaInterna.addCell(new Phrase(getLabel("valorTotalDaNotaDeCredito"),subFont7b));
		tabelaInterna.addCell(addCellDireita(notaCredito.get("vlTotal")));
		
		tabelaPrincipal.addCell(tabelaInterna);
		return tabelaPrincipal;
	}
	
	protected PdfPTable getObservacaoTable(Map<String, String> notaCredito) {
		PdfPTable tabelaPrincipal = new PdfPTable(1);
		tabelaPrincipal.setWidthPercentage(100f);				
		tabelaPrincipal.getDefaultCell().setPadding(2);
		
		tabelaPrincipal.setSpacingBefore(5f);
		
		PdfPTable tabelaInterna = new PdfPTable(1);
		tabelaInterna.setWidthPercentage(100f);				
		tabelaInterna.getDefaultCell().setPadding(2);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		tabelaInterna.addCell(new Phrase(getLabel("observacao"),subFont7b));
		tabelaInterna.addCell(new Phrase(notaCredito.get("obNotaCredito"),subFont7));
		
		tabelaPrincipal.addCell(tabelaInterna);
		
		return tabelaPrincipal;
	}

	protected PdfPTable getAcrescimoDescontoTable(Map<String, String> notaCredito) {
		PdfPTable tabelaPrincipal = new PdfPTable(1);
		tabelaPrincipal.setWidthPercentage(100f);				
		tabelaPrincipal.getDefaultCell().setPadding(2);
		
		tabelaPrincipal.setSpacingBefore(5f);
		
		PdfPTable tabelaInterna = new PdfPTable(3);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		tabelaInterna.setWidthPercentage(100f);	
		tabelaInterna.getDefaultCell().setPaddingLeft(2f);
		
		PdfPCell c2 = new PdfPCell(new Phrase(getLabel("acrescimoDesconto"), subFont8b));
		setDefaultConfig(c2);
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);		
		c2.setPaddingLeft(2f);
		c2.setColspan(9);
		
		tabelaInterna.addCell(c2);
				
		tabelaInterna.addCell(addChaveValorPremio(getLabel("acrescimo"), notaCredito.get("vlAcrescimo") != null ? notaCredito.get("vlAcrescimo") : "0,00"));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("desconto"), notaCredito.get("vlDesconto") != null ? notaCredito.get("vlDesconto") : "0,00"));
		tabelaInterna.addCell(addChaveValorPremio(getLabel("descontoReferenteUsoCarretaTNT"), notaCredito.get("vlDescUsoEquipamento") != null ? notaCredito.get("vlDescUsoEquipamento") : "0,00"));
		
		tabelaPrincipal.addCell(tabelaInterna);
		return tabelaPrincipal;
	}
	
	private PdfPCell addCellDireita(String texto) {
		PdfPCell cellConteudo = new PdfPCell(new Phrase(texto,subFont7b));
		setDefaultConfig(cellConteudo);
		cellConteudo.setPadding(0);
		cellConteudo.setHorizontalAlignment(Element.ALIGN_RIGHT);
		return cellConteudo;
	}

	protected PdfPTable getDetalheEntregaTable(String nmTabela, List<Map<String, Object>> listDoctos, List<Map<String, Object>> listCalculos) {
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100f);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table.getDefaultCell().setPadding(5);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
		PdfPCell title = new PdfPCell(new Phrase(nmTabela, subFont6b));	
		title.setPaddingTop(0);		
		title.setMinimumHeight(12.5f);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			if(!preview) {
			title.setBackgroundColor(new BaseColor(238,238,238));
			}
			
		table.addCell(title);	
			
		if(!listDoctos.isEmpty()){			
			addTableEntrega(listDoctos, table);		
		}
		
		addCalculo(listCalculos, table);		
		
		PdfPTable tableEntrega = new PdfPTable(1);
		tableEntrega.getDefaultCell().setPadding(2.5f);
		tableEntrega.setSpacingBefore(5f);
		tableEntrega.setWidthPercentage(100f);	
		tableEntrega.getDefaultCell().setBorder(PdfPCell.BOX);		
		tableEntrega.addCell(table);
		
		return tableEntrega;
	}

	/**
	 * @param item
	 * @param table
	 */
	private void addTableEntrega(List<Map<String, Object>> listDoctos, PdfPTable table) {
		PdfPTable header = new PdfPTable(1);
		header.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		header.getDefaultCell().setPadding(0);
		header.getDefaultCell().setBorder(PdfPCell.BOTTOM);
		
		PdfPTable headerLine = new PdfPTable(new float[]{
				0.75f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f
		});
		
		headerLine.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		headerLine.setWidthPercentage(100f);
		headerLine.setSpacingBefore(0.5f);
		headerLine.setSpacingAfter(0.5f);
	
		headerLine.addCell(new Phrase(getLabel("documento"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("servico"),subFont6b));		
		headerLine.addCell(new Phrase(getLabel("volumes"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("volumesCalculado"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("pesoCalculo"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("pesoAferido"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("pesoDeclarado"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("pesoReferencia"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("pesoCalculado"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("valorMercadoria"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("valorMercadoriaCalculado"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("valorFreteBruto"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("valorFreteLiquido"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("ocorrencia"),subFont6b));
				
		header.addCell(headerLine);
		
		table.addCell(header);
						
		boolean banding = false;
		
		for (Map<String, Object> map : listDoctos) {
			banding = !banding;				
															
			PdfPTable infoLinha = new PdfPTable(new float[]{
			        0.75f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 1.25f
			});
			
			infoLinha.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			infoLinha.setWidthPercentage(100f);	
			
			infoLinha.getDefaultCell().setPaddingTop(0);
			infoLinha.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			infoLinha.getDefaultCell().setPaddingLeft(0.5f);
					
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "documento"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "tpModalServico"),subFont6));		
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "qtVolumes"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "volumeCalculado"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "psControleCarga"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "psAferido"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "psReal"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "psReferenciaCalculo"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "pesoCalculado"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "vlMercadoria"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "valorMercadoriaCalculado"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "vlBrutoControleCarga"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "vlLiquidoControleCarga"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "dsOcorrenciaEntrega"),subFont6));
			
			PdfPCell cellConteudo = new PdfPCell(infoLinha);
			
			cellConteudo.setBorder(Cell.NO_BORDER);
			
			if(!preview){				
				cellConteudo.setBackgroundColor(banding ? new BaseColor(245, 245, 245) : BaseColor.WHITE);				
			}

			table.addCell(cellConteudo);
			
			PdfPTable tableEndereco = new PdfPTable(new float[] {0.75f,0.75f,1f});
			tableEndereco.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			tableEndereco.setWidthPercentage(100f);		
			tableEndereco.setSpacingAfter(1f);
			tableEndereco.getDefaultCell().setPadding(0);
			tableEndereco.getDefaultCell().setPaddingLeft(5f);
			
			tableEndereco.addCell(new Phrase(MapUtils.getString(map, "nmPessoaRemetente"), subFont6));
			tableEndereco.addCell(new Phrase(MapUtils.getString(map, "nmPessoaDestinario"), subFont6));		
			tableEndereco.addCell(new Phrase(MapUtils.getString(map, "enderecoEntrega"), subFont6));
			
			PdfPCell cellEndereco = new PdfPCell(tableEndereco);			
			
			if(!preview){
				cellEndereco.setBorder(Cell.NO_BORDER);
				cellEndereco.setBackgroundColor(banding ? new BaseColor(245, 245, 245) : BaseColor.WHITE);
			} else {
				cellEndereco.setBorder(PdfPCell.BOTTOM);
			}
			
			table.addCell(cellEndereco);
		}
	}
	
	/**
	 * @param item
	 * @param table
	 */
	private void addTableColeta(List<Map<String, Object>> listPedidos, PdfPTable table) {
		PdfPTable header = new PdfPTable(1);
		header.getDefaultCell().setPadding(0);
		header.getDefaultCell().setBorder(PdfPCell.BOTTOM);
		
		PdfPTable headerLine = new PdfPTable(new float[]{
				0.75f, 0.75f, 2.0f
		});
		
		headerLine.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		headerLine.getDefaultCell().setPaddingTop(0);
		headerLine.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
						
		headerLine.addCell(new Phrase(getLabel("pedido"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("manifesto"),subFont6b));
		headerLine.addCell(new Phrase(getLabel("endereco"),subFont6b));		
				
		header.addCell(headerLine);
		
		table.addCell(header);
						
		boolean banding = false;
		
		for (Map<String, Object> map : listPedidos) {
			banding = !banding;				
						
			PdfPTable infoLinha = new PdfPTable(new float[]{
					0.75f, 0.75f, 2.0f
			});		
			
			infoLinha.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			infoLinha.getDefaultCell().setPaddingTop(0);
			infoLinha.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			infoLinha.setWidthPercentage(100f);		
			
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "pedidoColeta"), subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "manifestoColeta"), subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "enderecoColeta"), subFont6));
						
			PdfPCell cellConteudo = new PdfPCell(infoLinha);
			
			cellConteudo.setBorder(PdfPCell.NO_BORDER);	
			
			if(!preview){
				cellConteudo.setBackgroundColor(banding ? new BaseColor(245, 245, 245) : BaseColor.WHITE);	
			} else {
				cellConteudo.setBorder(PdfPCell.BOTTOM);	
			}
			
			table.addCell(cellConteudo);
		}
	}
	
	protected PdfPTable getDetalheColetaTable(String nmTabela,
			List<Map<String, Object>> listPedidos,
			List<Map<String, Object>> listCalculos) {			
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100f);
		table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table.getDefaultCell().setPadding(5);
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
			
		PdfPCell title = new PdfPCell(new Phrase(nmTabela, subFont6b));	
		title.setPaddingTop(0);		
		title.setMinimumHeight(12.5f);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
			
			if(!preview) {
			title.setBackgroundColor(new BaseColor(238,238,238));
			}
			
		table.addCell(title);	
			
		if(!listPedidos.isEmpty()){			
			addTableColeta(listPedidos, table);		
		}
		
		addCalculo(listCalculos, table);		
		
		PdfPTable tableEntrega = new PdfPTable(1);
		tableEntrega.getDefaultCell().setPadding(2.5f);
		tableEntrega.setSpacingBefore(5f);
		tableEntrega.setWidthPercentage(100f);	
		tableEntrega.getDefaultCell().setBorder(PdfPCell.BOX);		
		tableEntrega.addCell(table);
		
		return tableEntrega;
	}

	/**
	 * @param item
	 * @param tabelaInterna
	 */
	private void addCalculo(List<Map<String, Object>> listCalculo, PdfPTable tabelaInterna) {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(5f);
		table.getDefaultCell().setBorder(PdfPCell.BOX);
		table.getDefaultCell().setPadding(5);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
				
		PdfPCell title = new PdfPCell(new Phrase(getLabel("fatoresDeCalculo"), subFont6b));	
		title.setPaddingTop(0);
		title.setMinimumHeight(12.5f);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);

		if(!preview) {
			title.setBackgroundColor(new BaseColor(250,250,253));
		}
		
		table.addCell(title);
		
		PdfPTable content = new PdfPTable(1);
		content.getDefaultCell().setPaddingTop(0);
		content.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);		
		content.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable header = new PdfPTable(1);
		header.getDefaultCell().setPadding(0);
		header.getDefaultCell().setBorder(PdfPCell.BOTTOM);
		
		header.addCell(addCabecalhoTabela());
		
		content.addCell(header);
				
		boolean banding = false;
				
		for (Map<String, Object> map : listCalculo) {
			banding = !banding;			
			
			PdfPTable infoLinha = new PdfPTable(new float[]{
					1.5f, 0.75f, 0.5f, 0.5f
			});
			
			infoLinha.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			infoLinha.setWidthPercentage(100f);
			infoLinha.getDefaultCell().setPaddingTop(0);
			infoLinha.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
					
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "tpValorDescription"),subFont6));						
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "qtTotal"),subFont6));
			infoLinha.addCell(new Phrase(MapUtils.getString(map, "vlValor"),subFont6));
			infoLinha.addCell(new Phrase(String.valueOf(MapUtils.getString(map, "vlTotalFormatado")),subFont6));
						
			PdfPCell cellConteudo = new PdfPCell(infoLinha);
			
			if(!preview) {
				cellConteudo.setBorder(PdfPCell.NO_BORDER);	
				cellConteudo.setBackgroundColor(banding ? new BaseColor(245, 245, 245) : BaseColor.WHITE);
			} else {
				cellConteudo.setBorder(PdfPCell.BOTTOM);	
			}			
			
			content.addCell(cellConteudo);
		}
		
		table.addCell(content);		
		
		tabelaInterna.addCell(table);
	}
	
	protected PdfPTable getDetalheColetasNaoExecutadasTable(List<Map<String, Object>> listNaoExecutados) {				
		PdfPTable subTable = new PdfPTable(1);
		subTable.setWidthPercentage(100f);
		subTable.getDefaultCell().setPadding(0);
		subTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable content = new PdfPTable(1);
		content.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		content.getDefaultCell().setPaddingTop(0);
		content.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable header = new PdfPTable(1);
		header.getDefaultCell().setPadding(0);
		header.getDefaultCell().setBorder(PdfPCell.BOTTOM);
				
		content.addCell(header);
						
		addTableColeta(listNaoExecutados, subTable);
						
		subTable.addCell(content);
				
		PdfPTable tableEntrega = new PdfPTable(1);
		tableEntrega.getDefaultCell().setPadding(2.5f);
		tableEntrega.setSpacingBefore(5f);
		tableEntrega.setWidthPercentage(100f);	
		tableEntrega.getDefaultCell().setBorder(PdfPCell.BOX);		
		
		PdfPCell title = new PdfPCell(new Phrase(getLabel("pedidosNaoExecutados"), subFont6b));	
		title.setPaddingTop(0);		
		title.setMinimumHeight(12.5f);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		if(!preview){
			title.setBackgroundColor(new BaseColor(238,238,238));
		}
		
		tableEntrega.addCell(title);		
		tableEntrega.addCell(subTable);
		
		return tableEntrega;
	}
	
	protected PdfPTable getDetalheEntregasNaoExecutadasTable(List<Map<String, Object>> listNaoExecutados) {	
		PdfPTable subTable = new PdfPTable(1);
		subTable.setWidthPercentage(100f);
		subTable.getDefaultCell().setPadding(0);
		subTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable content = new PdfPTable(1);
		content.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		content.getDefaultCell().setPaddingTop(0);
		content.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		PdfPTable header = new PdfPTable(1);
		header.getDefaultCell().setPadding(0);
		header.getDefaultCell().setBorder(PdfPCell.BOTTOM);
				
		content.addCell(header);
						
		addTableEntrega(listNaoExecutados, subTable);
						
		subTable.addCell(content);		
						
		PdfPTable tableEntrega = new PdfPTable(1);
		tableEntrega.getDefaultCell().setPadding(2.5f);
		tableEntrega.setSpacingBefore(5f);
		tableEntrega.setWidthPercentage(100f);	
		tableEntrega.getDefaultCell().setBorder(PdfPCell.BOX);		
		
		PdfPCell title = new PdfPCell(new Phrase(getLabel("documentosNaoExecutados"), subFont6b));	
		title.setPaddingTop(0);		
		title.setMinimumHeight(12.5f);
		title.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		if(!preview){
			title.setBackgroundColor(new BaseColor(238,238,238));
		}
		
		tableEntrega.addCell(title);		
		tableEntrega.addCell(subTable);
		
		return tableEntrega;
	}
	
	protected PdfPTable getCabecalhoContadoresTable(String tipo, Map<String, Object> contadores) {		
		PdfPTable cabecalho2 = new PdfPTable(3);
		cabecalho2.getDefaultCell().setBorder(PdfPCell.NO_BORDER);		
		cabecalho2.setWidthPercentage(100f);
		
		String title = tipo + "s ";
		
		cabecalho2.addCell(addChaveValor(title + getLabel("programadas"), String.valueOf(MapUtils.getLong(contadores,"programadas"))));
		cabecalho2.addCell(addChaveValor(title + getLabel("executadas"), String.valueOf(MapUtils.getLong(contadores,"executadas"))));
		cabecalho2.addCell(addChaveValor(title + getLabel("naoContabilizadas"), String.valueOf(MapUtils.getLong(contadores,"naoExecutadas"))));
								
		PdfPTable cabecalhoInterno = new PdfPTable(1);
		cabecalhoInterno.setWidthPercentage(100f);	
		cabecalhoInterno.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
				
		cabecalhoInterno.getDefaultCell().setPadding(0);
		
		cabecalhoInterno.addCell(cabecalho2);
		
		PdfPTable cabecalho = new PdfPTable(1);
		cabecalho.setWidthPercentage(100f);	
		cabecalho.setSpacingBefore(5f);
		cabecalho.addCell(cabecalhoInterno);
		
		return cabecalho;
	}

	protected PdfPTable getCabecalhoPadraoTable(Map<String, String> header) {
		PdfPTable cabecalhol1 = getCabecalhoNota(getLabel("controleDeCarga"),header.get("controleCarga"),"","",getLabel("dataDeGeracao"),header.get("dhGeracao"));
		PdfPTable cabecalhol2 = getCabecalhoNota(getLabel("frotaPlaca"),header.get("frotaPlaca"),getLabel("tipoMeioTransporte"),header.get("tipoTransporte"),getLabel("marcaModelo"),header.get("modeloTransporte"));
		PdfPTable cabecalhol3 = getCabecalhoNota(getLabel("rota"),header.get("rota"),getLabel("kMRota"),header.get("kmRota"),getLabel("motorista"),header.get("motorista"));
		
		PdfPTable cabecalhoInterno = new PdfPTable(1);
		cabecalhoInterno.setWidthPercentage(100f);	
		cabecalhoInterno.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		
		cabecalhoInterno.getDefaultCell().setPadding(0);
		
		cabecalhoInterno.addCell(cabecalhol1);
		cabecalhoInterno.addCell(cabecalhol2);
		cabecalhoInterno.addCell(cabecalhol3);
		
		PdfPTable cabecalho = new PdfPTable(1);
		cabecalho.setWidthPercentage(100f);	
		cabecalho.addCell(cabecalhoInterno);
		
		return cabecalho;
	}
	
	protected PdfPTable addInfoLinhaDetalhe(String remetente, String destinatario, String endereco) {
		PdfPTable infoLinha = new PdfPTable(new float[] {0.75f,0.75f,1f});
		infoLinha.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		infoLinha.setWidthPercentage(100f);		
		infoLinha.setSpacingAfter(1f);
		infoLinha.getDefaultCell().setPadding(0);
		infoLinha.getDefaultCell().setPaddingLeft(5f);
		
		infoLinha.addCell(new Phrase(remetente, subFont6));
		infoLinha.addCell(new Phrase(destinatario, subFont6));		
		infoLinha.addCell(new Phrase(endereco, subFont6));
		
		return infoLinha;
	}
	
	protected PdfPTable getCabecalhoNota(String chave1, String valor1, String chave2, String valor2, String chave3, String valor3) {
		PdfPTable cabecalho = new PdfPTable(3);
		cabecalho.getDefaultCell().setBorder(PdfPCell.NO_BORDER);		
		cabecalho.setWidthPercentage(100f);
		
		cabecalho.addCell(addChaveValor(chave1,valor1));
		cabecalho.addCell(addChaveValor(chave2,valor2));
		cabecalho.addCell(addChaveValor(chave3,valor3));
		
		return cabecalho;
	}

	protected PdfPTable addCabecalhoTabela() {
		PdfPTable infoLinha = new PdfPTable(new float[]{
				1.5f, 0.75f, 0.5f, 0.5f
		});
		
		infoLinha.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		infoLinha.setWidthPercentage(100f);	
		infoLinha.getDefaultCell().setPaddingTop(0);
		infoLinha.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);		
				
		infoLinha.addCell(new Phrase(getLabel("tipo"),subFont6b));	
		infoLinha.addCell(new Phrase(getLabel("quantidade"),subFont6b));
		infoLinha.addCell(new Phrase(getLabel("valor"),subFont6b));
		infoLinha.addCell(new Phrase(getLabel("total"),subFont6b));	
		
		return infoLinha;
	}
	
	protected PdfPTable addChaveValor(String chave, String valor) {
		PdfPTable pdfPTable = new PdfPTable(2);
		pdfPTable.setWidthPercentage(100f);		
		
		PdfPCell c1 = new PdfPCell(new Phrase(chave,subFont7b));
		setDefaultConfig(c1);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setPadding(0);
		pdfPTable.addCell(c1);
		
		PdfPCell c2 = new PdfPCell(new Phrase(valor,subFont7));
		setDefaultConfig(c2);
		c2.setHorizontalAlignment(Element.ALIGN_LEFT);
		c2.setVerticalAlignment(Element.ALIGN_MIDDLE);		
		c2.setPadding(0);
		pdfPTable.addCell(c2);
		return pdfPTable;
	}
	
	private void setDefaultConfig(PdfPCell cell) {
		cell.setBorderWidthBottom(0);
		cell.setBorderWidthLeft(0);
		cell.setBorderWidthRight(0);
		cell.setBorderWidthTop(0);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	}
	
	protected PdfPTable getFooterTable(){
		if(preview){
			return new PdfPTable(1);
		}
		
		PdfPTable tabelaPrincipal = new PdfPTable(1);
		tabelaPrincipal.setWidthPercentage(50f);				
		tabelaPrincipal.getDefaultCell().setPadding(2);
		tabelaPrincipal.setSpacingBefore(40f);
		tabelaPrincipal.getDefaultCell().setBorder(PdfPCell.NO_BORDER);		
		
		PdfPTable tabelaInterna = new PdfPTable(2);
		tabelaInterna.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		tabelaInterna.setWidthPercentage(100f);	
		tabelaInterna.getDefaultCell().setPadding(0);
		tabelaInterna.getDefaultCell().setPaddingLeft(30f);
			
		tabelaInterna.addCell(getAssinaturaCell("vistoColaborador"));				
		tabelaInterna.addCell(getAssinaturaCell("vistoMotorista"));
				
		tabelaPrincipal.addCell(tabelaInterna);
		
		return tabelaPrincipal;
	}
	
	public static String getLabel(String key){
		if(!getDefaultResourceBundle().containsKey(key)){
			return "NO_LABEL";
		}
		
		return getDefaultResourceBundle().getString(key);
	}
	
	private static ResourceBundle getDefaultResourceBundle(){		
		return ResourceBundle.getBundle("com.mercurio.lms.fretecarreteirocoletaentrega.report.emitirNotaCreditoPadrao", LocaleContextHolder.getLocale());
	}
}

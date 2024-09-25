package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class TemplatePdf {
	public static final Font FONT_9B = new Font(Font.FontFamily.HELVETICA, 9,Font.BOLD);
	public static final Font FONT_8B = new Font(Font.FontFamily.HELVETICA, 8,Font.BOLD);
	public static final Font FONT_8 = new Font(Font.FontFamily.HELVETICA, 8,Font.NORMAL);
	public static final Font FONT_8U = new Font(Font.FontFamily.HELVETICA, 8,Font.UNDERLINE);
	public static final Font FONT_7 = new Font(Font.FontFamily.HELVETICA, 7,Font.NORMAL);
	public static final Font FONT_7B = new Font(Font.FontFamily.HELVETICA, 7,Font.BOLD);
	public static final Font FONT_6B = new Font(Font.FontFamily.HELVETICA, 6,Font.BOLD);
	public static final Font FONT_10 = new Font(Font.FontFamily.HELVETICA, 10,Font.NORMAL);
	public static final Font FONT_10B = new Font(Font.FontFamily.HELVETICA, 10,Font.BOLD);
	public static final String CENTER_TEXT = "CenterText";
	public static final String ALIGN_CENTER = "Center";
	public static final String ALIGN_LEFT= "Left";
	public static final String ALIGN_RIGHT = "Right";
	public static final String ALIGN = "Align";
	
	private final Log log = LogFactory.getLog(TemplatePdf.class); 
	
	protected File file = null;
	private String usuario;
	private boolean rodape = true;
	protected Document document;
	PdfWriter writer;
	boolean landsscape;
	Map<String, String> dados = new HashMap<String, String>();
	
	protected void getDocument(File file, boolean landsscape, Class origem,boolean rodape) {
		setRodape(rodape);
		getDocument(file, landsscape, origem);
	}
	
	
	protected void getDocument(File file, boolean landsscape, Class origem) {
		this.file = file;
		this.landsscape = landsscape;
		this.document = new Document(landsscape ? PageSize.A4.rotate() : PageSize.A4, 30, 30, 30, 36);
		log.warn("Report:"+origem.getSimpleName());
		try {
			writer = PdfWriter.getInstance(document,new FileOutputStream(file) );
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (DocumentException e) {
			log.error(e);
		}
		RodapeCabecalhoPdfHelper event = new RodapeCabecalhoPdfHelper(landsscape);
		event.setUsuario(getUsuario());
		event.setData(getData());
		event.setRodape(isRodape());
		writer.setPageEvent(event);
		document.open();
		
		try{
			/**
			 * Utilizado para escrever no pdf o nome do report
			 * 
			 * */
//			Paragraph listaTabelaPreco = new Paragraph(origem.getSimpleName(), FONT_7);
//			listaTabelaPreco.setAlignment(Element.ALIGN_CENTER);
//			document.add(listaTabelaPreco);
			
			if(usuario == null){				
				getDadosCliente();
			}
			addContent();
			document.close();
		} catch (Exception e) {
			addErro(origem,e);
		}
	}

	private String getData() {
		return dados.get("data");
	}


	protected Paragraph addTitulo(String string) {
		Paragraph listaTabelaPreco = new Paragraph(string, FONT_10);
		listaTabelaPreco.setAlignment(Element.ALIGN_CENTER);
		return listaTabelaPreco;
	}
	
	protected PdfPTable getFinalQuadro() {
		PdfPTable fim = new PdfPTable(1);
		fim.setWidthPercentage(100f);
		PdfPCell c1 = new PdfPCell(new Phrase(""));		
		c1.setBorderWidthBottom(0);
		c1.setBorderWidthLeft(0);
		c1.setBorderWidthRight(0);
		c1.setBorderWidthTop(0f);
		fim.addCell(c1);
		return fim;
	}

	protected PdfPTable montaQuadro(String tituloEsquerda, List<Map<String,String>> listaEsquerda,String tituloDireita, List<Map<String,String>> listaDireita) {
		if(validaListaVazia(listaEsquerda) && validaListaVazia(listaDireita)){
			return new PdfPTable(1);
		}
		if(listaEsquerda == null){
			listaEsquerda = new ArrayList<Map<String,String>>();
		}
		if(listaDireita == null){
			listaDireita = new ArrayList<Map<String,String>>();
		}
			
		
		float[] widths = {0.8f, 1f,0.8f, 1f};
		PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100f);
		addTitle(table,tituloEsquerda,2);
		addTitle(table,tituloDireita,2);
		String anterior = "";
		for (Map<String,String> mapG : listaEsquerda) {
			if(!mapG.containsKey("linha") && (mapG.get("label") == null || anterior.equals(mapG.get("label")))){
				continue;
			}
			if(mapG.containsKey("linha")){
				addCellLeft(table, mapG.remove("linha"),2,0,0,0.5f,0.5f);
				
			}else{				
				addCellLeft(table, mapG.get("label"),1,0,0,0.5f,0);
				addCellLeft(table, mapG.get("valor"),1,0,0,0,0.5f);
			}
			
			boolean isBlank = true;
			for (Map<String,String> mapF : listaDireita){
				if(mapF.isEmpty()){
					continue;
				}
				if(mapF.containsKey("linha")){
					addCellLeft(table, mapF.remove("linha"),2,0,0,0,0.5f);
					isBlank = false;
					break;
				}
				String label = mapF.remove("label");
				String valor = 	mapF.remove("valor");
				addCellLeft(table, label,1,0,0,0,0);
				addCellLeft(table, valor,1,0,0,0,0.5f);
				isBlank = false;
				break;
			}
			if(isBlank){
				addCellLeft(table, "",1,0,0,0,0);
				addCellLeft(table, "",1,0,0,0,0.5f);
			}
			if(mapG.get("label") == null){
				anterior = "";
			}else{
				anterior = mapG.get("label");				
			}
			
		}
		if(!listaDireita.isEmpty()){
			for (Map<String,String> mapF : listaDireita){
				if(mapF.isEmpty()){
					continue;
				}
				if(mapF.containsKey("linha")){
					addCellLeft(table, "",1,0,0,0.5f,0);
					addCellLeft(table, "",1,0,0,0,0.5f);
					addCellLeft(table, mapF.remove("linha"),2,0,0,0,0.5f);
					continue;
				}
				String label = mapF.remove("label");
				String valor = 	mapF.remove("valor");
				addCellLeft(table, "",1,0,0,0.5f,0);
				addCellLeft(table, "",1,0,0,0,0.5f);
				addCellLeft(table, label,1,0,0,0,0);
				addCellLeft(table, valor,1,0,0,0,0.5f);
			}
		}
		return table;
	}

	private boolean validaListaVazia(List<Map<String, String>> lista) {
		return lista == null || lista.isEmpty();
	}

	private void addCellLine(PdfPTable table, String parametro, String valor, int colspan) {
		Paragraph subPara = new Paragraph(parametro, FONT_7);
		PdfPCell c1 = new PdfPCell(subPara);
		c1.setBorder(0);
		c1.setBorderWidthLeft(0.1f);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setColspan(colspan);
		table.addCell(c1);
		Paragraph subPara2 = new Paragraph(valor, FONT_7);
		PdfPCell c2 = new PdfPCell(subPara2);
		c2.setBorder(0);
		c2.setBorderWidthRight(0.1f);
		c2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c2.setColspan(colspan);
		table.addCell(c2);
	}

	protected void addCellCenter(PdfPTable table, String string,int colspan) {
		Paragraph subPara = new Paragraph(string, FONT_7);
		PdfPCell c1 = new PdfPCell(subPara);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setColspan(colspan);
		table.addCell(c1);
	}
	protected void addCellCenterWithoutBottomBorder(PdfPTable table, String string) {
		Paragraph subPara = new Paragraph(string, FONT_7);
		PdfPCell c1 = new PdfPCell(subPara);
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setBorder(0);
		c1.setBorderWidthRight(0.1f);
		c1.setBorderWidthLeft(0.1f);
		c1.setColspan(4);
		table.addCell(c1);
	}
	
	protected void addCellLeft(PdfPTable table, String string,int colspan,float borderBottom,float borderTop,float borderLeft,float borderRight) {
		addCellLeft(table, string,colspan,borderBottom,borderTop,borderLeft,borderRight,null);
	}
	
	protected void addCellLeft(PdfPTable table, String string,int colspan,float borderBottom,float borderTop,float borderLeft,float borderRight,Font fonte) {
		if (fonte == null) {
			fonte = FONT_7;
		}
		PdfPCell c1 = new PdfPCell(new Phrase(string,fonte));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setColspan(colspan);
		c1.setBorderWidthBottom(borderBottom);
		c1.setBorderWidthLeft(borderLeft);
		c1.setBorderWidthRight(borderRight);
		c1.setBorderWidthTop(borderTop);
		c1.setPadding(1f);
		c1.setPaddingLeft(2f);
		table.addCell(c1);
	}
	
	protected void addCellRight(PdfPTable table, String string,int colspan,float borderBottom,float borderTop,float borderLeft,float borderRight) {
		PdfPCell c1 = new PdfPCell(new Phrase(string, FONT_7));
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setColspan(colspan);
		c1.setBorderWidthBottom(borderBottom);
		c1.setBorderWidthLeft(borderLeft);
		c1.setBorderWidthRight(borderRight);
		c1.setBorderWidthTop(borderTop);
		table.addCell(c1);
	}

	protected void addTitle(PdfPTable table, String titulo, int colspan) {		
		PdfPCell c1 = new PdfPCell(new Phrase(titulo, FONT_8B));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setColspan(colspan);
		c1.setGrayFill(0.75f);
		table.addCell(c1);
	}
	
	protected void addTitle(PdfPTable table, PdfPCell cell, int colspan) {				
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(colspan);
		cell.setGrayFill(1f);
		table.addCell(cell);
	}

	protected Paragraph addEmptyLine(int number) {
		Paragraph paragraph = new Paragraph();
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
		return paragraph;
	}
	
	public File getFile() {
		return file;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}
	
	public void montaGeneralidadeFormalidade(int colspanTotal, PdfPTable table,List<Map<String,String>> generalidades,List<Map<String,String>> formalidades) {
		montaGeneralidade(colspanTotal, table,	generalidades);
		
		montaFormalidade(colspanTotal, table, formalidades);
	}
	
	public void montaServicosAdicionaisDificuldadeEntrega(int colspanTotal, PdfPTable table,List<Map<String,String>> generalidades,List<Map<String,String>> servAdicionais) {
		montaDificuldadesEntrega(table, generalidades, colspanTotal);
		
		montaServicosAdicionais(table, servAdicionais, colspanTotal);
		
		montaFinalQuadro(colspanTotal, table);
	}


	protected void montaFinalQuadro(int colspanTotal, PdfPTable table) {
		PdfPCell finalQuadro = new PdfPCell(getFinalQuadro());
		finalQuadro.setColspan(colspanTotal);
		finalQuadro.setBorderWidthBottom(0f);
		finalQuadro.setBorderWidthLeft(0f);
		finalQuadro.setBorderWidthRight(0f);
		finalQuadro.setBorderWidthTop(0f);
		table.addCell(finalQuadro);
	}

	protected void montaServicosAdicionais(PdfPTable table, List<Map<String, String>> servAdicionais, int colspanTotal) {
		PdfPTable tableServAdicionais = montaQuadroCentral(getDefaultResourceBundle().getString("ServicosAdicionais"), servAdicionais);
		PdfPCell servAdicionaisCell = new PdfPCell(tableServAdicionais);
		servAdicionaisCell.setColspan(colspanTotal);
		servAdicionaisCell.setRowspan(servAdicionais.size());
		servAdicionaisCell.setBorderWidthBottom(0.1f);
		servAdicionaisCell.setBorderWidthLeft(0.1f);
		servAdicionaisCell.setBorderWidthRight(0.1f);
		servAdicionaisCell.setBorderWidthTop(0f);
		servAdicionaisCell.setPaddingBottom(0f);
		table.addCell(servAdicionaisCell);
	}


	protected void montaDificuldadesEntrega(PdfPTable table, List<Map<String, String>> dificuldadesEntrega, int colspanTotal) {
		PdfPTable tableGeneralidadeDifEntrega = montaQuadroCentral(getDefaultResourceBundle().getString("dificuldadeEntrega"), dificuldadesEntrega);
		
		PdfPCell generalidadeCell = new PdfPCell(tableGeneralidadeDifEntrega);
		generalidadeCell.setColspan(colspanTotal);
		generalidadeCell.setRowspan(dificuldadesEntrega.size());
		generalidadeCell.setBorderWidthBottom(0.1f);
		generalidadeCell.setBorderWidthLeft(0.1f);
		generalidadeCell.setBorderWidthRight(0.1f);
		generalidadeCell.setBorderWidthTop(0f);
		generalidadeCell.setPaddingBottom(0f);
		table.addCell(generalidadeCell);
	}
	

	private void montaGeneralidade(int colspanTotal, PdfPTable table,
			List<Map<String, String>> generalidades) {
		PdfPTable tableGeneralidade = montaQuadroCentral(getDefaultResourceBundle().getString("generalidades"), generalidades);
		PdfPCell generalidadeCell = new PdfPCell(tableGeneralidade);
		generalidadeCell.setColspan(colspanTotal);
		generalidadeCell.setRowspan(generalidades.size());
		generalidadeCell.setBorderWidthBottom(0.1f);
		generalidadeCell.setBorderWidthLeft(0.1f);
		generalidadeCell.setBorderWidthRight(0.1f);
		generalidadeCell.setBorderWidthTop(0f);
		generalidadeCell.setPaddingBottom(0f);
		table.addCell(generalidadeCell);
	}


	private void montaFormalidade(int colspanTotal, PdfPTable table,
			List<Map<String, String>> formalidades) {
		PdfPTable tableFormalidade = montaQuadroCentral(getDefaultResourceBundle().getString("formalidades"), formalidades);
		PdfPCell formalidadeCell = new PdfPCell(tableFormalidade);
		formalidadeCell.setColspan(colspanTotal);
		formalidadeCell.setRowspan(formalidades.size());
		formalidadeCell.setBorderWidthBottom(0.1f);
		formalidadeCell.setBorderWidthLeft(0.1f);
		formalidadeCell.setBorderWidthRight(0.1f);
		formalidadeCell.setBorderWidthTop(0f);
		formalidadeCell.setPaddingBottom(0f);
		table.addCell(formalidadeCell);
	}
	

	private PdfPTable montaQuadroCentral(String tituloCentro,
			List<Map<String, String>> listaCentro) {
		if (validaListaVazia(listaCentro)) {
			return new PdfPTable(1);
		}
		if (listaCentro == null) {
			listaCentro = new ArrayList<Map<String, String>>();
		}
		float[] widths = { 0.8f, 1f, 0.8f, 1f };
		PdfPTable table = new PdfPTable(widths);
		table.setWidthPercentage(100f);
		addTitle(table, tituloCentro, 4);
		String anterior = "";
		
		for (Map<String, String> mapG : listaCentro) {
			if (!mapG.containsKey("linha") && (mapG.get("label") == null || anterior.equals(mapG.get("label")))) {
				continue;
			}
			if (mapG.containsKey("linha")) {
				addCellLine(table, mapG.remove("linha"), "", 4);
			}
			if (mapG.containsKey("txtDifEntrega")) {
				addCellLeft(table, mapG.remove("txtDifEntrega"), 3, 0.0f, 0.0f, 0.1f, 0f);
				addCellRight(table, "", 3, 0f, 0f, 0.0f, 0.1f);
				addLinhaBranco(5f, 4, table);
			}

			if (mapG.containsKey(ALIGN) && (mapG.get(ALIGN) == ALIGN_CENTER)) {
				addLinhaBranco(5f, 4, table);
				addCellLine(table, mapG.get("label") , "", 4);
			} else if (mapG.containsKey(ALIGN) && (mapG.get(ALIGN) == ALIGN_LEFT)) {
				addCellLeft(table, mapG.get("label") + ": " + mapG.get("valor"), 4, 0.0f, 0.0f, 0.1f, 0f);
			} else if(mapG.containsKey("label") && mapG.get("label") != null && mapG.containsKey("valor") && mapG.get("valor") != null){
				addCellLine(table, mapG.get("label"), mapG.get("valor"), 3);
			}
			if (mapG.get("label") == null) {
				anterior = "";
			} else {
				anterior = mapG.get("label");
			}

		}
		addLinhaBranco(5f, 4, table);
		return table;
	}

	public void getLegenda(int total, PdfPTable tableConteudo,List<Map<String,String>> legendas, List<Map<String,String>> aereo) {
		if(legendas == null){
			return;
		}
		
		addLinhaBranco(15f, total, tableConteudo);
		
		PdfPTable lnda = new PdfPTable(4);
		
		PdfPCell legenda = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("legenda"), FONT_8B));
		legenda.setHorizontalAlignment(Element.ALIGN_LEFT);
		legenda.setVerticalAlignment(Element.ALIGN_MIDDLE);
		legenda.setBorderWidthBottom(0f);
		legenda.setBorderWidthRight(0f);
		legenda.setColspan(3);
		lnda.addCell(legenda);
		
		int cont = 0;
		
		PdfPCell linha = new PdfPCell(new Phrase("", FONT_7));
		if(aereo != null){			
			linha = new PdfPCell(new Phrase(aereo.get(cont).get("SUB_AEREO"), FONT_7));
		}
		linha.setHorizontalAlignment(Element.ALIGN_LEFT);
		linha.setVerticalAlignment(Element.ALIGN_MIDDLE);
		linha.setBorderWidthBottom(0f);
		lnda.addCell(linha);
		
		
		for (Map m: legendas) {
			cont++;
		
			String li0 = m.get("SUB1_TXT_LEG0") ==  null ? "" : String.valueOf(m.get("SUB1_TXT_LEG0"));
			String li1 = m.get("SUB1_TXT_LEG1") ==  null ? "" : String.valueOf(m.get("SUB1_TXT_LEG1"));
			String li2 = m.get("SUB1_TXT_LEG2") ==  null ? "" : String.valueOf(m.get("SUB1_TXT_LEG2"));
			
			PdfPCell l = new PdfPCell(new Phrase(li0, FONT_7));
			l.setHorizontalAlignment(Element.ALIGN_LEFT);
			l.setVerticalAlignment(Element.ALIGN_MIDDLE);
			l.setBorderWidthBottom(0f);
			l.setBorderWidthTop(0f);
			l.setBorderWidthRight(0f);			
			lnda.addCell(l);
			
			
			PdfPCell l1 = new PdfPCell(new Phrase(li1, FONT_7));
			l1.setHorizontalAlignment(Element.ALIGN_LEFT);
			l1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			l1.setBorderWidthBottom(0f);
			l1.setBorderWidthTop(0f);
			l1.setBorderWidthRight(0f);
			l1.setBorderWidthLeft(0f);
			lnda.addCell(l1);
			
			PdfPCell l2 = new PdfPCell(new Phrase(li2, FONT_7));
			l2.setHorizontalAlignment(Element.ALIGN_LEFT);
			l2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			l2.setBorderWidthBottom(0f);
			l2.setBorderWidthTop(0f);
			l2.setBorderWidthLeft(0f);
			l2.setBorderWidthRight(0f);
			lnda.addCell(l2);
			
			PdfPCell l3 = null;
			
			if(aereo != null){	
				if(cont > aereo.size()-1){
					l3 = new PdfPCell(new Phrase("", FONT_7));
				}else{							
					l3 = new PdfPCell(new Phrase(aereo.get(cont).get("SUB_AEREO"), FONT_7));
				}
				l3.setHorizontalAlignment(Element.ALIGN_LEFT);
				l3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				l3.setBorderWidthBottom(0f);
				l3.setBorderWidthTop(0f);
				l3.setColspan(total/4);
				lnda.addCell(l3);
			}else{
				l3 = new PdfPCell(new Phrase("", FONT_7));
				l3.setHorizontalAlignment(Element.ALIGN_LEFT);
				l3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				l3.setBorderWidthBottom(0f);
				l3.setBorderWidthTop(0f);
				lnda.addCell(l3);
			}
		}
		
		PdfPCell quadra = new PdfPCell(lnda);							
		quadra.setColspan(total);
		tableConteudo.addCell(quadra);
	}
	
	public void addLinhaBranco(float altura, int total, PdfPTable tableConteudo) {
		PdfPCell branco = new PdfPCell(new Phrase(""));
		branco.setColspan(total);
		branco.setFixedHeight(altura);
		branco.setBorder(0);
		tableConteudo.addCell(branco);
	}

	
	public static ResourceBundle getDefaultResourceBundle(){
		return ResourceBundle.getBundle("com.mercurio.lms.tabelaprecos.report.emitirTabelaEcommerceDiferenciadaPDF",LocaleContextHolder.getLocale());
	}
	
	public void getLegendaGeneralidade(int total, PdfPTable tableConteudo, List<Map<String,String>> legendaGeneralidades) {
		if(legendaGeneralidades != null && !legendaGeneralidades.isEmpty()){
			for (Map<String, String> m : legendaGeneralidades){
				for (int i = 1; i < 5; i++) {
					String valor = m.get("desc"+i) != null ? String.valueOf(m.get("desc"+i)): null;
					if(valor == null){
						break;
					}
					PdfPCell cell = new PdfPCell(new Phrase(valor, FONT_7));
					cell.setBorder(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(total/5);
					tableConteudo.addCell(cell);
				}
				
			}
			
		}
	}
	
	public void getTaxaTerrestre(int total, PdfPTable tableConteudo, List<Map<String,String>> terrestre) {
		if(terrestre == null){
			return;
		}
		
		PdfPTable pdfPTable = new PdfPTable(5);
		PdfPCell cellTitulo = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaTerrestre"), FONT_8B));
		cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellTitulo.setGrayFill(0.75f);
		cellTitulo.setColspan(5);
		pdfPTable.addCell(cellTitulo);
		
		for (Map<String, String> m : terrestre) {
			String faixa1 = m.get("VL_FAIXA_LAYOUT1");
			String valor1 = m.get("VL_TAXA_LAYOUT1");
			String faixa3 = m.get("VL_FAIXA_LAYOUT3");
			String valor3 = m.get("VL_TAXA_LAYOUT3");
			
			PdfPCell c1 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("ate")+faixa1 +getDefaultResourceBundle().getString("kg"), FONT_7));
			c1.setBorder(0);
			c1.setHorizontalAlignment(Element.ALIGN_LEFT);
			pdfPTable.addCell(c1);
			
			PdfPCell c2 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("moeda") + valor1, FONT_7));
			c2.setHorizontalAlignment(Element.ALIGN_LEFT);
			c2.setBorder(0);
			pdfPTable.addCell(c2);
			
			PdfPCell c3 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("excedente"), FONT_7));
			c3.setHorizontalAlignment(Element.ALIGN_LEFT);
			c3.setBorder(0);
			pdfPTable.addCell(c3);
			
			PdfPCell c4 = new PdfPCell(new Phrase("Acima "+faixa3 +getDefaultResourceBundle().getString("kg"), FONT_7));
			c4.setHorizontalAlignment(Element.ALIGN_LEFT);
			c4.setBorder(0);
			pdfPTable.addCell(c4);
			
			PdfPCell c5 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("moeda") + valor3, FONT_7));
			c5.setHorizontalAlignment(Element.ALIGN_LEFT);
			c5.setBorder(0);
			pdfPTable.addCell(c5);
		}
		
		PdfPCell taxaTerrestre = new PdfPCell(pdfPTable);
		taxaTerrestre.setColspan(total);
		
		tableConteudo.addCell(taxaTerrestre);
	}
	
	public void getTaxaColetaEntrega(int total, PdfPTable tableConteudo,boolean isColeta, List<Map<String, String>> entrega, List<Map<String, String>> coleta) {
		List<Map<String, String>> valoresColetaEntrega = entrega;
		String titulo = getDefaultResourceBundle().getString("taxaParaEntrega");
		if(isColeta){
			valoresColetaEntrega = coleta;
			titulo = getDefaultResourceBundle().getString("taxaParaColeta");
		}
		
		if(valoresColetaEntrega == null){
			return;
		}
		
		PdfPTable pdfPTable = new PdfPTable(8);
		PdfPCell cellTitulo = new PdfPCell(new Phrase(titulo, FONT_8B));
		cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellTitulo.setGrayFill(0.75f);
		cellTitulo.setColspan(8);
		pdfPTable.addCell(cellTitulo);
		
		PdfPCell taxaUrbanaConvencional = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaUrbanaConvencional"), FONT_8B));
		taxaUrbanaConvencional.setColspan(2);
		taxaUrbanaConvencional.setBorderWidth(0f);
		taxaUrbanaConvencional.setBorderWidthRight(0.5f);
		pdfPTable.addCell(taxaUrbanaConvencional);
		
		PdfPCell taxaInteriorConvencional = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaInteriorConvencional"), FONT_8B));
		taxaInteriorConvencional.setColspan(2);
		taxaInteriorConvencional.setBorderWidth(0f);
		taxaInteriorConvencional.setBorderWidthRight(0.5f);
		pdfPTable.addCell(taxaInteriorConvencional);
		
		PdfPCell taxaUrbanaEmergencial = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaUrbanaEmergencial"), FONT_8B));
		taxaUrbanaEmergencial.setColspan(2);
		taxaUrbanaEmergencial.setBorderWidth(0f);
		taxaUrbanaEmergencial.setBorderWidthRight(0.5f);
		pdfPTable.addCell(taxaUrbanaEmergencial);
		
		PdfPCell taxaInteriorEmergencial = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaInteriorEmergencial"), FONT_8B));
		taxaInteriorEmergencial.setColspan(2);
		taxaInteriorEmergencial.setBorderWidth(0f);
		taxaInteriorEmergencial.setBorderWidthRight(0.5f);
		pdfPTable.addCell(taxaInteriorEmergencial);
		
		for (Map<String, String> co : valoresColetaEntrega) {
			for (int i = 1; i < 4; i++) {
				PdfPCell label1 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("ate")+String.valueOf(co.get("PS_TAXADO_LAYOUT"+i))+getDefaultResourceBundle().getString("km"), FONT_7));
				label1.setColspan(1);
				label1.setBorderWidth(0f);
				pdfPTable.addCell(label1);
				
				PdfPCell valor1 = new PdfPCell(new Phrase(co.get("DS_SIMBOLO_LAYOUT"+i) + " "+String.valueOf(co.get("VL_TAXA_LAYOUT"+i)), FONT_7));
				valor1.setColspan(1);
				valor1.setBorderWidth(0f);
				valor1.setBorderWidthRight(0.5f);
				pdfPTable.addCell(valor1);
			}
			
			
			PdfPCell valor = new PdfPCell(new Phrase(co.get("DS_SIMBOLO_LAYOUT4") + " "+ String.valueOf(co.get("VL_TAXA_LAYOUT4"))+getDefaultResourceBundle().getString("kmRodadoIdaVolta"), FONT_7));
			valor.setColspan(2);
			valor.setBorderWidth(0f);
			pdfPTable.addCell(valor);
			
			PdfPCell pCell = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("excedente"), FONT_7));
			pCell.setBorderWidth(0f);
			pCell.setColspan(1);
			pdfPTable.addCell(pCell);
			
			PdfPCell pCell1 = new PdfPCell(new Phrase(co.get("DS_SIMBOLO_LAYOUT4") + " "+ String.valueOf(co.get("VL_EXCEDENTE_LAYOUT1")), FONT_7));
			pCell1.setColspan(1);
			pCell1.setBorderWidth(0f);
			pCell1.setBorderWidthRight(0.5f);
			pdfPTable.addCell(pCell1);
			
			PdfPCell pCell2 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("excedente"), FONT_7));
			pCell2.setColspan(1);
			pCell2.setBorderWidth(0f);
			pdfPTable.addCell(pCell2);
			
			PdfPCell pCell3 = new PdfPCell(new Phrase(co.get("DS_SIMBOLO_LAYOUT4") + " "+ String.valueOf(co.get("VL_EXCEDENTE_LAYOUT2")), FONT_7));
			pCell3.setColspan(1);
			pCell3.setBorderWidth(0f);
			pCell3.setBorderWidthRight(0.5f);
			pdfPTable.addCell(pCell3);
			
			PdfPCell pCell4 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("excedente"), FONT_7));
			pCell4.setBorderWidth(0f);
			pCell4.setColspan(1);
			pdfPTable.addCell(pCell4);
			
			PdfPCell pCell5 = new PdfPCell(new Phrase(co.get("DS_SIMBOLO_LAYOUT4") + " "+ String.valueOf(co.get("VL_EXCEDENTE_LAYOUT3")), FONT_7));
			pCell5.setColspan(1);
			pCell5.setBorderWidth(0f);
			pCell5.setBorderWidthRight(0.5f);
			pdfPTable.addCell(pCell5);
			
			PdfPCell pCell6 = new PdfPCell(new Phrase("", FONT_7));
			pCell6.setBorderWidth(0f);
			pCell6.setColspan(2);
			pdfPTable.addCell(pCell6);
		}
		
		PdfPCell taxaColeta = new PdfPCell(pdfPTable);
		taxaColeta.setColspan(total);
		
		tableConteudo.addCell(taxaColeta);
	}

	public void getTaxaCombustivel(int total, PdfPTable tableConteudo,Set taxaCombustivelCabecalho,  List<Map<String, String>> taxaCombustivel) {
		if (taxaCombustivelCabecalho == null){
			return;
		}
		
		float[] wt = new float[taxaCombustivelCabecalho.size()+1];
		wt[0] = 2f;
		
		for (int i = 1; i < taxaCombustivelCabecalho.size()+1; i++) {
			wt[i] = 1f;
		}
		
		PdfPTable taxa = new PdfPTable(wt);
		
		PdfPCell taxaConbustivel = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaCombustivel"), FONT_8B));
		taxaConbustivel.setHorizontalAlignment(Element.ALIGN_CENTER);
		taxaConbustivel.setVerticalAlignment(Element.ALIGN_MIDDLE);
		taxaConbustivel.setColspan(taxaCombustivelCabecalho.size()+1);
		taxaConbustivel.setGrayFill(0.75f);
		taxa.addCell(taxaConbustivel);
		
		PdfPCell or = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("origem"), FONT_8B));
		or.setHorizontalAlignment(Element.ALIGN_CENTER);
		or.setVerticalAlignment(Element.ALIGN_MIDDLE);
		taxa.addCell(or);
		
		PdfPTable pdfPTable = new PdfPTable(taxaCombustivelCabecalho.size());
		
		PdfPCell destino = new PdfPCell(new Phrase("Destino", FONT_8B));
		destino.setHorizontalAlignment(Element.ALIGN_CENTER);
		destino.setVerticalAlignment(Element.ALIGN_MIDDLE);
		destino.setColspan(taxaCombustivelCabecalho.size());
		pdfPTable.addCell(destino);
		
		for(Object estado :taxaCombustivelCabecalho){
			PdfPCell legenda = new PdfPCell(new Phrase(String.valueOf(estado), FONT_8B));
			legenda.setHorizontalAlignment(Element.ALIGN_CENTER);
			legenda.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(legenda);
		}
		
		PdfPCell pdfPCell = new PdfPCell(pdfPTable);
		pdfPCell.setColspan(taxaCombustivelCabecalho.size());
		taxa.addCell(pdfPCell); 
		
		for (Map<String, String> tx : taxaCombustivel) {
			for (int i = 0; i < taxaCombustivelCabecalho.size()+1; i++) {		
				if(i == 0){
					PdfPCell legenda = new PdfPCell(new Phrase(String.valueOf(tx.get("ORIGEM")), FONT_7));
					legenda.setHorizontalAlignment(Element.ALIGN_CENTER);
					legenda.setVerticalAlignment(Element.ALIGN_MIDDLE);
					taxa.addCell(legenda);
				}else{				
					String valor = tx.get("COLUMN"+i) == null ? "" : String.valueOf(tx.get("COLUMN"+i));
					PdfPCell legenda = new PdfPCell(new Phrase(valor, FONT_7));
					legenda.setHorizontalAlignment(Element.ALIGN_CENTER);
					legenda.setVerticalAlignment(Element.ALIGN_MIDDLE);
					taxa.addCell(legenda);
				}
			}
		}
		
		PdfPCell taxaTable = new PdfPCell(taxa);
		taxaTable.setColspan(total);
		tableConteudo.addCell(taxaTable);
	}

	public abstract void addContent() throws Exception;
	
	public  void addErro(Class classe, Exception e) {
		Paragraph erro = addTitulo("Erro ao gerar  "+classe.getCanonicalName() + e.getMessage());
		try {
			document.add(erro);
		} catch (DocumentException de) {
			log.error(de);
		}
		document.close();
	}
	
	public void getDadosCliente() throws DocumentException {
		String nomeFantasia =dados.get("NM_FANTASIA"); 
		String identificacao  = dados.get("IDENTIFICACAO");
		String endereco = dados.get("ENDERECO");
		String nrIdentificacao  = dados.get("NUMERO");
		String cep  = dados.get("CEP");
		String telefone  = dados.get("TELEFONE");
		String fax  = dados.get("Fax");
		float[] w = {3.5f,1f,1f};
		PdfPTable  tableDados = new PdfPTable(w);
		tableDados.setWidthPercentage(100f);
		
		PdfPCell  vazio = getCellSemBorda("",false);
		
		PdfPCell cNomeFantasia = getCellSemBorda(nomeFantasia,true);
		tableDados.addCell(cNomeFantasia);
		tableDados.addCell(vazio);
		
		PdfPCell  cIdentificacao = getCellSemBorda(identificacao,false);
		tableDados.addCell(cIdentificacao);
		
		PdfPCell  cEndereco = getCellSemBorda(endereco,false);//
		tableDados.addCell(cEndereco);
		tableDados.addCell(vazio);
		
		PdfPCell  cNrIdentificacao = getCellSemBorda(nrIdentificacao,false);
		tableDados.addCell(cNrIdentificacao);
		
		PdfPCell  cCEP = getCellSemBorda(cep,false);
		tableDados.addCell(cCEP);
		tableDados.addCell(vazio);
		
		PdfPCell  ctelefone =getCellSemBorda(telefone,false);
		tableDados.addCell(ctelefone);
		
		PdfPCell  cFax = getCellSemBorda(fax,false);
		tableDados.addCell(cFax);
		
		document.add(tableDados);
	}

	private PdfPCell getCellSemBorda(String nomeFantasia, boolean bold) {
		PdfPCell  cNomeFantasia = new PdfPCell(new Phrase(nomeFantasia,bold ? FONT_8B : FONT_7));
		cNomeFantasia.setBorder(0);
		return cNomeFantasia;
	}

	public void setRodape(boolean rodape) {
		this.rodape = rodape;
}

	public boolean isRodape() {
		return rodape;
	}
}

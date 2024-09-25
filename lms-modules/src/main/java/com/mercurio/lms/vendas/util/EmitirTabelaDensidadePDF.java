package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTabelaDensidadePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, String>> listaGeneralidades;
	private List<Map<String, String>> listaFormalidades;
	private List<Map<String, String>> servicosAdicionais;
	private List<Map<String, String>> dificuldadeEntrega;
	
	public EmitirTabelaDensidadePDF(String usuario, List<Map<String, String>> listaPrecos,List<Map<String, String>> listaGeneralidades, List<Map<String, String>> listaFormalidades, List<Map<String, String>> servicosAdicionais, List<Map<String, String>> dificuldadeEntrega, List<Map<String, String>> legendas) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = listaGeneralidades;
		this.listaFormalidades = listaFormalidades;
		this.servicosAdicionais = servicosAdicionais;
		this.dificuldadeEntrega = dificuldadeEntrega;
		setUsuario(usuario);
		
		
		try {
			File file = File.createTempFile("report-listadepreco-tabela-densidade-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,true,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
				
	}
		
	public void addContent() throws DocumentException  {
			final float[] widths = {0.99f,1f,1f,1f,1f,1f,1f,1f,1f,1f};
			
			Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(listaTabelaPreco);
			
			int colspanTotal = widths.length;
			PdfPTable  tablePrincipal = new PdfPTable(widths);
			tablePrincipal.setSpacingBefore(20f);
			tablePrincipal.setSpacingAfter(20f);
			
			tablePrincipal.setWidthPercentage(100f);
			tablePrincipal.getDefaultCell().setFixedHeight(20f);
			
			PdfPTable cabecalhoModal = getCabecalhoTabelaDensidade();
			
			PdfPCell cell = new PdfPCell(cabecalhoModal);
			cell.setColspan(colspanTotal);		
			tablePrincipal.addCell(cell);
			
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("tarifa"), 1);
			
			PdfPTable cabecalho3 = getCabecalhoFretePeso();		
			PdfPCell tabelaInternaFretePeso = new PdfPCell(cabecalho3);
			tabelaInternaFretePeso.setColspan(8);
			
			addTitle(tablePrincipal,tabelaInternaFretePeso ,8);
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("valorAdValorem"), 1);
			
			montaValoresTabela(tablePrincipal,listaPrecos);
			
			PdfPCell celulaValores = new PdfPCell(tablePrincipal);
			celulaValores.setBorderWidthBottom(0f);
			celulaValores.setBorderWidthLeft(0f);
			celulaValores.setBorderWidthRight(0f);
			celulaValores.setBorderWidthTop(0f);
			
			montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades,listaFormalidades);
			
			montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega,servicosAdicionais);
			
			document.add(tablePrincipal);
			
	}

	private PdfPTable getCabecalhoTabelaDensidade() {
		float[] widthsCabecalho = {1f};
		PdfPTable  cabecalhoModal = new PdfPTable(widthsCabecalho);
		
		cabecalhoModal.setWidthPercentage(100f);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("tabelaDensidade"), 1);
		return cabecalhoModal;
	}

	private void montaValoresTabela(PdfPTable tablePrincipal,List<Map<String,String>> valoresList ){
		for (Map<String,String> map : valoresList) {
			if("A".equals(String.valueOf(map.get("DENSIDADE")))){				
				addCellRight(tablePrincipal, map.get("TARIFA"),1,0.5f,0,0.5f,0);	
			}
			addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_COLUNA")),1,0.5f,0,0.5f,0);
			if("H".equals(String.valueOf(map.get("DENSIDADE")))){				
				addCellRight(tablePrincipal, String.valueOf(map.get("ADVALOREM")),1,0.5f,0,0.5f,0.5f);
			}
		}
		addCellRight(tablePrincipal, " ",8,0,0,0,0);
		addCellRight(tablePrincipal, " ",8,0,0,0,0);
	}
	

	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f,1f,1f,1f,1f,1f,1f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePorKg"), 8);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("a"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("b"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("c"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("d"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("e"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("f"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("g"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("h"), 1);
		return cabecalho3;
	}
}

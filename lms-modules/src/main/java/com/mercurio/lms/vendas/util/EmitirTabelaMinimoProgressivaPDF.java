package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;


public class EmitirTabelaMinimoProgressivaPDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, String>> listaGeneralidades;
	private List<Map<String, String>> listaFormalidades;
	private List<Map<String, String>> servicosAdicionais;
	private List<Map<String, String>> dificuldadeEntrega;
	
	public EmitirTabelaMinimoProgressivaPDF(String usuario, List<Map<String, String>> listaPrecos,List<Map<String, String>> listaGeneralidades, List<Map<String, String>> listaFormalidades, List<Map<String, String>> servicosAdicionais, List<Map<String, String>> dificuldadeEntrega, List<Map<String, String>> legendas) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = listaGeneralidades;
		this.listaFormalidades = listaFormalidades;
		this.servicosAdicionais = servicosAdicionais;
		this.dificuldadeEntrega = dificuldadeEntrega;
		setUsuario(usuario);
		
		
		try {
			File file = File.createTempFile("report-listadepreco-mimimo-progressivo-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
			} catch (IOException e) {
				log.error(e);
			}
				
		}
		
	public void addContent() throws DocumentException  {
		final float[] widths = {0.4f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.6f,0.7f};
		
		Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
		document.add(listaTabelaPreco);
		
		int colspanTotal = widths.length;
		PdfPTable  tablePrincipal = new PdfPTable(widths);
		tablePrincipal.setSpacingBefore(20f);
		tablePrincipal.setSpacingAfter(20f);
		
		tablePrincipal.setWidthPercentage(100f);
		tablePrincipal.getDefaultCell().setFixedHeight(20f);
		
		PdfPTable cabecalhoModal = getCabecalhoModal();
		
		PdfPCell cell = new PdfPCell(cabecalhoModal);
		cell.setColspan(colspanTotal);
		
		tablePrincipal.addCell(cell);
		
		addTitle(tablePrincipal, getDefaultResourceBundle().getString("tarifa"), 1);
			
		PdfPTable cabecalho3 = getCabecalhoFretePeso();		
		PdfPCell tabelaInternaFretePeso = new PdfPCell(cabecalho3);
		tabelaInternaFretePeso.setColspan(9);
		
		addTitle(tablePrincipal,tabelaInternaFretePeso , 9);
		addTitle(tablePrincipal, getDefaultResourceBundle().getString("valorAdValorem"), 1);
		
		montaValoresTabela(tablePrincipal,listaPrecos);
		
		PdfPCell celulaValores = new PdfPCell(tablePrincipal);
		celulaValores.setBorderWidthBottom(0f);
		celulaValores.setBorderWidthLeft(0f);
		celulaValores.setBorderWidthRight(0f);
		celulaValores.setBorderWidthTop(0f);
		
		cabecalhoModal.addCell(celulaValores);

		montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades,listaFormalidades);
		
		montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega,servicosAdicionais);
		
		document.add(tablePrincipal);
	}		


	private PdfPTable getCabecalhoModal() {
		float[] widthsCabecalho = {1f,1f,1f};
		PdfPTable  cabecalhoModal = new PdfPTable(widthsCabecalho);
		
		cabecalhoModal.setWidthPercentage(100f);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("modalRodoviario"), 1);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("abrangenciaNacional"), 1);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("servicoExpresso"), 1);
		return cabecalhoModal;
	}
	


	private void montaValoresTabela(PdfPTable tablePrincipal,List<Map<String,String>> valoresList ){
		for (Map<String,String> map : valoresList) {
			if("10".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){
				if(map.get("COD_TARIFA")!= null ){
					addCellRight(tablePrincipal, String.valueOf(map.get("COD_TARIFA")),1,0.5f,0,0.5f,0);
				}
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("20".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("30".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("50".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("70".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("100".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("150".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){								
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
			}else if("200".equals(String.valueOf(map.get("FAIXA_PROGRESSIVA")))){				
				addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA")),1,0.5f,0,0.5f,0);
				if(map.get("VALOR_FAIXA_ADICIONAL") != null){					
					addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FAIXA_ADICIONAL")),1,0.5f,0,0.5f,0);
				}else{					
					addCellRight(tablePrincipal, String.valueOf(map.get("VALOR_FRETE")),1,0.5f,0,0.5f,0);
				}
				addCellRight(tablePrincipal, String.valueOf(map.get("VL_ADVALOREM")),1,0.5f,0,0.5f,0.5f);
			}
		}
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
	}

	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f,1f,1f,1f,1f,1f,1f,1.195f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePesoFaixas"), 9);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("10k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("20k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("30k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("50k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("70k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("100k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("150k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("200k"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("200Acima"), 1);
		return cabecalho3;
	}


	
}

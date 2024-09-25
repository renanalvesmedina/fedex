package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;


public class EmitirTabelaFreteMinimoTarifaClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> listaFormalidades;
	private List<Map<String, List<Map<String, String>>>> servicosAdicionais;
	private List<Map<String, List<Map<String, String>>>> dificuldadeEntrega;
	private int grupos = 1;
	
	public EmitirTabelaFreteMinimoTarifaClientePDF(int grupos, Map<String, String> dados, List<Map<String, String>> listaPrecos,List<Map<String, List<Map<String, String>>>> generalidadesList, List<Map<String, List<Map<String, String>>>> formalidadesList, List<Map<String, List<Map<String, String>>>> servicosAdicionaisList, List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList, List<Map<String, String>> legendas) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidadesList;
		this.listaFormalidades = formalidadesList;
		this.servicosAdicionais = servicosAdicionaisList;
		this.dificuldadeEntrega = dificuldadeEntregaList;
		this.grupos = grupos;
		setUsuario(null);
		this.dados = dados;
		
		
		try {
			File file = File.createTempFile("report-mimimo-tarifa-c" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
				
	}
		
	public void addContent() throws DocumentException  {
		final float[] widths = {2f,2f,2f,1.5f};
		int colspanTotal = widths.length;
		for (int i = 0; i < grupos; i++) {
			Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(listaTabelaPreco);
			
			
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
			tabelaInternaFretePeso.setColspan(2);
			
			addTitle(tablePrincipal,tabelaInternaFretePeso , 2);
			
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("valorAdValorem"), 1);
			
			montaValoresTabela(tablePrincipal,listaPrecos,"grupo"+i);
			
			
			PdfPCell celulaValores = new PdfPCell(tablePrincipal);
			celulaValores.setBorderWidthBottom(0f);
			celulaValores.setBorderWidthLeft(0f);
			celulaValores.setBorderWidthRight(0f);
			celulaValores.setBorderWidthTop(0f);
			
			cabecalhoModal.addCell(celulaValores);
			
			
			montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades.get(i).get("grupo"+i),listaFormalidades.get(i).get("grupo"+i));
			
			montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega.isEmpty() ? new ArrayList<Map<String,String>>() : dificuldadeEntrega.get(i).get("grupo"+i),servicosAdicionais.isEmpty() ? new ArrayList<Map<String,String>>(): servicosAdicionais.get(i).get("grupo"+i));
		
			document.add(tablePrincipal);
			addLinhaBranco(15f, colspanTotal, tablePrincipal);
		}
		

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
	

	private void montaValoresTabela(PdfPTable tablePrincipal,List<Map<String,String>> valoresList ,String grupo){
		for (Map<String,String> map : valoresList) {
			if(grupo.equals(map.get("GRUPO"))){
				addCellRight(tablePrincipal, String.valueOf(map.get("TARIFA")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal,String.valueOf(map.get("VL_PESO_MINIMO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("VL_FRETE_PESO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("VL_ADVALOREM")),1,0.5f,0,0.5f,0.5f);
			}
		}

		addCellRight(tablePrincipal, " ",11,0,0,0,0);
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
	}
	
	
	


	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		BigDecimal valorMinimoFrete = MapUtilsPlus.getBigDecimal(listaPrecos.get(0),"VL_MIN_FRETE_PESO");
		String valor = valorMinimoFrete == null ? "" : valorMinimoFrete.toString();
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePeso"), 2);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("pesoMinimo")+valor+getDefaultResourceBundle().getString("kgPorCTRC"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("valorKg"), 1);
		return cabecalho3;
	}


}

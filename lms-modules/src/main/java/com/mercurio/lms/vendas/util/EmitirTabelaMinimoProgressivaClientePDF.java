package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTabelaMinimoProgressivaClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, String>> listaGeneralidades;
	private List<Map<String, String>> listaFormalidades;
	private List<Map<String, String>> servicosAdicionais;
	private List<Map<String, String>> dificuldadeEntrega;
	
	public EmitirTabelaMinimoProgressivaClientePDF(Map<String, String>  dados, List<Map<String, String>> listaPrecos,List<Map<String, String>> generalidades, List<Map<String, String>> formalidades, List<Map<String, String>> servicosList, List<Map<String, String>> dificuldadeEntrega2, List<Map<String, String>> legendas) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidades;
		this.listaFormalidades = formalidades;
		this.servicosAdicionais = servicosList;
		this.dificuldadeEntrega = dificuldadeEntrega2;
		this.dados = dados;
		setUsuario(null);
		
		
		try {
			File file = File.createTempFile("report-listadepreco-mimimo-progressivo-c" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
					
	}
			
	public void addContent() throws DocumentException  {
		final float[] widths = {0.4f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.5f,0.6f,0.7f};
		
		Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
		document.add(listaTabelaPreco);
		
		PdfPTable cabecalhoProposta = getCabecalhoProposta(dados);
		if(cabecalhoProposta != null){
			cabecalhoProposta.setSpacingBefore(5f);
			document.add(cabecalhoProposta);
		}
		
		int colspanTotal = widths.length;
		PdfPTable  tablePrincipal = new PdfPTable(widths);
		tablePrincipal.setSpacingBefore(5f);
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
		
		if(dados.get("obs") != null){
			PdfPCell obs = new PdfPCell(new Phrase(dados.get("obs"), FONT_8B));
			obs.setColspan(colspanTotal);
			obs.setBorder(0);
			tablePrincipal.addCell(obs);
		}
		
		montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades,listaFormalidades);
		
		montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega,servicosAdicionais);
		
		document.add(tablePrincipal);
	}
	
	private PdfPTable getCabecalhoProposta(Map<String, String> dados) {
		if(dados.containsKey("NR_SIMULACAO") && dados.containsKey("DT_SIMULACAO") && dados.containsKey("TP_SIMULACAO")){
			float[] widthsCabecalho = {1f,1f,1f};
			PdfPTable  cabecalhoModal = new PdfPTable(widthsCabecalho);
			
			cabecalhoModal.setWidthPercentage(100f);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("nrProposta")+" "+dados.get("SG_FILIAL")+" "+dados.get("NR_SIMULACAO"), 1);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("dtInclusao")+" "+dados.get("DT_SIMULACAO"), 1);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("situacao")+" "+dados.get("TP_SIMULACAO"), 1);
			
			return cabecalhoModal;
		}
		return null;
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
					addCellRight(tablePrincipal, String.valueOf(map.get("COD_TARIFA")),1,0.5f,0,0.5f,0);
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

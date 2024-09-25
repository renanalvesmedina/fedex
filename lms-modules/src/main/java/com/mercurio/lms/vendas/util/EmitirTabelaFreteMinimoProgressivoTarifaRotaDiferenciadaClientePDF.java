
package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTabelaFreteMinimoProgressivoTarifaRotaDiferenciadaClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> listaFormalidades;
	private List<Map<String, List<Map<String, String>>>> servicosAdicionais;
	private List<Map<String, List<Map<String, String>>>> dificuldadeEntrega;
	private int grupos = 1;
	private String primeira = null;
	private String ultima = null;
	private int cont = 0;
	private float[] widthsFretePeso ;

	public EmitirTabelaFreteMinimoProgressivoTarifaRotaDiferenciadaClientePDF(int grupos, Map<String, String> dados, List<Map<String, String>> listaPrecos,List<Map<String, List<Map<String, String>>>> generalidadesList, List<Map<String, List<Map<String, String>>>> formalidadesList, List<Map<String, List<Map<String, String>>>> servicosAdicionaisList, List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList, List<Map<String, String>> legendas) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidadesList;
		this.listaFormalidades = formalidadesList;
		this.servicosAdicionais = servicosAdicionaisList;
		this.dificuldadeEntrega = dificuldadeEntregaList;
		this.grupos = grupos;
		setUsuario(null);
		this.dados = dados;
		
		
		try {
			File file = File.createTempFile("report-mimimo-progressivo-rota-c" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,true,this.getClass());
			} catch (IOException e) {
				log.error(e);
			}
					
		}

	public void addContent() throws DocumentException  {
		
		for (int i = 0; i < grupos; i++) {
			
			PdfPTable cabecalho3 = getCabecalhoFretePeso(listaPrecos);		
			PdfPCell tabelaInternaFretePeso = new PdfPCell(cabecalho3);
			tabelaInternaFretePeso.setColspan(cont);
			
			final float[] widths = new float[4+cont] ;
			widths[0] = 0.4f;
			widths[1] = 1.2f;
			widths[2] = 1.5f;
			for (int j = 3; j < widths.length; j++) {
				widths[j] = 0.5f;
			}
			widths[widths.length-2] = 0.6f;
			widths[widths.length-1] = 0.7f;
			
			int colspanTotal = widths.length;
			
			Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(listaTabelaPreco);
			
			PdfPTable cabecalhoProposta = getCabecalhoProposta(dados);
			if(cabecalhoProposta != null){
				cabecalhoProposta.setSpacingBefore(5f);
				document.add(cabecalhoProposta);
			}
			
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
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("origem"), 1);
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("destino"), 1);
				
			addTitle(tablePrincipal,tabelaInternaFretePeso , cont);
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("valorAdValorem"), 1);
			
			montaValoresTabela(tablePrincipal,listaPrecos,"grupo"+i);
			
			PdfPCell celulaValores = new PdfPCell(tablePrincipal);
			celulaValores.setBorderWidthBottom(0f);
			celulaValores.setBorderWidthLeft(0f);
			celulaValores.setBorderWidthRight(0f);
			celulaValores.setBorderWidthTop(0f);
			
			cabecalhoModal.addCell(celulaValores);
			
			montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades.isEmpty() ?  new ArrayList() :listaGeneralidades.get(i).get("grupo"+i),listaFormalidades.isEmpty() ?  new ArrayList() : listaFormalidades.get(i).get("grupo"+i));
			
			montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega.isEmpty() ?  new ArrayList() : dificuldadeEntrega.get(i).get("grupo"+i),servicosAdicionais.isEmpty() ?  new ArrayList() :  servicosAdicionais.get(i).get("grupo"+i));
		
			document.add(tablePrincipal);
			addLinhaBranco(15f, colspanTotal, tablePrincipal);
		}
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
	

	private void montaValoresTabela(PdfPTable tablePrincipal,List<Map<String,String>> valoresList, String grupo ){
			for (Map<String,String> map : valoresList) {
				if(primeira.equals(String.valueOf(map.get("VL_FAIXA_PROGRESSIVA")))){				
					addCellRight(tablePrincipal, map.get("CD_TARIFA"),1,0.5f,0,0.5f,0);						
					addCellLeft(tablePrincipal, map.get("ORIGEM"),1,0.5f,0,0.5f,0);
					addCellLeft(tablePrincipal, map.get("DESTINO"),1,0.5f,0,0.5f,0);
				}
				addCellRight(tablePrincipal, String.valueOf(map.get("VL_FIXO")),1,0.5f,0,0.5f,0);
				
				if(ultima.equals(String.valueOf(map.get("VL_FAIXA_PROGRESSIVA")))){		
					addCellRight(tablePrincipal, String.valueOf(map.get("FRETE_QUILO")),1,0.5f,0,0.5f,0);
					addCellRight(tablePrincipal, String.valueOf(map.get("VL_ADVALOREM")),1,0.5f,0,0.5f,0.5f);		
				}
						
			}
			addCellRight(tablePrincipal, " ",11,0,0,0,0);
			addCellRight(tablePrincipal, " ",11,0,0,0,0);
		}
		
	private PdfPTable getCabecalhoFretePeso(List<Map<String,String>> valoresList) {
		List<String> cabecalho = new ArrayList<String>();
		for (Map<String, String> map : valoresList) {
			if(primeira == null){
				primeira = String.valueOf(map.get("VL_FAIXA_PROGRESSIVA"));
			}
			else if(primeira.equals(String.valueOf(map.get("VL_FAIXA_PROGRESSIVA")))){
				cont++;
				break;
			}			
				ultima = String.valueOf(map.get("VL_FAIXA_PROGRESSIVA"));
				cont++;
				cabecalho.add(String.valueOf(map.get("VL_FAIXA_PROGRESSIVA")));
		}
		
		widthsFretePeso = new float[cont];
		
		for (int i = 0; i < cont; i++) {
			widthsFretePeso[i] = 1f;
		}
		widthsFretePeso[cont-1] = 1.195f;
		
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePesoFaixas"), cont);
		
		for (String c : cabecalho) {
			addCellCenter(cabecalho3, c+getDefaultResourceBundle().getString("kgPorCTRC"), 1);
		}
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("valorKgExcedente"), 1);
		return cabecalho3;
	}
}

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


public class EmitirTabelaFretePercentualClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> listaFormalidades;
	private List<Map<String, List<Map<String, String>>>> servicosAdicionais;
	private List<Map<String, List<Map<String, String>>>> dificuldadeEntrega;
	private List<Map<String, String>> legendas;
	private Map<String, Boolean> mapCifFob;
	
	private int grupos = 1;
	
	public EmitirTabelaFretePercentualClientePDF(int grupos, Map<String, String> dados, List<Map<String, String>> listaPrecos,List<Map<String, List<Map<String, String>>>> generalidadesList, List<Map<String, List<Map<String, String>>>> formalidadesList, List<Map<String, List<Map<String, String>>>> servicosAdicionaisList, List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList, List<Map<String, String>> legendas,Map<String, Boolean> mapCifFob) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidadesList;
		this.listaFormalidades = formalidadesList;
		this.servicosAdicionais = servicosAdicionaisList;
		this.dificuldadeEntrega = dificuldadeEntregaList;
		this.legendas = legendas;
		this.grupos = grupos;
		setUsuario(null);
		this.dados = dados;
		
		// LMS-6170
		this.mapCifFob = mapCifFob;
		
		
		try {
			File file = File.createTempFile("report-listadepreco-mimimo-progressivo-c" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
				
	}
		
	public void addContent() throws DocumentException  {
		final float[] widths = {2f,2f,1f,1f,1f,1f};
		int colspanTotal = widths.length;
		for (int i = 0; i < grupos; i++) {
			Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(listaTabelaPreco);
			
			PdfPTable cabecalhoProposta = getCabecalhoProposta(dados);
			if(cabecalhoProposta != null){
				cabecalhoProposta.setSpacingBefore(5f);
				document.add(cabecalhoProposta);
			}
			
			// LMS-6170
			PdfPTable cabecalhoCifFob = getCabecalhoCifFob(i);
			if (cabecalhoCifFob != null) {
				cabecalhoCifFob.setSpacingBefore(5f);
				document.add(cabecalhoCifFob);
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
			
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("origem"), 1);
			addTitle(tablePrincipal, getDefaultResourceBundle().getString("destino"), 1);
				
			PdfPTable cabecalho3 = getCabecalhoFretePeso();		
			PdfPCell tabelaInternaFretePeso = new PdfPCell(cabecalho3);
			tabelaInternaFretePeso.setColspan(6);
			
			addTitle(tablePrincipal,tabelaInternaFretePeso , 6);
			
			montaValoresTabela(tablePrincipal,listaPrecos,"grupo"+i);
			
			
			PdfPCell celulaValores = new PdfPCell(tablePrincipal);
			celulaValores.setBorderWidthBottom(0f);
			celulaValores.setBorderWidthLeft(0f);
			celulaValores.setBorderWidthRight(0f);
			celulaValores.setBorderWidthTop(0f);
			
			cabecalhoModal.addCell(celulaValores);
			
			
			montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades.get(i).get("grupo"+i),listaFormalidades.get(i).get("grupo"+i));
			
			montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega.isEmpty() ? new ArrayList<Map<String,String>>() : dificuldadeEntrega.get(i).get("grupo"+i),servicosAdicionais.isEmpty() ? new ArrayList<Map<String,String>>() : servicosAdicionais.get(i).get("grupo"+i));
		
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
	
	/**
	 * LMS-6170 - Conforme mapeamento por grupo, gera cabeçalho
	 * "CIF expedido FOB recebido" ou retorna <tt>null</tt> se não houver a
	 * opção para o grupo.
	 * 
	 * @param indexGrupo índice do grupo
	 * @return cabeçalho "CIF expedido FOB recebido" ou <tt>null</tt> se não
	 *         houver
	 */
	private PdfPTable getCabecalhoCifFob(int indexGrupo) {
		if (mapCifFob == null || !mapCifFob.get("grupo" + indexGrupo)) {
			return null;
		}
		PdfPTable cabecalhoCifFob = new PdfPTable(1);
		cabecalhoCifFob.setWidthPercentage(100f);
		String titulo = getDefaultResourceBundle().getString("cifFob");
		addTitle(cabecalhoCifFob, titulo, 1);
		return cabecalhoCifFob;
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
		String origem  = "";
		String destino  = "";
		for (Map<String,String> map : valoresList) {
			if(grupo.equals(map.get("GRUPO"))){
				if(origem.equals( map.get("ORIGEM")) && destino.equals( map.get("DESTINO"))){
					continue;
				}
				
				addCellLeft(tablePrincipal, map.get("ORIGEM"),1,0.5f,0,0.5f,0);
				addCellLeft(tablePrincipal, map.get("DESTINO"),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("PERCVALORNF")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("FRETECTRC")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("PESOMIN")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("FRETETONEL")),1,0.5f,0,0.5f,0.5f);
				
				origem  = map.get("ORIGEM");
				destino  = map.get("DESTINO");
				
			}
		}
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
	}
	
	

	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f,1f,1f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("tabelaPrecentual"), 4);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("percentualSobreNotaFiscal"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("freteMinimoCtrc"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("pesoMinimo"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("freteTonelada"), 1);
		return cabecalho3;
	}
	
}

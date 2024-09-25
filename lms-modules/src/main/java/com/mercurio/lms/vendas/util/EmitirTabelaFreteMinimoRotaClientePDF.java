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


public class EmitirTabelaFreteMinimoRotaClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> listaFormalidades;
	private List<Map<String, List<Map<String, String>>>> servicosAdicionais;
	private List<Map<String, List<Map<String, String>>>> dificuldadeEntrega;
	
	private List<Map<String, String>> generalidades;
	private List<Map<String, String>> formalidades;
	private List<Map<String, String>> lServicosAdicionais;
	private List<Map<String, String>> lDificuldadeEntrega;
	
	private int grupos = 1;
	private boolean grupo = false;
	private boolean pesoExcedente = false;

	// LMS-4526 - Mapa por grupo para cabeçalho "CIF expedido FOB recebido"
	private Map<String, Boolean> mapCifFob;

	public EmitirTabelaFreteMinimoRotaClientePDF(
			int grupos, 
			Map<String, String> dados, 
			List<Map<String, String>> listaPrecos,
			List<Map<String, List<Map<String, String>>>> generalidadesList, 
			List<Map<String, List<Map<String, String>>>> formalidadesList, 
			List<Map<String, List<Map<String, String>>>> servicosAdicionaisList, 
			List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList, 
			List<Map<String, String>> legendas,
			boolean pesoExcedente,
			// LMS-4526
			Map<String, Boolean> mapCifFob) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidadesList;
		this.listaFormalidades = formalidadesList;
		this.servicosAdicionais = servicosAdicionaisList;
		this.dificuldadeEntrega = dificuldadeEntregaList;
		this.grupos = grupos;
		setUsuario(null);
		this.dados = dados;
		this.grupo = true;
		this.pesoExcedente = pesoExcedente; 

		// LMS-4526
		this.mapCifFob = mapCifFob;

		try {
			File file = File.createTempFile("report-mimimo-progressivo-rota-c" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
			} catch (IOException e) {
				log.error(e);
			}
				
		}
			
	public void addContent() throws DocumentException  {
		final float[] widths = {2f,2f,2f,2f,1.5f};
		int colspanTotal = widths.length;
		for (int i = 0; i < grupos; i++) {
			Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(listaTabelaPreco);
			
			PdfPTable cabecalhoProposta = getCabecalhoProposta(dados);
			if(cabecalhoProposta != null){
				cabecalhoProposta.setSpacingBefore(5f);
				document.add(cabecalhoProposta);
			}

			// LMS-4526
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
			
			if(grupo){				
				montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,listaGeneralidades.get(i).get("grupo"+i),listaFormalidades.get(i).get("grupo"+i));
				
				montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,dificuldadeEntrega.get(i).get("grupo"+i),servicosAdicionais.get(i).get("grupo"+i));
			}else{
				montaGeneralidadeFormalidade(colspanTotal, tablePrincipal,generalidades,formalidades);
				
				montaServicosAdicionaisDificuldadeEntrega(colspanTotal, tablePrincipal,lDificuldadeEntrega,lServicosAdicionais);
			}
		
			document.add(tablePrincipal);
			addLinhaBranco(15f, colspanTotal, tablePrincipal);
		}
	}

	/**
	 * LMS-4526 - Conforme mapeamento por grupo, gera cabeçalho
	 * "CIF expedido FOB recebido" ou retorna <tt>null</tt> se não houver a
	 * opção para o grupo.
	 * 
	 * @param indexGrupo
	 *            índice do grupo
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
	
	public EmitirTabelaFreteMinimoRotaClientePDF(
			Map<String, String> dados,
			List<Map<String, String>> precosList,
			List<Map<String, String>> generalidades,
			List<Map<String, String>> formalidades,
			List<Map<String, String>> servicosList,
			List<Map<String, String>> dificuldadeEntrega,
			List<Map<String, String>> legendas) {
		this.listaPrecos = precosList;
		this.generalidades = generalidades;
		this.formalidades = formalidades;
		this.lServicosAdicionais = servicosList;
		this.lDificuldadeEntrega = dificuldadeEntrega;
		setUsuario(null);
		this.dados = dados;
		this.grupo = false;
		
		try {
			File file = File.createTempFile("report-mimimo-progressivo-rota-c_a-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			grupo = false;
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
		
	}

	
	public EmitirTabelaFreteMinimoRotaClientePDF(Map<String, String> dados,
			List<Map<String, String>> precosList,
			List<Map<String, String>> generalidades,
			List<Map<String, String>> formalidades,
			List<Map<String, String>> servicosList,
			List<Map<String, String>> dificuldadeEntrega,
			List<Map<String, String>> legendas, boolean pesoExcedente) {
		this.listaPrecos = precosList;
		this.generalidades = generalidades;
		this.formalidades = formalidades;
		this.lServicosAdicionais = servicosList;
		this.lDificuldadeEntrega = dificuldadeEntrega;
		setUsuario(null);
		this.dados = dados;
		this.grupo = false;
		this.pesoExcedente = pesoExcedente;
		
		try {
			File file = File.createTempFile("report-peso_excedente_-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			grupo = false;
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
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
	
	private PdfPTable getModal(Map parameters) {
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
				addCellLeft(tablePrincipal, String.valueOf(map.get("ORIGEM")),1,0.5f,0,0.5f,0);
				addCellLeft(tablePrincipal,String.valueOf(map.get("DESTINO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("VLMINIMOFRETEQUILO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("VLFRETEPESO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("VLADVALOREM")),1,0.5f,0,0.5f,0.5f);
			}
		}

		addCellRight(tablePrincipal, " ",11,0,0,0,0);
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
	}


	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		String valor = String.valueOf(listaPrecos.get(0).get("VLMINFRETEPESO"));
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePeso"), 2);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("pesoMinimo1") + valor +getDefaultResourceBundle().getString("kg")+ getDefaultResourceBundle().getString("rsCtrc"), 1);
		
		if(pesoExcedente){
			addCellCenter(cabecalho3, getDefaultResourceBundle().getString("valorKgExcedente"), 1);
		}else{			
			addCellCenter(cabecalho3, getDefaultResourceBundle().getString("valorKg"), 1);
		}
		
		
		return cabecalho3;
	}


}

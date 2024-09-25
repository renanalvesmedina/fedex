package com.mercurio.lms.vendas.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.util.BigDecimalUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmitirFreteAereoConvencionalPDF extends TemplatePdf {
	
	private static final String STRING_VAZIO = "";
	private static final String STRING_NULL = "null";
	private static final Color COLOR_1 = new Color(240, 240, 240);
	private static final Color COLOR_2 = new Color(220, 220, 220);

	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, String>> produtoEspecificoList;
	private List<Map<String, Object>> faixaPesoList;
	private List<Map<String, String>> legendas;
	private List<Map<String, String>> cabecalhoProdutoEspecifico;
	private List<Map<String, Object>> cabecalhoFaixaPeso;
	private List<Map<String, String>> aereo;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> servicosAdicionais;

	private List<Map<String, List<Map<String, String>>>> dificuldadeEntrega;
	private int grupos = 1;
	private int totalFaixaPesoPorLinha = 10;

	public EmitirFreteAereoConvencionalPDF(int grupos, Map<String, String> dados, List<Map<String, String>> listaPrecos, List<Map<String, String>> produtoEspecificoList,
			List<Map<String, Object>> faixaPesoList, List<Map<String, List<Map<String, String>>>> generalidadesList, List<Map<String, List<Map<String, String>>>> servicosAdicionaisList,
			List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList, List<Map<String, String>> legendas, List<Map<String, String>> aereo, List<Map<String, String>> cabecalhoProdutoEspecifico,
			List<Map<String, Object>> cabecalhoFaixaPeso) {

		this.listaPrecos = listaPrecos;
		this.produtoEspecificoList = produtoEspecificoList;
		this.faixaPesoList = faixaPesoList;
		this.listaGeneralidades = generalidadesList;
		this.servicosAdicionais = servicosAdicionaisList;
		this.dificuldadeEntrega = dificuldadeEntregaList;
		this.cabecalhoProdutoEspecifico = cabecalhoProdutoEspecifico;
		this.cabecalhoFaixaPeso = cabecalhoFaixaPeso;
		this.legendas = legendas;
		this.aereo = aereo;
		this.grupos = grupos;
		setUsuario(null);
		this.dados = dados;

		try {
			File file = File.createTempFile("report-convencional" + System.currentTimeMillis(), ".pdf",
					new ReportExecutionManager().generateOutputDir());
			getDocument(file, true, this.getClass());
		} catch (IOException e) {
			log.error(e);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addContent() throws DocumentException {
		float[] widthsPrincipal = { 1.7f, 3.14f, 1.2f, 1.2f, 0.75f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f, 0.77f};
		float[] widthsPsMinVlExc = { 1.9f, 6f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f};
		float[] widthsAdValorem = { 1.3f, 4.1f, 2f, 2f, 1.7f, 1.7f};
		int colspanTotal = widthsPrincipal.length;
		for (int i = 0; i < grupos; i++) {
			if(i != 0){
				document.newPage();
			}
			
			Paragraph tituloPrincipal = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
			document.add(tituloPrincipal);

			PdfPTable cabecalhoProposta = getCabecalhoProposta(dados);
			if (cabecalhoProposta != null) {
				cabecalhoProposta.setSpacingBefore(5f);
				document.add(cabecalhoProposta);
			}
			PdfPTable cabecalhoModal = getCabecalhoModal(dados);
			if (cabecalhoModal != null) {
				document.add(cabecalhoModal);
				cabecalhoModal.setSpacingBefore(5f);
			}

			PdfPTable tablePrincipal = new PdfPTable(widthsPrincipal);
			tablePrincipal.setSpacingBefore(5f);
			tablePrincipal.setSpacingAfter(20f);
			tablePrincipal.setWidthPercentage(100f);
			tablePrincipal.getDefaultCell().setFixedHeight(20f);
			tablePrincipal.setSplitLate(false);

			if(faixaPesoList != null && !faixaPesoList.isEmpty()){
				PdfPTable faixaPesoTable = getFaixaPesoTable();
				PdfPCell cellFaixaPeso = new PdfPCell(faixaPesoTable);
				cellFaixaPeso.setColspan(colspanTotal);
				cellFaixaPeso.setBorder(0);
				tablePrincipal.addCell(cellFaixaPeso);
				
				montarFaixaPeso(faixaPesoTable, faixaPesoList, i);
			}
			
			if(produtoEspecificoList != null && !produtoEspecificoList.isEmpty() && cabecalhoProdutoEspecifico != null && !cabecalhoProdutoEspecifico.isEmpty()){
				addLinhaBranco(8f, colspanTotal, tablePrincipal);
				
				PdfPTable produtoEspecificoModal = getProdutoEspecificoModal(dados);
				addCellLeft(produtoEspecificoModal, getDefaultResourceBundle().getString("tituloProdutoEspecifico"), colspanTotal, 0, 0, 0, 0, FONT_10);
				addLinhaBranco(4f, colspanTotal, produtoEspecificoModal);
				
				PdfPCell cellProdutoEspecifico = new PdfPCell(produtoEspecificoModal);
				cellProdutoEspecifico.setColspan(colspanTotal);
				cellProdutoEspecifico.setBorder(0);
				tablePrincipal.addCell(cellProdutoEspecifico);
				
				montaCabecalhoTabelaProdutoEspecifico(produtoEspecificoModal);
				montaValoresTabelaProdutoEspecifico(produtoEspecificoModal, produtoEspecificoList, "subgrupo" + i, produtoEspecificoModal.getNumberOfColumns());
			}
			
			if(listaPrecos != null && !listaPrecos.isEmpty()){
				addLinhaBranco(8f, colspanTotal, tablePrincipal);
				
				//SUPERIOR				
				PdfPTable tablePsMinVlExcSuperior = new PdfPTable(widthsPsMinVlExc);
				tablePsMinVlExcSuperior.setSpacingBefore(5f);
				tablePsMinVlExcSuperior.setSpacingAfter(20f);
				tablePsMinVlExcSuperior.setWidthPercentage(100f);
				tablePsMinVlExcSuperior.getDefaultCell().setFixedHeight(20f);
				addCellLeft(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("tituloTaxas"), colspanTotal, 0, 0, 0, 0, FONT_10);
				addLinhaBranco(4f, colspanTotal, tablePsMinVlExcSuperior);
				
				montaCabecalhoTabelaPesoMinimoValorExcedenteSuperior(tablePsMinVlExcSuperior, dados);
				montaValoresTabelaPesoMinimoValorExcedenteSuperior(tablePsMinVlExcSuperior, listaPrecos, "grupo" + i);
				
				PdfPCell cellPsMinVlExcSuperior = new PdfPCell(tablePsMinVlExcSuperior);
				cellPsMinVlExcSuperior.setColspan(colspanTotal);
				cellPsMinVlExcSuperior.setBorder(0);
				tablePrincipal.addCell(cellPsMinVlExcSuperior);
				
				//INFERIOR				
				PdfPTable tablePsMinVlExcInferior = new PdfPTable(widthsPsMinVlExc);
				tablePsMinVlExcInferior.setSpacingBefore(5f);
				tablePsMinVlExcInferior.setSpacingAfter(20f);
				tablePsMinVlExcInferior.setWidthPercentage(100f);
				tablePsMinVlExcInferior.getDefaultCell().setFixedHeight(20f);
				addCellLeft(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("tituloTaxas"), colspanTotal, 0, 0, 0, 0, FONT_10);
				addLinhaBranco(4f, colspanTotal, tablePsMinVlExcInferior);
				
				montaCabecalhoTabelaPesoMinimoValorExcedenteInferior(tablePsMinVlExcInferior, dados);
				montaValoresTabelaPesoMinimoValorExcedenteInferior(tablePsMinVlExcInferior, listaPrecos, "grupo" + i);
				
				PdfPCell cellPsMinVlExcInferior = new PdfPCell(tablePsMinVlExcInferior);
				cellPsMinVlExcInferior.setColspan(colspanTotal);
				cellPsMinVlExcInferior.setBorder(0);
				tablePrincipal.addCell(cellPsMinVlExcInferior);
				
				//ADVALOREM
				PdfPTable tableAdValorem = new PdfPTable(widthsAdValorem);
				tablePsMinVlExcInferior.setSpacingBefore(5f);
				tablePsMinVlExcInferior.setSpacingAfter(20f);
				tablePsMinVlExcInferior.setWidthPercentage(100f);
				tablePsMinVlExcInferior.getDefaultCell().setFixedHeight(20f);
				
				addCellLeft(tableAdValorem, getDefaultResourceBundle().getString("tituloAdValoremDemaisTaxas"), colspanTotal, 0, 0, 0, 0, FONT_10);
				addLinhaBranco(4f, colspanTotal, tableAdValorem);
				montaCabecalhoTabelaAdValorem(tableAdValorem, dados);
				montaValoresTabelaAdValorem(tableAdValorem, listaPrecos, "grupo" + i);
				
				PdfPCell cellAdvalorem = new PdfPCell(tableAdValorem);
				cellAdvalorem.setColspan(colspanTotal);
				cellAdvalorem.setBorder(0);
				tablePrincipal.addCell(cellAdvalorem);
			}

			if(listaGeneralidades != null && !listaGeneralidades.isEmpty()){
				montaGeneralidadeFormalidade(colspanTotal, tablePrincipal, listaGeneralidades.get(i).get("grupo" + i), new ArrayList());
			}
			
			if(dificuldadeEntrega != null && !dificuldadeEntrega.isEmpty()){
				montaDificuldadesEntrega(tablePrincipal, dificuldadeEntrega.get(i).get("grupo" + i), colspanTotal);
			}
			
			if(servicosAdicionais != null && !servicosAdicionais.isEmpty()){
				montaServicosAdicionais(tablePrincipal, servicosAdicionais.get(i).get("grupo" + i), colspanTotal);
			}

			if(legendas != null && !legendas.isEmpty()){
				getLegenda(colspanTotal, tablePrincipal, legendas,aereo);
			}
			
			montaFinalQuadro(colspanTotal, tablePrincipal);

			document.add(tablePrincipal);
			addLinhaBranco(15f, colspanTotal, tablePrincipal);
		}

	}

	private PdfPTable getFaixaPesoTable() {
		float cabecalho = 1.0f;
		float[] widthsCabecalho = {1.1f, 2.1f, 1.1f};
		
		for (int i = 0; i < totalFaixaPesoPorLinha; i++) {
			widthsCabecalho = ArrayUtils.add(widthsCabecalho, cabecalho);
		}
		
		PdfPTable faixaPesoTable = new PdfPTable(widthsCabecalho);
		faixaPesoTable.setWidthPercentage(100f);
		return faixaPesoTable;
	}
	
	private void montarFaixaPeso(PdfPTable faixaPesoTable, List<Map<String, Object>> faixaPesoList, int grupo) {
		String grupoFaixaPeso = "grupofaixapeso" + grupo;
		String grupoFaixaPesoCabecalho = "grupofaixapesocabecalho" + grupo;
		
		List<Map<String, Object>> faixaPesoGrupoList = getFaixaPesoGrupoList(faixaPesoList, grupoFaixaPeso);
		List<BigDecimal> cabecalhoFaixaPesoGrupo = getCabeCalhoFaixaPesoGrupo(grupoFaixaPesoCabecalho);
		
		if(!faixaPesoGrupoList.isEmpty() && !cabecalhoFaixaPesoGrupo.isEmpty()){
			while (!faixaPesoGrupoList.isEmpty()){
				List<BigDecimal> cabecalhosDinamicos = getCabecalhosDinamicosFaixaPeso(cabecalhoFaixaPesoGrupo);
				montarLinhaFaixaPeso(faixaPesoTable, faixaPesoGrupoList, grupoFaixaPeso, cabecalhosDinamicos);
			}
		}
	}
	
	private List<BigDecimal> getCabeCalhoFaixaPesoGrupo(String grupo) {
		List<BigDecimal> cabecalhoFaixaPesoGrupo = new ArrayList<BigDecimal>();
		
		for (Iterator<Map<String, Object>> iterator = cabecalhoFaixaPeso.iterator(); iterator.hasNext();) {
			Map mapGrupo = iterator.next();
			cabecalhoFaixaPesoGrupo.add((BigDecimal)mapGrupo.get("valor_faixa"));
			iterator.remove();
		}
		
		return cabecalhoFaixaPesoGrupo;
	}

	private List<Map<String, Object>> getFaixaPesoGrupoList(List<Map<String, Object>> faixaPesoList, String grupo){
		List<Map<String, Object>> faixaPesoGrupoList = new ArrayList<Map<String, Object>>();
		
		for (Iterator<Map<String, Object>> iterator = faixaPesoList.iterator(); iterator.hasNext();) {
			Map<String, Object> mapGrupo = iterator.next();

			if(grupo.equals(mapGrupo.get("GRUPOFAIXAPESO"))){
				faixaPesoGrupoList.add(mapGrupo);
				iterator.remove();
			}
		}
		
		return faixaPesoGrupoList;
	}
	
	private List<BigDecimal> getCabecalhosDinamicosFaixaPeso(List<BigDecimal> cabecalhoFaixaPesoGrupo){
		List<BigDecimal> cabecalhosDinamicos = new ArrayList<BigDecimal>();
		
		for (int i = 0; i < totalFaixaPesoPorLinha; i++) {
			if(!cabecalhoFaixaPesoGrupo.isEmpty()){
				cabecalhosDinamicos.add(cabecalhoFaixaPesoGrupo.get(0));
				cabecalhoFaixaPesoGrupo.remove(0);
			}
		}
		
		return cabecalhosDinamicos;
	}
	
	private void montarLinhaFaixaPeso(PdfPTable faixaPesoTable, List<Map<String, Object>> faixaPesoGrupoList, String grupo, List<BigDecimal> cabecalhosDinamicos) {
		addCabecalhoFaixaPeso(faixaPesoTable, cabecalhosDinamicos);
		
		Map mapInicial = faixaPesoGrupoList.get(0);
		String destinoAtual = (String) mapInicial.get("dsDestino");

		addValoresColunasFixasFaixaPeso(faixaPesoTable, mapInicial);
		
		int totalFaixasAdicionadas = 0;
		for (Iterator<Map<String, Object>> iterator = faixaPesoGrupoList.iterator(); iterator.hasNext();) {
			Map map = iterator.next();
			
			for (BigDecimal vlFaixa : cabecalhosDinamicos) {
				if(vlFaixa.equals((BigDecimal) map.get("vlFaixa"))){
					
					if(!destinoAtual.equals((String) map.get("dsDestino"))){
						if(totalFaixasAdicionadas < totalFaixaPesoPorLinha){
							for (int i = totalFaixasAdicionadas; i < totalFaixaPesoPorLinha; i++) {
								addCellRight(faixaPesoTable, "", 1, 0, 0, 0, 0);
							}
						}
						totalFaixasAdicionadas = 0;
						
						destinoAtual = (String) map.get("dsDestino");
						addValoresColunasFixasFaixaPeso(faixaPesoTable, map);
					}
					
					addCellRight(faixaPesoTable, (String) map.get("valorFixo"), 1, 0.5f, 0, 0.5f, 0.5f);
					totalFaixasAdicionadas++;
					iterator.remove();
				}
			}
		}
		
		addLinhaBranco(4f, faixaPesoTable.getNumberOfColumns(), faixaPesoTable);
	}

	private void addValoresColunasFixasFaixaPeso(PdfPTable faixaPesoTable, Map map) {
		addCellLeft(faixaPesoTable,  String.valueOf(map.get("dsRegiao")), 1, 0.5f, 0, 0.5f, 0);
		addCellLeft(faixaPesoTable,  String.valueOf(map.get("dsDestino")), 1, 0.5f, 0, 0.5f, 0);
		addCellRight(faixaPesoTable,  String.valueOf(map.get("vlTarifa")), 1, 0.5f, 0, 0.5f, 0);
		
	}

	private void addCabecalhoFaixaPeso(PdfPTable faixaPesoTable, List<BigDecimal> cabecalhosDinamicos) {
		addTitleCustom(faixaPesoTable, getDefaultResourceBundle().getString("regiao"), 1);
		addTitleCustom(faixaPesoTable, getDefaultResourceBundle().getString("destino"), 1);
		addTitleCustom(faixaPesoTable, getDefaultResourceBundle().getString("tarifaMinima"), 1);
		
		for (BigDecimal vlFaixa : cabecalhosDinamicos) {
			addTitleCustom(faixaPesoTable, vlFaixa.toString() + " Kg", 1);
		}
		
		completarCelulaEmBrancoFaixaPeso(faixaPesoTable, cabecalhosDinamicos);
	}

	private void completarCelulaEmBrancoFaixaPeso(PdfPTable faixaPesoTable, List<BigDecimal> cabecalhosDinamicos) {
		if(cabecalhosDinamicos.size() < totalFaixaPesoPorLinha){
			for (int i = cabecalhosDinamicos.size(); i < totalFaixaPesoPorLinha; i++) {
				addCellRight(faixaPesoTable, "", 1, 0, 0, 0, 0);
			}
		}
	}
	
	private void addTitleCustom(PdfPTable table, String titulo, int colspan) {		
		PdfPCell c1 = new PdfPCell(new Phrase(titulo, FONT_7B));
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setColspan(colspan);
		c1.setGrayFill(0.75f);
		table.addCell(c1);
	}
	
	private void addCabecalhoComposto(PdfPTable table, String titulo, int colspan) {
		float[] widthsCabecalhoComposto = { 1.2f, 1.2f, 1.2f};
		
		PdfPTable tableCabecalho = new PdfPTable(widthsCabecalhoComposto);
		tableCabecalho.setSpacingBefore(0f);
		tableCabecalho.setSpacingAfter(0f);
		
		tableCabecalho.setWidthPercentage(100f);
		tableCabecalho.getDefaultCell().setFixedHeight(20f);
		
		addTitleCustom(tableCabecalho, titulo, 3);
		addTitleCustom(tableCabecalho, getDefaultResourceBundle().getString("valor"), 1);
		addTitleCustom(tableCabecalho, getDefaultResourceBundle().getString("pesoMin"), 1);
		addTitleCustom(tableCabecalho, getDefaultResourceBundle().getString("valorExc"), 1);
		
		PdfPCell c1 = new PdfPCell(new Phrase());
		c1.setHorizontalAlignment(Element.ALIGN_CENTER);
		c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		c1.setColspan(colspan);
		c1.setGrayFill(0.75f);
		c1.setPadding(-0f);
		
		c1.addElement(tableCabecalho);
		
		table.addCell(c1);
	}

	private PdfPTable getCabecalhoProposta(Map<String, String> dados) {
		if (dados.containsKey("NR_SIMULACAO") && dados.containsKey("DT_SIMULACAO") && dados.containsKey("TP_SIMULACAO")) {
			float[] widthsCabecalho = { 1f, 1f, 1f };
			PdfPTable cabecalhoModal = new PdfPTable(widthsCabecalho);

			cabecalhoModal.setWidthPercentage(100f);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("nrProposta") + " " + dados.get("SG_FILIAL") + " " + dados.get("NR_SIMULACAO"), 1);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("dtInclusao") + " " + dados.get("DT_SIMULACAO"), 1);
			addTitle(cabecalhoModal, getDefaultResourceBundle().getString("situacao") + " " + dados.get("TP_SIMULACAO"), 1);

			return cabecalhoModal;
		}
		return null;
	}

	private PdfPTable getCabecalhoModal(Map<String, String> dados) {
		float[] widthsCabecalho = {1f, 1f, 1f, 1f };
		PdfPTable cabecalhoModal = new PdfPTable(widthsCabecalho);

		cabecalhoModal.setWidthPercentage(100f);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("origem") + ": " + (String) dados.get("sgAeroportoOrigem"), 1);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("modalAereo"), 1);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString("abrangencia") + " " + dados.get("ABRANGENCIA"), 1);
		addTitle(cabecalhoModal, getDefaultResourceBundle().getString(dados.get("LABEL_TIPO_PROPOSTA")), 1);
		return cabecalhoModal;
	}
	
	private void montaCabecalhoTabelaPesoMinimoValorExcedenteSuperior(PdfPTable tablePsMinVlExcSuperior, Map<String, String> dados) {
		addTitleCustom(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("regiao"), 1);
		addTitleCustom(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("destino"), 1);
		
		addCabecalhoComposto(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("taxaColetaUrbanaConvencional"), 3);
		addCabecalhoComposto(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("taxaColetaInteriorConvencional"), 3);
		addCabecalhoComposto(tablePsMinVlExcSuperior, getDefaultResourceBundle().getString("taxaEntregaUrbanaConvencional"), 3);
	}
	
	private void montaCabecalhoTabelaPesoMinimoValorExcedenteInferior(PdfPTable tablePsMinVlExcInferior, Map<String, String> dados) {
		addTitleCustom(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("regiao"), 1);
		addTitleCustom(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("destino"), 1);
		
		addCabecalhoComposto(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("taxaEntregaInteriorConvencional"), 3);
		addCabecalhoComposto(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("taxaColetaUrbanaEmergencial"), 3);
		addCabecalhoComposto(tablePsMinVlExcInferior, getDefaultResourceBundle().getString("taxaEntregaUrbanaEmergencial"), 3);
	}
	
	private void montaCabecalhoTabelaAdValorem(PdfPTable tableAdValorem, Map<String, String> dados) {
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("regiao"), 1);
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("destino"), 1);
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("taxaColetaInteriorEmergencial"), 1);
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("taxaEntregaInteriorEmergencial"), 1);
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("advalorem1"), 1);
		addTitleCustom(tableAdValorem, getDefaultResourceBundle().getString("advalorem2"), 1); 
	}
	
	private void montaValoresTabelaPesoMinimoValorExcedenteSuperior(PdfPTable tablePsMinVlExcSuperior, List<Map<String, String>> valoresList, String grupo) {
		for (Map<String, String> map : valoresList) {
			if (grupo.equals(map.get("GRUPO"))) {
				addCellLeft(tablePsMinVlExcSuperior,  String.valueOf(map.get("dsRegiao")), 1, 0.5f, 0, 0.5f, 0);
				addCellLeft(tablePsMinVlExcSuperior,  String.valueOf(map.get("dsDestino")), 1, 0.5f, 0, 0.5f, 0);
				
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("taxaColUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("psMinimoColUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("vlExcedenteColUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("taxaColIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("psMinimoColIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("vlExcedenteColIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("taxaEntUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("psMinimoEntUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcSuperior, defaultString(String.valueOf(map.get("vlExcedenteEntUrbConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0.5f, COLOR_2);
				
			}
		}
		addCellRight(tablePsMinVlExcSuperior, " ", 11, 0, 0, 0, 0);
	}
	
	private void addCellRightCustom(PdfPTable table, String string,int colspan,float borderBottom,float borderTop,float borderLeft,float borderRight, Color color) {
		PdfPCell c1 = new PdfPCell(new Phrase(string, FONT_7));
		c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c1.setColspan(colspan);
		c1.setBorderWidthBottom(borderBottom);
		c1.setBorderWidthLeft(borderLeft);
		c1.setBorderWidthRight(borderRight);
		c1.setBorderWidthTop(borderTop);
		c1.setBackgroundColor(new BaseColor(color));
		table.addCell(c1);
	}
	
	private void montaValoresTabelaPesoMinimoValorExcedenteInferior(PdfPTable tablePsMinVlExcInferior, List<Map<String, String>> valoresList, String grupo) {
		for (Map<String, String> map : valoresList) {
			if (grupo.equals(map.get("GRUPO"))) {
				addCellLeft(tablePsMinVlExcInferior,  String.valueOf(map.get("dsRegiao")), 1, 0.5f, 0, 0.5f, 0);
				addCellLeft(tablePsMinVlExcInferior,  String.valueOf(map.get("dsDestino")), 1, 0.5f, 0, 0.5f, 0);
				
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("taxaEntIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("psMinimoEntIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("vlExcedenteEntIntConvencional")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("taxaColUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("psMinimoColUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("vlExcedenteColUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_1);
				
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("taxaEntUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("psMinimoEntUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0, COLOR_2);
				addCellRightCustom(tablePsMinVlExcInferior, defaultString(String.valueOf(map.get("vlExcedenteEntUrbEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0.5f, COLOR_2);
				
				
			}
		}
		addCellRight(tablePsMinVlExcInferior, " ", 11, 0, 0, 0, 0);
	}
	
	private void montaValoresTabelaAdValorem(PdfPTable tableAdValorem, List<Map<String, String>> valoresList, String grupo) {
		for (Map<String, String> map : valoresList) {
			if (grupo.equals(map.get("GRUPO"))) {
				addCellLeft(tableAdValorem,  String.valueOf(map.get("dsRegiao")), 1, 0.5f, 0, 0.5f, 0);
				addCellLeft(tableAdValorem,  String.valueOf(map.get("dsDestino")), 1, 0.5f, 0, 0.5f, 0);
				addCellRight(tableAdValorem, defaultString(String.valueOf(map.get("taxaColIntEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0);
				addCellRight(tableAdValorem, defaultString(String.valueOf(map.get("taxaEntIntEmergencial")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0);
				addCellRight(tableAdValorem, defaultString(String.valueOf(map.get("advalorem1")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0);
				addCellRight(tableAdValorem, defaultString(String.valueOf(map.get("advalorem2")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, 0.5f);
			}
		}
		addCellRight(tableAdValorem, " ", 11, 0, 0, 0, 0);
	}
	
	private PdfPTable getProdutoEspecificoModal(Map<String, String> dados) {
		float cabecalho = 1.0f;
		float[] widthsCabecalho = {1.1f, 2.1f};
		
		for (Map<String, String> map : cabecalhoProdutoEspecifico) {
			widthsCabecalho = ArrayUtils.add(widthsCabecalho, cabecalho);
		}
		
		PdfPTable produtoEspecificoModal = new PdfPTable(widthsCabecalho);
		produtoEspecificoModal.setWidthPercentage(100f);
		return produtoEspecificoModal;
	}
	
	private void montaCabecalhoTabelaProdutoEspecifico(PdfPTable tablePrincipal) {
		addTitleCustom(tablePrincipal, getDefaultResourceBundle().getString("regiao"), 1);
		addTitleCustom(tablePrincipal, getDefaultResourceBundle().getString("destino"), 1);
		
		for (Map<String, String> map : cabecalhoProdutoEspecifico) {
			String dsCabecalhoAux = "000"+String.valueOf(map.get("ds_cabecalho"));
			dsCabecalhoAux = dsCabecalhoAux.substring(dsCabecalhoAux.length()-3, dsCabecalhoAux.length());
			addTitleCustom(tablePrincipal, getDefaultResourceBundle().getString("freteKgEspecifico") + " TE" + dsCabecalhoAux, 1);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void montaValoresTabelaProdutoEspecifico(PdfPTable produtoEspecificoModal, List<Map<String, String>> valoresList, String grupo, int colspanTotal) {
		Set destinosUtilizados = new HashSet();
		
		for (Map<String, String> map : valoresList) {
			if (grupo.equals(map.get("SUBGRUPO"))) {
				
				if(!destinosUtilizados.contains(map.get("dsDestino"))){
					destinosUtilizados.add(map.get("dsDestino"));
				} else {
					continue;
				}
				
				addCellLeft(produtoEspecificoModal,  String.valueOf(map.get("dsRegiao")), 1, 0.5f, 0, 0.5f, 0);
				addCellLeft(produtoEspecificoModal,  String.valueOf(map.get("dsDestino")), 1, 0.5f, 0, 0.5f, 0);

				int totalAdicionadas = 0;
				for (Map<String, String> mapCabecalho : cabecalhoProdutoEspecifico) {
					boolean achou = false;
					float bordaUltimaColuna = 0;
					
					for (Map<String, String> map2 : valoresList) {
						if (grupo.equals(map2.get("SUBGRUPO"))) {
							if(BigDecimalUtils.getBigDecimal(map2.get("nrTarifaEspecifica")).equals(BigDecimalUtils.getBigDecimal(mapCabecalho.get("ds_cabecalho"))) && map.get("dsDestino").equals((map2.get("dsDestino")))){
								totalAdicionadas++;
								
								if(totalAdicionadas == cabecalhoProdutoEspecifico.size()){
									bordaUltimaColuna = 0.5f;
								}
									
								addCellRight(produtoEspecificoModal, defaultString(String.valueOf(map2.get("valorFixo")), STRING_VAZIO), 1, 0.5f, 0, 0.5f, bordaUltimaColuna);
								bordaUltimaColuna = 0;
								achou = true;
							} 
						}
					}
					
					if(!achou){
						totalAdicionadas++;
						if(totalAdicionadas == cabecalhoProdutoEspecifico.size()){
							bordaUltimaColuna = 0.5f;
							totalAdicionadas = 0;
						}
						addCellRight(produtoEspecificoModal, STRING_VAZIO, 1, 0.5f, 0, 0.5f, bordaUltimaColuna);
						bordaUltimaColuna = 0;
					}
				}
			}
		}
		
		addLinhaBranco(4f, colspanTotal, produtoEspecificoModal);
	}
	
	private String defaultString(String value, String defaultValue) {
        return STRING_NULL.endsWith(value) ? defaultValue : value;
    }

	public List<Map<String, String>> getLegendas() {
		return legendas;
	}

	public void setLegendas(List<Map<String, String>> legendas) {
		this.legendas = legendas;
	}

	public List<Map<String, String>> getAereo() {
		return aereo;
	}

	public void setAereo(List<Map<String, String>> aereo) {
		this.aereo = aereo;
	}

	public List<Map<String, String>> getCabecalhoProdutoEspecifico() {
		return cabecalhoProdutoEspecifico;
	}

	public void setCabecalhoProdutoEspecifico(List<Map<String, String>> cabecalhoProdutoEspecifico) {
		this.cabecalhoProdutoEspecifico = cabecalhoProdutoEspecifico;
	}
}

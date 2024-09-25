package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTabelaFreteAereaClientePDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, List<Map<String, String>>>> listaGeneralidades;
	private List<Map<String, List<Map<String, String>>>> listaFormalidades;
	private List<Map<String, String>> servicosAdicionais;
	private List<Map<String, String>> dificuldadeEntrega;
	private List<Map<String, String>> legendas;
	private List<Map<String, String>> aereo;
	private List<Map<String, String>> taxaCombustivel; 
	private Set taxaCombustivelCabecalho;
	private List<Map<String, String>> coleta;
	private List<Map<String, String>> entrega;
	private List<Map<String, String>> terrestre;
	private List<Map<String, String>> legendaGeneralidades;
	
	public EmitirTabelaFreteAereaClientePDF(List<Map<String, String>> cabecalho, Map<String, String> dados, List<Map<String, String>> listaPrecos,List<Map<String, List<Map<String, String>>>> generalidadesList, List<Map<String, List<Map<String, String>>>> formalidadesList, List<Map<String, String>> servicosAdicionais, List<Map<String, String>> dificuldadeEntrega, List<Map<String, String>> legendas, List<Map<String, String>> aereo, List<Map<String, String>> taxaCombustivel, Set taxaCombustivelCabecalho, List<Map<String, String>> coleta, List<Map<String, String>> entrega, List<Map<String, String>> legendaGeneralidades,List<Map<String, String>> terrestre) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = generalidadesList;
		this.listaFormalidades = formalidadesList;
		this.servicosAdicionais = servicosAdicionais;
		this.dificuldadeEntrega = dificuldadeEntrega;
		this.legendas = legendas;
		this.aereo = aereo;
		this.taxaCombustivel = taxaCombustivel;
		this.taxaCombustivelCabecalho = taxaCombustivelCabecalho;
		this.coleta = coleta;
		this.entrega = entrega;
		this.legendaGeneralidades = legendaGeneralidades;
		this.terrestre = terrestre;
		setUsuario(null);
		this.dados = dados;
		
		
		try {
			File file = File.createTempFile("report-listadepreco-aereo-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
				
	}
		
	public void addContent() throws DocumentException  {
			float[] w = {2f,2f,2f,2f,2f};
			int total = w.length;
			
			PdfPTable  tableConteudo = new PdfPTable(w);
			tableConteudo.setWidthPercentage(100f);
			
			final float[] widths = {2f,2f,2f,2f,1.5f};
			int colspanTotal = widths.length;
				
			String origem = "";
			int cont = 0;
			for (Map<String, String> cab : listaPrecos) {		
				if(!origem.equals(cab.get("origem"))){
					if(!"".equals(origem)){
						addLinhaBranco(15f, total, tableConteudo);
						
						getTaxaColeta(total,tableConteudo);
						getTaxaEntrega(total,tableConteudo);					
						getTaxaTerrestreCliente(total, tableConteudo,terrestre);
						
						montaGeneralidadeFormalidade(total, tableConteudo,listaGeneralidades.get(cont).get("grupo"+cont),listaFormalidades.get(cont).get("grupo"+cont));
						montaServicosAdicionaisDificuldadeEntrega(total, tableConteudo,dificuldadeEntrega,servicosAdicionais);
						getLegendaGeneralidade(total, tableConteudo,legendaGeneralidades);						
						
						
						getLegenda(total, tableConteudo,legendas,aereo);
						
						addLinhaBranco(15f, total, tableConteudo);
						
						getTaxaCombustivelCliente(total, tableConteudo,taxaCombustivelCabecalho,taxaCombustivel);
						cont++;
					}
					origem = cab.get("origem");
					addLinhaBranco(15f, total, tableConteudo);
					
					Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
					document.add(listaTabelaPreco);
					
					tableConteudo.setSpacingBefore(20f);
					tableConteudo.setSpacingAfter(20f);
					
					tableConteudo.setWidthPercentage(100f);
					tableConteudo.getDefaultCell().setFixedHeight(20f);
					
					PdfPTable cabecalhoModal = getCabecalhoModal();
					
					PdfPCell cell = new PdfPCell(cabecalhoModal);
					cell.setColspan(colspanTotal);
					
					tableConteudo.addCell(cell);
					
					addTitle(tableConteudo, getDefaultResourceBundle().getString("origem"), 1);
					addTitle(tableConteudo, getDefaultResourceBundle().getString("destino"), 1);
						
					PdfPTable cabecalho3 = getCabecalhoFretePeso();		
					PdfPCell tabelaInternaFretePeso = new PdfPCell(cabecalho3);
					tabelaInternaFretePeso.setColspan(2);
					
					addTitle(tableConteudo,tabelaInternaFretePeso , 2);
					
					addTitle(tableConteudo, getDefaultResourceBundle().getString("excedenteKg"), 1);
					
					montaValoresTabela(tableConteudo,listaPrecos,"grupo"+cont);
					
					PdfPCell celulaValores = new PdfPCell(tableConteudo);
					celulaValores.setBorderWidthBottom(0f);
					celulaValores.setBorderWidthLeft(0f);
					celulaValores.setBorderWidthRight(0f);
					celulaValores.setBorderWidthTop(0f);
					
					cabecalhoModal.addCell(celulaValores);
				}
				
			}
			if(cont == 0){
				addLinhaBranco(15f, total, tableConteudo);
				
				getTaxaColeta(total,tableConteudo);
				getTaxaEntrega(total,tableConteudo);					
				getTaxaTerrestreCliente(total, tableConteudo,terrestre);
				
				montaGeneralidadeFormalidade(total, tableConteudo,listaGeneralidades.get(cont).get("grupo"+cont),listaFormalidades.get(cont).get("grupo"+cont));
				montaServicosAdicionaisDificuldadeEntrega(total, tableConteudo,dificuldadeEntrega,servicosAdicionais);
				getLegendaGeneralidade(total, tableConteudo,legendaGeneralidades);						
				
				getLegenda(total, tableConteudo,legendas,aereo);
				
				addLinhaBranco(15f, total, tableConteudo);
				
				getTaxaCombustivelCliente(total, tableConteudo,taxaCombustivelCabecalho,taxaCombustivel);
				
				addLinhaBranco(15f, colspanTotal, tableConteudo);
				document.add(tableConteudo);
			}
	}
	
	public void getTaxaCombustivelCliente(int total, PdfPTable tableConteudo,Set taxaCombustivelCabecalho,  List<Map<String, String>> taxaCombustivel) {
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
		
		PdfPCell destino = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("destino"), FONT_8B));
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
		
		List<Map<String,String>> mapList = (List<Map<String, String>>) taxaCombustivel.get(0);
		
		for (Map<String, String> tx : mapList) {
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

	public void getTaxaTerrestreCliente(int total, PdfPTable tableConteudo, List<Map<String,String>> terrestre) {
		if(terrestre == null){
			return;
		}
		
		PdfPTable pdfPTable = new PdfPTable(5);
		PdfPCell cellTitulo = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("taxaCombustivel"), FONT_8B));
		cellTitulo.setHorizontalAlignment(Element.ALIGN_CENTER);
		cellTitulo.setGrayFill(0.75f);
		cellTitulo.setColspan(5);
		pdfPTable.addCell(cellTitulo);
		
		List<Map<String, String>> listMap =  (List<Map<String, String>>) terrestre.get(0);
		
		for (Map<String, String> m : listMap) {
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
			
			PdfPCell c5 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("moeda")+ valor3, FONT_7));
			c5.setHorizontalAlignment(Element.ALIGN_LEFT);
			c5.setBorder(0);
			pdfPTable.addCell(c5);
		}
		
		PdfPCell taxaTerrestre = new PdfPCell(pdfPTable);
		taxaTerrestre.setColspan(total);
		
		tableConteudo.addCell(taxaTerrestre);
	}


	private void getTaxaEntrega(int total, PdfPTable tableConteudo) {
		getTaxaColetaEntregaCliente(total, tableConteudo,false,entrega,coleta);
	}

	private void getTaxaColetaEntregaCliente(int total,	PdfPTable tableConteudo, boolean isColeta,List<Map<String, String>> entrega,List<Map<String, String>> coleta) {
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
		
		List<Map<String, String>> listMap = (List<Map<String, String>>) valoresColetaEntrega.get(0);
			for (Map<String, String> co : listMap) {
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

	private void getTaxaColeta(int total, PdfPTable tableConteudo) {
		getTaxaColetaEntregaCliente(total, tableConteudo,true,entrega,coleta);
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
				addCellRight(tablePrincipal, String.valueOf(map.get("ORIGEM")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal,String.valueOf(map.get("DESTINO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("TAXAMINIMA")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("PESOMINIMO")),1,0.5f,0,0.5f,0);
				addCellRight(tablePrincipal, String.valueOf(map.get("EXCEDENTE")),1,0.5f,0,0.5f,0.5f);
			}
		}

		addCellRight(tablePrincipal, " ",11,0,0,0,0);
		addCellRight(tablePrincipal, " ",11,0,0,0,0);
	}

	private PdfPTable getCabecalhoFretePeso() {
		float[] widthsFretePeso = {1f,1f};
		PdfPTable  cabecalho3 = new PdfPTable(widthsFretePeso);
		addTitle(cabecalho3,      getDefaultResourceBundle().getString("fretePeso"), 2);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("taxaMinima"), 1);
		addCellCenter(cabecalho3, getDefaultResourceBundle().getString("pesoMinimo"), 1);
		return cabecalho3;
	}

	
}

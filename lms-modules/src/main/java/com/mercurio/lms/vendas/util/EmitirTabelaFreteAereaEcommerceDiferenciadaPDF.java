package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTabelaFreteAereaEcommerceDiferenciadaPDF extends TemplatePdf{
	private Logger log = LogManager.getLogger(this.getClass());
	private List<Map<String, String>> listaPrecos;
	private List<Map<String, String>> listaGeneralidades;
	private List<Map<String, String>> listaFormalidades;
	private List<Map<String, String>> servicosAdicionais;
	private List<Map<String, String>> dificuldadeEntrega;
	private List<Map<String, String>> legendas;
	private List<Map<String, String>> cabecalho;
	private List<Map<String, String>> aereo;
	private List<Map<String, String>> taxaCombustivel; 
	private Set taxaCombustivelCabecalho;
	private List<Map<String, String>> coleta;
	private List<Map<String, String>> entrega;
	private List<Map<String, String>> terrestre;
	private List<Map<String, String>> legendaGeneralidades;
	private String valorAbrangencia = "Nacional";
	private String valorModal = "Aéreo";
	
	public EmitirTabelaFreteAereaEcommerceDiferenciadaPDF(List<Map<String, String>> cabecalho, String usuario, List<Map<String, String>> listaPrecos,List<Map<String, String>> listaGeneralidades, List<Map<String, String>> listaFormalidades, List<Map<String, String>> servicosAdicionais, List<Map<String, String>> dificuldadeEntrega, List<Map<String, String>> legendas, List<Map<String, String>> aereo, List<Map<String, String>> taxaCombustivel, Set taxaCombustivelCabecalho, List<Map<String, String>> coleta, List<Map<String, String>> entrega, List<Map<String, String>> legendaGeneralidades,List<Map<String, String>> terrestre) {
		this.listaPrecos = listaPrecos;
		this.listaGeneralidades = listaGeneralidades;
		this.listaFormalidades = listaFormalidades;
		this.servicosAdicionais = servicosAdicionais;
		this.dificuldadeEntrega = dificuldadeEntrega;
		this.legendas = legendas;
		this.cabecalho = cabecalho;
		this.aereo = aereo;
		this.taxaCombustivel = taxaCombustivel;
		this.taxaCombustivelCabecalho = taxaCombustivelCabecalho;
		this.coleta = coleta;
		this.entrega = entrega;
		this.legendaGeneralidades = legendaGeneralidades;
		this.terrestre = terrestre;
		setUsuario(usuario);
		try {
			File file = File.createTempFile("report-listadepreco-aereo-ecormmerce-diferenciada-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,true,this.getClass());
		} catch (IOException e) {
			log.error(e);
		}
			
	}
	
	public void addContent() throws DocumentException  {
		
		int total = cabecalho.size()+5;
		int colunas1e2 = (total)/3;
		int ultima = colunas1e2 + (total)%3;
		
		float[] w = new float[total];
		w[0] = 1f;
		w[1] = 2f;
		w[2] = 2f;
		
		for (int i = 3; i < total; i++) {
			w[i] = 1f;
		}
		  
		
		PdfPTable  tableConteudo = new PdfPTable(w);
		tableConteudo.setWidthPercentage(100f);
		
		String origem = "";
		boolean adicionarSub = true;
		for (Map<String, String> cab : listaPrecos) {		
			if(!origem.equals(cab.get("origem"))){
				if(!"".equals(origem)){
					addSubReports(total, tableConteudo);
					
					adicionarSub = false;
					
				}
				addLinhaBranco(15f, total, tableConteudo);
				
				
				getCabecalho(total, colunas1e2, ultima, tableConteudo,getDefaultResourceBundle().getString("convencional") );				
			
				PdfPCell tarifa = new PdfPCell(new Phrase(cab.get("tarifa"), FONT_7));
				tarifa.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableConteudo.addCell(tarifa);	
				
				PdfPCell origemC = new PdfPCell(new Phrase(cab.get("origem"), FONT_7));
				origemC.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableConteudo.addCell(origemC);
				
				
				PdfPCell destino = new PdfPCell(new Phrase(cab.get("destino"), FONT_7));
				destino.setHorizontalAlignment(Element.ALIGN_LEFT);
				tableConteudo.addCell(destino);
			}
			
			String ultimo = "";
			
			for (Map m : cabecalho) {
				String valor = cab.get(String.valueOf(m.get("valor")));
				ultimo = String.valueOf(m.get("valor"));
				if(valor == null){
					continue;
				}
				PdfPCell label = new PdfPCell(new Phrase(valor, FONT_7));
				label.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tableConteudo.addCell(label);
			}
			
			
			if(cab.get(ultimo) != null){
				PdfPCell freteKg = new PdfPCell(new Phrase(cab.get("fretePorKg"), FONT_7));
				freteKg.setHorizontalAlignment(Element.ALIGN_RIGHT);
				freteKg.setFollowingIndent(100f);
				tableConteudo.addCell(freteKg);		
				
				PdfPCell taxa = new PdfPCell(new Phrase(cab.get("advalorem"), FONT_7));
				taxa.setHorizontalAlignment(Element.ALIGN_RIGHT);
				taxa.setFollowingIndent(100f);
				tableConteudo.addCell(taxa);				
			}
			
			origem = cab.get("origem");
			
		}
		
		if(adicionarSub){
			addSubReports(total, tableConteudo);
		}
		
		document.add(tableConteudo);
		

	}

	private void addSubReports(int total, PdfPTable tableConteudo) {
		addLinhaBranco(15f, total, tableConteudo);
		
		getTaxaColeta(total,tableConteudo);
		getTaxaEntrega(total,tableConteudo);					
		getTaxaTerrestre(total, tableConteudo,terrestre);
		
		montaGeneralidadeFormalidade(total, tableConteudo,listaGeneralidades,listaFormalidades);
		montaServicosAdicionaisDificuldadeEntrega(total, tableConteudo,dificuldadeEntrega,servicosAdicionais);
		getLegendaGeneralidade(total, tableConteudo, legendaGeneralidades);						
		
		getLegenda(total, tableConteudo, legendas, aereo);
		
		addLinhaBranco(15f, total, tableConteudo);
		
		getTaxaCombustivel(total, tableConteudo,taxaCombustivelCabecalho,taxaCombustivel);
	}
	

	private void getTaxaEntrega(int total, PdfPTable tableConteudo) {
		getTaxaColetaEntrega(total, tableConteudo,false,entrega,coleta);
	}

	private void getTaxaColeta(int total, PdfPTable tableConteudo) {
		getTaxaColetaEntrega(total, tableConteudo,true,entrega,coleta);
	}

	
	private void getCabecalho(int total, int colunas1e2, int ultima,PdfPTable tableConteudo,String servico) {
		PdfPCell linha = new PdfPCell(new Phrase(""));
		linha.setColspan(total);
		linha.setFixedHeight(30f);
		linha.setBorder(0);
		tableConteudo.addCell(linha);
		
		PdfPCell modal = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("modal")+valorModal, FONT_7));
		modal.setHorizontalAlignment(Element.ALIGN_CENTER);
		modal.setVerticalAlignment(Element.ALIGN_MIDDLE);
		modal.setGrayFill(0.75f);
		modal.setFollowingIndent(100f);
		modal.setColspan(colunas1e2);
		tableConteudo.addCell(modal);
		
		PdfPCell abrangencia = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("abrangencia")+valorAbrangencia, FONT_7));
		abrangencia.setHorizontalAlignment(Element.ALIGN_CENTER);
		abrangencia.setVerticalAlignment(Element.ALIGN_MIDDLE);
		abrangencia.setGrayFill(0.75f);
		abrangencia.setFollowingIndent(100f);
		abrangencia.setColspan(colunas1e2);
		tableConteudo.addCell(abrangencia);
		
		
		
		PdfPCell sub = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("servico")+servico, FONT_7));
		sub.setHorizontalAlignment(Element.ALIGN_CENTER);
		sub.setVerticalAlignment(Element.ALIGN_MIDDLE);
		sub.setGrayFill(0.75f);
		sub.setFollowingIndent(100f);
		sub.setColspan(ultima);
		tableConteudo.addCell(sub);
		
		
		int cont = 1;
		for (Map<String, String> cab : cabecalho) {
			if(cab.get("valor").contains("P")){
				cont++;
			}
		}
		
		PdfPCell especifica = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("tarifa"), FONT_7));
		especifica.setHorizontalAlignment(Element.ALIGN_CENTER);
		especifica.setVerticalAlignment(Element.ALIGN_MIDDLE);
		especifica.setColspan(1);
		especifica.setGrayFill(0.75f);
		especifica.setFollowingIndent(100f);
		tableConteudo.addCell(especifica);
		
		PdfPCell ori = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("origem"), FONT_7));
		ori.setHorizontalAlignment(Element.ALIGN_CENTER);
		ori.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ori.setGrayFill(0.75f);
		ori.setFollowingIndent(100f);
		ori.setColspan(1);
		tableConteudo.addCell(ori);
		
		PdfPCell fretePeso = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("destino"), FONT_7));
		fretePeso.setHorizontalAlignment(Element.ALIGN_CENTER);
		fretePeso.setVerticalAlignment(Element.ALIGN_MIDDLE);
		fretePeso.setGrayFill(0.75f);
		fretePeso.setFollowingIndent(100f);
		fretePeso.setColspan(1);
		tableConteudo.addCell(fretePeso);
		
		for (Map<String, String> cab : cabecalho) {
			String valor = String.valueOf(cab.get("label"));
			PdfPCell c = new PdfPCell(new Phrase(valor, FONT_7));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			c.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c.setGrayFill(0.75f);
			tableConteudo.addCell(c);
		}
		
		PdfPCell freteKl = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("fretePorKg"), FONT_7));
		freteKl.setHorizontalAlignment(Element.ALIGN_CENTER);
		freteKl.setVerticalAlignment(Element.ALIGN_MIDDLE);
		freteKl.setGrayFill(0.75f);
		freteKl.setFollowingIndent(100f);
		freteKl.setColspan(1);
		tableConteudo.addCell(freteKl);
		
		PdfPCell adValoren = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("valorAdValorem"), FONT_7));
		adValoren.setHorizontalAlignment(Element.ALIGN_CENTER);
		adValoren.setVerticalAlignment(Element.ALIGN_MIDDLE);
		adValoren.setGrayFill(0.75f);
		adValoren.setFollowingIndent(100f);
		adValoren.setColspan(1);
		tableConteudo.addCell(adValoren);
	}

}

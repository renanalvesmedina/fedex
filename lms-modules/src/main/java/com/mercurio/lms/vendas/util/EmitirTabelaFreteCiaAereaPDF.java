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


public class EmitirTabelaFreteCiaAereaPDF extends TemplatePdf{
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
	private String valorAbrangencia = "Nacional";
	private String valorModal = "Aéreo";
	
	public EmitirTabelaFreteCiaAereaPDF(List<Map<String, String>> cabecalho, String usuario, List<Map<String, String>> listaPrecos,List<Map<String, String>> listaGeneralidades, List<Map<String, String>> listaFormalidades, List<Map<String, String>> servicosAdicionais, List<Map<String, String>> dificuldadeEntrega, List<Map<String, String>> legendas, List<Map<String, String>> aereo, List<Map<String, String>> taxaCombustivel, Set taxaCombustivelCabecalho) {
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
		setUsuario(usuario);
		
		
		try {
			File file = File.createTempFile("report-listadepreco-cia-aereo-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,true,this.getClass());
			} catch (IOException e) {
				log.error(e);
			}
		}
			
	public void addContent() throws DocumentException  {
		
		int total = cabecalho.size()+3;
		int colunas1e2 = (cabecalho.size()+1)/3;
		int ultima = cabecalho.size()+1-colunas1e2;
		
		float[] w = new float[total];
		w[0] = 3f;
		w[1] = 1f;
		w[2] = 2f;
		
		for (int i = 3; i < total; i++) {
			w[i] = 1f;
		}
		  
		Paragraph listaTabelaPreco = addTitulo(getDefaultResourceBundle().getString("tituloPrincipal"));
		document.add(listaTabelaPreco);
		
		PdfPTable  tableConteudo = new PdfPTable(w);
		tableConteudo.setWidthPercentage(100f);
		
		String origem = "";
		for (Map<String, String> cab : listaPrecos) {		
			if(!origem.equals(cab.get("origem"))){
				if(!"".equals(origem)){
					getLegenda(total, tableConteudo,legendas,aereo);
					
					PdfPCell branco = new PdfPCell(new Phrase(""));
					branco.setColspan(total);
					branco.setFixedHeight(15f);
					branco.setBorder(0);
					tableConteudo.addCell(branco);
					
					getTaxaCombustivel(total, tableConteudo,taxaCombustivelCabecalho,taxaCombustivel);
					
				}

				origem = cab.get("origem");
				String ciaAerea  = cab.get("ciaAerea");
				String subtipo  = cab.get("subtipo");
				
				getCabecalho(total, colunas1e2, ultima, tableConteudo, ciaAerea, subtipo, origem);
			}
						
			PdfPCell destino = new PdfPCell(new Phrase(cab.get("destino"), FONT_7));
			destino.setHorizontalAlignment(Element.ALIGN_LEFT);
			tableConteudo.addCell(destino);
			
			PdfPCell sigla = new PdfPCell(new Phrase(cab.get("sigla"), FONT_7));
			sigla.setHorizontalAlignment(Element.ALIGN_CENTER);
			tableConteudo.addCell(sigla);
			
			PdfPCell taxa = new PdfPCell(new Phrase(cab.get("taxaMinima"), FONT_7));
			taxa.setHorizontalAlignment(Element.ALIGN_RIGHT);
			taxa.setFollowingIndent(100f);
			tableConteudo.addCell(taxa);
			
			for (Map m : cabecalho) {
				String valor = cab.get(String.valueOf(m.get("valor")));
				if(valor == null){
					valor = getDefaultResourceBundle().getString("valorPadrao");
				}
				PdfPCell label = new PdfPCell(new Phrase(valor, FONT_7));
				label.setHorizontalAlignment(Element.ALIGN_RIGHT);
				tableConteudo.addCell(label);
			}
		}
		
		montaGeneralidadeFormalidade(total, tableConteudo,listaGeneralidades,listaFormalidades);
		
		montaServicosAdicionaisDificuldadeEntrega(total, tableConteudo,dificuldadeEntrega,servicosAdicionais);
		
		document.add(tableConteudo);
		

	}

	private void getCabecalho(int total, int colunas1e2, int ultima,PdfPTable tableConteudo,String ciaAereao, String subtipo, String origem) {
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
		modal.setColspan(2);
		tableConteudo.addCell(modal);
		
		PdfPCell abrangencia = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("abrangencia")+valorAbrangencia, FONT_7));
		abrangencia.setHorizontalAlignment(Element.ALIGN_CENTER);
		abrangencia.setVerticalAlignment(Element.ALIGN_MIDDLE);
		abrangencia.setGrayFill(0.75f);
		abrangencia.setFollowingIndent(100f);
		abrangencia.setColspan(colunas1e2);
		tableConteudo.addCell(abrangencia);
		
		
		PdfPCell cia = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("ciaAerea")+ciaAereao, FONT_7));
		cia.setHorizontalAlignment(Element.ALIGN_CENTER);
		cia.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cia.setColspan(colunas1e2);
		cia.setGrayFill(0.75f);
		cia.setFollowingIndent(100f);
		tableConteudo.addCell(cia);
		
		PdfPCell sub = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("subtipo")+subtipo, FONT_7));
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
		
		PdfPCell ori = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("origem")+origem, FONT_7));
		ori.setHorizontalAlignment(Element.ALIGN_CENTER);
		ori.setVerticalAlignment(Element.ALIGN_MIDDLE);
		ori.setGrayFill(0.75f);
		ori.setFollowingIndent(100f);
		ori.setColspan(2);
		tableConteudo.addCell(ori);
		
		PdfPCell fretePeso = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("fretePeso"), FONT_7));
		fretePeso.setHorizontalAlignment(Element.ALIGN_CENTER);
		fretePeso.setVerticalAlignment(Element.ALIGN_MIDDLE);
		fretePeso.setGrayFill(0.75f);
		fretePeso.setFollowingIndent(100f);
		fretePeso.setColspan(cont);
		tableConteudo.addCell(fretePeso);
		
		PdfPCell especifica = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("tabelaEspecifica"), FONT_7));
		especifica.setHorizontalAlignment(Element.ALIGN_CENTER);
		especifica.setVerticalAlignment(Element.ALIGN_MIDDLE);
		especifica.setColspan(total-2-cont);
		especifica.setGrayFill(0.75f);
		especifica.setFollowingIndent(100f);
		tableConteudo.addCell(especifica);
		
		String dest = getDefaultResourceBundle().getString("destino");
		PdfPCell destinoC = new PdfPCell(new Phrase(dest, FONT_7));
		destinoC.setHorizontalAlignment(Element.ALIGN_CENTER);
		destinoC.setVerticalAlignment(Element.ALIGN_MIDDLE);
		destinoC.setGrayFill(0.75f);
		destinoC.setFollowingIndent(100f);
		tableConteudo.addCell(destinoC);
		
		String sig = getDefaultResourceBundle().getString("sigla");
		PdfPCell siglaC = new PdfPCell(new Phrase(sig, FONT_7));
		siglaC.setHorizontalAlignment(Element.ALIGN_CENTER);
		siglaC.setVerticalAlignment(Element.ALIGN_MIDDLE);
		siglaC.setGrayFill(0.75f);
		
		tableConteudo.addCell(siglaC);
		
		String tarifa = getDefaultResourceBundle().getString("tarifaMinima");
		PdfPCell tarifaC = new PdfPCell(new Phrase(tarifa, FONT_7));
		tarifaC.setHorizontalAlignment(Element.ALIGN_CENTER);
		tarifaC.setVerticalAlignment(Element.ALIGN_MIDDLE);
		tarifaC.setGrayFill(0.75f);
		tableConteudo.addCell(tarifaC);
		
		for (Map<String, String> cab : cabecalho) {
			String valor = String.valueOf(cab.get("label"));
			PdfPCell c = new PdfPCell(new Phrase(valor, FONT_7));
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			c.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c.setGrayFill(0.75f);
			tableConteudo.addCell(c);
		}	
	}
}

package com.mercurio.lms.vendas.util;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EmitirTermoPDF extends TemplatePdf{

	private Logger log = LogManager.getLogger(this.getClass());
	
	public EmitirTermoPDF() {
		try {
			File file = File.createTempFile("report-termo-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			getDocument(file,false,this.getClass(),false);
		} catch (IOException e) {
			log.error(e);
		}
	}
		
	public void addContent() throws DocumentException  {
			float[] f1 = {1f,0.06f,1f};
			PdfPTable  tablePrincipal = new PdfPTable(f1);
			tablePrincipal.setWidthPercentage(100f);
			
			PdfPCell cEspaco= new PdfPCell(new Phrase(""));
			cEspaco.setBorder(0);
			
			PdfPTable  tableEsquerda = new PdfPTable(1);
			PdfPTable  tableDireita = new PdfPTable(1);
			
			PdfPCell branco = new PdfPCell(new Phrase(""));
			branco.setBorder(0);
			branco.setColspan(3);
			branco.setFixedHeight(5f);					
			
			PdfPCell tituloTermosCondicoes = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("tituloTermosCondicoes"), FONT_10B));
			tituloTermosCondicoes.setColspan(3);
			tituloTermosCondicoes.setBorder(0);
			tituloTermosCondicoes.setHorizontalAlignment(Element.ALIGN_CENTER);
			tablePrincipal.addCell(tituloTermosCondicoes);
			
			tablePrincipal.addCell(branco);
			
			PdfPCell cabecalho1 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("cabecalho1"), FONT_8));
			cabecalho1.setColspan(3);
			cabecalho1.setBorder(0);
			cabecalho1.setHorizontalAlignment(Element.ALIGN_CENTER);
			tablePrincipal.addCell(cabecalho1);
			
			tablePrincipal.addCell(branco);

			PdfPCell clausula1 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("clausula1"), FONT_9B));
			clausula1.setBorder(0);
			tableEsquerda.addCell(clausula1);			
			tableEsquerda.addCell(branco);
			
			addParagrafo(tableEsquerda,"clausula1_1","1.1.");
			addParagrafo(tableEsquerda,"clausula1_2","1.2.");
			
			addParagrafo(tableEsquerda,"clausulaDetalhe1_2","");
			addParagrafo(tableEsquerda,"clausula1_3","1.3.");
			addParagrafo(tableEsquerda,"clausula1_4","1.4.");
			
			PdfPCell clausula2 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("clausula2"), FONT_9B));
			clausula2.setBorder(0);	
			clausula2.setLeading(0f, 1.3f);//espaço entre linhas
			tableEsquerda.addCell(clausula2);
			
			tableEsquerda.addCell(branco);
			
			addParagrafo(tableEsquerda,"clausula2_1","2.1.");
			addParagrafo(tableEsquerda,"clausula2_2","2.2.");
			addParagrafo(tableEsquerda,"clausula2_3","2.3.");
			
			addParagrafo(tableDireita,"clausula2_4","2.4.");
			addParagrafo(tableDireita,"clausula2_5","2.5.");
			addParagrafo(tableDireita,"clausula2_6","2.6.");
			
			
			PdfPCell clausula3 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("clausula3"), FONT_9B));
			clausula3.setBorder(0);	
			clausula3.setLeading(0f, 1.3f);//espaço entre linhas
			tableDireita.addCell(clausula3);
			
			tableDireita.addCell(branco);
			
			addParagrafo(tableDireita,"clausula3_1","3.1.");
			addParagrafo(tableDireita,"clausula3_2","3.2.");
			addParagrafo(tableDireita,"clausula3_3","3.3.");
			
			PdfPCell clausula4 = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("clausula4"), FONT_9B));
			clausula4.setBorder(0);	
			clausula4.setLeading(0f, 1.3f);//espaço entre linhas
			tableDireita.addCell(clausula4);
			
			tableDireita.addCell(branco);
			
			addParagrafo(tableDireita,"clausula4_1","4.1.");
			addParagrafo(tableDireita,"clausula4_2","4.2.");
			
			PdfPTable t = new PdfPTable(1);
			t.setHorizontalAlignment(Element.ALIGN_RIGHT);
			t.setWidthPercentage(80f);
			
			PdfPCell b = new PdfPCell(new Phrase(""));
			b.setFixedHeight(10f);
			b.setBorder(0);
			b.setColspan(2);

			PdfPCell m = new PdfPCell(t);
			m.setBorder(0);
			tableDireita.addCell(m);
			
			PdfPCell e = new PdfPCell(tableEsquerda);
			e.setBorder(0);
			tablePrincipal.addCell(e);
			
			tablePrincipal.addCell(cEspaco);
			
			PdfPCell d = new PdfPCell(tableDireita);
			d.setBorder(0);			
			tablePrincipal.addCell(d);

			PdfPCell espaco = new PdfPCell(new Phrase(""));
			espaco.setBorder(0);
			espaco.setColspan(3);
			espaco.setFixedHeight(10f);
			
			tablePrincipal.addCell(espaco);
			
			PdfPCell c = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("rodape1"), FONT_8));
			c.setBorder(0);
			c.setColspan(3);
			tablePrincipal.addCell(c);
			
			PdfPCell contratante = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("contratanteCarimbo"), FONT_10));
			contratante.setBorder(0);
			contratante.setBorderWidthTop(0.5f);
			contratante.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell carimbo = new PdfPCell(new Phrase(getDefaultResourceBundle().getString("mercurioCarimbo"), FONT_10));
			carimbo.setBorder(0);
			carimbo.setBorderWidthTop(0.5f);
			carimbo.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			float[] f = {1f,0.5f,1f};
			PdfPTable carimboBox = new PdfPTable(f);
			carimboBox.setSpacingBefore(45f);
			
			carimboBox.addCell(contratante);
			carimboBox.addCell(cEspaco);
			carimboBox.addCell(carimbo);
			
			PdfPCell cell = new PdfPCell(carimboBox);
			cell.setColspan(3);
			cell.setBorder(0);
			
			tablePrincipal.addCell(cell);
			document.add(tablePrincipal);
	}

	private void addParagrafo(PdfPTable tabela,String chave, String negrito) {
		PdfPTable t = new PdfPTable(1);
		t.setHorizontalAlignment(Element.ALIGN_RIGHT);
		t.setWidthPercentage(80f);
		
		Phrase n = new Phrase("", FONT_8);
		n.add(new Chunk(negrito + " ", FONT_8B));
		n.add(new Chunk(getDefaultResourceBundle().getString(chave), FONT_8));
		
		PdfPCell p = new PdfPCell(n);
		p.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		p.setBorder(0);
		p.setLeading(0f, 1.3f);//espaço entre linhas
		
		PdfPCell branco = new PdfPCell(new Phrase(""));
		branco.setFixedHeight(5f);
		branco.setBorder(0);
		branco.setColspan(2);
		
		t.addCell(p);
		t.addCell(branco);
		PdfPCell m = new PdfPCell(t);
		m.setBorder(0);
		tabela.addCell(m);
	}
}

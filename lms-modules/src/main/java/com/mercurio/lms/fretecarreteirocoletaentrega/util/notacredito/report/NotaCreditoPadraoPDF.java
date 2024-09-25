package com.mercurio.lms.fretecarreteirocoletaentrega.util.notacredito.report;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.helper.NotaCreditoPadraoReportHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe responsável por criar um relatório de nota de crédito de coleta ou
 * entrega.
 * 
 */
public class NotaCreditoPadraoPDF extends TemplateNotaCreditoPadraoPDF {

	private Logger log = LogManager.getLogger(this.getClass());
	private Map<String, String> nota = null;
	private Map<String, String> premio = null;
	private Map<String, Object> contadores = null;
	
	private List<Map<String, Object>> itensEntrega = null;
	private List<Map<String, Object>> itensColeta = null;	
	private List<Map<String, Object>> itensNaoExecutados = null;
		
	/**
	 * Cria objeto do relatório a partir de um conjunto de dados.
	 * 
	 * @param summary
	 */
	public NotaCreditoPadraoPDF(Map<String, Object> summary, Boolean preview){
		super(preview);
				
		init(summary);
	}
		
	/**
	 * Inicializa dados a serem utilizados para construir o report.
	 * 
	 * @param summary
	 */
	@SuppressWarnings("unchecked")
	private void init(Map<String, Object> summary){
		this.nota = MapUtils.getMap(summary, NotaCreditoPadraoReportHelper.NOTA);
		this.premio = MapUtils.getMap(summary, NotaCreditoPadraoReportHelper.PREMIO);				
		this.contadores = MapUtils.getMap(summary, NotaCreditoPadraoReportHelper.CONTADORES);
		
		this.itensEntrega = (List<Map<String, Object>>) MapUtils.getObject(summary, NotaCreditoPadraoReportHelper.ITENS_ENTREGA);
		this.itensColeta = (List<Map<String, Object>>) MapUtils.getObject(summary, NotaCreditoPadraoReportHelper.ITENS_COLETA);		
		this.itensNaoExecutados = (List<Map<String, Object>>) MapUtils.getObject(summary, NotaCreditoPadraoReportHelper.ITENS_NAO_CALCULADOS);
	}
	
	/**
	 * Retorna arquivo do relatório.
	 * 
	 * @return File
	 */
	public File getReport() {				
		try {			
			createDocument(PageSize.A4.rotate());
						
			addPageEvent(this.nota);
			
			openDocument();
			
			addContent();
			
			closeDocument();
		} catch (IOException e) {
			log.error(e);
		} catch (DocumentException e) {
			log.error(e);
		}
		
		return getDocumentFile();
	}

	/**
	 * Adiciona o conteúdo do relatório.
	 *  
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void addContent() throws DocumentException, MalformedURLException,
			IOException {		
		createHeader();
		createContent();		
		createFooter();		
	}
	
	/**
	 * Cria parte inferior.
	 * 
	 * @throws DocumentException
	 */
	private void createFooter() throws DocumentException {		
		getDocument().add(getFooterTable());
	}

	/**
	 * Cria parte superior.
	 * 
	 * @throws DocumentException
	 */
	private void createHeader() throws DocumentException {		
		getDocument().add(getCabecalhoPadraoTable(nota));	
		getDocument().add(getCabecalhoContadoresTable(MapUtils.getString(this.nota, "tipoDescription"), contadores));
	}

	/**
	 * Cria parte central para itens de coleta.
	 * 
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private void addContentColeta()
			throws DocumentException {		
		/*
		 * Adiciona itens de coleta, se houverem.
		 */
		if(itensColeta != null){
			for (Map<String, Object> map : itensColeta) {
				List<Map<String, Object>> listPedidos = (List<Map<String, Object>>) MapUtils.getObject(map, NotaCreditoPadraoReportHelper.LIST_PEDIDOS);
				List<Map<String, Object>> itensCalculo = (List<Map<String, Object>>) MapUtils.getObject(map, NotaCreditoPadraoReportHelper.ITENS_CALCULO);			
								
				getDocument().add(getDetalheColetaTable(MapUtils.getString(map, "nmTabela"), listPedidos, itensCalculo));			
			}
		}
		
		/*
		 * Adiciona lista de itens não executados, se houverem.
		 */
		if(itensNaoExecutados != null && !itensNaoExecutados.isEmpty()){
			getDocument().add(getDetalheColetasNaoExecutadasTable(itensNaoExecutados));		
		}
	}
	
	/**
	 * Cria parte central para itens de entrega.
	 *  
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private void addContentEntrega() throws DocumentException {		
		/*
		 * Adiciona itens de entrega, se houverem.
		 * 
		 */
		if(itensEntrega != null){
			for (Map<String, Object> map : itensEntrega) {
				List<Map<String, Object>> listDoctos = (List<Map<String, Object>>) MapUtils.getObject(map, NotaCreditoPadraoReportHelper.LIST_DOCTOS);
				List<Map<String, Object>> itensCalculo = (List<Map<String, Object>>) MapUtils.getObject(map, NotaCreditoPadraoReportHelper.ITENS_CALCULO);
				
				getDocument().add(getDetalheEntregaTable(MapUtils.getString(map, "nmTabela"), listDoctos, itensCalculo));			
			}
		}
		
		/*
		 * Adiciona lista de itens não executados, se houverem.
		 */
		if(itensNaoExecutados != null && !itensNaoExecutados.isEmpty()){
			getDocument().add(getDetalheEntregasNaoExecutadasTable(itensNaoExecutados));		
		}
	}
	
	/**
	 * Cria parte central.
	 *  
	 * @throws DocumentException
	 */
	private void createContent() throws DocumentException {			
		String tipoNotaCredito = MapUtils.getString(this.nota, "tipo");
		
		if("C".equals(tipoNotaCredito) || "CP".equals(tipoNotaCredito)){
			addContentColeta();
		} else if("E".equals(tipoNotaCredito) || "EP".equals(tipoNotaCredito)){
			addContentEntrega();
		}
		
		/*
		 * Adiciona valores de acréscimo ou desconto.
		 * 
		 */
		if(this.nota.get("vlAcrescimo") != null || this.nota.get("vlDesconto") != null || this.nota.get("vlDescUsoEquipamento") != null){			
			getDocument().add(getAcrescimoDescontoTable(this.nota));
		}
		
		/*
		 * Adiciona valores de prêmio.
		 * 
		 */
		if(premio != null && !premio.isEmpty()){
			getDocument().add(getPremioTable(premio));
		}
		
		/*
		 * Adiciona total da nota. 
		 * 
		 */
		getDocument().add(getTotalTable(this.nota));
		
		/*
		 * Adiciona observação.
		 * 
		 */
		if(this.nota.get("obNotaCredito") != null){
			getDocument().add(getObservacaoTable(this.nota));
		}
	}
}

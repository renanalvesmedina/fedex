package com.mercurio.lms.fretecarreteirocoletaentrega.model.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.NotaCreditoPadraoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe auxiliar para preparar dados para o relatório de nota de crédito
 * padrão.
 * 
 */
public class NotaCreditoPadraoReportHelper {	
	
	public static final String CONTADORES = "contadores";
	public static final String NOTA = "nota";
	public static final String CONTROLE_CARGA = "controleCarga";
	public static final String PREMIO = "premio";
	public static final String ITENS_NAO_CALCULADOS = "itensNaoExecutados";
	
	public static final String ITENS_ENTREGA = "itensEntrega";
	public static final String ITENS_COLETA = "itensColeta";	
	public static final String ITENS_CALCULO = "listCalculo";

	public static final String LIST_PEDIDOS = "listPedidos";
	public static final String LIST_DOCTOS = "listDoctos";
	public static final String LIST_PREMIOS = "listPremios";

	private static final String VL_TOTAL_CALCULO = "vlTotalCalculo";
	
	private NotaCreditoPadraoDAO notaCreditoPadraoDAO;
	
	public NotaCreditoPadraoReportHelper(NotaCreditoPadraoDAO notaCreditoPadraoDAO){
		this.notaCreditoPadraoDAO = notaCreditoPadraoDAO;
	}
	
	/**
	 * Monta conjunto de dados para relatório em PDF, adicionando mais
	 * informações da nota de crédito.
	 * 
	 * @param notaCredito
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getSummaryPDF(Long idNotaCredito, String tpNotaCredito){
		Map<String, Object> summary = getItensNota(idNotaCredito, tpNotaCredito);				
		summary.put(NOTA, getNotaInfo(idNotaCredito));
		
		return summary;
	}
	
	/**
	 * Monta conjunto de dados para relatório resumido em tela.
	 * 
	 * @param notaCredito
	 * 
	 * @return Map<String, Object>
	 */
	public Map<String, Object> getSummaryScreen(Long idNotaCredito, String tpNotaCredito){
		return getItensNota(idNotaCredito, tpNotaCredito);
	}
	
	/**
	 * Retorna os itens da nota de crédito agrupados pela sua tabela de frete
	 * padrão.
	 * 
	 * @param idNotaCredito
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getItensNota(Long idNotaCredito, String tpNotaCredito){		
		Map<String, Integer> contadores = new HashMap<String, Integer>();
		Map<String, String> premios = new HashMap<String, String>();		
		List<Map<String, Object>> itensNaoCalculados = new ArrayList<Map<String, Object>>();		
		
		Map<Long, Map<String, Object>> tabelas = getTabelas(idNotaCredito, premios);
				
		Map<String, Object> summary = populateSummary(idNotaCredito, tpNotaCredito, tabelas, contadores, itensNaoCalculados);		
		summary.put(ITENS_NAO_CALCULADOS, itensNaoCalculados);
		summary.put(CONTADORES, contadores);
		summary.put(PREMIO, premios);
		
		return summary;
	}

	/**
	 * Popula os indicadores e informações do cálculo para cada tabela.
	 * 
	 * @param idNotaCredito
	 * @param tpNotaCredito
	 * @param tabelas
	 * @param contadores
	 * @param itensNaoExecutados
	 */
	private Map<String, Object> populateSummary(Long idNotaCredito, String tpNotaCredito,
			Map<Long, Map<String, Object>> tabelas,
			Map<String, Integer> contadores,
			List<Map<String, Object>> itensNaoExecutados) {		
		Map<String, Object> summary = new HashMap<String, Object>();
		
		if("E".equals(tpNotaCredito) || "EP".equals(tpNotaCredito)){	
			
			addItensDoctoToTabelas(idNotaCredito, tabelas, itensNaoExecutados, contadores,tpNotaCredito);
			
			summary.put(ITENS_ENTREGA, new ArrayList<Map<String, Object>>(tabelas.values()));
		} else if("C".equals(tpNotaCredito) || "CP".equals(tpNotaCredito)){
			addItensPedidoToTabelas(idNotaCredito, tabelas, itensNaoExecutados, contadores);
			
			summary.put(ITENS_COLETA, new ArrayList<Map<String, Object>>(tabelas.values()));
		}
		
		roundingCalc(summary);
		
		
		return summary;
	}
	
	private void roundingCalc(Map<String, Object> summary) {
		roundingMap(summary,ITENS_COLETA);
		roundingMap(summary,ITENS_ENTREGA);
	}

	/**
	 * @param summary
	 * @param keyList 
	 */
	private void roundingMap(Map<String, Object> summary, String keyList) {
		if(summary.containsKey(keyList)){
			for (Map<String, Object> map : (List<Map<String, Object>>)summary.get(keyList)) {			
				BigDecimal total = (BigDecimal) map.get("vlTotalCalculo");
				map.put("vlTotalCalculo", total.setScale(2, RoundingMode.FLOOR));
			}		
		}
	}

	/**
	 * Monta os documentos da nota de crédito de entrega.
	 * 
	 * @param idNotaCredito
	 * @param tabelas
	 * @param itensNaoCalculados
	 * @param contadores
	 * @param tpNotaCredito 
	 */
	private void addItensDoctoToTabelas(Long idNotaCredito,
			Map<Long, Map<String, Object>> tabelas, 
			List<Map<String, Object>> itensNaoCalculados, 
			Map<String, Integer> contadores, String tpNotaCredito) {		
		int calculados = 0;
		int naoCalculados = 0;
		
		List<Object[]> doctos = notaCreditoPadraoDAO.findDoctos(idNotaCredito);
		
		if(doctos == null || doctos.isEmpty()){
			throw new BusinessException("LMS-25126");
		}
		
		if("EP".equals(tpNotaCredito)){
						
			List<Object[]> qtds = notaCreditoPadraoDAO.findSituacaoDocs(idNotaCredito);
			naoCalculados = ((Integer)qtds.get(1)[0]).intValue();
			calculados = ((Integer)qtds.get(2)[0]).intValue();
			
			for (Object[] doctoServico : doctos) {
				Map<String, Object> docto = getItemDocto(doctoServico);
				
				String blCalculado = MapUtils.getString(docto, "blCalculado");
				
				if(isCalculado(blCalculado)){
					Long idTabelaFcValores = MapUtils.getLong(docto, "idTabelaFcValores");
					addItemToTabela(tabelas.get(idTabelaFcValores), LIST_DOCTOS, docto);
				} else {
					itensNaoCalculados.add(docto);
				}
			}
			
		}else{		
			for (Object[] doctoServico : doctos) {
				Map<String, Object> docto = getItemDocto(doctoServico);
				
				String blCalculado = MapUtils.getString(docto, "blCalculado");
				
				if(isCalculado(blCalculado)){
					calculados++;
					Long idTabelaFcValores = MapUtils.getLong(docto, "idTabelaFcValores");
					
					addItemToTabela(tabelas.get(idTabelaFcValores), LIST_DOCTOS, docto);
				} else {
					naoCalculados++;
					itensNaoCalculados.add(docto);
				}
			}
			
		}
		
		
		updateCounter(contadores, calculados, naoCalculados);
	}
	
	/**
	 * Monta os pedidos da nota de crédito executados.
	 * @param tpNotaCredito 
	 * 
	 * @param notaCredito
	 * @param itens
	 */
	private void addItensPedidoToTabelas(Long idNotaCredito, 
			Map<Long, Map<String, Object>> tabelas, 
			List<Map<String, Object>> itensNaoCalculados, 
			Map<String, Integer> contadores) {		
		int calculados = 0;
		int naoExecutadas = 0;
				
		List<Object[]> coletas = notaCreditoPadraoDAO.findColetas(idNotaCredito);
		
		if(coletas == null || coletas.isEmpty()){
			throw new BusinessException("LMS-25126");
		}
		
		for (Object[] coleta : coletas) {
			Map<String, Object> pedido = getItemPedido(coleta);
			
			String blCalculado = MapUtils.getString(pedido, "blCalculado");
			
			if(isCalculado(blCalculado)){
				calculados++;
				Long idTabelaFcValores = MapUtils.getLong(pedido, "idTabelaFcValores");
				
				addItemToTabela(tabelas.get(idTabelaFcValores), LIST_PEDIDOS, pedido);
			} else {
				naoExecutadas++;
				itensNaoCalculados.add(pedido);
			}
		}					
		
		updateCounter(contadores, calculados, naoExecutadas);
	}

	/**
	 * Atualiza contadores de documentos/pedidos.
	 * 
	 * @param contadores
	 * @param executadas
	 * @param naoExecutadas
	 */
	private void updateCounter(Map<String, Integer> contadores, int executadas, int naoExecutadas) {
		contadores.put("executadas", executadas);
		contadores.put("naoExecutadas", naoExecutadas);
		contadores.put("programadas", executadas + naoExecutadas);
	}
	
	/**
	 * Verifica se item foi calculado.
	 * 
	 * @param notaCreditoCalcPadraoDocto
	 * @return boolean
	 */
	private boolean isCalculado(String blCalculado) {
		return blCalculado != null && "S".equals(blCalculado);
	}
	
	/**
	 * Monta os cálculos para cada tabela da nota de crédito.
	 * 
	 * @param notaCredito
	 * @param tabelas
	 */
	private Map<Long, Map<String, Object>> getTabelas(Long idNotaCredito, Map<String, String> premios) {
		Map<Long, Map<String, Object>> tabelas = new HashMap<Long, Map<String,Object>>();
		
		List<Object[]> calculoTabelas = notaCreditoPadraoDAO.findTabelas(idNotaCredito);
		
		if(calculoTabelas == null || calculoTabelas.isEmpty()){
			throw new BusinessException("LMS-25126");
		}
		
		for (Object[] calculoTabela : calculoTabelas) {
			Map<String, Object> tabela = getTabelaCalculo(tabelas, premios, calculoTabela);			
			Map<String, Object> notaCreditoCalculo = getItemCalculo(calculoTabela);					
						
			addItemToTabela(tabela, ITENS_CALCULO, notaCreditoCalculo);			
			addSumToTabela(tabela, VL_TOTAL_CALCULO, notaCreditoCalculo, "vlTotal");
		}
		
		return tabelas;
	}
	
	/**
	 * Retorna uma tabela de calculo, caso não exista a adiciona no map.
	 * 
	 * @param tabelas
	 * @param calculoTabela
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getTabelaCalculo(Map<Long, Map<String, Object>> tabelas, Map<String, String> premios, Object[] calculoTabela){
		Map<String, Object> tabela = getTabela(calculoTabela, premios);
		Long idTabela = MapUtils.getLong(tabela, "idTabela");
		
		if(!tabelas.containsKey(idTabela)){
			tabelas.put(idTabela, tabela);
		} else {
			return tabelas.get(idTabela);
		}
		
		return tabela;		
	}
	
	/**
	 * Adiciona um item a lista de itens da nota de crédito.
	 * 
	 * @param tabela
	 * @param listKey
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	private void addItemToTabela(Map<String, Object> tabela, String listKey, Map<String, Object> item){
            if(tabela != null){
		List<Map<String, Object>> itens = (List<Map<String, Object>>) tabela.get(listKey);		
		itens.add(item);
            }
	}
	
	/**
	 * Adiciona somatório a uma tabela.
	 * 
	 * @param tabela
	 * @param vlTotalKey
	 * @param item
	 * @param vlKey
	 */
	private void addSumToTabela(Map<String, Object> tabela, String vlTotalKey, Map<String, Object> item, String vlKey){
		BigDecimal total = (BigDecimal) tabela.get(vlTotalKey);
		total = total.add((BigDecimal) item.get(vlKey));
		
		tabela.put(vlTotalKey, total);
	}
	
	/**
	 * Retorna um documento de entrega.
	 * 
	 * @param doctoServico
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getItemDocto(Object[] doctoServico) {
		Map<String, Object> docto = new HashMap<String, Object>();
				
		docto.put("documento", doctoServico[1] + " " + getNrFormatado((Long) doctoServico[2], false));
		docto.put("qtVolumes", doctoServico[3]);
		docto.put("vlMercadoria", getValorFormatado((BigDecimal) doctoServico[4]));		
		docto.put("psControleCarga", FormatUtils.formatPeso((Number) doctoServico[5], true));
		docto.put("vlLiquidoControleCarga",  getValorFormatado((BigDecimal) doctoServico[6]));
		docto.put("vlBrutoControleCarga",  getValorFormatado((BigDecimal) doctoServico[7]));				
		docto.put("enderecoEntrega",  doctoServico[8]);
		docto.put("dsOcorrenciaEntrega",  doctoServico[9]);
		docto.put("tpModalServico", doctoServico[10]);	
		docto.put("nmPessoaRemetente", doctoServico[11]);
		docto.put("nmPessoaDestinario", doctoServico[12]);
		docto.put("blCalculado", doctoServico[13]);
		docto.put("psAferido", FormatUtils.formatPeso((Number)doctoServico[14],true));
		docto.put("psReal",  FormatUtils.formatPeso((Number)doctoServico[15],true));
		docto.put("psReferenciaCalculo",  FormatUtils.formatPeso((Number)doctoServico[16],true));
		docto.put("idTabelaFcValores", doctoServico[0]);
		docto.put("cteFedex", doctoServico[17]);
		docto.put("notasFiscais", arrayToString(doctoServico[18]));
		docto.put("volumeCalculado", doctoServico[19]);
		docto.put("pesoCalculado", FormatUtils.formatPeso((Number)doctoServico[20],true));
		docto.put("valorMercadoriaCalculado", getValorFormatado((BigDecimal) doctoServico[21]));
		return docto;
	}
	
	private String arrayToString(Object rawArray) {
		if (rawArray == null) {
			return null;
		}
		String[] arrayString = (String[]) rawArray;
		return StringUtils.join(arrayString, ", ");
	}

	/**
	 * Retorna um item do pedido de coleta.
	 * 
	 * @param doctoServico
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getItemPedido(Object[] coleta) {
		Map<String, Object> pedido = new HashMap<String, Object>();
		
		pedido.put("pedidoColeta", coleta[1] + " " + getNrFormatado((Long) coleta[2], false));		
		pedido.put("psTotalInformado", FormatUtils.formatPeso((Number) coleta[3], true));
		pedido.put("vlTotalInformado", getValorFormatado((BigDecimal) coleta[4]));
		pedido.put("qtTotalVolumesInformado", coleta[5]);
		pedido.put("manifestoColeta",coleta[6] + " " + getNrFormatado((Long) coleta[7], false));						
		pedido.put("enderecoColeta", getEnderecoPessoaFormatado(coleta));		
		pedido.put("blCalculado", coleta[15]);
		pedido.put("idTabelaFcValores", coleta[0]);
				
		return pedido;
	}
	
	/**
	 * Retorna endereço da pessoa formatado de acordo com o padrão do sistema.
	 * 
	 * @param enderecoPessoa
	 * @return String
	 */
	private String getEnderecoPessoaFormatado(Object[] coleta) {		
		return FormatUtils.formatEnderecoPessoa((String) coleta[8],
				(String) coleta[9], (String) coleta[10], (String) coleta[11],
				(String) coleta[12], (String) coleta[13], (String) coleta[14]);
	}
	
	/**
	 * @param notaCreditoCalcPadrao
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getItemCalculo(Object[] calculo) {
		Map<String, Object> notaCreditoCalculoDTO = new HashMap<String, Object>();
		
		BigDecimal qtTotal = (BigDecimal) calculo[1];
		BigDecimal vlValor = (BigDecimal) calculo[2];
		BigDecimal vlTotal = qtTotal.multiply(vlValor);
		
		notaCreditoCalculoDTO.put("qtTotal", getValorFormatado(qtTotal));
		notaCreditoCalculoDTO.put("vlValor",  getValorFormatado(vlValor));
		notaCreditoCalculoDTO.put("vlTotal", vlTotal);
		
		notaCreditoCalculoDTO.put("tpValorDescription", calculo[3]);
		notaCreditoCalculoDTO.put("vlTotalFormatado", getValorFormatado(vlTotal));
		
		return notaCreditoCalculoDTO;
	}
	
	/**
	 * Cria um grupo de itens para nota de crédito de acordo com a tabela de
	 * frete padrão.
	 * 
	 * @param tabelaFcValores
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getTabela(Object[] tabela, Map<String, String> premios) {
		Map<String, Object> notaCreditoItens = new HashMap<String, Object>();
				
		/* Informação da tabela */
		notaCreditoItens.put("idTabela", tabela[4]);
		notaCreditoItens.put("nrTabela", getNrFormatado((Long) tabela[6], true));
		notaCreditoItens.put("blTipo", tabela[10]);
		notaCreditoItens.put("nmTabela", getNomeTabela(tabela));
	
		/* Lista de itens da tabela */
		notaCreditoItens.put(LIST_PREMIOS, getPremios(premios, tabela));
		notaCreditoItens.put(ITENS_CALCULO, new ArrayList<Object>());
		notaCreditoItens.put(LIST_DOCTOS, new ArrayList<Object>());
		notaCreditoItens.put(LIST_PEDIDOS, new ArrayList<Object>());		
		
		/* Somatório da tabela */
		notaCreditoItens.put(VL_TOTAL_CALCULO, new BigDecimal(0));
		
		notaCreditoItens.put("blAtivo", true);
		notaCreditoItens.put("blDoctoAtivo", true);
		notaCreditoItens.put("blPedidoAtivo", true);
		notaCreditoItens.put("blCalculoAtivo", true);
		
		return notaCreditoItens;
	}	
	
	/**
	 * Recupera valores de prêmio para uma tabela de frete padrão. <br>
	 * 
	 * <b>Por padrão apenas uma tabela de frete deve possuir valores do premio
	 * definidos.<b/>
	 * 
	 * @param notaCredito
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, String> getPremios(Map<String, String> premios, Object[] tabela){	
		addPremio(premios, "pcPremioCte", (BigDecimal) tabela[28]);
		addPremio(premios, "pcPremioEvento", (BigDecimal) tabela[29]);
		addPremio(premios, "pcPremioDiaria", (BigDecimal) tabela[30]);
		addPremio(premios, "pcPremioVolume", (BigDecimal) tabela[31]);
		addPremio(premios, "pcPremioSaida", (BigDecimal) tabela[32]);
		addPremio(premios, "pcPremioFreteBruto", (BigDecimal) tabela[33]);
		addPremio(premios, "pcPremioFreteLiq", (BigDecimal) tabela[34]);
		addPremio(premios, "pcPremioMercadoria", (BigDecimal)tabela[35]);
		
		return premios;
	}
	
	private void addPremio(Map<String, String> values, String key, BigDecimal value){		
		if(value != null && value.compareTo(BigDecimal.ZERO) > 0){
			values.put(key, getValorFormatado(value));
		}
	}
		
	/**
	 * Monta nome da tabela.
	 * 
	 * @param tabelaFcValores
	 * @return String
	 */
	private String getNomeTabela(Object[] tabela){
		StringBuilder nmTabela = new StringBuilder();
		nmTabela.append(tabela[8]);
		nmTabela.append(" ");
		nmTabela.append(getNrFormatado((Long) tabela[6], true));
		nmTabela.append(" - ");
		nmTabela.append(tabela[27]);
		nmTabela.append(" - ");
		nmTabela.append(tabela[26]);		
		nmTabela.append(getNomeTabelaEspecifica(tabela));
		
		return nmTabela.toString();
	}

	/**
	 * Monta nome da tabela específica.
	 * 
	 * @param tabelaFcValores
	 * @param nmTabela
	 */
	private String getNomeTabelaEspecifica(Object[] tabela) {
		if("GE".equals(tabela[12])){
			return new String();
		}
		
		StringBuilder nmTabelaEsp = new StringBuilder();
				
		if(tabela[15] != null){
			StringBuilder rota = new StringBuilder(); 
			rota.append(tabela[21]);
			rota.append(" - ");
			rota.append(tabela[22]);	
			
			addNmTabela(rota, nmTabelaEsp);
		}
		
		if(tabela[12] != null){
			StringBuilder meioTransporte = new StringBuilder(); 
			meioTransporte.append(tabela[17]);
			meioTransporte.append("/");
			meioTransporte.append(tabela[18]);	
			
			addNmTabela(meioTransporte, nmTabelaEsp);
		}
		
		if(tabela[14] != null){				
			addNmTabela(tabela[24], nmTabelaEsp);
		}
		
		if(tabela[11] != null){
			addNmTabela(tabela[25], nmTabelaEsp);
		}
		
		if(tabela[13] != null){
			addNmTabela(tabela[19], nmTabelaEsp);
		}
		
		if(tabela[16] != null){
			addNmTabela(tabela[23], nmTabelaEsp);
		}
				
		return nmTabelaEsp.toString();
	}
	
	/**
	 * @param value
	 * @param nmTabelaEsp
	 */
	private void addNmTabela(Object value, StringBuilder nmTabelaEsp) {
		if(nmTabelaEsp.length() != 0){
			nmTabelaEsp.append(" > ");
		} else {
			nmTabelaEsp.append(": ");
		}
		
		nmTabelaEsp.append(value);
	}
	
	/**
	 * Dados gerais da nota de crédito.
	 * 
	 * @param idNotaCredito
	 * 
	 * @return Map<String, String>
	 */
	private Map<String, String> getNotaInfo(Long idNotaCredito){
		Map<String, String> info = new HashMap<String, String>();
						
		List<Object[]> cabecalhos = notaCreditoPadraoDAO.findCabecalho(idNotaCredito);
		
		if(cabecalhos != null && !cabecalhos.isEmpty()){			
			Object[] cabecalho = cabecalhos.get(0);
			
			/* Informações do controle de carga */
			info.put("controleCarga", cabecalho[0] + " " + getNrFormatado((Long) cabecalho[1], false));		
			info.put("frotaPlaca", cabecalho[2] + "/" + cabecalho[3]);		
			info.put("tipoTransporte", (String) cabecalho[4]);
			info.put("modeloTransporte", (String) cabecalho[5]);		
			info.put("motorista", (String) cabecalho[6]);
			info.put("rota", cabecalho[7] + " - " + cabecalho[8]);
			info.put("kmRota", String.valueOf(cabecalho[9]));		
			info.put("dhGeracao", JTDateTimeUtils.formatDateTimeToString((DateTime) cabecalho[10]));			
			
			/* Informações da nota de crédito */
			info.put("nota", cabecalho[11] + " " + getNrFormatado((Long) cabecalho[12], true));
			info.put("tipo", (String) cabecalho[13]);
			info.put("tipoDescription", (String) cabecalho[14]);			
			info.put("dhEmissao", JTDateTimeUtils.formatDateTimeToString((DateTime) cabecalho[15]));
			info.put("dhGeracao", JTDateTimeUtils.formatDateTimeToString((DateTime) cabecalho[16]));
			info.put("vlTotal", getValorFormatado((BigDecimal) cabecalho[17]));		
			info.put("vlAcrescimo", cabecalho[18] != null ? getValorFormatado((BigDecimal) cabecalho[18]) : null);
			info.put("vlDesconto", cabecalho[19] != null ? getValorFormatado((BigDecimal) cabecalho[19]) : null);
			info.put("vlDescUsoEquipamento", getValorFormatado((BigDecimal) cabecalho[20]));
			info.put("obNotaCredito", (String) cabecalho[21]);
		}
		
		return info;
	}
		
	/**
	 * Formata valor de acordo com máscara de numeração.
	 * 
	 * @param nr
	 * @param custom
	 * 
	 * @return String
	 */
	private String getNrFormatado(Long nr, boolean custom) {
		if (nr == null) {
			return null;
		}
		
		if(!custom){
			return FormatUtils.formatLongWithZeros(nr);
		} else {
			return FormatUtils.formatLongWithZeros(nr, "0000000000");	
		}
	}
	
	/**
	 * Formata valor de acordo com máscara de moeda.
	 * 
	 * @param valor
	 * 
	 * @return String
	 */
	private String getValorFormatado(BigDecimal valor) {
		if (valor == null) {
			return FormatUtils.formatDecimal("###,###,##0.00", BigDecimal.ZERO);
		}
		if(BigDecimal.ONE.compareTo(valor) <= 0){
			return FormatUtils.formatDecimal("###,###,##0.00", valor);		
		}
		return FormatUtils.formatDecimal("###,###,##0.0000", valor);
	}
}
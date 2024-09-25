package com.mercurio.lms.fretecarreteirocoletaentrega.reports;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class NotaCreditoCalculoDoisReportRunner extends ExcelRemoteReportsRunner {

	private static final String REPORT_NAME = "notaCreditoCalculoDois";
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private Map<String, Object> dadosNotaCredito;
	private Map<String, Object> dadosTotalParcelas;
	private List<Map<String, Object>> listTabelaFrete;
	private Map<String, List<Map<String, Object>>> mapTabelaManifestoColeta;
	private Map<String, List<Map<String, Object>>> mapTabelaManifestoEntrega;
	
	public NotaCreditoCalculoDoisReportRunner(String reportHostUrl) {
		super(reportHostUrl);
	}	
	
	@Override
	protected String getReportName() {
		return REPORT_NAME;
	}

	/**
	 * Realiza a geração do xls a partir do modelo passado em xlsModel.
	 * Retorna o workbook pronto para ser baixado pelo usuário. Be aware, dragons ahead.
	 */
	@Override
	protected HSSFWorkbook generateWorkbook(InputStream xlsModel) throws IOException {
		workbook = new HSSFWorkbook(xlsModel);
		sheet = workbook.getSheetAt(0);
		populateDadosNotaCredito();
		populateTabelaDeFrete();
		populateTabelaManifestoColeta();
		populateTabelaManifestoEntrega();
		populateTotalParcelas();
		ExcelReportUtils.clearReport(sheet);
		sheet.getRow(1).getCell((short)4).setAsActiveCell();		
		return workbook;
	}

	private void populateDadosNotaCredito(){
		//Controle de Carga
		ExcelReportUtils.setStringVar(sheet, "nrNotaCredito", dadosNotaCredito.get("NR_NOTA_CREDITO").toString());
		ExcelReportUtils.setStringVar(sheet, "nrControleCarga", dadosNotaCredito.get("CONTROLE_CARGA").toString());
		ExcelReportUtils.setStringVar(sheet, "dtGeracaoControleCarga", dadosNotaCredito.get("DH_GERACAO").toString());
		ExcelReportUtils.setStringVar(sheet, "nrFrota", dadosNotaCredito.get("NR_FROTA").toString());
		ExcelReportUtils.setStringVar(sheet, "nrPlaca", dadosNotaCredito.get("NR_IDENTIFICADOR").toString());
		ExcelReportUtils.setStringVar(sheet, "dsTipoMeioTransporte", dadosNotaCredito.get("DS_TIPO_MEIO_TRANSPORTE").toString());
		ExcelReportUtils.setStringVar(sheet, "dsModeloMeioTransporte", dadosNotaCredito.get("DS_MODELO_MEIO_TRANSPORTE").toString());
		ExcelReportUtils.setStringVar(sheet, "dsMarcaMeioTransporte", dadosNotaCredito.get("DS_MARCA_MEIO_TRANSPORTE").toString());
		ExcelReportUtils.setStringVar(sheet, "dsRota", dadosNotaCredito.get("DS_ROTA").toString());
		ExcelReportUtils.setNumericVar(sheet, "qtKmRota", Double.valueOf(dadosNotaCredito.get("NR_KM").toString()));
		ExcelReportUtils.setStringVar(sheet, "nmMotorista", dadosNotaCredito.get("NM_MOTORISTA").toString());
		//Informações
		ExcelReportUtils.setStringVar(sheet, "dsTipoTabela", dadosNotaCredito.get("TP_REGISTRO_TABELA").toString());
		ExcelReportUtils.setNumericVar(sheet, "qtDias", dadosNotaCredito.get("QT_DIAS") == null ? 0 : Double.valueOf(dadosNotaCredito.get("QT_DIAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "qtKmRodado", Double.valueOf(dadosNotaCredito.get("KM_RODADOS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "COLETASPROGRAMADAS", Double.valueOf(dadosNotaCredito.get("COLETASPROGRAMADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "ENTREGASPROGRAMADAS", Double.valueOf(dadosNotaCredito.get("ENTREGASPROGRAMADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "COLETASEXECUTADAS", Double.valueOf(dadosNotaCredito.get("COLETASEXECUTADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "ENTREGASEXECUTADAS", Double.valueOf(dadosNotaCredito.get("ENTREGASEXECUTADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "COLETASCONTABILIZADAS", Double.valueOf(dadosNotaCredito.get("COLETASCONTABILIZADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "ENTREGASCONTABILIZADAS", Double.valueOf(dadosNotaCredito.get("ENTREGASCONTABILIZADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "qtColetas", Double.valueOf(dadosNotaCredito.get("QT_COLETAS_EXECUTADAS").toString()));
		ExcelReportUtils.setNumericVar(sheet, "qtEntregaColeta", Double.valueOf(dadosNotaCredito.get("PROGRAMADO").toString()));
		ExcelReportUtils.setNumericVar(sheet, "qtEntregas", Double.valueOf(dadosNotaCredito.get("QT_ENTREGAS_REALIZADAS").toString()));
		//Acrescimo Desconto
		ExcelReportUtils.setNumericVar(sheet, "vlAcrescimo", Double.valueOf(dadosNotaCredito.get("vlAcrescimo").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlDesconto", Double.valueOf(dadosNotaCredito.get("vlDesconto").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlDescontoCarretaTNT", Double.valueOf(dadosNotaCredito.get("vlDescUsoEquipamento").toString()));
		//Valor total
		ExcelReportUtils.setNumericVar(sheet, "vlTotalNotaCredito", Double.valueOf(dadosNotaCredito.get("VL_TOTAL_NOTA_CREDITO").toString()));
		//Observações - caso não exista outras notas de crédito na observação, apaga as linhas da tabela
		ExcelReportUtils.setStringVar(sheet, "dsObsFormatada", dadosNotaCredito.get("dsObsFormatada").toString());
	}
	
	private void populateTabelaDeFrete(){
		//Caso exista mais de uma tabela, copia tabela para utilização posterior.
		if (listTabelaFrete.size() > 1){
			ExcelReportUtils.copyTable(sheet, "Tabela de Frete", listTabelaFrete.size() - 1);
		}
		for (Map tabelaFrete : listTabelaFrete){
			ExcelReportUtils.setStringVar(sheet, "nmCliente", tabelaFrete.get("dsCliente") == null ? "Cliente não especificado" : tabelaFrete.get("dsCliente").toString());
			ExcelReportUtils.setStringVar(sheet, "tpCalculo", tabelaFrete.get("tpCalculo").toString());
			ExcelReportUtils.replaceStringVar(sheet, "qtPeso", tabelaFrete.get("qtFracaoPeso").toString());
			ExcelReportUtils.setDateVar(sheet, "dtVigenciaInicial", tabelaFrete.get("dtVigenciaInicial").toString());
			if (tabelaFrete.get("dtVigenciaFinal") != null){
				ExcelReportUtils.setDateVar(sheet, "dtVigenciaFinal", tabelaFrete.get("dtVigenciaFinal").toString());
			}
			else{
				ExcelReportUtils.setStringVar(sheet, "dtVigenciaFinal", "Indeterminado");
			}
			ExcelReportUtils.setNumericVar(sheet, "vlDiaria", Double.valueOf(tabelaFrete.get("vlDiaria").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlPernoite", Double.valueOf(tabelaFrete.get("pePernoite").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlEvento", Double.valueOf(tabelaFrete.get("vlEvento").toString()));	
			ExcelReportUtils.setNumericVar(sheet, "vlKm", Double.valueOf(tabelaFrete.get("vlKm").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlVolume", Double.valueOf(tabelaFrete.get("vlVolume").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlPeSobreFrete", Double.valueOf(tabelaFrete.get("vlPeSobreFrete").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlMinimoPeSobreFrete", Double.valueOf(tabelaFrete.get("vlMinimoPeSobreFrete").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlPeSobreMerc", Double.valueOf(tabelaFrete.get("vlPeSobreMerc").toString()));
			ExcelReportUtils.setNumericVar(sheet, "vlMinimoPeSobreMerc", Double.valueOf(tabelaFrete.get("vlMinimoPeSobreMerc").toString()));
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> parcelasCe = (List<Map<String, Object>>) tabelaFrete.get("listParcelaCe");
			if (parcelasCe.isEmpty()){
				
			}
			else if (parcelasCe.size() == 1){
				HSSFRow row = ExcelReportUtils.findRow(sheet, "${tpFatorFretePeso}", HSSFCell.CELL_TYPE_STRING);
				ExcelReportUtils.removeRows(sheet, row.getRowNum(), row.getRowNum());
			}
			else if (parcelasCe.size() > 2){
				HSSFRow row = ExcelReportUtils.findRow(sheet, "${tpFatorFretePeso}", HSSFCell.CELL_TYPE_STRING);
				for (int i = 0; i < parcelasCe.size() - 2; i++){
					ExcelReportUtils.duplicateRow(sheet, row.getRowNum(), row.getRowNum() + 1 + i);
				}
			}
			for (Map parcelaCe : parcelasCe){
				ExcelReportUtils.setStringVar(sheet, "tpFatorFretePeso", parcelaCe.get("tpFatorFretePeso").toString());
				ExcelReportUtils.setStringVar(sheet, "dsFaixaFretePeso", parcelaCe.get("dsFaixaFretePeso").toString());
				ExcelReportUtils.setNumericVar(sheet, "vlFretePeso", Double.valueOf(parcelaCe.get("vlFretePeso").toString()));			
			}
		}
	}
	
	private void populateTabelaManifestoEntrega() {	
		//Caso exista mais de uma tabela, copia tabela para utilização posterior.
		if (mapTabelaManifestoEntrega.size() > 1){
			ExcelReportUtils.copyTable(sheet, "${manifestoEntrega}", mapTabelaManifestoEntrega.size() - 1);
		}
		List<Map<String, Object>> listEntrega;
		for (String nrManifestoEntrega: mapTabelaManifestoEntrega.keySet()){
			ExcelReportUtils.setStringVar(sheet, "nrManifestoEntrega", nrManifestoEntrega);
			listEntrega = mapTabelaManifestoEntrega.get(nrManifestoEntrega);
			HSSFRow row = ExcelReportUtils.findRow(sheet, "${sgFilialDoctoServico}", HSSFCell.CELL_TYPE_STRING);
			//Se não encontrar primeira linha da tabela, encerra.
			if (row == null)
				return;
			//Define título da tabela, para manifesto de entrega normal ou parceira.
			if ("EP".equals(listEntrega.get(0).get("tpManifestoEntrega").toString())){
				ExcelReportUtils.setStringVar(sheet, "manifestoEntrega", "Manifesto de Entrega Parceira");
			}
			else{
				ExcelReportUtils.setStringVar(sheet, "manifestoEntrega", "Manifesto de Entrega");
			}	
			//Caso manifesto não tenha docto servico (deveria estar fora da nota), então apaga linhas de docto servico
			if (listEntrega.size() == 1){
				if (Boolean.valueOf(listEntrega.get(0).get("isManifestoForaNota").toString())){	
					ExcelReportUtils.removeRows(sheet, row.getRowNum() - 2, row.getRowNum() + 1);
					return;
				}
			}
			//Caso exista mais de um docto servico (entrega), copia linhas de detalhe
			if (listEntrega.size() > 1){
				for (int i = 1; i < listEntrega.size(); i++){
					ExcelReportUtils.duplicateMultipleRows(sheet, row.getRowNum(), row.getRowNum() + 1, row.getRowNum() + (2 * i));
				}
			}			
			for (Map doctoServico : listEntrega){		
				ExcelReportUtils.setStringVar(sheet, "sgFilialDoctoServico", doctoServico.get("sgFilial").toString());
				ExcelReportUtils.setNumericVar(sheet, "nrDoctoServico", Double.valueOf(doctoServico.get("nrDoctoServico").toString()));
				ExcelReportUtils.setStringVar(sheet, "enderecoReal", doctoServico.get("enderecoReal").toString());
				ExcelReportUtils.setNumericVar(sheet, "vlPesoEntrega", Double.valueOf(doctoServico.get("vlPeso").toString()));
				ExcelReportUtils.setNumericVar(sheet, "qtEventoEntrega", Double.valueOf(doctoServico.get("qtEvento").toString()));
				ExcelReportUtils.setNumericVar(sheet, "qtVolumeEntrega", Double.valueOf(doctoServico.get("qtVolume").toString()));
				ExcelReportUtils.setNumericVar(sheet, "vlMercadoriaEntrega", Double.valueOf(doctoServico.get("vlMercadoria").toString()));
				ExcelReportUtils.setNumericVar(sheet, "vlFreteEntrega", Double.valueOf(doctoServico.get("vlFrete").toString()));
			}
		}
	}

	private void populateTabelaManifestoColeta() {
		//Caso exista mais de uma tabela, copia tabela para utilização posterior.
		if (mapTabelaManifestoColeta.size() > 1){
			ExcelReportUtils.copyTable(sheet, "Manifesto de Coleta", mapTabelaManifestoColeta.size() - 1);
		}
		List<Map<String, Object>> listPedidoColeta;
		for (String nrManifestoColeta : mapTabelaManifestoColeta.keySet()){
			ExcelReportUtils.setStringVar(sheet, "nrManifestoColeta", nrManifestoColeta);
			listPedidoColeta = mapTabelaManifestoColeta.get(nrManifestoColeta);
			HSSFRow row = ExcelReportUtils.findRow(sheet, "${sgFilialPedidoColeta}", HSSFCell.CELL_TYPE_STRING);
			//Se não encontrar primeira linha da tabela, encerra.
			if (row == null)
				return;
			//Caso manifesto não tenha pedido de coleta (deveria estar fora da nota), então apaga linhas de pedido de coleta
			if (listPedidoColeta.size() == 1){
				if (Boolean.valueOf(listPedidoColeta.get(0).get("isManifestoForaNota").toString())){			
					ExcelReportUtils.removeRows(sheet, row.getRowNum() - 2, row.getRowNum());
					return;
				}
			}
			//Caso exista mais de um pedido de coleta, copia linhas de detalhe
			if (listPedidoColeta.size() > 1){
				for (int i = 0; i < listPedidoColeta.size() - 1; i++){
					ExcelReportUtils.duplicateRow(sheet, row.getRowNum(), row.getRowNum() + 1 + i);
				}
			}			
			for (Map pedidoColeta : listPedidoColeta){		
				ExcelReportUtils.setStringVar(sheet, "sgFilialPedidoColeta", pedidoColeta.get("sgFilial").toString());
				ExcelReportUtils.setNumericVar(sheet, "nrPedidoColeta", Double.valueOf(pedidoColeta.get("nrPedidoColeta").toString()));
				ExcelReportUtils.setStringVar(sheet, "dsEnderecoPedidoColeta", pedidoColeta.get("dsEndereco").toString());
				ExcelReportUtils.setNumericVar(sheet, "vlPesoPedidoColeta", Double.valueOf(pedidoColeta.get("vlPeso").toString()));
				ExcelReportUtils.setNumericVar(sheet, "qtEventoPedidoColeta", Double.valueOf(pedidoColeta.get("qtEvento").toString()));
				ExcelReportUtils.setNumericVar(sheet, "qtVolumePedidoColeta", Double.valueOf(pedidoColeta.get("qtVolume").toString()));		
			}
		}
	}
	
	private void populateTotalParcelas(){
		ExcelReportUtils.setNumericVar(sheet, "vlTotalDiaria", Double.valueOf(dadosTotalParcelas.get("vlTotalDiaria").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlTotalKm", Double.valueOf(dadosTotalParcelas.get("vlTotalKm").toString()));	
		ExcelReportUtils.setNumericVar(sheet, "vlTotalEvento", Double.valueOf(dadosTotalParcelas.get("vlTotalEvento").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlTotalPeSobreFrete", Double.valueOf(dadosTotalParcelas.get("vlTotalPeSobreFrete").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlTotalPeSobreMerc", Double.valueOf(dadosTotalParcelas.get("vlTotalPeSobreValorMercadoria").toString()));
		ExcelReportUtils.setNumericVar(sheet, "vlTotalVolume", Double.valueOf(dadosTotalParcelas.get("vlTotalVolume").toString()));
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> parcelasCe = (List<Map<String, Object>>) dadosTotalParcelas.get("listParcelaCe");
		Double vlTotalFretePeso = 0.0;
		if (parcelasCe.size() > 1){
			HSSFRow row = ExcelReportUtils.findRow(sheet, "${tpFatorFretePesoParcela}", HSSFCell.CELL_TYPE_STRING);
			for (int i = 0; i < parcelasCe.size() - 1; i++){
				ExcelReportUtils.duplicateRow(sheet, row.getRowNum(), row.getRowNum() + 1 + i);
			}
		}
		for (Map parcelaCe : parcelasCe){
			ExcelReportUtils.setStringVar(sheet, "tpFatorFretePesoParcela", parcelaCe.get("tpFatorFretePeso").toString() + " (" + parcelaCe.get("dsFaixaFretePeso").toString() + ")");
			ExcelReportUtils.setNumericVar(sheet, "vlFretePesoParcela", Double.valueOf(parcelaCe.get("vlFretePeso").toString()));
			vlTotalFretePeso += Double.valueOf(parcelaCe.get("vlFretePeso").toString());
		}
		ExcelReportUtils.setNumericVar(sheet, "vlTotalFretePeso", vlTotalFretePeso);
	}

	public List<Map<String, Object>> getListTabelaFrete() {
		return listTabelaFrete;
	}

	public void setListTabelaFrete(List<Map<String, Object>> listTabelaFrete) {
		this.listTabelaFrete = listTabelaFrete;
	}

	public Map<String, Object> getDadosNotaCredito() {
		return dadosNotaCredito;
	}

	public void setDadosNotaCredito(Map<String, Object> dadosNotaCredito) {
		this.dadosNotaCredito = dadosNotaCredito;
	}	

	public Map<String, Object> getDadosTotalParcelas() {
		return dadosTotalParcelas;
	}

	public void setDadosTotalParcelas(Map<String, Object> dadosTotalParcelas) {
		this.dadosTotalParcelas = dadosTotalParcelas;
	}

	public Map<String, List<Map<String, Object>>> getMapTabelaManifestoColeta() {
		return mapTabelaManifestoColeta;
	}

	public void setMapTabelaManifestoColeta(
			Map<String, List<Map<String, Object>>> mapTabelaManifestoColeta) {
		this.mapTabelaManifestoColeta = mapTabelaManifestoColeta;
	}

	public Map<String, List<Map<String, Object>>> getMapTabelaManifestoEntrega() {
		return mapTabelaManifestoEntrega;
	}

	public void setMapTabelaManifestoEntrega(
			Map<String, List<Map<String, Object>>> mapTabelaManifestoEntrega) {
		this.mapTabelaManifestoEntrega = mapTabelaManifestoEntrega;
	}
}

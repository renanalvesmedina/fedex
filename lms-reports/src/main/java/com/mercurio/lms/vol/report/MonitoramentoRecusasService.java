package com.mercurio.lms.vol.report;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.service.VolRecusasService;

public class MonitoramentoRecusasService extends ReportServiceSupport { 
	
	private static final int TOLERANCIA = 15; 
	private VolRecusasService volRecusasService;
	
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		List listRecusas = volRecusasService.findRecusa((TypedFlatMap)parameters);
		List list = new ArrayList();
		
		for(Iterator it = listRecusas.iterator(); it.hasNext();){
			Map params = (Map)it.next();
			Map param = new HashMap(); 
			param.put("TP_DOC", getDomainValue("tpDocumentoServico", params));
			param.put("FILIAL", getValue("sgFilial", params));
			param.put("CTRC", getValue("nrConhecimento", params));
			param.put("FROTA", getValue("frota", params));
			param.put("PLACA", getValue("nrIdentificador",params));
			param.put("OTD", getDateValue("dpe", params));
			param.put("EQUIPAMENTO", getValue("numero", params));
			param.put("RESOLUCAO",  getValue("dhResolucao", params));
			param.put("BAIXA", getDateTimeValue("dhOcorrencia", params));
			param.put("DIAS", qtdDiasMap(params));
			param.put("REMETENTE", getValue("remetente", params));
			param.put("DESTINATARIO", getValue("destinatario", params));
			param.put("DT_MOTIVO", getDateTimeValue("dhRecusa", params));
			param.put("MOTIVO", getValue("dsOcorrenciaEntrega",params));
			param.put("OP_CLIENTE", getDomainValue("opcaoCliente", params));
			list.add(param);
		}
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
    	return createReportDataObject(dataSource, reportParams(parameters));
	}
	
	private String getValue(String key, Map params){
		return params.get(key) == null ? "" : params.get(key).toString(); 
	}
	
	private String getDateTimeValue(String key, Map params){
		return params.get(key) == null ? "" : DateFormatUtils.format(((DateTime)params.get(key)).toDate(), "dd/MM/yyyy");
	}
	
	private String getDateValue(String key, Map params) throws ParseException{
		Date dt = DateUtils.parseDate(params.get(key).toString(), new String[]{"yyyy-MM-dd"});
		return DateFormatUtils.format(dt, "dd/MM/yyyy");
	}
	
	
	private String getDomainValue(String key, Map params){
		return ((DomainValue)params.get(key)).getDescriptionAsString();
	}
	
	private String qtdDiasMap(Map params){
		return params.get("dhResolucao") != null ? "-" : faltamDias((DateTime)params.get("dhOcorrencia"));
	}
	
	private Map<String, String> reportParams(Map params) throws ParseException {
		Map<String, String> parametersReport = new HashMap<String, String>();
		parametersReport.put("FILTRO_FROTA", getValue("FILTRO_FROTA", params));
		parametersReport.put("FILTRO_PERIODO", getDateValue("dataInicial", params)+ " a " + getDateValue("dataFinal", params));
		parametersReport.put("FILTRO_STATUS", getValue("FILTRO_STATUS", params));
		parametersReport.put("FILTRO_REMETENTE", getValue("pessoa.nmPessoa", params));
		parametersReport.put("FILTRO_FILIAL", getValue("FILTRO_FILIAL", params));
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		return parametersReport;
	}
	
	public void setVolRecusasService(VolRecusasService volRecusasService) {
		this.volRecusasService = volRecusasService;
	}
	
	private String faltamDias (DateTime data){
		int faltam = 0; 
		DateTime dataAtual = JTDateTimeUtils.getDataHoraAtual();
		int difDias = JTDateTimeUtils.getIntervalInDays(data.toYearMonthDay(),dataAtual.toYearMonthDay());	
		faltam = TOLERANCIA - difDias;
		return String.valueOf(faltam);
	} 
}

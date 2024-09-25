package com.mercurio.lms.coleta.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaResumidoDTO;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.report.dto.RelatorioEficienciaColetaResumido;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração dos Relatórios de Eficiência de Coleta Resumido
 * Especificação técnica 02.03.02.13
 * 
 * @spring.bean id="lms.coleta.emitirRelatorioEficienciaColetaResumidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/eficienciaResumidoColeta.jasper"
 */
public class EmitirRelatorioEficienciaColetaResumidoService extends ReportServiceSupport {
	
	private PedidoColetaService pedidoColetaService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		YearMonthDay dataInicial = (YearMonthDay) parameters.get("dataInicial");
    	YearMonthDay dataFinal = (YearMonthDay) parameters.get("dataFinal");
    	Long idFilial = MapUtils.getLong(parameters, "idFilial");
    	String modoPedidoColeta =  MapUtils.getString(parameters, "modoPedidoColeta");
    	Long idRegional = MapUtils.getLong(parameters, "idRegional");
    	Long idCliente = MapUtils.getLong(parameters, "idCliente");
    	Long idRotaColetaEntrega = MapUtils.getLong(parameters, "idRotaColetaEntrega");
    	String efc = MapUtils.getString(parameters, "efc");
    	
    	List<RelatorioEficienciaColetaResumidoDTO> data = pedidoColetaService.listRelatorioEficienciaColetaResumido(dataInicial, dataFinal, idFilial, modoPedidoColeta, idRegional, idCliente, idRotaColetaEntrega, efc);
    	
    	List<RelatorioEficienciaColetaResumido> list = new ArrayList<RelatorioEficienciaColetaResumido>();
    	
    	for (RelatorioEficienciaColetaResumidoDTO dto : data) {
			list.add(parse(dto, SessionUtils.getUsuarioLogado().getLocale()));
		}
    	
    	JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
	    	
    	return createReportDataObject(dataSource, reportParams(parameters));
	}
	
	private RelatorioEficienciaColetaResumido parse(RelatorioEficienciaColetaResumidoDTO dto, Locale locale) {
		return new RelatorioEficienciaColetaResumido(dto.getIdFilial(),
				dto.getNomeFilial(),
				dto.getQtdTotal(),
				dto.getQtdEficiente(),
				dto.getQtdIneficiente(),
				dto.getQtdNeutra());
	}

	private Map<String, String> reportParams(Map param) {
		Map<String, String> parametersReport = new HashMap<String, String>();

        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa", filterSummary(param));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
		return parametersReport;
	}
    
	private String filterSummary(Map param) {
		StringBuilder builder = new StringBuilder();
		builder.append("periodo inicial: ")
				.append(JTFormatUtils.format((YearMonthDay) param.get("dataInicial")))
				.append(" | periodo final: ")
				.append(JTFormatUtils.format((YearMonthDay) param.get("dataFinal")))
				.append(" | filial: ")
				.append(param.get("idFilial") == null ? "" : param.get("idFilial"))
				.append(" | modo pedido coleta: ")
				.append(param.get("modoPedidoColeta") == null ? "" : param.get("modoPedidoColeta"))
				.append(" | regional: ")
				.append(param.get("idRegional") == null ? "" : param.get("idRegional"))
				.append(" | cliente: ")
				.append(param.get("idCliente") == null ? "" : param.get("idCliente"))
				.append(" | rota: ")
				.append(param.get("idRotaColetaEntrega") == null ? "" : param.get("idRotaColetaEntrega"))
				.append(" | efc: ")
				.append(param.get("efc") == null ? "" : param.get("efc"));
		return builder.toString();
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}  

}

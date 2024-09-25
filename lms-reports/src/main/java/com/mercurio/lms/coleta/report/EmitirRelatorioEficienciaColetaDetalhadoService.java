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
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaDetalhadoDTO;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.report.dto.RelatorioEficienciaColetaDetalhado;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração dos Relatórios de Eficiência de Coleta Detalhado
 * Especificação técnica 02.03.02.13
 * 
 * @spring.bean id="lms.coleta.emitirRelatorioEficienciaColetaDetalhadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/eficienciaDetalhadoColeta.jasper"
 */
public class EmitirRelatorioEficienciaColetaDetalhadoService extends ReportServiceSupport {
	
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
    	
    	List<RelatorioEficienciaColetaDetalhadoDTO> data = pedidoColetaService.listRelatorioEficienciaColetaDetalhado(dataInicial, dataFinal, idFilial, modoPedidoColeta, idRegional, idCliente, idRotaColetaEntrega, efc);
    	
    	List<RelatorioEficienciaColetaDetalhado> list = new ArrayList<RelatorioEficienciaColetaDetalhado>();
    	
    	for (RelatorioEficienciaColetaDetalhadoDTO dto : data) {
			list.add(parse(dto, SessionUtils.getUsuarioLogado().getLocale()));
		}
    	
    	JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
	    	
    	return createReportDataObject(dataSource, reportParams(parameters));
	}
	
	private RelatorioEficienciaColetaDetalhado parse(RelatorioEficienciaColetaDetalhadoDTO dto, Locale locale) {
		return new RelatorioEficienciaColetaDetalhado(dto.getIdCliente(),
				dto.getTipoIdentificacao(),
				dto.getNomeCliente(),
				dto.getIdFilial(),
				dto.getNomeFilial(),
				dto.getNumeroColeta(),
				dto.getNumeroRota(),
				dto.getModoPedidoColeta() != null ? dto.getModoPedidoColeta().getValue(locale) : "",
				dto.getNomeUsuario(),
				dto.getQuantidadeDocumentos(),
				dto.getDataSolicitacao(),
				dto.getDataPrevista(),
				dto.getDataBaixa(),
				dto.getUsuarioBaixa(),
				dto.getEfc(),
				dto.getCodigoUltimaOcorrencia(),
				dto.getDescricaoUltimaOcorrencia() != null ? dto.getDescricaoUltimaOcorrencia().getValue(locale) : "",
				dto.getUsuarioUltimaOcorrencia(),
				dto.getDataUltimaOcorrencia(),
				dto.getCodigoOcorrenciaTNT(),
				dto.getDescricaoOcorrenciaTNT() != null ? dto.getDescricaoOcorrenciaTNT().getValue(locale) : "",
				dto.getUsuarioOcorrenciaTNT(),
				dto.getDataOcorrenciaTNT());
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

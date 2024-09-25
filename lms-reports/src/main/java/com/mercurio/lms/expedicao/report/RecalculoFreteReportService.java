package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.edi.dto.RelatorioErrosRecalculoFreteDTO;
import com.mercurio.lms.expedicao.model.RecalculoFrete;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.recalculoFreteReportService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirRelacaoCargaAerea.jasper"
 */
public class RecalculoFreteReportService extends ReportServiceSupport {
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		final List<RelatorioErrosRecalculoFreteDTO> erros = (List<RelatorioErrosRecalculoFreteDTO>) parameters.get("erros");
		
		RecalculoFrete recalculoFrete = (RecalculoFrete) parameters.get("recalculoFrete");
		
		JRReportDataObject jr = new JRReportDataObject() {
			
			Map parameters = new HashMap();
			
			public void setParameters(Map arg0) {
				parameters = arg0;
			}
			
			public Map getParameters() {
				return parameters;
			}
			
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(erros);
			}
		};;;
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("nrProcesso", recalculoFrete.getNrProcesso());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		jr.setParameters(parametersReport);
		
		return jr;
	}

}

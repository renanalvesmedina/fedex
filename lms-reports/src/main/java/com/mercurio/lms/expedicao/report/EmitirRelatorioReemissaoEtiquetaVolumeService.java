package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolumeDTO;
import com.mercurio.lms.expedicao.model.service.ReemissaoEtiquetaVolumeService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * @spring.bean id="lms.expedicao.reemissaoEtiquetaVolumeDTO"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/expedicao/report/emitirRelatorioReemissaoEtiquetaVolume.jasper"
 */
public class EmitirRelatorioReemissaoEtiquetaVolumeService extends
		ReportServiceSupport {
	private ReemissaoEtiquetaVolumeService reemissaoEtiquetaVolumeService;

	@Override
	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, parameters);
		final List<ReemissaoEtiquetaVolumeDTO> registros = this.reemissaoEtiquetaVolumeService.find(tfm);
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(registros);
			}

			public Map getParameters() {
				return parameters;
			}

			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};
		;
		;
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		return jr;
	}

	public void setReemissaoEtiquetaVolumeService(ReemissaoEtiquetaVolumeService reemissaoEtiquetaVolumeService) {
		this.reemissaoEtiquetaVolumeService = reemissaoEtiquetaVolumeService;
	}
}

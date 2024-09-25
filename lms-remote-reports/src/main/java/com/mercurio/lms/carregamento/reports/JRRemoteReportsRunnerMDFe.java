package com.mercurio.lms.carregamento.reports;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;

/**
 * 
 * @author RafaelKF
 *
 */
public class JRRemoteReportsRunnerMDFe extends JRRemoteReportsRunner {

	public JRRemoteReportsRunnerMDFe(Locale locale, String reportHostUrl) {
		super(locale, reportHostUrl);
	}

	
	public File executeReportMDFe(List<byte[]> dsXmls, List<Long> protocolos, List<String> observacoes, String obsContingMdfe1, String obsContingMdfe2, Map<String,List<Map<String,String>>> listChaveCte) throws Exception {
		ClassPathResource jasperResource = new ClassPathResource("com/mercurio/lms/carregamento/reports/impressaoMDFe.jasper");

		return generateReportFile(MDFeJasperReportFiller.fillXmlJasperReport(dsXmls, protocolos, observacoes, this.getLocale(), jasperResource.getInputStream(),
				this.getReportHostUrl(), obsContingMdfe1, obsContingMdfe2, listChaveCte));
	}
}

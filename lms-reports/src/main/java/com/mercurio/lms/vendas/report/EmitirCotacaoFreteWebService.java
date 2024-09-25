package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.util.VendasUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.vendas.emitirCotacaoFreteWebService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirCotacaoFreteWeb.jasper"
 */
public class EmitirCotacaoFreteWebService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		Cotacao cotacao = VendasUtils.getCotacaoInSession();
		cotacao.setNmClienteRemetente(cotacao.getClienteByIdClienteSolicitou().getPessoa().getNmPessoa());
		cotacao.setNmClienteDestino(cotacao.getClienteByIdClienteDestino().getPessoa().getNmPessoa());

		List data = new ArrayList();
		data.add(cotacao);
		JRDataSource datasource = new JRBeanCollectionDataSource(data);

		Municipio municipioOrigem = cotacao.getMunicipioByIdMunicipioOrigem();

		Map parametersReport = new HashMap();
		parametersReport.put("dataEmissao", municipioOrigem.getNmMunicipio() + ", " + JTFormatUtils.format(JTDateTimeUtils.getDataAtual(), JTFormatUtils.FULL));

		return createReportDataObject(datasource, parametersReport);
	}

	public JRDataSource executeDimensoes() throws Exception {
		return new JRBeanCollectionDataSource(VendasUtils.getCotacaoInSession().getDimensoes());
	}

	public JRDataSource executeParcelas() throws Exception {
		return new JRBeanCollectionDataSource(VendasUtils.getCotacaoInSession().getParcelaCotacoes());
	}
}
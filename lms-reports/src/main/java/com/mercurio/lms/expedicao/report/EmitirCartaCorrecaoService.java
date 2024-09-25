package com.mercurio.lms.expedicao.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.emitirCartaCorrecaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirCartaCorrecao.jasper"
 */
public class EmitirCartaCorrecaoService extends ReportServiceSupport {
	
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap params = (TypedFlatMap)parameters;
		String nrPermisso = FormatUtils.formatDecimal("#000", params.getLong("ctoInternacional.nrPermisso"));
		String nrCrt = FormatUtils.formatDecimal("000000", params.getLong("ctoInternacional.nrCrt"));
		String nrCrtFormatado = params.getString("ctoInternacional.sgPais") + "." 
			+ nrPermisso + "." + nrCrt;
		params.put("nrCrtFormatado", nrCrtFormatado);
		Map parametersReport = new HashMap();
		List data = new ArrayList();
		data.add(params);
		JRMapCollectionDataSource datasource = new JRMapCollectionDataSource(data);

		
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		Empresa empresa = SessionUtils.getEmpresaSessao();
		EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(empresa.getIdEmpresa());

		EnderecoPessoa epf = enderecoPessoaService.findByIdPessoa(SessionUtils.getFilialSessao().getIdFilial());
		String nmMunicipio = epf.getMunicipio().getNmMunicipio();

		String localData =  nmMunicipio + ", " + 
			JTFormatUtils.format(JTDateTimeUtils.getDataAtual(), JTFormatUtils.LONG, JTFormatUtils.YEARMONTHDAY);
		parametersReport.put("localData", localData);
		parametersReport.put("nmEmpresa", ep.getPessoa().getNmPessoa());
		parametersReport.put("homePage", empresa.getDsHomePage());
		StringBuffer enderecoCompleto = new StringBuffer(this.enderecoPessoaService.getEnderecoCompleto(ep.getIdEnderecoPessoa()));
		if(StringUtils.isNotBlank(ep.getNrCep()))
			enderecoCompleto.append(" " + configuracoesFacade.getMensagem("cep") + " " + ep.getNrCep());
		enderecoCompleto.append(" - " + nmMunicipio + " - " + ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		TelefoneEndereco te = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(ep.getIdEnderecoPessoa(), "FO", "C");
		if(te != null) {
			enderecoCompleto.append(" - " + configuracoesFacade.getMensagem("telefone") + " " 
					+ FormatUtils.formatTelefone(te.getNrTelefone(), te.getNrDdd(), te.getNrDdi()));
		}
		te = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(ep.getIdEnderecoPessoa(), "FA", "C");
		if(te != null) {
			enderecoCompleto.append(" - " + configuracoesFacade.getMensagem("fax") + " " 
					+ FormatUtils.formatTelefone(te.getNrTelefone(), te.getNrDdd(), te.getNrDdi()));
		}
		parametersReport.put("enderecoCompleto", enderecoCompleto.toString());
		return createReportDataObject(datasource, parametersReport);
	}

	public JRReportDataObject executeNew(Map parameters) throws Exception {
		TypedFlatMap params = (TypedFlatMap)parameters;
		return null;
}
}

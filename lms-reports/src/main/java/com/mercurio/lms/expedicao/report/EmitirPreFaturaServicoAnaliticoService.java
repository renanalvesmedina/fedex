package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoItemService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

public class EmitirPreFaturaServicoAnaliticoService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private PreFaturaServicoItemService preFaturaServicoItemService;
	private TelefoneEnderecoService telefoneEnderecoService;

	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		Long idPreFatura = (Long)parameters.get("idPreFaturaServico");
		String tpRelatorio = (String)parameters.get("tpRelatorio");
		final List<Map<String, Object>> itens = preFaturaServicoItemService.findReportEmissaoPreFaturaItem(idPreFatura, tpRelatorio);
		
		JRReportDataObject jr = new JRReportDataObject() {			
			Map parameters = new HashMap();
			@Override
			public void setParameters(Map arg0) {
				parameters = arg0;
			}
			@Override
			public Map getParameters() {
				return parameters;
			}
			@Override
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(itens);
			}
		};

		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		reportParameters.put("filialEmissora", (String)parameters.get("sgFilialCobranca"));
		reportParameters.put("numero", FormatUtils.formataNrDocumento(((Long)parameters.get("nrPreFatura")).toString(), "PFS"));
		reportParameters.put("emissao", JTDateTimeUtils.formatDateTimeToString((DateTime)parameters.get("dhGeracao")));
		String tomador = (String)parameters.get("nrIdentificacao") + " " + (String)parameters.get("nmPessoa");
		reportParameters.put("clienteTomador", tomador);
		reportParameters.put("tpRelatorio", (String)parameters.get("tpRelatorio"));
		
		Filial filial = filialService.findById(Long.valueOf(parameters.get("idFilialCobranca").toString()));
		Pessoa pessoa = filial.getPessoa();
		reportParameters.put("razaoSocial", pessoa.getNmPessoa());
		reportParameters.put("NR_CNPJ", pessoa.getNrIdentificacao());
		
		for (InscricaoEstadual inscricaoEstadual : pessoa.getInscricaoEstaduais()) {
			if (inscricaoEstadual.getBlIndicadorPadrao()) {
				reportParameters.put("NR_INSCRICAO_ESTADUAL", inscricaoEstadual.getNrInscricaoEstadual());
				break;
			}
		}
		
		EnderecoPessoa enderecoPessoa =pessoa.getEnderecoPessoa();
		String endereco = enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().toString()
				+ " " + enderecoPessoa.getDsEndereco();
		if (enderecoPessoa.getNrEndereco() != null) {
			endereco += " - " + enderecoPessoa.getNrEndereco();
		}
		if (enderecoPessoa.getDsBairro() != null) {
			endereco += " - " + enderecoPessoa.getDsBairro();
		}
		
		reportParameters.put("enderecoCompletoMatriz", endereco);
		Municipio municipio = enderecoPessoa.getMunicipio();
		String enderecoMunicipio = municipio.getNmMunicipio()
				+ " - " + municipio.getUnidadeFederativa().getSgUnidadeFederativa()
				+ " - " + FormatUtils.formatCep(municipio.getUnidadeFederativa()
						.getPais().getSgPais(), enderecoPessoa.getNrCep());
		reportParameters.put("enderecoMunicipio", enderecoMunicipio);
	
		String site = configuracoesFacade.getMensagem("website");
		reportParameters.put("site", site);
		String telefone = getTelefoneEndereco(enderecoPessoa, "FO");
		if (telefone == null) {
			telefone = getTelefoneEndereco(enderecoPessoa, "FF");
		}
		reportParameters.put("telefone", telefone);
		jr.setParameters(reportParameters);
		
		return jr;
	}
	
	private String getTelefoneEndereco(EnderecoPessoa endereco, String tpUso) {
		if(endereco != null) {
			TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(endereco.getIdEnderecoPessoa(), tpUso, ConstantesConfiguracoes.TP_TELEFONE_COMERCIAL);
			if(telefoneEndereco != null) {
				return FormatUtils.formatTelefone(telefoneEndereco.getNrTelefone(), telefoneEndereco.getNrDdd(), "");
			}
		}
		return null;
	}
	
	public void setPreFaturaServicoItemService(
			PreFaturaServicoItemService preFaturaServicoItemService) {
		this.preFaturaServicoItemService = preFaturaServicoItemService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
}
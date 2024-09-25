package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.calendariotnt.CalendarioTntDMN;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoDMN;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoWrapperDMN;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;

public class DemonstrativoGerentesService extends ReportServiceSupport {

	private ExecutivoTerritorioService executivoTerritorioService;
	private EdwService edwService;
	
	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = (TypedFlatMap) criteria;

		Integer mesCalculo = map.getInteger("mesCalculo");
		Integer anoCalculo = map.getInteger("anoCalculo");
		Long cargoCpf = map.getLong("cargoCpf");

		ComissaoWrapperDMN totaisCaixas = edwService.findTotalDemonstrativoFechamentoByCpf(cargoCpf, mesCalculo, anoCalculo, null, "GERENTE_VENDAS,GERENTE_REGIONAL_VENDAS,GERENTE_FILIAL,GERENTE_DE_CONTA_ESTRATEGICA");
		ComissaoDMN caixasDados = totaisCaixas.getListComissaoDMN().iterator().next();
		String totalDemonstrativoComissao = caixasDados.getSomaComissaoValorBase() == null ? "" : caixasDados.getSomaComissaoValorBase().toString();
		
		final List<DemonstrativoComissoesReportVisualization> lista = new ArrayList<DemonstrativoComissoesReportVisualization>();
		lista.add(new DemonstrativoComissoesReportVisualization());
		
		JRReportDataObject jr = createReportDataObject(lista);

		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		ComissaoWrapperDMN dadosDemonstrativoPagamento = edwService.findDemonstrativoFechamentoByCpf(cargoCpf, mesCalculo, anoCalculo, null, "GERENTE_VENDAS,GERENTE_REGIONAL_VENDAS,GERENTE_FILIAL,GERENTE_DE_CONTA_ESTRATEGICA");
		List<ComissaoDMN> dadosDemonstrativoPagamentoList = dadosDemonstrativoPagamento.getListComissaoDMN();

        if (!dadosDemonstrativoPagamentoList.isEmpty()) {
            parametersReport.put("nomeGerente", dadosDemonstrativoPagamentoList.iterator().next().getCargoNome());
            parametersReport.put("matricula", dadosDemonstrativoPagamentoList.iterator().next().getNumeroMatricula().toString());
        }
        
        parametersReport.put("competenciaDemonstrativo", toCompetencia(anoCalculo + "-" + mesCalculo + "-01"));
        parametersReport.put("filial", executivoTerritorioService.getLocalizacaoFromExecutivo(cargoCpf.toString()));
        
        parametersReport.put("rangeDatas", getRangeFromCalendarioTnt(mesCalculo, anoCalculo));
        
        parametersReport.put("totalDemonstrativoComissao", formatCurrency(totalDemonstrativoComissao));
        parametersReport.put("totalComissaoTriEquipe", "");
        parametersReport.put("mediaComissaoEquipe", "");
        parametersReport.put("valorGerente", "");
        parametersReport.put("mensagemTipoGerente", "");
        parametersReport.put("diferenca", "");
        parametersReport.put("crossSelling", "");
        
        parametersReport.put("somaComissoes", formatCurrency(totalDemonstrativoComissao));
        
        String comissao = caixasDados.getSomaComissaoValorCalculado().toString();
        
        parametersReport.put("comissao", formatCurrency(comissao));
        parametersReport.put("totalComissoes", formatCurrency(comissao));
        
		jr.setParameters(parametersReport);
		return jr;
	}

	private JRReportDataObject createReportDataObject(final List<DemonstrativoComissoesReportVisualization> lista) {
		return new JRReportDataObject() {
			Map parameters = new HashMap();

			@Override
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(lista);
			}

			@Override
			public Map getParameters() {
				return parameters;
			}

			@Override
			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};
	}

	private String toModal(String modal) {
		if (modal == null) {
			return null;
		}
		
		if (modal.equals("R")) {
			return "Rodoviario";
		}
		
		if (modal.equals("A")) {
			return "Aéreo";
		}

		return null;
	}

	private String toCompetencia(String emissao) {
		String[] split = emissao.split("-");
		String mes = toMonth(split[1]);
		return mes + "/" + split[0];
	}

	private String toMonth(String mes) {
		if ("01".equals(mes) || "1".equals(mes)) {
			return "Jan";
		}
		if ("02".equals(mes) || "2".equals(mes)) {
			return "Fev";
		}
		if ("03".equals(mes) || "3".equals(mes)) {
			return "Mar";
		}
		if ("04".equals(mes)  || "4".equals(mes)) {
			return "Abr";
		}
		if ("05".equals(mes) || "5".equals(mes)) {
			return "Mai";
		}
		if ("06".equals(mes) || "6".equals(mes)) {
			return "Jun";
		}
		if ("07".equals(mes) || "7".equals(mes)) {
			return "Jul";
		}
		if ("08".equals(mes) || "8".equals(mes)) {
			return "Ago";
		}
		if ("09".equals(mes) || "9".equals(mes)) {
			return "Set";
		}
		if ("10".equals(mes)) {
			return "Out";
		}
		if ("11".equals(mes)) {
			return "Nov";
		}
		if ("12".equals(mes)) {
			return "Dez";
		}
		return null;
	}


	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}
	
	private String getRangeFromCalendarioTnt(Integer mesCalculo, Integer anoCalculo) {
		CalendarioTntDMN calendarioTnt = edwService.findCalendarioTNTByData(new YearMonthDay(anoCalculo, mesCalculo, 15));
		
		String dataInicioMes = toBrazilianDate(calendarioTnt.getDataInicioMes());
		String dataFimMes = toBrazilianDate(calendarioTnt.getDataFimMes());
		
		return dataInicioMes + " - " + dataFimMes;
	}

	
	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}
	
	private String toBrazilianDate(String s) {
		String[] split = s.split("-");
		return split[2].trim() + "/" + split[1] + "/" + split[0];
	}

	private String formatCurrency(String rawNumber) {
		if (rawNumber == null || rawNumber.isEmpty()) {
			return "";
		}
		return NumberFormat.getCurrencyInstance().format(new BigDecimal(rawNumber)).substring(3);
	}

}

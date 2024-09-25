package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoDMN;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoWrapperDMN;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;

public class DemonstrativoExecutivoInternoService extends ReportServiceSupport {

	private EdwService edwService;
	private ExecutivoTerritorioService executivoTerritorioService;
	
	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = (TypedFlatMap) criteria;

		Integer mesCalculo = map.getInteger("mesCalculo");
		Integer anoCalculo = map.getInteger("anoCalculo");
		Long cargoCpf = map.getLong("cargoCpf");

		final List<DemonstrativoExecutivoInternoVisualization> lista = new ArrayList<DemonstrativoExecutivoInternoVisualization>();
		
		ComissaoWrapperDMN manutencaoList = edwService.findDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "MANUTENCAO", "Executivo Interno");
		addManutencao(lista, manutencaoList.getListComissaoDMN());
		
		ComissaoWrapperDMN servicoAdicionalList = edwService.findDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "SERVICO_ADICIONAL", "Executivo Interno");
		addServicoAdicional(lista, servicoAdicionalList.getListComissaoDMN());
		
		ComissaoWrapperDMN conquistaList = edwService.findDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "CONQUISTA", "Executivo Interno");
		addConquista(lista, conquistaList.getListComissaoDMN());
		
		JRReportDataObject jr = createReportDataObject(lista);
		
		Map<String, Object> parametersReport = new HashMap<String, Object>();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

        parametersReport.put("filial", executivoTerritorioService.getLocalizacaoFromExecutivo(cargoCpf.toString()));
        parametersReport.put("competencia", toCompetencia(anoCalculo + "-" + mesCalculo + "-01"));
        
        if (!manutencaoList.getListComissaoDMN().isEmpty()) {
            parametersReport.put("nomeExecutivo", manutencaoList.getListComissaoDMN().iterator().next().getCargoNome());
            parametersReport.put("matricula", manutencaoList.getListComissaoDMN().iterator().next().getNumeroMatricula().toString());
        }
        
		ComissaoWrapperDMN totalFob = edwService.findDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "FOB", "Executivo Interno");
        
		parametersReport.put("manutencaoSobreSalario", formatCurrency(somaReceita(manutencaoList.getListComissaoDMN()).toString()));
        parametersReport.put("clientesNovos", formatCurrency(somaReceita(conquistaList.getListComissaoDMN()).toString()));
        parametersReport.put("diferenca", "");
        parametersReport.put("servicosAdicionais", formatCurrency(somaReceita(servicoAdicionalList.getListComissaoDMN()).toString()));
        parametersReport.put("crossSelling", "");
        parametersReport.put("fobDirigido", formatCurrency(somaFob(totalFob.getListComissaoDMN()).toString()));
        
		jr.setParameters(parametersReport);
		return jr;
	}

	private JRReportDataObject createReportDataObject(
			final List<DemonstrativoExecutivoInternoVisualization> lista) {
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

	private void addConquista(List<DemonstrativoExecutivoInternoVisualization> lista, List<ComissaoDMN> listComissaoDMN) {
		for (ComissaoDMN comissaoDMN : listComissaoDMN) {
			boolean novo = false;
			String territorioName = comissaoDMN.getTerritorioNome();
			DemonstrativoExecutivoInternoVisualization visualizationBean = findTerritorioInList(lista, territorioName);
			if (visualizationBean == null) {
				visualizationBean = new DemonstrativoExecutivoInternoVisualization();
				visualizationBean.setTerritorio(comissaoDMN.getTerritorioNome());
				
				novo = true;
			}
			
			if (comissaoDMN.getTerritorioModal().equals("RODOVIARIO")) {
				visualizationBean.addClienteNovoRodoviario(comissaoDMN.getTerritorioReceita());
			} else if (comissaoDMN.getTerritorioModal().equals("AEREO")) {
				visualizationBean.addClienteNovoAereo(comissaoDMN.getTerritorioReceita());
			}
			
			if (novo) {
				lista.add(visualizationBean);
			}
		}
	}

	private void addServicoAdicional(List<DemonstrativoExecutivoInternoVisualization> lista, List<ComissaoDMN> listComissaoDMN) {
		for (ComissaoDMN comissaoDMN : listComissaoDMN) {
			boolean novo = false;
			String territorioName = comissaoDMN.getTerritorioNome();
			DemonstrativoExecutivoInternoVisualization visualizationBean = findTerritorioInList(lista, territorioName);
			if (visualizationBean == null) {
				visualizationBean = new DemonstrativoExecutivoInternoVisualization();
				visualizationBean.setTerritorio(comissaoDMN.getTerritorioNome());
				
				novo = true;
			}
			
			visualizationBean.addReceitaLiquidaPaga(comissaoDMN.getTerritorioReceita());
			
			if (novo) {
				lista.add(visualizationBean);
			}
		}
	}

	private void addManutencao(List<DemonstrativoExecutivoInternoVisualization> lista, List<ComissaoDMN> listComissaoDMN) {
		for (ComissaoDMN comissaoDMN : listComissaoDMN) {
			boolean novo = false;
			String territorioName = comissaoDMN.getTerritorioNome();
			DemonstrativoExecutivoInternoVisualization visualizationBean = findTerritorioInList(lista, territorioName);
			if (visualizationBean == null) {
				visualizationBean = new DemonstrativoExecutivoInternoVisualization();
				visualizationBean.setTerritorio(comissaoDMN.getTerritorioNome());
				
				novo = true;
			}
			
			if (comissaoDMN.getTerritorioModal().equals("RODOVIARIO")) {
				visualizationBean.addMetaRodoviario(comissaoDMN.getTerritorioMeta());
				visualizationBean.addReceitaRodoviario(comissaoDMN.getTerritorioReceita());
			} else if (comissaoDMN.getTerritorioModal().equals("AEREO")) {
				visualizationBean.addMetaAereo(comissaoDMN.getTerritorioMeta());
				visualizationBean.addReceitaAereo(comissaoDMN.getTerritorioReceita());
			}
			
			if (novo) {
				lista.add(visualizationBean);
			}
		}
	}

	private DemonstrativoExecutivoInternoVisualization findTerritorioInList(List<DemonstrativoExecutivoInternoVisualization> lista, String territorioName) {
		for (DemonstrativoExecutivoInternoVisualization demonstrativoExecutivoInternoVisualization : lista) {
			if (demonstrativoExecutivoInternoVisualization.getTerritorio().equals(territorioName)) {
				return demonstrativoExecutivoInternoVisualization;
			}
		}
		return null;
	}

	private BigDecimal somaReceita(List<ComissaoDMN> listComissaoDMN) {
		BigDecimal soma = new BigDecimal(0);
		
		for(ComissaoDMN comissao : listComissaoDMN) {
        	BigDecimal somaTerritorioReceita = comissao.getTerritorioReceita();
        	if (somaTerritorioReceita != null) {
        		soma = soma.add(somaTerritorioReceita);
        	}
        }

		return soma;
	}

	private BigDecimal somaFob(List<ComissaoDMN> listComissaoDMN) {
		BigDecimal soma = new BigDecimal(0);
		
		for(ComissaoDMN comissao : listComissaoDMN) {
        	BigDecimal somaTerritorioReceita = comissao.getSomaComissaoValorCalculado();
        	if (somaTerritorioReceita != null) {
        		soma = soma.add(somaTerritorioReceita);	
        	}
        }

		return soma;
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
		if ("01".equals(mes)) {
			return "Jan";
		}
		if ("02".equals(mes)) {
			return "Fev";
		}
		if ("03".equals(mes)) {
			return "Mar";
		}
		if ("04".equals(mes)) {
			return "Abr";
		}
		if ("05".equals(mes)) {
			return "Mai";
		}
		if ("06".equals(mes)) {
			return "Jun";
		}
		if ("07".equals(mes)) {
			return "Jul";
		}
		if ("08".equals(mes)) {
			return "Ago";
		}
		if ("09".equals(mes)) {
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


	private String formatCurrency(String rawNumber) {
		if (rawNumber == null || rawNumber.isEmpty()) {
			return "";
		}
		return NumberFormat.getCurrencyInstance().format(new BigDecimal(rawNumber)).substring(3);
	}

	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}
	public void setExecutivoTerritorioService(
			ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}
}

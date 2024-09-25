package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;

public class DemonstrativoFieldService extends ReportServiceSupport {

	private EdwService edwService;
	private FilialService filialService;
	private UsuarioLMSService usuarioLmsService;
	private ExecutivoTerritorioService executivoTerritorioService;
	
	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = (TypedFlatMap) criteria;

		Integer mesCalculo = map.getInteger("mesCalculo");
		Integer anoCalculo = map.getInteger("anoCalculo");
		String tpModal = toModal(map.getString("tpModal"));
		Long cargoCpf = map.getLong("cargoCpf");
		
		ComissaoWrapperDMN wrapper = edwService.findDemonstrativoPagamentoByCpfComModal(cargoCpf, mesCalculo, anoCalculo, tpModal, "Executivo Externo");
        ComissaoWrapperDMN totaisCaixas = edwService.findTotalDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "MANUTENCAO,CONQUISTA", "Executivo Externo");
        ComissaoWrapperDMN totaisServicosAdicionaisDados = edwService.findTotalDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, "SERVICO_ADICIONAL", "Executivo Externo");
        ComissaoWrapperDMN totalComissoesDados = edwService.findTotalDemonstrativoPagamentoByCpf(cargoCpf, mesCalculo, anoCalculo, null, "Executivo Externo");

		convertBasedOnCalentarioTnt(wrapper.getListComissaoDMN());
		
		List<ComissaoDMN> a = groupAndSumCif(wrapper.getListComissaoDMN());
		List<ComissaoDMN> b = groupAndSumFob(wrapper.getListComissaoDMN());
		
		a.addAll(b);
		
		final List<DemonstrativoComissoesReportVisualization> list = groupCifAndFob(a);
		BigDecimal[] totais = somaTotais(list);
		
		BigDecimal totalCif = totais[0];
		BigDecimal totalFob = totais[1];
		BigDecimal totalCifFob = totalCif.add(totalFob);
		BigDecimal valorTotal = totais[3];
		
		formatListForVisualization(list);
		Collections.sort(list);
		
		JRReportDataObject jr = createReportDataObject(list);
		Double ceil = Math.ceil((double)list.size()/ 13);
		int numberOfPages = ceil.intValue() + 1;
		
		
		int numberOfPages2 = -1;
		if (list.size() % 13 == 0 || list.size() % 13 == 12) {
			numberOfPages += 1;
			numberOfPages2 = numberOfPages-1;
		}
		
		
		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

        ComissaoDMN caixasDados = totaisCaixas.getListComissaoDMN().iterator().next();
        
        String somaComissaoValorBase = caixasDados.getSomaComissaoValorBase() == null ? "-" : caixasDados.getSomaComissaoValorBase().toString(); 
        
		parametersReport.put("totalDemonstrativoComissao", somaComissaoValorBase);
        parametersReport.put("comissaoRodo", formatCurrency(caixasDados.getSomaComissaoValorCalculado().toString()));
        parametersReport.put("servicosAdicionais", formatCurrency(totaisServicosAdicionaisDados.getListComissaoDMN().iterator().next().getSomaComissaoValorCalculado().toString()));
        parametersReport.put("totalComissao", formatCurrency(totalComissoesDados.getListComissaoDMN().iterator().next().getSomaComissaoValorCalculado().toString()));
        parametersReport.put("localizacao", executivoTerritorioService.getLocalizacaoFromExecutivo(cargoCpf.toString()));
        parametersReport.put("competencia", toCompetencia(anoCalculo + "-" + mesCalculo + "-01"));
        parametersReport.put("rangeDatas", getRangeFromCalendarioTnt(mesCalculo, anoCalculo));
        parametersReport.put("tpTransporte", tpModal);
        parametersReport.put("nrMatricula", getNrMatricula(wrapper));
        parametersReport.put("nomeVendedor", wrapper.getListComissaoDMN().get(0).getCargoNome());
        parametersReport.put("numberOfPages", numberOfPages);
        parametersReport.put("numberOfPages2", numberOfPages2);
        parametersReport.put("somaCif", formatCurrency(totalCif.toString()));
        parametersReport.put("somaFob", formatCurrency(totalFob.toString()));
        parametersReport.put("somaCifFob", formatCurrency(totalCifFob.toString()));
        parametersReport.put("somaValor", formatCurrency(valorTotal.toString()));
     
		jr.setParameters(parametersReport);
		
		return jr;
	}


	private String getNrMatricula(ComissaoWrapperDMN wrapper) {
		return wrapper.getListComissaoDMN().get(0).getNumeroMatricula().toString();
	}


	private String toModal(String modal) {
		if (modal == null) {
			return null;
		}
		
		if (modal.equals("R")) {
			return "Rodoviario";
		}
		
		if (modal.equals("A")) {
			return "Aereo";
		}

		return null;
	}



	private BigDecimal[] somaTotais(List<DemonstrativoComissoesReportVisualization> list) {
		BigDecimal totalCif = new BigDecimal(0);
		BigDecimal totalFob = new BigDecimal(0);
		BigDecimal totalCifFob = new BigDecimal(0);
		BigDecimal totalValor = new BigDecimal(0);
		
		for (DemonstrativoComissoesReportVisualization l : list) {
			BigDecimal cif = l.getCif() == null ? new BigDecimal(0) : new BigDecimal(l.getCif());
			BigDecimal fob = l.getFob() == null ? new BigDecimal(0) : new BigDecimal(l.getFob());
			BigDecimal valor = l.getValor() == null ? new BigDecimal(0) : new BigDecimal(l.getValor());
			
			totalCif = totalCif.add(cif);
			totalFob = totalFob.add(fob);
			totalCifFob = totalCifFob.add(totalCif.add(totalFob));
			totalValor = totalValor.add(valor);
		}
		
		return new BigDecimal[]{totalCif, totalFob, totalCifFob, totalValor};
	}

	private void formatListForVisualization(List<DemonstrativoComissoesReportVisualization> list) {
		
		for (DemonstrativoComissoesReportVisualization l : list) {
			if (l.getRazaoSocial().length() > 30) {
				l.setRazaoSocial(l.getRazaoSocial().substring(0, 30));	
			}
			l.setClienteTerritorio(l.getClienteTerritorio().substring(0, 11));
			l.setExecutivoTerritorio(l.getExecutivoTerritorio().substring(0, 11));
			
			String cif = l.getCif() == null ? "0" : l.getCif();
			String fob = l.getFob() == null ? "0" : l.getFob();
			l.setCifFob(new BigDecimal(cif).add(new BigDecimal(fob)).toString());
			l.setTipo(l.getTipo().substring(0, 1));
			
			l.setClienteTerritorio(toBrazilianDate(l.getClienteTerritorio()));
			l.setExecutivoTerritorio(toBrazilianDate(l.getExecutivoTerritorio()));
			l.setEmissao(toCompetencia(l.getEmissao()));

			String formattedCif = formatCurrency(l.getCif());
			String formattedFob = formatCurrency(l.getFob());
			String formattedCifFob = formatCurrency(l.getCifFob());
			String formattedValor = formatCurrency(l.getValor());
			
			l.setCif(formattedCif);
			l.setFob(formattedFob);
			l.setCifFob(formattedCifFob);
			l.setValor(formattedValor);
		}
	}

	private String formatCurrency(String rawNumber) {
		if (rawNumber == null || rawNumber.isEmpty()) {
			return "";
		}
		return NumberFormat.getCurrencyInstance().format(new BigDecimal(rawNumber)).substring(3);
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



	private String toBrazilianDate(String s) {
		String[] split = s.split("-");
		return split[2].trim() + "/" + split[1] + "/" + split[0];
	}
	

	private List<ComissaoDMN> groupAndSumCif(List<ComissaoDMN> listComissaoDMN) {
		List<ComissaoDMN> comissoes = new ArrayList<ComissaoDMN>();

		for(ComissaoDMN comissao : listComissaoDMN) {
			if (comissao.getTipoFrete().equals("CIF")) {
				ComissaoDMN findInList = findInList(comissoes, comissao.getClienteTomadorNome(), comissao.getDocumentoDataEmissao(), "CIF");
				if (findInList != null) {
					findInList.setSomaComissaoValorBase(findInList.getSomaComissaoValorBase().add(comissao.getSomaComissaoValorBase()));
					findInList.setSomaComissaoValorCalculado(findInList.getSomaComissaoValorCalculado().add(comissao.getSomaComissaoValorCalculado()));
					findInList.setQuantidadeTotalDoctoServico(findInList.getQuantidadeTotalDoctoServico() + comissao.getQuantidadeTotalDoctoServico());
				} else {
					comissoes.add(comissao);
				}
			}
		}
		
		return comissoes;
	}

	private List<ComissaoDMN> groupAndSumFob(List<ComissaoDMN> listComissaoDMN) {
		List<ComissaoDMN> comissoes = new ArrayList<ComissaoDMN>();

		for(ComissaoDMN comissao : listComissaoDMN) {
			if (comissao.getTipoFrete().equals("FOB")) {
				ComissaoDMN findInList = findInList(comissoes, comissao.getClienteTomadorNome(), comissao.getDocumentoDataEmissao(), "FOB");
				if (findInList != null) {
					findInList.setSomaComissaoValorBase(findInList.getSomaComissaoValorBase().add(comissao.getSomaComissaoValorBase()));
					findInList.setSomaComissaoValorCalculado(findInList.getSomaComissaoValorCalculado().add(comissao.getSomaComissaoValorCalculado()));
					findInList.setQuantidadeTotalDoctoServico(findInList.getQuantidadeTotalDoctoServico() + comissao.getQuantidadeTotalDoctoServico());
				} else {
					comissoes.add(comissao);
				}
			}
		}
		
		return comissoes;
	}



	private ComissaoDMN findInList(List<ComissaoDMN> comissoes, String clienteTomadorNome, String documentoDataEmissao, String cifOrFob) {
		for (ComissaoDMN comissaoDMN : comissoes) {
			if (comissaoDMN.getClienteTomadorNome().equals(clienteTomadorNome) 
					&& comissaoDMN.getDocumentoDataEmissao().equals(documentoDataEmissao)
					&& comissaoDMN.getTipoFrete().equals(cifOrFob)) {
				return comissaoDMN;
			}
		}
		return null;
	}



	private String getRangeFromCalendarioTnt(Integer mesCalculo, Integer anoCalculo) {
		CalendarioTntDMN calendarioTnt = edwService.findCalendarioTNTByData(new YearMonthDay(anoCalculo, mesCalculo, 15));
		
		String dataInicioMes = toBrazilianDate(calendarioTnt.getDataInicioMesProducao());
		String dataFimMes = toBrazilianDate(calendarioTnt.getDataFimMesProducao());
		
		return dataInicioMes + " - " + dataFimMes;
	}



	private JRReportDataObject createReportDataObject(
			final List<DemonstrativoComissoesReportVisualization> list) {
		return new JRReportDataObject() {
			Map parameters = new HashMap();

			@Override
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(list);
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

	
	
	private List<DemonstrativoComissoesReportVisualization> groupCifAndFob(List<ComissaoDMN> listComissaoDMN) {
		List<DemonstrativoComissoesReportVisualization> myList = new ArrayList<DemonstrativoComissoesReportVisualization>();
	
		for(ComissaoDMN comissao : listComissaoDMN) {
			if (comissao.getTipoFrete().equals("CIF")) {
				myList.add(createBeanForVisualizationFromDomain(comissao));
			}
		}

		for(ComissaoDMN comissao : listComissaoDMN) {
			if (comissao.getTipoFrete().equals("FOB")) {
				DemonstrativoComissoesReportVisualization cifBean = lookForCif(myList, comissao.getClienteTomadorNome(), comissao.getDocumentoDataEmissao());
				if (cifBean != null) {
					cifBean.setFob(comissao.getSomaComissaoValorBase().toString());
					
					Long qtdCtrcs = Long.parseLong(cifBean.getQtdCtrc()) ;
					Long qtdCtrcsSomada = ((qtdCtrcs + comissao.getQuantidadeTotalDoctoServico()));
					
					BigDecimal valor = new BigDecimal(cifBean.getValor());
					
					BigDecimal totalCifFob = new BigDecimal(cifBean.getCif()).add(comissao.getSomaComissaoValorBase());
					
					cifBean.setCifFob(totalCifFob.toString());
					cifBean.setQtdCtrc(qtdCtrcsSomada.toString());
					cifBean.setValor(valor.add(comissao.getSomaComissaoValorCalculado()).toString());
				} else {
					myList.add(createBeanForVisualizationFromDomain(comissao));	
				}
			}
		}

		return myList;
	}

	private DemonstrativoComissoesReportVisualization lookForCif(List<DemonstrativoComissoesReportVisualization> myList, String nome, String documentoDataEmissao) {
		for(DemonstrativoComissoesReportVisualization bean : myList) {
			if (nome.equals(bean.getRazaoSocial()) && bean.getEmissao().equals(documentoDataEmissao)) {
				return bean;
			}
		}
		return null;
	}


	private DemonstrativoComissoesReportVisualization createBeanForVisualizationFromDomain(ComissaoDMN comissao) {

		String cnpj = comissao.getClienteTomadorIdentificacao().toString();
		String razaoSocial = comissao.getClienteTomadorNome();
		String territorio = comissao.getTerritorioNome();
		String clienteTerritorio = comissao.getClienteTomadorDataInicio();
		String executivoTerritorio = comissao.getCargoDataInicio();
		String emissao = comissao.getDocumentoDataEmissao();
		String cif = comissao.getTipoFrete().equals("CIF") ? comissao.getSomaComissaoValorBase().toString() : null;
		String fob = comissao.getTipoFrete().equals("FOB") ? comissao.getSomaComissaoValorBase().toString() : null;
		String cifFob = null;
		String qtdCtrc = comissao.getQuantidadeTotalDoctoServico().toString();
		String percentual = comissao.getPercentualComissao().toString();
		String tipo = comissao.getTipoComissao();
		String valor = comissao.getSomaComissaoValorCalculado().toString();
		String cargoNome = comissao.getCargoNome();
		BigDecimal divide = comissao.getTerritorioReceita().divide(comissao.getTerritorioMeta(), 3, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100)).stripTrailingZeros();
		String percentualAtingimentoMeta = divide.toString();
		
		return new DemonstrativoComissoesReportVisualization(cnpj, razaoSocial, territorio, clienteTerritorio, 
				executivoTerritorio, emissao, cif, fob, cifFob, qtdCtrc, percentual, tipo, valor, cargoNome, percentualAtingimentoMeta);
	}



	private void convertBasedOnCalentarioTnt(List<ComissaoDMN> listComissaoDMN) {
		for(ComissaoDMN c : listComissaoDMN) {
			String[] splitted = c.getDocumentoDataEmissao().split("-");
			YearMonthDay dtEmissao = new YearMonthDay(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2].substring(0, 2)));
			
			CalendarioTntDMN calendarioTntDMN = edwService.findCalendarioTNTByData(dtEmissao);
			//String dataCalendarioTnt = calendarioTntDMN.getDataInicioMes();
			String dataCalendarioTnt = calendarioTntDMN.getAno() + "-" + calendarioTntDMN.getNumeroMesProducao() + "-" + "01";
			c.setDocumentoDataEmissao(dataCalendarioTnt);
		}
	}

	public EdwService getEdwService() {
		return edwService;
	}
	
	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}

	public FilialService getFilialService() {
		return filialService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public UsuarioLMSService getUsuarioLmsService() {
		return usuarioLmsService;
	}
	public void setUsuarioLmsService(UsuarioLMSService usuarioLmsService) {
		this.usuarioLmsService = usuarioLmsService;
	}
	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}
	
	
}

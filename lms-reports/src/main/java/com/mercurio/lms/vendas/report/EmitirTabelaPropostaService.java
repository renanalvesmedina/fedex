package com.mercurio.lms.vendas.report;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.MultiReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.SubtipoTabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TipoTabelaPrecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ClienteMergerPDF;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.EmitirTermoPDF;
import com.mercurio.lms.vendas.util.SimulacaoUtils;
import com.mercurio.lms.vendas.util.TemplatePdf;

/**
 * @author claitong
 * 
 * @spring.bean id="lms.vendas.report.emitirTabelaPropostaService"
 */

public class EmitirTabelaPropostaService extends MultiReportServiceSupport {

	private static final String AEREO_NACIONAL_CONVENCIONAL = "ANC";
	
	private ParametroClienteService parametroClienteService;
	private TabelaPrecoService tabelaPrecoService;
	private TipoTabelaPrecoService tipotabelaPrecoService;
	private SubtipoTabelaPrecoService subtipoTabelaPrecoService;
	private ServicoService servicoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private EmitirTabelasClientesService emitirTabelasClienteService;
	private TabelaFreteMinimoProgressivoTarifaService tabelaFreteMinimoProgressivoTarifaService;
	private TabelaFreteMinimoProgressivoTaxaEmbutidaService tabelaFreteMinimoProgressivoTaxaEmbutidaService;
	private TabelaFretePercentualService tabelaFretePercentualService;
	private TabelaFreteMinimoProgressivoPesoExcedenteService tabelaFreteMinimoProgressivoPesoExcedenteService;
	private TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService;
	private TabelaFreteMinimoProgressivoRotaService tabelaFreteMinimoProgressivoRotaService;
	private TabelaFreteMinimoTarifaService tabelaFreteMinimoTarifaService;
	private TabelaFreteMinimoRotaService tabelaFreteMinimoRotaService;
	private TabelaFreteAereoEspecificaKiloExcedenteService tabelaFreteAereoEspecificaKiloExcedenteService;
	private TabelaFreteAereoConvencionalService tabelaFreteAereoConvencionalService;
	private EmitirTabelaFreteAereoPadraoService emitirTabelaFreteAereoPadraoService;
	private TabelaFreteAereoTaxaMinimaPesoExcedenteService tabelaFreteAereoTaxaMinimaPesoExcedenteService;
	private TabelaFreteAereoProgressivaPesoExcedenteService tabelaFreteAereoProgressivaPesoExcedenteService;
	private TabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService;
	private TabelaFreteAereoProgressivoService tabelaFreteAereoProgressivoService;
	private TabelaFreteAereoPercentualService tabelaFreteAereoPercentualService;
	private TabelaFreteVolumeService tabelaFreteVolumeService;
	private TabelaFreteAereoVolumeService tabelaFreteAereoVolumeService;
	private TabelaFreteDiferenciadaService tabelaFreteDiferenciadaService;
	private TabelaFretePesoCargaCompletaTarifaService tabelaFretePesoCargaCompletaTarifaService;
	private TypedFlatMap parameters;
	private List<Map<String, String>> precos;
	protected Simulacao simulacao;
	protected TipoTabelaPreco tipoTabelaPreco;
	protected SubtipoTabelaPreco subTipoTabelaPreco;
	protected TabelaDivisaoCliente tabelaDivisaoCliente;

	protected List<ParametroCliente> parametrosCliente;
	protected Servico servico;
	protected TabelaPreco tabelaPreco;
	protected DivisaoCliente divisaoCliente;

	protected String validTipoModal;
	protected String validTipoAbrangencia;
	protected String[] validTipoTabelaPreco;
	private final Log log = LogFactory.getLog(EmitirTabelaPropostaService.class);

	@Override
	protected MultiReportCommand prepareMultiReport(TypedFlatMap parameters) throws Exception {
		MultiReportCommand mrc = new MultiReportCommand("EmitirTabelaPropostaService");
		return mrc;
	}

	public File executePDF(TypedFlatMap parameters) {
		boolean isTarifada = true;
		ClienteMergerPDF clienteMerger = new ClienteMergerPDF();
		List<TemplatePdf> templatesPdf = new ArrayList<TemplatePdf>();

		this.parameters = parameters;

		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		this.simulacao = simulacao;
		parameters.put("simulacao", simulacao);
		parameters.put("idSimulacao", simulacao.getIdSimulacao());
		parameters.put("idCliente", simulacao.getClienteByIdCliente().getIdCliente());

		// parametros da simulaçao
		this.parametrosCliente = simulacao.getParametroClientes();
		this.servico = simulacao.getServico();
		this.tabelaPreco = simulacao.getTabelaPreco();
		this.divisaoCliente = simulacao.getDivisaoCliente();

		if (servico != null) {
			parameters.put("idServico", servico.getIdServico());
			parameters.put("abrangencia", servico.getDsServico().getValue());
		}
		if (simulacao.getTabelaPreco() != null) {
			TypedFlatMap tabelaPreco = tabelaPrecoService.findByIdMap(simulacao.getTabelaPreco().getIdTabelaPreco());
			TipoTabelaPreco tipoTabelaPreco = tipotabelaPrecoService.findById(tabelaPreco.getLong("tipoTabelaPreco.idTipoTabelaPreco"));
			SubtipoTabelaPreco subTipoTabelaPreco = subtipoTabelaPrecoService
					.findById(tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"));
			this.subTipoTabelaPreco = subTipoTabelaPreco;
			parameters.put("idTabelaPreco", simulacao.getTabelaPreco().getIdTabelaPreco());
		}

		TabelaDivisaoCliente tabelaDivisaoCliente = null;
		if (simulacao.getDivisaoCliente() != null) {
			tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(simulacao.getDivisaoCliente().getIdDivisaoCliente(),
					simulacao.getServico().getIdServico());
			parameters.put("idTabelaDivisao", tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
			parameters.put("idDivisao", simulacao.getDivisaoCliente().getIdDivisaoCliente());
		}

		parameters.put("parametroCliente", parametrosCliente);
		List<Long> listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			listaParametrosOK.add(pc.getIdParametroCliente());
		}
		parameters.put("LISTAPARAMETROS", listaParametrosOK);

		
		if(isImpressaoAereoNovo(simulacao)){
			executeImpressaoAereoNovo(parameters, templatesPdf, simulacao, listaParametrosOK);
		} else {
			executeImpressaoRodo(parameters, isTarifada, templatesPdf, simulacao, listaParametrosOK);
		}

		if (!templatesPdf.isEmpty()) {
			templatesPdf.add(new EmitirTermoPDF());
			clienteMerger.merge(templatesPdf);
		} else {
			throw new BusinessException("emptyReport");
		}

		return clienteMerger.getFile();
	}
	
	private Boolean isImpressaoAereoNovo(Simulacao simulacao){
		Boolean retorno = Boolean.FALSE;
		Servico servicoAereoNacionalConvencional = servicoService.findServicoBySigla(AEREO_NACIONAL_CONVENCIONAL);
		
		if(simulacao != null && simulacao.getBlNovaUI() != null && simulacao.getBlNovaUI() 
				&& simulacao.getServico() != null 
				&& servicoAereoNacionalConvencional != null 
				&& servicoAereoNacionalConvencional.getIdServico().equals(simulacao.getServico().getIdServico())){
			retorno = Boolean.TRUE;
		}
		
		return retorno;
	}
	
	private void executeImpressaoAereoNovo(TypedFlatMap parameters, List<TemplatePdf> templatesPdf, Simulacao simulacao,
			List<Long> listaParametrosOK) {

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if("P".equals(pc.getTpSituacaoParametro().getValue())){
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}
		
		if (ConstantesVendas.TP_PROPOSTA_CONVENCIONAL.equals(simulacao.getTpGeracaoProposta().getValue())) {
			if (!listaParametrosOK.isEmpty()) {
				parameters.put("listaParametros", listaParametrosOK);
				try {
					precos = tabelaFreteAereoConvencionalService.findDados(parameters);

					if (precos != null && !precos.isEmpty()) {
						log.debug("addSubReportConvencional");
						emitirTabelasClienteService.addSubreportConvencional(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}
			}
		} else {
			if (!listaParametrosOK.isEmpty()) {
				parameters.put("listaParametros", listaParametrosOK);
				try {
					precos = tabelaFreteAereoEspecificaKiloExcedenteService.findDados(parameters);

					if (precos != null && !precos.isEmpty()) {
						log.debug("addSubReportEspecificoKiloExcedente");
						emitirTabelasClienteService.addSubreportEspecificoKiloExcedente(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}
			}
		}
		
		emitirTabelasClienteService.addCapaAereo(parameters, templatesPdf);
	}
	
	private void executeImpressaoRodo(TypedFlatMap parameters, boolean isTarifada, List<TemplatePdf> templatesPdf, Simulacao simulacao,
			List<Long> listaParametrosOK) {
		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P") && (BigDecimalUtils.ZERO.equals(pc.getPcFretePercentual()) == false)
					&& (pc.getVlMinimoFretePercentual() != null) && (pc.getPsFretePercentual() != null) && (pc.getVlToneladaFretePercentual() != null)) {

				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			try {
				precos = tabelaFretePercentualService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFretePercentual");
					emitirTabelasClienteService.addSubReportTabelaFretePercentual(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if ((simulacao.getTabelaPreco() != null && simulacao.getTabelaPreco().getTpCalculoFretePeso() != null)
				&& "E".equals(simulacao.getTabelaPreco().getTpCalculoFretePeso().getValue())) {
			isTarifada = false;

			ParametroCliente pc = parametrosCliente.iterator().next();

			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}

			if (pc != null) {
				parameters.put("idParametroCliente", pc.getIdParametroCliente());
			}
			try {
				precos = tabelaFreteMinimoProgressivoPesoExcedenteService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoProgressivoPesoExcedenteReport");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoProgressivoPesoExcedenteReport(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}

		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P") && "V".equals(pc.getTpIndicadorFretePeso().getValue())
					&& "P".equals(pc.getTpIndicadorMinFretePeso().getValue()) && BigDecimalUtils.gtZero(pc.getVlMinimoFreteQuilo())
					&& BigDecimalUtils.ZERO.equals(pc.getVlPercMinimoProgr()) && pc.getBlPagaPesoExcedente()) {
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK);
			precos = tabelaFreteMinimoPesoExcedenteService.findDados(parameters);

			if (precos != null && !precos.isEmpty()) {
				log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
				emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
			}
		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if ("P".equalsIgnoreCase(pc.getTpSituacaoParametro().getValue()) && "V".equals(pc.getTpIndicadorFretePeso().getValue())
					&& "T".equals(pc.getTpIndicadorMinFretePeso().getValue()) && "V".equals(pc.getTpIndicadorAdvalorem().getValue())) {
				Map<String, Object> map = parametroClienteService.findByIdMap(pc.getIdParametroCliente());
				if (map.get("unidadeFederativaByIdUfOrigem.siglaDescricao") == null)
					continue;
				if (map.get("unidadeFederativaByIdUfDestino.siglaDescricao") == null)
					continue;

				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK);
			parameters.put("pagaFreteTonelada", simulacao.getBlPagaFreteTonelada());
			try {
				precos = tabelaFreteMinimoProgressivoRotaService.findDados(parameters);

				if (precos != null && !precos.isEmpty()) {
					log.debug("addReportTabelaFretePercentual");
					emitirTabelasClienteService.addReportTabelaFretePercentual(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}

		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")
					&& ("T".equals(pc.getTpIndicadorFretePeso().getValue()) || "D".equals(pc.getTpIndicadorFretePeso().getValue()) || "A".equals(pc
							.getTpIndicadorFretePeso().getValue())) && ("P".equals(pc.getTpIndicadorMinFretePeso().getValue()))
					&& (BigDecimalUtils.ZERO.equals(pc.getVlMinimoFreteQuilo()))) {
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			parameters.put("idParametroCliente", listaParametrosOK.iterator().next());
			precos = tabelaFreteMinimoTarifaService.findDados(parameters);
		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P") && ("V".equals(pc.getTpIndicadorFretePeso().getValue()))
					&& ("P".equals(pc.getTpIndicadorMinFretePeso().getValue())) && (CompareUtils.ge(pc.getVlMinimoFreteQuilo(), BigDecimalUtils.ZERO))
					&& (BigDecimalUtils.ZERO.equals(pc.getVlPercMinimoProgr())) && (pc.getBlPagaPesoExcedente() == false)) {
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK);
			try {
				precos = tabelaFreteMinimoRotaService.findDados(parameters);

				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaPercentual");
					emitirTabelasClienteService.addSubReportTabelaPercentual(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P"))
				continue;

			if ((!"A".equals(pc.getTpIndicadorFretePeso().getValue()) && !"D".equals(pc.getTpIndicadorFretePeso().getValue()))
					&& (!"A".equals(pc.getTpIndicVlrTblEspecifica().getValue()) && !"D".equals(pc.getTpIndicVlrTblEspecifica().getValue())))
				continue;

			Map<String, Object> map = parametroClienteService.findByIdMap(pc.getIdParametroCliente());
			if (map.get("unidadeFederativaByIdUfOrigem.siglaDescricao") != null
					&& StringUtils.isNotEmpty(map.get("unidadeFederativaByIdUfOrigem.siglaDescricao").toString()))
				continue;
			if (map.get("unidadeFederativaByIdUfDestino.siglaDescricao") != null
					&& StringUtils.isNotEmpty(map.get("unidadeFederativaByIdUfDestino.siglaDescricao").toString()))
				continue;

			listaParametrosOK.add(pc.getIdParametroCliente());
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK.iterator().next());
			parameters.put("relatorio", "27");
			try {
				precos = emitirTabelaFreteAereoPadraoService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P") && ("V".equals(pc.getTpIndicadorFretePeso().getValue()))
					&& ("P".equals(pc.getTpIndicadorMinFretePeso().getValue())) && (BigDecimalUtils.ZERO.equals(pc.getVlMinimoFreteQuilo()) == false)
					&& (BigDecimalUtils.ZERO.equals(pc.getVlPercMinimoProgr()) == false) && (pc.getBlPagaPesoExcedente() == false)) {
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK);
			parameters.put("relatorio", "28");
			try {
				precos = tabelaFreteAereoTaxaMinimaPesoExcedenteService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteAereoTaxaMinimaPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteAereoTaxaMinimaPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if (simulacao.getTabelaPreco() != null && emitirTabelasClienteService.hasFaixaRota(simulacao.getTabelaPreco().getIdTabelaPreco())) {
			ParametroCliente pc = parametrosCliente.iterator().next();

			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}

			if (pc != null) {
				isTarifada = false;

				parameters.put("idParametroCliente", pc.getIdParametroCliente());
				try {
					precos = tabelaFreteAereoProgressivaPesoExcedenteService.findDados(parameters);
					if (precos != null && !precos.isEmpty()) {
						log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
						emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}
			}
		}

		if ((simulacao.getTabelaPreco() != null && simulacao.getTabelaPreco().getTpCalculoFretePeso() != null)
				&& "E".equals(simulacao.getTabelaPreco().getTpCalculoFretePeso().getValue())
				&& !"X".equals(simulacao.getTabelaPreco().getTpCalculoFretePeso().getValue())) {

			isTarifada = false;

			ParametroCliente pc = parametrosCliente.iterator().next();

			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}

			if (pc != null) {
				parameters.put("idParametroCliente", pc.getIdParametroCliente());
			}
			try {
				precos = tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if ((simulacao.getTabelaPreco() != null && simulacao.getTabelaPreco().getTpCalculoFretePeso() != null)
				&& ("Q".equals(simulacao.getTabelaPreco().getTpCalculoFretePeso().getValue())
						|| "M".equals(simulacao.getTabelaPreco().getTpCalculoFretePeso().getValue()) || "T".equals(simulacao.getTabelaPreco()
						.getTpCalculoFretePeso().getValue()))) {

			if (!emitirTabelasClienteService.hasFaixaPorVolume(simulacao.getTabelaPreco().getIdTabelaPreco())) {

				ParametroCliente pc = parametrosCliente.iterator().next();
				if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
					pc = parametrosCliente.iterator().next();
				}

				if (pc != null) {
					parameters.put("idParametroCliente", pc.getIdParametroCliente());
				}
				try {
					precos = tabelaFreteAereoProgressivoService.findDados(parameters);
					if (precos != null && !precos.isEmpty()) {
						log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
						emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}

			}
		}

		listaParametrosOK = new ArrayList<Long>();
		for (ParametroCliente pc : parametrosCliente) {
			if (pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P") && (BigDecimalUtils.ZERO.equals(pc.getPcFretePercentual()) == false)
					&& (pc.getVlMinimoFretePercentual() != null) && (pc.getPsFretePercentual() != null) && (pc.getVlToneladaFretePercentual() != null)) {
				listaParametrosOK.add(pc.getIdParametroCliente());
			}
		}

		if (!listaParametrosOK.isEmpty()) {
			isTarifada = false;

			parameters.put("listaParametros", listaParametrosOK);
			try {
				precos = tabelaFreteAereoPercentualService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if (simulacao.getTabelaPreco() != null && emitirTabelasClienteService.hasFaixaPorVolume(simulacao.getTabelaPreco().getIdTabelaPreco())) {
			isTarifada = false;

			ParametroCliente pc = parametrosCliente.iterator().next();
			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}

			if (pc != null) {
				parameters.put("idParametroCliente", pc.getIdParametroCliente());
			}

			precos = null;
		}

		if (simulacao.getTabelaPreco() != null && emitirTabelasClienteService.hasFaixaPorVolume(simulacao.getTabelaPreco().getIdTabelaPreco())) {
			isTarifada = false;

			ParametroCliente pc = parametrosCliente.iterator().next();
			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}
			if (pc != null) {
				parameters.put("idParametroCliente", pc.getIdParametroCliente());
			}
			try {
				precos = tabelaFreteAereoVolumeService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if (CollectionUtils.isNotEmpty(parametrosCliente)) {
			ParametroCliente pc = parametrosCliente.iterator().next();
			if (!pc.getTpSituacaoParametro().getValue().equalsIgnoreCase("P")) {
				pc = parametrosCliente.iterator().next();
			}

			if (pc != null) {
				parameters.put("idParametroCliente", pc.getIdParametroCliente());
			}
			precos = tabelaFreteDiferenciadaService.findDados(parameters);
		}

		if (Boolean.TRUE.equals(simulacao.getBlEmiteCargaCompleta())) {
			isTarifada = false;
			try {
				precos = tabelaFretePesoCargaCompletaTarifaService.findDados(parameters);
				if (precos != null && !precos.isEmpty()) {
					log.debug("addSubReportTabelaFreteMinimoPesoExcedente");
					emitirTabelasClienteService.addSubReportTabelaFreteMinimoPesoExcedente(parameters, templatesPdf, precos);
				}
			} catch (Exception e) {
				precos = null;
			}
		}

		if (isTarifada && subTipoTabelaPreco != null) {
			if ("X".equals(subTipoTabelaPreco.getTpSubtipoTabelaPreco())) {
				try {
					precos = tabelaFreteMinimoProgressivoTarifaService.findDados(parameters);
					if (precos != null && !precos.isEmpty()) {
						log.debug("addReportTabelaMinimoProgressivoTarifa");
						emitirTabelasClienteService.addReportTabelaMinimoProgressivoTarifa(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}
			}
			if ("Y".equals(subTipoTabelaPreco.getTpSubtipoTabelaPreco())) {
				try {
					precos = tabelaFreteMinimoProgressivoTaxaEmbutidaService.findDados(parameters);
					if (precos != null && !precos.isEmpty()) {
						log.debug("addSubReportTabelaMinimoProgressivoTaxaEmbutida");
						emitirTabelasClienteService.addSubReportTabelaMinimoProgressivoTaxaEmbutida(parameters, templatesPdf, precos);
					}
				} catch (Exception e) {
					precos = null;
				}
			}
		}
	}

	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setTipotabelaPrecoService(TipoTabelaPrecoService tipotabelaPrecoService) {
		this.tipotabelaPrecoService = tipotabelaPrecoService;
	}

	public void setSubtipoTabelaPrecoService(SubtipoTabelaPrecoService subtipoTabelaPrecoService) {
		this.subtipoTabelaPrecoService = subtipoTabelaPrecoService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setEmitirTabelasClienteService(EmitirTabelasClientesService emitirTabelasClienteService) {
		this.emitirTabelasClienteService = emitirTabelasClienteService;
	}

	public TabelaFreteMinimoProgressivoTarifaService getTabelaFreteMinimoProgressivoTarifaService() {
		return tabelaFreteMinimoProgressivoTarifaService;
	}

	public void setTabelaFreteMinimoProgressivoTarifaService(TabelaFreteMinimoProgressivoTarifaService tabelaFreteMinimoProgressivoTarifaService) {
		this.tabelaFreteMinimoProgressivoTarifaService = tabelaFreteMinimoProgressivoTarifaService;
	}

	public TabelaFreteMinimoProgressivoTaxaEmbutidaService getTabelaFreteMinimoProgressivoTaxaEmbutidaService() {
		return tabelaFreteMinimoProgressivoTaxaEmbutidaService;
	}

	public void setTabelaFreteMinimoProgressivoTaxaEmbutidaService(
			TabelaFreteMinimoProgressivoTaxaEmbutidaService tabelaFreteMinimoProgressivoTaxaEmbutidaService) {
		this.tabelaFreteMinimoProgressivoTaxaEmbutidaService = tabelaFreteMinimoProgressivoTaxaEmbutidaService;
	}

	public TabelaFretePercentualService getTabelaFretePercentualService() {
		return tabelaFretePercentualService;
	}

	public void setTabelaFretePercentualService(TabelaFretePercentualService tabelaFretePercentualService) {
		this.tabelaFretePercentualService = tabelaFretePercentualService;
	}

	public TabelaFreteMinimoProgressivoPesoExcedenteService getTabelaFreteMinimoProgressivoPesoExcedenteService() {
		return tabelaFreteMinimoProgressivoPesoExcedenteService;
	}

	public void setTabelaFreteMinimoProgressivoPesoExcedenteService(
			TabelaFreteMinimoProgressivoPesoExcedenteService tabelaFreteMinimoProgressivoPesoExcedenteService) {
		this.tabelaFreteMinimoProgressivoPesoExcedenteService = tabelaFreteMinimoProgressivoPesoExcedenteService;
	}

	public TabelaFreteMinimoPesoExcedenteService getTabelaFreteMinimoPesoExcedenteService() {
		return tabelaFreteMinimoPesoExcedenteService;
	}

	public void setTabelaFreteMinimoPesoExcedenteService(TabelaFreteMinimoPesoExcedenteService tabelaFreteMinimoPesoExcedenteService) {
		this.tabelaFreteMinimoPesoExcedenteService = tabelaFreteMinimoPesoExcedenteService;
	}

	public TabelaFreteMinimoProgressivoRotaService getTabelaFreteMinimoProgressivoRotaService() {
		return tabelaFreteMinimoProgressivoRotaService;
	}

	public void setTabelaFreteMinimoProgressivoRotaService(TabelaFreteMinimoProgressivoRotaService tabelaFreteMinimoProgressivoRotaService) {
		this.tabelaFreteMinimoProgressivoRotaService = tabelaFreteMinimoProgressivoRotaService;
	}

	public TabelaFreteMinimoTarifaService getTabelaFreteMinimoTarifaService() {
		return tabelaFreteMinimoTarifaService;
	}

	public void setTabelaFreteMinimoTarifaService(TabelaFreteMinimoTarifaService tabelaFreteMinimoTarifaService) {
		this.tabelaFreteMinimoTarifaService = tabelaFreteMinimoTarifaService;
	}

	public TabelaFreteMinimoRotaService getTabelaFreteMinimoRotaService() {
		return tabelaFreteMinimoRotaService;
	}

	public void setTabelaFreteMinimoRotaService(TabelaFreteMinimoRotaService tabelaFreteMinimoRotaService) {
		this.tabelaFreteMinimoRotaService = tabelaFreteMinimoRotaService;
	}

	public EmitirTabelaFreteAereoPadraoService getEmitirTabelaFreteAereoPadraoService() {
		return emitirTabelaFreteAereoPadraoService;
	}

	public void setEmitirTabelaFreteAereoPadraoService(EmitirTabelaFreteAereoPadraoService emitirTabelaFreteAereoPadraoService) {
		this.emitirTabelaFreteAereoPadraoService = emitirTabelaFreteAereoPadraoService;
	}

	public TabelaFreteAereoTaxaMinimaPesoExcedenteService getTabelaFreteAereoTaxaMinimaPesoExcedenteService() {
		return tabelaFreteAereoTaxaMinimaPesoExcedenteService;
	}

	public void setTabelaFreteAereoTaxaMinimaPesoExcedenteService(
			TabelaFreteAereoTaxaMinimaPesoExcedenteService tabelaFreteAereoTaxaMinimaPesoExcedenteService) {
		this.tabelaFreteAereoTaxaMinimaPesoExcedenteService = tabelaFreteAereoTaxaMinimaPesoExcedenteService;
	}

	public TabelaFreteAereoProgressivaPesoExcedenteService getTabelaFreteAereoProgressivaPesoExcedenteService() {
		return tabelaFreteAereoProgressivaPesoExcedenteService;
	}

	public void setTabelaFreteAereoProgressivaPesoExcedenteService(
			TabelaFreteAereoProgressivaPesoExcedenteService tabelaFreteAereoProgressivaPesoExcedenteService) {
		this.tabelaFreteAereoProgressivaPesoExcedenteService = tabelaFreteAereoProgressivaPesoExcedenteService;
	}

	public TabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService getTabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService() {
		return tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService;
	}

	public void setTabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService(
			TabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService) {
		this.tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService = tabelaFreteAereoTaxaMinimaPesoExcedenteProgressivoService;
	}

	public TabelaFreteAereoProgressivoService getTabelaFreteAereoProgressivoService() {
		return tabelaFreteAereoProgressivoService;
	}

	public void setTabelaFreteAereoProgressivoService(TabelaFreteAereoProgressivoService tabelaFreteAereoProgressivoService) {
		this.tabelaFreteAereoProgressivoService = tabelaFreteAereoProgressivoService;
	}

	public TabelaFreteAereoPercentualService getTabelaFreteAereoPercentualService() {
		return tabelaFreteAereoPercentualService;
	}

	public void setTabelaFreteAereoPercentualService(TabelaFreteAereoPercentualService tabelaFreteAereoPercentualService) {
		this.tabelaFreteAereoPercentualService = tabelaFreteAereoPercentualService;
	}

	public TabelaFreteVolumeService getTabelaFreteVolumeService() {
		return tabelaFreteVolumeService;
	}

	public void setTabelaFreteVolumeService(TabelaFreteVolumeService tabelaFreteVolumeService) {
		this.tabelaFreteVolumeService = tabelaFreteVolumeService;
	}

	public TabelaFreteAereoVolumeService getTabelaFreteAereoVolumeService() {
		return tabelaFreteAereoVolumeService;
	}

	public void setTabelaFreteAereoVolumeService(TabelaFreteAereoVolumeService tabelaFreteAereoVolumeService) {
		this.tabelaFreteAereoVolumeService = tabelaFreteAereoVolumeService;
	}

	public TabelaFreteDiferenciadaService getTabelaFreteDiferenciadaService() {
		return tabelaFreteDiferenciadaService;
	}

	public void setTabelaFreteDiferenciadaService(TabelaFreteDiferenciadaService tabelaFreteDiferenciadaService) {
		this.tabelaFreteDiferenciadaService = tabelaFreteDiferenciadaService;
	}

	public TabelaFretePesoCargaCompletaTarifaService getTabelaFretePesoCargaCompletaTarifaService() {
		return tabelaFretePesoCargaCompletaTarifaService;
	}

	public void setTabelaFretePesoCargaCompletaTarifaService(TabelaFretePesoCargaCompletaTarifaService tabelaFretePesoCargaCompletaTarifaService) {
		this.tabelaFretePesoCargaCompletaTarifaService = tabelaFretePesoCargaCompletaTarifaService;
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public TipoTabelaPrecoService getTipotabelaPrecoService() {
		return tipotabelaPrecoService;
	}

	public EmitirTabelasClientesService getEmitirTabelasClienteService() {
		return emitirTabelasClienteService;
	}

	public void setTabelaFreteAereoEspecificaKiloExcedenteService(TabelaFreteAereoEspecificaKiloExcedenteService tabelaFreteAereoEspecificaKiloExcedenteService) {
		this.tabelaFreteAereoEspecificaKiloExcedenteService = tabelaFreteAereoEspecificaKiloExcedenteService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public TabelaFreteAereoConvencionalService getTabelaFreteAereoConvencionalService() {
		return tabelaFreteAereoConvencionalService;
	}

	public void setTabelaFreteAereoConvencionalService(TabelaFreteAereoConvencionalService tabelaFreteAereoConvencionalService) {
		this.tabelaFreteAereoConvencionalService = tabelaFreteAereoConvencionalService;
	}
}

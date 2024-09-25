package com.mercurio.lms.franqueados.report;


import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.SimulacaoDoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.SimulacaoReembarqueDoctoServicoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSintetitcoParticipacaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacao.jasper"
 */
public class SimulacaoRelatorioSinteticoParticipacaoResumoMesService extends RelatorioSinteticoParticipacaoService {

	private LancamentoFranqueadoService lancamentoFranqueadoService;
	private SimulacaoReembarqueDoctoServicoFranqueadoService reembarqueService;
	private SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService;

	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List listaComumOrdered = (List) (parameters.containsKey("defaultQuery")?parameters.get("defaultQuery"):Collections.EMPTY_LIST);

		
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(listaComumOrdered);
			}

			public Map getParameters() {
				return parameters;
			}

			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};

		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null)
			parametersReport.put("idFranquia", parameters.get("idFilial"));
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
			parametersReport.put("competencia", dtCompetencia);
		}
		
		
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null && parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			BigDecimal countBDM = getBDMs((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countIndenizacao = getIndenizacoes((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countRecalculo = getRecalculos((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countOver = getOver60((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countCreditos = getCreditosDiversos((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countDebitos = getDebitosDiversos((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countRodoviarioInternacional = getRodoviarioInternacional((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countReembarque = getServicosReembarque((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countServicosAdicionais = getServicosAdicionais((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			
			parametersReport.put("BDMS", countBDM);
			parametersReport.put("Indenizacoes", countIndenizacao);
			parametersReport.put("Recalculos", countRecalculo);
			parametersReport.put("Over60", countOver);
			parametersReport.put("CreditosDiversos", countCreditos);
			parametersReport.put("DebitosDiversos", countDebitos);
			parametersReport.put("RodoviarioInternacional", countRodoviarioInternacional);
			parametersReport.put("Reembarques", countReembarque);
			parametersReport.put("ServicosAdicionais", countServicosAdicionais);
		}
		jr.setParameters(parametersReport);
		return jr;
	}
    
    public Boolean hasCalcParticipacaoFreteRodoviarioNacional(String tpFrete, BigDecimal vlCustoAereo) {
    	if ( ! tpFrete.equals(ConstantesFranqueado.SERVICO_ADICIONAL) &&
    			! tpFrete.equals(ConstantesFranqueado.FRETE_LOCAL) &&
    			vlCustoAereo.compareTo(BigDecimal.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasCalcParticipacaoFreteAereo(BigDecimal vlCustoAereo) {
    	if (vlCustoAereo.compareTo(BigDecimal.ZERO) > 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasCalcParticipacaoFreteLocal(String tpFrete) {
    	if (tpFrete.equals(ConstantesFranqueado.FRETE_LOCAL)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasCalcServicosAdicionais(String tpFrete) {
    	if (tpFrete.equals(ConstantesFranqueado.SERVICO_ADICIONAL) ) {
    		return true;
    	}
    	
    	return false;
    }
    
    @SuppressWarnings("rawtypes")
    public BigDecimal getRodoviarioInternacional(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaTRIIREList = lancamentoFranqueadoService.getConsultaTRIIRE(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaTRIIREList.isEmpty() && consultaTRIIREList.get(0) != null) {
    		valor = (BigDecimal) consultaTRIIREList.get(0);
    	}
    	
    	return valor;
    }
    
    @SuppressWarnings("rawtypes")
    public BigDecimal getServicosReembarque(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaReembarqueList = reembarqueService.findValorTotalReembarque(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaReembarqueList.isEmpty() && consultaReembarqueList.get(0) != null) {
    		valor = (BigDecimal) consultaReembarqueList.get(0);
    	}
    	
    	return valor;
    }
    
    @SuppressWarnings("rawtypes")
    public BigDecimal getServicosAdicionais(YearMonthDay competencia, Long idFranquia) {
    	
    	List servicos = doctoServicoFranqueadoService.findServicosAdicionais(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! servicos.isEmpty() && servicos.get(0) != null) {
    		valor = (BigDecimal) servicos.get(0);
    	}
    	
    	return valor;
    }
    
    
    
    
    @SuppressWarnings("rawtypes")
    public BigDecimal getOver60(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaOver60List = lancamentoFranqueadoService.getConsultaOver60(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaOver60List.isEmpty() && consultaOver60List.get(0) != null) {
    		valor = (BigDecimal) consultaOver60List.get(0);
    	}
    	
    	return valor;
    }
    
    @SuppressWarnings("rawtypes")
    public BigDecimal getIndenizacoes(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaIndenizacoesList = lancamentoFranqueadoService.getConsultaIndenizacoes(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaIndenizacoesList.isEmpty() && consultaIndenizacoesList.get(0) != null) {
    		valor = (BigDecimal) consultaIndenizacoesList.get(0);
    	}
   
    	return valor;
    }

    @SuppressWarnings("rawtypes")
    public BigDecimal getRecalculos(YearMonthDay competencia, Long idFranquia) {
	
		List consultaRecalculosList = lancamentoFranqueadoService.getConsultaRecalculos(competencia, idFranquia);
		
		BigDecimal valor = new BigDecimal(0);
		if ( ! consultaRecalculosList.isEmpty() && consultaRecalculosList.get(0) != null) {
			valor = (BigDecimal) consultaRecalculosList.get(0);
		}
		
		return valor;
    }

    @SuppressWarnings("rawtypes")
    public BigDecimal getBDMs(YearMonthDay competencia, Long idFranquia) {
	
		List consultaBDMsList = lancamentoFranqueadoService.getConsultaBDMs(competencia, idFranquia);
		
		BigDecimal valor = new BigDecimal(0);
		if ( ! consultaBDMsList.isEmpty() && consultaBDMsList.get(0) != null) {
			valor = (BigDecimal) consultaBDMsList.get(0);
		}
		
		return valor;
    }

    @SuppressWarnings("rawtypes")
    public BigDecimal getCreditosDiversos(YearMonthDay competencia, Long idFranquia) {
	
		List consultaCreditosDiversosList = lancamentoFranqueadoService.getConsultaCreditosDiversos(competencia, idFranquia);
		
		BigDecimal valor = new BigDecimal(0);
		if ( ! consultaCreditosDiversosList.isEmpty() && consultaCreditosDiversosList.get(0) != null) {
			valor = (BigDecimal) consultaCreditosDiversosList.get(0);
		}
		
		return valor;
    }

    @SuppressWarnings("rawtypes")
	public BigDecimal getDebitosDiversos(YearMonthDay competencia, Long idFranquia) {
	
		List consultaDebitosDiversosList = lancamentoFranqueadoService.getConsultaDebitosDiversos(competencia, idFranquia);
		
		BigDecimal valor = new BigDecimal(0);
		if ( ! consultaDebitosDiversosList.isEmpty() && consultaDebitosDiversosList.get(0) != null) {
			valor = (BigDecimal) consultaDebitosDiversosList.get(0);
		}
		
		return valor;
    }
    
    public Boolean hasCalcCTe(String tpDocumentoServico) {
    	if (tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_CTR) ||
    			tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_CTE)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasCalcNFeFretes(String tpDocumentoServico) {
    	if (tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NFT) ||
    			tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NTE)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasCalcNFeServicos(String tpDocumentoServico) {
    	if (tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NFS) ||
    			tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NSE)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Boolean hasNotServicoAdicional(String tpFrete) {
    	if ( ! tpFrete.equals(ConstantesFranqueado.SERVICO_ADICIONAL)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}

	public void setReembarqueService(
			SimulacaoReembarqueDoctoServicoFranqueadoService reembarqueService) {
		this.reembarqueService = reembarqueService;
	}

	public void setDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
   
}

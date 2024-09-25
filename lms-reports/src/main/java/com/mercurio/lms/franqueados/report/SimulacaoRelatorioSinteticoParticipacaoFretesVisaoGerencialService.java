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
import com.mercurio.lms.franqueados.model.service.SimulacaoDoctoServicoFranqueadoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSintetitcoParticipacaoFretesVisaoGerencialService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoFretesVisaoGerencial.jasper"
 */
public class SimulacaoRelatorioSinteticoParticipacaoFretesVisaoGerencialService extends RelatorioSinteticoParticipacaoService {

	private SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService;
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List listaComumOrdered = (List) (parameters.containsKey("defaultQuery")?parameters.get("defaultQuery"):Collections.EMPTY_LIST);
		
		List listServicosAdicionais =doctoServicoFranqueadoService.findRelatorioSinteticoServicoAdicional(parameters);
		
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
		parametersReport.put("dataSourceServicosAdicionais", new JRBeanCollectionDataSource(listServicosAdicionais));
		
		jr.setParameters(parametersReport);
		return jr;
	}
    
    public Boolean hasCalcCIFExp(String tpFrete, BigDecimal vlCustoAereo) {
    	if (tpFrete.equals(ConstantesFranqueado.CIF_EXPEDIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countCIFExp(String tpFrete, BigDecimal vlCustoAereo) {
    	if (hasCalcCIFExp(tpFrete, vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcFOBRec(String tpFrete, BigDecimal vlCustoAereo) {
    	if (tpFrete.equals(ConstantesFranqueado.FOB_RECEBIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countFOBRec(String tpFrete, BigDecimal vlCustoAereo) {
    	if (hasCalcFOBRec(tpFrete, vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcCIFRec(String tpFrete, BigDecimal vlCustoAereo) {
    	if (tpFrete.equals(ConstantesFranqueado.CIF_RECEBIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countCIFRec(String tpFrete, BigDecimal vlCustoAereo) {
    	if (hasCalcCIFRec(tpFrete, vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcFOBExp(String tpFrete, BigDecimal vlCustoAereo) {
    	if (tpFrete.equals(ConstantesFranqueado.FOB_EXPEDIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countFOBExp(String tpFrete, BigDecimal vlCustoAereo) {
    	if (hasCalcFOBExp(tpFrete, vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcAereo(BigDecimal vlCustoAereo) {
    	if (vlCustoAereo.compareTo(BigDecimalUtils.ZERO) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countAereo(BigDecimal vlCustoAereo) {
    	if (hasCalcAereo(vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcLocal(String tpFrete, BigDecimal vlCustoAereo) {
    	if (tpFrete.equals(ConstantesFranqueado.FRETE_LOCAL) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countLocal(String tpFrete, BigDecimal vlCustoAereo) {
    	if (hasCalcLocal(tpFrete, vlCustoAereo)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcCIFExp(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (tpFrete.equals(ConstantesFranqueado.CIF_EXPEDIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0 &&
    			blLiqNaCompetencia.equals("S")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countCIFExp(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcCIFExp(tpFrete, vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcFOBRec(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (tpFrete.equals(ConstantesFranqueado.FOB_RECEBIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0 &&
    	    	blLiqNaCompetencia.equals("S")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countFOBRec(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcFOBRec(tpFrete, vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcCIFRec(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (tpFrete.equals(ConstantesFranqueado.CIF_RECEBIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0 &&
    	    	blLiqNaCompetencia.equals("S")) {
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
    
    public int countCIFRec(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcCIFRec(tpFrete, vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcFOBExp(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (tpFrete.equals(ConstantesFranqueado.FOB_EXPEDIDO) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0 &&
    	    	blLiqNaCompetencia.equals("S")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countFOBExp(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcFOBExp(tpFrete, vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcAereo(BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (vlCustoAereo.compareTo(BigDecimalUtils.ZERO) != 0 &&
    			blLiqNaCompetencia.equals("S")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countAereo(BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcAereo(vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasCalcLocal(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (tpFrete.equals(ConstantesFranqueado.FRETE_LOCAL) &&
    			vlCustoAereo.compareTo(BigDecimalUtils.ZERO) == 0 &&
    	    	blLiqNaCompetencia.equals("S")) {
    		return true;
    	}
    	
    	return false;
    }
    
    public int countLocal(String tpFrete, BigDecimal vlCustoAereo, String blLiqNaCompetencia) {
    	if (hasCalcLocal(tpFrete, vlCustoAereo, blLiqNaCompetencia)) {
    		return IntegerUtils.ONE;
    	}
    	
    	return IntegerUtils.ZERO;
    }
    
    public Boolean hasFreteLocal(String tpFrete) {
    	if (tpFrete.equals(ConstantesFranqueado.FRETE_LOCAL)) {
    		return true;
    	}
    	
    	return false;
    }
    
	public void setDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
   
}

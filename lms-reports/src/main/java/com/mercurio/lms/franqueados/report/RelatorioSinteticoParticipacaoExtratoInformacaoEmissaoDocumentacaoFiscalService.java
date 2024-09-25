package com.mercurio.lms.franqueados.report;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.LancamentoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.ReembarqueDoctoServicoFranqueadoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.vendas.emitirRelatorioSintetitcoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService"
 * @spring.property name="reportName" value="com/mercurio/lms/franqueados/report/emitirRelatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscal.jasper"
 */
public class RelatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService extends ReportServiceSupport {

	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private ReembarqueDoctoServicoFranqueadoService reembarqueDoctoServicoFranqueadoService;
	private LancamentoFranqueadoService lancamentoFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private PessoaService pessoaService;
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		final List documentos = doctoServicoFranqueadoService.findRelatorioSinteticoDocumentosFiscais(parameters);

		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(documentos);
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
		
		parametersReport.put("Cte", fixoFranqueadoService.getValorCte((Long)parameters.get("idFilial"), (YearMonthDay) parameters.get("competencia")));
		parametersReport.put("NomeEmpresa", pessoaService.findNomePessoaByCompetenciaIdFranquia((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial")));
		
		
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null && parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			BigDecimal countRodoviarioInternacional = getSumParticipacaoFretesInternacionais((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countReembarque = getSumServicosReembarque((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));
			BigDecimal countServicosAdicionais = getSumServicosAdicionais((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial"));

			parametersReport.put("RodoviarioInternacional", countRodoviarioInternacional);
			parametersReport.put("Reembarques", countReembarque);
			parametersReport.put("ServicosAdicionais", countServicosAdicionais);
		}
		
		jr.setParameters(parametersReport);
		return jr;
	}
    
    @SuppressWarnings("rawtypes")
	private String getFilterSummary(Map parameters){
    	SqlTemplate sql = createSqlTemplate();

    	if (parameters.containsKey("dsFranquia") && parameters.get("dsFranquia") != null) {
    		String dsFranquia = (String) parameters.get("dsFranquia");
    		sql.addFilterSummary("franquia", dsFranquia);
    	}
    	
    	if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
    		YearMonthDay dtCompetencia = (YearMonthDay) parameters.get("competencia");
    		
    		String competencia = dtCompetencia.toString(DateTimeFormat.forPattern("MM/yyyy"));

    		sql.addFilterSummary("competencia", competencia);
    	}
    	
    	return sql.getFilterSummary();
    }
    
    public static Boolean hasCalcMunicipioSedeMunicipioSede(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idMunicipioSede, Long idMunicipioColeta, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_FRETE_LOCAL) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) == 0 &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioSedeMunicipioAtendido(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idMunicipioSede, Long idMunicipioColeta, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_FRETE_LOCAL) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) == 0 &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioAtendidoMunicipioSede(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idMunicipioSede, Long idMunicipioColeta, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_FRETE_LOCAL) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) != 0 &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioAtendidoMunicipioAtendido(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idMunicipioSede, Long idMunicipioColeta, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_FRETE_LOCAL) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) != 0 &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioSedeDentroEstado(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeEntrega, Long idMunicipioSede, Long idMunicipioColeta) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_COLETA) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) == 0 &&
    			idUnidadeSede.compareTo(idUnidadeEntrega) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioSedeForaEstado(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeEntrega, Long idMunicipioSede, Long idMunicipioColeta) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_COLETA) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) == 0 &&
    			idUnidadeSede.compareTo(idUnidadeEntrega) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioAtendidoDentroEstado(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeEntrega, Long idMunicipioSede, Long idMunicipioColeta) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_COLETA) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) != 0 &&
    			idUnidadeSede.compareTo(idUnidadeEntrega) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcMunicipioAtendidoForaEstado(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeEntrega, Long idMunicipioSede, Long idMunicipioColeta) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_COLETA) &&
    			idMunicipioSede.compareTo(idMunicipioColeta) != 0 &&
    			idUnidadeSede.compareTo(idUnidadeEntrega) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcForaEstadoMunicipioSede(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeColeta, Long idMunicipioSede, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_ENTREGA) &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) == 0 &&
    			idUnidadeSede.compareTo(idUnidadeColeta) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcDentroEstadoMunicipioSede(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeColeta, Long idMunicipioSede, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_ENTREGA) &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) == 0 &&
    			idUnidadeSede.compareTo(idUnidadeColeta) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    
    public static Boolean hasCalcForaEstadoMunicipioAtendido(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeColeta, Long idMunicipioSede, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_ENTREGA) &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) != 0 &&
    			idUnidadeSede.compareTo(idUnidadeColeta) != 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcDentroEstadoMunicipioAtendido(String tpColetaEntregaRel, String tpDocumentoServico,
    		Long idUnidadeSede, Long idUnidadeColeta, Long idMunicipioSede, Long idMunicipioEntrega) {
    	if (tpColetaEntregaRel.equals(ConstantesFranqueado.TP_COLETA_ENTREGA_REL_ENTREGA) &&
    			idMunicipioSede.compareTo(idMunicipioEntrega) != 0 &&
				idUnidadeSede.compareTo(idUnidadeColeta) == 0) {
    		return true;
    	}
    	
    	return false;
    }
    
    @SuppressWarnings("rawtypes")
	public BigDecimal getSumServicosAdicionais(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaSomatorioServicosAdicionaisList = doctoServicoFranqueadoService.findServicosAdicionais(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaSomatorioServicosAdicionaisList.isEmpty() && consultaSomatorioServicosAdicionaisList.get(0) != null) {
    		valor = (BigDecimal) consultaSomatorioServicosAdicionaisList.get(0);
    	}
    	
    	return valor;
    }
    
    @SuppressWarnings("rawtypes")
	public BigDecimal getSumServicosReembarque(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaSomatorioServicosReembarqueList = reembarqueDoctoServicoFranqueadoService.findServicosReembarque(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaSomatorioServicosReembarqueList.isEmpty() && consultaSomatorioServicosReembarqueList.get(0) != null) {
    		valor = (BigDecimal) consultaSomatorioServicosReembarqueList.get(0);
    	}
    	
    	return valor;
    }
    
    @SuppressWarnings("rawtypes")
	public BigDecimal getSumParticipacaoFretesInternacionais(YearMonthDay competencia, Long idFranquia) {
    	
    	List consultaSomatorioParticipacaoFretesInternacionaisList = lancamentoFranqueadoService.getConsultaTRIIRE(competencia, idFranquia);
    	
    	BigDecimal valor = new BigDecimal(0);
    	if ( ! consultaSomatorioParticipacaoFretesInternacionaisList.isEmpty() && consultaSomatorioParticipacaoFretesInternacionaisList.get(0) != null) {
    		valor = (BigDecimal) consultaSomatorioParticipacaoFretesInternacionaisList.get(0);
    	}
    	
    	return valor;
    }
    
    public static Boolean hasCalcCTe(String tpDocumentoServico) {
    	if (tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_CTR) ||
    			tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_CTE)) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static Boolean hasCalcNotaFiscalServTransp(String tpDocumentoServico) {
    	if (tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NFT) ||
    			tpDocumentoServico.equals(ConstantesFranqueado.TP_DOCUMENTO_SERVICO_NTE)) {
    		return true;
    	}
    	
    	return false;
    }
    
	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}

	public void setReembarqueDoctoServicoFranqueadoService(
			ReembarqueDoctoServicoFranqueadoService reembarqueDoctoServicoFranqueadoService) {
		this.reembarqueDoctoServicoFranqueadoService = reembarqueDoctoServicoFranqueadoService;
	}

	public void setLancamentoFranqueadoService(
			LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}
	
	public void setFixoFranqueadoService(
			FixoFranqueadoService fixoFranqueadoService) {
		this.fixoFranqueadoService = fixoFranqueadoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}

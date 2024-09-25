package com.mercurio.lms.carregamento.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.carregamento.model.OcorrenciaDoctoFilial;
import com.mercurio.lms.carregamento.model.service.OcorrenciaDoctoFilialService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Classe responsável pela geração do Relatório
 * 
 * @spring.bean id="lms.carregamento.manterFilialLocalizacaoDoctoServicoService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/ocorrenciaDoctoFilial.jasper"
 */
public class ManterFilialLocalizacaoDoctoServicoService extends ReportServiceSupport {
	private OcorrenciaDoctoFilialService ocorrenciaDoctoFilialService;
	private FilialService filialService;
	private DoctoServicoService doctoServicoService;

	/**
     * Método responsável por gerar o relatório. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		YearMonthDay dataInicial	=  (YearMonthDay) parameters.get("periodoInicial");
		YearMonthDay dataFinal		=  (YearMonthDay) parameters.get("periodoFinal");
		Long idDoctoServico			=  (Long) parameters.get("idDoctoServico");
		Long idFilialOrigem			=  (Long) parameters.get("idFilialO");
		Long idFilialDestino		=  (Long) parameters.get("idFilialDestino");
		Long idFilialOcorrencia		=  (Long) parameters.get("idFilialOcorrencia");
		
		List<OcorrenciaDoctoFilial> registros = ocorrenciaDoctoFilialService.findOcorrenciasAlteracaoFilialLocalizacaoDoctoServico(dataInicial, dataFinal, idDoctoServico, idFilialOrigem, idFilialDestino, idFilialOcorrencia, null);
		
		if(registros.isEmpty())
			throw new BusinessException("LMS-05378");
		
		final List<OcorrenciaDoctoFilialDTO> lista = populate(registros);
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(lista);
			}

			public Map getParameters() {
				return parameters;
			}

			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};

		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", getFilterSummary(parameters));
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	private String getFilterSummary(Map param) {

		String filtro = "";
		
		if (param.get("periodoInicial") != null) {
			filtro = addSummary(filtro, "Período Inicial", JTFormatUtils.format((YearMonthDay)param.get("periodoInicial")));
		}
		if (param.get("periodoFinal") != null) {
			filtro = addSummary(filtro, "Período Final", JTFormatUtils.format((YearMonthDay)param.get("periodoFinal")));
		}
		
		if (param.get("idDoctoServico") != null) {
			DoctoServico doctoServico = doctoServicoService.findById((Long)param.get("idDoctoServico"));
			filtro = addSummary(filtro, "Documento de Serviço", doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + doctoServico.getNrDoctoServico());
		}
		
		if (param.get("idFilialO") != null) {
			Filial filial = filialService.findById((Long)param.get("idFilialO"));
			filtro = addSummary(filtro, "Filial Origem", filial.getSgFilial());
		}
		
		if (param.get("idFilialDestino") != null) {
			Filial filial = filialService.findById((Long)param.get("idFilialDestino"));
			filtro = addSummary(filtro, "Filial Destino", filial.getSgFilial());
		}
		
		if (param.get("idFilialOcorrencia") != null) {
			Filial filial = filialService.findById((Long)param.get("idFilialOcorrencia"));
			filtro = addSummary(filtro, "Filial Ocorrência", filial.getSgFilial());
		}
		
		return filtro;
	}
	
	private List<OcorrenciaDoctoFilialDTO> populate(List<OcorrenciaDoctoFilial> registros) {
		List<OcorrenciaDoctoFilialDTO> list = new ArrayList<OcorrenciaDoctoFilialDTO>();
		for (OcorrenciaDoctoFilial ocorrenciaDoctoFilial : registros) {
			
			OcorrenciaDoctoFilialDTO ocorrenciaDoctoFilialDTO = new OcorrenciaDoctoFilialDTO();
			
			ocorrenciaDoctoFilialDTO.setTP_DOCTO_SERVICO(ocorrenciaDoctoFilial.getDoctoServico().getTpDocumentoServico().getDescription().toString());
			ocorrenciaDoctoFilialDTO.setFILIAL_DOCTO_SERVICO(ocorrenciaDoctoFilial.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			ocorrenciaDoctoFilialDTO.setNR_DOCTO_SERVICO(ocorrenciaDoctoFilial.getDoctoServico().getNrDoctoServico().toString());
			ocorrenciaDoctoFilialDTO.setFILIAL_LOCALIZACAO(ocorrenciaDoctoFilial.getFilialDoctoLocalizacao().getSgFilial());
			ocorrenciaDoctoFilialDTO.setFILIAL_OCORRENCIA(ocorrenciaDoctoFilial.getFilialOcorrencia().getSgFilial());
			ocorrenciaDoctoFilialDTO.setFILIAL_DESTINO(ocorrenciaDoctoFilial.getFilialDoctoDestino().getSgFilial());
			ocorrenciaDoctoFilialDTO.setDT_OCORRENCIA(ocorrenciaDoctoFilial.getDhOcorrencia().toDate());
			ocorrenciaDoctoFilialDTO.setDS_LOGIN(ocorrenciaDoctoFilial.getUsuarioOcorrencia().getUsuarioADSM().getLogin());

			list.add(ocorrenciaDoctoFilialDTO);
		}
		return list;
	}
	
	private String addSummary(String filterSummary, String name, String value) {
		if (filterSummary.length() > 0) {
			filterSummary += "|  ";
		}
		filterSummary += name +": " + value +"  ";
		return filterSummary;
	}

	public OcorrenciaDoctoFilialService getOcorrenciaDoctoFilialService() {
		return ocorrenciaDoctoFilialService;
	}

	public void setOcorrenciaDoctoFilialService(
			OcorrenciaDoctoFilialService ocorrenciaDoctoFilialService) {
		this.ocorrenciaDoctoFilialService = ocorrenciaDoctoFilialService;
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


    public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
}
package com.mercurio.lms.sim.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.dao.RelatorioRiscoAgendamentoDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.util.AwbUtils;


public class RelatorioRiscoAgendamentoService {
	
	
	private final static Integer MAXIMO_REGISTROS_RELATORIO_CSV = 100000;
	private RelatorioRiscoAgendamentoDAO relatorioRiscoAgendamentoDAO;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainValueService domainValueService;
	
	private File generateReportFile(List<Map<String, Object>> listaParaCsv, File reportOutputDir){
		try {
			File reportFile =  File.createTempFile("report", ".csv", reportOutputDir);
			FileOutputStream out = new FileOutputStream (reportFile);
			if(listaParaCsv != null &&  !listaParaCsv.isEmpty()){
				String arquivoCsv = CsvUtils.convertMappedListToCsv(listaParaCsv, ";");
				out.write(arquivoCsv.getBytes());
			}
			out.flush();
			out.close();
			return reportFile;
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
	}
	
	public File executeExportacaoCsv(TypedFlatMap criteria, File reportOutputDir) {
		ResultSetPage rsp = relatorioRiscoAgendamentoDAO.findPaginatedRelatorioRiscoAgendamento(criteria, new FindDefinition(1,
							MAXIMO_REGISTROS_RELATORIO_CSV, Collections.emptyList()));
			List<Map<String, Object>> listaParaCsv = populateReportCsvMap(rsp.getList());
			return generateReportFile(listaParaCsv, reportOutputDir);	
	}
	
	
	private List<Map<String, Object>> populateReportCsvMap(List<Object[]> rs){
		List<Map<String, Object>> listaParaCsv = new ArrayList<Map<String, Object>>();
		for (Object[] obj : rs) {
			Map<String, Object> registro = new LinkedHashMap<String, Object>();
			
			registro.put(configuracoesFacade.getMensagem("filialDestino"), obj[relatorioRiscoAgendamentoDAO.FILIAL_DESTINO]);
		
			if(obj[relatorioRiscoAgendamentoDAO.NF] != null){
				registro.put(configuracoesFacade.getMensagem("notaFiscal"), FormatUtils.completaDados( obj[relatorioRiscoAgendamentoDAO.NF], "0", 8, 0, true));
			} else {
				registro.put(configuracoesFacade.getMensagem("notaFiscal"), "");
			}
			
			registro.put(configuracoesFacade.getMensagem("modal"),  obj[relatorioRiscoAgendamentoDAO.MODAL]);
		
			
			registro.put(configuracoesFacade.getMensagem("dataEmissao"), ((YearMonthDay) obj[relatorioRiscoAgendamentoDAO.EMISSAO_DOCTO_SERV]).toString("dd/MM/yyyy"));
		
			
			if(obj[relatorioRiscoAgendamentoDAO.NR_DOCTO_SERV] != null){
				registro.put(configuracoesFacade.getMensagem("documentoServico"), domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",
						obj[relatorioRiscoAgendamentoDAO.TP_DOCTO_SERV].toString()) + " " + obj[relatorioRiscoAgendamentoDAO.FILIAL_ORIGEM] + " " 
								+ FormatUtils.completaDados( obj[relatorioRiscoAgendamentoDAO.NR_DOCTO_SERV], "0", 8, 0, true));
			} else {
				registro.put(configuracoesFacade.getMensagem("documentoServico"), "");
			}
			
			if(obj[relatorioRiscoAgendamentoDAO.CIA_AEREA] != null){
				registro.put(configuracoesFacade.getMensagem("ciaAerea"), obj[relatorioRiscoAgendamentoDAO.CIA_AEREA]);
			} else {
				registro.put(configuracoesFacade.getMensagem("ciaAerea"), "");
			}
			
			String serieNroAwb = "";
			if( obj[relatorioRiscoAgendamentoDAO.DS_SERIE] != null){
				serieNroAwb += (String) obj[relatorioRiscoAgendamentoDAO.DS_SERIE] + " / ";
			}
				serieNroAwb +=AwbUtils.formatNrAwb((Long) obj[relatorioRiscoAgendamentoDAO.NR_AWB], (Integer) obj[relatorioRiscoAgendamentoDAO.DV_AWB]);
				
			registro.put(configuracoesFacade.getMensagem("awb"), serieNroAwb);
			
			registro.put(configuracoesFacade.getMensagem("remetente"), obj[relatorioRiscoAgendamentoDAO.REMENTENTE]);
			
			registro.put(configuracoesFacade.getMensagem("destinatario"), obj[relatorioRiscoAgendamentoDAO.DESTINATARIO]);
			
			registro.put(configuracoesFacade.getMensagem("cidade"), obj[relatorioRiscoAgendamentoDAO.MUNICIPIO]);
			
			registro.put(configuracoesFacade.getMensagem("uf"), obj[relatorioRiscoAgendamentoDAO.UF]);
			
			
			if(obj[relatorioRiscoAgendamentoDAO.SITUACAO] != null){
				registro.put(configuracoesFacade.getMensagem("situacao"), obj[relatorioRiscoAgendamentoDAO.SITUACAO]);
			}else{
				registro.put(configuracoesFacade.getMensagem("situacao"), "");
			}	
			
			registro.put(configuracoesFacade.getMensagem("dpe"), ((YearMonthDay)obj[relatorioRiscoAgendamentoDAO.DPE]).toString("dd/MM/yyyy"));
			
			registro.put(configuracoesFacade.getMensagem("localizacao"), obj[relatorioRiscoAgendamentoDAO.LOCALIZACAO]);
			
			listaParaCsv.add(registro);
		}
		
		return listaParaCsv;
	}
	
	
	public RelatorioRiscoAgendamentoDAO getRelatorioRiscoAgendamentoDAO() {
		return relatorioRiscoAgendamentoDAO;
	}
	public void setRelatorioRiscoAgendamentoDAO(RelatorioRiscoAgendamentoDAO relatorioRiscoAgendamentoDAO) {
		this.relatorioRiscoAgendamentoDAO = relatorioRiscoAgendamentoDAO;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}

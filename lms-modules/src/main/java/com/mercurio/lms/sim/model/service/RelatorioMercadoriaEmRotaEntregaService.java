package com.mercurio.lms.sim.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.dao.MonitoramentoNotasFiscaisCCTDAO;
import com.mercurio.lms.sim.model.dao.RelatorioMercadoriaEmRotaEntregaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;

public class RelatorioMercadoriaEmRotaEntregaService {

	private RelatorioMercadoriaEmRotaEntregaDAO relatorioMercadoriaEmRotaEntregaDAO;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainValueService domainValueService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private MonitoramentoNotasFiscaisCCTDAO monitoramentoNotasFiscaisCCTDAO;
	
	public File executeExportacaoCsv(TypedFlatMap criteria, File reportOutputDir) {
		ResultSetPage rs = relatorioMercadoriaEmRotaEntregaDAO.findPaginatedRelatorioMercadoriaEmRota(criteria);
		List<Map<String, Object>> listaFull = findDadosAgendamento(rs.getList());
		List<Map<String, Object>> listaParaCsv = populateReportCsvMap(listaFull);
		return generateReportFile(listaParaCsv, reportOutputDir);
	}

	/**
	 * Monta as colunas do csv Relatorio de Mercadoria em Rota
	 * A ordem de inclusão na lista é a ordem de apresentação no relatório.
	 * @param lista
	 * @return listaParaCsv
	 */
	
	private List<Map<String, Object>> populateReportCsvMap(List<Map<String, Object>> lista) {
		List<Map<String, Object>> listaParaCsv = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> obj : lista) {
			Map<String, Object> registro = new LinkedHashMap<String, Object>();
			
			registro.put(configuracoesFacade.getMensagem("filialDestino"), obj.get("filialDestino"));
			registro.put(configuracoesFacade.getMensagem("notaFiscal"), obj.get("notaFiscal"));
			registro.put(configuracoesFacade.getMensagem("dhEmissaoManifesto"), ((YearMonthDay) obj.get("dhEmissaoManifesto")).toString("dd/MM/yyyy"));
			
			registro.put(configuracoesFacade.getMensagem("documentoServico"), domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",
					obj.get("tpDoctoServico").toString()) + " " + obj.get("filialOrigem") + " " + FormatUtils.completaDados( obj.get("nrDoctoServico"), "0", 8, 0, true));
			
			registro.put(configuracoesFacade.getMensagem("serieNumAWBCiaAerea"), obj.get("nrAwb"));
			
			registro.put(configuracoesFacade.getMensagem("ciaAerea"), obj.get("ciaAerea"));
			registro.put(configuracoesFacade.getMensagem("modal"), obj.get("modal"));
			registro.put(configuracoesFacade.getMensagem("situacao"), obj.get("situacao"));
			registro.put(configuracoesFacade.getMensagem("remetente"), obj.get("nmClienteRemetente"));
			registro.put(configuracoesFacade.getMensagem("destinatario"), obj.get("nmClienteDestinatario"));
			registro.put(configuracoesFacade.getMensagem("cidade"), obj.get("nmMunicipio"));
			registro.put(configuracoesFacade.getMensagem("uf"), obj.get("sgUf"));
			registro.put(configuracoesFacade.getMensagem("localizacao"), obj.get("localizacao"));
			registro.put(configuracoesFacade.getMensagem("dpe"), obj.get("dpe"));
			registro.put(configuracoesFacade.getMensagem("dataEntrega"), obj.get("dtEntrega"));
			registro.put(configuracoesFacade.getMensagem("dataAgendamento"), obj.get("dtAgendamento"));
			registro.put(configuracoesFacade.getMensagem("dadosEntrega"), obj.get("obAgendamentoEntrega"));
			listaParaCsv.add(registro);
		}
		
		return listaParaCsv;
	}

	private File generateReportFile(List<Map<String, Object>> listaParaCsv, File reportOutputDir){
		try {
			File reportFile =  File.createTempFile("report", ".csv", reportOutputDir);
			FileOutputStream out = new FileOutputStream (reportFile);
			if(listaParaCsv != null && !listaParaCsv.isEmpty()){
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
	
	public List<Map<String, Object>> findDadosAgendamento(List<Object[]> lista) {
		List<Map<String, Object>> listaFull = new ArrayList<Map<String, Object>>();
		for(Object[] obj : lista){
			Long idDoctoServico = (Long) obj[0];
			
			Map<String, Object> objFull = convertArrayObjectToMap(obj);
			Map<String, Object> mapaAgendamentoMonitoramento = relatorioMercadoriaEmRotaEntregaDAO.findDtObAgendamentoByMonitoramentos(idDoctoServico);
			if(mapaAgendamentoMonitoramento.containsKey("dtAgendamento")){
				objFull.put("dtAgendamento", mapaAgendamentoMonitoramento.get("dtAgendamento"));
				objFull.put("obAgendamentoEntrega", mapaAgendamentoMonitoramento.get("obAgendamentoEntrega"));
			}else{
				Map<String, Object> mapaAgendamentoDoctoServico = relatorioMercadoriaEmRotaEntregaDAO.findDtObAgendamentoByDocumentoServico(idDoctoServico);
				if(mapaAgendamentoDoctoServico.containsKey("dtAgendamento")){
					objFull.put("dtAgendamento", mapaAgendamentoDoctoServico.get("dtAgendamento"));
					objFull.put("obAgendamentoEntrega", mapaAgendamentoDoctoServico.get("obAgendamentoEntrega"));
				}else{
					objFull.put("dtAgendamento", "");
					objFull.put("obAgendamentoEntrega", "");
				}
			}
			
			List<EventoDocumentoServico> eventosDoctoServico = eventoDocumentoServicoService.findEventoDoctoServico(idDoctoServico, (short) 21);
			if(!eventosDoctoServico.isEmpty() && eventosDoctoServico.get(0) != null){
				objFull.put("dtEntrega", JTFormatUtils.format(eventosDoctoServico.get(0).getDhEvento(), "dd/MM/yyyy"));
			}
			
			if(objFull.get("situacaoSigla") != null){
				objFull.put("situacao", monitoramentoNotasFiscaisCCTDAO.findDsTpSituacaoNfCctByVlSituacao((String) objFull.get("situacaoSigla")));
			}
			listaFull.add(objFull);
		}
		return listaFull;
	}

	private Map<String, Object> convertArrayObjectToMap(Object[] obj) {
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("filialDestino", obj[2]);
		mapa.put("notaFiscal", obj[3]);
		mapa.put("dhEmissaoManifesto", obj[4]);
		mapa.put("modal", obj[5]);
		mapa.put("tpDoctoServico", obj[6]);
		mapa.put("filialOrigem", obj[7]);
		mapa.put("nrDoctoServico", obj[8]);
		mapa.put("ciaAerea", obj[9]);
		mapa.put("nrAwb", obj[10]);
		mapa.put("nmClienteRemetente", obj[11]);
		mapa.put("nmClienteDestinatario", obj[12]);
		mapa.put("nmMunicipio", obj[13]);
		mapa.put("sgUf", obj[14]);
		mapa.put("situacaoSigla", obj[15]);
		mapa.put("dpe", obj[16]);
		mapa.put("localizacao", obj[17]);
		return mapa;
	}

	public RelatorioMercadoriaEmRotaEntregaDAO getRelatorioMercadoriaEmRotaEntregaDAO() {
		return relatorioMercadoriaEmRotaEntregaDAO;
	}

	public void setRelatorioMercadoriaEmRotaEntregaDAO(
			RelatorioMercadoriaEmRotaEntregaDAO relatorioMercadoriaEmRotaEntregaDAO) {
		this.relatorioMercadoriaEmRotaEntregaDAO = relatorioMercadoriaEmRotaEntregaDAO;
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

	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public MonitoramentoNotasFiscaisCCTDAO getMonitoramentoNotasFiscaisCCTDAO() {
		return monitoramentoNotasFiscaisCCTDAO;
	}

	public void setMonitoramentoNotasFiscaisCCTDAO(
			MonitoramentoNotasFiscaisCCTDAO monitoramentoNotasFiscaisCCTDAO) {
		this.monitoramentoNotasFiscaisCCTDAO = monitoramentoNotasFiscaisCCTDAO;
	}

}

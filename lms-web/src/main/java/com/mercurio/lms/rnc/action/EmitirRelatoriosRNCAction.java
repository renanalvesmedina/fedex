package com.mercurio.lms.rnc.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.util.NFEUtils;
import com.mercurio.lms.rnc.model.service.CausaNaoConformidadeService;
import com.mercurio.lms.rnc.report.RelatorioNaoConformidadeService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.emitirRelatoriosRNCAction"
 */

public class EmitirRelatoriosRNCAction {
    
    private CausaNaoConformidadeService causaNaoConformidadeService;
    private DomainValueService domainValueService;
    private ReportExecutionManager reportExecutionManager;
    private RelatorioNaoConformidadeService relatorioNaoConformidadeService;
    private NotaFiscalEletronicaService notaFiscalEletronicaService;
    private ConfiguracoesFacade configuracoesFacade;


    public CausaNaoConformidadeService getCausaNaoConformidadeService() {
        return causaNaoConformidadeService;
    }

    public void setCausaNaoConformidadeService(CausaNaoConformidadeService causaNaoConformidadeService) {
        this.causaNaoConformidadeService = causaNaoConformidadeService;
    }    
    
    public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
     * Seta a service responsavel pela gera��o da consulta no relatorio 
     * @param defaultService
     */
    public void setRelatorioNaoConformidadeService(RelatorioNaoConformidadeService defaultService) {
        this.relatorioNaoConformidadeService = defaultService;
    }

    public String execute(TypedFlatMap map) throws Exception {
    	return this.reportExecutionManager.generateReportLocator(relatorioNaoConformidadeService, map);
    }
    
    /**
	 * Retorna a data atual.
	 * @return
	 */
	public TypedFlatMap getDataAtual() {
		TypedFlatMap tfm = new TypedFlatMap();
		
		tfm.put("dataAtual", JTDateTimeUtils.getDataAtual());
						
		return tfm;
	}
    
    /**
     * M�todo que busca da sess�o a moeda do usuario.
     *  
     */
    public TypedFlatMap findMoedaSessao(Map map) {
        TypedFlatMap tfm = new TypedFlatMap();
        
        tfm.put("idMoeda", SessionUtils.getMoedaSessao().getIdMoeda());
        tfm.put("siglaSimbolo", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        
        return tfm;
    }
    
    public List findCausaNaoConformidadeOrdenado(Map map) {
        List l = new ArrayList();
        l.add("dsCausaNaoConformidade");
        return getCausaNaoConformidadeService().findListByCriteria(map,l);
    }
   
	/**
	 * Busca os tipos de documento servi�o.
	 * @param criteria
	 * @return
	 */
    public List findTipoDocumentoServico(TypedFlatMap criteria) {
    	
    	List dominiosValidos = Arrays.asList("CTR", "CRT", "NFT", "MDA", "CTE", "NTE");
    	
    	List retorno = this.getDomainValueService().findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
    }

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
	
	public File executeExportacaoCsv(TypedFlatMap criteria, File reportOutputDir) {
		List<Map<String, Object>> lista = relatorioNaoConformidadeService.findDadosRalatorioRNCByItem(criteria);
		List<Map<String, Object>> listaFull = findDadosXML(lista);
		List<Map<String, Object>> listaParaCsv = populateReportCsvMap(listaFull);
		return generateReportFile(listaParaCsv, reportOutputDir);
}
	
	private List<Map<String, Object>> populateReportCsvMap(List<Map<String, Object>> lista) {
		List<Map<String, Object>> listaParaCsv = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> obj : lista) {
			Map<String, Object> registro = new LinkedHashMap<String, Object>();
			
			registro.put(configuracoesFacade.getMensagem("naoConformidade"), obj.get("filialNaoConformidade") + " " + obj.get("nrNC"));
			registro.put(configuracoesFacade.getMensagem("documentoServico"), domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",
					obj.get("tpDocumento").toString()) + " " + obj.get("filialOrigem") + " " + FormatUtils.completaDados( obj.get("nrDoctoServico"), "0", 8, 0, true));
			
			registro.put(configuracoesFacade.getMensagem("statusNaoConformidade"), obj.get("statusNC"));
			registro.put(configuracoesFacade.getMensagem("motivoNaoConformidade"), obj.get("motivoAbertura"));
			registro.put(configuracoesFacade.getMensagem("motivoDisposicao"), obj.get("motivoDisposicao"));
			registro.put(configuracoesFacade.getMensagem("filialEmitente"), obj.get("filialEmitente"));
			registro.put(configuracoesFacade.getMensagem("filialResponsavel"), obj.get("filialResponsavel"));
			registro.put(configuracoesFacade.getMensagem("filialDestino"), obj.get("filialDestino"));
			registro.put(configuracoesFacade.getMensagem("dataHoraAbertura"), obj.get("dhNC"));
			registro.put(configuracoesFacade.getMensagem("quantidadeVolumes2"), obj.get("qtdVolumes"));
			registro.put(configuracoesFacade.getMensagem("status"), obj.get("statusOcorrenciaNC"));
			registro.put(configuracoesFacade.getMensagem("notaFiscal"), obj.get("nrChave") != null ? NFEUtils.getNumeroNotaFiscalByChave(obj.get("nrChave").toString()) : "");
			registro.put(configuracoesFacade.getMensagem("remetente"), obj.get("nmRemetente"));
			registro.put(configuracoesFacade.getMensagem("destinatario"), obj.get("nmDestinatario"));
			registro.put(configuracoesFacade.getMensagem("serieNumAWBCiaAerea"), obj.get("nrAwb"));
			registro.put(configuracoesFacade.getMensagem("ciaAerea"), obj.get("ciaAerea"));
			registro.put(configuracoesFacade.getMensagem("causaNaoConformidade"), obj.get("causaNCI") + (obj.get("causaNC") != null ? " - " + obj.get("causaNC"): "" ));
			registro.put(configuracoesFacade.getMensagem("descricaoNaoConformidade"), obj.get("descricaoNCI") + (obj.get("descricaoOcorrenciaNC") != null ? " " + obj.get("descricaoOcorrenciaNC").toString().replace("\n"," ") :  ""));
			registro.put(configuracoesFacade.getMensagem("item"), obj.get("itemOcorrenciaNC"));
			registro.put(configuracoesFacade.getMensagem("descricaoItem"), obj.get("dsItem"));
			registro.put(configuracoesFacade.getMensagem("qtdOriginal"), obj.get("qtdItem")!= null ? FormatUtils.formatBigDecimalWithPattern((BigDecimal) obj.get("qtdItem"), "#,##0.000"): "");
			registro.put(configuracoesFacade.getMensagem("qtdNaoConformidade"), obj.get("qtdOcorrenciaNC") != null ?  FormatUtils.formatBigDecimalWithPattern((BigDecimal) obj.get("qtdOcorrenciaNC"), "#,##0.000"): "");
			registro.put(configuracoesFacade.getMensagem("vlTotalItem"), obj.get("vlTotalItem") != null ? FormatUtils.formatBigDecimalWithPattern((BigDecimal) obj.get("vlTotalItem"), "#,##0.00") : "");
			registro.put(configuracoesFacade.getMensagem("vlNaoConformidade"), obj.get("vlOcorrenciaNC") != null ? FormatUtils.formatBigDecimalWithPattern((BigDecimal) obj.get("vlOcorrenciaNC"), "#,##0.00") : "");
		
			listaParaCsv.add(registro);
		}
		
		return listaParaCsv;
	}

	private List<Map<String, Object>> findDadosXML(List<Map<String, Object>> lista) {
		for(Map<String, Object> map: lista){
			if(map.get("nrChave") != null){
				map = populaDadosItem(map);
			}
		}
		return lista;
	}

	private Map<String, Object> populaDadosItem(Map<String, Object> map) {
		BigDecimal itemOcorrencia = (BigDecimal) map.get("itemOcorrenciaNC");
		List<NotaFiscalEdiItem> itens = notaFiscalEletronicaService.findNfeItensByNrChave(map.get("nrChave").toString());
		for(int x=0; x < itens.size(); x++){
			if(itemOcorrencia != null && itens.get(x).getNumeroItem() != null 
					&& itemOcorrencia.toString().equals(itens.get(x).getNumeroItem().toString())){
					map.put("qtdItem", itens.get(x).getQtdeItem());
					map.put("vlTotalItem", itens.get(x).getVlTotalItem());
					map.put("dsItem", itens.get(x).getDescricaoItem());
			}
		}
		return map;
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

	public File executeCsvReport(TypedFlatMap criteria) throws Exception {
	   	return executeExportacaoCsv(criteria, reportExecutionManager.generateOutputDir());
	}

	public NotaFiscalEletronicaService getNotaFiscalEletronicaService() {
		return notaFiscalEletronicaService;
	}

	public void setNotaFiscalEletronicaService(
			NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}

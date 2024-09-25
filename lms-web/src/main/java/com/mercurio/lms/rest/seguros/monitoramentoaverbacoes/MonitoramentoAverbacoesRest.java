package com.mercurio.lms.rest.seguros.monitoramentoaverbacoes;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto.MonitoramentoAverbacaoDTO;
import com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto.MonitoramentoAverbacaoFilterDTO;
import com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.helper.MonitoramentoAverbacoesRestHelper;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoMdfeService;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path("/seguros/monitoramentoAverbacoes")
public class MonitoramentoAverbacoesRest extends LmsBaseCrudReportRest<MonitoramentoAverbacaoDTO, MonitoramentoAverbacaoDTO, MonitoramentoAverbacaoFilterDTO> {

    private static final String DM_TIPO_MON_AVERBACAO = "tipoMonAverbacao";


	@InjectInJersey
    private AverbacaoDoctoServicoService averbacaoDoctoServicoService;
    
    @InjectInJersey
    private AverbacaoDoctoServicoMdfeService averbacaoDoctoServicoMdfeService;

    @Override
    protected List<Map<String, String>> getColumns() {
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        listMap.add(getColumn("tipo", "tipo"));
        listMap.add(getColumn("documentoServico","documentoServico"));
        listMap.add(getColumn("cliente", "cliente"));
        listMap.add(getColumn("situacaoDocumento", "situacaoDocumento"));
        listMap.add(getColumn("dtAverbacao", "dtAverbacao"));
        listMap.add(getColumn("nrAverbacao", "nrAverbacao"));
        listMap.add(getColumn("averbado", "averbado"));
        listMap.add(getColumn("retornoAverbacao", "retornoAverbacao"));
        listMap.add(getColumn("observacaoSeFaz", "observacaoSeFaz"));
        listMap.add(getColumn("modal", "modal"));
        listMap.add(getColumn("operacaoSpitFire", "operacaoSpitFire"));
        
        return listMap;
    }

    @Override
    protected List<Map<String, Object>> findDataForReport(MonitoramentoAverbacaoFilterDTO filterDTO) {
        Map<String, Object> criteria = MonitoramentoAverbacoesRestHelper.toFilterMap(filterDTO);
        
        List<Map<String, Object>> reportData = new ArrayList<Map<String,Object>>();
        if(tipoSelecionadoCteOuTodos(filterDTO.getTipoMonitoriamentoAverbacao())){
        	List<MonitoramentoAverbacaoDTO> dtoList = mountDtoListDoctoServico(averbacaoDoctoServicoService.findMonitoramentoAverbacoesBySql(criteria));
        	reportData.addAll(MonitoramentoAverbacoesRestHelper.toListMap(dtoList));
        }
        if(tipoSelecionadoMdfeOuTodos(filterDTO.getTipoMonitoriamentoAverbacao())){
        	List<MonitoramentoAverbacaoDTO> dtoList = mountDtoListMdfe(averbacaoDoctoServicoMdfeService.findMonitoramentoAverbacoesBySql(criteria));
            reportData.addAll(MonitoramentoAverbacoesRestHelper.toListMap(dtoList));
        }
        return reportData;
    }

    @Override
    protected MonitoramentoAverbacaoDTO findById(Long aLong) {
        return null;
    }

    @Override
    protected Long store(MonitoramentoAverbacaoDTO monitoramentoAverbacaoDTO) {
        return null;
    }

    @Override
    protected void removeById(Long aLong) {
    }

    @Override
    protected void removeByIds(List<Long> list) {
    }

    @Override
    protected List<MonitoramentoAverbacaoDTO> find(MonitoramentoAverbacaoFilterDTO filterDTO) {
        Map<String, Object> criteria = MonitoramentoAverbacoesRestHelper.toFilterMap(filterDTO);
        DomainValue tipoMonAverbacao = (DomainValue) criteria.get(DM_TIPO_MON_AVERBACAO);
        
        List<MonitoramentoAverbacaoDTO> dtoList = new ArrayList<MonitoramentoAverbacaoDTO>();
        
        if(tipoSelecionadoCteOuTodos(tipoMonAverbacao)){
            List<Map<String, Object>> mapMonitoramentosDsList = averbacaoDoctoServicoService.findMonitoramentoAverbacoesBySql(criteria);
            dtoList.addAll(mountDtoListDoctoServico(mapMonitoramentosDsList));
        }
        
        if(tipoSelecionadoMdfeOuTodos(tipoMonAverbacao)){
            List<Map<String, Object>> mapMonitoramentosMdfeList = averbacaoDoctoServicoMdfeService.findMonitoramentoAverbacoesBySql(criteria);
            dtoList.addAll(mountDtoListMdfe(mapMonitoramentosMdfeList));
        }
        
        return dtoList;
    }

    private List<MonitoramentoAverbacaoDTO> mountDtoListDoctoServico(
			List<Map<String, Object>> mapMonitoramentosDsList) {
		
    	List<MonitoramentoAverbacaoDTO> dtoList = new ArrayList<MonitoramentoAverbacaoDTO>();
    	
    	for (Map<String, Object> map : mapMonitoramentosDsList) {
    		MonitoramentoAverbacaoDTO dto = new MonitoramentoAverbacaoDTO();
        	
    		dto.setIdAverbacaoDoctoServico((Long) map.get("idAverbacaoDoctoServico"));
            dto.setTpDoctoServico((String)map.get("tpDocumentoServico"));
            dto.setSgFilial((String)map.get("sgFilial"));
            dto.setNrDoctoServico((String)map.get("nrDoctoServico"));
            dto.setNmCliente((String)map.get("nmCliente"));
            dto.setTpSituacaoDocumento((String)map.get("tpSituacaoDocumento"));
            dto.setNrAverbacao((String)map.get("nrAverbacao"));
            dto.setDsRetorno((String)map.get("dsRetorno"));
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            dto.setDtAverbacao(df.format((Date)map.get("dhEnvio")));
            dto.setProtocolo((String)map.get("nrProtocolo"));
            String averbado = (String)map.get("blAverbado");
            dto.setAverbado("S".equalsIgnoreCase(averbado));
            dto.setTipoMonitoramentoAverbacao("CT-e");
            dto.setTpDocumentoServico((String)map.get("tpDocumentoServico"));
            
            String modal = "Rodoviário";
    		String tpModal = (String)map.get("tpModal");
    		if ("A".equals(tpModal)){
    			modal = "Aéreo";
    		}
    		dto.setModal(modal);
    		dto.setDsObservacaoSefaz((String)map.get("dsObservacaoSefaz"));
            dto.setBlOperacaoSpitFire((String) map.get(("blOperacaoSpitFire")));
        
    		dtoList.add(dto);
		}
    	
		return dtoList;
	}

	private List<MonitoramentoAverbacaoDTO> mountDtoListMdfe(
			List<Map<String, Object>> mapMonitoramentosMdfeList) {

    	
    	List<MonitoramentoAverbacaoDTO> monitoramentos = new ArrayList<MonitoramentoAverbacaoDTO>();
    	for (Map<String, Object> map : mapMonitoramentosMdfeList) {
			MonitoramentoAverbacaoDTO dto = new MonitoramentoAverbacaoDTO();
			
			dto.setIdAverbacaoDoctoServico((Long) map.get("idAverbacaoDoctoServico"));
			dto.setTpSituacaoDocumento((String) map.get("tpSituacaoDocumento"));
			dto.setNrAverbacao((String)map.get("nrProtocolo"));
			
			Boolean blAutorizado = "S".equalsIgnoreCase((String) map.get("blAutorizado"));
			Boolean blEncerrado = "S".equalsIgnoreCase((String) map.get("blEncerrado"));
			
			dto.setAverbado(blAutorizado|| blEncerrado);
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
			String tpWebService = (String)map.get("tpWebservice");
	        if("E".equals(tpWebService)){
	        	dto.setDsRetorno("Autorizado");
	        	dto.setDtAverbacao(df.format((Date)map.get("dhEnvioAutorizado")));
	        } else if("C".equals(tpWebService)){
	            dto.setDsRetorno("Cancelado");
	            dto.setDtAverbacao(df.format((Date)map.get("dhEnvioCancelado")));
	        } else if ("G".equals(tpWebService)){
	            dto.setDsRetorno("Encerramento");
	            dto.setDtAverbacao(df.format((Date)map.get("dhEnvioEncerrado")));
	        } else {
	            dto.setDsRetorno(tpWebService);
	        }
			
	        String modal = "Rodoviário";
			String tpModal = (String)map.get("tpModalManifesto");
			if ("A".equals(tpModal)){
				modal = "Aéreo";
			}
	        dto.setModal(modal);
	        
	        dto.setTipoMonitoramentoAverbacao("MDF-e");
	        dto.setDsObservacaoSefaz((String)map.get("dsObservacaoSefaz"));
            dto.setBlOperacaoSpitFire((String) map.get(("blOperacaoSpitFire")));
	    
	        
	        monitoramentos.add(dto);
		}
    	
		return monitoramentos;
	}

    private static boolean tipoSelecionadoMdfeOuTodos(DomainValue tipoMonAverbacao) {
        return tipoMonAverbacao == null || tipoMonAverbacao.equals(new DomainValue("MDFE"));
    }

    private static boolean tipoSelecionadoCteOuTodos(DomainValue tipoMonAverbacao) {
        return tipoMonAverbacao == null || tipoMonAverbacao.equals(new DomainValue("CTE"));
    }

    @Override
    protected Integer count(MonitoramentoAverbacaoFilterDTO filterDTO) {
    	return 0;
    }
    
    @POST
    @Path("reenviar")
    public Response reenviar(List<Long> idsMonitoramentoAverbacao) {
        averbacaoDoctoServicoService.reenviar(idsMonitoramentoAverbacao);
        return Response.ok().build();
    }

    @Override
    @POST
    @Path("reportCsv")
    public Response reportCsvRest(MonitoramentoAverbacaoFilterDTO filter) {
        if (!isValidLimit(filter, ROW_LIMIT, count(filter))) {
            return getException();
        }

        return createFile(findDataForReport(filter));
    }
    
}
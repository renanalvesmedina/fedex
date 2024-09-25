package com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.helper;

import com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto.MonitoramentoAverbacaoDTO;
import com.mercurio.lms.rest.seguros.monitoramentoaverbacoes.dto.MonitoramentoAverbacaoFilterDTO;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServicoMdfe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitoramentoAverbacoesRestHelper {

    private static final String SIM = "S";
    private static final String TP_DESTINO = "ATEM";

    public static Map<String, Object> toFilterMap(MonitoramentoAverbacaoFilterDTO filterDTO) {
        Map<String, Object> criteria = new HashMap<String, Object>();

        if (filterDTO.getFilial() != null) {
            criteria.put("idFilial", filterDTO.getFilial().getIdFilial());
        }

        if (filterDTO.getCliente() != null) {
            criteria.put("idCliente", filterDTO.getCliente().getIdCliente());
        }

        if (filterDTO.getDtAverbacaoInicial() != null) {
            criteria.put("dtAverbacaoInicial", filterDTO.getDtAverbacaoInicial());
        }

        if (filterDTO.getDtAverbacaoFinal() != null) {
            criteria.put("dtAverbacaoFinal", filterDTO.getDtAverbacaoFinal());
        }
        
        if (filterDTO.getNrConhecimento() != null) {
            criteria.put("nrConhecimento", filterDTO.getNrConhecimento());
        }

        if (filterDTO.getAverbado() != null) {
            criteria.put("averbado", SIM.equals(filterDTO.getAverbado().getValue()));
        }
        
        if (filterDTO.getTipoMonitoriamentoAverbacao() != null) {
            criteria.put("tipoMonAverbacao", filterDTO.getTipoMonitoriamentoAverbacao());
        }

        criteria.put("tpDestino", TP_DESTINO);

        return criteria;
    }

    public static List<MonitoramentoAverbacaoDTO> toListDTO(List<AverbacaoDoctoServico> averbacoesDoctoServico, List<AverbacaoDoctoServicoMdfe> averbacoesDoctoServicoMdfe) {
        List<MonitoramentoAverbacaoDTO> monitoramentoAverbacaoDTOList = new ArrayList<MonitoramentoAverbacaoDTO>();

        for (AverbacaoDoctoServico averbacaoDoctoServico : averbacoesDoctoServico) {
            monitoramentoAverbacaoDTOList.add(new MonitoramentoAverbacaoDTO(averbacaoDoctoServico));
        }
        
        for (AverbacaoDoctoServicoMdfe averbacaoDoctoServicoMdfe : averbacoesDoctoServicoMdfe) {
            monitoramentoAverbacaoDTOList.add(new MonitoramentoAverbacaoDTO(averbacaoDoctoServicoMdfe));
        }

        return monitoramentoAverbacaoDTOList;
    }

    public static List<Map<String, Object>> toListMap(List<MonitoramentoAverbacaoDTO> averbacoesDoctoServico) {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();

        for (MonitoramentoAverbacaoDTO averbacaoDoctoServico : averbacoesDoctoServico) {
            Map<String, Object> map = new HashMap<String, Object>();
            
            boolean isCTE = averbacaoDoctoServico.getNrDoctoServico() != null;
            map.put("tipo", averbacaoDoctoServico.getTipoMonitoramentoAverbacao());
            map.put("documentoServico", isCTE ? averbacaoDoctoServico.getTpDocumentoServico()
            		+" "
            		+averbacaoDoctoServico.getSgFilial()
            		+" "
            		+averbacaoDoctoServico.getNrDoctoServico().toString() : "");
            map.put("cliente", isCTE ? averbacaoDoctoServico.getNmCliente() : "");
            map.put("situacaoDocumento", averbacaoDoctoServico.getTpSituacaoDocumento());
            map.put("dtAverbacao", averbacaoDoctoServico.getDtAverbacao());
            map.put("protocolo", averbacaoDoctoServico.getProtocolo());
            map.put("nrAverbacao", averbacaoDoctoServico.getNrAverbacao() == null ? "" : "'" + averbacaoDoctoServico.getNrAverbacao());
            map.put("averbado", averbacaoDoctoServico.isAverbado() ? "Sim": "Não");
            map.put("retornoAverbacao", averbacaoDoctoServico.getDsRetorno());
            map.put("observacaoSeFaz", averbacaoDoctoServico.getDsObservacaoSefaz() == null ? "" : averbacaoDoctoServico.getDsObservacaoSefaz());
            map.put("modal",averbacaoDoctoServico.getModal());
            map.put("operacaoSpitFire", averbacaoDoctoServico.getBlOperacaoSpitFire() == null ||
                    averbacaoDoctoServico.getBlOperacaoSpitFire().equalsIgnoreCase("N") ? "Não" : "Sim");
            listMap.add(map);
        }

        return listMap;
    }

}

package com.mercurio.lms.rest.veiculoonline.relatorio.helper;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.rest.veiculoonline.relatorio.dto.RelatorioGreenPODFilterDTO;

public class RelatorioGreenPODRestHelper {
	
	private RelatorioGreenPODRestHelper(){}

    public static Map<String, Object> toFilterMap(RelatorioGreenPODFilterDTO filterDTO, ParametroGeral parametroIDSNatura) {
        Map<String, Object> criteria = new HashMap<String, Object>();

        if (filterDTO.getDtPeriodoInicial() != null) {
            criteria.put("dtPeriodoInicial", filterDTO.getDtPeriodoInicial().toString("dd/MM/yyyy"));
        }

        if (filterDTO.getDtPeriodoInicial() != null) {
            criteria.put("dtPeriodoFinal", filterDTO.getDtPeriodoFinal().toString("dd/MM/yyyy"));
        }
        
        if (filterDTO.getFilialOrigem() != null) {
        	criteria.put("idFilialOrigem", filterDTO.getFilialOrigem().getIdFilial());
        }
        
        if (filterDTO.getFilialDestino() != null) {
        	criteria.put("idFilialDestino", filterDTO.getFilialDestino().getIdFilial());
        }
        
        if (parametroIDSNatura != null){
        	criteria.put("idsNatura", parametroIDSNatura.getDsConteudo().replace(';', ','));
        }
        
        return criteria;
    }

}

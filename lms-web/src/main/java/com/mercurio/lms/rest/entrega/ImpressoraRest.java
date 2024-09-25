package com.mercurio.lms.rest.entrega;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.service.ImpressoraService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/entrega/impressora")
public class ImpressoraRest {
	@InjectInJersey ImpressoraService impressoraService;
	
	@GET
	@Path("findImpressora")
	public Response findImpressora() {
	    Filial filial = SessionUtils.getFilialSessao();  
        List<Impressora> impressoras = impressoraService.findImpressorasByIdFilial(filial.getIdFilial());
         
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (impressoras != null) {
            for (Impressora impressora : impressoras) {
                if (impressora.getTpImpressora().getValue().equals("E")) {
                    Map<String, Object> mapImpressora = new HashMap<String, Object>();
                    mapImpressora.put("idImpressora", impressora.getIdImpressora());
                    mapImpressora.put("checkinLocalizacao", impressora.getCheckinLocalizacao());
                    result.add(mapImpressora);
                }
            }
        }
        return Response.ok(result).build();
	}
}

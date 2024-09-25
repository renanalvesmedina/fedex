package com.mercurio.lms.services.evento;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.WhiteListService;
import com.mercurio.lms.municipios.dto.UltimoEventoDto;
import com.mercurio.lms.sim.model.service.EventoService;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

@Path("/evento")
public class EventoRest {

	@InjectInJersey 
	EventoService eventoService;

	@InjectInJersey
	WhiteListService whiteListService;

	/**
	 * Responsavel por retornar dados do ultimo evento 
	 * @param nrDoctoServico
	 * @param sgFilial
	 * @param dhEmissao
	 * @return
	 */
	@POST
	@Path("findUltimoEvento")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findUltimoEvento(Map<String, Object> data) {
		Long nrDoctoServico = MapUtils.getLong(data, "nrDoctoServico");	
		String sgFilial = MapUtils.getString(data, "sgFilial");
		Long dhEmissaoTime =  MapUtils.getLong(data, "dhEmissaoTime");
		Date dhEmissao =  new Date(dhEmissaoTime);
		UltimoEventoDto ultEvento = eventoService.findUltimoEvento(nrDoctoServico, sgFilial, dhEmissao);
		return Response.ok(ultEvento).type(MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Path("executeEventoRastreabilidade")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeEventoRastreabilidade(EventoDocumentoServicoDMN eventoDoctoServico) {
		whiteListService.executeWhiteListRastreabilidade(eventoDoctoServico);
		return Response.ok().build();
	}
}

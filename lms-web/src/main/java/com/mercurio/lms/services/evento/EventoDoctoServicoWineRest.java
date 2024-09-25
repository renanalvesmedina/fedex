package com.mercurio.lms.services.evento;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoDecathlonDMN;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoNotaFiscalWine;

@Path("evento/doctoservico/integracaowine")
public class EventoDoctoServicoWineRest {
	
	@InjectInJersey
	private EventoDocumentoServicoService eventoDocumentoServicoService;
		
	@POST
	@Path("enriqueceocorrenciaentrega")
	public Response enriqueceocorrenciaentrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){

		EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
		boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
				.carregarIdEventoDocumentoServicoValidar
						(
								eventoDocumentoServicoDMN,
								eventoDocumentoServicoService
						);

		if (carregarIdEventoDocumentoServicoValidar) {
			return Response.status(Status.NOT_MODIFIED).build();
		}

		List<EventoNotaFiscalWine> notasEventoWine = eventoDocumentoServicoService.findNotasEventoDoctoServicoWine(eventoDocumentoServicoDMN.getIdEventoDocumentoServico());
		if (notasEventoWine == null){
			return Response.status(Status.NOT_MODIFIED).build();
		}else{
			return Response.ok(notasEventoWine).build();
		}
	}

}

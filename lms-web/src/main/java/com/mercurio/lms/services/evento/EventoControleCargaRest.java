package com.mercurio.lms.services.evento;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;

import br.com.tntbrasil.integracao.domains.carregamento.EventoControleCargaDMN;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaDMN;

@Path("evento/controleCarga") 
public class EventoControleCargaRest extends BaseRest{

		@InjectInJersey 
		ControleCargaService controleCargaService;
		
		@POST
		@Path("enriqueceRomaneioEntrega")
		public Response enriqueceRomaneioEntrega(EventoControleCargaDMN eventoControleCargaDMN) {
		    RomaneioEntregaDMN dto = controleCargaService.executeEnriqueceRomaneioEntrega(eventoControleCargaDMN);
            return Response.ok(dto).build();
		}
	} 

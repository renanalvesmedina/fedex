package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.pedidocoleta.AceiteColetaBoschDMN;
import br.com.tntbrasil.integracao.domains.sim.ColetaRealizadaDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoColetaDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoColetaFedexDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.PedidoColetaService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/evento/coleta/integracaobosh")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class EventoColetaServicoBoschRest extends BaseRest {

    @InjectInJersey
    private PedidoColetaService pedidoColetaService;

    @POST
    @Path("enriqueceColetaRealizada")
    public Response enriqueceColetaRealizada(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
        List<ColetaRealizadaDMN> listaColetaRealizada = pedidoColetaService
                                                        .findEventoColetaRealizadaBosch(eventoDocumentoServicoDMN.getIdEventoDocumentoServico());
        return Response.ok(listaColetaRealizada).build();
    }

    @POST
    @Path("enriqueceEventoColeta")
    public Response enriqueceEventoColeta(EventoColetaDMN eventoColetaDMN){
        AceiteColetaBoschDMN aceiteColetaBosch = pedidoColetaService.findEventoAceiteColetaBosch
            (eventoColetaDMN.getIdPedidoColeta(),
             eventoColetaDMN.getIdEventoColeta(),
             eventoColetaDMN.getDhEvento());
        return Response.ok(aceiteColetaBosch).build();
    }

}

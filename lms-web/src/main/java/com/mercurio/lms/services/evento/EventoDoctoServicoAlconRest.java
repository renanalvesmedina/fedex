package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoAlconDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoRotaCremerDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("evento/doctoservico/integracaoalcon")
public class EventoDoctoServicoAlconRest extends BaseRest {
    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;

    @POST
    @Path("enriqueceOcorrenciaEntrega")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public Response enriqueceOcorrenciaEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {

        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            throw new WebApplicationException("Evento documento servico invalido", 400);
        }
        EventoDoctoServicoAlconDMN eventoDoctoServicoAlconDMN = eventoDocumentoServicoService.findNotasEventoAlcon
            (eventoDocumentoServicoDMN.getIdEventoDocumentoServico(), eventoDocumentoServicoDMN.getIdDoctoServico());

        return Response.ok(eventoDoctoServicoAlconDMN).build();
    }

}

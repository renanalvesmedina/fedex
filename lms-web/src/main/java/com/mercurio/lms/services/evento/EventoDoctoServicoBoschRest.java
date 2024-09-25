package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoBoschDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoCremerDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/evento/doctoservico/integracaobosh")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class EventoDoctoServicoBoschRest  extends BaseRest {

    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;

    @POST
    @Path("enriqueceocorrenciaentrega")
    public Response enriqueceOcorrenciaEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN){
        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new EventoDoctoServicoBoschDMN()).build();
        }

        EventoDoctoServicoBoschDMN eventoDoctoServicoBosch = eventoDocumentoServicoService.
            findNotasEventoDoctoServicoBosch
                (eventoDocumentoServicoDMN.getIdEventoDocumentoServico(), eventoDocumentoServicoDMN.getIdDoctoServico());
        return Response.ok(eventoDoctoServicoBosch).build();
    }
}

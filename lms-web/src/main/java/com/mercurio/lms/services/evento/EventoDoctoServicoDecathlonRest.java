package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoDecathlonDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.RetornoIntegracaoClienteService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("evento/doctoservico/integracaodecathlon")
public class EventoDoctoServicoDecathlonRest extends BaseRest {
    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;

    @InjectInJersey
    private RetornoIntegracaoClienteService retornoIntegracaoClienteService;

    @POST
    @Path("enriqueceocorrenciaentrega")
    public Response enriqueceOcorrencia(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) throws Exception {

        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );

        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new EventoDoctoServicoDecathlonDMN()).build();
        }
        EventoDoctoServicoDecathlonDMN eventoDoctoServicoDecathlonDMN =
                retornoIntegracaoClienteService
                        .findNotasEventoDoctoServicoDecathlon
                                (
                                        eventoDocumentoServicoDMN.getIdEventoDocumentoServico(),
                                        eventoDocumentoServicoDMN.getIdDoctoServico()

                                );
        return Response.ok(eventoDoctoServicoDecathlonDMN).build();
    }
    
}

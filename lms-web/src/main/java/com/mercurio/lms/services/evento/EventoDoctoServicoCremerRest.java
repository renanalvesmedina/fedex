package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.*;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.RetornoIntegracaoClienteService;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("evento/doctoservico/integracaocremer")
public class EventoDoctoServicoCremerRest  extends BaseRest {
    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    @InjectInJersey
    private RetornoIntegracaoClienteService retornoIntegracaoClienteService;

    @POST
    @Path("enriqueceocorrencia")
    public Response enriqueceOcorrencia(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {

        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new EventoDoctoServicoCremerDMN()).build();
        }

        EventoDoctoServicoCremerDMN eventoDoctoServicoCremerDMN = retornoIntegracaoClienteService
                .findNotasEventoDoctoServicoCremer
                        (eventoDocumentoServicoDMN
                                        .getIdEventoDocumentoServico(),
                                eventoDocumentoServicoDMN
                                        .getIdDoctoServico(),
                                eventoDocumentoServicoDMN.getCdEvento()
                        );
        return Response.ok(eventoDoctoServicoCremerDMN).build();

    }

    @POST
    @Path("enriqueceocorrenciaentrega")
    public Response enriqueceOcorrenciaEntrega(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {

        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new EventoDoctoServicoRotaCremerDMN()).build();
        }
        EventoDoctoServicoRotaCremerDMN eventoDoctoServicoRotaCremerDMN =
            retornoIntegracaoClienteService
                .findNotasEventoDoctoServicoRotaCremer
                    (
                        eventoDocumentoServicoDMN.getIdEventoDocumentoServico(),
                        eventoDocumentoServicoDMN.getIdDoctoServico(),
                        eventoDocumentoServicoDMN.getIdFilialEvento(),
                        eventoDocumentoServicoDMN.getDhEvento()
                    );
        return Response.ok(eventoDoctoServicoRotaCremerDMN).build();
    }

    @POST
    @Path("enriqueceocorrenciafinalizarota")
    public Response enriqueceOcorrenciaFinalizar(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new RouteParameterCremerDMN()).build();
        }

        RouteParameterCremerDMN routeParameterCremerDMN = retornoIntegracaoClienteService
                                                            .findNotasEventoDoctoServicoFinalizaRotaCremer
                                                                (
                                                                    eventoDocumentoServicoDMN.getIdEventoDocumentoServico(),
                                                                    eventoDocumentoServicoDMN.getIdDoctoServico(),
                                                                    eventoDocumentoServicoDMN.getIdFilialEvento(),
                                                                    eventoDocumentoServicoDMN.getDhEvento()

                                                                );

        return Response.ok(routeParameterCremerDMN).build();
    }

    @POST
    @Path("enriqueceAgendamento")
    public Response enriqueceAgendamento(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
        EventoDocumentoServicoUtil eventoDocumentoServicoUtil = new EventoDocumentoServicoUtil();
        boolean carregarIdEventoDocumentoServicoValidar = eventoDocumentoServicoUtil
                .carregarIdEventoDocumentoServicoValidar
                        (
                                eventoDocumentoServicoDMN,
                                eventoDocumentoServicoService
                        );
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok(new SchedulingParameterCremerDMN()).build();
        }

        SchedulingParameterCremerDMN schedulingParameterCremerDMN = eventoDocumentoServicoService
                .findNotasEventoDoctoServicoAgendamentoCremer
                        (
                            eventoDocumentoServicoDMN.getIdEventoDocumentoServico(),
                            eventoDocumentoServicoDMN.getIdDoctoServico()
                        );

        return Response.ok(schedulingParameterCremerDMN).build();
    }
}

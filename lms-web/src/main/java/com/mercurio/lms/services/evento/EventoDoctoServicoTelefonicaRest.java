package com.mercurio.lms.services.evento;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("evento/doctoservico/integracaotelefonica")
public class EventoDoctoServicoTelefonicaRest {

    private static final Logger LOGGER = LogManager.getLogger(EventoDoctoServicoTelefonicaRest.class);

    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;

    @POST
    @Path("enriqueceenvioctetoutbox/{idEventoDocumentoServico}")
    public Response enriqueceEnvioCteToutBox(@PathParam("idEventoDocumentoServico") Long idEventoDocumentoServico,
                                             @Context HttpHeaders headers) {

        try {
            return Response.ok(eventoDocumentoServicoService.findEnvioCteToutBox(idEventoDocumentoServico)).build();

        }catch (BusinessException businessException) {

            Map<String, Object> mapException = this.gerarMapException(businessException, headers, idEventoDocumentoServico);

            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();

        }
    }

    @POST
    @Path("enriqueceocorrenciaentregatoutbox/{idEventoDocumentoServico}")
    public Response enriqueceOcorrenciaEntregaToutBox(@PathParam("idEventoDocumentoServico") Long idEventoDocumentoServico,
                                                      @Context HttpHeaders headers) {

        try {
            return Response.ok(eventoDocumentoServicoService.findOcorrenciaEntregaToutBox(idEventoDocumentoServico)).build();
        }catch (BusinessException businessException) {

            Map<String, Object> mapException = this.gerarMapException(businessException, headers, idEventoDocumentoServico);

            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();
        }
    }

    @POST
    @Path("enriqueceocorrenciaentregaintelipost/{idEventoDocumentoServico}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enriqueceOcorrenciaEntregaIntelipost(@PathParam("idEventoDocumentoServico") Long idEventoDocumentoServico,
                                                         @Context HttpHeaders headers) throws JsonProcessingException {

        try {
            return Response.ok(eventoDocumentoServicoService.findOcorrenciaEntregaIntelipost(idEventoDocumentoServico)).build();
        }catch (BusinessException businessException) {

            Map<String, Object> mapException = this.gerarMapException(businessException, headers, idEventoDocumentoServico);

            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();
        }

    }

    private Map<String, Object> gerarMapException(BusinessException businessException, HttpHeaders headers, Long idEventoDocumentoServico) {

        Map<String, Object> mapException = new HashMap<>();

        mapException.put("IdEventoDocumentoServico", idEventoDocumentoServico);
        mapException.put("Mensagem", businessException.getMessage());
        mapException.put("Path", headers.getHeaderString("Path"));
        mapException.put("RouteId", headers.getHeaderString("RouteId"));

        return  mapException;

    }

}

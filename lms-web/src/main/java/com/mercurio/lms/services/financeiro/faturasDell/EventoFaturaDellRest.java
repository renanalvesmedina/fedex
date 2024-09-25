package com.mercurio.lms.services.financeiro.faturasDell;

import br.com.tntbrasil.integracao.domains.dell.FaturaDellDMN;
import br.com.tntbrasil.integracao.domains.dell.layout.d2l.D2LIntegracaoDMN;
import br.com.tntbrasil.integracao.domains.dell.layout.traxcte.TraxCteDMN;
import br.com.tntbrasil.integracao.domains.dell.layout.traxnfse.TraxNfseDMN;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path("/financeiro/faturasDell")
public class EventoFaturaDellRest extends BaseRest {

    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    @InjectInJersey
    private FaturaService faturaService;

    private static final Logger LOGGER = LogManager.getLogger(EventoFaturaDellRest.class);

    @POST
    @Path("enriqueceEventoTraxCte")
    public Response enriqueceOcorrenciaEntrega(FaturaDellDMN faturaDellDMN,
                                               @Context HttpHeaders headers) {

        try {
            return Response.ok(eventoDocumentoServicoService.
                    findFaturasDelTraxCte(faturaDellDMN.getIdFatura(), faturaDellDMN.getNrIdentificacaoCliente())).build();

        } catch (BusinessException businessException) {
            Map<String, Object> mapException = this.gerarMapException(businessException, headers, faturaDellDMN);
            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();
        }
    }

    @POST
    @Path("enriqueceEventoTraxNfse")
    public Response enriqueceEventoTraxNfse(FaturaDellDMN faturaDellDMN,
                                            @Context HttpHeaders headers) {

        try {
            return Response.ok(eventoDocumentoServicoService
                    .findNotasEventoDellNfse(faturaDellDMN.getIdFatura(), faturaDellDMN.getNrIdentificacaoCliente())).build();
        } catch (BusinessException businessException) {
            Map<String, Object> mapException = this.gerarMapException(businessException, headers, faturaDellDMN);
            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();
        }
    }


    @POST
    @Path("enriqueceEventoD2lCte")
    public Response enriqueceFaturaD2l(FaturaDellDMN faturaDellDMN,
                                       @Context HttpHeaders headers) {
        try {
            return Response.ok(faturaService.findFaturaLayoutD2l(faturaDellDMN)).build();
        } catch (BusinessException businessException) {
            Map<String, Object> mapException = this.gerarMapException(businessException, headers, faturaDellDMN);
            LOGGER.error(businessException.getMessage());

            return Response.status(Response.Status.BAD_REQUEST).entity(mapException).build();
        }
    }

    private Map<String, Object> gerarMapException(BusinessException businessException, HttpHeaders headers, FaturaDellDMN faturaDellDMN) {
        Map<String, Object> mapException = new HashMap<>();

        mapException.put("faturaDellDMN", faturaDellDMN);
        mapException.put("Mensagem", businessException.getMessage());
        mapException.put("Path", headers.getHeaderString("Path"));
        mapException.put("RouteId", headers.getHeaderString("RouteId"));

        return mapException;
    }
}

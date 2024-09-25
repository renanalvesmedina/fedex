package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.RetornoIntegracaoClienteDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.sim.model.service.RetornoIntegracaoClienteService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("evento/doctoservico/retornointegracaocliente")
public class RetornoIntegracaoClienteRest extends BaseRest {

    @InjectInJersey
    private RetornoIntegracaoClienteService retornoIntegracaoClienteService;

    @POST
    @Path("storeprotocolo")
    public Response storeProtocolo(RetornoIntegracaoClienteDMN retornoIntegracaoClienteDMN){
        try {
            retornoIntegracaoClienteService.storeRetornoIntegracao(retornoIntegracaoClienteDMN);
            return Response.status(Response.Status.CREATED).build();
        }catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}

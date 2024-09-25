package com.mercurio.lms.rest;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/integracaoJwt")
public class IntegracaoJwtRest {

    @InjectInJersey
    private IntegracaoJwtService integracaoJwtService;

    @GET
    @Produces("application/json")
    @Path("/refreshToken")
    public String refreshToken(@Context HttpHeaders headers){
        return integracaoJwtService.refreshToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
    }

    public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
        this.integracaoJwtService = integracaoJwtService;
    }
}

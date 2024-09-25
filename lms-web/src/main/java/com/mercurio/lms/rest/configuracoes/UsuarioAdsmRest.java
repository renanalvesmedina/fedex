package com.mercurio.lms.rest.configuracoes;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.UsuarioAdsmService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/configuracoes/usuarioAdsm")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class UsuarioAdsmRest extends BaseRest {

    @InjectInJersey
    private UsuarioAdsmService usuarioAdsmService;

    @GET
    @Path("/findUsuarioAdsmSuggest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List findUsuarioAdsmSuggest(@QueryParam("value") String value, @QueryParam("limiteRegistro") Integer limiteRegistro)throws Exception{
        return usuarioAdsmService.findSuggestUsuario(value, limiteRegistro);
    }

    public UsuarioAdsmService getUsuarioAdsmService() {
        return usuarioAdsmService;
    }

    public void setUsuarioAdsmService(UsuarioAdsmService usuarioAdsmService) {
        this.usuarioAdsmService = usuarioAdsmService;
    }
}

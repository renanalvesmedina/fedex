package com.mercurio.lms.rest.expedicao;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.edi.dto.FiltroProcessamentoNotasEdiDTO;
import com.mercurio.lms.expedicao.edi.model.service.ProcessamentoEdiService;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("expedicao/processamento/edi")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class ProcessamentoEdiRest extends BaseRest {

    @InjectInJersey
    private ProcessamentoEdiService processamentoEdiService;

    @InjectInJersey
    private IntegracaoJwtService integracaoJwtService;

    @GET
    @Path("findDadosCliente/{nrIdentificacao}")
    public Response findDadosCliente(@PathParam("nrIdentificacao") String nrIdentificacao, @Context HttpHeaders headers) throws Exception {
        return Response.ok(processamentoEdiService.findDadosCliente(nrIdentificacao,
                           integracaoJwtService.getIdFilialByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))
                                                                    )).build();
    }

    @POST
    @Path("findByFilterProcessamentoEdi")
    public Response findByFilterProcessamentoEdi(FiltroProcessamentoNotasEdiDTO filtroProcessamentoNotasEdiDTO, @Context HttpHeaders headers){
        filtroProcessamentoNotasEdiDTO.setIdFilial(integracaoJwtService.getIdFilialByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
        return Response.ok(processamentoEdiService.findByFilterProcessamentoEdi(filtroProcessamentoNotasEdiDTO)).build();
    }

}

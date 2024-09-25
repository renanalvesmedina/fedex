package com.mercurio.lms.rest.vendas;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.vendas.model.ParametrizacaoEnvioCTeCliente;
import com.mercurio.lms.vendas.model.service.ParametrizacaoEnvioCTeClienteService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/vendas/parametrizacaoEnvioCTeCliente")
public class ParametrizacaoEnvioCTeClienteRest extends BaseRest {

    @InjectInJersey
    private ParametrizacaoEnvioCTeClienteService parametrizacaoEnvioCTeClienteService;

    @GET
    @Path("findParametrizacaoEnvioCTeCliente")
    public List findParametrizacaoEnvioCTeCliente(@QueryParam("tpCnpj") String tpCnpj,
                                                  @QueryParam("nrIdentificacao") String nrIdentificacao,
                                                  @QueryParam("dsDiretorioArmazenagem") String dsDiretorioArmazenagem,
                                                  @QueryParam("tpPesquisa") String tpPesquisa,
                                                  @QueryParam("blAtivo") Boolean blAtivo){
        return parametrizacaoEnvioCTeClienteService
                .findByNrIdentificacao(tpCnpj, nrIdentificacao, dsDiretorioArmazenagem, tpPesquisa, blAtivo);
    }

    @GET
    @Path("findNomeCliente")
    public String findNomeCliente(@QueryParam("nrIdentificacao") String nrIdentificacao, @QueryParam("tpCnpj") String tpCnpj){
        return parametrizacaoEnvioCTeClienteService.findNomeFantasiaCliente(nrIdentificacao, tpCnpj);
    }

    @POST
    @Path("store")
    public Response store(ParametrizacaoEnvioCTeCliente dto){
        parametrizacaoEnvioCTeClienteService.store(dto);
        return Response.ok().status(Response.Status.CREATED) .build();
    }
}
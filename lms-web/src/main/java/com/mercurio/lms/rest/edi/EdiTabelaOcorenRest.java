package com.mercurio.lms.rest.edi;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.edi.dto.OcorrenciaSelectDTO;
import com.mercurio.lms.edi.dto.TabelaClienteDTO;
import com.mercurio.lms.edi.dto.TabelaOcorenDetSelectDTO;
import com.mercurio.lms.edi.dto.TipoOcorrenciaDTO;
import com.mercurio.lms.edi.model.EdiTabelaCliente;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;
import com.mercurio.lms.edi.model.EdiTabelaOcorenDet;
import com.mercurio.lms.edi.model.service.EdiTabelaClienteService;
import com.mercurio.lms.edi.model.service.EdiTabelaOcorenDetService;
import com.mercurio.lms.edi.model.service.EdiTabelaOcorenService;

import com.mercurio.lms.rest.edi.dto.EdiTabelaClienteDTO;
import com.mercurio.lms.rest.edi.dto.EdiTabelaOcorenDTO;
import com.mercurio.lms.rest.edi.dto.EdiTabelaOcorenDetDTO;

import javax.ws.rs.*;

import javax.ws.rs.core.Response;
import java.util.List;

@Path("edi/tabela/ocorencia")
public class EdiTabelaOcorenRest extends BaseEditTabelaRest {

    @InjectInJersey
    private EdiTabelaOcorenService ediTabelaOcorenService;

    @InjectInJersey
    private EdiTabelaOcorenDetService ediTabelaOcorenDetService;

    @InjectInJersey
    private EdiTabelaClienteService ediTabelaClienteService;

    @InjectInJersey
    private DomainValueService domainValueService;

    @GET
    @Path("listar")
    @Produces("application/json;charset=utf-8")
    public Response  find(@QueryParam("nome") String nome, @QueryParam("webService") String webService) {
        List<EdiTabelaOcoren> ediTabelaOcorenList = ediTabelaOcorenService
                                                        .findByNmTabelaOcorenWebService(nome, webService);
        List<EdiTabelaOcorenDTO> ediTabelaOcorenDTOList = toListEditTabelaOcorenDTO(ediTabelaOcorenList);
        return Response.ok(ediTabelaOcorenDTOList).build();
    }

    @GET
    @Path("listarOcorrencia/idEdiTabelaOcoren/{id}")
    @Produces("application/json;charset=utf-8")
    public Response listarOcorrencia(@PathParam("id") Long idEdiTabelaOcoren){
       List<TabelaOcorenDetSelectDTO> ediTabelaOcorenDetList = ediTabelaOcorenDetService
                                                            .findByIdEdiTabelaOcoren(idEdiTabelaOcoren);
        return Response.ok(toListEditTabelaOcorenDetDTO(ediTabelaOcorenDetList)).build();
    }

    @GET
    @Path("listarCliente/idEdiTabelaOcoren/{id}")
    @Produces("application/json;charset=utf-8")
    public Response listarCliente(@PathParam("id") Long idEdiTabelaOcoren){
       List<TabelaClienteDTO> tabelaClienteDTOList = ediTabelaClienteService.findByIdEdiTabelaOcoren(idEdiTabelaOcoren);
       return Response.ok(toListEdiTabelaClienteDTO(tabelaClienteDTOList)).build();
    }

    @GET
    @Path("carregar/tipoOcorrecia")
    @Produces("application/json;charset=utf-8")
    public Response carregarTipoOcorrencia(){
        List<TipoOcorrenciaDTO> tipoOcorrenciaDTOList = ediTabelaOcorenDetService
                                                            .findTipoOcorrenciaByValorDominio();
        return Response.ok(toListComponentTipoOcorrenciaDTO(tipoOcorrenciaDTOList)).build();
    }

    @GET
    @Path("carregar/codigoOcorrencia/tipoOcorrecia/{tipoOcorrencia}")
    @Produces("application/json;charset=utf-8")
    public Response carregarCodigoOcorrencia(@PathParam("tipoOcorrencia") String tipoOcorrencia){
        List<OcorrenciaSelectDTO> ocorrenciaSelectDTOList = ediTabelaOcorenDetService
                .findCodigoOcorrenciaByTpOcorrencia(tipoOcorrencia);
        return Response.ok(toListComponentCodigoOcorrecia(ocorrenciaSelectDTOList)).build();
    }

    @POST
    @Path("store")
    @Consumes("application/json;charset=utf-8")
    @Produces("application/json;charset=utf-8")
    public Response store(EdiTabelaOcorenDTO ediTabelaOcorenDTO) throws Exception {
        EdiTabelaOcoren ediTabelaOcoren = toEdiTabelaOcorenEntity(ediTabelaOcorenDTO);
        ediTabelaOcorenService.store(ediTabelaOcoren);
        ediTabelaOcorenDTO.setIdEdiTabelaOcoren(ediTabelaOcoren.getIdEdiTabelaOcoren());
        return Response.ok(ediTabelaOcorenDTO).status(Response.Status.CREATED).build();
    }

    @GET
    @Path("buscar/cliente/numeroIdentificacao/{nrIdentificacao}")
    @Produces("application/json;charset=utf-8")
    public Response buscarPessoa(@PathParam("nrIdentificacao") String nrIdentificacao) throws Exception {
        Pessoa pessoa = ediTabelaClienteService.findNomePessoaByNumeroIdentificacao(nrIdentificacao);
        return Response.ok(toPessoaDTO(pessoa)).build();
    }

    @POST
    @Path("store/ocorrencia")
    @Produces("application/json;charset=utf-8")
    public Response storeOcorrecia(EdiTabelaOcorenDetDTO ediTabelaOcorenDetDTO){
        EdiTabelaOcorenDet ediTabelaOcorenDet = toEdiTabelaOcorenDetEntity(ediTabelaOcorenDetDTO);
        ediTabelaOcorenDetService.store(ediTabelaOcorenDet);
        ediTabelaOcorenDetDTO.setIdEdiTabelaOcorenDet(ediTabelaOcorenDet.getIdEdiTabelaOcorenDet());
        return Response.ok(ediTabelaOcorenDetDTO).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("store/cliente")
    @Produces("application/json;charset=utf-8")
    public Response storeCliente(EdiTabelaClienteDTO ediTabelaClienteDTO){
        EdiTabelaCliente ediTabelaCliente = toEdiTabelaClienteEntity(ediTabelaClienteDTO);
        ediTabelaClienteService.store(ediTabelaCliente);
        ediTabelaClienteDTO.setIdEdiTabelaCliente(ediTabelaCliente.getIdEdiTabelaCliente());
        return Response.ok(ediTabelaCliente).status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("remove/idEdiTabelaOcoren/{id}")
    public Response remove(@PathParam("id") Long id) throws Exception{
        ediTabelaOcorenService.remove(id);
        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("remove/ocorrencia")
    public Response removeOcorrencia(List<Long> ids){
        ediTabelaOcorenDetService.removeByIds(ids);
        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("remove/cliente")
    public Response removeCliente(List<Long> ids){
        ediTabelaClienteService.removeByIds(ids);
        return Response.ok().status(Response.Status.NO_CONTENT).build();
    }

}

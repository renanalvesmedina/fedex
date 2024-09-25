package com.mercurio.lms.rest.mobilescanapp;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.mobilescanapp.model.service.ConsultaVolumeMobileScanService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("mobilescanapp/consultaVolume")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class ConsultaVolumeMobileScanAppRest extends BaseRest {

    @InjectInJersey
    ConsultaVolumeMobileScanService consultaVolumeMobileScanService;

    @POST
    @Path("/executeVolumeByBarCode/{nrCodigoBarras}")
    public Response executeVolumeByBarCode(@PathParam("nrCodigoBarras") String nrCodigoBarras, @Context HttpHeaders headers) {
        return Response.ok(consultaVolumeMobileScanService.executeVolumeByBarCode(nrCodigoBarras,
                headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/executeGeraEventoVolumeEncontrado/{idVolumeNotaFiscal}")
    public Response executeGeraEventoVolumeEncontrado(@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal, @Context HttpHeaders headers) {
        return Response.ok(consultaVolumeMobileScanService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal,
                headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/executeAtualizarFilialLocalizacaoVolume/{idVolumeNotaFiscal}")
    public Response executeAtualizarFilialLocalizacaoVolume(@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal, @Context HttpHeaders headers) {
        return Response.ok(consultaVolumeMobileScanService.executeAtualizarFilialLocalizacaoVolume(idVolumeNotaFiscal,
                headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @GET
    @Path("/findPaginatedEventoVolumeByIdVolume/{idVolumeNotaFiscal}/{pageNumber}/{pageSize}")
    public Response findPaginatedEventoVolumeByIdVolume(@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal,
                                                        @PathParam("pageNumber") int pageNumber,
                                                        @PathParam("pageSize") int pageSize,
                                                        @Context HttpHeaders headers) {
        Map<String, Object>  resposta = consultaVolumeMobileScanService.findPaginatedEventoVolumeByIdVolume(idVolumeNotaFiscal, pageNumber, pageSize);
        return Response.ok(resposta).build();
    }

    @GET
    @Path("/findMensagemAlteraLocalizacao/{key}")
    public Response findMensagemAlteraLocalizacao(@PathParam("key") String key, @Context HttpHeaders headers) {
         consultaVolumeMobileScanService.findMensagemAlteraLocalizacao(key, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        return Response.ok().build();
    }
}

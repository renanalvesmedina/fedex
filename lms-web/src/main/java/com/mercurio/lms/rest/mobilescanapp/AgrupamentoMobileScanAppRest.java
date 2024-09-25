package com.mercurio.lms.rest.mobilescanapp;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.mobilescanapp.model.dto.StoreAlocarDto;
import com.mercurio.lms.mobilescanapp.model.dto.StoreUnitilizacaoDto;
import com.mercurio.lms.mobilescanapp.model.service.AgrupamentoService;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.service.MacroZonaService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("mobilescanapp/agrupamento")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class AgrupamentoMobileScanAppRest {

    @InjectInJersey
    private AgrupamentoService agrupamentoService;

    @InjectInJersey
    private MacroZonaService macroZonaService;


    @GET
    @Path("obter-unitizacao-pai/{barcode}/idFilial/{idFilial}")
    public Response executeFindDispositivoUnitizacaoByBarcode
    (@PathParam("barcode") String barCode, @PathParam("idFilial") Long idFilial) {
        Map<String, Object> retorno = agrupamentoService.executeFindDispositivoUnitizacaoByBarcode(barCode, idFilial);
        return Response.ok(retorno).build();
    }

    @GET
    @Path("obter-volume/{barcode}")
    public Response findVolumeByBarCode(@PathParam("barcode") String barCode){
        Map<String, Object> retorno =  agrupamentoService.findVolumeByBarCode(barCode);
        return Response.ok(retorno).build();
    }

    @GET
    @Path("carrega-codigo-barras")
    public Response findIdCodigoBarras(){
        Map<String, Object> retorno = agrupamentoService.findIdCodigoBarras();
        return Response.ok(retorno).build();
    }

    @GET
    @Path("obter-unitizacao/{barcode}/idFilial/{idFilial}")
    public Response findMapDispositivoUnitizacaoByBarcode
    (@PathParam("barcode") String barCode, @PathParam("idFilial") Long idFilial) {
        Map<String, Object> retorno = agrupamentoService.findMapDispositivoUnitizacaoByBarcode(barCode, idFilial);
        return Response.ok(retorno).build();
    }

    @GET
    @Path("encontrar-conteiner/{idDispsitivoUnitizacao}/idFilial/{idFilial}/idUsuario/{idUsuario}")
    public Response executeGeraEventoDispositivoEncontrado
    (@PathParam("idDispsitivoUnitizacao") Long idDispsitivoUnitizacao,
     @PathParam("idFilial") Long idFilial,
     @PathParam("idUsuario") Long idUsuario
    ) {
        agrupamentoService.executeGeraEventoDispositivoEncontrado(idDispsitivoUnitizacao, idFilial, idUsuario);
        return Response.ok().build();
    }

    @GET
    @Path("gera-evento-conteiner-lido/{idDispsitivoUnitizacao}/idFilial/{idFilial}/idUsuario/{idUsuario}")
    public Response geraEventoConteinerLido
    (@PathParam("idDispsitivoUnitizacao") Long idDispsitivoUnitizacao,
     @PathParam("idFilial") Long idFilial,
     @PathParam("idUsuario") Long idUsuario
    ) {
        agrupamentoService.executeEventoDispositivoLido(idDispsitivoUnitizacao, idFilial, idUsuario);
        return Response.ok().build();
    }

    @GET
    @Path("encontrar-volume/{idVolumeNotaFiscal}/idFilial/{idFilial}/idUsuario/{idUsuario}")
    public Response executeGeraEventoVolumeEncontrado
    (@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal,
     @PathParam("idFilial") Long idFilial,
     @PathParam("idUsuario") Long idUsuario
    ) {
        agrupamentoService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal, idFilial, idUsuario);
        return Response.ok().build();
    }

    @GET
    @Path("gera-evento-volume-lido/{idVolumeNotaFiscal}/idFilial/{idFilial}/idUsuario/{idUsuario}")
    public Response executeEventoVolumeLido
    (@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal,
     @PathParam("idFilial") Long idFilial,
     @PathParam("idUsuario") Long idUsuario
    ) {
        agrupamentoService.executeEventoVolumeLido(idVolumeNotaFiscal, idFilial, idUsuario);
        return Response.ok().build();
    }

    @POST
    @Path("concluir-unitizacao")
    public Response concluirUnitizacao(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        agrupamentoService.storeUnitizacao(storeUnitilizacaoDto);
        return Response.ok().build();
    }

    @GET
    @Path("obter-desunitizacao/{barcode}/idFilial/{idFilial}")
    public Response findDadosDispositivoUnitizacao
    (@PathParam("barcode") String barCode, @PathParam("idFilial") Long idFilial) {
        Map<String, Object> retorno = agrupamentoService.findDadosDispositivoUnitizacao(barCode, idFilial);
        return Response.ok(retorno).build();
    }

    @POST
    @Path("desunitizar-parcial")
    public Response storeDesunitizarParcial(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        agrupamentoService.storeDesunitizarParcial(storeUnitilizacaoDto);
        return Response.ok().build();
    }

    @POST
    @Path("desunitizar-total")
    public Response storeDesunitizarTotal(StoreUnitilizacaoDto storeUnitilizacaoDto){
        Map<String, Object> retorno = agrupamentoService.storeDesunitizarTotal(storeUnitilizacaoDto);
        return Response.ok(retorno).build();
    }

    @POST
    @Path("desunitizar-total-com-divergencias")
    public Response storeDesunitizarTotalComDivergencias(StoreUnitilizacaoDto storeUnitilizacaoDto) {
        agrupamentoService.storeDesunitizarTotalComDivergencias(storeUnitilizacaoDto);
        return Response.ok().build();
    }

    @GET
    @Path("obter-endereco/{barcode}")
    public Response findEnderecoByBarcode(@PathParam("barcode") String barCode){
        Map<String, Object> retorno =  agrupamentoService.findEnderecoByBarcode(barCode);
        return Response.ok(retorno).build();
    }

    @POST
    @Path("concluir-alocacao")
    public Response storeAlocar(StoreAlocarDto storeAlocarDto, @Context HttpHeaders headers) {
        String token = headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0);
        MacroZona macroZona = macroZonaService.findById(Long.parseLong(storeAlocarDto.getIdEnderecoTerminal()));
        agrupamentoService.storeAlocar(storeAlocarDto, macroZona, token);
        return Response.ok().build();
    }

    @PUT
    @Path("store-desalocar")
    public Response storeDesalocar(StoreAlocarDto store, @Context HttpHeaders headers){
        String token = headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0);
        agrupamentoService.storeDesalocar(store,token);
        return Response.ok().build();
    }

    @GET
    @Path("obter-dispositivo-unitizacao/{idDispositivoUnitizacao}")
    public Response findDispositivoUnitizacaoById(@PathParam("idDispositivoUnitizacao") Long idDispositivoUnitizacao){
        Map<String, Object> retorno =  agrupamentoService.findDispositivoUnitizacaoById(idDispositivoUnitizacao);
        return Response.ok(retorno).build();
    }
}

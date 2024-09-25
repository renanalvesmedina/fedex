package com.mercurio.lms.rest.mobilescanapp;

import br.com.tntbrasil.integracao.domains.mobileScanApp.DescargaDipositivoDMN;
import br.com.tntbrasil.integracao.domains.mobileScanApp.DescargaDispositivoConfirmacaoDMN;
import br.com.tntbrasil.integracao.domains.mobileScanApp.DescargaVolumeDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.mobilescanapp.model.service.DescargaMobileScanService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("mobilescanapp/descarga")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class DescargaMobileScanAppRest extends BaseRest {

    @InjectInJersey
    DescargaMobileScanService descargaMobileScanService;

    @GET
    @Path("/viagem/{nrCodigoBarras}")
    public Response descarregamentoViagem(@PathParam("nrCodigoBarras") String nrCodigoBarras, @Context HttpHeaders headers) {
        String token = headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0);
        MeioTransporte meioTransporte = descargaMobileScanService.findMeioTransporteByCodigoBarras(nrCodigoBarras, token);
        ControleCarga controleCarga = descargaMobileScanService.findControleCargaViagem(meioTransporte.getIdMeioTransporte(), token);
        return Response.ok(descargaMobileScanService.executeMeioTransportes(controleCarga.getTpControleCarga().getValue(), "N", meioTransporte, controleCarga, token)).build();
    }

    @POST
    @Path("/descargaVolume")
    public Response descargaVolume(DescargaVolumeDMN descargavolumeDMN, @Context HttpHeaders headers) {
        return Response.ok(descargaMobileScanService.storeDescargaVolume(descargavolumeDMN.getNrCodigoBarras(), descargavolumeDMN.getIdControleCarga(), headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/descargaDispositivo")
    public Response descargaDispositivo(DescargaDipositivoDMN descargaDispositivoDMN, @Context HttpHeaders headers){
        return Response.ok(descargaMobileScanService.storeDescargaDispositivo(descargaDispositivoDMN.getNrCodigoBarras(),
                descargaDispositivoDMN.getIdControleCarga(), headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();

    }

    @POST
    @Path("/descargaDispositivoConfirmacao")
    public Response descargaDispositivoConfirmacao(DescargaDispositivoConfirmacaoDMN descargaDispositivoConfirmacaoDMN, @Context HttpHeaders headers){
        return Response.ok(descargaMobileScanService.storeDescargaDispositivoConfirmacao(descargaDispositivoConfirmacaoDMN.getIdDispositivoUnitizacao(),
                descargaDispositivoConfirmacaoDMN.getIdControleCarga(), headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @GET
    @Path("/desunitizarDispositivo/{idDispositivoUnitizacao}")
    public Response desunitizarDispositivo(@PathParam("idDispositivoUnitizacao") Long idDispositivoUnitizacao, @Context HttpHeaders headers){
        descargaMobileScanService.desunitizaDispositivo(idDispositivoUnitizacao, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        return Response.ok().build();
    }
}

package com.mercurio.lms.rest.mobilescanapp;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.ConferirDispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.mobilescanapp.model.service.CarregamentoMobileScanEntregaService;
import com.mercurio.lms.mobilescanapp.model.service.CarregamentoMobileScanViagemService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.session.SessionKey;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("mobilescanapp/carregamento")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class CarregamentoMobileScanAppRest extends BaseRest {
    private static final String TP_CONTROLE_CARGA = "tpControleCarga";

    @InjectInJersey CarregamentoMobileScanViagemService carregamentoMobileScanAppViagemService;
    @InjectInJersey CarregamentoMobileScanEntregaService carregamentoMobileScanAppEntregaService;
    @InjectInJersey ConferirDispositivoUnitizacaoService conferirDispositivoUnitizacaoService;
    @InjectInJersey ConferirVolumeService conferirVolumeService;
    @InjectInJersey IntegracaoJwtService integracaoJwtService;
    @InjectInJersey PaisService paisService;
    @InjectInJersey MoedaService moedaService;
    @InjectInJersey HistoricoFilialService historicoFilialService;
    private static final String TIPO_CONTROLE_VIAGEM = "V";

    @GET
    @Path("/findMeioTransporte/{nrCodigoBarras}")
    public Response findMeioTransporte(@PathParam("nrCodigoBarras") Long nrCodigoBarras, @Context HttpHeaders headers) {
        criaSessao(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        try {
            Map<String, Object> transporte = carregamentoMobileScanAppViagemService.findMeioTransporteMobileScan(nrCodigoBarras);
            if (TIPO_CONTROLE_VIAGEM.equals(transporte.get(TP_CONTROLE_CARGA).toString())) {
                return Response.ok(carregamentoMobileScanAppViagemService.findControleCargaAberto(transporte)).build();
            }
            return Response.ok(carregamentoMobileScanAppEntregaService.findControleCargaAberto(transporte)).build();
        }finally {
            destroiSessao();
        }
    }

    @GET
    @Path("/executeGeraEventoDispositivoEncontrado/{idDispsitivoUnitizacao}")
    public Response executeGeraEventoDispositivoEncontrado(@PathParam("idDispsitivoUnitizacao") Long idDispsitivoUnitizacao,  @Context HttpHeaders headers) {
        criaSessao(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        try {
            return Response.ok(conferirDispositivoUnitizacaoService.executeGeraEventoDispositivoEncontrado(idDispsitivoUnitizacao)).build();
        }finally {
            destroiSessao();
        }
    }

    @GET
    @Path("/executeEventoDispositivoLido/{idDispsitivoUnitizacao}")
    public Response executeEventoDispositivoLido(@PathParam("idDispsitivoUnitizacao") Long idDispsitivoUnitizacao,  @Context HttpHeaders headers) {
        criaSessao(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        try {
            return Response.ok(conferirDispositivoUnitizacaoService.executeEventoDispositivoLido(idDispsitivoUnitizacao)).build();
        }finally {
            destroiSessao();
        }
    }

    @GET
    @Path("/executeGeraEventoVolumeEncontrado/{idVolumeNotaFiscal}")
    public Response executeGeraEventoVolumeEncontradoViagem(@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal,  @Context HttpHeaders headers) {
        criaSessao(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        try {
            return Response.ok(conferirVolumeService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal)).build();
        }finally {
            destroiSessao();
        }
    }

    @GET
    @Path("/executeEventoVolumeLido/{idVolumeNotaFiscal}")
    public Response executeEventoVolumeLidoViagem(@PathParam("idVolumeNotaFiscal") Long idVolumeNotaFiscal,  @Context HttpHeaders headers) {
        criaSessao(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
        try {
            return Response.ok(conferirVolumeService.executeEventoVolumeLido(idVolumeNotaFiscal)).build();
        }finally {
            destroiSessao();
        }
    }

    /**
     * metodos viagem
     */

    @POST
    @Path("/viagem/storeVolume")
    public Response storeVolumeViagem(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppViagemService.storeVolume(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/viagem/storeDispositivo")
    public Response storeDispositivoViagem(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppViagemService.storeDispositivo(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/viagem/deleteVolume")
    public Response deleteVolumeViagem(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppViagemService.deleteVolume(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/viagem/deleteDispositivo")
    public Response deleteDispositivoViagem(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppViagemService.deleteDispositivo(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @GET
    @Path("/viagem/findFiliaisDesvio/{idControleCarga}")
    public Response findFiliaisDesvio(@PathParam("idControleCarga") Long idControleCarga,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppViagemService.findFiliaisDesvio(idControleCarga, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    /**
     * Entrega
     * */

    @POST
    @Path("/entrega/storeVolume")
    public Response storeVolumeEntrega(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppEntregaService.storeVolume(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/entrega/storeDispositivo")
    public Response storeDispositivoEntrega(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppEntregaService.storeDispositivo(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/entrega/deleteVolume")
    public Response deleteVolumeEntrega(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppEntregaService.deleteVolume(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    @POST
    @Path("/entrega/deleteDispositivo")
    public Response deleteDispositivoEntrega(Map param,  @Context HttpHeaders headers) {
        return Response.ok(carregamentoMobileScanAppEntregaService.deleteDispositivo(param, headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0))).build();
    }

    private void criaSessao(String token){
        Filial filial =  integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(token));
        Usuario usuario = integracaoJwtService.getUsuarioSessaoByToken(token);
        Empresa empresa = integracaoJwtService.getEmpresaSessao(integracaoJwtService.getIdEmpresaByToken(token));
        SessionContext.setUser(usuario);
        SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
        SessionContext.set(SessionKey.FILIAL_KEY, filial);
        SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
        SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
        SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
    }

    private void destroiSessao(){
        SessionContext.remove(SessionKey.EMPRESA_KEY);
        SessionContext.remove(SessionKey.FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_DTZ);
        SessionContext.remove(SessionKey.MOEDA_KEY);
        SessionContext.remove(SessionKey.PAIS_KEY);
        SessionContext.remove("adsm.session.authenticatedUser");
        SessionContext.remove(SessionKey.ULT_HIST_FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_MATRIZ_KEY);
    }

    public void setCarregamentoMobileScanAppViagemService(CarregamentoMobileScanViagemService carregamentoMobileScanAppViagemService) {
        this.carregamentoMobileScanAppViagemService = carregamentoMobileScanAppViagemService;
    }

    public void setCarregamentoMobileScanAppEntregaService(CarregamentoMobileScanEntregaService carregamentoMobileScanAppEntregaService) {
        this.carregamentoMobileScanAppEntregaService = carregamentoMobileScanAppEntregaService;
    }

    public void setConferirDispositivoUnitizacaoService(ConferirDispositivoUnitizacaoService conferirDispositivoUnitizacaoService) {
        this.conferirDispositivoUnitizacaoService = conferirDispositivoUnitizacaoService;
    }

    public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
        this.conferirVolumeService = conferirVolumeService;
    }

    public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
        this.integracaoJwtService = integracaoJwtService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }
}

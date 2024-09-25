package com.mercurio.lms.rest.expedicao;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.edi.dto.*;
import com.mercurio.lms.expedicao.dto.*;
import com.mercurio.lms.expedicao.edi.model.service.ProcessarNotasEDIDadosGeraisService;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;


@Path("expedicao/conhecimento/processar/nota")
@Produces("application/json;charset=utf-8")
@Consumes("application/json;charset=utf-8")
public class ProcessarNotaEdiRest extends BaseRest {

    @InjectInJersey
    private ProcessarNotasEDIDadosGeraisService processarNotasEDIDadosGeraisService;


    @GET
    @Path("findDadosRemetente/{nrIdentificacao}/{idFilial}")
    public Response findDadosRemetente(@PathParam("nrIdentificacao") String nrIdentificacao,
                    @PathParam("idFilial") Long idFilial) throws Exception {
        DadosProcessamentoEdiDto dadosProcessamentoEdiDto = processarNotasEDIDadosGeraisService
                .findDadosGeraisCliente(nrIdentificacao, idFilial);

        return Response.ok(dadosProcessamentoEdiDto).build();
    }

    @POST
    @Path("findNotasFiscaisDoctoCliente")
    public Response findNotasFiscaisDoctoCliente(FiltroNotaFiscalDoctoClienteDto filtroNotaFiscalDoctoClienteDto) throws Exception {
        NotaFiscalDoctoClienteDto notaFiscalDoctoClienteDto = processarNotasEDIDadosGeraisService
                .findNotasFiscaisDoctoCliente(filtroNotaFiscalDoctoClienteDto);

        return Response.ok(notaFiscalDoctoClienteDto).build();

    }

    @GET
    @Path("doctoClienteDescricao/{idCliente}")
    public Response findInformacaoDoctoClienteDescription(@PathParam("idCliente") Long idCliente) {
        Map<String, String> retorno = processarNotasEDIDadosGeraisService
                .findInformacaoDoctoClienteDescription(idCliente);
        return Response.ok(retorno).build();
    }

    @POST
    @Path("execute-filtro-notas-fiscais")
    public Response executeFiltroNotasFiscais(RequestFiltroNotasFiscaisEdiDTO requestFiltroNotasFiscaisEdiDTO) {
        return Response.ok(processarNotasEDIDadosGeraisService.executeFiltroNotasFiscaisEdi(requestFiltroNotasFiscaisEdiDTO)).build();
    }

    @POST
    @Path("execute-prevalidacao-nota")
    public Response executePreValidacaoNota(List<DadosValidacaoEdiDTO> validarEdiDTO) {
        return Response.ok(processarNotasEDIDadosGeraisService.preValidarNotaFiscalEDIRest(validarEdiDTO)).build();
    }

    @PUT
    @Path("execute-ajustar-notas")
    public Response executeAjustarNotas(LogErroDTO logErroDTO) {
        processarNotasEDIDadosGeraisService.executeAjustarNotas(logErroDTO);
        return Response.ok().build();
    }

    @POST
    @Path("execute-prevalidacao-volume")
    public Response executePreValidacaoVolume(List<DadosValidacaoEdiDTO> validarEdiDTO) {
        return Response.ok(processarNotasEDIDadosGeraisService.executePreValidacaoVolume(validarEdiDTO)).build();
    }

    @POST
    @Path("find-notas-edi-ajuste")
    public Response findNotasEDIParaAjuste(ParamAjusteNotaDTO paramAjusteNotaDTO) {
        List<LogErrosEdiDTO> notasParaAjuste = processarNotasEDIDadosGeraisService.findNotasEDIParaAjuste(paramAjusteNotaDTO.getNaoTrazerLogsDoTipo(), paramAjusteNotaDTO.getNrProcessamento());
        return Response.ok(notasParaAjuste).build();
    }

    @PUT
    @Path("execute-ajustar-volume")
    public Response executeAjustarVolumes(LogErroDTO logErroDTO) {
        processarNotasEDIDadosGeraisService.executeAjustarVolumes(logErroDTO);
        return Response.ok().build();
    }

    @POST
    @Path("execute-recarregar-lista-valicadao")
    public Response executeRecarregarListaValicadao(List<DadosValidacaoEdiDTO> listaValidacao) {
        processarNotasEDIDadosGeraisService.executeRecarregarListaValicadao(listaValidacao);
        return Response.ok(listaValidacao).build();
    }

    @POST
    @Path("execute-validacao-item")
    public Response executeValidacaoItem(List<DadosValidacaoEdiDTO> validarEdiDTO) {
        return Response.ok(processarNotasEDIDadosGeraisService.executeValidacaoEdiItem(validarEdiDTO)).build();
    }

    @POST
    @Path("preparar-dados-agrupar-notas-fiscais")
    public Response prepararDadosAgruparNotasFiscaisEdi(ParamAgruparNotasFiscaisDTO paramAgruparNotasFiscaisDTO, @Context HttpHeaders headers) throws Exception {
        String token = headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0);
        this.processarNotasEDIDadosGeraisService.prepararDadosAgruparNotasFiscaisEdi(paramAgruparNotasFiscaisDTO, token);
        return Response.ok().build();
    }

    @POST
    @Path("verificar-pendencia-atualizacao")
    public Response verificarPendenciaAtualizacao(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {
        DocumentoPorNotaFiscalDto documentoPorNotaFiscalDto =
                processarNotasEDIDadosGeraisService.executeValidatePendenciaAtualizacao(requestFiltroProcessoPorNotaFiscalDTO);
        return Response.ok(documentoPorNotaFiscalDto).build();
    }

    @POST
    @Path("validate-responsavel-etiqueta-por-volume")
    public Response validateResponsavelEtiquetaPorVolume
            (ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto) {

        ResponsavelEtiquetaDto responsavelEtiquetaDto = processarNotasEDIDadosGeraisService
                .validateResponsavelEtiquetaPorVolume(validateResponsavelEtiquetaDto);
        return Response.ok(responsavelEtiquetaDto).build();

    }

    @POST
    @Path("validate-responsavel-paleteFechado")
    public Response validateResponsavelPaleteFechado(ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto) {
        ResponsavelEtiquetaDto responsavelEtiquetaDto = processarNotasEDIDadosGeraisService
                .validateResponsavelPaleteFechado(validateResponsavelEtiquetaDto);
        return Response.ok(responsavelEtiquetaDto).build();
    }

    @GET
    @Path("validate-cliente-sem-intervalo-etiqueta/{idCliente}")
    public Response validateClienteSemIntervaloEtiqueta(@PathParam("idCliente") Long idCliente) {
        Map<String, Object> result = processarNotasEDIDadosGeraisService.validateClienteSemIntervaloEtiqueta(idCliente);
        return Response.ok(result).build();
    }

    @POST
    @Path("verificar-pendencia-atualizacao-intervalo-nota")
    public Response verificarPendenciaAtualizacaoIntervaloNota(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {
        List<DocumentoPorNotaFiscalDto> documentoPorNotaFiscalDto =
                processarNotasEDIDadosGeraisService.executeValidatePendenciaAtualizacaoIntervaloNota(requestFiltroProcessoPorNotaFiscalDTO);
        return Response.ok(documentoPorNotaFiscalDto).build();
    }

    public void setProcessarNotasEDIDadosGeraisService(ProcessarNotasEDIDadosGeraisService processarNotasEDIDadosGeraisService) {
        this.processarNotasEDIDadosGeraisService = processarNotasEDIDadosGeraisService;
    }
}

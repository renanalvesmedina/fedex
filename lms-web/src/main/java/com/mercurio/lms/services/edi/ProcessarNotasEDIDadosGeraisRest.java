package com.mercurio.lms.services.edi;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.edi.dto.AgruparNotaDTO;
import com.mercurio.lms.edi.dto.ProcessamentoAgrupamentoDTO;
import com.mercurio.lms.expedicao.dto.ConhecimentoSemPesagemDto;
import com.mercurio.lms.expedicao.dto.ErroAgruparNotaFiscalDto;
import com.mercurio.lms.expedicao.dto.ProcessaNotasEdiItemDto;
import com.mercurio.lms.expedicao.dto.StoreLogEdiDto;
import com.mercurio.lms.expedicao.edi.model.service.ProcessarNotasEDIDadosGeraisService;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("processar/notas/edi")
@Consumes("application/json;charset=utf-8")
@Produces("application/json;charset=utf-8")
public class ProcessarNotasEDIDadosGeraisRest extends BaseRest {

    @InjectInJersey
    private ProcessarNotasEDIDadosGeraisService processarNotasEDIDadosGeraisService;

    @POST
    @Path("execute-open-monitoramento-descarga")
    public Response executeOpenMonitoramentoDescarga(AgruparNotaDTO agruparNotaDTO) throws Exception {
        processarNotasEDIDadosGeraisService.executeOpenMonitoramentoDescarga(agruparNotaDTO.getConhecimento(), agruparNotaDTO.getMapMeioTransporte(), agruparNotaDTO.getNrProcessamento());
        return Response.ok(agruparNotaDTO).build();
    }

    @POST
    @Path("execute-processar-notas-fiscais-item")
    public Response executeProcessarNotasFiscaisItem(ProcessaNotasEdiItemDto processaNotasEdiItem) throws Exception {
        processarNotasEDIDadosGeraisService.executeProcessarNotasFiscaisEdiItem(processaNotasEdiItem);
        return Response.ok().build();
    }

    @POST
    @Path("finaliza-conhecimento-sem-pesagem")
    public Response finalizaConhecimentoSemPesagem(ConhecimentoSemPesagemDto conhecimentoSemPesagemDto) throws Exception {
        processarNotasEDIDadosGeraisService.finalizaConhecimentoSemPesagem(conhecimentoSemPesagemDto);
        return Response.ok().build();
    }

    @POST
    @Path("remove-registros-processados")
    public Response removeRegistrosProcessados(ProcessaNotasEdiItemDto processaNotasEdiItemDto){
        processarNotasEDIDadosGeraisService.removeRegistrosProcessados(processaNotasEdiItemDto);
        return Response.ok().build();
    }

    @POST
    @Path("find-conhecimento-sem-pesagem")
    public Response findConhecimentosSemPesagem(ProcessaNotasEdiItemDto processaNotasEdiItem){
        processarNotasEDIDadosGeraisService.findConhecimentosSemPesagem(processaNotasEdiItem);
        return Response.ok().build();
    }

    @POST
    @Path("finaliza-processamento-edi")
    public Response finalizaProcessamentoEDI(ProcessaNotasEdiItemDto processaNotasEdiItemDto){
        this.processarNotasEDIDadosGeraisService.finalizaProcessamentoEDI(processaNotasEdiItemDto);
        return Response.ok().build();
    }

    @POST
    @Path("store-log-edi")
    public Response storeLogEdi(StoreLogEdiDto storeLogEdiDto){
        this.processarNotasEDIDadosGeraisService.updateQtNotasProcessadasProcessamentoEdi(storeLogEdiDto);
        return Response.ok().build();
    }

    @POST
    @Path("execute-preparar-agrupar-notas-fiscais")
    public Response executePrepararAgruparNotasFiscais(ProcessamentoAgrupamentoDTO processamentoAgrupamentoDTO) {
        Response response = Response.ok().build();
        try {
            this.processarNotasEDIDadosGeraisService.executePrepararAgruparNotasFiscaisEdi(processamentoAgrupamentoDTO.getNrProcessamento());
        } catch (Exception e) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;
    }

    @POST
    @Path("store-log-processamento-edi")
    public Response storeLogProcessamentoEdi(ErroAgruparNotaFiscalDto erroAgruparNotaFiscalDto)  {
        Response response = Response.ok().build();
        try {
            this.processarNotasEDIDadosGeraisService.storeLogProcessamentoEdi(erroAgruparNotaFiscalDto);
        }catch (Exception e) {
            response = Response.status(Response.Status.BAD_REQUEST).build();
        }

        return response;

    }
}

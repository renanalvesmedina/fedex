package com.mercurio.lms.expedicao.edi.action;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.dto.ParameterDto;
import com.mercurio.lms.expedicao.dto.ProcessaNotasEdiItemDto;
import com.mercurio.lms.expedicao.edi.model.service.ProcessamentoEdiService;
import com.mercurio.lms.expedicao.edi.model.service.ProcessarNotaFiscalEDIService;
import com.mercurio.lms.expedicao.edi.model.service.ProcessarNotasEDIDadosGeraisService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDescargaService;
import com.mercurio.lms.expedicao.model.service.ProcessamentoIbEdiService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Assynchronous

public class ProcessarNotasEDIDadosGeraisAction extends CrudAction {

    private ProcessarNotasEDIDadosGeraisService processarNotasEDIDadosGeraisService;
    private ProcessamentoIbEdiService processamentoIbEdiService;
    private MonitoramentoDescargaService monitoramentoDescargaService;
    private IntegracaoJmsService integracaoJmsService;
    private ProcessamentoEdiService processamentoEdiService;
    private ProcessarNotaFiscalEDIService processarNotaFiscalEDIService;

    @AssynchronousMethod( name = "ProcessarNotasEDIDadosGerais.finalizaProcessamentoEDI", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
    public void finalizaProcessamentoEDI(){
            List<Object[]> monitoramentoDescargaList = this.monitoramentoDescargaService.findMonitoramentosConcluidos();
            this.executarProcessaNotasEdiItem(monitoramentoDescargaList);
    }

    private synchronized void executarProcessaNotasEdiItem(List<Object[]> monitoramentoDescargaList) {
        if (monitoramentoDescargaList != null && !monitoramentoDescargaList.isEmpty()) {
            monitoramentoDescargaList.forEach(m -> {
                try {
                    processaNotasEdiItem(m);
                }catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            });

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    protected void processaNotasEdiItem(Object[] monitoramentoDescarga) throws IOException {

        String nrProcessamento = monitoramentoDescarga[0].toString();
        ProcessaNotasEdiItemDto processaNotasEdiItemDto = this.processamentoIbEdiService
            .findProcessaNotasEdiItemDto(nrProcessamento);

        if (processaNotasEdiItemDto != null) {

            ParameterDto parameterDto = processaNotasEdiItemDto.getParameters();
            processaNotasEdiItemDto.setIdMonitoramentoDescarga(Long.parseLong(monitoramentoDescarga[1].toString()));
            processaNotasEdiItemDto.setIdPedidoColeta(parameterDto.getIdPedidoColeta());
            processaNotasEdiItemDto.setTpProcessamento(parameterDto.getTpProcessamento());

            IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
                    .createMessage(Queues.CONHECIMENTO_SEM_PESAGEM, processaNotasEdiItemDto);

            integracaoJmsService.storeMessage(jmsMessageSender);
            processamentoEdiService.updateTpStatus(nrProcessamento, new DomainValue("PJ"));
        }

    }

    public ProcessarNotasEDIDadosGeraisService getProcessarNotasEDIDadosGeraisService() {
        return processarNotasEDIDadosGeraisService;
    }

    public void setProcessarNotasEDIDadosGeraisService(ProcessarNotasEDIDadosGeraisService processarNotasEDIDadosGeraisService) {
        this.processarNotasEDIDadosGeraisService = processarNotasEDIDadosGeraisService;
    }

    public ProcessamentoIbEdiService getProcessamentoIbEdiService() {
        return processamentoIbEdiService;
    }

    public void setProcessamentoIbEdiService(ProcessamentoIbEdiService processamentoIbEdiService) {
        this.processamentoIbEdiService = processamentoIbEdiService;
    }

    public MonitoramentoDescargaService getMonitoramentoDescargaService() {
        return monitoramentoDescargaService;
    }

    public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
        this.monitoramentoDescargaService = monitoramentoDescargaService;
    }

    public IntegracaoJmsService getIntegracaoJmsService() {
        return integracaoJmsService;
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }

    public void setProcessamentoEdiService(ProcessamentoEdiService processamentoEdiService) {
        this.processamentoEdiService = processamentoEdiService;
    }

    public void setProcessarNotaFiscalEDIService(ProcessarNotaFiscalEDIService processarNotaFiscalEDIService) {
        this.processarNotaFiscalEDIService = processarNotaFiscalEDIService;
    }
}

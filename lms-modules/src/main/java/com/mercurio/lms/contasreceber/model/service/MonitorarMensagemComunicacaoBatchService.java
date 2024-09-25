package com.mercurio.lms.contasreceber.model.service;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.Email;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemDAO;

public class MonitorarMensagemComunicacaoBatchService extends CrudService<MonitoramentoMensagem, Long> {

	private MonitoramentoMensagemDAO monitoramentoMensagemDao;
	private MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService;
	private MonitoramentoMensagemService monitoramentoMensagemService;
	private MensagemComunicacaoService mensagemComunicacaoService;
	
	public void storeMonitoramentoMensagem(MonitoramentoMensagem monitoramento, Email emailFaturamento) {
		if (emailFaturamento == null) {
			
			return;
		}
		if (emailFaturamento.getpCorpo() != null){
			monitoramentoMensagemService.saveEventoMensagem(monitoramento.getIdMonitoramentoMensagem(), "P", "LMS-36300");
			monitoramentoMensagemService.enviaMensagemComunicacao(monitoramento, emailFaturamento);
			MonitoramentoMensagemDetalhe monitoramentoMensagemDetalhe = monitoramentoMensagemDetalheService.findByIdMonitoramentoMensagem(monitoramento.getIdMonitoramentoMensagem());
			monitoramentoMensagemDetalhe.setMonitoramentoMensagem(monitoramento);
			monitoramentoMensagemDetalhe.setDcMensagem(emailFaturamento.getpCorpo());
			monitoramentoMensagemDetalheService.store(monitoramentoMensagemDetalhe);
		}
		monitoramento = monitoramentoMensagemDao.findByIdMonitoramentoMensagem(monitoramento.getIdMonitoramentoMensagem());
		monitoramento.setDhProcessamento(new DateTime());
		monitoramentoMensagemDao.store(monitoramento);
	}
	
	
	private void executeStoreMonitoramentoMensagem(MonitoramentoMensagem monitoramento) {
        Email emailFaturamento  = null;
        if ("FA".equalsIgnoreCase(monitoramento.getTpModeloMensagem().getValue())) {
            emailFaturamento = monitoramentoMensagemService.executeCreateEmailMensagenFaturamento(monitoramento);
        } else if ("CO".equalsIgnoreCase(monitoramento.getTpModeloMensagem().getValue())){
            emailFaturamento = monitoramentoMensagemService.executeCreateEmailMensagenCobranca(monitoramento);
        } else if ("CP".equalsIgnoreCase(monitoramento.getTpModeloMensagem().getValue())){
            emailFaturamento = monitoramentoMensagemService.executeCreateEmailMensagenFaturamento(monitoramento);
        } else if ("GL".equalsIgnoreCase(monitoramento.getTpModeloMensagem().getValue())){
            emailFaturamento = monitoramentoMensagemService.executeCreateEmailMensagenCobrancaTerceira(monitoramento);
        } else {
            emailFaturamento = mensagemComunicacaoService.executeMontaMensagemGenerica(monitoramento);
        }
        storeMonitoramentoMensagem(monitoramento,emailFaturamento);
    }
	
	
	public void setMonitoramentoMensagemDao(MonitoramentoMensagemDAO monitoramentoMensagemDao) {
		this.monitoramentoMensagemDao = monitoramentoMensagemDao;
	}
	public void setMonitoramentoMensagemDetalheService(MonitoramentoMensagemDetalheService monitoramentoMensagemDetalheService) {
		this.monitoramentoMensagemDetalheService = monitoramentoMensagemDetalheService;
	}
	
	public void setMonitoramentoMensagemService(MonitoramentoMensagemService monitoramentoMensagemService) {
		this.monitoramentoMensagemService = monitoramentoMensagemService;
	}

    public void setMensagemComunicacaoService(MensagemComunicacaoService mensagemComunicacaoService) {
        this.mensagemComunicacaoService = mensagemComunicacaoService;
    }
}

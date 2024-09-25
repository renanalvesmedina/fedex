package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchTransactionType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.security.model.service.AuthenticationService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Email;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemDAO;

@Assynchronous
public class MonitorarMensagemComunicacaoService extends CrudService<MonitoramentoMensagem, Long> {

	
	private MonitoramentoMensagemDAO monitoramentoMensagemDao;
	private MonitoramentoMensagemService monitoramentoMensagemService;
	private MensagemComunicacaoService mensagemComunicacaoService;
	private MonitorarMensagemComunicacaoBatchService monitorarMensagemComunicacaoBatchService;
	private AuthenticationService authenticationService;
	private ParametroGeralService parametroGeralService;
	
	
	public void setMonitoramentoMensagemService(MonitoramentoMensagemService monitoramentoMensagemService) {
		this.monitoramentoMensagemService = monitoramentoMensagemService;
	}
	
	public void setMensagemComunicacaoService(MensagemComunicacaoService mensagemComunicacaoService) {
		this.mensagemComunicacaoService = mensagemComunicacaoService;
	}
	
	public void setMonitoramentoMensagemDao(MonitoramentoMensagemDAO monitoramentoMensagemDao) {
		this.monitoramentoMensagemDao = monitoramentoMensagemDao;
		setDao(this.monitoramentoMensagemDao);
	}

	@AssynchronousMethod(name = "contasreceber.monitorarMensagemComunicacao", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR, transaction=BatchTransactionType.NON_TRANSACTIONAL)	
	public void executaMonitoraMensagemComunicacao() {
		executaMensagemComunicacao();
	}
	
	public void executaMonitoraMensagemComunicacaoTeste() {
		executaMensagemComunicacao();
	}
	
	void executaMensagemComunicacao() {
		List<MonitoramentoMensagem> obj = monitoramentoMensagemService.findByDhProcessamentoNull();
		if ( obj.isEmpty() ){
			return;
		}
		
		for(MonitoramentoMensagem monitoramento : obj){
		    executeStoreMonitoramentoMensagem(monitoramento);
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void executeStoreMonitoramentoMensagem(MonitoramentoMensagem monitoramento) {
	    
	    loginUsuarioFatura();
	    
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
        monitorarMensagemComunicacaoBatchService.storeMonitoramentoMensagem(monitoramento,emailFaturamento);
    }

	public void loginUsuarioFatura() {
		String login = (String)parametroGeralService.findConteudoByNomeParametro("USUARIO_ENVIO_FATURAS", false);
	    authenticationService.loginAsSystem(login);
	}

    public void setMonitorarMensagemComunicacaoBatchService(MonitorarMensagemComunicacaoBatchService monitorarMensagemComunicacaoBatchService) {
		this.monitorarMensagemComunicacaoBatchService = monitorarMensagemComunicacaoBatchService;
	}

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }
	
}

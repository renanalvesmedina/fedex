package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.edi.model.LogAtualizacaoEDI;
import com.mercurio.lms.edi.model.dao.LogAtualizacaoEDIDAO;
import com.mercurio.lms.vendas.model.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logAtualizacaoEDIService"
 */

public class LogAtualizacaoEDIService extends CrudService<LogAtualizacaoEDI, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private ConfiguracoesFacade configuracoesFacade;
	
	
	public void storeLog(Integer nrNotaFiscal, Long nrProcessamento, Cliente clienteRemetente, String tpCliente, String erro) {
		try{
			LogAtualizacaoEDI logAtualizacaoEDI = new LogAtualizacaoEDI();
			logAtualizacaoEDI.setNrNotaFiscal(nrNotaFiscal);
			logAtualizacaoEDI.setClienteRemetente(clienteRemetente);
			logAtualizacaoEDI.setDsMensagemErro(erro.length() > 200 ? erro.substring(0, 200) : erro);
			logAtualizacaoEDI.setNrProcessamento(nrProcessamento);
			store(logAtualizacaoEDI);
		}catch (Exception e) {
			log.error(e);
		}
	}
	
	public void storeLog(Integer nrNotaFiscal, Long nrProcessamento, Cliente clienteRemetente, String tpCliente, BusinessException e) {
		String msg = e.getMessageKey() + " - " + configuracoesFacade.getMensagem(e.getMessageKey(), e.getMessageArguments());
		storeLog(nrNotaFiscal, nrProcessamento, clienteRemetente, tpCliente, msg, false);
	}
	
	public void storeLog(Integer nrNotaFiscal, Long nrProcessamento, Cliente clienteRemetente, String tpCliente, String msg, boolean isMsgKey) {
		if (isMsgKey) {
			msg = msg + " - " + configuracoesFacade.getMensagem(msg);
		}
		if(msg == null) {
			msg = "null";
		}
		storeLog(nrNotaFiscal, nrProcessamento, clienteRemetente, tpCliente, msg);
	}
	
	@Override
	public LogAtualizacaoEDI findById(Long id) {		
		return (LogAtualizacaoEDI)super.findById(id);
	}	
	
	@Override
	public Serializable store(LogAtualizacaoEDI logAtualizacaoEDI) {
		return super.store(logAtualizacaoEDI);
	}
	
	public List findByNrNotaFiscal(String nrNotaFiscal) {
		return getLogAtualizacaoEDIDAO().findByNrNotaFiscal(nrNotaFiscal);
	}

	public List findByIdClienteRemetente(Long idClienteRemente) {
		return getLogAtualizacaoEDIDAO().findByIdClienteRemetente(idClienteRemente);
	}

	public List findByDistinctNrProcessamentoByIdClienteRemetente(Long idClienteRemente) {
		return getLogAtualizacaoEDIDAO().findByIdClienteRemetente(idClienteRemente);
	}

	public List findByNrProcessamento(Long nrProcessamento) {
		return getLogAtualizacaoEDIDAO().findByNrProcessamento(nrProcessamento);
	}
	
	public void removeByNrProcessamento(Long nrProcessamento) {
		getLogAtualizacaoEDIDAO().removeByNrProcessamento(nrProcessamento);
	}
	
	private LogAtualizacaoEDIDAO getLogAtualizacaoEDIDAO() {
        return (LogAtualizacaoEDIDAO) getDao();
    }
    
    public void setLogAtualizacaoEDIDAO(LogAtualizacaoEDIDAO dao) {
        setDao(dao);
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	
}

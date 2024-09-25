package com.mercurio.lms.facade.reajuste.cliente;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.tabelaprecos.model.CloneClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.ReajusteClienteAutomaticoDTO;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteParametroClienteDispatcherService;
import com.mercurio.lms.tabelaprecos.model.service.ReajusteParametroClienteService;

@ServiceSecurity
public class ReajusteDeClienteFacade {
	
	private static final String ATUALIZACAO_DIVISOES_E_HISTORICO_INICIALIZADO = "ATUALIZACAODIVISOES E HISTORICO INICIALIZADO!";
	private static final String ATUALIZACAO_DIVISOES_E_HISTORICO_FINALIZADO   = "ATUALIZACAODIVISOES E HISTORICO FINALIZADO!";
	private static final String PROCESSO_DE_CLONAGEM_INICIALIZADO   = "PROCESSO DE CLONAGEM INICIALIZADO - idTabelaDivisaoCliente: ";
	private static final String PROCESSO_DE_CLONAGEM_FINALIZADO     = "PROCESSO DE CLONAGEM FINALIZADO - idTabelaDivisaoCliente: ";
	private static final String PROCESSO_DE_REAJUSTE_INICIALIZADO   = "PROCESSO DE REAJUSTE INICIALIZADO - idTabelaDivisaoCliente: ";
	private static final String PROCESSO_DE_REAJUSTE_FINALIZADO     = "PROCESSO DE REAJUSTE FINALIZADO - idTabelaDivisaoCliente: ";
	private static final String FIND_DIVISOES_CLONAR_NOVO_REAJUSTE_INICIALIZADO   = "FIND DIVISOES CLIENTES PARA CLONAR NOVO REAJUSTE INICIALIZADO!";
	private static final String FIND_DIVISOES_CLONAR_INICIALIZADO   = "FIND DIVISOES CLIENTES PARA CLONAR INICIALIZADO!";
	private static final String FIND_DIVISOES_CLONAR_FINALIZADO     = "FIND DIVISOES CLIENTES PARA CLONAR FINALIZADO!";
	private static final String FIND_DIVISOES_CLONAR_NOVO_REAJUSTE_FINALIZADO     = "FIND DIVISOES CLIENTES PARA CLONAR NOVO REAJUSTE FINALIZADO!";
	private static final String FIND_DIVISOES_REAJUSTE_INICIALIZADO = "FIND DIVISOES CLIENTES PARA REAJUSTAR INICIALIZADO!";
	private static final String FIND_DIVISOES_REAJUSTE_FINALIZADO   = "FIND DIVISOES CLIENTES PARA REAJUSTAR FINALIZADO!";
	private static final String ERRO_REAJUSTE_PARAMETROS = "ReajustarClienteAutomatico - IdTabelaDivisaoCliente: ";
	private static final String ERRO_CLONAGEM_PARAMETROS = "CloneParametroCliente - IdTabelaDivisaoCliente: ";
	
	private ReajusteParametroClienteService reajusteParametroClienteService;
	
	private ReajusteParametroClienteDispatcherService reajusteParametroClienteDispatcherService;
	
	private static final Log LOG = LogFactory.getLog(ReajusteDeClienteFacade.class);
	
	@MethodSecurity(processGroup = "reajuste.cliente", processName = "findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste", authenticationRequired=false)
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste(){
		LOG.info(FIND_DIVISOES_CLONAR_NOVO_REAJUSTE_INICIALIZADO);
		List<CloneClienteAutomaticoDTO> list =  reajusteParametroClienteService.findTabelasDivisaoClienteAutomaticosParaClonarNovoReajuste();
		LOG.info(FIND_DIVISOES_CLONAR_NOVO_REAJUSTE_FINALIZADO);
		
		return list;
	}
	
	@MethodSecurity(processGroup = "reajuste.cliente", processName = "cloneParametroCliente", authenticationRequired=false)
	public boolean cloneParametroCliente(CloneClienteAutomaticoDTO reajusteClienteAutomaticoDTO){
		try{
			LOG.info(PROCESSO_DE_CLONAGEM_INICIALIZADO + reajusteClienteAutomaticoDTO.getIdTabelaDivisaoCliente());
			reajusteParametroClienteService.executeClone(reajusteClienteAutomaticoDTO);
			LOG.info(PROCESSO_DE_CLONAGEM_FINALIZADO + reajusteClienteAutomaticoDTO.getIdTabelaDivisaoCliente());
		}catch(Exception e){
			LOG.error(ERRO_CLONAGEM_PARAMETROS + reajusteClienteAutomaticoDTO.getIdTabelaDivisaoCliente(), e);
			return Boolean.FALSE; 
		}
		
		return Boolean.TRUE; 
	}

	@MethodSecurity(processGroup = "reajuste.cliente", processName = "findTabelasDivisaoClienteAutomaticosParaClonar", authenticationRequired=false)
	public List<CloneClienteAutomaticoDTO> findTabelasDivisaoClienteAutomaticosParaClonar(){
		LOG.info(FIND_DIVISOES_CLONAR_INICIALIZADO);
		List<CloneClienteAutomaticoDTO> list =  reajusteParametroClienteService.findTabelasDivisaoClienteAutomaticosParaClonar();
		LOG.info(FIND_DIVISOES_CLONAR_FINALIZADO);
		
		return list;
	}
	
	@MethodSecurity(processGroup = "reajuste.cliente", processName = "findTabelasDivisaoClienteAutomaticosParaReajustar", authenticationRequired=false)
	public List<Long> findTabelasDivisaoClienteAutomaticosParaReajustar(){
		LOG.info(FIND_DIVISOES_REAJUSTE_INICIALIZADO);
		List<Long> list = reajusteParametroClienteService.findTabelasDivisaoClienteAutomaticosParaReajustar();
		LOG.info(FIND_DIVISOES_REAJUSTE_FINALIZADO);
		
		return list;
	}

	@MethodSecurity(processGroup = "reajuste.cliente", processName = "reajustarClienteAutomatico", authenticationRequired=false)
	public boolean reajustarClienteAutomatico(ReajusteClienteAutomaticoDTO parametroCliente) {
		try{
			LOG.info(PROCESSO_DE_REAJUSTE_INICIALIZADO + parametroCliente.getIdTabelaDivisaoCliente());
			reajusteParametroClienteService.executeReajustarClienteAutomatico(parametroCliente, false);
			LOG.info(PROCESSO_DE_REAJUSTE_FINALIZADO + parametroCliente.getIdTabelaDivisaoCliente());
		}catch(Exception e){
			LOG.error(ERRO_REAJUSTE_PARAMETROS + parametroCliente.getIdTabelaDivisaoCliente(), e);
			return Boolean.FALSE; 
		}
		
		return Boolean.TRUE; 
	}

	@MethodSecurity(processGroup = "reajuste.cliente", processName = "reajustarClienteAutomatico", authenticationRequired=false)
	public boolean reajustarClientesAutomaticos() {
		return reajusteParametroClienteDispatcherService.reajustarClientesAutomaticos();
	}

	@MethodSecurity(processGroup = "reajuste.cliente", processName = "reajustarClientesAutomaticosNovoReajuste", authenticationRequired=false)
	public boolean reajustarClientesAutomaticosNovoReajuste() throws InterruptedException, ExecutionException {
		return reajusteParametroClienteDispatcherService.reajustarClientesAutomaticosNovoReajuste();
	}
	
	@MethodSecurity(processGroup = "reajuste.cliente", processName = "atualizarTabelasDivisaoEHistoricoReajuste", authenticationRequired=false)
	public boolean atualizarTabelasDivisaoEHistoricoReajuste(boolean isNovoReajuste) {
		LOG.info(ATUALIZACAO_DIVISOES_E_HISTORICO_INICIALIZADO);
		reajusteParametroClienteService.updateTabelasDivisaoEHistoricoReajuste(isNovoReajuste);
		LOG.info(ATUALIZACAO_DIVISOES_E_HISTORICO_FINALIZADO);
		
		return Boolean.TRUE; 
	}
	
	public void setReajusteParametroClienteService(ReajusteParametroClienteService reajusteParametroClienteService) {
		this.reajusteParametroClienteService = reajusteParametroClienteService;
	}
	
	public void setReajusteParametroClienteDispatcherService(
			ReajusteParametroClienteDispatcherService reajusteParametroClienteDispatcherService) {
		this.reajusteParametroClienteDispatcherService = reajusteParametroClienteDispatcherService;
	}
}

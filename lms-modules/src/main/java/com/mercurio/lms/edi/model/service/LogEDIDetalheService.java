package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts.taglib.tiles.GetAttributeTag;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.LogEDIItem;
import com.mercurio.lms.edi.model.LogEDIVolume;
import com.mercurio.lms.edi.model.dao.LogEDIDetalheDAO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.logEDIDetalheService"
 */

public class LogEDIDetalheService extends CrudService<LogEDIDetalhe, Long> {

	private  LogEDIItemService logEDIItemService;
	private  LogEDIVolumeService logEDIVolumeService;
	private  LogEDIComplementoService logEDIComplementoService;
	private  ConfiguracoesFacade configuracoesFacade;
	private  ClienteService clienteService;

	@Override
	public LogEDIDetalhe findById(Long id) {		
		return (LogEDIDetalhe)super.findById(id);
	}
	
	public ResultSetPage findPaginatedLogDetalhe(TypedFlatMap criteria) {	
		ResultSetPage rsp = getLogEDIDetalheDAO().findPaginatedLogDetalhe(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}
	public Integer getRowCountLogDetalhe(TypedFlatMap criteria) {	
		return getLogEDIDetalheDAO().getRowCountLogDetalhe(criteria);
	}
	
	@Override
	public Serializable store(LogEDIDetalhe logEDIDetalhe) {
		if(logEDIDetalhe.getIdLogEdiDetalhe() == null){
			logEDIDetalhe.setIdLogEdiDetalhe(getLogEDIDetalheDAO().findSequence());
		}
		Long idArquivoDetalhe = (Long)super.store(logEDIDetalhe);
		logEDIDetalhe.setIdLogEdiDetalhe(idArquivoDetalhe);
		for (LogEDIItem logEDIItem : logEDIDetalhe.getLogItens()) {		
			logEDIItem.setLogEDIDetalhe(logEDIDetalhe);
			this.logEDIItemService.store(logEDIItem);
		}
		for (LogEDIComplemento logEDIComplemento : logEDIDetalhe.getLogComplementos()) {
			logEDIComplemento.setLogEDIDetalhe(logEDIDetalhe);
			this.logEDIComplementoService.store(logEDIComplemento);
		}
		for (LogEDIVolume logEDIVolume : logEDIDetalhe.getLogVolumes()) {
			logEDIVolume.setLogEDIDetalhe(logEDIDetalhe);
			this.logEDIVolumeService.store(logEDIVolume);
		}
		return idArquivoDetalhe;
	}
	
	
	public Serializable storeLogEdiDetalhe(LogEDIDetalhe logEDIDetalhe){
		return super.store(logEDIDetalhe);
	}
	
	public LogEDIDetalhe findByCnpjRemeNrNota(Long cnpjReme, Integer nrNotaFiscal){
		return getLogEDIDetalheDAO().findByCnpjRemeNrNota(cnpjReme, nrNotaFiscal);
	}
	
	public List<LogEDIDetalhe> findByNrCCEReprocessamento(Long nrCce, String cnpjReme){
		return getLogEDIDetalheDAO().findByNrCCEReprocessamento(nrCce, cnpjReme);
	}
	
	private LogEDIDetalheDAO getLogEDIDetalheDAO() {
        return (LogEDIDetalheDAO) getDao();
    }
    
    public void setLogEDIDetalheDAO(LogEDIDetalheDAO dao) {
        setDao(dao);
    }

	public LogEDIItemService getLogEDIItemService() {
		return logEDIItemService;
	}

	public void setLogEDIItemService(LogEDIItemService logEDIItemService) {
		this.logEDIItemService = logEDIItemService;
	}

	public LogEDIVolumeService getLogEDIVolumeService() {
		return logEDIVolumeService;
	}

	public void setLogEDIVolumeService(LogEDIVolumeService logEDIVolumeService) {
		this.logEDIVolumeService = logEDIVolumeService;
	}

	public LogEDIComplementoService getLogEDIComplementoService() {
		return logEDIComplementoService;
	}

	public void setLogEDIComplementoService(LogEDIComplementoService logEDIComplementoService) {
		this.logEDIComplementoService = logEDIComplementoService;
	}

	public LogEDIDetalhe findByNrNotaFiscalReprocessamento(String nrIdentificacao,
			Integer nrNotaFiscal) {
		return getLogEDIDetalheDAO().findByNrNotaFiscalReprocessamento(nrIdentificacao,nrNotaFiscal);
	}
	
	public List<LogEDIDetalhe> findByDoctoClienteReprocessamento(Long idCliente, String nrDoctoCliente, String tpProcessamento){
		
		final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
		final InformacaoDoctoCliente informacaoDoctoClienteEDI = cliente.getInformacaoDoctoClienteEDI();
		 
		BigDecimal maxDiasLog = (BigDecimal)configuracoesFacade.getValorParametro("MAX_DIAS_LOG_EDI");
		return getLogEDIDetalheDAO().findByDoctoClienteReprocessamento(cliente.getPessoa().getNrIdentificacao(),
				informacaoDoctoClienteEDI.getDsCampo(),nrDoctoCliente, tpProcessamento, maxDiasLog.longValue());
	}

	public List<LogEDIDetalhe> findByNrNotaFiscalReprocessamento
	(String nrIdentificacao, Integer nrNotaFiscalInicial, Integer nrNotaFiscalFinal) {
		return getLogEDIDetalheDAO()
				.findByNrNotaFiscalReprocessamento(nrIdentificacao,nrNotaFiscalInicial, nrNotaFiscalFinal);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public LogEDIDetalhe findByIdNotaFiscalEDI(Long idNotaFiscalEdi) {
		// TODO Auto-generated method stub
		return getLogEDIDetalheDAO().findByIdNotaFiscalEDI(idNotaFiscalEdi);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
    
}

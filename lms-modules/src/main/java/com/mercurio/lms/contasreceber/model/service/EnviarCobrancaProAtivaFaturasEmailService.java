package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.FaturaEmailPojo;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.contasreceber.model.dao.BoletoDAO;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemDAO;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemEventoDAO;
import com.mercurio.lms.contasreceber.model.dao.MonitoramentoMensagemFaturaDAO;

@Assynchronous
public class EnviarCobrancaProAtivaFaturasEmailService extends CrudService<Fatura, Long> {

	private FaturaDAO faturaDao;
	private MonitoramentoMensagemDAO monitoramentoMensagemDao;
	private MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDao;
	private BoletoDAO boletoDao;
	private MonitoramentoMensagemEventoDAO monitoramentoEventoDao;
	private ConfiguracoesFacade configuracoesFacade;
	
	private HistoricoBoletoService historicoBoletoService;
	
	public void setFaturaDao(FaturaDAO faturaDao) {
		this.faturaDao = faturaDao;
		setDao(faturaDao);
	}

	public FaturaDAO getFaturaDao() {
		return faturaDao;
	}
	
	public void setMonitoramentoEventoDao(MonitoramentoMensagemEventoDAO monitoramentoEventoDao) {
		this.monitoramentoEventoDao = monitoramentoEventoDao;
	}
	
	public MonitoramentoMensagemEventoDAO getMonitoramentoEventoDao() {
		return monitoramentoEventoDao;
	}
	public void setBoletoDao(BoletoDAO boletoDao) {
		this.boletoDao = boletoDao;
	}
	public BoletoDAO getBoletoDao() {
		return boletoDao;
	}
	
	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}
	
	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}
	
	public void setMonitoramentoMensagemFaturaDao(MonitoramentoMensagemFaturaDAO monitoramentoMensagemFaturaDao) {
		this.monitoramentoMensagemFaturaDao = monitoramentoMensagemFaturaDao;
	}
	
	public void setMonitoramentoMensagemDao(MonitoramentoMensagemDAO monitoramentoMensagemDao) {
		this.monitoramentoMensagemDao = monitoramentoMensagemDao;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	@AssynchronousMethod(name = "contasreceber.enviarCobrancaProAtivaFaturasEmail", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeEnviarCobrancaProAtivaFaturasEmail() {
		executeEnviarFaturasEmail();
	}

	void putSaveOrUpdate(Map<Long,List<FaturaEmailPojo>> m,FaturaEmailPojo fatura,Long id){
		List<FaturaEmailPojo> faturas = m.get(id);
		if ( faturas == null ) {
			faturas = new ArrayList<FaturaEmailPojo>();
		}
		faturas.add(fatura);
 		m.put(id,faturas);
	}
	
	public void executeEnviarFaturasEmail() {
		List<Object[]> faturas = faturaDao.findCobrancaProAtivaFaturas();

		Map<Long,List<FaturaEmailPojo>> clientesComEmail = new HashMap<Long, List<FaturaEmailPojo>>();
		Map<Long,List<FaturaEmailPojo>> clientesSemEmail = new HashMap<Long, List<FaturaEmailPojo>>();
		
		Long lastClientId = null;
		Long lastRegionalId = null;
		for (int i = 0; i < faturas.size(); i++) {
			FaturaEmailPojo fatura = createFaturaPojo(faturas,i);
			if ( i == 0 ){
				lastRegionalId = fatura.getIdRegional();
				lastClientId   = fatura.getIdCliente();
			}
			if (fatura.getIdCliente().equals(lastClientId) ){ // cliente novo
				agrupaFaturas(clientesComEmail, clientesSemEmail, fatura);
			}else{
				if ( !clientesComEmail.isEmpty() ){
					storeGeraEmailComunicacao(clientesComEmail.remove(lastClientId),"CP");
				}
				agrupaFaturas(clientesComEmail, clientesSemEmail, fatura);
			}
			if ( !lastRegionalId.equals(fatura.getIdRegional()) ){
				storeGeraEmailComunicacao(clientesSemEmail.remove(lastRegionalId),"SP");
			}
			lastClientId = fatura.getIdCliente();
			lastRegionalId = fatura.getIdRegional();
		}
		if ( !clientesSemEmail.isEmpty() ){
			Iterator<Long> keys = clientesSemEmail.keySet().iterator();
			while(keys.hasNext()){
				storeGeraEmailComunicacao(clientesSemEmail.remove(keys.next()),"SP");
			}
		}
		if ( !clientesComEmail.isEmpty() ){
			Iterator<Long> keys = clientesComEmail.keySet().iterator();
			while(keys.hasNext()){
				storeGeraEmailComunicacao(clientesComEmail.remove(keys.next()),"CP");
			}
		}
	}

	private void agrupaFaturas(Map<Long, List<FaturaEmailPojo>> clientesComEmail,Map<Long, List<FaturaEmailPojo>> clientesSemEmail,FaturaEmailPojo fatura) {
		if ( fatura.getDsEmail() != null ){ 
			putSaveOrUpdate(clientesComEmail,fatura,fatura.getIdCliente());
		}else {
			putSaveOrUpdate(clientesSemEmail,fatura,fatura.getIdRegional());
		}
	}


	private void storeGeraEmailComunicacao(List<FaturaEmailPojo> faturas,String tpModeloMensagem) {
		if ( faturas == null || faturas.isEmpty() ){
			return;
		}
		MonitoramentoMensagem modelo = new MonitoramentoMensagem();
		modelo.setTpModeloMensagem(new DomainValue(tpModeloMensagem));
		modelo.setTpEnvioMensagem(new DomainValue("E"));
		modelo.setDhInclusao(new DateTime());
		modelo.setDsParametro("{\":1\":\"" + faturas.get(0).getIdRegional() + "\"}");
		
		String para = "";
		
		if ("CO".equalsIgnoreCase(tpModeloMensagem)){
			para = faturas.get(0).getDsEmail();
		} else {
			para = faturas.get(0).getDsEmailFaturamento();
		}
		
		modelo.setDsDestinatario("{"+configuracoesFacade.getMensagem("dsDestinatario", new Object[]{faturas.get(0).getDsEmailFaturamento(), para})+"}");
		monitoramentoMensagemDao.store(modelo);
		
		for (FaturaEmailPojo faturaEmail : faturas) {
			MonitoramentoMensagemFatura monitoramentoMensagemFatura = new MonitoramentoMensagemFatura();
			monitoramentoMensagemFatura.setMonitoramentoMensagem(monitoramentoMensagemDao.findByIdMonitoramentoMensagem(modelo.getIdMonitoramentoMensagem()));
			monitoramentoMensagemFatura.setFatura(faturaDao.findByIdFatura(faturaEmail.getIdFatura()));
			monitoramentoMensagemFaturaDao.store(monitoramentoMensagemFatura);
		}
		
		updateMonitoramentoMensagemEvento(modelo);
	}

	private void updateMonitoramentoMensagemEvento(MonitoramentoMensagem modelo) {
		MonitoramentoMensagemEvento monitoramentoMensagem = new MonitoramentoMensagemEvento();
		monitoramentoMensagem.setMonitoramentoMensagem(modelo);
		monitoramentoMensagem.setTpEvento(new DomainValue("S"));
		monitoramentoMensagem.setDsEvento("LMS-36310");
		monitoramentoMensagem.setDhEvento(new DateTime());
		monitoramentoEventoDao.store(monitoramentoMensagem);
	}


	private FaturaEmailPojo createFaturaPojo(List<Object[]> faturas,int i) {
		FaturaEmailPojo fatura = new FaturaEmailPojo();
		fatura.setIdFatura((Long)faturas.get(i)[11]);
		fatura.setDsEmail((String)faturas.get(i)[10]);
		fatura.setDsEmailFaturamento((String) faturas.get(i)[2]);
		fatura.setIdCliente((Long)faturas.get(i)[6]);
		fatura.setIdFilialCob((Long) faturas.get(i)[4]);
		fatura.setIdFilialFat((Long) faturas.get(i)[8]);
		fatura.setNmPessoa((String) faturas.get(i)[5]);
		fatura.setNrFatura((String) faturas.get(i)[9]);
		fatura.setSgFilialCob((String) faturas.get(i)[3]);
		fatura.setSgFilialFat((String) faturas.get(i)[7]);
		fatura.setSgRegional((String) faturas.get(i)[0]);
		fatura.setIdRegional(faturas.get(i)[1] == null ? null : Long.valueOf(""+faturas.get(i)[1]));
		return fatura;
	}
}

package com.mercurio.lms.contasreceber.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchTransactionType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.FaturaEmailPojo;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;

@Assynchronous
public class EnviarFaturasEmailService extends CrudService<Fatura, Long> {

	private FaturaDAO faturaDao;
	private MonitoramentoMensagemService monitoramentoMensagemService;
	private MonitorarMensagemComunicacaoService monitorarMensagemComunicacaoService;
	
	public void setFaturaDao(FaturaDAO faturaDao) {
		this.faturaDao = faturaDao;
		setDao(faturaDao);
	}

	public FaturaDAO getFaturaDao() {
		return faturaDao;
	}
	
	@AssynchronousMethod(name = "contasreceber.enviarFaturasEmail", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR, transaction = BatchTransactionType.NON_TRANSACTIONAL)
	public void executeEnviarFaturas() {
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
	
	@SuppressWarnings("unchecked")
	public void executeEnviarFaturasEmail() {
		monitorarMensagemComunicacaoService.loginUsuarioFatura();
		
		List<Object[]> faturas = faturaDao.findFaturasNaoEnviadasEmail();

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
					monitoramentoMensagemService.storeGeraEmailComunicacao(clientesComEmail.remove(lastClientId), "FA");
				}
				
				agrupaFaturas(clientesComEmail, clientesSemEmail, fatura);
			}
			
			if ( !lastRegionalId.equals(fatura.getIdRegional()) ){
				monitoramentoMensagemService.storeGeraEmailComunicacao(clientesSemEmail.remove(lastRegionalId), "SE");
			}
			
			lastClientId = fatura.getIdCliente();
			lastRegionalId = fatura.getIdRegional();
		}
		
		if ( !clientesSemEmail.isEmpty() ){
			Iterator<Long> keys = clientesSemEmail.keySet().iterator();
			while(keys.hasNext()){
				monitoramentoMensagemService.storeGeraEmailComunicacao(clientesSemEmail.remove(keys.next()), "SE");
			}
		}
		
		if ( !clientesComEmail.isEmpty() ){
			Iterator<Long> keys = clientesComEmail.keySet().iterator();
			while(keys.hasNext()){
				monitoramentoMensagemService.storeGeraEmailComunicacao(clientesComEmail.remove(keys.next()), "FA");
			}
		}
	}

	private void agrupaFaturas(Map<Long, List<FaturaEmailPojo>> clientesComEmail,Map<Long, List<FaturaEmailPojo>> clientesSemEmail,FaturaEmailPojo fatura) {
		if ( fatura.getDsEmail() != null ){ // com email
			putSaveOrUpdate(clientesComEmail,fatura,fatura.getIdCliente());
		}else {//sem email
			putSaveOrUpdate(clientesSemEmail,fatura,fatura.getIdRegional());
		}
	}

	private FaturaEmailPojo createFaturaPojo(List<Object[]> faturas,int i) {
		FaturaEmailPojo fatura = new FaturaEmailPojo();
		fatura.setIdFatura((Long)faturas.get(i)[11]);
		fatura.setDsEmail((String)faturas.get(i)[10]);
		fatura.setDsEmailFaturamento((String) faturas.get(i)[2]);
		fatura.setIdCliente((Long)faturas.get(i)[6]);
		fatura.setIdFilialCob((Long) faturas.get(i)[4]);
		fatura.setIdFilialFat((Long) faturas.get(i)[8]);
		fatura.setIdRegional(faturas.get(i)[1] == null ? null : Long.valueOf(""+faturas.get(i)[1]));
		fatura.setNmPessoa((String) faturas.get(i)[5]);
		fatura.setNrFatura((String) faturas.get(i)[9]);
		fatura.setSgFilialCob((String) faturas.get(i)[3]);
		fatura.setSgFilialFat((String) faturas.get(i)[7]);
		fatura.setSgRegional((String) faturas.get(i)[0]);
		return fatura;
	}
	
	public void setMonitoramentoMensagemService(MonitoramentoMensagemService monitoramentoMensagemService) {
		this.monitoramentoMensagemService = monitoramentoMensagemService;
	}
	
	public void setMonitorarMensagemComunicacaoService(MonitorarMensagemComunicacaoService monitorarMensagemComunicacaoService) {
		this.monitorarMensagemComunicacaoService = monitorarMensagemComunicacaoService;
}
}
package com.mercurio.lms.layoutedi.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.service.ConfiguracaoProcessoEDIService;
import com.mercurio.lms.edi.model.service.RegistroLayoutEDIService;


/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ConfiguracoesEdiAction"
 */
@ServiceSecurity
public class ConfiguracoesEdiAction {
	private  ConfiguracaoProcessoEDIService configuracaoProcessoEDIService;
	private RegistroLayoutEDIService registroLayoutEDIService;
	
	
	public ConfiguracaoProcessoEDIService getConfiguracaoProcessoEDIService() {
		return configuracaoProcessoEDIService;
	}



	public void setConfiguracaoProcessoEDIService(ConfiguracaoProcessoEDIService configuracaoProcessoEDIService) {
		this.configuracaoProcessoEDIService = configuracaoProcessoEDIService;
	}



	public RegistroLayoutEDIService getRegistroLayoutEDIService() {
		return registroLayoutEDIService;
	}



	public void setRegistroLayoutEDIService(RegistroLayoutEDIService registroLayoutEDIService) {
		this.registroLayoutEDIService = registroLayoutEDIService;
	}



	@MethodSecurity(processGroup = "layoutedi.buscaConfiguracoesEdiWebService", processName = "buscaConfiguracoesEdi", authenticationRequired=false)	
	public Object buscaConfiguracoesEdi(){
		String hostBPEL = configuracaoProcessoEDIService.findConfigByChave("ftp-host-bpel").getValor();
		String userBPEL = configuracaoProcessoEDIService.findConfigByChave("ftp-user-bpel").getValor();
		String pwdBPEL = configuracaoProcessoEDIService.findConfigByChave("ftp-password-bpel").getValor();
		String raizBPEL = configuracaoProcessoEDIService.findConfigByChave("ftp-raiz-bpel").getValor();
		String recebidosBPEL = configuracaoProcessoEDIService.findConfigByChave("ftp-recebidos-bpel").getValor();
		String hostEmail = configuracaoProcessoEDIService.findConfigByChave("smtp-host").getValor();
		String to = configuracaoProcessoEDIService.findConfigByChave("email-responsavel").getValor();
		String hostClientes =  configuracaoProcessoEDIService.findConfigByChave("ftp-host-ntEsales").getValor();
		String raizClientes =  configuracaoProcessoEDIService.findConfigByChave("ftp-raiz-ntEsales").getValor();
		String tempo = configuracaoProcessoEDIService.findConfigByChave("tempo_na_pasta").getValor();
		String hostNT = configuracaoProcessoEDIService.findConfigByChave("ftp-host-ntEsales").getValor();
		String userNT = configuracaoProcessoEDIService.findConfigByChave("ftp-user-ntEsales").getValor();
		String pwdNT = configuracaoProcessoEDIService.findConfigByChave("ftp-password-ntEsales").getValor();
		String raizNT = configuracaoProcessoEDIService.findConfigByChave("ftp-raiz-ntEsales").getValor();
		String xslsNT = configuracaoProcessoEDIService.findConfigByChave("ftp-xsls-ntEsales").getValor();
		String qtdeDest = configuracaoProcessoEDIService.findConfigByChave("num-dest-quebre-arq").getValor();
		String tempoBuscaArquivo = configuracaoProcessoEDIService.findConfigByChave("tempo-busca-arquivo").getValor();
		String caminhoNT = configuracaoProcessoEDIService.findConfigByChave("caminho-ntEsales").getValor();
		HashMap<String, String> dados = new HashMap<String, String>();
		dados.put("ftp-host-bpel", hostBPEL);
		dados.put("ftp-user-bpel", userBPEL);
		dados.put("ftp-password-bpel", pwdBPEL);
		dados.put("ftp-raiz-bpel", raizBPEL);
		dados.put("ftp-recebidos-bpel", recebidosBPEL);
		dados.put("smtp-host", hostEmail);
		dados.put("email-responsavel", to);
		dados.put("ftp-host-ntEsales", hostClientes);
		dados.put("ftp-raiz-ntEsales", raizClientes);
		dados.put("tempo_na_pasta", tempo);
		dados.put("ftp-host-ntEsales", hostNT);
		dados.put("ftp-user-ntEsales", userNT);
		dados.put("ftp-password-ntEsales", pwdNT);
		dados.put("ftp-raiz-ntEsales", raizNT);
		dados.put("ftp-xsls-ntEsales", xslsNT);
		dados.put("ftp-xsls-ntEsales", xslsNT);
		dados.put("num-dest-quebre-arq", qtdeDest);
		dados.put("tempo-busca-arquivo", tempoBuscaArquivo);
		dados.put("caminho-ntEsales", caminhoNT);
		
		
		
		return (Object)dados;
	}
	
	@MethodSecurity(processGroup = "layoutedi.buscaConfiguracoesEdiWebService", processName = "buscaConfiguracoesLayoutEdi", authenticationRequired=false)	
	public Object buscaConfiguracoesLayoutEdi(){
		String idDestnfSemRem = configuracaoProcessoEDIService.findConfigByChave("id-destnf-sem-rem").getValor();
		String idDestnfComRem = configuracaoProcessoEDIService.findConfigByChave("id-destnf-com-rem").getValor();
		String idRemNaoLido = configuracaoProcessoEDIService.findConfigByChave("id-rem-nao-lido").getValor();
		HashMap<String, String> dados = new HashMap<String, String>();
		dados.put("id-destnf-sem-rem", idDestnfSemRem);
		dados.put("id-destnf-com-rem", idDestnfComRem);
		dados.put("id-rem-nao-lido", idRemNaoLido);
		
		
		return (Object)dados;
	}	
	
	
	@MethodSecurity(processGroup = "layoutedi.buscaConfiguracoesEdiWebService", processName = "buscaDadosQuebraArquivos", authenticationRequired=false)	
	public Object buscaDadosQuebraArquivos(Map params){
		Long idLayout = (Long)params.get("idLayoutEDI");
		
		List<RegistroLayoutEDI> registros = this.registroLayoutEDIService.findDadosQuebraArquivo(idLayout);

		HashMap<String, String> dados = new HashMap<String, String>();
		if(registros != null){
			for (RegistroLayoutEDI registroLayoutEDI : registros) {
				dados.put(registroLayoutEDI.getNomeIdentificador(), registroLayoutEDI.getIdentificador().toString());
			}
		}
		
		return (Object)dados;
	}
	
}

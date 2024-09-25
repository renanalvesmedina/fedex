package com.mercurio.lms.util.session;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.core.model.hibernate.ConnectionClientInfo;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Filial;

public class LMSConnectionInfo implements ConnectionClientInfo {

	public Map<String, String> getConnectionClientInfo() {
		Map<String, String> dadosSessaoBanco = new HashMap<String, String>();
		// define dados padr�es para as variaveis de sess�o,
		// a trigger de integra��o gera erro caso n�o encontre
		// o parametro que ela busca
		dadosSessaoBanco.put("i", "0"); // por padr�o integra��o n�o esta rodando
		dadosSessaoBanco.put("tl", "1"); // por padr�o as triggers da integra��o devem logar, ver coment�rio mais abaixo
		dadosSessaoBanco.put("b", "1"); // por padr�o batch n�o esta rodando 
		dadosSessaoBanco.put("f", "361"); // por padr�o ID_FILIAL da MATRIZ no banco de produ��o
		dadosSessaoBanco.put("to", new DateTime(DateTimeZone.forID("America/Sao_Paulo")).toString().substring(23, 26)); // por padr�o offset de SAO_PAULO
		
		// HACK que permite a execu��o o log do Batch via JMS
		// Quando este m�todo era chamado no contexto da thread que 
		// grava no banco os logs lidos da fila JMS era gerado um NullPointerException
		// porque n�o existe informa��es de quem est� logado no contexto da thread.
		// Ver classe com.mercurio.adsm.batch.log.LogPersisterImpl
		if (SessionContext.getUser(true) == null) {
			if (SessionUtils.isBatchJobRunning()) {
				dadosSessaoBanco.put("b", "1");
			}
			return dadosSessaoBanco;
		}
		
		// Teste para validar se j� existe um contexto Web, est� horrivel mas n�o
		// existe outra forma de validar esta situa��o da forma que est� hoje o 
		// mecanismo de sess�o
		if (HttpServletRequestHolder.getHttpServletRequest() != null) {
			
			Filial filialSessao = SessionUtils.getFilialSessao();
			if (filialSessao != null) {
				dadosSessaoBanco.put("f", filialSessao.getIdFilial().toString());
				String offset = new DateTime(DateTimeZone.forID(filialSessao.getDsTimezone())).toString().substring(23, 26);
				dadosSessaoBanco.put("to", offset);
			}
			
			if (SessionUtils.isIntegrationTriggerLogDisabled()) {
				dadosSessaoBanco.put("tl", "0");
			}
			if (SessionUtils.isIntegrationRunning()) {
				dadosSessaoBanco.put("i", "1");
			}
			if (SessionUtils.isBatchJobRunning()) {
				dadosSessaoBanco.put("b", "1");
			}
		}
		
		return dadosSessaoBanco;
	}

}

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
		// define dados padrões para as variaveis de sessão,
		// a trigger de integração gera erro caso não encontre
		// o parametro que ela busca
		dadosSessaoBanco.put("i", "0"); // por padrão integração não esta rodando
		dadosSessaoBanco.put("tl", "1"); // por padrão as triggers da integração devem logar, ver comentário mais abaixo
		dadosSessaoBanco.put("b", "1"); // por padrão batch não esta rodando 
		dadosSessaoBanco.put("f", "361"); // por padrão ID_FILIAL da MATRIZ no banco de produção
		dadosSessaoBanco.put("to", new DateTime(DateTimeZone.forID("America/Sao_Paulo")).toString().substring(23, 26)); // por padrão offset de SAO_PAULO
		
		// HACK que permite a execução o log do Batch via JMS
		// Quando este método era chamado no contexto da thread que 
		// grava no banco os logs lidos da fila JMS era gerado um NullPointerException
		// porque não existe informações de quem está logado no contexto da thread.
		// Ver classe com.mercurio.adsm.batch.log.LogPersisterImpl
		if (SessionContext.getUser(true) == null) {
			if (SessionUtils.isBatchJobRunning()) {
				dadosSessaoBanco.put("b", "1");
			}
			return dadosSessaoBanco;
		}
		
		// Teste para validar se já existe um contexto Web, está horrivel mas não
		// existe outra forma de validar esta situação da forma que está hoje o 
		// mecanismo de sessão
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

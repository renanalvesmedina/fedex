package com.mercurio.lms.services.financeiro.preFaturaNatura;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.financeiro.DocumentoPreFatura;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.financeiro.model.service.PreFaturaNaturaException;
import com.mercurio.lms.financeiro.model.service.PreFaturaNaturaService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;

@Path("/financeiro/preFaturaNatura") 
public class PreFaturaNaturaRest {

	@InjectInJersey
	PreFaturaNaturaService preFaturaNaturaService;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private IntegracaoJmsService integracaoJmsService;
	
	private static final Logger LOGGER = LogManager.getLogger(PreFaturaNaturaRest.class);

	private static final String RETORNO_OK = "OK";
	private static final Pattern PATTERN = Pattern.compile(".*(LMS-\\d+)");
	
	private static final String DESTINATARIO_EMAIL_ERRO = "DESTINATARIO_ERRO_PREFAT_NATURA";
	private static final String ASSUNTO_EMAIL_ERRO = "ASSUNTO_ERRO_PREFAT_NATURA";
	private static final String REMETENTE_EMAIL_ERRO = "edi@tntbrasil.com.br";
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	@POST
	@Path("importarPreFaturaNatura")
	public Response importarPreFaturaNatura(List<DocumentoPreFatura> documentos){

		try{
			SessionContext.set("FILIAL_KEY", preFaturaNaturaService.findIdFilialPrimeiroDocumento(documentos));
			
			FaturaDMN preFaturaNatura = preFaturaNaturaService.executeImportarPreFaturaNatura(documentos);
			
			preFaturaNatura.setStatusRetorno(RETORNO_OK);
			return  Response.ok(
					preFaturaNatura
					).build();
		}catch(Exception e){
			if (!(	e instanceof BusinessException || 
					e instanceof PreFaturaNaturaException)) {
				LOGGER.error(e);
			} 
			
			FaturaDMN returnException = new FaturaDMN();

			if (StringUtils.isNotBlank(e.getMessage())) {
				returnException.setStatusRetorno(getErrorMessage(e.getMessage()));
			} else {
				returnException.setStatusRetorno(e.getClass().toString());
			}
			return Response.ok(returnException).build();
		}
		
	}

    private String getErrorMessage(String exceptionMessage) {
		Matcher matcher = PATTERN.matcher(exceptionMessage);
		
        if (matcher.matches()) {
        	return configuracoesFacade.getMensagem(matcher.group(1));
        }
        return exceptionMessage;
	}
}

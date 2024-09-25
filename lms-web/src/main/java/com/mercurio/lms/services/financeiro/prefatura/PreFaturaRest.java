package com.mercurio.lms.services.financeiro.prefatura;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.financeiro.model.service.PreFatException;
import com.mercurio.lms.financeiro.model.service.PreFatService;

import br.com.tntbrasil.integracao.domains.financeiro.DocumentoPreFatura;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;

@Path("/financeiro/preFaturaRest")
public class PreFaturaRest {

	@InjectInJersey
	PreFatService preFatService;

	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	private static final Logger LOGGER = LogManager.getLogger(PreFaturaRest.class);

	private static final String RETORNO_OK = "OK";
	private static final Pattern PATTERN = Pattern.compile(".*(LMS-\\d+)");

	@POST
	@Path("importarPreFatura")
	public Response importarPreFatura(List<DocumentoPreFatura> documentos) {

		try {
			SessionContext.set("FILIAL_KEY", preFatService.findIdFilialPrimeiroDocumento(documentos));

			FaturaDMN preFatura = preFatService.executeImportarPreFatura(documentos);

			preFatura.setStatusRetorno(RETORNO_OK);

			return Response.ok(preFatura).build();

		} catch (Exception e) {

			if (!(e instanceof BusinessException || e instanceof PreFatException)) {
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

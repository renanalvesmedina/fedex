package com.mercurio.lms.services.contasareceber;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;
import br.com.tntbrasil.integracao.domains.financeiro.Redeco;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.service.InclusaoRedecoService;
import com.mercurio.lms.contasreceber.model.service.exception.InclusaoRedecoException;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionKey;

@Path("/contasareceber/inclusaoRedeco") 
public class InclusaoRedecoRest {

	@InjectInJersey 
	InclusaoRedecoService inclusaoRedecoService;
	
	@InjectInJersey 
	private FilialService filialService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;

	private static final Long FILIAL_MTZ = 361L;
	private static final String RETORNO_OK = "OK";
	private static final Logger LOGGER = LogManager.getLogger(InclusaoRedecoRest.class);
	private static final Pattern PATTERN = Pattern.compile(".*(LMS-\\d+)");

	
	@POST
	@Path("incluirRedeco")
	public Response incluirRedeco(List<FaturaDMN> faturas){
		Redeco redecoCanonico = null;
		
		try { 
			SessionContext.set(SessionKey.FILIAL_KEY, filialService.findById(FILIAL_MTZ));
			redecoCanonico = transformToRedecoCanonico(inclusaoRedecoService.executeIncluirRedeco(faturas));
			redecoCanonico.setStatusRetorno(RETORNO_OK);
			
		} catch (Exception exception) {
			if (!(exception instanceof InclusaoRedecoException || exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}

			redecoCanonico = new Redeco();
			
			if (StringUtils.isNotBlank(exception.getMessage())) {
				redecoCanonico.setStatusRetorno(getErrorMessage(exception.getMessage()));
			} else {
				redecoCanonico.setStatusRetorno(exception.getClass().toString());
			}
		}
		
		return  Response.ok(redecoCanonico).build();	
	}
	
	private String getErrorMessage(String exceptionMessage) {
		Matcher matcher = PATTERN.matcher(exceptionMessage);
		
        if (matcher.matches()) {
        	return configuracoesFacade.getMensagem(matcher.group(1));
        }
        return exceptionMessage;
	}

	private Redeco transformToRedecoCanonico(com.mercurio.lms.contasreceber.model.Redeco redecoStore) {
		
		Redeco redeco = new Redeco();
		redeco.setIdRedeco(redecoStore.getIdRedeco());
		redeco.setSgFilial(redecoStore.getFilial().getSgFilial());
		redeco.setNrRedeco(redecoStore.getNrRedeco());
		return redeco;
	}

}

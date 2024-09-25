package com.mercurio.lms.services.contasareceber;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.service.RetornoOcorrenciaBoletoBancoService;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.RetornoArquivoRemessaDTO;

@Path("/contasareceber/retornoBancario") 
public class RetornoBancarioRest {

	private static final String RETORNO_OK = "OK";
	private static final Logger LOGGER = LogManager.getLogger(RetornoBancarioRest.class);

	@InjectInJersey
	private RetornoOcorrenciaBoletoBancoService retornoOcorrenciaBoletoBancoService; 
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService; 

	@POST
	@Path("executeProcessarRetornoBancario")
	public Response executeRetornoOcorrenciaBoletoBanco(BoletoDMN boletoDmn){
		try {
			boletoDmn = retornoOcorrenciaBoletoBancoService.execute(boletoDmn);
			boletoDmn.setStatusRetorno(RETORNO_OK);
		} catch(Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			} 

			if (StringUtils.isNotBlank(exception.getMessage())) {
				boletoDmn.setStatusRetorno(exception.getMessage());
			} else {
				boletoDmn.setStatusRetorno(exception.getClass().toString());
			}
		}
		return  Response.ok(boletoDmn).build();
	}
	
	
	@POST 
	@Path("executeProcessarRetornoBancarioBradesco")
	public Response executeProcessarRetornoBancarioBradesco(RetornoArquivoRemessaDTO retornoArquivoRemessaDTO){
		BoletoDMN boletoDmn = new BoletoDMN();
		try {
			retornoOcorrenciaBoletoBancoService.executeProcessarRetornoBancarioBradesco(retornoArquivoRemessaDTO);
			boletoDmn.setStatusRetorno(RETORNO_OK);
			
			
		} catch(BusinessException be) {
			boletoDmn.setStatusRetorno(parametroGeralService.getMessage(be.getMessageKey(), be.getMessageArguments()));
		} catch (Exception e) {
			if (StringUtils.isNotBlank(e.getMessage())) {
				boletoDmn.setStatusRetorno(e.getMessage());
			} else {
				boletoDmn.setStatusRetorno(e.getClass().toString());
			}
		}	
			
		return  Response.ok(boletoDmn).build();
		
	}

}

package com.mercurio.lms.services.entrega;

import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.endToEnd.ERecebimentoTrackingFedex;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.endtoend.model.service.IntegracaoEventoDocumentoServicoService;
import com.mercurio.lms.util.JTDateTimeUtils;

@Path("/entrega")
public class RecebimentoTrakingFedexRest {
	
	
	public static final String RETORNO_ERRO = "ERRO";
	public static final String RETORNO_OK = "OK";

	@InjectInJersey
	private IntegracaoEventoDocumentoServicoService integracaoEventoDocumentoServicoService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade; 
	
	@POST()
	@Path("/recebimentoTrackingFedex")
	public Response executeRecebimentoTrackingFedex(ERecebimentoTrackingFedex eRecebimentoTrackingFedex){
		
		eRecebimentoTrackingFedex.setDataHoraRecebimento(JTDateTimeUtils.getDataHoraAtual());
		eRecebimentoTrackingFedex.setListErros(new ArrayList<String>());
		try {
			integracaoEventoDocumentoServicoService.executeRecebimentoTrackingCte(eRecebimentoTrackingFedex);
			eRecebimentoTrackingFedex.setStatus(RETORNO_OK);
		} catch (BusinessException be) {
			eRecebimentoTrackingFedex.getListErros().add(configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
			eRecebimentoTrackingFedex.setStatus(RETORNO_ERRO);
		} catch (Exception ex){
			if(ex.getMessage() != null){
				eRecebimentoTrackingFedex.getListErros().add(ex.getMessage());
	        }else{
	        	eRecebimentoTrackingFedex.getListErros().add(ex.getClass().toString());
	        }
			eRecebimentoTrackingFedex.setStatus(RETORNO_ERRO);
		}
		
		return Response.ok(eRecebimentoTrackingFedex).build();
	}
	
}

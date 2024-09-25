package com.mercurio.lms.services.entrega;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.vol.model.service.VolInformarOcorrenciasService;

import br.com.tntbrasil.integracao.domains.entrega.OcorrenciaDMN;
import br.com.tntbrasil.integracao.domains.entrega.retornofedex.RetornoOcorrenciaFedexDMN;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;


@Path("/entrega/ocorrenciaEntrega/fedex") 
public class IntegracaoFedexOcorrenciaEntregaRest extends BaseRest {
	
	@InjectInJersey
	NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	
	@InjectInJersey
	ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	
	@InjectInJersey
	VolInformarOcorrenciasService volInformarOcorrenciasService;
	
	@InjectInJersey
	ConfiguracoesFacade configuracoesFacade;
	
	@POST
	@Path("buscarDados")
	public Response buscarDados() {
		
		List<NotaFiscalDMN> retornoList = notaFiscalConhecimentoService.findNotasFiscaisConhecimentoIntegracaoFedex();
		  		
		return Response.ok(retornoList).build();
	}
	
	@POST
	@Path("executeInformarOcorrenciaIntegracaoFedex")
	public Response executeInformarOcorrenciaIntegracaoFedex(RetornoOcorrenciaFedexDMN retornoOcorrenciaFedexDMN) {
		
		OcorrenciaDMN ocorrenciaDMN = retornoOcorrenciaFedexDMN.getListOcorrencias().get(0);
		String cdEventoChegadaCliente = (String) configuracoesFacade.getValorParametro("CD_EVENTO_CHEGADA_CLIENTE");
		
		if (ocorrenciaDMN.getCodOcorrencia().equals(cdEventoChegadaCliente)) {
			volInformarOcorrenciasService.executeInformarOcorrenciaIntegracaoFedex(retornoOcorrenciaFedexDMN);
		} else {
			manifestoEntregaDocumentoService.executeBaixaDocumentoIntegracaoFedex(retornoOcorrenciaFedexDMN);
		}
		
		return Response.ok(new HashMap<String, String>()).build();
	}

}

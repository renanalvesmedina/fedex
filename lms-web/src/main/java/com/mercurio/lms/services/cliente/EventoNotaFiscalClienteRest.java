package com.mercurio.lms.services.cliente;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/cliente/eventoNotaFiscalCliente")
public class EventoNotaFiscalClienteRest {
	@InjectInJersey
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;

	@POST
	@Path("/findDadosNotaFiscalConhecimentoCliente")
	public Response findDadosNotaFiscalConhecimentoCliente(Map<String, Object> criteria) {
		String serie = null;
		if(criteria.get("dsSerie") != null){
			serie = criteria.get("dsSerie").toString();
		}
		return Response.ok(notaFiscalConhecimentoService.findDadosNotaFiscalConhecimento(criteria.get("nrIdentificacaoRemetente").toString(), Integer.parseInt(criteria.get("nrNotaFiscal").toString().replace("-","")), serie)).build();
	}

	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return notaFiscalConhecimentoService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}


}

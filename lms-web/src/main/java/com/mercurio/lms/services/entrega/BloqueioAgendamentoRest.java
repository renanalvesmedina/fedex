package com.mercurio.lms.services.entrega;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.service.CalcularDiasUteisBloqueioAgendamentoService;

@Path("/entrega/bloqueioAgendamento")
public class BloqueioAgendamentoRest {
	
	@InjectInJersey CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;

	@GET
	@Path("calcularDiasUteisBloqueio")
	public Response calcularDiasUteisBloqueio(@QueryParam("idDocumentoServico") Long idDocumentoServico) {
		calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(idDocumentoServico, Boolean.TRUE);
		return Response.ok().build();
	}
}

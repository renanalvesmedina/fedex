package com.mercurio.lms.services.evento;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.service.ComprovanteEntregaService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoNaturaDMN;

@Path("/evento/natura")
public class EventoDoctoServicoNaturaRest {

	private Logger log = LogManager.getLogger(this.getClass());

	@InjectInJersey 
	private EventoDocumentoServicoService eventoDocumentoServicoService;

	@InjectInJersey
	private IntegracaoJmsService integracaoJmsService;
	
	@InjectInJersey
	private ComprovanteEntregaService comprovanteEntregaService;

	@POST
	@Path("executeEventoDocumentoServicoNatura")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeEventoDocumentoServicoNatura(EventoDocumentoServicoDMN eventoDoctoServico) {
		List<EventoDocumentoServicoNaturaDMN> eventoDocumentoServicoNaturaDMNList = eventoDocumentoServicoService.findAndFillEventoDocumentoServicoNaturaDMN(eventoDoctoServico);
		return Response.ok(eventoDocumentoServicoNaturaDMNList).build();
	}
	
	@POST
	@Path("updateComprovanteEntrega")
	public void updateComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN) {
		try {
			comprovanteEntregaService.updateComprovanteEntrega(eventoDocumentoServicoNaturaDMN);
		} catch (Exception e) {
			log.error(e);
		}
	}

	@POST
	@Path("updateTentativaEnvioComprovanteEntrega")
	public void updateTentativaEnvioComprovanteEntrega(EventoDocumentoServicoNaturaDMN eventoDocumentoServicoNaturaDMN) {
		try {
			comprovanteEntregaService.updateTentativaEnvioComprovanteEntrega(eventoDocumentoServicoNaturaDMN);
		} catch (Exception e) {
			log.error(e);
		}
	}
	
	@GET
	@Path("executeTesteEventoDocumentoServicoNatura")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeTesteEventoDocumentoServicoNatura(@QueryParam("id") Long idEventoDocumentoServico) {
		return executeEventoDocumentoServicoNatura(eventoDocumentoServicoService.findEventoDocumentoServicoDMNById(idEventoDocumentoServico));
	}

	@GET
	@Path("executeTesteJmsEventoDocumentoServicoNatura")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeOcorrenciaEntregaNaturaTeste(@QueryParam("id") Long idEventoDocumentoServico) {
		EventoDocumentoServicoDMN eventoDocumentoServicoDMN = eventoDocumentoServicoService.findEventoDocumentoServicoDMNById(idEventoDocumentoServico);
		IntegracaoJmsService.JmsMessageSender messsageSender = integracaoJmsService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO, eventoDocumentoServicoDMN);
		integracaoJmsService.storeMessage(messsageSender);
		return Response.ok(eventoDocumentoServicoDMN).build();
	}
	
	@GET
	@Path("executeBuscaEventoBaixaComAssNaoEnviada")
	@Produces(MediaType.APPLICATION_JSON)
	public Response executeBuscaEventoBaixaComAssNaoEnviada() {
		List<EventoDocumentoServicoDMN> result = eventoDocumentoServicoService.findEventosBaixadosComAssNaoEnviadas();
		return Response.ok(result).build();
	}
}

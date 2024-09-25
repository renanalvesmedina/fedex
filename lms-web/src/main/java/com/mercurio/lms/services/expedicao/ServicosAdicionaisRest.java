package com.mercurio.lms.services.expedicao;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.expedicao.servicoadicional.DocumentoCalculoMensalServicoAdicionalDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.MonitoramentoServicosAdicionaisService;

@Path("/expedicao/servicosAdicionaisRest") 
public class ServicosAdicionaisRest extends BaseRest{
		
		@InjectInJersey 
		MonitoramentoServicosAdicionaisService monitoramentoServicosAdicionaisService;
		
		@POST
		@Path("calculoServicosAdicionais")
		public Response calculoServicosAdicionais(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
			monitoramentoServicosAdicionaisService.executeCalculoServicosAdicionais(eventoDocumentoServicoDMN);
			return Response.ok(eventoDocumentoServicoDMN).build();
		} 
		
		@POST
		@Path("calculoMensalServicosAdicionais")
		public Response calculoServicosAdicionais(DocumentoCalculoMensalServicoAdicionalDMN documentoCalculoMensalDMN) {
			monitoramentoServicosAdicionaisService.executeCalculoMensalServicosAdicionais(documentoCalculoMensalDMN);
			return Response.ok(documentoCalculoMensalDMN).build();
		}
		
		@POST
		@Path("cancelaCalculoServicosAdicionais")
		public Response cancelaCalculoServicosAdicionais(EventoDocumentoServicoDMN eventoDocumentoServicoDMN) {
			monitoramentoServicosAdicionaisService.executeCancelarCalculoServicoAdicional(eventoDocumentoServicoDMN);
			return Response.ok(eventoDocumentoServicoDMN).build();
		}
	} 

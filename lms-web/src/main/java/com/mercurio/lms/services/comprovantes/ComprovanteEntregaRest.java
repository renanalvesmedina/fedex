package com.mercurio.lms.services.comprovantes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.imagemdigital.ComprovanteEntregaDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.service.ComprovanteEntregaService;

@Path("/comprovante")
public class ComprovanteEntregaRest {

	private static final Logger LOGGER = LogManager.getLogger(ComprovanteEntregaRest.class);

	@InjectInJersey
	private ComprovanteEntregaService service;

	@GET
	@Path("assinatura")
	public Response obtemComprovanteAssinado(@QueryParam("idDoctoServico") final Long idDoctoServico) {
		ComprovanteEntregaDMN retorno = null;
		try {
			byte[] assinatura = service.findAssinaturaByDoctoServico(idDoctoServico);

			if (assinatura != null) {
				retorno = new ComprovanteEntregaDMN();
				retorno.setFile(assinatura);
				retorno.setOrigem("LMS-" + idDoctoServico);
			}
		} catch (Exception exception) {
			if (!(exception instanceof BusinessException)) {
				LOGGER.error(exception);
			}
		}
		return Response.ok(retorno).build();
	}

}

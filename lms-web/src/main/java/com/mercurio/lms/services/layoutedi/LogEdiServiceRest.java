package com.mercurio.lms.services.layoutedi;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.webservice.edi.LogDetalhe;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.layoutedi.action.ManterLogEdiAction;
import com.mercurio.lms.services.layoutedi.util.DeParaLayouEDI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/layoutedi/log")
public class LogEdiServiceRest {

	private Logger log = LogManager.getLogger(this.getClass());

	@InjectInJersey
	private ManterLogEdiAction manterLogEdiAction;

	@POST
	@Path("gravarArquivoLog")
	public Response gravarArquivoLog(String[] dadosArquivo) {
		try {
			return  Response.ok(manterLogEdiAction.gravaArquivoLog(dadosArquivo)).build();
		} catch (Exception e) {
			log.error(e);
		}
		return Response.serverError().build();
	}
	
	@POST
	@Path("gravarArquivoDetalheLog")
	public Response gravarArquivoDetalheLog(LogDetalhe deLogDetalhe) {
		try {
			return  Response.ok(manterLogEdiAction.gravaArquivoDetalheLog(DeParaLayouEDI.deParaLog(deLogDetalhe))).build();
		} catch (Exception e) {
			log.error(e);
		}
		return Response.serverError().build();
	}

}

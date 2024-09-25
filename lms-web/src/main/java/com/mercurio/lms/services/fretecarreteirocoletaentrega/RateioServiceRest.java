package com.mercurio.lms.services.fretecarreteirocoletaentrega;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.RateioFreteCarreteiroCeService;

@Path("/fretecarreteirocoletaentrega/rateio")
public class RateioServiceRest {

	private static final String ATIVA_CARGA_RATEIO_RFC = "CARGA_RATEIO_RFC";

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@InjectInJersey
	private RateioFreteCarreteiroCeService rateioFreteCarreteiroCeService;

	@GET
	@Path("carga")
	public Response carga(@QueryParam("idRecibo") Long idRecibo) {

		String retorno = "Parametro 'CARGA_RATEIO_RFC' não ativo";
		Object parametro = parametroGeralService.findConteudoByNomeParametro(ATIVA_CARGA_RATEIO_RFC, false);

		if (parametro != null && "S".equalsIgnoreCase(String.valueOf(parametro))) {
			rateioFreteCarreteiroCeService.execute(idRecibo);
			retorno = "Executado";
		}

		return Response.ok(retorno).build();
	}
}

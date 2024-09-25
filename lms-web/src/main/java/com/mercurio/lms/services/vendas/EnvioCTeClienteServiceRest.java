package com.mercurio.lms.services.vendas;

import br.com.tntbrasil.integracao.domains.vendas.CTeDMN;
import br.com.tntbrasil.integracao.domains.vendas.CTeDadosEnvioDMN;
import br.com.tntbrasil.integracao.domains.vendas.CTeUpdateDMN;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.vendas.model.service.EnvioCTeClienteService;
import com.mercurio.lms.vendas.model.service.ManterParametrizacaoEnvioService;
import com.mercurio.lms.vendas.model.service.ParametrizacaoEnvioCTeClienteService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/vendas/envioCteClienteService")
public class EnvioCTeClienteServiceRest {
	@InjectInJersey
	private ManterParametrizacaoEnvioService manterParametrizacaoEnvioService;

	@InjectInJersey
	private ParametrizacaoEnvioCTeClienteService parametrizacaoEnvioCTeClienteService;

	@InjectInJersey
	private EnvioCTeClienteService envioCteClienteService;

	@POST
	@Path("findDadosParametrizacaoEnvio")
	public Response findDadosParametrizacaoEnvio(String nrChave) {
		if(nrChave!= null){
			CTeDadosEnvioDMN cteDadosEnvioDMN = manterParametrizacaoEnvioService.findDadosParametrizacaoEnvio(nrChave);
			if(cteDadosEnvioDMN != null){
				return Response.ok(cteDadosEnvioDMN).build();
			}
		}
		return Response.ok().build();
	}
	
	@POST
	@Path("updateMonitoramento")
	public Response updateMonitoramento(CTeUpdateDMN cteUpdateDMN) {
		manterParametrizacaoEnvioService.updateMonitoramento(cteUpdateDMN);
		return Response.ok().build();
	}

	@GET
	@Path("enriquecePathSftpCte")
	public Response enriquecePathSftpCte
		(
			@QueryParam("nrIdentificaoDestinatario") String nrIdentificaoDestinatario,
			@QueryParam("nrIdentificaoRemetente") String nrIdentificaoRemetente,
			@QueryParam("nrIdentificaoTomador")  String nrIdentificaoTomador)
	{

		String retorno = parametrizacaoEnvioCTeClienteService
			.findDsDiretorioArmazenagem
				(
					nrIdentificaoDestinatario,
					nrIdentificaoRemetente,
					nrIdentificaoTomador
				);

		return Response.ok(retorno).build();

	}

	@POST
	@Path("enriqueceCteEdw")
	public Response enriqueceCteEdw(CTeDMN cTeDMN)	{
		return Response.ok(envioCteClienteService.enriqueceEdw(cTeDMN)).build();
	}
}
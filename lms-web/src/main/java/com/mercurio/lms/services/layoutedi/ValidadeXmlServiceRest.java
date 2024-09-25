package com.mercurio.lms.services.layoutedi;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.layoutedi.action.ValidarXmlEdiAction;

@Path("/layoutedi/validateXml")
public class ValidadeXmlServiceRest {

	@InjectInJersey
	private ValidarXmlEdiAction validarXmlEdiAction;

	@GET
	@Path("validarConexao")
	public Response validarConexao() {
		return  Response.ok(validarXmlEdiAction.validaConexao()).build();
	}
	
	@POST
	@Path("buscarCampos")
	public Response buscarCampos(Long idClienteLayoutEdi) {
		return  Response.ok(validarXmlEdiAction.buscaCampos(idClienteLayoutEdi)).build();
	}
	
	@POST
	@Path("buscarNomeCampos")
	public Response buscarNomeCampos(Long idClienteLayoutEdi) {
		return  Response.ok(validarXmlEdiAction.buscaNomeCampos(idClienteLayoutEdi)).build();
	}
	
	@POST
	@Path("buscarDePara")
	public Response buscarDePara(Object[] parametros) {
		return  Response.ok(validarXmlEdiAction.buscarDePara(parametros)).build();
	}
	
	@POST
	@Path("buscarDadosComplemento")
	public Response buscarDadosComplemento(String[] parametros) {
		return  Response.ok(validarXmlEdiAction.buscarDadosComplemento(parametros)).build();
	}
	
	@POST
	@Path("buscarDadosVolume")
	public Response buscarDadosVolume(Long idComposicaoLayout) {
		return  Response.ok(validarXmlEdiAction.buscarDadosVolume(idComposicaoLayout)).build();
	}
	
	@POST
	@Path("buscarNomeCampoVolume")
	public Response buscarNomeCampoVolume(Long idClienteLayoutEdi) {
		return  Response.ok(validarXmlEdiAction.buscaNomeCampoVolume(idClienteLayoutEdi)).build();
	}
}

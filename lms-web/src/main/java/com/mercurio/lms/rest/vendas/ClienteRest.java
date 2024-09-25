package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.vendas.dto.DivisaoClienteSuggestDTO;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;

@Path("/vendas/cliente")
public class ClienteRest {
	
	@InjectInJersey DivisaoClienteService divisaoClienteService;

	@GET
	@Path("{id}/divisoes")
	public Response findDivisoesCliente(@PathParam("id") Long idCliente) {
		List<DivisaoClienteSuggestDTO> divisoes = new ArrayList<DivisaoClienteSuggestDTO>(); 
		if(LongUtils.hasValue(idCliente)) {
			divisoes.addAll(this.converteListaDivisoesCliente(divisaoClienteService.findLookupDivisoesCliente(idCliente,ConstantesVendas.SITUACAO_ATIVO)));
		}
		return Response.ok(divisoes).build();
	}
	
	@SuppressWarnings("rawtypes")
	private List<DivisaoClienteSuggestDTO> converteListaDivisoesCliente(List registros) {
		List<DivisaoClienteSuggestDTO> divisoes = new ArrayList<DivisaoClienteSuggestDTO>();
		for (Object registro : registros) {
			Map map = (Map) registro;
			divisoes.add(new DivisaoClienteSuggestDTO((Long) map.get("idDivisaoCliente"), (String) map.get("dsDivisaoCliente")));
		}
		return divisoes;
	}
}
package com.mercurio.lms.rest.tabeladeprecos;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;

@Path("/tabeladeprecos")
public class TabelaPrecoRest {
	
	

	@InjectInJersey TabelaPrecoService tabelaPrecoService;
	@InjectInJersey ServicoService servicoService;
	
	@GET
	@Path("{id}/parcelas")
	@Produces({MediaType.APPLICATION_JSON})
	public Response buscaParcelasDaTabela(@PathParam("id") long idTabela) {
		List<ParcelaPreco> parcelas = tabelaPrecoService.findParcelasByTabelaPreco(idTabela);
		return Response.ok(parcelas).build();
	}
	
	@POST
	@Path("suggest")
	@Produces({MediaType.APPLICATION_JSON})
	public Response buscaSuggest(TypedFlatMap params) {
		String value = (String) params.get("value");
		String subtipo = (String) params.get("subtipo");
		boolean apenasVigentes = true;
		if (params.containsKey("apenasVigentes")){
		    apenasVigentes = params.getBoolean("apenasVigentes");
		}
		DomainValue tpModal = StringUtils.isNotEmpty((String) params.get("tpmodal")) ? new DomainValue((String) params.get("tpmodal")) : null;
		
		if (StringUtils.isBlank(value)) {
			return Response.ok().build();
		}
		
		value = value.toUpperCase();
		
		Pattern pattern = Pattern.compile("([A-Z])([0-9]{0,2})(-?)([A-Z0-9]?)");
		Matcher matcher = pattern.matcher(value);
		if(!matcher.matches()){
			return Response.ok().build();
		}
		
		List<TabelaPrecoSuggestDTO> tabelaJSONList = new ArrayList<TabelaPrecoSuggestDTO>();
		List<TabelaPreco> tabelaPrecoList = buscaTabelas(matcher, subtipo, tpModal,apenasVigentes);
		
		for (TabelaPreco tabelaPreco : tabelaPrecoList) {
			tabelaJSONList.add(new TabelaPrecoSuggestDTO(tabelaPreco));
		}
		
		return Response.ok(tabelaJSONList).build();
	}

	private List<TabelaPreco> buscaTabelas(Matcher matcher, String subtipo, DomainValue tpModal, boolean apenasVigentes) {
		
		String tpTipoTabelaPreco = matcher.group(1);
		String nrVersaoString = matcher.group(2);
		Integer nrVersao = StringUtils.isNotBlank(nrVersaoString) ? Integer.valueOf(nrVersaoString) : null ;
		String tpSubtipoTabelaPreco = subtipo;
		if (StringUtils.isBlank(subtipo)) {
			tpSubtipoTabelaPreco = matcher.group(4);
			tpSubtipoTabelaPreco = StringUtils.isNotBlank(tpSubtipoTabelaPreco) ? tpSubtipoTabelaPreco : null;
		}
			
		List<TabelaPreco> tabelaPrecoList = tabelaPrecoService.findTabelaPrecoSuggest(tpTipoTabelaPreco, tpSubtipoTabelaPreco, nrVersao, tpModal, apenasVigentes);
		return tabelaPrecoList;
	}
	
	
	
}

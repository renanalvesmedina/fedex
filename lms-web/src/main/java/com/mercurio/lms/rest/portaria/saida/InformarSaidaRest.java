	package com.mercurio.lms.rest.portaria.saida;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.adsm.rest.PaginationDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.portaria.model.service.InformarSaidaService;
import com.mercurio.lms.rest.portaria.saida.contract.Saida;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/portaria/informarsaida")
public class InformarSaidaRest extends BaseRest {
	
	@InjectInJersey private InformarSaidaService saidaService;

	@POST
	@Path("/saidas/viagem")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findSaidasViagem() {
		List<Map<String, Object>> saidas = saidaService.findGridViagem(SessionUtils.getFilialSessao().getIdFilial());
		return Response.ok(wrapResult(convertSaidas(saidas))).build();
	}
	
	@POST
	@Path("/saidas/coletaEntrega")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response findSaidasColetaEntrega(Long portariaId) {
		List<Map<String, Object>> saidas = saidaService.findGridColetaEntrega(SessionUtils.getFilialSessao().getIdFilial(), portariaId);
		return Response.ok(wrapResult(convertSaidas(saidas))).build();
	}

	private List<Saida> convertSaidas(List<Map<String, Object>> saidas) {
		List<Saida> saidasDTO = new ArrayList<Saida>();
		for (Map<String, Object> saida : saidas) {
			saidasDTO.add(convertDTO(saida));
		}
		return saidasDTO;
	}
	
	private Saida convertDTO(Map<String, Object> m) {
		Saida saida = new Saida();
		saida.setPlaca(String.valueOf(m.get("nrIdentificador")));	
		saida.setFrota(String.valueOf(m.get("nrFrota")));
		
		saida.setFrotaSemiReboque(String.valueOf(m.get("nrFrotaReboque")));
		saida.setIdSemiReboque(String.valueOf(m.get("idSemiReboque")));
		
		saida.setSiglaFilial(String.valueOf(m.get("sgFilialCC")));
		saida.setControleCarga(String.valueOf(m.get("nrControleCarga")));
		
		saida.setFilialDestino((String) m.get("sgFilial"));
		
		saida.setNumeroRota(String.valueOf(m.get("nrRota")));
		saida.setDescricaoRota(String.valueOf(m.get("dsRota")));
		
		saida.setTipoControleCarga("");
		saida.setHoraSaidaPrevista(JTDateTimeUtils.formatDateTimeToString((DateTime) m.get("dhPrevisaoSaida")));
		
		return saida;
	}
	
	private PaginationDTO wrapResult(List<Saida> saidas) {
		PaginationDTO pagination = new PaginationDTO();
		pagination.setList(saidas);
		pagination.setQtRegistros(saidas.size());
		return pagination;
	}

}

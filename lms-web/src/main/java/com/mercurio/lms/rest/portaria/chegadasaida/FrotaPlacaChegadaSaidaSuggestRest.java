package com.mercurio.lms.rest.portaria.chegadasaida;

import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.portaria.model.service.PortariaService;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.FrotaPlacaChegadaSaidaSuggestDTO;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.FrotaPlacaChegadaSaidaSuggestFilterDTO;

@Path("portaria/frotaPlacaChegadaSaida")
public class FrotaPlacaChegadaSaidaSuggestRest extends BaseSuggestRest<FrotaPlacaChegadaSaidaSuggestDTO, FrotaPlacaChegadaSaidaSuggestFilterDTO> {
	
	@InjectInJersey
	private PortariaService portariaService;
	
	private Long idFilial;
	
	@Override
	protected Map<String, Object> filterConvert(
			FrotaPlacaChegadaSaidaSuggestFilterDTO filter) {
		setIdFilial(filter.getIdFilial());
		return filter.toMap();
	}

	@Override
	protected FrotaPlacaChegadaSaidaSuggestDTO responseConvert(Map<String, Object> parametros) {
		parametros.put("idFilial", getIdFilial());
		return new FrotaPlacaChegadaSaidaSuggestDTO(new TypedFlatMap(parametros));
	}

	@Override
	protected PortariaService getService() {
		return portariaService;
	}

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	
}
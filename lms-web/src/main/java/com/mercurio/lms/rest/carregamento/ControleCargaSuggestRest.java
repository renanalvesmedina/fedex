package com.mercurio.lms.rest.carregamento;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestFilterDTO;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/carregamento/controleCargaSuggest")
public class ControleCargaSuggestRest extends BaseSuggestRest<ControleCargaSuggestDTO, ControleCargaSuggestFilterDTO> {

	private static final int TRES = 3;
	private static final int QUATRO = 4;

	@InjectInJersey
	ControleCargaService controleCargaService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;

	@POST
	@Path("/findSuggestByToken")
	public Response findSuggestByToken(ControleCargaSuggestFilterDTO controleCargaSuggestFilterDTO, @Context HttpHeaders headers) {
		controleCargaSuggestFilterDTO.setIdEmpresa(integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
		return findSuggest(controleCargaSuggestFilterDTO);
	}
	

	@Override
	protected Map<String, Object> filterConvert(ControleCargaSuggestFilterDTO filter) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (filter.getValue() != null) {
			String val = filter.getValue().replace(" ", "");
			if (val.length() >= QUATRO) {
				String sgFilial = val.substring(0, TRES).toUpperCase();
				String sNrControleCarga = val.substring(TRES);
				
				if (StringUtils.isNumeric(sNrControleCarga)) {
					map.put("sgFilial", sgFilial);
					map.put("nrControleCarga", Long.valueOf(sNrControleCarga));
					map.put("idEmpresa", getIdEmpresaSessao(filter));
				}
			}
		}
		return map;
	}

	private Long getIdEmpresaSessao(ControleCargaSuggestFilterDTO filter){
		if(SessionUtils.getEmpresaSessao() == null){
			return filter.getIdEmpresa();
		}
		return  SessionUtils.getEmpresaSessao().getIdEmpresa();
	}

	@Override
	protected ControleCargaSuggestDTO responseConvert(Map<String, Object> map) {
		ControleCargaSuggestDTO controleCargaSuggestDTO = new ControleCargaSuggestDTO();
		
		if (map.get("idcontrolercarga") != null) {
			controleCargaSuggestDTO.setId(Long.valueOf(map.get("idcontrolercarga").toString()));
		}
		
		if (map.get("sgfilial") != null) {
			controleCargaSuggestDTO.setSgFilial(map.get("sgfilial").toString());
		}
		
		if (map.get("nrcontrolecarga") != null) {
			controleCargaSuggestDTO.setNrControleCarga(Long.valueOf(map.get("nrcontrolecarga").toString()));
		}
		
		if (map.get("dhgeracao") != null) {
			controleCargaSuggestDTO.setDhGeracao(new DateTime(map.get("dhgeracao")));
		}
		
		return controleCargaSuggestDTO;
	}

	@Override
	protected ControleCargaService getService() {
		return controleCargaService;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}

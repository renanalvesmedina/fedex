package com.mercurio.lms.rest.municipios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/municipios/filialPerfilSuggest")
public class FilialPerfilSuggestRest {
	
	public static final int MINIM_LENGTHG = 3;
	
	@InjectInJersey
	private FilialService filialService; 
	
	@InjectInJersey
	protected ParametroGeralService parametroGeralService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;

	@POST
	@Path("findFilialPerfilSuggest")
	public Response findFilialPerfilSuggest(Map<String, Object> data, @Context HttpHeaders headers) {
		Usuario usuario = null;
		Long idEmpresa = 0L;
		try {
			usuario = integracaoJwtService.getUsuarioSessao(integracaoJwtService.findAutenticacaoDMN(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
			idEmpresa = integracaoJwtService.getIdEmpresaByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
		}catch (Exception e){
			usuario = SessionUtils.getUsuarioLogado();
			idEmpresa = SessionUtils.getEmpresaSessao().getIdEmpresa();
		}

		String sgFilial = MapUtils.getString(data, "value");

		try{
			if (StringUtils.isBlank(sgFilial)) {
				return Response.ok().build();
			}

			if(!StringUtils.isNumeric(sgFilial) && sgFilial.length() != MINIM_LENGTHG) {
				return Response.ok().build();
			}

			List<Map<String, Object>> listFilial = filialService.findFilialPerfilSuggest(sgFilial, selectIdEmpresa(idEmpresa), usuario);
			if(CollectionUtils.isEmpty(listFilial)) {
				return Response.ok().build();
			}
			return Response.ok(convertListaFiliais(listFilial, idEmpresa)).build();
		}catch (Exception e) {
			return Response.serverError().build();
		}
	}

	private String selectIdEmpresa(Long idEmpresa){
		try{
			return SessionUtils.getEmpresaSessao().getIdEmpresa().toString();
		}catch (Exception e) {
			return integracaoJwtService.getEmpresaSessao(idEmpresa).getIdEmpresa().toString();
		}
	}
	
	private List<FilialSuggestDTO> convertListaFiliais(List<Map<String, Object>> listFilial, Long idEmpresa) {
		List<FilialSuggestDTO> result = new ArrayList<>();
		for (Map<String, Object> mapFilial : listFilial) {
			result.add(convertDTO(mapFilial, idEmpresa));
		}
		return result;
	}
	
	private FilialSuggestDTO convertDTO(Map<String, Object> map, Long idEmpresa) {
		FilialSuggestDTO filialSuggestDTO = new FilialSuggestDTO();
		
		if (map.get("idFilial") != null) {
			filialSuggestDTO.setId(Long.valueOf(map.get("idFilial").toString()));
		}
		
		if (map.get("sgFilial") != null) {
			filialSuggestDTO.setSgFilial(map.get("sgFilial").toString());
		}
		
		if (map.get("pessoa.nmFantasia") != null) {
			filialSuggestDTO.setNmFilial(map.get("pessoa.nmFantasia").toString());
		}

		return findEmpresaSessao(filialSuggestDTO, idEmpresa);
	}

	private FilialSuggestDTO findEmpresaSessao(FilialSuggestDTO filialSuggestDTO, Long idEmpresa){
		if(SessionUtils.getEmpresaSessao() != null){
			filialSuggestDTO.setIdEmpresa(SessionUtils.getEmpresaSessao().getIdEmpresa());
			filialSuggestDTO.setNmEmpresa(SessionUtils.getEmpresaSessao().getPessoa().getNmPessoa());
			return filialSuggestDTO;
		}
		filialSuggestDTO.setIdEmpresa(integracaoJwtService.getEmpresaSessao(idEmpresa).getIdEmpresa());
		filialSuggestDTO.setNmEmpresa(integracaoJwtService.getEmpresaSessao(idEmpresa).getPessoa().getNmPessoa());
		return filialSuggestDTO;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
	
}

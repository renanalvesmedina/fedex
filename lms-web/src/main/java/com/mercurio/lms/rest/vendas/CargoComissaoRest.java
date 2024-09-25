package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.security.model.service.UsuarioService;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;
import com.mercurio.lms.rest.vendas.dto.CargoComissaoDTO;
import com.mercurio.lms.rest.vendas.dto.CargoComissaoFilterDTO;
import com.mercurio.lms.rest.vendas.dto.CargoComissaoGridDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.CargoComissao;
import com.mercurio.lms.vendas.model.service.CargoComissaoService;
 
@Path("/vendas/cargoComissao") 
public class CargoComissaoRest extends BaseCrudRest<CargoComissaoDTO, CargoComissaoGridDTO, CargoComissaoFilterDTO> { 
 
	public static final String TP_PESSOA = "J";
	
	@InjectInJersey CargoComissaoService cargoComissaoService;
	@InjectInJersey UsuarioService usuarioService;
	@InjectInJersey DomainValueService domainValueService;
	

	@SuppressWarnings("unchecked")
	@POST
	@Path("findComissionadoSuggest")
	public Response findComissionadoSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");	
		List<String> notINrIdentificacao = (List<String>) MapUtils.getObject(data, "notINrIdentificacao");;
		
		return new SuggestResponseBuilder(this.findCliente(value, notINrIdentificacao), 5).build();
	}
	
	private List<Map<String, Object>> findCliente(String value, List<String> notINrIdentificacao) {
		if (StringUtils.isNotBlank(value)) {
			
			String aux = PessoaUtils.clearIdentificacao(value);
			if (StringUtils.isNumeric(aux)) {
				value = aux;
			}
			
			String nrIdentificacao = null;
			String nmPessoa = null;
			
			if (StringUtils.isNumeric(value) && value.length() >= 8 && value.length() <= 14) {
				nrIdentificacao = value;
			} else {
				nmPessoa = value;
			}
			
			List<Map<String, Object>> list = cargoComissaoService.findComissionadoSuggest(nrIdentificacao, nmPessoa, TP_PESSOA, notINrIdentificacao);

			return list;
			
		}
		
		return null;
	}
	
	@Override 
	protected List<CargoComissaoGridDTO> find(CargoComissaoFilterDTO cargoComissaoFilterDTO) {
		
		List<Map<String, Object>> results = cargoComissaoService.findToMappedResult(transformDTOtoCriteria(cargoComissaoFilterDTO));
		List<CargoComissaoGridDTO> gridResults = new ArrayList<CargoComissaoGridDTO>();
		for (Map<String, Object> cargoComissao : results) {
 			gridResults.add(convertToGridDTO(cargoComissao));
		}
		return gridResults;		
	} 
 
	private Map<String, Object> transformDTOtoCriteria(
			CargoComissaoFilterDTO cargoComissaoFilterDTO) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		
		criteria.put("idRegional",(cargoComissaoFilterDTO.getRegional() == null)? null : cargoComissaoFilterDTO.getRegional().getId());
		criteria.put("idFilial", (cargoComissaoFilterDTO.getFilial() == null)? null : cargoComissaoFilterDTO.getFilial().getIdFilial());
		criteria.put("idUsuario",(cargoComissaoFilterDTO.getUsuarioDTO() == null)? null : cargoComissaoFilterDTO.getUsuarioDTO().getId());
		criteria.put("tpCargo", cargoComissaoFilterDTO.getTpCargo());
		criteria.put("situacao", (cargoComissaoFilterDTO.getSituacaoAtivo() == null)? null : cargoComissaoFilterDTO.getSituacaoAtivo().getValue());
		
		return criteria;
	}

	@Override 
	protected Integer count(CargoComissaoFilterDTO cargoComissaoFilterDTO) { 
		return cargoComissaoService.findCount(transformDTOtoCriteria(cargoComissaoFilterDTO));
	} 
	
	@Override 
	protected CargoComissaoDTO findById(Long id) { 
		return convertToDTO(cargoComissaoService.findById(id)); 
	} 
 
	@Override 
	protected Long store(CargoComissaoDTO cargoComissaoDTO) { 
		return (Long) cargoComissaoService.store(convertToEntity(cargoComissaoDTO)); 
	} 
 
	@Override 
	protected void removeById(Long id) { 
		cargoComissaoService.updateStatusInativo(id); 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
		for (Long id : ids) {
			removeById(id);
		} 
	} 
	
	private CargoComissaoGridDTO convertToGridDTO(Map<String, Object> cargoComissao) {
		
		CargoComissaoGridDTO cargoComissaoGridDTO = new CargoComissaoGridDTO();

		cargoComissaoGridDTO.setId(Long.valueOf((String) cargoComissao.get("id")));
		cargoComissaoGridDTO.setRegional((String)cargoComissao.get("regional"));
		cargoComissaoGridDTO.setFilial((String)cargoComissao.get("filial"));
		cargoComissaoGridDTO.setNmFuncionario((String)cargoComissao.get("nmFuncionario"));
		cargoComissaoGridDTO.setNrCpf(FormatUtils.formatCPF((String)cargoComissao.get("nrCpf")));
		cargoComissaoGridDTO.setTpCargo(domainValueService.findDomainValueDescription("DM_TIPO_EXECUTIVO", (String)cargoComissao.get("tpCargo")));
		cargoComissaoGridDTO.setNrMatricula((String)cargoComissao.get("nrMatricula"));
		cargoComissaoGridDTO.setDtInclusaoComissao((String)cargoComissao.get("dtInclusaoComissao"));
		cargoComissaoGridDTO.setDtAdmissao((String)cargoComissao.get("dtAdmissao"));
		cargoComissaoGridDTO.setDtDemissao((String)cargoComissao.get("dtDemissao"));
		cargoComissaoGridDTO.setSituacao(isAtivo((YearMonthDay)cargoComissao.get("dtDesligamento")));
		
		return cargoComissaoGridDTO;
	}
	
	private String isAtivo(YearMonthDay dtDesligamento) {
		if(dtDesligamento == null){
			return "Ativo";
		}
		if(! dtDesligamento.isAfter(JTDateTimeUtils.getDataAtual())){
			return "Inativo";
		}
		return "";
	}

	private CargoComissaoDTO convertToDTO(CargoComissao cargoComissao) {
		if (cargoComissao == null) {
			return null;
		}
		
		UsuarioLMS usuarioLMS = cargoComissao.getUsuario();
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setIdUsuario(usuarioLMS.getIdUsuario());
		usuarioDTO.setNmUsuario(usuarioLMS.getUsuarioADSM().getNmUsuario());
		usuarioDTO.setNrMatricula(usuarioLMS.getUsuarioADSM().getNrMatricula());
		
		CargoComissaoDTO cargoComissaoDTO = new CargoComissaoDTO();
		cargoComissaoDTO.setDtDesligamento(cargoComissao.getDtDesligamento());
		cargoComissaoDTO.setId(cargoComissao.getIdCargoComissao());
		cargoComissaoDTO.setNrCpf(cargoComissao.getNrCpf());
		cargoComissaoDTO.setTpCargo(cargoComissao.getTpCargo());
		cargoComissaoDTO.setTpSituacao(cargoComissao.getTpSituacao());
		cargoComissaoDTO.setUsuarioDTO(usuarioDTO);
		
		return cargoComissaoDTO;
	}
	
	private CargoComissao convertToEntity(CargoComissaoDTO cargoComissaoDTO) {
		CargoComissao cargoComissao = new CargoComissao();
		
		cargoComissao.setIdCargoComissao(cargoComissaoDTO.getId());
		cargoComissao.setNrCpf(cargoComissaoDTO.getNrCpf());
		cargoComissao.setTpCargo(cargoComissaoDTO.getTpCargo());
		cargoComissao.setTpSituacao(cargoComissaoDTO.getTpSituacao());
		cargoComissao.setDtDesligamento(cargoComissaoDTO.getDtDesligamento());
		
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(cargoComissaoDTO.getUsuarioDTO().getIdUsuario());
		cargoComissao.setUsuario(usuarioLMS);
		
		return cargoComissao;
	}
} 

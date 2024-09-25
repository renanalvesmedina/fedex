package com.mercurio.lms.rest.tributos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.rest.tributos.dto.AliquotaFundoCombatePobrezaDTO;
import com.mercurio.lms.rest.tributos.dto.AliquotaFundoCombatePobrezaFilterDTO;
import com.mercurio.lms.rest.tributos.dto.AliquotaFundoCombatePobrezaListDTO;
import com.mercurio.lms.tributos.model.AliquotaFundoCombatePobreza;
import com.mercurio.lms.tributos.model.service.AliquotaFundoCombatePobrezaService;
import com.mercurio.lms.util.JTDateTimeUtils;

@Path("/tributos/manterAliquotaFundoCombatePobreza")
public class AliquotaFundoCombatePobrezaRest
		extends BaseCrudRest<AliquotaFundoCombatePobrezaDTO, AliquotaFundoCombatePobrezaListDTO, AliquotaFundoCombatePobrezaFilterDTO> {

	@InjectInJersey
	AliquotaFundoCombatePobrezaService aliquotaFundoCombatePobrezaService;

	@InjectInJersey
	UnidadeFederativaService unidadeFederativaService;
	
	@POST
	@SuppressWarnings("unchecked")
	@Path("findUnidadeFederativaCombo")
	public Map<String, Object> findUnidadeFederativaCombo() {
		List<Map<String, Object>> ufs = unidadeFederativaService.findUfsBySgPais("BRA");
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("unidadesFederativas", ufs);
		return retorno;
	}
	
	@Override
	protected AliquotaFundoCombatePobrezaDTO findById(Long id) {
		AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza = aliquotaFundoCombatePobrezaService.findById(id);
		return converToAliquotaFundoCombatePobrezaDTO(aliquotaFundoCombatePobreza);
	}

	private AliquotaFundoCombatePobrezaDTO converToAliquotaFundoCombatePobrezaDTO(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		AliquotaFundoCombatePobrezaDTO aliquotaFundoCombatePobrezaDTO = new AliquotaFundoCombatePobrezaDTO();
		aliquotaFundoCombatePobrezaDTO.setId(aliquotaFundoCombatePobreza.getIdAliquotaFundoCombatePobreza());
		aliquotaFundoCombatePobrezaDTO.setIdUsuarioInclusao(aliquotaFundoCombatePobreza.getUsuarioInclusao().getIdUsuario());
		aliquotaFundoCombatePobrezaDTO.setPcAliquota(aliquotaFundoCombatePobreza.getPcAliquota());
		aliquotaFundoCombatePobrezaDTO.setDtVigenciaInicial(aliquotaFundoCombatePobreza.getDtVigenciaInicial());
		aliquotaFundoCombatePobrezaDTO.setDtVigenciaFinal(aliquotaFundoCombatePobreza.getDtVigenciaFinal());
		aliquotaFundoCombatePobrezaDTO.setDhInclusao(aliquotaFundoCombatePobreza.getDhInclusao());
		
		Map<String, Object> unidadeFederativaMap = new HashMap<String, Object>();
		unidadeFederativaMap.put("idUnidadeFederativa", aliquotaFundoCombatePobreza.getUnidadeFederativa().getIdUnidadeFederativa());
		aliquotaFundoCombatePobrezaDTO.setUnidadeFederativa(unidadeFederativaMap);
		
		executeBloqueioCampos(aliquotaFundoCombatePobrezaDTO);
		
		return aliquotaFundoCombatePobrezaDTO;
	}

	private void executeBloqueioCampos(AliquotaFundoCombatePobrezaDTO aliquotaFundoCombatePobrezaDTO) {
		if(aliquotaFundoCombatePobrezaDTO.getDtVigenciaFinal() != null 
				&& aliquotaFundoCombatePobrezaDTO.getDtVigenciaFinal().isBefore(JTDateTimeUtils.getDataAtual())){
			aliquotaFundoCombatePobrezaDTO.setBlDisableSalvar(Boolean.TRUE);
			aliquotaFundoCombatePobrezaDTO.setBlDisableExcluir(Boolean.TRUE);
			aliquotaFundoCombatePobrezaDTO.setBlDisableUfDestino(Boolean.TRUE);
			aliquotaFundoCombatePobrezaDTO.setBlDisableAliquota(Boolean.TRUE);
			aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaInicial(Boolean.TRUE);
			aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaFinal(Boolean.TRUE);
			
		} else {
			if(aliquotaFundoCombatePobrezaDTO.getDtVigenciaInicial().isAfter(JTDateTimeUtils.getDataAtual())){
				aliquotaFundoCombatePobrezaDTO.setBlDisableSalvar(Boolean.FALSE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableExcluir(Boolean.FALSE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableUfDestino(Boolean.TRUE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableAliquota(Boolean.FALSE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaInicial(Boolean.FALSE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaFinal(Boolean.FALSE);
			} else {
				aliquotaFundoCombatePobrezaDTO.setBlDisableSalvar(Boolean.FALSE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableExcluir(Boolean.TRUE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableUfDestino(Boolean.TRUE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableAliquota(Boolean.TRUE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaInicial(Boolean.TRUE);
				aliquotaFundoCombatePobrezaDTO.setBlDisableDtVigenciaFinal(Boolean.FALSE);
			}
		}
	}

	@Override
	protected Long store(AliquotaFundoCombatePobrezaDTO aliquotaFundoCombatePobrezaDTO) {
		AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza = convertToAliquotaFundoCombatePobreza(aliquotaFundoCombatePobrezaDTO);
		aliquotaFundoCombatePobrezaService.store(aliquotaFundoCombatePobreza);
		return aliquotaFundoCombatePobreza.getIdAliquotaFundoCombatePobreza();
	}

	private AliquotaFundoCombatePobreza convertToAliquotaFundoCombatePobreza(AliquotaFundoCombatePobrezaDTO aliquotaFundoCombatePobrezaDTO) {
		AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza = new AliquotaFundoCombatePobreza();
		aliquotaFundoCombatePobreza.setIdAliquotaFundoCombatePobreza(aliquotaFundoCombatePobrezaDTO.getId());
		aliquotaFundoCombatePobreza.setPcAliquota(aliquotaFundoCombatePobrezaDTO.getPcAliquota());
		aliquotaFundoCombatePobreza.setDtVigenciaInicial(aliquotaFundoCombatePobrezaDTO.getDtVigenciaInicial());
		aliquotaFundoCombatePobreza.setDtVigenciaFinal(aliquotaFundoCombatePobrezaDTO.getDtVigenciaFinal());
		aliquotaFundoCombatePobreza.setDhInclusao(aliquotaFundoCombatePobrezaDTO.getDhInclusao());

		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();
		unidadeFederativa.setIdUnidadeFederativa(MapUtilsPlus.getLong(aliquotaFundoCombatePobrezaDTO.getUnidadeFederativa(), "idUnidadeFederativa"));
		aliquotaFundoCombatePobreza.setUnidadeFederativa(unidadeFederativa);

		UsuarioLMS usuarioInclusao = new UsuarioLMS();
		usuarioInclusao.setIdUsuario(aliquotaFundoCombatePobrezaDTO.getIdUsuarioInclusao());
		aliquotaFundoCombatePobreza.setUsuarioInclusao(usuarioInclusao);

		return aliquotaFundoCombatePobreza;
	}

	@Override
	protected void removeById(Long id) {
		aliquotaFundoCombatePobrezaService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		aliquotaFundoCombatePobrezaService.removeByIds(ids);
	}

	@Override
	protected List<AliquotaFundoCombatePobrezaListDTO> find(AliquotaFundoCombatePobrezaFilterDTO aliquotaFundoCombatePobrezaFilterDTO) {
		TypedFlatMap criteria = getTypedFlatMap(aliquotaFundoCombatePobrezaFilterDTO);
		ResultSetPage<AliquotaFundoCombatePobreza> rps = aliquotaFundoCombatePobrezaService.findPaginated(criteria);
		return convertToAliquotaFundoCombatePobrezaListDTO(rps.getList());
	}

	private List<AliquotaFundoCombatePobrezaListDTO> convertToAliquotaFundoCombatePobrezaListDTO(List<AliquotaFundoCombatePobreza> listaEntidade) {
		List<AliquotaFundoCombatePobrezaListDTO> listaDTO = new ArrayList<AliquotaFundoCombatePobrezaListDTO>();
		
		for (AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza : listaEntidade) {
			AliquotaFundoCombatePobrezaListDTO aliquotaFundoCombatePobrezaDTO = new AliquotaFundoCombatePobrezaListDTO();
			aliquotaFundoCombatePobrezaDTO.setId(aliquotaFundoCombatePobreza.getIdAliquotaFundoCombatePobreza());
			aliquotaFundoCombatePobrezaDTO.setUfDestino(aliquotaFundoCombatePobreza.getUnidadeFederativa().getSiglaDescricao());
			aliquotaFundoCombatePobrezaDTO.setPcAliquota(aliquotaFundoCombatePobreza.getPcAliquota());
			aliquotaFundoCombatePobrezaDTO.setDtVigenciaInicial(aliquotaFundoCombatePobreza.getDtVigenciaInicial());
			aliquotaFundoCombatePobrezaDTO.setDtVigenciaFinal(aliquotaFundoCombatePobreza.getDtVigenciaFinal());
			
			listaDTO.add(aliquotaFundoCombatePobrezaDTO);
		}
		
		return listaDTO;
	}

	@Override
	protected Integer count(AliquotaFundoCombatePobrezaFilterDTO aliquotaFundoCombatePobrezaFilterDTO) {
		TypedFlatMap criteria = getTypedFlatMap(aliquotaFundoCombatePobrezaFilterDTO);
		return aliquotaFundoCombatePobrezaService.getRowCount(criteria);
	}

	private TypedFlatMap getTypedFlatMap(AliquotaFundoCombatePobrezaFilterDTO aliquotaFundoCombatePobrezaFilterDTO) {
		TypedFlatMap toReturn = super.getTypedFlatMapWithPaginationInfo(aliquotaFundoCombatePobrezaFilterDTO);
		if(aliquotaFundoCombatePobrezaFilterDTO.getUnidadeFederativa() != null){
			toReturn.put("idUnidadeFederativa", MapUtilsPlus.getLong(aliquotaFundoCombatePobrezaFilterDTO.getUnidadeFederativa(), "idUnidadeFederativa"));
		}
		toReturn.put("pcAliquota", aliquotaFundoCombatePobrezaFilterDTO.getPcAliquota());
		toReturn.put("dtVigencia", aliquotaFundoCombatePobrezaFilterDTO.getDtVigencia());
		return toReturn;
	}
}
package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.vendas.dto.ExecutivoTerritorioDTO;
import com.mercurio.lms.rest.vendas.dto.ExecutivoTerritorioFilterDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestDTO;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;
 
@Path("/vendas/executivoTerritorio") 
public class ExecutivoTerritorioRest extends BaseCrudRest<ExecutivoTerritorioDTO, ExecutivoTerritorioDTO, ExecutivoTerritorioFilterDTO> { 
 
	@InjectInJersey
	private ExecutivoTerritorioService executivoTerritorioService;
	
	@Override
	protected Integer count(ExecutivoTerritorioFilterDTO filter) {
		return executivoTerritorioService.findCount(createExecutivoTerritorioMap(filter));
	}

	@Override
	protected List<ExecutivoTerritorioDTO> find(ExecutivoTerritorioFilterDTO dto) {
		List<ExecutivoTerritorio> executivos = executivoTerritorioService.find(createExecutivoTerritorioMap(dto));
		return converteParaExecutivoDTO(executivos);
	}

	@Override
	protected ExecutivoTerritorioDTO findById(Long id) {
		ExecutivoTerritorio executivo = executivoTerritorioService.findById(id);
		return converteParaExecutivoTerritorioDTO(executivo);
	}

	private List<ExecutivoTerritorioDTO> converteParaExecutivoDTO(List<ExecutivoTerritorio> executivoTerritorio) {
		List<ExecutivoTerritorioDTO> list = new ArrayList<ExecutivoTerritorioDTO>();
		for (ExecutivoTerritorio e : executivoTerritorio) {
			list.add(converteParaExecutivoTerritorioDTO(e));
		}
		return list;
	}
	
	private ExecutivoTerritorioDTO converteParaExecutivoTerritorioDTO(ExecutivoTerritorio executivoTerritorio) {
		ExecutivoTerritorioDTO executivoDTO = new ExecutivoTerritorioDTO();
		
		Long idTerritorio = executivoTerritorio.getTerritorio().getIdTerritorio();
		String nmTerritorio = executivoTerritorio.getTerritorio().getNmTerritorio();
		TerritorioSuggestDTO territorioDto = new TerritorioSuggestDTO(idTerritorio, nmTerritorio);

		YearMonthDay periodoInicialDto = executivoTerritorio.getDtVigenciaInicial();
		YearMonthDay periodoFinalDto = executivoTerritorio.getDtVigenciaFinal();
		DomainValue tpExecutivoDto = executivoTerritorio.getTpExecutivo();
		
		Long idUsuario = executivoTerritorio.getUsuario().getIdUsuario();
		String nmUsuario = executivoTerritorio.getUsuario().getUsuarioADSM().getNmUsuario();
		String nrMatricula = executivoTerritorio.getUsuario().getUsuarioADSM().getNrMatricula();
		
		UsuarioDTO usuarioDto = new UsuarioDTO(idUsuario, nmUsuario, nrMatricula);
		
		executivoDTO.setId(executivoTerritorio.getIdExecutivoTerritorio());
		executivoDTO.setTerritorio(territorioDto);
		executivoDTO.setPeriodoInicial(periodoInicialDto);
		executivoDTO.setPeriodoFinal(periodoFinalDto);
		executivoDTO.setTpExecutivo(tpExecutivoDto);
		executivoDTO.setUsuario(usuarioDto);

		return executivoDTO;
	}

	@Override
	protected void removeById(Long id) {
		executivoTerritorioService.removeById(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}

	@Override
	protected Long store(ExecutivoTerritorioDTO executivoTerritorioDTO) {
		ExecutivoTerritorio executivoTerritorio = (ExecutivoTerritorio) executivoTerritorioService.store(createExecutivoTerritorio(executivoTerritorioDTO));
		return executivoTerritorio.getIdExecutivoTerritorio();
	}

	private ExecutivoTerritorio createExecutivoTerritorio(ExecutivoTerritorioDTO dto) {
		ExecutivoTerritorio executivoTerritorio = new ExecutivoTerritorio();
		
		executivoTerritorio.setIdExecutivoTerritorio(dto.getId());
		
		if (dto.getTerritorio() != null) {
			Territorio territorio = new Territorio();
			territorio.setIdTerritorio(dto.getTerritorio().getId());
			executivoTerritorio.setTerritorio(territorio);
		}
		
		executivoTerritorio.setDtVigenciaInicial(dto.getPeriodoInicial());
		executivoTerritorio.setDtVigenciaInicial(dto.getPeriodoFinal());
		executivoTerritorio.setTpExecutivo(dto.getTpExecutivo());
		
		if (dto.getUsuario() != null) {
			UsuarioLMS usuarioLMS = new UsuarioLMS();
			usuarioLMS.setIdUsuario(dto.getUsuario().getIdUsuario());
			executivoTerritorio.setUsuario(usuarioLMS);
		}

		return executivoTerritorio;
	}

	private Map<String, Object> createExecutivoTerritorioMap(ExecutivoTerritorioFilterDTO dto) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idExecutivoTerritorio", dto.getId());
		
		if (dto.getTerritorio() != null) {
			map.put("idTerritorio", dto.getTerritorio().getId());
		}

		map.put("vigenciaInicial", dto.getPeriodoInicial());
		map.put("vigenciaFinal", dto.getPeriodoFinal());
		map.put("tpExecutivo", dto.getTpExecutivo());
		
		if (dto.getUsuario() != null) {
			map.put("idFuncionario", dto.getUsuario().getIdUsuario());
		}

		return map;
	}

	public ExecutivoTerritorioService getExecutivoTerritorioService() {
		return executivoTerritorioService;
	}
	
	public void setExecutivoTerritorioService(ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

} 

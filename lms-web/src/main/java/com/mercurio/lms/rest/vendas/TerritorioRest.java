package com.mercurio.lms.rest.vendas;

import static com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService.DOMINIO_TP_EXECUTIVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalChosenDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ExecutivoTerritorioDTO;
import com.mercurio.lms.rest.vendas.dto.RecalculoComissaoDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioFilterDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioGridDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestDTO;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.ComissaoGarantida;
import com.mercurio.lms.vendas.model.DiferencaComissao;
import com.mercurio.lms.vendas.model.ExecutivoTerritorio;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.ComissaoGarantidaService;
import com.mercurio.lms.vendas.model.service.DiferencaComissaoService;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;
import com.mercurio.lms.vendas.model.service.TerritorioService;

@Path("/vendas/territorio")
public class TerritorioRest extends BaseCrudRest<TerritorioDTO, TerritorioGridDTO, TerritorioFilterDTO> {
	
	public static final String EXCEPTION_STORE_PERIODO_INFORMADO_FORA_DA_VIGENCIA = "LMS-29090";

	@InjectInJersey
	private TerritorioService territorioService;
	
	@InjectInJersey
	private ExecutivoTerritorioService executivoTerritorioService;
	
	@InjectInJersey
	private DomainValueService domainValueService;

	@InjectInJersey
	private FilialService filialService;
	
	@InjectInJersey
	private RegionalService regionalService;

	@InjectInJersey
	private ComissaoGarantidaService comissaoGarantidaService;

	@InjectInJersey
	private DiferencaComissaoService diferencaComissaoService;

	@POST
	@Path("/findRegionalByFilial")
	public RegionalChosenDTO findRegionalByFilial(Long idFilial) {
		if (idFilial == null) {
			return null;
		}
		
		Regional regional = regionalService.findRegionalAtivaByIdFilial(idFilial);
		if (regional == null) {
			return null;
		}
		
		RegionalChosenDTO regionalChosenDTO = new RegionalChosenDTO(regional.getIdRegional(), regional.getDsRegional());
		return regionalChosenDTO;
	}
	
	@POST
	@Path("/findHistoricoEquipeVendas")
	public Response findHistorioEquipeVendas(Long idTerritorio) {
		Map<String, Object> mapFilter = new HashMap<String, Object>();
		mapFilter.put("idTerritorio", idTerritorio);
		List<ExecutivoTerritorio> listaExecTerritorio = executivoTerritorioService.find(mapFilter);
		
		List<ExecutivoTerritorioDTO> equipe = new ArrayList<ExecutivoTerritorioDTO>();
		for (ExecutivoTerritorio executivoTerritorio : listaExecTerritorio) {
			equipe.add(createMembroEquipe(executivoTerritorio));
		}
		Collections.sort(equipe, new Comparator<ExecutivoTerritorioDTO>() {
			@Override
			public int compare(ExecutivoTerritorioDTO o1, ExecutivoTerritorioDTO o2) {
				int comparison = o1.getTpExecutivo().getDescriptionAsString().compareTo(o2.getTpExecutivo().getDescriptionAsString());
				if (comparison != 0) {
					return comparison;
				}
				return o1.getPeriodoInicial().compareTo(o2.getPeriodoInicial()) * -1;//order desc
			}
		});
		
		return getReturnFind(equipe, equipe.size());
	}
	
	@POST
	@Path("/findEquipeVendas")
	public Response findEquipeVendas(Long idTerritorio) {
		List<ExecutivoTerritorioDTO> equipe = new ArrayList<ExecutivoTerritorioDTO>();
		
		List<ExecutivoTerritorio> listaExecTerritorio = executivoTerritorioService.findEquipeVendas(idTerritorio);
		Map<DomainValue, ExecutivoTerritorio> mapa = mapearListaExecutivoTerritorio(listaExecTerritorio);
		
		List<DomainValue> tiposExecutivo = domainValueService.findDomainValues(DOMINIO_TP_EXECUTIVO);
		sortByDomainValueDescription(tiposExecutivo);

		// Por solicitação do usuário, Executivo Interno foi colocado em primeiro na lista
		Collections.rotate(tiposExecutivo.subList(0, 2), -1);
		
		for (DomainValue tipoExecutivo : tiposExecutivo) {
			ExecutivoTerritorio executivoTerritorio = mapa.get(tipoExecutivo);
			if (executivoTerritorio == null) {
				Territorio territorio = new Territorio();
				territorio.setIdTerritorio(idTerritorio);
				
				executivoTerritorio = new ExecutivoTerritorio();
				executivoTerritorio.setTerritorio(territorio);
				executivoTerritorio.setTpExecutivo(tipoExecutivo);
			}
			
			equipe.add(createMembroEquipe(executivoTerritorio));
		}
		return getReturnFind(equipe, equipe.size());
	}
	
	@POST
	@Path("/findRecalculoComissao")
	public Response findRecalculoComissao(Long idTerritorio) {
		List<RecalculoComissaoDTO> equipe = new ArrayList<RecalculoComissaoDTO>();
		
		List<ExecutivoTerritorio> listaExecTerritorio = executivoTerritorioService.findEquipeVendas(idTerritorio);
		Map<DomainValue, ExecutivoTerritorio> mapa = mapearListaExecutivoTerritorio(listaExecTerritorio);
		
		List<DomainValue> tiposExecutivo = domainValueService.findDomainValues(DOMINIO_TP_EXECUTIVO);
		sortByDomainValueDescription(tiposExecutivo);

		// Por solicitação do usuário, Executivo Interno foi colocado em primeiro na lista
		Collections.rotate(tiposExecutivo.subList(0, 2), -1);
		
		for (DomainValue tipoExecutivo : tiposExecutivo) {
			ExecutivoTerritorio executivoTerritorio = mapa.get(tipoExecutivo);
			ComissaoGarantida comissaoGarantida = new ComissaoGarantida();
			DiferencaComissao diferencaComissao = new DiferencaComissao();

			if (executivoTerritorio == null) {
				Territorio territorio = new Territorio();
				territorio.setIdTerritorio(idTerritorio);

				executivoTerritorio = new ExecutivoTerritorio();
				executivoTerritorio.setTerritorio(territorio);
				executivoTerritorio.setTpExecutivo(tipoExecutivo);
			} else {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("idExecutivoTerritorio", executivoTerritorio.getIdExecutivoTerritorio());
				
				List<ComissaoGarantida> comissaoGarantidaList = comissaoGarantidaService.find(map);
				List<DiferencaComissao> diferencaComissaoList = diferencaComissaoService.find(map);
				
				if (comissaoGarantidaList.size() > 0) comissaoGarantida = comissaoGarantidaList.get(0);
				if (diferencaComissaoList.size() > 0) diferencaComissao = diferencaComissaoList.get(0);
			}
			
			equipe.add(createRecalculoEquipe(executivoTerritorio, comissaoGarantida, diferencaComissao));
		}
		return getReturnFind(equipe, equipe.size());
	}
	
	@POST
	@Path("/salvarEquipeVendas")
	public Response salvarEquipeVendas(List<ExecutivoTerritorioDTO> equipeVendasDTO) {
		List<ExecutivoTerritorio> equipeVendas = convertToEntityList(equipeVendasDTO);
		executivoTerritorioService.storeAll(equipeVendas);
		return Response.ok().build();
	}
	
	@POST
	@Path("/salvarRecalculo")
	public Response salvarRecalculo(List<RecalculoComissaoDTO> recalculoComissaoDTOList) {

		List<ComissaoGarantida> comissaoGarantidaList = new ArrayList<ComissaoGarantida>();
		List<DiferencaComissao> diferencaComissaoList = new ArrayList<DiferencaComissao>();
		
		ComissaoGarantida comissaoGarantida;
		DiferencaComissao diferencaComissao;

		for (RecalculoComissaoDTO recalculoComissaoDTO : recalculoComissaoDTOList) {

			// Testa se existe executivo para este recálculo 
			if (recalculoComissaoDTO.getExecutivoTerritorio() != null) {
				
				// Testa se existe registro para Comissão Garantida
				if ((recalculoComissaoDTO.getIdComissaoGarantida() != null) || (recalculoComissaoDTO.getVlComissaoGarantida() != null)) {
					comissaoGarantida = new ComissaoGarantida();
					comissaoGarantida.setIdComissaoGarantida(recalculoComissaoDTO.getIdComissaoGarantida());
					
					ExecutivoTerritorio executivoTerritorio = new ExecutivoTerritorio();
					executivoTerritorio.setIdExecutivoTerritorio(recalculoComissaoDTO.getExecutivoTerritorio().getId());
					comissaoGarantida.setExecutivoTerritorio(executivoTerritorio);
					
					comissaoGarantida.setVlComissao(recalculoComissaoDTO.getVlComissaoGarantida());
					
					if ((recalculoComissaoDTO.getDtInicio() != null) && 
						(recalculoComissaoDTO.getDtVigenciaEquipeVendasInicial() != null) &&
						(recalculoComissaoDTO.getDtInicio().isBefore(recalculoComissaoDTO.getDtVigenciaEquipeVendasInicial()))) {
						throw new BusinessException(EXCEPTION_STORE_PERIODO_INFORMADO_FORA_DA_VIGENCIA);	
					}

					if ((recalculoComissaoDTO.getDtFim() != null) && 
						(recalculoComissaoDTO.getDtVigenciaEquipeVendasFinal() != null) &&
						(recalculoComissaoDTO.getDtFim().isAfter(recalculoComissaoDTO.getDtVigenciaEquipeVendasFinal()))) {
						throw new BusinessException(EXCEPTION_STORE_PERIODO_INFORMADO_FORA_DA_VIGENCIA);	
					}
					
					comissaoGarantida.setDtInicio(recalculoComissaoDTO.getDtInicio());
					comissaoGarantida.setDtFim(recalculoComissaoDTO.getDtFim());
					
					comissaoGarantidaList.add(comissaoGarantida);
				}

				// Testa se existe registro para Diferença de Comissão
				if ((recalculoComissaoDTO.getIdDiferencaComissao() != null) || (recalculoComissaoDTO.getVlDiferencaComissao() != null)) {
					diferencaComissao = new DiferencaComissao();
					diferencaComissao.setIdDiferencaComissao(recalculoComissaoDTO.getIdDiferencaComissao());
					
					ExecutivoTerritorio executivoTerritorio = new ExecutivoTerritorio();
					executivoTerritorio.setIdExecutivoTerritorio(recalculoComissaoDTO.getExecutivoTerritorio().getId());
					diferencaComissao.setExecutivoTerritorio(executivoTerritorio);

					diferencaComissao.setVlComissao(recalculoComissaoDTO.getVlDiferencaComissao());
					diferencaComissao.setDtCompetencia(recalculoComissaoDTO.getDtCompetencia());
					diferencaComissao.setTpTeto(recalculoComissaoDTO.getTpTeto());
					diferencaComissao.setDsObservacao(recalculoComissaoDTO.getDsObservacao());

					diferencaComissaoList.add(diferencaComissao);
				}
			}
		}
		
		comissaoGarantidaService.storeListaComissaoGarantida(comissaoGarantidaList);
		diferencaComissaoService.storeListaDiferencaComissao(diferencaComissaoList);

		return Response.ok().build();
	}

	@Override
	protected Long store(TerritorioDTO dto) {
		Territorio territorio = new Territorio();
		territorio.setIdTerritorio(dto.getId());

		if (dto.getRegional() != null) {
			Regional regional = new Regional();
			regional.setIdRegional(dto.getRegional().getId());
			territorio.setRegional(regional);
		}

		if (dto.getFilial() != null) {
			Filial filial = new Filial();
			filial.setIdFilial(dto.getFilial().getId());
			territorio.setFilial(filial);
		}

		territorio.setNmTerritorio(dto.getNmTerritorio());

		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		territorio.setUsuarioAlteracao(usuario);
		return (Long) territorioService.store(territorio);
	}

	@Override
	protected void removeById(Long id) {
		territorioService.updateStatusInativo(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}

	@Override
	protected TerritorioDTO findById(Long id) {
		Territorio territorio = territorioService.findById(id);
		return convertToDto(territorio);
	}

	@Override
	protected List<TerritorioGridDTO> find(TerritorioFilterDTO filter) {
		Long regionalId = filter.getRegional() != null ? filter.getRegional().getId() : null;
		Long filialId = filter.getFilial() != null ? filter.getFilial().getId() : null;
		List<Territorio> territorioList = territorioService.find(regionalId, filialId, filter.getNmTerritorio(), null);
		return convertToGridDTOList(territorioList);
	}

	@Override
	protected Integer count(TerritorioFilterDTO filter) {
		Long regionalId = filter.getRegional() != null ? filter.getRegional().getId() : null;
		Long filialId = filter.getFilial() != null ? filter.getFilial().getId() : null;
		return territorioService.findCount(regionalId, filialId, filter.getNmTerritorio(), null);
	}
	
	private List<ExecutivoTerritorio> convertToEntityList(List<ExecutivoTerritorioDTO> equipeVendasDTO) {
		UsuarioLMS usuarioAlteracao = new UsuarioLMS();
		usuarioAlteracao.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		List<ExecutivoTerritorio> equipeVendas = new ArrayList<ExecutivoTerritorio>();
		for (ExecutivoTerritorioDTO executivoTerritorioDTO : equipeVendasDTO) {
			if (!isRemoveItem(executivoTerritorioDTO)) {
				ExecutivoTerritorio executivoTerritorio = new ExecutivoTerritorio();
				executivoTerritorio.setIdExecutivoTerritorio(executivoTerritorioDTO.getId());
				
				Territorio territorio = new Territorio();
				territorio.setIdTerritorio(executivoTerritorioDTO.getTerritorio().getId());
				executivoTerritorio.setTerritorio(territorio);
				
				executivoTerritorio.setTpExecutivo(executivoTerritorioDTO.getTpExecutivo());
				
				UsuarioLMS usuario = new UsuarioLMS();
				usuario.setIdUsuario(executivoTerritorioDTO.getUsuario().getIdUsuario());
				executivoTerritorio.setUsuario(usuario);
				
				executivoTerritorio.setDtVigenciaInicial(executivoTerritorioDTO.getPeriodoInicial());
				executivoTerritorio.setDtVigenciaFinal(executivoTerritorioDTO.getPeriodoFinal());
				
				executivoTerritorio.setUsuarioAlteracao(usuarioAlteracao);
				
				equipeVendas.add(executivoTerritorio);
			}
		}
		return equipeVendas;
	}
	
	private boolean isRemoveItem(ExecutivoTerritorioDTO executivoTerritorioDTO) {
		return executivoTerritorioDTO.getUsuario() == null 
				&& executivoTerritorioDTO.getPeriodoInicial() == null
				&& executivoTerritorioDTO.getPeriodoFinal() == null;
	}
	
	private void sortByDomainValueDescription(List<DomainValue> tiposExecutivo) {
		Collections.sort(tiposExecutivo, new Comparator<DomainValue>() {
			@Override
			public int compare(DomainValue o1, DomainValue o2) {
				return o1.getDescriptionAsString().compareToIgnoreCase(o2.getDescriptionAsString());
			}
		});
	}
	
	private ExecutivoTerritorioDTO createMembroEquipe(ExecutivoTerritorio executivoTerritorio) {
		TerritorioSuggestDTO territorioDTO = new TerritorioSuggestDTO();
		territorioDTO.setId(executivoTerritorio.getTerritorio().getIdTerritorio());
		
		ExecutivoTerritorioDTO membroEquipe = new ExecutivoTerritorioDTO();
		DomainValue tpExecutivo = executivoTerritorio.getTpExecutivo();
		tpExecutivo.setDomain(null);
		membroEquipe.setTpExecutivo(tpExecutivo);
		
		membroEquipe.setPeriodoInicial(executivoTerritorio.getDtVigenciaInicial());
		membroEquipe.setPeriodoFinal(executivoTerritorio.getDtVigenciaFinal());
		membroEquipe.setTerritorio(territorioDTO);
		
		UsuarioLMS usuarioLMS = executivoTerritorio.getUsuario();
		if (usuarioLMS != null) {
			UsuarioDTO usuarioDTO = new UsuarioDTO(usuarioLMS.getIdUsuario(), usuarioLMS.getUsuarioADSM().getNmUsuario(), usuarioLMS.getUsuarioADSM().getNrMatricula());
			membroEquipe.setUsuario(usuarioDTO);
		} 
		
		membroEquipe.setId(executivoTerritorio.getIdExecutivoTerritorio());
		return membroEquipe;
	}
	
	private RecalculoComissaoDTO createRecalculoEquipe(ExecutivoTerritorio executivoTerritorio, ComissaoGarantida comissaoGarantida, DiferencaComissao diferencaComissao) {
		RecalculoComissaoDTO recalculoComissaoDTO = new RecalculoComissaoDTO();
		recalculoComissaoDTO.setExecutivoTerritorio(createMembroEquipe(executivoTerritorio));

		recalculoComissaoDTO.setDtVigenciaEquipeVendasInicial(executivoTerritorio.getDtVigenciaInicial());
		recalculoComissaoDTO.setDtVigenciaEquipeVendasFinal(executivoTerritorio.getDtVigenciaFinal());

		if (comissaoGarantida != null) {
			recalculoComissaoDTO.setIdComissaoGarantida(comissaoGarantida.getIdComissaoGarantida());
			recalculoComissaoDTO.setVlComissaoGarantida(comissaoGarantida.getVlComissao());
			recalculoComissaoDTO.setDtInicio(comissaoGarantida.getDtInicio());
			recalculoComissaoDTO.setDtFim(comissaoGarantida.getDtFim());
		}

		if (diferencaComissao != null) {
			recalculoComissaoDTO.setIdDiferencaComissao(diferencaComissao.getIdDiferencaComissao());
			recalculoComissaoDTO.setVlDiferencaComissao(diferencaComissao.getVlComissao());
			recalculoComissaoDTO.setDtCompetencia(diferencaComissao.getDtCompetencia());
			recalculoComissaoDTO.setTpTeto(diferencaComissao.getTpTeto());
			recalculoComissaoDTO.setDsObservacao(diferencaComissao.getDsObservacao());
		}

		return recalculoComissaoDTO;
	}
	
	private Map<DomainValue, ExecutivoTerritorio> mapearListaExecutivoTerritorio(List<ExecutivoTerritorio> listaExecTerritorio) {
		Map<DomainValue, ExecutivoTerritorio> mapa = new HashMap<DomainValue, ExecutivoTerritorio>();
		for (ExecutivoTerritorio executivoTerritorio : listaExecTerritorio) {
			mapa.put(executivoTerritorio.getTpExecutivo(), executivoTerritorio);
		}
		return mapa;
	}
	
	private List<TerritorioGridDTO> convertToGridDTOList(List<Territorio> territorioList) {
		List<TerritorioGridDTO> gridDtoList = new ArrayList<TerritorioGridDTO>();
		for (Territorio territorio : territorioList) {
			gridDtoList.add(convertToGridDto(territorio));
		}
		return gridDtoList;
	}

	private TerritorioGridDTO convertToGridDto(Territorio territorio) {
		TerritorioGridDTO gridDto = new TerritorioGridDTO();
		gridDto.setId(territorio.getIdTerritorio());
		gridDto.setDsRegional(territorio.getRegional() != null ? territorio.getRegional().getDsRegional() : null);
		
		if (territorio.getFilial() != null) {
			gridDto.setSgFilial(territorio.getFilial().getSgFilial());
			gridDto.setNmFilial(territorio.getFilial().getPessoa().getNmFantasia());
		} else {
			// Para não gerar problema no "inlinePropertyFilter"
			gridDto.setSgFilial("");
			gridDto.setNmFilial("");
		}
		gridDto.setNmTerritorio(territorio.getNmTerritorio());
		return gridDto;
	}

	private TerritorioDTO convertToDto(Territorio territorio) {
		TerritorioDTO dto = new TerritorioDTO();
		dto.setId(territorio.getIdTerritorio());

		if (territorio.getRegional() != null) {
			dto.setRegional(new RegionalSuggestDTO(
				territorio.getRegional().getIdRegional(), 
				territorio.getRegional().getSgRegional(), 
				territorio.getRegional().getDsRegional()));
		}

		if (territorio.getFilial() != null) {
			Filial filial = filialService.findByIdJoinPessoa(territorio.getFilial().getIdFilial());
			if (filial != null) {
				dto.setFilial(new FilialSuggestDTO(
					filial.getIdFilial(), 
					filial.getPessoa().getNmFantasia(), 
					filial.getSgFilial()));
			}
		}

		dto.setNmTerritorio(territorio.getNmTerritorio());
		return dto;
	}

	public TerritorioService getTerritorioService() {
		return territorioService;
	}

	public void setTerritorioService(TerritorioService territorioService) {
		this.territorioService = territorioService;
	}

	public ExecutivoTerritorioService getExecutivoTerritorioService() {
		return executivoTerritorioService;
	}

	public void setExecutivoTerritorioService(
			ExecutivoTerritorioService executivoTerritorioService) {
		this.executivoTerritorioService = executivoTerritorioService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}

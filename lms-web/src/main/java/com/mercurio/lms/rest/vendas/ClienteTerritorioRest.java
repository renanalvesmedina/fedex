package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.joda.time.YearMonthDay;
import org.joda.time.format.ISODateTimeFormat;

import br.com.tntbrasil.integracao.domains.calendariotnt.CalendarioTntDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.RegionalFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.municipios.dto.RegionalChosenDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteTerritorioDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteTerritorioFilterDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteTerritorioGridDTO;
import com.mercurio.lms.rest.vendas.dto.TerritorioSuggestDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ClienteTerritorio;
import com.mercurio.lms.vendas.model.ComissaoConquista;
import com.mercurio.lms.vendas.model.Territorio;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ClienteTerritorioService;
import com.mercurio.lms.vendas.model.service.ComissaoConquistaService;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;
import com.mercurio.lms.vendas.model.service.TerritorioService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

@Path("/vendas/clienteTerritorio")
public class ClienteTerritorioRest extends BaseCrudRest<ClienteTerritorioDTO, ClienteTerritorioGridDTO, ClienteTerritorioFilterDTO> {

	private static final String EXCEPTION_STORE_COMISSAO_CONQUISTA_INICIO_VIGENCIA = "LMS-01317";
	private static final String EXCEPTION_STORE_COMISSAO_CONQUISTA_FIM_VIGENCIA = "LMS-01318";
	private static final String EXCEPTION_REGISTRO_NAO_ENCONTRADO = "LMS-00053";
	private static final String EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO = "LMS-01323"; 

	@InjectInJersey
	private ClienteTerritorioService clienteTerritorioService;

	@InjectInJersey
	private TerritorioService territorioService;

	@InjectInJersey
	private ClienteService clienteService;

	@InjectInJersey
	private ExecutivoTerritorioService executivoTerritorioService;

	@InjectInJersey
	private WorkflowPendenciaService workflowPendenciaService;

	@InjectInJersey
	private RegionalFilialService regionalFilialService;

	@InjectInJersey
	private RegionalService regionalService;

	@InjectInJersey
	private ComissaoConquistaService comissaoConquistaService;

	@InjectInJersey
	private EdwService edwService;

	private List<ClienteTerritorioGridDTO> convertToGridDTOList(List<ClienteTerritorio> clienteTerritorioList) {
		List<ClienteTerritorioGridDTO> gridDtoList = new ArrayList<ClienteTerritorioGridDTO>();
		for (ClienteTerritorio clienteTerritorio : clienteTerritorioList) {
			gridDtoList.add(convertToGridDTO(clienteTerritorio));
		}
		return gridDtoList;
	}

	private ClienteTerritorioGridDTO convertToGridDTO(ClienteTerritorio clienteTerritorio) {
		ClienteTerritorioGridDTO gridDto = new ClienteTerritorioGridDTO();
		gridDto.setId(clienteTerritorio.getIdClienteTerritorio());
		gridDto.setNmPessoa(clienteTerritorio.getCliente() != null ? clienteTerritorio.getCliente().getPessoa().getNmPessoa() : null);
		gridDto.setNmTerritorio(clienteTerritorio.getTerritorio() != null ? clienteTerritorio.getTerritorio().getNmTerritorio() : null);
		gridDto.setTpModal(clienteTerritorio.getTpModal() != null ? clienteTerritorio.getTpModal().getDescriptionAsString() : null);
		gridDto.setDtInicio(clienteTerritorio.getDtInicio());
		gridDto.setDtFim(clienteTerritorio.getDtFim());
		gridDto.setCnpj(FormatUtils.formatIdentificacao(clienteTerritorio.getCliente().getPessoa()));
		gridDto.setTpCliente(clienteTerritorio.getCliente().getTpCliente().getDescriptionAsString());
		gridDto.setTpSituacao(clienteTerritorio.getCliente().getTpSituacao().getDescriptionAsString());
		gridDto.setTpSituacaoAprovacao(clienteTerritorio.getTpSituacaoAprovacao() == null ? "-" : clienteTerritorio.getTpSituacaoAprovacao()
				.getDescriptionAsString());
		gridDto.setSgFilialResponsavel(clienteTerritorio.getCliente().getFilialByIdFilialAtendeComercial() == null ? null : clienteTerritorio.getCliente()
				.getFilialByIdFilialAtendeComercial().getSgFilial());
		gridDto.setSgFilialNegociacao(clienteTerritorio.getTerritorio().getFilial() == null ? null : clienteTerritorio.getTerritorio().getFilial()
				.getSgFilial());

		return gridDto;
	}

	private ClienteTerritorioDTO convertToDTO(ClienteTerritorio clienteTerritorio, ComissaoConquista comissaoConquista) {
		ClienteTerritorioDTO dto = new ClienteTerritorioDTO();
		dto.setId(clienteTerritorio.getIdClienteTerritorio());

		if (clienteTerritorio.getCliente() != null) {
			dto.setCliente(new ClienteSuggestDTO(clienteTerritorio.getCliente().getIdCliente(), clienteTerritorio.getCliente().getPessoa().getNmPessoa(),
					clienteTerritorio.getCliente().getPessoa().getNrIdentificacao()));
		}

		if (clienteTerritorio.getTerritorio() != null) {
			dto.setTerritorio(new TerritorioSuggestDTO(clienteTerritorio.getTerritorio().getIdTerritorio(), clienteTerritorio.getTerritorio().getNmTerritorio()));
		}

		dto.setTpModal(clienteTerritorio.getTpModal());
		dto.setDtInicio(clienteTerritorio.getDtInicio());
		dto.setDtFim(clienteTerritorio.getDtFim());
		dto.setIdPendeciaAprovacao(clienteTerritorio.getPendenciaAprovacao() == null ? null : clienteTerritorio.getPendenciaAprovacao().getIdPendencia());

		if (comissaoConquista != null) {
			dto.setBlComissaoConquista(true);
			dto.setIdComissaoConquista(comissaoConquista.getIdComissaoConquista());
			dto.setDtComissaoConquistaInicio(comissaoConquista.getDtInicio());
			dto.setDtComissaoConquistaFim(comissaoConquista.getDtFim());
		} else {
			dto.setBlComissaoConquista(false);
		}

		return dto;
	}

	@GET
	@Path("/findDtInicioMesTNT")
	public Response findDtInicioMesTNT() {
		Map<String, Object> map = new HashMap<String, Object>();
		CalendarioTntDMN calendarioTntDMN = edwService.findCalendarioTNTByData(JTDateTimeUtils.getDataAtual());
		map.put("dtInicio", new YearMonthDay(ISODateTimeFormat.dateTimeParser().parseDateTime(calendarioTntDMN.getDataInicioMesProducao())));
		return Response.ok(map).build();
	}

	@GET
	@Path("/findDadosCliente")
	public Response findDadosCliente(@QueryParam("id") Long id) {
		Cliente cliente = clienteService.findByIdInitLazyProperties(id, true);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sgFilial", cliente.getFilialByIdFilialAtendeComercial().getSgFilial());
		map.put("nmFilial", cliente.getFilialByIdFilialAtendeComercial().getPessoa().getNmFantasia());
		map.put("idFilial", cliente.getFilialByIdFilialAtendeComercial().getIdFilial());
		map.put("tpCliente", cliente.getTpCliente());
		map.put("tpSituacao", cliente.getTpSituacao());

		return Response.ok(map).build();
	}

	@GET
	@Path("/findDadosTerritorio")
	public Response findDadosTerritorio(@QueryParam("id") Long id) {
		Territorio territorio = territorioService.findByIdInitLazyProperties(id);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("sgFilial", territorio.getFilial() == null ? "   " : territorio.getFilial().getSgFilial());
		map.put("nmFilial", territorio.getFilial() == null ? "" : territorio.getFilial().getPessoa().getNmFantasia());
		map.put("idFilial", territorio.getFilial() == null ? null : territorio.getFilial().getIdFilial());

		return Response.ok(map).build();
	}

	@Override
	protected Long store(ClienteTerritorioDTO dto) {

		validaDataComissaoConquista(dto);
		
		if (clienteTerritorioService.findRegistrosEmAprovacaoDoCliente(dto.getCliente().getId()).size() > 0) {
			throw new BusinessException(EXCEPTION_NAO_PODE_SER_ALTERADO_EM_APROVACAO);
		}

		ClienteTerritorio clienteTerritorio = buildClienteTerritorio(dto);
		Long idClienteTerritorio = (Long) clienteTerritorioService.saveWorkflow(clienteTerritorio);

		if (dto.getBlComissaoConquista()) {
			comissaoConquistaService.storeComissaoConquista(buildUsuarioLMS(), idClienteTerritorio, dto.getDtComissaoConquistaInicio(), dto.getDtComissaoConquistaFim());
		} else {
			comissaoConquistaService.updateStatusInativoByIdClienteTerritorio(idClienteTerritorio);
		}
		return idClienteTerritorio;
	}
	
	private ClienteTerritorio buildClienteTerritorio(ClienteTerritorioDTO dto) {
		ClienteTerritorio clienteTerritorio = new ClienteTerritorio();

		clienteTerritorio.setIdClienteTerritorio(dto.getId());
		clienteTerritorio.setCliente(buildCliente(dto));
		clienteTerritorio.setTerritorio(buildTerritorio(dto));
		clienteTerritorio.setTpModal(dto.getTpModal());
		clienteTerritorio.setDtInicio(dto.getDtInicio());
		clienteTerritorio.setDtFim(dto.getDtFim());
		clienteTerritorio.setUsuarioAlteracao(buildUsuarioLMS());
		
		return clienteTerritorio;
	}

	private UsuarioLMS buildUsuarioLMS() {
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		return usuario;
	}

	private Cliente buildCliente(ClienteTerritorioDTO dto) {
		Cliente cliente = null;
		if (dto.getCliente() != null) {
			cliente = new Cliente();
			cliente.setIdCliente(dto.getCliente().getId());
		}
		return cliente;
	}

	private Territorio buildTerritorio(ClienteTerritorioDTO dto) {
		Territorio territorio = null;
		if (dto.getTerritorio() != null) {
			territorio = new Territorio();
			territorio.setIdTerritorio(dto.getTerritorio().getId());
		}
		return territorio;
	}

	private void validaDataComissaoConquista(ClienteTerritorioDTO dto) {
		if (dto.getBlComissaoConquista()) {
			if (dto.getDtInicio().isAfter(dto.getDtComissaoConquistaInicio())) {
				throw new BusinessException(EXCEPTION_STORE_COMISSAO_CONQUISTA_INICIO_VIGENCIA);
			}
			if ((dto.getDtFim() != null) && (dto.getDtFim().isBefore(dto.getDtComissaoConquistaFim()))) {
				throw new BusinessException(EXCEPTION_STORE_COMISSAO_CONQUISTA_FIM_VIGENCIA);
			}
		}
	}

	@Override
	protected void removeById(Long id) {
		clienteTerritorioService.updateStatusInativo(id);
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}

	@Override
	protected ClienteTerritorioDTO findById(Long id) {
		ClienteTerritorio clienteTerritorio = clienteTerritorioService.findById(id);
		if (clienteTerritorio == null) {
			throw new BusinessException(EXCEPTION_REGISTRO_NAO_ENCONTRADO);
		}

		return convertToDTO(clienteTerritorio, comissaoConquistaService.findByIdClienteTerritorio(clienteTerritorio.getIdClienteTerritorio()));
	}

	@Override
	protected List<ClienteTerritorioGridDTO> find(ClienteTerritorioFilterDTO filter) {
		Long clienteId = filter.getCliente() != null ? filter.getCliente().getId() : null;
		Long territorioId = filter.getTerritorio() != null ? filter.getTerritorio().getId() : null;
		Long idFilial = filter.getFilial() != null ? filter.getFilial().getIdFilial() : null;
		Long idRegional = filter.getRegional() != null ? filter.getRegional().getIdRegional() : null;

		List<ClienteTerritorio> clienteTerritorioList = clienteTerritorioService.find(clienteId, territorioId, filter.getTpModal(), filter.getDtInicio(),
				filter.getDtFim(), null, idFilial, idRegional);
		return convertToGridDTOList(clienteTerritorioList);
	}

	@Override
	protected Integer count(ClienteTerritorioFilterDTO filter) {
		Long clienteId = filter.getCliente() != null ? filter.getCliente().getId() : null;
		Long territorioId = filter.getTerritorio() != null ? filter.getTerritorio().getId() : null;
		return clienteTerritorioService.findCount(clienteId, territorioId, filter.getTpModal(), filter.getDtInicio(), filter.getDtFim(), null);
	}

	public ClienteTerritorioService getClienteTerritorioService() {
		return clienteTerritorioService;
	}

	public void setClienteTerritorioService(ClienteTerritorioService clienteTerritorioService) {
		this.clienteTerritorioService = clienteTerritorioService;
	}

	public TerritorioService getTerritorioService() {
		return territorioService;
	}

	public void setTerritorioService(TerritorioService territorioService) {
		this.territorioService = territorioService;
	}

	@POST
	@Path("transferirCarteira")
	public Response transferirCarteira(ClienteTerritorioDTO dto) {
		Long territorioDeId = dto.getClienteTerritorioTransferenciaCarteiraDTO().getTerritorioDe().getId();
		Long territorioParaId = dto.getClienteTerritorioTransferenciaCarteiraDTO().getTerritorioPara().getId();
		YearMonthDay dtInicio = dto.getClienteTerritorioTransferenciaCarteiraDTO().getDtInicio();
		YearMonthDay dtFechamento = dto.getClienteTerritorioTransferenciaCarteiraDTO().getDtFechamento();

		clienteTerritorioService.executeTransferirCarteira(territorioDeId, territorioParaId, dtFechamento, dtInicio);

		return Response.ok().build();
	}

	@POST
	@Path("carregarValoresPadrao")
	public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();

		Filial filialLogada = SessionUtils.getFilialSessao();
		retorno.put("idFilial", filialLogada.getIdFilial());
		retorno.put("sgFilial", filialLogada.getSgFilial());
		retorno.put("nmFilial", filialLogada.getPessoa().getNmFantasia());

		return Response.ok(retorno).build();
	}

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

		return new RegionalChosenDTO(regional.getIdRegional(), regional.getDsRegional());
	}

	public void setEdwService(EdwService edwService) {
		this.edwService = edwService;
	}

}

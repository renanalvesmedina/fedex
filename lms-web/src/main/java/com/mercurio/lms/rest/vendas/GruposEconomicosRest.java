package com.mercurio.lms.rest.vendas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.GrupoEconomicoClienteDTO;
import com.mercurio.lms.rest.vendas.dto.GruposEconomicosDTO;
import com.mercurio.lms.rest.vendas.dto.GruposEconomicosFilterDTO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.GrupoEconomico;
import com.mercurio.lms.vendas.model.GrupoEconomicoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.GrupoEconomicoClienteService;
import com.mercurio.lms.vendas.model.service.GrupoEconomicoService;
 
@Path("/vendas/gruposEconomicos") 
public class GruposEconomicosRest extends BaseCrudRest<GruposEconomicosDTO, GruposEconomicosDTO, GruposEconomicosFilterDTO> { 
	
	private static final String LBL_CLIENTE_PRINCIPAL = " (Cliente Principal)";
 
	@InjectInJersey GrupoEconomicoService grupoEconomicoService;
	@InjectInJersey GrupoEconomicoClienteService grupoEconomicoClienteService;
	@InjectInJersey ClienteService clienteService;
	
	@Override 
	protected List<GruposEconomicosDTO> find(GruposEconomicosFilterDTO filter) { 
		List<GrupoEconomico> grupoEconomicoBD = grupoEconomicoService.findGruposEconomicos(convertFilterToTypedFlatMap(filter));
		return convertToGruposEconomicosDto(grupoEconomicoBD);
	} 
	
	private List<GruposEconomicosDTO> convertToGruposEconomicosDto(List<GrupoEconomico> listGrupoEconomico) {
		List<GruposEconomicosDTO> listGruposEconomicosDto = new ArrayList<GruposEconomicosDTO>();
		for (GrupoEconomico entidade : listGrupoEconomico) {
			GruposEconomicosDTO gruposEconomicosDto = new GruposEconomicosDTO();
			gruposEconomicosDto.setId(entidade.getIdGrupoEconomico());
			gruposEconomicosDto.setCodigo(entidade.getDsCodigo().toString());
			gruposEconomicosDto.setDescricao(entidade.getDsGrupoEconomico().toString());
			gruposEconomicosDto.setTpSituacao(entidade.getTpSituacao());
			Cliente cliente = clienteService.findById(entidade.getClientePrincipal().getIdCliente());
			gruposEconomicosDto.setCliente(new ClienteSuggestDTO(
					cliente.getIdCliente(), 
					cliente.getPessoa().getNmPessoa(), 
					cliente.getPessoa().getNrIdentificacao()));
			listGruposEconomicosDto.add(gruposEconomicosDto);
		}
		return listGruposEconomicosDto;
	}
	
	private TypedFlatMap convertFilterToTypedFlatMap(GruposEconomicosFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("idGrupoEconomico", filter.getId());
		if (filter.getCliente() != null) tfm.put("clientePrincipal.idCliente", filter.getCliente().getIdCliente());
		if (filter.getTpSituacao() != null) tfm.put("tpSituacao", filter.getTpSituacao().getValue());
		if (StringUtils.isNotBlank(filter.getDescricao())) tfm.put("dsGrupoEconomico", filter.getDescricao());
		if (StringUtils.isNotBlank(filter.getCodigo())) tfm.put("dsCodigo", filter.getCodigo());
		
		return tfm;
	}
	
	@Override 
	protected Integer count(GruposEconomicosFilterDTO filter) { 
		Map filterMap = this.convertFilterToTypedFlatMap(filter);
		return this.grupoEconomicoService.getRowCount(filterMap);
	} 
	
	@Override 
	protected GruposEconomicosDTO findById(Long id) { 
		GrupoEconomico entity = grupoEconomicoService.findById(id);
		GruposEconomicosDTO gruposEconomicosDTO = new GruposEconomicosDTO();
		gruposEconomicosDTO.setId(entity.getIdGrupoEconomico());
		gruposEconomicosDTO.setCodigo(entity.getDsCodigo());
		gruposEconomicosDTO.setDescricao(entity.getDsGrupoEconomico());
		gruposEconomicosDTO.setTpSituacao(entity.getTpSituacao());
		ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO();
		clienteDTO.setIdCliente(entity.getClientePrincipal().getIdCliente());
		clienteDTO.setNrIdentificacao(entity.getClientePrincipal().getPessoa().getNrIdentificacao());
		clienteDTO.setNmPessoa(entity.getClientePrincipal().getPessoa().getNmPessoa());
		gruposEconomicosDTO.setCliente(clienteDTO);
		
		return gruposEconomicosDTO; 
	} 
 
	@Override 
	protected Long store(GruposEconomicosDTO dto) { 
		
		this.validaCampos(dto);
		
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		GrupoEconomico grupoEconomivo = null;
		
		if(dto.getId() != null){
			grupoEconomivo = grupoEconomicoService.findById(dto.getId());
		}else{
			grupoEconomivo = new GrupoEconomico();
			grupoEconomivo.setUsuarioInclusao(usuarioLMS);
			grupoEconomivo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
		grupoEconomivo.setDsCodigo(dto.getCodigo());
		grupoEconomivo.setDsGrupoEconomico(dto.getDescricao());
		grupoEconomivo.setTpSituacao(dto.getTpSituacao());
		grupoEconomivo.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		grupoEconomivo.setUsuarioAlteracao(usuarioLMS);
		
		if (dto.getCliente() != null) {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(dto.getCliente().getId());
			grupoEconomivo.setClientePrincipal(cliente);
		}
		
		grupoEconomicoService.store(grupoEconomivo);
		
		return grupoEconomivo.getIdGrupoEconomico();
	} 
 
	private void validaCampos(GruposEconomicosDTO dto) {
		validaCodigo(dto);
		validaDescricao(dto);
		validaCliente(dto.getId(), dto.getCliente().getIdCliente());
		validaClientePrincipalOutroGrupo(dto);
	}
	
	private void validaCodigo(GruposEconomicosDTO dto) {
		List<GrupoEconomico> ListGrupoEconomico = grupoEconomicoService.findByCodigoDescricaoAtivoDiferente(dto.getCodigo(), null, dto.getId());
		if(CollectionUtils.isNotEmpty(ListGrupoEconomico)) {					
			throw new BusinessException("LMS-01284");
		}
	}
	
	private void validaDescricao(GruposEconomicosDTO dto) {
		List<GrupoEconomico> ListGrupoEconomico = grupoEconomicoService.findByCodigoDescricaoAtivoDiferente(null, dto.getDescricao(), dto.getId());
		if(CollectionUtils.isNotEmpty(ListGrupoEconomico)) {					
			throw new BusinessException("LMS-01301");
		}
	}
	
	private void validaCliente(Long idGrupoEconomico, Long idCliente){
		if(idGrupoEconomico != null){
			List<GrupoEconomicoCliente> listGrupoEconomicoCliente = grupoEconomicoClienteService.findByIdGrupoIdCliente(idGrupoEconomico, idCliente);
			if(CollectionUtils.isNotEmpty(listGrupoEconomicoCliente)) {					
				throw new BusinessException("LMS-01302");
			}
		}
	}
	
	private void validaClientePrincipalOutroGrupo(GruposEconomicosDTO dto){
		GrupoEconomico grupoEconomico = grupoEconomicoService.findGrupoEconomicoByIdGrupoIdCliente(dto.getId(), dto.getCliente().getIdCliente());
		if(grupoEconomico != null && !grupoEconomico.getIdGrupoEconomico().equals(dto.getId())) {				
			throw new BusinessException("LMS-01312");
		}
	}
	
	@Override 
	protected void removeById(Long id) { 
		grupoEconomicoService.removeById(id);
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
		grupoEconomicoService.removeByIds(ids);
	} 
	
	private TypedFlatMap getFiltrosBasicosPaginacaoAbas(BaseFilterDTO filter) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("_currentPage", filter.getPagina() == null ? "1" : String.valueOf(filter.getPagina()));
		toReturn.put("_pageSize", filter.getQtRegistrosPagina() == null ? String.valueOf(ROW_LIMIT) : String.valueOf(filter.getQtRegistrosPagina()));
		toReturn.put("grupoEconomico.idGrupoEconomico", filter.getId());
		return toReturn;
	}
	
	@POST
	@Path("findGrupoEconomicoClientes")
	public Response findGrupoEconomicoClientes(BaseFilterDTO filter){
		TypedFlatMap criteria = getFiltrosBasicosPaginacaoAbas(filter);
		ResultSetPage<GrupoEconomicoCliente> rsp = grupoEconomicoClienteService.findGrupoEconomicoClienteList(criteria);
		Integer qtRegistros = grupoEconomicoClienteService.findSimulacaoAnexoRowCount(criteria);
		qtRegistros++;//adicionado mais 1, pois sera o cliente principal fake criado abaixo
		return getReturnFind(convertToGrupoEconomicoClienteDTO(rsp.getList(), filter.getId()), qtRegistros);
	}
	
	private List<GrupoEconomicoClienteDTO> convertToGrupoEconomicoClienteDTO(List list, Long idGrupoEconomico) {
		List<GrupoEconomicoClienteDTO> grupoEClientesDTO = new ArrayList<GrupoEconomicoClienteDTO>();
		GrupoEconomicoClienteDTO grupoEconomicoClienteFake = createGrupoEconomicoClienteFake(idGrupoEconomico);
		grupoEClientesDTO.add(grupoEconomicoClienteFake);
		
		for (Object obj : list) {
			GrupoEconomicoCliente grupoECliente = (GrupoEconomicoCliente) obj;
			Cliente cliente = clienteService.findById(grupoECliente.getCliente().getIdCliente());
			ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO(cliente.getIdCliente(), cliente.getPessoa().getNmPessoa(), cliente.getPessoa().getNrIdentificacao());
			clienteDTO.setTpCliente(cliente.getTpCliente().getDescriptionAsString());
			
			GrupoEconomicoClienteDTO gecDTO = new GrupoEconomicoClienteDTO();
			gecDTO.setId(grupoECliente.getIdGrupoEconomicoCliente());
			gecDTO.setCliente(clienteDTO);
			grupoEClientesDTO.add(gecDTO);
		}
		
		return grupoEClientesDTO;
	}
	
	private GrupoEconomicoClienteDTO createGrupoEconomicoClienteFake(Long idGrupoEconomico) {
		GrupoEconomico grupoEconomico = grupoEconomicoService.findById(idGrupoEconomico);
		Cliente cliente = grupoEconomico.getClientePrincipal();
		ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO(cliente.getIdCliente(), cliente.getPessoa().getNmPessoa()+LBL_CLIENTE_PRINCIPAL, cliente.getPessoa().getNrIdentificacao());
		clienteDTO.setTpCliente(cliente.getTpCliente().getDescriptionAsString());
		
		GrupoEconomicoClienteDTO gecDTO = new GrupoEconomicoClienteDTO();
		gecDTO.setCliente(clienteDTO);
		return gecDTO;
	}

	@POST
	@Path("/storeGrupoEconomicoCliente")
	public Response storeGrupoEconomicoCliente(TypedFlatMap dados) {
		
		Long idGrupoEconomico = Long.valueOf((Integer)dados.get("idGrupoEconomico"));
		Long idCliente = Long.valueOf((Integer)dados.get("idCliente"));
		
		validaCliente(idGrupoEconomico, idCliente);
		validaClientePrincipal(idGrupoEconomico, idCliente);
		
		GrupoEconomico grupoEconomico = new GrupoEconomico(idGrupoEconomico);
		Cliente cliente = new Cliente(idCliente);
		GrupoEconomicoCliente bean = new GrupoEconomicoCliente(grupoEconomico, cliente);
		grupoEconomicoClienteService.store(bean);
		return Response.ok().build();
	}
	
	private void validaClientePrincipal(Long idGrupoEconomico, Long idCliente){
		GrupoEconomico grupoEconomico = grupoEconomicoService.findGrupoEconomicoByIdGrupoIdCliente(idGrupoEconomico, idCliente);
		if(grupoEconomico != null) {					
			throw new BusinessException("LMS-01303");
		}
	}
	
	@POST
	@Path("removeGrupoEconomicoClientesByIdsClientes")
	public Response removeGrupoEconomicoClientesByIdsClientes(List<Long> ids) {
		grupoEconomicoClienteService.removeByIds(ids);
		return Response.ok().build();
	}
 
} 

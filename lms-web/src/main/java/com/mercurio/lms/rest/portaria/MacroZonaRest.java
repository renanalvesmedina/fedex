package com.mercurio.lms.rest.portaria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.service.MacroZonaService;
import com.mercurio.lms.portaria.model.service.TerminalService;
import com.mercurio.lms.rest.carregamento.DispositivoUnitizacaoDTO;
import com.mercurio.lms.rest.carregamento.DispositivoUnitizacaoFiltroDTO;
import com.mercurio.lms.rest.carregamento.TipoDispositivoUnitizacaoDTO;
import com.mercurio.lms.rest.expedicao.ConhecimentoDTO;
import com.mercurio.lms.rest.expedicao.VolumeNotaFiscalDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.sim.LocalizacaoMercadoriaDTO;

@Path("/portaria/macroZona")
public class MacroZonaRest extends BaseRest {

	@InjectInJersey 
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	
	@InjectInJersey 
	private MacroZonaService macroZonaService;
	
	@InjectInJersey 
	private TerminalService terminalService;
	
	@InjectInJersey 
	private FilialService filialService;
	
	@InjectInJersey 
	private PessoaService pessoaService;
	
	@InjectInJersey
	private VolumeNotaFiscalService volumeNotaFiscalService;
	
	@GET
	@Path("findById")
	public Response findById(@QueryParam("id") Long id) {
    	
		MacroZona macroZona = macroZonaService.findById(id);
    	
		Terminal terminal = terminalService.findById(macroZona.getTerminal().getIdTerminal());
    	Filial filial = filialService.findById(terminal.getFilial().getIdFilial());
    	Pessoa pessoaTerminal = pessoaService.findById(terminal.getPessoa().getIdPessoa());
    	
    	Map<String, Object> toReturn = new HashMap<String, Object>();
    	toReturn.put("idMacroZona", macroZona.getIdMacroZona());
    	toReturn.put("idFilial", filial.getIdFilial());
    	toReturn.put("sgFilial", filial.getSgFilial());
    	toReturn.put("nmFantasia",filial.getPessoa().getNmFantasia());
    	toReturn.put("idTerminal",terminal.getIdTerminal());
    	toReturn.put("nmPessoaTerminal", pessoaTerminal.getNmPessoa());
    	toReturn.put("dsMacroZona", macroZona.getDsMacroZona());
    	toReturn.put("nrCodigoBarras", macroZona.getNrCodigoBarras());
    	toReturn.put("tpSituacao", macroZona.getTpSituacao().getDescriptionAsString());
		
    	return Response.ok(toReturn).build();
    	
	}
	
	@POST
	@Path("findVolumes")
    public Response findVolumes(DispositivoUnitizacaoFiltroDTO filtro) {

		TypedFlatMap tfm = this.getTypedFlatMapWithPaginationInfo(filtro);

		if (filtro.getMacroZona() != null) {
			tfm.put("idMacroZona", filtro.getMacroZona().getId());
		}
		
		List<VolumeNotaFiscal> list = volumeNotaFiscalService.findPaginated(new PaginatedQuery(tfm)).getList();
		
		Integer qtRegistros = volumeNotaFiscalService.getRowCount(tfm);
		
		return getReturnFind(convertToVolumeDTO(list), qtRegistros);
    }
	
	@POST
	@Path("findDispositivos")
	public Response findDispositivos(DispositivoUnitizacaoFiltroDTO filtro) {
		TypedFlatMap toReturn = this.getTypedFlatMapWithPaginationInfo(filtro);
		
		if (filtro.getMacroZona() != null) {
			toReturn.put("idMacroZona", filtro.getMacroZona().getId());
		}
		
		List<Map<String, Object>> list = dispositivoUnitizacaoService.findDispositivosUnitizacao(toReturn);
		return getReturnFind(convertToDispositivoUnitizacaoDTO(list), list.size());
	}
	
	private List<DispositivoUnitizacaoDTO> convertToDispositivoUnitizacaoDTO (List<Map<String, Object>> list) {
		List<DispositivoUnitizacaoDTO> dispositivos = new ArrayList<DispositivoUnitizacaoDTO>();
		for (Map<String, Object> item : list) {
			DispositivoUnitizacaoDTO dispositivoUnitizacaoDTO = new DispositivoUnitizacaoDTO();
			
			dispositivoUnitizacaoDTO.setId((Long) item.get("id"));
			dispositivoUnitizacaoDTO.setNrIdentificacao((String)item.get("nrIdentificacao"));
			EmpresaDTO empresaDTO = new EmpresaDTO();
			empresaDTO.setIdEmpresa((Long) item.get("idEmpresa"));
			empresaDTO.setNmPessoa((String)item.get("nmPessoa"));
			dispositivoUnitizacaoDTO.setEmpresa(empresaDTO);
			
			TipoDispositivoUnitizacaoDTO tipoDispositivoUnitizacaoDTO = new TipoDispositivoUnitizacaoDTO();
			tipoDispositivoUnitizacaoDTO.setIdTipoDispositivoUnitizacao((Long) item.get("idTipoDispositivoUnitizacao"));
			tipoDispositivoUnitizacaoDTO.setDsTipoDispositivoUnitizacao((String) item.get("dsTipoDispositivoUnitizacao"));
			dispositivoUnitizacaoDTO.setTipoDispositivoUnitizacao(tipoDispositivoUnitizacaoDTO);
			
			dispositivoUnitizacaoDTO.setTpSituacao((DomainValue)item.get("tpSituacao"));
			dispositivoUnitizacaoDTO.setQtVolumes((Integer)item.get("volumes"));
			dispositivoUnitizacaoDTO.setQtDispositivos((Integer)item.get("dispositivos"));
			dispositivos.add(dispositivoUnitizacaoDTO);
		}
		return dispositivos;
	}

	private List<VolumeNotaFiscalDTO> convertToVolumeDTO (List<VolumeNotaFiscal> list) {
		List<VolumeNotaFiscalDTO> dispositivos = new ArrayList<VolumeNotaFiscalDTO>();
		for (VolumeNotaFiscal item : list) {
			VolumeNotaFiscalDTO volumeDTO = new VolumeNotaFiscalDTO();
			
			volumeDTO.setNrVolumeEmbarque(item.getNrVolumeEmbarque());
			
			ConhecimentoDTO conhecimentoDTO = new ConhecimentoDTO();
			Conhecimento conhecimento = item.getNotaFiscalConhecimento().getConhecimento();
			conhecimentoDTO.setNrConhecimento(conhecimento.getNrConhecimento());
			conhecimentoDTO.setDvConhecimento(conhecimento.getDvConhecimento());
			
			if(item.getTpVolume().equals(ConstantesExpedicao.TP_VOLUME_DETALHE)) {
				volumeDTO.setNrSequencia(item.getNrSequenciaPalete());
			} else {
				volumeDTO.setNrSequencia(item.getNrSequencia());
			}
			conhecimentoDTO.setQtVolumes(conhecimento.getQtVolumes());
			FilialSuggestDTO filialOrigem = new FilialSuggestDTO();
			filialOrigem.setSgFilial(conhecimento.getFilialByIdFilialOrigem().getSgFilial());
			conhecimentoDTO.setFilialOrigem(filialOrigem);
			volumeDTO.setConhecimento(conhecimentoDTO);
			
			if(item.getLocalizacaoFilial() != null) {
				FilialSuggestDTO filialLocalizacao = new FilialSuggestDTO();
				filialLocalizacao.setSgFilial(item.getLocalizacaoFilial().getSgFilial());
				volumeDTO.setFilialLocalizacao(filialLocalizacao);
			}
			if(item.getLocalizacaoMercadoria() != null) {
				LocalizacaoMercadoriaDTO localizacaoMercadoria = new LocalizacaoMercadoriaDTO();
				localizacaoMercadoria.setDsLocalizacaoMercadoria(item.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
				volumeDTO.setLocalizacaoMercadoria(localizacaoMercadoria);
			}

			dispositivos.add(volumeDTO);
		}
		return dispositivos;
	}
	
	private TypedFlatMap getTypedFlatMapWithPaginationInfo(DispositivoUnitizacaoFiltroDTO filter) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("_currentPage", filter.getPagina() == null ? "1" : String.valueOf(filter.getPagina()));
		toReturn.put("_pageSize", filter.getQtRegistrosPagina() == null ? String.valueOf(ROW_LIMIT) : String.valueOf(filter.getQtRegistrosPagina()));
		return toReturn;
	}

}
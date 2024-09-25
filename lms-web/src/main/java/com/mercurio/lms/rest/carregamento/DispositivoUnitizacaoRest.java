package com.mercurio.lms.rest.carregamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.service.MonitorarMensagemComunicacaoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.expedicao.ConhecimentoDTO;
import com.mercurio.lms.rest.expedicao.VolumeNotaFiscalDTO;
import com.mercurio.lms.rest.municipios.EmpresaDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.portaria.dto.MacroZonaDTO;
import com.mercurio.lms.rest.sim.EventoDTO;
import com.mercurio.lms.rest.sim.EventoDispositivoUnitizacaoDTO;
import com.mercurio.lms.rest.sim.LocalizacaoMercadoriaDTO;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;

import org.joda.time.DateTime;

@Path("/carregamento/dispositivoUnitizacao")
public class DispositivoUnitizacaoRest extends LmsBaseCrudReportRest<DispositivoUnitizacaoDTO, DispositivoUnitizacaoDTO, DispositivoUnitizacaoFiltroDTO> {

	@InjectInJersey 
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	
	@InjectInJersey
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	
	@InjectInJersey
	private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;

	@InjectInJersey
	private VolumeNotaFiscalService volumeNotaFiscalService;
	
	@InjectInJersey 
    private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	MonitorarMensagemComunicacaoService monitorarMensagemComunicacaoService;
	
	
	
	public void setMonitorarMensagemComunicacaoService(
			MonitorarMensagemComunicacaoService monitorarMensagemComunicacaoService) {
		this.monitorarMensagemComunicacaoService = monitorarMensagemComunicacaoService;
	}
	
	@POST
	@Path("findVolumes")
    public Response findVolumes(DispositivoUnitizacaoFiltroDTO filtro) {

		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filtro);
		if (filtro.getId() != null) {
			tfm.put("idDispositivoUnitizacao", filtro.getId());
		}
		
		List<VolumeNotaFiscal> list = volumeNotaFiscalService.findPaginated(new PaginatedQuery(tfm)).getList();
		
		Integer qtRegistros = volumeNotaFiscalService.getRowCount(tfm);
		
		return getReturnFind(convertToVolumeDTO(list), qtRegistros);
    }
    
	@POST
	@Path("findEventos")
	public Response findEventos(DispositivoUnitizacaoFiltroDTO filtro) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filtro);
		if (filtro.getId() != null) {
			tfm.put("idDispositivoUnitizacao", filtro.getId());
		}
		
		List<EventoDispositivoUnitizacao> list = eventoDispositivoUnitizacaoService.findPaginated(new PaginatedQuery(tfm)).getList();
		Integer qtRegistros = eventoDispositivoUnitizacaoService.getRowCount(tfm);
		
		return getReturnFind(convertToEventoDTO(list), qtRegistros);
	}
	
	@Override
	protected DispositivoUnitizacaoDTO findById(Long id) {
    	DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById((Long)id);
    	
    	DispositivoUnitizacaoDTO dispositivoDTO = new DispositivoUnitizacaoDTO();    	

    	if (dispositivo != null) {
    		dispositivoDTO.setId(dispositivo.getIdDispositivoUnitizacao());
    		dispositivoDTO.setTpNrIdentificacao(Long.valueOf(dispositivo.getNrIdentificacao().substring(0, 2)));
    		dispositivoDTO.setNrIdentificacao(dispositivo.getNrIdentificacao().substring(2));
    		dispositivoDTO.setTpSituacao(dispositivo.getTpSituacao());
    		dispositivoDTO.setTipoDispositivoUnitizacao(getTipoDispositivoUnitizacaoDTO(dispositivo));
    		dispositivoDTO.setEmpresa(getEmpresaDTO(dispositivo));
    		
    		if(dispositivo.getLocalizacaoMercadoria() != null) {
    			dispositivoDTO.setDsLocalizacaoMercadoria(dispositivo.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue());
    		}
    		if(dispositivo.getLocalizacaoFilial() != null) {
    			dispositivoDTO.setSgFilialLocalizacaoMercadoria(dispositivo.getLocalizacaoFilial().getSgFilial());    	
    		}
    		if(dispositivo.getMacroZona() != null) {
    			dispositivoDTO.setMacroZona(getMacroZonaDTO(dispositivo.getMacroZona()));
    		}
    		if(dispositivo.getDispositivoUnitizacaoPai() != null) {
    			dispositivoDTO.setDispositivoUnitizacaoPai(getDispositivoUnitizacaoDTO(dispositivo.getDispositivoUnitizacaoPai()));
    		}
    	}
	    	
    	return dispositivoDTO;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(DispositivoUnitizacaoFiltroDTO filtro) {
		TypedFlatMap map = getTypedFlatMap(filtro);
		map.remove("_currentPage");
		map.remove("_pageSize");
		return dispositivoUnitizacaoService.findDispositivosUnitizacao(map);
	}
	
	@Override
	protected List<DispositivoUnitizacaoDTO> find(DispositivoUnitizacaoFiltroDTO filtro) {
		List<Map<String, Object>> list = dispositivoUnitizacaoService.findDispositivosUnitizacao(getTypedFlatMap(filtro));
		return convertToDispositivoUnitizacaoDTO(list);
	}
	
	@Override
	protected Integer count(DispositivoUnitizacaoFiltroDTO filtro) {
		if ("C".equals(filtro.getAcao())) {
			return dispositivoUnitizacaoService.getRowCountDispositivosUnitizacao(getTypedFlatMap(filtro));
		} 
		return 0;
	}
	
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("tipoDispositivo", "dsTipoDispositivoUnitizacao"));
		list.add(getColumn("numeroIdentificacao", "nrIdentificacao"));
		list.add(getColumn("empresa", "nmPessoa"));
		list.add(getColumn("situacao", "tpSituacao"));
		list.add(getColumn("totalVolumes", "volumes"));
		list.add(getColumn("totalDispositivos", "dispositivos"));
		list.add(getColumn("localizacaoDispositivo", "sgFilialLocalizacao"));
		list.add(getColumn("dataUltimaMovimentacao", "dhUltimaMovimentacao"));
		return list;
	}
	
    @Override
    protected Long store(DispositivoUnitizacaoDTO bean) {    	    	    	
    	DispositivoUnitizacao dispositivo = new DispositivoUnitizacao();    	
    	
    	if(bean.getId() != null) {
    		dispositivo = dispositivoUnitizacaoService.findById(bean.getId());
    	}
    	
    	TipoDispositivoUnitizacaoDTO tipoDispositivoMap = bean.getTipoDispositivoUnitizacao();
    	dispositivo.setNrIdentificacao(String.valueOf(tipoDispositivoMap.getTpNrIdentificacao()).concat(bean.getNrIdentificacao()));
    	dispositivo.setTpSituacao(bean.getTpSituacao());
    	
    	/* Seta o id do tipo de dispositivo */
    	TipoDispositivoUnitizacao tipoDispositivo = new TipoDispositivoUnitizacao();
    	tipoDispositivo.setIdTipoDispositivoUnitizacao(tipoDispositivoMap.getIdTipoDispositivoUnitizacao());
    	dispositivo.setTipoDispositivoUnitizacao(tipoDispositivo);
    	
    	/* Seta o id da empresa */
    	Empresa empresa = new Empresa();
    	EmpresaDTO empresaDTO = bean.getEmpresa();
    	empresa.setIdEmpresa(empresaDTO.getIdEmpresa());
    	dispositivo.setEmpresa(empresa);
    	
		return (Long)dispositivoUnitizacaoService.store(dispositivo);
    }
    
	@Override
	protected void removeById(Long id) {
        dispositivoUnitizacaoService.removeById(id);
    }       

	@Override
	protected void removeByIds(List<Long> ids) {
		dispositivoUnitizacaoService.removeByIds(ids);
    }

	private MacroZonaDTO getMacroZonaDTO(MacroZona macroZona) {
		MacroZonaDTO macroZonaDTO = new MacroZonaDTO();
		macroZonaDTO.setId(macroZona.getIdMacroZona());
		macroZonaDTO.setDsMacroZona(macroZona.getDsMacroZona());
		macroZonaDTO.setNmPessoaTerminal(macroZona.getTerminal().getPessoa().getNmPessoa());
		macroZonaDTO.setSgFilialTerminal(macroZona.getTerminal().getFilial().getSgFilial());
		return macroZonaDTO;
	}

	private DispositivoUnitizacaoDTO getDispositivoUnitizacaoDTO(DispositivoUnitizacao dispositivo) {
		DispositivoUnitizacaoDTO dispositivoUnitizacaoDTO = new DispositivoUnitizacaoDTO();
		dispositivoUnitizacaoDTO.setId(dispositivo.getIdDispositivoUnitizacao());
		dispositivoUnitizacaoDTO.setTipoDispositivoUnitizacao(getTipoDispositivoUnitizacaoDTO(dispositivo));
		dispositivoUnitizacaoDTO.setNrIdentificacao(dispositivo.getNrIdentificacao());
		return dispositivoUnitizacaoDTO;
	}
	
	private EmpresaDTO getEmpresaDTO(DispositivoUnitizacao dispositivo) {
		EmpresaDTO empresa = new EmpresaDTO();
		empresa.setIdEmpresa(dispositivo.getEmpresa().getIdEmpresa());
		empresa.setNmPessoa(dispositivo.getEmpresa().getPessoa().getNmPessoa());
		return empresa;
	}

	private TipoDispositivoUnitizacaoDTO getTipoDispositivoUnitizacaoDTO(DispositivoUnitizacao dispositivo) {
		TipoDispositivoUnitizacaoDTO tipoDispositivoUnitizacao = new TipoDispositivoUnitizacaoDTO();
		tipoDispositivoUnitizacao.setIdTipoDispositivoUnitizacao(dispositivo.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
		tipoDispositivoUnitizacao.setDsTipoDispositivoUnitizacao(dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue());
		tipoDispositivoUnitizacao.setTpNrIdentificacao(tipoDispositivoUnitizacaoService.getNrTipoDispositivoUnitizacaoById(dispositivo.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao()));
		return tipoDispositivoUnitizacao;
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
			dispositivoUnitizacaoDTO.setSgFilialLocalizacaoMercadoria((String)item.get("sgFilialLocalizacao"));
			dispositivoUnitizacaoDTO.setDhUltimaMovimentacao(item.get("dhUltimaMovimentacao") == null ? null : ((DateTime)item.get("dhUltimaMovimentacao")));
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
	
	private List<EventoDispositivoUnitizacaoDTO> convertToEventoDTO(List<EventoDispositivoUnitizacao> list) {
		List<EventoDispositivoUnitizacaoDTO> eventos = new ArrayList<EventoDispositivoUnitizacaoDTO>();
		for (EventoDispositivoUnitizacao eventoDispositivoUnitizacao : list) {
			EventoDispositivoUnitizacaoDTO eventoDispositivoUnitizacaoDTO = new EventoDispositivoUnitizacaoDTO();
			
			eventoDispositivoUnitizacaoDTO.setDhInclusao(eventoDispositivoUnitizacao.getDhInclusao());
			EventoDTO evento = new EventoDTO();
			evento.setCdEvento(eventoDispositivoUnitizacao.getEvento().getCdEvento());
			evento.setTpEvento(eventoDispositivoUnitizacao.getEvento().getTpEvento());
			eventoDispositivoUnitizacaoDTO.setEvento(evento);
			FilialSuggestDTO filial = new FilialSuggestDTO();
			filial.setSgFilial(eventoDispositivoUnitizacao.getFilial().getSgFilial());
			eventoDispositivoUnitizacaoDTO.setFilial(filial);
			eventoDispositivoUnitizacaoDTO.setObComplemento(eventoDispositivoUnitizacao.getObComplemento());
			eventoDispositivoUnitizacaoDTO.setTpScan(eventoDispositivoUnitizacao.getTpScan());
			UsuarioDTO usuario = new UsuarioDTO();
			usuario.setNmUsuario(eventoDispositivoUnitizacao.getUsuario().getNmUsuario());
			eventoDispositivoUnitizacaoDTO.setUsuario(usuario);
			
			eventos.add(eventoDispositivoUnitizacaoDTO);
		}
		return eventos;
	}
	
	private TypedFlatMap getTypedFlatMap(DispositivoUnitizacaoFiltroDTO filtro) {
		
		TypedFlatMap toReturn = super.getTypedFlatMapWithPaginationInfo(filtro);
		
		if (filtro.getEmpresa() != null) {
			toReturn.put("empresa.idEmpresa", filtro.getEmpresa().getIdEmpresa());
		}
		if (filtro.getTipoDispositivoUnitizacao() != null) {
			toReturn.put("tipoDispositivoUnitizacao.idTipoDispositivoUnitizacao", filtro.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
			toReturn.put("tpNrIdentificacao", String.valueOf(filtro.getTipoDispositivoUnitizacao().getTpNrIdentificacao()));
		}
		toReturn.put("nrIdentificacao", filtro.getNrIdentificacao());
		if (filtro.getTpSituacao() != null) {
			toReturn.put("tpSituacao.value", filtro.getTpSituacao().getValue());
		}

		toReturn.put("dispositivoVazio", String.valueOf(filtro.getDispositivoVazio()));
		
		if (filtro.getDispositivoUnitizacaoPai() != null) {
			toReturn.put("idDispositivoUnitizacaoPai", filtro.getDispositivoUnitizacaoPai().getId());
		}
		
		if (filtro.getFilial() != null) {
            toReturn.put("filial.idFilial", filtro.getFilial().getId());
        }else{
            if("E".equals(filtro.getAcao())){
                throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("filialLocalizacao")});
            }
        }
		
		return toReturn;
	}
}

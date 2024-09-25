package com.mercurio.lms.mww.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.sim.model.DescricaoEvento;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.mww.conferirVolumeAction"
 */
public class ConferirVolumeAction extends CrudAction {
   
	private static final String DATE_PATTERN_WITH_TIMEZONE = "dd/MM/yyyy HH:mm ZZ";
	private ConferirVolumeService conferirVolumeService;
	private EventoVolumeService eventoVolumeService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private VolumeNotaFiscalService volumeNotaFiscalService;

	/**
	 * 
	 * @param barCode
	 * @return Map
	 */
	
	public Map<String, Object> findVolumeByBarCode(Map param){
		String barCode =(String)param.get("barCode");
		return conferirVolumeService.findVolumeByBarCode(barCode);    	
    }
	public Map<String, Object> findVolumeByBarCodeSorter(Map param){	
		String barCode = (String)param.get("barCode");
		Boolean blSorter= (Boolean)param.get("blSorter");
		return conferirVolumeService.findVolumeByBarCodeSorter(barCode, blSorter);
	}
	
	public Map<String, Object> findPaginatedEventoVolumeByIdVolume(Map param){
		Long id = this.getLongProperty("id", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));	
		ResultSetPage<EventoVolume> rs = eventoVolumeService.findPaginatedByIdVolume(id, new FindDefinition(pageNumber, pageSize, null));
		return this.getEventoVolumePaginatedMappedList(rs, pageSize);    	
    }
	
	public Map<String, Object> findPaginatedEventoDispositivoByIdDispositivo(Map param){
		Long id = this.getLongProperty("id", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));	
		ResultSetPage<EventoDispositivoUnitizacao> rs = eventoDispositivoUnitizacaoService.findPaginatedByIdDispositivo(id, new FindDefinition(pageNumber, pageSize, null));
		return this.getEventoDispositivoPaginatedMappedList(rs, pageSize);    	
    }
	
	public Map<String, Object> findPaginatedEventoDocumentoByIdDocumento(Map param){
		Long id = this.getLongProperty("id", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));	
		ResultSetPage<EventoDocumentoServico> rs = eventoDocumentoServicoService.findPaginatedByIdDocumento(id, new FindDefinition(pageNumber, pageSize, null));
		return this.getEventoDocumentoPaginatedMappedList(rs, pageSize);    	
    }
	
	public Map<String, Object> findPaginatedEventoMeioTranspByIdMeioTransp(Map param){
		Long id = this.getLongProperty("id", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));	
		ResultSetPage<EventoMeioTransporte> rs = eventoMeioTransporteService.findPaginatedByIdMeioTransporte(id, new FindDefinition(pageNumber, pageSize, null));
		return this.getEventoMeioTransportePaginatedMappedList(rs, pageSize);    	
    }
	
	public void findMensagemAlteraLocalizacao (String key) {
		throw new BusinessException(key);
	}
	
	private Map<String,Object> getEventoVolumePaginatedMappedList(ResultSetPage<EventoVolume> rs, Integer pageSize) {
		Map<String,Object> retorno = createPaginatedMap(rs, pageSize);
		List<EventoVolume> itens = rs.getList();			
		retorno.put("itens", getEventoVolumeListMapped(itens));
		return retorno;
	}
	
	private Map<String, Object> getEventoDispositivoPaginatedMappedList(ResultSetPage<EventoDispositivoUnitizacao> rs, Integer pageSize) {
		Map<String,Object> retorno = createPaginatedMap(rs, pageSize);
		List<EventoDispositivoUnitizacao> itens = rs.getList();			
		retorno.put("itens", getEventoDispositivoListMapped(itens));
		return retorno;
	}
	
	private Map<String, Object> getEventoDocumentoPaginatedMappedList(ResultSetPage<EventoDocumentoServico> rs, Integer pageSize) {
		Map<String,Object> retorno = createPaginatedMap(rs, pageSize);
		List<EventoDocumentoServico> itens = rs.getList();			
		retorno.put("itens", getEventoDocumentoListMapped(itens));
		return retorno;
	}
	
	private Map<String, Object> getEventoMeioTransportePaginatedMappedList(ResultSetPage<EventoMeioTransporte> rs, Integer pageSize) {
		Map<String,Object> retorno = createPaginatedMap(rs, pageSize);
		List<EventoMeioTransporte> itens = rs.getList();			
		retorno.put("itens", getEventoMeioTransporteListMapped(itens));
		return retorno;
	}
	
	private Map<String,Object> createPaginatedMap(ResultSetPage rs, Integer pageSize){
		Map<String,Object> retorno = new HashMap<String, Object>();				
						
		BigDecimal bdRowCount = new BigDecimal(rs.getRowCount());
		BigDecimal bdPageSize = new BigDecimal(pageSize);
		
		Integer pageCount = bdRowCount.divide(bdPageSize,RoundingMode.DOWN).add(BigDecimal.valueOf(1)).intValue();		
		
		retorno.put("rowCount", rs.getRowCount());
		retorno.put("pageCount", pageCount);
		retorno.put("currentPage", rs.getCurrentPage());
		retorno.put("hasNext", rs.getHasNextPage());
		retorno.put("hasPrior", rs.getHasPriorPage());
		return retorno;
	}

	private List<Map<String,Object>> getEventoVolumeListMapped(List<EventoVolume> itens) {
		List<Map<String,Object>> listMapEvtVolume = new ArrayList();
		for(EventoVolume evtVol : itens){
			listMapEvtVolume.add(getEventoVolumeMapped(evtVol));
		}
		return listMapEvtVolume;
	}
	
	private List<Map<String,Object>> getEventoDispositivoListMapped(List<EventoDispositivoUnitizacao> itens) {
		List<Map<String,Object>> listMap = new ArrayList();
		for(EventoDispositivoUnitizacao evt : itens){
			listMap.add(getEventoDispositivoMapped(evt));
		}
		return listMap;
	}
	
	private List<Map<String,Object>> getEventoDocumentoListMapped(List<EventoDocumentoServico> itens) {
		List<Map<String,Object>> listMap = new ArrayList();
		for(EventoDocumentoServico evt : itens){
			listMap.add(getEventoDocumentoMapped(evt));
		}
		return listMap;
	}
	
	private List<Map<String,Object>> getEventoMeioTransporteListMapped(List<EventoMeioTransporte> itens) {
		List<Map<String,Object>> listMap = new ArrayList();
		for(EventoMeioTransporte evt : itens){
			listMap.add(getEventoMeioTransporteMapped(evt));
		}
		return listMap;
	}
	
	private Map<String, Object> getEventoVolumeMapped(EventoVolume evtVol) {
		if(evtVol == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tpScan", evtVol.getTpScan().getValue());
		map.put("idEventoVolume", evtVol.getIdEventoVolume());
		map.put("dhEvento", JTDateTimeUtils.formatDateTimeToString(evtVol.getDhEvento(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("dhInclusao", JTDateTimeUtils.formatDateTimeToString(evtVol.getDhInclusao(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("blEventoCancelado", evtVol.getBlEventoCancelado());
		map.put("obComplemento", evtVol.getObComplemento());
		map.put("evento", getEventoMapped(evtVol.getEvento()));
		map.put("filial", getFilialMapped(evtVol.getFilial()));
		map.put("usuario", getUsuarioMapped(evtVol.getUsuario()));
		return map;
	}

	private Map<String, Object> getEventoDispositivoMapped(EventoDispositivoUnitizacao evt) {
		if(evt == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tpScan", evt.getTpScan().getValue());
		map.put("idEventoDispositivoUnitizacao", evt.getIdEventoDispositivoUnitizacao());
		map.put("dhEvento", JTDateTimeUtils.formatDateTimeToString(evt.getDhEvento(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("dhInclusao", JTDateTimeUtils.formatDateTimeToString(evt.getDhInclusao(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("blEventoCancelado", evt.getBlEventoCancelado());
		map.put("obComplemento", evt.getObComplemento());
		map.put("evento", getEventoMapped(evt.getEvento()));
		map.put("filial", getFilialMapped(evt.getFilial()));
		map.put("usuario", getUsuarioMapped(evt.getUsuario()));
		return map;
	}
	
	private Map<String, Object> getEventoDocumentoMapped(EventoDocumentoServico evt) {
		if(evt == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idEventoDocumentoServico", evt.getIdEventoDocumentoServico());
		map.put("dhEvento", JTDateTimeUtils.formatDateTimeToString(evt.getDhEvento(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("dhInclusao", JTDateTimeUtils.formatDateTimeToString(evt.getDhInclusao(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("blEventoCancelado", evt.getBlEventoCancelado());
		map.put("obComplemento", evt.getObComplemento());
		map.put("evento", getEventoMapped(evt.getEvento()));
		map.put("filial", getFilialMapped(evt.getFilial()));
		map.put("usuario", getUsuarioMapped(evt.getUsuario()));
		return map;
	}
	
	private Map<String, Object> getEventoMeioTransporteMapped(EventoMeioTransporte evt) {
		if(evt == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idEventoMeioTransporte", evt.getIdEventoMeioTransporte());
		map.put("dhInicioEvento", JTDateTimeUtils.formatDateTimeToString(evt.getDhInicioEvento(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("tpSituacaoMeioTransporte", eventoMeioTransporteService.determinaDescEvento(evt));
		map.put("dhFimEvento", JTDateTimeUtils.formatDateTimeToString(evt.getDhFimEvento(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("dhGeracao", JTDateTimeUtils.formatDateTimeToString(evt.getDhGeracao(), DATE_PATTERN_WITH_TIMEZONE));
		map.put("dsLocalManutencao", evt.getDsLocalManutencao());
		map.put("filial", getFilialMapped(evt.getFilial()));
		map.put("box", getBoxMapped(evt.getBox()));
		return map;
	}

	public java.io.Serializable executeGeraEventoVolumeEncontrado(Long idVolumeNotaFiscal) {
		return conferirVolumeService.executeGeraEventoVolumeEncontrado(idVolumeNotaFiscal);
	}
	public java.io.Serializable executeEventoVolumeLido(Long idVolumeNotaFiscal) {
		return conferirVolumeService.executeEventoVolumeLido(idVolumeNotaFiscal);
	}	
	public java.io.Serializable executeGeraEventoVolumeEncontradoTpScan(Long idVolumeNotaFiscal, String tpScan){
		return conferirVolumeService.executeGeraEventoVolumeEncontradoTpScan(idVolumeNotaFiscal, tpScan);
	}
	public java.io.Serializable executeEventoVolumeLidoTpScan(Long idVolumeNotaFiscal, String tpScan) {
		return conferirVolumeService.executeEventoVolumeLidoTpScan(idVolumeNotaFiscal,tpScan);
	}
	
	/**
	 * Atualiza o volume, colocando ele na filial do usu�rio logado e setando a localiza��o da mercadoria para �No Terminal�
	 */
	public void executeAtualizarFilialLocalizacaoVolume(Long idVolumeNotaFiscal) {
		VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findById(idVolumeNotaFiscal);	
		volumeNotaFiscalService.executeAtualizarFilialLocalizacaoVolume(volumeNotaFiscal);		
	}

	private Map<String, Object> getBoxMapped(Box box) {
		if(box == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idBox", box.getIdBox());
		map.put("nrBox", box.getNrBox());
		map.put("dsBox", box.getDsBox());
		map.put("tpSituacaoBox", box.getTpSituacaoBox().getValue());
		map.put("obBox", box.getObBox());
		return map;
	}
	
	private Map<String, Object> getUsuarioMapped(Usuario usuario) {
		if(usuario == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("login", usuario.getLogin());
		map.put("idUsuario", usuario.getIdUsuario());
		map.put("nomeUsuario", usuario.getNmUsuario());
		return map;
	}

	private Map<String, Object> getFilialMapped(Filial filial) {
		if(filial == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idFilial", filial.getIdFilial());
		map.put("sgFilial", filial.getSgFilial());
		return map;
	}

	private Map<String, Object> getEventoMapped(Evento evento) {
		if(evento == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idEvento", evento.getIdEvento());
		map.put("cdEvento", evento.getCdEvento());
		map.put("tpEvento", evento.getTpEvento().getValue());
		map.put("blExibeCliente", evento.getBlExibeCliente());
		map.put("blGeraParceiro", evento.getBlGeraParceiro());
		map.put("cancelaEvento", getEventoMapped(evento.getCancelaEvento()));
		map.put("tpSituacao", evento.getTpSituacao().getValue());
		map.put("descricaoEvento", getDescricaoEventoMapped(evento.getDescricaoEvento()));					
		return map;
	}

	private Map<String, Object> getDescricaoEventoMapped(DescricaoEvento descricaoEvento) {
		if(descricaoEvento == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idDescricaoEvento", descricaoEvento.getIdDescricaoEvento());
		map.put("cdDescricaoEvento", descricaoEvento.getCdDescricaoEvento());
		map.put("dsDescricaoEvento", descricaoEvento.getDsDescricaoEvento());
		map.put("tpSituacao", descricaoEvento.getTpSituacao().getValue());
		return map;
	}

	private Long getLongProperty(String mapKey, Map mapa) {
		if(mapa.get(mapKey) != null) {
			return Long.parseLong((String) mapa.get(mapKey));
		} else {
			return null;
		}		
	}
	
	public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
		this.conferirVolumeService = conferirVolumeService;
	}
	
	public ConferirVolumeService getConferirVolumeService() {
		return conferirVolumeService;
	}
	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}

	public void setEventoMeioTransporteService(
			EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}
	
	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}
	
	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
}

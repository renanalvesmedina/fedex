package com.mercurio.lms.rest.expedicao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaPendencia;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.service.AwbOcorrenciaPendenciaService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.LocalizacaoAwbCiaAereaService;
import com.mercurio.lms.expedicao.model.service.TrackingAwbService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.rest.expedicao.dto.AwbOcorrenciaPendenciaDTO;
import com.mercurio.lms.rest.expedicao.dto.NetworkAereoAwbDTO;
import com.mercurio.lms.rest.expedicao.dto.OcorrenciaPendenciaComboDTO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.TimeUtils;
 
@Path("/expedicao/networkAereo") 
public class NetworkAereoRest extends BaseRest { 

	private static final String CHAVE_TODAS = "todas2";

	private static final String TMP_ATUALIZACAO_TELA_NETWORK_AEREO = "TMP_ATUALIZACAO_TELA_NETWORK_AEREO";

	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private AwbService awbService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private AwbOcorrenciaPendenciaService awbOcorrenciaPendenciaService;
	
	@InjectInJersey
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	
	@InjectInJersey 
	LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService;
	
	@InjectInJersey
	TrackingAwbService trackingAwbService;
	
	@InjectInJersey
	ConfiguracoesFacade configuracoesFacade;
	
	@POST
	@Path("getIntervalAtualizacao")
	public Integer getIntervalAtualizacao() {
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(TMP_ATUALIZACAO_TELA_NETWORK_AEREO);		
		return TimeUtils.convertSecondToMiliSecond(Integer.valueOf(parametroGeral.getDsConteudo()));
	}
	
	private TypedFlatMap convertFilter(NetworkAereoFilterDTO filter) {
		TypedFlatMap t = new TypedFlatMap();
		
		if ( filter.getDtAtualizacaoInicial() != null ){
			t.put("dtPeriodoInicial",filter.getDtAtualizacaoInicial());
		}
		if ( filter.getDtAtualizacaoFinal() != null ){
			t.put("dtPeriodoFinal",filter.getDtAtualizacaoFinal());
		}
		if ( filter.getAwb() != null ){
			t.put("idAwb",filter.getAwb().getIdAwb());
		}
		if ( filter.getMonitoramentoAeroportoOrigem() != null ){
			t.put("aeroportoOrigem", filter.getMonitoramentoAeroportoOrigem().getIdAeroporto());
		}
		if ( filter.getMonitoramentoAeroportoDestino() != null ){
			t.put("aeroportoDestino",filter.getMonitoramentoAeroportoDestino().getIdAeroporto());
		}
		if ( filter.getServico() != null ){
			t.put("servico", filter.getServico().getValue());
		}
		if ( filter.getCiaAerea() != null ){
			t.put("idCiaAerea", Long.valueOf(filter.getCiaAerea().get("idEmpresa").toString()));
		}
		if ( filter.getSituacao() != null ){
			t.put("situacao",filter.getSituacao().getValue());
		}
		if ( filter.getSituacaoFS() != null ){
			t.put("situacaoFS",filter.getSituacaoFS());
		}
		
		return t;
	}

	@POST
	@Path("findMonitoramentoNetworkAereoCiaAerea")
	public Response findMonitoramentoNetworkAereoCiaAerea(NetworkAereoFilterDTO filter) {
		TypedFlatMap criteria = convertFilter(filter);
		
		List<Map<String, Object>> list  = awbService.findMonitoramentoNetworkAereoCiaAerea(criteria);
		
		List<NetworkAereoDTO> listNetworkAereoDTO = new ArrayList<NetworkAereoDTO>();
		Integer totalPreAwb = 0;
		Integer totalAwb = 0;
		Integer totalAguardandoEmbarque = 0;
		Integer totalAguardandoEntrega = 0;
		Integer totalEmtransito = 0;
		Integer totalDisponivel = 0;
		Integer totalRetirada = 0;
		Integer totalLiberacaoFiscal = 0;
		Double  totalTT = 0D;
		Integer semaforoEea = 0; 
		Integer semaforoEva = 0; 
		Integer semaforoDpr = 0; 
		
		for (Map<String, Object> map : list) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			
			NetworkAereoDTO networkAereoDTO = new NetworkAereoDTO();
			networkAereoDTO.setIdCiaAerea(tfm.getLong("id_cia_aerea"));
			networkAereoDTO.setNmCiaAerea(tfm.getString("nm_cia_aerea"));
			networkAereoDTO.setCountPreAwb(tfm.getInteger("qt_pre_awb"));
			networkAereoDTO.setCountAwb(tfm.getInteger("qt_awb"));
			networkAereoDTO.setCountAguardandoEmbarque(tfm.getInteger("qt_embarque_aereo"));
			networkAereoDTO.setCountAguardandoEntrega(tfm.getInteger("qt_aguardando_entrega"));
			networkAereoDTO.setEea(tfm.getInteger("eea"));
			networkAereoDTO.setEva(tfm.getInteger("eva"));
			networkAereoDTO.setDpr(tfm.getInteger("dpr"));
			networkAereoDTO.setCountEmTransito(tfm.getInteger("qt_viagem_aerea"));
			networkAereoDTO.setCountEmLibFiscal(tfm.getInteger("qt_liberacao_fiscal"));
			networkAereoDTO.setCountDisponivel(tfm.getInteger("qt_disponivel"));
			networkAereoDTO.setTempoTransferencia(tfm.getDouble("tt"));
			networkAereoDTO.setTempoTransferenciaFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoDTO.getTempoTransferencia()));
			networkAereoDTO.setCountRetirada(tfm.getInteger("qt_retirada"));

			listNetworkAereoDTO.add(networkAereoDTO);
			
			totalPreAwb = IntegerUtils.add(totalPreAwb, networkAereoDTO.getCountPreAwb());
			totalAwb = IntegerUtils.add(totalAwb, networkAereoDTO.getCountAwb());
			totalAguardandoEmbarque = IntegerUtils.add(totalAguardandoEmbarque, networkAereoDTO.getCountAguardandoEmbarque());
			totalAguardandoEntrega = IntegerUtils.add(totalAguardandoEntrega, networkAereoDTO.getCountAguardandoEntrega());
			totalEmtransito = IntegerUtils.add(totalEmtransito, networkAereoDTO.getCountEmTransito());
			totalDisponivel = IntegerUtils.add(totalDisponivel, networkAereoDTO.getCountDisponivel());
			totalRetirada = IntegerUtils.add(totalRetirada, networkAereoDTO.getCountRetirada());
			totalLiberacaoFiscal = IntegerUtils.add(totalLiberacaoFiscal, networkAereoDTO.getCountEmLibFiscal());
			totalTT = this.getTempoTransferenciaMaior(totalTT, networkAereoDTO.getTempoTransferencia());
			semaforoEea = this.getCriticidadeMaior(semaforoEea, networkAereoDTO.getEea());
			semaforoEva = this.getCriticidadeMaior(semaforoEva, networkAereoDTO.getEva());
			semaforoDpr = this.getCriticidadeMaior(semaforoDpr, networkAereoDTO.getDpr());
		}
		
		if (!listNetworkAereoDTO.isEmpty()) {
			NetworkAereoDTO networkAereoDTO = new NetworkAereoDTO();
			
			networkAereoDTO.setIdCiaAerea(null);
			networkAereoDTO.setNmCiaAerea(configuracoesFacade.getMensagem(CHAVE_TODAS));
			networkAereoDTO.setCountPreAwb(totalPreAwb);
			networkAereoDTO.setCountAwb(totalAwb);
			networkAereoDTO.setCountAguardandoEmbarque(totalAguardandoEmbarque);
			networkAereoDTO.setCountAguardandoEntrega(totalAguardandoEntrega);
			networkAereoDTO.setCountEmTransito(totalEmtransito);
			networkAereoDTO.setCountRetirada(totalRetirada);
			networkAereoDTO.setCountDisponivel(totalDisponivel);
			networkAereoDTO.setCountEmLibFiscal(totalLiberacaoFiscal);
			networkAereoDTO.setTempoTransferencia(totalTT);
			networkAereoDTO.setEea(semaforoEea);
			networkAereoDTO.setEva(semaforoEva);
			networkAereoDTO.setDpr(semaforoDpr);
			networkAereoDTO.setTempoTransferenciaFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoDTO.getTempoTransferencia()));
			
			listNetworkAereoDTO.add(0, networkAereoDTO);
		}
		
		return getReturnFind(listNetworkAereoDTO, listNetworkAereoDTO.size());
	}
	
	private Double getTempoTransferenciaMaior(Double ttAtual, Double ttNovo) {
		if (ttAtual < ttNovo) {
			return ttNovo;
		}
		
		return ttAtual;
	}

	private Integer getCriticidadeMaior(Integer semaforoAtual, Integer semaforoNovo) {
		if (semaforoAtual < semaforoNovo) {
			return semaforoNovo;
		}
		
		return semaforoAtual;
	}
	
	@POST
	@Path("findMonitoramentoNetworkAereoAwb")
	public Response findMonitoramentoNetworkAereoAwb(NetworkAereoFilterDTO filter) {
		TypedFlatMap criteria = convertFilter(filter);
		
		List<Map<String, Object>> list  = awbService.findMonitoramentoNetworkAereoAwb(criteria);
		
		List<NetworkAereoAwbDTO> listNetworkAereoAwbDTO = new ArrayList<NetworkAereoAwbDTO>();
		for (Map<String, Object> map : list) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			
			NetworkAereoAwbDTO networkAereoAwbDTO = new NetworkAereoAwbDTO();
			networkAereoAwbDTO.setAeroportoOrigem(tfm.getString("sg_aeroporto_origem"));
			networkAereoAwbDTO.setAeroportoDestino(tfm.getString("sg_aeroporto_destino"));
			networkAereoAwbDTO.setPreAwb(tfm.getString("nr_pre_awb"));
			networkAereoAwbDTO.setIdAwb(tfm.getLong("id_awb"));
			networkAereoAwbDTO.setAwb(tfm.getString("nr_awb"));
			networkAereoAwbDTO.setIdCiaAerea(tfm.getLong("id_cia_aerea"));
			networkAereoAwbDTO.setNmCiaAerea(tfm.getString("nm_cia_aerea"));
			networkAereoAwbDTO.setSgCiaAerea(tfm.getString("sg_empresa"));
			networkAereoAwbDTO.setEea(tfm.getInteger("eea"));
			networkAereoAwbDTO.setEva(tfm.getInteger("eva"));
			networkAereoAwbDTO.setDpr(tfm.getInteger("dpr"));
			networkAereoAwbDTO.setValor(FormatUtils.formatDecimal("#,###,###,###,##0.00", tfm.getBigDecimal("vl_awb")));
			networkAereoAwbDTO.setPeso(FormatUtils.formatDecimal("#,###,###,###,##0.000", tfm.getBigDecimal("ps_total")));
			
			if(tfm.get("dh_AN") != null){		
				networkAereoAwbDTO.setEntrega(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_AN")).getTime()));
			}
			if(tfm.get("dh_EV") != null){		
				networkAereoAwbDTO.setEmbarque(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_EV")).getTime()));
			}
			if(tfm.get("dh_FV") != null){		
				networkAereoAwbDTO.setDesembarque(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_FV")).getTime()));
			}
			if(tfm.get("dh_RE") != null){		
				networkAereoAwbDTO.setRetirada(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_RE")).getTime()));
			}
			if(tfm.get("dh_DR") != null){		
				networkAereoAwbDTO.setDisponivelRetirada(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_DR")).getTime()));
			}
			if(tfm.get("dh_FS") != null){		
				networkAereoAwbDTO.setRetidoSefaz(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_FS")).getTime()));
			}
			if(tfm.get("dh_LF") != null){		
				networkAereoAwbDTO.setLiberadoSefaz(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_LF")).getTime()));
			}
			if(tfm.get("dh_AE") != null){		
				networkAereoAwbDTO.setDhAguardandoEmbarque(new org.joda.time.DateTime(((Timestamp)tfm.get("dh_AE")).getTime()));
			}

			DomainValue tpLocalizacao = tfm.getDomainValue("tp_localizacao");
			if (tpLocalizacao != null) {
				networkAereoAwbDTO.setUltimoStatus(tpLocalizacao);
			}			
			
			BigDecimal ttSefaz = tfm.getBigDecimal("tt_sefaz");
			if (BigDecimalUtils.hasValue(ttSefaz)) {
				networkAereoAwbDTO.setTtSefaz(ttSefaz);
				networkAereoAwbDTO.setTtSefazFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoAwbDTO.getTtSefaz().doubleValue()));
			}
			
			BigDecimal ttEea = tfm.getBigDecimal("tt_eea");
			if (BigDecimalUtils.hasValue(ttEea)) {
				networkAereoAwbDTO.setTtEEA(ttEea);
				networkAereoAwbDTO.setTtEEAFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoAwbDTO.getTtEEA().doubleValue()));
			}
			
			BigDecimal ttEva = tfm.getBigDecimal("tt_eva");
			if (BigDecimalUtils.hasValue(ttEva)) {
				networkAereoAwbDTO.setTtEVA(ttEva);
				networkAereoAwbDTO.setTtEVAFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoAwbDTO.getTtEVA().doubleValue()));
			}
			
			BigDecimal ttdpr = tfm.getBigDecimal("tt_total");
			if (BigDecimalUtils.hasValue(ttdpr)) {
				networkAereoAwbDTO.setTtDpr(ttdpr);
				networkAereoAwbDTO.setTtDprFormatado(TimeUtils.convertFractionToHourMinutes(networkAereoAwbDTO.getTtDpr().doubleValue()));
			}	
			
			listNetworkAereoAwbDTO.add(networkAereoAwbDTO);
		}
		
		return getReturnFind(listNetworkAereoAwbDTO, listNetworkAereoAwbDTO.size());
	}

	@POST
	@Path("findOcorrenciaPendenciaAtiva")
	public Response findOcorrenciaPendenciaAtiva() {
		List<OcorrenciaPendencia> l = ocorrenciaPendenciaService.findOcorrenciaPendenciaAtiva();
		
		List<OcorrenciaPendenciaComboDTO> list = new ArrayList<OcorrenciaPendenciaComboDTO>();
		for (OcorrenciaPendencia op : l) {
			list.add(new OcorrenciaPendenciaComboDTO(op.getIdOcorrenciaPendencia(),
					op.getDsOcorrencia().getValue(),
					op.getCdOcorrencia(),
					op.getTpOcorrencia().getDescriptionAsString()));
		}
		
		return Response.ok(list).build();
	}
	
	@POST
	@Path("findAwbOcorrenciaAwbPendenciaByAwb")
	public Response findAwbOcorrenciaAwbPendenciaByAwb(Long idAwb) {
		List<AwbOcorrenciaPendencia> list = awbOcorrenciaPendenciaService.findAwbOcorrenciaByAwb(idAwb);
		List<AwbOcorrenciaPendenciaDTO> listAwbOcorrenciaPendenciaDTO = new ArrayList<AwbOcorrenciaPendenciaDTO>();
		for (AwbOcorrenciaPendencia aop : list) {
			AwbOcorrenciaPendenciaDTO awbOcorrenciaPendenciaDTO = new AwbOcorrenciaPendenciaDTO();
			
			awbOcorrenciaPendenciaDTO.setId(aop.getIdAwbOcorrenciaPendencia());
			awbOcorrenciaPendenciaDTO.setDhOcorrenciaPendencia(aop.getDhOcorrencia());
			awbOcorrenciaPendenciaDTO.setOcorrencia(aop.getOcorrenciaPendencia().getDsOcorrencia().getValue());
			awbOcorrenciaPendenciaDTO.setTpOcorrencia(aop.getOcorrenciaPendencia().getTpOcorrencia().getDescriptionAsString());
			awbOcorrenciaPendenciaDTO.setUsuarioOcorrencia(aop.getUsuarioLms().getUsuarioADSM().getNmUsuario());
			
			listAwbOcorrenciaPendenciaDTO.add(awbOcorrenciaPendenciaDTO);
		}
		
		return getReturnFind(listAwbOcorrenciaPendenciaDTO, listAwbOcorrenciaPendenciaDTO.size());
	}
	
	@POST
	@Path("storeAwbOcorrenciaPendencia")
	public Response storeAwbOcorrenciaPendencia(AwbOcorrenciaPendenciaDTO awbOcorrenciaPendenciaDTO) {	
		awbOcorrenciaPendenciaService.storeAwbOcorrenciaPendencia(
				awbOcorrenciaPendenciaDTO.getIdAwb(), 
				awbOcorrenciaPendenciaDTO.getIdOcorrenciaPendencia(),
				awbOcorrenciaPendenciaDTO.getIdCiaAerea(),
				awbOcorrenciaPendenciaDTO.getDhOcorrenciaPendencia()
				);
		
		

		return Response.ok().build();
	}
	
	@POST
	@Path("findLocalizacoesByCiaAereaAwb")
	public List<Map<String, Object>> findLocalizacoesByCiaAereaAwb(Map<String, Object> criteria) {
		List<LocalizacaoAwbCiaAerea> localizacoes  = localizacaoAwbCiaAereaService.findLocalizacaoesCiaAereaByIdCiaAereaAndTpLocalizacao(Long.parseLong(String.valueOf(criteria.get("idCiaAerea"))));
		List<Map<String, Object>> mapas = new ArrayList<Map<String,Object>>();
		for(LocalizacaoAwbCiaAerea loc : localizacoes){
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("idLocalizacao", loc.getIdLocalizacaoAwbCiaAerea());
			mapa.put("dsTracking", loc.getDsTracking());
			mapas.add(mapa);
		}
		return mapas;
	}
	
	
	@POST
	@Path("findTrackingByCiaAereaAndAwb")
	public Response findTrackingAwbByCiaAereaAndAwb(Long idAwb) {
		List<Map<String, Object>> localizacoes =  trackingAwbService.findTrackingAwbByCiaAereaAndAwb(idAwb);
		List<EventosLocalizacaoDTO> listlocalizacoesTracking = new ArrayList<EventosLocalizacaoDTO>();
		for (Map<String, Object> map : localizacoes) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			EventosLocalizacaoDTO evento = new EventosLocalizacaoDTO();
			evento.setDsTracking(tfm.getString("dsTracking"));
			evento.setDataEventoFormatada(JTDateTimeUtils.formatDateTimeToString(tfm.getDateTime("dhEvento")));
			evento.setTpLocalizacao(tfm.getDomainValue("tpLocalizacao").getDescriptionAsString());
			listlocalizacoesTracking.add(evento);
		}
		
		return getReturnFind(listlocalizacoesTracking, listlocalizacoesTracking.size());
	}
	
	@POST
	@Path("storeEventosLocalizacao")
	public Response storeEventosLocalizacao(EventosLocalizacaoDTO eventosLocalizacaoDTO) {	
		trackingAwbService.storeIncluirEventoLocalizacaoModal(
				eventosLocalizacaoDTO.getIdAwb(), 
				eventosLocalizacaoDTO.getIdLocalizacao(),
				eventosLocalizacaoDTO.getDtEvento()
				);

		return Response.ok().build();
	}

	@POST
	@Path("validateOcorrenciaPendencia")
	public void validateOcorrenciaPendencia(AwbOcorrenciaPendenciaDTO awbOcorrenciaPendenciaDTO) {
		Long idAwb = awbOcorrenciaPendenciaDTO.getIdAwb();
		Long idCiaAerea = awbOcorrenciaPendenciaDTO.getIdCiaAerea();
		Long idOcorrenciaPendencia = awbOcorrenciaPendenciaDTO.getIdOcorrenciaPendencia();
		
		awbOcorrenciaPendenciaService.validateOcorrenciaPendencia(idAwb, idOcorrenciaPendencia, idCiaAerea);
	}

} 

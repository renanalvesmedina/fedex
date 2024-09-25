package com.mercurio.lms.portaria.model.processo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.evento.EventoPortaria;
import com.mercurio.lms.portaria.model.evento.EventoPortariaColeta;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDoctoServico;
import com.mercurio.lms.portaria.model.evento.EventoPortariaManifestoColeta;
import com.mercurio.lms.portaria.model.evento.EventoPortariaMeioTransporte;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

/**
 * LMSA-768 - Processo "Informar Chegada na Portaria" de {@link ControleCarga}s
 * de operações tipo Coleta/Entrega.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class ChegadaPortariaColetaEntrega extends ChegadaPortariaControleCarga {

	private Map<String, OcorrenciaColeta> ocorrenciaColetaMap;
	private List<Long> documentosEntregues;
	private List<Long> volumesEntregues;
	private List<EventoVolumeNotaFiscal> eventosVolumesNotaFiscal;

	private ChegadaPortariaColetaEntrega(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		super(service, dao, parameters);
	}

	/**
	 * Factory method para "Informar Chegada na Portaria" tipo Coleta/Entrega.
	 * 
	 * @param service
	 *            Classe de serviços para processo.
	 * @param dao
	 *            DAO para carga, validação e processamento.
	 * @param parameters
	 *            Mapa de parâmetros gerados na action.
	 * @return Instância de {@link ChegadaPortariaColetaEntrega}.
	 */
	public static ChegadaPortaria createChegadaPortaria(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		return new ChegadaPortariaColetaEntrega(service, dao, parameters);
	}

	/**
	 * Carrega dados do {@link ControleCarga} a processar para uma operação tipo
	 * Coleta/Entrega.
	 */
	@Override
	protected void carregarDados() {
		super.carregarDados();
		controleCarga = dao.findControleCarga(parameters.getLong("idControleCarga"));
	}

	/**
	 * Prepara o processamento de operações tipo Coleta/Entrega.
	 */
	@Override
	protected void prepararProcessamento() {
		super.prepararProcessamento();
		ocorrenciaColetaMap = dao.findOcorrenciaColetaMap();
		documentosEntregues = dao.findDocumentosEntregues(controleCarga);
		volumesEntregues = dao.findVolumesEntregues(controleCarga);
		eventosVolumesNotaFiscal = new ArrayList<EventoVolumeNotaFiscal>();
	}

	/**
	 * Valida processamento de {@link ControleCarga} de operação tipo
	 * Coleta/Entrega conforme as regras de negócio.
	 */
	@Override
	protected boolean validarInformarChegada() {
		String tpStatus = controleCarga.getTpStatusControleCarga().getValue();
		if (!ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA.equals(tpStatus)) {
			throw new BusinessException("LMS-06038");
		}
		if (dao.countOcorrenciaBloqueio(controleCarga) > 0) {
			throw new BusinessException("LMS-06041");
		}
		if (dao.countOcorrenciaLiberacao(controleCarga, dhChegada.minusMinutes(2)) > 0) {
			throw new BusinessException("LMS-06042");
		}
		return true;
	}

	/**
	 * Concluí processamento com encerramento de {@link ManifestoEletronico}s e
	 * registro de quilometragem informada para o {@link MeioTransporte}.
	 */
	@Override
	protected void processarInformarChegada() {
		super.processarInformarChegada();
		service.encerrarManifestosEletronicos(controleCarga);
		service.processarQuilometragemMeioTransporte(
				filialSessao, transportado, nrQuilometragem, blVirouHodometro, controleCarga, obChegada);
	}

	/**
	 * Registra eventos para operações tipo Coleta/Entrega.
	 */
	@Override
	protected void registrarEventos() {
		super.registrarEventos();
		registrarEventosManifesto();
		registrarEventosManifestoColeta();
	}

	/**
	 * Concluí geração do {@link EventoMeioTransporte} definindo atributo
	 * {@code tpSituacao} com valor {@code 'AGDE'} (Aguardando Descarga).
	 */
	@Override
	protected EventoPortariaMeioTransporte gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		return super.gerarEventoMeioTransporte(meioTransporte)
				.setTpSituacao(ConstantesPortaria.TP_SITUACAO_AGUARDANDO_DESCARGA);
	}

	private void registrarEventosManifesto() {
		List<Manifesto> manifestos = controleCarga.getManifestos();
		if (!manifestos.isEmpty()) {
			for (Manifesto manifesto : manifestos) {
				if (validarManifesto(manifesto)) {
					registrarEventosDoctoServico(manifesto);
					eventosVolumesNotaFiscal.addAll(obterEventosVolumeNotaFiscal(manifesto));
				}
			}
		}
	}

	private List<EventoVolumeNotaFiscal> obterEventosVolumeNotaFiscal(Manifesto manifesto) {
		return obterEventosVolumeNotaFiscal(getVolumeNotaFiscais(manifesto), true, false);
	}

	private List<EventoVolumeNotaFiscal> obterEventosVolumeNotaFiscal(Set<VolumeNotaFiscal> volumeNotaFiscais, boolean entrega, boolean aero) {
		List<EventoVolumeNotaFiscal> eventosVolumes = new ArrayList<EventoVolumeNotaFiscal>();

		for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscais) {
			if (validarVolumeNotaFiscal(volumeNotaFiscal, aero)) {
				Evento evento = getEvento(volumeNotaFiscal.getLocalizacaoMercadoria(), entrega, false);
				eventosVolumes.add(new EventoVolumeNotaFiscal(evento, volumeNotaFiscal));
			}
		}
		return eventosVolumes;
	}
	
	private boolean validarVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal, boolean aero) {
		Short cdLocalizacaoMercadoria = volumeNotaFiscal.getLocalizacaoMercadoria() != null
				? volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
		return !ConstantesPortaria.CD_LOCALIZACAO_ENTREGA_REALIZADA.equals(cdLocalizacaoMercadoria)
				&& (aero || !volumesEntregues.contains(volumeNotaFiscal.getIdVolumeNotaFiscal()));
	}

	private void registrarEventosManifestoColeta() {
		List<ManifestoColeta> manifestoColetas = controleCarga.getManifestoColetas();
		if (!manifestoColetas.isEmpty()) {
			for (ManifestoColeta manifestoColeta : manifestoColetas) {
				registrarEvento(gerarEventoManifestoColeta(manifestoColeta));
				registrarEventosPedidoColeta(manifestoColeta);
				registrarEventosDoctoServico(manifestoColeta);
				eventosVolumesNotaFiscal.addAll(obterEventosVolumeNotaFiscal(manifestoColeta));
			}
		}
	}

	private List<EventoVolumeNotaFiscal> obterEventosVolumeNotaFiscal(ManifestoColeta manifestoColeta) {
		List<EventoVolumeNotaFiscal> eventosVolumes = new ArrayList<EventoVolumeNotaFiscal>();
		for (PedidoColeta pedidoColeta : manifestoColeta.getPedidoColetas()) {
			Set<VolumeNotaFiscal> volumeNotaFiscais = getVolumeNotaFiscais(pedidoColeta);
			if (validarManifestoColeta(manifestoColeta)) {
				eventosVolumes.addAll(obterEventosVolumeNotaFiscal(volumeNotaFiscais, false, false));
			} else if (validarPedidoColetaAeroporto(pedidoColeta)) {
				eventosVolumes.addAll(obterEventosVolumeNotaFiscal(volumeNotaFiscais, false, true));
			}
		}
		return eventosVolumes;
	}
	
	private Set<VolumeNotaFiscal> getVolumeNotaFiscais(PedidoColeta pedidoColeta) {
		Set<VolumeNotaFiscal> volumeNotaFiscais = new HashSet<VolumeNotaFiscal>();
		for (DoctoServico doctoServico : getDoctoServicos(pedidoColeta)) {
			addAllVolumeNotaFiscais(volumeNotaFiscais, doctoServico);
		}
		return volumeNotaFiscais;
	}
	
	private void addAllVolumeNotaFiscais(Set<VolumeNotaFiscal> volumeNotaFiscais, DoctoServico doctoServico) {
		for (PreManifestoVolume preManifestoVolume : doctoServico.getPreManifestoVolumes()) {
			volumeNotaFiscais.add(preManifestoVolume.getVolumeNotaFiscal());
		}
		for (ManifestoEntregaVolume manifestoEntregaVolume : doctoServico.getManifestoEntregaVolumes()) {
			volumeNotaFiscais.add(manifestoEntregaVolume.getVolumeNotaFiscal());
		}
	}

	private void registrarEventosPedidoColeta(ManifestoColeta manifestoColeta) {
		for (PedidoColeta pedidoColeta : manifestoColeta.getPedidoColetas()) {
			if (validarPedidoColeta(pedidoColeta)) {
				registrarEvento(gerarEventoPedidoColeta(pedidoColeta));
			}
		}
	}

	private void registrarEventosDoctoServico(Manifesto manifesto) {
		registrarEventosDoctoServico(getDoctoServicos(manifesto), true, false);
	}

	private void registrarEventosDoctoServico(ManifestoColeta manifestoColeta) {
		for (PedidoColeta pedidoColeta : manifestoColeta.getPedidoColetas()) {
			Set<DoctoServico> doctoServicos = getDoctoServicos(pedidoColeta);
			if (validarManifestoColeta(manifestoColeta)) {
				registrarEventosDoctoServico(doctoServicos, false, false);
			} else if (validarPedidoColetaAeroporto(pedidoColeta)) {
				registrarEventosDoctoServico(doctoServicos, false, true);
			}
		}
	}

	private void registrarEventosDoctoServico(Set<DoctoServico> doctoServicos, boolean entrega, boolean aero) {
		for (DoctoServico doctoServico : doctoServicos) {
			if (validarDoctoServico(doctoServico, aero)) {
				registrarEvento(gerarEventoDoctoServico(doctoServico, entrega));
			}
		}
	}

	private Set<DoctoServico> getDoctoServicos(Manifesto manifesto) {
		Set<DoctoServico> doctoServicos = new HashSet<DoctoServico>();
		for (PreManifestoDocumento preManifestoDocumento : manifesto.getPreManifestoDocumentos()) {
			doctoServicos.add(preManifestoDocumento.getDoctoServico());
		}
		ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
		if (manifestoEntrega != null) {
			for (ManifestoEntregaDocumento manifestoEntregaDocumento : manifestoEntrega.getManifestoEntregaDocumentos()) {
				doctoServicos.add(manifestoEntregaDocumento.getDoctoServico());
			}
		}
		return doctoServicos;
	}

	private Set<DoctoServico> getDoctoServicos(PedidoColeta pedidoColeta) {
		Set<DoctoServico> doctoServicos = new HashSet<DoctoServico>();
		@SuppressWarnings("unchecked")
		List<DetalheColeta> detalheColetas = pedidoColeta.getDetalheColetas();
		for (DetalheColeta detalheColeta : detalheColetas) {
			CollectionUtils.addIgnoreNull(doctoServicos, detalheColeta.getDoctoServico());
		}
		return doctoServicos;
	}

	private Set<VolumeNotaFiscal> getVolumeNotaFiscais(Manifesto manifesto) {
		Set<VolumeNotaFiscal> volumeNotaFiscais = new HashSet<VolumeNotaFiscal>();
		for (PreManifestoVolume preManifestoVolume : manifesto.getPreManifestoVolumes()) {
			volumeNotaFiscais.add(preManifestoVolume.getVolumeNotaFiscal());
		}
		ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
		if (manifestoEntrega != null) {
			@SuppressWarnings("unchecked")
			List<ManifestoEntregaVolume> manifestoEntregaVolumes = manifestoEntrega.getManifestoEntregaVolumes();
			for (ManifestoEntregaVolume manifestoEntregaVolume : manifestoEntregaVolumes) {
				volumeNotaFiscais.add(manifestoEntregaVolume.getVolumeNotaFiscal());
			}
		}
		return volumeNotaFiscais;
	}
	
	private Evento getEvento(LocalizacaoMercadoria localizacaoMercadoria, boolean entrega, boolean bloqueio) {
		if (localizacaoMercadoria != null) {
			if (bloqueio && pendBloqLocalizacaoList.contains(localizacaoMercadoria.getIdLocalizacaoMercadoria())) {
				return cdEventoMap.get(ConstantesPortaria.CD_EVENTO_SEM_LOCALIZACAO_MERCADORIA);
			}
			if (ConstantesPortaria.CD_LOCALIZACAO_COLETA_REALIZADA_NO_AEROPORTO.equals(localizacaoMercadoria.getCdLocalizacaoMercadoria())
					|| ConstantesPortaria.CD_LOCALIZACAO_ROTA_DE_ENTREGA_DIRETA_AO_CLIENTE.equals(
							localizacaoMercadoria.getCdLocalizacaoMercadoria())) {
				return cdEventoMap.get(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA);
			}
		}
		return entrega ? cdEventoMap.get(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA) : null;
	}

	private boolean validarManifesto(Manifesto manifesto) {
		String tpStatus = manifesto.getTpStatusManifesto().getValue();
		return ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA.equals(tpStatus);
	}

	private boolean validarManifestoColeta(ManifestoColeta manifestoColeta) {
		String tpStatus = manifestoColeta.getTpStatusManifestoColeta().getValue();
		return ConstantesPortaria.TP_STATUS_EM_TRANSITO_COLETA.equals(tpStatus);
	}

	private boolean validarPedidoColeta(PedidoColeta pedidoColeta) {
		String tpStatus = pedidoColeta.getTpStatusColeta().getValue();
		return ConstantesPortaria.TP_STATUS_COLETA_EXECUTADA.equals(tpStatus);
	}

	private boolean validarPedidoColetaAeroporto(PedidoColeta pedidoColeta) {
		String tpStatus = pedidoColeta.getTpPedidoColeta().getValue();
		return ConstantesPortaria.TP_PEDIDO_COLETA_AEROPORTO.equals(tpStatus);
	}

	private boolean validarDoctoServico(DoctoServico doctoServico, boolean aero) {
		Short cdLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria() != null
				? doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
		return !ConstantesPortaria.CD_LOCALIZACAO_ENTREGA_REALIZADA.equals(cdLocalizacaoMercadoria)
				&& (aero || !documentosEntregues.contains(doctoServico.getIdDoctoServico()));
	}

	private EventoPortariaManifestoColeta gerarEventoManifestoColeta(ManifestoColeta manifestoColeta) {
		return EventoPortariaManifestoColeta
				.createEventoPortaria(dhChegada, manifestoColeta)
				.setTpEvento(ConstantesPortaria.TP_EVENTO_CHEGADA_NA_PORTARIA);
	}

	private EventoPortaria<?> gerarEventoPedidoColeta(PedidoColeta pedidoColeta) {
		OcorrenciaColeta ocorrenciaColeta = ocorrenciaColetaMap.get(ConstantesPortaria.TP_EVENTO_CHEGADA_NA_PORTARIA);
		return EventoPortariaColeta
				.createEventoPortaria(usuarioLogado, dhChegada, pedidoColeta)
				.setOcorrenciaColeta(ocorrenciaColeta)
				.setTpEvento(ConstantesPortaria.TP_EVENTO_CHEGADA_NA_PORTARIA)
				.setDsDescricao(ocorrenciaColeta.getDsDescricaoResumida().getValue())
				.setMeioTransporteRodoviario(transportado.getMeioTransporteRodoviario());
	}

	private EventoPortariaDoctoServico gerarEventoDoctoServico(DoctoServico doctoServico, boolean entrega) {
		Evento evento = getEvento(doctoServico.getLocalizacaoMercadoria(), entrega, true);
		if (evento != null) {
			return gerarEventoDoctoServico(doctoServico)
					.setEvento(evento);
		}
		return null;
	}

	/**
	 * Registra atualizações para operações tipo Coleta/Entrega.
	 */
	@Override
	protected void registrarAtualizacoes() {
		
		if(getParametroPortChegadaNovo()){
			registrarAtualizacao(gerarAtualizacaoControleCarga(ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA, dhChegada));
			registrarAtualizacao(gerarAtualizacaoManifesto());
			registrarAtualizacaoManifestoColeta();
			registrarAtualizacaoPedidoColeta();
			registrarAtualizacaoDoctoServico();
			registrarAtualizacao(gerarAtualizacaoOcorrenciaDoctoServico(ConstantesPortaria.CD_FASE_PROCESSO_NO_TERMINAL));
			registrarAtualizacaoVolumeNotaFiscal();
			registrarAtualizacaoDispositivoUnitizacao();
		} else {
			registrarAtualizacao(gerarAtualizacaoControleCarga(ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA, dhChegada));
			registrarAtualizacao(gerarAtualizacaoManifesto());
			registrarAtualizacao(gerarAtualizacaoManifestoColeta(new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoPedidoColeta(new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, null, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, null, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoOcorrenciaDoctoServico(ConstantesPortaria.CD_FASE_PROCESSO_NO_TERMINAL));
			registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, new ArrayList<Long>()));
		}
	}

	private void registrarAtualizacaoDispositivoUnitizacao(){
		List<Long> idsDispositivoUnitizacaoAguardandoDescarga = new ArrayList<Long>(getIdsDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA));
		if(!idsDispositivoUnitizacaoAguardandoDescarga.isEmpty()){
			while(idsDispositivoUnitizacaoAguardandoDescarga.size() > 1000){
				List<Long> sublistIdsDispositivoUnitizacao = new ArrayList<Long>(idsDispositivoUnitizacaoAguardandoDescarga.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, sublistIdsDispositivoUnitizacao));
				idsDispositivoUnitizacaoAguardandoDescarga.removeAll(sublistIdsDispositivoUnitizacao);
			}
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, idsDispositivoUnitizacaoAguardandoDescarga));
		}
		
		List<Long> idsDispositivoUnitizacaoMercadoriaRetornada = new ArrayList<Long>(getIdsDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA));
		if(!idsDispositivoUnitizacaoMercadoriaRetornada.isEmpty()){
			while(idsDispositivoUnitizacaoMercadoriaRetornada.size() > 1000){
				List<Long> sublistIdsDispositivoUnitizacao = new ArrayList<Long>(idsDispositivoUnitizacaoMercadoriaRetornada.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, sublistIdsDispositivoUnitizacao));
				idsDispositivoUnitizacaoMercadoriaRetornada.removeAll(sublistIdsDispositivoUnitizacao);
			}
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, idsDispositivoUnitizacaoMercadoriaRetornada));
		}
	}
	
	private void registrarAtualizacaoPedidoColeta(){
		List<Long> idsPedidoColeta = new ArrayList<Long>(getIdsPedidoColetas());
		if(!idsPedidoColeta.isEmpty()){
			while(idsPedidoColeta.size() > 1000){
				List<Long> sublistIdsPedidoColeta = new ArrayList<Long>(idsPedidoColeta.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoPedidoColeta(sublistIdsPedidoColeta));
				idsPedidoColeta.removeAll(sublistIdsPedidoColeta);
			}
			registrarAtualizacao(gerarAtualizacaoPedidoColeta(idsPedidoColeta));
		}
	}

	private Set<Long> getIdsVolumeNotaFiscal(short cdEvento){
		Set<Long> idsVolumeNotaFiscal = new HashSet<Long>();
		for (EventoVolumeNotaFiscal eventoVolume : eventosVolumesNotaFiscal) {
			if(eventoVolume.getEvento().getCdEvento().compareTo(cdEvento) == 0) {
				idsVolumeNotaFiscal.add(eventoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal());
			}
		}
		return idsVolumeNotaFiscal;
	}
	
	private void registrarAtualizacaoVolumeNotaFiscal() {
		List<Long> idsVolumeNotaFiscalAguardandoDescarga = new ArrayList<Long>(getIdsVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA));
		if(!idsVolumeNotaFiscalAguardandoDescarga.isEmpty()){
			while(idsVolumeNotaFiscalAguardandoDescarga.size() > 1000){
				List<Long> sublistIdsVolumeNotaFiscal = new ArrayList<Long>(idsVolumeNotaFiscalAguardandoDescarga.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, sublistIdsVolumeNotaFiscal));
				idsVolumeNotaFiscalAguardandoDescarga.removeAll(sublistIdsVolumeNotaFiscal);
			}
			registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, idsVolumeNotaFiscalAguardandoDescarga));
		}
		
		List<Long> idsVolumeNotaFiscalMercadoriaRetornada = new ArrayList<Long>(getIdsVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA));
		if(!idsVolumeNotaFiscalMercadoriaRetornada.isEmpty()){
			while(idsVolumeNotaFiscalMercadoriaRetornada.size() > 1000){
				List<Long> sublistIdsVolumeNotaFiscal = new ArrayList<Long>(idsVolumeNotaFiscalMercadoriaRetornada.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, sublistIdsVolumeNotaFiscal));
				idsVolumeNotaFiscalMercadoriaRetornada.removeAll(sublistIdsVolumeNotaFiscal);
			}
			registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, idsVolumeNotaFiscalMercadoriaRetornada));
		}
	}
	
	private void registrarAtualizacaoManifestoColeta(){
		
		List<Long> idsManifestoColeta = new ArrayList<Long>(getIdsManifestoColetas());
		if(!idsManifestoColeta.isEmpty()){
			while(idsManifestoColeta.size() > 1000){
				List<Long> sublistIdsManifestoColeta = new ArrayList<Long>(idsManifestoColeta.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoManifestoColeta(sublistIdsManifestoColeta));
				idsManifestoColeta.removeAll(sublistIdsManifestoColeta);
			}
			registrarAtualizacao(gerarAtualizacaoManifestoColeta(idsManifestoColeta));
		}
		
	}
	
	private void registrarAtualizacaoDoctoServico(){
		
		List<Long> idsDoctoServicosAguardandoDescarga = new ArrayList<Long>(getIdsDoctoServicos(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA));
		if(!idsDoctoServicosAguardandoDescarga.isEmpty()){
			while(idsDoctoServicosAguardandoDescarga.size() > 1000){
				List<Long> sublistIdsDoctoServico = new ArrayList<Long>(idsDoctoServicosAguardandoDescarga.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, null, sublistIdsDoctoServico));
				idsDoctoServicosAguardandoDescarga.removeAll(sublistIdsDoctoServico);
			}
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, null, idsDoctoServicosAguardandoDescarga));
		}
		
		List<Long> idsDoctoServicosMercadoriaRetornada = new ArrayList<Long>(getIdsDoctoServicos(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA));
		if(!idsDoctoServicosMercadoriaRetornada.isEmpty()){
			while(idsDoctoServicosMercadoriaRetornada.size() > 1000){
				List<Long> sublistIdsDoctoServico = new ArrayList<Long>(idsDoctoServicosMercadoriaRetornada.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, null, sublistIdsDoctoServico));
				idsDoctoServicosMercadoriaRetornada.removeAll(sublistIdsDoctoServico);
			}
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_MERCADORIA_RETORNADA, null, idsDoctoServicosMercadoriaRetornada));
		}
		
	}

	private Query gerarAtualizacaoManifesto() {
		return dao.queryUpdateManifestoColetaEntrega(controleCarga);
	}

	private Query gerarAtualizacaoManifestoColeta(List<Long> idsManifestoColeta) {
		return dao.queryUpdateManifestoColeta(getParametroPortChegadaNovo(), idsManifestoColeta, controleCarga, dhChegada);
	}

	private Query gerarAtualizacaoPedidoColeta(List<Long> idsPedidoColeta) {
		return dao.queryUpdatePedidoColeta(getParametroPortChegadaNovo(), idsPedidoColeta, controleCarga, dhChegada);
	}

}

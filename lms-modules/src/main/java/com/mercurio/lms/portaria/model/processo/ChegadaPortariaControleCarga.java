package com.mercurio.lms.portaria.model.processo;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.evento.EventoPortariaControleCarga;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDispositivoUnitizacao;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDoctoServico;
import com.mercurio.lms.portaria.model.evento.EventoPortariaMeioTransporte;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.util.FormatUtils;

/**
 * LMSA-768 - Classe abstrata base para processo "Informar Chegada na Portaria"
 * de {@link ControleCarga}s de operações tipo Viagem e Coleta/Entrega.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public abstract class ChegadaPortariaControleCarga extends ChegadaPortaria {

	protected ControleCarga controleCarga;

	protected Filial filialOrigem;
	protected Filial filialDestino;
	protected String nrDocumento;
	protected String obComplemento;
	protected DomainValue tpDocumento;

	protected List<Long> pendBloqLocalizacaoList;
	protected Map<Short, Evento> cdEventoMap;
	protected Map<Short, FaseProcesso> cdFaseProcessoMap;

	protected ChegadaPortariaControleCarga(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		super(service, dao, parameters);
	}

	/**
	 * Prepara o processamento dos {@link MeioTransporte}s relacionados ao
	 * {@link ControleCarga}.
	 */
	@Override
	protected void prepararMeioTransporte() {
		transportado = controleCarga.getMeioTransporteByIdTransportado();
		semiReboque = controleCarga.getMeioTransporteByIdSemiRebocado();
	}

	/**
	 * Prepara o processamento de operações de Viagem e de Coleta/Entrega.
	 */
	@Override
	protected void prepararProcessamento() {
		super.prepararProcessamento();

		filialOrigem = controleCarga.getFilialByIdFilialOrigem();
		filialDestino = controleCarga.getFilialByIdFilialDestino();
		nrDocumento = FormatUtils.formatSgFilialWithLong(filialOrigem.getSgFilial(), controleCarga.getNrControleCarga());
		obComplemento = filialSessao.getSiglaNomeFilial();
		tpDocumento = new DomainValue(ConstantesPortaria.TP_DOCUMENTO_CONTROLE_CARGA);

		pendBloqLocalizacaoList = dao.findPendBloqLocalizacaoList();
		cdEventoMap = dao.findCdEventoMap();
		cdFaseProcessoMap = dao.findCdFaseProcessoMap();
	}

	/**
	 * Registra eventos para "Informar Chegada na Portaria" de operações tipo
	 * Viagem e Coleta/Entrega.
	 */
	@Override
	protected void registrarEventos() {
		super.registrarEventos();
		registrarEvento(gerarEventoControleCarga());
	}

	/**
	 * Complementa geração do {@link EventoMeioTransporte} definindo
	 * relacionamento com {@link ControleCarga}.
	 */
	@Override
	protected EventoPortariaMeioTransporte gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		return super.gerarEventoMeioTransporte(meioTransporte)
				.setControleCarga(controleCarga);
	}

	private EventoPortariaControleCarga gerarEventoControleCarga() {
		return EventoPortariaControleCarga
				.createEventoPortaria(filialSessao, usuarioLogado, dhChegada, controleCarga)
				.setTpEvento(ConstantesPortaria.TP_EVENTO_CHEGADA_NA_PORTARIA)
				.setMeioTransporte(transportado);
	}

	/**
	 * Gera {@link EventoPortariaDoctoServico} básico, a ser complementado para
	 * operações tipo Viagem e Coleta/Entrega.
	 * 
	 * @param doctoServico
	 * @return
	 */
	protected EventoPortariaDoctoServico gerarEventoDoctoServico(DoctoServico doctoServico) {
		return EventoPortariaDoctoServico
				.createEventoPortaria(filialSessao, usuarioLogado, dhChegada, doctoServico)
				.setNrDocumento(nrDocumento);
	}
	
	/**
	 * Gera {@link EventoPortariaDispositivoUnitizacao} básico, a ser
	 * complementado para operações tipo Viagem e Coleta/Entrega.
	 * 
	 * @param dispositivoUnitizacao
	 * @return
	 */
	protected EventoPortariaDispositivoUnitizacao gerarEventoDispositivoUnitizacao(
			DispositivoUnitizacao dispositivoUnitizacao) {
		return EventoPortariaDispositivoUnitizacao
				.createEventoPortaria(filialSessao, usuarioLogado, dhChegada, dispositivoUnitizacao);
	}

	/**
	 * Gera atualização para {@link ControleCarga}.
	 * 
	 * @param tpStatus
	 * @param dhChegada
	 * @return
	 */
	protected Query gerarAtualizacaoControleCarga(String tpStatus, DateTime dhChegada) {
		return dao.queryUpdateControleCarga(controleCarga, filialSessao, tpStatus, dhChegada);
	}

	/**
	 * Gera atualização para {@link DoctoServico}s relacionados a determinado
	 * {@link Evento}.
	 * 
	 * @param cdEvento
	 * @param obComplemento
	 * @return
	 */
	protected Query gerarAtualizacaoDoctoServico(Short cdEvento, String obComplemento, List<Long> idsDoctoServico) {
		return dao.queryUpdateDoctoServico(getParametroPortChegadaNovo(), controleCarga, cdEventoMap.get(cdEvento), filialSessao, obComplemento, idsDoctoServico, dhChegada);
	}

	/**
	 * Gera atualização para {@link OcorrenciaDoctoServico}s.
	 * 
	 * @param cdFaseProcesso
	 * @return
	 */
	protected Query gerarAtualizacaoOcorrenciaDoctoServico(Short cdFaseProcesso) {
		return dao.queryUpdateOcorrenciaDoctoServico(controleCarga, cdFaseProcessoMap.get(cdFaseProcesso));
	}

	/**
	 * Gera atualização para {@link VolumeNotaFiscal}s relacionados a
	 * determinado {@link Evento}.
	 * 
	 * @param cdEvento
	 * @return
	 */
	protected Query gerarAtualizacaoVolumeNotaFiscal(Short cdEvento, List<Long> idsVolumeNotaFiscal) {
		return dao.queryUpdateVolumeNotaFiscal(getParametroPortChegadaNovo(), controleCarga, cdEventoMap.get(cdEvento), filialSessao, idsVolumeNotaFiscal, dhChegada);
	}

	/**
	 * Gera atualização para {@link DispositivoUnitizacao}s relacionados a
	 * determinado {@link Evento}.
	 * 
	 * @param cdEvento
	 * @return
	 */
	protected Query gerarAtualizacaoDispositivoUnitizacao(Short cdEvento, List<Long> idsDispositivoUnitizacao) {
		return dao.queryUpdateDispositivoUnitizacao(getParametroPortChegadaNovo(), controleCarga, cdEventoMap.get(cdEvento), filialSessao, idsDispositivoUnitizacao, dhChegada);
	}
	
	protected Query gerarAtualizacaoVolumeNotaFiscal(Short cdEvento, Manifesto manifesto) {
		return dao.queryUpdateVolumeNotaFiscal(manifesto, cdEventoMap.get(cdEvento), filialSessao);
	}
	
}

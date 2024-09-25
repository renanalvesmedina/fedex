package com.mercurio.lms.portaria.model.processo;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.service.NewInformarSaidaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.FormatUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.*;

/**
 * LMSA-1002 - Processo "Informar Saída na Portaria" de {@link ControleCarga}s
 * de operações tipo Viagem.
 *
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 */
public class SaidaPortariaViagem extends SaidaPortaria {

	private static final DomainValue TP_STATUS_EM_VIAGEM = new DomainValue(ConstantesPortaria.TP_STATUS_EM_VIAGEM);

	private Long idControleCarga;

	private ControleTrecho controleTrecho;
	private ControleCarga controleCarga;

	private Map<Short, Evento> cdEventoMap;
	private long nrAgrupadorAcaoIntegracao;

	private SaidaPortariaViagem(NewInformarSaidaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		super(service, dao, parameters);
	}

	/**
	 * Factory method para "Informar Saída na Portaria" tipo Viagem.
	 *
	 * @param service    Classe de serviços para processo.
	 * @param dao        DAO para carga, validação e processamento.
	 * @param parameters Mapa de parâmetros gerados na action.
	 * @return Instância de {@link SaidaPortariaViagem}.
	 */
	public static SaidaPortaria createSaidaPortaria(NewInformarSaidaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		return new SaidaPortariaViagem(service, dao, parameters);
	}

	/**
	 * Processa requisição considerando operação tipo Viagem.
	 */
	@Override
	protected void processarRequisicao() {
		super.processarRequisicao();
		idControleCarga = parameters.getLong("idControleCarga");
	}

	/**
	 * Carrega dados do {@link ControleTrecho} a processar para uma operação
	 * tipo Viagem.
	 */
	@Override
	protected void carregarDados() {
		controleTrecho = dao.findControleTrecho(idControleCarga, filialSessao.getIdFilial());
		controleCarga = controleTrecho.getControleCarga();
	}

	/**
	 * Valida processamento de {@link ControleCarga} de operação tipo Viagem
	 * conforme as regras de negócio.
	 */
	@Override
	protected boolean validarInformarSaida() {
		if (!validarControleCarga()) {
			throw new BusinessException("ERRO");
		}
		return true;
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
	 * Prepara o processamento de operações tipo Viagem.
	 */
	@Override
	protected void prepararProcessamento() {
		cdEventoMap = dao.findCdEventoMap();
		nrAgrupadorAcaoIntegracao = service.getEventoAcaoIntegracaoNrAgrupador();
	}

	/**
	 * Processa operações tipo Viagem.
	 */
	@Override
	protected void processarInformarSaida() {
		processarMeioTransporte(transportado);
		processarMeioTransporte(semiReboque);
		processarControleTrechos();
		processarControleCarga();
		processarManifestos();
	}

	/**
	 * Verifica regras de negócio de rastreabilidade,
	 * opcionalmente produzindo para
	 * determinado {@link EventoDocumentoServico}.
	 *
	 * @param entidade
	 */
	@Override
	protected EventoDocumentoServicoDMN criarEventoRastreabilidade(Serializable entidade) {
		if (entidade instanceof EventoDocumentoServico) {
			return EventoDoctoServicoHelper.convertEventoDoctoServico((EventoDocumentoServico) entidade);
		}
		return null;
	}

	private void processarMeioTransporte(MeioTransporte meioTransporte) {
		if (meioTransporte != null) {
			registrarEntidades(gerarEventoMeioTransporte(meioTransporte));
		}
	}

	private void processarControleTrechos() {
		for (ControleTrecho controleTrechoAux : controleCarga.getControleTrechos()) {
			if (filialSessao.equals(controleTrechoAux.getFilialByIdFilialOrigem())) {
				controleTrechoAux.setDhSaida(dhSaida);
				registrarEntidade(controleTrechoAux);
			}
		}
	}

	private void processarControleCarga() {
		registrarEntidade(controleCarga);
		registrarEntidade(gerarEventoControleCarga(ConstantesPortaria.TP_EVENTO_SAIDA_NA_PORTARIA));
		if (validarFilialAntecedePorto()) {
			registrarEntidade(gerarEventoControleCarga(ConstantesPortaria.TP_STATUS_EM_TRANSITO_PARA_O_PORTO));
		}
	}

	private void processarManifestos() {
		String nrDocumento = getNrDocumento(controleCarga);
		String dsObservacao = getDsObservacao(controleTrecho.getFilialByIdFilialDestino());
		DateTime dhPrevisaoChegada = getDhPrevisaoChegada();
		for (Manifesto manifesto : controleCarga.getManifestos()) {
			if (validarManifesto(manifesto)) {
				processarManifesto(manifesto, nrDocumento, dsObservacao, dhPrevisaoChegada);
			}
		}
		dao.getAdsmHibernateTemplate().clear();
	}

	private void processarManifesto(
			Manifesto manifesto, String nrDocumento, String dsObservacao, DateTime dhPrevisaoChegada) {
		if (validarManifestoViagem(manifesto)) {
			processarManifestoViagem(manifesto, dhPrevisaoChegada);
		}
		if (validarManifestoEscala(manifesto)) {
			processarManifestoEscala(manifesto, nrDocumento, dsObservacao);
			manifesto.setTpStatusManifesto(TP_STATUS_EM_VIAGEM);
			registrarEntidade(manifesto);
		}
		if (validarManifestoAcaoIntegracao(manifesto)) {
			registrarEntidades(gerarEventoAcaoIntegracao(manifesto));
		}
	}

	private void processarManifestoViagem(Manifesto manifesto, DateTime dhPrevisaoChegada) {
		Set<DoctoServico> doctoServicos = getDoctoServicos(manifesto);
		String dsObservacao = getDsObservacao(manifesto.getFilialByIdFilialDestino());
		for (DoctoServico doctoServico : doctoServicos) {
			registrarEntidade(gerarEventoPrevisaoChegada(doctoServico, dsObservacao, dhPrevisaoChegada));
		}
		gerarPreAlertaManifesto(manifesto, doctoServicos);
	}

	private void processarManifestoEscala(Manifesto manifesto, String nrDocumento, String dsObservacao) {
		Set<DoctoServico> doctoServicos = getDoctoServicos(manifesto);
		processarDoctoServicos(doctoServicos, nrDocumento, dsObservacao);
		Set<VolumeNotaFiscal> volumeNotaFiscais = getVolumeNotaFiscais(manifesto);
		processarDispositivosUnitizacao(getDispositivosUnitizacao(volumeNotaFiscais));
	}

	public void processarDoctoServicos(Set<DoctoServico> doctoServicos, String nrDocumento, String dsObservacao) {
		for (DoctoServico doctoServico : doctoServicos) {
			registrarEntidade(doctoServico);
			registrarEntidade(gerarEventoDoctoServico(doctoServico, nrDocumento, dsObservacao));
		}
	}

	public void processarVolumeNotaFiscais(Set<VolumeNotaFiscal> volumeNotaFiscais) {
		for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscais) {
			registrarEntidade(volumeNotaFiscal);
		}
	}

	public void processarDispositivosUnitizacao(Set<DispositivoUnitizacao> dispositivosUnitizacao) {
		for (DispositivoUnitizacao dispositivoUnitizacao : dispositivosUnitizacao) {
			registrarEntidade(dispositivoUnitizacao);
			registrarEntidade(gerarEventoDispositivoUnitizacao(dispositivoUnitizacao));
		}
	}

	private boolean validarControleCarga() {
		String tpStatus = controleCarga.getTpStatusControleCarga().getValue();
		return ConstantesPortaria.TP_STATUS_AGUARDANDO_SAIDA_PARA_VIAGEM.equals(tpStatus);
	}

	private boolean validarFilialAntecedePorto() {
		for (Manifesto manifesto : controleCarga.getManifestos()) {
			String tpModal = manifesto.getTpModal().getValue();
			if (ConstantesPortaria.TP_MODAL_RODOVIARIO.equals(tpModal)) {
				FilialRotaCc frc = dao.findFilialRota(controleCarga, filialSessao, null);
				if (frc != null) {
					Integer nrOrdem = frc.getNrOrdem().intValue() + 1;
					FilialRotaCc frcAux = dao.findFilialRota(controleCarga, null, nrOrdem);
					if (frcAux != null) {
						FluxoFilial fluxoFilial = dao.findFluxoFilial(filialSessao, frcAux.getFilial(), dhSaida);
						return fluxoFilial != null;
					}
				}
			}
		}
		return false;
	}

	private boolean validarManifesto(Manifesto manifesto) {
		String tpManifesto = manifesto.getTpManifesto().getValue();
		return ConstantesPortaria.TP_VIAGEM.equals(tpManifesto);
	}

	private boolean validarManifestoViagem(Manifesto manifesto) {
		String tpStatus = manifesto.getTpStatusManifesto().getValue();
		return ConstantesPortaria.TP_STATUS_EMITIDO.equals(tpStatus)
				|| ConstantesPortaria.TP_STATUS_EM_VIAGEM.equals(tpStatus);
	}

	private boolean validarManifestoEscala(Manifesto manifesto) {
		String tpStatus = manifesto.getTpStatusManifesto().getValue();
		return ConstantesPortaria.TP_STATUS_EMITIDO.equals(tpStatus)
				|| ConstantesPortaria.TP_STATUS_EM_ESCALA_NA_FILIAL.equals(tpStatus);
	}

	private boolean validarManifestoAcaoIntegracao(Manifesto manifesto) {
		Filial filial = manifesto.getFilialByIdFilialOrigem();
		return filialSessao.equals(filial);
	}

	private boolean validarDoctoServicoAereo(DoctoServico doctoServico) {
		String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();
		if (ConstantesPortaria.TP_DOCUMENTO_CONHECIMENTO_NACIONAL.equals(tpDocumentoServico)
				|| ConstantesPortaria.TP_DOCUMENTO_CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)) {
			Servico servico = doctoServico.getServico();
			String tpModal = servico != null ? servico.getTpModal().getValue() : null;
			return tpModal != null && ConstantesPortaria.TP_MODAL_AEREO.equals(tpModal);
		}
		return false;
	}

	private List<EventoMeioTransporte> gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		return service.gerarEventoMeioTransporte(filialSessao, controleTrecho, meioTransporte, dhSaida);
	}

	private EventoControleCarga gerarEventoControleCarga(String tpEvento) {
		return service.gerarEventoControleCarga(filialSessao, controleCarga, tpEvento, dhSaida);
	}

	private EventoDocumentoServico gerarEventoPrevisaoChegada(
			DoctoServico doctoServico, String dsObservacao, DateTime dhPrevisaoChegada) {
		return service.gerarEventoDoctoServico(
				filialSessao,
				usuarioLogado,
				doctoServico,
				getNrDocumento(doctoServico),
				dsObservacao,
				doctoServico.getTpDocumentoServico().getValue(),
				cdEventoMap.get(ConstantesPortaria.CD_EVENTO_PREVISAO_DE_CHEGADA),
				dhPrevisaoChegada);
	}

	private void gerarPreAlertaManifesto(Manifesto manifesto, Set<DoctoServico> doctoServicos) {
		List<DoctoServico> doctoServicoAereos = getDoctoServicoAereos(doctoServicos);
		if (!doctoServicoAereos.isEmpty()) {
			Collections.sort(doctoServicoAereos, new DoctoServicoComparator());
			service.gerarPreAlertaManifesto(manifesto, doctoServicoAereos);
		}
	}

	private EventoDocumentoServico gerarEventoDoctoServico(
			DoctoServico doctoServico, String nrDocumento, String dsObservacao) {
		return service.gerarEventoDoctoServico(
				filialSessao,
				usuarioLogado,
				doctoServico,
				nrDocumento,
				dsObservacao,
				ConstantesPortaria.TP_DOCUMENTO_CONTROLE_CARGA,
				cdEventoMap.get(ConstantesPortaria.CD_EVENTO_EM_VIAGEM),
				dhSaida);
	}

	private EventoDispositivoUnitizacao gerarEventoDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao) {
		return service.gerarEventoDispositivoUnitizacao(
				filialSessao,
				usuarioLogado,
				dispositivoUnitizacao,
				cdEventoMap.get(ConstantesPortaria.CD_EVENTO_EM_VIAGEM),
				dhSaida);
	}

	private List<AcaoIntegracaoEvento> gerarEventoAcaoIntegracao(Manifesto manifesto) {
		List<AcaoIntegracaoEvento> acaoIntegracaoEventos = service.gerarEventosAcaoIntegracao(manifesto);
		for (AcaoIntegracaoEvento acaoIntegracaoEvento : acaoIntegracaoEventos) {
			acaoIntegracaoEvento.setFilial(filialSessao);
			acaoIntegracaoEvento.setNrAgrupador(nrAgrupadorAcaoIntegracao++);
		}
		return acaoIntegracaoEventos;
	}

	private Set<DoctoServico> getDoctoServicos(Manifesto manifesto) {
		HashSet<DoctoServico> doctoServicos = new HashSet<DoctoServico>();
		for (PreManifestoDocumento preManifestoDocumento : manifesto.getPreManifestoDocumentos()) {
			doctoServicos.add(preManifestoDocumento.getDoctoServico());
		}
		ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();
		for (ManifestoNacionalCto manifestoNacionalCto : manifestoViagemNacional.getManifestoNacionalCtos()) {
			doctoServicos.add(manifestoNacionalCto.getConhecimento());
		}
		return doctoServicos;
	}

	private List<DoctoServico> getDoctoServicoAereos(Set<DoctoServico> doctoServicos) {
		List<DoctoServico> doctoServicoAereos = new ArrayList<DoctoServico>();
		for (DoctoServico doctoServico : doctoServicos) {
			if (validarDoctoServicoAereo(doctoServico)) {
				doctoServicoAereos.add(doctoServico);
			}
		}
		return doctoServicoAereos;
	}

	private Set<VolumeNotaFiscal> getVolumeNotaFiscais(Manifesto manifesto) {
		Set<VolumeNotaFiscal> volumeNotaFiscais = new HashSet<VolumeNotaFiscal>();
		for (PreManifestoVolume preManifestoVolume : manifesto.getPreManifestoVolumes()) {
			volumeNotaFiscais.add(preManifestoVolume.getVolumeNotaFiscal());
		}
		ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();
		for (ManifestoNacionalVolume manifestoNacionalVolume : manifestoViagemNacional.getManifestoNacionalVolumes()) {
			volumeNotaFiscais.add(manifestoNacionalVolume.getVolumeNotaFiscal());
		}
		return volumeNotaFiscais;
	}

	private Set<DispositivoUnitizacao> getDispositivosUnitizacao(Set<VolumeNotaFiscal> volumeNotaFiscais) {
		Set<DispositivoUnitizacao> dispositivosUnitizacao = new HashSet<DispositivoUnitizacao>();
		for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscais) {
			CollectionUtils.addIgnoreNull(dispositivosUnitizacao, volumeNotaFiscal.getDispositivoUnitizacao());
		}
		return dispositivosUnitizacao;
	}

	private String getNrDocumento(ControleCarga controleCarga) {
		String sgFilial = controleCarga.getFilialByIdFilialOrigem().getSgFilial();
		Long nrControleCarga = controleCarga.getNrControleCarga();
		return FormatUtils.formatSgFilialWithLong(sgFilial, nrControleCarga);
	}

	private String getNrDocumento(DoctoServico doctoServico) {
		String sgFilial = doctoServico.getFilialByIdFilialOrigem().getSgFilial();
		Long nrDoctoServico = doctoServico.getNrDoctoServico();
		return FormatUtils.formatSgFilialWithLong(sgFilial, nrDoctoServico);
	}

	private String getDsObservacao(Filial filial) {
		return new StringBuffer()
				.append(filial.getSgFilial())
				.append(" - ")
				.append(filial.getPessoa().getNmFantasia())
				.toString();
	}

	private DateTime getDhPrevisaoChegada() {
		DateTime dhPrevisaoChegada = controleTrecho.getDhPrevisaoChegada();
		String tpRotaViagem = controleCarga.getTpRotaViagem() != null
				? controleCarga.getTpRotaViagem().getValue() : null;
		if (ConstantesPortaria.TP_STATUS_EM_VIAGEM.equals(tpRotaViagem)) {
			dhPrevisaoChegada = dhSaida.plusMinutes(controleTrecho.getNrTempoViagem());
		}
		return dhPrevisaoChegada;
	}

	/**
	 * Classe auxiliar para ordenação de {@link DoctoServico} considerando:
	 * <ol>
	 * <li>sigla da {@link Filial} de destino;
	 * <li>sigla da {@link Filial} de origem; e
	 * <li>número do {@link DoctoServico}.
	 * </ol>
	 *
	 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
	 */
	private class DoctoServicoComparator implements Comparator<DoctoServico> {

		@Override
		public int compare(DoctoServico ds1, DoctoServico ds2) {
			return new CompareToBuilder()
					.append(sgFilial(ds1.getFilialByIdFilialDestino()), sgFilial(ds2.getFilialByIdFilialDestino()))
					.append(sgFilial(ds1.getFilialByIdFilialOrigem()), sgFilial(ds2.getFilialByIdFilialOrigem()))
					.append(ds1.getNrDoctoServico(), ds2.getNrDoctoServico())
					.toComparison();
		}

		private String sgFilial(Filial filial) {
			return filial != null ? filial.getSgFilial() : null;
		}

	}

}

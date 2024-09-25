package com.mercurio.lms.portaria.model.processo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDispositivoUnitizacao;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDoctoServico;
import com.mercurio.lms.portaria.model.evento.EventoPortariaMeioTransporte;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;

/**
 * LMSA-768 - Processo "Informar Chegada na Portaria" de {@link ControleTrecho}s
 * e {@link ControleCarga}s de operações tipo Viagem.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class ChegadaPortariaViagem extends ChegadaPortariaControleCarga {

	/**
	 * Factory method para "Informar Chegada na Portaria" tipo Viagem.
	 * 
	 * @param service
	 *            Classe de serviços para processo.
	 * @param dao
	 *            DAO para carga, validação e processamento.
	 * @param parameters
	 *            Mapa de parâmetros gerados na action.
	 * @return Instância de {@link ChegadaPortariaViagem}.
	 */
	public static ChegadaPortaria createChegadaPortaria(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		return new ChegadaPortariaViagem(service, dao, parameters);
	}

	private ControleTrecho controleTrecho;

	private ChegadaPortariaViagem(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		super(service, dao, parameters);
	}

	/**
	 * Carrega dados do {@link ControleTrecho} a processar para uma operação
	 * tipo Viagem.
	 */
	@Override
	protected void carregarDados() {
		super.carregarDados();
		controleTrecho = dao.findControleTrecho(parameters.getLong("idControleTrecho"));
		controleCarga = controleTrecho.getControleCarga();
	}

	/**
	 * Valida processamento de {@link ControleCarga} de operação tipo Viagem
	 * conforme as regras de negócio.
	 */
	@Override
	protected boolean validarInformarChegada() {
		String tpStatus = controleCarga.getTpStatusControleCarga().getValue();
		if (!ConstantesPortaria.TP_STATUS_EM_VIAGEM.equals(tpStatus)) {
			throw new BusinessException("LMS-06038");
		}
		if (!validarManifestoViagemNacional(controleCarga.getManifestos())) {
			throw new BusinessException("LMS-06034");
		}
		Filial filialSemInformacaoSaida = dao.findFilialAnteriorSemEvento(
				controleCarga, filialSessao, ConstantesPortaria.TP_EVENTO_SAIDA_NA_PORTARIA);
		if (filialSemInformacaoSaida != null) {
			throw new BusinessException("LMS-06043", new String[] {
					filialSemInformacaoSaida.getSgFilial()
			});
		}
		if (filialSessao.equals(controleCarga.getFilialByIdFilialOrigem())) {
			throw new BusinessException("LMS-05010");
		}
		return true;
	}

	/**
	 * Concluí processamento com encerramento opcional de
	 * {@link ManifestoEletronico}s.
	 */
	@Override
	protected void processarInformarChegada() {
		super.processarInformarChegada();
		if (validarManifestosEletronicos()) {
			service.encerrarManifestosEletronicos(controleCarga);
		}
		service.geraXmlSorterPortaria(controleCarga);
	}

	

	/**
	 * Registra eventos para "Informar Chegada na Portaria" de operações tipo
	 * Viagem.
	 */
	@Override
	protected void registrarEventos() {
		super.registrarEventos();
		registrarEventosManifesto();
	}

	/**
	 * Concluí geração do {@link EventoMeioTransporte} definindo relacionamento
	 * com {@link ControleTrecho} e atributo {@code tpSituacao} com valor
	 * {@code 'AGDE'} (Aguardando Descarga) ou {@code 'PAOP'} (Parada
	 * Operacional) conforme {@link Filial} de destino.
	 */
	@Override
	protected EventoPortariaMeioTransporte gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		String tpSituacao = filialDestino == null || filialSessao.equals(filialDestino)
				? ConstantesPortaria.TP_SITUACAO_AGUARDANDO_DESCARGA
				: ConstantesPortaria.TP_SITUACAO_PARADA_OPERACIONAL;
		return super.gerarEventoMeioTransporte(meioTransporte)
				.setControleTrecho(controleTrecho)
				.setTpSituacao(tpSituacao);
	}

	private void registrarEventosManifesto() {
		for (Manifesto manifesto : controleCarga.getManifestos()) {
			if (validarManifesto(manifesto)) {
				registrarEventosManifesto(manifesto);
			}
		}
	}

	private void registrarEventosManifesto(Manifesto manifesto) {
		Filial filialDestino = manifesto.getFilialByIdFilialDestino();
		registrarEventosDoctoServico(getDoctoServicos(manifesto), filialDestino);
		List<ManifestoNacionalVolume> manifestoNacionalVolumes = manifesto.getManifestoViagemNacional().getManifestoNacionalVolumes();
		registrarEventosManifestoNacionalVolume(manifestoNacionalVolumes, filialDestino);
	}

	private void registrarEventosDoctoServico(Set<DoctoServico> doctoServicos, Filial filialDestino) {
		for (DoctoServico doctoServico : doctoServicos) {
			if (validarDoctoServico(doctoServico)) {
				registrarEvento(gerarEventoDoctoServico(doctoServico, filialDestino));
			}
		}
	}

	private void registrarEventosManifestoNacionalVolume(
			List<ManifestoNacionalVolume> manifestoNacionalVolumes, Filial filialDestino) {
		for (ManifestoNacionalVolume manifestoNacionalVolume : manifestoNacionalVolumes) {
			VolumeNotaFiscal volumeNotaFiscal = manifestoNacionalVolume.getVolumeNotaFiscal();
			if (validarVolumeNotaFiscal(volumeNotaFiscal)) {
				registrarEventoDispositivoUnitizacao(filialDestino, volumeNotaFiscal);
			}
		}
	}

	private void registrarEventoDispositivoUnitizacao(Filial filialDestino, VolumeNotaFiscal volumeNotaFiscal) {
		if (volumeNotaFiscal.getDispositivoUnitizacao() != null) {
			registrarEvento(gerarEventoDispositivoUnitizacao(volumeNotaFiscal.getDispositivoUnitizacao(), filialDestino));
		}
	}

	private boolean validarManifesto(Manifesto manifesto) {
		String tpStatus = manifesto.getTpStatusManifesto().getValue();
		return ConstantesPortaria.TP_STATUS_EM_VIAGEM.equals(tpStatus);
	}

	private boolean validarManifestoViagemNacional(List<Manifesto> manifestos) {
		for (Manifesto manifesto : manifestos) {
			ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();
			Integer nrCto = Integer.valueOf(manifestoViagemNacional.getManifestoNacionalCtos().size());
			if (!nrCto.equals(manifestoViagemNacional.getNrCto())) {
				return false;
			}
		}
		return true;
	}

	private boolean validarManifestosEletronicos() {
		return filialSessao.equals(filialDestino) || dao.findFilialPorto(controleCarga, filialSessao, dhChegada);
	}

	private boolean validarDoctoServico(DoctoServico doctoServico) {
		Short cdLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria() != null
				? doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
		return !ConstantesPortaria.CD_LOCALIZACAO_ENTREGA_REALIZADA.equals(cdLocalizacaoMercadoria);
	}

	private boolean validarVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		Short cdLocalizacaoMercadoria = volumeNotaFiscal.getLocalizacaoMercadoria() != null
				? volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() : null;
		return !ConstantesPortaria.CD_LOCALIZACAO_ENTREGA_REALIZADA.equals(cdLocalizacaoMercadoria);
	}

	private Set<DoctoServico> getDoctoServicos(Manifesto manifesto) {
		HashSet<DoctoServico> doctoServicos = new HashSet<DoctoServico>();
		for (PreManifestoDocumento preManifestoDocumento : manifesto.getPreManifestoDocumentos()) {
			doctoServicos.add(preManifestoDocumento.getDoctoServico());
		}
		for (ManifestoNacionalCto manifestoNacionalCto : manifesto.getManifestoViagemNacional().getManifestoNacionalCtos()) {
			doctoServicos.add(manifestoNacionalCto.getConhecimento());
		}
		return doctoServicos;
	}

	private EventoPortariaDoctoServico gerarEventoDoctoServico(DoctoServico doctoServico, Filial filialDestino) {
		return gerarEventoDoctoServico(doctoServico)
				.setEvento(getEvento(doctoServico.getLocalizacaoMercadoria(), filialDestino))
				.setObComplemento(obComplemento);
	}

	private EventoPortariaDispositivoUnitizacao gerarEventoDispositivoUnitizacao(
			DispositivoUnitizacao dispositivoUnitizacao, Filial filialDestino) {
		return super.gerarEventoDispositivoUnitizacao(dispositivoUnitizacao)
				.setEvento(getEvento(null, filialDestino));
	}

	private Evento getEvento(LocalizacaoMercadoria localizacaoMercadoria, Filial filialDestino) {
		if (localizacaoMercadoria != null
				&& pendBloqLocalizacaoList.contains(localizacaoMercadoria.getIdLocalizacaoMercadoria())) {
			return cdEventoMap.get(ConstantesPortaria.CD_EVENTO_SEM_LOCALIZACAO_MERCADORIA);
		}
		if (filialSessao.equals(filialDestino)) {
			return cdEventoMap.get(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA);
		}
		return cdEventoMap.get(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL);
	}

	/**
	 * Registra atualizações para operações tipo Viagem.
	 */
	@Override
	protected void registrarAtualizacoes() {
		
		if(getParametroPortChegadaNovo()) {
			registrarAtualizacao(gerarAtualizacaoControleTrecho());
			registrarAtualizacao(gerarAtualizacaoControleCarga(getTpStatusControleCarga(), null));
			registrarAtualizacao(gerarAtualizacaoManifesto());
			registrarAtualizacaoDoctoServico();
			registrarAtualizacao(gerarAtualizacaoOcorrenciaDoctoServico(ConstantesPortaria.CD_FASE_PROCESSO_NO_TERMINAL));
			registrarAtualizacaoVolumeNotaFiscal();
			registrarAtualizacaoDispositivoUnitizacao();
		} else {
			registrarAtualizacao(gerarAtualizacaoControleTrecho());
			registrarAtualizacao(gerarAtualizacaoControleCarga(getTpStatusControleCarga(), null));
			registrarAtualizacao(gerarAtualizacaoManifesto());
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, obComplemento, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, obComplemento, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoOcorrenciaDoctoServico(ConstantesPortaria.CD_FASE_PROCESSO_NO_TERMINAL));
			registrarAtualizacaoVolumeNotaFiscal();
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, new ArrayList<Long>()));
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, new ArrayList<Long>()));
		}
		
	}

	private void registrarAtualizacaoVolumeNotaFiscal() {
		for (Manifesto manifesto : controleCarga.getManifestos()) {
			Filial filialDestino = manifesto.getFilialByIdFilialDestino();
			if (filialSessao.equals(filialDestino)) {
				registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, manifesto));
			}
			else {
				registrarAtualizacao(gerarAtualizacaoVolumeNotaFiscal(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, manifesto));
			}
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
		
		List<Long> idsDispositivoUnitizacaoEmEscalaFilial = new ArrayList<Long>(getIdsDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL));
		if(!idsDispositivoUnitizacaoEmEscalaFilial.isEmpty()){
			while(idsDispositivoUnitizacaoEmEscalaFilial.size() > 1000){
				List<Long> sublistIdsDispositivoUnitizacao = new ArrayList<Long>(idsDispositivoUnitizacaoEmEscalaFilial.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, sublistIdsDispositivoUnitizacao));
				idsDispositivoUnitizacaoEmEscalaFilial.removeAll(sublistIdsDispositivoUnitizacao);
			}
			registrarAtualizacao(gerarAtualizacaoDispositivoUnitizacao(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, idsDispositivoUnitizacaoEmEscalaFilial));
		}
	}
	
	private void registrarAtualizacaoDoctoServico(){
		List<Long> idsDoctoServicosAguardandoDescarga = new ArrayList<Long>(getIdsDoctoServicos(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA));
		if(!idsDoctoServicosAguardandoDescarga.isEmpty()){
			while(idsDoctoServicosAguardandoDescarga.size() > 1000){
				List<Long> sublistIdsDoctoServico = new ArrayList<Long>(idsDoctoServicosAguardandoDescarga.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, obComplemento, sublistIdsDoctoServico));
				idsDoctoServicosAguardandoDescarga.removeAll(sublistIdsDoctoServico);
			}
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_AGUARDANDO_DESCARGA, obComplemento, idsDoctoServicosAguardandoDescarga));
		}
		
		List<Long> idsDoctoServicosEmEscalaFilial = new ArrayList<Long>(getIdsDoctoServicos(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL));
		if(!idsDoctoServicosEmEscalaFilial.isEmpty()){
			while(idsDoctoServicosEmEscalaFilial.size() > 1000){
				List<Long> sublistIdsDoctoServico = new ArrayList<Long>(idsDoctoServicosEmEscalaFilial.subList(0, 999));
				registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, obComplemento, sublistIdsDoctoServico));
				idsDoctoServicosEmEscalaFilial.removeAll(sublistIdsDoctoServico);
			}
			registrarAtualizacao(gerarAtualizacaoDoctoServico(ConstantesPortaria.CD_EVENTO_EM_ESCALA_NA_FILIAL, obComplemento, idsDoctoServicosEmEscalaFilial));
		}
		
	}

	private Query gerarAtualizacaoControleTrecho() {
		return dao.queryUpdateControleTrecho(controleCarga, filialSessao, dhChegada);
	}

	private String getTpStatusControleCarga() {
		return filialSessao.equals(filialDestino)
				? ConstantesPortaria.TP_STATUS_AGUARDANDO_DESCARGA : ConstantesPortaria.TP_STATUS_PARADA_OPERACIONAL;
	}

	private Query gerarAtualizacaoManifesto() {
		return dao.queryUpdateManifestoViagem(controleCarga, filialSessao);
	}

}

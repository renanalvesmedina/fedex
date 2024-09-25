package com.mercurio.lms.portaria.model.processo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.evento.EventoPortaria;
import com.mercurio.lms.portaria.model.evento.EventoPortariaColeta;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDispositivoUnitizacao;
import com.mercurio.lms.portaria.model.evento.EventoPortariaDoctoServico;
import com.mercurio.lms.portaria.model.evento.EventoPortariaManifestoColeta;
import com.mercurio.lms.portaria.model.evento.EventoPortariaMeioTransporte;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;

/**
 * LMSA-768 - Classe abstrata base para processo "Informar Chegada na Portaria".
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public abstract class ChegadaPortaria {

	/**
	 * Factory method para iniciar processo para Viagem, Coleta/Entrega e Ordem
	 * de Saída.
	 * 
	 * @param service
	 *            Classe de serviços para processo.
	 * @param dao
	 *            DAO para carga, validação e processamento.
	 * @param parameters
	 *            Mapa de parâmetros gerados na action.
	 * @return Especialização de {@link ChegadaPortaria} conforme tipo de
	 *         operação.
	 */
	public static ChegadaPortaria createChegadaPortaria(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		String tpChegada = parameters.getString("tpChegada");
		if (ConstantesPortaria.TP_VIAGEM.equals(tpChegada)) {
			return ChegadaPortariaViagem.createChegadaPortaria(service, dao, parameters);
		}
		if (ConstantesPortaria.TP_COLETA_ENTREGA.equals(tpChegada)) {
			return ChegadaPortariaColetaEntrega.createChegadaPortaria(service, dao, parameters);
		}
		if (ConstantesPortaria.TP_ORDEM_SAIDA.equals(tpChegada)) {
			return ChegadaPortariaOrdemSaida.createChegadaPortaria(service, dao, parameters);
		}
		throw new IllegalStateException("Tipo de chegada inválido.");
	}

	protected NewInformarChegadaService service;
	protected NewSaidaChegadaDAO dao;

	protected TypedFlatMap parameters;

	protected Filial filialSessao;
	protected Usuario usuarioLogado;
	protected DateTime dhChegada;
	protected String tpChegada;

	protected Integer nrQuilometragem;
	protected Boolean blVirouHodometro;
	protected String obChegada;
	protected Box box;

	protected MeioTransporte transportado;
	protected MeioTransporte semiReboque;

	private List<EventoPortaria<?>> eventos = new ArrayList<EventoPortaria<?>>();
	private List<Query> atualizacoes = new ArrayList<Query>();
	private Map<VirtualTopics, List<Serializable>> mensagens = new HashMap<VirtualTopics, List<Serializable>>();

	/**
	 * Construtor genérico para processo "Informar Chegada na Portaria",
	 * procedendo os seguintes passos:
	 * <ol>
	 * <li>Processa dados da requisição;
	 * <li>Processa dados do formulário;
	 * <li>Carrega dados necessários a cada processo;
	 * <li>Valida processamento conforme regras específicas;
	 * <li>Prepara o processamento para {@link MeioTransporte}s relacionados;
	 * <li>Prepara o processamento conforme cada tipo;
	 * <li>Executa o processamento de eventos, atualizações e mensagens.
	 * </ol>
	 * 
	 * @param service
	 * @param dao
	 * @param parameters
	 */
	protected ChegadaPortaria(NewInformarChegadaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
		this.service = service;
		this.dao = dao;
		this.parameters = parameters;

		processarRequisicao();
		processarFormulario();
		carregarDados();
		if (validarInformarChegada()) {
			prepararMeioTransporte();
			prepararProcessamento();
			processarInformarChegada();
		}
	}

	/**
	 * Processa requisição considerando atributos da sessão, data/hora local e
	 * tipo de operação (Viagem, Coleta/Entrega ou Ordem de Saída).
	 */
    private void processarRequisicao() {
        filialSessao = SessionUtils.getFilialSessao();
        usuarioLogado = SessionUtils.getUsuarioLogado();
        
        dhChegada = converteStringParaDataHoraComTimezone(parameters.getString("dhChegada"));
        
        tpChegada = parameters.getString("tpChegada");

        if (dhChegada == null) {
            dhChegada = JTDateTimeUtils.getDataHoraAtual().withMillisOfSecond(0);
        }
    }
    
    /**
     * Insere a letra T, indicando timezone, entre a data e a hora e converter corretamente para DateTime com Timezone.
     * @param dataHora - Parametro no formato yyyy-MM-dd HH:mm:ss
     * @return Objeto DateTime formatado com yyyy-MM-dd'T'HH:mm:ss
     */
    private DateTime converteStringParaDataHoraComTimezone(String dataHora) {
    	String novaDataHora = dataHora.replace(" ", "T");
    	return new DateTime(novaDataHora, DateTimeZone.forID(filialSessao.getDsTimezone()));
    }

	/**
	 * Processa formulário reservando parâmetros para o processo.
	 */
	private void processarFormulario() {
		nrQuilometragem = parameters.getInteger("nrQuilometragem");
		blVirouHodometro = parameters.getBoolean("blVirouHodometro");
		obChegada = parameters.getString("obChegada");
	}

	/**
	 * Processa carga de dados comuns para todos os processos.
	 */
	protected void carregarDados() {
		Long idBox = parameters.getLong("box.idBox");
		if (idBox != null) {
			box = dao.findBox(idBox);
		}
	}

	protected abstract boolean validarInformarChegada();

	protected abstract void prepararMeioTransporte();

	protected void prepararProcessamento() {}

	/**
	 * Processa "Informar Chegada na Portaria" com os seguintes passos
	 * específicos de cada processo:
	 * <ol>
	 * <li>Verifica, produz e registra conjunto de eventos;
	 * <li>Prepara e registra atualizações necessárias;
	 * <li>Processa conjunto de eventos;
	 * <li>Processa atualizações;
	 * <li>Verifica, gera e processa mensagens JMS.
	 * </ol>
	 * 
	 */
	protected void processarInformarChegada() {
		registrarEventos();
		registrarAtualizacoes();
		processarEventos();
		processarAtualizacoes();
		processarMensagens();
	}

	/**
	 * Registra eventos para {@link MeioTransporte}s comuns a todos os
	 * processos.
	 */
	protected void registrarEventos() {
		if (transportado != null) {
			registrarEvento(gerarEventoMeioTransporte(transportado));
		}
		if (semiReboque != null) {
			registrarEvento(gerarEventoMeioTransporte(semiReboque));
		}
	}

	/**
	 * Gera {@link EventoMeioTransporte} básico, fazendo relacionamento com
	 * {@link Box}, se existir.
	 * 
	 * @param meioTransporte
	 * @return
	 */
	protected EventoPortariaMeioTransporte gerarEventoMeioTransporte(MeioTransporte meioTransporte) {
		return EventoPortariaMeioTransporte
				.createEventoPortaria(filialSessao, usuarioLogado, dhChegada, meioTransporte)
				.setBox(box);
	}

	/**
	 * Registra especialização de {@link EventoPortaria} para posterior
	 * processamento.
	 * 
	 * @param evento
	 */
	protected final void registrarEvento(EventoPortaria<?> evento) {
		CollectionUtils.addIgnoreNull(eventos, evento);
	}

	protected abstract void registrarAtualizacoes();

	/**
	 * Registra atualização ({@link Query}) para posterior execução.
	 * 
	 * @param atualizacao
	 */
	protected final void registrarAtualizacao(Query atualizacao) {
		CollectionUtils.addIgnoreNull(atualizacoes, atualizacao);
	}

	/**
	 * Verifica e, opcionalmente, registra mensagem JMS relacionada a
	 * determinado {@link EventoPortaria}, para posterior processamento.
	 * 
	 * @param evento
	 */
	protected final void registrarMensagem(EventoPortaria<?> evento) {
		VirtualTopics topic = evento.verificarMensagem();
		if (topic != null) {
			List<Serializable> list = mensagens.get(topic);
			if (list == null) {
				list = new ArrayList<Serializable>();
				mensagens.put(topic, list);
			}
			list.add(evento.gerarMensagem());
		}
	}

	/**
	 * Processa conjunto de {@link EventoPortaria}s, gerando as entidades
	 * correspondentes, procedendo a inserção na base de dados e, opcionalmente,
	 * registrando atualizações e mensagens JMS relacionadas.
	 */
	private void processarEventos() {
		List<Serializable> entidades = new ArrayList<Serializable>();
		for (EventoPortaria<?> evento : eventos) {
			entidades.add(evento.gerarEntidade());
		}
		dao.store(entidades, true);
		for (EventoPortaria<?> evento : eventos) {
			registrarAtualizacao(evento.gerarAtualizacao(dao, getParametroPortChegadaNovo()));
			registrarMensagem(evento);
		}
	}

	/**
	 * Processa conjunto de atualizações procedento a execução das atualizações
	 * ({@link Query}) na base de dados.
	 */
	private void processarAtualizacoes() {
		for (Query query : atualizacoes) {
			query.executeUpdate();
		}
	}

	/**
	 * Processa conjunto de mensagens JMS.
	 */
	private void processarMensagens() {
		for (VirtualTopics topic : mensagens.keySet()) {
			List<Serializable> list = mensagens.get(topic);
			if (list != null && !list.isEmpty()) {
				service.processarMensagens(topic, list);
			}
		}
	}
	
	protected Set<Long> getIdsDoctoServicos(short cdEvento){
		Set<Long> idsDoctosServicos = new HashSet<Long>();
		for(EventoPortaria eventoPortaria : eventos){
			if(eventoPortaria instanceof EventoPortariaDoctoServico){
				EventoPortariaDoctoServico e = (EventoPortariaDoctoServico) eventoPortaria;
				if(e.getEvento().getCdEvento().compareTo(cdEvento) == 0){
					idsDoctosServicos.add(e.getDoctoServico().getIdDoctoServico());
				}
			}
		}
		return idsDoctosServicos;
	}
	
	protected Set<Long> getIdsManifestoColetas(){
		Set<Long> idsManifestoColetas = new HashSet<Long>();
		for(EventoPortaria eventoPortaria : eventos){
			if(eventoPortaria instanceof EventoPortariaManifestoColeta){
				EventoPortariaManifestoColeta e = (EventoPortariaManifestoColeta) eventoPortaria;
				idsManifestoColetas.add(e.getManifestoColeta().getIdManifestoColeta());
			}
		}
		return idsManifestoColetas;
	}
	
	protected Set<Long> getIdsPedidoColetas(){
		Set<Long> idsPedidoColetas = new HashSet<Long>();
		for(EventoPortaria eventoPortaria : eventos){
			if(eventoPortaria instanceof EventoPortariaColeta){
				EventoPortariaColeta e = (EventoPortariaColeta) eventoPortaria;
				idsPedidoColetas.add(e.getPedidoColeta().getIdPedidoColeta());
			}
		}
		return idsPedidoColetas;
	}
	
	protected Set<Long> getIdsDispositivoUnitizacao(short cdEvento){
		Set<Long> idsDispositivoUnitizacao = new HashSet<Long>();
		for(EventoPortaria eventoPortaria : eventos){
			if(eventoPortaria instanceof EventoPortariaDispositivoUnitizacao){
				EventoPortariaDispositivoUnitizacao e = (EventoPortariaDispositivoUnitizacao) eventoPortaria;
				if(e.getEvento().getCdEvento().compareTo(cdEvento) == 0){
					idsDispositivoUnitizacao.add(e.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
				}
			}
		}
		return idsDispositivoUnitizacao;
	}
	
	protected boolean getParametroPortChegadaNovo(){
		return (Boolean) parameters.get(ConstantesPortaria.PARAMETRO_BL_PORT_CHEGADA_NOVO);
	}

}

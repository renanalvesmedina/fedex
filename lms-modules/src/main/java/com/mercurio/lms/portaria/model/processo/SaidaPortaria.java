package com.mercurio.lms.portaria.model.processo;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.util.AdsmHibernateUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.constantes.ConsGeral;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.service.NewInformarSaidaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * LMSA-1002 - Classe abstrata base para processo "Informar Sa�da na Portaria".
 *
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 */
public abstract class SaidaPortaria {

    protected NewInformarSaidaService service;
    protected NewSaidaChegadaDAO dao;

    protected TypedFlatMap parameters;

    protected Filial filialSessao;
    protected Usuario usuarioLogado;
    protected DateTime dhSaida;
    protected String tpSaida;

    protected MeioTransporte transportado;
    protected MeioTransporte semiReboque;

    private List<Serializable> entidades = new ArrayList<Serializable>();

    /**
     * Construtor gen�rico para processo "Informar Sa�da na Portaria",
     * procedendo os seguintes passos:
     * <ol>
     * <li>Processa dados da requisi��o;
     * <li>Carrega dados necess�rios ao processo;
     * <li>Valida processamento conforme regras espec�ficas;
     * <li>Prepara o processamento para {@link MeioTransporte}s relacionados;
     * <li>Prepara o processamento;
     * <li>Executa o processamento com gera��o de eventos e atualiza��es de
     * entidades.
     * </ol>
     *
     * @param service NewInformarSaidaService
     * @param dao NewSaidaChegadaDAO
     * @param parameters TypedFlatMap
     */
    protected SaidaPortaria(NewInformarSaidaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
        this.service = service;
        this.dao = dao;
        this.parameters = parameters;

        flushModeParaCommit();

        processarRequisicao();
        carregarDados();
        if (validarInformarSaida()) {
            prepararMeioTransporte();
            prepararProcessamento();
            processarInformarSaida();
            processarEntidades();
        }

        flushModeParaAuto();
    }

    /**
     * Processa requisi��o considerando atributos da sess�o, data/hora local e
     * tipo de opera��o (atualmente somente Viagem; no futuro tamb�m
     * Coleta/Entrega, Ordem de Sa�da e Visitantes).
     */
    protected void processarRequisicao() {
        filialSessao = SessionUtils.getFilialSessao();
        usuarioLogado = SessionUtils.getUsuarioLogado();
        dhSaida = JTDateTimeUtils.getDataHoraAtual().withMillisOfSecond(0);
        tpSaida = parameters.getString("tpSaida");
    }

    protected abstract void carregarDados();

    protected abstract boolean validarInformarSaida();

    protected abstract void prepararMeioTransporte();

    protected abstract void prepararProcessamento();

    protected abstract void processarInformarSaida();

    /**
     * Registra {@link List} de {@link Serializable}s para posterior
     * persist�ncia ou atualiza��o.
     *
     * @param entidades List<? extends Serializable>
     */
    protected final void registrarEntidades(List<? extends Serializable> entidades) {
        if (entidades != null) {
            for (Serializable entidade : entidades) {
                registrarEntidade((Serializable) entidade);
            }
        }
    }

    /**
     * Registra inst�ncia de {@link Serializable} para posterior persist�ncia ou
     * atualiza��o.
     *
     * @param entidade Serializable
     */
    protected final void registrarEntidade(Serializable entidade) {
        CollectionUtils.addIgnoreNull(entidades, entidade);
    }

    /**
     * Processa conjunto de {@link Serializable}s, persistindo/atualizando
     * entidades e, opcionalmente, gerando eventos de rastreabilidade.
     */
    protected void processarEntidades() {
        dao.getAdsmHibernateTemplate().clear();
        processarEntidadesSqlNativo();
    }

    /**
     * A fun��o ser� usada para percorrer o array de entidades e preparar os dados para ser inseridos em lote. A fun��o
     * foi feita dessa maneira pois o hibernate, no estado como foi configurada a aplica��o, n�o suporta inserts em lote.
     */
    private void processarEntidadesSqlNativo() {

        List<Serializable> entidadesASeremInseridas = (ArrayList<Serializable>) ((ArrayList<Serializable>) entidades).clone();

        dao.processarEntidadesASeremInseridasSaidaPortaria(entidadesASeremInseridas, entidades);
        dao.store(entidadesASeremInseridas);

        dao.getAdsmHibernateTemplate().flush();
        dao.getAdsmHibernateTemplate().clear();

        AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations = new AdsmNativeBatchSqlOperations(service.getNewSaidaChegadaDAO(), ConsGeral.LIMITE_OPERACAO);

        for (Serializable entidade : entidades) {
            if (entidade instanceof EventoDocumentoServico) {
                service.processarEventosRastreabilidade((EventoDocumentoServico) entidade, adsmNativeBatchSqlOperations);
            }
        }

        adsmNativeBatchSqlOperations.runAllCommands();

    }

    protected abstract EventoDocumentoServicoDMN criarEventoRastreabilidade(Serializable entidade);

    /**
     * Factory method para iniciar processo para Viagem.
     *
     * @param service    Classe de servi�os para processo.
     * @param dao        DAO para carga, valida��o e processamento.
     * @param parameters Mapa de par�metros gerados na action.
     * @return Inst�ncia de {@link ChegadaPortariaViagem}.
     */
    public static SaidaPortaria createSaidaPortaria(NewInformarSaidaService service, NewSaidaChegadaDAO dao, TypedFlatMap parameters) {
        String tpSaida = parameters.getString("tpSaida");
        if (ConstantesPortaria.TP_VIAGEM.equals(tpSaida)) {
            return SaidaPortariaViagem.createSaidaPortaria(service, dao, parameters);
        }
        throw new UnsupportedOperationException("Implementa��o dispon�vel somente para opera��o tipo Viagem.");
    }


    public void flushModeParaCommit() {
        AdsmHibernateUtils.setFlushModeToCommit(this.dao.getAdsmHibernateTemplate());
    }

    public void flushModeParaAuto() {
        AdsmHibernateUtils.setFlushModeToAuto(this.dao.getAdsmHibernateTemplate());
    }
}

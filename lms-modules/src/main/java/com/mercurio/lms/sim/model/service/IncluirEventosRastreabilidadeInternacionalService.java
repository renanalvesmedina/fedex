package com.mercurio.lms.sim.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.ListUtilsPlus;
import com.mercurio.lms.constantes.entidades.ConsDoctoServico;
import com.mercurio.lms.endtoend.model.service.IntegracaoEventoDocumentoServicoService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsComBatchService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.PedidoCompra;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

/**
 * @spring.bean id="lms.municipios.incluirEventosRastreabilidadeInternacionalService"
 */
public class IncluirEventosRastreabilidadeInternacionalService {

    private static final String ID_FILIAL = "idFilial";
    private static final String NR_DOCUMENTO = "nrDocumento";
    private static final String DH_EVENTO = "dhEvento";
    private static final String ID_PEDIDO_COMPRA = "idPedidoCompra";
    private static final String DS_OBSERVACAO = "dsObservacao";
    private static final String TP_DOCUMENTO = "tpDocumento";
    private static final String ID_OCORRENCIA_ENTREGA = "idOcorrenciaEntrega";
    private static final String ID_OCORRENCIA_PENDENCIA = "idOcorrenciaPendencia";
    private static final String EVENTO_CANCELADO = "eventoCancelado";
    private static final String ARMAZENA_DADOS_DOCTO = "armazenaDadosDocto";

    private static final String ID_FILIAL_LOCALIZACAO = "ID_FILIAL_LOCALIZACAO";
    private static final String ID_LOCALIZACAO_MERCADORIA = "ID_LOCALIZACAO_MERCADORIA";
    private static final String NR_DIAS_REAL_ENTREGA = "NR_DIAS_REAL_ENTREGA";
    private static final String OB_COMPLEMENTO_LOCALIZACAO = "OB_COMPLEMENTO_LOCALIZACAO";

    private ControleCargaService controleCargaService;
    private EventoService eventoService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private ManifestoViagemNacionalService manifestoViagemNacionalService;
    private ManifestoInternacionalService manifestoInternacionalService;
    private ManifestoEntregaService manifestoEntregaService;
    private AwbService awbService;
    private PedidoCompraService pedidoCompraService;
    private ParametroGeralService parametroGeralService;
    private ConhecimentoService conhecimentoService;
    private CtoInternacionalService ctoInternacionalService;
    private MdaService mdaService;
    private DoctoServicoService doctoServicoService;
    private ReciboReembolsoService reciboReembolsoService;
    private EventoVolumeService eventoVolumeService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private HistoricoFilialService historicoFilialService;
    private IntegracaoJmsService integracaoJmsService;
    private IntegracaoJmsComBatchService integracaoJmsComBatchService;
    private IntegracaoEventoDocumentoServicoService integracaoEventoDocumentoServicoService;
    
    // LMSA-6607
    private ConfiguracoesFacade configuracoesFacade;
    private ManifestoService manifestoService;

    /**
     * Atenção! método que valida se existem documentos entregues antes de incluir os eventos
     *
     * @param tpDocumento
     * @param nrDocumento
     * @param cdEvento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    public void executeInsereEventos(String tpDocumento, String nrDocumento,
                                     Short cdEvento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        // Valida se a data/hora do evento for futura, lança Exception.
        if (dhEvento == null) {
            throw new IllegalArgumentException("O argumento dhEvento é obrigatório.");
        }
        if (dhEvento.compareTo(JTDateTimeUtils.getDataHoraAtual()) > 0) {
            throw new BusinessException("LMS-10059");
        }
        validateDocumentoEntregue(nrDocumento, idFilial, tpDocumento);
        this.insereEventos(tpDocumento, nrDocumento, cdEvento, idFilial, dhEvento, dsObservacao, null);
    }

    public void executeInsereEventosSemValidarEntregaDoDocumento(String tpDocumento, String nrDocumento,
                                                                 Short cdEvento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        // Valida se a data/hora do evento for futura, lança Exception.
        if (dhEvento == null) {
            throw new IllegalArgumentException("O argumento dhEvento é obrigatório.");
        }
        if (dhEvento.compareTo(JTDateTimeUtils.getDataHoraAtual()) > 0) {
            throw new BusinessException("LMS-10059");
        }
        this.insereEventos(tpDocumento, nrDocumento, cdEvento, idFilial, dhEvento, dsObservacao, null);
    }

    public void generateEventosPED(Long idPedido, String nrDocumento, Short cdEvento, DateTime dhEvento, String dsObservacao) {
        Evento evento = eventoService.findByCdEvento(cdEvento);
        insereEventosPED(evento, idPedido, nrDocumento, dhEvento, dsObservacao);
    }

    public void generateEventosPEI(Long idPedido, String nrDocumento, Short cdEvento, DateTime dhEvento, String dsObservacao) {
        Evento evento = eventoService.findByCdEvento(cdEvento);
        insereEventosPEI(evento, idPedido, nrDocumento, dhEvento, dsObservacao);
    }

    /**
     * Recebe a informacao de algum documento e evento e o insere para todos os documentos de servico associaados ao documento recebido
     *
     * @param tpDocumento
     * @param nrDocumento
     * @param cdEvento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    public void insereEventos(String tpDocumento, String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento, String dsObservacao, Long idCiaFilialMercurio) {
        Evento evento = eventoService.findByCdEvento(cdEvento);
        /* Regra 01 */
        if (tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_CONTROLE_CARGA)) {
            insereEventosCCA(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 02 */
        } else if (tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_MANIFESTO_VIAGEM)) {
            insereEventosMVA(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 03 */
        } else if (tpDocumento.equals(ConstantesExpedicao.MANIFESTO_ITERNACIONAL_CARGA)) {
            insereEventosMIC(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 04 */
        } else if (tpDocumento.equals(ConstantesEventosDocumentoServico.TP_DOCTO_MID)) {
            insereEventosMID(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 05 */
        } else if (tpDocumento.equals(ConstantesEntrega.MANIFESTO_ENTREGA)) {
            insereEventosMAE(evento, nrDocumento, idFilial, dhEvento, dsObservacao, ConstantesEntrega.MANIFESTO_ENTREGA);
        /* Regra 06 */
        } else if (tpDocumento.equals(ConstantesExpedicao.AIRWAY_BILL)) {
            insereEventosAWB(evento, nrDocumento, idFilial, dhEvento, dsObservacao, ConstantesExpedicao.AIRWAY_BILL, idCiaFilialMercurio);
        /* Regra 10 */
        } else if (tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)) {
            insereEventosCTR(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 11 */
        } else if (tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL)) {
            insereEventosCRT(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 12 */
        } else if (tpDocumento.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE)) {
            insereEventosNFT(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 14 */
        } else if (tpDocumento.equals(ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO)) {
            insereEventosMDA(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        /* Regra 15 */
        } else if (tpDocumento.equals(ConstantesExpedicao.RECIBO_REEMBOLSO)) {
            insereEventosRRE(evento, nrDocumento, idFilial, dhEvento, dsObservacao);
        }
    }

    /**
     * Regra 01. Lanca eventos para documentos do tipo CCA - Controle de Carga
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void insereEventosCCA(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List controleCargas = controleCargaService.findControleCargaByNrControleByFilial(parseNrDocumentoLong(nrDocumento), idFilial);
        if (controleCargas.isEmpty()) {
            throw new BusinessException("LMS-10001");
        }

        ControleCarga controleCarga = (ControleCarga) controleCargas.get(0);
        Long idControleCarga = controleCarga.getIdControleCarga();

        List documentos = new ArrayList(100);
        List documentosCCNacional = controleCargaService.findDocumentosControleCargaNacional(idControleCarga);
        if (documentosCCNacional != null) {
            documentos.addAll(documentosCCNacional);
        }
        List documentosCCInternacional = controleCargaService.findDocumentosControleCargaInternacional(idControleCarga);
        if (documentosCCInternacional != null) {
            documentos.addAll(documentosCCInternacional);
        }
        List documentosCCEntrega = controleCargaService.findDocumentosControleCargaEntrega(idControleCarga);
        if (documentosCCEntrega != null) {
            documentos.addAll(documentosCCEntrega);
        }
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesEventosDocumentoServico.TP_DOCTO_CONTROLE_CARGA,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 02. Lanca eventos para documentos tipo MVA - Manifesto Viagem Nacional
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosMVA(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List manifestos = manifestoViagemNacionalService.findByNrManifestoOrigemByFilial(
                Integer.valueOf(filterNumber(nrDocumento)),
                idFilial
        );

        if (manifestos.isEmpty()) {
            throw new BusinessException("LMS-10002");
        }

        ManifestoViagemNacional mvn = (ManifestoViagemNacional) manifestos.get(0);

        List<ManifestoNacionalCto> documentos = mvn.getManifestoNacionalCtos();
        validateExisteDocumentos(documentos);

        for (ManifestoNacionalCto manifestoNacionalCto : documentos) {
            Long idDocumentoServico = manifestoNacionalCto.getConhecimento().getIdDoctoServico();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesEventosDocumentoServico.TP_DOCTO_MANIFESTO_VIAGEM,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 03. Lanca eventos para documentos do tipo MIC - Manifesto Internacional
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosMIC(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        String tpMic = "M";
        List manifestos = manifestoInternacionalService.findByNrManifestoByFilialByTpMic(parseNrDocumentoLong(nrDocumento), idFilial, tpMic);
        if (manifestos.isEmpty()) {
            throw new BusinessException("LMS-10003");
        }

        ManifestoInternacional mi = (ManifestoInternacional) manifestos.get(0);
        Long idManifestoInternacional = mi.getIdManifestoInternacional();

        List documentos = manifestoInternacionalService.findDocumentosServico(idManifestoInternacional, tpMic);
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesExpedicao.MANIFESTO_ITERNACIONAL_CARGA,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 04. Lanca eventos para documentos do tipo MID - Manifesto Internacional
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosMID(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        String tpMic = "D";
        List manifestos = manifestoInternacionalService.findByNrManifestoByFilialByTpMic(parseNrDocumentoLong(nrDocumento), idFilial, tpMic);
        if (manifestos.isEmpty()) {
            throw new BusinessException("LMS-10004");
        }

        ManifestoInternacional mi = (ManifestoInternacional) manifestos.get(0);
        Long idManifestoInternacional = mi.getIdManifestoInternacional();

        List documentos = manifestoInternacionalService.findDocumentosServico(idManifestoInternacional, tpMic);
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesEventosDocumentoServico.TP_DOCTO_MID,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 05. Lanca eventos para documentos do tipo MAE - Manifesto de Entrega
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     * @param tpDocumento
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosMAE(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao, String tpDocumento) {
        List manifestos = manifestoEntregaService.findByNrManifestoByFilial(
                Integer.valueOf(filterNumber(nrDocumento)),
                idFilial
        );

        if (manifestos.isEmpty()) {
            throw new BusinessException("LMS-10005");
        }

        ManifestoEntrega me = (ManifestoEntrega) manifestos.get(0);
        Long idManifestoEntrega = me.getIdManifestoEntrega();

        List documentos = manifestoEntregaService.findDocumentosServico(idManifestoEntrega);
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    tpDocumento,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 06. Lanca eventos para documentos do tipo AWB -
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     * @param tpDocumento
     * @param idCiaFilialMercurio
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosAWB(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao, String tpDocumento, Long idCiaFilialMercurio) {
        List result = awbService.findByNrAwbByCiaAerea(Long.parseLong(filterNumber(nrDocumento)), idCiaFilialMercurio, idFilial, null);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10006");
        }

        Long idAwb = ((TypedFlatMap) result.get(0)).getLong("idAwb");

        List documentos = awbService.findDocumentosServico(idAwb);
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    tpDocumento,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 08. Lanca eventos para documentos do tipo PED - Pedido de compra
     *
     * @param evento
     * @param idPedido
     * @param nrDocumento
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosPED(Evento evento, Long idPedido, String nrDocumento, DateTime dhEvento, String dsObservacao) {
        BigDecimal codTipoAnexoFatura = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("COD_TIPO_ANEXO_FATURA", false);

        List documentos = pedidoCompraService.findDocumentosServico(idPedido, Long.valueOf(codTipoAnexoFatura.longValue()));
        validateExisteDocumentos(documentos);
        if (documentos.size() > 1) {
            throw new BusinessException("LMS-10054");
        }

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Object[] retorno = (Object[]) it.next();
            Long idDocumentoServico = (Long) retorno[0];
            Long idFilial = (Long) retorno[1];
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    idPedido,
                    dsObservacao,
                    ConstantesEventosDocumentoServico.TP_DOCTO_PED_NACIONAL,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 09. Lanca eventos para documentos do tipo PEI - Pedido de compra internacional
     *
     * @param evento
     * @param idPedido
     * @param nrDocumento
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosPEI(Evento evento, Long idPedido, String nrDocumento, DateTime dhEvento, String dsObservacao) {
        BigDecimal codTipoAnexoFatura = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("COD_TIPO_ANEXO_FATURA", false);

        List documentos = pedidoCompraService.findDocumentosServico(idPedido, Long.valueOf(codTipoAnexoFatura.longValue()));
        validateExisteDocumentos(documentos);
        if (documentos.size() > 1) {
            throw new BusinessException("LMS-10054");
        }

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Object[] retorno = (Object[]) it.next();
            Long idDocumentoServico = (Long) retorno[0];
            Long idFilial = (Long) retorno[1];
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    idPedido,
                    dsObservacao,
                    ConstantesEventosDocumentoServico.TP_DOCTO_PEI_INTERNACIONAL,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 10. Lanca eventos para documentos do tipo CTO - Conhecimento
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosCTR(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List documentos = getDocumentosCTR(nrDocumento, idFilial);
        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesExpedicao.CONHECIMENTO_NACIONAL,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Retorna o documento do conhecimento
     */
    @SuppressWarnings("rawtypes")
    private List getDocumentosCTR(String nrDocumento, Long idFilial) {
        String tpDocumentoServico = ConstantesExpedicao.CONHECIMENTO_NACIONAL;

        List result = conhecimentoService.findByNrConhecimentoByFilial(parseNrDocumentoLong(nrDocumento), idFilial, tpDocumentoServico);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10009");
        }

        Conhecimento conhecimento = (Conhecimento) result.get(0);
        Long id = conhecimento.getIdDoctoServico();

        List documentos = conhecimentoService.findDocumentosServico(id, tpDocumentoServico);
        validateExisteDocumentos(documentos);

        return documentos;
    }

    /**
     * Regra 11. Lanca eventos para documentos do tipo CRT - Conhecimento Internacional
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosCRT(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List documentos = getDocumentosCRT(nrDocumento, idFilial);
        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Retorna o documento do conhecimento
     */
    @SuppressWarnings("rawtypes")
    private List getDocumentosCRT(String nrDocumento, Long idFilial) {
        List result = ctoInternacionalService.findByNrCrtByFilial(parseNrDocumentoLong(nrDocumento), idFilial);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10010");
        }

        CtoInternacional cto = (CtoInternacional) result.get(0);
        Long id = cto.getIdDoctoServico();

        List documentos = ctoInternacionalService.findDocumentosServico(id);
        validateExisteDocumentos(documentos);
        return documentos;
    }

    /**
     * Regra 12. Lanca eventos para documentos do tipo NFT - Nota fiscal de transporte
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosNFT(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List documentos = getDocumentosNFT(nrDocumento, idFilial);
        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Retorna o documento do conhecimento
     */
    @SuppressWarnings("rawtypes")
    private List getDocumentosNFT(String nrDocumento, Long idFilial) {
        String tpDocumentoServico = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;

        List result = conhecimentoService.findByNrConhecimentoByFilial(parseNrDocumentoLong(nrDocumento), idFilial, tpDocumentoServico);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10012");
        }

        Conhecimento conhecimento = (Conhecimento) result.get(0);
        Long id = conhecimento.getIdDoctoServico();

        List documentos = conhecimentoService.findDocumentosServico(id, tpDocumentoServico);
        validateExisteDocumentos(documentos);

        return documentos;
    }

    /**
     * Regra 14. Lanca eventos para documentos do tipo MDA
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosMDA(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List result = mdaService.findByNrMdaByFilialOrigem(parseNrDocumentoLong(nrDocumento), idFilial);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10015");
        }

        Mda mda = (Mda) result.get(0);
        Long id = mda.getIdDoctoServico();

        List documentos = mdaService.findDocumentosServico(id);
        validateExisteDocumentos(documentos);

        for (Iterator it = documentos.iterator(); it.hasNext(); ) {
            Long idDocumentoServico = (Long) it.next();
            generateEventoDocumento(
                    evento,
                    idDocumentoServico,
                    idFilial,
                    nrDocumento,
                    dhEvento,
                    null,
                    dsObservacao,
                    ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO,
                    null,
                    null,
                    null,
                    SessionUtils.getUsuarioLogado()
            );
        }
    }

    /**
     * Regra 15. Lanca eventos para documentos do tipo RRE
     *
     * @param evento
     * @param nrDocumento
     * @param idFilial
     * @param dhEvento
     * @param dsObservacao
     */
    @SuppressWarnings("rawtypes")
    private void insereEventosRRE(Evento evento, String nrDocumento, Long idFilial, DateTime dhEvento, String dsObservacao) {
        List result = reciboReembolsoService.findByNrReciboReembolsoByFilialOrigem(parseNrDocumentoLong(nrDocumento), idFilial);
        if (result.isEmpty()) {
            throw new BusinessException("LMS-10037");
        }

        ReciboReembolso reciboReembolso = (ReciboReembolso) result.get(0);
        Long id = reciboReembolso.getIdDoctoServico();

        List<ReciboReembolso> documentos = reciboReembolsoService.findDocumentosServico(id);
        validateExisteDocumentos(documentos);
        
        for (ReciboReembolso reciboReembolso2 : documentos) {
        	 generateEventoDocumento(
                     evento,
                     reciboReembolso2.getIdDoctoServico(),
                     idFilial,
                     nrDocumento,
                     dhEvento,
                     null,
                     dsObservacao,
                     ConstantesExpedicao.RECIBO_REEMBOLSO,
                     null,
                     null,
                     null,
                     SessionUtils.getUsuarioLogado()
             );
		}

    }

    /**
     * Metodo que gera eventos do documento
     *
     * @param cdEvento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param tpDocumento
     */
    public void generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            String tpDocumento
    ) {
        generateEventoDocumento(
                cdEvento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                null,
                null,
                tpDocumento
        );
    }

    /**
     * Metodo que gera eventos do documento
     *
     * @param cdEvento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param idPedidoCompra
     * @param dsObservacao
     * @param tpDocumento
     */
    public void generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento
    ) {
        generateEventoDocumento(
                cdEvento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                Boolean.FALSE
        );
    }

    /**
     * Metodo que gera eventos do documento retornando Boolean
     *
     * @param cdEvento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param idPedidoCompra
     * @param dsObservacao
     * @param tpDocumento
     * @param withReturn
     */
    public Boolean generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Boolean withReturn
    ) {

        return generateEventoDocumento(cdEvento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                withReturn,
                Boolean.FALSE);
    }

    public Boolean generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Boolean withReturn,
            Boolean blExecutarVerificacaoDocumentoManifestado
    ) {

        Evento evento = findEventoRecebido(cdEvento);
        return gerarEventoDocumento(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                null,
                null,
                withReturn,
                blExecutarVerificacaoDocumentoManifestado,
                null,
                SessionUtils.getUsuarioLogado(),
                null
        );
    }

    public Boolean generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Boolean withReturn,
            Boolean blExecutarVerificacaoDocumentoManifestado,
            Long idFilialLocalizacao,
            Usuario usuario
    ) {

        Evento evento = findEventoRecebido(cdEvento);
        return gerarEventoDocumento(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                null,
                null,
                withReturn,
                blExecutarVerificacaoDocumentoManifestado,
                idFilialLocalizacao,
                usuario,
                null
        );
    }

    /**
     * Metodo que gera eventos do documento com ocorrencias de entrega e pendencia
     *
     * @param cdEvento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param idPedidoCompra
     * @param dsObservacao
     * @param tpDocumento
     * @param idOcorrenciaEntrega
     * @param idOcorrenciaPendencia
     */
    public void generateEventoDocumento(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Long idOcorrenciaEntrega,
            Long idOcorrenciaPendencia,
            Boolean blOcorrenciaDocumentoManual,
            Usuario usuarioLogado
    ) {
        Evento evento = findEventoRecebido(cdEvento);
        generateEventoDocumento(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                idOcorrenciaEntrega,
                idOcorrenciaPendencia,
                blOcorrenciaDocumentoManual,
                usuarioLogado
        );
    }

    public void generateEventoDocumentoComBatch(
            Long idDocumentoServico,
            Map<String, Object> valoresEventoMap,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations
    ) {
        Evento evento = findEventoRecebido((Short) valoresEventoMap.get("cdEvento"));

        generateEventoDocumentoComBatch(
                evento,
                idDocumentoServico,
                (Long) valoresEventoMap.get(ID_FILIAL),
                (String) valoresEventoMap.get(NR_DOCUMENTO),
                (DateTime) valoresEventoMap.get(DH_EVENTO),
                (valoresEventoMap.get(ID_PEDIDO_COMPRA) != null) ? (Long) valoresEventoMap.get(ID_PEDIDO_COMPRA) : null,
                (valoresEventoMap.get(DS_OBSERVACAO) != null) ? (String) valoresEventoMap.get(DS_OBSERVACAO) : null,
                (String) valoresEventoMap.get(TP_DOCUMENTO),
                (valoresEventoMap.get(ID_OCORRENCIA_ENTREGA) != null) ? (Long) valoresEventoMap.get(ID_OCORRENCIA_ENTREGA) : null,
                (valoresEventoMap.get(ID_OCORRENCIA_PENDENCIA) != null) ? (Long) valoresEventoMap.get(ID_OCORRENCIA_PENDENCIA) : null,
                adsmNativeBatchSqlOperations
        );
    }

    public void generateEventoDocumentoComBatch(
            DoctoServico doctoServico,
            Map<String, Object> valoresEventoMap,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations
    ) {
        Evento evento = findEventoRecebido((Short) valoresEventoMap.get("cdEvento"));

        Filial filial = null;
        if (valoresEventoMap.get(ID_FILIAL) != null && valoresEventoMap.get(ID_FILIAL) instanceof Long) {
            filial = new Filial();
            filial.setIdFilial((Long) valoresEventoMap.get(ID_FILIAL));
        }

        Boolean armazenaDadosDocto = (valoresEventoMap.get(ARMAZENA_DADOS_DOCTO) != null) ? (Boolean) valoresEventoMap.get(ARMAZENA_DADOS_DOCTO) : true;

        gerarEventoDocumentoComBatch(
                evento,
                doctoServico,
                filial,
                (String) valoresEventoMap.get(NR_DOCUMENTO),
                (DateTime) valoresEventoMap.get(DH_EVENTO),
                (valoresEventoMap.get(ID_PEDIDO_COMPRA) != null) ? (Long) valoresEventoMap.get(ID_PEDIDO_COMPRA) : null,
                (valoresEventoMap.get(DS_OBSERVACAO) != null) ? (String) valoresEventoMap.get(DS_OBSERVACAO) : null,
                (String) valoresEventoMap.get(TP_DOCUMENTO),
                (valoresEventoMap.get(ID_OCORRENCIA_ENTREGA) != null) ? (Long) valoresEventoMap.get(ID_OCORRENCIA_ENTREGA) : null,
                (valoresEventoMap.get(ID_OCORRENCIA_PENDENCIA) != null) ? (Long) valoresEventoMap.get(ID_OCORRENCIA_PENDENCIA) : null,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                SessionUtils.getUsuarioLogado(),
                armazenaDadosDocto,
                adsmNativeBatchSqlOperations,
                false
        );
    }

    public void generateEventoDocumentoComBatch(
            Short cdEvento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            String tpDocumento,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations
    ) {
        Evento evento = findEventoRecebido(cdEvento);
        generateEventoDocumentoComBatch(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                null,
                null,
                tpDocumento,
                null,
                null,
                adsmNativeBatchSqlOperations
        );
    }

    /**
     * Gera um evento para o documento de servico recebido
     *
     * @param evento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param idPedidoCompra
     * @param dsObservacao
     * @param tpDocumento
     * @param idOcorrenciaEntrega
     * @param idOcorrenciaPendencia
     */
    public void generateEventoDocumento(
            Evento evento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Long idOcorrenciaEntrega,
            Long idOcorrenciaPendencia,
            Boolean blOcorrenciaDocumentoManual,
            Usuario usuarioLogado
    ) {//
        gerarEventoDocumento(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                idOcorrenciaEntrega,
                idOcorrenciaPendencia,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                usuarioLogado,
                blOcorrenciaDocumentoManual
        );
    }

    public Boolean gerarEventoDocumento(Evento evento, DoctoServico doctoServico, Filial filial, String nrDocumento,
                                        DateTime dhEvento, String dsObservacao, String tpDocumento) {
        return gerarEventoDocumento(
                evento,
                doctoServico,
                filial,
                nrDocumento,
                dhEvento,
                null,
                dsObservacao,
                tpDocumento,
                null,
                null,
                null,
                null,
                null,
                SessionUtils.getUsuarioLogado(),
                null);
    }

    public Boolean gerarEventoDocumento(Evento evento, DoctoServico doctoServico, Filial filial, String nrDocumento,
                                        DateTime dhEvento, String dsObservacao, String tpDocumento, Long idOcorrenviaEntrega,
                                        Usuario usuario) {
        return gerarEventoDocumento(
                evento,
                doctoServico,
                filial,
                nrDocumento,
                dhEvento,
                null,
                dsObservacao,
                tpDocumento,
                idOcorrenviaEntrega,
                null,
                null,
                null,
                null,
                usuario,
                null);
    }

    /**
     * Gera um evento para o documento de servico recebido retornando Boolean
     *
     * @param evento
     * @param idDocumentoServico
     * @param idFilial
     * @param nrDocumento
     * @param dhEvento
     * @param idPedidoCompra
     * @param dsObservacao
     * @param tpDocumento
     * @param idOcorrenciaEntrega
     * @param idOcorrenciaPendencia
     * @param withReturn
     * @param blExecutarVerificacaoDocumentoManifestado
     * @param idFilialLocalizacao
     * @param usuario
     * @return
     */
    private Boolean gerarEventoDocumento(Evento evento, Long idDocumentoServico, Long idFilial, String nrDocumento,
                                         DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento, Long idOcorrenciaEntrega,
                                         Long idOcorrenciaPendencia, Boolean withReturn, Boolean blExecutarVerificacaoDocumentoManifestado,
                                         Long idFilialLocalizacao, Usuario usuario, Boolean blOcorrenciaDocumentoManual) {

        DoctoServico doctoServico = doctoServicoService.findById(idDocumentoServico);
        Filial filial = null;
        if (idFilial != null) {
            filial = new Filial();
            filial.setIdFilial(idFilial);
        }

        return gerarEventoDocumento(evento, doctoServico, filial, nrDocumento, dhEvento, idPedidoCompra, dsObservacao,
                tpDocumento, idOcorrenciaEntrega, idOcorrenciaPendencia, withReturn,
                blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, usuario, blOcorrenciaDocumentoManual);
    }


    @SuppressWarnings("rawtypes")
    public Boolean gerarEventoDocumento(Evento evento, DoctoServico doctoServico, Filial filial, String nrDocumento,
                                        DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento, Long idOcorrenciaEntrega,
                                        Long idOcorrenciaPendencia, Boolean withReturn, Boolean blExecutarVerificacaoDocumentoManifestado,
                                        Long idFilialLocalizacao, Usuario usuario, Boolean blOcorrenciaDocumentoManual) {

        Long idDocumentoServico = doctoServico.getIdDoctoServico();

		/* Verificação da regra de cancelamento de evento */
        if (evento != null && evento.getCancelaEvento() != null) {
            List eventosDoctoServico = eventoDocumentoServicoService
                    .findByEventoByDocumentoServico(evento.getCancelaEvento().getIdEvento(), idDocumentoServico);
            if (!eventosDoctoServico.isEmpty()) {
                EventoDocumentoServico eventoDoctoServico = (EventoDocumentoServico) eventosDoctoServico.get(0);
                eventoDoctoServico.setBlEventoCancelado(Boolean.TRUE);
                eventoDocumentoServicoService.store(eventoDoctoServico);

                sendEventoDoctoServicoJMS(eventoDoctoServico, blOcorrenciaDocumentoManual);
            } else {
                if (withReturn.booleanValue()) {
                    return Boolean.FALSE;
                } else {
                    String dsEvento = evento.getCdEvento() + " - "
                            + evento.getDescricaoEvento().getDsDescricaoEvento().getValue();
                    throw new BusinessException("LMS-10014", new String[]{dsEvento});
                }
            }
        }

        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
        if (dhEvento == null) {
            dhEvento = dhAtual;
        }

        EventoDocumentoServico eventoDctoServico = gerarEventoDocumento(evento, doctoServico, filial, nrDocumento,
                dhEvento, idPedidoCompra, dsObservacao, tpDocumento, idOcorrenciaEntrega, idOcorrenciaPendencia,
                usuario, dhAtual);

        atualizaLocalizacaoMercadoria(evento, doctoServico, dhEvento, dsObservacao,
                blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao);

        // Salva documentoServico
        if (doctoServico.getLocalizacaoMercadoria() != null) {
            doctoServicoService.store(doctoServico);
        }

        // Salva eventoDocumentoServico
        eventoDocumentoServicoService.store(eventoDctoServico);

        // Adiciona o evento na fila JMS
        sendEventoDoctoServicoJMS(eventoDctoServico, blOcorrenciaDocumentoManual);
        
        sendEventoDoctoServicoJMSIBM(eventoDctoServico);

        return Boolean.TRUE;
    }

    public EventoDocumentoServico gerarEventoDocumento(Evento evento, DoctoServico doctoServico, Filial filial,
                                                       String nrDocumento, DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento,
                                                       Long idOcorrenciaEntrega, Long idOcorrenciaPendencia, Usuario usuario, DateTime dhAtual) {
        EventoDocumentoServico eventoDctoServico = new EventoDocumentoServico();

        eventoDctoServico.setEvento(evento);

        eventoDctoServico.setBlEventoCancelado(Boolean.FALSE);
        eventoDctoServico.setDhEvento(dhEvento);
        eventoDctoServico.setDhInclusao(dhAtual);
        eventoDctoServico.setObComplemento(dsObservacao);
        eventoDctoServico.setNrDocumento(nrDocumento);
        if (tpDocumento != null) {
            eventoDctoServico.setTpDocumento(new DomainValue(tpDocumento));
        }

        eventoDctoServico.setUsuario(usuario);
        eventoDctoServico.setDoctoServico(doctoServico);

        if (filial != null) {
            eventoDctoServico.setFilial(filial);
        }

        if (idPedidoCompra != null) {
            PedidoCompra pedidoCompra = new PedidoCompra();
            pedidoCompra.setIdPedidoCompra(idPedidoCompra);
            eventoDctoServico.setPedidoCompra(pedidoCompra);
        }

		/*
		 * Se o evento possuir ocorrencia de entrega, atualiza o campo
		 * ocorrencia de entrega
		 */
        if (idOcorrenciaEntrega != null) {
            OcorrenciaEntrega ocorrenciaEntrega = new OcorrenciaEntrega();
            ocorrenciaEntrega.setIdOcorrenciaEntrega(idOcorrenciaEntrega);
            eventoDctoServico.setOcorrenciaEntrega(ocorrenciaEntrega);
        }

		/*
		 * Se o evento possuir ocorrencia de pendencia, atualiza o campo
		 * ocorrencia de pendencia
		 */
        if (idOcorrenciaPendencia != null) {
            OcorrenciaPendencia ocorrenciaPendencia = new OcorrenciaPendencia();
            ocorrenciaPendencia.setIdOcorrenciaPendencia(idOcorrenciaPendencia);
            eventoDctoServico.setOcorrenciaPendencia(ocorrenciaPendencia);
        }
        return eventoDctoServico;
    }

    @SuppressWarnings({ "rawtypes", "deprecation" })
    public void atualizaLocalizacaoMercadoria(Evento evento, DoctoServico doctoServico, DateTime dhEvento,
            String dsObservacao, Boolean blExecutarVerificacaoDocumentoManifestado, Long idFilialLocalizacao) {
		/*
		 * Se o evento possuir localização de mercadoria, atualiza o documento
		 * de serviço
		 */
        if (evento.getLocalizacaoMercadoria() != null && evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() != null) {

			/*
			 * LMS-4054: Ao emitir um documento de serviço, que já está
			 * manifestado, a localização não deve ser alterada.
			 */
            if (Boolean.TRUE.equals(blExecutarVerificacaoDocumentoManifestado)) {
                if (doctoServico.getPreManifestoDocumentos() == null || doctoServico.getPreManifestoDocumentos().isEmpty()) {
                    doctoServico.setLocalizacaoMercadoria(evento.getLocalizacaoMercadoria());
                }
            } else {
                doctoServico.setLocalizacaoMercadoria(evento.getLocalizacaoMercadoria());
            }

            /**
             * Lms-606 Se a filial do usuário logado for (MTZ) matriz
             * OB_COMPLEMENTO_LOCALIZACAO recebe NULL Se não
             * OB_COMPLEMENTO_LOCALIZACAO recebe Sigla Filial + Filial (Ex: FLN
             * Florianópolis)
             */
            if (!SessionUtils.isFilialSessaoMatriz()) {
                doctoServico.setFilialLocalizacao(idFilialLocalizacao == null ? SessionUtils.getFilialSessao()
                        : this.generateNewFilial(idFilialLocalizacao));
                // LMSA-6607 - LMSA-6949
                String complementoViaFedex = complementarLocalizacaoMercadoriaViaFedex(evento.getCdEvento(), doctoServico, dsObservacao);
                doctoServico.setObComplementoLocalizacao(complementoViaFedex);
            } else {
                doctoServico.setObComplementoLocalizacao(null);
            }

			/*
			 * Se a localização a ser lançada for de entrega realizada
			 * (CD_LOCALIZACAO_MERCADORIA = 1):
			 */
            if (evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() != null
                    && evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() == 1) {
				/*
				 * Atualizar quantidade de dias que o documento recebido
				 * (atualizar atributo NR_DIAS_REAL_ENTREGA com valor retornado
				 * da rotina Retorna Quantidade Dias Uteis Entrega Documento
				 * (item 10.04.01.01 A Localizar Mercadoria A) passando o
				 * documento de serviço que se está lançando o evento)
				 */
                Integer qtdDias = doctoServicoService.findQtdeDiasUteisEntregaDocto(doctoServico.getIdDoctoServico(),
                        dhEvento.toYearMonthDay());
                if (qtdDias != null) {
                    doctoServico.setNrDiasRealEntrega(qtdDias.shortValue());
                }
            }
        }

		/*
		 * Se for evento de cancelamento, atualiza o documento de serviço com a
		 * última localização de mercadoria associada
		 */
        if (evento.getCancelaEvento() != null) {
            List eventosDoctoServico = eventoDocumentoServicoService
                    .findEventosDoctoServicoNaoCancelados(doctoServico.getIdDoctoServico());
            if (!eventosDoctoServico.isEmpty()) {
                EventoDocumentoServico eventoDoctoServico = (EventoDocumentoServico) eventosDoctoServico.get(0);

                doctoServico.setLocalizacaoMercadoria(eventoDoctoServico.getEvento().getLocalizacaoMercadoria());

                if (!historicoFilialService.validateFilialUsuarioMatriz(eventoDoctoServico.getFilial().getIdFilial())) {
                    doctoServico.setFilialLocalizacao(eventoDoctoServico.getFilial());
                }
            }
        }
    }

    private void atualizaLocalizacaoMercadoriaComBatch(Evento evento, DoctoServico doctoServico, DateTime dhEvento,
                                                      String dsObservacao, Boolean blExecutarVerificacaoDocumentoManifestado, Long idFilialLocalizacao,
                                                      Boolean atualizarPorDoctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Boolean blIntegracaoFedex) {
        /**
         * Só atualiza o docto_servico se os dados forem diferentes do existente.
         */

        Boolean atualizaObComplementoLocalizacao = false;
        Map<String,Object> mapaAuxiliarDadosDoctoServico = new HashMap<String, Object>();

        if (evento.getLocalizacaoMercadoria() != null && evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() != null) {
            if(atualizarPorDoctoServico) {
                atualizaObComplementoLocalizacao = carregarDadosAtualizacaoDoctoServico(evento, doctoServico, dhEvento, dsObservacao, blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, atualizaObComplementoLocalizacao, blIntegracaoFedex);
            } else{
                atualizaObComplementoLocalizacao = carregarDadosAtualizacaoMapDoctoServico(evento, doctoServico, dhEvento, dsObservacao, blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, atualizaObComplementoLocalizacao, mapaAuxiliarDadosDoctoServico);
            }
        }

        if (evento.getCancelaEvento() != null) {
            if(atualizarPorDoctoServico) {
                tratarCancelamentoLocalizacaoMercadoriaDoctoServico(doctoServico);
            } else{
                tratarCancelamentoLocalizacaoMercadoriaMapDoctoServico(doctoServico, mapaAuxiliarDadosDoctoServico);
            }
        }

        if(atualizarPorDoctoServico) {
        	// LMSA-6949
            atualizaInformacoesDoctoServico(evento.getCdEvento(), doctoServico, adsmNativeBatchSqlOperations, atualizaObComplementoLocalizacao);
        } else{
        	// LMSA-6949
            atualizaInformacoesMapDoctoServico(evento.getCdEvento(), doctoServico, mapaAuxiliarDadosDoctoServico, adsmNativeBatchSqlOperations, atualizaObComplementoLocalizacao);
        }

    }

    private Boolean carregarDadosAtualizacaoDoctoServico(Evento evento, DoctoServico doctoServico, DateTime dhEvento, String dsObservacao, Boolean blExecutarVerificacaoDocumentoManifestado, Long idFilialLocalizacao, Boolean atualizaObComplementoLocalizacao, Boolean blIntegracaoFedex) {

        Boolean atualizaObComplementoLocalizacaoAux = atualizaObComplementoLocalizacao;

        if (Boolean.TRUE.equals(blExecutarVerificacaoDocumentoManifestado)) {
            if (doctoServico.getPreManifestoDocumentos() == null
                    || doctoServico.getPreManifestoDocumentos().isEmpty()) {
                doctoServico.setLocalizacaoMercadoria(evento.getLocalizacaoMercadoria());
            }
        } else {
            doctoServico.setLocalizacaoMercadoria(evento.getLocalizacaoMercadoria());
        }

        if (!SessionUtils.isFilialSessaoMatriz() && !Boolean.TRUE.equals(blIntegracaoFedex)) {

            Filial filialLocalizacao = idFilialLocalizacao == null ? SessionUtils.getFilialSessao() : this.generateNewFilial(idFilialLocalizacao);

            if (!doctoServico.getFilialLocalizacao().equals(filialLocalizacao)) {
                doctoServico.setFilialLocalizacao(filialLocalizacao);
            }

            if (doctoServico.getObComplementoLocalizacao() != null && !doctoServico.getObComplementoLocalizacao().equals(dsObservacao)) {
                atualizaObComplementoLocalizacaoAux = true;
                doctoServico.setObComplementoLocalizacao(dsObservacao);
            }
            
        } else {
            if (doctoServico.getObComplementoLocalizacao() != null) {
                atualizaObComplementoLocalizacaoAux = true;
                doctoServico.setObComplementoLocalizacao(null);
            }
        }

        atualizarDoctoServicoNrDiasRealEntrega(evento, doctoServico, dhEvento);

        return atualizaObComplementoLocalizacaoAux;
    }

    @SuppressWarnings("deprecation")
    private void atualizarDoctoServicoNrDiasRealEntrega(Evento evento, DoctoServico doctoServico, DateTime dhEvento) {
        if (evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() != null
                && evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() == 1) {
            Integer qtdDias = doctoServicoService.findQtdeDiasUteisEntregaDocto(doctoServico.getIdDoctoServico(),
                    dhEvento.toYearMonthDay());
            if (qtdDias != null && !qtdDias.equals(Integer.valueOf(doctoServico.getNrDiasRealEntrega()))) {
                doctoServico.setNrDiasRealEntrega(qtdDias.shortValue());
            }
        }
    }

    private Boolean carregarDadosAtualizacaoMapDoctoServico(Evento evento, DoctoServico doctoServico, DateTime dhEvento,
                                                            String dsObservacao, Boolean blExecutarVerificacaoDocumentoManifestado,
                                                            Long idFilialLocalizacao, Boolean atualizaObComplementoLocalizacao,
                                                            Map<String,Object> mapaAuxiliarDadosDoctoServico) {

        Boolean atualizaObComplementoLocalizacaoAux = atualizaObComplementoLocalizacao;

        if (Boolean.TRUE.equals(blExecutarVerificacaoDocumentoManifestado)) {
            if (doctoServico.getPreManifestoDocumentos() == null
                    || doctoServico.getPreManifestoDocumentos().isEmpty()) {
                mapaAuxiliarDadosDoctoServico.put(ID_LOCALIZACAO_MERCADORIA,evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
            }
        } else {
            mapaAuxiliarDadosDoctoServico.put(ID_LOCALIZACAO_MERCADORIA,evento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
        }

        if (!SessionUtils.isFilialSessaoMatriz()) {

            Filial filialLocalizacao = idFilialLocalizacao == null ? SessionUtils.getFilialSessao() : this.generateNewFilial(idFilialLocalizacao);

            if (!doctoServico.getFilialLocalizacao().equals(filialLocalizacao)) {
                mapaAuxiliarDadosDoctoServico.put(ID_FILIAL_LOCALIZACAO,filialLocalizacao.getIdFilial());
            }

            if (doctoServico.getObComplementoLocalizacao() != null && !doctoServico.getObComplementoLocalizacao().equals(dsObservacao)) {
                atualizaObComplementoLocalizacaoAux = true;
                mapaAuxiliarDadosDoctoServico.put(OB_COMPLEMENTO_LOCALIZACAO,dsObservacao);
            }

        } else {
            if (doctoServico.getObComplementoLocalizacao() != null) {
                atualizaObComplementoLocalizacaoAux = true;
                mapaAuxiliarDadosDoctoServico.put(OB_COMPLEMENTO_LOCALIZACAO,null);
            }
        }

        atualizarMapDoctoServicoNrDiasRealEntrega(evento, doctoServico, dhEvento, mapaAuxiliarDadosDoctoServico);

        return atualizaObComplementoLocalizacaoAux;
    }

    @SuppressWarnings("deprecation")
    private void atualizarMapDoctoServicoNrDiasRealEntrega(Evento evento, DoctoServico doctoServico, DateTime dhEvento, Map<String, Object> mapaAuxiliarDadosDoctoServico) {
        if (evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() != null
                && evento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() == 1) {
            Integer qtdDias = doctoServicoService.findQtdeDiasUteisEntregaDocto(doctoServico.getIdDoctoServico(),
                    dhEvento.toYearMonthDay());
            if (qtdDias != null && !qtdDias.equals(Integer.valueOf(doctoServico.getNrDiasRealEntrega()))) {
                mapaAuxiliarDadosDoctoServico.put(NR_DIAS_REAL_ENTREGA,qtdDias.shortValue());
            }
        }
    }

    @SuppressWarnings({ "rawtypes" })
    private void tratarCancelamentoLocalizacaoMercadoriaDoctoServico(DoctoServico doctoServico) {
        List eventosDoctoServico = eventoDocumentoServicoService
                .findEventosDoctoServicoNaoCancelados(doctoServico.getIdDoctoServico());
        if (!eventosDoctoServico.isEmpty()) {
            EventoDocumentoServico eventoDoctoServico = (EventoDocumentoServico) eventosDoctoServico.get(0);

            if (!doctoServico.getLocalizacaoMercadoria().equals(eventoDoctoServico.getEvento().getLocalizacaoMercadoria())) {
                doctoServico.setLocalizacaoMercadoria(eventoDoctoServico.getEvento().getLocalizacaoMercadoria());
            }

            if (!historicoFilialService.validateFilialUsuarioMatriz(eventoDoctoServico.getFilial().getIdFilial())
                    && !doctoServico.getFilialLocalizacao().equals(eventoDoctoServico.getFilial())) {
                doctoServico.setFilialLocalizacao(eventoDoctoServico.getFilial());
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void tratarCancelamentoLocalizacaoMercadoriaMapDoctoServico(DoctoServico doctoServico, Map<String,Object> mapaAuxiliarDadosDoctoServico) {
        List eventosDoctoServico = eventoDocumentoServicoService
                .findEventosDoctoServicoNaoCancelados(doctoServico.getIdDoctoServico());
        if (!eventosDoctoServico.isEmpty()) {
            EventoDocumentoServico eventoDoctoServico = (EventoDocumentoServico) eventosDoctoServico.get(0);

            if (!doctoServico.getLocalizacaoMercadoria().equals(eventoDoctoServico.getEvento().getLocalizacaoMercadoria())) {
                mapaAuxiliarDadosDoctoServico.put(ID_LOCALIZACAO_MERCADORIA,eventoDoctoServico.getEvento().getLocalizacaoMercadoria());
            }

            if (!historicoFilialService.validateFilialUsuarioMatriz(eventoDoctoServico.getFilial().getIdFilial())
                    && !doctoServico.getFilialLocalizacao().equals(eventoDoctoServico.getFilial())) {
                mapaAuxiliarDadosDoctoServico.put(ID_FILIAL_LOCALIZACAO,eventoDoctoServico.getFilial());
            }
        }
    }

    // LMSA-6949
    private void atualizaInformacoesDoctoServico(Short codigoEvento, DoctoServico doctoServico, 
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
            Boolean atualizaObComplementoLocalizacao) {
        //
        String updateTableAlias = ((doctoServico.getFilialLocalizacao() != null) ? doctoServico.getFilialLocalizacao().getIdFilial().toString() : "") +
                ((doctoServico.getLocalizacaoMercadoria() != null && doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() != null) ? doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria().toString() : "") +
                ((doctoServico.getNrDiasRealEntrega() != null) ? doctoServico.getNrDiasRealEntrega().toString() : "");

        // LMSA-6607
        updateTableAlias += (atualizaObComplementoLocalizacao ?
                ((doctoServico.getObComplementoLocalizacao() != null) ?
                		// LMSA-6949
                        complementarLocalizacaoMercadoriaViaFedex(codigoEvento, doctoServico, doctoServico.getObComplementoLocalizacao())
                         : "NULL")
                : "");

        if(!"".equals(updateTableAlias)) {
            if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsDoctoServico.TN_DOCTO_SERVICO, updateTableAlias)) {
                carregarBatchDoctoServico(doctoServico, adsmNativeBatchSqlOperations, atualizaObComplementoLocalizacao, updateTableAlias);
            }
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsDoctoServico.TN_DOCTO_SERVICO, doctoServico.getIdDoctoServico(), updateTableAlias);
        }
    }

    private void carregarBatchDoctoServico(DoctoServico doctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Boolean atualizaObComplementoLocalizacao, String updateTableAlias) {
        if (doctoServico.getFilialLocalizacao() != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    ID_FILIAL_LOCALIZACAO, doctoServico.getFilialLocalizacao().getIdFilial(), updateTableAlias);
        }

        if (doctoServico.getLocalizacaoMercadoria() != null && doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    ID_LOCALIZACAO_MERCADORIA, doctoServico.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria(), updateTableAlias);
        }

        if (doctoServico.getNrDiasRealEntrega() != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    NR_DIAS_REAL_ENTREGA, doctoServico.getNrDiasRealEntrega(), updateTableAlias);
        }

        if (atualizaObComplementoLocalizacao) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    OB_COMPLEMENTO_LOCALIZACAO, doctoServico.getObComplementoLocalizacao(), updateTableAlias);
        }
    }

    // LMSA-6949
    private void atualizaInformacoesMapDoctoServico(Short codigoEvento, DoctoServico doctoServico, Map<String,Object> mapaAuxiliarDadosDoctoServico,
                                                 AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                 Boolean atualizaObComplementoLocalizacao) {

        String updateTableAlias = ((mapaAuxiliarDadosDoctoServico.get(ID_FILIAL_LOCALIZACAO) != null) ? mapaAuxiliarDadosDoctoServico.get(ID_FILIAL_LOCALIZACAO).toString() : "") +
                ((mapaAuxiliarDadosDoctoServico.get(ID_LOCALIZACAO_MERCADORIA) != null) ? mapaAuxiliarDadosDoctoServico.get(ID_LOCALIZACAO_MERCADORIA).toString() : "") +
                ((mapaAuxiliarDadosDoctoServico.get(NR_DIAS_REAL_ENTREGA) != null) ? mapaAuxiliarDadosDoctoServico.get(NR_DIAS_REAL_ENTREGA).toString() : "");

        // LMSA-6607
        updateTableAlias +=
                (atualizaObComplementoLocalizacao ?
                        ((mapaAuxiliarDadosDoctoServico.get(OB_COMPLEMENTO_LOCALIZACAO) != null) ?
                        		//LMSA-6949
                                complementarLocalizacaoMercadoriaViaFedex(
                                		codigoEvento,
                                        doctoServico, 
                                        mapaAuxiliarDadosDoctoServico.get(OB_COMPLEMENTO_LOCALIZACAO).toString())
                                 : "NULL")
                        : "");

        if(!"".equals(updateTableAlias)) {
            if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsDoctoServico.TN_DOCTO_SERVICO, updateTableAlias)) {
                carregarBatchMapDoctoServico(mapaAuxiliarDadosDoctoServico, adsmNativeBatchSqlOperations, atualizaObComplementoLocalizacao, updateTableAlias);
            }
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsDoctoServico.TN_DOCTO_SERVICO, doctoServico.getIdDoctoServico(), updateTableAlias);
        }
    }

    private void carregarBatchMapDoctoServico(Map<String, Object> mapaAuxiliarDadosDoctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Boolean atualizaObComplementoLocalizacao, String updateTableAlias) {
        if (mapaAuxiliarDadosDoctoServico.get(ID_FILIAL_LOCALIZACAO) != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    ID_FILIAL_LOCALIZACAO, mapaAuxiliarDadosDoctoServico.get(ID_FILIAL_LOCALIZACAO), updateTableAlias);
        }

        if (mapaAuxiliarDadosDoctoServico.get(ID_LOCALIZACAO_MERCADORIA) != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    ID_LOCALIZACAO_MERCADORIA, mapaAuxiliarDadosDoctoServico.get(ID_LOCALIZACAO_MERCADORIA), updateTableAlias);
        }

        if (mapaAuxiliarDadosDoctoServico.get(NR_DIAS_REAL_ENTREGA) != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    NR_DIAS_REAL_ENTREGA, mapaAuxiliarDadosDoctoServico.get(NR_DIAS_REAL_ENTREGA), updateTableAlias);
        }

        if (atualizaObComplementoLocalizacao) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDoctoServico.TN_DOCTO_SERVICO,
                    OB_COMPLEMENTO_LOCALIZACAO, mapaAuxiliarDadosDoctoServico.get(OB_COMPLEMENTO_LOCALIZACAO), updateTableAlias);
        }
    }

    private Filial generateNewFilial(Long idFilial) {
        Filial filial = new Filial();
        filial.setIdFilial(idFilial);
        return filial;
    }

    private void sendEventoDoctoServicoJMS(EventoDocumentoServico eventoDctoServico, Boolean blOcorrenciaDoctoServicoManual) {
        if (eventoDctoServico.getEvento() != null && eventoDctoServico.getEvento().getCdEvento() != null) {
            eventoDocumentoServicoService.refreshEventoDocumentoServico(eventoDctoServico);
            EventoDocumentoServicoDMN eventoDocumentoServicoDTO = EventoDoctoServicoHelper.convertEventoDoctoServico(eventoDctoServico);
            eventoDocumentoServicoDTO.setBlOcorrenciaDoctoServicoManual(blOcorrenciaDoctoServicoManual);
            JmsMessageSender msg = integracaoJmsService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO, eventoDocumentoServicoDTO);
            integracaoJmsService.storeMessage(msg);
        }
    }
    
    public void sendEventoDoctoServicoJMSIBM(EventoDocumentoServico eventoDctoServico) {
        if (eventoDctoServico.getEvento() != null ) {
            integracaoEventoDocumentoServicoService.executeEnvioEventoDocumentoServicoIntegracao(eventoDctoServico);
        }
    }

    private void sendEventoDoctoServicoJMSEmBatch(EventoDocumentoServico eventoDctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        if (eventoDctoServico.getEvento() != null && eventoDctoServico.getEvento().getCdEvento() != null) {
            EventoDocumentoServicoDMN eventoDocumentoServicoDTO = EventoDoctoServicoHelper.convertEventoDoctoServico(eventoDctoServico);
            IntegracaoJmsComBatchService.JmsMessageSender msg =
                    integracaoJmsComBatchService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO, eventoDocumentoServicoDTO);
            integracaoJmsComBatchService.storeMessage(msg, adsmNativeBatchSqlOperations);
        }
    }


    /**
     * Método que retorna a descrição do evento de cancelamento que cancela o evento recebido
     *
     * @param cdEvento
     * @return
     */
    public Short findDescricaoEventoCancelamento(Short cdEvento) {
        Evento evento = findEventoRecebido(cdEvento);

        Evento eventoCancela = eventoService.findEventoByEventoCancelamento(evento.getIdEvento());

		/*Se não houver cancelamento associado ao evento*/
        if (eventoCancela == null) {
            throw new BusinessException("LMS-10039", new Object[]{cdEvento});
        } else {
            return eventoCancela.getCdEvento();
        }
    }

    /* Valida se existe evento associado ao código recebido*/
    private Evento findEventoRecebido(Short cdEvento) {
        Evento evento = eventoService.findByCdEvento(cdEvento);

		/*Se não existe evento aborta operação*/
        if (evento == null) {
            throw new BusinessException("LMS-10021", new Object[]{cdEvento});
        } else {
            return evento;
        }
    }

    @SuppressWarnings("rawtypes")
    private void validateExisteDocumentos(List documentos) {
        if (documentos == null || documentos.isEmpty()) {
            throw new BusinessException("LMS-10050");
        }
    }

    /**
     * Retorna somente o numerod o documento, sem filial nem digito verificador.
     *
     * @param nrDocumento
     * @return
     */
    private String filterNumber(String nrDocumento) {
        //Retira digito verificador caso exista
        String value = nrDocumento;
        if (value.indexOf("-") > 0) {
            value = StringUtils.substringBefore(nrDocumento, "-");
        }
        //Retira a sigla da filial caso exista
        if (value.indexOf(" ") > 0) {
            value = StringUtils.substringAfter(value, " ");
        }
        /** retorna somente o numerico da string */
        return FormatUtils.filterNumber(value);
    }

    /**
     * Retorna a parte numerica do numero de documento passado (Ex.: POA 00001234 -> 00001234)
     *
     * @param nrDocumento
     * @return
     */
    private Long parseNrDocumentoLong(String nrDocumento) {
        return Long.valueOf(filterNumber(nrDocumento));
    }

    /**
     * Valida se o documento já foi entregue, lançando uma exceção caso positivo.
     *
     * @param nrDocumento
     * @param idFilial
     * @param tpDocumento
     */
    @SuppressWarnings("rawtypes")
    public void validateDocumentoEntregue(String nrDocumento, Long idFilial, String tpDocumento) {
        List documentos = null;
        if (tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)) {
            documentos = getDocumentosCTR(nrDocumento, idFilial);
        } else if (tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL)) {
            documentos = getDocumentosCRT(nrDocumento, idFilial);
        } else if (tpDocumento.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE)) {
            documentos = getDocumentosNFT(nrDocumento, idFilial);
        }
        if (documentos != null) {
            for (Iterator it = documentos.iterator(); it.hasNext(); ) {
                Long idDocumentoServico = (Long) it.next();

                if (doctoServicoService.getRowCountDoctoServicoEntregues(idDocumentoServico).intValue() > 0) {
                    throw new BusinessException("LMS-10030");
                }
            }
        }
    }


    /**
     * CQPRO00025051
     * Gera evento volume de cada volume do manifesto associado ao Controle de Carga
     * Criado neste ponto apenas para referencia aos eventos de rastreabilidade.
     * Poderia ser chamada diretamente a classe EventoVolumeService.
     *
     * @param volumeNotaFiscal
     * @param cdEvento
     */
    @SuppressWarnings("deprecation")
    public void generateEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Short cdEvento) {
        getEventoVolumeService().generateEventoVolume(volumeNotaFiscal, cdEvento, "LM");
    }

    /**
     * CQPRO00025051
     * Gera evento dispositivo unitizacao de cada manifesto associado ao Controle de Carga
     * Criado neste ponto apenas para referencia aos eventos de rastreabilidade.
     * Poderia ser chamada diretamente a classe EventoDispositivoUnitizacaoService.
     *
     * @param dispositivoUnitizacao
     * @param cdEvento
     */
    public void generateEventoDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao, Short cdEvento, String obComplemento) {
        getEventoDispositivoUnitizacaoService().generateEventoDispositivo(dispositivoUnitizacao, cdEvento, "LM", obComplemento);
    }


    /**
     * CQPRO00025051
     * Gera evento dispositivo unitizacao de cada manifesto associado ao Controle de Carga
     * Criado neste ponto apenas para referencia aos eventos de rastreabilidade.
     * Poderia ser chamada diretamente a classe EventoDispositivoUnitizacaoService.
     *
     * @param dispositivoUnitizacao
     * @param cdEvento
     * @param adsmNativeBatchSqlOperations
     */
    public void generateEventoDispositivoUnitizacaoComBatch(DispositivoUnitizacao dispositivoUnitizacao, Short cdEvento,
                                                            String obComplemento, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        getEventoDispositivoUnitizacaoService().generateEventoDispositivoComBatch(dispositivoUnitizacao, cdEvento, "LM",
                obComplemento, adsmNativeBatchSqlOperations);
    }


    /**
     * LMS-7596 - Método utilizado na alteração de uma ocorrência de entrega, sendo que a nova ocorrência de entrega
     * é associada ao evento volume que será gerado.
     *
     * @param volumeNotaFiscal
     * @param cdEvento
     * @param ocorrenciaEntrega
     */
    @SuppressWarnings("deprecation")
    public void generateEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Short cdEvento, OcorrenciaEntrega ocorrenciaEntrega, DateTime dhOcorrencia) {
        getEventoVolumeService().generateEventoVolume(volumeNotaFiscal, cdEvento, "LM", null, ocorrenciaEntrega, dhOcorrencia);
    }

    public void generateEventoDocumentoComBatch(
            Evento evento,
            Long idDocumentoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Long idOcorrenciaEntrega,
            Long idOcorrenciaPendencia,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations
    ) {
        gerarEventoDocumentoComBatch(
                evento,
                idDocumentoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                idOcorrenciaEntrega,
                idOcorrenciaPendencia,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                SessionUtils.getUsuarioLogado(),
                adsmNativeBatchSqlOperations
        );
    }

    private Boolean gerarEventoDocumentoComBatch(Evento evento, Long idDocumentoServico, Long idFilial, String nrDocumento,
                                                 DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento, Long idOcorrenciaEntrega,
                                                 Long idOcorrenciaPendencia, Boolean withReturn, Boolean blExecutarVerificacaoDocumentoManifestado,
                                                 Long idFilialLocalizacao, Usuario usuario, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        DoctoServico doctoServico = doctoServicoService.findById(idDocumentoServico);
        Filial filial = null;
        if (idFilial != null) {
            filial = new Filial();
            filial.setIdFilial(idFilial);
        }

        return gerarEventoDocumentoComBatch(evento, doctoServico, filial, nrDocumento, dhEvento, idPedidoCompra, dsObservacao,
                tpDocumento, idOcorrenciaEntrega, idOcorrenciaPendencia, withReturn,
                blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, usuario, true,
                adsmNativeBatchSqlOperations, false);
    }

    public void generateEventoDocumentoComBatch(
            Evento evento,
            DoctoServico doctoServico,
            Long idFilial,
            String nrDocumento,
            DateTime dhEvento,
            Long idPedidoCompra,
            String dsObservacao,
            String tpDocumento,
            Long idOcorrenciaEntrega,
            Long idOcorrenciaPendencia,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
            Boolean blIntegracaoFedex
    ) {
        gerarEventoDocumentoComBatch(
                evento,
                doctoServico,
                idFilial,
                nrDocumento,
                dhEvento,
                idPedidoCompra,
                dsObservacao,
                tpDocumento,
                idOcorrenciaEntrega,
                idOcorrenciaPendencia,
                Boolean.FALSE,
                Boolean.FALSE,
                null,
                SessionUtils.getUsuarioLogado(),
                true,
                adsmNativeBatchSqlOperations,
                blIntegracaoFedex
        );
    }

    private Boolean gerarEventoDocumentoComBatch(Evento evento, DoctoServico doctoServico, Long idFilial, String nrDocumento,
                                                 DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento, Long idOcorrenciaEntrega,
                                                 Long idOcorrenciaPendencia, Boolean withReturn, Boolean blExecutarVerificacaoDocumentoManifestado,
                                                 Long idFilialLocalizacao, Usuario usuario,
                                                 Boolean armazenaDadosDoctoServico,
                                                 AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                 Boolean blIntegracaoFedex) {
        Filial filial = null;
        if (idFilial != null) {
            filial = new Filial();
            filial.setIdFilial(idFilial);
        }

        return gerarEventoDocumentoComBatch(evento, doctoServico, filial, nrDocumento, dhEvento, idPedidoCompra, dsObservacao,
                tpDocumento, idOcorrenciaEntrega, idOcorrenciaPendencia, withReturn,
                blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, usuario,
                armazenaDadosDoctoServico,
                adsmNativeBatchSqlOperations,
                blIntegracaoFedex);
    }

    @SuppressWarnings("rawtypes")
	public Boolean gerarEventoDocumentoComBatch(Evento evento, DoctoServico doctoServico, Filial filial, String nrDocumento,
                                                DateTime dhEvento, Long idPedidoCompra, String dsObservacao, String tpDocumento, Long idOcorrenciaEntrega,
                                                Long idOcorrenciaPendencia, Boolean withReturn, Boolean blExecutarVerificacaoDocumentoManifestado,
                                                Long idFilialLocalizacao, Usuario usuario,
                                                Boolean armazenaDadosDoctoServico,
                                                AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                Boolean blIntegracaoFedex) {

        Long idDocumentoServico = doctoServico.getIdDoctoServico();

		/* Verificação da regra de cancelamento de evento */
        if (evento != null && evento.getCancelaEvento() != null) {

            List eventosDoctoServico =
                    eventoDocumentoServicoService.findByEventoByDocumentoServico(evento.getCancelaEvento().getIdEvento(), idDocumentoServico);

            if (!eventosDoctoServico.isEmpty()) {
                atualizaEventoDoctoServicoCancelado(adsmNativeBatchSqlOperations, eventosDoctoServico);
            } else {
                if (withReturn) {
                    return Boolean.FALSE;
                } else {
                    String dsEvento = evento.getCdEvento() + " - "
                            + evento.getDescricaoEvento().getDsDescricaoEvento().getValue();
                    throw new BusinessException("LMS-10014", new String[]{dsEvento});
                }
            }
        }

        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
        if (dhEvento == null) {
            dhEvento = dhAtual;
        }

        EventoDocumentoServico eventoDctoServico = gerarEventoDocumento(evento, doctoServico, filial, nrDocumento,
                dhEvento, idPedidoCompra, dsObservacao, tpDocumento, idOcorrenciaEntrega, idOcorrenciaPendencia,
                usuario, dhAtual);

        atualizaLocalizacaoMercadoriaComBatch(evento, doctoServico, dhEvento, dsObservacao,
                blExecutarVerificacaoDocumentoManifestado, idFilialLocalizacao, armazenaDadosDoctoServico,
                adsmNativeBatchSqlOperations, blIntegracaoFedex);

        Map<String, Object> eventoDocumentoServicoKeyValueMap = new HashMap<String, Object>();
        eventoDocumentoServicoKeyValueMap.put("DH_EVENTO", eventoDctoServico.getDhEvento());
        eventoDocumentoServicoKeyValueMap.put("DH_EVENTO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoDocumentoServicoKeyValueMap.put("DH_INCLUSAO", eventoDctoServico.getDhInclusao());
        eventoDocumentoServicoKeyValueMap.put("DH_INCLUSAO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoDocumentoServicoKeyValueMap.put("BL_EVENTO_CANCELADO", eventoDctoServico.getBlEventoCancelado());
        eventoDocumentoServicoKeyValueMap.put("NR_DOCUMENTO", eventoDctoServico.getNrDocumento());
        eventoDocumentoServicoKeyValueMap.put("OB_COMPLEMENTO", eventoDctoServico.getObComplemento());
        eventoDocumentoServicoKeyValueMap.put("DH_GERACAO_PARCEIRO", eventoDctoServico.getDhGeracaoParceiro());
        eventoDocumentoServicoKeyValueMap.put("DH_GERACAO_PARCEIRO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoDocumentoServicoKeyValueMap.put("DH_COMUNICACAO", eventoDctoServico.getDhComunicacao());
        eventoDocumentoServicoKeyValueMap.put("DH_COMUNICACAO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoDocumentoServicoKeyValueMap.put("TP_DOCUMENTO", eventoDctoServico.getTpDocumento().getValue());
        eventoDocumentoServicoKeyValueMap.put("ID_USUARIO", eventoDctoServico.getUsuario().getIdUsuario());
        eventoDocumentoServicoKeyValueMap.put("ID_DOCTO_SERVICO", eventoDctoServico.getDoctoServico().getIdDoctoServico());
        eventoDocumentoServicoKeyValueMap.put("ID_EVENTO", eventoDctoServico.getEvento().getIdEvento());
        eventoDocumentoServicoKeyValueMap.put("ID_FILIAL", eventoDctoServico.getFilial().getIdFilial());
        eventoDocumentoServicoKeyValueMap.put("ID_PEDIDO_COMPRA", (eventoDctoServico.getPedidoCompra() != null) ? eventoDctoServico.getPedidoCompra().getIdPedidoCompra() : null);
        eventoDocumentoServicoKeyValueMap.put("ID_OCORRENCIA_ENTREGA", (eventoDctoServico.getOcorrenciaEntrega() != null) ? eventoDctoServico.getOcorrenciaEntrega().getIdOcorrenciaEntrega() : null);
        eventoDocumentoServicoKeyValueMap.put("ID_OCORRENCIA_PENDENCIA", (eventoDctoServico.getOcorrenciaPendencia() != null) ? eventoDctoServico.getOcorrenciaPendencia().getIdOcorrenciaPendencia() : null);

        adsmNativeBatchSqlOperations.addNativeBatchInsert(ConstantesEventosDocumentoServico.TN_EVENTO_DOCUMENTO_SERVICO, eventoDocumentoServicoKeyValueMap);

        // Adiciona o evento na fila JMS
        sendEventoDoctoServicoJMSEmBatch(eventoDctoServico, adsmNativeBatchSqlOperations);
        
        sendEventoDoctoServicoJMSIBM(eventoDctoServico);

        return Boolean.TRUE;
    }

    @SuppressWarnings("rawtypes")
    private void atualizaEventoDoctoServicoCancelado(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, List eventosDoctoServico) {
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConstantesEventosDocumentoServico.TN_EVENTO_DOCUMENTO_SERVICO,
                EVENTO_CANCELADO)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    ConstantesEventosDocumentoServico.TN_EVENTO_DOCUMENTO_SERVICO, "BL_EVENTO_CANCELADO",
                    true, EVENTO_CANCELADO
            );
        }

        EventoDocumentoServico eventoDoctoServico = (EventoDocumentoServico) eventosDoctoServico.get(0);
        eventoDoctoServico.setBlEventoCancelado(Boolean.TRUE);

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(
                ConstantesEventosDocumentoServico.TN_EVENTO_DOCUMENTO_SERVICO,
                eventoDoctoServico.getIdEventoDocumentoServico(),
                EVENTO_CANCELADO
        );

        sendEventoDoctoServicoJMSEmBatch(eventoDoctoServico, adsmNativeBatchSqlOperations);
    }
    
    // LMSA-6607
    private static final String PARAMETRO_COMPLEMENTO_MERCADORIA_FEDEX = "COMPLEMENTO_MERCADORIA_FDX";
    // LMSA-6949
    private String complementarLocalizacaoMercadoriaViaFedex(Short codigoEvento, DoctoServico doctoServico, String complementoLocalizacao) {
        StringBuilder complementoViaFedex = new StringBuilder(
                StringUtils.isNotEmpty(complementoLocalizacao) ? complementoLocalizacao : "");

        // validar integridade parametro informado
        boolean complementarViaFedex = ConstantesSim.EVENTO_SAIDA_PORTARIA_EMVIAGEM.equals(codigoEvento);
        complementarViaFedex = complementarViaFedex && doctoServico != null && doctoServico.getIdDoctoServico() != null;
        
        if (complementarViaFedex) {
            Filial filialEvento = doctoServico != null ? doctoServico.getFilialByIdFilialOrigem() : null;
            Long idDoctoServico = doctoServico != null ? doctoServico.getIdDoctoServico() : null;
            Manifesto manifesto = manifestoService.findManifestoViagemByIdDoctoServicoFilialEvento (
            		idDoctoServico, filialEvento != null ? filialEvento.getIdFilial() : null );

            complementarViaFedex = validarManifestoFedex(filialEvento, manifesto);
            // caso o 1o teste OK, entao manifesto esta devidamente populado e pode se extrair Carga Compartilhada do mesmo
            ControleCarga controleCarga = manifesto != null ? manifesto.getControleCarga() : null;
            complementarViaFedex = complementarViaFedex && validarProprietarioFedex(controleCarga);
            if (complementarViaFedex) {
                String complementoFedexParametrizado = (String) configuracoesFacade.getValorParametro(PARAMETRO_COMPLEMENTO_MERCADORIA_FEDEX);
                complementoViaFedex.append(" ");
                complementoViaFedex.append(complementoFedexParametrizado);
            }
        }
        return complementoViaFedex.toString();
    }
    private static final String MODAL_MANIFESTO_FDX = "MODAL_MANIFESTO_FDX";
    private boolean validarManifestoFedex(Filial filialEvento, Manifesto manifesto) {
        // primeiramente validar integridade dos parametros informados
        boolean test = filialEvento != null && manifesto != null;
        test = test && filialEvento.getIdFilial() != null;
        test = test && manifesto.getFilialByIdFilialOrigem() != null;
        // aplicar validar de igualdade entre filiais
        test = test && filialEvento.getIdFilial().equals(manifesto.getFilialByIdFilialOrigem().getIdFilial());
        
        if (test) {
            List<String> parametroModal = ListUtilsPlus.splitToListString(
                    (String) configuracoesFacade.getValorParametro(MODAL_MANIFESTO_FDX), ";");
            test = parametroModal.contains(manifesto.getTpModal().getValue());
        }
        return test;
    }
    private static final String CNPJ_PROPRIETARIO_FDX = "CNPJ_PROPRIETARIO_FDX";
    private boolean validarProprietarioFedex(ControleCarga controleCarga) {
        boolean test = controleCarga != null;
        if (test) {
            List<String> cnpjProprietarios = ListUtilsPlus.splitToListString(
                    (String) configuracoesFacade.getValorParametro(CNPJ_PROPRIETARIO_FDX), ";");
            test = cnpjProprietarios.contains(controleCarga.getProprietario().getPessoa().getNrIdentificacao().substring(0, 8));
        }
        return test;
    }


    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
        this.manifestoViagemNacionalService = manifestoViagemNacionalService;
    }

    public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
        this.manifestoInternacionalService = manifestoInternacionalService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    public void setPedidoCompraService(PedidoCompraService pedidoCompraService) {
        this.pedidoCompraService = pedidoCompraService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
        this.ctoInternacionalService = ctoInternacionalService;
    }

    public void setMdaService(MdaService mdaService) {
        this.mdaService = mdaService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
        this.reciboReembolsoService = reciboReembolsoService;
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

    public HistoricoFilialService getHistoricoFilialService() {
        return historicoFilialService;
    }

    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
        this.historicoFilialService = historicoFilialService;
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }

    public void setIntegracaoJmsComBatchService(IntegracaoJmsComBatchService integracaoJmsComBatchService) {
        this.integracaoJmsComBatchService = integracaoJmsComBatchService;
    }
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
	
	public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

	public void setIntegracaoEventoDocumentoServicoService(IntegracaoEventoDocumentoServicoService integracaoEventoDocumentoServicoService) {
		this.integracaoEventoDocumentoServicoService = integracaoEventoDocumentoServicoService;
	}
}

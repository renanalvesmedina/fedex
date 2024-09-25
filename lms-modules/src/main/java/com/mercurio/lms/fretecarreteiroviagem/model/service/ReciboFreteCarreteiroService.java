package com.mercurio.lms.fretecarreteiroviagem.model.service;

import br.com.tntbrasil.integracao.domains.ciot.CIOTBuscaIntegracaoPUDDTO;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.*;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.AdiantamentoTrechoService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.*;
import com.mercurio.lms.contratacaoveiculos.model.service.FluxoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.DescontoRfcService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.EventoNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ExecuteWorkflowReciboFreteCarreteiroColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;
import com.mercurio.lms.fretecarreteiroviagem.model.OcorrenciaFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.dao.ReciboFreteCarreteiroDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.tributos.model.service.CalcularInssService;
import com.mercurio.lms.tributos.model.service.CalcularIssService;
import com.mercurio.lms.tributos.model.service.DescontoInssCarreteiroService;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * não inserir documentação apés ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.fretecarreteiroviagem.reciboFreteCarreteiroService"
 */
public class ReciboFreteCarreteiroService extends CrudService<ReciboFreteCarreteiro, Long> {

    private static final String DESCONTO_OPERACIONAL = "Desconto Operacional";
    private static final String DESCONTO_CONCEDIDO_REFERENTE_A = " Desconto concedido referente à:";
    private static final String DESCONTO_VINCULADO_REFERENTE_A = " Recibo vinculado ao desconto:";
    private static final String VIAGEM = "V";
    private static final String COLETA_ENTREGA = "C";
    private static final String BL_COMPLEMENTAR = "blComplementar";
    private static final String TP_RECIBO_FRETE_CARRETEIRO = "tpReciboFreteCarreteiro";
    private static final String PROPRIETARIO_NR_IDENTIFICACAO = "proprietario.nrIdentificacao";
    private static final String PROPRIETARIO_TP_IDENTIFICACAO = "proprietario.tpIdentificacao";
    private static final String ID_RECIBO_COMPLEMENTADO = "idReciboComplementado";
    private static final String NR_RECIBO_FRETE_CARRETEIRO_2 = "nrReciboFreteCarreteiro2";
    private static final String NR_RECIBO_FRETE_CARRETEIRO = "nrReciboFreteCarreteiro";
    private static final String NR_RECIBO_FRETE_PATTERN = "0000000000";
    private static final String ID_RECIBO_FRETE_CARRETEIRO = "idReciboFreteCarreteiro";
    private static final String SG_FILIAL = "sgFilial";
    private static final String DH_EMISSAO = "dhEmissao";
    private static final String DS_MOEDA_01 = "dsMoeda_01";
    private static final String VL_BRUTO = "vlBruto";
    private static final String TP_SITUACAO_RECIBO = "tpSituacaoRecibo";
    private static final String DT_PAGTO_REAL = "dtPagtoReal";
    private static final String DT_SUGERIDA_PAGTO = "dtSugeridaPagto";
    private static final String NR_FROTA = "nrFrota";
    private static final String NR_IDENTIFICADOR = "nrIdentificador";
    private static final String RECIBO_FRETE_CARRETEIRO_TP_RECIBO_FRETE_CARRETEIRO = "reciboFreteCarreteiro.tpReciboFreteCarreteiro";
    private static final String RECIBO_FRETE_CARRETEIRO_TP_SITUACAO_RECIBO = "reciboFreteCarreteiro.tpSituacaoRecibo";
    private static final String FILIAL_ID_FILIAL = "filial.idFilial";
    private static final String PROPRIETARIO_ID_PROPRIETARIO = "proprietario.idProprietario";
    private static final String ID_MOTORISTA = "idMotorista";
    private static final String IS_EMPRESA_PARCEIRA = "isEmpresaParceira";
    private static final String ID_PROPRIETARIO = "idProprietario";
    private static final String ID_MEIO_TRANSPORTE = "idMeioTransporte";
    private static final String VL_OUTRAS_FONTES = "vlOutrasFontes";
    private static final String VL_INSS = "vlInss";
    private static final String VL_SALARIO_BASE = "vlSalarioBase";
    private static final String PC_ALIQUOTA_INSS = "pcAliquotaInss";
    private static final String PC_ALIQUOTA_ISS = "pcAliquotaIss";
    private static final String DATA_RETROATIVA_MINIMA = "dataRetroativaMinima";
    private static final String DATA_RETROATIVA_MAXIMA = "dataRetroativaMaxima";
    private static final String NR_RECIBOS = "nrRecibos";
    private static final String RECIBO_FRETE_CARRETEIRO_ID_RECIBO_FRETE_CARRETEIRO = "reciboFreteCarreteiro.idReciboFreteCarreteiro";
    private static final String DT_EMISSAO_INICIAL = "dtEmissaoInicial";
    private static final String DT_EMISSAO_FINAL = "dtEmissaoFinal";
    private static final String ID_SERVICO_TRIBUTO_CARRETEIRO = "ID_SERVICO_TRIBUTO_CARRETEIRO";
    private static final String DIA_SEMANA_EMISSAO_RECIBO = "DIA_SEMANA_EMISSAO_RECIBO";
    private static final String PERIODO_MAX_EMISSAO_RECIBO = "PERIODO_MAX_EMISSAO_RECIBO";
    private static final String PROPRIETARIO_NM_PESSOA = "proprietario.nmPessoa";
    private static final String BL_ADIANTAMENTO = "blAdiantamento";
    private static final String RECIBO_FRETE_CARRETEIRO_FILIAL_SG_FILIAL = "reciboFreteCarreteiro.filial.sgFilial";
    private static final String RECIBO_FRETE_CARRETEIRO_NR_RECIBO_FRETE_CARRETEIRO = "reciboFreteCarreteiro.nrReciboFreteCarreteiro";
    private static final String FILIAL_SG_FILIAL = "filial.sgFilial";
    private static final String MEIO_TRANSPORTE_NR_FROTA = "meioTransporte_nrFrota";
    private static final String MEIO_TRANSPORTE_NR_IDENTIFICADOR = "meioTransporte_nrIdentificador";
    private static final String CONTROLE_CARGA_SG_FILIAL_ORIGEM = "controleCarga.sgFilialOrigem";
    private static final String CONTROLE_CARGA_NR_CONTROLE_CARGA = "controleCarga.nrControleCarga";
    private static final String DT_PGTO_REAL = "dtPgtoReal";
    private static final String VL_LIQUIDO = "vlLiquido";
    private static final String RECIBO_FRETE_CARRETEIRO = "reciboFreteCarreteiro";
    private static final String BL_DESCONTO_CANCELADO = "blDescontoCancelado";
    private static final String TP_SITUACAO = "tpSituacao";
    private static final String TP_OCORRENCIA = "tpOcorrencia";
    private static final String DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE = "DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE";
    private static final String CONTROLE_CARGA = "controleCarga";
    private static final String ID_CONTROLE_CARGA = "idControleCarga";
    private static final String TP_EVENTO_CONTROLE_CARGA = "tpEventoControleCarga";
    private static final String RECIBO_COMPLEMENTADO = "reciboComplementado";
    private static final String TP_SITUACAO_RECIBO_DESCRIPTION = "tpSituacaoRecibo.description";
    private static final String TP_SITUACAO_RECIBO_VALUE = "tpSituacaoRecibo.value";
    private static final String NR_NF_CARRETEIRO = "nrNfCarreteiro";
    private static final String OB_RECIBO_FRETE_CARRETEIRO = "obReciboFreteCarreteiro";
    private static final String DT_PGTO_PROGRAMADA_ORIGINAL = "dtPgtoProgramadaOriginal";
    private static final String DT_PGTO_REAL_ORIGINAL = "dtPgtoRealOriginal";
    private static final String SITUACAO_RECIBO_ORIGINAL = "situacaoReciboOriginal";
    private static final String ULTIMO_APROVADOR_ORIGINAL = "ultimoAprovadorOriginal";
    private static final String CRIADOR_EMISSOR_ORIGINAL = "criadorEmissorOriginal";
    private static final String OBSERVACAO_ORIGINAL = "observacaoOriginal";
    private static final String VL_LIQUIDO_ORIGINAL = "vlLiquidoOriginal";
    private static final String VL_BRUTO_ORIGINAL = "vlBrutoOriginal";
    private static final String MEIO_TRANSPORTE_NR_IDENTIFICADOR_ORIGINAL = "meioTransporteNrIdentificadorOriginal";
    private static final String MEIO_TRANSPORTE_NR_FROTA_ORIGINAL = "meioTransporteNrFrotaOriginal";
    private static final String PROPRIETARIO_IDENTIFICACAO_ORIGINAL = "proprietarioIdentificacaoOriginal";
    private static final String PROPRIETARIO_NOME_ORIGINAL = "proprietarioNomeOriginal";
    private static final String DT_EMISSAO_ORIGINAL = "dtEmissaoOriginal";
    private static final String NR_RECIBO_FRETE_CARRETEIRO_ORIGINAL = "nrReciboFreteCarreteiroOriginal";
    private static final String ID_RECIBO_FRETE_CARRETEIRO_ORIGINAL = "idReciboFreteCarreteiroOriginal";
    private static final String DT_PGTO_PROGRAMADA = "dtPgtoProgramada";
    private static final String SITUACAO_RECIBO = "situacaoRecibo";
    private static final String ULTIMO_APROVADOR = "ultimoAprovador";
    private static final String CRIADOR_EMISSOR = "criadorEmissor";
    private static final String OBSERVACAO = "observacao";
    private static final String MEIO_TRANSPORTE_NR_IDENTIFICADOR1 = "meioTransporteNrIdentificador";
    private static final String MEIO_TRANSPORTE_NR_FROTA1 = "meioTransporteNrFrota";
    private static final String PROPRIETARIO_NOME = "proprietarioNome";
    private static final String PROPRIETARIO_IDENTIFICACAO = "proprietarioIdentificacao";
    private static final String DT_EMISSAO = "dtEmissao";
    private static final String TP_STATUS_RECIBO = "tpStatusRecibo";
    public static final String VL_ISS = "vlIss";
    public static final String MEIO_TRANSPORTE_RODOVIARIO_ID_MEIO_TRANSPORTE = "meioTransporteRodoviario.idMeioTransporte";

    private FilialService filialService;
    private ProprietarioService proprietarioService;
    private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    private MotoristaService motoristaService;
    private ControleCargaService controleCargaService;
    private OcorrenciaFreteCarreteiroService ocorrenciaFreteCarreteiroService;
    private DomainService domainService;
    private EventoControleCargaService eventoControleCargaService;
    private ConfiguracoesFacade configuracoesFacade;
    private DescontoInssCarreteiroService descontoInssCarreteiroService;
    private CalcularInssService calcularInssService;
    private CalcularIssService calcularIssService;
    private EnderecoPessoaService enderecoPessoaService;
    private MoedaPaisService moedaPaisService;
    private GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService;
    private AdiantamentoTrechoService adiantamentoTrechoService;
    private FluxoContratacaoService fluxoContratacaoService;
    private ManifestoService manifestoService;
    private NotaCreditoService notaCreditoService;
    private EventoNotaCreditoService eventoNotaCreditoService;
    private IntegracaoJmsService integracaoJmsService;
    private WorkflowPendenciaService workflowPendenciaService;
    private AnexoReciboFcService anexoReciboFcService;
    private ExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService;
    private ExecuteWorkflowReciboFreteCarreteiroColetaEntregaService executeWorkflowReciboFreteCarreteiroColetaEntregaService;
    private DescontoRfcService descontoRfcService;
    private ParametroGeralService parametroGeralService;
    private CalculoReciboIRRFService calculoReciboIRRFService;
    private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setDescontoRfcService(DescontoRfcService descontoRfcService) {
        this.descontoRfcService = descontoRfcService;
    }

    public ExecuteWorkflowReciboFreteCarreteiroColetaEntregaService getExecuteWorkflowReciboFreteCarreteiroColetaEntregaService() {
        return executeWorkflowReciboFreteCarreteiroColetaEntregaService;
    }

    public void setExecuteWorkflowReciboFreteCarreteiroColetaEntregaService(
            ExecuteWorkflowReciboFreteCarreteiroColetaEntregaService executeWorkflowReciboFreteCarreteiroColetaEntregaService) {
        this.executeWorkflowReciboFreteCarreteiroColetaEntregaService = executeWorkflowReciboFreteCarreteiroColetaEntregaService;
    }

    public AnexoReciboFcService getAnexoReciboFcService() {
        return anexoReciboFcService;
    }

    public void setAnexoReciboFcService(AnexoReciboFcService anexoReciboFcService) {
        this.anexoReciboFcService = anexoReciboFcService;
    }

    public ExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService getExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService() {
        return executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService;
    }

    public void setExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService(
            ExecuteWorkflowReciboComplementarFreteCarreteiroColetaEntregaService executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService) {
        this.executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService = executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService;
    }

    public WorkflowPendenciaService getWorkflowPendenciaService() {
        return workflowPendenciaService;
    }

    public void setWorkflowPendenciaService(
            WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

    public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
        this.adiantamentoTrechoService = adiantamentoTrechoService;
    }

    public void setFluxoContratacaoService(FluxoContratacaoService fluxoContratacaoService) {
        this.fluxoContratacaoService = fluxoContratacaoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
        this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
    }

    public void setMotoristaService(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    public void setProprietarioService(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    public void setOcorrenciaFreteCarreteiroService(OcorrenciaFreteCarreteiroService ocorrenciaFreteCarreteiroService) {
        this.ocorrenciaFreteCarreteiroService = ocorrenciaFreteCarreteiroService;
    }

    public void setDomainService(DomainService domainService) {
        this.domainService = domainService;
    }

    public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setDescontoInssCarreteiroService(DescontoInssCarreteiroService descontoInssCarreteiroService) {
        this.descontoInssCarreteiroService = descontoInssCarreteiroService;
    }

    public void setCalcularInssService(CalcularInssService calcularInssService) {
        this.calcularInssService = calcularInssService;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
        this.moedaPaisService = moedaPaisService;
    }

    public void setCalcularIssService(CalcularIssService calcularIssService) {
        this.calcularIssService = calcularIssService;
    }

    public void setGerarRateioFreteCarreteiroService(GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService) {
        this.gerarRateioFreteCarreteiroService = gerarRateioFreteCarreteiroService;
    }

    public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
        this.notaCreditoService = notaCreditoService;
    }

    public EventoNotaCreditoService getEventoNotaCreditoService() {
        return eventoNotaCreditoService;
    }

    public void setEventoNotaCreditoService(
            EventoNotaCreditoService eventoNotaCreditoService) {
        this.eventoNotaCreditoService = eventoNotaCreditoService;
    }

    public void setCalculoReciboIRRFService(CalculoReciboIRRFService calculoReciboIRRFService) {
        this.calculoReciboIRRFService = calculoReciboIRRFService;
    }

    public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
        this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
    }

    /**
     * Atribui o DAO responsével por tratar a persisténcia dos dados deste serviéo.
     *
     * @param dao Instância do DAO.
     */
    public void setReciboFreteCarreteiroDAO(ReciboFreteCarreteiroDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviéo que é responsével por tratar a persisténcia dos dados deste serviéo.
     *
     * @return instância do DAO.
     */
    private final ReciboFreteCarreteiroDAO getReciboFreteCarreteiroDAO() {
        return (ReciboFreteCarreteiroDAO) getDao();
    }

    /**
     * Método utilizado pela Integraééo
     *
     * @param idFilial
     * @param nrReciboFreteCarreteiro
     * @return <b>ReciboFreteCarreteiro</b>
     * @author Andre Valadas
     */
    public ReciboFreteCarreteiro findReciboFreteCarreteiro(Long idFilial, Long nrReciboFreteCarreteiro) {
        return getReciboFreteCarreteiroDAO().findReciboFreteCarreteiro(idFilial, nrReciboFreteCarreteiro);
    }

    /**
     * Recupera uma instância de <code>ReciboFreteCarreteiro</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return instância que possui o id informado.
     */
    @Override
    public ReciboFreteCarreteiro findById(java.lang.Long id) {
        return (ReciboFreteCarreteiro) super.findById(id);
    }

    /**
     * Retorna TypedFlatMap com todos campos para o detalhamento na tela 'Manter Recibos'.
     *
     * @param id
     * @return
     */
    public ReciboFreteCarreteiro findByIdCustom(java.lang.Long id) {
        return getReciboFreteCarreteiroDAO().findByIdCustom(id);

    }


    /**
     * Retorna TypedFlatMap com todos campos para o detalhamento na tela 'Manter Recibos Complementares'.
     *
     * @param id
     * @return
     */
    public ReciboFreteCarreteiro findByIdComplementar(java.lang.Long id) {
        return getReciboFreteCarreteiroDAO().findByIdCustom(id);
    }

    private ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
        criteria.put(BL_COMPLEMENTAR, false);
        ResultSetPage rsp = getReciboFreteCarreteiroDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
        FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
            @Override
            public Map<String, Object> filterItem(Object item) {
                Map<String, Object> oldRow = (Map<String, Object>) item;
                return bean2Map(oldRow);
            }
        };
        return (ResultSetPage) frsp.doFilter();
    }

    public Integer getRowCountViagem(TypedFlatMap criteria) {
        criteria.put(TP_RECIBO_FRETE_CARRETEIRO, new String[]{VIAGEM, "P"});
        criteria.put(BL_COMPLEMENTAR, false);
        return getReciboFreteCarreteiroDAO().getRowCountCustom(criteria);
    }

    public ResultSetPage findPaginatedViagem(TypedFlatMap criteria) {
        criteria.put(TP_RECIBO_FRETE_CARRETEIRO, new String[]{VIAGEM, "P"});
        return this.findPaginatedCustom(criteria);
    }

    public Integer getRowCountColetaEntrega(TypedFlatMap criteria) {
        criteria.put(TP_RECIBO_FRETE_CARRETEIRO, new String[]{COLETA_ENTREGA});
        return getReciboFreteCarreteiroDAO().getRowCountCustom(criteria);
    }

    public ResultSetPage findPaginatedColetaEntrega(TypedFlatMap criteria) {
        criteria.put(TP_RECIBO_FRETE_CARRETEIRO, new String[]{COLETA_ENTREGA});
        return this.findPaginatedCustom(criteria);
    }

    private List findLookupCustom(String tpRecibo, TypedFlatMap criteria) {
        criteria.put(TP_RECIBO_FRETE_CARRETEIRO, tpRecibo);
        List l = getReciboFreteCarreteiroDAO().findLookupCustom(criteria);
        List newList = new ArrayList();
        Iterator i = l.iterator();
        while (i.hasNext()) {
            Map oldMap = (Map) i.next();
            newList.add(bean2Map(oldMap));
        }
        return newList;
    }

    public List findLookupViagem(TypedFlatMap criteria) {
        return this.findLookupCustom(VIAGEM, criteria);
    }

    public List findLookupColetaEntrega(TypedFlatMap criteria) {
        return this.findLookupCustom(COLETA_ENTREGA, criteria);
    }

    private TypedFlatMap bean2Map(Map oldRow) {
        TypedFlatMap newRow = new TypedFlatMap();
        Set s = oldRow.keySet();
        Iterator i = s.iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            newRow.put(key.replace('_', '.'), oldRow.get(key));
        }

        String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(
                newRow.getDomainValue(PROPRIETARIO_TP_IDENTIFICACAO).getValue(),
                newRow.getString(PROPRIETARIO_NR_IDENTIFICACAO)
        );
        newRow.put(PROPRIETARIO_NR_IDENTIFICACAO, nrIdentificacaoProprietario);

        Object idComplementado = oldRow.get(ID_RECIBO_COMPLEMENTADO);
        newRow.put(NR_RECIBO_FRETE_CARRETEIRO_2,
                FormatUtils.formatLongWithZeros(newRow.getLong(NR_RECIBO_FRETE_CARRETEIRO), NR_RECIBO_FRETE_PATTERN)
                        + (idComplementado != null ? COLETA_ENTREGA : "")
        );
        newRow.put(BL_COMPLEMENTAR, idComplementado != null);

        return newRow;
    }

    /**
     * Find da grid de Atendimentos da tela 'Manter Recibos'
     *
     * @param criteria
     * @return
     */
    public List<Map<String, Object>> findGridAdiantamentos(TypedFlatMap criteria) {
        List<ReciboFreteCarreteiro> lResult = getReciboFreteCarreteiroDAO().findGridAdiantamentos(criteria);
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();

        for (ReciboFreteCarreteiro reciboFreteCarreteiro : lResult) {
            Moeda moeda = reciboFreteCarreteiro.getMoedaPais().getMoeda();

            Map<String, Object> newRow = new HashMap<String, Object>();
            newRow.put(ID_RECIBO_FRETE_CARRETEIRO, reciboFreteCarreteiro.getIdReciboFreteCarreteiro());
            newRow.put(SG_FILIAL, reciboFreteCarreteiro.getFilial().getSgFilial());
            newRow.put(NR_RECIBO_FRETE_CARRETEIRO, reciboFreteCarreteiro.getNrReciboFreteCarreteiro());
            newRow.put(DH_EMISSAO, reciboFreteCarreteiro.getDhEmissao());
            newRow.put(DS_MOEDA_01, moeda.getSiglaSimbolo());
            newRow.put(VL_BRUTO, reciboFreteCarreteiro.getVlBruto());
            newRow.put(TP_SITUACAO_RECIBO, reciboFreteCarreteiro.getTpSituacaoRecibo().getDescription().getValue());
            newRow.put(DT_PAGTO_REAL, reciboFreteCarreteiro.getDtPagtoReal());
            newRow.put(DT_SUGERIDA_PAGTO, reciboFreteCarreteiro.getDtSugeridaPagto());

            newList.add(newRow);
        }

        return newList;
    }

    /**
     * Store utilizado nas telas de 'Manter Recibos'.
     *
     * @param values
     * @return
     */
    public java.io.Serializable storeCustom(TypedFlatMap values) {
        Long idReciboFreteCarreteiro = values.getLong(ID_RECIBO_FRETE_CARRETEIRO);
        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO()
                .getHibernateTemplate().load(ReciboFreteCarreteiro.class, idReciboFreteCarreteiro);

        rfc.setNrNfCarreteiro(values.getString(NR_NF_CARRETEIRO));
        rfc.setObReciboFreteCarreteiro(values.getString(OB_RECIBO_FRETE_CARRETEIRO));

        rfc.setVlLiquido(this.findValorLiquidoRecibo(rfc.getIdReciboFreteCarreteiro()));

        return super.store(rfc);
    }

    /**
     * Cancelamento de recibo utilizado nas telas de 'Manter Recibos'.
     *
     * @param values
     * @return
     */
    public TypedFlatMap storeCancelarReciboAfterValidation(TypedFlatMap values) {
        Long idReciboFreteCarreteiro = values.getLong(ID_RECIBO_FRETE_CARRETEIRO);
        List<ReciboFreteCarreteiro> lComplementares = this.findRecibosComplementares(idReciboFreteCarreteiro);

        for (ReciboFreteCarreteiro rfcCompl : lComplementares) {
            this.storeCancelarRecibo(rfcCompl.getIdReciboFreteCarreteiro());
        }

        ReciboFreteCarreteiro rfc = this.storeCancelarRecibo(idReciboFreteCarreteiro);
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put(TP_SITUACAO_RECIBO_VALUE, rfc.getTpSituacaoRecibo().getValue());
        retorno.put(TP_SITUACAO_RECIBO_DESCRIPTION, rfc.getTpSituacaoRecibo().getDescription());
        return retorno;
    }

    /**
     * Cancela um recibo a partir do id recebido.
     *
     * @param idReciboFreteCarreteiro
     * @return
     */
    public ReciboFreteCarreteiro storeCancelarRecibo(Long idReciboFreteCarreteiro) {
        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getHibernateTemplate().load(ReciboFreteCarreteiro.class, idReciboFreteCarreteiro);

        String tpSituacaoRecibo = rfc.getTpSituacaoRecibo().getValue();
        if (!"GE".equals(tpSituacaoRecibo) && !"EM".equals(tpSituacaoRecibo) && !"RE".equals(tpSituacaoRecibo))
            throw new BusinessException("LMS-24013");

        DateTime dhGeracaoMovimento = rfc.getDhGeracaoMovimento();
        if ("EM".equals(tpSituacaoRecibo) && dhGeracaoMovimento != null)
            throw new BusinessException("LMS-24029");

        Domain d = domainService.findByName(DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE);
        DomainValue dv = d.findDomainValueByValue("CA");
        rfc.setTpSituacaoRecibo(dv);

        List<NotaCredito> notaCreditos = rfc.getNotaCreditos();
        for (NotaCredito notaCredito : notaCreditos) {
            Filial filial = (Filial) getReciboFreteCarreteiroDAO()
                    .getHibernateTemplate().load(Filial.class, rfc.getFilial().getIdFilial());

            eventoNotaCreditoService.storeEventoNotaCredito(notaCredito,
                    ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_RECIBO_FRETE_CARRETEIRO,
                    ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_CANCELADO);

            notaCredito.setReciboFreteCarreteiro(null);

            String obNota = notaCredito.getObNotaCredito() == null ? "" : notaCredito.getObNotaCredito();
            String recibo = filial.getSgFilial() + " - " +
                    FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(), NR_RECIBO_FRETE_PATTERN);
            String texto = obNota + "\n" + configuracoesFacade.getMensagem("LMS-25040", new Object[]{recibo});
            if (texto.length() > 500) {
                texto = texto.substring(0, 499);
            }
            notaCredito.setObNotaCredito(texto);


            getReciboFreteCarreteiroDAO().store(notaCredito);
        }

        if (rfc.getVlInss() != null && rfc.getVlInss().compareTo(new BigDecimal(0.0)) != 0) {
            YearMonthDay data = JTDateTimeUtils.getDataAtual();
            if (rfc.getDhEmissao() != null) {
                data = rfc.getDhEmissao().toYearMonthDay();
            } else if (rfc.getPendencia() != null) {
                data = rfc.getPendencia().getOcorrencia().getDhInclusao().toYearMonthDay();
            }

            calcularInssService.estornoInssPessoaFisica(
                    rfc.getProprietario().getIdProprietario(),
                    data,
                    rfc.getVlBruto(),
                    rfc.getVlInss()
            );
        }

        calculoReciboIRRFService.executeEstornoReciboIRRF(rfc);

        descontoRfcService.cancelarParcela(rfc);

        super.store(rfc);

        if (rfc.getTpReciboFreteCarreteiro().getValue().equals(VIAGEM)) {
            ControleCarga controleCarga = rfc.getControleCarga();
            if (controleCarga != null) {
                this.gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga());
            }
        }

        return rfc;
    }

    /**
     * Atualiza o valor líquido de um recibo.
     *
     * @param idRecibo
     */
    public void storeUpdateVlLiquido(Long idRecibo) {
        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO()
                .getAdsmHibernateTemplate().get(ReciboFreteCarreteiro.class, idRecibo);

        BigDecimal newLiquido = this.findValorLiquidoRecibo(rfc);
        rfc.setVlLiquido(newLiquido);

        BigDecimal newDesconto = this.getSomaValoresDescontos(idRecibo);
        rfc.setVlDesconto(newDesconto);

        super.store(rfc);
    }

    /**
     * Encontra todos os recibos complementares do Recibo recebido como parâmetro.
     *
     * @param idReciboFreteCarreteiro
     * @return lista de pojos <b>ReciboFreteCarreteiro</b>.
     */
    public List findRecibosComplementares(Long idReciboFreteCarreteiro) {
        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, Object> reciboFreteCarreteiro = new HashMap<String, Object>();
        criteria.put(RECIBO_COMPLEMENTADO, reciboFreteCarreteiro);
        reciboFreteCarreteiro.put(ID_RECIBO_FRETE_CARRETEIRO, idReciboFreteCarreteiro);

        return super.find(criteria);
    }

    public DateTime findCCDhEvento(Long idControleCarga) {
        Map<String, Object> criteria = new HashMap<String, Object>();
        Map<String, Object> controleCarga = new HashMap<String, Object>();
        criteria.put(CONTROLE_CARGA, controleCarga);

        controleCarga.put(ID_CONTROLE_CARGA, idControleCarga);
        criteria.put(TP_EVENTO_CONTROLE_CARGA, "EM");

        List<EventoControleCarga> lResult = eventoControleCargaService.find(criteria);
        if (lResult == null || lResult.isEmpty())
            return null;

        return (lResult.get(0)).getDhEvento();
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    @Override
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga vérias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @Override
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrério.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    @Override
    public java.io.Serializable store(ReciboFreteCarreteiro bean) {
        return super.store(bean);
    }


    /**
     * Utilizar o metodo storeReciboComplementar. <B>(frontEnd)</B>
     *
     * @param rfc
     * @return
     * @see #storeReciboComplementar(ReciboFreteCarreteiro, List)
     */
    public java.io.Serializable storeComplementar(ReciboFreteCarreteiro rfc) {
        if (CompareUtils.le(rfc.getVlBruto(), BigDecimalUtils.ZERO))
            throw new BusinessException("LMS-24011");

        ReciboFreteCarreteiro rfcComplementado = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getHibernateTemplate()
                .get(ReciboFreteCarreteiro.class, rfc.getReciboComplementado().getIdReciboFreteCarreteiro());
        YearMonthDay dtPagto = rfcComplementado.getDtPagtoReal();
        if (dtPagto != null) {
            YearMonthDay dtBase = JTDateTimeUtils.getDataAtual();
            dtBase = dtBase.minusMonths(1);

            if (dtPagto.isBefore(dtBase)) {
                throw new BusinessException("LMS-24012");
            }
        }

        if (rfc.getIdReciboFreteCarreteiro() == null) {
            return this.generateReciboFreteCarreteiro(rfc, "CO");
        } else {
            ControleCarga controleCarga = rfc.getControleCarga();
            String tpRecibo = rfc.getTpReciboFreteCarreteiro().getValue();
            if (tpRecibo.equals(VIAGEM) && controleCarga != null) {
                gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga());
            }
        }

        ReciboFreteCarreteiro rfcUpdate = populateUpdate(rfc);

        super.store(rfcUpdate);

        return rfcUpdate;
    }

    public ReciboFreteCarreteiro populateUpdate(ReciboFreteCarreteiro rfc) {
        ReciboFreteCarreteiro rfcUpdate = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getHibernateTemplate().get(ReciboFreteCarreteiro.class, rfc.getIdReciboFreteCarreteiro());

        rfcUpdate.setVlBruto(rfc.getVlBruto());
        rfcUpdate.setVlLiquido(this.findValorLiquidoRecibo(rfcUpdate));
        rfcUpdate.setNrNfCarreteiro(rfc.getNrNfCarreteiro());
        rfcUpdate.setObReciboFreteCarreteiro(rfc.getObReciboFreteCarreteiro());

        if (rfc.getTpSituacaoRecibo() != null) {
            rfcUpdate.setTpSituacaoRecibo(rfc.getTpSituacaoRecibo());
        }

        if (rfc.getTpSituacaoWorkflow() != null) {
            rfcUpdate.setTpSituacaoWorkflow(rfc.getTpSituacaoWorkflow());
        }

        if (rfc.getPendencia() != null) {
            rfcUpdate.setPendencia(rfc.getPendencia());
        }
        if (rfc.getMoedaPais() != null) {
            rfcUpdate.setMoedaPais(rfc.getMoedaPais());
        }

        if (rfc.getDtProgramadaPagto() != null) {
            rfcUpdate.setDtProgramadaPagto(rfc.getDtProgramadaPagto());
        }

        return rfcUpdate;
    }

    /**
     * Cria um novo recibo do frete carreteiro.
     * <p>
     * <br> Os atributos não listados serão desconsiderados. Os que estão em negrito séo obrigatórios.
     * Caso não forem informados será lançado uma <b>IllegalArgumentException</b>.
     * <ul>
     * <li><b>filial.idFilial</b></li>
     * <li><b>tpRecibo</b></li>
     * <li><b>moedaPais.idMoedaPais</b></li>
     * <li><b>vlBruto</b></li>
     * <li><b>blAdiantamento</b></li>
     * <li><b>proprietario.idProprietario</b></li>
     * <li><b>motorista.idMotorista</b></li>
     * <li><b>meioTransporteRodoviario.idMeioTransporte</b></li>
     * <li>controleCarga.idControleCarga</li>
     * <li>vlPremio</li>
     * <li>vlPostoPassagem</li>
     * <li>reciboComplementado.idReciboComplementado</li>
     * <li>obReciboFreteCarreteiro</li>
     * <li>nrNfCarreteiro</li>
     * <li>pcAdiantamentoFrete</li>
     * </ul>
     *
     * @author Felipe Ferreita
     * @since 12-01-2006
     */
    public java.io.Serializable generateReciboFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiroOriginal, String tpReciboFrete) {
        Filial filial = reciboFreteCarreteiroOriginal.getFilial();
        MoedaPais moedaPais = reciboFreteCarreteiroOriginal.getMoedaPais();
        Proprietario proprietario = reciboFreteCarreteiroOriginal.getProprietario();
        Motorista motorista = reciboFreteCarreteiroOriginal.getMotorista();
        MeioTransporteRodoviario meioTransporteRodoviario = reciboFreteCarreteiroOriginal.getMeioTransporteRodoviario();

        validaCamposObrigatorios(reciboFreteCarreteiroOriginal, filial, moedaPais, proprietario, meioTransporteRodoviario);

        ReciboFreteCarreteiro rfc = new ReciboFreteCarreteiro();
        // cópia do bean recebido como argumento.
        rfc.setFilial(filial);

        Long nrRecibo;

        if (reciboFreteCarreteiroOriginal.getTpReciboFreteCarreteiro().getValue().equals(VIAGEM)) {
            nrRecibo = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_RECIBO_FRETE_VI", true);
        } else if ("P".equals(reciboFreteCarreteiroOriginal.getTpReciboFreteCarreteiro().getValue())) {
            nrRecibo = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_RECIBO_FRETE_PU", true);
        } else {
            nrRecibo = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_RECIBO_FRETE_CE", true);
        }

        rfc.setNrReciboFreteCarreteiro(nrRecibo);
        rfc.setTpReciboFreteCarreteiro(reciboFreteCarreteiroOriginal.getTpReciboFreteCarreteiro());
        rfc.setMoedaPais(moedaPais);
        rfc.setVlBruto(reciboFreteCarreteiroOriginal.getVlBruto());
        rfc.setBlAdiantamento(reciboFreteCarreteiroOriginal.getBlAdiantamento());
        rfc.setDtProgramadaPagto(reciboFreteCarreteiroOriginal.getDtProgramadaPagto());

        rfc.setProprietario(proprietario);
        // Para Recibo Complementar o Motorista deve ficar null.
        if ("CA".equals(tpReciboFrete)) {
            rfc.setMotorista(motorista);
        } else {
            rfc.setMotorista(null);
        }
        rfc.setMeioTransporteRodoviario(meioTransporteRodoviario);

        rfc.setBeneficiario(null);

        rfc.setControleCarga(reciboFreteCarreteiroOriginal.getControleCarga());
        rfc.setVlPremio(reciboFreteCarreteiroOriginal.getVlPremio());
        rfc.setVlPostoPassagem(reciboFreteCarreteiroOriginal.getVlPostoPassagem());
        rfc.setReciboComplementado(reciboFreteCarreteiroOriginal.getReciboComplementado());
        rfc.setRecibosComplementares(reciboFreteCarreteiroOriginal.getRecibosComplementares());
        rfc.setObReciboFreteCarreteiro(reciboFreteCarreteiroOriginal.getObReciboFreteCarreteiro());
        rfc.setNrNfCarreteiro(reciboFreteCarreteiroOriginal.getNrNfCarreteiro());
        rfc.setPcAdiantamentoFrete(reciboFreteCarreteiroOriginal.getPcAdiantamentoFrete());
        rfc.setManifestoViagemNacional(reciboFreteCarreteiroOriginal.getManifestoViagemNacional());

        // Seta situação como 'Gerado'. é realizado um find para retornar já o valor do doménio.
        Domain d = domainService.findByName(DM_STATUS_RECIBO_PAGAMENTO_FRETE_CARRETE);
        DomainValue dvTpSituacaoRecibo = d.findDomainValueByValue("GE");
        rfc.setTpSituacaoRecibo(dvTpSituacaoRecibo);

        // Atualiza vlLiquido dos recibos do mesmo controle de carga que não estão cancelados e que não séo de adiantamento.
        ControleCarga controleCarga = rfc.getControleCarga();
        if (rfc.getBlAdiantamento().equals(Boolean.FALSE)
                && controleCarga != null
                && controleCarga.getIdControleCarga() != null) {
            List<ReciboFreteCarreteiro> recibosComplementares = findRecibosNaoCancelados(rfc, Boolean.FALSE);
            for (ReciboFreteCarreteiro rfcAux : recibosComplementares) {
                rfcAux.setVlLiquido(this.findValorLiquidoRecibo(rfcAux));
                super.store(rfcAux);
            }
        }

        rfc.setVlLiquido(this.findValorLiquidoRecibo(rfc));

        rfc.setNotaCreditos(reciboFreteCarreteiroOriginal.getNotaCreditos());
        List<NotaCredito> notasCredito = reciboFreteCarreteiroOriginal.getNotaCreditos();
        if (notasCredito != null) {
            for (NotaCredito notaCredito : notasCredito) {
                notaCredito.setReciboFreteCarreteiro(rfc);
            }
        }

        rfc.setManifestoViagemNacional(reciboFreteCarreteiroOriginal.getManifestoViagemNacional());
        rfc.setFilialDestino(reciboFreteCarreteiroOriginal.getFilialDestino());

        rfc.setIdReciboFreteCarreteiro((Long) super.store(rfc));

        return rfc;
    }

    private void validaCamposObrigatorios(ReciboFreteCarreteiro reciboFreteCarreteiroOriginal, Filial filial, MoedaPais moedaPais, Proprietario proprietario, MeioTransporteRodoviario meioTransporteRodoviario) {
        StringBuilder campos = new StringBuilder();
        if (verificarExistenciaFilial(filial)) {
            campos.append("filial,");
        }
        if(reciboFreteCarreteiroOriginal.getTpReciboFreteCarreteiro() == null){
            campos.append("tpReciboFreteCarreitero,");
        }
        if(verificarExistenciaMoeda(moedaPais)){
                campos.append("moedaPais,");
        }
        if(reciboFreteCarreteiroOriginal.getVlBruto() == null){
            campos.append("vlBruto,");
        }
        if(reciboFreteCarreteiroOriginal.getBlAdiantamento() == null){
            campos.append("blAdiantamento,");
        }
        if(verificarExistenciaProprietario(proprietario)){
            campos.append("proprietario,");
        }
        if(verificarExistenciaMeioTransporte(meioTransporteRodoviario)){
            campos.append("meioTransporte,");
        }
        if(campos.length()>0){
            throw new IllegalArgumentException("O método 'generateReciboFreteCarreteiro' possui parâmetros obrigatórios não informados. Os campos são:{"+
                    campos.toString().substring(0,campos.length()-1)
                    +"}.");
        }

    }

    private boolean verificarExistenciaMeioTransporte(MeioTransporteRodoviario meioTransporteRodoviario) {
        return meioTransporteRodoviario == null || meioTransporteRodoviario.getIdMeioTransporte() == null;
    }

    private boolean verificarExistenciaProprietario(Proprietario proprietario) {
        return proprietario == null || proprietario.getIdProprietario() == null;
    }

    private boolean verificarExistenciaMoeda(MoedaPais moedaPais) {
        return moedaPais == null || moedaPais.getIdMoedaPais() == null;
    }

    private boolean verificarExistenciaFilial(Filial filial) {
        return filial == null || filial.getIdFilial() == null;
    }

    /**
     * Encontra valor líquido de um recibo.
     *
     * @param idRecibo Caso não for informado será lançado uma <b>IllegalArgumentException</b>.
     * @return BigDecimal valor líquido do recibo.
     * @author Felipe Ferreira
     * @since 12-01-2006
     */
    public BigDecimal findValorLiquidoRecibo(Long idRecibo) {
        if (idRecibo == null)
            throw new IllegalArgumentException("Deve ser informado o idRecibo obrigatoriamente.");
        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getHibernateTemplate()
                .get(ReciboFreteCarreteiro.class, idRecibo);

        return this.findValorLiquidoRecibo(rfc);
    }

    /**
     * Encontra valor líquido de um recibo.
     *
     * @param rfc ReciboFreteCarreteiro
     * @return BigDecimal valor líquido do recibo.
     * @author Felipe Ferreita
     * @since 12-01-2006
     */
    public BigDecimal findValorLiquidoRecibo(ReciboFreteCarreteiro rfc) {
        if (rfc == null)
            throw new IllegalArgumentException("Deve ser informado o recibo obrigatoriamente.");

        BigDecimal vlAdiantamentos = BigDecimal.ZERO;
        if (rfc.getReciboComplementado() == null) {
            vlAdiantamentos = this.findValorAdiantamento(rfc);
        }

        BigDecimal vlDescontos = this.getSomaValoresDescontos(rfc.getIdReciboFreteCarreteiro());

        BigDecimal sum = BigDecimalUtils.ZERO;

        sum = sum.add(rfc.getVlBruto().subtract(vlAdiantamentos).subtract(vlDescontos));

        if (rfc.getVlPremio() != null)
            sum = sum.add(rfc.getVlPremio());
        if (rfc.getVlDiaria() != null)
            sum = sum.add(rfc.getVlDiaria());
        if (rfc.getVlInss() != null)
            sum = sum.subtract(rfc.getVlInss());
        if (rfc.getVlIrrf() != null)
            sum = sum.subtract(rfc.getVlIrrf());
        if (rfc.getVlIssqn() != null)
            sum = sum.add(rfc.getVlIssqn());

        return sum;
    }

    /**
     * Encontra recibos com mesmo Controle de Carga e Proprietério do recibo recebido por parâmetro.<br>
     * Considera apenas registros com blAdiantamento recebido, e que não estejam cancelados.<br>
     * Se o pojo possuir idReciboFreteCarreteiro, este será ignorado na consulta.<br>
     * Se não houver um controle de carga e um proprietario associados, será retornado <b>null</b>.
     *
     * @param rfc            pojo de ReciboFreteCarreteiro
     * @param blAdiantamento
     * @return List de ReciboFreteCarreteiro
     * @author Felipe Ferreita
     * @since 12-01-2006
     */
    public List<ReciboFreteCarreteiro> findRecibosNaoCancelados(ReciboFreteCarreteiro rfc, Boolean blAdiantamento) {
        Long idControleCarga = rfc.getControleCarga().getIdControleCarga();
        Long idProprietario = rfc.getProprietario().getIdProprietario();

        if (idControleCarga == null && idProprietario == null)
            return null;

        return getReciboFreteCarreteiroDAO().findRecibosNaoCancelados(
                idControleCarga,
                idProprietario,
                rfc.getIdReciboFreteCarreteiro(),
                rfc.getManifestoViagemNacional().getIdManifestoViagemNacional(),
                blAdiantamento
        );
    }

    /**
     * Soma valores do adiantamento.
     *
     * @param recibosFreteCarreteiro
     * @return BigDecimal
     */
    private BigDecimal somaValoresAdiantamento(List<ReciboFreteCarreteiro> recibosFreteCarreteiro) {
        BigDecimal sum = BigDecimalUtils.ZERO;
        for (ReciboFreteCarreteiro reciboFreteCarreteiro : recibosFreteCarreteiro) {
            sum = sum.add(reciboFreteCarreteiro.getVlBruto());
        }
        return sum;
    }

    public BigDecimal findValorAdiantamento(ReciboFreteCarreteiro rfc) {
        ControleCarga ccRecibo = rfc.getControleCarga();
        if (ccRecibo != null && ccRecibo.getIdControleCarga() != null) {
            List<ReciboFreteCarreteiro> lAdiantamentos = this.findRecibosNaoCancelados(rfc, Boolean.TRUE);
            return this.somaValoresAdiantamento(lAdiantamentos);
        }
        return BigDecimalUtils.ZERO;
    }

    /**
     * Soma valores dos Descontos do recibo.
     *
     * @param idRecibo
     * @return
     */
    public BigDecimal getSomaValoresDescontos(Long idRecibo) {
        if (idRecibo == null)
            return BigDecimalUtils.ZERO;

        Map<String, Object> ocorrenciaCriteria = new HashMap<String, Object>();
        ocorrenciaCriteria.put(TP_OCORRENCIA, "D");
        ocorrenciaCriteria.put(TP_SITUACAO, "A");
        ocorrenciaCriteria.put(BL_DESCONTO_CANCELADO, "N");

        Map<String, Object> recibo = new HashMap<String, Object>();
        recibo.put(ID_RECIBO_FRETE_CARRETEIRO, idRecibo);
        ocorrenciaCriteria.put(RECIBO_FRETE_CARRETEIRO, recibo);

        BigDecimal sum = BigDecimalUtils.ZERO;
        List<OcorrenciaFreteCarreteiro> ocorrenciasFreteCarreteiro = ocorrenciaFreteCarreteiroService.find(ocorrenciaCriteria);
        for (OcorrenciaFreteCarreteiro ocorrenciaFreteCarreteiro : ocorrenciasFreteCarreteiro) {
            sum = sum.add(ocorrenciaFreteCarreteiro.getVlDesconto());
            getReciboFreteCarreteiroDAO().getHibernateTemplate().evict(ocorrenciaFreteCarreteiro);
        }
        return sum;
    }

    /**
     * Retorna consulta paginada.
     *
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedComplementar(TypedFlatMap criteria) {
        ResultSetPage rsp = getReciboFreteCarreteiroDAO().findPaginatedComplementar(criteria, FindDefinition.createFindDefinition(criteria));
        List<ReciboFreteCarreteiro> recibosFreteCarreteiro = rsp.getList();
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>(recibosFreteCarreteiro.size());

        for (ReciboFreteCarreteiro rfc : recibosFreteCarreteiro) {
            TypedFlatMap newRow = new TypedFlatMap();
            newRow.put(ID_RECIBO_FRETE_CARRETEIRO, rfc.getIdReciboFreteCarreteiro());
            newRow.put(FILIAL_SG_FILIAL, rfc.getFilial().getSgFilial());
            newRow.put(NR_RECIBO_FRETE_CARRETEIRO, rfc.getNrReciboFreteCarreteiro());
            newRow.put(RECIBO_FRETE_CARRETEIRO_NR_RECIBO_FRETE_CARRETEIRO, rfc.getReciboComplementado().getNrReciboFreteCarreteiro());
            newRow.put(RECIBO_FRETE_CARRETEIRO_FILIAL_SG_FILIAL, rfc.getReciboComplementado().getFilial().getSgFilial());
            newRow.put(DH_EMISSAO, rfc.getDhEmissao());
            newRow.put(TP_SITUACAO_RECIBO, rfc.getTpSituacaoRecibo());
            newRow.put(BL_ADIANTAMENTO, rfc.getBlAdiantamento());
            newRow.put(TP_RECIBO_FRETE_CARRETEIRO, rfc.getTpReciboFreteCarreteiro().getDescription().getValue());
            Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
            DomainValue tpIdentificacaoProprietario = proprietarioPessoa.getTpIdentificacao();
            if (tpIdentificacaoProprietario != null) {
                newRow.put(PROPRIETARIO_TP_IDENTIFICACAO, tpIdentificacaoProprietario.getDescription());
                newRow.put(PROPRIETARIO_NM_PESSOA, proprietarioPessoa.getNmPessoa());
                String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(
                        tpIdentificacaoProprietario, proprietarioPessoa.getNrIdentificacao());
                newRow.put(PROPRIETARIO_NR_IDENTIFICACAO, nrIdentificacaoProprietario);
            }

            MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
            newRow.put(MEIO_TRANSPORTE_NR_FROTA, meioTransporte.getNrFrota());
            newRow.put(MEIO_TRANSPORTE_NR_IDENTIFICADOR, meioTransporte.getNrIdentificador());

            ControleCarga controleCarga = rfc.getControleCarga();
            if (controleCarga != null) {
                newRow.put(CONTROLE_CARGA_SG_FILIAL_ORIGEM, controleCarga.getFilialByIdFilialOrigem().getSgFilial());
                newRow.put(CONTROLE_CARGA_NR_CONTROLE_CARGA, controleCarga.getNrControleCarga());
            }
            newRow.put(DT_PGTO_REAL, rfc.getDtPagtoReal());

            if (rfc.getVlLiquido() != null) {
                newRow.put(DS_MOEDA_01, rfc.getMoedaPais().getMoeda().getSiglaSimbolo());
                newRow.put(VL_LIQUIDO, rfc.getVlLiquido());
            }
            newList.add(newRow);
        }
        rsp.setList(newList);

        return rsp;
    }

    /**
     * Retorna total de registros para paginaééo.
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountComplementar(TypedFlatMap criteria) {
        return getReciboFreteCarreteiroDAO().getRowCountComplementar(criteria);
    }

    public List findContribuicoesOutrasFontes(Long idProprietario, Long idReciboExclusivo) {
        List l = descontoInssCarreteiroService.findByProprietario(idProprietario, JTDateTimeUtils.getDataAtual());
        List l2 = this.findByProprietarioAndSituacao(idProprietario, "EM", JTDateTimeUtils.getDataAtual(), idReciboExclusivo);

        l.addAll(l2);

        return l;
    }

//##############################################################################################################
//	Méotodos responséveis pela emisséo de recibos.
//##############################################################################################################

    /**
     * Validaééo pré-emisséo do recibo de coleta entrega.
     *
     * @param parameters
     * @return TypedFlatMap
     */
    public TypedFlatMap storeValidateEmissaoRecibo(TypedFlatMap parameters) {
        Long idRecibo = parameters.getLong(RECIBO_FRETE_CARRETEIRO_ID_RECIBO_FRETE_CARRETEIRO);
        YearMonthDay dtEmissaoInicial = parameters.getYearMonthDay(DT_EMISSAO_INICIAL);
        YearMonthDay dtEmissaoFinal = parameters.getYearMonthDay(DT_EMISSAO_FINAL);

        List<Long> recibos;
        StringBuilder nrRecibosGerados = new StringBuilder();
        if (idRecibo == null && dtEmissaoInicial == null && dtEmissaoFinal == null) {

            validateControleCarga(parameters);

            if (!notaCreditoService.validateNotaCreditoNaoEmitida(parameters.getLong(FILIAL_ID_FILIAL), parameters.getLong(PROPRIETARIO_ID_PROPRIETARIO), parameters.getLong(MEIO_TRANSPORTE_RODOVIARIO_ID_MEIO_TRANSPORTE))) {
                throw new BusinessException("LMS-25062");
            } else if (!notaCreditoService.validateNotaCreditoNaoEmitidaPadrao(parameters.getLong(FILIAL_ID_FILIAL), parameters.getLong(PROPRIETARIO_ID_PROPRIETARIO), parameters.getLong(MEIO_TRANSPORTE_RODOVIARIO_ID_MEIO_TRANSPORTE))) {
                throw new BusinessException("LMS-25062");
            }

            recibos = carregarRecibos(parameters, nrRecibosGerados);


        } else {
            parameters.put(RECIBO_FRETE_CARRETEIRO_TP_RECIBO_FRETE_CARRETEIRO, COLETA_ENTREGA);
            parameters.put(RECIBO_FRETE_CARRETEIRO_TP_SITUACAO_RECIBO, "AJ");
            recibos = this.findRecibosToReport(parameters);
        }

        TypedFlatMap m = new TypedFlatMap();

        if (nrRecibosGerados.length() != 0) {
            m.put(NR_RECIBOS, nrRecibosGerados.toString());
        }


        List<Long> idsReciboParaCIOT = new ArrayList<Long>();
        for (Long idReciboIteracao : recibos) {
            boolean isReemissao = this.storeValidateEmissaoReciboColetaEntrega(idReciboIteracao);
            m.put(idReciboIteracao, new Object[]{isReemissao});

            if (Boolean.FALSE.equals(isReemissao)) {
                idsReciboParaCIOT.add(idReciboIteracao);
            }
        }

        if (!idsReciboParaCIOT.isEmpty()) {
            CIOTBuscaIntegracaoPUDDTO ciotDTO = new CIOTBuscaIntegracaoPUDDTO();
            ciotDTO.setIdsReciboParaCIOT(idsReciboParaCIOT);
            JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.CIOT_BUSCA_DADOS_ALTERACAO_PUD, ciotDTO);
            integracaoJmsService.storeMessage(jmsMessageSender);
        }

        return m;
    }

    private List<Long> carregarRecibos(TypedFlatMap parameters, StringBuilder nrRecibosGerados) {
        List<Long> recibos;
        recibos = this.storeReciboByNotasCredito(parameters);

        for (Long id : recibos) {
            ReciboFreteCarreteiro recibofreteCarreteiro = findByIdCustom(id);
            executeWorkflowReciboFreteCarreteiroColetaEntregaService.executeWorkflowPendencia(recibofreteCarreteiro);
            if (nrRecibosGerados.length() == 0) {
                nrRecibosGerados.append(String.valueOf(recibofreteCarreteiro.getNrReciboFreteCarreteiro()));
            } else {
                nrRecibosGerados.append(",".concat(String.valueOf(recibofreteCarreteiro.getNrReciboFreteCarreteiro())));
            }
        }
        return recibos;
    }

    private void validateControleCarga(TypedFlatMap parameters) {
        BigDecimal periodoMaximoEmissaoRecibo = (BigDecimal) configuracoesFacade.getValorParametro(PERIODO_MAX_EMISSAO_RECIBO);
        YearMonthDay dataRetroativaMaxima = new YearMonthDay();
        dataRetroativaMaxima = dataRetroativaMaxima.minusDays(periodoMaximoEmissaoRecibo.intValue());

        BigDecimal periodoMinimoEmissaoRecibo = (BigDecimal) configuracoesFacade.getValorParametro(DIA_SEMANA_EMISSAO_RECIBO);
        YearMonthDay dataRetroativaMinima = JTDateTimeUtils.getLastDay(new YearMonthDay(), periodoMinimoEmissaoRecibo.intValue());

        controleCargaService.validateControleCargaSemNotaCredito(parameters.getLong(FILIAL_ID_FILIAL), parameters.getLong(PROPRIETARIO_ID_PROPRIETARIO), parameters.getLong(MEIO_TRANSPORTE_RODOVIARIO_ID_MEIO_TRANSPORTE), dataRetroativaMinima, dataRetroativaMaxima);

        parameters.put(DATA_RETROATIVA_MINIMA, dataRetroativaMinima);
        parameters.put(DATA_RETROATIVA_MAXIMA, dataRetroativaMaxima);
    }

    public List<Map<String, Object>> findDadosReciboPUDParaCIOT(Long idReciboFreteCarreteiro) {
        return getReciboFreteCarreteiroDAO().findDadosReciboPUDParaCIOT(idReciboFreteCarreteiro);
    }

    /**
     * Consulta ids de recibos a partir de alguns critérios.
     *
     * @param criteria
     * @return List de Object[] com os ids dos Recibos.
     */
    public List findRecibosToReport(TypedFlatMap criteria) {
        return getReciboFreteCarreteiroDAO().findRecibosToReport(criteria);
    }

    public boolean storeValidateEmissaoReciboViagem(Long idRecibo) {
        boolean blReemissao = false;

        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate()
                .get(ReciboFreteCarreteiro.class, idRecibo);
        ControleCarga cc = rfc.getControleCarga();

        if ("CA".equals(rfc.getTpSituacaoRecibo().getValue())) {
            throw new BusinessException("LMS-24008");
        }

        //para garantir que existe valor
        if (rfc.getBlAdiantamento() != null && !rfc.getBlAdiantamento()) {
            boolean blLms24021 = true;
            if (cc != null) {
                blLms24021 = false;
                String tpStatusControleCarga = cc.getTpStatusControleCarga().getValue();
                if (!"FE".equals(tpStatusControleCarga) && !"ED".equals(tpStatusControleCarga)) {

                    String tpSituacaoRecibo = rfc.getTpSituacaoRecibo().getValue();
                    if ("BL".equals(tpSituacaoRecibo)) {
                        blLms24021 = true;
                    }
                }
            }

            if (blLms24021) {
                throw new BusinessException("LMS-24021");
            }
        } //fim if

        if (cc != null && "FE".equalsIgnoreCase(cc.getTpStatusControleCarga().getValue())) {
            gerarRateioFreteCarreteiroService.execute(cc.getIdControleCarga());
        }
        rfc = validateProprietarioBeneficiarioEmissaoRecibo(rfc);

        Proprietario proprietario = rfc.getProprietario();


        if (rfc.getDhEmissao() == null) {
            // se proprietário é pessoa física e o recibo não for de adiantamento:
            calculaValoresPessoaFisica(rfc, proprietario);

            // REGRA 1.4
            BigDecimal vlLiquido = findValorLiquidoRecibo(rfc);
            rfc.setVlLiquido(vlLiquido);
        }

        // REGRA 1.3
        if (rfc.getDhEmissao() == null) {
            rfc.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
            DomainValue dv = new DomainValue("EM");
            rfc.setTpSituacaoRecibo(dv);

            calculoReciboIRRFService.executeCalcularReciboIRRF(rfc, Boolean.TRUE);

            super.store(rfc);
        } else {
            blReemissao = true;
        }

        return blReemissao;
    }

    private void calculaValoresPessoaFisica(ReciboFreteCarreteiro rfc, Proprietario proprietario) {
        if ("F".equals(proprietario.getPessoa().getTpPessoa().getValue()) && !rfc.getBlAdiantamento()) {
            aplicaInss(rfc);
            rfc.setPcAliquotaIrrf(BigDecimal.valueOf(0));
            rfc.setVlIrrf(BigDecimal.valueOf(0));

        }
    }

    /**
     * @param idRecibo
     * @return
     */
    public boolean storeValidateEmissaoReciboColetaEntrega(Long idRecibo) {
        boolean blReemissao = false;

        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate()
                .get(ReciboFreteCarreteiro.class, idRecibo);

        rfc = validateProprietarioBeneficiarioEmissaoRecibo(rfc);

        Proprietario proprietario = (Proprietario) getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate()
                .load(Proprietario.class, rfc.getProprietario().getIdProprietario());
        EnderecoPessoa epProprietario = validateEnderecoPropEmissaoRecibo(proprietario);

        if (rfc.getDhEmissao() == null) {
            //se proprietério é pessoa jurédica:
            if ("J".equals(proprietario.getPessoa().getTpPessoa().getValue())) {
                EnderecoPessoa epFilial = enderecoPessoaService.findEnderecoPessoaPadrao(rfc.getFilial().getIdFilial());

                Map resultCalc = calcularIssService.calcularIssCarreteiro(
                        rfc.getProprietario().getIdProprietario(),
                        epProprietario.getMunicipio().getIdMunicipio(),
                        epFilial.getMunicipio().getIdMunicipio(),
                        ((BigDecimal) configuracoesFacade.getValorParametro(ID_SERVICO_TRIBUTO_CARRETEIRO)).longValue(),
                        JTDateTimeUtils.getDataAtual(),
                        rfc.getVlBruto()
                );

                if (resultCalc != null && resultCalc.get(PC_ALIQUOTA_ISS) != null) {
                    rfc.setPcAliquotaIssqn((BigDecimal) resultCalc.get(PC_ALIQUOTA_ISS));
                }
                if (resultCalc != null && resultCalc.get(VL_ISS) != null) {
                    rfc.setVlIssqn((BigDecimal) resultCalc.get(VL_ISS));
                }
                // se proprietério é pessoa fésica:
            } else {
                //LMSA-2152
                rfc.setPcAliquotaInss(BigDecimal.ZERO);
                rfc.setVlSalarioContribuicao(BigDecimal.ZERO);
                rfc.setVlInss(BigDecimal.ZERO);
                rfc.setVlOutrasFontes(BigDecimal.ZERO);
                rfc.setPcAliquotaIrrf(BigDecimal.valueOf(0));
                rfc.setVlIrrf(BigDecimal.valueOf(0));
            }

            // REGRA 2.8
            BigDecimal vlLiquido = findValorLiquidoRecibo(rfc);
            rfc.setVlLiquido(vlLiquido);

            ParcelaDescontoRfc parcela = descontoRfcService.storeAplicarAjuste(rfc);

            if (parcela != null) {
                TipoDescontoRfc tipoDescontoRfc = parcela.getDescontoRfc().getTipoDescontoRfc();
                String obReciboFreteCarreteiro = rfc.getObReciboFreteCarreteiro() == null ? "" : rfc.getObReciboFreteCarreteiro();

                if (new DomainValue("S").equals(tipoDescontoRfc.getRecalculaInss())) {
                    rfc.setVlBruto(rfc.getVlBruto().subtract(parcela.getVlParcela()));

                    rfc.setVlLiquido(this.findValorLiquidoRecibo(rfc));

                    rfc.setObReciboFreteCarreteiro(obReciboFreteCarreteiro.concat(DESCONTO_CONCEDIDO_REFERENTE_A).concat(DESCONTO_OPERACIONAL));

                } else {
                    if (new DomainValue("S").equals(tipoDescontoRfc.getBlAbateRecibo())) {
                        if (rfc.getVlDesconto() != null) {
                            rfc.setVlDesconto(rfc.getVlDesconto().add(parcela.getVlParcela()));
                        } else {
                            rfc.setVlDesconto(parcela.getVlParcela());
                        }

                        rfc.setVlLiquido(rfc.getVlLiquido().subtract(parcela.getVlParcela()));
                        rfc.setObReciboFreteCarreteiro(obReciboFreteCarreteiro.concat(DESCONTO_CONCEDIDO_REFERENTE_A).concat(tipoDescontoRfc.getDsTipoDescontoRfc()));
                    } else {
                        DecimalFormat decimalFmt = new DecimalFormat("#,##0.00;-#,##0.00");
                        String complemento = " - Parcela " + parcela.getNrParcela() + " - " + decimalFmt.format(parcela.getVlParcela());

                        rfc.setObReciboFreteCarreteiro(obReciboFreteCarreteiro.concat(DESCONTO_VINCULADO_REFERENTE_A).concat(tipoDescontoRfc.getDsTipoDescontoRfc()).concat(complemento));
                    }

                }

                descontoRfcService.vincularRecibo(parcela, rfc);

            }

        }

        // REGRA 1.3
        if (rfc.getDhEmissao() == null) {
            if (!COLETA_ENTREGA.equals(rfc.getTpReciboFreteCarreteiro().getValue()) || rfc.getReciboComplementado() != null) {
                rfc.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
                DomainValue dv = new DomainValue("EM");
                rfc.setTpSituacaoRecibo(dv);
            }

            super.store(rfc);

            List<NotaCredito> notasCredito = rfc.getNotaCreditos();
            for (NotaCredito notaCredito : notasCredito) {
                eventoNotaCreditoService.storeEventoNotaCredito(notaCredito,
                        ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_RECIBO_FRETE_CARRETEIRO,
                        ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_EMITIDO);
            }
        } else {
            blReemissao = true;
        }

        return blReemissao;
    }

    /**
     * @param rfc
     */
    public ReciboFreteCarreteiro aplicaInss(ReciboFreteCarreteiro rfc) {
        if ("J".equals(rfc.getProprietario().getPessoa().getTpPessoa().getValue())) {
            return rfc;
        }

        Map<String, Object> resultCalc = calcularInssService.findValorInss(
                rfc.getProprietario().getIdProprietario(),
                JTDateTimeUtils.getDataAtual(),
                rfc.getVlBruto()
        );
        if (resultCalc != null) {
            if (resultCalc.get(PC_ALIQUOTA_INSS) != null)
                rfc.setPcAliquotaInss((BigDecimal) resultCalc.get(PC_ALIQUOTA_INSS));
            if (resultCalc.get(VL_SALARIO_BASE) != null)
                rfc.setVlSalarioContribuicao((BigDecimal) resultCalc.get(VL_SALARIO_BASE));
            if (resultCalc.get(VL_INSS) != null)
                rfc.setVlInss((BigDecimal) resultCalc.get(VL_INSS));
            if (resultCalc.get(VL_OUTRAS_FONTES) != null)
                rfc.setVlOutrasFontes((BigDecimal) resultCalc.get(VL_OUTRAS_FONTES));
        }
        BigDecimal valorLiquido = findValorLiquidoRecibo(rfc);
        rfc.setVlLiquido(valorLiquido);
        return rfc;
    }

    /**
     * Gera recibos para todas as notas de créditos que ainda não estão associadas a um.
     *
     * @param criteria
     * @return List de Long com ids dos RecibosGerados.
     */
    public List<Long> storeReciboByNotasCredito(TypedFlatMap criteria) {
        List<Long> recibosGerados = new ArrayList<Long>();

        criteria.put(IS_EMPRESA_PARCEIRA, true);
        List<Map<String, Object>> grupoNotasCredito = getReciboFreteCarreteiroDAO().findNotasCreditoToEmissaoColetaEntrega(criteria);

        criteria.put(IS_EMPRESA_PARCEIRA, false);
        grupoNotasCredito.addAll(getReciboFreteCarreteiroDAO().findNotasCreditoToEmissaoColetaEntrega(criteria));

        Iterator<Map<String, Object>> i = grupoNotasCredito.iterator();
        if (i.hasNext()) {
            Filial filial = new Filial();
            Long idFilial = criteria.getLong(FILIAL_ID_FILIAL);
            filial.setIdFilial(idFilial);
            EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(idFilial);
            Long idPais = ep.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
            MoedaPais moedaPais = moedaPaisService.findMoedaPaisMaisUtilizada(idPais);

            StringBuilder sbMensagem = new StringBuilder();

            do {
                Map<String, Object> dadosGrupo = i.next();
                Long idProprietarioGroup = (Long) dadosGrupo.get(ID_PROPRIETARIO);
                Long idMeioTransporteGroup = (Long) dadosGrupo.get(ID_MEIO_TRANSPORTE);

                criteria.put(IS_EMPRESA_PARCEIRA, true);
                List<NotaCredito> notasCredito = getReciboFreteCarreteiroDAO().findNotasCreditoToEmissaoColetaEntrega(idProprietarioGroup, idMeioTransporteGroup, idFilial, criteria);

                criteria.put(IS_EMPRESA_PARCEIRA, false);
                notasCredito.addAll(getReciboFreteCarreteiroDAO().findNotasCreditoToEmissaoColetaEntrega(idProprietarioGroup, idMeioTransporteGroup, idFilial, criteria));

                if (verificaBlAprovado(sbMensagem, dadosGrupo, notasCredito))
                    continue;

                ReciboFreteCarreteiro rfc = new ReciboFreteCarreteiro();

                Proprietario proprietario = new Proprietario();
                Motorista motorista = new Motorista();
                MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();

                rfc.setFilial(filial);
                rfc.setMoedaPais(moedaPais);
                rfc.setProprietario(proprietario);
                rfc.setMotorista(motorista);
                rfc.setMeioTransporteRodoviario(meioTransporteRodoviario);

                rfc.setTpReciboFreteCarreteiro(new DomainValue(COLETA_ENTREGA));
                rfc.setBlAdiantamento(Boolean.FALSE);

                proprietario.setIdProprietario(idProprietarioGroup);
                meioTransporteRodoviario.setIdMeioTransporte(idMeioTransporteGroup);
                Long idMotoristaGroup = (Long) dadosGrupo.get(ID_MOTORISTA);
                motorista.setIdMotorista(idMotoristaGroup);

                // Encontra valor bruto do recibo frete carreteiro a partir das notas de crédito.
                BigDecimal vlBruto = BigDecimalUtils.ZERO;
                for (NotaCredito notaCredito : notasCredito) {
                    BigDecimal vlNotaCredito = notaCreditoService.findValorTotalNotaCredito(notaCredito.getIdNotaCredito());
                    vlBruto = vlBruto.add(vlNotaCredito);
                }
                rfc.setVlBruto(vlBruto);

                BigDecimal vlPostoPassagem = getReciboFreteCarreteiroDAO()
                        .findVlPostoPassagemNotasCredito(idProprietarioGroup, idMeioTransporteGroup);
                if (vlPostoPassagem == null)
                    vlPostoPassagem = BigDecimalUtils.ZERO;
                rfc.setVlPostoPassagem(vlPostoPassagem);

                notasCredito = removeZeradas(notasCredito);

                if (notasCredito.isEmpty()) {
                    continue;
                }

                rfc.setNotaCreditos(notasCredito);

                ReciboFreteCarreteiro recibo = (ReciboFreteCarreteiro) this.generateReciboFreteCarreteiro(rfc, "CA");

                recibosGerados.add(recibo.getIdReciboFreteCarreteiro());

                for (NotaCredito notaCredito : notasCredito) {
                    eventoNotaCreditoService.storeEventoNotaCredito(notaCredito,
                            ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_RECIBO_FRETE_CARRETEIRO,
                            ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_GERADO);
                }

            } while (i.hasNext());

            if (sbMensagem.length() > 0) {
                SessionContext.set("LMS_25030_KEY", sbMensagem.append(",").toString());
            }
        }
        return recibosGerados;
    }

    private boolean verificaBlAprovado(StringBuilder sbMensagem, Map<String, Object> dadosGrupo, List<NotaCredito> notasCredito) {
        for (NotaCredito notaCredito : notasCredito) {
            DomainValue dvAprovacao = notaCredito.getTpSituacaoAprovacao();
            if (dvAprovacao != null && "S".equals(dvAprovacao.getValue())) {
                if (sbMensagem.length() > 0) {
                    sbMensagem.append(", ");
                }
                sbMensagem.append(dadosGrupo.get(NR_FROTA)).append(" - ").append(dadosGrupo.get(NR_IDENTIFICADOR));
                return true;
            }
        }
        return false;
    }

//##############################################################################################################

    private List<NotaCredito> removeZeradas(List<NotaCredito> notasCredito) {
        List<NotaCredito> notasRetorno = new ArrayList<NotaCredito>();
        for (NotaCredito notaCredito : notasCredito) {
            if (notaCredito.getVlTotal() == null || (notaCredito.getVlTotal().compareTo(BigDecimal.ZERO)) == 1) {
                notasRetorno.add(notaCredito);
            }
        }
        return notasRetorno;
    }

    /**
     * Retorna se recibo é adiantamento.
     *
     * @param idRecibo
     * @return
     */
    public Boolean validateIfBlAdiantamento(Long idRecibo) {
        if (idRecibo == null) {
            throw new IllegalArgumentException("o parâmetro idRecibo é obrigatório.");
        }

        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) getReciboFreteCarreteiroDAO()
                .getAdsmHibernateTemplate().load(ReciboFreteCarreteiro.class, idRecibo);

        return rfc.getBlAdiantamento();
    }

    private EnderecoPessoa validateEnderecoPropEmissaoRecibo(Proprietario proprietario) {
        return enderecoPessoaService.findEnderecoPessoaPadrao(proprietario.getIdProprietario());
    }

    private ReciboFreteCarreteiro validateProprietarioBeneficiarioEmissaoRecibo(ReciboFreteCarreteiro rfc) {
        if (rfc.getDhEmissao() == null) {

            boolean blPossuiContaBancaria;

            Pessoa pessoa = null;
            List<AdiantamentoTrecho> adiantamentoTrecho = null;
            if (rfc.getBlAdiantamento() && rfc.getIdReciboFreteCarreteiro() != null) {
                adiantamentoTrecho = adiantamentoTrechoService.findByIdControleCarga(rfc.getControleCarga().getIdControleCarga(), null, null);
            }
            if (adiantamentoTrecho != null && !adiantamentoTrecho.isEmpty() && adiantamentoTrecho.get(0).getPostoConveniado() != null) {
                pessoa = adiantamentoTrecho.get(0).getPostoConveniado().getPessoa();
            }

            Proprietario proprietario = (Proprietario) getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().get(Proprietario.class, rfc.getProprietario().getIdProprietario());
            if (pessoa == null) {
                pessoa = proprietario.getPessoa();
            }
            // continua como estava antes
            List contasBancarias = pessoa.getContaBancarias();

            blPossuiContaBancaria = verificarSeExisteContaBancaria(rfc, contasBancarias);
            blPossuiContaBancaria = verificarSeBeneficiarioPossuiContaBancaria(rfc, blPossuiContaBancaria, proprietario);

            if (!blPossuiContaBancaria)
                throw new BusinessException("LMS-24022");
        }
        return rfc;
    }

    private boolean verificarSeBeneficiarioPossuiContaBancaria(ReciboFreteCarreteiro rfc, boolean blPossuiContaBancaria, Proprietario proprietario) {
        List<ContaBancaria> contasBancarias;
        boolean blPossuiContaBancariaAux = blPossuiContaBancaria;
        if (!blPossuiContaBancaria) {
            List<BeneficiarioProprietario> beneficiarios = proprietario.getBeneficiarioProprietarios();
            Iterator<BeneficiarioProprietario> iBeneficiarios = beneficiarios.iterator();
            while (!blPossuiContaBancariaAux && iBeneficiarios.hasNext()) {
                BeneficiarioProprietario bp = iBeneficiarios.next();
                Beneficiario beneficiario = bp.getBeneficiario();
                Pessoa beneficiarioPessoa = beneficiario.getPessoa();
                contasBancarias = beneficiarioPessoa.getContaBancarias();
                blPossuiContaBancariaAux = verificarSeExisteContaBancaria(rfc, contasBancarias);
            }
        }
        return blPossuiContaBancariaAux;
    }

    private boolean verificarSeExisteContaBancaria(ReciboFreteCarreteiro rfc, List<ContaBancaria> contasBancarias) {
        for (ContaBancaria cbAux : contasBancarias) {
            Integer intVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(cbAux.getDtVigenciaInicial(), cbAux.getDtVigenciaFinal());
            if (intVigencia.equals(1)) {
                rfc.setContaBancaria(cbAux);
                return true;
            }
        }
        return false;
    }

    public List<ReciboFreteCarreteiro> findByProprietarioAndSituacao(Long idProprietario, String tpSituacaoRecibo, YearMonthDay dtBase, Long idReciboExclusivo) {
        return getReciboFreteCarreteiroDAO().findByProprietarioAndSituacao(idProprietario, tpSituacaoRecibo, dtBase, idReciboExclusivo);
    }

    /**
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
        ResultSetPage rsp = getReciboFreteCarreteiroDAO().findPaginatedByIdControleCarga(idControleCarga, findDefinition);
        List<ReciboFreteCarreteiro> result = new AliasToNestedBeanResultTransformer(ReciboFreteCarreteiro.class).transformListResult(rsp.getList());
        rsp.setList(result);
        return rsp;
    }

    /**
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountFindPaginatedByIdControleCarga(Long idControleCarga) {
        return getReciboFreteCarreteiroDAO().getRowCountFindPaginatedByIdControleCarga(idControleCarga);
    }

    /**
     * @param idReciboFreteCarreteiro
     * @return
     */
    public ReciboFreteCarreteiro findByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
        Map<String, Object> map = getReciboFreteCarreteiroDAO().findByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        result.add(map);
        List<ReciboFreteCarreteiro> recibosFreteCarreteiro = new AliasToNestedBeanResultTransformer(ReciboFreteCarreteiro.class).transformListResult(result);
        return recibosFreteCarreteiro.get(0);
    }

    /**
     * @param idControleCarga
     * @param idMeioMeioTransporte
     * @param idMotorista
     * @param idProprietario
     * @param pcAdiantamentoFrete
     * @param vlBruto
     * @param obReciboFreteCarreteiro
     */
    public Long storeReciboFreteCarreteiroByControleCarga(
            Long idControleCarga,
            Long idMeioMeioTransporte,
            Long idMotorista,
            Long idProprietario,
            Long idFilial,
            BigDecimal pcAdiantamentoFrete,
            BigDecimal vlBruto,
            String obReciboFreteCarreteiro,
            Boolean blAdiantamento,
            BigDecimal vlPremio,
            BigDecimal vlPostoPassagem
    ) {
        Filial filial = SessionUtils.getFilialSessao();
        if (idFilial != null) {
            filial = filialService.findById(idFilial);
        }
        ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
        reciboFreteCarreteiro.setFilial(filial);
        reciboFreteCarreteiro.setTpReciboFreteCarreteiro(new DomainValue(VIAGEM));
        reciboFreteCarreteiro.setMoedaPais(moedaPaisService.findMoedaPaisUsuarioLogado());
        reciboFreteCarreteiro.setPcAdiantamentoFrete(pcAdiantamentoFrete);
        reciboFreteCarreteiro.setVlBruto(vlBruto);
        reciboFreteCarreteiro.setBlAdiantamento(blAdiantamento);
        reciboFreteCarreteiro.setProprietario(proprietarioService.findById(idProprietario));
        reciboFreteCarreteiro.setMeioTransporteRodoviario(meioTransporteRodoviarioService.findById(idMeioMeioTransporte));
        reciboFreteCarreteiro.setMotorista(motoristaService.findById(idMotorista));
        reciboFreteCarreteiro.setControleCarga(controleCargaService.findByIdInitLazyProperties(idControleCarga, false));
        reciboFreteCarreteiro.setVlPremio(vlPremio);
        reciboFreteCarreteiro.setVlPostoPassagem(vlPostoPassagem);
        reciboFreteCarreteiro.setReciboComplementado(null);
        reciboFreteCarreteiro.setObReciboFreteCarreteiro(obReciboFreteCarreteiro);
        return ((ReciboFreteCarreteiro) generateReciboFreteCarreteiro(reciboFreteCarreteiro, "CA")).getIdReciboFreteCarreteiro();
    }

    /**
     * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
     *
     * @param idControleCarga
     * @return
     */
    public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga) {
        return this.findReciboFreteCarreteiroByIdControleCarga(idControleCarga, null);
    }

    /**
     * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
     *
     * @param idControleCarga
     * @param blAdiantamento
     * @return
     */
    public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga, Boolean blAdiantamento) {
        return this.getReciboFreteCarreteiroDAO().findReciboFreteCarreteiroByIdControleCarga(idControleCarga, blAdiantamento);
    }

    /**
     * Método que retorna uma lista de Recibo Frete Carreteiro a partir do ID do Controle de Carga.
     *
     * @param idControleCarga
     * @param blAdiantamento
     * @param tpProprietario  - Array de objetos com os TP_PROPRIETARIOS: [A]gregado, [E]ventual ...
     * @return
     */
    public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByIdControleCarga(Long idControleCarga, Boolean blAdiantamento, String[] tpProprietario) {
        return this.getReciboFreteCarreteiroDAO().findReciboFreteCarreteiroByIdControleCarga(idControleCarga, blAdiantamento, tpProprietario);
    }

    public List<ReciboFreteCarreteiro> findReciboFreteCarreteiroByEmissaoControleCarga(Long idControleCarga, Boolean blAdiantamento) {
        return getReciboFreteCarreteiroDAO().findReciboFreteCarreteiroByEmissaoControleCarga(idControleCarga, blAdiantamento);
    }

    /**
     * @param idControleCarga
     */
    public void storeReciboToTrocaVeiculoByIdControleCarga(Long idControleCarga) {
        getReciboFreteCarreteiroDAO().storeReciboToTrocaVeiculoByIdControleCarga(idControleCarga);
    }

    public List findReciboFreteCarreteiroByIdManifesto(Long idManifesto) {
        return getReciboFreteCarreteiroDAO().findReciboFreteCarreteiroByIdManifesto(idManifesto);
    }

    /**
     * Cria e emitir um novo recibo de frete carreteiro / recibo de adiantamento pertencente a um manifesto.
     *
     * @param manifesto (required)
     */
    public List generateReciboFreteCarreteiro(Manifesto manifesto) {
        ControleCarga controleCarga = manifesto.getControleCarga();
        MeioTransporte meioTransporte = controleCarga.getMeioTransporteByIdTransportado();
        String tpVinculo = meioTransporte.getTpVinculo().getValue();
        if (!"E".equals(tpVinculo) && !"A".equals(tpVinculo)) {
            return Collections.emptyList();
        }

        /** Regra 1.1 */
        if (buscarEValidarListaManiestoEmitidos(manifesto, controleCarga)) {
            return Collections.emptyList();
        }

        BigDecimal vlFrete = null;
        BigDecimal vlAdiantamento = null;
        BigDecimal pcAdiantamento = null;
        AdiantamentoTrecho at = null;

        /** Regra 1.3 */
        if (!"EV".equals(controleCarga.getTpRotaViagem().getValue())) {
            vlFrete = buscarVlFreteIgualEv(manifesto, controleCarga);
        } else {
            List listaFluxoContratacao = fluxoContratacaoService.findFluxoContratacaoByIdSolicitacaoContratacao(
                    controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao(),
                    manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                    manifesto.getFilialByIdFilialDestino().getIdFilial());
            if (!listaFluxoContratacao.isEmpty()) {
                FluxoContratacao fc = (FluxoContratacao) listaFluxoContratacao.get(0);
                vlFrete = BigDecimalUtils.percentValue(controleCarga.getSolicitacaoContratacao().getVlFreteMaximoAutorizado(), fc.getPcValorFrete(), 2);
            }

            List listaAdiantamentoTrecho = adiantamentoTrechoService.findByIdControleCarga(
                    controleCarga.getIdControleCarga(),
                    manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                    manifesto.getFilialByIdFilialDestino().getIdFilial());
            if (!listaAdiantamentoTrecho.isEmpty()) {
                at = (AdiantamentoTrecho) listaAdiantamentoTrecho.get(0);
                vlAdiantamento = at.getVlAdiantamento();
                pcAdiantamento = at.getPcFrete();
            }
        }
        List listaRetorno = new ArrayList();
        if (BigDecimalUtils.hasValue(vlFrete)) {
            listaRetorno.add(
                    generateReciboFreteCarreteiro(manifesto, controleCarga, vlFrete, null, controleCarga.getVlPedagio(), Boolean.FALSE));
        }
        if (BigDecimalUtils.hasValue(vlAdiantamento)) {
            salvarAdiantamentoTrechoSeExisteAdiantamento(manifesto, controleCarga, vlAdiantamento, pcAdiantamento, at, listaRetorno);
        }
        return listaRetorno;
    }

    private void salvarAdiantamentoTrechoSeExisteAdiantamento(Manifesto manifesto, ControleCarga controleCarga, BigDecimal vlAdiantamento, BigDecimal pcAdiantamento, AdiantamentoTrecho at, List listaRetorno) {
        Long idReciboFreteCarreteiro = generateReciboFreteCarreteiro(manifesto, controleCarga, vlAdiantamento, pcAdiantamento, null, Boolean.TRUE);
        listaRetorno.add(idReciboFreteCarreteiro);

        at.setTpStatusRecibo(new DomainValue("E"));
        at.setReciboFreteCarreteiro(findById(idReciboFreteCarreteiro));
        adiantamentoTrechoService.store(at);
    }

    private BigDecimal buscarVlFreteIgualEv(Manifesto manifesto, ControleCarga controleCarga) {
        List<TrechoRotaIdaVolta> listaTrechos = trechoRotaIdaVoltaService.findByTrechoRota(
                controleCarga.getRotaIdaVolta().getIdRotaIdaVolta(),
                manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                manifesto.getFilialByIdFilialDestino().getIdFilial());


        if (!listaTrechos.isEmpty()) {
            TrechoRotaIdaVolta triv = listaTrechos.get(0);
            if (BigDecimalUtils.hasValue(triv.getVlRateio())) {
                return triv.getVlRateio();
            }
        }
        return null;
    }

    private boolean buscarEValidarListaManiestoEmitidos(Manifesto manifesto, ControleCarga controleCarga) {
        List listaManifestoEmitidos =
                manifestoService.findManifestosByTrecho(ConstantesEntrega.TP_MANIFESTO_VIAGEM,
                        ConstantesEntrega.STATUS_MANIFESTO_EMITIDO,
                        controleCarga.getIdControleCarga(),
                        manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                        manifesto.getFilialByIdFilialDestino().getIdFilial());
        /** Regra 1.2 */
        if (!listaManifestoEmitidos.isEmpty()) {
            for (Iterator iter = listaManifestoEmitidos.iterator(); iter.hasNext(); ) {
                Manifesto m = (Manifesto) iter.next();
                if (!findReciboFreteCarreteiroByIdManifesto(m.getIdManifesto()).isEmpty())
                    return true;
            }
        }
        return false;
    }


    private Long generateReciboFreteCarreteiro(Manifesto manifesto, ControleCarga controleCarga, BigDecimal vlBruto, BigDecimal pcAdiantamento, BigDecimal vlPostoPassagem, Boolean blAdiantamento) {
        ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
        reciboFreteCarreteiro.setFilial(manifesto.getFilialByIdFilialOrigem());
        reciboFreteCarreteiro.setFilialDestino(manifesto.getFilialByIdFilialDestino());
        reciboFreteCarreteiro.setTpReciboFreteCarreteiro(new DomainValue(VIAGEM));
        reciboFreteCarreteiro.setMoedaPais(moedaPaisService.findMoedaPaisUsuarioLogado());
        reciboFreteCarreteiro.setPcAdiantamentoFrete(pcAdiantamento);
        reciboFreteCarreteiro.setVlBruto(vlBruto);
        reciboFreteCarreteiro.setBlAdiantamento(blAdiantamento);
        reciboFreteCarreteiro.setProprietario(controleCarga.getProprietario());
        reciboFreteCarreteiro.setManifestoViagemNacional(manifesto.getManifestoViagemNacional());
        reciboFreteCarreteiro.setMeioTransporteRodoviario(controleCarga.getMeioTransporteByIdTransportado().getMeioTransporteRodoviario());
        reciboFreteCarreteiro.setMotorista(controleCarga.getMotorista());
        reciboFreteCarreteiro.setControleCarga(controleCarga);
        reciboFreteCarreteiro.setVlPremio(null);
        reciboFreteCarreteiro.setVlPostoPassagem(vlPostoPassagem);
        reciboFreteCarreteiro.setReciboComplementado(null);
        reciboFreteCarreteiro.setObReciboFreteCarreteiro(null);
        ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) generateReciboFreteCarreteiro(reciboFreteCarreteiro, "CA");
        return rfc.getIdReciboFreteCarreteiro();
    }

    /**
     * Cancela recibo de frete carreteiro / recibo de adiantamento pertencente a um manifesto.
     *
     * @param idManifesto (required)
     */
    public void generateCancelamentoReciboFreteCarreteiro(Long idManifesto) {
        generateCancelamentoReciboFreteCarreteiro(idManifesto, Boolean.TRUE);
    }


    /**
     * @param idManifesto
     */
    public void removeReciboFreteCarreteiroInAdiantamentoTrecho(Long idManifesto) {
        generateCancelamentoReciboFreteCarreteiro(idManifesto, Boolean.FALSE);
        getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().flush();
    }


    private void generateCancelamentoReciboFreteCarreteiro(Long idManifesto, Boolean blCancelaRecibo) {
        List listaRecibos = findReciboFreteCarreteiroByIdManifesto(idManifesto);
        for (Iterator iter = listaRecibos.iterator(); iter.hasNext(); ) {
            ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) iter.next();

            Map mapRecibo = new HashMap();
            mapRecibo.put(ID_RECIBO_FRETE_CARRETEIRO, rfc.getIdReciboFreteCarreteiro());

            Map map = new HashMap();
            map.put(RECIBO_FRETE_CARRETEIRO, mapRecibo);
            map.put(TP_STATUS_RECIBO, "E");

            List listaAdiantamentoTrecho = adiantamentoTrechoService.find(map);
            for (Iterator iterAdiantamentoTrecho = listaAdiantamentoTrecho.iterator(); iterAdiantamentoTrecho.hasNext(); ) {
                AdiantamentoTrecho at = (AdiantamentoTrecho) iterAdiantamentoTrecho.next();
                at.setTpStatusRecibo(new DomainValue("G"));
                at.setReciboFreteCarreteiro(null);
                adiantamentoTrechoService.store(at);
            }

            if (blCancelaRecibo)
                storeCancelarRecibo(rfc.getIdReciboFreteCarreteiro());
        }
    }


    /**
     * Exclui os recibos de viagem nacional.
     *
     * @param idManifestoViagemNacional
     */
    public void removeRecibos(Long idManifestoViagemNacional) {
        getReciboFreteCarreteiroDAO().removeRecibos(idManifestoViagemNacional);
    }

    public Long findPropPessIdByCodPlacaNroPlaca(String codPlaca, Long nroPlaca) {
        return getReciboFreteCarreteiroDAO().findPropPessIdByCodPlacaNroPlaca(codPlaca, nroPlaca);
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }

    public ReciboFreteCarreteiro findByIdCustomColeta(Long id) {
        return getReciboFreteCarreteiroDAO().findByIdCustomColeta(id);
    }

    public ResultSetPage findPaginatedReciboColetaEntrega(TypedFlatMap criteria) {
        criteria.put(BL_COMPLEMENTAR, false);
        return getReciboFreteCarreteiroDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
    }

    public List<Map<String, Object>> findReciboSuggest(String sgFilial, Long nrReciboFreteCarreteiro, Integer limiteRegistros) {
        return getReciboFreteCarreteiroDAO().findReciboSuggest(sgFilial, nrReciboFreteCarreteiro, limiteRegistros);
    }

    public ResultSetPage findReciboComplementarReport(TypedFlatMap criteria) {
        ResultSetPage rsp = getReciboFreteCarreteiroDAO().findPaginatedComplementar(criteria, FindDefinition.createFindDefinition(criteria));
        List<ReciboFreteCarreteiro> recibosFreteCarreteiro = rsp.getList();
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>(recibosFreteCarreteiro.size());

        for (ReciboFreteCarreteiro rfc : recibosFreteCarreteiro) {
            TypedFlatMap newRow = new TypedFlatMap();

            ReciboFreteCarreteiro original = rfc.getReciboComplementado();

            setReciboOriginal(newRow, original);

            setRecibo(rfc, newRow);

            newList.add(newRow);
        }
        rsp.setList(newList);

        return rsp;
    }

    private void setRecibo(ReciboFreteCarreteiro rfc, TypedFlatMap newRow) {
        setAtributo(newRow, ID_RECIBO_FRETE_CARRETEIRO, rfc.getIdReciboFreteCarreteiro());
        setAtributo(newRow, NR_RECIBO_FRETE_CARRETEIRO, formataNumeroFreteCarreteiro(rfc));
        setAtributo(newRow, DT_EMISSAO, rfc.getDhEmissao());

        setAtributo(newRow, PROPRIETARIO_IDENTIFICACAO, formataIdentificacao(rfc.getProprietario().getPessoa()));
        setAtributo(newRow, PROPRIETARIO_NOME, rfc.getProprietario().getPessoa().getNmPessoa());

        MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();

        setAtributo(newRow, MEIO_TRANSPORTE_NR_FROTA1, meioTransporte.getNrFrota());
        setAtributo(newRow, MEIO_TRANSPORTE_NR_IDENTIFICADOR1, meioTransporte.getNrIdentificador());

        setAtributo(newRow, VL_LIQUIDO, formataValor(rfc.getMoedaPais(), rfc.getVlLiquido()));
        setAtributo(newRow, VL_BRUTO, formataValor(rfc.getMoedaPais(), rfc.getVlBruto()));
        setAtributo(newRow, OBSERVACAO, rfc.getObReciboFreteCarreteiro());
        setAtributo(newRow, CRIADOR_EMISSOR, rfc.getUsuario());

        setAtributo(newRow, ULTIMO_APROVADOR, getUltimoAprovador(rfc));

        setAtributo(newRow, SITUACAO_RECIBO, rfc.getTpSituacaoRecibo().getDescriptionAsString());
        setAtributo(newRow, DT_PGTO_REAL, rfc.getDtPagtoReal());
        setAtributo(newRow, DT_PGTO_PROGRAMADA, rfc.getDtProgramadaPagto());
    }

    private void setReciboOriginal(TypedFlatMap newRow, ReciboFreteCarreteiro original) {
        setAtributo(newRow, ID_RECIBO_FRETE_CARRETEIRO_ORIGINAL, original.getIdReciboFreteCarreteiro());
        setAtributo(newRow, NR_RECIBO_FRETE_CARRETEIRO_ORIGINAL, formataNumeroFreteCarreteiro(original));
        setAtributo(newRow, DT_EMISSAO_ORIGINAL, original.getDhEmissao());

        setAtributo(newRow, PROPRIETARIO_IDENTIFICACAO_ORIGINAL, formataIdentificacao(original.getProprietario().getPessoa()));
        setAtributo(newRow, PROPRIETARIO_NOME_ORIGINAL, original.getProprietario().getPessoa().getNmPessoa());

        MeioTransporte meioTransporteO = original.getMeioTransporteRodoviario().getMeioTransporte();

        setAtributo(newRow, MEIO_TRANSPORTE_NR_FROTA_ORIGINAL, meioTransporteO.getNrFrota());
        setAtributo(newRow, MEIO_TRANSPORTE_NR_IDENTIFICADOR_ORIGINAL, meioTransporteO.getNrIdentificador());

        setAtributo(newRow, VL_LIQUIDO_ORIGINAL, formataValor(original.getMoedaPais(), original.getVlLiquido()));
        setAtributo(newRow, VL_BRUTO_ORIGINAL, formataValor(original.getMoedaPais(), original.getVlBruto()));
        setAtributo(newRow, OBSERVACAO_ORIGINAL, original.getObReciboFreteCarreteiro());
        setAtributo(newRow, CRIADOR_EMISSOR_ORIGINAL, getUsuario(original));

        setAtributo(newRow, ULTIMO_APROVADOR_ORIGINAL, getUltimoAprovador(original));

        setAtributo(newRow, SITUACAO_RECIBO_ORIGINAL, original.getTpSituacaoRecibo().getDescriptionAsString());
        setAtributo(newRow, DT_PGTO_REAL_ORIGINAL, original.getDtPagtoReal());
        setAtributo(newRow, DT_PGTO_PROGRAMADA_ORIGINAL, original.getDtProgramadaPagto());
    }

    private String getUsuario(ReciboFreteCarreteiro recibo) {
        if (recibo.getUsuario() == null) {
            return null;
        }
        return recibo.getUsuario().getNmUsuario();
    }

    private String getUltimoAprovador(ReciboFreteCarreteiro original) {
        if (original.getPendencia() == null) {
            return null;
        }
        return original.getPendencia().getOcorrencia().getUsuario().getNmUsuario();
    }

    private String formataValor(MoedaPais moedaPais, BigDecimal valor) {
        String moeda = moedaPais.getMoeda().getSiglaSimbolo() + " ";
        if (valor == null) {
            return moeda + FormatUtils.formatDecimal("###,###,##0.00", BigDecimal.ZERO);
        }

        return moeda + FormatUtils.formatDecimal("###,###,##0.00", valor);
    }

    private String formataIdentificacao(Pessoa pessoa) {
        DomainValue tpIdentificacaoProprietario = pessoa.getTpIdentificacao();
        return tpIdentificacaoProprietario.getDescriptionAsString() + " " + FormatUtils.formatIdentificacao(tpIdentificacaoProprietario, pessoa.getNrIdentificacao());
    }

    private String formataNumeroFreteCarreteiro(ReciboFreteCarreteiro recibo) {
        return recibo.getFilial().getSgFilial() + " - " + FormatUtils.formatLongWithZeros(recibo.getNrReciboFreteCarreteiro(), NR_RECIBO_FRETE_PATTERN);
    }


    private void setAtributo(TypedFlatMap newRow, String atributo, Object valor) {
        if (valor == null) {
            newRow.put(atributo, "");
        } else {
            newRow.put(atributo, valor);
        }

    }

    public ResultSetPage findPaginatedReciboComplementar(TypedFlatMap criteria) {
        return getReciboFreteCarreteiroDAO().findPaginatedComplementar(criteria, FindDefinition.createFindDefinition(criteria));
    }

    /**
     * Atualiza um recibo complementar, grava seus anexos e gera workflow, caso seja necessário.
     * <p>
     * <b>Utilizado pelo novo frontend.</b>
     *
     * @param rfc
     * @param listAnexoReciboFc
     * @return ReciboFreteCarreteiro
     */
    public ReciboFreteCarreteiro storeReciboComplementar(ReciboFreteCarreteiro rfc, List<AnexoReciboFc> listAnexoReciboFc) {
        defineCommonValues(rfc);
        defineManifestoViagemNacional(rfc);

        ReciboFreteCarreteiro stored;

        if (rfc.getIdReciboFreteCarreteiro() == null) {
            defineDtProgramadaPagto(rfc);

            stored = (ReciboFreteCarreteiro) this.generateReciboFreteCarreteiro(rfc, "CO");

			/*
             * Solicita a atualização de querys pendentes em cache e atualiza a
			 * entidade na session.
			 */
            getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().flush();
            getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().refresh(stored);
        } else {
            ControleCarga controleCarga = rfc.getControleCarga();
            String tpRecibo = rfc.getTpReciboFreteCarreteiro().getValue();

            if (VIAGEM.equals(tpRecibo) && controleCarga != null) {
                gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga());
            }

            stored = populateUpdate(rfc);

            super.store(stored);
        }

        if (!listAnexoReciboFc.isEmpty()) {
            stored.setUsuario(SessionUtils.getUsuarioLogado());

            anexoReciboFcService.storeAnexos(stored, listAnexoReciboFc);
        }

        if (isReciboComplementarGerarWorkflow(stored, listAnexoReciboFc)) {
            executeWorkflowReciboComplementarFreteCarreteiroColetaEntregaService.executeWorkflowPendencia(stored);
        }

        return stored;
    }

    /**
     * Define a data de pagamento programada.
     *
     * @param rfc
     */
    private void defineDtProgramadaPagto(ReciboFreteCarreteiro rfc) {
        ParametroGeral parametro = parametroGeralService.findByNomeParametro("VL_DT_SUGERIDA_PAGTO", false);

        if (parametro == null) {
            return;
        }

        rfc.setDtProgramadaPagto(JTDateTimeUtils.getDataAtual().plusDays(Integer.parseInt(parametro.getDsConteudo())));
    }

    /**
     * @param rfc
     */
    private void defineCommonValues(ReciboFreteCarreteiro rfc) {
        rfc.setBlAdiantamento(Boolean.FALSE);
        rfc.setVlPremio(new BigDecimal(0));
        rfc.setVlPostoPassagem(new BigDecimal(0));
    }

    /**
     * @param rfc
     */
    private void defineManifestoViagemNacional(ReciboFreteCarreteiro rfc) {
        if (rfc.getReciboComplementado() == null) {
            return;
        }

        ReciboFreteCarreteiro rfcDB = findById(rfc.getReciboComplementado().getIdReciboFreteCarreteiro());
        rfc.setManifestoViagemNacional(rfcDB.getManifestoViagemNacional());
    }

    /**
     * Executa a gravação de anexos e workflow, se necessário.
     * <p>
     * <b>Utilizado pelo novo frontend.</b>
     *
     * @param rfc
     * @param listAnexoReciboFc
     * @return ReciboFreteCarreteiro
     */
    public ReciboFreteCarreteiro storeReciboColetaEntregaWorkflow(ReciboFreteCarreteiro rfc, List<AnexoReciboFc> listAnexoReciboFc) {
        super.store(rfc);
		
		/*
		 * Solicita a atualização de queries pendentes em cache e atualiza a
		 * entidade na session.
		 */
        getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().flush();
        getReciboFreteCarreteiroDAO().getAdsmHibernateTemplate().refresh(rfc);

        if (!listAnexoReciboFc.isEmpty()) {
            rfc.setUsuario(SessionUtils.getUsuarioLogado());

            anexoReciboFcService.storeAnexos(rfc, listAnexoReciboFc);
        }
		
		/*
		 * Se foram inseridos novos anexos, salva todos eles, gerando ou
		 * atualizando o workflow.
		 */
        if (isReciboColetaEntregaGerarWorkflow(rfc, listAnexoReciboFc)) {
            executeWorkflowReciboFreteCarreteiroColetaEntregaService.executeWorkflowPendencia(rfc);
        }

        return rfc;
    }

    /**
     * Verifica se precisa gerar/atualizar workflow.
     *
     * @param rfc
     * @param listAnexoReciboFc
     * @return boolean
     */
    private boolean isReciboColetaEntregaGerarWorkflow(ReciboFreteCarreteiro rfc, List<AnexoReciboFc> listAnexoReciboFc) {
        if (SessionUtils.isFilialSessaoMatriz()) {
            return false;
        }

        return true;
    }

    /**
     * Verifica se precisa gerar/atualizar workflow.
     *
     * @param rfc
     * @param listAnexoReciboFc
     * @return boolean
     */
    private boolean isReciboComplementarGerarWorkflow(ReciboFreteCarreteiro rfc, List<AnexoReciboFc> listAnexoReciboFc) {
        String tpReciboFreteCarreteiro = rfc.getTpReciboFreteCarreteiro().getValue();

        if ("V".equals(tpReciboFreteCarreteiro) || "P".equals(tpReciboFreteCarreteiro)) {
            return false;
        }

        if (SessionUtils.isFilialSessaoMatriz()) {
            return true;
        }

        return anexoReciboFcService.getRowCountItensByIdReciboFreteCarreteiro(rfc.getIdReciboFreteCarreteiro()) > 0 || !listAnexoReciboFc.isEmpty();
    }

    public List<Long> findByIntervalo(YearMonthDay dtInicial, YearMonthDay dtFinal, String sgFilial) {
        return getReciboFreteCarreteiroDAO().findByIntervalo(dtInicial, dtFinal, sgFilial);
    }
}
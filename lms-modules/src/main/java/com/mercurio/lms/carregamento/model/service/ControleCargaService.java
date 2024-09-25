package com.mercurio.lms.carregamento.model.service;

import br.com.tntbrasil.integracao.domains.carregamento.EventoControleCargaDMN;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaDMN;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaDocumentoDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.dao.ControleCargaDAO;
import com.mercurio.lms.carregamento.util.ControleCargaHelper;
import com.mercurio.lms.carregamento.util.MeioTranspProprietarioBuilder;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.service.*;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.constantes.ConsGeral;
import com.mercurio.lms.contratacaoveiculos.ConstantesContratacaoVeiculos;
import com.mercurio.lms.contratacaoveiculos.model.*;
import com.mercurio.lms.contratacaoveiculos.model.service.*;
import com.mercurio.lms.edi.enums.CampoNotaFiscalEdiComplementoFedex;
import com.mercurio.lms.edi.model.*;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaVolumeService;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalExpedicaoEDIService;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaCCService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.ConstantesMunicipios;
import com.mercurio.lms.municipios.model.*;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.service.AcaoIntegracaoEventosService;
import com.mercurio.lms.portaria.model.service.AcaoIntegracaoService;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.portaria.model.service.OrdemSaidaService;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;
import com.mercurio.lms.sgr.model.service.*;
import com.mercurio.lms.sgr.model.util.PlanoGerenciamentoRiscoUtils;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.LMControleCargaDAO;
import com.mercurio.lms.sim.model.service.*;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;
import com.mercurio.lms.workflow.model.EventoWorkflow;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.EventoWorkflowService;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.carregamento.controleCargaService"
 */
@SuppressWarnings("deprecation")
public class ControleCargaService extends CrudService<ControleCarga, Long> {
	private static final String EVENTOS_REENVIO_NOTFIS_CC_FAAV = "EVENTOS_REENVIO_NOTFIS_CC_FAAV";
	private static final Long ID_USUARIO_INTEGRACAO = 5000l;

    private static final Short LOCALIZACAO_EM_DESCARGA = Short.valueOf("34");
    private static final Short LOCALIZACAO_AGUARDANDO_DESCARGA = Short.valueOf("38");

	private static final Short EVENTO_FIM_DESCARGA = Short.valueOf("30");

	private static final Short EVENTO_FIM_DESCARGA_SEM_LOCALIZACAO = Short.valueOf("125");

    private static final Short NR_EVENTO_LIBERAR_EMISSAO_CC = Short.valueOf("0501");

    private static final String SGR_PERIFERICOS_RAST_CONTING = "SGR_PERIFERICOS_RAST_CONTING";
    private static final String MONIT_RAST_PRINC_REBOQ_SEG_TEC = "MONIT_RAST_PRINC_REBOQ_SEG_TEC";
    private static final String MONIT_RAST_PRINC_REBOQ_CONT = "MONIT_RAST_PRINC_REBOQ_CONT";
    private static final String MONIT_RAST_VEICULO_PRINC_REBOQ = "MONIT_RAST_VEICULO_PRINC_REBOQ";
    private static final String MONIT_RAST_VEICULO_PRINC = "MONIT_RAST_VEICULO_PRINC";
    private static final String COLETA = "C";
    private static final String MOTORISTA_FUNCIONARIO = "MOTORISTA_FUNCIONARIO";
    private static final String MOTORISTA_AGREGADO = "MOTORISTA_AGREGADO";
    private static final String MOTORISTA_EVENTUAL = "MOTORISTA_EVENTUAL";
    private static final String VINCULO_FUNCIONARIO = "F";
    private static final String VINCULO_AGREGADO = "A";
    private static final String VINCULO_EVENTUAL = "E";
    private static final String PA_SGR_VAL_EXIST_PGR = "SGR_VAL_EXIST_PGR";
    private static final String CONTROLE_CARGA_COLETA_ENTREGA = "C";
    private static final String CIOT_GERADO = "G";

    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final int ARR_POS_2 = 2;
    private static final int ARR_POS_1 = 1;
    private static final int ARR_POS_4 = 4;
    private static final int ARR_POS_3 = 3;
    private static final int ARR_POS_5 = 5;
    private static final int ARR_POS_0 = 0;
    private static final int ARR_POS_6 = 6;
    private static final int ARR_POS_7 = 7;
    private static final int ARR_POS_8 = 8;
    private static final int ARR_POS_9 = 9;
    private static final int ARR_POS_10 = 10;
    private static final int ARR_POS_11 = 11;
    private static final int ARR_POS_12 = 12;
    private static final int ARR_POS_13 = 13;
    private static final int ARR_POS_14 = 14;
    private static final int ARR_POS_15 = 15;
    private static final int ARR_POS_16 = 16;
    private static final int ARR_POS_17 = 17;
    private static final int ARR_POS_18 = 18;
    private static final int ARR_POS_19 = 19;
    private static final int ARR_POS_20 = 20;
    private static final int ARR_POS_21 = 21;
    private static final int ARR_POS_22 = 22;
    private static final int ARR_POS_23 = 23;
    private static final int ARR_POS_24 = 24;
    private static final int ARR_POS_25 = 25;
    // LMSA-6340
    private static final int ARR_POS_26 = 26;
    
    private static final int SEGUNDO_INTERVALO_MILIS = 1000;
    private static final int DIVIDE_SCALE = 2;
    private static final int HORA_LIMITE = 24;
    private static final int FILL_SIZE = 9;
    private static final int BIG_DIVIDE_SCALE = 8;
    private static final int DIF_DIAS = 15;

    private static final String ID_PAIS = "idPais";
    private static final String TOTAL_VL_MERCADORIA = "totalVlMercadoria";
    private static final String ID_MOEDA = "idMoeda";
    private static final String ID_MEIO_TRANSPORTE = "idMeioTransporte";
    private static final String DH_GERACAO = "dhGeracao";
    private static final String SEMI_REBOQUE = "semiReboque";
    private static final String FORMAT_PATTERN = "00000000";
    private static final String ID_CONTROLE_CARGA = "idControleCarga";
    private static final String REEMISSAO = "reemissao";
    private static final String EMISSAO = "emissao";
    private static final String ACAO = "acao";
    private static final String BL_EMISSAO = "blEmissao";
    private static final String TP_STATUS_CONTROLE_CARGA = "tpStatusControleCarga";
    private static final String BL_ENTREGA_DIRETA = "blEntregaDireta";
    private static final String SOLICITACAO_CONTRATACAO_ID_SOLICITACAO_CONTRATACAO = "solicitacaoContratacao.idSolicitacaoContratacao";
    private static final String SOLICITACAO_CONTRATACAO_NR_SOLICITACAO_CONTRATACAO = "solicitacaoContratacao.nrSolicitacaoContratacao";
    private static final String MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE = "meioTransporteByIdTransportado.idMeioTransporte";
    private static final String MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_FROTA = "meioTransporteByIdTransportado.nrFrota";
    private static final String MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_IDENTIFICADOR = "meioTransporteByIdTransportado.nrIdentificador";
    private static final String MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_TP_VINCULO = "meioTransporteByIdTransportado.tpVinculo";
    private static final String MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE = "meioTransporteByIdSemiRebocado.idMeioTransporte";
    private static final String MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_FROTA = "meioTransporteByIdSemiRebocado.nrFrota";
    private static final String MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_IDENTIFICADOR = "meioTransporteByIdSemiRebocado.nrIdentificador";
    private static final String ROTA_COLETA_ENTREGA_ID_ROTA_COLETA_ENTREGA = "rotaColetaEntrega.idRotaColetaEntrega";
    private static final String ROTA_COLETA_ENTREGA_NR_ROTA = "rotaColetaEntrega.nrRota";
    private static final String ROTA_COLETA_ENTREGA_DS_ROTA = "rotaColetaEntrega.dsRota";
    private static final String PROPRIETARIO_ID_PROPRIETARIO = "proprietario.idProprietario";
    private static final String PROPRIETARIO_PESSOA_NR_IDENTIFICACAO_FORMATADO = "proprietario.pessoa.nrIdentificacaoFormatado";
    private static final String PROPRIETARIO_PESSOA_NM_PESSOA = "proprietario.pessoa.nmPessoa";
    private static final String PC_OCIOSIDADE_VISUAL = "pcOciosidadeVisual";
    private static final String TIPO_TABELA_COLETA_ENTREGA_ID_TIPO_TABELA_COLETA_ENTREGA = "tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega";
    private static final String TABELA_COLETA_ENTREGA_ID_TABELA_COLETA_ENTREGA = "tabelaColetaEntrega.idTabelaColetaEntrega";
    private static final String MOTORISTA_ID_MOTORISTA = "motorista.idMotorista";
    private static final String MOTORISTA_PESSOA_NR_IDENTIFICACAO = "motorista.pessoa.nrIdentificacao";
    private static final String MOTORISTA_PESSOA_NR_IDENTIFICACAO_FORMATADO = "motorista.pessoa.nrIdentificacaoFormatado";
    private static final String MOTORISTA_PESSOA_NM_PESSOA = "motorista.pessoa.nmPessoa";
    private static final String CONTROLE_CARGA = "controleCarga";
    private static final String BL_SAIDA = "blSaida";
    private static final String NR_QUILOMETRAGEM = "nrQuilometragem";
    private static final String BL_INFORMA_KM_PORTARIA = "blInformaKmPortaria";
    private static final String FILIAL_USUARIO_ID_FILIAL = "filialUsuario.idFilial";
    private static final String FILIAL_USUARIO_SG_FILIAL = "filialUsuario.sgFilial";
    private static final String FILIAL_USUARIO_PESSOA_NM_FANTASIA = "filialUsuario.pessoa.nmFantasia";
    private static final String ID_EQUIPE_OPERACAO = "idEquipeOperacao";
    private static final String ID_EQUIPE = "idEquipe";
    private static final String BTN_MDFE = "btnMdfe";
    private static final String LIMITE_ESPERA = "limiteEspera";
    private static final String IDS_MANIFESTO_ELETRONICO = "idsManifestoEletronico";
    private static final String DH_EMISSAO = "dhEmissao";
    private static final String LIMITE_ESPERA_MDFE = "LIMITE_ESPERA_MDFE";
    private static final String LIMITE_RETORNO_MDFE = "LIMITE_RETORNO_MDFE";
    private static final String ID_MANIFESTO_ELETRONICO_ENCERRADO = "idManifestoEletronicoEncerrado";
    private static final String DH_ENCERRAMENTO = "dhEncerramento";
    private static final String MENSAGENS_ENQUADRAMENTO = "mensagensEnquadramento";
    private static final String MENSAGENS_WORKFLOW_REGRAS = "mensagensWorkflowRegras";
    private static final String MENSAGENS_WORKFLOW_EXIGENCIAS = "mensagensWorkflowExigencias";
    private static final String IMPRIME_MDFE_AUTORIZADO = "imprimeMdfeAutorizado";
    private static final String ENCERRAR_MDFES_AUTORIZADOS = "encerrarMdfesAutorizados";
    private static final String HAS_CONTINGENCIA = "hasContingencia";
    private static final String REJEITADOS = "rejeitados";
    private static final String EXECUTAR_GERAR_MDFE_CONTIGENCIA = "executarGerarMDFEContigencia";
    private static final String PULAR_GERACAO_MDFE = "pularGeracaoMdfe";
    private static final String FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL = "filialByIdFilialOrigem.idFilial";
    private static final String NR_CONTROLE_CARGA = "nrControleCarga";
    private static final String NOTA_CREDITO_ID_NOTA_CREDITO = "notaCredito.idNotaCredito";
    private static final String DT_GERACAO_INICIAL = "dtGeracaoInicial";
    private static final String DT_GERACAO_FINAL = "dtGeracaoFinal";
    private static final String TP_STATUS_MANIFESTO_VALUE = "tpStatusManifesto.value";
    private static final String TP_MANIFESTO_VALUE = "tpManifesto.value";
    private static final String SG_FILIAL_ORIGEM_MANIFESTO = "sgFilialOrigemManifesto";
    private static final String ID_DOCTO_SERVICO = "idDoctoServico";
    private static final String NR_PRE_MANIFESTO = "nrPreManifesto";
    private static final String TP_ROTA_VIAGEM = "tpRotaViagem";
    private static final String VL_FRETE_CARRETEIRO = "vlFreteCarreteiro";
    private static final String DH_PREVISAO_SAIDA = "dhPrevisaoSaida";
    private static final String NR_TEMPO_VIAGEM = "nrTempoViagem";
    private static final String TP_CONTROLE_CARGA = "tpControleCarga";
    private static final String INSTRUTOR_MOTORISTA_ID_INSTRUTOR_MOTORISTA = "instrutorMotorista.idInstrutorMotorista";
    private static final String ROTA_IDA_VOLTA_ID_ROTA_IDA_VOLTA = "rotaIdaVolta.idRotaIdaVolta";
    private static final String ROTA_ID_ROTA = "rota.idRota";

    private static final String LMS_05057 = "LMS-05057";
    private static final String LMS_05056 = "LMS-05056";
    private static final String LMS_05123 = "LMS-05123";
    private static final String LMS_05168 = "LMS-05168";
    private static final String LMS_05027 = "LMS-05027";
    private static final String LMS_05164 = "LMS-05164";
    private static final String LMS_05357 = "LMS-05357";
    private static final String LMS_05124 = "LMS-05124";
    private static final String LMS_26044 = "LMS-26044";
    private static final String LMS_11348 = "LMS-11348";
    private static final String LMS_05127 = "LMS-05127";
    private static final String LMS_11335 = "LMS-11335";

    private ServicoService servicoService;
    private AdiantamentoTrechoService adiantamentoTrechoService;
    private CarregamentoDescargaService carregamentoDescargaService;
    private CartaoPedagioService cartaoPedagioService;
    private ConfiguracoesFacade configuracoesFacade;
    private ControleQuilometragemService controleQuilometragemService;
    private ControleTrechoService controleTrechoService;
    private ConversaoMoedaService conversaoMoedaService;
    private DispositivoUnitizacaoService dispositivoUnitizacaoService;
    private DoctoServicoService doctoServicoService;
    private DomainValueService domainValueService;
    private EquipeOperacaoService equipeOperacaoService;
    private EquipeService equipeService;
    private EventoControleCargaService eventoControleCargaService;
    private EventoColetaService eventoColetaService;
    private EventoManifestoColetaService eventoManifestoColetaService;
    private EventoManifestoService eventoManifestoService;
    private EventoMeioTransporteService eventoMeioTransporteService;
    private EventoVolumeService eventoVolumeService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private EventoWorkflowService eventoWorkflowService;
    private FeriadoService feriadoService;
    private FilialRotaCcService filialRotaCcService;
    private FilialRotaService filialRotaService;
    private FilialService filialService;
    private FluxoFilialService fluxoFilialService;
    private GerarEnviarSMPService gerarEnviarSMPService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private LacreControleCargaService lacreControleCargaService;
    private LiberacaoReguladoraService liberacaoReguladoraService;
    private ManifestoService manifestoService;
    private ManifestoColetaService manifestoColetaService;
    private ManifestoEntregaService manifestoEntregaService;
    private ManifestoViagemNacionalService manifestoViagemNacionalService;
    private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    private MeioTransporteService meioTransporteService;
    private MoedaPaisService moedaPaisService;
    private MoedaService moedaService;
    private MotivoCancelamentoCcService motivoCancelamentoCcService;
    private MotoristaService motoristaService;
    private MotoristaControleCargaService motoristaControleCargaService;
    private MunicipioService municipioService;
    private OperadoraCartaoPedagioService operadoraCartaoPedagioService;
    private OrdemSaidaService ordemSaidaService;
    private PagtoPedagioCcService pagtoPedagioCcService;
    private PagtoProprietarioCcService pagtoProprietarioCcService;
    private PedidoColetaService pedidoColetaService;
    private PendenciaService pendenciaService;
    private PostoPassagemCcService postoPassagemCcService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private ProcessoSinistroService processoSinistroService;
    private ProprietarioService proprietarioService;
    private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
    private ReciboPostoPassagemService reciboPostoPassagemService;
    private RotaService rotaService;
    private RotaColetaEntregaService rotaColetaEntregaService;
    private RotaIdaVoltaService rotaIdaVoltaService;
    private SemiReboqueCcService semiReboqueCcService;
    private SolicitacaoContratacaoService solicitacaoContratacaoService;
    private SolicitacaoSinalService solicitacaoSinalService;
    private SolicMonitPreventivoService solicMonitPreventivoService;
    private TabelaColetaEntregaService tabelaColetaEntregaService;
    private TipoMeioTransporteService tipoMeioTransporteService;
    private TipoPagamPostoPassagemService tipoPagamPostoPassagemService;
    private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
    private TrechoCorporativoService trechoCorporativoService;
    private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
    private VeiculoControleCargaService veiculoControleCargaService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private WorkflowPendenciaService workflowPendenciaService;
    private LMControleCargaDAO lmControleCargaDao;
    private TransponderService transponderService;
    private TabelaColetaEntregaCCService tabelaColetaEntregaCCService;
    private UsuarioService usuarioService;
    private AcaoIntegracaoService acaoIntegracaoService;
    private AcaoIntegracaoEventosService acaoIntegracaoEventosService;
    private MeioTranspProprietarioService meioTranspProprietarioService;
    private BloqueioMotoristaPropService bloqueioMotoristaPropService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private ParcelaTabelaCeService parcelaTabelaCeService;
    private ConhecimentoService conhecimentoService;
    private ParametroGeralService parametroGeralService;
    private ManifestoEletronicoService manifestoEletronicoService;
    private CarregamentoDescargaVolumeService carregamentoDescargaVolumeService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;
    private PlanoGerenciamentoRiscoUtils planoUtils;
    private PerifericoRastreadorService perifericoRastreadorService;
    private VirusCargaService virusCargaService;
    private CIOTService ciotService;
    private CIOTControleCargaService ciotControleCargaService;
    private PaisService paisService;
    private VolDadosSessaoService volDadosSessaoService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
   
    private DescargaManifestoService descargaManifestoService;
    private EmpresaService empresaService;
    
    
    private TelefoneEnderecoService telefoneEnderecoService;
    private PessoaService pessoaService;
   
    public void setDescargaManifestoService(
			DescargaManifestoService descargaManifestoService) {
		this.descargaManifestoService = descargaManifestoService;
	}

    //LMSA-6520: LMSA-6534
    private ConhecimentoFedexService conhecimentoFedexService;
    private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;
    private InformacaoDoctoClienteService informacaoDoctoClienteService;

    // LMSA-6159 LMSA-6249
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService; 
    private IntegracaoJmsService integracaoJmsService;
    private DadosComplementoService dadosComplementoService;
    
    private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
    private static final String SIM = "S";

    public BigDecimal findCapacidadeMediaMeioTransporteControleCarga(ControleCarga controleCarga) {
        BigDecimal franquia = BigDecimal.valueOf(controleCarga.getFilialByIdFilialOrigem().getNrFranquiaPeso());

        if (franquia == null) {
            throw new BusinessException("LMS-25039");
        }

        return meioTransporteService.findCapacidadeMedia(controleCarga.getMeioTransporteByIdTransportado(), franquia);
    }

    public List<ControleCarga> findControlesCargaProprietarioNoPeriodo(ControleCarga controleCarga) {
        List<ControleCarga> controlesCarga = getControleCargaDAO().findControlesCargaProprietarioNoPeriodo(
                controleCarga);

        if (controlesCarga == null) {
            return new ArrayList<ControleCarga>();
        }

        controlesCarga.add(controleCarga);

        return controlesCarga;
    }

    @SuppressWarnings("rawtypes")
    public boolean hasTabelaColetaEntregaCC(ControleCarga controleCarga) {
        List tabelas = tabelaColetaEntregaCCService.findByIdContoleCarga(controleCarga.getIdControleCarga());
        return tabelas != null && !tabelas.isEmpty();
    }

    public Long findQuilometrosPercorridosByIdControleCarga(Long idControleCarga) {
        return findQuilometrosPercorridosControleCarga(findById(idControleCarga));
    }

    public Long findQuilometrosPercorridosControleCarga(ControleCarga controleCarga) {
        Long totalSaida = 0L;
        Long totalChegada = 0L;
        int virouHodometro = 0;

        for (ControleQuilometragem controleQuilometragem : getControlesQuilometragem(controleCarga)) {
            if (controleQuilometragem.getNrQuilometragem() == null) {
                throw new BusinessException("LMS-25028", new Object[]{
                        FormatUtils.formatSgFilialWithLong(
                                controleCarga.getFilialByIdFilialOrigem().getSgFilial(),
                                controleCarga.getNrControleCarga())});
            }

            if (controleQuilometragem.getBlSaida()) {
                totalSaida += controleQuilometragem.getNrQuilometragem();
            } else {
                if (controleQuilometragem.getBlVirouHodometro()) {
                    virouHodometro++;
                }

                totalChegada += controleQuilometragem.getNrQuilometragem();
            }
        }

        return calculaTotalQuilometragem(virouHodometro, totalSaida, totalChegada);
    }

    private Long calculaTotalQuilometragem(int virouHodometro, Long totalSaida, Long totalChegada) {
        Long retorno = (virouHodometro > 0)
                ? ((ControleQuilometragem.MAX_QUILOMETRAGEM * virouHodometro) - totalSaida) + totalChegada
                : totalChegada - totalSaida;
        return retorno > 0 ? retorno : 0L;
    }

    private List<ControleQuilometragem> getControlesQuilometragem(ControleCarga controleCarga) {
        if (controleCarga.getControleQuilometragems() == null) {
            controleCarga.setControleQuilometragems(new ArrayList<ControleQuilometragem>());
        } else {
            ControleQuilometragemService.sortByDhMedicao(controleCarga.getControleQuilometragems());
        }

        return controleCarga.getControleQuilometragems();
    }

    public ServicoService getServicoService() {
        return servicoService;
    }

    public void setServicoService(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    public AcaoIntegracaoEventosService getAcaoIntegracaoEventosService() {
        return acaoIntegracaoEventosService;
    }


    public void setAcaoIntegracaoService(AcaoIntegracaoService acaoIntegracaoService) {
        this.acaoIntegracaoService = acaoIntegracaoService;
    }

    public void setAcaoIntegracaoEventosService(
            AcaoIntegracaoEventosService acaoIntegracaoEventosService) {
        this.acaoIntegracaoEventosService = acaoIntegracaoEventosService;
    }

    /**
     * Recupera uma instância de <code>ControleCarga</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public ControleCarga findById(java.lang.Long id) {
        return (ControleCarga) super.findById(id);
    }

    public ControleCarga findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
        return (ControleCarga) super.findByIdInitLazyProperties(id, initializeLazyProperties);
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(ControleCarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao
     */
    public void setControleCargaDAO(ControleCargaDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ControleCargaDAO getControleCargaDAO() {
        return (ControleCargaDAO) getDao();
    }

    @SuppressWarnings("rawtypes")
    public List findControleCargaByNotaCredito(Long idNotaCredito) {
        return getControleCargaDAO().findControleCargaByNotaCredito(idNotaCredito);
    }

    @SuppressWarnings("rawtypes")
    public List findControleCargaByProprietario(Long idProprietario) {
        return getControleCargaDAO().findControleCargaByProprietario(idProprietario);
    }

    /**
     * Valida se existe uma nota de crédito para o controle de carga informado.
     *
     * @param idControleCarga
     * @return <b>true</b> se encontrar uma nota de crédito para o CC informado.
     * @author Felipe Ferreira
     */
    public boolean validateExisteNotaCreditoInControleCarga(Long idControleCarga) {
        return getControleCargaDAO().validateExisteNotaCreditoInControleCarga(idControleCarga);
    }

    /**
     * Altera o status do controle de carga e gera um evento para o controle de carga em questão.
     */
    public void generateEventoChangeStatusControleCarga(Long idControleCarga, Long idFilial, String tpEventoControleCarga) {
        generateEventoChangeStatusControleCarga(idControleCarga, idFilial, tpEventoControleCarga, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Altera o status do controle de carga e gera um evento para o controle de carga em questão.
     * Assinatura com parâmetros necessários para Integração proveniente da solicitação CQPRO00005472 e chamada a partir do generateControleCarga
     */
    public void generateEventoChangeStatusControleCarga(Long idControleCarga, Long idFilial, String tpEventoControleCarga, DateTime dhEvento) {
        generateEventoChangeStatusControleCarga(idControleCarga, idFilial, tpEventoControleCarga, dhEvento,
                null, null, null, null, null, null, null, null,
                null, null, (BigDecimal) null);
    }


    /**
     * Altera o status do controle de carga e gera um evento para o controle de
     * carga em questão.
     */
    public void generateEventoChangeStatusControleCarga(Long idControleCarga, Long idFilial,
                                                        String tpEventoControleCarga, Long idEquipeOperacao,
                                                        Long idMeioTransporte, String dsEvento, BigDecimal vlLiberacao,
                                                        Long idMoeda, BigDecimal psReal, BigDecimal psAforado, BigDecimal vlTotal,
                                                        BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado,
                                                        BigDecimal pcOcupacaoInformado) {

        this.generateEventoChangeStatusControleCarga(idControleCarga,
                idFilial,
                tpEventoControleCarga,
                JTDateTimeUtils.getDataHoraAtual(),
                idEquipeOperacao,
                idMeioTransporte,
                dsEvento,
                vlLiberacao,
                idMoeda,
                psReal,
                psAforado,
                vlTotal,
                pcOcupacaoCalculado,
                pcOcupacaoAforadoCalculado,
                pcOcupacaoInformado);
    }

    /**
     * Método com parâmetro alterado para ficar de acordo com a necessidade da Integração (parâmetros dhEvento),
     * proveniente da solicitação CQPRO00005472 e CQPRO00005477 e chamada a partir do generateControleCarga.
     * Para necessidades externas à Integração, utilizar o método definido acima, que não possua esses parâmetros.
     * <p>
     * Altera o status do controle de carga e gera um evento para o controle de
     * carga em questão.
     */
    public void generateEventoChangeStatusControleCarga(Long idControleCarga, Long idFilial,
                                                        String tpEventoControleCarga, DateTime dhEvento, Long idEquipeOperacao,
                                                        Long idMeioTransporte, String dsEvento, BigDecimal vlLiberacao,
                                                        Long idMoeda, BigDecimal psReal, BigDecimal psAforado, BigDecimal vlTotal,
                                                        BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado,
                                                        BigDecimal pcOcupacaoInformado) {

        EquipeOperacao equipeOperacao = null;
        if (idEquipeOperacao != null) {
            equipeOperacao = equipeOperacaoService.findById(idEquipeOperacao);
        }

        generateEventoChangeStatusControleCarga(idControleCarga,
                idFilial, tpEventoControleCarga, dhEvento, idMeioTransporte,
                dsEvento, vlLiberacao, idMoeda, psReal, psAforado, vlTotal,
                pcOcupacaoCalculado, pcOcupacaoAforadoCalculado,
                pcOcupacaoInformado, equipeOperacao);
    }

    public void generateEventoChangeStatusControleCarga(Long idControleCarga, Long idFilial,
                                                        String tpEventoControleCarga, DateTime dhEvento, Long idMeioTransporte, String dsEvento,
                                                        BigDecimal vlLiberacao, Long idMoeda, BigDecimal psReal, BigDecimal psAforado, BigDecimal vlTotal,
                                                        BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado, BigDecimal pcOcupacaoInformado,
                                                        EquipeOperacao equipeOperacao) {

        ControleCarga controleCarga = this.findByIdInitLazyProperties(idControleCarga, false);
        Filial filial = filialService.findById(idFilial);

        MeioTransporte meioTransporte = null;
        if (idMeioTransporte != null) {
            meioTransporte = meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false);
        }

        Moeda moeda = null;
        if (idMoeda != null) {
            moeda = this.moedaService.findById(idMoeda);
        }

        generateEventoChangeStatusControleCarga(controleCarga, filial, tpEventoControleCarga, dhEvento, meioTransporte,
                dsEvento, vlLiberacao, moeda, psReal, psAforado, vlTotal, pcOcupacaoCalculado,
                pcOcupacaoAforadoCalculado, pcOcupacaoInformado, equipeOperacao, true);
    }


    public EventoControleCarga generateEventoChangeStatusControleCarga(ControleCarga controleCarga, Filial filial,
                                                                       String tpEventoControleCarga, DateTime dhEvento, MeioTransporte meioTransporte, String dsEvento,
                                                                       BigDecimal vlLiberacao, Moeda moeda, BigDecimal psReal, BigDecimal psAforado, BigDecimal vlTotal,
                                                                       BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado, BigDecimal pcOcupacaoInformado,
                                                                       EquipeOperacao equipeOperacao, boolean store) {

        return generateEventoChangeStatusControleCarga(
                controleCarga, filial, tpEventoControleCarga, dhEvento, meioTransporte, dsEvento, vlLiberacao, moeda, psReal,
                psAforado, vlTotal, pcOcupacaoCalculado, pcOcupacaoAforadoCalculado, pcOcupacaoInformado, equipeOperacao,
                store, null, true);
    }

    public EventoControleCarga generateEventoChangeStatusControleCarga(ControleCarga controleCarga, Filial filial,
                                                                       String tpEventoControleCarga, DateTime dhEvento, MeioTransporte meioTransporte, String dsEvento,
                                                                       BigDecimal vlLiberacao, Moeda moeda, BigDecimal psReal, BigDecimal psAforado, BigDecimal vlTotal,
                                                                       BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado, BigDecimal pcOcupacaoInformado,
                                                                       EquipeOperacao equipeOperacao, boolean store, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                                       boolean updateTransponder) {

        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        if ((vlLiberacao != null || vlTotal != null) && moeda == null) {
            throw new BusinessException("LMS-05068");
        }

        Long idFilial = filial.getIdFilial();
        Long idFilialOrigem = controleCarga.getFilialByIdFilialOrigem().getIdFilial();
        Long idFilialDestino = null;
        if (controleCarga.getFilialByIdFilialDestino() != null) {
            idFilialDestino = controleCarga.getFilialByIdFilialDestino().getIdFilial();
        }

        String tpControleCarga = controleCarga.getTpControleCarga().getValue();
        String tpStatusControleCargaAntigo = controleCarga.getTpStatusControleCarga().getValue();

        if ("LR".equalsIgnoreCase(tpEventoControleCarga) || "MO".equalsIgnoreCase(tpEventoControleCarga)
                || "GE".equalsIgnoreCase(tpEventoControleCarga)) {
            // Não altera o status do Controle de Carga.
        } else if ("EM".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoEM(controleCarga, tpControleCarga);
        } else if ("CP".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoCP(controleCarga, idFilial, idFilialOrigem, idFilialDestino,
                    tpControleCarga);
        } else if ("ID".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoID(controleCarga, idFilial, idFilialOrigem, idFilialDestino,
                    tpControleCarga);
        } else if ("SP".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoSP(controleCarga, tpControleCarga);
        } else if ("FD".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoFD(controleCarga, idFilial, idFilialDestino, tpControleCarga);
        } else if ("CA".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoCA(controleCarga);
        } else if ("IC".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoIC(controleCarga, idFilial, idFilialOrigem, idFilialDestino,
                    tpControleCarga);
        } else if ("FC".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoFC(controleCarga);
        } else if ("EC".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoEC(controleCarga);
        } else if ("PO".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoPO(controleCarga);
        } else if ("CD".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoCD(controleCarga, tpControleCarga);
        } else if ("CC".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoCC(controleCarga, idFilial, idFilialOrigem, tpControleCarga);
        } else if ("FM".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoFM(controleCarga);
        } else if ("TP".equalsIgnoreCase(tpEventoControleCarga)) {
            trocaStatusControleCargaTpEventoTP(controleCarga);
        } else {
            // Exceção, pois o parâmetro é inválido.
            throw new BusinessException("LMS-05014");
        }

        if (!tpStatusControleCargaAntigo.equals(controleCarga.getTpStatusControleCarga().getValue())) {
            controleCarga.setFilialByIdFilialAtualizaStatus(filial);
            generateEventoChangeStatusControleCargaUpdate(controleCarga, store, adsmNativeBatchSqlOperations, updateTransponder);
        }

        // LMS-8141
        if ("TP".equalsIgnoreCase(tpEventoControleCarga)) {
            return null;
        }


        EventoControleCarga eventoControleCarga = new EventoControleCarga();
        eventoControleCarga.setFilial(filial);
        eventoControleCarga.setDhEvento(dhEvento);
        eventoControleCarga.setTpEventoControleCarga(new DomainValue(tpEventoControleCarga));
        eventoControleCarga.setUsuario(usuarioLogado);
        eventoControleCarga.setEquipeOperacao(equipeOperacao);
        eventoControleCarga.setMeioTransporte(meioTransporte);
        eventoControleCarga.setDsEvento(dsEvento);
        eventoControleCarga.setVlLiberacao(vlLiberacao);
        eventoControleCarga.setMoeda(moeda);
        eventoControleCarga.setPsReal(psReal);
        eventoControleCarga.setPsAforado(psAforado);
        eventoControleCarga.setVlTotal(vlTotal);
        eventoControleCarga.setPcOcupacaoCalculado(pcOcupacaoCalculado);
        eventoControleCarga.setPcOcupacaoAforadoCalculado(pcOcupacaoAforadoCalculado);
        eventoControleCarga.setPcOcupacaoInformado(pcOcupacaoInformado);
        eventoControleCarga.setControleCarga(controleCarga);
        // gera um registro para eventoControleCarga

        if (store) {
            generateEventoChangeStatusControleCargaAddEvento(adsmNativeBatchSqlOperations, eventoControleCarga);
        }
        
        generateEventoControleCargaFilaIntegracao(controleCarga, eventoControleCarga);
        return eventoControleCarga;
    }

    private void generateEventoControleCargaFilaIntegracao(ControleCarga controleCarga, EventoControleCarga eventoControleCarga) {
        EventoControleCargaDMN ccDMN = new EventoControleCargaDMN();
        ccDMN.setIdControleCarga(controleCarga.getIdControleCarga());
        ccDMN.setNrControleCarga(controleCarga.getNrControleCarga());
        ccDMN.setIdFilialOrigem(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
        ccDMN.setIdFilialDestino(controleCarga.getFilialByIdFilialDestino().getIdFilial());
        ccDMN.setTpControleCarga(controleCarga.getTpControleCarga().getValue());
        ccDMN.setTpStatusControleCarga(controleCarga.getTpStatusControleCarga().getValue());
        
        ccDMN.setIdEventoControleCarga(eventoControleCarga.getIdEventoControleCarga());
        ccDMN.setIdFilialEvento(eventoControleCarga.getFilial().getIdFilial());
        ccDMN.setDhEvento(eventoControleCarga.getDhEvento());
        ccDMN.setTpEventoControleCarga(eventoControleCarga.getTpEventoControleCarga().getValue());
        
        JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.EVENTO_CONTROLE_CARGA, ccDMN);
        integracaoJmsService.storeMessage(jmsMessageSender);
    }

    private void generateEventoChangeStatusControleCargaAddEvento(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, EventoControleCarga eventoControleCarga) {
        if (adsmNativeBatchSqlOperations != null) {
            Map<String, Object> eventoControleCargaKeyValueMap = new HashMap<String, Object>();
            eventoControleCargaKeyValueMap.put("ID_FILIAL", eventoControleCarga.getFilial().getIdFilial());
            eventoControleCargaKeyValueMap.put("DH_EVENTO", eventoControleCarga.getDhEvento());
            eventoControleCargaKeyValueMap.put("DH_EVENTO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
            eventoControleCargaKeyValueMap.put("TP_EVENTO_CONTROLE_CARGA", eventoControleCarga.getTpEventoControleCarga().getValue());
            eventoControleCargaKeyValueMap.put("ID_USUARIO", eventoControleCarga.getUsuario().getIdUsuario());
            eventoControleCargaKeyValueMap.put("ID_EQUIPE_OPERACAO", eventoControleCarga.getEquipeOperacao().getIdEquipeOperacao());
            eventoControleCargaKeyValueMap.put("ID_MEIO_TRANSPORTE", eventoControleCarga.getMeioTransporte().getIdMeioTransporte());
            eventoControleCargaKeyValueMap.put("DS_EVENTO", eventoControleCarga.getDsEvento());
            eventoControleCargaKeyValueMap.put("VL_LIBERACAO", eventoControleCarga.getVlLiberacao());
            if (eventoControleCarga.getMoeda() != null) {
                eventoControleCargaKeyValueMap.put("ID_MOEDA", eventoControleCarga.getMoeda().getIdMoeda());
            }
            eventoControleCargaKeyValueMap.put("PS_REAL", eventoControleCarga.getPsReal());
            eventoControleCargaKeyValueMap.put("PS_AFORADO", eventoControleCarga.getPsAforado());
            eventoControleCargaKeyValueMap.put("VL_TOTAL", eventoControleCarga.getVlTotal());
            eventoControleCargaKeyValueMap.put("PC_OCUPACAO_CALCULADO", eventoControleCarga.getPcOcupacaoCalculado());
            eventoControleCargaKeyValueMap.put("PC_OCUPACAO_AFORADO_CALCULADO", eventoControleCarga.getPcOcupacaoAforadoCalculado());
            eventoControleCargaKeyValueMap.put("PC_OCUPACAO_INFORMADO", eventoControleCarga.getPcOcupacaoInformado());
            eventoControleCargaKeyValueMap.put("ID_CONTROLE_CARGA", eventoControleCarga.getControleCarga().getIdControleCarga());

            adsmNativeBatchSqlOperations.addNativeBatchInsert("EVENTO_CONTROLE_CARGA", eventoControleCargaKeyValueMap);
        } else {
            this.eventoControleCargaService.store(eventoControleCarga);
        }
    }

    private void generateEventoChangeStatusControleCargaUpdate(ControleCarga controleCarga, boolean store, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, boolean updateTransponder) {
        if (adsmNativeBatchSqlOperations != null) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues("CONTROLE_CARGA",
                    "TP_STATUS_CONTROLE_CARGA", controleCarga.getTpStatusControleCarga().getValue());
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues("CONTROLE_CARGA",
                    controleCarga.getIdControleCarga());
        } else {
            if (store) {
                super.store(controleCarga);
            }
        }
        if (updateTransponder) {
            transponderService.executeAtualizaPosicaoTransponder(controleCarga);
        }
    }

    /**
     * @param controleCarga
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoCD(ControleCarga controleCarga, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("AD"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            throw new BusinessException("LMS-05013");
        }
    }

    /**
     * @param controleCarga
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoCC(ControleCarga controleCarga, Long idFilialSessao, Long idFilialOrigem, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("EC"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            if (idFilialSessao.equals(idFilialOrigem)) {
                controleCarga.setTpStatusControleCarga(new DomainValue("EC"));
            } else {
                controleCarga.setTpStatusControleCarga(new DomainValue("CP"));
            }
        }
    }

    /**
     * @param controleCarga
     */
    private void trocaStatusControleCargaTpEventoPO(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
    }

    /**
     * @param controleCarga
     */
    private void trocaStatusControleCargaTpEventoFM(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
    }

    private void trocaStatusControleCargaTpEventoTP(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("TP"));
    }

    /**
     * @param controleCarga
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoEM(ControleCarga controleCarga, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("AE"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("AV"));
        }
    }

    /**
     * @param controleCarga
     */
    private void trocaStatusControleCargaTpEventoFC(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
    }

    /**
     * @param controleCarga
     */
    private void trocaStatusControleCargaTpEventoEC(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
    }

    /**
     * @param controleCarga
     * @param idFilialSessao
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoIC(ControleCarga controleCarga, Long idFilialSessao, Long idFilialOrigem, Long idFilialDestino, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("EC"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            if (idFilialSessao.equals(idFilialOrigem)) {
                controleCarga.setTpStatusControleCarga(new DomainValue("EC"));
            } else if ((!idFilialSessao.equals(idFilialDestino)) && (!idFilialSessao.equals(idFilialOrigem))) {
                controleCarga.setTpStatusControleCarga(new DomainValue("CP"));
            } else if (idFilialSessao.equals(idFilialOrigem)) {
                throw new BusinessException("LMS-05012");
            }
        }
    }

    /**
     * @param controleCarga
     */
    private void trocaStatusControleCargaTpEventoCA(ControleCarga controleCarga) {
        controleCarga.setTpStatusControleCarga(new DomainValue("CA"));
    }

    /**
     * @param controleCarga
     * @param idFilialSessao
     * @param idFilialDestino
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoFD(ControleCarga controleCarga, Long idFilialSessao, Long idFilialDestino, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            if (idFilialSessao.equals(idFilialDestino)) {
                controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
            } else {
                controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
            }
        }
    }

    /**
     * @param controleCarga
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoSP(ControleCarga controleCarga, String tpControleCarga) {
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("TC"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("EV"));
        }
    }

    /**
     * @param controleCarga
     * @param idFilialSessao
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoID(ControleCarga controleCarga, Long idFilialSessao, Long idFilialOrigem, Long idFilialDestino, String tpControleCarga) {
        //Início de Descarga
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("ED"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            if (idFilialSessao.equals(idFilialDestino)) {
                controleCarga.setTpStatusControleCarga(new DomainValue("ED"));
            } else if ((!idFilialSessao.equals(idFilialDestino)) && (!idFilialSessao.equals(idFilialOrigem))) {
                controleCarga.setTpStatusControleCarga(new DomainValue("EP"));
            } else if (idFilialSessao.equals(idFilialOrigem)) {
                throw new BusinessException("LMS-05011");
            }
        }
    }

    /**
     * @param controleCarga
     * @param idFilialSessao
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpControleCarga
     */
    private void trocaStatusControleCargaTpEventoCP(ControleCarga controleCarga, Long idFilialSessao, Long idFilialOrigem, Long idFilialDestino, String tpControleCarga) {
        //Chegada na Portaria
        if (tpControleCarga.equalsIgnoreCase(COLETA)) {
            controleCarga.setTpStatusControleCarga(new DomainValue("AD"));
        } else if ("V".equalsIgnoreCase(tpControleCarga)) {
            if (idFilialSessao.equals(idFilialDestino)) {
                controleCarga.setTpStatusControleCarga(new DomainValue("AD"));
            } else if ((!idFilialSessao.equals(idFilialDestino)) && (!idFilialSessao.equals(idFilialOrigem))) {
                controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
            } else if (idFilialSessao.equals(idFilialOrigem)) {
                throw new BusinessException("LMS-05010");
            }
        }
    }

    /**
     * @param idControleCarga
     * @param tpControleCarga
     * @param idTransportado
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findControleCargaByTransmitirColeta(Long idControleCarga, String tpControleCarga, Long idTransportado) {
        return getControleCargaDAO().findControleCargaByTransmitirColeta(idControleCarga, tpControleCarga, idTransportado);
    }

    @SuppressWarnings("rawtypes")
    public List findControleCargaByNrControleByFilial(Long nrControleCarga, Long idFilial) {
        return getControleCargaDAO().findControleCargaByNrControleByFilial(nrControleCarga, idFilial);
    }

    public ControleCarga findControleCargaByNrControleCargaByIdFilial(Long nrControleCarga, Long idFilial) {
        return getControleCargaDAO().findControleCargaByNrControleCargaByIdFilial(nrControleCarga, idFilial);
    }

    public void updateExigenciaCIOTEnviadoIntegCIOT(Long idControleCarga, Boolean blExigeCIOT, Boolean blEnviadoIntegCIOT) {
        getControleCargaDAO().updateExigenciaCIOTEnviadoIntegCIOT(idControleCarga, blExigeCIOT, blEnviadoIntegCIOT);
    }

    @SuppressWarnings("rawtypes")
    public List findDocumentosControleCargaInternacional(Long idControleCarga) {
        return getControleCargaDAO().findDocumentosControleCargaInternacional(idControleCarga);
    }

    public Object[] findMoedaPaisByControleCarga(Long idControleCarga) {
        return getControleCargaDAO().findMoedaPaisByControleCarga(idControleCarga);
    }

    @SuppressWarnings("rawtypes")
    public List findDocumentosControleCargaNacional(Long idControleCarga) {
        return getControleCargaDAO().findDocumentosControleCargaNacional(idControleCarga);
    }

    @SuppressWarnings("rawtypes")
    public List findDocumentosControleCargaEntrega(Long idControleCarga) {
        return getControleCargaDAO().findDocumentosControleCargaEntrega(idControleCarga);
    }

    public boolean findExistsOnlyClienteRemetenteComEtiquetaEDI(Long idControleCarga) {
        return getControleCargaDAO().findExistsOnlyClienteRemetenteComEtiquetaEDI(idControleCarga);
    }

    @SuppressWarnings("rawtypes")
    public List findDadosChegadaSaida(Long idMeioTransporte, String tpControleCarga) {
        return getControleCargaDAO().findDadosChegadaSaida(idMeioTransporte, tpControleCarga);
    }

    @SuppressWarnings("rawtypes")
    public List findControleCargaByManifesto(Long idManifesto, Long idManifestoColeta) {
        return getControleCargaDAO().findControleCargaByManifesto(idManifesto, idManifestoColeta);
    }

    public void validateGeracaoCiot(Long idFilial, ControleCarga controleCarga) {
        if (controleCarga.getBlExigeCIOT() != null && controleCarga.getBlExigeCIOT() && isFilialBloqueiaCiot(idFilial)) {
            CIOT ciot = null;
            CIOTControleCarga ciotControleCarga = ciotControleCargaService.findByIdControleCargaIdMeioTransporte(controleCarga.getIdControleCarga(), controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
            if (ciotControleCarga != null) {
                ciot = ciotControleCarga.getCiot();
            }

            if (ciot == null || !ciot.getTpSituacao().getValue().equals(CIOT_GERADO)) {
                throw new BusinessException("LMS-05412");
            }
        }
    }

    public boolean isFilialBloqueiaCiot(Long idFilialUsuario) {
        String indicadorBloqueiaCiot = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "BLOQUEIA_CIOT", false);
        return "S".equals(indicadorBloqueiaCiot);
    }

    /**
     * Valida um veículo passado como parâmetro, verificando se o mesmo pode ser vinculado a um novo Controle de Carga
     *
     * @param idMeioTransporte
     */
    public void validateVeiculoControleCarga(Long idMeioTransporte, boolean isMWW) {
        validateVeiculoControleCarga(idMeioTransporte, null, isMWW);
    }

    /**
     * Valida um veículo passado como parâmetro, verificando se o mesmo pode ser vinculado a um novo Controle de Carga
     *
     * @param idMeioTransporte
     */
    public void validateVeiculoControleCarga(Long idMeioTransporte, Long idControleCarga, boolean isMWW) {
        /* Regra 1 */
        validateVeiculoAtivo(idMeioTransporte);

        validateDadosIniciais(idMeioTransporte);

		/* Regra 2 - Verifica se o meio de transporte não é um semi-reboque */
        meioTransporteService.validateMeioTransporteIsVeiculo(idMeioTransporte);

    	/* Regra 3 - Verifica se o meio de transporte possui um semi-reboque cadastrado */
        Boolean blUtilizaSemiReboque = null;
        TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findTipoMeioTransporteCompostoByIdMeioTransporte(idMeioTransporte);
        if (tipoMeioTransporte != null) {
            blUtilizaSemiReboque = Boolean.TRUE;
        }

        validateVeiculoManifestoEntregaAberto(idMeioTransporte, blUtilizaSemiReboque, idControleCarga);

    	/* Regra 4 */
        validateVeiculoAssociadoControleCarga(idMeioTransporte, blUtilizaSemiReboque, idControleCarga);

		/* Regra 7 */
        if (!isMWW) {
            validateLocalVeiculo(idMeioTransporte, false);
        }

    	/* Regra 5 */
        validateVeiculoOrdemSaidaEmAberto(idMeioTransporte);
        meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(idMeioTransporte));
    }

    @SuppressWarnings("rawtypes")
    private void validateDadosIniciais(Long idMeioTransporte) {
        Proprietario proprietario = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(idMeioTransporte, JTDateTimeUtils.getDataAtual());
        if (proprietario == null) {
            throw new BusinessException("LMS-25036");
        }

        if (proprietario.getPessoa() != null && proprietario.getPessoa().getEnderecoPessoa() == null) {
            throw new BusinessException("LMS-26096");
        }

        List listaBloqueios = bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(idMeioTransporte);
        if (!listaBloqueios.isEmpty()) {
            throw new BusinessException("LMS-25010");
        }
    }

    /**
     * Regra 1 - validateVeiculoControleCarga
     *
     * @param idMeioTransporte
     */
    private void validateVeiculoAtivo(Long idMeioTransporte) {
        if (!meioTransporteService.validateMeioTransporteAtivo(idMeioTransporte)) {
            throw new BusinessException("LMS-00058");
        }
    }

    /**
     * Verifica se existe algum manifesto em aberto para o semi reboque.
     *
     * @param idSemiRebocado
     */
    private void validateSemiReboqueManifestoEntregaAberto(Long idSemiRebocado) {
        List<Object[]> listaFilial = getControleCargaDAO().findFilialControleCargaManifestoAberto(null, idSemiRebocado, null, true);

        if (!listaFilial.isEmpty()) {
            throw new BusinessException("LMS-05400", new Object[]{StringUtils.join(listaFilial.toArray(), ", ")});
        }
    }

    /**
     * Verifica se existe algum manifesto em aberto para o veículo.
     *
     * @param idMeioTransporte
     * @param blUtilizaSemiReboque
     * @param idControleCarga
     */
    private void validateVeiculoManifestoEntregaAberto(Long idMeioTransporte, Boolean blUtilizaSemiReboque, Long idControleCarga) {
        if (blUtilizaSemiReboque == null || !blUtilizaSemiReboque) {
            List<Object[]> listaFilial = getControleCargaDAO().findFilialControleCargaManifestoAberto(idMeioTransporte, null, idControleCarga, blUtilizaSemiReboque);

            if (!listaFilial.isEmpty()) {
                throw new BusinessException("LMS-05398", new Object[]{StringUtils.join(listaFilial.toArray(), ", ")});
            }
        }
    }

    /**
     * Regra 4 - validateVeiculoControleCarga
     *
     * @param idMeioTransporte
     * @param blUtilizaSemiReboque
     */
    @SuppressWarnings("unchecked")
    private void validateVeiculoAssociadoControleCarga(Long idMeioTransporte, Boolean blUtilizaSemiReboque, Long idControleCarga) {
        List<ControleCarga> resultado = getControleCargaDAO().validateMeioTransporteInControleCarga(idMeioTransporte, null, blUtilizaSemiReboque);

        if (!resultado.isEmpty() && !verificaPuxada(resultado)) {
            throw new BusinessException("LMS-05337");
        }

        if (validateMeioTranporte(resultado, idControleCarga)) {
            throw new BusinessException("LMS-05001", new Object[]{
                    getValorControleCarga(resultado), getSiglaFilialAtualizaStatus(resultado)});
        }
    }

    private boolean validateMeioTranporte(List<ControleCarga> controlesCargas, Long idControleCarga) {
        //•	Se o idControleCarga foi passado por parâmetro, verificar se algum dos id do controle de carga é diferente do idControleCarga
        if (controlesCargas != null) {
            for (Object object : controlesCargas.toArray()) {
                if (idControleCarga != null && idControleCarga.compareTo(((ControleCarga) object).getIdControleCarga()) == 0) {
                    controlesCargas.remove(object);
                }
            }
        }

        if (controlesCargas == null || controlesCargas.isEmpty()) {
            return false;
        }

        //•	E possua um frete DIFERENTE de *FRETE PORTO* (retornando lista vazia os testes abaixo lançarão a exception)
        List<ControleCarga> portos = this.verificaPorto(controlesCargas);
        if (portos == null || portos.isEmpty()) {
            return false;
        }

        if (verificaControleCargaAereoEmprestimo(controlesCargas)) {
            return false;
        }

        if (verificaTipoManifesto(controlesCargas)) {
            return false;
        }

        return !controlesCargas.isEmpty();
    }

    private boolean verificaTipoManifesto(List<ControleCarga> controleCargas) {
        boolean result = true;
        for (Object o : controleCargas.toArray()) {
            ControleCarga cc = (ControleCarga) o;
            if (getControleCargaDAO().findValidacaoTipoManifestoOperacao(cc)) {
                result = false;
            } else {
                controleCargas.remove(cc);
            }
        }
        return result;
    }


    private boolean verificaControleCargaAereoEmprestimo(List<ControleCarga> controleCargas) {
        boolean result = true;
        if (controleCargas != null) {
            for (Object o : controleCargas.toArray()) {
                ControleCarga cc = (ControleCarga) o;

                if ((!getControleCargaDAO().findSaiuFilialOrigem(cc)) &&
                        !(getControleCargaDAO().findColeta(cc) || getControleCargaDAO().findEntrega(cc))) {
                    result = false;
                } else {
                    controleCargas.remove(cc);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private boolean verificaPuxada(List controleCargas) {
        if (controleCargas != null) {

            for (Object o : controleCargas) {
                ControleCarga controleCarga = (ControleCarga) o;
                if (getControleCargaDAO().findEmPuxada(controleCarga)) {
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Verifica a existência de fluxo filial com ocorrência de porto entre as filiais.
     * Caso isto ocorra, a lista passada por parâmetro deve ser limpa.
     *
     * @param controleCarga
     * @return
     */
    private List<ControleCarga> verificaPorto(List<ControleCarga> controleCarga) {

        Servico servico = this.getServicoService().findServicoByFluxoFilial();
        if (servico == null) {
            servico = new Servico();
        }
        List<ControleCarga> ccNovos = new ArrayList<ControleCarga>();

        if (controleCarga != null) {
            for (Object o : controleCarga.toArray()) {
                ControleCarga cc = (ControleCarga) o;
                Long idFilialOrigem = null;
                Long idFilialDestino = null;

                if (cc.getFilialByIdFilialOrigem() != null) {
                    idFilialOrigem = cc.getFilialByIdFilialAtualizaStatus().getIdFilial();
                }
                if (cc.getFilialByIdFilialDestino() != null) {
                    idFilialDestino = cc.getFilialByIdFilialDestino().getIdFilial();
                }

                YearMonthDay diaAtual = new YearMonthDay();
                FluxoFilial ff = fluxoFilialService.findFluxoFilial(idFilialOrigem, idFilialDestino, diaAtual, servico.getIdServico());

                if (ff == null || (ff != null && (ff.getBlPorto() == null || !ff.getBlPorto()))) {
                    ccNovos.add(cc);
                } else {
                    controleCarga.remove(cc);
                }
            }
        }
        return ccNovos;
    }


    /**
     * Regra 5 - validateVeiculoControleCarga
     * Verifica se existe alguma ordem de saída aberta para o veículo informado.
     *
     * @param idMeioTransporte
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void validateVeiculoOrdemSaidaEmAberto(Long idMeioTransporte) {
        Map mapMeioTransporteTransportado = new HashMap();
        mapMeioTransporteTransportado.put(ID_MEIO_TRANSPORTE, idMeioTransporte);
        Map mapOrdemSaida = new HashMap();
        mapOrdemSaida.put("meioTransporteRodoviarioByIdMeioTransporte", mapMeioTransporteTransportado);
        List<OrdemSaida> ordensSaida = ordemSaidaService.findByMeioTransporteInOrdemSaida(mapOrdemSaida);
        if (ordensSaida != null && !ordensSaida.isEmpty()) {
            OrdemSaida ordemSaida = ordensSaida.get(0);
            throw new BusinessException("LMS-05007", new Object[]{ordemSaida.getFilialByIdFilialOrigem().getSgFilial()});
        }

    }

    /**
     * Método que valida a localização em que o veículo (com tpmodal = R) se encontra, caso a filial esteja configurada para esta validação.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void validateLocalVeiculo(Long idMeioTransporte, boolean semiReboque) {
        Filial filial = SessionUtils.getFilialSessao();
        if (filial.getBlValidaLocalVeiculo()) {
            MeioTransporte meioTransporte = meioTransporteService.findById(idMeioTransporte);
            if ("R".equals(meioTransporte.getTpModal().getValue())) {
                ControleCarga ccUltimo = findUltimoControleCargaComMeioTransporte(idMeioTransporte, semiReboque);

                if (ccUltimo != null && !filial.getIdFilial().equals(ccUltimo.getFilialByIdFilialAtualizaStatus().getIdFilial())) {
                    TypedFlatMap filter = new TypedFlatMap();
                    filter.put(ID_MEIO_TRANSPORTE, idMeioTransporte);
                    filter.put(DH_GERACAO, ccUltimo.getDhGeracao());
                    filter.put(SEMI_REBOQUE, semiReboque);
                    OrdemSaida ordemSaida = ordemSaidaService.findUltimaOrdemSaida(filter);
                    boolean deveValidar = true;
                    if (!semiReboque) {//Estas regras não devem ser consideradas para validação de Semi-Reboque

                        ArrayList lstControleCarga = new ArrayList();
                        lstControleCarga.add(ccUltimo);

                        //Esta lista retornará o CC se ele não for do tipo Frete Porto.
                        List resultado = this.verificaPorto(lstControleCarga);

                        //Deve validar se o CC é diferente de porto E manifesto.tpModal != A. Se uma das duas forem verdadeiras, não deve validar.
                        if (resultado.isEmpty()) {
                            deveValidar = false;
                        } else {
                            boolean aereoForaUnidadeOrigem = getControleCargaDAO().findSaiuFilialOrigem(ccUltimo);
                            deveValidar = !aereoForaUnidadeOrigem;
                        }
                    }

                    if (deveValidar) {
                        if (ordemSaida == null) {
                            throw new BusinessException("LMS-05364", new String[]{ccUltimo.getFilialByIdFilialOrigem().getSgFilial(), String.valueOf(ccUltimo.getNrControleCarga()), ccUltimo.getFilialByIdFilialAtualizaStatus().getSgFilial()});
                        } else if (ordemSaida.getBlSemRetorno()) {
                            if (ordemSaida.getDhSaida() == null) {
                                throw new BusinessException("LMS-05364", new String[]{ccUltimo.getFilialByIdFilialOrigem().getSgFilial(), String.valueOf(ccUltimo.getNrControleCarga()), ccUltimo.getFilialByIdFilialAtualizaStatus().getSgFilial()});
                            }
                        } else if (!filial.getIdFilial().equals(ordemSaida.getFilialByIdFilialOrigem().getIdFilial()) || ordemSaida.getDhChegada() == null) {
                            throw new BusinessException("LMS-05365", new String[]{ordemSaida.getFilialByIdFilialOrigem().getSgFilial()});
                        }
                    }
                }
            }
        }
    }

    /**
     * Método que retorna o último controle de carga com o meio de transporte passado por parâmetro.
     */
    private ControleCarga findUltimoControleCargaComMeioTransporte(Long idMeioTransporte, boolean semiReboque) {
        return getControleCargaDAO().findUltimoControleCargaComMeioTransporte(idMeioTransporte, semiReboque);
    }

    public Boolean validateGeracaoNotaCreditoParceiraByIdControleCargar(Long idControleCarga) {
        return getControleCargaDAO().validateGeracaoNotaCreditoParceiraByIdControleCarga(idControleCarga);
    }

    /**
     * Valida um semi-reboque passado como parâmetro, verificando se o mesmo pode ser vinculado a um novo Controle de Carga.
     *
     * @param idMeioTransporte
     */
    public void validateSemiReboqueControleCarga(Long idMeioTransporte) {
        /* Regra 1 */
        validateSemiReboqueAtivo(idMeioTransporte);

        validateSemiReboqueManifestoEntregaAberto(idMeioTransporte);

		/* Regra 2 */
        validateSemiReboqueAssociadoControleCarga(idMeioTransporte);
        /* Regra 3 - Verifica se o meio de transporte é um semi-reboque */
        meioTransporteService.validateMeioTransporteIsSemiReboque(idMeioTransporte);

        validateLocalVeiculo(idMeioTransporte, true);
    }

    /**
     * Regra 1 - validateSemiReboqueControleCarga
     *
     * @param idMeioTransporte
     */
    private void validateSemiReboqueAtivo(Long idMeioTransporte) {
        if (!meioTransporteService.validateMeioTransporteAtivo(idMeioTransporte)) {
            throw new BusinessException("LMS-00059");
        }
    }

    /**
     * Regra 2 - validateSemiReboqueControleCarga
     *
     * @param idMeioTransporte
     */
    @SuppressWarnings("unchecked")
    private void validateSemiReboqueAssociadoControleCarga(Long idMeioTransporte) {
        List<ControleCarga> resultado = getControleCargaDAO().validateMeioTransporteInControleCarga(null, idMeioTransporte, null);

        if (CollectionUtils.isNotEmpty(resultado) && !verificaTipoManifesto(resultado)) {
            throw new BusinessException("LMS-05008", new Object[]{getValorControleCarga(resultado), getSiglaFilialAtualizaStatus(resultado)});
        }
        meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(idMeioTransporte));
        meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(idMeioTransporte));
    }

    /**
     * Retorna o valor do controle de carga. Ex.: xxx 00000000
     *
     * @param lista
     * @return
     */
    private String getValorControleCarga(List<ControleCarga> lista) {
        StringBuilder sb = new StringBuilder();
        for (ControleCarga cc : lista) {
            String siglaNumero = FormatUtils.formatSgFilialWithLong(
                    cc.getFilialByIdFilialOrigem().getSgFilial(), cc.getNrControleCarga(), FORMAT_PATTERN);
            sb.append(siglaNumero);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Retorna a sigla da filial que deve finalizar a descarga dos manifestos
     * relacionados ao controle de carga em questão.
     *
     * @param lista
     * @return String
     */
    private String getSiglaFilialAtualizaStatus(List<ControleCarga> lista) {
        StringBuilder sb = new StringBuilder();
        for (ControleCarga cc : lista) {
            String sgFilial = cc.getFilialByIdFilialAtualizaStatus().getSgFilial();
            sb.append(sgFilial);
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados
     * parametros.
     *
     * @param criteria
     * @return Integer numero de registros
     */
    @SuppressWarnings("rawtypes")
    public Integer getRowCountByRotaColetaEntregaGE(Map criteria) {
        Long idRota = Long.valueOf(((Map) criteria.get("rotaColetaEntrega")).get("idRotaColetaEntrega").toString());
        return this.getControleCargaDAO().getRowCountByRotaColetaEntregaGE(idRota);
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um idRotaColetaEntrega seja informado.
     * Tem como restricao buscar apenas "Tipo de Status de Controle de Carga" e "Manifestos de Coleta" setado como "GE".
     *
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    @SuppressWarnings("rawtypes")
    public ResultSetPage findPaginatedByRotaColetaEntregaGE(Map criteria) {
        Long idRota = Long.valueOf(((Map) criteria.get("rotaColetaEntrega")).get("idRotaColetaEntrega").toString());
        return this.getControleCargaDAO().findPaginatedByRotaColetaEntregaGE(idRota, FindDefinition.createFindDefinition(criteria));
    }


    /**
     * Busca os dados do controle de carga informado.
     *
     * @param controleCarga
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TypedFlatMap findByIdByEmitirControleCargaColetaEntrega(ControleCarga controleCarga) {
        Filial filialUsuario = SessionUtils.getFilialSessao();

        Map mapControleCarga = new HashMap();
        mapControleCarga.put(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga());

        String labelReemissao = configuracoesFacade.getMensagem(REEMISSAO);
        String labelEmissao = configuracoesFacade.getMensagem(EMISSAO);

        TypedFlatMap typedFlatMap = new TypedFlatMap();

        Boolean blControleCargaEmitido = validateExisteEventoEmitido(controleCarga.getIdControleCarga());

        typedFlatMap.put(ACAO, blControleCargaEmitido ? labelReemissao : labelEmissao);
        typedFlatMap.put(BL_EMISSAO, !blControleCargaEmitido);

        typedFlatMap.put(TP_STATUS_CONTROLE_CARGA, controleCarga.getTpStatusControleCarga().getDescription().toString());
        typedFlatMap.put(BL_ENTREGA_DIRETA, controleCarga.getBlEntregaDireta());
        if (controleCarga.getSolicitacaoContratacao() != null) {
            typedFlatMap.put(SOLICITACAO_CONTRATACAO_ID_SOLICITACAO_CONTRATACAO, controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao());
            typedFlatMap.put(SOLICITACAO_CONTRATACAO_NR_SOLICITACAO_CONTRATACAO, controleCarga.getSolicitacaoContratacao().getNrSolicitacaoContratacao());
        }
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE, controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_FROTA, controleCarga.getMeioTransporteByIdTransportado().getNrFrota());
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_IDENTIFICADOR, controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_TP_VINCULO, controleCarga.getMeioTransporteByIdTransportado().getTpVinculo().getValue());
        }
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE, controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte());
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_FROTA, controleCarga.getMeioTransporteByIdSemiRebocado().getNrFrota());
            typedFlatMap.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_IDENTIFICADOR, controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
        }
        if (controleCarga.getRotaColetaEntrega() != null) {
            typedFlatMap.put(ROTA_COLETA_ENTREGA_ID_ROTA_COLETA_ENTREGA, controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
            typedFlatMap.put(ROTA_COLETA_ENTREGA_NR_ROTA, controleCarga.getRotaColetaEntrega().getNrRota());
            typedFlatMap.put(ROTA_COLETA_ENTREGA_DS_ROTA, controleCarga.getRotaColetaEntrega().getDsRota());
        }
        if (controleCarga.getProprietario() != null) {
            typedFlatMap.put(PROPRIETARIO_ID_PROPRIETARIO, controleCarga.getProprietario().getIdProprietario());
            typedFlatMap.put(PROPRIETARIO_PESSOA_NR_IDENTIFICACAO_FORMATADO, FormatUtils.formatIdentificacao(controleCarga.getProprietario().getPessoa()));
            typedFlatMap.put(PROPRIETARIO_PESSOA_NM_PESSOA, controleCarga.getProprietario().getPessoa().getNmPessoa());
        }

        typedFlatMap.put(PC_OCIOSIDADE_VISUAL, controleCarga.getPcOcupacaoInformado() == null ? null : (BigDecimalUtils.HUNDRED).subtract(controleCarga.getPcOcupacaoInformado()));

        if (controleCarga.getTipoTabelaColetaEntrega() != null) {
            typedFlatMap.put(TIPO_TABELA_COLETA_ENTREGA_ID_TIPO_TABELA_COLETA_ENTREGA, controleCarga.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega());
        }
        if (controleCarga.getTabelaColetaEntrega() != null) {
            typedFlatMap.put(TABELA_COLETA_ENTREGA_ID_TABELA_COLETA_ENTREGA, controleCarga.getTabelaColetaEntrega().getIdTabelaColetaEntrega());
        }
        if (controleCarga.getMotorista() != null) {
            String tpIdentificacao = FormatUtils.formatIdentificacao(controleCarga.getMotorista().getPessoa().getTpIdentificacao(), controleCarga.getMotorista().getPessoa().getNrIdentificacao());
            typedFlatMap.put(MOTORISTA_ID_MOTORISTA, controleCarga.getMotorista().getIdMotorista());
            typedFlatMap.put(MOTORISTA_PESSOA_NR_IDENTIFICACAO, controleCarga.getMotorista().getPessoa().getNrIdentificacao());
            typedFlatMap.put(MOTORISTA_PESSOA_NR_IDENTIFICACAO_FORMATADO, tpIdentificacao);
            typedFlatMap.put(MOTORISTA_PESSOA_NM_PESSOA, controleCarga.getMotorista().getPessoa().getNmPessoa());
        }

        Map mapControleQuilometragem = new HashMap();
        mapControleQuilometragem.put(CONTROLE_CARGA, mapControleCarga);
        mapControleQuilometragem.put(BL_SAIDA, Boolean.TRUE);
        List listControleQuilometragem = controleQuilometragemService.find(mapControleQuilometragem);

        if (listControleQuilometragem != null && !listControleQuilometragem.isEmpty() && ((ControleQuilometragem) listControleQuilometragem.get(0)).getNrQuilometragem() != null) {
            typedFlatMap.put(NR_QUILOMETRAGEM, FormatUtils.formatDecimal("###,##0", ((ControleQuilometragem) listControleQuilometragem.get(0)).getNrQuilometragem()));
        } else {
            typedFlatMap.put(NR_QUILOMETRAGEM, null);
        }

        typedFlatMap.put(BL_INFORMA_KM_PORTARIA, filialUsuario.getBlInformaKmPortaria());

        typedFlatMap.put(FILIAL_USUARIO_ID_FILIAL, filialUsuario.getIdFilial());
        typedFlatMap.put(FILIAL_USUARIO_SG_FILIAL, filialUsuario.getSgFilial());
        typedFlatMap.put(FILIAL_USUARIO_PESSOA_NM_FANTASIA, filialUsuario.getPessoa().getNmFantasia());

        EquipeOperacao equipeOperacao = equipeOperacaoService.findByIdControleCargaColetaEntrega(controleCarga.getIdControleCarga());
        if (equipeOperacao != null) {
            typedFlatMap.put(ID_EQUIPE_OPERACAO, equipeOperacao.getIdEquipeOperacao());
            typedFlatMap.put(ID_EQUIPE, equipeOperacao.getEquipe().getIdEquipe());
        }

        //LMS-5146
        List<ManifestoEletronico> manifestoEletronicos = 
                manifestoEletronicoService.findManifestoEletronicoByControleCargaAndTpSituacao(
                        controleCarga.getIdControleCarga(), null, (String[]) null);
        if (manifestoEletronicos != null && !manifestoEletronicos.isEmpty()) {
            typedFlatMap.put(BTN_MDFE, "true");
        } else {
            typedFlatMap.put(BTN_MDFE, "false");
        }

        return typedFlatMap;
    }


    /**
     * Gerar o registro de inicio de carregamento, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja <code>null</code> ou atualiza a entidade, caso contrário.
     *
     * @param criteria
     * @param controleCarga
     * @param itemListIntegranteEqOperac list contem filhos da entidade IntegranteEqOperac a ser armazenada
     * @param itemListPagtoPedagioCc     list contem filhos da entidade PagtoPedagioCc a ser armazenada
     * @param itemListPostoPassagemCc    list contem filhos da entidade PostoPassagemCc a ser armazenada
     * @return
     */
    public TypedFlatMap storeEmitirControleCargasColetaEntrega(
            TypedFlatMap criteria,
            ControleCarga controleCarga,
            ItemList itemListIntegranteEqOperac, ItemListConfig itemListConfigIntegranteEqOperac,
            ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc,
            ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc) {
        TypedFlatMap mapRetorno = new TypedFlatMap();

        Long idEquipeOperacao = criteria.getLong(ID_EQUIPE_OPERACAO);
        Long idEquipe = criteria.getLong(ID_EQUIPE);
        Long idMotorista = criteria.getLong(MOTORISTA_ID_MOTORISTA);

        try {
            storeControleCargaByEmitirColetaEntrega(
                    controleCarga,
                    criteria.getLong(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE),
                    criteria.getBoolean("veiculoInformadoManualmente"),
                    criteria.getLong(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE),
                    criteria.getBoolean("semiReboqueInformadoManualmente"),
                    idMotorista,
                    criteria.getBoolean("motoristaInformadoManualmente"),
                    criteria.getLong(PROPRIETARIO_ID_PROPRIETARIO),
                    criteria.getLong(TIPO_TABELA_COLETA_ENTREGA_ID_TIPO_TABELA_COLETA_ENTREGA),
                    criteria.getLong(TABELA_COLETA_ENTREGA_ID_TABELA_COLETA_ENTREGA),
                    criteria.getBigDecimal(PC_OCIOSIDADE_VISUAL),
                    criteria.getInteger(NR_QUILOMETRAGEM),
                    criteria.getBoolean("blVirouHodometro"),
                    criteria.getBoolean("blQuilometragemInformadoManualmente"),
                    criteria.getString("obControleQuilometragem"));

            if (idEquipe != null) {
                idEquipeOperacao = storeEquipeOperacao(itemListIntegranteEqOperac,
                        itemListConfigIntegranteEqOperac, controleCarga, idEquipeOperacao, idEquipe);
                mapRetorno.put(ID_EQUIPE_OPERACAO, idEquipeOperacao);
            }

            storePagtoPedagioCcAndPostosPassagemCc(controleCarga, itemListPagtoPedagioCc,
                    itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);

        } catch (RuntimeException runEx) {
            this.rollbackMasterState(controleCarga, false, runEx, true);
            itemListIntegranteEqOperac.rollbackItemsState();
            itemListPagtoPedagioCc.rollbackItemsState();
            itemListPostoPassagemCc.rollbackItemsState();
            throw runEx;
        }
        return mapRetorno;
    }


    /**
     * Valida os integrantes da equipe e salva a equipe operacação.
     * Usado pelo EmitirControleCargaColetaEntrega e GerarControleCarga
     *
     * @param itemList
     * @param itemListConfig
     * @param controleCarga
     * @param idEquipeOperacao
     * @param idEquipe
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Long storeEquipeOperacao(ItemList itemList, ItemListConfig itemListConfig,
                                     ControleCarga controleCarga, Long idEquipeOperacao, Long idEquipe) {
        EquipeOperacao equipeOperacao = null;
        if (idEquipeOperacao != null) {
            equipeOperacao = equipeOperacaoService.findById(idEquipeOperacao);
        } else if (idEquipe != null) {
            equipeOperacao = new EquipeOperacao();
            equipeOperacao.setIdEquipeOperacao(null);
            equipeOperacao.setEquipe(equipeService.findById(idEquipe));
            equipeOperacao.setControleCarga(controleCarga);
            equipeOperacao.setDhInicioOperacao(JTDateTimeUtils.getDataHoraAtual());
            equipeOperacao.setCarregamentoDescarga(null);
            equipeOperacao.setDhFimOperacao(null);
        }

        if (equipeOperacao != null) {
            // A equipe deve pelo menos ter um funcionário ou terceiro
            if (!itemList.hasItems()) {
                throw new BusinessException("LMS-05101");
            }

            for (Iterator iter = itemList.iterator(null, itemListConfig); iter.hasNext(); ) {
                IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
                integranteEqOperac.setEquipeOperacao(equipeOperacao);
                Long idPessoa = null;
                Long idUsuario = null;
                if (integranteEqOperac.getPessoa() != null) {
                    idPessoa = integranteEqOperac.getPessoa().getIdPessoa();
                }
                if (integranteEqOperac.getUsuario() != null) {
                    idUsuario = integranteEqOperac.getUsuario().getIdUsuario();
                }
                validateIntegranteEmEquipesComControleCarga(controleCarga.getIdControleCarga(), idPessoa, idUsuario);
                if ("F".equals(integranteEqOperac.getTpIntegrante().getValue())) {
                    integranteEqOperac.setCargoOperacional(null);
                }
            }
            idEquipeOperacao = (Long) equipeOperacaoService.storeEquipeOperacao(equipeOperacao, itemList);
        }
        return idEquipeOperacao;
    }


    /**
     * @param idControleCarga
     * @param tpStatusControleCarga
     * @param blEmissao
     */
    public void generatePreEmissaoControleCarga(Long idControleCarga, String tpStatusControleCarga, Boolean blEmissao) {
        boolean calculoPadrao = isCalculoPadrao();

        if (!calculoPadrao && !getControleCargaDAO().isControleCargaNullTest(idControleCarga) && !getControleCargaDAO().isControleCargaNotNullTest(idControleCarga)) {
            throw new BusinessException("LMS-05386");
        }

        if (!manifestoService.findVerificaManifestosAssociados(idControleCarga) &&
                !manifestoColetaService.findVerificaManifestosAssociados(idControleCarga)) {
            throw new BusinessException("LMS-02095");
        }
        if (blEmissao && !"PO".equals(tpStatusControleCarga) && !"GE".equals(tpStatusControleCarga)) {
            throw new BusinessException("LMS-02065");
        }
        if (manifestoService.findVerificaExisteManifestoNaoEmitido(idControleCarga)
                || manifestoColetaService.findVerificaExisteManifestoNaoEmitido(idControleCarga)) {
            throw new BusinessException("LMS-02064");
        }

        MotoristaControleCarga motoristaControleCarga = motoristaControleCargaService.findMotoristaCcByIdControleCarga(idControleCarga);
        if (blEmissao && motoristaControleCarga != null && motoristaControleCarga.getLiberacaoReguladora() != null
                && motoristaControleCarga.getLiberacaoReguladora().getDtVencimento() != null &&
                JTDateTimeUtils.comparaData(JTDateTimeUtils.getDataAtual(), motoristaControleCarga.getLiberacaoReguladora().getDtVencimento()) > 0) {
            throw new BusinessException("LMS-02086");
        }
    }

    /**
     * Busca todos os dados da tela de controleCargaCarregamento a partir do 'nrControleCarga'
     *
     * @param nrControleCarga
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findControleCargaByNrControleCarga(Long nrControleCarga, Long idFilial, String tpOperacao) {
        return this.getControleCargaDAO().findControleCargaByNrControleCarga(nrControleCarga, idFilial, tpOperacao);
    }

    /**
     * Metodo para Emitir Controle de Carga Coleta Entrega
     *
     * @param idControleCarga
     * @param idMeioTransporte
     * @param idSemiReboque
     * @param idEquipeOperacao
     * @param blEmissao
     */

    public void generatePosEmissaoControleCarga(Long idControleCarga, Long idMeioTransporte, Long idSemiReboque, Long idEquipeOperacao, Boolean blEmissao) {
        generatePosEmissaoControleCarga(idControleCarga, idMeioTransporte, idSemiReboque, idEquipeOperacao, blEmissao, null);
    }

    public void generatePosEmissaoControleCarga(Long idControleCarga, Long idMeioTransporte, Long idSemiReboque, Long idEquipeOperacao, Boolean blEmissao, Long idFilial) {
        if (blEmissao) {

            idFilial = idFilial != null ? idFilial : SessionUtils.getFilialSessao().getIdFilial();

            DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
            ControleCarga controleCarga = findByIdInitLazyProperties(idControleCarga, false);
            Long idMoeda = null;
            if (controleCarga.getMoeda() != null) {
                idMoeda = controleCarga.getMoeda().getIdMoeda();
            }
            generateEventoChangeStatusControleCarga(controleCarga.getIdControleCarga(), idFilial, "EM", idEquipeOperacao,
                    idMeioTransporte, null, null, idMoeda, controleCarga.getPsTotalFrota(), controleCarga.getPsTotalAforado(), controleCarga.getVlTotalFrota(),
                    controleCarga.getPcOcupacaoCalculado(), controleCarga.getPcOcupacaoAforadoCalculado(), controleCarga.getPcOcupacaoInformado());

            // Evento 26.03.01.10 - Consultar Eventos dos Meios de Transporte
            // Regra 1.2 da ET 02.01.02.08 - Emitir Controle de Cargas Coleta
            // Entrega
            if (idMeioTransporte != null) {
                EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
                eventoMeioTransporte.setControleCarga(controleCarga);
                eventoMeioTransporte.setMeioTransporte(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
                eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue("AGSA"));
                eventoMeioTransporte.setDhInicioEvento(dataHoraAtual);
                eventoMeioTransporte.setFilial(SessionUtils.getFilialSessao());
                eventoMeioTransporte.setUsuario(SessionUtils.getUsuarioLogado());
                eventoMeioTransporteService.generateEvent(eventoMeioTransporte);
            }

            // Regra 1.3 da ET 02.01.02.08 - Emitir Controle de Cargas Coleta
            // Entrega
            if (idSemiReboque != null) {
                EventoMeioTransporte eventoSemiReboque = new EventoMeioTransporte();
                eventoSemiReboque.setControleCarga(controleCarga);
                eventoSemiReboque.setMeioTransporte(meioTransporteService.findByIdInitLazyProperties(idSemiReboque, false));
                eventoSemiReboque.setTpSituacaoMeioTransporte(new DomainValue("AGSA"));
                eventoSemiReboque.setDhInicioEvento(dataHoraAtual);
                eventoSemiReboque.setFilial(SessionUtils.getFilialSessao());
                eventoSemiReboque.setUsuario(SessionUtils.getUsuarioLogado());
                eventoMeioTransporteService.generateEvent(eventoSemiReboque);
            }

            // Regra 1.6 da ET 02.01.02.08 - Emitir Controle de Cargas Coleta
            // Entrega
            List<PedidoColeta> listPedidoColeta = pedidoColetaService.findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, null);
            for (PedidoColeta pedidoColeta : listPedidoColeta) {
                EventoColeta eventoColeta = eventoColetaService.findLastEventoColetaManifestada(pedidoColeta.getIdPedidoColeta());
                if (eventoColeta != null && eventoColeta.getMeioTransporteRodoviario() == null) {
                    MeioTransporte mt = meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false);
                    eventoColeta.setMeioTransporteRodoviario(mt.getMeioTransporteRodoviario());
                    eventoColetaService.store(eventoColeta);
                    // grava no topic de eventos de pedido coleta
                    eventoColetaService.storeMessageTopic(eventoColeta);
                }
            }

            // Evento 05.01.01.04 - Gerar Recibo de Posto de Passagem” passando
            // por parâmetro o Controle de Carga.
            reciboPostoPassagemService.generateReciboPostoPassagem(idControleCarga);


            enviarIntegracaoSmp(controleCarga);

            List<Manifesto> listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
            for (Manifesto manifesto : listManifestos) {
                if (!"FE".equals(manifesto.getTpStatusManifesto().getValue()) && !"CA".equals(manifesto.getTpStatusManifesto().getValue())) {
                    this.generateAcaoIntegracaoEvento(manifesto.getIdManifesto(), "PI LMS-FRMS");
                    this.generateAcaoIntegracaoEvento(manifesto.getIdManifesto(), "PI LMS-FRMSPROT");
                }
            }

            // LMS-5002
            generateAcaoIntegracaoEventoMT(idControleCarga);
        }
    }

    /**
     * enviar smp por versao
     */
    private void enviarIntegracaoSmp(ControleCarga controleCarga) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        String versaoGerRisco = (String) conteudoParametroFilialService.findConteudoByNomeParametro(
                idFilialUsuario, ConstantesGerRisco.SGR_VERS_INT_GER_RIS, false);
        if (ConstantesGerRisco.SGR_GER_RIS_VERS_1.equals(versaoGerRisco) || versaoGerRisco == null) {
            if (validateGeracaoAcaoIntegracaoEventoParaControleCarga(controleCarga.getIdControleCarga())) {
                this.generateAcaoIntegracaoEvento(controleCarga.getIdControleCarga(), "PI LMS-SMP");
            }
        } else if (ConstantesGerRisco.SGR_GER_RIS_VERS_2.equals(versaoGerRisco)) {
            SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService
                    .findByIdControleCargaAndFilial(controleCarga.getIdControleCarga(), idFilialUsuario);
            if (solicMonitPreventivo != null && solicMonitPreventivo.getIdSolicMonitPreventivo() != null) {
                gerarEnviarSMPService.generateEnviarSMP(controleCarga.getIdControleCarga(),
                        solicMonitPreventivo.getIdSolicMonitPreventivo());
            }
        } else if (ConstantesGerRisco.SGR_GER_RIS_AMBOS.equals(versaoGerRisco)) {
            if (validateGeracaoAcaoIntegracaoEventoParaControleCarga(controleCarga.getIdControleCarga())) {
                this.generateAcaoIntegracaoEvento(controleCarga.getIdControleCarga(), "PI LMS-SMP");
            }

            SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService
                    .findByIdControleCargaAndFilial(controleCarga.getIdControleCarga(), idFilialUsuario);
            if (solicMonitPreventivo != null && solicMonitPreventivo.getIdSolicMonitPreventivo() != null) {
                gerarEnviarSMPService.generateEnviarSMP(controleCarga.getIdControleCarga(),
                        solicMonitPreventivo.getIdSolicMonitPreventivo());
            }
        } else {
            // só para não parar o processo da emissão do controle de carga caso
            // o parametro esteja errado.
            // assim que todas as filiais estiverem na 2.0 esse trecho de código
            // pode ser removido, pois não sera mais dado manutenção no
            // parametro filial
            SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService.findByIdControleCargaAndFilial(
                    controleCarga.getIdControleCarga(), idFilialUsuario);
            if (solicMonitPreventivo != null && solicMonitPreventivo.getIdSolicMonitPreventivo() != null) {
                gerarEnviarSMPService.generateEnviarSMP(controleCarga.getIdControleCarga(),
                        solicMonitPreventivo.getIdSolicMonitPreventivo());
            }
        }
    }


    /**
     * @param controleCarga
     * @param idMeioTransporteTransportado
     * @param veiculoInformadoManualmente
     * @param idMeioTransporteSemiRebocado
     * @param semiReboqueInformadoManualmente
     * @param idMotorista
     * @param motoristaInformadoManualmente
     * @param idProprietario
     * @param idTipoTabelaColetaEntrega
     * @param idTabelaColetaEntrega
     * @param ociosidadeVisual
     * @param nrQuilometragem
     * @param blVirouHodomotreto
     * @param blQuilometragemInformadoManualmente
     * @param obControleQuilometragem
     */
    private void storeControleCargaByEmitirColetaEntrega(
            ControleCarga controleCarga,
            Long idMeioTransporteTransportado,
            Boolean veiculoInformadoManualmente,
            Long idMeioTransporteSemiRebocado,
            Boolean semiReboqueInformadoManualmente,
            Long idMotorista,
            Boolean motoristaInformadoManualmente,
            Long idProprietario,
            Long idTipoTabelaColetaEntrega,
            Long idTabelaColetaEntrega,
            BigDecimal ociosidadeVisual,
            Integer nrQuilometragem,
            Boolean blVirouHodomotreto,
            Boolean blQuilometragemInformadoManualmente,
            String obControleQuilometragem
    ) {
        Filial filialUsuario = SessionUtils.getFilialSessao();
        MeioTransporte meioTransporteTransportado = getMeioTransporte(idMeioTransporteTransportado);

        if (blQuilometragemInformadoManualmente) {
            //LMS-4572
            boolean isExisteLancamentoQuilometragem = findLancamentoQuilometragem(controleCarga.getIdControleCarga(), filialUsuario.getIdFilial(), Boolean.TRUE);

            if (!isExisteLancamentoQuilometragem) {
                storeQuilometragemMeioTransporte(filialUsuario.getIdFilial(), idMeioTransporteTransportado, nrQuilometragem,
                        blVirouHodomotreto, controleCarga.getIdControleCarga(), obControleQuilometragem);
            }

        }

        TabelaColetaEntrega tabelaColetaEntrega = getTabelaColetaEntrega(idTabelaColetaEntrega);
        TipoTabelaColetaEntrega tipoTabelaColetaEntrega = getTipoTabelaColetaEntrega(idTipoTabelaColetaEntrega);
        Proprietario proprietario = getProprietario(idProprietario);
        Motorista motorista = getMotorista(idMotorista);
        MeioTransporte meioTransporteSemiRebocado = getMeioTransporte(idMeioTransporteSemiRebocado);

        BigDecimal pcOcupacaoInformado = null;
        if (ociosidadeVisual != null) {
            pcOcupacaoInformado = BigDecimalUtils.HUNDRED.subtract(ociosidadeVisual);
        }

        controleCarga = findById(controleCarga.getIdControleCarga());
        controleCarga.setMeioTransporteByIdTransportado(meioTransporteTransportado);
        controleCarga.setMotorista(motorista);
        controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporteSemiRebocado);
        controleCarga.setProprietario(proprietario);
        controleCarga.setPcOcupacaoInformado(pcOcupacaoInformado);
        List<TabelaColetaEntregaCC> tabelasColetasCC = storeTabelasColetaEntrega(controleCarga, idMeioTransporteTransportado, controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega(), tabelaColetaEntrega, tipoTabelaColetaEntrega);

        validateTabelasColetaEntrega(tabelasColetasCC, controleCarga, tabelaColetaEntrega, idMeioTransporteTransportado, controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());

        store(controleCarga);

        if (!isCalculoPadrao()) {
            tabelaColetaEntregaCCService.storeAll(tabelasColetasCC);
        }

        if (veiculoInformadoManualmente && proprietario != null) {
            Long idVeiculoControleCarga = storeVeiculoControleCarga(controleCarga, false, meioTransporteTransportado, null);
            storePagtoProprietarioControleCarga(controleCarga, proprietario, getVeiculoControleCarga(idVeiculoControleCarga));
        }

        if (semiReboqueInformadoManualmente && meioTransporteSemiRebocado != null) {
            storeSemiReboqueControleCarga(controleCarga, false, meioTransporteSemiRebocado);
        }

        if (motoristaInformadoManualmente && motorista != null) {
            MotoristaControleCarga motoristaControleCarga = new MotoristaControleCarga();
            motoristaControleCarga.setControleCarga(controleCarga);
            motoristaControleCarga.setMotorista(motorista);

            LiberacaoReguladora liberacaoReguladora = liberacaoReguladoraService.findLiberacaoReguladoraMotorista(motorista.getIdMotorista(), controleCarga.getTpControleCarga().getValue());
            motoristaControleCarga.setLiberacaoReguladora(liberacaoReguladora);
            motoristaControleCargaService.store(motoristaControleCarga);
        }

        generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga, Boolean.TRUE, SessionUtils.getFilialSessao());
    }

    /**
     * @param idMeioTransporte
     * @return
     */
    private MeioTransporte getMeioTransporte(Long idMeioTransporte) {
        if (idMeioTransporte != null) {
            return meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false);
        }
        return null;
    }

    /**
     * @param idProprietario
     * @return
     */
    private Proprietario getProprietario(Long idProprietario) {
        if (idProprietario != null) {
            return proprietarioService.findById(idProprietario);
        }
        return null;
    }

    /**
     * @param idMotorista
     * @return
     */
    private Motorista getMotorista(Long idMotorista) {
        if (idMotorista != null) {
            return motoristaService.findById(idMotorista);
        }
        return null;
    }

    /**
     * @param idTipoTabelaColetaEntrega
     * @return
     */
    private TipoTabelaColetaEntrega getTipoTabelaColetaEntrega(Long idTipoTabelaColetaEntrega) {
        if (idTipoTabelaColetaEntrega != null) {
            return tipoTabelaColetaEntregaService.findById(idTipoTabelaColetaEntrega);
        }
        return null;
    }

    /**
     * @param idTabelaColetaEntrega
     * @return
     */
    private TabelaColetaEntrega getTabelaColetaEntrega(Long idTabelaColetaEntrega) {
        if (!isCalculoPadrao() && idTabelaColetaEntrega != null) {
            return tabelaColetaEntregaService.findById(idTabelaColetaEntrega);
        }
        return null;
    }

    /**
     * @param idSolicitacaoContratacao
     * @return
     */
    private SolicitacaoContratacao getSolicitacaoContratacao(Long idSolicitacaoContratacao) {
        if (idSolicitacaoContratacao != null) {
            return solicitacaoContratacaoService.findById(idSolicitacaoContratacao);
        }
        return null;
    }

    /**
     * @param idRotaColetaEntrega
     * @return
     */
    private RotaColetaEntrega getRotaColetaEntrega(Long idRotaColetaEntrega) {
        if (idRotaColetaEntrega != null) {
            return rotaColetaEntregaService.findById(idRotaColetaEntrega);
        }
        return null;
    }

    /**
     * @param idRotaIdaVolta
     * @return
     */
    private RotaIdaVolta getRotaIdaVolta(Long idRotaIdaVolta) {
        if (idRotaIdaVolta != null) {
            return rotaIdaVoltaService.findById(idRotaIdaVolta);
        }
        return null;
    }

    /**
     * @param idVeiculoControleCarga
     * @return
     */
    private VeiculoControleCarga getVeiculoControleCarga(Long idVeiculoControleCarga) {
        if (idVeiculoControleCarga != null) {
            return veiculoControleCargaService.findById(idVeiculoControleCarga);
        }
        return null;
    }

    /**
     * Rotina: 06.01.01.03 - Manter Tabela de Fretes agregados
     *
     * @param idFilial
     * @param idMeioTransporte
     * @param nrQuilometragem
     * @param blVirouHodomotreto
     * @param idControleCarga
     */
    private void storeQuilometragemMeioTransporte(Long idFilial, Long idMeioTransporte, Integer nrQuilometragem,
                                                  Boolean blVirouHodomotreto, Long idControleCarga, String obControleQuilometragem) {
        controleQuilometragemService.storeInformarQuilometragemMeioTransporte(idFilial, Boolean.FALSE,
                idMeioTransporte, Boolean.TRUE, nrQuilometragem, blVirouHodomotreto, idControleCarga, null, obControleQuilometragem);
    }

    /**
     * @param controleCarga
     * @param isNewControleCarga
     * @param meioTransporte
     * @param solicitacaoContratacao
     * @return
     */
    private Long storeVeiculoControleCarga(ControleCarga controleCarga, boolean isNewControleCarga, MeioTransporte meioTransporte, SolicitacaoContratacao solicitacaoContratacao) {
        ControleCarga lastControleCarga = new ControleCarga();

        //não validar quando for integração
        if (!SessionUtils.isIntegrationRunning()) {
            meioTransporteRodoviarioService.validateEstadoMeioTransporte(meioTransporte.getIdMeioTransporte());
        }
        meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildMeioTranspProprietarioFromMeioTransporte(meioTransporte));

        if (bloqueioMotoristaPropService.executeBloqueioEventual(controleCarga, isNewControleCarga, meioTransporte.getIdMeioTransporte(), lastControleCarga)) {
            Object[] args = new Object[]{lastControleCarga.getIdControleCarga(), meioTransporte.getIdMeioTransporte(), isNewControleCarga};

            throw new BusinessException(LMS_26044, args);
        }

        meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildMeioTranspProprietarioFromMeioTransporte(meioTransporte));

        VeiculoControleCarga veiculoControleCarga = new VeiculoControleCarga();
        veiculoControleCarga.setControleCarga(controleCarga);
        veiculoControleCarga.setMeioTransporte(meioTransporte);
        veiculoControleCarga.setSolicitacaoContratacao(solicitacaoContratacao);
        return (Long) veiculoControleCargaService.store(veiculoControleCarga);
    }

    public void storeBloqueioViagemEventual(Long idControleCarga, Long idMeioTransporte, boolean isNewControleCarga) {
        bloqueioMotoristaPropService.storeBloqueioViagemEventual(idControleCarga, idMeioTransporte, isNewControleCarga);
    }

    /**
     * @param controleCarga
     * @param proprietario
     * @param veiculoControleCarga
     */
    private void storePagtoProprietarioControleCarga(ControleCarga controleCarga, Proprietario proprietario, VeiculoControleCarga veiculoControleCarga) {
        PagtoProprietarioCc pagtoProprietarioCc = new PagtoProprietarioCc();
        pagtoProprietarioCc.setControleCarga(controleCarga);
        pagtoProprietarioCc.setProprietario(proprietario);
        pagtoProprietarioCc.setVeiculoControleCarga(veiculoControleCarga);
        if (controleCarga.getVlFreteCarreteiro() != null) {
            pagtoProprietarioCc.setMoeda(controleCarga.getMoeda());
            pagtoProprietarioCc.setVlPagamento(controleCarga.getVlFreteCarreteiro());
        }
        pagtoProprietarioCcService.store(pagtoProprietarioCc);
    }

    /**
     * @param controleCarga
     * @param meioTransporte
     */
    private void storeSemiReboqueControleCarga(ControleCarga controleCarga, boolean isNewControleCarga, MeioTransporte meioTransporte) {
        ControleCarga lastControleCarga = new ControleCarga();

        meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildMeioTranspProprietarioFromMeioTransporte(meioTransporte));

        if (bloqueioMotoristaPropService.executeBloqueioEventual(controleCarga, isNewControleCarga, meioTransporte.getIdMeioTransporte(), lastControleCarga)) {
            Object[] args = new Object[]{lastControleCarga.getIdControleCarga(), meioTransporte.getIdMeioTransporte(), isNewControleCarga};

            throw new BusinessException(LMS_26044, args);
        }

        meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildMeioTranspProprietarioFromMeioTransporte(meioTransporte));

        SemiReboqueCc semiReboqueCc = new SemiReboqueCc();
        semiReboqueCc.setControleCarga(controleCarga);
        semiReboqueCc.setMeioTransporte(meioTransporte);
        semiReboqueCcService.store(semiReboqueCc);
    }

    /**
     * Busca o último controle de carga gerado
     * filtrando pelo meio de transporte
     *
     * @param idMeioTransporte
     * @param idControleCargaAtual
     * @return
     */
    public ControleCarga findLastByMeioTransporte(Long idMeioTransporte, Long idControleCargaAtual) {
        return this.getControleCargaDAO().findLastByMeioTransporte(idMeioTransporte, idControleCargaAtual);
    }

    /**
     * @param controleCarga
     * @param motorista
     */
    private void storeMotoristaControleCarga(ControleCarga controleCarga, Motorista motorista) {
        MotoristaControleCarga motoristaControleCarga = new MotoristaControleCarga();
        motoristaControleCarga.setControleCarga(controleCarga);
        motoristaControleCarga.setMotorista(motorista);
        if (!SessionUtils.isIntegrationRunning()) {
            LiberacaoReguladora liberacaoReguladora = liberacaoReguladoraService.findLiberacaoReguladoraMotorista(motorista.getIdMotorista(), controleCarga.getTpControleCarga().getValue());
            motoristaControleCarga.setLiberacaoReguladora(liberacaoReguladora);

            if ("V".equals(controleCarga.getTpControleCarga().getValue()) && "E".equals(motorista.getTpVinculo().getValue())) {
                liberacaoReguladora.setDtVencimento(JTDateTimeUtils.getDataAtual());
                liberacaoReguladoraService.store(liberacaoReguladora);
            }
        }
        motoristaControleCargaService.store(motoristaControleCarga);
    }

    /**
     * Método que calcula o valor total das mercadorias de um Controle de Carga
     *
     * @param idControleCarga
     */
    public BigDecimal generateCalculaValorTotalMercadoriaControleCarga(Long idControleCarga) {
        return getControleCargaDAO().generateCalculaValorTotalMercadoriaControleCarga(idControleCarga);
    }


    /**
     * Método q calcula o valor total das mercadorias de um Controle de Carga, podendo filtrar apenas documentos
     * de determinado modal, de determinada abrangência e que tenham ou não seguro do cliente.
     *
     * @param idControleCarga
     * @param blSeguroMercurio
     * @param tpModal
     * @param tpAbrangencia
     * @return
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal generateCalculaValorTotalMercadoriaControleCarga(Long idControleCarga, Boolean blSeguroMercurio,
                                                                       String tpModal, String tpAbrangencia,
                                                                       Boolean blIncluirPreManifesto) {
        BigDecimal valorTotal = BigDecimalUtils.ZERO;

        List listValores = this.getControleCargaDAO().generateCalculaValorTotalMercadoriaControleCarga(
                idControleCarga, blSeguroMercurio, tpModal, tpAbrangencia, blIncluirPreManifesto);

        valorTotal = iterateValoresGenCalcValorTotalMercadoriaControleCarga(valorTotal, listValores);
        return valorTotal;

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private BigDecimal iterateValoresGenCalcValorTotalMercadoriaControleCarga(BigDecimal valorTotal, List listValores) {

        BigDecimal valorTotalAux = valorTotal;

        for (Iterator iter = listValores.iterator(); iter.hasNext(); ) {
            Object[] object = (Object[]) iter.next();
            Map map = new HashMap();
            map.put(TOTAL_VL_MERCADORIA, object[ARR_POS_0]);

            if (object[ARR_POS_1] != null) {
                map.put(ID_PAIS, object[ARR_POS_1]);
            } else {
                map.put(ID_PAIS, SessionUtils.getPaisSessao().getIdPais());
            }

            map.put(ID_MOEDA, object[ARR_POS_2]);

            if (map.get(TOTAL_VL_MERCADORIA) != null) {
                valorTotalAux = valorTotalAux.add(conversaoMoedaService
                        .findConversaoMoeda((Long) map.get(ID_PAIS),
                                (Long) map.get(ID_MOEDA),
                                SessionUtils.getPaisSessao().getIdPais(),
                                SessionUtils.getMoedaSessao().getIdMoeda(),
                                JTDateTimeUtils.getDataAtual(),
                                (BigDecimal) map.get(TOTAL_VL_MERCADORIA)));


            }
        }
        return valorTotalAux;
    }

    /**
     * Método q calcula o valor total das mercadorias de um Controle de Carga, podendo filtrar apenas documentos
     * de determinado modal, de determinada abrangência e que tenham ou não seguro do cliente. Busca apenas
     * valores vinculados ao pre manifesto.
     *
     * @param idControleCarga
     * @param blSeguroMercurio
     * @return
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal generateCalculaValorTotalMercadoriaControleCargaPreManifesto(Long idControleCarga, Boolean blSeguroMercurio) {
        BigDecimal valorTotal = BigDecimalUtils.ZERO;

        List listValores = this.getControleCargaDAO()
                .generateCalculaValorTotalMercadoriaControleCargaPreManifesto(idControleCarga, blSeguroMercurio);

        valorTotal = iterateValoresGenCalcValorTotalMercadoriaControleCarga(valorTotal, listValores);

        return valorTotal;
    }

    /**
     * @param listEnquadramento
     * @param controleCarga
     * @return
     */
    @SuppressWarnings({ "unused", "rawtypes" })
    private Boolean verificaExisteLiberacaoCemop(List listEnquadramento, ControleCarga controleCarga) {
        Boolean hasPrecisaLiberacao = false;

        if (listEnquadramento.isEmpty()) {
            throw new BusinessException("LMS-11320");
        }

        for (Object object : listEnquadramento) {
            TypedFlatMap map = (TypedFlatMap) object;
            hasPrecisaLiberacao = map.getBoolean("blRequerLiberacaoCemop");
        }

        if (hasPrecisaLiberacao) {
            EventoWorkflow evento = eventoWorkflowService.findByTipoEvento(NR_EVENTO_LIBERAR_EMISSAO_CC);
            List<Pendencia> pendencias = pendenciaService.findPendenciaByEvento(
                    controleCarga.getIdControleCarga(), evento.getIdEventoWorkflow(), new String[]{"E", "A", "R"});

            if (pendencias.isEmpty()) {
                this.generatePendencia(controleCarga);
                return Boolean.FALSE;
            }

            for (Pendencia pendencia : pendencias) {
                String tpSituacaoPendencia = pendencia.getTpSituacaoPendencia().getValue();

                if ("E".equals(tpSituacaoPendencia)) {
                    throw new BusinessException("LMS-05332");
                }

                if ("R".equals(tpSituacaoPendencia)) {
                    this.generatePendencia(controleCarga);
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }


    /**
     * Método que gera uma pendência (workflow) para enviar avisos à DIVOP e CEMOP.
     *
     * @return
     */
    private Pendencia generatePendencia(ControleCarga controleCarga) {
        String strControleCarga = FormatUtils.formatSgFilialWithLong(
                controleCarga.getFilialByIdFilialOrigem().getSgFilial(), controleCarga.getNrControleCarga());

        return workflowPendenciaService.generatePendencia(
                SessionUtils.getFilialSessao().getIdFilial(),
                NR_EVENTO_LIBERAR_EMISSAO_CC,
                controleCarga.getIdControleCarga(),
                "Liberação para Emissão do Controle de Cargas de Viagem " + strControleCarga,
                JTDateTimeUtils.getDataHoraAtual());
    }


    /**
     * Executa o Workflow gerado a partir da pendencia gerada de Controle de Carga
     */
    @SuppressWarnings("rawtypes")
    public void executeWorkflow(List idsProcesso, List tpsSituacao) {
        // Regra de negócio após chamar o Workflow.
        Long idProcesso = (Long) idsProcesso.get(0);
        this.generateEventoControleCargaForLiberarEmissaoControleCarga(idProcesso, null);
    }

    /**
     * Método que gera um Evento de Controle de Carga após a aprovação do Workflow de
     * 'Liberação para Emissão do Controle de Carga'.
     *
     * @param idControleCarga
     */
    public void generateEventoControleCargaForLiberarEmissaoControleCarga(Long idControleCarga, String dsEvento) {
        ControleCarga controleCarga = this.findByIdInitLazyProperties(idControleCarga, false);

        // Gera um Evento de Controle de Carga
        EventoControleCarga eventoControleCarga = new EventoControleCarga();
        eventoControleCarga.setFilial(SessionUtils.getFilialSessao());
        eventoControleCarga.setControleCarga(controleCarga);
        eventoControleCarga.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
        eventoControleCarga.setTpEventoControleCarga(new DomainValue("LR"));
        eventoControleCarga.setUsuario(SessionUtils.getUsuarioLogado());
        eventoControleCarga.setEquipeOperacao(null);
        eventoControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
        eventoControleCarga.setDsEvento(dsEvento);
        eventoControleCarga.setMoeda(SessionUtils.getMoedaSessao());
        eventoControleCarga.setVlTotal(generateCalculaValorTotalMercadoriaControleCarga(controleCarga.getIdControleCarga(), null, null, null, null));
        eventoControleCarga.setVlLiberacao(eventoControleCarga.getVlTotal());
        eventoControleCarga.setPsReal(controleCarga.getPsTotalFrota());
        eventoControleCarga.setPsAforado(controleCarga.getPsTotalAforado());
        eventoControleCarga.setPcOcupacaoCalculado(controleCarga.getPcOcupacaoCalculado());
        eventoControleCarga.setPcOcupacaoAforadoCalculado(controleCarga.getPcOcupacaoAforadoCalculado());
        eventoControleCarga.setPcOcupacaoInformado(controleCarga.getPcOcupacaoInformado());
        eventoControleCargaService.store(eventoControleCarga);
    }

    /**
     * Método que retorna o Peso Total do Manifesto usando como filtro o ID do Controle de Carga
     * e o tipo de Status do Manifesto como EF (Em escala na Filial) ou CC (Carregamento Concluido) ou ME (Manifesto Emitido)
     * ou EV (Em viagem)
     *
     * @param idControleCarga
     * @return
     */
    public BigDecimal findPsTotalFromManifestoByIdControleCarga(Long idControleCarga) {
        List<Manifesto> listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        BigDecimal psTotal = BigDecimalUtils.ZERO;
        for (Manifesto manifesto : listManifestos) {
            String tpStatusManifesto = manifesto.getTpStatusManifesto().getValue();
            if ("EF".equals(tpStatusManifesto) || "CC".equals(tpStatusManifesto) || "ME".equals(tpStatusManifesto) || "EV".equals(tpStatusManifesto)) {
                psTotal = psTotal.add(manifesto.getPsTotalManifesto());
            }
        }
        return psTotal;
    }

    /**
     * Método que retorna o Peso Total Aforado do Manifesto usando como filtro o ID do Controle de Carga
     * e o tipo de Status do Manifesto como EF (Em escala na Filial) ou CC (Carregamento Concluido) ou ME (Manifesto Emitido)
     * ou EV (Em viagem)
     *
     * @param idControleCarga
     * @return
     */
    @SuppressWarnings("rawtypes")
    public BigDecimal findPsTotalAforadoFromManifestoByIdControleCarga(Long idControleCarga) {
        List listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        BigDecimal psTotalAforado = BigDecimalUtils.ZERO;
        for (Iterator iter = listManifestos.iterator(); iter.hasNext(); ) {
            Manifesto manifesto = (Manifesto) iter.next();
            if ("EF".equals(manifesto.getTpStatusManifesto().getValue()) || "CC".equals(manifesto.getTpStatusManifesto().getValue()) ||
                    "ME".equals(manifesto.getTpStatusManifesto().getValue()) || "EV".equals(manifesto.getTpStatusManifesto().getValue())) {
                psTotalAforado = psTotalAforado.add(manifesto.getPsTotalAforadoManifesto());
            }
        }
        return psTotalAforado;
    }

    /**
     * Método que calcula o Percentual de Ocupação do Controle de Carga
     *
     * @param psTotal
     * @param nrCapacidadeKg
     * @return
     */
    public BigDecimal executeCalculoPcOcupacao(BigDecimal psTotal, BigDecimal nrCapacidadeKg) {
        BigDecimal result = psTotal.multiply(BigDecimalUtils.HUNDRED).divide(nrCapacidadeKg, BIG_DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
        return result.setScale(DIVIDE_SCALE, 0);
    }

    /**
     * Método que calcula o Percentual de Ocupação Aforado do Controle de Carga
     *
     * @param psTotalAforado
     * @param nrCapacidadeKg
     * @return
     */
    public BigDecimal executeCalculoPcOcupacaoAforado(BigDecimal psTotalAforado, BigDecimal nrCapacidadeKg) {
        BigDecimal result = psTotalAforado.multiply(BigDecimalUtils.HUNDRED).divide(nrCapacidadeKg, BigDecimal.ROUND_HALF_UP);
        return result.setScale(DIVIDE_SCALE, 0);
    }

    /**
     * @param idControleCarga
     * @return
     */
    public DomainValue findTipoControleCarga(Long idControleCarga) {
        return getControleCargaDAO().findTipoControleCarga(idControleCarga);
    }

    /**
     * Assinatura de método com parâmetro idFilialUsuario e dataHoraGeracao, necessários para a Integração
     * conforme solicitação CQPRO00005472
     *
     * @param controleCarga
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param idMotorista
     * @param idProprietario
     * @param idTipoTabelaColetaEntrega
     * @param idRotaColetaEntrega
     * @param idRotaIdaVolta
     * @param idRota
     * @param idSolicitacaoContratacao
     * @param tpControleCarga
     * @param tpRotaViagem
     * @param vlFreteCarreteiro
     */
    public void generateControleCarga(ControleCarga controleCarga,
                                      Long idMeioTransporteTransportado,
                                      Long idMeioTransporteSemiRebocado,
                                      Long idMotorista,
                                      Long idProprietario,
                                      Long idTipoTabelaColetaEntrega,
                                      Long idTabelaColetaEntrega,
                                      Long idRotaColetaEntrega,
                                      Long idRotaIdaVolta,
                                      Long idRota,
                                      Long idSolicitacaoContratacao,
                                      DomainValue tpControleCarga,
                                      DomainValue tpRotaViagem,
                                      BigDecimal vlFreteCarreteiro,
                                      DateTime dhPrevisaoSaida,
                                      DateTime dhGeracao,
                                      Integer nrTempoViagem,
                                      Boolean blEntregaDireta,
                                      Boolean isMWW,
                                      Long idFilial) {

        generateControleCarga(controleCarga,
                idMeioTransporteTransportado,
                idMeioTransporteSemiRebocado,
                idMotorista,
                null,
                idProprietario,
                idTipoTabelaColetaEntrega,
                idTabelaColetaEntrega,
                idRotaColetaEntrega,
                idRotaIdaVolta,
                idRota,
                idSolicitacaoContratacao,
                tpControleCarga,
                tpRotaViagem,
                vlFreteCarreteiro,
                dhPrevisaoSaida,
                dhGeracao,
                nrTempoViagem,
                blEntregaDireta,
                isMWW,
                idFilial);
    }

    /**
     * @param controleCarga
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param idMotorista
     * @param idInstrutorMotorista
     * @param idProprietario
     * @param idTipoTabelaColetaEntrega
     * @param idTabelaColetaEntrega
     * @param idRotaColetaEntrega
     * @param idRotaIdaVolta
     * @param idRota
     * @param idSolicitacaoContratacao
     * @param tpControleCarga
     * @param tpRotaViagem
     * @param vlFreteCarreteiro
     * @param dhPrevisaoSaida
     * @param dhGeracao
     * @param nrTempoViagem
     * @param blEntregaDireta
     * @param isMWW
     */
    public void generateControleCarga(ControleCarga controleCarga,
                                      Long 
                                      idMeioTransporteTransportado,
                                      Long idMeioTransporteSemiRebocado,
                                      Long idMotorista,
                                      Long idInstrutorMotorista,
                                      Long idProprietario,
                                      Long idTipoTabelaColetaEntrega,
                                      Long idTabelaColetaEntrega,
                                      Long idRotaColetaEntrega,
                                      Long idRotaIdaVolta,
                                      Long idRota,
                                      Long idSolicitacaoContratacao,
                                      DomainValue tpControleCarga,
                                      DomainValue tpRotaViagem,
                                      BigDecimal vlFreteCarreteiro,
                                      DateTime dhPrevisaoSaida,
                                      DateTime dhGeracao,
                                      Integer nrTempoViagem,
                                      Boolean blEntregaDireta,
                                      Boolean isMWW,
                                      Long idFilial) {

        Filial filialUsuario = new Filial();
        if (idFilial != null) {
            filialUsuario.setIdFilial(idFilial);
        } else {
            filialUsuario = SessionUtils.getFilialSessao();
        }


        if (!SessionUtils.isIntegrationRunning()) {
            if (idMeioTransporteTransportado != null) {
                validateVeiculoControleCarga(idMeioTransporteTransportado, isMWW);
            }

            if (idMeioTransporteSemiRebocado != null) {
                validateSemiReboqueControleCarga(idMeioTransporteSemiRebocado);
            }
        }
        MeioTransporte meioTransporteTransportado = getMeioTransporte(idMeioTransporteTransportado);
        TabelaColetaEntrega tabelaColetaEntrega = getTabelaColetaEntrega(idTabelaColetaEntrega);
        TipoTabelaColetaEntrega tipoTabelaColetaEntrega = getTipoTabelaColetaEntrega(idTipoTabelaColetaEntrega);
        Proprietario proprietario = getProprietario(idProprietario);
        Motorista motorista = getMotorista(idMotorista);
        Motorista instrutorMotorista = getMotorista(idInstrutorMotorista);
        MeioTransporte meioTransporteSemiRebocado = getMeioTransporte(idMeioTransporteSemiRebocado);
        RotaColetaEntrega rotaColetaEntrega = getRotaColetaEntrega(idRotaColetaEntrega);
        RotaIdaVolta rotaIdaVolta = getRotaIdaVolta(idRotaIdaVolta);
        SolicitacaoContratacao solicitacaoContratacao = getSolicitacaoContratacao(idSolicitacaoContratacao);

        validaRotaVigente(tpControleCarga, rotaColetaEntrega);

        Filial filialOrigem = filialUsuario;
        Filial filialDestino = filialUsuario;
        Rota rota = null;
        if ("V".equals(tpControleCarga.getValue())) {
            if ("EX".equals(tpRotaViagem.getValue())) {
                filialOrigem = filialService.findFilialOrigemDestinoToControleCarga(idRotaIdaVolta, idRota, null, Boolean.TRUE, null);
                filialDestino = filialService.findFilialOrigemDestinoToControleCarga(idRotaIdaVolta, idRota, null, null, Boolean.TRUE);
                rota = rotaService.findById(idRota);
            } else if ("EC".equals(tpRotaViagem.getValue())) {
                filialOrigem = filialService.findFilialOrigemDestinoToControleCarga(idRotaIdaVolta, idRota, null, Boolean.TRUE, null);
                filialDestino = filialService.findFilialOrigemDestinoToControleCarga(idRotaIdaVolta, idRota, null, null, Boolean.TRUE);
                rota = rotaService.findById(idRota);
            } else if ("EV".equals(tpRotaViagem.getValue())) {
                filialOrigem = filialService.findFilialOrigemDestinoToControleCarga(null, idRota, idSolicitacaoContratacao, Boolean.TRUE, null);
                filialDestino = filialService.findFilialOrigemDestinoToControleCarga(null, idRota, idSolicitacaoContratacao, null, Boolean.TRUE);
                if (solicitacaoContratacao != null) {
                    rota = solicitacaoContratacao.getRota();
                }
            }

        }

        controleCarga.setIdControleCarga(null);
        controleCarga.setBlEntregaDireta(blEntregaDireta);
        controleCarga.setFilialByIdFilialOrigem(filialOrigem);
        controleCarga.setFilialByIdFilialAtualizaStatus(filialOrigem);
        controleCarga.setMotorista(motorista);
        controleCarga.setInstrutorMotorista(instrutorMotorista);
        controleCarga.setProprietario(proprietario);
        controleCarga.setTpControleCarga(tpControleCarga);
        controleCarga.setTpStatusControleCarga(new DomainValue("GE"));
        controleCarga.setTpRotaViagem(tpRotaViagem);
        controleCarga.setDhGeracao(dhGeracao);
        controleCarga.setDhPrevisaoSaida(dhPrevisaoSaida);
        controleCarga.setNrTempoViagem(nrTempoViagem);

        // será atualizado depois de salvar
        controleCarga.setVlPedagio(null);
        controleCarga.setFilialByIdFilialDestino(filialDestino);
        controleCarga.setSolicitacaoContratacao(solicitacaoContratacao);
        controleCarga.setMeioTransporteByIdTransportado(meioTransporteTransportado);
        controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporteSemiRebocado);
        controleCarga.setRota(rota);
        controleCarga.setRotaColetaEntrega(rotaColetaEntrega);
        controleCarga.setRotaIdaVolta(rotaIdaVolta);
        controleCarga.setMotivoCancelamentoCc(null);
        controleCarga.setMoeda(SessionUtils.getMoedaSessao());
        controleCarga.setVlFreteCarreteiro(vlFreteCarreteiro);
        controleCarga.setDsSenhaCelularVeiculo(null);
        controleCarga.setDhGeracaoSeguro(null);
        controleCarga.setDhSaidaColetaEntrega(null);
        controleCarga.setDhChegadaColetaEntrega(null);
        controleCarga.setPcOcupacaoCalculado(BigDecimalUtils.ZERO);
        controleCarga.setPcOcupacaoAforadoCalculado(BigDecimalUtils.ZERO);
        controleCarga.setPcOcupacaoInformado(BigDecimalUtils.ZERO);
        controleCarga.setPsTotalFrota(null);
        controleCarga.setPsTotalAforado(null);
        controleCarga.setPsColetado(null);
        controleCarga.setPsAColetar(null);
        controleCarga.setPsEntregue(null);
        controleCarga.setPsAEntregar(null);
        controleCarga.setVlTotalFrota(null);
        controleCarga.setVlColetado(null);

        boolean calculoPadrao = isCalculoPadrao();

        List<TabelaColetaEntregaCC> tabelasColetasCC = null;

        if (!calculoPadrao) {
            tabelasColetasCC = storeTabelasColetaEntrega(controleCarga, idMeioTransporteTransportado, idRotaColetaEntrega, tabelaColetaEntrega, tipoTabelaColetaEntrega);

            validateTabelasColetaEntrega(tabelasColetasCC, controleCarga, tabelaColetaEntrega, idMeioTransporteTransportado, idRotaColetaEntrega);
        }

        if (!SessionUtils.isIntegrationRunning()) {
            Long nrControleCarga = configuracoesFacade.incrementaParametroSequencial(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "NR_CONTROLE_CARGA", true);
            controleCarga.setNrControleCarga(nrControleCarga);
        } else {
            controleCarga.setNrControleCarga(controleCarga.getNrControleCarga());
        }
        controleCarga.setIdControleCarga((Long) getControleCargaDAO().storeWithFlush(controleCarga));

        if (motorista != null) {
            storeMotoristaControleCarga(controleCarga, motorista);
        }

        validaPISProprietario(proprietario);

        if (meioTransporteTransportado != null && proprietario != null) {
            Long idVeiculoControleCarga = storeVeiculoControleCarga(controleCarga, true, meioTransporteTransportado, solicitacaoContratacao);
            storePagtoProprietarioControleCarga(controleCarga, proprietario, getVeiculoControleCarga(idVeiculoControleCarga));
        }

        if (meioTransporteSemiRebocado != null) {
            storeSemiReboqueControleCarga(controleCarga, true, meioTransporteSemiRebocado);
        }
        getControleCargaDAO().getAdsmHibernateTemplate().flush();
        generateEventoChangeStatusControleCarga(controleCarga.getIdControleCarga(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "GE", dhGeracao);

        if (!calculoPadrao) {
            tabelaColetaEntregaCCService.storeAll(tabelasColetasCC);
        }
    }

    /**
     * @return
     */
    private boolean isCalculoPadrao() {
        boolean calculoPadrao = false;
        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
        if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
            calculoPadrao = true;
        }
        return calculoPadrao;
    }

    protected void validaRotaVigente(DomainValue tpControleCarga, RotaColetaEntrega rotaColetaEntrega) {
        if (isControleDeCarga(tpControleCarga) && rotaTemDataFinalMenorQueAtual(rotaColetaEntrega)) {
            throw new BusinessException("LMS-05407");
        }
    }

    protected boolean rotaTemDataFinalMenorQueAtual(RotaColetaEntrega rotaColetaEntrega) {
        return isRotaColetaEntregaPreenchida(rotaColetaEntrega) && rotaColetaEntrega.getDtVigenciaFinal().compareTo(JTDateTimeUtils.getDataAtual()) < 0;
    }

    protected boolean isRotaColetaEntregaPreenchida(
            RotaColetaEntrega rotaColetaEntrega) {
        return rotaColetaEntrega != null && rotaColetaEntrega.getDtVigenciaFinal() != null;
    }

    protected boolean isControleDeCarga(DomainValue tpControleCarga) {
        return tpControleCarga != null && tpControleCarga.getValue() != null && tpControleCarga.getValue().equals(COLETA);
    }

    /**
     * Verifica se o Proprietário do Veiculo possui um PIS cadastrado.
     */
    private void validaPISProprietario(Proprietario proprietario) {

        if (proprietario == null || proprietario.getIdProprietario() == null || proprietarioService.hasPIS(proprietario.getIdProprietario())) {
            return;
        }
        // Não é possível gerar controle de Carga. O Proprietário do veículo não possui número de PIS cadastrado.
        throw new BusinessException("LMS-05405");
    }

    /**
     * Método responsável por identificar se a tabela coleta entrega do objeto é o mesmo que o selecionado na tela
     */
    private void validateTabelasColetaEntrega(List<TabelaColetaEntregaCC> tabelasColetasCC, ControleCarga controleCarga,
                                              TabelaColetaEntrega tabColEntSelecionada, Long idMeioTransporteTransportado, Long idRotaColetaEntrega) {

        if (isCalculoPadrao()) {
            return;
        }

        if (COLETA.equals(controleCarga.getTpControleCarga().getValue()) && idMeioTransporteTransportado != null
                && tabColEntSelecionada != null && tabColEntSelecionada.getTpCalculo() != null) {

            String tpCalculoRetornado = "";
            String tpCalculoSelecionado = tabColEntSelecionada.getTpCalculo().getValue();

            List<TypedFlatMap> ltfm = tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(
                    controleCarga.getFilialByIdFilialOrigem().getIdFilial(), idMeioTransporteTransportado, idRotaColetaEntrega);

            if (ltfm != null && !ltfm.isEmpty()) {
                tpCalculoRetornado = (String) ltfm.get(0).get("tpCalculo");
            }

            if (tabelasColetasCC != null && !tabelasColetasCC.isEmpty() && tabelasColetasCC.get(0).getTabelaColetaEntrega() != null) {
                Long idTabelaColetaEntregaCons = tabelasColetasCC.get(0).getTabelaColetaEntrega().getIdTabelaColetaEntrega();

                //Realizado a consulta para garantir o valor foi retornado do banco, pois o objeto tabelasColetasCC está na memória
                TabelaColetaEntrega tabelaColetaEntregaCons = tabelaColetaEntregaService.findById(idTabelaColetaEntregaCons);

                if (tabelaColetaEntregaCons != null && tabelaColetaEntregaCons.getTpCalculo() != null) {
                    tpCalculoRetornado = tabelaColetaEntregaCons.getTpCalculo().getValue();
                }
            }

            if (!tpCalculoSelecionado.equals(tpCalculoRetornado)) {
                throw new BusinessException("LMS-05385");
            }
        }
    }

    private List<TabelaColetaEntregaCC> storeTabelasColetaEntrega(
            ControleCarga controleCarga, Long idMeioTransporteTransportado,
            Long idRotaColetaEntrega, TabelaColetaEntrega tabelaColetaEntrega,
            TipoTabelaColetaEntrega tipoTabelaColetaEntrega) {

        if (isCalculoPadrao()) {
            return null;
        }

        List<TabelaColetaEntregaCC> tabelasColetasCC = new ArrayList<TabelaColetaEntregaCC>();
        if (controleCarga.getTpControleCarga().getValue().equals(COLETA) && controleCarga.getMeioTransporteByIdTransportado() != null) {
            if (controleCarga.getIdControleCarga() != null) {
                tabelasColetasCC = tabelaColetaEntregaCCService.findByIdContoleCarga(controleCarga.getIdControleCarga());
            }
            //
            List<TypedFlatMap> ltfm = tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(
                    controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                    idMeioTransporteTransportado,
                    idRotaColetaEntrega);
            if ("C1".equals(ltfm.get(0).get("tpCalculo"))) {
                controleCarga.setTabelaColetaEntrega(tabelaColetaEntrega);
                controleCarga.setTipoTabelaColetaEntrega(tipoTabelaColetaEntrega);
            } else {
                iterateAndAddTabelasColetasCC(controleCarga, tabelasColetasCC, ltfm);
            }
        }
        return tabelasColetasCC;
    }

    private void iterateAndAddTabelasColetasCC(ControleCarga controleCarga, List<TabelaColetaEntregaCC> tabelasColetasCC, List<TypedFlatMap> ltfm) {
        for (TypedFlatMap typedFlatMap : ltfm) {
            Boolean continueNextIteration = false;
            // se a tabela já foi vinculada ao controle carga, nao vincula novamente
            for (TabelaColetaEntregaCC tabelaColetaEntregaCC : tabelasColetasCC) {
                if (tabelaColetaEntregaCC.getTabelaColetaEntrega().getIdTabelaColetaEntrega().equals(typedFlatMap.getLong("idTabelaColetaEntrega"))) {
                    continueNextIteration = true;
                    break;
                }
            }
            if (!continueNextIteration) {
                TabelaColetaEntregaCC tabelaColetaEntregaCC = new TabelaColetaEntregaCC();
                tabelaColetaEntregaCC.setControleCarga(controleCarga);

                TabelaColetaEntrega tce = new TabelaColetaEntrega();
                tce.setIdTabelaColetaEntrega(typedFlatMap.getLong("idTabelaColetaEntrega"));
                tabelaColetaEntregaCC.setTabelaColetaEntrega(tce);
                tabelasColetasCC.add(tabelaColetaEntregaCC);
            }
        }
    }


    /**
     * Gerar o registro de inicio de carregamento, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja <code>null</code> ou atualiza a entidade, caso contrário.
     *
     * @param criteria                   TypedFlatMap
     * @param controleCarga
     * @param itemListIntegranteEqOperac list contem filhos da entidade IntegranteEqOperac a ser armazenada
     * @param itemListPagtoPedagioCc     list contem filhos da entidade PagtoPedagioCc a ser armazenada
     * @param itemListPostoPassagemCc    list contem filhos da entidade PostoPassagemCc a ser armazenada
     * @return entidade que foi armazenada.
     */
    @SuppressWarnings("rawtypes")
    public synchronized java.io.Serializable storeGerarControleCarga(
            TypedFlatMap criteria,
            ControleCarga controleCarga,
            ItemList itemListIntegranteEqOperac, ItemListConfig itemListConfigIntegranteEqOperac,
            ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc,
            ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc,
            ItemList itemListTrechos, ItemListConfig itemListConfigTrechos, String tpBeneficiarioAdiantamento,
            ItemList itemListTrechoCorporativo, ItemListConfig itemListConfigTrechoCorporativo,
            ItemList itemListAdiantamentoTrecho, ItemListConfig itemListConfigAdiantamentoTrecho
            // LMSA-6520: LMSA-6534
            , boolean cargaCompartilhadaSegundoCarregamento, List<TypedFlatMap> manifestosFedexSegundoCarregamento
            ) {
        boolean rollbackMasterId = controleCarga.getIdControleCarga() != null && controleCarga.getIdControleCarga().intValue() == -1;
        try {
            Long idRota = criteria.getLong(ROTA_ID_ROTA);
            Long idRotaIdaVolta = criteria.getLong(ROTA_IDA_VOLTA_ID_ROTA_IDA_VOLTA);
            Long idRotaColetaEntrega = criteria.getLong(ROTA_COLETA_ENTREGA_ID_ROTA_COLETA_ENTREGA);
            Long idEquipe = criteria.getLong(ID_EQUIPE);
            Long idMeioTransporteTransportado = criteria.getLong(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE);
            Long idMeioTransporteSemiRebocado = criteria.getLong(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE);
            Long idMotorista = criteria.getLong(MOTORISTA_ID_MOTORISTA);
            Long idInstrutorMotorista = criteria.getLong(INSTRUTOR_MOTORISTA_ID_INSTRUTOR_MOTORISTA);
            Long idProprietario = criteria.getLong(PROPRIETARIO_ID_PROPRIETARIO);
            DomainValue tpControleCarga = criteria.getDomainValue(TP_CONTROLE_CARGA);

            generateControleCarga(controleCarga,
                    idMeioTransporteTransportado,
                    idMeioTransporteSemiRebocado,
                    idMotorista,
                    idInstrutorMotorista,
                    idProprietario,
                    criteria.getLong(TIPO_TABELA_COLETA_ENTREGA_ID_TIPO_TABELA_COLETA_ENTREGA),
                    criteria.getLong(TABELA_COLETA_ENTREGA_ID_TABELA_COLETA_ENTREGA),
                    idRotaColetaEntrega,
                    idRotaIdaVolta,
                    idRota,
                    criteria.getLong(SOLICITACAO_CONTRATACAO_ID_SOLICITACAO_CONTRATACAO),
                    tpControleCarga,
                    criteria.getDomainValue(TP_ROTA_VIAGEM),
                    criteria.getBigDecimal(VL_FRETE_CARRETEIRO),
                    criteria.getDateTime(DH_PREVISAO_SAIDA),
                    JTDateTimeUtils.getDataHoraAtual(),
                    criteria.getInteger(NR_TEMPO_VIAGEM),
                    criteria.getBoolean(BL_ENTREGA_DIRETA),
                    false,
                    null);

            if (itemListTrechoCorporativo != null) {
                for (Iterator iter = itemListTrechoCorporativo.iterator(controleCarga.getIdControleCarga(), itemListConfigTrechoCorporativo); iter.hasNext(); ) {
                    TrechoCorporativo trechoCorporativo = (TrechoCorporativo) iter.next();
                    trechoCorporativo.setControleCarga(controleCarga);
                }
                trechoCorporativoService.storeTrechoCorporativo(itemListTrechoCorporativo);
            }

            PostoConveniado postoConveniado = null;
            if ("PO".equals(tpBeneficiarioAdiantamento)) {
                postoConveniado = controleCarga.getFilialByIdFilialOrigem().getPostoConveniado();
            }

            if (itemListAdiantamentoTrecho != null) {
                for (Iterator iter = itemListAdiantamentoTrecho.iterator(controleCarga.getIdControleCarga(), itemListConfigAdiantamentoTrecho); iter.hasNext(); ) {
                    AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho) iter.next();
                    adiantamentoTrecho.setControleCarga(controleCarga);
                    adiantamentoTrecho.setPostoConveniado(postoConveniado);
                }
                adiantamentoTrechoService.storeAdiantamentoTrecho(itemListAdiantamentoTrecho);
            }

            storeEquipeOperacao(itemListIntegranteEqOperac, itemListConfigIntegranteEqOperac, controleCarga, null, idEquipe);

            if (idRota != null || idRotaIdaVolta != null) {
                // Salva os dados da Aba Trecho
                for (Iterator iter = itemListTrechos.iterator(controleCarga.getIdControleCarga(), itemListConfigTrechos); iter.hasNext(); ) {
                    ControleTrecho controleTrecho = (ControleTrecho) iter.next();
                    controleTrecho.setControleCarga(controleCarga);
                }
                controleTrechoService.storeControleTrecho(itemListTrechos);
                filialRotaCcService.generateFilialRotaCcByRotaOrRotaIdaVolta(controleCarga.getIdControleCarga(), idRota, idRotaIdaVolta);
            }

            if ((idRota != null || idRotaIdaVolta != null || idRotaColetaEntrega != null) && idMeioTransporteTransportado != null) {
                BigDecimal vlPedagio = storePagtoPedagioCcAndPostosPassagemCc(controleCarga,
                        itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);

                if (vlPedagio.compareTo(BigDecimalUtils.ZERO) != 0) {
                    controleCarga.setVlPedagio(vlPedagio);
                    store(controleCarga);
                }
            }

            // Salva os dados da aba Solicitação de Sinal
            String nmEmpresaAnterior = criteria.getString("solicitacaoSinal.nmEmpresaAnterior");
            if (!StringUtils.isBlank(nmEmpresaAnterior)) {
                solicitacaoSinalService.generateEnviarSolicitacaoSinal(SessionUtils.getFilialSessao().getIdFilial(),
                        controleCarga.getIdControleCarga(),
                        idMeioTransporteTransportado,
                        idMeioTransporteSemiRebocado,
                        idMotorista,
                        criteria.getString("solicitacaoSinal.nrTelefoneEmpresa"),
                        nmEmpresaAnterior,
                        criteria.getString("solicitacaoSinal.nmResponsavelEmpresa"),
                        criteria.getBoolean("solicitacaoSinal.blPertenceProjCaminhoneiro"),
                        null);
            }
            
            // LMSA-6520: LMSA-6534: gravar aqui EDI <-> Conhecimento Fedex
            if (cargaCompartilhadaSegundoCarregamento) {
                processarConhecimentosFedex(controleCarga, manifestosFedexSegundoCarregamento);
            }
            
        } catch (RuntimeException runEx) {
            this.rollbackMasterState(controleCarga, rollbackMasterId, runEx, true);
            if (rollbackMasterId) {
                controleCarga.setIdControleCarga(Long.valueOf(-1));
            }
            itemListIntegranteEqOperac.rollbackItemsState();
            itemListPagtoPedagioCc.rollbackItemsState();
            itemListPostoPassagemCc.rollbackItemsState();
            itemListTrechos.rollbackItemsState();
            if (itemListTrechoCorporativo != null) {
                itemListTrechoCorporativo.rollbackItemsState();
            }
            if (itemListAdiantamentoTrecho != null) {
                itemListAdiantamentoTrecho.rollbackItemsState();
            }
            if (runEx.getMessage().contains(LMS_26044)) {
                throw (BusinessException) runEx;
            }
            throw runEx;
        }
        return controleCarga;
    }

    /**
     * o processamento de conhecimentos Fedex ira gravar novas/atualizar os conhecimentos fedex e, quando existir,
     * excluir os manifestos EDI
     * @param controleCarga
     * @param manifestosFedex
     * LMSA-6520: LMSA-6534
     */
    @SuppressWarnings("unchecked")
	private void processarConhecimentosFedex(ControleCarga controleCarga, List<TypedFlatMap> manifestosFedex) {
        for (TypedFlatMap manifestoFedex : manifestosFedex) {
        	
        	String chave = manifestoFedex.getString("numeroChave");
        	List<Integer> notasEDI = manifestoFedex.getList("notasEDI");
        	List<String> conhecimentosFedex = manifestoFedex.getList("conhecimentosFedex");
        	
            // cenario: sem notas EDI, apenas conhecimentos Fedex
            if (notasEDI == null || notasEDI.isEmpty()) {
            	atualizarConhecimentoFedexComControleCarga(chave, controleCarga, conhecimentosFedex);
            } else {
                // cenario: a partir das notas EDI, inserir ou atualizar conhecimentos fedex
            	adicionarAtualizarConhecimentosFedexApartirNotasEdi(chave, controleCarga, notasEDI);
            	removerNotasFiscaisEdi(chave, notasEDI);
            }
        }
    }
    
    private void removerNotasFiscaisEdi(String manifesto, List<Integer> notasEDI) {
    	if (manifesto != null && notasEDI != null) {
    		for (Integer numeroNota : notasEDI) {
    			notaFiscalExpedicaoEDIService.removerNotaFiscalEdiProcessadaByChaveMdfeFedexENumeroNotaFiscal(manifesto, numeroNota);
    		}
    	}
    }
    
    private void atualizarConhecimentoFedexComControleCarga(String chaveMdfe, ControleCarga controleCarga, List<String> conhecimentosFedex) {
    	List<ConhecimentoFedex> update = new ArrayList<ConhecimentoFedex>(conhecimentosFedex.size());
        for (String numeroConhecimento : conhecimentosFedex) {
            ConhecimentoFedex conhecimentoFedex = conhecimentoFedexService.findByChaveMdfeAndConhecimentoFedex(
                    chaveMdfe, numeroConhecimento);
            conhecimentoFedex.setControleCarga(controleCarga);
            update.add(conhecimentoFedex);
        }
        conhecimentoFedexService.storeAll(update);
    }
    
    private void adicionarAtualizarConhecimentosFedexApartirNotasEdi(String chaveMdfe, ControleCarga controleCarga, List<Integer> notasEdi) {
    	List<ConhecimentoFedex> addUpdate = new ArrayList<ConhecimentoFedex>();
        for (Integer numeroNotaFiscalEdi: notasEdi) {
            ConhecimentoFedex conhecimentoFedex = conhecimentoFedexService.findByChaveMdfeAndConhecimentoFedex(
                    chaveMdfe, numeroNotaFiscalEdi.toString() );
            if (conhecimentoFedex == null) {
            	conhecimentoFedex = converterNotaEdiParaConhecimentoFedex(chaveMdfe, numeroNotaFiscalEdi);
            }
            conhecimentoFedex.setControleCarga(controleCarga);
        	
        	addUpdate.add(conhecimentoFedex);
      }
        conhecimentoFedexService.storeAll(addUpdate);
    }
    
    private String getValorComplementoFedex(String cnpjRemetente, List<NotaFiscalEdiComplemento> complementos, CampoNotaFiscalEdiComplementoFedex campo) {
    	String valor = null;
    	if (complementos != null) {
    		InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.findByNrIdentificacaoClienteAndDsCampo(cnpjRemetente, campo.getNomeCampo());
    		if (informacaoDoctoCliente != null) {
	    		for (NotaFiscalEdiComplemento complemento : complementos) {
	    			if (informacaoDoctoCliente.getIdInformacaoDoctoCliente().equals(complemento.getIndcIdInformacaoDoctoClien())) {
	    				valor = complemento.getValorComplemento();
	    				break;
	    			}
	    		}
    		}
    	}
    	return valor;
    }
    
    private static final Long ID_PAIS_BRASIL = 30L;
    
    private ConhecimentoFedex converterNotaEdiParaConhecimentoFedex(String chaveMdfe, Integer numeroNotaEdi) {
        ConhecimentoFedex conhecimentoFedex = new ConhecimentoFedex();
            
        NotaFiscalEdi notaFiscalEdi = notaFiscalExpedicaoEDIService.findNotaFiscalEdiByChaveMdfeFedexENumeroNotaFiscal(chaveMdfe, numeroNotaEdi);
        if (notaFiscalEdi != null) {
        	String cnpjRemetenteNotaFiscalEdi = notaFiscalEdi.getCnpjReme().toString();
        	conhecimentoFedex.setDoctoServico(null);
        	
        	String cnpjPessoa = getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.CNPJ_REMETENTE_CTE_FEDEX); 
        	conhecimentoFedex.setCnpjRemetente(cnpjPessoa);
        	conhecimentoFedex.setNomeRemetente(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.NOME_REMETENTE_CTE_FEDEX));
        	
        	cnpjPessoa = getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.CNPJ_DESTINATARIO_CTE_FEDEX); 
        	conhecimentoFedex.setCnpjDestinatario(cnpjPessoa);
        	conhecimentoFedex.setNomeDestinatario(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.NOME_DESTINATARIO_CTE_FEDEX));
        	
        	conhecimentoFedex.setCnpjTomador(notaFiscalEdi.getCnpjTomador().toString());
        	conhecimentoFedex.setNomeTomador(notaFiscalEdi.getNomeTomador());
        	conhecimentoFedex.setSiglaFilialOrigem(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.FILIAL_ORIGEM_CTE_FEDEX));
        	conhecimentoFedex.setSiglaFilialDestino(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.FILIAL_DESTINO_CTE_FEDEX));
        	conhecimentoFedex.setTipoServico(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.TIPO_SERVICO_CTE_FEDEX));
        	conhecimentoFedex.setTipoMoeda(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.MOEDA_CTE_FEDEX));
        	conhecimentoFedex.setTipoDocumento(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.TIPO_DOCUMENTO_FEDEX));
        	conhecimentoFedex.setTipoFrete(notaFiscalEdi.getTipoFrete());
        	conhecimentoFedex.setNumeroConhecimento(notaFiscalEdi.getNrNotaFiscal().toString());
        	conhecimentoFedex.setNumeroSerie(notaFiscalEdi.getSerieNf());
        	conhecimentoFedex.setChaveCTEFedex(notaFiscalEdi.getChaveNfe());
        	conhecimentoFedex.setDataEmissao(notaFiscalEdi.getDataEmissaoNf());
        	
        	conhecimentoFedex.setValorMercadoria(notaFiscalEdi.getVlrTotalMerc());
        	conhecimentoFedex.setQtdvolume(notaFiscalEdi.getQtdeVolumes());
        	conhecimentoFedex.setPesoReal(notaFiscalEdi.getPesoReal());
        	conhecimentoFedex.setPesoAferido(new BigDecimal(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.PESO_AFERIDO_CTE_FEDEX)));
        	conhecimentoFedex.setPesoCubado(new BigDecimal(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.PESO_AFORADO_CTE_FEDEX)));
        	conhecimentoFedex.setPesoCalculoFrete(new BigDecimal(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.PESO_CALCULO_CTE_FEDEX)));
        	conhecimentoFedex.setChaveMdfe(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.CHAVE_MDFE_FEDEX));
        	conhecimentoFedex.setPlacaVeiculo(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.PLACA_VEICULO_FEDEX));
        	conhecimentoFedex.setPlacaCarreta(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.PLACA_CARRETA_FEDEX));
        	conhecimentoFedex.setNumeroIdentificacaoMotorista(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.CNPJ_MOTORISTA_FEDEX));
        	conhecimentoFedex.setNomeMotorista(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.NOME_MOTORISTA_FEDEX));
        	conhecimentoFedex.setNumeroManifestoViagemFedex(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.MANIFESTO_FEDEX));
        	conhecimentoFedex.setNaturezaProduto(getValorComplementoFedex(cnpjRemetenteNotaFiscalEdi, notaFiscalEdi.getComplementos(), CampoNotaFiscalEdiComplementoFedex.NATUREZA_PRODUTO_FEDEX));
        	conhecimentoFedex.setEspecieDocumento(notaFiscalEdi.getEspecie());
        	conhecimentoFedex.setDataLogEDI(notaFiscalEdi.getDataLog());
        	
        	conhecimentoFedex.setIdPais(ID_PAIS_BRASIL);
        	conhecimentoFedex.setCteDescarregado("N");

        	if (notaFiscalEdi.getVolumes() != null && !notaFiscalEdi.getVolumes().isEmpty()) {
        		if (conhecimentoFedex.getVolumes() == null) {
            		conhecimentoFedex.setVolumes(new ArrayList<ConhecimentoVolumeFedex>());
        		}
        		for (NotaFiscalEdiVolume volumeEdi : notaFiscalEdi.getVolumes()) {
        			ConhecimentoVolumeFedex volume = new ConhecimentoVolumeFedex();
        			volume.setCodigoVolume(volumeEdi.getCodigoVolume());
        			volume.setConhecimentoFedex(conhecimentoFedex);
        			conhecimentoFedex.getVolumes().add(volume);
        		}
        	}
        }
        
        return conhecimentoFedex;
    }

    /**
     * @param controleCarga
     * @param itemListPagtoPedagioCc
     * @param itemListConfigPagtoPedagioCc
     * @param itemListPostoPassagemCc
     * @param itemListConfigPostoPassagemCc
     * @return
     */
    @SuppressWarnings("rawtypes")
    private BigDecimal storePagtoPedagioCcAndPostosPassagemCc(ControleCarga controleCarga,
                                                              ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc,
                                                              ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc) {

        BigDecimal vlPedagio = BigDecimalUtils.ZERO;
        for (Iterator iter = itemListPagtoPedagioCc.iterator(controleCarga.getIdControleCarga(), itemListConfigPagtoPedagioCc); iter.hasNext(); ) {
            PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();
            if (pagtoPedagioCc.getTipoPagamPostoPassagem().getBlCartaoPedagio() != null && pagtoPedagioCc.getTipoPagamPostoPassagem().getBlCartaoPedagio()) {
                if (pagtoPedagioCc.getOperadoraCartaoPedagio() == null) {
                    throw new BusinessException("LMS-05079");
                }
                if (pagtoPedagioCc.getCartaoPedagio() == null) {
                    throw new BusinessException("LMS-05080");
                }
            }
            vlPedagio = vlPedagio.add(pagtoPedagioCc.getVlPedagio());
            pagtoPedagioCc.setControleCarga(controleCarga);
        }
        pagtoPedagioCcService.storePagtoPedagioCc(itemListPagtoPedagioCc);

        // Salva os dados da grid Postos de Passagem
        for (Iterator iter = itemListPostoPassagemCc.iterator(controleCarga.getIdControleCarga(), itemListConfigPostoPassagemCc); iter.hasNext(); ) {
            PostoPassagemCc postoPassagemCc = (PostoPassagemCc) iter.next();
            postoPassagemCc.setControleCarga(controleCarga);
            if (postoPassagemCc.getTipoPagamPostoPassagem() == null) {
                throw new BusinessException("LMS-05078");
            }
            if (postoPassagemCc.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem() == null) {
                postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagemService.findById(postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem()));
            }
        }
        postoPassagemCcService.storePostoPassagemCc(itemListPostoPassagemCc);
        return vlPedagio;
    }


    /**
     * @param idRota
     * @param idSolicitacaoContratacao
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Long findNrTempoViagemBySolicitacaoContratacao(Long idRota, Long idSolicitacaoContratacao) {
        Rota rota = rotaService.findById(idRota);
        List<FilialRota> filiaisRota = filialRotaService.findByIdRota(rota.getIdRota());
        List listaFiliais = new ArrayList();
        for (FilialRota filialRota : filiaisRota) {
            listaFiliais.add(filialRota.getFilial());
        }

        Integer nrDistancia = fluxoFilialService.findDistanciaTotalFluxoFilialOrigemDestino(listaFiliais, JTDateTimeUtils.getDataAtual());
        return solicitacaoContratacaoService.findHrTempoViagem(nrDistancia);
    }


    /**
     * Verifica se um integrante está em uma outra EQUIPE_OPERACAO que esteja vinculada a um
     * CONTROLE_CARGA com status diferente de "FE" e de "CA".
     *
     * @param idControleCarga
     * @param idPessoa
     * @param idUsuario
     */
    @SuppressWarnings("rawtypes")
    public void validateIntegranteEmEquipesComControleCarga(Long idControleCarga, Long idPessoa, Long idUsuario) {
        List retorno = findIntegranteEmEquipesComControleCarga(idControleCarga, idPessoa, idUsuario);
        if (!retorno.isEmpty()) {
            Map map = (Map) retorno.get(0);
            String siglaNumero = FormatUtils.formatSgFilialWithLong((String) map.get("sgFilial"), (Long) map.get(NR_CONTROLE_CARGA), FORMAT_PATTERN);
            throw new BusinessException("LMS-05055", new Object[]{(String) map.get("nmIntegrante"), siglaNumero});
        }
    }


    /**
     * Verifica se um integrante está em uma outra EQUIPE_OPERACAO que esteja vinculada a um
     * CONTROLE_CARGA com status diferente de "FE" e de "CA".
     *
     * @param idControleCarga
     * @param idPessoa
     * @param idUsuario
     * @return uma lista vazia caso não encontre o integrante em outra equipe_operacao.
     */
    @SuppressWarnings("rawtypes")
    public List findIntegranteEmEquipesComControleCarga(Long idControleCarga, Long idPessoa, Long idUsuario) {
        return getControleCargaDAO().findIntegranteEmEquipesComControleCarga(idControleCarga, idPessoa, idUsuario);
    }


    /**
     * Valida se o integrante equipe ja existe na sessao. Caso ele exista levanta uma exceção.
     *
     * @param iterIntegranteEquipe
     * @param tpIntegrante
     * @param idUsuario
     * @param idPessoa
     * @param
     */
    @SuppressWarnings("rawtypes")
    public void validateIntegranteExisteSessao(Iterator iterIntegranteEquipe, String tpIntegrante, Long idUsuario, Long idPessoa, Long idIntegranteEqOperac) {
        boolean retorno = false;
        for (Iterator iter = iterIntegranteEquipe; iter.hasNext(); ) {
            IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
            if (integranteEqOperac.getIdIntegranteEqOperac() != null && idIntegranteEqOperac != null
                    && integranteEqOperac.getIdIntegranteEqOperac().compareTo(idIntegranteEqOperac) == 0) {
                continue;
            }
            if (tpIntegrante.equals(integranteEqOperac.getTpIntegrante().getValue())) {
                if ("F".equals(tpIntegrante)) {
                    if (integranteEqOperac.getUsuario().getIdUsuario().compareTo(idUsuario) == 0) {
                        retorno = true;
                        break;
                    }
                } else if (integranteEqOperac.getPessoa().getIdPessoa().compareTo(idPessoa) == 0) {
                    retorno = true;
                    break;
                }
            }
        }
        if (retorno) {
            throw new BusinessException("LMS-05311");
        }
    }


    /**
     * @param map
     */
    public void validatePaginatedControleCarga(TypedFlatMap map) {
        if (map.getYearMonthDay(DT_GERACAO_INICIAL) != null && map.getYearMonthDay(DT_GERACAO_FINAL) != null) {
            long difDias = JTDateTimeUtils.getIntervalInDays(map.getYearMonthDay(DT_GERACAO_INICIAL), map.getYearMonthDay(DT_GERACAO_FINAL));
            if (difDias > DIF_DIAS) {
                throw new BusinessException(LMS_05057);
            }
        }
        if (checkDataValidatePaginatedControleCarga(map) &&
                (map.getLong(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE) == null &&
                        map.getLong(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE) == null)) {
            throw new BusinessException(LMS_05056);
        }
    }

    private boolean checkDataValidatePaginatedControleCarga(TypedFlatMap map) {

        Boolean filialOrigemAndControleCargaNotNull = map.getLong(FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL) != null
                && map.getLong(NR_CONTROLE_CARGA) != null;

        Boolean dtGeracaoInicialFinalFilialByIdFilialOrigemNotNull = map.getYearMonthDay(DT_GERACAO_INICIAL) != null
                && map.getYearMonthDay(DT_GERACAO_FINAL) != null
                && map.getLong(FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL) != null;

        Boolean notaCreditoNotNull = map.getLong(NOTA_CREDITO_ID_NOTA_CREDITO) != null;

        return !(dtGeracaoInicialFinalFilialByIdFilialOrigemNotNull ||
                filialOrigemAndControleCargaNotNull || notaCreditoNotNull);
    }

    /**
     * @param criteria
     * @param findDefinition
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginatedControleCarga(TypedFlatMap criteria, FindDefinition findDefinition) {
        ResultSetPage rsp = getControleCargaDAO().findPaginatedControleCarga(criteria, findDefinition);
        List newList = new ArrayList();
        for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ) {
            Object[] obj = (Object[]) iter.next();
            TypedFlatMap tfm = new TypedFlatMap();
            tfm.put(ID_CONTROLE_CARGA, obj[ARR_POS_0]);
            tfm.put(NR_CONTROLE_CARGA, obj[ARR_POS_1]);
            if (obj[ARR_POS_2] != null) {
                tfm.put(DH_GERACAO, obj[ARR_POS_2]);
            }
            if (obj[ARR_POS_3] != null) {
                tfm.put(DH_PREVISAO_SAIDA, obj[ARR_POS_3]);
            }
            tfm.put(TP_CONTROLE_CARGA, domainValueService.findDomainValueByValue("DM_TIPO_CONTROLE_CARGAS", (String) obj[ARR_POS_4]));
            if (obj[ARR_POS_5] != null) {
                tfm.put("tpRotaViagemDominio", domainValueService.findDomainValueByValue("DM_TIPO_ROTA_VIAGEM_CC", (String) obj[ARR_POS_5]));
            }
            tfm.put(FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL, obj[ARR_POS_6]);
            tfm.put("filialByIdFilialOrigem.sgFilial", obj[ARR_POS_7]);
            tfm.put("filialByIdFilialOrigem.pessoa.nmFantasia", obj[ARR_POS_8]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE, obj[ARR_POS_9]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_FROTA, obj[ARR_POS_10]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_NR_IDENTIFICADOR, obj[ARR_POS_11]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE, obj[ARR_POS_12]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_FROTA, obj[ARR_POS_13]);
            tfm.put(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_NR_IDENTIFICADOR, obj[ARR_POS_14]);
            tfm.put("dsRotaColeta", obj[ARR_POS_15]);
            tfm.put("dsRotaViagem", obj[ARR_POS_16]);
            tfm.put("filialByIdFilialDestino.idFilial", obj[ARR_POS_17]);
            tfm.put("filialByIdFilialDestino.sgFilial", obj[ARR_POS_18]);
            tfm.put("filialByIdFilialDestino.pessoa.nmFantasia", obj[ARR_POS_19]);
            tfm.put(MOTORISTA_PESSOA_NM_PESSOA, obj[ARR_POS_20]);
            tfm.put(MOTORISTA_PESSOA_NR_IDENTIFICACAO, obj[ARR_POS_21]);
            if (obj[ARR_POS_22] != null) {
                tfm.put("motorista.pessoa.tpIdentificacao",
                        domainValueService.findDomainValueByValue("DM_TIPO_IDENTIFICACAO_PESSOA", (String) obj[ARR_POS_22]));
            }
            tfm.put("motorista.pessoa.idPessoa", obj[ARR_POS_23]);
            tfm.put(TP_STATUS_CONTROLE_CARGA, domainValueService.findDomainValueByValue("DM_STATUS_CONTROLE_CARGA", (String) obj[ARR_POS_24]));

            if (obj[ARR_POS_25] != null) {
                tfm.put("nrManif", obj[ARR_POS_25]);
            }
            
            // LMSA-6340
            tfm.put("tipoCargaCompartilhada", obj[ARR_POS_26]);

            newList.add(tfm);
        }
        rsp.setList(newList);
        return rsp;
    }

    /**
     * @param criteria
     * @return
     */
    public Integer getRowCountControleCarga(TypedFlatMap criteria) {
        return getControleCargaDAO().getRowCountControleCarga(criteria);
    }

    /*
     * Método só pode ser chamado em classes Action...
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ControleCarga findByIdControleCarga(Long idControleCarga) {
        Map map = getControleCargaDAO().findByIdControleCarga(idControleCarga);
        List result = new ArrayList();
        result.add(map);
        result = new AliasToNestedBeanResultTransformer(ControleCarga.class).transformListResult(result);
        return (ControleCarga) result.get(0);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findPaginatedControleCargaByLocalizacoMerc(Long idDoctoServico) {
        ResultSetPage rs = lmControleCargaDao.findPaginatedControleCarga(idDoctoServico);
        List newList = null;
        if (!rs.getList().isEmpty()) {
            newList = new ArrayList();
            for (Iterator iter = rs.getList().iterator(); iter.hasNext(); ) {
                Object[] obj = (Object[]) iter.next();
                TypedFlatMap typedFlatMap = new TypedFlatMap();
                DomainValue tpControleCarga = null;
                DomainValue tpStatusControleCarga = null;
                if (obj[ARR_POS_4] != null) {
                    tpControleCarga = domainValueService.findDomainValueByValue("DM_TIPO_CONTROLE_CARGAS", (String) obj[ARR_POS_4]);
                    typedFlatMap.put(TP_CONTROLE_CARGA, tpControleCarga);
                }
                if (obj[ARR_POS_9] != null) {
                    tpStatusControleCarga = domainValueService.findDomainValueByValue("DM_STATUS_CONTROLE_CARGA", (String) obj[ARR_POS_9]);
                    typedFlatMap.put(TP_STATUS_CONTROLE_CARGA, tpStatusControleCarga);
                }
                typedFlatMap.put("sgFilialOrigem", obj[ARR_POS_0]);
                typedFlatMap.put("nrControle", obj[ARR_POS_1]);
                typedFlatMap.put(ID_CONTROLE_CARGA, obj[ARR_POS_2]);
                typedFlatMap.put("sgFilialDest", obj[ARR_POS_3]);
                typedFlatMap.put(DH_GERACAO, obj[ARR_POS_5]);
                typedFlatMap.put("rota", obj[ARR_POS_6]);
                typedFlatMap.put("dhSaida", obj[ARR_POS_7]);
                typedFlatMap.put("dhChegada", obj[ARR_POS_8]);

                newList.add(typedFlatMap);
            }
        }
        return newList;
    }


    /**
     * @param criteria
     */
    public void storeManterControleCarga(TypedFlatMap criteria) {
        Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
        Long idMeioTransporteTransportado = criteria.getLong(MEIO_TRANSPORTE_BY_ID_TRANSPORTADO_ID_MEIO_TRANSPORTE);
        Long idMeioTransporteSemiRebocado = criteria.getLong(MEIO_TRANSPORTE_BY_ID_SEMI_REBOCADO_ID_MEIO_TRANSPORTE);
        Long idRotaColetaEntrega = criteria.getLong(ROTA_COLETA_ENTREGA_ID_ROTA_COLETA_ENTREGA);
        String tpControleCarga = criteria.getString("tpControleCargaValor");
        Boolean veiculoInformadoManualmente = criteria.getBoolean("veiculoInformadoManualmente");
        Long idSolicitacaoContratacao = criteria.getLong(SOLICITACAO_CONTRATACAO_ID_SOLICITACAO_CONTRATACAO);

        storeManterControleCarga(idControleCarga,
                criteria.getLong(TIPO_TABELA_COLETA_ENTREGA_ID_TIPO_TABELA_COLETA_ENTREGA),
                criteria.getLong(TABELA_COLETA_ENTREGA_ID_TABELA_COLETA_ENTREGA),
                idMeioTransporteTransportado,
                idMeioTransporteSemiRebocado,
                criteria.getLong(MOTORISTA_ID_MOTORISTA),
                criteria.getLong(PROPRIETARIO_ID_PROPRIETARIO),
                idRotaColetaEntrega,
                idSolicitacaoContratacao,
                criteria.getBigDecimal(VL_FRETE_CARRETEIRO),
                veiculoInformadoManualmente,
                criteria.getBoolean("semiReboqueInformadoManualmente"),
                criteria.getBoolean("motoristaInformadoManualmente"),
                criteria.getString("solicitacaoSinal.nrTelefoneEmpresa"),
                criteria.getString("solicitacaoSinal.nmEmpresaAnterior"),
                criteria.getString("solicitacaoSinal.nmResponsavelEmpresa"),
                criteria.getBoolean("solicitacaoSinal.blPertenceProjCaminhoneiro"),
                criteria.getBoolean(BL_ENTREGA_DIRETA));

        if (veiculoInformadoManualmente && idMeioTransporteTransportado != null) {
            postoPassagemCcService.generatePostosPassagemCcAndPagtoPedagioCc(idControleCarga,
                    tpControleCarga,
                    idRotaColetaEntrega,
                    idMeioTransporteTransportado,
                    idMeioTransporteSemiRebocado);
        }
    }


    /**
     * @param idControleCarga
     * @param idTipoTabelaColetaEntrega
     * @param idTabelaColetaEntrega
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param idMotorista
     * @param idProprietario
     * @param idRotaColetaEntrega
     * @param vlFreteCarreteiro
     * @param veiculoInformadoManualmente
     * @param semiReboqueInformadoManualmente
     * @param motoristaInformadoManualmente
     * @param nrTelefoneEmpresaSolicitacaoSinal
     * @param nmEmpresaAnteriorSolicitacaoSinal
     * @param nmResponsavelEmpresaSolicitacaoSinal
     * @param blPertenceProjCaminhoneiroSolicitacaoSinal
     */
    @SuppressWarnings("rawtypes")
    private void storeManterControleCarga(Long idControleCarga,
                                          Long idTipoTabelaColetaEntrega,
                                          Long idTabelaColetaEntrega,
                                          Long idMeioTransporteTransportado,
                                          Long idMeioTransporteSemiRebocado,
                                          Long idMotorista,
                                          Long idProprietario,
                                          Long idRotaColetaEntrega,
                                          Long idSolicitacaoContratacao,
                                          BigDecimal vlFreteCarreteiro,
                                          Boolean veiculoInformadoManualmente,
                                          Boolean semiReboqueInformadoManualmente,
                                          Boolean motoristaInformadoManualmente,
                                          String nrTelefoneEmpresaSolicitacaoSinal,
                                          String nmEmpresaAnteriorSolicitacaoSinal,
                                          String nmResponsavelEmpresaSolicitacaoSinal,
                                          Boolean blPertenceProjCaminhoneiroSolicitacaoSinal,
                                          Boolean blEntregaDireta) {
        Motorista motorista = getMotorista(idMotorista);
        Proprietario proprietario = getProprietario(idProprietario);
        MeioTransporte meioTransporteTransportado = getMeioTransporte(idMeioTransporteTransportado);
        MeioTransporte meioTransporteSemiRebocado = getMeioTransporte(idMeioTransporteSemiRebocado);
        RotaColetaEntrega rotaColetaEntrega = getRotaColetaEntrega(idRotaColetaEntrega);
        SolicitacaoContratacao solicitacaoContratacao = getSolicitacaoContratacao(idSolicitacaoContratacao);
        TipoTabelaColetaEntrega tipoTabelaColetaEntrega = getTipoTabelaColetaEntrega(idTipoTabelaColetaEntrega);
        TabelaColetaEntrega tabelaColetaEntrega = getTabelaColetaEntrega(idTabelaColetaEntrega);
        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
        Filial filialUsuario = SessionUtils.getFilialSessao();

        /** Carrega Controle Carga */
        final ControleCarga controleCarga = findById(idControleCarga);

        /** Valida Veiculo Controle Carga: LMS-671 */
        final MeioTransporte meioTransporteByIdTransportado = controleCarga.getMeioTransporteByIdTransportado();
        /** Verifica se meioTransporte é diferente do existente no Controle de Carga */
        if (idMeioTransporteTransportado != null &&
                (meioTransporteByIdTransportado == null || !idMeioTransporteTransportado.equals(meioTransporteByIdTransportado.getIdMeioTransporte()))) {
            validateVeiculoControleCarga(idMeioTransporteTransportado, false);
        }

        controleCarga.setBlEntregaDireta(blEntregaDireta);
        controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporteSemiRebocado);
        controleCarga.setMeioTransporteByIdTransportado(meioTransporteTransportado);
        controleCarga.setMotorista(motorista);
        controleCarga.setProprietario(proprietario);
        controleCarga.setRotaColetaEntrega(rotaColetaEntrega);
        controleCarga.setSolicitacaoContratacao(solicitacaoContratacao);
        List<TabelaColetaEntregaCC> tabelasColetasCC = storeTabelasColetaEntrega(controleCarga, idMeioTransporteTransportado, idRotaColetaEntrega, tabelaColetaEntrega, tipoTabelaColetaEntrega);

        validateTabelasColetaEntrega(tabelasColetasCC, controleCarga, tabelaColetaEntrega, idMeioTransporteTransportado, idRotaColetaEntrega);

        if (vlFreteCarreteiro == null) {
            if (solicitacaoContratacao != null) {
                vlFreteCarreteiro = solicitacaoContratacao.getVlFreteNegociado();
            } else if (controleCarga.getRotaIdaVolta() != null) {
                RotaIdaVolta rotaIdaVolta = rotaIdaVoltaService.findById(controleCarga.getRotaIdaVolta().getIdRotaIdaVolta());
                vlFreteCarreteiro = rotaIdaVolta.getVlFreteKm().multiply(BigDecimal.valueOf(rotaIdaVolta.getNrDistancia().longValue()));
            }
        }
        controleCarga.setVlFreteCarreteiro(vlFreteCarreteiro);
        controleCarga.setVlPedagio(pagtoPedagioCcService.getVlPedagioByIdControleCarga(controleCarga.getIdControleCarga()));
        store(controleCarga);

        boolean calculoPadrao = isCalculoPadrao();

        if (!calculoPadrao) {
            tabelaColetaEntregaCCService.storeAll(tabelasColetasCC);
        }


        if (motoristaInformadoManualmente && motorista != null) {
            storeMotoristaControleCarga(controleCarga, motorista);
        }

        if (meioTransporteTransportado != null && veiculoInformadoManualmente && proprietario != null) {
            Long idVeiculoControleCarga = storeVeiculoControleCarga(controleCarga, false, meioTransporteTransportado, controleCarga.getSolicitacaoContratacao());
            storePagtoProprietarioControleCarga(controleCarga, proprietario, getVeiculoControleCarga(idVeiculoControleCarga));

            List listaEcc = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(filialUsuario.getIdFilial(), idControleCarga, "IC");
            if (!listaEcc.isEmpty()) {
                EventoMeioTransporte emt = new EventoMeioTransporte();
                emt.setControleCarga(controleCarga);
                emt.setDhInicioEvento(dhAtual);
                emt.setFilial(filialUsuario);
                emt.setMeioTransporte(meioTransporteTransportado);
                emt.setTpSituacaoMeioTransporte(new DomainValue("EMCA"));
                emt.setUsuario(SessionUtils.getUsuarioLogado());
                eventoMeioTransporteService.generateEvent(emt);
            }
        }

        if (semiReboqueInformadoManualmente && meioTransporteSemiRebocado != null) {
            storeSemiReboqueControleCarga(controleCarga, false, meioTransporteSemiRebocado);

            List listaEcc = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(filialUsuario.getIdFilial(), idControleCarga, "IC");
            if (!listaEcc.isEmpty()) {
                EventoMeioTransporte emt = new EventoMeioTransporte();
                emt.setControleCarga(controleCarga);
                emt.setDhInicioEvento(dhAtual);
                emt.setFilial(filialUsuario);
                emt.setMeioTransporte(meioTransporteSemiRebocado);
                emt.setTpSituacaoMeioTransporte(new DomainValue("EMCA"));
                emt.setUsuario(SessionUtils.getUsuarioLogado());
                eventoMeioTransporteService.generateEvent(emt);
            }
        }

        //Salva os dados da aba Solicitação de Sinal
        if (!StringUtils.isBlank(nmEmpresaAnteriorSolicitacaoSinal)) {
            solicitacaoSinalService.generateEnviarSolicitacaoSinal(SessionUtils.getFilialSessao().getIdFilial(),
                    controleCarga.getIdControleCarga(),
                    idMeioTransporteTransportado,
                    idMeioTransporteSemiRebocado,
                    idMotorista,
                    nrTelefoneEmpresaSolicitacaoSinal,
                    nmEmpresaAnteriorSolicitacaoSinal,
                    nmResponsavelEmpresaSolicitacaoSinal,
                    blPertenceProjCaminhoneiroSolicitacaoSinal,
                    null);
        }
        validateParcelaColetaEntrega(idControleCarga);
    }


    /**
     * @param idControleCarga
     * @param listaPagamentos
     * @param listaPostos
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void storePostosPassagem(Long idControleCarga, List listaPagamentos, List listaPostos) {
        List listaPagtosCadastrados = Collections.emptyList();

        List listaPostosAlterados = new ArrayList();
        for (Iterator iter = listaPostos.iterator(); iter.hasNext(); ) {
            TypedFlatMap map = (TypedFlatMap) iter.next();
            Long idPostoPassagemCc = map.getLong("postoPassagemCc_idPostoPassagemCc");

            PostoPassagemCc postoPassagemCc = postoPassagemCcService.findById(idPostoPassagemCc);
            Long idTipoPagamPostoPassagem = map.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem");
            if (idTipoPagamPostoPassagem.compareTo(postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem()) != 0) {
                postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagemService.findById(idTipoPagamPostoPassagem));
                listaPostosAlterados.add(postoPassagemCc);
            }
        }

        if (!listaPostosAlterados.isEmpty()) {
            postoPassagemCcService.storePostoPassagemCcAll(listaPostosAlterados);
            pagtoPedagioCcService.removeByIdControleCarga(idControleCarga);
            listaPagtosCadastrados = pagtoPedagioCcService.generatePagtoPedagioCcByIdControleCarga(idControleCarga);
        }

        if (listaPagtosCadastrados.isEmpty()) {
            listaPagtosCadastrados = pagtoPedagioCcService.findPagtoPedagioCcByIdControleCarga(idControleCarga);
        }

        List listaPagtosAlterados = new ArrayList();
        for (Iterator iter = listaPagtosCadastrados.iterator(); iter.hasNext(); ) {
            PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();
            boolean alterouPagto = false;
            for (Iterator iterPagtos = listaPagamentos.iterator(); iterPagtos.hasNext(); ) {
                TypedFlatMap map = (TypedFlatMap) iterPagtos.next();
                Long idTipoPagamPostoPassagem = map.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem");
                if (idTipoPagamPostoPassagem.equals(pagtoPedagioCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem())) {
                    Long idOperadoraCartaoPedagio = map.getLong("operadoraCartaoPedagio_idOperadoraCartaoPedagio");
                    Long idCartaoPedagio = map.getLong("cartaoPedagio_idCartaoPedagio");

                    if (idOperadoraCartaoPedagio != null) {
                        if (pagtoPedagioCc.getOperadoraCartaoPedagio() == null || !pagtoPedagioCc.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio().equals(idOperadoraCartaoPedagio)) {
                            pagtoPedagioCc.setOperadoraCartaoPedagio(operadoraCartaoPedagioService.findById(idOperadoraCartaoPedagio));
                            pagtoPedagioCc.setCartaoPedagio(cartaoPedagioService.findById(idCartaoPedagio));
                            alterouPagto = true;
                        }
                    } else if (pagtoPedagioCc.getOperadoraCartaoPedagio() != null) {
                        pagtoPedagioCc.setOperadoraCartaoPedagio(null);
                        pagtoPedagioCc.setCartaoPedagio(null);
                        alterouPagto = true;
                    }
                    if (alterouPagto) {
                        listaPagtosAlterados.add(pagtoPedagioCc);
                    }
                    break;
                }
            }
        }

        if (!listaPagtosAlterados.isEmpty()) {
            pagtoPedagioCcService.storePagtoPedagioCcAll(listaPagtosAlterados);
        }
    }


    /**
     * @param tpStatusManifesto
     * @param idDoctoServico
     * @param tpManifesto
     * @param strDocumento
     * @param dhAtual
     */
    private void generateEventoCancelamentoDocumento(String tpStatusManifesto, Long idDoctoServico, String tpManifesto, String strDocumento, DateTime dhAtual) {
        Short cdEvento = null;
        if ("PM".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA : ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM;
        } else if ("EC".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA : ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM;
        } else if ("CC".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA : ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM;
        }

        String tpDocumento = "PMV";
        if ("E".equals(tpManifesto)) {
            tpDocumento = "PME";
        }

        incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                cdEvento, idDoctoServico,
                SessionUtils.getFilialSessao().getIdFilial(),
                strDocumento, dhAtual, null,
                SessionUtils.getFilialSessao().getSiglaNomeFilial(),
                tpDocumento);
    }

    //LMS-3544
    @SuppressWarnings({ "rawtypes", "unused" })
    public Map generateCancelamentoControleCargaAndMdfe(Long idControleCarga, Long idMotivoCancelamentoCc, String dsEvento) {
        generateCancelamentoControleCarga(idControleCarga, idMotivoCancelamentoCc, dsEvento);
        ControleCarga cc = findById(idControleCarga);
        Map map = manifestoEletronicoService.cancelarMdfe(idControleCarga);

        ciotService.executeCancelamentoCIOT(idControleCarga);

        // LMSA-6520
        conhecimentoFedexService.desvincularConhecimentosFedexControleCarga(idControleCarga);

        return map;
    }

    public void generateCancelamentoControleCarga(Long idControleCarga, Long idMotivoCancelamentoCc, String dsEvento) {
        generateCancelamentoControleCarga(idControleCarga, idMotivoCancelamentoCc, dsEvento, false, SessionUtils.getFilialSessao().getIdFilial());
    }

    /**
     * @param idControleCarga
     * @param idMotivoCancelamentoCc
     * @param dsEvento
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void generateCancelamentoControleCarga(Long idControleCarga, Long idMotivoCancelamentoCc, String dsEvento, boolean isAutoCancel, Long idFilial) {
        ControleCarga cc = findById(idControleCarga);
        if ("CA".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException("LMS-05073");
        }

        if (!isAutoCancel && !SessionUtils.getFilialSessao().getIdFilial().equals(cc.getFilialByIdFilialOrigem().getIdFilial())) {
            throw new BusinessException("LMS-05077");
        }

        List listEventos = eventoControleCargaService.
                findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "SP");

        if (!isAutoCancel && !listEventos.isEmpty()) {
            throw new BusinessException("LMS-05138");
        }
        if (!isAutoCancel && (manifestoService.validateManifestoByCancelamentoControleCarga(idControleCarga) ||
                manifestoColetaService.validateManifestoByCancelamentoControleCarga(idControleCarga))) {
            throw new BusinessException("LMS-05141");
        }

        Filial filialUsuario = SessionUtils.getFilialSessao();
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();

        Map mapControleCarga = new HashMap();
        mapControleCarga.put(ID_CONTROLE_CARGA, idControleCarga);

        List listaDoctosServico = doctoServicoService.findDoctoServicoByIdControleCarga(idControleCarga);
        for (Iterator iter = listaDoctosServico.iterator(); iter.hasNext(); ) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();

            Long idDoctoServico = tfm.getLong(ID_DOCTO_SERVICO);
            String strDocumento = tfm.getString(SG_FILIAL_ORIGEM_MANIFESTO) + " " + StringUtils.leftPad(tfm.getLong(NR_PRE_MANIFESTO).toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
            String tpManifesto = tfm.getString(TP_MANIFESTO_VALUE);
            String tpStatusManifesto = tfm.getString(TP_STATUS_MANIFESTO_VALUE);

            if ("PM".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDocumento("PM", idDoctoServico, tpManifesto, strDocumento, dhAtual);
            } else if ("EC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDocumento("PM", idDoctoServico, tpManifesto, strDocumento, dhAtual);
                generateEventoCancelamentoDocumento("EC", idDoctoServico, tpManifesto, strDocumento, dhAtual);
            } else if ("CC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDocumento("PM", idDoctoServico, tpManifesto, strDocumento, dhAtual);
                generateEventoCancelamentoDocumento("EC", idDoctoServico, tpManifesto, strDocumento, dhAtual);
                generateEventoCancelamentoDocumento("CC", idDoctoServico, tpManifesto, strDocumento, dhAtual);
            }
        }

        generateCancelamentoControleCargaDispositivos(idControleCarga);

        LocalizacaoMercadoria localizacaoMercadoriaNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
        List listaVolume = volumeNotaFiscalService.findVolumeByIdControleCarga(idControleCarga);
        for (Iterator iter = listaVolume.iterator(); iter.hasNext(); ) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();

            Long idVolume = tfm.getLong("idVolumeNotaFiscal");
            String tpManifesto = tfm.getString(TP_MANIFESTO_VALUE);
            String tpStatusManifesto = tfm.getString(TP_STATUS_MANIFESTO_VALUE);

            if ("PM".equals(tpStatusManifesto)) {
                generateEventoCancelamentoVolume("PM", idVolume, tpManifesto);
            } else if ("EC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoVolume("PM", idVolume, tpManifesto);
                generateEventoCancelamentoVolume("EC", idVolume, tpManifesto);
            } else if ("CC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoVolume("PM", idVolume, tpManifesto);
                generateEventoCancelamentoVolume("EC", idVolume, tpManifesto);
                generateEventoCancelamentoVolume("CC", idVolume, tpManifesto);
            }

            VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findById(idVolume);
            if (volumeNotaFiscal.getLocalizacaoMercadoria() == null) {
                volumeNotaFiscal.setLocalizacaoMercadoria(localizacaoMercadoriaNoTerminal);
                volumeNotaFiscalService.store(volumeNotaFiscal);
            }
        }


        List listaManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (Iterator iter = listaManifestos.iterator(); iter.hasNext(); ) {
            Manifesto manifesto = (Manifesto) iter.next();
            EventoManifesto eventoManifesto = new EventoManifesto();
            eventoManifesto.setManifesto(manifesto);
            eventoManifesto.setFilial(filialUsuario);
            eventoManifesto.setUsuario(usuarioLogado);
            eventoManifesto.setDhEvento(dhAtual);
            eventoManifesto.setTpEventoManifesto(new DomainValue("CA"));
            eventoManifestoService.store(eventoManifesto);

            if ("CC".equals(manifesto.getTpStatusManifesto().getValue())) {
                dispositivoUnitizacaoService.executeVoltarDispositivosUnitizacaoCarregados(manifesto.getIdManifesto());
            }
            manifesto.setTpStatusManifesto(new DomainValue("CA"));
            manifestoService.storeBasic(manifesto);
        }

        preManifestoDocumentoService.removeByIdControleCarga(idControleCarga);

        pedidoColetaService.updateStatusColetaByIdControleCarga(idControleCarga);


        List listaManifestoColeta = manifestoColetaService.findManifestoColetaByIdControleCarga(idControleCarga);
        for (Iterator iter = listaManifestoColeta.iterator(); iter.hasNext(); ) {
            ManifestoColeta mc = (ManifestoColeta) iter.next();
            EventoManifestoColeta emc = new EventoManifestoColeta();
            emc.setManifestoColeta(mc);
            emc.setDhEvento(dhAtual);
            emc.setTpEventoManifestoColeta(new DomainValue("CA"));
            eventoManifestoColetaService.store(emc);
        }
        manifestoColetaService.updateSituacaoManifestoColetaByIdControleCarga(idControleCarga);

        if (cc.getMeioTransporteByIdTransportado() != null) {
            EventoMeioTransporte eventoMeioTransporteTransportado = new EventoMeioTransporte();
            eventoMeioTransporteTransportado.setTpSituacaoMeioTransporte(new DomainValue("ADFR"));
            eventoMeioTransporteTransportado.setDhInicioEvento(dhAtual);
            eventoMeioTransporteTransportado.setControleCarga(cc);
            eventoMeioTransporteTransportado.setFilial(filialUsuario);
            eventoMeioTransporteTransportado.setMeioTransporte(cc.getMeioTransporteByIdTransportado());
            eventoMeioTransporteTransportado.setUsuario(SessionUtils.getUsuarioLogado());
            eventoMeioTransporteService.generateEvent(eventoMeioTransporteTransportado);
        }

        if (cc.getMeioTransporteByIdSemiRebocado() != null) {
            EventoMeioTransporte eventoMeioTransporteSemiRebocado = new EventoMeioTransporte();
            eventoMeioTransporteSemiRebocado.setTpSituacaoMeioTransporte(new DomainValue("ADFR"));
            eventoMeioTransporteSemiRebocado.setDhInicioEvento(dhAtual);
            eventoMeioTransporteSemiRebocado.setControleCarga(cc);
            eventoMeioTransporteSemiRebocado.setFilial(filialUsuario);
            eventoMeioTransporteSemiRebocado.setMeioTransporte(cc.getMeioTransporteByIdSemiRebocado());
            eventoMeioTransporteSemiRebocado.setUsuario(SessionUtils.getUsuarioLogado());
            eventoMeioTransporteService.generateEvent(eventoMeioTransporteSemiRebocado);
        }

        equipeOperacaoService.updateEquipeOperacaoByCancelamentoControleCarga(idControleCarga, dhAtual);

        reciboPostoPassagemService.updateStatusReciboByIdControleCarga(idControleCarga);


        List listaRecibos = reciboFreteCarreteiroService.findReciboFreteCarreteiroByIdControleCarga(idControleCarga);
        for (Iterator iter = listaRecibos.iterator(); iter.hasNext(); ) {
            ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) iter.next();
            reciboFreteCarreteiroService.storeCancelarRecibo(rfc.getIdReciboFreteCarreteiro());
        }

        solicitacaoSinalService.storeCancelarSolicitacaoSinalByIdControleCarga(idControleCarga);

        generateEventoChangeStatusControleCarga(idControleCarga, idFilial,
                "CA", null, null, dsEvento, null, null, null, null, null, null, null, null);

        Map mapLacreCc = new HashMap();
        mapLacreCc.put(CONTROLE_CARGA, mapControleCarga);
        List listaLacres = lacreControleCargaService.find(mapLacreCc);
        List listaIdsLacres = new ArrayList();
        for (Iterator iter = listaLacres.iterator(); iter.hasNext(); ) {
            LacreControleCarga lcc = (LacreControleCarga) iter.next();
            listaIdsLacres.add(lcc.getIdLacreControleCarga());
        }
        if (!listaIdsLacres.isEmpty()) {
            lacreControleCargaService.removeByIds(listaIdsLacres);
        }


        //Mudança CQPRO00004777
        adiantamentoTrechoService.removeByIdControleCarga(idControleCarga);

        //LMS-3906
        carregamentoDescargaVolumeService.removeByIdControleCarga(idControleCarga);

        cc = findById(idControleCarga);
        cc.setMotivoCancelamentoCc(motivoCancelamentoCcService.findById(idMotivoCancelamentoCc));

        //Demanda LMS-768 Regra 1.30 do 05.01.01.03 Manter Controle de Cargas
        if (cc.getCarregamentoDescargas() != null) {
            List<CarregamentoDescarga> listCarregamentoDescargas = cc.getCarregamentoDescargas();
            if (listCarregamentoDescargas != null && !listCarregamentoDescargas.isEmpty()) {
                for (CarregamentoDescarga cd : listCarregamentoDescargas) {
                    cd.setTpStatusOperacao(new DomainValue(COLETA));

                    if (cd.getBox() != null) {
                        cd.getBox().setTpSituacaoBox(new DomainValue("L"));
                        if (cd.getBox().getDoca() != null) {
                            cd.getBox().getDoca().setTpSituacaoDoca(new DomainValue("L"));
                        }
                    }
                }
            }
        }

        enviarIntegracaoCancelamentoSMP(cc);

        store(cc);

        //Regra 1.23 do Manter Controle de Cargas
        if ("V".equals(cc.getTpControleCarga().getValue())) {
            MotoristaControleCarga motoristaControleCarga = motoristaControleCargaService.findMotoristaCcByIdControleCarga(idControleCarga);
            if (motoristaControleCarga != null && "E".equals(motoristaControleCarga.getMotorista().getTpVinculo().getValue())) {
                LiberacaoReguladora liberacaoReguladora = motoristaControleCarga.getLiberacaoReguladora();
                if (liberacaoReguladora != null) {
                    liberacaoReguladora.setDtVencimento(null);
                }
            }
        }


    }

    private void enviarIntegracaoCancelamentoSMP(ControleCarga cc) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        String versaoGerRisco = (String) conteudoParametroFilialService.findConteudoByNomeParametro(
                idFilialUsuario, ConstantesGerRisco.SGR_VERS_INT_GER_RIS, false);
        if (ConstantesGerRisco.SGR_GER_RIS_VERS_1.equals(versaoGerRisco) || versaoGerRisco == null) {
            solicMonitPreventivoService.updateStatusSMPByIdControleCarga(cc.getIdControleCarga(), ConstantesGerRisco.TP_STATUS_SMP_CANCELADA);
            cancelarSMPVersao1(cc);
        } else if (ConstantesGerRisco.SGR_GER_RIS_VERS_2.equals(versaoGerRisco)) {
            cancelarSMPVersao2(cc);
        } else {
          /* assim que todas as filiais estiverem na 2.0 esse trecho de código
             pode ser removido, pois não sera mais dado manutenção no
			 parametro filial */
            cancelarSMPVersao1(cc);
        }
    }

    private void cancelarSMPVersao2(ControleCarga cc) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        SolicMonitPreventivo smp = solicMonitPreventivoService.findByIdControleCargaAndFilial(cc.getIdControleCarga(), idFilialUsuario);
        if (smp != null) {
            gerarEnviarSMPService.generateCancelarSMP(cc.getIdControleCarga(), smp.getIdSolicMonitPreventivo());
        }

    }

    private void cancelarSMPVersao1(ControleCarga cc) {
        // Demanda LMS-3972
        AcaoIntegracao acao = acaoIntegracaoService.findByProcesso("PI LMS-SMPCANC");

        Long nrAgrupador = acaoIntegracaoEventosService.findLastAgrupador() + 1;

        if (acao != null) {
            AcaoIntegracaoEvento evento = new AcaoIntegracaoEvento();

            evento.setAcaoIntegracao(acao);
            evento.setNrDocumento(cc.getIdControleCarga());
            evento.setTpDocumento(acao.getTpDocumento());
            evento.setNrAgrupador(nrAgrupador);
            evento.setDsInformacao(acao.getDsProcessoIntegracao());
            evento.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());
            evento.setFilial(SessionUtils.getFilialSessao());

            acaoIntegracaoEventosService.store(evento);
        }
    }

    @SuppressWarnings("rawtypes")
    private void generateCancelamentoControleCargaDispositivos(Long idControleCarga) {
        List listaDispositivo = dispositivoUnitizacaoService.findByControleCarga(idControleCarga);
        for (Iterator iter = listaDispositivo.iterator(); iter.hasNext(); ) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();

            Long idDispositivo = tfm.getLong("idDispositivoUnitizacao");
            String tpManifesto = tfm.getString(TP_MANIFESTO_VALUE);
            String tpStatusManifesto = tfm.getString(TP_STATUS_MANIFESTO_VALUE);

            if ("PM".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDispositivo("PM", idDispositivo, tpManifesto);
            } else if ("EC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDispositivo("PM", idDispositivo, tpManifesto);
                generateEventoCancelamentoDispositivo("EC", idDispositivo, tpManifesto);
            } else if ("CC".equals(tpStatusManifesto)) {
                generateEventoCancelamentoDispositivo("PM", idDispositivo, tpManifesto);
                generateEventoCancelamentoDispositivo("EC", idDispositivo, tpManifesto);
                generateEventoCancelamentoDispositivo("CC", idDispositivo, tpManifesto);
            }
        }
    }

    private void generateEventoCancelamentoDispositivo(String tpStatusManifesto, Long idDispositivo, String tpManifesto) {
        Short cdEvento = returnCdEvento(tpStatusManifesto, tpManifesto);
        eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, cdEvento, "LM");
    }


    private void generateEventoCancelamentoVolume(String tpStatusManifesto, Long idVolume, String tpManifesto) {
        Short cdEvento = returnCdEvento(tpStatusManifesto, tpManifesto);
        eventoVolumeService.generateEventoVolume(idVolume, cdEvento, "LM");
    }

    private Short returnCdEvento(String tpStatusManifesto, String tpManifesto) {

        Short cdEvento = null;

        if ("PM".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_ENTREGA : ConstantesSim.EVENTO_RETIRADO_PRE_MANIFESTO_VIAGEM;
        } else if ("EC".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_ENTREGA : ConstantesSim.EVENTO_CANCELAMENTO_INICIO_CARREGAMENTO_VIAGEM;
        } else if ("CC".equals(tpStatusManifesto)) {
            cdEvento = "E".equals(tpManifesto) ? ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA : ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM;
        }

        return cdEvento;
    }


    /**
     * Soma valores dos Pagamentos Proprietario CC, onde o proprietário seja do tipo Mercúrio
     *
     * @param idControleCarga
     * @return
     */
    public BigDecimal getSomaValoresMercurioPagtoProprietarioCC(Long idControleCarga) {
        return getControleCargaDAO().getSomaValoresMercurioPagtoProprietarioCC(idControleCarga);
    }

    @SuppressWarnings("rawtypes")
    public List findTrechosDireto(Long idControleCarga) {
        return getControleCargaDAO().findTrechosDireto(idControleCarga);
    }

    /**
     * Usado para autenticação no sistema VOL, onde a senha é a hora/minuto da geração do controle de carga
     *
     * @param idTransportado identificador do meio de transporte
     * @param hrMin          hora e minuto da geração do controle de carga
     * @return ControleCarga
     * @author Marcelo Adamatti
     */
    public ControleCarga findControleCargaAtual(Long idTransportado, Long hrMin) {
        return getControleCargaDAO().findControleCargaAtual(idTransportado, hrMin);
    }

    /**
     * Verifica se o controle de carga em questao possui em sua rota a filial
     * do usuario logado.
     *
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean findControleCargaInControleTrecho(Long idControleCarga, Long idFilial) {
        ControleCarga controleCarga = this.findById(idControleCarga);

        for (Iterator iter = controleCarga.getControleTrechos().iterator(); iter.hasNext(); ) {
            ControleTrecho controleTrecho = (ControleTrecho) iter.next();
            if (idFilial.equals(controleTrecho.getFilialByIdFilialOrigem().getIdFilial())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que grava Evento de Controle Carga, chama a rotina 'Gerar Evento de Rastreabilidade',
     * chama evento 'Gerar e Enviar SMP para CEMOP-MZ e gerenciadora de Risco'.
     *
     * @param idControleCarga
     */
    private void generateEventosByEmitirControleCarga(Long idControleCarga) {
        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
        final ControleCarga controleCarga = findByIdInitLazyProperties(idControleCarga, false);
        // 1.1
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            final BigDecimal vlTotal = generateCalculaValorTotalMercadoriaControleCarga(idControleCarga, null, null, null, null);

            generateEventoChangeStatusControleCarga(idControleCarga,
                    SessionUtils.getFilialSessao().getIdFilial(), "EM", null,
                    controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(), null, null,
                    SessionUtils.getMoedaSessao().getIdMoeda(),
                    controleCarga.getPsTotalFrota(), controleCarga.getPsTotalAforado(),
                    vlTotal,
                    controleCarga.getPcOcupacaoCalculado(),
                    controleCarga.getPcOcupacaoAforadoCalculado(),
                    controleCarga.getPcOcupacaoInformado());


            // Regra 1.2 da ET 05.01.01.02 - Emitir Controle de Cargas Viagem
            EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
            eventoMeioTransporte.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
            eventoMeioTransporte.setControleCarga(controleCarga);
            eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue("AGSA"));
            eventoMeioTransporte.setDhGeracao(dhAtual);
            eventoMeioTransporte.setDhInicioEvento(dhAtual);
            eventoMeioTransporte.setFilial(SessionUtils.getFilialSessao());
            eventoMeioTransporte.setUsuario(SessionUtils.getUsuarioLogado());
            eventoMeioTransporteService.generateEvent(eventoMeioTransporte);
        }

        // Regra 1.3 da ET 05.01.01.02 - Emitir Controle de Cargas Viagem
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            EventoMeioTransporte eventoSemiReboque = new EventoMeioTransporte();
            eventoSemiReboque.setMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado());
            eventoSemiReboque.setControleCarga(controleCarga);
            eventoSemiReboque.setTpSituacaoMeioTransporte(new DomainValue("AGSA"));
            eventoSemiReboque.setDhGeracao(dhAtual);
            eventoSemiReboque.setDhInicioEvento(dhAtual);
            eventoSemiReboque.setFilial(SessionUtils.getFilialSessao());
            eventoSemiReboque.setUsuario(SessionUtils.getUsuarioLogado());
            eventoMeioTransporteService.generateEvent(eventoSemiReboque);
        }


        enviarIntegracaoSmp(controleCarga);

        List<Manifesto> listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null,
                null, null);
        for (Manifesto manifesto : listManifestos) {
            if (!"FE".equals(manifesto.getTpStatusManifesto().getValue())
                    && !"CA".equals(manifesto.getTpStatusManifesto().getValue())) {
                this.generateAcaoIntegracaoEvento(manifesto.getIdManifesto(), "PI LMS-FRMS");
                this.generateAcaoIntegracaoEvento(manifesto.getIdManifesto(), "PI LMS-FRMSPROT");
            }
        }

        // LMS-5002
        generateAcaoIntegracaoEventoMT(idControleCarga);

    }

    /**
     * LMS-5002 - Gerar um EDI fiscal por controle de cargas para MT
     *
     * @param idControleCarga
     */
    private void generateAcaoIntegracaoEventoMT(Long idControleCarga) {
        String dsProcessoIntegracao = "PI LMS-FRMT";

        AcaoIntegracao acaoIntegracao = acaoIntegracaoService.findByProcesso(dsProcessoIntegracao);

        if (acaoIntegracao != null) {
            AcaoIntegracaoEvento evento = new AcaoIntegracaoEvento();

            evento.setNrDocumento(idControleCarga);
            evento.setTpDocumento(new DomainValue("CCA"));
            evento.setAcaoIntegracao(acaoIntegracao);
            evento.setDsInformacao(dsProcessoIntegracao);
            evento.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());
            evento.setNrAgrupador(acaoIntegracaoEventosService.findLastAgrupador() + 1);
            evento.setFilial(SessionUtils.getFilialSessao());

            acaoIntegracaoEventosService.store(evento);
        }
    }


    /**
     * LMS-4154 Consultar a tabela ACAO_INTEGRACAO_EVENTO, filtrando pelo
     * controle de carga (ACAO_INTEGRACAO_EVENTO.NR_DOCUMENTO = <CONTROLE_CARGA.ID_CONTROLE_CARGA>) e
     * filial logada (ACAO_INTEGRACAO_EVENTO.ID_FILIAL = <filial logada>).
     *
     * @param idControleCarga
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Boolean validateGeracaoAcaoIntegracaoEventoParaControleCarga(Long idControleCarga) {
        Map criteria = new HashMap();
        criteria.put("nrDocumento", idControleCarga);
        criteria.put("filial.idFilial", SessionUtils.getFilialSessao().getIdFilial());
        List eventos = acaoIntegracaoEventosService.find(criteria);
        return eventos.isEmpty();
    }

    /**
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap generateDataControleCargaByEmissao(Long idControleCarga) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        final ControleCarga controleCarga = findByIdInitLazyProperties(idControleCarga, false);

        generateEventosByEmitirControleCarga(idControleCarga);

        //Verifica se a Filial Origem do Controle de Carga é igual a Filial do usuario logado.
        if (controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario)) {
            // 1.20
            reciboPostoPassagemService.generateReciboPostoPassagem(idControleCarga);
        }

        if (!"EV".equals(controleCarga.getTpRotaViagem().getValue())) {
            generateEventoPrevisaoSaidaParaDoctoServico(idControleCarga, SessionUtils.getFilialSessao().getIdFilial());
        }

        if ("V".equals(controleCarga.getTpControleCarga().getValue())) {
            controleCarga.setNrManif(manifestoService.findManifestosByIdControleCarga(idControleCarga));
            this.store(controleCarga);
        }

        return new TypedFlatMap();
    }

    /**
     * Valida a geracao da emisao do controle de carga. E caso de uma businessException,
     * emula a geracao de uma excecao via typedFlatMap.
     *
     * @param idControleCarga
     * @param tpStatusControleCarga
     * @param sgAcao
     * @param blRegistroAuditoria
     * @param idMeioTransporteTransportado
     * @param idMotorista
     * @param idFilialAtualizaStatus
     * @param pcOcupacaoInformado
     */
    @SuppressWarnings("unchecked")
    public TypedFlatMap generateValidateEmissaoControleCarga(Long idControleCarga, String tpStatusControleCarga,
                                                             String sgAcao, Boolean blRegistroAuditoria,
                                                             Long idMeioTransporteTransportado, Long idMotorista,
                                                             Long idFilialAtualizaStatus, BigDecimal pcOcupacaoInformado,
                                                             Long idFilial,
                                                             String tpControleCarga, boolean contingenciaConfirmada) {

        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

        if (blRegistroAuditoria == null) {
            executePreValidacoesControleCarga(idControleCarga, tpStatusControleCarga,
                    sgAcao, idMeioTransporteTransportado, idMotorista,
                    idFilialAtualizaStatus, idFilialUsuario);
        }

		/* PROCESSO DE ENQUADRAMENTOS E WORKFLOWS PGR */
        PlanoGerenciamentoRiscoDTO planoPGR = new PlanoGerenciamentoRiscoDTO();
        if (planoUtils.findParametroFilial(PA_SGR_VAL_EXIST_PGR)) {
            planoPGR = planoGerenciamentoRiscoService.executeVerificarEnquadramentoRegra(idControleCarga);
            planoGerenciamentoRiscoService.generateExigenciasGerRisco(planoPGR);
            TypedFlatMap mapRetornoPGR = validateEnquadramentosEWorkflows(planoPGR);

            if (!mapRetornoPGR.isEmpty()) {
                return mapRetornoPGR;
            }
        }
        generateSMP(idControleCarga, idFilialUsuario, planoPGR);

        //lms-3544
        boolean mdfe = "EM".equals(sgAcao) && "V".equals(tpControleCarga) && isFilialMdfe(idFilialUsuario) && hasConhecimentoControleCarga(idControleCarga)
                && manifestoEletronicoService.validaGeracaoMDFEManifestoAereo(idControleCarga);

        List<ManifestoEletronico> rejeitados = new ArrayList<ManifestoEletronico>();
        boolean executarGerarMDFEContigencia = false;
        if (mdfe) {
            TypedFlatMap validaEmissaoMdfe = manifestoEletronicoService.validaEmissaoMdfe(idControleCarga, contingenciaConfirmada);
            mdfe = !Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(PULAR_GERACAO_MDFE));
            if (Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(IMPRIME_MDFE_AUTORIZADO))
                    || Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(ENCERRAR_MDFES_AUTORIZADOS))
                    || Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(HAS_CONTINGENCIA))) {
                return validaEmissaoMdfe;
            }
            rejeitados = validaEmissaoMdfe.getList(REJEITADOS);
            executarGerarMDFEContigencia = Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(EXECUTAR_GERAR_MDFE_CONTIGENCIA));
        }

        if ("EM".equals(sgAcao)) {
            atualizaControleCargaByValidateEmissaoControleCarga(idControleCarga, pcOcupacaoInformado);
        }

        if (mdfe) {
            if (executarGerarMDFEContigencia) {
                return manifestoEletronicoService.gerarMDFEContingencia(idControleCarga, true);
            } else {
                return manifestoEletronicoService.gerarMDFEViagem(idControleCarga, rejeitados);
            }
        }


        return null;
    }

    @SuppressWarnings("rawtypes")
    private void executePreValidacoesControleCarga(Long idControleCarga, String tpStatusControleCarga, String sgAcao,
                                                   Long idMeioTransporteTransportado, Long idMotorista, Long idFilialAtualizaStatus, Long idFilialUsuario) {
        if (idMeioTransporteTransportado == null) {
            throw new BusinessException("LMS-05060");
        }
        if (idMotorista == null) {
            throw new BusinessException("LMS-05061");
        }

        if ("EM".equals(sgAcao) && !(idFilialAtualizaStatus.equals(idFilialUsuario) && "PO".equals(tpStatusControleCarga))) {
            throw new BusinessException("LMS-05029");
        }

        List listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (Iterator iter = listManifestos.iterator(); iter.hasNext(); ) {
            Manifesto manifesto = (Manifesto) iter.next();
            if (manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario) && manifesto.getDhEmissaoManifesto() == null) {
                throw new BusinessException("LMS-05030");
            }
        }
    }

    /**
     * Retorna um TypedFlatMap com as mensagens de enquadramento ou workflow se existirem
     *
     * @param planoPGR
     * @return
     */
    public TypedFlatMap validateEnquadramentosEWorkflows(PlanoGerenciamentoRiscoDTO planoPGR) {
        TypedFlatMap retornoPGR = new TypedFlatMap();

		/*inicializa map retorno*/
        retornoPGR.put(MENSAGENS_ENQUADRAMENTO, "");
        retornoPGR.put(MENSAGENS_WORKFLOW_REGRAS, "");
        retornoPGR.put(MENSAGENS_WORKFLOW_EXIGENCIAS, "");

		/*Mensagens bloqueio*/
        if (!CollectionUtils.isEmpty(planoPGR.getMensagensEnquadramento())) {
            retornoPGR.put(MENSAGENS_ENQUADRAMENTO, planoUtils.join(planoPGR.getMensagensEnquadramento()));
            return retornoPGR;
        }

		/*workflow regras*/
        if (planoGerenciamentoRiscoService.validateDeveGerarWorkflowRegras(planoPGR)
                && !CollectionUtils.isEmpty(planoPGR.getMensagensWorkflowRegras())) {
            retornoPGR.put(MENSAGENS_WORKFLOW_REGRAS, planoUtils.join(planoPGR.getMensagensWorkflowRegras()));
        }

		/*workflow exigências*/
        validateWorkflowExigenciaPGR(planoPGR);
        if (planoGerenciamentoRiscoService.validateDeveGerarWorkflowExigencias(planoPGR)
                && !CollectionUtils.isEmpty(planoPGR.getMensagensWorkflowExigencias())) {
            retornoPGR.put(MENSAGENS_WORKFLOW_EXIGENCIAS, planoUtils.join(planoPGR.getMensagensWorkflowExigencias()));
        }

        //zera as mensagens de regras se tiver workflow de exigencias aprovado
        if (!planoPGR.isBlGerarWorkflowExigencias()) {
            retornoPGR.put(MENSAGENS_WORKFLOW_REGRAS, "");
        }

        if ("".equals(retornoPGR.get(MENSAGENS_ENQUADRAMENTO))
                && "".equals(retornoPGR.get(MENSAGENS_WORKFLOW_REGRAS))
                && "".equals(retornoPGR.get(MENSAGENS_WORKFLOW_EXIGENCIAS))) {
            return new TypedFlatMap();
        }

        return retornoPGR;
    }

    private void validateWorkflowExigenciaPGR(PlanoGerenciamentoRiscoDTO planoPGR) {

        if (!CollectionUtils.isEmpty(planoPGR.getExigencias())) {
            planoPGR.setBlValidaExigenciasMonitoramento(planoUtils.findParametroFilial("SGR_VAL_EXIG_MONITOR"));
            planoPGR.setBlValidaExigenciasVeiculo(planoUtils.findParametroFilial("SGR_VAL_EXIG_VEICULO"));
            planoPGR.setBlValidaExigenciasMotorista(planoUtils.findParametroFilial("SGR_VAL_EXIG_MOTORIS"));
            planoPGR.setBlValidaExigenciasVirus(planoUtils.findParametroFilial("SGR_VAL_EXIG_VIRUS"));
            ControleCarga controleCarga = findById(planoPGR.getIdControleCarga());
            for (TipoExigenciaGerRisco tipo : planoPGR.getExigenciasTipo().keySet()) {
                if (BooleanUtils.isTrue(tipo.getBlTravaSistema())) {
                    String tpExigencia = tipo.getTpExigencia().getValue();
                    Collection<ExigenciaGerRiscoDTO> exigencias = planoPGR.getExigenciasTipo(tipo);
                    if (ConstantesGerRisco.TP_EXIGENCIA_MONITORAMENTO.equals(tpExigencia)) {
                        validateExigenciasMonitoramento(planoPGR, exigencias, controleCarga);
                    } else if (ConstantesGerRisco.TP_EXIGENCIA_MOTORISTA.equals(tpExigencia)) {
                        validateExigenciasMotorista(planoPGR, exigencias, controleCarga);
                    } else if (ConstantesGerRisco.TP_EXIGENCIA_VEICULO.equals(tpExigencia)) {
                        validateExigenciasVeiculo(planoPGR, exigencias, controleCarga);
                    } else if (ConstantesGerRisco.TP_EXIGENCIA_VIRUS.equals(tpExigencia)) {
                        validateExigenciasVirus(planoPGR, exigencias, controleCarga);
                    }
                }
            }
        }
    }

    private void validateExigenciasVirus(PlanoGerenciamentoRiscoDTO planoPGR, Collection<ExigenciaGerRiscoDTO> exigencias,
                                         ControleCarga controleCarga) {
        if (planoPGR.isBlValidaExigenciasVirus()) {
            int qtExistente = virusCargaService.getRowCountByControleCarga(controleCarga.getIdControleCarga());
            int qtExigida = 0;
            for (ExigenciaGerRiscoDTO exigenciaGerRiscoDTO : exigencias) {
                qtExigida += exigenciaGerRiscoDTO.getQtExigida();
            }
            if (qtExigida > qtExistente) {
                addMensagemExigenciaVirus(planoPGR, exigencias.iterator().next(), qtExigida, qtExistente);
            }
        }
    }

    /**
     * para cada exigencia encontrar o periferico de exigencia na listagem de meio de transporte da carga
     *
     * @param planoPGR
     * @param exigencias
     */
    @SuppressWarnings("rawtypes")
    private void validateExigenciasVeiculo(PlanoGerenciamentoRiscoDTO planoPGR,
                                           Collection<ExigenciaGerRiscoDTO> exigencias, ControleCarga controleCarga) {
        if (planoPGR.isBlValidaExigenciasVeiculo()) {
            List<MeioTransportePeriferico> listMeioTransportePeriferico = montarListaPerifericoMeiosTransporte(controleCarga);
            Set<PerifericoRastreador> perifericoRastreadores = montarListaPerifericosExigencia(exigencias);
            ExigenciaGerRiscoDTO exigenciaDto = (ExigenciaGerRiscoDTO) ((List) exigencias).get(0);
            Boolean blRestrito = exigenciaDto.getExigenciaGerRisco().getTipoExigenciaGerRisco().getBlRestrito();
            for (PerifericoRastreador perifericoRastreador : perifericoRastreadores) {
                if (!findRatreadorMeioTRansportePeriferico(listMeioTransportePeriferico,
                        perifericoRastreador.getIdPerifericoRastreador())) {
                    addMensagemPerifericoRastreador(planoPGR, perifericoRastreador, blRestrito, "LMS-11336");
                }
            }
        }
    }

    private Set<PerifericoRastreador> montarListaPerifericosExigencia(Collection<ExigenciaGerRiscoDTO> exigencias) {
        Set<PerifericoRastreador> perifericoRastreadores = new HashSet<PerifericoRastreador>();
        for (ExigenciaGerRiscoDTO exigencia : exigencias) {
            Long idExigenciaGerRisco = exigencia.getExigenciaGerRisco().getIdExigenciaGerRisco();
            List<PerifericoRastreador> perifericos = perifericoRastreadorService
                    .findPerifericosRastreador(idExigenciaGerRisco);
            for (PerifericoRastreador perifericoRastreador : perifericos) {
                perifericoRastreadores.add(perifericoRastreador);
            }
        }
        return perifericoRastreadores;
    }

    /**
     * busca a listagem de meio de transporte da carga para encontrar os perifericos
     *
     * @param controleCarga
     * @return
     */
    private List<MeioTransportePeriferico> montarListaPerifericoMeiosTransporte(ControleCarga controleCarga) {
        List<MeioTransportePeriferico> listMeioTransportePeriferico = new ArrayList<MeioTransportePeriferico>();
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            listMeioTransportePeriferico.addAll(controleCarga.getMeioTransporteByIdSemiRebocado().getMeioTransportePerifericos());
        }
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            listMeioTransportePeriferico.addAll(controleCarga.getMeioTransporteByIdTransportado().getMeioTransportePerifericos());
        }
        return listMeioTransportePeriferico;
    }

    /**
     * verifica se o periferico da exigencia está na listagem de meio de transporte da carga
     *
     * @param listMeioTransportePeriferico
     * @param idPerifericoRastreador
     * @return
     */
    private Boolean findRatreadorMeioTRansportePeriferico(List<MeioTransportePeriferico> listMeioTransportePeriferico, Long idPerifericoRastreador) {
        for (MeioTransportePeriferico meioTransportePeriferico : listMeioTransportePeriferico) {
            if (meioTransportePeriferico.getPerifericoRastreador().getIdPerifericoRastreador().equals(idPerifericoRastreador)) {
                return true;
            }
        }
        return false;
    }

    /**
     * verifica se o motorista possui liberacao reguladora e
     * tem o vinculo correto para a devida exigencia solicitada
     *
     * @param planoPGR
     * @param exigencias
     */
    private void validateExigenciasMotorista(PlanoGerenciamentoRiscoDTO planoPGR,
                                             Collection<ExigenciaGerRiscoDTO> exigencias, ControleCarga controleCarga) {
        if (planoPGR.isBlValidaExigenciasMotorista()) {
            for (ExigenciaGerRiscoDTO exigencia : exigencias) {
                if (exigencia.getExigenciaGerRisco() != null
                        && exigencia.getExigenciaGerRisco().getCdExigenciaGerRisco() != null) {
                    String cdExigencia = exigencia.getExigenciaGerRisco().getCdExigenciaGerRisco();
                    Motorista motorista = controleCarga.getMotorista();
                    verificarLiberacaoVinculo(planoPGR, exigencia, cdExigencia, motorista, controleCarga);
                }
            }

        }
    }

    /**
     * verifica se o motorista possui liberacao reguladora e
     * tem o vinculo correto para a devida exigencia solicitada
     *
     * @param planoPGR
     * @param exigencia
     * @param cdExigencia
     * @param motorista
     * @param controleCarga
     */
    private void verificarLiberacaoVinculo(PlanoGerenciamentoRiscoDTO planoPGR, ExigenciaGerRiscoDTO exigencia,
                                           String cdExigencia, Motorista motorista, ControleCarga controleCarga) {

        Boolean exigenciaViolada = verificarVinculoMotorista(cdExigencia, motorista);

        if (exigenciaViolada) {
            addMensagemExigencia(planoPGR, exigencia, LMS_11348);
        }

    }

    private Boolean verificarVinculoMotorista(String cdExigencia, Motorista motorista) {
        Boolean exigenciaViolada = Boolean.FALSE;
        String tpVinculo = motorista.getTpVinculo().getValue();
        Boolean vinculoFuncionario = tpVinculo.equals(VINCULO_FUNCIONARIO);
        Boolean vinculoAgregado = vinculoFuncionario || tpVinculo.equals(VINCULO_AGREGADO);
        Boolean vinculoMotoristaEventual = vinculoAgregado || tpVinculo.equals(VINCULO_EVENTUAL);

        if (cdExigencia.equals(MOTORISTA_FUNCIONARIO) && !vinculoFuncionario) {
            exigenciaViolada = Boolean.TRUE;
        } else if (cdExigencia.equals(MOTORISTA_AGREGADO) && !vinculoAgregado) {
            exigenciaViolada = Boolean.TRUE;
        } else if (cdExigencia.equals(MOTORISTA_EVENTUAL) && !vinculoMotoristaEventual) {
            exigenciaViolada = Boolean.TRUE;
        }

        return exigenciaViolada;
    }

    @SuppressWarnings("unused")
    private void validateExigenciasMonitoramento(PlanoGerenciamentoRiscoDTO planoPGR, Collection<ExigenciaGerRiscoDTO> exigencias, ControleCarga controleCarga) {
        Long idControleCarga = planoPGR.getIdControleCarga();
        if (planoPGR.isBlValidaExigenciasMonitoramento()) {
            for (ExigenciaGerRiscoDTO exigencia : exigencias) {
                String cdExigencia = exigencia.getExigenciaGerRisco().getCdExigenciaGerRisco();
                addMensagensMonitoramentoNecessarias(planoPGR, controleCarga, exigencia, cdExigencia);
            }
        }
    }

    private void addMensagensMonitoramentoNecessarias(PlanoGerenciamentoRiscoDTO planoPGR, ControleCarga controleCarga,
                                                      ExigenciaGerRiscoDTO exigencia, String cdExigencia) {
        if (isMensagensMonitoramentoNecessarias(controleCarga, cdExigencia)) {
            addMensagemExigenciaMonitoramento(planoPGR, exigencia);
        }
    }

    private boolean isMensagensMonitoramentoNecessarias(ControleCarga controleCarga, String cdExigencia) {
        Long idControleCarga = controleCarga.getIdControleCarga();
        if (MONIT_RAST_VEICULO_PRINC.equals(cdExigencia)) {
            // rastreador veículo principal
            return !isMonitoradoIdTransportado(idControleCarga);
        }
        if (MONIT_RAST_VEICULO_PRINC_REBOQ.equals(cdExigencia)) {
            // rastreador veículo principal E semi-reboque
            return !isMonitoradoIdTransportado(idControleCarga) || !isMonitoradoSemiRebocado(idControleCarga);
        }
        if (MONIT_RAST_PRINC_REBOQ_CONT.equals(cdExigencia)) {
            // rastreador veículo principal E (semi-reboque OU rastreador contingência)
            return !isMonitoradoIdTransportado(idControleCarga)
                    || (!isMonitoradoSemiRebocado(idControleCarga) && !temRastreadorContingencia(controleCarga));
        }
        if (MONIT_RAST_PRINC_REBOQ_SEG_TEC.equals(cdExigencia)) {
            // rastreamento veículo principal E segunda tecnologia
            return !isMonitoradoIdTransportado(idControleCarga)
                    || (!isMonitoradoSemiRebocado(idControleCarga)
                    && !temRastreadorContingencia(controleCarga)
                    && virusCargaService.getRowCountByControleCarga(idControleCarga) == 0);
        }
        return false;
    }

    private void addMensagemExigencia(PlanoGerenciamentoRiscoDTO planoPGR, ExigenciaGerRiscoDTO exigencia, String chaveNaoRestrito) {
        planoPGR.setBlGerarWorkflowExigencias(true);
        if (exigencia.getExigenciaGerRisco().getTipoExigenciaGerRisco().getBlRestrito()) {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem(LMS_11335));
        } else {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem(chaveNaoRestrito,
                    new Object[]{exigencia.getExigenciaGerRisco().getDsResumida()}));
        }
    }


    private void addMensagemPerifericoRastreador(PlanoGerenciamentoRiscoDTO planoPGR,
                                                 PerifericoRastreador perifericoRastreador, Boolean blRestrito, String chaveNaoRestrito) {
        planoPGR.setBlGerarWorkflowExigencias(true);
        if (blRestrito) {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem(LMS_11335));
        } else {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem(chaveNaoRestrito,
                    new Object[]{perifericoRastreador.getDsPerifericoRastreador()}));
        }
    }


    private void addMensagemExigenciaMonitoramento(PlanoGerenciamentoRiscoDTO planoPGR, ExigenciaGerRiscoDTO exigencia) {
        addMensagemExigencia(planoPGR, exigencia, "LMS-11337");
    }

    private void addMensagemExigenciaVirus(PlanoGerenciamentoRiscoDTO planoPGR, ExigenciaGerRiscoDTO exigencia,
                                           int qtExigida, int qtExistente) {
        planoPGR.setBlGerarWorkflowExigencias(true);
        if (exigencia.getExigenciaGerRisco().getTipoExigenciaGerRisco().getBlRestrito()) {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem(LMS_11335));
        } else {
            planoPGR.addMensagemWorkflowExigencias(configuracoesFacade.getMensagem("LMS-11353",
                    new Object[]{String.valueOf(qtExigida), String.valueOf(qtExistente)}));
        }
    }

    private boolean temRastreadorContingencia(ControleCarga controleCarga) {
        MeioTransporte meioTransporteByIdSemiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado();
        if (meioTransporteByIdSemiRebocado != null) {
            List<MeioTransportePeriferico> listPerifericosSemiRebocado = meioTransporteByIdSemiRebocado.getMeioTransportePerifericos();
            List<Long> listIdsPerifericos = new ArrayList<Long>();
            for (MeioTransportePeriferico meioTransportePeriferico : listPerifericosSemiRebocado) {
                listIdsPerifericos.add(meioTransportePeriferico.getPerifericoRastreador().getIdPerifericoRastreador());
            }
            if (!CollectionUtils.isEmpty(listPerifericosSemiRebocado)) {
                return verifyPerifericos(listIdsPerifericos);
            }
        }
        return false;
    }

    private void validateWorkflowExigenciaCarregamento(PlanoGerenciamentoRiscoDTO planoPGR) {

        if (!CollectionUtils.isEmpty(planoPGR.getExigencias())) {
            planoPGR.setBlValidaExigenciasVirus(planoUtils.findParametroFilial("SGR_VAL_EXIG_VIRUS"));
            ControleCarga controleCarga = findById(planoPGR.getIdControleCarga());
            for (TipoExigenciaGerRisco tipo : planoPGR.getExigenciasTipo().keySet()) {
                if (BooleanUtils.isTrue(tipo.getBlTravaSistema())) {
                    String tpExigencia = tipo.getTpExigencia().getValue();
                    Collection<ExigenciaGerRiscoDTO> exigencias = planoPGR.getExigenciasTipo(tipo);
                    if (ConstantesGerRisco.TP_EXIGENCIA_VIRUS.equals(tpExigencia)) {
                        validateExigenciasVirus(planoPGR, exigencias, controleCarga);
                    }
                }
            }
        }
    }

    public void validateExigenciasFinalizarCarregamento(PlanoGerenciamentoRiscoDTO plano) {
        validateWorkflowExigenciaCarregamento(plano);
    }

    /**
     * Retorna se, dada uma lista de IDs de perifericos, existe o mesmo
     * id de periferico no parâmetro geral especificado
     *
     * @param listPerifericosMeioTransporte
     * @return
     */
    private boolean verifyPerifericos(List<Long> listPerifericosMeioTransporte) {
        String perifericosParamGeral = (String) configuracoesFacade.getValorParametro(SGR_PERIFERICOS_RAST_CONTING);
        List<Long> listPerifericosParamGeral = new ArrayList<Long>();
        for (String s : perifericosParamGeral.split("\\D+")) {
            if (s.length() > 0) {
                listPerifericosParamGeral.add(Long.valueOf(s));
            }
        }
        return CollectionUtils.containsAny(listPerifericosMeioTransporte, listPerifericosParamGeral);
    }

    public boolean isMonitoradoIdTransportado(Long idControleCarga) {
        return getControleCargaDAO().findBlMonitoramentoByMeioTranspIdTransportadoRodoviario(idControleCarga);
    }

    public boolean isMonitoradoSemiRebocado(Long idControleCarga) {
        return getControleCargaDAO().findBlMonitoramentoByMeioTranspIdSemiRebocadoRodoviario(idControleCarga);
    }

    public List<Object[]> findTrechosOrigemDestino(Long idControleCarga, Long idFilialLogada) {
        return getControleCargaDAO().findTrechosOrigemDestino(idControleCarga, idFilialLogada);
    }

    public List<Object[]> findCargaTrechosDestino(Long idControleCarga, Long idFilial) {
        return getControleCargaDAO().findCargaTrechosDestino(idControleCarga, idFilial);
    }

    public List<Object[]> findTrechosDestinoColetaEntrega(Long idFilial) {
        return getControleCargaDAO().findTrechosDestinoColetaEntrega(idFilial);
    }


    public List<Object[]> findTrechosDestinoColetaEntregaCargas(Long idControleCarga) {
        return getControleCargaDAO().findTrechosDestinoColetaEntregaCargas(idControleCarga);
    }

    /**
     * Geração do MDFe fora da emissão do controle de carga
     *
     * @param idControleCarga
     */
    public TypedFlatMap generateMdfe(Long idControleCarga, boolean contingenciaConfirmada) {
        return generateMdfe(idControleCarga, contingenciaConfirmada, false);
    }

    @SuppressWarnings({ "unchecked", "unused" })
    public TypedFlatMap generateMdfe(Long idControleCarga, boolean contingenciaConfirmada, boolean isViagem) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

        List<ManifestoEletronico> rejeitados = new ArrayList<ManifestoEletronico>();
        TypedFlatMap validaEmissaoMdfe = manifestoEletronicoService.validaEmissaoMdfe(idControleCarga, contingenciaConfirmada);

        if (Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(PULAR_GERACAO_MDFE))) {
            return new TypedFlatMap();
        }

        if (Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(IMPRIME_MDFE_AUTORIZADO)) || Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(ENCERRAR_MDFES_AUTORIZADOS)) || Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(HAS_CONTINGENCIA))) {
            return validaEmissaoMdfe;
        }

        if (Boolean.TRUE.equals(validaEmissaoMdfe.getBoolean(EXECUTAR_GERAR_MDFE_CONTIGENCIA))) {
            return manifestoEletronicoService.gerarMDFEContingencia(idControleCarga, isViagem);
        }

        rejeitados = validaEmissaoMdfe.getList(REJEITADOS);

        if (isViagem) {
            return manifestoEletronicoService.gerarMDFEViagem(idControleCarga, rejeitados);
        } else {
            return manifestoEletronicoService.gerarMDFE(idControleCarga, rejeitados);
        }
    }

    public boolean hasManifestoAereoControleCarga(Long idControleCarga) {
        Integer count = getControleCargaDAO().countManifestoAereoControleCarga(idControleCarga);
        return count != null && count > 0;
    }

    public boolean isFilialMdfe(Long idFilialUsuario) {
        String indicadorMdfe = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "INDICADOR_MDFE", false);
        return "S".equals(indicadorMdfe);
    }

    public boolean isFilialMdfeE(Long idFilialUsuario) {
        String indicadorMdfe = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "INDICADOR_MDFE_E", false);
        return "S".equals(indicadorMdfe);
    }

    public TypedFlatMap executeEncerrarMdfesAutorizados(Long idControleCarga) {
        return manifestoEletronicoService.encerrarMdfesAutorizados(idControleCarga);
    }

    public TypedFlatMap cancelarMdfe(Long idManifestoEletronico) {
        ManifestoEletronico mdfe = (ManifestoEletronico) manifestoEletronicoService.findById(idManifestoEletronico);
        return manifestoEletronicoService.cancelarMdfe(mdfe, true);
    }

    /**
     * Atualiza os valores do Controle de Carga
     */
    private void atualizaControleCargaByValidateEmissaoControleCarga(Long idControleCarga, BigDecimal pcOcupacaoInformado) {
        ControleCarga controleCarga = findByIdInitLazyProperties(idControleCarga, false);
        controleCarga.setPcOcupacaoInformado(BigDecimalUtils.HUNDRED.subtract(pcOcupacaoInformado));

        BigDecimal nrCapacidadeKg = controleCarga.getMeioTransporteByIdTransportado().getNrCapacidadeKg();
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            nrCapacidadeKg = controleCarga.getMeioTransporteByIdSemiRebocado().getNrCapacidadeKg();
        }
        BigDecimal psTotal = findPsTotalFromManifestoByIdControleCarga(idControleCarga);
        BigDecimal psTotalAforado = findPsTotalAforadoFromManifestoByIdControleCarga(idControleCarga);

        controleCarga.setPsTotalFrota(psTotal);
        controleCarga.setPsTotalAforado(psTotalAforado);
        controleCarga.setPcOcupacaoCalculado(executeCalculoPcOcupacao(psTotal, nrCapacidadeKg));
        controleCarga.setPcOcupacaoAforadoCalculado(executeCalculoPcOcupacaoAforado(psTotalAforado, nrCapacidadeKg));
        controleCarga.setVlTotalFrota(generateCalculaValorTotalMercadoriaControleCarga(idControleCarga, null, null, null, null));
        controleCarga.setMoeda(SessionUtils.getMoedaSessao());
        store(controleCarga);
    }


    /**
     * Verifica se para a data/hora informada por parâmetro pode ser utilizada a Rota Informada
     *
     * @param dhTesteRota        (required)
     * @param idRotaIdaVolta     (required)
     * @param trechoRotaIdaVolta (required)
     * @param blGeraExcecao      (required) Indica  se deve ou não mostrar mensagem de exceção
     * @return True, se para a data/hora informada pode ser utilizada a Rota informada. Caso contrário, False.
     */
    private Boolean validateDataHoraRotaControleCarga(DateTime dhTesteRota, Long idRotaIdaVolta, TrechoRotaIdaVolta trechoRotaIdaVolta, Boolean blGeraExcecao, DateTime dataLimite) {
        int diffDatas = getDiffDates(dhTesteRota, dataLimite);
        YearMonthDay dtTesteRota = dhTesteRota.toYearMonthDay();


        // Regra 1
        boolean blRegra1 = true;
        int dia = JTDateTimeUtils.getNroDiaSemana(dtTesteRota);
        switch (dia) {
            case DateTimeConstants.MONDAY:
                if (!trechoRotaIdaVolta.getBlSegunda()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.TUESDAY:
                if (!trechoRotaIdaVolta.getBlTerca()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.WEDNESDAY:
                if (!trechoRotaIdaVolta.getBlQuarta()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.THURSDAY:
                if (!trechoRotaIdaVolta.getBlQuinta()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.FRIDAY:
                if (!trechoRotaIdaVolta.getBlSexta()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.SATURDAY:
                if (!trechoRotaIdaVolta.getBlSabado()) {
                    blRegra1 = false;
                }
                break;
            case DateTimeConstants.SUNDAY:
                if (!trechoRotaIdaVolta.getBlDomingo()) {
                    blRegra1 = false;
                }
                break;
            default:
                break;
        }
        if (!blRegra1) {
            if (blGeraExcecao) {
                throw new BusinessException(LMS_05127);
            } else {
                return Boolean.FALSE;
            }
        }

        if (dataLimite != null) {
            int diaLimite = JTDateTimeUtils.getNroDiaSemana(dataLimite.toYearMonthDay());
            if (diffDatas > 0 || (dia == diaLimite && trechoRotaIdaVolta.getHrSaida().compareTo(dataLimite.toTimeOfDay()) > 0)) {
                throw new BusinessException(LMS_05127);
            }
        }

        // Regra 2
        boolean blRegra2 = dhTesteRota.toTimeOfDay().compareTo(trechoRotaIdaVolta.getHrSaida()) <= 0;
        if (!blRegra2) {
            if (blGeraExcecao) {
                throw new BusinessException(LMS_05127);
            } else {
                return Boolean.FALSE;
            }
        }


        // Regra 3
        boolean blRegra3 = getControleCargaDAO().validateReciboInMeioTransporteControleCarga(idRotaIdaVolta, dtTesteRota);
        if (!blRegra3) {
            if (blGeraExcecao) {
                throw new BusinessException("LMS-05109");
            } else {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    private int getDiffDates(DateTime dhTesteRota, DateTime dataLimite) {
        if (dhTesteRota != null && dataLimite != null) {
            return dhTesteRota.compareTo(dataLimite);
        }
        return 0;
    }

    /**
     * Verifica se a Rota Ida Volta pode ser utilizada no Controle de Carga e para qual data ela será atribuída.
     *
     * @param idRotaIdaVolta
     * @param horasTempoLimite
     * @return
     */
    public YearMonthDay validateRotaControleCarga(Long idRotaIdaVolta, int horasTempoLimite) {
        if (horasTempoLimite > HORA_LIMITE) {
            throw new BusinessException("LMS-05108");
        }
        final TrechoRotaIdaVolta trechoRotaIdaVolta = trechoRotaIdaVoltaService.findByTrechoRotaCompleta(idRotaIdaVolta);

        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();

        DateTime dataAtualSemHora = JTDateTimeUtils.getFirstHourOfDay(dhAtual);
        YearMonthDay dtTesteRota = dataAtualSemHora.toYearMonthDay();
        int horasSomar = getHorasSomarByDiaSemana(JTDateTimeUtils.getNroDiaSemana(dtTesteRota));
        DateTime dataLimite = dataAtualSemHora.plusHours(horasSomar);
        if (horasSomar == 0) {
            dataLimite = null;
        }


        DateTime dhAlterada = dhAtual.minusHours(horasTempoLimite);

        // Regra 2
        if (validateDataHoraRotaControleCarga(dhAlterada, idRotaIdaVolta, trechoRotaIdaVolta, Boolean.FALSE, dataLimite)) {
            return dhAlterada.toYearMonthDay();
        }

        // Regra 3
        if (dhAlterada.toYearMonthDay().compareTo(dhAtual.toYearMonthDay()) != 0 &&
                validateDataHoraRotaControleCarga(JTDateTimeUtils.getFirstHourOfDay(dhAtual), idRotaIdaVolta, trechoRotaIdaVolta, Boolean.FALSE, dataLimite)) {
            return dhAtual.toYearMonthDay();
        }

        // Regra 4
        DateTime dhTesteRota = dhAtual.plusDays(1);
        dhTesteRota = JTDateTimeUtils.getFirstHourOfDay(dhTesteRota);

        Boolean blGeraExcecao = Boolean.valueOf(JTDateTimeUtils.getNroDiaSemana(dhAtual.toYearMonthDay()) == DateTimeConstants.FRIDAY ||
                JTDateTimeUtils.getNroDiaSemana(dhAtual.toYearMonthDay()) == DateTimeConstants.SATURDAY ||
                JTDateTimeUtils.getNroDiaSemana(dhAtual.toYearMonthDay()) == DateTimeConstants.SUNDAY);
        if (validateDataHoraRotaControleCarga(dhTesteRota, idRotaIdaVolta, trechoRotaIdaVolta, !blGeraExcecao, dataLimite)) {
            return dhTesteRota.toYearMonthDay();
        }

        Long idFilial = trechoRotaIdaVolta.getFilialRotaByIdFilialRotaOrigem().getFilial().getIdFilial();
        Long idMunicipio = municipioService.findIdMunicipioByPessoa(idFilial);

        return validateRotaControleCargaFinaisDeSemana(idRotaIdaVolta, trechoRotaIdaVolta, idMunicipio, dataAtualSemHora, dataLimite);
    }

    /**
     * Executa a quinta regra para o método validateRotaControleCarga.
     *
     * @param idRotaIdaVolta
     * @param trechoRotaIdaVolta
     * @param idMunicipio
     * @param dataAtualSemHora
     * @param dataLimite
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private YearMonthDay validateRotaControleCargaToRegra5(Long idRotaIdaVolta, TrechoRotaIdaVolta trechoRotaIdaVolta, Long idMunicipio, DateTime dataAtualSemHora, DateTime dataLimite) {

        List listaDatas = new ArrayList();
        listaDatas.add(dataAtualSemHora.plusDays(1).toYearMonthDay());
        List listaFeriados = feriadoService.findFeriadoByMunicipio(idMunicipio, listaDatas);
        Boolean blGeraExcecao = Boolean.valueOf(listaFeriados.isEmpty());

        dataAtualSemHora = dataAtualSemHora.plusDays(1);

        if (validateDataHoraRotaControleCarga(dataAtualSemHora, idRotaIdaVolta, trechoRotaIdaVolta, !blGeraExcecao, dataLimite)) {
            return dataAtualSemHora.toYearMonthDay();
        }

        return validateRotaControleCargaToRegra5(idRotaIdaVolta, trechoRotaIdaVolta, idMunicipio, dataAtualSemHora, dataLimite);
    }

    private YearMonthDay validateRotaControleCargaFinaisDeSemana(Long idRotaIdaVolta, TrechoRotaIdaVolta trechoRotaIdaVolta, Long idMunicipio, DateTime dataAtualSemHora, DateTime dataLimite) {
        return validateRotaControleCargaToRegra5(idRotaIdaVolta, trechoRotaIdaVolta, idMunicipio, dataAtualSemHora, dataLimite);
    }

    public int getHorasSomarByDiaSemana(int dia) {
        if (DateTimeConstants.FRIDAY == dia) {
            return getHorasSomarFromParametroGeral("TEMPO_LIMITE_ROTA_SEXTA");
        } else if (DateTimeConstants.SATURDAY == dia) {
            return getHorasSomarFromParametroGeral("TEMPO_LIMITE_ROTA_SABADO");
        } else if (DateTimeConstants.SUNDAY == dia) {
            return getHorasSomarFromParametroGeral("TEMPO_LIMITE_ROTA_DOMINGO");
        }
        return 0;
    }

    private int getHorasSomarFromParametroGeral(String nomeParametro) {
        ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(nomeParametro);
        String dsConteudo = parametroGeral != null ? parametroGeral.getDsConteudo() : "";
        Double horas = Double.parseDouble(StringUtils.isEmpty(dsConteudo) ? "0" : dsConteudo);
        return horas.intValue();
    }

    /**
     * @param idRotaColetaEntrega
     * @param idMeioTransporte
     * @param findDefinition
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginatedByProgramacaoColetasVeiculos(Long idRotaColetaEntrega, Long idMeioTransporte, FindDefinition findDefinition) {
        ResultSetPage rsp = getControleCargaDAO().findPaginatedByProgramacaoColetasVeiculos(idRotaColetaEntrega, idMeioTransporte, findDefinition);
        rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList()));
        return rsp;
    }

    /**
     * @param idRotaColetaEntrega
     * @param idMeioTransporte
     * @return
     */
    public Integer getRowCountByProgramacaoColetasVeiculos(Long idRotaColetaEntrega, Long idMeioTransporte) {
        return getControleCargaDAO().getRowCountByProgramacaoColetasVeiculos(idRotaColetaEntrega, idMeioTransporte);
    }

    /**
     * Busca um Controle de Carga com status GE (gerado) que tenha rota coleta entrega igual à rota passada por parâmetro e que possua o menor psTotalFrota.
     * Caso nao entrontre, retorna null;
     *
     * @param idRotaColetaEntrega
     * @return
     */
    public ControleCarga findControleCargaGEByIdRotaColetaEntregaWithLowerPsTotalFrota(Long idRotaColetaEntrega) {
        return getControleCargaDAO().findControleCargaGEByIdRotaColetaEntregaWithLowerPsTotalFrota(idRotaColetaEntrega);
    }

    /**
     * Calcula os totais de um Controle de Carga em termos de valores e pesos e, em seguida, grava estes valores
     * no próprio controle de carga.
     *
     * @param idControleCarga
     * @param blMeioTransportePodeSerNulo
     */
    public void generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(Long idControleCarga, Boolean blMeioTransportePodeSerNulo, Filial filialSessao) {
        generateAtualizacaoTotaisParaCcColetaEntrega(findByIdInitLazyProperties(idControleCarga, false), blMeioTransportePodeSerNulo, filialSessao);
    }


    /**
     * Calcula os totais de um Controle de Carga em termos de valores e pesos e, em seguida, grava estes valores
     * no próprio controle de carga.
     *
     * @param cc
     * @param blMeioTransportePodeSerNulo
     */
    public void generateAtualizacaoTotaisParaCcColetaEntrega(ControleCarga cc, Boolean blMeioTransportePodeSerNulo, Filial filialSessao) {

        Pais pais = this.paisService.findPaisByIdFilialSessao(filialSessao.getIdFilial());
        Moeda moeda = filialSessao.getMoeda();

        if (!cc.getTpControleCarga().getValue().equals(COLETA)) {
            throw new BusinessException("LMS-05009");
        }

        if (cc.getMeioTransporteByIdTransportado() == null && !blMeioTransportePodeSerNulo) {
            throw new BusinessException("LMS-05100");
        }

        TypedFlatMap mapColetasRealizadas = pedidoColetaService.findSomatorioColetasRealizadasByIdControleCarga(cc.getIdControleCarga());
        BigDecimal vlColetado = mapColetasRealizadas.getBigDecimal("vlColetado");
        BigDecimal psColetado = mapColetasRealizadas.getBigDecimal("psColetado");
        BigDecimal psAforadoColetado = mapColetasRealizadas.getBigDecimal("psAforadoColetado");

        TypedFlatMap mapColetasASeremRealizadas =
                pedidoColetaService.findSomatorioColetasASeremRealizadasByIdControleCarga(cc.getIdControleCarga());
        BigDecimal vlAColetar = mapColetasASeremRealizadas.getBigDecimal("vlAColetar");
        BigDecimal psAColetar = mapColetasASeremRealizadas.getBigDecimal("psAColetar");

        TypedFlatMap mapEntregasRealizadas = manifestoEntregaService.findSomatorioEntregasRealizadasByIdControleCarga(cc.getIdControleCarga(), pais.getIdPais(), moeda.getIdMoeda()); // aqui
        BigDecimal vlEntregue = mapEntregasRealizadas.getBigDecimal("vlEntregue");
        BigDecimal psEntregue = mapEntregasRealizadas.getBigDecimal("psEntregue");

        TypedFlatMap mapEntregasASeremRealizadas =
                manifestoEntregaService.findSomatorioEntregasASeremRealizadasByIdControleCarga(cc.getIdControleCarga(), pais.getIdPais(), moeda.getIdMoeda());
        BigDecimal vlAEntregar = mapEntregasASeremRealizadas.getBigDecimal("vlAEntregar");
        BigDecimal psAEntregar = mapEntregasASeremRealizadas.getBigDecimal("psAEntregar");
        BigDecimal psAforadoAEntregar = mapEntregasASeremRealizadas.getBigDecimal("psAforadoAEntregar");

        cc.setVlTotalFrota(vlAEntregar.add(vlColetado));
        cc.setVlColetado(vlColetado);
        cc.setVlAColetar(vlAColetar);
        cc.setVlEntregue(vlEntregue);
        cc.setVlAEntregar(vlAEntregar);
        cc.setPsTotalFrota(psAEntregar.add(psColetado));
        cc.setPsTotalAforado(psAforadoAEntregar.add(psAforadoColetado));
        cc.setPsColetado(psColetado);
        cc.setPsAColetar(psAColetar);
        cc.setPsEntregue(psEntregue);
        cc.setPsAEntregar(psAEntregar);

        if (cc.getMeioTransporteByIdTransportado() != null) {
            BigDecimal nrCapacidadeKg;
            if (cc.getMeioTransporteByIdSemiRebocado() != null) {
                nrCapacidadeKg = cc.getMeioTransporteByIdSemiRebocado().getNrCapacidadeKg();
            } else {
                nrCapacidadeKg = cc.getMeioTransporteByIdTransportado().getNrCapacidadeKg();
            }

            cc.setPcOcupacaoCalculado(somaPcOcupacao(cc.getPsTotalFrota(), nrCapacidadeKg, cc.getIdControleCarga(), true));
            cc.setPcOcupacaoAforadoCalculado(somaPcOcupacao(cc.getPsTotalAforado(), nrCapacidadeKg, cc.getIdControleCarga(), false));
        }
        cc.setMoeda(moeda);
        store(cc);
    }

    /**
     * @param ps
     * @param nrCapacidade
     * @return
     */
    private BigDecimal somaPcOcupacao(BigDecimal ps, BigDecimal nrCapacidade, Long idControleCarga, boolean blOcupacaoCalculado) {
        if (ps.compareTo(BigDecimalUtils.ZERO) == 0 || nrCapacidade == null || nrCapacidade.compareTo(BigDecimalUtils.ZERO) == 0) {
            return BigDecimalUtils.ZERO;
        }

        BigDecimal vlTotal = ps.multiply(BigDecimalUtils.HUNDRED);
        vlTotal = vlTotal.divide(nrCapacidade, DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);

        if (vlTotal.compareTo(new BigDecimal(SEGUNDO_INTERVALO_MILIS)) >= 0) {
            NumberFormat nf = DecimalFormat.getInstance(LocaleContextHolder.getLocale());
            String strValor = nf.format(vlTotal.doubleValue());

            ControleCarga cc = findById(idControleCarga);
            String siglaNumero = FormatUtils.formatSgFilialWithLong(
                    cc.getFilialByIdFilialOrigem().getSgFilial(), cc.getNrControleCarga(), FORMAT_PATTERN);

            String nmCampo = blOcupacaoCalculado ? configuracoesFacade.getMensagem("percentualOcupacaoCalculado") :
                    configuracoesFacade.getMensagem("percentualOcupacaoAforadoCalculado");

            throw new BusinessException("LMS-05152", new Object[]{siglaNumero, nmCampo, strValor});
        }
        return vlTotal;
    }


    /**
     * Método responsável por verificar se o usuário poderá fazer alguma alteração na tela do manter controle de carga.
     * O perfil DIVOP não tem acesso ao método validatePermiteAlteracaoCcByPerfilUsuario.
     *
     * @param idControleCarga
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param idFilialAtualizaStatus
     * @param tpControleCarga
     * @param tpStatusCc
     * @return TRUE se é permitido fazer alguma alteração no cc, caso contrário, FALSE.
     */
    @SuppressWarnings("rawtypes")
    public Boolean validatePermissaoAlteracaoCc(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino,
                                                Long idFilialAtualizaStatus, String tpControleCarga, String tpStatusCc) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        if (tpControleCarga.equals(COLETA)) {
            if (!idFilialOrigem.equals(idFilialUsuario) && !SessionUtils.isFilialSessaoMatriz()) {
                return Boolean.FALSE;
            }

            if ("TC".equals(tpStatusCc) || "AD".equals(tpStatusCc) || "ED".equals(tpStatusCc) ||
                    "FE".equals(tpStatusCc) || "CA".equals(tpStatusCc)) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }

        if ("V".equals(tpControleCarga)) {
            if (idFilialUsuario.equals(idFilialAtualizaStatus) && !idFilialUsuario.equals(idFilialDestino) &&
                    !"EV".equals(tpStatusCc) && !"FE".equals(tpStatusCc) && !"CA".equals(tpStatusCc) && !"AD".equals(tpStatusCc)) {
                return Boolean.TRUE;
            }

            List listaEventoControleCarga = eventoControleCargaService.
                    findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilialUsuario, idControleCarga, "SP");
            if (idFilialOrigem.equals(idFilialUsuario) && listaEventoControleCarga.isEmpty()
                    && !"EV".equals(tpStatusCc) && !"FE".equals(tpStatusCc) && !"CA".equals(tpStatusCc) && !"AD".equals(tpStatusCc)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Método utilizado pelo framework de segurança para verificar permissões de acesso à funcionalidades do controle de carga.
     * O perfil DIVOP não tem acesso a este método.
     */
    public void validatePermiteAlteracaoCcByPerfilUsuario() {
        // ESSE MÉTODO NÃO PODE SER REMOVIDO
    }


    /**
     * Verifica se existe algum pagtoPedagioCc onde não tenha sido informado a operadora/número do cartão para blCartaoPedagio = 'S'.
     *
     * @param idControleCarga
     */
    public void validatePreenchimentoCartaoPedagioNoControleCarga(Long idControleCarga) {
        if (pagtoPedagioCcService.validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(idControleCarga)) {
            throw new BusinessException("LMS-05161");
        }
    }


    /**
     * @param controleCarga
     */
    @SuppressWarnings("rawtypes")
    public void storeAtualizaTempoViagemParaControleCarga(ControleCarga controleCarga) {
        int nrTempoViagem = 0;
        List listaControleTrecho = controleTrechoService.
                findControleTrechoByControleCarga(controleCarga.getIdControleCarga(), Boolean.TRUE, null, null);
        for (Iterator iter = listaControleTrecho.iterator(); iter.hasNext(); ) {
            ControleTrecho ct = (ControleTrecho) iter.next();
            if (!ct.getBlTrechoDireto()) {
                continue;
            }
            nrTempoViagem += ct.getNrDistancia().intValue();
        }
        controleCarga.setNrTempoViagem(Integer.valueOf(nrTempoViagem));
        store(controleCarga);
    }


    /**
     * Gera eventos de previsão de saída para os documentos de serviço do controle de cargas. Será chamada na emissão do
     * controle de carga somente para rota expressa ou expressa cliente.
     *
     * @param idControleCarga
     * @param idFilial        Filial para a qual está sendo emitido o controle de carga
     */
    @SuppressWarnings("rawtypes")
    public void generateEventoPrevisaoSaidaParaDoctoServico(Long idControleCarga, Long idFilial) {
        ControleCarga cc = findByIdInitLazyProperties(idControleCarga, false);
        if (!"V".equals(cc.getTpControleCarga().getValue())) {
            throw new BusinessException(LMS_05027);
        }

        if ("EV".equals(cc.getTpRotaViagem().getValue())) {
            throw new BusinessException(LMS_05164);
        }

        Short cdEvento = ConstantesSim.EVENTO_CANCELAMENTO_FIM_CARREGAMENTO;
        Filial filial = filialService.findById(idFilial);

        List listaManifestos = manifestoService.findDoctoServicoVinculadosManifestoByIdControleCarga(idControleCarga, idFilial);
        for (Iterator iter = listaManifestos.iterator(); iter.hasNext(); ) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();

            String strDocumento = tfm.getString("sgFilialOrigemDoctoServico") + " " + StringUtils.leftPad(tfm.getLong("nrDoctoServico").toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);

            incluirEventosRastreabilidadeInternacionalService.
                    generateEventoDocumento(cdEvento, tfm.getLong(ID_DOCTO_SERVICO),
                            idFilial, strDocumento, tfm.getDateTime(DH_PREVISAO_SAIDA),
                            null, filial.getSiglaNomeFilial(), tfm.getString("tpDoctoServico.value"));
        }
    }


    /**
     * Gera eventos de previsão chegada para os documentos de serviço do controle de cargas.
     *
     * @param idControleCarga
     * @param idFilial        Filial para a qual está sendo emitido o controle de carga
     * @param dhSaidaPortaria Data e hora da saída na portaria. Esta informação só é utilizada no cálculo da previsão de chegada
     *                        da rota eventual.
     */
    @SuppressWarnings("rawtypes")
    public void generateEventoPrevisaoChegadaParaDoctoServico(Long idControleCarga, Long idFilial, DateTime dhSaidaPortaria) {
        ControleCarga cc = findByIdInitLazyProperties(idControleCarga, false);
        if (!"V".equals(cc.getTpControleCarga().getValue())) {
            throw new BusinessException(LMS_05027);
        }

        Short cdEvento = ConstantesSim.EVENTO_PREVISAO_CHEGADA;

        List listaManifestos = manifestoService.findDoctoServicoVinculadosManifestoByIdControleCarga(idControleCarga, idFilial);
        for (Iterator iter = listaManifestos.iterator(); iter.hasNext(); ) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();

            DateTime dhPrevisaoChegada = tfm.getDateTime("dhPrevisaoChegada");
            if ("EV".equals(cc.getTpRotaViagem().getValue())) {
                dhPrevisaoChegada = dhSaidaPortaria.plusMinutes(tfm.getInteger(NR_TEMPO_VIAGEM).intValue());
            }

            String strDocumento = tfm.getString("sgFilialOrigemDoctoServico") + " " + StringUtils.leftPad(tfm.getLong("nrDoctoServico").toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
            //De acordo com a Camila a filial do evento de previsão de chegada deve ser a filial de destino do Manifesto.
            String strFilialDestinoManifesto = tfm.getString("sgFilialDestinoManifesto") + " - " + tfm.getString("nmFilialDestinoManifesto");

            incluirEventosRastreabilidadeInternacionalService.
                    generateEventoDocumento(cdEvento, tfm.getLong(ID_DOCTO_SERVICO),
                            idFilial, strDocumento, dhPrevisaoChegada,
                            null, strFilialDestinoManifesto, tfm.getString("tpDoctoServico.value"));
        }
    }

    /**
     * Solicitação CQPRO00005467 da integração.
     * <p>
     * Método que retorna uma ou mais instâncias da classe ControleCarga
     * de acordo com tpStatusControleCarga e idMeioTransporte.
     *
     * @param tpStatusControleCarga
     * @param idMeioTransporte
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findControleCarga(String tpStatusControleCarga, Long idMeioTransporte) {
        return getControleCargaDAO().findControleCarga(tpStatusControleCarga, idMeioTransporte);
    }


    @SuppressWarnings("rawtypes")
    public void generateFechamentoControleCarga(Long idControleCarga) {
        ControleCarga cc = findByIdInitLazyProperties(idControleCarga, false);

        if ("CA".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException(LMS_05123);
        }

        if ("FE".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException(LMS_05168);
        }

        if (!"V".equals(cc.getTpControleCarga().getValue())) {
            throw new BusinessException(LMS_05027);
        }

        List listaManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (Iterator iter = listaManifestos.iterator(); iter.hasNext(); ) {
            Manifesto manifesto = (Manifesto) iter.next();
            manifesto.setTpStatusManifesto(new DomainValue("FE"));
            manifestoService.storeBasic(manifesto);
        }

        Long idMeioTransporte = null;
        if (cc.getMeioTransporteByIdTransportado() != null) {
            idMeioTransporte = cc.getMeioTransporteByIdTransportado().getIdMeioTransporte();
        }

        generateEventoChangeStatusControleCarga(idControleCarga, SessionUtils.getFilialSessao().getIdFilial(),
                "FM", JTDateTimeUtils.getDataHoraAtual(), null,
                idMeioTransporte, configuracoesFacade.getMensagem("fechamentoManual"),
                null, null, null, null, null, null, null, null);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List generateReciboFreteCarreteiroToEmissaoControleCarga(ControleCarga controleCarga) {
        Long idControleCarga = controleCarga.getIdControleCarga();
        List listaIdsReciboFreteCarreteiro = new ArrayList();

        BigDecimal vlPremio = null;
        if (controleCarga.getSolicitacaoContratacao() != null) {
            SolicitacaoContratacao sc = solicitacaoContratacaoService.
                    findById(controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao());
            vlPremio = sc.getVlPremio();
        } else if (controleCarga.getRotaIdaVolta() != null && controleCarga.getRotaIdaVolta().getVlPremio() != null) {
            vlPremio = controleCarga.getRotaIdaVolta().getVlPremio();
        }

        MeioTransporteRodoviario mtr = meioTransporteRodoviarioService.findById(controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());


        List listaManifestoViagemNacional = manifestoViagemNacionalService.findByAdiantamentoTrecho(idControleCarga, SessionUtils.getFilialSessao().getIdFilial());
        for (Iterator iter = listaManifestoViagemNacional.iterator(); iter.hasNext(); ) {
            ManifestoViagemNacional mvn = (ManifestoViagemNacional) iter.next();

            List listaAdiantamentoTrechos = adiantamentoTrechoService.findByIdControleCarga(
                    idControleCarga,
                    mvn.getManifesto().getFilialByIdFilialOrigem().getIdFilial(),
                    mvn.getManifesto().getFilialByIdFilialDestino().getIdFilial());

            for (Iterator iterAdiantamentoTrecho = listaAdiantamentoTrechos.iterator(); iterAdiantamentoTrecho.hasNext(); ) {
                AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho) iterAdiantamentoTrecho.next();
                if (adiantamentoTrecho.getVlAdiantamento() == null || adiantamentoTrecho.getVlAdiantamento().compareTo(BigDecimalUtils.ZERO) == 0) {
                    continue;
                }

                ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
                reciboFreteCarreteiro.setFilial(adiantamentoTrecho.getFilialByIdFilialOrigem());
                reciboFreteCarreteiro.setFilialDestino(adiantamentoTrecho.getFilialByIdFilialDestino());
                reciboFreteCarreteiro.setTpReciboFreteCarreteiro(new DomainValue("V"));
                reciboFreteCarreteiro.setMoedaPais(moedaPaisService.findMoedaPaisUsuarioLogado());
                reciboFreteCarreteiro.setPcAdiantamentoFrete(adiantamentoTrecho.getPcFrete());
                reciboFreteCarreteiro.setVlBruto(adiantamentoTrecho.getVlAdiantamento());
                reciboFreteCarreteiro.setBlAdiantamento(Boolean.TRUE);
                reciboFreteCarreteiro.setProprietario(controleCarga.getProprietario());
                reciboFreteCarreteiro.setManifestoViagemNacional(mvn);
                reciboFreteCarreteiro.setMeioTransporteRodoviario(mtr);
                reciboFreteCarreteiro.setMotorista(controleCarga.getMotorista());
                reciboFreteCarreteiro.setControleCarga(controleCarga);
                reciboFreteCarreteiro.setVlPremio(null);
                reciboFreteCarreteiro.setVlPostoPassagem(null);
                reciboFreteCarreteiro.setReciboComplementado(null);
                reciboFreteCarreteiro.setObReciboFreteCarreteiro(null);
                ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro) reciboFreteCarreteiroService.generateReciboFreteCarreteiro(reciboFreteCarreteiro, "CA");
                listaIdsReciboFreteCarreteiro.add(rfc.getIdReciboFreteCarreteiro());

                adiantamentoTrecho.setReciboFreteCarreteiro(rfc);
                adiantamentoTrecho.setTpStatusRecibo(new DomainValue("E"));
                adiantamentoTrechoService.store(adiantamentoTrecho);
            }

            ReciboFreteCarreteiro reciboFreteCarreteiro = new ReciboFreteCarreteiro();
            reciboFreteCarreteiro.setFilial(mvn.getManifesto().getFilialByIdFilialOrigem());
            reciboFreteCarreteiro.setFilialDestino(mvn.getManifesto().getFilialByIdFilialDestino());
            reciboFreteCarreteiro.setTpReciboFreteCarreteiro(new DomainValue("V"));
            reciboFreteCarreteiro.setMoedaPais(moedaPaisService.findMoedaPaisUsuarioLogado());
            reciboFreteCarreteiro.setPcAdiantamentoFrete(null);
            reciboFreteCarreteiro.setVlBruto(controleCarga.getVlFreteCarreteiro());
            reciboFreteCarreteiro.setBlAdiantamento(Boolean.FALSE);
            reciboFreteCarreteiro.setProprietario(controleCarga.getProprietario());
            reciboFreteCarreteiro.setManifestoViagemNacional(mvn);
            reciboFreteCarreteiro.setMeioTransporteRodoviario(mtr);
            reciboFreteCarreteiro.setMotorista(controleCarga.getMotorista());
            reciboFreteCarreteiro.setControleCarga(controleCarga);
            reciboFreteCarreteiro.setVlPremio(vlPremio);
            reciboFreteCarreteiro.setVlPostoPassagem(controleCarga.getVlPedagio());
            reciboFreteCarreteiro.setReciboComplementado(null);
            reciboFreteCarreteiro.setObReciboFreteCarreteiro(null);
            ReciboFreteCarreteiro rfc1 = (ReciboFreteCarreteiro) reciboFreteCarreteiroService.generateReciboFreteCarreteiro(reciboFreteCarreteiro, "CA");
            listaIdsReciboFreteCarreteiro.add(rfc1.getIdReciboFreteCarreteiro());
        }
        return listaIdsReciboFreteCarreteiro;
    }


    @SuppressWarnings("rawtypes")
    public Long generateSMP(Long idControleCarga, Long idFilial, PlanoGerenciamentoRiscoDTO plano) {
        List listaEventoControleCarga = eventoControleCargaService.
                findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(SessionUtils.getFilialSessao().getIdFilial(), idControleCarga, "EM");

        Long idSolicMonitPreventivo = null;
        if (listaEventoControleCarga.isEmpty()) {
            idSolicMonitPreventivo = gerarEnviarSMPService.generateGerarSMP(idControleCarga, plano);
        }
        return idSolicMonitPreventivo;
    }


    /**
     * Verifica se há manifestos para todos os trechos com origem na filial do usuário logado.
     *
     * @param idControleCarga
     * @return True se existe, caso contrário, False.
     */
    public Boolean validateExisteManifestoParaTrechoOrigem(Long idControleCarga) {
        ControleCarga cc = findByIdInitLazyProperties(idControleCarga, false);
        String tpVinculo = cc.getMeioTransporteByIdTransportado().getTpVinculo().getValue();
        if ("E".equals(tpVinculo) || "A".equals(tpVinculo)) {
            return getControleCargaDAO().validateExisteManifestoParaTrechoOrigem(idControleCarga, SessionUtils.getFilialSessao().getIdFilial());
        } else {
            return Boolean.TRUE;
        }
    }


    /**
     * Verifica se é permitido fechar manualmente o controle de carga.
     *
     * @param idControleCarga
     * @return True se é permitido, caso contrário, False.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Boolean validatePermiteFecharControleCarga(Long idControleCarga) {
        ControleCarga cc = findByIdInitLazyProperties(idControleCarga, false);
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

        // Regra 1
        if (!"V".equals(cc.getTpControleCarga().getValue())) {
            throw new BusinessException(LMS_05027);
        }

        if ("CA".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException(LMS_05123);
        }

        if ("FE".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException(LMS_05168);
        }

        // Regra
        Map mapControleCarga = new HashMap();
        mapControleCarga.put(ID_CONTROLE_CARGA, cc.getIdControleCarga());

        Map mapSinistro = new HashMap();
        mapSinistro.put(CONTROLE_CARGA, mapControleCarga);

        if (!processoSinistroService.find(mapSinistro).isEmpty() && SessionUtils.isFilialSessaoMatriz()) {
            return Boolean.TRUE;
        }

        // Regra
        if (cc.getFilialByIdFilialDestino() != null) {
            Filial filialDestino = filialService.findById(cc.getFilialByIdFilialDestino().getIdFilial());
            if (!filialDestino.getEmpresa().getTpEmpresa().getValue().equals(ConstantesMunicipios.TP_EMPRESA_MERCURIO) &&
                    cc.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario)) {
                return Boolean.TRUE;
            }
        }

        // Regra
        if (!manifestoService.findManifestoWithDocumentosEntreguesByControleCarga(idControleCarga)) {
            return Boolean.FALSE;
        }

        // Regra 5
        Map mapManifesto = new HashMap();
        mapManifesto.put(CONTROLE_CARGA, mapControleCarga);

        List listaManifesto = manifestoService.find(mapManifesto);
        if (listaManifesto.size() == 1) {
            Manifesto m = (Manifesto) listaManifesto.get(0);
            if ("V".equals(m.getTpManifesto().getValue()) && "ED".equals(m.getTpManifestoViagem().getValue()) &&
                    (m.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario) || m.getFilialByIdFilialDestino().getIdFilial().equals(idFilialUsuario))) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    /**
     * @param idControleCarga
     */
    @SuppressWarnings("rawtypes")
    public void generateCancelamentoEmissaoControleCarga(Long idControleCarga) {
        ControleCarga cc = findById(idControleCarga);

        if ("CA".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException("LMS-05073");
        }

        if (!"AV".equals(cc.getTpStatusControleCarga().getValue()) && !"AE".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException("LMS-05175");
        }

        DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

        generateEventoChangeStatusControleCarga(idControleCarga, idFilialUsuario,
                "EC", dhAtual, null,
                cc.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                configuracoesFacade.getMensagem("emissaoCancelada"),
                null, null, null, null, null, null, null, null);

        generateEventoMeioTransporteByCancelamentoEmissaoCC(cc, cc.getMeioTransporteByIdTransportado(), dhAtual);
        if (cc.getMeioTransporteByIdSemiRebocado() != null) {
            generateEventoMeioTransporteByCancelamentoEmissaoCC(cc, cc.getMeioTransporteByIdSemiRebocado(), dhAtual);
        }

        List listaAdiantamentoTrecho = adiantamentoTrechoService.findByIdControleCarga(idControleCarga, idFilialUsuario, null);
        DomainValue tpStatusRecibo = new DomainValue("G");
        for (Iterator iter = listaAdiantamentoTrecho.iterator(); iter.hasNext(); ) {
            AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho) iter.next();
            adiantamentoTrecho.setTpStatusRecibo(tpStatusRecibo);
            adiantamentoTrechoService.store(adiantamentoTrecho);
        }
    }

    //LMS-3544
    @SuppressWarnings("rawtypes")
    public Map generateCancelamentoEmissaoControleCargaMdfe(Long idControleCarga) {
        generateCancelamentoEmissaoControleCarga(idControleCarga);

        ControleCarga cc = findById(idControleCarga);

        if ("V".equals(cc.getTpControleCarga().getValue())) {
            return manifestoEletronicoService.cancelarMdfe(idControleCarga);
        }
        return new HashMap();
    }

    /**
     * @param cc
     * @param mt
     * @param dhAtual
     */
    private void generateEventoMeioTransporteByCancelamentoEmissaoCC(ControleCarga cc, MeioTransporte mt, DateTime dhAtual) {
        EventoMeioTransporte eventoMeioTransportado = new EventoMeioTransporte();
        eventoMeioTransportado.setTpSituacaoMeioTransporte(new DomainValue("PAOP"));
        eventoMeioTransportado.setDhInicioEvento(dhAtual);
        eventoMeioTransportado.setDhFimEvento(null);
        eventoMeioTransportado.setBox(null);
        eventoMeioTransportado.setControleCarga(cc);
        eventoMeioTransportado.setFilial(SessionUtils.getFilialSessao());
        eventoMeioTransportado.setMeioTransporte(mt);
        eventoMeioTransportado.setUsuario(SessionUtils.getUsuarioLogado());
        eventoMeioTransporteService.generateEvent(eventoMeioTransportado, dhAtual);
    }


    /**
     * Verifica se existe eventos emitidos que não foram cancelados.
     *
     * @param idControleCarga
     * @return True se existe eventos emitidos que não foram cancelados, caso contrário, False.
     */
    @SuppressWarnings("rawtypes")
    public Boolean validateExisteEventoEmitido(Long idControleCarga) {
        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

        List listaEventosEM = eventoControleCargaService.
                findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilialUsuario, idControleCarga, "EM");

        List listaEventosEC = eventoControleCargaService.
                findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilialUsuario, idControleCarga, "EC");

        if (listaEventosEM.size() > listaEventosEC.size()) {
            return true;
        }

        return false;
    }


    public void validateControleCargaByInclusaoDocumentos(Long idControleCarga) {
        ControleCarga cc = get(idControleCarga);
        this.validateControleCargaByInclusaoDocumentos(cc);
    }


    public void validateControleCargaByInclusaoDocumentos(ControleCarga cc) {
        if ("CA".equals(cc.getTpStatusControleCarga().getValue())) {
            throw new BusinessException(LMS_05123);
        }

        if (carregamentoDescargaService.validateExisteCarregamentoFinalizado(cc.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial())) {
            throw new BusinessException(LMS_05124);
        }
    }


    /**
     * Verifica se o perfil do usúario possue o perfil "PERMITE_MANUTENCOES_ESPECIAIS_CC"
     * para manipular o controle de carga no LMS
     *
     * @return Boolean
     */
    public Boolean validateManutencaoEspecialCC(Usuario usuario) {
        return usuarioService.findUsuarioHasPerfil(usuario.getIdUsuario(), "PERMITE_MANUTENCOES_ESPECIAIS_CC");
    }

    /**
     * Método que retorna os Controle de Carga e que não possuem
     * um evento de baixa de coleta
     *
     * @param idControleCarga
     * @return
     */
    private boolean existsPedidoColetaSemOcorrenciaColeta(Long idControleCarga) {
        return getControleCargaDAO().existsControleCargaSemOcorrenciaColeta(idControleCarga);
    }

    /**
     * Validações para regra 5.2 da ET 03.01.01.01
     *
     * @param idControleCarga
     */
    public void validateControleCargaComOcorrenciaVinculada(Long idControleCarga) {
        ControleCarga controleCarga = findByIdInitLazyProperties(idControleCarga, false);
        CarregamentoDescarga carregamentoDescarga =
                carregamentoDescargaService.findCarregamentoDescarga(idControleCarga,
                        SessionUtils.getFilialSessao().getIdFilial(),
                        controleCarga.getTpControleCarga().getValue());
        //Se o carregamento já foi iniciado pelo MWW
        //LMS-03005 - Descarga já iniciada para este veículo.
        if (carregamentoDescarga != null && "O".equals(carregamentoDescarga.getTpStatusOperacao().getValue())) {
            throw new BusinessException("LMS-03005");
        }
        if (controleCarga.getTpControleCarga().getValue().equals(COLETA) && this.existsPedidoColetaSemOcorrenciaColeta(idControleCarga)) {
            throw new BusinessException("LMS-03016");
        }
    }

    public List<ControleCarga> findByMeioTransporteAndStatusAndControleDeCargaAndIdFilialUsuario(Long idMeioTransporte, List<String> tpStatus, Long idFilialUsuarioLogado) throws Exception{

        if (meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte)) {
            return getControleCargaDAO().findByMeioTransporteAndStatusAndControleDeCargaAndIdFilial(null, idMeioTransporte, tpStatus, idFilialUsuarioLogado);
        } else {
            return getControleCargaDAO().findByMeioTransporteAndStatusAndControleDeCargaAndIdFilial(idMeioTransporte, null, tpStatus, idFilialUsuarioLogado);
        }

    }

    public List<ControleCarga> findByMeioTransporteAndStatus(Long idMeioTransporte, List<String> tpStatus) {
        List<ControleCarga> retorno;
        if (meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte)) {
            retorno = getControleCargaDAO().findByMeioTransporteAndStatus(null, idMeioTransporte, tpStatus);
        } else {
            retorno = getControleCargaDAO().findByMeioTransporteAndStatus(idMeioTransporte, null, tpStatus);
        }
        return retorno;
    }

    public ControleCarga findByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
        return getControleCargaDAO().findByIdCarregamentoDescarga(idCarregamentoDescarga);
    }

    public ControleCarga findByMeioTransporteAndTpStatusControleCargaEmDescargaViagem(Long idMeioTransporte) {
        return  getControleCargaDAO().findByMeioTransporteAndTpStatusControleCargaEmDescargaViagem(idMeioTransporte);
    }

    public ControleCarga findByMeioTransporteTpControleCargaAndFilialAtualizaStatus(Long idMeioTransporte, String tpControleCarga, Long idFilial) {
        ControleCarga retorno;
        if (meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte)) {
            retorno = getControleCargaDAO().findByMeioTransporteTpControleCargaAndFilialAtualizaStatus(null, idMeioTransporte, tpControleCarga, idFilial);
        } else {
            retorno = getControleCargaDAO().findByMeioTransporteTpControleCargaAndFilialAtualizaStatus(idMeioTransporte, null, tpControleCarga, idFilial);
        }
        return retorno;
    }

    public ControleCarga findByIdAndTpOperacaoAndDispositivoUnitizacao(Long idControleCarga, Long idDispositivo, String tpOperacao) {
        return getControleCargaDAO().findByIdAndTpOperacaoAndDispositivoUnitizacao(idControleCarga, idDispositivo, tpOperacao);
    }


    public boolean findSolicitacaoPuxada(Long idControleCarga, Long idFilial) {
        return getControleCargaDAO().findSolicitacaoPuxada(idControleCarga, idFilial);
    }

    public void setTransponderService(TransponderService transponderService) {
        this.transponderService = transponderService;
    }

    /**
     * Verifica se o controle de carga iniciou viagem.
     * Se existe um evento de saida portaria, significa que iniciou viagem
     *
     * @param controleCarga
     * @return
     * @author DanielT
     */
    public boolean controleCargaIniciouViagem(ControleCarga controleCarga) {
        return !eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, controleCarga.getIdControleCarga(), "SP").isEmpty();
    }

    public void validateConhecimentosNaoEmitidosByControleCarga(long idControleCarga) {
        ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro("VALIDA_FIM_CARREG_EMISSAO_DS", false);
        if (parametroGeral != null && "S".equals(parametroGeral.getDsConteudo())) {
            List<Conhecimento> conhecimentosNaoEmitidos = conhecimentoService.findPreConhecimentoByIdControleCarga(idControleCarga);
            if (conhecimentosNaoEmitidos != null && !conhecimentosNaoEmitidos.isEmpty()) {
                throw new BusinessException("LMS-05351");
            }
        }
    }

    /*
     *
	 */
    public void generateAcaoIntegracaoEvento(Long nrDocumento, String dsProcesso) {
        AcaoIntegracao acaoIntegracao = acaoIntegracaoService.findByProcesso(dsProcesso);
        if (acaoIntegracao != null) {
            AcaoIntegracao acao = acaoIntegracao;
            AcaoIntegracaoEvento evento = new AcaoIntegracaoEvento();
            evento.setAcaoIntegracao(acao);
            evento.setDsInformacao(acao.getDsProcessoIntegracao());
            evento.setNrAgrupador(acaoIntegracaoEventosService.findLastAgrupador() + 1);
            evento.setNrDocumento(nrDocumento);
            evento.setTpDocumento(acao.getTpDocumento());
            evento.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());

            acaoIntegracaoEventosService.store(evento);
        }
    }

    /**
     * LMS-4527: Rotina GerarMDFE - Passo 8
     * <p>
     * Utilizar executeVerificaAutorizacaoMdfeSSS
     *
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public TypedFlatMap executeVerificaAutorizacaoMdfe(TypedFlatMap parameters) {
        DateTime dhEmissao = parameters.getDateTime(DH_EMISSAO);
        List<String> idsManifestoEletronico = (List<String>) parameters.get(IDS_MANIFESTO_ELETRONICO);

        if (idsManifestoEletronico == null || idsManifestoEletronico.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível monitorar a autorização do MDF-e junto ao sefaz.");
        }

        // Acessar na tabela MANIFESTO_ELETRONICO todos os MDF-e gerados pelos
        // seus respectivos IDs, verificando se a situação dos mesmos está como
        // "Autorizado" ou "Rejeitado" (MANIFESTO_ELETRONICO.TP_SITUACAO = "A"
        // ou "R"), se todos estiverem nestas situações sair do loop
        // normalmente. Se algum destes MDF-e não estiver, aguardar a quantidade
        // de segundos descritos no parâmetro geral "LIMITE_ESPERA_MDFE") e
        // realizar o acesso novamente.
        Integer count = 0;
        TypedFlatMap mapRetorno = new TypedFlatMap();
        for (String idManifestoEletronico : idsManifestoEletronico) {
            count++;
            ManifestoEletronico mdfe = (ManifestoEletronico) manifestoEletronicoService.findById(Long.valueOf(idManifestoEletronico));

            // Se algum dos manifestos retornados na query estiver com a
            // situação igual a “Rejeitado” visualizar mensagem LMS-05358,
            // finalizando o processo com erro.
            if (ConstantesExpedicao.TP_MDFE_REJEITADO.equals(mdfe.getTpSituacao().getValue())) {
                throw new BusinessException("LMS-05358");

            } else if (ConstantesExpedicao.TP_MDFE_AUTORIZADO.equals(mdfe.getTpSituacao().getValue())) {
                if (count.intValue() == 1) {
                    mapRetorno.put("nfMdfe", mdfe.getFilialOrigem().getSgFilial() + "-" + FormatUtils.fillNumberWithZero(mdfe.getNrManifestoEletronico().toString(), FILL_SIZE));
                }

                if (count.intValue() == idsManifestoEletronico.size()) {
                    mapRetorno.put(IMPRIME_MDFE_AUTORIZADO, true);
                    return mapRetorno;
                }

            } else {
                break;
            }
        }

        // Se o tempo limite estourou visualizar mensagem LMS-05357, finalizando o processo com erro.
        long limiteRetornoMdfe = Long.valueOf(parametroGeralService.findConteudoByNomeParametro(LIMITE_RETORNO_MDFE, false).toString());
        long intervalInMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - dhEmissao.getMillis();
        long intervalo = intervalInMillis / SEGUNDO_INTERVALO_MILIS;
        if (intervalo > limiteRetornoMdfe) {
            throw new BusinessException(LMS_05357);
        }

        mapRetorno = new TypedFlatMap();
        mapRetorno.put(LIMITE_ESPERA, parametroGeralService.findConteudoByNomeParametro(LIMITE_ESPERA_MDFE, false));
        mapRetorno.put(IDS_MANIFESTO_ELETRONICO, idsManifestoEletronico);
        mapRetorno.put(DH_EMISSAO, dhEmissao.toString(YYYY_MM_DD_HH_MM_SS));
        return mapRetorno;
    }

    /**
     * LMS-4527: Rotina GerarMDFE - Passo 8
     * <p>
     * Verifica se TODOS os mdfes foram processados pelo sefaz e estão autorizados OU rejeitados.
     *
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public TypedFlatMap executeVerificaAutorizacaoMdfes(TypedFlatMap parameters) {
        DateTime dhEmissao = parameters.getDateTime(DH_EMISSAO);
        List<Long> idsManifestoEletronico = (List<Long>) parameters.get(IDS_MANIFESTO_ELETRONICO);

        List<ManifestoEletronico> listMdfe = manifestoEletronicoService.findByIds(idsManifestoEletronico);


        if (idsManifestoEletronico == null || idsManifestoEletronico.isEmpty()) {
            throw new IllegalArgumentException("Não foi possível monitorar a autorização do MDF-e junto ao sefaz.");
        }

        // Acessar na tabela MANIFESTO_ELETRONICO todos os MDF-e gerados pelos
        // seus respectivos IDs, verificando se a situação dos mesmos está como
        // "Autorizado" ou "Rejeitado" (MANIFESTO_ELETRONICO.TP_SITUACAO = "A"
        // ou "R"), se todos estiverem nestas situações sair do loop
        // normalmente. Se algum destes MDF-e não estiver, aguardar a quantidade
        // de segundos descritos no parâmetro geral "LIMITE_ESPERA_MDFE") e
        // realizar o acesso novamente.
        TypedFlatMap mapRetorno = new TypedFlatMap();

        Collection outros = CollectionUtils.select(listMdfe, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                ManifestoEletronico mdfe = (ManifestoEletronico) arg0;
                return !(ConstantesExpedicao.TP_MDFE_AUTORIZADO.equals(mdfe.getTpSituacao().getValue()) || ConstantesExpedicao.TP_MDFE_REJEITADO.equals(mdfe.getTpSituacao().getValue()));
            }

        });

        if (!outros.isEmpty()) {
            //aguardar

            // Se o tempo limite estourou visualizar mensagem LMS-05357, finalizando o processo com erro.
            long limiteRetornoMdfe = Long.valueOf(parametroGeralService.findConteudoByNomeParametro(LIMITE_RETORNO_MDFE, false).toString());
            long intervalInMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - dhEmissao.getMillis();
            long intervalo = intervalInMillis / SEGUNDO_INTERVALO_MILIS;
            if (intervalo > limiteRetornoMdfe) {
                throw new BusinessException(LMS_05357);
            }

            //Se não estourou, volta para a tela que deve aguardar
            mapRetorno = new TypedFlatMap();
            mapRetorno.put(LIMITE_ESPERA, parametroGeralService.findConteudoByNomeParametro(LIMITE_ESPERA_MDFE, false));
            mapRetorno.put(IDS_MANIFESTO_ELETRONICO, idsManifestoEletronico);
            mapRetorno.put(DH_EMISSAO, dhEmissao.toString(YYYY_MM_DD_HH_MM_SS));
            return mapRetorno;
        }

        Collection rejeitados = CollectionUtils.select(listMdfe, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                ManifestoEletronico mdfe = (ManifestoEletronico) arg0;
                return ConstantesExpedicao.TP_MDFE_REJEITADO.equals(mdfe.getTpSituacao().getValue());
            }

        });

        //Se não retornar a chave "limiteEspera", então significa que terminou o processamento
        return mapRetorno;

    }

    public TypedFlatMap executeVerificaEncerramentoMdfe(Long idManifestoEletronicoEncerrado, DateTime dhEncerramento) {
        if (idManifestoEletronicoEncerrado == null) {
            throw new IllegalArgumentException("Não foi possível monitorar o encerramento do MDF-e junto ao sefaz.");
        }

        ManifestoEletronico mdfe = (ManifestoEletronico) manifestoEletronicoService.findById(idManifestoEletronicoEncerrado);
        if (mdfe != null) {

            if (ConstantesExpedicao.TP_MDFE_ENCERRADO.equals(mdfe.getTpSituacao().getValue())) {

                return new TypedFlatMap();

            } else if (ConstantesExpedicao.TP_MDFE_ENCERRADO_REJEITADO.equals(mdfe.getTpSituacao().getValue())) {

                throw new BusinessException("LMS-05359");

            } else {

                long limiteRetornoMdfe = Long.valueOf(parametroGeralService.findConteudoByNomeParametro(LIMITE_RETORNO_MDFE, false).toString());
                long intervalInMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - dhEncerramento.getMillis();
                long intervalo = intervalInMillis / SEGUNDO_INTERVALO_MILIS;
                if (intervalo > limiteRetornoMdfe) {
                    throw new BusinessException(LMS_05357);
                }

            }

        }

        TypedFlatMap map = new TypedFlatMap();
        map.put(LIMITE_ESPERA, parametroGeralService.findConteudoByNomeParametro(LIMITE_ESPERA_MDFE, false));
        map.put(ID_MANIFESTO_ELETRONICO_ENCERRADO, idManifestoEletronicoEncerrado);
        map.put(DH_ENCERRAMENTO, dhEncerramento.toString(YYYY_MM_DD_HH_MM_SS));
        return map;
    }

    public TypedFlatMap executeVerificaCancelamentoMdfe(Long idManifestoEletronicoCancelado, DateTime dhCancelamento) {
        if (idManifestoEletronicoCancelado == null) {
            throw new IllegalArgumentException("Não foi possível monitorar o cancelamento do MDF-e junto ao sefaz.");
        }

        ManifestoEletronico mdfe = (ManifestoEletronico) manifestoEletronicoService.findById(idManifestoEletronicoCancelado);
        if (mdfe != null) {

            if (ConstantesExpedicao.TP_MDFE_CANCELADO.equals(mdfe.getTpSituacao().getValue())) {

                return new TypedFlatMap();

            } else if (ConstantesExpedicao.TP_MDFE_CANCELADO_REJEITADO.equals(mdfe.getTpSituacao().getValue())) {

                throw new BusinessException("LMS-05360");

            } else {

                long limiteRetornoMdfe = Long.valueOf(parametroGeralService.findConteudoByNomeParametro(LIMITE_RETORNO_MDFE, false).toString());
                long intervalInMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - dhCancelamento.getMillis();
                long intervalo = intervalInMillis / SEGUNDO_INTERVALO_MILIS;
                if (intervalo > limiteRetornoMdfe) {
                    throw new BusinessException(LMS_05357);
                }

            }

        }

        TypedFlatMap map = new TypedFlatMap();
        map.put(LIMITE_ESPERA, parametroGeralService.findConteudoByNomeParametro(LIMITE_ESPERA_MDFE, false));
        map.put("idManifestoEletronicoCancelado", idManifestoEletronicoCancelado);
        map.put(DH_ENCERRAMENTO, dhCancelamento.toString(YYYY_MM_DD_HH_MM_SS));
        return map;
    }

    public Integer countByVolumeEmDescargaFilial(Long idVolumeNotaFiscal, Long idFilial, Long idControleCarga) {
        return getControleCargaDAO().countByVolumeEmDescargaFilial(idVolumeNotaFiscal, idFilial, idControleCarga);
    }

    public Integer countCteControleCarga(Long idControleCarga) {
        return getControleCargaDAO().countCteControleCarga(idControleCarga);
    }

    public boolean hasConhecimentoControleCarga(Long idControleCarga) {
        Integer count = getControleCargaDAO().countConhecimentoControleCarga(idControleCarga);
        return count != null && count > 0;
    }

    @SuppressWarnings("rawtypes")
    public void validateParcelaColetaEntrega(Long idControleCarga) {
        ControleCarga controleCarga = findById(idControleCarga);

        if (isCalculoPadrao()) {
            return;
        }

        TabelaColetaEntregaCC tabelaColetaCC = null;
        if (controleCarga.getTabelaColetaEntrega() == null) {
            tabelaColetaCC = tabelaColetaEntregaCCService.findOneByIdControleCarga(controleCarga.getIdControleCarga());
        }
        if (controleCarga.getTabelaColetaEntrega() != null || tabelaColetaCC != null) {
            List manifestosTipoEntrega = manifestoService.getManifestosTipoEntregaByIdControleCarga(controleCarga.getIdControleCarga());
            if (CollectionUtils.isNotEmpty(manifestosTipoEntrega)) {

                ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "VALIDA_PARCELA_PUD", false, true);
                List<String> listParametros = ControleCargaHelper.createListByConteudoParametro(conteudoParametroFilial);

                if (CollectionUtils.isNotEmpty(listParametros)) {

                    TabelaColetaEntrega tabelaColetaEntrega = controleCarga.getTabelaColetaEntrega() == null
                            ? controleCarga.getTabelaColetaEntrega() : tabelaColetaCC.getTabelaColetaEntrega();
                    ParcelaTabelaCe parcelaTabelaCeLoaded = parcelaTabelaCeService.findByTabelaColetaEntrega(tabelaColetaEntrega);

                    if (parcelaTabelaCeLoaded != null) {
                        for (Iterator iterator = manifestosTipoEntrega.iterator(); iterator.hasNext(); ) {
                            Manifesto manifesto = (Manifesto) iterator.next();
                            String chaveValidacao = ControleCargaHelper.createKeyValidacaoParcelaColetaEntrega(manifesto, tabelaColetaEntrega, parcelaTabelaCeLoaded);
                            for (String vlParametro : listParametros) {
                                if (chaveValidacao.equals(vlParametro)) {
                                    throw new BusinessException("LMS-05350");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void validateParcelaPud(Long idControleCarga, String tpPreManifesto) {
        ControleCarga controleCarga = findById(idControleCarga);
        List<TabelaColetaEntrega> tabelasColetasCC = getTabelasColetasEntregasByControleCarga(controleCarga);
        if (CollectionUtils.isNotEmpty(tabelasColetasCC)) {
            for (TabelaColetaEntrega tabelaColetaEntrega : tabelasColetasCC) {
                if (CollectionUtils.isNotEmpty(tabelaColetaEntrega.getParcelaTabelaCes())) {
                    for (ParcelaTabelaCe parcelaTabelaCe : tabelaColetaEntrega.getParcelaTabelaCes()) {
                        String chaveValidacao = ControleCargaHelper.createKeyValidacaoParcelaColetaEntrega(tpPreManifesto, tabelaColetaEntrega, parcelaTabelaCe);

                        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "VALIDA_PARCELA_PUD", false, true);
                        List<String> listParametros = ControleCargaHelper.createListByConteudoParametro(conteudoParametroFilial);
                        if (CollectionUtils.isNotEmpty(listParametros)) {
                            for (String vlParametro : listParametros) {
                                if (chaveValidacao.equals(vlParametro)) {
                                    throw new BusinessException("LMS-05350");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private List<TabelaColetaEntrega> getTabelasColetasEntregasByControleCarga(ControleCarga controleCarga) {
        List<TabelaColetaEntrega> result = null;

        if (isCalculoPadrao()) {
            return result;
        }


        if (controleCarga.getTabelaColetaEntrega() == null) {
            result = new ArrayList<TabelaColetaEntrega>();
            List<TabelaColetaEntregaCC> listTabelaColetaEntregaCC = tabelaColetaEntregaCCService.findByIdControleCarga(controleCarga.getIdControleCarga());
            for (TabelaColetaEntregaCC tabelaColetaEntregaCC : listTabelaColetaEntregaCC) {
                result.add(tabelaColetaEntregaCC.getTabelaColetaEntrega());
            }
        } else {
            result = Arrays.asList(controleCarga.getTabelaColetaEntrega());
        }
        return result;
    }

    public boolean findVinculoColetaEntregaTabelaColetaQuilometragem(Long idControleCarga) {
        return getControleCargaDAO().findVinculoColetaEntregaTabelaColetaQuilometragem(idControleCarga);
    }

    public Integer getRowCountParceiros(TypedFlatMap criteria) {
        return this.getControleCargaDAO().getRowCountParceiros(criteria);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginatedByParceiro(TypedFlatMap criteria, FindDefinition findDefinition) {

        ResultSetPage rsp = getControleCargaDAO().findPaginatedByParceiro(criteria, findDefinition);
        List newList = new ArrayList();
        for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ) {

            Object[] obj = (Object[]) iter.next();
            TypedFlatMap tfm = new TypedFlatMap();
            tfm.put(NR_CONTROLE_CARGA, obj[ARR_POS_0]);
            tfm.put(ID_CONTROLE_CARGA, obj[ARR_POS_1]);
            tfm.put("hasNotaCreditoPendente", "S".equals(obj[ARR_POS_2].toString()) ? "Sim" : "Não");
            tfm.put("doctoPlusColetasPendentes", obj[ARR_POS_3]);
            tfm.put("qtdNotasGeradas", obj[ARR_POS_4]);

            newList.add(tfm);
        }
        rsp.setList(newList);
        return rsp;
    }

    @SuppressWarnings("rawtypes")
    public List findControlesByIdNotaCredito(Long idNotaCredito) {
        // FIXME: Uma nota de crédito só possui um controle de carga. Por que não utilizar notaCredito.getControleCarga()?
        return getControleCargaDAO().findControleCargaByIdNotaCredito(idNotaCredito);
    }

    /**
     * 06.03.01.01 Operações Portuárias
     * <p>
     * Ao clicar em um item da GRID [Entregas no porto]
     *
     * @param idControleCarga
     */
    public void executeEntregaControleCargaPorto(Long idControleCarga) {
        ControleCarga cc = findById(idControleCarga);
        if ("TP".equals(cc.getTpStatusControleCarga().getValue())) {
            //Alterar o Controle de Carga para "Em transito fluvial" e remover o veículo do controle de carga.
            //CONTROLE_CARGA.TP_STATUS_CONTROLE_CARGA = "TF".
            //CONTROLE_CARGA.ID_TRANSPORTADO = NULO

            cc.setTpStatusControleCarga(new DomainValue("TF"));
            cc.setMeioTransporteByIdTransportado(null);
            store(cc);

            //Fechar qualquer manifesto eletrônico vinculado ao controle de carga
            executeEncerrarMdfesAutorizados(idControleCarga);

        }

    }

    /**
     * 06.03.01.01 Operações Portuárias
     * <p>
     * Ao clicar em um item da GRID [Coletas no porto]
     *
     * @param idControleCarga
     */
    public TypedFlatMap executeColetaControleCargaPortoGeracaoMDFe(Long idControleCarga, Long idMeioTransporte, boolean contingenciaConfirmada) {
        //Chamar a rotina "Validar Veículo Controle de Carga" da ET 05.01.01.01 Gerar Controle de Cargas, passando por parâmetro:
        //	idMeioTransporte = ID do Meio Transporte informado pelo usuário.
        //	idControleCarga = ID do Controle de Carga selecionado na GRID.

        meioTransporteService.findMeioTransporteIsCT(idMeioTransporte);

        validateVeiculoControleCarga(idMeioTransporte, idControleCarga, false);

        ControleCarga cc = findById(idControleCarga);
        //Gravar o veículo no controle de carga (CONTROLE_CARGA.ID_TRANSPORTADO = (ID do Meio Transporte informado pelo usuário).
        MeioTransporte meioTransporte = meioTransporteService.findById(idMeioTransporte);
        cc.setMeioTransporteByIdTransportado(meioTransporte);
        //Alterar o proprietário atual, para o proprietário correspondente ao veículo (CONTROLE_CARGA.ID_TRANSPORTADO - MEIO_TRANSP_PROPRIETARIO - PROPRIETARIO, Na tabela MEIO_TRANSP_PROPRIETARIO deve considerar apenas registros vigentes. O relacionamento retornará apenas um registro).
        Proprietario proprietario = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(idMeioTransporte, JTDateTimeUtils.getDataAtual());
        cc.setProprietario(proprietario);

        store(cc);

        //Gerar Manifesto Eletrônico para a viagem: Chamar a rotina “GerarMDFE” da ET 05.01.01.02 Emitir Controle de Carga, passando por parâmetro o ID do Controle de Carga selecionado na GRID.
        return generateMdfe(idControleCarga, contingenciaConfirmada, true);

    }

    /**
     * 06.03.01.01 Operações Portuárias
     * <p>
     * Ao clicar em um item da GRID [Coletas no porto] - atualizar para EV somente após a autorização do MDFe (assincrono)
     *
     * @param idControleCarga
     */
    public void executeColetaControleCargaPortoAtualizacaoStatus(Long idControleCarga) {

        ControleCarga cc = findById(idControleCarga);
        //Alterar a situação do controle de carga para “Em Viagem”. (CONTROLE_CARGA.TP_STATUS_CONTROLE_CARGA = “EV”).
        cc.setTpStatusControleCarga(new DomainValue("EV"));

        store(cc);

    }

    /**
     * Método responsável por validar se existe o documento de serviço atual em outro manifesto de viagem
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TypedFlatMap validateDoctoServOutroManifesto(Long idControleCarga) {
        //LMS-4911
        ControleCarga controleCarga = this.findByIdInitLazyProperties(idControleCarga, false);

        TypedFlatMap result = new TypedFlatMap();
        List resultDoctos = null;

        if ("V".equals(controleCarga.getTpControleCarga().getValue())) {

            for (Manifesto manifesto : controleCarga.getManifestos()) {

                for (PreManifestoDocumento preManifestoDocumento : manifesto.getPreManifestoDocumentos()) {
                    TypedFlatMap map = doctoServicoService.findDoctoServOutroManifestoViagem(manifesto.getIdManifesto(),
                            preManifestoDocumento.getDoctoServico().getIdDoctoServico(), manifesto.getFilialByIdFilialDestino().getIdFilial());

                    if (!map.isEmpty()) {

                        if (resultDoctos == null) {
                            resultDoctos = new ArrayList();
                        }

                        resultDoctos.add(map);
                    }
                }
            }

        }

        result.put("listaDoctos", resultDoctos);

        return result;

    }

    public List<Conhecimento> findConhecimentosControleCarga(Long idControleCarga) {
        return conhecimentoService.findConhecimentosByControleCarga(idControleCarga);
    }

    /**
     * Método responsável por retornar a soma do valor do frete dos documentos de serviço carregados no controle de carga em questão
     * LMS-2212
     *
     * @param controleCarga
     * @return
     */
    public BigDecimal findSomaVlTotalFreteByControleCarga(ControleCarga controleCarga) {
        if (controleCarga.getTpControleCarga().getValue().equals(COLETA)) {
            return getControleCargaDAO().findSomaVlTotalFreteByIdControleCargaColetaEntrega(controleCarga.getIdControleCarga());
        } else if ("V".equals(controleCarga.getTpControleCarga().getValue())) {
            return getControleCargaDAO().findSomaVlTotalFreteByIdControleCargaViagem(controleCarga.getIdControleCarga());
        }
        return null;
    }

    public void setTabelaColetaEntregaCCService(
            TabelaColetaEntregaCCService tabelaColetaEntregaCCService) {
        this.tabelaColetaEntregaCCService = tabelaColetaEntregaCCService;
    }


    @SuppressWarnings("static-access")
    public ControleCarga generateValidacaoControleCargaParceiraAutomatico(Long idMeioTransporte, Long idRotaColetaEntregaContrCarga, Long idMotorista, Long idFilial) {

        Long idMeioTranspTransportado = null;
        Long idMeioTranspSemiRebocado = null;
        Long idProprietario = null;
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
        ControleCarga controleCarga = new ControleCarga();

		/* Verifica tipo do meio de transporte */
        if (meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte)) {
            controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
            idMeioTranspSemiRebocado = idMeioTransporte;
        } else {
            controleCarga.setMeioTransporteByIdTransportado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
            findIsCavaloTrator(idMeioTransporte);
            idProprietario = executeValidaProprietario(idMeioTransporte);
            idMeioTranspTransportado = idMeioTransporte;
        }

        RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
        rotaColetaEntrega.setIdRotaColetaEntrega(idRotaColetaEntregaContrCarga);
        controleCarga.setRotaColetaEntrega(rotaColetaEntrega);
        controleCarga.setTpControleCarga(new DomainValue(this.CONTROLE_CARGA_COLETA_ENTREGA));

        Long idTipoTabelaColetaEntrega = null;
        Long idTabelaColetaEntrega = null;
        if (!isCalculoPadrao()) {
            List<TabelaColetaEntrega> tabelaColetaEntregaList = tabelaColetaEntregaService.validateTabelaColetaEntrega(idFilial, idMeioTransporte, idRotaColetaEntregaContrCarga);
            if (tabelaColetaEntregaList != null && !tabelaColetaEntregaList.isEmpty()) {
                idTabelaColetaEntrega = tabelaColetaEntregaList.get(0).getIdTabelaColetaEntrega();
                idTipoTabelaColetaEntrega = tabelaColetaEntregaList.get(0).getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega();
            }
        }


        generateControleCarga(controleCarga, idMeioTranspTransportado,
                idMeioTranspSemiRebocado, idMotorista, idProprietario, idTipoTabelaColetaEntrega, idTabelaColetaEntrega, idRotaColetaEntregaContrCarga, null,
                idRotaColetaEntregaContrCarga, null, controleCarga.getTpControleCarga(),
                null, null, null, dataHora, null, false, true, idFilial);

        return controleCarga;
    }

    @SuppressWarnings("rawtypes")
    private Long executeValidaProprietario(Long idMeioTranspTransportado) {
        Map mapResultado = meioTranspProprietarioService.findProprietarioByMeioTransporte(idMeioTranspTransportado);
        Long idProprietario = null;
        if (mapResultado != null) {
            Map mapProprietario = (Map) mapResultado.get("proprietario");
            if (mapProprietario != null) {
                idProprietario = Long.parseLong(mapProprietario.get("idProprietario").toString());
            }
        }

        if (idProprietario == null) {
            throw new BusinessException("LMS-25036");
        }
        return idProprietario;
    }

    private void findIsCavaloTrator(Long idMeioTranspTransportado) {
        if (meioTransporteService.findMeioTransporteIsCavaloTrator(idMeioTranspTransportado)) {
            throw new BusinessException("LMS-45076"); //Não pode iniciar Carregamento com um Cavalo-Trator
        }
    }

    /**
     * @param usuarioService the usuarioService to set
     */
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public void setEventoDispositivoUnitizacaoService(EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
        this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
    }

    public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
        this.carregamentoDescargaService = carregamentoDescargaService;
    }

    public void setFilialRotaService(FilialRotaService filialRotaService) {
        this.filialRotaService = filialRotaService;
    }

    public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
        this.fluxoFilialService = fluxoFilialService;
    }

    public void setProcessoSinistroService(ProcessoSinistroService processoSinistroService) {
        this.processoSinistroService = processoSinistroService;
    }

    public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
        this.manifestoViagemNacionalService = manifestoViagemNacionalService;
    }

    public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
        this.moedaPaisService = moedaPaisService;
    }

    public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
        this.adiantamentoTrechoService = adiantamentoTrechoService;
    }

    public void setTrechoCorporativoService(TrechoCorporativoService trechoCorporativoService) {
        this.trechoCorporativoService = trechoCorporativoService;
    }

    public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
        this.lacreControleCargaService = lacreControleCargaService;
    }

    public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
        this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setSolicMonitPreventivoService(SolicMonitPreventivoService solicMonitPreventivoService) {
        this.solicMonitPreventivoService = solicMonitPreventivoService;
    }

    public void setEventoManifestoColetaService(EventoManifestoColetaService eventoManifestoColetaService) {
        this.eventoManifestoColetaService = eventoManifestoColetaService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
        this.eventoManifestoService = eventoManifestoService;
    }

    public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
        this.gerarEnviarSMPService = gerarEnviarSMPService;
    }

    public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
        this.tipoMeioTransporteService = tipoMeioTransporteService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
        this.manifestoColetaService = manifestoColetaService;
    }

    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    public void setFeriadoService(FeriadoService feriadoService) {
        this.feriadoService = feriadoService;
    }

    public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
        this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
    }

    public void setRotaService(RotaService rotaService) {
        this.rotaService = rotaService;
    }

    public void setReciboPostoPassagemService(ReciboPostoPassagemService reciboPostoPassagemService) {
        this.reciboPostoPassagemService = reciboPostoPassagemService;
    }

    public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
        this.filialRotaCcService = filialRotaCcService;
    }

    public void setMotivoCancelamentoCcService(MotivoCancelamentoCcService motivoCancelamentoCcService) {
        this.motivoCancelamentoCcService = motivoCancelamentoCcService;
    }

    public void setCartaoPedagioService(CartaoPedagioService cartaoPedagioService) {
        this.cartaoPedagioService = cartaoPedagioService;
    }

    public void setOperadoraCartaoPedagioService(OperadoraCartaoPedagioService operadoraCartaoPedagioService) {
        this.operadoraCartaoPedagioService = operadoraCartaoPedagioService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setLmControleCargaDao(LMControleCargaDAO lmControleCargaDao) {
        this.lmControleCargaDao = lmControleCargaDao;
    }

    public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
        this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
    }

    public void setSolicitacaoSinalService(SolicitacaoSinalService solicitacaoSinalService) {
        this.solicitacaoSinalService = solicitacaoSinalService;
    }

    public void setControleTrechoService(ControleTrechoService controleTrechoService) {
        this.controleTrechoService = controleTrechoService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
        this.rotaColetaEntregaService = rotaColetaEntregaService;
    }

    public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
        this.rotaIdaVoltaService = rotaIdaVoltaService;
    }

    public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
        this.solicitacaoContratacaoService = solicitacaoContratacaoService;
    }

    public void setTipoPagamPostoPassagemService(TipoPagamPostoPassagemService tipoPagamPostoPassagemService) {
        this.tipoPagamPostoPassagemService = tipoPagamPostoPassagemService;
    }

    public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
        this.eventoMeioTransporteService = eventoMeioTransporteService;
    }

    public void setLiberacaoReguladoraService(LiberacaoReguladoraService liberacaoReguladoraService) {
        this.liberacaoReguladoraService = liberacaoReguladoraService;
    }

    public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
        this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
    }

    public void setTabelaColetaEntregaService(TabelaColetaEntregaService tabelaColetaEntregaService) {
        this.tabelaColetaEntregaService = tabelaColetaEntregaService;
    }

    public void setTipoTabelaColetaEntregaService(TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService) {
        this.tipoTabelaColetaEntregaService = tipoTabelaColetaEntregaService;
    }

    public void setMotoristaService(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    public void setProprietarioService(ProprietarioService proprietarioService) {
        this.proprietarioService = proprietarioService;
    }

    public void setEquipeService(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
        this.postoPassagemCcService = postoPassagemCcService;
    }

    public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
        this.pagtoPedagioCcService = pagtoPedagioCcService;
    }

    public void setMotoristaControleCargaService(MotoristaControleCargaService motoristaControleCargaService) {
        this.motoristaControleCargaService = motoristaControleCargaService;
    }

    public void setSemiReboqueCcService(SemiReboqueCcService semiReboqueCcService) {
        this.semiReboqueCcService = semiReboqueCcService;
    }

    public void setPagtoProprietarioCcService(PagtoProprietarioCcService pagtoProprietarioCcService) {
        this.pagtoProprietarioCcService = pagtoProprietarioCcService;
    }

    public void setVeiculoControleCargaService(VeiculoControleCargaService veiculoControleCargaService) {
        this.veiculoControleCargaService = veiculoControleCargaService;
    }

    public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
        this.controleQuilometragemService = controleQuilometragemService;
    }

    public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }

    public void setEventoColetaService(EventoColetaService eventoColetaService) {
        this.eventoColetaService = eventoColetaService;
    }

    public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
        this.ordemSaidaService = ordemSaidaService;
    }

    public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
        this.equipeOperacaoService = equipeOperacaoService;
    }

    public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
        this.meioTransporteService = meioTransporteService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

    public void setEventoWorkflowService(EventoWorkflowService eventoWorkflowService) {
        this.eventoWorkflowService = eventoWorkflowService;
    }

    public void setPendenciaService(PendenciaService pendenciaService) {
        this.pendenciaService = pendenciaService;
    }

    public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
        this.meioTranspProprietarioService = meioTranspProprietarioService;
    }

    public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
        this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
    }

    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public void setParcelaTabelaCeService(ParcelaTabelaCeService parcelaTabelaCeService) {
        this.parcelaTabelaCeService = parcelaTabelaCeService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setManifestoEletronicoService(ManifestoEletronicoService manifestoEletronicoService) {
        this.manifestoEletronicoService = manifestoEletronicoService;
    }

    public void setCarregamentoDescargaVolumeService(
            CarregamentoDescargaVolumeService carregamentoDescargaVolumeService) {
        this.carregamentoDescargaVolumeService = carregamentoDescargaVolumeService;
    }

    class ControleCargaValido {
        private boolean isInvalid = true;
        private ControleCarga controleCarga;

        public ControleCarga getControleCarga() {
            return controleCarga;
        }

        public void setControleCarga(ControleCarga controleCarga) {
            this.controleCarga = controleCarga;
        }

        public boolean isInvalid() {
            return isInvalid;
        }

        public void setInvalid(boolean isInvalid) {
            this.isInvalid = isInvalid;
        }
    }

    public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
        return localizacaoMercadoriaService;
    }

    public ControleCarga findControleCargaByPedidoColeta(Long idPedidoColeta) {
        return getControleCargaDAO().findControleCargaByPedidoColeta(idPedidoColeta);
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    @SuppressWarnings("unchecked")
    public BigDecimal findValorTotalFrota(Long idControleCarga) {
        BigDecimal valorTotalFrota = BigDecimal.ZERO;
        List<Object[]> totalFrotaList = getControleCargaDAO().findValorTotalFrota(idControleCarga);

        for (Object[] obj : totalFrotaList) {
            valorTotalFrota = valorTotalFrota.add(obj[ARR_POS_4] != null ? (BigDecimal) obj[ARR_POS_4] : BigDecimal.ZERO);
        }

        return valorTotalFrota;
    }

    public ControleCarga findControleCargaByDoctoServicoDetalheColeta(Long idDoctoServico) {
        return getControleCargaDAO().findControleCargaByDoctoServico(idDoctoServico);
    }

    @SuppressWarnings("rawtypes")
    public List findControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
        return getControleCargaDAO().findControleCarga(idControleCarga, idFilialOrigem, idFilialDestino);
    }

    /**
     * Verificar se existe controle de carga de coleta/entrega da filial do
     * usuário logado, sem nota de crédito, com emissão no período informado,
     * que proprietário não seja próprio, nem empresa parceira, que não esteja cancelado.
     * Se existir: apresentar a mensagem: LMS-25133.
     */
    public void validateControleCargaSemNotaCredito(Long idFilial, Long idProprietario, Long idMeioTransporte, YearMonthDay dataRetroativaMinima, YearMonthDay dataRetroativaMaxima) {
        Boolean existeCCSemNotaCredito = getControleCargaDAO().validateControleCargaSemNotaCredito(idFilial, idProprietario, idMeioTransporte, dataRetroativaMinima, dataRetroativaMaxima);
        if (existeCCSemNotaCredito) {
            throw new BusinessException("LMS-25133");
        }
    }

    /**
     * Consulta se existe registro de lançamento de quilomatragem através do controle de carga e filial.
     * Caso a consulta retorne registros, ocorreu a saída.
     */
    public boolean findLancamentoQuilometragem(Long idControleCarga, Long idFilial, Boolean possuiSaida) {
        boolean isExisteLancamentoQuilometragem = false;

        List<ControleCarga> controleCargaList = getControleCargaDAO().findLancamentoQuilometragem(idControleCarga, idFilial, possuiSaida);

        if (!controleCargaList.isEmpty()) {
            isExisteLancamentoQuilometragem = true;
        }

        return isExisteLancamentoQuilometragem;
    }

    /**
     * FIXME corrigir número do JIRA
     * <p>
     * LMS-???? (Tela para simulação do Plano de Gerenciamento de Risco) - Busca
     * quantidade de {@link ControleCarga} conforme parâmetros de filtro da tela
     * "Simular Plano de Gerenciamento de Risco".
     *
     * @param criteria Filtro incluindo {@code idControleCarga} ou início/término
     *                 para {@code dhGeracao}.
     * @return Quantidade de {@link ControleCarga}s.
     */
    public Integer getRowCountByControleCargaByDhGeracao(TypedFlatMap criteria) {
        return getControleCargaDAO().getRowCountByControleCargaByDhGeracao(criteria);
    }

    /**
     * FIXME corrigir número do JIRA
     * <p>
     * LMS-???? (Tela para simulação do Plano de Gerenciamento de Risco) - Busca
     * {@link ControleCarga}s conforme parâmetros de filtro da tela
     * "Simular Plano de Gerenciamento de Risco".
     *
     * @param criteria Filtro incluindo {@code idControleCarga} ou início/término
     *                 para {@code dhGeracao}.
     * @return {@link ControleCarga}s conforme parâmetros de filtro.
     */
    public List<ControleCarga> findByControleCargaByDhGeracao(TypedFlatMap criteria) {
        return getControleCargaDAO().findByControleCargaByDhGeracao(criteria);
    }

    public ControleCarga findDadosParaCiot(Long idControleCarga) {
        return getControleCargaDAO().findDadosParaCiot(idControleCarga);
    }

    public ControleCarga findDadosParaAlteracaoCiot(Long idControleCarga) {
        return getControleCargaDAO().findDadosParaAlteracaoCiot(idControleCarga);
    }

    public Boolean verificaSaidaPortariaDiaPosterior(DateTime dia, Long idDoctoServico) {
        return getControleCargaDAO().verificaSaidaPortariaDiaPosterior(dia, idDoctoServico);
    }

    public boolean validateExistePreManifestoDocumentoPreManifestoVolume(Long idControleCarga, Long idDoctoServico, Long idFilial) {
        return getControleCargaDAO().validateExistePreManifestoDocumentoPreManifestoVolume(idControleCarga, idDoctoServico, idFilial);
    }

    public void setPlanoGerenciamentoRiscoService(
            PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
        this.planoGerenciamentoRiscoService = planoGerenciamentoRiscoService;
    }

    public void setPlanoUtils(PlanoGerenciamentoRiscoUtils planoUtils) {
        this.planoUtils = planoUtils;
    }

    public void setPerifericoRastreadorService(PerifericoRastreadorService perifericoRastreadorService) {
        this.perifericoRastreadorService = perifericoRastreadorService;
    }

    public void setVirusCargaService(VirusCargaService virusCargaService) {
        this.virusCargaService = virusCargaService;
    }

    public void setCiotService(CIOTService ciotService) {
        this.ciotService = ciotService;
    }

    public void setCiotControleCargaService(
            CIOTControleCargaService ciotControleCargaService) {
        this.ciotControleCargaService = ciotControleCargaService;
    }

    public List<Long> findIdControleCargaFechaManifestoAuto(Long idFilial){
        String dateString = parametroGeralService.findSimpleConteudoByNomeParametro("DT_INICIO_FECH_AUT_MANIF_FDX");

        List<Long> listControleCarga = this.findIdControleCargaFechaManifestoAuto(idFilial, dateString);
        return listControleCarga;
    }

    public synchronized Boolean executeFinalizaManifesto(Long idContoleCarga) {
        try {
            ControleCarga controleCarga = findById(idContoleCarga);

            if(!controleCarga.getTpStatusControleCarga().getValue().equals("TC")){
                return Boolean.TRUE;
            }

            Usuario usuarioIntegracao = new Usuario();
            usuarioIntegracao.setIdUsuario(5000l);
            Usuario usuario = usuarioService.findById(usuarioIntegracao.getIdUsuario());
            Pais pais = paisService.findByIdPessoa(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
            volDadosSessaoService.executeDadosSessaoBanco(usuario, controleCarga.getFilialByIdFilialOrigem(), pais);

            eventoControleCargaService.storeEventoControleCarga(controleCarga, "ID");
            eventoControleCargaService.storeEventoControleCarga(controleCarga, "FD");
            eventoControleCargaService.storeEventoControleCarga(controleCarga, "CP");

            for (Manifesto manifesto : controleCarga.getManifestos()) {
                eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), "CP");
                eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), "ID");
                eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), "FD");
                eventoManifestoService.generateEventoManifesto(manifesto, SessionUtils.getFilialSessao(), "FE");

                ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
                manifestoEntrega.setUsuarioFechamento(usuario);
                manifestoEntrega.setDhFechamento(JTDateTimeUtils.getDataHoraAtual());
                manifestoEntregaService.store(manifestoEntrega);

                manifesto.setTpStatusManifesto(new DomainValue("FE"));
                manifestoService.storeManifesto(manifesto);
            }

            CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
            carregamentoDescarga.setTpOperacao(new DomainValue("D"));
            carregamentoDescarga.setTpStatusOperacao(new DomainValue("F"));
            carregamentoDescarga.setDhInicioOperacao(JTDateTimeUtils.getDataHoraAtual());
            carregamentoDescarga.setDhFimOperacao(JTDateTimeUtils.getDataHoraAtual());
            carregamentoDescarga.setControleCarga(controleCarga);
            carregamentoDescarga.setFilial(SessionUtils.getFilialSessao());
            carregamentoDescarga.setBox(null);
            carregamentoDescarga.setUsuarioByIdUsuarioIniciado(usuario);
            carregamentoDescarga.setUsuarioByIdUsuarioFinalizado(usuario);

            carregamentoDescargaService.store(carregamentoDescarga);

            controleCarga.setTpStatusControleCarga(new DomainValue("FE"));

            this.store(controleCarga);

        }catch (Exception e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private boolean isDataMaior(YearMonthDay date1, YearMonthDay date2) {
		return JTDateTimeUtils.comparaData(date1, date2) > 0;
	}
    
    public List<Long> findIdControleCargaFechaManifestoAuto(Long idFilial, String dhInicioFechamentoAutomatico) {
    	return getControleCargaDAO().findIdControleCargaFechaManifestoAuto(idFilial, dhInicioFechamentoAutomatico);
    }

	@SuppressWarnings("unchecked")
	public void gerarManifestoFedex(Long idDoctoServico, Long idfilialDestino) {

		Moeda real = new Moeda();
		real.setIdMoeda(1l);

		Usuario usuarioIntegracao = new Usuario();
		usuarioIntegracao.setIdUsuario(ID_USUARIO_INTEGRACAO);

		String cpfMotoristaFedex = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(idfilialDestino, "MOTORISTA_FILIAL_FDX", false);

		Motorista motoristaFedex = null;
		Pessoa pessoaMotorista = pessoaService.findByNrIdentificacao(cpfMotoristaFedex);
		if (pessoaMotorista != null) {
			motoristaFedex = motoristaService.findById(pessoaMotorista.getIdPessoa());
		}

		String placaMeioTransporteFedex = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(idfilialDestino, "PLACA_VEICULO_FDX", false);
		MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(placaMeioTransporteFedex);

		MeioTransporteRodoviario meioTransporteRodoviario = meioTransporteRodoviarioService.findById(meioTransporte.getIdMeioTransporte());
		Proprietario proprietarioFedex = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(meioTransporte.getIdMeioTransporte(), JTDateTimeUtils.getDataAtual());

		RotaColetaEntrega rota = rotaColetaEntregaService.findRotaColetaEntrega(idfilialDestino, Short.valueOf((String)configuracoesFacade.getValorParametro("NR_ROTA_ENTREGA_FAAV")));

		Filial filialDestino = filialService.findById(idfilialDestino);
		
		Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);

		ControleCarga cc = null;
		Manifesto manifesto = null;
		ManifestoEntrega manifestoEntrega = null;

		VeiculoControleCarga veiculoControleCarga = null;
		MotoristaControleCarga motoristaControleCarga = null;
		ControleQuilometragem quilometragem = null;

		List<Long> listIdsManifesto = manifestoService.findManifestoEntregaParceira(conhecimento.getIdDoctoServico(), conhecimento.getFilialByIdFilialDestino().getIdFilial());
		boolean existeManifesto = CollectionUtils.isNotEmpty(listIdsManifesto);

		if (existeManifesto) {
			List<VolumeNotaFiscal> volumesConhecimento = volumeNotaFiscalService.findfindByIdConhecimento(conhecimento.getIdDoctoServico());
			manifesto = manifestoService.findById(listIdsManifesto.get(0));
			
			ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
			manifestoEntregaDocumento.setManifestoEntrega(manifesto.getManifestoEntrega());
			manifestoEntregaDocumento.setDoctoServico(conhecimento);
			manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue("PBAI"));
			manifestoEntregaDocumento.setUsuario(usuarioIntegracao);
			manifestoEntregaDocumento.setDhInclusao(new DateTime());
			manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(Boolean.FALSE);

			PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
			preManifestoDocumento.setManifesto(manifesto);
			preManifestoDocumento.setDoctoServico(conhecimento);
			preManifestoDocumento.setNrOrdem(1);
			preManifestoDocumento.setVersao(0);

			List<ManifestoEntregaVolume> listManifestoEntregaVolumes = new ArrayList<ManifestoEntregaVolume>();
			List<PreManifestoVolume> listPreManifestoVolumes = new ArrayList<PreManifestoVolume>();
			
			for (VolumeNotaFiscal volumeNotaFiscal : volumesConhecimento) {
				ManifestoEntregaVolume manifestoEntregaVolume = new ManifestoEntregaVolume();
				manifestoEntregaVolume.setManifestoEntrega(manifesto.getManifestoEntrega());
				manifestoEntregaVolume.setDoctoServico(conhecimento);
				manifestoEntregaVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				manifestoEntregaVolume.setManifestoEntregaDocumento(manifestoEntregaDocumento);
				listManifestoEntregaVolumes.add(manifestoEntregaVolume);
				
				PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
				preManifestoVolume.setManifesto(manifesto);
				preManifestoVolume.setDoctoServico(conhecimento);
				preManifestoVolume.setPreManifestoDocumento(preManifestoDocumento);
				preManifestoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				preManifestoVolume.setTpScan(new DomainValue("LM"));
				preManifestoVolume.setVersao(0);
				listPreManifestoVolumes.add(preManifestoVolume);
			}
			
			this.generateControleCargaManifestoAutomaticoAux(manifesto, manifestoEntregaDocumento, preManifestoDocumento, listManifestoEntregaVolumes, listPreManifestoVolumes, usuarioIntegracao);
			
		} else {

			cc = buildControleCarga(real, filialDestino, meioTransporte, proprietarioFedex, motoristaFedex, rota);

			veiculoControleCarga = new VeiculoControleCarga();
			veiculoControleCarga.setControleCarga(cc);
			veiculoControleCarga.setMeioTransporte(meioTransporte);

			motoristaControleCarga = new MotoristaControleCarga();
			motoristaControleCarga.setMotorista(motoristaFedex);
			motoristaControleCarga.setControleCarga(cc);

			quilometragem = new ControleQuilometragem();
			quilometragem.setFilial(filialDestino);
			quilometragem.setMeioTransporteRodoviario(meioTransporteRodoviario);

			DateTime dhLiberacaoRomaneio = new DateTime(filialDestino.getDateTimeZone());
			quilometragem.setDhMedicao(dhLiberacaoRomaneio);
			quilometragem.setNrQuilometragem(0);
			quilometragem.setBlVirouHodometro(Boolean.FALSE);
			quilometragem.setBlSaida(Boolean.TRUE);
			quilometragem.setUsuarioByIdUsuario(usuarioIntegracao);
			quilometragem.setControleCarga(cc);

			manifesto = buildManifesto(real, filialDestino);
			totalizaAtributosDocumentosManifesto(manifesto, conhecimento);

			manifesto.setControleCarga(cc);
			manifesto.setPreManifestoDocumentos(new ArrayList<PreManifestoDocumento>());
			manifesto.setPreManifestoVolumes(new ArrayList<PreManifestoVolume>());

			manifestoEntrega = new ManifestoEntrega();
			manifestoEntrega.setManifesto(manifesto);
			manifestoEntrega.setFilial(filialDestino);
			manifestoEntrega.setDhEmissao(dhLiberacaoRomaneio);
			manifestoEntrega.setObManifestoEntrega(configuracoesFacade.getMensagem("observacaoManifestoAutomatico"));
			manifestoEntrega.setManifestoEntregaDocumentos(new ArrayList<ManifestoEntregaDocumento>());
			manifestoEntrega.setManifestoEntregaVolumes(new ArrayList<ManifestoEntregaVolume>());
			manifesto.setManifestoEntrega(manifestoEntrega);
			
			ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
			manifestoEntregaDocumento.setManifestoEntrega(manifestoEntrega);
			manifestoEntregaDocumento.setDoctoServico(conhecimento);
			manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue("PBAI"));
			manifestoEntregaDocumento.setUsuario(usuarioIntegracao);
			manifestoEntregaDocumento.setDhInclusao(new DateTime());
			manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(Boolean.FALSE);
			manifestoEntrega.getManifestoEntregaDocumentos().add(manifestoEntregaDocumento);

			PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
			preManifestoDocumento.setManifesto(manifesto);
			preManifestoDocumento.setDoctoServico(conhecimento);
			preManifestoDocumento.setNrOrdem(1);
			preManifestoDocumento.setVersao(0);
			manifesto.getPreManifestoDocumentos().add(preManifestoDocumento);

			List<VolumeNotaFiscal> volumesConhecimento = volumeNotaFiscalService.findfindByIdConhecimento(conhecimento.getIdDoctoServico());
			for (VolumeNotaFiscal volumeNotaFiscal : volumesConhecimento) {
				ManifestoEntregaVolume manifestoEntregaVolume = new ManifestoEntregaVolume();
				manifestoEntregaVolume.setManifestoEntrega(manifestoEntrega);
				manifestoEntregaVolume.setDoctoServico(conhecimento);
				manifestoEntregaVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				manifestoEntregaVolume.setManifestoEntregaDocumento(manifestoEntregaDocumento);
				manifestoEntrega.getManifestoEntregaVolumes().add(manifestoEntregaVolume);
				PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
				preManifestoVolume.setManifesto(manifesto);
				preManifestoVolume.setDoctoServico(conhecimento);
				preManifestoVolume.setPreManifestoDocumento(preManifestoDocumento);
				preManifestoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				preManifestoVolume.setTpScan(new DomainValue("LM"));
				preManifestoVolume.setVersao(0);
				manifesto.getPreManifestoVolumes().add(preManifestoVolume);
			}
			
			cc.setManifestos(new ArrayList<Manifesto>());
			cc.getManifestos().add(manifesto);
			this.generateControleCargaManifestoAutomatico(cc, veiculoControleCarga, motoristaControleCarga, quilometragem, usuarioIntegracao);

		}

	}

	private ControleCarga buildControleCarga( Moeda real, Filial filialFedex, MeioTransporte meioTransporte, Proprietario proprietarioFedex,
			Motorista motoristaFedex, RotaColetaEntrega rota) {

		DateTime dataLiberacaoRomaneio = new DateTime(filialFedex.getDateTimeZone());

		ControleCarga cc = new ControleCarga();
		cc.setIdControleCarga(null);
		cc.setFilialByIdFilialOrigem(filialFedex);
		cc.setFilialByIdFilialAtualizaStatus(filialFedex);
		cc.setProprietario(proprietarioFedex);
		cc.setMotorista(motoristaFedex);
		cc.setTpControleCarga(new DomainValue("C"));
		cc.setTpStatusControleCarga(new DomainValue("TC"));
		cc.setFilialByIdFilialDestino(filialFedex);
		cc.setMoeda(real);
		cc.setDhSaidaColetaEntrega(dataLiberacaoRomaneio);
		cc.setDhGeracao(dataLiberacaoRomaneio);
		cc.setMeioTransporteByIdTransportado(meioTransporte);
		cc.setBlEntregaDireta(Boolean.FALSE);
		cc.setRotaColetaEntrega(rota);
		return cc;
	}

	private Manifesto buildManifesto(Moeda real, Filial filialFedex) {
		Manifesto manifesto = new Manifesto();
		DateTime dataLiberacaoRomaneio = new DateTime(filialFedex.getDateTimeZone());

		manifesto.setFilialByIdFilialOrigem(filialFedex);
		manifesto.setFilialByIdFilialDestino(filialFedex);
		manifesto.setDhGeracaoPreManifesto(dataLiberacaoRomaneio);
		manifesto.setTpManifesto(new DomainValue("E"));
		manifesto.setMoeda(real);
		manifesto.setDhEmissaoManifesto(dataLiberacaoRomaneio);
		manifesto.setTpModal(new DomainValue("R"));
		manifesto.setTpManifestoEntrega(new DomainValue("EP"));
		manifesto.setTpAbrangencia(new DomainValue("N"));
		manifesto.setTpStatusManifesto(new DomainValue("TC"));
		manifesto.setVersao(8);
		manifesto.setBlBloqueado(Boolean.FALSE);
		return manifesto;
	}

	private void totalizaAtributosDocumentosManifesto(Manifesto manifesto, Conhecimento conhecimento) {
		BigDecimal vlTotalManifesto = BigDecimalUtils.defaultBigDecimal(manifesto.getVlTotalManifesto());
		BigDecimal psTotalManifesto = BigDecimalUtils.defaultBigDecimal(manifesto.getPsTotalManifesto());
		BigDecimal psTotalAforadoManifesto = BigDecimalUtils.defaultBigDecimal(manifesto.getPsTotalAforadoManifesto());
		BigDecimal vlTotalManifestoEmissao = BigDecimalUtils.defaultBigDecimal(manifesto.getVlTotalManifestoEmissao());
		BigDecimal psTotalManifestoEmissao = BigDecimalUtils.defaultBigDecimal(manifesto.getPsTotalManifestoEmissao());
		BigDecimal vlTotalFreteEmissao = BigDecimalUtils.defaultBigDecimal(manifesto.getVlTotalFreteEmissao());
		Integer qtTotalVolumesEmissao = IntegerUtils.defaultInteger(manifesto.getQtTotalVolumesEmissao());

		vlTotalManifesto = vlTotalManifesto.add(conhecimento.getVlMercadoria());
		psTotalManifesto = psTotalManifesto.add(conhecimento.getPsReal());
		psTotalAforadoManifesto = psTotalAforadoManifesto.add(conhecimento.getPsAforado());
		vlTotalManifestoEmissao = vlTotalManifestoEmissao.add(conhecimento.getVlMercadoria());
		psTotalManifestoEmissao = psTotalManifestoEmissao.add(conhecimento.getPsReal());
		vlTotalFreteEmissao = vlTotalFreteEmissao.add(conhecimento.getVlTotalDocServico());
		qtTotalVolumesEmissao += conhecimento.getQtVolumes();

		manifesto.setVlTotalManifesto(vlTotalManifesto);
		manifesto.setPsTotalManifesto(psTotalManifesto);
		manifesto.setPsTotalAforadoManifesto(psTotalAforadoManifesto);
		manifesto.setVlTotalManifestoEmissao(vlTotalManifestoEmissao);
		manifesto.setPsTotalManifestoEmissao(psTotalManifestoEmissao);
		manifesto.setVlTotalFreteEmissao(vlTotalFreteEmissao);
		manifesto.setQtTotalVolumesEmissao(qtTotalVolumesEmissao);

	}

	@SuppressWarnings("unchecked")
	public void generateControleCargaManifestoAutomaticoAux(
			Manifesto manifesto,
			ManifestoEntregaDocumento manifestoEntregaDocumento, 
			PreManifestoDocumento preManifestoDocumento, 
			List<ManifestoEntregaVolume> listManifestoEntregaVolumes, 
			List<PreManifestoVolume> listPreManifestoVolumes, 
			Usuario usuarioIntegracao) {
		

		Session session = getControleCargaDAO().getAdsmHibernateTemplate().getSessionFactory().openSession();
		session.beginTransaction();

		Usuario usuario = usuarioService.findById(usuarioIntegracao.getIdUsuario());
		
		ControleCarga controleCarga = manifesto.getControleCarga();

		Pais pais = paisService.findByIdPessoa(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
		volDadosSessaoService.executeDadosSessaoBanco(usuario, controleCarga.getFilialByIdFilialOrigem(), pais);

		preManifestoDocumentoService.store(preManifestoDocumento);
		preManifestoVolumeService.storeAll(listPreManifestoVolumes);

		manifestoEntregaDocumentoService.store(manifestoEntregaDocumento);
		manifestoEntregaVolumeService.storeAll(listManifestoEntregaVolumes);

		try {
			generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga, false, SessionUtils.getFilialSessao());
		} catch (BusinessException be) {
			// Não faz nada, deve ignorar as validações neste momento
		}

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("62"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(),
				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getManifestoEntrega().getNrManifestoEntrega().toString(), new DateTime(), null,
				controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(), "PME");

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("24"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(),
				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getManifestoEntrega().getNrManifestoEntrega().toString(), new DateTime(), null,
				controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(), "PME");

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("27"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(),
				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getManifestoEntrega().getNrManifestoEntrega().toString(), new DateTime(), null,
				controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(), "PME");

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("115"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(),
				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getManifestoEntrega().getNrManifestoEntrega().toString(), new DateTime(), null,
				controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(), "MAE");

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("56"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(),
				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), controleCarga.getNrControleCarga().toString(), new DateTime(), null,
				controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(), "CCA");

		for (Object object : listManifestoEntregaVolumes) {
			ManifestoEntregaVolume manifestoEntregaVolume = (ManifestoEntregaVolume) object;
			if (manifestoEntregaVolume.getManifestoEntregaDocumento().getIdManifestoEntregaDocumento().longValue() == manifestoEntregaDocumento.getIdManifestoEntregaDocumento()
					.longValue()) {
				incluirEventosRastreabilidadeInternacionalService.generateEventoVolume(manifestoEntregaVolume.getVolumeNotaFiscal(), Short.valueOf("56"));
			}
		}

		try {
			session.getTransaction().commit();
		} catch (Exception ex) {
			throw new IllegalStateException(ex.getMessage());
		} finally {
			if (session != null) {
				session.flush();
				session.close();
			}
		}

	}

	
	public void generateControleCargaManifestoAutomatico(ControleCarga cc,
			VeiculoControleCarga veiculoControleCarga,
			MotoristaControleCarga motoristaControleCarga,
			ControleQuilometragem quilometragem, Usuario usuarioIntegracao
			) {
		generateControleCargaManifestoAutomatico(cc, veiculoControleCarga, motoristaControleCarga, quilometragem, usuarioIntegracao, true);
	}
	
    @SuppressWarnings("unchecked")
    public void generateControleCargaManifestoAutomatico(ControleCarga controleCarga, VeiculoControleCarga veiculoControleCarga,
            MotoristaControleCarga motoristaControleCarga, ControleQuilometragem quilometragem, Usuario usuarioIntegracao, boolean blGeraEventos) {
         
        Usuario usuario = usuarioService.findById(usuarioIntegracao.getIdUsuario());

        Pais pais = paisService.findByIdPessoa(controleCarga.getFilialByIdFilialOrigem().getIdFilial());           
        volDadosSessaoService.executeDadosSessaoBanco(usuario, controleCarga.getFilialByIdFilialOrigem(), pais);
        
        controleCarga.setNrControleCarga(configuracoesFacade.incrementaParametroSequencial(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "NR_CONTROLE_CARGA", true));
        store(controleCarga);
        
        for (Manifesto manifesto:controleCarga.getManifestos()){
            manifesto.setNrPreManifesto(configuracoesFacade.incrementaParametroSequencial(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), PedidoColetaServiceConstants.NR_PRE_MANIFESTO, true));
            
            manifesto.getManifestoEntrega().setNrManifestoEntrega(Integer.valueOf(configuracoesFacade.incrementaParametroSequencial(
                    controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "NR_MANIFESTO_ENTREGA", true).intValue()));
            manifestoService.storeManifesto(manifesto);
            preManifestoDocumentoService.storeAll(manifesto.getPreManifestoDocumentos());
            preManifestoVolumeService.storeAll(manifesto.getPreManifestoVolumes());
            
            ManifestoEntrega manifestoEntrega =manifesto.getManifestoEntrega(); 
            manifestoEntregaService.store(manifestoEntrega);
            manifestoEntregaDocumentoService.storeAll(manifestoEntrega.getManifestoEntregaDocumentos());
            manifestoEntregaVolumeService.storeAll(manifestoEntrega.getManifestoEntregaVolumes());
        }
        
        veiculoControleCargaService.store(veiculoControleCarga);
        motoristaControleCargaService.store(motoristaControleCarga);
        controleQuilometragemService.store(quilometragem);
        
        try{
            generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga,false, SessionUtils.getFilialSessao());
        }catch(BusinessException be){
            //Não faz nada, deve ignorar as validações neste momento
        }
        store(controleCarga);
        
        EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
        eventoMeioTransporte.setControleCarga(controleCarga);
        eventoMeioTransporte.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
        eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue("EMCE"));
        eventoMeioTransporte.setFilial(controleCarga.getFilialByIdFilialOrigem());
        eventoMeioTransporte.setDhFimEvento(controleCarga.getDhGeracao());
        eventoMeioTransporte.setDhInicioEvento(controleCarga.getDhGeracao());
        eventoMeioTransporte.setDhGeracao(controleCarga.getDhGeracao());
        eventoMeioTransporteService.store(eventoMeioTransporte);
        
        generateEventoControleCargaManifestoAutomatico(controleCarga, "GE", usuarioIntegracao, controleCarga.getDhGeracao());
        generateEventoControleCargaManifestoAutomatico(controleCarga, "IC", usuarioIntegracao, controleCarga.getDhGeracao());
        generateEventoControleCargaManifestoAutomatico(controleCarga, "FC", usuarioIntegracao, controleCarga.getDhGeracao());
        generateEventoControleCargaManifestoAutomatico(controleCarga, "EM", usuarioIntegracao, controleCarga.getDhGeracao());
        generateEventoControleCargaManifestoAutomatico(controleCarga, "SP", usuarioIntegracao, controleCarga.getDhGeracao());
        
        if (blGeraEventos){
        	
        for (Manifesto manifesto: controleCarga.getManifestos()){
            ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
            for (ManifestoEntregaDocumento manifestoEntregaDocumento: manifestoEntrega.getManifestoEntregaDocumentos()){
                
                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        Short.valueOf("62"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                        manifestoEntrega.getNrManifestoEntrega().toString(), controleCarga.getDhGeracao(), null, controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(),
                        "PME");
                
                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        Short.valueOf("24"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                        manifestoEntrega.getNrManifestoEntrega().toString(), controleCarga.getDhGeracao(), null, controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(),
                        "PME");
                
                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        Short.valueOf("27"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                        manifestoEntrega.getNrManifestoEntrega().toString(), controleCarga.getDhGeracao(), null, controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(),
                        "PME");
                
                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        Short.valueOf("115"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                        manifestoEntrega.getNrManifestoEntrega().toString(), controleCarga.getDhGeracao(), null, controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(),
                        "MAE");
                
                incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                        Short.valueOf("56"), manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(),
                        controleCarga.getNrControleCarga().toString(), controleCarga.getDhGeracao(), null, controleCarga.getFilialByIdFilialOrigem().getSiglaNomeFilial(),
                        "CCA");
                
            }
            
            for (Object object: manifestoEntrega.getManifestoEntregaVolumes()){
                ManifestoEntregaVolume manifestoEntregaVolume = (ManifestoEntregaVolume) object;
                incluirEventosRastreabilidadeInternacionalService.generateEventoVolume(manifestoEntregaVolume.getVolumeNotaFiscal(), Short.valueOf("56"));
            }
        }
        
            }
        }
        
    public void generateEventoControleCargaManifestoAutomatico(ControleCarga controleCarga, String tpEvento, Usuario usuario, DateTime dhEvento){
        EventoControleCarga eventoControleCarga = new EventoControleCarga();
        eventoControleCarga.setControleCarga(controleCarga);
        eventoControleCarga.setFilial(controleCarga.getFilialByIdFilialOrigem());
        eventoControleCarga.setDhEvento(dhEvento);
        eventoControleCarga.setTpEventoControleCarga(new DomainValue(tpEvento));
        eventoControleCarga.setUsuario(usuario);
        eventoControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
        eventoControleCarga.setMoeda(SessionUtils.getMoedaSessao());
        
        if ("EM".equals(tpEvento)) {
            eventoControleCarga.setPsReal(controleCarga.getPsTotalFrota());
            eventoControleCarga.setPsAforado(controleCarga.getPsTotalAforado());
            eventoControleCarga.setVlTotal(controleCarga.getVlTotalFrota());
            eventoControleCarga.setPcOcupacaoCalculado(controleCarga.getPcOcupacaoCalculado());
            eventoControleCarga.setPcOcupacaoAforadoCalculado(controleCarga.getPcOcupacaoAforadoCalculado());
            eventoControleCarga.setPcOcupacaoInformado(controleCarga.getPcOcupacaoInformado());
        }

        if ("IC".equals(tpEvento)) {
            eventoControleCarga.setDsEvento(configuracoesFacade.getMensagem("inicioCarregamento"));
        } else if ("FC".equals(tpEvento)) {
            eventoControleCarga.setDsEvento(configuracoesFacade.getMensagem("fimCarregamento"));
        }

        eventoControleCargaService.store(eventoControleCarga);
    }
    
    
    
    // LMSA-6159 LMSA-6249
    public void generateNotFisFedEXAgainByControleCarga(TypedFlatMap criteria) {
        Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
        // validar existencia de evento de documento do controle de carga. 
        // Nao existindo devolver uma mensagem de negocio
        String paramEventosReenvNotfis = (String) parametroGeralService.findConteudoByNomeParametro(EVENTOS_REENVIO_NOTFIS_CC_FAAV, false);
    	List<Short> cdsEventosReenvNotfis = new ArrayList<Short>();
    	for(String cdEvento : Arrays.asList(paramEventosReenvNotfis.split(","))){
    		cdsEventosReenvNotfis.add(Short.valueOf(cdEvento));
    	}
        if (!eventoControleCargaService.existReSendEventoControleCarga(idControleCarga, cdsEventosReenvNotfis)) {
            throw new BusinessException("LMS-05416");
        }
        // buscar a lista de eventos existentes para os documentos do controle de carga
        List<EventoDocumentoServicoDMN> eventos = eventoControleCargaService.getEventoDocumentoContidoControleCarga(idControleCarga, cdsEventosReenvNotfis);
        // enviar lista de documentos para reenvio
        this.monitoramentoDocEletronicoService.executeEnvioNotFisFedex(eventos);
    }

    // LMSA-6159 LMSA-6249
    public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService service) {
        this.monitoramentoDocEletronicoService = service;
    }
    
    public RomaneioEntregaDMN executeEnriqueceRomaneioEntrega(EventoControleCargaDMN eventoControleCargaDMN){
        RomaneioEntregaDMN romaneioEntregaDMN = null;
        
        if(isFilialIntegraRomaneioFedex(eventoControleCargaDMN.getIdFilialOrigem()) && 
                "SP".equals(eventoControleCargaDMN.getTpEventoControleCarga()) && 
                "C".equals(eventoControleCargaDMN.getTpControleCarga())){
            
        	ControleCarga controleCarga = findById(eventoControleCargaDMN.getIdControleCarga()); 
            romaneioEntregaDMN = buildCabecalhoRomaneioEntrega(controleCarga);
        	
            List<DoctoServico> listDoctos = getListDoctosControleCargaTomadorFedex(eventoControleCargaDMN);
            if(CollectionUtils.isNotEmpty(listDoctos)){
                

                List<RomaneioEntregaDocumentoDMN> listDoctosRomaneio = new ArrayList<RomaneioEntregaDocumentoDMN>();
                for(DoctoServico docto : listDoctos){
                    RomaneioEntregaDocumentoDMN romaneioEntregaDocumentoDMN = buildDetalheRomaneioEntrega(controleCarga, docto);
                    if (romaneioEntregaDocumentoDMN != null) {
                    	listDoctosRomaneio.add(romaneioEntregaDocumentoDMN);
                    }
                }
                romaneioEntregaDMN.setFileName(buildRomaneioEntregaFileName(controleCarga));
                romaneioEntregaDMN.setDocumentos(listDoctosRomaneio);
                romaneioEntregaDMN.setQtdCtes(listDoctosRomaneio.size());
            }
        }
        
        if (romaneioEntregaDMN != null && CollectionUtils.isEmpty(romaneioEntregaDMN.getDocumentos())) {
	    return null;
	}
        
	return romaneioEntregaDMN;
    }
    
    private boolean isFilialIntegraRomaneioFedex(Long idFilial){
        ConteudoParametroFilial isFilialIntegraRomaneioFedex = conteudoParametroFilialService.findByNomeParametro(idFilial, "INTEGRA_ROMANEIO_FDX", false, true);
        if(isFilialIntegraRomaneioFedex != null && 
                SIM.equals(isFilialIntegraRomaneioFedex.getVlConteudoParametroFilial())){
            return true;
        }
        return false;
    }

    private List<DoctoServico> getListDoctosControleCargaTomadorFedex(EventoControleCargaDMN eventoControleCargaDMN) {
        List<DoctoServico> listDoctos = doctoServicoService.findDoctosByIdControleCarga(eventoControleCargaDMN.getIdControleCarga());
        List<DoctoServico> listDoctosValidos = new ArrayList<DoctoServico>();
        if(CollectionUtils.isNotEmpty(listDoctos)){
            for(DoctoServico docto : listDoctos){
                if(validateDoctoTomadorFedex(docto)){
                    listDoctosValidos.add(docto);
                }
            }
        }
        return listDoctosValidos;
    }

    private boolean validateDoctoTomadorFedex(DoctoServico docto) {
        DevedorDocServ devedor = ConhecimentoUtils.getDevedorDocServ(docto);
        List<String> tomadorFedex = FormatUtils.asList((String)configuracoesFacade.getValorParametro("GERA_ROMANEIO_FEDEX"), ";");
        Conhecimento conhecimento = (Conhecimento) docto;
        
        boolean retorno = !BooleanUtils.isTrue(conhecimento.getBlRedespachoIntermediario()) && 
        			CollectionUtils.isNotEmpty(tomadorFedex) && 
        				tomadorFedex.contains(devedor.getCliente().getPessoa().getNrIdentificacao().substring(0, 8));
        
        return  retorno;
    }
    
    @SuppressWarnings("null")
    private RomaneioEntregaDMN buildCabecalhoRomaneioEntrega(ControleCarga controleCarga){
        RomaneioEntregaDMN romaneioEntregaDMN = new RomaneioEntregaDMN();
        romaneioEntregaDMN.setCnpjRedespacho(controleCarga.getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
        romaneioEntregaDMN.setNrControleCarga(controleCarga.getNrControleCarga().toString());
        
        EventoControleCarga eventoSaidaPortaria = getEventoControleCargaByTpEvento(controleCarga.getEventoControleCargas(), "SP");
        if(eventoSaidaPortaria != null){
            romaneioEntregaDMN.setDataLiberacaRomaneio(eventoSaidaPortaria.getDhEvento().toString("ddMMyyyy"));
            romaneioEntregaDMN.setHoraLiberacaRomaneio(eventoSaidaPortaria.getDhEvento().toString("HHmmss"));
        }
        
        EventoControleCarga eventoEmissao = getEventoControleCargaByTpEvento(controleCarga.getEventoControleCargas(), "EM");
        if(eventoEmissao != null){
            romaneioEntregaDMN.setDataEmissaoRomaneio(eventoEmissao.getDhEvento().toString("ddMMyyyy"));
            romaneioEntregaDMN.setHoraEmissaoRomaneio(eventoEmissao.getDhEvento().toString("HHmmss"));
        }
        
        romaneioEntregaDMN.setPlacaVeiculo(controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
        romaneioEntregaDMN.setCpfMotorista(controleCarga.getMotorista().getPessoa().getNrIdentificacao());
        romaneioEntregaDMN.setNomeMotorista(controleCarga.getMotorista().getPessoa().getNmPessoa());
        
        TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findByPessoaEnderecoVigente(controleCarga.getMotorista().getPessoa());
        
        if (telefoneEndereco != null){
        	romaneioEntregaDMN.setNrFoneMotorista(telefoneEndereco.getNrTelefone());
        }
        
        if (controleCarga.getMeioTransporteByIdTransportado() != null){
        	romaneioEntregaDMN.setRnTrcVeiculo(controleCarga.getMeioTransporteByIdTransportado().getNrAntt());
        }
        
        romaneioEntregaDMN.setNrRotaEntrega(controleCarga.getRotaColetaEntrega().getNrRota().toString());
        
        return romaneioEntregaDMN;
    }
    
    private EventoControleCarga getEventoControleCargaByTpEvento(List<EventoControleCarga> listEventoCC, String tpEvento){
        if(CollectionUtils.isNotEmpty(listEventoCC)){
            for(EventoControleCarga eventoControleCarga : listEventoCC){
                if(tpEvento.equals(eventoControleCarga.getTpEventoControleCarga().getValue())){
                    return eventoControleCarga;
                }
            }
        }
        return null;
    }
    
    @SuppressWarnings("null")
    private RomaneioEntregaDocumentoDMN buildDetalheRomaneioEntrega(ControleCarga controleCarga, DoctoServico doctoServico){
        RomaneioEntregaDocumentoDMN romaneioEntregaDocumentoDMN = null;
        Conhecimento conhecimento = conhecimentoService.findById(doctoServico.getIdDoctoServico());

        DadosComplemento dadosComplementoChave = dadosComplementoService.findByIdConhecimentoDocServico(doctoServico.getIdDoctoServico(), "NR_CHAVE_DOCUMENTO_ANTERIOR");
        DadosComplemento dadosComplementoData = dadosComplementoService.findByIdConhecimentoTpRegistro(doctoServico.getIdDoctoServico(), "DTCONH");

        if(dadosComplementoChave != null){
        	romaneioEntregaDocumentoDMN = new RomaneioEntregaDocumentoDMN();
            romaneioEntregaDocumentoDMN.setCnpjEmissor(dadosComplementoChave.getDsValorCampo().substring(6,20));
            romaneioEntregaDocumentoDMN.setNumeroDocumento(dadosComplementoChave.getDsValorCampo().substring(25,34));
            romaneioEntregaDocumentoDMN.setSerieDocumento(dadosComplementoChave.getDsValorCampo().substring(23,26));
            romaneioEntregaDocumentoDMN.setChaveDocumento(dadosComplementoChave.getDsValorCampo());
            
            validateDtEmissaoRomaneioEntrega(romaneioEntregaDocumentoDMN, dadosComplementoChave, dadosComplementoData);
            
            validateTpCteRomaneioEntrega(romaneioEntregaDocumentoDMN, conhecimento);
            validateTpEmissaoCteRomaneioEntrega(romaneioEntregaDocumentoDMN, conhecimento);
            
            romaneioEntregaDocumentoDMN.setNrManifestoEntrega(getNrManifetoEntregaRomaneioByControleCargaDocto(controleCarga, doctoServico));
            
        }
        
        return romaneioEntregaDocumentoDMN;
    }

    private void validateDtEmissaoRomaneioEntrega(
            RomaneioEntregaDocumentoDMN romaneioEntregaDocumentoDMN,
            DadosComplemento dadosComplementoChave,
            DadosComplemento dadosComplementoData) {
        String dataFormatada = null;
        if(dadosComplementoData!= null){
            dataFormatada = JTDateTimeUtils.validateStringDate(dadosComplementoData.getDsValorCampo());
        }
        
        if(dataFormatada == null){
            StringBuilder dataEmissaoFormated = new StringBuilder();
            dataEmissaoFormated.append("01");
            dataEmissaoFormated.append(dadosComplementoChave.getDsValorCampo().substring(4,6));
            dataEmissaoFormated.append(dadosComplementoChave.getDsValorCampo().substring(2,4));
            
            dataFormatada = DateTimeFormat.forPattern("ddMMyy").parseDateTime(dataEmissaoFormated.toString()).toString("ddMMyyyy");
        }
        romaneioEntregaDocumentoDMN.setDataEmissao(dataFormatada);
    }
    
    private void validateTpEmissaoCteRomaneioEntrega(RomaneioEntregaDocumentoDMN romaneioEntregaDocumentoDMN, Conhecimento conhecimento) {
        if(conhecimento!= null && conhecimento.getBlIndicadorEdi()){
            romaneioEntregaDocumentoDMN.setTpEmissaoDocumento("D");
        }else{
            romaneioEntregaDocumentoDMN.setTpEmissaoDocumento("M");
        }
    }

    private void validateTpCteRomaneioEntrega(RomaneioEntregaDocumentoDMN romaneioEntregaDocumentoDMN, Conhecimento conhecimento) {
        if(conhecimento!= null && 
                ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(conhecimento.getTpConhecimento().getValue())){
            romaneioEntregaDocumentoDMN.setTpDocumento("D");
        }else{
            romaneioEntregaDocumentoDMN.setTpDocumento("E");
        }
    }
    
    private String getNrManifetoEntregaRomaneioByControleCargaDocto(ControleCarga controleCarga, DoctoServico doctoServico){
        if(doctoServico != null && CollectionUtils.isNotEmpty(doctoServico.getManifestoEntregaDocumentos())){
            for(ManifestoEntregaDocumento med : doctoServico.getManifestoEntregaDocumentos()){
                if(med.getManifestoEntrega().getManifesto().getControleCarga().getIdControleCarga().equals(controleCarga.getIdControleCarga())){
                    return med.getManifestoEntrega().getNrManifestoEntrega().toString();
                }
            }
        }
        return null;
    }
    
    private String buildRomaneioEntregaFileName(ControleCarga cc){
        StringBuilder fileName = new StringBuilder();
        fileName.append("ROMANEIO_FDX_");
        fileName.append(cc.getFilialByIdFilialOrigem().getSgFilial());
        fileName.append(cc.getNrControleCarga());
        fileName.append("_");
        fileName.append(new DateTime().toString("ddMMyyyyHHmmss"));
        
        return fileName.toString();
    }
    
    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
        this.volDadosSessaoService = volDadosSessaoService;
    }

    public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public void setManifestoEntregaVolumeService(ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
        this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }
    
    // LMSA-6520: LMSA-6534
    public void setConhecimentoFedexService(ConhecimentoFedexService conhecimentoFedexService) {
    	this.conhecimentoFedexService = conhecimentoFedexService;
    }
    public void setNotaFiscalExpedicaoEDIService(NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
    	this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
    }
    public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
    	this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    }
    
    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }
    
    public void setDadosComplementoService(DadosComplementoService dadosComplementoService){
        this.dadosComplementoService = dadosComplementoService;
    }
    
    public void generateStatusFimDescarga(Conhecimento conhecimento, Filial filialFedex, Usuario usuario, DateTime dhEvento){
    	boolean encerraControleCarga = false;
		ControleCarga controleCarga = null;
		ManifestoViagemNacional manifestoViagemNacional = manifestoViagemNacionalService.findByConhecimento(conhecimento, filialFedex);
		if (manifestoViagemNacional != null){
			controleCarga = manifestoViagemNacional.getManifesto().getControleCarga();
			controleCarga = findByIdInitLazyProperties(controleCarga.getIdControleCarga(), true);
			Long count = manifestoViagemNacionalService.findCountConhecimentosEmDescargaControleCarga(controleCarga, filialFedex);
			encerraControleCarga = (count == 1); 
}
    
		LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findById(conhecimento.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
		Filial filialLocalizacao = filialService.findById(conhecimento.getFilialLocalizacao().getIdFilial());
		
		List eventosDescargaNaFilial = eventoDocumentoServicoService.findEventoDoctoServico(
				conhecimento.getIdDoctoServico(),
				filialFedex.getIdFilial(), 
				new Short[]{EVENTO_FIM_DESCARGA,EVENTO_FIM_DESCARGA_SEM_LOCALIZACAO},true,null,null);
		if (eventosDescargaNaFilial != null && eventosDescargaNaFilial.size() > 0){
			return;
		}
		
		Short cdEvento = EVENTO_FIM_DESCARGA_SEM_LOCALIZACAO;
		if ((LOCALIZACAO_EM_DESCARGA.equals(localizacaoMercadoria.getCdLocalizacaoMercadoria()) 
			|| LOCALIZACAO_AGUARDANDO_DESCARGA.equals(localizacaoMercadoria.getCdLocalizacaoMercadoria())) 
				&& filialLocalizacao.equals(filialFedex)){
			cdEvento = EVENTO_FIM_DESCARGA;
		}
		
		String nrdocumento =conhecimento.getFilialByIdFilialOrigem().getSgFilial() + " " +  FormatUtils.formataNrDocumento(conhecimento.getNrDoctoServico(), "CTE");
		
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento,conhecimento.getIdDoctoServico(),
				filialFedex.getIdFilial(),
				nrdocumento, 
				dhEvento,
				conhecimento.getTpDoctoServico().getValue());
		
		List<VolumeNotaFiscal> volumesDocto = volumeNotaFiscalService.findByIdConhecimento(conhecimento.getIdDoctoServico());
		eventoVolumeService.storeEventoVolumeDhOcorrencia(volumesDocto, cdEvento, "LM", null, usuario, dhEvento);
		
		if (encerraControleCarga){
			if(filialFedex.equals(controleCarga.getFilialByIdFilialDestino())){
			controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
			}else{
				controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
			}
			store(controleCarga);
			
			List<Manifesto> manifestosControleCarga = manifestoService.findManifestosByControleCarga(controleCarga);
			for (Manifesto manifesto : manifestosControleCarga){
				
				if(filialFedex.equals(manifesto.getFilialByIdFilialDestino()) && "V".equals(manifesto.getTpManifesto().getValue())){
				manifesto.setTpStatusManifesto(new DomainValue("FE"));
				manifestoService.storeManifesto(manifesto);
				
				eventoManifestoService.generateEventoManifesto(manifesto, filialFedex, "FD",dhEvento);
			}
			
			}
			
			eventoControleCargaService.storeEventoControleCarga(controleCarga, "FD",dhEvento);
			
			
			String statusEventoMeioTransporte = "PAOP";
			if ("C".equals(controleCarga.getTpControleCarga())){
				statusEventoMeioTransporte = "ADFR";
			}else{
				if (filialFedex.equals(controleCarga.getFilialByIdFilialDestino())){
					statusEventoMeioTransporte = "ADFR";
				}
			}
			
			//Gera evento meio transporte 
			EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
			eventoMeioTransporte.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
			eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue(statusEventoMeioTransporte));
			eventoMeioTransporte.setDhInicioEvento(dhEvento);
			eventoMeioTransporte.setFilial(filialFedex);
			eventoMeioTransporte.setUsuario(usuario);
			eventoMeioTransporte.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
			eventoMeioTransporteService.store(eventoMeioTransporte);
			
			if (controleCarga.getMeioTransporteByIdSemiRebocado() != null){
				EventoMeioTransporte eventoMeioTransporteSemiReboque = new EventoMeioTransporte();
				eventoMeioTransporteSemiReboque.setMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado());
				eventoMeioTransporteSemiReboque.setTpSituacaoMeioTransporte(new DomainValue(statusEventoMeioTransporte));
				eventoMeioTransporteSemiReboque.setDhInicioEvento(dhEvento);
				eventoMeioTransporteSemiReboque.setFilial(filialFedex);
				eventoMeioTransporteSemiReboque.setUsuario(usuario);
				eventoMeioTransporteSemiReboque.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
				eventoMeioTransporteService.store(eventoMeioTransporteSemiReboque);
			}
			
			
			List<CarregamentoDescarga> carregamentosDescarga = carregamentoDescargaService.findByControleCarga(controleCarga,"D",filialFedex);
			for (CarregamentoDescarga carregamentoDescarga : carregamentosDescarga) {
				carregamentoDescarga.setTpStatusOperacao(new DomainValue("F"));
				carregamentoDescarga.setDhFimOperacao(dhEvento);
				carregamentoDescargaService.store(carregamentoDescarga);
				
				List<DescargaManifesto> descargasManifesto = descargaManifestoService.findByCarregamentoDescarga(carregamentoDescarga);
				for (DescargaManifesto descargaManifesto : descargasManifesto) {
					if (descargaManifesto.getDhFimDescarga() == null){
						descargaManifesto.setDhFimDescarga(dhEvento);
						descargaManifestoService.store(descargaManifesto);
					}
				}
			}
			
			carregamentoDescargaService.executeEnviarFinalizarIntegracaoSmp(controleCarga.getIdControleCarga());
			
			if(filialFedex.equals(controleCarga.getFilialByIdFilialDestino())){
			ciotService.executeAlteracaoCIOT(controleCarga.getIdControleCarga());
		}
		}
		
    }
    
	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
    
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
}

	public String findOwnerContractModel(Long idControleCarga) {
	
		
		ControleCarga controleCarga = findById(idControleCarga);
		
		Map<String, Object> contractParameters = new HashMap<String, Object>();
		Filial filialOrigem = controleCarga.getFilialByIdFilialOrigem();
		
		String emissaoContCCViagem = (String)configuracoesFacade.getValorParametro(filialOrigem.getIdFilial(), "EMISSAO_CONT_CC_VIAG");
		
		
		String modeloContratoPrestServ = ConstantesContratacaoVeiculos.CONTRATO_MOTORISTA_MODELO_1;
 		
		if ("S".equalsIgnoreCase(emissaoContCCViagem)){
			Proprietario proprietario = controleCarga.getProprietario();
			Boolean blRotaFixa =  proprietario.getBlRotaFixa() != null && "S".equals(proprietario.getBlRotaFixa().getValue())?Boolean.TRUE:Boolean.FALSE;
			Boolean blProprietarioTerceiro = !"P".equals(proprietario.getTpProprietario().getValue());
			
			if(blProprietarioTerceiro && !blRotaFixa){
				
				modeloContratoPrestServ = ConstantesContratacaoVeiculos.CONTRATO_MOTORISTA_MODELO_2;
			}else{
				modeloContratoPrestServ = null;
			}
			
			
		}
		
		return modeloContratoPrestServ;
	}

	public Boolean validateImprimeTermoRespColeta(Long idControleCarga) {
		
		ControleCarga controleCarga = findById(idControleCarga);
		String emissaoContCCColetaEntrega = (String)configuracoesFacade.getValorParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "EMIS_CONT_CC_COL_ENT");
		if ("S".equals(emissaoContCCColetaEntrega)){
			if (controleCarga.getProprietario() != null && "E".equals(controleCarga.getProprietario().getTpProprietario().getValue())){
				return Boolean.TRUE;
			}else{
				return Boolean.FALSE;
			}
			
		}
		
		
		return Boolean.TRUE;
	}

	public Boolean validateImprimeContratoPrestServColeta(Long idControleCarga) {
		ControleCarga controleCarga = findById(idControleCarga);
		String emissaoContCCColetaEntrega = (String)configuracoesFacade.getValorParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "EMIS_CONT_CC_COL_ENT");
		if ("S".equals(emissaoContCCColetaEntrega)){
			Proprietario proprietario = controleCarga.getProprietario();
			
			
			if (proprietario != null){
				
				try{
					Empresa empresa = empresaService.findEmpresaByProprietario(proprietario);
					boolean blProprietarioProprio = "P".equals(proprietario.getTpProprietario().getValue());
					boolean blEmpresaParceira =  empresa != null && "P".equals(empresa.getTpEmpresa().getValue());
					
					if (!blProprietarioProprio && !blEmpresaParceira){
						return Boolean.TRUE;
					}
				}catch(ObjectNotFoundException e){
					return Boolean.FALSE;
				}
				
			}
			
		}
		
		return Boolean.FALSE;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public Boolean validateImprimeModeloDeclaracaoPrevSocialColeta(
			Long idControleCarga) {
			
		ControleCarga controleCarga = findById(idControleCarga);
		String emissaoDecPrevSocial = (String)configuracoesFacade.getValorParametro(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "EMIS_DEC_PREV_SOC_PF");
		Proprietario proprietario = controleCarga.getProprietario();
		
		if (proprietario != null){
			String tpPessoa = proprietario.getPessoa().getTpPessoa().getValue();
			
			
			if (("S".equals(emissaoDecPrevSocial) && "F".equals(tpPessoa))){
				return Boolean.TRUE;
			}
			
			
			if((emissaoDecPrevSocial == null || "N".equals(emissaoDecPrevSocial)) && "E".equals(proprietario.getTpProprietario().getValue())){
				return Boolean.TRUE;
			}
			
		}
		return Boolean.FALSE;
	}

	public Boolean validateImprimeDeclaracaoCCViagem(String modeloContrato,
			Long idControleCarga) {
		
		
		ControleCarga controleCarga = findById(idControleCarga);
		
		
		Filial filialOrigem = controleCarga.getFilialByIdFilialOrigem();
		String emissaoContCCViagem = (String)configuracoesFacade.getValorParametro(filialOrigem.getIdFilial(), "EMISSAO_CONT_CC_VIAG");

		if (ConstantesContratacaoVeiculos.CONTRATO_MOTORISTA_MODELO_1.equals(modeloContrato)){
			return Boolean.TRUE;
		}else{
			Proprietario proprietario = controleCarga.getProprietario();
			if (proprietario != null){
				Boolean blProprietarioPessoaFisica = "F".equals(proprietario.getPessoa().getTpPessoa().getValue());
				if ("S".equals(emissaoContCCViagem) && blProprietarioPessoaFisica){
					return Boolean.TRUE;
				}
				
			}
		}
		
		return Boolean.FALSE;
	}
    
}

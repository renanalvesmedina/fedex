package com.mercurio.lms.coleta.model.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaDetalhadoDTO;
import com.mercurio.lms.coleta.dto.RelatorioEficienciaColetaResumidoDTO;
import com.mercurio.lms.coleta.model.AwbColeta;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EficienciaVeiculoColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.MilkRemetente;
import com.mercurio.lms.coleta.model.NotaFiscalColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.PedidoColetaProduto;
import com.mercurio.lms.coleta.model.SemanaRemetMrun;
import com.mercurio.lms.coleta.model.ServicoAdicionalColeta;
import com.mercurio.lms.coleta.model.dao.PedidoColetaDAO;
import com.mercurio.lms.coleta.model.dto.ConsultarColetaDTO;
import com.mercurio.lms.coleta.model.util.ConstantesColeta;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoServico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.TipoEnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NomeProduto;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.AwbOcorrenciaService;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.expedicao.model.service.NomeProdutoService;
import com.mercurio.lms.expedicao.model.service.PreAlertaService;
import com.mercurio.lms.expedicao.model.service.ProdutoCategoriaProdutoService;
import com.mercurio.lms.expedicao.model.service.ProdutoService;
import com.mercurio.lms.expedicao.model.service.TrackingAwbService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.sgr.model.service.ProcedimentoGerRiscoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ColetaAutomaticaCliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ColetaAutomaticaClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vol.utils.VolFomatterUtil;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.coleta.pedidoColetaService"
 */
public class PedidoColetaService extends CrudService<PedidoColeta, Long> {
	
	private static final String REPORT_NAME = "COLETAS_ABERTAS";
	private static final String CNPJ_CLIENTE = "CNPJ Cliente";
	private static final String NR_IDENTIFICACAO = "NR_IDENTIFICACAO";
	private static final String CLIENTE = "CLIENTE";
	private static final String MUNICIPIO = "MUNICIPIO";
	private static final String MOEDA = "MOEDA";
	private static final String VALOR = "VALOR";
	private static final String DATA_INICIAL = "dataInicial";
	private static final String DATA_FINAL = "dataFinal";
	public static final int SIZE_CNPJ = 14;
	public static final int SIZE_CPF = 11;
	public static final int SIZE_CEP = 8;

    private static final int TWO_DAYS_HOURS = 48;
    private static final String ID_PEDIDO_COLETA = "idPedidoColeta";
    private static final String ID_AWB = "idAwb";
    private static final String ID_CONTROLE_CARGA = "idControleCarga";
    private static final String CONTROLE_CARGA = "controleCarga";
    private static final String TP_STATUS_MANIFESTO_COLETA = "tpStatusManifestoColeta";
    private static final String MANIFESTO_COLETA = "manifestoColeta";
    private static final String CATEGORIA_PRODUTO_PERIGOSO = "01";
    private static final String CATEGORIA_PRODUTO_CONTROLADO_POLICIA_CIVIL = "03";
    private static final String CATEGORIA_PRODUTO_CONTROLADO_POLICIA_FEDERAL = "04";
    private static final String CATEGORIA_PRODUTO_CONTROLADO_EXERCITO = "05";
    
    private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    private ProcedimentoGerRiscoService procedimentoGerRiscoService;
    private ConversaoMoedaService conversaoMoedaService;
    private EficienciaVeiculoColetaService eficienciaVeiculoColetaService;
    private ManifestoColetaService manifestoColetaService;
    private ControleCargaService controleCargaService;
    private EventoColetaService eventoColetaService;
    private MilkRunService milkRunService;
    private OcorrenciaColetaService ocorrenciaColetaService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private SemanaRemetMrunService semanaRemetMrunService;
    private ColetaAutomaticaClienteService coletaAutomaticaClienteService;
    private DetalheColetaService detalheColetaService;
    private ServicoAdicionalColetaService servicoAdicionalColetaService;
    private PedidoColetaProdutoService pedidoColetaProdutoService;
    private ServicoService servicoService;
    private TelefoneEnderecoService telefoneEnderecoService;
    private TelefoneContatoService telefoneContatoService;
    private DomainValueService domainValueService;
    private EnderecoPessoaService enderecoPessoaService;
    private TipoEnderecoPessoaService tipoEnderecoPessoaService;
    private AwbColetaService awbColetaService;
    private NotaFiscalColetaService notaFiscalColetaService;
    private UsuarioService usuarioService;
    private MunicipioService municipioService;
    private RotaColetaEntregaService rotaColetaEntregaService;
    private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
    private ConfiguracoesFacade configuracoesFacade;
    private PreAlertaService preAlertaService;
    private CotacaoService cotacaoService;
    private VersaoDescritivoPceService versaoDescritivoPceService;
    private WorkflowPendenciaService workflowPendenciaService;
    private ClienteService clienteService;
    private ProdutoService produtoService;
    private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
    private CtoAwbService ctoAwbService;
    private AwbService awbService;
    private ConhecimentoService conhecimentoService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private EventoVolumeService eventoVolumeService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private ManifestoService manifestoService;
    private ControleTrechoService controleTrechoService;
    private DoctoServicoService doctoServicoService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private ManifestoEntregaService manifestoEntregaService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private CarregamentoMobileService carregamentoMobileService;
    private EventoControleCargaService eventoControleCargaService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private AwbOcorrenciaService awbOcorrenciaService;
    private TrackingAwbService trackingAwbService;
    private MoedaService moedaService;
    private FilialService filialService;
    private MunicipioFilialService municipioFilialService;
    private NaturezaProdutoService naturezaProdutoService;
    private NomeProdutoService nomeProdutoService;
    private ProdutoCategoriaProdutoService produtoCategoriaProdutoService;

    private static final Logger LOGGER = LogManager.getLogger(PedidoColetaService.class);

    /**
     * Recupera uma instância de <code>PedidoColeta</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public PedidoColeta findById(java.lang.Long id) {
        return (PedidoColeta) super.findById(id);
    }

    /**
     * Recupera uma instância de <code>PedidoColeta</code> a partir do ID sem fazer fetch.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public PedidoColeta findByIdBasic(java.lang.Long id) {
        return getPedidoColetaDAO().findByIdBasic(id);
    }

    public PedidoColeta findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
        return (PedidoColeta) super.findByIdInitLazyProperties(id, initializeLazyProperties);
    }

    public Map findMeioTransporteByIdPedidoColeta(Long idPedidoColeta) {
        return getPedidoColetaDAO().findMeioTransporteByIdPedidoColeta(idPedidoColeta);
    }

    public Map findColetaNaoFinalizadaByFilialNrColeta(Long idFilialResponsavel, Long nrColeta) {
        DateTime dataHoraLimite = JTDateTimeUtils.getDataHoraAtual().minusHours(TWO_DAYS_HOURS);
        return getPedidoColetaDAO().findColetaNaoFinalizadaByFilialNrColeta(idFilialResponsavel, nrColeta, dataHoraLimite);
    }

    public List<PedidoColeta> findByIdControleCarga(Long idControleCarga) {
        return getPedidosColeta(getPedidoColetaDAO().findByIdControleCarga(idControleCarga));
    }

    public List<PedidoColeta> findByIdControleCargaAndIdCliente(Long idControleCarga, Long idCliente) {
        return getPedidosColeta(getPedidoColetaDAO().findByIdControleCargaAndIdCliente(idControleCarga, idCliente));
    }

    private List<PedidoColeta> getPedidosColeta(List<PedidoColeta> coletas) {
        if (coletas == null) {
            coletas = new ArrayList<PedidoColeta>();
        }

        return coletas;
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param idPedidoColeta indica a entidade que deverá ser removida.
     */
    public void removeById(Long idPedidoColeta) {
        List<DetalheColeta> listDetalheColeta = detalheColetaService.findDetalheColeta(idPedidoColeta);
        for (DetalheColeta detalheColeta : listDetalheColeta) {
            awbColetaService.removeByIdDetalheColeta(detalheColeta.getIdDetalheColeta());
            notaFiscalColetaService.removeByIdDetalheColeta(detalheColeta.getIdDetalheColeta());
            detalheColetaService.removeById(detalheColeta.getIdDetalheColeta());
        }

        eventoColetaService.removeByIdPedidoColeta(idPedidoColeta);
        preAlertaService.removeByIdPedidoColeta(idPedidoColeta);
        servicoAdicionalColetaService.removeByIdPedidoColeta(idPedidoColeta);
        pedidoColetaProdutoService.removeByidPedidoColeta(idPedidoColeta);

        super.removeById(idPedidoColeta);
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @Override
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        for (Long id : ids) {
            this.removeById(id);
        }
    }

    /**
     * Realiza a consulta utilizando os filtros padrões recebidos da tela,
     * gera um mapeamento dos campos da tela com a classe de persistencia
     * gerenciada pelo DAO.
     *
     * @param criteria Mapa de campos da tela.
     * @return Resultado paginado.
     */
    public ResultSetPage findPaginated(Map criteria) {
        return this.getPedidoColetaDAO().findPaginatedManterColetas(criteria, FindDefinition.createFindDefinition(criteria));
    }

    /**
     * Retorna o total de paginas para o resultado de acordo com os filtros especificados.
     * Este método é utilizado em conjunto com o findPaginated para obter o total
     * de linhas que a consulta retornaria e calcular com isso o total de paginas.
     *
     * @param criteria
     * @return
     */
    public Integer getRowCount(Map criteria) {
        return this.getPedidoColetaDAO().getRowCountManterColetas(criteria, FindDefinition.createFindDefinition(criteria));
    }

    /**
     * Find usado na tela de Manter Coletas ao clicar em um registro da listagem para
     * carregar a aba de Detalhamento de Pedido de Coleta usando ID do Pedido de Coleta.
     *
     * @param idPedidoColeta representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public PedidoColeta findDetalhamentoByIdPedidoColeta(Long idPedidoColeta) {
        PedidoColeta pedidoColeta = this.getPedidoColetaDAO().findDetalhamentoByIdPedidoColeta(idPedidoColeta);
        List<DetalheColeta> listDetalheColeta = detalheColetaService.findDetalheColeta(idPedidoColeta);
        List<ServicoAdicionalColeta> listServicoAdicionalColeta = servicoAdicionalColetaService.findServicoAdicionalColetaByIdPedidoColeta(idPedidoColeta);
        List<PedidoColetaProduto> lisPedidoColetaProduto = pedidoColetaProdutoService.findPedidoColetaProdutoByidPedidoColeta(idPedidoColeta);

        pedidoColeta.setDetalheColetas(listDetalheColeta);
        pedidoColeta.setServicoAdicionalColetas(listServicoAdicionalColeta);
        pedidoColeta.setPedidoColetaProdutos(lisPedidoColetaProduto);

        return pedidoColeta;
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(PedidoColeta bean) {
        return super.store(bean);
    }

    public TypedFlatMap storeAll(PedidoColeta bean, TypedFlatMap tfmBean, ItemList items, ItemListConfig config) {

        boolean rollbackMasterId = bean.getIdPedidoColeta() == null;
        try {
            if (items == null || !items.hasItems()) {
                if (!items.getRemovedItems().isEmpty()) {
                    throw new BusinessException("LMS-02026");
                } else {
                    throw new BusinessException("LMS-02008");
                }
            }

            if (PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(bean.getTpPedidoColeta().getValue())) {
                this.executeEventosRemove(items);
                this.recalcularTotais(bean, items, config);
            }

            this.getPedidoColetaDAO().removeAll(items);


            bean.setDetalheColetas(new ArrayList<DetalheColeta>());

            for (Iterator iter = items.iterator(bean.getIdPedidoColeta(), config); iter.hasNext(); ) {
                DetalheColeta detalheColeta = (DetalheColeta) iter.next();

                bean.getDetalheColetas().add(detalheColeta);
            }

            // Verifica se existe tipo Coleta para o endereço em questão.
            //Caso não exista, gera o endereço com o tipo para Coleta.
            this.storeEnderecoColeta(tfmBean);

            List<TypedFlatMap> listMapsProdutoDiferenciadoColeta = tfmBean.getList("produtoDiferenciado");
            List<PedidoColetaProduto> listPedidoColetaProduto = new ArrayList<PedidoColetaProduto>();
            if (listMapsProdutoDiferenciadoColeta != null) {
                listPedidoColetaProduto = this.createListPedidoColetaProduto(listMapsProdutoDiferenciadoColeta);
            }

            // Pega os registros da Listbox
            List<TypedFlatMap> listMapsServicoAdicionalColeta = tfmBean.getList("servicoAdicionalColetas");
            List<ServicoAdicionalColeta> listServicoAdicionalColetas = new ArrayList<ServicoAdicionalColeta>();
            if (listMapsServicoAdicionalColeta != null) {
                listServicoAdicionalColetas = this.createListServicoAdicionalColeta(listMapsServicoAdicionalColeta);
            }

            EventoColeta eventoColeta = null;
            if (tfmBean.getLong("eventoColeta.usuario.idUsuario") != null) {
                eventoColeta = new EventoColeta();
                eventoColeta.setDsDescricao(tfmBean.getString("eventoColeta.dsDescricao"));
                eventoColeta.setUsuario(new Usuario());
                eventoColeta.getUsuario().setIdUsuario(tfmBean.getLong("eventoColeta.usuario.idUsuario"));
                eventoColeta.setOcorrenciaColeta(new OcorrenciaColeta());
                eventoColeta.getOcorrenciaColeta().setIdOcorrenciaColeta(tfmBean.getLong("eventoColeta.ocorrenciaColeta.idOcorrenciaColeta"));
            }

            return this.storeAll(bean, listPedidoColetaProduto, listServicoAdicionalColetas, eventoColeta, tfmBean.getBoolean("AlertaProdutoDiferenciado"));

        } catch (RuntimeException e) {
            this.rollbackMasterState(bean, rollbackMasterId, e);
            
            if (items != null) {
                items.rollbackItemsState();
            }
            
            if (e instanceof BusinessException && "LMS-02093".equals(((BusinessException) e).getMessageKey())) {
                return null;
            } else {
                throw e;
            }
        }
    }

    private void recalcularTotais(PedidoColeta bean, ItemList items, ItemListConfig config) {
        Integer totalVols = 0;
        BigDecimal psMercadoria = new BigDecimal(0);
        BigDecimal psAforado = new BigDecimal(0);
        BigDecimal vlMercadoria = new BigDecimal(0);
        for (Iterator iter = items.iterator(bean.getIdPedidoColeta(), config); iter.hasNext(); ) {
            DetalheColeta detalheColeta = (DetalheColeta) iter.next();
            if (detalheColeta.getDoctoServico() != null) {
                totalVols += detalheColeta.getQtVolumes();
                psMercadoria = psMercadoria.add(detalheColeta.getPsMercadoria());
                psAforado = psAforado.add(detalheColeta.getPsAforado());

                vlMercadoria = vlMercadoria.add(detalheColeta.getVlMercadoria());
            }
        }
        bean.setQtTotalVolumesInformado(totalVols);
        bean.setQtTotalVolumesVerificado(totalVols);

        bean.setPsTotalInformado(psMercadoria);
        bean.setPsTotalVerificado(psMercadoria);
        bean.setPsTotalAforadoInformado(psAforado);
        bean.setPsTotalAforadoVerificado(psAforado);

        bean.setVlTotalInformado(vlMercadoria);
        bean.setVlTotalVerificado(vlMercadoria);
    }

    private void executeEventosRemove(ItemList items) {
        if (!items.getRemovedItems().isEmpty()) {
            for (Iterator iter = items.getRemovedItems().iterator(); iter.hasNext(); ) {
                DetalheColeta detalheColeta = (DetalheColeta) iter.next();
                if (detalheColeta.getDoctoServico() != null) {
                    Conhecimento con = conhecimentoService.findById(detalheColeta.getDoctoServico().getIdDoctoServico());
                    this.generateEventoDoctoVolume(con, ConstantesSim.EVENTO_REALIZAR_COLETA_NO_AEROPORTO, null);
                }
            }
        }
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean
     * @param listPedidoColetaProduto
     * @param listServicoAdicionalColetas
     * @param eventoColeta
     * @param blAlertaProdutoDiferenciado
     * @return entidade que foi armazenada.
     */
    public TypedFlatMap storeAll(PedidoColeta bean, List<PedidoColetaProduto> listPedidoColetaProduto, List<ServicoAdicionalColeta> listServicoAdicionalColetas,
                                 EventoColeta eventoColeta, Boolean blAlertaProdutoDiferenciado) {
        return storeAll(bean, listPedidoColetaProduto, listServicoAdicionalColetas, eventoColeta,
                blAlertaProdutoDiferenciado, null, SessionUtils.getUsuarioLogado(), true,
                SessionUtils.getPaisSessao().getIdPais(), SessionUtils.getMoedaSessao().getIdMoeda());
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean                        PedidoColeta a ser armazenado.
     * @param listPedidoColetaProduto
     * @param listServicoAdicionalColetas
     * @param eventoColeta                DetalheColeta a ser armazenado.
     * @param blAlertaProdutoDiferenciado
     * @param dataHoraAtual               Data e hora atual
     * @param usuario                     Usuário
     * @param converterMoeda              Indica se deve converter valores para a moeda da sessão
     * @param idPais                      Identificador do país utilizada para conversão da moeda
     * @param idMoeda                     Identificador da moeda de destino utilizada para conversão da moeda
     * @return entidade que foi armazenada.
     */
    @SuppressWarnings("unchecked")
    private TypedFlatMap storeAll(PedidoColeta bean, List<PedidoColetaProduto> listPedidoColetaProduto,
                                  List<ServicoAdicionalColeta> listServicoAdicionalColetas, EventoColeta eventoColeta,
                                  Boolean blAlertaProdutoDiferenciado, DateTime dataHoraAtual, Usuario usuario,
                                  Boolean converterMoeda, Long idPais, Long idMoeda) {

        BigDecimal totalValorMercadoria = BigDecimalUtils.ZERO;
        BigDecimal totalPesoMercadoria = BigDecimalUtils.ZERO;
        BigDecimal totalPesoAforado = BigDecimalUtils.ZERO;
        Integer totalQuantidadeVolumes = 0;
        List<String> numerosPedidoColeta = new ArrayList<String>();

        //Flag para a necessidadade de geracao do workflow...
        boolean geraWorkflow = false;
        PedidoColeta pedidoColetaByWorkflow = null;
        //Flag para a necessidadade de geracao do workflow de produtos diferenciados nao aprovados
        boolean geraWorkflowProdDiferenciados = false;

        boolean isInsert = bean.getIdPedidoColeta() == null;

        // Ao pegar os itens novos com getNewOrModifiedItems(), todos os DetalheColeta do ItemList ficam com o ID 'NULO'.
        int quantDetalhesNovos = bean.getDetalheColetas().size();

        if (bean.getDetalheColetas() == null || bean.getDetalheColetas().isEmpty()) {
            throw new BusinessException("LMS-02008");
        }

        // Caso a cotação tenha sido informada, trocar a situação da mesma por 'O' => Utilizada Coleta.
        if (bean.getCotacao() != null) {
            Cotacao cotacao = bean.getCotacao();
            this.executeValidaCotacao(cotacao, bean.getCliente().getIdCliente(), bean.getMunicipio().getIdMunicipio(), bean.getIdPedidoColeta());

            cotacao.setTpSituacao(new DomainValue("O"));
            cotacaoService.store(cotacao);
        }

        String sitAprovacao = this.getSituacaoAprovacao(bean.getBlProdutoDiferenciado(), listPedidoColetaProduto, blAlertaProdutoDiferenciado, bean.getCliente().getIdCliente());

        if (sitAprovacao.equals(PedidoColetaServiceConstants.WORKFLOW_EM_APROVACAO)) {
            geraWorkflowProdDiferenciados = true;
        }

        bean.setSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", sitAprovacao));

        //Percorre cada detalhe para verificar se o serviço possui prioridade ou somar os valores, peso e quantidades
        for (int j = bean.getDetalheColetas().size() - 1; j >= 0; j--) {
            DetalheColeta detalheColeta = (DetalheColeta) bean.getDetalheColetas().get(j);

            if (detalheColeta.getPsAforado() != null && detalheColeta.getPsMercadoria() == null) {
                detalheColeta.setPsMercadoria(detalheColeta.getPsAforado());
            } else if (detalheColeta.getPsAforado() == null && detalheColeta.getPsMercadoria() != null) {
                detalheColeta.setPsAforado(detalheColeta.getPsMercadoria());
            }

			/*Para saber se um detalhe possui prioridade, verificar na tabela SERVICO o ID_TIPO_SERVICO e com isso na tabela TIPO_SERVICO verificar se a coluna
            BL_PRIORIZAR é 'S'. Se for incluir um Pedido de Coleta com os mesmos dados e com maior numero de coleta ara a filial responsavela para o Detalhe Coleta*/
            Servico servico = servicoService.findById(detalheColeta.getServico().getIdServico());
            TipoServico tipoServico = servico.getTipoServico();

            if ((detalheColeta.getIdDetalheColeta() == null || detalheColeta.getIdDetalheColeta() < 0)
                    && (tipoServico.getBlPriorizar().booleanValue() || (servico.getTpModal().getValue().equals(PedidoColetaServiceConstants.TP_MODAL_AEREO) && detalheColeta.getDoctoServico() == null))
                    && quantDetalhesNovos > 1
                    ) {
                bean.getDetalheColetas().remove(j);
                quantDetalhesNovos--;

                PedidoColeta novoPedidoColeta = storeNovoPedidoColeta(bean, detalheColeta, dataHoraAtual, listServicoAdicionalColetas,
                        listPedidoColetaProduto, numerosPedidoColeta);

                // Gera workflow para aprovacao de produtos diferenciados nao aprovados
                if (geraWorkflowProdDiferenciados) {
                    geraWorkFlowAprovacaoProdutosDiferenciados(novoPedidoColeta);
                }

                // Salva o evento de coleta para o bloqueio de credito para coleta
                if (eventoColeta != null) {
                    this.gravarEventoColetaLiberacao(novoPedidoColeta, eventoColeta);
                }

                // Salva um evento de coleta ao inserir um pedido de coleta no banco
                this.gravarEventoColetaSolicitacao(novoPedidoColeta, usuario, dataHoraAtual != null ? dataHoraAtual : JTDateTimeUtils.getDataHoraAtual());
                if (novoPedidoColeta.getTpModoPedidoColeta().getValue().equals(PedidoColetaServiceConstants.MODO_PEDIDO_COLETA_BALCAO)) {
                    this.gravarEventoColetaExecucao(novoPedidoColeta);
                }

            } else { // Caso não possua prioridade, incluir todos so Detalhes Coleta para o Pedido Coleta principal

                if (converterMoeda) {
                    BigDecimal valorMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
                            idPais, detalheColeta.getMoeda().getIdMoeda(), idPais, idMoeda,
                            dataHoraAtual == null ? JTDateTimeUtils.getDataAtual() : dataHoraAtual.toYearMonthDay(),
                            detalheColeta.getVlMercadoria()
                    );

                    totalValorMercadoria = totalValorMercadoria.add(valorMercadoriaConvertido);
                } else {
                    totalValorMercadoria = totalValorMercadoria.add(detalheColeta.getVlMercadoria());
                }

                totalPesoMercadoria = totalPesoMercadoria.add(detalheColeta.getPsMercadoria());
                totalPesoAforado = totalPesoAforado.add(detalheColeta.getPsAforado());
                totalQuantidadeVolumes = Integer.valueOf(totalQuantidadeVolumes.intValue() + detalheColeta.getQtVolumes().intValue());
            }

            //Caso o detalhe de coleta seja Aéreo, gera o workflow...
            if (detalheColeta.getServico().getTpModal().getValue().equals(PedidoColetaServiceConstants.TP_MODAL_AEREO)) {
                geraWorkflow = true;
                pedidoColetaByWorkflow = detalheColeta.getPedidoColeta();
            }
        }

        if (bean.getIdPedidoColeta() == null) {
            bean.setVlTotalInformado(totalValorMercadoria);
            bean.setPsTotalInformado(totalPesoMercadoria);
            bean.setPsTotalAforadoInformado(totalPesoAforado);
            bean.setQtTotalVolumesInformado(totalQuantidadeVolumes);
        }
        bean.setVlTotalVerificado(totalValorMercadoria);
        bean.setPsTotalVerificado(totalPesoMercadoria);
        bean.setPsTotalAforadoVerificado(totalPesoAforado);
        bean.setQtTotalVolumesVerificado(totalQuantidadeVolumes);

        if (bean.getIdPedidoColeta() != null) {
            servicoAdicionalColetaService.removeByIdPedidoColeta(bean.getIdPedidoColeta());
            bean.getServicoAdicionalColetas().clear();

            pedidoColetaProdutoService.removeByidPedidoColeta(bean.getIdPedidoColeta());
            bean.getPedidoColetaProdutos().clear();

            for (DetalheColeta detalheColeta : (List<DetalheColeta>) bean.getDetalheColetas()) {
                if (detalheColeta.getIdDetalheColeta() != null) {
                    notaFiscalColetaService.removeByIdDetalheColeta(detalheColeta.getIdDetalheColeta());
                    awbColetaService.removeByIdDetalheColeta(detalheColeta.getIdDetalheColeta());
                }
            }
        }
        if (listServicoAdicionalColetas == null) {
            listServicoAdicionalColetas = new ArrayList<ServicoAdicionalColeta>();
        }
        for (int i = 0; i < listServicoAdicionalColetas.size(); i++) {
            ServicoAdicionalColeta servicoAdicionalColeta = (ServicoAdicionalColeta) listServicoAdicionalColetas.get(i);
            servicoAdicionalColeta.setIdServicoAdicionalColeta(null);
            servicoAdicionalColeta.setPedidoColeta(bean);
            listServicoAdicionalColetas.set(i, servicoAdicionalColeta);
        }
        bean.setServicoAdicionalColetas(listServicoAdicionalColetas);

        if (bean.getBlProdutoDiferenciado()) {
            for (int i = 0; i < listPedidoColetaProduto.size(); i++) {
                PedidoColetaProduto pedidoColetaProduto = (PedidoColetaProduto) listPedidoColetaProduto.get(i);
                pedidoColetaProduto.setIdPedidoColetaProduto(null);
                pedidoColetaProduto.setPedidoColeta(bean);
                listPedidoColetaProduto.set(i, pedidoColetaProduto);
            }
            bean.setPedidoColetaProdutos(listPedidoColetaProduto);

            bean = setProdutoPerigosoControladoPedidoColeta(bean);
        } else {
            bean.setBlProdutoPerigoso(false);
            bean.setBlProdutoControlado(false);
            bean.setPedidoColetaProdutos(new ArrayList<PedidoColetaProduto>());
        }

        // Pega o ultimo numero de coleta no banco
        Long nroColetaFinal = null;
        if (bean.getNrColeta() == null) {
            nroColetaFinal = configuracoesFacade.incrementaParametroSequencial(bean.getFilialByIdFilialResponsavel().getIdFilial(), "NR_COLETA", true);
            bean.setNrColeta(nroColetaFinal);
        } else {
            nroColetaFinal = bean.getNrColeta();
        }

        // Completa o numero de coleta com zeros a esquerda e concatena com a sigla da filial responsável
        String nrColetaConcatenado = FormatUtils.fillNumberWithZero(nroColetaFinal.toString(), 8);
        numerosPedidoColeta.add(bean.getFilialByIdFilialResponsavel().getSgFilial() + " " + nrColetaConcatenado);

        bean = getPedidoColetaDAO().storeAll(bean);

        // LMS-3252

        for (DetalheColeta detalheColeta : (List<DetalheColeta>) bean.getDetalheColetas()) {
            if (detalheColeta.getNotaFiscalColetas() != null) {
                for (NotaFiscalColeta nfc : (List<NotaFiscalColeta>) detalheColeta.getNotaFiscalColetas()) {
                    if (nfc.getNrChave() != null) {
                        this.monitoramentoNotasFiscaisCCTService.storeEvento("CO", nfc.getNrChave(), bean.getCliente(),
                                bean, null, null, null, SessionUtils.getUsuarioLogado());
                    }
                }
            }
        }

        generateEventosColetaAeroByPedidoColeta(bean, PedidoColetaServiceConstants.CD_PEDIDO_COLETA_EMITIDO, new Short[]{ConstantesSim.CD_MERCADORIA_EM_VIAGEM_AEREA,
                ConstantesSim.CD_MERCADORIA_AGUARDANDO_EMBARQUE_CIA_AEREA});

        generateManifestoEntregaDireta(bean, dataHoraAtual != null ? dataHoraAtual : JTDateTimeUtils.getDataHoraAtual());

        //Caso exista a necessidade de se gerar o workflow...
        if (geraWorkflow) {
            List<String> parametro = new ArrayList<String>();
            parametro.add(pedidoColetaByWorkflow.getFilialByIdFilialResponsavel().getSgFilial());
            parametro.add(FormatUtils.fillNumberWithZero(pedidoColetaByWorkflow.getNrColeta().toString(), 8));

            workflowPendenciaService.generatePendencia(
                    SessionUtils.getFilialSessao().getIdFilial(),
                    ConstantesWorkflow.NR0201_COLETA_AEREA,
                    pedidoColetaByWorkflow.getIdPedidoColeta(),
                    configuracoesFacade.getMensagem("LMS-02082", parametro.toArray()),
                    JTDateTimeUtils.getDataHoraAtual()
            );
        }

        TypedFlatMap registrosSalvos = new TypedFlatMap();
        // Seta dados para o map para ser usado na tela pai.
        registrosSalvos.put("filialByIdFilialResponsavel.sgFilial", bean.getFilialByIdFilialResponsavel().getSgFilial());
        registrosSalvos.put(PedidoColetaServiceConstants.NR_COLETA, nrColetaConcatenado);
        registrosSalvos.put("numerosPedidoColeta", numerosPedidoColeta);
        registrosSalvos.put(PedidoColetaServiceConstants.ID_PEDIDO_COLETA, bean.getIdPedidoColeta());
        registrosSalvos.put("vlTotalInformado", bean.getVlTotalInformado());
        registrosSalvos.put("qtTotalVolumesInformado", bean.getQtTotalVolumesInformado());
        registrosSalvos.put("psTotalInformado", bean.getPsTotalInformado());
        registrosSalvos.put("psTotalAforadoInformado", bean.getPsTotalAforadoInformado());
        registrosSalvos.put("vlTotalVerificado", bean.getVlTotalVerificado());
        registrosSalvos.put("qtTotalVolumesVerificado", bean.getQtTotalVolumesVerificado());
        registrosSalvos.put("psTotalVerificado", bean.getPsTotalVerificado());
        registrosSalvos.put("psTotalAforadoVerificado", bean.getPsTotalAforadoVerificado());
        registrosSalvos.put("situacaoAprovacao", bean.getSituacaoAprovacao() != null ? bean.getSituacaoAprovacao().getValue() : "");

        // Salva o evento de coleta para o bloqueio de credito para coleta
        if (eventoColeta != null) {
            this.gravarEventoColetaLiberacao(bean, eventoColeta);
        }

        // Salva um evento de coleta ao inserir um pedido de coleta no banco
        if (isInsert) {
            // Salva um evento de coleta ao inserir um pedido de coleta no banco
            this.gravarEventoColetaSolicitacao(bean, usuario, dataHoraAtual != null ? dataHoraAtual : JTDateTimeUtils.getDataHoraAtual());

            if (bean.getTpModoPedidoColeta().getValue().equals(PedidoColetaServiceConstants.MODO_PEDIDO_COLETA_BALCAO)) {
                this.gravarEventoColetaExecucao(bean);
            }
        }

        // Gera workflow para aprovacao de produtos diferenciados nao aprovados
        if (geraWorkflowProdDiferenciados) {
            geraWorkFlowAprovacaoProdutosDiferenciados(bean);
        }

        return registrosSalvos;
    }

    private PedidoColeta setProdutoPerigosoControladoPedidoColeta(PedidoColeta bean) {
        List<PedidoColetaProduto> listPedidoColetaProdutos = bean.getPedidoColetaProdutos();
        for(PedidoColetaProduto pcp : listPedidoColetaProdutos){
            List listaCdCategoriaProduto = new ArrayList();
            
            listaCdCategoriaProduto.add(CATEGORIA_PRODUTO_PERIGOSO);
            bean.setBlProdutoPerigoso(produtoCategoriaProdutoService.findProdutoCategoriaProdutoByIdProdutoCdCategoria(pcp.getProduto().getIdProduto(), listaCdCategoriaProduto));

            listaCdCategoriaProduto.clear();
            
            listaCdCategoriaProduto.add(CATEGORIA_PRODUTO_CONTROLADO_POLICIA_CIVIL);
            listaCdCategoriaProduto.add(CATEGORIA_PRODUTO_CONTROLADO_POLICIA_FEDERAL);
            listaCdCategoriaProduto.add(CATEGORIA_PRODUTO_CONTROLADO_EXERCITO);
            bean.setBlProdutoControlado(produtoCategoriaProdutoService.findProdutoCategoriaProdutoByIdProdutoCdCategoria(pcp.getProduto().getIdProduto(), listaCdCategoriaProduto));
        }
        return bean;
    }

    private void storeEnderecoColeta(TypedFlatMap tfmBean) {
        if (tfmBean.getLong("idEnderecoPessoa") != null) {
            TipoEnderecoPessoa tipoEnderecoPessoa = enderecoPessoaService.findTipoEnderecoPessoaByIdEnderecoPessoaAndTipo(tfmBean.getLong("idEnderecoPessoa"), "COL");
            if (tipoEnderecoPessoa == null) {
                this.storeTipoEnderecoPessoa(tfmBean.getLong("idEnderecoPessoa"));
            }

            //Nesse ponto precisa deixar o containsKey para saber se o map vem da tela de cadastro ou manutenção do pedido coleta.
            if (tfmBean.containsKey("telefoneEndereco") && tfmBean.getLong("telefoneEndereco.idTelefoneEndereco") == null) {
                // Salva um novo telefoneEndereco caso o telefone informado seja novo.
                this.storeTelefoneEndereco(tfmBean.getLong("idEnderecoPessoa"), tfmBean.getString("nrDddTelefoneNovo"), tfmBean.getString("nrTelefoneNovo"));
            }
        }
    }

    private void gravarEventoColetaExecucao(PedidoColeta novoPedidoColeta) {
        OcorrenciaColeta ocorrenciaColetaExecucao = this.findOcorrenciaColeta(PedidoColetaServiceConstants.EVENTO_COLETA_EXECUCAO);
        this.gravarEventoColeta(SessionUtils.getUsuarioLogado(), novoPedidoColeta, null, ocorrenciaColetaExecucao, PedidoColetaServiceConstants.EVENTO_COLETA_EXECUCAO, null, null);
    }

    private void gravarEventoColetaSolicitacao(PedidoColeta novoPedidoColeta, Usuario usuario, DateTime dhEvento) {
        OcorrenciaColeta ocorrenciaColetaSolicitacao = this.findOcorrenciaColeta(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO);
        this.gravarEventoColeta(usuario, novoPedidoColeta, null, ocorrenciaColetaSolicitacao, PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO, null, null, dhEvento);
    }

    private OcorrenciaColeta findOcorrenciaColeta(String eventoColetaSolicitacao) {
        List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO);
        return listOcorrenciaColeta.get(0);
    }

    private String getSituacaoAprovacao(Boolean blProdutoDiferenciado, List<PedidoColetaProduto> listPedidoColetaProduto, Boolean blAlertaProdutoDiferenciado, Long idCliente) {
        // Verifica se foram selecionados produtos diferenciados
        if (blProdutoDiferenciado && CollectionUtils.isNotEmpty(listPedidoColetaProduto)) {
            ArrayList<Long> produtosDiferenciados = new ArrayList<Long>();
            List<Long> produtoIdList = new ArrayList<Long>();

            if (listPedidoColetaProduto != null) {
                for (PedidoColetaProduto pcp : listPedidoColetaProduto) {
                    produtosDiferenciados.add(pcp.getProduto().getIdProduto());
                }
                List<Produto> produtoList = produtoService.findByIdListProdutosAprovados(idCliente, produtosDiferenciados);
                if (produtoList != null) {
                    for (Produto prd : produtoList) {
                        produtoIdList.add(prd.getIdProduto());
                    }
                }
            }
            //Se todos os produtos estao aprovados
            if (produtoIdList.containsAll(produtosDiferenciados)) {
                return PedidoColetaServiceConstants.WORKFLOW_APROVADO;
            } else {
                // Existem produtos diferenciados não aprovado nos produtos do cliente ?
                if (blAlertaProdutoDiferenciado) {
                    throw new BusinessException("LMS-02093");//Isto é apenas para fazer o rollback
                }

                return PedidoColetaServiceConstants.WORKFLOW_EM_APROVACAO;
            }
        } else {
            //Não foram selecionados produtos diferenciados
            return PedidoColetaServiceConstants.WORKFLOW_APROVADO;
        }
    }

    private List<ServicoAdicionalColeta> createListServicoAdicionalColeta(List<TypedFlatMap> listMapsServicoAdicionalColeta) {
        List<ServicoAdicionalColeta> listServicoAdicionalColetas = new ArrayList<ServicoAdicionalColeta>();
        for (TypedFlatMap typedFlatMap : listMapsServicoAdicionalColeta) {
            ServicoAdicionalColeta servicoAdicionalColeta = new ServicoAdicionalColeta();
            ServicoAdicional servicoAdicional = new ServicoAdicional();
            servicoAdicional.setIdServicoAdicional(typedFlatMap.getLong("idServicoAdicional"));
            servicoAdicionalColeta.setIdServicoAdicionalColeta(null);
            servicoAdicionalColeta.setServicoAdicional(servicoAdicional);
            listServicoAdicionalColetas.add(servicoAdicionalColeta);
        }
        return listServicoAdicionalColetas;
    }

    private List<PedidoColetaProduto> createListPedidoColetaProduto(List<TypedFlatMap> listMapsProdutoDiferenciadoColeta) {
        List<PedidoColetaProduto> listPedidoColetaProduto = new ArrayList<PedidoColetaProduto>();
        for (TypedFlatMap tfm : listMapsProdutoDiferenciadoColeta) {
            PedidoColetaProduto pedidoColetaProduto = new PedidoColetaProduto();
            Produto produto = new Produto();
            
            if(tfm.getLong("idNomeProduto") != null){
                NomeProduto nomeProduto = nomeProdutoService.findById(tfm.getLong("idNomeProduto"));
                produto.setIdProduto(nomeProduto.getProduto().getIdProduto());
            }else if(tfm.getLong("idProduto") != null){
            produto.setIdProduto(tfm.getLong("idProduto"));
            }
            
            pedidoColetaProduto.setIdPedidoColetaProduto(null);
            pedidoColetaProduto.setProduto(produto);
            listPedidoColetaProduto.add(pedidoColetaProduto);
        }
        return listPedidoColetaProduto;
    }

    private void storeTelefoneEndereco(Long idEnderecoPessoa, String nrDddTelefoneNovo, String nrTelefoneNovo) {

        EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);

        TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
        telefoneEndereco.setEnderecoPessoa(enderecoPessoa);
        telefoneEndereco.setNrDdd(nrDddTelefoneNovo);
        telefoneEndereco.setNrTelefone(nrTelefoneNovo);
        telefoneEndereco.setPessoa(enderecoPessoa.getPessoa());
        telefoneEndereco.setTpTelefone(new DomainValue("C"));
        telefoneEndereco.setTpUso(new DomainValue("FO"));
        telefoneEnderecoService.store(telefoneEndereco);

    }

    private void storeTipoEnderecoPessoa(Long idEnderecoPessoa) {
        EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);

        TipoEnderecoPessoa tipoEnderecoPessoa = new TipoEnderecoPessoa();
        tipoEnderecoPessoa.setEnderecoPessoa(enderecoPessoa);
        tipoEnderecoPessoa.setTpEndereco(new DomainValue("COL"));
        tipoEnderecoPessoaService.store(tipoEnderecoPessoa);

    }

    private PedidoColeta storeNovoPedidoColeta(PedidoColeta bean, DetalheColeta detalheColeta, DateTime dataHoraAtual, List<ServicoAdicionalColeta> listServicoAdicionalColetas, List<PedidoColetaProduto> listPedidoColetaProduto, List<String> numerosPedidoColeta) {
        //Popula um novo bean de pedido de coleta com o bean principal para salvar o detalhe com prioridade alta
        PedidoColeta novoPedidoColeta = new PedidoColeta();
        try {
            BeanUtils.copyProperties(novoPedidoColeta, bean);
        } catch (Exception e) {
            LOGGER.warn(e);
        }
        novoPedidoColeta.setIdPedidoColeta(null);
        novoPedidoColeta.setVersao(null);

        BigDecimal valorMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
                detalheColeta.getMunicipio().getUnidadeFederativa().getPais().getIdPais(),
                detalheColeta.getMoeda().getIdMoeda(),
                SessionUtils.getPaisSessao().getIdPais(),
                SessionUtils.getMoedaSessao().getIdMoeda(),
                dataHoraAtual.toYearMonthDay(),
                detalheColeta.getVlMercadoria()
        );

        novoPedidoColeta.setVlTotalInformado(valorMercadoriaConvertido);
        novoPedidoColeta.setVlTotalVerificado(valorMercadoriaConvertido);
        novoPedidoColeta.setPsTotalInformado(detalheColeta.getPsMercadoria());
        novoPedidoColeta.setPsTotalVerificado(detalheColeta.getPsMercadoria());
        novoPedidoColeta.setQtTotalVolumesInformado(detalheColeta.getQtVolumes());
        novoPedidoColeta.setQtTotalVolumesVerificado(detalheColeta.getQtVolumes());
        novoPedidoColeta.setPsTotalAforadoInformado(detalheColeta.getPsAforado());
        novoPedidoColeta.setPsTotalAforadoVerificado(detalheColeta.getPsAforado());

        List<ServicoAdicionalColeta> listServicoAdicionalColetasNovo = new ArrayList<ServicoAdicionalColeta>();
        for (ServicoAdicionalColeta servicoAdicionalColetaBean : listServicoAdicionalColetas) {
            ServicoAdicionalColeta servicoAdicionalColeta = new ServicoAdicionalColeta();
            try {
                BeanUtils.copyProperties(servicoAdicionalColeta, servicoAdicionalColetaBean);
            } catch (Exception e) {
                LOGGER.warn(e);
            }

            servicoAdicionalColeta.setIdServicoAdicionalColeta(null);
            servicoAdicionalColeta.setPedidoColeta(novoPedidoColeta);
            listServicoAdicionalColetasNovo.add(servicoAdicionalColeta);
        }
        novoPedidoColeta.setServicoAdicionalColetas(listServicoAdicionalColetasNovo);

        if (novoPedidoColeta.getBlProdutoDiferenciado()) {
            List<PedidoColetaProduto> listPedidoColetaProdutosNovo = new ArrayList<PedidoColetaProduto>();
            for (PedidoColetaProduto pedidoColetaProdutoBean : listPedidoColetaProduto) {
                PedidoColetaProduto pedidoColetaProduto = new PedidoColetaProduto();
                try {
                    BeanUtils.copyProperties(pedidoColetaProduto, pedidoColetaProdutoBean);
                } catch (Exception e) {
                    LOGGER.warn(e);
                }

                pedidoColetaProduto.setIdPedidoColetaProduto(null);
                pedidoColetaProduto.setPedidoColeta(novoPedidoColeta);
                listPedidoColetaProdutosNovo.add(pedidoColetaProduto);
            }
            novoPedidoColeta.setPedidoColetaProdutos(listPedidoColetaProdutosNovo);
        } else {
            novoPedidoColeta.setPedidoColetaProdutos(new ArrayList<PedidoColetaProduto>());
        }

        detalheColeta.setPedidoColeta(novoPedidoColeta);

        List<DetalheColeta> listDetalheColeta = new ArrayList<DetalheColeta>();
        listDetalheColeta.add(detalheColeta);
        novoPedidoColeta.setDetalheColetas(listDetalheColeta);

        Long nroColetaFinal = configuracoesFacade.incrementaParametroSequencial(novoPedidoColeta.getFilialByIdFilialResponsavel().getIdFilial(), "NR_COLETA", true);
        novoPedidoColeta.setNrColeta(nroColetaFinal);

        // Completa o numero de coleta com zeros a esquerda e concatena com a sigla da filial responsável
        String nrColetaConcatenado = FormatUtils.fillNumberWithZero(nroColetaFinal.toString(), 8);
        numerosPedidoColeta.add(novoPedidoColeta.getFilialByIdFilialResponsavel().getSgFilial() + " " + nrColetaConcatenado);

        novoPedidoColeta.setPedidoColetaProdutos(new ArrayList<PedidoColetaProduto>());

        novoPedidoColeta = this.getPedidoColetaDAO().storeAll(novoPedidoColeta);

        generateEventosColetaAeroByPedidoColeta(novoPedidoColeta, PedidoColetaServiceConstants.CD_PEDIDO_COLETA_EMITIDO, (Short[]) null);

        // LMS-3252
        for (NotaFiscalColeta nfc : (List<NotaFiscalColeta>) detalheColeta.getNotaFiscalColetas()) {
            if (nfc.getNrChave() != null) {
                this.monitoramentoNotasFiscaisCCTService.storeEvento("CO", nfc.getNrChave(), novoPedidoColeta.getCliente(), novoPedidoColeta, null, null, null, SessionUtils.getUsuarioLogado());
            }
        }

        return novoPedidoColeta;
    }

    public void generateManifestoEntregaDireta(PedidoColeta pedidoColeta) {
        generateManifestoEntregaDireta(pedidoColeta, JTDateTimeUtils.getDataHoraAtual());
    }

    private void generateManifestoEntregaDireta(PedidoColeta pedidoColeta, DateTime dataHoraAtual) {

        ManifestoColeta manifestoColeta = pedidoColeta.getManifestoColeta();
        if (manifestoColeta == null) {
            return;
        }

        ControleCarga controleCarga = manifestoColeta.getControleCarga();
        if (controleCarga == null) {
            return;
        }

        List detalhes = detalheColetaService.findDetalheColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta());

        boolean updateMDF = false;

        if (CollectionUtils.isNotEmpty(detalhes)) {
            for (DetalheColeta detalheColeta : (List<DetalheColeta>) detalhes) {
                if (BooleanUtils.isTrue(detalheColeta.getBlEntregaDireta())) {
                    DoctoServico docto = doctoServicoService.findById(detalheColeta.getDoctoServico().getIdDoctoServico());
                    LocalizacaoMercadoria lm = localizacaoMercadoriaService.findById(docto.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());

                    if (ConstantesSim.CD_MERCADORIA_COLETA_REALIZADA_NO_AEROPORTO.equals(lm.getCdLocalizacaoMercadoria())) {
                        this.generatePreManifestos(controleCarga, docto, dataHoraAtual);
                        updateMDF = true;
                    }
                }
            }
        }

        if (updateMDF) {
            controleCargaService.executeEncerrarMdfesAutorizados(controleCarga.getIdControleCarga());
            controleCargaService.generateMdfe(controleCarga.getIdControleCarga(), true);
        }
    }

    private void generatePreManifestos(ControleCarga controleCarga, DoctoServico docto, DateTime dataHoraAtual) {
        List<Manifesto> manifestos = manifestoService.findManifestoByIdControleCarga(controleCarga.getIdControleCarga(),
                SessionUtils.getFilialSessao().getIdFilial(), null, ConstantesExpedicao.TP_MANIFESTO_ENTREGA);

        CollectionUtils.filter(manifestos, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Manifesto man = (Manifesto) arg0;
                return !man.getTpStatusManifesto().getValue().equals(ConstantesExpedicao.TP_STATUS_MANIFESTO_CANCELADO);
            }
        });

        Manifesto manifesto = null;

        if (CollectionUtils.isNotEmpty(manifestos)) {
            manifesto = manifestoService.findById(manifestos.get(0).getIdManifesto());

            PreManifestoDocumento preManifestoDocto = this.storePreManifestoDoctoVolume(manifesto, docto);
            carregamentoMobileService.updateValoresManifesto(docto, manifesto, PedidoColetaServiceConstants.ADICIONA_VALORES);

            this.generateEventoDoctoVolume((Conhecimento) docto, PedidoColetaServiceConstants.CD_EVENTO_PRE_MANIFESTO, null);

            ManifestoEntrega me = manifestoEntregaService.findById(manifesto.getManifestoEntrega().getIdManifestoEntrega());

            ManifestoEntregaDocumento med = manifestoEntregaService.storeManifestoEntregaDocVolume(preManifestoDocto, manifesto, false);

            manifestoEntregaService.generateEventoDocto(med, manifesto, me, ConstantesSim.EVENTO_MANIFESTO_EMITIDO, null);

            this.generateEventoDoctoVolume((Conhecimento) docto, PedidoColetaServiceConstants.CD_EM_ROTA_ENTREGA_DIRETA, null);
        } else {
            manifesto = this.storeManifestoEntrega(controleCarga, dataHoraAtual, ConstantesExpedicao.TP_MANIFESTO_ENTREGA_DIRETA);

            PreManifestoDocumento preManifestoDocto = this.storePreManifestoDoctoVolume(manifesto, docto);
            carregamentoMobileService.updateValoresManifesto(docto, manifesto, PedidoColetaServiceConstants.ADICIONA_VALORES);

            this.generateEventoDoctoVolume((Conhecimento) docto, PedidoColetaServiceConstants.CD_EVENTO_PRE_MANIFESTO, null);

            manifesto.setPreManifestoDocumentos(new ArrayList<PreManifestoDocumento>());
            manifesto.getPreManifestoDocumentos().add(preManifestoDocto);

            manifestoEntregaService.storeEmitirManifestoValidaManifestoEntrega(new TypedFlatMap(), manifesto, null, false);

            manifestoEntregaService.storeValidateManifestoEmitido(manifesto.getIdManifesto(), PedidoColetaServiceConstants.STATUS_MAN_EM_TRANS_COL_ENT);

            this.generateEventoDoctoVolume((Conhecimento) docto, PedidoColetaServiceConstants.CD_EM_ROTA_ENTREGA_DIRETA, null);
        }
    }

    public PreManifestoDocumento storePreManifestoDoctoVolume(Manifesto manifesto, DoctoServico docto) {
        PreManifestoDocumento preManifestoDocto = new PreManifestoDocumento();
        preManifestoDocto.setDoctoServico(docto);
        preManifestoDocto.setAwb(ctoAwbService.findAwbByIdCto(docto.getIdDoctoServico(), null));
        preManifestoDocto.setManifesto(manifesto);
        Integer ordem = preManifestoDocumentoService.findMaxNrOrdemByIdManifesto(manifesto.getIdManifesto());
        preManifestoDocto.setNrOrdem(++ordem);
        preManifestoDocumentoService.store(preManifestoDocto);

        List<VolumeNotaFiscal> volumeNotaFiscais = volumeNotaFiscalService.findByIdConhecimento(docto.getIdDoctoServico());

        for (VolumeNotaFiscal volume : volumeNotaFiscais) {
            //Monta o PreManifestoVolume
            PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
            preManifestoVolume.setDoctoServico(docto);
            preManifestoVolume.setManifesto(manifesto);
            preManifestoVolume.setVolumeNotaFiscal(volume);
            preManifestoVolume.setTpScan(new DomainValue(ConstantesSim.TP_SCAN_LMS));
            preManifestoVolume.setPreManifestoDocumento(preManifestoDocto);
            preManifestoVolumeService.store(preManifestoVolume);
        }
        return preManifestoDocto;
    }

    public Manifesto storeManifestoEntrega(ControleCarga controleCarga, DateTime dataHoraAtual, String tpManifestoEntrega) {
        Manifesto manifesto = new Manifesto();
        manifesto.setBlBloqueado(false);
        manifesto.setPsTotalManifesto(BigDecimalUtils.ZERO);
        manifesto.setPsTotalAforadoManifesto(BigDecimalUtils.ZERO);
        manifesto.setVlTotalManifesto(BigDecimalUtils.ZERO);
        manifesto.setMoeda(SessionUtils.getMoedaSessao());
        manifesto.setFilialByIdFilialOrigem(controleCarga.getFilialByIdFilialOrigem());
        manifesto.setTpStatusManifesto(new DomainValue(ConstantesExpedicao.TP_STATUS_MANIFESTO_EMITIDO));
        manifesto.setTpManifesto(new DomainValue(ConstantesExpedicao.TP_MANIFESTO_ENTREGA));
        manifesto.setTpAbrangencia(new DomainValue(ConstantesExpedicao.ABRANGENCIA_NACIONAL));
        manifesto.setNrPreManifesto(configuracoesFacade.incrementaParametroSequencial(SessionUtils
                .getFilialSessao().getIdFilial(), PedidoColetaServiceConstants.NR_PRE_MANIFESTO, true));

        manifesto.setDhGeracaoPreManifesto(dataHoraAtual);

        manifesto.setTpModal(new DomainValue(ConstantesExpedicao.MODAL_RODOVIARIO));

        manifesto.setControleCarga(controleCarga);
        manifesto.setTpManifestoEntrega(new DomainValue(tpManifestoEntrega));
        manifesto.setFilialByIdFilialDestino(SessionUtils.getFilialSessao());

        ControleTrecho controleTrecho = controleTrechoService.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(
                controleCarga.getIdControleCarga(), manifesto.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getFilialByIdFilialDestino().getIdFilial());

        manifesto.setControleTrecho(controleTrecho);

        manifestoService.storeBasic(manifesto);
        return manifesto;
    }

    public void validateAwb(PedidoColeta pedidoColeta, ItemList items, ItemListConfig config, Boolean hasAwb, Long idDoctoServico, Long idDetalheColeta) {
        Awb awb = awbService.findUltimoAwbByDoctoServico(idDoctoServico);
        Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
        /* Valida doctos/awb já inseridos */

        int awbNaoInserido = validateAwbIntersec(idDoctoServico, pedidoColeta, config, items, idDetalheColeta);

        if (awbNaoInserido != 0) {
            throw new BusinessException(hasAwb ? "LMS-02103" : "LMS-02101");
        }
		/* Valida se conheceimento(s) está(ão) com a localização diferente de
		 * 'Carga aguardando embarque Companhia Aérea' ou 'Em viagem aérea' */
        this.validateLocalizacaoDoctoServico(conhecimento, hasAwb);

		/* Valida filial de origem e/ou destino do AWB/Pré AWB */
        this.validateFilialSessao(awb, hasAwb);

        Long idPedidoColeta = pedidoColeta.getIdPedidoColeta() != null && pedidoColeta.getIdPedidoColeta() > 0 ? pedidoColeta.getIdPedidoColeta() : (Long) null;

		/* Valida se conhecimento(s) já possui(em) um pedido de coleta relacionado */
        this.validateExistePedidoColeta(conhecimento, hasAwb, idPedidoColeta);
    }

    public Integer validateAwbIntersec(Long idDoctoServico, PedidoColeta pedidoColeta, ItemListConfig config, ItemList items, Long idDetalheColeta) {
        int contadorDocumentosJaInseridos = 0;
        for (Iterator<DetalheColeta> iter = items.iterator(pedidoColeta.getIdPedidoColeta(), config); iter.hasNext(); ) {
            DetalheColeta detColAux = iter.next();

            if (idDetalheColeta == null || !idDetalheColeta.equals(detColAux.getIdDetalheColeta())) {
                Long idDocAux = detColAux.getDoctoServico() != null ? detColAux.getDoctoServico().getIdDoctoServico() : null;

                int doctoNaListaDeDetalhes = 0;
                if (idDocAux != null && idDocAux.equals(idDoctoServico)) {
                    contadorDocumentosJaInseridos = contadorDocumentosJaInseridos + 1;
                }
            }
        }
        return contadorDocumentosJaInseridos;
    }


    public List<Long> findIdsConhecimentoByAwb(Long idAwb, Long idDoctoServico) {
        List<Long> ids = new ArrayList<Long>();

        if (idAwb != null) {
            List<CtoAwb> ctoAwbs = ctoAwbService.findByIdAwb(idAwb);
            for (CtoAwb ca : ctoAwbs) {
                ids.add(ca.getConhecimento().getIdDoctoServico());
            }
        } else if (idDoctoServico != null) {
            ids.add(idDoctoServico);
        }
        return ids;
    }

    private void validateExistePedidoColeta(Conhecimento conhecimento, boolean isAwb, Long idPedidoColeta) {
        Integer qtPedidoColeta = this.findRowCountByIdDoctoServicoNotCanceled(conhecimento.getIdDoctoServico(), idPedidoColeta);
        if (qtPedidoColeta > 0) {
            throw new BusinessException(isAwb ? "LMS-02103" : "LMS-02101");
        }
    }

    public Integer findRowCountByIdDoctoServicoNotCanceled(Long idDoctoServico, Long idPedidoColeta) {
        return this.getPedidoColetaDAO().findRowCountByIdDoctoServicoNotCanceled(idDoctoServico, idPedidoColeta);
    }

    private void validateFilialSessao(Awb awb, boolean isAwb) {
        Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();

        if (PedidoColetaServiceConstants.TP_STATUS_AWB_PRE.equals(awb.getTpStatusAwb().getValue())) {
            if (!idFilialSessao.equals(awb.getFilialByIdFilialDestino().getIdFilial())
                    && !idFilialSessao.equals(awb.getFilialByIdFilialOrigem().getIdFilial())) {
                throw new BusinessException(isAwb ? "LMS-02097" : "LMS-02100");
            }
        } else {
            if (isAwb && !idFilialSessao.equals(awb.getFilialByIdFilialDestino().getIdFilial())) {
                throw new BusinessException("LMS-02098");
            }
        }
    }

    private void validateLocalizacaoDoctoServico(Conhecimento conhecimento, boolean isAwb) {
        LocalizacaoMercadoria lm = conhecimento.getLocalizacaoMercadoria();

        if (!PedidoColetaServiceConstants.CD_EM_VIAGEM_AEREA.equals(lm.getCdLocalizacaoMercadoria())
                && !PedidoColetaServiceConstants.CD_AGUARDANDO_EMBARQUE_CIA_AEREA.equals(lm.getCdLocalizacaoMercadoria())) {
            throw new BusinessException(isAwb ? "LMS-02096" : "LMS-02099");
        }
    }

    private void geraWorkFlowAprovacaoProdutosDiferenciados(PedidoColeta bean) {

        Pendencia pendencia = null;

        pendencia = generateWorkflow(SessionUtils.getFilialSessao().getIdFilial(), bean,
                ConstantesWorkflow.NR0202_APROVACAO_PROD_DIFERENCIADO_COLETA,
                "Foi solicitada uma coleta de produtos perigoso ou de risco. Coleta " +
                        bean.getFilialByIdFilialResponsavel().getSgFilial() + " " + bean.getNrColeta() + ". ");

        if (pendencia != null) {
            //seta a situação de Aprovação para “Pendente”.
            bean.setSituacaoAprovacao(domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW", PedidoColetaServiceConstants.WORKFLOW_EM_APROVACAO));

            //Setando o ID_PENDENCIA conforme o retorno da geração da pendência
            bean.setPendencia(pendencia);
        }
    }

    /**
     * Gera um workflow padrão a partir do pedidoColeta informado.
     *
     * @return Pendencia
     */
    private Pendencia generateWorkflow(Long filialId, PedidoColeta bean, Short cdWorkflow, String observacao) {
        return workflowPendenciaService.generatePendencia(filialId,
                cdWorkflow,
                bean.getIdPedidoColeta(),
                observacao,
                JTDateTimeUtils.getDataHoraAtual());
    }


    /**
     * Workflow utilizado no processo de Cadastrar Pedido Coleta.
     *
     * @param listIds
     * @param listTpSituacao
     * @return
     */
    public String executeWorkflow(List<Long> listIds, List<String> listTpSituacao) {
        Iterator<String> situacaoIt = listTpSituacao.iterator();
        for (Long id : listIds) {
            PedidoColeta pedidoColeta = findById(id);
            if (pedidoColeta != null) {
                pedidoColeta.setSituacaoAprovacao(new DomainValue(situacaoIt.next()));
                super.store(pedidoColeta);
            }
        }
        return null;
    }

    public String executeCancelarColeta(Long idPedidoColeta, Long idOcorrenciaColeta, String observacao) {
        return executeCancelarColeta(idPedidoColeta, idOcorrenciaColeta, observacao, false);
    }

    /**
     * Método que cancela uma coleta.
     * Adiciona um registro em eventoColeta e atualiza o tpStatusColeta em pedidoColeta.
     *
     * @param idPedidoColeta
     * @param idOcorrenciaColeta
     * @param observacao
     */
    public String executeCancelarColeta(Long idPedidoColeta, Long idOcorrenciaColeta, String observacao, boolean isAutoCancel) {
        return executeCancelarColeta(idPedidoColeta, idOcorrenciaColeta, observacao, isAutoCancel,
                JTDateTimeUtils.getDataHoraAtual(), SessionUtils.getUsuarioLogado());
    }

    /**
     * Método que cancela uma coleta.
     * Adiciona um registro em eventoColeta e atualiza o tpStatusColeta em pedidoColeta.
     *
     * @param idPedidoColeta
     * @param idOcorrenciaColeta
     * @param observacao
     * @param isAutoCancel
     * @param dataHoraAtual
     * @param usuario
     */
    public String executeCancelarColeta(Long idPedidoColeta, Long idOcorrenciaColeta, String observacao,
                                        boolean isAutoCancel, DateTime dataHoraAtual, Usuario usuario) {
        String statusColetaCancelada = null;
        // Carrega pedidoColeta
        PedidoColeta pedidoColeta = this.findById(idPedidoColeta);

        boolean hasDetalheExecutado = false;
        if (pedidoColeta.getTpPedidoColeta().getValue().equals(PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO)) {
            Integer doctosNaoExecutados = getRowCountDocumentosNaoExecutadosByPedidoColeta(idPedidoColeta);
            pedidoColeta.getDetalheColetas().size();
            hasDetalheExecutado = !doctosNaoExecutados.equals(pedidoColeta.getDetalheColetas().size());
        }

        boolean isColetaNaoAberta = (pedidoColeta.getTpStatusColeta().getValue() == null
                || !pedidoColeta.getTpStatusColeta().getValue().equalsIgnoreCase(PedidoColetaServiceConstants.TP_STATUS_COLETA_ABERTA)) && !isAutoCancel;

        if (hasDetalheExecutado || isColetaNaoAberta) {
            // lança a exceção, pois só pode ser cancelada uma coleta
            // que tem o status aberta. Condorme especificação 02.06.01.02
            throw new BusinessException("LMS-02001");
        }

        // Carrega a ocorrenciaColeta
        OcorrenciaColeta ocorrenciaColeta = ocorrenciaColetaService.findById(idOcorrenciaColeta);
        // instancia um evento coleta e atribui os valores que serão gravados
        EventoColeta eventoColeta = new EventoColeta();
        eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
        eventoColeta.setUsuario(usuario);
        eventoColeta.setDsDescricao(observacao);
        eventoColeta.setPedidoColeta(pedidoColeta);

        eventoColeta.setDhEvento(dataHoraAtual);

        //pedidoColeta.getEventoColetas().add(eventoColeta);
        DomainValue domainValue = new DomainValue(PedidoColetaServiceConstants.TP_EVENTO_COLETA_CANCELADA);
        eventoColeta.setTpEventoColeta(domainValue);
        // Armazena o eventoColeta
        eventoColetaService.store(eventoColeta);
        // grava no topic de eventos de pedido coleta
        eventoColetaService.storeMessageTopic(eventoColeta);

        generateEventosColetaAeroByPedidoColeta(pedidoColeta, ConstantesSim.EVENTO_REALIZAR_COLETA_NO_AEROPORTO, (Short[]) null);

        //LMSA-2244 desvincula conhecimentos
        pedidoColeta.setDetalheColetas(null);

        // Atualiza o tpStatusColeta do pedidoColeta
        pedidoColeta.setTpStatusColeta(domainValue);

        Cotacao cotacao = pedidoColeta.getCotacao();
        if (cotacao != null && !CotacaoService.SITUACAO_COTACAO_EFETIVADA.equals(cotacao.getTpSituacao().getValue())) {
            DomainValue tpSituacao = null;
            if (CotacaoService.SITUACAO_COTACAO_APROVADA.equals(cotacao.getTpSituacaoAprovacao().getValue())) {
                tpSituacao = new DomainValue(CotacaoService.SITUACAO_COTACAO_APROVADA);
            } else {
                tpSituacao = new DomainValue(CotacaoService.SITUACAO_COTACAO_COTADA);
            }
            cotacao.setTpSituacao(tpSituacao);
            pedidoColeta.setCotacao(cotacao);
        }


        this.store(pedidoColeta);
        statusColetaCancelada = domainValueService.findDomainValueDescription("DM_STATUS_COLETA", PedidoColetaServiceConstants.TP_EVENTO_COLETA_CANCELADA);
        statusColetaCancelada = statusColetaCancelada + "," + PedidoColetaServiceConstants.TP_EVENTO_COLETA_CANCELADA;

        return statusColetaCancelada;
    }

    /**
     * Altera a data do processo de coleta e dispara a geração das coletas automáticas e coletas milk run.
     *
     * @param filial             Filial para a qual serão geradas as coletas.
     * @param dataProcessoColeta Data para a geração das coletas.
     * @return StringBuffer log do processo.
     * @author Moacir Zardo Junior - 29/11/2005
     */
    public StringBuffer generateColetas(Filial filial, YearMonthDay dataProcessoColeta) {
        //Primeiro, salva a nova data (caso a data da tela foi modificada).
        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(filial.getIdFilial(), "DATA_PROCESSO_COLETA", true);
        conteudoParametroFilial.setVlConteudoParametroFilial(dataProcessoColeta.toString());
        conteudoParametroFilialService.store(conteudoParametroFilial);
        getPedidoColetaDAO().getAdsmHibernateTemplate().flush();

        //Primeira parte (Coletas Automáticas)
        StringBuffer logColetasAutomaticas = this.gerarColetas(filial, dataProcessoColeta).append("\n");

        //Segunda parte (Coletas Milk Run)
        StringBuffer logColetasMilkRun = this.gerarColetasMilkRun(filial, dataProcessoColeta);

        return logColetasAutomaticas.append(logColetasMilkRun);
    }

    /**
     * Rotina que gera as coletas Milk Run de acordo com a filial e
     * a data do processo de coleta passados por parâmetro.
     *
     * @param filial             Filial para a qual serão geradas as coletas Milk Run.
     * @param dataProcessoColeta Data para a geração das coletas Milk Run.
     * @return StringBuffer contendo o log do processo.
     * @author Moacir Zardo Junior - 29/11/2005
     */
    private StringBuffer gerarColetasMilkRun(Filial filial, YearMonthDay dataProcessoColeta) {
        DateTime dataHoraProcessoColeta = JTDateTimeUtils.yearMonthDayToDateTime(dataProcessoColeta, filial.getDateTimeZone());
        StringBuffer log = new StringBuffer();
        int coletasGeradas = 0;

        //Se não encontrar nenhuma ocorrência de coleta com evento "SO" não gera nenhuma coleta.
        List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO);
        OcorrenciaColeta ocorrenciaColeta = null;
        if (!listOcorrenciaColeta.isEmpty()) {
            ocorrenciaColeta = listOcorrenciaColeta.get(0);
        }
        List<SemanaRemetMrun> listSemRemet = semanaRemetMrunService.findSemanasRemetMrunByFaixaDiasAndFilialAtendeOperacional(filial, milkRunService.getSiglaSemana(dataProcessoColeta));

        int nroDiaSemana = JTDateTimeUtils.getNroDiaSemana(dataProcessoColeta);
        if (!listSemRemet.isEmpty()) {
            for (Iterator<SemanaRemetMrun> iter = listSemRemet.iterator(); iter.hasNext(); ) {
                if (ocorrenciaColeta != null) {
                    SemanaRemetMrun semanaRemetMrun = iter.next();
                    MilkRemetente milkRemetente = semanaRemetMrun.getMilkRemetente();
                    //Verifica se já foi gerado automaticamente um pedido para o cliente.
                    List<PedidoColeta> listPedido = findPedidoColetaMilkRun(semanaRemetMrun.getMilkRemetente().getCliente().getIdCliente(), dataProcessoColeta, filial.getDateTimeZone());
                    if (listPedido.isEmpty()) {
                        TimeOfDay horaInicial = semanaRemetMrunService.getHrInicialSemanaRemetMrun(semanaRemetMrun, nroDiaSemana);
                        TimeOfDay horaFinal = semanaRemetMrunService.getHrFinalSemanaRemetMrun(semanaRemetMrun, nroDiaSemana);
                        if (horaInicial != null && horaFinal != null) {
                            //Novo registro em Pedido Coleta
                            PedidoColeta pedidoColeta = new PedidoColeta();
                            pedidoColeta.setFilialByIdFilialSolicitante(filial);
                            pedidoColeta.setFilialByIdFilialResponsavel(filial);
                            pedidoColeta.setCliente(milkRemetente.getCliente());
                            pedidoColeta.setEnderecoPessoa(milkRemetente.getEnderecoPessoa());
                            pedidoColeta.setDhPedidoColeta(dataHoraProcessoColeta);

                            //DhColetaDisponível é a data do processo + hrInicial da coleta MilkRun
                            //de acordo com o dia da semana que a data se refere.
                            DateTime dhColetaDisponivel = dataProcessoColeta.toDateTime(horaInicial, filial.getDateTimeZone());

                            pedidoColeta.setDhColetaDisponivel(dhColetaDisponivel);
                            pedidoColeta.setDtPrevisaoColeta(dataProcessoColeta);
                            pedidoColeta.setHrLimiteColeta(horaFinal);

                            pedidoColeta.setEdColeta(milkRemetente.getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + milkRemetente.getEnderecoPessoa().getDsEndereco());
                            pedidoColeta.setDsBairro(milkRemetente.getEnderecoPessoa().getDsBairro());
                            pedidoColeta.setNrCep(milkRemetente.getEnderecoPessoa().getNrCep());
                            if (milkRemetente.getEnderecoPessoa().getNrEndereco() != null) {
                                pedidoColeta.setNrEndereco(milkRemetente.getEnderecoPessoa().getNrEndereco().trim());
                            }
                            pedidoColeta.setDsComplementoEndereco(milkRemetente.getEnderecoPessoa().getDsComplemento());
                            pedidoColeta.setMunicipio(milkRemetente.getEnderecoPessoa().getMunicipio());

                            TelefoneEndereco telefoneEndereco = telefoneEnderecoService.getTelefonePorTiposTelefone(milkRemetente.getEnderecoPessoa(), new String[]{"C", "R", "E"});
                            TelefoneContato telefoneContato = null;
                            if (telefoneEndereco != null) {
                                pedidoColeta.setNrTelefoneCliente(telefoneEndereco.getNrTelefone());
                                pedidoColeta.setNrDddCliente(telefoneEndereco.getNrDdd());
                                telefoneContato = telefoneContatoService.getTelefoneContato(telefoneEndereco);
                                if (telefoneContato != null) {
                                    pedidoColeta.setNmContatoCliente(telefoneContato.getContato().getNmContato());
                                } else {
                                    pedidoColeta.setNmContatoCliente(configuracoesFacade.getMensagem("LMS-02048"));
                                }
                            } else {
                                pedidoColeta.setNmContatoCliente(configuracoesFacade.getMensagem("LMS-02048"));
                                pedidoColeta.setNrTelefoneCliente("00000000");
                                pedidoColeta.setNrDddCliente("00");
                            }

                            pedidoColeta.setNmSolicitante(configuracoesFacade.getMensagem("automatico"));
                            pedidoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                            pedidoColeta.setMoeda(SessionUtils.getMoedaSessao());
                            pedidoColeta.setTpModoPedidoColeta(new DomainValue("MI"));
                            pedidoColeta.setTpPedidoColeta(new DomainValue("NO"));
                            pedidoColeta.setTpStatusColeta(new DomainValue("AB"));

                            //Seta pesos, valores e quantidades para 0.
                            pedidoColeta.setVlTotalInformado(BigDecimalUtils.ZERO);
                            pedidoColeta.setVlTotalVerificado(BigDecimalUtils.ZERO);
                            pedidoColeta.setPsTotalInformado(BigDecimalUtils.ZERO);
                            pedidoColeta.setPsTotalVerificado(BigDecimalUtils.ZERO);
                            pedidoColeta.setPsTotalAforadoInformado(BigDecimalUtils.ZERO);
                            pedidoColeta.setPsTotalAforadoVerificado(BigDecimalUtils.ZERO);
                            pedidoColeta.setQtTotalVolumesInformado(Integer.valueOf(0));
                            pedidoColeta.setQtTotalVolumesVerificado(Integer.valueOf(0));

                            pedidoColeta.setBlClienteLiberadoManual(Boolean.FALSE);
                            pedidoColeta.setBlAlteradoPosProgramacao(Boolean.FALSE);
                            pedidoColeta.setBlProdutoDiferenciado(ClienteUtils.isClienteComProdutoDiferenciado(milkRemetente.getCliente()));
                            pedidoColeta.setBlProdutoPerigoso(ClienteUtils.isClienteComProdutoPerigoso(milkRemetente.getCliente()));

                            RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
                                    milkRemetente.getEnderecoPessoa().getNrCep(),
                                    milkRemetente.getCliente().getIdCliente(),
                                    milkRemetente.getEnderecoPessoa().getIdEnderecoPessoa(),
                                    filial.getIdFilial(), //LMS-1321
                                    JTDateTimeUtils.getDataAtual());
                            if (rotaIntervaloCep != null) {
                                pedidoColeta.setRotaIntervaloCep(rotaIntervaloCep);
                                pedidoColeta.setRotaColetaEntrega(rotaIntervaloCep.getRotaColetaEntrega());

                                pedidoColeta.setMilkRun(milkRemetente.getMilkRun());

                                //Novo detalhe coleta
                                DetalheColeta detalheColeta = new DetalheColeta();
                                detalheColeta.setServico(milkRemetente.getServico());
                                EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(milkRemetente.getMilkRun().getCliente().getIdCliente());
                                if (enderecoPessoa != null) {
                                    detalheColeta.setMunicipio(enderecoPessoa.getMunicipio());
                                }
                                detalheColeta.setNaturezaProduto(milkRemetente.getNaturezaProduto());
                                detalheColeta.setCliente(milkRemetente.getMilkRun().getCliente());
                                detalheColeta.setMoeda(SessionUtils.getMoedaSessao());
                                detalheColeta.setTpFrete(new DomainValue("F"));
                                detalheColeta.setQtVolumes(Integer.valueOf(0));
                                detalheColeta.setVlMercadoria(BigDecimalUtils.ZERO);
                                detalheColeta.setPsMercadoria(BigDecimalUtils.ZERO);
                                detalheColeta.setPsAforado(BigDecimalUtils.ZERO);
                                detalheColeta.setObDetalheColeta(configuracoesFacade.getMensagem("LMS-02049"));

                                //Novo evento coleta
                                EventoColeta eventoColeta = new EventoColeta();
                                eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                                eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                                eventoColeta.setTpEventoColeta(new DomainValue(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO));
                                eventoColeta.setDhEvento(dataHoraProcessoColeta);
                                eventoColeta.setDsDescricao(configuracoesFacade.getMensagem("LMS-02049"));

                                //Associando entidades...
                                eventoColeta.setPedidoColeta(pedidoColeta);
                                detalheColeta.setPedidoColeta(pedidoColeta);

                                Long maiorNro = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_COLETA", true);
                                pedidoColeta.setNrColeta(maiorNro);

                                //Salvando entidades...
                                this.store(pedidoColeta);
                                eventoColetaService.store(eventoColeta);
                                // grava no topic de eventos de pedido coleta
                                eventoColetaService.storeMessageTopic(eventoColeta);
                                detalheColetaService.store(detalheColeta);
                                getPedidoColetaDAO().getAdsmHibernateTemplate().flush();
                                coletasGeradas++;
                            } else {
                                // "Milk Run para o cliente milkRemetente.getMilkRun().getCliente().getNmFantasia()
                                // não será gerada automaticamente pois nenhuma rota de coleta entrega foi encontrada para o municipio x e cep y
                                String strParametrosMensagem[] = {milkRemetente.getCliente().getPessoa().getTpIdentificacao().getDescription().getValue(),
                                        FormatUtils.formatIdentificacao(milkRemetente.getCliente().getPessoa()),
                                        milkRemetente.getEnderecoPessoa().getMunicipio().getNmMunicipio(),
                                        milkRemetente.getEnderecoPessoa().getNrCep()
                                };
                                log.append(configuracoesFacade.getMensagem("LMS-02051", strParametrosMensagem) + "\n");
                            }
                        } else {
                            // "Hora final ou hora inicial não preenchidos para o dia da semana
                            // para o remetente milkRemetente.getCliente().getNmFantasia()
                            String strParametrosMensagem[] = {milkRemetente.getCliente().getPessoa().getTpIdentificacao().getDescription().getValue(),
                                    FormatUtils.formatIdentificacao(milkRemetente.getCliente().getPessoa())};
                            log.append(configuracoesFacade.getMensagem("LMS-02052", strParametrosMensagem) + "\n");
                        }
                    } else {
                        // "Coleta Milk Run para Cliente X já foi gerada para a data informada.
                        String strParametrosMensagem[] = {milkRemetente.getCliente().getPessoa().getTpIdentificacao().getDescription().getValue(),
                                FormatUtils.formatIdentificacao(milkRemetente.getCliente().getPessoa())};
                        log.append(configuracoesFacade.getMensagem("LMS-02053", strParametrosMensagem) + "\n");
                    }
                } else {
                    // "Nenhuma ocorrencia de coleta para o evento "SO"
                    log.append(configuracoesFacade.getMensagem("LMS-02054") + "\n");
                }
            }
            if (coletasGeradas > 0) {
                if (coletasGeradas == 1) {
                    log.append(configuracoesFacade.getMensagem("LMS-02055") + "\n");
                } else {
                    String strParametrosMensagem[] = {Integer.toString(coletasGeradas)};
                    log.append(configuracoesFacade.getMensagem("LMS-02056", strParametrosMensagem) + "\n");
                }
            }
        } else {
            // "Nenhuma Coleta Milk Run a ser gerada."
            log.append(configuracoesFacade.getMensagem("LMS-02057") + "\n");
        }
        return log;
    }

    /**
     * Rotina que gera as coletas (exceto Milk Run) de acordo com a filial e a
     * data do processo de coleta passados por parâmetro.
     *
     * @param filial             Filial para a qual serão geradas as coletas.
     * @param dataProcessoColeta Data para a geração das coletas.
     * @return StringBuffer contendo o log do processo.
     * @author Moacir Zardo Junior - 29/11/2005
     */
    private StringBuffer gerarColetas(Filial filial, YearMonthDay dataProcessoColeta) {
        DateTime dataHoraProcessoColeta = JTDateTimeUtils.yearMonthDayToDateTime(dataProcessoColeta, filial.getDateTimeZone());
        StringBuffer log = new StringBuffer();
        int coletasGeradas = 0;

        //Se não encontrar nenhuma ocorrência de coleta com evento "SO" não gera nenhuma coleta.
        List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO);
        OcorrenciaColeta ocorrenciaColeta = null;
        if (!listOcorrenciaColeta.isEmpty()) {
            ocorrenciaColeta = listOcorrenciaColeta.get(0);
        }
        List<ColetaAutomaticaCliente> result = coletaAutomaticaClienteService.findColetasAutomaticasClienteByDiaSemanaAndFilialAtendeOperacional(filial, JTDateTimeUtils.getNroDiaSemana(dataProcessoColeta));
        if (!result.isEmpty()) {
            if (ocorrenciaColeta != null) {
                for (Iterator<ColetaAutomaticaCliente> iter = result.iterator(); iter.hasNext(); ) {
                    ColetaAutomaticaCliente coletaAutomaticaCliente = iter.next();

                    //Verifica se já foi gerado automaticamente um pedido para o cliente.
                    List<PedidoColeta> listPedido = findPedidoColetaAutomatica(coletaAutomaticaCliente.getCliente().getIdCliente(), dataProcessoColeta, filial.getDateTimeZone());
                    if (listPedido.isEmpty()) {
                        //Novo registro em Pedido Coleta
                        PedidoColeta pedidoColeta = new PedidoColeta();
                        pedidoColeta.setFilialByIdFilialSolicitante(filial);
                        pedidoColeta.setFilialByIdFilialResponsavel(filial);
                        pedidoColeta.setCliente(coletaAutomaticaCliente.getCliente());
                        pedidoColeta.setDhPedidoColeta(dataHoraProcessoColeta);

                        //DhColetaDisponível é a data do processo + hrChegada da coleta
                        // automatica cliente.
                        //Calendar dhColetaDisponivel = Calendar.getInstance();
                        //dhColetaDisponivel.setTime(dataProcessoColeta);
                        DateTime dhColetaDisponivel = dataProcessoColeta.toDateTime(coletaAutomaticaCliente.getHrChegada(), filial.getDateTimeZone());
                        pedidoColeta.setDhColetaDisponivel(dhColetaDisponivel);

                        pedidoColeta.setDtPrevisaoColeta(dataProcessoColeta);
                        pedidoColeta.setHrLimiteColeta(coletaAutomaticaCliente.getHrSaida());

                        pedidoColeta.setEnderecoPessoa(coletaAutomaticaCliente.getEnderecoPessoa());
                        pedidoColeta.setEdColeta(coletaAutomaticaCliente.getEnderecoPessoa().getTipoLogradouro().getDsTipoLogradouro() + " " + coletaAutomaticaCliente.getEnderecoPessoa().getDsEndereco());
                        pedidoColeta.setDsBairro(coletaAutomaticaCliente.getEnderecoPessoa().getDsBairro());
                        pedidoColeta.setNrCep(coletaAutomaticaCliente.getEnderecoPessoa().getNrCep());

                        if (coletaAutomaticaCliente.getEnderecoPessoa().getNrEndereco() != null) {
                            pedidoColeta.setNrEndereco(coletaAutomaticaCliente.getEnderecoPessoa().getNrEndereco().trim());
                        }

                        pedidoColeta.setDsComplementoEndereco(coletaAutomaticaCliente.getEnderecoPessoa().getDsComplemento());
                        pedidoColeta.setMunicipio(coletaAutomaticaCliente.getEnderecoPessoa().getMunicipio());

                        TelefoneEndereco telefoneEndereco = telefoneEnderecoService.getTelefonePorTiposTelefone(coletaAutomaticaCliente.getEnderecoPessoa(), new String[]{"C", "R", "E"});
                        TelefoneContato telefoneContato = null;
                        if (telefoneEndereco != null) {
                            pedidoColeta.setNrTelefoneCliente(telefoneEndereco.getNrTelefone());
                            pedidoColeta.setNrDddCliente(telefoneEndereco.getNrDdd());
                            telefoneContato = telefoneContatoService.getTelefoneContato(telefoneEndereco);
                            if (telefoneContato != null) {
                                pedidoColeta.setNmContatoCliente(telefoneContato.getContato().getNmContato());
                            } else {
                                pedidoColeta.setNmContatoCliente(configuracoesFacade.getMensagem("LMS-02048"));
                            }
                        } else {
                            pedidoColeta.setNmContatoCliente(configuracoesFacade.getMensagem("LMS-02048"));
                            pedidoColeta.setNrTelefoneCliente("00000000");
                            pedidoColeta.setNrDddCliente("00");
                        }

                        pedidoColeta.setNmSolicitante(configuracoesFacade.getMensagem("automatico"));
                        pedidoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                        pedidoColeta.setMoeda(SessionUtils.getMoedaSessao());
                        pedidoColeta.setTpModoPedidoColeta(new DomainValue("AU"));
                        pedidoColeta.setTpPedidoColeta(new DomainValue("NO"));
                        pedidoColeta.setTpStatusColeta(new DomainValue("AB"));

                        //Seta pesos, valores e quantidades para 0.
                        pedidoColeta.setVlTotalInformado(BigDecimalUtils.ZERO);
                        pedidoColeta.setVlTotalVerificado(BigDecimalUtils.ZERO);
                        pedidoColeta.setPsTotalInformado(BigDecimalUtils.ZERO);
                        pedidoColeta.setPsTotalVerificado(BigDecimalUtils.ZERO);
                        pedidoColeta.setPsTotalAforadoInformado(BigDecimalUtils.ZERO);
                        pedidoColeta.setPsTotalAforadoVerificado(BigDecimalUtils.ZERO);
                        pedidoColeta.setQtTotalVolumesInformado(Integer.valueOf(0));
                        pedidoColeta.setQtTotalVolumesVerificado(Integer.valueOf(0));

                        pedidoColeta.setBlClienteLiberadoManual(Boolean.FALSE);
                        pedidoColeta.setBlAlteradoPosProgramacao(Boolean.FALSE);

                        pedidoColeta.setBlProdutoDiferenciado(ClienteUtils.isClienteComProdutoDiferenciado(coletaAutomaticaCliente.getCliente()));
                        pedidoColeta.setBlProdutoPerigoso(ClienteUtils.isClienteComProdutoPerigoso(coletaAutomaticaCliente.getCliente()));

                        RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
                                coletaAutomaticaCliente.getEnderecoPessoa().getNrCep(),
                                coletaAutomaticaCliente.getCliente().getIdCliente(),
                                coletaAutomaticaCliente.getEnderecoPessoa().getIdEnderecoPessoa(),
                                filial.getIdFilial(), //LMS-1321
                                JTDateTimeUtils.getDataAtual());
                        if (rotaIntervaloCep != null) {
                            pedidoColeta.setRotaIntervaloCep(rotaIntervaloCep);
                            pedidoColeta.setRotaColetaEntrega(rotaIntervaloCep.getRotaColetaEntrega());

                            //Novo detalhe coleta
                            DetalheColeta detalheColeta = new DetalheColeta();
                            detalheColeta.setServico(coletaAutomaticaCliente.getServico());
                            detalheColeta.setNaturezaProduto(coletaAutomaticaCliente.getNaturezaProduto());
                            detalheColeta.setMoeda(SessionUtils.getMoedaSessao());
                            detalheColeta.setTpFrete(new DomainValue("C"));
                            detalheColeta.setQtVolumes(Integer.valueOf(0));
                            detalheColeta.setVlMercadoria(BigDecimalUtils.ZERO);
                            detalheColeta.setPsMercadoria(BigDecimalUtils.ZERO);
                            detalheColeta.setPsAforado(BigDecimalUtils.ZERO);
                            detalheColeta.setObDetalheColeta(configuracoesFacade.getMensagem("LMS-02050"));

                            //Novo evento de coleta
                            EventoColeta eventoColeta = new EventoColeta();
                            eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                            eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                            eventoColeta.setTpEventoColeta(new DomainValue(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO));
                            eventoColeta.setDhEvento(dataHoraProcessoColeta);
                            eventoColeta.setDsDescricao(configuracoesFacade.getMensagem("LMS-02050"));

                            //Associando entidades...
                            eventoColeta.setPedidoColeta(pedidoColeta);
                            detalheColeta.setPedidoColeta(pedidoColeta);

                            Long maiorNro = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_COLETA", true);
                            pedidoColeta.setNrColeta(maiorNro);

                            //Salvando entidades...
                            if (null == pedidoColeta.getBlProdutoDiferenciado()) {
                                pedidoColeta.setBlProdutoDiferenciado(false);
                            }
                            getPedidoColetaDAO().store(pedidoColeta);
                            eventoColetaService.store(eventoColeta);
                            // grava no topic de eventos de pedido coleta
                            eventoColetaService.storeMessageTopic(eventoColeta);
                            detalheColetaService.store(detalheColeta);

                            getPedidoColetaDAO().getAdsmHibernateTemplate().flush();
                            coletasGeradas++;
                        } else {
                            // "Coleta para o cliente C não será gerada automaticamente pois nenhuma rota de
                            // coleta entrega foi encontrada para o municipio x e cep y
                            String strParametrosMensagem[] = {coletaAutomaticaCliente.getCliente().getPessoa().getTpIdentificacao().getDescription().getValue(),
                                    FormatUtils.formatIdentificacao(coletaAutomaticaCliente.getCliente().getPessoa()),
                                    coletaAutomaticaCliente.getEnderecoPessoa().getMunicipio().getNmMunicipio(),
                                    coletaAutomaticaCliente.getEnderecoPessoa().getNrCep()};
                            log.append(configuracoesFacade.getMensagem("LMS-02058", strParametrosMensagem) + "\n");
                        }
                    } else {
                        // "Coleta Automática para Cliente X já foi gerada para a data informada.
                        String strParametrosMensagem[] = {coletaAutomaticaCliente.getCliente().getPessoa().getTpIdentificacao().getDescription().getValue(),
                                FormatUtils.formatIdentificacao(coletaAutomaticaCliente.getCliente().getPessoa())};
                        log.append(configuracoesFacade.getMensagem("LMS-02059", strParametrosMensagem) + "\n");
                    }
                }
            } else {
                // "Nenhuma ocorrencia de coleta para o evento "SO"
                log.append(configuracoesFacade.getMensagem("LMS-02060") + "\n");
            }
            if (coletasGeradas > 0) {
                if (coletasGeradas == 1) {
                    log.append(configuracoesFacade.getMensagem("LMS-02061") + "\n");
                } else {
                    String strParametrosMensagem[] = {Integer.toString(coletasGeradas)};
                    log.append(configuracoesFacade.getMensagem("LMS-02062", strParametrosMensagem) + "\n");
                }
            }
        } else {
            // "Nenhuma Coleta Automática a ser gerada."
            log.append(configuracoesFacade.getMensagem("LMS-02063") + "\n");
        }
        return log;
    }

    /**
     * Método que faz o findLookup e adiciona no map
     * um campo enderecoCompleto contendo as informações de endereço
     * da tabela pedidoColeta
     *
     * @param nrColeta
     * @param idPedidoColeta
     * @param idFilial
     * @return List
     */
    public List findLookupComEnderecoCompleto(Long nrColeta, Long idPedidoColeta, Long idFilial) {

        FilterList filter = new FilterList(getPedidoColetaDAO().findPedidoColetaForLookup(nrColeta, idPedidoColeta, idFilial)) {
            public Map filterItem(Object item) {
                PedidoColeta pc = (PedidoColeta) item;
                TypedFlatMap typedFlatMap = new TypedFlatMap();

                typedFlatMap.put(PedidoColetaServiceConstants.ID_PEDIDO_COLETA, pc.getIdPedidoColeta());
                typedFlatMap.put(PedidoColetaServiceConstants.NR_COLETA, pc.getNrColeta());
                typedFlatMap.put("filialPesquisa.sgFilial", pc.getFilialByIdFilialResponsavel().getSgFilial());
                typedFlatMap.put("filialPesquisa.idFilial", pc.getFilialByIdFilialResponsavel().getIdFilial());

                typedFlatMap.put("tpStatusColeta.description", pc.getTpStatusColeta().getDescription());
                typedFlatMap.put("tpStatusColeta.value", pc.getTpStatusColeta().getValue());
                typedFlatMap.put("filialByIdFilialSolicitante.sgFilial", pc.getFilialByIdFilialSolicitante().getSgFilial());
                typedFlatMap.put("filialByIdFilialSolicitante.pessoa.nmFantasia", pc.getFilialByIdFilialSolicitante().getPessoa().getNmFantasia());
                typedFlatMap.put("filialByIdFilialResponsavel.sgFilial", pc.getFilialByIdFilialResponsavel().getSgFilial());
                typedFlatMap.put("filialByIdFilialResponsavel.pessoa.nmFantasia", pc.getFilialByIdFilialResponsavel().getPessoa().getNmFantasia());
                typedFlatMap.put("dhPedidoColeta", pc.getDhPedidoColeta());
                typedFlatMap.put("usuario.idUsuario", pc.getUsuario().getNrMatricula());
                typedFlatMap.put("usuario.nmUsuario", pc.getUsuario().getNmUsuario());

                typedFlatMap.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pc.getCliente().getPessoa()));
                typedFlatMap.put("cliente.pessoa.nmPessoa", pc.getCliente().getPessoa().getNmPessoa());
                typedFlatMap.put("enderecoCompleto", montaEnderecoCompleto(pc));

                typedFlatMap.put("municipio.nmMunicipio", pc.getMunicipio().getNmMunicipio());
                typedFlatMap.put("municipio.unidadeFederativa.sgUnidadeFederativa", pc.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
                typedFlatMap.put("nmContatoCliente", pc.getNmContatoCliente());
                typedFlatMap.put("nrTelefoneCliente", pc.getNrDddCliente() + " " + pc.getNrTelefoneCliente());
                typedFlatMap.put("qtTotalVolumesVerificado", pc.getQtTotalVolumesVerificado());
                typedFlatMap.put("psTotalVerificado", pc.getPsTotalVerificado());
                typedFlatMap.put("vlTotalVerificado", pc.getVlTotalVerificado());
                typedFlatMap.put("psTotalAforadoVerificado", pc.getPsTotalAforadoVerificado());

                return typedFlatMap;
            }
        };
        return (List) filter.doFilter();
    }

    /**
     * Método que monta o endereço completo do PedidoColeta
     *
     * @param bean
     * @return String
     */
    private String montaEnderecoCompleto(PedidoColeta bean) {
        boolean quebra = false;
        StringBuffer sb = new StringBuffer();

        if (!StringUtils.isBlank(bean.getEdColeta())) {
            sb.append(bean.getEdColeta());
        }

        if (!StringUtils.isBlank(bean.getNrEndereco())) {
            sb.append(", ");
            sb.append(bean.getNrEndereco());
        }

        if (!StringUtils.isBlank(bean.getDsComplementoEndereco())) {
            sb.append(", ");
            sb.append(bean.getDsComplementoEndereco());
            sb.append("\n");
            quebra = true;
        }

        if (!quebra) {
            sb.append("\n");
        }

        if (!StringUtils.isBlank(bean.getDsBairro())) {
            sb.append(bean.getDsBairro());
            sb.append("\n");
        }

        if (bean.getNrCep() != null) {
            sb.append(bean.getNrCep());
        }
        return sb.toString();
    }

    /**
     * Realiza uma constula de coletas não programadas de acordo com os filtros recebidos por parâmetro.
     *
     * @param criteria
     * @return
     */
    public List<TypedFlatMap> findPaginatedByColetasNaoProgramadas(Map<String, Object> criteria) {
        List<Object[]> list = getPedidoColetaDAO().findPaginatedByColetasNaoProgramadas(criteria);
        List<TypedFlatMap> newList = new ArrayList<TypedFlatMap>(list.size());
        for (Object[] obj : list) {
            TypedFlatMap map = new TypedFlatMap();
            map.put(PedidoColetaServiceConstants.ID_PEDIDO_COLETA, obj[PedidoColetaServiceConstants.PROJECTION_ID_PEDIDO_COLETA]);
            map.put(PedidoColetaServiceConstants.SG_FILIAL, obj[PedidoColetaServiceConstants.PROJECTION_SG_FILIAL]);
            map.put(PedidoColetaServiceConstants.NR_COLETA, obj[PedidoColetaServiceConstants.PROJECTION_NR_COLETA]);
            map.put(PedidoColetaServiceConstants.DS_ROTA, obj[10] + " " + obj[PedidoColetaServiceConstants.PROJECTION_DS_ROTA]);
            map.put(PedidoColetaServiceConstants.NM_PESSOA, obj[PedidoColetaServiceConstants.PROJECTION_NM_PESSOA]);
            map.put(PedidoColetaServiceConstants.TP_IDENTIFICACAO, obj[PedidoColetaServiceConstants.PROJECTION_TP_IDENTIFICACAO]);
            map.put(PedidoColetaServiceConstants.NR_IDENTIFICACAO, obj[PedidoColetaServiceConstants.PROJECTION_NR_IDENTIFICACAO]);
            map.put(PedidoColetaServiceConstants.BL_PRIORIZAR, convertStringParaBoolean(String.valueOf(obj[PedidoColetaServiceConstants.PROJECTION_BL_PRIORIZAR])));
            map.put(PedidoColetaServiceConstants.BL_PERIGOSO, convertStringParaBoolean(String.valueOf(obj[PedidoColetaServiceConstants.PROJECTION_BL_PERIGOSO])));
            map.put(PedidoColetaServiceConstants.TP_MODO_PEDIDO_COLETA, obj[PedidoColetaServiceConstants.PROJECTION_TP_MODO_PEDIDO_COLETA]);
            map.put(PedidoColetaServiceConstants.BL_PRODUTO_PERIGOSO, convertStringParaBoolean(String.valueOf(obj[PedidoColetaServiceConstants.PROJECTION_BL_PRODUTO_PERIGOSO])));
            newList.add(map);
        }
        return newList;
    }

	private Boolean convertStringParaBoolean(String stringParaConverter) {
		return "S".equals(stringParaConverter) ? Boolean.TRUE : Boolean.FALSE;
	}

    /**
     * Realiza uma constula de coletas programadas de acordo com os filtros recebidos por parâmetro.
     *
     * @param findDefinition
     * @param blAlteradoPosProgramacao
     * @return
     */
    public ResultSetPage findPaginatedByColetasProgramadas(FindDefinition findDefinition, Boolean blAlteradoPosProgramacao) {
        ResultSetPage rsp = getPedidoColetaDAO().findPaginatedByColetasProgramadas(findDefinition, blAlteradoPosProgramacao);
        rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList()));
        return rsp;
    }

    /**
     * Executa uma consulta das coletas programadas de acordo com os filtros informados. Retorna o número de resultados retornados.
     *
     * @param blAlteradoPosProgramacao
     * @return
     */
    public Integer getRowCountByColetasProgramadas(Boolean blAlteradoPosProgramacao) {
        Integer rowCount = getPedidoColetaDAO().getRowCountByColetasProgramadas(blAlteradoPosProgramacao);
        return rowCount;
    }

    public ResultSetPage findPaginatedColetasNaoRealizadas(Map<String, Object> criteria) {
        ResultSetPage rsp = getPedidoColetaDAO().findPaginatedColetasNaoRealizadas(criteria, FindDefinition.createFindDefinition(criteria));
        return rsp;
    }

    public Integer getRowCountColetasNaoRealizadas(Map<String, Object> criteria) {
        Integer rowCount = getPedidoColetaDAO().getRowCountColetasNaoRealizadas(criteria);
        return rowCount;
    }

    /**
     * Retorna o PedidoColeta passando o ID do Cliente
     */
    public List<PedidoColeta> findPedidoColetaByIdCliente(Long idCliente) {
        return getPedidoColetaDAO().findPedidoColetaByIdCliente(idCliente);
    }

    public List<Map<String, Object>> findLookupPedidoColeta(Long idCliente) {
        List<Map<String, Object>> result = getPedidoColetaDAO().findLookupPedidoColeta(idCliente);

        return ajustaRetorno(result);
    }

    public List<Map<String, Object>> findLookupPedidoColeta(Long idCliente, Filial filial){
        List<Map<String, Object>> result = getPedidoColetaDAO().findLookupPedidoColeta(idCliente, filial);

        return ajustaRetorno(result);
    }
    public java.io.Serializable storeAlteracaoValoresColeta(PedidoColeta bean) {
        PedidoColeta pedidoColeta = findById(bean.getIdPedidoColeta());
        pedidoColeta.setBlAlteradoPosProgramacao(Boolean.TRUE);
        pedidoColeta.setQtTotalVolumesVerificado(bean.getQtTotalVolumesVerificado());
        pedidoColeta.setVlTotalVerificado(bean.getVlTotalVerificado());
        pedidoColeta.setPsTotalVerificado(bean.getPsTotalVerificado());
        Serializable obj = store(pedidoColeta);

        if (pedidoColeta.getManifestoColeta() != null) {
            Long idControleCarga = pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga();
            controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(idControleCarga, Boolean.FALSE, SessionUtils.getFilialSessao());
        }
        return obj;
    }


    /**
     * @param idMeioTransporte
     * @param idControleCarga
     * @param idPedidoColeta
     * @param dsMotivoByLiberacao
     */
    public void generateTransmitirColeta(Long idMeioTransporte, Long idControleCarga, Long idPedidoColeta, String dsMotivoByLiberacao) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

        if (!StringUtils.isBlank(dsMotivoByLiberacao)) {
            controleCargaService.generateEventoControleCargaForLiberarEmissaoControleCarga(idControleCarga, dsMotivoByLiberacao);
        }

        Map<String, Object> mapValidaVeiculo = validaVeiculo(idControleCarga, idMeioTransporte);
        ManifestoColeta manifestoColeta = (ManifestoColeta) mapValidaVeiculo.get(MANIFESTO_COLETA);

        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        BigDecimal nrCapacidadeKg = controleCarga.getMeioTransporteByIdTransportado().getNrCapacidadeKg();

        PedidoColeta pedidoColeta = findById(idPedidoColeta);
        if (pedidoColeta.getPsTotalVerificado().add(controleCarga.getPsTotalFrota() != null ? controleCarga.getPsTotalFrota() : BigDecimalUtils.ZERO).compareTo(nrCapacidadeKg) > 0) {
            throw new BusinessException("LMS-02016");
        }

        pedidoColeta.setTpStatusColeta(new DomainValue(PedidoColetaServiceConstants.TP_STATUS_COLETA_TRANSMITIDA));
        pedidoColeta.setBlConfirmacaoVol(false);
        pedidoColeta.setDhConfirmacaoVol(null);
        pedidoColeta.setManifestoColeta(manifestoColeta);
        store(pedidoColeta);

        controleCargaService.generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga, Boolean.FALSE, SessionUtils.getFilialSessao());

        Map<String, Object> mapOcorrenciaColeta = new HashMap<String, Object>();
        mapOcorrenciaColeta.put("tpEventoColeta", PedidoColetaServiceConstants.TP_EVENTO_COLETA_TRANSMITIDA);
        List<OcorrenciaColeta> listaOcorrenciaColeta = ocorrenciaColetaService.find(mapOcorrenciaColeta);
        OcorrenciaColeta ocorrenciaColeta = listaOcorrenciaColeta.get(0);

        MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
        meioTransporteRodoviario.setIdMeioTransporte(idMeioTransporte);
        EventoColeta eventoColeta = new EventoColeta();
        eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
        eventoColeta.setDhEvento(dataHoraAtual);
        eventoColeta.setTpEventoColeta(new DomainValue(PedidoColetaServiceConstants.TP_EVENTO_COLETA_TRANSMITIDA));
        eventoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
        eventoColeta.setPedidoColeta(pedidoColeta);
        eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
        eventoColetaService.store(eventoColeta);
        // grava no topic de eventos de pedido coleta
        eventoColetaService.storeMessageTopic(eventoColeta);

        Map<String, Object> mapEficienciaVeiculoColeta = new HashMap<String, Object>();
        Map<String, Object> mapMeioTransporteRodoviario = new HashMap<String, Object>();
        mapMeioTransporteRodoviario.put("idMeioTransporte", meioTransporteRodoviario.getIdMeioTransporte());
        mapEficienciaVeiculoColeta.put("meioTransporteRodoviario", mapMeioTransporteRodoviario);
        mapEficienciaVeiculoColeta.put("dtMovimentacao", dataHoraAtual.toYearMonthDay());
        List<EficienciaVeiculoColeta> listEficienciaVeiculoColeta = eficienciaVeiculoColetaService.find(mapEficienciaVeiculoColeta);
        if (listEficienciaVeiculoColeta.isEmpty()) {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = inicializaEficienciaVeiculoColeta();
            eficienciaVeiculoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
            eficienciaVeiculoColeta.setDtMovimentacao(dataHoraAtual.toYearMonthDay());
            eficienciaVeiculoColeta.setQtTransmitidas(Integer.valueOf(1));
            eficienciaVeiculoColeta.setQtProgramadasNaData(Integer.valueOf(1));
            eficienciaVeiculoColeta.setQtTotalProgramacoes(Integer.valueOf(1));
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        } else {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = listEficienciaVeiculoColeta.get(0);
            eficienciaVeiculoColeta.setQtTransmitidas(Integer.valueOf(eficienciaVeiculoColeta.getQtTransmitidas().intValue() + 1));
            eficienciaVeiculoColeta.setQtProgramadasNaData(Integer.valueOf(eficienciaVeiculoColeta.getQtProgramadasNaData().intValue() + 1));
            eficienciaVeiculoColeta.setQtTotalProgramacoes(Integer.valueOf(eficienciaVeiculoColeta.getQtTotalProgramacoes().intValue() + 1));

            Map<String, Object> mapEventoColeta = new HashMap<String, Object>();
            mapEventoColeta.put("tpEventoColeta", PedidoColetaServiceConstants.TP_EVENTO_COLETA_RETORNO);

            //Buscar pelo idPedidoColeta e tipoEventoColeta = 'Retorno Coleta'
            Integer qtdEventoColeta = eventoColetaService.findQtdEventoColeta(idPedidoColeta, "RC");
            if (qtdEventoColeta != null && qtdEventoColeta.compareTo(0) > 0) {
                eficienciaVeiculoColeta.setQtProgramadasMaisUmaVez(Integer.valueOf(eficienciaVeiculoColeta.getQtProgramadasMaisUmaVez().intValue() + 1));
            }

            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        }
    }

    public boolean validateIfIsColetaFinalizadaExecutadaOuNoTerminal(PedidoColeta coleta) {
        if (coleta != null) {
            return ConstantesColeta.COLETA_EXECUTADA.isEqualTo(coleta.getTpStatusColeta())
                    || ConstantesColeta.COLETA_FINALIZADA.isEqualTo(coleta.getTpStatusColeta())
                    || ConstantesColeta.COLETA_NO_TERMINAL.isEqualTo(coleta.getTpStatusColeta());
        }

        return false;
    }

    /**
     * Retorna os códigos dos PCEs para o processo que executa Coletas/Entregas
     *
     * @param idsPedidosColeta
     * @return List com objetos Long
     */
    public List<Long> validatePCEExecutarColetasEntregas(List<Long> idsPedidosColeta) {
        return this.validatePCE(idsPedidosColeta, OcorrenciaPce.ID_OCORR_PCE_COL_EXEC_EXECUCAO_COLETA);
    }

    /**
     * Retorna os códigos dos PCEs para o processo de retorno de Coletas/Entregas
     *
     * @param idsPedidosColeta
     * @return LIst com objetos Long
     */
    public List<Long> validatePCERetornarColetasEntregas(List<Long> idsPedidosColeta) {
        return this.validatePCE(idsPedidosColeta, OcorrenciaPce.ID_OCORR_PCE_COL_CANC_EXECUCAO_COLETA);
    }

    /**
     * Retorna os códigos dos PCEs para o processo de executar/retornar Coletas/Entregas
     *
     * @param idsPedidosColeta
     * @param ocorrenciaPce
     * @return List com objetos Long
     */
    private List<Long> validatePCE(List<Long> idsPedidosColeta, long ocorrenciaPce) {
        Set<Long> set = new HashSet<Long>();
        for (Long idPedidoColeta : idsPedidosColeta) {
            Cliente cliente = clienteService.findByIdPedidoColeta(idPedidoColeta);
            Long codigoPceRemetente = versaoDescritivoPceService.findCodigoVersaoDescritivoPceByCriteria(
                    cliente.getIdCliente(),
                    Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_COLETA),
                    Long.valueOf(EventoPce.ID_EVENTO_PCE_EXECUCAO_COLETA),
                    Long.valueOf(ocorrenciaPce));
            if (codigoPceRemetente != null) {
                set.add(codigoPceRemetente);
            }
        }
        return new ArrayList<Long>(set);
    }

    @Deprecated
    public String generateExecutarColetasPendentes(List<Long> ids, DateTime dhEvento) {
        List<OcorrenciaColeta> listOcorrenciaColeta = getOcorrenciaEntregaByTpEventoEntregaExecutada();
        //CQPRO00023234 item 1 - Deve existir tipo coleta ex.
        validarSeExisteOcorrenciaColeta(listOcorrenciaColeta);

        StringBuilder excecao = new StringBuilder();
        for (Long id : ids) {
            PedidoColeta pedidoColeta = findById(id);
            if ("AU".equals(pedidoColeta.getTpModoPedidoColeta().getValue())) {
                if (pedidoColeta.getQtTotalVolumesVerificado() == null || pedidoColeta.getQtTotalVolumesVerificado().compareTo(Integer.valueOf(0)) == 0 ||
                        pedidoColeta.getPsTotalVerificado() == null || pedidoColeta.getPsTotalVerificado().compareTo(BigDecimalUtils.ZERO) == 0 ||
                        pedidoColeta.getVlTotalVerificado() == null || pedidoColeta.getVlTotalVerificado().compareTo(BigDecimalUtils.ZERO) == 0) {
                    excecao.append(FormatUtils.formatSgFilialWithLong(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial(), pedidoColeta.getNrColeta()));
                    excecao.append(", ");
                    continue;
                }
            }

            MeioTransporteRodoviario meioTransporteRodoviario = newMeioTransporteRodoviario(pedidoColeta);

            this.generateEventoPedidoColeta(pedidoColeta, dhEvento, listOcorrenciaColeta.get(0), meioTransporteRodoviario);
            this.generateEventosColetaAeroByPedidoColeta(pedidoColeta, PedidoColetaServiceConstants.CD_COLETA_REALIZADA_AEROPORTO, (Short[]) null);
            this.generateManifestoEntregaDireta(pedidoColeta);

            this.criarLocalizacaoParaMonitoramentoNetworkAereo(pedidoColeta);
        }
        if (excecao.length() > 0) {
            return excecao.toString().substring(0, excecao.length() - 2);
        }
        return "";

    }

	private void criarLocalizacaoParaMonitoramentoNetworkAereo(PedidoColeta pedidoColeta) {
            if (PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(pedidoColeta.getTpPedidoColeta().getValue())) {
                List<Awb> awbs = ctoAwbService.findByPedidoColeta(pedidoColeta.getIdPedidoColeta());
                for (Awb awb : awbs) {
                    generateLocalizacaoToMonitoramentoNetworkAereo(awb.getIdAwb());
                }
            }
        }

	private void validarSeExisteOcorrenciaColeta(List<OcorrenciaColeta> listOcorrenciaColeta) {
		if (listOcorrenciaColeta.isEmpty()) {
            throw new IllegalArgumentException("Não existe um evento coleta do tipo 'EX'");
    }
	}

    public String generateExecutarColetasPendenteNovaUI(List<Long> ids, DateTime dhEvento) {
        List<OcorrenciaColeta> listOcorrenciaColeta = getOcorrenciaEntregaByTpEventoEntregaExecutada();

        //CQPRO00023234 item 1 - Deve existir tipo coleta ex.
        if (listOcorrenciaColeta.isEmpty()) {
            //A mensagem abaixo não existe, deve retornar: Não existe um evento coleta do tipo 'EX'
            throw new BusinessException("LMS-02130");
        }

        StringBuilder excecao = new StringBuilder();
        for (Long id : ids) {
            PedidoColeta pedidoColeta = findById(id);
            if ("AU".equals(pedidoColeta.getTpModoPedidoColeta().getValue()) &&
                    (verificaQtTotalVolumesPedidoColeta(pedidoColeta) || verificaPsTotalPedidoColeta(pedidoColeta) || verificaVlTotalPedidoColeta(pedidoColeta))) {
                concatenaFilialNrColeta(excecao, pedidoColeta);
                continue;
            }

            MeioTransporteRodoviario meioTransporteRodoviario = newMeioTransporteRodoviario(pedidoColeta);

            this.generateEventoPedidoColeta(pedidoColeta, dhEvento, listOcorrenciaColeta.get(0), meioTransporteRodoviario);
            this.generateEventosColetaAeroByPedidoColeta(pedidoColeta, PedidoColetaServiceConstants.CD_COLETA_REALIZADA_AEROPORTO, (Short[]) null);
            this.generateManifestoEntregaDireta(pedidoColeta);

            if (PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(pedidoColeta.getTpPedidoColeta().getValue())) {
                generateLocalizacaoToMonitoramentoNetWorkAereoPedidoColeta(pedidoColeta);
            }
        }
        if (excecao.length() > 0) {
            return excecao.toString().substring(0, excecao.length() - 2);
        }
        return "";

    }

    private void generateLocalizacaoToMonitoramentoNetWorkAereoPedidoColeta(PedidoColeta pedidoColeta) {
        List<Awb> awbs = ctoAwbService.findByPedidoColeta(pedidoColeta.getIdPedidoColeta());
        for (Awb awb : awbs) {
            generateLocalizacaoToMonitoramentoNetworkAereo(awb.getIdAwb());
        }
    }

    private void concatenaFilialNrColeta(StringBuilder excecao, PedidoColeta pedidoColeta) {
        String sgFilial = pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial();
        Long nrColeta = pedidoColeta.getNrColeta();
        excecao.append(sgFilial + " " + nrColeta + ", ");
    }

    private boolean verificaVlTotalPedidoColeta(PedidoColeta pedidoColeta) {
        return pedidoColeta.getPsTotalVerificado() == null || pedidoColeta.getPsTotalVerificado().compareTo(BigDecimalUtils.ZERO) == 0;
    }

    private boolean verificaPsTotalPedidoColeta(PedidoColeta pedidoColeta) {
        return pedidoColeta.getPsTotalVerificado() == null || pedidoColeta.getPsTotalVerificado().compareTo(BigDecimalUtils.ZERO) == 0;
    }

    private boolean verificaQtTotalVolumesPedidoColeta(PedidoColeta pedidoColeta) {
        return pedidoColeta.getQtTotalVolumesVerificado() == null || pedidoColeta.getQtTotalVolumesVerificado().compareTo(Integer.valueOf(0)) == 0;
    }

    private List<OcorrenciaColeta> getOcorrenciaEntregaByTpEventoEntregaExecutada() {
        Map<String, Object> mapOcorrenciaColeta = new HashMap<String, Object>();
        mapOcorrenciaColeta.put("tpEventoColeta", PedidoColetaServiceConstants.EVENTO_COLETA_EXECUCAO);
        return ocorrenciaColetaService.find(mapOcorrenciaColeta);
    }

    private MeioTransporteRodoviario newMeioTransporteRodoviario(PedidoColeta pedidoColeta) {
        MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
        meioTransporteRodoviario.setIdMeioTransporte(
                pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getIdMeioTransporte());
        return meioTransporteRodoviario;
    }

    public void generateEventoPedidoColeta(PedidoColeta pedidoColeta, DateTime dhEvento,
                                           OcorrenciaColeta ocorrenciaColeta, MeioTransporteRodoviario meioTransporteRodoviario) {
        DateTime dataHoraAtual;
        if (dhEvento != null) {
            dataHoraAtual = dhEvento;
        } else {
            dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        }

        // LMS-6958 - Caso já exista um evento de coleta do tipo EX,
        // não permite inserir outro do mesmo tipo
        List listEventoColeta = eventoColetaService.findEventoColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta(), PedidoColetaServiceConstants.TP_EVENTO_COLETA_EXECUCAO);
        if (CollectionUtils.isEmpty(listEventoColeta)) {
            EventoColeta eventoColeta = new EventoColeta();
            eventoColeta.setPedidoColeta(pedidoColeta);
            eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
            eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
            eventoColeta.setDhEvento(dataHoraAtual);
            eventoColeta.setTpEventoColeta(new DomainValue(PedidoColetaServiceConstants.TP_EVENTO_COLETA_EXECUCAO));
            eventoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
            eventoColetaService.store(eventoColeta);
            // grava no topic de eventos de pedido coleta
            eventoColetaService.storeMessageTopic(eventoColeta);
        }

        pedidoColeta.setTpStatusColeta(new DomainValue(PedidoColetaServiceConstants.TP_STATUS_COLETA_EXECUTADA));
        store(pedidoColeta);

        Long idControleCarga = pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga();
        controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(idControleCarga, Boolean.FALSE, SessionUtils.getFilialSessao());

        Map<String, Object> mapEficienciaVeiculoColeta = new HashMap<String, Object>();
        Map<String, Object> mapMeioTransporteRodoviario = new HashMap<String, Object>();
        mapMeioTransporteRodoviario.put("idMeioTransporte", meioTransporteRodoviario.getIdMeioTransporte());
        mapEficienciaVeiculoColeta.put("meioTransporteRodoviario", mapMeioTransporteRodoviario);
        mapEficienciaVeiculoColeta.put("dtMovimentacao", dataHoraAtual.toYearMonthDay());
        List<EficienciaVeiculoColeta> listEficienciaVeiculoColeta = eficienciaVeiculoColetaService.find(mapEficienciaVeiculoColeta);
        if (listEficienciaVeiculoColeta.isEmpty()) {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = inicializaEficienciaVeiculoColeta();
            eficienciaVeiculoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
            eficienciaVeiculoColeta.setDtMovimentacao(dataHoraAtual.toYearMonthDay());
            eficienciaVeiculoColeta.setQtExecutadas(IntegerUtils.ONE);
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        } else {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = listEficienciaVeiculoColeta.get(0);
            eficienciaVeiculoColeta.setQtExecutadas(Integer.valueOf(eficienciaVeiculoColeta.getQtExecutadas().intValue() + 1));
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        }

    }

    public void generateEventosColetaAeroByPedidoColeta(PedidoColeta pedidoColeta, Short cdEvento, Short[] cdLocalizacaoAtual) {
        String nrDocumento = getNrDocumento(pedidoColeta);
        generateEventosColetaAeroByPedidoColeta(pedidoColeta, cdEvento, cdLocalizacaoAtual, null, nrDocumento, PedidoColetaServiceConstants.COLETA);
    }

    public void generateEventosColetaAeroByPedidoColeta(PedidoColeta pedidoColeta, Short cdEventoNovo, Short cdLocalizacaoAtual, String nrDocumento) {
        generateEventosColetaAeroByPedidoColeta(pedidoColeta, cdEventoNovo, new Short[]{cdLocalizacaoAtual}, null, nrDocumento, PedidoColetaServiceConstants.COLETA);
    }

    public void generateEventosColetaAeroByPedidoColeta(PedidoColeta pedidoColeta, Short cdEvento, Short[] cdLocalizacaoAtual, Long idFilial) {
        String nrDocumento = getNrDocumento(pedidoColeta);
        this.generateEventosColetaAeroByPedidoColeta(pedidoColeta, cdEvento, cdLocalizacaoAtual, idFilial, nrDocumento, PedidoColetaServiceConstants.COLETA);
    }

    public void generateEventosColetaAeroByPedidoColeta(PedidoColeta pedidoColeta, Short cdEventoNovo, Short cdLocalizacaoAtual) {
        String nrDocumento = getNrDocumento(pedidoColeta);
        generateEventosColetaAeroByPedidoColeta(pedidoColeta, cdEventoNovo, new Short[]{cdLocalizacaoAtual}, null, nrDocumento, PedidoColetaServiceConstants.COLETA);
    }

    public void generateEventosColetaAeroByPedidoColetaWithLazyLoading(PedidoColeta pedidoColeta, Short cdEventoNovo, Short cdLocalizacaoAtual) {
        String nrDocumento = getNrDocumento(pedidoColeta);
        generateEventosColetaAeroByPedidoColetaWithLazyLoading(pedidoColeta, cdEventoNovo, new Short[]{cdLocalizacaoAtual}, null, nrDocumento, PedidoColetaServiceConstants.COLETA);
    }

    public void generateEventosColetaAeroByPedidoColetaWithLazyLoading(PedidoColeta pedidoColeta, Short cdEvento, Short[] cdLocalizacaoAtual, Long idFilial, String nrDocumento, String tpDocumento) {
        if (pedidoColeta.getTpPedidoColeta() != null && PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(pedidoColeta.getTpPedidoColeta().getValue())) {
            List<Conhecimento> ctos = this.getCtosByPedidoColetaWithLazyLoading(pedidoColeta);
            for (Conhecimento conhecimento : ctos) {
                this.generateEventoDoctoVolume(conhecimento, nrDocumento, tpDocumento, cdEvento, cdLocalizacaoAtual, idFilial, null, SessionUtils.getUsuarioLogado(), JTDateTimeUtils.getDataHoraAtual());
            }
        }
    }

    public void generateEventosColetaAeroByPedidoColeta(PedidoColeta pedidoColeta, Short cdEvento, Short[] cdLocalizacaoAtual, Long idFilial, String nrDocumento, String tpDocumento) {
        if (pedidoColeta.getTpPedidoColeta() != null && PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(pedidoColeta.getTpPedidoColeta().getValue())) {
            List<Conhecimento> ctos = this.getCtosByPedidoColeta(pedidoColeta);
            for (Conhecimento conhecimento : ctos) {
                this.generateEventoDoctoVolume(conhecimento, nrDocumento, tpDocumento, cdEvento, cdLocalizacaoAtual, idFilial, null, SessionUtils.getUsuarioLogado(), JTDateTimeUtils.getDataHoraAtual());
            }
        }
    }

    private String getNrDocumento(PedidoColeta pedidoColeta) {
        String nrColetaConcatenado = FormatUtils.fillNumberWithZero(pedidoColeta.getNrColeta().toString(), 8);
        String nrDocumento = new StringBuilder(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial()).append(" ").append(nrColetaConcatenado).toString();
        return nrDocumento;
    }

    public void generateEventosColetaAereoByPedidoColetaAndAwb(List<Map<String, Long>> param) {

        List<Map<String, Object>> dtColhetaCCAwb = new ArrayList<Map<String, Object>>();

        for (Map<String, Long> map : param) {
            Long idAwb = map.get(ID_AWB);
            Long idPedidoColeta = map.get(ID_PEDIDO_COLETA);
            Short cdEvento = PedidoColetaServiceConstants.CD_COLETA_REALIZADA_AEROPORTO;
            generateEventoColetaAereoByIdPedidoAndIdAwb(idAwb, idPedidoColeta, cdEvento);

            Integer doctosNaoExecutados = getRowCountDocumentosNaoExecutadosByPedidoColeta(idPedidoColeta);
            PedidoColeta pedidoColeta = this.findById(idPedidoColeta);
            if (IntegerUtils.isZero(doctosNaoExecutados)) {
                MeioTransporteRodoviario mtr = newMeioTransporteRodoviario(pedidoColeta);
                List<OcorrenciaColeta> list = getOcorrenciaEntregaByTpEventoEntregaExecutada();
                generateEventoPedidoColeta(pedidoColeta, null, list.get(IntegerUtils.ZERO), mtr);
            }

            ControleCarga cc = controleCargaService.findControleCargaByPedidoColeta(idPedidoColeta);

            boolean exists = false;
            for (Map<String, Object> mapCCAwb : dtColhetaCCAwb) {
                if (cc.getIdControleCarga().equals(((ControleCarga) mapCCAwb.get(CONTROLE_CARGA)).getIdControleCarga())) {
                    exists = true;
                    this.updateDoctosEntregaDireta(idPedidoColeta, idAwb, (List<DoctoServico>) mapCCAwb.get("doctos"));
                }
            }
            if (!exists) {
                dtColhetaCCAwb.add(this.generateMapDetalhesEntregaDireta(cc, idPedidoColeta, idAwb));
            }

            List<EventoControleCarga> chegadaPortaria = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(SessionUtils.getFilialSessao().getIdFilial(), cc.getIdControleCarga(), "CP");
            if (CollectionUtils.isNotEmpty(chegadaPortaria)) {
                this.generateEventosColetaAeroByIdControleCarga(cc.getIdControleCarga(),
                        ConstantesSim.EVENTO_CHEGADA_PORTARIA_AD, ConstantesSim.CD_MERCADORIA_COLETA_REALIZADA_NO_AEROPORTO);
            }

            //LMS-3381
            generateLocalizacaoToMonitoramentoNetworkAereo(idAwb);

        }

        this.generateManifestoEntregaDireta(dtColhetaCCAwb);
    }

    private void generateManifestoEntregaDireta(List<Map<String, Object>> dtColhetaCCAwb) {
        if (!dtColhetaCCAwb.isEmpty()) {
            DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
            for (Map<String, Object> map : dtColhetaCCAwb) {
                boolean updateMDF = false;
                List<DoctoServico> doctos = (List<DoctoServico>) map.get("doctos");
                ControleCarga cc = (ControleCarga) map.get(CONTROLE_CARGA);
                for (DoctoServico doctoServico : doctos) {
                    this.generatePreManifestos(cc, doctoServico, dataHoraAtual);
                    updateMDF = true;
                }
                if (updateMDF) {
                    controleCargaService.executeEncerrarMdfesAutorizados(cc.getIdControleCarga());
                    controleCargaService.generateMdfe(cc.getIdControleCarga(), true);
                }
            }
        }
    }

    private Map<String, Object> generateMapDetalhesEntregaDireta(ControleCarga controleCarga, Long idPedidoColeta, Long idAwb) {
        Map<String, Object> mapCC = new TypedFlatMap();
        mapCC.put(CONTROLE_CARGA, controleCarga);
        List<DoctoServico> doctosEntregaDireta = new ArrayList<DoctoServico>();
        this.updateDoctosEntregaDireta(idPedidoColeta, idAwb, doctosEntregaDireta);
        mapCC.put("doctos", doctosEntregaDireta);
        return mapCC;
    }

    private void updateDoctosEntregaDireta(Long idPedidoColeta, Long idAwb, List<DoctoServico> doctosEntregaDireta) {
        List detalhes = detalheColetaService.findDetalheColetaByIdPedidoColeta(idPedidoColeta);
        for (DetalheColeta detalhe : (List<DetalheColeta>) detalhes) {
            if (BooleanUtils.isTrue(detalhe.getBlEntregaDireta())) {
                DoctoServico docto = doctoServicoService.findById(detalhe.getDoctoServico().getIdDoctoServico());
                LocalizacaoMercadoria lm = localizacaoMercadoriaService.findById(docto.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria());
                if (ConstantesSim.CD_MERCADORIA_COLETA_REALIZADA_NO_AEROPORTO.equals(lm.getCdLocalizacaoMercadoria()) && idAwb != null) {
                    Awb awb = awbService.findUltimoAwbByDoctoServico(docto.getIdDoctoServico());
                    if (awb != null && awb.getIdAwb().equals(idAwb)) {
                        doctosEntregaDireta.add(docto);
                    }
                }
            }
        }
    }

    public void generateLocalizacaoToMonitoramentoNetworkAereo(Long idAwb) {
        Boolean hasEvento17 = true;
        Awb awb = awbService.findById(idAwb);
        List<CtoAwb> ctos = awb.getCtoAwbs();
        for (CtoAwb ctoAwb : ctos) {
            hasEvento17 = validateEventoColetaRealizadaAeroportoByIdDoctoServico(ctoAwb.getConhecimento().getIdDoctoServico());
            if (BooleanUtils.isFalse(hasEvento17)) {
                break;
            }
        }

        awb.setIdAwb(idAwb);

        if (hasEvento17) {
            awb.setTpLocalizacao(new DomainValue(PedidoColetaServiceConstants.TP_LOCALIZACAO_AWB_RETIRADO_AEROPORTO));
        } else {
            awb.setTpLocalizacao(new DomainValue(PedidoColetaServiceConstants.TP_LOCALIZACAO_AWB_RETIRADA_PARCIAL_AEROPORTO));
        }

        awbService.store(awb);

        if (awbOcorrenciaService.findBooleanAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(awb.getIdAwb(), awb.getTpLocalizacao().getValue())) {
            awbOcorrenciaService.store(awb, awb.getTpLocalizacao());
            trackingAwbService.storeTrackingAwb(awb, awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa(), awb.getTpLocalizacao().getValue());
        }

    }

    private Boolean validateEventoColetaRealizadaAeroportoByIdDoctoServico(Long idDoctoServico) {
        List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findEventoDoctoServico(idDoctoServico, PedidoColetaServiceConstants.CD_COLETA_REALIZADA_AEROPORTO);
        return !eventos.isEmpty();
    }

    private void generateEventoColetaAereoByIdPedidoAndIdAwb(Long idAwb, Long idPedidoColeta, Short cdEvento) {
        List<CtoAwb> l = ctoAwbService.findByAwbAndPedidoColeta(idAwb, idPedidoColeta);

        for (CtoAwb ctoAwb : l) {
            Conhecimento conhecimento = ctoAwb.getConhecimento();
            this.generateEventoDoctoVolume(conhecimento, cdEvento, (Short[]) null);
        }
    }

    private Integer getRowCountDocumentosNaoExecutadosByPedidoColeta(Long idPedidoColeta) {
        return IntegerUtils.getInteger(getPedidoColetaDAO().getRowCountDocumentosNaoExecutadosByPedidoColeta(idPedidoColeta));
    }

    private Integer getRowCountDocumentosRetornados(Long idPedidoColeta) {
        return IntegerUtils.getInteger(getPedidoColetaDAO().getRowCountDocumentosRetornados(idPedidoColeta));
    }

    public List<Conhecimento> getCtosByPedidoColetaWithLazyLoading(PedidoColeta pedidoColeta) {
        List<Conhecimento> ctos = new ArrayList<Conhecimento>();

        for (DetalheColeta detalheColeta : (List<DetalheColeta>) pedidoColeta.getDetalheColetas()) {

            getDao().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().update(detalheColeta);
            getDao().getAdsmHibernateTemplate().initialize(detalheColeta.getAwbColetas());

            getCtos(ctos, detalheColeta);
        }

        return ctos;
    }

    public List<Conhecimento> getCtosByPedidoColeta(PedidoColeta pedidoColeta) {
        List<Conhecimento> ctos = new ArrayList<Conhecimento>();

        for (DetalheColeta detalheColeta : (List<DetalheColeta>) pedidoColeta.getDetalheColetas()) {
            getCtos(ctos, detalheColeta);
        }

        return ctos;
    }

    private void getCtos(List<Conhecimento> ctos, DetalheColeta detalheColeta) {
        if (CollectionUtils.isNotEmpty(detalheColeta.getAwbColetas())
                && detalheColeta.getAwbColetas().get(0) != null
                && ((AwbColeta) detalheColeta.getAwbColetas().get(0)).getAwb() != null
                && ((AwbColeta) detalheColeta.getAwbColetas().get(0)).getAwb().getIdAwb() != null) {

            Long idAwb = ((AwbColeta) detalheColeta.getAwbColetas().get(0)).getAwb().getIdAwb();
            List<CtoAwb> ctoAwbs = ctoAwbService.findByIdAwb(idAwb);
            for (CtoAwb ca : ctoAwbs) {
                ctos.add(ca.getConhecimento());
            }

        } else if (detalheColeta.getDoctoServico() != null
                && detalheColeta.getDoctoServico().getIdDoctoServico() != null) {
            ctos.add(conhecimentoService.findById(detalheColeta.getDoctoServico().getIdDoctoServico()));
        }
    }


    /**
     * @param ids
     */
    public String generateExecutarColetasPendentes(List<Long> ids) {
        return this.generateExecutarColetasPendentes(ids, null);
    }

    private EficienciaVeiculoColeta inicializaEficienciaVeiculoColeta() {
        EficienciaVeiculoColeta eficienciaVeiculoColeta = new EficienciaVeiculoColeta();
        eficienciaVeiculoColeta.setQtSolicitadas(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtManifestadas(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtTransmitidas(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtExecutadas(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtCanceladas(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtRetornaram(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtProgramadasMaisUmaVez(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtProgramadasNaData(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtRetornaramPorIneficiencia(Integer.valueOf(0));
        eficienciaVeiculoColeta.setQtTotalProgramacoes(Integer.valueOf(0));
        return eficienciaVeiculoColeta;
    }

    /**
     * @param consultarColetaDTO
     * @return Integer
     */
    public Integer getRowCountConsultarColeta(ConsultarColetaDTO consultarColetaDTO) {
        return this.getPedidoColetaDAO().getRowCountConsultarColeta(consultarColetaDTO);
    }

    /**
     * @param consultaColetaDTO
     * @param findDefinition
     * @return ResultSetPage
     */
    @SuppressWarnings("rawtypes")
    public ResultSetPage findPaginatedConsultarColeta(ConsultarColetaDTO consultaColetaDTO, FindDefinition findDefinition) {
        return this.getPedidoColetaDAO().findPaginatedConsultarColeta(consultaColetaDTO, findDefinition);
    }

    /**
     * Busca os pedidos de coleta Milk Run para o cliente e na data informada.
     *
     * @param dataProcessoColeta
     * @param idCliente
     * @return
     */
    public List<PedidoColeta> findPedidoColetaMilkRun(Long idCliente, YearMonthDay dataProcessoColeta, DateTimeZone dateTimeZone) {
        return getPedidoColetaDAO().findPedidoColetaMilkRun(idCliente, dataProcessoColeta, dateTimeZone);
    }

    /**
     * Busca os pedidos de coleta automática para o cliente e na data informada.
     *
     * @param dataProcessoColeta
     * @param idCliente
     * @return
     */
    public List<PedidoColeta> findPedidoColetaAutomatica(Long idCliente, YearMonthDay dataProcessoColeta, DateTimeZone dateTimeZone) {
        return getPedidoColetaDAO().findPedidoColetaAutomatica(idCliente, dataProcessoColeta, dateTimeZone);
    }

    /**
     * Método que busca os clientes que possuem coleta, baseando a busca por
     * um cliente especifico ou por rotas.
     *
     * @author Rodrigo Antunes
     */
    public ResultSetPage findPaginatedClientesColeta(Long idFilial, Long idCliente, YearMonthDay dataInicial, YearMonthDay dataFinal, Long idRotaColetaEntrega, Long idRegiaoColetaEntregaFil, Map map) {
        List<Long> rotas = new ArrayList<Long>();
        if (idRegiaoColetaEntregaFil != null) {
            rotas = findRotasByRegiaoColetaEntrega(idRegiaoColetaEntregaFil);
        }
        return getPedidoColetaDAO().findPaginatedClientesColeta(idFilial, idCliente, dataInicial, dataFinal, idRotaColetaEntrega, rotas, FindDefinition.createFindDefinition(map));
    }

    /**
     * Rowcount para o método findPaginatedClientesColeta()
     *
     * @author Rodrigo Antunes
     */
    public Integer getRowCountClientesColeta(Long idFilial, Long idCliente, YearMonthDay dataInicial, YearMonthDay dataFinal, Long idRotaColetaEntrega, Long idRegiaoColetaEntregaFil) {
        List<Long> rotas = new ArrayList<Long>();
        if (idRegiaoColetaEntregaFil != null) {
            rotas = findRotasByRegiaoColetaEntrega(idRegiaoColetaEntregaFil);
        }
        return getPedidoColetaDAO().getRowCountClientesColeta(idFilial, idCliente, dataInicial, dataFinal, idRotaColetaEntrega, rotas);
    }

    /**
     * Método para buscar as rotas baseado na regiaoColetaEntrega
     *
     * @param idRegiaoColetaEntregaFil
     * @return
     */
    private List<Long> findRotasByRegiaoColetaEntrega(Long idRegiaoColetaEntregaFil) {
        List<Long> rotas = new ArrayList<Long>();
        rotas.add(Long.valueOf(-1));

        RegiaoColetaEntregaFil regiaoColetaEntregaFil = regiaoColetaEntregaFilService.findById(idRegiaoColetaEntregaFil);
        List<RegiaoFilialRotaColEnt> regiaoFilialRota = regiaoColetaEntregaFil.getRegiaoFilialRotaColEnts();
        for (RegiaoFilialRotaColEnt regiaoFilialRotaColEnt : regiaoFilialRota) {
            rotas.add(regiaoFilialRotaColEnt.getRotaColetaEntrega().getIdRotaColetaEntrega());
        }
        return rotas;
    }

    /**
     * Busca pedidos de coletas do cliente informado.
     *
     * @param idCliente
     * @param dtInicial
     * @param dtFinal
     * @param statusColeta
     * @param tpPedidoColeta
     * @param fd
     * @author Rodrigo Antunes
     */
    public ResultSetPage findPaginatedPedidosColetaByCliente(Long idCliente, YearMonthDay dtInicial, YearMonthDay dtFinal, List statusColeta, String tpPedidoColeta, FindDefinition fd) {
        return getPedidoColetaDAO().findPaginatedPedidosColetaByCliente(idCliente, dtInicial, dtFinal, statusColeta, tpPedidoColeta, fd);
    }

    /**
     * Row count para findPaginatedPedidosColetaByCliente()
     *
     * @return total de registros
     * @author Rodrigo Antunes
     */
    public Integer getRowCountPedidosColetaByCliente(
            Long idCliente,
            YearMonthDay dtInicial,
            YearMonthDay dtFinal,
            List statusColeta,
            String tpPedidoColeta
    ) {
        return getPedidoColetaDAO().getRowCountPedidosColetaByCliente(
                idCliente,
                dtInicial,
                dtFinal,
                statusColeta,
                tpPedidoColeta
        );
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     *
     * @param idControleCarga
     * @return
     */
    public List findPaginatedByColetasVeiculosRealizadas(Long idControleCarga) {
        return getPedidoColetaDAO().findPaginatedByColetasVeiculosRealizadas(idControleCarga);
    }

    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados
     * parametros.
     *
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountByColetasVeiculosRealizadas(Long idControleCarga) {
        Integer rowCount = getPedidoColetaDAO().getRowCountByColetasVeiculosRealizadas(idControleCarga);
        return rowCount;
    }

    public Integer getRowCountByIdBlClienteLiberadoManual(Long idPedidoColeta, Boolean blClienteLiberadoManual) {
        return getPedidoColetaDAO().getRowCountByIdBlClienteLiberadoManual(idPedidoColeta, blClienteLiberadoManual);
    }

    public void generateRetornarColetaAerea(TypedFlatMap criteria) {
        List<Map<String, Long>> l = criteria.getList("idsPedidoColetaAndAwb");
        for (Map<String, Long> m : l) {
            TypedFlatMap typedFlatMap = new TypedFlatMap(m);
            final Long idAwb = typedFlatMap.getLong(ID_AWB);
            Long idPedidoColeta = typedFlatMap.getLong(ID_PEDIDO_COLETA);
            Short cdEvento = criteria.getShort("cdEventoColetaAeroportoNaoExecutada");

            generateEventoColetaAereoByIdPedidoAndIdAwb(idAwb, idPedidoColeta, cdEvento);

            List<DetalheColeta> detalhes = (List<DetalheColeta>) detalheColetaService.findDetalheColetaByIdPedidoColeta(idPedidoColeta);

            Integer doctosRetornados = this.getRowCountDocumentosRetornados(idPedidoColeta);

			/* Se retornou TODOS os documentos do Pedido, cancela o Pedido */
            if (doctosRetornados == detalhes.size()) {
                this.generateRetornarColeta(this.setIdPedidoOnCriteria(criteria, idPedidoColeta));
                List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColetaCancelada(null);
                String dsCancelamento = configuracoesFacade.getMensagem("LMS-04530");
                executeCancelarColeta(idPedidoColeta, listOcorrenciaColeta.get(0).getIdOcorrenciaColeta(), dsCancelamento, true);
            } else {
				/* Senão remove os Detalhes do pedido e recalcula seus totais */
                CollectionUtils.filter(detalhes, new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        DetalheColeta detalhe = (DetalheColeta) arg0;
                        Awb awb = ctoAwbService.findAwbByIdCto(detalhe.getDoctoServico().getIdDoctoServico(), null);
                        return awb != null && awb.getIdAwb().equals(idAwb);
                    }
                });

                for (DetalheColeta detalheColeta : detalhes) {
                    detalheColetaService.removeById(detalheColeta.getIdDetalheColeta());
                }

                PedidoColeta pedidoColeta = this.findById(idPedidoColeta);
                this.recalcularTotaisRemove(pedidoColeta, detalhes);
                this.store(pedidoColeta);
            }
        }
    }

    private void recalcularTotaisRemove(PedidoColeta bean, List<DetalheColeta> detalhes) {
        Integer totalVols = bean.getQtTotalVolumesVerificado();
        BigDecimal psMercadoria = bean.getPsTotalInformado();
        BigDecimal psAforado = bean.getPsTotalAforadoInformado();
        BigDecimal vlMercadoria = bean.getVlTotalInformado();
        for (DetalheColeta detalheColeta : detalhes) {
            if (detalheColeta.getDoctoServico() != null) {
                totalVols -= detalheColeta.getQtVolumes();
                psMercadoria = psMercadoria.subtract(detalheColeta.getPsMercadoria());
                psAforado = psAforado.subtract(detalheColeta.getPsAforado());

                vlMercadoria = vlMercadoria.subtract(detalheColeta.getVlMercadoria());
            }
        }
        bean.setQtTotalVolumesInformado(totalVols);
        bean.setQtTotalVolumesVerificado(totalVols);

        bean.setPsTotalInformado(psMercadoria);
        bean.setPsTotalVerificado(psMercadoria);
        bean.setPsTotalAforadoInformado(psAforado);
        bean.setPsTotalAforadoVerificado(psAforado);

        bean.setVlTotalInformado(vlMercadoria);
        bean.setVlTotalVerificado(vlMercadoria);
    }

    private TypedFlatMap setIdPedidoOnCriteria(TypedFlatMap criteria, Long idPedidoColeta) {
        List<String> ids = (List<String>) criteria.getList("idsPedidoColeta.ids");
        ids.clear();
        ids.add(idPedidoColeta.toString());
        criteria.put("idsPedidoColeta.ids", ids);
        return criteria;
    }

    /**
     * @param criteria
     */
    public void generateRetornarColeta(TypedFlatMap criteria) {
        Usuario usuario = SessionUtils.getUsuarioLogado();
        List<String> ids = (List<String>) criteria.getList("idsPedidoColeta.ids");
        DateTime dhEvento = null;

        if (criteria.getString("dhEvento") != null) {
            dhEvento = VolFomatterUtil.formatStringToDateTime(criteria.getString("dhEvento"));
        } else if (criteria.getString("dtHoraOcorrencia") != null) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            dhEvento = new DateTime(format.parseDateTime(criteria.get("dtHoraOcorrencia").toString()));
        }

        MeioTransporteRodoviario meioTransporteRodoviarioNovo = null;
        if (criteria.getLong("meioTransporte.idMeioTransporte") != null) {
            meioTransporteRodoviarioNovo = new MeioTransporteRodoviario();
            meioTransporteRodoviarioNovo.setIdMeioTransporte(criteria.getLong("meioTransporte.idMeioTransporte"));
        }

        OcorrenciaColeta ocorrenciaColeta = new OcorrenciaColeta();
        ocorrenciaColeta.setIdOcorrenciaColeta(criteria.getLong("ocorrenciaColeta.idOcorrenciaColeta"));

        boolean blIneficienciaFrota = "S".equals(criteria.getString("ocorrenciaColeta.blIneficienciaFrota"));

        for (String id : ids) {
            PedidoColeta pedidoColeta = findById(Long.valueOf(id));

            MeioTransporteRodoviario meioTransporteRodoviarioAnterior = newMeioTransporteRodoviario(pedidoColeta);

            ControleCarga controleCargaAnterior = pedidoColeta.getManifestoColeta().getControleCarga();

            if (meioTransporteRodoviarioNovo == null) {
                if (dhEvento != null) {
                    gravarEventoColeta(usuario, pedidoColeta, meioTransporteRodoviarioAnterior,
                            ocorrenciaColeta, PedidoColetaServiceConstants.TP_STATUS_COLETA_RETORNO, (String) criteria.get("dsDescricao"), null, dhEvento);

                } else {
                    gravarEventoColeta(usuario, pedidoColeta, meioTransporteRodoviarioAnterior,
                            ocorrenciaColeta, PedidoColetaServiceConstants.TP_STATUS_COLETA_RETORNO, (String) criteria.get("dsDescricao"), null);
                }

                pedidoColeta.setTpStatusColeta(new DomainValue(PedidoColetaServiceConstants.TP_STATUS_COLETA_ABERTA));
                pedidoColeta.setManifestoColeta(null);
                store(pedidoColeta);

                gravarEficienciaVeiculoColetaByRetornoColeta(blIneficienciaFrota, meioTransporteRodoviarioAnterior);
                controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(controleCargaAnterior.getIdControleCarga(), Boolean.FALSE, SessionUtils.getFilialSessao());
            } else {
                Map<String, Object> mapValidaVeiculo = validaVeiculo(null, meioTransporteRodoviarioNovo.getIdMeioTransporte());
                ManifestoColeta manifestoColeta = (ManifestoColeta) mapValidaVeiculo.get(MANIFESTO_COLETA);
                ControleCarga controleCargaNovo = (ControleCarga) mapValidaVeiculo.get(CONTROLE_CARGA);

                pedidoColeta.setTpStatusColeta(new DomainValue(PedidoColetaServiceConstants.TP_STATUS_COLETA_TRANSMITIDA));
                pedidoColeta.setManifestoColeta(manifestoColeta);
                store(pedidoColeta);

                // Primeiro
                if (dhEvento != null) {
                    gravarEventoColeta(usuario, pedidoColeta, meioTransporteRodoviarioAnterior,
                            ocorrenciaColeta, PedidoColetaServiceConstants.TP_STATUS_COLETA_RETORNO, (String) criteria.get("dsDescricao"), null, dhEvento);
                } else {
                    gravarEventoColeta(usuario, pedidoColeta,
                            meioTransporteRodoviarioAnterior, ocorrenciaColeta, PedidoColetaServiceConstants.TP_STATUS_COLETA_RETORNO, null, null);
                }

                controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(controleCargaAnterior.getIdControleCarga(), Boolean.FALSE, SessionUtils.getFilialSessao());
                gravarEficienciaVeiculoColetaByRetornoColeta(blIneficienciaFrota, meioTransporteRodoviarioAnterior);

                // Segundo
                Map<String, Object> mapOcorrencia = new HashMap<String, Object>();
                mapOcorrencia.put("tpEventoColeta", "TR");
                List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.find(mapOcorrencia);

                if (dhEvento != null) {
                    gravarEventoColeta(usuario, pedidoColeta, meioTransporteRodoviarioAnterior,
                            ocorrenciaColeta, PedidoColetaServiceConstants.TP_STATUS_COLETA_RETORNO, (String) criteria.get("dsDescricao"), null, dhEvento);
                } else {
                    gravarEventoColeta(
                            usuario,
                            pedidoColeta,
                            meioTransporteRodoviarioNovo,
                            listOcorrenciaColeta.get(0),
                            PedidoColetaServiceConstants.TP_STATUS_COLETA_TRANSMITIDA,
                            null,
                            null
                    );
                }

                controleCargaService.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(controleCargaNovo.getIdControleCarga(), Boolean.FALSE, SessionUtils.getFilialSessao());
                gravarEficienciaVeiculoColetaByRetornoColeta(meioTransporteRodoviarioNovo, pedidoColeta.getIdPedidoColeta());
            }

        }
    }

    /**
     * @param idControleCarga
     * @param idMeioTransporte
     * @return
     */
    private Map<String, Object> validaVeiculo(Long idControleCarga, Long idMeioTransporte) {
        List<ControleCarga> listaControleCarga = controleCargaService.findControleCargaByTransmitirColeta(idControleCarga, "C", idMeioTransporte);
        if (listaControleCarga.isEmpty()) {
            throw new BusinessException("LMS-02014");
        }
        ControleCarga controleCarga = listaControleCarga.get(0);

        Map<String, Object> mapManifestoColeta = new HashMap<String, Object>();
        Map<String, Object> mapControleCarga = new HashMap<String, Object>();
        mapControleCarga.put(ID_CONTROLE_CARGA, controleCarga.getIdControleCarga());
        mapManifestoColeta.put(CONTROLE_CARGA, mapControleCarga);
        mapManifestoColeta.put(TP_STATUS_MANIFESTO_COLETA, "EM");

        List<ManifestoColeta> listaManifestoColeta = manifestoColetaService.find(mapManifestoColeta);
        if (listaManifestoColeta.isEmpty()) {
            throw new BusinessException("LMS-02015");
        }
        ManifestoColeta manifestoColeta = listaManifestoColeta.get(0);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(MANIFESTO_COLETA, manifestoColeta);
        map.put(CONTROLE_CARGA, controleCarga);
        return map;
    }


    /**
     * Grava um registro na tabela EVENTO_COLETA.
     *
     * @param usuario
     * @param pedidoColeta
     * @param meioTransporteRodoviario
     * @param ocorrenciaColeta
     * @param tipoEvento
     * @param detalheColeta
     */
    private void gravarEventoColeta(
            Usuario usuario,
            PedidoColeta pedidoColeta,
            MeioTransporteRodoviario meioTransporteRodoviario,
            OcorrenciaColeta ocorrenciaColeta,
            String tipoEvento,
            String descricao,
            DetalheColeta detalheColeta,
            DateTime dhEvento
    ) {
        // LMS-6958 - Caso já exista um evento de coleta do tipo EX,
        // não permite inserir outro do mesmo tipo
        if (!(PedidoColetaServiceConstants.TP_EVENTO_COLETA_EXECUCAO.equals(tipoEvento) &&
                !eventoColetaService.findEventoColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta(), tipoEvento).isEmpty())) {

            EventoColeta eventoColeta = new EventoColeta();
            eventoColeta.setUsuario(usuario);
            eventoColeta.setDetalheColeta(detalheColeta);

            if (dhEvento != null) {
                eventoColeta.setDhEvento(dhEvento);
            } else {
                eventoColeta.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
            }

            eventoColeta.setDsDescricao(descricao);
            eventoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
            eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
            eventoColeta.setPedidoColeta(pedidoColeta);
            eventoColeta.setTpEventoColeta(new DomainValue(tipoEvento));
            eventoColetaService.store(eventoColeta);
            // grava no topic de eventos de pedido coleta
            eventoColetaService.storeMessageTopic(eventoColeta);
        }
    }


    /**
     * Grava um registro na tabela EVENTO_COLETA.
     *
     * @param usuario
     * @param pedidoColeta
     * @param meioTransporteRodoviario
     * @param ocorrenciaColeta
     * @param tipoEvento
     * @param detalheColeta
     */
    private void gravarEventoColeta(
            Usuario usuario,
            PedidoColeta pedidoColeta,
            MeioTransporteRodoviario meioTransporteRodoviario,
            OcorrenciaColeta ocorrenciaColeta,
            String tipoEvento,
            String descricao,
            DetalheColeta detalheColeta
    ) {
        this.gravarEventoColeta(usuario, pedidoColeta, meioTransporteRodoviario, ocorrenciaColeta, tipoEvento, descricao, detalheColeta, null);
    }

    private void gravarEventoColetaLiberacao(PedidoColeta novoPedidoColeta, EventoColeta eventoColeta) {
        this.gravarEventoColeta(eventoColeta.getUsuario(), novoPedidoColeta, null, eventoColeta.getOcorrenciaColeta(), PedidoColetaServiceConstants.EVENTO_COLETA_LIBERACAO, eventoColeta.getDsDescricao(), null);
    }

    /**
     * Grava / altera registro na tabela EFICIENCIA_VEICULO_COLETA.
     *
     * @param blIneficienciaFrota
     * @param meioTransporteRodoviarioAnterior
     */
    private void gravarEficienciaVeiculoColetaByRetornoColeta(boolean blIneficienciaFrota, MeioTransporteRodoviario meioTransporteRodoviarioAnterior) {
        YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
        Map<String, Object> mapEficienciaVeiculoColeta = new HashMap<String, Object>();
        Map<String, Object> meioTransporteRodoviario = new HashMap<String, Object>();
        meioTransporteRodoviario.put("idMeioTransporte", meioTransporteRodoviarioAnterior.getIdMeioTransporte());
        mapEficienciaVeiculoColeta.put("meioTransporteRodoviario", meioTransporteRodoviario);
        mapEficienciaVeiculoColeta.put("dtMovimentacao", dataAtual);
        List<EficienciaVeiculoColeta> listEficienciaVeiculoColeta = eficienciaVeiculoColetaService.find(mapEficienciaVeiculoColeta);
        if (listEficienciaVeiculoColeta.isEmpty()) {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = inicializaEficienciaVeiculoColeta();
            eficienciaVeiculoColeta.setMeioTransporteRodoviario(meioTransporteRodoviarioAnterior);
            eficienciaVeiculoColeta.setDtMovimentacao(dataAtual);
            eficienciaVeiculoColeta.setQtRetornaram(Integer.valueOf(1));
            if (blIneficienciaFrota) {
                eficienciaVeiculoColeta.setQtRetornaramPorIneficiencia(Integer.valueOf(1));
            }
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        } else {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = listEficienciaVeiculoColeta.get(0);
            eficienciaVeiculoColeta.setQtRetornaram(Integer.valueOf(eficienciaVeiculoColeta.getQtRetornaram().intValue() + 1));
            if (blIneficienciaFrota) {
                eficienciaVeiculoColeta.setQtRetornaramPorIneficiencia(Integer.valueOf(eficienciaVeiculoColeta.getQtRetornaramPorIneficiencia().intValue() + 1));
            }
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        }
    }


    /**
     * Grava / altera registro na tabela EFICIENCIA_VEICULO_COLETA.
     *
     * @param meioTransporteRodoviarioNovo
     * @param idPedidoColeta
     */
    private void gravarEficienciaVeiculoColetaByRetornoColeta(MeioTransporteRodoviario meioTransporteRodoviarioNovo, Long idPedidoColeta) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        Map<String, Object> mapEficienciaVeiculoColeta = new HashMap<String, Object>();
        Map<String, Object> meioTransporteRodoviario = new HashMap<String, Object>();
        meioTransporteRodoviario.put("idMeioTransporte", meioTransporteRodoviarioNovo.getIdMeioTransporte());
        mapEficienciaVeiculoColeta.put("meioTransporteRodoviario", meioTransporteRodoviario);
        mapEficienciaVeiculoColeta.put("dtMovimentacao", dataHoraAtual.toYearMonthDay());
        List<EficienciaVeiculoColeta> listEficienciaVeiculoColeta = eficienciaVeiculoColetaService.find(mapEficienciaVeiculoColeta);
        if (listEficienciaVeiculoColeta.isEmpty()) {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = inicializaEficienciaVeiculoColeta();
            eficienciaVeiculoColeta.setMeioTransporteRodoviario(meioTransporteRodoviarioNovo);
            eficienciaVeiculoColeta.setDtMovimentacao(dataHoraAtual.toYearMonthDay());
            eficienciaVeiculoColeta.setQtTransmitidas(Integer.valueOf(1));
            eficienciaVeiculoColeta.setQtProgramadasNaData(Integer.valueOf(1));
            eficienciaVeiculoColeta.setQtTotalProgramacoes(Integer.valueOf(1));
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        } else {
            EficienciaVeiculoColeta eficienciaVeiculoColeta = listEficienciaVeiculoColeta.get(0);
            eficienciaVeiculoColeta.setQtTransmitidas(Integer.valueOf(eficienciaVeiculoColeta.getQtTransmitidas().intValue() + 1));
            eficienciaVeiculoColeta.setQtProgramadasNaData(Integer.valueOf(eficienciaVeiculoColeta.getQtProgramadasNaData().intValue() + 1));
            eficienciaVeiculoColeta.setQtTotalProgramacoes(Integer.valueOf(eficienciaVeiculoColeta.getQtTotalProgramacoes().intValue() + 1));

            Integer qtdEventoColeta = eventoColetaService.findQtdEventoColeta(idPedidoColeta, "RC");
            if (qtdEventoColeta != null && qtdEventoColeta.compareTo(0) > 0) {
                eficienciaVeiculoColeta.setQtProgramadasMaisUmaVez(Integer.valueOf(eficienciaVeiculoColeta.getQtProgramadasMaisUmaVez().intValue() + 1));
            }
            eficienciaVeiculoColetaService.store(eficienciaVeiculoColeta);
        }
    }

    /**
     * Chama o findPaginated default
     *
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedDefault(Map<String, Object> criteria) {
        return super.findPaginated(criteria);
    }

    /**
     * Chama o getRowCount default
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountDefault(Map<String, Object> criteria) {
        return super.getRowCount(criteria);
    }

    /**
     * @param idManifestoColeta
     * @return
     */
    public TypedFlatMap findSumValoresByMoedaByManifestoColeta(Long idManifestoColeta) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("vlTotalVerificado", BigDecimalUtils.ZERO);
        mapRetorno.put("psTotalVerificado", BigDecimalUtils.ZERO);

        List<Map<String, Object>> lista = getPedidoColetaDAO().findSumValoresByMoedaByManifestoColeta(idManifestoColeta);
        if (lista.isEmpty()) {
            return mapRetorno;
        }
        Map<String, Object> map = lista.get(0);

        if (map.get("psTotalVerificado") != null) {
            mapRetorno.put("psTotalVerificado", map.get("psTotalVerificado"));
        }

        BigDecimal valor = (BigDecimal) map.get("vlTotalVerificado");
        if (valor == null) {
            return mapRetorno;
        }

        Long idMoedaCadastrada = (Long) map.get("idMoeda");

        Moeda moedaUsuario = SessionUtils.getMoedaSessao();
        if (moedaUsuario.getIdMoeda().compareTo(idMoedaCadastrada) != 0) {
            valor = conversaoMoedaService.findConversaoMoeda(
                    SessionUtils.getPaisSessao().getIdPais(),
                    moedaUsuario.getIdMoeda(),
                    SessionUtils.getPaisSessao().getIdPais(),
                    idMoedaCadastrada,
                    dataHoraAtual.toYearMonthDay(),
                    valor
            );
        }
        mapRetorno.put("vlTotalVerificado", valor);
        return mapRetorno;
    }

    /**
     * Retorna uma list de registros de Pedido Coleta com o ID do Manifesto Coleta
     *
     * @param idManifestoColeta
     * @return
     */
    public List<PedidoColeta> findPedidoColetaByIdManifestoColeta(Long idManifestoColeta) {
        return this.getPedidoColetaDAO().findPedidoColetaByIdManifestoColeta(idManifestoColeta);
    }

    /**
     * Retorna uma list de registros de Pedido Coleta com os detalhes da mesma com o ID do Manifesto Coleta
     *
     * @param idManifestoColeta
     * @return
     */
    public List<PedidoColeta> findPedidoColetaByIdManifestoColetaComDetalhes(Long idManifestoColeta) {
        return this.getPedidoColetaDAO().findPedidoColetaByIdManifestoColetaComDetalhes(idManifestoColeta);
    }

    /**
     * Retorna a contagem de registros de Pedido Coleta com o ID do Manifesto Coleta
     *
     * @param idManifestoColeta
     * @return
     */
    public Integer getCountByManifestoColeta(Long idManifestoColeta) {
        return this.getPedidoColetaDAO().getCountByManifestoColeta(idManifestoColeta);
    }

    /**
     * Obtém a soma do valorTotalVerificado do pedido de coleta, agrupando por moeda.
     *
     * @param idManifestoColeta
     * @return
     */
    public List<Map<String, Object>> findValorVerificadoPedidoColetaByIdManifestoColeta(Long idManifestoColeta) {
        return getPedidoColetaDAO().findValorVerificadoPedidoColetaByIdManifestoColeta(idManifestoColeta);
    }

    /**
     * Método que busca uma lista de PedidoColeta usando como filtro uma coleção de Status de Coleta e
     * pelo ID da filial responsável.
     *
     * @param List tiposStatusColeta
     * @param Long IdFilialResponsavel
     * @return List
     */
    public List<PedidoColeta> findPedidoColetaByTpStatusColetaByIdFilialResponsavel(List<String> listTpStatusColeta, Long idFilialResponsavel) {
        return this.getPedidoColetaDAO().findPedidoColetaByTpStatusColetaByIdFilialResponsavel(listTpStatusColeta, idFilialResponsavel);
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     *
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedColetasPendentes(Long idControleCarga, FindDefinition findDefinition) {
        return getPedidoColetaDAO().findPaginatedColetasPendentes(idControleCarga, findDefinition);
    }

    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados
     * parametros.
     *
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountColetasPendentes(Long idControleCarga) {
        Integer rowCount = getPedidoColetaDAO().getRowCountColetasPendentes(idControleCarga);
        return rowCount;
    }

    /**
     * Método que retorna uma list de Pedidos de Coleta para o ID da Cotação informada.
     *
     * @param idCotacao
     * @return
     */
    public List<PedidoColeta> findPedidoColetaByIdCotacao(Long idCotacao) {
        return this.getPedidoColetaDAO().findPedidoColetaByIdCotacao(idCotacao);
    }

    public BigDecimal findSumVlTotalVerificadoByManifestoColeta(Long idManifestoColeta) {
        BigDecimal valor = BigDecimalUtils.ZERO;
        List<Map<String, Object>> result = getPedidoColetaDAO().findSomatorioPedidoColetaAgrupandoPorMoeda(idManifestoColeta);
        if (result.isEmpty()) {
            return valor;
        }

        Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
        YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

        for (Map<String, Object> map : result) {
            BigDecimal vlSoma = (BigDecimal) map.get("somaVlTotalVerificado");
            if (vlSoma != null) {
                EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao((Long) map.get("idFilialResponsavel"));
                Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();
                valor = valor.add(
                        conversaoMoedaService.findConversaoMoeda(
                                idPaisOrigem,
                                (Long) map.get("idMoeda"),
                                idPaisDestino,
                                SessionUtils.getMoedaSessao().getIdMoeda(),
                                dataAtual,
                                vlSoma
                        )
                );
            }
        }
        return valor;
    }

    /**
     * Valida a cotação
     *
     * @param idCotacao
     * @param idCliente
     * @param idMunicipio
     */
    public void executeValidaCotacao(Long idCotacao, Long idCliente, Long idMunicipio) {
        Cotacao cotacao = cotacaoService.findCotacaoByIdCotacao(idCotacao);
        executeValidaCotacao(cotacao, idCliente, idMunicipio);
    }

    public void executeValidaCotacao(Cotacao cotacao, Long idCliente, Long idMunicipio) {
        this.executeValidaCotacao(cotacao, idCliente, idMunicipio, null);
    }

    /**
     * Valida a cotação
     *
     * @param cotacao
     * @param idCliente
     * @param idMunicipio
     */
    public void executeValidaCotacao(Cotacao cotacao, Long idCliente, Long idMunicipio, Long idPedidoColeta) {
        if (cotacao == null) {
            throw new BusinessException("LMS-02073");
        }
        if (idPedidoColeta == null) {
            List<PedidoColeta> listPedidoColeta = this.findPedidoColetaByIdCotacao(cotacao.getIdCotacao());
            for (PedidoColeta pedidoColeta : listPedidoColeta) {
                if (!"CA".equals(pedidoColeta.getTpStatusColeta().getValue())) {
                    throw new BusinessException("LMS-02076");
                }
            }
        }
        if (!isCotacaoAprovada(cotacao, idPedidoColeta)) {
            throw new BusinessException("LMS-02072");
        }
        if (isRemetenteDiferenteClienteColeta(cotacao, idCliente)) {
            // Cliente remetente da cotação não é igual ao cliente da coleta.
            Pessoa pessoa = cotacao.getClienteByIdClienteSolicitou().getPessoa();
            throw new BusinessException("LMS-02071", new Object[]{FormatUtils.formatIdentificacao(pessoa) + " - " + pessoa.getNmPessoa()});
        }
        if (isMunicipioCotacaoIgualOrigemColeta(cotacao, idMunicipio)) {
            // Municipio Origem da cotação não é igual ao municipio da coleta.
            Municipio municipio = municipioService.findById(cotacao.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
            throw new BusinessException("LMS-02075", new Object[]{municipio.getNmMunicipio()});
        }
    }

    protected boolean isMunicipioCotacaoIgualOrigemColeta(Cotacao cotacao, Long idMunicipio) {
        return cotacao.getMunicipioByIdMunicipioOrigem() != null && idMunicipio != null && !cotacao.getMunicipioByIdMunicipioOrigem().getIdMunicipio().equals(idMunicipio);
    }

    protected boolean isRemetenteDiferenteClienteColeta(Cotacao cotacao, Long idCliente) {
        return cotacao.getClienteByIdClienteSolicitou() != null && idCliente != null && !cotacao.getClienteByIdClienteSolicitou().getIdCliente().equals(idCliente);
    }

    protected boolean isCotacaoAprovada(Cotacao cotacao, Long idPedidoColeta) {
        return (CotacaoService.SITUACAO_COTACAO_APROVADA.equals(cotacao.getTpSituacao().getValue()) && !JTDateTimeUtils.getDataAtual().isAfter(cotacao.getDtValidade())) || idPedidoColeta != null;
    }

    /**
     * Retorna o Maior Número de Coleta existente na Tabela PEDIDO_COLETA para a Filial
     */
    public Long findMaiorNroColetaByFilial(Filial filial) {
        return getPedidoColetaDAO().findMaiorNroColetaByFilial(filial);
    }

    /**
     * Retorna o último PedidoColeta do Cliente. Caso não encontre, retorna null;
     *
     * @param idCliente
     * @return List: Pedidos de Coleta do Cliente.
     */
    public PedidoColeta findUltimoPedidoColetaByIdCliente(Long idCliente) {
        return getPedidoColetaDAO().findUltimoPedidoColetaByIdCliente(idCliente);
    }

    public boolean findPedidoColetaJaExecutadoByMeiodeTransporte(Long idPedidoColeta, Long idMeioTransporte) {
        return getPedidoColetaDAO().findPedidoColetaJaExecutadoByMeiodeTransporte(idPedidoColeta, idMeioTransporte);
    }


    /**
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap validateExigenciasGerRisco(Long idControleCarga) {
        List<TypedFlatMap> listEnquadramento = procedimentoGerRiscoService.
                generateIdentificarEnquadramentoRegrasGerRiscoParaColetaEntrega(
                        idControleCarga,
                        SessionUtils.getMoedaSessao().getIdMoeda(),
                        SessionUtils.getPaisSessao().getIdPais(),
                        "P",
                        Boolean.FALSE
                );

        Boolean blPossuiLiberacaoGerRisco = verificaPossuiLiberacaoGerRisco(listEnquadramento);
        if (blPossuiLiberacaoGerRisco) {
            listEnquadramento = procedimentoGerRiscoService.
                    generateIdentificarEnquadramentoRegrasGerRiscoParaColetaEntrega(
                            idControleCarga,
                            SessionUtils.getMoedaSessao().getIdMoeda(),
                            SessionUtils.getPaisSessao().getIdPais(),
                            "P",
                            Boolean.TRUE
                    );

            blPossuiLiberacaoGerRisco = verificaPossuiLiberacaoGerRisco(listEnquadramento);
        }

        TypedFlatMap tfm = new TypedFlatMap();
        tfm.put("blPossuiLiberacaoGerRisco", blPossuiLiberacaoGerRisco);

        if (!procedimentoGerRiscoService.generateIdentificacaoExigenciasGerRisco(listEnquadramento).isEmpty()) {
            tfm.put("blPossuiExigenciasGerRisco", Boolean.TRUE);
        } else {
            tfm.put("blPossuiExigenciasGerRisco", Boolean.FALSE);
        }

        return tfm;
    }

    /**
     * @param listEnquadramento
     * @return
     */
    private Boolean verificaPossuiLiberacaoGerRisco(List<TypedFlatMap> listEnquadramento) {
        Boolean blPossuiLiberacaoGerRisco = Boolean.TRUE;
        for (TypedFlatMap mapParam : listEnquadramento) {
            if (mapParam.getBoolean("blRequerLiberacaoCemop")) {
                blPossuiLiberacaoGerRisco = Boolean.FALSE;
                break;
            }
        }
        return blPossuiLiberacaoGerRisco;
    }

    /**
     * @param idControleCarga
     * @param idMeioTransporte
     * @param idPedidoColeta
     * @param validateGerRisco
     * @return
     */
    public TypedFlatMap generateTransmissaoColetaByLiberacaoGerRisco(Long idControleCarga, Long idMeioTransporte, Long idPedidoColeta, boolean validateGerRisco) {

        long tempoInicial = System.currentTimeMillis();

        generateTransmitirColeta(idMeioTransporte, idControleCarga, idPedidoColeta, null);

        Boolean blPossuiLiberacaoGerRisco = Boolean.TRUE;
        Boolean blPossuiExigenciasGerRisco = Boolean.FALSE;

        if (validateGerRisco) {
            TypedFlatMap mapExigGerRisco = validateExigenciasGerRisco(idControleCarga);
            blPossuiLiberacaoGerRisco = mapExigGerRisco.getBoolean("blPossuiLiberacaoGerRisco");
            blPossuiExigenciasGerRisco = mapExigGerRisco.getBoolean("blPossuiExigenciasGerRisco");

            if (!blPossuiLiberacaoGerRisco) {
                throw new BusinessException("ExecutarRollback");
            }
        }

        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("blPossuiLiberacaoGerRisco", blPossuiLiberacaoGerRisco);
        mapRetorno.put("blRedirecionar", blPossuiExigenciasGerRisco);
        mapRetorno.put(ID_CONTROLE_CARGA, idControleCarga);

        log.warn(String.valueOf(System.currentTimeMillis() - tempoInicial) + "ms");

        return mapRetorno;
    }


    /**
     * Retorna os Pedidos de Coleta que estejam no status, na rota informada e com filial responsável igual à filial informada,
     * cuja data de previsao coleta seja menor ou igual a data passada.
     *
     * @param tpStatusColeta:      é o status da coleta.
     * @param idRotaColetaEntrega: rota de coleta entrega.
     * @param idFilialResponsavel: filial responsavel pela coleta.
     * @param dataPrevisaoColeta:  é o data de previsão de coleta.
     * @return List: Pedidos.
     */
    public List<PedidoColeta> findPedidosColetaByStatusByIdRotaColetaEntregaUntilPrevisaoColeta(String tpStatusColeta, Long idFilialResponsavel, Long idRotaColetaEntrega, YearMonthDay dataPrevisaoColeta) {
        return getPedidoColetaDAO().findPedidosColetaByStatusByIdRotaColetaEntregaUntilPrevisaoColeta(tpStatusColeta, idFilialResponsavel, idRotaColetaEntrega, dataPrevisaoColeta);
    }

    /**
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findSomatorioColetasRealizadasByIdControleCarga(Long idControleCarga) {
        List<TypedFlatMap> result = getPedidoColetaDAO().findSomatorioColetasRealizadasByIdControleCarga(idControleCarga);
        result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
        BigDecimal vlColetado = BigDecimalUtils.ZERO;
        BigDecimal psColetado = BigDecimalUtils.ZERO;
        BigDecimal psAforadoColetado = BigDecimalUtils.ZERO;
        YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        for (TypedFlatMap tfm : result) {
            if (tfm.getBigDecimal("somaPsTotalVerificado") != null) {
                psColetado = psColetado.add(tfm.getBigDecimal("somaPsTotalVerificado"));
            }
            if (tfm.getBigDecimal("somaPsTotalAforadoVerificado") != null) {
                psAforadoColetado = psAforadoColetado.add(tfm.getBigDecimal("somaPsTotalAforadoVerificado"));
            }
            if (tfm.getBigDecimal("somaVlTotalVerificado") != null) {
                EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao(tfm.getLong("idFilial"));
                BigDecimal vlConvertido = conversaoMoedaService.findConversaoMoeda(
                        enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais(),
                        tfm.getLong("idMoeda"),
                        SessionUtils.getPaisSessao().getIdPais(),
                        SessionUtils.getMoedaSessao().getIdMoeda(),
                        dtAtual,
                        tfm.getBigDecimal("somaVlTotalVerificado"));

                vlColetado = vlColetado.add(vlConvertido);
            }
        }

        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("vlColetado", vlColetado);
        mapRetorno.put("psColetado", psColetado);
        mapRetorno.put("psAforadoColetado", psAforadoColetado);
        return mapRetorno;
    }

    /**
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findSomatorioColetasASeremRealizadasByIdControleCarga(Long idControleCarga) {
        List<TypedFlatMap> result = getPedidoColetaDAO().findSomatorioColetasASeremRealizadasByIdControleCarga(idControleCarga);
        result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
        BigDecimal vlAColetar = BigDecimalUtils.ZERO;
        BigDecimal psAColetar = BigDecimalUtils.ZERO;
        YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        for (TypedFlatMap tfm : result) {
            if (tfm.getBigDecimal("somaPsTotalVerificado") != null) {
                psAColetar = psAColetar.add(tfm.getBigDecimal("somaPsTotalVerificado"));
            }
            if (tfm.getBigDecimal("somaVlTotalVerificado") != null) {
                EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao(tfm.getLong("idFilial"));
                BigDecimal vlConvertido = conversaoMoedaService.findConversaoMoeda(
                        enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais(),
                        tfm.getLong("idMoeda"),
                        SessionUtils.getPaisSessao().getIdPais(),
                        SessionUtils.getMoedaSessao().getIdMoeda(),
                        dtAtual,
                        tfm.getBigDecimal("somaVlTotalVerificado")
                );

                vlAColetar = vlAColetar.add(vlConvertido);
            }
        }

        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("vlAColetar", vlAColetar);
        mapRetorno.put("psAColetar", psAColetar);
        return mapRetorno;
    }


    public void removeByIdsColetasProgramadas(List<Long> ids) {
        for (Long id : ids) {
            PedidoColeta pedidoColeta = findById(id);
            pedidoColeta.setBlAlteradoPosProgramacao(Boolean.FALSE);
            store(pedidoColeta);
        }
    }

    /**
     * Método responsável por gravar os eventos da coleta.
     *
     * @param idPedidoColeta
     * @param strTpEventoColeta
     * @param idMeioTransporte
     * @param idDetalheColeta
     */
    public void generateEventoColeta(Long idPedidoColeta, String strTpEventoColeta, Long idMeioTransporte, Long idDetalheColeta) {
        strTpEventoColeta = strTpEventoColeta.toUpperCase();
        PedidoColeta pc = findById(idPedidoColeta);
        DetalheColeta dc = detalheColetaService.findById(idDetalheColeta);
        MeioTransporteRodoviario mtr = meioTransporteRodoviarioService.findById(idMeioTransporte);

        List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(strTpEventoColeta);
        OcorrenciaColeta oc = listOcorrenciaColeta.get(0);

        gravarEventoColeta(SessionUtils.getUsuarioLogado(), pc, mtr, oc, strTpEventoColeta, oc.getDsDescricaoResumida().toString(), dc);

        if ("CP".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("AD"));
        } else if (strTpEventoColeta.equals(PedidoColetaServiceConstants.EVENTO_COLETA_SOLICITACAO)) {
            pc.setTpStatusColeta(new DomainValue("AB"));
        } else if ("ID".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("ED"));
        } else if ("FD".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("NT"));
        } else if (strTpEventoColeta.equals(PedidoColetaServiceConstants.EVENTO_COLETA_EXECUCAO)) {
            pc.setTpStatusColeta(new DomainValue(PedidoColetaServiceConstants.EVENTO_COLETA_EXECUCAO));
        } else if ("TR".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("TR"));
        } else if ("MA".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("MA"));
        } else if ("CA".equals(strTpEventoColeta)) {
            pc.setTpStatusColeta(new DomainValue("CA"));
        }
        super.store(pc);
    }

    /**
     * @param idControleCarga
     * @param tpStatusColeta
     * @return
     */
    public List<PedidoColeta> findPedidoColetaByIdControleCargaByTpStatusColeta(Long idControleCarga, String tpStatusColeta) {
        return getPedidoColetaDAO().findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, tpStatusColeta);
    }

    public Boolean validatePedidoColetaByIdControleCarga(Long idControleCarga) {
        return getPedidoColetaDAO().validatePedidoColetaByIdControleCarga(idControleCarga);
    }

    public boolean validateColetaExecutadaOuNao(Long idControleCarga) {
        return getPedidoColetaDAO().validateColetaExecutadaOuNao(idControleCarga);
    }

    /**
     * Alterar o campo tpStatusColeta para "AB" na tabela PedidoColeta para o Controle de Carga
     * em questão.
     *
     * @param idControleCarga
     */
    public void updateStatusColetaByIdControleCarga(Long idControleCarga) {
        getPedidoColetaDAO().updateStatusColetaByIdControleCarga(idControleCarga);
    }

    public List<PedidoColeta> findPedidoColetaByIdManifestoColetaIdNotaCredito(Long idManifestoColeta, Long idNotaCredito) {
        return getPedidoColetaDAO().findPedidoColetaByIdManifestoColetaIdNotaCredito(idManifestoColeta, idNotaCredito);
    }

    public PedidoColeta findByNrChave(String nrChave) {
        return getPedidoColetaDAO().findByNrChave(nrChave);
    }

    public List<RelatorioEficienciaColetaDetalhadoDTO> listRelatorioEficienciaColetaDetalhado(
            YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
            String modoPedidoColeta, Long idRegional, Long idCliente,
            Long idRotaColetaEntrega, String efc) {
        return getPedidoColetaDAO().listRelatorioEficienciaColetaDetalhado(
                dataInicial, dataFinal, idFilial, modoPedidoColeta, idRegional,
                idCliente, idRotaColetaEntrega, efc);
    }

    public List<RelatorioEficienciaColetaResumidoDTO> listRelatorioEficienciaColetaResumido(
            YearMonthDay dataInicial, YearMonthDay dataFinal, Long idFilial,
            String modoPedidoColeta, Long idRegional, Long idCliente,
            Long idRotaColetaEntrega, String efc) {
        return getPedidoColetaDAO().listRelatorioEficienciaColetaResumido(
                dataInicial, dataFinal, idFilial, modoPedidoColeta, idRegional,
                idCliente, idRotaColetaEntrega, efc);
    }

    public List<Map<String, Object>> findPedidoColetaSuggest(String sgFilial, Long nrPedidoColeta, Long idEmpresa) {
        return getPedidoColetaDAO().findPedidoColetaSuggest(sgFilial, nrPedidoColeta, idEmpresa);
    }

    public void generateEventosColetaAeroByIdControleCarga(Long idControleCarga, Short cdEventoNovo, Short cdLocalizacaoAtual) {
        generateEventosColetaAeroByIdControleCarga(idControleCarga, cdEventoNovo, new Short[]{cdLocalizacaoAtual});
    }

    public void generateEventosColetaAeroByIdControleCarga(Long idControleCarga, Short cdEventoNovo, Short[] cdsLocalizacaoAtual) {
        ControleCarga cc = controleCargaService.findById(idControleCarga);
        String mrDocumento = FormatUtils.formatSgFilialWithLong(cc.getFilialByIdFilialOrigem().getSgFilial(), cc.getNrControleCarga());
        generateEventosColetaAeroByIdControleCarga(idControleCarga, cdEventoNovo, cdsLocalizacaoAtual, null, mrDocumento, PedidoColetaServiceConstants.CONTROLE_CARGA);
    }

    public void generateEventosColetaAeroByIdControleCarga(Long idControleCarga, Short cdEventoNovo, Short[] cdsLocalizacaoAtual, Long idFilial, String nrDocumento, String tpDocumento) {
        List<PedidoColeta> pedidos = this.findPedidoColetaByIdControleCargaByTpStatusColeta(idControleCarga, null);
        for (PedidoColeta pedidoColeta : pedidos) {
            generateEventosColetaAeroByPedidoColeta(pedidoColeta, cdEventoNovo, cdsLocalizacaoAtual, idFilial, nrDocumento, tpDocumento);
        }
    }


    public boolean isVolumePrevistoDescargaAereo(final Long idVolumeNotaFiscal, Long idControleCarga, final Long idFilialSessao, final Short cdLocalizacaMercadoria) {
        List<VolumeNotaFiscal> volumes = this.getPedidoColetaDAO().findVolumesPedidoColetaByControleCarga(idControleCarga);
        return CollectionUtils.exists(volumes, new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                VolumeNotaFiscal volume = (VolumeNotaFiscal) object;
                return volume.getIdVolumeNotaFiscal().equals(idVolumeNotaFiscal)
                        && (cdLocalizacaMercadoria == null || cdLocalizacaMercadoria.equals(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()))
                        && idFilialSessao.equals(volume.getLocalizacaoFilial().getIdFilial());
            }
        });
    }

    public void generateEventoDoctoVolume(Conhecimento conhecimento, Short cdEvento, Short[] cdsLocalizacaoAtual) {
        generateEventoDoctoVolume(conhecimento, cdEvento, cdsLocalizacaoAtual, null, null);
    }

    public void generateEventoDoctoVolume(Long idConhecimento, Short cdEvento, Short[] cdsLocalizacaoAtual, String dsObservacao) {
        Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);
        generateEventoDoctoVolume(conhecimento, cdEvento, cdsLocalizacaoAtual, null, dsObservacao);
    }

    public void generateEventoDoctoVolume(Conhecimento conhecimento, Short cdEvento, Short[] cdsLocalizacaoAtual, Long idFilial, String dsObservacao) {
        String nrDocumento = ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico());
        generateEventoDoctoVolume(conhecimento, nrDocumento, conhecimento.getTpDoctoServico().getValue(), cdEvento, cdsLocalizacaoAtual, idFilial, dsObservacao, SessionUtils.getUsuarioLogado(), JTDateTimeUtils.getDataHoraAtual());
    }

    public void generateEventoDoctoVolume(Conhecimento conhecimento, String nrDocumento, String tpDocumento, Short cdEvento, Short[] cdsLocalizacaoAtual, Long idFilial, String dsObservacao, Usuario usuario, DateTime dhEvento) {

        if (cdsLocalizacaoAtual == null ||
                org.apache.commons.lang.ArrayUtils.contains(cdsLocalizacaoAtual, conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {

            idFilial = idFilial == null ? SessionUtils.getFilialSessao().getIdFilial() : idFilial;

            /** Gera eventos para documentos e volumes*/
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento,
                    conhecimento.getIdDoctoServico(),
                    idFilial,
                    nrDocumento,
                    dhEvento, null, dsObservacao,
                    tpDocumento, Boolean.FALSE, Boolean.FALSE, idFilial, usuario);

            List<VolumeNotaFiscal> volumeNotaFiscais = getVolumeNotaFiscalService().findByIdConhecimento(conhecimento.getIdDoctoServico());

            eventoVolumeService.storeEventoVolumeDhOcorrencia(volumeNotaFiscais, cdEvento, ConstantesSim.TP_SCAN_LMS, idFilial, usuario, dhEvento);

            List<DispositivoUnitizacao> listDU = new ArrayList<DispositivoUnitizacao>();
            for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscais) {
                if (volumeNotaFiscal.getDispositivoUnitizacao() != null) {
                    final Long idDU = volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao();
                    boolean blExists = CollectionUtils.exists(listDU, new Predicate() {
                        @Override
                        public boolean evaluate(Object arg0) {
                            DispositivoUnitizacao du = (DispositivoUnitizacao) arg0;
                            return idDU.equals(du.getIdDispositivoUnitizacao());
                        }
                    });

                    if (!blExists) {
                        listDU.add(volumeNotaFiscal.getDispositivoUnitizacao());
                    }
                }
            }
            eventoDispositivoUnitizacaoService.generateEventoDispositivo(listDU, cdEvento, ConstantesSim.TP_SCAN_LMS, idFilial);
        }
    }

    public List<Map<String, Object>> findPedicoColetasPendentes(TypedFlatMap parameter) {
        return getPedidoColetaDAO().findPedidosColetasPendentes(parameter);
    }

    public List<Map<String, Object>> findPedicoColetasRealizadas(TypedFlatMap parameter) {
        return getPedidoColetaDAO().findPedidosColetasRealizadas(parameter);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setPedidoColetaDAO(PedidoColetaDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public PedidoColetaDAO getPedidoColetaDAO() {
        return (PedidoColetaDAO) getDao();
    }

    public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
        this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
    }

    public void setProcedimentoGerRiscoService(ProcedimentoGerRiscoService procedimentoGerRiscoService) {
        this.procedimentoGerRiscoService = procedimentoGerRiscoService;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public DomainValueService getDomainValueService() {
        return domainValueService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setEficienciaVeiculoColetaService(EficienciaVeiculoColetaService eficienciaVeiculoColetaService) {
        this.eficienciaVeiculoColetaService = eficienciaVeiculoColetaService;
    }

    public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
        this.manifestoColetaService = manifestoColetaService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setEventoColetaService(EventoColetaService eventoColetaService) {
        this.eventoColetaService = eventoColetaService;
    }

    public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
        this.ocorrenciaColetaService = ocorrenciaColetaService;
    }

    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public void setMilkRunService(MilkRunService milkRunService) {
        this.milkRunService = milkRunService;
    }

    public void setSemanaRemetMrunService(SemanaRemetMrunService semanaRemetMrunService) {
        this.semanaRemetMrunService = semanaRemetMrunService;
    }

    public void setColetaAutomaticaClienteService(ColetaAutomaticaClienteService coletaAutomaticaClienteService) {
        this.coletaAutomaticaClienteService = coletaAutomaticaClienteService;
    }

    public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
        this.detalheColetaService = detalheColetaService;
    }

    public void setTelefoneContatoService(TelefoneContatoService telefoneContatoService) {
        this.telefoneContatoService = telefoneContatoService;
    }

    public TelefoneEnderecoService getTelefoneEnderecoService() {
        return telefoneEnderecoService;
    }

    public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
        this.telefoneEnderecoService = telefoneEnderecoService;
    }

    public void setServicoAdicionalColetaService(ServicoAdicionalColetaService servicoAdicionalColetaService) {
        this.servicoAdicionalColetaService = servicoAdicionalColetaService;
    }

    public void setPedidoColetaProdutoService(PedidoColetaProdutoService pedidoColetaProdutoService) {
        this.pedidoColetaProdutoService = pedidoColetaProdutoService;
    }

    public ServicoService getServicoService() {
        return servicoService;
    }

    public void setServicoService(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    public EnderecoPessoaService getEnderecoPessoaService() {
        return enderecoPessoaService;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

    public void setTipoEnderecoPessoaService(TipoEnderecoPessoaService tipoEnderecoPessoaService) {
        this.tipoEnderecoPessoaService = tipoEnderecoPessoaService;
    }

    public void setAwbColetaService(AwbColetaService awbColetaService) {
        this.awbColetaService = awbColetaService;
    }

    public void setNotaFiscalColetaService(NotaFiscalColetaService notaFiscalColetaService) {
        this.notaFiscalColetaService = notaFiscalColetaService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public MunicipioService getMunicipioService() {
        return municipioService;
    }

    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }

    public void setRegiaoColetaEntregaFilService(RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
        this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
    }

    public RotaColetaEntregaService getRotaColetaEntregaService() {
        return rotaColetaEntregaService;
    }

    public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
        this.rotaColetaEntregaService = rotaColetaEntregaService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setPreAlertaService(PreAlertaService preAlertaService) {
        this.preAlertaService = preAlertaService;
    }

    public void setCotacaoService(CotacaoService cotacaoService) {
        this.cotacaoService = cotacaoService;
    }

    public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
        this.versaoDescritivoPceService = versaoDescritivoPceService;
    }

    public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setProdutoService(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    public void setMonitoramentoNotasFiscaisCCTService(
            MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
        this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
    }

    public CtoAwbService getCtoAwbService() {
        return ctoAwbService;
    }

    public void setCtoAwbService(CtoAwbService ctoAwbService) {
        this.ctoAwbService = ctoAwbService;
    }

    public AwbService getAwbService() {
        return awbService;
    }

    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    public ConhecimentoService getConhecimentoService() {
        return conhecimentoService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
        return incluirEventosRastreabilidadeInternacionalService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(
            IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public VolumeNotaFiscalService getVolumeNotaFiscalService() {
        return volumeNotaFiscalService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
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

    public Integer findDocumentosAwbComColeta(Long id, ItemList lista, ItemListConfig config, PedidoColeta pedidoColeta) {
        List<Long> idsConhecimento = findIdsConhecimentoByAwb(id, null);
        Integer qtPedidoColeta = 0;
        for (Long idConhecimento : idsConhecimento) {
            qtPedidoColeta = qtPedidoColeta + this.findRowCountByIdDoctoServicoNotCanceled(idConhecimento, null);
        }
        for (Long idConhecimento : idsConhecimento) {
            qtPedidoColeta = qtPedidoColeta + validateAwbIntersec(idConhecimento, pedidoColeta, config, lista, null);
        }

        if (qtPedidoColeta == idsConhecimento.size()) {
            return PedidoColetaServiceConstants.BLOQUEAR_INCLUSAO;
        } else if (qtPedidoColeta == PedidoColetaServiceConstants.INCLUSAO_NORMAL) {
            return PedidoColetaServiceConstants.INCLUSAO_NORMAL;
        } else {
            return PedidoColetaServiceConstants.INCLUSAO_PARCIAL;
        }
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setControleTrechoService(ControleTrechoService controleTrechoService) {
        this.controleTrechoService = controleTrechoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setLocalizacaoMercadoriaService(
            LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setPreManifestoDocumentoService(
            PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setPreManifestoVolumeService(
            PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public void setCarrementoMobileService(CarregamentoMobileService carrementoMobileService) {
        this.carregamentoMobileService = carrementoMobileService;
    }

    public List<Map<String, Object>> findPedidoColetaLoja(Long idCliente) {
        List<Map<String, Object>> result = getPedidoColetaDAO().findPedidoColetaLoja(idCliente);

        return ajustaRetorno(result);
    }

    private List<Map<String, Object>> ajustaRetorno(List<Map<String, Object>> result) {
        for (Map<String, Object> item : result) {
            DateTime dateTime = (DateTime) item.remove("dhEvento");
            item.put("nrColetaDhEvento", item.get(PedidoColetaServiceConstants.NR_COLETA) + " " + JTFormatUtils.format(dateTime));
        }
        return result;
    }

    public List<Map<String, Object>> findPedidoColetaJaInseridoByAwb(Long idAwb) {
        return getPedidoColetaDAO().findPedidoColetaJaInseridoByAwb(idAwb);
    }

    public PedidoColeta findPedidoColetaByAwb(Long idAwb) {
        List<Map<String, Object>> pcMap = this.findPedidoColetaJaInseridoByAwb(idAwb);
        if (pcMap != null && !pcMap.isEmpty()) {
            return findById((Long) pcMap.get(0).get("id_pedido_coleta"));
        }
        return null;
    }

    public EventoControleCargaService getEventoControleCargaService() {
        return eventoControleCargaService;
    }


    public void confirmarDataHoraEvento(TypedFlatMap retonoExecutarDTO) {
        Long idPedidoColeta = retonoExecutarDTO.getLong(ID_PEDIDO_COLETA);
        DateTime dhLiberacao = retonoExecutarDTO.getDateTime("dhLiberacao");
        Long idControleCarga = retonoExecutarDTO.getLong(ID_CONTROLE_CARGA);

        boolean transmitida = false;

        List<EventoControleCarga> eventosChegada = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "CP");

        DateTime dhEventoColetaTransmitida = getDhEventoColetaTransmitida(idPedidoColeta);

        DateTime dhDhEventoManifestado = getDhEventoManifestado(idPedidoColeta);

        transmitida = isColetaTransmitida(transmitida, dhEventoColetaTransmitida, dhDhEventoManifestado);

        if (transmitida) {
            validaDhExecucaoRetornoColetaTransmitida(dhLiberacao, eventosChegada, dhEventoColetaTransmitida);
        } else {
            validaDhExecucaoRetornoColetaNaoTransmitida(dhLiberacao, idControleCarga, eventosChegada);
        }

        validaDhEventoChegada(dhLiberacao, transmitida, eventosChegada);

    }

    private boolean isColetaTransmitida(boolean transmitida, DateTime dhEventoColetaTransmitida, DateTime dhDhEventoManifestado) {

        Boolean isColetaTransmitida = transmitida;

        if (dhEventoColetaTransmitida == null) {
            isColetaTransmitida = false;
        }
        if (dhDhEventoManifestado == null) {
            isColetaTransmitida = true;
        }

        // se transmitida maior que manifestada seto transmitida
        if (dhEventoColetaTransmitida != null && dhDhEventoManifestado != null && dhEventoColetaTransmitida.isAfter(dhDhEventoManifestado)) {
            isColetaTransmitida = true;
        }
        return isColetaTransmitida;
    }

    private void validaDhEventoChegada(DateTime dhLiberacao, boolean transmitida, List<EventoControleCarga> eventosChegada) {
        if (!eventosChegada.isEmpty()) {
            validaDhChegadaPortaria(dhLiberacao, transmitida, eventosChegada);
        } else {
            validaDhLiberacao(dhLiberacao, transmitida);
        }
    }

    private void validaDhLiberacao(DateTime dhLiberacao, boolean transmitida) {
        DateTime dhAgora = JTDateTimeUtils.getDataHoraAtual();
        if (dhLiberacao.isAfter(dhAgora)) {
            if (transmitida) {
                throw new BusinessException("LMS-02108");
            } else {
                throw new BusinessException("LMS-02109");
            }
        }
    }

    private void validaDhChegadaPortaria(DateTime dhLiberacao, boolean transmitida, List<EventoControleCarga> eventosChegada) {
        DateTime dhChegadaPortaria = eventosChegada.get(0).getDhEvento();
        if (dhLiberacao.isAfter(dhChegadaPortaria)) {
            if (transmitida) {
                throw new BusinessException("LMS-02110");
            } else {
                throw new BusinessException("LMS-02107");
            }
        }
    }

    private void validaDhExecucaoRetornoColetaNaoTransmitida(DateTime dhLiberacao, Long idControleCarga, List<EventoControleCarga> eventosChegada) {

        List<EventoControleCarga> eventosSaidaPortaria = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "SP");
        if (!eventosSaidaPortaria.isEmpty()) {
            DateTime dhSaidaPortaria = (DateTime) eventosSaidaPortaria.get(0).getDhEvento();

            if (dhLiberacao.isBefore(dhSaidaPortaria)) {
                if (!eventosChegada.isEmpty()) {
                    throw new BusinessException("LMS-02107");
                } else {
                    throw new BusinessException("LMS-02109");
                }

            }
        }
    }

    private void validaDhExecucaoRetornoColetaTransmitida(DateTime dhLiberacao, List<EventoControleCarga> eventosChegada, DateTime dhTransmitida) {
        if (dhLiberacao.isBefore(dhTransmitida)) {
            if (!eventosChegada.isEmpty()) {
                // A data/hora da ocorrência deve ser posterior à data/hora
                // da transmissão da coleta e anterior à chegada do veículo
                // na portaria.
                throw new BusinessException("LMS-02110");
            } else {
                // A data/hora da ocorrência deve ser posterior à data/hora
                // da transmissão da coleta e igual ou anterior à data
                // atual.
                throw new BusinessException("LMS-02108");
            }
        }
    }

    private DateTime getDhEventoManifestado(Long idPedidoColeta) {
        DateTime dhManifestada = null;
        List<Map> eventosColetaManifestada = eventoColetaService.findEventoColetaManifestadaByIdPedidoColeta(idPedidoColeta);
        if (!eventosColetaManifestada.isEmpty()) {
            dhManifestada = (DateTime) eventosColetaManifestada.get(0).get("dhEvento");
        }
        return dhManifestada;
    }

    private DateTime getDhEventoColetaTransmitida(Long idPedidoColeta) {
        DateTime dhTransmitida = null;
        List<Map> eventosColetaTransmitida = eventoColetaService.findEventoColetaTransmitidaByIdPedidoColeta(idPedidoColeta);
        if (!eventosColetaTransmitida.isEmpty()) {
            dhTransmitida = (DateTime) eventosColetaTransmitida.get(0).get("dhEvento");
        }
        return dhTransmitida;
    }
    
    public List<Map<String, Object>> findInfoColetasAbertas(Map<String, Object> parameters) {
		return getPedidoColetaDAO().findInfoColetasAbertas(parameters);
	}
    
    public File executeExportacaoCsv(TypedFlatMap parameters, File generateOutputDir) {
		
		Map<String, Object> mapParameters = getParameters(parameters);
		
		List<Map<String, Object>> lista = findInfoColetasAbertas(mapParameters);
		
		return FileUtils.generateReportFile(changeTitles(lista), generateOutputDir, REPORT_NAME);
	}

	private List<Map<String, Object>> changeTitles(List<Map<String, Object>> inputList) {
		List<Map<String, Object>> outputList = new ArrayList<Map<String,Object>>(); 
		for (Map<String, Object> inputMap : inputList) {
			LinkedHashMap<String, Object> outputMap = new LinkedHashMap<String, Object>();
			
			outputMap.put("Coleta", inputMap.get("SG_FILIAL_COLETA") + " " + inputMap.get("NR_COLETA"));
			
			if("J".equals(inputMap.get("TP_PESSOA"))){
				outputMap.put(CNPJ_CLIENTE, StringUtils.leftPad((String) inputMap.get(NR_IDENTIFICACAO), SIZE_CNPJ, "0"));
			}else{
				outputMap.put(CNPJ_CLIENTE, StringUtils.leftPad((String) inputMap.get(NR_IDENTIFICACAO), SIZE_CPF, "0"));
			}
			
			outputMap.put("Cliente", inputMap.get(CLIENTE));
			outputMap.put("Endereço Coleta", (String) inputMap.get("ENDERECO") + inputMap.get("NR_ENDERECO"));
			outputMap.put("Complemento", inputMap.get("COMPLEMENTO"));
			outputMap.put("CEP", StringUtils.leftPad((String) inputMap.get("NR_CEP"), SIZE_CEP, "0"));
			outputMap.put("Município", inputMap.get(MUNICIPIO));
			outputMap.put("UF", inputMap.get("UF"));
			outputMap.put("Volumes", inputMap.get("VOLUMES"));
			outputMap.put("Peso", FormatUtils.formatDecimalLocale("#,###,##0.000", inputMap.get("PESO") != null ? (BigDecimal) inputMap.get("PESO") : BigDecimal.ZERO));
			outputMap.put("Peso Aforado", FormatUtils.formatDecimalLocale("#,###,##0.000", inputMap.get("PESO_AFORADO") != null ? (BigDecimal) inputMap.get("PESO_AFORADO") : BigDecimal.ZERO));
			outputMap.put("Valor Mercadoria", inputMap.get(MOEDA) + " " + FormatUtils.formatDecimalLocale("#,###,##0.00", inputMap.get(VALOR) != null ? (BigDecimal) inputMap.get(VALOR) : BigDecimal.ZERO));
			outputMap.put("Numero da Rota", inputMap.get("NR_ROTA"));
			outputMap.put("Nome da Rota", inputMap.get("DS_ROTA"));
			
			DateTime dtCorte = new DateTime(((java.sql.Timestamp) inputMap.get("HR_CORTE_SOLICITACAO")).getTime());
			outputMap.put("Horário de Corte", JTDateTimeUtils.formatDateTimeToString(dtCorte, "HH:mm"));
			
			DateTime dtLimite = new DateTime(((java.sql.Timestamp) inputMap.get("HR_LIMITE_COLETA")).getTime());
			outputMap.put("Horário Limite", JTDateTimeUtils.formatDateTimeToString(dtLimite, "HH:mm"));
			
			outputList.add(outputMap);
			
		}
		
		return outputList;
	}
	
	private  Map<String, Object> getParameters(TypedFlatMap parameters) {
		Map<String, Object> mapParameters = new HashMap<String, Object>();
		
		Long idFilial =  parameters.getLong("filial.idFilial");
        if (idFilial!=null) {
        	mapParameters.put("idFilial", idFilial);
        }            
            
        Long idRotaColetaEntrega = parameters.getLong("rotaColetaEntrega.idRotaColetaEntrega");
        if (idRotaColetaEntrega!=null) {
        	mapParameters.put("idRotaColetaEntrega", idRotaColetaEntrega);
        }            
        
        Long idServico = parameters.getLong("servico.idServico"); 
        if (idServico!=null) {
        	mapParameters.put("idServico", idServico);
        }            
        
        YearMonthDay dataInicial = parameters.getYearMonthDay(DATA_INICIAL);
        if (dataInicial!=null) {
        	mapParameters.put(DATA_INICIAL, dataInicial);
        }
        
        YearMonthDay dataFinal = parameters.getYearMonthDay(DATA_FINAL);
        if (dataFinal!=null) {
        	mapParameters.put(DATA_FINAL, dataFinal);
        }
        
        return mapParameters;
	}


    public void setEventoControleCargaService(
            EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }

    public PedidoColeta findPedidoColetaByIdDoctoServicoAndTpColeta(Long idDoctoServico, String tpPedidoColeta) {
        return getPedidoColetaDAO().findPedidoColetaByIdDoctoServicoAndTpColeta(idDoctoServico, tpPedidoColeta);
    }

    public EventoDocumentoServicoService getEventoDocumentoServicoService() {
        return eventoDocumentoServicoService;
    }

    public void setEventoDocumentoServicoService(
            EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public AwbOcorrenciaService getAwbOcorrenciaService() {
        return awbOcorrenciaService;
    }

    public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
        this.awbOcorrenciaService = awbOcorrenciaService;
    }

    public void setTrackingAwbService(TrackingAwbService trackingAwbService) {
        this.trackingAwbService = trackingAwbService;
    }

    public MoedaService getMoedaService() {
        return moedaService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public MunicipioFilialService getMunicipioFilialService() {
        return municipioFilialService;
    }

    public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
        this.municipioFilialService = municipioFilialService;
    }

    public NaturezaProdutoService getNaturezaProdutoService() {
        return naturezaProdutoService;
    }

    public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
        this.naturezaProdutoService = naturezaProdutoService;
    }

    public UsuarioService getUsuarioService() {
        return this.usuarioService;
    }

    public void setNomeProdutoService(NomeProdutoService nomeProdutoService) {
        this.nomeProdutoService = nomeProdutoService;
}

    public void setProdutoCategoriaProdutoService(
            ProdutoCategoriaProdutoService produtoCategoriaProdutoService) {
        this.produtoCategoriaProdutoService = produtoCategoriaProdutoService;
    }

    public List<PedidoColeta> findPedidoColetaByIds(List<Long> idsPedidoColeta){
        return getPedidoColetaDAO().findPedidoColetaByIds(idsPedidoColeta);
    }
}

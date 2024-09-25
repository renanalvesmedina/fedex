package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleCargaConhScan;
import com.mercurio.lms.carregamento.model.DescargaManifesto;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EstoqueDispIdentificado;
import com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.EventoDescargaVeiculo;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.JustificativaDoctoNaoCarregado;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.MotivoCancelDescarga;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.dao.CarregamentoDescargaDAO;
import com.mercurio.lms.carregamento.model.dao.ControleCargaDAO;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.coleta.model.service.EventoManifestoColetaService;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaServiceConstants;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.FotoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.constantes.ConsErro;
import com.mercurio.lms.constantes.ConsGeral;
import com.mercurio.lms.constantes.entidades.ConsBox;
import com.mercurio.lms.constantes.entidades.ConsCarregamentoDescarga;
import com.mercurio.lms.constantes.entidades.ConsControleCarga;
import com.mercurio.lms.constantes.entidades.ConsControleQuilometragem;
import com.mercurio.lms.constantes.entidades.ConsDescargaManifesto;
import com.mercurio.lms.constantes.entidades.ConsDoctoServico;
import com.mercurio.lms.constantes.entidades.ConsEquipe;
import com.mercurio.lms.constantes.entidades.ConsEventoColeta;
import com.mercurio.lms.constantes.entidades.ConsEventoManifesto;
import com.mercurio.lms.constantes.entidades.ConsEventoManifestoColeta;
import com.mercurio.lms.constantes.entidades.ConsFilial;
import com.mercurio.lms.constantes.entidades.ConsManifesto;
import com.mercurio.lms.constantes.entidades.ConsManifestoColeta;
import com.mercurio.lms.constantes.entidades.ConsPedidoColeta;
import com.mercurio.lms.constantes.entidades.ConsVolumeNotaFiscal;
import com.mercurio.lms.constantes.modulos.ConsCarregamento;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CIOTService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.GerarNotaCreditoService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.GerarRateioFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.service.BoxService;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.service.GerarEnviarSMPService;
import com.mercurio.lms.sgr.model.service.PlanoGerenciamentoRiscoService;
import com.mercurio.lms.sgr.model.service.SolicMonitPreventivoService;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeGenerator;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.RegistroPriorizacaoDoctoService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;


/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.carregamento.carregamentoDescargaService"
 */
public class CarregamentoDescargaService extends CrudService<CarregamentoDescarga, Long> {

    private static final String TP_EVENTO_MANIFESTO = "tpEventoManifesto";
    private static final String EQUIPE_OPERACAO = "equipeOperacao";
    private static final String CARREGAMENTO_DESCARGA = "carregamentoDescarga";
    private static final String MANIFESTO = "manifesto";
    private static final String MANIFESTO_COLETA = "manifestoColeta";
    private static final String FILIAL_USUARIO = "filialUsuario";
    private static final String DATA_HORA = "dataHora";
    public static final String DS_SERVICO_ALIAS = "ser";
    private static final Short CD_OCORRENCIA_ENTREGA_PARCIAL = Short.valueOf("102");

    private ControleCargaDAO controleCargaDAO;
    private ControleCargaService controleCargaService;
    private CarregamentoPreManifestoService carregamentoPreManifestoService;
    private BoxService boxService;
    private EventoMeioTransporteService eventoMeioTransporteService;
    private EventoControleCargaService eventoControleCargaService;
    private EventoManifestoService eventoManifestoService;
    private EventoManifestoColetaService eventoManifestoColetaService;
    private EventoColetaService eventoColetaService;
    private FilialService filialService;
    private GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService;
    private UsuarioService usuarioService;
    private EquipeService equipeService;
    private EquipeOperacaoService equipeOperacaoService;
    private DoctoServicoService doctoServicoService;
    private ControleQuilometragemService controleQuilometragemService;
    private ManifestoService manifestoService;
    private ManifestoEntregaService manifestoEntregaService;
    private ManifestoColetaService manifestoColetaService;
    private DescargaManifestoService descargaManifestoService;
    private PedidoColetaService pedidoColetaService;
    private OcorrenciaColetaService ocorrenciaColetaService;
    private LacreControleCargaService lacreControleCargaService;
    private FotoCarregmtoDescargaService fotoCarregmtoDescargaService;
    private FotoService fotoService;
    private MotivoCancelDescargaService motivoCancelDescargaService;
    private DispCarregDescQtdeService dispCarregDescQtdeService;
    private DispCarregIdentificadoService dispCarregIdentificadoService;
    private EstoqueDispositivoQtdeService estoqueDispositivoQtdeService;
    private EstoqueDispIdentificadoService estoqueDispIdentificadoService;
    private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
    private DispositivoUnitizacaoService dispositivoUnitizacaoService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private GerarNotaCreditoService gerarNotaCreditoService;
    private VersaoDescritivoPceService versaoDescritivoPceService;
    private ConfiguracoesFacade configuracoesFacade;
    private ParametroGeralService parametroGeralService;
    private JustificativaDoctoNaoCarregadoService justificativaDoctoNaoCarregadoService;
    private RegistroPriorizacaoDoctoService registroPriorizacaoDoctoService;
    private DomainValueService domainValueService;
    private ConversaoMoedaService conversaoMoedaService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private EventoVolumeService eventoVolumeService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private ControleCargaConhScanService controleCargaConhScanService;
    private ManifestoEletronicoService manifestoEletronicoService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private CarregamentoDescargaVolumeService carregamentoDescargaVolumeService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
    private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;
    private PerfilUsuarioService perfilUsuarioService;
    private CIOTService ciotService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private SolicMonitPreventivoService solicMonitPreventivoService;
    private GerarEnviarSMPService gerarEnviarSMPService;
    
    
    private static final Log LOG = LogFactory.getLog(CarregamentoDescargaService.class);


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setCarregamentoDescargaDAO(CarregamentoDescargaDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CarregamentoDescargaDAO getCarregamentoDescargaDAO() {
        return (CarregamentoDescargaDAO) getDao();
    }


    /**
     * Recupera uma instância de <code>CarregamentoDescarga</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public CarregamentoDescarga findById(java.lang.Long id) {
        return (CarregamentoDescarga) super.findById(id);
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
    @Override
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(CarregamentoDescarga bean) {
        return super.store(bean);
    }

    /**
     * Faz a chamada para a camada DAO, retornando o numero de registros encontrados contendo um determinado
     * numero de controle de carga
     *
     * @param criteria   vindo da view que deve conter um nrControleCarga
     * @param tpOperacao
     * @return
     */
    public Integer getRowCountCarregamentoDescarga(TypedFlatMap criteria, String tpOperacao) {
        Long idControleCarga = criteria.getLong(ConsCarregamentoDescarga.ID_CONTROLE_CARGA);
        Long idFilial = criteria.getLong(ConsFilial.ID_FILIAL);
        Long idPostoAvancado = criteria.getLong(ConsCarregamentoDescarga.ID_POSTO_AVANCADO);
        return this.getCarregamentoDescargaDAO().getRowCountByIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
    }

    public Integer getRowCountCarregamentoDescarga(Long idControleCarga, String tpOperacao) {
        return this.getCarregamentoDescargaDAO().getRowCountByIdControleCargaTpOperacao(idControleCarga, tpOperacao);
    }


    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um nrControleCarga e um 'FindDefinitio' sejam informados.
     *
     * @param criteria   vindo da view que deve conter um nrControleCarga
     * @param tpOperacao
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedCarregamentoDescarga(TypedFlatMap criteria, String tpOperacao) {
        Long idControleCarga = criteria.getLong(ConsCarregamentoDescarga.ID_CONTROLE_CARGA);
        Long idFilial = criteria.getLong(ConsFilial.ID_FILIAL);
        Long idPostoAvancado = criteria.getLong(ConsCarregamentoDescarga.ID_POSTO_AVANCADO);
        ResultSetPage<Map<String, Object>> rsp = this.getCarregamentoDescargaDAO().findPaginatedByIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao, FindDefinition.createFindDefinition(criteria));
        List<Map<String, Object>> rs = rsp.getList();
        for (Map<String, Object> map : rs) {
            DomainValue tpManifesto = (DomainValue) map.get(ConsCarregamentoDescarga.TP_MANIFESTO);
            if ("V".equals(tpManifesto.getValue())) {
                map.put("tipo", map.get("tpManifestoViagem"));
            } else if ("E".equals(tpManifesto.getValue())) {
                map.put("tipo", map.get("tpManifestoEntrega"));
            }
        }
        return rsp;
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um nrControleCarga e um 'FindDefinitio' sejam informados.
     *
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    public List<Map<String, Object>> findCarregamentoDescarga(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao) {
        return this.getCarregamentoDescargaDAO().findByIdControleCarga(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
    }

    /**
     * Método que retorna um Integer da quantidade de Manifestos a partir do ID do Controle de Carga testando se o
     * tipo do Controle de Carga é de 'Viagem' ou 'Coleta'.
     *
     * @param idControleCarga
     * @param tpControleCarga
     * @param idFilialDestino
     * @return
     */
    public Integer getRowCountManifestoByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino) {
        return this.getCarregamentoDescargaDAO().getRowCountManifestoByIdControleCarga(idControleCarga, tpControleCarga, idFilialDestino);
    }

    /**
     * Método que retorna um Integer da quantidade de Manifestos de Viagem e Entrega (menos de Coleta) a partir do
     * ID do Controle de Carga testando se o tipo do Controle de Carga é de 'Viagem' ou 'Coleta'.
     *
     * @param idControleCarga
     * @param tpControleCarga
     * @param idFilialDestino
     * @return
     */
    public Integer getRowCountManifestoViagemEntregaByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino) {
        return this.getCarregamentoDescargaDAO().getRowCountManifestoViagemEntregaByIdControleCarga(idControleCarga, tpControleCarga, idFilialDestino);
    }

    /**
     * Método que retorna um ResultSetPage de Manifestos a partir do ID do Controle de Carga testando se o
     * tipo do Controle de Carga é de 'Viagem' ou 'Coleta' e se o tipo modal e tipo abrangência do Manifesto para
     * pegar o número correspondente.
     *
     * @param idControleCarga
     * @param tpControleCarga
     * @param idFilialDestino
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedManifestoByIdControleCarga(final Long idControleCarga, final String tpControleCarga, final Long idFilialDestino, final FindDefinition findDefinition) {
        final ResultSetPage rsp = this.getCarregamentoDescargaDAO().findPaginatedManifestoByIdControleCarga(idControleCarga, tpControleCarga, idFilialDestino, findDefinition);
        final List<Object[]> listManifestos = rsp.getList();
        final List<TypedFlatMap> listRetornoManifestos = new ArrayList<TypedFlatMap>(listManifestos.size());

        for (final Object[] objResult : listManifestos) {
            final TypedFlatMap mapResult = mountMapByObject(objResult);

            final Long idManifesto = (Long) mapResult.get(ConsCarregamentoDescarga.ID_MANIFESTO);
            if (!"C".equals(mapResult.getDomainValue(ConsCarregamentoDescarga.TP_MANIFESTO).getValue())) {

                /** Otimização LMS-788 */
                final Map<String, String> alias = new HashMap<String, String>();
                alias.put(ConsCarregamentoDescarga.DS_SERVICO, "se");
                alias.put(ConsCarregamentoDescarga.SE_TIPO_SERVICO, "ts");

                final List<Criterion> criterions = new ArrayList<Criterion>();
                criterions.add(Restrictions.or(
                        Restrictions.le(ConsCarregamentoDescarga.DS_DT_PREV_ENTREGA, JTDateTimeUtils.getDataAtual())
                        , Restrictions.eq(ConsCarregamentoDescarga.TS_BL_PRIORIZAR, Boolean.TRUE)
                ));

                final Boolean blPriorizar = doctoServicoService.verifyDoctoServicoByIdManifesto(idManifesto, alias, criterions);
                mapResult.put(ConsCarregamentoDescarga.EM_DESTAQUE, blPriorizar);
            }
            listRetornoManifestos.add(mapResult);
        }

        rsp.setList(listRetornoManifestos);
        return rsp;
    }

    /**
     * Método que monta um map a partir do Objeto.
     *
     * @param objResult
     * @return
     */
    private TypedFlatMap mountMapByObject(Object[] objResult) {

        TypedFlatMap mapResult = new TypedFlatMap();
        mapResult.put(ConsCarregamentoDescarga.ID_MANIFESTO, objResult[ConsCarregamentoDescarga.ARR_POS_ID_MANIFESTO]);
        mapResult.put(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM, objResult[ConsCarregamentoDescarga.ARR_POS_SG_FILIAL_ORIGEM]);
        mapResult.put(ConsCarregamentoDescarga.NR_MANIFESTO, objResult[ConsCarregamentoDescarga.ARR_POS_NR_MANIFESTO]);
        mapResult.put(ConsCarregamentoDescarga.DH_GERACAO_PRE_MANIFESTO, objResult[ConsCarregamentoDescarga.ARR_POS_DH_GERACAO_PRE_MANIFESTO]);

        if (objResult[ConsCarregamentoDescarga.ARR_POS_TP_MANIFESTO] != null) {
            DomainValue domainValue1 = domainValueService.findDomainValueByValue("DM_TIPO_MANIFESTO", objResult[ConsCarregamentoDescarga.ARR_POS_TP_MANIFESTO].toString());
            mapResult.put(ConsCarregamentoDescarga.TP_MANIFESTO, new DomainValue(domainValue1.getValue(), domainValue1.getDescription(), domainValue1.getStatus()));
            DomainValue domainValue2 = domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO", objResult[ConsCarregamentoDescarga.ARR_POS_TP_STATUS_MANIFESTO].toString());
            mapResult.put(ConsCarregamentoDescarga.TP_STATUS_MANIFESTO, new DomainValue(domainValue2.getValue(), domainValue2.getDescription(), domainValue2.getStatus()));
        } else {
            mapResult.put(ConsCarregamentoDescarga.TP_MANIFESTO, new DomainValue("C", new VarcharI18n("Coleta"), Boolean.TRUE));
            DomainValue domainValue2 = domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO_COLETA", objResult[ConsCarregamentoDescarga.ARR_POS_TP_STATUS_MANIFESTO].toString());
            mapResult.put(ConsCarregamentoDescarga.TP_STATUS_MANIFESTO, new DomainValue(domainValue2.getValue(), domainValue2.getDescription(), domainValue2.getStatus()));
        }

        if (objResult[ConsCarregamentoDescarga.ARR_NM_BL_BLOQUEADO] != null) {
            if ("N".equals(objResult[ConsCarregamentoDescarga.ARR_NM_BL_BLOQUEADO].toString())) {
                mapResult.put(ConsCarregamentoDescarga.BL_BLOQUEADO, Boolean.FALSE);
            } else {
                mapResult.put(ConsCarregamentoDescarga.BL_BLOQUEADO, Boolean.TRUE);
            }
        } else {
            mapResult.put(ConsCarregamentoDescarga.BL_BLOQUEADO, objResult[ConsCarregamentoDescarga.ARR_NM_BL_BLOQUEADO]);
        }
        mapResult.put(ConsCarregamentoDescarga.ID_FILIAL_DESTINO, objResult[ConsCarregamentoDescarga.ARR_POS_ID_FILIAL_DESTINO]);
        mapResult.put(ConsCarregamentoDescarga.SG_FILIAL_DESTINO, objResult[ConsCarregamentoDescarga.ARR_POS_SG_FILIAL_DESTINO]);

        if (objResult[ConsCarregamentoDescarga.ARR_POS_VL_TOTAL_MANIFESTO] != null) {
            mapResult.put(ConsCarregamentoDescarga.DS_SIMBOLO, objResult[ConsCarregamentoDescarga.ARR_POS_DS_SIMBOLO]);
            mapResult.put(ConsCarregamentoDescarga.SG_MOEDA, objResult[ConsCarregamentoDescarga.ARR_POS_SG_MOEDA]);
            mapResult.put(ConsCarregamentoDescarga.VL_TOTAL_MANIFESTO, objResult[ConsCarregamentoDescarga.ARR_POS_VL_TOTAL_MANIFESTO]);
        } else {
            BigDecimal valorTotal = BigDecimalUtils.ZERO;
            List<PedidoColeta> listPedidoColeta = pedidoColetaService.findPedidoColetaByIdManifestoColeta(Long.valueOf(objResult[0].toString()));
            Pais paisSessao = SessionUtils.getPaisSessao();
            Moeda moedaSessao = SessionUtils.getMoedaSessao();

            for (PedidoColeta pedidoColeta : listPedidoColeta) {
                valorTotal = valorTotal.add(conversaoMoedaService.findConversaoMoeda(
                        paisSessao.getIdPais(),
                        pedidoColeta.getMoeda().getIdMoeda(),
                        paisSessao.getIdPais(),
                        moedaSessao.getIdMoeda(),
                        JTDateTimeUtils.getDataAtual(),
                        pedidoColeta.getVlTotalVerificado()
                        )
                );
            }

            mapResult.put(ConsCarregamentoDescarga.DS_SIMBOLO, moedaSessao.getDsSimbolo());
            mapResult.put(ConsCarregamentoDescarga.SG_MOEDA, moedaSessao.getSgMoeda());
            mapResult.put(ConsCarregamentoDescarga.VL_TOTAL_MANIFESTO, valorTotal);
        }

        return mapResult;
    }

    /**
     * Método que retorna um ResultSetPage de Manifestos de Viagem e Entrega (menos de Coleta) a partir do ID
     * do Controle de Carga testando se o tipo do Controle de Carga é de 'Viagem' ou 'Coleta'
     * e se o tipo modal e tipo abrangência do Manifesto para pegar o número correspondente.
     *
     * @param idControleCarga
     * @param tpControleCarga
     * @param idFilialDestino
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedManifestoViagemEntregaByIdControleCarga(Long idControleCarga, String tpControleCarga, Long idFilialDestino, FindDefinition findDefinition) {
        return this.getCarregamentoDescargaDAO().findPaginatedManifestoViagemEntregaByIdControleCarga(idControleCarga, tpControleCarga, idFilialDestino, findDefinition);
    }

    /**
     * Retorna um ResultSetPage para a tela de carregarVeiculoGerenciamentoRisco.
     * LSM-6851
     *
     * @param idControleCarga
     * @return
     */
    //@SuppressWarnings("unchecked")
    public ResultSetPage<TypedFlatMap> findPaginatedExigenciasGerRisco(Long idControleCarga) {
        List<TypedFlatMap> lista = new ArrayList<TypedFlatMap>();
        PlanoGerenciamentoRiscoDTO plano = planoGerenciamentoRiscoService.generateEnquadramentoRegra(
                idControleCarga, Boolean.TRUE, Boolean.FALSE);
        planoGerenciamentoRiscoService.generateExigenciasGerRisco(plano);
        Collection<ExigenciaGerRiscoDTO> exigencias = plano.getExigencias();
        Boolean integrantePer = integrantePerfilCemop();
        if (!CollectionUtils.isEmpty(exigencias)) {
            // ordenar a lista por tipo Exigencia e exigencia
            Collections.sort((List<ExigenciaGerRiscoDTO>) exigencias);
            for (ExigenciaGerRiscoDTO exigenciaGerRiscoDTO : exigencias) {
                convertExigenciasToFlatMapList(exigenciaGerRiscoDTO, lista, integrantePer);
            }
        }
        return new ResultSetPage<TypedFlatMap>(1, lista);
    }

    /**
     * popula lista typedFlatMap para mostrar na grid
     * LSM-6851
     *
     * @param exigenciaGerRiscoDTO
     * @param lista
     * @param integrantePer
     */
    private void convertExigenciasToFlatMapList(ExigenciaGerRiscoDTO exigenciaGerRiscoDTO,
                                                List<TypedFlatMap> lista, Boolean integrantePer) {
        Boolean naoPossuiBlRestrito = Boolean.FALSE;
        if (exigenciaGerRiscoDTO.getTipoExigenciaGerRisco() != null
                && exigenciaGerRiscoDTO.getTipoExigenciaGerRisco().getBlRestrito() != null) {
            naoPossuiBlRestrito = !exigenciaGerRiscoDTO.getTipoExigenciaGerRisco().getBlRestrito();
        }
        if (integrantePer || naoPossuiBlRestrito) {
            // popula na grid
            TypedFlatMap tfm = new TypedFlatMap();
            String tipoExigencia = exigenciaGerRiscoDTO.getTipoExigenciaGerRisco().getTpExigencia().getDescriptionAsString();
            String dsResumida = exigenciaGerRiscoDTO.getExigenciaGerRisco().getDsResumida().getValue();
            String filialInicio = exigenciaGerRiscoDTO.getFilialInicio() != null ? exigenciaGerRiscoDTO.getFilialInicio().getSiglaNomeFilial() : "";
            Integer vlKmFranquia = exigenciaGerRiscoDTO.getVlKmFranquia();
            Integer qtdeExigida = exigenciaGerRiscoDTO.getQtExigida();
            String exigencia = exigenciaGerRiscoDTO.getExigenciaGerRisco().getDsCompleta().getValue();
            Long idExigencia = exigenciaGerRiscoDTO.getExigenciaGerRisco().getIdExigenciaGerRisco();
            tfm.put("dsTipoExigencia", tipoExigencia);
            tfm.put("dsResumida", dsResumida);
            tfm.put("filialInicio", filialInicio);
            tfm.put("vlKmFranquia", vlKmFranquia);
            tfm.put("qtExigida", qtdeExigida);
            tfm.put("exigencia", exigencia);
            tfm.put("dsCompleta", exigencia);
            tfm.put("idExigenciaGerRisco", idExigencia);
            tfm.put("exigenciaGerRisco.idExigenciaGerRisco", idExigencia);
            lista.add(tfm);
        }

    }

    /**
     * verifica se o perfil logado possui o perfil de cemop
     * LSM-6851
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    private Boolean integrantePerfilCemop() {
        Usuario user = SessionUtils.getUsuarioLogado();
        String perfilUsuarioCemop = (String) configuracoesFacade.getValorParametro(ConsCarregamento.PA_SGR_PERFIL_USUARIO_CEMOP);
        if (perfilUsuarioCemop != null) {
            List<PerfilUsuario> listaPerfisUsuario = new ArrayList<PerfilUsuario>();
            if (user != null && user.getIdUsuario() != null) {
                List<PerfilUsuario> listaPerfis = perfilUsuarioService.findByIdUsuarioPerfilUsuario(user
                        .getIdUsuario());
                if (listaPerfis != null && !listaPerfis.isEmpty()) {
                    listaPerfisUsuario.addAll(listaPerfis);
                }
            }
            for (PerfilUsuario perfil : listaPerfisUsuario) {
                if (perfil.getPerfil() != null && perfil.getPerfil().getDsPerfil().equals(perfilUsuarioCemop)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }


    /**
     * Busca os dados de controle carga mais os dados especificos para a tela de carregarVeiculoCarregamento
     *
     * @param nrControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    public List<Map<String, Object>> findCarregamentoDescargaByNrControleCarga(Long nrControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao) {
        List<Map<String, Object>> result = controleCargaService.findControleCargaByNrControleCarga(nrControleCarga, idFilial, tpOperacao);
        if (!result.isEmpty()) {
            Map<String, Object> resultMap = result.get(0);

            Long idControleCarga = Long.valueOf(resultMap.get(ConsCarregamentoDescarga.ID_CONTROLE_CARGA).toString());
            Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

            List<Map<String, Object>> resultCarregagamento = this.getCarregamentoDescargaDAO().findCarregamentoDescargaByIdControleCarga(idControleCarga, idFilialUsuario, idPostoAvancado, tpOperacao);
            if (!resultCarregagamento.isEmpty()) {
                Map<String, Object> resultCarregagamentoMap = resultCarregagamento.get(0);
                resultMap.put(ConsCarregamentoDescarga.ID_CARREGAMENTO_DESCARGA, resultCarregagamentoMap.get(ConsCarregamentoDescarga.ID_CARREGAMENTO_DESCARGA));
                resultMap.put(ConsCarregamentoDescarga.SG_FILIAL_POSTO_AVANCADO, resultCarregagamentoMap.get(ConsCarregamentoDescarga.SG_FILIAL_POSTO_AVANCADO));
                resultMap.put(ConsCarregamentoDescarga.NM_PESSOA_POSTO_AVANCADO, resultCarregagamentoMap.get(ConsCarregamentoDescarga.NM_PESSOA_POSTO_AVANCADO));
                resultMap.put(ConsCarregamentoDescarga.DH_INICIO_OPERACAO, resultCarregagamentoMap.get(ConsCarregamentoDescarga.DH_INICIO_OPERACAO));
                resultMap.put(ConsCarregamentoDescarga.DH_FIM_OPERACAO, resultCarregagamentoMap.get(ConsCarregamentoDescarga.DH_FIM_OPERACAO));
                resultMap.put(ConsCarregamentoDescarga.SG_FILIAL, resultCarregagamentoMap.get(ConsCarregamentoDescarga.SG_FILIAL));
                resultMap.put(ConsCarregamentoDescarga.NM_PESSOA, resultCarregagamentoMap.get(ConsCarregamentoDescarga.NM_PESSOA));
                resultMap.put(ConsCarregamentoDescarga.NM_FANTASIA, resultCarregagamentoMap.get(ConsCarregamentoDescarga.NM_FANTASIA));
                resultMap.put(ConsCarregamentoDescarga.TP_STATUS_OPERACAO, resultCarregagamentoMap.get(ConsCarregamentoDescarga.TP_STATUS_OPERACAO));
            }
        }

        return result;
    }


    public List<Map<String, Object>> findCarregamentoDescargaByIdControleCarga(Long idControleCarga, Long idFilialUsuario, Long idPostoAvancado, String tpOperacao) {
        return this.getCarregamentoDescargaDAO().findCarregamentoDescargaByIdControleCarga(idControleCarga, idFilialUsuario, idPostoAvancado, tpOperacao);
    }

    /**
     * Faz a conversao para a moeda do usuario logado e soma os valores para os campos
     * da tela de 'Gerenciamento de riscos'
     *
     * @param idControleCarga
     * @return
     */
    public Map<String, Object> findValoresManifesto(Long idControleCarga) {
        Map<String, Object> valoresManifesto = new HashMap<String, Object>();

        BigDecimal totalCarregadoSeguroCliente = controleCargaService.generateCalculaValorTotalMercadoriaControleCargaPreManifesto(idControleCarga, Boolean.FALSE);
        BigDecimal totalCarregadoSeguroMercurio = controleCargaService.generateCalculaValorTotalMercadoriaControleCargaPreManifesto(idControleCarga, Boolean.TRUE);
        BigDecimal totalCarregado = totalCarregadoSeguroCliente.add(totalCarregadoSeguroMercurio);

        Map<String, Object> moeda = new HashMap<String, Object>();
        moeda.put("dsMoeda", SessionUtils.getMoedaSessao().getSgMoeda() + " " + SessionUtils.getMoedaSessao().getDsSimbolo());
        moeda.put("totalCarregado", totalCarregado);
        moeda.put("totalCarregadoSeguroCliente", totalCarregadoSeguroCliente);
        moeda.put("totalCarregadoSeguroMercurio", totalCarregadoSeguroMercurio);

        valoresManifesto.put("moeda", moeda);

        return valoresManifesto;
    }

    /**
     * Gerar o registro de inicio de carregamento, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja null ou atualiza a entidade, caso contrário. Considera a data/hora atual, ou seja, a data/hora
     * em que o método é chamado.
     *
     * @param criteria
     * @param equipeOperacao
     * @param items
     * @param config
     * @return
     */
    public java.io.Serializable storeInicioCarregamento(TypedFlatMap criteria, EquipeOperacao equipeOperacao, ItemList items, ItemListConfig config) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        return this.storeInicioCarregamento(criteria, equipeOperacao, items, config, dataHoraAtual);
    }


    /**
     * Gerar o registro de inicio de carregamento
     * caso o id seja null ou atualiza a entidade, caso contrário.
     * Assinatura do método alterada para poder passar uma dataHora diferente da atual
     * conforme solicitação CQPRO00005473 da integração.
     *
     * @param controleCarga
     * @param idBox
     * @param equipeOperacao
     * @param equipe
     * @param dataHora
     * @return
     */
    public Serializable storeInicioCarregamento(ControleCarga controleCarga, Long idBox, EquipeOperacao equipeOperacao, Equipe equipe, DateTime dataHora) {
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        Filial filialUsuario = SessionUtils.getFilialSessao();

        Box box = null;
        if (idBox != null) {
            box = boxService.storeOcuparBox(idBox);
        }

        //Verifica se a filial de origem do controle de carga e igual a filial atual...
        if (controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals(filialUsuario.getIdFilial())) {
            controleCarga.setTpStatusControleCarga(new DomainValue("EC"));
        } else {
            controleCarga.setTpStatusControleCarga(new DomainValue("CP"));
        }

        //Persiste os objetos...
        controleCargaService.store(controleCarga);

        //Caso o meio de transporte exista...
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            //Gerando novo evento de meio de transporte...
            EventoMeioTransporte eventoMeioTransporte = populaEvento(
                    "EMCA",
                    dataHora,
                    null,
                    box,
                    controleCarga,
                    filialUsuario,
                    controleCarga.getMeioTransporteByIdTransportado(),
                    SessionUtils.getUsuarioLogado());

            //Gerando evento de rastreabilidade...
            eventoMeioTransporteService.generateEvent(eventoMeioTransporte, dataHora);
        }

        //Caso o semi-reboque exista...
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            //Gerando novo evento de meio de transporte...
            EventoMeioTransporte eventoMeioTransporte = populaEvento(
                    "EMCA",
                    dataHora,
                    null,
                    box,
                    controleCarga,
                    filialUsuario,
                    controleCarga.getMeioTransporteByIdSemiRebocado(),
                    SessionUtils.getUsuarioLogado());

            //Gerando evento de rastreabilidade...
            eventoMeioTransporteService.generateEvent(eventoMeioTransporte, dataHora);
        }

        //Gerando um novo carregamentoDescarga...
        CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
        carregamentoDescarga.setDhInicioOperacao(dataHora);
        carregamentoDescarga.setTpOperacao(new DomainValue("C"));
        carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
        //Gerando os relacionamentos...
        carregamentoDescarga.setControleCarga(controleCarga);
        carregamentoDescarga.setFilial(filialUsuario);
        carregamentoDescarga.setBox(box);

        if (equipeOperacao != null && equipe != null) {
            equipeOperacao.setDhInicioOperacao(dataHora);
            //Gerando os relacionamentos...
            equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
            equipeOperacao.setControleCarga(controleCarga);
        }

		/* Persiste CarregamentoDescarga */
        Long idCarregamentoDescarga = (Long) this.store(carregamentoDescarga);

		/* Atribui e grava a equipe de operação ao carregamentoDescarga */
        equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
        equipeOperacao.setControleCarga(controleCarga);
        equipeOperacaoService.store(equipeOperacao);

        //Gerando o evento de controle de carga...
        eventoControleCargaService.storeEventoControleCarga("IC", "Início de Carregamento", filialUsuario, controleCarga, equipeOperacao, dataHora, usuarioLogado);

        return idCarregamentoDescarga;
    }


    /**
     * Gerar o registro de inicio de carregamento, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja null ou atualiza a entidade, caso contrário.
     * Assinatura do método alterada para poder passar uma dataHora diferente da atual
     * conforme solicitação CQPRO00005473 da integração.
     *
     * @param criteria
     * @param equipeOperacao
     * @param items
     * @param config
     * @param dataHora
     * @return
     */
    public Serializable storeInicioCarregamento(TypedFlatMap criteria, EquipeOperacao equipeOperacao, ItemList items, ItemListConfig config, DateTime dataHora) {
        Long idControleCarga = criteria.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        if (controleCarga.getManifestos().isEmpty()) {
            throw new BusinessException("LMS-05156");
        }

        //o IF foi alterado para atender a Integração, pois items pode ser vazio na Integração.
        if (!SessionUtils.isIntegrationRunning() && !items.hasItems()) {
            throw new BusinessException(ConsErro.EQP_PRECISA_UM_FUNC_OU_TERCEIRO);
        } else {
            for (Iterator<IntegranteEqOperac> iter = items.iterator(equipeOperacao.getIdEquipeOperacao(), config); iter.hasNext(); ) {
                IntegranteEqOperac integranteEqOperac = iter.next();
                integranteEqOperac.setEquipeOperacao(equipeOperacao);

                //Remove a instancia setada na action para visualizacao apenas...
                if ("F".equals(integranteEqOperac.getTpIntegrante().getValue())) {
                    integranteEqOperac.setCargoOperacional(null);
                }
            }
        }

        Long idBox = criteria.getLong(ConsBox.BOX_ID_BOX);
        Long idEquipe = criteria.getLong(ConsEquipe.EQUIPE_ID_EQUIPE);
        Equipe equipe = null;

        //Busca objetos para a sessao do hibernate...
        if (idEquipe != null) {
            equipe = equipeService.findById(idEquipe);
        }


        if (equipeOperacao != null && equipe != null) {
            equipeOperacao.setDhInicioOperacao(dataHora);
            //Gerando os relacionamentos...
            equipeOperacao.setControleCarga(controleCarga);
            equipeOperacao.setEquipe(equipe);

            //Persistencia da DF2
            equipeOperacaoService.storeEquipeOperacao(equipeOperacao, items);
        }

        return this.storeInicioCarregamento(controleCarga, idBox, equipeOperacao, equipe, dataHora);
    }

    /**
     * Inicia o carregamento de um determinado manifesto
     *
     * @param idManifesto
     * @param idCarregamentoDescarga
     */
    public void storeIniciarCarregamentoPreManifesto(Long idManifesto, Long idCarregamentoDescarga) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        this.storeIniciarCarregamentoPreManifesto(idManifesto, idCarregamentoDescarga, dataHoraAtual);
    }

    /**
     * Inicia o carregamento de um determinado manifesto
     * Assinatura do método alterada para contemplar solicitação CQPRO00005475 da Integração.
     * Para os casos do LMS onde nao é necessário a passagem de todos os parâmetros, chamar a
     * storeIniciarCarregamentoPreManifesto(Long idManifesto, Long idCarregamentoDescarga)
     *
     * @param idManifesto
     * @param idCarregamentoDescarga
     * @param dataHora
     */
    public void storeIniciarCarregamentoPreManifesto(Long idManifesto, Long idCarregamentoDescarga, DateTime dataHora) {
        ControleCarga controleCarga = controleCargaDAO.findControleCargaByIdManifesto(idManifesto, "C");
        if ("EC".equals(controleCarga.getTpStatusControleCarga().getValue()) ||
                "CP".equals(controleCarga.getTpStatusControleCarga().getValue())) {

            Filial filial = SessionUtils.getFilialSessao();
            Usuario usuario = SessionUtils.getUsuarioLogado();
            Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
            CarregamentoDescarga carregamentoDescarga = this.findById(idCarregamentoDescarga);

            List<EquipeOperacao> equipes = equipeOperacaoService.findEquipeOperacaoByIdControleCarga(controleCarga.getIdControleCarga(), true);
            EquipeOperacao equipeOperacao = equipes.get(0);

            //Gera novos objetos a serem persistidos na tabela...
            EventoManifesto eventoManifesto = new EventoManifesto();
            CarregamentoPreManifesto carregamentoPreManifesto = new CarregamentoPreManifesto();

            manifesto.setTpStatusManifesto(new DomainValue("EC"));

            eventoManifesto.setTpEventoManifesto(new DomainValue("IC"));
            eventoManifesto.setDhEvento(dataHora);
            eventoManifesto.setManifesto(manifesto);
            eventoManifesto.setFilial(filial);
            eventoManifesto.setUsuario(usuario);

            carregamentoPreManifesto.setDhInicioCarregamento(dataHora);
            carregamentoPreManifesto.setEquipeOperacao(equipeOperacao);
            carregamentoPreManifesto.setCarregamentoDescarga(carregamentoDescarga);
            carregamentoPreManifesto.setManifesto(manifesto);

            //Persistindo os objetos...
            manifestoService.storeBasic(manifesto);
            eventoManifestoService.storeBasic(eventoManifesto);
            carregamentoPreManifestoService.store(carregamentoPreManifesto);

            //Verifica o tipo de controle de carga para gerar um codigo de evento especifico para o mesmo...
            Short cdEvento = null;
            String tpDocumento = null;
            if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM)) {
                cdEvento = Short.valueOf("25");
                tpDocumento = "PMV"; //Pre-Manifesto Viagem
            } else {
                cdEvento = Short.valueOf("24");
                tpDocumento = "PME"; //Pre-Manifesto Entrega
            }
            this.generateEventoDocumentoServicoByIdPreManifesto(manifesto.getIdManifesto(), cdEvento, tpDocumento, dataHora);

            if (manifestoService.verificaGeraEventoVolume(manifesto)) { //LMSA-3449
                // [CQPRO00027292] Desenvolvimento do quest acima ([CQ25050]) ocasionou efeito colateral no módulo MWW, pois estava colocando em Carregamento
                // volumes que não são daquele manifesto, pois a busca foi feita pelo controle de cargas. Foi corrigido para buscar pelo menifesto.
                List<PreManifestoVolume> listaPreManifestoVolume = preManifestoVolumeService.findByManifesto(idManifesto);
                generateEventoVolumeFromPreManifestoVolume(cdEvento, listaPreManifestoVolume);
            }
        } else {
            throw new BusinessException("LMS-05020");
        }
    }

    private void generateEventoVolumeFromPreManifestoVolume(Short cdEvento, List<PreManifestoVolume> listaPreManifestoVolume) {
        for (PreManifestoVolume preManifestoVolume : listaPreManifestoVolume) {
            if (preManifestoVolume.getVolumeNotaFiscal().getLocalizacaoMercadoria() != null && preManifestoVolume.getVolumeNotaFiscal().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().longValue() == 1) {
                continue;
            }
            eventoVolumeService.generateEventoVolume(preManifestoVolume.getVolumeNotaFiscal(), cdEvento, "LM");
        }
    }

    /**
     * Gerar o registro de inicio de descarga, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja null ou atualiza a entidade, caso contrário.
     *
     * @param parameters
     * @param equipeOperacao
     * @param itemsIntegranteEqOperac
     * @param itemsIntegranteEqOperacConfig
     * @param itemsLacreControleCarga
     * @param itemsLacreControleCargaConfig
     * @param itemsFotoCarregmtoDescarga
     * @param itemsFotoCarregmtoDescargaConfig
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable storeInicioDescarga(
            TypedFlatMap parameters,
            EquipeOperacao equipeOperacao,
            ItemList itemsIntegranteEqOperac,
            ItemListConfig itemsIntegranteEqOperacConfig,
            ItemList itemsLacreControleCarga,
            ItemListConfig itemsLacreControleCargaConfig,
            ItemList itemsFotoCarregmtoDescarga,
            ItemListConfig itemsFotoCarregmtoDescargaConfig
    ) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

        return this.storeInicioDescarga(
                parameters,
                equipeOperacao,
                itemsIntegranteEqOperac,
                itemsIntegranteEqOperacConfig,
                itemsLacreControleCarga,
                itemsLacreControleCargaConfig,
                itemsFotoCarregmtoDescarga,
                itemsFotoCarregmtoDescargaConfig,
                dataHoraAtual,
                ConstantesSim.TP_SCAN_LMS,
                null
        );
    }

    public java.io.Serializable storeInicioDescargaByMWW(TypedFlatMap parameters,
                                                         DateTime dataHora,
                                                         List<LacreControleCarga> lacreControleCargaList) {
        return this.storeInicioDescarga(
                parameters,
                null, null, null, null,
                null, null, null,
                dataHora,
                ConstantesSim.TP_SCAN_FISICO,
                lacreControleCargaList
        );
    }

    /**
     * Gerar o registro de inicio de descarga, inserindo tambem os dados da tela filho (integrantes)
     * caso o id seja null ou atualiza a entidade, caso contrário.
     * Assinatura alterada conforme solicitação da Integração.
     *
     * @param parameters
     * @param equipeOperacao
     * @param itemsIntegranteEqOperac
     * @param itemsIntegranteEqOperacConfig
     * @param itemsLacreControleCarga
     * @param itemsLacreControleCargaConfig
     * @param itemsFotoCarregmtoDescarga
     * @param itemsFotoCarregmtoDescargaConfig
     * @param dataHora
     * @param tpScan
     * @param lacreControleCargaList
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable storeInicioDescarga(TypedFlatMap parameters,
                                                    EquipeOperacao equipeOperacao,
                                                    ItemList itemsIntegranteEqOperac,
                                                    ItemListConfig itemsIntegranteEqOperacConfig,
                                                    ItemList itemsLacreControleCarga,
                                                    ItemListConfig itemsLacreControleCargaConfig,
                                                    ItemList itemsFotoCarregmtoDescarga,
                                                    ItemListConfig itemsFotoCarregmtoDescargaConfig,
                                                    DateTime dataHora,
                                                    String tpScan,
                                                    List<LacreControleCarga> lacreControleCargaList) {

        Boolean semLacre = false;
        String obSemLacre = "";

        if (ConstantesSim.TP_SCAN_LMS.equals(tpScan)) {
            // Gerar mensagem no banco ...
            if (!itemsIntegranteEqOperac.hasItems()) {
                throw new BusinessException(ConsErro.EQP_PRECISA_UM_FUNC_OU_TERCEIRO);
            } else {
                iterarItemsIntegranteEqOperac(equipeOperacao, itemsIntegranteEqOperac, itemsIntegranteEqOperacConfig);
            }

            // Caso não seja marcado 'Sem lacre' e o usuário não tenha inserido um lacre, joga exceção
            semLacre = parameters.getBoolean("semLacre");
            obSemLacre = parameters.getString("obSemLacre");

            if (!semLacre) {
                if (!itemsLacreControleCarga.hasItems()) {
                    throw new BusinessException(ConsErro.INSIRA_AO_MENOS_UM_LACRE_OU_MARQUE_S_LACRE);
                }
            } else {
                if ("".equals(obSemLacre)) {
                    throw new BusinessException(ConsErro.INSIRA_OBSERVACAO_CAMPO_S_LACRE);
                }
            }
        }
        boolean rollbackMasterId = false;
        if (ConstantesSim.TP_SCAN_LMS.equals(tpScan)) {
            rollbackMasterId = equipeOperacao.getIdEquipeOperacao() == null;
        }
        try {

            Long idControleCarga = parameters.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
            Long idBox = parameters.getLong(ConsBox.BOX_ID_BOX);
            Long idFilial = parameters.getLong(ConsFilial.ID_FILIAL);
            Long idEquipe = parameters.getLong(ConsEquipe.EQUIPE_ID_EQUIPE);

            // Busca objetos para a sessao do hibernate...
            ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

            Box box = null;
            if (idBox != null) {
                box = boxService.findById(idBox);
            }

            Filial filialUsuario = filialService.findById(idFilial);

            if (parameters.get(ConsCarregamentoDescarga.NR_QUILOMETRAGEM) != null && !"".equals((parameters.get(ConsCarregamentoDescarga.NR_QUILOMETRAGEM)).toString())) {
                Boolean blVirouHodometro = parameters.getBoolean(ConsCarregamentoDescarga.BL_VIROU_HODOMETRO);
                Integer nrQuilometragem = parameters.getInteger(ConsCarregamentoDescarga.NR_QUILOMETRAGEM);
                String obControleQuilometragem = null;
                if (parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM) != null && !"".equals(parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM))) {
                    obControleQuilometragem = parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM);
                }
                Long idMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();

                this.storeInformarQuilometragem(idControleCarga, idFilial, blVirouHodometro, nrQuilometragem, obControleQuilometragem, idMeioTransporte);
            }

            CarregamentoDescarga carregamentoDescarga = this.storeCarregDescIniciarDescarga(dataHora, obSemLacre, idControleCarga, idFilial,
                    controleCarga, box, filialUsuario);

            if (ConstantesSim.TP_SCAN_LMS.equals(tpScan)) {
                Equipe equipe = equipeService.findById(idEquipe);
                // Gerando uma nova equipe operacao e os relacionamentos...
                equipeOperacao.setDhInicioOperacao(dataHora);
                equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
                equipeOperacao.setControleCarga(controleCarga);
                equipeOperacao.setEquipe(equipe);
                // Persistencia da DF2
                equipeOperacaoService.storeEquipeOperacao(equipeOperacao, itemsIntegranteEqOperac);
            } else if (ConstantesSim.TP_SCAN_FISICO.equals(tpScan)) {
                equipeOperacao = equipeOperacaoService.storeEquipeDescarga(controleCarga, carregamentoDescarga);
            }

            // Altera o status do controle de carga e gera um evento para o controle de carga em questão.
            controleCargaService.generateEventoChangeStatusControleCarga(
                    controleCarga.getIdControleCarga(),
                    idFilial,
                    ConstantesExpedicao.TP_EVENTO_CONTROLE_CARGA_INICIO_DESCARGA,
                    dataHora,
                    controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                    configuracoesFacade.getMensagem(ConsCarregamentoDescarga.INICIO_DE_DESCARGA),
                    null, null, null, null, null, null, null, null,
                    equipeOperacao
            );

            //Pega todos os lacres do controle de carga em questão com o status de 'FE' (fechado)
            this.updateLacresFechadosParaNaoEncontrados(idControleCarga);

            // Seta o controle de carga para os lacres e grava os registros
            if (!semLacre && itemsLacreControleCarga != null && ConstantesSim.TP_SCAN_LMS.equals(tpScan)) {
                lacreControleCargaList = new ArrayList<LacreControleCarga>();
                for (Iterator<LacreControleCarga> iter2 = itemsLacreControleCarga.iterator(null, itemsLacreControleCargaConfig); iter2.hasNext(); ) {
                    lacreControleCargaList.add((LacreControleCarga) iter2.next());
                }
            }

            // Seta o controle de carga para os lacres e grava os registros
            if (!semLacre) {
                this.storeLacreControleCargaNovo(lacreControleCargaList, controleCarga, dataHora);
            }

            if (ConstantesSim.TP_SCAN_LMS.equals(tpScan)) {
                // Seta o carregamento descarga para as fotos e grava os registros
                for (Iterator<FotoCarregmtoDescarga> iter3 = itemsFotoCarregmtoDescarga.iterator(null, itemsFotoCarregmtoDescargaConfig); iter3.hasNext(); ) {
                    FotoCarregmtoDescarga fotoCarregmtoDescarga = (FotoCarregmtoDescarga) iter3.next();
                    fotoCarregmtoDescarga.setIdFotoCarregmtoDescarga(null);
                    fotoCarregmtoDescarga.setCarregamentoDescarga(carregamentoDescarga);
                    fotoService.store(fotoCarregmtoDescarga.getFoto());
                }
                fotoCarregmtoDescargaService.storeFotosCarregmtoDescarga(itemsFotoCarregmtoDescarga.getNewOrModifiedItems());
            }

            // Altera o status do controle de carga e gera um evento para o controle de carga em questão.
            // Atualiza o status de todos os manifestos do controle de carga e grava seu evento de manifesto.
            if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM)) {
                this.generateEventoInicioDescargaViagem(equipeOperacao, dataHora, idControleCarga, idFilial, filialUsuario, carregamentoDescarga);
            } else if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA)) {
                this.generateEventoInicioDescargaColetaEntrega(equipeOperacao, dataHora, tpScan, idControleCarga, controleCarga, filialUsuario, carregamentoDescarga);
            }

            // Gerando novo evento de meio de transporte para ID Transportado...
            if (controleCarga.getMeioTransporteByIdTransportado() != null) {
                this.generateEventoMeioTransporte(controleCarga.getMeioTransporteByIdTransportado(), dataHora, controleCarga, box, filialUsuario);
            }

            // Gerando novo evento de meio de transporte para ID Semi-Rebocado...
            if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                this.generateEventoMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado(), dataHora, controleCarga, box, filialUsuario);
            }


        } catch (RuntimeException runex) {
            //Usado quando o MWW ainda não chegou a associar uma equipe.
            if (equipeOperacao != null || itemsIntegranteEqOperac != null) {

                this.rollbackMasterState(equipeOperacao, rollbackMasterId, runex);

                if (itemsIntegranteEqOperac != null) {
                    itemsIntegranteEqOperac.rollbackItemsState();
                }
            }
            throw runex;
        }

        return new CarregamentoDescarga();
    }

    private void iterarItemsIntegranteEqOperac(EquipeOperacao equipeOperacao, ItemList itemsIntegranteEqOperac, ItemListConfig itemsIntegranteEqOperacConfig) {
        for (Iterator<IntegranteEqOperac> iter = itemsIntegranteEqOperac.iterator(equipeOperacao.getIdEquipeOperacao(), itemsIntegranteEqOperacConfig); iter.hasNext(); ) {
            IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
            integranteEqOperac.setEquipeOperacao(equipeOperacao);

            //Remove a instancia setada na action para visualizacao apenas...
            if ("F".equals(integranteEqOperac.getTpIntegrante().getValue())) {
                integranteEqOperac.setCargoOperacional(null);
            }
        }
    }

    @Deprecated
    public java.io.Serializable storeInicioDescargaNovo(TypedFlatMap parameters,
                                                        DateTime dataHora,
                                                        List<LacreControleCarga> lacreControleCargaList) {

        Long idControleCarga = parameters.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
        Long idBox = parameters.getLong(ConsBox.BOX_ID_BOX);
        Long idFilial = parameters.getLong(ConsFilial.ID_FILIAL);

        // Busca objetos para a sessao do hibernate...
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        Box box = null;
        if (idBox != null) {
            box = boxService.findById(idBox);
        }

        Filial filialUsuario = filialService.findById(idFilial);

        if (parameters.get(ConsCarregamentoDescarga.NR_QUILOMETRAGEM) != null && !"".equals((parameters.get(ConsCarregamentoDescarga.NR_QUILOMETRAGEM)).toString())) {
            Boolean blVirouHodometro = parameters.getBoolean(ConsCarregamentoDescarga.BL_VIROU_HODOMETRO);
            Integer nrQuilometragem = parameters.getInteger(ConsCarregamentoDescarga.NR_QUILOMETRAGEM);
            String obControleQuilometragem = null;
            if (parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM) != null && !"".equals(parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM))) {
                obControleQuilometragem = parameters.getString(ConsCarregamentoDescarga.OB_CONTROLE_QUILOMETRAGEM);
            }
            Long idMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();

            this.storeInformarQuilometragem(idControleCarga, idFilial, blVirouHodometro, nrQuilometragem, obControleQuilometragem, idMeioTransporte);
        }

        CarregamentoDescarga carregamentoDescarga = this.storeCarregDescIniciarDescarga(dataHora, "", idControleCarga, idFilial,
                controleCarga, box, filialUsuario);

        EquipeOperacao equipeOperacao = equipeOperacaoService.storeEquipeDescarga(controleCarga, carregamentoDescarga);

        // Altera o status do controle de carga e gera um evento para o controle de carga em questão.
        controleCargaService.generateEventoChangeStatusControleCarga(
                controleCarga.getIdControleCarga(),
                idFilial,
                ConstantesExpedicao.TP_EVENTO_CONTROLE_CARGA_INICIO_DESCARGA,
                dataHora,
                controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                configuracoesFacade.getMensagem(ConsCarregamentoDescarga.INICIO_DE_DESCARGA),
                null, null, null, null, null, null, null, null,
                equipeOperacao
        );

        //Pega todos os lacres do controle de carga em questão com o status de 'FE' (fechado)
        this.updateLacresFechadosParaNaoEncontrados(idControleCarga);

        // Seta o controle de carga para os lacres e grava os registros
        this.storeLacreControleCargaNovo(lacreControleCargaList, controleCarga, dataHora);

        // Altera o status do controle de carga e gera um evento para o controle de carga em questão.
        // Atualiza o status de todos os manifestos do controle de carga e grava seu evento de manifesto.
        if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM)) {
            this.generateEventoInicioDescargaViagem(equipeOperacao, dataHora, idControleCarga, idFilial, filialUsuario, carregamentoDescarga);
        } else if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA)) {
            this.generateEventoInicioDescargaColetaEntrega(equipeOperacao, dataHora, ConstantesSim.TP_SCAN_FISICO, idControleCarga, controleCarga, filialUsuario, carregamentoDescarga);
        }

        // Gerando novo evento de meio de transporte para ID Transportado...
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            this.generateEventoMeioTransporte(controleCarga.getMeioTransporteByIdTransportado(), dataHora, controleCarga, box, filialUsuario);
        }

        // Gerando novo evento de meio de transporte para ID Semi-Rebocado...
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            this.generateEventoMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado(), dataHora, controleCarga, box, filialUsuario);
        }

        return new CarregamentoDescarga();
    }

    private void updateLacresFechadosParaNaoEncontrados(Long idControleCarga) {
        List<Map<String, Object>> listLacreControleCargaFechados = lacreControleCargaService.findLacresFechadosByIdControleCarga(idControleCarga);

        List<LacreControleCarga> listLacresFechadosParaSalvar = new ArrayList<LacreControleCarga>(listLacreControleCargaFechados.size());
        for (Map<String, Object> mapLacreControleCarga : listLacreControleCargaFechados) {
            LacreControleCarga lacreControleCarga = lacreControleCargaService.findById((Long) mapLacreControleCarga.get("idLacre"));
            lacreControleCarga.setTpStatusLacre(new DomainValue("NE"));
            lacreControleCarga.setFilialByIdFilialAlteraStatus(SessionUtils.getFilialSessao());
            lacreControleCarga.setUsuarioByIdFuncAlteraStatus(SessionUtils.getUsuarioLogado());
            listLacresFechadosParaSalvar.add(lacreControleCarga);
        }
        lacreControleCargaService.storeLacresControleCarga(listLacresFechadosParaSalvar);
    }

    private void generateEventoMeioTransporte(MeioTransporte meioTransporte, DateTime dataHora, ControleCarga controleCarga, Box box, Filial filialUsuario) {
        EventoMeioTransporte eventoMeioTransporteTransportado = new EventoMeioTransporte();
        eventoMeioTransporteTransportado.setTpSituacaoMeioTransporte(new DomainValue("EMDE"));
        eventoMeioTransporteTransportado.setDhInicioEvento(dataHora);
        eventoMeioTransporteTransportado.setBox(box);
        eventoMeioTransporteTransportado.setControleCarga(controleCarga);
        eventoMeioTransporteTransportado.setFilial(filialUsuario);
        eventoMeioTransporteTransportado.setMeioTransporte(meioTransporte);
        eventoMeioTransporteTransportado.setUsuario(SessionUtils.getUsuarioLogado());
        eventoMeioTransporteService.generateEvent(eventoMeioTransporteTransportado, dataHora);
    }

    private CarregamentoDescarga storeCarregDescIniciarDescarga(DateTime dataHora, String obSemLacre, Long idControleCarga, Long idFilial,
                                                                ControleCarga controleCarga, Box box, Filial filialUsuario) {
        // Verifica se já existe um registro de carregamento descarga
        CarregamentoDescarga carregamentoDescarga = null;
        boolean existeCarregamentoDescarga = false;
        List<CarregamentoDescarga> listCarregamentoDescarga = this.findCarregamentoDescargaByIdFilialByIdControleCarga(idFilial, idControleCarga);

        if (listCarregamentoDescarga != null) {
            for (int i = 0; i < listCarregamentoDescarga.size(); i++) {
                CarregamentoDescarga carregamentoDescargatemp = (CarregamentoDescarga) listCarregamentoDescarga.get(i);
                if ("D".equals(carregamentoDescargatemp.getTpOperacao().getValue()) &&
                        ("P".equals(carregamentoDescargatemp.getTpStatusOperacao().getValue()) || "C".equals(carregamentoDescargatemp.getTpStatusOperacao().getValue()))) {
                    carregamentoDescarga = carregamentoDescargatemp;
                    // Setando dados para inicio de carregamentoDescarga...
                    carregamentoDescarga.setDhInicioOperacao(dataHora);
                    carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
                    carregamentoDescarga.setObSemLacre(obSemLacre);
                    // Persiste os objetos...
                    store(carregamentoDescarga);
                    existeCarregamentoDescarga = true;
                    break;
                }
            }
        }

        // Verifica se não existe um registro para gerar um novo carregamentoDescarga
        if (listCarregamentoDescarga == null || listCarregamentoDescarga.isEmpty() || !existeCarregamentoDescarga) {
            // Gerando um novo carregamentoDescarga...
            carregamentoDescarga = new CarregamentoDescarga();
            carregamentoDescarga.setTpOperacao(new DomainValue("D"));
            carregamentoDescarga.setControleCarga(controleCarga);
            carregamentoDescarga.setFilial(filialUsuario);
            // Setando dados para inicio de carregamentoDescarga...
            carregamentoDescarga.setDhInicioOperacao(dataHora);
            carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
            carregamentoDescarga.setObSemLacre(obSemLacre);
            carregamentoDescarga.setUsuarioByIdUsuarioIniciado(SessionUtils.getUsuarioLogado());
            if (box != null) {
                boxService.storeOcuparBox(box);
            }
            carregamentoDescarga.setBox(box);
            // Persiste os objetos...
            store(carregamentoDescarga);
        }
        return carregamentoDescarga;
    }

    private void storeInformarQuilometragem(Long idControleCarga, Long idFilial, Boolean blVirouHodometro, Integer nrQuilometragem, String obControleQuilometragem, Long idMeioTransporte) {
        //Valida se já existe lançamento de registro de quilometragem
        boolean isExisteLancamentoQuilometragem = controleCargaService.findLancamentoQuilometragem(idControleCarga, idFilial, Boolean.FALSE);

        //Caso não exista lançamentos de quilometragem, deverá realizar o processo para informar a quilometragem
        if (!isExisteLancamentoQuilometragem) {
            //Rotina Informar Quilometragem Meio Transporte
            controleQuilometragemService.storeInformarQuilometragemMeioTransporte(idFilial, Boolean.FALSE, idMeioTransporte, Boolean.FALSE, nrQuilometragem,
                    blVirouHodometro, idControleCarga, null, obControleQuilometragem);
        }
    }

    private void generateEventoInicioDescargaColetaEntrega(
            EquipeOperacao equipeOperacao, DateTime dataHora, String tpScan,
            Long idControleCarga, ControleCarga controleCarga,
            Filial filialUsuario, CarregamentoDescarga carregamentoDescarga) {
        // Pega uma lista de manifestos para o controle de carga
        List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (Manifesto manifesto : listManifesto) {
            List<ManifestoEntrega> listManifestoEntrega = manifestoEntregaService.findManifestoEntregaByIdManifesto(manifesto.getIdManifesto());
            if (!listManifestoEntrega.isEmpty() &&
                    !ConsManifesto.VL_STATUS_MANIFESTO_FECHADO.equals(manifesto.getTpStatusManifesto().getValue()) && !"CA".equals(manifesto.getTpStatusManifesto().getValue())) {
                manifesto.setTpStatusManifesto(new DomainValue(ConsManifesto.VL_EM_DESCARGA));
                manifestoService.storeBasic(manifesto);
                this.storeEventosEDescargaDeManifestos("ID", equipeOperacao, carregamentoDescarga, manifesto, null, filialUsuario, dataHora);
                this.generateEventoDoctoServicoByIdManifestoIfNotOcorrenciaEntrega(manifesto, ConstantesSim.EVENTO_INICIO_DESCARGA_ENTREGA, dataHora);
                this.generateEventoVolumeToColetaEntrega(manifesto, controleCarga.getIdControleCarga(), ConstantesSim.EVENTO_INICIO_DESCARGA_ENTREGA, carregamentoDescarga.getIdCarregamentoDescarga(), tpScan);
            }
        }

        // Pega uma lista de manifestos de coleta para o controle de carga
        List<ManifestoColeta> listManifestoColeta = manifestoColetaService.findManifestoColetaByIdControleCarga(idControleCarga);
        for (ManifestoColeta manifestoColeta : listManifestoColeta) {
            if (!ConsManifesto.VL_STATUS_MANIFESTO_FECHADO.equals(manifestoColeta.getTpStatusManifestoColeta().getValue()) && !"CA".equals(manifestoColeta.getTpStatusManifestoColeta().getValue())) {
                manifestoColeta.setTpStatusManifestoColeta(new DomainValue(ConsManifesto.VL_EM_DESCARGA));

                // Pega uma lista de pedidos de coleta para o manifesto de coleta
                List<PedidoColeta> listPedidoColeta = pedidoColetaService.findPedidoColetaByIdManifestoColeta(manifestoColeta.getIdManifestoColeta());
                for (PedidoColeta pedidoColeta : listPedidoColeta) {

                    // se tem evento de coleta EX, então prossegue
                    if (eventoColetaService.validateEventoColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta(), "EX")) {
                        if ("AD".equals(pedidoColeta.getTpStatusColeta().getValue())) {
                            pedidoColeta.setTpStatusColeta(new DomainValue(ConsManifesto.VL_EM_DESCARGA));
                            pedidoColetaService.store(pedidoColeta);
                        }

                        // Gerando o evento de coleta...
                        EventoColeta eventoColeta = new EventoColeta();
                        eventoColeta.setDhEvento(dataHora);
                        eventoColeta.setTpEventoColeta(new DomainValue("ID"));
                        eventoColeta.setPedidoColeta(pedidoColeta);
                        OcorrenciaColeta ocorrenciaColeta = (OcorrenciaColeta) ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta("ID").get(0);
                        eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                        eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                        // Grava o evento de coleta...
                        eventoColetaService.store(eventoColeta);
                        // grava no topic de eventos de pedido coleta
                        eventoColetaService.storeMessageTopic(eventoColeta);

                        pedidoColetaService.generateEventosColetaAeroByPedidoColeta(pedidoColeta, ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM, ConstantesSim.CD_MERCADORIA_AGUARDANDO_DESCARGA);
                    }
                }

                manifestoColetaService.store(manifestoColeta);
                this.storeEventosEDescargaDeManifestos("ID", equipeOperacao, carregamentoDescarga, null, manifestoColeta, null, dataHora);
            }
        }
    }

    private void generateEventoInicioDescargaViagem(EquipeOperacao equipeOperacao, DateTime dataHora, Long idControleCarga, Long idFilial, Filial filialUsuario, CarregamentoDescarga carregamentoDescarga) {
        // Pega uma lista de manifestos para o controle de carga (TpStatusManifesto not in "FE", "CA")
        List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCargaAndNotInTpStatusManifesto(idControleCarga, Arrays.asList(new String[]{ConsManifesto.VL_STATUS_MANIFESTO_FECHADO, "CA"}));
        for (Manifesto manifesto : listManifesto) {
            if (manifesto.getFilialByIdFilialDestino().getIdFilial().equals(idFilial)) {
                manifesto.setTpStatusManifesto(new DomainValue(ConsManifesto.VL_EM_DESCARGA));
                manifestoService.storeBasic(manifesto);

                this.generateEventoDocumentoServicoByIdManifesto(
                        manifesto.getIdManifesto(),
                        ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM,
                        "MAV",
                        dataHora);

                this.storeEventoInicioDescargaViagemVolumes(manifesto.getIdManifesto());

                this.storeEventoInicioDescargaDispUitizacao(manifesto.getIdManifesto());

                this.storeEventosEDescargaDeManifestos("ID",
                        equipeOperacao,
                        carregamentoDescarga,
                        manifesto, null,
                        filialUsuario,
                        dataHora);
            } else {
                manifesto.setTpStatusManifesto(new DomainValue("EF"));
                manifestoService.storeBasic(manifesto);
            }
        }
    }

    private void storeEventoInicioDescargaDispUitizacao(Long idManifesto) {
        List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacaoService.findByIdManifesto(idManifesto);
        eventoDispositivoUnitizacaoService.generateEventoDispositivo(dispositivos, ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM, ConstantesSim.TP_SCAN_LMS);
    }

    private void storeEventoInicioDescargaViagemVolumes(Long idManifesto) {
        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.VNF_LOCALIZACAO_MERCADORIA, "loc");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.ne(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA, ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA));
        final List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdManifesto(idManifesto, null, alias, criterion);

        eventoVolumeService.newEventoVolumeGenerator()
                .cdEvento(ConstantesSim.EVENTO_INICIO_DESCARGA_VIAGEM)
                .generate(volumes)
                .storeAll();
    }

    /**
     * Método criado para que o MWW possa utilizar o método storeInicioDescarga
     *
     * @param lacreControleCargaList
     * @param controleCarga
     * @param dataHora
     */
    private void storeLacreControleCargaNovo(List<LacreControleCarga> lacreControleCargaList,
                                             ControleCarga controleCarga,
                                             DateTime dataHora) {

        for (LacreControleCarga lacreControleCarga : lacreControleCargaList) {

            //Busca o Lacre e muda alguns atributos.
            LacreControleCarga lacreControleCarga2 =
                    lacreControleCargaService.
                            findLacreControleCargaByIdControleCargaAndNrLacre(controleCarga.getIdControleCarga(), lacreControleCarga.getNrLacres());

            if (lacreControleCarga2 != null) {
                lacreControleCarga2.setTpStatusLacre(lacreControleCarga.getTpStatusLacre());
                lacreControleCarga2.setObConferenciaLacre(lacreControleCarga.getObConferenciaLacre());
                lacreControleCarga2.setFilialByIdFilialAlteraStatus(SessionUtils.getFilialSessao());
                lacreControleCarga2.setUsuarioByIdFuncAlteraStatus(SessionUtils.getUsuarioLogado());
                lacreControleCarga2.setDhAlteracao(dataHora);

                lacreControleCargaService.store(lacreControleCarga2);
            } else {
                lacreControleCarga.setControleCarga(controleCarga);
                lacreControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
                lacreControleCarga.setFilialByIdFilialInclusao(SessionUtils.getFilialSessao());
                lacreControleCarga.setUsuarioByIdFuncInclusao(SessionUtils.getUsuarioLogado());

                //“CA” (Conferido e Aberto) colocar status “NC” (Número não confere)
                if ("CA".equals(lacreControleCarga.getTpStatusLacre().getValue())) {
                    lacreControleCarga.setTpStatusLacre(new DomainValue("NC"));
                }
                lacreControleCarga.setDhInclusao(dataHora);
                lacreControleCarga.setDhAlteracao(null);

                lacreControleCargaService.store(lacreControleCarga);
            }
        }
    }

    /**
     * Gerar o registro de fim de descarga, inserindo tambem os dados das telas de dispositivo
     * sem identificacao e com identificacao.
     *
     * @param parameters
     * @return
     */
    public TypedFlatMap storeFimDescarga(
            TypedFlatMap parameters
    ) {

        AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations = new AdsmNativeBatchSqlOperations(this.getDao(), ConsGeral.LIMITE_OPERACAO);

        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
        TypedFlatMap retorno = new TypedFlatMap();

        Long idControleCarga = parameters.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
        Long idCarregamentoDescarga = parameters.getLong(ConsCarregamentoDescarga.ID_CARREGAMENTO_DESCARGA);
        Long idFilial = parameters.getLong(ConsFilial.ID_FILIAL);
        String obOperacao = parameters.getString(ConsCarregamentoDescarga.OB_OPERACAO);

        try {

            //Está sendo usado
            List<Manifesto> manifestos = manifestoService.findManifestoByIdControleCargaByFilialDestino(idControleCarga, SessionUtils.getFilialSessao().getIdFilial());
            if (!manifestos.isEmpty()) {
                throw new BusinessException(ConsErro.DESCARGA_NAO_FIN_EXIST_MAN_DESC);
            }

            Filial filialUsuario = filialService.findById(idFilial);

            EquipeOperacao equipeOperacao = this.findUpdateEquipeOperacao(dataHora, idCarregamentoDescarga); //Usado pelo findUpdateControleCarga
            CarregamentoDescarga carregamentoDescarga = this.findUpdateCarregamentoDescarga(dataHora, idCarregamentoDescarga, obOperacao);
            ControleCarga controleCarga = this.findUpdateControleCarga(dataHora, idControleCarga, filialUsuario, equipeOperacao, adsmNativeBatchSqlOperations);

            // LMS-2703 - Valida Work-flow descarga veiculo
            this.storeAndValidateDescargaVeiculo(controleCarga, EventoDescargaVeiculo.FINALIZAR_DESCARGA, adsmNativeBatchSqlOperations);

            liberarBoxFimDescarga(adsmNativeBatchSqlOperations, carregamentoDescarga);

            this.encerraRNCsAptas(controleCarga, adsmNativeBatchSqlOperations);

            retorno = executeFinalizarDescarga(dataHora, retorno, filialUsuario, equipeOperacao, carregamentoDescarga, controleCarga, adsmNativeBatchSqlOperations);

            if (controleCarga.getMeioTransporteByIdTransportado() != null) {
                this.generateEventoMeioTransporte(dataHora, controleCarga, controleCarga.getMeioTransporteByIdTransportado());
            }

            if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                this.generateEventoMeioTransporte(dataHora, controleCarga, controleCarga.getMeioTransporteByIdSemiRebocado());
            }

        } catch (RuntimeException runex) {
            LOG.error("Erro storeFimDescarga:", runex);
            throw runex;
        }

        ciotService.executeAlteracaoCIOT(idControleCarga);

        return retorno;
    }

    private void liberarBoxFimDescarga(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, CarregamentoDescarga carregamentoDescarga) {
        if (carregamentoDescarga.getBox() != null) {
            if (adsmNativeBatchSqlOperations != null) {
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                        ConsBox.TN_BOX,
                        ConsBox.SQL_TP_SITUACAO_BOX,
                        ConsBox.DV_TP_BOX_LIVRE
                );
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(
                        ConsBox.TN_BOX, carregamentoDescarga.getBox().getIdBox()
                );
            } else {
                boxService.storeDesocuparBox(carregamentoDescarga.getBox());
            }
        }
    }

    private TypedFlatMap executeFinalizarDescarga(DateTime dataHora, TypedFlatMap retorno, Filial filialUsuario,
                                                  EquipeOperacao equipeOperacao, CarregamentoDescarga carregamentoDescarga, ControleCarga controleCarga,
                                                  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        // Atualiza o status de todos os manifestos do controle de carga e
        // grava seu evento de manifesto.
        String tpControleCarga = controleCarga.getTpControleCarga().getValue();

        if (ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM.equals(tpControleCarga)) {
            return finalizarDescargaViagem(dataHora, retorno, controleCarga, carregamentoDescarga, filialUsuario, equipeOperacao, adsmNativeBatchSqlOperations);
        } else if (ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA.equals(tpControleCarga)) {
            this.finalizarDescargaEntrega(dataHora, controleCarga, carregamentoDescarga, filialUsuario, equipeOperacao, adsmNativeBatchSqlOperations);
        }

        return retorno;
    }

    private void encerraRNCsAptas(ControleCarga controleCarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        /** OTIMIZADO */
        // LMS-4460 - Baixa automática de RNC
        // Busca as RNCs aptas para o encerramento
        List<OcorrenciaNaoConformidade> listaOcoRNCsAptasEncerramento = ocorrenciaNaoConformidadeService
                .buscaRNCsAptasEncerramento(controleCarga.getIdControleCarga(), new String[]{ConstantesSim.TP_SCAN_FISICO,
                        ConstantesSim.TP_DESCARGA_SORTER});

        if (listaOcoRNCsAptasEncerramento != null && !listaOcoRNCsAptasEncerramento.isEmpty()) {
            // Realiza o fechamento das Ocorrencias
            ocorrenciaNaoConformidadeService.fechaOcoRNC(listaOcoRNCsAptasEncerramento, controleCarga.getIdControleCarga(),
                    adsmNativeBatchSqlOperations);
        }
    }

    private ControleCarga findUpdateControleCarga(DateTime dataHora,
                                                  Long idControleCarga, Filial filialUsuario,
                                                  EquipeOperacao equipeOperacao,
                                                  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
        //Otimizado
        // Altera o status do controle de carga e gera um evento para o controle de carga em questão.
        controleCargaService.generateEventoChangeStatusControleCarga(
                controleCarga,
                filialUsuario,
                ConsManifesto.VL_FIM_DESCARGA,
                dataHora,
                controleCarga.getMeioTransporteByIdTransportado(),
                configuracoesFacade.getMensagem("fimDeDescarga"),
                null, null, null, null, null, null, null, null,
                equipeOperacao, true, adsmNativeBatchSqlOperations, false
        );

        getDao().getAdsmHibernateTemplate().evict(controleCarga);

        return controleCarga;
    }

    private CarregamentoDescarga findUpdateCarregamentoDescarga(
            DateTime dataHora, Long idCarregamentoDescarga, String obOperacao) {
        CarregamentoDescarga carregamentoDescarga = this.findById(idCarregamentoDescarga);
        // Setando dados para fim de carregamentoDescarga...
        carregamentoDescarga.setDhFimOperacao(dataHora);
        carregamentoDescarga.setTpStatusOperacao(new DomainValue("F"));
        carregamentoDescarga.setObOperacao(obOperacao);
        carregamentoDescarga.setUsuarioByIdUsuarioFinalizado(SessionUtils.getUsuarioLogado());
        // Persiste os objetos...
        store(carregamentoDescarga);
        return carregamentoDescarga;
    }

    private EquipeOperacao findUpdateEquipeOperacao(DateTime dataHora, Long idCarregamentoDescarga) {
        /** OTIMIZADO */
        // Busca Equipe Operação do CarregamentoDescarga em questão que possui dhFimOperacao NULO.
        List<EquipeOperacao> equipes = equipeOperacaoService.findEquipeOperacaoByIdCarregamentoDescarga(idCarregamentoDescarga, false, "D");

        EquipeOperacao equipeOperacao = null;

        for (EquipeOperacao equipe : equipes) {
            equipeOperacao = equipe;
            if (equipe.getDhFimOperacao() == null) {
                break;
            }
        }

        equipeOperacao.setDhFimOperacao(dataHora);
        equipeOperacaoService.store(equipeOperacao);
        return equipeOperacao;
    }

    public void generateEventoMeioTransporte(DateTime dataHora, ControleCarga controleCarga, MeioTransporte meioTransporte) {
        //Regra 1.4 da ET 03.01.01.09 - Informar Fim Descarga
        EventoMeioTransporte eventoEMDE = eventoMeioTransporteService.findLastEventoMeioTransporteToMeioTransporte(
                meioTransporte.getIdMeioTransporte(),
                SessionUtils.getFilialSessao().getIdFilial(),
                controleCarga.getIdControleCarga(),
                "EMDE");
        if (eventoEMDE == null) {
            eventoEMDE = new EventoMeioTransporte();
            eventoEMDE.setMeioTransporte(meioTransporte);
            eventoEMDE.setTpSituacaoMeioTransporte(new DomainValue("EMDE"));
            eventoEMDE.setDhInicioEvento(dataHora);
            eventoEMDE.setFilial(SessionUtils.getFilialSessao());
            eventoEMDE.setUsuario(SessionUtils.getUsuarioLogado());
            eventoMeioTransporteService.generateEvent(eventoEMDE);
        }


        //Regra 1.6 da ET 03.01.01.09 - Informar Fim Descarga
        EventoMeioTransporte evento = new EventoMeioTransporte();
        evento.setControleCarga(controleCarga);
        evento.setMeioTransporte(meioTransporte);
        evento.setDhInicioEvento(dataHora);
        evento.setFilial(SessionUtils.getFilialSessao());
        evento.setUsuario(SessionUtils.getUsuarioLogado());
        if ("C".equals(controleCarga.getTpControleCarga().getValue())) {
            evento.setTpSituacaoMeioTransporte(new DomainValue("ADFR"));
        } else if (SessionUtils.getFilialSessao().getIdFilial().equals(controleCarga.getFilialByIdFilialDestino().getIdFilial())) {
            evento.setTpSituacaoMeioTransporte(new DomainValue("ADFR"));
        } else {
            evento.setTpSituacaoMeioTransporte(new DomainValue("PAOP"));
        }
        eventoMeioTransporteService.generateEvent(evento, dataHora);
    }

    public void finalizarDescargaEntrega(DateTime dataHora, ControleCarga controleCarga,
                                         CarregamentoDescarga carregamentoDescarga, Filial filialUsuario,
                                         EquipeOperacao equipeOperacao, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        // Pega uma lista de manifestos para o controle de carga...

        List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCargaAndNotInTpStatusManifesto(controleCarga.getIdControleCarga(),
                Arrays.asList(ConsManifesto.VL_STATUS_MANIFESTO_FECHADO, "CA"));

        for (Manifesto manifesto : listManifesto) {

            List<ManifestoEntrega> listManifestoEntrega = manifestoEntregaService.findManifestoEntregaByIdManifesto(manifesto.getIdManifesto());

            if (listManifestoEntrega.isEmpty()) {
                continue;
            }

            StringBuilder tpOcorrencias = new StringBuilder();
            tpOcorrencias.append("'"+ConstantesEntrega.TP_OCORRENCIA_ENTREGUE+"'");
            tpOcorrencias.append(", ");
            tpOcorrencias.append("'"+ConstantesEntrega.TP_OCORRENCIA_ENTREGUE_AEROPORTO+"'");

            final List<DoctoServico> listDoctoServico = doctoServicoService.findDocumentosByTpOcorrenciasByIdManifesto(manifesto.getIdManifesto(), tpOcorrencias.toString());
            final List<String> doctoServicoAliasList = new ArrayList<String>();
            final List<String> volumeNotaFiscalAliasList = new ArrayList<String>();

            for (DoctoServico doctoServico : listDoctoServico) {
                generateEventoDoctoServico(dataHora, carregamentoDescarga, manifesto, doctoServico, adsmNativeBatchSqlOperations);
                updateDoctoServicoAndLocalizacaoMercadoriaByDoctoServico(manifesto, doctoServico, adsmNativeBatchSqlOperations,
                        doctoServicoAliasList, volumeNotaFiscalAliasList);
            }

            for (String volumeNotaFiscalAlias : volumeNotaFiscalAliasList) {
                adsmNativeBatchSqlOperations.runSingleUpdateForTable(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL, volumeNotaFiscalAlias);
            }

            for (String doctoServicoAlias : doctoServicoAliasList) {
                adsmNativeBatchSqlOperations.runSingleUpdateForTable(ConsDoctoServico.TN_DOCTO_SERVICO, doctoServicoAlias);
            }

            this.generateEventoVolumeFimDescargaEntrega(manifesto.getIdManifesto(), controleCarga.getIdControleCarga(),
                    carregamentoDescarga.getIdCarregamentoDescarga(), adsmNativeBatchSqlOperations);

            updateManifestoFinalizarDescargaEntrega(adsmNativeBatchSqlOperations, manifesto);

            Map<String, Object> storeEventosEDescargaDeManifestosValues = new HashMap<String, Object>();
            storeEventosEDescargaDeManifestosValues.put(TP_EVENTO_MANIFESTO, ConsManifesto.VL_FIM_DESCARGA);
            storeEventosEDescargaDeManifestosValues.put(EQUIPE_OPERACAO, equipeOperacao);
            storeEventosEDescargaDeManifestosValues.put(CARREGAMENTO_DESCARGA, carregamentoDescarga);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO, manifesto);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO_COLETA, null);
            storeEventosEDescargaDeManifestosValues.put(FILIAL_USUARIO, filialUsuario);
            storeEventosEDescargaDeManifestosValues.put(DATA_HORA, dataHora);
            storeEventosEDescargaDeManifestos(storeEventosEDescargaDeManifestosValues, adsmNativeBatchSqlOperations);

            this.storeCarregamentoDescargaVolumesEntrega(controleCarga, carregamentoDescarga, listManifestoEntrega, adsmNativeBatchSqlOperations);
        }

        List<PedidoColeta> pedidoColetasAero = new ArrayList<PedidoColeta>();

        this.updateManifestosColetaAndGenarateEventos(dataHora, controleCarga, carregamentoDescarga, equipeOperacao, pedidoColetasAero, adsmNativeBatchSqlOperations);

        adsmNativeBatchSqlOperations.runAllCommands();

        gerarNotaCreditoService.execute(controleCarga, false);

        for (PedidoColeta pedidoColeta : pedidoColetasAero) {
            pedidoColetaService.generateEventosColetaAeroByPedidoColetaWithLazyLoading(pedidoColeta, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, ConstantesSim.CD_MERCADORIA_EM_DESCARGA);
        }
    }

    private void updateManifestoFinalizarDescargaEntrega(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Manifesto manifesto) {
        String alias = "mn" + ConsManifesto.VL_STATUS_DESCARGA_CONCLUIDA;
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsManifesto.TN_MANIFESTO, alias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    ConsManifesto.TN_MANIFESTO,
                    ConsManifesto.SQL_TP_STATUS_MANIFESTO,
                    ConsManifesto.VL_STATUS_DESCARGA_CONCLUIDA,
                    alias);
        }
        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsManifesto.TN_MANIFESTO, manifesto.getIdManifesto(), alias);
        manifesto.setTpStatusManifesto(new DomainValue(ConsManifesto.VL_STATUS_DESCARGA_CONCLUIDA));
        getDao().getAdsmHibernateTemplate().evict(manifesto);
    }


    private void updateDoctoServicoAndLocalizacaoMercadoriaByDoctoServico(Manifesto manifesto, DoctoServico doctoServico,
                                                                          AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                                          List<String> doctoServicoAliasList, List<String> volumeNotaFiscalAliasList) {
        //LMS-5056
        if (doctoServico.getBlBloqueado() != null && doctoServico.getBlBloqueado()) {
            OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());

            if (ocorrenciaDoctoServico != null && ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio() != null
                    && ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getEvento() != null
                    && ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getEvento().getLocalizacaoMercadoria() != null) {

                ManifestoEntregaDocumento manifestoEntregaDocumento = manifestoEntregaDocumentoService.findManifestoEntregaDocumento(
                        manifesto.getIdManifesto(), doctoServico.getIdDoctoServico());

                checkAndAddDoctoServicoLocalizacaoMercadoria(doctoServico, adsmNativeBatchSqlOperations, doctoServicoAliasList,
                        volumeNotaFiscalAliasList, ocorrenciaDoctoServico, manifestoEntregaDocumento);
            }
        }
    }

    private void checkAndAddDoctoServicoLocalizacaoMercadoria(DoctoServico doctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, List<String> doctoServicoAliasList,
                                                              List<String> volumeNotaFiscalAliasList, OcorrenciaDoctoServico ocorrenciaDoctoServico, ManifestoEntregaDocumento manifestoEntregaDocumento) {
        if (manifestoEntregaDocumento != null
                && manifestoEntregaDocumento.getOcorrenciaEntrega() != null
                && manifestoEntregaDocumento.getOcorrenciaEntrega().getOcorrenciaPendencia() != null
                && manifestoEntregaDocumento.getOcorrenciaEntrega().getOcorrenciaPendencia().getIdOcorrenciaPendencia()
                .equals(ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getIdOcorrenciaPendencia())) {

            addDoctoServicoLocalizacaoMercadoriaBatchUpdate(doctoServico, adsmNativeBatchSqlOperations, doctoServicoAliasList, volumeNotaFiscalAliasList, ocorrenciaDoctoServico);
        }
    }

    private void addDoctoServicoLocalizacaoMercadoriaBatchUpdate(DoctoServico doctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, List<String> doctoServicoAliasList,
                                                                 List<String> volumeNotaFiscalAliasList, OcorrenciaDoctoServico ocorrenciaDoctoServico) {
        String alias = ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getEvento().getLocalizacaoMercadoria().getIdLocalizacaoMercadoria().toString();
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsDoctoServico.TN_DOCTO_SERVICO, alias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    ConsDoctoServico.TN_DOCTO_SERVICO,
                    ConsDoctoServico.SQL_ID_LOCALIZACAO_MERCADORIA,
                    ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getEvento().getLocalizacaoMercadoria().getIdLocalizacaoMercadoria(),
                    alias);
        }
        getDao().getAdsmHibernateTemplate().evict(doctoServico);
        doctoServico.setLocalizacaoMercadoria(ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getEvento().getLocalizacaoMercadoria());

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsDoctoServico.TN_DOCTO_SERVICO, doctoServico.getIdDoctoServico(), alias);

        if (doctoServicoAliasList != null && !doctoServicoAliasList.contains(alias)) {
            doctoServicoAliasList.add(alias);
        }

        volumeNotaFiscalService.updateLocalizacaoMercadoriaByDoctoServicoComBatch(doctoServico.getIdDoctoServico(),
                doctoServico.getLocalizacaoMercadoria(), adsmNativeBatchSqlOperations, volumeNotaFiscalAliasList);
    }

    private void generateEventoDoctoServico(DateTime dataHora, CarregamentoDescarga carregamentoDescarga, Manifesto manifesto, DoctoServico doctoServico, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findEventoDoctoServico(doctoServico.getIdDoctoServico(),
                SessionUtils.getFilialSessao().getIdFilial(),
                new Short[]{ConstantesSim.EVENTO_FIM_DESCARGA, ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO},
                true, carregamentoDescarga.getDhInicioOperacao(), carregamentoDescarga.getDhFimOperacao());

        if (eventos.isEmpty()) {
            Short cdLocalizacao = doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
            if (cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_EM_DESCARGA) || cdLocalizacao.equals(ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA)) {
                this.generateEventoDocumentoServico(manifesto, doctoServico, ConstantesSim.EVENTO_FIM_DESCARGA, dataHora, adsmNativeBatchSqlOperations);
            } else {
                this.generateEventoDocumentoServico(manifesto, doctoServico, ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO, dataHora, adsmNativeBatchSqlOperations);
            }
        }

        getDao().getAdsmHibernateTemplate().evict(doctoServico);
    }

    private void updateManifestosColetaAndGenarateEventos(DateTime dataHora,
                                                          ControleCarga controleCarga,
                                                          CarregamentoDescarga carregamentoDescarga,
                                                          EquipeOperacao equipeOperacao,
                                                          List<PedidoColeta> pedidoColetasAereo,
                                                          AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        // Pega uma lista de manifestos de coleta para o controle de carga
        OcorrenciaColeta ocorrenciaColeta = (OcorrenciaColeta) ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(ConsManifesto.VL_FIM_DESCARGA).get(0);
        List<ManifestoColeta> listManifestoColeta = manifestoColetaService.findManifestoColetaByIdControleCarga(controleCarga.getIdControleCarga());

        String manifestoColetaAlias = "mc" + ConsManifesto.VL_STATUS_MANIFESTO_FECHADO;
        String pedidoColetaAlias = "pcnt";

        for (ManifestoColeta manifestoColeta : listManifestoColeta) {
            if (ConsManifesto.VL_STATUS_MANIFESTO_FECHADO.equals(manifestoColeta.getTpStatusManifestoColeta().getValue()) || "CA".equals(manifestoColeta.getTpStatusManifestoColeta().getValue())) {
                continue;
            }

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsManifestoColeta.TN_MANIFESTO_COLETA, manifestoColeta.getIdManifestoColeta(), manifestoColetaAlias);

            // Pega uma lista de pedidos de coleta para o manifesto de coleta
            List<PedidoColeta> listPedidoColeta = pedidoColetaService.findPedidoColetaByIdManifestoColetaComDetalhes(manifestoColeta.getIdManifestoColeta());
            for (PedidoColeta pedidoColeta : listPedidoColeta) {

                if (ConsManifesto.VL_EM_DESCARGA.equals(pedidoColeta.getTpStatusColeta().getValue())) {
                    addPedidoColetaBatchUpdate(adsmNativeBatchSqlOperations, pedidoColetaAlias, pedidoColeta);
                }

                // Gerando o evento de coleta...
                EventoColeta eventoColeta = new EventoColeta();
                eventoColeta.setDhEvento(dataHora);
                eventoColeta.setTpEventoColeta(new DomainValue(ConsManifesto.VL_FIM_DESCARGA));
                eventoColeta.setPedidoColeta(pedidoColeta);
                eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());


                Map<String, Object> eventoKeyValueMap = new HashMap<String, Object>();
                eventoKeyValueMap.put(ConsEventoColeta.DH_EVENTO, eventoColeta.getDhEvento());
                eventoKeyValueMap.put(ConsEventoColeta.DH_EVENTO_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID());
                eventoKeyValueMap.put(ConsEventoColeta.TP_EVENTO_COLETA, eventoColeta.getTpEventoColeta().getValue());
                eventoKeyValueMap.put(ConsEventoColeta.ID_PEDIDO_COLETA, eventoColeta.getPedidoColeta().getIdPedidoColeta());
                eventoKeyValueMap.put(ConsEventoColeta.ID_OCORRENCIA_COLETA, eventoColeta.getOcorrenciaColeta().getIdOcorrenciaColeta());
                eventoKeyValueMap.put(ConsEventoColeta.ID_USUARIO, SessionUtils.getUsuarioLogado().getIdUsuario());
                adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsEventoColeta.EVENTO_COLETA, eventoKeyValueMap);

                // Grava o evento de coleta...

                // grava no topic de eventos de pedido coleta
                eventoColetaService.storeMessageTopic(eventoColeta, null, adsmNativeBatchSqlOperations);

                if (PedidoColetaServiceConstants.TP_PEDIDO_COLETA_AEREO.equals(pedidoColeta.getTpPedidoColeta().getValue())) {
                    pedidoColetasAereo.add(pedidoColeta);
                }

            }

            if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsManifestoColeta.TN_MANIFESTO_COLETA, manifestoColetaAlias)) {
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsManifestoColeta.TN_MANIFESTO_COLETA,
                        ConsManifestoColeta.TP_STATUS_MANIFESTO_COLETA, ConsManifesto.VL_STATUS_MANIFESTO_FECHADO, manifestoColetaAlias
                );
            }

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsManifestoColeta.TN_MANIFESTO_COLETA,
                    manifestoColeta.getIdManifestoColeta(), manifestoColetaAlias);

            Map<String, Object> storeEventosEDescargaDeManifestosValues = new HashMap<String, Object>();
            storeEventosEDescargaDeManifestosValues.put(TP_EVENTO_MANIFESTO, ConsManifesto.VL_FIM_DESCARGA);
            storeEventosEDescargaDeManifestosValues.put(EQUIPE_OPERACAO, equipeOperacao);
            storeEventosEDescargaDeManifestosValues.put(CARREGAMENTO_DESCARGA, carregamentoDescarga);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO, null);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO_COLETA, manifestoColeta);
            storeEventosEDescargaDeManifestosValues.put(FILIAL_USUARIO, null);
            storeEventosEDescargaDeManifestosValues.put(DATA_HORA, dataHora);

            storeEventosEDescargaDeManifestos(storeEventosEDescargaDeManifestosValues, adsmNativeBatchSqlOperations);

        }
    }

    private void addPedidoColetaBatchUpdate(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, String pedidoColetaAlias, PedidoColeta pedidoColeta) {
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsPedidoColeta.PEDIDO_COLETA, pedidoColetaAlias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsPedidoColeta.PEDIDO_COLETA,
                    ConsPedidoColeta.TP_STATUS_COLETA, ConsPedidoColeta.TP_STATUS_COLETA_NO_TERMINAL, pedidoColetaAlias);
        }

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsPedidoColeta.PEDIDO_COLETA,
                pedidoColeta.getIdPedidoColeta(), pedidoColetaAlias);
        pedidoColeta.setTpStatusColeta(new DomainValue(ConsPedidoColeta.TP_STATUS_COLETA_NO_TERMINAL));
        getDao().getAdsmHibernateTemplate().evict(pedidoColeta);
    }

    public TypedFlatMap finalizarDescargaViagem(DateTime dataHora, TypedFlatMap retorno, ControleCarga controleCarga,
                                                CarregamentoDescarga carregamentoDescarga, Filial filialUsuario, EquipeOperacao equipeOperacao,
                                                AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        updateControleCargaConhecimentoScamWithCarregamentoDescarga(controleCarga, adsmNativeBatchSqlOperations);

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                ConsManifesto.TN_MANIFESTO,
                ConsManifesto.SQL_TP_STATUS_MANIFESTO,
                ConsManifesto.VL_STATUS_MANIFESTO_FECHADO
        );

        List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), null, ConsManifesto.VL_EM_DESCARGA, null);
        for (Manifesto manifesto : listManifesto) {

            if (manifesto.getManifestoViagemNacional() != null) {
                getDao().getAdsmHibernateTemplate().initialize(manifesto.getManifestoViagemNacional().getManifestoNacionalVolumes());
            }

            this.generateEventoDoctoServicoFimDescargaViagem(dataHora, carregamentoDescarga.getDhInicioOperacao(), manifesto, adsmNativeBatchSqlOperations);
            this.generateEventoVolumeFimDescargaViagem(manifesto.getIdManifesto(), controleCarga.getIdControleCarga(), carregamentoDescarga.getIdCarregamentoDescarga(), adsmNativeBatchSqlOperations);

            //Atualiza e desatacha o manifesto
            updateManifestoFinalizarDescargaViagem(adsmNativeBatchSqlOperations, manifesto);

            Map<String, Object> storeEventosEDescargaDeManifestosValues = new HashMap<String, Object>();
            storeEventosEDescargaDeManifestosValues.put(TP_EVENTO_MANIFESTO, ConsManifesto.VL_FIM_DESCARGA);
            storeEventosEDescargaDeManifestosValues.put(EQUIPE_OPERACAO, equipeOperacao);
            storeEventosEDescargaDeManifestosValues.put(CARREGAMENTO_DESCARGA, carregamentoDescarga);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO, manifesto);
            storeEventosEDescargaDeManifestosValues.put(MANIFESTO_COLETA, null);
            storeEventosEDescargaDeManifestosValues.put(FILIAL_USUARIO, filialUsuario);
            storeEventosEDescargaDeManifestosValues.put(DATA_HORA, dataHora);
            storeEventosEDescargaDeManifestos(storeEventosEDescargaDeManifestosValues, adsmNativeBatchSqlOperations);

            this.storeCarregamentoDescargaVolumesViagem(controleCarga, carregamentoDescarga, manifesto, adsmNativeBatchSqlOperations);
        }

        if (filialUsuario.getIdFilial().equals(controleCarga.getFilialByIdFilialDestino().getIdFilial())) {
            //LMS-3544
            retorno = manifestoEletronicoService.encerrarMdfesAutorizados(controleCarga.getIdControleCarga());
            gerarRateioFreteCarreteiroService.execute(controleCarga.getIdControleCarga(), adsmNativeBatchSqlOperations);
        }

        adsmNativeBatchSqlOperations.runAllCommands();

        /* LMS-1052 Preparar relatório de discrepâncias */
        //LMSA-4419 - Linha de código removida devido a orientação do Contreras de que não se trabalha mais com a GM
        return retorno;
    }

    private void updateManifestoFinalizarDescargaViagem(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Manifesto manifesto) {
        manifesto.setTpStatusManifesto(new DomainValue(ConsManifesto.VL_STATUS_MANIFESTO_FECHADO));
        getDao().getAdsmHibernateTemplate().evict(manifesto);
        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsManifesto.TN_MANIFESTO, manifesto.getIdManifesto());
    }

    private void generateEventoDoctoServicoFimDescargaViagem(DateTime dataHora, DateTime dhInicioOperacao, Manifesto manifesto, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        /** Otimização LMS-807 */
        final ProjectionList projection = Projections.projectionList()
                .add(Projections.property(ConsCarregamentoDescarga.DS_ID), ConsCarregamentoDescarga.ID_DOCTO_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_FILIAL_LOCALIZACAO), ConsCarregamentoDescarga.FILIAL_LOCALIZACAO)
                .add(Projections.property(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA), ConsCarregamentoDescarga.LOCALIZACAO_MERCADORIA_CD_LOCALIZACAO_MERCADORIA)
                .add(Projections.property(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_ORIGEM_ID), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_ORIGEM_ID_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.FO_SG_FILIAL), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_ORIGEM_SG_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_DESTINO_ID), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_DESTINO_ID_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.FD_SG_FILIAL), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_DESTINO_SG_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.DS_NR_DOCTO_SERVICO), ConsDoctoServico.NR_DOCTO_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_TP_DOCUMENTO_SERVICO), ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO);

        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.DS_LOCALIZACAO_MERCADORIA, "loc");
        alias.put(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_ORIGEM, ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_ORIGEM_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_DESTINO, ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_DESTINO_ALIAS);

        //Buscar valores onde o cd_mercadoria_em_descarga é 34 e que a filial de localização seja igual a logada
        final List<Criterion> criterionWithCdEmDescarga = new ArrayList<Criterion>();
        criterionWithCdEmDescarga.add(Restrictions.eq(
                ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                ConstantesSim.CD_MERCADORIA_EM_DESCARGA));

        criterionWithCdEmDescarga.add(Restrictions.eq(
                ConsCarregamentoDescarga.DS_FILIAL_LOCALIZACAO_ID_FILIAL,
                SessionUtils.getFilialSessao().getIdFilial()));

        //Buscar valores onde o cd_mercadoria_em_descarga é 34 e que a filial de localização não seja igual a logada
        //Ou que o cd_mercadoria_em_descarga seja diferente de 1 e 34
        final List<Criterion> criterionWoCdEmDescarga = new ArrayList<Criterion>();
        criterionWoCdEmDescarga.add(
                Restrictions.or(
                        Restrictions.and(
                                Restrictions.ne(
                                        ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                                        ConstantesSim.CD_MERCADORIA_EM_DESCARGA),
                                Restrictions.ne(
                                        ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                                        ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA)
                        ),
                        Restrictions.and(
                                Restrictions.eq(
                                        ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                                        ConstantesSim.CD_MERCADORIA_EM_DESCARGA),
                                Restrictions.ne(
                                        ConsCarregamentoDescarga.DS_FILIAL_LOCALIZACAO_ID_FILIAL,
                                        SessionUtils.getFilialSessao().getIdFilial()))
                )
        );

        final List<DoctoServico> listDoctoServicoCdEmDescarga =
                doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias, criterionWithCdEmDescarga, false, true, dhInicioOperacao);

        final List<DoctoServico> listDoctoServicoFimDescargaSemMudarLoc =
                doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias, criterionWoCdEmDescarga, false, true, dhInicioOperacao);

        for (DoctoServico doctoServico : listDoctoServicoCdEmDescarga) {
            this.generateEventoDocumentoServico(manifesto, doctoServico, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, dataHora, adsmNativeBatchSqlOperations);
        }

        for (DoctoServico doctoServico : listDoctoServicoFimDescargaSemMudarLoc) {
            this.generateEventoDocumentoServico(manifesto, doctoServico, ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO, dataHora, adsmNativeBatchSqlOperations);
        }
    }

    private void updateControleCargaConhecimentoScamWithCarregamentoDescarga(
            ControleCarga controleCarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        List<ControleCargaConhScan> controleCargaConhScanList = controleCargaConhScanService.findByIdControleCarga(controleCarga.getIdControleCarga());
        CarregamentoDescarga cd = null;
        for (ControleCargaConhScan cccs : controleCargaConhScanList) {
            if (cccs.getCarregamentoDescarga() == null) {
                if (cd == null) {
                    cd = getCarregamentoDescargaDAO().findCarregamentoDescarga(controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "D");
                }
                if (adsmNativeBatchSqlOperations != null) {
                    nativeUpdateCohnScan(adsmNativeBatchSqlOperations, cd, cccs);
                } else {
                    cccs.setCarregamentoDescarga(cd);
                    controleCargaConhScanService.store(cccs);
                }
            }
        }
    }

    private void nativeUpdateCohnScan(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, CarregamentoDescarga cd, ControleCargaConhScan cccs) {
        String updateTableAlias = "conhScan";
        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsControleCarga.SQL_CONTROLE_CARGA_CONH_SCAN, updateTableAlias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                    ConsControleCarga.SQL_CONTROLE_CARGA_CONH_SCAN,
                    ConsControleCarga.SQL_ID_CARREGAMENTO_DESCARGA,
                    cd.getIdCarregamentoDescarga(),
                    updateTableAlias);
        }
        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(
                ConsControleCarga.SQL_CONTROLE_CARGA_CONH_SCAN,
                cccs.getIdControleCargaConhScan(),
                updateTableAlias);
    }

    public void storeCarregamentoDescargaVolumesViagem(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga,
                                                       Manifesto manifesto, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        List<VolumeNotaFiscal> volumes = new ArrayList<VolumeNotaFiscal>();

        if (manifesto.getManifestoViagemNacional() != null) {
            for (ManifestoNacionalVolume mnv : manifesto.getManifestoViagemNacional().getManifestoNacionalVolumes()) {
                volumes.add(mnv.getVolumeNotaFiscal());
            }
        }

        //LMS-3906
        this.storeValidateCarregamentoDescargaVolume(controleCarga, carregamentoDescarga, volumes, "D", adsmNativeBatchSqlOperations);
    }

    public void storeCarregamentoDescargaVolumesEntrega(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga,
                                                        List<ManifestoEntrega> listManifestoEntrega, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        //LMS-3906
        List<VolumeNotaFiscal> volumes = new ArrayList<VolumeNotaFiscal>();
        for (ManifestoEntrega manifestoEntrega : listManifestoEntrega) {
            for (ManifestoEntregaVolume manifestoEntregaVolume : (List<ManifestoEntregaVolume>) manifestoEntrega.getManifestoEntregaVolumes()) {
                if (manifestoEntregaVolume.getOcorrenciaEntrega() == null || !"E".equals(manifestoEntregaVolume.getOcorrenciaEntrega().getTpOcorrencia().getValue())) {
                    volumes.add(manifestoEntregaVolume.getVolumeNotaFiscal());
                }
            }
        }

        storeValidateCarregamentoDescargaVolume(controleCarga, carregamentoDescarga, volumes, "D", adsmNativeBatchSqlOperations);
    }

    private void storeValidateCarregamentoDescargaVolume(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga,
                                                         List<VolumeNotaFiscal> volumes, String tpOperacao,
                                                         AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        for (VolumeNotaFiscal volume : volumes) {

            List<CarregamentoDescargaVolume> listaCarregamentoDescargaVolume =
                    carregamentoDescargaVolumeService.findByControleCargaByVolumeNotaFiscalByCarregamentoDescarga(
                            controleCarga.getIdControleCarga(), volume.getIdVolumeNotaFiscal(), carregamentoDescarga.getIdCarregamentoDescarga(), tpOperacao);

            if (listaCarregamentoDescargaVolume == null || listaCarregamentoDescargaVolume.isEmpty()) {
                Map<String, Object> carregamentoDescargaVolumeKeyValueMap = new HashMap<String, Object>();
                carregamentoDescargaVolumeKeyValueMap.put(ConsDescargaManifesto.ID_CARREGAMENTO_DESCARGA, carregamentoDescarga.getIdCarregamentoDescarga());
                carregamentoDescargaVolumeKeyValueMap.put("ID_DISPOSITIVO_UNITIZACAO", volume.getDispositivoUnitizacao() != null ? volume.getDispositivoUnitizacao().getIdDispositivoUnitizacao() : null);
                carregamentoDescargaVolumeKeyValueMap.put("DH_OPERACAO", JTDateTimeUtils.getDataHoraAtual());
                carregamentoDescargaVolumeKeyValueMap.put("DH_OPERACAO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
                carregamentoDescargaVolumeKeyValueMap.put("TP_SCAN", "LM");
                carregamentoDescargaVolumeKeyValueMap.put("ID_VOLUME_NOTA_FISCAL", volume.getIdVolumeNotaFiscal());
                carregamentoDescargaVolumeKeyValueMap.put("QT_VOLUMES", volume.getQtVolumes());
                adsmNativeBatchSqlOperations.addNativeBatchInsert("CARREG_DESC_VOLUME", carregamentoDescargaVolumeKeyValueMap);
            }
        }
    }

    private void storeValidateCarregamentoDescargaVolume(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga,
                                                         VolumeNotaFiscal volume, String tpOperacao) {
        List<VolumeNotaFiscal> volumes = new ArrayList<VolumeNotaFiscal>();
        volumes.add(volume);
        this.storeValidateCarregamentoDescargaVolume(controleCarga, carregamentoDescarga, volumes, tpOperacao);
    }

    private void storeValidateCarregamentoDescargaVolume(ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga,
                                                         List<VolumeNotaFiscal> volumes, String tpOperacao) {

        List<CarregamentoDescargaVolume> cdVolumes = new ArrayList<CarregamentoDescargaVolume>();

        for (VolumeNotaFiscal volume : volumes) {

            List<CarregamentoDescargaVolume> listaCarregamentoDescargaVolume =
                    carregamentoDescargaVolumeService.findByControleCargaByVolumeNotaFiscalByCarregamentoDescarga(
                            controleCarga.getIdControleCarga(), volume.getIdVolumeNotaFiscal(), carregamentoDescarga.getIdCarregamentoDescarga(), tpOperacao);

            if (listaCarregamentoDescargaVolume == null || listaCarregamentoDescargaVolume.isEmpty()) {
                CarregamentoDescargaVolume carregamentoDescargaVolume = new CarregamentoDescargaVolume();
                carregamentoDescargaVolume.setCarregamentoDescarga(carregamentoDescarga);
                carregamentoDescargaVolume.setDispositivoUnitizacao(volume.getDispositivoUnitizacao());
                carregamentoDescargaVolume.setDhOperacao(JTDateTimeUtils.getDataHoraAtual());
                carregamentoDescargaVolume.setTpScan(new DomainValue("LM"));
                carregamentoDescargaVolume.setVolumeNotaFiscal(volume);
                carregamentoDescargaVolume.setQtVolumes(volume.getQtVolumes());
                cdVolumes.add(carregamentoDescargaVolume);
            }
        }
        carregamentoDescargaVolumeService.storeAll(cdVolumes);
    }

    /**
     * Gera evento manifesto e descarga manifesto e grava os registros
     *
     * @param storeEventosEDescargaDeManifestosMap
     * @param adsmNativeBatchSqlOperations
     */
    private void storeEventosEDescargaDeManifestos(
            Map<String, Object> storeEventosEDescargaDeManifestosMap,
            AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations
    ) {

        String tpEventoManifesto = (String) storeEventosEDescargaDeManifestosMap.get(TP_EVENTO_MANIFESTO);
        EquipeOperacao equipeOperacao = (EquipeOperacao) storeEventosEDescargaDeManifestosMap.get(EQUIPE_OPERACAO);
        CarregamentoDescarga carregamentoDescarga = (CarregamentoDescarga) storeEventosEDescargaDeManifestosMap.get(CARREGAMENTO_DESCARGA);

        Manifesto manifesto = storeEventosEDescargaDeManifestosMap.get(MANIFESTO) != null
                ? (Manifesto) storeEventosEDescargaDeManifestosMap.get(MANIFESTO) : null;

        ManifestoColeta manifestoColeta = storeEventosEDescargaDeManifestosMap.get(MANIFESTO_COLETA) != null
                ? (ManifestoColeta) storeEventosEDescargaDeManifestosMap.get(MANIFESTO_COLETA) : null;

        Filial filialUsuario = storeEventosEDescargaDeManifestosMap.get(FILIAL_USUARIO) != null
                ? (Filial) storeEventosEDescargaDeManifestosMap.get(FILIAL_USUARIO) : null;

        DateTime dataHora = (DateTime) storeEventosEDescargaDeManifestosMap.get(DATA_HORA);

        DescargaManifesto descargaManifesto = null;

        if (manifesto != null) {
            descargaManifesto = saveEventoManifestoAndGetDescargaManifesto(adsmNativeBatchSqlOperations, tpEventoManifesto, manifesto, filialUsuario, dataHora);
        }

        if (manifestoColeta != null) {
            descargaManifesto = saveEventoManifestoColetaAndGetDescargaManifesto(adsmNativeBatchSqlOperations, tpEventoManifesto, manifestoColeta, dataHora);
        }

        // Gerando ou alterando registro para descarga manifesto
        if (descargaManifesto == null) {
            insertDescargaManifestoBatch(adsmNativeBatchSqlOperations, equipeOperacao, carregamentoDescarga, manifesto, manifestoColeta, dataHora);
        } else {
            updateDescargaManifestoBatch(adsmNativeBatchSqlOperations, dataHora, descargaManifesto);
        }
    }

    private void updateDescargaManifestoBatch(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, DateTime dataHora, DescargaManifesto descargaManifesto) {
        String updateTableAlias = "fimDescarga";

        if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsDescargaManifesto.TN_DESCARGA_MANIFESTO, updateTableAlias)) {
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDescargaManifesto.TN_DESCARGA_MANIFESTO,
                    ConsDescargaManifesto.DH_FIM_DESCARGA, dataHora, updateTableAlias);
            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsDescargaManifesto.TN_DESCARGA_MANIFESTO,
                    ConsDescargaManifesto.DH_FIM_DESCARGA_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID(), updateTableAlias);
        }

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsDescargaManifesto.TN_DESCARGA_MANIFESTO,
                descargaManifesto.getIdDescargaManifesto(), updateTableAlias);
    }

    private void insertDescargaManifestoBatch(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, EquipeOperacao equipeOperacao, CarregamentoDescarga carregamentoDescarga, Manifesto manifesto, ManifestoColeta manifestoColeta, DateTime dataHora) {
        Map<String, Object> descargaManifestoKeyValueMap = new HashMap<String, Object>();
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.DH_INICIO_DESCARGA, dataHora);
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.DH_INICIO_DESCARGA_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID());
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.ID_CARREGAMENTO_DESCARGA, carregamentoDescarga.getIdCarregamentoDescarga());
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.ID_MANIFESTO, manifesto != null ? manifesto.getIdManifesto() : null);
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.ID_MANIFESTO_COLETA, manifestoColeta != null ? manifestoColeta.getIdManifestoColeta() : null);
        descargaManifestoKeyValueMap.put(ConsDescargaManifesto.ID_EQUIPE_OPERACAO, equipeOperacao != null ? equipeOperacao.getIdEquipeOperacao() : null);

        adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsDescargaManifesto.TN_DESCARGA_MANIFESTO, descargaManifestoKeyValueMap);
    }

    private DescargaManifesto saveEventoManifestoColetaAndGetDescargaManifesto(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, String tpEventoManifesto, ManifestoColeta manifestoColeta, DateTime dataHora) {
        Map<String, Object> eventoKeyValueMap = new HashMap<String, Object>();
        eventoKeyValueMap.put(ConsEventoManifestoColeta.DH_EVENTO, dataHora);
        eventoKeyValueMap.put(ConsEventoManifestoColeta.DH_EVENTO_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoKeyValueMap.put(ConsEventoManifestoColeta.TP_EVENTO_MANIFESTO_COLETA, tpEventoManifesto);
        eventoKeyValueMap.put(ConsEventoManifestoColeta.ID_MANIFESTO_COLETA, manifestoColeta.getIdManifestoColeta());
        adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsEventoManifestoColeta.TN_EVENTO_MANIFESTO_COLETA, eventoKeyValueMap);

        return descargaManifestoService.findDescargaManifestoByIdManifestoColeta(manifestoColeta.getIdManifestoColeta());
    }

    private DescargaManifesto saveEventoManifestoAndGetDescargaManifesto(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations,
                                                                         String tpEventoManifesto, Manifesto manifesto, Filial filialUsuario, DateTime dataHora) {
        Map<String, Object> eventoKeyValueMap = new HashMap<String, Object>();
        eventoKeyValueMap.put(ConsEventoManifesto.DH_EVENTO, dataHora);
        eventoKeyValueMap.put(ConsEventoManifesto.DH_EVENTO_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID());
        eventoKeyValueMap.put(ConsEventoManifesto.TP_EVENTO_MANIFESTO, tpEventoManifesto);
        eventoKeyValueMap.put(ConsEventoManifesto.ID_MANIFESTO, manifesto.getIdManifesto());
        eventoKeyValueMap.put(ConsEventoManifesto.ID_USUARIO, SessionUtils.getUsuarioLogado().getIdUsuario());
        eventoKeyValueMap.put(ConsEventoManifesto.ID_FILIAL, filialUsuario.getIdFilial());
        adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsEventoManifesto.TN_EVENTO_MANIFESTO, eventoKeyValueMap);

        return descargaManifestoService.findDescargaManifestoByIdManifesto(manifesto.getIdManifesto());
    }

    /**
     * Gera evento manifesto e descarga manifesto e grava os registros
     *
     * @param tpEventoManifesto
     * @param equipeOperacao
     * @param carregamentoDescarga
     * @param manifesto
     * @param manifestoColeta
     * @param filialUsuario
     * @param dataHora
     */
    private void storeEventosEDescargaDeManifestos(
            String tpEventoManifesto,
            EquipeOperacao equipeOperacao,
            CarregamentoDescarga carregamentoDescarga,
            Manifesto manifesto,
            ManifestoColeta manifestoColeta,
            Filial filialUsuario,
            DateTime dataHora
    ) {
        // Criando instância
        DescargaManifesto descargaManifesto = null;

        if (manifesto != null) {
            // Gerando o evento de manifesto...
            EventoManifesto eventoManifesto = new EventoManifesto();
            eventoManifesto.setDhEvento(dataHora);
            eventoManifesto.setTpEventoManifesto(new DomainValue(tpEventoManifesto));
            eventoManifesto.setManifesto(manifesto);
            eventoManifesto.setFilial(filialUsuario);
            eventoManifesto.setUsuario(SessionUtils.getUsuarioLogado());
            // Grava o evento de manifesto...
            eventoManifestoService.store(eventoManifesto);

            descargaManifesto = descargaManifestoService.findDescargaManifestoByIdManifesto(manifesto.getIdManifesto());
        }

        if (manifestoColeta != null) {
            // Gerando o evento de manifesto...
            EventoManifestoColeta eventoManifestoColeta = new EventoManifestoColeta();
            eventoManifestoColeta.setDhEvento(dataHora);
            eventoManifestoColeta.setTpEventoManifestoColeta(new DomainValue(tpEventoManifesto));
            eventoManifestoColeta.setManifestoColeta(manifestoColeta);
            // Grava o evento de manifesto...
            eventoManifestoColetaService.store(eventoManifestoColeta);

            descargaManifesto = descargaManifestoService.findDescargaManifestoByIdManifestoColeta(manifestoColeta.getIdManifestoColeta());
        }

        // Gerando ou alterando registro para descarga manifesto
        if (descargaManifesto == null) {
            descargaManifesto = new DescargaManifesto();
            descargaManifesto.setDhInicioDescarga(dataHora);
            descargaManifesto.setCarregamentoDescarga(carregamentoDescarga);
            descargaManifesto.setManifesto(manifesto);
            descargaManifesto.setManifestoColeta(manifestoColeta);
            descargaManifesto.setEquipeOperacao(equipeOperacao);
        } else {
            descargaManifesto.setDhFimDescarga(dataHora);
        }
        // Grava descarga manifesto
        descargaManifestoService.store(descargaManifesto);
    }

    /**
     * Incluido para atender necessidade do MWW
     * Gera evento manifesto e descarga manifesto e grava os registros
     *
     * @param tpEventoManifesto
     * @param carregamentoDescarga
     * @param manifesto
     * @param filialUsuario
     * @param dataHora
     */
    public void storeEventosManifestos(
            String tpEventoManifesto,
            CarregamentoDescarga carregamentoDescarga,
            Manifesto manifesto,
            Filial filialUsuario,
            DateTime dataHora
    ) {
        this.storeEventosEDescargaDeManifestos(tpEventoManifesto, null, carregamentoDescarga, manifesto, null, filialUsuario, dataHora);
    }

    /**
     * Atualiza a quantidade de estoque em EstoqueDispositivoQtde
     *
     * @param dispCarregDescQtde
     * @param controleCarga
     * @param filialUsuario
     */
    private void atualizaEstoqueDispositivoQtde(
            DispCarregDescQtde dispCarregDescQtde,
            ControleCarga controleCarga,
            Filial filialUsuario
    ) {
        Integer quantidade = null;

        // Código que busca o EstoqueDispositivoQtde com o id do Controle de Carga e subtrai
        // a quantidade total da quantidade informada, sem deixar ficar negativo,
        // salvando em seguida o registro alterado.
        EstoqueDispositivoQtde estoquePorControleCarga = estoqueDispositivoQtdeService.findEstoqueDispositivoQtde(
                dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao(),
                dispCarregDescQtde.getEmpresa().getIdEmpresa(),
                controleCarga.getIdControleCarga(),
                null
        );
        if (estoquePorControleCarga != null) {
            if ("C".equals(dispCarregDescQtde.getCarregamentoDescarga().getTpOperacao().getValue())) {
                quantidade = estoquePorControleCarga.getQtEstoque() +
                        dispCarregDescQtde.getQtDispositivo();
            } else if ("D".equals(dispCarregDescQtde.getCarregamentoDescarga().getTpOperacao().getValue())) {
                quantidade = estoquePorControleCarga.getQtEstoque() -
                        dispCarregDescQtde.getQtDispositivo();
                if (quantidade < 0) {
                    quantidade = 0;
                }
            }
            estoquePorControleCarga.setQtEstoque(quantidade);
        } else {
            estoquePorControleCarga = new EstoqueDispositivoQtde();
            estoquePorControleCarga.setTipoDispositivoUnitizacao(dispCarregDescQtde.getTipoDispositivoUnitizacao());
            estoquePorControleCarga.setEmpresa(dispCarregDescQtde.getEmpresa());
            estoquePorControleCarga.setQtEstoque(dispCarregDescQtde.getQtDispositivo());
            estoquePorControleCarga.setFilial(null);
            estoquePorControleCarga.setControleCarga(controleCarga);
        }
        estoqueDispositivoQtdeService.store(estoquePorControleCarga);

        // Código que busca o EstoqueDispositivoQtde com o id da filial do usuário logado...
        // Caso exista, soma a quantidade total com quantidade informada, senão cria um novo
        // registro e seta a quantidade, salvando em seguida o novo ou a alteração.
        EstoqueDispositivoQtde estoquePorFilial = estoqueDispositivoQtdeService
                .findEstoqueDispositivoQtde(dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao(),
                        dispCarregDescQtde.getEmpresa().getIdEmpresa(),
                        null,
                        filialUsuario.getIdFilial());
        if (estoquePorFilial != null) {
            if ("C".equals(dispCarregDescQtde.getCarregamentoDescarga().getTpOperacao().getValue())) {
                quantidade = estoquePorFilial.getQtEstoque() -
                        dispCarregDescQtde.getQtDispositivo();
                if (quantidade < 0) {
                    quantidade = 0;
                }
            } else if ("D".equals(dispCarregDescQtde.getCarregamentoDescarga().getTpOperacao().getValue())) {
                quantidade = estoquePorFilial.getQtEstoque() +
                        dispCarregDescQtde.getQtDispositivo();
            }
            estoquePorFilial.setQtEstoque(quantidade);
        } else {
            estoquePorFilial = new EstoqueDispositivoQtde();
            estoquePorFilial.setTipoDispositivoUnitizacao(dispCarregDescQtde.getTipoDispositivoUnitizacao());
            estoquePorFilial.setEmpresa(dispCarregDescQtde.getEmpresa());
            estoquePorFilial.setQtEstoque(dispCarregDescQtde.getQtDispositivo());
            estoquePorFilial.setFilial(filialUsuario);
            estoquePorFilial.setControleCarga(null);
        }
        estoqueDispositivoQtdeService.store(estoquePorFilial);

    }

    /**
     * Atualiza a quantidade de estoque em EstoqueDispIdentificado
     *
     * @param dispositivoUnitizacao
     * @param controleCarga
     * @param filialUsuario
     */
    private void atualizaEstoqueDispIdentificado(
            DispositivoUnitizacao dispositivoUnitizacao,
            ControleCarga controleCarga,
            Filial filialUsuario
    ) {
        EstoqueDispIdentificado estoquePorControleCarga = estoqueDispIdentificadoService.findEstoqueDispIdentificado(
                dispositivoUnitizacao.getIdDispositivoUnitizacao(),
                controleCarga.getIdControleCarga(),
                null
        );
        if (estoquePorControleCarga != null) {
            estoqueDispIdentificadoService.removeById(estoquePorControleCarga.getIdEstoqueDispIdentificado());
        }

        EstoqueDispIdentificado estoquePorFilial = estoqueDispIdentificadoService.findEstoqueDispIdentificado(
                dispositivoUnitizacao.getIdDispositivoUnitizacao(),
                null,
                filialUsuario.getIdFilial()
        );

        if (estoquePorFilial == null) {
            estoquePorFilial = new EstoqueDispIdentificado();
            estoquePorFilial.setDispositivoUnitizacao(dispositivoUnitizacao);
            estoquePorFilial.setFilial(filialUsuario);
            estoquePorFilial.setControleCarga(null);

            estoqueDispIdentificadoService.store(estoquePorFilial);
        }

    }

    /**
     * Busca um CarregamentoDescarga com o id da filial e do controle de carga
     *
     * @param idFilial
     * @param idControleCarga
     * @return
     */
    public List<CarregamentoDescarga> findCarregamentoDescargaByIdFilialByIdControleCarga(Long idFilial, Long idControleCarga) {
        return this.getCarregamentoDescargaDAO().findCarregamentoDescargaByIdFilialByIdControleCarga(idFilial, idControleCarga);
    }

    /**
     * Busca um CarregamentoDescarga com o id do volume
     *
     * @param idVolume
     * @return
     */
    public CarregamentoDescarga findDescargaIniciadaByVolumeAndFilial(Long idVolume, Long idFilial) {
        return this.getCarregamentoDescargaDAO().findCarregamentoDescargaByIdVolume(idVolume, idFilial);
    }

    /**
     * Gera um evento para TODOS os documentos de servicos associados a
     * um determinado manifesto, filtrando pela(s) localizacao(oes) mercadoria(s) desejada(s).
     *
     * @param idManifesto
     * @param cdEvento
     * @param tpDocumento
     * @param cdLocalizacaoMercadoria
     */
    private void generateEventoDocumentoServicoByIdManifesto(Long idManifesto, Short cdEvento, String tpDocumento, Short[] cdLocalizacaoMercadoria) {
        /* Otimização LMS-806 */
        final ProjectionList projection = Projections.projectionList()
                .add(Projections.property(ConsCarregamentoDescarga.DS_ID), ConsCarregamentoDescarga.ID_DOCTO_SERVICO);

        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.DS_LOCALIZACAO_MERCADORIA, "loc");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.in(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA, cdLocalizacaoMercadoria));

        final List<DoctoServico> listDoctoServico = doctoServicoService.findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterion);


        Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
        String strManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifesto.getNrPreManifesto().toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);

        for (DoctoServico doctoServico : listDoctoServico) {
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strManifesto, dataHoraAtual, null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
        }
    }

    /**
     * Faz a geracao de eventos para os doctoServico de forma individual.
     * Método chamado no fim da descarga.
     *
     * @param manifesto
     * @param doctoServico
     * @param cdEvento
     */
    public void generateEventoDocumentoServico(Manifesto manifesto, DoctoServico doctoServico, Short cdEvento) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        this.generateEventoDocumentoServico(manifesto, doctoServico, cdEvento, dataHoraAtual, null);
    }

    /**
     * Faz a geracao de eventos para os doctoServico de forma individual.
     * Método chamado no fim da descarga.
     * Chamada alterada para contemplar necessidade de se passar uma data/hora diferente da atual (Integração).
     *
     * @param manifesto
     * @param doctoServico
     * @param cdEvento
     */
    public void generateEventoDocumentoServico(Manifesto manifesto, DoctoServico doctoServico, Short cdEvento, DateTime dataHora, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
        String strManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifestoService.findNrManifestoByManifesto(manifesto),
                ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);

        String tpDocumento = null;
        if (manifesto.getControleCarga().getTpControleCarga().getValue()
                .equals(ConstantesExpedicao.TP_MANIFESTO_VIAGEM)) {
            tpDocumento = ConsManifesto.VL_MANIFESTO_VIAGEM;
        } else if (ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA
                .equals(manifesto.getControleCarga().getTpControleCarga().getValue())) {
            tpDocumento = ConsManifesto.VL_MANIFESTO_ENTREGA;
        }

        if (adsmNativeBatchSqlOperations == null) {
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                    cdEvento,
                    doctoServico.getIdDoctoServico(),
                    filialUsuarioLogado.getIdFilial(),
                    strManifesto,
                    dataHora,
                    null,
                    filialUsuarioLogado.getSiglaNomeFilial(),
                    tpDocumento);
        } else {
            Map<String, Object> eventosValores = new HashMap<String, Object>();
            eventosValores.put("cdEvento", cdEvento);
            eventosValores.put("idFilial", filialUsuarioLogado.getIdFilial());
            eventosValores.put("nrDocumento", strManifesto);
            eventosValores.put("dhEvento", dataHora);
            eventosValores.put("dsObservacao", filialUsuarioLogado.getSiglaNomeFilial());
            eventosValores.put("tpDocumento", tpDocumento);
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumentoComBatch(
                    doctoServico,
                    eventosValores,
                    adsmNativeBatchSqlOperations);
        }

    }

    /**
     * Gera um evento para TODOS os documentos de servicos associados a
     * um determinado manifesto.
     *
     * @param idManifesto
     * @param cdEvento
     * @param tpDocumento
     * @param dataHora
     */
    public void generateEventoDocumentoServicoByIdManifesto(Long idManifesto, Short cdEvento, String tpDocumento, DateTime dataHora) {
        /** Otimização LMS-804 */
        final ProjectionList projection = Projections.projectionList()
                .add(Projections.property(ConsCarregamentoDescarga.DS_ID), ConsCarregamentoDescarga.ID_DOCTO_SERVICO)
	    		.add(Projections.property("fd.id"), "filialByIdFilialDestino.idFilial")
        		.add(Projections.property("cd.blAgendamento"), "clienteByIdClienteDestinatario.blAgendamento");

        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.DS_LOCALIZACAO_MERCADORIA, "loc");
        alias.put("ds.filialByIdFilialDestino", "fd");
        alias.put("ds.clienteByIdClienteDestinatario", "cd");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.ne(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA, ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA));

        final List<DoctoServico> listDoctoServico = doctoServicoService.findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterion);

        Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
        Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
        final String strManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifestoService.findNrManifestoByManifesto(manifesto), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
        for (final DoctoServico doctoServico : listDoctoServico) {
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, doctoServico.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strManifesto, dataHora, null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
            //Apoyo 201908086 LMSA-8359
			ocorrenciaDoctoServicoService.validateAgendamentoEntrega(filialUsuarioLogado.getIdFilial(), doctoServico);
        }
    }

    /**
     * Gera um evento para TODOS os documentos de servicos associados a
     * um determinado manifesto. Verifica se o se os doctoServico possuem ocorrenciaEntrega
     * setada para "E" ou "A". Caso sim, nao é realizado a geração deste evento.
     *
     * @param manifesto
     * @param cdEvento
     * @param dataHora
     */
    private void generateEventoDoctoServicoByIdManifestoIfNotOcorrenciaEntrega(Manifesto manifesto,
                                                                               Short cdEvento, DateTime dataHora) {
        Short cdEventoAux = cdEvento;
        Filial filialUsuario = SessionUtils.getFilialSessao();
        List<ManifestoEntregaDocumento> manifestoEntregaDocumentos = manifesto.getManifestoEntrega().getManifestoEntregaDocumentos();
        String strManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifestoService.findNrManifestoByManifesto(manifesto), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
        String cdbloqueio = (String) parametroGeralService.findConteudoByNomeParametro("CD_PEND_BLOQ_LOCALIZACAO", false);
        List<String> lstCodigos = new ArrayList<String>();
        addCodigosBloqueio(cdbloqueio, lstCodigos);

        for (ManifestoEntregaDocumento manifestoEntregaDocumento : manifestoEntregaDocumentos) {
        	boolean isEntregaParcial = manifestoEntregaDocumento.getOcorrenciaEntrega() != null && CD_OCORRENCIA_ENTREGA_PARCIAL.equals(manifestoEntregaDocumento.getOcorrenciaEntrega().getCdOcorrenciaEntrega());

        	boolean naoPossuiOcorrenciaEntrega = manifestoEntregaDocumento.getOcorrenciaEntrega() == null
                    || (!ConstantesEntrega.TP_OCORRENCIA_ENTREGUE.equals(manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue())
	            && !ConstantesEntrega.TP_OCORRENCIA_ENTREGUE_AEROPORTO.equals(manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue()));
        	
        	if (isEntregaParcial || naoPossuiOcorrenciaEntrega) {
                DoctoServico doc = doctoServicoService.findDoctoServicoById(manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico());
                LocalizacaoMercadoria localizacao = doc.getLocalizacaoMercadoria();

                if (localizacao != null && lstCodigos.contains(String.valueOf(localizacao.getCdLocalizacaoMercadoria()))) {
                    cdEventoAux = ConsCarregamentoDescarga.CD_EVENTO_153;
                }

                if (localizacao == null || !ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.equals(localizacao.getCdLocalizacaoMercadoria())) {
                    incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEventoAux, manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico(), filialUsuario.getIdFilial(), strManifesto, dataHora, null, filialUsuario.getSiglaNomeFilial(), "MAE");
                }
            }
        }
    }

    private void addCodigosBloqueio(String cdbloqueio, List<String> lstCodigos) {
        String[] vet;
        if (cdbloqueio != null && !cdbloqueio.isEmpty()) {
            vet = cdbloqueio.split(";");
            for (String v : vet) {
                lstCodigos.add(v);
            }

        }
    }

    /**
     * Gera um evento para TODOS os documentos de servicos associados a um determinado manifesto.
     *
     * @param idManifesto id do Manifesto em questao
     * @param cdEvento    chave codigo do evento
     * @param tpDocumento tipo do documento
     */
    public void generateEventoDocumentoServicoByIdPreManifesto(Long idManifesto, Short cdEvento, String tpDocumento) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        this.generateEventoDocumentoServicoByIdPreManifesto(idManifesto, cdEvento, tpDocumento, dataHoraAtual);
    }

    /**
     * Gera um evento para TODOS os documentos de servicos associados a um determinado manifesto.
     * Assinatura modificada (inserido dataHora) para contemplar
     * solicitação da integração CQPRO00005475 e CQPRO00005476. Nos casos que não forem da integração, chamar o método
     * generateEventoDocumentoServicoByIdPreManifesto(Long idManifesto, Short cdEvento, String tpDocumento)
     *
     * @param idManifesto id do Manifesto em questao
     * @param cdEvento    chave codigo do evento
     * @param tpDocumento tipo do documento
     * @param dataHora    necessário para Integração
     */
    public void generateEventoDocumentoServicoByIdPreManifesto(Long idManifesto, Short cdEvento, String tpDocumento, DateTime dataHora) {
        Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
        List<PreManifestoDocumento> listDoctosServicoPreManifesto = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(idManifesto);
        for (PreManifestoDocumento preManifestoDocumento : listDoctosServicoPreManifesto) {
            String strPreManifesto = preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(preManifestoDocumento.getManifesto().getNrPreManifesto().toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, preManifestoDocumento.getDoctoServico().getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHora, null, filialUsuarioLogado.getSiglaNomeFilial(), tpDocumento);
        }
    }

    /**
     * Realiza a operacao de salvar os registros da tela de finalizar pre manifestos
     *
     * @param criteria
     * @param itemsSemIdentificacao
     * @param itemsSemIdentificacaoConfig
     * @param itemsComIdentificacao
     * @param itemsComIdentificacaoConfig
     * @return
     */
    public java.io.Serializable storeFinalizarCarregamentoPreManifesto(
            TypedFlatMap criteria,
            ItemList itemsSemIdentificacao,
            ItemListConfig itemsSemIdentificacaoConfig,
            ItemList itemsComIdentificacao,
            ItemListConfig itemsComIdentificacaoConfig
    ) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        return this.storeFinalizarCarregamentoPreManifesto(
                criteria,
                itemsSemIdentificacao,
                itemsSemIdentificacaoConfig,
                itemsComIdentificacao,
                itemsComIdentificacaoConfig,
                dataHoraAtual
        );
    }

    /**
     * Realiza a operacao de salvar os registros da tela de finalizar pre manifestos
     * Alteração na assinatura do método (adicionado dataHora) para
     * atender a solicitação CQPRO00005476 da Integração.
     *
     * @param criteria
     * @param itemsSemIdentificacao
     * @param itemsSemIdentificacaoConfig
     * @param itemsComIdentificacao
     * @param itemsComIdentificacaoConfig
     * @param dataHora
     * @return
     */
    public java.io.Serializable storeFinalizarCarregamentoPreManifesto(
            TypedFlatMap criteria,
            ItemList itemsSemIdentificacao,
            ItemListConfig itemsSemIdentificacaoConfig,
            ItemList itemsComIdentificacao,
            ItemListConfig itemsComIdentificacaoConfig,
            DateTime dataHora
    ) {
        //Captura os objetos...
        Manifesto manifesto = manifestoService.findById(criteria.getLong("masterId"));
        Filial filial = new Filial();
        filial.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());

        //Edita o manifesto
        manifesto.setTpStatusManifesto(new DomainValue(ConstantesExpedicao.TP_STATUS_MANIFESTO_CARREGAMENTO_CONCLUIDO));

        //Busca carregamentoPreManifesto com a data de início de Carregamento maior.
        CarregamentoPreManifesto carregamentoPreManifesto = (CarregamentoPreManifesto) Collections.max(
                manifesto.getCarregamentoPreManifestos(), new Comparator<CarregamentoPreManifesto>() {
                    public int compare(CarregamentoPreManifesto obj1, CarregamentoPreManifesto obj2) {
                        return obj1.getDhInicioCarregamento().compareTo(obj2.getDhInicioCarregamento());
                    }
                });
        carregamentoPreManifesto.setDhFimCarregamento(dataHora);

        //Gera um novo evento manifesto...
        EventoManifesto eventoManifesto = new EventoManifesto();
        eventoManifesto.setDhEvento(dataHora);
        eventoManifesto.setTpEventoManifesto(new DomainValue(ConstantesExpedicao.TP_EVENTO_MANIFESTO_FIM_CARREGAMENTO));
        eventoManifesto.setFilial(filial);
        eventoManifesto.setManifesto(manifesto);
        eventoManifesto.setUsuario(usuario);

        //Persiste objetos...
        carregamentoPreManifestoService.store(carregamentoPreManifesto);
        manifestoService.storeBasic(manifesto);
        eventoManifestoService.storeBasic(eventoManifesto);

        ControleCarga controleCarga = controleCargaService.findById(criteria.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA));

        //Verifica o tipo de controle de carga para gerar um codigo de evento especifico para o mesmo...
        Short cdEvento = null;
        String tpDocumento = null;
        if (ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM.equalsIgnoreCase(controleCarga.getTpControleCarga().getValue())) {
            cdEvento = ConstantesSim.EVENTO_FIM_CARREGAMENTO_VIAGEM;
            tpDocumento = ConstantesEventosDocumentoServico.TP_DOCTO_PRE_MANIFESTO_VIAGEM;
        } else {
            cdEvento = ConstantesSim.EVENTO_FIM_CARREGAMENTO_ENTREGA;
            tpDocumento = ConstantesEventosDocumentoServico.TP_DOCTO_PRE_MANIFESTO_ENTREGA;
        }

        //TODO lucianos utilizar implementação do Granja
        this.generateEventoDocumentoServicoByIdPreManifesto(manifesto.getIdManifesto(), cdEvento, tpDocumento, dataHora);

        generateEventoVolume(manifesto.getIdManifesto(), cdEvento, ConstantesSim.TP_SCAN_LMS);

        generateEventoDispositivo(manifesto.getIdManifesto(), cdEvento, ConstantesSim.TP_SCAN_LMS);

        //Objetos necessarios para a iteracao dos filhos...
        CarregamentoDescarga carregamentoDescarga = this.findById(criteria.getLong(ConsCarregamentoDescarga.ID_CARREGAMENTO_DESCARGA));

        //Salva a list de itemsSemIdentificacao...
        storeItensSemIdentificacao(itemsSemIdentificacao, itemsSemIdentificacaoConfig, filial, carregamentoPreManifesto, controleCarga, carregamentoDescarga);

        //Salva a list de itemsComIdentificacao...
        storeItensComIdentificacao(itemsComIdentificacao, itemsComIdentificacaoConfig, filial, carregamentoPreManifesto, controleCarga, carregamentoDescarga);

        return LongUtils.ZERO;
    }

    private void storeItensComIdentificacao(ItemList itemsComIdentificacao, ItemListConfig itemsComIdentificacaoConfig, Filial filial,
                                            CarregamentoPreManifesto carregamentoPreManifesto, ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga) {

        if (itemsComIdentificacao.hasItems()) {
            DispCarregIdentificado dispCarregIdentificado = null;
            removeDispositivosDivergencias(itemsComIdentificacao, controleCarga.getIdControleCarga(), itemsComIdentificacaoConfig);
            for (Iterator<DispCarregIdentificado> iter = itemsComIdentificacao.iterator(null, itemsComIdentificacaoConfig); iter.hasNext(); ) {
                dispCarregIdentificado = iter.next();

                DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
                dispCarregIdentificado.setDispositivoUnitizacao(dispositivoUnitizacao);

                dispCarregIdentificado.setCarregamentoDescarga(carregamentoDescarga);
                dispCarregIdentificado.setCarregamentoPreManifesto(carregamentoPreManifesto);

                this.atualizaEstoqueDispIdentificado(dispositivoUnitizacao, controleCarga, filial);
            }
        }
        dispCarregIdentificadoService.storeDispCarregIdentificado(itemsComIdentificacao.getNewOrModifiedItems());
    }

    private void storeItensSemIdentificacao(ItemList itemsSemIdentificacao, ItemListConfig itemsSemIdentificacaoConfig, Filial filial,
                                            CarregamentoPreManifesto carregamentoPreManifesto, ControleCarga controleCarga, CarregamentoDescarga carregamentoDescarga) {

        if (itemsSemIdentificacao.hasItems()) {
            DispCarregDescQtde dispCarregDescQtde = null;
            for (Iterator<DispCarregDescQtde> iter = itemsSemIdentificacao.iterator(null, itemsSemIdentificacaoConfig); iter.hasNext(); ) {
                dispCarregDescQtde = iter.next();

                Long idTipoDispositivoUnitizacao = dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao();

                dispCarregDescQtde.setTipoDispositivoUnitizacao(tipoDispositivoUnitizacaoService.findById(idTipoDispositivoUnitizacao));

                dispCarregDescQtde.setCarregamentoDescarga(carregamentoDescarga);
                dispCarregDescQtde.setCarregamentoPreManifesto(carregamentoPreManifesto);

                this.atualizaEstoqueDispositivoQtde(dispCarregDescQtde, controleCarga, filial);
            }
        }
        dispCarregDescQtdeService.storeDispCarregDescQtde(itemsSemIdentificacao.getNewOrModifiedItems());
    }

    private void generateEventoDispositivo(Long idManifesto, Short cdEvento, String tpScan) {

        List<DispCarregIdentificado> listaDCargIdent = dispCarregIdentificadoService.findDispCarregIdentificadoByIdManifesto(idManifesto);

        if (listaDCargIdent != null && !listaDCargIdent.isEmpty()) {
            List<DispositivoUnitizacao> listDispositivoUnitizacao = new ArrayList<DispositivoUnitizacao>();

            for (DispCarregIdentificado dispCarregIdentificado : listaDCargIdent) {
                listDispositivoUnitizacao.add(dispCarregIdentificado.getDispositivoUnitizacao());
            }
            eventoDispositivoUnitizacaoService.generateEventoDispositivo(listDispositivoUnitizacao, cdEvento, tpScan);
        }

    }

    private void generateEventoVolume(Long idManifesto, Short cdEvento, String tpScan) {
        //[CQ25050] Gera eventos de volume
        List<PreManifestoVolume> preManifestoVolumeList = preManifestoVolumeService.findVolumesNaoEntreguesByManifesto(idManifesto);

        if (preManifestoVolumeList != null && !preManifestoVolumeList.isEmpty()) {
            List<VolumeNotaFiscal> volumeNotaFiscalList = new ArrayList<VolumeNotaFiscal>();

            for (PreManifestoVolume preManifestoVolume : preManifestoVolumeList) {
                volumeNotaFiscalList.add(preManifestoVolume.getVolumeNotaFiscal());
            }

            eventoVolumeService.storeEventoVolume(volumeNotaFiscalList, cdEvento, tpScan);
        }


    }

    /**
     * Remove do banco de dados os dispositivos que são divergentes entre o banco e a lista da sessão, alem de liberar os itens da sessão para
     * serem salvos no banco.
     *
     * @param itemsComIdentificacao
     * @param idControleCarga
     * @param itemsComIdentificacaoConfig
     */
    private void removeDispositivosDivergencias(ItemList itemsComIdentificacao, Long idControleCarga, ItemListConfig itemsComIdentificacaoConfig) {
        DispCarregIdentificado dispCarregIdentificado = null;
        List<DispositivoUnitizacao> lstDispUnit = dispositivoUnitizacaoService.findListaDispositivoUnitizacaoCarregadoControleCarga(idControleCarga);

        for (Iterator<DispCarregIdentificado> iter = itemsComIdentificacao.iterator(null, itemsComIdentificacaoConfig); iter.hasNext(); ) {
            dispCarregIdentificado = iter.next();
            DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
            if (lstDispUnit.contains(dispositivoUnitizacao)) {
                //Remove da lista do banco os dispositivos que não foram removidos.
                lstDispUnit.remove(dispositivoUnitizacao);
                //Remove da sessão os itens que já existem no banco.
                iter.remove();
            }
        }
        for (DispositivoUnitizacao dispUnit : lstDispUnit) {
            DispCarregIdentificado carregIdentificado = dispCarregIdentificadoService.findByDispositivoUnitizacaoAndControleCarga(idControleCarga, dispUnit.getIdDispositivoUnitizacao());
            //Apaga os itens do banco que foram removidos na tela.
            dispCarregIdentificadoService.removeById(carregIdentificado.getIdDispCarregIdentificado());
        }

    }

    /**
     * Método para cancelar o inicio de descarga.
     *
     * @param idControleCarga
     * @param idMotivoCancelamento
     * @param obMotivoCancelamentoDescarga
     * @author Rodrigo Antunes
     */
    public void executeCancelarInicioDescarga(Long idControleCarga, Long idMotivoCancelamento, String obMotivoCancelamentoDescarga) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
        Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();

        if (!ConsCarregamento.TP_CONTROLE_CARGA_COLETA_ENTREGA.equals(controleCarga.getTpControleCarga().getValue())
                || !ConsCarregamento.TP_STATUS_CONTROLE_CARGA_EM_DESCARGA.equals(controleCarga.getTpStatusControleCarga().getValue())) {
            throw new BusinessException("LMS-03001");
        }

        //TODO: Adicionar para buscar posto avançado, caso não tenha, buscar o posto avançado que seja null (verificar se será necessário cria uma consulta nova)
        //TODO: Deveria ter como buscar o posto avançado da sessao e colocar no lugar do NULL do seguinte FIND
        List<CarregamentoDescarga> carregamentoDescargaList = this.findByIdControleCargaIdFilialIdPostoAvancadoTpOperacao(idControleCarga, idFilialSessao, null, "D");

        CarregamentoDescarga carregamentoDescarga = null;
        // atualiza o carregamento de descarga
        if (carregamentoDescargaList != null && !carregamentoDescargaList.isEmpty()) {
            carregamentoDescarga = carregamentoDescargaList.get(0);
            DomainValue dv = new DomainValue("C");
            carregamentoDescarga.setTpStatusOperacao(dv);
            carregamentoDescarga.setDhCancelamentoOperacao(dataHoraAtual);
            MotivoCancelDescarga motivoCancelDescarga = motivoCancelDescargaService.findById(idMotivoCancelamento);
            carregamentoDescarga.setMotivoCancelDescarga(motivoCancelDescarga);
            carregamentoDescarga.setObCancelamento(obMotivoCancelamentoDescarga);
            carregamentoDescarga.setUsuarioByIdUsuarioFinalizado(SessionUtils.getUsuarioLogado());
            if (carregamentoDescarga.getBox() != null) {
                boxService.storeDesocuparBox(carregamentoDescarga.getBox().getIdBox());
            }

            carregamentoDescarga.setBox(null);
            // atualiza o carregamentoDescarga
            this.store(carregamentoDescarga);
        } else {
            // Lança uma exceção, pois é obrigatório ter um carregamentoDescarga
            // A mensagem mostrada não é nem um pouco intuitiva. Além disso, se o usuario estiver logado em uma filial diferente
            // do CC a ser cancelado, também é apresentada a mensagem abaixo, pois a consulta de CarregamentoDescarga acima, leva
            // em consideração a filial do usuario logado.
            throw new BusinessException("LMS-03002");
        }
        // atualiza o controle de carga
        controleCargaService.generateEventoChangeStatusControleCarga(
                controleCarga.getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(),
                "CD",
                null,
                controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                configuracoesFacade.getMensagem("cancelamentoInicioDescarga"),
                null,
                null, null, null, null, null, null, null
        );

        // atualiza registro na tabela DESCARGA_MANIFESTO
        // busca a descargaManifesto baseado no carregamentodescarga
        List<DescargaManifesto> descargaManifestosList = carregamentoDescarga.getDescargaManifestos();
        // para cada item de descargaManifesto, atualizar o dhCancelamentoDescarga
        for (DescargaManifesto descargaManifesto : descargaManifestosList) {
            descargaManifesto.setDhCancelamentoDescarga(dataHoraAtual);
            // para cada descargaManifesto, gerar um registro em evento_manifesto
            Manifesto manifesto = descargaManifesto.getManifesto();

            if (manifesto != null && manifesto.getIdManifesto() != null) {
                ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();

                //Como o idManifesto e idManifestoEntrega são os mesmos, pode ser gravado o manifesto ao invés do manifestoEntrega
                if (manifestoEntrega != null && manifestoEntrega.getIdManifestoEntrega() != null) {

                    // atualiza o manifesto
                    DomainValue tpStatusManifestoDV = new DomainValue("AD");
                    manifesto.setTpStatusManifesto(tpStatusManifestoDV);
                    manifestoService.storeBasic(manifesto);

                    // gera eventoManifesto
                    EventoManifesto eventoManifesto = new EventoManifesto();
                    eventoManifesto.setManifesto(manifesto);
                    eventoManifesto.setDhEvento(dataHoraAtual);
                    DomainValue tpEventoManifestoDV = new DomainValue("CA");
                    eventoManifesto.setTpEventoManifesto(tpEventoManifestoDV);
                    eventoManifesto.setFilial(SessionUtils.getFilialSessao());
                    eventoManifesto.setUsuario(SessionUtils.getUsuarioLogado());
                    // grava um novo registro eventoManifesto
                    eventoManifestoService.store(eventoManifesto);
                }

                String tpDocumento = null;
                if (manifesto.getTpManifesto().getValue().equalsIgnoreCase(ConstantesExpedicao.TP_MANIFESTO_VIAGEM)) {
                    tpDocumento = "MAV";
                } else {
                    tpDocumento = "MAE";
                }
                this.generateEventoDocumentoServicoByIdManifesto(
                        manifesto.getIdManifesto(),
                        ConstantesSim.EVENTO_CANCELAMENTO_DESCARGA_ENTREGA,
                        tpDocumento,
                        new Short[]{ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA, ConstantesSim.CD_MERCADORIA_EM_DESCARGA});

                eventoDispositivoUnitizacaoService.generateEventoByManifesto(
                        manifesto.getIdManifesto(),
                        ConstantesSim.EVENTO_CANCELAMENTO_DESCARGA_ENTREGA,
                        ConstantesSim.TP_SCAN_LMS, null);

                eventoVolumeService.generateEventoByManifesto(
                        manifesto.getIdManifesto(),
                        ConstantesSim.EVENTO_CANCELAMENTO_DESCARGA_ENTREGA,
                        ConstantesSim.TP_SCAN_LMS);
            }
            // atualiza a descargaManifesto
            descargaManifestoService.store(descargaManifesto);
        }

        // gerar evento_manifesto_coleta
        List<ManifestoColeta> manifestoColetasList = controleCarga.getManifestoColetas();
        if (manifestoColetasList != null) {
            for (ManifestoColeta manifestoColeta : manifestoColetasList) {
                EventoManifestoColeta eventoManifestoColeta = new EventoManifestoColeta();
                eventoManifestoColeta.setDhEvento(dataHoraAtual);
                eventoManifestoColeta.setManifestoColeta(manifestoColeta);
                DomainValue tpEventoManifestoColeta = new DomainValue("CA");
                eventoManifestoColeta.setTpEventoManifestoColeta(tpEventoManifestoColeta);
                // grava um novo registro eventoManifestoColeta
                eventoManifestoColetaService.store(eventoManifestoColeta);

                DomainValue tpStatusManifestoColeta = new DomainValue("AD");
                manifestoColeta.setTpStatusManifestoColeta(tpStatusManifestoColeta);
                // atualiza o manifestoColeta
                manifestoColetaService.store(manifestoColeta);

                List<PedidoColeta> pedidoColetas = manifestoColeta.getPedidoColetas();
                if (pedidoColetas != null) {
                    for (PedidoColeta pedidoColeta : pedidoColetas) {
                        if (ConsManifesto.VL_EM_DESCARGA.equalsIgnoreCase(pedidoColeta.getTpStatusColeta().getValue())) {
                            Map<String, String> ocorrenciaColetaMap = new HashMap<String, String>(ConsCarregamentoDescarga.SIZE_2);
                            ocorrenciaColetaMap.put("tpSituacao", "A");
                            ocorrenciaColetaMap.put("tpEventoColeta", "CD");

                            List<OcorrenciaColeta> ocorrenciaColetaList = ocorrenciaColetaService.find(ocorrenciaColetaMap);
                            OcorrenciaColeta ocorrenciaColeta = null;
                            if (ocorrenciaColetaList != null && !ocorrenciaColetaList.isEmpty()) {
                                ocorrenciaColeta = ocorrenciaColetaList.get(0);
                            } else {
                                throw new BusinessException("LMS-03006");
                            }

                            if (ocorrenciaColeta == null || ocorrenciaColeta.getIdOcorrenciaColeta() == null) {
                                throw new BusinessException("LMS-03006");
                            }

                            EventoColeta eventoColeta = new EventoColeta();
                            eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                            eventoColeta.setDhEvento(dataHoraAtual);
                            eventoColeta.setDsDescricao(configuracoesFacade.getMensagem("cancelamentoDescarga"));
                            eventoColeta.setPedidoColeta(pedidoColeta);
                            DomainValue tpEventoColeta = new DomainValue("CD");
                            eventoColeta.setTpEventoColeta(tpEventoColeta);
                            eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                            // grava um novo eventoColeta
                            eventoColetaService.store(eventoColeta);
                            // grava no topic de eventos de pedido coleta
                            eventoColetaService.storeMessageTopic(eventoColeta);

                            DomainValue tpStatusColeta = new DomainValue("AD");
                            pedidoColeta.setTpStatusColeta(tpStatusColeta);
                            // atualiza a coleta
                            pedidoColetaService.store(pedidoColeta);
                        }
                    }
                }
            }
        }

        //Coloca dhFimOperacao em equipeOperacao.
        List<EquipeOperacao> equipeOperacoes = equipeOperacaoService.findEquipeOperacaoByIdCarregamentoDescarga(carregamentoDescarga.getIdCarregamentoDescarga(), true, "D");
        for (EquipeOperacao equipeOperacao : equipeOperacoes) {
            equipeOperacao.setDhFimOperacao(dataHoraAtual);
            equipeOperacaoService.store(equipeOperacao);
        }

        //Regra 1.10 da ET 03.01.01.10 - Cancelamento Início de Descarga
        if (controleCarga.getMeioTransporteByIdTransportado() != null) {
            EventoMeioTransporte eventoMeioTransporte = eventoMeioTransporteService.findLastEventoMeioTransporteToMeioTransporte(controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(), idFilialSessao, idControleCarga, "EMDE");
            if (eventoMeioTransporte != null && eventoMeioTransporte.getDhFimEvento() == null) {
                eventoMeioTransporteService.removeById(eventoMeioTransporte.getIdEventoMeioTransporte());
            }
        }

        //Regra 1.11 da ET 03.01.01.10 - Cancelamento Início de Descarga
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            EventoMeioTransporte eventoSemiReboque = eventoMeioTransporteService.findLastEventoMeioTransporteToMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(), idFilialSessao, idControleCarga, "EMDE");
            if (eventoSemiReboque != null && eventoSemiReboque.getDhFimEvento() == null) {
                eventoMeioTransporteService.removeById(eventoSemiReboque.getIdEventoMeioTransporte());
            }

        }

        //-	ID_MEIO_TRANSPORTE = CONTROLE_CARGA.ID_TRANSPORTADO
        //-	ID_CONTROLE_CARGA = Controle de Cargas em questão
        //-	TP_SITUACAO_MEIO_TRANSPORTE = “EMDE”
        //-	ID_FILIAL = filial do usuário logado
    }

    /**
     * Salva a tela de finalizar carregamento
     *
     * @param criteria
     * @param itemsFotos
     * @return
     */
    public void storeFinalizarCarregamento(CarregamentoDescarga carregamentoDescarga, TypedFlatMap criteria, ItemList itemsFotos, ItemListConfig itemsFotosConfig) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
        this.storeFinalizarCarregamento(carregamentoDescarga, criteria, itemsFotos, itemsFotosConfig, dataHoraAtual);

        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), ConsCarregamento.INDICADOR_CIOT, false, true);
        if (conteudoParametroFilial != null && ConsCarregamento.SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
        	if (controleDeCargaTemProprietario(criteria.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA))){
        		ciotService.executeSolicitacaoCIOT(criteria.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA));
        	}
        }
    }
    
    /**
     * Conforme acordado com a área de negócio(Leonardo Contreras), 
     * caso o controle de carga não tenha o proprietário, o mesmo 
     * não fará a solicitação do CIOT, dessa forma evita que ocorra 
     * erro de NullPointerException, quando o serviço do CIOT chamar 
     * o LMS, pelo endpoint /expedicao/ciotServiceRest/findDados
     * 
     * @param idControleCarga
     * @return
     */
    private boolean controleDeCargaTemProprietario(Long idControleCarga) {
       	ControleCarga controleCarga = controleCargaDAO.findDadosParaCiot(idControleCarga);
		return (controleCarga != null && controleCarga.getProprietario() != null && 
				controleCarga.getProprietario().getIdProprietario() != null);
	}

    /**
     * Salva a tela de finalizar carregamento
     * Assinatura modificada para a Integração.
     *
     * @param criteria
     * @param itemsFotos
     * @return
     */
    public void storeFinalizarCarregamento(CarregamentoDescarga carregamentoDescarga, TypedFlatMap criteria, ItemList itemsFotos, ItemListConfig itemsFotosConfig, DateTime dataHora) {
        //Buscando objetos...
        Long idControleCarga = criteria.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        if (controleCarga.getMeioTransporteByIdTransportado() == null) {
            throw new BusinessException("LMS-05143");
        }
        //Valida se o meio de transporte deve possuir um semi-reboque...
        if (controleCarga.getMeioTransporteByIdSemiRebocado() == null) {
            ModeloMeioTransporte modeloMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getModeloMeioTransporte();
            if (modeloMeioTransporte != null && modeloMeioTransporte.getTipoMeioTransporte() != null && modeloMeioTransporte.getTipoMeioTransporte().getTipoMeioTransporte() != null) {
                throw new BusinessException("LMS-05157");
            }
        }

        if (controleCarga.getMotorista() == null) {
            throw new BusinessException("LMS-05144");
        }

        if (controleCarga.getTpRotaViagem() != null &&
                (ConstantesExpedicao.TP_ROTA_VIAGEM_EXPRESSA.equals(controleCarga.getTpRotaViagem().getValue())
                        || ConstantesExpedicao.TP_ROTA_VIAGEM_EXPRESSA_CLIENTE.equals(controleCarga.getTpRotaViagem().getValue()))
                &&
                (controleCarga.getVlFreteCarreteiro() == null || controleCarga.getVlFreteCarreteiro().compareTo(BigDecimal.ZERO) == 0)) {
            //LMSA-1261
            throw new BusinessException("LMS-05413");

        }

        //LMSA-2470
        if (getCarregamentoDescargaDAO().validateCarregamentoFinalizado(idControleCarga, SessionUtils.getFilialSessao().getIdFilial())) {
            throw new BusinessException("LMS-05025");
        }

        //LMS-3906
        storeCarregamentoDescargaVolumes(carregamentoDescarga, controleCarga);

        //Verifica o tipo controle carga...
        if (!"C".equals(controleCarga.getTpControleCarga().getValue()) &&
                criteria.getList("lacreControleCarga.idsLacreControleCarga") == null) {
            throw new BusinessException("LMS-05149");
        }

        //Verifica se algums dos parametros do SMP existem...
        parametroGeralService.findByNomeParametro("SETOR_SMP", false);
        parametroGeralService.findByNomeParametro("FONE_SETOR_SMP", false);
        parametroGeralService.findByNomeParametro("ENDERECO_EMAIL_CEMOP", false);

        List<Map<String, Object>> lacres;
        List<Map<String, Object>> lacresRemovidos;
        try {
            lacres = criteria.getList("lacreControleCarga.idsLacreControleCarga");
            lacresRemovidos = criteria.getList("lacresRemovidos");

            Filial filial = SessionUtils.getFilialSessao();
            Usuario usuario = SessionUtils.getUsuarioLogado();
            DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();

            //Carregamento descarga...
            carregamentoDescarga = this.findById(carregamentoDescarga.getIdCarregamentoDescarga());
            carregamentoDescarga.setDhFimOperacao(dataHora);
            carregamentoDescarga.setTpStatusOperacao(new DomainValue("F"));
            carregamentoDescarga.setObOperacao(criteria.getString(ConsCarregamentoDescarga.CARREGAMENTO_DESCARGA_OB_OPERACAO));

            // LMS-4460 - Baixa automática de RNC
            // Busca as RNCs aptas para o encerramento
            List<OcorrenciaNaoConformidade> listaOcoRNCsAptasEncerramento = ocorrenciaNaoConformidadeService
                    .buscaRNCsAptasEncerramento(controleCarga.getIdControleCarga(), new String[]{ConstantesSim.TP_SCAN_FISICO});

            if (listaOcoRNCsAptasEncerramento != null && !listaOcoRNCsAptasEncerramento.isEmpty()) {
                // Realiza o fechamento das Ocorrencias
                ocorrenciaNaoConformidadeService.fechaOcoRNC(listaOcoRNCsAptasEncerramento, controleCarga.getIdControleCarga());
            }

            //Finalizando a equipe atual deste carregamento descarga...
            List<EquipeOperacao> equipeOperacoes = equipeOperacaoService.findEquipeByIdControleCarga(carregamentoDescarga.getIdCarregamentoDescarga());
            EquipeOperacao equipeOperacao = Collections.max(equipeOperacoes, new Comparator<EquipeOperacao>() {
                public int compare(EquipeOperacao obj1, EquipeOperacao obj2) {
                    return obj1.getDhInicioOperacao().compareTo(obj2.getDhInicioOperacao());
                }
            });
            equipeOperacao.setDhFimOperacao(dataHora);
            equipeOperacaoService.store(equipeOperacao);

            //ControleCarga...
            controleCargaService.generateEventoChangeStatusControleCarga(idControleCarga, filial.getIdFilial(),
                    "FC", dataHora, equipeOperacao.getIdEquipeOperacao(),
                    controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                    configuracoesFacade.getMensagem(ConsCarregamentoDescarga.FIM_CARREGAMENTO),
                    null, null, null, null, null, null, null, null);
            BigDecimal percent = BigDecimalUtils.HUNDRED;
            percent = percent.subtract(new BigDecimal(criteria.getInteger(ConsCarregamentoDescarga.CONTROLE_CARGA_PC_OCUPACAO_INFORMADO)));
            controleCarga.setPcOcupacaoInformado(percent);

            //Verifica se existe algum evento EMCA para o meio de transporte...
            EventoMeioTransporte eventoMeioTransporte = eventoMeioTransporteService.findLastEventoMeioTransporteToMeioTransporte(
                    controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                    SessionUtils.getFilialSessao().getIdFilial(),
                    controleCarga.getIdControleCarga(),
                    "EMCA"
            );

            if (eventoMeioTransporte == null) {
                //Caso não exista gera um novo evento de meio de transporte com data de inicio e fim
                eventoMeioTransporte = populaEvento(
                        "EMCA",
                        dataHora,
                        dataHora,
                        carregamentoDescarga.getBox(),
                        controleCarga,
                        SessionUtils.getFilialSessao(),
                        controleCarga.getMeioTransporteByIdTransportado(),
                        SessionUtils.getUsuarioLogado()
                );

                //Gerando evento de rastreabilidade...
                eventoMeioTransporteService.generateEvent(eventoMeioTransporte, dataHora);
                getCarregamentoDescargaDAO().getAdsmHibernateTemplate().flush();
            }

            // Gera evento PAOP para meio transporte.
            EventoMeioTransporte eventoPAOPMeioTransporte = populaEvento(
                    "PAOP",
                    dataHora,
                    null,
                    carregamentoDescarga.getBox(),
                    controleCarga,
                    SessionUtils.getFilialSessao(),
                    controleCarga.getMeioTransporteByIdTransportado(),
                    SessionUtils.getUsuarioLogado()
            );
            eventoMeioTransporteService.generateEvent(eventoPAOPMeioTransporte, dataHora);

            //Verifica se o controle de carga possui um semi-reboque.
            if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                //Verifica se existe algum evento para o semi-reboque...
                EventoMeioTransporte eventoSemiReboque = eventoMeioTransporteService.findLastEventoMeioTransporteToMeioTransporte(
                        controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(),
                        SessionUtils.getFilialSessao().getIdFilial(),
                        controleCarga.getIdControleCarga(),
                        "EMCA"
                );

                if (eventoSemiReboque == null) {
                    //Caso nao exista gera um novo evento de meio de semi-reboque com data de inicio e fim
                    eventoSemiReboque = populaEvento(
                            "EMCA",
                            dataHora,
                            dataHora,
                            carregamentoDescarga.getBox(),
                            controleCarga,
                            SessionUtils.getFilialSessao(),
                            controleCarga.getMeioTransporteByIdSemiRebocado(),
                            SessionUtils.getUsuarioLogado()
                    );

                    //Gerando evento de rastreabilidade...
                    eventoMeioTransporteService.generateEvent(eventoSemiReboque, dataHora);
                    getCarregamentoDescargaDAO().getAdsmHibernateTemplate().flush();
                }

                // Gera evento PAOP para semi-reboque.
                EventoMeioTransporte eventoPAOPSemiReboque = populaEvento(
                        "PAOP",
                        dataHora,
                        null,
                        carregamentoDescarga.getBox(),
                        controleCarga,
                        SessionUtils.getFilialSessao(),
                        controleCarga.getMeioTransporteByIdSemiRebocado(),
                        SessionUtils.getUsuarioLogado()
                );
                eventoMeioTransporteService.generateEvent(eventoPAOPSemiReboque, dataHora);
            }

            //Lacres...
            List<LacreControleCarga> listLacreControleCarga = new ArrayList<LacreControleCarga>();
            adicionarListaLacreControleCarga(criteria, dataHora, controleCarga, lacres, filial, usuario, listLacreControleCarga);

            //LMS-2594
            removerObjetosLacresRemovidos(lacresRemovidos);

            //Busca todas as fotos setados no carregamentoDescarga
            List<FotoCarregmtoDescarga> listfotoCarregmtoDescarga = new ArrayList<FotoCarregmtoDescarga>();
            if (itemsFotos.hasItems()) {
                for (Iterator<FotoCarregmtoDescarga> iter = itemsFotos.iterator(null, itemsFotosConfig); iter.hasNext(); ) {
                    FotoCarregmtoDescarga fotoCarregmtoDescarga = iter.next();
                    fotoCarregmtoDescarga.setIdFotoCarregmtoDescarga(null);
                    fotoCarregmtoDescarga.setCarregamentoDescarga(carregamentoDescarga);
                    fotoService.store(fotoCarregmtoDescarga.getFoto());
                    listfotoCarregmtoDescarga.add(fotoCarregmtoDescarga);
                }
            }

            //Caso nao exista nenhum dado setado na lista de lacres...
            if (!listLacreControleCarga.isEmpty()) {
                lacreControleCargaService.storeLacresControleCarga(listLacreControleCarga);
            }

            fotoCarregmtoDescargaService.storeFotosCarregmtoDescarga(listfotoCarregmtoDescarga);

            controleCargaService.store(controleCarga);
            this.store(carregamentoDescarga);

            if (carregamentoDescarga.getBox() != null) {
                boxService.storeDesocuparBox(carregamentoDescarga.getBox().getIdBox());
            }

            List<Map<String, Object>> listaDocumentos = criteria.getList(ConsCarregamentoDescarga.DOCTOS_PRIORIZACAO_EMBARQUE);
            if (listaDocumentos != null) {
                for (Map<String, Object> map : listaDocumentos) {
                    JustificativaDoctoNaoCarregado jdnc = new JustificativaDoctoNaoCarregado();
                    jdnc.setCarregamentoDescarga(carregamentoDescarga);
                    jdnc.setDoctoServico(doctoServicoService.findById((Long) map.get(ConsCarregamentoDescarga.ID_DOCTO_SERVICO)));
                    jdnc.setDsJustificativa((String) map.get(ConsCarregamentoDescarga.DS_JUSTIFICATIVA));
                    jdnc.setDhJustificativa(dhAtual);
                    jdnc.setUsuarioJustificativa(usuario);
                    jdnc.setRegistroPriorizacaoDocto(registroPriorizacaoDoctoService.findById((Long) map.get(ConsCarregamentoDescarga.ID_REGISTRO_PRIORIZACAO_DOCTO)));
                    justificativaDoctoNaoCarregadoService.store(jdnc);
                }
            }


        } catch (RuntimeException runEx) {
            this.rollbackMasterState(carregamentoDescarga, false, runEx, true);
            itemsFotos.rollbackItemsState();
            throw runEx;
        }
    }

    private void adicionarListaLacreControleCarga(TypedFlatMap criteria, DateTime dataHora, ControleCarga controleCarga, List<Map<String, Object>> lacres, Filial filial, Usuario usuario, List<LacreControleCarga> listLacreControleCarga) {
        LacreControleCarga lacreControleCarga;
        if (lacres != null) {
            for (Map<String, Object> mapLacre : lacres) {
                if (mapLacre.get(ConsCarregamentoDescarga.ID_LACRE_CONTROLE_CARGA) != null) {
                    lacreControleCarga = lacreControleCargaService.findById((Long) mapLacre.get(ConsCarregamentoDescarga.ID_LACRE_CONTROLE_CARGA));
                } else {
                    lacreControleCarga = new LacreControleCarga();
                    lacreControleCarga.setDhInclusao(dataHora);
                    lacreControleCarga.setFilialByIdFilialInclusao(filial);
                    lacreControleCarga.setUsuarioByIdFuncInclusao(usuario);
                }

                String nrLacres = (String) mapLacre.get("nrLacres");
                lacreControleCarga.setNrLacres(nrLacres);
                lacreControleCarga.setTpStatusLacre(new DomainValue(ConsManifesto.VL_STATUS_MANIFESTO_FECHADO));
                lacreControleCarga.setDsLocalInclusao(null);
                lacreControleCarga.setControleCarga(controleCarga);
                lacreControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
                lacreControleCarga.setObInclusaoLacre(criteria.getString(ConsCarregamentoDescarga.CARREGAMENTO_DESCARGA_OB_OPERACAO));

                listLacreControleCarga.add(lacreControleCarga);
            }
        }
    }

    private void removerObjetosLacresRemovidos(List<Map<String, Object>> lacresRemovidos) {
        if (lacresRemovidos != null) {
            for (Map<String, Object> lacreRemocao : lacresRemovidos) {
                if (lacreRemocao.get(ConsCarregamentoDescarga.ID_LACRE_CONTROLE_CARGA) != null) {
                    lacreControleCargaService.removeById((Long) lacreRemocao.get(ConsCarregamentoDescarga.ID_LACRE_CONTROLE_CARGA));
                }
            }
        }
    }

    /**
     * @param carregamentoDescarga
     * @param controleCarga
     */
    private void storeCarregamentoDescargaVolumes(CarregamentoDescarga carregamentoDescarga, ControleCarga controleCarga) {
        //LMS-3906
        List<Manifesto> listManifesto = manifestoService.findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), null, null, null);
        for (Manifesto manifesto : listManifesto) {
            if (manifesto.getFilialByIdFilialOrigem().getIdFilial().compareTo(SessionUtils.getFilialSessao().getIdFilial()) == 0) {
                List<PreManifestoVolume> listPreManifestoVolume = preManifestoVolumeService.findbyIdManifesto(manifesto.getIdManifesto());
                for (PreManifestoVolume preManifestoVolume : listPreManifestoVolume) {
                    storeValidateCarregamentoDescargaVolume(
                            controleCarga,
                            carregamentoDescarga,
                            preManifestoVolume.getVolumeNotaFiscal(), "C");
                }
            }
        }
    }

    /**
     * Gera uma lista com os dados dos documentos de servico que possuem atrasos na DPE e
     * que estejam na rota deste controle de carga.
     *
     * @param idControleCarga
     * @return
     */
    public List<TypedFlatMap> validateDoctoServico(Long idControleCarga) {
        List<TypedFlatMap> doctosServico = findDoctoServicoWithServicoPrioritario(idControleCarga);
        doctosServico.addAll(findDoctoServicoWithDpeAtrasado(idControleCarga));
        ordenaListaDoctoServico(doctosServico);
        return doctosServico;
    }

    /**
     * @param doctosServico
     */
    private void ordenaListaDoctoServico(List<TypedFlatMap> doctosServico) {
        Collections.sort(doctosServico, new Comparator<TypedFlatMap>() {
            public int compare(TypedFlatMap obj1, TypedFlatMap obj2) {
                int valor = obj1.getString(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO_DESCRIPTION).compareTo(obj2.getString(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO_DESCRIPTION));
                if (valor == 0) {
                    valor = obj1.getString(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM).compareTo(obj2.getString(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM));
                    if (valor == 0) {
                        valor = obj1.getLong(ConsCarregamentoDescarga.NR_DOCTO_SERVICO).compareTo(obj2.getLong(ConsCarregamentoDescarga.NR_DOCTO_SERVICO));
                    }
                }
                return valor;
            }
        });

    }


    /**
     * Método que valida se já não existe o número de lacre incluido para o Controle de Carga em questão.
     *
     * @param idControleCarga
     * @param nrLacres
     */
    public void validateLacreControleCarga(Long idControleCarga, String nrLacres) {
        Map<String, Object> mapControleCarga = new HashMap<String, Object>();
        Map<String, Object> mapLacreControleCarga = new HashMap<String, Object>();
        mapControleCarga.put(ConsCarregamentoDescarga.ID_CONTROLE_CARGA, idControleCarga);
        mapLacreControleCarga.put("controleCarga", mapControleCarga);
        mapLacreControleCarga.put("nrLacres", nrLacres);

        List listaLacresCc = lacreControleCargaService.find(mapLacreControleCarga);
        if (!listaLacresCc.isEmpty()) {
            throw new BusinessException("LMS-05165", new Object[]{nrLacres});
        }
    }

    /**
     * @param idControleCarga
     * @return
     */
    public List<TypedFlatMap> findDoctoServicoWithServicoPrioritario(Long idControleCarga) {
        return findDoctoServicoByDpeAtrasadoByServicoPrioritario(idControleCarga, false, false);
    }

    /**
     * @param idControleCarga
     * @return
     */
    public List<TypedFlatMap> findDoctoServicoWithDpeAtrasado(Long idControleCarga) {
        return findDoctoServicoByDpeAtrasadoByServicoPrioritario(idControleCarga, true, false);
    }

    /**
     * @param idControleCarga
     * @return
     */
    public List<TypedFlatMap> findDoctoServicoWithWithPriorizacaoEmbarque(Long idControleCarga) {
        return findDoctoServicoByDpeAtrasadoByServicoPrioritario(idControleCarga, false, true);
    }


    /**
     * @param idControleCarga
     * @param isDpeAtrasado
     * @return
     */
    private List<TypedFlatMap> findDoctoServicoByDpeAtrasadoByServicoPrioritario(Long idControleCarga, boolean isDpeAtrasado, boolean isPriorizacaoEmbarque) {
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        Long idRotaColetaEntrega = null;
        if (controleCarga.getRotaColetaEntrega() != null) {
            idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
        }

        Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
        List<Map<String, Long>> listaManifestos = new ArrayList();

        List<Manifesto> manifestos = controleCarga.getManifestos();
        for (Manifesto manifesto : manifestos) {
            if (manifesto.getFilialByIdFilialOrigem().getIdFilial().compareTo(idFilialUsuario) == 0) {
                Map<String, Long> mapManifeto = new HashMap();
                mapManifeto.put(ConsCarregamentoDescarga.ID_FILIAL_ORIGEM, manifesto.getFilialByIdFilialOrigem().getIdFilial());
                mapManifeto.put(ConsCarregamentoDescarga.ID_FILIAL_DESTINO, manifesto.getFilialByIdFilialDestino().getIdFilial());
                listaManifestos.add(mapManifeto);
            }
        }

        List<Map<String, Object>> doctosServico = Collections.emptyList();

        if (isPriorizacaoEmbarque) {
            doctosServico = doctoServicoService.findDoctoServicoWithWithPriorizacaoEmbarque(
                    controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(),
                    controleCarga.getTpControleCarga().getValue(),
                    listaManifestos,
                    idRotaColetaEntrega);
        } else if (isDpeAtrasado) {
            doctosServico = doctoServicoService.findDoctoServicoWithDpeAtrasado(
                    controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(),
                    controleCarga.getTpControleCarga().getValue(),
                    listaManifestos,
                    idRotaColetaEntrega);
        } else {
            doctosServico = doctoServicoService.findDoctoServicoWithServicoPrioritario(
                    controleCarga.getFilialByIdFilialAtualizaStatus().getIdFilial(),
                    controleCarga.getTpControleCarga().getValue(),
                    listaManifestos,
                    idRotaColetaEntrega);
        }

        List<TypedFlatMap> listaDocumentos = new ArrayList<TypedFlatMap>();
        String[] documentoServicoDescription = null; 
        for (Map mapDoctoServico : doctosServico) {
            TypedFlatMap tfm = new TypedFlatMap();
            tfm.put(ConsCarregamentoDescarga.ID_DOCTO_SERVICO, mapDoctoServico.get(ConsCarregamentoDescarga.ID_DOCTO_SERVICO));
            
            documentoServicoDescription = mapDoctoServico.get(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO_DESCRIPTION).toString().split("»");
            VarcharI18n description = new VarcharI18n(documentoServicoDescription[1].replace("¦", ""));
            DomainValue tpDocumentoServico = new DomainValue
            										(
        												mapDoctoServico.get(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO).toString(),
            											description, true); 
            
            tfm.put(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO, tpDocumentoServico);
            tfm.put(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO_VALUE, tpDocumentoServico.getValue());
            tfm.put(ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO_DESCRIPTION, tpDocumentoServico.getDescription().toString());
            tfm.put(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM, mapDoctoServico.get(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM));
            tfm.put(ConsCarregamentoDescarga.NR_DOCTO_SERVICO, mapDoctoServico.get(ConsCarregamentoDescarga.NR_DOCTO_SERVICO));
            tfm.put(ConsCarregamentoDescarga.ID_REGISTRO_PRIORIZACAO_DOCTO, mapDoctoServico.get(ConsCarregamentoDescarga.ID_REGISTRO_PRIORIZACAO_DOCTO));
            listaDocumentos.add(tfm);
        }
        ordenaListaDoctoServico(listaDocumentos);
        return listaDocumentos;
    }

    /**
     * Método que busca os Documentos Serviço do Manifesto que possuem Data Prevista para Entrega
     *
     * @param criteria
     * @return
     */
    public ResultSetPage<TypedFlatMap> findPaginatedDoctoServico(TypedFlatMap criteria) {
        Long idControleCarga = criteria.getLong(ConsCarregamentoDescarga.ID_CONTROLE_CARGA);

        /** Otimização LMS-809 */
        final ProjectionList projection = createProjectionToPaginated();
        final Map<String, String> alias = createAliasToPaginated();
        final List<Criterion> criterion = createCriterionToPaginated(criteria);

        final List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
        final List<Manifesto> listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (final Manifesto manifesto : listManifestos) {
            final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias, criterion);
            for (final DoctoServico doctoServico : doctoServicoList) {
                result.add(loadMapDoctoServicoAndManifesto(doctoServico, manifesto));
            }
        }

        FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
        return new ResultSetPage<TypedFlatMap>(findDefinition.getCurrentPage(), result);
    }

    /**
     * Método RowCount da busca de Documentos Serviço do manifesto
     */
    public Integer getRowCountDoctoServico(TypedFlatMap criteria) {
        Integer rowCount = IntegerUtils.ZERO;
        Long idControleCarga = criteria.getLong(ConsCarregamentoDescarga.ID_CONTROLE_CARGA);

        /** Otimização LMS-809 */
        final ProjectionList projection = createProjectionToPaginated();
        final Map<String, String> alias = createAliasToPaginated();
        final List<Criterion> criterion = createCriterionToPaginated(criteria);

        final List<Manifesto> listManifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (final Manifesto manifesto : listManifestos) {
            final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias, criterion);
            if (!doctoServicoList.isEmpty()) {
                rowCount = IntegerUtils.add(rowCount, doctoServicoList.size());
            }
        }
        return rowCount;
    }

    private ProjectionList createProjectionToPaginated() {
        return Projections.projectionList()
                .add(Projections.property(ConsCarregamentoDescarga.DS_ID), ConsCarregamentoDescarga.ID_DOCTO_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_NR_DOCTO_SERVICO), ConsCarregamentoDescarga.NR_DOCTO_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_TP_DOCUMENTO_SERVICO), ConsCarregamentoDescarga.TP_DOCUMENTO_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.SER_DS_SERVICO), ConsCarregamentoDescarga.SERVICO_DS_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.SER_SG_SERVICO), ConsCarregamentoDescarga.SERVICO_SG_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.FO_SG_FILIAL), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_ORIGEM_SG_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.FD_SG_FILIAL), ConsCarregamentoDescarga.FILIAL_BY_ID_FILIAL_DESTINO_SG_FILIAL)
                .add(Projections.property(ConsCarregamentoDescarga.PR_NM_PESSOA), ConsCarregamentoDescarga.CLIENTE_BY_ID_CLIENTE_REMETENTE_PESSOA_NM_PESSOA)
                .add(Projections.property(ConsCarregamentoDescarga.PD_NM_PESSOA), ConsCarregamentoDescarga.CLIENTE_BY_ID_CLIENTE_DESTINATARIO_PESSOA_NM_PESSOA)
                .add(Projections.property(ConsCarregamentoDescarga.DS_QT_VOLUMES), ConsCarregamentoDescarga.QT_VOLUMES)
                .add(Projections.property(ConsCarregamentoDescarga.DS_PS_REAL), ConsCarregamentoDescarga.PS_REAL)
                .add(Projections.property(ConsCarregamentoDescarga.M_SG_MOEDA), ConsCarregamentoDescarga.MOEDA_SG_MOEDA)
                .add(Projections.property(ConsCarregamentoDescarga.M_DS_SIMBOLO), ConsCarregamentoDescarga.MOEDA_DS_SIMBOLO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_VL_MERCADORIA), ConsCarregamentoDescarga.VL_MERCADORIA)
                .add(Projections.property(ConsCarregamentoDescarga.DS_VL_TOTAL_DOC_SERVICO), ConsCarregamentoDescarga.VL_TOTAL_DOC_SERVICO)
                .add(Projections.property(ConsCarregamentoDescarga.DS_DT_PREV_ENTREGA), ConsCarregamentoDescarga.DT_PREV_ENTREGA);
    }

    private Map<String, String> createAliasToPaginated() {
        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.DS_SERVICO, DS_SERVICO_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_ORIGEM, ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_ORIGEM_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_DESTINO, ConsCarregamentoDescarga.DS_FILIAL_BY_ID_FILIAL_DESTINO_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_CLIENTE_BY_ID_CLIENTE_REMETENTE, ConsCarregamentoDescarga.DS_CLIENTE_BY_ID_CLIENTE_REMETENTE_ALIAS);
        alias.put(ConsCarregamentoDescarga.CR_PESSOA, ConsCarregamentoDescarga.CR_PESSOA_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_CLIENTE_BY_ID_CLIENTE_DESTINATARIO, ConsCarregamentoDescarga.DS_CLIENTE_BY_ID_CLIENTE_DESTINATARIO_ALIAS);
        alias.put(ConsCarregamentoDescarga.CD_PESSOA, ConsCarregamentoDescarga.CD_PESSOA_ALIAS);
        alias.put(ConsCarregamentoDescarga.DS_MOEDA, ConsCarregamentoDescarga.DS_MOEDA_ALIAS);
        return alias;
    }

    private List<Criterion> createCriterionToPaginated(final TypedFlatMap criteria) {
        boolean emAtraso = criteria.getBoolean("emAtraso");
        boolean naData = criteria.getBoolean("naData");
        boolean emDia = criteria.getBoolean("emDia");
        final Long idServico = criteria.getLong("servico.idServico");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.eq("manifesto.filialByIdFilialDestino.id", SessionUtils.getFilialSessao().getIdFilial()));
        if (LongUtils.hasValue(idServico)) {
            criterion.add(Restrictions.eq("ser.id", idServico));
        }
        final YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
        final List<Criterion> dateRestrictions = new ArrayList<Criterion>();
        if (emAtraso) {
            dateRestrictions.add(Restrictions.lt(ConsCarregamentoDescarga.DS_DT_PREV_ENTREGA, dataAtual));
        }
        if (naData) {
            dateRestrictions.add(Restrictions.eq(ConsCarregamentoDescarga.DS_DT_PREV_ENTREGA, dataAtual));
        }
        if (emDia) {
            dateRestrictions.add(Restrictions.ge(ConsCarregamentoDescarga.DS_DT_PREV_ENTREGA, dataAtual));
        }

        // lógica para clausula OR
        if (!dateRestrictions.isEmpty()) {
            if (dateRestrictions.size() > ConsCarregamentoDescarga.SIZE_2) {
                criterion.add(Restrictions.or(dateRestrictions.get(0), Restrictions.or(dateRestrictions.get(1), dateRestrictions.get(ConsCarregamentoDescarga.SIZE_2))));
            } else if (dateRestrictions.size() == ConsCarregamentoDescarga.SIZE_2) {
                criterion.add(Restrictions.or(dateRestrictions.get(0), dateRestrictions.get(1)));
            } else {
                criterion.add(dateRestrictions.get(0));
            }
        }
        return criterion;
    }

    /**
     * Busca a quantidade de todos os manifestosDoctoServico relacionados com um manifesto em questao.
     *
     * @param idManifesto
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountPreManifestoDoctoServicoByIdManifesto(Long idManifesto, Long idDoctoServico) {
        return preManifestoDocumentoService.getRowCountPreManifestoDoctoServicoByIdManifesto(idManifesto, idDoctoServico);
    }


    /**
     * @param idControleCarga
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    public TypedFlatMap findCarregamentoDescargaByControleCarga(Long idControleCarga, Long idFilial, String tpOperacao) {
        List result = getCarregamentoDescargaDAO().findCarregamentoDescargaByControleCarga(idControleCarga, idFilial, tpOperacao);
        if (result.isEmpty()) {
            return null;
        }

        result = new AliasToTypedFlatMapResultTransformer().transformListResult(result);
        TypedFlatMap tfm = (TypedFlatMap) result.get(0);
        List<EventoControleCarga> listaEcc = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilial, idControleCarga, "CP");
        if (!listaEcc.isEmpty()) {
            EventoControleCarga ecc = listaEcc.get(0);
            tfm.put("dhEvento", ecc.getDhEvento());
        }
        return tfm;
    }


    /**
     * Método que gera o mapeamento a partir dos dados do Documento Serviço
     *
     * @param doctoServico
     * @param manifesto
     * @return
     */
    private TypedFlatMap loadMapDoctoServicoAndManifesto(DoctoServico doctoServico, Manifesto manifesto) {
        TypedFlatMap mapDoctoServicoAndManifesto = new TypedFlatMap();

        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.NR_DOCTO_SERVICO, doctoServico.getNrDoctoServico());
        mapDoctoServicoAndManifesto.put("dsServico", doctoServico.getServico().getDsServico());
        mapDoctoServicoAndManifesto.put("sgServico", doctoServico.getServico().getSgServico());
        mapDoctoServicoAndManifesto.put("tpDocumento", doctoServico.getTpDocumentoServico());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM, doctoServico.getFilialByIdFilialOrigem().getSgFilial());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.SG_FILIAL_DESTINO, doctoServico.getFilialByIdFilialDestino().getSgFilial());
        mapDoctoServicoAndManifesto.put("clienteRemetente", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
        mapDoctoServicoAndManifesto.put("clienteDestinatario", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.QT_VOLUMES, doctoServico.getQtVolumes());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.PS_REAL, doctoServico.getPsReal());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.SG_MOEDA, doctoServico.getMoeda().getSgMoeda());
        mapDoctoServicoAndManifesto.put("dsSimboloMoeda", doctoServico.getMoeda().getDsSimbolo());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.VL_MERCADORIA, doctoServico.getVlMercadoria());
        mapDoctoServicoAndManifesto.put("sgMoeda2", doctoServico.getMoeda().getSgMoeda());
        mapDoctoServicoAndManifesto.put("dsSimboloMoeda2", doctoServico.getMoeda().getDsSimbolo());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.VL_TOTAL_DOC_SERVICO, doctoServico.getVlTotalDocServico());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.DT_PREV_ENTREGA, doctoServico.getDtPrevEntrega());
        mapDoctoServicoAndManifesto.put(ConsCarregamentoDescarga.TP_STATUS_MANIFESTO, manifesto.getTpStatusManifesto());
        mapDoctoServicoAndManifesto.put("sgFilialManifesto", manifesto.getFilialByIdFilialOrigem().getSgFilial());
        mapDoctoServicoAndManifesto.put("nrPreManifesto", manifesto.getNrPreManifesto());

        return mapDoctoServicoAndManifesto;
    }

    /**
     * Realiza o cancelamento do fim de um carregamento.
     *
     * @param idCarregamentoDescarga
     * @param obCancelamento
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Map generateCancelarFimCarregamento(Long idCarregamentoDescarga, String obCancelamento) {
        Map retorno = new HashMap();
        CarregamentoDescarga carregamentoDescarga = this.findById(idCarregamentoDescarga);
        ControleCarga controleCarga = carregamentoDescarga.getControleCarga();
        Long idControleCarga = controleCarga.getIdControleCarga();

        Filial filialSessao = SessionUtils.getFilialSessao();

        //LMS-3379
        //Regra 1.1 da especificação 05.01.02.05
        if (controleCargaService.validateExisteEventoEmitido(idControleCarga)) {
            controleCargaService.generateEventoChangeStatusControleCarga(
                    idControleCarga, filialSessao.getIdFilial(), "EC", null,
                    controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                    configuracoesFacade.getMensagem("emissaoCancelada"),
                    null, null, null, null, null, null, null, null);
        }

        //Regra 1.12 da especificação 05.01.02.05
        retorno = manifestoEletronicoService.cancelarMdfe(idControleCarga);

        //LMSA-3773
        boolean isCarregamentoFilial = false;
        List<Manifesto> manifestosControleCarga = controleCarga.getManifestos();
        for (Manifesto manifesto : manifestosControleCarga) {
            if (manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                isCarregamentoFilial = true;
            }
        }

        if (!isCarregamentoFilial) {
            return retorno;
        }

        //Gera a mudanca de evento do status do controle de carga...
        //Regra 1.2 da especificação 05.01.02.05
        controleCargaService.generateEventoChangeStatusControleCarga(
                idControleCarga,
                filialSessao.getIdFilial(),
                "CC",
                null,
                null,
                configuracoesFacade.getMensagem("cancelamentoFinalCarregamento"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // Procura e remove todos os eventos FC (Fim Carregamento)
        //Regra 1.3 da especificação 05.01.02.05
        List<EventoControleCarga> listEventosFC = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(filialSessao.getIdFilial(), idControleCarga, "FC");
        for (EventoControleCarga eventoControleCarga : listEventosFC) {
            eventoControleCargaService.removeById(eventoControleCarga.getIdEventoControleCarga());
        }

        //Atualiza o carregamentoDescarga...
        //Regra 1.4 da especificação 05.01.02.05
        carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
        carregamentoDescarga.setUsuarioByIdUsuarioFinalizado(null);
        carregamentoDescarga.setDhFimOperacao(null);
        carregamentoDescarga.setObCancelamento(obCancelamento);

        //Reabre a equipe ultima operacao
        List<EquipeOperacao> eos = controleCarga.getEquipeOperacoes();
        if (eos != null) {
            int index = 0;
            for (int i = 0; i < eos.size(); i++) {
                EquipeOperacao eo1 = eos.get(i);
                EquipeOperacao eo2 = eos.get(index);
                if (eo1.getDhFimOperacao() != null
                        && eo1.getDhFimOperacao().compareTo(eo2.getDhFimOperacao()) >= 0) {
                    index = i;
                }
            }
            eos.get(index).setDhFimOperacao(null);
        }

        //Remove todos os lacres que estao relacionados com este carregamento...
        //Regra 1.5 da especificação 05.01.02.05
        for (LacreControleCarga lacreControleCarga : controleCarga.getLacreControleCargas()) {
            //Deve remover somente os lacres que sao desta filial...
            if (lacreControleCarga.getFilialByIdFilialInclusao().getIdFilial().compareTo(SessionUtils.getFilialSessao().getIdFilial()) == 0) {
                lacreControleCargaService.removeById(lacreControleCarga.getIdLacreControleCarga());
            }
        }


        //Regra 1.6 da especificação 05.01.02.05
        controleCarga.setPcOcupacaoInformado(null);
        controleCargaService.store(controleCarga);

        //Regra 1.8 da especificação 05.01.02.05
        //Verifica se existe algum evento PAOP para o meio de transporte e remove caso encontre-o.
        EventoMeioTransporte eventoPAOPMeioTransporte = eventoMeioTransporteService
                .findLastEventoMeioTransporteToMeioTransporte(
                        controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                        SessionUtils.getFilialSessao().getIdFilial(),
                        idControleCarga,
                        "PAOP");
        if (eventoPAOPMeioTransporte != null) {
            eventoMeioTransporteService.removeById(eventoPAOPMeioTransporte.getIdEventoMeioTransporte());
        }

        //Regra 1.7 da especificação 05.01.02.05
        //Verifica se existe algum evento EMCA para o meio de transporte e remove a data de fim evento caso encontre-o.
        EventoMeioTransporte eventoEMCAMeioTransporte = eventoMeioTransporteService
                .findLastEventoMeioTransporteToMeioTransporte(
                        controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(),
                        SessionUtils.getFilialSessao().getIdFilial(),
                        idControleCarga,
                        "EMCA");
        if (eventoEMCAMeioTransporte != null) {
            eventoEMCAMeioTransporte.setDhFimEvento(null);
            eventoEMCAMeioTransporte.setUsuario(SessionUtils.getUsuarioLogado());

            eventoMeioTransporteService.store(eventoEMCAMeioTransporte);
        }

        //Regra 1.9 da especificação 05.01.02.05
        //Verifica se o controle de carga possui um semi-reboque.
        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            //Verifica se existe algum evento PAOP para o semi-reboque...
            EventoMeioTransporte eventoPAOPSemiReboque = eventoMeioTransporteService
                    .findLastEventoMeioTransporteToMeioTransporte(
                            controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(),
                            SessionUtils.getFilialSessao().getIdFilial(),
                            idControleCarga,
                            "PAOP");
            if (eventoPAOPSemiReboque != null) {
                eventoMeioTransporteService.removeById(eventoPAOPSemiReboque.getIdEventoMeioTransporte());
            }

            //Verifica se existe algum evento EMCA para o semi-reboque e remove a data de fim evento caso encontre-o.
            EventoMeioTransporte eventoEMCASemiReboque = eventoMeioTransporteService
                    .findLastEventoMeioTransporteToMeioTransporte(
                            controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(),
                            SessionUtils.getFilialSessao().getIdFilial(),
                            idControleCarga,
                            "EMCA");
            if (eventoEMCASemiReboque != null) {
                eventoEMCASemiReboque.setDhFimEvento(null);
                eventoEMCASemiReboque.setUsuario(SessionUtils.getUsuarioLogado());
                eventoMeioTransporteService.store(eventoEMCASemiReboque);
            }
        }

        //Regra 1.10 da especificação 05.01.02.05
        List<Manifesto> manifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga, null, null, null);
        for (Manifesto manifesto : manifestos) {
            if ("CC".equals(manifesto.getTpStatusManifesto().getValue())) {
                dispositivoUnitizacaoService.executeVoltarDispositivosUnitizacaoCarregados(manifesto.getIdManifesto());
            }
        }

        this.store(carregamentoDescarga);

        //LMS-3906
        //Regra 1.11 da especificação 05.01.02.05
        carregamentoDescargaVolumeService.removeByCarregamentoDescargaNaFilialQuandoCarregamento(carregamentoDescarga);

        return retorno;

    }

    /**
     * Faz a validacao do PCE para a tela de descarregar veiculo.
     *
     * @param doctosServicos
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     * @return
     */
    public List<Long> validatePCE(List<DoctoServico> doctosServicos, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
        Set<Long> set = new HashSet<Long>();
        for (DoctoServico doctoServico : doctosServicos) {
            if (doctoServico.getClienteByIdClienteRemetente() != null) {
                Long codigoPceRemetente = versaoDescritivoPceService.findCodigoVersaoDescritivoPceByCriteria(
                        doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
                        cdProcesso, cdEvento, cdOcorrencia);
                if (codigoPceRemetente != null) {
                    set.add(codigoPceRemetente);
                }
            }

            if (doctoServico.getClienteByIdClienteDestinatario() != null) {
                Long codigoPceDestinatario = versaoDescritivoPceService.findCodigoVersaoDescritivoPceByCriteria(
                        doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
                        cdProcesso, cdEvento, cdOcorrencia);
                if (codigoPceDestinatario != null) {
                    set.add(codigoPceDestinatario);
                }
            }
        }

        return new ArrayList<Long>(set);
    }

    /**
     * Faz a validacao do PCE para a tela de finalizar descarga. A validacao
     * e realizada para cada doctoServico que se encotra dentro dos manifestos do controle
     * de carga do mesmo.
     *
     * @param idControleCarga
     * @param cdProcesso
     * @param cdEvento
     * @param cdOcorrencia
     */
    public List<Long> validatePCE(Long idControleCarga, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {
        //Busca todos os Documentos de Servico vinculados com o Controle de Carga
        //e entao chama o método "validatePCE(List doctosServicos..."
        List<DoctoServico> doctosServicos = doctoServicoService.findDoctosServicoByIdControleCarga(idControleCarga);
        return this.validatePCE(doctosServicos, cdProcesso, cdEvento, cdOcorrencia);
    }

    private EventoMeioTransporte populaEvento(String tpSituacao, DateTime inicioEvento, DateTime fimEvento, Box box, ControleCarga controleCarga, Filial filial, MeioTransporte meioTransporte, Usuario usuario) {
        EventoMeioTransporte eventoMeioTransporte = populaEvento(tpSituacao, inicioEvento, fimEvento, box, controleCarga, filial, meioTransporte);
        eventoMeioTransporte.setUsuario(usuario);

        return eventoMeioTransporte;
    }

    private EventoMeioTransporte populaEvento(String tpSituacao, DateTime inicioEvento, DateTime fimEvento, Box box, ControleCarga controleCarga, Filial filial, MeioTransporte meioTransporte) {

        EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
        eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue(tpSituacao));
        eventoMeioTransporte.setDhInicioEvento(inicioEvento);
        eventoMeioTransporte.setDhFimEvento(fimEvento);
        //Gerando os relacionamentos...
        eventoMeioTransporte.setBox(box);
        eventoMeioTransporte.setControleCarga(controleCarga);
        eventoMeioTransporte.setFilial(filial);
        eventoMeioTransporte.setMeioTransporte(meioTransporte);

        return eventoMeioTransporte;
    }

    public void executeEnviarFinalizarIntegracaoSmp(Long idControleCarga) {
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
		String versaoGerRisco = (String) conteudoParametroFilialService.findConteudoByNomeParametro(
				idFilialUsuario, ConstantesGerRisco.SGR_VERS_INT_GER_RIS, false);
		if (ConstantesGerRisco.SGR_GER_RIS_VERS_2.equals(versaoGerRisco)) {
			finalizarIntegracaoSmpVersao2(controleCarga);
		} else if (ConstantesGerRisco.SGR_GER_RIS_VERS_1.equals(versaoGerRisco) || versaoGerRisco == null) {
			finalizarSmp(controleCarga);
		} else {
			finalizarIntegracaoSmpVersao2(controleCarga);
		}
	}

	private void finalizarSmp(ControleCarga controleCarga) {
		SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService
				.findByIdControleCarga(controleCarga.getIdControleCarga());
		if (solicMonitPreventivo != null && solicMonitPreventivo.getIdSolicMonitPreventivo() != null) {
			gerarEnviarSMPService.storeStatusSmp(solicMonitPreventivo.getIdSolicMonitPreventivo(),
					new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_FINALIZADA));
		}
	}

	private void finalizarIntegracaoSmpVersao2(ControleCarga controleCarga) {
		SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService
				.findByIdControleCarga(controleCarga.getIdControleCarga());
		if (solicMonitPreventivo != null && solicMonitPreventivo.getIdSolicMonitPreventivo() != null) {
			gerarEnviarSMPService.generateFinalizarSMP(controleCarga.getIdControleCarga(),
					solicMonitPreventivo.getIdSolicMonitPreventivo());
		}
	}
    
    
    /**
     * Retorna uma instancia de CarregamentoDescarca a partir dos parâmetros informados.
     *
     * @param idControleCarga
     * @param idFilial
     * @param idPostoAvancado
     * @param tpOperacao
     * @return
     */
    public List<CarregamentoDescarga> findByIdControleCargaIdFilialIdPostoAvancadoTpOperacao(Long idControleCarga, Long idFilial, Long idPostoAvancado, String tpOperacao) {
        return getCarregamentoDescargaDAO().findByIdControleCargaIdFilialIdPostoAvancadoTpOperacao(idControleCarga, idFilial, idPostoAvancado, tpOperacao);
    }

    public List<CarregamentoDescarga> findByControleCarga(ControleCarga controleCarga, String tpOperacao, Filial filial){
    	return getCarregamentoDescargaDAO().findByControleCarga(controleCarga,  tpOperacao, filial);
    }
    
    
    /**
     * Solicitação da Integração - CQPRO00005521
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(long idControleCarga, long idFilial) : CarregamentoDescarga
     * OBS.O método assume como critério que a dhFimOperacao seja null;
     *
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public List<CarregamentoDescarga> findCarregamentoDescarga(Long idControleCarga, Long idFilial) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, idFilial);
    }

    /**
     * Solicitação da Integração - CQPRO00006071
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String tpOperacao ) : CarregamentoDescarga
     *
     * @param idControleCarga
     * @param idFilial
     * @param tpStatusOperacao
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String tpOperacao) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, idFilial, tpStatusOperacao, tpOperacao);
    }

    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, Object[] tpStatusOperacao, String tpOperacao) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, idFilial, tpStatusOperacao, tpOperacao);
    }

    /**
     * Retorna uma instância do CarregamentoDescarga a partir do idControleCarga, idFilial e tpOperacao
     *
     * @param idControleCarga
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpOperacao) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, idFilial, tpOperacao);
    }

    public List<CarregamentoDescarga> findCarregamentoDescarga(Long idControleCarga, String tpOperacao) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, tpOperacao);
    }

    public boolean validateCarregamentoFinalizado(Long idControleCarga, Long idFilial) {
        return getCarregamentoDescargaDAO().validateCarregamentoFinalizado(idControleCarga, idFilial);
    }

    public boolean validateDescarregamentoFinalizado(Long idControleCarga, Long idFilial) {
        return getCarregamentoDescargaDAO().validateDescarregamentoFinalizado(idControleCarga, idFilial);
    }

    public boolean validateDescarregamentoIniciado(Long idControleCarga, Long idFilial) {
        return getCarregamentoDescargaDAO().validateDescarregamentoIniciado(idControleCarga, idFilial);
    }

    public void storeReabrirCarregamentoDescarga(Long idCarregamentoDescarga) {
        CarregamentoDescarga carregamentoDescarga = this.findById(idCarregamentoDescarga);

        if (carregamentoDescarga != null) {
            carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
            this.store(carregamentoDescarga);
        }
    }

    /**
     * Verifica se existe CarregamentoDescarga no box
     * passado por parâmetro
     *
     * @param idBox
     * @param idFilial
     * @param tpOperacao
     * @return
     */
    public CarregamentoDescarga findBoxCarregamentoDescarga(Long idBox, Long idFilial, String tpOperacao) {
        return getCarregamentoDescargaDAO().findBoxCarregamentoDescarga(idBox, idFilial, tpOperacao);
    }

    /**
     * Verifica se o carregamento do controle de carga já está finalizado.
     *
     * @param idControleCarga
     * @param idFilial
     * @return True, se estiver finalizado, caso contrário, False.
     */
    public Boolean validateExisteCarregamentoFinalizado(Long idControleCarga, Long idFilial) {
        return getCarregamentoDescargaDAO().validateExisteCarregamentoFinalizado(idControleCarga, idFilial);
    }

    /**
     * Solicitação da Integração
     * Criar um método na classe CarregamentoDescargaService que retorne uma instancia da classe CarregamentoDescarga conforme os parametros especificados
     * Nome do método: findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String condTpStatusOperacao, String tpOperacao ) : CarregamentoDescarga
     *
     * @param idControleCarga
     * @param idFilial
     * @param tpStatusOperacao
     * @param condTpStatusOperacao Parametro que permite buscar por Carregamento Descarga com status da operacao diferente de "C" (Cancelada)
     * @param tpOperacao
     * @return CarregamentoDescarga
     */
    public CarregamentoDescarga findCarregamentoDescarga(Long idControleCarga, Long idFilial, String tpStatusOperacao, String condTpStatusOperacao,
                                                         String tpOperacao) {
        return getCarregamentoDescargaDAO().findCarregamentoDescarga(idControleCarga, idFilial, tpStatusOperacao, condTpStatusOperacao, tpOperacao);
    }

    public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }


    /**
     * CQPRO00025051
     * Gera um evento para TODOS os volumes associados
     * a um determinado manifesto. Verifica se o se os manifestoEntregaVolume possuem ocorrenciaEntrega
     * setada para "E" ou "A". Caso sim, nao é realizado a geração deste evento.
     *
     * @param manifesto
     * @param idControleCarga
     * @param cdEvento
     * @param idCarregamentoDescarga
     */
    private void generateEventoVolumeToColetaEntrega(Manifesto manifesto, Long idControleCarga, Short cdEvento, Long idCarregamentoDescarga, String tpScan) {
        /** Percorre todos volumes */
        final List<ManifestoEntregaVolume> manifestoEntregaVolumes = manifesto.getManifestoEntrega().getManifestoEntregaVolumes();

        for (final ManifestoEntregaVolume manifestoEntregaVolume : manifestoEntregaVolumes) {
            final OcorrenciaEntrega ocorrenciaEntrega = manifestoEntregaVolume.getOcorrenciaEntrega();
            /** Não gera evento caso o mesmo possua uma OCORRENCIA DE ENTREGA */
            if (ocorrenciaEntrega == null || (!ConstantesEntrega.TP_OCORRENCIA_ENTREGUE.equals(ocorrenciaEntrega.getTpOcorrencia().getValue())
                    && !ConstantesEntrega.TP_OCORRENCIA_ENTREGUE_AEROPORTO.equals(ocorrenciaEntrega.getTpOcorrencia().getValue()))) {
                final VolumeNotaFiscal volumeNotaFiscal = manifestoEntregaVolume.getVolumeNotaFiscal();
                /** Gera Evento Volume */
                if (volumeNotaFiscal.getLocalizacaoMercadoria() == null
                        || !ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.equals(volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
                        || ConstantesSim.TP_SCAN_FISICO.equalsIgnoreCase(tpScan)
                        || ConstantesSim.TP_SCAN_CASCADE.equalsIgnoreCase(tpScan)) {
                    getEventoVolumeService().generateEventoVolume(volumeNotaFiscal, cdEvento, tpScan);
                }

                final DispositivoUnitizacao dispositivoUnitizacao = volumeNotaFiscal.getDispositivoUnitizacao();
                if (dispositivoUnitizacao != null) {
                    final List<DispCarregIdentificado> dispCarregIdentificados = dispositivoUnitizacao.getDispCarregIdentificados();
                    for (final DispCarregIdentificado dispCarregIdentificado : dispCarregIdentificados) {
                        final CarregamentoDescarga carregamentoDescarga = dispCarregIdentificado.getCarregamentoDescarga();
                        if (carregamentoDescarga != null && idCarregamentoDescarga != null && idCarregamentoDescarga.equals(carregamentoDescarga.getIdCarregamentoDescarga())) {
                            final CarregamentoPreManifesto carregamentoPreManifesto = dispCarregIdentificado.getCarregamentoPreManifesto();
                            if (carregamentoPreManifesto != null
                                    && carregamentoPreManifesto.getManifesto() != null && "C".equals(carregamentoPreManifesto.getManifesto().getTpManifesto().getValue())) {
                                // Gera Evento Dispositivo Utilizacao
                                incluirEventosRastreabilidadeInternacionalService.generateEventoDispositivoUnitizacao(dispositivoUnitizacao, cdEvento, null);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * CQPRO00025051
     * Gera Eventos para os volumes do manifesto;
     *
     * @param idManifesto
     * @param idControleCarga
     * @param idCarregamentoDescarga
     */
    private void generateEventoVolumeFimDescargaViagem(Long idManifesto, Long idControleCarga, Long idCarregamentoDescarga,
                                                       AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        /// Busca Volumes com ENTREGA DIFERENTE DE EFETUADA
        List<VolumeNotaFiscal> volumesNotaFiscal = this.findVolumesByManifesto(idManifesto, idControleCarga, idCarregamentoDescarga, "D");

        EventoVolumeGenerator gen = eventoVolumeService.newEventoVolumeGenerator();
        for (VolumeNotaFiscal volumeNotaFiscal : volumesNotaFiscal) {
            Short cdLocalizacao = volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
            gen.cdEvento(ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(cdLocalizacao) ?
                    ConstantesSim.EVENTO_DESCARGA_CONCLUIDA :
                    ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO)
                    .generate(volumeNotaFiscal, adsmNativeBatchSqlOperations);
            getDao().getAdsmHibernateTemplate().evict(volumeNotaFiscal);
        }

        /** Busca Dispositivos EM DESCARGA */
        final List<DispositivoUnitizacao> dispositivosUnitizacao = dispositivoUnitizacaoService.findByCarregamentoDescargaManifesto(idCarregamentoDescarga, idManifesto,
                Arrays.asList(ConstantesSim.CD_MERCADORIA_EM_DESCARGA), ConstantesExpedicao.TP_MANIFESTO_VIAGEM);

        for (final DispositivoUnitizacao dispositivoUnitizacao : dispositivosUnitizacao) {
            incluirEventosRastreabilidadeInternacionalService.generateEventoDispositivoUnitizacaoComBatch(
                    dispositivoUnitizacao, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, null, adsmNativeBatchSqlOperations);
        }
    }

    private List<VolumeNotaFiscal> findVolumesByManifesto(Long idManifesto, Long idControleCarga, Long idCarregamentoDescarga, String tpOperacao) {
        /** Otimização LMS-807 */
        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.VNF_LOCALIZACAO_MERCADORIA, "loc");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.ne(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA, ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA));

        DetachedCriteria detCriteria = buildQueryCarregamentoDescargaScan(idControleCarga, idCarregamentoDescarga, tpOperacao);

        criterion.add(Subqueries.notExists(detCriteria));
        return volumeNotaFiscalService.findByIdManifesto(idManifesto, alias, criterion);
    }

    private DetachedCriteria buildQueryCarregamentoDescargaScan(
            Long idControleCarga, Long idCarregamentoDescarga, String tpOperacao) {
        DetachedCriteria detCriteria = DetachedCriteria.forClass(CarregamentoDescargaVolume.class, "carregamentoDescargaVolume");

        detCriteria.setProjection(Projections.property("carregamentoDescargaVolume.idCarregamentoDescargaVolume"));

        detCriteria.add(Restrictions.in("carregamentoDescargaVolume.tpScan", new String[]{"SF", "SS"}));
        String tpStatusOperacao = "F";
        if ("C".equals(tpOperacao)) {
            tpStatusOperacao = "O";
        }
        detCriteria.createAlias("carregamentoDescargaVolume.carregamentoDescarga", "carregDesc");
        detCriteria.createAlias("carregamentoDescargaVolume.volumeNotaFiscal", "volumeNF");
        detCriteria.createAlias("carregDesc.filial", "filial");
        detCriteria.createAlias("carregDesc.controleCarga", "controleCarga");
        detCriteria.add(Restrictions.eq("carregDesc.tpStatusOperacao", tpStatusOperacao));
        detCriteria.add(Restrictions.eq("carregDesc.tpOperacao", tpOperacao));
        detCriteria.add(Restrictions.eq("filial.idFilial", SessionUtils.getFilialSessao().getIdFilial()));
        detCriteria.add(Restrictions.eq(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA, idControleCarga));
        detCriteria.add(Restrictions.eqProperty("volumeNF.idVolumeNotaFiscal", "vnf.idVolumeNotaFiscal"));

        if (idCarregamentoDescarga != null) {
            detCriteria.add(Restrictions.eq("carregDesc.idCarregamentoDescarga", idCarregamentoDescarga));
        }
        return detCriteria;
    }

    /**
     * CQPRO00025051
     * Gera Eventos para os volumes do manifesto;
     *
     * @param idManifesto
     * @param idControleCarga
     * @param idCarregamentoDescarga
     */
    private void generateEventoVolumeFimDescargaEntrega(Long idManifesto, Long idControleCarga, Long idCarregamentoDescarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        /** Otimização LMS-807 */
        final Map<String, String> alias = new HashMap<String, String>();
        alias.put(ConsCarregamentoDescarga.VNF_LOCALIZACAO_MERCADORIA, "loc");

        final List<Criterion> criterion = new ArrayList<Criterion>();
        criterion.add(Restrictions.in(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                new Short[]{ConstantesSim.CD_MERCADORIA_EM_DESCARGA, ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA}));
        criterion.add(Restrictions.ne(ConsCarregamentoDescarga.LOC_CD_LOCALIZACAO_MERCADORIA,
                ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA));

        DetachedCriteria detCriteria = buildQueryCarregamentoDescargaScan(idControleCarga, idCarregamentoDescarga, "D");

        criterion.add(Subqueries.notExists(detCriteria));

        /** Busca Volumes com ENTREGA DIFERENTE DE EFETUADA */
        final List<VolumeNotaFiscal> volumesNotaFiscal = volumeNotaFiscalService.findByIdManifesto(idManifesto, alias, criterion);
        if(volumesNotaFiscal != null && !volumesNotaFiscal.isEmpty()) {
            EventoVolumeGenerator gen = eventoVolumeService.newEventoVolumeGenerator();
            for (final VolumeNotaFiscal volumeNotaFiscal : volumesNotaFiscal) {
                final Short cdLocalizacaoMercadoria = volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();

                gen.cdEvento(ConstantesSim.CD_MERCADORIA_EM_DESCARGA.equals(cdLocalizacaoMercadoria) || ConstantesSim.CD_MERCADORIA_RETORNADA_EM_DESCARGA.equals(cdLocalizacaoMercadoria) ?
                        ConstantesSim.EVENTO_FIM_DESCARGA : ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO)
                        .generate(volumeNotaFiscal, adsmNativeBatchSqlOperations);

                getDao().getAdsmHibernateTemplate().evict(volumeNotaFiscal);
            }
        }

        /** Busca Dispositivos EM DESCARGA */
        final List<DispositivoUnitizacao> dispositivosUnitizacao =
                dispositivoUnitizacaoService.findByCarregamentoDescargaManifesto(idCarregamentoDescarga, idManifesto,
                        Arrays.asList(ConstantesSim.CD_MERCADORIA_EM_DESCARGA), "C");

        for (final DispositivoUnitizacao dispositivoUnitizacao : dispositivosUnitizacao) {
            incluirEventosRastreabilidadeInternacionalService.generateEventoDispositivoUnitizacaoComBatch(
                    dispositivoUnitizacao, ConstantesSim.CD_MERCADORIA_EM_CARREGAMENTO_VIAGEM, null, adsmNativeBatchSqlOperations);
        }
    }

    /**
     * CQPRO00025050
     *
     * @param idControleCarga
     * @param idCarregamentoDescarga
     */
    public void executeDesfazerFinalizarCarregamentoPreManifestos(long idControleCarga, long idCarregamentoDescarga) {
        ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        CarregamentoDescarga carregamentoDescarga = findById(idCarregamentoDescarga);

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("carregamentoDescarga.idCarregamentoDescarga", idCarregamentoDescarga);

        List<CarregamentoPreManifesto> carregamentoPreManifestoList =
                carregamentoPreManifestoService.findByIdCarregamentoDescarga(idCarregamentoDescarga);

        for (CarregamentoPreManifesto carregamentoPreManifesto : carregamentoPreManifestoList) {
            //Captura os objetos...
            Manifesto manifesto = manifestoService.findByIdInitLazyProperties(carregamentoPreManifesto.getManifesto().getIdManifesto(), false);
            Filial filial = filialService.findByIdInitLazyProperties(SessionUtils.getFilialSessao().getIdFilial(), false);
            Usuario usuario = usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());

            //Edita o manifesto
            manifesto.setTpStatusManifesto(new DomainValue("EC"));
            carregamentoPreManifesto.setDhFimCarregamento(null);

            List<PreManifestoDocumento> listPreManifestoDocumentos = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(manifesto.getIdManifesto());
            Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
            String strPreManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifesto.getNrPreManifesto().toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);


            for (PreManifestoDocumento preManifestoDocumento : listPreManifestoDocumentos) {
                DoctoServico docto = preManifestoDocumento.getDoctoServico();

                if (ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_VIAGEM.equals(docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
                    incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM, docto.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHora, null, filialUsuarioLogado.getSiglaNomeFilial(), "PMV");
                } else if (ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_ENTREGA.equals(docto.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
                    incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA, docto.getIdDoctoServico(), filialUsuarioLogado.getIdFilial(), strPreManifesto, dataHora, null, filialUsuarioLogado.getSiglaNomeFilial(), "PME");
                }
            }

            //Gera um novo evento manifesto...
            EventoManifesto eventoManifesto = new EventoManifesto();
            eventoManifesto.setDhEvento(dataHora);
            eventoManifesto.setTpEventoManifesto(new DomainValue("IC"));
            eventoManifesto.setFilial(filial);
            eventoManifesto.setManifesto(manifesto);
            eventoManifesto.setUsuario(usuario);

            //Persiste objetos...
            carregamentoPreManifestoService.store(carregamentoPreManifesto);
            manifestoService.storeBasic(manifesto);
            eventoManifestoService.storeBasic(eventoManifesto);


            if ("CC".equals(manifesto.getTpStatusManifesto().getValue())) {
                //Verifica o tipo de controle de carga para gerar um codigo de evento especifico para o mesmo...
                Short cdEvento = null;
                String tpDocumento = null;
                if (controleCarga.getTpControleCarga().getValue().equals(ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM)) {
                    cdEvento = ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_VIAGEM;
                    tpDocumento = "PMV"; //Pre-Manifesto Viagem
                } else {
                    cdEvento = ConstantesSim.EVENTO_CANCELAMENTO_CAREGAMENTO_COLETA_ENTREGA;
                    tpDocumento = "PME"; //Pre-Manifesto Entrega
                }
                this.generateEventoDocumentoServicoByIdPreManifesto(manifesto.getIdManifesto(), cdEvento, tpDocumento, dataHora);

                //[CQ25050] Gera eventos de volume
                //TODO lucianos - esse trecho pode ser substituido pelo  método generateEventoVolume(manifesto, cdEvento)
                //realizar a substituição após o storeFinalizarCarregamentoPreManifesto entrar em PD
                List<PreManifestoVolume> listaPreManifestoVolume = preManifestoVolumeService.findByManifesto(manifesto.getIdManifesto());
                generateEventoVolumeFromPreManifestoVolume(cdEvento, listaPreManifestoVolume);

                //TODO lucianos - esse trecho pode ser substituido pelo  método generateEventoDispo sitivo(manifesto, cdEvento, "LM");
                //realizar a substituição após o storeFinalizarCarregamentoPreManifesto entra em PD

                List<DispCarregIdentificado> listaDCargIdent = dispCarregIdentificadoService.findDispCarregIdentificadoByIdManifesto(manifesto.getIdManifesto());
                for (DispCarregIdentificado dispCarregIdentificado : listaDCargIdent) {
                    eventoDispositivoUnitizacaoService.generateEventoDispositivo
                            (dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), cdEvento, "LM");
                }

            }
        }
        carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
        store(carregamentoDescarga);
    }

    public List<Map<String, Object>> findManifestosByIdControleCarga(Map<String, Object> criteria) {
        List<Map<String, Object>> grid = new ArrayList<Map<String, Object>>();
        String totalVolumesTealAux = (String) criteria.get(ConsCarregamentoDescarga.TOTAL_VOLUMES);
        totalVolumesTealAux = totalVolumesTealAux.replaceAll("\\.", "");
        Integer totalVolumesTela = Integer.valueOf(totalVolumesTealAux);
        if (totalVolumesTela != null && totalVolumesTela.compareTo(0) > 0) {
            List<Conhecimento> conhecimentos = getCarregamentoDescargaDAO().findManifestosByIdControleCarga(criteria);

            if (!conhecimentos.isEmpty()) {
                for (Conhecimento conhecimento : conhecimentos) {
                    addGridFromConhecimento(criteria, grid, conhecimento);
                }
            }
        }
        return grid;
    }

    private void addGridFromConhecimento(Map<String, Object> criteria, List<Map<String, Object>> grid, Conhecimento conhecimento) {
        Map<String, Object> gridLinha = new HashMap<String, Object>();

        if (grid.isEmpty()) {
            gridLinha.put(ConsCarregamentoDescarga.NR_CONTROLE_CARGA, criteria.get(ConsCarregamentoDescarga.NR_CONTROLE_CARGA));
            gridLinha.put(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM, criteria.get(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM));
        }

        //if descarga / entrega
        Long nrDoctoServico = conhecimento.getNrDoctoServico();
        String sgFilialDocumentoServico = conhecimento.getFilialOrigem().getSgFilial();
        Integer quantidadeVolumes = 0;
        BigDecimal totalPeso = BigDecimal.ZERO;

        Integer totalContagemVolumes = 0;
        BigDecimal notaFiscalConhecimentoVlTotal = BigDecimal.ZERO;
        BigDecimal notaFiscalConhecimentoQtVolumes = BigDecimal.ZERO;
        quantidadeVolumes = conhecimento.getQtVolumes();
        if ("D".equals(criteria.get(ConsCarregamentoDescarga.TP_OPERACAO))) {
            if ("C".equals(criteria.get(ConsCarregamentoDescarga.TP_CONTROLE_CARGA))) {
                List<ManifestoEntregaVolume> manifestoEntregaVolumes = (List<ManifestoEntregaVolume>) getCarregamentoDescargaDAO()
                        .findManifestoVolumes(ManifestoEntregaVolume.class, conhecimento.getIdDoctoServico(), criteria);
                totalContagemVolumes = manifestoEntregaVolumes.size();
                totalPeso = addTotalPesoManifestoEntregaVolume(totalPeso, manifestoEntregaVolumes);
                List<NotaFiscalConhecimento> notaFiscalConhecimentos = (List<NotaFiscalConhecimento>) getCarregamentoDescargaDAO()
                        .findNotaFiscalConhecimento(ManifestoEntregaDocumento.class, conhecimento.getIdDoctoServico(), criteria);
                for (NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos) {
                    notaFiscalConhecimentoVlTotal = addVlNotaFiscalCOnhecimentoVlTotal(notaFiscalConhecimentoVlTotal, notaFiscalConhecimento);
                    notaFiscalConhecimentoQtVolumes = addQtNotaFiscalConhecimentoQtVolume(notaFiscalConhecimentoQtVolumes, notaFiscalConhecimento);
                }

            } else {
                List<ManifestoNacionalVolume> manifestoEntregaVolumes = (List<ManifestoNacionalVolume>) getCarregamentoDescargaDAO()
                        .findManifestoVolumes(ManifestoNacionalVolume.class, conhecimento.getIdDoctoServico(), criteria);
                totalContagemVolumes = manifestoEntregaVolumes.size();
                totalPeso = addTotalPesoManifestoNacionalVolume(totalPeso, manifestoEntregaVolumes);
                List<NotaFiscalConhecimento> notaFiscalConhecimentos = (List<NotaFiscalConhecimento>) getCarregamentoDescargaDAO()
                        .findNotaFiscalConhecimento(ManifestoNacionalCto.class, conhecimento.getIdDoctoServico(), criteria);
                for (NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos) {
                    notaFiscalConhecimentoVlTotal = addVlNotaFiscalCOnhecimentoVlTotal(notaFiscalConhecimentoVlTotal, notaFiscalConhecimento);
                    notaFiscalConhecimentoQtVolumes = addQtNotaFiscalConhecimentoQtVolume(notaFiscalConhecimentoQtVolumes, notaFiscalConhecimento);
                }
            }
        } else {

            List<PreManifestoVolume> manifestoEntregaVolumes = (List<PreManifestoVolume>) getCarregamentoDescargaDAO()
                    .findManifestoVolumes(PreManifestoVolume.class, conhecimento.getIdDoctoServico(), criteria);
            totalContagemVolumes = manifestoEntregaVolumes.size();
            totalPeso = addTotalPesoPreManifestoVolume(totalPeso, manifestoEntregaVolumes);
            List<NotaFiscalConhecimento> notaFiscalConhecimentos = (List<NotaFiscalConhecimento>) getCarregamentoDescargaDAO()
                    .findNotaFiscalConhecimento(PreManifestoDocumento.class, conhecimento.getIdDoctoServico(), criteria);
            for (NotaFiscalConhecimento notaFiscalConhecimento : notaFiscalConhecimentos) {
                notaFiscalConhecimentoVlTotal = addVlNotaFiscalCOnhecimentoVlTotal(notaFiscalConhecimentoVlTotal, notaFiscalConhecimento);
                notaFiscalConhecimentoQtVolumes = addQtNotaFiscalConhecimentoQtVolume(notaFiscalConhecimentoQtVolumes, notaFiscalConhecimento);
            }
        }

        gridLinha.put(ConsCarregamentoDescarga.DOCUMENTO_SERVICO, nrDoctoServico);
        gridLinha.put(ConsCarregamentoDescarga.SG_FILIAL_DOCUMENTO_SERVICO, sgFilialDocumentoServico);
        gridLinha.put(ConsCarregamentoDescarga.TOTAL_VOLUMES, totalContagemVolumes + "/" + quantidadeVolumes);
        gridLinha.put(ConsCarregamentoDescarga.TOTAL_PESO, FormatUtils.formatDecimal("###,##0", totalPeso));
        BigDecimal perRepresentativoValorTotal = calcularPercentualValorTotal(totalContagemVolumes, notaFiscalConhecimentoVlTotal, notaFiscalConhecimentoQtVolumes);

        gridLinha.put(ConsCarregamentoDescarga.PER_REPRESENTATIVO_VALOR_TOTAL, FormatUtils.formatDecimal("#,###,###,###,##0.00", perRepresentativoValorTotal) + "%");

        String totalPercValor = (String) criteria.get("totalPercValor");
        BigDecimal totalPercValorAux = (BigDecimal) criteria.get("totalPercValorAux");
        totalPercValor = totalPercValor.replaceAll("%", "");

        gridLinha.put(ConsCarregamentoDescarga.ALERTA_VALOR_REPRESENTATIVO, setarImagemRepresentativoValorTotal(totalPercValorAux, perRepresentativoValorTotal));

        grid.add(gridLinha);
    }

    private BigDecimal addTotalPesoPreManifestoVolume(BigDecimal totalPeso, List<PreManifestoVolume> preManifestoVolumes) {
        BigDecimal totalPesoAux = totalPeso;
        for (PreManifestoVolume manifestoVolume : preManifestoVolumes) {
            if (manifestoVolume.getVolumeNotaFiscal() != null && manifestoVolume.getVolumeNotaFiscal().getPsAferido() != null) {
                totalPesoAux = totalPesoAux.add(manifestoVolume.getVolumeNotaFiscal().getPsAferido());
            }
        }
        return totalPesoAux;
    }

    private BigDecimal addTotalPesoManifestoNacionalVolume(BigDecimal totalPeso, List<ManifestoNacionalVolume> manifestoNacionalVolumes) {
        BigDecimal totalPesoAux = totalPeso;
        for (ManifestoNacionalVolume manifestoVolume : manifestoNacionalVolumes) {
            if (manifestoVolume.getVolumeNotaFiscal() != null && manifestoVolume.getVolumeNotaFiscal().getPsAferido() != null) {
                totalPesoAux = totalPesoAux.add(manifestoVolume.getVolumeNotaFiscal().getPsAferido());
            }
        }
        return totalPesoAux;
    }

    private BigDecimal addTotalPesoManifestoEntregaVolume(BigDecimal totalPeso, List<ManifestoEntregaVolume> manifestoEntregaVolumes) {
        BigDecimal totalPesoAux = totalPeso;
        for (ManifestoEntregaVolume manifestoVolume : manifestoEntregaVolumes) {
            if (manifestoVolume.getVolumeNotaFiscal() != null && manifestoVolume.getVolumeNotaFiscal().getPsAferido() != null) {
                totalPesoAux = totalPesoAux.add(manifestoVolume.getVolumeNotaFiscal().getPsAferido());
            }
        }
        return totalPesoAux;
    }

    private BigDecimal addVlNotaFiscalCOnhecimentoVlTotal(BigDecimal notaFiscalConhecimentoVlTotal, NotaFiscalConhecimento notaFiscalConhecimento) {
        BigDecimal notaFiscalConhecimentoVlTotalAux = notaFiscalConhecimentoVlTotal;
        if (notaFiscalConhecimentoVlTotalAux != null) {
            notaFiscalConhecimentoVlTotalAux = notaFiscalConhecimentoVlTotal.add(notaFiscalConhecimento.getVlTotal());
        }
        return notaFiscalConhecimentoVlTotalAux;
    }

    private BigDecimal addQtNotaFiscalConhecimentoQtVolume(BigDecimal notaFiscalConhecimentoQtVolumes, NotaFiscalConhecimento notaFiscalConhecimento) {
        BigDecimal notaFiscalConhecimentoQtVolumesAux = notaFiscalConhecimentoQtVolumes;
        if (notaFiscalConhecimento.getQtVolumes() != null) {
            notaFiscalConhecimentoQtVolumesAux = notaFiscalConhecimentoQtVolumes.add(new BigDecimal(notaFiscalConhecimento.getQtVolumes()));
        }
        return notaFiscalConhecimentoQtVolumesAux;
    }

    private String setarImagemRepresentativoValorTotal(BigDecimal totalPercValor, BigDecimal perRepresentativoValorTotal) {
        BigDecimal totalPercValorAux = totalPercValor;
        if (totalPercValorAux.compareTo(new BigDecimal(ConsCarregamentoDescarga.VAL_PERCENTUAL)) > 0) {
            totalPercValorAux = totalPercValor.subtract(new BigDecimal(ConsCarregamentoDescarga.VAL_PERCENTUAL));
            if (perRepresentativoValorTotal.compareTo(totalPercValorAux) > 0) {
                return ConsCarregamento.IMAGE_BOLA_VERMELHA_GIF;
            } else {
                return ConsCarregamento.IMAGE_BOLA_VERDE_GIF;
            }
        } else {
            return ConsCarregamento.IMAGE_BOLA_VERDE_GIF;
        }
    }

    public ResultSetPage<CarregamentoDescarga> findPaginated(PaginatedQuery paginatedQuery) {
        return getCarregamentoDescargaDAO().findPaginated(paginatedQuery);
    }

    public ResultSetPage<Map<String, Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {
        ResultSetPage rsp = this.findPaginated(paginatedQuery);

        List<CarregamentoDescarga> list = rsp.getList();
        List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>(list.size());

        for (CarregamentoDescarga carregamento : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ConsCarregamentoDescarga.ID_CARREGAMENTO_DESCARGA, carregamento.getIdCarregamentoDescarga());
            map.put(ConsCarregamentoDescarga.ID_CONTROLE_CARGA, carregamento.getControleCarga().getIdControleCarga());
            map.put(ConsCarregamentoDescarga.NR_CONTROLE_CARGA, carregamento.getControleCarga().getNrControleCarga());
            map.put(ConsCarregamentoDescarga.SG_FILIAL_ORIGEM, carregamento.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
            map.put(ConsCarregamentoDescarga.TP_STATUS_OPERACAO, carregamento.getTpStatusOperacao().getDescription());
            map.put(ConsCarregamentoDescarga.DH_INICIO_OPERACAO, carregamento.getDhInicioOperacao());
            map.put("dhInicioOperacaoTela", JTDateTimeUtils.formatDateTimeToString(carregamento.getDhInicioOperacao()));
            map.put(ConsCarregamentoDescarga.DH_FIM_OPERACAO, carregamento.getDhFimOperacao());
            map.put(ConsCarregamentoDescarga.TP_OPERACAO, carregamento.getTpOperacao().getDescription());
            map.put(ConsCarregamentoDescarga.TP_CONTROLE_CARGA, carregamento.getControleCarga().getTpControleCarga().getDescription());

            if (carregamento.getBox() != null) {
                map.put("nrBox", carregamento.getBox().getNrBox());
                map.put("nrDoca", carregamento.getBox().getDoca().getNrDoca());
                map.put("nmPessoaTerminal", carregamento.getBox().getDoca().getTerminal().getPessoa().getNmPessoa());
            }
            BigDecimal capacidadeMedia = null;
            if (carregamento.getControleCarga().getMeioTransporteByIdTransportado() != null) {
                map.put("nrIdentificador", carregamento.getControleCarga().getMeioTransporteByIdTransportado().getNrIdentificador());
                TipoMeioTransporte tipoMeioTransporte = carregamento.getControleCarga().getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte();
                map.put("tipoVeiculo", tipoMeioTransporte.getDsTipoMeioTransporte());
                capacidadeMedia = (new BigDecimal(tipoMeioTransporte.getNrCapacidadePesoInicial())).add(new BigDecimal(tipoMeioTransporte.getNrCapacidadePesoFinal()));
            }

            if (carregamento.getControleCarga().getMeioTransporteByIdSemiRebocado() != null) {
                MeioTransporte meioTransporte = carregamento.getControleCarga().getMeioTransporteByIdSemiRebocado();
                TipoMeioTransporte tipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte();
                map.put("nrIdentificadorSemiReboque", meioTransporte.getNrIdentificador());
                map.put("tipoVeiculo", tipoMeioTransporte.getDsTipoMeioTransporte());
                capacidadeMedia = (new BigDecimal(tipoMeioTransporte.getNrCapacidadePesoInicial())).add(new BigDecimal(tipoMeioTransporte.getNrCapacidadePesoFinal()));
            }
            if (capacidadeMedia != null) {
                capacidadeMedia = capacidadeMedia.divide(new BigDecimal(ConsCarregamentoDescarga.SIZE_2));
                map.put("capacidadeMediaKg", FormatUtils.formatDecimal("##,##0", capacidadeMedia));
            }

            if (ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM.equals(paginatedQuery.getCriteria().get(ConsCarregamentoDescarga.TP_CONTROLE_CARGA))) {
                map.put("filialRotaDestino", carregamento.getControleCarga().getFilialByIdFilialDestino().getSgFilial());
            } else {
                map.put("filialRotaDestino", carregamento.getControleCarga().getRotaColetaEntrega().getNrRota());
            }

            Integer totalVolumes = 0;
            BigDecimal peso = BigDecimal.ZERO;
            BigDecimal notaFiscalConhecimentoVlTotal = null;
            BigDecimal notaFiscalConhecimentoQtVolumes = null;

            if ("C".equals(paginatedQuery.getCriteria().get(ConsCarregamentoDescarga.TP_OPERACAO))) {
                for (Manifesto manifesto : carregamento.getControleCarga().getManifestos()) {
                    if (notaFiscalConhecimentoVlTotal == null && notaFiscalConhecimentoQtVolumes == null) {
                        notaFiscalConhecimentoVlTotal = BigDecimal.ZERO;
                        notaFiscalConhecimentoQtVolumes = BigDecimal.ZERO;
                    }

                    List<PreManifestoVolume> listaPreManifestoVolume = preManifestoVolumeService.findByManifesto(manifesto.getIdManifesto());
                    peso = addTotalPesoPreManifestoVolume(peso, listaPreManifestoVolume);
                    for (PreManifestoDocumento preManifestoDocumento : manifesto
                            .getPreManifestoDocumentos()) {
                        List<NotaFiscalConhecimento> listaNotaFiscalConhecimento = notaFiscalConhecimentoService.findByConhecimento(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
                        for (NotaFiscalConhecimento notaFiscalConhecimento : listaNotaFiscalConhecimento) {
                            notaFiscalConhecimentoVlTotal = notaFiscalConhecimentoVlTotal.add(notaFiscalConhecimento.getVlTotal());
                            notaFiscalConhecimentoQtVolumes = notaFiscalConhecimentoQtVolumes.add(new BigDecimal(notaFiscalConhecimento.getQtVolumes()));
                        }
                    }
                    totalVolumes += listaPreManifestoVolume.size();
                }
            } else {
                if (ConstantesExpedicao.TP_CONTROLE_CARGA_VIAGEM.equals(paginatedQuery.getCriteria().get(ConsCarregamentoDescarga.TP_CONTROLE_CARGA))) {
                    for (Manifesto manifesto : carregamento.getControleCarga().getManifestos()) {
                        if (notaFiscalConhecimentoVlTotal == null && notaFiscalConhecimentoQtVolumes == null) {
                            notaFiscalConhecimentoVlTotal = BigDecimal.ZERO;
                            notaFiscalConhecimentoQtVolumes = BigDecimal.ZERO;
                        }
                        manifesto = getCarregamentoDescargaDAO().findManifestoViagem(manifesto.getIdManifesto(), (Long) paginatedQuery.getCriteria().get(ConsFilial.ID_FILIAL), carregamento.getDhInicioOperacao(), carregamento.getDhFimOperacao());//Manobra para evitar muitos selects
                        if (manifesto != null && manifesto.getManifestoViagemNacional() != null) {
                            totalVolumes += manifesto.getManifestoViagemNacional().getManifestoNacionalVolumes().size();
                            peso = addTotalPesoManifestoNacionalVolume(peso, manifesto.getManifestoViagemNacional().getManifestoNacionalVolumes());
                            for (ManifestoNacionalCto manifestoNacionalCto : manifesto.getManifestoViagemNacional().getManifestoNacionalCtos()) {
                                Long idDoctoServico = manifestoNacionalCto.getConhecimento().getIdDoctoServico();
                                List<NotaFiscalConhecimento> listaNotaFiscalConhecimento = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);
                                for (NotaFiscalConhecimento notaFiscalConhecimento : listaNotaFiscalConhecimento) {
                                    notaFiscalConhecimentoVlTotal = notaFiscalConhecimentoVlTotal.add(notaFiscalConhecimento.getVlTotal());
                                    notaFiscalConhecimentoQtVolumes = notaFiscalConhecimentoQtVolumes.add(new BigDecimal(notaFiscalConhecimento.getQtVolumes()));
                                }
                            }
                        }
                    }
                } else {
                    for (Manifesto manifesto : carregamento.getControleCarga().getManifestos()) {
                        if (notaFiscalConhecimentoVlTotal == null && notaFiscalConhecimentoQtVolumes == null) {
                            notaFiscalConhecimentoVlTotal = BigDecimal.ZERO;
                            notaFiscalConhecimentoQtVolumes = BigDecimal.ZERO;
                        }
                        manifesto = getCarregamentoDescargaDAO().findManifestoEntrega(manifesto.getIdManifesto(), (Long) paginatedQuery.getCriteria().get(ConsFilial.ID_FILIAL), carregamento.getDhInicioOperacao(), carregamento.getDhFimOperacao());
                        if (manifesto != null && manifesto.getManifestoEntrega() != null) {
                            totalVolumes += manifesto.getManifestoEntrega().getManifestoEntregaVolumes().size();
                            peso = addTotalPesoManifestoEntregaVolume(peso, (List<ManifestoEntregaVolume>) manifesto.getManifestoEntrega().getManifestoEntregaVolumes());
                            for (ManifestoEntregaDocumento manifestoEntregaDocumento : manifesto.getManifestoEntrega().getManifestoEntregaDocumentos()) {
                                List<NotaFiscalConhecimento> listaNotaFiscalConhecimento = notaFiscalConhecimentoService.findByConhecimento(manifestoEntregaDocumento.getDoctoServico().getIdDoctoServico());
                                for (NotaFiscalConhecimento notaFiscalConhecimento : listaNotaFiscalConhecimento) {
                                    notaFiscalConhecimentoVlTotal = notaFiscalConhecimentoVlTotal.add(notaFiscalConhecimento.getVlTotal());
                                    notaFiscalConhecimentoQtVolumes = notaFiscalConhecimentoQtVolumes.add(new BigDecimal(notaFiscalConhecimento.getQtVolumes()));
                                }
                            }
                        }
                    }
                }
            }
            map.put(ConsCarregamentoDescarga.TOTAL_VOLUMES, FormatUtils.formatDecimal("###,##0", totalVolumes));
            map.put(ConsCarregamentoDescarga.TOTAL_PESO, FormatUtils.formatDecimal("##,##0", peso));
            map.put("alertaPeso", setarImagemPeso(peso, capacidadeMedia));
            BigDecimal totalPercValor = BigDecimal.ZERO.subtract(BigDecimal.ONE);//iniciado com valor negativo para não exibir imagem.
            if (notaFiscalConhecimentoVlTotal != null && notaFiscalConhecimentoQtVolumes != null) {
                totalPercValor = calcularPercentualValorTotal(totalVolumes, notaFiscalConhecimentoVlTotal, notaFiscalConhecimentoQtVolumes);

                map.put("totalPercValor", FormatUtils.formatDecimal("#,###,###,###,##0.00", totalPercValor).toString() + "%");
                map.put("totalPercValorAux", totalPercValor);
            }
            map.put("alertaValor", setarImagemValor(totalPercValor));

            retorno.add(map);
        }
        rsp.setList(retorno);
        return rsp;
    }

    private String setarImagemValor(BigDecimal totalPercValor) {
        if (totalPercValor.compareTo(new BigDecimal(ConsCarregamentoDescarga.VAL_PERCENTUAL)) > 0) {
            return ConsCarregamento.IMAGE_BOLA_VERMELHA_GIF;
        } else if (totalPercValor.compareTo(new BigDecimal(ConsCarregamentoDescarga.METADE_VAL_PERCENTUAL)) > 0) {
            return ConsCarregamento.IMAGE_BOLA_AMARELA_GIF;
        } else if (totalPercValor.compareTo(BigDecimal.ZERO) >= 0) {
            return ConsCarregamento.IMAGE_BOLA_VERDE_GIF;
        } else {
            return ConsCarregamento.IMAGE_UNCHECKED_GIF;
        }
    }

    private String setarImagemPeso(BigDecimal peso, BigDecimal capacidadeMedia) {
        if (capacidadeMedia == null) {
            return ConsCarregamento.IMAGE_UNCHECKED_GIF;
        } else if (peso.compareTo(capacidadeMedia) > 0) {
            return ConsCarregamento.IMAGE_BOLA_VERMELHA_GIF;
        } else if (peso.compareTo(capacidadeMedia.divide(new BigDecimal(ConsCarregamentoDescarga.SIZE_2))) > 0) {
            return ConsCarregamento.IMAGE_BOLA_AMARELA_GIF;
        } else {
            return ConsCarregamento.IMAGE_BOLA_VERDE_GIF;
        }
    }

    /**
     * Calcula o percentual para a Coluna "Valor % Total".
     */
    private BigDecimal calcularPercentualValorTotal(Integer totalVolumes,
                                                    BigDecimal notaFiscalConhecimentoVlTotal,
                                                    BigDecimal notaFiscalConhecimentoQtVolumes) {
        BigDecimal vtmnc;
        BigDecimal percentual;
        if (notaFiscalConhecimentoQtVolumes.compareTo(new BigDecimal(0)) == 0) {
            vtmnc = BigDecimal.ZERO;
        } else {
            vtmnc = notaFiscalConhecimentoVlTotal.divide(notaFiscalConhecimentoQtVolumes, ConsCarregamentoDescarga.SCALE, BigDecimal.ROUND_UP);
            vtmnc = vtmnc.multiply(new BigDecimal(totalVolumes));
        }
        percentual = vtmnc.multiply(new BigDecimal(ConsCarregamentoDescarga.VAL_PERCENTUAL));
        percentual.setScale(ConsCarregamentoDescarga.SCALE);
        BigDecimal valorMaxCargaRodo = (BigDecimal) configuracoesFacade.getValorParametro("VALOR_MAX_CARGA_RODO");
        valorMaxCargaRodo.setScale(ConsCarregamentoDescarga.SCALE, BigDecimal.ROUND_UP);
        percentual = percentual.divide(valorMaxCargaRodo, ConsCarregamentoDescarga.SCALE, BigDecimal.ROUND_UP);
        return percentual.setScale(ConsCarregamentoDescarga.SCALE, BigDecimal.ROUND_UP);
    }

    public List<Object[]> findManifestosParaSOM(Long idMonitoramentoDescarga) {
        return getCarregamentoDescargaDAO().findManifestosParaSOM(idMonitoramentoDescarga);
    }

    public void storeAndValidateDescargaVeiculo(Long idControleCarga, EventoDescargaVeiculo eventoDescargaVeiculo) {
        ControleCarga controleCargaLoaded = controleCargaService.findById(idControleCarga);
        this.storeAndValidateDescargaVeiculo(controleCargaLoaded, eventoDescargaVeiculo);
    }

    public void storeAndValidateDescargaVeiculo(ControleCarga controleCargaLoaded, EventoDescargaVeiculo eventoDescargaVeiculo) {
        storeAndValidateDescargaVeiculo(controleCargaLoaded, eventoDescargaVeiculo, null);
    }

    public void storeAndValidateDescargaVeiculo(ControleCarga controleCargaLoaded, EventoDescargaVeiculo eventoDescargaVeiculo, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        if (!"C".equals(controleCargaLoaded.getTpControleCarga().getValue())) {
            return;
        }
        ControleQuilometragem ultimoControleQuilometragem = controleQuilometragemService.findUltimoControleQuilometragemByIdControleCarga(controleCargaLoaded.getIdControleCarga(), Boolean.FALSE);
        if (adsmNativeBatchSqlOperations != null) {
            getDao().getAdsmHibernateTemplate().evict(ultimoControleQuilometragem);
        }

        if (ultimoControleQuilometragem != null  && ultimoControleQuilometragem.getTpSituacaoPendencia() != null && ultimoControleQuilometragem.getPendencia() != null) {
            if ("A".equals(ultimoControleQuilometragem.getTpSituacaoPendencia().getValue())) {
                ultimoControleQuilometragem.setPendencia(null);
                if (adsmNativeBatchSqlOperations != null) {
                    adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM, ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM_ID_PENDENCIA, null);
                }

            } else {
                validateDescargaVeiculo(eventoDescargaVeiculo, ultimoControleQuilometragem);
                if (adsmNativeBatchSqlOperations != null) {
                    adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM, ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM_ID_PENDENCIA, null);
                    adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM, ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM_TP_SITUACAO_PENDENCIA, ultimoControleQuilometragem.getTpSituacaoPendencia().getValue());
                }
            }
            if (adsmNativeBatchSqlOperations != null) {
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsControleQuilometragem.DB_CONTROLE_QUILOMETRAGEM, ultimoControleQuilometragem.getIdControleQuilometragem());
            } else {
                controleQuilometragemService.store(ultimoControleQuilometragem); // save do controle quilometragem
            }
        }
    }

    private void validateDescargaVeiculo(EventoDescargaVeiculo eventoDescargaVeiculo, ControleQuilometragem ultimoControleQuilometragem) {
        String stPendencia = ultimoControleQuilometragem.getPendencia().getTpSituacaoPendencia().getValue();
        if ("A".equals(stPendencia)) {
            ultimoControleQuilometragem.setTpSituacaoPendencia(new DomainValue("A"));
            ultimoControleQuilometragem.setPendencia(null);
        } else if ("R".equals(stPendencia)) {
            throw new BusinessException("LMS-03021");
        } else if ("E".equals(stPendencia)) {
            if (EventoDescargaVeiculo.INICIAR_DESCARGAR.equals(eventoDescargaVeiculo)) {
                throw new BusinessException("LMS-03018");
            } else {
                throw new BusinessException("LMS-03019");
            }
        }
    }

    /**
     * LMS - 4460
     * Verificar qual processo está sendo feito e pegar o último, conforme data de Finalização
     *
     * @param idControleCarga
     * @return
     */
    public CarregamentoDescarga findDadosFechamentoOcorrenciaRNCs(Long idControleCarga) {
        return getCarregamentoDescargaDAO().findDadosFechamentoOcorrenciaRNCs(idControleCarga);
    }

    public CarregamentoDescarga findByControleCargaAndFilialRomaneio(ControleCarga controleCarga, Filial filialRomaneio) {
	    // TODO Auto-generated method stub
	return getCarregamentoDescargaDAO().findByControleCargaAndFilialRomaneio(controleCarga, filialRomaneio);
}    
    
    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public void setEventoDispositivoUnitizacaoService(EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
        this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
    }

    public void setRegistroPriorizacaoDoctoService(RegistroPriorizacaoDoctoService registroPriorizacaoDoctoService) {
        this.registroPriorizacaoDoctoService = registroPriorizacaoDoctoService;
    }

    public EventoVolumeService getEventoVolumeService() {
        return eventoVolumeService;
    }

    public void setJustificativaDoctoNaoCarregadoService(JustificativaDoctoNaoCarregadoService justificativaDoctoNaoCarregadoService) {
        this.justificativaDoctoNaoCarregadoService = justificativaDoctoNaoCarregadoService;
    }

    public void setGerarRateioFreteCarreteiroService(GerarRateioFreteCarreteiroService gerarRateioFreteCarreteiroService) {
        this.gerarRateioFreteCarreteiroService = gerarRateioFreteCarreteiroService;
    }

    public void setControleCargaDAO(ControleCargaDAO controleCargaDAO) {
        this.controleCargaDAO = controleCargaDAO;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setCarregamentoPreManifestoService(CarregamentoPreManifestoService carregamentoPreManifestoService) {
        this.carregamentoPreManifestoService = carregamentoPreManifestoService;
    }

    public void setBoxService(BoxService boxService) {
        this.boxService = boxService;
    }

    public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
        this.eventoMeioTransporteService = eventoMeioTransporteService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }

    public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
        this.eventoManifestoService = eventoManifestoService;
    }

    public void setEventoManifestoColetaService(EventoManifestoColetaService eventoManifestoColetaService) {
        this.eventoManifestoColetaService = eventoManifestoColetaService;
    }

    public void setEventoColetaService(EventoColetaService eventoColetaService) {
        this.eventoColetaService = eventoColetaService;
    }

    public void setEquipeService(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
        this.equipeOperacaoService = equipeOperacaoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
        this.controleQuilometragemService = controleQuilometragemService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
        this.manifestoColetaService = manifestoColetaService;
    }

    public void setDescargaManifestoService(DescargaManifestoService descargaManifestoService) {
        this.descargaManifestoService = descargaManifestoService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
        this.ocorrenciaColetaService = ocorrenciaColetaService;
    }

    public void setFotoCarregmtoDescargaService(FotoCarregmtoDescargaService fotoCarregmtoDescargaService) {
        this.fotoCarregmtoDescargaService = fotoCarregmtoDescargaService;
    }

    public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
        this.lacreControleCargaService = lacreControleCargaService;
    }

    public void setFotoService(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    public void setMotivoCancelDescargaService(MotivoCancelDescargaService motivoCancelDescargaService) {
        this.motivoCancelDescargaService = motivoCancelDescargaService;
    }

    public void setDispCarregIdentificadoService(DispCarregIdentificadoService dispCarregIdentificadoService) {
        this.dispCarregIdentificadoService = dispCarregIdentificadoService;
    }

    public void setEstoqueDispIdentificadoService(EstoqueDispIdentificadoService estoqueDispIdentificadoService) {
        this.estoqueDispIdentificadoService = estoqueDispIdentificadoService;
    }

    public void setEstoqueDispositivoQtdeService(EstoqueDispositivoQtdeService estoqueDispositivoQtdeService) {
        this.estoqueDispositivoQtdeService = estoqueDispositivoQtdeService;
    }

    public void setDispCarregDescQtdeService(DispCarregDescQtdeService dispCarregDescQtdeService) {
        this.dispCarregDescQtdeService = dispCarregDescQtdeService;
    }

    public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
        this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setTipoDispositivoUnitizacaoService(TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
        this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setGerarNotaCreditoService(GerarNotaCreditoService gerarNotaCreditoService) {
        this.gerarNotaCreditoService = gerarNotaCreditoService;
    }

    public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
        this.versaoDescritivoPceService = versaoDescritivoPceService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public void setControleCargaConhScanService(ControleCargaConhScanService controleCargaConhScanService) {
        this.controleCargaConhScanService = controleCargaConhScanService;
    }

    public void setOcorrenciaDoctoServicoService(
            OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public void setManifestoEntregaDocumentoService(
            ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public void setManifestoEletronicoService(ManifestoEletronicoService manifestoEletronicoService) {
        this.manifestoEletronicoService = manifestoEletronicoService;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public void setCarregamentoDescargaVolumeService(
            CarregamentoDescargaVolumeService carregamentoDescargaVolumeService) {
        this.carregamentoDescargaVolumeService = carregamentoDescargaVolumeService;
    }

    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public void setOcorrenciaNaoConformidadeService(
            OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
        this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
    }

    public PlanoGerenciamentoRiscoService getPlanoGerenciamentoRiscoService() {
        return planoGerenciamentoRiscoService;
    }

    public void setPlanoGerenciamentoRiscoService(
            PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
        this.planoGerenciamentoRiscoService = planoGerenciamentoRiscoService;
    }

    public PerfilUsuarioService getPerfilUsuarioService() {
        return perfilUsuarioService;
    }

    public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
        this.perfilUsuarioService = perfilUsuarioService;
    }

    public void setCiotService(CIOTService ciotService) {
        this.ciotService = ciotService;
    }

    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

	public void setSolicMonitPreventivoService(
			SolicMonitPreventivoService solicMonitPreventivoService) {
		this.solicMonitPreventivoService = solicMonitPreventivoService;
	}

	public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
		this.gerarEnviarSMPService = gerarEnviarSMPService;
	}
}

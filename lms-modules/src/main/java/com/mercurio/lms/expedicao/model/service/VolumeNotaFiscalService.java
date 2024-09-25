package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.*;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.util.AdsmHibernateUtils;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaVolumeService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.constantes.entidades.ConsVolumeNotaFiscal;
import com.mercurio.lms.expedicao.dto.QtdVolumesConhecimentoDto;
import com.mercurio.lms.expedicao.dto.UpdateVolumeDadosSorterDto;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.dao.VolumeNotaFiscalDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParcelaRecalculoSorter;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ParcelaRecalculoSorterService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.expedicao.volumeNotaFiscalService"
 */
public class VolumeNotaFiscalService extends CrudService<VolumeNotaFiscal, Long> {

    private static final String ID_CONHECIMENTO = "idConhecimento";
    private static final String ID_FILIAL_USUARIO = "idFilialUsuario";
    private static final String ID_DOCTO_SERVICO = "idDoctoServico";
    private static final String PS_AFERIDO = "psAferido";
    private static final String ID_VOLUME_NOTA_FISCAL = "idVolumeNotaFiscal";
    private static final String NR_VOLUME_COLETA = "nrVolumeColeta";
    private static final String IS_VOLUME_GM_DIRETO = "isVolumeGMDireto";
    private static final String SG_RED_FILIAL_ORIGEM = "sgRedFilialOrigem";
    private static final String SG_RED_FILIAL_DESTINO = "sgRedFilialDestino";
    private static final String ROUTE = "route";
    private static final String ID_FILIAL_ORIGEM = "idFilialOrigem";
    private static final String ID_SERVICO = "idServico";
    private static final String IS_DIMENSOES_CUBAGEM_OPCIONAL = "isDimensoesCubagemOpcional";
    private static final String ID_FILIAL_DESTINO = "idFilialDestino";
    private static final String TP_CTRC_PARCERIA = "tpCtrcParceria";
    private static final String DS_LOCAL_ENTREGA = "dsLocalEntrega";
    private static final String NR_DIAS_COLETA = "nrDiasColeta";
    private static final String NR_DIAS_ENTREGA = "nrDiasEntrega";
    private static final String NR_DIAS_TRANSFERENCIA = "nrDiasTransferencia";
    private static final String DT_PREVISTA_ENTREGA = "dtPrevistaEntrega";
    private static final String NR_DIAS_PREV_ENTREGA = "nrDiasPrevEntrega";
    private static final String NR_PRAZO = "nrPrazo";
    private static final String DT_PRAZO_ENTREGA = "dtPrazoEntrega";
    private static final String DANFE_SIMPLIFICADA = "danfeSimplificada";
    private static final String NR_IP = "nrIp";
    private static final String NR_PORT = "nrPort";
    private static final String TP_VOLUME = "tpVolume";
    private static final String LISTA_VOLUMES_D = "listaVolumesD";
    private static final String PARAMETRO_GERAL_DIAS_LIMITE_MWW = "DIAS_LIMITE_MWW";
    private static final String NM_DESTINATARIO = "nmDestinatario";
    private static final String TIPO_LOGRADOURO_DESTINATARIO = "tipoLogradouroDestinatario";
    private static final String ENDERECO_DESTINATARIO = "enderecoDestinatario";
    private static final String NR_ENDERECO_DESTINATARIO = "nrEnderecoDestinatario";
    private static final String COMPLEMENTO_ENDERECO_DESTINATARIO = "complementoEnderecoDestinatario";
    private static final String MUNICIPIO_DESTINATARIO = "municipioDestinatario";
    private static final String UNID_FEDERATIVA_DESTINATARIO = "unidFederativaDestinatario";
    private static final String PAIS_DESTINATARIO = "paisDestinatario";
    private static final String CEP_DESTINATARIO = "cepDestinatario";
    private static final String BAIRRO_DESTINATARIO = "bairroDestinatario";
    private static final String DH_EMISSAO = "dhEmissao";
    private static final String TP_SITUACAO_CONHECIMENTO = "tpSituacaoConhecimento";
    private static final int FILL_FIELD_WITH_4_ZEROS = 4;
    private static final int FILL_FIELD_WITH_3_ZEROS = 3;
    private static final int FILL_FIELD_WITH_9_ZEROS = 9;
    private static final int INT_CACHE_POS_0 = 0;
    private static final int INT_CACHE_POS_3 = 3;
    private static final int INT_CACHE_POS_4 = 4;
    private static final int INT_CACHE_POS_1 = 1;
    private static final int INT_CACHE_POS_2 = 2;

    private static final String NR_CONHECIMENTO = "nrConhecimento";
    private static final String COD_FILIAL_FILIAL_ORIGEM = "codFilialFilialOrigem";
    private static final String COD_FILIAL_DESTINO = "codFilialDestino";
    private static final String NR_SEQUENCIA = "nrSequencia";
    private static final String QT_VOLUMES = "qtVolumes";
    private static final String ROUTE_FINAL_DEPOT_NUMBER = "routeFinalDepotNumber";
    private static final String TP_DOCTO_SERVICO = "tpDoctoServico";
    private static final String NR_VOLUME_EMBARQUE = "nrVolumeEmbarque";
    private static final String IS_IMPRIME_ETIQUETA_REEMBARQUE = "isImprimeEtiquetaReembarque";
    private static final String QT_VOLUMES_TOTAL = "qtVolumesTotal";
    private static final String DIMENSAO_1 = "dimensao1";
    private static final String DIMENSAO_2 = "dimensao2";
    private static final String DIMENSAO_3 = "dimensao3";
    private static final String NR_SEQUENCIA_PALETE = "nrSequenciaPalete";
    private static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    private static final Logger LOGGER = LogManager.getLogger(VolumeNotaFiscalService.class);
    private static final String TP_CONHECIMENTO = "tpConhecimento";

    private static final String NOVA_ETIQUETA = "NOVA_ETIQUETA";

    private FluxoFilialService fluxoFilialService;
    private ImpressoraService impressoraService;
    private DpeService dpeService;
    private FilialService filialService;
    protected ConfiguracoesFacade configuracoesFacade;
    protected ConhecimentoService conhecimentoService;
    private CalcularFreteService calcularFreteService;
    private MonitoramentoDescargaService monitoramentoDescargaService;
    private ManifestoService manifestoService;
    private EventoVolumeService eventoVolumeService;
    private LiberacaoDocServService liberacaoDocServService;
    private DoctoServicoService doctoServicoService;
    private EmitirDocumentoService emitirDocumentoService;
    private VolumeSobraFilialService volumeSobraFilialService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private TabelaDivisaoClienteService tabelaDivisaoClienteService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private DevedorDocServService devedorDocServService;
    private RecalculoFreteService recalculoFreteService;
    private ParcelaRecalculoSorterService parcelaRecalculoSorterService;
    private PaisService paisService;
    private CarregamentoDescargaService carregamentoDescargaServie;
    private CarregamentoDescargaVolumeService carregamentoDescargaVolumeService;
    private DoctoServicoPPEPadraoService doctoServicoPPEPadraoService;
    private ParametroGeralService parametroGeralService;
    private ClienteService clienteService;
    private EventoService eventoService;

    private static final BigDecimal million = new BigDecimal("1000000");

    /**
     * Recupera uma instância de <code>VolumeNotaFiscal</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public VolumeNotaFiscal findById(java.lang.Long id) {
        return (VolumeNotaFiscal) super.findById(id);
    }

    /**
     * Retorna uam lista de VolumeNotaFiscal com o nrVolumeColeta
     */
    public List findByNrVolumeColeta(String nrVolumeColeta) {
        return getVolumeNotaFiscalDAO().findByNrVolumeColeta(nrVolumeColeta);
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
    public java.io.Serializable store(VolumeNotaFiscal bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setVolumeNotaFiscalDAO(VolumeNotaFiscalDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolumeNotaFiscalDAO getVolumeNotaFiscalDAO() {
        return (VolumeNotaFiscalDAO) getDao();
    }

    /**
     * Busca o somatório de Peso Aferido dos Volumes
     *
     * @param idNotaFiscalConhecimento
     * @return
     * @author André Valadas
     */
    public BigDecimal findTotalPsAferidoByIdNotaFiscalConhecimento(final Long idNotaFiscalConhecimento) {
        return getVolumeNotaFiscalDAO().findTotalPsAferidoByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
    }

    /**
     * Busca o maior de Peso Aferido dos Volumes
     *
     * @param idDoctoServico
     * @return
     */
    public BigDecimal findMaxPsAferidoByIdDoctoServico(final Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findMaxPsAferidoByIdDoctoServico(idDoctoServico);
    }
    
    public void validateIntervaloEtiquetasNaoUsado(List<Map<String, Object>> notasFiscaisConhecimento) {
        if (notasFiscaisConhecimento != null) {
            String nrVolumeInicial = notasFiscaisConhecimento.get(0).get("nrVolumeColetaInicial") != null ? "nrVolumeColetaInicial" : "nrVolumeEtiquetaInicial";
            String nrVolumeFinal = notasFiscaisConhecimento.get(0).get("nrVolumeColetaFinal") != null ? "nrVolumeColetaFinal" : "nrVolumeEtiquetaFinal";
            for (Map<String, Object> notaFiscal : notasFiscaisConhecimento) {
                Long count = 0L;
                if (notaFiscal.get(nrVolumeInicial) != null && notaFiscal.get(nrVolumeFinal) != null) {
                    String nrVolumeColetaInicial = notaFiscal.get(nrVolumeInicial).toString();
                    String nrVolumeColetaFinal = notaFiscal.get(nrVolumeFinal).toString();
                    if (nrVolumeColetaInicial == null || nrVolumeColetaFinal == null) {
                        throw new BusinessException("LMS-04218");
                    }
                    if (NumberUtils.isNumber(nrVolumeColetaFinal) && NumberUtils.isNumber(nrVolumeColetaInicial)) {
                        count = getVolumeNotaFiscalDAO().countVolumesByIntervaloEtiquetas(nrVolumeColetaInicial, nrVolumeColetaFinal);
                    }
                }
                if (count == null || count.compareTo(0L) > 0) {
                    throw new BusinessException("LMS-04223", new Object[]{notaFiscal.get("nrNotaFiscal")});
                }
            }
        }
    }

    public List findAll() {
        return getVolumeNotaFiscalDAO().findAll();
    }

    public List<VolumeNotaFiscal> findfindByIdConhecimento(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findByIdConhecimento(idConhecimento);
    }
    
    public List<VolumeNotaFiscal> findfindByIdNotaFiscal(Long idNotaFiscal) {
        return getVolumeNotaFiscalDAO().findByIdNotaFiscal(idNotaFiscal);
    }

    /**
     * Metodo para retornar a informação do find dados volumes.
     * A construção do retorno para a action não está mais sendo feita aqui para evitar problemas de concorrência,
     * visto que os objetos carregados pelo findDadosVolumes permaneciam vinculados ao hibernate.
     *
     * @param paginatedQuery
     * @return
     */
    public List executeFindDadosVolumes(PaginatedQuery paginatedQuery, String idCliente) {

        AdsmHibernateUtils.setFlushModeToManual(getVolumeNotaFiscalDAO().getAdsmHibernateTemplate());
        Boolean isDanfeSimplificada = isDanfeSimplificada(idCliente);

        List result = getVolumeNotaFiscalDAO().findDadosVolumes(paginatedQuery, false, false);
        buildMapFindDadosVolume(paginatedQuery, result, false, isDanfeSimplificada);

        AdsmHibernateUtils.setFlushModeToAutoComFlushEClear(getVolumeNotaFiscalDAO().getAdsmHibernateTemplate());

        return result;
    }

    /**
     * Metodo para retornar a informação do find dados volume.
     * A construção do retorno para a action não está mais sendo feita aqui para evitar problemas de concorrência,
     * visto que os objetos carregados pelo findDadosVolumes permaneciam vinculados ao hibernate.
     *
     * @param paginatedQuery
     * @param isImprimeEtiquetaReembarque
     * @return
     */
    public List executeFindDadosVolume(PaginatedQuery paginatedQuery, Boolean isImprimeEtiquetaReembarque, String idCliente) {

        AdsmHibernateUtils.setFlushModeToCommit(getVolumeNotaFiscalDAO().getAdsmHibernateTemplate());
        Boolean isDanfeSimplificada = isDanfeSimplificada(idCliente);

        List result = getVolumeNotaFiscalDAO().findDadosVolume(paginatedQuery, isImprimeEtiquetaReembarque, isDanfeSimplificada);

        if(isDanfeSimplificada && !result.isEmpty() && result.size() > 1){
            //SWT nao reconhece subArray
            List subList = new ArrayList();
            subList.add(result.get(0));
            result = subList;
        }

        buildMapFindDadosVolume(paginatedQuery, result, false, isDanfeSimplificada);

        if (isImprimeEtiquetaReembarque) {
            atualizaNrVolumeEmbarque(result, isImprimeEtiquetaReembarque);
        }

        AdsmHibernateUtils.setFlushModeToAutoComFlushEClear(getVolumeNotaFiscalDAO().getAdsmHibernateTemplate());

        return result;
    }

    public Boolean validateImprimeEtiquetaReembarque(Long idFilial, String nrVolumeColeta) {
        return getVolumeNotaFiscalDAO().validateImprimeEtiquetaReembarque(idFilial, nrVolumeColeta);
    }


    public void atualizaNrVolumeEmbarque(List result, boolean isImprimeEtiquetaReembarque) {
        for (Object volumeNotaFiscal : result) {
            Map<String, Object> mapa = (Map<String, Object>) volumeNotaFiscal;
            Long idVolumeNotaFiscal = (Long) mapa.get(ID_VOLUME_NOTA_FISCAL);
            Long nrConhecimento = (Long) mapa.get(NR_CONHECIMENTO);
            Integer codFilialOrigem = (Integer) mapa.get(COD_FILIAL_FILIAL_ORIGEM);
            Integer codFilialDestino = (Integer) mapa.get(COD_FILIAL_DESTINO);
            Integer nrSequencia = (Integer) mapa.get(NR_SEQUENCIA);
            Integer qtVolumes = (Integer) mapa.get(QT_VOLUMES);
            Short routeFinalDepotNumber = (Short) mapa.get(ROUTE_FINAL_DEPOT_NUMBER);
            DomainValue tpDoctoServico = new DomainValue(mapa.get(TP_DOCTO_SERVICO).toString());

            String nrVolumeEmbarque = buildLabelBarCode(nrConhecimento, codFilialOrigem, codFilialDestino, nrSequencia, qtVolumes, null, routeFinalDepotNumber, null, tpDoctoServico, null, false);

            mapa.put(NR_VOLUME_EMBARQUE, nrVolumeEmbarque);
            mapa.put(IS_IMPRIME_ETIQUETA_REEMBARQUE, isImprimeEtiquetaReembarque);

            getVolumeNotaFiscalDAO().updateNrVolumeEmbarqueById(idVolumeNotaFiscal, nrVolumeEmbarque);
        }
    }

    public Map updateDataById(Map<String, Object> parameters) {

        boolean isVolumeGMDireto = false;
        BigDecimal psAferido = parameters.get(PS_AFERIDO) != null ? new BigDecimal((String) parameters.get(PS_AFERIDO)) : null;
        Long idVolumeNotaFiscal = (Long) parameters.get(ID_VOLUME_NOTA_FISCAL);
        Long nrPreConhecimento = (Long) parameters.get("nrPreConhecimento");
        Long nrConhecimento = (Long) parameters.get(NR_CONHECIMENTO);
        Long idFilialOrigem = SessionUtils.getFilialSessao().getIdFilial();
        Integer statusBalanca = (Integer) parameters.get("statusBalanca");
        Boolean isBalancaManual = statusBalanca != null ? statusBalanca == 1 : true;
        List<LiberacaoDocServ> liberacaoDocServs = (List<LiberacaoDocServ>) parameters.get("liberacaoDocServs");
        Integer qtVolumes = (Integer) parameters.get(QT_VOLUMES);
        Integer qtVolumesPalete = (Integer) parameters.get("qtVolumesPalete");
        Boolean blPaletizacao = (Boolean) parameters.get("blPaletizacao");
        Boolean blPesagemPalete = (Boolean) parameters.get("blPesagemPalete");
        String tpVolume = (String) parameters.get(TP_VOLUME);
        Integer qtVolumesVolume = (Integer) parameters.get("qtVolumesVolume");
        Integer codFilialFilialOrigem = (Integer) parameters.get(COD_FILIAL_FILIAL_ORIGEM);
        Integer codFilialDestino = (Integer) parameters.get(COD_FILIAL_DESTINO);
        Integer nrSequencia = (Integer) parameters.get(NR_SEQUENCIA);
        Short routeFinalDepotNumber = (Short) parameters.get(ROUTE_FINAL_DEPOT_NUMBER);
        Long idDoctoServico = (Long) parameters.get(ID_DOCTO_SERVICO);
        Boolean blEstacaoAutomatizada = (Boolean) parameters.get("blEstacaoAutomatizada");
        DomainValue tpDoctoServico = (DomainValue) parameters.get(TP_DOCTO_SERVICO);
        Integer nrDimensao1Cm = (Integer) parameters.get("nrDimensao1Cm");
        Integer nrDimensao2Cm = (Integer) parameters.get("nrDimensao2Cm");
        Integer nrDimensao3Cm = (Integer) parameters.get("nrDimensao3Cm");
        Double nrCubagemM3 = (Double) parameters.get("nrCubagemM3");
        String dsMac = (String) parameters.get("dsMac");
        Long idUsuario = (Long) parameters.get("idUsuario");
        Long idFilial = (Long) parameters.get("idFilial");
        Boolean blPesagemOpcional = (Boolean) parameters.get("blPesagemOpcional");
        Integer qtPaletes = (Integer) parameters.get("qtPaletes");
        String tpOrigemDimensoes = (String) parameters.get("tpOrigemDimensoes");
        Boolean blEtiquetaPorVolume = (Boolean) parameters.get("blEtiquetaPorVolume");
        String tpOrigemPeso = (String) parameters.get("tpOrigemPeso");
        Integer nrSeqNoPalete = null;


        Map<String, Object> toReturn = new HashMap<String, Object>();
        //TODO: verificar tempo dessa rotina
        executeValidacaoDimensoesECubagem(idDoctoServico, idVolumeNotaFiscal, blEstacaoAutomatizada, nrDimensao1Cm, nrDimensao2Cm, nrDimensao3Cm, nrCubagemM3);

        /** Busca Volumes ainda NAO Verificados */

        //TODO: otimizar essa roptina
        List listVolumesNaoChecados = findVolumesNaoChecadosPorNrPreCTRC(nrPreConhecimento, idFilialOrigem, isVolumeGMDireto);

        /** Verifica se existe Volumes não Verificados */
        if (listVolumesNaoChecados == null || listVolumesNaoChecados.isEmpty()) {
            if (parameters.get(DT_PREVISTA_ENTREGA) == null) {
                Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
                Map dpeMap = getDpe(conhecimento);
                toReturn.put(DT_PREVISTA_ENTREGA, (YearMonthDay) dpeMap.get(DT_PRAZO_ENTREGA));
                toReturn.put(NR_DIAS_PREV_ENTREGA, (Long) dpeMap.get(NR_PRAZO));

                toReturn.put(NR_DIAS_COLETA, (Long) dpeMap.get(NR_DIAS_COLETA));
                toReturn.put(NR_DIAS_ENTREGA, (Long) dpeMap.get(NR_DIAS_ENTREGA));
                toReturn.put(NR_DIAS_TRANSFERENCIA, (Long) dpeMap.get(NR_DIAS_TRANSFERENCIA));
            }
        } else {
            /* Aplica Regras para Estações NAO Automatizadas */
            if (BooleanUtils.isFalse(blEstacaoAutomatizada) &&
                    (!BigDecimalUtils.hasValue(psAferido) || BigDecimalUtils.ltZero(psAferido))) {
                if (psAferido == null) {
                    /* Valida parametros EDI do cadastro */
                    psAferido = BigDecimal.ZERO;
                }

                if (!BigDecimalUtils.isZero(psAferido) || blPesagemOpcional == null || !blPesagemOpcional) {
                    /* Valida se Peso Aferido foi informado */
                    throw new BusinessException("LMS-04242");
                }
            }

			/* Atualiza o Volume com o Código Digitado */
            toReturn = updateDataById(
                    idDoctoServico,
                    psAferido,
                    idVolumeNotaFiscal,
                    isBalancaManual,
                    liberacaoDocServs,
                    qtVolumes,
                    qtVolumesPalete,
                    qtVolumesVolume,
                    blPaletizacao,
                    qtPaletes,
                    tpVolume,
                    blPesagemPalete,
                    nrConhecimento,
                    codFilialFilialOrigem,
                    codFilialDestino,
                    nrSequencia,
                    routeFinalDepotNumber,
                    idFilialOrigem,
                    isVolumeGMDireto,
                    tpDoctoServico,
                    dsMac,
                    idUsuario,
                    idFilial,
                    nrDimensao1Cm,
                    nrDimensao2Cm,
                    nrDimensao3Cm,
                    nrCubagemM3,
                    tpOrigemDimensoes,
                    tpOrigemPeso,
                    blEtiquetaPorVolume
            );

            //duplicado pois será utilizado no fechaEmiteCTRC
            if (parameters.get(DT_PREVISTA_ENTREGA) == null) {
                Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
                Map dpeMap = getDpe(conhecimento);
                toReturn.put(DT_PREVISTA_ENTREGA, (YearMonthDay) dpeMap.get(DT_PRAZO_ENTREGA));
                parameters.put(DT_PREVISTA_ENTREGA, (YearMonthDay) dpeMap.get(DT_PRAZO_ENTREGA));
                toReturn.put(NR_DIAS_PREV_ENTREGA, (Long) dpeMap.get(NR_PRAZO));
                parameters.put(NR_DIAS_PREV_ENTREGA, (Long) dpeMap.get(NR_PRAZO));

                toReturn.put(NR_DIAS_COLETA, (Long) dpeMap.get(NR_DIAS_COLETA));
                toReturn.put(NR_DIAS_ENTREGA, (Long) dpeMap.get(NR_DIAS_ENTREGA));
                toReturn.put(NR_DIAS_TRANSFERENCIA, (Long) dpeMap.get(NR_DIAS_TRANSFERENCIA));
                // LMS-8779
                doctoServicoPPEPadraoService.updateDoctoServicoPPEPadrao(idDoctoServico, (Long) dpeMap.get(NR_DIAS_COLETA), (Long) dpeMap.get(NR_DIAS_ENTREGA), (Long) dpeMap.get(NR_DIAS_TRANSFERENCIA));

            }

            //TODO: Otimizar essa rotina
            Map volumesUD = findVolumesChecadosPorIdCTRC(idDoctoServico, idFilialOrigem, isVolumeGMDireto);

            Long qtVolumesTotal = null;
            Long qtVolumesUD = null;
            if (volumesUD != null && volumesUD.size() > 0) {
                qtVolumesTotal = (Long) volumesUD.get(QT_VOLUMES_TOTAL);
                qtVolumesUD = volumesUD.get(QT_VOLUMES) != null ? (Long) volumesUD.get(QT_VOLUMES) : 0;
                if (qtVolumesTotal.compareTo(qtVolumesUD) < 0) {
                    throw new BusinessException("LMS-04295");
                }
            }

			/* Atualiza o Status do Monitoramento */
            monitoramentoDescargaService.updateMonitoramentoDescargaAposPesagemVolume(idVolumeNotaFiscal, blEstacaoAutomatizada, isVolumeGMDireto);

			/* Aplica Regras para Estações NAO Automatizadas */
            /* Calcula Frete do Conhecimento na Pesagem do Ultimo Volume */
            if (BooleanUtils.isFalse(blEstacaoAutomatizada) &&
                    listVolumesNaoChecados.size() == 1 && listVolumesNaoChecados.get(0).equals(idVolumeNotaFiscal)) {
                Map volumesUM = findVolumesUMchecadosPorIdCTRC(idDoctoServico, idFilialOrigem, isVolumeGMDireto);
                if (volumesUM != null) {
                    executeFechaEmiteCTRCEValidacaoParaBloqueioValores(parameters, idDoctoServico, volumesUM);
                }
            }
        }

        if (booleanValido(blPaletizacao) &&
                booleanValido(blPesagemPalete) &&
                booleanValido(blEtiquetaPorVolume) &&
                qtVolumesPalete > 0) {
            List<VolumeNotaFiscal> listaVolume = new ArrayList<VolumeNotaFiscal>();
            nrSeqNoPalete = 0;
            for (int i = 0; i < qtVolumesPalete; i++) {
                VolumeNotaFiscal volume = new VolumeNotaFiscal();
                VolumeNotaFiscal tmp = this.findVolumeById(idVolumeNotaFiscal);
                volume.setNotaFiscalConhecimento(tmp.getNotaFiscalConhecimento());
                volume.setNrSequencia(nrSequencia);

                Conhecimento docto = conhecimentoService.findByIdJoinFiliais(idDoctoServico);
                Conhecimento ctr = conhecimentoService.findById(idDoctoServico);

                Integer emitidas = ctr.getQtEtiquetasEmitidas();
                Short rota = docto.getRotaColetaEntregaByIdRotaColetaEntregaReal() == null ? 0 : docto.getRotaColetaEntregaByIdRotaColetaEntregaReal().getNrRota();
                emitidas++;
                volume.setNrSequenciaPalete(emitidas);

                volume.setNrVolumeEmbarque(buildLabelBarCode(nrConhecimento,
                        docto.getFilialByIdFilialOrigem().getCodFilial(),
                        docto.getFilialByIdFilialDestino().getCodFilial(),
                        volume.getNrSequencia(),
                        docto.getQtVolumes(),
                        volume.getQtVolumes(),
                        rota, volume.getNrSequenciaPalete(), tpDoctoServico, blPesagemPalete, blEtiquetaPorVolume));
                volume.setPsAferido(BigDecimal.ZERO);

                final MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.findMonitoramentoDescargaByVolumeNotaFiscal(idVolumeNotaFiscal);
                volume.setMonitoramentoDescarga(monitoramentoDescarga);
                volume.setTpVolume("D");
                volume.setNrVolumeColeta((String) parameters.get(NR_VOLUME_COLETA));
                volume.setQtVolumes(1);
                volume.setNrConhecimento(nrConhecimento);

                //LMS-3515
                nrSeqNoPalete++;
                volume.setNrSeqNoPalete(nrSeqNoPalete);

                ctr.setQtEtiquetasEmitidas(emitidas);
                conhecimentoService.store(ctr);

                this.store(volume);
                listaVolume.add(volume);
            }
            if (!listaVolume.isEmpty()) {
                toReturn.put("listaVolumePalete", listaVolume);
            }
        }
        return toReturn;
    }

    private void executeFechaEmiteCTRCEValidacaoParaBloqueioValores(Map<String, Object> parameters, Long idDoctoServico, Map volumesUM) {
        Long qtVolumesTotal;
        qtVolumesTotal = (Long) volumesUM.get(QT_VOLUMES_TOTAL);
        Long qtVolumesUM = volumesUM.get(QT_VOLUMES) != null ? (Long) volumesUM.get(QT_VOLUMES) : 0;
        if (volumesUM.size() > 0 && qtVolumesTotal.compareTo(qtVolumesUM) <= 0) {
            /* Calcula o Frete do Conhecimento */
            calcularFreteService.fechaEmiteCTRC(parameters);

                /* Executa validações de valores do documento. */
            doctoServicoService.executeValidacoesParaBloqueioValores(idDoctoServico);
        }
    }

    private boolean booleanValido(Boolean blPaletizacao) {
        return blPaletizacao != null && blPaletizacao;
    }

    private void executeValidacaoDimensoesECubagem(Long idDoctoServico, Long idVolumeNotaFiscal, Boolean blEstacaoAutomatizada, Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, Double nrCubagemM3) {
        if (nrCubagemM3 == null) {
            if ((nrDimensao1Cm == null && (nrDimensao2Cm != null || nrDimensao3Cm != null))
                    || (nrDimensao2Cm == null && (nrDimensao1Cm != null || nrDimensao3Cm != null))
                    || (nrDimensao3Cm == null && (nrDimensao1Cm != null || nrDimensao2Cm != null))
                    ) {
                throw new BusinessException("LMS-01101");
            }

            String obrigaDimensoesRpp = (String) conteudoParametroFilialService.findConteudoByNomeParametro(SessionUtils.getFilialSessao().getIdFilial()
                    , "OBRIGA_DIMENSOES_RPP", false);
            Boolean isDimensaoECubagemObrigatorios = findObrigatoriedadeDadosDimensaoECubagemByNrVolume(idVolumeNotaFiscal);

            DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
            Cliente clienteBaseCalc = doctoServico.getClienteByIdClienteBaseCalculo();

            String tpCliente = clienteBaseCalc == null ? null : clienteBaseCalc.getTpCliente().getValue();

            if ("S".equals(obrigaDimensoesRpp)) {
                if (!blEstacaoAutomatizada
                        && ("S".equals(tpCliente) || "F".equals(tpCliente))
                        && isDimensaoECubagemObrigatorios && (nrDimensao1Cm == null || nrDimensao2Cm == null || nrDimensao3Cm == null)) {
                    throw new BusinessException("LMS-04061");
                }

                if (!blEstacaoAutomatizada && (nrDimensao1Cm == null || nrDimensao2Cm == null || nrDimensao3Cm == null)
                        && ("E".equals(tpCliente) || "P".equals(tpCliente))) {
                    throw new BusinessException("LMS-04061");
                }
            }
        } else {
            if (nrDimensao1Cm != null || nrDimensao2Cm != null || nrDimensao3Cm != null) {
                throw new BusinessException("LMS-04409");
            }
        }
        /*LMS-6185*/
        verifyLimCub(nrDimensao1Cm, nrDimensao2Cm, nrDimensao3Cm, nrCubagemM3);
    }

    /*LMS-6185*/
    private void verifyLimCub(Integer nrDimensao1Cm, Integer nrDimensao2Cm,
                              Integer nrDimensao3Cm, Double nrCubagemM3) {
        BigDecimal nrCubagemM3BigDecimal;

        if (nrCubagemM3 == null) {
            TypedFlatMap dimensoes = new TypedFlatMap();
            dimensoes.put(DIMENSAO_1, nrDimensao1Cm);
            dimensoes.put(DIMENSAO_2, nrDimensao2Cm);
            dimensoes.put(DIMENSAO_3, nrDimensao3Cm);
            nrCubagemM3BigDecimal = calculaCubagem(dimensoes);
        } else {
            String doubleString = String.valueOf(nrCubagemM3);
            nrCubagemM3BigDecimal = new BigDecimal(doubleString);
        }

		/* LMS-6185 */
        BigDecimal limiteMinCubagemFilial = (BigDecimal) configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), "LIM_MIN_CUB");
        BigDecimal limiteMinCubagemGeral = (BigDecimal) configuracoesFacade.getValorParametro("LIM_MIN_CUB");

        BigDecimal limiteMaxCubagemFilial = (BigDecimal) configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), "LIM_MAX_CUB");
        BigDecimal limiteMaxCubagemGeral = (BigDecimal) configuracoesFacade.getValorParametro("LIM_MAX_CUB");

		/* menor */
        if (BigDecimalUtils.hasValue(limiteMinCubagemFilial)
                && CompareUtils.lt(nrCubagemM3BigDecimal, limiteMinCubagemFilial)) {
            throw new BusinessException("LMS-04500");
        } else if (BigDecimalUtils.hasValue(limiteMinCubagemGeral)
                && CompareUtils.lt(nrCubagemM3BigDecimal, limiteMinCubagemGeral)) {
            throw new BusinessException("LMS-04500");
        }

		/* maior */
        if (BigDecimalUtils.hasValue(limiteMaxCubagemFilial)
                && CompareUtils.gt(nrCubagemM3BigDecimal, limiteMaxCubagemFilial)) {
            throw new BusinessException("LMS-04500");
        } else if (BigDecimalUtils.hasValue(limiteMaxCubagemGeral)
                && CompareUtils.gt(nrCubagemM3BigDecimal, limiteMaxCubagemGeral)) {
            throw new BusinessException("LMS-04500");
        }
    }

    public BigDecimal calculaCubagem(TypedFlatMap dimensoes) {
        BigDecimal dimensao1 = dimensoes.getInteger(DIMENSAO_1) != null ? new BigDecimal(dimensoes.getInteger(DIMENSAO_1).intValue()) : null;
        BigDecimal dimensao2 = dimensoes.getInteger(DIMENSAO_2) != null ? new BigDecimal(dimensoes.getInteger(DIMENSAO_2).intValue()) : null;
        BigDecimal dimensao3 = dimensoes.getInteger(DIMENSAO_3) != null ? new BigDecimal(dimensoes.getInteger(DIMENSAO_3).intValue()) : null;

        BigDecimal result = BigDecimal.ZERO;
        result.setScale(FILL_FIELD_WITH_4_ZEROS, RoundingMode.HALF_UP);

        if ((dimensao1 != null && dimensao1.compareTo(BigDecimal.ZERO) > 0)
                && (dimensao2 != null && dimensao2.compareTo(BigDecimal.ZERO) > 0)
                && (dimensao3 != null && dimensao3.compareTo(BigDecimal.ZERO) > 0)) {

            result = result.add(dimensao1);
            result = (result.multiply(dimensao2)).multiply(dimensao3);
            result = result.divide(million);
        }

        return result;
    }

    public List<VolumeNotaFiscal> findVolumeByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
        return getVolumeNotaFiscalDAO().findByDispositivoUnitizacao(idDispositivoUnitizacao);
    }

    public List<VolumeNotaFiscal> findVolumesNaoUnitizadosControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findVolumesNaoUnitizadosControleCargaAndDoctoServico(idControleCarga, idDoctoServico);
    }

    public Integer countVolumesCarregadosNaoUnitizados(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().countVolumesCarregadosNaoUnitizados(idControleCarga, idDoctoServico);
    }

    public Integer countVolumesCarregadosTotal(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().countVolumesCarregadosTotal(idControleCarga, idDoctoServico);
    }

    public Integer countVolumesDescarregadosNaoUnitizados(Long idControleCarga, Long idDoctoServico, boolean isDescargaEntrega) {
        return isDescargaEntrega
                ? getVolumeNotaFiscalDAO().countVolumesDescarregadosEntregaNaoUnitizados(idControleCarga, idDoctoServico)
                : getVolumeNotaFiscalDAO().countVolumesDescarregadosViagemNaoUnitizados(idControleCarga, idDoctoServico);
    }

    public Integer countVolumesDescarregadosTotal(Long idControleCarga, Long idDoctoServico, boolean isDescargaEntrega) {
        return isDescargaEntrega
                ? getVolumeNotaFiscalDAO().countVolumesDescarregadosEntregaTotal(idControleCarga, idDoctoServico)
                : getVolumeNotaFiscalDAO().countVolumesDescarregadosViagemTotal(idControleCarga, idDoctoServico);
    }

    public Integer findRowCountVolumesCarregamentoDescarga(Long idControleCarga, Long idDoctoServico, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao) {
        return getVolumeNotaFiscalDAO().countVolumesCarregamentoDescarga(idControleCarga, idDoctoServico, tpOperacao, dispositivoUnitizacao);
    }
    public Set<QtdVolumesConhecimentoDto> findQtdVolumesPorIdsConhecimento(Long idControleCarga, Set<Long> idsDoctoServico, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao, boolean isConhecimento) {
        return getVolumeNotaFiscalDAO().findQtdVolumesPorIdsConhecimento(idControleCarga, idsDoctoServico, tpOperacao, dispositivoUnitizacao, isConhecimento);
    }

    public Integer findRowCountVolumesCarregamentoDescargaDispositivo(Long idControleCarga, String tpOperacao, DispositivoUnitizacao dispositivoUnitizacao) {
        return getVolumeNotaFiscalDAO().countVolumesCarregamentoDescargaDispositivo(idControleCarga, tpOperacao, dispositivoUnitizacao);
    }

    public Integer findRowCountVolumesCarregamentoDescargaConhecimento(Long idControleCarga, Long idDoctoServico, String tpOperacao) {
        return getVolumeNotaFiscalDAO().countVolumesCarregamentoDescargaConhecimento(idControleCarga, idDoctoServico, tpOperacao);
    }

    public Boolean validateIsVolumePaletizado(Long idVolume) {
        VolumeNotaFiscal volumeNotaFiscal = this.findById(idVolume);
        return validateIsVolumePaletizado(volumeNotaFiscal);
    }

    public Boolean validateIsVolumePaletizado(VolumeNotaFiscal volumeNotaFiscal) {
        if ("D".equals(volumeNotaFiscal.getTpVolume()) || volumeNotaFiscal.getQtVolumes().compareTo(1) > 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }


    /**
     * Retorna um {@link VolumeNotaFiscal} através de um barCode [VolumeNotaFiscal.getNrVolumeEmbarque()]
     *
     * @param {@link String}
     * @return {@link VolumeNotaFiscal}
     */
    public VolumeNotaFiscal findVolumeByBarCodeUniqueResult(String barcode) {
        Boolean isEtiquetaMWW = this.validateIsEtiquetaMWW(barcode);

        List<VolumeNotaFiscal> volumeNotaFiscal = null;
        if (isEtiquetaMWW) {
            volumeNotaFiscal = getVolumeNotaFiscalDAO().findByCodigoBarrasMWW(barcode, obterDataMinimaMWW());
        } else {
            volumeNotaFiscal = getVolumeNotaFiscalDAO().findByCodigoBarras(barcode);
        }
        if (volumeNotaFiscal == null || volumeNotaFiscal.isEmpty()) {
            //LMS-45017 - Volume não foi encontrado.
            throw new BusinessException("LMS-45017");
        }

        if (volumeNotaFiscal.size() > 1) {
            throw new NonUniqueResultException(volumeNotaFiscal.size());
        }

        return volumeNotaFiscal.get(0);
    }

    /**
     * Consulta Otimizada para busca de Volumes pelo Código de Barras
     *
     * @param codigoBarras
     * @param alias
     * @return
     * @author André Valadas
     */
    public VolumeNotaFiscal findVolumeByBarCodeUniqueResult(final String codigoBarras, final Map<String, String> alias) {
        Boolean isEtiquetaMWW = this.validateIsEtiquetaMWW(codigoBarras);

        List<VolumeNotaFiscal> volumeNotaFiscal = null;
        if (isEtiquetaMWW) {
            volumeNotaFiscal = getVolumeNotaFiscalDAO().findByCodigoBarrasMWW(codigoBarras, alias, obterDataMinimaMWW());
        } else {
            volumeNotaFiscal = getVolumeNotaFiscalDAO().findByCodigoBarras(codigoBarras, alias);
        }

        if (volumeNotaFiscal == null || volumeNotaFiscal.isEmpty()) {
            throw new BusinessException("LMS-45017");//Volume não foi encontrado.
        }
        if (volumeNotaFiscal.size() > 1) {
            throw new NonUniqueResultException(volumeNotaFiscal.size());
        }

        return volumeNotaFiscal.get(0);
    }

    /**
     * Retorna um {@link VolumeNotaFiscal} através de um barCode [VolumeNotaFiscal.getNrVolumeEmbarque()]
     *
     * @param {@link String}
     * @return {@link VolumeNotaFiscal}
     */
    public VolumeNotaFiscal findVolumeById(Long idVolume) {

        VolumeNotaFiscal volumeNotaFiscal = getVolumeNotaFiscalDAO().findVolumeById(idVolume);

        if (volumeNotaFiscal == null) {
            //LMS-45017 - Volume não foi encontrado.
            throw new BusinessException("LMS-45017");
        }

        return volumeNotaFiscal;
    }

    private void buildMapFindDadosVolume(PaginatedQuery paginatedQuery, List result, boolean isVolumeGMDireto, boolean isDanfeSimplificada) {
        if (result != null && !result.isEmpty()) {
            executeBuildDadosVolume(paginatedQuery, result, isVolumeGMDireto, isDanfeSimplificada);
        }
    }

    private void executeBuildDadosVolume(PaginatedQuery paginatedQuery, List result, boolean isVolumeGMDireto, boolean isDanfeSimplificada) {
        Impressora impressora = impressoraService.findImpressoraUsuario((Long) paginatedQuery.getCriteria().get(ID_FILIAL_USUARIO), (String) paginatedQuery.getCriteria().get("macAdress"), (String) paginatedQuery.getCriteria().get("tpImpressora"));
        Map<Long, Object[]> conhecimentoCache = new HashMap<Long, Object[]>();
        for (Object volumeNotaFiscal : result) {
            Map<String, Object> mapa = (Map<String, Object>) volumeNotaFiscal;
            Conhecimento conhecimento = conhecimentoService.findById((Long) mapa.get(ID_CONHECIMENTO));

            carregarMapaDados(mapa);

            Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
            String parametroEtiquetaNova = buscarParametroNovaEtiqueta(idFilial);
            if ("S".equals(parametroEtiquetaNova)){
                Long idFilialDestino = (Long)mapa.get("idFilialDestino");
            	Long idServico = (Long)mapa.get("idServico");
            	Long idMunicipio = (Long)mapa.get("idMunicipioDestinatarioLocalEntrega");
            	mapa.put("blEtiquetaQRCode", parametroEtiquetaNova);
            	mapa.putAll(findAtendimentoByMunicipioFilialServicoVigente(idMunicipio, idFilialDestino, idServico));
            }


            tratarDadosCache(conhecimentoCache, mapa, conhecimento);

            if(isDanfeSimplificada) {
                mapa.put(DANFE_SIMPLIFICADA, isDanfeSimplificada);
            }

            if (impressora != null) {
                mapa.put(NR_IP, impressora.getNrIp() != null ? FormatUtils.convertNumberToIp(BigInteger.valueOf(impressora.getNrIp())) : null);
                mapa.put(NR_PORT, impressora.getNrPort());
            }
            String tpVolume = (String) mapa.get(TP_VOLUME);
            BigDecimal psAferido = (BigDecimal) mapa.get(PS_AFERIDO);
            if (tpVolume != null && tpVolume.equals(ConstantesExpedicao.TP_VOLUME_MESTRE) && psAferido != null) {
                mapa.put(LISTA_VOLUMES_D, this.findVolumesPaleteTipoDPeloNrVolumeColeta(mapa.get(NR_VOLUME_COLETA).toString()));
            }
            Boolean isDimensoesCubagemOpcional = this.validateDimensaoOpcionalByFatorDensidade((Long) mapa.get(ID_VOLUME_NOTA_FISCAL));
            mapa.put(IS_DIMENSOES_CUBAGEM_OPCIONAL, isDimensoesCubagemOpcional);

            mapa.put(IS_VOLUME_GM_DIRETO, isVolumeGMDireto ? "S" : "N");
        }
    }

    private void tratarDadosCache(Map<Long, Object[]> conhecimentoCache, Map<String, Object> mapa, Conhecimento conhecimento) {
        if (conhecimentoCache.containsKey((Long) mapa.get(ID_DOCTO_SERVICO))) {
            Object[] cache = conhecimentoCache.get((Long) mapa.get(ID_DOCTO_SERVICO));
            mapa.put(DT_PREVISTA_ENTREGA, (YearMonthDay) cache[INT_CACHE_POS_0]);
            mapa.put(SG_RED_FILIAL_ORIGEM, (String) cache[INT_CACHE_POS_3]);
            mapa.put(SG_RED_FILIAL_DESTINO, (String) cache[INT_CACHE_POS_4]);
            mapa.put(NR_DIAS_PREV_ENTREGA, (Long) cache[INT_CACHE_POS_1]);
            mapa.put(ROUTE, (String) cache[INT_CACHE_POS_2]);
        } else {
            String findRota = this.findRota((Long) mapa.get(ID_FILIAL_ORIGEM), (Long) mapa.get(ID_FILIAL_DESTINO), (Long) mapa.get(ID_SERVICO), (Long) mapa.get("idMunicipioDestinatarioLocalEntrega"));
            String findSgFilialLegado = filialService.findSgFilialLegadoByIdFilial((Long) mapa.get(ID_FILIAL_ORIGEM));
            String findSgFilialDestinoLegado = filialService.findSgFilialLegadoByIdFilial((Long) mapa.get(ID_FILIAL_DESTINO));
            mapa.put(SG_RED_FILIAL_ORIGEM, findSgFilialLegado);
            mapa.put(SG_RED_FILIAL_DESTINO, findSgFilialDestinoLegado);
            mapa.put(ROUTE, findRota);

            Long nrPrazo;
            YearMonthDay dtPrazoEntrega;

            if (conhecimento.getDhEmissao() != null ||
                    (conhecimento.getDtPrevEntrega() != null && conhecimento.getNrDiasPrevEntrega() != null)) {
                Map<String, Object> dpeMap = this.getDpe(conhecimento);
                nrPrazo = (Long) dpeMap.get(NR_PRAZO);
                dtPrazoEntrega = (YearMonthDay) dpeMap.get(DT_PRAZO_ENTREGA);

                // Converte prazos em dias
                mapa.put(NR_DIAS_COLETA, (Long) dpeMap.get(NR_DIAS_COLETA));
                mapa.put(NR_DIAS_ENTREGA, (Long) dpeMap.get(NR_DIAS_ENTREGA));
                mapa.put(NR_DIAS_TRANSFERENCIA, (Long) dpeMap.get(NR_DIAS_TRANSFERENCIA));

                mapa.put(DT_PREVISTA_ENTREGA, dtPrazoEntrega);
                mapa.put(NR_DIAS_PREV_ENTREGA, nrPrazo);
            } else {
                nrPrazo = null;
                dtPrazoEntrega = null;
            }

            conhecimentoCache.put((Long) mapa.get(ID_DOCTO_SERVICO), new Object[]{dtPrazoEntrega, nrPrazo, findRota,
                    findSgFilialLegado, findSgFilialDestinoLegado});
        }
    }

    private void carregarMapaDados(Map<String, Object> mapa) {
        if (mapa.get(TP_CTRC_PARCERIA) == null && (Cliente) mapa.get("clieConsignatario") != null) {
            // envio para consignatario
            mapa.put(NM_DESTINATARIO, mapa.get("nmDestinatarioConsignatario"));
            mapa.put(TIPO_LOGRADOURO_DESTINATARIO, mapa.get("tipoLogradouroDestinatarioConsignatario"));
            mapa.put(ENDERECO_DESTINATARIO, mapa.get("enderecoDestinatarioConsignatario"));
            mapa.put(NR_ENDERECO_DESTINATARIO, mapa.get("nrEnderecoDestinatarioConsignatario"));
            mapa.put(COMPLEMENTO_ENDERECO_DESTINATARIO, mapa.get("complementoEnderecoDestinatarioConsignatario"));
            mapa.put(MUNICIPIO_DESTINATARIO, mapa.get("municipioDestinatarioConsignatario"));
            mapa.put(UNID_FEDERATIVA_DESTINATARIO, mapa.get("unidFederativaDestinatarioConsignatario"));
            mapa.put(PAIS_DESTINATARIO, mapa.get("paisDestinatarioConsignatario"));
            mapa.put(CEP_DESTINATARIO, mapa.get("cepDestinatarioConsignatario"));
            mapa.put(BAIRRO_DESTINATARIO, mapa.get("bairroDestinatarioConsignatario"));
        } else if (mapa.get(DS_LOCAL_ENTREGA) != null || mapa.get("dsEnderecoEntrega") != null) { // Local de Entrega
            mapa.put(NM_DESTINATARIO, mapa.get("nmDestinatarioLocalEntrega"));
            mapa.put(TIPO_LOGRADOURO_DESTINATARIO, mapa.get("tipoLogradouroDestinatarioLocalEntrega"));
            mapa.put(ENDERECO_DESTINATARIO, mapa.get("enderecoDestinatarioLocalEntrega"));
            mapa.put(NR_ENDERECO_DESTINATARIO, mapa.get("nrEnderecoDestinatarioLocalEntrega"));
            mapa.put(COMPLEMENTO_ENDERECO_DESTINATARIO, mapa.get("complementoEnderecoDestinatarioLocalEntrega"));
            mapa.put(MUNICIPIO_DESTINATARIO, mapa.get("municipioDestinatarioLocalEntrega"));
            mapa.put(UNID_FEDERATIVA_DESTINATARIO, mapa.get("unidFederativaDestinatarioLocalEntrega"));
            mapa.put(PAIS_DESTINATARIO, mapa.get("paisDestinatarioLocalEntrega"));
            mapa.put(CEP_DESTINATARIO, mapa.get("cepDestinatarioLocalEntrega"));
            mapa.put(BAIRRO_DESTINATARIO, mapa.get("bairroDestinatarioLocalEntrega"));
        } else { // destinatário
            mapa.put(NM_DESTINATARIO, mapa.get("nmDestinatarioDestinatario"));
            mapa.put(TIPO_LOGRADOURO_DESTINATARIO, mapa.get("tipoLogradouroDestinatarioDestinatario"));
            mapa.put(ENDERECO_DESTINATARIO, mapa.get("enderecoDestinatarioDestinatario"));
            mapa.put(NR_ENDERECO_DESTINATARIO, mapa.get("nrEnderecoDestinatarioDestinatario"));
            mapa.put(COMPLEMENTO_ENDERECO_DESTINATARIO, mapa.get("complementoEnderecoDestinatarioDestinatario"));
            mapa.put(MUNICIPIO_DESTINATARIO, mapa.get("municipioDestinatarioDestinatario"));
            mapa.put(UNID_FEDERATIVA_DESTINATARIO, mapa.get("unidFederativaDestinatarioDestinatario"));
            mapa.put(PAIS_DESTINATARIO, mapa.get("paisDestinatarioDestinatario"));
            mapa.put(CEP_DESTINATARIO, mapa.get("cepDestinatarioDestinatario"));
            mapa.put(BAIRRO_DESTINATARIO, mapa.get("bairroDestinatarioDestinatario"));
        }

        mapa.remove("nmDestinatarioDestinatario");
        mapa.remove("tipoLogradouroDestinatarioDestinatario");
        mapa.remove("enderecoDestinatarioDestinatario");
        mapa.remove("nrEnderecoDestinatarioDestinatario");
        mapa.remove("complementoEnderecoDestinatarioDestinatario");
        mapa.remove("municipioDestinatarioDestinatario");
        mapa.remove("unidFederativaDestinatarioDestinatario");
        mapa.remove("paisDestinatarioDestinatario");
        mapa.remove("cepDestinatarioDestinatario");
        mapa.remove("bairroDestinatarioDestinatario");

        mapa.remove("nmDestinatarioLocalEntrega");
        mapa.remove("tipoLogradouroDestinatarioLocalEntrega");
        mapa.remove("enderecoDestinatarioLocalEntrega");
        mapa.remove("nrEnderecoDestinatarioLocalEntrega");
        mapa.remove("complementoEnderecoDestinatarioLocalEntrega");
        mapa.remove("municipioDestinatarioLocalEntrega");
        mapa.remove("unidFederativaDestinatarioLocalEntrega");
        mapa.remove("paisDestinatarioLocalEntrega");
        mapa.remove("cepDestinatarioLocalEntrega");
        mapa.remove("bairroDestinatarioLocalEntrega");

        mapa.remove("nmDestinatarioConsignatario");
        mapa.remove("tipoLogradouroDestinatarioConsignatario");
        mapa.remove("enderecoDestinatarioConsignatario");
        mapa.remove("nrEnderecoDestinatarioConsignatario");
        mapa.remove("complementoEnderecoDestinatarioConsignatario");
        mapa.remove("municipioDestinatarioConsignatario");
        mapa.remove("unidFederativaDestinatarioConsignatario");
        mapa.remove("paisDestinatarioConsignatario");
        mapa.remove("cepDestinatarioConsignatario");
        mapa.remove("bairroDestinatarioConsignatario");

        mapa.remove("conhecimento");
        mapa.remove("clieConsignatario");

        //suspeito - esta data tem que ser pega do servidor do robo no caso de filial SOM
        mapa.put(DH_EMISSAO, (DateTime) mapa.get(DH_EMISSAO) == null ? JTDateTimeUtils.getDataHoraAtual() : (DateTime) mapa.get(DH_EMISSAO));
    }

    public List findVolumesPaleteTipoDPeloNrVolumeColeta(String nrColeta) {
        return getVolumeNotaFiscalDAO().findVolumesPaleteTipoDPeloNrVolumeColeta(nrColeta);
    }

    public String findRota(Long idFilialOrigem, Long idFilialDestino, Long idServico, Long idMunicipio) {
        FluxoFilial fluxoFilial = fluxoFilialService.findFluxoFilial(idFilialOrigem, idFilialDestino, JTDateTimeUtils.getDataAtual(), idServico);
        if(fluxoFilial == null){
            fluxoFilial = fluxoFilialService.findFluxoFilialPadraoMCD(idFilialOrigem, idFilialDestino, JTDateTimeUtils.getDataAtual(), idServico, idMunicipio);
        }
        return fluxoFilial != null ? fluxoFilial.getDsFluxoFilial() : null;
    }

    public List<VolumeNotaFiscal> findByIdControleCarga(Long idControleCarga, String tpManifesto, Short cdLocalizacaoMercadoria) {
        List<Short> lstCdLocMerc = new ArrayList<Short>();
        lstCdLocMerc.add(cdLocalizacaoMercadoria);
        return getVolumeNotaFiscalDAO().findByIdControleCargaTpManifestoAndCdLocalizacao(idControleCarga, tpManifesto, lstCdLocMerc);
    }

    public List findVolumeByIdControleCarga(Long idControleCarga) {
        List lista = getVolumeNotaFiscalDAO().findVolumeByIdControleCarga(idControleCarga);
        return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
    }

    public List<VolumeNotaFiscal> findVolumesNaoUnitizadosControleCarga(Long idControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesNaoUnitizadosControleCarga(idControleCarga);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosControleCarga(Long idControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesUnitizadosControleCarga(idControleCarga);
    }

    public List<VolumeNotaFiscal> findVolumesDispositivoUnitizacao(Long idControleCarga, Long idDispositivoUnitizacao) {
        return getVolumeNotaFiscalDAO().findVolumesDispositivoUnitizacao(idControleCarga, idDispositivoUnitizacao);
    }

    public List<VolumeNotaFiscal> findVolumesDescargaControleCarga(Long idControleCarga, Long idDispositivoUnitizacao, Boolean isViagem) {
        return getVolumeNotaFiscalDAO().findVolumesDescargaControleCarga(idControleCarga, idDispositivoUnitizacao, isViagem);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescargaControleCarga(Long idControleCarga, Boolean isViagem) {
        return getVolumeNotaFiscalDAO().findVolumesUnitizadosDescargaControleCarga(idControleCarga, isViagem);
    }

    public List<VolumeNotaFiscal> findByIdControleCarga(Long idControleCarga, String tpManifesto, List<Short> lstCdLocMerc) {
        return getVolumeNotaFiscalDAO().findByIdControleCargaTpManifestoAndCdLocalizacao(idControleCarga, tpManifesto, lstCdLocMerc);
    }

    public List<VolumeNotaFiscal> findByIdControleCargaDoctoServico(Long idControleCarga, Long idDoctoServico, String tpManifesto, Short cdLocalizacaoMercadoria) {
        return getVolumeNotaFiscalDAO().findByControleCargaTpManifestoCdLocalizacaoDoctoServico(idControleCarga, idDoctoServico, tpManifesto, cdLocalizacaoMercadoria);
    }

    public List<VolumeNotaFiscal> findVolumesDescarregados(Long idControleCarga, Long idDoctoServico, boolean isDescargaEntrega) {
        return isDescargaEntrega
                ? findVolumesDescarregadosEntrega(idControleCarga, idDoctoServico)
                : findVolumesDescarregadosViagem(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescarregados(Long idControleCarga, boolean isDescargaEntrega) {
        return isDescargaEntrega
                ? findVolumesUnitizadosDescarregadosEntrega(idControleCarga)
                : findVolumesUnitizadosDescarregadosViagem(idControleCarga);
    }

    public List<VolumeNotaFiscal> findTodosVolumesDescarregados(Long idControleCarga, Long idDoctoServico, boolean isDescargaEntrega) {
        return isDescargaEntrega
                ? findVolumesTotalDescarregadosEntrega(idControleCarga, idDoctoServico)
                : findVolumesTotalDescarregadosViagem(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findVolumesDescarregadosEntrega(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescarregadosEntrega(Long idControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesUnitizadosDescarregadosEntrega(idControleCarga);
    }

    public List<VolumeNotaFiscal> findVolumesTotalDescarregadosEntrega(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findVolumesTotalDescarregadosEntrega(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findVolumesDescarregadosViagem(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesUnitizadosDescarregadosViagem(Long idControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesUnitizadosDescarregadosViagem(idControleCarga);
    }

    public List<VolumeNotaFiscal> findVolumesTotalDescarregadosViagem(Long idControleCarga, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().findVolumesTotalDescarregadosViagem(idControleCarga, idDoctoServico);
    }

    public List<VolumeNotaFiscal> findVolumesEmDescargaByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial, DomainValue tpControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesEmDescargaByIdDoctoAndIdFilial(idDoctoServico, idFilial, tpControleCarga);
    }

    public List<VolumeNotaFiscal> findVolumesByIdDoctoAndIdFilial(Long idDoctoServico, Long idFilial) {
        return getVolumeNotaFiscalDAO().findVolumesByIdDoctoAndIdFilial(idDoctoServico, idFilial);
    }

    public Map<String, Object> getDpe(Conhecimento conhecimento) {

        if (conhecimento.getDtPrevEntrega() != null) {
            Map<String, Object> retorno = new HashMap<String, Object>();
            retorno.put(DT_PRAZO_ENTREGA, conhecimento.getDtPrevEntrega());
            retorno.put(NR_PRAZO, null);
            return retorno;
        }

        Long idPedidoColeta = null;
        if (conhecimento.getPedidoColeta() != null) {
            idPedidoColeta = conhecimento.getPedidoColeta().getIdPedidoColeta();
        }

        Cliente clienteDevedor = conhecimento.getDevedorDocServs().get(0).getCliente();

        return dpeService.executeCalculoDPE(
                conhecimento.getClienteByIdClienteRemetente(),
                conhecimento.getClienteByIdClienteDestinatario(),
                clienteDevedor,
                conhecimento.getClienteByIdClienteConsignatario(),
                conhecimento.getClienteByIdClienteRedespacho(),
                idPedidoColeta,
                conhecimento.getServico().getIdServico(),
                conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio(),
                conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
                conhecimento.getFilialByIdFilialDestino().getIdFilial(),
                conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
                conhecimento.getNrCepColeta(),
                conhecimento.getNrCepEntrega(),
                conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao()
        );

    }

    /**
     * LMS-1786
     * Busca o próximo número de conhecimento e
     * grava nos volumes utilizando a mesma transação
     */
    public void updateNrConhecimentoByPreCT(Conhecimento conhecimento, DateTime dhEmissao) {
        Long idDoctoServico = conhecimento.getIdDoctoServico();

		/* Gerar próximo número */
        conhecimento.setGenerateUniqueNumber(true);
        emitirDocumentoService.generateProximoNumero(conhecimento);

		/* Grava próximo número nos volumes */
        getVolumeNotaFiscalDAO().updateNrConhecimentoByPreCT(conhecimento.getNrConhecimento(), idDoctoServico);

        if (dhEmissao != null) {
            DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
            doctoServico.setDhEmissao(dhEmissao);
            doctoServicoService.store(doctoServico);
        }
    }

    public Long countVolumesDeCTRCsEmCalculoFretePorMonitoramentoDescarga(Long idMonitoramentoDescarga) {
        return getVolumeNotaFiscalDAO().countVolumesDeCTRCsEmCalculoFretePorMonitoramentoDescarga(idMonitoramentoDescarga);
    }

    private Map<String, Object> updateDataById(Long idDoctoServico, BigDecimal psAferido, Long idVolumeNotaFiscal, Boolean isBalancaManual, List<LiberacaoDocServ> liberacaoDocServs,
                                               Integer qtVolumes, Integer qtVolumesPalete, Integer qtVolumesVolume, Boolean blPaletizacao, Integer qtPaletes, String tpVolume, Boolean blPesagemPalete, Long nrConhecimento,
                                               Integer codFilialOrigem, Integer codFilialDestino, Integer nrSequencia, Short routeFinalDepotNumber, Long idFilialOrigem, boolean isVolumeGMDireto, DomainValue tpDoctoServico,
                                               String dsMac, Long idUsuario, Long idFilial, Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, Double nrCubagemM3, String tpOrigemDimensoes, String tpOrigemPeso, Boolean blEtiquetaPorVolume) {
        Map<String, Object> map = new HashMap<String, Object>();

        String nrVolumeEmbarque = null;
        Integer nrSequenciaPalete = null;
        Integer nrSeqNoPalete = null;

        if (booleanValido(blPaletizacao)) {
            if (blPesagemPalete != null && !blPesagemPalete) {
                qtVolumesVolume = Integer.valueOf(1);
                tpVolume = ConstantesExpedicao.TP_VOLUME_UNITARIO;
                Integer nrSequenciaEtiqueta = conhecimentoService.updateQtEtiquetasEmitidasByQtEtiquetas(idDoctoServico, 1);
                nrSequenciaPalete = nrSequenciaEtiqueta + 1;
                nrVolumeEmbarque = buildLabelBarCode(nrConhecimento, codFilialOrigem, codFilialDestino, nrSequencia, qtVolumes, null, routeFinalDepotNumber, nrSequenciaPalete, tpDoctoServico, blPesagemPalete, blEtiquetaPorVolume);
                map.put(NR_SEQUENCIA_PALETE, nrSequenciaPalete);
                map.put(NR_VOLUME_EMBARQUE, nrVolumeEmbarque);
                map.put(TP_VOLUME, tpVolume);
            } else {
                qtVolumesVolume = qtVolumesPalete;
                if (blEtiquetaPorVolume == null || !blEtiquetaPorVolume) {
                    tpVolume = ConstantesExpedicao.TP_VOLUME_UNITARIO;
                    nrVolumeEmbarque = buildLabelBarCode(nrConhecimento, codFilialOrigem, codFilialDestino, nrSequencia, qtPaletes, qtVolumesPalete, routeFinalDepotNumber, nrSequenciaPalete, tpDoctoServico, blPesagemPalete, blEtiquetaPorVolume);
                    map.put(NR_VOLUME_EMBARQUE, nrVolumeEmbarque);
                    //LMS-3515
                    nrSeqNoPalete = 1;
                }
                map.put(TP_VOLUME, tpVolume);
            }
        } else {
            nrVolumeEmbarque = buildLabelBarCode(nrConhecimento, codFilialOrigem, codFilialDestino, nrSequencia, qtVolumes, null, routeFinalDepotNumber, null, tpDoctoServico, blPesagemPalete, blEtiquetaPorVolume);
            map.put(NR_VOLUME_EMBARQUE, nrVolumeEmbarque);
        }
        map.put("qtVolumesVolume", qtVolumesVolume);

        map.put("tpOrigemDimensoes", tpOrigemDimensoes);
        map.put("tpOrigemPeso", tpOrigemPeso);

        getVolumeNotaFiscalDAO().updateDataById(nrVolumeEmbarque, psAferido, idVolumeNotaFiscal, qtVolumesVolume, tpVolume, nrSequenciaPalete, dsMac, idUsuario, idFilial
                , nrDimensao1Cm, nrDimensao2Cm, nrDimensao3Cm, nrCubagemM3, tpOrigemDimensoes == null ? null : new DomainValue(tpOrigemDimensoes), tpOrigemPeso == null ? null : new DomainValue(tpOrigemPeso), nrSeqNoPalete);

        VolumeNotaFiscal vol = findById(idVolumeNotaFiscal);

        getDao().flush();
        getVolumeNotaFiscalDAO().flush();

		/* CQPRO00027030 - Evento 141 = Impressão da etiqueta : LM = LMS */
        eventoVolumeService.generateEventoVolume(vol, ConstantesSim.EVENTO_IMPRESSAO_ETIQUETA, ConstantesSim.TP_SCAN_FISICO);

        if (liberacaoDocServs != null) {
            DoctoServico ds = new DoctoServico();
            ds.setIdDoctoServico(idDoctoServico);
            for (LiberacaoDocServ liberacaoDocServ : liberacaoDocServs) {
                liberacaoDocServ.setDoctoServico(ds);
            }
            liberacaoDocServService.storeAll(liberacaoDocServs);
        }


        return map;
    }

    public void updateVolumeDadosSorter(Long idVolumeNotaFiscal, Integer nrDimensao1Cm, Integer nrDimensao2Cm, Integer nrDimensao3Cm, BigDecimal psAferido, DateTime dhPesagem) {

        UpdateVolumeDadosSorterDto updateVolumeDadosSorterDto = new UpdateVolumeDadosSorterDto();
        updateVolumeDadosSorterDto.setIdVolumeNotaFiscal(idVolumeNotaFiscal);
        updateVolumeDadosSorterDto.setNrDimensao1Cm(nrDimensao1Cm);
        updateVolumeDadosSorterDto.setNrDimensao2Cm(nrDimensao2Cm);
        updateVolumeDadosSorterDto.setNrDimensao3Cm(nrDimensao3Cm);
        updateVolumeDadosSorterDto.setPsAferido(psAferido);
        updateVolumeDadosSorterDto.setDhPesagem(dhPesagem);

        getVolumeNotaFiscalDAO().updateVolumeDadosSorterById(updateVolumeDadosSorterDto);

        getDao().flush();

    }

    public Map findVolumeNotaFiscalNaoAferido(final String nrVolumeEmbarque) {
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalNaoAferido(nrVolumeEmbarque);
    }

    public Map findVolumeNotaFiscalNaoAferidoSorter(final String nrVolumeEmbarque) {
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalNaoAferidoSorter(nrVolumeEmbarque);
    }

    public Map findVolumeBynrVolumeEmbarque(String nrVolumeEmbarque) {
        return getVolumeNotaFiscalDAO().findVolumeBynrVolumeEmbarque(nrVolumeEmbarque);
    }

    public Map<String, Object> findVolumeNotaFiscalSorter(String nrVolumeEmbarque) {
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalSorter(nrVolumeEmbarque);
    }

    /**
     * LMS-2498 Criar contigencia para emissão de documentos de serviço sem necesidade de labeling
     * <p>
     * Gera o codigo de barras ao realizar o fechamento do CTRC pelo EDI
     * Método chamado por CalcularFreteService.fechaEmiteCTRC
     */
    public String buildLabelBarCode(Conhecimento conhecimento, VolumeNotaFiscal vnf) {
        Cliente c = devedorDocServService.findByIdDoctoServico(conhecimento.getIdDoctoServico());

        Short nrRota = null;
        if (conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaReal() != null) {
            nrRota = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaReal().getNrRota();
        }

        return buildLabelBarCode(
                conhecimento.getNrConhecimento(),
                conhecimento.getFilialByIdFilialOrigem().getCodFilial(),
                conhecimento.getFilialByIdFilialDestino().getCodFilial(),
                vnf.getNrSequencia(),
                conhecimento.getQtVolumes(),
                vnf.getQtVolumes(),
                nrRota,
                null,
                conhecimento.getTpDoctoServico(),
                null,
                c.getBlEtiquetaPorVolume()

        );
    }


    public String buildLabelBarCode(Long nrConhecimento, Integer codFilialOrigem, Integer codFilialDestino, Integer nrSequencia, Integer qtVolumes, Integer qtVolumesPalete, Short routeFinalDepotNumber, Integer nrSequenciaPalete, DomainValue tpDoctoServico, Boolean blPesagemPalete, Boolean blEiquetaPorVolume) {
        String labelBarCode = "";

        Long tipoDocumento;

        if (tpDoctoServico != null && "NFT".equals(tpDoctoServico.getValue())) {
            tipoDocumento = 2L;
        } else if ("NTE".equals(tpDoctoServico.getValue())) {
            tipoDocumento = 3L;
        } else {
            if ("CTE".equals(tpDoctoServico.getValue())) {
                tipoDocumento = 1L;
            } else {
                tipoDocumento = 0L;
            }
        }
        // TipoDocumento engessado

        labelBarCode = labelBarCode.concat("02");
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(codFilialOrigem.toString(), FILL_FIELD_WITH_3_ZEROS));
        labelBarCode = labelBarCode.concat(tipoDocumento.toString());
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(nrConhecimento.toString(), FILL_FIELD_WITH_9_ZEROS));
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(codFilialDestino.toString(), FILL_FIELD_WITH_3_ZEROS));
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(nrSequencia.toString(), FILL_FIELD_WITH_4_ZEROS));
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(qtVolumes.toString(), FILL_FIELD_WITH_4_ZEROS));
        labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(routeFinalDepotNumber != null ? routeFinalDepotNumber.toString() : null, FILL_FIELD_WITH_3_ZEROS));
        if (booleanValido(blPesagemPalete)) {
            if (blEiquetaPorVolume == null || !blEiquetaPorVolume) {
                labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(qtVolumes != null ? qtVolumes.toString() : "", FILL_FIELD_WITH_4_ZEROS));
            } else {
                labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero(nrSequenciaPalete != null ? nrSequenciaPalete.toString() : "", FILL_FIELD_WITH_4_ZEROS));
            }
        } else {
            labelBarCode = labelBarCode.concat(FormatUtils.fillNumberWithZero("0000", FILL_FIELD_WITH_4_ZEROS));
        }

        return labelBarCode;
    }


    public List<Map> findVolumeNotaFiscalSemDoctoServicoNaFilial(Long idDoctoServico, Long idFilialLogada, List<Long> idVolumeNotaFiscalList,
                                                                 String tpManifesto, String localizacaoVolume) {

        List cdLocalizacaoMercadoriaList = new ArrayList();

        //ENTREGA
        if ("E".equalsIgnoreCase(tpManifesto)) {
            if ("terminal".equalsIgnoreCase(localizacaoVolume)) {
                cdLocalizacaoMercadoriaList.add(24);
                cdLocalizacaoMercadoriaList.add(35);
                cdLocalizacaoMercadoriaList.add(43);
            } else if ("descarga".equalsIgnoreCase(localizacaoVolume)) {
                cdLocalizacaoMercadoriaList.add(33);
                cdLocalizacaoMercadoriaList.add(34);
            }
            //VIAGEM
        } else if ("V".equalsIgnoreCase(tpManifesto)) {
            if ("terminal".equalsIgnoreCase(localizacaoVolume)) {
                cdLocalizacaoMercadoriaList.add(24);
                cdLocalizacaoMercadoriaList.add(28);
            } else if ("descarga".equalsIgnoreCase(localizacaoVolume)) {
                cdLocalizacaoMercadoriaList.add(33);
                cdLocalizacaoMercadoriaList.add(34);
            }
        }

        String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), 
                "BL_BLOQ_DOC_SUB");
        
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalSemDoctoServicoNaFilial(idDoctoServico, idFilialLogada, idVolumeNotaFiscalList, cdLocalizacaoMercadoriaList, bloqFluxoSubcontratacao);

    }

    public List findVolumesNaoChecadosPorNrPreCTRC(Long nrPreConhecimento, Long idFilialOrigem, boolean isVolumeGMDireto) {
        return getVolumeNotaFiscalDAO().findVolumesNaoChecadosPorNrPreCTRC(nrPreConhecimento, idFilialOrigem, isVolumeGMDireto);
    }

    /**
     * @param nrPreConhecimento
     * @param idFilialOrigem
     * @return
     */
    public Boolean verifyVolumesPendentes(final Long nrPreConhecimento, final Long idFilialOrigem) {
        return getVolumeNotaFiscalDAO().verifyVolumesPendentes(nrPreConhecimento, idFilialOrigem);
    }

    public Boolean verifyVolumesPendentesSorter(final Long nrPreConhecimento, final Long idFilialOrigem) {
        return getVolumeNotaFiscalDAO().verifyVolumesPendentesSorter(nrPreConhecimento, idFilialOrigem);
    }

    public List findVolumeNotaFiscalByIdMonitoramentoDescarga(Long id) {
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalByIdMonitoramentoDescarga(id);
    }

    public List findQtVolumesNotaFiscalByIdMonitoramentoDescarga(Long id) {
        return getVolumeNotaFiscalDAO().findQtVolumesNotaFiscalByIdMonitoramentoDescarga(id);
    }

    public List<VolumeNotaFiscal> findVolumeNotaFiscalByMonitoramentoDescarga(Long id, boolean isVolumeGMDireto) {
        return getVolumeNotaFiscalDAO().findVolumeNotaFiscalByMonitoramentoDescarga(id, isVolumeGMDireto);
    }

    public void updateVolumesByMonitDescargasAntigas(final Long idFilial, final DateTime dhTmpExc) {
        getVolumeNotaFiscalDAO().updateVolumesByMonitDescargasAntigas(idFilial, dhTmpExc);
    }

    public void updateVolumesByMonitDescargasComCTRCEmitido(final Long idFilial, final DateTime dhTmpEmi) {
        getVolumeNotaFiscalDAO().updateVolumesByMonitDescargasComCTRCEmitido(idFilial, dhTmpEmi);
    }

    /**
     * Busca Total de Volumes Pesados do CTRC
     *
     * @param idConhecimento
     * @param idFilial
     * @return
     */
    public BigDecimal findTotalVolumesCTRC(final Long idConhecimento, final Long idFilial) {
        return getVolumeNotaFiscalDAO().findTotalVolumesCTRC(idConhecimento, idFilial);
    }

    public Map findVolumesChecadosPorIdCTRC(Long idConhecimento, Long idFilial, boolean isVolumeGMDireto) {
        Object[] result = getVolumeNotaFiscalDAO().findVolumesChecadosPorIdCTRC(idConhecimento, idFilial, isVolumeGMDireto);
        Map map = new HashMap();
        map.put(QT_VOLUMES_TOTAL, result[0]);
        map.put(QT_VOLUMES, result[1]);
        return map;
    }

    /**
     * Retorna o valor total das dimensoes de volumes de notas fiscais
     * relacionadas  ao conhecimento
     * <p>
     * nrDimensao1CM, nrDimensao2CM, nrDimensao3CM
     *
     * @param idConhecimento
     * @return BigDecimal
     */
    public BigDecimal findTotalDimensaoVolumes(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findTotalDimensaoVolumes(idConhecimento);
    }

    /**
     * Retorna o valor total da cubagem de volumes de notas fiscais
     * relacionadas  ao conhecimento
     * <p>
     * nrCubagemM3
     *
     * @param idConhecimento
     * @return BigDecimal
     */
    public BigDecimal findTotalCubagemVolumes(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findTotalCubagemVolumes(idConhecimento);
    }

    /**
     * gera eventos para o VolumeNotaFiscal removido do PreManifestoVolume
     *
     * @param idManifesto
     * @param idVolumeNotaFiscal
     */
    public void generateEventoParaVolumeRemovidoDoPreManifestoVolume(Long idManifesto, Long idVolumeNotaFiscal, String tpScan) {
        VolumeNotaFiscal volumeNotaFiscal = this.findById(idVolumeNotaFiscal);
        Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
        ControleCarga controleCarga = manifesto.getControleCarga();

        List<Short> cdEventosList = new ArrayList<Short>();
        if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("29"))) {
            cdEventosList.add(Short.valueOf("86"));
            cdEventosList.add(Short.valueOf("123"));
        } else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("30"))) {
            cdEventosList.add(Short.valueOf("63"));
            cdEventosList.add(Short.valueOf("124"));
        } else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("31"))) {
            cdEventosList.add(Short.valueOf("63"));
            cdEventosList.add(Short.valueOf("124"));
            cdEventosList.add(Short.valueOf("121"));
        } else if (volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("32"))) {
            cdEventosList.add(Short.valueOf("86"));
            cdEventosList.add(Short.valueOf("123"));
            cdEventosList.add(Short.valueOf("122"));
        }

        if (controleCarga != null && "V".equals(controleCarga.getTpControleCarga().getValue())) {

            //na ET 05.01.01.07 está código 30 e 125, mas de acordo com conversa do o Leonardo vamos manter igual ao Pré-manifesto Documento
            //ou seja, somente gera evento se já existe evento de "FIM DE DESCARGA"

            Short[] codigosEvento = new Short[]{Short.valueOf("125")};
            List<EventoVolume> listEventoVolume = eventoVolumeService.findEventoVolumeNotaFiscal(idVolumeNotaFiscal, SessionUtils.getFilialSessao().getIdFilial(), codigosEvento);

            if (!listEventoVolume.isEmpty()) {
                if (!cdEventosList.contains(Short.valueOf("129"))) {
                    cdEventosList.add(Short.valueOf("129"));
                }
            } else {
                if (!cdEventosList.contains(Short.valueOf("63"))) {
                    cdEventosList.add(Short.valueOf("63"));
                }
            }
        } else if (controleCarga == null || "C".equals(controleCarga.getTpControleCarga().getValue())) {

            //na ET 05.01.01.07 está código 31 e 125, mas de acordo com conversa do o Leonardo vamos manter igual ao Pré-manifesto Documento
            //ou seja, somente gera evento se já existe evento de "FIM DE DESCARGA"
            Short[] codigosEvento = new Short[]{Short.valueOf("125")};
            List<EventoVolume> listEventoVolume = eventoVolumeService.findEventoVolumeNotaFiscal(idVolumeNotaFiscal, SessionUtils.getFilialSessao().getIdFilial(), codigosEvento);
            if (!listEventoVolume.isEmpty()) {
                if (!cdEventosList.contains(Short.valueOf("128"))) {
                    cdEventosList.add(Short.valueOf("128"));
                }
            } else {
                if (!cdEventosList.contains(Short.valueOf("86"))) {
                    cdEventosList.add(Short.valueOf("86"));
                }
            }
        }

        for (Short cdEvento : cdEventosList) {
            eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, cdEvento, tpScan);
        }
    }

    /**
     * LMS-1261 - Devolve Volume para o TERMINAL
     *
     * @param volumeNotaFiscal
     * @param localizacaoTerminal
     * @author André Valadas
     */
    public void executeReleaseVolume(final VolumeNotaFiscal volumeNotaFiscal, final LocalizacaoMercadoria localizacaoTerminal) {
        volumeNotaFiscal.setLocalizacaoMercadoria(localizacaoTerminal);
        this.store(volumeNotaFiscal);
    }

    /**
     * Gera evento para o PreManifestoVolume adicionado no PreManifesto
     *
     * @param preManifestoVolume
     * @param tpScan
     */
    public void generateEventoParaVolumeAdicionadoNoPreManifestoVolume(PreManifestoVolume preManifestoVolume, String tpScan) {

        Manifesto manifesto = preManifestoVolume.getManifesto();
        List<Short> cdEventosList = new ArrayList();
        if ("V".equals(manifesto.getTpManifesto().getValue())) {
            if ("PM".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO);
            } else if ("EC".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO);
                cdEventosList.add(Short.valueOf("25"));
            } else if ("CC".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO);
                cdEventosList.add(Short.valueOf("25"));
                cdEventosList.add(Short.valueOf("26"));
            }
        } else if ("E".equals(manifesto.getTpManifesto().getValue())) {

            if ("PM".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO);
            } else if ("EC".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO);
                cdEventosList.add(Short.valueOf("24"));
            } else if ("CC".equals(manifesto.getTpStatusManifesto().getValue())) {
                cdEventosList.add(ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO);
                cdEventosList.add(Short.valueOf("24"));
                cdEventosList.add(Short.valueOf("27"));
            }
        }

        for (Short cdEvento : cdEventosList) {
            eventoVolumeService.generateEventoVolume(preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal(),
                    cdEvento, tpScan);
        }
    }

    /**
     * Atualiza os volumes da esteira
     *
     * @param controleEsteira
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> processaVolumeControleEsteira(final ControleEsteira controleEsteira) {
        return executeVolumeControleEsteira(controleEsteira);
    }

    public Map<String, Object> executeVolumeControleEsteira(final ControleEsteira controleEsteira) {
        Map<String, Object> volume = findVolumeNotaFiscalNaoAferido(controleEsteira.getCodBarras());
        if (volume != null && !volume.isEmpty()) {
            final Long idVolumeNotaFiscal = (Long) volume.get(ID_VOLUME_NOTA_FISCAL);
            DateTime dataPesagem = JTDateTimeUtils.yearMonthDayToDateTime(controleEsteira.getData(), DateTimeZone.forID(AMERICA_SAO_PAULO));
            if (controleEsteira.getHora() != null && (controleEsteira.getHora().length() == 8 || controleEsteira.getHora().length() == 5)) {
                String[] horario = controleEsteira.getHora().split(":");
                if (horario != null && horario.length == FILL_FIELD_WITH_3_ZEROS || horario.length == 2) {
                    dataPesagem = JTDateTimeUtils.setHour(dataPesagem, Integer.valueOf(horario[0]));
                    dataPesagem = JTDateTimeUtils.setMinute(dataPesagem, Integer.valueOf(horario[1]));
                }
            }

            //LMS-2354
            // Adicionados campos nrDimensao1Sorter, nrDimensao2Sorter, nrDimensao3Sorter e psAferidoSorter (atualizados quando for recálculo)
            // Adicionado parâmetro no map verificaFechaRecalculoConhecimento
            UpdateVolumeDadosSorterDto updateVolumeDadosSorterDto = new UpdateVolumeDadosSorterDto();
            updateVolumeDadosSorterDto.setIdVolumeNotaFiscal(idVolumeNotaFiscal);


            updateVolumeDadosSorterDto.setNrDimensao1Cm(controleEsteira.getComprimento().intValue());
            updateVolumeDadosSorterDto.setNrDimensao2Cm(controleEsteira.getLargura().intValue());
            updateVolumeDadosSorterDto.setNrDimensao3Cm(controleEsteira.getAltura().intValue());
            updateVolumeDadosSorterDto.setPsAferido(controleEsteira.getPeso());
            updateVolumeDadosSorterDto.setDhPesagem(dataPesagem);

            updateVolumeDadosSorterDto.setNrDimensao1Sorter(controleEsteira.getComprimento().intValue());
            updateVolumeDadosSorterDto.setNrDimensao2Sorter(controleEsteira.getLargura().intValue());
            updateVolumeDadosSorterDto.setNrDimensao3Sorter(controleEsteira.getAltura().intValue());
            updateVolumeDadosSorterDto.setPsAferidoSorter(controleEsteira.getPeso());
            getVolumeNotaFiscalDAO().updateVolumeDadosSorterById(updateVolumeDadosSorterDto);

            volume.put("verificaFechaConhecimento", Boolean.TRUE);
            volume.put("verificaFechaRecalculoConhecimento", Boolean.FALSE);

            monitoramentoDescargaService.updateMonitoramentoDescargaAposPesagemVolume(idVolumeNotaFiscal, false, false);

            getDao().flush();

        } else {

            //LMS-2354
            // Adicionados campos nrDimensao1Sorter, nrDimensao2Sorter, nrDimensao3Sorter e psAferidoSorter (atualizados quando for recálculo)
            // Adicionado parâmetro no map verificaFechaRecalculoConhecimento
            volume = findVolumeNotaFiscalNaoAferidoSorter(controleEsteira.getCodBarras());
            if (volume != null && !volume.isEmpty()) {

                final Long idVolumeNotaFiscal = (Long) volume.get(ID_VOLUME_NOTA_FISCAL);

                DateTime dataPesagem = JTDateTimeUtils.yearMonthDayToDateTime(controleEsteira.getData(), DateTimeZone.forID(AMERICA_SAO_PAULO));

                dataPesagem = setDataPesagem(controleEsteira, dataPesagem);

                UpdateVolumeDadosSorterDto updateVolumeDadosSorterDto = new UpdateVolumeDadosSorterDto();
                updateVolumeDadosSorterDto.setIdVolumeNotaFiscal(idVolumeNotaFiscal);

                if (recalculoSorter(volume)) {

                    updateVolumeDadosSorterDto.setNrDimensao1Sorter(controleEsteira.getComprimento().intValue());
                    updateVolumeDadosSorterDto.setNrDimensao2Sorter(controleEsteira.getLargura().intValue());
                    updateVolumeDadosSorterDto.setNrDimensao3Sorter(controleEsteira.getAltura().intValue());
                    updateVolumeDadosSorterDto.setPsAferidoSorter(controleEsteira.getPeso());


                    getVolumeNotaFiscalDAO().updateVolumeDadosSorterById(updateVolumeDadosSorterDto);

                    volume.put("verificaFechaConhecimento", Boolean.FALSE);
                    volume.put("verificaFechaRecalculoConhecimento", isTipoCalculoNormal(volume) ? Boolean.TRUE : Boolean.FALSE);

                    getDao().flush();

                }
            } else {

                volume = findVolumeBynrVolumeEmbarque(controleEsteira.getCodBarras());
                if (volume == null || volume.isEmpty() || volume.get(PS_AFERIDO) != null || !"P".equals(((DomainValue) volume.get(TP_SITUACAO_CONHECIMENTO)).getValue())) {
                    if (volume == null) {
                        volume = new HashMap<String, Object>();
                    }
                    volume.put("verificaFechaConhecimento", Boolean.FALSE);
                    volume.put("verificaFechaRecalculoConhecimento", Boolean.FALSE);
                }

            }

        }
        executeVolumesSobra(controleEsteira);
        return volume;
    }

    private DateTime setDataPesagem(ControleEsteira controleEsteira, DateTime dataPesagem) {

        DateTime dataPesagemAux = dataPesagem;

        if (controleEsteira.getHora() != null && (controleEsteira.getHora().length() == 8 || controleEsteira.getHora().length() == 5)) {
            String[] horario = controleEsteira.getHora().split(":");
            if (horario != null && horario.length == FILL_FIELD_WITH_3_ZEROS || horario.length == 2) {
                dataPesagemAux = JTDateTimeUtils.setHour(dataPesagemAux, Integer.valueOf(horario[0]));
                dataPesagemAux = JTDateTimeUtils.setMinute(dataPesagemAux, Integer.valueOf(horario[1]));
            }
        }

        return dataPesagemAux;
    }

    private void setDadosSessao(Long idVolumeNotaFiscal) {
        Conhecimento c = conhecimentoService.findConhecimentoByIdVolumeNotaFiscal(idVolumeNotaFiscal);
        Filial f = filialService.findByIdInitLazyProperties(c.getFilialByIdFilialOrigem().getIdFilial(), true);
        SessionContext.set(SessionKey.FILIAL_KEY, f);
        Pais p = paisService.findByIdPessoa(f.getPessoa().getIdPessoa());
        SessionContext.set(SessionKey.PAIS_KEY, p);
    }


    public Map<String, Object> executeVolumeSorter(final ControleEsteira controleEsteira) {
        Map<String, Object> volume = findVolumeNotaFiscalSorter(controleEsteira.getCodBarras());

        if (MapUtils.isEmpty(volume)) {
            //LMS-XXXX Automação - Volume não encontrado para processar
            throw new BusinessException("LMS-45210");
        }

        final Long idVolumeNotaFiscal = (Long) volume.get(ID_VOLUME_NOTA_FISCAL);
        final Long idConhecimento = (Long) volume.get(ID_CONHECIMENTO);

        setDadosSessao(idVolumeNotaFiscal);

        if (ConstantesExpedicao.PRE_CONHECIMENTO.equals(getDomainToString(volume, TP_SITUACAO_CONHECIMENTO))
                && volume.get(PS_AFERIDO) == null) {
            executeUpdateVolumeDadosSorterDto(idVolumeNotaFiscal, controleEsteira, false);
            volume.put("verificaFechaConhecimento", Boolean.TRUE);
            volume.put("verificaFechaRecalculoConhecimento", Boolean.FALSE);
        } else if (volume.get("psAferidoSorter") == null && recalculoSorter(volume)) {
            executeUpdateVolumeDadosSorterDto(idVolumeNotaFiscal, controleEsteira, true);
            volume.put("verificaFechaConhecimento", Boolean.FALSE);
            volume.put("verificaFechaRecalculoConhecimento", isTipoCalculoNormal(volume) ? Boolean.TRUE : Boolean.FALSE);
        } else if (volume.get(PS_AFERIDO) != null || !ConstantesExpedicao.PRE_CONHECIMENTO.equals(getDomainToString(volume, TP_SITUACAO_CONHECIMENTO))) {
            executeUpdateVolumeDadosSorterDto(idVolumeNotaFiscal, controleEsteira, true);
            volume.put("verificaFechaConhecimento", Boolean.FALSE);
            volume.put("verificaFechaRecalculoConhecimento", Boolean.FALSE);
        } else {
            //LMS-45212 Automação - Volume não se enquadra nas regras de atualização
            throw new BusinessException("LMS-45212");
        }

        if(Boolean.FALSE.equals(isExisteEventoVolume(idVolumeNotaFiscal, controleEsteira.getNrLote()))) {
            executeVolumesSobra(idVolumeNotaFiscal, idConhecimento, controleEsteira);
        }

        return volume;
    }

    private boolean isTipoCalculoNormal(Map<String, Object> volume) {
        final String tpCalculoPreco = volume.get("tpCalculoPreco") == null ? null : ((DomainValue) volume.get("tpCalculoPreco")).getValue();
        return "N".equals(tpCalculoPreco);
    }

    private String getDomainToString(Map map, String key) {
        return MapUtilsPlus.getDomainValue(map, TP_SITUACAO_CONHECIMENTO).getValue();
    }

    private DateTime getDataPesagem(Long idVolumeNotaFiscal, ControleEsteira controleEsteira) {
        DateTime dataPesagem = JTDateTimeUtils.yearMonthDayToDateTime(controleEsteira.getData(), DateTimeZone.forID(AMERICA_SAO_PAULO));
        if (controleEsteira.getHora() != null && (controleEsteira.getHora().length() == 8 || controleEsteira.getHora().length() == 5)) {
            String[] horario = controleEsteira.getHora().split(":");
            if (horario != null && horario.length == FILL_FIELD_WITH_3_ZEROS || horario.length == 2) {
                dataPesagem = JTDateTimeUtils.setHour(dataPesagem, Integer.valueOf(horario[0]));
                dataPesagem = JTDateTimeUtils.setMinute(dataPesagem, Integer.valueOf(horario[1]));
            }
        }
        return dataPesagem;
    }

    private void executeUpdateVolumeDadosSorterDto(Long idVolumeNotaFiscal, ControleEsteira controleEsteira, boolean isRecalculo) {

        UpdateVolumeDadosSorterDto updateVolumeDadosSorterDto = new UpdateVolumeDadosSorterDto();
        updateVolumeDadosSorterDto.setIdVolumeNotaFiscal(idVolumeNotaFiscal);

        if (!isRecalculo) {
            updateVolumeDadosSorterDto.setNrDimensao1Cm(controleEsteira
                    .getComprimento().intValue());
            updateVolumeDadosSorterDto.setNrDimensao2Cm(controleEsteira
                    .getLargura().intValue());
            updateVolumeDadosSorterDto.setNrDimensao3Cm(controleEsteira.getAltura()
                    .intValue());
            updateVolumeDadosSorterDto.setPsAferido(controleEsteira.getPeso());
            updateVolumeDadosSorterDto.setDhPesagem(getDataPesagem(idVolumeNotaFiscal, controleEsteira));
        }

        updateVolumeDadosSorterDto.setNrDimensao1Sorter(controleEsteira
                .getComprimento().intValue());
        updateVolumeDadosSorterDto.setNrDimensao2Sorter(controleEsteira
                .getLargura().intValue());
        updateVolumeDadosSorterDto.setNrDimensao3Sorter(controleEsteira
                .getAltura().intValue());
        updateVolumeDadosSorterDto
                .setPsAferidoSorter(controleEsteira.getPeso());

        getVolumeNotaFiscalDAO().updateVolumeDadosSorterById(updateVolumeDadosSorterDto);

        if (!isRecalculo) {
            monitoramentoDescargaService.updateMonitoramentoDescargaAposPesagemVolume(idVolumeNotaFiscal, false, false);
        }
        getDao().getSessionFactory().getCurrentSession().flush();
        getDao().getSessionFactory().getCurrentSession().clear();
    }

    /**
     * LMS-2354
     *
     * @param volume -   Verificar:
     *               * a.  Se o Documento de Serviço vinculado ao volume já está emitido (C.TP_SITUACAO_CONHECIMENTO = 'E' para C.ID_CONHECIMENTO = NFC.ID_CONHECIMENTO e NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO);
     *               b.  Se o volume ainda não possui informações de pesagem no Sorter (VNF.PS_AFERIDO_SORTER = nulo);
     *               c.  Se a finalidade do documento de serviço é Normal, Refaturamento ou Devolução Parcial (CONHECIMENTO.TP_CONHECIMENTO = NO, DP, RF)
     *               d.  Se o recálculo do sorter ainda não foi realizado (DOCUMENTO_SERVICO.VL_ TOTAL_SORTER = nulo);
     *               e.  Se o Tipo de cálculo selecionado for "Normal".
     * @param volume
     * @return
     */
    private boolean recalculoSorter(Map<String, Object> volume) {

        String tpSituacaoConhecimento = volume.get(TP_SITUACAO_CONHECIMENTO) == null ? null : ((DomainValue) volume.get(TP_SITUACAO_CONHECIMENTO)).getValue();
        String tpConhecimento = volume.get(TP_CONHECIMENTO) == null ? null : ((DomainValue) volume.get(TP_CONHECIMENTO)).getValue();

        if ("E".equals(tpSituacaoConhecimento) &&
                volume.get("psAferidoSorter") == null &&
                ("NO".equals(tpConhecimento) || "DP".equals(tpConhecimento) || "RF".equals(tpConhecimento))) {

            if (volume.get("vlTotalSorter") == null) {
                // LMS-4888 - Retirado "if" se tipo de cálculo selecionado for "Normal"
                return true;
            }
        }

        return false;
    }

    public void executeConhecimentoSorter(final Map<String, Object> dadosVolume) {
        final Long idFilialOrigem = (Long) dadosVolume.get(ID_FILIAL_ORIGEM);
        final Long idConhecimento = (Long) dadosVolume.get(ID_CONHECIMENTO);
        final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);

        /** Verifica se todos volumes foram pesados */
        final Boolean volumesPendentes = verifyVolumesPendentes(conhecimento.getNrConhecimento(), idFilialOrigem);
        if (Boolean.FALSE.equals(volumesPendentes)) {
            final Map volumeUM = findVolumesUMchecadosPorIdCTRC(conhecimento.getIdDoctoServico(), idFilialOrigem, false);
            if (volumeUM != null && !volumeUM.isEmpty()) {
                final Long qtVolumesTotal = (Long) volumeUM.get(QT_VOLUMES_TOTAL);
                final Long qtVolumes = (Long) volumeUM.get(QT_VOLUMES);

                /** Verifica a Quantidade Total de Volumes */
                if (qtVolumesTotal.compareTo(qtVolumes) <= 0) {
                    dadosVolume.put(TP_DOCTO_SERVICO, conhecimento.getTpDoctoServico());
                    dadosVolume.put(ID_DOCTO_SERVICO, conhecimento.getIdDoctoServico());

                    /** Busca Data de Previsão de Entrega */
                    final Map dpeMap = getDpe(conhecimento);
                    dadosVolume.put(DT_PREVISTA_ENTREGA, (YearMonthDay) dpeMap.get(DT_PRAZO_ENTREGA));
                    dadosVolume.put(NR_DIAS_PREV_ENTREGA, (Long) dpeMap.get(NR_PRAZO));

                    /** Seta como processo Atutomatizado */
                    dadosVolume.put("blEstacaoAutomatizada", Boolean.TRUE);
                    calcularFreteService.fechaEmiteCTRC(dadosVolume);

                    conhecimentoService.executeCancelamentoPorBloqueioValores(conhecimento, true);
                }
            }
        }
    }

    public void executeRecalculoConhecimentoSorter(Map<String, Object> dadosVolume) {

        final Long idFilialOrigem = (Long) dadosVolume.get(ID_FILIAL_ORIGEM);
        final Long idConhecimento = (Long) dadosVolume.get(ID_CONHECIMENTO);
        final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);

        /** Verifica se todos volumes foram pesados */
        final Boolean volumesPendentes = verifyVolumesPendentesSorter(conhecimento.getNrConhecimento(), idFilialOrigem);
        if (Boolean.FALSE.equals(volumesPendentes)) {

            Frete frete = recalculoFreteService.executeRecalculoFreteConhecimento(conhecimento, true);

            Conhecimento conhecimentoSalvar = conhecimentoService.findById(idConhecimento);

            conhecimentoSalvar.setVlTotalSorter(frete.getCalculoFrete().getVlTotal());
            conhecimentoSalvar.setVlLiquidoSorter(frete.getCalculoFrete().getVlDevido());
            conhecimentoSalvar.setPsCubadoReal(frete.getCalculoFrete().getDoctoServico().getPsCubadoReal());
            conhecimentoSalvar.setNrFatorCubagem(frete.getCalculoFrete().getDoctoServico().getNrFatorCubagem());
            conhecimentoSalvar.setPsEstatistico(frete.getCalculoFrete().getDoctoServico().getPsEstatistico());
            conhecimentoSalvar.setNrCubagemEstatistica(frete.getCalculoFrete().getDoctoServico().getNrCubagemEstatistica());

            doctoServicoService.store(conhecimentoSalvar);

            for (ParcelaServico parcela : frete.getCalculoFrete().getParcelas()) {
                ParcelaRecalculoSorter prs = new ParcelaRecalculoSorter();
                prs.setIdDoctoServico(frete.getCalculoFrete().getDoctoServico().getIdDoctoServico());
                prs.setIdParcelaPreco(parcela.getParcelaPreco().getIdParcelaPreco());
                prs.setVlParcelaPreco(parcela.getVlParcela());

                parcelaRecalculoSorterService.store(prs);
            }

        }
    }

    public void updateLocalizacaoMercadoriaByDoctoServicoComBatch(Long idDoctoServico, LocalizacaoMercadoria localizacaoMercadoria,
                                                                  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        updateLocalizacaoMercadoriaByDoctoServicoComBatch(idDoctoServico, localizacaoMercadoria, adsmNativeBatchSqlOperations, null);
    }

    public void updateLocalizacaoMercadoriaByDoctoServicoComBatch(Long idDoctoServico, LocalizacaoMercadoria localizacaoMercadoria,
                                                                  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, List<String> aliasList) {

        String alias = null;
        List<VolumeNotaFiscal> listVolumeNotaFiscal = this.findByIdConhecimento(idDoctoServico);
        for (VolumeNotaFiscal volumeNotaFiscal : listVolumeNotaFiscal) {
            if (volumeNotaFiscal.getManifestoEntregaVolumes() != null && !volumeNotaFiscal.getManifestoEntregaVolumes().isEmpty()) {
                getDao().getAdsmHibernateTemplate().evict(volumeNotaFiscal);
                if (alias == null) {
                    alias = localizacaoMercadoria.getIdLocalizacaoMercadoria().toString();
                }
                if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL, alias)) {
                    adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL,
                            "ID_LOCALIZACAO_MERCADORIA", localizacaoMercadoria.getIdLocalizacaoMercadoria(),
                            alias);
                }
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(ConsVolumeNotaFiscal.TN_VOLUME_NOTA_FISCAL,
                        volumeNotaFiscal.getIdVolumeNotaFiscal(), alias);
            }
        }
        //Forçando update pois o volume nota fiscal é usado por outro método
        if (alias != null && aliasList != null && !aliasList.contains(alias)) {
            aliasList.add(alias);
        }
    }

    public void updateLocalizacaoMercadoriaByDoctoServico(Long idDoctoServico, LocalizacaoMercadoria localizacaoMercadoria) {
        List<VolumeNotaFiscal> listVolumeNotaFiscal = this.findByIdConhecimento(idDoctoServico);
        if (listVolumeNotaFiscal != null && !listVolumeNotaFiscal.isEmpty()) {
            for (VolumeNotaFiscal volumeNotaFiscal : listVolumeNotaFiscal) {
                if (volumeNotaFiscal.getManifestoEntregaVolumes() != null && !volumeNotaFiscal.getManifestoEntregaVolumes().isEmpty()) {
                    volumeNotaFiscal.setLocalizacaoMercadoria(localizacaoMercadoria);
                    this.store(volumeNotaFiscal);
                }

            }
        }
    }

    public void generateInformacaoVolumeSorter(ControleEsteira controleEsteira) {
        final Map<String, Object> dadosVolume = executeVolumeSorter(controleEsteira);
        if (dadosVolume != null) {
            if (Boolean.TRUE.equals(MapUtilsPlus.getBoolean(dadosVolume,
                    "verificaFechaConhecimento"))) {

                executeConhecimentoSorter(dadosVolume);

            } else if (Boolean.TRUE.equals(MapUtilsPlus.getBoolean(dadosVolume,
                    "verificaFechaRecalculoConhecimento"))) {

                executeRecalculoConhecimentoSorter(dadosVolume);
            }
        } else {
            //LMS-45029 Automação - Volume não foi encontrado para ser atualizado
            throw new BusinessException("LMS-45209");
        }
    }

    @Transactional
    public void atualizaInformacaoDescarga(ControleEsteira controleEsteira) {

        Map<String, Object> volume = findVolumeNotaFiscalSorter(controleEsteira.getCodBarras());

        if (MapUtils.isEmpty(volume)) {
            throw new BusinessException("LMS-45210");
        }

        final Long idVolumeNotaFiscal = (Long) volume.get(ID_VOLUME_NOTA_FISCAL);
        final Long idConhecimento = (Long) volume.get(ID_CONHECIMENTO);

        if(Boolean.TRUE.equals(isExisteEventoVolume(idVolumeNotaFiscal, controleEsteira.getNrLote()))) {
            return;
        }

        setDadosSessao(idVolumeNotaFiscal);

        if ((ConstantesExpedicao.PRE_CONHECIMENTO.equals(getDomainToString(volume, TP_SITUACAO_CONHECIMENTO))
                && volume.get(PS_AFERIDO) == null)
                || (volume.get("psAferidoSorter") == null && recalculoSorter(volume)
                || (volume.get(PS_AFERIDO) != null || !ConstantesExpedicao.PRE_CONHECIMENTO.equals(getDomainToString(volume, TP_SITUACAO_CONHECIMENTO))))) {
            executeVolumesSobra(idVolumeNotaFiscal, idConhecimento, controleEsteira);
        } else {
            throw new BusinessException("LMS-45212");
        }

    }

    private Boolean isExisteEventoVolume(Long idVolumeNotaFiscal, String nrLote) {

        String sgFilialSorter = nrLote.substring(0, FILL_FIELD_WITH_3_ZEROS).toUpperCase();
        Filial filialSorter = filialService.findFilial(361L, sgFilialSorter);
        Evento evento = eventoService.findByCdEvento(Short.valueOf("30"));
        Long quantidade = this.eventoVolumeService.findQuantidadeEventoVolumeByIdVolumeByIdFilialByCdEvento(idVolumeNotaFiscal,
                filialSorter.getIdFilial(), evento.getIdEvento());

        return quantidade > 0;
    }

    public Long findTotalVolumesNaoAferidos(Long idMonitoramentoDescarga, boolean isVolumeGMDireto) {
        return getVolumeNotaFiscalDAO().findTotalVolumesNaoAferidos(idMonitoramentoDescarga, isVolumeGMDireto);
    }

    public List<VolumeNotaFiscal> findVolumesNotaFiscalByIdMonitoramento(Long idMonitoramentoDescarga) {
        return getVolumeNotaFiscalDAO().findVolumesNotaFiscalByIdMonitoramento(idMonitoramentoDescarga);
    }

    public List<Long> findVolumeByIdConhecimentoAndNrSequencia(final Long idConhecimento, final Integer nrSequencia) {
        return getVolumeNotaFiscalDAO().findVolumeByIdConhecimentoAndNrSequencia(idConhecimento, nrSequencia);
    }

    public List<Long> findVolumeByClienteNotaFiscalSequenciaVol(final Long idCliente, final Integer nrNotaFiscal, final Integer nrSequencia) {
        return getVolumeNotaFiscalDAO().findVolumeByClienteNotaFiscalSequenciaVol(idCliente, nrNotaFiscal, nrSequencia);
    }

    public List findDadosVolumeReimpressao(PaginatedQuery paginatedQuery) {

        List result = getVolumeNotaFiscalDAO().findDadosVolumeReimpressao(paginatedQuery);
        buildMapFindDadosVolume(paginatedQuery, result, false, false);

        return result;
    }
    
    public Map<String,Object> findAtendimentoByMunicipioFilialServicoVigente(Long idMunicipio, Long idFilial, Long idServico){
    	return getVolumeNotaFiscalDAO().findAtendimentoByMunicipioFilialServicoVigente(idMunicipio,idFilial, idServico);
    }

    /**
     * @param idDoctoServico
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedVolumesByDoctoServico(Long idDoctoServico, FindDefinition findDefinition) {
        return getVolumeNotaFiscalDAO().findPaginatedVolumesByDoctoServico(idDoctoServico, findDefinition);
    }

    /**
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountVolumesByDoctoServico(Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().getRowCountVolumesByDoctoServico(idDoctoServico);
    }

    public ResultSetPage<Map<String, Object>> findPaginatedVolumesByDoctoServicoAndManifestoEntrega(Long idDoctoServico, Long idManifestoEntrega, FindDefinition findDefinition) {
        return getVolumeNotaFiscalDAO().findPaginatedVolumesByDoctoServicoAndManifestoEntrega(idDoctoServico, idManifestoEntrega, findDefinition);
    }

    public Integer getRowCountVolumesByDoctoServicoAndManifestoEntrega(Long idDoctoServico, Long idManifestoEntrega) {
        return getVolumeNotaFiscalDAO().getRowCountVolumesByDoctoServicoAndManifestoEntrega(idDoctoServico, idManifestoEntrega);
    }


    public List<VolumeNotaFiscal> findByIdConhecimento(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findByIdConhecimento(idConhecimento);
    }

    public VolumeNotaFiscal findVolumeByIdConhecimento(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findVolumeByIdConhecimento(idConhecimento);
    }

    public List<VolumeNotaFiscal> findByIdConhecimentoAndIdLocalizacaoFilial(Long idConhecimento, Long idFilial) {
        return getVolumeNotaFiscalDAO().findByIdConhecimentoAndIdLocalizacaoFilial(idConhecimento, idFilial);
    }

    /**
     * Busca a lista de volumes que estejam unitizados no dispositivo passado por parâmetro.
     *
     * @param idDispositivoUnitizacao
     * @return lista de volumes
     */
    public List<VolumeNotaFiscal> findVolumesByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
        return getVolumeNotaFiscalDAO().findVolumesByIdDispositivoUnitizacao(idDispositivoUnitizacao);
    }

    public Integer getRowCount(Map criteria) {
        return getVolumeNotaFiscalDAO().getRowCount(criteria);
    }

    public ResultSetPage<VolumeNotaFiscal> findPaginated(PaginatedQuery paginatedQuery) {
        return getVolumeNotaFiscalDAO().findPaginated(paginatedQuery);
    }

    public ResultSetPage<Map<String, Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {
        ResultSetPage rsp = this.findPaginated(paginatedQuery);

        List<VolumeNotaFiscal> list = rsp.getList();
        List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>(list.size());

        for (VolumeNotaFiscal volume : list) {
            Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();

            Conhecimento conhecimentoReentrega = conhecimentoService.findConhecimentoReentregaByDoctoServicoOriginal(conhecimento.getIdDoctoServico());
            if (conhecimentoReentrega != null) {
                conhecimento = conhecimentoReentrega;
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ID_VOLUME_NOTA_FISCAL, volume.getIdVolumeNotaFiscal());
            map.put(NR_VOLUME_EMBARQUE, volume.getNrVolumeEmbarque());
            map.put(NR_VOLUME_COLETA, volume.getNrVolumeColeta());
            if (volume.getTpVolume().equals(ConstantesExpedicao.TP_VOLUME_DETALHE)) {
                map.put(NR_SEQUENCIA, volume.getNrSequenciaPalete() + "/" + conhecimento.getQtVolumes());
            } else {
                map.put(NR_SEQUENCIA, volume.getNrSequencia() + "/" + conhecimento.getQtVolumes());
            }
            map.put("sgFilialOrigem", conhecimento.getFilialByIdFilialOrigem().getSgFilial());
            map.put(NR_CONHECIMENTO, conhecimento.getNrConhecimento());
            map.put("dvConhecimento", conhecimento.getDvConhecimento());
            map.put(TP_CONHECIMENTO, conhecimento.getTpConhecimento().getDescription());
            map.put("nrNotaFiscal", volume.getNotaFiscalConhecimento().getNrNotaFiscal());
            if (volume.getDispositivoUnitizacao() != null) {
                DispositivoUnitizacao dispositivo = volume.getDispositivoUnitizacao();
                map.put("dsTipoDipositivoUnitizacao", dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
                map.put("nrIdentificacaoDipositivoUnitizacao", dispositivo.getNrIdentificacao());
                map.put("idDispositivoUnitizacao", dispositivo.getIdDispositivoUnitizacao());
                if (dispositivo.getLocalizacaoFilial() != null) {
                    map.put("sgFilialLocalizacaoDispositivo", dispositivo.getLocalizacaoFilial().getSgFilial());
                }
                if (dispositivo.getLocalizacaoMercadoria() != null) {
                    map.put("dsLocalizacaoMercadoriaDispositivo", dispositivo.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
                }
            }
            if (volume.getMacroZona() != null) {
                MacroZona mz = volume.getMacroZona();
                map.put("idMacroZona", mz.getIdMacroZona());
                map.put("dsMacroZona", mz.getDsMacroZona());
                map.put("sgFilialTerminal", mz.getTerminal().getFilial().getSgFilial());
                map.put("nmPessoaTerminal", mz.getTerminal().getPessoa().getNmPessoa());
            }
            if (volume.getLocalizacaoFilial() != null) {
                map.put("sgFilialLocalizacao", volume.getLocalizacaoFilial().getSgFilial());
            }
            if (volume.getLocalizacaoMercadoria() != null) {
                map.put("dsLocalizacaoMercadoria", volume.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria());
            }
            
            map.put("possuiEventoEntrega", eventoVolumeService.eventoVolumePossuiEventoEntrega(volume.getIdVolumeNotaFiscal()));
            
            retorno.add(map);
        }
        rsp.setList(retorno);
        return rsp;
    }


    /**
     * Valida se o volume está em um manifesto válido.
     *
     * @param volume
     * @return
     */
    public boolean validateVolumeManifestado(VolumeNotaFiscal volume, Filial filial) {
        return getVolumeNotaFiscalDAO().validateVolumeManifestado(volume, filial);
    }


    /**
     * Seta volumeNotaFiscal.psAferido para null em volumeNotaFiscal.tpVolume
     * igual a 'D' ou 'N' e com volumeNotaFiscal.idMonitoramentoDescarga igual
     * ao parametro
     *
     * @param idMonitoramentoDescarga
     * @param isVolumeGMDireto
     * @author lucianos
     */
    public void updatePsAferidoToNull(final Long idMonitoramentoDescarga, final boolean isVolumeGMDireto) {
        this.getVolumeNotaFiscalDAO().updatePsAferidoToNull(idMonitoramentoDescarga, isVolumeGMDireto);
    }


    /**
     * Retorna o somatório do volumeNotaFiscal.qtVolumes e a quantidade de
     * volumeNotaFiscal que estão relacionados com o monitoramentoDescarga
     *
     * @param idMonitoramentoDescarga
     * @param isVolumeGMDireto
     * @return
     * @author lucianos
     */
    public Object[] countQtdVolumesByIdMonitoramentoDescargaAndTpVolume(Long idMonitoramentoDescarga,
                                                                        boolean isVolumeGMDireto) {
        return this.getVolumeNotaFiscalDAO().countQtdVolumesByIdMonitoramentoDescargaAndTpVolume(
                idMonitoramentoDescarga, isVolumeGMDireto);
    }


    public Boolean hasVolumesCarregados(Long idManifesto, Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().hasVolumesCarregadosByIdManifesto(idManifesto, idDoctoServico);
    }

    private void executeVolumesSobra(ControleEsteira controleEsteira) {
        VolumeNotaFiscal volume = findVolumeByBarCodeUniqueResult(controleEsteira.getCodBarras());
        if (volume != null) {
            Long idVolumeNotaFiscal = volume.getIdVolumeNotaFiscal();
            Long idConhecimento = volume.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento();
            executeVolumesSobra(idVolumeNotaFiscal, idConhecimento, controleEsteira);
        } else {
            log.error("Volume " + controleEsteira.getCodBarras() + " não encontrado na filial " + controleEsteira.getNrLote().substring(0, FILL_FIELD_WITH_3_ZEROS).toUpperCase());
        }
    }

    private void executeVolumesSobra(Long idVolumeNotaFiscal, Long idConhecimento, ControleEsteira controleEsteira) {
        DateTime dhLeituraSorter;
        try {
            dhLeituraSorter = new DateTime(Integer.parseInt(controleEsteira
                    .getNrLote().substring(6, 10)), // ano
                    Integer.parseInt(controleEsteira.getNrLote().substring(10,
                            12)), // mes
                    Integer.parseInt(controleEsteira.getNrLote().substring(12,
                            14)), // dia
                    Integer.parseInt(controleEsteira.getNrLote().substring(14,
                            16)), // hora
                    Integer.parseInt(controleEsteira.getNrLote().substring(16,
                            18)), // minuto
                    Integer.parseInt(controleEsteira.getNrLote().substring(18,
                            20)), // segundo
                    0);
        } catch (Exception e) {
            dhLeituraSorter = JTDateTimeUtils.getDataHoraAtual();
            LOGGER.warn(e);
        }

        String sgFilialSorter = controleEsteira.getNrLote().substring(0, FILL_FIELD_WITH_3_ZEROS).toUpperCase();
        Filial filialSorter = filialService.findFilial(361L, sgFilialSorter);

        if (filialSorter != null && Boolean.TRUE.equals(filialSorter.getBlSorter())) {

            VolumeNotaFiscal volume = this.findById(idVolumeNotaFiscal);

			/* Busca descarga iniciada para este volume nesta filial */
            CarregamentoDescarga descargaIniciada = carregamentoDescargaServie.findDescargaIniciadaByVolumeAndFilial(idVolumeNotaFiscal, filialSorter.getIdFilial());
            if (descargaIniciada != null) {
				/* Verifica de volume já foi descarrgado através dessa descarga encontrada */
                boolean isVolumeDescarregado = carregamentoDescargaVolumeService.validateVolumeDescarregado(descargaIniciada.getIdCarregamentoDescarga(), idVolumeNotaFiscal);
                if (!isVolumeDescarregado) {
                    this.storeCarregamentoDescargaVolume(volume, dhLeituraSorter, descargaIniciada);
                }
            }

            Long idEventoGerado = (Long) eventoVolumeService.generateEventoVolumeSorter(volume, filialSorter,
                    dhLeituraSorter, SessionUtils.getUsuarioLogado(),
                    new DomainValue(ConstantesSim.TP_DESCARGA_SORTER));

            Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);
            if (conhecimento != null && !conhecimento.getFilialByIdFilialOrigem().equals(filialSorter)
                    && !this.validateVolumeManifestado(volume, filialSorter)) {
                VolumeSobraFilial sobraFilial = new VolumeSobraFilial();
                sobraFilial.setDhCriacao(dhLeituraSorter);
                sobraFilial.setVolumeNotaFiscal(volume);
                sobraFilial.setFilial(filialSorter);
                sobraFilial.setEventoVolume(eventoVolumeService.findById(idEventoGerado));
                volumeSobraFilialService.store(sobraFilial);
            }
        }
    }

    /**
     * LMS-5464 - Ajustar a rotina Batch do Sorter para gravar registro de leitura de volume na tabela CARREG_DESC_VOLUME
     * <p>
     * Caso exista carregamento_descarga para o volume, inserir regitro na tabela CARREG_DESC_VOLUME
     *
     * @param volume          que será gravado em CARREG_DESC_VOLUME caso exista carregamento_descarga para ele
     * @param dhLeituraSorter data e hora da leitura no sorter
     */
    private void storeCarregamentoDescargaVolume(VolumeNotaFiscal volume, DateTime dhLeituraSorter, CarregamentoDescarga carregamento) {

        // Insere registro na tabela CARREG_DESC_VOLUME
        CarregamentoDescargaVolume carregamentoVolume = new CarregamentoDescargaVolume();
        carregamentoVolume.setCarregamentoDescarga(carregamento);
        carregamentoVolume.setVolumeNotaFiscal(volume);
        carregamentoVolume.setQtVolumes(volume.getQtVolumes());
        carregamentoVolume.setDhOperacao(dhLeituraSorter);
        carregamentoVolume.setTpScan(new DomainValue("SS"));

        carregamentoDescargaVolumeService.store(carregamentoVolume);

    }

    /**
     * Busca Volumes do Manifesto
     *
     * @param idManifesto
     * @param projection
     * @param alias
     * @param criterions
     * @return
     * @author André Valadas
     */
    public List<VolumeNotaFiscal> findByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions) {
        return getVolumeNotaFiscalDAO().findByIdManifesto(idManifesto, projection, alias, criterions);
    }

    public List<VolumeNotaFiscal> findByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias) {
        return findByIdManifesto(idManifesto, projection, alias, null);
    }

    public List<VolumeNotaFiscal> findByIdManifesto(final Long idManifesto, final Map<String, String> alias, final List<Criterion> criterions) {
        return findByIdManifesto(idManifesto, null, alias, criterions);
    }

    public Boolean findObrigatoriedadeDadosDimensaoECubagemByNrVolume(Long idVolumeNotaFiscal) {
        return tabelaDivisaoClienteService.findObrigatoriedadeDadosDimensaoECubagemByNrVolume(idVolumeNotaFiscal);
    }

    public Boolean validateDimensaoOpcionalByFatorDensidade(Long idVolumeNotaFiscal) {
        return tabelaDivisaoClienteService.validateDimensaoOpcionalByFatorDensidade(idVolumeNotaFiscal);
    }

    public Boolean validateDimensaoCubagemPorTpCliente(Long idVolumeNotaFiscal) {
        Conhecimento conhecimento = conhecimentoService.findConhecimentoByIdVolumeNotaFiscal(idVolumeNotaFiscal);

        Cliente clienteBaseCalc = conhecimento.getClienteByIdClienteBaseCalculo();

        boolean isclienteEventualPotencial = "E".equals(clienteBaseCalc.getTpCliente().getValue()) ||
                "P".equals(clienteBaseCalc.getTpCliente().getValue());

        DivisaoCliente divisaoCliente = conhecimento.getDivisaoCliente();
        boolean blObrigaDimensoes = false;
        if (divisaoCliente != null && "S".equals(clienteBaseCalc.getTpCliente().getValue())) {
            TabelaDivisaoCliente tabelaDivisaoCliente = (TabelaDivisaoCliente) divisaoCliente.getTabelaDivisaoClientes().get(0);
            blObrigaDimensoes = BooleanUtils.isTrue(tabelaDivisaoCliente.getBlObrigaDimensoes());
        }

        return isclienteEventualPotencial || blObrigaDimensoes;
    }

    public Map findVolumesUMchecadosPorIdCTRC(Long idConhecimento, Long idFilial, boolean isVolumeGMDireto) {
        Object[] result = getVolumeNotaFiscalDAO().findVolumesUMchecadosPorIdCTRC(idConhecimento, idFilial, isVolumeGMDireto);
        Map map = new HashMap();
        map.put(QT_VOLUMES_TOTAL, result[0]);
        map.put(QT_VOLUMES, result[1]);
        return map;
    }

    /**
     * Retorna se existe informação de dimensão ou cubagem nos volumes
     *
     * @param idConhecimento
     * @return Boolean
     */
    public Boolean existeVolumeSemDimensaoCubagem(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().existeVolumeSemDimensaoCubagem(idConhecimento);
    }

    /**
     * Retorna quantidade de volumes do pré-CTRC tipo "M" ou "U" cujo peso
     * aferido não originou-se de uma pesagem em balança com módulo ou de forma
     * automatizada.
     *
     * @param idDoctoServico Identificador do documento de serviço
     * @return Quantidade de volumes tipo M ou U tem peso aferido em balança sem
     * módulo ou de forma não automatizada
     */
    public Integer countVolumeSemModulo(Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().countVolumeSemModulo(idDoctoServico);
    }

    /**
     * Retorna o valor total das dimensoes de volumes do sorter de notas fiscais
     * relacionadas  ao conhecimento
     * <p>
     * nrDimensao1CM, nrDimensao2CM, nrDimensao3CM
     *
     * @param idConhecimento
     * @return BigDecimal
     */
    public BigDecimal findTotalDimensaoVolumesSorter(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findTotalDimensaoVolumesSorter(idConhecimento);
    }

    public BigDecimal findTotalPsAferidoSorter(Long idConhecimento) {
        return getVolumeNotaFiscalDAO().findTotalPsAferidoSorter(idConhecimento);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer findCodigoLocalizacaoMercadoria(Long idVolumeNotaFiscal) {
        return getVolumeNotaFiscalDAO().findCodigoLocalizacaoMercadoria(idVolumeNotaFiscal);
    }

    public void setImpressoraService(ImpressoraService impressoraService) {
        this.impressoraService = impressoraService;
    }

    public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
        this.fluxoFilialService = fluxoFilialService;
    }

    public void setDpeService(DpeService dpeService) {
        this.dpeService = dpeService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public void setCalcularFreteService(CalcularFreteService calcularFreteService) {
        this.calcularFreteService = calcularFreteService;
    }

    public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
        this.monitoramentoDescargaService = monitoramentoDescargaService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public void setLiberacaoDocServService(LiberacaoDocServService liberacaoDocServService) {
        this.liberacaoDocServService = liberacaoDocServService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
        this.emitirDocumentoService = emitirDocumentoService;
    }

    public void setVolumeSobraFilialService(
            VolumeSobraFilialService volumeSobraFilialService) {
        this.volumeSobraFilialService = volumeSobraFilialService;
    }

    public void setLocalizacaoMercadoriaService(
            LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    public void setTabelaDivisaoClienteService(
            TabelaDivisaoClienteService tabelaDivisaoClienteService) {
        this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
    }

    public void setConteudoParametroFilialService(
            ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public void setDevedorDocServService(
            DevedorDocServService devedorDocServService) {
        this.devedorDocServService = devedorDocServService;
    }

    public void setRecalculoFreteService(RecalculoFreteService recalculoFreteService) {
        this.recalculoFreteService = recalculoFreteService;
    }

    public ParcelaRecalculoSorterService getParcelaRecalculoSorterService() {
        return parcelaRecalculoSorterService;
    }

    // LMS-5464
    public CarregamentoDescargaService getCarregamentoDescargaServie() {
        return carregamentoDescargaServie;
    }

    // LMS-5464
    public void setCarregamentoDescargaServie(CarregamentoDescargaService carregamentoDescargaServie) {
        this.carregamentoDescargaServie = carregamentoDescargaServie;
    }

    // LMS-5464
    public CarregamentoDescargaVolumeService getCarregamentoDescargaVolumeService() {
        return carregamentoDescargaVolumeService;
    }

    // LMS-5464
    public void setCarregamentoDescargaVolumeService(CarregamentoDescargaVolumeService carregamentoDescargaVolumeService) {
        this.carregamentoDescargaVolumeService = carregamentoDescargaVolumeService;
    }

    public void setParcelaRecalculoSorterService(ParcelaRecalculoSorterService parcelaRecalculoSorterService) {
        this.parcelaRecalculoSorterService = parcelaRecalculoSorterService;
    }

    public void atualizarFilialLocalizacaoVolume(VolumeNotaFiscal volumeNotaFiscal) {
        getVolumeNotaFiscalDAO().atualizarFilialLocalizacaoVolume(volumeNotaFiscal);
    }

    // Atualiza a localização do volume e coloca a localização da mercadoria no terminal
    public void executeAtualizarFilialLocalizacaoVolume(VolumeNotaFiscal volumeNotaFiscal) {
        LocalizacaoMercadoria localizacaoMercadoriaNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
        volumeNotaFiscal.setLocalizacaoFilial(SessionUtils.getFilialSessao());
        Long idConhecimento = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico().longValue();
        doctoServicoService.executeAtualizarObComplementoLocalizacao(idConhecimento, SessionUtils.getFilialSessao().getSgFilial(), SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
        volumeNotaFiscal.setLocalizacaoMercadoria(localizacaoMercadoriaNoTerminal);
        getVolumeNotaFiscalDAO().atualizarFilialLocalizacaoVolume(volumeNotaFiscal);
    }

    /**
     * Retorna a lista de volume nota fiscal onde o cliente da nota fiscal conhecimento possua o nrIdentificação
     * igual ao passado como parametro e o numero da nota fiscal seja igual ao passado como parametro.
     * Utilizado pela rotina generateEventoVolumeGM
     *
     * @param nrIdentificacao
     * @param nrNotaFiscal
     * @return
     */
    public List<VolumeNotaFiscal> findVolumesNotaFiscalByNrIdentificacaoClienteENrNota(String nrIdentificacao, Integer nrNotaFiscal) {
        return getVolumeNotaFiscalDAO().findVolumesNotaFiscalByNrIdentificacaoClienteENrNota(nrIdentificacao, nrNotaFiscal);
    }

    public void updateLocalizacaoELocalizacaoFilial(Long idVolume, LocalizacaoMercadoria localizacaoVolume, Filial filial) {
        Long idLocalizacao = null;
        Long idFilial = null;

        if (localizacaoVolume != null) {
            idLocalizacao = localizacaoVolume.getIdLocalizacaoMercadoria();
        }

        if (filial != null) {
            idFilial = filial.getIdFilial();
        }

        getVolumeNotaFiscalDAO().updateLocalizacaoELocalizacaoFilial(idVolume, idLocalizacao, idFilial);
    }

    /**
     * LMS-6531 - Verifica se para todos os volumes do pré-CTRC que sejam do
     * tipo "M" ou "U" (<tt>VNF.TP_VOLUME</tt>) o peso aferido originou-se de
     * uma pesagem com módulo ou de forma automatizada
     * (<tt>VNF.TP_ORIGEM_PESO</tt> igual a "A" ou "B"). Retorna <tt>false</tt>
     * se existir algum volume do tipo "M" ou "U" que não atenda a regra acima.
     *
     * @param idDoctoServico id do <tt>DoctoServico</tt>
     * @return <tt>false</tt> se existir algum volume tipo "M" ou "U" com peso
     * aferido de modo não automático, caso contrário retorna
     * <tt>true</tt>
     */
    public boolean pesoOriginadoBalancaModulo(Long idDoctoServico) {
        return getVolumeNotaFiscalDAO().pesoOriginadoBalancaModulo(idDoctoServico);
    }

    //LMS-5719
    public List<Map<String, Object>> findVolumesConsultaDivergencia(Long idDoctoServico, Long idControleCarga) {
        return getVolumeNotaFiscalDAO().findVolumesConsultaDivergencia(idDoctoServico, idControleCarga);
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public Object validateIsVolumeSobra(VolumeNotaFiscal volume, ControleCarga controleCarga, Filial filialDestino) {
        return getVolumeNotaFiscalDAO().validateVolumeSobra(volume, controleCarga, filialDestino);
    }

    public void executeFinalizaConhecimento(Map<String, Object> conhecimentoRPP) {

        final Map<String, Object> parameters = findDadosFinalizaConhecimentoRPP((Long) conhecimentoRPP.get(ID_CONHECIMENTO), (Long) conhecimentoRPP.get(NR_CONHECIMENTO));
        if (parameters != null) {
            if (MapUtilsPlus.getBoolean(conhecimentoRPP, "recalculo", false)) {
                executeRecalculoConhecimentoSorter(parameters);
            } else {
                if (!MapUtilsPlus.getBoolean(parameters, "blPesoAferido")) {
                    executeConhecimentoSorter(parameters);
                }
            }
        }
    }

    /*
     * Demanda para contingenciar prblema de calculo de frete
     * TUDO - Alterar Rotina para solução Completa
     */
    private Map<String, Object> findDadosFinalizaConhecimentoRPP(Long idConhecimento, Long nrConhecimento) {
        Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

        Filial f = conhecimento.getFilialByIdFilialOrigem();
        SessionContext.set(SessionKey.FILIAL_KEY, f);
        Pais p = paisService.findByIdPessoa(f.getPessoa().getIdPessoa());
        SessionContext.set(SessionKey.PAIS_KEY, p);

        Map<String, Object> parameter = new HashMap<String, Object>();

        parameter.put(NR_CONHECIMENTO, nrConhecimento);
        parameter.put(TP_CONHECIMENTO, conhecimento.getTpConhecimento());
        parameter.put(TP_SITUACAO_CONHECIMENTO, conhecimento.getTpSituacaoConhecimento().getValue());
        parameter.put("blIndicadorEdi", conhecimento.getBlIndicadorEdi());
        parameter.put("blUtilizaPesoEDIRemet", conhecimento.getClienteByIdClienteRemetente().getBlUtilizaFreteEDI());
        parameter.put(ID_CONHECIMENTO, conhecimento.getIdDoctoServico());
        parameter.put(ID_FILIAL_ORIGEM, conhecimento.getFilialByIdFilialOrigem().getIdFilial());
        parameter.put("blPesoAferido", conhecimento.getBlPesoAferido());

        return parameter;
    }

    public void executeFinalizaDescargaColeta(Long idMonitoramento) {
        MonitoramentoDescarga monitoramentoDescarga = monitoramentoDescargaService.findById(idMonitoramento);
        final Long qtVolumesNaoAferidos = this.findTotalVolumesNaoAferidos(monitoramentoDescarga.getIdMonitoramentoDescarga(), false);
        if (qtVolumesNaoAferidos != null && qtVolumesNaoAferidos.compareTo(Long.valueOf(0)) <= 0) {
            /** Busca total de volumes Aferidos, somando quantidade de volumes de cada volume_nota_fiscal */
            final List listVolumes = findQtVolumesNotaFiscalByIdMonitoramentoDescarga(monitoramentoDescarga.getIdMonitoramentoDescarga());
            if (listVolumes != null && !listVolumes.isEmpty()) {
                Map volumes = (Map) listVolumes.get(0);
                Long qtVolumesTotal = (Long) volumes.get(QT_VOLUMES_TOTAL);
                Long qtVolumes = (Long) volumes.get(QT_VOLUMES);
                /** Verifica se todos volumes foram Aferidos */
                if (qtVolumesTotal.compareTo(qtVolumes) <= 0) {
                    monitoramentoDescarga.setTpSituacaoDescarga(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DESCARGA_TODOS_VOLUMES_AFERIDOS));
                    monitoramentoDescarga.setDhUltimaAfericao(JTDateTimeUtils.getDataHoraAtual());
                    monitoramentoDescargaService.store(monitoramentoDescarga);
                }
            }
        }
    }
    
    public List<VolumeNotaFiscal> findVolumeNotaFiscalNotaCreditoPadrao(Long idConhecimento, Long idControleCarga) {
    	return getVolumeNotaFiscalDAO().findVolumeNotaFiscalNotaCreditoPadrao(idConhecimento, idControleCarga);
    }

    public void setDoctoServicoPPEPadraoService(DoctoServicoPPEPadraoService doctoServicoPPEPadraoService) {
        this.doctoServicoPPEPadraoService = doctoServicoPPEPadraoService;
    }

    public Boolean validateIsEtiquetaMWW(String nrCodigoBarras) {
        return getVolumeNotaFiscalDAO().validateIsEtiquetaMWW(nrCodigoBarras, obterDataMinimaMWW(), false);
    }

	public Boolean validateIsEtiquetaMWW(String nrCodigoBarras, boolean isFilialSorter) {
		return getVolumeNotaFiscalDAO().validateIsEtiquetaMWW(nrCodigoBarras, obterDataMinimaMWW(), isFilialSorter);
	}

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    private Date obterDataMinimaMWW() {
        BigDecimal diasLimiteMWW = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(PARAMETRO_GERAL_DIAS_LIMITE_MWW, false);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -diasLimiteMWW.intValue());
        return calendar.getTime();
    }
    
    public List<VolumeNotaFiscal> findVolumesNotaFiscalByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
    	return getVolumeNotaFiscalDAO().findVolumesNotaFiscalByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
    }

	public void updateLocalizacaoMercadoriaByNotaFiscalConhecimento(
			NotaFiscalConhecimento notaFiscalConhecimento,
			LocalizacaoMercadoria localizacaoMercadoria) {
		
		getVolumeNotaFiscalDAO().updateLocalizacaoMercadoriaByNotaFiscalConhecimento(notaFiscalConhecimento,localizacaoMercadoria);
	}

    public String buscarParametroNovaEtiqueta(Long idFilial) {
        String etiqueta = null;
        try {
            etiqueta = (String)configuracoesFacade.getValorParametro(idFilial, NOVA_ETIQUETA);
        } catch (BusinessException e) {
            String mensagem = String.format("Parametro '%s' não encontrado para a filial id '%s'", NOVA_ETIQUETA, idFilial);
            log.info(mensagem);
        }
        return etiqueta;
    }

    private Boolean isDanfeSimplificada(String idColeta){
        return clienteService.validateIsDanfeSimplificadaByIdColeta(idColeta);
    }

    public ClienteService getClienteService() {
        return clienteService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }
}

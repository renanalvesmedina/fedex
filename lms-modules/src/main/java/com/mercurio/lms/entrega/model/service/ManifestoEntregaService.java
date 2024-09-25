package com.mercurio.lms.entrega.model.service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.contasreceber.model.service.GerarBoletoReciboManifestoService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.portaria.model.service.InformarSaidaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.dao.LMManifestoDAO;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DocumentoCliente;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.entrega.manifestoEntregaService"
 */
public class ManifestoEntregaService extends CrudService<ManifestoEntrega, Long> {

    public static final String CONS_CLIENTE_RETIRA = "CR";
    public static final String CONS_EMPRESA_PARCEIRA = "EP";

    public static final String PERGUNTA_CAIXA_LACRADA = "caixa lacrada?";
    private static final String GRAU_DE_RISCO_EXIBICAO_QUIZ = "3";

    private ConfiguracoesFacade configuracoesFacade;
    private ConversaoMoedaService conversaoMoedaService;
    private TipoDocumentoEntregaService tipoDocumentoEntregaService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private LMManifestoDAO lmManifestoDao;
    private DomainValueService domainValueService;
    private GerarBoletoReciboManifestoService gerarBoletoReciboManifestoService;
    private DoctoServicoService doctoServicoService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private DadosComplementoService dadosComplementoService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private ManifestoService manifestoService;
    private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
    private EventoService eventoService;
    private EventoVolumeService eventoVolumeService;
    private DevedorDocServService devedorDocServService;
    private PedidoColetaService pedidoColetaService;
    private CarregamentoMobileService carregamentoMobileService;
    private ControleCargaService controleCargaService;
    private InformarSaidaService informarSaidaService;
    private ManifestoEntregaService manifestoEntregaService;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private GrauRiscoPerguntaRespostaService grauRiscoPerguntaRespostaService;
    private NotaFiscalOperadaService notaFiscalOperadaService;

    /**
     * Recupera uma instância de <code>ManifestoEntrega</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public ManifestoEntrega findById(java.lang.Long id) {
        return (ManifestoEntrega) super.findById(id);
    }

    public List findByNrManifestoByFilial(Integer nrManifesto, Long idFilial) {
        return getManifestoEntregaDAO().findByNrManifestoByFilial(nrManifesto, idFilial);
    }

    public List findDocumentosServico(Long idManifestoEntrega) {
        return getManifestoEntregaDAO().findDocumentosServico(idManifestoEntrega);
    }

    /**
     * Consulta dados relativos a solicitacao de retirada do manifesto
     *
     * @param idSolicitacaoRetirada
     * @return
     */
    public List findManifestoByIdSolicitacaoRetirada(Long idSolicitacaoRetirada) {
        return getManifestoEntregaDAO().findManifestoByIdSolicitacaoRetirada(idSolicitacaoRetirada);
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
     *
     *
     */
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
     * contrário.
     *
     * @param bean entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(ManifestoEntrega bean) {
        return super.store(bean);
    }

    /**
     * Verifica se o documento de servico esta associada a algum manifesto de
     * entrega nao finalizado
     *
     * @param idDoctoServico
     * @return
     */
    public boolean validateDoctoServicoNaoFinalizado(Long idDoctoServico) {
        return getManifestoEntregaDAO().validateDoctoServicoNaoFinalizado(idDoctoServico);
    }

    /**
     * RowCount da grid da tela Reemitir/Cancelar Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    public Integer getRowCountGridCancelarManifesto(TypedFlatMap parametros) {
        return getManifestoEntregaDAO().getRowCountGridCancelarManifesto(parametros);
    }

    /**
     * Consulta utilizada na grid da tela Reemitir/Cancelar Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    public ResultSetPage findPaginatedGridCancelarManifesto(TypedFlatMap parametros) {
        ResultSetPage rsp = getManifestoEntregaDAO().findPaginatedGridCancelarManifesto(parametros, FindDefinition.createFindDefinition(parametros));

        FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
            public Map filterItem(Object item) {
                Object[] obj = (Object[]) item;
                Map map = new HashMap();
                map.put("idManifestoEntrega", obj[0]);
                map.put("sgFilial", obj[1]);
                map.put("nrManifestoEntrega", obj[2]);
                map.put("dhEmissaoManifesto", obj[3]);
                map.put("sgFilialCC", obj[4]);
                map.put("nrControleCarga", obj[5]);
                map.put("nrFrota", obj[6]);
                map.put("nrIdentificador", obj[7]);
                map.put("nrRota", obj[8]);
                map.put("dsRota", obj[9]);
                map.put("quantidadeEntregas", obj[10]);
                map.put("tpManifestoEntrega", obj[11]);
                map.put("tpStatusManifesto", obj[12]);
                return map;
            }
        };
        return (ResultSetPage) frsp.doFilter();
    }

    public boolean validateEmissaoNotaCreditoParaControleCarga(Long idControleCarga) {
        List<ManifestoEntrega> manifestos = findManifestoEntregaAtivoByControleCarga(idControleCarga);

        if (CollectionUtils.isEmpty(manifestos)) {
            return false;
        }

        for (ManifestoEntrega manifesto : manifestos) {
            boolean isValid = manifestoService.validateManifestoFechado(manifesto.getManifesto()) && !manifestoService.validateManifestoParceira(manifesto.getManifesto());

            if (!isValid) {
                return false;
            }
        }

        return true;
    }

    public boolean validateManifestoEntregaParceira(List<ManifestoEntrega> manifestosEntrega) {
        return hasManifestoEntregaAbertoOrParceira(manifestosEntrega, true);
    }

    public boolean validateManifestoEntregaParceira(ControleCarga controleCarga) {
        return validateManifestoEntregaParceira(findManifestoEntregaAtivoByControleCarga(controleCarga));
    }

    public boolean isManifestoEntregaParceira(ControleCarga controleCarga) {
        List<ManifestoEntrega> manifestos = findManifestoEntregaAtivoByControleCarga(controleCarga);

        final DomainValue dmConsEmpresaParceira = new DomainValue(CONS_EMPRESA_PARCEIRA);

        for (ManifestoEntrega manifestoEntrega : manifestos) {
            if (dmConsEmpresaParceira.equals(manifestoEntrega.getManifesto().getTpManifestoEntrega())) {
                return true;
            }
        }

        return false;
    }

    public boolean validateManifestoEntregaAberto(List<ManifestoEntrega> manifestosEntrega) {
        return hasManifestoEntregaAbertoOrParceira(manifestosEntrega, false);
    }

    private boolean hasManifestoEntregaAbertoOrParceira(List<ManifestoEntrega> manifestos, boolean isParceira) {
        if (manifestos == null) {
            return false;
        }

        for (ManifestoEntrega manifesto : manifestos) {
            if ((isParceira && manifestoService.validateManifestoParceira(manifesto.getManifesto())) | manifestoService.validateManifestoAberto(manifesto.getManifesto())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste
     * serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setManifestoEntregaDAO(ManifestoEntregaDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência
     * dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public ManifestoEntregaDAO getManifestoEntregaDAO() {
        return (ManifestoEntregaDAO) getDao();
    }

    /**
     * Retorna uma list de registros de Manifesto de Entrega com o ID do
     * Manifesto
     *
     * @param idManifesto
     * @return
     */
    public List findManifestoEntregaByIdManifesto(Long idManifesto) {
        return this.getManifestoEntregaDAO().findManifestoEntregaByIdManifesto(idManifesto);
    }

    /**
     * Consulta utilizada na lookup de manifesto de entrega.
     *
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedLookup(TypedFlatMap criteria) {
        ResultSetPage rsp = getManifestoEntregaDAO().findPaginatedGridLookup(criteria, FindDefinition.createFindDefinition(criteria));

        FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
            public Map filterItem(Object item) {
                Map map = (Map) item;
                TypedFlatMap retorno = new TypedFlatMap();
                Set keySet = map.keySet();
                Iterator it = keySet.iterator();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    retorno.put(key.replace("_", "."), map.get(key));
                }
                return retorno;
            }
        };

        return (ResultSetPage) frsp.doFilter();
    }

    /**
     * Contagem utilizada na paginação da grid da lookup de manifesto de de
     * entrega.
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountLookup(TypedFlatMap criteria) {
        return getManifestoEntregaDAO().getRowCountLookup(criteria);
    }

    /**
     * Consulta da lookup de manifesto de entrega.
     *
     * @param criteria
     * @return
     */
    public List findLookupCustom(TypedFlatMap criteria) {
        List l = getManifestoEntregaDAO().findLookupCustom(criteria);
        List newList = new ArrayList(2);

        Iterator itList = l.iterator();
        while (itList.hasNext()) {
            Map map = (Map) itList.next();
            TypedFlatMap retorno = new TypedFlatMap();
            Set keySet = map.keySet();
            Iterator it = keySet.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                retorno.put(key.replace("_", "."), map.get(key));
            }
            newList.add(retorno);
        }

        return newList;
    }

    /**
     * Consulta utilizada na lookup de manifesto de entrega.
     *
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedPreManifesto(TypedFlatMap criteria) {
        ResultSetPage rsp = getManifestoEntregaDAO().findPaginatedPreManifesto(criteria, FindDefinition.createFindDefinition(criteria));

        FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
            public Map filterItem(Object item) {
                Object[] obj = (Object[]) item;
                Map map = new HashMap();
                map.put("idManifesto", obj[0]);
                map.put("sgFilialManifesto", obj[1]);
                map.put("nrPreManifesto", obj[2]);
                map.put("nrRota", obj[3]);
                map.put("dsRota", obj[4]);
                map.put("qtEntregas", obj[5]);
                map.put("sgFilialSolicitacao", obj[6]);
                map.put("nrSolicitacao", obj[7]);
                map.put("tpManifesto", obj[8]);
                map.put("tpStatusManifesto", obj[9]);
                map.put("nrManifestoEntrega", obj[10]);
                map.put("sgFilialManifestoEntrega", obj[11]);
                map.put("dhEmissao", obj[12]);
                return map;
            }
        };

        return (ResultSetPage) frsp.doFilter();
    }

    /**
     * Contagem utilizada na paginação da grid da Emitir Manifesto.
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountPreManifesto(TypedFlatMap criteria) {
        return getManifestoEntregaDAO().getRowCountPreManifesto(criteria);
    }

    /**
     * Consulta manifesto a partir de um id recebido. Implementado para otimizar
     * alguns joins.
     *
     * @param id
     * @return
     */
    public Manifesto findByIdEmitirManifesto(Long id) {
        return getManifestoEntregaDAO().findByIdEmitirManifesto(id);
    }

    /**
     * Processo de validação da emissão de manifestos, e criação de
     * dependências.
     *
     * @param parameters
     * @return TypedFlatMap com flags para controle dos processos realizados com
     * sucesso: blEmitirBoleto blEmitirRecibo entidade do Manifesto de Entrega
     * gerado: manifestoEntrega
     */
    public TypedFlatMap storeValidateEmissaoAdaptor(TypedFlatMap parameters) {
        TypedFlatMap map = this.storeValidateEmissaoManifesto(parameters);

        ManifestoEntrega me = (ManifestoEntrega) map.get("manifestoEntrega");
        Manifesto manif = me.getManifesto();
        if (manif.getDhEmissaoManifesto() == null) {
            manif = this.storeValidateManifestoEmitido(me.getIdManifestoEntrega());
        }

        return map;
    }

    public TypedFlatMap storeValidateEmissaoManifesto(TypedFlatMap parameters) {
        // map com dados que retornarão para a tela.

        manifestoService.flushModeParaCommit();

        TypedFlatMap retorno = new TypedFlatMap();

        String tpIdentificacao = parameters.getString("pessoa.tpIdentificacao");
        Long nrCnpj = parameters.getLong("pessoa.nrIdentificacao");
        String nmRetirante = parameters.getString("pessoa.nmPessoa");
        String nrRg = parameters.getString("pessoa.nrRg");
        String nrPlaca = parameters.getString("meioTransporte.nrPlaca");
        String nrTelefone = parameters.getString("nrTelefone");
        //A Integração deverá passar false para "geraFaturamentoManifestoEntrega"
        Boolean geraFaturamentoManifestoEntrega = Boolean.TRUE;
        if (parameters.getBoolean("geraFaturamentoManifestoEntrega") != null) {
            geraFaturamentoManifestoEntrega = parameters.getBoolean("geraFaturamentoManifestoEntrega");
        }

        Long idManifesto = parameters.getLong("idManifesto");
        Manifesto manifesto = manifestoService.findById(idManifesto);
        DomainValue dvTpManifestoEntrega = manifesto.getTpManifestoEntrega();
        String tpManifestoEntrega = "";
        if (dvTpManifestoEntrega != null) {
            tpManifestoEntrega = dvTpManifestoEntrega.getValue();
            if (CONS_CLIENTE_RETIRA.equals(tpManifestoEntrega)) {
                if ((tpIdentificacao == null)
                        || (nrCnpj == null)
                        || (nmRetirante == null)
                        || (nrRg == null)
                        || (nrPlaca == null)
                        || (nrTelefone == null)) {
                    throw new BusinessException("LMS-09030");
                }

                SolicitacaoRetirada solicitacaoRetirada = manifesto.getSolicitacaoRetirada();
                solicitacaoRetirada = storeEmitirManifestoValidaSolicitacaoRetirada(parameters, tpIdentificacao, nrCnpj, nmRetirante,
                        nrRg, nrPlaca, nrTelefone, manifesto, tpManifestoEntrega, solicitacaoRetirada);
                manifesto.setSolicitacaoRetirada(solicitacaoRetirada);
                getManifestoEntregaDAO().store(manifesto);

                retorno.put("solicitacaoRetirada.idSolicitacaoRetirada", solicitacaoRetirada.getIdSolicitacaoRetirada());
                retorno.put("solicitacaoRetirada.nrSolicitacaoRetirada", solicitacaoRetirada.getNrSolicitacaoRetirada());

                // A filial de retirada será a filial de origem do manifesto.
                Filial filialRetirada = manifesto.getFilialByIdFilialOrigem();
                retorno.put("solicitacaoRetirada.filial.idFilial", filialRetirada.getIdFilial());
                retorno.put("solicitacaoRetirada.filial.sgFilial", filialRetirada.getSgFilial());
                retorno.put("solicitacaoRetirada.filial.nmFilial", filialRetirada.getPessoa().getNmFantasia());
            }
        }

        ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
        manifestoEntrega = storeEmitirManifestoValidaManifestoEntrega(parameters, manifesto, manifestoEntrega, geraFaturamentoManifestoEntrega);

        Long idManifestoEntrega = manifestoEntrega.getIdManifestoEntrega();

        List<ManifestoEntregaDocumento> manifestoEntregaDocumentos = manifestoEntrega.getManifestoEntregaDocumentos();
        List<DoctoServico> documentosManifesto = new ArrayList<DoctoServico>();
        for (ManifestoEntregaDocumento manifestoEntregaDocumento : manifestoEntregaDocumentos) {
            documentosManifesto.add(manifestoEntregaDocumento.getDoctoServico());
        }

        agendamentoDoctoServicoService.fechaAgendamentosDoctoServico(documentosManifesto);

        boolean blEmitirBoleto = this.validateIfExistsDocumentoCobranca(idManifestoEntrega, "B");

        retorno.put("blEmitirBoleto", blEmitirBoleto);

        retorno.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
        retorno.put("manifestoEntrega.filial.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
        retorno.put("manifestoEntrega.nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
        retorno.put("manifestoEntrega", manifestoEntrega);

        manifestoService.flushModeParaAutoComFlushEClear();

        return retorno;
    }

    private SolicitacaoRetirada storeEmitirManifestoValidaSolicitacaoRetirada(
            TypedFlatMap parameters,
            String tpIdentificacao,
            Long nrCnpj,
            String nmRetirante,
            String nrRg,
            String nrPlaca,
            String nrTelefone,
            Manifesto manifesto,
            String tpManifestoEntrega,
            SolicitacaoRetirada solicitacaoRetirada
    ) {
        solicitacaoRetirada.setTpSituacao(new DomainValue("R"));
        solicitacaoRetirada.setTpRegistroLiberacao(new DomainValue("G"));
        solicitacaoRetirada.setUsuarioAutorizacao(SessionUtils.getUsuarioLogado());
        solicitacaoRetirada.setNmRetirante(nmRetirante);
        solicitacaoRetirada.setTpIdentificacao(new DomainValue(tpIdentificacao));
        solicitacaoRetirada.setNrCnpj(nrCnpj);
        solicitacaoRetirada.setNrDdd(parameters.getString("nrDdd"));
        solicitacaoRetirada.setNrTelefone(nrTelefone);
        solicitacaoRetirada.setNrRg(nrRg);
        solicitacaoRetirada.setNrPlaca(nrPlaca);
        solicitacaoRetirada.setNrPlacaSemiReboque(parameters.getString("semiReboque.nrPlaca"));

        getManifestoEntregaDAO().store(solicitacaoRetirada);
        return solicitacaoRetirada;
    }

    /**
     *
     * @param parameters
     * @param manifesto
     * @param manifestoEntrega
     * @param geraFaturamentoManifestoEntrega Parâmetro necessário para a
     * Integração, pois nao necessita que a geração seja executada.
     * @return
     */
    public ManifestoEntrega storeEmitirManifestoValidaManifestoEntrega(TypedFlatMap parameters, Manifesto manifesto, ManifestoEntrega manifestoEntrega, boolean geraFaturamentoManifestoEntrega) {
        if (manifestoEntrega == null) {
            manifestoEntrega = new ManifestoEntrega();
            manifestoEntrega.setManifesto(manifesto);
            manifesto.setManifestoEntrega(manifestoEntrega);

            manifestoEntrega.setFilial(manifesto.getFilialByIdFilialOrigem());
            manifestoEntrega.setSetor(SessionUtils.getSetor());

            Integer nrManifestoEntrega = Integer.valueOf(configuracoesFacade.incrementaParametroSequencial(
                    manifestoEntrega.getFilial().getIdFilial(), "NR_MANIFESTO_ENTREGA", true).intValue());

            manifestoEntrega.setNrManifestoEntrega(nrManifestoEntrega);

            manifestoEntrega.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());

            manifestoEntrega.setObManifestoEntrega(parameters.getString("obManifestoEntrega"));
            super.store(manifestoEntrega);

            manifestoEntrega.setManifestoEntregaDocumentos(new ArrayList());
            manifestoEntrega.setManifestoEntregaVolumes(new ArrayList<ManifestoEntregaVolume>());

            List preManifestoDocumentos = manifesto.getPreManifestoDocumentos();
            Iterator itPreManifestoDocumentos = preManifestoDocumentos.iterator();

            while (itPreManifestoDocumentos.hasNext()) {
                PreManifestoDocumento pmd = (PreManifestoDocumento) itPreManifestoDocumentos.next();
                this.storeManifestoEntregaDocVolume(pmd, manifesto, geraFaturamentoManifestoEntrega);
            }

            List<PreManifestoVolume> listaVolumesSemDocto = preManifestoVolumeService.findVolumesSemPreManifDoctoByManifesto(manifesto.getIdManifesto());
            for (PreManifestoVolume preManifestoVolume : listaVolumesSemDocto) {
                ManifestoEntregaVolume mev = new ManifestoEntregaVolume();
                mev.setManifestoEntrega(manifestoEntrega);
                mev.setVolumeNotaFiscal(preManifestoVolume.getVolumeNotaFiscal());
                mev.setDoctoServico(preManifestoVolume.getDoctoServico());
                manifestoEntrega.getManifestoEntregaVolumes().add(mev);
            }

            getManifestoEntregaDAO().store(manifesto);

        } else {
            manifestoEntrega.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
            super.store(manifestoEntrega);
        }

        return manifestoEntrega;
    }

    public ManifestoEntregaDocumento storeManifestoEntregaDocVolume(PreManifestoDocumento pmd, Manifesto manifesto, Boolean geraFaturamentoManifestoEntrega) {
        DoctoServico doctoServico = pmd.getDoctoServico();

        ManifestoEntregaDocumento med = new ManifestoEntregaDocumento();
        med.setDoctoServico(doctoServico);
        med.setManifestoEntrega(manifesto.getManifestoEntrega());
        med.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

        String tpSituacaoDocumento = "PBAI";
        DoctoServico documento = doctoServicoService.findById(doctoServico.getIdDoctoServico(), false);

        /* Valida se o devedor do Documento de Serviço não possui
		indicador de retenção do comprovante de entrega. Se retornar algum devedor,
		atualizamos o camop blRetencaoComprovanteEnt para falso.
         */
        Integer nrDevedores = manifestoEntregaDocumentoService.getRowCountDevedorSemRetencaoComprovante(doctoServico.getIdDoctoServico());
        if (nrDevedores.compareTo(Integer.valueOf(0)) != 0) {
            med.setBlRetencaoComprovanteEnt(Boolean.FALSE);
        }

        med.setAwb(pmd.getAwb());

        med.setTpSituacaoDocumento(new DomainValue(tpSituacaoDocumento));
        manifesto.getManifestoEntrega().getManifestoEntregaDocumentos().add(med);

        List<PreManifestoVolume> listaVolumesDocto = preManifestoVolumeService.findByManifestoDoctoServico(manifesto.getManifestoEntrega().getIdManifestoEntrega(), doctoServico.getIdDoctoServico());
        for (PreManifestoVolume preManifestoVolume : listaVolumesDocto) {
        	ManifestoEntregaVolume mev = new ManifestoEntregaVolume();
            mev.setManifestoEntrega(manifesto.getManifestoEntrega());
            mev.setVolumeNotaFiscal(preManifestoVolume.getVolumeNotaFiscal());
            mev.setDoctoServico(doctoServico);
            mev.setManifestoEntregaDocumento(med);
            manifesto.getManifestoEntrega().getManifestoEntregaVolumes().add(mev);
        }

        if (geraFaturamentoManifestoEntrega
                && Hibernate.getClass(documento).equals(Conhecimento.class)
                && med.getAwb() == null) {
            gerarBoletoReciboManifestoService
                    .generateFaturamentoManifestoEntrega(
                            manifesto, med,
                            (Conhecimento) documento);
        }

        if (Hibernate.getClass(documento).equals(Conhecimento.class)
                || Hibernate.getClass(documento).equals(CtoInternacional.class)
                || Hibernate.getClass(documento).equals(NotaFiscalConhecimento.class)) {
            manifesto.setVlTotalManifestoEmissao(BigDecimalUtils.add(manifesto.getVlTotalManifestoEmissao(), documento.getVlMercadoria()));
            manifesto.setPsTotalManifestoEmissao(BigDecimalUtils.add(manifesto.getPsTotalManifestoEmissao(), documento.getPsReal()));
            manifesto.setQtTotalVolumesEmissao(IntegerUtils.addNull(manifesto.getQtTotalVolumesEmissao(), documento.getQtVolumes()));
            manifesto.setVlTotalFreteEmissao(BigDecimalUtils.add(manifesto.getVlTotalFreteEmissao(), documento.getVlTotalDocServico()));
        }

        Long idManifestoEntrega = manifesto.getManifestoEntrega().getIdManifestoEntrega();

        storeUpdateDocumentosManifesto(idManifestoEntrega, "BL", "B");

        return med;
    }

    public boolean validateIfIsEntregaRealizada(ManifestoEntrega manifesto) {
        if (manifesto == null || manifesto.getManifestoEntregaDocumentos() == null) {
            return false;
        }

        for (ManifestoEntregaDocumento documento : manifesto.getManifestoEntregaDocumentos()) {
            if (documento.getOcorrenciaEntrega() != null && eventoService.validateIfIsEventoEntregaRealizada(documento.getOcorrenciaEntrega().getEvento())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Valida se existe documento de cobrança do tipo informado.
     *
     * @param idManifestoEntrega
     * @param tpDocumentoCobranca
     * @return boolean true se existe documentos de cobrança.
     */
    public boolean validateIfExistsDocumentoCobranca(Long idManifestoEntrega, String tpDocumentoCobranca) {
        return getManifestoEntregaDAO().validateIfExistsDocumentoCobranca(idManifestoEntrega, tpDocumentoCobranca);
    }

    public Manifesto storeValidateManifestoEmitido(Long idManifestoEntrega) {
        return this.storeValidateManifestoEmitido(idManifestoEntrega, ConstantesExpedicao.TP_STATUS_MANIFESTO_EMITIDO);
    }

    /**
     * Validações realizadas após a emissão de um manifesto.
     *
     * @param idManifestoEntrega
     * @return entidade do Manifesto emitido.
     */
    public Manifesto storeValidateManifestoEmitido(Long idManifestoEntrega, String tpStatusManifesto) {
        manifestoService.flushModeParaCommit();

        Manifesto manifesto = (Manifesto) getManifestoEntregaDAO().getAdsmHibernateTemplate()
                .get(Manifesto.class, idManifestoEntrega);
        manifesto.setDhEmissaoManifesto(JTDateTimeUtils.getDataHoraAtual());
        DomainValue dvTpStatusManifesto = domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO", tpStatusManifesto);
        manifesto.setTpStatusManifesto(dvTpStatusManifesto);
        getManifestoEntregaDAO().store(manifesto);

        EventoManifesto eventoManifesto = new EventoManifesto();
        eventoManifesto.setManifesto(manifesto);
        Filial filialOrigemManifesto = manifesto.getFilialByIdFilialOrigem();
        eventoManifesto.setFilial(filialOrigemManifesto);
        eventoManifesto.setUsuario(SessionUtils.getUsuarioLogado());
        eventoManifesto.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
        eventoManifesto.setTpEventoManifesto(new DomainValue("EM"));
        getManifestoEntregaDAO().store(eventoManifesto);

        ManifestoEntrega me = (ManifestoEntrega) getManifestoEntregaDAO().getAdsmHibernateTemplate()
                .get(ManifestoEntrega.class, idManifestoEntrega);
        List documentos = me.getManifestoEntregaDocumentos();
        Iterator iDocumentos = documentos.iterator();
        DomainValue tpManifestoEntrega = manifesto.getTpManifestoEntrega();
        String dsObervacaoEvento = null;
        Short constanteEventoRastreabilidade = ConstantesSim.EVENTO_MANIFESTO_EMITIDO;
        if (tpManifestoEntrega != null) {
            String tpManifestoEntregaValue = tpManifestoEntrega.getValue();
            if (tpManifestoEntregaValue.equals(CONS_CLIENTE_RETIRA)) {
                constanteEventoRastreabilidade = ConstantesSim.EVENTO_MERCADORIA_SENDO_ENTREGUE;
                dsObervacaoEvento = "Cliente Retira";
            } else if (CONS_EMPRESA_PARCEIRA.equals(tpManifestoEntregaValue)) {
                constanteEventoRastreabilidade = ConstantesSim.EVENTO_MANIFESTO_EMITIDO_PARCEIRA;
            }
        }
        while (iDocumentos.hasNext()) {
            ManifestoEntregaDocumento med = (ManifestoEntregaDocumento) iDocumentos.next();
            this.generateEventoDocto(med, manifesto, me, constanteEventoRastreabilidade, dsObervacaoEvento);
        }
        eventoVolumeService.generateEventoEmissaoManifestoByManifesto(filialOrigemManifesto.getIdFilial(), manifesto);

        manifestoService.flush();
        manifestoService.flushModeParaAuto();

        return manifesto;
    }

    public void generateEventoDocto(ManifestoEntregaDocumento med, Manifesto manifesto, ManifestoEntrega me, Short constanteEventoRastreabilidade, String dsObervacaoEvento) {
        DoctoServico doctoServico = med.getDoctoServico();

        //O número deve ser inserido formatado e com a sigla da filial origem:
        String numero = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " "
                + FormatUtils.formatIntegerWithZeros(me.getNrManifestoEntrega(), "00000000");

        this.incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                constanteEventoRastreabilidade,
                doctoServico.getIdDoctoServico(),
                manifesto.getFilialByIdFilialOrigem().getIdFilial(),
                numero,
                JTDateTimeUtils.getDataHoraAtual(),
                null, dsObervacaoEvento,
                ConstantesEntrega.MANIFESTO_ENTREGA);

        // Encontra se há comprovantes para o documento de serviço do manifesto atual:
        List comprovantes = getManifestoEntregaDAO().findComprovantesDevedorOriginal(doctoServico.getIdDoctoServico(), false);
        if (!comprovantes.isEmpty()) {
            // Se existir um comprovantes pelo menos, é alterada a situação do documento:
            String tpSituacaoAtual = med.getTpSituacaoDocumento().getValue();
            if ("PBAI".equals(tpSituacaoAtual)) {
                med.setTpSituacaoDocumento(new DomainValue("PBCO"));
            }
            getManifestoEntregaDAO().store(med);
        }
        // Encontra se há comprovantes para o documento de serviço do manifesto atual sem registro de entrega:
        comprovantes = getManifestoEntregaDAO().findComprovantesDevedorOriginal(doctoServico.getIdDoctoServico(), true);
        Iterator iComprovantes = comprovantes.iterator();
        while (iComprovantes.hasNext()) {
            DocumentoCliente dc = (DocumentoCliente) iComprovantes.next();
            RegistroDocumentoEntrega rde = new RegistroDocumentoEntrega();
            rde.setBlComprovanteRecolhido(Boolean.FALSE);
            rde.setUsuario(SessionUtils.getUsuarioLogado());
            rde.setTipoDocumentoEntrega(dc.getTipoDocumentoEntrega());
            rde.setDoctoServico(doctoServico);
            rde.setTpSituacaoRegistro(new DomainValue(CONS_CLIENTE_RETIRA));
            rde.setObComprovante(null);
            getManifestoEntregaDAO().store(rde);
        }
    }

    private boolean storeUpdateDocumentosManifesto(Long idManifestoEntrega, String tpSituacaoFatura, String tpDocumentoCobranca) {
        getDao().flush();
        List documentos = this.findManifestoEntregaDocumentosByEntrega(idManifestoEntrega, tpSituacaoFatura);

        if (CollectionUtils.isEmpty(documentos)) {
            return false;
        }

        boolean blIndicador = true;
        TipoDocumentoEntrega tipoDocumentoBoleto = this.findTipoDocumentoEntrega(tpDocumentoCobranca);
        Usuario usuarioSessao = SessionUtils.getUsuarioLogado();

        Iterator itDocumentos = documentos.iterator();

        while (itDocumentos.hasNext()) {
            ManifestoEntregaDocumento med = (ManifestoEntregaDocumento) itDocumentos.next();
            med.setTpDocumentoCobranca(new DomainValue(tpDocumentoCobranca));

            if ("PBCO".equals(med.getTpSituacaoDocumento().getValue())) {
                med.setTpSituacaoDocumento(new DomainValue("PBAI"));
            }

            if (tipoDocumentoBoleto != null) {
                DoctoServico doctoServico = med.getDoctoServico();
                if (!this.validateIfExistsDoctoServicoInRegistroDocumentoEntrega(
                        doctoServico.getIdDoctoServico(), tipoDocumentoBoleto.getIdTipoDocumentoEntrega())) {
                    RegistroDocumentoEntrega rdeNew = new RegistroDocumentoEntrega();
                    rdeNew.setBlComprovanteRecolhido(Boolean.FALSE);
                    rdeNew.setUsuario(usuarioSessao);
                    rdeNew.setTipoDocumentoEntrega(tipoDocumentoBoleto);
                    rdeNew.setDoctoServico(doctoServico);
                    rdeNew.setTpSituacaoRegistro(new DomainValue(CONS_CLIENTE_RETIRA));

                    getManifestoEntregaDAO().store(rdeNew);
                }
            }

            getManifestoEntregaDAO().store(med);
        }

        return blIndicador;
    }

    private TipoDocumentoEntrega findTipoDocumentoEntrega(String tpDocumentoCobranca) {
        Map crit1 = new HashMap();
        crit1.put("tpDocumentoCobranca", tpDocumentoCobranca);
        List tiposDocumentoEntrega = tipoDocumentoEntregaService.find(crit1);

        if (CollectionUtils.isEmpty(tiposDocumentoEntrega)) {
            return null;
        }

        return (TipoDocumentoEntrega) tiposDocumentoEntrega.get(0);
    }

    private List findManifestoEntregaDocumentosByEntrega(Long idManifestoEntrega, String tpSituacaoFatura) {
        return getManifestoEntregaDAO().findManifestoEntregaDocumentosByEntrega(idManifestoEntrega, tpSituacaoFatura);
    }

    private boolean validateIfExistsDoctoServicoInRegistroDocumentoEntrega(Long idDoctoServico, Long idTipoDocumentoEntrega) {
        return getManifestoEntregaDAO().validateIfExistsDoctoServicoInRegistroDocumentoEntrega(idDoctoServico, idTipoDocumentoEntrega);
    }

    private List<ManifestoEntrega> findManifestoEntregaAtivoByControleCarga(ControleCarga controleCarga) {
        if (controleCarga == null) {
            return new ArrayList<ManifestoEntrega>();
        }

        return findManifestoEntregaAtivoByControleCarga(controleCarga.getIdControleCarga());
    }

    public List<ManifestoEntrega> findManifestoEntregaAtivoByControleCarga(Long idControleCarga) {
        return getManifestoEntregaDAO().findManifestoEntregaAtivoByControleCarga(idControleCarga);
    }

    public ResultSetPage findPaginatedManifestoEntregaByIdDoctoServico(Long idDoctoServico) {
        return lmManifestoDao.findPaginatedManifestoEntrega(idDoctoServico);
    }

    public ResultSetPage findPaginatedManifestoEntregaNotasFiscaisByIdDoctoServico(Long idDoctoServico) {
    	ResultSetPage result = lmManifestoDao.findPaginatedManifestoEntregaNotasFiscaisByIdDoctoServico(idDoctoServico);
    	List<TypedFlatMap> newList = null;
    	
		if (!result.getList().isEmpty()) {
			newList = new ArrayList<TypedFlatMap>();
			
			for (Iterator i = result.getList().iterator(); i.hasNext();) {
				TypedFlatMap map = new TypedFlatMap();
				Object[] obj = (Object[]) i.next();

				YearMonthDay dtEmissao = (YearMonthDay) obj[3];
				map.put("dtEmissao", JTFormatUtils.format(dtEmissao));
				map.put("sgFilial", obj[0]);
				map.put("nrNotaFiscal", obj[1]);
				map.put("dsSerie", obj[2]);
				map.put("dsOcorrenciaEntrega", obj[4]);
				map.put("nrManifesto", obj[5]);
				map.put("qtVolumesEntregues", obj[6]);
				
				if (!map.isEmpty()) {
					newList.add(map);
				}
			}
		}
		
		result.setList(newList);
		return result;
    }

    /**
     * Busca lista de entregas para o projeto VOL
     *
     * @param idControleCarga
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, Object> findEntregasToMobile(Long idControleCarga) {
        Map<String, Object> retorno = new HashMap<String, Object>();

        List<Map<String, Object>> entregasToMobile = this.getManifestoEntregaDAO().findEntregasToMobile(idControleCarga, this.getIdsQuiz());
        List<Map<String, Object>> perguntasParentesco = this.getPerguntasGrauParentesco();
        List<Map<String, Object>> perguntasRisco = this.getPerguntasRisco();

        this.ajustarEntregas(entregasToMobile, perguntasParentesco, perguntasRisco);

        retorno.put("list", entregasToMobile);

        return retorno;
    }

    private void ajustarEntregas(List<Map<String, Object>> entregasToMobile, List<Map<String, Object>> perguntasParentesco, List<Map<String, Object>> perguntasRisco) {
        for (Map<String, Object> entrega : entregasToMobile) {
            DoctoServico doc = new DoctoServico();
            doc.setIdDoctoServico((Long) entrega.get("DOCT"));

            DadosComplemento complemento = this.dadosComplementoService.findDadosComplementoToMobile(doc, ConstantesExpedicao.CNPJ_NATURA, new String[] { ConstantesExpedicao.COD_CONF_EDI, ConstantesExpedicao.COD_CONF_EDI_COM_ACENTO });

            this.popularGrauDeRisco(entrega, complemento);
            this.ajustarValoresFlag(entrega, complemento, perguntasParentesco, perguntasRisco, doc.getIdDoctoServico());
        }
    }

    private void popularGrauDeRisco(Map<String, Object> entrega, DadosComplemento complemento) {
        if (complemento == null) {
            entrega.put("GRAU", "0");
        } else {
            entrega.put("GRAU", complemento.getDsValorCampo());
        }
    }

    private void ajustarValoresFlag(Map<String, Object> entrega, DadosComplemento complemento, List<Map<String, Object>> perguntasParentesco, List<Map<String, Object>> perguntasRisco, Long idDoctoServico) {
        if (StringUtils.isBlank((String) entrega.get("FLAG"))) {
            return;
        }

        this.ajustarFlagObrigaRecebedor(entrega, idDoctoServico);
        this.popularVolumes(entrega, idDoctoServico);
        this.popularQuiz(entrega, complemento, perguntasParentesco, perguntasRisco);
        this.removerCaracteresEspeciaisDestinatario(entrega, idDoctoServico);

        entrega.remove("ID_REMETENTE");
    }

    private void removerCaracteresEspeciaisDestinatario(Map<String, Object> entrega, Long idDoctoServico) {
    	String flagObrigaRecebedor = (String) entrega.get("FLAG");
        if (flagObrigaRecebedor.startsWith("S") || isClienteDevedorNatura(idDoctoServico)) {
            entrega.put("DEST", removerCaracteresEspeciais((String) entrega.get("DEST")));
        }
	}

    private boolean isClienteDevedorNatura(Long idDoctoServico) {
        Cliente cliente = this.devedorDocServService.findByIdDoctoServico(idDoctoServico);

        if (cliente != null && cliente.getIdCliente() != null) {
            return this.isClienteNatura(cliente.getIdCliente());
        }

        return true;
    }
    
    private boolean isClienteNatura(Long idCliente) {
        if (idCliente == null) {
            return false;
        }
        List<Long> idsNatura = this.getIdsQuiz();
        if (idsNatura != null && !idsNatura.isEmpty()) {
            return idsNatura.contains(idCliente);
        }

        return false;
    }
    
	public String removerCaracteresEspeciais(String destinatario) {
		destinatario = Normalizer.normalize(destinatario, Normalizer.Form.NFD);
		destinatario = destinatario.replaceAll("[^\\p{Alnum}\\s^\\p{IsDigit}]", "");
	    return destinatario;
    }
	
    private void ajustarFlagObrigaRecebedor(Map<String, Object> entrega, Long idDoctoServico) {
        String flagObrigaRecebedor = (String) entrega.get("FLAG");

        if (!flagObrigaRecebedor.startsWith("N")) {
            return;
        }

        boolean isClienteDestinatarioObrigaRecebedor = this.isClienteDestinatarioObrigaRecebedor(entrega);
        boolean isClienteDevedorObrigaRecebedor = this.isClienteDevedorObrigaRecebedor(isClienteDestinatarioObrigaRecebedor, idDoctoServico);

        if (isClienteDestinatarioObrigaRecebedor || isClienteDevedorObrigaRecebedor) {
            entrega.remove("ID_DESTINATARIO");
            entrega.put("FLAG", flagObrigaRecebedor.replaceFirst("N", "S"));
        }
    }

    private boolean isClienteDevedorObrigaRecebedor(boolean isClienteDestinatarioParametroObrigaRecebedor, Long idDoctoServico) {
        if (isClienteDestinatarioParametroObrigaRecebedor) {
            return false;
        }

        Cliente cliente = this.devedorDocServService.findByIdDoctoServico(idDoctoServico);

        if (cliente != null && cliente.getIdCliente() != null) {
            return this.isClienteObrigaRecebedor(cliente.getIdCliente());
        }

        return false;
    }

    private boolean isClienteDestinatarioObrigaRecebedor(Map<String, Object> entrega) {
        if (entrega.get("ID_DESTINATARIO") == null) {
            return false;
        }

        return this.isClienteObrigaRecebedor((Long) entrega.get("ID_DESTINATARIO"));
    }

    private void popularVolumes(Map<String, Object> entrega, Long idDoctoServico) {
        String flagObrigaRecebedor = (String) entrega.get("FLAG");

        if ("S".equals(flagObrigaRecebedor.substring(5, 6))) {
            List<VolumeNotaFiscal> volumesConhecimento = this.volumeNotaFiscalService.findByIdConhecimento(idDoctoServico);
            entrega.put("VOLUMES", this.converterVolumesParaMap(volumesConhecimento, idDoctoServico));
        }
    }

    private void popularQuiz(Map<String, Object> entrega, DadosComplemento complemento, List<Map<String, Object>> perguntasParentesco, List<Map<String, Object>> perguntasRisco) {
        entrega.put("perguntasParentesco", perguntasParentesco);

        if (complemento == null) {
         return;   
        }
        
        if(GRAU_DE_RISCO_EXIBICAO_QUIZ.equals(complemento.getDsValorCampo())){
            entrega.put("perguntasRisco", perguntasRisco);
        } else {
            for (Map<String, Object> pergunta : perguntasRisco) {
                if(pergunta.values().toString().toLowerCase().contains(PERGUNTA_CAIXA_LACRADA)){
                    entrega.put("perguntasRisco", Arrays.asList(pergunta));
                    break;
                }
            }
        }
    }

    private List<Map<String, Object>> converterVolumesParaMap(List<VolumeNotaFiscal> volumesConhecimento, Long idDoctoServico) {
        List<Map<String, Object>> volumes = new ArrayList<Map<String, Object>>();

        for (VolumeNotaFiscal volume : volumesConhecimento) {
            Map<String, Object> volumeMap = new HashMap<String, Object>();

            volumeMap.put("ID_DOCTO_SERVICO", idDoctoServico);
            volumeMap.put("ID_VOLUME_NOTA_FISCAL", volume.getIdVolumeNotaFiscal());
            volumeMap.put("NR_VOLUME_EMBARQUE", "'" + volume.getNrVolumeEmbarque() + "'");

            volumes.add(volumeMap);
        }

        return volumes;
    }

    private boolean isClienteObrigaRecebedor(Long idCliente) {
        if (idCliente == null) {
            return false;
        }

        String idsClientesObrigaReceber = (String) this.configuracoesFacade.getValorParametro("NR_IDENTIFICACOES_OBRIGA_RECEBEDOR");

        if (StringUtils.isNotBlank(idsClientesObrigaReceber)) {
            List<String> idClientes = Arrays.asList(idsClientesObrigaReceber.split(";"));

            return idClientes.contains(idCliente.toString());
        }

        return false;
    }

    private List<Map<String, Object>> getPerguntasGrauParentesco() {
        List<Map<String, Object>> perguntasMap = new ArrayList<Map<String, Object>>();
        List<DomainValue> perguntas = this.domainValueService.findDomainValues("DM_GRAU_PARENTESCO", Boolean.TRUE);

        for (DomainValue pergunta : perguntas) {
            Map<String, Object> perguntaMap = new LinkedHashMap<String, Object>();

            perguntaMap.put("dominio", "'" + pergunta.getValue() + "'");
            perguntaMap.put("descricao", "'" + pergunta.getDescriptionAsString() + "'");

            perguntasMap.add(perguntaMap);
        }

        return perguntasMap;
    }

    private List<Map<String, Object>> getPerguntasRisco() {
        List<Map<String, Object>> perguntas = this.grauRiscoPerguntaRespostaService.findPerguntas();

        for (Map<String, Object> pergunta : perguntas) {
            pergunta.put("PERGUNTA", "'" + pergunta.get("PERGUNTA") + "'");
        }

        return perguntas;
    }

    private List<Long> getIdsQuiz() {
        List<Long> idsQuiz = new ArrayList<Long>();
        String dsIdsNatura = (String) this.configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_IDS_NATURA);
        String idNaturaString[] = dsIdsNatura.split(";");

        for (String idNatura : idNaturaString) {
            idsQuiz.add(Long.valueOf(idNatura));
        }

        return idsQuiz;
    }

    /**
     * Consulta da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    public List findLookupByTagManifesto(TypedFlatMap parametros) {
        List lista = getManifestoEntregaDAO().findLookupByTagManifesto(parametros);

        return new AliasToNestedBeanResultTransformer(ManifestoEntrega.class).transformListResult(lista);
    }

    /**
     *
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findSomatorioEntregasRealizadasByIdControleCarga(Long idControleCarga, Long idPais, Long idMoeda) {
        List result = getManifestoEntregaDAO().findSomatorioEntregasRealizadasByIdControleCarga(idControleCarga);
        result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
        BigDecimal vlEntregue = BigDecimalUtils.ZERO;
        BigDecimal psEntregue = BigDecimalUtils.ZERO;
        YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        for (Iterator iter = result.iterator(); iter.hasNext();) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();
            if (tfm.getBigDecimal("somaPsReal") != null) {
                psEntregue = psEntregue.add(tfm.getBigDecimal("somaPsReal"));
            }
            if (tfm.getBigDecimal("somaVlMercadoria") != null) {
                BigDecimal vlConvertido = conversaoMoedaService.findConversaoMoeda(
                        tfm.getLong("idPais"),
                        tfm.getLong("idMoeda"),
                        idPais,
                        idMoeda,
                        dtAtual,
                        tfm.getBigDecimal("somaVlMercadoria")
                );

                vlEntregue = vlEntregue.add(vlConvertido);
            }
        }

        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("vlEntregue", vlEntregue);
        mapRetorno.put("psEntregue", psEntregue);
        return mapRetorno;
    }

    /**
     *
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findSomatorioEntregasASeremRealizadasByIdControleCarga(Long idControleCarga, Long idPais, Long idMoeda) {
        List result = getManifestoEntregaDAO().findSomatorioEntregasASeremRealizadasByIdControleCarga(idControleCarga);
        result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
        BigDecimal vlAEntregar = BigDecimalUtils.ZERO;
        BigDecimal psAEntregar = BigDecimalUtils.ZERO;
        BigDecimal psAforadoAEntregar = BigDecimalUtils.ZERO;
        YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

        for (Iterator iter = result.iterator(); iter.hasNext();) {
            TypedFlatMap tfm = (TypedFlatMap) iter.next();
            if (tfm.getBigDecimal("somaPsReal") != null) {
                psAEntregar = psAEntregar.add(tfm.getBigDecimal("somaPsReal"));
            }
            if (tfm.getBigDecimal("somaPsAforado") != null) {
                psAforadoAEntregar = psAforadoAEntregar.add(tfm.getBigDecimal("somaPsAforado"));
            }
            if (tfm.getBigDecimal("somaVlMercadoria") != null) {
                BigDecimal vlConvertido = conversaoMoedaService.findConversaoMoeda(
                        tfm.getLong("idPais"),
                        tfm.getLong("idMoeda"),
                        idPais,
                        idMoeda,
                        dtAtual,
                        tfm.getBigDecimal("somaVlMercadoria")
                );

                vlAEntregar = vlAEntregar.add(vlConvertido);
            }
        }

        TypedFlatMap mapRetorno = new TypedFlatMap();
        mapRetorno.put("vlAEntregar", vlAEntregar);
        mapRetorno.put("psAEntregar", psAEntregar);
        mapRetorno.put("psAforadoAEntregar", psAforadoAEntregar);
        return mapRetorno;
    }

    public Map findCodNroPlacaManifestosEntregasCorporativo(Long seceNumero, String unidSigla) {
        List result = getManifestoEntregaDAO().findCodNroPlacaManifestosEntregasCorporativo(seceNumero, unidSigla);

        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return (Map) result.get(0);
    }

    public void generateManifestoAutomaticoColetaComEntregaDiretaAeroporto(List<DoctoServico> docs) {
        ControleCarga controleCarga = controleCargaService.findControleCargaByDoctoServicoDetalheColeta(docs.get(0).getIdDoctoServico());
        List<Manifesto> manifestos = this.getManifestosEntregaNaoCancelados(controleCarga.getIdControleCarga());

        boolean isNewMan = CollectionUtils.isEmpty(manifestos);

        Manifesto manifesto = null;
        ManifestoEntrega me = null;

        if (isNewMan) {
            manifesto = pedidoColetaService.storeManifestoEntrega(controleCarga, JTDateTimeUtils.getDataHoraAtual(), ConstantesExpedicao.TP_MANIFESTO_ENTREGA_NORMAL);
            manifesto.setPreManifestoDocumentos(new ArrayList<PreManifestoDocumento>());
        } else {
            manifesto = manifestoService.findById(manifestos.get(0).getIdManifesto());
            me = manifestoEntregaService.findById(manifesto.getManifestoEntrega().getIdManifestoEntrega());
        }

        for (DoctoServico doctoServico : docs) {
            PreManifestoDocumento preManifestoDocto = pedidoColetaService.storePreManifestoDoctoVolume(manifesto, doctoServico);
            carregamentoMobileService.updateValoresManifesto(doctoServico, manifesto, ConstantesExpedicao.MULTIPLICADOR_SINAIS_POSITIVO);
            pedidoColetaService.generateEventoDoctoVolume(doctoServico.getIdDoctoServico(), ConstantesExpedicao.CD_EVENTO_PRE_MANIFESTO, null, null);

            if (isNewMan) {
                manifesto.getPreManifestoDocumentos().add(preManifestoDocto);
            } else {
                ManifestoEntregaDocumento med = manifestoEntregaService.storeManifestoEntregaDocVolume(preManifestoDocto, manifesto, false);
                manifestoEntregaService.generateEventoDocto(med, manifesto, me, ConstantesSim.EVENTO_MANIFESTO_EMITIDO, null);
            }
        }

        if (isNewMan) {
            this.storeEmitirManifestoValidaManifestoEntrega(new TypedFlatMap(), manifesto, null, false);
            this.storeValidateManifestoEmitido(manifesto.getIdManifesto(), ConstantesExpedicao.TP_STATUS_MANIFESTO_COLETA_ENTREGA);
        }

        String obs = configuracoesFacade.getMensagem("aeroporto");
        for (DoctoServico docto : docs) {
            pedidoColetaService.generateEventoDoctoVolume(docto.getIdDoctoServico(), ConstantesSim.EVENTO_SAIDA_PORTARIA_EMROTA, null, obs);
        }

        controleCargaService.executeEncerrarMdfesAutorizados(controleCarga.getIdControleCarga());
        controleCargaService.generateMdfe(controleCarga.getIdControleCarga(), true);
    }

    private List<Manifesto> getManifestosEntregaNaoCancelados(Long idControleCarga) {
        List<Manifesto> manifestos = manifestoService.findManifestoByIdControleCarga(idControleCarga,
                SessionUtils.getFilialSessao().getIdFilial(), null, ConstantesExpedicao.TP_MANIFESTO_ENTREGA);

        CollectionUtils.filter(manifestos, new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                Manifesto man = (Manifesto) arg0;
                return !man.getTpStatusManifesto().getValue().equals(ConstantesExpedicao.TP_STATUS_MANIFESTO_CANCELADO);
            }
        });

        return manifestos;
    }

    public Long findTipoTabelaManifestosEntregasCorporativo(Long seceNumero, String unidSigla) {
        return getManifestoEntregaDAO().findTipoTabelaManifestosEntregasCorporativo(seceNumero, unidSigla);
    }

    public List<String> findEnderecosManifestosEntregas(Long idControleCarga) {
        return getManifestoEntregaDAO().findEnderecosManifestosEntregas(idControleCarga);
    }

    public List<Map<String, Object>> findManifestoEntregaSuggest(String sgFilial, Long nrManifestoEntrega, Long idEmpresa) {
        return getManifestoEntregaDAO().findManifestoEntregaSuggest(sgFilial, nrManifestoEntrega, idEmpresa);
    }

    public void setGerarBoletoReciboManifestoService(GerarBoletoReciboManifestoService gerarBoletoReciboManifestoService) {
        this.gerarBoletoReciboManifestoService = gerarBoletoReciboManifestoService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setTipoDocumentoEntregaService(TipoDocumentoEntregaService tipoDocumentoEntregaService) {
        this.tipoDocumentoEntregaService = tipoDocumentoEntregaService;
    }

    public void setLmManifestoDao(LMManifestoDAO lmManifestoDao) {
        this.lmManifestoDao = lmManifestoDao;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
        this.dadosComplementoService = dadosComplementoService;
    }

    public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
        this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public List<ManifestoEntrega> findManifestoByIdControleCarga(Long idControleCarga) {
        return getManifestoEntregaDAO().findManifestoByIdControleCarga(idControleCarga);
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
        this.devedorDocServService = devedorDocServService;
    }

    public Integer findQuantidadeEntregasEfetuadasByIdManifestoEntrega(Long idManifestoEntrega) {
        return getManifestoEntregaDAO().findQuantidadeEntregasEfetuadasByIdManifestoEntrega(idManifestoEntrega);
    }

    public PedidoColetaService getPedidoColetaService() {
        return pedidoColetaService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public CarregamentoMobileService getCarregamentoMobileService() {
        return carregamentoMobileService;
    }

    public void setCarregamentoMobileService(CarregamentoMobileService carregamentoMobileService) {
        this.carregamentoMobileService = carregamentoMobileService;
    }

    public InformarSaidaService getInformarSaidaService() {
        return informarSaidaService;
    }

    public void setInformarSaidaService(InformarSaidaService informarSaidaService) {
        this.informarSaidaService = informarSaidaService;
    }

    public ControleCargaService getControleCargaService() {
        return controleCargaService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
        this.manifestoEntregaService = manifestoEntregaService;
    }

    public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
        this.volumeNotaFiscalService = volumeNotaFiscalService;
    }

    public GrauRiscoPerguntaRespostaService getGrauRiscoPerguntaRespostaService() {
        return grauRiscoPerguntaRespostaService;
    }

    public void setGrauRiscoPerguntaRespostaService(GrauRiscoPerguntaRespostaService grauRiscoPerguntaRespostaService) {
        this.grauRiscoPerguntaRespostaService = grauRiscoPerguntaRespostaService;
    }

	public ManifestoEntrega findManifestoAbertoSubcontratacaoFedex(Filial filialEvento, Short nrRota, DateTime dhOcorrencia, Long idDoctoServico) {
		return getManifestoEntregaDAO().findManifestoAbertoSubcontratacaoFedex(filialEvento, nrRota,dhOcorrencia, idDoctoServico);
	}
	
	public ManifestoEntrega findManifestoAbertoByOcorrenciaSubcontratacaoFedex(DoctoServico doctoServico){
		return getManifestoEntregaDAO().findManifestoAbertoByOcorrenciaSubcontratacaoFedex(doctoServico);
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public ManifestoEntrega findManifestoAbertoByDoctoServico(
			Filial filialEvento, Short nrRota, DoctoServico doctoServico) {
		return getManifestoEntregaDAO().findManifestoAbertoByDoctoServico(filialEvento, nrRota,doctoServico);
	}

    public Integer findQuantidadeManifestoEntrega(Long idDoctoServico){
        return getManifestoEntregaDAO().findQuantidadeManifestoEntrega(idDoctoServico);
    }
}

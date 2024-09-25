package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.expedicao.DoctoServicoSaltoDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.param.DoctoServicoParam;
import com.mercurio.lms.contasreceber.model.param.RelacaoDocumentoServicoDepositoParam;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.edi.model.service.ConhecimentoFedexService;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.service.*;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.dao.DoctoServicoDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.service.*;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.rnc.model.service.FotoOcorrenciaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.sim.model.dao.LMFreteDAO;
import com.mercurio.lms.sim.model.dao.LMIntegranteDAO;
import com.mercurio.lms.sim.model.dao.LocalizacaoMercadoriaDAO;
import com.mercurio.lms.sim.model.service.FaseProcessoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

import com.mercurio.lms.municipios.model.service.FeriadoService;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projection;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser
 * utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.doctoServicoService"
 */
public class DoctoServicoService extends CrudService<DoctoServico, Long> {
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
    private static final String DM_STATUS_AWB = "DM_STATUS_AWB";
    private static final Integer MAXIMO_REGISTROS_RELATORIO_CSV = 100000;
    private static final String LMS_04421 = "LMS-04421";

    private ConhecimentoService conhecimentoService;
    private CtoInternacionalService ctoInternacionalService;
    private MdaService mdaService;
    private ReciboReembolsoService reciboReembolsoService;
    private ManifestoService manifestoService;
    private LMIntegranteDAO lmIntegranteDao;
    private LocalizacaoMercadoriaDAO localizacaoMercadoriaDAO;
    private LMComplementoDAO lmComplementoDao;
    private DomainValueService domainValueService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private RotaIdaVoltaService rotaIdaVoltaService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private LMFreteDAO lmFreteDao;
    private OrdemFilialFluxoService ordemFilialFluxoService;
    private FilialRotaService filialRotaService;
    private ConfiguracoesFacade configuracoesFacade;
    private SubstAtendimentoFilialService substAtendimentoFilialService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private DiaUtils diaUtils;
    private WorkflowPendenciaService workflowPendenciaService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private DoctoServicoWorkflowService doctoServicoWorkflowService;
    private IntegracaoJmsService integracaoJmsService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
    private ImpostoServicoService impostoServicoService;
    private CalculoTributoService calculoTributoService;
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
    private FaseProcessoService faseProcessoService;
    private CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;
    private InformacaoDoctoClienteService informacaoDoctoClienteService;
    private FotoOcorrenciaService fotoOcorrenciaService;
    private ConfiguracoesFacadeImpl configuracoesFacadeImpl;
    private ConhecimentoFedexService conhecimentoFedexService;
    private DevedorDocServService devedorDocServService;
    private DpeService dpeService;
    private FeriadoService feriadoService;

    public List<Long> findDoctoServicoToEnviaCTeCliente() {
        Integer rownum = IntegerUtils.getInteger((BigDecimal) configuracoesFacade.getValorParametro("QTD_DOCS_ENVIO_XML"));
        return this.getDoctoServicoDAO().findDoctoServicoToEnviaCTeCliente(rownum);
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public void setDiaUtils(DiaUtils diaUtils) {
        this.diaUtils = diaUtils;
    }

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public List<DoctoServico> findByIdManifestoMoreRestrictions(Long idManifesto) {
        return this.getDoctoServicoDAO().findByIdManifestoMoreRestrictions(idManifesto);
    }

    public List<DoctoServico> findByIds(List<Long> ids) {
        Session session = this.getDao().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession();
        return session.createCriteria(DoctoServico.class).add(Expression.in(ConstantesExpedicao.ID_DOCTO_SERVICO, ids)).list();
    }

    /**
     * Recupera uma instância de <code>DoctoServico</code> a partir do ID.
     *
     * @param id       representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    @SuppressWarnings("rawtypes")
    public DoctoServico findById(Long id, boolean lazyLoad) {
        DoctoServico doctoServico = (DoctoServico) super.findByIdInitLazyProperties(id, lazyLoad);
        Class classe = Hibernate.getClass(doctoServico);

        //Quando é Conhecimento
        if (classe.equals(Conhecimento.class)) {
            doctoServico = (Conhecimento) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(Conhecimento.class, doctoServico.getIdDoctoServico());
        } else if (classe.equals(NotaFiscalServico.class)) {
        //Quando é NotaFiscalServico
            doctoServico = (NotaFiscalServico) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(NotaFiscalServico.class, doctoServico.getIdDoctoServico());
        } else if (classe.equals(NotaDebitoNacional.class)) {
        //Quando é NotaDebitoNacional
            doctoServico = (NotaDebitoNacional) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(NotaDebitoNacional.class, doctoServico.getIdDoctoServico());
        } else if (classe.equals(ReciboReembolso.class)) {
        //Quando é ReciboReembolso
            doctoServico = (ReciboReembolso) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(ReciboReembolso.class, doctoServico.getIdDoctoServico());
        } else if (classe.equals(CtoInternacional.class)) {
        //Quando é CtoInternacional
            doctoServico = (CtoInternacional) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(CtoInternacional.class, doctoServico.getIdDoctoServico());
        } else if (classe.equals(Mda.class)) {
        //Quando é Mda
            doctoServico = (Mda) getDoctoServicoDAO().getAdsmHibernateTemplate()
                    .get(Mda.class, doctoServico.getIdDoctoServico());
        } else {
        //Só há a superclasse de DoctoServico. Isto é inconsistente.
            throw new IllegalStateException("Não foi possível encontrar a especialização da classe DoctoServico de id "
                    + doctoServico.getIdDoctoServico());
        }

        return doctoServico;
    }

    /**
     * Recupera uma instância de <code>DoctoServico</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    @Override
    @SuppressWarnings("rawtypes")
    public DoctoServico findById(Long id) {
        return findById(id, true);
    }

    /**
     * Retorna o doctumento de servico com os clientes remetente e destinatário 'fetchado'.
     *
     * @param idDoctoServico Long
     * @return DoctoServico
     * @author Mickaël Jalbert
     * @since 06/06/2006
     */
    public DoctoServico findByIdWithRemetenteDestinatario(Long idDoctoServico) {
        return getDoctoServicoDAO().findByIdWithRemetenteDestinatario(idDoctoServico);
    }


    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    @Override
    public void removeById(Long id) {
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
    @Override
    public java.io.Serializable store(DoctoServico bean) {
        return super.store(bean);
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setDoctoServicoDAO(DoctoServicoDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoServicoDAO getDoctoServicoDAO() {
        return (DoctoServicoDAO) getDao();
    }

    /**
     * @param idDoctoServico
     * @return
     */
    public TypedFlatMap findDoctoServicoByTpDocumento(Long idDoctoServico) {
        Object obj = getDoctoServicoDAO().findDoctoServicoByTpDocumento(idDoctoServico);
        DoctoServico doctoServico = (DoctoServico) obj;
        TypedFlatMap map = new TypedFlatMap();
        map.put("dhEmissao", doctoServico.getDhEmissao());
        map.put("moeda.dsSimbolo", doctoServico.getMoeda().getSiglaSimbolo());
        map.put("moeda.idMoeda", doctoServico.getMoeda().getIdMoeda());
        map.put("vlTotalDocServico", doctoServico.getVlMercadoria());
        map.put("servico.tpModal", doctoServico.getServico().getTpModal().getValue());

        if (doctoServico.getLocalizacaoMercadoria() != null) {
            map.put("localizacaoMercadoria.cdLocalizacaoMercadoria", doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
        }

        if (doctoServico.getFilialByIdFilialDestino() != null) {
            map.put("filialByIdFilialDestino.idFilial", doctoServico.getFilialByIdFilialDestino().getIdFilial());
            map.put("filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
        }

        if (doctoServico.getClienteByIdClienteRemetente() != null) {
            map.put("clienteByIdClienteRemetente.idCliente", doctoServico.getClienteByIdClienteRemetente().getIdCliente());
            map.put("clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
            map.put("clienteByIdClienteRemetente.pessoa.nrIdentificacaoFormatado",
                    FormatUtils.formatIdentificacao(doctoServico.getClienteByIdClienteRemetente().getPessoa().getTpIdentificacao(),
                            doctoServico.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao()));
            map.put("clienteByIdClienteRemetente.blClienteCCT", doctoServico.getClienteByIdClienteRemetente().getBlClienteCCT());
        }

        if (doctoServico.getClienteByIdClienteDestinatario() != null) {
            map.put("clienteByIdClienteDestinatario.idCliente", doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
            map.put("clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
            map.put("clienteByIdClienteDestinatario.pessoa.nrIdentificacaoFormatado",
                    FormatUtils.formatIdentificacao(doctoServico.getClienteByIdClienteDestinatario().getPessoa().getTpIdentificacao(),
                            doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao()));
        }
        
        DevedorDocServ devedorDocServ = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
        map.put("clienteByIdClienteTomador.idCliente", devedorDocServ.getCliente().getIdCliente());
        map.put("clienteByIdClienteTomador.pessoa.nmPessoa", devedorDocServ.getCliente().getPessoa().getNmPessoa());
        map.put("clienteByIdClienteTomador.pessoa.nrIdentificacaoFormatado",
                FormatUtils.formatIdentificacao(devedorDocServ.getCliente().getPessoa().getTpIdentificacao(),
                		devedorDocServ.getCliente().getPessoa().getNrIdentificacao()));

        List<String> tipos = Arrays.asList("CTE", "NFT", "NTE");

        if (tipos.contains(doctoServico.getTpDocumentoServico().getValue())) {
            Conhecimento conhecimento = (Conhecimento) obj;
            map.put("qtVolumes", conhecimento.getQtVolumes());
            map.put("nrDoctoServico", conhecimento.getNrConhecimento());
            map.put("psDoctoServico", conhecimento.getPsReal());
            map.put("tpSituacaoDoctoServico", conhecimento.getTpSituacaoConhecimento().getDescription().getValue().toString());
            if (conhecimento.getCtoAwbs() != null && !conhecimento.getCtoAwbs().isEmpty()) {
                map.put("awb", "ok");
            }
        } else if (doctoServico.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.MINUTA_DESPACHO_E_ACOMPANHA)) {
            Mda mda = (Mda) obj;
            Map<String, Object> mapSumMda = mdaService.findSomaQtdVolumesItens(idDoctoServico);
            map.put("qtVolumes", mapSumMda.get("sumQtVolumes"));
            map.put("nrDoctoServico", mda.getNrDoctoServico());
            map.put("psDoctoServico", mapSumMda.get("sumPsItem"));
            map.put("tpSituacaoDoctoServico", mda.getTpStatusMda());
        }

        map.put("isClienteObrigaBO", validateObrigatoriedadeBO(doctoServico));
        map.put("existFotoOcorrenciaTpAnexoBO", existFotoOcorrenciaTpAnexoBO(doctoServico));

        return map;
    }

    private Boolean validateObrigatoriedadeBO(DoctoServico doctoServico) {
        Boolean clienteObrigaBO = BooleanUtils.isTrue(doctoServico.getDevedorDocServFats().get(IntegerUtils.ZERO).getCliente().getBlObrigaBO());
        DomainValue clienteTpModalRnc = doctoServico.getDevedorDocServFats().get(IntegerUtils.ZERO).getCliente().getTpModalObrigaBO();
        DomainValue doctoServicoTpModal = doctoServico.getServico().getTpModal();

        if (clienteObrigaBO &&
                (clienteTpModalRnc != null && doctoServicoTpModal != null) &&
                (clienteTpModalRnc.getValue().equals(ConstantesExpedicao.MODAL_AMBOS_OBRIGA_BO) || clienteTpModalRnc.equals(doctoServicoTpModal))) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    private Boolean existFotoOcorrenciaTpAnexoBO(DoctoServico doctoServico) {
        List fotos = fotoOcorrenciaService.findByIdDoctoTpBO(doctoServico.getIdDoctoServico());

        if (fotos != null && !fotos.isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List<Map<String, Object>> findDoctoServicoOriginal(TypedFlatMap tfm) {
        return getDoctoServicoDAO().findDoctoServicoOriginal(tfm);
    }

    public List<DoctoServico> findDoctoServicoOriginalPojo(TypedFlatMap tfm) {
        return getDoctoServicoDAO().findDoctoServicoOriginalPojo(tfm);
    }

    public List findCTRCLookupByNrConhecimentoByFilialOrigem(Long nrConhecimento, Long idFilial) {
        // TODO ajustar
        //return processList4FindCTRCLookupByNrConhecimentoByFilialOrigem(getDoctoServicoDAO().findCTRCLookupByNrConhecimentoByFilialOrigem(nrConhecimento, idFilial));
        return null;
    }

    public Long findIdClienteRemetenteById(Long idDoctoServico) {
        return getDoctoServicoDAO().findIdClienteRemetenteById(idDoctoServico);
    }

    public Long findIdClienteDestinatarioById(Long idDoctoServico) {
        return getDoctoServicoDAO().findIdClienteDestinatarioById(idDoctoServico);
    }

    /**
     * Retorna um map dos valores somados dos documentos da fatura informada.
     *
     * @param idFatura Long
     * @return TypedFlatMap
     */
    public TypedFlatMap findSomaValoresByFatura(Long idFatura) {
        Object[] objSoma = (Object[]) getDoctoServicoDAO().findSomaValoresByFatura(idFatura);
        TypedFlatMap mapSoma = new TypedFlatMap();
        mapSoma.put("basepisconfinscsll", objSoma[0]);
        mapSoma.put("baseIR", objSoma[1]);
        mapSoma.put("confins", objSoma[2]);
        mapSoma.put("inss", objSoma[3]);
        mapSoma.put("csll", objSoma[4]);
        mapSoma.put("ir", objSoma[5]);
        mapSoma.put("iss", objSoma[6]);
        mapSoma.put("pis", objSoma[7]);
        return mapSoma;
    }


    /**
     * Verifica se o usuário logado possui permissões de acesso ao documento de serviço
     *
     * @param idDoctoServico Identificador do documento de serviço
     * @param idFilial       Identificador da filial
     * @return <code>TRUE</code> Se o usuário logado possui permissões de acesso ao documento de serviço informado ou <code>FALSE</code>
     * caso contrário.
     */
    public Boolean validatePermissaoDocumentoUsuario(Long idDoctoServico, Long idFilial) {
        if (SessionUtils.isFilialSessaoMatriz()) {
            return true;
        }

        return validatePermissaoDocumentoUsuarioIgnoreMatriz(idDoctoServico, idFilial);
    }

    public Boolean validatePermissaoDocumentoUsuarioIgnoreMatriz(Long idDoctoServico, Long idFilial) {
        return this.getDoctoServicoDAO().validatePermissaoDocumentoUsuario(idDoctoServico, idFilial);
    }


    public List findDoctoServicoByParams(Long idFilial, Long nrDoctoServico, String tpDoctoServico, String dhEmissao) {
        return getDoctoServicoDAO().findDoctoServicoByParams(idFilial, nrDoctoServico, tpDoctoServico, dhEmissao);
    }


    public DoctoServico findDoctoServicoByFilialNumeroEDhEmissao(Long idFilial, Long nrDoctoServico, DateTime dhEmissao) {
        return getDoctoServicoDAO().findDoctoServicoByFilialNumeroEDhEmissao(idFilial, nrDoctoServico, dhEmissao);
    }

    /**
     * Método que busca todos os Documentos de Servico de um Manifesto.
     * Caso não necessite todos os tipos de Documento de Serviço, utilizar os métodos específicos
     * de cada entidade.
     *
     * @param idManifesto
     * @return
     */
    public ListDoctosServicoManifesto findCustomListDoctosServicoByIdManifesto(Long idManifesto) {
        ListDoctosServicoManifesto listDoctosServicoManifesto = new ListDoctosServicoManifesto();
        if (manifestoService.isManifestoViagemNacional(idManifesto)) {
            listDoctosServicoManifesto = findCustomListDoctosServicoByIdManifestoViagemNacional(idManifesto);
        } else if (manifestoService.isManifestoViagemInternacional(idManifesto)) {
            listDoctosServicoManifesto = findCustomListDoctosServicoByIdManifestoViagemInternacional(idManifesto);
        } else if (manifestoService.isManifestoEntrega(idManifesto)) {
            findCustomListDoctosServicoByIdManifestoEntrega(idManifesto);
        }
        return listDoctosServicoManifesto;
    }


    private ListDoctosServicoManifesto findCustomListDoctosServicoByIdManifestoViagemNacional(Long idManifesto) {
        ListDoctosServicoManifesto listDoctosServicoManifesto = new ListDoctosServicoManifesto();
        listDoctosServicoManifesto.setConhecimentos(conhecimentoService.findConhecimentosByIdManifestoViagemNacional(idManifesto, null));
        listDoctosServicoManifesto.setMdas(mdaService.findMdasByIdManifestoViagemNacional(idManifesto));
        return listDoctosServicoManifesto;
    }

    private ListDoctosServicoManifesto findCustomListDoctosServicoByIdManifestoViagemInternacional(Long idManifesto) {
        ListDoctosServicoManifesto listDoctosServicoManifesto = new ListDoctosServicoManifesto();
        listDoctosServicoManifesto.setCtosInternacionais(ctoInternacionalService.findCtosInternacionaisByIdManifestoViagemInternacional(idManifesto));
        return listDoctosServicoManifesto;
    }

    private ListDoctosServicoManifesto findCustomListDoctosServicoByIdManifestoEntrega(Long idManifesto) {
        ListDoctosServicoManifesto listDoctosServicoManifesto = new ListDoctosServicoManifesto();
        listDoctosServicoManifesto.setCtosInternacionais(ctoInternacionalService.findCtosInternacionaisByIdManifestoEntrega(idManifesto));
        listDoctosServicoManifesto.setConhecimentos(conhecimentoService.findConhecimentosByIdManifestoEntrega(idManifesto));
        listDoctosServicoManifesto.setNotasFiscaisTransporte(conhecimentoService.findNotasFiscaisTransporteByIdManifestoEntrega(idManifesto));
        listDoctosServicoManifesto.setRecibosReembolso(reciboReembolsoService.findRecibosReembolsoByIdManifestoEntrega(idManifesto));
        listDoctosServicoManifesto.setMdas(mdaService.findMdasByIdManifestoEntrega(idManifesto));
        return listDoctosServicoManifesto;
    }

    /**
     * Metodo gerando overload de dados, otimizar pontualmente a projecao para para cada caso
     *
     * ver novos métodos findDoctoServicoByIdManifesto
     * @deprecated
     */
    @Deprecated
    public List<DoctoServico> findDoctosServicoByIdManifesto(Long idManifesto) {
        return getDoctoServicoDAO().findDoctoServicoByIdManifesto(idManifesto);
    }

    /**
     * Busca conhecimentos do manifesto com projeção específica
     *
     * @param idManifesto
     * @param projection
     * @param alias
     * @param criterions
     * @return
     * @author André Valadas
     */
    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions) {
        return getDoctoServicoDAO().findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterions);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions, boolean hasDoctosEntregues) {
        return getDoctoServicoDAO().findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterions, hasDoctosEntregues);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias) {
        return findDoctoServicoByIdManifesto(idManifesto, projection, alias, null);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection) {
        return findDoctoServicoByIdManifesto(idManifesto, projection, null, null);
    }

    public List<DoctoServico> findDocumentosByTpOcorrenciasByIdManifesto(Long idManifesto, String tpOcorrencias) {
        return getDoctoServicoDAO().findDocumentosByTpOcorrenciasByIdManifesto(idManifesto, tpOcorrencias);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions, boolean validateDoctosEntregues, boolean validateDoctosDescarrecados, DateTime dhInicioOperacao) {
        return getDoctoServicoDAO().findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterions, validateDoctosEntregues, validateDoctosDescarrecados, dhInicioOperacao);
    }

    /**
     * Verifica se existe conhecimentos para o Manifesto
     *
     * @param idManifesto
     * @param alias
     * @param criterions
     * @return
     * @author André Valadas
     */
    public Boolean verifyDoctoServicoByIdManifesto(final Long idManifesto, final Map<String, String> alias, final List<Criterion> criterions) {
        return getDoctoServicoDAO().verifyDoctoServicoByIdManifesto(idManifesto, alias, criterions);
    }

    public Boolean verifyDoctoServicoByIdManifesto(final Long idManifesto, final Map<String, String> alias) {
        return verifyDoctoServicoByIdManifesto(idManifesto, alias, null);
    }

    public Boolean verifyDoctoServicoByIdManifesto(final Long idManifesto, final List<Criterion> criterions) {
        return verifyDoctoServicoByIdManifesto(idManifesto, null, criterions);
    }

    public Boolean verifyDoctoServicoByIdManifesto(final Long idManifesto) {
        return verifyDoctoServicoByIdManifesto(idManifesto, null, null);
    }

    //TODO corrigir este metodo!!
    public ResultSetPage findPaginatedDoctoServicoByIdManifesto(Long idManifesto, String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, String notaFiscal, FindDefinition findDefinition) {
        Map<String, Object> dominio = new HashMap<String, Object>();
        List<DomainValue> domainValueList = domainValueService.findDomainValues("DM_TIPO_DOCUMENTO_SERVICO", true);
        for (DomainValue domainValue : domainValueList) {
            dominio.put(domainValue.getValue(), domainValue.getDescription());
        }

        ResultSetPage rsp = getDoctoServicoDAO().findPaginatedDoctoServicoByIdManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal, findDefinition);
        List<Object[]> rs = rsp.getList();
        List<DoctoServico> list = new ArrayList<DoctoServico>(rs.size());
        for (Object[] obj : rs) {
            DoctoServico doctoServico = new DoctoServico();
            doctoServico.setIdDoctoServico((Long) obj[0]);
            doctoServico.setNrDoctoServico((Long) obj[1]);
            doctoServico.setQtVolumes((Integer) obj[8]);
            doctoServico.setPsReal((BigDecimal) obj[9]);
            doctoServico.setVlMercadoria((BigDecimal) obj[12]);
            doctoServico.setVlTotalDocServico((BigDecimal) obj[13]);
            doctoServico.setDtPrevEntrega((YearMonthDay) obj[14]);

            if (obj[2] != null) {
                Servico servico = new Servico();
                VarcharI18n servicoI18n = new VarcharI18n((String) obj[2]);
                servico.setDsServico(servicoI18n);
                servico.setSgServico((String) obj[15]);
                doctoServico.setServico(servico);
            }

            if (obj[16] != null) {
                LocalizacaoMercadoria localizacaoMercadoria = new LocalizacaoMercadoria();
                VarcharI18n localizaI18n = new VarcharI18n((String) obj[16]);
                localizacaoMercadoria.setDsLocalizacaoMercadoria(localizaI18n);
                doctoServico.setLocalizacaoMercadoria(localizacaoMercadoria);
            }

            if (obj[4] != null) {
                Filial origem = new Filial();
                origem.setSgFilial((String) obj[4]);
                doctoServico.setFilialByIdFilialOrigem(origem);
            }

            if (obj[5] != null) {
                Filial destino = new Filial();
                destino.setSgFilial((String) obj[5]);
                doctoServico.setFilialByIdFilialDestino(destino);
            }

            if (obj[6] != null) {
                Cliente clienteRemetente = new Cliente();
                Pessoa pessoaRemetente = new Pessoa();
                pessoaRemetente.setNmPessoa((String) obj[6]);
                clienteRemetente.setPessoa(pessoaRemetente);
                doctoServico.setClienteByIdClienteRemetente(clienteRemetente);
            }

            if (obj[7] != null) {
                Cliente clienteDestinatario = new Cliente();
                Pessoa pessoaDestinatario = new Pessoa();
                pessoaDestinatario.setNmPessoa((String) obj[7]);
                clienteDestinatario.setPessoa(pessoaDestinatario);
                doctoServico.setClienteByIdClienteDestinatario(clienteDestinatario);
            }

            if (obj[10] != null && obj[11] != null) {
                Moeda moeda = new Moeda();
                moeda.setSgMoeda((String) obj[10]);
                moeda.setDsSimbolo((String) obj[11]);
                doctoServico.setMoeda(moeda);
            }

            if (obj[3] != null) {
                DomainValue domainValue = new DomainValue();
                domainValue.setValue((String) obj[3]);
                domainValue.setDescription((VarcharI18n) dominio.get(obj[3]));
                doctoServico.setTpDocumentoServico(domainValue);
            }

            list.add(doctoServico);

        }
        rsp.setList(list);
        return rsp;
    }

    public Integer getRowCountDoctoServicoByIdManifesto(Long idManifesto, String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, String notaFiscal) {
        return getDoctoServicoDAO().getRowCountDoctoServicoByIdManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal);
    }

    /**
     * Busca o numero de docto servico pelo tipo do manifesto
     *
     * @param filtrarTipoDocumento
     */
    public Integer findNrDoctosServicoByIdManifesto(Manifesto manifesto, boolean filtrarTipoDocumento) {
        int retorno = 0;

        String tpManifesto = manifesto.getTpManifesto().getValue();

        if ("V".equalsIgnoreCase(tpManifesto)) {
            DomainValue domainTpAbrangencia = manifesto.getTpAbrangencia();
            if (domainTpAbrangencia != null) {
                if ("N".equalsIgnoreCase(domainTpAbrangencia.getValue())) {
                    Integer r = getDoctoServicoDAO().getRowCountConhecimentosByIdManifestoViagemNacional(manifesto.getIdManifesto(), filtrarTipoDocumento);
                    if (r != null) {
                        retorno = retorno + r.intValue();
                    }
                    r = getDoctoServicoDAO().getRowCountMdasByIdManifestoViagemNacional(manifesto.getIdManifesto());
                    if (r != null) {
                        retorno = retorno + r.intValue();
                    }
                } else if ("I".equalsIgnoreCase(domainTpAbrangencia.getValue())) {
                    Integer r = getDoctoServicoDAO().getRowCountCtosInternacionaisByIdManifestoViagemInternacional(manifesto.getIdManifesto(), filtrarTipoDocumento);
                    if (r != null) {
                        retorno = retorno + r.intValue();
                    }
                }
            }
        } else if ("E".equalsIgnoreCase(tpManifesto)) {
            Integer r = getDoctoServicoDAO().getRowCountDoctosServicoByIdManifestoEntrega(manifesto.getIdManifesto(), filtrarTipoDocumento);
            if (r != null) {
                retorno = retorno + r.intValue();
            }
        }
        return Integer.valueOf(retorno);
    }

    public ResultSetPage findPaginatedRelacaoDeposito(RelacaoDocumentoServicoDepositoParam param, FindDefinition findDef) {
        return getDoctoServicoDAO().findPaginatedRelacaoDeposito(param, findDef);
    }

    public Integer getRowCountRelacaoDeposito(RelacaoDocumentoServicoDepositoParam param) {
        return getDoctoServicoDAO().getRowCountRelacaoDeposito(param);
    }

    /**
     * Pesquisa de grid que retorna doctoServico com conhecimento se tem.
     *
     * @author Mickaël Jalbert
     * @since 03/04/2006
     */
    public ResultSetPage findPaginatedComConhecimento(DoctoServicoParam doctoServicoParam, List tpsSituacao, FindDefinition findDef) {
        ResultSetPage rsp = getDoctoServicoDAO().findPaginatedComConhecimento(doctoServicoParam, tpsSituacao, findDef);
        List<Object[]> rs = rsp.getList();
        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>(rs.size());
        for (Object[] obj : rs) {
            Map<String, Object> map = new HashMap<String, Object>(14);

            map.put("idDevedorDocServFat", obj[0]);
            map.put("nrNotaFiscal", obj[1]);
            map.put("tpDocumentoServico", obj[2]);
            map.put("sgFilial", obj[3]);
            map.put("nrDoctoServico", FormatUtils.formataNrDocumento(((Long) obj[4]).toString(), (String) obj[2]));

            if (obj[5] != null) {
                map.put("dtEmissao", new YearMonthDay(obj[5]));
            }
            map.put("vlMercadoria", obj[6]);
            map.put(ConstantesExpedicao.VL_TOTAL_DOC_SERVICO, obj[7]);
            map.put("tpFrete", obj[8]);
            map.put("idMoeda", obj[9]);
            map.put("tpModal", obj[10]);
            map.put("tpAbrangencia", obj[11]);
            map.put("idServico", obj[12]);
            map.put("idDivisaoCliente", obj[13]);

            newList.add(map);
        }
        rsp.setList(newList);
        return rsp;
    }

    /**
     * Retorna o número de registro de documento baseado nos filtros informado.
     *
     * @author Mickaël Jalbert
     * @since 08/01/2007
     */
    public Integer getRowCountComConhecimento(DoctoServicoParam doctoServicoParam, List tpsSituacao) {
        return getDoctoServicoDAO().getRowCountComConhecimento(doctoServicoParam, tpsSituacao);
    }

    /**
     * Retorna os documentos de servico que tenha como destino a rota de viagem especificada.
     * Retorna apenas os documentos que tenham prioridade alta.
     *
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
        return getDoctoServicoDAO().findDoctoServicoWithServicoPrioritario(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega);
    }

    /**
     * Consulta paginada que retorna documentos de serviço com serviço prioritário a a partir de informações de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        return getDoctoServicoDAO().findPaginatedDoctoServicoWithServicoPrioritario(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, findDefinition);
    }

    /**
     * Retorna os documentos de servico que tenha como destino a rota de viagem especificada
     * Retorna apenas os documentos que tenham sua DPE (data previsao entegra) igual ou menor que a data de hoje.
     *
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
        return getDoctoServicoDAO().findDoctoServicoWithDpeAtrasado(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega);
    }

    /**
     * Consulta paginada que retorna documentos de serviço com DPE atrasado a a partir de informações de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        return getDoctoServicoDAO().findPaginatedDoctoServicoWithDpeAtrasado(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, findDefinition);
    }


    /**
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoWithWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
        return getDoctoServicoDAO().findDoctoServicoWithWithPriorizacaoEmbarque(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega);
    }

    /**
     * Consulta paginada para Documentos de Servico que possuem priorização de embarque, a partir de informações de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        return getDoctoServicoDAO().findPaginatedDoctoServicoWithPriorizacaoEmbarque(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, findDefinition);
    }

    /**
     * Faz uma busca paginada retornando os documentos de serviço de um determinado controle de carga onde não foram carregados todos os volumes
     * do documento, isto é, onde a quantidades de volumes do documento é diferente da quantidade de volumes carregados no controle de carga
     *
     * @param idControleCarga
     * @param findDef
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDivergenciaCarregamento(Long idControleCarga, FindDefinition findDef) {
        return getDoctoServicoDAO().findPaginatedDoctoServicoWithDivergenciaCarregamento(idControleCarga, findDef);
    }

    /**
     * Obtém a filial origem do documento de servico
     *
     * @param idDoctoServico
     * @return
     */
    public DoctoServico findByIdJoinFilial(Long idDoctoServico) {
        return getDoctoServicoDAO().findByIdJoinFilial(idDoctoServico);
    }

    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do manifesto de viagem de nacional
     *
     * @param idManifestoViagemNacional
     * @return
     */
    public List<Map<String, Object>> findDocumentosByManifestoViagemNacional(Long idManifestoViagemNacional) {
        return getDoctoServicoDAO().findDocumentosByManifestoViagemNacional(idManifestoViagemNacional);
    }

    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do manifesto de entrega
     *
     * @param idManifestoEntrega
     * @return
     */
    public List<Map<String, Object>> findDocumentosByManifestoEntrega(Long idManifestoEntrega) {
        return getDoctoServicoDAO().findDocumentosByManifestoEntrega(idManifestoEntrega);
    }

    /**
     * Consulta os documentos de servico ('CRT','CTR','NFT','RRE','MDA'), a partir do
     * id do manifesto de entrega
     *
     * @param idManifesto
     * @return
     */
    public List<DoctoServico> findDoctosServicoByIdManifestoEntrega(Long idManifesto) {
        return getDoctoServicoDAO().findDoctosServicoByIdManifestoEntrega(idManifesto);
    }


    public boolean validateDoctoServicoByIdManifestoIdDoctoServico(Long idManifesto, Long idControleCarga, Long idDoctoServico) {
        return getDoctoServicoDAO().validateDoctoServicoByIdManifestoEntregaIdDoctoServico(idManifesto, idControleCarga, idDoctoServico);
    }

    /**
     * Busca todos os documentos de servico mais a localizacao da mercadoria
     * a partir do id do Manifesto de entrega. (utilizado na portaria)
     * Os documentos buscados sao: 'CRT','CTR','NFT','RRE','MDA'
     */
    public List<Map<String, Object>> findDoctosServicoLocalizacaoByIdManifestoEntrega(Long idManifesto) {
        return getDoctoServicoDAO().findDoctosServicoLocalizacaoByIdManifestoEntrega(idManifesto);
    }

    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do conhecimento
     *
     * @param idConhecimento
     * @return
     */
    public List<Map<String, Object>> findDocumentosByConhecimento(Long idConhecimento) {
        return getDoctoServicoDAO().findDocumentosByConhecimento(idConhecimento);
    }

    /**
     * Método que busca a paginação de documentos de serviço pela localização mercadoria, pelo bloqueio e
     * criterios de filtro da página
     *
     * @param idsLocalizacao
     * @param bloqueado
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByLocalizacaoMercadoria(
            List<Long> idsLocalizacao,
            Boolean bloqueado,
            TypedFlatMap criteria,
            List<Long> idsDoctoServico
    ) {
        return this.getDoctoServicoDAO().findPaginatedDoctoServicoByLocalizacaoMercadoria(
                idsLocalizacao,
                bloqueado,
                criteria,
                idsDoctoServico,
                FindDefinition.createFindDefinition(criteria)
        );
    }

    /**
     * Método que busca uma list de documentos de serviço pela localização mercadoria, pelo bloqueio e
     * criterios de filtro da página
     *
     * @param idsLocalizacao
     * @param bloqueado
     * @param criteria
     * @return
     */
    public List<DoctoServico> findListDoctoServicoByLocalizacaoMercadoria(
            List<Long> idsLocalizacao,
            Boolean bloqueado,
            TypedFlatMap criteria,
            List<Long> idsDoctoServico
    ) {
        return this.getDoctoServicoDAO().findListDoctoServicoByLocalizacaoMercadoria(idsLocalizacao, bloqueado, criteria, idsDoctoServico);
    }

    /**
     * FindPaginated que obtém os documentos de servico de determinado manifesto
     *
     * @param tfm
     * @return
     */
    public ResultSetPage findPaginatedDocumentosManifesto(TypedFlatMap tfm) {
        return getDoctoServicoDAO().findPaginatedDocumentosManifesto(tfm, FindDefinition.createFindDefinition(tfm));
    }

    /**
     * GetRowCount que obtém a quantidade de documentos de servico de determinado manifesto
     *
     * @param tfm
     * @return
     */
    public Integer getRowCountDocumentosManifesto(TypedFlatMap tfm) {
        return getDoctoServicoDAO().getRowCountDocumentosManifesto(tfm);
    }

    public boolean findEventoByDoctoServico(Long idDoctoServico, String tpDocumento) {
        return getDoctoServicoDAO().findEventoByDoctoServico(idDoctoServico, tpDocumento);
    }

    public boolean findLocalizacaoMercByDoctoServico(Long idDoctoServico, String dsLocalizacaoMercadoria) {
        return getDoctoServicoDAO().findLocalizacaoMercByDoctoServico(idDoctoServico, dsLocalizacaoMercadoria);
    }

    public boolean findLocalizacaoMercCANByDoctoServico(Long idDoctoServico, Short cdLocalizacaoMercadoria) {
        return getDoctoServicoDAO().findLocalizacaoMerCANByDoctoServico(idDoctoServico, cdLocalizacaoMercadoria);
    }

    public boolean findLocalizacaoMercadoriaByidDoctoServicoCdLocalizacao(Long idDoctoServico, Short cdLocalizacaoMercadoria) {
        return getDoctoServicoDAO().findLocalizacaoMercadoriaByidDoctoServicoCdLocalizacao(idDoctoServico, cdLocalizacaoMercadoria);
    }

    public List<Map<String, Object>> findRotaColetaEntregaById(Long idDoctoServico) {
        return getDoctoServicoDAO().findRotaColetaEntregaById(idDoctoServico);
    }

    /**
     * Método que retorna um DoctoServico com fetch de alguns objetos a partir de um ID de DoctoServico.
     *
     * @param idDoctoServico
     * @return DoctoServico
     */
    public DoctoServico findDoctoServicoById(Long idDoctoServico) {
        return this.getDoctoServicoDAO().findDoctoServicoById(idDoctoServico);
    }

    /**
     * Método que retorna um DoctoServico usando como filtro o Tipo de Documento,
     * a Filial de Origem o Número e a idFilialLocalizacao.
     *
     * @param tpDocumento
     * @param idFilialOrigem
     * @param nrDoctoServico
     * @return DoctoServico
     */
    public DoctoServico findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServico(
            String tpDocumento, Long idFilialOrigem, Long nrDoctoServico) {
        return this.getDoctoServicoDAO().findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
                tpDocumento, idFilialOrigem, nrDoctoServico, null);
    }

    /**
     * Método que retorna um DoctoServico usando como filtro o Tipo de Documento,
     * a Filial de Origem o Número e a idFilialLocalizacao.
     *
     * @param tpDocumento
     * @param idFilialOrigem
     * @param nrDoctoServico
     * @param idFilialLocalizacao
     * @return DoctoServico
     */
    public DoctoServico findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
            String tpDocumento, Long idFilialOrigem, Long nrDoctoServico, Long idFilialLocalizacao) {
        return this.getDoctoServicoDAO().findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
                tpDocumento, idFilialOrigem, nrDoctoServico, idFilialLocalizacao);
    }

    public DoctoServico findByIdJoinProcessoSinistro(Long idDoctoServico) {
        return getDoctoServicoDAO().findByIdJoinProcessoSinistro(idDoctoServico);
    }

    /**
     * Executa a mesma consulta da tela de pesquisa e devolver arquivo .csv com os dados.
     *
     * @param criteria
     * @return
     */
    public File executeExportacaoCsv(TypedFlatMap criteria, File reportOutputDir) {
        ResultSetPage rsp = localizacaoMercadoriaDAO.findPaginatedConsultaLocalizacaoMercadoria(criteria, new FindDefinition(1,
                MAXIMO_REGISTROS_RELATORIO_CSV, Collections.emptyList()));

        if (rsp.getList() != null && !rsp.getList().isEmpty()) {
            List<Map<String, Object>> listaParaCsv = populateReportCsvMap(rsp.getList());
            return generateReportFile(listaParaCsv, reportOutputDir);
        } else {
            throw new BusinessException("emptyReport");
        }
    }

    /**
     * Cria lista com os campos do relatório .csv, a ordem de inclusão na lista
     * é a ordem de apresentação no relatório.
     *
     * @param rs
     * @return
     */
    private List<Map<String, Object>> populateReportCsvMap(List<Object[]> rs) {
        List<Map<String, Object>> listaParaCsv = new ArrayList<Map<String, Object>>();

        for (Object[] obj : rs) {
            Map<String, Object> registro = new LinkedHashMap<String, Object>();

            registro.put(configuracoesFacade.getMensagem("documentoServico"), domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",
                    obj[LocalizacaoMercadoriaDAO.TP_DOCUMENTO_SERVICO].toString()) + " " + obj[LocalizacaoMercadoriaDAO.SG_FILIAL_OR] + " " + FormatUtils.completaDados(obj[LocalizacaoMercadoriaDAO.NR_DOCTO_SERVICO], "0", 8, 0, true));

            if (obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO] != null) {
                registro.put(configuracoesFacade.getMensagem("finalidade"), domainValueService.findDomainValueDescription("DM_TIPO_CONHECIMENTO", obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO].toString()));
            } else {
                registro.put(configuracoesFacade.getMensagem("finalidade"), "");
            }

            registro.put(configuracoesFacade.getMensagem("filialDestino2"), obj[LocalizacaoMercadoriaDAO.SG_FILIAL_DEST]);

            String serieNroAwb = "";
            if (obj[LocalizacaoMercadoriaDAO.DS_SERIE_AWB] != null) {
                serieNroAwb += (String) obj[LocalizacaoMercadoriaDAO.DS_SERIE_AWB] + " / ";
            }
            serieNroAwb += AwbUtils.formatNrAwb((Long) obj[LocalizacaoMercadoriaDAO.NR_AWB], (Integer) obj[LocalizacaoMercadoriaDAO.DV_AWB]);
            registro.put(configuracoesFacade.getMensagem("serieNumAWBCiaAerea"), serieNroAwb);

            registro.put(configuracoesFacade.getMensagem("ciaAerea"), obj[LocalizacaoMercadoriaDAO.SG_CIA_AEREA_AWB]);

            if (obj[LocalizacaoMercadoriaDAO.DH_EMISSAO] != null) {
                registro.put(configuracoesFacade.getMensagem("dataEmissao"), ((DateTime) obj[LocalizacaoMercadoriaDAO.DH_EMISSAO]).toString("dd/MM/yyyy HH:mm ZZ"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dataEmissao"), "");
            }

            if (obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA] != null) {
                registro.put(configuracoesFacade.getMensagem("dataPrevista"), ((YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA]).toString("dd/MM/yyyy"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dataPrevista"), "");
            }

            if (obj[LocalizacaoMercadoriaDAO.DT_SAIDA_VIAGEM] != null) {
                registro.put(configuracoesFacade.getMensagem("dtSaidaViagem"), ((DateTime) obj[LocalizacaoMercadoriaDAO.DT_SAIDA_VIAGEM]).toString("dd/MM/yyyy HH:mm ZZ"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dtSaidaViagem"), "");
            }

            if (obj[LocalizacaoMercadoriaDAO.DT_CHEGADA_VIAGEM] != null) {
                registro.put(configuracoesFacade.getMensagem("dtChegadaViagem"), ((DateTime) obj[LocalizacaoMercadoriaDAO.DT_CHEGADA_VIAGEM]).toString("dd/MM/yyyy HH:mm ZZ"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dtChegadaViagem"), "");
            }

            if (obj[LocalizacaoMercadoriaDAO.DT_SAIDA_ENT_MAN] != null) {
                registro.put(configuracoesFacade.getMensagem("dtSaidaEntrega"), ((DateTime) obj[LocalizacaoMercadoriaDAO.DT_SAIDA_ENT_MAN]).toString("dd/MM/yyyy HH:mm ZZ"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dtSaidaEntrega"), "");
            }

            if (obj[LocalizacaoMercadoriaDAO.DT_ENTREGA] != null) {
                registro.put(configuracoesFacade.getMensagem("dataEntrega"), ((DateTime) obj[LocalizacaoMercadoriaDAO.DT_ENTREGA]).toString("dd/MM/yyyy HH:mm ZZ"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dataEntrega"), "");
            }

            registro.put(configuracoesFacade.getMensagem("localizacao"), obj[LocalizacaoMercadoriaDAO.LOCALIZACAO]);

            if (obj[LocalizacaoMercadoriaDAO.NF] != null) {
                registro.put(configuracoesFacade.getMensagem("notaFiscal"), FormatUtils.completaDados(obj[LocalizacaoMercadoriaDAO.NF], "0", 8, 0, true));
            } else {
                registro.put(configuracoesFacade.getMensagem("notaFiscal"), "");
            }

            String identificacaoRemetente = "";
            if (obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM] != null && obj[LocalizacaoMercadoriaDAO.NR_IDENT_REM] != null) {
                identificacaoRemetente = (String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM] + " ";
                identificacaoRemetente += FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_REM].toString());
            }
            registro.put(configuracoesFacade.getMensagem("identificacaoRemetente"), identificacaoRemetente);
            registro.put(configuracoesFacade.getMensagem("remetente"), obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REM]);

            String identificacaoDestinatario = "";
            if (obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST] != null && obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEST] != null) {
                identificacaoDestinatario = (String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST] + " ";
                identificacaoDestinatario += FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEST].toString());
            }
            registro.put(configuracoesFacade.getMensagem("identificacaoDestinatario"), identificacaoDestinatario);
            registro.put(configuracoesFacade.getMensagem("destinatario"), obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEST]);

            String identificacaoConsignatario = "";
            if (obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI] != null && obj[LocalizacaoMercadoriaDAO.NR_IDENT_CONSI] != null) {
                identificacaoConsignatario = (String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI] + " ";
                identificacaoConsignatario += FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_CONSI].toString());
            }
            registro.put(configuracoesFacade.getMensagem("identificacaoConsignatario"), identificacaoConsignatario);
            registro.put(configuracoesFacade.getMensagem("consignatario"), obj[LocalizacaoMercadoriaDAO.NM_PESSOA_CONSI]);

            String identificacaoRedespacho = "";
            if (obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES] != null && obj[LocalizacaoMercadoriaDAO.NR_IDENT_REDES] != null) {
                identificacaoRedespacho = (String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES] + " ";
                identificacaoRedespacho += FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_REDES].toString());
            }
            registro.put(configuracoesFacade.getMensagem("identificacaoRedespacho"), identificacaoRedespacho);
            registro.put(configuracoesFacade.getMensagem("redespacho"), obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REDES]);

            String identificacaoResponsavelFrete = "";
            if (obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV] != null && obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEV] != null) {
                identificacaoResponsavelFrete = (String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV] + " ";
                identificacaoResponsavelFrete += FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEV].toString());
            }
            registro.put(configuracoesFacade.getMensagem("identificacaoResponsavelFrete"), identificacaoResponsavelFrete);
            registro.put(configuracoesFacade.getMensagem("responsavelFrete"), obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEV]);

            if (obj[LocalizacaoMercadoriaDAO.DT_AGEN] != null) {
                registro.put(configuracoesFacade.getMensagem("dataAgendamento"), ((YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_AGEN]).toString("dd/MM/yyyy"));
            } else if (obj[LocalizacaoMercadoriaDAO.DT_AGEND_MONIT] != null) {
                registro.put(configuracoesFacade.getMensagem("dataAgendamento"), ((YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_AGEND_MONIT]).toString("dd/MM/yyyy"));
            } else {
                registro.put(configuracoesFacade.getMensagem("dataAgendamento"), "");
            }

            listaParaCsv.add(registro);
        }

        return listaParaCsv;
    }

    /**
     * @param listaParaCsv
     * @param reportOutputDir
     * @return
     */
    private File generateReportFile(List<Map<String, Object>> listaParaCsv, File reportOutputDir) {
        String arquivoCsv = CsvUtils.convertMappedListToCsv(listaParaCsv, ";");
        try {
            File reportFile = File.createTempFile("report", ".csv", reportOutputDir);
            FileOutputStream out = new FileOutputStream(reportFile);
            out.write(arquivoCsv.getBytes());
            out.flush();
            out.close();
            return reportFile;
        } catch (IOException e) {
            throw new InfrastructureException(e);
        }
    }

    /**
     * findPaginated da tela Consultar Localização de mercadoria
     *
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedConsultaLocalizacaoMercadoria(TypedFlatMap criteria) {
        ResultSetPage rsp = localizacaoMercadoriaDAO.findPaginatedConsultaLocalizacaoMercadoria(criteria, FindDefinition.createFindDefinition(criteria));
        List<Object[]> rs = rsp.getList();
        if (!rs.isEmpty()) {
            List<TypedFlatMap> listaNova = new ArrayList<TypedFlatMap>(rs.size());
            for (Object[] obj : rs) {
                TypedFlatMap registro = new TypedFlatMap();
                registro.put(ConstantesExpedicao.ID_DOCTO_SERVICO, obj[LocalizacaoMercadoriaDAO.ID_DOCTO_SERVICO]);
                registro.put("doctoServico", domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",
                        obj[LocalizacaoMercadoriaDAO.TP_DOCUMENTO_SERVICO].toString()) + " " + obj[LocalizacaoMercadoriaDAO.SG_FILIAL_OR] + " " + FormatUtils.completaDados(obj[LocalizacaoMercadoriaDAO.NR_DOCTO_SERVICO], "0", 8, 0, true));
                registro.put("nrFormulario", obj[LocalizacaoMercadoriaDAO.NR_FORMULARIO]);
                if (obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO] != null){
                    registro.put("finalidade", domainValueService.findDomainValueDescription("DM_TIPO_CONHECIMENTO", obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO].toString()));
                }

                registro.put("sgFilialDestino", obj[LocalizacaoMercadoriaDAO.SG_FILIAL_DEST]);
                registro.put(ConstantesExpedicao.DH_EMISSAO, obj[LocalizacaoMercadoriaDAO.DH_EMISSAO]);
                registro.put("dtEntrega", obj[LocalizacaoMercadoriaDAO.DT_ENTREGA]);
                registro.put("dsLocMerc", obj[LocalizacaoMercadoriaDAO.LOCALIZACAO]);
                registro.put("nf", obj[LocalizacaoMercadoriaDAO.NF]);
                registro.put("remTpIdentificacao", obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM]);
                if (obj[LocalizacaoMercadoriaDAO.NR_IDENT_REM] != null){
                    registro.put("remNrIdentificacao", FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_REM].toString()));
                    }
                registro.put("remNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REM]);

                registro.put("destTpIdentificacao", obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST]);
                if (obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEST] != null) {
                    registro.put("destNrIdentificacao", FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEST].toString()));
                }
                registro.put("destNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEST]);

                registro.put("consTpIdentificacao", obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI]);
                if (obj[LocalizacaoMercadoriaDAO.NR_IDENT_CONSI] != null) {
                    registro.put("consNrIdentificacao", FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_CONSI].toString()));
                }
                registro.put("consNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_CONSI]);

                registro.put("redesTpIdentificacao", obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES]);
                if (obj[LocalizacaoMercadoriaDAO.NR_IDENT_REDES] != null) {
                    registro.put("redesNrIdentificacao", FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_REDES].toString()));
                }
                registro.put("redesNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REDES]);

                registro.put("respTpIdentificacao", obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV]);
                if (obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEV] != null) {
                    registro.put("respNrIdentificacao", FormatUtils.formatIdentificacao(obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV].toString(), obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEV].toString()));
                }
                registro.put("respNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEV]);

                Object dtAgend = obj[LocalizacaoMercadoriaDAO.DT_AGEN];
                if (dtAgend == null) {
                    dtAgend = obj[LocalizacaoMercadoriaDAO.DT_AGEND_MONIT];
                }
                registro.put("dtAgen", dtAgend);

                if (obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA] != null) {
                    registro.put("dtPrevista", obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA]);
                }

                if (obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA] != null && obj[LocalizacaoMercadoriaDAO.DT_ENTREGA] == null) {
                    YearMonthDay dtPrevEntrega = (YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA];

                    registro.put("corBola", (JTDateTimeUtils.comparaData(dtPrevEntrega, JTDateTimeUtils.getDataAtual()) > 0)?
                            "Verde": (JTDateTimeUtils.comparaData(dtPrevEntrega, JTDateTimeUtils.getDataAtual()) < 0)?
                            "Vermelha" : "Amarela");
                }

                String serieNroAwb = "";
                if (obj[LocalizacaoMercadoriaDAO.DS_SERIE_AWB] != null) {
                    serieNroAwb += (String) obj[LocalizacaoMercadoriaDAO.DS_SERIE_AWB] + " / ";
                }
                serieNroAwb += AwbUtils.formatNrAwb((Long) obj[LocalizacaoMercadoriaDAO.NR_AWB], (Integer) obj[LocalizacaoMercadoriaDAO.DV_AWB]);
                registro.put("serieNroAwb", serieNroAwb);

                registro.put("nmCiaAereaAwb", obj[LocalizacaoMercadoriaDAO.SG_CIA_AEREA_AWB]);

                registro.put("dtSaidaViagem", obj[LocalizacaoMercadoriaDAO.DT_SAIDA_VIAGEM]);
                registro.put("dtChegadaViagem", obj[LocalizacaoMercadoriaDAO.DT_CHEGADA_VIAGEM]);
                registro.put("dtSaidaEntrega", obj[LocalizacaoMercadoriaDAO.DT_SAIDA_ENT_MAN]);

                listaNova.add(registro);
            }
            rsp.setList(listaNova);
        }
        return rsp;
    }

    /**
     * findPaginated da tela Consultar Localização de mercadoria nova ui
     *
     * @param criteria
     * @return
     */
    public List<Map<String, Object>> findPaginatedConsultaLocalizacaoMercadoriaMap(TypedFlatMap criteria) {

        ResultSetPage rsp = localizacaoMercadoriaDAO.findPaginatedConsultaLocalizacaoMercadoria(criteria, FindDefinition.createFindDefinition(criteria));
        return new ListToMapConverter<Object[]>().mapRows(rsp.getList(), new RowMapper<Object[]>() {
            @Override
            public Map<String, Object> mapRow(Object[] obj) {
                Map<String, Object> map = new HashMap<String, Object>();

                map.put(ConstantesExpedicao.ID_DOCTO_SERVICO, obj[LocalizacaoMercadoriaDAO.ID_DOCTO_SERVICO]);

                map.put("dsDoctoServico", domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", obj[LocalizacaoMercadoriaDAO.TP_DOCUMENTO_SERVICO].toString()) + " " + obj[LocalizacaoMercadoriaDAO.SG_FILIAL_OR] + " " + FormatUtils.completaDados(obj[LocalizacaoMercadoriaDAO.NR_DOCTO_SERVICO], "0", 8, 0, true));

                if (obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO] != null) {
                    map.put("finalidade", domainValueService.findDomainValueDescription("DM_TIPO_CONHECIMENTO", obj[LocalizacaoMercadoriaDAO.TP_CONHECIMENTO].toString()));
                }

                map.put("sgFilialDestino", obj[LocalizacaoMercadoriaDAO.SG_FILIAL_DEST]);

                map.put("nrCae", obj[LocalizacaoMercadoriaDAO.NR_CAE] != null ? obj[LocalizacaoMercadoriaDAO.SG_FILIAL_OR] + " " + StringUtils.leftPad((String) obj[LocalizacaoMercadoriaDAO.NR_CAE], 8, "0") : "");

                if (obj[LocalizacaoMercadoriaDAO.ID_AWB] != null) {
                    if ((Long) obj[LocalizacaoMercadoriaDAO.NR_AWB] > 0) {
                        map.put("serieNroAwb", obj[LocalizacaoMercadoriaDAO.SG_CIA_AEREA_AWB] + " " +
                                AwbUtils.getNrAwbFormated((String) obj[LocalizacaoMercadoriaDAO.DS_SERIE_AWB],
                                        (Long) obj[LocalizacaoMercadoriaDAO.NR_AWB], (Integer) obj[LocalizacaoMercadoriaDAO.DV_AWB]));

                    } else {
                        map.put("serieNroAwb", obj[LocalizacaoMercadoriaDAO.SG_CIA_AEREA_AWB] + " " + (Long) obj[LocalizacaoMercadoriaDAO.ID_AWB]);
                    }
                }

                map.put(ConstantesExpedicao.DH_EMISSAO, obj[LocalizacaoMercadoriaDAO.DH_EMISSAO]);
                map.put("dtPrevista", JTDateTimeUtils.yearMonthDayToDateTimeNullSafe((YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA]));
                map.put("dtSaidaViagem", obj[LocalizacaoMercadoriaDAO.DT_SAIDA_VIAGEM]);
                map.put("dtChegadaViagem", obj[LocalizacaoMercadoriaDAO.DT_CHEGADA_VIAGEM]);
                map.put("dtSaidaEntrega", obj[LocalizacaoMercadoriaDAO.DT_SAIDA_ENT_MAN]);

                map.put("dtEntrega", obj[LocalizacaoMercadoriaDAO.DT_ENTREGA]);
                map.put("dsLocMerc", obj[LocalizacaoMercadoriaDAO.LOCALIZACAO]);
                map.put("nf", obj[LocalizacaoMercadoriaDAO.NF]);

                map.put("remDsIdentificacao", FormatUtils.formatIdentificacaoComTipo((String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_REM], (String) obj[LocalizacaoMercadoriaDAO.NR_IDENT_REM]));
                map.put("remNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REM]);

                map.put("destDsIdentificacao", FormatUtils.formatIdentificacaoComTipo((String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEST], (String) obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEST]));
                map.put("destNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEST]);

                map.put("consDsIdentificacao", FormatUtils.formatIdentificacaoComTipo((String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_CONSI], (String) obj[LocalizacaoMercadoriaDAO.NR_IDENT_CONSI]));
                map.put("consNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_CONSI]);

                map.put("redesDsIdentificacao", FormatUtils.formatIdentificacaoComTipo((String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_REDES], (String) obj[LocalizacaoMercadoriaDAO.NR_IDENT_REDES]));
                map.put("redesNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_REDES]);

                map.put("respDsIdentificacao", FormatUtils.formatIdentificacaoComTipo((String) obj[LocalizacaoMercadoriaDAO.TP_IDENT_DEV], (String) obj[LocalizacaoMercadoriaDAO.NR_IDENT_DEV]));
                map.put("respNmPessoa", obj[LocalizacaoMercadoriaDAO.NM_PESSOA_DEV]);

                map.put("dtAgen", JTDateTimeUtils.yearMonthDayToDateTimeNullSafe((YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_AGEN]));

                String cor = null;

                if (obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA] != null && obj[LocalizacaoMercadoriaDAO.DT_ENTREGA] == null) {
                    YearMonthDay dtPrevEntrega = (YearMonthDay) obj[LocalizacaoMercadoriaDAO.DT_PREV_ENTREGA];
                    if (JTDateTimeUtils.comparaData(dtPrevEntrega, JTDateTimeUtils.getDataAtual()) > 0){
                        cor = "verde";
                    } else if (JTDateTimeUtils.comparaData(dtPrevEntrega, JTDateTimeUtils.getDataAtual()) < 0){
                        cor = "vermelha";
                    } else {
                        cor = "amarela";
                }
                }

                if (cor != null && cor.length() > 0) {
                    map.put("corBola", "bola-" + cor.toLowerCase());
                }

                return map;
            }
        });
    }

    /**
     * rowCount da tela Consultar Localização de mercadoria
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountConsultaLocalizacaoMercadoria(TypedFlatMap criteria) {
        return localizacaoMercadoriaDAO.getRowCountConsultaLocalizacaoMercadoria(criteria);
    }

    /**
     * findById da tela Consultar Localização de mercadoria
     *
     * @param criteria
     * @return
     */
    public Map findByIdDSByLocalizacaoMercadoria(TypedFlatMap criteria) {
        Object[] obj = (Object[]) localizacaoMercadoriaDAO.findByIdDSByLocalizacaoMercadoria(criteria);
        if (obj == null) {
            return null;
        }

        DomainValue tpDocumentoServico = null;
        DomainValue tpConhecimento = null;
        DomainValue tpFrete = null;
        DomainValue tpSituacaoDocumento = null;
        if (obj[0] != null) {
            tpDocumentoServico = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", (String) obj[0]);
        }
        if (obj[7] != null) {
            tpConhecimento = domainValueService.findDomainValueByValue("DM_TIPO_CONHECIMENTO", (String) obj[7]);
        }
        if (obj[8] != null) {
            tpFrete = domainValueService.findDomainValueByValue("DM_TIPO_FRETE", (String) obj[8]);
        }
        if (obj[0] != null) {
            tpDocumentoServico = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", (String) obj[0]);
        }
        if (obj[28] != null) {
            tpSituacaoDocumento = domainValueService.findDomainValueByValue("DM_SITUACAO_DOC_ELETRONICO", (String) obj[28]);
        }


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tpDocumentoServico", tpDocumentoServico.getDescription().toString());
        map.put("tpDocumentoServicoLinkProperty", tpDocumentoServico.getValue());

        map.put("nrCae", obj[31] != null ? obj[5] + " " + StringUtils.leftPad((String) obj[31], 8, "0") : "");

        map.put("nrDoctoServico", obj[1]);
        map.put(ConstantesExpedicao.ID_DOCTO_SERVICO, obj[2]);
        if ((DateTime) obj[3] != null){
            map.put("dsDhEmissao", JTFormatUtils.format((DateTime) obj[3]));
            map.put("dhEmissaoFormatado", JTFormatUtils.format((DateTime) obj[3], "yyyyMMdd"));
        }
        if ((BigDecimal) obj[4] != null) {
            String vlParcela = (String) obj[10] + " " + FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) obj[4]);
            map.put("vlTotalParcelas", vlParcela);
        }
        map.put("dsSgFilial", obj[5]);
        map.put("dsServico", obj[6]);
        if (tpConhecimento != null) {
            map.put("tpConhecimento", tpConhecimento.getDescription().toString());
        }
        if (tpFrete != null) {
            map.put("tpFrete", tpFrete.getDescription().toString());
        }

        if (tpSituacaoDocumento != null) {
            map.put("tpSituacaoDocumento", tpSituacaoDocumento.getDescription().toString());
        }

        map.put("filialDestino", obj[9]);
        map.put("nmFantasiaDestino", obj[11]);
        map.put("dvConhecimento", obj[12]);
        map.put("dsLocalMercadoria", obj[13]);

        if (obj[24] != null && !"".equals(obj[24].toString())) {
            map.put("nrIdentificacaoReme", FormatUtils.formatIdentificacao((String) obj[24], (String) obj[14]));
        }

        map.put("nmPessoaReme", obj[15]);
        map.put("idPessoaReme", obj[16]);

        if (obj[25] != null && !"".equals(obj[25].toString())) {
            map.put("nrIdentificacaoDest", FormatUtils.formatIdentificacao(obj[25].toString(), obj[17].toString()));
        }

        map.put("nmPessoaDest", obj[18]);
        map.put("idPessoaDest", obj[19]);

        if ((Long) obj[22] != null) {
            map.put("nrIdentificacaoConsi", obj[20]);
            map.put("nmPessoaConsi", obj[21]);
            map.put("idPessoaConsi", obj[22]);
        }
        map.put("idFilial", obj[23]);
        map.put("tpSituacaoConhecimento", obj[26]);
        map.put("cdFilial", obj[27]);

        map.put("sgFilialLocalizacao", obj[29]);
        map.put("nmFilialLocalizacao", obj[30]);

        map.put("idEmpresaFilialOrigem", obj[32]);
        return map;
    }

    /**
     * findById da tela Consultar Localização de mercadoria
     *
     * @param criteria
     * @return
     */

    public Map<String, Object> findByIdDSByLocalizacaoMercadoriaPrincipal(TypedFlatMap criteria) {
        Object[] obj = (Object[]) localizacaoMercadoriaDAO.findByIdDSByLocalizacaoMercadoriaPrincipal(criteria);
        Map<String, Object> map = new HashMap<String, Object>();
        DomainValue tpDensidade = null;
        DomainValue tpIdentificacaoRem = null;
        DomainValue tpIdentificacaoDest;
        if (obj[LocalizacaoMercadoriaDAO.DET_TP_DENSIDADE] != null) {
            tpDensidade = domainValueService.findDomainValueByValue("DM_DENSIDADES", (String) obj[LocalizacaoMercadoriaDAO.DET_TP_DENSIDADE]);
        }
        if (obj[LocalizacaoMercadoriaDAO.DET_TP_IDENT_REM] != null) {
            tpIdentificacaoRem = domainValueService.findDomainValueByValue("DM_TIPO_IDENTIFICACAO_PESSOA", (String) obj[LocalizacaoMercadoriaDAO.DET_TP_IDENT_REM]);
        }
        if (obj[LocalizacaoMercadoriaDAO.DET_TP_IDENT_DEST] != null) {
            tpIdentificacaoDest = domainValueService.findDomainValueByValue("DM_TIPO_IDENTIFICACAO_PESSOA", (String) obj[LocalizacaoMercadoriaDAO.DET_TP_IDENT_DEST]);
            map.put("tpIdentificacaoDest", tpIdentificacaoDest);
        }
        map.put("qtVolumes", obj[LocalizacaoMercadoriaDAO.DET_QT_VOLUMES]);
        map.put("psReal", obj[LocalizacaoMercadoriaDAO.DET_PS_REAL]);
        map.put("psAforado", obj[LocalizacaoMercadoriaDAO.DET_PS_AFORADO]);
        map.put("psReferenciaCalculo", obj[LocalizacaoMercadoriaDAO.DET_PS_REFERENCIAL]);
        map.put("dsEnderecoEntrega", obj[LocalizacaoMercadoriaDAO.DET_DS_END_ENTREGA]);
        map.put("sgFilialOriginal", obj[LocalizacaoMercadoriaDAO.DET_SG_FIL_OR]);
        map.put("nrDoctoServicoOriginal", obj[LocalizacaoMercadoriaDAO.DET_NR_DOCTO_OR]);
        map.put("sgFilialPedCol", obj[LocalizacaoMercadoriaDAO.DET_SG_FIL_PEDCOL]);
        map.put("nrColeta", obj[LocalizacaoMercadoriaDAO.DET_NR_COL]);
        map.put("nmPessoRem", obj[LocalizacaoMercadoriaDAO.DET_NM_PESSOA_REM]);
        map.put("nrIdentificacaoRem", obj[LocalizacaoMercadoriaDAO.DET_NR_IDENT_REM]);
        map.put("nmPessoDest", obj[LocalizacaoMercadoriaDAO.DET_NM_PESSOA_DEST]);
        map.put("nrIdentificacaoDest", obj[LocalizacaoMercadoriaDAO.DET_NR_IDENT_DEST]);
        map.put("nrNotaFiscal", obj[LocalizacaoMercadoriaDAO.DET_NR_NF]);
        map.put("qtNotaFiscal", obj[LocalizacaoMercadoriaDAO.DET_QT_NF]);
        map.put("dsRotaEntrega", obj[LocalizacaoMercadoriaDAO.DET_DS_ROTA]);
        map.put("nrFormulario", obj[LocalizacaoMercadoriaDAO.DET_NR_FORMULARIO]);
        map.put("nrFatorDensidade", obj[LocalizacaoMercadoriaDAO.DET_NR_FATOR_DENSIDADE]);
        map.put("nrDocumentoEletronico", obj[LocalizacaoMercadoriaDAO.DET_NR_DOCUMENTO_ELETRONICO]);
        map.put("tpDocumentoServico", obj[LocalizacaoMercadoriaDAO.DET_TP_DOCUMENTO_SERVICO]);
        map.put("psAferido", obj[LocalizacaoMercadoriaDAO.DET_PS_AFERIDO]);
        
        
        
        if (tpDensidade != null) {
            map.put("tpDensidade", tpDensidade.getDescription().toString());
        }
        map.put("dsNaturezaProduto", obj[LocalizacaoMercadoriaDAO.DET_DS_NATUREZA]);
        if (obj[LocalizacaoMercadoriaDAO.DET_ID_AWB] != null) {
            if (obj[LocalizacaoMercadoriaDAO.DET_NR_AWB] != null && (Long) obj[LocalizacaoMercadoriaDAO.DET_NR_AWB] > 0) {
                map.put("awb", (String) obj[LocalizacaoMercadoriaDAO.DET_SG_EMP_CIA] + " " + AwbUtils.getNrAwbFormated(
                        (String) obj[LocalizacaoMercadoriaDAO.DET_DS_SERIE],
                        (Long) obj[LocalizacaoMercadoriaDAO.DET_NR_AWB],
                        (Integer) obj[LocalizacaoMercadoriaDAO.DET_DV_AWB]));
            } else {
                map.put("awb", (String) obj[LocalizacaoMercadoriaDAO.DET_SG_EMP_CIA] + " " + (Long) obj[LocalizacaoMercadoriaDAO.DET_ID_AWB]);
            }
            map.put("tpStatusAwb", domainValueService.findDomainValueByValue(DM_STATUS_AWB, (String) obj[LocalizacaoMercadoriaDAO.DET_TP_STATUS_AWB]).getDescriptionAsString());
            map.put("nmEmpresaCiaAerea", obj[LocalizacaoMercadoriaDAO.DET_NM_EMP_CIA]);
        }

        map.put("dtPrevEntrega", obj[LocalizacaoMercadoriaDAO.DET_DT_PREV_ENTREGA]);
        map.put("dtNovaPrevisaoEntrega", obj[LocalizacaoMercadoriaDAO.DET_DT_NOVO_DPE_DOCTO_SERVICO]);
        map.put("nrDiasEntrega", obj[LocalizacaoMercadoriaDAO.DET_NR_DIAS_ENTREGA]);
        map.put("nrDiasRealEntrega", obj[LocalizacaoMercadoriaDAO.DET_NR_DIAS_ENTREGA_REAL]);
        map.put("nrDiasBloqueio", obj[LocalizacaoMercadoriaDAO.DET_NR_DIAS_BLOQUEIO]);
        map.put("nmRecebedor", obj[LocalizacaoMercadoriaDAO.DET_NM_RECEBEDOR]);
        map.put("dsLocalMercadoria", obj[LocalizacaoMercadoriaDAO.DET_DS_LOCAL_MERC]);

        map.put("dtAgendamento", obj[LocalizacaoMercadoriaDAO.DET_DT_AGEND]);
        map.put("dsTurno", obj[LocalizacaoMercadoriaDAO.DET_DS_TURNO]);
        map.put("hrPreferenciaInicial", obj[LocalizacaoMercadoriaDAO.DET_HR_INICIAL]);
        map.put("hrPreferenciaFinal", obj[LocalizacaoMercadoriaDAO.DET_HR_FINAL]);
        map.put("edColeta", obj[LocalizacaoMercadoriaDAO.DET_ED_COLETA]);
        map.put("municipioColeta", obj[LocalizacaoMercadoriaDAO.DET_MUN_COLETA]);
        map.put("nmUfColeta", obj[LocalizacaoMercadoriaDAO.DET_MUN_UF_COL]);
        map.put("sgUfColeta", obj[LocalizacaoMercadoriaDAO.DET_SG_UF_COL]);
        map.put("paisColeta", obj[LocalizacaoMercadoriaDAO.DET_PAIS_COL]);

        map.put("dhBaixa", obj[LocalizacaoMercadoriaDAO.DET_DH_BAIXA]);
        map.put("tpIdentificacaoRem", tpIdentificacaoRem);

        if (obj[LocalizacaoMercadoriaDAO.DET_SOLICITACAO_PRIORIZACAO] != null) {
            map.put("solicitacaoPriorizacao", obj[LocalizacaoMercadoriaDAO.DET_SOLICITACAO_PRIORIZACAO].toString());
        } else {
            map.put("solicitacaoPriorizacao", configuracoesFacade.getMensagem("nao"));
        }

        if (obj[LocalizacaoMercadoriaDAO.DET_SOLICITACAO_RETIRADA] != null) {
            map.put("solicitacaoRetirada", obj[LocalizacaoMercadoriaDAO.DET_SOLICITACAO_RETIRADA].toString());
        } else {
            map.put("solicitacaoRetirada", configuracoesFacade.getMensagem("nao"));
        }

        if ((BigDecimal) obj[LocalizacaoMercadoriaDAO.DET_VL_MERC] != null) {
            String valor = (String) obj[LocalizacaoMercadoriaDAO.DET_DS_SIMBOLO_M0EDA] + " " + FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) obj[LocalizacaoMercadoriaDAO.DET_VL_MERC]);
            map.put("valor", valor);
        }

        map.put("municipioEntrega", obj[LocalizacaoMercadoriaDAO.DET_MUN_ENTREGA]);
        map.put("nmUfEntrega", obj[LocalizacaoMercadoriaDAO.DET_MUN_UF_ENT]);
        map.put("sgUfEntrega", obj[LocalizacaoMercadoriaDAO.DET_SG_UF_ENT]);
        map.put("paisEntrega", obj[LocalizacaoMercadoriaDAO.DET_PAIS_ENT]);

        ConhecimentoFedex conhecimentoFedex = conhecimentoFedexService.findByIdConhecimento(criteria.getLong("idDoctoServico"));
        if (conhecimentoFedex != null){
        	map.put("nrConhecimentoFedex",conhecimentoFedex.getSiglaFilialOrigem()+" "+conhecimentoFedex.getNumeroConhecimento());
        }
        
		if (conhecimentoService.validateAgendamentoObrigatorio(criteria.getLong("idDoctoServico"))){
        	map.put("agendamentoObrigatorio", configuracoesFacade.getMensagem("sim"));
        } else {
        	map.put("agendamentoObrigatorio", configuracoesFacade.getMensagem("nao"));
        }

        return map;
    }

    public List<DoctoServico> findByIdControleCarga(Long idControleCarga) {
        return getDoctosServico(getDoctoServicoDAO().findByIdControleCarga(idControleCarga));
    }
    
    public List<DoctoServico> findByIdControleCargaAndIdCliente(Long idControleCarga, Long idCliente) {
        return getDoctosServico(getDoctoServicoDAO().findByIdControleCargaAndIdCliente(idControleCarga, idCliente));
    }

    private List<DoctoServico> getDoctosServico(List<DoctoServico> documentos) {
        if (documentos == null) {
            documentos = new ArrayList<DoctoServico>();
        }

        return documentos;
    }

    /**
     * findPaginated da tela Consultar Localização de mercadoria - aba Integrantes
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */

    public List<Map<String, Object>> findPaginatedIntegrantes(Long idDoctoServico) {
        return lmIntegranteDao.findPaginatedIntegrantes(idDoctoServico);
    }

    public void setLmIntegranteDao(LMIntegranteDAO lmIntegranteDao) {
        this.lmIntegranteDao = lmIntegranteDao;
    }

    /**
     * RowCount da tela de emitir carta mercadoria disposicao
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */
    public Integer getRowCountDoctoServicoWithNFConhecimento(Long idDoctoServico) {
        return this.getDoctoServicoDAO().getRowCountDoctoServicoWithNFConhecimento(idDoctoServico);
    }

    public Integer getRowCountDoctoServicoWithNFConhecimentoToInteger(Long idDoctoServico) {
        return this.getDoctoServicoDAO().getRowCountDoctoServicoWithNFConhecimentoToInteger(idDoctoServico);
    }

    /**
     * RowCount da tela de emitir carta mercadoria disposicao
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoWithNFConhecimento(Long idDoctoServico, FindDefinition findDefinition) {
        return this.getDoctoServicoDAO().findPaginatedDoctoServicoWithNFConhecimento(idDoctoServico, findDefinition);
    }

    /**
     * find da tela Consultar Localização de mercadoria
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */

    public List<Map<String, Object>> findComplementosOutros(Long idDoctoServico) {
        return lmComplementoDao.findComplementosOutros(idDoctoServico);
    }

    /**
     * Método que retorna a quantidade de dias úteis necessários para entrega do documento de serviço
     *
     * @param idDoctoServico
     * @param dtEntrega
     * @return qtdeDiasEntrega
     */
    public Integer findQtdeDiasUteisEntregaDocto(Long idDoctoServico, YearMonthDay dtEntrega) {
        DoctoServico doctoServico = findById(idDoctoServico);
        EnderecoPessoa enderecoEntrega = doctoServico.getEnderecoPessoa();
        Long idMunicipioEntrega = getIdMunicipioEntrega(enderecoEntrega, doctoServico);

        if (idMunicipioEntrega == null) {
            return null;
        }

        Long idMunicipioFilialDestino = doctoServico.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
        YearMonthDay dtPrazoEntrega = doctoServico.getDhEmissao().toYearMonthDay();

        if (dtEntrega == null) {
            dtEntrega = manifestoEntregaDocumentoService.findDtEntregaDoctoServico(idDoctoServico);
        }

        if (dtPrazoEntrega == null || dtEntrega == null) {
            return null;
        }

        return this.findQtdeDiasUteisEntrega(doctoServico, idMunicipioEntrega, idMunicipioFilialDestino, dtPrazoEntrega, dtEntrega);
    }

    private int findQtdeDiasUteisEntrega(DoctoServico doctoServico, Long idMunicipioEntrega, Long idMunicipioFilialDestino, YearMonthDay dtPrazoEntrega, YearMonthDay dtEntrega) {
        List<Long> idsMunicipio = getMunicipiosEntregaAndFilialDestino(idMunicipioEntrega, idMunicipioFilialDestino);
        
        Cliente clienteRemetente = doctoServico.getClienteByIdClienteRemetente();
        Cliente clienteDevedor = doctoServico.getClienteByIdClienteBaseCalculo();
        Boolean remetenteIgnoraFeriados = clienteRemetente.getBlDpeFeriado();
        Set<String> feriadosOrigem = findFeriadosSemFinaisDeSemana(idMunicipioEntrega, dtPrazoEntrega, dtEntrega);
        Set<String> feriadosDestino = findFeriadosSemFinaisDeSemana(idMunicipioFilialDestino, dtPrazoEntrega, dtEntrega);
        Boolean devedorEmiteDiaNaoUtil = clienteDevedor.getBlEmissaoDiaNaoUtil();
        Boolean devedorEmiteSabado = clienteDevedor.getBlEmissaoSabado();
        
        int qtdeDiasEntrega = dpeService.findQtdeDiasUteisEntreDatas(dtPrazoEntrega, dtEntrega, remetenteIgnoraFeriados, feriadosOrigem, feriadosDestino, devedorEmiteDiaNaoUtil, devedorEmiteSabado);
        int qtDiasAgendamento = this.findQtdDiasAgendamento(doctoServico.getIdDoctoServico(), idsMunicipio);
        Short qtDiasBloqueio = doctoServico.getNrDiasBloqueio();
        
        qtdeDiasEntrega = qtdeDiasEntrega - qtDiasAgendamento;
        
        if (qtDiasBloqueio != null) {
            qtdeDiasEntrega = qtdeDiasEntrega - qtDiasBloqueio;
        }

        if (qtdeDiasEntrega < 1) {
            qtdeDiasEntrega = 1;
        }
        
        return qtdeDiasEntrega;
    }

    private Long getIdMunicipioEntrega(EnderecoPessoa enderecoEntrega, DoctoServico doctoServico) {
        if (enderecoEntrega != null) {
            return enderecoEntrega.getMunicipio().getIdMunicipio();
        }

        String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();

        if (ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDocumentoServico)
                || ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumentoServico)
                || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoServico)
                || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
            return ((Conhecimento) doctoServico).getMunicipioByIdMunicipioEntrega().getIdMunicipio();
        }

        if (ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL.equals(tpDocumentoServico)) {
            return doctoServico.getFilialByIdFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
        }

        return null;
    }
    
    private List<Long> getMunicipiosEntregaAndFilialDestino(Long idMunicipioEntrega, Long idMunicipioFilialDestino) {
        List<Long> idsMunicipio = new ArrayList<Long>();
        
        if (idMunicipioEntrega != null) {
            idsMunicipio.add(idMunicipioEntrega);
        }
        
        if (idMunicipioFilialDestino != null) {
            idsMunicipio.add(idMunicipioFilialDestino);
        }
        
        return idsMunicipio;
    }
    
    private Set<String> findFeriadosSemFinaisDeSemana(Long idMunicipio, YearMonthDay dtEmissao, YearMonthDay dtChegada) {
        if (idMunicipio == null) {
            return new HashSet<String>();
        }

        Set<String> feriadosMap = new LinkedHashSet<String>();
        
        List<YearMonthDay> datasSemFinaisDeSemana = dpeService.findDatasSemFinaisDeSemana(dtEmissao, dtChegada);
        if (datasSemFinaisDeSemana == null || datasSemFinaisDeSemana.isEmpty()){
        	return feriadosMap;
        }
        List<YearMonthDay> feriados = feriadoService.findFeriadoByMunicipio(idMunicipio, datasSemFinaisDeSemana);
        
        for (YearMonthDay dtAtual : feriados) {
            feriadosMap.add(dtAtual.toString(DpeService.PATTERN_DATA));
        }

        return feriadosMap;
    }

    @SuppressWarnings("deprecation")
    public int findQtdDiasAgendamento(Long idDoctoServico, List<Long> idsMunicipio) {
        List<AgendamentoDoctoServico> listAgendamentosFechados = this.agendamentoDoctoServicoService.findAgendamentoByIdDoctoServicoAndTpSituacao(idDoctoServico, "F", "R", "C");

        if (CollectionUtils.isEmpty(listAgendamentosFechados)) {
            return 0;
        }

        int diasAgendamento = 0;

        for (AgendamentoDoctoServico agendamento : listAgendamentosFechados) {
            diasAgendamento = diasAgendamento + diaUtils.findQtdeDiasUteisEntreDatas(agendamento.getAgendamentoEntrega().getDhContato().toYearMonthDay(), agendamento.getAgendamentoEntrega().getDhFechamento().toYearMonthDay(), idsMunicipio);
        }

        return diasAgendamento;
    }

    /**
     * Método que recebe um TypedFlatMap com informacoes do docto de servico pesquisado e retorna uma list com varias informacoes do docto de servico - tela consultar localizacoes mercadoria
     *
     * @param criteria TypedFlatMap
     * @return List
     */

    public List<Map<String, Object>> findLookupCustomLocMerc(TypedFlatMap criteria) {
        return getDoctoServicoDAO().findLookupCustomLocMerc(criteria);
    }

    public List findLookupDoctoServico(final TypedFlatMap criteria) {
        return getDoctoServicoDAO().findLookupDoctoServico(criteria);
    }

    /**
     * Método que recebe um TypedFlatMap com informacoes do docto de servico pesquisado e retorna uma list com varias informacoes do docto de servico - tela informar regiao entrega
     *
     * @param criteria
     * @return List
     */

    public List<Map<String, Object>> findLookupCustomRotaColetaEnt(TypedFlatMap criteria) {
        return getDoctoServicoDAO().findLookupCustomRotaColetaEnt(criteria);
    }

    /**
     * somente para docto servico = RRE - Método que recebe um TypedFlatMap com informacoes do docto de servico pesquisado e retorna uma list com varias informacoes do docto de servico
     *
     * @param criteria TypedFlatMap
     * @return List
     */
    public List findLookupCustomReemb(TypedFlatMap criteria) {
        return getDoctoServicoDAO().findLookupCustomReemb(criteria);
    }

    public Filial findFilialDestinoOperacionalById(Long idDoctoServico) {
        return getDoctoServicoDAO().findFilialDestinoOperacionalById(idDoctoServico);
    }

    public DivisaoCliente findDivisaoClienteById(Long idDoctoServico) {
        return getDoctoServicoDAO().findDivisaoClienteById(idDoctoServico);
    }

    /**
     * Realiza o getRowCount da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @return
     */
    public Integer getRowCountDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico,
                                                                         Long idManifesto, Long idRotaIdaVolta) {

        return this.getDoctoServicoDAO().getRowCountDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta);
    }

    /**
     * Realiza o findPaginated da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico, Long idManifesto, Long idRotaIdaVolta, FindDefinition findDefinition) {
        ResultSetPage rsp = this.getDoctoServicoDAO().findPaginatedDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta, findDefinition);
        List<Map<String, Object>> rs = rsp.getList();
        List<TimeOfDay> result = null;
        for (Map<String, Object> element : rs) {
            idRotaIdaVolta = (Long) element.get("idRotaIdaVolta");

            if (idRotaIdaVolta != null) {
                result = this.getDoctoServicoDAO().findSubQueryHrSaida(idRotaIdaVolta);

                if (!result.isEmpty()) {
                    TimeOfDay time = result.get(0);
                    element.put("hrSaida", JTFormatUtils.format(time, JTFormatUtils.DEFAULT));
                }
            }
        }

        return rsp;
    }

    /**
     * Realiza a soma dos atributos encontrados pela pesquisa da tela de <b>ReceberDocumentosServicoCheckIn</b>
     * @param idDoctoServico
     * @param idFilialOrigem
     * @param tpDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @return
     */
    public List<Map<String, Object>> findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta(
            Long idDoctoServico,
            Long idFilialOrigem,
            String tpDoctoServico,
            Long idManifesto,
            Long idRotaIdaVolta
    ) {
        return this.getDoctoServicoDAO().findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta);
    }

    /**
     * Validate se o doctoServico em questao possui os requisitos necessarios para o processo de
     * check-in...
     *
     * @param idDoctoServico
     * @return
     */
    public boolean validateCaptureDoctoServico(Long idDoctoServico) {
        DoctoServico doctoServico = findById(idDoctoServico);

        if (doctoServico.getFluxoFilial() == null) {
            //Documento de Servico não possui Fluxo Filial.
            throw new BusinessException("LMS-05106");
        }
        if (doctoServico.getBlBloqueado().booleanValue()) {
            //Documento de Serviço está bloqueado.
            throw new BusinessException("LMS-05087");
        }

        if ((doctoServico.getFilialLocalizacao() == null)
                || (!doctoServico.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()))) {
            //O Documento de Serviço não encontra-se na filial.
            throw new BusinessException("LMS-05088");
        }

        if (doctoServico.getLocalizacaoMercadoria() == null) {
            throw new BusinessException("LMS-05089");
        } else if (!((doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("24"))) ||
                (doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("34"))))) {
            //Somente Documento de Serviço que estejam no 'Terminal' ou 'Em Descarga' podem ser incluídos em um Pré-Manifesto.
            throw new BusinessException("LMS-05089");
        }

        return true;
    }

    /**
     * Contem as regras de validacao da tela de <code>DoctoServicoCheckIn</code>.
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     */
    public void validateDocumentosServicoCheckin(Long idDoctoServico, Long idManifesto, Long idRotaIdaVolta) {
        DoctoServico doctoServico = findById(idDoctoServico);

        //Caso o campo de pre-manifesto tenha sido preenchido
        if (idManifesto != null) {
            Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
            if (manifesto.getTpAbrangencia() != null) {
                if ("N".equals(manifesto.getTpAbrangencia().getValue()) &&
                        (!"MDA".equals(doctoServico.getTpDocumentoServico().getValue()) &&
                                !"CTR".equals(doctoServico.getTpDocumentoServico().getValue()))) {
                    //Este Documento de Serviço não pode ser incluído em um pré-manifesto de viagem nacional.
                    throw new BusinessException("LMS-05083");
                }
            } else {
                //Pré-Manifesto informado não é de viagem.
                throw new BusinessException("LMS-05081");
            }

            Integer qtFluxoFiliais = ordemFilialFluxoService
                    .getRowCountFluxoFilialByFilialOrigemDestinoAndDoctoServico(
                            manifesto.getFilialByIdFilialOrigem()
                                    .getIdFilial(), manifesto
                                    .getFilialByIdFilialDestino()
                                    .getIdFilial(), doctoServico
                                    .getIdDoctoServico());

            if (qtFluxoFiliais.intValue() == 0) {
                //Documento de Serviço não esta na rota do manifesto informado. Deseja confirmar sua inclusão?
                throw new BusinessException("LMS-05085");
            }

            //Caso o campo de rota ida e volta tenha sido preenchido
        } else if (idRotaIdaVolta != null) {

            boolean doctoPertence = false;
            Filial filialUsuario = SessionUtils.getFilialSessao();
            RotaIdaVolta rotaIdaVolta = rotaIdaVoltaService.findById(idRotaIdaVolta);

            List ordensFluxoFilial = ordemFilialFluxoService.findByIdFluxoFilial(doctoServico.getFluxoFilial().getIdFluxoFilial());
            List filialRotas = filialRotaService.findByIdRota(rotaIdaVolta.getRota().getIdRota());

            Long idFilialOrigem = null;
            Long idFilialDestino = null;

            // percorre até a penultima posição
            for (int i = 0; i < ordensFluxoFilial.size() - 1; i++) {
                OrdemFilialFluxo ordemFilialFluxo = (OrdemFilialFluxo) ordensFluxoFilial.get(i);
                if (ordemFilialFluxo.getFilial().getIdFilial().equals(filialUsuario.getIdFilial())) {
                    idFilialOrigem = ordemFilialFluxo.getFilial().getIdFilial();
                    idFilialDestino = ((OrdemFilialFluxo) ordensFluxoFilial.get(i + 1)).getFilial().getIdFilial();
                    break;
                }
            }

            // se nao encontrou filial origem e destino no fluxo
            if (idFilialOrigem == null) {
                throw new BusinessException("LMS-05086");
            }

            // percorre até a penúltima posicao
            for (int i = 0; i < filialRotas.size() - 1; i++) {
                FilialRota filialRotaOrigem = (FilialRota) filialRotas.get(i);
                FilialRota filialRotaDestino = (FilialRota) filialRotas.get(i + 1);
                if (idFilialOrigem.equals(filialRotaOrigem.getFilial().getIdFilial()) &&
                        idFilialDestino.equals(filialRotaDestino.getFilial().getIdFilial())) {
                    doctoPertence = true;
                    break;
                }
            }

            // se o documento nao pertence à rota
            if (!doctoPertence) {
                throw new BusinessException("LMS-05086");
            }
        }
    }

    /**
     * Contem as regras de persistencia da tela de <code>DoctoServicoCheckIn</code>.
     * Caso  <code>update<code> seja true,
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @param update
     */
    public void storeDocumentosServicoCheckin(Long idDoctoServico, Long idManifesto, Long idRotaIdaVolta, boolean update) {
        DoctoServico doctoServico = findById(idDoctoServico);
        Manifesto manifesto = null;
        RotaIdaVolta rotaIdaVolta = null;

        if (idManifesto != null) {
            manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
        }
        if (idRotaIdaVolta != null) {
            rotaIdaVolta = rotaIdaVoltaService.findById(idRotaIdaVolta);
        }

        if (doctoServico.getRotaIdaVolta() != null && rotaIdaVolta == null) {
            doctoServico.setRotaIdaVolta(null);
            store(doctoServico);
            String strDoctoServico = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), 8, '0');
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                    ConstantesSim.EVENTO_CANCELAMENTO_VINCULACAO_RV,
                    doctoServico.getIdDoctoServico(),
                    SessionUtils.getFilialSessao().getIdFilial(),
                    strDoctoServico,
                    JTDateTimeUtils.getDataHoraAtual(),
                    null,
                    SessionUtils.getFilialSessao().getSiglaNomeFilial(),
                    doctoServico.getTpDocumentoServico().getValue()
            );
        }

        //Caso o campo de pre-manifesto tenha sido preenchido
        if (manifesto != null) {
            PreManifestoDocumento preManifestoDocumento = null;
            preManifestoDocumento = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifestoDocto(idManifesto, idDoctoServico);

            if (preManifestoDocumento != null) {
                throw new BusinessException("LMS-05170");
            } else {
                preManifestoDocumento = new PreManifestoDocumento();
            }
            preManifestoDocumento.setDoctoServico(doctoServico);
            preManifestoDocumento.setManifesto(manifesto);
            preManifestoDocumento.setNrOrdem(null);

            preManifestoDocumentoService.store(preManifestoDocumento);
            String strNrDocumento = preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(preManifestoDocumento.getManifesto().getNrPreManifesto().toString(), 8, '0');
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                    ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO,
                    doctoServico.getIdDoctoServico(),
                    SessionUtils.getFilialSessao().getIdFilial(),
                    strNrDocumento,
                    JTDateTimeUtils.getDataHoraAtual(),
                    null,
                    SessionUtils.getFilialSessao().getSiglaNomeFilial(),
                    "PMV");

            //Caso o campo de rota ida e volta tenha sido preenchido
        } else if (rotaIdaVolta != null) {

            doctoServico.setRotaIdaVolta(rotaIdaVolta);
            store(doctoServico);
            String nrDoctoServico = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), 8, '0');
            incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                    ConstantesSim.EVENTO_VINCULACAO_RV,
                    doctoServico.getIdDoctoServico(),
                    SessionUtils.getFilialSessao().getIdFilial(),
                    nrDoctoServico,
                    JTDateTimeUtils.getDataHoraAtual(),
                    null,
                    SessionUtils.getFilialSessao().getSiglaNomeFilial(),
                    doctoServico.getTpDocumentoServico().getValue());
        }
    }

    public TypedFlatMap findDoctoServicoCheckin(Long idDoctoServico, Long idManifesto) {

        TypedFlatMap result = new TypedFlatMap();

        if (idManifesto != null) {
            Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);

            result.put("idFilialManifesto", manifesto.getFilialByIdFilialOrigem().getIdFilial());
            result.put("sgFilialManifesto", manifesto.getFilialByIdFilialOrigem().getSgFilial());
            result.put("idManifesto", manifesto.getIdManifesto());
            result.put("nrPreManifesto", manifesto.getNrPreManifesto());

            if (manifesto.getControleCarga() != null && manifesto.getControleCarga().getRotaIdaVolta() != null) {
                    result.put("idRotaIdaVolta", manifesto.getControleCarga().getRotaIdaVolta().getIdRotaIdaVolta());
                    result.put("nrRota", manifesto.getControleCarga().getRotaIdaVolta().getNrRota());
                    result.put("dsRota", manifesto.getControleCarga().getRotaIdaVolta().getRota().getDsRota());
                }
            }


        DoctoServico doctoServico = findById(idDoctoServico);

        //Busca algum manifesto que possa estar associado ao doctoServico
        if (idManifesto == null && !doctoServico.getPreManifestoDocumentos().isEmpty()) {
                PreManifestoDocumento preManifestoDocumento =
                        doctoServico.getPreManifestoDocumentos().get(0);
                result.put("idFilialManifesto", preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
                result.put("sgFilialManifesto", preManifestoDocumento.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
                result.put("idManifesto", preManifestoDocumento.getManifesto().getIdManifesto());
                result.put("nrPreManifesto", preManifestoDocumento.getManifesto().getNrPreManifesto());

            }

        result.put("tpDoctoServico", doctoServico.getTpDocumentoServico().getValue());
        result.put("idFilialDoctoServico", doctoServico.getFilialByIdFilialOrigem().getIdFilial());
        result.put("sgFilialDoctoServico", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
        result.put(ConstantesExpedicao.ID_DOCTO_SERVICO, doctoServico.getIdDoctoServico());
        result.put("nrDoctoServico", doctoServico.getNrDoctoServico());
        if (doctoServico instanceof Conhecimento) {
            Conhecimento conhecimento = (Conhecimento) doctoServico;
            result.put("dvConhecimento", conhecimento.getDvConhecimento());
        }

        if ((idManifesto == null) && (doctoServico.getRotaIdaVolta() != null)) {
            result.put("idRotaIdaVolta", doctoServico.getRotaIdaVolta().getIdRotaIdaVolta());
            result.put("nrRota", doctoServico.getRotaIdaVolta().getNrRota());
            result.put("dsRota", doctoServico.getRotaIdaVolta().getRota().getDsRota());
        }

        return result;
    }

    /**
     * @param tpManifesto
     * @param blPreManifesto
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByManifesto(String tpManifesto, Boolean blPreManifesto,
                                                              Long idManifesto, Long idDoctoServico, String tpDocumentoServico, Long idFilialOrigem, FindDefinition findDefinition) {
        Manifesto m = manifestoService.findByIdInitLazyProperties(idManifesto, false);
        String tpManifestoViagem = null;
        if (m.getTpManifestoViagem() != null) {
            tpManifestoViagem = m.getTpManifestoViagem().getValue();
        }

        ResultSetPage rsp = getDoctoServicoDAO().findPaginatedDoctoServicoByManifesto(
                tpManifesto, blPreManifesto, idManifesto, idDoctoServico, tpDocumentoServico, idFilialOrigem, findDefinition);

        List<Map<String, Object>> result = rsp.getList();
        for (Map<String, Object> map : result) {
            Long idDoctoServicoResult = (Long) map.get("idDoctoServico");
            if ("E".equals(tpManifesto)) {
                OcorrenciaEntrega oe = ocorrenciaEntregaService.findOcorrenciaEntregaByIdDoctoServicoByIdManifesto(idDoctoServicoResult, idManifesto);
                map.put("dsOcorrenciaEntrega", oe == null ? null : oe.getDsOcorrenciaEntrega().toString());
            } else if (tpManifestoViagem != null && "ED".equals(tpManifestoViagem)) {
                OcorrenciaEntrega oe = ocorrenciaEntregaService.findOcorrenciaEntregaByIdDoctoServico(idDoctoServicoResult);
                map.put("dsOcorrenciaEntrega", oe == null ? null : oe.getDsOcorrenciaEntrega().toString());
            }
        }
        return rsp;
    }


    /**
     * @param tpManifesto
     * @param blPreManifesto
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @return
     */
    public Integer getRowCountDoctoServicoByManifesto(String tpManifesto, Boolean blPreManifesto,
                                                      Long idManifesto, Long idDoctoServico, String tpDocumentoServico, Long idFilialOrigem) {
        return getDoctoServicoDAO().getRowCountDoctoServicoByManifesto(tpManifesto,
                blPreManifesto, idManifesto, idDoctoServico, tpDocumentoServico, idFilialOrigem);
    }

    /**
     * Busca a hrSaida de uma <code>RotaIdaVolta</code>.
     *
     * @param idRotaIdaVolta
     * @return
     */
    public List<TimeOfDay> findSubQueryHrSaida(Long idRotaIdaVolta) {
        return this.getDoctoServicoDAO().findSubQueryHrSaida(idRotaIdaVolta);
    }

    public Map<String, Object> findTotaisCalculoServico(Long idDoctoServico) {
        return lmFreteDao.findTotaisCalculoServico(idDoctoServico);
    }

    public void setOrdemFilialFluxoService(OrdemFilialFluxoService ordemFilialFluxoService) {
        this.ordemFilialFluxoService = ordemFilialFluxoService;
    }

    public void setFilialRotaService(FilialRotaService filialRotaService) {
        this.filialRotaService = filialRotaService;
    }

    /**
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountDoctoServicoEntregues(Long idDoctoServico) {
        return getDoctoServicoDAO().getRowCountDoctoServicoEntregues(idDoctoServico);
    }

    /**
     * verifica se existe algum Controle carga para o documento de servico
     *
     * @param idDoctoServico
     * @return
     */

    public boolean findCCByIdDoctoServico(Long idDoctoServico) {
        return getDoctoServicoDAO().findCCByIdDoctoServico(idDoctoServico);
    }

    /**
     * Busca doctoServico para a grid da aba "Terminal" ou "Descarga" da pop-up de abrir doctoServicoPreManifesto.
     *
     * @param idManifesto
     * @param idSolicitacaoRetirada
     * @param idRotaColetaEntrega
     * @param idFilialDestino
     * @param idConsignatario
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param tpDoctoServico
     * @param idFilialOrigemDoctoServico
     * @param idFilialDestinoDoctoServico
     * @param idDoctoServico
     * @param idAwb
     * @param tpManifesto
     * @param tpAbrangencia
     * @param tpPreManifesto
     * @param idsDoctoServico
     * @param localizacaoDocumento
     * @param idPedidoColeta
     * @param dsBox
     * @return
     */
    @SuppressWarnings("unchecked")
    public List findAdicionarDoctoServicoPreManifesto(
            Long idManifesto,
            Long idSolicitacaoRetirada,
            Long idRotaColetaEntrega,
            Long idFilialDestino,
            Long idConsignatario,
            Long idClienteRemetente,
            Long idClienteDestinatario,
            String tpDoctoServico,
            Long idFilialOrigemDoctoServico,
            Long idFilialDestinoDoctoServico,
            Long idDoctoServico,
            Long idAwb,
            String tpManifesto,
            String tpAbrangencia,
            String tpPreManifesto,
            List<Long> idsDoctoServico,
            String localizacaoDocumento,
            Long idPedidoColeta,
            String dsBox) {


        if (idManifesto != null) {
            //Se idManifesto é diferente de null entao significa que está
            //sendo alterado um pre-manifesto já gerado (manter/gerar pre-manifesto)
            //ou esta sendo feito o carregamento (carregar veiculo).

            Manifesto manifesto = manifestoService.findManifestoById(idManifesto);

            idFilialDestino = manifesto.getFilialByIdFilialDestino().getIdFilial();

            tpManifesto = manifesto.getTpManifesto().getValue();
            tpAbrangencia = manifesto.getTpAbrangencia().getValue();

            if ("E".equals(tpManifesto)) {
                tpPreManifesto = manifesto.getTpManifestoEntrega().getValue();
            } else {
                tpPreManifesto = manifesto.getTpManifestoViagem().getValue();
            }
        }

        List<Object[]> query = new ArrayList<Object[]>();
        List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
        List<Long> idsLocalizacao = new ArrayList<Long>();
        List<String> codigos = new ArrayList<String>();
        List<LocalizacaoMercadoria> listLocalizacaoMercadoria = new ArrayList<LocalizacaoMercadoria>();
        String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), 
                "BL_BLOQ_DOC_SUB");

        //ENTREGA
        if ("E".equals(tpManifesto)) {
            if ("terminal".equals(localizacaoDocumento)) {
                codigos.add("24");
                codigos.add("35");
                codigos.add("43");
                listLocalizacaoMercadoria = localizacaoMercadoriaService.findByCodigosLocalizacaoMercadoria(codigos, null);
            } else if ("descarga".equals(localizacaoDocumento)) {
                codigos.add("33");
                codigos.add("34");
                listLocalizacaoMercadoria = localizacaoMercadoriaService.findByCodigosLocalizacaoMercadoria(codigos, null);
            }
            for (LocalizacaoMercadoria localizacaoMercadoria : listLocalizacaoMercadoria) {
                idsLocalizacao.add(localizacaoMercadoria.getIdLocalizacaoMercadoria());
            }

            query = this.getDoctoServicoDAO().findAdicionarDoctoServicoEntrega(
                    idSolicitacaoRetirada,
                    idRotaColetaEntrega,
                    idConsignatario,
                    idClienteRemetente,
                    idClienteDestinatario,
                    tpDoctoServico,
                    idFilialOrigemDoctoServico,
                    idFilialDestinoDoctoServico,
                    idDoctoServico,
                    idAwb,
                    tpPreManifesto,
                    idsDoctoServico,
                    idsLocalizacao,
                    SessionUtils.getFilialSessao().getIdFilial(),
                    idPedidoColeta,
                    dsBox,
                    bloqFluxoSubcontratacao);
            //VIAGEM
        } else {
            if ("terminal".equals(localizacaoDocumento)) {
                codigos.add("24");
                codigos.add("28");
                listLocalizacaoMercadoria = localizacaoMercadoriaService.findByCodigosLocalizacaoMercadoria(codigos, null);
            } else if ("descarga".equals(localizacaoDocumento)) {
                codigos.add("33");
                codigos.add("34");
                listLocalizacaoMercadoria = localizacaoMercadoriaService.findByCodigosLocalizacaoMercadoria(codigos, null);
            }
            for (LocalizacaoMercadoria localizacaoMercadoria : listLocalizacaoMercadoria) {
                idsLocalizacao.add(localizacaoMercadoria.getIdLocalizacaoMercadoria());
            }
            
            query = this.getDoctoServicoDAO().findAdicionarDoctoServicoViagem(
                    idFilialDestino,
                    idClienteRemetente,
                    idClienteDestinatario,
                    tpDoctoServico,
                    idFilialOrigemDoctoServico,
                    idFilialDestinoDoctoServico,
                    idDoctoServico,
                    idAwb,
                    tpAbrangencia,
                    tpPreManifesto,
                    idsDoctoServico,
                    idsLocalizacao,
                    SessionUtils.getFilialSessao().getIdFilial(),
                    idPedidoColeta,
                    dsBox,
                    bloqFluxoSubcontratacao);

            if (idPedidoColeta == null && dsBox == null) {
                List<Long> idsFilialSubstituta = substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialDestino(idFilialDestino, null);
                idsFilialSubstituta.addAll(substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialSubstituta(idFilialDestino));
                if (!idsFilialSubstituta.isEmpty()) {
                    List<Object[]> listDoctoServicoSubst = new ArrayList<Object[]>();
                    for (Long idFilialSubst : idsFilialSubstituta) {
                        List<Object[]> resultDoctosFluxoSubst = this.getDoctoServicoDAO().findAdicionarDoctoServicoViagem(
                                idFilialSubst,
                                idClienteRemetente,
                                idClienteDestinatario,
                                tpDoctoServico,
                                idFilialOrigemDoctoServico,
                                idFilialDestinoDoctoServico,
                                idDoctoServico,
                                idAwb,
                                tpAbrangencia,
                                tpPreManifesto,
                                idsDoctoServico,
                                idsLocalizacao,
                                SessionUtils.getFilialSessao().getIdFilial(),
                                idPedidoColeta,
                                dsBox,
                                bloqFluxoSubcontratacao);

                        for (Object[] objDoctoSubst : resultDoctosFluxoSubst) {
                            Filial filialDestinoSubstituta = substAtendimentoFilialService.findFilialDestinoDoctoServico((Long) objDoctoSubst[0], null, null, null);
                            if (idFilialDestino.equals(filialDestinoSubstituta.getIdFilial())) {
                                listDoctoServicoSubst.add(objDoctoSubst);
                            }
                        }
                    }

                    query.addAll(listDoctoServicoSubst);
                }
            }
        }

        for (Object[] object : query) {
            TypedFlatMap typedFlatMap = new TypedFlatMap();
            typedFlatMap.put(ConstantesExpedicao.ID_DOCTO_SERVICO, object[0]);
            typedFlatMap.put("tpDocumentoServico", domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", (String) object[1]));
            typedFlatMap.put("nrDoctoServico", object[2]);
            typedFlatMap.put("dvDoctoServico", object[3]);
            typedFlatMap.put("qtVolumes", object[4]);
            typedFlatMap.put("psReal", object[5]);
            typedFlatMap.put("vlMercadoria", object[6]);
            typedFlatMap.put("dtPrevEntrega", object[7]);
            typedFlatMap.put(ConstantesExpedicao.DH_EMISSAO, object[8]);
            typedFlatMap.put("blBloqueado", object[9]);
            typedFlatMap.put("filialByIdFilialOrigem.idFilial", object[10]);
            typedFlatMap.put("filialByIdFilialOrigem.sgFilial", object[11]);
            typedFlatMap.put("filialByIdFilialOrigem.pessoa.nmFantasia", object[12]);
            typedFlatMap.put(ConstantesExpedicao.FILIAL_BY_ID_FILIAL_DESTINO_ID_FILIAL, object[13]);
            typedFlatMap.put(ConstantesExpedicao.FILIAL_BY_ID_FILIAL_DESTINO_SG_FILIAL, object[14]);
            typedFlatMap.put("filialByIdFilialDestino.pessoa.nmFantasia", object[15]);
            typedFlatMap.put(ConstantesExpedicao.MOEDA_ID_MOEDA, object[16]);
            typedFlatMap.put("moeda.sgMoeda", object[17]);
            typedFlatMap.put(ConstantesExpedicao.MOEDA_DS_SIMBOLO, object[18]);
            typedFlatMap.put("servico.idServico", object[19]);
            typedFlatMap.put("servico.sgServico", object[20]);
            typedFlatMap.put(ConstantesExpedicao.SERVICO_TP_MODAL, object[21]);
            typedFlatMap.put("servico.tpAbrangencia", object[22]);
            typedFlatMap.put("servico.tipoServico.blPriorizar", object[23]);
            typedFlatMap.put(ConstantesExpedicao.CLIENTE_BY_ID_CLIENTE_REMETENTE_ID_CLIENTE, object[24]);
            typedFlatMap.put(ConstantesExpedicao.CLIENTE_BY_ID_CLIENTE_REMETENTE_PESSOA_NM_PESSOA, object[25]);
            typedFlatMap.put("clienteByIdClienteRemetente.blAgendamentoPessoaFisica", object[26]);
            typedFlatMap.put("clienteByIdClienteRemetente.blAgendamentoPessoaJuridica", object[27]);
            typedFlatMap.put(ConstantesExpedicao.CLIENTE_BY_ID_CLIENTE_DESTINATARIO_ID_CLIENTE, object[28]);
            typedFlatMap.put(ConstantesExpedicao.CLIENTE_BY_ID_CLIENTE_DESTINATARIO_PESSOA_NM_PESSOA, object[29]);
            typedFlatMap.put("clienteByIdClienteDestinatario.tpPessoa", object[30]);
            typedFlatMap.put("clienteByIdClienteConsignatario.idCliente", object[31]);
            typedFlatMap.put("clienteByIdClienteConsignatario.pessoa.nmPessoa", object[32]);
            typedFlatMap.put("clienteByIdClienteConsignatario.tpPessoa", object[33]);
            typedFlatMap.put("rotaColetaEntregaByIdRotaColetaEntregaReal.idRotaColetaEntrega", object[34]);
            typedFlatMap.put("paisOrigem.idPais", object[35]);

            YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
            DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

            //Captura dados da consulta...
            YearMonthDay dtPrevEntrega = (YearMonthDay) object[7];
            String tpModal = (String) object[21];
            Boolean blPriorizar = (Boolean) object[23];
            DateTime dhEvento = (DateTime) object[36];

            int dias = 0;
            if (dhEvento != null) {
                dias = JTDateTimeUtils.getIntervalInDays(
                        dhEvento.toDateTime(dataHoraAtual.getZone()).toYearMonthDay(), dataHoraAtual.toYearMonthDay());
                typedFlatMap.put("diasTerminal", dias);
            }

            if ((dtPrevEntrega != null && dtPrevEntrega.compareTo(dataAtual) < 0) || "A".equals(tpModal) || blPriorizar.booleanValue()) {
                typedFlatMap.put("emDestaque", Boolean.TRUE);
            } else {
                typedFlatMap.put("emDestaque", Boolean.FALSE);
            }

            // LMS-2337 :(
            if ("terminal".equals(localizacaoDocumento) && idFilialDestino != null && StringUtils.isNotBlank(tpManifesto)) {
                if (preManifestoDocumentoService.validateDocumentoServicoManifesto(idFilialDestino, tpManifesto, typedFlatMap.getLong("idDoctoServico"))) {
                    result.add(typedFlatMap);
                }
            } else {
                result.add(typedFlatMap);
            }

        }

        return result;
    }

    /**
     * Retorna uma lista de TypedFlatMaps contendo as informações dos Documentos de Serviço.
     *
     * @param idControleCarga
     * @return
     */
    public List findDoctoServicoByIdControleCarga(Long idControleCarga) {
        List lista = getDoctoServicoDAO().findDoctoServicoByIdControleCarga(idControleCarga);
        return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
    }

    /**
     * Retorna uma lista com Documentos de Serviço do Controle de Carga em questão.
     *
     * @param idControleCarga
     * @return
     */
    public List<DoctoServico> findDoctosServicoByIdControleCarga(Long idControleCarga) {
        return getDoctoServicoDAO().findDoctosServicoByIdControleCarga(idControleCarga);
    }

    /**
     * Retorna uma lista com Documentos de Serviço do Pedido de Coleta em questão.
     *
     * @param idPedidoColeta
     * @return
     */
    public List<Map<String, Object>> findDoctosServicoByIdPedidoColetaLiberaEtiquetaEdi(Long idPedidoColeta) {
        return getDoctoServicoDAO().findDoctosServicoByIdPedidoColetaLiberaEtiquetaEdi(idPedidoColeta);
    }


    /**
     * Carrega doctoServico's de acordo com o idReciboDesconto
     * que exitam no Lms e não vieram no "movimento da integração".
     *
     * @param idsDevedores
     * @return
     * @author Hector Julian Esnaola Junior
     * @since 26/09/2007
     */
    public List<DoctoServico> findDoctoServicoDivergenteMovimentoLmsByReciboDesconto(
            List<Long> idsDevedores,
            Long idReciboDesconto
    ) {
        if (idsDevedores == null || idsDevedores.isEmpty() || idReciboDesconto == null) {
            return new ArrayList<DoctoServico>();
        }

        return getDoctoServicoDAO()
                .findDoctoServicoDivergenteMovimentoLmsByReciboDesconto(
                        idsDevedores,
                        idReciboDesconto
                );
    }

    /**
     * Carrega doctoServico's de acordo com o idDemonstrativoDesconto
     * que exitam no Lms e não vieram no "movimento da integração".
     *
     * @param idsDevedores
     * @return
     * @author Hector Julian Esnaola Junior
     * @since 26/09/2007
     */
    public List<DoctoServico> findDoctoServicoDivergenteMovimentoLmsByDemonstrativoDesconto(
            List<Long> idsDevedores,
            Long idDemonstrativoDesconto
    ) {

        if (idsDevedores == null || idsDevedores.isEmpty() || idDemonstrativoDesconto == null) {
            return new ArrayList<DoctoServico>();
        }

        return getDoctoServicoDAO()
                .findDoctoServicoDivergenteMovimentoLmsByDemonstrativoDesconto(
                        idsDevedores,
                        idDemonstrativoDesconto);
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setLmFreteDao(LMFreteDAO lmFreteDao) {
        this.lmFreteDao = lmFreteDao;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
        this.lmComplementoDao = lmComplementoDao;
    }

    public void setLocalizacaoMercadoriaDAO(LocalizacaoMercadoriaDAO localizacaoMercadoriaDAO) {
        this.localizacaoMercadoriaDAO = localizacaoMercadoriaDAO;
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

    public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
        this.reciboReembolsoService = reciboReembolsoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
        this.rotaIdaVoltaService = rotaIdaVoltaService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setSubstAtendimentoFilialService(SubstAtendimentoFilialService substAtendimentoFilialService) {
        this.substAtendimentoFilialService = substAtendimentoFilialService;
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    /**
     * Verifica se o documento possui um agendamento.
     *
     * @param idDoctoServico
     * @return
     */
    public List<DoctoServico> findDoctoServicoWithAgendamento(Long idDoctoServico) {
        return getDoctoServicoDAO().findDoctoServicoWithAgendamento(idDoctoServico);
    }
    
    public void generateLiberacaoDocumentoServico(Long idDoctoServico, DateTime dhLiberacao) {
        generateLiberacaoDocumentoServicoComCodigoOcorrencia(idDoctoServico, dhLiberacao, Short.valueOf("92"));
    }
    
    public void generateLiberacaoDocumentoServico(Long idDoctoServico, DateTime dhLiberacao, Short cdOcorrencia) {
        generateLiberacaoDocumentoServicoComCodigoOcorrencia(idDoctoServico, dhLiberacao, cdOcorrencia);
    }

    /**
     * Faz a liberação de um documento de serviço, caso esse esteja bloqueado, criando o evento para o mesmo.
     * @param idDoctoServico (required)
     * @param dhLiberacao
     */
    public void generateLiberacaoDocumentoServicoComCodigoOcorrencia(Long idDoctoServico, DateTime dhLiberacao, Short cdOcorrencia) {
        DoctoServico ds = findById(idDoctoServico);
        Filial filialUsuario = SessionUtils.getFilialSessao();

        OcorrenciaPendencia ocorrenciaPendencia = null;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cdOcorrencia", cdOcorrencia);

        List<OcorrenciaPendencia> listaOcorrencias = ocorrenciaPendenciaService.find(map);
        if (!listaOcorrencias.isEmpty()) {
            ocorrenciaPendencia = listaOcorrencias.get(0);
        }

        /**
         * LMS-4333: SE o documento de serviço informado não estiver bloqueado
         * (DOCTO_SERVICO.BL_BLOQUEADO = 'N') e tentar salvar uma ocorrência de
         * Liberação (OCORRENCIA_PENDENCIA. TP_OCORRENCIA = 'L')
         * ENTÃO abortar a execução dessa rotina;
         */
        if (!ds.getBlBloqueado() && "L".equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {
            return;
        }

        incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
                ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
                idDoctoServico,
                filialUsuario.getIdFilial(),
                ds.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(ds.getNrDoctoServico().toString(), 8, '0'),
                dhLiberacao != null ? dhLiberacao : JTDateTimeUtils.getDataHoraAtual(),
                null,
                filialUsuario.getSiglaNomeFilial(),
                ConstantesExpedicao.CONHECIMENTO_RODOVIARIO_CARGAS_NACIONAL,
                null,
                ds.getBlBloqueado() ? ocorrenciaPendencia.getIdOcorrenciaPendencia() : null,
                null,
                SessionUtils.getUsuarioLogado());

        if (ds.getBlBloqueado()) {
            OcorrenciaDoctoServico ods = ocorrenciaDoctoServicoService.findLastOcorrenciaDoctoServicoByIdDoctoServico(idDoctoServico);
            if (ods != null) {
                ods.setFilialByIdFilialLiberacao(filialUsuario);
                ods.setOcorrenciaPendenciaByIdOcorLiberacao(ocorrenciaPendencia);
                ods.setUsuarioLiberacao(SessionUtils.getUsuarioLogado());
                ods.setDhLiberacao(dhLiberacao != null ? dhLiberacao : JTDateTimeUtils.getDataHoraAtual());
                ocorrenciaDoctoServicoService.store(ods);
                ocorrenciaDoctoServicoService.flush();

                //LMS-4187
                calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(ds, Boolean.TRUE);
                ds.setBlBloqueado(Boolean.FALSE);
                store(ds);
            }
        }
    }

    /**
     * Verifica a existencia de Documentos de Serviço pelo Controle Carga
     * Joins :: DOCTO_SERVICO > MANIFESTO_ENTREGA_DOCUMENTO > MANIFESTO_ENTREGA_DOCUMENTO  > MANIFESTO_ENTREGA > MANIFESTO > CONTROLE_CARGA
     *
     * @param idControleCarga Long
     */
    public Boolean validateDoctoServicoByIdControleCarga(Long idControleCarga) {
        return getDoctoServicoDAO().validateDoctoServicoByIdControleCarga(idControleCarga);
    }

    public boolean validateDoctoServicoSemOcorrenciaEntrega(Long idControleCarga) {
        return getDoctoServicoDAO().validateDoctoServicoSemOcorrenciaEntrega(idControleCarga);
    }

    public List<Integer> validateDoctoServicoComPendenciaAprovacao(Long idMonitoramentoDescarga) {
        return getDoctoServicoDAO().validateDoctoServicoComPendenciaAprovacao(idMonitoramentoDescarga);
    }


    public void validateDoctoServicoComMonitoramentoEletronicoAutorizado(Long idDoctoServico) {
        if (!isMonitoramentoEletronicoAutorizado(idDoctoServico)) {
            throw new BusinessException("LMS-36282");
        }
    }

    public Boolean isMonitoramentoEletronicoAutorizado(Long idDoctoServico) {
        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);
        if (monitoramentoDocEletronico != null) {
            DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
            return isMonitoramentoEletronicoAutorizado(doctoServico.getTpDocumentoServico().getValue(), monitoramentoDocEletronico.getTpSituacaoDocumento().getValue());
        }
        return Boolean.TRUE;
    }

    public Boolean isMonitoramentoEletronicoAutorizado(String tpDocto, String tpSituacao) {
        if ((tpDocto.equals(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA)
                || tpDocto.equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)) &&
                !ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_AUTORIZADO.equals(tpSituacao)) {
                return Boolean.FALSE;

            }
        return Boolean.TRUE;
    }

    /**
     * Busca todos os conhecimentos que não possuem fluxo filial
     * <p>
     * Retorna somente idDoctoServico, filialOrigem, filialDestino e Servico
     * estes dados serão utilizados na rotina AtualizarFluxoFilialDocumentos
     *
     * @return List<DoctoServico>
     */
    public List<DoctoServico> findDocSemFluxoFilial(Long idUsuario) {
        return getDoctoServicoDAO().findDocSemFluxoFilial(idUsuario);
    }

    /**
     * Atualiza somente o fluxo filial do documento
     *
     * @param doctoServico
     */
    public void executeUpdateFluxoDoc(DoctoServico doctoServico) {
        getDoctoServicoDAO().executeUpdateFluxoDoc(doctoServico);
    }

    public DoctoServico findCdLocalizacaoByParams(Long idFilial, Long nrDoctoServico, String tpDoctoServico) {
        return getDoctoServicoDAO().findCdLocalizacaoByParams(idFilial, nrDoctoServico, tpDoctoServico);
    }

    public List<DoctoServico> findDoctoServicoByIdManifestoAndTipoManifesto(Long idManifesto, String tpManifesto) {
        return getDoctoServicoDAO().findDoctoServicoByIdManifestoAndTipoManifesto(idManifesto, tpManifesto);
    }

    public List<Map<String, Object>> findConhecimentoByIdManifestoEntrega(Long idManifestoEntrega) {
        return getDoctoServicoDAO().findConhecimentoByIdManifestoEntrega(idManifestoEntrega);
    }

    public List<String> findXObsCTE(Long idDoctoServico) {
        return getDoctoServicoDAO().findXObsCTE(idDoctoServico);
    }

    public List<DoctoServicoSaltoDMN> executefindSaltosNrCte() {

        Date startDtEmissao = new Date();

        DateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");

        String dtUltimaExcucao = (String) configuracoesFacadeImpl.getValorParametro(ConstantesExpedicao.DIAS_GERACAO_FALT);
        configuracoesFacadeImpl.storeValorParametro(ConstantesExpedicao.DIAS_GERACAO_FALT, fmt.format(startDtEmissao));

        try {
            startDtEmissao = fmt.parse(dtUltimaExcucao);
        } catch (ParseException e) {
            throw new BusinessException("LMS-45035", new Object[]{"String", "Date"});
        }

        return getDoctoServicoDAO().findSaltosNrCte(startDtEmissao, new Date());
    }

    public Long findDoctoServicoByTpDoctoByIdFilialOrigemByNrDocto(String tpDocumentoServico, Long idFilial, Long nrDoctoServico) {
        return getDoctoServicoDAO().findDoctoServicoByTpDoctoByIdFilialOrigemByNrDocto(tpDocumentoServico, idFilial, nrDoctoServico);
    }

    public Long findDoctoServicoByTpDoctoByIdFilialOrigemByNrDoctoSalto(String tpDocumentoServico, Long idFilial, Long nrDoctoServico) {
        return getDoctoServicoDAO().findDoctoServicoByTpDoctoByIdFilialOrigemByNrDoctoSalto(tpDocumentoServico, idFilial, nrDoctoServico);
    }

    public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
        this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
    }

    public List findDoctoServicoManual(Map criteria) {
        if (criteria.get("nrDoctoServico") == null) {
            return new ArrayList<DoctoServico>();
        }

        List list = getDoctoServicoDAO().findDoctoServicoManual(criteria);

        return transformList(list);
    }

    /**
     * Executa regras para validações do valor da mercadoria, valor do frete, e percentual do segundo em relação ao primeiro. Caso o documento de
     * serviço não seja validado em alguma regra, é criado um workflow, um registro em doctoServicoWorkflow, e enviado um email de aviso para o
     * usuário que incluiu o documento, avisando sobre a não validação.
     *
     * @param idDoctoServico
     */
    @SuppressWarnings("unchecked")
    public void executeValidacoesParaBloqueioValores(Long idDoctoServico) {
        Pendencia pendencia = null;
        StringBuilder dsProcesso = null;
        DoctoServicoWorkflow doctoServicoWorkflow = null;
        List<Integer> notas = notaFiscalConhecimentoService.findNFByIdConhecimento(idDoctoServico);

        DoctoServico doctoServicoValidacao = this.getDoctoServicoDAO().findDoctoServico(idDoctoServico);
        Hibernate.initialize(doctoServicoValidacao.getUsuarioByIdUsuarioInclusao());
        Hibernate.initialize(doctoServicoValidacao.getClienteByIdClienteRemetente());

        BigDecimal vlMercadoria = doctoServicoValidacao.getVlMercadoria();
        BigDecimal vlFrete = doctoServicoValidacao.getVlTotalDocServico();

        TypedFlatMap map = new TypedFlatMap();
        map.put("lancarExcecao", false);

        map.put("vlFrete", vlFrete);
        map = this.executeValidacaoLimiteValorFrete(map);
        if (!map.getBoolean("vlValido")) {
            doctoServicoWorkflow = new DoctoServicoWorkflow();
            doctoServicoWorkflow.setDoctoServico(doctoServicoValidacao);
            doctoServicoWorkflowService.store(doctoServicoWorkflow);

            dsProcesso = this.getDsProcessoInicialParaValidacoesBloqueioValores(doctoServicoValidacao, notas);
            dsProcesso.append("teve o valor do frete/serviço calculado em R$ ");
            dsProcesso.append(FormatUtils.formatDecimal("#,##0.00", vlFrete, true));
            dsProcesso.append(". Este valor excedeu o limite de R$ ");
            dsProcesso.append(FormatUtils.formatDecimal("#,##0.00", map.getBigDecimal("vlLimite"), true));
            dsProcesso.append(".");

            pendencia = workflowPendenciaService
                    .generatePendencia(SessionUtils.getFilialSessao().getIdFilial(), ConstantesWorkflow.NR402_VL_FRETE_SUPERIOR_LIMITE,
                            doctoServicoWorkflow.getIdDoctoServicoWorkflow(), dsProcesso.toString(), JTDateTimeUtils.getDataHoraAtual());

            doctoServicoWorkflow.setPendencia(pendencia);
            doctoServicoWorkflow.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
            doctoServicoWorkflowService.store(doctoServicoWorkflow);

            this.generateEmailAvisoUsuarioInclusaoDocumento(doctoServicoValidacao, dsProcesso);
        }

        map.put("vlMercadoria", vlMercadoria);
        map = this.executeValidacaoPercentualValorMercadoria(map);
        if (!map.getBoolean("vlValido")) {
            doctoServicoWorkflow = new DoctoServicoWorkflow();
            doctoServicoWorkflow.setDoctoServico(doctoServicoValidacao);
            doctoServicoWorkflowService.store(doctoServicoWorkflow);

            dsProcesso = this.getDsProcessoInicialParaValidacoesBloqueioValores(doctoServicoValidacao, notas);
            dsProcesso.append("teve o valor do frete/serviço calculado em R$ ");
            dsProcesso.append(FormatUtils.formatDecimal("#,##0.00", vlFrete, true));
            dsProcesso.append(". Este valor excedeu o limite de ");
            dsProcesso.append(FormatUtils.formatDecimal("#,##0.00", map.getBigDecimal("vlLimite"), true));
            dsProcesso.append("% do valor da mercadoria.");

            pendencia = workflowPendenciaService
                    .generatePendencia(SessionUtils.getFilialSessao().getIdFilial(), ConstantesWorkflow.NR403_VL_PERCENTUAL_FRETE_SUPERIOR_LIMITE,
                            doctoServicoWorkflow.getIdDoctoServicoWorkflow(), dsProcesso.toString(), JTDateTimeUtils.getDataHoraAtual());

            doctoServicoWorkflow.setPendencia(pendencia);
            doctoServicoWorkflow.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
            doctoServicoWorkflowService.store(doctoServicoWorkflow);

            this.generateEmailAvisoUsuarioInclusaoDocumento(doctoServicoValidacao, dsProcesso);
        }
    }

    /**
     * Envia email para o usuário que incluiu o documento de serviço alertando que o documento não foi validado.
     *
     * @param doctoServico
     */
    private void generateEmailAvisoUsuarioInclusaoDocumento(DoctoServico doctoServico, final StringBuilder dsProcesso) {
        final String strTo = doctoServico.getUsuarioByIdUsuarioInclusao().getDsEmail();

        if (strTo != null && !"".equals(strTo)) {
            final String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
            final StringBuilder strSubject = new StringBuilder("Geração de workflow para aprovação do documento ");
            strSubject.append(doctoServico.getTpDocumentoServico().getDescription().getValue());
            strSubject.append(" ");
            strSubject.append(doctoServico.getFilialByIdFilialOrigem().getSgFilial());
            strSubject.append(" ");
            strSubject.append(FormatUtils.formatLongWithZeros(doctoServico.getNrDoctoServico(), "00000000"));

			Mail mail = createMail(strTo, strFrom, strSubject.toString(), dsProcesso.toString(), new ArrayList<>());
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
                }
        }
	
    public Mail createMail(String strTo, String strFrom, String strSubject, String body, List<MailAttachment> mailAttachmentList) {
        Mail mail = new Mail();
        mail.setContentType(TEXT_HTML);
        mail.setFrom(strFrom);
        mail.setTo(strTo);
        mail.setSubject(strSubject);
        mail.setBody(body);
        mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));
        return mail;
    }

    /**
     * Retorna parte do texto que é igual para todas as mensagens de validação de bloqueio de valores.
     *
     * @param doctoServicoValidacao
     * @param notas
     * @return
     */
    private StringBuilder getDsProcessoInicialParaValidacoesBloqueioValores(DoctoServico doctoServicoValidacao, List<Integer> notas) {
        StringBuilder dsProcesso = new StringBuilder("O documento ");
        dsProcesso.append(doctoServicoValidacao.getTpDocumentoServico().getDescription().getValue());
        dsProcesso.append(" ");
        dsProcesso.append(doctoServicoValidacao.getFilialByIdFilialOrigem().getSgFilial());
        dsProcesso.append(" ");
        dsProcesso.append(FormatUtils.formatLongWithZeros(doctoServicoValidacao.getNrDoctoServico(), "00000000"));
        dsProcesso.append(" do cliente ");
        dsProcesso.append(doctoServicoValidacao.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
        dsProcesso.append(", referente à nota fiscal ");

        if (!notas.isEmpty()) {
            dsProcesso.append(notas.get(0));
        }
        dsProcesso.append(", ");

        return dsProcesso;
    }

    public TypedFlatMap executeValidacaoLimiteValorMercadoria(TypedFlatMap map) {
       return executeValidacaoLimiteValorMercadoria(map, SessionUtils.getFilialSessao().getIdFilial());
    }

    public TypedFlatMap executeValidacaoLimiteValorMercadoria(TypedFlatMap map, Long idFilialParametro) {
        Boolean lancarExcecao = map.getBoolean("lancarExcecao", true);
        Boolean buscarMsgValidacao = map.getBoolean("buscarMsgValidacao", false);
        BigDecimal vlMercadoria = map.getBigDecimal("vlMercadoria");
        map.put("vlValido", Boolean.TRUE);

        if (vlMercadoria != null) {

			/* LMS-5176
             * Quando o método é chamado a partir de um JOB, neste caso do JOB do Sorter, a filial da sessão não é a filial correta para a busca do parâmetro.
			 */
            if (map.get("filialOrigem") != null) {
                idFilialParametro = ((Filial) map.get("filialOrigem")).getIdFilial();
            }

            BigDecimal vlLimiteMercadoriaFilial = (BigDecimal) configuracoesFacade.getValorParametro(idFilialParametro,
                    ConstantesExpedicao.NM_PARAMETRO_FILIAL_LIMITE_VL_MERCADORIA);

            if (vlLimiteMercadoriaFilial != null) {
                if (vlMercadoria.compareTo(vlLimiteMercadoriaFilial) > 0) {
                    Object[] param = new Object[]{"valor da mercadoria", FormatUtils.formatDecimal("#,##0.00", vlMercadoria, true), "da filial",
                            FormatUtils.formatDecimal("#,##0.00", vlLimiteMercadoriaFilial, true)};

                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlLimiteMercadoriaFilial, param, LMS_04421);
                        }
            } else {
                BigDecimal vlLimiteMercadoriaGeral = (BigDecimal) configuracoesFacade
                        .getValorParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_LIMITE_GERAL_VL_MERCADORIA);

                if (vlLimiteMercadoriaGeral != null && vlMercadoria.compareTo(vlLimiteMercadoriaGeral) > 0) {
                        Object[] param = new Object[]{"valor da mercadoria", FormatUtils.formatDecimal("#,##0.00", vlMercadoria, true), "geral",
                                FormatUtils.formatDecimal("#,##0.00", vlLimiteMercadoriaGeral, true)};

                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlLimiteMercadoriaGeral, param, LMS_04421);
                }
            }
        }

        return map;
    }

    private void adicionaDadosVl(TypedFlatMap map, Boolean lancarExcecao, Boolean buscarMsgValidacao,
                                 BigDecimal vlLimiteMercadoriaGeral, Object[] param, String excecao) {
                        if (lancarExcecao) {
            throw new BusinessException(excecao, param);
                        } else {
                            map.put("vlValido", Boolean.FALSE);
                            map.put("vlLimite", vlLimiteMercadoriaGeral);

                            if (buscarMsgValidacao) {
                map.put("msgValidacao", configuracoesFacade.getMensagem(excecao, param));
                map.put("chaveMsgValidacao", excecao);
                            }
                        }
                    }

    /**
     * Valida se o valor do frete informado é menor que o valor limite da filial ou do parâmetro geral, podendo silenciar a exceção.
     *
     * @param map
     * @return
     */
    public TypedFlatMap executeValidacaoLimiteValorFrete(TypedFlatMap map) {
        Boolean lancarExcecao = map.getBoolean("lancarExcecao", true);
        Boolean buscarMsgValidacao = map.getBoolean("buscarMsgValidacao", false);
        BigDecimal vlFrete = map.getBigDecimal("vlFrete");
        map.put("vlValido", Boolean.TRUE);

        if (vlFrete != null) {

			/* LMS-5176
             * Quando o método é chamado a partir de um JOB, neste caso do JOB do Sorter,
			 * a filial da sessão não é a filial correta para a busca do parâmetro.
			 */
            Long idFilialParametro = SessionUtils.getFilialSessao().getIdFilial();
            if (map.get("filialOrigem") != null) {
                idFilialParametro = ((Filial) map.get("filialOrigem")).getIdFilial();
            }

            BigDecimal vlLimiteFreteFilial = (BigDecimal) configuracoesFacade.getValorParametro(idFilialParametro,
                    ConstantesExpedicao.NM_PARAMETRO_FILIAL_LIMITE_VL_FRETE);

            if (vlLimiteFreteFilial != null) {
                if (vlFrete.compareTo(vlLimiteFreteFilial) > 0) {
                    Object[] param = new Object[]{"valor do frete", FormatUtils.formatDecimal("#,##0.00", vlFrete, true), "da filial",
                            FormatUtils.formatDecimal("#,##0.00", vlLimiteFreteFilial, true)};

                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlLimiteFreteFilial, param, LMS_04421);
                        }
            } else {
                BigDecimal vlLimiteFreteGeral = (BigDecimal) configuracoesFacade
                        .getValorParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_LIMITE_GERAL_VL_FRETE);

                if (vlLimiteFreteGeral != null && vlFrete.compareTo(vlLimiteFreteGeral) > 0) {
                        Object[] param = new Object[]{"valor do frete", FormatUtils.formatDecimal("#,##0.00", vlFrete, true), "geral",
                                FormatUtils.formatDecimal("#,##0.00", vlLimiteFreteGeral, true)};

                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlLimiteFreteGeral, param, LMS_04421);
                            }
                        }
                    }

        return map;
    }

    /**
     * Valida se o percentual do valor do frete em relação ao valor da mercadoria informados, é menor que o valor limite da filial ou do parâmetro
     * geral, podendo silenciar a exceção.
     *
     * @param map
     * @return
     */
    public TypedFlatMap executeValidacaoPercentualValorMercadoria(TypedFlatMap map) {
        Boolean lancarExcecao = map.getBoolean("lancarExcecao", true);
        Boolean buscarMsgValidacao = map.getBoolean("buscarMsgValidacao", false);
        BigDecimal vlMercadoria = map.getBigDecimal("vlMercadoria");
        BigDecimal vlFrete = map.getBigDecimal("vlFrete");
        map.put("vlValido", Boolean.TRUE);

        if (vlMercadoria != null && vlMercadoria.compareTo(BigDecimal.ZERO) > 0 && vlFrete != null && vlFrete.compareTo(BigDecimal.ZERO) > 0) {

			/* LMS-5176
			 * Quando o método é chamado a partir de um JOB, neste caso do JOB do Sorter, a filial da sessão não é a filial correta para a busca do parâmetro.
			 */
            Long idFilialParametro = SessionUtils.getFilialSessao().getIdFilial();
            if (map.get("filialOrigem") != null) {
                idFilialParametro = ((Filial) map.get("filialOrigem")).getIdFilial();
            }

            BigDecimal vlPercentualInformado = vlFrete.multiply(BigDecimalUtils.HUNDRED).divide(vlMercadoria, 2, RoundingMode.HALF_UP);

            BigDecimal vlPercentualFilial = (BigDecimal) configuracoesFacade.getValorParametro(idFilialParametro,
                    ConstantesExpedicao.NM_PARAMETRO_FILIAL_PERCENTUAL_VL_MERC);

            BigDecimal limiteMinVlFreteFilial = (BigDecimal) configuracoesFacade.getValorParametro(idFilialParametro,
                    ConstantesExpedicao.NM_PARAMETRO_FILIAL_LIMITE_MIN_VL_FRETE);

            if (vlPercentualFilial != null && limiteMinVlFreteFilial != null) {
                if (vlPercentualInformado.compareTo(vlPercentualFilial) > 0 && vlFrete.compareTo(limiteMinVlFreteFilial) > 0) {
                    Object[] param = new Object[]{"da filial", FormatUtils.formatDecimal("#,##0.00", vlPercentualFilial, true)};
                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlPercentualFilial, param, "LMS-04422");
                        }
            } else {
                BigDecimal vlPercentualGeral = (BigDecimal) configuracoesFacade
                        .getValorParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_PERCENTUAL_GERAL_VL_MERCADORIA);
                BigDecimal limiteMinVlFreteGeral = (BigDecimal) configuracoesFacade
                        .getValorParametroWithoutException(ConstantesExpedicao.NM_PARAMETRO_LIMITE_GERAL_MIN_VL_FRETE);

                if (vlPercentualGeral != null && vlPercentualInformado.compareTo(vlPercentualGeral) > 0 && vlFrete.compareTo(limiteMinVlFreteGeral) > 0) {
                        Object[] param = new Object[]{"geral", FormatUtils.formatDecimal("#,##0.00", vlPercentualGeral, true)};
                    adicionaDadosVl(map, lancarExcecao, buscarMsgValidacao, vlPercentualGeral, param, "LMS-04422");
                            }
                        }
                    }

        return map;
    }

    /**
     * Aplica o evict na entidade
     */
    public void evict(DoctoServico o) {
        getDoctoServicoDAO().getHibernateTemplate().evict(o);
    }

    /**
     * Método privado responsável por transformar uma lista de map's em
     * uma lista de TypedFlatMap's. As chaves também são alteradas substituindo
     * o caracter '_' pelo caracter '.'.
     *
     * @param l List contendo Map's
     * @return List contendo TypedFlatMap's
     */
    private List transformList(List l) {
        AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
        return a.transformListResult(l);
    }

    public List<Map<String, Object>> findConhecimentoByIdManifestoEntregaIdNotaCredito(Long idManifestoEntrega, Long idNotaCredito) {
        return getDoctoServicoDAO().findConhecimentoByIdManifestoEntregaIdNotaCredito(idManifestoEntrega, idNotaCredito);
    }

    public Map<String, Object> findDoctoServicoByNrFiscalRps(Long idFilial, Long nrFiscalRps) {
        return getDoctoServicoDAO().findDoctoServicoByNrFiscalRps(idFilial, nrFiscalRps);
    }

    public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
    }

    public void setDoctoServicoWorkflowService(DoctoServicoWorkflowService doctoServicoWorkflowService) {
        this.doctoServicoWorkflowService = doctoServicoWorkflowService;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
        this.impostoServicoService = impostoServicoService;
    }

    public void setCalcularDiasUteisBloqueioAgendamentoService(CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService) {
        this.calcularDiasUteisBloqueioAgendamentoService = calcularDiasUteisBloqueioAgendamentoService;
    }

    public Long findIdDoctoServicoByNrDoctoServico(Long nrDoctoServico, String nrIdentificacao, int destRmt) {
        return getDoctoServicoDAO().findIdDoctoServicoByNrDoctoServico(nrDoctoServico, nrIdentificacao, destRmt);
    }

    public List<DoctoServico> findDoctoServicoByTpControleCarga(Long idControleCarga, String tpControleCarga) {
        if ("C".equals(tpControleCarga)) {
            return getDoctoServicoDAO().findDoctoServicoByTpControleCargaEntrega(idControleCarga);
        } else if ("V".equals(tpControleCarga)) {
            return getDoctoServicoDAO().findDoctoServicoByTpControleCargaViagem(idControleCarga);
        }
        return new ArrayList<DoctoServico>();
    }

    public void processarDoctoServicoAssociadosControleCarga(Long idControleCarga, String tpControleCarga) {
        List<DoctoServico> list = findDoctoServicoByTpControleCarga(idControleCarga, tpControleCarga);

        for (DoctoServico doctoServico : list) {
            List<OcorrenciaDoctoServico> listOcorrenciaDocServico = doctoServico.getOcorrenciaDoctoServicos();
            for (OcorrenciaDoctoServico ocorrenciaDoctoServico : listOcorrenciaDocServico) {
                if (ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorLiberacao() == null
                        && ocorrenciaDoctoServico.getDhLiberacao() == null) {

                    FaseProcesso fp = new FaseProcesso();
                    fp.setCdFase(ConstantesExpedicao.CD_FASE_PROCESSO);

                    ocorrenciaDoctoServico.setFaseProcesso(faseProcessoService.findByCdFaseProcesso(fp).get(0));
                    ocorrenciaDoctoServicoService.store(ocorrenciaDoctoServico);
                }
            }

        }

    }
    // LMSA-7399 - 09/07/2018 - Inicio
    public List<DoctoServico> findDoctosByIdManifestoEntregaSemAssinaturaDigital(Long idManifesto) {
    	return getDoctoServicoDAO().findDoctosByIdManifestoEntregaSemAssinaturaDigital(idManifesto);
    }
    // LMSA-7399 - 09/07/2018 - Fim

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer findCodigoLocalizacaoMercadoria(Long idDoctoServico) {
        return getDoctoServicoDAO().findCodigoLocalizacaoMercadoria(idDoctoServico);
    }

    public Long executeAtualizarDoctoServicoRegerarRps(Long idMonitoramentoDocEletronic) {
        //Busca os dados para o recálculo dos tributos
        MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
        DoctoServico ds = findById(mde.getDoctoServico().getIdDoctoServico());

        if (!ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(ds.getTpDocumentoServico().getValue()) &&
                !ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(ds.getTpDocumentoServico().getValue())) {

            throw new BusinessException("LMS-01130");

        }

        Long idServicoAdicional = null;
        Long idServicoTributo = null;
        Long idMunicipioDestino = null;
        Cliente cliente = null;

        if (ds.getDevedorDocServs() != null && !ds.getDevedorDocServs().isEmpty()) {
            cliente = ds.getDevedorDocServs().get(0).getCliente();
        }

        if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(ds.getTpDocumentoServico().getValue())) {
            idServicoTributo = LongUtils.getLong((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));
            if (ds instanceof Conhecimento) {
                idMunicipioDestino = ((Conhecimento) ds).getMunicipioByIdMunicipioEntrega().getIdMunicipio();
            }
        } else if (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(ds.getTpDocumentoServico().getValue())) {
            if (ds.getServAdicionalDocServs() != null && !ds.getServAdicionalDocServs().isEmpty()) {
                idServicoAdicional = ds.getServAdicionalDocServs().get(0).getServicoAdicional().getIdServicoAdicional();
            }

            if (ds instanceof NotaFiscalServico) {
                idMunicipioDestino = ((NotaFiscalServico) ds).getMunicipio().getIdMunicipio();
            }
        }

        //Executar a rotina de cálculo de imposto CalculaImpostosdeServico da ET 04.01.01.01N
        Object[] tributos = calculoTributoService.montaTributos(cliente,
                idServicoAdicional,
                idServicoTributo,
                ds.getVlTotalDocServico(),
                SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio(),
                idMunicipioDestino,
                false);

        BigDecimal vlDevido = (BigDecimal) tributos[0];

        //Se o valor faturado retornado da rotina de cálculo do imposto for diferente do campo DOCTO_SERVICO.VL_FRETE_LIQUIDO
        if (ds.getVlLiquido().compareTo(vlDevido) != 0
                && ds.getDevedorDocServFats() != null && !ds.getDevedorDocServFats().isEmpty()) {
            //e DEVEDOR_DOC_SERV_FAT.TP_SITUACAO_COBRANCA (para DEVEDOR_DOC_SERV_FAT.ID_DOCTO_SERVICO = DOCTO_SERVICO.ID_DOCTO_SERVICO) for diferente de “C” e “P”
                DevedorDocServFat ddsf = ds.getDevedorDocServFats().get(0);
                if (!"C".equals(ddsf.getTpSituacaoCobranca().getValue()) && !"P".equals(ddsf.getTpSituacaoCobranca().getValue())) {
                    //visualizar a mensagem LMS-04434, abortando o processo.
                    throw new BusinessException("LMS-04434");
                }
            }


        List<ImpostoServico> impostosServicos = (List<ImpostoServico>) tributos[2];
        for (ImpostoServico is : impostosServicos) {
            if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(ds.getTpDocumentoServico().getValue())
                    && ds instanceof Conhecimento) {
                    is.setConhecimento((Conhecimento) ds);
            } else if (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(ds.getTpDocumentoServico().getValue())
                    && ds instanceof NotaFiscalServico) {
                    is.setNotaFiscalServico((NotaFiscalServico) ds);
                }
            }

        if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(ds.getTpDocumentoServico().getValue()) &&
                ds instanceof Conhecimento) {
            ((Conhecimento) ds).setImpostoServicos(impostosServicos);
        } else if (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(ds.getTpDocumentoServico().getValue()) &&
                ds instanceof NotaFiscalServico) {
            ((NotaFiscalServico) ds).setImpostoServicos(impostosServicos);
        }

        String IdDoctoServicos = impostoServicoService.retornarIdDoctoServicosParaUtilizarNaRemocaoDoImpostoServico(idMonitoramentoDocEletronic);
        impostoServicoService.removeImpostoServicoByMonitoramentoDocEletronico(IdDoctoServicos);

        getDoctoServicoDAO().executeAtualizarDoctoServicoRegerarRps(ds.getIdDoctoServico(), vlDevido);

        impostoServicoService.storeAll(impostosServicos);

        //LMS-8003
        return monitoramentoDocEletronicoService.executeGetProximoNumeroRPS(ds);

    }
    
    public void setCalculoTributoService(CalculoTributoService calculoTributoService) {
        this.calculoTributoService = calculoTributoService;
    }

    public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
        this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
    }

    public void setFaseProcessoService(FaseProcessoService faseProcessoService) {
        this.faseProcessoService = faseProcessoService;
    }

    public TypedFlatMap findDoctoServOutroManifestoViagem(Long idManifesto, Long idConhecimento, Long idFilialDestino) {
        return getDoctoServicoDAO().findDoctoServOutroManifestoViagem(idManifesto, idConhecimento, idFilialDestino);
    }

    public List<Map<String, Object>> findDoctoServicoSuggest(String sgFilial, Long nrDoctoServico, Long idEmpresa) {
        List<Map<String, Object>> list = getDoctoServicoDAO().findDoctoServicoSuggest(sgFilial, nrDoctoServico, idEmpresa);
        for (Map<String, Object> map : list) {

            map.put("tpDoctoServico", convertDomainValueToMap("DM_TIPO_DOCUMENTO_SERVICO", (String) map.get("tpDoctoServico")));

            map.put("dsDoctoServico", ConhecimentoUtils.formatConhecimento((String) map.get("sgFilial"), (Long) map.get("nrDoctoServico")));

            map.put("modal", convertDomainValueToMap("DM_MODAL", (String) map.get("modal")));
            map.put("abrangencia", convertDomainValueToMap("DM_ABRANGENCIA", (String) map.get("abrangencia")));
            map.put("tipoServico", convertMapTipoServico(map));
            map.put("filialOrigem", convertMapFilial((Long) map.remove("idFilialOrigem"), (String) map.remove("sgFilialOrigem"), (String) map.remove("nmFilialOrigem")));
            map.put("filialDestino", convertMapFilial((Long) map.remove("idFilialDestino"), (String) map.remove("sgFilialDestino"), (String) map.remove("nmFilialDestino")));
            map.put("finalidade", convertDomainValueToMap("DM_TIPO_CONHECIMENTO", (String) map.get("finalidade")));
            map.put("pedidoColeta", convertMapPedidoColeta(map));
            map.put("remetente", convertMapClienteRemetente(map));
            map.put("destinatario", convertMapClienteDestinatario(map));

            Map<String, Object> criteria = new HashMap<String, Object>();
            criteria.put("modal", ((Map<String, String>) map.get("modal")).get("value"));
            criteria.put("abrangencia", ((Map<String, String>) map.get("abrangencia")).get("value"));

            criteria.put("cliente.idCliente", ((Map<String, Object>) map.get("remetente")).get("idCliente"));

            List<InformacaoDoctoCliente> informacoes = informacaoDoctoClienteService.find(criteria);
            if (informacoes != null) {
                List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                for (InformacaoDoctoCliente doctoCliente : informacoes) {
                    Map<String, Object> mapInfDocCliente = new HashMap<String, Object>();
                    mapInfDocCliente.put("idInformacaoDoctoCliente", doctoCliente.getIdInformacaoDoctoCliente());
                    mapInfDocCliente.put("dsCampo", doctoCliente.getDsCampo());
                    mapInfDocCliente.put("tpCampo", doctoCliente.getTpCampo().getValue());
                    mapInfDocCliente.put("dsFormatacao", doctoCliente.getDsFormatacao());
                    mapInfDocCliente.put("nrTamanho", doctoCliente.getNrTamanho());
                    mapInfDocCliente.put("blOpcional", doctoCliente.getBlOpcional());
                    mapInfDocCliente.put("dsValorPadrao", doctoCliente.getDsValorPadrao());
                    mapInfDocCliente.put("blValorFixo", doctoCliente.getBlValorFixo());
                    result.add(mapInfDocCliente);
                }

                ((Map<String, Object>) map.get("remetente")).put("informacoesDoctoCliente", result);
            }
        }
        return list;

    }

    private HashMap<String, Object> convertMapTipoServico(Map<String, Object> map) {
        HashMap<String, Object> tipoServico = new HashMap<String, Object>();
        tipoServico.put("idTipoServico", map.remove("idTipoServico"));
        tipoServico.put("dsTipoServico", map.remove("dsTipoServico_i"));
        return tipoServico;
    }

    private HashMap<String, Object> convertMapPedidoColeta(Map<String, Object> map) {
        HashMap<String, Object> pedidoColeta = new HashMap<String, Object>();
        pedidoColeta.put("idPedidoColeta", map.remove("idPedidoColeta"));
        pedidoColeta.put("sgFilial", map.remove("sgFilial"));
        pedidoColeta.put("nrColeta", map.remove("nrColeta"));
        return pedidoColeta;
    }

    private HashMap<String, Object> convertMapClienteRemetente(Map<String, Object> map) {
        HashMap<String, Object> cliente = new HashMap<String, Object>();
        cliente.put("idCliente", map.remove("idPessoaRemetente"));
        cliente.put("nrIdentificacao", map.remove("nrIdentificacaoRemetente"));
        cliente.put("nmPessoa", map.remove("nmPessoaRemetente"));
        cliente.put("nmFantasia", map.remove("nmFantasiaRemetente"));
        cliente.put("nrConta", map.remove("nrContaRemetente"));
        return cliente;
    }

    private HashMap<String, Object> convertMapClienteDestinatario(Map<String, Object> map) {
        HashMap<String, Object> cliente = new HashMap<String, Object>();
        cliente.put("idCliente", map.remove("idPessoaDestinatario"));
        cliente.put("nrIdentificacao", map.remove("nrIdentificacaoDestinatario"));
        cliente.put("nmPessoa", map.remove("nmPessoaDestinatario"));
        cliente.put("nmFantasia", map.remove("nmFantasiaDestinatario"));
        cliente.put("nrConta", map.remove("nrContaDestinatario"));
        return cliente;
    }

    private HashMap<String, Object> convertMapFilial(Long idFilial, String sgFilial, String nmFilial) {
        HashMap<String, Object> filial = new HashMap<String, Object>();
        filial.put("idFilial", idFilial);
        filial.put("sgFilial", sgFilial);
        filial.put("nmFilial", nmFilial);
        return filial;
    }

    private Map<String, String> convertDomainValueToMap(String domain, String value) {
        DomainValue domainValue = domainValueService.findDomainValueByValue(domain, value);
        Map<String, String> dv = new HashMap<String, String>();
        dv.put("value", domainValue.getValue());
        dv.put("description", domainValue.getDescriptionAsString());
        return dv;
    }
    
    public DoctoServico findByTipoDoctoFilialNumero(String nrIdentificacaoFilialOrigem, Long nrDoctoServico, String tpDoctoServico){
    	return getDoctoServicoDAO().findByTipoDoctoFilialNumero(nrIdentificacaoFilialOrigem, nrDoctoServico, tpDoctoServico);
    }

    public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
        this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    }

    public List<TypedFlatMap> findDocumentoReembarcado(Long idFranquia, YearMonthDay dtInicioCompetencia, YearMonthDay dtFimCompetencia) {
        return getDoctoServicoDAO().findDocumentoReembarcado(idFranquia, dtInicioCompetencia, dtFimCompetencia);
    }

    public BigDecimal findValorCustoFreteAereo(Long idDoctoServico) {
        return getDoctoServicoDAO().findValorCustoFreteAereo(idDoctoServico);
    }

    public BigDecimal findValorCustoFreteCarreteiro(Long idDoctoServico) {
        return getDoctoServicoDAO().findValorCustoFreteCarreteiro(idDoctoServico);
    }


    public List<Map<String, Object>> findPreAwbDoctoServicoAtivo(Long idAwb) {
        return getDoctoServicoDAO().findPreAwbDoctoServicoAtivo(idAwb);
    }

    public List findNrDoctoServicoFilialByDoctosServico(List<Long> doctosServico) {
        return getDoctoServicoDAO().findNrDoctoServicoFilialByDoctosServico(doctosServico);
    }


    /**
     * LMS-6106 - Busca <tt>DoctoServico</tt> por tipo, filial de origem e
     * número. Retorna <tt>null</tt> se não encontrado.
     *
     * @param tpDocumentoServico tipo do documento de serviço
     * @param idFilialOrigem     id da filial de origem
     * @param nrDoctoServico     número do documento de serviço
     * @return <tt>DoctoServico</tt> ou <tt>null</tt> se não encontrado
     */
    public DoctoServico findDoctoServico(String tpDocumentoServico, Long idFilialOrigem, Long nrDoctoServico) {
        return getDoctoServicoDAO().findDoctoServico(tpDocumentoServico, idFilialOrigem, nrDoctoServico);
    }

    /**
     * LMS-6106 - Verifica se <tt>DoctoServico</tt> está relacionado a
     * determinado <tt>Boleto</tt>.
     *
     * @param idBoleto       id do <tt>Boleto</tt>
     * @param idDoctoServico id do <tt>DoctoServico</tt>
     * @return <tt>true</tt> se <tt>Boleto</tt> estiver relacionado ao
     * <tt>DoctoServico</tt>, <tt>false</tt> caso contrário
     */
    public boolean findBoletoDoctoServico(Long idBoleto, Long idDoctoServico) {
        return getDoctoServicoDAO().isBoletoDoctoServico(idBoleto, idDoctoServico);
    }

    /**
     * LMS-6106 - Busca dados de documentos de serviço relacionados a um
     * <tt>Boleto</tt> e marcados para exclusão (<tt>BL_EXCLUIR</tt>) no
     * processo de cancelamento parcial de fatura.
     *
     * @param idBoleto id do <tt>Boleto</tt>
     * @return lista de mapas para popular <tt>&lt;adsm:listbox /&gt;</tt>
     */
    public List<Map<String, Object>> findDoctoServicoListByBoleto(Long idBoleto) {
        return getDoctoServicoDAO().findDoctoServicoListByBoleto(idBoleto);
    }

    public List<Map<String, Object>> findDoctoServicoNaoFaturadoReport(TypedFlatMap tfm) {
        return getDoctoServicoDAO().findDoctoServicoNaoFaturadoReport(tfm);
    }

    public List<Map<String, Object>> findDoctoServicoNaoFaturado(TypedFlatMap parans) {
        return getDoctoServicoDAO().findDoctoServicoNaoFaturado(parans);
    }

    public Integer getRowCountDoctoServicoNaoFaturado(TypedFlatMap parans) {
        return getDoctoServicoDAO().getRowCountDoctoServicoNaoFaturado(parans);
    }

    public BigDecimal findVlFreteLiquidoByIdDoctoServico(Long idDoctoServico) {
        return getDoctoServicoDAO().findVlFreteLiquidoByIdDoctoServico(idDoctoServico);
    }

    public void setFotoOcorrenciaService(FotoOcorrenciaService fotoOcorrenciaService) {
        this.fotoOcorrenciaService = fotoOcorrenciaService;
    }
    
    public Integer getCountDoctosServicoReentregaByIdDoctoServico(Long idDoctoServico) {
        return getDoctoServicoDAO().getCountDoctosServicoReentregaByIdDoctoServico(idDoctoServico);
    }

    /**
     * @param criteria
     * @return
     */
    public Object[] findDetailImage(TypedFlatMap criteria) {
        return getDoctoServicoDAO().findDetailImage(criteria);
    }

    public DoctoServico findDoctoServicoByIdWhiteList(Long idWhiteList) {
        return getDoctoServicoDAO().findDoctoServicoByIdWhiteList(idWhiteList);
    }

    public List<DoctoServico> findDoctosServicoByIdControleCargaColetaParceira(Long idControleCarga) {
        return getDoctoServicoDAO().findDoctosServicoByIdControleCargaColetaParceira(idControleCarga);
    }

    public List<DoctoServico> findDoctosServicoByControleCargaNCPadrao(Long idControleCarga, List<Long> idPedidoColeta) {
        return getDoctoServicoDAO().findDoctosServicoByControleCargaNCPadrao(idControleCarga, idPedidoColeta);
    }


    public List<NotaFiscalConhecimento> findNotasFiscaisByDoctoServico(DoctoServico doctoServico) {
        return getDoctoServicoDAO().findNotasFiscaisByDoctoServico(doctoServico);
    }

    public List<Map<String, Object>> findDoctosByIdNotaCredito(Long idNotaCredito) {
        return getDoctoServicoDAO().findDoctosByIdNotaCredito(idNotaCredito);
    }

    public List<Map<String, Object>> findColeatsByIdNotaCredito(Long idNotaCredito) {
        return getDoctoServicoDAO().findColeatsByIdNotaCredito(idNotaCredito);
    }

    public List<Map<String, Object>> findColeatsByIdNotaCreditoRateio(Long idNotaCredito) {
        return getDoctoServicoDAO().findColeatsByIdNotaCreditoRateio(idNotaCredito);
    }

    public Long findMaxIdDoctoServico(Long idMonitoramentoDescarga) {
        return getDoctoServicoDAO().findMaxIdDoctoServico(idMonitoramentoDescarga);
    }

    public boolean findExisteDoctoServicoNegativo(Long idMonitoramentoDescarga) {
        return getDoctoServicoDAO().findExisteDoctoServicoNegativo(idMonitoramentoDescarga);
    }

    public List<Map<String, Object>> findDoctsManifestoEntregaByNotaCredito(Long idNotaCredito) {
        return getDoctoServicoDAO().findDoctsManifestoEntregaByNotaCredito(idNotaCredito);
    }

    public List<DoctoServico> findDoctosByIdControleCarga(Long idControleCarga) {
        return getDoctoServicoDAO().findDoctosByIdControleCarga(idControleCarga);
    }

    public List<Map<String, Object>> findDadosPodScanning(Long idDoctoServico) {
        return getDoctoServicoDAO().findDadosPodScanning(idDoctoServico);
    }

    public void executeAtualizarObComplementoLocalizacao(Long idConhecimento, String sgFilial, String nmFantasia){
        String obComplementoLocalizacao = sgFilial.concat(" - ").concat(nmFantasia);
        getDoctoServicoDAO().executeAtualizarObComplementoLocalizacao(idConhecimento, obComplementoLocalizacao);
    }

    public ConfiguracoesFacadeImpl getConfiguracoesFacadeImpl() {
        return configuracoesFacadeImpl;
    }

    public void setConfiguracoesFacadeImpl(ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
        this.configuracoesFacadeImpl = configuracoesFacadeImpl;
    }

	public void setConhecimentoFedexService(
			ConhecimentoFedexService conhecimentoFedexService) {
		this.conhecimentoFedexService = conhecimentoFedexService;
	}

    public void setDpeService(DpeService dpeService) {
        this.dpeService = dpeService;
    }

    public void setFeriadoService(FeriadoService feriadoService) {
        this.feriadoService = feriadoService;
    }    
    

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
}

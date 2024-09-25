package com.mercurio.lms.pendencia.model.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.util.AdsmHibernateUtils;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.EventoManifestoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.CalcularDiasUteisBloqueioAgendamentoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.EmailCartaUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.ComunicadoApreensao;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.dao.OcorrenciaDoctoServicoDAO;
import com.mercurio.lms.pendencia.model.util.ConstantesPendencia;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.FaseProcesso;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.LMBloqueioLiberacaoDAO;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.FaseProcessoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.pendencia.ocorrenciaDoctoServicoService"
 */
public class OcorrenciaDoctoServicoService extends CrudService<OcorrenciaDoctoServico, Long> {

    public static final int SEGUNDOS = 1000;
    public static final int MINUTOS = 60;
    private static final String OCORRENCIA_DOCTO_SERVICO = "OCORRENCIA_DOCTO_SERVICO";
    private static final String DOCTO_SERVICO = "DOCTO_SERVICO";
    private static final int LIMITE_OPERACAO = 1000;
    private static final short CD_AGUARDANDO_AGENDAMENTO_ENTREGA = 119;

    private static final String TEXT_HTML= "text/html; charset='utf-8'";
    
    private ReportExecutionManager reportExecutionManager;
    private LMBloqueioLiberacaoDAO lmBloqueioLiberacaoDao;
    private DoctoServicoService doctoServicoService;
    private MoedaService moedaService;
    private ComunicadoApreensaoService comunicadoApreensaoService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
    private EventoService eventoService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private ManifestoService manifestoService;
    private EventoManifestoService eventoManifestoService;
    private LiberacaoBloqueioService liberacaoBloqueioService;
    private ConfiguracoesFacade configuracoesFacade;
    private EnderecoPessoaService enderecoPessoaService;
    private DiaUtils diaUtils;
    private IntegracaoJmsService integracaoJmsService;
    private FaseProcessoService faseProcessoService;
    private CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private ContatoService contatoService;
    private ConhecimentoService conhecimentoService;

    public void setContatoService(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }

    public void setLmBloqueioLiberacaoDao(
            LMBloqueioLiberacaoDAO lmBloqueioLiberacaoDao) {
        this.lmBloqueioLiberacaoDao = lmBloqueioLiberacaoDao;
    }

    public EventoService getEventoService() {
        return eventoService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
        return incluirEventosRastreabilidadeInternacionalService;
    }

    public void setIncluirEventosRastreabilidadeInternacionalService(
            IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
        this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
    }

    public ComunicadoApreensaoService getComunicadoApreensaoService() {
        return comunicadoApreensaoService;
    }

    public void setComunicadoApreensaoService(
            ComunicadoApreensaoService comunicadoApreensaoService) {
        this.comunicadoApreensaoService = comunicadoApreensaoService;
    }

    public OcorrenciaEntregaService getOcorrenciaEntregaService() {
        return ocorrenciaEntregaService;
    }

    public void setOcorrenciaEntregaService(
            OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public OcorrenciaPendenciaService getOcorrenciaPendenciaService() {
        return ocorrenciaPendenciaService;
    }

    public void setOcorrenciaPendenciaService(
            OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public DoctoServicoService getDoctoServicoService() {
        return doctoServicoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public MoedaService getMoedaService() {
        return moedaService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

    public ManifestoService getManifestoService() {
        return manifestoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public EventoManifestoService getEventoManifestoService() {
        return eventoManifestoService;
    }

    public void setEventoManifestoService(
            EventoManifestoService eventoManifestoService) {
        this.eventoManifestoService = eventoManifestoService;
    }

    public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
        return manifestoEntregaDocumentoService;
    }

    public void setManifestoEntregaDocumentoService(
            ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }

    public ConfiguracoesFacade getConfiguracoesFacade() {
        return configuracoesFacade;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setReportExecutionManager(
            ReportExecutionManager reportExecutionManager) {
        this.reportExecutionManager = reportExecutionManager;
    }

    public CalcularDiasUteisBloqueioAgendamentoService getCalcularDiasUteisBloqueioAgendamentoService() {
        return calcularDiasUteisBloqueioAgendamentoService;
    }

    public void setCalcularDiasUteisBloqueioAgendamentoService(
            CalcularDiasUteisBloqueioAgendamentoService calcularDiasUteisBloqueioAgendamentoService) {
        this.calcularDiasUteisBloqueioAgendamentoService = calcularDiasUteisBloqueioAgendamentoService;
    }

    /**
     * Recupera uma instância de <code>OcorrenciaDoctoServico</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws
     */
    public OcorrenciaDoctoServico findById(java.lang.Long id) {
        return (OcorrenciaDoctoServico) super.findById(id);
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
    public java.io.Serializable store(OcorrenciaDoctoServico bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setOcorrenciaDoctoServicoDAO(OcorrenciaDoctoServicoDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private OcorrenciaDoctoServicoDAO getOcorrenciaDoctoServicoDAO() {
        return (OcorrenciaDoctoServicoDAO) getDao();
    }

    public OcorrenciaDoctoServico executeVerificaComunicadoApreensao(Long idDoctoServico) {
        List list = this.getOcorrenciaDoctoServicoDAO().findVerificaComunicadoApreensao(idDoctoServico);

        Iterator iterator = list.iterator();
        OcorrenciaDoctoServico ocorrenciaDoctoServico = null;
        if (iterator.hasNext()) {
            ocorrenciaDoctoServico = (OcorrenciaDoctoServico) iterator.next();
            ajustaEmailRemetente(ocorrenciaDoctoServico);
        }
        return ocorrenciaDoctoServico;
    }

    private void ajustaEmailRemetente(OcorrenciaDoctoServico ocorrenciaDoctoServico) {
        String email = getDsEmailFromContato(SessionUtils.getFilialSessao().getIdFilial());
        if (!isClienteRemetente(ocorrenciaDoctoServico) && StringUtils.isNotBlank(email)) {
            ocorrenciaDoctoServico.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().setDsEmail(email);
        }
    }

    private boolean isClienteRemetente(OcorrenciaDoctoServico ocorrenciaDoctoServico) {
        return ocorrenciaDoctoServico.getDoctoServico() != null && ocorrenciaDoctoServico.getDoctoServico().getClienteByIdClienteRemetente() != null
                && StringUtils.isNotBlank(ocorrenciaDoctoServico.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getDsEmail());
    }

    private String getDsEmailFromContato(Long idFilial) {
        List<Contato> contatos = contatoService.findContatosByIdPessoaTpContato(idFilial, ConstantesPendencia.OCORRENCIA_COLETA);
        if (contatos != null && !contatos.isEmpty()) {
            return contatos.get(0).getDsEmail();
        }
        return "";
    }


    public TypedFlatMap findByIdToComunicadoApreensaoAoCliente(Long idOcorrenciaDoctoServico) {
        TypedFlatMap retorno = this.getOcorrenciaDoctoServicoDAO().findByIdToComunicadoApreensaoAoCliente(idOcorrenciaDoctoServico);
        Filial filialSessao = SessionUtils.getFilialSessao();

        //Formatação dos nrIdentificação
        String nrIdentificacaoRemetente = retorno.getString(ConstantesPendencia.DOCTO_SERVICO_CLIENTE_BY_ID_CLIENTE_REMETENTE_PESSOA_NR_IDENTIFICACAO);
        String tpIdentificacaoRemetente = retorno.getString(ConstantesPendencia.DOCTO_SERVICO_CLIENTE_BY_ID_CLIENTE_REMETENTE_PESSOA_TP_IDENTIFICACAO_VALUE);
        String nrIdentificacaoFormatadoRemetente = FormatUtils.formatIdentificacao(tpIdentificacaoRemetente, nrIdentificacaoRemetente);
        retorno.put(ConstantesPendencia.DOCTO_SERVICO_CLIENTE_BY_ID_CLIENTE_REMETENTE_PESSOA_NR_IDENTIFICACAO, nrIdentificacaoFormatadoRemetente);

        String nrIdentificacaoDestinatario = retorno.getString(ConstantesPendencia.DOCTO_SERVICO_CLIENTE_BY_ID_CLIENTE_DESTINATARIO_PESSOA_NR_IDENTIFICACAO);
        String tpIdentificacaoDestinatario = retorno.getString(ConstantesPendencia.DOCTO_SERVICO_CLIENTE_BY_ID_CLIENTE_DESTINATARIO_PESSOA_TP_IDENTIFICACAO_VALUE);
        String nrIdentificacaoFormatadoDestinatario = FormatUtils.formatIdentificacao(tpIdentificacaoDestinatario, nrIdentificacaoDestinatario);
        retorno.put(ConstantesPendencia.BY_ID_CLIENTE_DESTINATARIO_PESSOA_NR_IDENTIFICACAO, nrIdentificacaoFormatadoDestinatario);

        Usuario usuario = SessionUtils.getUsuarioLogado();

        retorno.put(ConstantesPendencia.FILIAL_SG_FILIAL, filialSessao.getSgFilial());
        retorno.put(ConstantesPendencia.FILIAL_PESSOA_NM_FANTASIA, filialSessao.getPessoa().getNmFantasia());
        retorno.put(ConstantesPendencia.USUARIO_NR_MATRICULA, usuario.getNrMatricula());
        retorno.put(ConstantesPendencia.USUARIO_NM_USUARIO, usuario.getNmUsuario());

        String email = getDsEmailFromContato(SessionUtils.getFilialSessao().getIdFilial());

        retorno.put(ConstantesPendencia.USUARIO_DS_EMAIL, !StringUtils.isEmpty(email) ? email : usuario.getDsEmail());

        return retorno;
    }

    /**
     * Retorna uma ocorrencia de documento de servico.
     *
     * @param idDoctoServico
     * @return
     */
    public TypedFlatMap findOcorrenciaByIdDoctoServico(Long idDoctoServico) {

        OcorrenciaDoctoServico ocorrenciaDoctoServico = this.findLastOcorrenciaDoctoServicoByIdDoctoServico(idDoctoServico);
        ManifestoEntregaDocumento manifestoEntregaDocumento = manifestoEntregaDocumentoService.findLastManifestoEntregaDocumentoByIdDoctoServico(idDoctoServico);

        TypedFlatMap result = new TypedFlatMap();
        result.put(ConstantesPendencia.ID_DOCTO_SERVICO, idDoctoServico);

        if (ocorrenciaDoctoServico != null && manifestoEntregaDocumento != null) {
            if (ocorrenciaDoctoServico.getDhLiberacao() != null) {
                return retornarValorDhBloqueioNaoNulo(ocorrenciaDoctoServico, manifestoEntregaDocumento, result);
            } else {
                return retornarValorDhBloqueioNulo(ocorrenciaDoctoServico, manifestoEntregaDocumento, result);
            }
        } else if (ocorrenciaDoctoServico != null) {
            if (ocorrenciaDoctoServico.getDhLiberacao() != null) {
                result.put(ConstantesPendencia.CD_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorLiberacao().getCdOcorrencia());
                result.put(ConstantesPendencia.DS_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorLiberacao().getDsOcorrencia());
                result.put(ConstantesPendencia.DH_BLOQUEIO, ocorrenciaDoctoServico.getDhLiberacao());
            } else {
                result.put(ConstantesPendencia.CD_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia());
                result.put(ConstantesPendencia.DS_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getDsOcorrencia());
                result.put(ConstantesPendencia.DH_BLOQUEIO, ocorrenciaDoctoServico.getDhBloqueio());
            }
            return result;
        } else if (manifestoEntregaDocumento != null) {
            if (manifestoEntregaDocumento.getOcorrenciaEntrega() != null) {
                result.put(ConstantesPendencia.TP_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue());
                result.put(ConstantesPendencia.CD_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
                result.put(ConstantesPendencia.DS_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getDsOcorrenciaEntrega());
            }
            result.put(ConstantesPendencia.DH_BLOQUEIO, manifestoEntregaDocumento.getDhInclusao());
            return result;
        }

        return null;
    }

    private TypedFlatMap retornarValorDhBloqueioNaoNulo(OcorrenciaDoctoServico ocorrenciaDoctoServico, ManifestoEntregaDocumento manifestoEntregaDocumento, TypedFlatMap result) {
        if ((ocorrenciaDoctoServico.getDhLiberacao().getMillis()) >= (manifestoEntregaDocumento.getDhInclusao().getMillis())) {
            result.put(ConstantesPendencia.CD_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorLiberacao().getCdOcorrencia());
            result.put(ConstantesPendencia.DS_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorLiberacao().getDsOcorrencia());
            result.put(ConstantesPendencia.DH_BLOQUEIO, ocorrenciaDoctoServico.getDhLiberacao());
            return result;
        } else {
            result.put(ConstantesPendencia.TP_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue());
            result.put(ConstantesPendencia.CD_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
            result.put(ConstantesPendencia.DS_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getDsOcorrenciaEntrega());
            result.put(ConstantesPendencia.DH_BLOQUEIO, manifestoEntregaDocumento.getDhInclusao());
            return result;
        }
    }

    private TypedFlatMap retornarValorDhBloqueioNulo(OcorrenciaDoctoServico ocorrenciaDoctoServico, ManifestoEntregaDocumento manifestoEntregaDocumento, TypedFlatMap result) {
        if ((ocorrenciaDoctoServico.getDhBloqueio().getMillis()) >= (manifestoEntregaDocumento.getDhInclusao().getMillis())) {
            result.put(ConstantesPendencia.CD_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia());
            result.put(ConstantesPendencia.DS_OCORRENCIA, ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getDsOcorrencia());
            result.put(ConstantesPendencia.DH_BLOQUEIO, ocorrenciaDoctoServico.getDhBloqueio());
            return result;
        } else {
            if (manifestoEntregaDocumento.getOcorrenciaEntrega() != null) {
                result.put(ConstantesPendencia.TP_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getTpOcorrencia().getValue());
                result.put(ConstantesPendencia.CD_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getCdOcorrenciaEntrega());
                result.put(ConstantesPendencia.DS_OCORRENCIA, manifestoEntregaDocumento.getOcorrenciaEntrega().getDsOcorrenciaEntrega());
            }
            result.put(ConstantesPendencia.DH_BLOQUEIO, manifestoEntregaDocumento.getDhInclusao());
            return result;
        }
    }

    private boolean validateBloqueioDoctoServico(Long idDoctoServico, DateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("A data passada como parametro não pode ser nulla");
        }

        List dates = new ArrayList();
        dates.add(date);

        return getRowCountDoctoServicoWithBloqueio(idDoctoServico, dates) == 1;
    }

    /**
     * Verifica se há bloqueio para o documento de serviço
     *
     * @param idDoctoServico
     * @param date
     * @return
     */
    public BusinessException validateTemBloqueioDoctoServico(Long idDoctoServico, DateTime date) {
        DateTime dataValidateTemBloqDoctoServ = date;
        if (date == null) {
            dataValidateTemBloqDoctoServ = JTDateTimeUtils.getDataHoraAtual();
        }
        Boolean hasBloqueio = validateBloqueioDoctoServico(idDoctoServico, dataValidateTemBloqDoctoServ);
        if (hasBloqueio) {
            DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
            String doctoServicoFomatado = formataDoctoServico(doctoServico);
            String localizacaoDoctoServicoFomatado = formataLocalizacaoDoctoServico(doctoServico);
            return new BusinessException(ConstantesPendencia.LMS_ERR_DOCUMENTO_BLOQUEADO, new String[]{doctoServicoFomatado, localizacaoDoctoServicoFomatado});
        }
        return null;
    }

    /**
     * Verifica se o documento de servico esta bloqueado nas datas informadas.
     * E retorna o numero de data em que ele esta bloqueado.
     *
     * @param idDoctoServico
     * @param dates
     * @return
     */
    public Integer getRowCountDoctoServicoWithBloqueio(Long idDoctoServico, List dates) {
        if (dates.isEmpty()) {
            throw new IllegalArgumentException("A lista de datas não pode ser vazia");
        }
        return getOcorrenciaDoctoServicoDAO().getRowCountDoctoServicoWithBloqueio(idDoctoServico, dates);
    }

    /**
     * Gera e-mail para comunicado de apreensão ao cliente
     *
     * @author Rodrigo Antunes
     */
    public void executeEnviarEmailComunicadoApreensaoCliente(TypedFlatMap tfm) {

        // Grava evento na tabela EVENTO_DOCUMENTO_SERVICO para o documento em questão.
        Filial filialSessao = SessionUtils.getFilialSessao();
        Long idDoctoServico = tfm.getLong(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_DOCTO_SERVICO_ID_DOCTO_SERVICO);
        DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
        String strDoctoServico = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), ConstantesPendencia.PAD_SIZE_EVENTO_DOC_SERV, '0');

        this.getIncluirEventosRastreabilidadeInternacionalService().generateEventoDocumento(
                ConstantesSim.EVENTO_COMUNICADO_APREENSAO_ENVIADO,
                idDoctoServico,
                filialSessao.getIdFilial(),
                strDoctoServico,
                JTDateTimeUtils.getDataHoraAtual(),
                null,
                buildDsObservacao(filialSessao, tfm.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_CLIENTE_BY_ID_CLI_DEST_PES_DS_EMAIL),
                        tfm.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_CLIENTE_BY_ID_CLI_REM_PES_DS_EMAIL)),
                doctoServico.getTpDocumentoServico().getValue());

        sendEmailComunicadoApreensaoCliente(tfm);
    }

    private String buildDsObservacao(Filial filial, String emailPrimeiroDest, String emailSegundoDest) {
        return filial.getSiglaNomeFilial() + ", Email do Destinatário: " + emailPrimeiroDest + ", " + emailSegundoDest;
    }

    /**
     * Solicitação CQPRO00005864 da Integração.
     * Método utilizado apenas pela Integração, caso contrário, utilizar o
     * método executeRegistrarOcorrenciaDoctoServico(TypedFlatMap mapBean)...
     *
     * @param ocorrenciaPendencia
     * @param doctoServico
     * @param dataHora
     */
    public void executeRegistrarOcorrenciaDoctoServico(OcorrenciaPendencia ocorrenciaPendencia, DoctoServico doctoServico, DateTime dataHora) {
        Filial filialSessao = SessionUtils.getFilialSessao();
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        OcorrenciaDoctoServico ocorrenciaDoctoServicoBuscado = null;
        ocorrenciaDoctoServicoBuscado = this.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());

        // Quando a ocorrência informada for de BLOQUEIO.
        if (ConstantesPendencia.STATUS_PENDENCIA_BLOQUEIO.equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {

            if (ocorrenciaDoctoServicoBuscado != null) {
                //Já existe uma ocorrencia de bloqueio para o documento de servico informado.
                throw new BusinessException(ConstantesPendencia.LMS_ERR_JA_EXISTE_OCORRENCIA_BLOQUEIO, new String[]{this.formataDoctoServico(doctoServico)});
            }
            OcorrenciaDoctoServico ocorrenciaDoctoServico = new OcorrenciaDoctoServico();
            ocorrenciaDoctoServico.setDoctoServico(doctoServico);
            ocorrenciaDoctoServico.setFilialByIdFilialBloqueio(filialSessao);
            ocorrenciaDoctoServico.setOcorrenciaPendenciaByIdOcorBloqueio(ocorrenciaPendencia);
            ocorrenciaDoctoServico.setDhBloqueio(dataHora);
            ocorrenciaDoctoServico.setComunicadoApreensao(null);
            ocorrenciaDoctoServico.setUsuarioBloqueio(usuarioLogado);
            ocorrenciaDoctoServico.setFaseProcesso(this.getFaseProcessoService().findByIdDoctoServico(doctoServico.getIdDoctoServico()));
            this.store(ocorrenciaDoctoServico);

            doctoServico.setBlBloqueado(Boolean.TRUE);
            this.getDoctoServicoService().store(doctoServico);
        }

        // Quando a ocorrência informada for de LIBERAÇÃO.
        if (ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {
            //Buscar ocorrencia docto servico com idDoctoServico igual ao recebido
            //e dhLiberacao vazio.
            if (ocorrenciaDoctoServicoBuscado == null) {
                //Não existe uma ocorrência de bloqueio a ser liberada para o Documento de Serviço informado.
                throw new BusinessException(ConstantesPendencia.LMS_ERR_NAO_EXISTE_BLOQUEIO_LIBERACAO_DOCTO_SERV, new String[]{this.formataDoctoServico(doctoServico)});
            }

            ocorrenciaDoctoServicoBuscado.setFilialByIdFilialLiberacao(filialSessao);
            ocorrenciaDoctoServicoBuscado.setOcorrenciaPendenciaByIdOcorLiberacao(ocorrenciaPendencia);
            ocorrenciaDoctoServicoBuscado.setDhLiberacao(dataHora);
            ocorrenciaDoctoServicoBuscado.setComunicadoApreensao(null);
            ocorrenciaDoctoServicoBuscado.setUsuarioLiberacao(usuarioLogado);
            this.store(ocorrenciaDoctoServicoBuscado);

            // obtendo dados do municipio destino
            YearMonthDay dtBloqueio = ocorrenciaDoctoServicoBuscado.getDhBloqueio().toYearMonthDay();
            Long idFilialDestino = doctoServico.getFilialByIdFilialDestino().getIdFilial();
            EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idFilialDestino);

            // calculando dias de bloqueio
            short nrDiasBloqueio = doctoServico.getNrDiasBloqueio() != null ? doctoServico.getNrDiasBloqueio() : 0;
            short nrDiasUteis = (short) diaUtils.findQtdeDiasUteisEntreDatas(dtBloqueio, dataHora.toYearMonthDay(), enderecoPessoa.getMunicipio().getIdMunicipio());
            nrDiasBloqueio += nrDiasUteis;
            doctoServico.setNrDiasBloqueio(nrDiasBloqueio);

            doctoServico.setBlBloqueado(Boolean.FALSE);
            this.getDoctoServicoService().store(doctoServico);
        }
    }

    public DateTime executeRegistrarOcorrenciaDoctoServico(TypedFlatMap mapBean) {

        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
        if(mapBean.getDateTime(ConstantesPendencia.DH_OCORRENCIA) != null){
        	dataHora = mapBean.getDateTime(ConstantesPendencia.DH_OCORRENCIA);
        }
        Filial filialSessao = SessionUtils.getFilialSessao();
        DoctoServico doctoServico = this.getDoctoServicoService().findById(mapBean.getLong(ConstantesPendencia.DOCTO_SERVICO_ID_DOCTO_SERVICO));
        String strDoctosServico = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), ConstantesPendencia.PAD_SIZE_EVENTO_DOC_SERV, '0');

        //
        validateRegistroOcorrenciaDoctoServico(mapBean, doctoServico, strDoctosServico);

        // Grava na tabela COMUNICADO_APREENSAO.
        ComunicadoApreensao comunicadoApreensao = null;

        if (mapBean.getBoolean(ConstantesPendencia.OCORRENCIA_PENDENCIA_BL_APREENSAO)) {
            String numeroTermoApreensao = mapBean.getString(ConstantesPendencia.NUMERO_TERMO_APREENSAO);
            YearMonthDay dtTermoApreensao = mapBean.getYearMonthDay(ConstantesPendencia.DT_TERMO_APREENSAO);
            Long idMoeda = mapBean.getLong(ConstantesPendencia.MOEDA_ID_MOEDA);
            BigDecimal valorMulta = mapBean.getBigDecimal(ConstantesPendencia.VALOR_MULTA);
            String motivoAlegado = mapBean.getString(ConstantesPendencia.MOTIVO_ALEGADO);
            //Obrigatoriedade de todos os campos.

            if (StringUtils.isNotBlank(numeroTermoApreensao)
                    && dtTermoApreensao != null
                    && idMoeda != null
                    && valorMulta != null
                    && (StringUtils.isNotBlank(motivoAlegado))) {
                comunicadoApreensao = new ComunicadoApreensao();
                comunicadoApreensao.setNrTermoApreensao(mapBean.getString(ConstantesPendencia.NUMERO_TERMO_APREENSAO));
                comunicadoApreensao.setDtOcorrencia(mapBean.getYearMonthDay(ConstantesPendencia.DT_TERMO_APREENSAO));
                comunicadoApreensao.setMoeda(this.getMoedaService().findById(mapBean.getLong(ConstantesPendencia.MOEDA_ID_MOEDA)));
                comunicadoApreensao.setVlMulta(mapBean.getBigDecimal(ConstantesPendencia.VALOR_MULTA));
                comunicadoApreensao.setDsMotivoAlegado(mapBean.getString(ConstantesPendencia.MOTIVO_ALEGADO));
                this.getComunicadoApreensaoService().store(comunicadoApreensao);
            } else if (StringUtils.isNotBlank(numeroTermoApreensao)
                    || dtTermoApreensao != null
                    || valorMulta != null
                    || StringUtils.isNotBlank(motivoAlegado)) {
                //Para que um comunicado de apreensão seja salvo (opcional), todos os seus respectivos campos
                //precisam estar preenchidos, caso contrário, deixar os campos vazios.
                throw new BusinessException(ConstantesPendencia.LMS_ERR_DADOS_COMUNICADO_APREENSAO_INVALIDO);
            }
        }

        // Grava evento na tabela EVENTO_DOCUMENTO_SERVICO para o documento em questão.
        Evento evento = this.getEventoService().findByIdInitLazyProperties(mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_EVENTO_ID_EVENTO), false);

        if (doctoServico.getFilialByIdFilialDestino() == null) {
            throw new BusinessException(ConstantesPendencia.LMS_ERR_FILIAL_OCORRENCIA_DESTINO_NULA);
        }

        this.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(doctoServico.getIdDoctoServico(),
                mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_ID_OCORRENCIA_PENDENCIA), comunicadoApreensao, dataHora, null);

        // Chama a rotina de geração de evento (nessa chamada da rotina ela recebe o idOcorrenciaPendencia)
        this.getIncluirEventosRastreabilidadeInternacionalService().generateEventoDocumento(
                evento.getCdEvento(), doctoServico.getIdDoctoServico(), filialSessao.getIdFilial(),
                strDoctosServico, dataHora, null, filialSessao.getSiglaNomeFilial(),
                doctoServico.getTpDocumentoServico().getValue(), null, mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_ID_OCORRENCIA_PENDENCIA),
                mapBean.getBoolean("blOcorrenciaDocumentoManual"), SessionUtils.getUsuarioLogado());

        return dataHora;
    }

    /**
     * LMS-4333
     *
     * @param mapBean
     * @param doctoServico
     * @param strDoctosServico
     */
    private void validateRegistroOcorrenciaDoctoServico(TypedFlatMap mapBean, DoctoServico doctoServico, String strDoctosServico) {

        if (!doctoServico.getBlBloqueado() && ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            throw new BusinessException(ConstantesPendencia.LMS_ERR_NAO_EXISTE_BLOQUEIO_LIBERACAO_DOCTO_SERV, new Object[]{strDoctosServico});
        }

        if (doctoServico.getBlBloqueado() && ConstantesPendencia.STATUS_PENDENCIA_BLOQUEIO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            throw new BusinessException(ConstantesPendencia.LMS_ERR_JA_EXISTE_OCORRENCIA_BLOQUEIO, new Object[]{strDoctosServico});
        }
    }

    public void executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(Long idDoctoServico, Long idOcorrenciaPendencia, ComunicadoApreensao comunicadoApreensao, FaseProcesso faseProcesso) {
        this.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(idDoctoServico, idOcorrenciaPendencia, comunicadoApreensao, null, faseProcesso);
    }

    public void executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(Long idDoctoServico, Long idOcorrenciaPendencia, ComunicadoApreensao comunicadoApreensao, DateTime dhOcorrencia, FaseProcesso faseProcesso) {
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
        Filial filialSessao = SessionUtils.getFilialSessao();
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();

        if (dhOcorrencia != null) {
            dataHora = dhOcorrencia;
        }

        DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);

        OcorrenciaDoctoServico ocorrenciaDoctoServicoBuscado = this.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
        OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findById(idOcorrenciaPendencia);

        if ("B".equals(ocorrenciaPendencia.getTpOcorrencia().getValue()) && doctoServico.getBlBloqueado() && ocorrenciaDoctoServicoBuscado != null) {
            return;
        }

        if (ocorrenciaPendencia.getTpOcorrencia() != null) {
            // Quando a ocorrência informada for de BLOQUEIO.
            if ("B".equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {

                OcorrenciaDoctoServico ocorrenciaDoctoServico = new OcorrenciaDoctoServico();
                ocorrenciaDoctoServico.setDoctoServico(doctoServico);
                ocorrenciaDoctoServico.setFilialByIdFilialBloqueio(filialSessao);
                ocorrenciaDoctoServico.setOcorrenciaPendenciaByIdOcorBloqueio(ocorrenciaPendencia);
                ocorrenciaDoctoServico.setDhBloqueio(dataHora);
                ocorrenciaDoctoServico.setComunicadoApreensao(comunicadoApreensao);
                ocorrenciaDoctoServico.setUsuarioBloqueio(usuarioLogado);

                if (faseProcesso != null) {
                    ocorrenciaDoctoServico.setFaseProcesso(faseProcesso);
                } else {
                    ocorrenciaDoctoServico.setFaseProcesso(this.getFaseProcessoService().findByIdDoctoServico(idDoctoServico));
                }

                this.store(ocorrenciaDoctoServico);

                doctoServico.setBlBloqueado(Boolean.TRUE);
                if (!ocorrenciaPendencia.getCdOcorrencia().equals(ConstantesPendencia.CD_OCORRENCIA_PENDENCIA_AGENDAMENTO_BLOQUEIO) && ocorrenciaPendencia != null && ocorrenciaPendencia.getEvento() != null) {
                    LocalizacaoMercadoria localizacaoMercadoria = ocorrenciaPendencia.getEvento().getLocalizacaoMercadoria();
                    doctoServico.setLocalizacaoMercadoria(localizacaoMercadoria);
                }


                this.getDoctoServicoService().store(doctoServico);
            }

            // Quando a ocorrência informada for de LIBERAÇÃO.
            if ("L".equals(ocorrenciaPendencia.getTpOcorrencia().getValue())) {

                if (!doctoServico.getBlBloqueado()) {
                    return;
                }

                Long diferenca = dataHora.getMillis() - ocorrenciaDoctoServicoBuscado.getDhBloqueio().getMillis();
                Long minutos = (diferenca / SEGUNDOS) / MINUTOS;

                if (minutos < 1) {
                    throw new BusinessException(ConstantesPendencia.LMS_ERR_IMPOSSIVEL_DESB_MESMA_HORA_MINUTO);
                }

                ocorrenciaDoctoServicoBuscado.setFilialByIdFilialLiberacao(filialSessao);
                ocorrenciaDoctoServicoBuscado.setOcorrenciaPendenciaByIdOcorLiberacao(ocorrenciaPendencia);
                ocorrenciaDoctoServicoBuscado.setDhLiberacao(dataHora);
                ocorrenciaDoctoServicoBuscado.setComunicadoApreensao(comunicadoApreensao);
                ocorrenciaDoctoServicoBuscado.setUsuarioLiberacao(usuarioLogado);
                this.store(ocorrenciaDoctoServicoBuscado);
                this.flush();
                // calculando dias de bloqueio LMS-4187
                calcularDiasUteisBloqueioAgendamentoService.executeCalcularDiasUteisBloqueioAgendamento(doctoServico, null);

                doctoServico.setBlBloqueado(Boolean.FALSE);
                if (!ocorrenciaPendencia.getCdOcorrencia().equals(ConstantesPendencia.CD_OCORRENCIA_PENDENCIA_AGENDAMENTO_LIBERACAO)
                        && ocorrenciaPendencia != null && ocorrenciaPendencia.getEvento() != null) {
                    LocalizacaoMercadoria localizacaoMercadoria = ocorrenciaPendencia.getEvento().getLocalizacaoMercadoria();
                    doctoServico.setLocalizacaoMercadoria(localizacaoMercadoria);
                }

                this.getDoctoServicoService().store(doctoServico);
            }
        }
    }

    public DateTime executeRegistrarOcorrenciaDoctoServicoCC(TypedFlatMap mapBean) {
    	
    	Boolean blIntegracaoFedex = mapBean.getBoolean("blIntegracaoFedex") != null ? mapBean.getBoolean("blIntegracaoFedex") : false;

        AdsmHibernateUtils.setFlushModeToManual(this.getOcorrenciaDoctoServicoDAO().getAdsmHibernateTemplate());

        List<Manifesto> manifestos = findManifestos(mapBean);

        AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations = new AdsmNativeBatchSqlOperations(this.getDao(), LIMITE_OPERACAO);
        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(DOCTO_SERVICO, "BL_BLOQUEADO",
                ConstantesPendencia.STATUS_PENDENCIA_BLOQUEIO.equals(
                        mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))
        );

        ComunicadoApreensao comunicadoApreensao = storeComunicadoApreensao(mapBean);

        if (ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            carregarOperacoesLiberacao(mapBean, adsmNativeBatchSqlOperations, comunicadoApreensao);
        }

        Evento evento = this.getEventoService().findByIdInitLazyProperties(mapBean.getLong("ocorrenciaPendencia.evento.idEvento"), false);

        for (Manifesto manifesto : manifestos) {
            updateManifestoEEventoManifesto(mapBean, manifesto);

            // Busca uma lista de documentos de serviço para o manifesto informado.
            // O summary continua buscando o doctoservico para que o mesmo seja reaproveitado dentro da
            // classe IncluirEventosRastreabilidadeInternacionalService
            final ProjectionList projection = Projections.projectionList()
                    .add(Projections.property("ds.id"), "idDoctoServico")
                    .add(Projections.property("ds.tpDocumentoServico"), "tpDocumentoServico")
                    .add(Projections.property("ds.filialByIdFilialDestino"), "filialByIdFilialDestino")
                    .add(Projections.property("ds.filialByIdFilialOrigem"), "filialByIdFilialOrigem")
                    .add(Projections.property("ds.nrDoctoServico"), "nrDoctoServico");

            final List<DoctoServico> listDoctoServico = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection);

            List<Long> idsDoctoServico = new ArrayList<Long>();
            List<Long> idsDoctoServicoToRemove = new ArrayList<Long>();

            for (DoctoServico doctoServico : listDoctoServico) {
                idsDoctoServico.add(doctoServico.getIdDoctoServico());
                idsDoctoServicoToRemove.add(doctoServico.getIdDoctoServico());
            }

            if (CollectionUtils.isEmpty(listDoctoServico)) {
                throw new BusinessException(ConstantesPendencia.LMS_ERR_NENHUMA_OCORRENCIA_GERADA);
            }

            Map<Long, Long> mapIdOcorrenciaDoctoServico =
                    this.getExistingOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(mapBean, idsDoctoServico, idsDoctoServicoToRemove, listDoctoServico);

            for (DoctoServico doctoServico : listDoctoServico) {
                if (ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO.equals(doctoServico.getTpDocumentoServico().getValue())) {
                    continue;
                }

                if (doctoServico.getFilialByIdFilialDestino() == null) {
                    throw new BusinessException(ConstantesPendencia.LMS_ERR_FILIAL_OCORRENCIA_DESTINO_NULA);
                }

                // Quando a ocorrência informada for de BLOQUEIO.
                if (ConstantesPendencia.STATUS_PENDENCIA_BLOQUEIO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
                    executeBloqueio(mapBean, mapIdOcorrenciaDoctoServico, comunicadoApreensao, doctoServico, adsmNativeBatchSqlOperations);
                }

                generateEventoDocumento(mapBean, doctoServico, evento, adsmNativeBatchSqlOperations, blIntegracaoFedex);

                // Quando a ocorrência informada for de LIBERAÇÃO.
                if (ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
                    executeLiberacao(doctoServico, mapIdOcorrenciaDoctoServico, adsmNativeBatchSqlOperations);
                }
            }

        }

        adsmNativeBatchSqlOperations.runAllCommands();

        AdsmHibernateUtils.setFlushModeToAuto(this.getOcorrenciaDoctoServicoDAO().getAdsmHibernateTemplate());

        return JTDateTimeUtils.getDataHoraAtual();
    }

    private void carregarOperacoesLiberacao(TypedFlatMap mapBean, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, ComunicadoApreensao comunicadoApreensao) {
        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(OCORRENCIA_DOCTO_SERVICO, "ID_FILIAL_LIBERACAO",
                SessionUtils.getFilialSessao().getIdFilial());

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(OCORRENCIA_DOCTO_SERVICO, "ID_OCOR_LIBERACAO",
                this.getOcorrenciaPendenciaService().findById(mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_ID_OCORRENCIA_PENDENCIA))
                        .getIdOcorrenciaPendencia());

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(OCORRENCIA_DOCTO_SERVICO, "DH_LIBERACAO",
                JTDateTimeUtils.getDataHoraAtual());

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(OCORRENCIA_DOCTO_SERVICO, "ID_COMUNICADO_APREENSAO",
                (comunicadoApreensao != null) ? comunicadoApreensao.getIdComunicadoApreensao() : null);

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(OCORRENCIA_DOCTO_SERVICO, "ID_USUARIO_LIBERACAO",
                SessionUtils.getUsuarioLogado().getIdUsuario());
    }

    private void generateEventoDocumento(TypedFlatMap mapBean, DoctoServico doctoServico, Evento evento, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, Boolean blIntegracaoFedex) {

        Filial filialSessao = SessionUtils.getFilialSessao();
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        // Grava evento na tabela EVENTO_DOCUMENTO_SERVICO para o documento em questão.

        String strDocumento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), ConstantesPendencia.PAD_SIZE_EVENTO_DOC_SERV, '0');

        this.getIncluirEventosRastreabilidadeInternacionalService().generateEventoDocumentoComBatch(
                evento, doctoServico, filialSessao.getIdFilial(),
                strDocumento, dataHora, null, filialSessao.getSiglaNomeFilial(),
                doctoServico.getTpDocumentoServico().getValue(), null,
                mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_ID_OCORRENCIA_PENDENCIA), adsmNativeBatchSqlOperations, blIntegracaoFedex);
    }

    private List<Manifesto> findManifestos(TypedFlatMap mapBean) {
        List<String> idsManifesto = mapBean.getList("ids");
        List<Long> idsManifestos = new ArrayList<Long>();

        for (String id : idsManifesto) {
            idsManifestos.add(Long.valueOf(id));
        }

        return getManifestoService().findByIds(idsManifestos);
    }

    private void executeLiberacao(DoctoServico doctoServico,
                                  Map<Long, Long> mapIdOcorrenciaDoctoServico,
                                  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        //Buscar ocorrencia docto servico com idDoctoServico igual ao recebido
        //e dhLiberacao vazio.

        if (mapIdOcorrenciaDoctoServico == null) {
            //Não existe uma ocorrência de bloqueio a ser liberada para o Documento de Serviço informado.
            throw new BusinessException(ConstantesPendencia.LMS_ERR_NAO_EXISTE_BLOQUEIO_LIBERACAO_DOCTO_SERV, new String[]{this.formataDoctoServico(doctoServico)});
        }

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(OCORRENCIA_DOCTO_SERVICO,
                mapIdOcorrenciaDoctoServico.get(doctoServico.getIdDoctoServico()));

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(DOCTO_SERVICO,
                doctoServico.getIdDoctoServico());
    }

    private void executeBloqueio(TypedFlatMap mapBean, Map<Long, Long> mapIdOcorrenciaDoctoServico,
                                 ComunicadoApreensao comunicadoApreensao, DoctoServico doctoServico,
                                 AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        Filial filialSessao = SessionUtils.getFilialSessao();
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        if (mapIdOcorrenciaDoctoServico != null && !mapIdOcorrenciaDoctoServico.isEmpty()) {
            //Já existe uma ocorrencia de bloqueio para o documento de servico informado.
            throw new BusinessException(ConstantesPendencia.LMS_ERR_JA_EXISTE_OCORRENCIA_BLOQUEIO, new String[]{this.formataDoctoServico(doctoServico)});
        }

        Map<String, Object> ocorrenciaDoctoServicoValuesMap = new HashMap<String, Object>();
        ocorrenciaDoctoServicoValuesMap.put("ID_DOCTO_SERVICO", doctoServico.getIdDoctoServico());
        ocorrenciaDoctoServicoValuesMap.put("ID_FILIAL_BLOQUEIO", filialSessao.getIdFilial());
        ocorrenciaDoctoServicoValuesMap.put("ID_OCOR_BLOQUEIO", mapBean.getLong(ConstantesPendencia.OCORRENCIA_PENDENCIA_ID_OCORRENCIA_PENDENCIA));
        ocorrenciaDoctoServicoValuesMap.put("DH_BLOQUEIO", dataHora);
        ocorrenciaDoctoServicoValuesMap.put("ID_COMUNICADO_APREENSAO", (comunicadoApreensao != null) ? comunicadoApreensao.getIdComunicadoApreensao() : null);
        ocorrenciaDoctoServicoValuesMap.put("ID_USUARIO_BLOQUEIO", usuarioLogado.getIdUsuario());
        ocorrenciaDoctoServicoValuesMap.put("ID_FASE_PROCESSO", this.getFaseProcessoService().findByIdDoctoServico(
                doctoServico.getIdDoctoServico()).getIdFaseProcesso());

        adsmNativeBatchSqlOperations.addNativeBatchInsert(OCORRENCIA_DOCTO_SERVICO,
                ocorrenciaDoctoServicoValuesMap);

        adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(DOCTO_SERVICO,
                doctoServico.getIdDoctoServico());
    }

    private ComunicadoApreensao storeComunicadoApreensao(TypedFlatMap mapBean) {
        ComunicadoApreensao comunicadoApreensao = null;

        if (mapBean.getBoolean(ConstantesPendencia.OCORRENCIA_PENDENCIA_BL_APREENSAO)) {
            String numeroTermoApreensao = mapBean.getString(ConstantesPendencia.NUMERO_TERMO_APREENSAO);
            YearMonthDay dtTermoApreensao = mapBean.getYearMonthDay(ConstantesPendencia.DT_TERMO_APREENSAO);
            Long idMoeda = mapBean.getLong(ConstantesPendencia.MOEDA_ID_MOEDA);
            BigDecimal valorMulta = mapBean.getBigDecimal(ConstantesPendencia.VALOR_MULTA);
            String motivoAlegado = mapBean.getString(ConstantesPendencia.MOTIVO_ALEGADO);
            //Obrigatoriedade de todos os campos.

            if ((numeroTermoApreensao != null &&
                    !"".equals(numeroTermoApreensao)) &&
                    dtTermoApreensao != null &&
                    idMoeda != null
                    && valorMulta != null &&
                    (motivoAlegado != null && !"".equals(motivoAlegado))) {
                comunicadoApreensao = new ComunicadoApreensao();
                comunicadoApreensao.setNrTermoApreensao(mapBean.getString(ConstantesPendencia.NUMERO_TERMO_APREENSAO));
                comunicadoApreensao.setDtOcorrencia(mapBean.getYearMonthDay(ConstantesPendencia.DT_TERMO_APREENSAO));
                comunicadoApreensao.setMoeda(this.getMoedaService().findById(mapBean.getLong(ConstantesPendencia.MOEDA_ID_MOEDA)));
                comunicadoApreensao.setVlMulta(mapBean.getBigDecimal(ConstantesPendencia.VALOR_MULTA));
                comunicadoApreensao.setDsMotivoAlegado(mapBean.getString(ConstantesPendencia.MOTIVO_ALEGADO));
                this.getComunicadoApreensaoService().store(comunicadoApreensao);
            } else if ((numeroTermoApreensao != null && !"".equals(numeroTermoApreensao)) || dtTermoApreensao != null
                    || valorMulta != null || (motivoAlegado != null && !"".equals(motivoAlegado))) {
                //Para que um comunicado de apreensão seja salvo (opcional), todos os seus respectivos campos
                //precisam estar preenchidos, caso contrário, deixar os campos vazios.
                throw new BusinessException(ConstantesPendencia.LMS_ERR_DADOS_COMUNICADO_APREENSAO_INVALIDO);
            }
        }
        return comunicadoApreensao;
    }

    private void updateManifestoEEventoManifesto(TypedFlatMap mapBean, Manifesto manifesto) {

        Filial filialSessao = SessionUtils.getFilialSessao();
        Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        EventoManifesto eventoManifesto = new EventoManifesto();
        eventoManifesto.setManifesto(manifesto);
        eventoManifesto.setFilial(filialSessao);
        eventoManifesto.setUsuario(usuarioLogado);
        eventoManifesto.setDhEvento(dataHora);

        // Atualiza o Manifesto e o Evento de manifesto de acordo com a ocorrência.
        if (ConstantesPendencia.STATUS_PENDENCIA_BLOQUEIO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            manifesto.setBlBloqueado(Boolean.TRUE);
            eventoManifesto.setTpEventoManifesto(new DomainValue(ConstantesPendencia.DOMAIN_VALUE_BLOQUEIO));
        } else if (ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            manifesto.setBlBloqueado(Boolean.FALSE);
            eventoManifesto.setTpEventoManifesto(new DomainValue(ConstantesPendencia.DOMAIN_VALUE_LIBERACAO));
        }

        this.getManifestoService().storeBasic(manifesto);
        this.getEventoManifestoService().store(eventoManifesto);
    }

    /**
     * Realiza o envio do email e geracao do anexo.
     *
     * @param reportCriteria id
     */

    private void sendEmailComunicadoApreensaoCliente(TypedFlatMap reportCriteria) {
        reportCriteria.put("_reportCall", Boolean.TRUE);
        File anexo = null;

        try {
            anexo = reportExecutionManager.executeReport(ConstantesPendencia.LMS_PENDENCIA_EMITIR_COMUNICADO_APREENSAO_CLIENTE_SERVICE, reportCriteria);

            String tipoDocumento = reportCriteria.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_DOCTO_SERVICO_TP_DOCUMENTO_SERVICO);
            String sgFilialOrigem = reportCriteria.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_DOCTO_SERVICO_FILIAL_BY_ID_FILIAL_ORIGEM_SG_FILIAL);
            Long nrDoctoServico = reportCriteria.getLong(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_DOCTO_SERVICO_NR_DOCTO_SERVICO);
            String notasFiscais = buildNotasFiscais(reportCriteria, tipoDocumento, nrDoctoServico);

            String sgFilial = reportCriteria.getString(ConstantesPendencia.FILIAL_SG_FILIAL);
            String nmFantasia = reportCriteria.getString(ConstantesPendencia.FILIAL_PESSOA_NM_FANTASIA);

            final String subject = EmailCartaUtils.buildEmailSubject(configuracoesFacade.getMensagem(ConstantesPendencia.COMUNICACAO_APREENSAO_FISCAL),
                    notasFiscais,
                    tipoDocumento,
                    sgFilialOrigem,
                    nrDoctoServico.toString());

            final String message = buildDefaultMessage(notasFiscais, tipoDocumento, sgFilialOrigem, nrDoctoServico.toString(), sgFilial, nmFantasia);
            final File anexoFinal = anexo;

			final String[] dsEmailsDestinatario = EmailCartaUtils.getEmails(reportCriteria, ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_CLIENTE_BY_ID_CLI_DEST_PES_DS_EMAIL);
			this.sendEmail(
				dsEmailsDestinatario, 
				subject, 
				reportCriteria.getString(ConstantesPendencia.USUARIO_DS_EMAIL), 
				message, 
				anexoFinal
			);

			final String[] dsEmailsRemetente = EmailCartaUtils.getEmails(reportCriteria, ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_CLIENTE_BY_ID_CLI_REM_PES_DS_EMAIL);
			this.sendEmail(
				dsEmailsRemetente, 
				subject, 
				reportCriteria.getString(ConstantesPendencia.USUARIO_DS_EMAIL), 
				message, 
				anexoFinal
			);

            ComunicadoApreensao ca = new ComunicadoApreensao();
            Long idComunicadoApreensao = reportCriteria.getLong(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_ID_COMUNICADO_APREENSAO);
            if (idComunicadoApreensao != null) {
                ca = comunicadoApreensaoService.findById(idComunicadoApreensao);
            }
            ca.setDsMotivoAlegado(reportCriteria.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_DS_MOTIVO_ALEGADO));
            ca.setDtOcorrencia(reportCriteria.getYearMonthDay(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_DT_OCORRENCIA));
            ca.setMoeda(moedaService.findById(reportCriteria.getLong(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_MOEDA_ID_MOEDA)));
            ca.setNrTermoApreensao(reportCriteria.getString(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_NR_TERMO_APREENSAO));
            ca.setVlMulta(reportCriteria.getBigDecimal(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_COMUNICADO_APREENSAO_VL_MULTA));
            comunicadoApreensaoService.store(ca);
        } catch (Exception e) {
            //Ocorreu erro ao tentar enviar o e-mail xxxxx
            throw new BusinessException(ConstantesPendencia.LMS_ERR_ERRO_TENTAR_GERAR_EMAIL, e);
        }
    }

	private void sendEmail(String[] strEmails, String strSubject, String strFrom, String strText, File anexo){
		Mail mail = createMail(StringUtils.join(strEmails, ";"), strFrom, strSubject, strText, anexo);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Mail createMail(String strTo, String strFrom, String strSubject, String body, File anexo) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		
		MailAttachment mailAttachment = new MailAttachment();
		mailAttachment.setName(anexo.getName());
		mailAttachment.setData(FileUtils.readFile(anexo));
		MailAttachment[] mailAttachmentArr = {mailAttachment};
		mail.setAttachements(mailAttachmentArr);
		
		return mail;
	}

    private String buildDefaultMessage(String notasFiscais, String tipoDoctoServico, String sgFilialOrigem, String nrDoctoServico, String sgFilial, String nmFantasia) {
        Contato contato = getContatoOCById();
        String[] args = {"<br>",
                notasFiscais,
                tipoDoctoServico,
                sgFilialOrigem,
                nrDoctoServico,
                sgFilial,
                nmFantasia,
                EmailCartaUtils.buildObContato(contato),
                EmailCartaUtils.buildDepartamento(contato)};

        return configuracoesFacade.getMensagem(ConstantesPendencia.TEXTO_COMUNICACAO_MERCADORIA_DISPOSICAO, args);
    }

    private String buildNotasFiscais(TypedFlatMap criteria, String tipoDocumento, Long nrDoctoServico) {
        Long idFilialOrigem = criteria.getLong(ConstantesPendencia.OCORRENCIA_DOCTO_SERVICO_FILIAL_BY_ID_FIL_ORI_ID_FIL);

        DoctoServico doctoServico = doctoServicoService.findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServico(tipoDocumento,
                idFilialOrigem, nrDoctoServico);
        String notasFiscais = "";

        List<NotaFiscalConhecimento> nf = notaFiscalConhecimentoService.findByConhecimento(doctoServico.getIdDoctoServico());
        if (nf != null && !nf.isEmpty()) {
            notasFiscais = "NF " + nf.get(0).getNrNotaFiscal();
        }
        return notasFiscais;
    }

    private Contato getContatoOCById() {
        Filial filialSessao = SessionUtils.getFilialSessao();
        return configuracoesFacade.findContatoByIdPessoaAndTipoContato(filialSessao.getIdFilial(), ConstantesPendencia.OCORRENCIA_COLETA);
    }

    public ResultSetPage findPaginatedBloqueiosLiberacoesByIdDoctoServ(Long idDoctoServico) {
        return lmBloqueioLiberacaoDao.findPaginatedBloqueiosLiberacoes(idDoctoServico);
    }

    /**
     * verifica se existe alguma ocorrencia para o documento de servico
     *
     * @param idDoctoServico
     * @return
     */

    public boolean findOcorDSByIdDoctoServico(Long idDoctoServico) {
        return getOcorrenciaDoctoServicoDAO().findOcorDsByIdDoctoServico(idDoctoServico);
    }

    /**
     * verifica se existe alguma ocorrencia com dhLiberacao vazio (ocorrencia em aberto) para o documento de servico
     *
     * @param idDoctoServico
     * @return
     */
    public OcorrenciaDoctoServico findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(Long idDoctoServico) {
        List ocorrencias = getOcorrenciaDoctoServicoDAO().findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(idDoctoServico);
        if (ocorrencias != null) {
            if (ocorrencias.size() == 1) {
                return (OcorrenciaDoctoServico) ocorrencias.get(0);
            }
            if (ocorrencias.size() > 1) {
                DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
                throw new BusinessException(ConstantesPendencia.LMS_ERR_EXISTE_MAIS_DE_UMA_OCORR_BLOQ_ABERTA, new String[]{this.formataDoctoServico(doctoServico)});
            }
        }
        return null;
    }


    public List<Long> findSimplifiedOcorrenciaDoctoServicoEmAbertoByIdsDoctoServico(List<Long> idsDoctoServico) {
        List<Long> resultIds = new ArrayList<Long>();
        List<Object[]> ocorrencias = getOcorrenciaDoctoServicoDAO().findSimplifiedOcorrenciaDoctoServicoEmAbertoByIdsDoctoServico(idsDoctoServico);
        if (ocorrencias != null && !ocorrencias.isEmpty()) {
            for (Object[] objects : ocorrencias) {
                resultIds.add((Long) objects[1]);
            }
        }
        return resultIds;
    }

    public Map<Long, Long> getExistingOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(TypedFlatMap mapBean, List<Long> idsDoctoServico, List<Long> idsDoctoServicoSemOcorrencia,
                                                                                     List<DoctoServico> doctoServicos) {
        List<Object[]> ocorrencias = getOcorrenciaDoctoServicoDAO().findSimplifiedOcorrenciaDoctoServicoEmAbertoByIdsDoctoServico(idsDoctoServico);

        if (ocorrencias != null && !ocorrencias.isEmpty()) {
            Map<Long, Long> result = new HashMap<Long, Long>();

            for (Object[] objects : ocorrencias) {
                Long idDoctoServico = (Long) objects[1];
                Long idOcorrenciaDoctoServico = (Long) objects[0];
                result.put(idDoctoServico, idOcorrenciaDoctoServico);
                idsDoctoServicoSemOcorrencia.remove(idDoctoServico);
            }

            if (ocorrencias.size() != idsDoctoServico.size()) {
                removerDoctosServicosDaListaDebloqueio(mapBean, idsDoctoServico, idsDoctoServicoSemOcorrencia, doctoServicos);
                if (!ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
                    result.clear();
                }
            }

            return result;
        }
        return null;
    }

    private void removerDoctosServicosDaListaDebloqueio(TypedFlatMap mapBean, List<Long> idsDoctoServico, List<Long> idsDoctoServicoSemOcorrencia, List<DoctoServico> doctoServicos) {
        List<DoctoServico> flagObjectToRemove = new ArrayList<DoctoServico>();

        if (ConstantesPendencia.STATUS_PENDENCIA_LIBERACAO.equals(mapBean.getString(ConstantesPendencia.OCORRENCIA_PENDENCIA_TP_OCORRENCIA))) {
            for (DoctoServico doctoServico : doctoServicos) {
                if (idsDoctoServicoSemOcorrencia.contains(doctoServico.getIdDoctoServico())) {
                    flagObjectToRemove.add(doctoServico);
                }
            }
            idsDoctoServico.removeAll(idsDoctoServicoSemOcorrencia);
        } else {
            for (DoctoServico doctoServico : doctoServicos) {
                if (!idsDoctoServicoSemOcorrencia.contains(doctoServico.getIdDoctoServico())) {
                    flagObjectToRemove.add(doctoServico);
                }
            }
            idsDoctoServico.clear();
            idsDoctoServico.addAll(idsDoctoServicoSemOcorrencia);
        }
        doctoServicos.removeAll(flagObjectToRemove);
    }

    /**
     * Solicitação  INT-166 da Integração
     * verifica se existe alguma ocorrencia com dhLiberacao vazio (ocorrencia em aberto) para o documento de servico
     *
     * @param idDoctoServico
     * @return
     */
    public int findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(Long idDoctoServico) {
        List ocorrencias = getOcorrenciaDoctoServicoDAO().findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(idDoctoServico);
        return ocorrencias != null ? ocorrencias.size() : 0;
    }

    /**
     * Solicitação CQPRO00005860 da Integração.
     * Retorna uma instância de OcorrenciaDoctoServico com base em uma ocorrencia de bloqueio e sua data/hora;
     *
     * @param idOcorrenciaPendenciaByIdOcorBloqueio
     * @param dhBloqueio
     * @return
     */
    public OcorrenciaDoctoServico findByOcorrenciaPendenciaByIdOcorBloqueio(Long idOcorrenciaPendenciaByIdOcorBloqueio, DateTime dhBloqueio) {
        return getOcorrenciaDoctoServicoDAO().findByOcorrenciaPendenciaByIdOcorBloqueio(idOcorrenciaPendenciaByIdOcorBloqueio, dhBloqueio);
    }

    /**
     * Busca a última OcorrenciaDoctoServico para um doctoServico informado.
     *
     * @param idDoctoServico
     * @return
     */
    public OcorrenciaDoctoServico findLastOcorrenciaDoctoServicoByIdDoctoServico(Long idDoctoServico) {
        return getOcorrenciaDoctoServicoDAO().findLastOcorrenciaDoctoServicoByIdDoctoServico(idDoctoServico);
    }

    private String formataLocalizacaoDoctoServico(DoctoServico doctoServico) {
        String dsLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue();
        String obComplementoLocalizacao = doctoServico.getObComplementoLocalizacao();

        if (obComplementoLocalizacao == null) {
            obComplementoLocalizacao = SessionUtils.getFilialSessao().getSiglaNomeFilial();
        }

        return dsLocalizacaoMercadoria + " " + obComplementoLocalizacao;
    }

    private String formataDoctoServico(DoctoServico doctoServico) {
        String tpDoctoServico = doctoServico.getTpDocumentoServico().getDescription().toString();
        String strDoctoServico = doctoServico.getFilialByIdFilialOrigem().getSgFilial();
        strDoctoServico = tpDoctoServico + " - " + strDoctoServico + " - " + FormatUtils.formataNrDocumento(doctoServico.getNrDoctoServico().toString(), doctoServico.getTpDocumentoServico().getValue());
        return strDoctoServico;
    }

    public Boolean findHasOcorrenciaSemLiberacaoBloqueioDoctoServicoByCdOcorrencia(Long idDoctoServico, Short[] cdOcorrencias) {
        List<OcorrenciaDoctoServico> list = getOcorrenciaDoctoServicoDAO().findOcorrenciaSemLiberacaoBloqueioDoctoServicoByCdOcorrencia(idDoctoServico, cdOcorrencias);
        return CollectionUtils.isNotEmpty(list);
    }
    
    public void validateAgendamentoEntrega(Long idFilialUsuarioLogado, DoctoServico doctoServico) {
    	if(doctoServico.getTpDocumentoServico() != null && 
    			(doctoServico.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.CONHECIMENTO_ELETRONICO) 
    			|| doctoServico.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA)
    			|| doctoServico.getTpDocumentoServico().getValue().equals(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE))){
	    	Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(doctoServico.getIdDoctoServico(), false);
	    	
			if(idFilialUsuarioLogado.equals(doctoServico.getFilialByIdFilialDestino().getIdFilial())
					&& BooleanUtils.isTrue(conhecimento.getBlObrigaAgendamento())
					&& BooleanUtils.isTrue(doctoServico.getClienteByIdClienteDestinatario().getBlAgendamento())){
				
				OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(CD_AGUARDANDO_AGENDAMENTO_ENTREGA);
				
				this.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(doctoServico.getIdDoctoServico(),
						ocorrenciaPendencia.getIdOcorrenciaPendencia(), null, null);
				
			}
    	}
	}	

    /**
     * Verifica se uma determinada ocorrência de bloqueio pode ser liberada por outra ocorrência de liberação.
     * LMS-4567
     *
     * @param idOcorrenciaBloqueio
     * @param idOcorrenciaLiberacao
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Boolean validateVerificaDePara(Long idOcorrenciaBloqueio, Long idOcorrenciaLiberacao) {
        Map criteria = new HashMap();
        criteria.put(ConstantesPendencia.BY_ID_OCORRENCIA_BLOQUEIO_ID_OCORRENCIA_PENDENCIA, idOcorrenciaBloqueio);
        criteria.put(ConstantesPendencia.BY_ID_OCORRENCIA_LIBERACAO_ID_OCORRENCIA_PENDENCIA, idOcorrenciaLiberacao);
        List liberacoesBloqueio = liberacaoBloqueioService.find(criteria);

        if (liberacoesBloqueio.isEmpty()) {
            OcorrenciaPendencia ocorrenciaPendenciaLiberacao = ocorrenciaPendenciaService.findById(idOcorrenciaLiberacao);
            throw new BusinessException(ConstantesPendencia.LMS_ERR_NAO_POSSIVEL_LIBERAR_BLOQ_DOCTO_SERV, new String[]{ocorrenciaPendenciaLiberacao.getCdOcorrencia().toString()});
        }

        return Boolean.TRUE;
    }

    /**
     * Método que retorna uma lista de map com dados das ocorrencias relacionados ao documento de serviço com id igual ao
     * passado como parametro
     *
     * @param idDoctoServico
     * @return
     */
    public List<Map<String, Object>> findListMapOcorrenciaByIdDoctoServico(Long idDoctoServico) {
        return getOcorrenciaDoctoServicoDAO().findListMapOcorrenciaByIdDoctoServico(idDoctoServico);
    }

    public void setDiaUtils(DiaUtils diaUtils) {
        this.diaUtils = diaUtils;
    }

    public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
        this.enderecoPessoaService = enderecoPessoaService;
    }

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

    public List<OcorrenciaDoctoServico> findOcorrenciaDoctoServicoByCdOcorrencia(
            long idDoctoServico, Short[] cdOcorrencias, boolean isInOperator) {
        return getOcorrenciaDoctoServicoDAO().findOcorrenciaDoctoServicoByCdOcorrencia(idDoctoServico, cdOcorrencias, isInOperator);
    }

    public List<OcorrenciaDoctoServico> findOcorrenciaByControleCargaManifestoEntrega(Long idControleCarga) {
        return getOcorrenciaDoctoServicoDAO().findOcorrenciaByControleCargaManifestoEntrega(idControleCarga);
    }

    public List<OcorrenciaDoctoServico> findByDetachedCriteria(DetachedCriteria detachedCriteria) {
        return getOcorrenciaDoctoServicoDAO().findByDetachedCriteria(detachedCriteria);
    }

    public List<OcorrenciaDoctoServico> findOcorrenciaBloqueioDoctoServicoByCdOcorrencia(
            long idDoctoServico, Short[] cdOcorrencias) {
        return getOcorrenciaDoctoServicoDAO().findOcorrenciaBloqueioDoctoServicoByCdOcorrencia(idDoctoServico, cdOcorrencias);
    }

    public List<OcorrenciaDoctoServico> findBloqueioDoctoServicoPorPeriodoLiberacao(Long idDoctoServico, YearMonthDay dtInicial, YearMonthDay dtFinal) {
        return getOcorrenciaDoctoServicoDAO().findBloqueioDoctoServicoPorPeriodoLiberacao(idDoctoServico, dtInicial, dtFinal);
    }

    public boolean ocorrenciaHasBloqueio(Long idDoctoServico, DateTime data) {
        return getOcorrenciaDoctoServicoDAO().ocorrenciaHasBloqueio(idDoctoServico, data);
    }

    public Boolean executeVerificarLiberacaoByCodigo(Short cdOcorrencia) {
        return getOcorrenciaDoctoServicoDAO().executeVerificarLiberacaoByCodigo(cdOcorrencia);
    }

    public OcorrenciaPendencia findOcorrenciaLiberacaoByOcorrenciaBloqueada(Short cdOcorrencia) {
        return getOcorrenciaDoctoServicoDAO().findOcorrenciaLiberacaoByOcorrenciaBloqueada(cdOcorrencia);
    }

    public DateTime findMaxDataLiberacaoOcorrencia(Long idDoctoServico) {
        return getOcorrenciaDoctoServicoDAO().findMaxDataLiberacaoOcorrencia(idDoctoServico);
    }

    public LiberacaoBloqueioService getLiberacaoBloqueioService() {
        return liberacaoBloqueioService;
    }

    public void setLiberacaoBloqueioService(LiberacaoBloqueioService liberacaoBloqueioService) {
        this.liberacaoBloqueioService = liberacaoBloqueioService;
    }

    public FaseProcessoService getFaseProcessoService() {
        return faseProcessoService;
    }

    public void setFaseProcessoService(FaseProcessoService faseProcessoService) {
        this.faseProcessoService = faseProcessoService;
    }

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
}

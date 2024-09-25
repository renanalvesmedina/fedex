package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalConhecimentoDAO;
import com.mercurio.lms.expedicao.util.EmailCartaUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import java.io.File;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.notaFiscalConhecimentoService"
 */
public class NotaFiscalConhecimentoService extends CrudService<NotaFiscalConhecimento, Long> {

    private static final String CD_INTEGRA_OCO_FDX = "CD_INTEGRA_OCO_FDX";
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	
    private LMComplementoDAO lmComplementoDao;
    private ReportExecutionManager reportExecutionManager;
    private ParametroGeralService parametroGeralService;
    private ConfiguracoesFacade configuracoesFacade;
    private DoctoServicoService doctoServicoService;
    private VersaoDescritivoPceService versaoDescritivoPceService;
    private FilialService filialService;
    private IntegracaoJmsService integracaoJmsService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private MotivoAberturaNcService motivoAberturaNcService;
    private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
    
    public Long findNFConhecimentoByNrNotaFiscal(Integer nrNotaFiscal, int destRmt, String nrIdentificacao) {
        return getNotaFiscalConhecimentoDAO().findNFConhecimentoByNrNotaFiscal(nrNotaFiscal, destRmt, nrIdentificacao);
    }

    /**
     * Recupera uma instância de <code>NotaFiscalConhecimento</code> a partir do
     * ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public NotaFiscalConhecimento findById(Long id) {
        return (NotaFiscalConhecimento) super.findById(id);
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    public void removeById(Long id) {
        super.removeById(id);
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @ParametrizedAttribute(type = Long.class)
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
    public java.io.Serializable store(NotaFiscalConhecimento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste
     * serviço.
     * @param dao .
     */
    public void setNotaFiscalConhecimentoDAO(NotaFiscalConhecimentoDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência
     * dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private NotaFiscalConhecimentoDAO getNotaFiscalConhecimentoDAO() {
        return (NotaFiscalConhecimentoDAO) getDao();
    }

    /**
     * Método utilizado pela Integração
     *
     * @param idConhecimento
     * @param nrNotaFiscal
     * @return <NotaFiscalConhecimento>
     * @author Andre Valadas
     */
    public NotaFiscalConhecimento findNotaFiscalConhecimento(Long idConhecimento, Integer nrNotaFiscal) {
        return getNotaFiscalConhecimentoDAO().findNotaFiscalConhecimento(idConhecimento, nrNotaFiscal);
    }

    public List<Map> findIdNrNotaByIdConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findIdNrNotaByIdConhecimento(idConhecimento);
    }

    public List<Map<String, Object>> findIdNrNotaEtiquetByIdConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findIdNrNotaEtiquetByIdConhecimento(idConhecimento);
    }

    public NotaFiscalConhecimento findByIdConhecimento(Long idNotaFiscalConhecimento) {
        return getNotaFiscalConhecimentoDAO().findByIdConhecimento(idNotaFiscalConhecimento);
    }

    /**
     * Localiza uma lista de resultados a partir dos critérios de busca
     * informados. Permite especificar regras de ordenação.
     *
     * @param criteria Critérios de busca.
     * @param campoOrdenacao      com criterios de ordenação. Deve ser uma java.lang.String no
     *                   formato <code>nomePropriedade:asc</code> ou
     *                   <code>associacao_.nomePropriedade:desc</code>. A utilização de
     *                   <code>asc</code> ou <code>desc</code> é opcional sendo o
     *                   padrão <code>asc</code>.
     * @return Lista de resultados sem paginação.
     */
    public List findListByCriteria(Map criteria, List campoOrdenacao) {
        return getNotaFiscalConhecimentoDAO().findListByCriteria(criteria, campoOrdenacao);
    }

    public List findListByCriteriaByMda(Long idMda) {
        return getNotaFiscalConhecimentoDAO().findListByCriteriaByMda(idMda);
    }

    public List findIdsByIdConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findIdsByIdConhecimento(idConhecimento);
    }

    public List findByConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findByConhecimento(idConhecimento);
    }

    @SuppressWarnings("rawtypes")
    public List findByNrChave(String chave) {
        return getNotaFiscalConhecimentoDAO().findByNrChave(chave);
    }

    // LMSA-7137
    public List findByNrChaveEClienteRemetente(String chave, Long idCliente) {
        return getNotaFiscalConhecimentoDAO().findByNrChaveEClienteRemetente(chave, idCliente);
    }

    public List findByNrChaveIdConhecimentoOriginal(String chave, Long idConhecimentoOriginal) {
        return getNotaFiscalConhecimentoDAO().findByNrChaveIdConhecimentoOriginal(chave, idConhecimentoOriginal);
    }

    public List<Map<String, Object>> findNFByIdDoctoServico(Long idDoctoServico) {
        return getNotaFiscalConhecimentoDAO().findNFByIdDoctoServico(idDoctoServico);
    }

    public List findNFByIdConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findNFByIdConhecimento(idConhecimento);
    }

    public List findNFByIdConhecimentoAndIdsCliente(Long idConhecimento, List<Long> idsCliente){
        return getNotaFiscalConhecimentoDAO().findNFByIdConhecimentoAndIdsClienteDevedor(idConhecimento, idsCliente);
    }

    public List findPaginatedComplNF(Long idDoctoServico) {
        return lmComplementoDao.findPaginatedComplNF(idDoctoServico);
    }

    /**
     * Gera a carta Mercadoria a Disosicao e realiza o envio do e-mail.
     *
     * @param criteria TypedFlatMap
     * @param destinatario String
     * @param destinatarioCopia String
     */

    public void sendEmailCartaOcorrencia(TypedFlatMap criteria, String destinatario, final String destinatarioCopia) {
        File anexo = null;

        try {
            anexo = reportExecutionManager.executeReport("lms.pendencia.emitirCartaMercadoriasDisposicaoService", criteria);
        } catch (Exception e) {
            log.error(e);
        }

        final String subject = EmailCartaUtils.buildEmailSubject(this.getConfiguracoesFacade().getMensagem("comunicacaoMercadoriaDisposicao"),
                criteria.getString("notasFiscais"), criteria.get("tpDoctoServico").toString(), criteria.get("sgfilialOrigem").toString(),
                criteria.get("nrDoctoServico").toString());

        Long idFilial = criteria.getLong("formBean.filial.idFilial");

        Contato contato = configuracoesFacade.findContatoByIdPessoaAndTipoContato(idFilial, "OC");

        String obContato = EmailCartaUtils.buildObContato(contato);
        String departamento = EmailCartaUtils.buildDepartamento(contato);

        final String remetenteLms = EmailCartaUtils.buildRemetente(null, parametroGeralService);

        String mensagem = buildMessage(criteria, obContato, departamento);

        if (!"".equals(destinatario)) {
            final String[] dsEmails = destinatario.split(";");
            final String mensagemFinal = mensagem;
            final File anexoFinal = anexo;
            
            for (String email : dsEmails) {
                if (!ValidateUtils.validateEmail(email)) {
                	throw new BusinessException("ASN-00003");
                }
            }

    		Mail mail = createMail(
    			StringUtils.join(dsEmails, ";"),
    			destinatarioCopia,
    			remetenteLms,
    			subject,
    			mensagemFinal,
    			anexoFinal,
    			"cartaMercadoriaDisposicao.pdf"
    		);

			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
		}
	}

	private Mail createMail(String strTo, String cc, String strFrom, String strSubject, 
	String body, File file, String filename) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);

		if (cc != null && cc.length() > 0) {
			mail.setCc(cc);
		}

		MailAttachment mailAttachment = new MailAttachment();
		mailAttachment.setName(filename);
		mailAttachment.setData(FileUtils.readFile(file));
		MailAttachment[] mailAttachmentArr = {mailAttachment};
		mail.setAttachements(mailAttachmentArr);
		
		return mail;
	}

    private String buildMessage(TypedFlatMap criteria, String obContato, String departamento) {
        if (!"".equals(criteria.getString("idRecusa"))) {
            return buildMessageWithRecusa(criteria);
        }
        return buildDefaultMessage(criteria, obContato, departamento);
    }

    private String buildDefaultMessage(TypedFlatMap criteria, String obContato, String departamento) {
        String[] args = {"<br>", criteria.getString("notasFiscais"), criteria.get("tpDoctoServico").toString(),
                criteria.get("sgfilialOrigem").toString(), criteria.get("nrDoctoServico").toString(),
                criteria.getString("formBean.filial.sgFilial"), criteria.getString("formBean.filial.pessoa.nmFantasia"), obContato, departamento};

        return configuracoesFacade.getMensagem("textoComunicacaoMercadoriaDisposicao", args);
    }

    private String buildMessageWithRecusa(TypedFlatMap criteria) {
        String serverName = HttpServletRequestHolder.getHttpServletRequest().getServerName();
        String contexto = HttpServletRequestHolder.getHttpServletRequest().getContextPath();

        String[] args = {serverName + contexto, criteria.getString("idRecusa").toString(), "<br><br>", "<a href=", ">", "</a>"};
        return configuracoesFacade.getMensagem("textoMailRecusa", args);
    }

    public List findPaginatedByIdAwb(Long idAwb) {
        return getNotaFiscalConhecimentoDAO().findPaginatedByIdAwb(idAwb);
    }

    public Integer getRowCountByIdAwb(Long idAwb) {
        return getNotaFiscalConhecimentoDAO().getRowCountByIdAwb(idAwb);
    }

    public ResultSetPage findPaginatedNotaFiscalCliente(TypedFlatMap criteria) {
        FindDefinition def = FindDefinition.createFindDefinition(criteria);
        return getNotaFiscalConhecimentoDAO().findPaginatedNotaFiscalCliente(criteria, def);
    }

    public Long getRowCountNotaFiscalCliente(TypedFlatMap criteria) {
        return getNotaFiscalConhecimentoDAO().getRowCountNotaFiscalCliente(criteria);
    }

    public List findLookupNotaFiscalCliente(TypedFlatMap criteria) {
        return getNotaFiscalConhecimentoDAO().findLookupNotaFiscalCliente(criteria);
    }

    public List findListByRemetenteNrNotas(Long idRemetente, List nrNotasFiscais, List<YearMonthDay> datas, String dsSerie) {
        return getNotaFiscalConhecimentoDAO().findListByRemetenteNrNotasDsSerie(idRemetente, nrNotasFiscais, datas, dsSerie);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerie(Long idRemetente, List nrNotas, List dsSeries) {
        return getNotaFiscalConhecimentoDAO().findListByRemetenteNrNotasDsSerie(idRemetente, nrNotas, dsSeries);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerieTipoDoc(Long idRemetente, List nrNotas, List dsSeries, String tipoDoc) {
        return getNotaFiscalConhecimentoDAO().findListByRemetenteNrNotasDsSerieTipoDoc(idRemetente, nrNotas, dsSeries, tipoDoc);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSeriePaddingSeries(
            Long idRemetente, List nrNotas, List dsSeries, String tipoDoc) {

        return getNotaFiscalConhecimentoDAO().findListByRemetenteNrNotasDsSerie(idRemetente, nrNotas, dsSeries, null, tipoDoc);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerieConhecimentoOriginal(Long idRemetente, List nrNotas, List dsSeries,
                                                                                Long idConhecimentoOriginal) {
        return getNotaFiscalConhecimentoDAO().findListByRemetenteNrNotasDsSerieConhecimentoOriginal(idRemetente, nrNotas, dsSeries,
                idConhecimentoOriginal, null);
    }

    /**
     * @param idDoctoServico
     * @return retorna o mapa com a informação de bloqueado ou não
     */
    public TypedFlatMap validateDocBloqueado(Long idDoctoServico) {
        DoctoServico doctoServico = this.getDoctoServicoService().findById(idDoctoServico);
        TypedFlatMap result = new TypedFlatMap();

        result.put("isDocBloqueado", doctoServico.getBlBloqueado());
        return result;
    }

    /**
     * Faz a validacao do PCE para a tela de registrarDisposicao.
     *
     * @param idDoctoServico
     * @return
     */
    public TypedFlatMap validatePCE(Long idDoctoServico) {
        DoctoServico doctoServico = this.getDoctoServicoService().findById(idDoctoServico);
        TypedFlatMap result = new TypedFlatMap();

        if (doctoServico != null) {
            if (doctoServico.getClienteByIdClienteRemetente() != null) {
                result.put(
                        "remetente",
                        this.getVersaoDescritivoPceService().validatePCE(doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
                                Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_MERCADORIA_DISPOSICAO),
                                Long.valueOf(EventoPce.ID_EVENTO_PCE_INCLUSAO_MERC_DISPOSICAO),
                                Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_DISPOSICAO)));
            }

            if (doctoServico.getClienteByIdClienteDestinatario() != null) {
                result.put(
                        "destinatario",
                        this.getVersaoDescritivoPceService().validatePCE(doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
                                Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_MERCADORIA_DISPOSICAO),
                                Long.valueOf(EventoPce.ID_EVENTO_PCE_INCLUSAO_MERC_DISPOSICAO),
                                Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_DISPOSICAO)));
            }
            result.put("idDoctoServico", doctoServico.getIdDoctoServico());
        }
        return result;
    }

    public List findByMonitoramentoDescargaByBlPsAferido(Long idMonitoramentoDescarga, Boolean blPesoAferido) {
        return getNotaFiscalConhecimentoDAO().findByMonitoramentoDescargaByBlPsAferido(idMonitoramentoDescarga, blPesoAferido);
    }

    @SuppressWarnings("rawtypes")
    public List findNrNotaByIdConhecimento(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findNrNotaByIdConhecimento(idConhecimento);
    }


    public Map<String, Object> findDadosNotaFiscalConhecimento(String nrIdentificacao, Integer nrNotaFiscal, String dsSerie) {
        ParametroGeral cnpjDell = parametroGeralService.findByNomeParametro("CNPJ_RADAR_DELL");
        ParametroGeral cnpjRaizDell = parametroGeralService.findByNomeParametro("CNPJ_RAIZ_DELL");

        List<Map<String, Object>> l;

        if (cnpjDell.getDsConteudo().equals((nrIdentificacao))) {
            nrIdentificacao =  cnpjRaizDell.getDsConteudo();
            l = getNotaFiscalConhecimentoDAO().findDadosNotaFiscalConhecimentoDell(nrIdentificacao, nrNotaFiscal.toString());
        } else {
            l = getNotaFiscalConhecimentoDAO().findDadosNotaFiscalConhecimentoCliente(nrIdentificacao, nrNotaFiscal, dsSerie);
        }

        Map<String, Object> retorno = null;
        if (CollectionUtils.isNotEmpty(l)) {
            retorno = popularRetono(l.get(0));
        }
        return retorno;
    }


    private Map<String, Object> popularRetono(Map<String, Object> cabecalhoDetails){
        Long idDoctoServico = Long.parseLong(cabecalhoDetails.get("idDoctoServico").toString());
        Long idFilialOrigem = Long.parseLong(cabecalhoDetails.get("idFilialOrigem").toString());
        cabecalhoDetails.remove("idDoctoServico");
        cabecalhoDetails.remove("idFilialOrigem");
        cabecalhoDetails.putAll(populateDetailsCabecalhoRadar(idDoctoServico, idFilialOrigem));
        cabecalhoDetails.put("dtPrevEntrega", JTFormatUtils.format((YearMonthDay)cabecalhoDetails.get("dtPrevEntrega")));
        Map<String, Object> retorno = new HashMap<>();
        retorno.put("cabecalho", cabecalhoDetails);
        retorno.put("ocorrencias", populateDetailsOcorrenciasRadar(idDoctoServico));
        return retorno;
    }

    private List<Map<String, Object>> populateDetailsOcorrenciasRadar(Long idDoctoServico) {
        return getEventoDocumentoServicoService().findAllEventosByIdDoctoServico(idDoctoServico);
    }

    private Map<String, Object> populateDetailsCabecalhoRadar(Long idDoctoServico, Long idFilialOrigem) {
        Map<String, Object> map = new HashMap<>();

        EventoDocumentoServico edsDhEmbarque = eventoDocumentoServicoService
                .findEventosByIdDoctoServicoAndCdEventoAndIdFilial(idDoctoServico, ConstantesEventosDocumentoServico.CD_EVENTO_SAIDA_NA_PORTARIA, idFilialOrigem);

        if (edsDhEmbarque != null) {
            map.put("dhEmbarque", JTFormatUtils.format(edsDhEmbarque.getDhEvento(), "dd/MM/YYYY hh:mm"));
        }

        EventoDocumentoServico edsDhEntrega = eventoDocumentoServicoService
                .findEventosByIdDoctoServicoAndCdEventoAndIdFilial(idDoctoServico, (short) ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA, null);

        if (edsDhEntrega != null) {
            map.put("dhEntrega", JTFormatUtils.format(edsDhEntrega.getDhEvento(), "dd/MM/YYYY hh:mm"));
        }

        populateCampoStatusDetailRadar(idDoctoServico, map);

        return map;
    }

    private void populateCampoStatusDetailRadar(Long idDoctoServico, Map<String, Object> map) {
        MotivoAberturaNc motivoAberturaNc = motivoAberturaNcService.findMotivoAberturaNcByIdDoctoServico(idDoctoServico);
        if (motivoAberturaNc != null) {
            map.put("motivoAberturaNcId", motivoAberturaNc.getIdMotivoAberturaNc());
            map.put("motivoAberturaNc", motivoAberturaNc.getDsMotivoAbertura().getValue());
        } else {
            List<ManifestoEntregaDocumento> l = manifestoEntregaDocumentoService.findManifestoEntregaDocumentoByDoctoServico(idDoctoServico);
            map.put("emRotaEntrega", CollectionUtils.isNotEmpty(l));
            map.put("nrManifesto", CollectionUtils.isNotEmpty(l) ? l.get(0).getManifestoEntrega().getNrManifestoEntrega() : null);

            EventoDocumentoServico edsNaFilialDestino = eventoDocumentoServicoService
                    .findEventosByIdDoctoServicoAndCdEventoAndIdFilial(idDoctoServico, ConstantesEventosDocumentoServico.CD_EVENTO_FIM_DE_DESCARGA, null);
            map.put("naFilialDestino", (edsNaFilialDestino != null));
        }
    }

    public List<NotaFiscalConhecimento> findByNrChavesNaFilial(List<String> nrChavesList, Filial filialOrigem){
        return getNotaFiscalConhecimentoDAO().findByNrChavesNaFilial(nrChavesList, filialOrigem);
    }
    
    public List<Long> findNfDisponivel(Long idDoctoServico){
        return getNotaFiscalConhecimentoDAO().findNfDisponivel(idDoctoServico);
    }
    
    public List<Map<String, Object>> findNotasFiscaisDisponiveisByIdDoctoServico(Long idDoctoServico){
        return getNotaFiscalConhecimentoDAO().findNotasFiscaisDisponiveisByIdDoctoServico(idDoctoServico);
    }
    
    public List<NotaFiscalDMN> findNotasFiscaisConhecimentoIntegracaoFedex() {
        List<NotaFiscalDMN> retornoList = new ArrayList<NotaFiscalDMN>();

        List<Long> idsFiliaisIntegraOcorreFedex = getIdsFilaisFedex();
        if (CollectionUtils.isNotEmpty(idsFiliaisIntegraOcorreFedex)) {
            List<NotaFiscalConhecimento> listNFC = this.findNotasFiscaisConhecimentoIntegracaoFedex(idsFiliaisIntegraOcorreFedex);

            for (NotaFiscalConhecimento notaFiscalConhecimento : listNFC) {
                NotaFiscalDMN nfDMN = new NotaFiscalDMN();
                nfDMN.setCNPJRemetente(notaFiscalConhecimento.getConhecimento().getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao());
                nfDMN.setCNPJDestinatario(notaFiscalConhecimento.getConhecimento().getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao());
                nfDMN.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal().toString());
                nfDMN.setDtEmissaoNF(notaFiscalConhecimento.getDtEmissao().toString("yyyyMMdd"));
                nfDMN.setIdDoctoServico(notaFiscalConhecimento.getConhecimento().getIdDoctoServico());

                retornoList.add(nfDMN);
            }
        }

        return retornoList;
    }

    public List findNotasFiscaisConhecimentoIntegracaoFedex(List<Long> idsFiliaisIntegraOcorreFedex) {
        return getNotaFiscalConhecimentoDAO().findNotasFiscaisConhecimentoIntegracaoFedex(idsFiliaisIntegraOcorreFedex);
    }

    public List<NotaFiscalConhecimento> findByIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade) {
        return getNotaFiscalConhecimentoDAO().findByIdOcorrenciaNaoConformidade(idOcorrenciaNaoConformidade);
    }
    
    public List findByidDoctoServico(Long idDoctoServico) {
        return this.getNotaFiscalConhecimentoDAO().findByidDoctoServico(idDoctoServico);
    }

    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoNotaCreditoPadrao(Long idConhecimento, Long idControleCarga) {
    	return getNotaFiscalConhecimentoDAO().findNotasFiscaisConhecimentoNotaCreditoPadrao(idConhecimento, idControleCarga);
    }
    
    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoSemOcorrenciaEntrega(Long idConhecimento, Long idControleCarga) {
        return getNotaFiscalConhecimentoDAO().findNotasFiscaisConhecimentoSemOcorrenciaEntrega(idConhecimento, idControleCarga);
    }

    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoSemNotaFiscalOperada(Long idConhecimento) {
        return getNotaFiscalConhecimentoDAO().findNotasFiscaisConhecimentoSemNotaFiscalOperada(idConhecimento);
    }
    
    private List<Long> getIdsFilaisFedex() {
        List<Integer> cdsFiliais = asList((String) configuracoesFacade.getValorParametro(CD_INTEGRA_OCO_FDX));
        if (CollectionUtils.isNotEmpty(cdsFiliais)) {
            List<Filial> filiaisIntegraOcorreFedex = filialService.findFiliaisByCodsFilial(cdsFiliais);

            List<Long> idsFiliaisIntegraOcorreFedex = new ArrayList<Long>();
            for (Filial filial : filiaisIntegraOcorreFedex) {
                idsFiliaisIntegraOcorreFedex.add(filial.getIdFilial());
            }
            return idsFiliaisIntegraOcorreFedex;
        } else {
            return Collections.emptyList();
        }
    }

    private List<Integer> asList(String valorParametro) {
        if (valorParametro == null) {
            return Collections.emptyList();
        }

        List<Integer> retorno = new ArrayList<Integer>();
        List<String> stringList = Arrays.asList(valorParametro.split(";"));
        for (String string : stringList) {
            retorno.add(Integer.valueOf(string));
        }

        return retorno;
    }
    
    /**
     * Remove todas as notas fiscais vinculadas ao conhecimento recebido.
     *
     * @param idConhecimento identificador do conhecimento
     */
    public void removeByIdConhecimento(Long idConhecimento) {
        getNotaFiscalConhecimentoDAO().removeByIdConhecimento(idConhecimento);
    }


    public VersaoDescritivoPceService getVersaoDescritivoPceService() {
        return versaoDescritivoPceService;
    }

    public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
        this.versaoDescritivoPceService = versaoDescritivoPceService;
    }

    public ConfiguracoesFacade getConfiguracoesFacade() {
        return configuracoesFacade;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public DoctoServicoService getDoctoServicoService() {
        return doctoServicoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
        this.reportExecutionManager = reportExecutionManager;
    }

    public LMComplementoDAO getLmComplementoDao() {
        return lmComplementoDao;
    }

    public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
        this.lmComplementoDao = lmComplementoDao;
    }

    public ParametroGeralService getParametroGeralService() {
        return parametroGeralService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public FilialService getFilialService() {
        return filialService;
}

    public IntegracaoJmsService getIntegracaoJmsService() {
        return integracaoJmsService;
    }

    public EventoDocumentoServicoService getEventoDocumentoServicoService() {
        return eventoDocumentoServicoService;
    }

    public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
        this.eventoDocumentoServicoService = eventoDocumentoServicoService;
    }

    public MotivoAberturaNcService getMotivoAberturaNcService() {
        return motivoAberturaNcService;
    }

    public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
        this.motivoAberturaNcService = motivoAberturaNcService;
    }

    public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
        return manifestoEntregaDocumentoService;
    }

    public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
        this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
    }
}

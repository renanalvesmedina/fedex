package com.mercurio.lms.expedicao.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.TBInputDocuments;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.cancelarConhecimentoEletronicoService"
 */
public class CancelarConhecimentoEletronicoService {

    private static final String PASTA_XML_CTE_CANCELADO = "B2B_PASTA_XML_CTE_CANCELADO";
    private static final String REMETENTE_EMAIL_LMS = "REMETENTE_EMAIL_LMS";
    private static final String TEXT_HTML= "text/html; charset='utf-8'";

    private DevedorDocServFatService devedorDocServFatService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConhecimentoService conhecimentoService;
	private ReciboReembolsoService reciboReembolsoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ParametroGeralService parametroGeralService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private UnidadeFederativaService unidadeFederativaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ConfiguracoesFacade configuracoesFacade;
	private ContatoService contatoService;
	private IntegracaoJmsService integracaoJmsService;
	private ClienteService clienteService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private NFEConjugadaService nfeConjugadaService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private WorkflowPendenciaService workflowPendenciaService;

	private File criaAnexo1XML(String nomeAnexo1,String xml) throws IOException {
		HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();

		StringBuilder host = new StringBuilder();
		host.append("http://");
		host.append(request.getLocalAddr());
		host.append(":");
		host.append(request.getLocalPort());
		host.append(request.getContextPath());

		JRRemoteReportsRunner reportsRunner = new JRRemoteReportsRunner(host.toString());
		File xmlTemp = File.createTempFile(nomeAnexo1, ".xml", reportsRunner.generateOutputDir());
		Writer out = new OutputStreamWriter(new FileOutputStream(xmlTemp), Charset.forName("UTF-8"));

		try {
			out.write(xml);
		}finally {
			out.close();
		}

		return xmlTemp;
	}
	private String gerarEmailCancelamentoTexto(String prefixo,MonitoramentoDocEletronico mde,Filial filial){
		return new StringBuilder().append(prefixo).append(" ")
								   .append(filial.getSgFilial()).append(" para ").append(mde.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial())
								   .append(" ").append(mde.getDoctoServico().getNrDoctoServico()).toString();

	}
	private void enviarEmailXMLCancelamento(final MonitoramentoDocEletronico mde,String xml,Filial filial){
		Cliente cliente = clienteService.findById(mde.getDoctoServico().getClienteByIdClienteRemetente().getIdCliente());
		if ( cliente == null ){
			return;
		}
		List<Contato> contatos = getContatosCte(cliente);
		if ( contatos.isEmpty() ){
			return;
		}
		String remetenteLms = (String) configuracoesFacade.getValorParametro(REMETENTE_EMAIL_LMS);
		String assunto = gerarEmailCancelamentoTexto("Aviso de Cancelamento do CT-e ",mde,filial);
		String mensagem = gerarEmailCancelamentoTexto("Informamos o cancelamento do CT-e ",mde,filial);


		final List<File> anexos = new ArrayList<File>();
		try {
			anexos.add(criaAnexo1XML(getNomeAnexo(mde), xml));
		} catch (IOException e) {
			throw new ADSMException(e);
		}
		this.sendEmail(remetenteLms, extractEmails(contatos), assunto, anexos, mensagem);
	}

	protected List<Contato> getContatosCte(Cliente cliente) {
		List<Contato> contatos = contatoService.findContatosByIdPessoaTpContato(cliente.getIdCliente(), "CT");
		if( contatos == null ) {
			contatos = new ArrayList<Contato>();
		}
		if ( contatos.isEmpty() && pessoaTemEmail(cliente) ){
			Contato contato = new Contato();
			contato.setDsEmail(cliente.getPessoa().getDsEmail());
			contatos.add(contato);
		}
		return contatos;
	}
	protected boolean pessoaTemEmail(Cliente cliente){
		return cliente.getPessoa() != null && !StringUtils.isEmpty(cliente.getPessoa().getDsEmail());
	}

	protected List<String> extractEmails(final List<Contato> contatos) {
		List<String> destinatarios = new ArrayList<String>();
		for(Contato contato : contatos){
			destinatarios.add(contato.getDsEmail().trim());
		}
		return destinatarios;
	}

	private void sendEmail(final String remetenteLms, final List<String> dsEmails, final String assunto,
			final List<File> anexos, final String mensagem) {

		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();

		for (File anexo : anexos) {
			MailAttachment mailAttachment = new MailAttachment();
			mailAttachment.setName(anexo.getName());
			mailAttachment.setData(FileUtils.readFile(anexo));
			mailAttachments.add(mailAttachment);
		}

		Mail mail = createMail(
			StringUtils.join(dsEmails.toArray(new String[dsEmails.size()]), ";"),
			remetenteLms,
			assunto,
			mensagem,
			mailAttachments
		);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body, List<MailAttachment> mailAttachmentList) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));
		return mail;
	}

	private String getNomeAnexo(MonitoramentoDocEletronico moEletronico) {
		StringBuilder nomeArquivo = new StringBuilder();
		nomeArquivo.append("CT-e_");
		nomeArquivo.append(moEletronico.getNrChave());
		return nomeArquivo.toString();
	}

	public void cancelar(final MonitoramentoDocEletronico monitoramentoDocEletronico) throws UnsupportedEncodingException, Exception{

		String tpDocumentoServico = monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue();

		boolean isCTE = ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(tpDocumentoServico);
		boolean isNTE = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpDocumentoServico);
		boolean isCTR = ConstantesExpedicao.CONHECIMENTO_NACIONAL.equalsIgnoreCase(tpDocumentoServico);
		boolean isNSE = ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(tpDocumentoServico);

		if( isCTE || isNTE ||  isCTR ){
			storeConhecimento(monitoramentoDocEletronico, isCTE, isNTE, isCTR);


			//LMS-4097 - Ao clicar no botão cancelar para documentos do tipo 'NSE'
		} else if(isNSE){

				//[3.14] - Validar a data de emissão do documento
				ConhecimentoCancelarService.validateDataEmissao(monitoramentoDocEletronico.getDoctoServico().getDhEmissao());

				//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.14 até 3.17 / Cancelamento de NSE
				notaFiscalServicoService.removeCancelaNF(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());

				NotaFiscalServico nfs = notaFiscalServicoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());

				if(isAtivaNfeConjugada(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial())){
					monitoramentoDocEletronico.setDhEnvio(JTDateTimeUtils.getDataHoraAtual(SessionUtils.getFilialSessao()));
					nfeConjugadaService.storeCancelar(nfs.getIdDoctoServico());
				}else{
					//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.18 /Cancelamento de NSE
					TBInputDocuments tbInputDocuments = notaFiscalEletronicaService.executeCancelamentoNse(nfs, monitoramentoDocEletronico);
					monitoramentoDocEletronico.setIdEnvioDocEletronicoC(tbInputDocuments.getId());
				}

			}

		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("B"));
		monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);

	}
	/**
	 * @return
	 */
	private boolean isAtivaNfeConjugada(Long idFilial) {
		return nfeConjugadaService.isAtivaNfeConjugada(idFilial);
	}

	private void storeConhecimento(final MonitoramentoDocEletronico monitoramentoDocEletronico,
			boolean isCTE, boolean isNTE, boolean isCTR) {
		final Conhecimento conhecimento = conhecimentoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
		//LMS-4210
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		generateCancelarConhecimento(conhecimento, false, filialUsuarioLogado);

		if(isCTE || isCTR){
			//LMS-4210
			String conteudoParametro = String.valueOf(conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialUsuarioLogado.getIdFilial(), "Versão_XML_CTe", false));
			EVersaoXMLCTE eVersaoXMLCTE = EVersaoXMLCTE.getByConteudoParametro(conteudoParametro);

			String xml;

			switch (eVersaoXMLCTE) {

				case VERSAO_300a:
				case VERSAO_400:
					xml = geraXMLCancCTE(monitoramentoDocEletronico, eVersaoXMLCTE);
					break;

				default:
					throw new IllegalArgumentException("Versao do XML do CTE nao reconhecida pelo LMS");
			}
			enviarEmailXMLCancelamento(monitoramentoDocEletronico,xml,filialUsuarioLogado);
			TBDatabaseInputCTE tbDatabaseInput = integracaoNDDigitalService.generateIntegracaoCancelamento(monitoramentoDocEletronico, xml);

			monitoramentoDocEletronico.setIdEnvioDocEletronicoC(tbDatabaseInput.getId());

			if(conhecimento.getPendencia() != null && new DomainValue(ConstantesWorkflow.EM_APROVACAO).equals(conhecimento.getPendencia().getTpSituacaoPendencia())) {
				this.workflowPendenciaService.cancelPendencia(conhecimento.getPendencia().getIdPendencia());
			}

		}else if(isNTE){

			if(isAtivaNfeConjugada(conhecimento.getFilialOrigem().getIdFilial())){
				monitoramentoDocEletronico.setDhEnvio(JTDateTimeUtils.getDataHoraAtual(SessionUtils.getFilialSessao()));
				nfeConjugadaService.storeCancelar(conhecimento.getIdDoctoServico());
			}else{
				//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.10 /Cancelamento de NFE
				TBInputDocuments tbInputDocuments = notaFiscalEletronicaService.executeCancelamentoNte(conhecimento, monitoramentoDocEletronico);
				monitoramentoDocEletronico.setIdEnvioDocEletronicoC(tbInputDocuments.getId());
			}
		}

		conhecimentoService.store(conhecimento);
	}

	public void generateCancelarConhecimento(final Conhecimento conhecimento, boolean isInutilizacao, Filial filialUsuarioLogadoParam) {
		Filial filialUsuarioLogado = filialUsuarioLogadoParam != null ? filialUsuarioLogadoParam : SessionUtils.getFilialSessao();

		boolean isCteOrCtr = ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue()) ||
			ConstantesExpedicao.CONHECIMENTO_NACIONAL.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue());

		boolean isNteOrNse = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue()) ||
				ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(conhecimento.getTpDocumentoServico().getValue());

		if(isCteOrCtr || isNteOrNse) {
			ConhecimentoCancelarService.validateDataEmissao(conhecimento.getDhEmissao());
		}
		if (isCteOrCtr && isInutilizacao) {
			validateInutilizarCTE(conhecimento);
		} else {
			validateCancelarCTRC(conhecimento, filialUsuarioLogado);
		}
		if (isCteOrCtr && !isInutilizacao) {
			validateCancelarPrazoLegal(conhecimento,"PRAZO_LEGAL_CANC_CTE");
		}
		if(isNteOrNse && isAtivaNfeConjugada(conhecimento.getFilialOrigem().getIdFilial()) ){
			validateCancelarPrazoLegal(conhecimento,"PRAZO_CANC_NFE_CONJUGADA");
		}

		prepareConhecimentoCancelado(conhecimento);

		final Long idLocalizacaoMercadoria = Long.valueOf(String.valueOf(parametroGeralService.findConteudoByNomeParametro("ID_LOCA_DOC_CANCELADO", false)));
		final LocalizacaoMercadoria localizacaoMercadoria = new LocalizacaoMercadoria();
		localizacaoMercadoria.setIdLocalizacaoMercadoria(idLocalizacaoMercadoria);
		conhecimento.setLocalizacaoMercadoria(localizacaoMercadoria);

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
			ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
			conhecimento.getIdDoctoServico(),
			conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
			String.valueOf(ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico())),
			JTDateTimeUtils.getDataHoraAtual(),
			null,
			null,
			conhecimento.getTpDocumentoServico().getValue()
		);


		cancelarDevedorDocServFat(conhecimento);

		liberacaoNotaNaturaService.atualizaTerraNaturaCancelado(conhecimento.getIdDoctoServico());

		inativarMonitoramentoCCT(conhecimento.getIdDoctoServico());

		notaFiscalConhecimentoService.removeByIdConhecimento(conhecimento.getIdDoctoServico());

		reciboReembolsoService.executeCancelaReciboByDoctoServico(conhecimento.getIdDoctoServico());

		notaFiscalOperadaService.removeByIdDoctoServico(conhecimento.getIdDoctoServico());
	}

	/**
	 * Para localizar o serviço de monitoramento é necessário localizar a chave da NFe utilizada.
	 * Isto deve ser feito antes de remover os registros da tabela nota fiscal conhecimento.
	 *
	 * Rotina previamente inserida no batch: ProcessamentoRetornoCTEItemV103Service.java
	 *
	 * LMS-6744
	 *
	 * @param idDoctoServico
	 */
	@SuppressWarnings("unchecked")
	private void inativarMonitoramentoCCT(Long idDoctoServico){
		List<NotaFiscalConhecimento> nfcs = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);

		for (NotaFiscalConhecimento nfc : nfcs) {
			MonitoramentoCCT monitoramentoCCT = monitoramentoNotasFiscaisCCTService.findMonitoramentoCCTByNrChave(nfc.getNrChave());

			if (monitoramentoCCT != null) {
				monitoramentoNotasFiscaisCCTService.storeEvento("CA", nfc.getNrChave(), null, null, monitoramentoCCT.getDoctoServico(), null, null, SessionUtils.getUsuarioLogado());
			}
		}
	}

	private void cancelarDevedorDocServFat(final Conhecimento conhecimento) {
		DevedorDocServFat devedorDocServFat = devedorDocServFatService.findDevedorDocServFatByIdDoctoServico(conhecimento.getIdDoctoServico());
		if(devedorDocServFat != null) {
			devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
			devedorDocServFat.setDtLiquidacao(YearMonthDay.fromDateFields(conhecimento.getDhEmissao().toDate()));
			devedorDocServFatService.store(devedorDocServFat);
		}
	}

	private void validateCancelarPrazoLegal(final Conhecimento conhecimento, String parametro) {

		Long qtdeHorasPrazo = null;

		UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(SessionUtils.getFilialSessao().getIdFilial());
		if(uf.getNrPrazoCancCte() == null){
			qtdeHorasPrazo = Long.valueOf(String.valueOf(parametroGeralService.findConteudoByNomeParametro(parametro, false)));
		} else {
			qtdeHorasPrazo = uf.getNrPrazoCancCte();
		}


		if(conhecimento.getDhEmissao().isBefore(JTDateTimeUtils.getDataHoraAtual().minusHours(qtdeHorasPrazo.intValue()))){
			throw new BusinessException("LMS-04377");
		}
	}

	private String geraXMLCancCTE(MonitoramentoDocEletronico monitoramentoDocEletronico, EVersaoXMLCTE eVersaoXMLCTE){

		//TODO - Substituir por pasta de cancelamento exclusiva para não entrar em conflito com os ctes que serão enviados para os clientes. E: B2B_PASTA_CANC_XML_CTE
		String pastaCteB2B = (String) parametroGeralService.findConteudoByNomeParametro(PASTA_XML_CTE_CANCELADO, false);
		StringBuilder sb = new StringBuilder();
		String nrVersaoLayout = (String) conteudoParametroFilialService.findConteudoByNomeParametroWithException(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial(), "Versão_XML_CTe", false);
		nrVersaoLayout = nrVersaoLayout.replaceAll("[a-z]|[A-Z]", "");
		String urlPortalFiscal = "http://www.portalfiscal.inf.br/cte";
		String xmlns = " xmlns=";

		sb.append("<eventoCTe").append(xmlns).append("\"").append(urlPortalFiscal).append("\"")
				.append(" versao=").append("\"").append(nrVersaoLayout).append("\"").append(">");

			sb.append("<infEvento").append(xmlns).append("\"").append(urlPortalFiscal).append("\"")
					.append(" Id=").append("\"").append("ID110111").append(monitoramentoDocEletronico.getNrChave())
					.append(("4.00".equals(nrVersaoLayout) ? "001" : "01")).append("\"").append(">");

				sb.append("<cOrgao>");
				UnidadeFederativa uf = unidadeFederativaService.findByIdPessoa(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
				if(uf != null) {
					sb.append(uf.getNrIbge());
				}

				sb.append("</cOrgao>");

				sb.append("<tpAmb>");
					sb.append(parametroGeralService.findSimpleConteudoByNomeParametro("AMBIENTE_CTE"));
				sb.append("</tpAmb>");

				sb.append("<CNPJ>");
					sb.append(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao());
				sb.append("</CNPJ>");

				sb.append("<chCTe>");
					sb.append(monitoramentoDocEletronico.getNrChave());
				sb.append("</chCTe>");

				sb.append("<dhEvento>");

				sb.append(JTFormatUtils.format(JTDateTimeUtils.getDataHoraAtual(), "yyyy'-'MM'-'dd'T'HH':'mm':'ssZZ"));

				sb.append("</dhEvento>");

				sb.append("<tpEvento>");
					sb.append("110111");
				sb.append("</tpEvento>");

				sb.append("<nSeqEvento>");
					sb.append("1");
				sb.append("</nSeqEvento>");

				sb.append("<detEvento").append(" versaoEvento=").append("\"").append(nrVersaoLayout).append("\"").append(">");
					sb.append("<evCancCTe").append(xmlns).append("\"").append(urlPortalFiscal).append("\"").append(">");
						sb.append("<descEvento>");
							sb.append("Cancelamento");
						sb.append("</descEvento>");

						sb.append("<nProt>");
							sb.append(monitoramentoDocEletronico.getNrProtocolo());
						sb.append("</nProt>");

						sb.append("<xJust>");
							sb.append(parametroGeralService.findSimpleConteudoByNomeParametro("JUST_CANC_CTE")).append("</xJust>");
					sb.append("</evCancCTe>");
				sb.append("</detEvento>");
			if (StringUtils.isNotEmpty(pastaCteB2B)){
				sb.append("<dadosAdic>");
				sb.append("<B2BDirectory>");
				sb.append(pastaCteB2B);
				sb.append("</B2BDirectory>");
				sb.append("</dadosAdic>");
			}
			sb.append("</infEvento>");

		sb.append("</eventoCTe>");

		return  sb.toString();
	}

	private void prepareConhecimentoCancelado(final Conhecimento conhecimento) {
		conhecimento.setTpSituacaoConhecimento(new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO));
		conhecimento.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());

		conhecimento.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());
	}

	private void validateCancelarCTRC(final Conhecimento conhecimentoOriginal, final Filial filialUsuarioLogado) {
		EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findUltimoEventoDoctoServico(conhecimentoOriginal.getIdDoctoServico(), ConstantesSim.TP_EVENTO_REALIZADO, Boolean.FALSE);
		if(isUltimoEventoDocumentoEmitido(eventoDocumentoServico)) {
			//Verifica se o Conhecimento ja foi faturado
			devedorDocServFatService.validateCTRCFaturado(conhecimentoOriginal.getIdDoctoServico());

			//Verifica se a filial do usuario logado eh a mesma filial de origem do documento de servico
			Long idFilialOrigem = conhecimentoOriginal.getFilialByIdFilialOrigem().getIdFilial();
			if(!idFilialOrigem.equals(filialUsuarioLogado.getIdFilial())) {
				throw new BusinessException("LMS-04084");
			}
		} else {
			throw new BusinessException("LMS-04092");
		}
	}

	private boolean isUltimoEventoDocumentoEmitido(EventoDocumentoServico eventoDocumentoServico) {
		return eventoDocumentoServico != null && (ConstantesSim.EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServico.getEvento().getCdEvento())
				|| (ConstantesSim.EVENTO_DOCUMENTO_EMITIDO_SEM_LOCALIZACAO.equals(eventoDocumentoServico.getEvento().getCdEvento())));
	}

	private void validateInutilizarCTE(final Conhecimento conhecimento){
		BigDecimal prazoLegalInutCTE = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("PRAZO_LEGAL_INUT_CTE", false);

		if(conhecimento.getDhEmissao().isBefore(JTDateTimeUtils.getDataHoraAtual().minusHours(prazoLegalInutCTE.intValue()))){
			throw new BusinessException("LMS-04377");
		}

		//Verifica se a filial do usuario logado eh a mesma filial de origem do documento de servico
		Long idFilialOrigem = conhecimento.getFilialByIdFilialOrigem().getIdFilial();
		if(!idFilialOrigem.equals(SessionUtils.getFilialSessao().getIdFilial())) {
			throw new BusinessException("LMS-04084");
		}
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}

	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}
	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}
	
	public void setNotaFiscalOperadaService(NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
}

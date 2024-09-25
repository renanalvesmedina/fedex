package com.mercurio.lms.expedicao.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.vendas.model.EnvioCteCliente;
import com.mercurio.lms.vendas.model.service.ManterParametrizacaoEnvioService;

/**
 * @author JonasFE
 *
 *         Não inserir documentação após ou remover a tag do XDoclet a seguir. O
 *         valor do <code>id</code> informado abaixo deve ser utilizado para
 *         referenciar este serviço.
 * @spring.bean id="lms.expedicao.reenviarConhecimentoEletronicoService"
 */
public class ReenviarConhecimentoEletronicoService {
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private GerarConhecimentoEletronicoService gerarConhecimentoEletronicoService;
	private ConhecimentoService conhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private IntegracaoJmsService integracaoJmsService;
	private ContatoService contatoService;
	private PessoaService pessoaService;
    private ManterParametrizacaoEnvioService manterParametrizacaoEnvioService;
    private NotaFiscalEletronicaService notaFiscalEletronicaService;
    
    public void generateReenvioConhecimentoEletronico(final Long idMonitoramentoDocEletronic) {
    	generateReenvioConhecimentoEletronicoDataAtual(idMonitoramentoDocEletronic, false);

	}
	
    public void generateReenvioConhecimentoEletronicoDataAtual(final Long idMonitoramentoDocEletronic, Boolean reenviarDataAtual){
    	final MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
    	//na ET não está especificado para outros tipos
    	if (ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue())) {
    		//busca o tipo específico
    		final Conhecimento conhecimento = conhecimentoService.findById(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
    		gerarConhecimentoEletronicoService.generateConhecimentoEletronicoEmissaoGenerico(conhecimento,monitoramentoDocEletronico);
		} else if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue()) 
    			|| ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(monitoramentoDocEletronico.getDoctoServico().getTpDocumentoServico().getValue())){
    		//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.23 / Reenviar de NTE-NSE
			//LMS-8003
			Long proximoNumeroRPS = monitoramentoDocEletronicoService.executeGetProximoNumeroRPS(monitoramentoDocEletronico.getDoctoServico());
			
			notaFiscalEletronicaService.gerarNotaFiscalEletronica(monitoramentoDocEletronico, monitoramentoDocEletronico.getDoctoServico(), proximoNumeroRPS,reenviarDataAtual);
    		
    	}
    }
	
    public void generateReenvioClienteConhecimentoEletronico(final Long idMonitoramentoDocEletronic)
    		throws UnsupportedEncodingException, Exception {
    	String remetenteLms = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String assunto = "";
		String mensagem = "";
		String nomeAnexo1;
		String nomeAnexo2;
		List<String> destinatarios = new ArrayList<String>();
		final List<File> anexos = new ArrayList<File>();

		MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);

		for (DevedorDocServ devedorDocServ : mde.getDoctoServico().getDevedorDocServs()) {
			EnvioCteCliente envioCteClientes = manterParametrizacaoEnvioService
					.findEnvioCteClienteByIdClienteTpEnvioEAndParametrizacaoC(devedorDocServ.getCliente()
							.getIdCliente());
			/*
			 * DESTINATARIOS
			 */
			destinatarios = this.getDestinatario(devedorDocServ);

			/*
			 * Se o cliente tomador do CT-e tiver parametrização de envio de
			 * CT-e por email
			 */
			if (envioCteClientes != null) {
				if (destinatarios == null || destinatarios.isEmpty()) {
					return;
				}

				/*
				 * ASSUNTO
				 */
				assunto = this.getAssuntoForClienteTomador(envioCteClientes, mde);

				/*
				 * CORPO EMAIL
				 */
				mensagem = envioCteClientes.getDsTextoEmail();

				/*
				 * ANEXOS
				 */
				nomeAnexo1 = this.getNomeAnexo1ForClienteTomador(envioCteClientes, mde);
				anexos.add(this.criaAnexo1XML(mde, nomeAnexo1));

				if (envioCteClientes.getBlEnviaPdf()) {
					nomeAnexo2 = this.getNomeAnexo2ForClienteTomador(envioCteClientes, mde);
					anexos.add(this.criaAnexo2PDF(mde, nomeAnexo2));
				}
			}
			else {
				if (destinatarios.isEmpty()) {
					throw new BusinessException("LMS-04399");
				}

				assunto = (String) configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE");
				mensagem = (String) configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE");

				/*
				 * ANEXOS
				 */
				nomeAnexo1 = this.getNomeAnexo(mde);
				anexos.add(this.criaAnexo1XML(mde, nomeAnexo1));

				String obrigaCTEGeral = (String) configuracoesFacade.getValorParametro("OBRIGA_PDF_CTE");
				String obrigaCTEFilial = (String) configuracoesFacade.getValorParametro(mde.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial(), "OBRIGA_PDF_CTE");
				boolean obrigaCTE = false;

				if(obrigaCTEGeral != null){
					if("S".equalsIgnoreCase(obrigaCTEGeral)){
						obrigaCTE = true;
					}
				}

				if(obrigaCTEFilial != null){
					if("S".equalsIgnoreCase(obrigaCTEFilial)){
						obrigaCTE = true;
					}
				}

				if(obrigaCTE){
					nomeAnexo2 = nomeAnexo1;
					anexos.add(this.criaAnexo2PDF(mde, nomeAnexo2));
				}
				
				
			}

			//LMS-7047 - solução para evitar 
			if (null == mensagem || mensagem.isEmpty()) {
				mensagem = " ";
			}

			if (null == assunto || assunto.isEmpty()) {
				assunto = " ";
			}
			
			this.sendEmail(remetenteLms, destinatarios, assunto, anexos, mensagem);
		}
	}

	private File criaAnexo1XML(MonitoramentoDocEletronico moEletronico, String nomeAnexo1)
			throws UnsupportedEncodingException, Exception {
		String xml = new String(moEletronico.getDsDadosDocumento(), Charset.forName("UTF-8"));

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
		}
		finally {
			out.close();
		}

		return xmlTemp;
	}

	private File criaAnexo2PDF(MonitoramentoDocEletronico moEletronico, String nomeAnexo1)
			throws UnsupportedEncodingException, Exception {

		String xml = gerarConhecimentoEletronicoService.findXmlCteComComplementos(moEletronico.getDoctoServico().getIdDoctoServico());

		String host = configuracoesFacade.getValorParametro("ADSM_REPORT_HOST").toString();

		JRRemoteReportsRunner reportsRunner = new JRRemoteReportsRunner(host.toString());
		File pdfTemp =  reportsRunner.executeReport(xml, null, nomeAnexo1);

		return pdfTemp;
	}

	private String getNomeAnexo(MonitoramentoDocEletronico moEletronico) {
		StringBuilder nomeArquivo = new StringBuilder();
		nomeArquivo.append("CT-e_");
		nomeArquivo.append(moEletronico.getNrChave());
		return nomeArquivo.toString();
	}

    /**
     * Anexo 1: Somente sera gerado se ENVIO_CTE_CLIENTE.BL_ENVIA_XML for igual
     * a “S”, conforme abaixo:
     * 
     * Se ENVIO_CTE_CLIENTE.NM_XML estiver nulo sera um arquivo com o nome
     * “CT-e_” + MONITORAMENTO_DOC_ELETRONICO.NR_CHAVE, extencao “.XML” e com o
     * conteudo do campo MONITORAMENTO_DOC_ELETRONICO.DS_DADOS_DOCUMENTO.
     * 
     * Caso contrario sera sera um arquivo com o nome ENVIO_CTE_CLIENTE.NM_XML
     * (se diferente de nulo) + um espaco em branco (se o campo NM_XML estiver
     * preenchido e o campo BL_ESPACO_XML for igual a “S”) + a chave do CT-e
     * (MONITORAMENTO_DOC_ELETRONICO.NR_CHAVE (se o campo BL_CHAVE_XML for igual
     * a “S”).
     * 
     * @param envioCteClientes
     * @param moEletronico
     * @return
     */
	private String getNomeAnexo1ForClienteTomador(EnvioCteCliente envioCteClientes,
			MonitoramentoDocEletronico moEletronico) {
		StringBuilder nomeArquivo = new StringBuilder();

		if (envioCteClientes.getBlEnviaXml()) {
			if (envioCteClientes.getNmXml() == null || envioCteClientes.getNmXml().equals("")) {
				nomeArquivo.append("CT-e_");
				nomeArquivo.append(moEletronico.getNrChave());
			}
			else {
				nomeArquivo.append(envioCteClientes.getNmXml());

				if (envioCteClientes.getBlEspacoXml()) {
					nomeArquivo.append(" ");
				}

				if (envioCteClientes.getBlChaveXml()) {
					nomeArquivo.append(moEletronico.getNrChave());
				}
			}
		}

		return nomeArquivo.toString();
	}

    /**
     * 6. Anexo 2: Somente sera gerado se ENVIO_CTE_CLIENTE.BL_ENVIA_PDF for
     * igual a “S”, conforme abaixo:
     * 
     * Se ENVIO_CTE_CLIENTE.NM_PDF estiver nulo sera um arquivo com o nome
     * “CT-e_” + MONITORAMENTO_DOC_ELETRONICO.NR_CHAVE, extencao “.PDF” e como
     * conteudo o PDF do DACTE do CT-e que esta sendo processado.
     * 
     * Caso contrario será um arquivo com o nome ENVIO_CTE_CLIENTE.NM_PDF + um
     * espaco em branco (se o campo BL_ESPACO_PDF for igual a “S”) + a chave do
     * CT-e (MONITORAMENTO_DOC_ELETRONICO.NR_CHAVE (se o campo BL_CHAVE_PDF for
     * igual a “S”).
     * 
     * @param envioCteClientes
     * @param moEletronico
     * @return
     */
	private String getNomeAnexo2ForClienteTomador(EnvioCteCliente envioCteClientes,
			MonitoramentoDocEletronico moEletronico) {
		StringBuilder nomeArquivo = new StringBuilder();

		if (envioCteClientes.getBlEnviaPdf()) {
			if (envioCteClientes.getNmPdf() == null || envioCteClientes.getNmPdf().equals("")) {
				nomeArquivo.append("CT-e_");
				nomeArquivo.append(moEletronico.getNrChave());
			}
			else {
				nomeArquivo.append(envioCteClientes.getNmPdf());

				if (envioCteClientes.getBlEspacoPdf()) {
					nomeArquivo.append(" ");
				}

				if (envioCteClientes.getBlChavePdf()) {
					nomeArquivo.append(moEletronico.getNrChave());
				}
			}
		}

		return nomeArquivo.toString();
	}

    /**
     * Destinatario: todos os contatos do cliente tomador do serviço que sejam
     * contatos para CT-e (um email para cada contato) ou o email geral do
     * cliente quando o cliente nao tiver contatos de CT-e cadastrados:
     * 
     * Contatos do tomador para CT-e:
     * 
     * DEVEDOR_DOC_SERV.ID_DOCTO_SERVICO =
     * MONITORAMENTO_DOC_ELETRONICO.ID_DOCTO_SERVICO CONTATO.ID_PESSOA =
     * DEVEDOR_DOC_SERV. ID_CLIENTE CONTATO_CLIENTE.TP_CONTATO = ‘CT’ Campo:
     * CONTATO.DS_EMAIL
     * 
     * Email geral do cliente:
     * 
     * DEVEDOR_DOC_SERV.ID_DOCTO_SERVICO =
     * MONITORAMENTO_DOC_ELETRONICO.ID_DOCTO_SERVICO PESSOA.ID_PESSOA =
     * DEVEDOR_DOC_SERV. ID_CLIENTE Campo: PESSOA.DS_EMAIL
     * 
     * Se nenhum endereço de email for gerado para o cliente nao gerar o
     * referido email.
     * 
     * @param moEletronico
     * @return
     */
	private List<String> getDestinatario(DevedorDocServ docServ) {
		List<String> emailDest = new ArrayList<String>();
		List<Contato> contatosList = contatoService.findContatosByIdPessoaTpContato(
				docServ.getCliente().getIdCliente(), "CT");

		if (contatosList.isEmpty()) {
			Pessoa pessoa = pessoaService.findById(docServ.getCliente().getIdCliente());

			if (pessoa != null) {
				splitEmails(emailDest, pessoa.getDsEmail());
			}
		}
		else {
			for (Contato contato : contatosList) {
				splitEmails(emailDest, contato.getDsEmail());
			}
		}

		return emailDest;
	}
	
	protected void splitEmails(List<String> destinatarios, final String dsEmail) {
		String[] emails = dsEmail.split(";");
		for(String email : emails){
			destinatarios.add(email.trim());
		}
	}
	
	

    /**
     * 3. Assunto:
     * 
     * Se ENVIO_CTE_CLIENTE.DS_ASSUNTO estiver nulo e
     * ENVIO_CTE_CLIENTE.BL_CHAVE_ASSUNTO = ‘N’ sera o conteudo do
     * parametro_geral “ASSUNTO_EMAIL_CTE”;
     * 
     * Caso contrario será o conteudo do campo ENVIO_CTE_CLIENTE.DS_ASSUNTO (se
     * diferente de nulo) + um espaco em branco (se o campo DS_ASSUNTO estiver
     * preenchido e o campo BL_ESPACO_ASSUNTO for igual a “S”) + a chave do CT-e
     * (MONITORAMENTO_DOC_ELETRONICO.NR_CHAVE (se o campo BL_CHAVE_ASSUNTO for
     * igual a “S”).
     * 
     * @param envioCteClientes
     * @param moEletronico
     * @return
     */
	private String getAssuntoForClienteTomador(EnvioCteCliente envioCteClientes, MonitoramentoDocEletronico moEletronico) {
		StringBuilder assunto = new StringBuilder();

		if (envioCteClientes.getDsAssunto() == null && !envioCteClientes.getBlChaveAssunto()) {
			assunto.append(configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE"));
		}
		else {
			assunto.append(envioCteClientes.getDsAssunto());

			if (envioCteClientes.getBlEspacoAssunto()) {
				assunto.append(" ");
			}

			if(envioCteClientes.getBlChaveAssunto()) {
				assunto.append(moEletronico.getNrChave());
			}
		}
		
		//LMS-7044
		if(null == assunto || assunto.length() < 1) {
			assunto.append(" ");
		}

		return assunto.toString();
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
	
	
	public void executeEnviarCTEFatura(Long idFatura) throws UnsupportedEncodingException, Exception{
		List<Conhecimento> listConhecimento = conhecimentoService.findCTEByIdFatura(idFatura);
		for(Conhecimento conhecimento : listConhecimento){
			MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(conhecimento.getIdDoctoServico());
			if(mde != null){
				if(mde.getDsDadosDocumento() != null)
					this.generateReenvioClienteConhecimentoEletronico(mde.getIdMonitoramentoDocEletronic());
			}
		}
	}

    public void setGerarConhecimentoEletronicoService(
	    final GerarConhecimentoEletronicoService gerarConhecimentoEletronicoService) {
		this.gerarConhecimentoEletronicoService = gerarConhecimentoEletronicoService;
	}

    public void setMonitoramentoDocEletronicoService(
	    final MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

    public void setManterParametrizacaoEnvioService(ManterParametrizacaoEnvioService manterParametrizacaoEnvioService) {
	this.manterParametrizacaoEnvioService = manterParametrizacaoEnvioService;
    }

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

}

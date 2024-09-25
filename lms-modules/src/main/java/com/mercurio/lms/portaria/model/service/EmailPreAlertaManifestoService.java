package com.mercurio.lms.portaria.model.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.portaria.model.dao.SaidaChegadaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
/**
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.emailPreAlertaManifestoService"
 */
public class EmailPreAlertaManifestoService {

	private SaidaChegadaDAO dao;
	private ConfiguracoesFacade configuracoesFacade;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private IntegracaoJmsService integracaoJmsService; 

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	/**
	 * Este método dispara cria uma nova thread, uma vez que o método generateEventoPreAlertaManifesto
	 * somente envia emails.
	 * @param manifestos
	 */
	public void generatePreAlertaManifestosAsync(final List<Manifesto> manifestos) {
		Thread asyncThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (Manifesto manifesto : manifestos) {
					generateEventoPreAlertaManifesto(manifesto);
				}
			}
		});
		asyncThread.start();
	}

	/**
	 * Rotina gerarPreAlertaManifesto
	 * @param manifesto
	 */
	@SuppressWarnings("unchecked")
	private void generateEventoPreAlertaManifesto(Manifesto manifesto) {
		
		List<DoctoServico> doctoServicoAereoList = new ArrayList<DoctoServico>();

		List<PreManifestoDocumento> documentos = preManifestoDocumentoService.findPreManifestoDocumentos(manifesto.getIdManifesto(), ConstantesExpedicao.CONHECIMENTO_NACIONAL, ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
		for (PreManifestoDocumento preManifestoDocumento : documentos) {
			if(preManifestoDocumento.getDoctoServico().getServico().getTpModal() != null && 
					preManifestoDocumento.getDoctoServico().getServico().getTpModal().getValue().equals(ConstantesExpedicao.MODAL_AEREO)){
				doctoServicoAereoList.add(preManifestoDocumento.getDoctoServico());
			}
		}

		if(!doctoServicoAereoList.isEmpty()) {
			ordenarListaDoctoServicoAereo(doctoServicoAereoList);
			gerarEmailContatosAereosFilialDestinoManifesto(manifesto, manifesto.getManifestoViagemNacional(), doctoServicoAereoList);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void gerarEmailContatosAereosFilialDestinoManifesto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, 
			List<DoctoServico> doctoServicoAereoList) {
		
		List<String> contatosAereos = new ArrayList<String>();
		if(manifesto.getFilialByIdFilialDestino().getPessoa() != null && manifesto.getFilialByIdFilialDestino().getPessoa().getContatosByIdPessoaContatado() != null){
			contatosAereos = montarContatosAereos(manifesto.getFilialByIdFilialDestino().getPessoa().getContatosByIdPessoaContatado());
		}
		
		if(!contatosAereos.isEmpty()){
			sendEmail(getRemetenteLms(), contatosAereos, montarAssunto(manifesto, manifestoViagemNacional), 
					montarMensagemDestinoManifesto(manifesto, manifestoViagemNacional, doctoServicoAereoList));
		}
	}
	
	private void sendEmail(final String remetenteLms, final List<String> dsEmails, final String assunto, final String mensagem) {
		
		Mail mail = createMail(StringUtils.join(dsEmails.toArray(), ";"), remetenteLms, assunto, mensagem);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}
	
	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {

		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);

		return mail;
	}
	
	private String getRemetenteLms() {
		String remetenteLms = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		return remetenteLms;
	}
	
	private List<String> montarContatosAereos(List<Contato> contatosByIdPessoaContatado) {
		List<String> contatosAereos = new ArrayList<String>();
		
		for (Contato contato : contatosByIdPessoaContatado) {
			if( ConstantesExpedicao.TIPO_CONTATO_AEREO.equalsIgnoreCase(contato.getTpContato().getValue()) ){
				if( StringUtils.isNotBlank(contato.getDsEmail()) ){
					contatosAereos.add(contato.getDsEmail());
				}
			}
		}
		return contatosAereos;
	}
	
	private void ordenarListaDoctoServicoAereo(List<DoctoServico> doctoServicoAereoList) {
		Collections.sort(doctoServicoAereoList, new Comparator<DoctoServico>() {
			@Override
			public int compare(DoctoServico ds1, DoctoServico ds2) {
				int valor = ds1.getFilialByIdFilialDestino().getSgFilial().compareTo(ds2.getFilialByIdFilialDestino().getSgFilial());
				if (valor == 0) {
					valor = ds1.getFilialByIdFilialOrigem().getSgFilial().compareTo(ds2.getFilialByIdFilialOrigem().getSgFilial());
					if (valor == 0){
						valor = ds1.getNrDoctoServico().compareTo(ds2.getNrDoctoServico());
					}
				}
				return valor;
			}
		});
	}

	private String montarAssunto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional) {
		StringBuilder assunto = new StringBuilder(configuracoesFacade.getMensagem("preAlertaEmbarqueCargaAerea"));
		assunto.append(" - ").append(configuracoesFacade.getMensagem("referenteManifesto")).append(" ").append(manifesto.getFilialByIdFilialOrigem().getSgFilial());
		assunto.append(" ").append(formatNrManifestoViagem(manifestoViagemNacional.getNrManifestoOrigem().longValue()));
		return assunto.toString();
	}
	
	private String montarMensagemDestinoManifesto(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, List<DoctoServico> doctoServicoAereoList) {
		return montarMensagem(manifesto, manifestoViagemNacional, doctoServicoAereoList, "M");
	}

	private String montarMensagem(Manifesto manifesto, ManifestoViagemNacional manifestoViagemNacional, List<DoctoServico> doctoServicoAereoList, String destino) {		
		String textoEmailManifestoAereoTabela = "";
		String rodape = "";
		
		if("D".equalsIgnoreCase(destino)){
			textoEmailManifestoAereoTabela = "textoEmailManifestoAereoTabelaFilial"; 
			rodape = "avisoEmailDocumentoAereo";
		}else if("M".equalsIgnoreCase(destino)){
			textoEmailManifestoAereoTabela = "textoEmailManifestoAereoTabela";
			rodape = "avisoEmailManifestoAereo";
		}
		
		List<String> argumentosTextoTabela = new ArrayList<String>();
		argumentosTextoTabela.add(manifesto.getFilialByIdFilialOrigem().getSgFilial());
		argumentosTextoTabela.add(formatNrManifestoViagem(manifestoViagemNacional.getNrManifestoOrigem().longValue()));
		argumentosTextoTabela.add(manifesto.getFilialByIdFilialDestino().getSgFilial());
		
		StringBuilder assunto = new StringBuilder(configuracoesFacade.getMensagem("logisticsManagementSystem"));
		assunto.append(LINE_SEPARATOR);
		assunto.append(configuracoesFacade.getMensagem("preAlertaEmbarqueCargaAerea"));
		assunto.append(LINE_SEPARATOR);
		assunto.append(configuracoesFacade.getMensagem("data")).append(": ").append(JTDateTimeUtils.formatDateYearMonthDayToString(JTDateTimeUtils.getDataAtual()));
		
		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		
		
		assunto.append(configuracoesFacade.getMensagem(textoEmailManifestoAereoTabela, argumentosTextoTabela.toArray()));
		
		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		
		assunto.append(gerarTabelaDocumentos(doctoServicoAereoList));
		
		assunto.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		
		assunto.append(configuracoesFacade.getMensagem(rodape));
		
		return assunto.toString();
	}

	private String gerarTabelaDocumentos(List<DoctoServico> doctoServicoAereoList) {		
		Integer colunaDocumento = 16;
		Integer colunaOtd = 10;
		Integer colunaVolume = 8;
		Integer colunaPeso = 10;
		Integer colunaServico = 8;
		
		for (DoctoServico doctoServico : doctoServicoAereoList) {
			if(doctoServico.getPsReal() != null && doctoServico.getPsReal().toString().length() > colunaPeso){
				colunaPeso = doctoServico.getPsReal().toString().length();
			}
			if(doctoServico.getServico() !=null && doctoServico.getServico().getDsServico().toString().length() > colunaServico){
				colunaServico = doctoServico.getServico().getDsServico().toString().length();
			}
		}
		
		String texto;
		StringBuilder tabela = new StringBuilder();
		
		texto = configuracoesFacade.getMensagem("documento") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaDocumento));
		
		texto = configuracoesFacade.getMensagem("dpe") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaOtd));
		
		texto = configuracoesFacade.getMensagem("volumes") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaVolume));
		
		texto = configuracoesFacade.getMensagem("pesoKG") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaPeso));
		
		texto = configuracoesFacade.getMensagem("servico") + ":";
		tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaServico));
		
		tabela.append(LINE_SEPARATOR);
		
		for (DoctoServico doctoServico : doctoServicoAereoList) {
			
			texto = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + formatNrDoctoServico(doctoServico.getNrDoctoServico()) 
					+ " " + doctoServico.getFilialByIdFilialDestino().getSgFilial() ;
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaDocumento));
			
			texto = JTDateTimeUtils.formatDateYearMonthDayToString( doctoServico.getDtPrevEntrega() );
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaOtd));
			
			texto = alinharDireita( doctoServico.getQtVolumes().toString(), colunaVolume );
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaVolume));
			
			texto = alinharDireita( FormatUtils.formatDecimal("##,###.000", doctoServico.getPsReal(), true), colunaPeso );
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaPeso));
			
			texto = doctoServico.getServico().getDsServico().toString();
			tabela.append(texto).append(tabulacoesPorColuna(texto.length(), colunaServico));
			
			tabela.append(LINE_SEPARATOR);

		}
		
		return tabela.toString();
	}

	private String alinharDireita(String formatQtdVolumes, Integer colunaVolume) {
		String texto = formatQtdVolumes;
		if(formatQtdVolumes.length() < colunaVolume){
			for (int i = 0; i < colunaVolume - formatQtdVolumes.length(); i++) {
				texto = " " + texto;
			}
		}
		return texto;
	}

	private Object tabulacoesPorColuna(Integer colunaAtual, Integer maxColuna) {
		Integer paradaTab = (maxColuna + 8) / 8 * 8;
		Integer quantidadeTabs = (paradaTab/8) - (colunaAtual/8) ;
		
		StringBuilder tabs = new StringBuilder();
		for (int i = 0; i < quantidadeTabs; i++) {
			tabs.append("\t");
		}
		return tabs.toString();
	}

	private String formatNrManifestoViagem(Long nrManifestoOrigem) {
		DecimalFormat df = new DecimalFormat("00000000");
		return df.format(nrManifestoOrigem);
	}
	
	private String formatNrDoctoServico(Long nrDoctoServico) {
		DecimalFormat df = new DecimalFormat("00000000");
		return df.format(nrDoctoServico);
	}
	
	/**
	 * @return Returns the dao.
	 */
	public SaidaChegadaDAO getDao() {
		return dao;
	}

	/**
	 * @param dao The dao to set.
	 */
	public void setDao(SaidaChegadaDAO dao) {
		this.dao = dao;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}

	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
}

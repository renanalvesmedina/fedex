package com.mercurio.lms.expedicao.model.service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.CartaCorrecaoCampo;
import com.mercurio.lms.expedicao.model.CartaCorrecaoEletronica;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.TBDatabaseInputCTE;
import com.mercurio.lms.expedicao.model.dao.CartaCorrecaoEletronicaDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

@Assynchronous
public class CartaCorrecaoEletronicaService extends CrudService<CartaCorrecaoEletronica, Long> {

	private static final String VERSAO_XML_CTE = "Vers�o_XML_CTe";
	private static final String AMBIENTE_CTE = "AMBIENTE_CTE";
	private static final String CONDICAO_USO_CTE = "TEXTO_CONDICAO_USO_CCE";
	
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private ConfiguracoesFacade configuracoesFacade;
	private UnidadeFederativaService unidadeFederativaService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private IntegracaoJmsService integracaoJmsService;
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param dao
	 *            Inst�ncia do DAO
	 */
	public void setCartaCorrecaoEletronicaDAO(CartaCorrecaoEletronicaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO
	 */
	private CartaCorrecaoEletronicaDAO getCartaCorrecaoEletronicaDAO() {
		return (CartaCorrecaoEletronicaDAO) getDao();
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	/**
	 * Gera conte�do para grid Hist�rico de Atualiza��es na aba Carta de
	 * Corre��o Eletr�nica da p�gina Monitoramento de Documentos Eletr�nicos.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Mapa de valores para grid de Hist�rico de Altera��es
	 */
	public List<TypedFlatMap> findHistoricoByIdDoctoServico(Long idDoctoServico) {
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		for (CartaCorrecaoEletronica cce : dao.findHistoricoCCEByIdDoctoServico(idDoctoServico)) {
			Long nrCCE = cce.getNrCartaCorrecaoEletronica();
			DateTime dhEvento = cce.getDhEmissao();
			String sgCampo = CartaCorrecaoCampo.findByGrupoTag(
					cce.getIdTagGrupo(), cce.getIdTagCampo()).getChave();
			String sgValorAntigo = cce.getDsConteudoDocto();
			String sgNovoValor = cce.getDsConteudoCarta();
			String tpSituacaoCartaCorrecao = cce.getTpSituacaoCartaCorrecao();
			
			TypedFlatMap map = new TypedFlatMap();
			map.put("nrCCE", nrCCE);
			map.put("dhEvento", JTDateTimeUtils.formatDateTimeToString(dhEvento));
			map.put("sgCampo", configuracoesFacade.getMensagem(sgCampo));
			map.put("sgValorAntigo", sgValorAntigo);
			map.put("sgNovoValor", sgNovoValor);
			map.put("tpSituacaoCartaCorrecao", configuracoesFacade.getDomainValue(
					"DM_SITUACAO_CARTA_CORRECAO", tpSituacaoCartaCorrecao).getDescription());
			list.add(map);
		}
		
		return list;
	}
	
	/**
	 * Gera conte�do de Cartas de Corre��o Eletr�nica aprovadas para determinado
	 * documento de servi�o.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Mapa de valores com Cartas de Corre��o aprovadas
	 */
	public List<TypedFlatMap> findAprovadasByIdDoctoServico(Long idDoctoServico) {
		List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
		
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		
		for (CartaCorrecaoEletronica cce : dao.findAprovadasByIdDoctoServico(idDoctoServico)) {
			String sgCampo = CartaCorrecaoCampo.findByGrupoTag(
					cce.getIdTagGrupo(), cce.getIdTagCampo()).getChave();
			String sgNovoValor = cce.getDsConteudoCarta();
			Long nrProtocolo = cce.getNrProtocolo();
			
			TypedFlatMap map = new TypedFlatMap();
			map.put("sgCampo", configuracoesFacade.getMensagem(sgCampo));
			map.put("sgNovoValor", sgNovoValor);
			map.put("nrProtocolo", nrProtocolo);
			list.add(map);
		}
		
		return list;
	}

	public Long validateLimiteCCEByIdDoctoServico(Long idDoctoServico) {
		return getCartaCorrecaoEletronicaDAO().validateLimiteCCEByIdDoctoServico(idDoctoServico);
	}
	
	/**
	 * Ap�s inclus�o de todos os registros alterados, gerar o XML com os dados
	 * da carta de corre��o de acordo com a planilha "De-Para CTe" + conte�do do
	 * par�metro da filial do usu�rio logado "Vers�o_XML_CTe", aba
	 * "Carta de Corre��o", este XML dever� ser gravado na tabela
	 * TBDATABASEINPUT_CTE.
	 * 
	 * @param monitoramentoDocEletronico
	 *            Monitoramento de documentos eletr�nicos
	 */
	public void executeGerarXMLCartaCorrecao(TypedFlatMap map) {
		Long idMonitoramentoDocEletronico = (Long) map.get("idMonitoramentoDocEletronico");
		MonitoramentoDocEletronico monitoramentoDocEletronico =
				monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronico);
		
		String xml = gerarXMLCartaCorrecao(monitoramentoDocEletronico);
		
		TBDatabaseInputCTE tbDatabaseInput =
				integracaoNDDigitalService.generateIntegracaoCCE(monitoramentoDocEletronico, xml);
		monitoramentoDocEletronico.setIdEnvioDocEletronicoA(tbDatabaseInput.getId());
		monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
	}
	
	private String gerarXMLCartaCorrecao(MonitoramentoDocEletronico monitoramentoDocEletronico) {
		DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
		Long idDoctoServico = doctoServico.getIdDoctoServico();
		Filial filialOrigem = doctoServico.getFilialByIdFilialOrigem();
		Long idFilialOrigem = filialOrigem.getIdFilial();
		
		String nrVersaoLayout = ((String) configuracoesFacade.getValorParametro(idFilialOrigem, VERSAO_XML_CTE)).replaceAll("[^.0-9]", "");
		String nrChave = monitoramentoDocEletronico.getNrChave();
		String nrIbge = unidadeFederativaService.findByIdPessoa(idFilialOrigem).getNrIbge().toString();
		String nrIdentificacao = filialOrigem.getPessoa().getNrIdentificacao();
		String nrCCE = getCartaCorrecaoEletronicaDAO().findMaxNrCCEByIdDoctoServico(idDoctoServico).toString();
		String dsCondUso = configuracoesFacade.getMensagem(CONDICAO_USO_CTE);
		
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		List<CartaCorrecaoEletronica> list = dao.findAutorizadosEnviadosByIdDoctoServico(idDoctoServico);
		
		return gerarXMLCartaCorrecao(nrVersaoLayout, nrChave, nrIbge, nrIdentificacao, nrCCE, dsCondUso, list);
	}
	
	/**
	 * Gera String com documento XML conforme especifica��o.
	 * @see <a href="https://nt-swdep02/svn/lms-docs/trunk/Projetos/LMS/Especifica%C3%A7%C3%B5es%20t%C3%A9cnicas/04_Expedi%C3%A7%C3%A3o/De-Para%20CTe%202.00.xlsx">De-Para CTe 2.00</a>
	 * 
	 * @param nrVersaoLayout
	 *            Vers�o do leiaute
	 * @param nrChave
	 *            Chave de acesso do CT-e
	 * @param nrIbge
	 *            C�digo do �rg�o de recep��o do evento
	 * @param nrIdentificacao
	 *            CNPJ do autor do evento
	 * @param nrCCE
	 *            Sequencial do evento
	 * @param list
	 *            Informa��es de corre��o
	 * @return XML baseado na tabela CARTA_CORRECAO_ELETRONICA
	 */
	private String gerarXMLCartaCorrecao(
			String nrVersaoLayout,
			String nrChave,
			String nrIbge,
			String nrIdentificacao,
			String nrCCE,
			String dsCondUso,
			List<CartaCorrecaoEletronica> list) {

		try {
			/*
			 * Gera elementos para documento XML
			 */
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			Element eventoCTe = document.createElement("eventoCTe");
			Element infEvento = document.createElement("infEvento");
			Element cOrgao = document.createElement("cOrgao");
			Element tpAmb = document.createElement("tpAmb");
			Element cnpj = document.createElement("CNPJ");
			Element chCTe = document.createElement("chCTe");
			Element dhEvento = document.createElement("dhEvento");
			Element tpEvento = document.createElement("tpEvento");
			Element nSeqEvento = document.createElement("nSeqEvento");
			Element detEvento = document.createElement("detEvento");
			Element evCCeCTe = document.createElement("evCCeCTe");
			Element descEvento = document.createElement("descEvento");
			Element xCondUso = document.createElement("xCondUso");
			
			/*
			 * Estrutura hierarquia do documento XML
			 */
			document.appendChild(eventoCTe);
			eventoCTe.appendChild(infEvento);
			infEvento.appendChild(cOrgao);
			infEvento.appendChild(tpAmb);
			infEvento.appendChild(cnpj);
			infEvento.appendChild(chCTe);
			infEvento.appendChild(dhEvento);
			infEvento.appendChild(tpEvento);
			infEvento.appendChild(nSeqEvento);
			infEvento.appendChild(detEvento);
			detEvento.appendChild(evCCeCTe);
			evCCeCTe.appendChild(descEvento);
			
			/*
			 * Preenche atributos e conte�do do documento XML
			 */
			eventoCTe.setAttribute("versao", nrVersaoLayout);
			eventoCTe.setAttribute("xmlns", "http://www.portalfiscal.inf.br/cte");
			infEvento.setAttribute("Id", "ID110110" + nrChave + StringUtils.leftPad(nrCCE, ("4.00".equals(nrVersaoLayout) ? 3 : 2), "0"));
			cOrgao.setTextContent(nrIbge);
			tpAmb.setTextContent(configuracoesFacade.getValorParametro(AMBIENTE_CTE).toString());
			cnpj.setTextContent(nrIdentificacao);
			chCTe.setTextContent(nrChave);
			dhEvento.setTextContent(JTFormatUtils.format(JTDateTimeUtils.getDataHoraAtual(), "yyyy'-'MM'-'dd'T'HH':'mm':'ssZZ"));
			tpEvento.setTextContent("110110");
			nSeqEvento.setTextContent(nrCCE);
			detEvento.setAttribute("versaoEvento", nrVersaoLayout);
			descEvento.setTextContent("Carta de Correcao");
			evCCeCTe.setAttribute("xmlns", "http://www.portalfiscal.inf.br/cte");
			xCondUso.setTextContent(dsCondUso);
			
			/*
			 * Processa Cartas de Corre��o gerando corpo do documento
			 */
			for (CartaCorrecaoEletronica cce : list) {
				Element infCorrecao = document.createElement("infCorrecao");
				Element grupoAlterado = document.createElement("grupoAlterado");
				Element campoAlterado = document.createElement("campoAlterado");
				Element valorAlterado = document.createElement("valorAlterado");
				
				evCCeCTe.appendChild(infCorrecao);
				infCorrecao.appendChild(grupoAlterado);
				infCorrecao.appendChild(campoAlterado);
				infCorrecao.appendChild(valorAlterado);
				
				grupoAlterado.setTextContent(cce.getIdTagGrupo());
				campoAlterado.setTextContent(cce.getIdTagCampo());
				valorAlterado.setTextContent(cce.getDsConteudoCarta());
			}
			
			evCCeCTe.appendChild(xCondUso);
			
			/*
			 * Transforma��o final do documento XML produzido
			 */
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			DOMSource source = new DOMSource(document);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			
			return writer.toString();
			
		} catch (ParserConfigurationException e) {
			throw new InfrastructureException(e);
		} catch (TransformerConfigurationException e) {
			throw new InfrastructureException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new InfrastructureException(e);
		} catch (TransformerException e) {
			throw new InfrastructureException(e);
		}
	}

	/**
	 * Atualizar carta de corre��o rejeitada buscando na tabela
	 * CARTA_CORRECAO_ELETRONICA pelo ID_DOCTO_SERVICO e
	 * TP_SITUACAO_CARTA_CORRECAO = "E", atualizando os seguintes campos:
	 *  - TP_SITUACAO_CARTA_CORRECAO:   "R"
	 *  - NR_PROTOCOLO:                 nProt
	 *  - DS_OBSERVACAO:                xMotivo
	 *  - NR_CARTA_CORRECAO_ELETRONICA: null
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @param dsObservacao
	 *            Motivo da rejei��o
	 */
	public void updateRejeitadaByIdDoctoServico(
			Long idDoctoServico, String dsObservacao) {
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		List<CartaCorrecaoEletronica> list = dao.findEnviadosByIdDoctoServico(idDoctoServico);
		for (CartaCorrecaoEletronica cce : list) {
			cce.setTpSituacaoCartaCorrecao("R");
			cce.setDsObservacao(dsObservacao);
			cce.setNrCartaCorrecaoEletronica(0L);
			dao.store(cce);
		}
	}

	/**
	 * Atualizar carta de corre��o autorizada buscando na tabela
	 * CARTA_CORRECAO_ELETRONICA pelo ID_DOCTO_SERVICO e
	 * TP_SITUACAO_CARTA_CORRECAO = "E", atualizando os seguintes campos:
	 *  - TP_SITUACAO_CARTA_CORRECAO:   "A"
	 *  - NR_PROTOCOLO:                 nProt
	 *  - DS_OBSERVACAO:                xMotivo
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @param nrProtocolo
	 *            N�mero do protocolo
	 * @param dsObservacao
	 *            Motivo da rejei��o
	 */
	public void updateAutorizadaByIdDoctoServico(
			Long idDoctoServico, Long nrProtocolo, String dsObservacao) {
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		List<CartaCorrecaoEletronica> list = dao.findEnviadosByIdDoctoServico(idDoctoServico);
		for (CartaCorrecaoEletronica cce : list) {
			cce.setTpSituacaoCartaCorrecao("A");
			cce.setNrProtocolo(nrProtocolo);
			cce.setDsObservacao(dsObservacao);
			dao.store(cce);
		}
	}

	/**
	 * Verifica conclus�o do processamento de Cartas de Corre��o Eletr�nicas,
	 * retornando o motivo caso rejeitada, uma string vazia caso aprovada e null
	 * caso processamento n�o conclu�do.
	 * 
	 * @param idDoctoServico
	 *            Identificador do documento de servi�o
	 * @return Motivo da rejei��o, string vazia se aprovada ou null se
	 *         processamento n�o conclu�do
	 */
	public String executeProcessarIntegracaoCCE(Long idDoctoServico) {
		CartaCorrecaoEletronicaDAO dao = getCartaCorrecaoEletronicaDAO();
		List<CartaCorrecaoEletronica> list = dao.findByIdDoctoServico(idDoctoServico);
		CartaCorrecaoEletronica cce = list.get(list.size() - 1);
		String tpSituacao = cce.getTpSituacaoCartaCorrecao();
		if ("R".equals(tpSituacao)) {
			// Rejeitada -- retorna motivo
			return cce.getDsObservacao();
		} else if ("A".equals(tpSituacao)) {
			// Aprovada -- retorna string vazia
			return "";
		} else {
			// Processamento n�o conclu�do
			return null;
		}
	}

	public void executeEnviarEmailCCE(TypedFlatMap map, File pdf) {
		Long idMonitoramentoDocEletronico = (Long) map.get("idMonitoramentoDocEletronico");
		MonitoramentoDocEletronico monitoramentoDocEletronico =
				monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronico);
		DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
		
		String xml = gerarXMLCartaCorrecao(monitoramentoDocEletronico);
		
		String to = monitoramentoDocEletronico.getDsEnvioEmail();
		if (StringUtils.isNotBlank(to)) {
			if (to.trim().endsWith(";")) {
				to = to.trim();
				to = to.substring(0, to.length() - 1);
			}
			
			String from = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			String cte = doctoServico.getFilialByIdFilialOrigem().getSgFilial()
					+ " " + doctoServico.getNrDoctoServico();
			String subject = configuracoesFacade.getMensagem(
					"assuntoEmailCartaCorrecaoEletronica", new Object[] { cte });
			String text = configuracoesFacade.getMensagem(
					"textoEmailCartaCorrecaoEletronica", new Object[] { cte });
			
			Map<String, File> attachments = new HashMap<String, File>();
			try {
				String filename = "CTe " + cte + ".xml";
				File file = File.createTempFile(filename, ".xml");
				
				OutputStream out = null;
				out = new FileOutputStream(file);
				IOUtils.write(xml, out);
				out.close();

				attachments.put(filename, file);
			} catch (IOException e) {
				throw new InfrastructureException(e);
			}
			if (pdf != null) {
				attachments.put("CTe " + cte + ".pdf", pdf);
			}
			
			sendMessage(from, to, subject, text, attachments);
		}
	}
	
	/**
	 * Envia email referente Carta de Corre��o Eletr�nica.
	 * 
	 * @param from
	 *            remetente
	 * @param to
	 *            destinat�rio
	 * @param subject
	 *            assunto
	 * @param text
	 *            texto
	 * @param attachments
	 *            anexos
	 */
	private void sendMessage(String from, String to, String subject,
	String text, Map<String, File> attachments) {
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
		
		for (Map.Entry<String,File> entry : attachments.entrySet()) {
		    String filename = entry.getKey();
		    File file = entry.getValue();
		    
			MailAttachment mailAttachment = new MailAttachment();
			mailAttachment.setName(filename);
			mailAttachment.setData(FileUtils.readFile(file));
			mailAttachments.add(mailAttachment);
		}
		
		Mail mail = createMail(to, from, subject, text, mailAttachments);
	
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
}


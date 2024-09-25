package com.mercurio.lms.expedicao.model.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.mercurio.lms.util.session.SessionUtils;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.joda.time.DateTime;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregaPorNotaFiscalService;
import com.mercurio.lms.expedicao.model.CodigoMunicipioFilial;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.TBInputDocuments;
import com.mercurio.lms.expedicao.model.TBIntegration;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalEletronicaDAO;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.reports.NFeJasperReportFiller;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.NFEUtils;
import com.mercurio.lms.expedicao.util.XMLUtils;
import com.mercurio.lms.expedicao.util.nfe.NfeConverterUtils;
import com.mercurio.lms.expedicao.util.nfe.NfeXmlCancelamentoWrapper;
import com.mercurio.lms.expedicao.util.nfe.NfeXmlEnvioWrapper;
import com.mercurio.lms.expedicao.util.nfe.converter.CancelamentoNseConverter;
import com.mercurio.lms.expedicao.util.nfe.converter.CancelamentoNteConverter;
import com.mercurio.lms.expedicao.util.nfe.converter.RpsConverterFactory;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.layoutNfse.model.cancelamento.Cancelamento;
import com.mercurio.lms.layoutNfse.model.rps.Rps;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.ValidateUtils;

/**
 * Classe responsável por gerar as informações para a NDDigital
 * 
 * @author lucianos
 * 
 */
public class NotaFiscalEletronicaService {
	
    private static final String NFSE_ISS_PRST_OUTLOC = "NFSE_ISS_PRST_OUTLOC";
	private static final String NFSE_RET_SPECIAL_ISS = "NFSE_RET_SPECIAL_ISS";
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	
	protected Logger log = LogManager.getLogger(NotaFiscalEletronicaService.class);

	private ConfiguracoesFacade configuracoesFacade;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ParametroGeralService parametroGeralService;
	private NotaFiscalEletronicaDAO notaFiscalEletronicaDAO;
    private NotaFiscalServicoService notaFiscalServicoService;
    private EmitirDocumentoService emitirDocumentoService;
    private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private FilialService filialService;
	private NotaFiscalEletronicaRetornoService notaFiscalEletronicaRetornoService;
	private DoctoServicoService doctoServicoService;
	private CodigoMunicipioFilialService codigoMunicipioFilialService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private NFEConjugadaService nfeConjugadaService;
	private IntegracaoJmsService integracaoJmsService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;
	
	public NotaFiscalEdi findNfeByNrChave(String chaveNFe) {
		try {
			TypedFlatMap mapaCampos = notaFiscalEletronicaDAO.findNfeByNrChave(chaveNFe);
			String xmlString = notaFiscalEletronicaDAO.findNfeXMLFromEDIToString(chaveNFe);
			
			if(mapaCampos == null || xmlString == null){
				return null;
			}else{
				InputStream xmlInputStream = new StringInputStream(xmlString, "UTF-8");
				return populateNotaFiscalEdi(xmlInputStream, mapaCampos);
			}
		} catch (SQLException e) {
			throw new InfrastructureException(e);
		}
	}
	
	private NotaFiscalEdi populateNotaFiscalEdi(InputStream xmlInputStream, TypedFlatMap mapaCampos) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlInputStream);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();

			// NF
			notaFiscalEdi.setNrNotaFiscal(XMLUtils.getIntegerElementValue(doc, xpath, mapaCampos.getString("NF_NRO")));
			notaFiscalEdi.setSerieNf(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("NF_SERIE")));
			notaFiscalEdi.setDataEmissaoNf(XMLUtils.getDatelElementValue(doc, xpath, mapaCampos.getString("NF_EMISSAO")));
			notaFiscalEdi.setNatureza(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("NF_NATUREZA")));

			// EMISSOR
			notaFiscalEdi.setCnpjReme(XMLUtils.getLongElementValue(doc, xpath, mapaCampos.getString("EMI_CNPJ")));
			notaFiscalEdi.setIeReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_IE")));
			notaFiscalEdi.setNomeReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_NOME")));
			notaFiscalEdi.setEnderecoReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_LOGRADOURO")));
			notaFiscalEdi.setBairroReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_BAIRRO")));
			notaFiscalEdi.setMunicipioReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_MUNICIPIO")));
			notaFiscalEdi.setUfReme(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("EMI_UF")));
			notaFiscalEdi.setCepEnderReme(XMLUtils.getIntegerElementValue(doc, xpath, mapaCampos.getString("EMI_CEP")));

			// DESTINATARIO
			notaFiscalEdi.setCnpjDest(XMLUtils.getLongElementValue(doc, xpath, mapaCampos.getString("DEST_CNPJ")));
			notaFiscalEdi.setIeDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_IE")));
			notaFiscalEdi.setNomeDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_NOME")));
			notaFiscalEdi.setEnderecoDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_LOGRADOURO")));
			notaFiscalEdi.setBairroDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_BAIRRO")));
			notaFiscalEdi.setMunicipioDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_MUNICIPIO")));
			notaFiscalEdi.setUfDest(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("DEST_UF")));
			notaFiscalEdi.setCepEnderDest(XMLUtils.getIntegerElementValue(doc, xpath, mapaCampos.getString("DEST_CEP")));

			// TRANSPORTADORA
			notaFiscalEdi.setCnpjTomador(XMLUtils.getLongElementValue(doc, xpath, mapaCampos.getString("TRASP_CNPJ")));
			notaFiscalEdi.setIeTomador(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("TRASP_IE")));
			notaFiscalEdi.setNomeTomador(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("TRASP_NOME")));
			notaFiscalEdi.setEnderecoTomador(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("TRASP_ENDERECO")));
			notaFiscalEdi.setUfTomador(XMLUtils.getStringElementValue(doc, xpath, mapaCampos.getString("TRASP_UF")));

			// VOLUMES
			notaFiscalEdi.setQtdeVolumes(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("VOL_QTDE")));
			notaFiscalEdi.setPesoReal(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("VOL_PESO_BRUTO")));

			// TOTAIS
			notaFiscalEdi.setVlrBaseCalcIcms(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("TOT_BASE_ICMS")));
			notaFiscalEdi.setVlrIcms(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("TOT_ICMS")));
			notaFiscalEdi.setOutrosValores(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("TOT_OUTROS")));
			notaFiscalEdi.setVlrTotalMerc(XMLUtils.getBigDecimalElementValue(doc, xpath, mapaCampos.getString("TOT_NF")));

			return notaFiscalEdi;
		} catch (XPathExpressionException e) {
			throw new InfrastructureException(e);
		} catch (ParserConfigurationException e) {
			throw new InfrastructureException(e);
		} catch (SAXException e) {
			throw new InfrastructureException(e);
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * 
	 * @param chaveNFe
	 * @return
	 */
	public List<NotaFiscalEdiItem> findNfeItensByNrChave(String chaveNFe) {
		List<NotaFiscalEdiItem> retorno = new ArrayList<NotaFiscalEdiItem>();

		Object xmlId = notaFiscalEletronicaDAO.findNfeItensByNrChave(chaveNFe);

		if (xmlId != null) {
			String xml = notaFiscalEletronicaDAO.findNfeItensByID(xmlId);

			if (xml != null) {
				StringReader sr = new StringReader(xml);
				List<TypedFlatMap> itensMap = new ArrayList<TypedFlatMap>();
				String[] campos = new String[] { "nItem", "xProd", "cProd", "cEAN", "NCM", "CFOP", "uCom", "qCom", "vUnCom", "vProd" };

				try {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = dbf.newDocumentBuilder();
					Document doc = docBuilder.parse(new InputSource(sr));

					NodeList nList = doc.getElementsByTagName("det");

					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							TypedFlatMap map = new TypedFlatMap();

							Element element = (Element) nNode;
							map.put(campos[0], element.getAttribute(campos[0]));

							for (int i = 1; i < campos.length; i++) {
								if(element.getElementsByTagName(campos[i]).item(0) != null){
									map.put(campos[i], element.getElementsByTagName(campos[i]).item(0).getTextContent());
								} 
							}
							
							itensMap.add(map);
						}
					}

				} catch (Exception e) {
					log.error(e);
					throw new InfrastructureException(e);
				} finally {
					if (sr != null) {
						sr.close();
					}
				}

				for (TypedFlatMap itemNfe : itensMap) {
					if (!itemNfe.isEmpty()) {
						NotaFiscalEdiItem notaFiscalEdiItem = new NotaFiscalEdiItem();
						notaFiscalEdiItem.setNumeroItem(itemNfe.getInteger(campos[0]));
						notaFiscalEdiItem.setDescricaoItem(itemNfe.getString(campos[1]));
						notaFiscalEdiItem.setCodItem(itemNfe.getString(campos[2]));
						notaFiscalEdiItem.setEanItem(itemNfe.getLong(campos[3]));
						notaFiscalEdiItem.setNcmItem(itemNfe.getLong(campos[4]));
						notaFiscalEdiItem.setCfopItem(itemNfe.getString(campos[5]));
						notaFiscalEdiItem.setUnidadeItem(itemNfe.getString(campos[6]));
						notaFiscalEdiItem.setQtdeItem(itemNfe.getBigDecimal(campos[7]));
						notaFiscalEdiItem.setVlUnidadeItem(itemNfe.getBigDecimal(campos[8]));
						notaFiscalEdiItem.setVlTotalItem(itemNfe.getBigDecimal(campos[9]));

						retorno.add(notaFiscalEdiItem);
					}
				}
			}
		}

		return retorno;
	}
	
	
    public String storeNotaFiscalServicoEletronica(NotaFiscalServico notaFiscalServico) {
    	//Forcar a atualizacao dos dados na base de dados 
    	notaFiscalEletronicaDAO.getAdsmHibernateTemplate().flush();
    	
    	//Remover da sessao para que o find busque os atributos da classe no banco
    	notaFiscalEletronicaDAO.getAdsmHibernateTemplate().evict(notaFiscalServico);
    	
		notaFiscalServico = notaFiscalServicoService.findById(notaFiscalServico.getIdDoctoServico());
		    	
		if(notaFiscalServico.getFilial() != null && notaFiscalServico.getFilial().getIdFilial() != null) {
			return storeNotaFiscalEletronica(notaFiscalServico, notaFiscalServico.getFilial().getIdFilial());
		}
    	return storeNotaFiscalEletronica(notaFiscalServico);
    }
    
    public String storeNotaFiscalTransporteEletronica(Conhecimento conhecimento) {
    	return storeNotaFiscalEletronica(conhecimento);
    }
    
	private String storeNotaFiscalEletronica(DoctoServico doctoServico) {
		return storeNotaFiscalEletronica(doctoServico, null);
	}

    
	/**
	 * Realiza o store da Nota Fisca Eletronica na tabela da NDDigital. Após o store, o software da NDD envia os dados da 
	 * nota a respectiva prefeitura
	 *  
	 * @param doctoServico
	 * @throws Exception 
	 * @throws IOException 
	 */
	private String storeNotaFiscalEletronica(DoctoServico doctoServico, Long idFilialRps ) {
		Long idFilialOrigem = doctoServico.getFilialByIdFilialOrigem().getIdFilial();
		
		if(nfeConjugadaService.isAtivaNfeConjugada(idFilialOrigem)){			
			return storeNotaFiscalEletronicaConjugada(doctoServico, createNrFiscalRps(idFilialOrigem));
		}
		
	    return storeNotaFiscalEletronicaNormal(doctoServico, createNrFiscalRps(idFilialRps));
	}

	private String storeNotaFiscalEletronicaNormal(DoctoServico doctoServico, Long nrFiscalRps) {
		TBInputDocuments notaFiscalEletronica = new TBInputDocuments();
		notaFiscalEletronica.setJobKey(Long.valueOf(doctoServico.getFilialByIdFilialOrigem().getCodFilial()));

		//DOCSTATUS = '0' ou '1' se o parâmetro de filial 'PREF_WEBSERVICE' existir e estiver preenchido com 'N'
		if (isPrefeituraWebserviceNFSe(doctoServico.getFilialByIdFilialOrigem().getIdFilial())) {
			storeObsDoctoServico(doctoServico, nrFiscalRps);
			notaFiscalEletronica.setDocStatus(0);
		} else {
			notaFiscalEletronica.setDocStatus(1);
		}
		
		notaFiscalEletronica.setDocKind(1);
		notaFiscalEletronica.setInsertDate(new DateTime());
		String xml = createXml(doctoServico, nrFiscalRps);
		notaFiscalEletronica.setDocData(xml.getBytes(Charset.forName("UTF-8")));
		
		notaFiscalEletronicaDAO.createTBInputDocuments(notaFiscalEletronica);
			
		MonitoramentoDocEletronico monitoramentoDocEletronico = gerarMonitoramentoDocEletronico(doctoServico, notaFiscalEletronica, nrFiscalRps);
		
		Long idMonitoramentoDocEletronico = (Long) monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
		
        String[] destinatarios = NFEUtils.getDestinatarios(doctoServico);
        if (destinatarios.length > 0) {
            
            final String nomeAnexo = "RPS" + doctoServico.getIdDoctoServico() + ".PDF";
            try {
                File anexo = gerarAnexo2PDF(doctoServico, monitoramentoDocEletronico, nomeAnexo);
                sendMail(doctoServico, xml, destinatarios, nomeAnexo, anexo);
                
                monitoramentoDocEletronico.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
                monitoramentoDocEletronico.setDsEnvioEmail(StringUtils.join(destinatarios,";"));
            } catch (Exception e) {
                log.error("Erro ao enviar email da nota fiscal eletrônica ", e);
            }
            
        }
        
		
		return idMonitoramentoDocEletronico.toString();
	}

	/**
	 * Realiza as operações necessárias para a nota fiscal conjugada.
	 * 
	 * @param doctoServico
	 * @param nrFiscalRps
	 * 
	 * @return String
	 */
	private String storeNotaFiscalEletronicaConjugada(DoctoServico doctoServico, Long nrFiscalRps) {
		Short nrCfop = nfeConjugadaService.findNrCFOP(doctoServico.getIdDoctoServico());
		
		doctoServico.setNrCfop(nrCfop); 
		
		storeObsDoctoServico(doctoServico, nrFiscalRps);
		
		return nfeConjugadaService.storeEnvio(doctoServico, nrFiscalRps, nrCfop);
	}
	
	private Long createNrFiscalRps(Long idFilialRps){
		return idFilialRps == null ? emitirDocumentoService.generateProximoNumeroRPS() : emitirDocumentoService.generateProximoNumeroRPS(idFilialRps);
	}
	
	private boolean isPrefeituraWebserviceNFSe(Long idFilial) {
		Object valorParametro = configuracoesFacade.getValorParametro(idFilial, "PREF_WEBSERVICE");
		//se o parâmetro de filial “PREF_WEBSERVICE” existir e estiver preenchido com “N”
		if (valorParametro != null && "N".equals(valorParametro)) {
			return false;
		}
		return true;
	}
	
	public void storeObsDoctoServico(DoctoServico doctoServico,
			Long nrFiscalRps) {
		ObservacaoDoctoServico obs = new ObservacaoDoctoServico();
		obs.setDoctoServico(doctoServico);
		obs.setDsObservacaoDoctoServico(String.format("Número fiscal do RPS: %s",StringUtils.leftPad(nrFiscalRps.toString(), 9, "0")));
		obs.setBlPrioridade(true);
		doctoServico.addObservacaoDoctoServico(obs);
		observacaoDoctoServicoService.store(obs);
	}

	private void sendMail(DoctoServico doctoServico, String xml, final String[] destinatarios, final String nomeAnexo, final File anexo) {
	    final String remetenteLms = (parametroGeralService.findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
	       
	    final String assunto = (parametroGeralService.findByNomeParametro("ASSUNTO_EMAIL_RPS", false)).getDsConteudo();
        
        final File anexoFinal = anexo;

        for (String email : destinatarios) {
            if(!ValidateUtils.validateEmail(email)){
            	throw new BusinessException("ASN-00003");
            }
        }
        
		Mail mail = createMail(StringUtils.join(destinatarios, ";"), remetenteLms, assunto, assunto, anexoFinal, nomeAnexo);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	
	}
	
	private Mail createMail(String strTo, String strFrom, String strSubject, String body, File file, String filename) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		
		MailAttachment mailAttachment = new MailAttachment();
		mailAttachment.setName(filename);
		mailAttachment.setData(FileUtils.readFile(file));
		MailAttachment[] mailAttachmentArr = {mailAttachment};
		mail.setAttachements(mailAttachmentArr);
		
		return mail;
	}

	private File gerarAnexo2PDF(DoctoServico doctoServico, MonitoramentoDocEletronico monitoramentoDocEletronico, String nomeAnexo)
			throws Exception {
		
		List<Map<String, Object>> listNtes = new ArrayList<Map<String, Object>>();
		Map<String, Object> nte = new HashMap<String, Object>();
		if(doctoServico.getIdDoctoServico()!=null) {
			nte.put("idConhecimento", doctoServico.getIdDoctoServico());
		}
		if(monitoramentoDocEletronico.getIdMonitoramentoDocEletronic() !=null){
			nte.put("idMonitoramentoDocEletronic", monitoramentoDocEletronico.getIdMonitoramentoDocEletronic());
		}
		listNtes.add(nte);

		String host = configuracoesFacade.getValorParametro("ADSM_REPORT_HOST").toString();
		JRRemoteReportsRunner reportsRunner = new JRRemoteReportsRunner(host.toString());
		File pdfTemp = null;

		if(doctoServico != null){
			if(doctoServico.getTpDocumentoServico() != null){
				if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(doctoServico.getTpDocumentoServico().getValue())){
					JasperPrint jasperPrint = createNteJasperPrintNteByList(listNtes);
					pdfTemp = reportsRunner.createPdf(jasperPrint, nomeAnexo);

				}else if(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(doctoServico.getTpDocumentoServico().getValue())){
					JasperPrint jasperPrint = createNseJasperPrintNteByList(listNtes);
					pdfTemp = reportsRunner.createPdf(jasperPrint, nomeAnexo);

				}
			}

		}

		return pdfTemp;
    }

    private MonitoramentoDocEletronico gerarMonitoramentoDocEletronico(DoctoServico doctoServico, TBInputDocuments notaFiscalEletronica, Long nrFiscalRps) {
		
		MonitoramentoDocEletronico monitoramentoDocEletronico = new MonitoramentoDocEletronico();
		monitoramentoDocEletronico.setDoctoServico(doctoServico);
		if (isPrefeituraWebserviceNFSe(doctoServico.getFilialByIdFilialOrigem().getIdFilial())) {
			monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("E"));
		} else {
			monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("F"));
		}
		monitoramentoDocEletronico.setDsSerieRps( String.valueOf(parametroGeralService.findConteudoByNomeParametro("SERIE_NFE", false)) );
		monitoramentoDocEletronico.setTpDocumentoRps( String.valueOf(parametroGeralService.findConteudoByNomeParametro("TP_DOCUMENTO_RPS", false)) );
		monitoramentoDocEletronico.setIdEnvioDocEletronicoE(notaFiscalEletronica.getId());
		monitoramentoDocEletronico.setDsDadosDocumento(notaFiscalEletronica.getDocData());
		monitoramentoDocEletronico.setNrFiscalRps(nrFiscalRps);

		return monitoramentoDocEletronico;
	}

    private String createXml(DoctoServico doctoServico, Long nrFiscalRps) {
    	return createXmlDataAtual(doctoServico, nrFiscalRps, false);
    }

	private String createXmlDataAtual(DoctoServico doctoServico, Long nrFiscalRps, Boolean reenviarDataAtual) {
		Filial filialOrigemDoctoServico = doctoServico.getFilialByIdFilialOrigem();
		String versaoNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro(filialOrigemDoctoServico.getIdFilial(), "VERSAO_NFE"),null);
		String regimeTributarioNfe = getRegimeTributarioNfe(doctoServico, filialOrigemDoctoServico);
		String serieNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro("SERIE_NFE"),null);
		String servicoNte = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro( "SERVICO_NTE"),null);
		String bairroPadrao = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro("Bairro_padrao"),null);
		String tipoDocumento = null;
		String munIncidenciaOutro = null;
		String codMunicipioOutros = null;
		
		Municipio municipioServico = getMunicipioServico(doctoServico);
		
		if(municipioServico != null) {
			CodigoMunicipioFilial codigoMunicipioFilial = codigoMunicipioFilialService.findByFilialMunicipio( doctoServico.getFilialByIdFilialOrigem(), municipioServico );
			if (codigoMunicipioFilial != null) {
				munIncidenciaOutro = codigoMunicipioFilial.getDsCodigo();
			}
		}
		
		String naturezaOperacaoNfe = getNaturezaOperacao(doctoServico, municipioServico);
		
		DevedorDocServ devedorDocServ = ConhecimentoUtils.getDevedorDocServ(doctoServico);
		if (devedorDocServ != null) {
			Pessoa pessoa = devedorDocServ.getCliente().getPessoa();
			
			Municipio municipioTomador = pessoa.getEnderecoPessoa().getMunicipio();

			if (municipioTomador != null) {
				tipoDocumento = getTipoDocumento(pessoa, municipioTomador, municipioServico);
				
				CodigoMunicipioFilial codigoMunicipioFilial = codigoMunicipioFilialService.findByFilialMunicipio( doctoServico.getFilialByIdFilialOrigem(), municipioTomador );
				if (codigoMunicipioFilial != null) {
					codMunicipioOutros = codigoMunicipioFilial.getDsCodigo();
				}
			}
			
		}
		
		String codigoCnae = null;
		String codigoTribMunicipio = null;
		if (doctoServico instanceof NotaFiscalServico) {
			codigoCnae = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro( "COD_CNAE_SERVICO"),null);
			codigoTribMunicipio = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro( "COD_TRIB_SERVICO"),null);
		} else if (doctoServico instanceof Conhecimento) {
			codigoCnae = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro( "COD_CNAE_TRANSPORTE"),null);
			codigoTribMunicipio = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro( "COD_TRIB_TRANSPORTE"),null);
		}
		String conteudoNfseGeraVlrIss = (String) conteudoParametroFilialService.findConteudoByNomeParametro(filialOrigemDoctoServico.getIdFilial(), "NFSE_GERA_VLR_ISS", false);
		Boolean nfseGeraVlrIss = "S".equals(conteudoNfseGeraVlrIss) ? true : false;
		
		String conteudoNfseIssPrestOutroLocal = (String) conteudoParametroFilialService.findConteudoByNomeParametro(filialOrigemDoctoServico.getIdFilial(), NFSE_ISS_PRST_OUTLOC, false);
		Boolean nfseIssPrstOutLoc = "S".equalsIgnoreCase(conteudoNfseIssPrestOutroLocal);
		
		String conteudoNfseRetIss = (String) conteudoParametroFilialService.findConteudoByNomeParametro(filialOrigemDoctoServico.getIdFilial(), NFSE_RET_SPECIAL_ISS, false);
		Boolean nfseRetEspIss = "S".equalsIgnoreCase(conteudoNfseRetIss);
		
		Rps rps = RpsConverterFactory.getInstance(versaoNfe, 
				naturezaOperacaoNfe, 
				regimeTributarioNfe,
				serieNfe,
				codigoCnae,
				codigoTribMunicipio,
				servicoNte,
				bairroPadrao,
				doctoServico,
				nrFiscalRps, tipoDocumento,
				munIncidenciaOutro, 
				codMunicipioOutros, 
				nfseGeraVlrIss,
				nfseIssPrstOutLoc,nfseRetEspIss,reenviarDataAtual).convert();
		
		String xml = new NfeXmlEnvioWrapper(rps).generate();
		return xml;
	}
	
	private Municipio getMunicipioServico(DoctoServico doctoServico) {
		Municipio municipioServico = null;
		if (doctoServico instanceof Conhecimento) {
			municipioServico = ((Conhecimento)doctoServico).getMunicipioByIdMunicipioColeta();
		} else if (doctoServico instanceof NotaFiscalServico) {
			municipioServico = ((NotaFiscalServico)doctoServico).getMunicipio();
		}
		return municipioServico;
	}

	private String getTipoDocumento(Pessoa pessoa, Municipio municipioTomador, Municipio municipioServico) {
		String tpPessoa = pessoa.getTpPessoa().getValue();
		if ("F".equals(tpPessoa)) {
			return "1";
		} else if ("J".equals(tpPessoa)) {
			Long idMunicipioServico = municipioServico.getIdMunicipio();
			Long idMunicipioTomador = municipioTomador.getIdMunicipio();
			return idMunicipioServico == idMunicipioTomador ? "2" : "3";
		} 
		return null;
	}

	private String getNaturezaOperacao(DoctoServico doctoServico, Municipio municipioServico) {
		Long idMunicipioFilialOrigem = getIdMunicipioFilialOrigem(doctoServico);
		Long idMunicipioServico = municipioServico.getIdMunicipio();
		return idMunicipioFilialOrigem == idMunicipioServico ? "1" : "2";
	}

	private Long getIdMunicipioFilialOrigem(DoctoServico doctoServico) {
		Long idMunicipioEndereco = null;
		if(doctoServico != null && doctoServico.getFilialByIdFilialOrigem() != null && doctoServico.getFilialByIdFilialOrigem().getPessoa() != null 
				&& doctoServico.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio() != null)
			idMunicipioEndereco = doctoServico.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
		return idMunicipioEndereco;
	}
	
	/**
	 * LMS-5037 - Codigo de identificação do municipio do Tomador de Serviço.
	 * 
	 * @param doctoServico
	 * @param filialSessao
	 * @return
	 */
	private String getRegimeTributarioNfe(DoctoServico doctoServico, Filial filialSessao) {
		String regimeTributarioNfe = null;
		Municipio municipioDocumento = null;
		Filial filialDocumento = null;

		if (doctoServico.getTpDocumentoServico() != null) {
			if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(doctoServico.getTpDocumentoServico().getValue())) {
				municipioDocumento = ((Conhecimento) doctoServico).getMunicipioByIdMunicipioColeta();
				filialDocumento = ((Conhecimento) doctoServico).getFilialOrigem();
			} else if (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(doctoServico.getTpDocumentoServico().getValue())) {
				municipioDocumento = ((NotaFiscalServico) doctoServico).getMunicipio();
				filialDocumento = ((NotaFiscalServico) doctoServico).getFilial();
			}
		}

		Municipio municipioFilial = null;
		if (doctoServico.getFilialByIdFilialOrigem() != null && doctoServico.getFilialByIdFilialOrigem().getPessoa() != null
				&& doctoServico.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa() != null) {
			EnderecoPessoa ep = doctoServico.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa();
			municipioFilial = ep.getMunicipio();
		}

		if (filialDocumento != null) {
		if (municipioDocumento != null && municipioFilial != null && municipioDocumento.getIdMunicipio().equals(municipioFilial.getIdMunicipio())) {
			regimeTributarioNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro(filialDocumento.getIdFilial(), "REGIME_TRIBUTARIO_NF"), null);
		} else {
			regimeTributarioNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro(filialDocumento.getIdFilial(), "REG_TRIB_NFE_OUT_MUN"), null);
		}
		}

		return regimeTributarioNfe;
	}
	
	public TBInputDocuments executeCancelamentoNte(Conhecimento conhecimento, MonitoramentoDocEletronico monitoramentoDocEletronico) {
		String versaoNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial(), "VERSAO_NFE"), null);
		//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.10 /Cancelamento de NFE 
		Cancelamento cancelamento = new CancelamentoNteConverter(conhecimento, monitoramentoDocEletronico, versaoNfe).convert();
		String xml = new NfeXmlCancelamentoWrapper(cancelamento).generate();
		TBInputDocuments tbInputDocuments = generateIntegracaoCancelamentoNfe(monitoramentoDocEletronico, xml);
		
		return tbInputDocuments;
	}
	
	public TBInputDocuments executeCancelamentoNse(NotaFiscalServico nfs, MonitoramentoDocEletronico monitoramentoDocEletronico) {
		String versaoNfe = NfeConverterUtils.objectToString(configuracoesFacade.getValorParametro(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial(), "VERSAO_NFE"), null);
		//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.18 /Cancelamento de NSE 
		Cancelamento cancelamento = new CancelamentoNseConverter(nfs, monitoramentoDocEletronico, versaoNfe).convert();
		String xml = new NfeXmlCancelamentoWrapper(cancelamento).generate();
		
		TBInputDocuments tbInputDocuments = generateIntegracaoCancelamentoNfe(monitoramentoDocEletronico, xml);
		
		return tbInputDocuments;
	}
	
	public TBInputDocuments generateIntegracaoCancelamentoNfe(MonitoramentoDocEletronico monitoramentoDocEletronico, String xml) {
		TBInputDocuments tbInputDocuments = new TBInputDocuments();
		tbInputDocuments.setDocData(xml.getBytes(Charset.forName("UTF-8")));
		tbInputDocuments.setDocKind(2);
		tbInputDocuments.setJobKey(Long.valueOf(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getCodFilial()));
		tbInputDocuments.setDocStatus(0);
		tbInputDocuments.setInsertDate(new DateTime());
		notaFiscalEletronicaDAO.createTBInputDocuments(tbInputDocuments);
		return tbInputDocuments;
	}
	
	public void gerarNotaFiscalEletronica(MonitoramentoDocEletronico monitoramentoDocEletronico, DoctoServico doctoServico, Long proximoNumeroRps, Boolean reenviarDataAtual) {
		if(nfeConjugadaService.isAtivaNfeConjugada(doctoServico.getFilialByIdFilialOrigem().getIdFilial())){
			nfeConjugadaService.storeReenvio(doctoServico, monitoramentoDocEletronico, proximoNumeroRps);
		} else {			
			gerarNotaFiscalEletronicaNormal(monitoramentoDocEletronico, doctoServico, proximoNumeroRps,reenviarDataAtual);		
		}		
	}

	private void gerarNotaFiscalEletronicaNormal(MonitoramentoDocEletronico monitoramentoDocEletronico, DoctoServico doctoServico, Long proximoNumeroRps, Boolean reenviarDataAtual) {
		//LMS-4097 / ET 04.01.01.20 Monitoramento de Documentos Eletrônicos / item 3.23 / Reenviar de NTE-NSE		
		if(proximoNumeroRps != null){
			monitoramentoDocEletronico.setNrFiscalRps(proximoNumeroRps);
		}
		
		String xml = createXmlDataAtual(doctoServico,monitoramentoDocEletronico.getNrFiscalRps(),reenviarDataAtual);
		TBInputDocuments tbInputDocuments = generateIntegracaoEmissaoNfe(monitoramentoDocEletronico, xml);
		monitoramentoDocEletronico.setIdEnvioDocEletronicoE(tbInputDocuments.getId());
		monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_ENVIADO));
		
		if(proximoNumeroRps != null){
			monitoramentoDocEletronico.setDsDadosDocumento(tbInputDocuments.getDocData());
		}
		
		monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
	}	
	
	public void executeRegerarNotaFiscalEletronica(Long idMonitoramentoDocEletronic, Long proximoNumeroRps) {
		
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDocEletronic);
		
		if(proximoNumeroRps != null){
			monitoramentoDocEletronico.setNrFiscalRps(proximoNumeroRps);
		}
		
		if(nfeConjugadaService.isAtivaNfeConjugada(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getIdFilial())){
			nfeConjugadaService.storeReenvio(monitoramentoDocEletronico.getDoctoServico(), monitoramentoDocEletronico, proximoNumeroRps);
		} else {
			//LMS-4741 - Menu para Reenvio de RPS quando a mesma esta rejeita por motivos fiscais ( Aliquota/Retenções).
			String xml = createXml(monitoramentoDocEletronico.getDoctoServico(),monitoramentoDocEletronico.getNrFiscalRps());
			TBInputDocuments tbInputDocuments = generateIntegracaoEmissaoNfe(monitoramentoDocEletronico, xml);
			monitoramentoDocEletronico.setIdEnvioDocEletronicoE(tbInputDocuments.getId());
			monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue(ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_ENVIADO));
			monitoramentoDocEletronico.setDsDadosDocumento(tbInputDocuments.getDocData());
			
			monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
		}		
	}
	
	public TBInputDocuments generateIntegracaoEmissaoNfe(MonitoramentoDocEletronico monitoramentoDocEletronico, String xml) {
		TBInputDocuments tbInputDocuments = new TBInputDocuments();
		tbInputDocuments.setDocData(xml.getBytes(Charset.forName("UTF-8")));
		tbInputDocuments.setDocKind(1);
		tbInputDocuments.setJobKey(Long.valueOf(monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getCodFilial()));
		tbInputDocuments.setDocStatus(0);
		tbInputDocuments.setInsertDate(new DateTime());
		notaFiscalEletronicaDAO.createTBInputDocuments(tbInputDocuments);
		return tbInputDocuments;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeAtualizaRetornoNFSe(TBIntegration notaFiscalEletronicaRetorno ) throws IOException, ParserConfigurationException, XPathExpressionException, SAXException{
		Integer docKind = notaFiscalEletronicaRetorno.getDocKind();
		if (docKind == 1) {
			processaRetornoAutorizacao(notaFiscalEletronicaRetorno, getFilial(notaFiscalEletronicaRetorno));
		}
		if (docKind == 2) {
			processaRetornoCancelamento(notaFiscalEletronicaRetorno, getFilial(notaFiscalEletronicaRetorno));
		}
		if (docKind == 3) { 
			processaErro(notaFiscalEletronicaRetorno, getFilial(notaFiscalEletronicaRetorno));
		}
		//5. Atualizar, na tabela TBINTEGRATION, o campo DOCSTATUS para 1
		notaFiscalEletronicaRetorno.setDocStatus(1);
		notaFiscalEletronicaRetornoService.store(notaFiscalEletronicaRetorno);
	}

	private Filial getFilial(TBIntegration notaFiscalEletronicaRetorno) {
			Long idEmpresa = ((BigDecimal)configuracoesFacade.getValorParametro("ID_EMPRESA_MERCURIO")).longValue();
		return filialService.findFilialByCodFilial(idEmpresa, notaFiscalEletronicaRetorno.getJobKey().intValue());
	}
			
	private void processaRetornoAutorizacao(TBIntegration notaFiscalEletronicaRetorno, Filial filial) {		
		Map<String, Object> map = doctoServicoService.findDoctoServicoByNrFiscalRps(filial.getIdFilial(), notaFiscalEletronicaRetorno.getRpsNumber());						
		if (map != null) {
			//1.3. Buscar o registro na tabela MONITORAMENTO_DOC_ELETRONICO pelo campo DOCTO_SERVICO.ID_DOCTO_SERVICO igual ao ID obtido no item acima.
			MonitoramentoDocEletronico monitoramentoDocEletronico = (MonitoramentoDocEletronico) map.get("monitoramentoDocEletronico");
			if (monitoramentoDocEletronico != null) {
				//1.4.	Se for encontrado um registro na tabela MONITORAMENTO_DOC_ELETRONICO, atualizar os seguintes campos na tabela MONITORAMENTO_DOC_ELETRONICO:
				String xml = notaFiscalEletronicaRetornoService.findDocData(notaFiscalEletronicaRetorno.getId());
				StringReader sr = new StringReader(xml);
				try {
					Document doc = createDoc(sr);
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("A"));
					String numnfsRetorno = getNUMNFSRetorno(doc);
					monitoramentoDocEletronico.setNrDocumentoEletronico(isNumericNUMNFSRetorno(numnfsRetorno) ? Long.valueOf( numnfsRetorno) : null);
					monitoramentoDocEletronico.setDhAutorizacao(getDATHORRetorno(doc));
					monitoramentoDocEletronico.setNrChave(getChaveRetorno(doc));
					monitoramentoDocEletronico.setDsObservacao(null);
					monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
					
					if(monitoramentoDocEletronico.getDoctoServico().getDoctoServicoOriginal()!= null){
					    finalizacaoCteOriginal(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico(), monitoramentoDocEletronico.getDoctoServico().getDoctoServicoOriginal().getIdDoctoServico());
					}
					
				} catch (Exception e) {
					log.error(e);
				} finally {
					if(sr != null){
						sr.close();
					}
				}
			}
		}
	}
	
	private void finalizacaoCteOriginal(Long idDoctoServico, Long idDoctoServicoOriginal) {
		if (CollectionUtils.isNotEmpty(notaFiscalOperadaService.findByIdDoctoServico(idDoctoServico))) {
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(idDoctoServicoOriginal, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}
	}
	
	private boolean isNumericNUMNFSRetorno(String numnfsRetorno) {
		return StringUtils.isNotBlank(numnfsRetorno) && StringUtils.isNumeric(numnfsRetorno);
	}
					
	private void processaRetornoCancelamento(TBIntegration notaFiscalEletronicaRetorno, Filial filial) {
		String xml = notaFiscalEletronicaRetornoService.findDocData(notaFiscalEletronicaRetorno.getId());
		StringReader sr = new StringReader(xml);
		try {
			//3.2.Acessar o documento de serviço relativo ao registro que se está processando (DOCTO_SERVICO.ID_FILIAL_ORIGEM = FILIAL.ID_FILIAL da filial acima, DOCTO_SERVICO.ID_DOCTO_SERVICO = MONITORAMENTO_DOC_ELETRONICO.ID_DOCTO_SERVICO e MONITORAMENTO_DOC_ELETRONICO.NR_FISCAL_RPS =   TBINTEGRATION.RPSNUMBER e  DOCTO_SERVICO.TP_DOCTO_SERVICO in (NTE,NSE).
			Map<String, Object> map = doctoServicoService.findDoctoServicoByNrFiscalRps(filial.getIdFilial(), notaFiscalEletronicaRetorno.getRpsNumber());
			
			if (map != null) {
				//3.3. Buscar o registro na tabela MONITORAMENTO_DOC_ELETRONICO pelo campo DOCTO_SERVICO.ID_DOCTO_SERVICO igual ao ID obtido no item acima.
				MonitoramentoDocEletronico monitoramentoDocEletronico = (MonitoramentoDocEletronico) map.get("monitoramentoDocEletronico");
				if (monitoramentoDocEletronico != null) {
					//3.4. Se for encontrado um registro na tabela MONITORAMENTO_DOC_ELETRONICO, atualizar os seguintes campos na tabela MONITORAMENTO_DOC_ELETRONICO:
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("C"));
					monitoramentoDocEletronico.setDsObservacao(null);
					monitoramentoDocEletronico.setDsDadosDocumento(xml.getBytes(Charset.forName("UTF-8")));
					monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
				}
				
				//3.5 Para cada chave associada ao documento de serviço não nula 
				DoctoServico doctoServico = (DoctoServico) map.get("doctoServico");
				monitoramentoNotasFiscaisCCTService.executeVincularDocumentoComMonitoramento(doctoServico, "CA");
			}
		} finally {
			if(sr != null){
				sr.close();
			}
		}
	}

	private void processaErro(TBIntegration notaFiscalEletronicaRetorno, Filial filial) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		Map<String, Object> map = doctoServicoService.findDoctoServicoByNrFiscalRps(filial.getIdFilial(), notaFiscalEletronicaRetorno.getRpsNumber());
		if (map != null) {
			//2.3, 4.3. Buscar o registro na tabela MONITORAMENTO_DOC_ELETRONICO pelo campo DOCTO_SERVICO.ID_DOCTO_SERVICO igual ao ID obtido no item acima.
			MonitoramentoDocEletronico monitoramentoDocEletronico = (MonitoramentoDocEletronico) map.get("monitoramentoDocEletronico");
			if (monitoramentoDocEletronico != null) {
				String xml = notaFiscalEletronicaRetornoService.findDocData(notaFiscalEletronicaRetorno.getId());
				StringReader sr = new StringReader(xml);
				try {
					Document doc = createDoc(sr);
					monitoramentoDocEletronico.setTpSituacaoDocumento(isCancelamentoByTag(doc) ? new DomainValue("D") : new DomainValue("R"));
					String msg = getObservacao(doc);
					monitoramentoDocEletronico.setDsObservacao(NfeConverterUtils.tratarString(msg,2000));
					monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
				} finally {
					if(sr != null) {
						sr.close();
					}
				}
			}													
		}
		
		//5. Atualizar, na tabela TBINTEGRATION, o campo DOCSTATUS para 1
		notaFiscalEletronicaRetorno.setDocStatus(1);
		notaFiscalEletronicaRetornoService.store(notaFiscalEletronicaRetorno);			
	}

	private Document createDoc(StringReader sr)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			
		Document doc = docBuilder.parse(new InputSource(sr));
		return doc;
	}

	private boolean isCancelamentoByTag(Document doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
				
	    // Compile the XPath expression
	    XPathExpression expr = getExpression(doc, "count(//param[@nome='NUMRPS'][@valor='CANC'])");
	    // Run the query and get a nodeset
	    Double result = (Double)expr.evaluate(doc, XPathConstants.NUMBER);
	    return result > 0;
	}
	
	private String getNUMNFSRetorno(Document doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		// Compile the XPath expression
		XPathExpression expr = getExpression(doc, "//param[@nome='NUMNFS']/@valor");
		// Run the query and get a nodeset
		return (String)expr.evaluate(doc, XPathConstants.STRING);
	}
	
	private DateTime getDATHORRetorno(Document doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		// Compile the XPath expression
		XPathExpression expr = getExpression(doc, "//param[@nome='DATHOR']/@valor");
		// Run the query and get a nodeset
		String dathorString = (String)expr.evaluate(doc, XPathConstants.STRING);
		
		return JTDateTimeUtils.formatStringToDateTimeWithSeconds(dathorString);
	}
	
	private List<String> getListaExpressoes(Document doc, String expression) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		XPathExpression expr = getExpression(doc, expression);
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		
		List<String> listaMensagens = new ArrayList<String>();
		for (int i = 0; i < nodes.getLength(); i++) {
			listaMensagens.add(nodes.item(i).getNodeValue());
	}
		return listaMensagens;
	}
	
	private String getObservacao(Document doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		List<String> mensagensRetorno = getListaExpressoes(doc, "//Mensagens/MensagemRetorno/Mensagem/text()");
		List<String> correcoesRetorno = getListaExpressoes(doc, "//Mensagens/MensagemRetorno/Correcao/text()");
		List<String> mensagensLote = getListaExpressoes(doc, "//MensagensLote/MensagemRetornoLote/Mensagem/text()");
		
		List<String> total = new ArrayList<String>();
		for (int i = 0; i < mensagensRetorno.size(); i++) {
			total.add(mensagensRetorno.get(i) + getCorrecaoRetorno(correcoesRetorno, i));
	}
		total.addAll(mensagensLote);
	
		return StringUtils.join(total.toArray(), " / ");
	}

	private String getCorrecaoRetorno(List<String> correcoesRetorno, int i) {
		if (correcoesRetorno.size() <= i) {
			return "";
		}
		String correcaoRetorno = correcoesRetorno.get(i);
		return StringUtils.isBlank(correcaoRetorno) ? "" : " / " + correcaoRetorno;
	}
	
	private String getChaveRetorno(Document doc) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		XPathExpression expr = getExpression(doc, "//param[@nome='CODVRF']/@valor");
		// Run the query and get a nodeset
		return (String)expr.evaluate(doc, XPathConstants.STRING);
	}
	
	private XPathExpression getExpression(Document doc, String expression) throws XPathExpressionException {
		// Create a XPathFactory
		XPathFactory xFactory = XPathFactory.newInstance();
		
		// Create a XPath object
		XPath xpath = xFactory.newXPath();
		
		// Compile the XPath expression
		return xpath.compile(expression);
	}
	
	@SuppressWarnings("unchecked")
	public JasperPrint createNseJasperPrintByList(Locale currentUserLocale,String host, ClassPathResource jasperResourceNse,
			List<Map<String, Object>> listNses) throws IOException {
    	JasperPrint jasperPrintNse = new JasperPrint();
		for (Map<String, Object> map : listNses) {
			Map<String, Object> nfeInfs = monitoramentoDocEletronicoService.executeMontarInfNse((Long)map.get("idConhecimento"), (Long)map.get("idMonitoramentoDocEletronic"));
			JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpss(1,
					(String)nfeInfs.get("nfeXML"),
					(Map<String, String>)nfeInfs.get("infRpss"),
					(List)nfeInfs.get("dsObservacaoDoctoServico"),
					currentUserLocale, jasperResourceNse.getInputStream(), host);
			if(jasperPrintNse.getPages().size() < 1){
				jasperPrintNse = jasperPrint;
			}else if(jasperPrint.getPages() != null && jasperPrint.getPages().size() > 0){
				jasperPrintNse.getPages().addAll(jasperPrint.getPages());
			}
		}
		return jasperPrintNse;
	}

	@SuppressWarnings("unchecked")
	public JasperPrint createNteJasperPrintByList(Locale currentUserLocale,String host, ClassPathResource jasperResourceNte, 
			List<Map<String, Object>> listNtes) throws IOException {
		JasperPrint jasperPrintNte = new JasperPrint();
		for (Map<String, Object> map : listNtes) {
			Map<String, Object> nfeInfs = monitoramentoDocEletronicoService.executeMontarInfNte((Long)map.get("idConhecimento"), (Long)map.get("idMonitoramentoDocEletronic"));
			JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpst(1,
					(String)nfeInfs.get("nfeXML"),
					(List)nfeInfs.get("listNrNotas"),
					(Map<String, String>)nfeInfs.get("infRpst"),
					(List)nfeInfs.get("dsObservacaoDoctoServico"),
					currentUserLocale, jasperResourceNte.getInputStream(), host);
			if(jasperPrintNte.getPages().size() < 1){
				jasperPrintNte = jasperPrint;
			}else if(jasperPrint.getPages() != null && jasperPrint.getPages().size() > 0){
				jasperPrintNte.getPages().addAll(jasperPrint.getPages());
			}
		}
		return jasperPrintNte;
	}
	
	/**
	 * @param listNtes (List<Map<String, Long>>)
	 * Map required idConhecimento and idMonitoramentoDocEletronic
	 * @return JasperPrint
	 */
	public JasperPrint createNteJasperPrintNteByList(List<Map<String,Object>> listNtes) { 
		
		final Usuario currentUser = SessionContext.getUser();
		Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
		HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
		String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		
		ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPST.jasper");
		try {
			return createNteJasperPrintByList(currentUserLocale, host, jasperResourceNte, listNtes);
		} catch (IOException e) {
			log.error(e);
			throw new BusinessException("Erro ao criar o relatório");
		}
	}
	
	/**
	 * @param listNtes (List<Map<String, Long>>)
	 * Map required idDoctoServico and idMonitoramentoDocEletronic
	 * @return JasperPrint
	 */
	public JasperPrint createNseJasperPrintNteByList(List<Map<String,Object>> listNtes) { 
		
		final Usuario currentUser = SessionContext.getUser();
		Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
		HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
		String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
		
		ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPSS.jasper");
		try {
			return createNseJasperPrintByList(currentUserLocale, host, jasperResourceNte, listNtes);
		} catch (IOException e) {
			log.error(e);
			throw new BusinessException("Erro ao criar o relatório");
		}
	}
	
	public InputStream findNfeXMLFromEDI(String chaveNFe) throws SQLException {
		return this.notaFiscalEletronicaDAO.findNfeXMLFromEDI(chaveNFe);
	}
	
	public String findNfeXMLFromEDIToString(String chaveNFe) throws SQLException {
		return this.notaFiscalEletronicaDAO.findNfeXMLFromEDIToString(chaveNFe);
	}
	
	public String findValorEspecificoFromNfeXMLEDI(String chaveNFe, String nodoPrincipal, String nodoValor){
		
		DocumentBuilder db;
        try {
        	String xml = findNfeXMLFromEDIToString(chaveNFe);
        	
        	if(xml != null){
	            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	            InputSource is = new InputSource();
	            is.setCharacterStream(new StringReader(xml));
	            Document doc = db.parse(is);
	            
	            NodeList nodes = doc.getElementsByTagName(nodoPrincipal);
	
	            for (int temp = 0; temp < nodes.getLength(); temp++) {
					Node nNode = nodes.item(temp);
	
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) nNode;
						if(element.getElementsByTagName(nodoValor).item(0) != null){
							return element.getElementsByTagName(nodoValor).item(0).getTextContent();
						}
					}
	            }
	            return "";
        	}else{
        		return "";
        	}
        }catch (Exception e) {
			throw new InfrastructureException(e);
		} 
	}

	public List findNotasEDINaoProcessadas(Integer qtLimiteNotas, Integer qtLimiteDias) {
		return notaFiscalEletronicaDAO.findNotasEDINaoProcessadas(qtLimiteNotas, qtLimiteDias);
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
        this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
    }
	
	public void setNotaFiscalEletronicaDAO(NotaFiscalEletronicaDAO notaFiscalEletronicaDAO) {
		this.notaFiscalEletronicaDAO = notaFiscalEletronicaDAO;
	}

	public Map<String, String> findInfoRpstByDoctoServico(Long idDoctoServico) {
		return notaFiscalEletronicaDAO.findInfoRpstByDoctoServico(idDoctoServico);
	}
	
	public Map<String, String> findInfoRpssByDoctoServico(Long idDoctoServico) {
		return notaFiscalEletronicaDAO.findInfoRpssByDoctoServico(idDoctoServico);
	}

	
	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}
	
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	
	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void setNotaFiscalEletronicaRetornoService(NotaFiscalEletronicaRetornoService notaFiscalEletronicaRetornoService) {
		this.notaFiscalEletronicaRetornoService = notaFiscalEletronicaRetornoService;
	}
	
	public void setCodigoMunicipioFilialService(CodigoMunicipioFilialService codigoMunicipioFilialService) {
		this.codigoMunicipioFilialService = codigoMunicipioFilialService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public NFEConjugadaService getNfeConjugadaService() {
		return nfeConjugadaService;
	}

	public void setNfeConjugadaService(NFEConjugadaService nfeConjugadaService) {
		this.nfeConjugadaService = nfeConjugadaService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setRegistrarBaixaEntregaPorNotaFiscalService(
			RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService) {
		this.registrarBaixaEntregaPorNotaFiscalService = registrarBaixaEntregaPorNotaFiscalService;
	}
}

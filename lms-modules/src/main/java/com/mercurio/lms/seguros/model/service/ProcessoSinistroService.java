package com.mercurio.lms.seguros.model.service;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.seguros.model.CustoAdicionalSinistro;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.seguros.model.dao.ProcessoSinistroDAO;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.processoSinistroService"
 */
public class ProcessoSinistroService extends CrudService<ProcessoSinistro, Long> {
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	private Logger log = LogManager.getLogger(this.getClass());
	private DoctoServicoService doctoServicoService;
	private SinistroDoctoServicoService sinistroDoctoServicoService;
	private ManifestoService manifestoService;
	private CustoAdicionalSinistroService custoAdicionalSinistroService;
	private ParametroGeralService parametroGeralService;
	private ConversaoMoedaService conversaoMoedaService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaService moedaService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private ReportExecutionManager reportExecutionManager;
	private IntegracaoJmsService integracaoJmsService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private ReciboReembolsoProcessoService reciboReembolsoProcessoService;
	private MeioTransporteService meioTransporteService;
	private DomainValueService domainValueService;
	
	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
	public SinistroDoctoServicoService getSinistroDoctoServicoService() {
		return sinistroDoctoServicoService;
	}

	public void setSinistroDoctoServicoService(SinistroDoctoServicoService sinistroDoctoServicoService) {
		this.sinistroDoctoServicoService = sinistroDoctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public CustoAdicionalSinistroService getCustoAdicionalSinistroService() {
		return custoAdicionalSinistroService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
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

	public ReciboReembolsoProcessoService getReciboReembolsoProcessoService() {
		return reciboReembolsoProcessoService;
	}

	public void setReciboReembolsoProcessoService(ReciboReembolsoProcessoService reciboReembolsoProcessoService) {
		this.reciboReembolsoProcessoService = reciboReembolsoProcessoService;
	}
	
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
	 * Recupera uma instância de <code>ProcessoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ProcessoSinistro findById(java.lang.Long id) {
        return (ProcessoSinistro)super.findById(id);
    }


	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ProcessoSinistro bean) {
        return super.store(bean);
    }

    /**
     * Método store da tela de processos de sinistro.
     * @param processoSinistro
     * @return
     */
    public java.io.Serializable storeCustom(ProcessoSinistro processoSinistro, ItemList itens) {

    	// se é um update
    	if (processoSinistro.getIdProcessoSinistro()!=null) {
	    	ProcessoSinistro processoSinistroBanco = findById(processoSinistro.getIdProcessoSinistro());
	    	
	    	// se o número do processo de sinistro foi alterado
	    	if (!processoSinistro.getNrProcessoSinistro().equals(processoSinistroBanco.getNrProcessoSinistro()))
	    	 {
		    	// se o novo número de processo de sinistro já existe, então dispara exceção.
		    	if (verifyByNrProcesso(processoSinistro.getNrProcessoSinistro())) {		    		
		    		throw new BusinessException("LMS-22013");
		    	}
	    	}

	    	// evitando da sessão o objeto populado pelo findById 
	    	getProcessoSinistroDAO().getAdsmHibernateTemplate().evict(processoSinistroBanco);
	    	
	    // se é um cadastro de processo e o número já existe, também dispara exceção
    	} else if (verifyByNrProcesso(processoSinistro.getNrProcessoSinistro())) {
	    	throw new BusinessException("LMS-22013");
	    }
    	
    	Serializable idProcessoSinistro = null;
    	
		boolean rollbackMasterId = processoSinistro.getIdProcessoSinistro() == null;
		
    	try {

    		idProcessoSinistro = this.storeProcessoSinistro(processoSinistro, itens);

    		
    	} catch (RuntimeException re) {
        	this.rollbackMasterState(processoSinistro, rollbackMasterId, re); 
            itens.rollbackItemsState();   
            throw re;
        }
    	
    	return idProcessoSinistro;
    }
    
    
    /**
     * Método store para a tela de manterProcessoSinisto (DF2).
     * @param processoSinistro
     * @param itens
     * @return
     */
    private Serializable storeProcessoSinistro(ProcessoSinistro processoSinistro, ItemList itens) {
    	
    	store(processoSinistro);
    	
    	if(itens != null){
    		for (Object item : itens.getNewOrModifiedItems()) {
    			SinistroDoctoServico documento = (SinistroDoctoServico) item;
    			// Seta a moeda de acordo com o processo de sinistro
    			documento.setMoeda(moedaService.findById(processoSinistro.getMoeda().getIdMoeda()));
    			documento.setProcessoSinistro(processoSinistro);
    			sinistroDoctoServicoService.store(documento);
    		}
    		
    		for (Object item : itens.getRemovedItems()) {
    			SinistroDoctoServico documento = (SinistroDoctoServico) item;
    			sinistroDoctoServicoService.removeById(documento.getIdSinistroDoctoServico());
    		}
    	}

    	return processoSinistro.getIdProcessoSinistro();
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param dao
     */
    public void setProcessoSinistroDAO(ProcessoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ProcessoSinistroDAO getProcessoSinistroDAO() {
        return (ProcessoSinistroDAO) getDao();
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
    	List list = new ArrayList();
    	ResultSetPage rsp = getProcessoSinistroDAO().findPaginatedCustom(FindDefinition.createFindDefinition(tfm), tfm);
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		Object[] result = (Object[]) it.next();
    		Map map = new HashMap(13);
    		map.put("idProcessoSinistro", result[0]);
    		map.put("nrProcessoSinistro", result[1]);
    		map.put("nrIdentificador", result[2]);
    		map.put("nrFrota", result[3]);
    		map.put("dhSinistro", result[4]);
    		map.put("tipoSinistro", result[5]);
    		map.put("tipoSeguro", result[6]);
    		map.put("municipio", result[7]);
    		map.put("uf", result[8]);
    		map.put("valorMercadoria", this.findSomaValoresMercadoria(Long.valueOf(result[0].toString())).toString());
    		map.put("valorPrejuizo", this.findSomaValoresPrejuizo(Long.valueOf(result[0].toString())).toString());
			map.put("valorIndenizado", this.findSomaValoresIndenizado(Long.valueOf(result[0].toString())).toString());
    		map.put("usuarioResponsavel", result[9]);
    		list.add(map);
    	}
    	rsp.setList(list);
    	return rsp;
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getProcessoSinistroDAO().getRowCountCustom(tfm);
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'emitirCartaOcorrencia'.
     * 
     * @param criteria
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedSinistroDoctoServico(TypedFlatMap criteria) {
    	List idsSinistroDoctoServico = new ArrayList();
    	
    	for (Iterator iter = criteria.getList("idsSinistroDoctoSinistro.ids").iterator(); iter.hasNext();) {
			String id = (String) iter.next();
			idsSinistroDoctoServico.add(Long.valueOf(id));
		}
    	
    	return this.getSinistroDoctoServicoService().findPaginatedCartaOcorrencia(idsSinistroDoctoServico, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Chamada para a consulta de getRowCount da tela de 'emitirCartaOcorrencia'
     * 
     * @param criteria 
     * @return
     */
    public Integer getRowCountSinistroDoctoServico(TypedFlatMap criteria) {
    	List idsSinistroDoctoServico = new ArrayList();
    	
    	for (Iterator iter = criteria.getList("idsSinistroDoctoSinistro.ids").iterator(); iter.hasNext();) {
			String id = (String) iter.next();
			idsSinistroDoctoServico.add(Long.valueOf(id));
		}
    	
    	return this.getSinistroDoctoServicoService().getRowCountCartaOcorrencia(idsSinistroDoctoServico);
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'selecionarDocumentos'
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedSelecionarDocumentos(TypedFlatMap criteria) {
    	
    	Long idClienteDestinatario = criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"); 
		Long idClienteRemetente =criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente");
		String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigemDoctoServico = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		Long idDocumentoServico = criteria.getLong("doctoServico.idDoctoServico");
		String tpPrejuizo = criteria.getString("tpPrejuizo");
		Boolean blNaoEnviados = criteria.getBoolean("blEnviado");
				
    	return this.getSinistroDoctoServicoService().findPaginatedSelecionarDocumentos(idClienteDestinatario, idClienteRemetente, 
    			tpDocumentoServico, idFilialOrigemDoctoServico, idDocumentoServico, tpPrejuizo, blNaoEnviados.booleanValue(), 
    			false, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Chamada para a consulta de getRowCount da tela de 'selecionarDocumentos'
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountSelecionarDocumentos(TypedFlatMap criteria) {
    	
    	Long idClienteDestinatario = criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"); 
		Long idClienteRemetente =criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente");
		String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigemDoctoServico = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");		
		Long idDocumentoServico = criteria.getLong("doctoServico.idDoctoServico");
		
		String tpPrejuizo = criteria.getString("tpPrejuizo");
		Boolean blNaoEnviados = criteria.getBoolean("blEnviado");
				
		return this.getSinistroDoctoServicoService().getRowCountSelecionarDocumentos(idClienteDestinatario, idClienteRemetente, 
				tpDocumentoServico, idFilialOrigemDoctoServico, idDocumentoServico, tpPrejuizo, blNaoEnviados.booleanValue(), false);
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'selecionarDocumentos'
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedSelecionarDocumentosRim(TypedFlatMap criteria) {
    	
    	Long idClienteDestinatario = criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"); 
		Long idClienteRemetente =criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente");
		Long idFilialOrigem = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"); 
		Long idDocumentoServico = criteria.getLong("doctoServico.idDoctoServico");
		String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
		
		String tpPrejuizo = criteria.getString("tpPrejuizo");
		Boolean blNaoEnviados = criteria.getBoolean("blEnviado");
				
    	return this.getSinistroDoctoServicoService().findPaginatedSelecionarDocumentosRim(idClienteDestinatario, idClienteRemetente, 
    			tpDocumentoServico, idDocumentoServico, idFilialOrigem, null, tpPrejuizo, blNaoEnviados.booleanValue(), true, 
    			FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Chamada para a consulta de getRowCount da tela de 'selecionarDocumentos'
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountSelecionarDocumentosRim(TypedFlatMap criteria) {
    	
    	Long idClienteDestinatario = criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"); 
		Long idClienteRemetente =criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente");
		Long idFilialOrigem = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"); 
		Long idDocumentoServico = criteria.getLong("doctoServico.idDoctoServico");		
		String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
		
		String tpPrejuizo = criteria.getString("tpPrejuizo");
		Boolean blNaoEnviados = criteria.getBoolean("blEnviado");
				
		return this.getSinistroDoctoServicoService().getRowCountSelecionarDocumentosRim(idClienteDestinatario, idClienteRemetente, 
				tpDocumentoServico, idDocumentoServico, idFilialOrigem, null, tpPrejuizo, blNaoEnviados.booleanValue(), true);
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'enviar email'.
     * 
     * @param criteria
     * @return ResultSetPage
     */
    public List findPaginatedEnviarEmail(TypedFlatMap criteria) {    	
    	
    	List idsSinistroDoctoServico = new ArrayList();
    	
    	for (Iterator iter = criteria.getList("idsSinistroDoctoServico").iterator(); iter.hasNext();) {
    		String id = (String) iter.next();
    		idsSinistroDoctoServico.add(Long.valueOf(id));
    	}
    	
    	return this.getSinistroDoctoServicoService().findPaginatedEmailCartaOcorrencia(idsSinistroDoctoServico, 
    			criteria.getString("destinatarioCarta"));
    }
    
    /**
     * Chamada para a consulta do findPaginated da tela de 'enviar email'.
     * 
     * @param criteria
     * @return ResultSetPage
     */
    public List findPaginatedEnviarEmailRim(TypedFlatMap criteria) {    	
    	List idsSinistroDoctoServico = new ArrayList();
    	    	
    	for (Iterator iter = criteria.getList("idsSinistroDoctoServico").iterator(); iter.hasNext();) {
			String id = (String) iter.next();
			idsSinistroDoctoServico.add(Long.valueOf(id));
		}
    	
    	return this.getSinistroDoctoServicoService().findPaginatedEmailRim(idsSinistroDoctoServico, 
    			criteria.getString("filial"), FindDefinition.createFindDefinition(criteria));
    }
    
    /**
	 * Atualiza a data de geracao da carta de ocorrencia/retificacao...
	 * 
	 * @param idsSinistroDoctoServico
	 * @param tpCarta
	 */
	public void updateDataGeracaoCarta(List idsSinistroDoctoServico, String tpCarta) {
		
		for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			String idSinistro = (String) iter.next();
			Long idSinistroDoctoServico = Long.valueOf(idSinistro);
			
			SinistroDoctoServico sinistroDoctoServico = this.getSinistroDoctoServicoService().findById(idSinistroDoctoServico);
			
			if (tpCarta.equals("R")) {
				sinistroDoctoServico.setDhGeracaoCartaRetificacao(JTDateTimeUtils.getDataHoraAtual());
			} else {
				sinistroDoctoServico.setDhGeracaoCartaOcorrencia(JTDateTimeUtils.getDataHoraAtual());
			}
			
			
			this.getSinistroDoctoServicoService().store(sinistroDoctoServico);
		}
	}
	
	/**
	 * Atualiza a data de geracao do E-Mail de ocorrencia/retificacao...
	 * 
	 * @param idsSinistroDoctoServico
	 * @param tpEmail
	 */
	public void updateDataGeracaoEmail(List idsSinistroDoctoServico, String tpEmail) {
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();	
		for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			String idSinistro = (String) iter.next();
			Long idSinistroDoctoServico = Long.valueOf(idSinistro);
			
			SinistroDoctoServico sinistroDoctoServico = this.getSinistroDoctoServicoService().findById(idSinistroDoctoServico);
			
			if (tpEmail.equals("R")) {
				sinistroDoctoServico.setDhGeracaoCartaRetificacao(dataHoraAtual);
				sinistroDoctoServico.setDhEnvioEmailRetificacao(dataHoraAtual);
			} else {
				sinistroDoctoServico.setDhGeracaoCartaOcorrencia(dataHoraAtual);
				sinistroDoctoServico.setDhEnvioEmailOcorrencia(dataHoraAtual);
			}
			this.getSinistroDoctoServicoService().store(sinistroDoctoServico);
		}
	}
    
	/**
	 * Faz a chamadas para a geracao dos emails e serem enviados tanto quanto a chamada
	 * para a atualizacao da data de geracao dos e-mails.
	 * 
	 * @param criteria
	 */
    public void generateEmailCartaOcorrencia(TypedFlatMap criteria){
		
    	String destinatarioCarta = criteria.getDomainValue("destinatarioCarta").getValue();
    	
    	List idsDestinatario = criteria.getList("idsDestinatario");
    	
    	//Itera sobre os ids das pessoas da tela de e-mail...
    	for (Iterator iter = criteria.getList("emailDestinatario").iterator(); iter.hasNext();) {
    		
    		TypedFlatMap emailDestinatario = (TypedFlatMap) iter.next();
    		String idPessoa = emailDestinatario.getString("id");
        	String dsMail = emailDestinatario.getString("dsEmail");
        	
        	//Verifica se existe um email
        	if (!"".equals(dsMail)) {
        	
	        	String[] dsEmails = dsMail.split(";");
	        	
	        	for (int i = 0; i < dsEmails.length; i++) {
					String email = dsEmails[i];
					email = email.trim();
	        	
					if (ValidateUtils.validateEmail(email)) {
					
			        	//Itera sobre os ids selecionados na tela inicial...
						List idsSinistroDoctoServico = new ArrayList();
			        	List idsSinistroDoctoServicoDestinario = new ArrayList();
			        	List idsSinistroDoctoServicoRemetente = new ArrayList();
			        	List idsSinistroDoctoServicoFilialOrigem = new ArrayList();
			        	List idsSinistroDoctoServicoFilialDestino = new ArrayList();
			        	
			        	for (Iterator iterator = idsDestinatario.iterator(); iterator.hasNext();) {
							TypedFlatMap ids = (TypedFlatMap) iterator.next();
							DoctoServico doctoServico = (DoctoServico) sinistroDoctoServicoService.findDoctoServicoByIdSinistroDoctoServico(ids.getLong("idSinistroDoctoServico")).get(0);
							
							if (doctoServico != null) {
								
								// Todos os destinatários
								if ("A".equals(destinatarioCarta)) {
									
									if (doctoServico.getClienteByIdClienteRemetente() != null && 
											idPessoa.equals(doctoServico.getClienteByIdClienteRemetente().getPessoa().getIdPessoa().toString())) {
										idsSinistroDoctoServicoRemetente.add(ids.getString("idSinistroDoctoServico"));
									} else if (doctoServico.getClienteByIdClienteDestinatario() != null && 
											idPessoa.equals(doctoServico.getClienteByIdClienteDestinatario().getPessoa().getIdPessoa().toString())) {
										idsSinistroDoctoServicoDestinario.add(ids.getString("idSinistroDoctoServico"));
									} else if (doctoServico.getFilialByIdFilialOrigem() != null && 
											idPessoa.equals(doctoServico.getFilialByIdFilialOrigem().getIdFilial().toString())) {
										idsSinistroDoctoServicoFilialOrigem.add(ids.getString("idSinistroDoctoServico"));
									} else if (doctoServico.getFilialByIdFilialDestino() != null && 
											idPessoa.equals(doctoServico.getFilialByIdFilialDestino().getIdFilial().toString())) {
										idsSinistroDoctoServicoFilialDestino.add(ids.getString("idSinistroDoctoServico"));
									}

								} else {
									
									if ("R".equals(destinatarioCarta) && doctoServico.getClienteByIdClienteRemetente() != null) {
										if (idPessoa.equals(doctoServico.getClienteByIdClienteRemetente().getPessoa().getIdPessoa().toString())) {
											idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
										}
									} else if ("D".equals(destinatarioCarta) && doctoServico.getClienteByIdClienteDestinatario() != null) {
										if (idPessoa.equals(doctoServico.getClienteByIdClienteDestinatario().getPessoa().getIdPessoa().toString())) {
											idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
										}
									} else if ("O".equals(destinatarioCarta)) {
										if (idPessoa.equals(doctoServico.getFilialByIdFilialOrigem().getIdFilial().toString())) {
											idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
										}
									} else if ("S".equals(destinatarioCarta) && doctoServico.getFilialByIdFilialDestino() != null) {
										if (idPessoa.equals(doctoServico.getFilialByIdFilialDestino().getIdFilial().toString())) {
											idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
										}
									}
								}
								
			        		}
						}
			        	
			        	if (!"".equals(email)) {
			        		
		            		//Gera o email...
		        			if (!"A".equals(destinatarioCarta) && (!idsSinistroDoctoServico.isEmpty())) {
		        				this.sendEmailCartaOcorrencia(idsSinistroDoctoServico, email, destinatarioCarta, criteria.getString("tipoCarta"));
		        				//Atualiza a data de geracao do email...
				        		this.updateDataGeracaoEmail(idsSinistroDoctoServico, criteria.getString("tipoCarta"));
		        			} else {
		        				
		        				if (!idsSinistroDoctoServicoDestinario.isEmpty()) {
		        					this.sendEmailCartaOcorrencia(idsSinistroDoctoServicoDestinario, email, "D", criteria.getString("tipoCarta"));
		        					//Atualiza a data de geracao do email...
					        		this.updateDataGeracaoEmail(idsSinistroDoctoServicoDestinario, criteria.getString("tipoCarta"));
		        				}
		        					
		        				if (!idsSinistroDoctoServicoRemetente.isEmpty()) {
	    							this.sendEmailCartaOcorrencia(idsSinistroDoctoServicoRemetente, email, "R", criteria.getString("tipoCarta"));
	    							//Atualiza a data de geracao do email...
		        					this.updateDataGeracaoEmail(idsSinistroDoctoServicoRemetente, criteria.getString("tipoCarta"));
		        				}
	
		        				if (!idsSinistroDoctoServicoFilialDestino.isEmpty()) {
									this.sendEmailCartaOcorrencia(idsSinistroDoctoServicoFilialDestino, email, "S", criteria.getString("tipoCarta"));
									//Atualiza a data de geracao do email...
		        					this.updateDataGeracaoEmail(idsSinistroDoctoServicoFilialDestino, criteria.getString("tipoCarta"));
								}
								
		        				if (!idsSinistroDoctoServicoFilialOrigem.isEmpty()) {
									this.sendEmailCartaOcorrencia(idsSinistroDoctoServicoFilialOrigem, email, "O", criteria.getString("tipoCarta"));
		        					//Atualiza a data de geracao do email...
		        					this.updateDataGeracaoEmail(idsSinistroDoctoServicoFilialOrigem, criteria.getString("tipoCarta"));
		        				}
		        				
		        			}
			        		
			        	}
					}
				}
        	}
		}
	}
    
    /**
     * Realiza o envio do email e geracao do anexo.
     *
     * @param idsSinistroDoctoServico
     * @param destinatario
     * @param destinatarioCarta
     * @param tipoCarta
     */
    private void sendEmailCartaOcorrencia(List idsSinistroDoctoServico,final  String destinatario, String destinatarioCarta, String tipoCarta) {

		TypedFlatMap reportCriteria = new TypedFlatMap();

		reportCriteria.put("idsSinistroDoctoServico", idsSinistroDoctoServico);
		reportCriteria.put("destinatarioCarta", destinatarioCarta);
		reportCriteria.put("tipoCarta", tipoCarta);
		reportCriteria.put("_reportCall", Boolean.TRUE);

		File anexo = null;

		try {
			anexo = reportExecutionManager.executeReport("lms.seguros.emitirCartaOcorrenciaReportService", reportCriteria);
		} catch (Exception e) {
			// FIXME avaliar risco do try/catch
			log.error(e);
		}

		String assunto = configuracoesFacade.getMensagem("LMS-22020"); //Comunicado de sinistro com a carga.
		String mensagem = "";
	    // Pega a descricao do parametro geral.
	    String remetenteLms = (this.getParametroGeralService().findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
	    String nomeAnexo = anexo.getName();

	    sendMail(destinatario, assunto, mensagem, remetenteLms, nomeAnexo, anexo);
    }

	private void sendMail(final String destinatario, final String assunto,final String mensagem, final String remetenteLms,final String nomeAnexo, final File anexoFinal) {
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
		MailAttachment mailAttachment = new MailAttachment();
		mailAttachment.setName(nomeAnexo);
		mailAttachment.setData(FileUtils.readFile(anexoFinal));
		mailAttachments.add(mailAttachment);

		Mail mail = createMail(destinatario, remetenteLms, assunto, mensagem, mailAttachments);

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


	/**
	 * Atualiza a data de geracao da carta de comunicacao de RIM...
	 * 
	 * @param idsSinistroDoctoServico
	 */
	public void updateDataGeracaoCartaRim(List idsSinistroDoctoServico) {
		
		for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			String idDocto = (String) iter.next();
			Long idSinistroDoctoServico = Long.valueOf(idDocto);
			
			SinistroDoctoServico sinistroDoctoServico = this.getSinistroDoctoServicoService().findById(idSinistroDoctoServico);
			sinistroDoctoServico.setDhGeracaoFilialRim(JTDateTimeUtils.getDataHoraAtual());
			this.getSinistroDoctoServicoService().store(sinistroDoctoServico);
		}
	}
	
	/**
	 * Atualiza a data de geracao do E-Mail com a carta de comunicacao de RIM...
	 * 
	 * @param idsDoctoServico
	 */
	public void updateDataGeracaoEmailRim(List idsDoctoServico) {
		this.updateDataGeracaoCartaRim(idsDoctoServico);
	}
    
    /**
	 * Faz a chamadas para a geracao dos emails e serem enviados tanto quanto a chamada
	 * para a atualizacao da data de geracao dos e-mails.
	 * 
	 * @param criteria
	 */
    public void generateEmailComunicarUnidadesEmissaoRIM(TypedFlatMap criteria){
		
    	String filial = criteria.getString("filial");
    	
    	List idsDestinatario = criteria.getList("idsDestinatario");
    	
    	//Itera sobre os ids das pessoas da tela de e-mail...
    	for (Iterator iter = criteria.getList("emailDestinatario").iterator(); iter.hasNext();) {
    		
    		TypedFlatMap emailDestinatario = (TypedFlatMap) iter.next();
    		String idPessoa = emailDestinatario.getString("id");
        	String dsMail = emailDestinatario.getString("dsEmail");
        	
        	//Verifica se existe um email
        	if (!dsMail.equals("")) {
        	
	        	String[] dsEmails = dsMail.split(";");
	        	
	        	for (int i = 0; i < dsEmails.length; i++) {
					String email = dsEmails[i];
					email = email.trim();
	        	
					if (ValidateUtils.validateEmail(email)) {
					
			        	//Itera sobre os ids selecionados na tela inicial...
			        	List idsSinistroDoctoServico = new ArrayList();
			            
			        	for (Iterator iterator = idsDestinatario.iterator(); iterator.hasNext();) {
							TypedFlatMap ids = (TypedFlatMap) iterator.next();
							DoctoServico doctoServico = (DoctoServico) sinistroDoctoServicoService.findDoctoServicoByIdSinistroDoctoServico(ids.getLong("idSinistroDoctoServico")).get(0);
							
							if(doctoServico != null){
								if (("origem").equals(filial)) {
									if (idPessoa.equals(doctoServico.getFilialByIdFilialOrigem().getIdFilial().toString())) {
										idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
									}
								} else if (("destino").equals(filial) &&
										doctoServico.getFilialByIdFilialDestino() != null &&
										idPessoa.equals(doctoServico.getFilialByIdFilialDestino().getIdFilial().toString())) {
									idsSinistroDoctoServico.add(ids.getString("idSinistroDoctoServico"));
								}
						    }
						}  
			        	  
			        	//Verifica se existe um email
			        	if (!dsMail.equals("")) {
			            		//Gera o email...
				        		this.sendEmailComunicarUnidadesEmissaoRIM(idsSinistroDoctoServico, dsMail, filial,
				        				criteria.getString("nrProcessoSinistro"));
				        		//Atualiza a data de geracao do email...
				        		this.updateDataGeracaoEmailRim(idsSinistroDoctoServico);
			        	}
					}
	        	}
        	}
		}
	}
    
    /**
     * Realiza o envio do email e geracao do anexo.
     *
     * @param idDoctoServico
     * @param destinatario
     * @param filial
     * @param processoSinistro
     */
    private void sendEmailComunicarUnidadesEmissaoRIM(List idDoctoServico, String destinatario, String filial, String processoSinistro) {

		TypedFlatMap reportCriteria = new TypedFlatMap();

		reportCriteria.put("idsSinistroDoctoServico", idDoctoServico);
		reportCriteria.put("filial", filial);

		File anexo = null;

		try {
			anexo = reportExecutionManager.executeReport("lms.seguros.comunicarUnidadesEmissaoRimReportService", reportCriteria);
		} catch (Exception e) {
			// FIXME avaliar risco do try/catch
			log.error(e);
		}

	    // Pega a descricao do parametro geral
		String[] param = {processoSinistro};
		String assunto = configuracoesFacade.getMensagem("LMS-22022", param);
		String mensagem = "";
	    String remetenteLms = (this.getParametroGeralService().findByNomeParametro("REMETENTE_EMAIL_LMS", false)).getDsConteudo();
	    String nomeAnexo = anexo.getName();

		sendMail(destinatario, assunto, mensagem, remetenteLms, nomeAnexo, anexo);
		}

    /**
     * Calcula o valor diferencial indenizado reembolsado do Processo de Sinistro, 
     * conforme fórmula passada pela usuária chave Camila e validada por Gianfranco.
     * Este método não efetua store. É necessário chamar o método store manualmente.
     * @param processoSinistro
     */
    public void calculaVlDifeIndenizadoReembolsado(ProcessoSinistro processoSinistro) {

    	BigDecimal totalCustoAdicional            = new BigDecimal(0.00);
    	BigDecimal totalReembolsadoCustoAdicional = new BigDecimal(0.00);
    	
    	// itera sobre os custos adicionais do processo
    	for (Iterator it = custoAdicionalSinistroService.findByIdProcessoSinistro(processoSinistro.getIdProcessoSinistro()).iterator(); it.hasNext(); ) {
    		CustoAdicionalSinistro custoAdicionalSinistro = (CustoAdicionalSinistro)it.next();
    		
    		Long idMoedaCusto = custoAdicionalSinistro.getMoeda().getIdMoeda();
    		Long idMoedaSinistro = processoSinistro.getMoeda().getIdMoeda();
    		
    		// soma os valores de custo adicional e reembolso, e convertendo para a moeda do processo de sinistro
    		// e tomando como datas de cotacao as datas do custo adicional e do reembolso  
			if (!idMoedaCusto.equals(idMoedaSinistro)) {
				BigDecimal vlConvertidoCusto = converteValor(idMoedaCusto, idMoedaSinistro, custoAdicionalSinistro.getDtCustoAdicional(), custoAdicionalSinistro.getVlCustoAdicional());
				totalCustoAdicional = totalCustoAdicional.add(vlConvertidoCusto);

				if (custoAdicionalSinistro.getDtReembolsado()!=null && custoAdicionalSinistro.getVlReembolsado()!=null) {
					BigDecimal vlConvertidoReembolso = converteValor(idMoedaCusto, idMoedaSinistro, custoAdicionalSinistro.getDtReembolsado(), custoAdicionalSinistro.getVlReembolsado());
					totalReembolsadoCustoAdicional = totalReembolsadoCustoAdicional.add(vlConvertidoReembolso);
				}
				
			} else {				
				totalCustoAdicional = totalCustoAdicional.add(custoAdicionalSinistro.getVlCustoAdicional());
				if (custoAdicionalSinistro.getDtReembolsado()!=null && custoAdicionalSinistro.getVlReembolsado()!=null) { 
					totalReembolsadoCustoAdicional = totalReembolsadoCustoAdicional.add(custoAdicionalSinistro.getVlReembolsado());
				}
			}
    	}    	
    }

    /**
     * Converte o valor especificado na data de cotação especificada, da moeda origem para a moeda destino. 
     * @param idMoedaOrigem
     * @param idMoedaDestino
     * @param dtCotacao
     * @param valor
     * @return
     */
    private BigDecimal converteValor(Long idMoedaOrigem, Long idMoedaDestino, YearMonthDay dtCotacao, BigDecimal valor) {
    	
    	Long idPais = SessionUtils.getMoedaSessao().getIdMoeda();
 
    	try {
    		BigDecimal resultado = conversaoMoedaService.findConversaoMoeda(idPais, idMoedaOrigem, idPais, idMoedaDestino, dtCotacao, valor);
    		return resultado;
    		
    	} catch (BusinessException be) {
			Moeda moedaOrigem = moedaService.findById(idMoedaOrigem); 
			Moeda moedaDestino = moedaService.findById(idMoedaDestino);
			String data = JTFormatUtils.format(dtCotacao);
			String sgMoedaOrigem = moedaOrigem.getSiglaSimbolo();
			String sgMoedaDestino = moedaDestino.getSiglaSimbolo();
			String nmPais = SessionUtils.getPaisSessao().getNmPais().getValue();
			throw new BusinessException("LMS-22016", new Object[]{sgMoedaOrigem, sgMoedaDestino, data, nmPais});
		}
    }
    
    
    public void validateIsProcessoSinistroFechado(Long idProcessoSinistro) {
    	ProcessoSinistro processoSinistro = findById(idProcessoSinistro);
    	validateIsProcessoSinistroFechado(processoSinistro);
    }
    
    public void validateIsProcessoSinistroFechado(ProcessoSinistro processoSinistro) {
    	if (processoSinistro.getDhFechamento()!=null) {
    		throw new BusinessException("LMS-22019");
    	}
    }
    
    /**
     * Calcula o valor da indenização do processo de sinistro.
     * Em resumo, soma os valores das indenizacoes dos documentos de servico dos recibos de indenizacao do processo de sinistro,
     * convertendo os valores para a moeda especificada, na data do recibo de indenizacao.
     * @param idProcessoSinistro 
     * @param idMoedaDestino
     * @return
     */
    public BigDecimal findCalculaVlIndenizacaoProcesso(Long idProcessoSinistro, Long idMoedaDestino) {
    	BigDecimal vlIndenizacao = new BigDecimal(0.00);
		for (Iterator iter = sinistroDoctoServicoService.findSinistroDoctoServicoByIdProcessoSinistro(idProcessoSinistro).iterator(); iter.hasNext(); ) {
			SinistroDoctoServico sinistroDoctoServico = (SinistroDoctoServico)iter.next();
			if (!sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().isEmpty()) {
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)sinistroDoctoServico.getDoctoServico().getDoctoServicoIndenizacoes().get(0);
				ReciboIndenizacao reciboIndenizacao = doctoServicoIndenizacao.getReciboIndenizacao();

    			// soma os valores dos recibos de indenizacao dos documentos do processo,
    			// convertendo para a data de geracao do recibo de indenizacao e para 
    			// a moeda do processo de sinistro
    			Long idMoedaRim = doctoServicoIndenizacao.getMoeda().getIdMoeda();
    			if (!idMoedaRim.equals(idMoedaDestino)) {
    				BigDecimal vlConvertido = converteValor(idMoedaRim, idMoedaDestino, reciboIndenizacao.getDtGeracao(), doctoServicoIndenizacao.getVlIndenizado());
    				vlIndenizacao = vlIndenizacao.add(vlConvertido);
    			} else {
    				vlIndenizacao = vlIndenizacao.add(doctoServicoIndenizacao.getVlIndenizado());
    			}	
			}
		}
		return vlIndenizacao;
    }

    /**
     * Find que busca apenas a entidade ProcessoSinistro (sem fetch) a partir do id
     * @param idProcessoSinistro
     * @return
     */
    public ProcessoSinistro findProcessoSinistroById(Long idProcessoSinistro){
    	return getProcessoSinistroDAO().findProcessoSinistroById(idProcessoSinistro);
    }
    
    /**
     * Verifica a existência de um processo de sinistro de acordo com o número informado. 
     * @param nrProcessoSinistro
     * @return True, em caso afirmativo ou false em caso negativo. 
     */
    public boolean verifyByNrProcesso(String nrProcessoSinistro) {
    	return getProcessoSinistroDAO().verifyByNrProcesso(nrProcessoSinistro);
    }
    
    /**
     * Solicitação CQPRO00006044 da Integração.
     * Retorna um processo de sinistro de acordo com os números informados. 
     * @param nrProcessoSinistro
     * @return 
     */
    public ProcessoSinistro findProcessoSinistro(String nrProcessoSinistro) {
    	return getProcessoSinistroDAO().findProcessoSinistro(nrProcessoSinistro);
    }    

	public void setCustoAdicionalSinistroService(
			CustoAdicionalSinistroService custoAdicionalSinistroService) {
		this.custoAdicionalSinistroService = custoAdicionalSinistroService;
	}
	
    /**
     * Método que retorna uma Lista de ProcessoSinistro a partir de um ID de DoctoServico.
     * 
     * @param idDoctoServico
     * @return
     */
    public List findProcessoSinistroByIdDoctoServico(Long idDoctoServico) {
    	return this.getProcessoSinistroDAO().findProcessoSinistroByIdDoctoServico(idDoctoServico);
    }

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}	
	
	/**
	 * Faz a validacao do PCE para a tela de comunicarClienteCartaOcorrencia
	 * 
	 * @param idsDoctoServicos
	 * @param cdProcesso
	 * @param cdEvento
	 * @param cdOcorrencia
	 */
	public TypedFlatMap validatePCE(List idsDoctoServicos, Long cdProcesso, Long cdEvento, Long cdOcorrencia) {

		final String dmTtipoDocumento = "DM_TIPO_DOCUMENTO";
		
		List resultList = new ArrayList();
		List<String> tiposDocumento = new ArrayList<String>();
       	
       	for (DomainValue domainValue : (List<DomainValue>)domainValueService.findDomainValues(dmTtipoDocumento)) {
       		tiposDocumento.add(domainValue.getValue());
		}
		
		for (Iterator iter = idsDoctoServicos.iterator(); iter.hasNext();) {
			Long idDoctoServico = (Long) iter.next();
			
			DoctoServico doctoServico = this.getDoctoServicoService().findById(idDoctoServico);
			
			if (doctoServico != null && tiposDocumento.contains(doctoServico.getTpDocumentoServico().getValue())) {
				
				if (doctoServico.getClienteByIdClienteRemetente()!=null) {
					resultList.add(this.getVersaoDescritivoPceService()
							.validatePCE(doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
									cdProcesso, cdEvento, cdOcorrencia));
				}
				
				if (doctoServico.getClienteByIdClienteDestinatario()!=null) {
					resultList.add(this.getVersaoDescritivoPceService()
							.validatePCE(doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
									cdProcesso, cdEvento, cdOcorrencia));
				}	
			}
		}
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("list", resultList);
		return result;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	public ReciboIndenizacaoService getReciboIndenizacaoService() {
		return reciboIndenizacaoService;
	}

	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param tfm indica a entidade que deverá ser removida.
	 */
	public void removeById(TypedFlatMap tfm) {
		ProcessoSinistro processoSinistro = findById(tfm.getLong("idProcessoSinistro"));
		
		if(processoSinistro != null){
			
			// Atualiza RECIBO_INDENIZACAO.ID_PROCESSO_SINISTRO = NULL
			reciboIndenizacaoService.updateReciboIndenizacaoByIdProcessoSinistro(processoSinistro.getIdProcessoSinistro());
			
			// Excluir registros de SINISTRO_DOCTO_SERVICO 
			sinistroDoctoServicoService.removeByIdProcessoSinistro(processoSinistro.getIdProcessoSinistro());
			
			// Excluir PROCESSO_SINISTRO 
			super.removeById(processoSinistro.getIdProcessoSinistro());
			
		}
		
	}
	
	/**
	 * LMS-6180 - Retorna lista de documentos para a popup com base nos filtros informados
	 * @param tfm
	 */
	public ResultSetPage findPaginatedDocumentosPopup(TypedFlatMap tfm){
		return getProcessoSinistroDAO().findPaginatedDocumentosPopup(tfm, FindDefinition.createFindDefinition(tfm));
	}
	
	public List findAllDocuments(TypedFlatMap tfm){
		return getProcessoSinistroDAO().findAllDocuments(tfm);
	}
	
	public Integer getRowCountDocumentosPopup(TypedFlatMap tfm){
		return getProcessoSinistroDAO().getRowCountDocumentosPopup(tfm);
	}
	
	//LMS-6178
	public BigDecimal findSomaValoresMercadoria(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) getProcessoSinistroDAO().findSomaValoresMercadoria(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}

	//LMS-6178
	public BigDecimal findSomaValoresPrejuizo(Long idProcessoSinistro) {
		return this.findSomaValoresPrejuizoSinistroDoctoService(idProcessoSinistro).add(this.findSomaValoresPrejuizoCustoAdiconalSinistro(idProcessoSinistro));
	}
	
	//LMS-6611
	public BigDecimal findSomaValoresPrejuizoProprio(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) sinistroDoctoServicoService.findSomaValoresPrejuizoProprio(idProcessoSinistro).get(0);
		
		if(resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}
	
	//LMS-6178
	private BigDecimal findSomaValoresPrejuizoSinistroDoctoService(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) sinistroDoctoServicoService.findSomaValoresPrejuizo(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}
	
	//LMS-6178
	private BigDecimal findSomaValoresPrejuizoCustoAdiconalSinistro(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) custoAdicionalSinistroService.findSomaValoresPrejuizo(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}
	
	//LMS-6178
	public BigDecimal findSomaValoresIndenizado(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) getProcessoSinistroDAO().findSomaValoresIndenizado(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}

	//LMS-6178
	public BigDecimal findSomaValoresReembolso(Long idProcessoSinistro) {
		return this.findSomaValoresReembolsoReciboReembolso(idProcessoSinistro).add(this.findSomaValoresReembolsoCustoAdicional(idProcessoSinistro));
	}
	
	//LMS-6178
	private BigDecimal findSomaValoresReembolsoReciboReembolso(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) reciboReembolsoProcessoService.findSomaValoresReembolsoReciboReembolso(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}
	
	//LMS-6178
	private BigDecimal findSomaValoresReembolsoCustoAdicional(Long idProcessoSinistro) {
		BigDecimal resultado = (BigDecimal) custoAdicionalSinistroService.findSomaValoresReembolso(idProcessoSinistro).get(0);
		
		if (resultado == null) {
			resultado = BigDecimal.ZERO;
		}
		
		return resultado;
	}
	
	/**
	 * Carrega um map com os dados do processo de sinistro 
	 * para a tela Manter Processo Sinistro
	 * @param idProcessoSinistro
	 * @return
	 */
	public TypedFlatMap findByManterProcessoSinistro(Long idProcessoSinistro) {
		
		ProcessoSinistro processoSinistro = this.findById(idProcessoSinistro);
		
		TypedFlatMap tfm = new TypedFlatMap();
		
		tfm.put("idProcessoSinistro", idProcessoSinistro);
		tfm.put("nrProcessoSinistro", processoSinistro.getNrProcessoSinistro());
		
		tfm.put("nrBoletimOcorrencia", processoSinistro.getNrBoletimOcorrencia());
		tfm.put("dhSinistro", processoSinistro.getDhSinistro());
		tfm.put("tipoSinistro.idTipoSinistro", processoSinistro.getTipoSinistro().getIdTipoSinistro());
		tfm.put("tipoSeguro.idTipoSeguro", processoSinistro.getTipoSeguro().getIdTipoSeguro());
		tfm.put("dsSinistro", processoSinistro.getDsSinistro());
		tfm.put("localSinistro.value", processoSinistro.getTpLocalSinistro().getValue());
		
		if (processoSinistro.getRodovia() != null) {
    		tfm.put("rodovia.idRodovia", processoSinistro.getRodovia().getIdRodovia());
    		tfm.put("rodovia.dsRodovia", processoSinistro.getRodovia().getDsRodovia());
    		tfm.put("rodovia.sgRodovia", processoSinistro.getRodovia().getSgRodovia());
		}
		
		tfm.put("nrKmSinistro", processoSinistro.getNrKmSinistro());
		
		if (processoSinistro.getFilial() != null) {
    		tfm.put("filialSinistro.idFilial", processoSinistro.getFilial().getIdFilial());
    		tfm.put("filialSinistro.sgFilial", processoSinistro.getFilial().getSgFilial());
    		tfm.put("filial.pessoa.nmFantasia", processoSinistro.getFilial().getPessoa().getNmFantasia());
		}
		
		if (processoSinistro.getAeroporto() != null) {
    		tfm.put("aeroporto.idAeroporto", processoSinistro.getAeroporto().getIdAeroporto());
    		tfm.put("aeroporto.sgAeroporto", processoSinistro.getAeroporto().getSgAeroporto());
    		tfm.put("aeroporto.pessoa.nmPessoa", processoSinistro.getAeroporto().getPessoa().getNmPessoa());
		}
		
		if(processoSinistro.getSituacaoReembolso() != null) {
			tfm.put("situacaoReembolso.idSituacaoReembolso", processoSinistro.getSituacaoReembolso().getIdSituacaoReembolso());
		}
		
		if(processoSinistro.getMotorista() != null) {
			tfm.put("motorista.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(processoSinistro.getMotorista().getPessoa()));
			tfm.put("motorista.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(processoSinistro.getMotorista().getPessoa()));
			tfm.put("motorista.pessoa.nmPessoa", processoSinistro.getMotorista().getPessoa() == null ? null :
					processoSinistro.getMotorista().getPessoa().getNmPessoa());
			tfm.put("motorista.tpVinculo", processoSinistro.getMotorista().getTpVinculo().getDescriptionAsString());
		}
		
		if(processoSinistro.getMeioTransporte() != null) {
			tfm.put("meioTransporteRodoviario2.meioTransporte.nrFrota", processoSinistro.getMeioTransporte().getNrFrota());
			tfm.put("meioTransporteRodoviario.meioTransporte.nrIdentificador", processoSinistro.getMeioTransporte().getNrIdentificador());
			tfm.put("meioTransporteRodoviario.idMeioTransporte", processoSinistro.getMeioTransporte().getIdMeioTransporte());
			tfm.put("tpVeiculo", getMeioTransporteService().findTipoVeiculoByIdMeioTransporte(processoSinistro.getMeioTransporte().getIdMeioTransporte()));
			tfm.put("nrCertificado", processoSinistro.getMeioTransporte().getMeioTransporteRodoviario().getNrCertificado());
		}
		
		tfm.put("municipio.idMunicipio", processoSinistro.getMunicipio().getIdMunicipio());
		tfm.put("municipio.nmMunicipio", processoSinistro.getMunicipio().getNmMunicipio());
		tfm.put("municipio.unidadeFederativa.sgUnidadeFederativa", processoSinistro.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		
		tfm.put("moeda.idMoeda", processoSinistro.getMoeda().getIdMoeda());
		
		tfm.put("dsLocalSinistro", processoSinistro.getDsLocalSinistro());
		tfm.put("obSinistro", processoSinistro.getObSinistro());
		tfm.put("dsComunicadoCorretora", processoSinistro.getDsComunicadoCorretora());
		
		tfm.put("usuarioAbertura.idUsuario", processoSinistro.getUsuarioAbertura().getIdUsuario());
		tfm.put("usuarioAbertura.nmUsuario", processoSinistro.getUsuarioAbertura().getNmUsuario());
		
		tfm.put("dhAbertura", processoSinistro.getDhAbertura());
		
		if (processoSinistro.getDhFechamento()!=null) {
			Integer tempoPagamento = Integer.valueOf(JTDateTimeUtils.getIntervalInDays(processoSinistro.getDhSinistro().toYearMonthDay(), processoSinistro.getDhFechamento().toYearMonthDay()));
			tfm.put("tempoPagamento", tempoPagamento);
			tfm.put("dhFechamento", processoSinistro.getDhFechamento());
			tfm.put("situacao", getMessage("fechado"));
			tfm.put("situacaoHidden", "F");
		} else {
			tfm.put("situacao", getMessage("aberto"));
			tfm.put("situacaoHidden", "A");    			
		}
		
		tfm.put("vlFranquia", processoSinistro.getVlFranquia());
		
		return tfm;
	}
	
	public List findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo(TypedFlatMap tfm){
		return getProcessoSinistroDAO().findIdsSinistroDoctoServicoByIdProcessoSinistroComPrejuizo(tfm);
	}
}

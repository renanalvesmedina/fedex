package com.mercurio.lms.indenizacoes.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.CotacaoIndicadorFinanceiroService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.EventoRim;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.MotivoCancelamentoRim;
import com.mercurio.lms.indenizacoes.model.ParcelaReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.ReciboIndenizacaoDAO;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.portaria.model.service.AcaoIntegracaoEventosService;
import com.mercurio.lms.portaria.model.service.AcaoIntegracaoService;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.reciboIndenizacaoService"
 */
public class ReciboIndenizacaoService extends CrudService<ReciboIndenizacao, Long> {
	
	private EventoService eventoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private MoedaService moedaService;
	private FilialService filialService;
	private EmpresaService empresaService;	
	private EventoRimService eventoRimService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialDebitadaService filialDebitadaService;
	private DomainValueService domainValueService;
	private NaoConformidadeService naoConformidadeService;	
	private ConversaoMoedaService conversaoMoedaService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ReciboIndenizacaoNfService reciboIndenizacaoNfService;	
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	private MotivoCancelamentoRimService motivoCancelamentoRimService;
	private ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService;
	private CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private HistoricoFilialService historicoFilialService;
	private ParametroGeralService parametroGeralService;
	private AcaoIntegracaoService acaoIntegracaoService;
	private AcaoIntegracaoEventosService acaoIntegracaoEventosService;
	private PessoaService pessoaService;
	private ClienteService clienteService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private DoctoServicoService doctoServicoService;
	private DevedorDocServService devedorDocServService;

	private final String INFORMAR_PAGAMENTO = "IP";
	private final String LIBERAR_PAGAMENTO = "LP";
	private final String CANCELAR_PAGAMENTO = "CP";
	private final String CANCELAR_LIBERACAO = "CL";

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setCotacaoIndicadorFinanceiroService(CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService) {
		this.cotacaoIndicadorFinanceiroService = cotacaoIndicadorFinanceiroService;
	}
	public void setDoctoServicoIndenizacaoService(DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setEventoRimService(EventoRimService eventoRimService) {
		this.eventoRimService = eventoRimService;
	}
	public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
		this.filialDebitadaService = filialDebitadaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setMotivoCancelamentoRimService(MotivoCancelamentoRimService motivoCancelamentoRimService) {
		this.motivoCancelamentoRimService = motivoCancelamentoRimService;
	}
	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public void setParcelaReciboIndenizacaoService(ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService) {
		this.parcelaReciboIndenizacaoService = parcelaReciboIndenizacaoService;
	}
	public void setReciboIndenizacaoNfService(ReciboIndenizacaoNfService reciboIndenizacaoNfService) {
		this.reciboIndenizacaoNfService = reciboIndenizacaoNfService;
	}
	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * @param Instância do DAO.
     */
    public void setReciboIndenizacaoDAO(ReciboIndenizacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     * @return Instância do DAO.
     */
    private ReciboIndenizacaoDAO getReciboIndenizacaoDAO() {
        return (ReciboIndenizacaoDAO) getDao();
    }
    
	/**
	 * 
	 * @param tfm
	 * @return
	 */
	public ResultSetPage findPaginatedReciboIndenizacao(TypedFlatMap tfm) {
    	return getReciboIndenizacaoDAO().findPaginatedReciboIndenizacao(tfm, FindDefinition.createFindDefinition(tfm));
    }

	/**
	 * 
	 * @param tfm
	 * @return
	 */
    public Integer getRowCountReciboIndenizacao(TypedFlatMap tfm) {
    	return getReciboIndenizacaoDAO().getRowCountReciboIndenizacao(tfm);
    }

    /**
     * Find de ReciboIndenizacao sem fazer nenhum fetch.
     * @param idReciboIndenizacao
     * @return
     */
    public ReciboIndenizacao findReciboIndenizacaoById(Long idReciboIndenizacao) {
    	return getReciboIndenizacaoDAO().findReciboIndenizacaoById(idReciboIndenizacao);
    }
    
    /**
     * Atualiza TP_STATUS_INDENIZACAO ref. pagamento manual de indenização
     * 
     * @param idReciboIndenizacao
     * @param tpStatusIndenizacao
     */
    public void updateReciboIndenizacaRimPago(Long idReciboIndenizacao, String tpStatusIndenizacao) {
		
    	getReciboIndenizacaoDAO().updateReciboIndenizacaRimPago(idReciboIndenizacao, tpStatusIndenizacao);
	}

    public void updateReciboIndenizacaoByIdProcessoSinistro(Long idProcessoSinistro){
    	getReciboIndenizacaoDAO().updateReciboIndenizacaoByIdProcessoSinistro(idProcessoSinistro);
    }
    
    /**
     * Solicitação CQPRO00006043 da Integração.
     * Busca uma instância de ReciboIndenizacao com base na filial e número do mesmo.
     * @param idFilial
     * @param nrReciboIndenizacao
     * @return
     */
    public ReciboIndenizacao findByIdFilialNrReciboIndenizacao(Long idFilial, Integer nrReciboIndenizacao) {
    	return getReciboIndenizacaoDAO().findByIdFilialNrReciboIndenizacao(idFilial, nrReciboIndenizacao);
    }
    
    public ReciboIndenizacao findReciboIndenizacaoByIdDoctoServico(Long idDoctoServico) {
    	return getReciboIndenizacaoDAO().findReciboIndenizacaoByIdDoctoServico(idDoctoServico);
    }

	/**
	 * Recupera uma instância de <code>ReciboIndenizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ReciboIndenizacao findById(java.lang.Long id) {
        return (ReciboIndenizacao)getReciboIndenizacaoDAO().findReciboIndenizacaoByIdReciboIndenizacao(id);
    }

    /**
     * Solicitação CQPRO00006180 da Integração.
     * Método que retorna uma lista de ReciboIndenizacao conforme o filtro.
     * @param tpStatusIndenizacao
     * @param dtProgramadaPagamentoLimite
     * @param dtDevolucaoBancoLimite
     * @return
     */
    public List findReciboIndenizacao(String tpStatusIndenizacao, YearMonthDay dtProgramadaPagamento, YearMonthDay dtProgramadaPagamentoLimite, YearMonthDay dtDevolucaoBancoLimite){
    	return getReciboIndenizacaoDAO().findReciboIndenizacao(tpStatusIndenizacao, dtProgramadaPagamento, dtProgramadaPagamentoLimite, dtDevolucaoBancoLimite);
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

	@Override
	protected ReciboIndenizacao beforeStore(ReciboIndenizacao reciboIndenizacao) {
		/*
		 * LMS-3748: Se existir mais de um documento de serviço para o recibo, ou seja, mais de um registro na tabela DOCTO_SERVICO_INDENIZACAO,
		 * exibir a mensagem LMS-21089.
		 */
		if (reciboIndenizacao.getIdReciboIndenizacao() != null){
			Integer documentos = doctoServicoIndenizacaoService.getRowCountDoctoServicosByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
			if(documentos != null && documentos.intValue() > 1){
				throw new BusinessException("LMS-21089");
			}
		}
		
		return super.beforeStore(reciboIndenizacao);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ReciboIndenizacao bean) {
        return super.store(bean);
    }
    
    /**
     * Método store da tela Incluir Recibos de Indenização
     * @param reciboIndenizacao
     * @param itemList
     * @param itemListConfig
     * @param tfm
     * @return
     */
    public java.io.Serializable storeIncluir(ReciboIndenizacao reciboIndenizacao, ItemList itensDocto, ItemList itensMda, ItemList itensParcela, ItemList itensAnexoRim, ItemListConfig configDocto, TypedFlatMap tfm) {
    	// identifica se é uma operacao de store ou update
		boolean isUpdate = (reciboIndenizacao.getIdReciboIndenizacao() != null);
    	// identifica se deve efetuar rollback
       	boolean rollbackMaster = !isUpdate;
       	
       	// tipo de forma de pagamento
       	String tpFormaPagto = tfm.getDomainValue("tpFormaPagamento").getValue();

       	if(itensDocto != null && itensDocto.size() > 1){
			throw new BusinessException("LMS-21089");
		}
       	
       	for(Iterator iter = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); iter.hasNext();) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
			// inclui evento de geração para o documento de servico
			DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
			Long idCliente = tfm.getLong("clienteBeneficiario.idCliente");
			String escolhido = tfm.getDomainValue("tpBeneficiarioIndenizacao").getValue();
			
			validateIntegranteFrete(doctoServico, idCliente, escolhido);
       	}
			
    	try {
	    	if(isUpdate) {				
	    		filialDebitadaService.removeByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
	    		parcelaReciboIndenizacaoService.removeParcelasByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
			}
	    	else {
	    		// se não for update, gera o número do recibo de reembolso
    			Long maxNrRecibo = configuracoesFacade.incrementaParametroSequencial(reciboIndenizacao.getFilial().getIdFilial(), "NR_REC_INDENIZACAO", true);
    			reciboIndenizacao.setNrReciboIndenizacao(Integer.valueOf(maxNrRecibo.intValue()));
	    	}

	    	//Rotina: Salvar Filiais Debitadas
	    	BigDecimal vlSalarioMinimo = getVlSalarioMinimo();//valida se tem salário minimo cadastrado.
	       	List filiaisDebitadas = this.storeFiliaisDebitadas(reciboIndenizacao, itensDocto, configDocto, isUpdate, vlSalarioMinimo);

	    	getReciboIndenizacaoDAO().storeAll(reciboIndenizacao, itensDocto, itensMda, itensParcela, itensAnexoRim, configDocto, filiaisDebitadas);

			reciboIndenizacao.setTpSituacaoWorkflow(new DomainValue("E"));
	    	
			Pendencia pendencia = executeWorkflowPendencia(reciboIndenizacao);
	    	
	    	reciboIndenizacao.setPendencia(pendencia);
	    	getReciboIndenizacaoDAO().store(reciboIndenizacao);	    	
	    	
	    	/***************************************************************************************************************/
	    	
	    	// se a forma de pagamento for boleto bancario
	    	if (("BO").equals(tpFormaPagto)) {
	    		int qtParcelasBoleto = tfm.getByte("qtParcelasBoletoBancario").intValue();
	    		// e se o número de parcelas for 1, então salva uma parcela do recibo de indenizacao
				if ( qtParcelasBoleto == 1) {
					YearMonthDay dtVencto = tfm.getYearMonthDay("dtVencimento");
					String nrBoleto = tfm.getString("nrBoleto");
					storeParcelaReciboIndenizacao(reciboIndenizacao, dtVencto, nrBoleto);
				}
			}
				
			// se estiver salvando pela primeira vez, então gera o evento de rim
			if (!isUpdate) {
				generateEventoRim(reciboIndenizacao, "GE", null);
			}
			
	    	// calcula o valor total indenizado e a quantidade total de valores, somando os valores de indenizacao da itemList e convertendo para a moeda do usuário
			getReciboIndenizacaoDAO().getAdsmHibernateTemplate().flush();
			String strRim = configuracoesFacade.getMensagem("rim");
			for(Iterator iter = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); iter.hasNext();) {
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
				// inclui evento de geração para o documento de servico
				DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
				String strDocumento = reciboIndenizacao.getFilial().getSgFilial() + " " + StringUtils.leftPad(reciboIndenizacao.getNrReciboIndenizacao().toString(), 8, '0');
				
				Short codEvento;
				// Se o número de volumes indenizados for igual ao número de volumes do documento, 
				// passar o valor 35, senão passar o valor 132
				if (doctoServicoIndenizacao.getQtVolumes().equals(doctoServico.getQtVolumes())){
					codEvento = ConstantesSim.EVENTO_GERACAO_RECIBO_INDENIZACAO; 
				} else {
					codEvento = ConstantesSim.EVENTO_GERACAO_RIM_SEM_LOCALIZACAO;
				}
				incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
						codEvento,
						doctoServico.getIdDoctoServico(),
						SessionUtils.getFilialSessao().getIdFilial(),
						strDocumento,
						JTDateTimeUtils.getDataHoraAtual(),
						null,
						SessionUtils.getFilialSessao().getSiglaNomeFilial(),
						strRim);
				
				// se for um update remove as notas fiscais
				if (isUpdate) {
					reciboIndenizacaoNfService.removeByIdDoctoServicoIndenizacao(doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
				}				
			}
			
			/*
			 * Para cada Documento de Serviço do RIM 
			 * Verifica e insere registros de bloqueio para o documento de serviço quando necessário.
			 */
			this.storeBloqueiosParaDocumentosServicoRIM(reciboIndenizacao, itensDocto, configDocto);
			
			return reciboIndenizacao.getIdReciboIndenizacao();
    	} catch (RuntimeException re) {
			this.rollbackMasterState(reciboIndenizacao, rollbackMaster, re);
            itensDocto.rollbackItemsState();
    		throw re;
		}    	
    }
    
    private void validateIntegranteFrete (DoctoServico doctoServico, Long idCliente, String tpIntegrante){
    	final String CONSIGNATARIO = "C";
		
		final String DESTINATARIO = "D";
		final String REMETENTE = "R";
		final String TERCEIRO = "T";
		final String DEVEDORDOCUMENTO = "V";
		
		boolean isDevedorDocumento = devedorDocServService.getRowCountByIdDoctoServicoIdCliente(doctoServico.getIdDoctoServico(), idCliente).intValue() > 0;    	
		boolean isRemetente = (doctoServico.getClienteByIdClienteRemetente()!=null && doctoServico.getClienteByIdClienteRemetente().getIdCliente().equals(idCliente));
		boolean isDestinatario = (doctoServico.getClienteByIdClienteDestinatario()!=null && doctoServico.getClienteByIdClienteDestinatario().getIdCliente().equals(idCliente));
		boolean isConsignatario = (doctoServico.getClienteByIdClienteConsignatario()!=null && doctoServico.getClienteByIdClienteConsignatario().getIdCliente().equals(idCliente));
		
		String preenchido  = isDevedorDocumento 		? DEVEDORDOCUMENTO 	: "";
		preenchido += isDestinatario 			? DESTINATARIO 		: "";
		preenchido += isRemetente 				? REMETENTE 		: "";
		preenchido += isConsignatario 			? CONSIGNATARIO		: "";
		preenchido += preenchido.length() == 0 	? TERCEIRO 			: ""; 
		
		if(preenchido.equals(TERCEIRO)){
			if(!tpIntegrante.equals(TERCEIRO)){
				throw new BusinessException("LMS-21046");
			}
		}else{
			if(tpIntegrante.equals(TERCEIRO)){
				throw new BusinessException("LMS-21058");	
			}
			if(!preenchido.contains(tpIntegrante)){
				throw new BusinessException("LMS-21059", new Object[] { domainValueService.findDomainValueByValue("DM_BENEFICIARIO_INDENIZACAO", preenchido.substring(0, 1)).getDescriptionAsString() });
			}
		}
    }
   
    
    /**
     * Verifica e insere registros de bloqueio para o documento de serviço quando necessário.
     * 
     * @param reciboIndenizacao
     * @param itensDocto
     * @param configDocto
     */
	private void storeBloqueiosParaDocumentosServicoRIM(
			ReciboIndenizacao reciboIndenizacao, ItemList itensDocto,
			ItemListConfig configDocto) {
		//Para cada Documento de Serviço do RIM.
		for(Iterator iter = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); iter.hasNext();) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
			
			DoctoServico doctoServico = doctoServicoService.findById(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			
			//Verificar quantidade de bloqueios em aberto para o documento de serviço.
			Integer numeroOcorrenciaDoctoServicoEmAberto = ocorrenciaDoctoServicoService.findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
			
			//Se possuir apenas um bloqueio em aberto. 
			if ((Integer.valueOf(1)).compareTo(numeroOcorrenciaDoctoServicoEmAberto) == 0) {
				OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
				Short cdOcorrencia = ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia();
				//verificar se não  pode ser liberado pelo código 95
				Boolean podeLiberar = ocorrenciaDoctoServicoService.executeVerificarLiberacaoByCodigo(cdOcorrencia);
				if ( Boolean.FALSE.equals(podeLiberar)){ 
					OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaDoctoServicoService.findOcorrenciaLiberacaoByOcorrenciaBloqueada(cdOcorrencia);
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia, doctoServico, JTDateTimeUtils.getDataHoraAtual());
					
					//inserir um bloqueio com código 97, utilizando a data e hora atuais mais 1 minuto
					OcorrenciaPendencia ocorrenciaPendencia97 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("97"));
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia97, doctoServico, JTDateTimeUtils.getDataHoraAtual().plusMinutes(1));
				}
			}
			//Se não existir bloqueios em aberto, inserir um bloqueio com código 97.
			if ((Integer.valueOf(0)).compareTo(numeroOcorrenciaDoctoServicoEmAberto) == 0) {
				OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("97"));
				ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia, doctoServico, JTDateTimeUtils.getDataHoraAtual());
			}
		}
	}
    
    /**
     * Rotina: Salvar Filiais Debitadas
     * 
     * @param reciboIndenizacao
     * @param itensDocto
     * @param configDocto
     * @param isUpdate
     * @param filiaisDebitadas
     */
	private List storeFiliaisDebitadas(ReciboIndenizacao reciboIndenizacao,
			ItemList itensDocto, ItemListConfig configDocto, boolean isUpdate, BigDecimal vlSalarioMinimo) {
       	List filiaisDebitadas = new ArrayList();
		
		boolean possuiFilialParceira = false;
		if ("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())) {

			String siglaNumero = FormatUtils.formatSgFilialWithLong(
					reciboIndenizacao.getFilial().getSgFilial(), Long.valueOf(reciboIndenizacao.getNrReciboIndenizacao().intValue()), "00000000");
			String dsDisposicao = configuracoesFacade.getMensagem("LMS-21055", new Object[] {siglaNumero});

			// iterando sobre os documentos de servico indenizados
			for (Iterator it = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); it.hasNext();) {
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
				
				if (doctoServicoIndenizacao.getDoctoServico().getNaoConformidades()!=null) {
					NaoConformidade naoConformidade = (NaoConformidade)doctoServicoIndenizacao.getDoctoServico().getNaoConformidades().get(0);
					List ocorrencias = ocorrenciaNaoConformidadeService.findOcorrenciasByIdNaoConformidade(naoConformidade.getIdNaoConformidade());
					// determinando se possui filial responsavel parceira 
					for(Iterator iter=ocorrencias.iterator(); iter.hasNext(); ) {
						OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade) iter.next();
						if (ocorrenciaNaoConformidade.getFilialByIdFilialResponsavel().getEmpresa().getTpEmpresa().getValue().equals("P")) {
							possuiFilialParceira=true;
						}
					}
					// verificando o atributo blMaisUmaOcorrencia, setando true, caso satistaça a validação
					if (ocorrencias.size()>1) {
						reciboIndenizacao.setBlMaisUmaOcorrencia(Boolean.TRUE);
					}
				}

				// altera o status da nao conformidade para 'ROI' (indenizado) e suas ocorrências para 'F' (fechado)
				naoConformidadeService.executeUpdateStatusNaoConformidadeByIncluirRIM(
						doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico(), "ROI", "F", dsDisposicao);
			}
		}
			
		// se posssui ocorrencia de nao conformidade com filial responsável 
		// sendo parceira, então salva um registro com 100% para a matriz
		if ("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue()) && possuiFilialParceira) {
			Filial matriz = findFilialMatriz();
	    	FilialDebitada filialDebitada = new FilialDebitada();
	    	filialDebitada.setFilial(matriz);
	    	filialDebitada.setReciboIndenizacao(reciboIndenizacao);
	    	filialDebitada.setPcDebitado(BigDecimalUtils.HUNDRED);
	    	filiaisDebitadas.add(filialDebitada);
	    	
		} else {
			if ("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())) {
				BigDecimal meioVlSalarioMinimo = vlSalarioMinimo.divide(BigDecimal.valueOf(2));

				FilialDebitada filialDebitada = new FilialDebitada();
				Filial filial = null;
				Iterator it = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(),configDocto);
				if (it.hasNext()) {
					DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) it.next();
					if (doctoServicoIndenizacao.getOcorrenciaNaoConformidade() != null
							&& doctoServicoIndenizacao.getOcorrenciaNaoConformidade().getFilialByIdFilialResponsavel() != null) {
						OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService
								.findById(doctoServicoIndenizacao.getOcorrenciaNaoConformidade().getIdOcorrenciaNaoConformidade());
						filial = ocorrenciaNaoConformidade.getFilialByIdFilialResponsavel();
					}
				}
				if (filial == null) {
					filial = findFilialMatriz();
				}
				filialDebitada.setFilial(filial);
				filialDebitada.setReciboIndenizacao(reciboIndenizacao);
				filialDebitada.setPcDebitado(BigDecimalUtils.HUNDRED);
				filiaisDebitadas.add(filialDebitada);

			} else {
				// salva uma filial debitada com 100% para a filial matriz
				Filial matriz = findFilialMatriz();
				FilialDebitada filialDebitada = new FilialDebitada();
				filialDebitada.setFilial(matriz);
				filialDebitada.setReciboIndenizacao(reciboIndenizacao);
				filialDebitada.setPcDebitado(BigDecimalUtils.HUNDRED);
				filiaisDebitadas.add(filialDebitada);
			}
		}
		return filiaisDebitadas;
	}
    
    /**
     * Verifica se o vlIndenizacao se enquadra na faixa passada por parâmetro 
     * @param vlIndenizacao
     * @param nivelFaixa
     * @return
     */
    private boolean validateFaixaIndenizacao(BigDecimal vlIndenizacao, String nivelFaixa, BigDecimal vlSalarioMinimo) {
    	return validateFaixaIndenizacao(vlIndenizacao, nivelFaixa, vlSalarioMinimo, true);
    }
    private boolean validateFaixaIndenizacao(BigDecimal vlIndenizacao, String nivelFaixa, BigDecimal vlSalarioMinimo, boolean validaFinal) {
    	BigDecimal vlFaxaiInicial = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("VL_INDENIZACAO_FAIXA".concat(nivelFaixa).concat("_INICIAL"), false);
    	BigDecimal vlFaixaFinal = null;
    	if (validaFinal) {
    		vlFaixaFinal = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("VL_INDENIZACAO_FAIXA".concat(nivelFaixa).concat("_FINAL"), false);
    	}
		if(BigDecimalUtils.hasValue(vlFaixaFinal)) {
			if(CompareUtils.between(vlIndenizacao, vlFaxaiInicial.multiply(vlSalarioMinimo), vlFaixaFinal.multiply(vlSalarioMinimo))) {
				return true;
			}
		} else if(CompareUtils.ge(vlIndenizacao, vlFaxaiInicial.multiply(vlSalarioMinimo))) {
			return true;
		}
		return false;
    }
    
    
    /**
     * Retorna a mensagem para dsProcesso do workflow passando com parâmetros os dado do reciboIndenizacao informado.
     * 
	 * Mensagem: "RIM: TP_INDENIZACAO SG_FILIAL NR_RECIBO_INDENIZACAO, Valor: DS_SIMBOLO VL_INDENIZACAO, Favorecido: NR_IDENTIFICACAO NM_PESSOA" 
	 * 
	 * Onde:
	 * - TP_INDENIZACAO: RECIBO_INDENIZACAO.TP_INDENIZACAO (domínio DM_TIPO_INDENIZACAO) 
	 * - SG_FILIAL: RECIBO_INDENIZACAO.ID_FILIAL -> FILIAL.SG_FILIAL
	 * - NR_RECIBO_INDENIZACAO: RECIBO_INDENIZACAO.NR_RECIBO_INDENIZACAO 
	 * - DS_SIMBOLO: RECIBO_INDENIZACAO.ID_MOEDA -> MOEDA.DS_SIMBOLO 
	 * - VL_INDENIZACAO: RECIBO_INDENIZACAO.VL_INDENIZACAO 
	 * - NR_IDENTIFICACAO: RECIBO_INDENIZACAO.ID_FAVORECIDO -> PESSOA.NR_IDENTIFICACAO 
	 * - NM_PESSOA: RECIBO_INDENIZACAO.ID_FAVORECIDO -> PESSOA.NM_PESSOA
	 * 
	 * @param reciboIndenizacao
	 * @return
	 */
    public String getDsProcessoWorkflowParaReciboIndenizacao(ReciboIndenizacao reciboIndenizacao){
    	List<String> parametros = new ArrayList<String>();

    	String tipoIndenizacao = null;
    	if(reciboIndenizacao.getTpIndenizacao() != null && reciboIndenizacao.getTpIndenizacao().getDescriptionAsString() != null && !"".equals(reciboIndenizacao.getTpIndenizacao().getDescriptionAsString())){
    		tipoIndenizacao = reciboIndenizacao.getTpIndenizacao().getDescriptionAsString();
    	} else {    	
    		tipoIndenizacao = domainValueService.findDomainValueByValue("DM_TIPO_INDENIZACAO", reciboIndenizacao.getTpIndenizacao().getValue()).getDescriptionAsString();
    	}
    	parametros.add(tipoIndenizacao);
    	
    	parametros.add(reciboIndenizacao.getFilial().getSgFilial());
    	parametros.add(reciboIndenizacao.getNrReciboIndenizacao().toString());
    	parametros.add(reciboIndenizacao.getMoeda().getDsSimbolo());
    	parametros.add(new DecimalFormat("###,###,###,##0.00").format(reciboIndenizacao.getVlIndenizacao()));
    	
    	Pessoa pessoaFavorecido = reciboIndenizacao.getPessoaByIdFavorecido();
    	if(pessoaFavorecido != null){ 
    		if(pessoaFavorecido.getNrIdentificacao() == null){
    			pessoaFavorecido = pessoaService.findById(pessoaFavorecido.getIdPessoa());
    		}
    		parametros.add(FormatUtils.formatIdentificacao(pessoaFavorecido));
    		parametros.add(pessoaFavorecido.getNmPessoa());
    	}
    	
		return configuracoesFacade.getMensagem("LMS-21054", parametros.toArray());
    }
    
    /**
     * Solicitação CQPRO00024253
     * Metodo de Workflow externalizado para facilitar o uso através da Integração.
     * @param reciboIndenizacao
     * @return
     */
	@SuppressWarnings("unchecked")
	public Pendencia executeWorkflowPendencia(ReciboIndenizacao reciboIndenizacao) {
		Short nrTipoEvento = null;
		Short nrTipoEventoAlerta = null;
		Pendencia pendencia = null;
		BigDecimal vlIndenizacao = reciboIndenizacao.getVlIndenizacao();
		BigDecimal vlSalarioMinimo = cotacaoIndicadorFinanceiroService.findVlCotacaoIndFinanceiro("SALMIN", SessionUtils.getPaisSessao().getIdPais(), JTDateTimeUtils.getDataAtual());
		String mensagemWorkflow = getDsProcessoWorkflowParaReciboIndenizacao(reciboIndenizacao);

		 /** FAIXA 00 */
		//Se valor informado no campo “Valor indenização” for menor ou igual ao parâmetro 
		//VL_INDENIZACAO_FAIXA00_FINAL x Valor do salário mínimo:
		//Aprovação do Gerente da filial 
		BigDecimal faixaFinal = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("VL_INDENIZACAO_FAIXA00_FINAL", false);
		BigDecimal faixaMeio = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("VL_INDENIZACAO_FAIXA00_MEIO", false);
		
		if(CompareUtils.le(vlIndenizacao, faixaMeio.multiply(vlSalarioMinimo))){
			if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
				boolean isMatriz = reciboIndenizacao.getFilial() != null && 
									historicoFilialService.validateFilialUsuarioMatriz(reciboIndenizacao.getFilial().getIdFilial());
				if(isMatriz) {				
					nrTipoEvento = ConstantesWorkflow.NR2141_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2101_VALOR_INDENIZACAO;
				}
			} else {
				nrTipoEvento = ConstantesWorkflow.NR2118_VALOR_INDENIZACAO;
			}
			
			pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
					reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
		} else if(CompareUtils.le(vlIndenizacao, faixaFinal.multiply(vlSalarioMinimo))) {
			if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
				nrTipoEvento = ConstantesWorkflow.NR2100_VALOR_INDENIZACAO;
				nrTipoEventoAlerta = ConstantesWorkflow.NR2139_VALOR_INDENIZACAO;
				
				List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());

				for (FilialDebitada filialDebitada : filiaisDebitadas) {
					// Se filial não é Matriz executa workflow.
					if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
							workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
									nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
								JTDateTimeUtils.getDataHoraAtual());
					}
				}
			}else{
				nrTipoEvento = ConstantesWorkflow.NR2118_VALOR_INDENIZACAO;
			}
			
			pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
					reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			
		}
		else {
			if (validateFaixaIndenizacao(vlIndenizacao, "01", vlSalarioMinimo)) { /** FAIXA 01 */
		//Se valor informado no campo “Valor indenização” estiver entre os parâmetros 
		//VL_INDENIZACAO_FAIXA01_INICIAL x Valor do salário mínimo e VL_INDENIZACAO_FAIXA01_FINAL x Valor do salário mínimo:
		// Aprovação do Gerente da filial + Assistente de P&D + Gerente de P&D + Gerente Regional Operações 

				if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
					nrTipoEvento = ConstantesWorkflow.NR2104_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2103_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2121_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2120_VALOR_INDENIZACAO;
				}
				
			List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
			
				pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
						reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			for (FilialDebitada filialDebitada : filiaisDebitadas) {
				// Se filial não é Matriz executa workflow.
				if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
						workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
								nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
							JTDateTimeUtils.getDataHoraAtual());
				}
			}
			
			} else if (validateFaixaIndenizacao(vlIndenizacao, "02", vlSalarioMinimo)) { /** FAIXA 02 */
		//Se valor informado no campo “Valor indenização” estiver entre os parâmetros 
		//VL_INDENIZACAO_FAIXA02_INICIAL x Valor do salário mínimo e VL_INDENIZACAO_FAIXA02_FINAL x Valor do salário mínimo:
		//Aprovação do Gerente da filial + Assistente de P&D + Gerente de P&D + Gerente Regional Operações + Diretor de Operações

				if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
					nrTipoEvento = ConstantesWorkflow.NR2107_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2106_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2124_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2123_VALOR_INDENIZACAO;
				}
				
			List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
			
				pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(),
						nrTipoEvento, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
					JTDateTimeUtils.getDataHoraAtual());
			
			for (FilialDebitada filialDebitada : filiaisDebitadas) {
				// Se filial não é Matriz executa workflow.
				if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
						workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
								nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
							JTDateTimeUtils.getDataHoraAtual());
				}
			}
			
			} else if (validateFaixaIndenizacao(vlIndenizacao, "03", vlSalarioMinimo)) { /** FAIXA 03 */
		//Se valor informado no campo “Valor indenização” estiver entre os parâmetros 
		//VL_INDENIZACAO_FAIXA03_INICIAL x Valor do salário mínimo e VL_INDENIZACAO_FAIXA03_FINAL x Valor do salário mínimo:
				// Aprovação do Gerente da filial + Assistente de P&D + Gerente de P&D + Gerente Regional Operações + Diretor de Operações + Diretor
				// Financeiro

				if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
					nrTipoEvento = ConstantesWorkflow.NR2110_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2109_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2127_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2126_VALOR_INDENIZACAO;
				}
				
			List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
				pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
						reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			for (FilialDebitada filialDebitada:filiaisDebitadas) {
				// Se filial não é Matriz executa workflow.
				if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
						workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
								nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
							JTDateTimeUtils.getDataHoraAtual());
				}
			}
			
			} else if (validateFaixaIndenizacao(vlIndenizacao, "04", vlSalarioMinimo)) { /** FAIXA 04 */
		//Se valor informado no campo “Valor indenização” estiver entre os parâmetros 
		//VL_INDENIZACAO_FAIXA04_INICIAL x Valor do salário mínimo e VL_INDENIZACAO_FAIXA04_FINAL x Valor do salário mínimo:
		//Aprovação do Ger. da filial + Assistente de P&D + Ger. de P&D + Ger. Regional Operações + Diretor de Operações + Diretor Financeiro

				if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
					nrTipoEvento = ConstantesWorkflow.NR2113_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2112_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2130_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2129_VALOR_INDENIZACAO;
				}
				
			List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
				pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
						reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			for (FilialDebitada filialDebitada:filiaisDebitadas) {
				// Se filial não é Matriz executa workflow.
				if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
						workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
								nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
							JTDateTimeUtils.getDataHoraAtual());
				}
			}
			
			} else if (validateFaixaIndenizacao(vlIndenizacao, "05", vlSalarioMinimo, false)) { /** FAIXA 05 */
		//Se valor informado no campo “Valor indenização” for maior que o parâmetro 
		//VL_INDENIZACAO_FAIXA05_INICIAL x Valor do salário mínimo:
				// Aprovação do Ger. da filial + Assist. de P&D + Ger. de P&D + Ger. Regional Operações + Dir. de Operações + Dir. Financeiro + Dir.
				// Presidente

				if("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())){
					nrTipoEvento = ConstantesWorkflow.NR2116_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2115_VALOR_INDENIZACAO;
				} else {
					nrTipoEvento = ConstantesWorkflow.NR2133_VALOR_INDENIZACAO;
					nrTipoEventoAlerta = ConstantesWorkflow.NR2132_VALOR_INDENIZACAO;
				}
				
			List<FilialDebitada>filiaisDebitadas = filialDebitadaService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
				pendencia = workflowPendenciaService.generatePendencia(reciboIndenizacao.getFilial().getIdFilial(), nrTipoEvento,
						reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow, JTDateTimeUtils.getDataHoraAtual());
			
			for (FilialDebitada filialDebitada:filiaisDebitadas) {
				// Se filial não é Matriz executa workflow.
				if (!historicoFilialService.validateFilialUsuarioMatriz(filialDebitada.getFilial().getIdFilial())){
						workflowPendenciaService.generatePendencia(filialDebitada.getFilial().getIdFilial(),
								nrTipoEventoAlerta, reciboIndenizacao.getIdReciboIndenizacao(), mensagemWorkflow,
							JTDateTimeUtils.getDataHoraAtual());
		}
			}
				
		}
		}
		
		return pendencia;
	}
	/**
	 * Método necessário para Visualização dos Alertas: NÃO EXCLUIR!
	 */
	public String executeWorkflow(List<Long>ids, List<String>situacoes) {
		return null;
	}

    /**
     * 
     * @param filiaisDebitadas
     * @param reciboIndenizacao
     * @param doctoServicoIndenizacao
     * @param pcDebitado
     */
	private void povoaFilialDebitada(List filiaisDebitadas, Filial filial, ReciboIndenizacao reciboIndenizacao, 
									 DoctoServicoIndenizacao doctoServicoIndenizacao, String pcDebitado) 
	{
		FilialDebitada filialDebitadaMatriz = new FilialDebitada();						
		filialDebitadaMatriz.setFilial(filial);
		filialDebitadaMatriz.setDoctoServicoIndenizacao(doctoServicoIndenizacao);
		filialDebitadaMatriz.setReciboIndenizacao(reciboIndenizacao);
		filialDebitadaMatriz.setPcDebitado(new BigDecimal(pcDebitado));
		filiaisDebitadas.add(filialDebitadaMatriz);
	}


    /**
     * Método store da tela Manter Recibos de Indenização
     * @param reciboIndenizacao
     * @param itemList
     * @param itemListConfig
     * @param tfm
     * @return
     */
	public java.io.Serializable storeManter(
			ReciboIndenizacao reciboIndenizacao, ItemList itensDocto,
			ItemList itensMda, ItemList itensParcela, ItemList itensAnexoRim,
			ItemListConfig configDocto, TypedFlatMap tfm, boolean hasBeanModification) {
    	
       	// lista de filiais debitadas
       	List filiaisDebitadas = new ArrayList();
       	
       	//TODO
       	/* LMS-666
       	 * Item 6.3 – Cancelar pendência de workflow
         */
       	
       	try {
	    	// calcula o valor total indenizado e a quantidade total de valores, somando os valores de indenizacao da itemList e convertendo para a moeda do usuário
        	BigDecimal vlTotalIndenizado = BigDecimalUtils.ZERO;
        	int qtTotalVolumes = 0;
    		for(Iterator iter = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); iter.hasNext();) {
    			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
    			doctoServicoIndenizacao.setReciboIndenizacao(reciboIndenizacao);

    			if(reciboIndenizacao.getPessoaByIdBeneficiario() != null ){
    				DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
    				Long idCliente = reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa();
    				String escolhido = reciboIndenizacao.getTpBeneficiarioIndenizacao().getValue();
    				
    				validateIntegranteFrete(doctoServico, idCliente, escolhido);
    			}
    			
    			if(reciboIndenizacao.getPessoaByIdFavorecido() != null ){
    				DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
    				Long idCliente = reciboIndenizacao.getPessoaByIdFavorecido().getIdPessoa();
    				String escolhido = reciboIndenizacao.getTpFavorecidoIndenizacao().getValue();
    				
    				validateIntegranteFrete(doctoServico, idCliente, escolhido);
    			}
    			
    			// removendo as notas fiscais dos documentos
    			reciboIndenizacaoNfService.removeByIdDoctoServicoIndenizacao(doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
    			
    			// somando a quantidade de volumes
    			if (doctoServicoIndenizacao.getQtVolumes()!=null)
    				qtTotalVolumes += doctoServicoIndenizacao.getQtVolumes().intValue();
    			
    			// somando o valor total indenizado
    			vlTotalIndenizado = vlTotalIndenizado.add(converteValor(doctoServicoIndenizacao.getMoeda().getIdMoeda(), reciboIndenizacao.getMoeda().getIdMoeda(), doctoServicoIndenizacao.getVlIndenizado(), reciboIndenizacao.getDtGeracao()));
    		}
	    	
	    	reciboIndenizacao.setVlIndenizacao(vlTotalIndenizado);
	    	reciboIndenizacao.setQtVolumesIndenizados(qtTotalVolumes);
	    	
	    	// ##################  Método Store  ##################
	    	getReciboIndenizacaoDAO().storeAll(reciboIndenizacao, itensDocto, itensMda, itensParcela, itensAnexoRim, configDocto, filiaisDebitadas);
	    	// ##################################################

	       		Pendencia pendencia = reciboIndenizacao.getPendencia();
			if (hasBeanModification) {
				if (compareReciboIndenizacaoStatus(reciboIndenizacao, "E")) {
					Pendencia p = reciboIndenizacao.getPendencia();
					if (p != null) {
						workflowPendenciaService.cancelPendencia(p.getIdPendencia());

						reciboIndenizacao.setTpSituacaoWorkflow(new DomainValue("E"));
						pendencia = executeWorkflowPendencia(reciboIndenizacao);
	       		}
				} else if (compareReciboIndenizacaoStatus(reciboIndenizacao, "A")) {
					reciboIndenizacao.setTpSituacaoWorkflow(new DomainValue("E"));
					pendencia = executeWorkflowPendencia(reciboIndenizacao);
			}
			}
			if (compareReciboIndenizacaoStatus(reciboIndenizacao, "R")) {
			reciboIndenizacao.setTpSituacaoWorkflow(new DomainValue("E"));
				pendencia = executeWorkflowPendencia(reciboIndenizacao);
			}

	    	// seta a pendencia do workflow no rim
	    	reciboIndenizacao.setPendencia(pendencia);
	    	getReciboIndenizacaoDAO().store(reciboIndenizacao);	    	
	    	
	    	return reciboIndenizacao.getIdReciboIndenizacao();

       	} catch (RuntimeException re) {
			this.rollbackMasterState(reciboIndenizacao, true, re);
            itensDocto.rollbackItemsState();
    		throw re;
		}   
    }

    private boolean compareReciboIndenizacaoStatus(ReciboIndenizacao reciboIndenizacao, String status) {
    	return reciboIndenizacao.getTpSituacaoWorkflow()!=null && reciboIndenizacao.getTpSituacaoWorkflow().getValue().equals(status);
	}
    /**
     * Obtém o valor do salário mínimo, convertido para a moeda do usuário
     * @return
     */
    private BigDecimal getVlSalarioMinimo() {
    	BigDecimal vlSalarioMinimo = cotacaoIndicadorFinanceiroService.findVlCotacaoIndFinanceiro("SALMIN", SessionUtils.getPaisSessao().getIdPais(), JTDateTimeUtils.getDataAtual());
		// se nao há salário mínimo ou cotacao para ele, então dispara excecao
    	if (vlSalarioMinimo==null) { 
			throw new BusinessException("LMS-21028");
		}
		Pais paisUsuario = SessionUtils.getPaisSessao();
		Moeda moedaUsuario = SessionUtils.getMoedaSessao();
		// se nao há moeda pardrao para o pais, entao dispara excecao
		Moeda moedaPadrao = moedaService.findMoedaPadraoByPais(paisUsuario.getIdPais());
		if (moedaPadrao==null) {
			throw new BusinessException("LMS-21029", new Object[]{paisUsuario.getNmPais()});
		}
		// converte o valor do salário mínimo, que foi cadastrado na moeda padrao do pais, para a moeda do usuario
		vlSalarioMinimo = converteValor(moedaPadrao.getIdMoeda(), moedaUsuario.getIdMoeda(), vlSalarioMinimo, JTDateTimeUtils.getDataAtual());
		return vlSalarioMinimo;
    }
            
    /**
     * Obtém a filial matriz vigente através da empresa.
     * @return
     */
    private Filial findFilialMatriz() {
    	Empresa empresa = empresaService.findById(SessionUtils.getEmpresaSessao().getIdEmpresa());
		Filial matriz = filialService.findFilialByIdEmpresaTpFilial(empresa.getIdEmpresa(), "MA");
		
		if (matriz==null)
			throw new BusinessException("LMS-21009", new Object[]{empresa.getPessoa().getNmPessoa()});
		
		return matriz;
    }
    
    /**
     * Salva uma parcela de recibo de indenizacao
     * @param reciboIndenizacao
     * @param tfm
     */
    private Serializable storeParcelaReciboIndenizacao(ReciboIndenizacao reciboIndenizacao, YearMonthDay dtVencimento, String nrBoleto) {
		ParcelaReciboIndenizacao parcelaReciboIndenizacao = new ParcelaReciboIndenizacao();
		parcelaReciboIndenizacao.setDtVencimento(dtVencimento);
		parcelaReciboIndenizacao.setMoeda(SessionUtils.getMoedaSessao());
		parcelaReciboIndenizacao.setNrBoleto(nrBoleto);
		parcelaReciboIndenizacao.setReciboIndenizacao(reciboIndenizacao);
		parcelaReciboIndenizacao.setTpStatusParcelaIndenizacao(new DomainValue("A"));
		parcelaReciboIndenizacao.setVlPagamento(reciboIndenizacao.getVlIndenizacao());
		return parcelaReciboIndenizacaoService.store(parcelaReciboIndenizacao);
    }

    /**
     * Método find customizado para busca de reciboIndenizacao. Utilizado na lookup de
     * reciboIndenizacao na tela de liberar pagamento de indenização. 
     * @param idFilial
     * @param nrReciboIndenizacao
     * @return
     */
    public List findReciboIndenizacaoToProcessosRim(Long idFilial, Integer nrReciboIndenizacao, Long idReciboIndenizacao) {
    	return this.getReciboIndenizacaoDAO().findReciboIndenizacaoToProcessosRim(idFilial, nrReciboIndenizacao, idReciboIndenizacao);
    }
    
    /**
     * Executa a liberação para pagamento do rim.
     * DF 21.02.01.02 
     * @param idReciboIndenizacao
     */
    public ReciboIndenizacao executeLiberaPagamento(Long idReciboIndenizacao, YearMonthDay dtProgramadaPagamento) {
    	ReciboIndenizacao reciboIndenizacao = findById(idReciboIndenizacao);
    	String strTpStatusIndenizacao = reciboIndenizacao.getTpStatusIndenizacao().getValue();
    	String strTpSituacaoWorkflow = reciboIndenizacao.getTpSituacaoWorkflow()!=null?reciboIndenizacao.getTpSituacaoWorkflow().getValue():"";
    	String strTpFormaPagamento = reciboIndenizacao.getTpFormaPagamento().getValue();
    	
    	/*
    	 * 1.	Se RECIBO_INDENIZACAO.TP_STATUS_INDENIZACAO for igual a “G” = Gerado, 
    	 * “T” = Retornado do Banco ou “E” = Enviado ao JDE e RECIBO_INDENIZACAO.TP_SITUACAO_WORKFLOW for igual a  
    	 * “A” = Aprovado e RECIBO_INDENIZACAO.TP_FORMA_PAGAMENTO <> “PU” = Pago na Unidade habilitar botão “Liberar pagamento”
    	 * */
    	 
    	
		if(	(
				strTpStatusIndenizacao.equalsIgnoreCase("G") ||
				strTpStatusIndenizacao.equalsIgnoreCase("T") ||
				strTpStatusIndenizacao.equalsIgnoreCase("E")
			) &&
			strTpSituacaoWorkflow.equalsIgnoreCase("A") &&
			!strTpFormaPagamento.equalsIgnoreCase("PU")) {
			
			
			/*
			 * gravar na tabela RECIBO_INDENIZACAO para o RIM em questão:
			 * DT_LIBERACAO_PAGAMENTO com “Data atual”
			 * LMS-5065: DT_PROGRAMADA_PAGAMENTO com “Data programada de pagto”
			 * TP_STATUS_ INDENIZACAO com “L”=Liberado para pagamento
			 */
			reciboIndenizacao.setDtLiberacaoPagamento(JTDateTimeUtils.getDataAtual());
			reciboIndenizacao.setDtProgramadaPagamento(dtProgramadaPagamento);
    		DomainValue tpStatusIndenizacao = domainValueService.findDomainValueByValue("DM_STATUS_INDENIZACAO", "L");
    		reciboIndenizacao.setTpStatusIndenizacao(tpStatusIndenizacao);
    		store(reciboIndenizacao);
    		
    		/*
    		 *Gravar na tabela EVENTO_RIM:
    		 *ID_FILIAL: com ID da filial do usuário logado
    		 *ID_RECIBO_INDENIZACAO: com o ID do recibo em questão
    		 *ID_MOTIVO_CANCELAMENTO: com Null
    		 *ID_USUARIO: com ID do usuário logado
    		 *DT_EVENTO_RIM: data e hora atual
    		 *TP_EVENTO_INDENIZACAO: com “LI” = Liberação pagamento
    		 */
    		generateEventoRim(reciboIndenizacao, String.valueOf("LI"), null);
    		

    		executeGeraEventoDoctoServicoRIM(reciboIndenizacao, LIBERAR_PAGAMENTO);
    		return reciboIndenizacao;
		}
		throw new BusinessException("LMS-21007");
    }
    
    /**
     * Processo que envia os Rims ao JDE 
     * 
     * @param idReciboIndenizacao
     */
    public void executeEnvioJDE() {

    	AcaoIntegracao acaoIntegracao = acaoIntegracaoService.findByProcesso("PI LMS-J005S");
    	if(acaoIntegracao == null){
    		throw new BusinessException("LMS-21061");
    	}
    	
    	AcaoIntegracaoEvento acaoIntegracaoEvento = acaoIntegracaoEventosService.findByProcesso("PI LMS-J005S");
    	if(acaoIntegracaoEvento == null){
    		
    		acaoIntegracaoEvento = new AcaoIntegracaoEvento();
    		acaoIntegracaoEvento.setNrDocumento(1L);
    		acaoIntegracaoEvento.setTpDocumento(new DomainValue("MAV"));
    		acaoIntegracaoEvento.setNrAgrupador(1L);
    		acaoIntegracaoEvento.setDsInformacao("RIM - JDE");
    		acaoIntegracaoEvento.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());
    		acaoIntegracaoEvento.setAcaoIntegracao(acaoIntegracao);
    		
    		/*Filial matriz*/
    		Filial filialMatriz = historicoFilialService.findFilialMatriz(SessionUtils.getEmpresaSessao().getIdEmpresa());    		
    		acaoIntegracaoEvento.setFilial(filialMatriz);
    		
    		acaoIntegracaoEventosService.store(acaoIntegracaoEvento);
    		
    	}else{
    		throw new BusinessException("LMS-21062");    		
    	}
    }
    
    /**
     * Método que informa o pagamento do rim.
     * DF 21.01.01.03 
     * @param idReciboIndenizacao
     * @author Rodrigo Antunes
     */
    public void executeInformaPagamento(Long idReciboIndenizacao, YearMonthDay dtPagamentoEfetuado) {
    	if (idReciboIndenizacao!=null) {
    		ReciboIndenizacao ri = findById(idReciboIndenizacao);
			YearMonthDay dataGeracao = ri.getDtGeracao();
			if (dtPagamentoEfetuado.isBefore(dataGeracao)) {
				throw new BusinessException("LMS-21021");
			}
    		if ("PU".equalsIgnoreCase( ri.getTpFormaPagamento().getValue()) 
    				&& "A".equalsIgnoreCase(ri.getTpSituacaoWorkflow().getValue())){
        		ri.setDtPagamentoEfetuado( dtPagamentoEfetuado );
        		DomainValue dv = new DomainValue("P");
        		ri.setTpStatusIndenizacao(dv);
        		store(ri);
        		
        		storeBloqueiosParaDocumentosServicoRIMImformaPagamento(ri, dtPagamentoEfetuado);

        		generateEventoRim(ri, String.valueOf("PA"), null);
        		// Gera evento para os documentos de serviço do RIM
        		executeGeraEventoDoctoServicoRIM(ri, INFORMAR_PAGAMENTO);
    		} else {
    			throw new BusinessException("LMS-21008");
    		}
    	}
    }
    
    
    private void storeBloqueiosParaDocumentosServicoRIMImformaPagamento(ReciboIndenizacao reciboIndenizacao, YearMonthDay dtPagamentoEfetuado) {
    	DateTime dhPagamentoEfetuado = new DateTime(dtPagamentoEfetuado.getYear(),dtPagamentoEfetuado.getMonthOfYear(),dtPagamentoEfetuado.getDayOfMonth(),0,0,0,0);
		//Para cada Documento de Serviço do RIM.
		for(DoctoServicoIndenizacao doctoServicoIndenizacao : (List<DoctoServicoIndenizacao>)reciboIndenizacao.getDoctoServicoIndenizacoes()) {
			DoctoServico doctoServico = doctoServicoService.findById(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			//Verificar quantidade de bloqueios em aberto para o documento de serviço.
			Integer numeroOcorrenciaDoctoServicoEmAberto = ocorrenciaDoctoServicoService.
					findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());

			//Se possuir apenas um bloqueio em aberto. 
			if ((Integer.valueOf(1)).compareTo(numeroOcorrenciaDoctoServicoEmAberto) == 0) {
				OcorrenciaPendencia ocorrenciaPendencia95 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("95"));
				
				OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.
						findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
				Short cdOcorrencia = ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia();
				//verificar se pode ser liberado pelo código 95
				Boolean podeLiberar = ocorrenciaDoctoServicoService.executeVerificarLiberacaoByCodigo(cdOcorrencia);
				
				DateTime dhLiberacaoBloqueio = ocorrenciaDoctoServico.getDhBloqueio();
				
				
				if ( Boolean.TRUE.equals(podeLiberar)){ 
					//inserir uma liberação de código 95
					dhLiberacaoBloqueio = verificarMaiorData(dhPagamentoEfetuado, dhLiberacaoBloqueio.plusMinutes(1));
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia95, doctoServico, dhLiberacaoBloqueio);
				} else { 
					OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaDoctoServicoService.findOcorrenciaLiberacaoByOcorrenciaBloqueada(cdOcorrencia);
					
					dhLiberacaoBloqueio = verificarMaiorDataParaLiberacaoBloqueio(reciboIndenizacao, ocorrenciaDoctoServico);
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia, doctoServico, dhLiberacaoBloqueio);
					
					//inserir um bloqueio com código 97, utilizando a data e hora mais 1 minuto
					OcorrenciaPendencia ocorrenciaPendencia97 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("97"));
					dhLiberacaoBloqueio = dhLiberacaoBloqueio.plusMinutes(1);
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia97, doctoServico, dhLiberacaoBloqueio);
					
					//inserir uma liberação de código 95
					dhLiberacaoBloqueio = verificarMaiorData(dhPagamentoEfetuado, dhLiberacaoBloqueio.plusMinutes(1));
					ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia95, doctoServico, dhLiberacaoBloqueio);
				}
			}
			//Se não existir bloqueios em aberto.
			if ((Integer.valueOf(0)).compareTo(numeroOcorrenciaDoctoServicoEmAberto) == 0) {
				//Inserir um bloqueio com código 97.
				OcorrenciaPendencia ocorrenciaPendencia97 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("97"));
				
				DateTime dhMaiorLiberacaoBloqueio = ocorrenciaDoctoServicoService.
						findMaxDataLiberacaoOcorrencia(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
				DateTime dtEmissao = new DateTime(reciboIndenizacao.getDtEmissao().getYear(),reciboIndenizacao.getDtEmissao().getMonthOfYear(),reciboIndenizacao.getDtEmissao().getDayOfMonth(),0,0,0,0);
				DateTime dhLiberacaoBloqueio  = this.verificarMaiorData( dtEmissao, dhMaiorLiberacaoBloqueio.plusMinutes(1) );
				
				ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia97, doctoServico, dhLiberacaoBloqueio);
				
				//Inserir uma liberação de código 95 .
				OcorrenciaPendencia ocorrenciaPendencia95 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("95"));
				dhLiberacaoBloqueio = verificarMaiorData(dhPagamentoEfetuado, dhLiberacaoBloqueio.plusMinutes(1));
				ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia95, doctoServico, dhLiberacaoBloqueio);
			}
		}
	}
    
	private DateTime verificarMaiorDataParaLiberacaoBloqueio(
			ReciboIndenizacao reciboIndenizacao, OcorrenciaDoctoServico ocorrenciaDoctoServico) {
		DateTime dtEmissao = new DateTime(reciboIndenizacao.getDtEmissao().getYear(),reciboIndenizacao.getDtEmissao().getMonthOfYear(),reciboIndenizacao.getDtEmissao().getDayOfMonth(),0,0,0,0);
		dtEmissao = dtEmissao.minusMinutes(1);
		DateTime dhBloqueio = ocorrenciaDoctoServico.getDhBloqueio().plusMinutes(1);
		
		DateTime dhMaior = verificarMaiorData(dtEmissao, dhBloqueio);
		return dhMaior;
	}
	
	private DateTime verificarMaiorData(DateTime dtEmissao, DateTime dhBloqueio) {
		DateTime dhMaior = null;
		if (dtEmissao.compareTo(dhBloqueio) > 0) {
			dhMaior = dtEmissao;
		} else {
			dhMaior = dhBloqueio;
		}
		return dhMaior;
	}
    
    /**
     * Método que informa o pagamento do rim em lote.
     * 
     * @param loteReciboIndenizacao
     * @author Sidarta Silva
     */
    public void executeInformaPagamentoLote(String loteReciboIndenizacao) {
    
    	List<ReciboIndenizacao> recibosParaInformarPagto = new ArrayList<ReciboIndenizacao>();
    	
    	String[] linhas = loteReciboIndenizacao.split("\n");
    	if (linhas != null && linhas.length > 0) {
    		//Verifica se todas as linhas estao de acordo como o regex
        	for (int i=0; i<linhas.length; i++) {
        		if (linhas[i] != null && !"".equals(linhas[i])) {
        			if (!linhas[i].matches("[A-Z]{3}[;][\\d]{1,}[;](0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[-/.](19|20)\\d\\d[;](0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[-/.](19|20)\\d\\d$")) {
        				throw new BusinessException("LMS-21084", new Object[]{i+1});
        			}
        		}
        		String[] colunas = linhas[i].split(";");
        		if (colunas != null && colunas.length > 0) {
        			//Cria o recibo baseado nas colunas do text
        			Filial filial = new Filial();
        			filial.setSgFilial(colunas[0]);
        			ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
        			reciboIndenizacao.setFilial(filial);
        			reciboIndenizacao.setNrReciboIndenizacao(Integer.parseInt(colunas[1]));
        			String[] dtEmissao = colunas[2].split("/");
        			reciboIndenizacao.setDtEmissao(new YearMonthDay(Integer.parseInt(dtEmissao[2]), Integer.parseInt(dtEmissao[1]), Integer.parseInt(dtEmissao[0])));
        			ReciboIndenizacao ri = this
        			.getReciboIndenizacaoDAO()
        			.findReciboIndenizacaoByFilialNrReciboIndenizacaoDtEmissao(
        					reciboIndenizacao.getFilial(),
        					reciboIndenizacao.getNrReciboIndenizacao(),
        					reciboIndenizacao.getDtEmissao());
        			
        			if (ri == null) {
        				throw new BusinessException("LMS-21090", new Object[] {reciboIndenizacao.getFilial().getSgFilial(), reciboIndenizacao.getNrReciboIndenizacao()});
        			}
        			
        			//Testa se é possivel pagar esse recibo
        			if (!this.executeConfirmaPagamento(ri)) {
        				throw new BusinessException("LMS-21085", new Object[] {reciboIndenizacao.getFilial().getSgFilial(), reciboIndenizacao.getNrReciboIndenizacao()});
        			}
        			//Seta a data de pagamento do text
        			String[] dtPagamentoEfetuado = colunas[3].split("/");
							
					ri.setDtPagamentoEfetuado(new YearMonthDay(
							Integer.parseInt(dtPagamentoEfetuado[2]),
							Integer.parseInt(dtPagamentoEfetuado[1]),
							Integer.parseInt(dtPagamentoEfetuado[0])));
					//Guarda em uma lista os recibos para serem pagos, para nao ser necessario um load novamente
        			recibosParaInformarPagto.add(ri);
        		}
        	}
        	//Para cada recibo guardado executa o informar pagamento
        	for (ReciboIndenizacao reciboIndenizacao : recibosParaInformarPagto) {
        		this.executeInformaPagamento(
        				reciboIndenizacao.getIdReciboIndenizacao(),
        				reciboIndenizacao.getDtPagamentoEfetuado());
			}	
		}//if linhas
    }    
    public boolean executeConfirmaPagamento(Long idReciboIndenizacao) {
    	ReciboIndenizacao reciboIndenizacao = this.findById(idReciboIndenizacao);
    	return executeConfirmaPagamento(reciboIndenizacao);
    }
    
    public boolean executeConfirmaPagamento(ReciboIndenizacao reciboIndenizacao) {

    	
    	Filial filial = SessionUtils.getFilialSessao();
    	boolean filialMatriz = historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial());
    	
    	
    	
    	Cliente cliente = clienteService.findById(reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa());
    	
    	List doctos = doctoServicoIndenizacaoService.findByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());
    	
    	Filial filialOrigem = null;
    	Filial filialDestino = null;
		if (!doctos.isEmpty()) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)doctos.get(0);
			filialOrigem = doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialOrigem();
			filialDestino = doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialDestino();
		}
		/*
    	 * 1.	Se RECIBO_INDENIZACAO.TP_STATUS_INDENIZACAO for igual a  “G”= Gerado, 
    	 * e a Forma de pagamento, RECIBO_INDENIZACAO.TP_FORMA_PAGAMENTO igual a “PU”= Pago na unidade 
    	 * e campo RECIBO_INDENIZACAO.TP_SITUACAO_WORKFLOW = “A” (Aprovado) E 
    	 * (RECIBO_INDENIZACAO.ID_FILIAL = Filial do usuário logado 
    	 * OU Filial do usuário logado = MTZ 
    	 * OU Primeiro DOCTO_SERVICO_INDENIZACAO.ID_DOCTO_SERVICO -> DOCTO_SERVICO.ID_FILIAL_ORIGEM = Filial do usuário logado 
    	 * OU Primeiro DOCTO_SERVICO_INDENIZACAO.ID_DOCTO_SERVICO -> DOCTO_SERVICO.ID_FILIAL_DESTINO = Filial do usuário logado 
    	 * OU (RECIBO_INDENIZACAO.TP_BENEFICIARIO_INDENIZACAO <> “T” 
    	 * E RECIBO_INDENIZACAO.ID_BENEFICIARIO -> CLIENTE.ID_FILIAL_ATENDE_OPERACIONAL = Filial do usuário logado)), 
    	 * habilitar botão “Confirmar pagamento”.
    	 */

		if ("G".equals(reciboIndenizacao.getTpStatusIndenizacao().getValue()) && 
				"PU".equals(reciboIndenizacao.getTpFormaPagamento().getValue()) && 
				"A".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue()) &&
				(filial.getIdFilial().equals(reciboIndenizacao.getFilial().getIdFilial()) || 
						filialMatriz ||
						filialOrigem.getIdFilial().equals(filial.getIdFilial()) ||
						filialDestino.getIdFilial().equals(filial.getIdFilial()) ||
						(!"T".equals(reciboIndenizacao.getTpBeneficiarioIndenizacao().getValue()) &&
								cliente.getFilialByIdFilialAtendeOperacional().getIdFilial().equals(filial.getIdFilial())))) {
			return true;
		}
    	
    	return false;
    }
    /**
	 * Gera um eventoRim para as telas de Rim
	 * @param ri
	 * @author Rodrigo Antunes
	 */
    private void generateEventoRim(ReciboIndenizacao ri, String tpEventoIndenizacao, Long idMotivoCancelamento ) {
    	EventoRim eventoRim = new EventoRim();
    	eventoRim.setFilial( SessionUtils.getFilialSessao() );
    	eventoRim.setReciboIndenizacao( ri );
    	eventoRim.setMotivoCancelamentoRim(null);
    	eventoRim.setUsuario( SessionUtils.getUsuarioLogado() );
    	eventoRim.setDhEventoRim( JTDateTimeUtils.getDataHoraAtual() );
    	DomainValue dv = new DomainValue(tpEventoIndenizacao);
    	eventoRim.setTpEventoIndenizacao( dv );
    	
    	MotivoCancelamentoRim mc = null;
    	
    	if(idMotivoCancelamento!=null) {
    		mc = motivoCancelamentoRimService.findById(idMotivoCancelamento);
    	}
    	
    	eventoRim.setMotivoCancelamentoRim(mc);
    	this.eventoRimService.store( eventoRim );
    }

    /**
     * Método que cancela a liberação do rim
     * DF 21.01.01.07 
     * @param idReciboIndenizacao
     * @author Rodrigo Antunes
     */
    public void executeCancelarLiberacao(Long idReciboIndenizacao, Long idMotivoCancelamento) {
    	if (idReciboIndenizacao!=null) {
    		ReciboIndenizacao ri = findById(idReciboIndenizacao);
    		if("L".equalsIgnoreCase( ri.getTpStatusIndenizacao().getValue() )) {
        		ri.setDtLiberacaoPagamento( null );
        		DomainValue dv = new DomainValue("G");
        		ri.setTpStatusIndenizacao(dv);
        		store(ri);
        		generateEventoRim(ri, "CL", idMotivoCancelamento);
        		// Gera evento para os documentos de serviço do RIM
        		executeGeraEventoDoctoServicoRIM(ri, CANCELAR_LIBERACAO);
    		} else {
    			throw new BusinessException("LMS-21006");
    		}
    	}
    }

    /**
     * Método que cancela o pagamento do rim.
     * DF 21.01.01.07 
     * @param idReciboIndenizacao
     * @author Rodrigo Antunes
     */
    public void executeCancelarPagamento(Long idReciboIndenizacao, Long idMotivoCancelamento) {
    	if (idReciboIndenizacao!=null) {
    		
    		ReciboIndenizacao ri = findById(idReciboIndenizacao);
    		if ("P".equalsIgnoreCase(ri.getTpStatusIndenizacao().getValue()) &&  
    				"PU".equalsIgnoreCase( ri.getTpFormaPagamento().getValue())) 
    		{
        		ri.setDtPagamentoEfetuado( null );
        		DomainValue dv = new DomainValue("G");
        		ri.setTpStatusIndenizacao(dv);
        		store(ri);
        		generateEventoRim(ri, "CP", idMotivoCancelamento);
        		// Gera evento para os documentos de serviço do RIM
        		executeGeraEventoDoctoServicoRIM(ri, CANCELAR_PAGAMENTO);
    		} else {
    			throw new BusinessException("LMS-21005");
    		}
    	}
    }
    
    
    /**
     * Método que gera um evento para cada docto servico do rim. 
     * @param rim
     * @param cdEvento
     * @author Rodrigo Antunes
     */
    private void executeGeraEventoDoctoServicoRIM(ReciboIndenizacao rim, String tipo) {
    	if (rim!=null) {
    		List doctosServicos = rim.getDoctoServicoIndenizacoes();
    		if (doctosServicos!=null && !doctosServicos.isEmpty()) {
    			String strRim = configuracoesFacade.getMensagem("rim");
    			String strDocumento = rim.getFilial().getSgFilial() + " " + StringUtils.leftPad(rim.getNrReciboIndenizacao().toString(), 8, '0');
    			Iterator iter = doctosServicos.iterator();
    			while (iter.hasNext()) {
    				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) iter.next();
    				DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();

    				Evento eventoGerarRecibo = eventoService.findByCdEvento(ConstantesSim.EVENTO_GERACAO_RECIBO_INDENIZACAO);
    				List<EventoDocumentoServico> eventosDoctosServicosGerarRecibo = eventoDocumentoServicoService.findByEventoByDocumentoServico(eventoGerarRecibo.getIdEvento(), doctoServico.getIdDoctoServico());
    				Evento eventoGeracaoSemLocalizacao = eventoService.findByCdEvento(ConstantesSim.EVENTO_GERACAO_RIM_SEM_LOCALIZACAO);
    				List<EventoDocumentoServico> eventosDoctosServicosGeracaoSemLocalizacao = eventoDocumentoServicoService.findByEventoByDocumentoServico(eventoGeracaoSemLocalizacao.getIdEvento(), doctoServico.getIdDoctoServico());
    				
    				
    				Short cdEvento = null;
    				if (INFORMAR_PAGAMENTO.equals(tipo)) {//Informar pagamento
        				if (!eventosDoctosServicosGerarRecibo.isEmpty()) {
        					cdEvento = ConstantesSim.EVENTO_CLIENTE_INDENIZADO;
        				} else {
            				if (!eventosDoctosServicosGeracaoSemLocalizacao.isEmpty()) {
            					cdEvento = ConstantesSim.EVENTO_INFORMAR_PGTO_RIM;
            				}
        				}
    				} else if (CANCELAR_PAGAMENTO.equals(tipo)) {
    					if (!eventosDoctosServicosGerarRecibo.isEmpty()) {
        					cdEvento = ConstantesSim.EVENTO_CANCELAR_PGTO_RIM_SEM_LOCALIZACAO;
            				}
    					
            				if (!eventosDoctosServicosGeracaoSemLocalizacao.isEmpty()) {
            					cdEvento = ConstantesSim.EVENTO_CANCELAR_PGTO_RIM;
            				}
        				
    				} else if (CANCELAR_LIBERACAO.equals(tipo)) {
        					cdEvento = ConstantesSim.EVENTO_CANCELAR_LIBERACAO_RIM;
    				} else if (LIBERAR_PAGAMENTO.equals(tipo)) {
    					/*
        	    		 * Chamar a rotina “Gerar Evento Documento Serviço” para cada Documento do RIM, passando os seguintes parâmetros:
        	    		 * ID_DOCUMENTO_SERVICO: ID do documento em questão
        	    		 * CD_EVENTO: 36
        	    		 * ID_FILIAL: Id da filial do usuário logado
        	    		 * Demais parâmetros: null
        	    		 */
    					cdEvento = ConstantesSim.EVENTO_LIBERAR_PGTO_RIM;
    				}
    	    		
    				if (cdEvento != null) {
    		    	incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
    		    			cdEvento, 
    		    			doctoServico.getIdDoctoServico(), 
    		    			SessionUtils.getFilialSessao().getIdFilial(), 
    		    			strDocumento, 
    		    			JTDateTimeUtils.getDataHoraAtual(), 
    		    			null, 
    		    			SessionUtils.getFilialSessao().getSiglaNomeFilial(), 
    		    			strRim
    		    	);
    				}
    		    	
    		    	
    		    	
    			}
    		}
    	}
    }
    
    /**
     * Método para gerar um evento quando o RIM for impresso.
     * Atualiza a data de emissão, caso não exista uma data 
     * @param idReciboIndenizacao
     * @author Rodrigo Antunes
     */
    public void executeEmitirReciboRIM(ReciboIndenizacao rim) {
    	if (rim!=null) {
    		if ( !"P".equalsIgnoreCase( rim.getTpStatusIndenizacao().getValue() ) ) {
    			if (rim.getDtEmissao()==null) {
    				rim.setDtEmissao( JTDateTimeUtils.getDataAtual() );
            		store(rim);
    			}
        		generateEventoRim(rim, "EM", null);
    		} else {
    			throw new BusinessException("LMS-21012");
    		}
    	}
    }
    
    /**
     * Utilitário para a conversão de valor 
     * @param idMoedaOrigem
     * @param idMoedaDestino
     * @param valor
     * @return
     */
    private BigDecimal converteValor(Long idMoedaOrigem, Long idMoedaDestino, BigDecimal valor, YearMonthDay dtCotacao) {
    	Long idPais = SessionUtils.getPaisSessao().getIdPais();
    	BigDecimal result = conversaoMoedaService.findConversaoMoeda(idPais, idMoedaOrigem, idPais, idMoedaDestino, dtCotacao, valor);
    	return result;
    }
    
    /**
     * Cancela o Recibo de Indenizacao e gera um Evento de Cancelamento de Rim
     * @param idReciboIndenizacao
     * @param idMotivoCancelamentoRim
     */
    public void executeCancelarReciboIndenizacao(Long idReciboIndenizacao, Long idMotivoCancelamentoRim) {
    	ReciboIndenizacao reciboIndenizacao = findById(idReciboIndenizacao);
    	executeCancelarReciboIndenizacao(reciboIndenizacao, idMotivoCancelamentoRim);
    }

    /**
     * Cancela o Recibo de Indenizacao e gera um Evento de Cancelamento de Rim
     * @param idReciboIndenizacao
     * @param idMotivoCancelamentoRim
     */
    public void executeCancelarReciboIndenizacao(ReciboIndenizacao reciboIndenizacao, Long idMotivoCancelamentoRim) {    	
    	// setando o status para cancelado (consultando para obter tb a descricao)
    	DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	Filial filialUsuario = SessionUtils.getFilialSessao();
    	
    	DomainValue tpStatusIndenizacao = domainValueService.findDomainValueByValue("DM_STATUS_INDENIZACAO", "C");    	
    	reciboIndenizacao.setTpStatusIndenizacao(tpStatusIndenizacao);
    	store(reciboIndenizacao);
    	 
    	if(reciboIndenizacao.getTpSituacaoWorkflow()!=null && "E".equals(reciboIndenizacao.getTpSituacaoWorkflow().getValue())){
       		Pendencia pendencia = reciboIndenizacao.getPendencia();
       		if (pendencia != null) {
       			workflowPendenciaService.cancelPendencia(pendencia.getIdPendencia());
       		}
		}
    	 
    	List list = doctoServicoIndenizacaoService.findDoctoServicoIndenizacaoByIdReciboIndenizacao(reciboIndenizacao.getIdReciboIndenizacao());    	
    	for (Iterator it = list.iterator(); it.hasNext(); ) {
    		DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
    		DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();

    		this.storeFimBloqueiosParaDocumentosServicoRIM(doctoServico);
    		
    		// altera o status da nao conformidade para 'RNC' (rnc) e suas ocorrências para 'A' (aberto) 
    		// somente para as ocorrências que possuirem uma diposição com um motivo BL_SOMENTE_AUTOMATICO = 'S'
    		String strReciboIndenizacao = reciboIndenizacao.getFilial().getSgFilial() + " " + StringUtils.leftPad(reciboIndenizacao.getNrReciboIndenizacao().toString(), 8, '0');
    		naoConformidadeService.executeUpdateStatusNaoConformidade(doctoServico.getIdDoctoServico(), "RNC", "A");
    		
    		EventoDocumentoServico eds = eventoDocumentoServicoService.
    				findEventoDoctoServicoByLastDhEventoByIdDoctoServico(doctoServico.getIdDoctoServico(), Boolean.TRUE);
    		
    		if(eds != null){
    		int cdEvento = eds.getEvento().getCdEvento().intValue();
    		
    		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
    				cdEvento == 35 ? ConstantesSim.EVENTO_CANCELAMENTO_RIM : Short.valueOf("133"),
    				doctoServico.getIdDoctoServico(), 
    				filialUsuario.getIdFilial(), 
    				strReciboIndenizacao, 
    				dataHoraAtual, 
    				null, 
    				filialUsuario.getSiglaNomeFilial(), 
    				"RIM");    		
    	}
    	}
    	
    	EventoRim eventoRim = new EventoRim();
    	eventoRim.setFilial(filialUsuario);
    	eventoRim.setReciboIndenizacao(reciboIndenizacao);
    	MotivoCancelamentoRim motivoCancelamentoRim = motivoCancelamentoRimService.findById(idMotivoCancelamentoRim);
    	eventoRim.setMotivoCancelamentoRim(motivoCancelamentoRim);
    	eventoRim.setUsuario(SessionUtils.getUsuarioLogado());
    	eventoRim.setDhEventoRim(dataHoraAtual);
    	eventoRim.setTpEventoIndenizacao(new DomainValue("CR"));
    	eventoRimService.store(eventoRim);
    }
    
    /**
     * Verificar quantidade de bloqueios em aberto para o documento de serviço (Classe OcorrenciaDoctoServicoService, método findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico);
	 * - Se possuir apenas um bloqueio em aberto e se o mesmo é de código 97, finalizá-lo com o código 94 (utilizar método executeRegistrarOcorrenciaDoctoServico da classe OcorrenciaDoctoServicoService, utilizando a data e hora atuais).
     * 
     * @param doctoServico
     */
	private void storeFimBloqueiosParaDocumentosServicoRIM(DoctoServico doctoServico) {
		//Verificar quantidade de bloqueios em aberto para o documento de serviço.
		Integer numeroOcorrenciaDoctoServicoEmAberto = ocorrenciaDoctoServicoService.findCountOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());
    
		//Se possuir apenas um bloqueio em aberto. 
		if ((Integer.valueOf(1)).compareTo(numeroOcorrenciaDoctoServicoEmAberto) == 0) {

			OcorrenciaDoctoServico ocorrenciaDoctoServico = ocorrenciaDoctoServicoService.findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(doctoServico.getIdDoctoServico());

			Short cdOcorrencia = ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia();
			
			if ((Short.valueOf("97")).compareTo(cdOcorrencia) == 0) {
				//finalizá-lo com o código 94
				OcorrenciaPendencia ocorrenciaPendencia94 = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf("94"));
				ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(ocorrenciaPendencia94, doctoServico, JTDateTimeUtils.getDataHoraAtual());
			}
		}
	}
    
    
    public List<ReciboIndenizacao> findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto( String tpStatusIndenizacao,
			YearMonthDay dtPagamentoEfetuado, Boolean blEmailPagto){
    	return getReciboIndenizacaoDAO().findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(
    			tpStatusIndenizacao, dtPagamentoEfetuado, blEmailPagto);
    }
    
	public AcaoIntegracaoService getAcaoIntegracaoService() {
		return acaoIntegracaoService;
	}
	public void setAcaoIntegracaoService(AcaoIntegracaoService acaoIntegracaoService) {
		this.acaoIntegracaoService = acaoIntegracaoService;
	}
	public AcaoIntegracaoEventosService getAcaoIntegracaoEventosService() {
		return acaoIntegracaoEventosService;
	}
	public void setAcaoIntegracaoEventosService(
			AcaoIntegracaoEventosService acaoIntegracaoEventosService) {
		this.acaoIntegracaoEventosService = acaoIntegracaoEventosService;
	}
	public EventoService getEventoService() {
		return eventoService;
	}
	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	
	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
	public ResultSetPage findReciboIndenizacaoByIdRecidoFreteCarreteiro(Long idRecidoFreteCarreteiro) {
		return getReciboIndenizacaoDAO().findReciboIndenizacaoByIdRecidoFreteCarreteiro(idRecidoFreteCarreteiro);
		
	}
	
	public Integer getRowCountgetReciboIndenizacaoByIdReciboFreteCarreteiro(Long idRecidoFreteCarreteiro) {
		return getReciboIndenizacaoDAO().getRowCountgetReciboIndenizacaoByIdReciboFreteCarreteiro(idRecidoFreteCarreteiro);
		
	}
}

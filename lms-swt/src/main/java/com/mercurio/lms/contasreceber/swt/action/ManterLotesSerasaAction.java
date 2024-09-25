package com.mercurio.lms.contasreceber.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteSerasa;
import com.mercurio.lms.contasreceber.model.LoteSerasa;
import com.mercurio.lms.contasreceber.model.MotivoBaixaSerasa;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.LoteSerasaService;
import com.mercurio.lms.contasreceber.model.service.MotivoBaixaSerasaService;
import com.mercurio.lms.contasreceber.model.service.RelacaoPagtoParcialService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.service.ArquivoGeracaoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.contasreceber.swt.manterLotesSerasaAction"
 */

public class ManterLotesSerasaAction extends MasterDetailAction {

	private FaturaService faturaService;
	private FilialService filialService;
	private ArquivoGeracaoService arquivoGeracaoService;
	private RelacaoPagtoParcialService relacaoPagtoParcialService;
	private MotivoBaixaSerasaService motivoBaixaSerasaService;
	/**
	 * Busca a filial 
	 * 
	 * @param criteria
	 * @return List
	 */
	public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookupBySgFilial(criteria.getString("sgFilialFatura"), criteria.getString("tpAcesso"));
	}
	
	public List findMotivoBaixa(TypedFlatMap criteria) {
		return motivoBaixaSerasaService.findMotivosBaixa();
	}
	
	/**
	 * Busca a fatura de acordo com a filial e o n�mero da fatura digitados nas
	 * lookup's
	 * 
	 * @param criteria
	 * @return List
	 */
	public List<Fatura> findLookupFatura(TypedFlatMap criteria) {
		Long nrFatura = criteria.getLong("nrFatura");
		Long idFilial = criteria.getLong("idFilial");
		String tpLote = criteria.getString("tpLote");
		String siglaFilial = criteria.getString("sgFilial");
		
		List<Fatura> lista = this.faturaService.findByNrFaturaByFilial(nrFatura, idFilial);
		
		if (!lista.isEmpty()) {
			Fatura fatura = lista.get(0);
			
			if (!getLoteSerasaService().validaFatura(fatura, tpLote)) {
				throw new BusinessException("LMS-36273",new Object[] { siglaFilial,nrFatura });
			}
		}
		
		return lista;

	}
	
	/**
	 * Salva o mestre (LoteSerasa) e os detalhes (ItemLoteSerasa -> Fatura )
	 * 
	 * @param parameters
	 * @return
	 */
	public Map store(TypedFlatMap parameters) {
		MasterEntry entry = getMasterFromSession(parameters.getLong("idLoteSerasa"), true);
		LoteSerasa loteSerasa = (LoteSerasa)entry.getMaster();
		
		if (loteSerasa.getDhGeracao() == null) {
			ItemList itemList = getItemsFromSession(entry, "listaItemLoteSerasa");
			if (itemList ==  null || !itemList.hasItems()){
				throw new BusinessException("LMS-36064");
			}
			loteSerasa.setDsLote(parameters.getString("dsLote"));
			loteSerasa.setTpLote(parameters.getDomainValue("tpLote"));
	    	loteSerasa = this.getLoteSerasaService().store(loteSerasa,itemList);
	    	
	    	itemList.resetItemsState();
			updateMasterInSession(entry);
			
			parameters.put("idLoteSerasa", loteSerasa.getIdLoteSerasa());
			parameters.put("nrLote", loteSerasa.getNrLote());
			parameters.put("dhGeracao", loteSerasa.getDhGeracao());
	    	
		}

		return parameters;
	}
	

	
	/**
	 * Exclui o LoteSerasa
	 * @param id
	 */
	public void removeById(Long id) {
		this.getLoteSerasaService().removeById(id);
		removeMasterFromSession();
    }
	
	/**
	 * Exclui o Lista de  LoteSerasa
	 * @param id
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		this.getLoteSerasaService().removeByIds(ids);
    }
	
	/**
     * Salva um item na sess�o. chama o metodo populateNewItemInstance 
     * 
     * @param bean
     * @return Serializable
     */
    @SuppressWarnings("unchecked")
	public void storeItemLoteSerasa(Map parameters) {
		saveItemInstance(parameters, "listaItemLoteSerasa");	
    }
	
    /**
     * Valida se faturas podem ser enviadas ao serasa
     * 
     * @param parameters
     * @return
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map validateInserirFaturas(Map parameters) {
		Long idFatura = (Long)parameters.get("idFatura");
		String sgFilial = ""+parameters.get("sgFilial");
		String nrFatura = ""+parameters.get("nrFatura");
		
		String tpLote = ""+parameters.get("tpLote");
		
		Fatura fatura = faturaService.findById(idFatura);
		Map result = new HashMap();
		
		if (getLoteSerasaService().validaFatura(fatura, tpLote)) {
			result.put("faturaValida", Boolean.TRUE);
		} else {
			throw new BusinessException("LMS-36277", new Object[] { sgFilial,nrFatura });
		}
		return result;
	}
	
	/**
	 * Busca Lotes Serasa de acordo com crit�rios de pesquisa
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(Map parameters) {
		TypedFlatMap criteria = new TypedFlatMap(parameters);
		ResultSetPage rsp = getLoteSerasaService().findPaginated(criteria,FindDefinition.createFindDefinition(parameters));
		return rsp;
    }
	
	
	public Integer getRowCount(Map parameters) {
		TypedFlatMap criteria = new TypedFlatMap(parameters);
		return getLoteSerasaService().getRowCount(criteria);
	}
	
	
	/**	 
	 * Salva a referencia do objeto Master na sess�o.
	 * n�o devem ser inicializadas as cole��es que representam os filhos
	 * j� que o usu�rio pode vir a n�o utilizar a aba de filhos, evitando assim
	 * a carga desnecess�ria de objetos na sess�o e a partir do banco de dados.
	 * 
	 * @param id
	 * @return Object
	 */
	public Object findById(Long id) {
		Object masterObj = this.getLoteSerasaService().findById(id);
		putMasterInSession(masterObj); 
		return masterObj;
	}
	
	
	/**
     * Faz o findPaginated do filho
     * Possui uma chamada 'interna' para o findPaginated(initialize) 
     * contido dentro do 'createMasterConfig'
     * 
     * @param parameters
     * @return ResultSetPage
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginatedItemLoteSerasa(Map parameters) {
    	
    	Long masterId = getMasterId(parameters);
    	MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList listaFilhos = entry.getItems("listaItemLoteSerasa");
		
		if (! listaFilhos.isInitialized()) {
			listaFilhos.initialize(this.getLoteSerasaService().findItemLoteSerasaByIdLoteSerasa(masterId));
		}
    	
		ResultSetPage rsp = findPaginatedItemList(parameters, "listaItemLoteSerasa");
		
		return rsp;
    }
    
    /**
     * Faz o getRowCount do filho
     * Possui uma chamada 'interna' para o getRowCount contido dentro 
     * do 'createMasterConfig'
     * 
     * @param parameters
     * @return Integer
     */
    public Integer getRowCountItemLoteSerasa(Map parameters){
    	return getRowCountItemList(parameters, "listaItemLoteSerasa");
    }

    
    /**
     *  Remove um ItemLoteSerasa
     *  
     *  @param ids
     */
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeItemLoteSerasaByIds (List ids) {
    	super.removeItemByIds(ids, "listaItemLoteSerasa");
    }
    
    /**
     * Gera o arquivo para envio ao Serasa
     * 
     * @param parameters
     */
    public Map executeGerarArquivo(TypedFlatMap parameters) {
    	MasterEntry entry = getMasterFromSession(parameters.getLong("idLoteSerasa"), true);
		LoteSerasa loteSerasa = (LoteSerasa)entry.getMaster();
		
		ItemList itemList = entry.getItems("listaItemLoteSerasa");

		boolean naoGerarArquivo = itemList.getNewOrModifiedItems().size() > 0 || itemList.getRemovedItems().size() > 0;
		
		if (naoGerarArquivo) {
			throw new BusinessException("LMS-36244");
		}
		
		loteSerasa = this.getLoteSerasaService().executeGerarArquivo(parameters.getLong("idLoteSerasa"));
		
		itemList.resetItemsState();
		updateMasterInSession(entry);
		
		parameters.put("idLoteSerasa", loteSerasa.getIdLoteSerasa());
		parameters.put("nrLote", loteSerasa.getNrLote());
		parameters.put("dhGeracao", loteSerasa.getDhGeracao());
		
		return parameters;
    }
    
    /**
     *  Executa a importa��o arquivo CSV
     *  @param parameters
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public Map executeImportacao(TypedFlatMap parameters) {
    	Map result = this.getLoteSerasaService().executeImportacao(parameters);
    	List<BusinessException> listaErros = (List) result.get("listaErros");
		List<Long> listaFaturas = (List) result.get("listaFaturas");
		
		MasterEntry entry = getMasterFromSession(parameters.getLong("idLoteSerasa"), true);
		LoteSerasa loteSerasa = (LoteSerasa)entry.getMaster();
		
		Map param = new HashMap();
		param.put("masterId", loteSerasa.getIdLoteSerasa());
		
		for (Long id : listaFaturas) {
			param.put("idFatura", id);
			this.storeItemLoteSerasa(param);
		}

		param.clear();
		param.put("listaErros", listaErros);
		return param;
    }
    
    /**
     *  Executa download do arquivo
     *  @param parameters
     */
    /**
     * Executa download arquivo
     * 
     * @param parameters
     * @return
     */
    public Map executeDownloadArquivo(Map parameters) {
    	Map m = new HashMap();
    	LoteSerasa loteSerasa = (LoteSerasa) getLoteSerasaService().findById((Long)parameters.get("idLoteSerasa"));
    	m.put("arquivoGerado", Base64Util.encode(loteSerasa.getArquivoGerado()));
    	return m;
    }
    
	@Override
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		/**
		 * Declaracao da classe pai
		 */		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(LoteSerasa.class,true);
		
		
		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator faturaComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				ItemLoteSerasa o1 = (ItemLoteSerasa)obj1;
				ItemLoteSerasa o2 = (ItemLoteSerasa)obj2;
				
				Fatura f1 = o1.getFatura();
				Fatura f2 = o2.getFatura();
				
				return f1.getNrFatura().compareTo(f2.getNrFatura());
			}
		};
		
		/**
		 * Esta instancia � responsavel por carregar os 
		 * items filhos na sess�o a partir do banco de dados.
		 */
		ItemListConfig itemListConfigFaturas = new ItemListConfig() {
 
			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public List initialize(Long masterId, Map parameters) {
				return getLoteSerasaService().findItemLoteSerasaByIdLoteSerasa(masterId);
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */			
			public Integer getRowCount(Long masterId, Map parameters) {
				return getLoteSerasaService().getRowCountItemLoteSerasa(masterId);
			}
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				Fatura fatura = faturaService.findById((Long)parameters.get("idFatura"));
				MotivoBaixaSerasa motivo = null;
				if ( parameters.get("idMotivoBaixaSerasa") != null ){
					motivo = motivoBaixaSerasaService.findById((Long)parameters.get("idMotivoBaixaSerasa"));
				}
				ItemLoteSerasa itemLoteSerasa = (ItemLoteSerasa) bean;
				
				DomainValue tpIdentificacao = fatura.getCliente().getPessoa().getTpIdentificacao();
				String nrIdentificacao = fatura.getCliente().getPessoa().getNrIdentificacao();
				String nmPessoa = fatura.getCliente().getPessoa().getNmPessoa();
				
				String sgMoeda = fatura.getMoeda().getSgMoeda();
				String dsSimbolo = fatura.getMoeda().getDsSimbolo();
				
				itemLoteSerasa.setFatura(fatura);
				itemLoteSerasa.setMotivoBaixaSerasa(motivo);
				itemLoteSerasa.setNrFatura(fatura.getNrFatura());
				itemLoteSerasa.setNrFaturaDesc(fatura.getFilialByIdFilial().getSgFilial() + " " + fatura.getNrFatura());
				itemLoteSerasa.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(tpIdentificacao,nrIdentificacao));
				itemLoteSerasa.setNmPessoa(itemLoteSerasa.getNrIdentificacaoFormatado() + " - " + nmPessoa);
				itemLoteSerasa.setSgMoeda(sgMoeda);
				itemLoteSerasa.setDsSimbolo(dsSimbolo);
				
				BigDecimal vlTotal = getLoteSerasaService().findValorSaldo(itemLoteSerasa.getFatura());
				
				itemLoteSerasa.setVlSaldo(vlTotal);
				itemLoteSerasa.setDtEmissao(fatura.getDtEmissao());
				itemLoteSerasa.setDtVencimento(fatura.getDtVencimento());
				itemLoteSerasa.setTpSituacaoFatura(fatura.getTpSituacaoFatura());
				
				return itemLoteSerasa;
			}
		};
		
		config.addItemConfig("listaItemLoteSerasa", ItemLoteSerasa.class, itemListConfigFaturas,faturaComparator);
		
		return config;
	}

	public LoteSerasaService getLoteSerasaService() {
		return (LoteSerasaService) super.getMasterService();
	}

	public void setLoteSerasaService(LoteSerasaService loteSerasaService) {
		this.setMasterService(loteSerasaService);
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ArquivoGeracaoService getArquivoGeracaoService() {
		return arquivoGeracaoService;
	}

	public void setArquivoGeracaoService(ArquivoGeracaoService arquivoGeracaoService) {
		this.arquivoGeracaoService = arquivoGeracaoService;
	}

	public RelacaoPagtoParcialService getRelacaoPagtoParcialService() {
		return relacaoPagtoParcialService;
	}

	public void setRelacaoPagtoParcialService(
			RelacaoPagtoParcialService relacaoPagtoParcialService) {
		this.relacaoPagtoParcialService = relacaoPagtoParcialService;
	}
	
	public void setMotivoBaixaSerasaService(MotivoBaixaSerasaService motivoBaixaSerasaService) {
		this.motivoBaixaSerasaService = motivoBaixaSerasaService;
	}
}

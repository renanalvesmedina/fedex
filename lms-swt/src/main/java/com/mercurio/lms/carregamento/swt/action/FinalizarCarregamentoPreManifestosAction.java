 package com.mercurio.lms.carregamento.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.DispCarregDescQtde;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DispCarregDescQtdeService;
import com.mercurio.lms.carregamento.model.service.DispCarregIdentificadoService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.carregamento.swt.finalizarCarregamentoPreManifestosAction"
 */

public class FinalizarCarregamentoPreManifestosAction extends MasterDetailAction {
	
	private IntegranteEqOperacService integranteEqOperacService;
	private ControleCargaService controleCargaService;
	private FuncionarioService funcionarioService;
	private EmpresaService empresaService;
	private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private DispCarregIdentificadoService dispCarregIdentificadoService;
	private DispCarregDescQtdeService dispCarregDescQtdeService;
	
	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return (CarregamentoDescargaService) super.getMasterService();
	}	
	
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.setMasterService(carregamentoDescargaService);
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public IntegranteEqOperacService getIntegranteEqOperacService() {
		return integranteEqOperacService;
	}

	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	
	public TipoDispositivoUnitizacaoService getTipoDispositivoUnitizacaoService() {
		return tipoDispositivoUnitizacaoService;
	}

	public void setTipoDispositivoUnitizacaoService(
			TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
	}

	public DispositivoUnitizacaoService getDispositivoUnitizacaoService() {
		return dispositivoUnitizacaoService;
	}

	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}
	
	public List findLookupEmpresa(Map criteria) {
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.remove("nrIdentificacao"));
		criteria.put("pessoa", pessoa);
		List<Empresa> empresas = empresaService.findLookup(criteria);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if (empresas != null) {
			for(Empresa empresa : empresas) {
				Map<String, Object> mapEmpresa = new HashMap<String, Object>();
				String nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(empresa.getPessoa());
		
				mapEmpresa.put("idEmpresa", empresa.getIdEmpresa());
				mapEmpresa.put("nrIdentificacao", nrIdentificacaoFormatado);
				mapEmpresa.put("nmPessoa", empresa.getPessoa().getNmPessoa());
				result.add(mapEmpresa);
	}
		}
		return result;
	}
	
	public List findTipoDispositivo(Map criteria) {
		criteria.put("tpControleDispositivo", "Q");
		return this.getTipoDispositivoUnitizacaoService().findTipoDispositivoOrdenado(criteria);
	}
	
	public List findTipoDispositivoIdentificacao(Map criteria) {
		criteria.put("tpControleDispositivo", "I");
		return this.getTipoDispositivoUnitizacaoService().findTipoDispositivoOrdenado(criteria);
	}
	
	public List findDispositivoUnitizacao(Map criteria){
		Map mapTipoDispositivoUnitizacao = new HashMap();
		mapTipoDispositivoUnitizacao.put("idTipoDispositivoUnitizacao", criteria.get("idTipoDispositivoUnitizacao"));
		
		Map mapEmpresa = new HashMap();
		mapEmpresa.put("idEmpresa", criteria.get("idEmpresa"));
		
		criteria.put("tipoDispositivoUnitizacao", mapTipoDispositivoUnitizacao);
		criteria.put("empresa", mapEmpresa);
		criteria.remove("idTipoDispositivoUnitizacao");
		criteria.remove("idEmpresa");

		return this.getDispositivoUnitizacaoService().findLookup(criteria);
	}
	
	public DispCarregDescQtdeService getDispCarregDescQtdeService() {
		return dispCarregDescQtdeService;
	}

	public void setDispCarregDescQtdeService(
			DispCarregDescQtdeService dispCarregDescQtdeService) {
		this.dispCarregDescQtdeService = dispCarregDescQtdeService;
	}

	public DispCarregIdentificadoService getDispCarregIdentificadoService() {
		return dispCarregIdentificadoService;
	}

	public void setDispCarregIdentificadoService(
			DispCarregIdentificadoService dispCarregIdentificadoService) {
		this.dispCarregIdentificadoService = dispCarregIdentificadoService;
	}
	
	//###############################
	// Metodos de negocio
	//###############################
	
	/**
	 * Retorna os dados de empresa do usuario logado.
	 */
	public Map findBasicDataUsuario() {
		Map mapSessao = new HashMap();
		Empresa empresa = SessionUtils.getEmpresaSessao();
		
		mapSessao.put("idEmpresa", empresa.getIdEmpresa());
		mapSessao.put("nrIdentificacao", FormatUtils.formatIdentificacao(empresa.getPessoa().getTpIdentificacao(), empresa.getPessoa().getNrIdentificacao()));
		mapSessao.put("nmPessoa", empresa.getPessoa().getNmPessoa());
		mapSessao.put("tpIdentificacao", empresa.getPessoa().getTpIdentificacao().getValue());
		
		return mapSessao;
	}

	/**
	 * Chama a service de carregamentoDescarga para finalizar o carregamento de um
	 * determinado manifesto.
	 * 
	 * @param TypedFlatMap bean
	 * @return
	 */
	public void storeFinalizarCarregamentoPreManifesto(Map criteria) {
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("masterId", criteria.get("masterId"));
		tfmCriteria.put("idCarregamentoDescarga", criteria.get("idCarregamentoDescarga"));
		tfmCriteria.put("controleCarga.idControleCarga", criteria.get("idControleCarga"));
		
		MasterEntry entry = getMasterFromSession(null, true);
    	ItemList itemsSemIdentificacao = getItemsFromSession(entry, "dispositivosSemIdentificacao");
    	ItemListConfig itemsSemIdentificacaoConfig = getMasterConfig().getItemListConfig("dispositivosSemIdentificacao");
    	ItemList itemsComIdentificacao = getItemsFromSession(entry, "dispositivosComIdentificacao");
    	ItemListConfig itemsComIdentificacaoConfig = getMasterConfig().getItemListConfig("dispositivosComIdentificacao");

    	this.getCarregamentoDescargaService().storeFinalizarCarregamentoPreManifesto(tfmCriteria, itemsSemIdentificacao, itemsSemIdentificacaoConfig, itemsComIdentificacao, itemsComIdentificacaoConfig);
    	// FIXME: verificar se o ItemList n�o precisa ser resetado, por padr�o toda a DF2
    	// deve ter esse comportamento (ItemList.resetItemsState())
    	updateMasterInSession(entry);
	}
	
	
	
	
	public void executeDesfazerFinalizarCarregamentoPreManifestos(Map criteria){
		Long idControleCarga = (Long) criteria.get("idControleCarga");
		Long idCarregamentoDescarga = (Long) criteria.get("idCarregamentoDescarga");
		this.getCarregamentoDescargaService().executeDesfazerFinalizarCarregamentoPreManifestos(idControleCarga, idCarregamentoDescarga);
	}
	
	/***
     * Remo��o de um conjunto de registros Master.
     * 
     * @param ids
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsDispositivosSemIdentificacao(List ids) {
    	super.removeItemByIds(ids, "dispositivosSemIdentificacao");
    }
    
    /***
     * Remo��o de um conjunto de registros Master.
     * 
     * @param ids
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsDispositivosComIdentificacao(List ids) {
    	super.removeItemByIds(ids, "dispositivosComIdentificacao");
    }
    
    /**
     * Salva um item na sess�o.
     * 
     * @param bean
     * @return
     */
    public Serializable saveDispositivosSemIdentificacao(Map parameters) {    	
    	MasterEntry entry = getMasterFromSession(null, true);
    	ItemList itemsSemIdentificacao = getItemsFromSession(entry, "dispositivosSemIdentificacao");
    	ItemListConfig itensSemIdentificacaoConfig = getMasterConfig().getItemListConfig("dispositivosSemIdentificacao");
    	
	    	for (Iterator iter = itemsSemIdentificacao.iterator(null, itensSemIdentificacaoConfig); iter.hasNext();) {
	    		DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) iter.next();
				
    		//Verifica se o objeto a ser validado j� existe na grid...
    		if (dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao().equals((Long)parameters.get("idTipoDispositivoUnitizacao"))
    				&& dispCarregDescQtde.getEmpresa().getIdEmpresa().equals((Long)parameters.get("idEmpresa"))
    				&& !dispCarregDescQtde.getIdDispCarregDescQtde().equals(parameters.get("idDispCarregDescQtde"))) {
	    			throw new BusinessException("LMS-05021");
	    		}
			}
    	return saveItemInstance(parameters, "dispositivosSemIdentificacao");
    }
    
    /**
     * Salva um item na sess�o.
     * 
     * @param bean
     * @return
     */
    public Serializable saveDispositivosComIdentificacao(Map parameters) {   	
    	MasterEntry entry = getMasterFromSession(null, true);
    	ItemList itemsComIdentificacao = getItemsFromSession(entry, "dispositivosComIdentificacao");
    	
    	ItemListConfig itensComIdentificacaoCofnig = getMasterConfig().getItemListConfig("dispositivosComIdentificacao");
    	
	    	for (Iterator iter = itemsComIdentificacao.iterator(null, itensComIdentificacaoCofnig); iter.hasNext();) {
	    		DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) iter.next();
				
	    		//Verifica se o objeto a ser validade ja existe na grid...
	    		if (dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao().equals((Long)parameters.get("idDispositivoUnitizacao"))
    				&& !dispCarregIdentificado.getIdDispCarregIdentificado().equals(parameters.get("idDispCarregIdentificado"))) { 
	    			throw new BusinessException("LMS-05022");
	    		}
			}
    	
    	return saveItemInstance(parameters, "dispositivosComIdentificacao");
    }
   
    
    
       
    /**
     * Faz o findPaginated do filho
     * Possui uma chamada 'interna' para o findPaginated(initialize) 
     * contido dentro do 'createMasterConfig'
     * 
     * @param parameters
     * @return
     */
    public ResultSetPage findPaginatedDispositivosSemIdentificacao(Map parameters) {
    	
    	Long idCarregamentoDescarga = Long.parseLong(parameters.get("masterId").toString());
    	List<DispCarregDescQtde> dispList = dispCarregDescQtdeService.findDispCarregDescQtdeByIdCarregamentoDescarga(idCarregamentoDescarga);
     	    	
    	parameters.put("masterId", null);
    	ResultSetPage result = findPaginatedItemList(parameters, "dispositivosSemIdentificacao");
    	
       	List listDispositivosSemIdentificacao = new ArrayList();
    	for(int i=0; i < result.getList().size(); i++) {
    		DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) result.getList().get(i);
    		
    		Map mapDispositivosSemIdentificacao = new HashMap();    		
        	mapDispositivosSemIdentificacao.put("idDispCarregDescQtde", dispCarregDescQtde.getIdDispCarregDescQtde());
    		mapDispositivosSemIdentificacao.put("idTipoDispositivoUnitizacao", dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
        	mapDispositivosSemIdentificacao.put("dsTipoDispositivoUnitizacao", dispCarregDescQtde.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
        	mapDispositivosSemIdentificacao.put("idEmpresa", dispCarregDescQtde.getEmpresa().getIdEmpresa());
        	mapDispositivosSemIdentificacao.put("nrIdentificacao", FormatUtils.formatIdentificacao(dispCarregDescQtde.getEmpresa().getPessoa()));
        	mapDispositivosSemIdentificacao.put("nmEmpresa", dispCarregDescQtde.getEmpresa().getPessoa().getNmPessoa());
        	mapDispositivosSemIdentificacao.put("qtDispositivo", dispCarregDescQtde.getQtDispositivo());
        	    		
    		listDispositivosSemIdentificacao.add(mapDispositivosSemIdentificacao);
    	}    	
    	result.setList(listDispositivosSemIdentificacao);    	
    	return result;
    }
    
    
    
    
    public ResultSetPage findPaginatedDispositivosComIdentificacao(Map parameters) {
    	
    	ResultSetPage result = findPaginatedItemList(parameters, "dispositivosComIdentificacao");
    	
    	List<DispCarregIdentificado> listDispCarregIdentificado = new ArrayList();
       	List listDispositivosComIdentificacao = new ArrayList();
       	/**
       	 * Busca do Banco se existe dispositivos j� adicionados a esse controle de carga
       	 */
       	if(parameters.get("idControleCarga")!=null){
       		Long idControleCarga = Long.parseLong(parameters.get("idControleCarga").toString());
       		listDispCarregIdentificado = getDispCarregIdentificadoService().findListDispCarregIdentificadoByControleCarga(idControleCarga);
	       	for (DispCarregIdentificado dispCarregIdentificado : listDispCarregIdentificado) {
	    		DispositivoUnitizacao dispositivoUnitizacao = dispCarregIdentificado.getDispositivoUnitizacao();
	    		Map map = parameters;
	    		map.put("idDispCarregIdentificado", dispCarregIdentificado.getIdDispCarregIdentificado());
	    		map.put("idEmpresa", dispositivoUnitizacao.getEmpresa().getIdEmpresa());
	    		map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
	    		map.put("idTipoDispositivoUnitizacao", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());

	    		//Se o result j� esta populado n�o salva na instancia as informa��es do banco.
	    		if(!result.getList().isEmpty()){
    	for(int i=0; i < result.getList().size(); i++) {
		        		DispCarregIdentificado dispCarreg = (DispCarregIdentificado) result.getList().get(i);
		        		if (!dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao().equals(dispCarreg.getDispositivoUnitizacao().getIdDispositivoUnitizacao())
		        				&& dispCarregIdentificado.getIdDispCarregIdentificado().equals(dispCarreg.getIdDispCarregIdentificado())) {
		        	    	Long masterId = (Long) getMasterId(parameters);
		        	    	saveItemInstanceOnSession(masterId, dispCarregIdentificado, "dispositivosComIdentificacao");
		        		}    	
					}
	    		}else{
	    	    	Long masterId = (Long) getMasterId(parameters);
	    	    	saveItemInstanceOnSession(masterId, dispCarregIdentificado, "dispositivosComIdentificacao");
	    		}
	    	}  
       	}
       	
       	
    	for(int i=0; i < result.getList().size(); i++) {
    		DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) result.getList().get(i);
    		
    		Map mapDispositivosComIdentificacao = new HashMap();    		
    		mapDispositivosComIdentificacao.put("idDispCarregIdentificado", dispCarregIdentificado.getIdDispCarregIdentificado());
    		DispositivoUnitizacao dispositivoUnitizacao = dispCarregIdentificado.getDispositivoUnitizacao();
    		mapDispositivosComIdentificacao.put("dsTipoDispositivoUnitizacao", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
    		mapDispositivosComIdentificacao.put("nmEmpresa", dispositivoUnitizacao.getEmpresa().getPessoa().getNmPessoa());
    		mapDispositivosComIdentificacao.put("nrIdentificacao", dispositivoUnitizacao.getNrIdentificacao());
    		
    		listDispositivosComIdentificacao.add(mapDispositivosComIdentificacao);
    	}    	
    	result.setList(listDispositivosComIdentificacao);    	
    	
    	return result;
    }

    
    /**
     * Faz o getRowCount do filho
     * Possui uma chamada 'interna' para o getRowCount contido dentro 
     * do 'createMasterConfig'
     * 
     * @param parameters
     * @return
     */
    public Integer getRowCountDispositivosSemIdentificacao(Map parameters){
    	parameters.put("masterId", null);
    	return getRowCountItemList(parameters, "dispositivosSemIdentificacao");
    }
    
    public Integer getRowCountDispositivosComIdentificacao(Map parameters){
    	parameters.put("masterId", null);
    	return getRowCountItemList(parameters, "dispositivosComIdentificacao");
    }
    
    /**
     * Busca o objeto para popular os dados do form.
     * 
     * @param key
     * @return
     */
    public Object findByIdDispositivosSemIdentificacao(MasterDetailKey key) {
    	DispCarregDescQtde	 dispCarregDescQtde = (DispCarregDescQtde)findItemById(key, "dispositivosSemIdentificacao");

		Map dispCarregDescQtdeMap = new HashMap();
		
		dispCarregDescQtdeMap.put("idDispCarregDescQtde", dispCarregDescQtde.getIdDispCarregDescQtde());
		dispCarregDescQtdeMap.put("idTipoDispositivoUnitizacao", dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
		dispCarregDescQtdeMap.put("idEmpresa", dispCarregDescQtde.getEmpresa().getIdEmpresa());
		dispCarregDescQtdeMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(dispCarregDescQtde.getEmpresa().getPessoa()));
		dispCarregDescQtdeMap.put("tpIdentificacao", dispCarregDescQtde.getEmpresa().getPessoa().getTpIdentificacao().getValue());
		dispCarregDescQtdeMap.put("nmEmpresa", dispCarregDescQtde.getEmpresa().getPessoa().getNmPessoa());		
		dispCarregDescQtdeMap.put("qtDispositivo", dispCarregDescQtde.getQtDispositivo());
    	
    	return dispCarregDescQtdeMap;
    }
    
    /**
     * Busca o objeto para popular os dados do form.
     * 
     * @param key
     * @return
     */
    public Object findByIdDispositivosComIdentificacao(MasterDetailKey key) {
    	DispCarregIdentificado	 dispCarregIdentificado = (DispCarregIdentificado)findItemById(key, "dispositivosComIdentificacao");

    	Map dispCarregIdentificadoMap = new HashMap();
		
    	dispCarregIdentificadoMap.put("idDispCarregIdentificado", dispCarregIdentificado.getIdDispCarregIdentificado());
		dispCarregIdentificadoMap.put("idTipoDispositivoUnitizacao", dispCarregIdentificado.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao());
		dispCarregIdentificadoMap.put("idEmpresa", dispCarregIdentificado.getDispositivoUnitizacao().getEmpresa().getIdEmpresa());
		dispCarregIdentificadoMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(dispCarregIdentificado.getDispositivoUnitizacao().getEmpresa().getPessoa()));
		dispCarregIdentificadoMap.put("tpIdentificacao", dispCarregIdentificado.getDispositivoUnitizacao().getEmpresa().getPessoa().getTpIdentificacao().getValue());
		dispCarregIdentificadoMap.put("nmEmpresa", dispCarregIdentificado.getDispositivoUnitizacao().getEmpresa().getPessoa().getNmPessoa());
		
		dispCarregIdentificadoMap.put("idDispositivoUnitizacao", dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao());
		dispCarregIdentificadoMap.put("nrIdentificacao", dispCarregIdentificado.getDispositivoUnitizacao().getNrIdentificacao());
		
    	return dispCarregIdentificadoMap;
    }
    
    /***
     * Remove uma lista de registros items.
     *  
     * @param ids ids dos registros item a serem removidos.
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsIntegranteEquipe(List ids) {
    	super.removeItemByIds(ids, "integrantes");
    }

    
    //####################################
    // Dados da DF2
    //####################################
    
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) { 
		
		//Declaracao da classe pai
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(EquipeOperacao.class);
		
    	Comparator dispositivosSemComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DispCarregDescQtde dispCarregDescQtde1 = (DispCarregDescQtde)obj1;
				DispCarregDescQtde dispCarregDescQtde2 = (DispCarregDescQtde)obj2;
        		return dispCarregDescQtde1.getTipoDispositivoUnitizacao().getTpControleDispositivo().getValue()
        			.compareTo(dispCarregDescQtde2.getTipoDispositivoUnitizacao().getTpControleDispositivo().getValue());  		
			}    		
    	};
    	
    	Comparator dispositivosComComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DispCarregIdentificado dispCarregIdentificado1 = (DispCarregIdentificado)obj1;
				DispCarregIdentificado dispCarregIdentificado2 = (DispCarregIdentificado)obj2;
        		return dispCarregIdentificado1.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getTpControleDispositivo().getValue()
        			.compareTo(dispCarregIdentificado2.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getTpControleDispositivo().getValue());  		
			}    		
    	};
    	    	
    	//########################
    	// Primeira aba
    	//########################
    	ItemListConfig dispCarregDescQtdeListConfig = new ItemListConfig() {
 
			public List initialize(Long masterId, Map parameters) {
				return getDispCarregDescQtdeService().findDispCarregDescQtdeByIdCarregamentoDescarga(masterId);			
			}
			
			public Integer getRowCount(Long masterId, Map parameters) {
				return getDispCarregDescQtdeService().getRowCountDispCarregDescQtdeByIdCarregamentoDescarga(masterId);				
			}			

			/**
             * Seta um pai para o itemConfig de DispositivoUnitizacao
             */
            public void setMasterOnItem(Object master, Object itemBean) {
            	CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
            	((DispCarregDescQtde) itemBean).setCarregamentoDescarga(carregamentoDescarga);
            }
            
			public Object populateNewItemInstance(Map parameters, Object object) {
				DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) object;
				
				//Populando objeto...
				dispCarregDescQtde.setIdDispCarregDescQtde((Long)parameters.get("idDispCarregDescQtde"));
				dispCarregDescQtde.setQtDispositivo((Integer)parameters.get("qtDispositivo"));
				
				TipoDispositivoUnitizacao tipoDispositivoUnitizacao = getTipoDispositivoUnitizacaoService().findById((Long)parameters.get("idTipoDispositivoUnitizacao"));
				dispCarregDescQtde.setTipoDispositivoUnitizacao(tipoDispositivoUnitizacao); 
				
				Empresa empresa = getEmpresaService().findById((Long)parameters.get("idEmpresa"));
				dispCarregDescQtde.setEmpresa(empresa);
				
				return dispCarregDescQtde;
			}
			
			public void modifyItemValues(Object newBean, Object oldBean) {
				Set ignore = new HashSet();
				ignore.add("versao");
				ignore.add("idDispCarregIdentificado");	
				ReflectionUtils.syncObjectProperties(oldBean, newBean, ignore);				
			}

			public Map configItemDomainProperties() {
				return null;
			}

    	};

    	//########################
    	// Segunda aba
    	//########################
    	ItemListConfig dispCarregIdentificadoListConfig = new ItemListConfig() {
    		
			public List initialize(Long masterId, Map parameters) {
				return getDispCarregIdentificadoService().findDispCarregIdentificadoByIdCarregamentoDescarga(masterId);		
			}
			
			public Integer getRowCount(Long masterId, Map parameters) {
				return getDispCarregIdentificadoService().getRowCountDispCarregIdentificadoByIdCarregamentoDescarga(masterId);			
			}			

            public void setMasterOnItem(Object master, Object itemBean) {
            	CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
            	((DispCarregIdentificado) itemBean).setCarregamentoDescarga(carregamentoDescarga);
            }
			
			public Object populateNewItemInstance(Map parameters, Object object) {												
				DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) object;
				dispCarregIdentificado.setIdDispCarregIdentificado((Long)parameters.get("idDispCarregIdentificado"));
				//Populando objeto...
				DispositivoUnitizacao dispositivoUnitizacao = getDispositivoUnitizacaoService().findById((Long)parameters.get("idDispositivoUnitizacao"));
				dispCarregIdentificado.setDispositivoUnitizacao(dispositivoUnitizacao);
				
				return dispCarregIdentificado;
			}
			
			public void modifyItemValues(Object newBean, Object oldBean) {
				Set ignore = new HashSet();
				ignore.add("versao");
				ignore.add("idIntegranteEqOperac");		
				ReflectionUtils.syncObjectProperties(oldBean, newBean, ignore);				
			}

			public Map configItemDomainProperties() {
				return null;
			}
    	};
    	
    	//Seta as configuracoes do filho...
		config.addItemConfig("dispositivosSemIdentificacao", DispCarregDescQtde.class, dispCarregDescQtdeListConfig, dispositivosSemComparator);
		config.addItemConfig("dispositivosComIdentificacao", DispCarregIdentificado.class, dispCarregIdentificadoListConfig, dispositivosComComparator); 
		
		return config;
	}
	
}

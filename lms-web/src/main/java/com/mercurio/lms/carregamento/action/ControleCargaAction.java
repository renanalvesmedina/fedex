package com.mercurio.lms.carregamento.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CargoOperacional;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.service.CargoOperacionalService;
import com.mercurio.lms.carregamento.model.service.CartaoPedagioService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EquipeService;
import com.mercurio.lms.carregamento.model.service.PagtoPedagioCcService;
import com.mercurio.lms.carregamento.model.service.PostoPassagemCcService;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.FormatUtils;

/**
 * 
 * @author 
 */
public abstract class ControleCargaAction extends MasterDetailAction {

	protected CargoOperacionalService cargoOperacionalService;
	protected CartaoPedagioService cartaoPedagioService;
	protected DomainValueService domainValueService;
	protected EmpresaService empresaService;
	protected EquipeService equipeService;
	protected MeioTranspProprietarioService meioTranspProprietarioService;
	protected MoedaService moedaService;
	protected MotoristaService motoristaService;
	protected PagtoPedagioCcService pagtoPedagioCcService;
	protected PostoPassagemCcService postoPassagemCcService;
	protected PessoaService pessoaService;
	protected UsuarioService usuarioService;
		
	
    public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}
	public void setCartaoPedagioService(CartaoPedagioService cartaoPedagioService) {
		this.cartaoPedagioService = cartaoPedagioService;
	}
	public void setCargoOperacionalService(CargoOperacionalService cargoOperacionalService) {
		this.cargoOperacionalService = cargoOperacionalService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
		this.pagtoPedagioCcService = pagtoPedagioCcService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public List findLookupEmpresa(Map criteria) {
    	return empresaService.findLookup(criteria);
    }
    public List findLookupPessoa(TypedFlatMap criteria) {
    	Map mapPessoa = new HashMap();
    	mapPessoa.put("tpPessoa", criteria.getString("pessoa.tpPessoa"));
		mapPessoa.put("nrIdentificacao", criteria.getString("pessoa.nrIdentificacao"));
    	return pessoaService.findLookup(mapPessoa);
    }

    public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
    	String nrMatricula = tfm.getString("nrMatricula");
    	if (!StringUtils.isBlank(nrMatricula)){
    		nrMatricula = StringUtils.leftPad(nrMatricula, 9, '0');
    	}
    	return usuarioService.findLookupUsuarioFuncionario(
    			tfm.getLong("idUsuario"), nrMatricula, null, null, null, null, true);
    }

    public List findMoeda(Map criteria) {
    	FilterList filter = new FilterList(moedaService.find(criteria)) {
			public Map filterItem(Object item) {
				Moeda moeda = (Moeda)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idMoeda", moeda.getIdMoeda());
	    		typedFlatMap.put("siglaSimbolo", moeda.getSiglaSimbolo());
		    	typedFlatMap.put("sgMoeda", moeda.getSgMoeda());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    public List findCargoOperacional(Map criteria) {
    	List list = cargoOperacionalService.findCargo(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		CargoOperacional cargoOperacional = (CargoOperacional)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idCargoOperacional", cargoOperacional.getIdCargoOperacional());
    		typedFlatMap.put("dsCargo", cargoOperacional.getDsCargo());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    public List findLookupEquipe(Map criteria) {
    	return equipeService.findLookup(criteria);
    }

    public List findLookupMotorista(TypedFlatMap criteria) {
   		criteria.remove("pessoa.nmPessoa");
   		criteria.remove("rotaIdaVolta");

    	List list = motoristaService.findLookupMotorista(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Motorista motorista = (Motorista)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMotorista", motorista.getIdMotorista());
    		typedFlatMap.put("pessoa.nrIdentificacao", motorista.getPessoa().getNrIdentificacao());
    		typedFlatMap.put("pessoa.tpIdentificacao", motorista.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(motorista.getPessoa()));
    		typedFlatMap.put("pessoa.nmPessoa", motorista.getPessoa().getNmPessoa());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    public void validateCNHMotorista(TypedFlatMap criteria) {
		Long idMotorista = Long.valueOf(criteria.get("idMotorista").toString());
		motoristaService.validateCNHMotorista(idMotorista);
	}

    public Map findCartaoPedagio(TypedFlatMap criteria) {
    	Map retorno = new HashMap();
    	List resultado = cartaoPedagioService.findCartaoPedagioByOperadora(
    			criteria.getLong("idOperadoraCartaoPedagio"), criteria.getLong("nrCartao"), Boolean.FALSE);

    	if (!resultado.isEmpty()) {
    		CartaoPedagio cartaoPedagio = (CartaoPedagio)resultado.get(0);
    		cartaoPedagioService.validateDtValidadeByIdCartaoPedagio(cartaoPedagio.getIdCartaoPedagio());
    		retorno.put("idCartaoPedagio", cartaoPedagio.getIdCartaoPedagio());
    	}
		return retorno;
    }
    

    /**
     * 
     * @param idMeioTransporte
     * @param mapRetorno
     */
    protected TypedFlatMap findProprietarioVeiculo(Long idMeioTransporte) {
    	TypedFlatMap tfm = new TypedFlatMap();

    	Map mapResultado = meioTranspProprietarioService.findProprietarioByMeioTransporte(idMeioTransporte);
    	if (mapResultado != null) {
	    	Map mapProprietario = (Map)mapResultado.get("proprietario");
	    	if (mapProprietario != null) {
	    		tfm.put("proprietario.idProprietario", mapProprietario.get("idProprietario"));
	    		Map mapPessoa = (Map)mapProprietario.get("pessoa");
	    		if (mapPessoa != null) {
	    			tfm.put("proprietario.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(
		    			(String)((Map)mapPessoa.get("tpIdentificacao")).get("value"), (String)mapPessoa.get("nrIdentificacao"))); 
		    		tfm.put("proprietario.pessoa.nmPessoa", mapPessoa.get("nmPessoa"));
		    	}
		    }
    	}
    	return tfm;
    }

	
	
	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	public ControleCargaService getControleCargaService() {
		return (ControleCargaService) super.getMasterService();
	}	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.setMasterService(controleCargaService);
	}


    /**
     * 
     * @param parameters
     */
	protected void resetDataTab(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		String alias = parameters.getString("alias"); 

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
    	ItemList itemList = getItemsFromSession(masterEntry, alias);
    	ItemListConfig itemListConfig = getMasterConfig().getItemListConfig(alias);
    	
    	if (itemList.isInitialized()) {
    		removeAllItemList(idControleCarga, itemList, itemListConfig);
    	}
    	updateMasterInSession(masterEntry);
	}

	
	
	
    /**
     * 
     * @param idControleCarga
     * @param masterEntry
     * @param itemList
     * @param itemListConfig
     * @param lista
     */
    protected void resetaAtualizaItemList(Long idControleCarga, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig, List lista) {
		for (Iterator iter = itemList.iterator(idControleCarga, itemListConfig); iter.hasNext();) {
			iter.next();
			iter.remove();
		}
		populateItemList(lista, masterEntry, itemList, itemListConfig);
	}

    /**
     * 
     * @param lista
     * @param masterEntry
     * @param itemList
     * @param itemListConfig
     */
    protected void populateItemList(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
    	if (lista.isEmpty()) {
    		itemList.initialize(Collections.EMPTY_LIST);
    	}
    	else {
			for (Iterator iter = lista.iterator(); iter.hasNext();) {
				itemList.addItem(iter.next(), itemListConfig);
			}
    	}
		updateMasterInSession(masterEntry);
	}

    
    /**
     * 
     * @param idControleCarga
     * @param itemList
     * @param itemListConfig
     */
    protected void removeAllItemList(Long idControleCarga, ItemList itemList, ItemListConfig itemListConfig) {
		for (Iterator iter = itemList.iterator(idControleCarga, itemListConfig); iter.hasNext();) {
			iter.next();
			iter.remove();
		}
	}

    
    /**
     * 
     * @param idControleCarga
     * @return
     */
    protected Long getValorIdControleCarga(Long idControleCarga) {
    	if (idControleCarga != null && idControleCarga.compareTo(Long.valueOf(0)) > 0)
    		return idControleCarga;
    	return null;
    }

    
	
	
	
    /************************************************************************************
     								INICIO - EQUIPE OPERACAO
    ************************************************************************************/
    /**
     * Salva um item na sessão.
     * 
     * @param bean
     * @return
     */
    public Serializable saveIntegranteEqOperac(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	String tpIntegrante = criteria.getString("tpIntegrante");
    	Long idUsuario = criteria.getLong("usuario.idUsuario");
    	Long idPessoa = criteria.getLong("pessoa.idPessoa");
    	Long idIntegranteEqOperac = criteria.getLong("idIntegranteEqOperac");

    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("integrantes");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("integrantes");
		
		Iterator iter = itemList.iterator(idControleCarga, itemListConfig);
		getControleCargaService().validateIntegranteExisteSessao(iter, tpIntegrante, idUsuario, idPessoa, idIntegranteEqOperac);
		getControleCargaService().validateIntegranteEmEquipesComControleCarga(idControleCarga, idUsuario, idPessoa);

		if (tpIntegrante.equals("T")) {
			Pessoa pessoa = pessoaService.findById(idPessoa);
			equipeService.validateTerceiroIsFuncionario(FormatUtils.formatIdentificacao(pessoa));
		}

    	return saveItemInstance(criteria, "integrantes");
    }


    /**
     * 
     * @param key
     * @return
     */
    public TypedFlatMap findByIdIntegranteEqOperac(MasterDetailKey key) {
    	IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)findItemById(key, "integrantes");

    	TypedFlatMap mapIntegranteEqOperac = new TypedFlatMap();
    	mapIntegranteEqOperac.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
        mapIntegranteEqOperac.put("nmIntegranteEquipe", integranteEqOperac.getNmIntegranteEquipe());
        mapIntegranteEqOperac.put("tpIntegrante.description", integranteEqOperac.getTpIntegrante().getDescription());
        mapIntegranteEqOperac.put("tpIntegrante.value", integranteEqOperac.getTpIntegrante().getValue());
        mapIntegranteEqOperac.put("tpIntegrante.status", integranteEqOperac.getTpIntegrante().getStatus());

        // Funcionário
        if (integranteEqOperac.getTpIntegrante().getValue().equals("F")) {
              mapIntegranteEqOperac.put("usuario.idUsuario", integranteEqOperac.getUsuario().getIdUsuario());
              mapIntegranteEqOperac.put("usuario.nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
              mapIntegranteEqOperac.put("usuario.nmUsuario", integranteEqOperac.getUsuario().getNmUsuario());
              if (integranteEqOperac.getCargoOperacional() != null) {
            	  mapIntegranteEqOperac.put("usuario.dsFuncao", integranteEqOperac.getCargoOperacional().getDsCargo());
              }
        }
        else
        	// Terceiro
        	if (integranteEqOperac.getTpIntegrante().getValue().equals("T")) {
				mapIntegranteEqOperac.put("pessoa.idPessoa", integranteEqOperac.getPessoa().getIdPessoa());
				mapIntegranteEqOperac.put("pessoa.pessoa.nrIdentificacao", integranteEqOperac.getPessoa().getNrIdentificacao());
				mapIntegranteEqOperac.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()));
				mapIntegranteEqOperac.put("pessoa.tpIdentificacao", integranteEqOperac.getPessoa().getTpIdentificacao().getValue());
				mapIntegranteEqOperac.put("pessoa.nmPessoa", integranteEqOperac.getPessoa().getNmPessoa());
				mapIntegranteEqOperac.put("pessoa.dhInclusao", integranteEqOperac.getPessoa().getDhInclusao());
        	}  

        if (integranteEqOperac.getCargoOperacional() != null) {
              mapIntegranteEqOperac.put("cargoOperacional.idCargoOperacional", integranteEqOperac.getCargoOperacional().getIdCargoOperacional());
              mapIntegranteEqOperac.put("cargoOperacional.dsCargo", integranteEqOperac.getCargoOperacional().getDsCargo());
        }  

        if (integranteEqOperac.getEmpresa() != null) {
              mapIntegranteEqOperac.put("empresa.idEmpresa", integranteEqOperac.getEmpresa().getIdEmpresa());
              mapIntegranteEqOperac.put("empresa.tpSituacao.description", integranteEqOperac.getEmpresa().getTpSituacao().getDescription());
              mapIntegranteEqOperac.put("empresa.tpSituacao.value", integranteEqOperac.getEmpresa().getTpSituacao().getValue());
              mapIntegranteEqOperac.put("empresa.tpSituacao.status", integranteEqOperac.getEmpresa().getTpSituacao().getStatus());
              mapIntegranteEqOperac.put("empresa.pessoa.nmPessoa", integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
              mapIntegranteEqOperac.put("empresa.pessoa.nrIdentificacao", integranteEqOperac.getEmpresa().getPessoa().getNrIdentificacao());
              mapIntegranteEqOperac.put("empresa.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getEmpresa().getPessoa()));
        }
    	return mapIntegranteEqOperac;
    }

    
	/**
	 * 
	 * @param parameters
	 */
	public void resetDataByEquipe(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 
		TypedFlatMap map = new TypedFlatMap();
		map.put("idControleCarga", idControleCarga);
		map.put("alias", "integrantes"); 
		resetDataTab(map);
	}

	
	/**
	 * 
	 * @param parameters
	 * @param object
	 * @return
	 */
	protected Object populateNewItemInstanceIntegranteEqOperac(Map parameters, Object object) {
		TypedFlatMap criteria = (TypedFlatMap)(Map)parameters;

		String tpIntegrante = criteria.getDomainValue("tpIntegrante").getValue();
		IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) object;
		integranteEqOperac.setTpIntegrante(domainValueService.findDomainValueByValue("DM_INTEGRANTE_EQUIPE", tpIntegrante));
		integranteEqOperac.setIdIntegranteEqOperac(criteria.getLong("idIntegranteEqOperac"));

		if (tpIntegrante.equals("F")) {
			Usuario usuario = usuarioService.findById(criteria.getLong("usuario.idUsuario")) ;

			CargoOperacional cargoOperacional = new CargoOperacional();
			cargoOperacional.setDsCargo(criteria.getString("usuario.dsFuncao"));

			integranteEqOperac.setNmIntegranteEquipe(usuario.getNmUsuario());
			integranteEqOperac.setUsuario(usuario);
		} 
		else {
			Long idPessoa = criteria.getLong("pessoa.idPessoa");
			Long idCargoOperacional = criteria.getLong("cargoOperacional.idCargoOperacional");
			Long idEmpresa = criteria.getLong("empresa.idEmpresa");

			if (idPessoa != null) { 
				integranteEqOperac.setPessoa(pessoaService.findById(idPessoa));
				integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getPessoa().getNmPessoa()); 
			}

			if (idCargoOperacional != null) 
				integranteEqOperac.setCargoOperacional(cargoOperacionalService.findById(idCargoOperacional));

			if (idEmpresa != null) 
				integranteEqOperac.setEmpresa(empresaService.findById(idEmpresa));					
		}
		return integranteEqOperac;
	}
	
	/**
	 * 
	 * @param newBean
	 * @param oldBean
	 */
	protected void modifyItemValuesEquipeOperacao(Object newBean, Object oldBean) {
		Set ignore = new HashSet();
		ignore.add("versao");
		ignore.add("idIntegranteEqOperac");
		ignore.add("equipeOperacao");				
		ReflectionUtils.syncObjectProperties(oldBean, newBean, ignore);				
	}
	
	
	protected List getListaIntegrantesOrdenada(Iterator iterator) {
		List listaRetorno = new ArrayList();
		for (Iterator iter = iterator; iter.hasNext();) {
			IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
			tfm.put("nmIntegranteEquipe", integranteEqOperac.getNmIntegranteEquipe());
			tfm.put("tpIntegrante", integranteEqOperac.getTpIntegrante());
			if (integranteEqOperac.getUsuario() != null) {
				tfm.put("usuario.idUsuario", integranteEqOperac.getUsuario().getIdUsuario());
				tfm.put("usuario.nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
			}
			if (integranteEqOperac.getPessoa() != null) {
				tfm.put("pessoa.idPessoa", integranteEqOperac.getPessoa().getIdPessoa());
				tfm.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()) );
			}
			if (integranteEqOperac.getCargoOperacional() != null) {
				tfm.put("cargoOperacional.dsCargo", integranteEqOperac.getCargoOperacional().getDsCargo());
			}
			if (integranteEqOperac.getEmpresa() != null) {
				tfm.put("empresa.pessoa.nmPessoa", integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
			}
			listaRetorno.add(tfm);
		}

		Collections.sort(listaRetorno, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				TypedFlatMap bean1 = (TypedFlatMap)obj1;
				TypedFlatMap bean2 = (TypedFlatMap)obj2;
				return bean1.getString("nmIntegranteEquipe").compareTo(bean2.getString("nmIntegranteEquipe"));
			}    		
		});
		return listaRetorno;
	}
    /************************************************************************************
									FIM - EQUIPE OPERACAO
     ************************************************************************************/
	
	
	
	
	
	
	


    /************************************************************************************
									INICIO - PagtoPedagioCc
	************************************************************************************/
	/**
	 * Salva um item na sessão.
	 * 
	 * @param bean
	 * @return
	 */
	public Serializable savePagtoPedagioCc(TypedFlatMap criteria) {
		return saveItemInstance(criteria, "pagamentos");
	}


    /**
     * Faz a paginação da grid pagto pedágio. Esse método é chamado somente pela grid (na parte web).
     * 
     * @param parameters
     * @return
     */
    public ResultSetPage findPaginatedPagtoPedagioCc(TypedFlatMap parameters) {
    	ItemList itemList = getMasterFromSession(parameters.getLong("idControleCarga"), false).getItems("pagamentos");
    	if (!itemList.isInitialized())
    		return ResultSetPage.EMPTY_RESULTSET;

    	parameters.put("_currentPage", "1");
    	parameters.put("_pageSize", "1000");
    	ResultSetPage rsp = findPaginatedItemList(parameters, "pagamentos");

    	Map mapValoresOperadora = pagtoPedagioCcService.findOperadoraCartaoPedagio();
    	List listaValor = (List)mapValoresOperadora.get("listaValor");
    	List listaDescricao = (List)mapValoresOperadora.get("listaDescricao");

    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();

			TypedFlatMap map = new TypedFlatMap();
			map.put("idPagtoPedagioCc", pagtoPedagioCc.getIdPagtoPedagioCc());
			map.put("vlPedagio", pagtoPedagioCc.getVlPedagio());
			if (pagtoPedagioCc.getControleCarga() != null) {
				map.put("controleCarga.idControleCarga", pagtoPedagioCc.getControleCarga().getIdControleCarga());
			}
			map.put("moeda.idMoeda", pagtoPedagioCc.getMoeda().getIdMoeda());
			map.put("moeda.sgMoeda", pagtoPedagioCc.getMoeda().getSgMoeda());
			map.put("moeda.dsSimbolo", pagtoPedagioCc.getMoeda().getDsSimbolo());
			map.put("tipoPagamPostoPassagem.idTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());
			map.put("tipoPagamPostoPassagem.dsTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem());
			
			if (pagtoPedagioCc.getOperadoraCartaoPedagio() != null) {
				map.put("operadoraCartaoPedagio_idOperadoraCartaoPedagio", pagtoPedagioCc.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio());
			}
			if (pagtoPedagioCc.getCartaoPedagio() != null) {
				map.put("cartaoPedagio_idCartaoPedagio", pagtoPedagioCc.getCartaoPedagio().getIdCartaoPedagio());
				map.put("cartaoPedagio_nrCartao", pagtoPedagioCc.getCartaoPedagio().getNrCartao());
			}
			map.put("pagtoPedagioCc_idPagtoPedagioCc", pagtoPedagioCc.getIdPagtoPedagioCc());
			map.put("tipoPagamPostoPassagem_blCartaoPedagio", pagtoPedagioCc.getTipoPagamPostoPassagem().getBlCartaoPedagio());

			map.put("valores", listaValor);
			map.put("descricoes", listaDescricao);
			retorno.add(map);
		}
    	rsp.setList(retorno);
    	return rsp;
    }
    
    /**
     * 
     * @param parameters
     * @param object
     * @return
     */
	protected Object populateNewItemInstancePagtoPedagioCc(Map parameters, Object object) {
		TypedFlatMap criteria = (TypedFlatMap)(Map)parameters;

		TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
		tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem(criteria.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem"));

		CartaoPedagio cartaoPedagio = null;
		Long idCartaoPedagio = criteria.getLong("cartaoPedagio_idCartaoPedagio");
		if (idCartaoPedagio != null) {
			cartaoPedagio = new CartaoPedagio();
			cartaoPedagio.setIdCartaoPedagio(idCartaoPedagio);
			cartaoPedagio.setNrCartao(criteria.getLong("cartaoPedagio_nrCartao"));
		}

		OperadoraCartaoPedagio operadoraCartaoPedagio = null;
		Long idOperadoraCartaoPedagio = criteria.getLong("operadoraCartaoPedagio_idOperadoraCartaoPedagio");
		if (idOperadoraCartaoPedagio != null) {
			operadoraCartaoPedagio = new OperadoraCartaoPedagio();
			operadoraCartaoPedagio.setIdOperadoraCartaoPedagio(criteria.getLong("operadoraCartaoPedagio_idOperadoraCartaoPedagio"));
		}

		PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) object;
		pagtoPedagioCc.setIdPagtoPedagioCc(criteria.getLong("pagtoPedagioCc_idPagtoPedagioCc"));
		pagtoPedagioCc.setOperadoraCartaoPedagio(operadoraCartaoPedagio);
		pagtoPedagioCc.setCartaoPedagio(cartaoPedagio);
		return pagtoPedagioCc;
	}

	/**
	 * 
	 * @param newBean
	 * @param oldBean
	 */
	protected void modifyItemValuesPagtoPedagioCc(Object newBean, Object oldBean) {
		PagtoPedagioCc newPagtoPedagioCc = (PagtoPedagioCc)newBean;
		PagtoPedagioCc oldPagtoPedagioCc = (PagtoPedagioCc)oldBean;
		oldPagtoPedagioCc.setCartaoPedagio(newPagtoPedagioCc.getCartaoPedagio());
		oldPagtoPedagioCc.setOperadoraCartaoPedagio(newPagtoPedagioCc.getOperadoraCartaoPedagio());
	}

	
    /**
     * Recebe da tela uma lista com os valores da grid pagamentos. Se algum registro foi alterado na grid, 
     * esse método atualiza na sessão.
     * Se não receber da tela nenhuma lista da da grid pagamentos,  
     *    
     * @param idControleCarga
     * @param parameters
     * @param masterEntry
     * @param itemListPagtoPedagioCc
     * @param itemListConfigPagtoPedagioCc
     */
	protected void atualizaDadosPagtosNaSessao(	Long idControleCarga, List listaPagamentos, MasterEntry masterEntry, 
												ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc) 
    {
		for (Iterator iterPagamentos = listaPagamentos.iterator(); iterPagamentos.hasNext();) {
			TypedFlatMap map = (TypedFlatMap)iterPagamentos.next();

			Long newIdPagtoPedagioCc = map.getLong("pagtoPedagioCc_idPagtoPedagioCc");
			Long newIdOperadoraCartaoPedagio = map.getLong("operadoraCartaoPedagio_idOperadoraCartaoPedagio");
			Long newIdCartaoPedagio = map.getLong("cartaoPedagio_idCartaoPedagio");

			for (Iterator iterPagamentosSecao = itemListPagtoPedagioCc.iterator(idControleCarga, itemListConfigPagtoPedagioCc); iterPagamentosSecao.hasNext();) { 
				PagtoPedagioCc pagtoPedagioCcSecao = (PagtoPedagioCc) iterPagamentosSecao.next();

				if (pagtoPedagioCcSecao.getIdPagtoPedagioCc().compareTo(newIdPagtoPedagioCc) == 0) {
					if ( (newIdOperadoraCartaoPedagio == null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() == null) ||
						 (newIdCartaoPedagio == null && pagtoPedagioCcSecao.getCartaoPedagio() == null) )
					{
						continue;
					}
					if ( (newIdOperadoraCartaoPedagio != null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() == null) ||
						 (newIdOperadoraCartaoPedagio == null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() != null) ||
						 (newIdOperadoraCartaoPedagio.compareTo(pagtoPedagioCcSecao.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio()) != 0) ||
						 (newIdCartaoPedagio != null && pagtoPedagioCcSecao.getCartaoPedagio() == null) ||
						 (newIdCartaoPedagio == null && pagtoPedagioCcSecao.getCartaoPedagio() != null) ||
					     (newIdCartaoPedagio.compareTo(pagtoPedagioCcSecao.getCartaoPedagio().getIdCartaoPedagio()) != 0) )
					{
						map.put("idControleCarga", idControleCarga == null ? "-1" : idControleCarga.toString());
						PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) itemListConfigPagtoPedagioCc.populateNewItemInstance(map, new PagtoPedagioCc());
						itemListPagtoPedagioCc.addItem(pagtoPedagioCc, itemListConfigPagtoPedagioCc);
					}
				}
			}
			updateMasterInSession(masterEntry);
		}
	}
	
	
	/**
	 * Insere os dados no no itemList de pagtoPedagioCc de acordo com o conteúdo da lista. 
	 * O método initialize é usado quando os dados estão cadastrados no banco e o populateItemList quando os dados estão sendo
	 * gerados 'virtualmente'. 
	 * 
	 * @param lista
	 * @param masterEntry
	 * @param itemList
	 * @param itemListConfig
	 */
	protected void populateListaPagtoPedagioCc(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
		if (pagtoPedagioCcExisteBanco(lista))
			itemList.initialize(lista);
		else
			populateItemList(lista, masterEntry, itemList, itemListConfig);
	}


	/**
	 * Verifica se o conteúdo da lista está cadastrado no banco (id positivo).
	 * 
	 * @param lista
	 * @return
	 */
	private boolean pagtoPedagioCcExisteBanco(List lista) {
		if (lista.isEmpty())
			return true;
	
		PagtoPedagioCc bean = (PagtoPedagioCc)lista.get(0);
		return bean.getIdPagtoPedagioCc() != null && bean.getIdPagtoPedagioCc().compareTo(Long.valueOf(0)) > 0;
	}
    /************************************************************************************
									FIM - PagtoPedagioCc
     ************************************************************************************/



    
    
    
	/************************************************************************************
	 								INICIO - PostoPassagemCc
	 ************************************************************************************/
    /**
     * Salva um item na sessão.
     * 
     * @param bean
     * @return
     */
    public Serializable savePostoPassagemCc(TypedFlatMap criteria) {
    	return saveItemInstance(criteria, "postos");
    }
    
    
    /**
     * Faz a paginação da grid postos passagem. Esse método é chamado somente pela grid (na parte web).
     * Inicializa a grid de pagto caso ela não esteja inicializada.
     * 
     * @param parameters
     * @return
     */
    public ResultSetPage findPaginatedPostoPassagemCc(TypedFlatMap parameters) {
    	Long idControleCarga = parameters.getLong("idControleCarga");
    	String tpControleCarga = parameters.getString("_tpControleCargaValor");
    	parameters.put("_currentPage", "1");
    	parameters.put("_pageSize", "1000");
    	ResultSetPage rsp = findPaginatedItemList(parameters, "postos");

    	// Verifica se o itemListPagamentos não está inicializado. Se não estiver, busca os pagtos, inicializa o itemList e insere
    	// os pgtos dentro de itemList.
		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
    	ItemList itemListPagtoPedagioCc = getItemsFromSession(masterEntry, "pagamentos");

    	if (!itemListPagtoPedagioCc.isInitialized()) {
	    	ItemListConfig itemListConfigPagtoPedagioCc = getMasterConfig().getItemListConfig("pagamentos");
	    	List lista = pagtoPedagioCcService.findPagtoPedagioCc(getValorIdControleCarga(idControleCarga),
	    			masterEntry.getItems("postos").iterator(idControleCarga, itemListConfigPagtoPedagioCc), Boolean.TRUE);

	    	populateListaPagtoPedagioCc(lista, masterEntry, itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc);
    	}

    	List retorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			PostoPassagemCc postoPassagemCc = (PostoPassagemCc) iter.next();

			TypedFlatMap map = new TypedFlatMap();
			map.put("idPostoPassagemCc", postoPassagemCc.getIdPostoPassagemCc());
			map.put("vlPagar", postoPassagemCc.getVlPagar());
			if (postoPassagemCc.getControleCarga() != null) {
				map.put("controleCarga.idControleCarga", postoPassagemCc.getControleCarga().getIdControleCarga());
			}
			map.put("postoPassagem.idPostoPassagem", postoPassagemCc.getPostoPassagem().getIdPostoPassagem());
			map.put("postoPassagem.nrKm", postoPassagemCc.getPostoPassagem().getNrKm());
			map.put("postoPassagem.tpPostoPassagem", postoPassagemCc.getPostoPassagem().getTpPostoPassagem());
			map.put("postoPassagem.municipio.nmMunicipio", postoPassagemCc.getPostoPassagem().getMunicipio().getNmMunicipio());
			if (postoPassagemCc.getPostoPassagem().getRodovia() != null) {
				map.put("postoPassagem.rodovia.sgRodovia", postoPassagemCc.getPostoPassagem().getRodovia().getSgRodovia());
			}
			map.put("moeda.idMoeda", postoPassagemCc.getMoeda().getIdMoeda());
			map.put("moeda.sgMoeda", postoPassagemCc.getMoeda().getSgMoeda());
			map.put("moeda.dsSimbolo", postoPassagemCc.getMoeda().getDsSimbolo());
			map.put("tipoPagamPostoPassagem_idTipoPagamPostoPassagem", postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());
			map.put("tipoPagamPostoPassagem_dsTipoPagamPostoPassagem", postoPassagemCc.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem());
			map.put("tipoPagamPostoPassagem_blCartaoPedagio", postoPassagemCc.getTipoPagamPostoPassagem().getBlCartaoPedagio());
			map.put("postoPassagemCc_idPostoPassagemCc", postoPassagemCc.getIdPostoPassagemCc());

	    	Map mapValoresTipoPagamento = postoPassagemCcService.
	    			findFormasPagamentoPostoPassagemCc(postoPassagemCc.getPostoPassagem().getIdPostoPassagem(), tpControleCarga);

	    	map.put("valores", mapValoresTipoPagamento.get("listaValor"));
	    	map.put("descricoes", mapValoresTipoPagamento.get("listaDescricao"));

	    	retorno.add(map);
		}
		rsp.setList(retorno);
    	return rsp;
    }

    
    /**
     * 
     * @param parameters
     * @param object
     * @return
     */
	protected Object populateNewItemInstancePostoPassagemCc(Map parameters, Object object) {
		TypedFlatMap criteria = (TypedFlatMap)(Map)parameters;

		TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
		tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem(criteria.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem"));

		PostoPassagemCc postoPassagemCc = (PostoPassagemCc) object;
		postoPassagemCc.setIdPostoPassagemCc(criteria.getLong("postoPassagemCc_idPostoPassagemCc"));
		postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagem);
		return postoPassagemCc;
	}

	/**
	 * 
	 * @param newBean
	 * @param oldBean
	 */
	protected void modifyItemValuesPostoPassagemCc(Object newBean, Object oldBean) {
		PostoPassagemCc newPostoPassagemCc = (PostoPassagemCc)newBean;
		PostoPassagemCc oldPostoPassagemCc = (PostoPassagemCc)oldBean;
		oldPostoPassagemCc.setTipoPagamPostoPassagem(newPostoPassagemCc.getTipoPagamPostoPassagem());
	}
	
    /**
	 * Recebe da tela uma lista com os valores da grid postos. Se algum registro foi alterado na grid, 
     * esse método atualiza na sessão.
     *  
     * @param idControleCarga
     * @param parameters
     * @param masterEntry
     * @param itemListPostoPassagemCc
     * @param itemListConfigPostoPassagemCc
     */
	protected Boolean atualizaDadosPostosNaSessao(	Long idControleCarga, TypedFlatMap parameters, MasterEntry masterEntry, 
								  					ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc) 
	{
		Boolean blAtualizou = Boolean.FALSE;

		// Recebe a lista com os valores da grid postos passagens
		List listaPostos = parameters.getList("postos");
		if (listaPostos != null) {
			for (Iterator iterPostos = listaPostos.iterator(); iterPostos.hasNext();) {
				TypedFlatMap map = (TypedFlatMap)iterPostos.next();

				Long newIdPostoPassagemCc = map.getLong("postoPassagemCc_idPostoPassagemCc");
				Long newIdTipoPagamPostoPassagem = map.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem");
				
				for (Iterator iterPostosSecao = itemListPostoPassagemCc.iterator(idControleCarga, itemListConfigPostoPassagemCc); iterPostosSecao.hasNext();) { 
					PostoPassagemCc postoPassagemCcSecao = (PostoPassagemCc) iterPostosSecao.next();
					if (postoPassagemCcSecao.getIdPostoPassagemCc().compareTo(newIdPostoPassagemCc) == 0 &&
						postoPassagemCcSecao.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem().compareTo(newIdTipoPagamPostoPassagem) != 0) 
					{
						map.put("idControleCarga", idControleCarga == null ? "-1" : idControleCarga.toString());
						PostoPassagemCc postoCc = (PostoPassagemCc) itemListConfigPostoPassagemCc.populateNewItemInstance(map, new PostoPassagemCc());
						itemListPostoPassagemCc.addItem(postoCc, itemListConfigPostoPassagemCc);
						blAtualizou = Boolean.TRUE;
					}
				}
			}
			updateMasterInSession(masterEntry);
		}
		return blAtualizou;
	}
	
	
	/**
	 * Insere os dados no no itemList de postoPassagemCc de acordo com o conteúdo da lista. 
	 * O método initialize é usado quando os dados estão cadastrados no banco e o populateItemList quando os dados estão sendo
	 * gerados 'virtualmente'.
	 *  
	 * @param lista
	 * @param masterEntry
	 * @param itemList
	 * @param itemListConfig
	 */
	protected void populateListaPostoPassagemCc(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
		if (postoPassagemCcExisteBanco(lista))
			itemList.initialize(lista);
		else
			populateItemList(lista, masterEntry, itemList, itemListConfig);
	}

	/**
	 * Verifica se o conteúdo da lista está cadastrado no banco (id positivo).
	 * 
	 * @param lista
	 * @return
	 */
	private boolean postoPassagemCcExisteBanco(List lista) {
		if (lista.isEmpty())
			return true;

		PostoPassagemCc bean = (PostoPassagemCc)lista.get(0);
		return bean.getIdPostoPassagemCc() != null && bean.getIdPostoPassagemCc().compareTo(Long.valueOf(0)) > 0;
	}


	public void resetTabPostos(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 

		TypedFlatMap mapTab = new TypedFlatMap();
		mapTab.put("idControleCarga", idControleCarga);
		mapTab.put("alias", "postos"); 
		resetDataTab(mapTab);

		mapTab.put("alias", "pagamentos"); 
		resetDataTab(mapTab);
    }
	/************************************************************************************
									FIM - PostoPassagemCc
     ************************************************************************************/
	
}
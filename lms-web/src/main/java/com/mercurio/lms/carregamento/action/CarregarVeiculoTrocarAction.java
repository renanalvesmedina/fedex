package com.mercurio.lms.carregamento.action;

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
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CargoOperacional;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.service.CargoOperacionalService;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.EquipeService;
import com.mercurio.lms.carregamento.model.service.IntegranteEqOperacService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.portaria.model.service.BoxService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.carregamento.carregarVeiculoTrocarAction"
 */

public class CarregarVeiculoTrocarAction extends MasterDetailAction {
	private IntegranteEqOperacService integranteEqOperacService;
	private EquipeService equipeService;
	private EquipeOperacaoService equipeOperacaoService;
	private BoxService boxService;
	private CargoOperacionalService cargoOperacionalService;
	private UsuarioService usuarioService;
	private PessoaService pessoaService;
	private EmpresaService empresaService;
	
	/**
	 * Faz o findPaginated da tela de carregarVeiculoTrocarEquipe
	 * 
	 * @param critesria
	 * @return
	 */
	public ResultSetPage findPaginatedTrocarEquipe(TypedFlatMap criteria) {
		Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
		ResultSetPage resultSetPage = equipeOperacaoService.findPaginatedByIdControleCarga(
				idCarregamentoDescarga, null, Boolean.TRUE, null, FindDefinition.createFindDefinition(criteria));

		if (criteria.getDateTime("dataTerminoEquipe") != null) {
			List result = resultSetPage.getList();
			EquipeOperacao equipeOperacao = (EquipeOperacao) result.get(0);
			equipeOperacao.setDhFimOperacao(criteria.getDateTime("dataTerminoEquipe"));
		}
		return resultSetPage; 
	}

	/**
	 * Faz o getRowCount da tela de carregarVeiculoTrocarEquipe
	 * 
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTrocarEquipe(TypedFlatMap criteria) {
		Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
		Integer integer = equipeOperacaoService.getRowCountByIdControleCarga(idCarregamentoDescarga, null);
		return integer;
	}

	/**
	 * Retorna a ultima equipe cadastrada se ela existir 
	 * 
	 * @param criteria
	 * @return
	 */
	public Map findUltimaEquipe(TypedFlatMap criteria) {
		Long idCarregamentoDescarga = criteria.getLong("idCarregamentoDescarga");
		List result = equipeOperacaoService.findEquipeByIdControleCarga(idCarregamentoDescarga);
		
		Map equipeOperacaoMap = null;
		if (result.size()>0) {
			EquipeOperacao equipeOperacao = (EquipeOperacao) result.get(result.size()-1);
			
			equipeOperacaoMap = new HashMap();
			Map equipeMap = new HashMap();
			
			equipeOperacaoMap.put("idEquipeOperacao", equipeOperacao.getIdEquipeOperacao());
			equipeMap.put("dsEquipe", equipeOperacao.getEquipe().getDsEquipe());
			equipeMap.put("idEquipe", equipeOperacao.getEquipe().getIdEquipe());
			equipeOperacaoMap.put("equipe", equipeMap);
			equipeOperacaoMap.put("dsEquipe", equipeOperacao.getIdEquipeOperacao());
			equipeOperacaoMap.put("dhInicioOperacao", equipeOperacao.getDhInicioOperacao());
			equipeOperacaoMap.put("dhFimOperacao", equipeOperacao.getDhFimOperacao());
		}
		
		Map resultMap = new HashMap();
		resultMap.put("equipeOperacao", equipeOperacaoMap);
		return resultMap;
	}
	
	/**
	 * Captura a data para termino da equipe.
	 * 
	 * @param criteria
	 * @return
	 */
	public Map findDataTerminoEquipe(TypedFlatMap criteria) {
		
		Map dataTermino = new HashMap();
		dataTermino.put("dataTerminoEquipe", JTDateTimeUtils.getDataHoraAtual());
		
		return dataTermino;
	}
	
	/**
	 * Troca de equipe
	 * 
	 * @param TypedFlatMap bean
	 * @return
	 */
	public Serializable storeTrocarEquipe(TypedFlatMap criteria) {
		
		if (criteria.getLong("equipe.idEquipe").equals(criteria.getLong("idEquipeOld"))) {
			throw new BusinessException("LMS-05024");
		}
		
		MasterEntry entry = getMasterFromSession(criteria.getLong(""), true);
    	ItemList items = getItemsFromSession(entry, "integrantes");
    	
    	if (items.isInitialized() == false) {
			items.initialize(integranteEqOperacService.findIntegranteEqOperacao(criteria.getLong("equipe.idEquipe")));
    	}
    	
    	EquipeOperacao equipeOperacao = (EquipeOperacao) entry.getMaster();
    	ItemListConfig config = getMasterConfig().getItemListConfig("integrantes");
    	Serializable idCarregamentoDescarga = equipeOperacaoService.storeTrocarEquipe(criteria, equipeOperacao, items, config);
    	
    	items.resetItemsState(); 
    	updateMasterInSession(entry);
		
		return idCarregamentoDescarga;
	}
    
    /***
     * Remo��o de um conjunto de registros Master.
     * 
     * @param ids
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	equipeService.removeByIds(ids);
    }

    /**
     * Remo��o de um registro Master.
     * @param id
     */
    public void removeById(Long id) {
    	this.getCarregamentoDescargaService().removeById(id);
		super.newMaster();
    }
    
	/**	 
	 * Salva a referencia do objeto Master na sess�o.
	 * n�o devem ser inicializadas as cole��es que representam os filhos
	 * j� que o usu�rio pode vir a n�o utilizar a aba de filhos, evitando assim
	 * a carga desnecess�ria de objetos na sess�o e a partir do banco de dados.
	 * 
	 * @param id
	 */
    public Object findById(Long id) {
		Object masterObj = this.getCarregamentoDescargaService().findById(id);
		putMasterInSession(masterObj); 
		return masterObj;
    }
    
    /**
     * Salva um item na sess�o.
     * Verifica se o mesma ja nao existe na sessao antes de atualizar
     * 
     * @param bean
     * @return
     */
    public Serializable saveIntegranteEqOperac(TypedFlatMap parameters) {
    	
    	MasterEntry entry = getMasterFromSession(parameters.getLong(""), true);
    	ItemList itemsIntegrantes = getItemsFromSession(entry, "integrantes");
    	ItemListConfig itensIntegrantesConfig = getMasterConfig().getItemListConfig("integrantes");
    	for (Iterator iter = itemsIntegrantes.iterator(null, itensIntegrantesConfig); iter.hasNext();) {
    		IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
			
    		if (parameters.getString("tpIntegrante").equals(integranteEqOperac.getTpIntegrante().getValue())){
	    		if (parameters.getString("tpIntegrante").equals("F")){
	    			if ((integranteEqOperac.getUsuario().getIdUsuario().compareTo(parameters.getLong("usuario.idUsuario")))==0) {
	    				throw new BusinessException("LMS-05023");
	    			}
	    		} else {
	    			if (integranteEqOperac.getPessoa().getIdPessoa().equals(parameters.getLong("pessoa.idPessoa"))) {
	    				throw new BusinessException("LMS-05023");
	    			}
	    		}
    		}
		}
    	
    	return saveItemInstance(parameters, "integrantes");
    }
   
    /**
     * Faz o findPaginated do filho
     * Possui uma chamada 'interna' para o findPaginated(initialize) 
     * contido dentro do 'createMasterConfig'
     * 
     * @param parameters
     * @return
     */
    public ResultSetPage findPaginatedIntegranteEqOperac(Map parameters) {

    	Long masterId = getMasterId(parameters);
    	
    	MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList listIntegrantes = entry.getItems("integrantes");
		
		//Na primeira passada ele se obriga a carregar a tela...
		if (listIntegrantes.isInitialized() == false) {
			
			List result = null;
			
			if (parameters.get("idEquipe")!=null) {
				//Caso esteja se capturando o integrantes a partir de uma equipe...
				Long idEquipe = Long.valueOf(parameters.get("idEquipe").toString());
				result = integranteEqOperacService.findIntegranteEqOperacao(idEquipe);
			} else {
				//Caso a equipe em questao ja exista...
				Long idEquipeOperacao =  Long.valueOf(parameters.get("idEquipeOperacao").toString());
				result = integranteEqOperacService.findIntegranteEqOperacByIdEquipeOp(idEquipeOperacao);
			}
			
			listIntegrantes.initialize(new ArrayList());
			
			for (Iterator iter = result.iterator(); iter.hasNext();) {
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
				
				//Atributo nome e um atributo 'Virtual' e deve ser setado na mao...
				if (integranteEqOperac.getTpIntegrante().getValue().equals("F")) {
					integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getUsuario().getNmUsuario());
				} else  {
					integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getPessoa().getNmPessoa());
				}
				
				super.saveItemInstanceOnSession(masterId, integranteEqOperac, "integrantes");
			}
		}
		
    	ResultSetPage result = findPaginatedItemList(parameters, "integrantes");
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
    public Integer getRowCountIntegranteEqOperac(Map parameters){
    	return getRowCountItemList(parameters, "integrantes");
    }
    
    public TypedFlatMap findByIdIntegranteEqOperac(MasterDetailKey key) {
    	IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)findItemById(key, "integrantes");
    	
    	TypedFlatMap mapIntegranteEqOperac = new TypedFlatMap();
    	
    	mapIntegranteEqOperac.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
        mapIntegranteEqOperac.put("nmIntegranteEquipe", integranteEqOperac.getNmIntegranteEquipe());
        mapIntegranteEqOperac.put("tpIntegrante.description", integranteEqOperac.getTpIntegrante().getDescription());
        mapIntegranteEqOperac.put("tpIntegrante.value", integranteEqOperac.getTpIntegrante().getValue());
        mapIntegranteEqOperac.put("tpIntegrante.status", integranteEqOperac.getTpIntegrante().getStatus());

        if (integranteEqOperac.getUsuario() != null) {
              mapIntegranteEqOperac.put("usuario.nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
              mapIntegranteEqOperac.put("usuario.idUsuario", integranteEqOperac.getUsuario().getIdUsuario());
              mapIntegranteEqOperac.put("usuario.nmUsuario", integranteEqOperac.getUsuario().getNmUsuario());
        }

        if (integranteEqOperac.getPessoa() != null) {
              mapIntegranteEqOperac.put("pessoa.idPessoa", integranteEqOperac.getPessoa().getIdPessoa());
              mapIntegranteEqOperac.put("pessoa.pessoa.nrIdentificacao", integranteEqOperac.getPessoa().getNrIdentificacao());
              mapIntegranteEqOperac.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()));          
              mapIntegranteEqOperac.put("pessoa.nmPessoa", integranteEqOperac.getPessoa().getNmPessoa());
              mapIntegranteEqOperac.put("pessoa.dhInclusao", integranteEqOperac.getPessoa().getDhInclusao());
              mapIntegranteEqOperac.put("pessoa.tpPessoa.description", integranteEqOperac.getPessoa().getTpPessoa().getDescription());
              mapIntegranteEqOperac.put("pessoa.tpPessoa.value", integranteEqOperac.getPessoa().getTpPessoa().getValue());
              mapIntegranteEqOperac.put("pessoa.tpPessoa.status", integranteEqOperac.getPessoa().getTpPessoa().getStatus());
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
        }
    	
    	return mapIntegranteEqOperac;
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

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) { 
		
		//Declaracao da classe pai
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(EquipeOperacao.class);

		/*
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
    	Comparator descComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				IntegranteEqOperac integranteEqOperac1 = (IntegranteEqOperac)obj1;
				IntegranteEqOperac integranteEqOperac2 = (IntegranteEqOperac)obj2;
				
				if (integranteEqOperac1.getCargoOperacional()==null) return -1;
				if (integranteEqOperac2.getCargoOperacional()==null) return 1;
				
        		return integranteEqOperac1.getCargoOperacional().getDsCargo().compareTo(integranteEqOperac2.getCargoOperacional().getDsCargo());  		
			}    		
    	};
    	    	
    	/*
    	 * Esta instancia � responsavel por carregar os 
    	 * items filhos na sess�o a partir do banco de dados.
    	 */
    	ItemListConfig itemInit = new ItemListConfig() {
 
    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				Long idEquipe = Long.valueOf(((Map)parameters.get("equipe")).get("idEquipe").toString());
				return integranteEqOperacService.findIntegranteEqOperacao(idEquipe);				
			}
			
			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				Long idEquipe = Long.valueOf(((Map)parameters.get("equipe")).get("idEquipe").toString());
				return integranteEqOperacService.getRowCountIntegranteEqOperac(idEquipe);				
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
			public Object populateNewItemInstance(Map parameters, Object object) {
				
				TypedFlatMap criteria = (TypedFlatMap) parameters;
				
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) object;
				integranteEqOperac.setIdIntegranteEqOperac(criteria.getLong("idIntegranteEqOperac"));
				
				DomainValue domainValue = getDomainValueService().findDomainValueByValue("DM_INTEGRANTE_EQUIPE", criteria.getString("tpIntegrante"));
				
				integranteEqOperac.setTpIntegrante(domainValue);
				 
				if (integranteEqOperac.getTpIntegrante().getValue().equals("F")) {
					Usuario usuario = usuarioService.findById(criteria.getLong("usuario.idUsuario")) ;
					
					//Gera um objeto cargoOperacional apenas para visualizacao e ordenacao.
					CargoOperacional cargoOperacional = new CargoOperacional();
					cargoOperacional.setDsCargo(criteria.getString("usuario.dsFuncao"));
					
					integranteEqOperac.setCargoOperacional(cargoOperacional);
					integranteEqOperac.setNmIntegranteEquipe(usuario.getNmUsuario());
					integranteEqOperac.setUsuario(usuario);
				} else {
					//Busca os ids...
					final Long idPessoa =criteria.getLong("pessoa.idPessoa");
					final Long idCargoOperacional = criteria.getLong("cargoOperacional.idCargoOperacional");
					final Long idEmpresa = criteria.getLong("empresa.idEmpresa");
					
					//Busca e seta os objetos...
					if (idPessoa!=null) { 
						integranteEqOperac.setPessoa(pessoaService.findById(idPessoa));
						integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getPessoa().getNmPessoa()); 
					}
					if (idCargoOperacional!=null) integranteEqOperac.setCargoOperacional(cargoOperacionalService.findById(idCargoOperacional));
					if (idEmpresa!=null) integranteEqOperac.setEmpresa(empresaService.findById(idEmpresa));					
				}
				return integranteEqOperac;
			}
			
			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
				Set ignore = new HashSet();
				ignore.add("versao");
				ignore.add("idIntegranteEqOperac");
				ignore.add("equipeOperacao");				
				ReflectionUtils.syncObjectProperties(oldBean, newBean, ignore);				
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}

    	};
    	//Seta as configuracoes do filho...
		config.addItemConfig("integrantes", IntegranteEqOperac.class, itemInit, descComparator);
		return config;
	}

	/*
	 * Chamadas para metodos diversos da tela
	 */
	
	public List findEquipes(Map criteria) {
		return this.equipeService.findLookup(criteria);
	}

	public List findBox(Map criteria) {
		criteria = new HashMap();
		List boxes = this.boxService.findCombo(criteria);

		return boxes;
	}
	
	public List findCargos(Map criteria) {
		return this.cargoOperacionalService.findCargo(criteria);
	}

	public List findEmpresas(Map criteria) {
		return this.empresaService.findLookup(criteria);
	}

	/**
	 * Busca para o objeto pessoa.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findLookupPessoa(TypedFlatMap criteria) {
		Map pessoa = new HashMap();
		pessoa.put("tpPessoa", criteria.getString("pessoa.tpPessoa"));
		pessoa.put("nrIdentificacao", criteria.getString("nrIdentificacao"));

		return this.pessoaService.findLookup(pessoa);
	}

	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	private CarregamentoDescargaService getCarregamentoDescargaService() {
		return (CarregamentoDescargaService) super.getMasterService();
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		super.setMasterService(carregamentoDescargaService);
	}
	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}
	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}
	public void setCargoOperacionalService(CargoOperacionalService cargoOperacionalService) {
		this.cargoOperacionalService = cargoOperacionalService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}
}

package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DescritivoPce;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.VersaoContatoPce;
import com.mercurio.lms.vendas.model.VersaoDescritivoPce;
import com.mercurio.lms.vendas.model.VersaoPce;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DescritivoPceService;
import com.mercurio.lms.vendas.model.service.EventoPceService;
import com.mercurio.lms.vendas.model.service.OcorrenciaPceService;
import com.mercurio.lms.vendas.model.service.ProcessoPceService;
import com.mercurio.lms.vendas.model.service.VersaoPceService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterVersoesPCEAction"
 */

public class ManterVersoesPCEAction extends MasterDetailAction {
	private static final String ALIAS_NAME = VersaoDescritivoPce.class.getName();
	private ClienteService clienteService;
	private ProcessoPceService processoPceService;
	private EventoPceService eventoPceService;
	private OcorrenciaPceService ocorrenciaPceService;
	private DescritivoPceService descritivoPceService;
	private ContatoService contatoService;
	private VersaoPceService versaoPceService;
	private UsuarioService usuarioService;
	private DomainValueService domainValueService;
	
    public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setUsuarioService(
			com.mercurio.lms.configuracoes.model.service.UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void removeById(java.lang.Long id) {
        ((VersaoPceService)getMasterService()).removeById(id);
        newMaster();
    }

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((VersaoPceService)getMasterService()).removeByIds(ids);
    	newMaster();
    }

	public ResultSetPage findPaginated(Map criteria) {
		return (ResultSetPage)new FilterResultSetPage(((VersaoPceService)getMasterService()).findPaginated(criteria)) {

			public Map filterItem(Object item) {
				VersaoPce bean = (VersaoPce)item;
				TypedFlatMap result = new TypedFlatMap();
				Cliente cliente = bean.getCliente();
				Pessoa pessoa = cliente.getPessoa();
				result.put("idVersaoPce",bean.getIdVersaoPce());
				result.put("cliente.idCliente",cliente.getIdCliente());
				result.put("cliente.pessoa.nmPessoa",pessoa.getNmPessoa());
				result.put("cliente.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
				result.put("cliente.pessoa.tpIdentificacao",pessoa.getTpIdentificacao());
				result.put("cliente.pessoa.nrIdentificacaoFormatado", 
						FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao()));
				result.put("nrVersaoPce",bean.getNrVersaoPce());
				result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
				result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
				return result;
			}
			
		}.doFilter();
	}
	public Map findById(Long id) {
		VersaoPce bean = ((VersaoPceService)getMasterService()).findById(id);
		TypedFlatMap result = new TypedFlatMap();
		Cliente cliente = bean.getCliente();
		Pessoa pessoa = cliente.getPessoa();
		result.put("idVersaoPce",bean.getIdVersaoPce());
		result.put("cliente.idCliente",cliente.getIdCliente());
		result.put("cliente.pessoa.nmPessoa",pessoa.getNmPessoa());
		result.put("cliente.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
		result.put("cliente.pessoa.tpIdentificacao",pessoa.getTpIdentificacao());
		result.put("cliente.pessoa.nrIdentificacaoFormatado", 
				FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao()));
		result.put("nrVersaoPce",bean.getNrVersaoPce());
		result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
		
		result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
		putMasterInSession(bean);
		return result;
	}
	//LOOKUP(S) AND COMBO(S)
	public List findLookupCliente(Map criteria) {
		return clienteService.findLookup(criteria);
	}
	public List findProcessoPce(Map criteria) {
		criteria.put("empresa.idEmpresa",SessionUtils.getEmpresaSessao().getIdEmpresa());
		return processoPceService.find(criteria);
	}
	public List findEventoPce(Map criteria) {
		return eventoPceService.find(criteria);
	}
	public List findOcorrenciaPce(Map criteria) {
		return ocorrenciaPceService.find(criteria);
	}
	public List findDescritivoPce(Map criteria) {
		return descritivoPceService.findCombo(criteria);
	}
	public List findContato(Map criteria) {
		return contatoService.find(criteria);
	}
	
    public Map store(TypedFlatMap map) { 
    	MasterEntry entry = getMasterFromSession(map.getLong("idVersaoPce"), true);
		Cliente cliente = new Cliente();
		cliente.setIdCliente(map.getLong("cliente.idCliente"));
    	VersaoPce bean = (VersaoPce)entry.getMaster();
		bean.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		bean.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		bean.setNrVersaoPce(map.getInteger("nrVersaoPce"));
		bean.setIdVersaoPce(map.getLong("idVersaoPce"));
		bean.setCliente(cliente);

		ItemList items = getItemsFromSession(entry,ALIAS_NAME); 
		versaoPceService.store(bean, items);
		items.resetItemsState(); 
    	updateMasterInSession(entry);
	
		map.clear();
		map.put("idVersaoPce",bean.getIdVersaoPce());
		map.put("nrVersaoPce",bean.getNrVersaoPce());
		map.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		map.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
		return map;
	}
	

    /***
     * Remo��o de um conjunto de registros Master.
     * 
     * @param ids
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)

	/**
     * Salva um item Descri��o Padr�o na sess�o.
     * 
     * @param bean
     * @return
     */
    public Serializable saveVersaoDescritivasPce(TypedFlatMap parameters) {
    	
    	Long masterId = (Long) getMasterId(parameters);
    	MasterEntry entry = getMasterFromSession(masterId, true);
    	ItemList items = getItemsFromSession(entry,ALIAS_NAME);
    	ItemListConfig config = getMasterConfig().getItemListConfig(ALIAS_NAME);
    	for (Iterator iter = items.iterator(masterId, config); iter.hasNext();) {
    		VersaoDescritivoPce bean = (VersaoDescritivoPce) iter.next();
    		if (parameters.getString("descritivoPce.ocorrenciaPce.cdOcorrenciaPce").equals(bean.getDescritivoPce().getOcorrenciaPce().getCdOcorrenciaPce().toString()) &&
	    		parameters.getString("descritivoPce.ocorrenciaPce.eventoPce.cdEventoPce").equals(bean.getDescritivoPce().getOcorrenciaPce().getEventoPce().getCdEventoPce().toString()) &&
	    		parameters.getString("descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce").equals(bean.getDescritivoPce().getOcorrenciaPce().getEventoPce().getProcessoPce().getCdProcessoPce().toString()) &&
    		   !parameters.getString("idVersaoDescritivoPce").equals(bean.getIdVersaoDescritivoPce().toString()))		
    				throw new BusinessException("LMS-01081");
    	} 
    	return saveItemInstance(parameters,ALIAS_NAME);
    }
    
    
    public ResultSetPage findPaginatedVersaoDescritivasPce(Map parameters) {
    	return (ResultSetPage)new FilterResultSetPage(findPaginatedItemList(parameters,ALIAS_NAME)) {

			public Map filterItem(Object item) {
				TypedFlatMap result = new TypedFlatMap();
				VersaoDescritivoPce bean = (VersaoDescritivoPce)item;
				DescritivoPce descritivoPce = bean.getDescritivoPce();
				OcorrenciaPce ocorrenciaPce = descritivoPce.getOcorrenciaPce();
				EventoPce eventoPce = ocorrenciaPce.getEventoPce();
				ProcessoPce processoPce = eventoPce.getProcessoPce();

				result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.idProcessoPce",processoPce.getIdProcessoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.dsProcessoPce",processoPce.getDsProcessoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce",processoPce.getCdProcessoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.processoPceGrid",processoPce.getProcessoPceGrid());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.idEventoPce",eventoPce.getIdEventoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce",eventoPce.getDsEventoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce",eventoPce.getCdEventoPce());
				result.put("descritivoPce.ocorrenciaPce.eventoPce.eventoPceGrid",eventoPce.getEventoPceGrid());
				result.put("descritivoPce.ocorrenciaPce.idOcorrenciaPce",ocorrenciaPce.getIdOcorrenciaPce());
				result.put("descritivoPce.ocorrenciaPce.dsOcorrenciaPce",ocorrenciaPce.getDsOcorrenciaPce());
				result.put("descritivoPce.ocorrenciaPce.cdOcorrenciaPce",ocorrenciaPce.getCdOcorrenciaPce());
				result.put("descritivoPce.ocorrenciaPce.ocorrenciaPceGrid",ocorrenciaPce.getOcorrenciaPceGrid());
				result.put("descritivoPce.idDescritivoPce",descritivoPce.getIdDescritivoPce());
				result.put("descritivoPce.dsDescritivoPce",descritivoPce.getDsDescritivoPce());
				result.put("descritivoPce.cdDescritivoPce",descritivoPce.getCdDescritivoPce());
				result.put("descritivoPce.blIndicadorAviso",descritivoPce.getBlIndicadorAviso());
				result.put("idVersaoDescritivoPce",bean.getIdVersaoDescritivoPce());
				
				return result;
			}
    		
    	}.doFilter();
    }
    
    public Integer getRowCountVersaoDescritivasPce(Map parameters){
    	return getRowCountItemList(parameters, ALIAS_NAME);
    }
    
    public Object findByIdVersaoDescritivasPce(MasterDetailKey key) {
    	VersaoDescritivoPce bean = (VersaoDescritivoPce)findItemById(key, ALIAS_NAME);
		TypedFlatMap result = new TypedFlatMap();
  
		DescritivoPce descritivoPce = bean.getDescritivoPce();
		OcorrenciaPce ocorrenciaPce = descritivoPce.getOcorrenciaPce();
		EventoPce eventoPce = ocorrenciaPce.getEventoPce();
		ProcessoPce processoPce = eventoPce.getProcessoPce();

		result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.idProcessoPce",processoPce.getIdProcessoPce());
		result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.dsProcessoPce",processoPce.getDsProcessoPce());
		result.put("descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce",processoPce.getCdProcessoPce());
		
		result.put("descritivoPce.ocorrenciaPce.eventoPce.idEventoPce",eventoPce.getIdEventoPce());
		result.put("descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce",eventoPce.getDsEventoPce());
		result.put("descritivoPce.ocorrenciaPce.eventoPce.cdEventoPce",eventoPce.getCdEventoPce());
		
		result.put("descritivoPce.ocorrenciaPce.idOcorrenciaPce",ocorrenciaPce.getIdOcorrenciaPce());
		result.put("descritivoPce.ocorrenciaPce.dsOcorrenciaPce",ocorrenciaPce.getDsOcorrenciaPce());
		result.put("descritivoPce.ocorrenciaPce.cdOcorrenciaPce",ocorrenciaPce.getCdOcorrenciaPce());
		
		result.put("descritivoPce.idDescritivoPce",descritivoPce.getIdDescritivoPce());
		result.put("descritivoPce.dsDescritivoPce",descritivoPce.getDsDescritivoPce());
		result.put("descritivoPce.cdDescritivoPce",descritivoPce.getCdDescritivoPce());
		
		result.put("descritivoPce.blIndicadorAviso",descritivoPce.getBlIndicadorAviso());

		List list = bean.getVersaoContatoPces();
		List newList = new ArrayList();
		List newListFuncionario = new ArrayList();
		TypedFlatMap resultListFuncionario = null;
		TypedFlatMap resultList = null;
		if (list != null) {
			for (Iterator i = list.iterator(); i.hasNext();) {
				VersaoContatoPce versaoContatoPce = (VersaoContatoPce)i.next();
				if(versaoContatoPce.getContato()!= null){
					resultList = new TypedFlatMap(); 
					Contato contato = versaoContatoPce.getContato();
					resultList.put("contato.idContato",contato.getIdContato());
					resultList.put("contato.nmContato",contato.getNmContato());
					resultList.put("idVersaoContatoPce",versaoContatoPce.getIdVersaoContatoPce());
					resultList.put("tpFormaComunicacao.value",versaoContatoPce.getTpFormaComunicacao().getValue());
					resultList.put("tpFormaComunicacao.description",versaoContatoPce.getTpFormaComunicacao().getDescription());
					resultList.put("dsRegiaoAtuacao",versaoContatoPce.getDsRegiaoAtuacao());
					newList.add(resultList);
				}
				if(versaoContatoPce.getUsuario()!= null){
					resultListFuncionario = new TypedFlatMap(); 
					Usuario usuario = versaoContatoPce.getUsuario();
					resultListFuncionario.put("usuario.idUsuario",usuario.getIdUsuario());
					resultListFuncionario.put("usuario.codPessoa.nome",usuario.getNmUsuario());
					resultListFuncionario.put("usuario.nrMatricula",usuario.getNrMatricula());
					resultListFuncionario.put("idVersaoContatoPce",versaoContatoPce.getIdVersaoContatoPce());
					newListFuncionario.add(resultListFuncionario);
				}
				
					
			}
			if(!newList.isEmpty())
				result.put("versaoContatoPces",newList);
			if(!newListFuncionario.isEmpty())
				result.put("versaoContatoPces1",newListFuncionario);
		}
		result.put("idVersaoDescritivoPce",bean.getIdVersaoDescritivoPce());
		return result;
    }
    
    /***
     * Remove uma lista de registros items.
     *  
     * @param ids ids dos registros item a serem removidos.
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsVersaoDescritivasPce(List ids) {
    	removeItemByIds(ids, ALIAS_NAME);
    }

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(VersaoPce.class);

		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
    	Comparator descComparator = new Comparator() {
    		
			public int compare(Object o1, Object o2) {
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					return 0;
				}
			}
    	};
    	
    	
   	ItemListConfig itemInit = new ItemListConfig()  {

			public List initialize(Long masterId) {
				if (masterId == null)
					return new ArrayList(0); 
				else
					return ((VersaoPceService)getMasterService()).findVersaoDescritivoPceByIdVersaoPce(masterId);
			}

			public Integer getRowCount(Long masterId) {
				if (masterId == null)
					return Integer.valueOf(0); 
				else
					return ((VersaoPceService)getMasterService()).getRowCountVersaoDescritivoPceByIdVersaoPce(masterId);
			}

			public void modifyItemValues(Object newValues, Object bean) {
				VersaoDescritivoPce nowBean = (VersaoDescritivoPce)bean;
				VersaoDescritivoPce newBean = (VersaoDescritivoPce)newValues;

				List versaoContatosPce = new ArrayList();
				for (Iterator i = newBean.getVersaoContatoPces().iterator(); i.hasNext();) {
					VersaoContatoPce versaoContatoPceNew = (VersaoContatoPce)i.next();
					VersaoContatoPce versaoContatoPce = new VersaoContatoPce();
					versaoContatoPce.setIdVersaoContatoPce(versaoContatoPceNew.getIdVersaoContatoPce());
					versaoContatoPce.setTpFormaComunicacao(versaoContatoPceNew.getTpFormaComunicacao());
					versaoContatoPce.setDsRegiaoAtuacao(versaoContatoPceNew.getDsRegiaoAtuacao());
					versaoContatoPce.setContato(versaoContatoPceNew.getContato());
					versaoContatoPce.setUsuario(versaoContatoPceNew.getUsuario());
					versaoContatosPce.add(versaoContatoPce);
				}
				nowBean.setIdVersaoDescritivoPce(newBean.getIdVersaoDescritivoPce());
				nowBean.setDescritivoPce(newBean.getDescritivoPce());
				nowBean.setVersaoContatoPces(versaoContatosPce);

			}
    		
			public Map configItemDomainProperties() {
				return null;
			}

			public void setMasterOnItem(Object master, Object itemBean) {
				((VersaoDescritivoPce)itemBean).setVersaoPce((VersaoPce)master);
			}
			
			public Object populateNewItemInstance(Map parametersTemp, Object beanTemp) {
				VersaoDescritivoPce bean = (VersaoDescritivoPce)beanTemp;
				TypedFlatMap parameters = (TypedFlatMap)parametersTemp;
				
				ProcessoPce processoPce = new ProcessoPce();
				processoPce.setIdProcessoPce(parameters.getLong("descritivoPce.ocorrenciaPce.eventoPce.processoPce.idProcessoPce"));
				processoPce.setDsProcessoPce(parameters.getVarcharI18n("descritivoPce.ocorrenciaPce.eventoPce.processoPce.dsProcessoPce"));
				processoPce.setCdProcessoPce(parameters.getLong("descritivoPce.ocorrenciaPce.eventoPce.processoPce.cdProcessoPce"));
				
				EventoPce eventoPce = new EventoPce();
				eventoPce.setIdEventoPce(parameters.getLong("descritivoPce.ocorrenciaPce.eventoPce.idEventoPce"));
				eventoPce.setDsEventoPce(parameters.getVarcharI18n("descritivoPce.ocorrenciaPce.eventoPce.dsEventoPce"));
				eventoPce.setCdEventoPce(parameters.getLong("descritivoPce.ocorrenciaPce.eventoPce.cdEventoPce"));
				eventoPce.setProcessoPce(processoPce);
				
				OcorrenciaPce ocorrenciaPce = new OcorrenciaPce();
				ocorrenciaPce.setIdOcorrenciaPce(parameters.getLong("descritivoPce.ocorrenciaPce.idOcorrenciaPce"));
				ocorrenciaPce.setDsOcorrenciaPce(parameters.getVarcharI18n("descritivoPce.ocorrenciaPce.dsOcorrenciaPce"));
				ocorrenciaPce.setCdOcorrenciaPce(parameters.getLong("descritivoPce.ocorrenciaPce.cdOcorrenciaPce"));
				ocorrenciaPce.setEventoPce(eventoPce);
				
				DescritivoPce descritivoPce = new DescritivoPce();
				descritivoPce.setIdDescritivoPce(parameters.getLong("descritivoPce.idDescritivoPce"));
				descritivoPce.setDsDescritivoPce(parameters.getVarcharI18n("descritivoPce.dsDescritivoPce"));
				descritivoPce.setCdDescritivoPce(parameters.getLong("descritivoPce.cdDescritivoPce"));
				descritivoPce.setBlIndicadorAviso(parameters.getBoolean("descritivoPce.blIndicadorAviso"));
				descritivoPce.setOcorrenciaPce(ocorrenciaPce);
				
				List versaoContatosPce = new ArrayList();
				
				//LISTA DE CONTATOS
				List list = parameters.getList("versaoContatoPces");
				if (list != null) {
					for (Iterator i = list.iterator(); i.hasNext();) {
						TypedFlatMap parameters2 = (TypedFlatMap)i.next();
						VersaoContatoPce versaoContatoPce = new VersaoContatoPce();
						versaoContatoPce.setIdVersaoContatoPce(parameters2.getLong("idVersaoContatoPce"));
						DomainValue tpFormaComunicacao = parameters2.getDomainValue("tpFormaComunicacao.value");
						tpFormaComunicacao.setDescription(parameters2.getVarcharI18n("tpFormaComunicacao.description"));
						versaoContatoPce.setTpFormaComunicacao(tpFormaComunicacao);
						versaoContatoPce.setDsRegiaoAtuacao(parameters2.getString("dsRegiaoAtuacao"));
						Contato contato = new Contato();
						contato.setIdContato(parameters2.getLong("contato.idContato"));
						contato.setNmContato(parameters2.getString("contato.nmContato"));
												
						versaoContatoPce.setContato(contato);
						versaoContatosPce.add(versaoContatoPce);
					}
				}
				
				//LISTA DE FUNCIONARIO
				List listFuncionario = parameters.getList("versaoContatoPces1");
				if (listFuncionario != null) {
					if(!listFuncionario.isEmpty()){
						for (Iterator i = listFuncionario.iterator(); i.hasNext();) {
							TypedFlatMap parametersFunc = (TypedFlatMap)i.next();
							VersaoContatoPce versaoContatoPce = new VersaoContatoPce();
							versaoContatoPce.setIdVersaoContatoPce(parametersFunc.getLong("idVersaoContatoPce"));
							DomainValue tpFormaComunicacao = domainValueService.findDomainValueByValue("DM_FORMA_COMUNICACAO_PCE", "E");
							tpFormaComunicacao.setDescription(tpFormaComunicacao.getDescription());
							versaoContatoPce.setTpFormaComunicacao(tpFormaComunicacao);
													
							Usuario usuario = new Usuario();
							usuario.setIdUsuario(parametersFunc.getLong("usuario.idUsuario"));
							usuario.setNrMatricula(parametersFunc.getString("usuario.nrMatricula"));
							usuario.setNmUsuario(parametersFunc.getString("usuario.codPessoa.nome"));
							versaoContatoPce.setUsuario(usuario);
							versaoContatosPce.add(versaoContatoPce);
					
						}
					}
				}
				
				bean.setIdVersaoDescritivoPce(parameters.getLong("idVersaoDescritivoPce"));
				bean.setDescritivoPce(descritivoPce);
				bean.setVersaoContatoPces(versaoContatosPce);

				List contatos = bean.getVersaoContatoPces();
				if (contatos != null) {  
					for (Iterator ie = contatos.iterator(); ie.hasNext();) {
						VersaoContatoPce vc = (VersaoContatoPce)ie.next();
						if(vc.getContato()!= null){
							if (vc.getTpFormaComunicacao().getValue().equals("E")) {
								//Email
								Contato c = contatoService.findById(vc.getContato().getIdContato());
								if (c.getDsEmail() == null)
									throw new BusinessException("LMS-01089");
								
							}else if (vc.getTpFormaComunicacao().getValue().equals("T")) {
								//Telefone
								if (versaoPceService.findTelefoneContatoByTpUso(vc.getContato().getIdContato(),new String[]{"FF","FO"}).size() == 0)
									throw new BusinessException("LMS-01088");  
							}else if (vc.getTpFormaComunicacao().getValue().equals("F")) {
								//Fax
								if (versaoPceService.findTelefoneContatoByTpUso(vc.getContato().getIdContato(),new String[]{"FF","FA"}).size() == 0)
									throw new BusinessException("LMS-01086");  
							}else if (vc.getTpFormaComunicacao().getValue().equals("C")) {
								if (versaoPceService.getRowCountEnderecoPessoaByContato(vc.getContato().getIdContato()) == 0)
									throw new BusinessException("LMS-01087");
							}else if (vc.getTpFormaComunicacao().getValue().equals("S")) {
								if (versaoPceService.findTelefoneContatoByTpTelefone(vc.getContato().getIdContato(),new String[]{"E"}).size() == 0)
									throw new BusinessException("LMS-01109");
							}
						}
					}
				}
				return bean;
			}

    	};
		
		config.addItemConfig(ALIAS_NAME, VersaoDescritivoPce.class, itemInit, descComparator);
		return config;
	}
	public ProcessoPceService getProcessoPceService() {
		return processoPceService;
	}
	public void setProcessoPceService(ProcessoPceService processoPceService) {
		this.processoPceService = processoPceService;
	}

	public EventoPceService getEventoPceService() {
		return eventoPceService;
	}
	public void setEventoPceService(EventoPceService eventoPceService) {
		this.eventoPceService = eventoPceService;
	}
	public DescritivoPceService getDescritivoPceService() {
		return descritivoPceService;
	}
	public void setDescritivoPceService(DescritivoPceService descritivoPceService) {
		this.descritivoPceService = descritivoPceService;
	}
	public ContatoService getContatoService() {
		return contatoService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public VersaoPceService getVersaoPceService() {
		return versaoPceService;
	}

	public void setVersaoPceService(VersaoPceService versaoPceService) {
		this.versaoPceService = versaoPceService;
		setMasterService(versaoPceService);
	}

	public OcorrenciaPceService getOcorrenciaPceService() {
		return ocorrenciaPceService;
	}

	public void setOcorrenciaPceService(OcorrenciaPceService ocorrenciaPceService) {
		this.ocorrenciaPceService = ocorrenciaPceService;
	}
	
	public List findLookupFuncionario(TypedFlatMap parameters) {
		List lista = usuarioService.findLookupUsuarioFuncionario(null, parameters
				.getString("nrMatricula"), null, null, null, null, true);
		if(lista.size()==1){
			Map map = (Map)lista.get(0);
			map.put("nmFuncionario", map.get("nmUsuario"));
		}
		
		return lista;
	}

	
}

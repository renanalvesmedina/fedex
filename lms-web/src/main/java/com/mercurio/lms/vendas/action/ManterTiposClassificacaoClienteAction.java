package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.DescClassificacaoCliente;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;
import com.mercurio.lms.vendas.model.service.TipoClassificacaoClienteService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterTiposClassificacaoClienteAction"
 */

public class ManterTiposClassificacaoClienteAction extends MasterDetailAction {
	
	/**
	 * Declara��o servi�o principal da Action.
	 * 
	 * @param tipoClassificacaoClienteService
	 */
	
	public void setTipoClassificacaoClienteService(TipoClassificacaoClienteService tipoClassificacaoClienteService) {
		super.setMasterService(tipoClassificacaoClienteService);
	}
	
	public TipoClassificacaoClienteService getTipoClassificacaoClienteService() {
		return (TipoClassificacaoClienteService)super.getMasterService();
	}
	
	/**
	 * M�todo que salva as altera��es feitas no mestre e nos detalhes
	 * author Micka�l Jalbert
	 * 31/10/2005
	 * @param tipoRegistroComplementoTela
	 * @return id gerado para o mestre
	 */
	public TypedFlatMap store(TipoClassificacaoCliente tipoClassificacaoClienteTela) {
		TypedFlatMap retorno = new TypedFlatMap();
		MasterEntry entry = getMasterFromSession(tipoClassificacaoClienteTela.getIdTipoClassificacaoCliente(), true);		
		ItemList items = getItemsFromSession(entry, "descClassificacaoCliente");		

		TipoClassificacaoCliente tipoClassificacaoCliente = (TipoClassificacaoCliente) entry.getMaster();
		Set ignore = new HashSet(1);
		ignore.add("versao");
		ReflectionUtils.syncObjectProperties(tipoClassificacaoCliente, tipoClassificacaoClienteTela, ignore);
		
		TipoClassificacaoCliente tipoClassificacaoClienteStored = getTipoClassificacaoClienteService().store(tipoClassificacaoCliente, items);
		items.resetItemsState(); 
		updateMasterInSession(entry);
		
		retorno.put("idTipoClassificacaoCliente",tipoClassificacaoClienteStored.getIdTipoClassificacaoCliente().toString());
		retorno.put("empresa.idEmpresa",tipoClassificacaoClienteStored.getEmpresa().getIdEmpresa().toString());			
		return retorno;

	}
	
	/**
	 * Busca um mestre pelo seu id e armazena-o na sess�o do usu�rio
	 * author Micka�l Jalbert
	 * 31/10/2005
	 * @param id
	 * @return um TipoRegistroComplemento, objeto mestre
	 */
	public Object findById(java.lang.Long id) {
		Object masterObj = getTipoClassificacaoClienteService().findById(id);
		putMasterInSession(masterObj); 
		return masterObj;
	}
	
	/**
	 * Remove uma listra de registros mestres
	 * author Micka�l Jalbert
	 * 31/10/2005
	 * @param ids
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getTipoClassificacaoClienteService().removeByIds(ids);
	}
	
	/**
	 * Remove um registro mestre
	 * author Micka�l Jalbert
	 * 31/10/2005
	 * @param id
	 */
	public void removeById(Long id) {	
		getTipoClassificacaoClienteService().removeById(id);
		newMaster();			
	}
	
	
	/**
	 * Salva um registro detalhe/filho na sess�o.
	 * author Micka�l Jalbert
	 * 31/10/2005
	 * @param parameters Parametros utilizado para montar o detalhe
	 * @return id do detalhe (tempor�rio no caso de inser��o)
	 */
	public Serializable saveDescClassificacaoCliente(Map parameters) {
    	// S� pode ter uma descri��o padr�o
		if (((String)parameters.get("blPadrao")).equals("true")){
	    	Long masterId = (Long) getMasterId(parameters);
	    	MasterEntry entry = getMasterFromSession(masterId, true);
	    	ItemList items = getItemsFromSession(entry, "descClassificacaoCliente");
	    	ItemListConfig config = getMasterConfig().getItemListConfig("descClassificacaoCliente");
	    	
	    	Long idFilho = null;
	    	
	    	// Por cada descri��o do tipo
	    	if( !parameters.get("idDescClassificacaoCliente").equals("") ){
	    		idFilho = Long.valueOf((String)parameters.get("idDescClassificacaoCliente"));
	    	}
	    	
	    	for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
	    		DescClassificacaoCliente descClassificacaoCliente = (DescClassificacaoCliente) iter.next();
	    		// Se j� tem um cadastrado padr�o lan�a uma exception
	    		if (descClassificacaoCliente.getBlPadrao().equals(Boolean.TRUE)){
	    			
	    			if( idFilho != null ){
	    				if( !descClassificacaoCliente.getIdDescClassificacaoCliente().equals(idFilho) ){
	    					throw new BusinessException("LMS-01073");
	    				} else {
	    					continue;
	    				}
	    			} else {
	    				throw new BusinessException("LMS-01073");
	    			}	    			
	    			
	    		}
			}
		}
		return saveItemInstance(parameters, "descClassificacaoCliente");
	}
	
	
	public ResultSetPage findPaginatedDescClassificacaoCliente(Map parameters) {
		return findPaginatedItemList(parameters, "descClassificacaoCliente");
	}
	
	public Integer getRowCountDescClassificacaoCliente(Map parameters){
		return getRowCountItemList(parameters, "descClassificacaoCliente");
	}
	
	public Object findByIdDescClassificacaoCliente(MasterDetailKey key) {
		return findItemById(key, "descClassificacaoCliente");
	}

	/***
	 * Remove uma lista de desci�oes items.
	 *  
	 * @param ids ids dos desci�oes item a serem removidos.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDescClassificacaoCliente(List ids) {
		super.removeItemByIds(ids, "descClassificacaoCliente");
	}	

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(TipoClassificacaoCliente.class);
		
		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
		Comparator descComparator = new Comparator() {
			
			public int compare(Object o1, Object o2) {
				Collator collator = Collator.getInstance(LocaleContextHolder.getLocale());
				
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					DescClassificacaoCliente d1 = (DescClassificacaoCliente) o1;
					DescClassificacaoCliente d2 = (DescClassificacaoCliente) o2;
					
					return collator.compare(d1.getDsDescClassificacaoCliente().getValue(),
							d2.getDsDescClassificacaoCliente().getValue());
				}
			}
			
		};
		
		
		// Esta instancia ser� responsavel por carregar os items filhos na sess�o a partir do banco de dados.
		ItemListConfig itemInit = new ItemListConfig() {
			
			
			public List initialize(Long masterId) {
				return getTipoClassificacaoClienteService().findDescClassificacaoCliente(masterId);
			}

			public Integer getRowCount(Long masterId) {
				return getTipoClassificacaoClienteService().getRowCountDescClassificacaoCliente(masterId);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet(3, 1f);
				ignore.add("idDescClassificacaoCliente");
				ignore.add("versao");
				ignore.add("tipoClassificacaoCliente");
				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			public Map configItemDomainProperties() {
				Map props = new HashMap(2, 1f);
				props.put("tpSituacao", "DM_STATUS");
				props.put("tpCampo", "DM_TIPO_CAMPO");
				return props;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				DescClassificacaoCliente idsNew = (DescClassificacaoCliente)bean;
				ReflectionUtils.copyNestedBean(idsNew, parameters);
	    		resolveDomainValueProperties(idsNew);
				ItemList items = getItemsFromSession(getMasterFromSession(getMasterId(parameters), true), "descClassificacaoCliente");
				ItemListConfig config = getMasterConfig().getItemListConfig("descClassificacaoCliente");
				for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
					DescClassificacaoCliente ids = (DescClassificacaoCliente) iter.next();
					if(idsNew.getDsDescClassificacaoCliente().getValue().equalsIgnoreCase(ids.getDsDescClassificacaoCliente().getValue()) 
							&& !ids.getIdDescClassificacaoCliente().equals(idsNew.getIdDescClassificacaoCliente()))
						throw new BusinessException("uniqueConstraintViolated", new Object[] {"idDescClassificacaoCliente"});
				}
				return idsNew;
			}
			
		};
		
		config.addItemConfig("descClassificacaoCliente",DescClassificacaoCliente.class, itemInit, descComparator);
		return config;
	}

}

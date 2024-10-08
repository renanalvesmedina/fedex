package com.mercurio.lms.expedicao.action;

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
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.TipoRegistroComplemento;
import com.mercurio.lms.expedicao.model.service.TipoRegistroComplementoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterTiposRegistroComplementoDocumentoServicoAction"
 */

public class ManterTiposRegistroComplementoDocumentoServicoAction extends MasterDetailAction {
	
	/**
	 * Declara��o servi�o principal da Action.
	 * 
	 * @param tipoRegistroComplementoService
	 */
	
	public void setTipoRegistroComplementoService(TipoRegistroComplementoService tipoRegistroComplementoService) {
		super.setMasterService(tipoRegistroComplementoService);
	}
	
	public TipoRegistroComplementoService getTipoRegistroComplementoService() {
		return (TipoRegistroComplementoService)super.getMasterService();
	}
	
	/**
	 * M�todo que salva as altera��es feitas no mestre e nos detalhes
	 * autor Julio Cesar Fernandes Corr�a
	 * 25/10/2005
	 * @param tipoRegistroComplementoTela
	 * @return id gerado para o mestre
	 */
	public Serializable store(TypedFlatMap parameters) {
		MasterEntry entry = getMasterFromSession(parameters.getLong("idTipoRegistroComplemento"), true);
		TipoRegistroComplemento tipoRegistroComplemento = (TipoRegistroComplemento) entry.getMaster();
		tipoRegistroComplemento.setDsTipoRegistroComplemento(parameters.getString("dsTipoRegistroComplemento"));
		ItemList items = getItemsFromSession(entry, InformacaoDocServico.class.getName());
		Serializable id = getTipoRegistroComplementoService().store(tipoRegistroComplemento, items);
		items.resetItemsState(); 
    	updateMasterInSession(entry);
		return id;    	
	}
	
	/**
	 * Busca um mestre pelo seu id e armazena-o na sess�o do usu�rio
	 * autor Julio Cesar Fernandes Corr�a
	 * 25/10/2005
	 * @param id
	 * @return um TipoRegistroComplemento, objeto mestre
	 */
	public Object findById(java.lang.Long id) {
		Object masterObj = getTipoRegistroComplementoService().findById(id);
		putMasterInSession(masterObj); 
		return masterObj;
	}
	
	/**
	 * Remove uma listra de registros mestres
	 * autor Julio Cesar Fernandes Corr�a
	 * 25/10/2005
	 * @param ids
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getTipoRegistroComplementoService().removeByIds(ids);
	}
	
	/**
	 * Remove um registro mestre
	 * autor Julio Cesar Fernandes Corr�a
	 * 25/10/2005
	 * @param id
	 */
	public void removeById(Long id) {
		getTipoRegistroComplementoService().removeById(id);
		newMaster();
	}
	
	
	/**
	 * Salva um registro detalhe/filho na sess�o.
	 * autor Julio Cesar Fernandes Corr�a
	 * 25/10/2005
	 * @param parameters Parametros utilizado para montar o detalhe
	 * @return id do detalhe (tempor�rio no caso de inser��o)
	 */
	public Serializable saveInformacaoDocServico(TypedFlatMap parameters) {
		Long masterId = getMasterId(parameters);
		ItemList items = getItemsFromSession(getMasterFromSession(masterId, true), InformacaoDocServico.class.getName());
		ItemListConfig config = getMasterConfig().getItemListConfig(InformacaoDocServico.class.getName());
		String dsCampo = parameters.getString("dsCampo");
		Long id = parameters.getLong("idInformacaoDocServico");
		for (Iterator iter = items.iterator(masterId, config); iter.hasNext();) {
			InformacaoDocServico ids = (InformacaoDocServico) iter.next();
			if(dsCampo.equalsIgnoreCase(ids.getDsCampo()) 
					&& !ids.getIdInformacaoDocServico().equals(id))
				throw new BusinessException("uniqueConstraintViolated", new Object[] {"idInformacaoDocServico"});
		}
		return saveItemInstance(parameters, InformacaoDocServico.class.getName());
	}
	
	
	public ResultSetPage findPaginatedInformacaoDocServico(Map parameters) {
		return findPaginatedItemList(parameters, InformacaoDocServico.class.getName());
	}
	
	public Integer getRowCountInformacaoDocServico(Map parameters){
		return getRowCountItemList(parameters, InformacaoDocServico.class.getName());
	}
	
	public Object findByIdInformacaoDocServico(MasterDetailKey key) {
		return findItemById(key, InformacaoDocServico.class.getName());
	}

	/***
	 * Remove uma lista de registros items.
	 *  
	 * @param ids ids dos registros item a serem removidos.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsInformacaoDocServico(List ids) {
		super.removeItemByIds(ids, InformacaoDocServico.class.getName());
	}
	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(TipoRegistroComplemento.class);
		
		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
		Comparator descComparator = new Comparator() {
			
			public int compare(Object o1, Object o2) {
				Collator collator = Collator.getInstance(LocaleContextHolder.getLocale());
				
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					InformacaoDocServico d1 = (InformacaoDocServico) o1;
					InformacaoDocServico d2 = (InformacaoDocServico) o2;
					
					return collator.compare(d1.getDsCampo(),
							d2.getDsCampo());
				}
			}
			
		};
		
		
		// Esta instancia ser� responsavel por carregar os items filhos na sess�o a partir do banco de dados.
		ItemListConfig itemInit = new ItemListConfig() {
			
			
			public List initialize(Long masterId) {
				return getTipoRegistroComplementoService().findInformacaoDocServico(masterId);
			}

			public Integer getRowCount(Long masterId) {
				return getTipoRegistroComplementoService().getRowCountInformacaoDocServico(masterId);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet(3, 1f);
				ignore.add("idInformacaoDocServico");
				ignore.add("versao");
				ignore.add("tipoRegistroComplemento");
				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			public Map configItemDomainProperties() {
				Map props = new HashMap(2, 1f);
				props.put("tpSituacao", "DM_STATUS");
				props.put("tpCampo", "DM_TIPO_CAMPO");
				return props;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap params = (TypedFlatMap)parameters;
				InformacaoDocServico idsNew = (InformacaoDocServico)bean;
				idsNew.setBlImprimeConhecimento(params.getBoolean("blImprimeConhecimento"));
				idsNew.setBlIndicadorNotaFiscal(params.getBoolean("blIndicadorNotaFiscal"));
	    		idsNew.setBlOpcional(params.getBoolean("blOpcional"));
	    		idsNew.setDsCampo(params.getString("dsCampo"));
	    		idsNew.setDsFormatacao(params.getString("dsFormatacao"));
	    		idsNew.setNrDecimais(params.getShort("nrDecimais"));
	    		idsNew.setNrTamanho(params.getShort("nrTamanho"));
	    		idsNew.setTpCampo(params.getDomainValue("tpCampo"));
	    		idsNew.setTpSituacao(params.getDomainValue("tpSituacao"));
	    		idsNew.setIdInformacaoDocServico(params.getLong("idInformacaoDocServico"));
				resolveDomainValueProperties(idsNew);
				return idsNew;
			}
			
		};
		
		config.addItemConfig(InformacaoDocServico.class.getName(), InformacaoDocServico.class, itemInit, descComparator);
		return config;
	}
	
	
}

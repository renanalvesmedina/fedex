package com.mercurio.lms.municipios.action;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

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
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EixosTipoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
import com.mercurio.lms.municipios.model.service.TarifaPostoPassagemService;
import com.mercurio.lms.util.JTVigenciaUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterTarifaPostoPassagemAction"
 */

public class ManterTarifaPostoPassagemAction extends MasterDetailAction {
	
	private MoedaPaisService moedaPaisService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private EixosTipoMeioTransporteService eixosTipoMeioTransporteService;
	
	public void setTarifaPostoPassagemService(TarifaPostoPassagemService tarifaPostoPassagemService) {
		super.setMasterService(tarifaPostoPassagemService);
	}
	
	private TarifaPostoPassagemService getService() {
		return (TarifaPostoPassagemService)super.getMasterService();
	}

	public List findComboEixosTipoMeioTransporte(Long idMeioTransporte) {
		List eixos = eixosTipoMeioTransporteService.findSumEixosByTpMeioTransp(idMeioTransporte);
		List newList = new ArrayList();
		for(Iterator i = eixos.iterator(); i.hasNext();) {
			Integer qtEixo = (Integer)i.next();
			TypedFlatMap row = new TypedFlatMap();
			row.put("qtEixos",qtEixo);
			newList.add(row);
		}
		return newList;
	}
	

	public List findComboTipoMeioTransporte(Map criteria) {
		if (criteria == null)
			criteria = new HashMap();
		criteria.put("tpSituacao","A");
		
		List rs = tipoMeioTransporteService.findTiposSemComposicao(criteria);
		List newList = new ArrayList();
		for(Iterator i = rs.iterator(); i.hasNext();) {
			TipoMeioTransporte tmt = (TipoMeioTransporte)i.next();
			TypedFlatMap row = new TypedFlatMap();
			row.put("idTipoMeioTransporte",tmt.getIdTipoMeioTransporte());
			row.put("dsTipoMeioTransporte",tmt.getDsTipoMeioTransporte());
			if (tmt.getTipoMeioTransporte() != null)
				row.put("tipoMeioTransporte.dsTipoMeioTransporte",tmt.getTipoMeioTransporte().getDsTipoMeioTransporte());
			newList.add(row);
		}
		return newList;
	}
	
    public Map store(TypedFlatMap flat) {  
    	if (flat.getDomainValue("tpFormaCobranca").getValue().equals("TI")) {
    		MasterEntry entry = getMasterFromSession(flat.getLong("idTarifaPostoPassagem"),true);
    		TarifaPostoPassagem bean = (TarifaPostoPassagem)entry.getMaster();
    		PostoPassagem postoPassagem = new PostoPassagem();
    		postoPassagem.setIdPostoPassagem(flat.getLong("postoPassagem.idPostoPassagem"));
    		bean.setPostoPassagem(postoPassagem);
    		bean.setIdTarifaPostoPassagem(flat.getLong("idTarifaPostoPassagem"));
    		bean.setDtVigenciaFinal(flat.getYearMonthDay("dtVigenciaFinal"));
    		bean.setDtVigenciaInicial(flat.getYearMonthDay("dtVigenciaInicial"));
    		bean.setTpFormaCobranca(flat.getDomainValue("tpFormaCobranca"));
    		ItemList items = getItemsFromSession(entry, ValorTarifaPostoPassagem.class.getName());

    		Map returnMap = getService().store(bean, items);
    		items.resetItemsState(); 
    		updateMasterInSession(entry);
    		return returnMap;
    	}else{
    		return getService().store(flat);
    	}
    }

    public Map findById(java.lang.Long id) {
		TarifaPostoPassagem masterObj = getService().findById(id);
		putMasterInSession(masterObj);
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("idTarifaPostoPassagem",masterObj.getIdTarifaPostoPassagem());
		map.put("dtVigenciaFinal",masterObj.getDtVigenciaFinal());
		map.put("dtVigenciaInicial",masterObj.getDtVigenciaInicial());
		map.put("tpFormaCobranca",masterObj.getTpFormaCobranca().getValue());
		map.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(masterObj,Integer.valueOf(1)));
		
    	if (!masterObj.getTpFormaCobranca().getValue().equals("TI")) {
    		ValorTarifaPostoPassagem vtpp = null;
    		if (masterObj.getValorTarifaPostoPassagems().size() > 0)
        		vtpp = (ValorTarifaPostoPassagem)masterObj.getValorTarifaPostoPassagems().get(0);
    		map.put("valorTarifaPostoPassagem.idValorTarifaPostoPassagem",vtpp.getIdValorTarifaPostoPassagem());
    		map.put("valorTarifaPostoPassagem.moedaPais.idMoedaPais",vtpp.getMoedaPais().getIdMoedaPais());
    		map.put("valorTarifaPostoPassagem.vlTarifa",vtpp.getVlTarifa());
    	}
    	return map;
    }
    
    
    /***
     * Remo��o de um conjunto de registros Master.
     * 
     * @param ids
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getService().removeByIds(ids);
    }

    /**
     * Remo��o de um registro Master.
     * @param id
     */
    public void removeById(Long id) {
    	getService().removeById(id);
    }
    
    /**
     * Salva um item Descri��o Padr�o na sess�o.
     * 
     * @param bean
     * @return
     */
    public Serializable saveValorTarifaPostoPassagem(TypedFlatMap parameters) {
    	if (parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte") != null) {
    		Long masterId = (Long) getMasterId(parameters);
    		MasterEntry entry = getMasterFromSession(masterId, true);
    		ItemList items = getItemsFromSession(entry,ValorTarifaPostoPassagem.class.getName());
    		ItemListConfig config = getMasterConfig().getItemListConfig(ValorTarifaPostoPassagem.class.getName());
    		
    		Long idValorTarifa = parameters.getLong("idValorTarifaPostoPassagem");
			Long idMeioTransporte = parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte");
			Integer qtEixos = parameters.getInteger("qtEixos");

    		for (Iterator iter = items.iterator(masterId, config); iter.hasNext();) {
    			ValorTarifaPostoPassagem bean = (ValorTarifaPostoPassagem) iter.next();
    			
    			if ((idValorTarifa == null || !bean.getIdValorTarifaPostoPassagem().equals(idValorTarifa)) &&
    					bean.getTipoMeioTransporte().getIdTipoMeioTransporte().equals(idMeioTransporte) &&
    					bean.getQtEixos().equals(qtEixos))
    					throw new BusinessException("LMS-29036");

    		}
    	}  
    	return saveItemInstance(parameters, ValorTarifaPostoPassagem.class.getName());
    }
    
    public ResultSetPage findPaginatedValorTarifaPostoPassagem(Map parameters) {
    	ResultSetPage rsp = findPaginatedItemList(parameters, ValorTarifaPostoPassagem.class.getName());
    	List newList = new ArrayList();
    	for(Iterator ie = rsp.getList().iterator(); ie.hasNext();) {
    		ValorTarifaPostoPassagem bean = (ValorTarifaPostoPassagem)ie.next();
    		TypedFlatMap result = new TypedFlatMap();
    		Moeda moeda = bean.getMoedaPais().getMoeda();
    		TipoMeioTransporte tipoMeioTransporte = bean.getTipoMeioTransporte();
			result.put("moedaPais.moeda.sgMoeda",moeda.getSgMoeda());
    		result.put("moedaPais.moeda.dsSimbolo",moeda.getDsSimbolo());
    		result.put("vlTarifa",bean.getVlTarifa());
    		result.put("qtEixos",bean.getQtEixos()); 
    		result.put("idValorTarifaPostoPassagem",bean.getIdValorTarifaPostoPassagem());
			result.put("tipoMeioTransporte.dsTipoMeioTransporte",tipoMeioTransporte.getDsTipoMeioTransporte());
    		newList.add(result);
    	}
    	return rsp;
    }
    
    public Integer getRowCountValorTarifaPostoPassagem(Map parameters){
    	return getRowCountItemList(parameters, ValorTarifaPostoPassagem.class.getName());
    }
    
    public Object findByIdValorTarifaPostoPassagem(MasterDetailKey key) {
    	return findItemById(key, ValorTarifaPostoPassagem.class.getName());
    }
    
    /***
     * Remove uma lista de registros items.
     *  
     * @param ids ids dos registros item a serem removidos.
     * 
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsValorTarifaPostoPassagem(List ids) {
    	super.removeItemByIds(ids, ValorTarifaPostoPassagem.class.getName());
    }

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(TarifaPostoPassagem.class);

		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
    	Comparator descComparator = new Comparator() {
    		
			public int compare(Object o1, Object o2) {
	    		Collator collator = Collator.getInstance(LocaleContextHolder.getLocale());
	    		
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					ValorTarifaPostoPassagem v1 = (ValorTarifaPostoPassagem) o1;
					ValorTarifaPostoPassagem v2 = (ValorTarifaPostoPassagem) o2;
					int order = v1.getQtEixos().compareTo(v2.getQtEixos());
					if (order == 0) {
						return collator.compare(v1.getTipoMeioTransporte().getDsTipoMeioTransporte(),
								v2.getTipoMeioTransporte().getDsTipoMeioTransporte());
					}
					return order;
				}
			}
    	};
    	
   	ItemListConfig itemInit = new ItemListConfig() {

			public List initialize(Long masterId) {
				if (masterId != null)
					return getService().findValorTarifaPostoPassagem(masterId);
				return new ArrayList();
			}

			public Integer getRowCount(Long masterId) {
				return getService().getRowCountValorTarifaPostoPassagem(masterId);
			}

			public void modifyItemValues(Object newValues, Object bean) {
				ValorTarifaPostoPassagem bean2 = ((ValorTarifaPostoPassagem)bean);
				ValorTarifaPostoPassagem newValues2 = ((ValorTarifaPostoPassagem)newValues);
				bean2.setMoedaPais(newValues2.getMoedaPais());
				bean2.setTipoMeioTransporte(newValues2.getTipoMeioTransporte());
				bean2.setVlTarifa(newValues2.getVlTarifa());
				bean2.setQtEixos(newValues2.getQtEixos());
			}
    		
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parametersTemp, Object beanTemp) {
				TypedFlatMap parameters = (TypedFlatMap)parametersTemp;
				ValorTarifaPostoPassagem bean = (ValorTarifaPostoPassagem)beanTemp;
				Moeda moeda = new Moeda();
				moeda.setDsSimbolo(parameters.getString("moedaPais.moeda.dsSimbolo"));
				moeda.setSgMoeda(parameters.getString("moedaPais.moeda.sgMoeda"));
				MoedaPais moedaPais = new MoedaPais();
				moedaPais.setMoeda(moeda); 
				moedaPais.setIdMoedaPais(parameters.getLong("moedaPais.idMoedaPais"));
				TipoMeioTransporte tipoMeioTransporte = new TipoMeioTransporte();
				tipoMeioTransporte.setIdTipoMeioTransporte(parameters.getLong("tipoMeioTransporte.idTipoMeioTransporte"));
				tipoMeioTransporte.setDsTipoMeioTransporte(parameters.getString("tipoMeioTransporte.dsTipoMeioTransporte"));
				if (StringUtils.isNotBlank(parameters.getString("tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte"))) {
					TipoMeioTransporte composto = new TipoMeioTransporte();
					composto.setDsTipoMeioTransporte(parameters.getString("tipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte"));
					tipoMeioTransporte.setTipoMeioTransporte(composto);
				}
				
				bean.setQtEixos(parameters.getInteger("qtEixos"));
				
				bean.setMoedaPais(moedaPais);
				bean.setTipoMeioTransporte(tipoMeioTransporte);
				bean.setVlTarifa(parameters.getBigDecimal("vlTarifa"));
				bean.setIdValorTarifaPostoPassagem(parameters.getLong("idValorTarifaPostoPassagem"));
				return bean;
			}
    	};
		config.addItemConfig(ValorTarifaPostoPassagem.class.getName(), ValorTarifaPostoPassagem.class, itemInit, descComparator);
		return config;
	}

	public List findComboMoedaPais(TypedFlatMap criteria) {
		return moedaPaisService.findByPais(criteria.getLong("moedaPais.pais.idPais"),Boolean.TRUE);
	}
	public void setConfiguracoesFacade(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}


	public void setTipoMeioTransporteService(
			TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}

	public void setEixosTipoMeioTransporteService(
			EixosTipoMeioTransporteService eixosTipoMeioTransporteService) {
		this.eixosTipoMeioTransporteService = eixosTipoMeioTransporteService;
	}
}

package com.mercurio.lms.franqueado.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.YearMonthDay;

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
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.model.Franqueado;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.service.FranqueadoFranquiaService;
import com.mercurio.lms.franqueados.model.service.FranqueadoService;
import com.mercurio.lms.franqueados.model.service.FranquiaService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;


public class ManterFranqueadoAction extends MasterDetailAction {

	private static final String FRANQUEADO_FRANQUIA_LIST = "franquiasList";
	private FranqueadoService franqueadoService;
	private FranqueadoFranquiaService franqueadoFranquiaService;
	private EmpresaService empresaService;
	private PessoaService pessoaService;
	private FranquiaService franquiaService;
	
	
	private MasterEntryConfig config;
	
	@SuppressWarnings("rawtypes")
	public TypedFlatMap store(TypedFlatMap map) {
		Long idFranqueado = map.getLong("idFranqueado");
		
		MasterEntry entry = getMasterFromSession(idFranqueado, false);

		Franqueado franqueado = (Franqueado) entry.getMaster();
		if(idFranqueado ==null){
			franqueado = mapToFranqueado(map);
		} else {
			franqueado = mapToFranqueado(map, franqueado);
		}

		ItemList franqueadoFranquiaList = entry.getItems(FRANQUEADO_FRANQUIA_LIST);
		
		if(!franqueadoFranquiaList.isInitialized()){
			List persistentBag = idFranqueado != null?franqueadoFranquiaService.findByFranqueado(idFranqueado):Collections.EMPTY_LIST;			
			franqueadoFranquiaList.initialize(persistentBag);
		}

		List newers = franqueadoFranquiaList.getNewOrModifiedItems();
		List removed = franqueadoFranquiaList.getRemovedItems();

		if(franqueadoFranquiaList.getItems() == null || franqueadoFranquiaList.getItems().isEmpty()){
			throw new BusinessException("LMS-46111");
		}
		
		List<Franquia> franquiasNovas = getFranquiasNovas(newers);
		List<Franquia> franquiasRemovidas = getFranquiasRemovidas(removed,newers);

		Franqueado insertedFranqueado = (Franqueado) getService().store(franqueado,franquiasNovas,franquiasRemovidas,newers,removed);

		franqueadoFranquiaList.resetItemsState(); 
    	updateMasterInSession(entry);
		
    	TypedFlatMap result = franqueadoToMap(insertedFranqueado);
    	
		return result;
	}

	@SuppressWarnings("rawtypes")
	private List<Franquia> getFranquiasNovas(List newers) {
		List<Franquia> franquiasNovas = new ArrayList<Franquia>();
		List franquias = franquiaService.find(new HashMap());
		
		for (Object object : newers) {
			FranqueadoFranquia tmp = (FranqueadoFranquia) object;
			TypedFlatMap franqueadoFrqMap = franqueadoFranquiaToMap(tmp);

			if(isNovaFranquia(franqueadoFrqMap, franquias)){
				franquiasNovas.add(tmp.getFranquia());
			}
		}
		return franquiasNovas;
	}

	@SuppressWarnings("rawtypes")
	private List<Franquia> getFranquiasRemovidas(List removed, List newers) {
		List<Franquia> franquiasRemovidas = new ArrayList<Franquia>();
		
		if(removed != null && !removed.isEmpty()){
			boolean hasNotOnDB = false;
			for (Object object : removed) {
				FranqueadoFranquia tmp = (FranqueadoFranquia) object;
				
				List franqueadoFrqs = franqueadoFranquiaService.find(new HashMap());
				
				if((newers == null || newers.isEmpty()) && hasNotFranqueadosFranquia(tmp, franqueadoFrqs)){
					franquiasRemovidas.add(tmp.getFranquia());
				} else if(hasNotFranqueadosFranquia(tmp, franqueadoFrqs)){
					hasNotOnDB = true;
				}
			}
			
			if(newers != null){
				for (Object object : newers) {
					FranqueadoFranquia tmp = (FranqueadoFranquia) object;
					
					List franqueadoFrqs = franqueadoFranquiaService.find(new HashMap());
					
					if(hasNotFranqueadosFranquia(tmp, franqueadoFrqs) && hasNotOnDB){
						franquiasRemovidas.add(tmp.getFranquia());
					}
		
				}
			}
		}
		return franquiasRemovidas;
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedFranqueados(Map criteria){

		FilterResultSetPage filterRs = new FilterResultSetPage(((FranqueadoService)getMasterService()).findPaginatedFranqueados(criteria)) {
				public Map filterItem(Object item) {
					Object[] obj = (Object[]) item;

					TypedFlatMap typedFlatMap = new TypedFlatMap();
					typedFlatMap.put("idFranqueado", obj[0]);
					typedFlatMap.put("nrIdentificacao", FormatUtils.formatCNPJ((String)obj[1]));
					typedFlatMap.put("nmPessoa", obj[2]);
					typedFlatMap.put("blOptanteSimples", BooleanUtils.toString((Boolean) obj[3], "Sim", "Não", null));

					return typedFlatMap;
				}
			};
			
		return (ResultSetPage) filterRs.doFilter();
	}
	
	@SuppressWarnings("rawtypes")
	public int getRowCountFranqueados(Map criteria){
		Long rowCountFranqueados = ((FranqueadoService)getMasterService()).getRowCountFranqueados(criteria);
		return rowCountFranqueados != null?Integer.valueOf(rowCountFranqueados.intValue()):Integer.valueOf(0);
	}

	public Serializable findByIdFranqueados(Long id){
		if(id!=null){
			Franqueado franqueado = (Franqueado) getService().findById(id);
		
			putMasterInSession(franqueado);
		
			return franqueadoToMap(franqueado);
		}
		return new TypedFlatMap();

	}
	
	@SuppressWarnings("rawtypes")
	public void removeByid(Long id) {
		List removed = franqueadoFranquiaService.findByFranqueado(id);
		
		List<Franquia> franquiasRemovidas =getFranquiasRemovidas(removed,null);
		
		getService().removeById(id,franquiasRemovidas);
	}
	
	@SuppressWarnings({ "rawtypes" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Object id : ids) {
			removeByid((Long) id);
		}
	}
	
	public Object findByIdFranqueadoFranquia(MasterDetailKey key) { 
		return (FranqueadoFranquia)findItemById(key, FRANQUEADO_FRANQUIA_LIST);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedFranquias(Map parameters){
		ResultSetPage rs  = (ResultSetPage) findPaginatedItemList(parameters, FRANQUEADO_FRANQUIA_LIST);
		
		FilterResultSetPage filter = new FilterResultSetPage(rs) {
			public Map filterItem(Object item) {		 	
				return franqueadoFranquiaToMap(item);
		 	}
		 };
		
		return (ResultSetPage) filter.doFilter();
	}
	
	@SuppressWarnings("rawtypes")
	public Serializable insertFranqueadoFranquia(TypedFlatMap parameters){
		Long masterId = parameters.getLong("masterId");
		YearMonthDay today = new YearMonthDay();
		
		MasterEntry master = getMasterFromSession(masterId, false);
		
		ItemList items = getItemsFromSession(master, FRANQUEADO_FRANQUIA_LIST);
		
		List itemList = items.getItems();
		List franqueadoFrqVigentes = franqueadoFranquiaService.findFranqueadoFranquiasVigentes(today);
		
		isValidFranqueadoFranquia(parameters, franqueadoFrqVigentes);
		isValidFranqueadoFranquia(parameters, itemList);
		
		YearMonthDay dtInicial = parameters.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtFinal = parameters.getYearMonthDay("dtVigenciaFinal");
		Long idFilial = parameters.getLong("idFilial");
		String sgFilial = parameters.getString("sgFilial");
		Long id = parameters.getLong("idFranqueadoFranquia");
		
		for (Object object : itemList) {
			FranqueadoFranquia franqueadoFranquia = (FranqueadoFranquia) object;
			
			if(franqueadoFranquia.getIdFranqueadoFranquia().equals(id)){
				Franquia franquia = franqueadoFranquia.getFranquia();
				Filial filial = franquia.getFilial();
				filial.setIdFilial(idFilial);
				filial.setSgFilial(sgFilial);
				
				franquia.setFilial(filial);
				
				franqueadoFranquia.setFranquia(franquia);

				franqueadoFranquia.setDtVigenciaInicial(dtInicial);
				franqueadoFranquia.setDtVigenciaFinal(dtFinal);
				
				items.addItem(franqueadoFranquia,config.getItemListConfig(FRANQUEADO_FRANQUIA_LIST));
				
				return franqueadoFranquia;
			}
		}
		
		return saveItemInstance(parameters, FRANQUEADO_FRANQUIA_LIST);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeFranqueadosFranquia(List ids){
		List removable = new ArrayList<Long>();
		
		for (Object idO : ids) {
			Long id = (Long)idO;
			if(id!=null && id.intValue() >0){
				FranqueadoFranquia franqueadoFranquia = (FranqueadoFranquia) franqueadoFranquiaService.findById(id);
				if(franqueadoFranquia != null){
					YearMonthDay today = new YearMonthDay();
					
					if(franqueadoFranquia.getDtVigenciaInicial().isBefore(today) || franqueadoFranquia.getDtVigenciaInicial().isEqual(today)){
						throw new BusinessException("LMS-46115",new Object[]{franqueadoFranquia.getFranquia().getFilial().getSgFilial(),franqueadoFranquia.getDtVigenciaInicial(),franqueadoFranquia.getDtVigenciaFinal()});
					}
					removable.add(id);
				}
			} else {
				removable.add(id);
			}
		}
		
		removeItemByIds(removable, FRANQUEADO_FRANQUIA_LIST);
	}
	
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findEmpresas(TypedFlatMap criteria) {
		criteria.put("pessoa.nrIdentificacao", PessoaUtils.clearIdentificacao((String) criteria.get("nrIdentificacao")));

		List result = null;
		
		try{
			result = empresaService.findLookupTypedFlatMap(criteria);
		}catch(Exception e){
			throw new BusinessException("LMS-23017");
		}
		
		List listReturn = new ArrayList();		
		
		if(result != null){
			for (Iterator iter = result.iterator(); iter.hasNext();) {
				Empresa empresa = (Empresa) iter.next();
	
				Map mapReturn = new HashMap();			
				mapReturn.put("idEmpresa", empresa.getIdEmpresa());
				mapReturn.put("nmPessoa", empresa.getPessoa().getNmPessoa());
				mapReturn.put("nrIdentificacao", empresa.getPessoa().getNrIdentificacao());
							
				listReturn.add(mapReturn);
			}
		}
		return listReturn;		
	}

	
	public FranqueadoService getService(){
		return franqueadoService;
	}
	
	public void setFranqueadoService(FranqueadoService franqueadoService) {
		this.franqueadoService = franqueadoService;
		this.setMasterService(franqueadoService);
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public FranqueadoFranquiaService getFranqueadoFranquiaService() {
		return franqueadoFranquiaService;
	}


	public void setFranqueadoFranquiaService(FranqueadoFranquiaService franqueadoFranquiaService) {
		this.franqueadoFranquiaService = franqueadoFranquiaService;
	}

	public FranquiaService getFranquiaService() {
		return franquiaService;
	}


	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}


	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MasterEntryConfig createMasterConfig(
			MasterDetailFactory masterFactory) {
		config = masterFactory.createMasterEntryConfig(Franqueado.class,true);
		
		ItemListConfig franquias = new ItemListConfig() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List initialize(Long masterId) {
				return franqueadoFranquiaService.findByFranqueado(masterId);			
			}

			@Override
			public Integer getRowCount(Long masterId) {
				return franqueadoFranquiaService.findByFranqueado(masterId).size();
			}
			
			
			@Override
			public List initialize(Long masterId, Map parameters) {
				return franqueadoFranquiaService.findByFranqueado(masterId);			
			}

			@Override
			public Integer getRowCount(Long masterId, Map parameters) {
				return franqueadoFranquiaService.findByFranqueado(masterId).size();
			}
			
			@Override
			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap map = new TypedFlatMap(parameters);
				FranqueadoFranquia franqueadoFranquia = (FranqueadoFranquia) bean;
				
				franqueadoFranquia.setIdFranqueadoFranquia(map.getLong("idFranqueadoFranquia"));
				franqueadoFranquia.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
				franqueadoFranquia.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
				
				Filial filial = new Filial();
				filial.setIdFilial(map.getLong("idFilial"));
				filial.setSgFilial(map.getString("sgFilial"));
				
				Franquia franquia = new Franquia();
				franquia.setIdFranquia(map.getLong("idFilial"));
				franquia.setFilial(filial);
				franqueadoFranquia.setFranquia(franquia);
				
				Franqueado franqueado = new Franqueado();
				franqueado.setIdFranqueado(map.getLong("idFranqueado"));
				franqueadoFranquia.setFranqueado(franqueado);

				return franqueadoFranquia;
			}
		};
		
		
		@SuppressWarnings("unchecked")
		Comparator<FranqueadoFranquia> franquedoFranquiaComparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				FranqueadoFranquia franquia1 = (FranqueadoFranquia)obj1;
				FranqueadoFranquia franquia2 = (FranqueadoFranquia)obj2;

        		return compareTo(franquia1,franquia2);  		
			}
			
			public int compareTo(FranqueadoFranquia franquia, FranqueadoFranquia other) {
				int i = 0;
				if(franquia.getFranquia() != null && other.getFranquia() != null){
					if(franquia.getFranquia().getFilial() != null && other.getFranquia().getFilial() != null){
						i = franquia.getFranquia().getFilial().getIdFilial().compareTo(other.getFranquia().getFilial().getIdFilial());
						if (i != 0) return i;
					}
				}
				
				i = franquia.getDtVigenciaFinal().compareTo(other.getDtVigenciaFinal());
				if (i != 0) return i;
				
				return franquia.getDtVigenciaInicial().compareTo(other.getDtVigenciaInicial());
			}
    	};

		config.addItemConfig(FRANQUEADO_FRANQUIA_LIST, FranqueadoFranquia.class, franquias, franquedoFranquiaComparator);
		return config;
	
	}
	
	
	private TypedFlatMap franqueadoFranquiaToMap(Object item) {
		FranqueadoFranquia franquia = (FranqueadoFranquia) item;
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idFranqueadoFranquia", franquia.getIdFranqueadoFranquia());
		tfm.put("dtVigenciaInicial",franquia.getDtVigenciaInicial());
		tfm.put("dtVigenciaFinal",franquia.getDtVigenciaFinal());
		
		if(franquia.getFranquia() != null && franquia.getFranquia().getFilial() != null){
			tfm.put("sgFilial",franquia.getFranquia().getFilial().getSgFilial());
		}
		
		if(franquia.getFranquia() != null && franquia.getFranquia().getIdFranquia() != null){
			tfm.put("idFilial",franquia.getFranquia().getFilial().getIdFilial());
		}

		return tfm;
	}
	
	private TypedFlatMap franqueadoToMap(Franqueado franqueado) {
		if(franqueado != null){
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idFranqueado", franqueado.getIdFranqueado());
			if(franqueado.getPessoa() != null){
				typedFlatMap.put("nrIdentificacao", FormatUtils.formatCNPJ(franqueado.getPessoa().getNrIdentificacao()));
				typedFlatMap.put("nmPessoa", franqueado.getPessoa().getNmPessoa());
				typedFlatMap.put("idEmpresa", franqueado.getPessoa().getIdPessoa());
			}
			typedFlatMap.put("blOptanteSimples", BooleanUtils.toString(franqueado.getBlOptanteSimples(), "S", "N", null));
			
			return typedFlatMap;
		}
		return new TypedFlatMap();
	}

	
	private Franqueado mapToFranqueado(TypedFlatMap map) {
		return mapToFranqueado(map,null);
	}
	
	private Franqueado mapToFranqueado(TypedFlatMap map, Franqueado franqueado) {
		Franqueado result = franqueado != null?franqueado:new Franqueado();
		
		if(map != null && !map.isEmpty()){
			if(map.containsKey("idFranqueado")){
				result.setIdFranqueado(map.getLong("idFranqueado"));
			}
			
			if(map.containsKey("idEmpresa")){
				Pessoa pessoa = pessoaService.findById(map.getLong("idEmpresa"));
				
				result.setPessoa(pessoa);
			}
			
			if(map.containsKey("blOptanteSimples")){
				result.setBlOptanteSimples(map.getString("blOptanteSimples").equals("S")?true:false);
			}
		}
		
		return result;
	}

	

	@SuppressWarnings({"rawtypes" })
	private Boolean isNovaFranquia(TypedFlatMap franqueadoFrqMap,List franquias) {	
		boolean isNovaFranquia = true;
		
		for (Object object : franquias) {
			Franquia  franquia = (Franquia)object;
			if(franquia.getIdFranquia().equals(franqueadoFrqMap.getLong("idFilial"))){
				isNovaFranquia = false;
			}
		}
		
		return isNovaFranquia;
	}
	


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void isValidFranqueadoFranquia(TypedFlatMap parameters, List list) {
		List updateIds = new ArrayList<Long>();
		
		YearMonthDay dtInicial = parameters.getYearMonthDay("dtVigenciaInicial");
		YearMonthDay dtFinal = parameters.getYearMonthDay("dtVigenciaFinal");
		Long idFilial = parameters.getLong("idFilial");
		Long id = parameters.getLong("idFranqueadoFranquia");

		if(dtFinal.isBefore(dtInicial)){
			throw new BusinessException("LMS-00083");
		}

		FranqueadoFranquia franqueadoFranquia = null;
		for (Object object : list) {
			franqueadoFranquia = (FranqueadoFranquia) object;
			
			if(franqueadoFranquia.getIdFranqueadoFranquia().equals(id)){
				updateIds.add(id);
			}
			
			if(FranqueadoUtils.isVigenciaEntreVigenciasAntigas(dtInicial, dtFinal, franqueadoFranquia.getDtVigenciaInicial(), franqueadoFranquia.getDtVigenciaFinal()) &&
					franqueadoFranquia.getFranquia().getFilial().getIdFilial().equals(idFilial) &&
					!franqueadoFranquia.getIdFranqueadoFranquia().equals(id)
				){
				throw new BusinessException("LMS-46114");
			}
			
			
		}

		if(!updateIds.contains(id) && dtInicial.isBefore(new YearMonthDay())){
			throw new BusinessException("LMS-00006");
		}
	} 
	
	@SuppressWarnings("rawtypes")
	private boolean hasNotFranqueadosFranquia(FranqueadoFranquia franqueadoFrq, List franqueadoFrqs) {
		return !hasFranqueadosFranquia(franqueadoFrq, franqueadoFrqs);
	}
	
	@SuppressWarnings("rawtypes")
	private boolean hasFranqueadosFranquia(FranqueadoFranquia franqueadoFrq, List franqueadoFrqs) {
		boolean hasFranqueados = false;
		for (Object object : franqueadoFrqs) {
			FranqueadoFranquia tmp = (FranqueadoFranquia) object;
			if(tmp.getFranquia().getIdFranquia().equals(franqueadoFrq.getFranquia().getIdFranquia()) && 
					!tmp.getIdFranqueadoFranquia().equals(franqueadoFrq.getIdFranqueadoFranquia())){
				hasFranqueados= true;
			}
		}
		
		return hasFranqueados;
	}
	


}

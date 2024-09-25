package com.mercurio.lms.integracao.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.integracao.model.MunicipioVinculo;
import com.mercurio.lms.integracao.model.service.MunicipioCorporativoService;
import com.mercurio.lms.integracao.model.service.MunicipioVinculoService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * 
 * @spring.bean id="lms.integracao.manterMunicipiosVinculoAction"
 */
public class ManterMunicipiosVinculoAction extends CrudAction {
	private MunicipioService municipioService;
	private UnidadeFederativaService unidadeFederativaService;
	
	private MunicipioCorporativoService municipioCorporativoService;
	
    public void removeById(java.lang.Long id) {
    	getService().removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getService().removeByIds(ids);
   }

    public MunicipioVinculo findById(java.lang.Long id) {
    	return getService().findById(id);
    }

    
	public Serializable store(MunicipioVinculo bean) {
		return getService().store(bean);
	}
	
	/**
	 * Consulta da grid principal
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		
		ResultSetPage rsPage = this.getService().findPaginated(criteria);
		FilterResultSetPage frsp = new FilterResultSetPage(rsPage) {

			public Map filterItem(Object item) {
				Map mapItem = (Map) item;
				TypedFlatMap filterItem = new TypedFlatMap();

				filterItem.put("idMunicipioVinculo", mapItem.get("idMunicipioVinculo"));
				
				filterItem.put("municipioLms.idMunicipio", mapItem.get("idMunicipioLms"));
				filterItem.put("municipioLms.nmMunicipio", mapItem.get("nmMunicipioLms"));
				filterItem.put("municipioLms.nrCep", mapItem.get("nrCepLms"));
				
				filterItem.put("municipioCorporativo.idMunicipio", mapItem.get("idMunicipioCorp"));
				filterItem.put("municipioCorporativo.nmMunicipio", mapItem.get("nmMunicipioCorp"));
				filterItem.put("municipioCorporativo.nrCep", mapItem.get("nrCepCorp"));
				
				return filterItem;
			}
		};
		return (ResultSetPage) frsp.doFilter();
	
	}

	/**
	 * RowCount da grid principal
	 */
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return this.getService().getRowCount(criteria);
	}
	
	///// COMBOS E LOOKUPS /////
	
	public List findLookupMunicipio(TypedFlatMap criteria){
		//da NullPointerException na localização se nao existir essa chave
		if(criteria.getString("nmMunicipio") == null){
			criteria.put("nmMunicipio", "");
		}
		List lista = getMunicipioService().findLookupMunicipio(criteria);
	
		if(lista.size() == 1){
			
		    HashMap munOld = (HashMap) lista.get(0);
			HashMap munNew = new HashMap();
			
			munNew.put("idMunicipio", munOld.get("idMunicipio"));
			munNew.put("nmMunicipio", munOld.get("nmMunicipio"));
			munNew.put("nrCep", munOld.get("nrCep"));
			munNew.put("cdIbge", getMunicipioService().findById((Long)munOld.get("idMunicipio")).getCdIbge());
			
			//uf
			HashMap unidadeFederativa = new HashMap();
			unidadeFederativa.put("idUnidadeFederativa", munOld.get("unidadeFederativa_idUnidadeFederativa"));
			unidadeFederativa.put("sgUnidadeFederativa", munOld.get("unidadeFederativa_sgUnidadeFederativa"));
			//pais
			HashMap pais = new HashMap();
			pais.put("idPais", munOld.get("unidadeFederativa_pais_idPais"));
			pais.put("nmPais", munOld.get("unidadeFederativa_pais_nmPais"));
			
			unidadeFederativa.put("pais", pais);
		
			munNew.put("unidadeFederativa", unidadeFederativa);
			
			lista.clear();
			lista.add(munNew);
		}
		return lista;
	}
	
	public List findLookupMunicipioCorporativo(TypedFlatMap criteria){
		List lista = getMunicipioCorporativoService().findLookupMunicipio(criteria);
		return lista;
	}
	
	public Map findMunicipio(TypedFlatMap param){
		Long idMunicipio = param.getLong("idMunicipio");
		Municipio municipio = getMunicipioService().findById(idMunicipio);
		
		Map retorno = new TypedFlatMap();
		retorno.put("cdIbge", municipio.getCdIbge());
		
		return retorno;
	}
	
	public List findLookupUnidadeFederativa(Map criteria){
		Map crit = new HashMap();
		crit.put("sgUnidadeFederativa", criteria.get("sgUnidadeFederativa"));
		return getUnidadeFederativaService().findLookup(crit);
	}
	
	///// GETTERS E SETTERS /////
	
	public void setService(MunicipioVinculoService municipioVinculoService) {
		this.defaultService = municipioVinculoService;
	}
	
	private MunicipioVinculoService getService() {
		return ((MunicipioVinculoService)defaultService);
	}
	
	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public MunicipioCorporativoService getMunicipioCorporativoService() {
		return municipioCorporativoService;
	}

	public void setMunicipioCorporativoService(
			MunicipioCorporativoService municipioCorporativoService) {
		this.municipioCorporativoService = municipioCorporativoService;
	}
}

package com.mercurio.lms.portaria.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.portaria.model.MeioTransporteRodoBox;
import com.mercurio.lms.portaria.model.service.MeioTransporteRodoBoxService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.portaria.manterMeiosTransporteBoxAction"
 */

public class ManterMeiosTransporteBoxAction extends CrudAction {
	
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	
	
	public void setService(MeioTransporteRodoBoxService meioTransporteRodoBoxService) {
		this.defaultService = meioTransporteRodoBoxService;
	}
	
	public List findMeioTransporteRodoviario(Map criteria){
		return getMeioTransporteRodoviarioService().findLookup(criteria);
	}
	
	public Serializable store(MeioTransporteRodoBox bean) {
		return ((MeioTransporteRodoBoxService) this.defaultService).store(bean); 
	}
	
	public Map storeMap(TypedFlatMap map) {
		return ((MeioTransporteRodoBoxService) this.defaultService).storeMap(map); 
	}
	
	public Map findById(Long id) {
		return ((MeioTransporteRodoBoxService) this.defaultService).findByIdDetalhamento(id); 
	}

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = this.defaultService.findPaginated(criteria);
		
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				MeioTransporteRodoBox mtr = (MeioTransporteRodoBox) item;
				TypedFlatMap row = new TypedFlatMap();
				
				row.put("idMeioTransporteRodoBox", mtr.getIdMeioTransporteRodoBox());	
				
				MeioTransporte meioTransporte = mtr.getMeioTransporteRodoviario().getMeioTransporte();
				row.put("meioTransporteRodoviario.meioTransporte.nrFrota", meioTransporte.getNrFrota()); 
				row.put("meioTransporteRodoviario.meioTransporte.nrIdentificador", meioTransporte.getNrIdentificador());
				
				row.put("dtVigenciaInicial", mtr.getDtVigenciaInicial());
				row.put("dtVigenciaFinal", mtr.getDtVigenciaFinal());
								
				return row;
			}
			
		};
		
		return (ResultSetPage)frsp.doFilter();
	}
	
	public void removeById(Long id) {
		((MeioTransporteRodoBoxService) this.defaultService).removeById(id); 
	}
	
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((MeioTransporteRodoBoxService) this.defaultService).removeByIds(ids); 
	}

	/**
	 * @return Returns the meioTransporteRodoviarioService.
	 */
	public MeioTransporteRodoviarioService getMeioTransporteRodoviarioService() {
		return meioTransporteRodoviarioService;
	}

	/**
	 * @param meioTransporteRodoviarioService The meioTransporteRodoviarioService to set.
	 */
	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	


}

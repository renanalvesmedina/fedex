package com.mercurio.lms.municipios.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.municipios.model.HorarioTransito;
import com.mercurio.lms.municipios.model.service.HorarioTransitoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.swt.manterHorariosRestricaoAcessoAction"
 */

public class ManterHorariosRestricaoAcessoAction extends CrudAction { 
	
	public void setService(HorarioTransitoService horarioTransitoService) {
		this.defaultService = horarioTransitoService; 
	}
	public HorarioTransitoService getHorarioTransitoService() {
		return (HorarioTransitoService) this.defaultService; 
	}

	
	public Map store(HorarioTransito bean) {
		Long idHorarioTransito = (Long)getHorarioTransitoService().store(bean);
		Map mapRetorno = new HashMap();
		mapRetorno.put("idHorarioTransito", idHorarioTransito);
		return mapRetorno;
	}
	
	public Map findById(Long id) {
		HorarioTransito hts = getHorarioTransitoService().findById(id);
		Map map = new HashMap();
		map.put("idHorarioTransito", hts.getIdHorarioTransito());
		map.put("hrTransitoInicial", hts.getHrTransitoInicial());
		map.put("hrTransitoFinal", hts.getHrTransitoFinal());
		return map;
	}
	
	public void removeById(Long id) {
		((HorarioTransitoService) this.defaultService).removeById(id); 
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
		((HorarioTransitoService) this.defaultService).removeByIds(ids); 
	}

}

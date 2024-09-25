package com.mercurio.lms.integracao.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsmmanager.integracao.model.GrupoLayoutBinder;
import com.mercurio.adsmmanager.integracao.model.PontoLayoutBinder;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.integracao.model.VersaoPontoLayoutBinder;
import com.mercurio.lms.integracao.model.service.PontoLayoutBinderService;
import com.mercurio.lms.integracao.model.service.VersaoPontoLayoutBinderService;

/**
 * @spring.bean id="lms.integracao.manterVersaoPontoLayoutBinderAction" 
 */
public class ManterVersaoPontoLayoutBinderAction extends CrudAction {	
	
	private ConteudoParametroFilialService conteudoParametroFilialService; 
	private ParametroFilialService parametroFilialService;
	private PontoLayoutBinderService pontoLayoutBinderService;
	
	
	@SuppressWarnings("unchecked")
	public HashMap findById(Long id) {
		return getService().findById((Serializable)id);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupPontoLayoutBinder(Map criteria) {
		return getService().findLookupPontoLayoutBinder(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupConteudoParametroFilial(Map criteria) {
		return getService().findLookupConteudoParametroFilial(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public List findLookupParametroFilial(Map criteria) {
		return getService().findLookupParametroFilial(criteria);
	}
	
	@SuppressWarnings("unchecked")
	public Serializable store(TypedFlatMap m) {		
		PontoLayoutBinder pontoLayoutBinder = null;
		GrupoLayoutBinder grupoLayoutBinder = null;
		if(m.getLong("pontoLayoutBinder.id") != null){
			pontoLayoutBinder = (PontoLayoutBinder) pontoLayoutBinderService.findById(m.getLong("pontoLayoutBinder.id"));
		}else{
			grupoLayoutBinder = new GrupoLayoutBinder();
			grupoLayoutBinder.setId(m.getLong("grupoLayoutBinder.id"));
		}
		
		ParametroFilial parametroFilialIni = parametroFilialService.findById(m.getLong("parametroFilialInicio.idParametroFilial"));
		
		ParametroFilial parametroFilialFim = parametroFilialService.findById(m.getLong("parametroFilialFim.idParametroFilial"));
		
		
		
		
		VersaoPontoLayoutBinder versaoPontoLayoutBinder = new VersaoPontoLayoutBinder();		
		versaoPontoLayoutBinder.setIdVersaoPontoLayoutBinder(m.getLong("idVersaoPontoLayoutBinder"));
		if(pontoLayoutBinder != null){
			if(pontoLayoutBinder.getGrupoLayoutBinder() == null){
				versaoPontoLayoutBinder.setPontoLayoutBinder(pontoLayoutBinder);
			}else{
				versaoPontoLayoutBinder.setGrupoLayoutBinder(pontoLayoutBinder.getGrupoLayoutBinder());
			}
		}else{
			versaoPontoLayoutBinder.setGrupoLayoutBinder(grupoLayoutBinder);
		}
		versaoPontoLayoutBinder.setParametroFilialInicio(parametroFilialIni);
		versaoPontoLayoutBinder.setParametroFilialFim(parametroFilialFim);
				
		return getService().store(versaoPontoLayoutBinder);
	}
	
	@Override
	@SuppressWarnings("unchecked")	
	public ResultSetPage findPaginated(Map criteria) {	
		Long id = MapUtilsPlus.getLongOnMap(criteria,"pontoLayoutBinder","id");
		if(id != null){
			PontoLayoutBinder pontoLayoutBinder = (PontoLayoutBinder) pontoLayoutBinderService.findById(id);
			if(pontoLayoutBinder.getGrupoLayoutBinder() != null){
				Map map = MapUtilsPlus.getMap(criteria, "pontoLayoutBinder", null);
				map.put("id", "");
				criteria.put("grupoLayoutBinder", new HashMap());
				map = MapUtilsPlus.getMap(criteria, "grupoLayoutBinder", null);
				map.put("id", pontoLayoutBinder.getGrupoLayoutBinder().getId());
				
			}
		}
		return getService().findPaginated(criteria);
	}	
	
	@Override
	@SuppressWarnings("unchecked")	
	public Integer getRowCount(Map criteria) {	
		return getService().getRowCount(criteria);
	}
	
	@SuppressWarnings("unchecked")
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getService().removeByIds(ids);
    }
	
	public void setService(VersaoPontoLayoutBinderService versaoPontoLayoutBinderService) {
		this.defaultService = versaoPontoLayoutBinderService;
	}
	
	private VersaoPontoLayoutBinderService getService() {
		return ((VersaoPontoLayoutBinderService)defaultService);
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public ParametroFilialService getParametroFilial() {
		return parametroFilialService;
	}

	public void setParametroFilialService(ParametroFilialService parametroFilial) {
		this.parametroFilialService = parametroFilial;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public PontoLayoutBinderService getPontoLayoutBinderService() {
		return pontoLayoutBinderService;
	}

	public void setPontoLayoutBinderService(
			PontoLayoutBinderService pontoLayoutBinderService) {
		this.pontoLayoutBinderService = pontoLayoutBinderService;
	}

	public ParametroFilialService getParametroFilialService() {
		return parametroFilialService;
	}
}
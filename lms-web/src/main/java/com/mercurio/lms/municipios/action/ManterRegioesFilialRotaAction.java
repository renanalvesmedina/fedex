package com.mercurio.lms.municipios.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.RegiaoColetaEntregaFilService;
import com.mercurio.lms.municipios.model.service.RegiaoFilialRotaColEntService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterRegioesFilialRotaAction"
 */

public class ManterRegioesFilialRotaAction extends CrudAction {
			
	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private RotaColetaEntregaService rotaColetaEntregaService;
		
	public void setService(RegiaoFilialRotaColEntService regiaoFilialRotaColEntService) {
		this.defaultService = regiaoFilialRotaColEntService; 
	}
	
	public Serializable store(RegiaoFilialRotaColEnt bean) {
		return ((RegiaoFilialRotaColEntService) this.defaultService).store(bean); 
	}
	
	public Map storeMap(Map map) {
		return ((RegiaoFilialRotaColEntService) this.defaultService).storeMap(map); 
	}
	
		
	public Map findById(Long id) {
		return ((RegiaoFilialRotaColEntService) this.defaultService).findByIdDetalhamento(id); 
	}
	
	public void removeById(Long id) {
		((RegiaoFilialRotaColEntService) this.defaultService).removeById(id); 
	}
	
	public List findComboRegiaoColetaList(TypedFlatMap criteria){	
		return getRegiaoColetaEntregaFilService().findListRegiaoVigente(criteria,false);
	}
	
	public List findRegiaoColetaFilial(TypedFlatMap criteria){	
		return getRegiaoColetaEntregaFilService().findListRegiaoVigente(criteria,true);
	}
	
	public List findRotaLookup(Map criteria){
		return getRotaColetaEntregaService().find(criteria);
	}
	
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = super.findPaginated(criteria);
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				RegiaoFilialRotaColEnt regiaoFilialRotaColEnt = (RegiaoFilialRotaColEnt) item;
				TypedFlatMap row = new TypedFlatMap();
				
				row.put("idRegiaoFilialRotaColEnt", regiaoFilialRotaColEnt.getIdRegiaoFilialRotaColEnt()); 
				
				RotaColetaEntrega rotaColetaEntrega = regiaoFilialRotaColEnt.getRotaColetaEntrega();
				Filial filial = rotaColetaEntrega.getFilial();
				
				row.put("rotaColetaEntrega.filial.sgFilial", filial.getSgFilial());
				row.put("rotaColetaEntrega.filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
				row.put("rotaColetaEntrega.nrRota", rotaColetaEntrega.getNrRota());
				row.put("rotaColetaEntrega.dsRota", rotaColetaEntrega.getDsRota());
				
				row.put("rotaColetaEntrega.numeroDescricaoRota",rotaColetaEntrega.getNumeroDescricaoRota());
				
				row.put("regiaoColetaEntregaFil.dsRegiaoColetaEntregaFil", regiaoFilialRotaColEnt.getRegiaoColetaEntregaFil().getDsRegiaoColetaEntregaFil());
				
				row.put("dtVigenciaFinal", regiaoFilialRotaColEnt.getDtVigenciaFinal());
				row.put("dtVigenciaInicial", regiaoFilialRotaColEnt.getDtVigenciaInicial());
					
								
				return row;
			}
			
		};
		
		return (ResultSetPage)frsp.doFilter();
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
		((RegiaoFilialRotaColEntService) this.defaultService).removeByIds(ids); 
	}

	/**
	 * @return Returns the regiaoColetaEntregaFilService.
	 */
	public RegiaoColetaEntregaFilService getRegiaoColetaEntregaFilService() {
		return regiaoColetaEntregaFilService;
	}

	/**
	 * @param regiaoColetaEntregaFilService The regiaoColetaEntregaFilService to set.
	 */
	public void setRegiaoColetaEntregaFilService(
			RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}

	/**
	 * @return Returns the rotaColetaEntregaService.
	 */
	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}

	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(
			RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	

}

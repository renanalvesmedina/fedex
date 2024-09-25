package com.mercurio.lms.integracao.action;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsmmanager.integracao.model.LayoutBinder;
import com.mercurio.lms.integracao.model.service.PontoLayoutBinderService;

/**
 * @spring.bean id="lms.integracao.manterPontoLayoutBinderAction" 
 */
public class ManterPontoLayoutBinderAction extends CrudAction {	
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage resultSetPage = getService().findPaginated(criteria); 
		List data = resultSetPage.getList();
		for(int i = 0; i < data.size(); i++){
			TypedFlatMap result = (TypedFlatMap)data.get(i);

			String nmLayoutFormated = formatNmLayout(
								result.getString("layoutBinder.idIdentificadorPrimario")
								, result.getString("layoutBinder.idIdentificadorSecundario")
								, result.getString("layoutBinder.idIdentificadorTerciario"));

			String nmGrupoLayoutFormated = formatNmLayout(result.getString("grupoLayoutBinder.layoutPrincipal.idIdentificadorPrimario")
					, result.getString("grupoLayoutBinder.layoutPrincipal.idIdentificadorSecundario")
					, result.getString("grupoLayoutBinder.layoutPrincipal.idIdentificadorTerciario"));

			String nmGrupo = result.getString("grupoLayoutBinder.nome");
			if(isNotBlank(nmGrupo)) nmGrupoLayoutFormated = nmGrupo + "/" + nmGrupoLayoutFormated;

			Map filterResult = new HashMap(4);
			filterResult.put("nmLayoutFormated", nmLayoutFormated);
			filterResult.put("nmGrupoLayoutFormated", nmGrupoLayoutFormated);
			filterResult.put("id", result.getLong("id"));
			filterResult.put("pontoBinder.nome", result.getString("pontoBinder.nome"));
			filterResult.put("layoutOrGroup", result.getString("layoutOrGroup"));
			data.set(i, new TypedFlatMap(filterResult));
		}
		return resultSetPage;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {	
		return getService().getRowCount(criteria);
	}
	
	private String formatNmLayout(String ...strings){
		int count = 0;
		StringBuilder nmLayoutFormated = new StringBuilder();
		for(String id : strings){
			if(id != null) {
				if(count++ > 0) nmLayoutFormated.append(" - ");
				nmLayoutFormated.append(id);
			}
		}
		return nmLayoutFormated.toString();
	}
	
	
	@SuppressWarnings("unchecked")	
	/**
     * Método responsável por popular a combo de Ponto de Integração
     * 
     * @return List <PontoBinder>
     */
	public List findAllPontoIntegracao(){
		return getService().findAllPontoIntegracao();
	}
	
	public List findAllGruposLayout(){
    	return getService().findAllGruposLayout();
    }
	
	public List findAllLayout(){
		List lista = getService().findAllLayout();
		String identificadores = "";
		for(Iterator<LayoutBinder> iter = lista.iterator();iter.hasNext();){
			LayoutBinder lb = iter.next();
			identificadores = lb.getIdIdentificadorPrimario();
			
			if(lb.getIdIdentificadorSecundario()!= null)
				identificadores = identificadores.concat("-".concat(lb.getIdIdentificadorSecundario()));
			
			if(lb.getIdIdentificadorTerciario()!= null)
				identificadores = identificadores.concat("-".concat(lb.getIdIdentificadorTerciario()));
			
			lb.setNome(identificadores);
			identificadores = "";
		}
		
    	return lista;
    }
	
	
	
	public void setService(PontoLayoutBinderService pontoLayoutBinderService) {
		setDefaultService(pontoLayoutBinderService);
	}
	
	private PontoLayoutBinderService getService() {
		return ((PontoLayoutBinderService)getDefaultService());
	}
	
	
}
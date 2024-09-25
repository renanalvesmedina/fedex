package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.CptTipoValor;
import com.mercurio.lms.vendas.model.service.CptTipoValorService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterTiposValorAction"
 */

public class ManterTiposValorAction extends CrudAction {

	private CptTipoValorService cptTipoValorService;
	
	public List findTiposValor(){
		return getCptTipoValorService().findListTipoValor(); 		
	}
	
	public Serializable findCdTpValor(TypedFlatMap map){
		TypedFlatMap tfm = new TypedFlatMap();
		
		CptTipoValor tipoValor = (CptTipoValor) cptTipoValorService.findById(map.getLong("idCptTipoValor"));  		
		tfm.put("codigo", tipoValor.getTpGrupoValor().getValue());
		return tfm;
	}
		
		
	public Serializable findById(java.lang.Long id) {
		return getCptTipoValorService().findById(id);
	}
	
	public Serializable store(CptTipoValor bean) {		
		return getCptTipoValorService().store(bean);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return getCptTipoValorService().findPaginated(criteria);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return getCptTipoValorService().getRowCount(criteria);
	}
	
	public void removeById(java.lang.Long id) {		
		getCptTipoValorService().removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
		getCptTipoValorService().removeByIds(ids);
    }	

	public CptTipoValorService getCptTipoValorService() {
		return cptTipoValorService;
	}

	public void setCptTipoValorService(CptTipoValorService cptTipoValorService) {
		this.cptTipoValorService = cptTipoValorService;
	}
	
}

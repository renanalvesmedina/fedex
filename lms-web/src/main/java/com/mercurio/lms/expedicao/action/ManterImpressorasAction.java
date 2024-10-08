package com.mercurio.lms.expedicao.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.service.ImpressoraService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionKey;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterImpressorasAction"
 */

public class ManterImpressorasAction extends CrudAction {
	private FilialService filialService;
	
	public void removeById(java.lang.Long id) {
		((ImpressoraService)defaultService).removeById(id);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((ImpressoraService)defaultService).removeByIds(ids);
	}

	public Impressora findById(java.lang.Long id) {
		return ((ImpressoraService)defaultService).findById(id);
	}

	public Serializable store(Impressora bean) {
		return ((ImpressoraService)defaultService).store(bean);
	}

	public FilialService getFilialService(){
		return this.filialService;
	}

	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}

	/**
	 * Metodo que chama o m�todo da lookup respectivo na service de filial
	 * @param criteria
	 * @return
	 */
	public List filialFindLookup(TypedFlatMap criteria) {
		return filialService.findLookupBySgFilial(criteria.getString("sgFilial"), criteria.getString("tpAcesso")); 	
	}

	/**
	 * Fun��o que retorna um mapa com os dados da filial vinculada ao 
	 * usuario que esta logado
	 * 
	 * @return
	 */
	public TypedFlatMap filialFindByUser() {
		Filial filial = null;
		TypedFlatMap retorno = new TypedFlatMap();
		filial = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);
		if(filial != null) {
			retorno.put("idFilial", filial.getIdFilial());
			retorno.put("sgFilial", filial.getSgFilial());
			retorno.put("nmFilial", filial.getPessoa().getNmFantasia());
			retorno.put("empresa.tpEmpresa", filial.getEmpresa().getTpEmpresa().getValue());
		}
		return retorno;
	}

	public void setImpressoraService(ImpressoraService impressoraService) {
		this.defaultService = impressoraService;
	}

}

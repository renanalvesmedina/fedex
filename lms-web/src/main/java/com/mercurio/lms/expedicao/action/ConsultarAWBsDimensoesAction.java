/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.DimensaoService;

/**
 * @author Luis Carlos poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.consultarAWBsDimensoesAction"
 * 
 */
public class ConsultarAWBsDimensoesAction extends CrudAction {
	
	private DimensaoService dimensaoService;
	
	public List findPaginated(TypedFlatMap criteria) {
		Long idAwb = criteria.getLong("idAwb");
		return getDimensaoService().findPaginatedByIdAwb(idAwb);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idAwb = criteria.getLong("idAwb");
		return getDimensaoService().getRowCountByIdAwb(idAwb);
	}

	/**
	 * @return Returns the dimensaoService.
	 */
	public DimensaoService getDimensaoService() {
		return dimensaoService;
	}

	/**
	 * @param dimensaoService The dimensaoService to set.
	 */
	public void setDimensaoService(DimensaoService dimensaoService) {
		this.dimensaoService = dimensaoService;
	}
	
	

}

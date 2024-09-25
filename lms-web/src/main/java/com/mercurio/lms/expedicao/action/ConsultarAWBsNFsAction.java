/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.util.FormatUtils;

/**
 * @author Luis Carlos poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.consultarAWBsNFsAction"
 *
 */
public class ConsultarAWBsNFsAction extends CrudAction {
	
	private static final boolean ESQUERDA = true;
	
	public List findPaginated(TypedFlatMap criteria) {
		Long idAwb = criteria.getLong("idAwb");
		List result = getNotaFiscalConhecimentoService().findPaginatedByIdAwb(idAwb);
		if (result != null && !result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				Map map = (Map) result.get(i);
				String nrNotaFiscal = FormatUtils.completaDados(map.remove("nrNotaFiscal"), "0", 9, 0, ESQUERDA );
				map.put("nrNotaFiscal", nrNotaFiscal);
			}
		}
		return result;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Long idAwb = criteria.getLong("idAwb");
		return getNotaFiscalConhecimentoService().getRowCountByIdAwb(idAwb);
	}

	public void setService(NotaFiscalConhecimentoService serviceService) {
		super.defaultService = serviceService;
	}
	
	public NotaFiscalConhecimentoService getNotaFiscalConhecimentoService() {
		return (NotaFiscalConhecimentoService) super.defaultService;
	}
	
}

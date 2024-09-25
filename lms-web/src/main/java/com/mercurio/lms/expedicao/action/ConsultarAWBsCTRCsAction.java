/**
 * 
 */
package com.mercurio.lms.expedicao.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.util.IntegerUtils;

/**
 * @author Luis Carlos poletto
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.consultarAWBsCTRCsAction"
 *
 */
public class ConsultarAWBsCTRCsAction extends CrudAction {
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = ResultSetPage.EMPTY_RESULTSET;
		Long idAwb = criteria.getLong("idAwb");
		List ctoAwbs = getCtoAwbService().findByIdAwb(idAwb);
		
		if (ctoAwbs != null) {
			FindDefinition def = FindDefinition.createFindDefinition(criteria);
			
			Comparator ordenador = new Comparator() {
				public int compare(Object arg0, Object arg1) {
					CtoAwb ctoAwb0 = (CtoAwb) arg0;
					CtoAwb ctoAwb1 = (CtoAwb) arg1;
					
					Conhecimento conhecimento0 = ctoAwb0.getConhecimento();
					Conhecimento conhecimento1 = ctoAwb1.getConhecimento();
					
					String sgFilial0 = conhecimento0.getFilialByIdFilialOrigem().getSgFilial();
					String sgFilial1 = conhecimento1.getFilialByIdFilialOrigem().getSgFilial();
					
					int compareSgFilial = sgFilial0.compareTo(sgFilial1);
					if (compareSgFilial == 0) {
						Long nrConhecimento0 = conhecimento0.getNrConhecimento();
						Long nrConhecimento1 = conhecimento1.getNrConhecimento();
						
						return nrConhecimento0.compareTo(nrConhecimento1);
					} 
					
					return compareSgFilial;
				}
			};
			
			rsp = MasterDetailAction.getResultSetPage(ctoAwbs, def.getCurrentPage(), def.getPageSize(), ordenador);
			
			Iterator itList = rsp.getList().iterator();
    		List newList = new ArrayList();

    		while(itList.hasNext()) {
    			CtoAwb ctoAwb = (CtoAwb) itList.next();
    			Conhecimento conhecimento = ctoAwb.getConhecimento();
    			TypedFlatMap map = new TypedFlatMap();

    			Aeroporto aeroportoOrigem = conhecimento.getAeroportoByIdAeroportoOrigem();
    			if (aeroportoOrigem != null) {
    				map.put("aeroportoByIdAeroportoOrigem.sgAeroporto", aeroportoOrigem.getSgAeroporto());
    			}
    			map.put("conhecimento.psReal", conhecimento.getPsReal());
    			map.put("idCtoAwb", Long.valueOf(ctoAwb.getIdCtoAwb().longValue()));
    			map.put("conhecimento.psAforado", conhecimento.getPsAforado());
    			map.put("conhecimento.qtVolumes", conhecimento.getQtVolumes());
    			map.put("conhecimento.vlMercadoria", conhecimento.getVlMercadoria());
    			map.put("conhecimento.nrConhecimento", ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrConhecimento(), conhecimento.getDvConhecimento()));
    			map.put("conhecimento.vlTotalDocServico", conhecimento.getVlTotalDocServico());
    			
    			newList.add(map);
    		}
    		
    		rsp.setList(newList);
			
		}
		
		return rsp;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		Integer result = IntegerUtils.ZERO;
		
		Long idAwb = criteria.getLong("idAwb");
		List ctoAwbs = getCtoAwbService().findByIdAwb(idAwb);
		
		if (ctoAwbs != null) {
			result = Integer.valueOf(ctoAwbs.size());
		}
		
		return result;
	}


	/**
	 * @return Returns the ctoAwbService.
	 */
	public CtoAwbService getCtoAwbService() {
		return (CtoAwbService) super.defaultService;
	}


	/**
	 * @param ctoAwbService The ctoAwbService to set.
	 */
	public void setService(CtoAwbService serviceService) {
		super.defaultService = serviceService;
	}

}

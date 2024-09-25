package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.HistoricoAfericaoSorterService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

public class HistoricoAfericaoSorterAction extends CrudAction {
	private FilialService filialService;
	private HistoricoAfericaoSorterService historicoAfericaoSorterService;

	public void setService(HistoricoAfericaoSorterService historicoAfericaoSorterService) {
		this.defaultService = historicoAfericaoSorterService;
	}

	public HistoricoAfericaoSorterService getService() {
		return (HistoricoAfericaoSorterService) this.defaultService;
	}
	
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial); 
			}
		}
		return result;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = historicoAfericaoSorterService.findPaginatedHistoricoAfericaoSorter(criteria);
		List<Map> historicoAfericaoList = (List<Map>) rsp.getList();
		for (Map map : historicoAfericaoList) {
			if(map.get("dimensoesPadrao").equals(map.get("dimensoesAferidas"))){
				map.put("ok", "S");
			}else{
				map.put("ok", "N");				
			}
		}	
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return historicoAfericaoSorterService.getRowCountHistoricoAfericaoSorter(criteria);
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setHistoricoAfericaoSorterService(HistoricoAfericaoSorterService historicoAfericaoSorterService) {
		this.historicoAfericaoSorterService = historicoAfericaoSorterService;
	}
}

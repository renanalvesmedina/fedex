package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.LiberacaoNotaNaturaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.swt.monitorarNotasNaturaAction"
 */
public class MonitorarNotasNaturaAction extends CrudAction {
	
	private FilialService filialService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	
    public List findLookupFilialByTpAcesso(Map criteria) {
		String sgFilial = (String)criteria.get("sgFilialDestino");
		List list = filialService.findLookupBySgFilial(sgFilial, "EM");
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Map mapFilial = (Map) iter.next();
			Map mapResult = new HashMap();
			mapResult.put("idFilial", mapFilial.get("idFilial"));
			mapResult.put("sgFilial", mapFilial.get("sgFilial"));
			Map mapPessoa = (Map) mapFilial.get("pessoa");
			mapResult.put("nmFantasia", mapPessoa.get("nmFantasia"));
    		retorno.add(mapResult);
    	}
    	return retorno;
    }
    
	public Map findDadosSessao() {
		Map<String, Object> retorno = new HashMap<String, Object>();
		Filial filial = SessionUtils.getFilialSessao();
		retorno.put("sgFilial", filial.getSgFilial());
		retorno.put("idFilial", filial.getIdFilial());
		retorno.put("nmFantasia", filial.getPessoa().getNmFantasia());
		return retorno;
	}
    
	public ResultSetPage findNotasNatura(TypedFlatMap criteria){
		return liberacaoNotaNaturaService.findNotasNatura(criteria);
	}
	
	public Integer getRowCountNotasNatura(TypedFlatMap criteria){
		return liberacaoNotaNaturaService.getRowCountNotasNatura(criteria);
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}
    
}

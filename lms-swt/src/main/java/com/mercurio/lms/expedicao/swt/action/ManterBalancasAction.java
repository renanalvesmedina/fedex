package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.expedicao.model.Balanca;
import com.mercurio.lms.expedicao.model.service.BalancaService;
/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.manterBalancasAction"
 */

public class ManterBalancasAction extends CrudAction {
	
	public void removeById(java.lang.Long id) {
		((BalancaService)defaultService).removeById(id);
	}
	
	public ResultSetPage findPaginated(Map criteria) {

		ResultSetPage rsp = ((BalancaService)defaultService).findPaginated(criteria);
		
		if (rsp != null) {
			List<Balanca> balancas = rsp.getList();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			if (balancas != null) {
				for (Balanca balanca : balancas) {
					result.add(populateMap(balanca));
				}
				rsp.setList(result);
			}
		}
		return rsp;
	}

	private Map<String, Object> populateMap(Balanca balanca) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idBalanca", balanca.getIdBalanca());
		map.put("dsBalanca", balanca.getDsBalanca());
		map.put("dsComandoLeitura", balanca.getDsComandoLeitura());
		map.put("dsVelocidade", balanca.getDsVelocidade());
		map.put("dsBitdados", balanca.getDsBitdados());
		map.put("dsParidade", balanca.getDsParidade());
		map.put("dsBitstop", balanca.getDsBitstop());
		map.put("dsExpressaoRegular", balanca.getDsExpressaoRegular());
		map.put("dsCharEstabilizado", balanca.getDsCharEstabilizado());
		return map;
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((BalancaService)defaultService).removeByIds(ids);
	}

	public Map findById(java.lang.Long id) {
		Balanca balanca = ((BalancaService)defaultService).findById(id);
		return populateMap(balanca);
	}
	
	public List findBalancas(Map criteria) {
		List<Balanca> balancas = ((BalancaService)defaultService).find(criteria);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (balancas != null && !balancas.isEmpty()) {
			for (int i = 0; i < balancas.size(); i++) {
				Balanca balanca = (Balanca) balancas.get(i);
				Map<String, Object> ramo = new HashMap<String, Object>();
				ramo.put("idBalanca", balanca.getIdBalanca());
				ramo.put("dsBalanca", balanca.getDsBalanca());
				result.add(ramo);
			}
		}
		return result;
	}

	public Map store(Map data) {
		Balanca bean = new Balanca();
		
		bean.setIdBalanca((Long) data.get("idBalanca"));
		bean.setDsBalanca((String)data.get("dsBalanca"));
		bean.setDsComandoLeitura((String)data.get("dsComandoLeitura"));
		bean.setDsVelocidade((String)data.get("dsVelocidade"));
		bean.setDsBitdados((String)data.get("dsBitdados"));
		bean.setDsParidade((String)data.get("dsParidade"));
		bean.setDsBitstop((String)data.get("dsBitstop"));
		bean.setDsExpressaoRegular((String)data.get("dsExpressaoRegular"));
		bean.setDsCharEstabilizado((String)data.get("dsCharEstabilizado"));
		
		Long idBalanca = (Long) ((BalancaService)defaultService).store(bean);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idBalanca", idBalanca);
		return result;
	}

	public void setBalancaService(BalancaService balancaService) {
		this.defaultService = balancaService;
	}
}
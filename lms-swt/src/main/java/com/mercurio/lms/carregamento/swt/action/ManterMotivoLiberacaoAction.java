package com.mercurio.lms.carregamento.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.HistoricoMpc;
import com.mercurio.lms.carregamento.model.LiberaMpc;
import com.mercurio.lms.carregamento.model.service.HistoricoMpcService;
import com.mercurio.lms.carregamento.model.service.LiberarMpcService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * 
 * @author rwendt@voiza.com.br
 *
 */
public class ManterMotivoLiberacaoAction extends CrudAction{
	
	private HistoricoMpcService historicoMpcService;

	public void removeById(java.lang.Long id) {
		((LiberarMpcService) defaultService).removeById(id);
	}

	public ResultSetPage findPaginated(Map criteria) {

		ResultSetPage rsp = ((LiberarMpcService) defaultService).findPaginated(criteria);

		if (rsp != null) {
			List<LiberaMpc> liberacoes = rsp.getList();
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			if (liberacoes != null) {
				for (LiberaMpc libera : liberacoes) {
					result.add(populateMap(libera));
				}
				rsp.setList(result);
			}
		}
		return rsp;
	}
	
	public Map findById(java.lang.Long id) {
		LiberaMpc libera = (LiberaMpc) ((LiberarMpcService) defaultService).findById(id);
		return populateMap(libera);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((LiberarMpcService) defaultService).removeByIds(ids);
	}
	
	private Map<String, Object> populateMap(LiberaMpc libera) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("idLiberaMPC", libera.getIdLiberaMPC());
		map.put("dsLiberaMPC", libera.getDsLiberaMPC());
		map.put("stLibera", libera.getStLibera().getDescription());
		map.put("tpLibera", libera.getTpLibera().getDescription());
		map.put("tpAutorizacao", libera.getTpAutorizacao().getDescription());
		map.put("dhCriacao", libera.getDhCriacao());

		return map;
	}
	
	public Map store(Map data) {
		LiberaMpc libera = new LiberaMpc();

		if (data.get("idLiberaMPC") != null) {
			libera.setIdLiberaMPC((Long) data.get("idLiberaMPC"));
		}
		libera.setDsLiberaMPC((String) data.get("dsLiberaMPC"));
		libera.setStLibera(new DomainValue((String) data.get("stLibera")));
		libera.setTpLibera(new DomainValue((String) data.get("tpLibera")));
		libera.setTpAutorizacao(new DomainValue((String) data.get("tpAutorizacao")));
		libera.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
		
		Long idLibera =  (Long) ((LiberarMpcService) defaultService).store(libera);
		
		Map<String, Object> mapResult = new HashMap<String, Object>();
		mapResult.put("idLiberaMPC", idLibera);
		
		return mapResult;
	}
	
	public void setLiberarMpcService(LiberarMpcService liberarMpcService) {
		this.defaultService = liberarMpcService;
	}

	public void setHistoricoMpcService(HistoricoMpcService historicoMpcService) {
		this.historicoMpcService = historicoMpcService;
	}
	
	public Map<String, Object> validaHistorico(Map map) {
		Long id = (Long) map.get("idLiberaMPC");

		Map<String, Object> mapResult = new HashMap<String, Object>();
		
		List<HistoricoMpc> historicosMpc = historicoMpcService.findByIdLiberaMpc(id);
				
		if (historicosMpc.size() > 0) {
			mapResult.put("isValido", true);
		} else {
			mapResult.put("isValido", false);
		}

		return mapResult;
	}
	
	/**
	 * Find para a tela de Cadastro, para trazer os values dos combos e aparecer corretamente.
	 * 
	 * @param id
	 * @return
	 */
	public Map findByIdCad(java.lang.Long id) {
		LiberaMpc libera = (LiberaMpc) ((LiberarMpcService) defaultService).findById(id);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("idLiberaMPC", libera.getIdLiberaMPC());
		map.put("dsLiberaMPC", libera.getDsLiberaMPC());
		map.put("stLibera", libera.getStLibera().getValue());
		map.put("tpAutorizacao", libera.getTpAutorizacao().getValue());
		map.put("tpLibera", libera.getTpLibera().getValue());
		map.put("dhCriacao", libera.getDhCriacao());
		return map;
	}
}
	
	

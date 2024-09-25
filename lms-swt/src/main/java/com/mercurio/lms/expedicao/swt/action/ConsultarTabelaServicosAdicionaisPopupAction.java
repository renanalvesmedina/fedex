package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ServicoAdicionalPrecificado;
import com.mercurio.lms.expedicao.model.service.TabelaServicoAdicionalService;

public class ConsultarTabelaServicosAdicionaisPopupAction extends CrudAction {
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;
		
	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {		
		Long idDivisaoCliente = criteria.getLong("idDivisaoCliente");
		Long idServico = criteria.getLong("idServico");
		
		List<ServicoAdicionalPrecificado> servicos = 
				tabelaServicoAdicionalService.findByTabelaCliente(idServico, idDivisaoCliente);
		
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		
		for(ServicoAdicionalPrecificado servico : servicos) {
			String descricao = tabelaServicoAdicionalService.findDsFormatada(servico, Boolean.FALSE);
			if(descricao != null) {
				Map<String, Object> newServico = new HashMap<String, Object>();
				newServico.put("dsServicoAdicional", servico.getDsParcela());
				newServico.put("dsValorComplemento", descricao);
				result.add(newServico);
			}
		}
		
		ResultSetPage<Map<String, Object>> rs = 
				new ResultSetPage<Map<String,Object>>(1, false, false, result, Long.valueOf(result.size()));
		
		return rs;
	}

	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
		this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
}

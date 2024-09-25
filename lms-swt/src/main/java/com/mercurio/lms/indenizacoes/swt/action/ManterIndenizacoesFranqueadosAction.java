package com.mercurio.lms.indenizacoes.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.CsvUtils;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.service.IndenizacoesFranqueadosService;

public class ManterIndenizacoesFranqueadosAction extends CrudAction {
	
	private IndenizacoesFranqueadosService indenizacoesFranqueadosService;
	
	private static final String SEPARATOR = ";";
	
	public List<TypedFlatMap> findIndenizacoesFranqueadas(TypedFlatMap criteria) {
		return indenizacoesFranqueadosService.findIndenizacoesFranqueadas(criteria);
	}

	public void setIndenizacoesFranqueadosService(IndenizacoesFranqueadosService indenizacoesFranqueadosService) {
		this.indenizacoesFranqueadosService = indenizacoesFranqueadosService;
	}
	
	public List getComboBoxNrParcelas() {
		return indenizacoesFranqueadosService.getComboBoxNrParcelas();
	}
	
	public void gerarDescontos(TypedFlatMap parameters) {
		ArrayList<TypedFlatMap> list = (ArrayList<TypedFlatMap>) parameters.get("list");
		indenizacoesFranqueadosService.storeDescontos(list);
	}
	
	@SuppressWarnings("unchecked")
	public Object executeCSV(TypedFlatMap parameters) throws Exception {
		Map<String,String> result = new HashMap<String, String>();
		final List<Map<String, Object>> listaIndenizacoesFranqueados = indenizacoesFranqueadosService.findRelatorioIndezacoesFranqueado(parameters);
		
		result.putAll(toMap("Documentos", listaIndenizacoesFranqueados));
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map toMap(String reportName, final List lista) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		if(!lista.isEmpty() && lista != null){
			String csv = CsvUtils.convertMappedListToCsv(lista, SEPARATOR);
			map.put(reportName, csv);
		}
		return map;
	}
}

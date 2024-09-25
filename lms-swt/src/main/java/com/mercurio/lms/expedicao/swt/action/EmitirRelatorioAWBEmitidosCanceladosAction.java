package com.mercurio.lms.expedicao.swt.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.report.EmitirRelatorioAWBEmitidosCanceladosService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class EmitirRelatorioAWBEmitidosCanceladosAction{
	private AeroportoService aeroportoService;
	private EmpresaService empresaService;
	private EmitirRelatorioAWBEmitidosCanceladosService emitirRelatorioAWBEmitidosCanceladosService;
	private ReportExecutionManager reportExecutionManager;

	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public EmitirRelatorioAWBEmitidosCanceladosService getemitirRelatorioAWBEmitidosCanceladosService() {
		return emitirRelatorioAWBEmitidosCanceladosService;
	}
	
	public void setEmitirRelatorioAWBEmitidosCanceladosService(
			EmitirRelatorioAWBEmitidosCanceladosService emitirRelatorioAWBEmitidosCanceladosService) {
		this.emitirRelatorioAWBEmitidosCanceladosService = emitirRelatorioAWBEmitidosCanceladosService;
	}
	
	/**
	 * Utilizado pela combo de cias aereas.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findCiaAerea(Map criteria) {
		List empresas = empresaService.findCiaAerea(criteria);
		if (empresas != null && !empresas.isEmpty()) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < empresas.size(); i++) {
				Empresa empresa = (Empresa) empresas.get(i);
				Map<String, Object> e = new HashMap<String, Object>();
				e.put("idEmpresa", empresa.getIdEmpresa());
				e.put("nmPessoa", empresa.getPessoa().getNmPessoa());
				result.add(e);
			}
			return result;
		}
		return null;
	}
	
	/**
	 * Lookups de aeroporto.
	 * 
	 * @param criteria
	 * @return
	 */
	public List findAeroporto(Map criteria) {
		List result = aeroportoService.findLookupAeroporto(criteria);
		if (result != null && !result.isEmpty()) {
			for (int i = 0; i < result.size(); i++) {
				Map aeroporto = (Map) result.get(i);
				Map pessoa = (Map) aeroporto.remove("pessoa");
				aeroporto.put("nmPessoa", pessoa.get("nmPessoa"));
			}
		}
		return result;
	}
	
	public Map getDefaultValues() {
		Map<String, YearMonthDay> result = new HashMap<String, YearMonthDay>();
		result.put("dataInicial", JTDateTimeUtils.getDataAtual().minusDays(10));
		result.put("dataFinal", JTDateTimeUtils.getDataAtual());
		result.put("dataHoje", JTDateTimeUtils.getDataAtual());
		return result;
	}
	

	/*
	 * GETTERS E SETTERS
	 */

	/**
	 * @param aeroportoService The aeroportoService to set.
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	
	/**
	 * @param empresaService The empresaService to set.
	 */
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public String execute(Map filters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);
		
		File file = emitirRelatorioAWBEmitidosCanceladosService.executeReport(tfm);
		if( file == null ){
			throw new BusinessException("LMS-3736");
		}
		return reportExecutionManager.generateReportLocator(file);
	}

}

package com.mercurio.lms.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class FiltroPaginacaoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Boolean report;
	private List<Map<String,String>> columns;
	private Integer qtRegistrosPagina;
	private Integer pagina;
	private Map<String, Object> filtros = new HashMap<String, Object>();
	private transient Map<String, Object> _filtros;
	private List<FiltroPaginacaoOrdenacaoDto> ordenacao = new ArrayList<FiltroPaginacaoOrdenacaoDto>();
	private Boolean remotePagination;
	
	public Integer getQtRegistrosPagina() {
		return qtRegistrosPagina;
	}
	public void setQtRegistrosPagina(Integer qtRegistrosPagina) {
		this.qtRegistrosPagina = qtRegistrosPagina;
	}
	public Integer getPagina() {
		return pagina;
	}
	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
	
	/**
	 * Converte para string os objtos por que o jackson deserializa numeros como inteiro, eventualmente, e o typedflatmap se vira melhor caso tudo seja string
	 * 
	 * @return
	 */
	public Map<String, Object> getFiltros() {
		if (_filtros == null) {
			_filtros = convertToString(filtros);
		}
		return _filtros;
	}
	
	public TypedFlatMap getTypedFlatMapWithPaginationInfo() {
		return getTypedFlatMapWithPaginationInfo(0);
	}
	
	public TypedFlatMap getTypedFlatMapWithPaginationInfo(Integer limiteRegistros) {
		TypedFlatMap toReturn = new TypedFlatMap(getFiltros());
		
		toReturn.put("_currentPage", getPagina() == null ? "1" : String.valueOf(getPagina()));
		toReturn.put("_pageSize", getQtRegistrosPagina() == null ? String.valueOf(limiteRegistros) : String.valueOf(getQtRegistrosPagina()));
		
		return toReturn;
	}
	
	private Map<String, Object> convertToString(Map<String, Object> map) {
		Map<String, Object> toReturn = new HashMap<String, Object>();
		for (String key: map.keySet()) {
			Object o = map.get(key);
			if (o != null) {
				if (o instanceof Map) {
					toReturn.putAll(convertToString(key, (Map<String, Object>)o));
				} else {
					toReturn.put(key, o.toString());
				}
			}
		}
		return toReturn;
	} 
	
	private Map<String, Object> convertToString(String root, Map<String, Object> map) {
		Map<String, Object> toReturn = new HashMap<String, Object>();
		for (String key: map.keySet()) {
			Object o = map.get(key);
			if (o != null) {
				if (o instanceof Map) {
					toReturn.putAll(convertToString(key, (Map<String, Object>)o));
				} else {
					toReturn.put(root+"."+key, o.toString());
				}
			}
		}
		return toReturn;
	} 
	
	public void setFiltros(Map<String, Object> filtros) {
		this.filtros = filtros;
	}
	public List<FiltroPaginacaoOrdenacaoDto> getOrdenacao() {
		return ordenacao;
	}
	public void setOrdenacao(List<FiltroPaginacaoOrdenacaoDto> ordenacao) {
		this.ordenacao = ordenacao;
	}
	public List<Map<String, String>> getColumns() {
		return columns;
	}
	public void setColumns(List<Map<String, String>> columns) {
		this.columns = columns;
	}
	public Boolean getReport() {
		return report;
	}
	public void setReport(Boolean report) {
		this.report = report;
	}
	public Boolean isRemotePagination() {
		return remotePagination;
	}
	public void setRemotePagination(Boolean remotePagination) {
		this.remotePagination = remotePagination;
	}
	
}

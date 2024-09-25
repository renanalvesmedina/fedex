package com.mercurio.lms.fretecarreteirocoletaentrega.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.carregamento.model.ControleCarga;

public class ParametrosCalculoDiariaNotaCreditoPadrao {

	private BigDecimal parametro;
	private List<ControleCarga> controlesCargaNoPeriodo;
	private Long idFilial;
	private Boolean filialPagaDiariaExcedente;
	private List<Map<String, Object>> notas;
	private boolean diariaPaga;
	
	public BigDecimal getParametro() {
		return parametro;
	}
	public void setParametro(BigDecimal parametro) {
		this.parametro = parametro;
	}
	
	public List<ControleCarga> getControlesCargaNoPeriodo() {
		return controlesCargaNoPeriodo;
	}
	public void setControlesCargaNoPeriodo(List<ControleCarga> controlesCargaNoPeriodo) {
		this.controlesCargaNoPeriodo = controlesCargaNoPeriodo;
	}
	public Long getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}
	public Boolean getFilialPagaDiariaExcedente() {
		return filialPagaDiariaExcedente;
	}
	public void setFilialPagaDiariaExcedente(Boolean filialPagaDiariaExcedente) {
		this.filialPagaDiariaExcedente = filialPagaDiariaExcedente;
	}
	public List<Map<String, Object>> getNotas() {
		return notas;
	}
	public void setNotas(List<Map<String, Object>> notas) {
		this.notas = notas;
	}
	public void setDiariaPaga(boolean diariaPaga) {
		this.diariaPaga = diariaPaga;
	}
	public boolean isDiariaPaga() {
		return diariaPaga;
	}
	
	

}

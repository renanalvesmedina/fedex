package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class Transponder implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum SITUACAO_TRANSPONDER{
		EM_USO("E"), DISPONIVEL("D"), INUTILIZADO("I");
		private String value;

		SITUACAO_TRANSPONDER(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
		
		public DomainValue getValorDominio(){
			return new DomainValue(value);
		}
	}
	
	private Long idTransponder;
	private ControleCarga controleCarga;
	private Long nrTransponder;
	private DomainValue tpSituacaoTransponder;
	private Filial filial;

	public Transponder() {
	}

	public Transponder(Long idTransponder, ControleCarga controleCarga, Long nrTransponder, DomainValue tpSituacaoTransponder, Filial filial) {
		this.idTransponder = idTransponder;
		this.controleCarga = controleCarga;
		this.nrTransponder = nrTransponder;
		this.tpSituacaoTransponder = tpSituacaoTransponder;
		this.filial = filial;
	}

	public Long getIdTransponder() {
		return idTransponder;
	}

	public void setIdTransponder(Long idTransponder) {
		this.idTransponder = idTransponder;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public Long getNrTransponder() {
		return nrTransponder;
	}

	public void setNrTransponder(Long nrTransponder) {
		this.nrTransponder = nrTransponder;
	}

	public DomainValue getTpSituacaoTransponder() {
		return tpSituacaoTransponder;
	}

	public void setTpSituacaoTransponder(DomainValue tpSituacaoTransponder) {
		this.tpSituacaoTransponder = tpSituacaoTransponder;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
}

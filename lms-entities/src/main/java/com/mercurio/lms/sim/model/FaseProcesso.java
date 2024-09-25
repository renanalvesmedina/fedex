package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;

public class FaseProcesso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idFaseProcesso;
	private Short cdFase;
	private String dsFase;

	public FaseProcesso() {
	}

	public FaseProcesso(Long idFaseProcesso, Short cdFase, String dsFase) {
		this.idFaseProcesso = idFaseProcesso;
		this.cdFase = cdFase;
		this.dsFase = dsFase;
	}

	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		bean.put("idFaseProcesso", this.getIdFaseProcesso());
		bean.put("idFaseProcessoGrid", this.getIdFaseProcesso());
		bean.put("cdFase", this.getCdFase());
		bean.put("dsFase", this.getDsFase());
		
		return bean;
	}
	
	public Long getIdFaseProcesso() {
		return idFaseProcesso;
	}
	
	public void setIdFaseProcesso(Long idFaseProcesso) {
		this.idFaseProcesso = idFaseProcesso;
	}
	
	public Short getCdFase() {
		return cdFase;
	}
	
	public void setCdFase(Short cdFase) {
		this.cdFase = cdFase;
	}
	
	public String getDsFase() {
		return dsFase;
	}
	
	public void setDsFase(String dsFase) {
		this.dsFase = dsFase;
	}

	public Long getIdFaseProcessoGrid() {
		return idFaseProcesso;
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaseProcesso))
			return false;
        FaseProcesso castOther = (FaseProcesso) other;
		return new EqualsBuilder().append(this.getIdFaseProcesso(),
				castOther.getIdFaseProcesso()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFaseProcesso())
            .toHashCode();
    }
}

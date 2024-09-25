package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * @author José Rodrigo Moraes
 * @since  28/04/2006
 */
public class ItemOcorrenciaPreFatura implements Serializable {
    
	private static final long serialVersionUID = 1L;

    /** persistent field */
    private Long idItemOcorrenciaPreFatura;
    
    /** persistent field */
    private OcorrenciaPreFatura ocorrenciaPreFatura;
    
    /** persistent field */
    private DomainValue tpDoctoServico;
    
    /** persistent field */
    private String sgFilial;
    
    /** persistent field */
    private Long nrDoctoServico;
    
    /** persistent field */
    private YearMonthDay dtEmissao;
    
    /** persistent field */
    private String obItemOcorrenciaPreFatura;

    public YearMonthDay getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public Long getIdItemOcorrenciaPreFatura() {
        return idItemOcorrenciaPreFatura;
    }

    public void setIdItemOcorrenciaPreFatura(Long idItemOcorrenciaPreFatura) {
        this.idItemOcorrenciaPreFatura = idItemOcorrenciaPreFatura;
    }

    public Long getNrDoctoServico() {
        return nrDoctoServico;
    }

    public void setNrDoctoServico(Long nrDoctoServico) {
        this.nrDoctoServico = nrDoctoServico;
    }

    public String getObItemOcorrenciaPreFatura() {
        return obItemOcorrenciaPreFatura;
    }

    public void setObItemOcorrenciaPreFatura(String obItemOcorrenciaPreFatura) {
        this.obItemOcorrenciaPreFatura = obItemOcorrenciaPreFatura;
    }

    public OcorrenciaPreFatura getOcorrenciaPreFatura() {
        return ocorrenciaPreFatura;
    }

    public void setOcorrenciaPreFatura(OcorrenciaPreFatura ocorrenciaPreFatura) {
        this.ocorrenciaPreFatura = ocorrenciaPreFatura;
    }

    public String getSgFilial() {
        return sgFilial;
    }

    public void setSgFilial(String sgFilial) {
        this.sgFilial = sgFilial;
    }

    public DomainValue getTpDoctoServico() {
        return tpDoctoServico;
    }

    public void setTpDoctoServico(DomainValue tpDoctoServico) {
        this.tpDoctoServico = tpDoctoServico;
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemOcorrenciaPreFatura))
			return false;
        ItemOcorrenciaPreFatura castOther = (ItemOcorrenciaPreFatura) other;
		return new EqualsBuilder().append(this.getIdItemOcorrenciaPreFatura(),
				castOther.getIdItemOcorrenciaPreFatura()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemOcorrenciaPreFatura())
            .toHashCode();
    }    
    
}

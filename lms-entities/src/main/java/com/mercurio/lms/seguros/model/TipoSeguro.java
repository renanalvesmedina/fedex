package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoSeguro implements Serializable {
	
	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoSeguro;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private VarcharI18n dsTipo;

    /** persistent field */
    private String sgTipo;

    /** persistent field */
    private List processoSinistros;

    /** persistent field */
    private List seguroTipoSinistros;

    /** persistent field */
    private List apoliceSeguros;

    /** persistent field */
    private List doctoServicoSeguros;
    
    /** persistent field */
    private String blEnvolveCarga;

    public Long getIdTipoSeguro() {
        return this.idTipoSeguro;
    }

    public void setIdTipoSeguro(Long idTipoSeguro) {
        this.idTipoSeguro = idTipoSeguro;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }
    


    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public VarcharI18n getDsTipo() {
		return dsTipo;
    }

	public void setDsTipo(VarcharI18n dsTipo) {
        this.dsTipo = dsTipo;
    }

    public String getSgTipo() {
		return sgTipo;
	}

	public void setSgTipo(String sgTipo) {
		this.sgTipo = sgTipo;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.TipoSinistro.class)     
    public List getSeguroTipoSinistros() {
        return this.seguroTipoSinistros;
    }

    public void setSeguroTipoSinistros(List seguroTipoSinistros) {
        this.seguroTipoSinistros = seguroTipoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List getApoliceSeguros() {
        return this.apoliceSeguros;
    }

    public void setApoliceSeguros(List apoliceSeguros) {
        this.apoliceSeguros = apoliceSeguros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServicoSeguros.class)     
    public List getDoctoServicoSeguros() {
        return this.doctoServicoSeguros;
    }

    public void setDoctoServicoSeguros(List doctoServicoSeguros) {
        this.doctoServicoSeguros = doctoServicoSeguros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoSeguro",
				getIdTipoSeguro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoSeguro))
			return false;
        TipoSeguro castOther = (TipoSeguro) other;
		return new EqualsBuilder().append(this.getIdTipoSeguro(),
				castOther.getIdTipoSeguro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoSeguro()).toHashCode();
    }

	public String getBlEnvolveCarga() {
		return blEnvolveCarga;
	}

	public void setBlEnvolveCarga(String blEnvolveCarga) {
		this.blEnvolveCarga = blEnvolveCarga;
	}

}

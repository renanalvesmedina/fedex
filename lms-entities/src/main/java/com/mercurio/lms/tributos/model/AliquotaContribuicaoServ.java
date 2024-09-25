package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class AliquotaContribuicaoServ implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAliquotaContribuico;

    /** persistent field */
    private BigDecimal vlPiso;

    /** persistent field */
    private BigDecimal pcAliquota;
    
    /** persistent field */
    private BigDecimal pcBaseCalcReduzida;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpImposto;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String obAliquotaContribuicaoServ;
    
    /** persistent field */
    private Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.tributos.model.ServicoTributo servicoTributo;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional;

    private List aliquotasContribuicaoServMunic;
    
    public Long getIdAliquotaContribuico() {
        return this.idAliquotaContribuico;
    }

    public void setIdAliquotaContribuico(Long idAliquotaContribuico) {
        this.idAliquotaContribuico = idAliquotaContribuico;
    }

    public BigDecimal getVlPiso() {
        return this.vlPiso;
    }

    public void setVlPiso(BigDecimal vlPiso) {
        this.vlPiso = vlPiso;
    }

    public BigDecimal getPcAliquota() {
        return this.pcAliquota;
    }

    public void setPcAliquota(BigDecimal pcAliquota) {
        this.pcAliquota = pcAliquota;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtInicioVigencia) {
        this.dtVigenciaInicial = dtInicioVigencia;
    }

    public DomainValue getTpImposto() {
        return this.tpImposto;
    }

    public void setTpImposto(DomainValue tpImposto) {
        this.tpImposto = tpImposto;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtFimVigencia) {
        this.dtVigenciaFinal = dtFimVigencia;
    }

    public String getObAliquotaContribuicaoServ() {
        return this.obAliquotaContribuicaoServ;
    }

    public void setObAliquotaContribuicaoServ(String obAliquotaContribuicaoServ) {
        this.obAliquotaContribuicaoServ = obAliquotaContribuicaoServ;
    }
    
    public com.mercurio.lms.tributos.model.ServicoTributo getServicoTributo() {
        return this.servicoTributo;
    }

	public void setServicoTributo(
			com.mercurio.lms.tributos.model.ServicoTributo servicoTributo) {
        this.servicoTributo = servicoTributo;
    }

    public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
        return this.servicoAdicional;
    }

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
        this.servicoAdicional = servicoAdicional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAliquotaContribuico",
				getIdAliquotaContribuico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AliquotaContribuicaoServ))
			return false;
        AliquotaContribuicaoServ castOther = (AliquotaContribuicaoServ) other;
		return new EqualsBuilder().append(this.getIdAliquotaContribuico(),
				castOther.getIdAliquotaContribuico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAliquotaContribuico())
            .toHashCode();
    }

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public BigDecimal getPcBaseCalcReduzida() {
		return pcBaseCalcReduzida;
	}

	public void setPcBaseCalcReduzida(BigDecimal pcBaseCalcReduzida) {
		this.pcBaseCalcReduzida = pcBaseCalcReduzida;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaContribuicaoServMunic.class)     
	public List getAliquotasContribuicaoServMunic() {
		return aliquotasContribuicaoServMunic;
}

	public void setAliquotasContribuicaoServMunic(List aliquotasContribuicaoServMunic) {
		this.aliquotasContribuicaoServMunic = aliquotasContribuicaoServMunic;
	}

}

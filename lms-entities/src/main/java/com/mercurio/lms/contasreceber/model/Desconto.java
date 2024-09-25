package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Desconto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDesconto;

    /** persistent field */
    private ReciboDesconto reciboDesconto;

    /** persistent field */
    private DemonstrativoDesconto demonstrativoDesconto;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** persistent field */
    private BigDecimal vlDesconto;

    /** nullable persistent field */
    private String obDesconto;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.MotivoDesconto motivoDesconto;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat;

    /** persistent field */    
    /**
	 * campo podera ser tanto uma pendencia quanto um questionamento fatura. Ver
	 * ET.
     */
    private Long idPendencia;
    
    /** campo não persistente */
    private String nrDocumento;
    
    /** campo não persistente */
    private BigDecimal percentualDesconto;
    
    private DomainValue tpSetorCausadorAbatimento;
    
    private String obAcaoCorretiva;
    
    private Integer versao;
    
    private List anexos;

    public Long getIdDesconto() {
        return this.idDesconto;
    }

    public void setIdDesconto(Long idDesconto) {
        this.idDesconto = idDesconto;
    }    

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public String getObDesconto() {
        return this.obDesconto;
    }

    public void setObDesconto(String obDesconto) {
        this.obDesconto = obDesconto;
    }

    public com.mercurio.lms.contasreceber.model.MotivoDesconto getMotivoDesconto() {
        return this.motivoDesconto;
    }

	public void setMotivoDesconto(
			com.mercurio.lms.contasreceber.model.MotivoDesconto motivoDesconto) {
        this.motivoDesconto = motivoDesconto;
    }

    public com.mercurio.lms.contasreceber.model.DevedorDocServFat getDevedorDocServFat() {
        return this.devedorDocServFat;
    }

	public void setDevedorDocServFat(
			com.mercurio.lms.contasreceber.model.DevedorDocServFat devedorDocServFat) {
        this.devedorDocServFat = devedorDocServFat;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDesconto", getIdDesconto())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Desconto))
			return false;
        Desconto castOther = (Desconto) other;
		return new EqualsBuilder().append(this.getIdDesconto(),
				castOther.getIdDesconto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDesconto()).toHashCode();
    }

    public String getNrDocumento() {
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    public void setPercentualDesconto(BigDecimal percentualDesconto) {
        this.percentualDesconto = percentualDesconto;
    }

    public ReciboDesconto getReciboDesconto() {
        return reciboDesconto;
    }

    public void setReciboDesconto(ReciboDesconto reciboDesconto) {
        this.reciboDesconto = reciboDesconto;
    }

    public DemonstrativoDesconto getDemonstrativoDesconto() {
        return demonstrativoDesconto;
    }

	public void setDemonstrativoDesconto(
			DemonstrativoDesconto demonstrativoDesconto) {
        this.demonstrativoDesconto = demonstrativoDesconto;
    }

    public DomainValue getTpSetorCausadorAbatimento() {
		return tpSetorCausadorAbatimento;
}

	public void setTpSetorCausadorAbatimento(
			DomainValue tpSetorCausadorAbatimento) {
		this.tpSetorCausadorAbatimento = tpSetorCausadorAbatimento;
	}

	public String getObAcaoCorretiva() {
		return obAcaoCorretiva;
	}

	public void setObAcaoCorretiva(String obAcaoCorretiva) {
		this.obAcaoCorretiva = obAcaoCorretiva;
	}

	public void setIdPendencia(Long idPendencia) {
		this.idPendencia = idPendencia;
	}

	public Long getIdPendencia() {
		return idPendencia;
	}
	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public void setAnexos(List anexos) {
		this.anexos = anexos;
	}

	public List getAnexos() {
		return anexos;
	}

}

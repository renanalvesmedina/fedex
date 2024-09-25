package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class DescontoLog implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long idDescontoLog;
	
	private ItemFaturaLog itemFaturaLog;

    /** identifier field */
    private Desconto desconto;

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
    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    
    private String loginLog;
    
    private DateTime dhLog;
    
    private String opLog;
    
    public ItemFaturaLog getItemFaturaLog() {
		return itemFaturaLog;
	}
    
    public void setItemFaturaLog(ItemFaturaLog itemFaturaLog) {
		this.itemFaturaLog = itemFaturaLog;
	}
    
    public Desconto getDesconto() {
		return desconto;
	}

	public void setDesconto(Desconto desconto) {
		this.desconto = desconto;
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
		return new ToStringBuilder(this).append("idDescontoLog",
				getIdDescontoLog()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescontoLog))
			return false;
        DescontoLog castOther = (DescontoLog) other;
		return new EqualsBuilder().append(this.getIdDescontoLog(),
				castOther.getIdDescontoLog()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescontoLog()).toHashCode();
    }

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
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

	public DateTime getDhLog() {
		return dhLog;
	}

	public void setDhLog(DateTime dhLog) {
		this.dhLog = dhLog;
	}

	public Long getIdDescontoLog() {
		return idDescontoLog;
	}

	public void setIdDescontoLog(Long idDescontoLog) {
		this.idDescontoLog = idDescontoLog;
	}

	public String getLoginLog() {
		return loginLog;
	}

	public void setLoginLog(String loginLog) {
		this.loginLog = loginLog;
	}

	public String getOpLog() {
		return opLog;
	}

	public void setOpLog(String opLog) {
		this.opLog = opLog;
	}
}
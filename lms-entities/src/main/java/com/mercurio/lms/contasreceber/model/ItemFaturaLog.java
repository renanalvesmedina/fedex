package com.mercurio.lms.contasreceber.model;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

public class ItemFaturaLog {

	private Long idItemFaturaLog;
	
    private ItemFatura itemFatura;
        
    private Integer versao;    

    /** persistent field */
    private Fatura fatura;

    /** persistent field */
    private DevedorDocServFat devedorDocServFat;
    
    private FaturaLog faturaLog;
    
    private List descontoLogs;
    
    private String loginLog;
    
    private DateTime dhLog;
    
    private String opLog;

	public ItemFatura getItemFatura() {
		return itemFatura;
	}

	public void setItemFatura(ItemFatura itemFatura) {
		this.itemFatura = itemFatura;
	}

	public Long getIdItemFaturaLog() {
		return idItemFaturaLog;
	}

	public void setIdItemFaturaLog(Long idItemFaturaLog) {
		this.idItemFaturaLog = idItemFaturaLog;
	}

	public FaturaLog getFaturaLog() {
		return faturaLog;
	}

	public void setFaturaLog(FaturaLog faturaLog) {
		this.faturaLog = faturaLog;
	}

	public DevedorDocServFat getDevedorDocServFat() {
		return devedorDocServFat;
	}

	public void setDevedorDocServFat(DevedorDocServFat devedorDocServFat) {
		this.devedorDocServFat = devedorDocServFat;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public DateTime getDhLog() {
		return dhLog;
	}

	public void setDhLog(DateTime dhLog) {
		this.dhLog = dhLog;
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

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.DescontoLog.class)
	public List getDescontoLogs() {
		return descontoLogs;
	}

	public void setDescontoLogs(List descontoLogs) {
		this.descontoLogs = descontoLogs;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idItemFaturaLog",
				getIdItemFaturaLog()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ItemFaturaLog))
			return false;
        ItemFaturaLog castOther = (ItemFaturaLog) other;
		return new EqualsBuilder().append(this.getIdItemFaturaLog(),
				castOther.getIdItemFaturaLog()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdItemFaturaLog()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
}
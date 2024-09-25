package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity(name="com.mercurio.lms.indenizacoes.model.AvisoPagoRimInd")
@Table(name="AVISO_PAGO_RIM")
@SequenceGenerator(name="SQ_AVISO_PAGO_RIM", sequenceName="AVISO_PAGO_RIM_SQ")
public class AvisoPagoRimInd implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long idAvisoPagoRim;
	private DateTime dhAvisoPagoRim;
	private String dsEmail;                                                                                                                                                                                 
	private Boolean blRetornou;                                                                                                                                                                   
	private ReciboIndenizacao reciboIndenizacao;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_AVISO_PAGO_RIM")
	@Column(name="ID_AVISO_PAGO_RIM", nullable=false)
	public Long getIdAvisoPagoRim() {
		return idAvisoPagoRim;
	}
	public void setIdAvisoPagoRim(Long idAvisoPagoRim) {
		this.idAvisoPagoRim = idAvisoPagoRim;
	}
	
	@Columns(columns = {
			@Column(name = "DH_AVISO_PAGO_RIM", nullable = false),
			@Column(name = "DH_AVISO_PAGO_RIM_TZR", nullable = false)
		}
	)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhAvisoPagoRim() {
		return dhAvisoPagoRim;
	}
	public void setDhAvisoPagoRim(DateTime dhAvisoPagoRim) {
		this.dhAvisoPagoRim = dhAvisoPagoRim;
	}
	
	@Column(name="DS_EMAIL", nullable=false)
	public String getDsEmail() {
		return dsEmail;
	}
	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}
	
	@Column(name="BL_RETORNOU",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlRetornou() {
		return blRetornou;
	}
	public void setBlRetornou(Boolean blRetornou) {
		this.blRetornou = blRetornou;
	}
	
	@ManyToOne
	@JoinColumn(name="ID_RECIBO_INDENIZACAO", nullable=false)
	public ReciboIndenizacao getReciboIndenizacao() {
		return reciboIndenizacao;
	}
	public void setReciboIndenizacao(ReciboIndenizacao reciboIndenizacao) {
		this.reciboIndenizacao = reciboIndenizacao;
	}
	
	
    public String toString() {
        return new ToStringBuilder(this)
            .append("idAvisoPagoRim", getIdAvisoPagoRim())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof AvisoPagoRimInd) ) return false;
        AvisoPagoRimInd castOther = (AvisoPagoRimInd) other;
        return new EqualsBuilder()
            .append(this.getIdAvisoPagoRim(), castOther.getIdAvisoPagoRim())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getIdAvisoPagoRim())
            .toHashCode();
    }

}

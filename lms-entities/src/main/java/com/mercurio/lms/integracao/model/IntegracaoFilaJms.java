package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "INTEGRACAO_FILA_JMS")
@SequenceGenerator(name = "INTEGRACAO_FILA_JMS_SQ", sequenceName = "INTEGRACAO_FILA_JMS_SQ", allocationSize = 1)
public class IntegracaoFilaJms implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTEGRACAO_FILA_JMS_SQ")
	@Column(name = "ID_INTEGRACAO_FILA_JMS", nullable = false)
	private Long idIntegracaoFilaJms;

	@Column(name = "NM_INTEGRACAO_FILA_JMS")
	private String nmIntegracaoFilaJms;

	@Column(name = "IDENTIFICADOR")
	private String identificador;
	
	@Column(name = "MESSAGE_JSON", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.CLobUserType")
	private String messageJson;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	@Column(name = "BL_ERRO")
	private Boolean blErro;

	@Column(name = "HEADER_JSON")
	private String headerJson;
	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	@Column(name = "BL_ATIVO")
	private Boolean blAtivo = true;
		
	@Column(name = "TP_DESTINO_IBM_MQ")
	private String tpDestinoIbmMq;

    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    @Column(name = "BL_COMPRESS_MESSAGE")
    private Boolean blCompressMessage;

	public Long getIdIntegracaoFilaJms() {
		return idIntegracaoFilaJms;
	}
	public void setIdIntegracaoFilaJms(Long idIntegracaoFilaJms) {
		this.idIntegracaoFilaJms = idIntegracaoFilaJms;
	}
	public String getNmIntegracaoFilaJms() {
		return nmIntegracaoFilaJms;
	}
	public void setNmIntegracaoFilaJms(String nmIntegracaoFilaJms) {
		this.nmIntegracaoFilaJms = nmIntegracaoFilaJms;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getMessageJson() {
		return messageJson;
	}
	public void setMessageJson(String messageJson) {
		this.messageJson = messageJson;
	}
	public Boolean getBlErro() {
		return blErro;
	}
	public void setBlErro(Boolean blErro) {
		this.blErro = blErro;
	}
	public String getHeaderJson() {
		return headerJson;
	}
	public void setHeaderJson(String headerJson) {
		this.headerJson = headerJson;
	}
	
	public Boolean getBlAtivo() {
		return blAtivo;
	}
	public void setBlAtivo(Boolean blAtivo) {
		this.blAtivo = blAtivo;
	}
	
	public Boolean getBlCompressMessage() {
        return blCompressMessage;
    }
    public String getTpDestinoIbmMq() {
        return tpDestinoIbmMq;
    }
    public void setTpDestinoIbmMq(String tpDestinoIbmMq) {
        this.tpDestinoIbmMq = tpDestinoIbmMq;
    }
    public void setBlCompressMessage(Boolean blCompressMessage) {
        this.blCompressMessage = blCompressMessage;
    }
	
    @Override
	public boolean equals(Object obj) {
		if ((this == obj))
			return true;
		if (!(obj instanceof IntegracaoFilaJms))
			return false;
		IntegracaoFilaJms castOther = (IntegracaoFilaJms) obj;
		return new EqualsBuilder().append(this.getIdIntegracaoFilaJms(),
				castOther.getIdIntegracaoFilaJms()).isEquals();
	}
    
    @Override
   	public int hashCode() {
   		final int prime = 31;
   		int result = 1;
   		result = prime * result + ((idIntegracaoFilaJms == null) ? 0 : idIntegracaoFilaJms.hashCode());
   		return result;
   	}

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_INTEGRACAO_FILA_JMS ", idIntegracaoFilaJms)
				.append("NM_INTEGRACAO_FILA_JMS ", nmIntegracaoFilaJms)
				.append("IDENTIFICADOR ", identificador)
				.append("HEADER_JSON ", headerJson)
				.append("BL_ERRO ", blErro)
				.append("MESSAGE_JSON ", messageJson)
				.append("BL_ATIVO ", blAtivo)
				.append("TP_DESTINO_IBM_MQ ", tpDestinoIbmMq)
				.append("BL_COMPRESS_MESSAGE ", blCompressMessage)
				.toString();
	}
}

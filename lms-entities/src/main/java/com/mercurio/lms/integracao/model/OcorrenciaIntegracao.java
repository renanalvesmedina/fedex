package com.mercurio.lms.integracao.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.integration.Integration;

public class OcorrenciaIntegracao implements Serializable {

	private static final long serialVersionUID = 1L;

	private Logger log = LogManager.getLogger(this.getClass());
	private Long idOcorrenciaIntegracao;
	private byte[] message = {};
	private String type;
	private String pi;
	private String layout;
	private boolean approve;
	private String filial;
	private String codigoDocumento;
	private String filial2;
	private String codigoDocumento2;	
	private String dsErro;
	private transient Integration integration;

	public String getFilial2() {
		return filial2;
	}

	public void setFilial2(String filial2) {
		this.filial2 = filial2;
	}

	public String getCodigoDocumento2() {
		return codigoDocumento2;
	}

	public void setCodigoDocumento2(String codigoDocumento2) {
		this.codigoDocumento2 = codigoDocumento2;
	}

	public String getDsErro() {
		return dsErro;
	}

	public void setDsErro(String dsErro) {
		this.dsErro = dsErro;
	}

	public String getFilial() {
		return filial;
	}

	public void setFilial(String filial) {
		this.filial = filial;
	}

	public String getCodigoDocumento() {
		return codigoDocumento;
	}

	public void setCodigoDocumento(String codigoDocumento) {
		this.codigoDocumento = codigoDocumento;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Integration getIntegration() {
		return integration;
	}

	public void setIntegration(Integration integration) {
		this.integration = integration;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPi() {
		return pi;
	}

	public void setPi(String pi) {
		this.pi = pi;
	}

	public boolean isApprove() {
		return approve;
	}

	public void setApprove(boolean approve) {
		this.approve = approve;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("codigo()",
				this.getIdOcorrenciaIntegracao()).toString();
    }
    
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaIntegracao))
			return false;
        OcorrenciaIntegracao castOther = (OcorrenciaIntegracao) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaIntegracao(),
				castOther.getIdOcorrenciaIntegracao()).isEquals();
    }    
    
    public int hashCode() {
		return new HashCodeBuilder().append(this.getIdOcorrenciaIntegracao())
            .toHashCode();
    }

	public Long getIdOcorrenciaIntegracao() {
		return idOcorrenciaIntegracao;
	}

	public void setIdOcorrenciaIntegracao(Long idOcorrenciaIntegracao) {
		this.idOcorrenciaIntegracao = idOcorrenciaIntegracao;
	}
	
	/** Don't invoke this.  Used by Hibernate only. */
	public void setMessageBlob(Blob messageBlob) {
		this.message = this.toByteArray(messageBlob);
	}
	
	/** Don't invoke this.  Used by Hibernate only. */
	public Blob getMessageBlob() {
		return Hibernate.createBlob(this.message);
	}
	
	private byte[] toByteArray(Blob fromBlob) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			return toByteArrayImpl(fromBlob, baos);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
	}

	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
			throws SQLException, IOException {
		byte[] buf = new byte[4000];
		InputStream is = fromBlob.getBinaryStream();
		try {
			for (;;) {
				int dataSize = is.read(buf);
				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.error(e);
				}
			}
		}
		return baos.toByteArray();
	}
}

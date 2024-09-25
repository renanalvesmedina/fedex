package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "ANEXO_RIM")
@SequenceGenerator(name = "ANEXO_RIM_SQ", sequenceName = "ANEXO_RIM_SQ")
public class AnexoRim implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANEXO_RIM_SQ")
	@Column(name = "ID_ANEXO_RIM", nullable = false)
	private Long idAnexoRim;
	
	@ManyToOne
	@JoinColumn(name = "ID_RECIBO_INDENIZACAO", nullable = false)
	private ReciboIndenizacao reciboIndenizacao;
	
	@ManyToOne()
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuarioLMS;
 	
	@Column(name = "DS_ANEXO", nullable = false)
	private String descAnexo;
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false), 
						 @Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;
	
	@Column(name = "DH_CRIACAO_TZR", nullable = false, insertable = false, updatable = false)
	private String dhCriacaoTimeZone; 
	
	@Lob
	@Column(name = "DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;
	
	public Long getIdAnexoRim() {
		return idAnexoRim;
	}

	public void setIdAnexoRim(Long idAnexoRim) {
		this.idAnexoRim = idAnexoRim;
	}

	public ReciboIndenizacao getReciboIndenizacao() {
		return reciboIndenizacao;
	}

	public void setReciboIndenizacao(ReciboIndenizacao reciboIndenizacao) {
		this.reciboIndenizacao = reciboIndenizacao;
	}

	public String getDescAnexo() {
		return descAnexo;
	}

	public void setDescAnexo(String descAnexo) {
		this.descAnexo = descAnexo;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public String getDhCriacaoTimeZone() {
		return dhCriacaoTimeZone;
	}

	public void setDhCriacaoTimeZone(String dhCriacaoTimeZone) {
		this.dhCriacaoTimeZone = dhCriacaoTimeZone;
	}
	
	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public String toString() {
        return new ToStringBuilder(this).append("idAnexoRim", getIdAnexoRim()).toString();
    }
	
	public UsuarioLMS getUsuarioLMS() {
		return usuarioLMS;
	}

	public void setUsuarioLMS(UsuarioLMS usuarioLMS) {
		this.usuarioLMS = usuarioLMS;
	}
	
	@Override
	public boolean equals(Object obj) {
		if ((this == obj))
			return true;
		if (!(obj instanceof AnexoRim))
			return false;
		AnexoRim castOther = (AnexoRim) obj;
		return new EqualsBuilder().append(this.getIdAnexoRim(),
				castOther.getIdAnexoRim()).isEquals();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAnexoRim == null) ? 0 : idAnexoRim.hashCode());
		return result;
	}

}
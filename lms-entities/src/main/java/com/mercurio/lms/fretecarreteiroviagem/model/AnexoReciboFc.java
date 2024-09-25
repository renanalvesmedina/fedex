package com.mercurio.lms.fretecarreteiroviagem.model;

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
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "ANEXO_RECIBO_FC")
@SequenceGenerator(name = "ANEXO_RECIBO_FC_SQ", sequenceName = "ANEXO_RECIBO_FC_SQ", allocationSize = 1)
public class AnexoReciboFc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANEXO_RECIBO_FC_SQ")
	@Column(name = "ID_ANEXO_RECIBO_FC", nullable = false)
	private Long idAnexoReciboFc;
	
	@ManyToOne
	@JoinColumn(name = "ID_RECIBO_FRETE_CARRETEIRO", nullable = false)
	private ReciboFreteCarreteiro reciboFreteCarreteiro;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuarioLMS;
 	
	@Column(name = "DS_ANEXO", nullable = false, length = 250)
	private String descAnexo;
	
	@Formula("TRIM(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(dc_arquivo, 1024)))")
	private String nmArquivo;
	
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
	
	public Long getIdAnexoReciboFc() {
		return idAnexoReciboFc;
	}

	public void setIdAnexoReciboFc(Long idAnexoReciboFc) {
		this.idAnexoReciboFc = idAnexoReciboFc;
	}

	public ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	public String getDescAnexo() {
		return descAnexo;
	}

	public void setDescAnexo(String descAnexo) {
		this.descAnexo = descAnexo;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
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
        return new ToStringBuilder(this).append("idAnexoReciboFc", getIdAnexoReciboFc()).toString();
    }
	
	public UsuarioLMS getUsuarioLMS() {
		return usuarioLMS;
	}

	public void setUsuarioLMS(UsuarioLMS usuarioLMS) {
		this.usuarioLMS = usuarioLMS;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAnexoReciboFc == null) ? 0 : idAnexoReciboFc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		
		if (!(obj instanceof AnexoReciboFc)){
			return false;
		}
		
		AnexoReciboFc castOther = (AnexoReciboFc) obj;
		
		return new EqualsBuilder().append(
				this.getIdAnexoReciboFc(), 
				castOther.getIdAnexoReciboFc()).isEquals();
	}
}
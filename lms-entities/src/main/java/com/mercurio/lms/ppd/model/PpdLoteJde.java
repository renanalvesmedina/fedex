package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "PPD_LOTES_JDE")
@SequenceGenerator(name = "LOTE_JDE_SEQ", sequenceName = "PPD_LOTES_JDE_SQ")
public class PpdLoteJde implements Serializable  {	
	private static final long serialVersionUID = 1L;
	private Long idLoteJde;
	private String nrLoteJde;
	private DateTime dhCriacao;
	private DateTime dhEnvio;
	private Boolean blBloqueado;
	private UsuarioLMS usuario;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOTE_JDE_SEQ")
	@Column(name = "ID_LOTE_JDE", nullable = false)
	public Long getIdLoteJde() {
		return idLoteJde;
	}

	public void setIdLoteJde(Long idLoteJde) {
		this.idLoteJde = idLoteJde;
	}	
	
	@Column(name = "NR_LOTE_JDE")
	public String getNrLoteJde() {
		return nrLoteJde;
	}

	public void setNrLoteJde(String nrLoteJde) {
		this.nrLoteJde = nrLoteJde;
	}
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false),
			@Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}
	
	@Columns(columns = { @Column(name = "DH_ENVIO"),
			@Column(name = "DH_ENVIO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	public DateTime getDhEnvio() {
		return dhEnvio;
	}

	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}
	
	@Column(name="BL_BLOQUEADO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")		
	public Boolean getBlBloqueado() {
		return blBloqueado;
	}

	public void setBlBloqueado(Boolean blBloqueado) {
		this.blBloqueado = blBloqueado;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idLoteJde", this.getIdLoteJde());
		bean.put("nrLoteJde", this.getNrLoteJde());
		bean.put("dhCriacao", this.getDhCriacao());
		bean.put("dhEnvio", this.getDhEnvio());
		bean.put("blBloqueado", this.getBlBloqueado());
		if(this.getUsuario() != null) {
			bean.put("idUsuario", this.getUsuario().getIdUsuario()); 
			if(this.getUsuario().getUsuarioADSM() != null)
				bean.put("nmUsuario", this.getUsuario().getUsuarioADSM()
						.getNmUsuario());
		}
		return bean;
	}
}

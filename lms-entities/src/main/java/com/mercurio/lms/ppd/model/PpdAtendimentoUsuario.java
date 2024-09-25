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

import org.hibernate.annotations.Type;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "PPD_ATENDIMENTO_USUARIOS")
@SequenceGenerator(name = "ATENDIMENTO_USUARIO_SEQ", sequenceName = "PPD_ATENDIMENTO_USUARIOS_SQ")
public class PpdAtendimentoUsuario implements Serializable  {	
	private static final long serialVersionUID = 1L;
	private Long idAtendimentoUsuario;
	private Boolean blAtivo;
	private PpdGrupoAtendimento grupoAtendimento;
	private UsuarioLMS usuario;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATENDIMENTO_USUARIO_SEQ")
	@Column(name = "ID_ATENDIMENTO_USUARIO", nullable = false)
	public Long getIdAtendimentoUsuario() {
		return idAtendimentoUsuario;
	}

	public void setIdAtendimentoUsuario(Long idAtendimentoUsuario) {
		this.idAtendimentoUsuario = idAtendimentoUsuario;
	}
	
	@Column(name="BL_ATIVO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")	
	public Boolean getBlAtivo() {
		return blAtivo;
	}

	public void setBlAtivo(Boolean blAtivo) {
		this.blAtivo = blAtivo;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO_ATENDIMENTO", nullable = false)
	public PpdGrupoAtendimento getGrupoAtendimento() {
		return grupoAtendimento;
	}

	public void setGrupoAtendimento(PpdGrupoAtendimento grupoAtendimento) {
		this.grupoAtendimento = grupoAtendimento;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idAtendimentoUsuario", this.getIdAtendimentoUsuario());
		bean.put("blAtivo", this.getBlAtivo());
		bean.putAll(this.getGrupoAtendimento().getMapped());		
		bean.put("idUsuario", this.getUsuario().getIdUsuario());		
		bean.put("nmUsuario", this.getUsuario().getUsuarioADSM().getNmUsuario());
		bean.put("nrMatricula", this.getUsuario().getUsuarioADSM()
				.getNrMatricula());
		return bean;
	}
}

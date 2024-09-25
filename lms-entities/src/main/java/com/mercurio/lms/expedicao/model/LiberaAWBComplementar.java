package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;



import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;


@Entity
@Table(name="LIBERA_AWB_COMPLEMENTAR")
@SequenceGenerator(name="LIBERA_AWB_COMPLEMENTAR_SQ", sequenceName="LIBERA_AWB_COMPLEMENTAR_SQ", allocationSize=1)
public class LiberaAWBComplementar implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LIBERA_AWB_COMPLEMENTAR_SQ")
	@Column(name="ID_LIBERA_AWB_COMPLEMENTAR", nullable=false)
	private Long idLiberaAWBComplementar;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_AWB_ORIGINAL", nullable = false)
	private Awb awbOriginal;
	
	@Columns(columns = { @Column(name = "DH_LIBERACAO"), @Column(name = "DH_LIBERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhLiberacao;
	
	@Column(name="DS_SENHA ", length=8, nullable = false)
	private String dsSenha;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_SOLICITANTE", nullable = false)
	private Filial filialSolicitante;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_SOLICITANTE", nullable = false)
	private UsuarioLMS usuarioSolicitante; 
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_LIBERADOR", nullable = false)
	private UsuarioLMS usuarioLiberador; 
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_EMPRESA", nullable = false)
	private Empresa empresa;
	
	@Column(name="DS_MOTIVO ", length=500, nullable = true)
	private String dsMotivo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_AWB_COMPLEMENTAR", nullable = true)
	private Awb awbComplementar;

	public Long getIdLiberaAWBComplementar() {
		return idLiberaAWBComplementar;
	}

	public void setIdLiberaAWBComplementar(Long idLiberaAWBComplementar) {
		this.idLiberaAWBComplementar = idLiberaAWBComplementar;
	}

	public Awb getAwbOriginal() {
		return awbOriginal;
	}

	public void setAwbOriginal(Awb awbOriginal) {
		this.awbOriginal = awbOriginal;
	}

	public DateTime getDhLiberacao() {
		return dhLiberacao;
	}

	public void setDhLiberacao(DateTime dhLiberacao) {
		this.dhLiberacao = dhLiberacao;
	}

	public String getDsSenha() {
		return dsSenha;
	}

	public void setDsSenha(String dsSenha) {
		this.dsSenha = dsSenha;
	}

	public Filial getFilialSolicitante() {
		return filialSolicitante;
	}

	public void setFilialSolicitante(Filial filialSolicitante) {
		this.filialSolicitante = filialSolicitante;
	}

	public UsuarioLMS getUsuarioSolicitante() {
		return usuarioSolicitante;
	}

	public void setUsuarioSolicitante(UsuarioLMS usuarioSolicitante) {
		this.usuarioSolicitante = usuarioSolicitante;
	}

	public UsuarioLMS getUsuarioLiberador() {
		return usuarioLiberador;
	}

	public void setUsuarioLiberador(UsuarioLMS usuarioLiberador) {
		this.usuarioLiberador = usuarioLiberador;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getDsMotivo() {
		return dsMotivo;
	}

	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}

	public Awb getAwbComplementar() {
		return awbComplementar;
	}

	public void setAwbComplementar(Awb awbComplementar) {
		this.awbComplementar = awbComplementar;
	}


}

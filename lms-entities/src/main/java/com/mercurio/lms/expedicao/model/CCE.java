package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "controle_conf_elet")
@SequenceGenerator(name = "controle_conf_elet_sq", sequenceName = "controle_conf_elet_sq", allocationSize = 1)
public class CCE implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Columns(columns = { @Column(name = "DH_EMISSAO", nullable = false),
		@Column(name = "DH_EMISSAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEmissao;

	@Id
	@Column(name="ID_CONTROLE_CONF_ELET", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "controle_conf_elet_sq")
	private Long idCCE;
	
	
	@Column(name="BL_DIVERGENCIA", nullable=false)
	private String blDivergencia; 
	
	@Column(name="ID_USUARIO_INCLUSAO", nullable=false)
	private Long idUsuarioInclusao;
	
	
	@OneToMany
	private List<CCEItem> itens;

	public Long getIdCCE() {
		return idCCE;
	}

	public void setIdCCE(Long idCCE) {
		this.idCCE = idCCE;
	}

	public List<CCEItem> getItens() {
		return itens;
	}

	public void setItens(List<CCEItem> itens) {
		this.itens = itens;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public String getBlDivergencia() {
		return blDivergencia;
	}

	public void setBlDivergencia(String blDivergencia) {
		this.blDivergencia = blDivergencia;
	}

	public Long getIdUsuarioInclusao() {
		return idUsuarioInclusao;
	}

	public void setIdUsuarioInclusao(Long idUsuarioInclusao) {
		this.idUsuarioInclusao = idUsuarioInclusao;
	}
	
}

package com.mercurio.lms.sim.model;

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

@Entity
@Table(name="EMAIL_CCT")
@SequenceGenerator(name="SQ_EMAIL_CCT", sequenceName="EMAIL_CCT_SQ", allocationSize=1)
public class EmailCCT implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_EMAIL_CCT")
	@Column(name="ID_EMAIL_CCT", nullable=false)
	private Long idEmailCCT;
	
	@ManyToOne
	@JoinColumn(name="ID_CONTATO_CCT", nullable=false)
	private ContatoCCT contatoCCT;
	
	@Column(name="DS_EMAIL", length=100, nullable=false)
	private String dsEmail;

	public Long getIdEmailCCT() {
		return idEmailCCT;
	}

	public void setIdEmailCCT(Long idEmailCCT) {
		this.idEmailCCT = idEmailCCT;
	}

	public ContatoCCT getContatoCCT() {
		return contatoCCT;
	}

	public void setContatoCCT(ContatoCCT contatoCCT) {
		this.contatoCCT = contatoCCT;
	}

	public String getDsEmail() {
		return dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}

package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author JonasFE
 *
 */
@Entity
@Table(name="TBDATABASEINPUT_CTE")
@SequenceGenerator(name = "TBDATABASEINPUT_CTE_SEQ", sequenceName = "TBDATABASEINPUT_CTE_SEQ", allocationSize=1)
public class TBDatabaseInputCTE implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBDATABASEINPUT_CTE_SEQ")
	private Long id;
	
	@Column(name="JOB", length=30, nullable=true)
	private String job;
	
	@Column(name="DOCUMENTUSER", length=30, nullable=true)
	private String documentUser;
	
	@Column(name="STATUS", length=5, nullable=false)
	private Integer status;
	
	@Column(name="KIND", length=5, nullable=false)
	private Integer kind;
	
	@Lob
	@Column(name="DOCUMENTDATA", nullable = false)
	private byte[] documentData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getDocumentUser() {
		return documentUser;
	}

	public void setDocumentUser(String documentUser) {
		this.documentUser = documentUser;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public byte[] getDocumentData() {
		return documentData;
	}

	public void setDocumentData(byte[] documentData) {
		this.documentData = documentData;
	}
	}

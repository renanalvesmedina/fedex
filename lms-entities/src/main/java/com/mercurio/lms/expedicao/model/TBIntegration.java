package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="TBINTEGRATION")
@SequenceGenerator(name = "TBINTEGRATION_SEQ", sequenceName = "TBINTEGRATION_SEQ", allocationSize=1)
public class TBIntegration implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID", nullable=false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TBINTEGRATION_SEQ")
    private Long id;

    @Column(name="DOCSTATUS")
    private Integer docStatus;
    
    @Column(name="JOBKEY")
    private Long jobKey;

    @Column(name="RPSNUMBER")
    private Long rpsNumber;
    
    
    @Column(name="RPSSERIES")
    private String rpsSeries;
    
    
    @Column(name="RPSKIND")
    private Integer rpsKind;
    
    @Column(name="CODIBGE")
    private Integer codIbge;
    
    @Column(name="DOCKIND")
    private Integer docKind;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(Integer docStatus) {
		this.docStatus = docStatus;
	}

	public Long getJobKey() {
		return jobKey;
	}

	public void setJobKey(Long jobKey) {
		this.jobKey = jobKey;
	}

	public Long getRpsNumber() {
		return rpsNumber;
	}

	public void setRpsNumber(Long rpsNumber) {
		this.rpsNumber = rpsNumber;
	}

	public String getRpsSeries() {
		return rpsSeries;
	}

	public void setRpsSeries(String rpsSeries) {
		this.rpsSeries = rpsSeries;
	}

	public Integer getRpsKind() {
		return rpsKind;
	}

	public void setRpsKind(Integer rpsKind) {
		this.rpsKind = rpsKind;
	}

	public Integer getCodIbge() {
		return codIbge;
	}

	public void setCodIbge(Integer codIbge) {
		this.codIbge = codIbge;
	}

	public Integer getDocKind() {
		return docKind;
	}

	public void setDocKind(Integer docKind) {
		this.docKind = docKind;
	}
    
}

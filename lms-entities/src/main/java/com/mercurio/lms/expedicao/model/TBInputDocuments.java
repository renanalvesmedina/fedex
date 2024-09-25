package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.joda.time.DateTime;

public class TBInputDocuments implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long jobKey;

    private DateTime insertDate;
    
    private Integer docStatus;
    
    private Integer docKind;
    
    private byte[] docData;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobKey() {
        return jobKey;
    }

    public void setJobKey(Long jobKey) {
        this.jobKey = jobKey;
    }

    public DateTime getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(DateTime insertDate) {
        this.insertDate = insertDate;
    }

    public Integer getDocStatus() {
        return docStatus;
    }

    public void setDocStatus(Integer docStatus) {
        this.docStatus = docStatus;
    }

    public Integer getDocKind() {
        return docKind;
    }

    public void setDocKind(Integer docKind) {
        this.docKind = docKind;
    }

    public byte[] getDocData() {
        return docData;
    }

    public void setDocData(byte[] docData) {
        this.docData = docData;
    }
    
}

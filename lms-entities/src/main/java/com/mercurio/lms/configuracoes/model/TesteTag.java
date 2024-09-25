package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author Hibernate CodeGenerator */
public class TesteTag implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTesteTag;

    /** persistent field */
    private String dsTesteTag;

    /** persistent field */
    private String nmTesteTag;

    /** persistent field */
    private Timestamp dhTesteTag;
    
    /** persistent field */
    private Time hrTesteTag;

    /** persistent field */
    private DomainValue tpSituacao;

    public Long getIdTesteTag() {
        return this.idTesteTag;
    }

    public void setIdTesteTag(Long idTesteTag) {
        this.idTesteTag = idTesteTag;
    }

    public String getDsTesteTag() {
        return this.dsTesteTag;
    }

    public void setDsTesteTag(String dsTesteTag) {
        this.dsTesteTag = dsTesteTag;
    }

    public String getNmTesteTag() {
        return this.nmTesteTag;
    }

    public void setNmTesteTag(String nmTesteTag) {
        this.nmTesteTag = nmTesteTag;
    }

    public Timestamp getDhTesteTag() {
        return this.dhTesteTag;
    }

    public void setDhTesteTag(Timestamp dhTesteTag) {
        this.dhTesteTag = dhTesteTag;
    }

    public Time getHrTesteTag() {
        return this.hrTesteTag;
    }

    public void setHrTesteTag(Time hrTesteTag) {
        this.hrTesteTag = hrTesteTag;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTesteTag", getIdTesteTag())
            .append("dsTesteTag", getDsTesteTag())
            .append("nmTesteTag", getNmTesteTag())
            .append("dhTesteTag", getDhTesteTag())
            .append("hrTesteTag", getHrTesteTag())
				.append("tpSituacao", getTpSituacao()).toString();
    }

}

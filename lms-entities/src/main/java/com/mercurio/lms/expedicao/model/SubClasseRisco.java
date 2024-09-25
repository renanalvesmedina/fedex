package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="SUBCLASSE_RISCO")
@SequenceGenerator(name="SUBCLASSE_RISCO_SEQ", sequenceName="SUBCLASSE_RISCO_SQ", allocationSize=1)
public class SubClasseRisco implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUBCLASSE_RISCO_SEQ")
    @Column(name = "ID_SUBCLASSE_RISCO", nullable = false)
	private Long idSubClasseRisco;
	
	@Column(name="NR_SUBCLASSE_RISCO", length=10, nullable = false)
	private String nrSubClasseRisco;
	
	@Column(name="DS_SUBCLASSE_RISCO", length=360, nullable = false)
	private String dsSubClasseRisco;
	
	@Column(name = "TP_SITUACAO", length = 1)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_CLASSE_RISCO", nullable = false)
	private ClasseRisco classeRisco;
	
    public Long getIdSubClasseRisco() {
        return idSubClasseRisco;
    }

    public void setIdSubClasseRisco(Long idSubClasseRisco) {
        this.idSubClasseRisco = idSubClasseRisco;
    }

    public String getNrSubClasseRisco() {
        return nrSubClasseRisco;
    }

    public void setNrSubClasseRisco(String nrSubClasseRisco) {
        this.nrSubClasseRisco = nrSubClasseRisco;
    }

    public String getDsSubClasseRisco() {
        return dsSubClasseRisco;
    }

    public void setDsSubClasseRisco(String dsSubClasseRisco) {
        this.dsSubClasseRisco = dsSubClasseRisco;
    }

    public DomainValue getTpSituacao() {
        return tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public ClasseRisco getClasseRisco() {
        return classeRisco;
    }

    public void setClasseRisco(ClasseRisco classeRisco) {
        this.classeRisco = classeRisco;
    }
    
}

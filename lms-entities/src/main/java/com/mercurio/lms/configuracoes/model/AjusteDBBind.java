package com.mercurio.lms.configuracoes.model;

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

import com.mercurio.lms.configuracoes.enums.AjusteDBTipoBind;

@Entity
@Table(name = "AJUSTE_DB_BIND")
public class AjusteDBBind implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idAjusteDBBind;
    private AjusteDB ajusteDB;
    private AjusteDBTipoBind nrTipo;
    private String nmBind;

    @Id
    @SequenceGenerator(name = "AJUSTE_DB_BIND_SQ", sequenceName = "AJUSTE_DB_BIND_SQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AJUSTE_DB_BIND_SQ")
    @Column(name = "ID_AJUSTE_DB_BIND", nullable = false)
    public Long getIdAjusteDBBind() {
        return idAjusteDBBind;
    }

    public void setIdAjusteDBBind(Long idAjusteDBBind) {
        this.idAjusteDBBind = idAjusteDBBind;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ID_AJUSTE_DB", nullable = false)
    public AjusteDB getAjusteDB() {
        return ajusteDB;
    }

    public void setAjusteDB(AjusteDB ajusteDB) {
        this.ajusteDB = ajusteDB;
    }

    @Column(name = "NR_TIPO", nullable = false)
    public AjusteDBTipoBind getNrTipo() {
        return nrTipo;
    }

    public void setNrTipo(AjusteDBTipoBind nrTipo) {
        this.nrTipo = nrTipo;
    }

    @Column(name = "NM_BIND", length = 100, nullable = false)
    public String getNmBind() {
        return nmBind;
    }

    public void setNmBind(String nmBind) {
        this.nmBind = nmBind;
    }

}

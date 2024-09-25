package com.mercurio.lms.edi.model;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "EDI_TABELA_OCOREN")
@SequenceGenerator(name = "EDI_TABELA_OCOREN_SQ", sequenceName = "EDI_TABELA_OCOREN_SQ", allocationSize=1)
public class EdiTabelaOcoren  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDI_TABELA_OCOREN_SQ")
    @Column(name = "ID_EDI_TABELA_OCOREN")
    private Long idEdiTabelaOcoren;

    @OneToMany(mappedBy = "ediTabelaOcoren", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    private List<EdiTabelaCliente> ediTabelaClientes = new ArrayList<>();

    @OneToMany(mappedBy = "ediTabelaOcoren", cascade = { CascadeType.MERGE, CascadeType.PERSIST})
    private List<EdiTabelaOcorenDet> ediTabelaOcorenDets = new ArrayList<>();

    @Column(name = "NM_TABELA_OCOREN", length = 50, nullable = false)
    private String nmTabelaOcoren;

    @Column(name = "DT_INSERT", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtInsert;

    @Column(name = "BL_WEBSERVICE", length = 1, nullable = false)
    @Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blWebservice;

    public Long getIdEdiTabelaOcoren() {
        return idEdiTabelaOcoren;
    }

    public void setIdEdiTabelaOcoren(Long idEdiTabelaOcoren) {
        this.idEdiTabelaOcoren = idEdiTabelaOcoren;
    }

    public List<EdiTabelaCliente> getEdiTabelaClientes() {
        return ediTabelaClientes;
    }

    public void setEdiTabelaClientes(List<EdiTabelaCliente> ediTabelaClientes) {
        this.ediTabelaClientes = ediTabelaClientes;
    }

    public List<EdiTabelaOcorenDet> getEdiTabelaOcorenDets() {
        return ediTabelaOcorenDets;
    }

    public void setEdiTabelaOcorenDets(List<EdiTabelaOcorenDet> ediTabelaOcorenDets) {
        this.ediTabelaOcorenDets = ediTabelaOcorenDets;
    }

    public String getNmTabelaOcoren() {
        return nmTabelaOcoren;
    }

    public void setNmTabelaOcoren(String nmTabelaOcoren) {
        this.nmTabelaOcoren = nmTabelaOcoren;
    }

    public Date getDtInsert() {
        return dtInsert;
    }

    public void setDtInsert(Date dtInsert) {
        this.dtInsert = dtInsert;
    }

    public Boolean getBlWebservice() {
        return blWebservice;
    }

    public void setBlWebservice(Boolean blWebservice) {
        this.blWebservice = blWebservice;
    }

    public void addEdiTabelaCliente(EdiTabelaCliente ediTabelaCliente){
        if (ediTabelaCliente == null){
            throw  new RuntimeException("Informe os dados para a tebela EdiTabelaCliente");
        }
        ediTabelaCliente.setEdiTabelaOcoren(this);
        this.ediTabelaClientes.add(ediTabelaCliente);
    }

    public void addEdiTabelaOcorenDet(EdiTabelaOcorenDet ediTabelaOcorenDet){
        if (ediTabelaOcorenDet == null){
            throw  new RuntimeException("Informe os dados para a tebela EdiTabelaOcorenDet");
        }

        ediTabelaOcorenDet.setEdiTabelaOcoren(this);
        this.ediTabelaOcorenDets.add(ediTabelaOcorenDet);
    }
}

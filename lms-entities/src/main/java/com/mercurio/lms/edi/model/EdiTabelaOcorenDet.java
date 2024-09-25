package com.mercurio.lms.edi.model;

import com.mercurio.adsm.framework.model.DomainValue;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EDI_TABELA_OCOREN_DET")
@SequenceGenerator(name = "EDI_TABELA_OCOREN_DET_SQ", sequenceName = "EDI_TABELA_OCOREN_DET_SQ", allocationSize=1)
public class EdiTabelaOcorenDet implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDI_TABELA_OCOREN_DET_SQ")
    @Column(name = "ID_EDI_TABELA_OCOREN_DET")
    private Long idEdiTabelaOcorenDet;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ID_EDI_TABELA_OCOREN", nullable = false)
    private EdiTabelaOcoren ediTabelaOcoren;

    @Column(name = "ID_LMS", nullable = false)
    private Long idLms;

    @Column(name = "CD_OCORRENCIA", nullable = false, length = 3)
    private String cdOcorrencia;

    @Column(name = "CD_OCORRENCIA_CLIENTE", nullable = false, length = 5)
    private String cdOcorrenciaCliente;

    @Column(name = "FL_OCORRENCIA", nullable = false, length = 1)
    private String flOcorrencia;

    @Column(name = "TP_OCORRENCIA", nullable = false, length = 1)
    @Type(
            type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
            parameters = { @Parameter(name = "domainName", value = "DM_EDI_TABELA_TP_OCORRENCIA") })
    private DomainValue tpOcorrencia;

    @Column(name = "DS_OCORRENCIA", nullable = false, length = 1)
    private String dsOcorrencia;

    @Column(name = "CD_WS_BDE", length = 50)
    private String cdWsDde;

    @Column(name = "CD_WS_EXCECAO", length = 3)
    private String cdWsExcecao;

    @Column(name = "DS_WS_EXCECAO", length = 3)
    private String dsWsExcecao;

    public Long getIdEdiTabelaOcorenDet() {
        return idEdiTabelaOcorenDet;
    }

    public void setIdEdiTabelaOcorenDet(Long idEdiTabelaOcorenDet) {
        this.idEdiTabelaOcorenDet = idEdiTabelaOcorenDet;
    }

    public EdiTabelaOcoren getEdiTabelaOcoren() {
        return ediTabelaOcoren;
    }

    public void setEdiTabelaOcoren(EdiTabelaOcoren ediTabelaOcoren) {
        this.ediTabelaOcoren = ediTabelaOcoren;
    }

    public Long getIdLms() {
        return idLms;
    }

    public void setIdLms(Long idLms) {
        this.idLms = idLms;
    }

    public String getCdOcorrencia() {
        return cdOcorrencia;
    }

    public void setCdOcorrencia(String cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    public String getCdOcorrenciaCliente() {
        return cdOcorrenciaCliente;
    }

    public void setCdOcorrenciaCliente(String cdOcorrenciaCliente) {
        this.cdOcorrenciaCliente = cdOcorrenciaCliente;
    }

    public String getFlOcorrencia() {
        return flOcorrencia;
    }

    public void setFlOcorrencia(String flOcorrencia) {
        this.flOcorrencia = flOcorrencia;
    }

    public DomainValue getTpOcorrencia() {
        return tpOcorrencia;
    }

    public void setTpOcorrencia(DomainValue tpOcorrencia) {
        this.tpOcorrencia = tpOcorrencia;
    }

    public String getDsOcorrencia() {
        return dsOcorrencia;
    }

    public void setDsOcorrencia(String dsOcorrencia) {
        this.dsOcorrencia = dsOcorrencia;
    }

    public String getCdWsDde() {
        return cdWsDde;
    }

    public void setCdWsDde(String cdWsDde) {
        this.cdWsDde = cdWsDde;
    }

    public String getCdWsExcecao() {
        return cdWsExcecao;
    }

    public void setCdWsExcecao(String cdWsExcecao) {
        this.cdWsExcecao = cdWsExcecao;
    }

    public String getDsWsExcecao() {
        return dsWsExcecao;
    }

    public void setDsWsExcecao(String dsWsExcecao) {
        this.dsWsExcecao = dsWsExcecao;
    }
}

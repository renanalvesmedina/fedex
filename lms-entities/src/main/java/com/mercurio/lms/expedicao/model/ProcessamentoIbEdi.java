package com.mercurio.lms.expedicao.model;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROCESSAMENTO_FILA_IB_EDI")
@SequenceGenerator(name = "PROCESSAMENTO_FILA_IB_EDI_SEQ", sequenceName = "PROCESSAMENTO_FILA_IB_EDI_SQ", allocationSize = 1)
public class ProcessamentoIbEdi implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProcessamentoFilaIbEdi;
    private Long nrProcessamento;
    private String dsDados;
    private DateTime dhInclusao;
    private DateTime dhEnvio;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSAMENTO_FILA_IB_EDI_SEQ")
    @Column(name = "ID_PROCESSAMENTO_FILA_IB_EDI", nullable = false)
    public Long getIdProcessamentoFilaIbEdi() {
        return idProcessamentoFilaIbEdi;
    }

    public void setIdProcessamentoFilaIbEdi(Long idProcessamentoFilaIbEdi) {
        this.idProcessamentoFilaIbEdi = idProcessamentoFilaIbEdi;
    }

    @Column(name = "NR_PROCESSAMENTO", nullable = false)
    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    @Column(name = "DS_DADOS", nullable = false)
    public String getDsDados() {
        return dsDados;
    }

    public void setDsDados(String dsDados) {
        this.dsDados = dsDados;
    }


    @Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    @Columns(columns = { @Column(name = "DH_ENVIO"), @Column(name = "DH_ENVIO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    public DateTime getDhEnvio() {
        return dhEnvio;
    }

    public void setDhEnvio(DateTime dhEnvio) {
        this.dhEnvio = dhEnvio;
    }
}

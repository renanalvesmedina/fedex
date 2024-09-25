package com.mercurio.lms.expedicao.model;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROCESSO_FILA_IB_AGRUPAR")
@SequenceGenerator(name = "PROCESSO_FILA_IB_AGRUPAR_SQ", sequenceName = "PROCESSO_FILA_IB_AGRUPAR_SQ", allocationSize = 1)
public class ProcessamentoFilaIbAgrupar  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idProcessamentoFilaIbAgrupar;
    private Long nrProcessamento;
    private String dsParamAgruparNotaFiscal;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSO_FILA_IB_AGRUPAR_SQ")
    @Column(name = "ID_PROCESSO_FILA_IB_AGRUPAR", nullable = false)
    public Long getIdProcessamentoFilaIbAgrupar() {
        return idProcessamentoFilaIbAgrupar;
    }

    public void setIdProcessamentoFilaIbAgrupar(Long idProcessamentoFilaIbAgrupar) {
        this.idProcessamentoFilaIbAgrupar = idProcessamentoFilaIbAgrupar;
    }

    @Column(name="NR_PROCESSAMENTO", nullable=false)
    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    @Column(name = "DS_PARAM_AGRUPAR_NOTA_FISCAL", nullable=false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.CLobUserType")
    public String getDsParamAgruparNotaFiscal() {
        return dsParamAgruparNotaFiscal;
    }

    public void setDsParamAgruparNotaFiscal(String dsParamAgruparNotaFiscal) {
        this.dsParamAgruparNotaFiscal = dsParamAgruparNotaFiscal;
    }
}

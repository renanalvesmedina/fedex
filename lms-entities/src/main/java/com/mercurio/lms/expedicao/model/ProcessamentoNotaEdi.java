package com.mercurio.lms.expedicao.model;

import com.mercurio.lms.vendas.model.Cliente;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PROCESSAMENTO_NOTA_EDI")
@SequenceGenerator(name = "PROCESSAMENTO_NOTA_EDI_SEQ", sequenceName = "PROCESSAMENTO_NOTA_EDI_SQ", allocationSize=1)
public class ProcessamentoNotaEdi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCESSAMENTO_NOTA_EDI_SEQ")
    @Column(name = "ID_PROCESSAMENTO_NOTA_EDI", nullable = false)
    private Long idProcessamentoNotaEdi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PROCESSAMENTO_EDI", nullable = false)
    private ProcessamentoEdi processamentoEdi;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE_REMETENTE", nullable = false)
    private Cliente clienteRemetente;

    @Column(name = "NR_NOTA_FISCAL", nullable = false)
    private Long nrNotaFiscal;

    @Column(name = "DS_MENSAGEM_ERRO")
    private String dsMensagemErro;

    @Column(name = "BL_PROCESSADA")
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blProcessada;

    @Column(name = "NR_PROCESSAMENTO")
    private String nrProcessamento;

    @Column(name = "NR_INDEX")
    private Long nrIndex;

    public Long getIdProcessamentoNotaEdi() {
        return idProcessamentoNotaEdi;
    }

    public void setIdProcessamentoNotaEdi(Long idProcessamentoNotaEdi) {
        this.idProcessamentoNotaEdi = idProcessamentoNotaEdi;
    }

    public ProcessamentoEdi getProcessamentoEdi() {
        return processamentoEdi;
    }

    public void setProcessamentoEdi(ProcessamentoEdi processamentoEdi) {
        this.processamentoEdi = processamentoEdi;
    }

    public Cliente getClienteRemetente() {
        return clienteRemetente;
    }

    public void setClienteRemetente(Cliente clienteRemetente) {
        this.clienteRemetente = clienteRemetente;
    }

    public String getDsMensagemErro() {
        return dsMensagemErro;
    }

    public void setDsMensagemErro(String dsMensagemErro) {
        this.dsMensagemErro = dsMensagemErro;
    }

    public Boolean getBlProcessada() {
        return blProcessada;
    }

    public void setBlProcessada(Boolean blProcessada) {
        this.blProcessada = blProcessada;
    }

    public Long getNrNotaFiscal() {
        return nrNotaFiscal;
    }

    public void setNrNotaFiscal(Long nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public String getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(String nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getNrIndex() {
        return nrIndex;
    }

    public void setNrIndex(Long nrIndex) {
        this.nrIndex = nrIndex;
    }
}

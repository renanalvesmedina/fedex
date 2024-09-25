package com.mercurio.lms.edi.model;
import com.mercurio.lms.configuracoes.model.Pessoa;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "EDI_TABELA_CLIENTE")
@SequenceGenerator(name = "EDI_TABELA_CLIENTE_SQ", sequenceName = "EDI_TABELA_CLIENTE_SQ", allocationSize=1)
public class EdiTabelaCliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EDI_TABELA_CLIENTE_SQ")
    @Column(name = "ID_EDI_TABELA_CLIENTE")
    private Long idEdiTabelaCliente;

    @ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "ID_EDI_TABELA_OCOREN", nullable = false)
    private EdiTabelaOcoren ediTabelaOcoren;

    @Column(name = "ID_CLIENTE", nullable = false)
    private Long idCliente;

    public Long getIdEdiTabelaCliente() {
        return idEdiTabelaCliente;
    }

    public void setIdEdiTabelaCliente(Long idEdiTabelaCliente) {
        this.idEdiTabelaCliente = idEdiTabelaCliente;
    }

    public EdiTabelaOcoren getEdiTabelaOcoren() {
        return ediTabelaOcoren;
    }

    public void setEdiTabelaOcoren(EdiTabelaOcoren ediTabelaOcoren) {
        this.ediTabelaOcoren = ediTabelaOcoren;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
}

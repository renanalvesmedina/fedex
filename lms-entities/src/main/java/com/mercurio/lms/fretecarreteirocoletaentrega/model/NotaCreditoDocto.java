package com.mercurio.lms.fretecarreteirocoletaentrega.model;

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

import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name = "NOTA_CREDITO_DOCTO")
@SequenceGenerator(name = "NOTA_CREDITO_DOCTO_SQ", sequenceName = "NOTA_CREDITO_DOCTO_SQ")
public class NotaCreditoDocto extends NotaCreditoDocumentoFrete {

    private static final long serialVersionUID = 1L;

    private Long idNotaCreditoDocto;
    private ManifestoEntrega manifestoEntrega;
    private DoctoServico doctoServico;

    public NotaCreditoDocto() {
    }

    public NotaCreditoDocto(Long idNotaCreditoDocto, ManifestoEntrega manifestoEntrega, DoctoServico doctoServico) {
        this.idNotaCreditoDocto = idNotaCreditoDocto;
        this.manifestoEntrega = manifestoEntrega;
        this.doctoServico = doctoServico;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_CREDITO_DOCTO_SQ")
    @Column(name = "ID_NOTA_CREDITO_DOCTO", nullable = false)
    public Long getIdNotaCreditoDocto() {
        return idNotaCreditoDocto;
    }

    public void setIdNotaCreditoDocto(Long idNotaCreditoDocto) {
        this.idNotaCreditoDocto = idNotaCreditoDocto;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MANIFESTO_ENTREGA", nullable = true)
    public ManifestoEntrega getManifestoEntrega() {
        return manifestoEntrega;
    }

    public void setManifestoEntrega(ManifestoEntrega manifestoEntrega) {
        this.manifestoEntrega = manifestoEntrega;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
    public DoctoServico getDoctoServico() {
        return doctoServico;
    }

    public void setDoctoServico(DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((doctoServico == null) ? 0 : doctoServico.hashCode());
        result = prime * result + ((manifestoEntrega == null) ? 0 : manifestoEntrega.hashCode());
        result = prime * result + ((getNotaCredito() == null) ? 0 : getNotaCredito().hashCode());
        result = prime * result + ((getTabelaColetaEntrega() == null) ? 0 : getTabelaColetaEntrega().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotaCreditoDocto other = (NotaCreditoDocto) obj;
        if (doctoServico == null) {
            if (other.doctoServico != null)
                return false;
        } else if (!doctoServico.equals(other.doctoServico))
            return false;
        if (manifestoEntrega == null) {
            if (other.manifestoEntrega != null)
                return false;
        } else if (!manifestoEntrega.equals(other.manifestoEntrega))
            return false;
        if (getNotaCredito() == null) {
            if (other.getNotaCredito() != null)
                return false;
        } else if (!getNotaCredito().equals(other.getNotaCredito()))
            return false;
        if (getTabelaColetaEntrega() == null) {
            if (other.getTabelaColetaEntrega() != null)
                return false;
        } else if (!getTabelaColetaEntrega().equals(other.getTabelaColetaEntrega()))
            return false;
        return true;
    }

}

package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

/**
 * @author LMS Custom Hibernate CodeGenerator
 */
@Entity
@Table(name = "NOVO_DPE_DOCTO_SERVICO")
@SequenceGenerator(name = "NOVO_DPE_DOCTO_SERVICO_SQ", sequenceName="NOVO_DPE_DOCTO_SERVICO_SQ", allocationSize = 1)
public class NovoDpeDoctoServico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_NOVO_DPE_DOCTO_SERVICO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOVO_DPE_DOCTO_SERVICO_SQ")
    private Long idNovoDpeDoctoServico;

    @Column(name = "ID_DOCTO_SERVICO", nullable = false)
    private Long idDoctoServico;

    @Column(name = "NOVO_DT_PREV_ENTREGA", nullable = true)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
    private YearMonthDay novoDtPrevEntrega;

    @Column(name="BL_ATENDIDO",length=1)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
    private Boolean blAtendido;

    public Long getIdNovoDpeDoctoServico() {
        return idNovoDpeDoctoServico;
    }

    public void setIdNovoDpeDoctoServico(Long idNovoDpeDoctoServico) {
        this.idNovoDpeDoctoServico = idNovoDpeDoctoServico;
    }

    public Long getIdDoctoServico() {
        return idDoctoServico;
    }

    public void setIdDoctoServico(Long idDoctoServico) {
        this.idDoctoServico = idDoctoServico;
    }

    public YearMonthDay getNovoDtPrevEntrega() {
        return novoDtPrevEntrega;
    }

    public void setNovoDtPrevEntrega(YearMonthDay novoDtPrevEntrega) {
        this.novoDtPrevEntrega = novoDtPrevEntrega;
    }

    public Boolean getBlAtendido() {
        return blAtendido;
    }

    public void setBlAtendido(Boolean blAtendido) {
        this.blAtendido = blAtendido;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.idNovoDpeDoctoServico != null ? this.idNovoDpeDoctoServico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NovoDpeDoctoServico other = (NovoDpeDoctoServico) obj;
        if (this.idNovoDpeDoctoServico != other.idNovoDpeDoctoServico && (this.idNovoDpeDoctoServico == null || !this.idNovoDpeDoctoServico.equals(other.idNovoDpeDoctoServico))) {
            return false;
        }
        return true;
    }

}

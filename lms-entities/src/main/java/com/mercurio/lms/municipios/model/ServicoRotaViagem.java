package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoRotaViagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoRotaViagem;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaViagem rotaViagem;

    public Long getIdServicoRotaViagem() {
        return this.idServicoRotaViagem;
    }

    public void setIdServicoRotaViagem(Long idServicoRotaViagem) {
        this.idServicoRotaViagem = idServicoRotaViagem;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.RotaViagem getRotaViagem() {
        return this.rotaViagem;
    }

	public void setRotaViagem(
			com.mercurio.lms.municipios.model.RotaViagem rotaViagem) {
        this.rotaViagem = rotaViagem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoRotaViagem",
				getIdServicoRotaViagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoRotaViagem))
			return false;
        ServicoRotaViagem castOther = (ServicoRotaViagem) other;
		return new EqualsBuilder().append(this.getIdServicoRotaViagem(),
				castOther.getIdServicoRotaViagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoRotaViagem())
            .toHashCode();
    }

}

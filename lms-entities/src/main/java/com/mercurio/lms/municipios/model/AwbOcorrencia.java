package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class AwbOcorrencia implements Serializable, Vigencia {
	
	private static final long serialVersionUID = 1L;

	private Long codigo;
	
	private String descricao;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AwbOcorrencia))
			return false;
        AwbOcorrencia castOther = (AwbOcorrencia) other;
		return new EqualsBuilder().append(this.getCodigo(),
				castOther.getCodigo()).isEquals();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return null;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return null;
	}

}

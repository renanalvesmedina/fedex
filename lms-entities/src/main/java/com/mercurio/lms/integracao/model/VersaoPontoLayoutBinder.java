package com.mercurio.lms.integracao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsmmanager.integracao.model.GrupoLayoutBinder;
import com.mercurio.adsmmanager.integracao.model.PontoLayoutBinder;
import com.mercurio.lms.configuracoes.model.ParametroFilial;

/**
 * Pojo que representa o vinculo entre versos implantadas das filiais com um
 * determinado ponto e layout binder.
 * 
 * @author Diego Pacheco
 * @version 1.0
 */
public class VersaoPontoLayoutBinder implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long idVersaoPontoLayoutBinder;
	private ParametroFilial parametroFilialInicio;
	private ParametroFilial parametroFilialFim;
	private PontoLayoutBinder pontoLayoutBinder;
	private GrupoLayoutBinder grupoLayoutBinder;
	
	public VersaoPontoLayoutBinder() {
	}
	
	public VersaoPontoLayoutBinder(Long idVersaoPontoLayoutBinder,
			ParametroFilial conteudoParametroFilialInicio,
			ParametroFilial conteudoParametroFilialFim,
			PontoLayoutBinder pontoLayoutBinder,
			GrupoLayoutBinder grupoLayoutBinder) {
		super();
		this.idVersaoPontoLayoutBinder = idVersaoPontoLayoutBinder;
		this.pontoLayoutBinder = pontoLayoutBinder;
		this.parametroFilialInicio = conteudoParametroFilialInicio;
		this.parametroFilialFim = conteudoParametroFilialFim;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idVersaoPontoLayoutBinder",
				getIdVersaoPontoLayoutBinder()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VersaoPontoLayoutBinder))
			return false;
        VersaoPontoLayoutBinder castOther = (VersaoPontoLayoutBinder) other;
		return new EqualsBuilder().append(this.getIdVersaoPontoLayoutBinder(),
				castOther.getIdVersaoPontoLayoutBinder()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdVersaoPontoLayoutBinder())
	        .toHashCode();
	}
	
	public Long getIdVersaoPontoLayoutBinder() {
		return idVersaoPontoLayoutBinder;
	}

	public void setIdVersaoPontoLayoutBinder(Long idVersaoPontoLayoutBinder) {
		this.idVersaoPontoLayoutBinder = idVersaoPontoLayoutBinder;
	}

	public ParametroFilial getParametroFilialInicio() {
		return parametroFilialInicio;
	}

	public void setParametroFilialInicio(ParametroFilial parametroFilialInicio) {
		this.parametroFilialInicio = parametroFilialInicio;
	}

	public ParametroFilial getParametroFilialFim() {
		return parametroFilialFim;
	}

	public void setParametroFilialFim(ParametroFilial parametroFilialFim) {
		this.parametroFilialFim = parametroFilialFim;
	}

	public GrupoLayoutBinder getGrupoLayoutBinder() {
		return grupoLayoutBinder;
	}

	public void setGrupoLayoutBinder(GrupoLayoutBinder grupoLayoutBinder) {
		this.grupoLayoutBinder = grupoLayoutBinder;
	}

	public PontoLayoutBinder getPontoLayoutBinder() {
		return pontoLayoutBinder;
	}

	public void setPontoLayoutBinder(PontoLayoutBinder pontoLayoutBinder) {
		this.pontoLayoutBinder = pontoLayoutBinder;
	}

}

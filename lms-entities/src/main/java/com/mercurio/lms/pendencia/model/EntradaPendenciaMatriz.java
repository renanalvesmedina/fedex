package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class EntradaPendenciaMatriz implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEntradaPendenciaMatriz;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private Long nrVolume;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.UnidadeProduto unidadeProduto;
    
    /** persistent field */
    private com.mercurio.lms.pendencia.model.EnderecoArmazem enderecoArmazem;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;

    /** persistent field */
    private List mercadoriaPendenciaMzs;
    
    public Long getIdEntradaPendenciaMatriz() {
        return this.idEntradaPendenciaMatriz;
    }

    public void setIdEntradaPendenciaMatriz(Long idEntradaPendenciaMatriz) {
        this.idEntradaPendenciaMatriz = idEntradaPendenciaMatriz;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

	public Long getNrVolume() {
		return nrVolume;
	}

	public void setNrVolume(Long nrVolume) {
		this.nrVolume = nrVolume;
	}

    public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacao() {
		return dispositivoUnitizacao;
	}

	public void setDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao) {
		this.dispositivoUnitizacao = dispositivoUnitizacao;
	}

	public com.mercurio.lms.pendencia.model.EnderecoArmazem getEnderecoArmazem() {
		return enderecoArmazem;
	}

	public void setEnderecoArmazem(
			com.mercurio.lms.pendencia.model.EnderecoArmazem enderecoArmazem) {
		this.enderecoArmazem = enderecoArmazem;
	}
	
	public com.mercurio.lms.pendencia.model.UnidadeProduto getUnidadeProduto() {
		return unidadeProduto;
	}

	public void setUnidadeProduto(
			com.mercurio.lms.pendencia.model.UnidadeProduto unidadeProduto) {
		this.unidadeProduto = unidadeProduto;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzs() {
        return this.mercadoriaPendenciaMzs;
    }

    public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
        this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEntradaPendenciaMatriz",
				getIdEntradaPendenciaMatriz()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EntradaPendenciaMatriz))
			return false;
        EntradaPendenciaMatriz castOther = (EntradaPendenciaMatriz) other;
		return new EqualsBuilder().append(this.getIdEntradaPendenciaMatriz(),
				castOther.getIdEntradaPendenciaMatriz()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEntradaPendenciaMatriz())
            .toHashCode();
    }

}

package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoAvancado implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoAvancado;

    /** persistent field */
    private String dsPostoAvancado;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private String obPostoAvancado;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List postoAvancadoCcs;

    /** persistent field */
    private List carregamentoDescargas;

    public Long getIdPostoAvancado() {
        return this.idPostoAvancado;
    }

    public void setIdPostoAvancado(Long idPostoAvancado) {
        this.idPostoAvancado = idPostoAvancado;
    }

    public String getNmPostoAvancado() {
        return this.dsPostoAvancado;
    }

    public void setNmPostoAvancado(String dsPostoAvancado) {
        this.dsPostoAvancado = dsPostoAvancado;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public String getObPostoAvancado() {
        return this.obPostoAvancado;
    }

    public void setObPostoAvancado(String obPostoAvancado) {
        this.obPostoAvancado = obPostoAvancado;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoAvancadoCc.class)     
    public List getPostoAvancadoCcs() {
        return this.postoAvancadoCcs;
    }

    public void setPostoAvancadoCcs(List postoAvancadoCcs) {
        this.postoAvancadoCcs = postoAvancadoCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoDescarga.class)     
    public List getCarregamentoDescargas() {
        return this.carregamentoDescargas;
    }

    public void setCarregamentoDescargas(List carregamentoDescargas) {
        this.carregamentoDescargas = carregamentoDescargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoAvancado",
				getIdPostoAvancado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoAvancado))
			return false;
        PostoAvancado castOther = (PostoAvancado) other;
		return new EqualsBuilder().append(this.getIdPostoAvancado(),
				castOther.getIdPostoAvancado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoAvancado()).toHashCode();
    }

	/**
	 * @return Returns the dsPostoAvancado.
	 */
	public String getDsPostoAvancado() {
		return dsPostoAvancado;
	}

	/**
	 * @param dsPostoAvancado
	 *            The dsPostoAvancado to set.
	 */
	public void setDsPostoAvancado(String dsPostoAvancado) {
		this.dsPostoAvancado = dsPostoAvancado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}

package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class BloqueioMotoristaProp implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBloqueioMotoristaProp;

    /** persistent field */
    private String obBloqueioMotoristaProp;

    /** persistent field */
    private YearMonthDay dtRegistroBloqueio;

    private DateTime dhVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtRegistroDesbloqueio;

    private DateTime dhVigenciaFinal;
    
    /** nullable persistent field */
    private Boolean blBloqueiaViagem;
    
    /** nullable persistent field */
    private Boolean blControleCargaNovo;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncionarioDesbloqueio;
    
    /** persistent field */
    private Filial filialByIdFilialBloqueio;
    
    /** persistent field */
    private Filial filialByIdFilialDesbloqueado;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncionarioBloqueio;    

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    public BloqueioMotoristaProp() {
		blBloqueiaViagem = false;
		blControleCargaNovo = false;
	}
    
    public Long getIdBloqueioMotoristaProp() {
        return this.idBloqueioMotoristaProp;
    }

    public void setIdBloqueioMotoristaProp(Long idBloqueioMotoristaProp) {
        this.idBloqueioMotoristaProp = idBloqueioMotoristaProp;
    }

    public String getObBloqueioMotoristaProp() {
        return this.obBloqueioMotoristaProp;
    }

    public void setObBloqueioMotoristaProp(String obBloqueioMotoristaProp) {
        this.obBloqueioMotoristaProp = obBloqueioMotoristaProp;
    }

    public YearMonthDay getDtRegistroBloqueio() {
        return this.dtRegistroBloqueio;
    }

    public void setDtRegistroBloqueio(YearMonthDay dtRegistroBloqueio) {
        this.dtRegistroBloqueio = dtRegistroBloqueio;
    }

    public DateTime getDhVigenciaInicial() {
        return this.dhVigenciaInicial;
    }

    public void setDhVigenciaInicial(DateTime dhVigenciaInicial) {
        this.dhVigenciaInicial = dhVigenciaInicial;
    }

    public YearMonthDay getDtRegistroDesbloqueio() {
        return this.dtRegistroDesbloqueio;
    }

    public void setDtRegistroDesbloqueio(YearMonthDay dtRegistroDesbloqueio) {
        this.dtRegistroDesbloqueio = dtRegistroDesbloqueio;
    }

    public DateTime getDhVigenciaFinal() {
        return this.dhVigenciaFinal;
    }

    public void setDhVigenciaFinal(DateTime dhVigenciaFinal) {
        this.dhVigenciaFinal = dhVigenciaFinal;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncionarioDesbloqueio() {
        return this.usuarioByIdFuncionarioDesbloqueio;
    }

	public void setUsuarioByIdFuncionarioDesbloqueio(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncionarioDesbloqueio) {
        this.usuarioByIdFuncionarioDesbloqueio = usuarioByIdFuncionarioDesbloqueio;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdFuncionarioBloqueio() {
        return this.usuarioByIdFuncionarioBloqueio;
    }

	public void setUsuarioByIdFuncionarioBloqueio(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdFuncionarioBloqueio) {
        this.usuarioByIdFuncionarioBloqueio = usuarioByIdFuncionarioBloqueio;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBloqueioMotoristaProp",
				getIdBloqueioMotoristaProp()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BloqueioMotoristaProp))
			return false;
        BloqueioMotoristaProp castOther = (BloqueioMotoristaProp) other;
		return new EqualsBuilder().append(this.getIdBloqueioMotoristaProp(),
				castOther.getIdBloqueioMotoristaProp()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBloqueioMotoristaProp())
            .toHashCode();
    }

	public Filial getFilialByIdFilialBloqueio() {
		return filialByIdFilialBloqueio;
	}

	public void setFilialByIdFilialBloqueio(Filial filialByIdFilialBloqueio) {
		this.filialByIdFilialBloqueio = filialByIdFilialBloqueio;
	}

	public Filial getFilialByIdFilialDesbloqueado() {
		return filialByIdFilialDesbloqueado;
	}

	public void setFilialByIdFilialDesbloqueado(
			Filial filialByIdFilialDesbloqueado) {
		this.filialByIdFilialDesbloqueado = filialByIdFilialDesbloqueado;
	}
	
	public Boolean getBlBloqueiaViagem() {
		return blBloqueiaViagem;
	}

	public void setBlBloqueiaViagem(Boolean blBloqueiaViagem) {
		this.blBloqueiaViagem = blBloqueiaViagem;
	}

	public Boolean getBlControleCargaNovo() {
		return blControleCargaNovo;
	}

	public void setBlControleCargaNovo(Boolean blControleCargaNovo) {
		this.blControleCargaNovo = blControleCargaNovo;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}
}

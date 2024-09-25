package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaIntervaloCep implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaIntervaloCep;

    /** persistent field */
    private String nrCepInicial;

    /** persistent field */
    private String nrCepFinal;

    /** persistent field */
    private DomainValue tpGrauRisco;

    /** persistent field */
    private TimeOfDay hrCorteSolicitacao;

    /** nullable persistent field */
    private String dsBairro;

    /** nullable persistent field */
    private TimeOfDay hrCorteExecucao;

    private YearMonthDay dtVigenciaInicial;
    
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private Boolean blAtendimentoTemporario;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.TipoDificuldadeAcesso tipoDificuldadeAcesso;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    /** persistent field */
    private List horarioTransitos;
    
    /** nullable persistent field */
    private List pedidoColetas;
    
    private Short nrOrdemOperacao;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;
    
    public Long getIdRotaIntervaloCep() {
        return this.idRotaIntervaloCep;
    }

    public void setIdRotaIntervaloCep(Long idRotaIntervaloCep) {
        this.idRotaIntervaloCep = idRotaIntervaloCep;
    }

    public String getNrCepInicial() {
        return this.nrCepInicial;
    }

    public void setNrCepInicial(String nrCepInicial) {
        this.nrCepInicial = nrCepInicial;
    }

    public String getNrCepFinal() {
        return this.nrCepFinal;
    }

    public void setNrCepFinal(String nrCepFinal) {
        this.nrCepFinal = nrCepFinal;
    }

    public DomainValue getTpGrauRisco() {
        return this.tpGrauRisco;
    }

    public void setTpGrauRisco(DomainValue tpGrauRisco) {
        this.tpGrauRisco = tpGrauRisco;
    }

    public TimeOfDay getHrCorteSolicitacao() {
        return this.hrCorteSolicitacao;
    }

    public void setHrCorteSolicitacao(TimeOfDay hrCorteSolicitacao) {
        this.hrCorteSolicitacao = hrCorteSolicitacao;
    }

    public String getDsBairro() {
        return this.dsBairro;
    }

    public void setDsBairro(String dsBairro) {
        this.dsBairro = dsBairro;
    }

    public TimeOfDay getHrCorteExecucao() {
        return this.hrCorteExecucao;
    }

    public void setHrCorteExecucao(TimeOfDay hrCorteExecucao) {
        this.hrCorteExecucao = hrCorteExecucao;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

    public String getIntervaloCep(){
    	if (nrCepInicial != null && nrCepFinal != null)
    		return nrCepInicial + " - " + nrCepFinal;
    	return "";
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColeta.class)     
    public List getPedidoColetas() {
		return pedidoColetas;
	}

	public void setPedidoColetas(List pedidoColetas) {
		this.pedidoColetas = pedidoColetas;
	}

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.TipoDificuldadeAcesso getTipoDificuldadeAcesso() {
        return this.tipoDificuldadeAcesso;
    }

	public void setTipoDificuldadeAcesso(
			com.mercurio.lms.municipios.model.TipoDificuldadeAcesso tipoDificuldadeAcesso) {
        this.tipoDificuldadeAcesso = tipoDificuldadeAcesso;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HorarioTransito.class)     
    public List getHorarioTransitos() {
        return this.horarioTransitos;
    }

    public void setHorarioTransitos(List horarioTransitos) {
        this.horarioTransitos = horarioTransitos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRotaIntervaloCep",
				getIdRotaIntervaloCep()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaIntervaloCep))
			return false;
        RotaIntervaloCep castOther = (RotaIntervaloCep) other;
		return new EqualsBuilder().append(this.getIdRotaIntervaloCep(),
				castOther.getIdRotaIntervaloCep()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaIntervaloCep())
            .toHashCode();
    }

	/**
	 * @return Returns the nrOrdemOperacao.
	 */
	public Short getNrOrdemOperacao() {
		return nrOrdemOperacao;
	}

	/**
	 * @param nrOrdemOperacao
	 *            The nrOrdemOperacao to set.
	 */
	public void setNrOrdemOperacao(Short nrOrdemOperacao) {
		this.nrOrdemOperacao = nrOrdemOperacao;
	}

	/**
	 * @return Returns the dtVigenciaFinal.
	 */
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	/**
	 * @param dtVigenciaFinal
	 *            The dtVigenciaFinal to set.
	 */
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	/**
	 * @return Returns the dtVigenciaInicial.
	 */
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	/**
	 * @param dtVigenciaInicial
	 *            The dtVigenciaInicial to set.
	 */
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return cliente;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public Boolean getBlAtendimentoTemporario() {
		return blAtendimentoTemporario;
	}

	public void setBlAtendimentoTemporario(Boolean blAtendimentoTemporario) {
		this.blAtendimentoTemporario = blAtendimentoTemporario;
	}
	
}

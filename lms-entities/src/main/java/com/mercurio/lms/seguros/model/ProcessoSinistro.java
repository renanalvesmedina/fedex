package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;

/** @author LMS Custom Hibernate CodeGenerator */
public class ProcessoSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idProcessoSinistro;

    /** persistent field */
    private String nrProcessoSinistro;

    /** persistent field */
    private DateTime dhSinistro;
    
    /** persistent field */
    private DateTime dhFechamento;
    
    /** persistent field */
    private DateTime dhAbertura;

    /** persistent field */
    private DomainValue tpLocalSinistro;

    /** nullable persistent field */
    private Integer nrKmSinistro;

    /** not null persistent field */
    private String dsSinistro;
    
    /** nullable persistent field */
    private String dsLocalSinistro;

    /** nullable persistent field */
    private Long nrBoletimOcorrencia;

    /** nullable persistent field */
    private String obSinistro;

    /** nullable persistent field */
    private String dsJustificativaEncerramento;
    
    /** persistent field */
    private Usuario usuarioAbertura;
    
    /** nullable persistent field */
    private Usuario usuarioFechamento;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Aeroporto aeroporto;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.seguros.model.TipoSinistro tipoSinistro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Rodovia rodovia;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private Integer versao;

    /** persistent field */
    private List custoAdicionalSinistros;

    /** persistent field */
    private List doctoProcessoSinistros;

    /** persistent field */
    private List reciboIndenizacoes;

    /** persistent field */
    private List reciboReembolsoProcessos;

    /** persistent field */
    private List fotoProcessoSinistros;
    
    //LMS-6178
    /** nullable persistent field */
    private SituacaoReembolso situacaoReembolso; 
    
    /** nullable persistent field */
    private Motorista motorista;
    
    /** persistent field */
    private BigDecimal vlFranquia;
    
    /** nullable persistent field */
    private String dsComunicadoCorretora; 

    /** GETTERS & SETTERS **/
    public Long getIdProcessoSinistro() {
        return this.idProcessoSinistro;
    }

    public void setIdProcessoSinistro(Long idProcessoSinistro) {
        this.idProcessoSinistro = idProcessoSinistro;
    }

    public String getNrProcessoSinistro() {
		return nrProcessoSinistro;
    }

	public void setNrProcessoSinistro(String nrProcessoSinistro) {
		this.nrProcessoSinistro = nrProcessoSinistro;
    }

    public DateTime getDhSinistro() {
        return this.dhSinistro;
    }

    public void setDhSinistro(DateTime dhSinistro) {
        this.dhSinistro = dhSinistro;
    }

    public DateTime getDhFechamento() {
		return dhFechamento;
	}

	public void setDhFechamento(DateTime dhFechamento) {
		this.dhFechamento = dhFechamento;
	}

	public DomainValue getTpLocalSinistro() {
        return this.tpLocalSinistro;
    }

    public void setTpLocalSinistro(DomainValue tpLocalSinistro) {
        this.tpLocalSinistro = tpLocalSinistro;
    }

    public Integer getNrKmSinistro() {
        return this.nrKmSinistro;
    }

    public void setNrKmSinistro(Integer nrKmSinistro) {
        this.nrKmSinistro = nrKmSinistro;
    }

    public String getDsSinistro() {
        return this.dsSinistro;
    }

    public void setDsSinistro(String dsSinistro) {
        this.dsSinistro = dsSinistro;
    }

    public String getDsLocalSinistro() {
		return dsLocalSinistro;
	}

	public void setDsLocalSinistro(String dsLocalSinistro) {
		this.dsLocalSinistro = dsLocalSinistro;
	}

	public Long getNrBoletimOcorrencia() {
        return this.nrBoletimOcorrencia;
    }

    public void setNrBoletimOcorrencia(Long nrBoletimOcorrencia) {
        this.nrBoletimOcorrencia = nrBoletimOcorrencia;
    }

    public String getDsJustificativaEncerramento() {
        return this.dsJustificativaEncerramento;
    }

	public void setDsJustificativaEncerramento(String dsJustificativaEncerramento) {
        this.dsJustificativaEncerramento = dsJustificativaEncerramento;
    }

    public com.mercurio.lms.seguros.model.TipoSeguro getTipoSeguro() {
        return this.tipoSeguro;
    }

	public void setTipoSeguro(com.mercurio.lms.seguros.model.TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.municipios.model.Aeroporto getAeroporto() {
        return this.aeroporto;
    }

	public void setAeroporto(com.mercurio.lms.municipios.model.Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.seguros.model.TipoSinistro getTipoSinistro() {
        return this.tipoSinistro;
    }

	public void setTipoSinistro(com.mercurio.lms.seguros.model.TipoSinistro tipoSinistro) {
        this.tipoSinistro = tipoSinistro;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.municipios.model.Rodovia getRodovia() {
        return this.rodovia;
    }

    public void setRodovia(com.mercurio.lms.municipios.model.Rodovia rodovia) {
        this.rodovia = rodovia;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.CustoAdicionalSinistro.class)     
    public List getCustoAdicionalSinistros() {
        return this.custoAdicionalSinistros;
    }

    public void setCustoAdicionalSinistros(List custoAdicionalSinistros) {
        this.custoAdicionalSinistros = custoAdicionalSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.DoctoProcessoSinistro.class)     
    public List getDoctoProcessoSinistros() {
        return this.doctoProcessoSinistros;
    }

    public void setDoctoProcessoSinistros(List doctoProcessoSinistros) {
        this.doctoProcessoSinistros = doctoProcessoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.ReciboIndenizacao.class)     
    public List getReciboIndenizacoes() {
        return this.reciboIndenizacoes;
    }

    public void setReciboIndenizacoes(List reciboIndenizacoes) {
        this.reciboIndenizacoes = reciboIndenizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ReciboReembolsoProcesso.class)     
    public List getReciboReembolsoProcessos() {
        return this.reciboReembolsoProcessos;
    }

    public void setReciboReembolsoProcessos(List reciboReembolsoProcessos) {
        this.reciboReembolsoProcessos = reciboReembolsoProcessos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.FotoProcessoSinistro.class)     
    public List getFotoProcessoSinistros() {
        return this.fotoProcessoSinistros;
    }

    public void setFotoProcessoSinistros(List fotoProcessoSinistros) {
        this.fotoProcessoSinistros = fotoProcessoSinistros;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProcessoSinistro", getIdProcessoSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProcessoSinistro))
			return false;
        ProcessoSinistro castOther = (ProcessoSinistro) other;
		return new EqualsBuilder().append(this.getIdProcessoSinistro(), castOther.getIdProcessoSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProcessoSinistro()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public String getObSinistro() {
		return obSinistro;
	}

	public void setObSinistro(String obSinistro) {
		this.obSinistro = obSinistro;
	}

	public DateTime getDhAbertura() {
		return dhAbertura;
	}

	public void setDhAbertura(DateTime dhAbertura) {
		this.dhAbertura = dhAbertura;
	}

	public Usuario getUsuarioAbertura() {
		return usuarioAbertura;
	}

	public void setUsuarioAbertura(Usuario usuarioAbertura) {
		this.usuarioAbertura = usuarioAbertura;
	}

	public Usuario getUsuarioFechamento() {
		return usuarioFechamento;
	}

	public void setUsuarioFechamento(Usuario usuarioFechamento) {
		this.usuarioFechamento = usuarioFechamento;
	}

	//LMS-6178
	public SituacaoReembolso getSituacaoReembolso() {
		return situacaoReembolso;
	}

	public void setSituacaoReembolso(SituacaoReembolso situacaoReembolso) {
		this.situacaoReembolso = situacaoReembolso;
	}

	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public BigDecimal getVlFranquia() {
		return vlFranquia;
	}

	public void setVlFranquia(BigDecimal vlFranquia) {
		this.vlFranquia = vlFranquia;
	}

	public String getDsComunicadoCorretora() {
		return dsComunicadoCorretora;
	}

	public void setDsComunicadoCorretora(String dsComunicadoCorretora) {
		this.dsComunicadoCorretora = dsComunicadoCorretora;
	}

}

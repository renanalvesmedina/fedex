package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class EnderecoPessoa implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_BY_ID_PESSOA_TP_ENDERECO = "com.mercurio.lms.configuracoes.model.EnderecoPessoa.findByIdPessoaTpEndereco";
	public static final String FIND_BY_ID_PESSOA_TP_ENDERECO_LOCAL_ENTREGA = "com.mercurio.lms.configuracoes.model.EnderecoPessoa.findByIdPessoaTpEnderecoLocalEntrega";
	public static final String FIND_BY_PESSOA_TIPO_ENDERECO = "com.mercurio.lms.configuracoes.model.EnderecoPessoa.findByPessoaTipoEndereco";

	/** identifier field */
    private Long idEnderecoPessoa;

    /** persistent field */
    private String nrCep;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private String dsEndereco;

    /** nullable persistent field */
    private String nrEndereco;

    /** nullable persistent field */
    private BigDecimal nrLatitude;

    /** nullable persistent field */
    private BigDecimal nrLongitude;

    private Integer nrQualidade;
    
    /** nullable persistent field */
    private BigDecimal nrLatitudeTmp;

    /** nullable persistent field */
    private BigDecimal nrLongitudeTmp;
    
    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String dsComplemento;

    /** nullable persistent field */
    private String dsBairro;

    /** nullable persistent field */
    private String obEnderecoPessoa;
    
    /** persistent field */
    private Boolean blEnderecoMigrado;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.TipoLogradouro tipoLogradouro;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List tipoEnderecoPessoas;

    /** persistent field */
    private List doctoServicos;

    /** persistent field */
    private List coletaAutomaticaClientes;

    /** persistent field */
    private List telefoneEnderecos;

    /** persistent field */
    private List horarioCorteClientes;
    
    /** persistent field */
    private List milkRemetentes;

    private Usuario usuarioInclusao;
    
    private Usuario usuarioAlteracao;
    
    public Long getIdEnderecoPessoa() {
        return this.idEnderecoPessoa;
    }

    public void setIdEnderecoPessoa(Long idEnderecoPessoa) {
        this.idEnderecoPessoa = idEnderecoPessoa;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public String getDsEndereco() {
        return this.dsEndereco;
    }

    public void setDsEndereco(String dsEndereco) {
        this.dsEndereco = dsEndereco;
    }

    public String getNrEndereco() {
        return this.nrEndereco;
    }

    public void setNrEndereco(String nrEndereco) {
        this.nrEndereco = nrEndereco;
    }

    public BigDecimal getNrLatitude() {
        return this.nrLatitude;
    }

    public void setNrLatitude(BigDecimal nrLatitude) {
        this.nrLatitude = nrLatitude;
    }

    public BigDecimal getNrLongitude() {
        return this.nrLongitude;
    }

    public void setNrLongitude(BigDecimal nrLongitude) {
        this.nrLongitude = nrLongitude;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public String getDsComplemento() {
        return this.dsComplemento;
    }

    public void setDsComplemento(String dsComplemento) {
        this.dsComplemento = dsComplemento;
    }

    public String getDsBairro() {
        return this.dsBairro;
    }

    public void setDsBairro(String dsBairro) {
        this.dsBairro = dsBairro;
    }

    public String getObEnderecoPessoa() {
        return this.obEnderecoPessoa;
    }

    public void setObEnderecoPessoa(String obEnderecoPessoa) {
        this.obEnderecoPessoa = obEnderecoPessoa;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.configuracoes.model.TipoLogradouro getTipoLogradouro() {
        return this.tipoLogradouro;
    }

	public void setTipoLogradouro(
			com.mercurio.lms.configuracoes.model.TipoLogradouro tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa.class)     
    public List getTipoEnderecoPessoas() {
        return this.tipoEnderecoPessoas;
    }

    public void setTipoEnderecoPessoas(List tipoEnderecoPessoas) {
        this.tipoEnderecoPessoas = tipoEnderecoPessoas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServico.class)     
    public List getDoctoServicos() {
        return this.doctoServicos;
    }

    public void setDoctoServicos(List doctoServicos) {
        this.doctoServicos = doctoServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ColetaAutomaticaCliente.class)     
    public List getColetaAutomaticaClientes() {
        return this.coletaAutomaticaClientes;
    }

    public void setColetaAutomaticaClientes(List coletaAutomaticaClientes) {
        this.coletaAutomaticaClientes = coletaAutomaticaClientes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TelefoneEndereco.class)     
    public List<TelefoneEndereco> getTelefoneEnderecos() {
        return this.telefoneEnderecos;
    }

    public void setTelefoneEnderecos(List telefoneEnderecos) {
        this.telefoneEnderecos = telefoneEnderecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HorarioCorteCliente.class)     
    public List getHorarioCorteClientes() {
        return this.horarioCorteClientes;
    }

    public void setHorarioCorteClientes(List horarioCorteClientes) {
        this.horarioCorteClientes = horarioCorteClientes;
    } 
    
    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRemetente.class)     
    public List getMilkRemetentes() {
        return milkRemetentes;
    }
    
    public void setMilkRemetentes(List milkRemetentes) {
        this.milkRemetentes = milkRemetentes;
    }

    /**
	 * Retorna o endereço no formato : tipoLogradouro.dsTipoLogradouro + “ “ +
	 * dsEndereco + “, “ + nrEndereco + “ – “ + dsComplemento
	 * 
     * @return
     */
    public String getEnderecoCompleto(){
		StringBuffer strEndereco = new StringBuffer();
		Hibernate.initialize(tipoLogradouro);
		if (Hibernate.isInitialized(tipoLogradouro)
				&& getTipoLogradouro() != null) {
			strEndereco.append(getTipoLogradouro().getDsTipoLogradouro() + " ");
		}
    	strEndereco.append(getDsEndereco() + ", ");

    	if (getNrEndereco() != null) {
    		strEndereco.append(getNrEndereco().toString().trim());
    	} else {
    		strEndereco.append("N/D");
    	}

    	if (getDsComplemento() != null) {
    		strEndereco.append(" - " + getDsComplemento());
    	}

    	return strEndereco.toString();
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEnderecoPessoa",
				getIdEnderecoPessoa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EnderecoPessoa))
			return false;
        EnderecoPessoa castOther = (EnderecoPessoa) other;
		return new EqualsBuilder().append(this.getIdEnderecoPessoa(),
				castOther.getIdEnderecoPessoa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEnderecoPessoa()).toHashCode();
    }

	public Boolean getBlEnderecoMigrado() {
		return blEnderecoMigrado;
	}

	public void setBlEnderecoMigrado(Boolean blEnderecoMigrado) {
		this.blEnderecoMigrado = blEnderecoMigrado;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public BigDecimal getNrLongitudeTmp() {
		return nrLongitudeTmp;
}

	public void setNrLongitudeTmp(BigDecimal nrLongitudeTmp) {
		this.nrLongitudeTmp = nrLongitudeTmp;
	}

	public BigDecimal getNrLatitudeTmp() {
		return nrLatitudeTmp;
	}

	public void setNrLatitudeTmp(BigDecimal nrLatitudeTmp) {
		this.nrLatitudeTmp = nrLatitudeTmp;
	}

	public Integer getNrQualidade() {
		return nrQualidade;
	}

	public void setNrQualidade(Integer nrQualidade) {
		this.nrQualidade = nrQualidade;
	}

}

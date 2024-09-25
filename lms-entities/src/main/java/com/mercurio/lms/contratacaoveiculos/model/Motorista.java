package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Motorista implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotorista;

    /** persistent field */
    private DomainValue tpVinculo;

    /** persistent field */
    private DomainValue tpSexo;

    /** persistent field */
    private String dsClasse;
 
    /** persistent field */
    private YearMonthDay dtVencimentoHabilitacao;

    /** persistent field */
    private YearMonthDay dtNascimento;

    /** persistent field */
    private Long nrCarteiraHabilitacao;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private DomainValue tpCorPele;

    /** persistent field */
    private DomainValue tpCorCabelo;

    /** persistent field */
    private DomainValue tpCorOlhos;

    /** persistent field */
    private Boolean blPossuiBarba;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Integer nrProntuario;

    /** nullable persistent field */
    private Long nrCarteiraProfissional;

    /** nullable persistent field */
    private Long nrSerieCarteiraProfissional;

    /** nullable persistent field */
    private YearMonthDay dtEmissaoCarteiraProfission;
    
    /** nullable persistent field */
    private String nmPai;

    /** nullable persistent field */
    private String nmMae;
    
    private YearMonthDay dtAtualizacao;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipioNaturalidade;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio localEmissaoIdentidade;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioMotorista;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List liberacaoReguladoras;

    /** persistent field */
    private List motoristaRotaViagems;

    /** persistent field */
    private List solicMonitPreventivos;

    /** persistent field */
    private List motoristaControleCargas;

    /** persistent field */
    private List meioTranspRodoMotoristas;

    /** persistent field */
    private List ordemSaidas;

    /** persistent field */
    private List bloqueioMotoristaProps;

    /** persistent field */
    private List solicitacaoSinais;

    /** persistent field */
    private List controleCargas;
    
    /** persistent field */
    private List fotoMotoristas;
    
    /** persistent field */
    private List reciboFreteCarreteiros;

    private Boolean blTermoComp;
    
    /** persistent field */
    private List volRetiradasEqptos;
    
    
    private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private Pendencia pendencia;
	    
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador;
    
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAlteracao;
    
    private DomainValue tpOperacao;
   

	public List getVolRetiradasEqptos() {
		return volRetiradasEqptos;
	}

	public void setVolRetiradasEqptos(List volRetiradasEqptos) {
		this.volRetiradasEqptos = volRetiradasEqptos;
	}
    
    public Long getIdMotorista() {
        return this.idMotorista;
    }

    public void setIdMotorista(Long idMotorista) {
        this.idMotorista = idMotorista;
    }

    public DomainValue getTpVinculo() {
        return this.tpVinculo;
    }

    public void setTpVinculo(DomainValue tpVinculo) {
        this.tpVinculo = tpVinculo;
    }

    public DomainValue getTpSexo() {
        return this.tpSexo;
    }

    public void setTpSexo(DomainValue tpSexo) {
        this.tpSexo = tpSexo;
    }

    public String getDsClasse() {
        return this.dsClasse;
    }

    public void setDsClasse(String dsClasse) {
        this.dsClasse = dsClasse;
    }

    public YearMonthDay getDtVencimentoHabilitacao() {
        return this.dtVencimentoHabilitacao;
    }

    public void setDtVencimentoHabilitacao(YearMonthDay dtVencimentoHabilitacao) {
        this.dtVencimentoHabilitacao = dtVencimentoHabilitacao;
    }

    public YearMonthDay getDtNascimento() {
        return this.dtNascimento;
    }

    public void setDtNascimento(YearMonthDay dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Long getNrCarteiraHabilitacao() {
        return this.nrCarteiraHabilitacao;
    }

    public void setNrCarteiraHabilitacao(Long nrCarteiraHabilitacao) {
        this.nrCarteiraHabilitacao = nrCarteiraHabilitacao;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public DomainValue getTpCorPele() {
        return this.tpCorPele;
    }

    public void setTpCorPele(DomainValue tpCorPele) {
        this.tpCorPele = tpCorPele;
    }

    public DomainValue getTpCorCabelo() {
        return this.tpCorCabelo;
    }

    public void setTpCorCabelo(DomainValue tpCorCabelo) {
        this.tpCorCabelo = tpCorCabelo;
    }

    public DomainValue getTpCorOlhos() {
        return this.tpCorOlhos;
    }

    public void setTpCorOlhos(DomainValue tpCorOlhos) {
        this.tpCorOlhos = tpCorOlhos;
    }

    public Boolean getBlPossuiBarba() {
        return this.blPossuiBarba;
    }

    public void setBlPossuiBarba(Boolean blPossuiBarba) {
        this.blPossuiBarba = blPossuiBarba;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Integer getNrProntuario() {
        return this.nrProntuario;
    }

    public void setNrProntuario(Integer nrProntuario) {
        this.nrProntuario = nrProntuario;
    }

    public Long getNrCarteiraProfissional() {
        return this.nrCarteiraProfissional;
    }

    public void setNrCarteiraProfissional(Long nrCarteiraProfissional) {
        this.nrCarteiraProfissional = nrCarteiraProfissional;
    }

    public Long getNrSerieCarteiraProfissional() {
        return this.nrSerieCarteiraProfissional;
    }

    public void setNrSerieCarteiraProfissional(Long nrSerieCarteiraProfissional) {
        this.nrSerieCarteiraProfissional = nrSerieCarteiraProfissional;
    }

    public YearMonthDay getDtEmissaoCarteiraProfission() {
        return this.dtEmissaoCarteiraProfission;
    }

	public void setDtEmissaoCarteiraProfission(
			YearMonthDay dtEmissaoCarteiraProfission) {
        this.dtEmissaoCarteiraProfission = dtEmissaoCarteiraProfission;
    }

    public String getNmPai() {
        return this.nmPai;
    }

    public void setNmPai(String nmPai) {
        this.nmPai = nmPai;
    }

    public String getNmMae() {
        return this.nmMae;
    }

    public void setNmMae(String nmMae) {
        this.nmMae = nmMae;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora.class)     
    public List getLiberacaoReguladoras() {
        return this.liberacaoReguladoras;
    }

    public void setLiberacaoReguladoras(List liberacaoReguladoras) {
        this.liberacaoReguladoras = liberacaoReguladoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MotoristaRotaViagem.class)     
    public List getMotoristaRotaViagems() {
        return this.motoristaRotaViagems;
    }

    public void setMotoristaRotaViagems(List motoristaRotaViagems) {
        this.motoristaRotaViagems = motoristaRotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List getSolicMonitPreventivos() {
        return this.solicMonitPreventivos;
    }

    public void setSolicMonitPreventivos(List solicMonitPreventivos) {
        this.solicMonitPreventivos = solicMonitPreventivos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.MotoristaControleCarga.class)     
    public List getMotoristaControleCargas() {
        return this.motoristaControleCargas;
    }

    public void setMotoristaControleCargas(List motoristaControleCargas) {
        this.motoristaControleCargas = motoristaControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista.class)     
    public List getMeioTranspRodoMotoristas() {
        return this.meioTranspRodoMotoristas;
    }

    public void setMeioTranspRodoMotoristas(List meioTranspRodoMotoristas) {
        this.meioTranspRodoMotoristas = meioTranspRodoMotoristas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.OrdemSaida.class)     
    public List getOrdemSaidas() {
        return this.ordemSaidas;
    }

    public void setOrdemSaidas(List ordemSaidas) {
        this.ordemSaidas = ordemSaidas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp.class)     
    public List getBloqueioMotoristaProps() {
        return this.bloqueioMotoristaProps;
    }

    public void setBloqueioMotoristaProps(List bloqueioMotoristaProps) {
        this.bloqueioMotoristaProps = bloqueioMotoristaProps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicitacaoSinal.class)     
    public List getSolicitacaoSinais() {
        return this.solicitacaoSinais;
    }

    public void setSolicitacaoSinais(List solicitacaoSinais) {
        this.solicitacaoSinais = solicitacaoSinais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idMotorista", getIdMotorista()).toString();
    }

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}
	
	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}
		
	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioMotorista() {
		return usuarioMotorista;
	}

	public void setUsuarioMotorista(
			com.mercurio.lms.configuracoes.model.Usuario usuarioMotorista) {
		this.usuarioMotorista = usuarioMotorista;
	}
	
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Motorista))
			return false;
        Motorista castOther = (Motorista) other;
		return new EqualsBuilder().append(this.getIdMotorista(),
				castOther.getIdMotorista()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotorista()).toHashCode();
    }

	/**
	 * @return Returns the municipioNaturalidade.
	 */
	public com.mercurio.lms.municipios.model.Municipio getMunicipioNaturalidade() {
		return municipioNaturalidade;
	}

	/**
	 * @param municipioNaturalidade
	 *            The municipioNaturalidade to set.
	 */
	public void setMunicipioNaturalidade(
			com.mercurio.lms.municipios.model.Municipio municipioNaturalidade) {
		this.municipioNaturalidade = municipioNaturalidade;
	}

	/**
	 * @return Returns the localEmissaoIdentidade.
	 */
	public com.mercurio.lms.municipios.model.Municipio getLocalEmissaoIdentidade() {
		return localEmissaoIdentidade;
	}

	/**
	 * @param localEmissaoIdentidade
	 *            The localEmissaoIdentidade to set.
	 */
	public void setLocalEmissaoIdentidade(
			com.mercurio.lms.municipios.model.Municipio localEmissaoIdentidade) {
		this.localEmissaoIdentidade = localEmissaoIdentidade;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.FotoMotorista.class)     
	public List getFotoMotoristas() {
		return fotoMotoristas;
	}

	public void setFotoMotoristas(List fotoMotoristas) {
		this.fotoMotoristas = fotoMotoristas;
	}

	/**
	 * @return Returns the unidadeFedetativa.
	 */
	public com.mercurio.lms.municipios.model.UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	/**
	 * @param unidadeFedetativa
	 *            The unidadeFedetativa to set.
	 */
	public void setUnidadeFederativa(
			com.mercurio.lms.municipios.model.UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
	public List getReciboFreteCarreteiros() {
		return reciboFreteCarreteiros;
	}

	public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
		this.reciboFreteCarreteiros = reciboFreteCarreteiros;
	}

	/**
	 * @return Returns the dtAtualizacao.
	 */
	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	/**
	 * @param dtAtualizacao
	 *            The dtAtualizacao to set.
	 */
	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public Boolean getBlTermoComp() {
		return blTermoComp;
	}

	public void setBlTermoComp(Boolean blTermoComp) {
		this.blTermoComp = blTermoComp;
	}

	public DomainValue getTpSituacaoWorkflow() {
		return tpSituacaoWorkflow;
	}

	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAprovador() {
		return usuarioAprovador;
	}

	public void setUsuarioAprovador(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(com.mercurio.lms.configuracoes.model.Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}
	
	

}

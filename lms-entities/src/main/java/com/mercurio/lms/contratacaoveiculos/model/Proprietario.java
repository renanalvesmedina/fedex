package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Proprietario implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idProprietario;

    /** persistent field */
    private DomainValue tpProprietario;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private TelefoneEndereco telefoneEndereco;

    /** nullable persistent field */
    private Long nrPis;

    /** nullable persistent field */
    private Byte nrDependentes;

    /** nullable persistent field */
    private Long nrAntt;

    /** nullable persistent field */
    private DomainValue tpLocalPagtoPostoPassagem;

    /** nullable persistent field */
    private DomainValue tpPeriodoPagto;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private DomainValue diaSemana;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List liberacaoReguladoras;

    /** persistent field */
    private List criterioAplicSimulacoes;

    /** persistent field */
    private List meioTranspProprietarios;

    /** persistent field */
    private List pagtoProprietarioCcs;

    /** persistent field */
    private List bloqueioMotoristaProps;

    /** persistent field */
    private List solicitacaoSinais;

    /** persistent field */
    private List descontoInssCarreteiros;

    /** persistent field */
    private List controleCargas;

    /** persistent field */
    private List beneficiarioProprietarios;
    
    /** persistent field */
    private List reciboFreteCarreteiros;

    private YearMonthDay dtAtualizacao;
    
	 private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private Pendencia pendencia;
	    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador;
    
    /** persistent field */
    private DomainValue tpOperacao;
	  
    private DomainValue blMei;
   
	private String nrIdentificacaoMei;
    
    private String nmMei;
    
    private YearMonthDay dtNascimento;
    
    private DomainValue blCooperado;
    
    private DomainValue blNaoAtualizaDbi;
    
    private DomainValue blRotaFixa;
    
    private YearMonthDay dtVigenciaInicial;
    
    private YearMonthDay dtVigenciaFinal;
  
	public Long getIdProprietario() {
        return this.idProprietario;
    }

    public void setIdProprietario(Long idProprietario) {
        this.idProprietario = idProprietario;
    }

    public DomainValue getTpProprietario() {
        return this.tpProprietario;
    }

    public void setTpProprietario(DomainValue tpProprietario) {
        this.tpProprietario = tpProprietario;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Byte getNrDependentes() {
        return this.nrDependentes;
    }

    public void setNrDependentes(Byte nrDependentes) {
        this.nrDependentes = nrDependentes;
    }

    public Long getNrAntt() {
        return this.nrAntt;
    }

    public void setNrAntt(Long nrAntt) {
        this.nrAntt = nrAntt;
    }

    public DomainValue getTpLocalPagtoPostoPassagem() {
        return this.tpLocalPagtoPostoPassagem;
    }

	public void setTpLocalPagtoPostoPassagem(
			DomainValue tpLocalPagtoPostoPassagem) {
        this.tpLocalPagtoPostoPassagem = tpLocalPagtoPostoPassagem;
    }

    public DomainValue getTpPeriodoPagto() {
        return this.tpPeriodoPagto;
    }

    public void setTpPeriodoPagto(DomainValue tpPeriodoPagto) {
        this.tpPeriodoPagto = tpPeriodoPagto;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public DomainValue getDiaSemana() {
        return this.diaSemana;
    }

    public void setDiaSemana(DomainValue diaSemana) {
        this.diaSemana = diaSemana;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }
  
    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora.class)     
    public List getLiberacaoReguladoras() {
        return this.liberacaoReguladoras;
    }

    public void setLiberacaoReguladoras(List liberacaoReguladoras) {
        this.liberacaoReguladoras = liberacaoReguladoras;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao.class)     
    public List getCriterioAplicSimulacoes() {
        return this.criterioAplicSimulacoes;
    }

    public void setCriterioAplicSimulacoes(List criterioAplicSimulacoes) {
        this.criterioAplicSimulacoes = criterioAplicSimulacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario.class)     
    public List getMeioTranspProprietarios() {
        return this.meioTranspProprietarios;
    }

    public void setMeioTranspProprietarios(List meioTranspProprietarios) {
        this.meioTranspProprietarios = meioTranspProprietarios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoProprietarioCc.class)     
    public List getPagtoProprietarioCcs() {
        return this.pagtoProprietarioCcs;
    }

    public void setPagtoProprietarioCcs(List pagtoProprietarioCcs) {
        this.pagtoProprietarioCcs = pagtoProprietarioCcs;
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

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.DescontoInssCarreteiro.class)     
    public List getDescontoInssCarreteiros() {
        return this.descontoInssCarreteiros;
    }

    public void setDescontoInssCarreteiros(List descontoInssCarreteiros) {
        this.descontoInssCarreteiros = descontoInssCarreteiros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario.class)     
    public List getBeneficiarioProprietarios() {
        return this.beneficiarioProprietarios;
    }

    public void setBeneficiarioProprietarios(List beneficiarioProprietarios) {
        this.beneficiarioProprietarios = beneficiarioProprietarios;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProprietario",
				getIdProprietario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Proprietario))
			return false;
        Proprietario castOther = (Proprietario) other;
		return new EqualsBuilder().append(this.getIdProprietario(),
				castOther.getIdProprietario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProprietario()).toHashCode();
    }

	public TelefoneEndereco getTelefoneEndereco() {
		return telefoneEndereco;
	}

	public void setTelefoneEndereco(TelefoneEndereco telefoneEndereco) {
		this.telefoneEndereco = telefoneEndereco;
	}

	public Long getNrPis() {
		return nrPis;
	}

	public void setNrPis(Long nrPis) {
		this.nrPis = nrPis;
	}
	
    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
    public List getReciboFreteCarreteiros() {
        return this.reciboFreteCarreteiros;
    }

    public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
    }

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
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

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAprovador() {
		return usuarioAprovador;
	}

	public void setUsuarioAprovador(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getBlMei() {
		return blMei;
	}

	public void setBlMei(DomainValue blMei) {
		this.blMei = blMei;
	}

	public String getNrIdentificacaoMei() {
		return nrIdentificacaoMei;
	}

	public void setNrIdentificacaoMei(String nrIdentificacaoMei) {
		this.nrIdentificacaoMei = nrIdentificacaoMei;
	}

	public String getNmMei() {
		return nmMei;
	}

	public void setNmMei(String nmMei) {
		this.nmMei = nmMei;
	}

	public YearMonthDay getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(YearMonthDay dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public DomainValue getBlCooperado() {
		return blCooperado;
	}

	public void setBlCooperado(DomainValue blCooperado) {
		this.blCooperado = blCooperado;
	}

	public DomainValue getBlNaoAtualizaDbi() {
		return blNaoAtualizaDbi;
	}

	public void setBlNaoAtualizaDbi(DomainValue blNaoAtualizaDbi) {
		this.blNaoAtualizaDbi = blNaoAtualizaDbi;
	}
	
	public DomainValue getBlRotaFixa() {
		return blRotaFixa;
	}
	
	public void setBlRotaFixa(DomainValue blRotaFixa) {
		this.blRotaFixa = blRotaFixa;
	}
	
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
}
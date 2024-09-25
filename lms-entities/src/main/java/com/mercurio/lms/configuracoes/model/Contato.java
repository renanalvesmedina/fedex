package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Contato implements Serializable {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idContato;

    /** persistent field */
    private String nmContato;

    /** persistent field */
    private DomainValue tpContato;

    /** nullable persistent field */
    private Long nrRepresentante;

    /** nullable persistent field */
    private YearMonthDay dtAniversario;

    /** nullable persistent field */
    private String dsEmail;

    /** nullable persistent field */
    private String dsFuncao;

    /** nullable persistent field */
    private String dsDepartamento;

    /** nullable persistent field */
    private String obContato;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List versaoContatoPces;

    /** persistent field */
    private List contatoComunicacoes;

    /** persistent field */
    private List agendaCobrancas;

    /** persistent field */
    private List telefoneContatos;

    /** persistent field */
    private List contatoCorrespondencias;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private List volFabricantes;

    /** persistent field */
    private List volOperadorasTelefonias;
   
    /** persistent field */
    private YearMonthDay dtCadastro;
    
    /** persistent field */
    private YearMonthDay dtUltimaMovimentacao;

    private DispositivoContato dispositivoContato;

    public Long getIdContato() {
        return this.idContato;
    }

    public void setIdContato(Long idContato) {
        this.idContato = idContato;
    }

    public String getNmContato() {
        return this.nmContato;
    }

    public void setNmContato(String nmContato) {
        this.nmContato = nmContato;
    }

    public DomainValue getTpContato() {
        return this.tpContato;
    }

    public void setTpContato(DomainValue tpContato) {
        this.tpContato = tpContato;
    }

    public Long getNrRepresentante() {
        return this.nrRepresentante;
    }

    public void setNrRepresentante(Long nrRepresentante) {
        this.nrRepresentante = nrRepresentante;
    }

    public YearMonthDay getDtAniversario() {
        return this.dtAniversario;
    }

    public void setDtAniversario(YearMonthDay dtAniversario) {
        this.dtAniversario = dtAniversario;
    }

    public String getDsEmail() {
        return this.dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = (dsEmail == null ? null : dsEmail.replaceAll("[\\t\\n\\r,;]+","").trim());
    }

    public String getDsFuncao() {
        return this.dsFuncao;
    }

    public void setDsFuncao(String dsFuncao) {
        this.dsFuncao = dsFuncao;
    }

    public String getDsDepartamento() {
        return this.dsDepartamento;
    }

    public void setDsDepartamento(String dsDepartamento) {
        this.dsDepartamento = dsDepartamento;
    }

    public String getObContato() {
        return this.obContato;
    }

    public void setObContato(String obContato) {
        this.obContato = obContato;
    }
   
    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.VersaoContatoPce.class)     
    public List getVersaoContatoPces() {
        return this.versaoContatoPces;
    }

    public void setVersaoContatoPces(List versaoContatoPces) {
        this.versaoContatoPces = versaoContatoPces;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sim.model.ContatoComunicacao.class)     
    public List getContatoComunicacoes() {
        return this.contatoComunicacoes;
    }

    public void setContatoComunicacoes(List contatoComunicacoes) {
        this.contatoComunicacoes = contatoComunicacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaCobranca.class)     
    public List getAgendaCobrancas() {
        return this.agendaCobrancas;
    }

    public void setAgendaCobrancas(List agendaCobrancas) {
        this.agendaCobrancas = agendaCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.TelefoneContato.class)     
    public List getTelefoneContatos() {
        return this.telefoneContatos;
    }

    public void setTelefoneContatos(List telefoneContatos) {
        this.telefoneContatos = telefoneContatos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ContatoCorrespondencia.class)     
    public List getContatoCorrespondencias() {
        return this.contatoCorrespondencias;
    }

    public void setContatoCorrespondencias(List contatoCorrespondencias) {
        this.contatoCorrespondencias = contatoCorrespondencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idContato", getIdContato())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Contato))
			return false;
        Contato castOther = (Contato) other;
		return new EqualsBuilder().append(this.getIdContato(),
				castOther.getIdContato()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdContato()).toHashCode();
    }

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List getVolFabricantes() {
		return volFabricantes;
	}

	public void setVolFabricantes(List volFabricantes) {
		this.volFabricantes = volFabricantes;
	}

	public List getVolOperadorasTelefonias() {
		return volOperadorasTelefonias;
	}

	public void setVolOperadorasTelefonias(List volOperadorasTelefonias) {
		this.volOperadorasTelefonias = volOperadorasTelefonias;
	}

	public YearMonthDay getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(YearMonthDay dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public YearMonthDay getDtUltimaMovimentacao() {
		return dtUltimaMovimentacao;
	}

	public void setDtUltimaMovimentacao(YearMonthDay dtUltimaMovimentacao) {
		this.dtUltimaMovimentacao = dtUltimaMovimentacao;
	}

    public DispositivoContato getDispositivoContato() {
        return dispositivoContato;
}

    public void setDispositivoContato(DispositivoContato dispositivoContato) {
        this.dispositivoContato = dispositivoContato;
    }

}

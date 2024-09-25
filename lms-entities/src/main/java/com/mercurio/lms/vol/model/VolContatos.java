package com.mercurio.lms.vol.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Filial;

public class VolContatos implements Serializable{
	 
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idContato;
	
	private Filial filial;
	
	private Pessoa pessoa;
	
	 /** persistent field */
    private List volEmailsRecusas; 
	
	/** persistent field */
	private String nmContato;
	
	/** persistent field */
	private String dsEmail;
	
	/** persistent field */
	private String dsDepartamento;
	
	/** persistent field */
	private String dsFuncao;
	
	/** persistent field */
	private String obContato;

    /** persistent field */
    private Boolean blAtivo;

	public Boolean getBlAtivo() {
		return blAtivo;
	}

	public void setBlAtivo(Boolean blAtivo) {
		this.blAtivo = blAtivo;
	}

	public String getDsDepartamento() {
		return this.dsDepartamento;
	}

	public void setDsDepartamento(String dsDepartamento) {
		this.dsDepartamento = dsDepartamento;
	}

	public String getDsFuncao() {
		return this.dsFuncao;
	}

	public void setDsFuncao(String dsFuncao) {
		this.dsFuncao = dsFuncao;
	}

	public Long getIdContato() {
		return this.idContato;
	}

	public void setIdContato(Long idContato) {
		this.idContato = idContato;
	}

	public String getObContato() {
		return this.obContato;
	}

	public void setObContato(String obContato) {
		this.obContato = obContato;
	}	
	
    public String toString() {
		return new ToStringBuilder(this).append("idContato", getIdContato())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolContatos))
			return false;
        VolContatos castOther = (VolContatos) other;
		return new EqualsBuilder().append(this.getIdContato(),
				castOther.getIdContato()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdContato()).toHashCode();
    }

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getDsEmail() {
		return this.dsEmail;
	}

	public void setDsEmail(String dsEmail) {
		this.dsEmail = dsEmail;
	}

	public String getNmContato() {
		return nmContato;
	}

	public void setNmContato(String nmContato) {
		this.nmContato = nmContato;
	}

	public List getVolEmailsRecusas() {
		return volEmailsRecusas;
	}

	public void setVolEmailsRecusas(List volEmailsRecusas) {
		this.volEmailsRecusas = volEmailsRecusas;
	}

}

package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class SolicitacaoRetirada implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSolicitacaoRetirada;
    
    /** identifier field */
    private Long nrSolicitacaoRetirada;
    
    /** persistent field */
    private String nmRetirante;

    /** persistent field */
    private DomainValue tpIdentificacao;
    
    /** persistent field */
    private DomainValue tpRegistroLiberacao;
    
    /** persistent field */
    private String nrDdd;

    /** persistent field */
    private String nrTelefone;

    /** persistent field */
    private DateTime dhSolicitacao;

    /** persistent field */
    private DateTime dhPrevistaRetirada;
    
    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private String dsFuncaoResponsavelAutoriza;
    
    /** nullable persistent field */
    private Integer versao;

    /** nullable persistent field */
    private String nrRg;

    /** nullable persistent field */
    private Long nrCnpj;

    /** nullable persistent field */
    private String nrPlaca;
    
    /** nullable persistent field */
    private String nrPlacaSemiReboque;
    
    /** persistent field */
    private String nmResponsavelAutorizacao;
    
    /** persistent field */
    private String nrTelefoneAutorizador;
    
    /** persistent field */
    private String nrDDDAutorizador;
        
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente consignatario;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente destinatario;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente remetente;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioCriacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialRetirada;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAutorizacao;

    /** persistent field */
    private List documentoServicoRetiradas;
    
    /** persistent field */
    private List manifestos;

    public Long getIdSolicitacaoRetirada() {
        return this.idSolicitacaoRetirada;
    }

    public void setIdSolicitacaoRetirada(Long idSolicitacaoRetirada) {
        this.idSolicitacaoRetirada = idSolicitacaoRetirada;
    }

    public String getNmRetirante() {
        return this.nmRetirante;
    }

    public void setNmRetirante(String nmRetirante) {
        this.nmRetirante = nmRetirante;
    }

    public DomainValue getTpIdentificacao() {
        return this.tpIdentificacao;
    }

    public void setTpIdentificacao(DomainValue tpIdentificacao) {
        this.tpIdentificacao = tpIdentificacao;
    }

    public String getNrDdd() {
        return this.nrDdd;
    }

    public void setNrDdd(String nrDdd) {
        this.nrDdd = nrDdd;
    }

    public String getNrTelefone() {
        return this.nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    public DateTime getDhSolicitacao() {
        return this.dhSolicitacao;
    }

    public void setDhSolicitacao(DateTime dhSolicitacao) {
        this.dhSolicitacao = dhSolicitacao;
    }

    public DateTime getDhPrevistaRetirada() {
        return this.dhPrevistaRetirada;
    }

    public void setDhPrevistaRetirada(DateTime dhPrevistaRetirada) {
        this.dhPrevistaRetirada = dhPrevistaRetirada;
    }

    public String getNrRg() {
        return this.nrRg;
    }

    public void setNrRg(String nrRg) {
        this.nrRg = nrRg;
    }

    public Long getNrCnpj() {
        return this.nrCnpj;
    }

    public void setNrCnpj(Long nrCnpj) {
        this.nrCnpj = nrCnpj;
    }

    public String getNrPlaca() {
        return this.nrPlaca;
    }

    public void setNrPlaca(String nrPlaca) {
        this.nrPlaca = nrPlaca;
    }

	public String getDsFuncaoResponsavelAutoriza() {
		return dsFuncaoResponsavelAutoriza;
	}

	public void setDsFuncaoResponsavelAutoriza(
			String dsFuncaoResponsavelAutoriza) {
		this.dsFuncaoResponsavelAutoriza = dsFuncaoResponsavelAutoriza;
	}

	public String getNmResponsavelAutorizacao() {
		return nmResponsavelAutorizacao;
	}

	public void setNmResponsavelAutorizacao(String nmResponsavelAutorizacao) {
		this.nmResponsavelAutorizacao = nmResponsavelAutorizacao;
	}

	public String getNrPlacaSemiReboque() {
		return nrPlacaSemiReboque;
	}

	public void setNrPlacaSemiReboque(String nrPlacaSemiReboque) {
		this.nrPlacaSemiReboque = nrPlacaSemiReboque;
	}

	public Long getNrSolicitacaoRetirada() {
		return nrSolicitacaoRetirada;
	}

	public void setNrSolicitacaoRetirada(Long nrSolicitacaoRetirada) {
		this.nrSolicitacaoRetirada = nrSolicitacaoRetirada;
	}

	public String getNrTelefoneAutorizador() {
		return nrTelefoneAutorizador;
	}

	public void setNrTelefoneAutorizador(String nrTelefoneAutorizador) {
		this.nrTelefoneAutorizador = nrTelefoneAutorizador;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer nrVersao) {
		this.versao = nrVersao;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	
	public String getNrDDDAutorizador() {
		return nrDDDAutorizador;
	}

	public void setNrDDDAutorizador(String nrDDDAutorizador) {
		this.nrDDDAutorizador = nrDDDAutorizador;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.DocumentoServicoRetirada.class)     
    public List getDocumentoServicoRetiradas() {
        return this.documentoServicoRetiradas;
    }

    public void setDocumentoServicoRetiradas(List documentoServicoRetiradas) {
        this.documentoServicoRetiradas = documentoServicoRetiradas;
    }

    public com.mercurio.lms.vendas.model.Cliente getConsignatario() {
		return consignatario;
	}

	public void setConsignatario(
			com.mercurio.lms.vendas.model.Cliente consignatrario) {
		this.consignatario = consignatrario;
	}

	public com.mercurio.lms.vendas.model.Cliente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(
			com.mercurio.lms.vendas.model.Cliente destinatario) {
		this.destinatario = destinatario;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialRetirada() {
		return filialRetirada;
	}

	public void setFilialRetirada(
			com.mercurio.lms.municipios.model.Filial filialRetirada) {
		this.filialRetirada = filialRetirada;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAutorizacao() {
		return usuarioAutorizacao;
	}

	public void setUsuarioAutorizacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAutorizacao) {
		this.usuarioAutorizacao = usuarioAutorizacao;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idSolicitacaoRetirada",
				getIdSolicitacaoRetirada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SolicitacaoRetirada))
			return false;
        SolicitacaoRetirada castOther = (SolicitacaoRetirada) other;
		return new EqualsBuilder().append(this.getIdSolicitacaoRetirada(),
				castOther.getIdSolicitacaoRetirada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSolicitacaoRetirada())
            .toHashCode();
    }

	/**
	 * @return Returns the remetente.
	 */
	public com.mercurio.lms.vendas.model.Cliente getRemetente() {
		return remetente;
	}

	/**
	 * @param remetente
	 *            The remetente to set.
	 */
	public void setRemetente(com.mercurio.lms.vendas.model.Cliente remetente) {
		this.remetente = remetente;
	}

	/**
	 * @return Returns the manifestos.
	 */
	public List getManifestos() {
		return manifestos;
	}

	/**
	 * @param manifestos
	 *            The manifestos to set.
	 */
	public void setManifestos(List manifestos) {
		this.manifestos = manifestos;
	}

	/**
	 * @return Returns the tpRegistroLiberacao.
	 */
	public DomainValue getTpRegistroLiberacao() {
		return tpRegistroLiberacao;
	}

	/**
	 * @param tpRegistroLiberacao
	 *            The tpRegistroLiberacao to set.
	 */
	public void setTpRegistroLiberacao(DomainValue tpRegistroLiberacao) {
		this.tpRegistroLiberacao = tpRegistroLiberacao;
	}

}

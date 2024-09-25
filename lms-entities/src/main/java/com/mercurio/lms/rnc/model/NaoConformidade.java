package com.mercurio.lms.rnc.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Awb;

/** @author LMS Custom Hibernate CodeGenerator */
public class NaoConformidade implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNaoConformidade;

    /** persistent field */
    private Integer nrNaoConformidade;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private DomainValue tpStatusNaoConformidade;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** persistent field */
    private String dsMotivoAbertura;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente clienteByIdClienteRemetente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente clienteByIdClienteDestinatario;
    
    /** persistent field */
    private List naoConformidadeAuditorias;

    /** persistent field */
    private List ocorrenciaNaoConformidades;

    /** persistent field */
    private List itensMda;
    
    private DomainValue tpModal;
    
    private DomainValue causadorRnc;
    
    private Awb awb;

    public NaoConformidade() {
    }

    public NaoConformidade(Long idNaoConformidade, Integer nrNaoConformidade, DateTime dhInclusao, DomainValue tpStatusNaoConformidade, DateTime dhEmissao, String dsMotivoAbertura, DoctoServico doctoServico, Filial filial, Cliente clienteByIdClienteRemetente, Cliente clienteByIdClienteDestinatario, List naoConformidadeAuditorias, List ocorrenciaNaoConformidades, List itensMda, DomainValue tpModal, DomainValue causadorRnc, Awb awb, String causadorConformidade) {
        this.idNaoConformidade = idNaoConformidade;
        this.nrNaoConformidade = nrNaoConformidade;
        this.dhInclusao = dhInclusao;
        this.tpStatusNaoConformidade = tpStatusNaoConformidade;
        this.dhEmissao = dhEmissao;
        this.dsMotivoAbertura = dsMotivoAbertura;
        this.doctoServico = doctoServico;
        this.filial = filial;
        this.clienteByIdClienteRemetente = clienteByIdClienteRemetente;
        this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
        this.naoConformidadeAuditorias = naoConformidadeAuditorias;
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
        this.itensMda = itensMda;
        this.tpModal = tpModal;
        this.causadorRnc = causadorRnc;
        this.awb = awb;
        this.causadorConformidade = causadorConformidade;
    }

    @Transient
    private String causadorConformidade;
    
    public Long getIdNaoConformidade() {
        return this.idNaoConformidade;
    }

    public void setIdNaoConformidade(Long idNaoConformidade) {
        this.idNaoConformidade = idNaoConformidade;
    }

    public Integer getNrNaoConformidade() {
        return this.nrNaoConformidade;
    }

    public void setNrNaoConformidade(Integer nrNaoConformidade) {
        this.nrNaoConformidade = nrNaoConformidade;
    }

    public DateTime getDhInclusao() {
        return this.dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }

    public String getDsMotivoAbertura() {
		return dsMotivoAbertura;
	}

	public void setDsMotivoAbertura(String dsMotivoAbertura) {
		this.dsMotivoAbertura = dsMotivoAbertura;
	}

    public DomainValue getTpStatusNaoConformidade() {
        return this.tpStatusNaoConformidade;
    }

    public void setTpStatusNaoConformidade(DomainValue tpStatusNaoConformidade) {
        this.tpStatusNaoConformidade = tpStatusNaoConformidade;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.vendas.model.Cliente getClienteByIdClienteDestinatario() {
		return clienteByIdClienteDestinatario;
	}

	public void setClienteByIdClienteDestinatario(
			com.mercurio.lms.vendas.model.Cliente clienteByIdClienteDestinatario) {
		this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
	}

	public com.mercurio.lms.vendas.model.Cliente getClienteByIdClienteRemetente() {
		return clienteByIdClienteRemetente;
	}

	public void setClienteByIdClienteRemetente(
			com.mercurio.lms.vendas.model.Cliente clienteByIdClienteRemetente) {
		this.clienteByIdClienteRemetente = clienteByIdClienteRemetente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ItemMda.class)     
	public List getItensMda() {
		return itensMda;
	}

	public void setItensMda(List itensMda) {
		this.itensMda = itensMda;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.portaria.model.NaoConformidadeAuditoria.class)     
    public List getNaoConformidadeAuditorias() {
        return this.naoConformidadeAuditorias;
    }

    public void setNaoConformidadeAuditorias(List naoConformidadeAuditorias) {
        this.naoConformidadeAuditorias = naoConformidadeAuditorias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade.class)     
    public List getOcorrenciaNaoConformidades() {
        return this.ocorrenciaNaoConformidades;
    }

    public void setOcorrenciaNaoConformidades(List ocorrenciaNaoConformidades) {
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNaoConformidade",
				getIdNaoConformidade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NaoConformidade))
			return false;
        NaoConformidade castOther = (NaoConformidade) other;
		return new EqualsBuilder().append(this.getIdNaoConformidade(),
				castOther.getIdNaoConformidade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNaoConformidade())
            .toHashCode();
    }

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public Awb getAwb() {
		return awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public DomainValue getCausadorRnc() {
		return causadorRnc;
	}

	public void setCausadorRnc(DomainValue causadorRnc) {
		this.causadorRnc = causadorRnc;
	}
}
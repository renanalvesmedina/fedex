package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class CtoCtoCooperada implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idCtoCtoCooperada;

    /** persistent field */
    private Integer nrCtoCooperada;

    /** nullable persistent field */
    private BigDecimal psAforado;

    /** nullable persistent field */
    private BigDecimal vlFrete;

    /** nullable persistent field */
    private Integer qtNfs;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** nullable persistent field */
    private YearMonthDay dtPrevisaoEntrega;

    /** nullable persistent field */
    private DateTime dhInclusao;

    /** nullable persistent field */
    private DomainValue tpFrete;

    /** nullable persistent field */
    private DomainValue tpConhecimento;

    /** nullable persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private String inscricaoEstadualRemetente;

    /** persistent field */
    private String inscricaoEstadualDestinatario;

    /** persistent field */
    private String inscricaoEstadualConsignatario;

    /** persistent field */
    private String inscricaoEstadualRedespacho;

    /** persistent field */
    private String inscricaoEstadualResponsavel;

    /** persistent field */
    private Cliente clienteByIdDestinatario;

    /** persistent field */
    private Cliente clienteByIdRedespacho;

    /** persistent field */
    private Cliente clienteByIdDevedor;

    /** persistent field */
    private Cliente clienteByIdConsignatario;

    /** persistent field */
    private Cliente clienteByIdRemetente;

    /** persistent field */
    private Embalagem embalagem;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private NaturezaProduto naturezaProduto;

    /** persistent field */
    private Conhecimento conhecimento;

    /** persistent field */
    private Filial filialByIdFilialRedespacho;

    /** persistent field */
    private Filial filialByIdFilial;

    /** persistent field */
    private List<NotaFiscalCtoCooperada> notaFiscalCtoCooperadas;

    public Long getIdCtoCtoCooperada() {
        return this.idCtoCtoCooperada;
    }

    public void setIdCtoCtoCooperada(Long idCtoCtoCooperada) {
        this.idCtoCtoCooperada = idCtoCtoCooperada;
    }

    public Integer getNrCtoCooperada() {
        return this.nrCtoCooperada;
    }

    public void setNrCtoCooperada(Integer nrCtoCooperada) {
        this.nrCtoCooperada = nrCtoCooperada;
    }

    public BigDecimal getPsAforado() {
        return this.psAforado;
    }

    public void setPsAforado(BigDecimal psAforado) {
        this.psAforado = psAforado;
    }

    public BigDecimal getVlFrete() {
        return this.vlFrete;
    }

    public void setVlFrete(BigDecimal vlFrete) {
        this.vlFrete = vlFrete;
    }

    public Integer getQtNfs() {
        return this.qtNfs;
    }

    public void setQtNfs(Integer qtNfs) {
        this.qtNfs = qtNfs;
    }

    public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public YearMonthDay getDtPrevisaoEntrega() {
		return dtPrevisaoEntrega;
	}

	public void setDtPrevisaoEntrega(YearMonthDay dtPrevisaoEntrega) {
		this.dtPrevisaoEntrega = dtPrevisaoEntrega;
	}

	public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
    }

    public DomainValue getTpConhecimento() {
        return this.tpConhecimento;
    }

    public void setTpConhecimento(DomainValue tpConhecimento) {
        this.tpConhecimento = tpConhecimento;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public String getInscricaoEstadualRemetente() {
        return this.inscricaoEstadualRemetente;
    }

    public void setInscricaoEstadualRemetente(String inscricaoEstadualRemetente) {
        this.inscricaoEstadualRemetente = inscricaoEstadualRemetente;
    }

    public String getInscricaoEstadualDestinatario() {
        return this.inscricaoEstadualDestinatario;
    }

	public void setInscricaoEstadualDestinatario(
			String inscricaoEstadualDestinatario) {
        this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
    }

    public String getInscricaoEstadualConsignatario() {
        return this.inscricaoEstadualConsignatario;
    }

	public void setInscricaoEstadualConsignatario(
			String inscricaoEstadualConsignatario) {
        this.inscricaoEstadualConsignatario = inscricaoEstadualConsignatario;
    }

    public String getInscricaoEstadualRedespacho() {
        return this.inscricaoEstadualRedespacho;
    }

	public void setInscricaoEstadualRedespacho(
			String inscricaoEstadualRedespacho) {
        this.inscricaoEstadualRedespacho = inscricaoEstadualRedespacho;
    }

    public String getInscricaoEstadualResponsavel() {
        return this.inscricaoEstadualResponsavel;
    }

	public void setInscricaoEstadualResponsavel(
			String inscricaoEstadualResponsavel) {
        this.inscricaoEstadualResponsavel = inscricaoEstadualResponsavel;
    }

    public Cliente getClienteByIdDestinatario() {
        return this.clienteByIdDestinatario;
    }

    public void setClienteByIdDestinatario(Cliente clienteByIdDestinatario) {
        this.clienteByIdDestinatario = clienteByIdDestinatario;
    }

    public Cliente getClienteByIdRedespacho() {
        return this.clienteByIdRedespacho;
    }

    public void setClienteByIdRedespacho(Cliente clienteByIdRedespacho) {
        this.clienteByIdRedespacho = clienteByIdRedespacho;
    }

    public Cliente getClienteByIdDevedor() {
        return this.clienteByIdDevedor;
    }

    public void setClienteByIdDevedor(Cliente clienteByIdDevedor) {
        this.clienteByIdDevedor = clienteByIdDevedor;
    }

    public Cliente getClienteByIdConsignatario() {
        return this.clienteByIdConsignatario;
    }

    public void setClienteByIdConsignatario(Cliente clienteByIdConsignatario) {
        this.clienteByIdConsignatario = clienteByIdConsignatario;
    }

    public Cliente getClienteByIdRemetente() {
        return this.clienteByIdRemetente;
    }

    public void setClienteByIdRemetente(Cliente clienteByIdRemetente) {
        this.clienteByIdRemetente = clienteByIdRemetente;
    }

    public Embalagem getEmbalagem() {
        return this.embalagem;
    }

    public void setEmbalagem(Embalagem embalagem) {
        this.embalagem = embalagem;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

    public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public Conhecimento getConhecimento() {
        return this.conhecimento;
    }

    public void setConhecimento(Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public Filial getFilialByIdFilialRedespacho() {
        return this.filialByIdFilialRedespacho;
    }

    public void setFilialByIdFilialRedespacho(Filial filialByIdFilialRedespacho) {
        this.filialByIdFilialRedespacho = filialByIdFilialRedespacho;
    }

    public Filial getFilialByIdFilial() {
        return this.filialByIdFilial;
    }

    public void setFilialByIdFilial(Filial filialByIdFilial) {
        this.filialByIdFilial = filialByIdFilial;
    }

    @ParametrizedAttribute(type = NotaFiscalCtoCooperada.class)     
    public List<NotaFiscalCtoCooperada> getNotaFiscalCtoCooperadas() {
        return this.notaFiscalCtoCooperadas;
    }

	public void setNotaFiscalCtoCooperadas(
			List<NotaFiscalCtoCooperada> notaFiscalCtoCooperadas) {
        this.notaFiscalCtoCooperadas = notaFiscalCtoCooperadas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCtoCtoCooperada",
				getIdCtoCtoCooperada()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CtoCtoCooperada))
			return false;
        CtoCtoCooperada castOther = (CtoCtoCooperada) other;
		return new EqualsBuilder().append(this.getIdCtoCtoCooperada(),
				castOther.getIdCtoCtoCooperada()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCtoCtoCooperada())
            .toHashCode();
    }

}

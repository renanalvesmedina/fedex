package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.vendas.model.Cotacao;

/** @author LMS Custom Hibernate CodeGenerator */
public class LiberacaoDocServ implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idLiberacaoDocServ;

	/** nullable persistent field */
	private String obLiberacao;

	/** persistent field */
	private DomainValue tpBloqueioLiberado;

	/** persistent field */
	private Usuario usuario;

	/** nullable persistent field */
	private DoctoServico doctoServico;

	/** nullable persistent field */
	private Cotacao cotacao;

	/** nullable persistent field */
	private TipoLiberacaoEmbarque tipoLiberacaoEmbarque;

	public LiberacaoDocServ() {
	}

	public LiberacaoDocServ(Long idLiberacaoDocServ, String obLiberacao, DomainValue tpBloqueioLiberado, Usuario usuario, DoctoServico doctoServico, Cotacao cotacao, TipoLiberacaoEmbarque tipoLiberacaoEmbarque) {
		this.idLiberacaoDocServ = idLiberacaoDocServ;
		this.obLiberacao = obLiberacao;
		this.tpBloqueioLiberado = tpBloqueioLiberado;
		this.usuario = usuario;
		this.doctoServico = doctoServico;
		this.cotacao = cotacao;
		this.tipoLiberacaoEmbarque = tipoLiberacaoEmbarque;
	}

	public Long getIdLiberacaoDocServ() {
		return this.idLiberacaoDocServ;
	}

	public void setIdLiberacaoDocServ(Long idLiberacaoDocServ) {
		this.idLiberacaoDocServ = idLiberacaoDocServ;
	}

	public String getObLiberacao() {
		return this.obLiberacao;
	}

	public void setObLiberacao(String obLiberacao) {
		this.obLiberacao = obLiberacao;
	}

	public DomainValue getTpBloqueioLiberado() {
		return this.tpBloqueioLiberado;
	}

	public void setTpBloqueioLiberado(DomainValue tpBloqueioLiberado) {
		this.tpBloqueioLiberado = tpBloqueioLiberado;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public TipoLiberacaoEmbarque getTipoLiberacaoEmbarque() {
		return this.tipoLiberacaoEmbarque;
	}

	public void setTipoLiberacaoEmbarque(
			TipoLiberacaoEmbarque tipoLiberacaoEmbarque) {
		this.tipoLiberacaoEmbarque = tipoLiberacaoEmbarque;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idLiberacaoDocServ",
				getIdLiberacaoDocServ()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LiberacaoDocServ))
			return false;
		LiberacaoDocServ castOther = (LiberacaoDocServ) other;
		return new EqualsBuilder().append(this.getIdLiberacaoDocServ(),
				castOther.getIdLiberacaoDocServ()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdLiberacaoDocServ())
			.toHashCode();
	}

}

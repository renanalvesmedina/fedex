package com.mercurio.lms.pendencia.model;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;

/** @author Hibernate CodeGenerator */
public class Mda extends DoctoServico {

	private static final long serialVersionUID = 1L;

    /** persistent field */
    private DomainValue tpDestinatarioMda;

    /** persistent field */
    private DomainValue tpRemetenteMda;

    /** persistent field */
    private DomainValue tpStatusMda;

    /** nullable persistent field */
    private String obMda;

    /** nullable persistent field */
    private String nmRecebedorCliente;

    /** nullable persistent field */
    private DateTime dhRecebimentoMatriz;

    /** nullable persistent field */
    private DateTime dhRecebimento;

    /** nullable persistent field */
    private DateTime dhCancelamento;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.EnderecoEstoque enderecoEstoque;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecMz;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioGeradaPor;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecebidoPor;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioEmitidoPor;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioDestino;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Setor setor;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoRemetente;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoDestinatario;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoConsignatario;    
    
    /** persistent field */
    private List itemMdas;

    public DomainValue getTpDestinatarioMda() {
        return this.tpDestinatarioMda;
    }

    public void setTpDestinatarioMda(DomainValue tpDestinatarioMda) {
        this.tpDestinatarioMda = tpDestinatarioMda;
    }

    public DomainValue getTpRemetenteMda() {
        return this.tpRemetenteMda;
    }

    public void setTpRemetenteMda(DomainValue tpRemetenteMda) {
        this.tpRemetenteMda = tpRemetenteMda;
    }

    public DomainValue getTpStatusMda() {
        return this.tpStatusMda;
    }

    public void setTpStatusMda(DomainValue tpStatusMda) {
        this.tpStatusMda = tpStatusMda;
    }

    public String getObMda() {
        return this.obMda;
    }

    public void setObMda(String obMda) {
        this.obMda = obMda;
    }

    public String getNmRecebedorCliente() {
        return this.nmRecebedorCliente;
    }

    public void setNmRecebedorCliente(String nmRecebedorCliente) {
        this.nmRecebedorCliente = nmRecebedorCliente;
    }

    public DateTime getDhRecebimentoMatriz() {
        return this.dhRecebimentoMatriz;
    }

    public void setDhRecebimentoMatriz(DateTime dhRecebimentoMatriz) {
        this.dhRecebimentoMatriz = dhRecebimentoMatriz;
    }

    public DateTime getDhRecebimento() {
        return this.dhRecebimento;
    }

    public void setDhRecebimento(DateTime dhRecebimento) {
        this.dhRecebimento = dhRecebimento;
    }

    public DateTime getDhCancelamento() {
        return this.dhCancelamento;
    }

    public void setDhCancelamento(DateTime dhCancelamento) {
        this.dhCancelamento = dhCancelamento;
    }

    public com.mercurio.lms.pendencia.model.EnderecoEstoque getEnderecoEstoque() {
        return this.enderecoEstoque;
    }

	public void setEnderecoEstoque(
			com.mercurio.lms.pendencia.model.EnderecoEstoque enderecoEstoque) {
        this.enderecoEstoque = enderecoEstoque;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioRecMz() {
        return this.usuarioByIdUsuarioRecMz;
    }

	public void setUsuarioByIdUsuarioRecMz(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecMz) {
        this.usuarioByIdUsuarioRecMz = usuarioByIdUsuarioRecMz;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioGeradaPor() {
        return this.usuarioByIdUsuarioGeradaPor;
    }

	public void setUsuarioByIdUsuarioGeradaPor(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioGeradaPor) {
        this.usuarioByIdUsuarioGeradaPor = usuarioByIdUsuarioGeradaPor;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioRecebidoPor() {
        return this.usuarioByIdUsuarioRecebidoPor;
    }

	public void setUsuarioByIdUsuarioRecebidoPor(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecebidoPor) {
        this.usuarioByIdUsuarioRecebidoPor = usuarioByIdUsuarioRecebidoPor;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioEmitidoPor() {
        return this.usuarioByIdUsuarioEmitidoPor;
    }

	public void setUsuarioByIdUsuarioEmitidoPor(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioEmitidoPor) {
        this.usuarioByIdUsuarioEmitidoPor = usuarioByIdUsuarioEmitidoPor;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioDestino() {
        return this.usuarioByIdUsuarioDestino;
    }

	public void setUsuarioByIdUsuarioDestino(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioDestino) {
        this.usuarioByIdUsuarioDestino = usuarioByIdUsuarioDestino;
    }

    public com.mercurio.lms.configuracoes.model.Setor getSetor() {
        return this.setor;
    }

    public void setSetor(com.mercurio.lms.configuracoes.model.Setor setor) {
        this.setor = setor;
    }

    public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoDestinatario() {
		return enderecoDestinatario;
	}

	public void setEnderecoDestinatario(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoDestinatario) {
		this.enderecoDestinatario = enderecoDestinatario;
	}

	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoRemetente() {
		return enderecoRemetente;
	}

	public void setEnderecoRemetente(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoRemetente) {
		this.enderecoRemetente = enderecoRemetente;
	}
	
	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoConsignatario() {
		return enderecoConsignatario;
	}

	public void setEnderecoConsignatario(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoConsignatario) {
		this.enderecoConsignatario = enderecoConsignatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ItemMda.class)     
    public List getItemMdas() {
        return this.itemMdas;
    }

    public void setItemMdas(List itemMdas) {
        this.itemMdas = itemMdas;
    }

}

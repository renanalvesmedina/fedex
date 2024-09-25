package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

@Entity
@Table(name = "DIVISAO_PARCELA")
@SequenceGenerator(name = "DIVISAO_PARCELA_SEQ", sequenceName = "DIVISAO_PARCELA_SQ", allocationSize = 1)
public class DivisaoParcela implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DIVISAO_PARCELA_SEQ")
	@Column(name = "ID_DIVISAO_PARCELA", nullable = false)
	private Long idDivisaoParcela;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_DIVISAO_CLIENTE", nullable = false)
	private TabelaDivisaoCliente tabelaDivisaoCliente;

	@ManyToOne
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = false)
	private ParcelaPreco parcelaPreco;

	@ManyToOne
	@JoinColumn(name = "ID_PARCELA_PRECO_COBRANCA", nullable = false)
	private ParcelaPreco parcelaPrecoCobranca;

	@Column(name = "TP_SITUACAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO"), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	public DivisaoParcela() {
		super();
	}

	public DivisaoParcela(Long idDivisaoParcela, TabelaDivisaoCliente tabelaDivisaoCliente, ParcelaPreco parcelaPreco, ParcelaPreco parcelaPrecoCobranca,
			DomainValue tpSituacao, UsuarioLMS usuarioInclusao, UsuarioLMS usuarioAlteracao, DateTime dhInclusao, DateTime dhAlteracao) {
		super();
		this.idDivisaoParcela = idDivisaoParcela;
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
		this.parcelaPreco = parcelaPreco;
		this.parcelaPrecoCobranca = parcelaPrecoCobranca;
		this.tpSituacao = tpSituacao;
		this.usuarioInclusao = usuarioInclusao;
		this.usuarioAlteracao = usuarioAlteracao;
		this.dhInclusao = dhInclusao;
		this.dhAlteracao = dhAlteracao;
	}

	public Long getIdDivisaoParcela() {
		return idDivisaoParcela;
	}

	public void setIdDivisaoParcela(Long idDivisaoParcela) {
		this.idDivisaoParcela = idDivisaoParcela;
	}

	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
		return tabelaDivisaoCliente;
	}

	public void setTabelaDivisaoCliente(TabelaDivisaoCliente tabelaDivisaoCliente) {
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public ParcelaPreco getParcelaPrecoCobranca() {
		return parcelaPrecoCobranca;
	}

	public void setParcelaPrecoCobranca(ParcelaPreco parcelaPrecoCobranca) {
		this.parcelaPrecoCobranca = parcelaPrecoCobranca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDivisaoParcela == null) ? 0 : idDivisaoParcela.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DivisaoParcela other = (DivisaoParcela) obj;
		if (idDivisaoParcela == null) {
			if (other.idDivisaoParcela != null) {
				return false;
			}
		} else if (!idDivisaoParcela.equals(other.idDivisaoParcela)) {
			return false;
		}
		return true;
	}

}

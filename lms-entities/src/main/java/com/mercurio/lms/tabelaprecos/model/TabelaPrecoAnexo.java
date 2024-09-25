package com.mercurio.lms.tabelaprecos.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "TABELA_PRECO_ANEXO")
@SequenceGenerator(name = "TABELA_PRECO_ANEXO_SEQ", sequenceName = "TABELA_PRECO_ANEXO_SQ", allocationSize = 1)
public class TabelaPrecoAnexo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_TABELA_PRECO_ANEXO", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABELA_PRECO_ANEXO_SEQ")
	private Long idTabelaPrecoAnexo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TABELA_PRECO", nullable = false)
	private TabelaPreco tabelaPreco;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;
	
	@Column(name = "DS_ANEXO", nullable = false, length = 100)
	private String dsAnexo;
	
	@Lob
	@Column(name="DC_ARQUIVO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;

	@Formula("TRIM(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(dc_arquivo, 1024)))")
	private String nmArquivo;
	
	@Columns(columns = { @Column(name = "DH_CRIACAO", nullable = false), @Column(name = "DH_CRIACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCriacao;
	
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaPrecoAnexo", getIdTabelaPrecoAnexo()).toString();
	}

	public boolean equals(Object other) {
		if (this == other){
			return true;
		}
		
		if (!(other instanceof TabelaPrecoAnexo)){
			return false;
		}
		
		TabelaPrecoAnexo castOther = (TabelaPrecoAnexo) other;
		return new EqualsBuilder().append(this.getIdTabelaPrecoAnexo(), castOther.getIdTabelaPrecoAnexo()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaPrecoAnexo()).toHashCode();
	}

	public Long getIdTabelaPrecoAnexo() {
		return idTabelaPrecoAnexo;
	}

	public void setIdTabelaPrecoAnexo(Long idTabelaPrecoAnexo) {
		this.idTabelaPrecoAnexo = idTabelaPrecoAnexo;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

	public void setNmArquivo(String nmArquivo) {
		this.nmArquivo = nmArquivo;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}
}

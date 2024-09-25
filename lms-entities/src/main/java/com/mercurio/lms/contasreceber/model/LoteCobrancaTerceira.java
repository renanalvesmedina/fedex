package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name="LOTE_COBRANCA_TERCEIRA")
public class LoteCobrancaTerceira implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_LOTE_COBRANCA_TERCEIRA")
    @SequenceGenerator(name = "LOTE_COBRANCA_TERCEIRA_SQ", sequenceName = "LOTE_COBRANCA_TERCEIRA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOTE_COBRANCA_TERCEIRA_SQ")
	private Long idLoteCobrancaTerceira;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;
	
	@Column(name = "TP_LOTE", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_LOTE_COB_TERCEIRA") })
	private DomainValue tpLote;
	
	@Column(name="NR_LOTE")
	private String nrLote;
	
	@Column(name="DS_LOTE")
	private String dsLote;
	
	@Lob
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	@Column(name="DC_ARQUIVO")
	private byte[] dcArquivo;
	
	@Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
	
	@Columns(columns = { @Column(name = "DH_ENVIO", nullable = true), @Column(name = "DH_ENVIO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEnvio;

	public Long getIdLoteCobrancaTerceira() {
		return idLoteCobrancaTerceira;
	}
	public void setIdLoteCobrancaTerceira(Long idLoteCobrancaTerceira) {
		this.idLoteCobrancaTerceira = idLoteCobrancaTerceira;
	}
	public UsuarioLMS getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}
	public DomainValue getTpLote() {
		return tpLote;
	}
	public void setTpLote(DomainValue tpLote) {
		this.tpLote = tpLote;
	}
	public String getNrLote() {
		return nrLote;
	}
	public void setNrLote(String nrLote) {
		this.nrLote = nrLote;
	}
	public String getDsLote() {
		return dsLote;
	}
	public void setDsLote(String dsLote) {
		this.dsLote = dsLote;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public byte[] getDcArquivo() {
		return this.dcArquivo;
	}
	
	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}
	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
	public DateTime getDhEnvio() {
		return dhEnvio;
	}
	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}

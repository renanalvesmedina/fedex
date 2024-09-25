package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;


/**
 * The persistent class for the LOTE_SERASA database table.
 * 
 */
@Entity
@Table(name="LOTE_SERASA")
public class LoteSerasa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_LOTE_SERASA")
    @SequenceGenerator(name = "LOTE_SERASA_SEQ", sequenceName = "LOTE_SERASA_SQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOTE_SERASA_SEQ")
	private Long idLoteSerasa;

	@Lob
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	@Column(name="ARQUIVO_GERADO")
	private byte[] arquivoGerado;

	@Columns(columns = { @Column(name = "DH_GERACAO", nullable = true), @Column(name = "DH_GERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhGeracao;
	
	@Column(name="DS_LOTE")
	private String dsLote;
	
	@Column(name="NR_LOTE")
	private Long nrLote;

	@Column(name = "TP_LOTE", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_LOTE_SERASA") })
	private DomainValue tpLote;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "loteSerasa")
    private List<ItemLoteSerasa> itensLoteSerasa ;
	
	
	@Transient
	private DateTime dhGeracaoInicial;
	
	@Transient
	private DateTime dhGeracaoFinal;
	
	public LoteSerasa() {
	}

	public Long getIdLoteSerasa() {
		return idLoteSerasa;
	}

	public void setIdLoteSerasa(Long idLoteSerasa) {
		this.idLoteSerasa = idLoteSerasa;
	}

	public byte[] getArquivoGerado() {
		return arquivoGerado;
	}

	public void setArquivoGerado(byte[] arquivoGerado) {
		this.arquivoGerado = arquivoGerado;
	}

	public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public String getDsLote() {
		return dsLote;
	}

	public void setDsLote(String dsLote) {
		this.dsLote = dsLote;
	}

	public Long getNrLote() {
		return nrLote;
	}

	public void setNrLote(Long nrLote) {
		this.nrLote = nrLote;
	}
	

	public List<ItemLoteSerasa> getItensLoteSerasa() {
		return itensLoteSerasa;
	}

	public void setItensLoteSerasa(List<ItemLoteSerasa> itensLoteSerasa) {
		this.itensLoteSerasa = itensLoteSerasa;
	}

	public DomainValue getTpLote() {
		return tpLote;
	}

	public void setTpLote(DomainValue tpLote) {
		this.tpLote = tpLote;
	}
	
	public DateTime getDhGeracaoInicial() {
		return dhGeracaoInicial;
	}

	public void setDhGeracaoInicial(DateTime dhGeracaoInicial) {
		this.dhGeracaoInicial = dhGeracaoInicial;
	}

	public DateTime getDhGeracaoFinal() {
		return dhGeracaoFinal;
	}

	public void setDhGeracaoFinal(DateTime dhGeracaoFinal) {
		this.dhGeracaoFinal = dhGeracaoFinal;
	}

	@Override
	public String toString() {
		return "LoteSerasa [idLoteSerasa=" + idLoteSerasa + ", dsLote="
				+ dsLote + ", nrLote=" + nrLote + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idLoteSerasa == null) ? 0 : idLoteSerasa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoteSerasa other = (LoteSerasa) obj;
		if (idLoteSerasa == null) {
			if (other.idLoteSerasa != null)
				return false;
		} else if (!idLoteSerasa.equals(other.idLoteSerasa))
			return false;
		return true;
	}


}
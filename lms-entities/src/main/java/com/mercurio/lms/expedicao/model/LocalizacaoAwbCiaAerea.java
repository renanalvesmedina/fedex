package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Empresa;

/**
 * @author RuhanB
 *
 */
@Entity
@Table(name="LOCALIZACAO_CIA_AEREA")
@SequenceGenerator(name = "LOCALIZACAO_CIA_AEREA_SQ", sequenceName = "LOCALIZACAO_CIA_AEREA_SQ", allocationSize=1)
public class LocalizacaoAwbCiaAerea implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_LOCALIZACAO_CIA_AEREA", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCALIZACAO_CIA_AEREA_SQ")
	private Long idLocalizacaoAwbCiaAerea;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CIA_AEREA", nullable = false)
    private Empresa ciaAerea;
	
	@Column(name="DS_TRACKING", length=200, nullable=false)
	private String dsTracking;
	
	@Column(name = "TP_LOCALIZACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_LOCALIZACAO_AWB") })
	private DomainValue tpLocalizacaoCiaAerea;
	
	@Column(name = "TP_LOCALIZACAO_ATUAL", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_LOCALIZACAO_AWB") })
	private DomainValue tpLocalizacaoAtual;

	public Long getIdLocalizacaoAwbCiaAerea() {
		return idLocalizacaoAwbCiaAerea;
	}

	public void setIdLocalizacaoAwbCiaAerea(Long idLocalizacaoAwbCiaAerea) {
		this.idLocalizacaoAwbCiaAerea = idLocalizacaoAwbCiaAerea;
	}

	public Empresa getCiaAerea() {
		return ciaAerea;
	}

	public void setCiaAerea(Empresa ciaAerea) {
		this.ciaAerea = ciaAerea;
	}

	public String getDsTracking() {
		return dsTracking;
	}

	public void setDsTracking(String dsTracking) {
		this.dsTracking = dsTracking;
	}

	public DomainValue getTpLocalizacaoCiaAerea() {
		return tpLocalizacaoCiaAerea;
	}

	public void setTpLocalizacaoCiaAerea(DomainValue tpLocalizacaoCiaAerea) {
		this.tpLocalizacaoCiaAerea = tpLocalizacaoCiaAerea;
	}

	public DomainValue getTpLocalizacaoAtual() {
		return tpLocalizacaoAtual;
	}

	public void setTpLocalizacaoAtual(DomainValue tpLocalizacaoAtual) {
		this.tpLocalizacaoAtual = tpLocalizacaoAtual;
	}
}

package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "DE_PARA_XML_CTE")
public class DeParaXmlCte implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long idDeParaXmlCte;
	private Boolean blMatriz;
	private DomainValue tpCampo;
	private String nmTnt;
	private String nmCliente;
	private Cliente cliente;

	public DeParaXmlCte() {
	}

	@Column(name = "BL_MATRIZ", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlMatriz() {
		return blMatriz;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	@Id
	@SequenceGenerator(name = "DE_PARA_XML_CTE_SEQ", sequenceName = "DE_PARA_XML_CTE_SQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DE_PARA_XML_CTE_SEQ")
	@Column(name = "ID_DE_PARA_XML_CTE", nullable = false)
	public Long getIdDeParaXmlCte() {
		return idDeParaXmlCte;
	}

	@Column(name = "NM_CLIENTE", nullable = false, length = 50)
	public String getNmCliente() {
		return nmCliente;
	}

	@Column(name = "NM_TNT", nullable = false, length = 50)
	public String getNmTnt() {
		return nmTnt;
	}

	@Column(name = "TP_CAMPO", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_CAMPO_XML_CTE") })
	public DomainValue getTpCampo() {
		return tpCampo;
	}

	public void setBlMatriz(Boolean blMatriz) {
		this.blMatriz = blMatriz;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setIdDeParaXmlCte(Long idDeParaXmlCte) {
		this.idDeParaXmlCte = idDeParaXmlCte;
	}

	public void setNmCliente(String nmCliente) {
		this.nmCliente = nmCliente;
	}

	public void setNmTnt(String nmTnt) {
		this.nmTnt = nmTnt;
	}

	public void setTpCampo(DomainValue tpCampo) {
		this.tpCampo = tpCampo;
	}
}

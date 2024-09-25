package com.mercurio.lms.sim.model;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name="CONTATO_CCT")
@SequenceGenerator(name="SQ_CONTATO_CCT", sequenceName="CONTATO_CCT_SQ", allocationSize=1)
public class ContatoCCT implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_CONTATO_CCT")
	@Column(name="ID_CONTATO_CCT", nullable=false)
	private Long idContatoCCT;
	
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE_REM", nullable=true)
	private Cliente clienteRemetente;
	
	@ManyToOne
	@JoinColumn(name="ID_CLIENTE_DEST", nullable=true)
	private Cliente clienteDestinatario;
	
	@Column(name = "TP_PARAMETRIZACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_EMAIL") })
	private DomainValue tpParametrizacao;

	public Long getIdContatoCCT() {
		return idContatoCCT;
	}

	public void setIdContatoCCT(Long idContatoCCT) {
		this.idContatoCCT = idContatoCCT;
	}

	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	public Cliente getClienteDestinatario() {
		return clienteDestinatario;
	}

	public void setClienteDestinatario(Cliente clienteDestinatario) {
		this.clienteDestinatario = clienteDestinatario;
	}

	public DomainValue getTpParametrizacao() {
		return tpParametrizacao;
	}

	public void setTpParametrizacao(DomainValue tpParametrizacao) {
		this.tpParametrizacao = tpParametrizacao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	} 
	
}

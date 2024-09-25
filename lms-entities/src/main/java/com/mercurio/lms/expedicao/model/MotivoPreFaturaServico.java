package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="MOTIVO_PRE_FATURA_SERVICO")
@SequenceGenerator(name = "MOTIVO_PRE_FATURA_SERVICO_SQ", sequenceName = "MOTIVO_PRE_FATURA_SERVICO_SQ", allocationSize=1)
public class MotivoPreFaturaServico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_MOTIVO_PRE_FATURA_SERVICO", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MOTIVO_PRE_FATURA_SERVICO_SQ")
	private Long idMotivoPreFaturaServico;
	
	@Column(name="DS_MOTIVO_PRE_FATURA_SERVICO", nullable=false, length=50)
	private String dsMotivoPreFaturaServico;

	@Column(name = "TP_MOTIVO_PRE_FATURA", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_ITEM_PRE_FATURA") })
	private DomainValue tpMotivoPreFatura; 
	
	public Long getIdMotivoPreFaturaServico() {
		return idMotivoPreFaturaServico;
	}
	public void setIdMotivoPreFaturaServico(Long idMotivoPreFaturaServico) {
		this.idMotivoPreFaturaServico = idMotivoPreFaturaServico;
	}

	public String getDsMotivoPreFaturaServico() {
		return dsMotivoPreFaturaServico;
	}
	public void setDsMotivoPreFaturaServico(String dsMotivoPreFaturaServico) {
		this.dsMotivoPreFaturaServico = dsMotivoPreFaturaServico;
	}

	public DomainValue getTpMotivoPreFatura() {
		return tpMotivoPreFatura;
	}
	public void setTpMotivoPreFatura(DomainValue tpMotivoPreFatura) {
		this.tpMotivoPreFatura = tpMotivoPreFatura;
	}
}

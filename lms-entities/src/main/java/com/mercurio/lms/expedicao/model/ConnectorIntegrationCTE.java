package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author JonasFE
 *
 */
@Entity
@Table(name="CONNECTORINTEGRATION_CTE")
@SequenceGenerator(name="connectorIntegration_CTE_SEQ", sequenceName="connectorIntegration_CTE_SEQ", allocationSize=1)
public class ConnectorIntegrationCTE implements Serializable {
	private static final long serialVersionUID = 1L;

	// este campo é maior que um Long na base, deve se tomar cuidado. Passivel
	// de refactor no futuro
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "connectorIntegration_CTE_SEQ")
	@Column(name = "CONNECTORINTEGRATION_CTEID", nullable = false)
	private Long connectorIntegrationCTEID;

	@Column(name="STATUS", length=5, nullable=false)
	private Integer status;
	
	@Column(name="KIND", length=5, nullable=false)
	private Integer kind;
	
	@Lob
	@Column(name="DOCUMENTDATA", nullable = false)
	private byte[] documentData;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getKind() {
		return kind;
	}

	public void setKind(Integer kind) {
		this.kind = kind;
	}

	public byte[] getDocumentData() {
		return documentData;
	}

	public void setDocumentData(byte[] documentData) {
		this.documentData = documentData;
	}

	public Long getConnectorIntegrationCTEID() {
		return connectorIntegrationCTEID;
	}

	public void setConnectorIntegrationCTEID(Long connectorIntegrationCTEID) {
		this.connectorIntegrationCTEID = connectorIntegrationCTEID;
	}

}

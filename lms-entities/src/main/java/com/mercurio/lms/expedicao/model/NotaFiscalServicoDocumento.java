package com.mercurio.lms.expedicao.model;

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

@Entity
@Table(name="NOTA_FISCAL_SERVICO_DOCUMENTO")
@SequenceGenerator(name = "NOTA_FISCAL_SERVICO_DOCUMEN_SQ", sequenceName = "NOTA_FISCAL_SERVICO_DOCUMEN_SQ", allocationSize=1)
public class NotaFiscalServicoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_NOTA_FISCAL_SERVICO_DOCUMEN", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_FISCAL_SERVICO_DOCUMEN_SQ")
	private Long idNotaFiscalServicoDocumento;
	 
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_NOTA_FISCAL_SERVICO", nullable = false)
	private NotaFiscalServico notaFiscalServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PRE_FATURA_SERVICO_ITEM", nullable = false)
	private PreFaturaServicoItem preFaturaServicoItem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;

	public Long getIdNotaFiscalServicoDocumento() {
		return idNotaFiscalServicoDocumento;
	}

	public void setIdNotaFiscalServicoDocumento(Long idNotaFiscalServicoDocumento) {
		this.idNotaFiscalServicoDocumento = idNotaFiscalServicoDocumento;
	}

	public NotaFiscalServico getNotaFiscalServico() {
		return notaFiscalServico;
	}

	public void setNotaFiscalServico(NotaFiscalServico notaFiscalServico) {
		this.notaFiscalServico = notaFiscalServico;
	}

	public PreFaturaServicoItem getPreFaturaServicoItem() {
		return preFaturaServicoItem;
	}

	public void setPreFaturaServicoItem(PreFaturaServicoItem preFaturaServicoItem) {
		this.preFaturaServicoItem = preFaturaServicoItem;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idNotaFiscalServicoDocumento == null) ? 0
						: idNotaFiscalServicoDocumento.hashCode());
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
		NotaFiscalServicoDocumento other = (NotaFiscalServicoDocumento) obj;
		if (idNotaFiscalServicoDocumento == null) {
			if (other.idNotaFiscalServicoDocumento != null)
				return false;
		} else if (!idNotaFiscalServicoDocumento
				.equals(other.idNotaFiscalServicoDocumento))
			return false;
		return true;
	}
}
package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name = "NOTA_CREDITO_CALC_PAD_DOCTO")
@SequenceGenerator(name = "NOTA_CREDITO_CALC_PAD_DOCTO_SQ", sequenceName = "NOTA_CREDITO_CALC_PAD_DOCTO_SQ", allocationSize = 1)
public class NotaCreditoCalcPadraoDocto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_CREDITO_CALC_PAD_DOCTO_SQ")
	@Column(name = "ID_NOTA_CREDITO_CALC_PAD_DOCTO", nullable = false)
	private Long idNotaCreditoCalcPadraoDocto;

	@ManyToOne
	@JoinColumn(name = "ID_NOTA_CREDITO")
	private NotaCredito notaCredito;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_FC_VALORES")
	private TabelaFcValores tabelaFcValores;

	@ManyToOne
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;

	@ManyToOne
	@JoinColumn(name = "ID_PEDIDO_COLETA")
	private PedidoColeta pedidoColeta;
	
	@Column(name = "BL_CALCULADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blCalculado;
	
	@Column(name = "QT_VOLUMES_CALCULADO", length = 6)
    private BigDecimal qtVolumesCalculado;
	
	@Column(name = "PS_CALCULADO")
    private BigDecimal psCalculado;
	
	@Column(name = "VL_MERCAD_CALCULADO")
    private BigDecimal vlMercadCalculado;

	public Long getIdNotaCreditoCalcPadraoDocto() {
		return this.idNotaCreditoCalcPadraoDocto;
	}

	public void setIdNotaCreditoCalcPadraoDocto(
			Long idNotaCreditoCalcPadraoDocto) {
		this.idNotaCreditoCalcPadraoDocto = idNotaCreditoCalcPadraoDocto;
	}

	public NotaCredito getNotaCredito() {
		return this.notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public TabelaFcValores getTabelaFcValores() {
		return this.tabelaFcValores;
	}

	public void setTabelaFcValores(TabelaFcValores tabelaFcValores) {
		this.tabelaFcValores = tabelaFcValores;
	}

	public DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public PedidoColeta getPedidoColeta() {
		return this.pedidoColeta;
	}

	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

	public DomainValue getBlCalculado() {
		return blCalculado;
	}

	public void setBlCalculado(DomainValue blCalculado) {
		this.blCalculado = blCalculado;
	}
	
	public BigDecimal getQtVolumesCalculado() {
        return qtVolumesCalculado;
    }

    public void setQtVolumesCalculado(BigDecimal qtVolumesCalculado) {
        this.qtVolumesCalculado = qtVolumesCalculado;
    }

    public BigDecimal getPsCalculado() {
        return psCalculado;
    }

    public void setPsCalculado(BigDecimal psCalculado) {
        this.psCalculado = psCalculado;
    }

    public BigDecimal getVlMercadCalculado() {
        return vlMercadCalculado;
    }

    public void setVlMercadCalculado(BigDecimal vlMercadCalculado) {
        this.vlMercadCalculado = vlMercadCalculado;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idNotaCreditoCalcPadraoDocto == null) ? 0
						: idNotaCreditoCalcPadraoDocto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof NotaCreditoCalcPadraoDocto)) {
			return false;
		}

		NotaCreditoCalcPadraoDocto castOther = (NotaCreditoCalcPadraoDocto) obj;

		return new EqualsBuilder().append(this.getIdNotaCreditoCalcPadraoDocto(),
				castOther.getIdNotaCreditoCalcPadraoDocto()).isEquals();
	}

}
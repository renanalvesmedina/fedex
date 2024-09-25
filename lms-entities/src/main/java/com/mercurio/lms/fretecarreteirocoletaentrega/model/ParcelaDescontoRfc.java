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
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;

@Entity
@Table(name = "PARCELA_DESCONTO_RFC")
@SequenceGenerator(name = "PARCELA_DESCONTO_RFC_SQ", sequenceName = "PARCELA_DESCONTO_RFC_SQ", allocationSize = 1)
public class ParcelaDescontoRfc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARCELA_DESCONTO_RFC_SQ")
	@Column(name = "ID_PARCELA_DESCONTO_RFC", nullable = false)
	private Long idParcelaDescontoRfc;
	
	@Column(name="NR_PARCELA", nullable = false, length = 6)
	private Integer nrParcela;
	
	@Column(name="VL_PARCELA", nullable = false)
	private BigDecimal vlParcela;
				
	@Column(name = "OB_PARCELA", length = 200)
	private String obParcela;
	
	@Column(name = "DT_PARCELA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtParcela;
	
	@ManyToOne
	@JoinColumn(name = "ID_DESCONTO_RFC", nullable = false)
	private DescontoRfc descontoRfc;
	
	@ManyToOne
	@JoinColumn(name = "ID_RECIBO_FRETE_CARRETEIRO")
	private ReciboFreteCarreteiro reciboFreteCarreteiro;

	public Long getIdParcelaDescontoRfc() {
		return idParcelaDescontoRfc;
	}

	public void setIdParcelaDescontoRfc(Long idParcelaDescontoRfc) {
		this.idParcelaDescontoRfc = idParcelaDescontoRfc;
	}

	public Integer getNrParcela() {
		return nrParcela;
	}

	public void setNrParcela(Integer nrParcela) {
		this.nrParcela = nrParcela;
	}

	public BigDecimal getVlParcela() {
		return vlParcela;
	}

	public void setVlParcela(BigDecimal vlParcela) {
		this.vlParcela = vlParcela;
	}

	public String getObParcela() {
		return obParcela;
	}

	public void setObParcela(String obParcela) {
		this.obParcela = obParcela;
	}

	public YearMonthDay getDtParcela() {
		return dtParcela;
	}

	public void setDtParcela(YearMonthDay dtParcela) {
		this.dtParcela = dtParcela;
	}

	public DescontoRfc getDescontoRfc() {
		return descontoRfc;
	}

	public void setDescontoRfc(DescontoRfc descontoRfc) {
		this.descontoRfc = descontoRfc;
	}

	public ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idParcelaDescontoRfc == null) ? 0 : idParcelaDescontoRfc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		
		if (!(obj instanceof ParcelaDescontoRfc)){
			return false;
		}
		
		ParcelaDescontoRfc castOther = (ParcelaDescontoRfc) obj;
		
		return new EqualsBuilder().append(
				this.getIdParcelaDescontoRfc(), 
				castOther.getIdParcelaDescontoRfc()).isEquals();
	}
}
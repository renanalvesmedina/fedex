package com.mercurio.lms.fretecarreteiroviagem.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

/**
 * Parâmetros para a procedure 'P_MGC_CALC_IMP_RENDA'.
 * 
 */
public class CalculoReciboIRRFDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Parâmetros de <b>entrada</b> para procedure.
	 *
	 */
	public class IN {
		
		private Boolean atualizaIRRF;
		private String nrCpf;
		private Byte nrDependentes;
		private DateTime dhEmissaoRFC;
		private BigDecimal vlrMinimoIRRF;
		private BigDecimal vlrPerBaseCarreteiro;
		private BigDecimal vlrBruto;
		private BigDecimal vlrComplPago;
		private BigDecimal vlInssRFC;
		private BigDecimal vlIRRF;
		private BigDecimal pcAliquotaIRRF;
		
		public Boolean getAtualizaIRRF() {
			return atualizaIRRF;
		}

		public void setAtualizaIRRF(Boolean atualizaIRRF) {
			this.atualizaIRRF = atualizaIRRF;
		}

		public String getNrCpf() {
			return nrCpf;
		}

		public void setNrCpf(String nrCpf) {
			this.nrCpf = nrCpf;
		}

		public Byte getNrDependentes() {
			return nrDependentes;
		}

		public void setNrDependentes(Byte nrDependentes) {
			this.nrDependentes = nrDependentes;
		}

		public DateTime getDhEmissaoRFC() {
			return dhEmissaoRFC;
		}

		public void setDhEmissaoRFC(DateTime dhEmissaoRFC) {
			this.dhEmissaoRFC = dhEmissaoRFC;
		}

		public BigDecimal getVlrMinimoIRRF() {
			return vlrMinimoIRRF;
		}

		public void setVlrMinimoIRRF(BigDecimal vlrMinimoIRRF) {
			this.vlrMinimoIRRF = vlrMinimoIRRF;
		}

		public BigDecimal getVlrPerBaseCarreteiro() {
			return vlrPerBaseCarreteiro;
		}

		public void setVlrPerBaseCarreteiro(BigDecimal vlrPerBaseCarreteiro) {
			this.vlrPerBaseCarreteiro = vlrPerBaseCarreteiro;
		}

		public BigDecimal getVlrBruto() {
			return vlrBruto;
		}

		public void setVlrBruto(BigDecimal vlrBruto) {
			this.vlrBruto = vlrBruto;
		}

		public BigDecimal getVlrComplPago() {
			return vlrComplPago;
		}

		public void setVlrComplPago(BigDecimal vlrComplPago) {
			this.vlrComplPago = vlrComplPago;
		}

		public BigDecimal getVlInssRFC() {
			return vlInssRFC;
		}

		public void setVlInssRFC(BigDecimal vlInssRFC) {
			this.vlInssRFC = vlInssRFC;
		}

		public BigDecimal getVlIRRF() {
			return vlIRRF;
		}

		public void setVlIRRF(BigDecimal vlIRRF) {
			this.vlIRRF = vlIRRF;
		}

		public BigDecimal getPcAliquotaIRRF() {
			return pcAliquotaIRRF;
		}

		public void setPcAliquotaIRRF(BigDecimal pcAliquotaIRRF) {
			this.pcAliquotaIRRF = pcAliquotaIRRF;
		}
	}
	
	
	
	/**
	 * Parâmetros de <b>saída</b> para procedure.
	 *
	 */
	public class OUT {		
		
		private BigDecimal vlIRRF;
		private BigDecimal vlAliqIRRF;
		private BigDecimal vlAcumuloIRRF;
		
		public BigDecimal getVlIRRF() {
			return vlIRRF;
		}
		
		public void setVlIRRF(BigDecimal vlIRRF) {
			this.vlIRRF = vlIRRF;
		}
		
		public BigDecimal getVlAliqIRRF() {
			return vlAliqIRRF;
		}
		
		public void setVlAliqIRRF(BigDecimal vlAliqIRRF) {
			this.vlAliqIRRF = vlAliqIRRF;
		}

		public void setVlAcumuloIRRF(BigDecimal vlAcumuloIRRF) {
			this.vlAcumuloIRRF = vlAcumuloIRRF;
		}

		public BigDecimal getVlAcumuloIRRF() {
			return vlAcumuloIRRF;
		}
	}
}

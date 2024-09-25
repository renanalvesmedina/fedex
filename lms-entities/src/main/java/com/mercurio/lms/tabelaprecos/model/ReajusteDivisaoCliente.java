package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

@Entity
@Table(name="REAJUSTE_DIVISAO_CLIENTE")
@SequenceGenerator(name = "REAJUSTE_DIVISAO_CLIENTE_SQ", sequenceName = "REAJUSTE_DIVISAO_CLIENTE_SQ")
public class ReajusteDivisaoCliente implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name="ID_REAJUSTE_DIVISAO_CLIENTE")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REAJUSTE_DIVISAO_CLIENTE_SQ")
		private Long idReajusteDivisaoCliente;

		@Column(name="ID_DIVISAO_CLIENTE", updatable=false)
		private Long idDivisaoCliente;
		
		@Column(name="ID_TABELA_PRECO_NOVA", updatable=false)
		private Long idTabelaPrecoNova;
		
		@Column(name="DT_AGENDAMENTO_REAJUSTE")
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataAgendamentoReajuste;
		
		@Column(name="BL_REAJUSTADA", nullable=false)
		private String isReajustada;
		
		@Column(name="BL_PROCESSADO", nullable=false)
		private String isProcessado;
		
		@Column(name="BL_CLONADA", nullable=false)
		private String isClonada;
		
		public ReajusteDivisaoCliente() { 
		}

		public Long getIdReajusteDivisaoCliente() {
			return idReajusteDivisaoCliente;
		}

		public void setIdReajusteDivisaoCliente(Long idReajusteDivisaoCliente) {
			this.idReajusteDivisaoCliente = idReajusteDivisaoCliente;
		}

		public Long getIdDivisaoCliente() {
			return idDivisaoCliente;
		}

		public void setIdDivisaoCliente(Long idDivisaoCliente) {
			this.idDivisaoCliente = idDivisaoCliente;
		}

		public Long getIdTabelaPrecoNova() {
			return idTabelaPrecoNova;
		}

		public void setIdTabelaPrecoNova(Long idTabelaPrecoNova) {
			this.idTabelaPrecoNova = idTabelaPrecoNova;
		}

		public YearMonthDay getDataAgendamentoReajuste() {
			return dataAgendamentoReajuste;
		}

		public void setDataAgendamentoReajuste(YearMonthDay dataAgendamentoReajuste) {
			this.dataAgendamentoReajuste = dataAgendamentoReajuste;
		}

		public String getIsReajustada() {
			return isReajustada;
		}

		public void setIsReajustada(String isReajustada) {
			this.isReajustada = isReajustada;
		}

		public String getIsProcessado() {
			return isProcessado;
		}

		public void setIsProcessado(String isProcessado) {
			this.isProcessado = isProcessado;
		}

		public String getIsClonada() {
			return isClonada;
		}

		public void setIsClonada(String isClonada) {
			this.isClonada = isClonada;
		}
		
		

}

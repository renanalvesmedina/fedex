package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name="REAJUSTE_TABELA_PRECO")
@SequenceGenerator(name = "REAJUSTE_TABELA_PRECO_SQ", sequenceName = "REAJUSTE_TABELA_PRECO_SQ")
public class ReajusteTabelaPrecoEntity implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@Column(name="ID_REAJUSTE_TABELA_PRECO")
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REAJUSTE_TABELA_PRECO_SQ")
		private Long id;

		@Column(name="ID_TABELA_PRECO_BASE", updatable=false)
		private Long idTabelaPrecoBase;
		
		@Column(name="ID_TABELA_NOVA", updatable=false)
		private Long idTabelaNova;
		
		@Column(name="ID_FILIAL", updatable=false)
		private Long idFilial;
		
		@Column(name="NR_REAJUSTE", updatable=false)
		private Long nrReajuste;
		
		@Column(name="PC_REAJUSTE_GERAL")
		private BigDecimal percentualReajusteGeral;
		
		@Column(name="DT_VIGENCIA_INICIAL")
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataVigenciaInicial;
		
		@Column(name="DT_VIGENCIA_FINAL")
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataVigenciaFinal;
		
		@Column(name="ID_USUARIO", updatable=false)
		private Long idUsuario;
		
		@Column(name="DT_GERACAO", updatable=false)
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataGeracao;
		
		@Column(name="DT_AGENDAMENTO")
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataAgendamento;
		
		@Column(name="DT_EFETIVACAO", updatable=false)
		@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
		private YearMonthDay dataEfetivacao;
		
		@Column(name="BL_EFETIVADO", updatable=false)
		private String isEfetivado;
		
		@Column(name="BL_FECHA_VIGENCIA_TABELA_BASE")
		private String blFechaVigenciaTabelaBase;
		
		public ReajusteTabelaPrecoEntity() { 
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getIdTabelaPrecoBase() {
			return idTabelaPrecoBase;
		}

		public void setIdTabelaPrecoBase(Long idTabelaPrecoBase) {
			this.idTabelaPrecoBase = idTabelaPrecoBase;
		}

		public Long getIdTabelaNova() {
			return idTabelaNova;
		}

		public void setIdTabelaNova(Long idTabelaNova) {
			this.idTabelaNova = idTabelaNova;
		}

		public Long getIdFilial() {
			return idFilial;
		}

		public void setIdFilial(Long idFilial) {
			this.idFilial = idFilial;
		}

		public Long getNrReajuste() {
			return nrReajuste;
		}

		public void setNrReajuste(Long nrReajuste) {
			this.nrReajuste = nrReajuste;
		}

		public BigDecimal getPercentualReajusteGeral() {
			return percentualReajusteGeral;
		}

		public void setPercentualReajusteGeral(BigDecimal percentualReajusteGeral) {
			this.percentualReajusteGeral = percentualReajusteGeral;
		}

		public YearMonthDay getDataVigenciaInicial() {
			return dataVigenciaInicial;
		}

		public void setDataVigenciaInicial(YearMonthDay dataVigenciaInicial) {
			this.dataVigenciaInicial = dataVigenciaInicial;
		}

		public YearMonthDay getDataVigenciaFinal() {
			return dataVigenciaFinal;
		}

		public void setDataVigenciaFinal(YearMonthDay dataVigenciaFinal) {
			this.dataVigenciaFinal = dataVigenciaFinal;
		}

		public Long getIdUsuario() {
			return idUsuario;
		}

		public void setIdUsuario(Long idUsuario) {
			this.idUsuario = idUsuario;
		}

		public YearMonthDay getDataGeracao() {
			return dataGeracao;
		}

		public void setDataGeracao(YearMonthDay dataGeracao) {
			this.dataGeracao = dataGeracao;
		}

		public YearMonthDay getDataAgendamento() {
			return dataAgendamento;
		}

		public void setDataAgendamento(YearMonthDay dataAgendamento) {
			this.dataAgendamento = dataAgendamento;
		}

		public YearMonthDay getDataEfetivacao() {
			return dataEfetivacao;
		}

		public void setDataEfetivacao(YearMonthDay dataEfetivacao) {
			this.dataEfetivacao = dataEfetivacao;
		}

		public String getIsEfetivado() {
			return isEfetivado;
		}

		public void setIsEfetivado(String isEfetivado) {
			this.isEfetivado = isEfetivado;
		}
		
		public String isFechaVigenciaTabelaBase() {
			return blFechaVigenciaTabelaBase;
		}

		public void setFechaVigenciaTabelaBase(String blFechaVigenciaTabelaBase) {
			this.blFechaVigenciaTabelaBase = blFechaVigenciaTabelaBase;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

}

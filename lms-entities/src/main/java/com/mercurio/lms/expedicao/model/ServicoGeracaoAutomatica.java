package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

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

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name="SERVICO_GERACAO_AUTOMATICA")
@SequenceGenerator(name="SERVICO_GERACAO_AUTOMATICA_SQ", sequenceName="SERVICO_GERACAO_AUTOMATICA_SQ", allocationSize=1)
public class ServicoGeracaoAutomatica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVICO_GERACAO_AUTOMATICA_SQ")
	@Column(name = "ID_SERVICO_GERACAO_AUTOMATICA", nullable = false)
	private Long idServicoGeracaoAutomatica;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = false)
	private ParcelaPreco parcelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;	
	
	@Column(name="BL_FATURADO", nullable=false)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blFaturado;
	
	@Column(name="BL_SEM_COBRANCA", nullable=false)
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blSemCobranca;
	
	@Column(name="BL_FINALIZADO")
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blFinalizado;
	
	@Column(name="VL_SERVICO_ADICIONAL", nullable=false)
	private BigDecimal vlServicoAdicional;
	
	@Column(name="VL_TABELA")
	private BigDecimal vlTabela;
	
	@Column(name="QT_DIAS_COBRADOS")
	private Integer qtDiasCobrados;
	
	@Columns(columns = { @Column(name = "DH_CALCULO",  nullable=false), @Column(name = "DH_CALCULO_TZR",  nullable=false)})
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhCalculo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_EXECUCAO", nullable = false)
	private Municipio municipioExecucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_OCORRENCIA_PENDENCIA", nullable = true)
	private OcorrenciaPendencia ocorrenciaPendencia;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_EXECUCAO", nullable = false)
	private Filial filialExecucao;
	
	@Column(name="NR_DIAS_CARENCIA")
	private Integer nrDiasCarencia;
	
	@Column(name = "TP_EXECUCAO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_EXEC_SERV_GER_AUT") })
	private DomainValue tpExecucao;
		
	@Column(name = "DT_INICIO_PERMANENCIA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInicioPermanencia;
		
	@Column(name = "DT_FIM_PERMANENCIA")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtFimPermanencia;
	
	
	
	public Long getIdServicoGeracaoAutomatica() {
		return idServicoGeracaoAutomatica;
	}

	public void setIdServicoGeracaoAutomatica(Long idServicoGeracaoAutomatica) {
		this.idServicoGeracaoAutomatica = idServicoGeracaoAutomatica;
	}

	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Boolean getBlFaturado() {
		return blFaturado;
	}

	public void setBlFaturado(Boolean blFaturado) {
		this.blFaturado = blFaturado;
	}

	public Boolean getBlSemCobranca() {
		return blSemCobranca;
	}

	public void setBlSemCobranca(Boolean blSemCobranca) {
		this.blSemCobranca = blSemCobranca;
	}

	public Boolean getBlFinalizado() {
		return blFinalizado;
	}

	public void setBlFinalizado(Boolean blFinalizado) {
		this.blFinalizado = blFinalizado;
	}

	public BigDecimal getVlServicoAdicional() {
		return vlServicoAdicional;
	}

	public void setVlServicoAdicional(BigDecimal vlServicoAdicional) {
		this.vlServicoAdicional = vlServicoAdicional;
	}

	public BigDecimal getVlTabela() {
		return vlTabela;
	}

	public void setVlTabela(BigDecimal vlTabela) {
		this.vlTabela = vlTabela;
	}

	public Integer getQtDiasCobrados() {
		return qtDiasCobrados;
	}

	public void setQtDiasCobrados(Integer qtDiasCobrados) {
		this.qtDiasCobrados = qtDiasCobrados;
	}

	public DateTime getDhCalculo() {
		return dhCalculo;
	}

	public void setDhCalculo(DateTime dhCalculo) {
		this.dhCalculo = dhCalculo;
	}

	public Municipio getMunicipioExecucao() {
		return municipioExecucao;
	}

	public void setMunicipioExecucao(Municipio municipioExecucao) {
		this.municipioExecucao = municipioExecucao;
	}

	public OcorrenciaPendencia getOcorrenciaPendencia() {
		return ocorrenciaPendencia;
	}

	public void setOcorrenciaPendencia(OcorrenciaPendencia ocorrenciaPendencia) {
		this.ocorrenciaPendencia = ocorrenciaPendencia;
	}
	
	public Filial getFilialExecucao() {
		return filialExecucao;
	}
	public void setFilialExecucao(Filial filialExecucao) {
		this.filialExecucao = filialExecucao;
	}
	
	public Integer getNrDiasCarencia() {
		return nrDiasCarencia;
	}
	
	public void setNrDiasCarencia(Integer nrDiasCarencia) {
		this.nrDiasCarencia = nrDiasCarencia;
	}
	
	public YearMonthDay getDtInicioPermanencia() {
		return dtInicioPermanencia;
	}
	
	public void setDtInicioPermanencia(YearMonthDay dtInicioPermanencia) {
		this.dtInicioPermanencia = dtInicioPermanencia;
	}
	
	public YearMonthDay getDtFimPermanencia() {
		return dtFimPermanencia;
	}
	
	public void setDtFimPermanencia(YearMonthDay dtFimPermanencia) {
		this.dtFimPermanencia = dtFimPermanencia;
	}
	
	public DomainValue getTpExecucao() {
		return tpExecucao;
	}
	
	public void setTpExecucao(DomainValue tpExecucao) {
		this.tpExecucao = tpExecucao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idServicoGeracaoAutomatica == null) ? 0
						: idServicoGeracaoAutomatica.hashCode());
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
		ServicoGeracaoAutomatica other = (ServicoGeracaoAutomatica) obj;
		if (idServicoGeracaoAutomatica == null) {
			if (other.idServicoGeracaoAutomatica != null)
				return false;
		} else if (!idServicoGeracaoAutomatica
				.equals(other.idServicoGeracaoAutomatica))
			return false;
		return true;
	}
}
package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "FRANQUIA_FORNEC_ESCOLTA")
@SequenceGenerator(name = "FRANQUIA_FORNEC_ESCOLTA_SQ", sequenceName = "FRANQUIA_FORNEC_ESCOLTA_SQ")
public class FranquiaFornecedorEscolta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_FRANQUIA_FORNEC_ESCOLTA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FRANQUIA_FORNEC_ESCOLTA_SQ")
	private Long idFranquiaFornecedorEscolta;

	@ManyToOne
	@JoinColumn(name = "ID_FORNECEDOR_ESCOLTA", nullable = false)
	private FornecedorEscolta fornecedorEscolta;

	@Column(name = "TP_ESCOLTA", nullable = false)
	@Type(
			type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType",
			parameters = { @Parameter(name = "domainName", value = "DM_TIPO_ESCOLTA") }
	)
	private DomainValue tpEscolta;

	@Column(name = "NR_QUILOMETRAGEM", nullable = false)
	private Long nrQuilometragem;

	@Column(name = "NR_TEMPO_VIAGEM_MINUTO", nullable = false)
	private Long nrTempoViagemMinutos;

	@Column(name = "VL_FRANQUIA", nullable = false)
	private BigDecimal vlFranquia;

	@Column(name = "VL_HORA_EXCEDENTE", nullable = false)
	private BigDecimal vlHoraExcedente;

	@Column(name = "VL_QUILOMETRO_EXCEDENTE", nullable = false)
	private BigDecimal vlQuilometroExcedente;

	@OneToMany(mappedBy = "franquiaFornecedorEscolta", cascade = CascadeType.ALL)
	private List<FranquiaFornecedorFilialAtendimento> filiaisAtendimento;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL_ORIGEM")
	private Filial filialOrigem;

	@ManyToOne
	@JoinColumn(name = "ID_FILIAL_DESTINO")
	private Filial filialDestino;

	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;

	@Column(name = "DT_VIGENCIA_FINAL")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;

	public Long getIdFranquiaFornecedorEscolta() {
		return idFranquiaFornecedorEscolta;
	}

	public void setIdFranquiaFornecedorEscolta(Long idFranquiaFornecedorEscolta) {
		this.idFranquiaFornecedorEscolta = idFranquiaFornecedorEscolta;
	}

	public FornecedorEscolta getFornecedorEscolta() {
		return fornecedorEscolta;
	}

	public void setFornecedorEscolta(FornecedorEscolta fornecedorEscolta) {
		this.fornecedorEscolta = fornecedorEscolta;
	}

	public DomainValue getTpEscolta() {
		return tpEscolta;
	}

	public void setTpEscolta(DomainValue tpEscolta) {
		this.tpEscolta = tpEscolta;
	}

	public Long getNrQuilometragem() {
		return nrQuilometragem;
	}

	public void setNrQuilometragem(Long nrQuilometragem) {
		this.nrQuilometragem = nrQuilometragem;
	}

	public Long getNrTempoViagemMinutos() {
		return nrTempoViagemMinutos;
	}

	public void setNrTempoViagemMinutos(Long nrTempoViagemMinutos) {
		this.nrTempoViagemMinutos = nrTempoViagemMinutos;
	}

	public BigDecimal getVlFranquia() {
		return vlFranquia;
	}

	public void setVlFranquia(BigDecimal vlFranquia) {
		this.vlFranquia = vlFranquia;
	}

	public BigDecimal getVlHoraExcedente() {
		return vlHoraExcedente;
	}

	public void setVlHoraExcedente(BigDecimal vlHoraExcedente) {
		this.vlHoraExcedente = vlHoraExcedente;
	}

	public BigDecimal getVlQuilometroExcedente() {
		return vlQuilometroExcedente;
	}

	public void setVlQuilometroExcedente(BigDecimal vlQuilometroExcedente) {
		this.vlQuilometroExcedente = vlQuilometroExcedente;
	}

	public List<FranquiaFornecedorFilialAtendimento> getFiliaisAtendimento() {
		return filiaisAtendimento;
	}

	public void setFiliaisAtendimento(List<FranquiaFornecedorFilialAtendimento> filiaisAtendimento) {
		this.filiaisAtendimento = filiaisAtendimento;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(idFranquiaFornecedorEscolta)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof FranquiaFornecedorEscolta)) {
			return false;
		}
		FranquiaFornecedorEscolta cast = (FranquiaFornecedorEscolta) other;
		return new EqualsBuilder()
				.append(idFranquiaFornecedorEscolta, cast.idFranquiaFornecedorEscolta)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(idFranquiaFornecedorEscolta)
				.toString();
	}

}

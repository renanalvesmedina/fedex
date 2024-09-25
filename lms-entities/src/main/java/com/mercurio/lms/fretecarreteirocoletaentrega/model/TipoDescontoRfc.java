package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;

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
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "TIPO_DESCONTO_RFC")
@SequenceGenerator(name = "TIPO_DESCONTO_RFC_SQ", sequenceName = "TIPO_DESCONTO_RFC_SQ", allocationSize = 1)
public class TipoDescontoRfc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_DESCONTO_RFC_SQ")
	@Column(name = "ID_TIPO_DESCONTO_RFC", nullable = false)
	private Long idTipoDescontoRfc;

	@Column(name = "DS_TIPO_DESCONTO_RFC", nullable = false, length = 60)
	private String dsTipoDescontoRfc;

	@Column(name = "BL_ABATE_RECIBO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blAbateRecibo;

	@Column(name = "BL_RECALCULA_INSS", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue recalculaInss;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;

	@Column(name = "DT_ALTERACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtAlteracao;

	public Long getIdTipoDescontoRfc() {
		return idTipoDescontoRfc;
	}

	public void setIdTipoDescontoRfc(Long idTipoDescontoRfc) {
		this.idTipoDescontoRfc = idTipoDescontoRfc;
	}

	public String getDsTipoDescontoRfc() {
		return dsTipoDescontoRfc;
	}

	public void setDsTipoDescontoRfc(String dsTipoDescontoRfc) {
		this.dsTipoDescontoRfc = dsTipoDescontoRfc;
	}

	public DomainValue getBlAbateRecibo() {
		return blAbateRecibo;
	}

	public void setBlAbateRecibo(DomainValue blAbateRecibo) {
		this.blAbateRecibo = blAbateRecibo;
	}

	public DomainValue getRecalculaInss() {
		return recalculaInss;
	}

	public void setRecalculaInss(DomainValue recalculaInss) {
		this.recalculaInss = recalculaInss;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public YearMonthDay getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(YearMonthDay dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idTipoDescontoRfc == null) ? 0 : idTipoDescontoRfc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TipoDescontoRfc)) {
			return false;
		}

		TipoDescontoRfc castOther = (TipoDescontoRfc) obj;

		return new EqualsBuilder().append(this.getIdTipoDescontoRfc(),
				castOther.getIdTipoDescontoRfc()).isEquals();
	}
}
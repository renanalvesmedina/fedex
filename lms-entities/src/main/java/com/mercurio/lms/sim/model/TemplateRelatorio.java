package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name="TEMPLATE_RELATORIOS")
@SequenceGenerator(name="SQ_TEMPLATE_RELATORIOS", sequenceName="TEMPLATE_RELATORIO_SQ", allocationSize=1)
public class TemplateRelatorio implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SQ_TEMPLATE_RELATORIOS")
	@Column(name="ID_TEMPLATE_RELATORIO", nullable=false)
	private Long idTemplate;
	
	@Column(name="DS_TEMPLATE_RELATORIO", length=100, nullable=false)
	private String nmTemplate;
	
	@Column(name = "TP_TEMPLATE_RELATORIO", length = 1, nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_EMAIL") })
	private DomainValue tpRelatorio;
	
	@Lob
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	@Column(name="DC_ARQUIVO")
	private byte[] dcArquivo;
	
	
	public Long getIdTemplate() {
		return idTemplate;
	}
	public void setIdTemplate(Long idTemplate) {
		this.idTemplate = idTemplate;
	}
	public String getNmTemplate() {
		return nmTemplate;
	}
	public void setNmTemplate(String nmTemplate) {
		this.nmTemplate = nmTemplate;
	}
	public byte[] getDcArquivo() {
		return dcArquivo;
	}
	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public DomainValue getTpRelatorio() {
		return tpRelatorio;
	}
	public void setTpRelatorio(DomainValue tpRelatorio) {
		this.tpRelatorio = tpRelatorio;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dcArquivo);
		result = prime * result
				+ ((idTemplate == null) ? 0 : idTemplate.hashCode());
		result = prime * result
				+ ((nmTemplate == null) ? 0 : nmTemplate.hashCode());
		result = prime * result
				+ ((tpRelatorio == null) ? 0 : tpRelatorio.hashCode());
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
		TemplateRelatorio other = (TemplateRelatorio) obj;
		if (!Arrays.equals(dcArquivo, other.dcArquivo))
			return false;
		if (idTemplate == null) {
			if (other.idTemplate != null)
				return false;
		} else if (!idTemplate.equals(other.idTemplate))
			return false;
		if (nmTemplate == null) {
			if (other.nmTemplate != null)
				return false;
		} else if (!nmTemplate.equals(other.nmTemplate))
			return false;
		if (tpRelatorio == null) {
			if (other.tpRelatorio != null)
				return false;
		} else if (!tpRelatorio.equals(other.tpRelatorio))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TemplateRelatorio [idTemplate=" + idTemplate + ", nmTemplate="
				+ nmTemplate + ", tpRelatorio=" + tpRelatorio + ", dcArquivo="
				+ Arrays.toString(dcArquivo) + "]";
	}
		
}

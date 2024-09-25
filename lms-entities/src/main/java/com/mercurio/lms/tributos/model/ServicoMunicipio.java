package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoMunicipio;

    /** persistent field */
    private String nrServicoMunicipio;

    /** persistent field */
    private String dsServicoMunicipio;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private List issMunicipioServicos;
    
    /**  
	 * no persistent field Atributo criado para a apresentação do Serviço
	 * Município na combo de Serviços Município
     */
    private String nrServicoDsServicoMunicipio;

    public Long getIdServicoMunicipio() {
        return this.idServicoMunicipio;
    }

    public void setIdServicoMunicipio(Long idServicoMunicipio) {
        this.idServicoMunicipio = idServicoMunicipio;
    }

    public String getNrServicoMunicipio() {
        return this.nrServicoMunicipio;
    }

    public void setNrServicoMunicipio(String nrServicoMunicipio) {
        this.nrServicoMunicipio = nrServicoMunicipio;
    }

    public String getDsServicoMunicipio() {
        return this.dsServicoMunicipio;
    }

    public void setDsServicoMunicipio(String dsServicoMunicipio) {
        this.dsServicoMunicipio = dsServicoMunicipio;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.IssMunicipioServico.class)     
    public List getIssMunicipioServicos() {
        return this.issMunicipioServicos;
    }

    public void setIssMunicipioServicos(List issMunicipioServicos) {
        this.issMunicipioServicos = issMunicipioServicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoMunicipio",
				getIdServicoMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoMunicipio))
			return false;
        ServicoMunicipio castOther = (ServicoMunicipio) other;
		return new EqualsBuilder().append(this.getIdServicoMunicipio(),
				castOther.getIdServicoMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoMunicipio())
            .toHashCode();
    }

    public String getNrServicoDsServicoMunicipio() {
        return nrServicoDsServicoMunicipio;
    }

	public void setNrServicoDsServicoMunicipio(
			String nrServicoDsServicoMunicipio) {
        this.nrServicoDsServicoMunicipio = nrServicoDsServicoMunicipio;
    }

}

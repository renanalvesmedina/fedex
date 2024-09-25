package com.mercurio.lms.tributos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoTributo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoTributo;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private String dsServicoTributo;

    /** nullable persistent field */
    private String obServicoTributo;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.tributos.model.ServicoOficialTributo servicoOficialTributo;

    /** persistent field */
    private List aliquotaContribuicaoServs;

    /** persistent field */
    private List issMunicipioServicos;

    public Long getIdServicoTributo() {
        return this.idServicoTributo;
    }

    public void setIdServicoTributo(Long idServicoTributo) {
        this.idServicoTributo = idServicoTributo;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public String getDsServicoTributo() {
        return this.dsServicoTributo;
    }

    public void setDsServicoTributo(String dsServicoTributo) {
        this.dsServicoTributo = dsServicoTributo;
    }

    public String getObServicoTributo() {
        return this.obServicoTributo;
    }

    public void setObServicoTributo(String obServicoTributo) {
        this.obServicoTributo = obServicoTributo;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.tributos.model.ServicoOficialTributo getServicoOficialTributo() {
        return this.servicoOficialTributo;
    }

	public void setServicoOficialTributo(
			com.mercurio.lms.tributos.model.ServicoOficialTributo servicoOficialTributo) {
        this.servicoOficialTributo = servicoOficialTributo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaContribuicaoServ.class)     
    public List getAliquotaContribuicaoServs() {
        return this.aliquotaContribuicaoServs;
    }

    public void setAliquotaContribuicaoServs(List aliquotaContribuicaoServs) {
        this.aliquotaContribuicaoServs = aliquotaContribuicaoServs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tributos.model.IssMunicipioServico.class)     
    public List getIssMunicipioServicos() {
        return this.issMunicipioServicos;
    }

    public void setIssMunicipioServicos(List issMunicipioServicos) {
        this.issMunicipioServicos = issMunicipioServicos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoTributo",
				getIdServicoTributo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoTributo))
			return false;
        ServicoTributo castOther = (ServicoTributo) other;
		return new EqualsBuilder().append(this.getIdServicoTributo(),
				castOther.getIdServicoTributo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoTributo()).toHashCode();
    }

}

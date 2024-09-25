package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaViagem implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaViagem;

    /** field */
    private Integer versao;    
    
    /** persistent field */
    private DomainValue tpRota;

    /** nullable persistent field */
    private DomainValue tpSistemaRota;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;
    
    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private List rotaIdaVoltas;

    /** persistent field */
    private List configuracaoAuditoriaFis;

    /** persistent field */
    private List motoristaRotaViagems;

    /** persistent field */
    private List servicoRotaViagems;

    /** persistent field */
    private List meioTransporteRotaViagems;

    public Long getIdRotaViagem() {
        return this.idRotaViagem;
    }

    public void setIdRotaViagem(Long idRotaViagem) {
        this.idRotaViagem = idRotaViagem;
    }

    public DomainValue getTpRota() {
        return this.tpRota;
    }

    public void setTpRota(DomainValue tpRota) {
        this.tpRota = tpRota;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public DomainValue getTpSistemaRota() {
        return this.tpSistemaRota;
    }

    public void setTpSistemaRota(DomainValue tpSistemaRota) {
        this.tpSistemaRota = tpSistemaRota;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaIdaVolta.class)     
    public List getRotaIdaVoltas() {
        return this.rotaIdaVoltas;
    }

    public void setRotaIdaVoltas(List rotaIdaVoltas) {
        this.rotaIdaVoltas = rotaIdaVoltas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil.class)     
    public List getConfiguracaoAuditoriaFis() {
        return this.configuracaoAuditoriaFis;
    }

    public void setConfiguracaoAuditoriaFis(List configuracaoAuditoriaFis) {
        this.configuracaoAuditoriaFis = configuracaoAuditoriaFis;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MotoristaRotaViagem.class)     
    public List getMotoristaRotaViagems() {
        return this.motoristaRotaViagems;
    }

    public void setMotoristaRotaViagems(List motoristaRotaViagems) {
        this.motoristaRotaViagems = motoristaRotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ServicoRotaViagem.class)     
    public List getServicoRotaViagems() {
        return this.servicoRotaViagems;
    }

    public void setServicoRotaViagems(List servicoRotaViagems) {
        this.servicoRotaViagems = servicoRotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MeioTransporteRotaViagem.class)     
    public List getMeioTransporteRotaViagems() {
        return this.meioTransporteRotaViagems;
    }

    public void setMeioTransporteRotaViagems(List meioTransporteRotaViagems) {
        this.meioTransporteRotaViagems = meioTransporteRotaViagems;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRotaViagem",
				getIdRotaViagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaViagem))
			return false;
        RotaViagem castOther = (RotaViagem) other;
		return new EqualsBuilder().append(this.getIdRotaViagem(),
				castOther.getIdRotaViagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaViagem()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return cliente;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

}

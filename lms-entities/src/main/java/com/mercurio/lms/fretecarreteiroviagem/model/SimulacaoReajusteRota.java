package com.mercurio.lms.fretecarreteiroviagem.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;

/** @author LMS Custom Hibernate CodeGenerator */
public class SimulacaoReajusteRota implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSimulacaoReajusteRota;

    /** persistent field */
    private String dsSimulacaoReajusteRota ;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    private DomainValue tpReajuste;
    
    private BigDecimal vlReajuste;
    
    private DomainValue tpRota;
    
    private DomainValue tpSituacaoRota;
    
    private Filial filialOrigem;
    
    private Filial filialDestino;
    
    private TipoMeioTransporte tipoMeioTransporte;
    
    private MeioTransporte meioTransporte;
    
    private Proprietario proprietario;
    
    private Regional regionalOrigem;
    
    private Regional regionalDestino;
    
    private MoedaPais moedaPais;

    /** persistent field */
    private List parametroSimulacaoRotas;

    public Long getIdSimulacaoReajusteRota() {
        return this.idSimulacaoReajusteRota;
    }

    public void setIdSimulacaoReajusteRota(Long idSimulacaoReajusteRota) {
        this.idSimulacaoReajusteRota = idSimulacaoReajusteRota;
    }

    public String getDsSimulacaoReajusteRota() {
        return this.dsSimulacaoReajusteRota;
    }

    public void setDsSimulacaoReajusteRota(String dsSimulacaoReajusteRota) {
        this.dsSimulacaoReajusteRota = dsSimulacaoReajusteRota;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.AplicaReajusteRota.class)     
    public List getParametroSimulacaoRotas() {
        return this.parametroSimulacaoRotas;
    }

    public void setParametroSimulacaoRotas(List parametroSimulacaoRotas) {
        this.parametroSimulacaoRotas = parametroSimulacaoRotas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSimulacaoReajusteRota",
				getIdSimulacaoReajusteRota()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SimulacaoReajusteRota))
			return false;
        SimulacaoReajusteRota castOther = (SimulacaoReajusteRota) other;
		return new EqualsBuilder().append(this.getIdSimulacaoReajusteRota(),
				castOther.getIdSimulacaoReajusteRota()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSimulacaoReajusteRota())
            .toHashCode();
    }

	public DomainValue getTpReajuste() {
		return tpReajuste;
	}

	public void setTpReajuste(DomainValue tpReajuste) {
		this.tpReajuste = tpReajuste;
	}

	public BigDecimal getVlReajuste() {
		return vlReajuste;
	}

	public void setVlReajuste(BigDecimal vlReajuste) {
		this.vlReajuste = vlReajuste;
	}

	public DomainValue getTpRota() {
		return tpRota;
	}

	public void setTpRota(DomainValue tpRota) {
		this.tpRota = tpRota;
	}

	public DomainValue getTpSituacaoRota() {
		return tpSituacaoRota;
	}

	public void setTpSituacaoRota(DomainValue tpSituacaoRota) {
		this.tpSituacaoRota = tpSituacaoRota;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filial) {
		this.filialOrigem = filial;
	}

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public Regional getRegionalOrigem() {
		return regionalOrigem;
	}

	public void setRegionalOrigem(Regional regional) {
		this.regionalOrigem = regional;
	}

	public TipoMeioTransporte getTipoMeioTransporte() {
		return tipoMeioTransporte;
	}

	public void setTipoMeioTransporte(TipoMeioTransporte tipoMeioTransporte) {
		this.tipoMeioTransporte = tipoMeioTransporte;
	}

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public Regional getRegionalDestino() {
		return regionalDestino;
	}

	public void setRegionalDestino(Regional regionalDestino) {
		this.regionalDestino = regionalDestino;
	}

}

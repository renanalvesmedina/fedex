package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class FluxoFilial implements Serializable, Vigencia {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idFluxoFilial;

    /** persistent field */
    private Integer nrDistancia;

    /** persistent field */
    private Integer nrPrazo;
    
    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private Integer nrGrauDificuldade;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private Boolean blDomingo;

    /** persistent field */
    private Boolean blSegunda;

    /** persistent field */
    private Boolean blTerca;

    /** persistent field */
    private Boolean blQuarta;

    /** persistent field */
    private Boolean blQuinta;

    /** persistent field */
    private Boolean blSexta;

    /** persistent field */
    private Boolean blSabado;

    /** persistent field */
    private String dsFluxoFilial;
    
    /** persistent field */
    private Integer nrPrazoTotal;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialReembarcadora;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialParceira;
    
    /** persistent field */
    private List doctoServicos;
    
    /** persistent field */
    private List ordemFilialFluxos;
    
    /** Filiais de Reembarque serão apenas visualisadas na tela */
    private List fluxoReembarque;
    
    /** Filial Origem - Filial Reembarque - Filial Destino */
    private String fluxo;
    
    /** persistent field */
    private Boolean blPorto;
    
    private Boolean blFluxoSubcontratacao;
    
    private Pessoa empresaSubcontratada;

    @Transient
	private boolean blClonar = false;

    @Transient
	private boolean atualizacaoClone = false;
    
	public Long getIdFluxoFilial() {
        return this.idFluxoFilial;
    }

    public void setIdFluxoFilial(Long idFluxoFilial) {
        this.idFluxoFilial = idFluxoFilial;
    }

    public Integer getNrDistancia() {
        return this.nrDistancia;
    }

    public void setNrDistancia(Integer nrDistancia) {
        this.nrDistancia = nrDistancia;
    }

    public Integer getNrPrazo() {
        return this.nrPrazo;
    }

    public void setNrPrazo(Integer nrPrazo) {
        this.nrPrazo = nrPrazo;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public Integer getNrGrauDificuldade() {
        return this.nrGrauDificuldade;
    }

    public void setNrGrauDificuldade(Integer nrGrauDificuldade) {
        this.nrGrauDificuldade = nrGrauDificuldade;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialReembarcadora() {
        return this.filialByIdFilialReembarcadora;
    }

	public void setFilialByIdFilialReembarcadora(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialReembarcadora) {
        this.filialByIdFilialReembarcadora = filialByIdFilialReembarcadora;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialParceira() {
		return filialByIdFilialParceira;
	}

	public void setFilialByIdFilialParceira(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialParceira) {
		this.filialByIdFilialParceira = filialByIdFilialParceira;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idFluxoFilial",
				getIdFluxoFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FluxoFilial))
			return false;
        FluxoFilial castOther = (FluxoFilial) other;
		return new EqualsBuilder().append(this.getIdFluxoFilial(),
				castOther.getIdFluxoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFluxoFilial()).toHashCode();
    }

	public Boolean getBlDomingo() {
		return blDomingo;
	}

	public void setBlDomingo(Boolean blDomingo) {
		this.blDomingo = blDomingo;
	}

	public Boolean getBlQuarta() {
		return blQuarta;
	}

	public void setBlQuarta(Boolean blQuarta) {
		this.blQuarta = blQuarta;
	}

	public Boolean getBlQuinta() {
		return blQuinta;
	}

	public void setBlQuinta(Boolean blQuinta) {
		this.blQuinta = blQuinta;
	}

	public Boolean getBlSabado() {
		return blSabado;
	}

	public void setBlSabado(Boolean blSabado) {
		this.blSabado = blSabado;
	}

	public Boolean getBlSegunda() {
		return blSegunda;
	}

	public void setBlSegunda(Boolean blSegunda) {
		this.blSegunda = blSegunda;
	}

	public Boolean getBlSexta() {
		return blSexta;
	}

	public void setBlSexta(Boolean blSexta) {
		this.blSexta = blSexta;
	}

	public Boolean getBlTerca() {
		return blTerca;
	}

	public void setBlTerca(Boolean blTerca) {
		this.blTerca = blTerca;
	}

	public Integer getNrPrazoView() {
		Integer hours = null;
		if (this.nrPrazo != null) {
			hours = Integer.valueOf(this.nrPrazo.intValue() / 60);
		}

		return hours;
	}

	public void setNrPrazoView(Integer nrPrazoView) {
		Integer minutes = null;
		if (nrPrazoView != null) {
			minutes = Integer.valueOf(nrPrazoView.intValue() * 60);
		}
		this.nrPrazo = minutes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.FluxoFilial.class)     
	public List getFluxoReembarque() {
		return fluxoReembarque;
	}

	public void setFluxoReembarque(List fluxoReembarque) {
		this.fluxoReembarque = fluxoReembarque;
	}

	public String getFluxo() {
		return fluxo;
	}

	public void setFluxo(String fluxo) {
		this.fluxo = fluxo;
	}

	public String getDsFluxoFilial() {
		return dsFluxoFilial;
	}

	public void setDsFluxoFilial(String dsFluxoFilial) {
		this.dsFluxoFilial = dsFluxoFilial;
	}

	public Integer getNrPrazoTotal() {
		return nrPrazoTotal;
	}

	public void setNrPrazoTotal(Integer nrPrazoTotal) {
		this.nrPrazoTotal = nrPrazoTotal;
	}

	public List getDoctoServicos() {
		return doctoServicos;
	}

	public void setDoctoServicos(List doctoServicos) {
		this.doctoServicos = doctoServicos;
	}

	public List getOrdemFilialFluxos() {
		return ordemFilialFluxos;
	}

	public void setOrdemFilialFluxos(List ordemFilialFluxos) {
		this.ordemFilialFluxos = ordemFilialFluxos;
	}

	public Boolean getBlPorto() {
		return blPorto;
	}

	public void setBlPorto(Boolean blPorto) {
		this.blPorto = blPorto;
	}

	public void setBlClone(boolean blClonar) {
		this.blClonar = blClonar;
	}
	
	public boolean isClone() {
		return blClonar;
	}
	
	public boolean hasFilialReembarcadora() {
		return filialByIdFilialReembarcadora != null;
	}
	
	public void setAtualizacaoClone(boolean atualizacaoClone) {
		this.atualizacaoClone = atualizacaoClone;
	}
	
	public boolean isAtualizacaoClone() {
		return atualizacaoClone;
	}

	public Boolean getBlFluxoSubcontratacao() {
		return blFluxoSubcontratacao;
	}

	public void setBlFluxoSubcontratacao(Boolean blFluxoSubcontratacao) {
		this.blFluxoSubcontratacao = blFluxoSubcontratacao;
	}

	public Pessoa getEmpresaSubcontratada() {
		return empresaSubcontratada;
	}

	public void setEmpresaSubcontratada(Pessoa empresaSubcontratada) {
		this.empresaSubcontratada = empresaSubcontratada;
	}
}
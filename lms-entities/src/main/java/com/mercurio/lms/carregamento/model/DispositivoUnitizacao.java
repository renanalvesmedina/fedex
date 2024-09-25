package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.portaria.model.MacroZona;

/** @author LMS Custom Hibernate CodeGenerator */
public class DispositivoUnitizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDispositivoUnitizacao;

    /** persistent field */
    private String nrIdentificacao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao tipoDispositivoUnitizacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List dispCarregIdentificados;

    /** persistent field */
    private List estoqueDispIdentificados;

    /** persistent field */
	private List entradaPendenciaMatrizs;

	private List<VolumeNotaFiscal> volumes;
	
	private List<DispositivoUnitizacao> dispositivosUnitizacao;
	
    private com.mercurio.lms.sim.model.LocalizacaoMercadoria localizacaoMercadoria;
    
    private com.mercurio.lms.municipios.model.Filial localizacaoFilial;
    
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacaoPai;
    
    private MacroZona macroZona;
    
    public Long getIdDispositivoUnitizacao() {
        return this.idDispositivoUnitizacao;
    }

    public void setIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
        this.idDispositivoUnitizacao = idDispositivoUnitizacao;
    }

    public String getNrIdentificacao() {
        return this.nrIdentificacao;
    }

    public void setNrIdentificacao(String nrIdentificacao) {
        this.nrIdentificacao = nrIdentificacao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao getTipoDispositivoUnitizacao() {
        return this.tipoDispositivoUnitizacao;
    }

	public void setTipoDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao tipoDispositivoUnitizacao) {
        this.tipoDispositivoUnitizacao = tipoDispositivoUnitizacao;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregIdentificado.class)     
    public List getDispCarregIdentificados() {
        return this.dispCarregIdentificados;
    }

    public void setDispCarregIdentificados(List dispCarregIdentificados) {
        this.dispCarregIdentificados = dispCarregIdentificados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EstoqueDispIdentificado.class)     
    public List getEstoqueDispIdentificados() {
        return this.estoqueDispIdentificados;
    }

    public void setEstoqueDispIdentificados(List estoqueDispIdentificados) {
        this.estoqueDispIdentificados = estoqueDispIdentificados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EntradaPendenciaMatriz.class)     
    public List getEntradaPendenciaMatrizs() {
        return this.entradaPendenciaMatrizs;
    }

    public void setEntradaPendenciaMatrizs(List entradaPendenciaMatrizs) {
        this.entradaPendenciaMatrizs = entradaPendenciaMatrizs;
    }
    
	public com.mercurio.lms.sim.model.LocalizacaoMercadoria getLocalizacaoMercadoria() {
		return localizacaoMercadoria;
	}

	public void setLocalizacaoMercadoria(
			com.mercurio.lms.sim.model.LocalizacaoMercadoria localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}

	public com.mercurio.lms.municipios.model.Filial getLocalizacaoFilial() {
		return localizacaoFilial;
	}

	public void setLocalizacaoFilial(
			com.mercurio.lms.municipios.model.Filial localizacaoFilial) {
		this.localizacaoFilial = localizacaoFilial;
	}

	public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacaoPai() {
		return dispositivoUnitizacaoPai;
	}

	public void setDispositivoUnitizacaoPai(
			com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacaoPai) {
		this.dispositivoUnitizacaoPai = dispositivoUnitizacaoPai;
	}

	public MacroZona getMacroZona() {
		return macroZona;
	}

	public void setMacroZona(MacroZona macroZona) {
		this.macroZona = macroZona;
	}
	
	public List<VolumeNotaFiscal> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<VolumeNotaFiscal> volumes) {
		this.volumes = volumes;
	}
	
	public List<DispositivoUnitizacao> getDispositivosUnitizacao() {
		return dispositivosUnitizacao;
	}

	public void setDispositivosUnitizacao(
			List<DispositivoUnitizacao> dispositivosUnitizacao) {
		this.dispositivosUnitizacao = dispositivosUnitizacao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idDispositivoUnitizacao",
				getIdDispositivoUnitizacao()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_DISPOSITIVO_UNITIZACAO", idDispositivoUnitizacao)
				.append("ID_EMPRESA", empresa != null ? empresa.getIdEmpresa() : null)
				.append("ID_TIPO_DISPOSITIVO_UNITIZACAO", tipoDispositivoUnitizacao != null ? tipoDispositivoUnitizacao.getIdTipoDispositivoUnitizacao() : null)
				.append("NR_IDENTIFICACAO", nrIdentificacao)
				.append("TP_SITUACAO", tpSituacao != null ? tpSituacao.getValue() : null)
				.append("ID_LOCALIZACAO_MERCADORIA", localizacaoMercadoria != null ? localizacaoMercadoria.getIdLocalizacaoMercadoria() : null)
				.append("ID_LOCALIZACAO_FILIAL", localizacaoFilial != null ? localizacaoFilial.getIdFilial() : null)
				.append("ID_DISPOSITIVO_UNITIZACAO_PAI", dispositivoUnitizacaoPai != null ? dispositivoUnitizacaoPai.getIdDispositivoUnitizacao() : null)
				.append("ID_MACRO_ZONA", macroZona != null ? macroZona.getIdMacroZona() : null)
				.toString();
	}

   public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DispositivoUnitizacao))
			return false;
        DispositivoUnitizacao castOther = (DispositivoUnitizacao) other;
		return new EqualsBuilder().append(this.getIdDispositivoUnitizacao(),
				castOther.getIdDispositivoUnitizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDispositivoUnitizacao())
            .toHashCode();
    }

}

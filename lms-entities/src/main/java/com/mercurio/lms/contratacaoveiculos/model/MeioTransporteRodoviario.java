package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporteRodoviario implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMeioTransporte;

    /** persistent field */
    private String nrBilheteSeguro;
 
    /** persistent field */
    private YearMonthDay dtVencimentoSeguro;

    /** persistent field */
    private Long cdRenavam;

    /** persistent field */
    private Long nrCertificado;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private String nrChassi;
    
    /** persistent field */
    private Boolean blPossuiPlataforma;
    
    /** persistent field */
    private Boolean blMonitorado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private Long nrRastreador;

    /** nullable persistent field */
    private Long nrCelular;

    /** nullable persistent field */
    private BigDecimal psTara;

    /** nullable persistent field */
    private String nrDddCelular;

    /** persistent field */
    private Boolean blControleTag;
    
    /** persistent field */
    private BigDecimal vlAlturaBau;

    /** persistent field */
    private BigDecimal vlLarguraBau;
    
    /** persistent field */
    private BigDecimal vlProfundidadeBau;
    
    /** persistent field */
    private Integer nrTag;
    
    /** nullable persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.OperadoraMct operadoraMct;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte eixosTipoMeioTransporte;

    /** persistent field */
    private List registroAuditorias;

    /** persistent field */
    private List meioTranspRodoPermissos;

    /** persistent field */
    private List rotaMeioTransporteRodovs;

    /** persistent field */
    private List meioTransporteRotaViagems;

    /** persistent field */
    private List ordemSaidasByIdSemiReboque;

    /** persistent field */
    private List ordemSaidasByIdMeioTransporte;

    /** persistent field */
    private List configuracaoAuditoriaFis;

    /** persistent field */
    private List tabelaColetaEntregas;

    /** persistent field */
    private List controleQuilometragems;

    /** persistent field */
    private List meioTransporteRodoBoxs;

    /** persistent field */
    private List meioTransporteRodoviarios;

    /** persistent field */
    private List paramSimulacaoHistoricas;

    /** persistent field */
    private List meioTranspRodoMotoristas;

    /** persistent field */
    private List eficienciaVeiculoColetas;

    /** persistent field */
    private List reciboFreteCarreteiros;
    
    public Long getIdMeioTransporte() {
        return this.idMeioTransporte;
    }

    public void setIdMeioTransporte(Long idMeioTransporte) {
        this.idMeioTransporte = idMeioTransporte;
    }

    public String getNrBilheteSeguro() {
        return this.nrBilheteSeguro;
    }

    public void setNrBilheteSeguro(String nrBilheteSeguro) {
        this.nrBilheteSeguro = nrBilheteSeguro;
    }

    public YearMonthDay getDtVencimentoSeguro() {
        return this.dtVencimentoSeguro;
    }

    public void setDtVencimentoSeguro(YearMonthDay dtVencimentoSeguro) {
        this.dtVencimentoSeguro = dtVencimentoSeguro;
    }

    public Long getCdRenavam() {
        return this.cdRenavam;
    }

    public void setCdRenavam(Long cdRenavam) {
        this.cdRenavam = cdRenavam;
    }

    public Long getNrCertificado() {
        return this.nrCertificado;
    }

    public void setNrCertificado(Long nrCertificado) {
        this.nrCertificado = nrCertificado;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getNrChassi() {
        return this.nrChassi;
    }

    public void setNrChassi(String nrChassi) {
        this.nrChassi = nrChassi;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Long getNrRastreador() {
        return this.nrRastreador;
    }

    public void setNrRastreador(Long nrRastreador) {
        this.nrRastreador = nrRastreador;
    }

    public Long getNrCelular() {
        return this.nrCelular;
    }

    public void setNrCelular(Long nrCelular) {
        this.nrCelular = nrCelular;
    }

    public BigDecimal getPsTara() {
        return this.psTara;
    }

    public void setPsTara(BigDecimal psTara) {
        this.psTara = psTara;
    }

    public String getNrDddCelular() {
        return this.nrDddCelular;
    }

    public void setNrDddCelular(String nrDddCelular) {
        this.nrDddCelular = nrDddCelular;
    }

    public Boolean getBlControleTag() {
        return this.blControleTag;
    }

    public void setBlControleTag(Boolean blControleTag) {
        this.blControleTag = blControleTag;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

	public void setMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.contratacaoveiculos.model.OperadoraMct getOperadoraMct() {
        return this.operadoraMct;
    }

	public void setOperadoraMct(
			com.mercurio.lms.contratacaoveiculos.model.OperadoraMct operadoraMct) {
        this.operadoraMct = operadoraMct;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte getEixosTipoMeioTransporte() {
        return this.eixosTipoMeioTransporte;
    }

	public void setEixosTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte eixosTipoMeioTransporte) {
        this.eixosTipoMeioTransporte = eixosTipoMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.RegistroAuditoria.class)     
    public List getRegistroAuditorias() {
        return this.registroAuditorias;
    }

    public void setRegistroAuditorias(List registroAuditorias) {
        this.registroAuditorias = registroAuditorias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoPermisso.class)     
    public List getMeioTranspRodoPermissos() {
        return this.meioTranspRodoPermissos;
    }

    public void setMeioTranspRodoPermissos(List meioTranspRodoPermissos) {
        this.meioTranspRodoPermissos = meioTranspRodoPermissos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaMeioTransporteRodov.class)     
    public List getRotaMeioTransporteRodovs() {
        return this.rotaMeioTransporteRodovs;
    }

    public void setRotaMeioTransporteRodovs(List rotaMeioTransporteRodovs) {
        this.rotaMeioTransporteRodovs = rotaMeioTransporteRodovs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MeioTransporteRotaViagem.class)     
    public List getMeioTransporteRotaViagems() {
        return this.meioTransporteRotaViagems;
    }

    public void setMeioTransporteRotaViagems(List meioTransporteRotaViagems) {
        this.meioTransporteRotaViagems = meioTransporteRotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.OrdemSaida.class)     
    public List getOrdemSaidasByIdSemiReboque() {
        return this.ordemSaidasByIdSemiReboque;
    }

    public void setOrdemSaidasByIdSemiReboque(List ordemSaidasByIdSemiReboque) {
        this.ordemSaidasByIdSemiReboque = ordemSaidasByIdSemiReboque;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.OrdemSaida.class)     
    public List getOrdemSaidasByIdMeioTransporte() {
        return this.ordemSaidasByIdMeioTransporte;
    }

	public void setOrdemSaidasByIdMeioTransporte(
			List ordemSaidasByIdMeioTransporte) {
        this.ordemSaidasByIdMeioTransporte = ordemSaidasByIdMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil.class)     
    public List getConfiguracaoAuditoriaFis() {
        return this.configuracaoAuditoriaFis;
    }

    public void setConfiguracaoAuditoriaFis(List configuracaoAuditoriaFis) {
        this.configuracaoAuditoriaFis = configuracaoAuditoriaFis;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.class)     
    public List getTabelaColetaEntregas() {
        return this.tabelaColetaEntregas;
    }

    public void setTabelaColetaEntregas(List tabelaColetaEntregas) {
        this.tabelaColetaEntregas = tabelaColetaEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ControleQuilometragem.class)     
    public List getControleQuilometragems() {
        return this.controleQuilometragems;
    }

    public void setControleQuilometragems(List controleQuilometragems) {
        this.controleQuilometragems = controleQuilometragems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.MeioTransporteRodoBox.class)     
    public List getMeioTransporteRodoBoxs() {
        return this.meioTransporteRodoBoxs;
    }

    public void setMeioTransporteRodoBoxs(List meioTransporteRodoBoxs) {
        this.meioTransporteRodoBoxs = meioTransporteRodoBoxs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario.class)     
    public List getMeioTransporteRodoviarios() {
        return this.meioTransporteRodoviarios;
    }

    public void setMeioTransporteRodoviarios(List meioTransporteRodoviarios) {
        this.meioTransporteRodoviarios = meioTransporteRodoviarios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica.class)     
    public List getParamSimulacaoHistoricas() {
        return this.paramSimulacaoHistoricas;
    }

    public void setParamSimulacaoHistoricas(List paramSimulacaoHistoricas) {
        this.paramSimulacaoHistoricas = paramSimulacaoHistoricas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista.class)     
    public List getMeioTranspRodoMotoristas() {
        return this.meioTranspRodoMotoristas;
    }

    public void setMeioTranspRodoMotoristas(List meioTranspRodoMotoristas) {
        this.meioTranspRodoMotoristas = meioTranspRodoMotoristas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.EficienciaVeiculoColeta.class)     
    public List getEficienciaVeiculoColetas() {
        return this.eficienciaVeiculoColetas;
    }

    public void setEficienciaVeiculoColetas(List eficienciaVeiculoColetas) {
        this.eficienciaVeiculoColetas = eficienciaVeiculoColetas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporte",
				getIdMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporteRodoviario))
			return false;
        MeioTransporteRodoviario castOther = (MeioTransporteRodoviario) other;
		return new EqualsBuilder().append(this.getIdMeioTransporte(),
				castOther.getIdMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporte()).toHashCode();
    }

	public Boolean getBlPossuiPlataforma() {
		return blPossuiPlataforma;
	}

	public void setBlPossuiPlataforma(Boolean blPossuiPlataforma) {
		this.blPossuiPlataforma = blPossuiPlataforma;
	}

	public BigDecimal getVlAlturaBau() {
		return vlAlturaBau;
	}

	public void setVlAlturaBau(BigDecimal vlAlturaBau) {
		this.vlAlturaBau = vlAlturaBau;
	}

	public BigDecimal getVlLarguraBau() {
		return vlLarguraBau;
	}

	public void setVlLarguraBau(BigDecimal vlLarguraBau) {
		this.vlLarguraBau = vlLarguraBau;
	}

	public BigDecimal getVlProfundidadeBau() {
		return vlProfundidadeBau;
	}

	public void setVlProfundidadeBau(BigDecimal vlProfundidadeBau) {
		this.vlProfundidadeBau = vlProfundidadeBau;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
	public List getReciboFreteCarreteiros() {
		return reciboFreteCarreteiros;
	}

	public void setReciboFreteCarreteiros(List reciboFreteCarreteiros) {
		this.reciboFreteCarreteiros = reciboFreteCarreteiros;
	}

	public Integer getNrTag() {
		return nrTag;
	}

	public void setNrTag(Integer nrTag) {
		this.nrTag = nrTag;
	}

	public Boolean getBlMonitorado() {
		return blMonitorado;
	}

	public void setBlMonitorado(Boolean blMonitorado) {
		this.blMonitorado = blMonitorado;
	}

}

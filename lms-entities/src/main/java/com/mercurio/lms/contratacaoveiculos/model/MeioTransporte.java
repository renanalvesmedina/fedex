package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Usuario;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.SemiReboqueCc;
import com.mercurio.lms.carregamento.model.VeiculoControleCarga;
import com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaMeioTransporteRodov;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.SolicitacaoSinal;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.VolEventosCelular;
import com.mercurio.lms.vol.model.VolGrfsVeiculos;
import com.mercurio.lms.vol.model.VolRetiradasEqptos;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class MeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idMeioTransporte;

    /** persistent field */
    private String nrIdentificador;

    /** persistent field */
    private DomainValue tpVinculo;

    /** persistent field */
    private Short nrAnoFabricao;

    /** persistent field */
    private Filial filialAgregadoCe;
    
    /** persistent field */
    private Short qtPortas;

    /** persistent field */
    private Long nrCodigoBarra;

    /** persistent field */
    private BigDecimal nrCapacidadeKg;

    /** persistent field */
    private BigDecimal nrCapacidadeM3;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String nrFrota;
    
    /** persistent field */
    private DomainValue tpModal;
    
    /** tpStatus auxilia a visualização do status de bloqueio do meioTransporte */
    private String tpStatus;

    /** nullable persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte modeloMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List<MeioTranspConteudoAtrib> meioTranspConteudoAtribs;

    /** persistent field */
    private List<EventoMeioTransporte> eventoMeioTransportes;

    /** persistent field */
    private List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioTransporte;

    /** persistent field */
    private List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioSemiReboque;

    /** persistent field */
    private List<VeiculoControleCarga> veiculoControleCargas;

    /** persistent field */
    private List<LacreControleCarga> lacreControleCargas;

    /** persistent field */
    private List<MeioTransporteContratado> meioTransporteContratados;

    /** persistent field */
    private List<SolicitacaoSinal> solicitacaoSinais;

    /** persistent field */
    private List<FotoMeioTransporte> fotoMeioTransportes;

    /** persistent field */
    private List<ProcessoSinistro> processoSinistros;

    /** persistent field */
    private List<MeioTranspProprietario> meioTranspProprietarios;

    /** persistent field */
    private List<SemiReboqueCc> semiReboqueCcs;

    /** persistent field */
    private List<BloqueioMotoristaProp> bloqueioMotoristaProps;

    /** persistent field */
    private List<ControleCarga> controleCargasByIdSemiRebocado;

    /** persistent field */
    private List<ControleCarga> controleCargasByIdTransportado;
    
    /** persistent field */
    private List<EventoControleCarga> eventoControleCargas;

    /** persistent field */
    private List<VolEquipamentos> volEquipamentos;
    
    /** persistent field */
    private List<VolRetiradasEqptos> volRetiradasEqptos;

    /** persistent field */
    private List<VolEventosCelular> volEventosCelulars;

    /** persistent field */
    private List<VolGrfsVeiculos> volGrfsVeiculos;

    /** persistent field */
    private List<RotaMeioTransporteRodov> rotaMeioTransporteRodovs;
    
    private YearMonthDay dtAtualizacao;
    
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAtualizacao;
    
    private List<MeioTransportePeriferico> meioTransportePerifericos;
    
    private com.mercurio.lms.fretecarreteiroviagem.model.TipoCombustivel tipoCombustivel;
    
    
    private DomainValue tpSituacaoWorkflow;

    /** persistent field */
    private Pendencia pendencia;
	    
    
    private com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador;
    
    
    private DomainValue tpOperacao;
    
    private String nrAntt;
    
	private DomainValue blAlugado;

    public MeioTransporte() {
    }

    public MeioTransporte(Long idMeioTransporte, String nrIdentificador, DomainValue tpVinculo, Short nrAnoFabricao, Filial filialAgregadoCe, Short qtPortas, Long nrCodigoBarra, BigDecimal nrCapacidadeKg, BigDecimal nrCapacidadeM3, DomainValue tpSituacao, String nrFrota, DomainValue tpModal, String tpStatus, MeioTransporteRodoviario meioTransporteRodoviario, ModeloMeioTransporte modeloMeioTransporte, Filial filial, List<MeioTranspConteudoAtrib> meioTranspConteudoAtribs, List<EventoMeioTransporte> eventoMeioTransportes, List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioTransporte, List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioSemiReboque, List<VeiculoControleCarga> veiculoControleCargas, List<LacreControleCarga> lacreControleCargas, List<MeioTransporteContratado> meioTransporteContratados, List<SolicitacaoSinal> solicitacaoSinais, List<FotoMeioTransporte> fotoMeioTransportes, List<ProcessoSinistro> processoSinistros, List<MeioTranspProprietario> meioTranspProprietarios, List<SemiReboqueCc> semiReboqueCcs, List<BloqueioMotoristaProp> bloqueioMotoristaProps, List<ControleCarga> controleCargasByIdSemiRebocado, List<ControleCarga> controleCargasByIdTransportado, List<EventoControleCarga> eventoControleCargas, List<VolEquipamentos> volEquipamentos, List<VolRetiradasEqptos> volRetiradasEqptos, List<VolEventosCelular> volEventosCelulars, List<VolGrfsVeiculos> volGrfsVeiculos, List<RotaMeioTransporteRodov> rotaMeioTransporteRodovs, YearMonthDay dtAtualizacao, Usuario usuarioAtualizacao, List<MeioTransportePeriferico> meioTransportePerifericos, TipoCombustivel tipoCombustivel, DomainValue tpSituacaoWorkflow, Pendencia pendencia, Usuario usuarioAprovador, DomainValue tpOperacao, String nrAntt, DomainValue blAlugado) {
        this.idMeioTransporte = idMeioTransporte;
        this.nrIdentificador = nrIdentificador;
        this.tpVinculo = tpVinculo;
        this.nrAnoFabricao = nrAnoFabricao;
        this.filialAgregadoCe = filialAgregadoCe;
        this.qtPortas = qtPortas;
        this.nrCodigoBarra = nrCodigoBarra;
        this.nrCapacidadeKg = nrCapacidadeKg;
        this.nrCapacidadeM3 = nrCapacidadeM3;
        this.tpSituacao = tpSituacao;
        this.nrFrota = nrFrota;
        this.tpModal = tpModal;
        this.tpStatus = tpStatus;
        this.meioTransporteRodoviario = meioTransporteRodoviario;
        this.modeloMeioTransporte = modeloMeioTransporte;
        this.filial = filial;
        this.meioTranspConteudoAtribs = meioTranspConteudoAtribs;
        this.eventoMeioTransportes = eventoMeioTransportes;
        this.solicMonitPreventivosByIdMeioTransporte = solicMonitPreventivosByIdMeioTransporte;
        this.solicMonitPreventivosByIdMeioSemiReboque = solicMonitPreventivosByIdMeioSemiReboque;
        this.veiculoControleCargas = veiculoControleCargas;
        this.lacreControleCargas = lacreControleCargas;
        this.meioTransporteContratados = meioTransporteContratados;
        this.solicitacaoSinais = solicitacaoSinais;
        this.fotoMeioTransportes = fotoMeioTransportes;
        this.processoSinistros = processoSinistros;
        this.meioTranspProprietarios = meioTranspProprietarios;
        this.semiReboqueCcs = semiReboqueCcs;
        this.bloqueioMotoristaProps = bloqueioMotoristaProps;
        this.controleCargasByIdSemiRebocado = controleCargasByIdSemiRebocado;
        this.controleCargasByIdTransportado = controleCargasByIdTransportado;
        this.eventoControleCargas = eventoControleCargas;
        this.volEquipamentos = volEquipamentos;
        this.volRetiradasEqptos = volRetiradasEqptos;
        this.volEventosCelulars = volEventosCelulars;
        this.volGrfsVeiculos = volGrfsVeiculos;
        this.rotaMeioTransporteRodovs = rotaMeioTransporteRodovs;
        this.dtAtualizacao = dtAtualizacao;
        this.usuarioAtualizacao = usuarioAtualizacao;
        this.meioTransportePerifericos = meioTransportePerifericos;
        this.tipoCombustivel = tipoCombustivel;
        this.tpSituacaoWorkflow = tpSituacaoWorkflow;
        this.pendencia = pendencia;
        this.usuarioAprovador = usuarioAprovador;
        this.tpOperacao = tpOperacao;
        this.nrAntt = nrAntt;
        this.blAlugado = blAlugado;
    }

    public Long getIdMeioTransporte() {
        return this.idMeioTransporte;
    }

    public void setIdMeioTransporte(Long idMeioTransporte) {
        this.idMeioTransporte = idMeioTransporte;
    }

    public String getNrIdentificador() {
        return this.nrIdentificador;
    }

    public void setNrIdentificador(String nrIdentificador) {
        this.nrIdentificador = nrIdentificador;
    }

    public Short getNrAnoFabricao() {
        return this.nrAnoFabricao;
    }

    public void setNrAnoFabricao(Short nrAnoFabricao) {
        this.nrAnoFabricao = nrAnoFabricao;
    }

    public Short getQtPortas() {
        return this.qtPortas;
    }

    public void setQtPortas(Short qtPortas) {
        this.qtPortas = qtPortas;
    }

    public BigDecimal getNrCapacidadeKg() {
        return this.nrCapacidadeKg;
    }

    public void setNrCapacidadeKg(BigDecimal nrCapacidadeKg) {
        this.nrCapacidadeKg = nrCapacidadeKg;
    }

    public BigDecimal getNrCapacidadeM3() {
        return this.nrCapacidadeM3;
    }

    public void setNrCapacidadeM3(BigDecimal nrCapacidadeM3) {
        this.nrCapacidadeM3 = nrCapacidadeM3;
    }

    public String getNrFrota() {
        return this.nrFrota;
    }

    public void setNrFrota(String nrFrota) {
        this.nrFrota = nrFrota;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte getModeloMeioTransporte() {
        return this.modeloMeioTransporte;
    }

	public void setModeloMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte modeloMeioTransporte) {
        this.modeloMeioTransporte = modeloMeioTransporte;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib.class)     
    public List<MeioTranspConteudoAtrib> getMeioTranspConteudoAtribs() {
        return this.meioTranspConteudoAtribs;
    }

	public void setMeioTranspConteudoAtribs(
			List<MeioTranspConteudoAtrib> meioTranspConteudoAtribs) {
        this.meioTranspConteudoAtribs = meioTranspConteudoAtribs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte.class)     
    public List<EventoMeioTransporte> getEventoMeioTransportes() {
        return this.eventoMeioTransportes;
    }

	public void setEventoMeioTransportes(
			List<EventoMeioTransporte> eventoMeioTransportes) {
        this.eventoMeioTransportes = eventoMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List<SolicMonitPreventivo> getSolicMonitPreventivosByIdMeioTransporte() {
        return this.solicMonitPreventivosByIdMeioTransporte;
    }

	public void setSolicMonitPreventivosByIdMeioTransporte(
			List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioTransporte) {
        this.solicMonitPreventivosByIdMeioTransporte = solicMonitPreventivosByIdMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List<SolicMonitPreventivo> getSolicMonitPreventivosByIdMeioSemiReboque() {
        return this.solicMonitPreventivosByIdMeioSemiReboque;
    }

	public void setSolicMonitPreventivosByIdMeioSemiReboque(
			List<SolicMonitPreventivo> solicMonitPreventivosByIdMeioSemiReboque) {
        this.solicMonitPreventivosByIdMeioSemiReboque = solicMonitPreventivosByIdMeioSemiReboque;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.VeiculoControleCarga.class)     
    public List<VeiculoControleCarga> getVeiculoControleCargas() {
        return this.veiculoControleCargas;
    }

	public void setVeiculoControleCargas(
			List<VeiculoControleCarga> veiculoControleCargas) {
        this.veiculoControleCargas = veiculoControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.LacreControleCarga.class)     
    public List<LacreControleCarga> getLacreControleCargas() {
        return this.lacreControleCargas;
    }

	public void setLacreControleCargas(
			List<LacreControleCarga> lacreControleCargas) {
        this.lacreControleCargas = lacreControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTransporteContratado.class)     
    public List<MeioTransporteContratado> getMeioTransporteContratados() {
        return this.meioTransporteContratados;
    }

	public void setMeioTransporteContratados(
			List<MeioTransporteContratado> meioTransporteContratados) {
        this.meioTransporteContratados = meioTransporteContratados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicitacaoSinal.class)     
    public List<SolicitacaoSinal> getSolicitacaoSinais() {
        return this.solicitacaoSinais;
    }

    public void setSolicitacaoSinais(List<SolicitacaoSinal> solicitacaoSinais) {
        this.solicitacaoSinais = solicitacaoSinais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.FotoMeioTransporte.class)     
    public List<FotoMeioTransporte> getFotoMeioTransportes() {
        return this.fotoMeioTransportes;
    }

	public void setFotoMeioTransportes(
			List<FotoMeioTransporte> fotoMeioTransportes) {
        this.fotoMeioTransportes = fotoMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List<ProcessoSinistro> getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List<ProcessoSinistro> processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario.class)     
    public List<MeioTranspProprietario> getMeioTranspProprietarios() {
        return this.meioTranspProprietarios;
    }

	public void setMeioTranspProprietarios(
			List<MeioTranspProprietario> meioTranspProprietarios) {
        this.meioTranspProprietarios = meioTranspProprietarios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.SemiReboqueCc.class)     
    public List<SemiReboqueCc> getSemiReboqueCcs() {
        return this.semiReboqueCcs;
    }

    public void setSemiReboqueCcs(List<SemiReboqueCc> semiReboqueCcs) {
        this.semiReboqueCcs = semiReboqueCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp.class)     
    public List<BloqueioMotoristaProp> getBloqueioMotoristaProps() {
        return this.bloqueioMotoristaProps;
    }

	public void setBloqueioMotoristaProps(
			List<BloqueioMotoristaProp> bloqueioMotoristaProps) {
        this.bloqueioMotoristaProps = bloqueioMotoristaProps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List<ControleCarga> getControleCargasByIdSemiRebocado() {
        return this.controleCargasByIdSemiRebocado;
    }

	public void setControleCargasByIdSemiRebocado(
			List<ControleCarga> controleCargasByIdSemiRebocado) {
        this.controleCargasByIdSemiRebocado = controleCargasByIdSemiRebocado;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List<ControleCarga> getControleCargasByIdTransportado() {
        return this.controleCargasByIdTransportado;
    }

	public void setControleCargasByIdTransportado(
			List<ControleCarga> controleCargasByIdTransportado) {
        this.controleCargasByIdTransportado = controleCargasByIdTransportado;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMeioTransporte",
				getIdMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MeioTransporte))
			return false;
        MeioTransporte castOther = (MeioTransporte) other;
		return new EqualsBuilder().append(this.getIdMeioTransporte(),
				castOther.getIdMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMeioTransporte()).toHashCode();
    }

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public void setTpVinculo(DomainValue tpVinculo) {
		this.tpVinculo = tpVinculo;
	}

	public DomainValue getTpVinculo() {
		return tpVinculo;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public String getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(String tpStatus) {
		this.tpStatus = tpStatus;
	}

    public List<EventoControleCarga> getEventoControleCargas() {
        return eventoControleCargas;
    }

	public void setEventoControleCargas(
			List<EventoControleCarga> eventoControleCargas) {
        this.eventoControleCargas = eventoControleCargas;
    }

	public Filial getFilialAgregadoCe() {
		return filialAgregadoCe;
	}

	public void setFilialAgregadoCe(Filial filialAgregadoCe) {
		this.filialAgregadoCe = filialAgregadoCe;
	}

	public List<VolEquipamentos> getVolEquipamentos() {
		return volEquipamentos;
	}

	public void setVolEquipamentos(List<VolEquipamentos> volEquipamentos) {
		this.volEquipamentos = volEquipamentos;
	}

	public List<VolEventosCelular> getVolEventosCelulars() {
		return volEventosCelulars;
	}

	public void setVolEventosCelulars(List<VolEventosCelular> volEventosCelulars) {
		this.volEventosCelulars = volEventosCelulars;
	}

	public List<VolGrfsVeiculos> getVolGrfsVeiculos() {
		return volGrfsVeiculos;
	}

	public void setVolGrfsVeiculos(List<VolGrfsVeiculos> volGrfsVeiculos) {
		this.volGrfsVeiculos = volGrfsVeiculos;
	}

	public List<VolRetiradasEqptos> getVolRetiradasEqptos() {
		return volRetiradasEqptos;
	}

	public void setVolRetiradasEqptos(
			List<VolRetiradasEqptos> volRetiradasEqptos) {
		this.volRetiradasEqptos = volRetiradasEqptos;
	}
	
	public List<RotaMeioTransporteRodov> getRotaMeioTransporteRodovs() {
		return rotaMeioTransporteRodovs;
	}

	public void setRotaMeioTransporteRodovs(
			List<RotaMeioTransporteRodov> rotaMeioTransporteRodovs) {
		this.rotaMeioTransporteRodovs = rotaMeioTransporteRodovs;
	}

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public List<MeioTransportePeriferico> getMeioTransportePerifericos() {
		return meioTransportePerifericos;
	}

	public void setMeioTransportePerifericos(
			List<MeioTransportePeriferico> meioTransportePerifericos) {
		this.meioTransportePerifericos = meioTransportePerifericos;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAtualizacao() {
		return usuarioAtualizacao;
	}

	public void setUsuarioAtualizacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAtualizacao) {
		this.usuarioAtualizacao = usuarioAtualizacao;
	}

	public Long getNrCodigoBarra() {
		return nrCodigoBarra;
}

	public void setNrCodigoBarra(Long nrCodigoBarra) {
		this.nrCodigoBarra = nrCodigoBarra;
	}
	
	public TipoCombustivel getTipoCombustivel() {
		return tipoCombustivel;
	}

	public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}

	public DomainValue getTpSituacaoWorkflow() {
		return tpSituacaoWorkflow;
	}

	public void setTpSituacaoWorkflow(DomainValue tpSituacaoWorkflow) {
		this.tpSituacaoWorkflow = tpSituacaoWorkflow;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioAprovador() {
		return usuarioAprovador;
	}

	public void setUsuarioAprovador(
			com.mercurio.lms.configuracoes.model.Usuario usuarioAprovador) {
		this.usuarioAprovador = usuarioAprovador;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}
	
	public String getNrAntt() {
		return nrAntt;
	}

	public void setNrAntt(String nrAntt) {
		this.nrAntt = nrAntt;
	}

	public DomainValue getBlAlugado() {
		return blAlugado;
	}

	public void setBlAlugado(DomainValue blAlugado) {
		this.blAlugado = blAlugado;
	}	
}
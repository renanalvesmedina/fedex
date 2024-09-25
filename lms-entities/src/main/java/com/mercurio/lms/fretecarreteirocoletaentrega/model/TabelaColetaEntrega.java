package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class TabelaColetaEntrega implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

	public enum DM_TP_CALCULO_TABELA_COLETA_ENTREGA  {
		CALCULO_1("C1"), CALCULO_2("C2");
		
		String v;

		DM_TP_CALCULO_TABELA_COLETA_ENTREGA(String value){
			v = value;
		}
		
		public DomainValue getDomainValue(){
			return new DomainValue(v);
		}
		
		public boolean equals(DomainValue domainValue){
			return domainValue != null && v.equals(domainValue.getValue());
		}

		public boolean equals(String domainValue){
			return domainValue != null && v.equals(domainValue);
		}
		
		@Override
		public String toString() {
			return v;
		}
	}

    /** identifier field */
    private Long idTabelaColetaEntrega;

    /** persistent field */
    private DomainValue tpRegistro;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

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

    /** nullable persistent field */
    private TimeOfDay hrDiariaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    
    private com.mercurio.lms.vendas.model.Cliente cliente;
    
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;
    
    private MoedaPais moedaPais;
    
    /** persistent field */
    private List parcelaTabelaCes;

    /** persistent field */
    private List parcelaTabelaCesRemove;
    
    /** persistent field */
    private List controleCargas;

    private DomainValue tpCalculo;

    private List<NotaCreditoDocto> notaCreditoDoctos;

    private List<NotaCreditoColeta> notaCreditoColetas;

    private List<FaixaPesoParcelaTabelaCE> faixasPesoParcelaTabelaCE;

    public Long getIdTabelaColetaEntrega() {
        return this.idTabelaColetaEntrega;
    }

    public void setIdTabelaColetaEntrega(Long idTabelaColetaEntrega) {
        this.idTabelaColetaEntrega = idTabelaColetaEntrega;
    }

    public DomainValue getTpRegistro() {
        return this.tpRegistro;
    }

    public void setTpRegistro(DomainValue tpRegistro) {
        this.tpRegistro = tpRegistro;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public Boolean getBlDomingo() {
        return this.blDomingo;
    }

    public void setBlDomingo(Boolean blDomingo) {
        this.blDomingo = blDomingo;
    }

    public Boolean getBlSegunda() {
        return this.blSegunda;
    }

    public void setBlSegunda(Boolean blSegunda) {
        this.blSegunda = blSegunda;
    }

    public Boolean getBlTerca() {
        return this.blTerca;
    }

    public void setBlTerca(Boolean blTerca) {
        this.blTerca = blTerca;
    }

    public Boolean getBlQuarta() {
        return this.blQuarta;
    }

    public void setBlQuarta(Boolean blQuarta) {
        this.blQuarta = blQuarta;
    }

    public Boolean getBlQuinta() {
        return this.blQuinta;
    }

    public void setBlQuinta(Boolean blQuinta) {
        this.blQuinta = blQuinta;
    }

    public Boolean getBlSexta() {
        return this.blSexta;
    }

    public void setBlSexta(Boolean blSexta) {
        this.blSexta = blSexta;
    }

    public Boolean getBlSabado() {
        return this.blSabado;
    }

    public void setBlSabado(Boolean blSabado) {
        this.blSabado = blSabado;
    }

    public TimeOfDay getHrDiariaInicial() {
        return this.hrDiariaInicial;
    }

    public void setHrDiariaInicial(TimeOfDay hrDiariaInicial) {
        this.hrDiariaInicial = hrDiariaInicial;
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

    public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
        return this.solicitacaoContratacao;
    }

	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
        this.solicitacaoContratacao = solicitacaoContratacao;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega getTipoTabelaColetaEntrega() {
        return this.tipoTabelaColetaEntrega;
    }

	public void setTipoTabelaColetaEntrega(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega) {
        this.tipoTabelaColetaEntrega = tipoTabelaColetaEntrega;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe.class)     
    public List<ParcelaTabelaCe> getParcelaTabelaCes() {
        return this.parcelaTabelaCes;
    }

	public void setParcelaTabelaCes(List parcelaTabelaCes) {
        this.parcelaTabelaCes = parcelaTabelaCes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTabelaColetaEntrega",
				getIdTabelaColetaEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaColetaEntrega))
			return false;
        TabelaColetaEntrega castOther = (TabelaColetaEntrega) other;
		return new EqualsBuilder().append(this.getIdTabelaColetaEntrega(),
				castOther.getIdTabelaColetaEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaColetaEntrega())
            .toHashCode();
    }

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public void setTpCalculo(DomainValue tpCalculo) {
		this.tpCalculo = tpCalculo;
}

	public DomainValue getTpCalculo() {
		return tpCalculo;
	}

	public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
		this.cliente = cliente;
	}

	public com.mercurio.lms.vendas.model.Cliente getCliente() {
		return cliente;
	}

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

    public List<NotaCreditoDocto> getNotaCreditoDoctos() {
        return notaCreditoDoctos;
}

    public void setNotaCreditoDoctos(List<NotaCreditoDocto> notaCreditoDoctos) {
        this.notaCreditoDoctos = notaCreditoDoctos;
    }

    public List<NotaCreditoColeta> getNotaCreditoColetas() {
        return notaCreditoColetas;
    }

    public void setNotaCreditoColetas(List<NotaCreditoColeta> notaCreditoColetas) {
        this.notaCreditoColetas = notaCreditoColetas;
    }

    public List<FaixaPesoParcelaTabelaCE> getFaixasPesoParcelaTabelaCE() {
        return faixasPesoParcelaTabelaCE;
    }

    public void setFaixasPesoParcelaTabelaCE(List<FaixaPesoParcelaTabelaCE> faixasPesoParcelaTabelaCE) {
        this.faixasPesoParcelaTabelaCE = faixasPesoParcelaTabelaCE;
    }

    public List<ParcelaTabelaCe> getParcelaTabelaCesRemove() {
		return parcelaTabelaCesRemove;
}
    
    public void setParcelaTabelaCesRemove(List parcelaTabelaCesRemove) {
		this.parcelaTabelaCesRemove = parcelaTabelaCesRemove;
	}

}

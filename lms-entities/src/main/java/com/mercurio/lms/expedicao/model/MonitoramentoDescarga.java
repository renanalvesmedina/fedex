
package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;

/**
 * @author LMS Custom Hibernate CodeGenerator
 */
public class MonitoramentoDescarga implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * identifier field
     */
    private Long idMonitoramentoDescarga;

    /**
     * persistent field
     */
    private Filial filial;

    /**
     * persistent field
     */
    private MeioTransporte meioTransporte;

    /**
     * persistent field
     */
    private DomainValue tpSituacaoDescarga;

    /**
     * persistent field
     */
    private Long qtVolumesTotal;

    /**
     * persistent field
     */
    private DateTime dhInicioDescarga;

    /**
     * persistent field
     */
    private DateTime dhFimDescarga;

    /**
     * persistent field
     */
    private DateTime dhUltimaAfericao;

    /**
     * persistent field
     */
    private DateTime dhEmissaoCTRC;

    /**
     * persistent field
     */
    private DateTime dhChegadaVeiculo;

    /**
     * persistent field
     */
    private String nrFrota;

    /**
     * persistent field
     */
    private String nrPlaca;

    /**
     * persistent field
     */
    private String nrProcessamento;

    private String dsInfColeta;

    private List<VolumeNotaFiscal> volumesNotaFiscal;
    private DateTime dhInclusao;

    public MonitoramentoDescarga() {
    }

    public MonitoramentoDescarga(Long idMonitoramentoDescarga, Filial filial, MeioTransporte meioTransporte, DomainValue tpSituacaoDescarga, Long qtVolumesTotal, DateTime dhInicioDescarga, DateTime dhFimDescarga, DateTime dhUltimaAfericao, DateTime dhEmissaoCTRC, DateTime dhChegadaVeiculo, String nrFrota, String nrPlaca, String nrProcessamento, String dsInfColeta, List<VolumeNotaFiscal> volumesNotaFiscal) {
        this.idMonitoramentoDescarga = idMonitoramentoDescarga;
        this.filial = filial;
        this.meioTransporte = meioTransporte;
        this.tpSituacaoDescarga = tpSituacaoDescarga;
        this.qtVolumesTotal = qtVolumesTotal;
        this.dhInicioDescarga = dhInicioDescarga;
        this.dhFimDescarga = dhFimDescarga;
        this.dhUltimaAfericao = dhUltimaAfericao;
        this.dhEmissaoCTRC = dhEmissaoCTRC;
        this.dhChegadaVeiculo = dhChegadaVeiculo;
        this.nrFrota = nrFrota;
        this.nrPlaca = nrPlaca;
        this.nrProcessamento = nrProcessamento;
        this.dsInfColeta = dsInfColeta;
        this.volumesNotaFiscal = volumesNotaFiscal;
    }

    public Filial getFilial() {
        return this.filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

    public void setMeioTransporte(MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public Long getIdMonitoramentoDescarga() {
        return this.idMonitoramentoDescarga;
    }

    public void setIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
        this.idMonitoramentoDescarga = idMonitoramentoDescarga;
    }

    public Long getQtVolumesTotal() {
        return this.qtVolumesTotal;
    }

    public void setQtVolumesTotal(Long qtVolumesTotal) {
        this.qtVolumesTotal = qtVolumesTotal;
    }

    public DateTime getDhInicioDescarga() {
        return this.dhInicioDescarga;
    }

    public void setDhInicioDescarga(DateTime dhInicioDescarga) {
        this.dhInicioDescarga = dhInicioDescarga;
    }

    public DateTime getDhFimDescarga() {
        return this.dhFimDescarga;
    }

    public void setDhFimDescarga(DateTime dhFimDescarga) {
        this.dhFimDescarga = dhFimDescarga;
    }

    public DateTime getDhUltimaAfericao() {
        return this.dhUltimaAfericao;
    }

    public void setDhUltimaAfericao(DateTime dhUltimaAfericao) {
        this.dhUltimaAfericao = dhUltimaAfericao;
    }

    public DateTime getDhEmissaoCTRC() {
        return this.dhEmissaoCTRC;
    }

    public void setDhEmissaoCTRC(DateTime dhEmissaoCTRC) {
        this.dhEmissaoCTRC = dhEmissaoCTRC;
    }

    public DomainValue getTpSituacaoDescarga() {
        return this.tpSituacaoDescarga;
    }

    public void setTpSituacaoDescarga(DomainValue tpSituacaoDescarga) {
        this.tpSituacaoDescarga = tpSituacaoDescarga;
    }

    public String getNrFrota() {
        return this.nrFrota;
    }

    public void setNrFrota(String nrFrota) {
        this.nrFrota = nrFrota;
    }

    public String getNrPlaca() {
        return this.nrPlaca;
    }

    public void setNrPlaca(String nrPlaca) {
        this.nrPlaca = nrPlaca;
    }

    public String getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(String nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public String toString() {
        return new ToStringBuilder(this).append("idMonitoramentoDescarga",
                getIdMonitoramentoDescarga()).toString();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MonitoramentoDescarga)) {
            return false;
        }
        MonitoramentoDescarga castOther = (MonitoramentoDescarga) other;
        return new EqualsBuilder().append(this.getIdMonitoramentoDescarga(),
                castOther.getIdMonitoramentoDescarga()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getIdMonitoramentoDescarga())
                .toHashCode();
    }

    public DateTime getDhChegadaVeiculo() {
        return dhChegadaVeiculo;
    }

    public void setDhChegadaVeiculo(DateTime dhChegadaVeiculo) {
        this.dhChegadaVeiculo = dhChegadaVeiculo;
    }

    public List<VolumeNotaFiscal> getVolumesNotaFiscal() {
        return volumesNotaFiscal;
    }

    public void setVolumesNotaFiscal(List<VolumeNotaFiscal> volumesNotaFiscal) {
        this.volumesNotaFiscal = volumesNotaFiscal;
    }

    public String getDsInfColeta() {
        return dsInfColeta;
    }

    public void setDsInfColeta(String dsInfColeta) {
        this.dsInfColeta = dsInfColeta;
    }

    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }
}

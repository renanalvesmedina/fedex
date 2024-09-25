package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ManifestoColeta implements Serializable {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idManifestoColeta;

    /** persistent field */
    private Integer nrManifesto;

    /** persistent field */
    private DomainValue tpStatusManifestoColeta;

    /** persistent field */
    private DateTime dhGeracao;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** persistent field */
	private Integer qtTotalColetasEmissao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List descargaManifestos;

    /** persistent field */
    private List pedidoColetas;

    /** persistent field */
    private List eventoManifestoColetas;

    public Long getIdManifestoColeta() {
        return this.idManifestoColeta;
    }

    public void setIdManifestoColeta(Long idManifestoColeta) {
        this.idManifestoColeta = idManifestoColeta;
    }

    public Integer getNrManifesto() {
        return this.nrManifesto;
    }

    public void setNrManifesto(Integer nrManifesto) {
        this.nrManifesto = nrManifesto;
    }

    public DomainValue getTpStatusManifestoColeta() {
        return this.tpStatusManifestoColeta;
    }

    public void setTpStatusManifestoColeta(DomainValue tpStatusManifestoColeta) {
        this.tpStatusManifestoColeta = tpStatusManifestoColeta;
    }

    public DateTime getDhGeracao() {
        return this.dhGeracao;
    }

    public void setDhGeracao(DateTime dhGeracao) {
        this.dhGeracao = dhGeracao;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DescargaManifesto.class)     
    public List getDescargaManifestos() {
        return this.descargaManifestos;
    }

    public void setDescargaManifestos(List descargaManifestos) {
        this.descargaManifestos = descargaManifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColeta.class)     
    public List<PedidoColeta> getPedidoColetas() {
        return this.pedidoColetas;
    }

    public void setPedidoColetas(List pedidoColetas) {
        this.pedidoColetas = pedidoColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.EventoManifestoColeta.class)     
    public List getEventoManifestoColetas() {
        return this.eventoManifestoColetas;
    }

    public void setEventoManifestoColetas(List eventoManifestoColetas) {
        this.eventoManifestoColetas = eventoManifestoColetas;
    }

    public Integer getQtTotalColetasEmissao() {
        return qtTotalColetasEmissao;
    }

    public void setQtTotalColetasEmissao(Integer qtTotalColetasEmissao) {
        this.qtTotalColetasEmissao = qtTotalColetasEmissao;
    }


    @Override
    public String toString() {
		return new ToStringBuilder(this).append("idManifestoColeta",
				getIdManifestoColeta()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_MANIFESTO_COLETA", idManifestoColeta)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("ID_ROTA_COLETA_ENTREGA", rotaColetaEntrega != null ? rotaColetaEntrega.getIdRotaColetaEntrega() : null)
				.append("ID_FILIAL_ORIGEM", filial != null ? filial.getIdFilial() : null)
				.append("NR_MANIFESTO", nrManifesto)
				.append("TP_STATUS_MANIFESTO_COLETA", tpStatusManifestoColeta != null ? tpStatusManifestoColeta.getValue() : null)
				.append("DH_GERACAO", dhGeracao)
				.append("DH_EMISSAO", dhEmissao)
				.append("QT_TOTAL_COLETAS_EMISSAO", qtTotalColetasEmissao)
				.toString();
	}

    @Override
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ManifestoColeta))
			return false;
        ManifestoColeta castOther = (ManifestoColeta) other;
		return new EqualsBuilder().append(this.getIdManifestoColeta(),
				castOther.getIdManifestoColeta()).isEquals();
    }

    @Override
    public int hashCode() {
		return new HashCodeBuilder().append(getIdManifestoColeta())
            .toHashCode();
    }

}

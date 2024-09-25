package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RotaColetaEntrega implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRotaColetaEntrega;

    /** persistent field */
    private Short nrRota;

    /** persistent field */
    private String dsRota;

    /** persistent field */
    private Integer nrKm;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List configuracaoAuditoriaFis;

    /** persistent field */
    private List rotaIntervaloCeps;

    /** persistent field */
    private List regiaoFilialRotaColEnts;

    /** persistent field */
    private List horarioPrevistoSaidaRotas;

    /** persistent field */
    private List doctoServicosByIdRotaColetaEntregaReal;

    /** persistent field */
    private List doctoServicosByIdRotaColetaEntregaSugerid;

    /** persistent field */
    private List rotaTipoMeioTransportes;

    /** persistent field */
    private List pedidoColetas;

    /** persistent field */
    private List postoPassagemRotaColEnts;

    /** persistent field */
    private List manifestoColetas;

    /** persistent field */
    private List controleCargas;

    public Long getIdRotaColetaEntrega() {
        return this.idRotaColetaEntrega;
    }

    public void setIdRotaColetaEntrega(Long idRotaColetaEntrega) {
        this.idRotaColetaEntrega = idRotaColetaEntrega;
    }

    public Short getNrRota() {
        return this.nrRota;
    }

    public void setNrRota(Short nrRota) {
        this.nrRota = nrRota;
    }

    public String getDsRota() {
        return this.dsRota;
    }

    public void setDsRota(String dsRota) {
        this.dsRota = dsRota;
    }

    public Integer getNrKm() {
        return this.nrKm;
    }

    public void setNrKm(Integer nrKm) {
        this.nrKm = nrKm;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

	public String getNumeroDescricaoRota(){
	    if (this.nrRota != null && this.dsRota != null){
	   		return this.nrRota + " - " + this.dsRota;
		}
	    
	    if (this.nrRota != null)
	       return this.nrRota.toString();
	    else 
	    	return "";
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

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil.class)     
    public List getConfiguracaoAuditoriaFis() {
        return this.configuracaoAuditoriaFis;
    }

    public void setConfiguracaoAuditoriaFis(List configuracaoAuditoriaFis) {
        this.configuracaoAuditoriaFis = configuracaoAuditoriaFis;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaIntervaloCep.class)     
    public List getRotaIntervaloCeps() {
        return this.rotaIntervaloCeps;
    }

    public void setRotaIntervaloCeps(List rotaIntervaloCeps) {
        this.rotaIntervaloCeps = rotaIntervaloCeps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt.class)     
    public List getRegiaoFilialRotaColEnts() {
        return this.regiaoFilialRotaColEnts;
    }

    public void setRegiaoFilialRotaColEnts(List regiaoFilialRotaColEnts) {
        this.regiaoFilialRotaColEnts = regiaoFilialRotaColEnts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HorarioPrevistoSaidaRota.class)     
    public List getHorarioPrevistoSaidaRotas() {
        return this.horarioPrevistoSaidaRotas;
    }

    public void setHorarioPrevistoSaidaRotas(List horarioPrevistoSaidaRotas) {
        this.horarioPrevistoSaidaRotas = horarioPrevistoSaidaRotas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServico.class)     
    public List getDoctoServicosByIdRotaColetaEntregaReal() {
        return this.doctoServicosByIdRotaColetaEntregaReal;
    }

	public void setDoctoServicosByIdRotaColetaEntregaReal(
			List doctoServicosByIdRotaColetaEntregaReal) {
        this.doctoServicosByIdRotaColetaEntregaReal = doctoServicosByIdRotaColetaEntregaReal;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServico.class)     
    public List getDoctoServicosByIdRotaColetaEntregaSugerid() {
        return this.doctoServicosByIdRotaColetaEntregaSugerid;
    }

	public void setDoctoServicosByIdRotaColetaEntregaSugerid(
			List doctoServicosByIdRotaColetaEntregaSugerid) {
        this.doctoServicosByIdRotaColetaEntregaSugerid = doctoServicosByIdRotaColetaEntregaSugerid;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaTipoMeioTransporte.class)     
    public List getRotaTipoMeioTransportes() {
        return this.rotaTipoMeioTransportes;
    }

    public void setRotaTipoMeioTransportes(List rotaTipoMeioTransportes) {
        this.rotaTipoMeioTransportes = rotaTipoMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColeta.class)     
    public List getPedidoColetas() {
        return this.pedidoColetas;
    }

    public void setPedidoColetas(List pedidoColetas) {
        this.pedidoColetas = pedidoColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoPassagemRotaColEnt.class)     
    public List getPostoPassagemRotaColEnts() {
        return this.postoPassagemRotaColEnts;
    }

    public void setPostoPassagemRotaColEnts(List postoPassagemRotaColEnts) {
        this.postoPassagemRotaColEnts = postoPassagemRotaColEnts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.ManifestoColeta.class)     
    public List getManifestoColetas() {
        return this.manifestoColetas;
    }

    public void setManifestoColetas(List manifestoColetas) {
        this.manifestoColetas = manifestoColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRotaColetaEntrega",
				getIdRotaColetaEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RotaColetaEntrega))
			return false;
        RotaColetaEntrega castOther = (RotaColetaEntrega) other;
		return new EqualsBuilder().append(this.getIdRotaColetaEntrega(),
				castOther.getIdRotaColetaEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRotaColetaEntrega())
            .toHashCode();
    }

}

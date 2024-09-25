package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.PostoAvancado;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.workflow.model.Pendencia;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CarregamentoDescarga implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCarregamentoDescarga;
    
    /** persistent field */
    private Integer versao;

    /** persistent field */
    private DomainValue tpOperacao;

    /** persistent field */
    private DomainValue tpStatusOperacao;
    
    /** persistent field */
    private DomainValue tpStatusWorkflow;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;

    /** nullable persistent field */
    private DateTime dhInicioOperacao;

    /** nullable persistent field */
    private DateTime dhFimOperacao;

    /** nullable persistent field */
    private DateTime dhCancelamentoOperacao;

    /** nullable persistent field */
    private String obOperacao;

    /** nullable persistent field */
    private String obCancelamento;
    
    /** nullable persistent field */
    private String obSemLacre;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioIniciado;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioFinalizado;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.MotivoCancelDescarga motivoCancelDescarga;

    /** nullable persistent field */
    private com.mercurio.lms.municipios.model.PostoAvancado postoAvancado;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Box box;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List equipeOperacoes;

    /** persistent field */
    private List carregamentoPreManifestos;

    /** persistent field */
    private List descargaManifestos;

    /** persistent field */
    private List dispCarregDescQtdes;

    /** persistent field */
    private List fotoCarregmtoDescargas;

    /** persistent field */
    private List dispCarregIdentificados;

    /** persistent field */
    private List justificativasDoctosNaoCarregados;

    public CarregamentoDescarga(Long idCarregamentoDescarga, Integer versao, DomainValue tpOperacao, DomainValue tpStatusOperacao, DomainValue tpStatusWorkflow, Pendencia pendencia, DateTime dhInicioOperacao, DateTime dhFimOperacao, DateTime dhCancelamentoOperacao, String obOperacao, String obCancelamento, String obSemLacre, ControleCarga controleCarga, Usuario usuarioByIdUsuarioIniciado, Usuario usuarioByIdUsuarioFinalizado, MotivoCancelDescarga motivoCancelDescarga, PostoAvancado postoAvancado, Box box, Filial filial, List equipeOperacoes, List carregamentoPreManifestos, List descargaManifestos, List dispCarregDescQtdes, List fotoCarregmtoDescargas, List dispCarregIdentificados, List justificativasDoctosNaoCarregados) {
        this.idCarregamentoDescarga = idCarregamentoDescarga;
        this.versao = versao;
        this.tpOperacao = tpOperacao;
        this.tpStatusOperacao = tpStatusOperacao;
        this.tpStatusWorkflow = tpStatusWorkflow;
        this.pendencia = pendencia;
        this.dhInicioOperacao = dhInicioOperacao;
        this.dhFimOperacao = dhFimOperacao;
        this.dhCancelamentoOperacao = dhCancelamentoOperacao;
        this.obOperacao = obOperacao;
        this.obCancelamento = obCancelamento;
        this.obSemLacre = obSemLacre;
        this.controleCarga = controleCarga;
        this.usuarioByIdUsuarioIniciado = usuarioByIdUsuarioIniciado;
        this.usuarioByIdUsuarioFinalizado = usuarioByIdUsuarioFinalizado;
        this.motivoCancelDescarga = motivoCancelDescarga;
        this.postoAvancado = postoAvancado;
        this.box = box;
        this.filial = filial;
        this.equipeOperacoes = equipeOperacoes;
        this.carregamentoPreManifestos = carregamentoPreManifestos;
        this.descargaManifestos = descargaManifestos;
        this.dispCarregDescQtdes = dispCarregDescQtdes;
        this.fotoCarregmtoDescargas = fotoCarregmtoDescargas;
        this.dispCarregIdentificados = dispCarregIdentificados;
        this.justificativasDoctosNaoCarregados = justificativasDoctosNaoCarregados;
    }

    public CarregamentoDescarga() {
    }

    public Long getIdCarregamentoDescarga() {
        return this.idCarregamentoDescarga;
    }

    public void setIdCarregamentoDescarga(Long idCarregamentoDescarga) {
        this.idCarregamentoDescarga = idCarregamentoDescarga;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}  
    
    public DomainValue getTpOperacao() {
        return this.tpOperacao;
    }

    public void setTpOperacao(DomainValue tpOperacao) {
        this.tpOperacao = tpOperacao;
    }

    public DomainValue getTpStatusOperacao() {
        return this.tpStatusOperacao;
    }

    public void setTpStatusOperacao(DomainValue tpStatusOperacao) {
        this.tpStatusOperacao = tpStatusOperacao;
    }

    public DateTime getDhInicioOperacao() {
        return this.dhInicioOperacao;
    }

    public void setDhInicioOperacao(DateTime dhInicioOperacao) {
        this.dhInicioOperacao = dhInicioOperacao;
    }

    public DateTime getDhFimOperacao() {
        return this.dhFimOperacao;
    }

    public void setDhFimOperacao(DateTime dhFimOperacao) {
        this.dhFimOperacao = dhFimOperacao;
    }

    public DateTime getDhCancelamentoOperacao() {
        return this.dhCancelamentoOperacao;
    }

    public void setDhCancelamentoOperacao(DateTime dhCancelamentoOperacao) {
        this.dhCancelamentoOperacao = dhCancelamentoOperacao;
    }

    public String getObOperacao() {
        return this.obOperacao;
    }

    public void setObOperacao(String obOperacao) {
        this.obOperacao = obOperacao;
    }

    public String getObCancelamento() {
        return this.obCancelamento;
    }

    public void setObCancelamento(String obCancelamento) {
        this.obCancelamento = obCancelamento;
    }
    
    public String getObSemLacre() {
		return obSemLacre;
	}

    public void setObSemLacre(String obSemLacre) {
		this.obSemLacre = obSemLacre;
	}

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioIniciado() {
        return this.usuarioByIdUsuarioIniciado;
    }

	public void setUsuarioByIdUsuarioIniciado(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioIniciado) {
        this.usuarioByIdUsuarioIniciado = usuarioByIdUsuarioIniciado;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioFinalizado() {
        return this.usuarioByIdUsuarioFinalizado;
    }

	public void setUsuarioByIdUsuarioFinalizado(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioFinalizado) {
        this.usuarioByIdUsuarioFinalizado = usuarioByIdUsuarioFinalizado;
    }

    public com.mercurio.lms.carregamento.model.MotivoCancelDescarga getMotivoCancelDescarga() {
        return this.motivoCancelDescarga;
    }

	public void setMotivoCancelDescarga(
			com.mercurio.lms.carregamento.model.MotivoCancelDescarga motivoCancelDescarga) {
        this.motivoCancelDescarga = motivoCancelDescarga;
    }

    public com.mercurio.lms.municipios.model.PostoAvancado getPostoAvancado() {
        return this.postoAvancado;
    }

	public void setPostoAvancado(
			com.mercurio.lms.municipios.model.PostoAvancado postoAvancado) {
        this.postoAvancado = postoAvancado;
    }

    public com.mercurio.lms.portaria.model.Box getBox() {
        return this.box;
    }

    public void setBox(com.mercurio.lms.portaria.model.Box box) {
        this.box = box;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EquipeOperacao.class)     
    public List getEquipeOperacoes() {
        return this.equipeOperacoes;
    }

    public void setEquipeOperacoes(List equipeOperacoes) {
        this.equipeOperacoes = equipeOperacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoPreManifesto.class)     
    public List getCarregamentoPreManifestos() {
        return this.carregamentoPreManifestos;
    }

    public void setCarregamentoPreManifestos(List carregamentoPreManifestos) {
        this.carregamentoPreManifestos = carregamentoPreManifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DescargaManifesto.class)     
    public List getDescargaManifestos() {
        return this.descargaManifestos;
    }

    public void setDescargaManifestos(List descargaManifestos) {
        this.descargaManifestos = descargaManifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregDescQtde.class)     
    public List getDispCarregDescQtdes() {
        return this.dispCarregDescQtdes;
    }

    public void setDispCarregDescQtdes(List dispCarregDescQtdes) {
        this.dispCarregDescQtdes = dispCarregDescQtdes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.FotoCarregmtoDescarga.class)     
    public List getFotoCarregmtoDescargas() {
        return this.fotoCarregmtoDescargas;
    }

    public void setFotoCarregmtoDescargas(List fotoCarregmtoDescargas) {
        this.fotoCarregmtoDescargas = fotoCarregmtoDescargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregIdentificado.class)     
    public List getDispCarregIdentificados() {
        return this.dispCarregIdentificados;
    }

    public void setDispCarregIdentificados(List dispCarregIdentificados) {
        this.dispCarregIdentificados = dispCarregIdentificados;
    }

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.JustificativaDoctoNaoCarregado.class) 
	public List getJustificativasDoctosNaoCarregados() {
		return justificativasDoctosNaoCarregados;
	}
	
	public void setJustificativasDoctosNaoCarregados(
			List justificativasDoctosNaoCarregados) {
		this.justificativasDoctosNaoCarregados = justificativasDoctosNaoCarregados;
	}
    
    public String toString() {
		return new ToStringBuilder(this).append("idCarregamentoDescarga",
				getIdCarregamentoDescarga()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_CARREGAMENTO_DESCARGA", idCarregamentoDescarga)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("TP_OPERACAO", tpOperacao != null ? tpOperacao.getValue() : null)
				.append("TP_STATUS_OPERACAO", tpStatusOperacao != null ? tpStatusOperacao.getValue() : null)
				.append("ID_BOX", box != null ? box.getIdBox() : null)
				.append("ID_POSTO_AVANCADO", postoAvancado != null ? postoAvancado.getIdPostoAvancado() : null)
				.append("ID_MOTIVO_CANCEL_DESCARGA", motivoCancelDescarga != null ? motivoCancelDescarga.getIdMotivoCancelDescarga() : null)
				.append("ID_USUARIO_INICIADO", usuarioByIdUsuarioIniciado != null ? usuarioByIdUsuarioIniciado.getIdUsuario() : null)
				.append("ID_USUARIO_FINALIZADO", usuarioByIdUsuarioFinalizado != null ? usuarioByIdUsuarioFinalizado.getIdUsuario() : null)
				.append("DH_INICIO_OPERACAO", dhInicioOperacao)
				.append("DH_FIM_OPERACAO", dhFimOperacao)
				.append("DH_CANCELAMENTO_OPERACAO", dhCancelamentoOperacao)
				.append("OB_OPERACAO", obOperacao)
				.append("OB_SEM_LACRE", obSemLacre)
				.append("OB_CANCELAMENTO", obCancelamento)
				.append("NR_VERSAO", versao)
				.append("ID_PENDENCIA", pendencia != null ? pendencia.getIdPendencia() : null)
				.append("TP_STATUS_WORKFLOW", tpStatusWorkflow != null ? tpStatusWorkflow.getValue() : null)
				.toString();
	}

    public DomainValue getTpStatusWorkflow() {
		return tpStatusWorkflow;
	}

	public void setTpStatusWorkflow(DomainValue tpStatusWorkflow) {
		this.tpStatusWorkflow = tpStatusWorkflow;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CarregamentoDescarga))
			return false;
        CarregamentoDescarga castOther = (CarregamentoDescarga) other;
		return new EqualsBuilder().append(this.getIdCarregamentoDescarga(),
				castOther.getIdCarregamentoDescarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCarregamentoDescarga())
            .toHashCode();
    }

}

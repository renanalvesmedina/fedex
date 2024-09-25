package com.mercurio.lms.pendencia.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.lms.sim.model.FaseProcesso;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaDoctoServico;

    /** persistent field */
    private DateTime dhBloqueio;

    /** nullable persistent field */
    private DateTime dhLiberacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioLiberacao;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioBloqueio;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorBloqueio;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorLiberacao;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** nullable persistent field */
    private com.mercurio.lms.pendencia.model.ComunicadoApreensao comunicadoApreensao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialLiberacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialBloqueio;

    private FaseProcesso faseProcesso;

    public OcorrenciaDoctoServico() {
    }

    public OcorrenciaDoctoServico(Long idOcorrenciaDoctoServico, DateTime dhBloqueio, DateTime dhLiberacao, Usuario usuarioLiberacao, Usuario usuarioBloqueio, OcorrenciaPendencia ocorrenciaPendenciaByIdOcorBloqueio, OcorrenciaPendencia ocorrenciaPendenciaByIdOcorLiberacao, DoctoServico doctoServico, ComunicadoApreensao comunicadoApreensao, Filial filialByIdFilialLiberacao, Filial filialByIdFilialBloqueio, FaseProcesso faseProcesso) {
        this.idOcorrenciaDoctoServico = idOcorrenciaDoctoServico;
        this.dhBloqueio = dhBloqueio;
        this.dhLiberacao = dhLiberacao;
        this.usuarioLiberacao = usuarioLiberacao;
        this.usuarioBloqueio = usuarioBloqueio;
        this.ocorrenciaPendenciaByIdOcorBloqueio = ocorrenciaPendenciaByIdOcorBloqueio;
        this.ocorrenciaPendenciaByIdOcorLiberacao = ocorrenciaPendenciaByIdOcorLiberacao;
        this.doctoServico = doctoServico;
        this.comunicadoApreensao = comunicadoApreensao;
        this.filialByIdFilialLiberacao = filialByIdFilialLiberacao;
        this.filialByIdFilialBloqueio = filialByIdFilialBloqueio;
        this.faseProcesso = faseProcesso;
    }

    public Long getIdOcorrenciaDoctoServico() {
        return this.idOcorrenciaDoctoServico;
    }

    public void setIdOcorrenciaDoctoServico(Long idOcorrenciaDoctoServico) {
        this.idOcorrenciaDoctoServico = idOcorrenciaDoctoServico;
    }

    public DateTime getDhBloqueio() {
        return this.dhBloqueio;
    }

    public void setDhBloqueio(DateTime dhBloqueio) {
        this.dhBloqueio = dhBloqueio;
    }

    public DateTime getDhLiberacao() {
        return this.dhLiberacao;
    }

    public void setDhLiberacao(DateTime dhLiberacao) {
        this.dhLiberacao = dhLiberacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioLiberacao() {
        return this.usuarioLiberacao;
    }

	public void setUsuarioLiberacao(
			com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuarioLiberacao = usuario;
    }

    public com.mercurio.lms.pendencia.model.OcorrenciaPendencia getOcorrenciaPendenciaByIdOcorBloqueio() {
        return this.ocorrenciaPendenciaByIdOcorBloqueio;
    }

	public void setOcorrenciaPendenciaByIdOcorBloqueio(
			com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorBloqueio) {
        this.ocorrenciaPendenciaByIdOcorBloqueio = ocorrenciaPendenciaByIdOcorBloqueio;
    }

    public com.mercurio.lms.pendencia.model.OcorrenciaPendencia getOcorrenciaPendenciaByIdOcorLiberacao() {
        return this.ocorrenciaPendenciaByIdOcorLiberacao;
    }

	public void setOcorrenciaPendenciaByIdOcorLiberacao(
			com.mercurio.lms.pendencia.model.OcorrenciaPendencia ocorrenciaPendenciaByIdOcorLiberacao) {
        this.ocorrenciaPendenciaByIdOcorLiberacao = ocorrenciaPendenciaByIdOcorLiberacao;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.pendencia.model.ComunicadoApreensao getComunicadoApreensao() {
        return this.comunicadoApreensao;
    }

	public void setComunicadoApreensao(
			com.mercurio.lms.pendencia.model.ComunicadoApreensao comunicadoApreensao) {
        this.comunicadoApreensao = comunicadoApreensao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialLiberacao() {
        return this.filialByIdFilialLiberacao;
    }

	public void setFilialByIdFilialLiberacao(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialLiberacao) {
        this.filialByIdFilialLiberacao = filialByIdFilialLiberacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialBloqueio() {
        return this.filialByIdFilialBloqueio;
    }

	public void setFilialByIdFilialBloqueio(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialBloqueio) {
        this.filialByIdFilialBloqueio = filialByIdFilialBloqueio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaDoctoServico",
				getIdOcorrenciaDoctoServico()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_OCORRENCIA_DOCTO_SERVICO", idOcorrenciaDoctoServico)
				.append("ID_DOCTO_SERVICO", doctoServico != null ? doctoServico.getIdDoctoServico() : null)
				.append("ID_OCOR_BLOQUEIO", ocorrenciaPendenciaByIdOcorBloqueio != null ? ocorrenciaPendenciaByIdOcorBloqueio.getIdOcorrenciaPendencia() : null)
				.append("ID_FILIAL_BLOQUEIO", filialByIdFilialBloqueio != null ? filialByIdFilialBloqueio.getIdFilial() : null)
				.append("ID_FILIAL_LIBERACAO", filialByIdFilialLiberacao != null ? filialByIdFilialLiberacao.getIdFilial() : null)
				.append("ID_COMUNICADO_APREENSAO", comunicadoApreensao != null ? comunicadoApreensao.getIdComunicadoApreensao() : null)
				.append("DH_BLOQUEIO", dhBloqueio)
				.append("ID_OCOR_LIBERACAO", ocorrenciaPendenciaByIdOcorLiberacao != null ? ocorrenciaPendenciaByIdOcorLiberacao.getIdOcorrenciaPendencia() : null)
				.append("DH_LIBERACAO", dhLiberacao)
				.append("ID_USUARIO_LIBERACAO", usuarioLiberacao != null ? usuarioLiberacao.getIdUsuario() : null)
				.append("ID_USUARIO_BLOQUEIO", usuarioBloqueio != null ? usuarioBloqueio.getIdUsuario() : null)
				.append("ID_FASE_PROCESSO", faseProcesso != null ? faseProcesso.getIdFaseProcesso() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaDoctoServico))
			return false;
        OcorrenciaDoctoServico castOther = (OcorrenciaDoctoServico) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaDoctoServico(),
				castOther.getIdOcorrenciaDoctoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaDoctoServico())
            .toHashCode();
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioBloqueio() {
        return usuarioBloqueio;
    }

	public void setUsuarioBloqueio(
			com.mercurio.lms.configuracoes.model.Usuario usuarioBloqueio) {
        this.usuarioBloqueio = usuarioBloqueio;
    }

	public FaseProcesso getFaseProcesso() {
		return faseProcesso;
}

	public void setFaseProcesso(FaseProcesso faseProcesso) {
		this.faseProcesso = faseProcesso;
	}

}

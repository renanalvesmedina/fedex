package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class Visita implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idVisita;

	/** identifier field */
	private Integer versao;   

	/** persistent field */
	private YearMonthDay dtVisita;

	/** persistent field */
	private YearMonthDay dtRegistro;

	/** persistent field */
	private TimeOfDay hrInicial;

	/** nullable persistent field */
	private TimeOfDay hrFinal;

	/** nullable persistent field */
	private YearMonthDay dtVisto;

	/** nullable persistent field */
	private String dsVisita;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private Usuario usuarioByIdUsuarioVisto;

	/** persistent field */
	private Usuario usuarioByIdUsuario;

	/** persistent field */
	private List<FuncionarioVisita> funcionarioVisitas;

	/** persistent field */
	private List<EtapaVisita> etapaVisitas;
	
	/** persistent field */
	private Filial filial;	

	/** persistent field */
	private Pendencia pendencia;

	private transient List<EtapaVisita> etapasRemovidas;

	private transient boolean blEnviarEmail;

	private transient boolean blProcessoAprovacao;

	public Long getIdVisita() {
		return this.idVisita;
	}

	public void setIdVisita(Long idVisita) {
		this.idVisita = idVisita;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public YearMonthDay getDtVisita() {
		return this.dtVisita;
	}

	public void setDtVisita(YearMonthDay dtVisita) {
		this.dtVisita = dtVisita;
	}

	public YearMonthDay getDtRegistro() {
		return this.dtRegistro;
	}

	public void setDtRegistro(YearMonthDay dtRegistro) {
		this.dtRegistro = dtRegistro;
	}

	public TimeOfDay getHrInicial() {
		return this.hrInicial;
	}

	public void setHrInicial(TimeOfDay hrInicial) {
		this.hrInicial = hrInicial;
	}

	public TimeOfDay getHrFinal() {
		return this.hrFinal;
	}

	public void setHrFinal(TimeOfDay hrFinal) {
		this.hrFinal = hrFinal;
	}

	public YearMonthDay getDtVisto() {
		return this.dtVisto;
	}

	public void setDtVisto(YearMonthDay dtVisto) {
		this.dtVisto = dtVisto;
	}

	public String getDsVisita() {
		return this.dsVisita;
	}

	public void setDsVisita(String dsVisita) {
		this.dsVisita = dsVisita;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuarioByIdUsuarioVisto() {
		return this.usuarioByIdUsuarioVisto;
	}

	public void setUsuarioByIdUsuarioVisto(Usuario usuarioByIdUsuarioVisto) {
		this.usuarioByIdUsuarioVisto = usuarioByIdUsuarioVisto;
	}

	public Usuario getUsuarioByIdUsuario() {
		return this.usuarioByIdUsuario;
	}

	public void setUsuarioByIdUsuario(Usuario usuarioByIdUsuario) {
		this.usuarioByIdUsuario = usuarioByIdUsuario;
	}

	@ParametrizedAttribute(type = FuncionarioVisita.class)
	public List<FuncionarioVisita> getFuncionarioVisitas() {
		return this.funcionarioVisitas;
	}

	public void setFuncionarioVisitas(List<FuncionarioVisita> funcionarioVisitas) {
		this.funcionarioVisitas = funcionarioVisitas;
	}

	@ParametrizedAttribute(type = EtapaVisita.class)
	public List<EtapaVisita> getEtapaVisitas() {
		return this.etapaVisitas;
	}

	public void setEtapaVisitas(List<EtapaVisita> etapaVisitas) {
		this.etapaVisitas = etapaVisitas;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}
	
	public List<EtapaVisita> getEtapasRemovidas() {
		return etapasRemovidas;
	}

	public void setEtapasRemovidas(List<EtapaVisita> etapasRemovidas) {
		this.etapasRemovidas = etapasRemovidas;
	}

	public boolean isEnviarEmail() {
		return blEnviarEmail;
	}

	public void setBlEnviarEmail(boolean blEnviarEmail) {
		this.blEnviarEmail = blEnviarEmail;
	}

	public boolean isProcessoAprovacao() {
		return blProcessoAprovacao;
	}

	public void setBlProcessoAprovacao(boolean blProcessoAprovacao) {
		this.blProcessoAprovacao = blProcessoAprovacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idVisita", getIdVisita())
			.toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Visita))
			return false;
		Visita castOther = (Visita) other;
		return new EqualsBuilder().append(this.getIdVisita(),
				castOther.getIdVisita()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdVisita()).toHashCode();
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

}

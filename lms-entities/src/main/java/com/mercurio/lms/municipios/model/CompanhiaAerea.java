package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class CompanhiaAerea implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

	private Long codigo;
	
	private String nome;
	
	private Long cnpj;
	
	private String inscricaoEstadual;
	
	private boolean indiCalc;
	
	private Integer obciCodigo;
	
	private Usuario usuarioInclusao;
	
	private DateTime dthrInclusao;
	
	private Usuario usuarioAlteracao;
	
	private DateTime dthrAlteracao;
	
    public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCnpj() {
		return cnpj;
	}

	public void setCnpj(Long cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public boolean isIndiCalc() {
		return indiCalc;
	}

	public void setIndiCalc(boolean indiCalc) {
		this.indiCalc = indiCalc;
	}

	public Integer getObciCodigo() {
		return obciCodigo;
	}

	public void setObciCodigo(Integer obciCodigo) {
		this.obciCodigo = obciCodigo;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public DateTime getDthrInclusao() {
		return dthrInclusao;
	}

	public void setDthrInclusao(DateTime dthrInclusao) {
		this.dthrInclusao = dthrInclusao;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public DateTime getDthrAlteracao() {
		return dthrAlteracao;
	}

	public void setDthrAlteracao(DateTime dthrAlteracao) {
		this.dthrAlteracao = dthrAlteracao;
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CompanhiaAerea))
			return false;
        CompanhiaAerea castOther = (CompanhiaAerea) other;
		return new EqualsBuilder().append(this.getCodigo(),
				castOther.getCodigo()).isEquals();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	public YearMonthDay getDtVigenciaFinal() {
		// TODO Auto-generated method stub
		return null;
	}

	public YearMonthDay getDtVigenciaInicial() {
		// TODO Auto-generated method stub
		return null;
	}

}

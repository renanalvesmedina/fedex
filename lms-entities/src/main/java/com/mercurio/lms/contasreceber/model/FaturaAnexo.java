package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;

public class FaturaAnexo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long idFaturaAnexo;
	private Usuario usuario;
	private Fatura fatura;
	private String dsAnexo;
	private DateTime dhCriacao;
	private byte[] dcArquivo;
	private DateTime dhModificacao;
	private DateTime dhEnvioQuestFat;
	private Boolean blEnvAnexoQuestFat;
	private Integer versao;
	
	public void setIdFaturaAnexo(Long idFaturaAnexo) {		
		this.idFaturaAnexo = idFaturaAnexo;
	}

	public Long getIdFaturaAnexo() {
		return idFaturaAnexo;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhModificacao(DateTime dhModificacao) {
		this.dhModificacao = dhModificacao;
	}

	public DateTime getDhModificacao() {
		return dhModificacao;
	}

	public void setDhEnvioQuestFat(DateTime dhEnvioQuestFat) {
		this.dhEnvioQuestFat = dhEnvioQuestFat;
	}

	public DateTime getDhEnvioQuestFat() {
		return dhEnvioQuestFat;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idFaturaAnexo",
				getIdFaturaAnexo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturaAnexo))
			return false;
        FaturaAnexo castOther = (FaturaAnexo) other;
		return new EqualsBuilder().append(this.getIdFaturaAnexo(),
				castOther.getIdFaturaAnexo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFaturaAnexo()).toHashCode();
    }

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setBlEnvAnexoQuestFat(Boolean blEnvAnexoQuestFat) {
		this.blEnvAnexoQuestFat = blEnvAnexoQuestFat;
	}

	public Boolean getBlEnvAnexoQuestFat() {
		return blEnvAnexoQuestFat;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Integer getVersao() {
		return versao;
	}	
}
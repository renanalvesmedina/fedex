package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;

public class BoletoAnexo implements Serializable {
	private static final long serialVersionUID = 1L;
    private Long idBoletoAnexo;
	private Boleto boleto;
    private Usuario usuario;
    private String dsAnexo;
    private DateTime dhCriacao;
    private byte[] dcArquivo;
    private DateTime dhModificacao;
    private DateTime dhEnvioQuestFat;
    private Boolean blEnvAnexoQuestFat;
    private Integer versao;
    
    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Long getIdBoletoAnexo() {
		return idBoletoAnexo;
	}

	public void setIdBoletoAnexo(Long idBoletoAnexo) {
		this.idBoletoAnexo = idBoletoAnexo;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public DateTime getDhCriacao() {
		return dhCriacao;
	}

	public void setDhCriacao(DateTime dhCriacao) {
		this.dhCriacao = dhCriacao;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}

	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public DateTime getDhModificacao() {
		return dhModificacao;
	}

	public void setDhModificacao(DateTime dhModificacao) {
		this.dhModificacao = dhModificacao;
	}

	public DateTime getDhEnvioQuestFat() {
		return dhEnvioQuestFat;
	}

	public void setDhEnvioQuestFat(DateTime dhEnvioQuestFat) {
		this.dhEnvioQuestFat = dhEnvioQuestFat;
	}

	public Boolean getBlEnvAnexoQuestFat() {
		return blEnvAnexoQuestFat;
	}

	public void setBlEnvAnexoQuestFat(Boolean blEnvAnexoQuestFat) {
		this.blEnvAnexoQuestFat = blEnvAnexoQuestFat;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idDescontoAnexo()",
				getIdBoletoAnexo()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BoletoAnexo))
			return false;
        BoletoAnexo castOther = (BoletoAnexo) other;
		return new EqualsBuilder().append(this.getIdBoletoAnexo(),
				castOther.getIdBoletoAnexo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBoletoAnexo()).toHashCode();
    }
    
}

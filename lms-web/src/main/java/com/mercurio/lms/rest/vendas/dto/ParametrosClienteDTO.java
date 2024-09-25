package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ParametrosClienteDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private DomainValue tipoPessoa;
    private DomainValue tipoIdentificacao;
    private String identificacao;
    private String nomeRazaoSocial;
    private String nomeFantasia;
    private String numeroConta;
    private DomainValue tipoCliente;
    private DomainValue situacao;
    private Boolean nfeConjulgada;
    private Boolean obrigaRG;
    private Boolean permiteBaixaPorVolume;
    private Boolean exigeComprovanteEntrega;
    private Boolean obrigaQuiz;
    private Boolean pemiteBaixasParciais;
    private Boolean permiteProdutoPerigoso;
    private Boolean permiteProdutoControladoPoliciaCivil;
    private Boolean permiteProdutoControladoPoliciaFederal;
    private Boolean permiteProdutoControladoExercito;
    private Boolean obrigaParentesco; 
    private Boolean naoPermiteSubcontratacao;
    private boolean enviaDocsFaturamentoNas;
    private Boolean validaCobrancDifTdeDest;
    private Boolean cobrancaTdeDiferenciada;
    private Boolean dificuldadeEntrega;

    public DomainValue getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(DomainValue tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public DomainValue getTipoIdentificacao() {
        return tipoIdentificacao;
    }

    public void setTipoIdentificacao(DomainValue tipoIdentificacao) {
        this.tipoIdentificacao = tipoIdentificacao;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public DomainValue getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(DomainValue tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public DomainValue getSituacao() {
        return situacao;
    }

    public void setSituacao(DomainValue situacao) {
        this.situacao = situacao;
    }

    public Boolean getNfeConjulgada() {
        return nfeConjulgada;
    }

    public void setNfeConjulgada(Boolean nfeConjulgada) {
        this.nfeConjulgada = nfeConjulgada;
    }

    public Boolean getObrigaRG() {
        return obrigaRG;
    }

    public void setObrigaRG(Boolean obrigaRG) {
        this.obrigaRG = obrigaRG;
    }

    public Boolean getPermiteBaixaPorVolume() {
        return permiteBaixaPorVolume;
    }

    public void setPermiteBaixaPorVolume(Boolean permiteBaixaPorVolume) {
        this.permiteBaixaPorVolume = permiteBaixaPorVolume;
    }

    public Boolean getExigeComprovanteEntrega() {
        return exigeComprovanteEntrega;
    }

    public void setExigeComprovanteEntrega(Boolean exigeComprovanteEntrega) {
        this.exigeComprovanteEntrega = exigeComprovanteEntrega;
    }

    public Boolean getObrigaQuiz() {
        return obrigaQuiz;
    }

    public void setObrigaQuiz(Boolean obrigaQuiz) {
        this.obrigaQuiz = obrigaQuiz;
    }

    public Boolean getPemiteBaixasParciais() {
        return pemiteBaixasParciais;
    }

    public void setPemiteBaixasParciais(Boolean pemiteBaixasParciais) {
        this.pemiteBaixasParciais = pemiteBaixasParciais;
    }

	public Boolean getPermiteProdutoPerigoso() {
		return permiteProdutoPerigoso;
	}

	public void setPermiteProdutoPerigoso(Boolean permiteProdutoPerigoso) {
		this.permiteProdutoPerigoso = permiteProdutoPerigoso;
	}

	public Boolean getPermiteProdutoControladoPoliciaCivil() {
		return permiteProdutoControladoPoliciaCivil;
	}

	public void setPermiteProdutoControladoPoliciaCivil(Boolean permiteProdutoControladoPoliciaCivil) {
		this.permiteProdutoControladoPoliciaCivil = permiteProdutoControladoPoliciaCivil;
	}

	public Boolean getPermiteProdutoControladoPoliciaFederal() {
		return permiteProdutoControladoPoliciaFederal;
	}

	public void setPermiteProdutoControladoPoliciaFederal(Boolean permiteProdutoControladoPoliciaFederal) {
		this.permiteProdutoControladoPoliciaFederal = permiteProdutoControladoPoliciaFederal;
	}

	public Boolean getPermiteProdutoControladoExercito() {
		return permiteProdutoControladoExercito;
	}

	public void setPermiteProdutoControladoExercito(Boolean permiteProdutoControladoExercito) {
		this.permiteProdutoControladoExercito = permiteProdutoControladoExercito;
	}

    public Boolean getObrigaParentesco() {
        return obrigaParentesco;
    }

    public void setObrigaParentesco(Boolean obrigaParentesco) {
        this.obrigaParentesco = obrigaParentesco;
    }

    public Boolean getNaoPermiteSubcontratacao() {
        return naoPermiteSubcontratacao;
    }

    public void setNaoPermiteSubcontratacao(Boolean naoPermiteSubcontratacao) {
        this.naoPermiteSubcontratacao = naoPermiteSubcontratacao;
    }

    public boolean getEnviaDocsFaturamentoNas() {
        return enviaDocsFaturamentoNas;
    }

    public void setEnviaDocsFaturamentoNas(boolean enviaDocsFaturamentoNas) {
        this.enviaDocsFaturamentoNas = enviaDocsFaturamentoNas;
    }

    public Boolean getValidaCobrancDifTdeDest() {
        return validaCobrancDifTdeDest;
    }

    public void setValidaCobrancDifTdeDest(Boolean validaCobrancDifTdeDest) {
        this.validaCobrancDifTdeDest = validaCobrancDifTdeDest;
    }

    public Boolean getCobrancaTdeDiferenciada() {
        return cobrancaTdeDiferenciada;
    }

    public void setCobrancaTdeDiferenciada(Boolean cobrancaTdeDiferenciada) {
        this.cobrancaTdeDiferenciada = cobrancaTdeDiferenciada;
    }

    public Boolean getDificuldadeEntrega() {
        return dificuldadeEntrega;
    }

    public void setDificuldadeEntrega(Boolean dificuldadeEntrega) {
        this.dificuldadeEntrega = dificuldadeEntrega;
    }

}

package com.mercurio.lms.layoutNfse.model.rps;



public class InfRps {
	
	private String Id;
	private String Versao;
	private String Competencia;
	private String DataEmissao;
	private String NaturezaOperacao;
	private String RegimeEspecialTributacao;
	private String OptanteSimplesNacional;
	private String IncentivoFiscal;
	private String Status;
	private String TributarMunicipio;
	private String TributarPrestador;
	
	private IdentificacaoRps IdentificacaoRps;
	private RpsSubstituido RpsSubstituido;
	private Servico Servicos;
	private Faturas Faturas;
	private Prestador Prestador;
	private Tomador Tomador;
	private Email Email;
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		this.Id = id;
	}
	public IdentificacaoRps getIdentificacaoRps() {
		return IdentificacaoRps;
	}
	public void setIdentificacaoRps(IdentificacaoRps identificacaoRps) {
		this.IdentificacaoRps = identificacaoRps;
	}

	public String getNaturezaOperacao() {
		return NaturezaOperacao;
	}
	public void setNaturezaOperacao(String naturezaOperacao) {
		this.NaturezaOperacao = naturezaOperacao;
	}
	public String getRegimeEspecialTributacao() {
		return RegimeEspecialTributacao;
	}
	public void setRegimeEspecialTributacao(String regimeEspecialTributacao) {
		this.RegimeEspecialTributacao = regimeEspecialTributacao;
	}

	public RpsSubstituido getRpsSubstituido() {
		return RpsSubstituido;
	}
	public void setRpsSubstituido(RpsSubstituido rpsSubstituido) {
		this.RpsSubstituido = rpsSubstituido;
	}

	public Prestador getPrestador() {
		return Prestador;
	}
	public void setPrestador(Prestador prestador) {
		this.Prestador = prestador;
	}
	public Tomador getTomador() {
		return Tomador;
	}
	public void setTomador(Tomador tomador) {
		this.Tomador = tomador;
	}
	public Servico getServicos() {
		return Servicos;
	}

	public void setServicos(Servico servicos) {
		this.Servicos = servicos;
	}
	
	public String getDataEmissao() {
		return DataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.DataEmissao = dataEmissao;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		this.Status = status;
	}
	public String getVersao() {
		return Versao;
	}
	public void setVersao(String versao) {
		this.Versao = versao;
	}
	public String getCompetencia() {
		return Competencia;
	}
	public void setCompetencia(String competencia) {
		this.Competencia = competencia;
	}
	public String getOptanteSimplesNacional() {
		return OptanteSimplesNacional;
	}
	public void setOptanteSimplesNacional(String optanteSimplesNacional) {
		this.OptanteSimplesNacional = optanteSimplesNacional;
	}
	public String getIncentivoFiscal() {
		return IncentivoFiscal;
	}
	public void setIncentivoFiscal(String incentivoFiscal) {
		this.IncentivoFiscal = incentivoFiscal;
	}
	public String getTributarMunicipio() {
		return TributarMunicipio;
	}
	public void setTributarMunicipio(String tributarMunicipio) {
		TributarMunicipio = tributarMunicipio;
	}
	public String getTributarPrestador() {
		return TributarPrestador;
	}
	public void setTributarPrestador(String tributarPrestador) {
		TributarPrestador = tributarPrestador;
	}
	public Email getEmail() {
		return Email;
	}
	public void setEmail(Email email) {
		Email = email;
	}
	
	public Faturas getFaturas() {
		return Faturas;
	}
	public void setFaturas(Faturas faturas) {
		Faturas = faturas;
	}
}

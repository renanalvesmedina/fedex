package com.mercurio.lms.layoutNfse.model.rps;


public class Servico {
	private Valores Valores;
	private AliquotasComplementares AliquotasComplementares;
	private String ItemListaServico;
	private String CodigoCnae;
	private String CodigoTributacaoMunicipio;
	private String Discriminacao;
	private String MunicipioIncidencia;
	private String MunicipioIncidenciaSiafi;
	private String DescricaoRPS;
	private String IssRetido;
	private String ResponsavelRetencao;
	private String MunicipioIncidenciaOutros;
	private String ServicoExportacao;
	private String EmpreitadaGlobal;
	private DadosComplementaresServico DadosComplementaresServico;
	
	public Valores getValores() {
		return Valores;
	}
	public void setValores(Valores valores) {
		this.Valores = valores;
	}
	public String getCodigoCnae() {
		return CodigoCnae;
	}
	public void setCodigoCnae(String codigoCnae) {
		this.CodigoCnae = codigoCnae;
	}
	public String getDiscriminacao() {
		return Discriminacao;
	}
	public void setDiscriminacao(String discriminacao) {
		this.Discriminacao = discriminacao;
	}
	public AliquotasComplementares getAliquotasComplementares() {
		return AliquotasComplementares;
	}
	public void setAliquotasComplementares(
			AliquotasComplementares aliquotasComplementares) {
		this.AliquotasComplementares = aliquotasComplementares;
	}
	public DadosComplementaresServico getDadosComplementaresServico() {
		return DadosComplementaresServico;
	}
	public void setDadosComplementaresServico(
			DadosComplementaresServico dadosComplementaresServico) {
		this.DadosComplementaresServico = dadosComplementaresServico;
	}
	public String getCodigoTributacaoMunicipio() {
		return CodigoTributacaoMunicipio;
	}
	public void setCodigoTributacaoMunicipio(String codigoTributacaoMunicipio) {
		CodigoTributacaoMunicipio = codigoTributacaoMunicipio;
	}
	public String getDescricaoRPS() {
		return DescricaoRPS;
	}
	public void setDescricaoRPS(String descricaoRPS) {
		DescricaoRPS = descricaoRPS;
	}
	public String getIssRetido() {
		return IssRetido;
	}
	public void setIssRetido(String issRetido) {
		IssRetido = issRetido;
	}
	public String getItemListaServico() {
		return ItemListaServico;
	}
	public void setItemListaServico(String itemListaServico) {
		ItemListaServico = itemListaServico;
	}
	public String getMunicipioIncidencia() {
		return MunicipioIncidencia;
	}
	public void setMunicipioIncidencia(String municipioIncidencia) {
		MunicipioIncidencia = municipioIncidencia;
	}
	public String getResponsavelRetencao() {
		return ResponsavelRetencao;
	}
	public void setResponsavelRetencao(String responsavelRetencao) {
		ResponsavelRetencao = responsavelRetencao;
	}
	
	public String getMunicipioIncidenciaSiafi() {
		return MunicipioIncidenciaSiafi;
	}
	
	public void setMunicipioIncidenciaSiafi(String municipioIncidenciaSiafi) {
		MunicipioIncidenciaSiafi = municipioIncidenciaSiafi;
	}
	
	public String getMunicipioIncidenciaOutros() {
		return MunicipioIncidenciaOutros;
	}
	public void setMunicipioIncidenciaOutros(String municipioIncidenciaOutros) {
		MunicipioIncidenciaOutros = municipioIncidenciaOutros;
	}
	public String getEmpreitadaGlobal() {
		return EmpreitadaGlobal;
	}
	public void setEmpreitadaGlobal(String empreitadaGlobal) {
		EmpreitadaGlobal = empreitadaGlobal;
	}
	public String getServicoExportacao() {
		return ServicoExportacao;
	}
	public void setServicoExportacao(String servicoExportacao) {
		ServicoExportacao = servicoExportacao;
	}
}

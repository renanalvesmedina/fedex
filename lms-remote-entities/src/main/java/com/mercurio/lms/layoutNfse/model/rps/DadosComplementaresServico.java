package com.mercurio.lms.layoutNfse.model.rps;


public class DadosComplementaresServico {
	
	private String TipoRecolhimento;
	private String MunicipioPrestacaoDescricao;
	private Integer SeriePrestacao;
	private ListaItens ListaItens;

	public String getTipoRecolhimento() {
		return TipoRecolhimento;
	}
	public void setTipoRecolhimento(String tipoRecolhimento) {
		TipoRecolhimento = tipoRecolhimento;
	}
	public String getMunicipioPrestacaoDescricao() {
		return MunicipioPrestacaoDescricao;
	}
	public void setMunicipioPrestacaoDescricao(String municipioPrestacaoDescricao) {
		MunicipioPrestacaoDescricao = municipioPrestacaoDescricao;
	}
	public Integer getSeriePrestacao() {
		return SeriePrestacao;
	}
	public void setSeriePrestacao(Integer seriePrestacao) {
		SeriePrestacao = seriePrestacao;
	}
	public ListaItens getListaItens() {
		return ListaItens;
	}
	public void setListaItens(ListaItens listaItens) {
		ListaItens = listaItens;
	}

}

package com.mercurio.lms.tabelaprecos.model.service.importacao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoImportacao {

	private List<String> mensagensResultadoImportacao;
	private List<String> mensagensResultadoAtualizacaoAutomatica;
	private boolean sucessoResultadoImportacao;

	public ResultadoImportacao(boolean sucesso) {
		this.mensagensResultadoImportacao = new ArrayList<String>();
		this.mensagensResultadoAtualizacaoAutomatica = new ArrayList<String>();
		this.sucessoResultadoImportacao = sucesso;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> mensagens() {
		return Collections.unmodifiableList(this.mensagensResultadoImportacao);
	}
	
	@SuppressWarnings("unchecked")
	public List<String> mensagensAtualizacao() {
		return Collections.unmodifiableList(this.mensagensResultadoAtualizacaoAutomatica);
	}
	
	public void incluiMensagemAtualizacao(String mensagem) {
		if (this.mensagensResultadoAtualizacaoAutomatica == null) {
			this.mensagensResultadoAtualizacaoAutomatica = new ArrayList<String>();
		}
		this.mensagensResultadoAtualizacaoAutomatica.add(mensagem);
	}
	
	public void incluiMensagem(String mensagem) {
		if (this.mensagensResultadoImportacao == null) {
			this.mensagensResultadoImportacao = new ArrayList<String>();
		}
		this.mensagensResultadoImportacao.add(mensagem);
	}
	
	public void incluiMensagens(List<String> erros) {
		if (this.mensagensResultadoImportacao == null) {
			this.mensagensResultadoImportacao = new ArrayList<String>();
		}
		this.mensagensResultadoImportacao.addAll(erros);
	}
	
	public boolean sucesso() {
		return this.sucessoResultadoImportacao;
	}


}

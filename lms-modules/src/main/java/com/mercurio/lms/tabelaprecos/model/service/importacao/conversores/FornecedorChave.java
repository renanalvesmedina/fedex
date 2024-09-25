package com.mercurio.lms.tabelaprecos.model.service.importacao.conversores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;

public abstract class FornecedorChave {

	private Map<ChaveProgressao, TradutorChaveProgressao> chavesProcessadas;
	private Set<ChaveProgressao> chavesComProblema;
	
	public boolean chaveJaProcessada(ChaveProgressao chaveProgressao) {
		this.inicializaCacheChavesProcessadas();
		return this.chavesProcessadas.containsKey(chaveProgressao);
	}

	private void inicializaCacheChavesProcessadas() {
		if (this.chavesProcessadas == null) {
			this.chavesProcessadas = new HashMap<ChaveProgressao, TradutorChaveProgressao>();
		}
	}
	
	public boolean chaveComProblema(ChaveProgressao chaveProgressao){
		this.inicializaCacheChavesComProblemas();
		return this.chavesComProblema.contains(chaveProgressao);
	}
	
	private void inicializaCacheChavesComProblemas() {
		if (this.chavesComProblema == null) {
			this.chavesComProblema = new HashSet<ChaveProgressao>();
		}		
	}
	
	private void incluiChaveJaProcessada(ChaveProgressao chaveProgressao, TradutorChaveProgressao tradutor) {
		this.inicializaCacheChavesProcessadas();
		this.chavesProcessadas.put(chaveProgressao, tradutor);
	}
	
	private void incluiChaveComProblema(ChaveProgressao chaveProgressao) {
		this.inicializaCacheChavesComProblemas();
		this.chavesComProblema.add(chaveProgressao);
	}
	
	protected abstract TradutorChaveProgressao executaForneceChave(ChaveProgressao chaveProgressao) ;
	public abstract void inicializa();

	public TradutorChaveProgressao forneceChave(ChaveProgressao chaveProgressao) {
		if (this.chaveJaProcessada(chaveProgressao)){
			return this.chavesProcessadas.get(chaveProgressao);
		}
		try {
			TradutorChaveProgressao tradutor = this.executaForneceChave(chaveProgressao);
			this.incluiChaveJaProcessada(chaveProgressao, tradutor);
			return tradutor;
		} catch (RuntimeException e) {
			this.incluiChaveComProblema(chaveProgressao);
			throw e;
		}
	}
	
	public abstract void persisteCache();

}

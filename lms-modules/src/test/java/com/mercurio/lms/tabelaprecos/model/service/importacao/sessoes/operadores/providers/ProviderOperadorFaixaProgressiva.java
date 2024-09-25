package com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.providers;

import java.util.Collections;
import java.util.List;

import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.RotaImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.TagImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.componentes.ValorImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ChaveProgressao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.ComponenteImportacao.TipoComponente;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.OperadorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.importacao.sessoes.operadores.OperadorObservavel;

public class ProviderOperadorFaixaProgressiva {
	
	public OperadorFaixaProgressiva operadorCom3FaixasFuncionais() {
		OperadorFaixaProgressiva operador = new OperadorFaixaProgressiva();
		incluiTags(operador, 3, true);
		incluiFaixas(operador, 3, true);
		incluiValores3FaixasOk(operador);
		return operador;
	}
	
	public OperadorFaixaProgressiva operadorCom1FaixaFuncional() {
		OperadorFaixaProgressiva operador = new OperadorFaixaProgressiva();
		OperadorFake observavel = new OperadorFake();
		operador.incluiTag(1, 4, "[#FRPE_FXP_UNMED]");
		operador.incluiTag(1, 5, "[#FRPE_FXP_TPINDCL]");
		operador.incluiTag(1, 6, "[#FRPE_FXP_MINPROG]");
		operador.incluiTag(1, 7, "[#FRPE_FXP_PRDESP]");
		operador.incluiTag(1, 8, "[#FRPE_FXP_PRDESP]");
		operador.incluiTag(1, 9, "[#FRPE_FXP_PRDESP]");
		operador.incluiTag(1, 10, "[#FRPE_FXP_PRDESP]");
		operador.incluiTag(1, 11, "[#FRPE_FXP_VLFXPROG]");
		operador.incluiTag(1, 12, "[#FRPE_FXP_VLFXPROG]");
		operador.validaTags();
		operador.incluiFaixa(2, 4, "");
		operador.incluiFaixa(2, 5, "");
		operador.incluiFaixa(2, 6, "");
		operador.incluiFaixa(2, 7, "10");
		operador.incluiFaixa(2, 8, "7");
		operador.incluiFaixa(2, 9, "8");
		operador.incluiFaixa(2, 10, "9");
		operador.incluiFaixa(2, 11, "25.5");
		operador.incluiFaixa(2, 12, "50.5");
		operador.encerraLinha();
		observavel.novaLinha(3, "STM", "BVB", "PA", "RR");
		operador.incluiValor(3, 4, "4");
		operador.incluiValor(3, 5, "VL");
		operador.incluiValor(3, 6, "PE");
		operador.incluiValor(3, 7, "0.1");
		operador.incluiValor(3, 8, "0.2");
		operador.incluiValor(3, 9, "0.3");
		operador.incluiValor(3, 10, "0.4");
		operador.incluiValor(3, 11, "14.97");
		operador.incluiValor(3, 12, "13.19");
		operador.atualiza(observavel);
		operador.encerraLinha();
		operador.reinicia();
		return operador;
	}

	private void incluiTags(OperadorFaixaProgressiva operador, int faixas, boolean pesoMinimo) {
		operador.incluiTag(1, 4, "[#FRPE_FXP_MINPROG]");
		operador.incluiTag(1, 11, "[#FRPE_FXP_TPINDCL]");
		operador.incluiTag(1, 12, "[#FRPE_FXP_UNMED]");
		int indiceInicial = 4;
		for(int i = 0; i < faixas; i++) {
			operador.incluiTag(1, ++indiceInicial, "[#FRPE_FXP_VLFXPROG]");
		}
		if (pesoMinimo) {
			for(int i = 0; i < faixas; i++) {
				operador.incluiTag(1, ++indiceInicial, "[#FRPE_FXP_PSMIN]");
			}
		}
		operador.validaTags();
	}


	private void incluiFaixas(OperadorFaixaProgressiva operador, int faixas, boolean pesoMinimo) {
		operador.incluiFaixa(2, 4, "");
		operador.incluiFaixa(2, 11, "");
		operador.incluiFaixa(2, 12, "");
		int indiceInicial = 4;
		for(int i = 0; i < faixas; i++) {
			operador.incluiFaixa(2, ++indiceInicial, String.valueOf((i+1)*20d));
		}
		if (pesoMinimo) {
			for(int i = 0; i < faixas; i++) {
				operador.incluiFaixa(2, ++indiceInicial, String.valueOf((i+1)*20d));
			}
		}
		operador.encerraLinha();
	}


	private void incluiValores3FaixasOk(OperadorFaixaProgressiva operador) {
		OperadorFake observavel = new OperadorFake();
		observavel.novaLinha(3, "AJU", "BEL", "SE", "PA");
		operador.incluiValor(3, 4, "PE");
		operador.incluiValor(3, 5, "1.0");
		operador.incluiValor(3, 6, "2.0");
		operador.incluiValor(3, 7, "3.0");
		operador.incluiValor(3, 8, "1.5");
		operador.incluiValor(3, 9, "2.5");
		operador.incluiValor(3, 10, "3.5");
		operador.incluiValor(3, 11, "PE");
		operador.incluiValor(3, 12, "4");
		operador.atualiza(observavel);
		operador.encerraLinha();
		operador.reinicia();
		observavel.novaLinha(4,  "AJU", "POA", "SE", "RS");
		operador.incluiValor(4, 4, "PE");
		operador.incluiValor(4, 5, "1.1");
		operador.incluiValor(4, 6, "2.1");
		operador.incluiValor(4, 7, "3.1");
		operador.incluiValor(4, 8, "1.6");
		operador.incluiValor(4, 9, "2.6");
		operador.incluiValor(4, 10, "3.6");
		operador.incluiValor(4, 11, "PE");
		operador.incluiValor(4, 12, "4");
		operador.atualiza(observavel);
		operador.encerraLinha();
		operador.reinicia();
		observavel.novaLinha(5,  "AJU", "CGH", "SE", "SP");
		operador.incluiValor(5, 4, "PE");
		operador.incluiValor(5, 5, "1.2");
		operador.incluiValor(5, 6, "2.2");
		operador.incluiValor(5, 7, "3.2");
		operador.incluiValor(5, 8, "1.7");
		operador.incluiValor(5, 9, "2.7");
		operador.incluiValor(5, 10, "3.7");
		operador.incluiValor(5, 11, "PE");
		operador.incluiValor(5, 12, "4");
		operador.atualiza(observavel);
		operador.encerraLinha();
		operador.reinicia();

	}
	
	private class OperadorFake extends OperadorObservavel {
		
		private int linha;
		private String aeroOrigem;
		private String aeroDestino;
		private String ufOrigem;
		private String ufDestino;

		public void novaLinha(int linha, String aeroOrigem, String aeroDestino, String ufOrigem, String ufDestino) {
			this.linha = linha;
			this.aeroOrigem = aeroOrigem;
			this.aeroDestino = aeroDestino;
			this.ufOrigem = ufOrigem;
			this.ufDestino = ufDestino;
			
		}
		
		@Override
		public void validaTags() {
			//não faz nada
		}
		@Override
		public TipoComponente tipoOperador() {
			return null;
		}
		@Override
		public List<ComponenteImportacao> resultadoImportacao() {
			return Collections.emptyList();
		}
		
		@Override
		public void reinicia() {
			//não faz nada
		}
		
		@Override
		public boolean incluiTag(int linha, int coluna, String valor) {
			return false;
		}
		
		@Override
		protected ChaveProgressao retornaChave() {
			RotaImportacao rota = new RotaImportacao();
			rota.incluiValor(new ValorImportacao(linha, 0, aeroOrigem, new TagImportacao(1, 0, "[#AERORIGEM]")));
			rota.incluiValor(new ValorImportacao(linha, 1, aeroDestino, new TagImportacao(1, 1, "[#AERODESTINO]")));
			rota.incluiValor(new ValorImportacao(linha, 2, ufOrigem, new TagImportacao(1, 2, "[#UFORIGEM]")));
			rota.incluiValor(new ValorImportacao(linha, 3, ufDestino, new TagImportacao(1, 3, "[#UFDESTINO]")));
			return rota;
		}
		
		@Override
		protected boolean executaIncluiValor(int linha, int coluna, String valor) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		protected boolean estahCompleto() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		protected void executaEncerraLinha() {
			// TODO Auto-generated method stub
			
		}
	}

}
